var billwmplan = {};
billwmplan.locno;
billwmplan.status10 = "10";
billwmplan.currentDate;
//品牌库编码
billwmplan.lookupcode_sys_no = "SYS_NO";
//计划类型
billwmplan.lookupcode_plan_type = "CITY_RECEDE_TYPE";

//退仓状态编码
billwmplan.lookupcode_plan_status= "CITY_WM_PLAN_STATUS";

billwmplan.qualityDataObj ={};
$(document).ready(function(){

	$("#startCreatetm").datebox('setValue',getDateStr(-2));
	//初始化业主
	billwmplan.loadOwner();
	//初始化仓库
	billwmplan.loadLoc();
	//加载列表数据
	//初始化计划类型
	billwmplan.initPlanType();
	//初始化用户
	billwmplan.initUsers();
	//初始化状态
	billwmplan.initPlanStatus();
	
	//加载品牌
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNo]',$('#detailForm'))},//新增
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	
//		$('#dataGridJG_view').datagrid(
//			{
//				'onLoadSuccess':function(data){
//		   			if(data.footer[1].isselectsum){
//		   				billwmplan.itemQty = data.footer[1].itemQty;
//		   			}else{
//		   				var rows = $('#dataGridJG_view').datagrid('getFooterRows');
//			   			rows[1]['itemQty'] = billwmplan.itemQty;
//			   			$('#dataGridJG_view').datagrid('reloadFooter');
//		   			}
//		   		}
//			}
//		);
});


/***********************************************初始化******************************************************/
//加载货主信息
//货主
billwmplan.ownnerData = {};
billwmplan.loadOwner = function(){
	wms_city_common.comboboxLoadFilter(
			["ownerNo","search_ownerNo"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[false, true],
			BasePath+'/entrust_owner/get_biz',
			{},
			billwmplan.ownnerData,
			null);
};

//加载仓库信息
billwmplan.loadLoc = function(){
		$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billwmplan.locno = data.locno;
			billwmplan.currentDate = data.currentDate10Str;
			billwmplan.loginName = data.loginName;
			billwmplan.username = data.username;
			$("#startTm").datebox('setValue',billwmplan.currentDate);
			$("#endTm").datebox('setValue',billwmplan.currentDate);
		}
	});
};

//退仓类型
billwmplan.typeData = {};
billwmplan.initPlanType = function(){
	wms_city_common.comboboxLoadFilter(
			["planType_search","planType","sourceType"],
			null,
			null,
			null,
			true,
			[true, false, false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billwmplan.lookupcode_plan_type,
			{},
			billwmplan.typeData,
			null);
};

//状态
billwmplan.status = {};
billwmplan.initPlanStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["search_status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billwmplan.lookupcode_plan_status,
			{},
			billwmplan.status,
			null);
};

billwmplan.initUsers = function(){
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
billwmplan.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

/***********************************************新增******************************************************/
//弹出新增窗口
billwmplan.addInfo = function(){
	 //清空所有数据
	 $("#detailForm").form("clear");
	 //仓库，委托业主初始化
	// $("#ownerNo").combobox("select",$("#ownerNo").combobox("getData")[0].ownerNo);
	 //清空列表
	 deleteAllGridCommon('dataGridJG_detail');
	 billwmplan.mainInfoShow("add");
	 $("#sub_con").hide();
	 $('#detailDialog').window({
		title:"新增",
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	 });
	 $("#addBtn").show();
	 $("#editBtn").hide();
	 //$("#btnToStockLock").hide();
	 $("#detailDialog").window('open'); 
};

billwmplan.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status!=billwmplan.status10){
		alert("非建单状态的数据不能修改");
		return;
	}else{
		 $("#detailForm").form('load',checkedRows[0]);
		 //清空列表
		 deleteAllGridCommon('dataGridJG_detail');
		 billwmplan.mainInfoShow("edit");
		 $("#editBtn").show();
		 $("#addBtn").hide();
		 //$("#btnToStockLock").show();
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
		var queryMxURL=BasePath+'/bill_wm_plan_dtl/dtl_list.json?planNo='+rowData.planNo+'&ownerNo='+rowData.ownerNo+'&locno='+rowData.locno;
		$( "#dataGridJG_detail").datagrid( 'options' ).url=queryMxURL;
	    $( "#dataGridJG_detail").datagrid( 'load' );
	}
};

//新增主表信息
billwmplan.save_main = function(){
	var fromObj=$('#detailForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    wms_city_common.loading("show","正在保存计划单......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_wm_plan/addMain',
		success : function(data) {
			if(data.result=="success"){
				alert('新增成功!');
				$("#addBtn").hide();
	 			$("#editBtn").show();
	 			//$("#btnToStockLock").show();
	 			billwmplan.mainInfoShow("edit");
	 			billwmplan.refresh("dataGridJG");
	 			$("#sub_con").show();
	 			$("#planNo").val(data.planNo);
			}else{
				alert('新增失败,请联系管理员!',2);
			}
			wms_city_common.loading();
		}
	});
};
//修改主表信息
billwmplan.edit_main = function(){
	var fromObj=$('#detailForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    wms_city_common.loading("show","正在保存计划单......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_wm_plan/eidtMain',
		success : function(data) {
			if(data.result=="success"){
				alert('修改成功!');
				$("#addBtn").hide();
	 			$("#editBtn").show();
				billwmplan.refresh("dataGridJG");
			}else{
				alert('修改失败,请联系管理员!',2);
			}
			wms_city_common.loading();
		}
	});
};

billwmplan.mainInfoShow = function(type){
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
	$("#planType").combobox(disable);
	$("#planTypeHide").attr("disabled",disabled);
};

billwmplan.showSub = function(data){
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

billwmplan.converStr2JsonObj= function(data){
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

billwmplan.converStr2JsonObj2= function(data){
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
billwmplan.statusFormatter = function(value, rowData, rowIndex){
	return billwmplan.status[value];
};
//委托业主
billwmplan.ownerFormatter = function(value, rowData, rowIndex){
	return billwmplan.ownnerData[value];
};
//退仓类型
billwmplan.typeFormatter  = function(value, rowData, rowIndex){
	return billwmplan.typeData[value];
};
//品质
billwmplan.qualityFormatter = function(value, rowData, rowIndex){
	return billwmplan.qualityDataObj[value];
};
/***********************************************格式化******************************************************/


//查询区域信息
billwmplan.searchArea = function(){
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
	var queryMxURL=BasePath+'/bill_wm_plan/list.json?locno='+billwmplan.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
};

billwmplan.searchLocClear = function(){
	$('#searchForm').form("clear");
	$('#brandNo').combobox("loadData",[]); 
};

//弹出商品选择对话框
billwmplan.showitemDialog = function(){
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
billwmplan.searchItem = function(){
	var fromObjStr=wms_city_common.convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_wm_plan_dtl/selectItem.json';
	var obj = eval("(" +fromObjStr+ ")");
	obj.sysNo = $("#sysNo").combobox("getValue");
	obj.locno = billwmplan.locno;
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams= obj;
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
};
//清空商品form
billwmplan.searchItemClear = function(){
	$('#itemSearchForm').form("clear");
};

billwmplan.selectItemOK = function(){
	var checkedRows = $("#dataGridJGItem").datagrid("getChecked");// 获取所有勾选checkbox的行
	for(var i=0,length=checkedRows.length;i<length;i++){
        $("#dataGridJG_detail").datagrid('appendRow', checkedRows[i]);
	}
	$("#itemDialog").window('close');
};

billwmplan.closeUI= function(){
	$("#itemDialog").window('close');
};

//删除一行
billwmplan.deleterow = function(){
	var obj = $('#dataGridJG_detail');
	var row = obj.datagrid('getSelected');
	if(row==null){
		return;
	}
	var index = obj.datagrid('getRowIndex', row);
	$('#dataGridJG_detail').datagrid('deleteRow',index);
};

//保存明细
billwmplan.saveDetail = function(){
	
	var tempObj = $('#dataGridJG_detail');
	var inserted = tempObj.datagrid('getChanges', "inserted");
	var insertedArray = [];
	$.each(inserted,function(index,item){
		insertedArray.push(item.itemNo);
	});
	var deleted = tempObj.datagrid('getChanges', "deleted");
	var deletedArray = [];
	$.each(deleted,function(index,item){
		deletedArray.push(item.itemNo);
	});
	wms_city_common.loading("show","正在保存计划单明细......");
	$.ajax({
		  async : true,
		  cache: true,
		  type: 'POST',
		  url: BasePath+'/bill_wm_plan_dtl/saveDetail',
		  data: {
		  	planNo:$("#planNo").val(),
		  	ownerNo:$("#ownerNo").combobox("getValue"),
		  	inserted:insertedArray.join(","),
		  	deleted:deletedArray.join(",")
		  },
		  success: function(data){
		  	 if(data.result=='success'){
				alert("保存成功");
				$("#detailDialog").window('close');
				billwmplan.refresh("dataGridJG");
		 	}else{
			 	alert(data.msg,2);
		 	}
		  	wms_city_common.loading();
		  }
	});
};


//转库存锁定
billwmplan.toStoreLock = function(){
//	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
//	var tipStr = "";
//	var dataList = [];
//	$.each(checkedRows, function(index, item){
//		var reqP = {
//			locno : item.locno,
//			ownerNo : item.ownerNo,
//			plankNo : item.plankNo,
//			planType : item.planType,
//			status : item.status
//		};
//		dataList[dataList.length] = reqP;
//		if(item.status != '11'&&item.status != '20'){
//			tipStr = item.planNo + "非<审核>或<已转移库>状态的数据不能转库存锁定!";
//			return;
//		}
//	});
//	
//	if(tipStr!=""){
//		alert(tipStr);
//		return;
//	}
	
	var tipStr = "";
	var dataList = [];
	var planNo = $("#planNoView").val();
	var ownerNo = $("#ownerNoView").val();
	var planType = $("#planTypeHid").val();
	var status =$("#statusHid").val();
	if(status != '11'&&status != '20'){
		tipStr = planNo + "非<审核>或<已转移库>状态的数据不能转库存锁定!";
		alert(tipStr);
		return;
	}
	var reqP = {
		locno : billwmplan.locno,
		ownerNo : ownerNo,
		planNo : planNo,
		planType : planType,
		status : status,
		creator : billwmplan.loginName,
		creatorName : billwmplan.username
		
	};
	dataList[dataList.length] = reqP;
	
	//确认是否审核
	$.messager.confirm('确认', '您确认要对'+planNo+'转库存锁定吗？', function(r) {
		if (!r) {
			return;
		}
		wms_city_common.loading("show", "正在处理......");
		var url = BasePath+'/bill_wm_plan/toStoreLock';
		var params = {
			datas:JSON.stringify(dataList)
		};
		$.post(url, params, function(data) {
			if (data.result == 'success') {
				alert('处理成功!');
				billwmplan.coloseDetailViewDialog();
				$("#dataGridJG").datagrid('load');
			} else {
				alert(data.msg, 1);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
			alert('审核失败!', 1);
			wms_city_common.loading();
		});
	});
};


billwmplan.getRepeat = function(tempary) {
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



//关闭详情框
billwmplan.coloseDetailDialog = function(){
	$('#detailDialog').window('close');
};

//关闭详情框
billwmplan.coloseDetailViewDialog = function(){
	$('#detailDialogView').window('close');
};


//详情
billwmplan.showDetail = function(rowData){
	$('#detailDialogView').window({
		title:"计划单明细"
	 });
	$("#detailDialogView").window('open'); 
	$("#detailForm_view").form('load',rowData);
	//加载明细
	var queryMxURL=BasePath+'/bill_wm_plan_dtl/dtl_list.json?planNo='+rowData.planNo+'&ownerNo='+rowData.ownerNo+'&locno='+rowData.locno;
	$( "#dataGridJG_view").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_view").datagrid( 'load' );
    
    //转库存锁定设置隐藏表单域
    $("#planTypeHid").val(rowData.planType);
    $("#statusHid").val(rowData.status);
    
};




/***********************************************删除******************************************************/
billwmplan.del = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keyStr = [];
	var allOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!=billwmplan.status10){
			allOk = false;
			return false;
		}
		keyStr.push(item.planNo+"|"+item.ownerNo);
	});   
	if(!allOk){
		alert("非建单状态的数据不能删除",1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	wms_city_common.loading("show");
            var url = BasePath+'/bill_wm_plan/deletePlan';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					  wms_city_common.loading();
					  if(data.result=='success'){
        				 alert('删除成功!');
						 billwmplan.refresh("dataGridJG");
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
billwmplan.audit=function(){
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
		keyStr.push(item.planNo+"|"+item.ownerNo);
	});  
	if(!allOk){
		alert("非建单状态的数据不能审核",1);
		return;
	} 
	if(!dtlOk){
		alert("没有明细的单据不能审核",1);
		return;
	} 
    var url = BasePath+'/bill_wm_plan/auditPlan';
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
						  billwmplan.refresh("dataGridJG");
        		 	}else{
        			 	alert(data.msg,2);
        		 	}
				  }
			});
        }  
    });
};

billwmplan.refresh = function(dataGridId){
	$("#"+dataGridId).datagrid('load');
};

// 转退厂申请
billwmplan.changeWMRequest = function() {
	var planNo = $("#planNoView").val();
	var ownerNo = $("#ownerNoView").val();
	// 确认是否转退厂申请
	$.messager.confirm('确认', '您确认要转退厂申请吗？', function(r) {
		if (!r) {
			return;
		}
		wms_city_common.loading("show", "正在转退厂申请......");
		var url = BasePath + '/bill_wm_plan/changeWMRequest';
		var params = {
			planNo : planNo,
			locno : billwmplan.locno,
			ownerNo : ownerNo
		};
		$.post(url, params, function(data) {
			if (data.result == 'success') {
				alert('转退厂申请成功!');
				$("#dataGridJG").datagrid('load');
			} else {
				alert(data.msg, 1);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
			alert('转退厂申请失败!', 1);
			wms_city_common.loading();
		});
	});
};

//转移库计划
billwmplan.changeHMPlan = function() {
	var planNo = $("#planNoView").val();
	var ownerNo = $("#ownerNoView").val();
	// 确认是否转移库计划
	$.messager.confirm('确认', '您确认要转移库计划吗？', function(r) {
		if (!r) {
			return;
		}
		wms_city_common.loading("show", "正在转转移库计划......");
		var url = BasePath + '/bill_wm_plan/changeHMPlan';
		var params = {
			planNo : planNo,
			locno : billwmplan.locno,
			ownerNo : ownerNo
		};
		$.post(url, params, function(data) {
			if (data.result == 'success') {
				alert('转移库计划成功!');
				$("#dataGridJG").datagrid('load');
			} else {
				alert(data.msg, 1);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
			alert('转移库计划失败!', 1);
			wms_city_common.loading();
		});
	});
};

