

var instantConContent = {};

instantConContent.detaildataGridFooter={};

// 它将jquery系列化后的值转为name:value的形式。
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

instantConContent.sizeTypeFiledName = 'sizeKind';

instantConContent.preColNames = [
	{title : "货号",field : "itemNo",width : 160},
	{title : "颜色",field : "colorName",width : 80,align:'left'}, 
	{title : "名称",field : "itemName",width : 200,align:'left'}, 
	{title : "大类",field : "cateName",width : 80,align:'left'} ,
	{title : "年份",field : "yearsName",width:70,align:'left'},
	{title : "季节",field : "seasonName",width:70,align:'left'},
	{title : "性别",field : "genderName",width:70,align:'left'}
];


instantConContent.endColNames = [ {
	title : "合计",
	field : "qty",
	width : 80,
	align : 'right'
}, {
	title : "单价",
	field : "salePrice",
	width : 80,
	align : 'right'
}, {
	title : "总金额",
	field : "totalSalePrice",
	width : 80,
	align : 'right'
}];


instantConContent.getColumnInfo = function(sysNo, beforeColArr, afterColArr) {
	var tempUrl = BasePath + '/initCache/getBrandList.htm';
	var resultData = {};
	$.ajax({
		type : 'POST',
		url : tempUrl,
		data : {
			sysNo : sysNo,
			preColNames : JSON.stringify(beforeColArr),
			endColNames : JSON.stringify(afterColArr),
			sizeTypeFiledName : instantConContent.sizeTypeFiledName
		},
		cache : true,
		async : false,
		success : function(returnData) {
			resultData.columnArr = returnData.returnCols;
			resultData.startType = returnData.startType;
			resultData.maxType = returnData.maxType;
		}
	});
	return resultData;
};

// 清除查询条件
instantConContent.searchClear = function(id){
	$('#'+id).form("clear");
	$('#brandNoCondition').combobox("loadData",[]);
	$('#seasonNameCondition').combobox("loadData",[]);
	$('#genderNameCondition').combobox("loadData",[]);
};

instantConContent.searchContent = function(){
	
	var fromObj=$('#searchForm');
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
		return;
	}
	var sysNo = $('#sysNoCondition').combobox('getValue');
	if(sysNo == ''){
		alert('请选择品牌库!');
		return;
	}
	var fromObjStr=convertArray(fromObj.serializeArray());
	var queryMxURL=BasePath+'/instant_concontent/findInstantConContentList';
	var queryParams = eval("(" +fromObjStr+ ")");
	queryParams['locno'] = instantConContent.locno;
	
	var beforeColArr = instantConContent.preColNames;
	var afterColArr = instantConContent.endColNames;
	var columnInfo = instantConContent.getColumnInfo(sysNo, beforeColArr, afterColArr);
	$("#dataGrid").datagrid({
		queryParams : queryParams,
		url : queryMxURL,
		columns : columnInfo.columnArr
	}); 
};

//初始化表格加载事件
instantConContent.initGridOnLoadSuccess = function() {
	$('#dataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.footer!=undefined){  
				if (data.footer[0] != null ) {	
					instantConContent.detaildataGridFooter=data.footer[0];
				} else {
					var rows = $('#dataGrid').datagrid('getFooterRows');
					data.footer[0]=instantConContent.detaildataGridFooter;
					$('#dataGrid').datagrid('reloadFooter');
				}
			}			
		}
	});
};

instantConContent.initLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			instantConContent.locno = data.locno;
		}
	});
};

instantConContent.initCategory = function(data){
	$('#cateNoCondition').combobox({
		data:data,
	    valueField:"cateNo",
	    textField:"cateName",
	    panelHeight:150
	});
};


//导出报表
instantConContent.do_export = function(){
export4Size("dataGrid", instantConContent.preColNames, instantConContent.endColNames, "即时库存明细", BasePath + '/instant_concontent/doExport');
	
};
function export4Size(dgId,preColNames,endColNames,fileName,url){
	var dgObj = $("#"+dgId);
	var dataRow = dgObj.datagrid('getRows');
	if(dataRow.length > 0){
		var cols = dgObj.datagrid('options').columns;;
		var params = dgObj.datagrid('options').queryParams;
		$("#exportExcelForm").remove();
		$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
		var fromObj = $('#exportExcelForm');
		wms_city_common.loading("show","正在导出");
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {

				param.preColNames = JSON.stringify(preColNames);
				param.endColNames = JSON.stringify(endColNames);
				param.fileName = fileName;

				if (params != null && params != {}) {
					$.each(params, function(i) {
						param[i] = params[i];
					});
				}
				
			},
			success : function() {
			}
		});
		wms_city_common.loading();
	}else{
		alert("没有可导出的数据!");
		return false;
	}
	
	
}

$(document).ready(function(){
	instantConContent.initLoc();
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoCondition'),"brandNoObj":$('input[id=brandNoCondition]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs,[false]);
	
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneCondition',
			'cateTwoCondition',
			'cateThreeCondition',
			true
    );
	
	//初始化表格加载事件
	instantConContent.initGridOnLoadSuccess();
	//根据品牌初始季节和性别
	instantConContent.initGenderAndSeason();
});
instantConContent.initGenderAndSeason =  function(){
	$('#sysNoCondition').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
//	    	initLookupBySysNo('GENDER',sysNo,'genderNameCondition');
//	    	initLookupBySysNo('SEASON',sysNo,'seasonNameCondition');
	    	
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderNameCondition',true,false);
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonNameCondition',true,false);
		}
	});
};
//function initLookupBySysNo(lookupcode,sysNo,comboboxId){
//	var data = null;
//	var url = BasePath+'/lookupdtl/selectLookupdtlBySysNo?lookupcode='+lookupcode+'&sysNo='+sysNo;
//	wms_city_common.ajaxRequest(url,'',false,function(r){data = r;});
//	$('#'+comboboxId).combobox({
//		multiple:true,
//		data:data,
//		valueField:'itemval',    
//	    textField:'itemname',
//	    loadFilter:function(data){
//	    	var tempData = [];
//	    	for(var i=0;i<data.length;i++){
//	    		data[i]['itemname'] = data[i]['itemval'] + '→' + data[i]['itemname'];
//	    		tempData[tempData.length] = data[i];
//	    	}
//	    	return tempData;
//	    }
//	});
//}