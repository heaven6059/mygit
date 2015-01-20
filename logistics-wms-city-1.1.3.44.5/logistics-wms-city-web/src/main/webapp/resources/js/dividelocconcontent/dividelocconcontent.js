
var dividelocconcontent = {};

dividelocconcontent.detaildataGridFooter = {};

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

dividelocconcontent.sizeTypeFiledName = 'sizeKind';

dividelocconcontent.preColNames = [ {
	title : "商品编码",
	field : "itemNo",
	width : 145
}, {
	title : "颜色",
	field : "colorName",
	width : 80
}, {
	title : "名称",
	field : "itemName",
	width : 200
}, {
	title : "品牌",
	field : "brandName",
	width : 80
}, {
	title : "大类",
	field : "cateName",
	width : 80
}, {
	title : "商品品质",
	field : "qualityName",
	width : 80
}, {
	title : "年份",
	field : "yearsName",
	width : 60
}, {
	title : "季节",
	field : "seasonName",
	width : 60
}, {
	title : "性别",
	field : "genderName",
	width : 60
} ];

dividelocconcontent.endColNames = [ {
	title : "合计",
	field : "qty",
	width : 80,
	align : 'right'
} ];

dividelocconcontent.getColumnInfo = function(sysNo, beforeColArr, afterColArr) {
	var tempUrl = BasePath + '/initCache/getBrandList.htm';
	var resultData = {};
	$.ajax({
		type : 'POST',
		url : tempUrl,
		data : {
			sysNo : sysNo,
			preColNames : JSON.stringify(beforeColArr),
			endColNames : JSON.stringify(afterColArr),
			sizeTypeFiledName : dividelocconcontent.sizeTypeFiledName
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
dividelocconcontent.searchClear = function(id) {
	$('#' + id).form("clear");
};

dividelocconcontent.searchContent = function() {

	var fromObj = $('#searchForm');
	var validateForm = fromObj.form('validate');
	if (validateForm == false) {
		return;
	}
	var sysNo = $('#sysNoCondition').combobox('getValue');
	if (sysNo == '') {
		alert('请选择品牌库!');
		return;
	}
	var fromObjStr = convertArray(fromObj.serializeArray());
	var queryMxURL = BasePath + '/divide_loc_concontent/cclist';
	//var queryMxURL=BasePath+'/instant_concontent/findInstantConContentList';
	var queryParams = eval("(" + fromObjStr + ")");
	queryParams['locno'] = dividelocconcontent.locno;

	var beforeColArr = dividelocconcontent.preColNames;
	var afterColArr = dividelocconcontent.endColNames;
	var columnInfo = dividelocconcontent.getColumnInfo(sysNo, beforeColArr,
			afterColArr);
	$("#dataGrid").datagrid({
		queryParams : queryParams,
		url : queryMxURL,
		columns : columnInfo.columnArr
	});
};
dividelocconcontent.sumFoot = {};
// 初始化表格加载事件
dividelocconcontent.initGridOnLoadSuccess = function() {
	$('#dataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if (data.footer != undefined) {
				if (data.footer[0] != null) {
					dividelocconcontent.sumFoot = data.footer[0];
				} else {
					var rows = $('#dataGrid').datagrid('getFooterRows');
					data.footer[0] = dividelocconcontent.sumFoot;
					$('#dataGrid').datagrid('reloadFooter');
				}
			}
		}
	});
};

dividelocconcontent.initLoc = function() {
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url : BasePath + '/initCache/getCurrentUser',
		success : function(data) {
			dividelocconcontent.locno = data.locno;
		}
	});
};

dividelocconcontent.initCategory = function(data) {
	$('#cateNoCondition').combobox({
		data : data,
		valueField : "cateNo",
		textField : "cateName",
		panelHeight : 150
	});
};

// 导出报表
dividelocconcontent.doExport = function() {
	
	export4Size("dataGrid", dividelocconcontent.preColNames, dividelocconcontent.endColNames, "分仓库存明细", BasePath + '/divide_loc_concontent/doExport');
	
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
$(document).ready(function() {
	dividelocconcontent.initLoc();
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoCondition'),
		"brandNoObj" : $('input[id=brandNoCondition]', $('#searchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs, [ false ]);
	// 初始化表格加载事件
	dividelocconcontent.initGridOnLoadSuccess();
	//初始化商品类别
	wms_city_common.comboboxLoadFilter(
			["cateCodeCondition"],
			'cateCode',
			'cateName',
			'valueAndText',
			false,
			[true],
			BasePath+'/category/getCategory4Simple?cateLevelid=1',
			{},
			null,
			null);
	//初始化商品属性
	wms_city_common.comboboxLoadFilter(
			["itemTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
			{},
			null,
			null);
	//初始化商品品质
	wms_city_common.comboboxLoadFilter(
			["qualityCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			null,
			null);
	//根据品牌初始化季节和性别
	dividelocconcontent.initGenderAndSeason();
	
});
dividelocconcontent.initGenderAndSeason =  function(){
	$('#sysNoCondition').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonNameCondition',true,false);
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderNameCondition',true,false);
		}
	});
};
