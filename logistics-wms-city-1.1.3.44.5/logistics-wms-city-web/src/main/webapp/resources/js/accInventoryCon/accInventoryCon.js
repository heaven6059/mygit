var accInventoryCon = {};
accInventoryCon.locNo;
accInventoryCon.importConToItem = function(){
//	$('#importDialogView').window({title:"箱明细初始化"});
//	$("#importDialogView").window('open'); 
	var fromObj = $("#showDialog");
	$("#iframe").attr("src",BasePath + "/acc_con/iframe?v="+new Date());
	$("#showImportDialog").window('open'); 
};
//下载excel模板
accInventoryCon.downloadTemp = function(){
	window.open(BasePath + "/acc_con/downloadTemple");
};
//弹出导入选择框
accInventoryCon.showImportExcel = function(formId){
	var fromObj = $("#"+formId);
	$("#iframe").attr("src",BasePath + "/acc_con/iframe?v="+new Date());
	$("#showImportDialog").window('open'); 
};
//清除excel数据
accInventoryCon.clearExcelTemp = function(){
	var uuId=$("#uuId").val();
	if(uuId){
		var url = BasePath+'/acc_con/clearExcelTemp?uuId='+uuId;
		$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:url,
			success : function(data) {
//				alert("清空数据成功!");
				var uuId=data.uuId;
				wms_city_common.loading();
				var tempObj = $('#importGridJG_view');
				tempObj.datagrid( 'options' ).url = BasePath+'/acc_con/excel_preview?uuId='+uuId;
				tempObj.datagrid('load');
				$("#uuId").val('');
			}
		});
	}else{
		alert("没有可清空的数据！",2);
	}
};
//保存excel数据
accInventoryCon.saveExcelItem = function(){
	var uuId=$("#uuId").val();
	if(uuId){
		accInventoryCon.loading("show","正在保存数据......");
		var url = BasePath+'/acc_con/saveExcelTemp?locNo='+accInventoryCon.locNo+'&uuId='+uuId;
		$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:url,
			success : function(data) {
				var uuId=data.uuId;
				var result=data.result;
				if(result=='success'){
					alert("您已成功导入"+data.count+"条数据。");
					accInventoryCon.closeWindow("importDialogView");
					$("#uuId").val('');
				}else{
					var msg=data.msg;
					alert(msg,2);
				}
				wms_city_common.loading();
//				var tempObj = $('#importGridJG_view');
//				tempObj.datagrid( 'options' ).url = BasePath+'/acc_con/excel_preview?uuId='+uuId;
//				tempObj.datagrid('load');
				
			}
		});
	}else{
		alert("没有保存的数据，请先导入Excel！",2);
	}
};
//关闭窗口
accInventoryCon.closeWindow = function(id){
	var uuId=$("#uuId").val();
	//清空预览数据
	if(uuId){
		var url = BasePath+'/acc_con/clearExcelTemp?locNo='+accInventoryCon.locNo+'&uuId='+uuId;
		$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:url,
			success : function(data) {}
		});
	}
	$('#'+id).window('close');  
	$("#uuId").val('');
};
accInventoryCon.loading = function(type,msg){
	wms_city_common.loading(type,msg);
};
accInventoryCon.importSuccess = function(uuId){
	$('#importDialogView').window({title:"箱明细初始化"});
	$("#importDialogView").window('open');
	accInventoryCon.closeWindow("showImportDialog");	
	var tempObj = $('#importGridJG_view');
	tempObj.datagrid( 'options' ).url = BasePath+'/acc_con/excel_preview?locNo='+accInventoryCon.locNo+'&uuId='+uuId;
	tempObj.datagrid('load');
	$("#uuId").val(uuId);
	//alert("导入成功!");
};
accInventoryCon.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/acc_con/list.json?locno='+accInventoryCon.locNo+'&sort=CREATETM&order=DESC','title':'容器操作日志列表','pageNumber':1 });
};
accInventoryCon.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};
accInventoryCon.searchClear = function(){
	$('#searchForm').form("clear");
};
accInventoryCon.searchContainer = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/acc_con/list.json?locno='+accInventoryCon.locNo+'&sort=CREATETM&order=DESC';
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
};
accInventoryCon.loadDetail = function(rowData,type){
	$('#detailDialogView').window({title:"容器库存明细"});
	$("#detailDialogView").window('open'); 
	$("#detailForm_view").form('load',rowData);
	//加载明细
	var queryMxURL=BasePath+'/acc_con/dtl_list.json?conNo='+rowData.conNo+'&locno='+accInventoryCon.locNo+'&conType='+rowData.conType;
	$( "#dataGridJG_view").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_view").datagrid( 'load' );

};
accInventoryCon.closeUI = function(opt){
	$("#openUI").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};

accInventoryCon.initLoc = function(){
	$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:BasePath+'/initCache/getCurrentUser',
			success : function(data) {
				accInventoryCon.locNo = data.locno;
			}
	});
};
//B-物流箱；C-原装箱；P-栈板；R-笼车
accInventoryCon.containerType = {};
accInventoryCon.containerTypeFormatter = function(value, rowData, rowIndex){
	return accInventoryCon.containerType[value];
};
//作业类型:A-装箱;B-拼箱;C-拆箱;D-绑板;E-解板;F:拼板
accInventoryCon.userType = {};
accInventoryCon.userTypeFormatter = function(value, rowData, rowIndex){
	return accInventoryCon.userType[value];
};
//作业进程标示:I-入方;O-出方
accInventoryCon.ioFlag = {};
accInventoryCon.ioFlagFormatter = function(value, rowData, rowIndex){
	return accInventoryCon.ioFlag[value];
};
//品质
accInventoryCon.quality = {};
accInventoryCon.qualityFormatter = function(value, rowData, rowIndex){
	return accInventoryCon.quality[value];
};
//商品类型
accInventoryCon.itemType = {};
accInventoryCon.itemTypeFormatter = function(value, rowData, rowIndex){
	return accInventoryCon.itemType[value];
};
//容器状态
accInventoryCon.conStatus = {};
accInventoryCon.conStatusFormatter = function(value, rowData, rowIndex){
	return accInventoryCon.conStatus[value];
};
$(document).ready(function(){
	accInventoryCon.initLoc();
	//accInventoryCon.loadDataGrid();
	//初始化容器类型
	wms_city_common.comboboxLoadFilter(
		[ "containerTypeCondition", "conType", "conType1" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=CONTAINER_TYPE',
		{}, accInventoryCon.containerType, null);
	//商品品质
	wms_city_common.comboboxLoadFilter(
		[ "qualityCondition", "quality" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
		{}, accInventoryCon.quality, null);
	//商品类型
	wms_city_common.comboboxLoadFilter(
		[ "itemTypeCondition","itemType"],
		null,
		null,
		null,
		true,
		[ true,true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
		{}, accInventoryCon.itemType, null);
	//容器状态
	wms_city_common.comboboxLoadFilter(
		[ "conStatusCondition", "conStatus" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=CON_STATUS',
		{}, accInventoryCon.conStatus, null);
});
