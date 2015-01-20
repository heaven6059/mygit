
var inoutdiffreport = {};
inoutdiffreport.footerSumData={};

//加载Grid数据Utils
inoutdiffreport.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);    
    tempObj.datagrid({
		queryParams : queryParams,
		url : url
	});
};


inoutdiffreport.getDate = function(n){
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
inoutdiffreport.isStartEndDate = function(startDate,endDate){
    if(startDate.length>0&&endDate.length>0){   
        var arrStartDate = startDate.split("-");   
        var arrEndDate = endDate.split("-");   
        var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]);   
        var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]);   
        if(allStartDate.getTime()>allEndDate.getTime()){  
        	alert("开始日期不能大于结束日期!");
            return false;   
        }
//        else if(((allEndDate.getTime()-allStartDate.getTime())/(24000*3600))>31){
//			alert("只能查31天以内的数据,请缩小日期范围!");
//			return false;
//		}
     }   
     return true;   
};


// 查询脚本
inoutdiffreport.searchData = function() {
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
//    var starttime = $("#startDateCondition").datebox('getValue');
//	var endTime = $("#endDateCondition").datebox('getValue');
//    if(starttime==""){
//		alert("请选择开始日期");
//		return;
//	}
//	if(endTime==""){
//		alert("请选择结束日期");
//		return;
//	}
//	if(!inoutdiffreport.isStartEndDate(starttime,endTime)){
//		return;
//	}
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/inoutdiffreport/listReport.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = inoutdiffreport.locno;
	
	$('#dataGridJG').datagrid('loadData', { total: 0, rows: [] });
	
	inoutdiffreport.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};


//清除查询条件
inoutdiffreport.searchClear = function(id){
	$('#'+id).form("clear");
};


// 初始化用户信息
inoutdiffreport.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		inoutdiffreport.locno = data.locno;
		$('#startDateCondition').datebox('setValue',inoutdiffreport.getDate(-30));
		$('#endDateCondition').datebox('setValue',data.currentDate10Str);
	}); 
};

$(document).ready(function() {
	
	inoutdiffreport.initCurrentUser();
	
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

	//初始化状态
	$('#billTypeCondition').combobox({
		onSelect:function(record){
			var lookupcode='';
			if(record.itemvalue=='1'){
				lookupcode='CITY_IMPORTOR_STATUS';
			}else if(record.itemvalue=='2'){
				lookupcode='UMUNTREAD_STATUS';
			}else if(record.itemvalue=='3' || record.itemvalue=='4'){
				lookupcode='CITY_OEM_STATUS';
			}
			
			//初始化单据类型
			wms_city_common.comboboxLoadFilter(
					["statusCondition"],
					null,
					null,
					null,
					true,
					[true],
					BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+lookupcode,
					{},
					null,
					null);
		}
	});
	
	//初始化单据类型
	wms_city_common.comboboxLoadFilter(
			["billTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CONDIFF_REPORT_BILLTYPE',
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
	//根据品牌初始化季节和性别
	inoutdiffreport.initGenderAndSeason();
			
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer){
						if(data.footer[1].isselectsum){
			   				inoutdiffreport.footerSumData = data.footer[1];
			   			}else{
			   				var rows = $('#dataGridJG').datagrid('getFooterRows');
				   			rows[1] = inoutdiffreport.footerSumData;
				   			$('#dataGridJG').datagrid('reloadFooter');
			   			}
					}		   			
		   		}
			}
	);
	
});
inoutdiffreport.initGenderAndSeason =  function(){
	$('#sysNoCondition').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonCondition',true,false);
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderCondition',true,false);
		}
	});
};
inoutdiffreport.doExport = function(){
	var url = BasePath + '/inoutdiffreport/doExport';
	var excelTitle = '差异报表';
	var $dg = $("#dataGridJG");
	var dataRow = $dg.datagrid('getRows');
	if (dataRow.length > 0) {
		var columns = $dg.datagrid('options').columns;
		var rsCol = inoutdiffreport.formatCol(columns);
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
inoutdiffreport.formatCol = function(columns){
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