var cmdefstock = {};
cmdefstock.user={};
cmdefstock.locno;

//========================库区代码======================================
//选择库区的时候  要加载 通道数据--用于查询栏
cmdefstock.selectType = function(){
	var locno = cmdefstock.locno;
	var wareNo = $("#wareNo").combobox("getValue");
	var areaNo = $("#areaNo").combobox("getValue");
	cmdefstock.ajaxRequest(BasePath+'/cm_defarea/get_biz',{"locno":locno,"wareNo":wareNo,"areaNo":areaNo},true,cmdefstock.initSelectType);
};
cmdefstock.initSelectType = function(data){
	if(data && data[0]){
		$('#areaQualityform').combobox("select",data[0].areaQuality);
		$('#itemType').combobox("select",data[0].itemType);
	}
};
//========================库区代码======================================

cmdefstock.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
	      async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

/**
 * 设置伪disable或enable
 * @param id 
 * @param type 可选 'disable' or 'enable'
 */
function inputDisableOrEnable(id,type){
	var obj = $("#"+id);
	if(type == "disable"){
		obj.attr('readonly',true);
		obj.css('background','#D4D0C8');
		obj.css('color','#808080');
	}else if(type == "enable"){
		obj.attr('readonly',false);
		obj.css('background','#fff');
		obj.css('color','#000');
	}
}






cmdefstock.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/cm_defstock/list.json?locno='+cmdefstock.locno,'title':'通道列表','pageNumber':1 });
};

cmdefstock.findOwner = function(rowData){
	var url = BasePath + '/cm_defcell/OwnerNoByStockNo';
	var params = {
		locno:rowData.locno,
		wareNo:rowData.wareNo,
		areaNo:rowData.areaNo,
		stockNo:rowData.stockNo
	};
	var result = "";
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: params,
		  cache: false,
		  async:false, // 一定要
		  success: function(ownerNo){
			  result = ownerNo;
		  }
   });
	return result;
};

cmdefstock.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
   var stockNo = $('#stockNo').val();
     //2.绑定数据
   wms_city_common.loading("show","正在修改通道......");
    var url = BasePath+'/cm_defstock/moditfyCmDefstock';
    fromObj.form('submit', {
		url: url,
		onSubmit: function(param){
			param.editor = cmdefstock.user.loginName;
			param.editorName = cmdefstock.user.username;//修改人中文名称
			//param.edittm = cmdefstock.user.currentDate19Str;
			param.locno = cmdefstock.user.locno;
			//param.stockNo = stockNo;
		},
		success: function(r){
			if(r){
				 alert('修改成功!');
				 //3.保存成功,清空表单
				 cmdefstock.loadDataGrid();
				cmdefstock.closeUI();
			}else{
				alert('修改失败,请联系管理员!',2);
			}
			wms_city_common.loading();
	    },
		error:function(){
			alert('修改失败,请联系管理员!',2);
			wms_city_common.loading();
		}
   });
};




//cmdefstock.onChangeNum = function(value){
	///alert("aaaa");
//};
//cmdefstock.initOwnerNo = function(data){
//	$('#ownerNo').combobox({
//		valueField:"ownerNo",
//	     textField:"ownerNameForAdd",
//	     data:data,
//	     panelHeight:150,
//	     loadFilter:function(data){
//		    	if(data){
//		       		 for(var i = 0;i<data.length;i++){
//		       			 var tempData = data[i];
//		       			 tempData.ownerNameForAdd = tempData.ownerNo+'→'+tempData.ownerName;
//		       		 }
//		       	 }
//		    	return data;
//		    }
//	});
//};

////==================商品类型=======================
//cmdefstock.itemType = {};
//cmdefstock.itemTypeFormatter = function(value, rowData, rowIndex){
//	return cmdefstock.itemType[value];
//};
////==================商品品质=======================
//cmdefstock.areaQuality = {};
//cmdefstock.areaQualityFormatter = function(value, rowData, rowIndex){
//	return cmdefstock.areaQuality[value];
//};
//==================储区类型=======================
cmdefstock.areaType = {};
cmdefstock.areaTypeFormatter = function(value, rowData, rowIndex){
	return cmdefstock.areaType[value];
};
//==================储区类型=======================
//==================通道状态=======================
cmdefstock.stockStatus = {};
cmdefstock.stockStatusFormatter = function(value, rowData, rowIndex){
	return cmdefstock.stockStatus[value];
};
//==================通道状态=======================
//==================混载标志=======================
cmdefstock.mixFlag = {};
cmdefstock.mixFlagFormatter = function(value, rowData, rowIndex){
	return cmdefstock.mixFlag[value];
};
//==================混载标志=======================
//==================试算标识=======================
cmdefstock.bPick = {};
cmdefstock.bPickFormatter = function(value, rowData, rowIndex){
	return cmdefstock.bPick[value];
};
//==================试算标识=======================

//***********************************************************************//
//***********************************************************************//
//******************************初始化BEGIN******************************//
$(document).ready(function(){
	cmdefstock.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(u){cmdefstock.user=u;cmdefstock.locno = u.locno;});
	
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["ownerNo","ownerNo_new"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[false,false],
			BasePath+'/entrust_owner/get_biz',
			{},
			null,
			null);
	//初始化仓区
	wms_city_common.initWareNoForCascade(
			cmdefstock.locno,
			['wareNoCondition','wareNo','wareNo_new'],
			['areaNoCondition','areaNo','areaNo_new'],
			['stockNoCondition',null],
			null,
			[true,false,false],
			null
			);
	//初始化库区类型
	wms_city_common.comboboxLoadFilter(
			["areaTypeCondition","areaType","areaType_new"],
			null,
			null,
			null,
			true,
			[true,false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_TYPE',
			{},
			cmdefstock.areaType,
			null);
	//初始化通道状态
	wms_city_common.comboboxLoadFilter(
			["stockStatus","stockStatus_new"],
			null,
			null,
			null,
			true,
			[false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=STOCK_STATUS',
			{},
			cmdefstock.stockStatus,
			null);
	//初始化混载标志
	wms_city_common.comboboxLoadFilter(
			["mixFlagCondition","mixFlag","mixFlag_new"],
			null,
			null,
			null,
			true,
			[true,false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=MIX_FLAG',
			{},
			cmdefstock.mixFlag,
			null);
	//初始化试算标志
	wms_city_common.comboboxLoadFilter(
			["bPickCondition","bPick","bPick_new"],
			null,
			null,
			null,
			true,
			[true,false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=B_PICK',
			{},
			cmdefstock.bPick,
			null);
	wms_city_common.initItemTypeAndQuality("itemType","areaQualityform");
	wms_city_common.initItemTypeAndQuality("itemType_new","areaQualityform_new");
	wms_city_common.closeTip("openUI");
	wms_city_common.closeTip("newUI");
});
//主要是推荐这个函数。它将jquery系列化后的值转为name:value的形式。
function convertArray(o) { 
	var v = {};
	for ( var i in o) {
		if (typeof (v[o[i].name]) == 'undefined')
			v[o[i].name] = o[i].value;
		else
			v[o[i].name] += "," + o[i].value;
	}
	return JSON.stringify(v);
}

//******************************初始化END******************************//
//******************************主页面BEGIN******************************//

//查询
cmdefstock.searchCell = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/cm_defstock/list.json?locno='+cmdefstock.locno;
  $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
  $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
  $( "#dataGridJG").datagrid( 'load' );
};
//清除
cmdefstock.searchClear = function(){
	$('#searchForm').form("clear");
	searchForm('/cm_defstock/list.json?locno='+cmdefstock.locno);
};
//删除
cmdefstock.deleteRows = function(){
	//1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	var keys = [];
	$.each(checkedRows, function(index, item){
		keys[keys.length] = {locno:item.locno,wareNo:item.wareNo,areaNo:item.areaNo,stockNo:item.stockNo};
		//keys.push(item.locno+"-"+item.wareNo+"-"+item.areaNo+"-"+item.stockNo);
	});   
   //2.绑定数据
   var url = BasePath+'/cm_defstock/delete_records';
   var data={
			 locno:cmdefstock.locno,
			 datas:JSON.stringify(keys)
	 };
   
//	 var data={
//			    "ids":keys.join(",")
//	  };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		    if (r){   
		    	wms_city_common.loading("show", "正在删除通道......");
			   	 cmdefstock.ajaxRequest(url,data,true,function(result){
//					 if(result=='success'){
//						 //4.删除成功,清空表单
//						 cmdefstock.loadDataGrid();
//						 alert('删除成功!');
//					 }else{
//						 alert('删除失败,请联系管理员!',2);
//					 }
					 
					 if(result.flag=='success'){
						 //4.删除成功,清空表单
						 cmdefstock.loadDataGrid();
						 alert('删除成功!');
					 }else if(result.flag=='warn'){
						 alert("通道：【"+result.msg+"】存在储位明细,不能删除!");
					 }else if(result.flag=='hasSettingNo'){
							alert("通道：【"+result.msg+"】已被上架策略【"+result.settingNo+"】引用,不能删除!");
						}else{
						 alert('删除失败,请联系管理员!',2);
					 }
					 wms_city_common.loading();
				}); 
			   	 
			   	 
		    }    
		});  



};
//******************************主页面END******************************//
//******************************新增BEGIN******************************//
cmdefstock.checkStock = function(stockStatus, wareNo, areaNo, stockNo){
	var result = false;
	if(stockStatus!=1) {
		return true;
	}
	var locNo = cmdefstock.locno;
	$.ajax({
		  async : false,
		  type: 'POST',
		  dataType : "json",
		  url: BasePath+'/cm_defstock/checkStock',
		  data:{
			"locno":locNo,
			"wareNo":wareNo,
			"areaNo":areaNo,
			"stockNo":stockNo,
			"stockStatus":stockStatus
		  },
		  cache: true,
		  success: function(data){
			if(data.result=="success"){
				result = true;
			}else if(data.result=="exist"){
				alert("请检查通道关联储位");
				result = false;
			}else if(data.result=="exist"){
				alert("检查通道出错");
				result = false;
			}
		  }
	});
	return result;
};
//关闭新增
cmdefstock.closeAddUI = function(opt){
	$("#newUI").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#newUI').window('close');
	$("#opt").val("");
};
//打开新增
cmdefstock.addUI = function(){
	$('#newUI').window({
		title:"新增"
	});
	$('#newUI').window('open');
	$('#newForm').form("clear");
	$("#add_save").show();
	$("#opt").val("add");
};
//保存新增
cmdefstock.addManage = function(){
	var ownerNo = $('#ownerNo_new').combobox('getValue');
	var wareNo = $("#wareNo_new").combobox("getValue");
	var areaNo = $("#areaNo_new").combobox("getValue");
	var areaType = $('#areaType_new').combobox('getValue');
	var stockNo = $("#stockNo_new").val();
	
	if(ownerNo != null && ownerNo.length <1) {
		alert('货主不允许为空!',1);
        return;
	}
	if(wareNo != null && wareNo.length <1) {
		alert('仓区不允许为空!',1);
        return;
	}
	if(areaNo != null && areaNo.length <1) {
		alert('库区不允许为空!',1);
        return;
	}
	if(areaType != null && areaType.length <1) {
		alert('库区类型不允许为空!',1);
        return;
	}
	if(stockNo != null && stockNo.length <1) {
		alert('通道编码不允许为空!',1);
        return;
	}
	if(!cmdefstock.checkStock(stockStatus, wareNo, areaNo, stockNo)){
      	return;
    } else {
    	//2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
    	var checkUrl=BasePath+'/cm_defstock/get_count.json';
    	var checkDataNo={"locno":cmdefstock.locno,"wareNo":wareNo,"areaNo":areaNo,"stockNo":stockNo};
    	if(cmdefstock.checkExistFun(checkUrl,checkDataNo)){
    		alert('通道已存在,不能重复!',1);
    		$("#locno").focus();
    		return;
    	}
    	cmdefstock.save();
    	
    }
};
cmdefstock.save = function(){
	var fromObj=$('#newForm'); 
	//1.校验必填项
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
		return ;
	}
	
	$.messager.confirm('确认', '您确认保存通道吗？', function(r) {
		if (r) {
			// 3. 保存
			wms_city_common.loading("show","正在保存通道......");
			var url = BasePath+'/cm_defstock/cascade_save';
			fromObj.form('submit', {
				url : url,
				onSubmit : function(param) {
					param.locno = cmdefstock.user.locno;
					param.creator = cmdefstock.user.loginName;
					param.editor = cmdefstock.user.loginName;
					param.creatorName = cmdefstock.user.username;//创建人中文名称
					param.editorName = cmdefstock.user.username;//修改人中文名称
				},
	    		success : function(data) {
	    			var obj = eval('(' + data + ')');
	    			if(obj.result == 'success'){
	    				alert('新增成功!');
	    				 cmdefstock.loadDataGrid();
						 cmdefstock.closeUI();
	    				}else{
	    					alert(obj.result, 2);
	    				}
	    			wms_city_common.loading();
	    		},
	    		error : function() {
	    			alert('新增失败,请联系管理员!', 2);
	    			wms_city_common.loading();
	    				}
	    		});
	    	}
	    });
};
cmdefstock.checkExistFun = function(url,checkColumnJsonData){
	var checkExist=false;
 	$.ajax({
		  type: 'POST',
		  url: url,
		  data: checkColumnJsonData,
		  cache: true,
		  async:false, // 一定要
		  success: function(totalData){
			  totalData = parseInt(totalData,10);
			  if(totalData>0){
				  checkExist=true;
			  }
		  }
     });
 	return checkExist;
};
//******************************新增END******************************//
//******************************修改、详情BEGIN******************************//
//打开
cmdefstock.openUI = function(opt){
	$('#openUI').window({
		title:opt
	});
	$("#info_save").show();
	$('#openUI').window('open');
};
//关闭
cmdefstock.closeUI = function(opt){
	$("#openUI").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};
//打开修改
cmdefstock.editUI = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		cmdefstock.loadDetail(checkedRows[0],"edit");
	}
};
//打开详情
cmdefstock.loadDetail = function(rowData,type){
	$("#opt").val("");
	if(type=="edit"){
		cmdefstock.openUI("修改");
		$("#wareNo").combobox("disable");
		$("#areaNo").combobox("disable");
	}else{
		cmdefstock.openUI("详情");
		$("#info_save").hide();
		$("#wareNo").combobox("disable");
		$("#areaNo").combobox("disable");
	}
	inputDisableOrEnable("stockNo", "disable");
	inputDisableOrEnable("qBayX", "disable");
	inputDisableOrEnable("qStockX", "disable");
	inputDisableOrEnable("qStockY", "disable");
	var ownerNo = cmdefstock.findOwner(rowData);
	rowData.ownerNo = ownerNo;
	$("#ownerNo").combobox("disable");
	
	$("#itemType").combobox("select",rowData.itemType);
	$('#dataForm').form('load',rowData);
	$("#locno+ span input.combo-text").attr("readOnly",true);
	$("#locno+ span input.combo-text+ span span").removeClass("combo-arrow");
	$("#locno+ span").css("background-color","#EBEBE4");
	$("#locno+ span input.combo-text").css("background-color","#EBEBE4");
};
//修改事件
cmdefstock.manage = function(){
	var stockStatus = $('#stockStatus').combobox('getValue');
	var wareNo = $("#wareNo").combobox("getValue");
	var areaNo = $("#areaNo").combobox("getValue");
	var stockNo = $("#stockNo").val();
	//判断当前通道是否状态是否可修改
    if(!cmdefstock.checkStock(stockStatus, wareNo, areaNo, stockNo)){
      	return;
    } else {
    	//获取下拉框的值
    	cmdefstock.update();
    }
};

//******************************修改、详情END******************************//