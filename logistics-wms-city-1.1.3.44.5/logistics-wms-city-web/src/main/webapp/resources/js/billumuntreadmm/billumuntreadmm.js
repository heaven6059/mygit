var billumuntreadmm = {};
billumuntreadmm.locno;
billumuntreadmm.status10 = "10";
billumuntreadmm.currentDate;
//品牌库编码
billumuntreadmm.lookupcode_sys_no = "SYS_NO";
//退仓类型编码
billumuntreadmm.lookupcode_untread_type = "ITEM_TYPE";
//来源类型编码
billumuntreadmm.lookupcode_po_type = "CMD_TYPE";
//退仓状态编码
billumuntreadmm.lookupcode_untread_status= "CITY_BILL_UM_UNTREAD_MM_STATUS";

$(document).ready(function(){
	//初始化业主
	billumuntreadmm.loadOwner();
	//初始化仓库
	billumuntreadmm.loadLoc();
	//加载列表数据
	//billumuntreadmm.loadDataGrid();
	//初始化品牌库
	billumuntreadmm.initSysNo();
	//初始化退仓类型
	billumuntreadmm.initUntreadType();
	//初始化来源类型
	billumuntreadmm.initPoType();
	//初始化用户
	billumuntreadmm.initUsers();
	//初始化状态
	billumuntreadmm.initUntreadStatus();
	//初始化品质
	billumuntreadmm.initQuality();
	//加载品牌
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNo')},//修改
			{"sysNoObj":$('#view_sysNo')},//详情
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')}//查询
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	
	$('#dataGridJG_view').datagrid(
		{
			'onLoadSuccess':function(data){
	   			if(data.footer[1].isselectsum){
	   				billumuntreadmm.itemQty = data.footer[1].itemQty;
	   			}else{
	   				var rows = $('#dataGridJG_view').datagrid('getFooterRows');
		   			rows[1]['itemQty'] = billumuntreadmm.itemQty;
		   			$('#dataGridJG_view').datagrid('reloadFooter');
	   			}
	   		}
		}
	);
});


/***********************************************初始化******************************************************/
//退仓类型
billumuntreadmm.typeData = {};
billumuntreadmm.initUntreadType = function(){
	wms_city_common.comboboxLoadFilter(
			["untreadType","view_untreadType","untreadType_search"],
			null,
			null,
			null,
			true,
			[false, false, true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumuntreadmm.lookupcode_untread_type,
			{},
			billumuntreadmm.typeData,
			null);
};
billumuntreadmm.qualityDataObj ={};
billumuntreadmm.initQuality = function(){
	wms_city_common.comboboxLoadFilter(
			["quality","quality_view","quality_search"],
			null,
			null,
			null,
			true,
			[false, false, true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			billumuntreadmm.qualityDataObj,
			null);
};
//加载货主信息
billumuntreadmm.ownnerData = {};
billumuntreadmm.loadOwner = function(){
	wms_city_common.comboboxLoadFilter(
			["ownerNo","view_ownerNo","search_ownerNo"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[false, false, true],
			BasePath+'/entrust_owner/get_biz',
			{},
			billumuntreadmm.ownnerData,
			null);
};

//加载仓库信息
billumuntreadmm.loadLoc = function(){
		$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billumuntreadmm.locno = data.locno;
			billumuntreadmm.currentDate = data.currentDate10Str;
			$("#startTm").datebox('setValue',billumuntreadmm.currentDate);
			$("#endTm").datebox('setValue',billumuntreadmm.currentDate);
		}
	});
};

//初始化品牌库
billumuntreadmm.initSysNo = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumuntreadmm.lookupcode_sys_no,
		success : function(data) {
			$('#sysNo,#view_sysNo').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150
			});
			if(null!=data){
				$('#sysNo').combobox("select",data[0].itemvalue); 
			}
		}
	});
};


billumuntreadmm.initPoType = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumuntreadmm.lookupcode_po_type,
		success : function(data) {
			$('#poType,#view_poType').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150
			});
		}
	});
};

//状态
billumuntreadmm.status = {};
billumuntreadmm.initUntreadStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["search_status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumuntreadmm.lookupcode_untread_status,
			{},
			billumuntreadmm.status,
			null);
};

billumuntreadmm.initUsers = function(){
	wms_city_common.comboboxLoadFilter(
			["search_creator","search_auditor"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true,true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
};
/***********************************************初始化end******************************************************/

//加载Grid数据Utils
billumuntreadmm.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

/***********************************************新增******************************************************/
//弹出新增窗口
billumuntreadmm.addInfo = function(){
	 //清空所有数据
	 $("#detailForm").form("clear");
	 //仓库，委托业主初始化
	// $("#ownerNo").combobox("select",$("#ownerNo").combobox("getData")[0].ownerNo);
	 //清空列表
	 deleteAllGridCommon('dataGridJG_detail');
	 billumuntreadmm.mainInfoShow("add");
	 $("#sub_con").hide();
	 $('#detailDialog').window({
		title:"新增",
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	 });
	 $("#addBtn").show();
	 $("#editBtn").hide();
	 $("#detailDialog").window('open'); 
};

billumuntreadmm.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status!=billumuntreadmm.status10){
		alert("非建单状态的数据不能修改");
		return;
	}else{
		 $("#detailForm").form('load',checkedRows[0]);
		 //清空列表
		 deleteAllGridCommon('dataGridJG_detail');
		 billumuntreadmm.mainInfoShow("edit");
		 $("#editBtn").show();
		 $("#addBtn").hide();
		 $("#submaintoolbar").show();
		 $('#detailDialog').window({
			title:"修改",
			onBeforeClose:function(){
				$(".tooltip").remove();
			}
		 });
		 $("#sub_con").show();
		 $("#detailDialog").window('open'); 
		 var rowData = checkedRows[0];
		 //加载明细
		var queryMxURL=BasePath+'/bill_um_untread_mm_dtl/list.json?untreadMmNo='+rowData.untreadMmNo+'&ownerNo='+rowData.ownerNo+'&locno='+rowData.locno;
		$( "#dataGridJG_detail").datagrid( 'options' ).url=queryMxURL;
	    $( "#dataGridJG_detail").datagrid( 'load' );
	}
};

//新增主表信息
billumuntreadmm.save_main = function(){
	var fromObj=$('#detailForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    var applyDate = $("#applyDate").datebox('getValue');
    var curDate = wms_city_common.getCurDate();
    if(applyDate==""||applyDate==null){
    	alert("申请日期不能为空!",1);
     	return;
    }
     if(!wms_city_common.isStartLessThanEndDate(curDate,applyDate)){
     	alert("申请日期不能小于当前日期!",1);
     	return;
     }
     wms_city_common.loading("show","正在生成退仓通知单......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_um_untread_mm/addMain',
		success : function(data) {
			if(data.result=="success"){
				alert('新增成功!');
				billumuntreadmm.refresh();
				$("#addBtn").hide();
	 			$("#editBtn").show();
	 			$("#untreadMmNo").val(data.untreadMmNo);
	 			billumuntreadmm.mainInfoShow("edit");
	 			billumuntreadmm.refresh("dataGridJG");
	 			$("#sub_con").show();
			}else{
				alert('新增失败,请联系管理员!',2);
			}
			wms_city_common.loading();
		}
	});
};
//修改主表信息
billumuntreadmm.edit_main = function(){
	var fromObj=$('#detailForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    var applyDate = $("#applyDate").datebox('getValue');
    var curDate = wms_city_common.getCurDate();
    if(!wms_city_common.isStartLessThanEndDate(curDate,applyDate)){
     	alert("申请日期不能小于当前日期!",1);
     	return;
     }
    wms_city_common.loading("show","正在保存退仓通知单......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_um_untread_mm/eidtMain',
		success : function(data) {
			if(data.result=="success"){
				alert('修改成功!');
				$("#addBtn").hide();
	 			$("#editBtn").show();
				billumuntreadmm.refresh("dataGridJG");
			}else{
				alert('修改失败,请联系管理员!',2);
			}
			wms_city_common.loading();
		}
	});
};

billumuntreadmm.mainInfoShow = function(type){
	var disable = "disable";
	var disabled = true;
	if(type=="add"){
		disable = "enable";
		disabled = true;
	}else{
		disable = "disable";
		disabled = false;
	}
	//货主
	$("#ownerNo").combobox(disable);
	$("#ownerNoHide").attr("disabled",disabled);
	 //品牌库
	$("#sysNo").combobox(disable);
	$("#sysNoHide").attr("disabled",disabled);
	//退仓类型
	$("#untreadType").combobox(disable);
	$("#untreadTypeHide").attr("disabled",disabled);
	//品质
	$("#quality").combobox(disable);
	$("#qualityHide").attr("disabled",disabled);
};

billumuntreadmm.showSub = function(data){
	//设置主表信息
	$("#receiptNo").val(data.receiptNo);
	$("#receiptNoHide").val(data.receiptNo);
	//仓库
	$("#locNo").combobox("disable");
	$("#locNoHide").attr("disabled",false);
	$("#locNoHide").val(data.locno);
	//委托业主
	$("#ownerNo").combobox("disable");
	$("#ownerNoHide").attr("disabled",false);
	$("#ownerNoHide").val(data.ownerNo);
	//收货日期
	$("#curDate").val(data.createtm);
	//供应商
	$("#supplierNo").combobox("disable");
	$("#supplierNoHide").attr("disabled",false);
	$("#supplierNoHide").val(data.supplierNo);
	$("#main_btn").hide();
	$("#sub_con").show();
	$("#opBtn").show();
};

billumuntreadmm.converStr2JsonObj= function(data){
	if(data==null){
		 return "";
	}
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billumuntreadmm.converStr2JsonObj2= function(data){
	if(data==null){
		 return "";
	}
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

/***********************************************格式化******************************************************/
//状态
billumuntreadmm.statusFormatter = function(value, rowData, rowIndex){
	return billumuntreadmm.status[value];
};
//委托业主
billumuntreadmm.ownerFormatter = function(value, rowData, rowIndex){
	return billumuntreadmm.ownnerData[value];
};
//退仓类型
billumuntreadmm.typeFormatter  = function(value, rowData, rowIndex){
	return billumuntreadmm.typeData[value];
};
//品质
billumuntreadmm.qualityFormatter = function(value, rowData, rowIndex){
	return billumuntreadmm.qualityDataObj[value];
};
/***********************************************格式化******************************************************/


//查询区域信息
billumuntreadmm.searchArea = function(){
	var startCreatetm = $('#startCreatetm').datebox('getValue');
	var endCreatetm = $('#endCreatetm').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
    
    var startAudittm = $('#startAudittm').datebox('getValue');
	var endAudittm = $('#endAudittm').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
	}
	var fromObjStr=wms_city_common.convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_untread_mm/list.json?locno='+billumuntreadmm.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
};

billumuntreadmm.searchLocClear = function(){
	$('#brandNo').combobox("loadData",[]);
	$('#searchForm').form("clear");
};

//弹出商品选择对话框
billumuntreadmm.showitemDialog = function(){
	//清除列表信息
    deleteAllGridCommon('dataGridJGItem');
	$("#itemDialog").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$("#itemSearchForm").form("clear");
	$("#itemDialog").window('open');
};

//查询商品
billumuntreadmm.searchItem = function(){
	var fromObjStr=wms_city_common.convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_untread_mm_dtl/selectItem';
	var obj = eval("(" +fromObjStr+ ")");
	obj.sysNo = $("#sysNo").combobox("getValue");
	obj.locno = billumuntreadmm.locno;
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams= obj;
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
};
//清空商品form
billumuntreadmm.searchItemClear = function(){
	$('#itemSearchForm').form("clear");
};

billumuntreadmm.selectItemOK = function(){
	var checkedRows = $("#dataGridJGItem").datagrid("getChecked");// 获取所有勾选checkbox的行
	for(var i=0,length=checkedRows.length;i<length;i++){
        $("#dataGridJG_detail").datagrid('appendRow', checkedRows[i]);
        var rows = $("#dataGridJG_detail").datagrid("getRows");
        $("#dataGridJG_detail").datagrid('beginEdit',rows.length-1);
	}
	$("#itemDialog").window('close');
};

billumuntreadmm.closeUI= function(){
	$("#itemDialog").window('close');
};

//删除一行
billumuntreadmm.deleterow = function(){
	var obj = $('#dataGridJG_detail');
	var row = obj.datagrid('getSelected');
	if(row==null){
		return;
	}
	var index = obj.datagrid('getRowIndex', row);
	$('#dataGridJG_detail').datagrid('deleteRow',index);
};

//保存明细
billumuntreadmm.saveDetail = function(){
	var tempFlag = billumuntreadmm.endEdit("dataGridJG_detail");
	if(!tempFlag){
		return;
	}
	var tempObj = $('#dataGridJG_detail');
	var inserted = tempObj.datagrid('getChanges', "inserted");
	var updated = tempObj.datagrid('getChanges', "updated");
	var deleted = tempObj.datagrid('getChanges', "deleted");
	
	var repeatArry =  billumuntreadmm.getRepeat(tempObj.datagrid('getRows'));
	 if(repeatArry!=null&&repeatArry.length>0){
	   		var repeat = "";
	   		var repeatTemp = [];
	   		for(var i=0;i<repeatArry.length;i++){
	   			repeatTemp = repeatArry[i].split("|");
	   			repeat=repeat+"商品编码："+repeatTemp[0]+"<br>尺码："+repeatTemp[1]+",重复!";
	   		}
	   		alert('保存失败,'+repeat+"",2);
	   		return;
	   }
	 wms_city_common.loading("show","正在生成退仓通知单明细......");
	$.ajax({
		  async : true,
		  cache: true,
		  type: 'POST',
		  url: BasePath+'/bill_um_untread_mm_dtl/saveDetail',
		  data: {
		  	untreadMmNo:$("#untreadMmNo").val(),
		  	ownerNo:$("#ownerNo").combobox("getValue"),
		  	inserted:encodeURIComponent(JSON.stringify(inserted)),
		  	updated:encodeURIComponent(JSON.stringify(updated)),
		  	deleted:encodeURIComponent(JSON.stringify(deleted))
		  },
		  success: function(data){
		  	 if(data.result=='success'){
				alert("保存成功");
				$("#detailDialog").window('close');
				billumuntreadmm.refresh("dataGridJG");
		 	}else{
			 	alert(data.msg,2);
		 	}
		  	wms_city_common.loading();
		  }
	});
};


billumuntreadmm.getRepeat = function(tempary) {
var ary=[];
for(var i = 0;i<tempary.length;i++){
	ary.push(tempary[i].itemNo+"|"+tempary[i].sizeNo);
}
var res = [];  
ary.sort();  
for(var i = 0;i<ary.length;){
 var count = 0;  
 for(var j=i;j<ary.length;j++){
  if(ary[i]== ary[j]) {  
   count++;  
  }    
 }
 if(count>1){
 	res.push(ary[i]); 
 }
 i+=count;   
};
return res;
};

billumuntreadmm.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		var storNoField = tempObj.datagrid('getEditor', {index:i,field:'storeNo'});
    		if(storNoField!=null){
    			if(!billumuntreadmm.checkStoreNo($(storNoField.target).val())){
	    			alert("第"+(i+1)+"行,客户编码不存在");
	    			return false;
    			}
    		}
    		tempObj.datagrid('endEdit', i);
    	}else{
    		alert('数据验证没有通过!',1);
    		return false;
    	}
    }
    return true;
};
billumuntreadmm.checkStoreNo = function(storeNo){
	var isOk = true;
	$.ajax({
		  async : false,
		  cache: true,
		  type: 'POST',
		  url: BasePath+'/bill_um_untread_mm_dtl/queryStoreByStoreNo',
		  data: {
		  	storeNo:storeNo,
		  	storeType:11
		  },
		  success: function(data){
		  	 if(data.storeNo==""||data.storeNo==null){
				isOk = false;
		 	}else{
			 	isOk = true;
		 	}
		  }
	});
	return isOk;
};
//关闭详情框
billumuntreadmm.coloseDetailDialog = function(){
	$('#detailDialog').window('close');
};


//详情
billumuntreadmm.showDetail = function(rowData){
	$('#detailDialogView').window({
		title:"退仓通知明细"
	 });
	$("#detailDialogView").window('open'); 
	$("#detailForm_view").form('load',rowData);
	//加载明细
	var queryMxURL=BasePath+'/bill_um_untread_mm_dtl/dtl_list.json?untreadMmNo='+rowData.untreadMmNo+'&ownerNo='+rowData.ownerNo+'&locno='+rowData.locno;
	$( "#dataGridJG_view").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_view").datagrid( 'load' );
};




/***********************************************删除******************************************************/
billumuntreadmm.del = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keyStr = [];
	var allOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!=billumuntreadmm.status10){
			allOk = false;
			return false;
		}
		keyStr.push(item.untreadMmNo+"|"+item.ownerNo);
	});   
	if(!allOk){
		alert("非建单状态的数据不能删除",1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
            var url = BasePath+'/bill_um_untread_mm/deleteUntreadMm';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	wms_city_common.loading("show","正在删除......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					 wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('删除成功!');
						 billumuntreadmm.refresh("dataGridJG");
        		 	}else{
        			 	alert('删除失败,请联系管理员!',2);
        		 	}
				  }
			});
        }  
    });  
};

/***********************************************删除end******************************************************/

/***********************************************审核***********************************************************/
billumuntreadmm.audit=function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}
	
	var keyStr = [];
	var allOk = true;
	var dtlOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!="10"){
			allOk = false;
			return false;
		}
		if(item.dtlCount=="0"){
			dtlOk = false;
			return false;
		}
		keyStr.push(item.untreadMmNo+"|"+item.ownerNo);
	});  
	if(!allOk){
		alert("非建单状态的数据不能审核",1);
		return;
	} 
	if(!dtlOk){
		alert("没有明细的单据不能审核",1);
		return;
	} 
    var url = BasePath+'/bill_um_untread_mm/auditUntreadMm';
	var data={
	    "keyStr":keyStr.join(",")
	};
	
	$.messager.confirm("确认","你确定要审核这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	wms_city_common.loading("show","正在审核......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					 wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('审核成功!');
						  billumuntreadmm.refresh("dataGridJG");
        		 	}else{
        			 	alert(data.msg,2);
        		 	}
				  }
			});
        }  
    });
};

billumuntreadmm.refresh = function(dataGridId){
	$("#"+dataGridId).datagrid('load');
};
