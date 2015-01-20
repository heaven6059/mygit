var conBoxDtl = {};
conBoxDtl.locNo;
conBoxDtl.dataGridItemFooter={};
conBoxDtl.importConToItem = function(){
//	$('#importDialogView').window({title:"箱明细初始化"});
//	$("#importDialogView").window('open'); 
	var fromObj = $("#showDialog");
	$("#iframe").attr("src",BasePath + "/acc_con/iframe?v="+new Date());
	$("#showImportDialog").window('open'); 
};
//下载excel模板
conBoxDtl.downloadTemp = function(){
	window.open(BasePath + "/acc_con/downloadTemple");
};
//弹出导入选择框
conBoxDtl.showImportExcel = function(formId){
	var fromObj = $("#"+formId);
	$("#iframe").attr("src",BasePath + "/acc_con/iframe?v="+new Date());
	$("#showImportDialog").window('open'); 
};
//清除excel数据
conBoxDtl.clearExcelTemp = function(){
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
conBoxDtl.saveExcelItem = function(){
	var uuId=$("#uuId").val();
	if(uuId){
		conBoxDtl.loading("show","正在保存数据......");
		var url = BasePath+'/acc_con/saveExcelTemp?locNo='+conBoxDtl.locNo+'&uuId='+uuId;
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
					conBoxDtl.closeWindow("importDialogView");
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
conBoxDtl.closeWindow = function(id){
	var uuId=$("#uuId").val();
	//清空预览数据
	if(uuId){
		var url = BasePath+'/acc_con/clearExcelTemp?locNo='+conBoxDtl.locNo+'&uuId='+uuId;
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
conBoxDtl.loading = function(type,msg){
	wms_city_common.loading(type,msg);
};
conBoxDtl.importSuccess = function(uuId){
	$('#importDialogView').window({title:"箱明细初始化"});
	$("#importDialogView").window('open');
	conBoxDtl.closeWindow("showImportDialog");	
	var tempObj = $('#importGridJG_view');
	tempObj.datagrid( 'options' ).url = BasePath+'/acc_con/excel_preview?locNo='+conBoxDtl.locNo+'&uuId='+uuId;
	tempObj.datagrid('load');
	$("#uuId").val(uuId);
	//alert("导入成功!");
};
conBoxDtl.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/acc_con/list.json?locno='+conBoxDtl.locNo+'&sort=CREATETM&order=DESC','title':'容器操作日志列表','pageNumber':1 });
};
conBoxDtl.clearAll = function(){
	$('#dataForm').form("clear");
	$("#supplierNo").attr('readOnly',false);
};
conBoxDtl.searchClear = function(){
	$('#searchForm').form("clear");
};
conBoxDtl.searchContainer = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/con_box_dtl/list.json?locno='+conBoxDtl.locNo+'';
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
conBoxDtl.loadDetail = function(rowData,type){
	$('#detailDialogView').window({title:"容器库存明细"});
	$("#detailDialogView").window('open'); 
	$("#detailForm_view").form('load',rowData);
	//加载明细
	var queryMxURL=BasePath+'/acc_con/dtl_list.json?conNo='+rowData.conNo+'&locno='+conBoxDtl.locNo+'&conType='+rowData.conType;
	$( "#dataGridJG_view").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_view").datagrid( 'load' );

};
conBoxDtl.closeUI = function(opt){
	$("#openUI").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
	$("#opt").val("");
};

conBoxDtl.initLoc = function(){
	$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:BasePath+'/initCache/getCurrentUser',
			success : function(data) {
				conBoxDtl.locNo = data.locno;
			}
	});
};
//B-物流箱；C-原装箱；P-栈板；R-笼车
conBoxDtl.containerType = {};
conBoxDtl.containerTypeFormatter = function(value, rowData, rowIndex){
	return conBoxDtl.containerType[value];
};
//作业类型:A-装箱;B-拼箱;C-拆箱;D-绑板;E-解板;F:拼板
conBoxDtl.userType = {};
conBoxDtl.userTypeFormatter = function(value, rowData, rowIndex){
	return conBoxDtl.userType[value];
};
//作业进程标示:I-入方;O-出方
conBoxDtl.ioFlag = {};
conBoxDtl.ioFlagFormatter = function(value, rowData, rowIndex){
	return conBoxDtl.ioFlag[value];
};
//品质
conBoxDtl.quality = {};
conBoxDtl.qualityFormatter = function(value, rowData, rowIndex){
	return conBoxDtl.quality[value];
};
//商品类型
conBoxDtl.itemType = {};
conBoxDtl.itemTypeFormatter = function(value, rowData, rowIndex){
	return conBoxDtl.itemType[value];
};
//容器状态
conBoxDtl.conStatus = {};
conBoxDtl.conStatusFormatter = function(value, rowData, rowIndex){
	return conBoxDtl.conStatus[value];
};
$(document).ready(function(){
	conBoxDtl.initLoc();
	conBoxDtl.initGridOnLoadSuccess();
	//conBoxDtl.loadDataGrid();
	//初始化容器类型
	wms_city_common.comboboxLoadFilter(
		[ "containerTypeCondition", "conType", "conType1" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=CONTAINER_TYPE',
		{}, conBoxDtl.containerType, null);
	//商品品质
	wms_city_common.comboboxLoadFilter(
		[ "qualityCondition", "quality" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
		{}, conBoxDtl.quality, null);
	//商品类型
	wms_city_common.comboboxLoadFilter(
		[ "itemTypeCondition","itemType"],
		null,
		null,
		null,
		true,
		[ true,true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
		{}, conBoxDtl.itemType, null);
	//容器状态
	wms_city_common.comboboxLoadFilter(
		[ "conStatusCondition", "conStatus" ],
		null,
		null,
		null,
		true,
		[ true, true ],
		BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=CON_STATUS',
		{}, conBoxDtl.conStatus, null);
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),brandNoObj:$("#brandNoCondition")}
			);
	wms_city_common.loadSysNo4Cascade(objs,[false]);
	conBoxDtl.initGenderAndSeason();
	
});
conBoxDtl.initGenderAndSeason =  function(){
	$('#sysNoSearch').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonNameCondition',true,false);
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderNameCondition',true,false);
		}
	});
};
//初始化表格加载事件
conBoxDtl.initGridOnLoadSuccess = function() {
	$('#dataGridJG').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.footer!=undefined){
				if (data.footer[1].isselectsum) {
					conBoxDtl.dataGridItemFooter.qty = data.footer[1].qty;
				} else {
					var rows = $('#dataGridJG').datagrid('getFooterRows');
					rows[1]['qty'] = conBoxDtl.dataGridItemFooter.qty;
					$('#dataGridJG').datagrid('reloadFooter');
				}
			}			
		}
	});
};