var accInventoryConSku = {};
accInventoryConSku.locNo;
accInventoryConSku.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

accInventoryConSku.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/acc_con/getSkuList.json?locno='+accInventoryConSku.locNo+"&sort=CELL_NO",'title':'商品储位库存','pageNumber':1 });
};
accInventoryConSku.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};
accInventoryConSku.searchClear = function(){
	$('#searchForm').form("clear");
};
accInventoryConSku.searchContainer = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/acc_con/getSkuList.json?locno='+accInventoryConSku.locNo+"&sort=CELL_NO";
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
accInventoryConSku.loadDetail = function(rowData,type){
	$('#detailDialogView').window({title:"容器库存明细"});
	$("#detailDialogView").window('open'); 
	$("#detailForm_view").form('load',rowData);
	//加载明细
	var queryMxURL=BasePath+'/acc_con/dtl_list.json?conNo='+rowData.conNo+'&locno='+accInventoryConSku.locNo+'&conType='+rowData.conType;
	$( "#dataGridJG_view").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_view").datagrid( 'load' );

};
accInventoryConSku.closeUI = function(opt){
	$("#openUI").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};

accInventoryConSku.initLoc = function(){
	$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:BasePath+'/initCache/getCurrentUser',
			success : function(data) {
				accInventoryConSku.locNo = data.locno;
			}
	});
};
//B-物流箱；C-原装箱；P-栈板；R-笼车
accInventoryConSku.containerType = {};
accInventoryConSku.containerTypeFormatter = function(value, rowData, rowIndex){
	return accInventoryConSku.containerType[value];
};
//作业类型:A-装箱;B-拼箱;C-拆箱;D-绑板;E-解板;F:拼板
accInventoryConSku.userType = {};
accInventoryConSku.userTypeFormatter = function(value, rowData, rowIndex){
	return accInventoryConSku.userType[value];
};
//作业进程标示:I-入方;O-出方
accInventoryConSku.ioFlag = {};
accInventoryConSku.ioFlagFormatter = function(value, rowData, rowIndex){
	return accInventoryConSku.ioFlag[value];
};
//品质
accInventoryConSku.quality = {};
accInventoryConSku.qualityFormatter = function(value, rowData, rowIndex){
	return accInventoryConSku.quality[value];
};
//商品类型
accInventoryConSku.itemType = {};
accInventoryConSku.itemTypeFormatter = function(value, rowData, rowIndex){
	return accInventoryConSku.itemType[value];
};
//货主
accInventoryConSku.ownerNo = {};
accInventoryConSku.ownerNoFormatter = function(value, rowData, rowIndex){
	return accInventoryConSku.ownerNo[value];
};
$(document).ready(function(){
	accInventoryConSku.initLoc();
	accInventoryConSku.loadDataGrid();
	//初始化容器类型
	wms_city_common.comboboxLoadFilter(
		[ "containerTypeCondition", "conType" ],
		null,
		null,
		null,
		true,
		[ true, false ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=CONTAINER_TYPE',
		{}, accInventoryConSku.containerType, null);
	//初始化员工
	wms_city_common.comboboxLoadFilter(
			["creatorCondition"],
			'workerNo',
			'workerName',
			'valueAndText',
			false,
			[true,true,true,false,false],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
	
	//商品品质
	wms_city_common.comboboxLoadFilter(
		[ "qualityCondition", "quality" ],
		null,
		null,
		null,
		true,
		[ true, false ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
		{}, accInventoryConSku.quality, null);
	//商品类型
	wms_city_common.comboboxLoadFilter(
		[ "itemTypeCondition", "itemType" ],
		null,
		null,
		null,
		true,
		[ true, false ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
		{}, accInventoryConSku.itemType, null);
	//货主
	wms_city_common.comboboxLoadFilter(
		[ "ownerNoCondition", "ownerNo" ],
		"ownerNo",
		"ownerName",
		"ownerName",
		false,
		[false, false, true],
		BasePath+ '/entrust_owner/get_biz',
		{}, accInventoryConSku.ownerNo, null);
});
