
var bizbilldetailsreport = {};
bizbilldetailsreport.footerSumData={};

//加载Grid数据Utils
bizbilldetailsreport.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);    
    tempObj.datagrid({
		queryParams : queryParams,
		url : url
	});
};


bizbilldetailsreport.getDate = function(n){
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
bizbilldetailsreport.isStartEndDate = function(startDate,endDate){
    if(startDate.length>0&&endDate.length>0){   
        var arrStartDate = startDate.split("-");   
        var arrEndDate = endDate.split("-");   
        var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]);   
        var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]);   
        if(allStartDate.getTime()>allEndDate.getTime()){  
        	alert("开始日期不能大于结束日期!");
            return false;   
        }
     }   
     return true;   
};



// 查询脚本
bizbilldetailsreport.searchData = function() {
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
    var starttime = $("#startDateCondition").datebox('getValue');
	var endTime = $("#endDateCondition").datebox('getValue');
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bizbilldetailsreport/listReport.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = bizbilldetailsreport.locno;
		
	bizbilldetailsreport.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};


//清除查询条件
bizbilldetailsreport.searchClear = function(id){
	$('#'+id).form("clear");
};


// 初始化用户信息
bizbilldetailsreport.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		bizbilldetailsreport.locno = data.locno;
		$('#startDateCondition').datebox('setValue',bizbilldetailsreport.getDate(-30));
		$('#endDateCondition').datebox('setValue',data.currentDate10Str);
	}); 
};

$(document).ready(function() {
	
	bizbilldetailsreport.initCurrentUser();
	
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
	
	//初始化业务单据明细报表单据类型
	wms_city_common.comboboxLoadFilter(
			["billTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_BIZDTS_RPT_BILLTYPE',
			{},
			null,
			bizbilldetailsreport.sortLoadFilter);
	
	//初始化业务单据明细报表单据状态
	wms_city_common.comboboxLoadFilter(
			["finishStatusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_BIZDTS_RPT_STATUS',
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
			
	//根据品牌初始化季节、性别
	bizbilldetailsreport.initGenderAndSeason();
	
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer){
						if(data.footer[1].isselectsum){
			   				bizbilldetailsreport.footerSumData = data.footer[1];
			   			}else{
			   				var rows = $('#dataGridJG').datagrid('getFooterRows');
				   			rows[1] = bizbilldetailsreport.footerSumData;
				   			$('#dataGridJG').datagrid('reloadFooter');
			   			}
					}		   			
		   		}
			}
	);
	
});
bizbilldetailsreport.initGenderAndSeason =  function(){
	$('#sysNoCondition').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonCondition',true,false);
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderCondition',true,false);
		}
	});
};
// 下拉数据排序
bizbilldetailsreport.sortLoadFilter = function(data) {
	var valueNo = 'itemvalue';
	var valueName = 'itemname';
	var showName = 'itemnamedetail';

	var first = {};
	first[valueNo] = '';
	first[showName] = '全选';
	var tempData = [];
	tempData[tempData.length] = first;

	//排序
	data.sort(function(a, b) {
		return a[valueNo] - b[valueNo];
	});

	for ( var i = 0; i < data.length; i++) {
		data[i][showName] = data[i][valueNo] + '→' + data[i][valueName];
		tempData[tempData.length] = data[i];
	}
	return tempData;
};

bizbilldetailsreport.doExport = function(){
	var url = BasePath + '/bizbilldetailsreport/doExport';
	var excelTitle = '业务单据明细报表';
	var $dg = $("#dataGridJG");
	var dataRow = $dg.datagrid('getRows');
	if (dataRow.length > 0) {
		var columns = $dg.datagrid('options').columns;
		var rsCol = bizbilldetailsreport.formatCol(columns);
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
bizbilldetailsreport.formatCol = function(columns){
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
bizbilldetailsreport.exportColumns = [
                               {title:"作业时间",field:"createtm",width:60,align:'left'},
       		                   {title:"品牌",field:"brandName",width:60,align:'left'},
       		                   {title:"单号",field:"billNo",width:80,align:'left'},
       		                   {title:"单据类型",field:"billTypeName",width:60,align:'left'},
       		                   {title:"单据状态",field:"statusName",width:80},
       		                   {title:"商品编码",field:"itemNo",width:180,align:'left'},
       		                   {title:"尺码",field:"sizeNo",width:60,align:'left'},
       		                   {title:"箱号",field:"boxNo",width:120,align:'left'},
       		                   {title:"计划数量",field:"planQty",width:60,align:'left'},
       		                   {title:"实数",field:"realQty",width:60,align:'left'},
       		                   {title:"来源单据",field:"sourceBillNo",width:120,align:'left'},
       		                   {title:"单据类型",field:"sourceBillTypeName",width:80},
	       		               {title:'更新人',field:'editor',width:80,align:'left'},
	       		               {title:'更新人名称',field:'editorName',width:80,align:'left'},
							   {title:'更新时间',field:'edittm',width:80,align:'left'}
                           ];
bizbilldetailsreport.doExport = function(){
    var url=BasePath+'/bizbilldetailsreport/doExport';
	var dgObj = $("#dataGridJG");
	var dataRow = dgObj.datagrid('getRows');
	if(dataRow.length > 0){
		//var params = dgObj.datagrid('options').queryParams;
		$("#exportExcelForm").remove();
		$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
		var fromObj = $('#exportExcelForm');
		wms_city_common.loading("show","正在导出");
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {
				param.locno = bizbilldetailsreport.locno;
				param.startDate = $("#startDateCondition").datebox('getValue');
			    param.endDate = $("#endDateCondition").datebox('getValue');
			    param.billType = $("#billTypeCondition").combobox("getValue");
			    param.finishStatus = $("#finishStatusCondition").combobox("getValue");
			    
			    param.billNo = $("#billNoCondition").val();
			    param.sourceBillNo = $("#sourceBillNoCondition").val();
			    param.boxNo = $("#boxNoCondition").val();
			    param.cateOne = $("#cateOneCondition").combobox("getValues");
			    
				param.cateTwo = $("#cateTwoCondition").combobox("getValues");
				param.cateThree = $("#cateThreeCondition").combobox("getValues");
				param.quality = $("#qualityCondition").combobox("getValue");
				param.season=$("#seasonCondition").combobox("getValues");
				
				param.years=$("#yearsCondition").val();
				param.gender=$("#genderCondition").combobox("getValues");
				param.sysNo=$("#sysNoCondition").combobox("getValue");
				param.brandNo=$("#brandNoCondition").combobox("getValue");
				
				param.exportColumns = JSON.stringify(bizbilldetailsreport.exportColumns);
				param.fileName = "业务单据明细报表";
				
			},
			success : function() {
			}
		});
		wms_city_common.loading();
	}else{
		alert("没有可导出的数据!");
		return false;
	}
};

function parsePage() {
	//工具条快捷搜索
	toolSearch();
};