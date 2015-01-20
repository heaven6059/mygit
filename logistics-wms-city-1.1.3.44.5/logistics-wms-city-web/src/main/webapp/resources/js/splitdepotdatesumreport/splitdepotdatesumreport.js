
var splitdepotdate = {};

//加载Grid数据Utils
splitdepotdate.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

splitdepotdate.getDate = function(n){
	var nowDate = new Date();
	var newDate = new Date();
	var newTimes = nowDate.getTime() + (n*24*3600000);
	newDate.setTime(newTimes);
	var y = newDate.getFullYear();
	var m = newDate.getMonth()+1;
	var d = newDate.getDate();
	var dateStr = y+"-";
	dateStr += (m<10?("0"+m):(m))+"-";
	dateStr += (d<10?("0"+d):(d));
	return dateStr;
};

//校验开始日期和结束日期的大小
function isStartEndDate(startDate,endDate){
    if(startDate.length>0&&endDate.length>0){   
        var arrStartDate = startDate.split("-");   
        var arrEndDate = endDate.split("-");   
        var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]);   
        var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]);   
        if(allStartDate.getTime()>allEndDate.getTime()){  
        	alert("开始日期不能大于结束日期!");
            return false;   
        }else if(((allEndDate.getTime()-allStartDate.getTime())/(24000*3600))>31){
			alert("只能查31天以内的数据,请缩小日期范围!");
			return false;
		}else if(endDate > splitdepotdate.currentDate){
			alert("结束日期不能大于当天日期!");
			return false;
		}
     }   
     return true;   
} 

//后退一天
function backDate(startDate) {
	if(startDate.length>0){  
		var arrStartDate = startDate.split("-");   
		var today = new Date(arrStartDate[0], arrStartDate[1],arrStartDate[2]);
		var yesterday_milliseconds = today.getTime() - 1000 * 60 * 60 * 24;
		var yesterday = new Date();
		yesterday.setTime(yesterday_milliseconds);
		var strYear = yesterday.getFullYear();
		var strDay = yesterday.getDate();
		if (strDay < 10) {
			strDay = "0" + strDay;
		}
		var strMonth = yesterday.getMonth();
		if (strMonth < 10) {
			strMonth = "0" + strMonth;
		}
		var strYesterday = strYear + "-" + strMonth + "-" + strDay;
		return strYesterday;
	}
	return null;
}


// 查询脚本
splitdepotdate.searchData = function() {
	
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
    var starttime = $("#startDateCondition").datebox('getValue');
	var endTime = $("#endDateCondition").datebox('getValue');
    if(starttime==""){
		alert("请选择开始日期");
		return;
	}
	if(endTime==""){
		alert("请选择结束日期");
		return;
	}
	if(!isStartEndDate(starttime,endTime)){
		return;
	}
	
	//品牌库多选
	var brandNoValues = "";
	var values = $('#brandNo').combobox('getValues');
	if(values.length > 0){
		for(var i=0; i< values.length;i++){
    		if(values[i]!=''){				    			
    			brandNoValues += "'"+values[i]+"',";
    		}
    	}		
	}
	if(brandNoValues!=""){
		brandNoValues=brandNoValues.substring(0, brandNoValues.length-1);
	}
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/splitdepotdatesumreport/listReport.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = splitdepotdate.locno;
	reqParam['brandNoValues'] = brandNoValues;//提前一天
	splitdepotdate.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};


//清除查询条件
splitdepotdate.searchClear = function(id){
	$('#'+id).form("clear");
};

splitdepotdate.columnsJsonList = [[
							 		{title:'日期',field:'selectDate',width:100,align:'left',rowspan: 2},
							 		{title:'上期库存',field:'qty',width:80,align:'right',rowspan: 2},
							 		{title:'本期库存',field:'thisIssueQty',width:80,align:'right',rowspan: 2},
							 		{title:'入库(已入库)',field:null,width:320,colspan: 5},
							 		{title:'出库',field:null,width:320,colspan: 5},
							 		{title:'盘点',field:null,width:160,colspan: 2},
							 		{title:'库存调整',field:null,width:160,colspan: 2},
							 		{title:'预计到货未收',field:null,width:240,colspan: 6},
							 		{title:'已收未验',field:null,width:240,colspan: 6},
							 		{title:'已收货数',field:null,width:240,colspan: 6}
							 	  ],[
							 		{title:'厂入库',field:'rkCrkQty',width:80,align:'right'},
							 		{title:'仓移入',field:'rkCyrQty',width:80,align:'right'},
							 		{title:'店退货',field:'rkDthQty',width:80,align:'right'},
							 		{title:'其他入库',field:'rkQtrkQty',width:80,align:'right'},
							 		{title:'差异调整',field:'rkCytzQty',width:80,align:'right'},
							 		{title:'仓出店',field:'ckCcdQty',width:80,align:'right'},
							 		{title:'仓移出',field:'ckCycQty',width:80,align:'right'},
							 		{title:'其他出库',field:'ckQtckQty',width:80,align:'right'},
							 		{title:'跨部门转货',field:'ckKbmzhQty',width:80,align:'right'},
							 		{title:'差异调整',field:'ckCytzQty',width:80,align:'right'},
							 		{title:'盘赢',field:'pdPyQty',width:80,align:'right'},
							 		{title:'盘亏',field:'pdPkQty',width:80,align:'right'},
							 		{title:'品质',field:'tzKctzQty',width:80,align:'right'},
							 		{title:'属性',field:'tzKctzTypeQty',width:80,align:'right'},
							 		{title:'厂入',field:'ydhCrQty',width:80,align:'right'},
							 		{title:'厂入箱数',field:'ydhCrBoxQty',width:80,align:'right'},
							 		{title:'仓移入',field:'ydhCyrQty',width:80,align:'right'},
							 		{title:'仓移入箱数',field:'ydhCyrBoxQty',width:80,align:'right'},
							 		{title:'店退仓',field:'ydhDtcQty',width:80,align:'right'},
							 		{title:'店退仓箱数',field:'ydhDtcBoxQty',width:80,align:'right'},
							 		{title:'厂入',field:'yswyCrQty',width:80,align:'right'},
							 		{title:'厂入箱数',field:'yswyCrBoxQty',width:80,align:'right'},
							 		{title:'仓移入',field:'yswyCyrQty',width:80,align:'right'},
							 		{title:'仓移入箱数',field:'yswyCyrBoxQty',width:80,align:'right'},
							 		{title:'店退仓',field:'yswyDtcQty',width:80,align:'right'},
							 		{title:'店退仓箱数',field:'yswyDtcBoxQty',width:80,align:'right'},
							 		{title:'厂入',field:'yshCrQty',width:80,align:'right'},
							 		{title:'厂入箱数',field:'yshCrBoxQty',width:80,align:'right'},
							 		{title:'仓移入',field:'yshCyrQty',width:80,align:'right'},
							 		{title:'仓移入箱数',field:'yshCyrBoxQty',width:80,align:'right'},
							 		{title:'店退仓',field:'yshDtcQty',width:80,align:'right'},
							 		{title:'店退仓箱数',field:'yshDtcBoxQty',width:80,align:'right'}
							 	  ]];
splitdepotdate.doExport = function(id){
	//exportExcelRepInfo('dataGridJG','/splitdepotdatesumreport/doExport','分仓库存按天汇总报表');
	var dataGridId = 'dataGridJG';
	var exportUrl = '/splitdepotdatesumreport/doExport';
	var excelTitle = '分仓库存按天汇总报表';
	var $dg = $("#" + dataGridId + "");
	var params = $dg.datagrid('options').queryParams;
	// var params = $("#searchForm").serializeArray();
	//var columns = $dg.datagrid('options').columns;
	var exportColumns = JSON.stringify(splitdepotdate.columnsJsonList);
	var url = BasePath + exportUrl;
	var dataRow = $dg.datagrid('getRows');

	$("#exportExcelForm").remove();

	$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
	;

	var fromObj = $('#exportExcelForm');

	if (dataRow.length > 0) {
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {

				param.exportColumns = exportColumns;
				param.fileName = excelTitle;

				if (params != null && params != {}) {
					$.each(params, function(i) {
						param[i] = params[i];
					});
				}

			},
			success : function() {

			}
		});
	} else {
		alert('查询记录为空，不能导出!', 1);

	}
};
// 初始化用户信息
splitdepotdate.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		splitdepotdate.locno = data.locno;
		$('#startDateCondition').datebox('setValue',splitdepotdate.getDate(-30));
		$('#endDateCondition').datebox('setValue',data.currentDate10Str);
		splitdepotdate.currentDate = data.currentDate10Str;
	}); 
};

$(document).ready(function() {
	splitdepotdate.initCurrentUser();
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
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneCondition',
			'cateTwoCondition',
			'cateThreeCondition',
			true
    );
	
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNo]',$('#dataForm'))},//新增
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNoMultiple4Cascade(objs);
	
	
//	$('#dataGridJG').datagrid(
//			{
//				'onLoadSuccess':function(data){
//		   			if(data.footer[1].isselectsum){
//		   				splitdepotdate.qty = data.footer[1].qty;
//		   				splitdepotdate.rkCrkQty = data.footer[1].rkCrkQty;
//		   				splitdepotdate.rkCyrQty = data.footer[1].rkCyrQty;
//		   				splitdepotdate.rkDthQty = data.footer[1].rkDthQty;
//		   				splitdepotdate.rkQtrkQty = data.footer[1].rkQtrkQty;
//		   				splitdepotdate.ckCcdQty = data.footer[1].ckCcdQty;
//		   				splitdepotdate.ckCycQty = data.footer[1].ckCycQty;
//		   				splitdepotdate.qtckQty = data.footer[1].ckQtckQty;
//		   				splitdepotdate.pdPyQty = data.footer[1].pdPyQty;
//		   				splitdepotdate.pdPkQty = data.footer[1].pdPkQty;
//		   				splitdepotdate.tzKctzQty = data.footer[1].tzKctzQty;
//		   				splitdepotdate.ydhCrQty = data.footer[1].ydhCrQty;
//		   				splitdepotdate.ydhCyrQty = data.footer[1].ydhCyrQty;
//		   				splitdepotdate.ydhDtcQty = data.footer[1].ydhDtcQty;
//		   				splitdepotdate.yswyCrQty = data.footer[1].yswyCrQty;
//		   				splitdepotdate.yswyCyrQty = data.footer[1].yswyCyrQty;
//		   				splitdepotdate.yswyDtcQty = data.footer[1].yswyDtcQty;
//		   				splitdepotdate.yshCrQty = data.footer[1].yshCrQty;
//		   				splitdepotdate.yshCyrQty = data.footer[1].yshCyrQty;
//		   				splitdepotdate.yshDtcQty = data.footer[1].yshDtcQty;
//		   			}else{
//		   				var rows = $('#dataGridJG').datagrid('getFooterRows');
//			   			rows[1]['qty'] = splitdepotdate.qty;
//			   			rows[1]['rkCrkQty'] = splitdepotdate.rkCrkQty;
//			   			rows[1]['rkCyrQty'] = splitdepotdate.rkCyrQty;
//			   			rows[1]['rkDthQty'] = splitdepotdate.rkDthQty;
//			   			rows[1]['rkQtrkQty'] = splitdepotdate.rkQtrkQty;
//			   			rows[1]['ckCcdQty'] = splitdepotdate.ckCcdQty;
//			   			rows[1]['ckCycQty'] = splitdepotdate.ckCycQty;
//			   			rows[1]['ckQtckQty'] = splitdepotdate.ckQtckQty;
//			   			rows[1]['pdPyQty'] = splitdepotdate.pdPyQty;
//			   			rows[1]['pdPkQty'] = splitdepotdate.pdPkQty;
//			   			rows[1]['tzKctzQty'] = splitdepotdate.tzKctzQty;
//			   			rows[1]['ydhCrQty'] = splitdepotdate.ydhCrQty;
//			   			rows[1]['ydhCyrQty'] = splitdepotdate.ydhCyrQty;
//			   			rows[1]['ydhDtcQty'] = splitdepotdate.ydhDtcQty;
//			   			rows[1]['yswyCrQty'] = splitdepotdate.yswyCrQty;
//			   			rows[1]['yswyCyrQty'] = splitdepotdate.yswyCyrQty;
//			   			rows[1]['yswyDtcQty'] = splitdepotdate.yswyDtcQty;
//			   			rows[1]['yshCrQty'] = splitdepotdate.yshCrQty;
//			   			rows[1]['yshCyrQty'] = splitdepotdate.yshCyrQty;
//			   			rows[1]['yshDtcQty'] = splitdepotdate.yshDtcQty;
//			   			$('#dataGridJG').datagrid('reloadFooter');
//		   			}
//		   		}
//			}
//	);
	//根据品牌初始化季节和性别
	splitdepotdate.initGenderAndSeason();
	
});
splitdepotdate.initGenderAndSeason =  function(){
	$('#sysNoSearch').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonCondition',true,false);
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderCondition',true,false);
		}
	});
};