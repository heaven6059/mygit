var splitdepot = {};

//加载Grid数据Utils
splitdepot.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

String.prototype.replaceAll  = function(s1,s2){     
    return this.replace(new RegExp(s1,"gm"),s2);     
};

splitdepot.getDate = function(n){
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
splitdepot.searchData = function() {
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
    //商品编码支持多个查询用逗号隔开
    var itemNo = $("#itemNoCondition").val();
    var strItemNo = '';
    var strItemNoOne = '';
    if(itemNo.indexOf(',') > -1||itemNo.indexOf('，') > -1){
    	var strs= new Array();
    	strs=itemNo.replaceAll('，',',').split(","); 
    	for(var i = 0;i<strs.length;i++){
    		if(strs[i]!=""){
    			strItemNo += "'"+strs[i]+"',";
    		}
    	}
    	if(strItemNo!=''){
    		strItemNo=strItemNo.substring(0, strItemNo.length - 1);
    	}
    	strItemNoOne = '';
    }else{
    	strItemNoOne = itemNo;
    }
    
    var starttime = $("#startContentDateCondition").datebox('getValue');
	var endTime = $("#endContentDateCondition").datebox('getValue');
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
	var queryMxURL=BasePath+'/splitdepotinoutreport/listReport.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = splitdepot.locno;
	//reqParam['backContentDate'] = backDate(starttime);//提前一天
	reqParam['strItemNo'] = strItemNo;//字符串模糊查询
	reqParam['itemNo'] = strItemNoOne;//商品编码
	reqParam['brandNoValues'] = brandNoValues;//品牌多选
	splitdepot.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};



//清除查询条件
splitdepot.searchClear = function(id){
	$('#'+id).form("clear");
};


// 初始化用户信息
splitdepot.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		splitdepot.locno = data.locno;
		$('#startContentDateCondition').datebox('setValue',splitdepot.getDate(-30));
		$('#endContentDateCondition').datebox('setValue',data.currentDate10Str);
	}); 
};

$(document).ready(function() {
	splitdepot.initCurrentUser();
	//初始化商品类别
//	wms_city_common.comboboxLoadFilter(
//			["cateCodeCondition"],
//			'cateCode',
//			'cateName',
//			'valueAndText',
//			false,
//			[true],
//			BasePath+'/category/getCategory4Simple?cateLevelid=1',
//			{},
//			null,
//			null);
	
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneCondition',
			'cateTwoCondition',
			'cateThreeCondition',
			true
    );
	
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
	
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNo]',$('#dataForm'))},//新增
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNoMultiple4Cascade(objs);
	//根据品牌初始化性别和季节
	splitdepot.initGenderAndSeason();
	
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				splitdepot.qty = data.footer[1].qty;
		   				splitdepot.crkQty = data.footer[1].crkQty;
		   				splitdepot.cyrQty = data.footer[1].cyrQty;
		   				splitdepot.dthQty = data.footer[1].dthQty;
		   				splitdepot.qtrkQty = data.footer[1].qtrkQty;
		   				splitdepot.ccdQty = data.footer[1].ccdQty;
		   				splitdepot.cycQty = data.footer[1].cycQty;
		   				splitdepot.qtckQty = data.footer[1].qtckQty;
		   				splitdepot.kbmzhQty = data.footer[1].kbmzhQty;
		   				splitdepot.pyQty = data.footer[1].pyQty;
		   				splitdepot.pkQty = data.footer[1].pkQty;
		   				splitdepot.thisIssueQty = data.footer[1].thisIssueQty;
		   			}else{
		   				var rows = $('#dataGridJG').datagrid('getFooterRows');
			   			rows[1]['qty'] = splitdepot.qty;
			   			rows[1]['crkQty'] = splitdepot.crkQty;
			   			rows[1]['cyrQty'] = splitdepot.cyrQty;
			   			rows[1]['dthQty'] = splitdepot.dthQty;
			   			rows[1]['qtrkQty'] = splitdepot.qtrkQty;
			   			rows[1]['ccdQty'] = splitdepot.ccdQty;
			   			rows[1]['cycQty'] = splitdepot.cycQty;
			   			rows[1]['qtckQty'] = splitdepot.qtckQty;
			   			rows[1]['kbmzhQty'] = splitdepot.kbmzhQty;
			   			rows[1]['pyQty'] = splitdepot.pyQty;
			   			rows[1]['pkQty'] = splitdepot.pkQty;
			   			rows[1]['thisIssueQty'] = splitdepot.thisIssueQty;
			   			$('#dataGridJG').datagrid('reloadFooter');
		   			}
		   		}
			}
	);
	
});

splitdepot.initGenderAndSeason =  function(){
	$('#sysNoSearch').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonCondition',true,false);
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderCondition',true,false);
		}
	});
};

splitdepot.columnsJsonList = [[
							 		{title:'商品编码',field:'itemNo',width:150,align:'left',rowspan: 2},
							 		{title:'名称',field:'itemName',width:150,align:'right',rowspan: 2},
							 		{title:'颜色',field:'colorName',width:80,align:'right',rowspan: 2},
							 		{title:'品牌',field:'strBrandNo',width:80,align:'right',rowspan: 2},
							 		{title:'大类',field:'strCateNo',width:80,align:'right',rowspan: 2},
							 		{title:'年份',field:'strYears',width:80,align:'right',rowspan: 2},
							 		{title:'上期库存',field:'qty',width:80,align:'right',rowspan: 2},
							 		{title:'本期库存',field:'thisIssueQty',width:80,align:'right',rowspan: 2},
							 		{title:'入库',field:null,width:320,colspan: 5},
							 		{title:'出库',field:null,width:320,colspan: 5},
							 		{title:'盘点',field:null,width:160,colspan: 2}
							 	  ],[
							 		{title:'厂入库',field:'crkQty',width:80,align:'right'},
							 		{title:'仓移入',field:'cyrQty',width:80,align:'right'},
							 		{title:'店退货',field:'dthQty',width:80,align:'right'},
							 		{title:'其他入库',field:'qtrkQty',width:80,align:'right'},
							 		{title:'差异调整',field:'rkCytzQty',width:80,align:'right'},
							 		{title:'仓出店',field:'ccdQty',width:80,align:'right'},
							 		{title:'仓移出',field:'cycQty',width:80,align:'right'},
							 		{title:'其他出库',field:'qtckQty',width:80,align:'right'},
							 		{title:'跨部门转货',field:'kbmzhQty',width:80,align:'right'},
							 		{title:'差异调整',field:'ckCytzQty',width:80,align:'right'},
							 		{title:'盘赢',field:'pyQty',width:80,align:'right'},
							 		{title:'盘亏',field:'pkQty',width:80,align:'right'}
							 	  ]];
//导出
splitdepot.do_export = function(){
	var dataGridId = 'dataGridJG';
	var exportUrl = '/splitdepotinoutreport/doExport';
	var excelTitle = '分仓库存进出明细报表';
	var $dg = $("#" + dataGridId + "");
	var params = $dg.datagrid('options').queryParams;
	
	var exportColumns = JSON.stringify(splitdepot.columnsJsonList);
	var url = BasePath + exportUrl;
	var dataRow = $dg.datagrid('getRows');
	
	$("#exportExcelForm").remove();
	$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
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
				wms_city_common.loading();
			}
		});
	} else {
		alert('查询记录为空，不能导出!', 1);
	}
	
};
