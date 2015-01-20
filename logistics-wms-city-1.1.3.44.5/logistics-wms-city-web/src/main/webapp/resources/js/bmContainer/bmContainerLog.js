var bmContainerLog = {};
bmContainerLog.locNo;
bmContainerLog.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

bmContainerLog.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bc_log/list.json?locNo='+bmContainerLog.locNo,'title':'容器操作日志列表','pageNumber':1 });
};
bmContainerLog.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};
bmContainerLog.searchClear = function(){
	$('#searchForm').form("clear");
};
bmContainerLog.searchContainer = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bc_log/list.json?locNo='+bmContainerLog.locNo;
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
bmContainerLog.loadDetail = function(rowData,type){
	$('#detailDialogView').window({title:"容器日志明细"});
	$("#detailDialogView").window('open'); 
	$("#detailForm_view").form('load',rowData);
	//加载明细
	var queryMxURL = BasePath + '/bc_log/dtl_list.json?billNo='
			+ rowData.billNo + '&conNo=' + rowData.subConNo + '&locNo='
			+ bmContainerLog.locNo +'&ioFlag=' + rowData.ioFlag;
	$("#dataGridJG_view").datagrid('options').url = queryMxURL;
	$("#dataGridJG_view").datagrid('load');

};
bmContainerLog.closeUI = function(opt){
	$("#openUI").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};

bmContainerLog.initLoc = function(){
	$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:BasePath+'/initCache/getCurrentUser',
			success : function(data) {
				bmContainerLog.locNo = data.locno;
			}
	});
};
//B-物流箱；C-原装箱；P-栈板；R-笼车
bmContainerLog.containerType = {};
bmContainerLog.containerTypeFormatter = function(value, rowData, rowIndex){
	return bmContainerLog.containerType[value];
};
//作业类型:A-装箱;B-拼箱;C-拆箱;D-绑板;E-解板;F:拼板
bmContainerLog.userType = {};
bmContainerLog.userTypeFormatter = function(value, rowData, rowIndex){
	return bmContainerLog.userType[value];
};
//作业进程标示:I-入方;O-出方
bmContainerLog.ioFlag = {};
bmContainerLog.ioFlagFormatter = function(value, rowData, rowIndex){
	return bmContainerLog.ioFlag[value];
};
//品质
bmContainerLog.quality = {};
bmContainerLog.qualityFormatter = function(value, rowData, rowIndex){
	return bmContainerLog.quality[value];
};
//商品类型
bmContainerLog.itemType = {};
bmContainerLog.itemTypeFormatter = function(value, rowData, rowIndex){
	return bmContainerLog.itemType[value];
};
$(document).ready(function(){
	bmContainerLog.initLoc();
	bmContainerLog.loadDataGrid();
	//初始化容器类型
	wms_city_common.comboboxLoadFilter(
		[ "containerTypeCondition", "conType" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=CONTAINER_TYPE',
		{}, bmContainerLog.containerType, null);
	//初始化单据作业类型
	wms_city_common.comboboxLoadFilter(
		[ "userTypeCondition", "userType","userType2" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=BILL_CON_USER_TYPE',
		{}, bmContainerLog.userType, null);
	//作业进程标示:I-入方;O-出方
	wms_city_common.comboboxLoadFilter(
		[ "ioFlagCondition", "ioFlag" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=BILL_CON_IO_FLAG',
		{}, bmContainerLog.ioFlag, null);
	//商品品质
	wms_city_common.comboboxLoadFilter(
		[ "qualityCondition", "quality" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
		{}, bmContainerLog.quality, null);
	//商品类型
	wms_city_common.comboboxLoadFilter(
		[ "itemTypeCondition", "itemType" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
		{}, bmContainerLog.itemType, null);
});
