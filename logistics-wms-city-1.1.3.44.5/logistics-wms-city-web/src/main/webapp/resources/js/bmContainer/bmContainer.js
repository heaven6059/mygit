var bmContainer = {};
bmContainer.user;
//仓库编码
bmContainer.locno;
bmContainer.locName;
//==================容器类型=======================
//　B-物流箱；C-原装箱；P-栈板；R-笼车
bmContainer.containerType = {};
bmContainer.containerTypeFormatter = function(value, rowData, rowIndex){
	return bmContainer.containerType[value];
};
//==================容器类型=======================
//容器是否允许混款
bmContainer.mixStyle= {};
bmContainer.mixStyleFormatter = function(value, rowData, rowIndex){
	return bmContainer.mixStyle[value];
};

bmContainer.containerPrefixFormatter = function(value, rowData, rowIndex){
	return bmContainer.containerPrefixSelect[value];
};
//==================容器状态 =====================
//0：禁用；1：正常
bmContainer.manageStatus = {};
bmContainer.manageStatusFormatter = function(value, rowData, rowIndex){
	return bmContainer.manageStatus[value];
};

bmContainer.manageConStatus = {};
bmContainer.manageConStatusFormatter = function(value, rowData, rowIndex){
	var valueStr = bmContainer.manageConStatus[value];
	if(valueStr){
		return valueStr;
	}
//	else{
//		return '没库存';
//	}
};
bmContainer.manageConTransFlag = {};
bmContainer.manageConTransFlagFormatter = function(value, rowData, rowIndex){
	return bmContainer.manageConTransFlag[value];
};

//==================容器状态 =====================
bmContainer.ownnerData = {};

bmContainer.ownerFormatter = function(value, rowData, rowIndex){
	return bmContainer.ownnerData[value];
};
//作业类型:A-装箱;B-拼箱;C-拆箱;D-绑板;E-解板;F:拼板
bmContainer.userType = {};
bmContainer.userTypeFormatter = function(value, rowData, rowIndex){
	return bmContainer.userType[value];
};
bmContainer.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
bmContainer.openUI = function(opt){
	$('#openUI').window({
		title:opt
	});
	$('#openUI').window('open');
	$("#stIsshow").show();
	$("#svIsshow").show();
};
bmContainer.closeUI = function(opt){
	$("#openUI").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};
bmContainer.addUI = function(opt){
	$("#info_save").show();
	bmContainer.openUI("新增");
	bmContainer.clearAll();
	$("#locno").attr('readOnly',false);
	$("#containerType").combobox('enable');
	$("#opt").val("add");
	$("#stIsshow").hide();
	$("#svIsshow").hide();
	$("#trIsShow").hide();
};

bmContainer.editUI = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		bmContainer.loadDetail(checkedRows[0],"edit");
		$("#stIsshow").show();
		$("#svIsshow").show();
		var billNO=$("#optBillNo").val();
		if(billNO){
			$("#trIsShow").show();
		}else{
			$("#trIsShow").hide();
		}
	}
};

bmContainer.manage = function(opt){
	var opt = $("#opt").val();
	if(opt=="add"){
		bmContainer.save();
	}else{
		bmContainer.update();
	}
};
bmContainer.initLoc = function(){
	$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:BasePath+'/initCache/getCurrentUser',
			success : function(data) {
				bmContainer.locno = data.locno;
				bmContainer.locName=data.locname;
			}
	});
};
bmContainer.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bmContainer/list.json?locno='+bmContainer.locno,'title':'容器资料列表','pageNumber':1 });
};

bmContainer.loadDetail = function(rowData,type){
	$("#opt").val("");
	if(type=="edit"){
		bmContainer.openUI("修改");
		$("#info_save").show();
	}else{
		bmContainer.openUI("详情");
		$("#info_save").hide();
	}
	$('#dataForm').form('load',rowData);
	$("#containerType").combobox('disable');
	if(type=="edit"){
		var billNO=$("#optBillNo").val();
		if(billNO){
			$("#trIsShow").show();
			$("#info_save").hide();
		}else{
			$("#trIsShow").hide();
			$("#info_save").show();
		}
	}else{
		var billNO=$("#optBillNo").val();
		if(billNO){
			$("#trIsShow").show();
		}else{
			$("#trIsShow").hide();
		}
	}
};
bmContainer.checkExistFun = function(url,checkColumnJsonData){
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

bmContainer.save = function(){
	    var fromObj=$('#dataForm');
	     //1.校验必填项
	     var validateForm= fromObj.form('validate');
	     if(validateForm==false){
	          return ;
	     }
		 //3. 保存
         var url = BasePath+'/bmContainer/saveBmContainer';
         fromObj.form('submit', {
				url: url,
				onSubmit: function(param){
					param.createtm = bmContainer.user.currentDate19Str;
					param.creator = bmContainer.user.loginName;
					param.editor = bmContainer.user.loginName;
					param.edittm = bmContainer.user.currentDate19Str;
					param.locno = bmContainer.user.locno;
				},
				success: function(data){
					var dataResult = eval('('+data+')'); 
					if(dataResult.result=="success"){
						 alert('新增成功!');
						 //4.保存成功,清空表单
						 bmContainer.loadDataGrid();
						 bmContainer.closeUI();
						 return;
					}else{
						var msg=dataResult.msg;
						alert(msg,2);
					}
					
			    }
		   });
	     
};

bmContainer.update = function(){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
   
     //2.绑定数据
    var url = BasePath+'/bmContainer/moditfyBmContainer';
    fromObj.form('submit', {
		url: url,
		onSubmit: function(param){
			param.editor = bmContainer.user.loginName;
			param.locno = bmContainer.user.locno;
			
		},
		success: function(r){
			if(r && r=='success'){
			 alert('修改成功!');
			 //3.保存成功,清空表单
			 bmContainer.loadDataGrid();
			 bmContainer.closeUI();
			 return;
			}else{
				alert('修改失败,状态已改变或已有容器库存',2);
			}
			
	    },
		error:function(){
			alert('修改失败,状态已改变或已有容器库存',2);
		}
   });
};

bmContainer.deleteRows = function(){
	//1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keys = [];
	$.each(checkedRows, function(index, item){
		keys.push(item.locno+"-"+item.conNo);
	});   
     //2.绑定数据
     var url = BasePath+'/bmContainer/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		    if (r){    
		   	 bmContainer.ajaxRequest(url,data,true,function(result){
				 if(result=='success'){
					 //4.删除成功,清空表单
					 bmContainer.loadDataGrid();
					 bmContainer.closeUI();
					 alert('删除成功!');
				 }else if(result=='2'){
					 alert('删除失败,请联系管理员!',2);
				 }else{
					 var strs=result.split(":"); //字符分割  
					 if(strs[0]=="1"){
						 alert(strs[1]+"被占用不能删除！",2);
					 }else{
						 alert(strs[1]+"有库存不能删除！",2);
					 }
				 }
			}); 
		    }    
		});  



};
bmContainer.clearAll = function(){
	$('#dataForm').form("clear");
	$("input[name='weight']").val('');
	$("input[name='volume']").val('');
	$("input[name='length']").val('');
	$("input[name='wide']").val('');
	$("input[name='height']").val('');
};
bmContainer.searchClear = function(){
	$('#searchForm').form("clear");
};
bmContainer.searchContainer = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bmContainer/list.json?locno='+bmContainer.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
};

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

$(document).ready(function(){
	bmContainer.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(u){bmContainer.user= u;});
	bmContainer.initLoc();
	bmContainer.loadDataGrid();
	//初始化容器类型
	wms_city_common.comboboxLoadFilter(
			["containerTypeCondition","containerType"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CONTAINER_TYPE',
			{},
			bmContainer.containerType,
			null);
	//初始化容器是否允许混款
	wms_city_common.comboboxLoadFilter(
			["mixStyleCondition","mixStyle"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=MIX_STYLE',
			{},
			bmContainer.mixStyle,
			null);
	//初始化容器状态
	wms_city_common.comboboxLoadFilter(
			["manageStatusCondition","manageStatus"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=MANAGE_STATUS',
			{},
			bmContainer.manageStatus,
			null);
	//加载货主信息
	wms_city_common.comboboxLoadFilter(
			["ownerNo","view_ownerNo","search_ownerNo"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[false, false, true],
			BasePath+'/entrust_owner/get_biz',
			{},
			bmContainer.ownnerData,
			null);
	//初始化单据作业类型
	wms_city_common.comboboxLoadFilter(
		[ "userTypeCondition", "userType","optBillTypeCondition"],
		null,
		null,
		null,
		true,
		[ true, false ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=BILL_CON_USER_TYPE',
		{}, bmContainer.userType, null);
	//初始化容器库存状态
	wms_city_common.comboboxLoadFilter(
			["manageConStatusCondition","conStatus"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CON_STATUS',
			{},
			bmContainer.manageConStatus,
			null);
	//初始化容器来源
	wms_city_common.comboboxLoadFilter(
			["manageTransFlagCondition","transFlag"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CON_TRANS_FLAG',
			{},
			bmContainer.manageConTransFlag,
			null);
});

bmContainer.printByBox = function(noneDtl){
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var conNo = $("#conNo").val();
	var keys = [];
	var LODOP; //声明为全局变量 
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
  	if(LODOP==null){
  		return;
  	}
	for(var i=0,length = rows.length;i<length;i++){
		var conType=rows[i].type;
		var type="";
		if(conType=='C'){
			type=bmContainer.containerType['C'];
		}else if(conType=='P'){
			type=bmContainer.containerType['P'];
		}
		keys.push(rows[i].conNo);
		LODOP.NewPage();
        var html = "<table border='0' cellpadding='0' cellspacing='0' style='background-color: #000;width:100%;'>" +
		        "<tr bgcolor='#fff'>" +
				"<td style='font-weight:bold;text-align:center;font-size=14px;'><span style='color:#FF0000'>"+bmContainer.locName+"("+bmContainer.locno+")"+type+"</span>标签</td>" +
				"</tr>" +	
        		"</table>";
        LODOP.SET_PRINT_PAGESIZE(1,'600','500',"");
		LODOP.ADD_PRINT_HTM(10,1,"100%","100%",html);
		LODOP.ADD_PRINT_BARCODE(75,17,195,75,"128A",rows[i].conNo);
	}
	LODOP.PREVIEW(); //打印预览
};
