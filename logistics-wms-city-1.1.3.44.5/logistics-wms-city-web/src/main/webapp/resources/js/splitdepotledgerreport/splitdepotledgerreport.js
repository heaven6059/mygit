
var splitdepotledgerreport = {};
splitdepotledgerreport.footerSumData={};

//加载Grid数据Utils
splitdepotledgerreport.loadGridDataUtil = function(gridDataId,url,queryParams,columns){
    var tempObj = $('#'+gridDataId);    
    tempObj.datagrid({
		queryParams : queryParams,
		url : url,
		columns : columns
	});
};

//获取表头定义
splitdepotledgerreport.getDataGridJGColumns = function(){
	var sysNoCondition=$("#sysNoCondition").combobox("getValue");
	var qualityCondition=$("#qualityCondition").combobox("getValues");
	var cateOneCondition=$("#cateOneCondition").combobox("getValues");
	var cateTwoCondition=$("#cateTwoCondition").combobox("getValues");
	var cateThreeCondition=$("#cateThreeCondition").combobox("getValues");
	
	var genderCondition=$("#genderCondition").combobox("getValues");
	var yearsCondition=$("#yearsCondition").val();
	var seasonCondition=$("#seasonCondition").combobox("getValues");
	
	var columns="";
	
	columns+="[[";
	columns+="{title:'仓库名称',field:'locName',width:100,align:'left',rowspan: 2},";
	
	if (sysNoCondition != '') {
		columns+="{title:'品牌',field:'brandName',width:80,align:'left',rowspan: 2},";
	}
	
	if (cateOneCondition!=null && cateOneCondition.length > 0 && cateOneCondition[0] != '') {
		columns+="{title:'大类一',field:'cateOneName',width:80,align:'left',rowspan: 2},";
	}
	
	if (cateTwoCondition!=null && cateTwoCondition.length > 0 && cateTwoCondition[0] != '') {
		columns+="{title:'大类二',field:'cateTwoName',width:80,align:'left',rowspan: 2},";
	}
	
	if (cateThreeCondition!=null && cateThreeCondition.length > 0 && cateThreeCondition[0] != '') {
		columns+="{title:'大类三',field:'cateThreeName',width:80,align:'left',rowspan: 2},";
	}
	
	if (qualityCondition!=null && qualityCondition.length > 0 && qualityCondition[0] != '') {
		columns+="{title:'品质',field:'qualityName',width:80,align:'left',rowspan: 2},";
	}
	
	if(genderCondition!=null && genderCondition.length>0 && genderCondition[0] != ''){
		columns+="{title:'性别',field:'gender',width:80,align:'left',rowspan: 2},";		
	}
	if(yearsCondition !=''){
		columns+="{title:'年份',field:'years',width:80,align:'left',rowspan: 2},";		
	}
	if(seasonCondition!='' && seasonCondition.length >0 && seasonCondition[0] != ''){
		columns+="{title:'季节',field:'season',width:80,align:'left',rowspan: 2},";		
	}
	
	columns+="{title:'上期库存',field:'lastIssueQty',width:80,align:'right',rowspan: 2},";
	columns+="{title:'入库(已入库)',field:null,width:320,colspan: 4},";
	columns+="{title:'出库',field:null,width:240,colspan: 4},";
	columns+="{title:'盘点',field:null,width:160,colspan: 2},";
	columns+="{title:'',field:null,width:160},";
	columns+="{title:'库存调整',field:null,width:160,colspan: 2},";
	columns+="{title:'预计到货未收',field:null,width:240,colspan: 3},";
	columns+="{title:'已收未验',field:null,width:240,colspan: 3},";
	columns+="{title:'已收货数',field:null,width:240,colspan: 3}";
	columns+="],[";
	columns+="{title:'厂入库',field:'rkCrkQty',width:80,align:'right'},";
	columns+="{title:'仓移入',field:'rkCyrQty',width:80,align:'right'},";
	columns+="{title:'店退货',field:'rkDthQty',width:80,align:'right'},";
	columns+="{title:'其他入库',field:'rkQtrkQty',width:80,align:'right'},";
	columns+="{title:'仓出店',field:'ckCcdQty',width:80,align:'right'},";
	columns+="{title:'仓移出',field:'ckCycQty',width:80,align:'right'},";
	columns+="{title:'其他出库',field:'ckQtckQty',width:80,align:'right'},";
	columns+="{title:'跨部门转货',field:'ckKbmzhQty',width:80,align:'right'},";
	columns+="{title:'盘赢',field:'pdPyQty',width:80,align:'right'},";
	columns+="{title:'盘亏',field:'pdPkQty',width:80,align:'right'},";
	columns+="{title:'本期库存',field:'thisIssueQty',width:80,align:'right',rowspan: 2},";
	columns+="{title:'品质',field:'tzKctzQty',width:80,align:'right'},";
	columns+="{title:'属性',field:'tzKctzTypeQty',width:80,align:'right'},";
	columns+="{title:'厂入',field:'ydhCrQty',width:80,align:'right'},";
	columns+="{title:'仓移入',field:'ydhCyrQty',width:80,align:'right'},";
	columns+="{title:'店退仓',field:'ydhDtcQty',width:80,align:'right'},";
	columns+="{title:'厂入',field:'yswyCrQty',width:80,align:'right'},";
	columns+="{title:'仓移入',field:'yswyCyrQty',width:80,align:'right'},";
	columns+="{title:'店退仓',field:'yswyDtcQty',width:80,align:'right'},";
	columns+="{title:'厂入',field:'yshCrQty',width:80,align:'right'},";
	columns+="{title:'仓移入',field:'yshCyrQty',width:80,align:'right'},";
	columns+="{title:'店退仓',field:'yshDtcQty',width:80,align:'right'}";
	columns+="]]";
	
	return eval(columns);
};

splitdepotledgerreport.getDate = function(n){
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
splitdepotledgerreport.isStartEndDate = function(startDate,endDate){
    if(startDate.length>0&&endDate.length>0){   
        var arrStartDate = startDate.split("-");   
        var arrEndDate = endDate.split("-");   
        var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]);   
        var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]);   
        if(allStartDate.getTime()>allEndDate.getTime()){  
        	alert("开始日期不能大于结束日期!");
            return false;   
        }
        else if(((allEndDate.getTime()-allStartDate.getTime())/(24000*3600))>31){
			alert("只能查31天以内的数据,请缩小日期范围!");
			return false;
		}
     }   
     return true;   
};

//后退一天
splitdepotledgerreport.backDate = function(startDate) {
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
};


// 查询脚本
splitdepotledgerreport.searchData = function() {
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
	if(!splitdepotledgerreport.isStartEndDate(starttime,endTime)){
		return;
	}
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/splitdepotledgerreport/listReport.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = splitdepotledgerreport.locno;
	//reqParam['startDate'] = splitdepotledgerreport.backDate(starttime);//提前一天

	var columns=splitdepotledgerreport.getDataGridJGColumns();
	
	$('#dataGridJG').datagrid('loadData', { total: 0, rows: [] });
	
	splitdepotledgerreport.loadGridDataUtil('dataGridJG', queryMxURL, reqParam,columns);
};


//清除查询条件
splitdepotledgerreport.searchClear = function(id){
	$('#'+id).form("clear");
};


// 初始化用户信息
splitdepotledgerreport.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		splitdepotledgerreport.locno = data.locno;
		$('#startDateCondition').datebox('setValue',splitdepotledgerreport.getDate(-30));
		$('#endDateCondition').datebox('setValue',data.currentDate10Str);
	}); 
};

$(document).ready(function() {
	
	splitdepotledgerreport.initCurrentUser();
	
	//初始化商品品质
	wms_city_common.comboboxLoadFilter4Multiple(
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
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoCondition'),"brandNoObj":$('#brandNoCondition')}
			);
	wms_city_common.loadSysNoMultiple4Cascade(objs);	
	
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneCondition',
			'cateTwoCondition',
			'cateThreeCondition',
			true
		);
			
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer){
						if(data.footer[1].isselectsum){
			   				splitdepotledgerreport.footerSumData = data.footer[1];
			   			}else{
			   				var rows = $('#dataGridJG').datagrid('getFooterRows');
				   			rows[1] = splitdepotledgerreport.footerSumData;
				   			$('#dataGridJG').datagrid('reloadFooter');
			   			}
					}		   			
		   		}
			}
	);
	
	//根据品牌初始季节和性别
	splitdepotledgerreport.initGenderAndSeason();
});
splitdepotledgerreport.initGenderAndSeason =  function(){
	$('#sysNoCondition').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonCondition',true,false);
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderCondition',true,false);
		}
	});
};
splitdepotledgerreport.doExport = function(){
	var url = BasePath + '/splitdepotledgerreport/doExport';
	var excelTitle = '分仓库存总账报表';
	var $dg = $("#dataGridJG");
	var dataRow = $dg.datagrid('getRows');
	if (dataRow.length > 0) {
		var columns = $dg.datagrid('options').columns;
		var rsCol = splitdepotledgerreport.formatCol(columns);
		var params = $dg.datagrid('options').queryParams;
		var firstRowNum = rsCol[0].length;
		$("#exportExcelForm").remove();
		$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
		var fromObj = $('#exportExcelForm');
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {

				param.exportColumns = JSON.stringify(rsCol);
				param.fileName = excelTitle;
				param.firstRowNum = firstRowNum;

				if (params != null && params != {}) {
					$.each(params, function(i) {
						param[i] = params[i];
					});
				}

			},
			success : function() {

			}
		});
	}else{
		alert('查询记录为空，不能导出!', 1);
	}
	
};
splitdepotledgerreport.formatCol = function(columns){
	var rs = [];
	for(var idx=0;idx<columns.length;idx++){
		var array = columns[idx];
		var temp = [];
		for(var i=0;i<array.length;i++){
			var p = {};
			p.field = array[i].field;
			p.title = array[i].title;
			temp[i] = p;
		}
		rs[idx] = temp;
	}
	return rs;
};