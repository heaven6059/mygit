
var datecontentreport = {};


//加载Grid数据Utils
datecontentreport.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//获取当日期
datecontentreport.getDate = function(n){
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

//查询波次信息
datecontentreport.searchData = function(){
	
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
    
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/datecontentreport/listReport.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = datecontentreport.locno;
	datecontentreport.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
	
};


//清除查询条件
datecontentreport.searchClear = function(id){
	$('#'+id).form("clear");
};

//到处报表
datecontentreport.do_export = function(){
	exportExcelBaseInfo('dataGridJG','/datecontentreport/do_export.htm','日库存变动报表导出');
};

//初始化用户信息
datecontentreport.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		datecontentreport.locno = data.locno;
		$('#startAudittmCondition').datebox('setValue',data.currentDate10Str);
		$('#endAudittmCondition').datebox('setValue',data.currentDate10Str);
	}); 
};


$(document).ready(function(){
	datecontentreport.initCurrentUser();
	wms_city_common.comboboxLoadFilter(
			["brandNo"],
			'brandNo',
			'brandName',
			'valueAndText',
			false,
			[true],
			BasePath+'/brand/get_bizDy',
			{},
			null,
			null);
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				datecontentreport.jhrkQty = data.footer[1].jhrkQty;
		   				datecontentreport.tcrkQty = data.footer[1].tcrkQty;
		   				datecontentreport.tcckQty = data.footer[1].tcckQty;
		   				datecontentreport.fhckQty = data.footer[1].fhckQty;
		   				datecontentreport.qtrkQty = data.footer[1].qtrkQty;
		   				datecontentreport.bsckQty = data.footer[1].bsckQty;
		   				datecontentreport.pyrkQty = data.footer[1].pyrkQty;
		   				datecontentreport.kpckQty = data.footer[1].kpckQty;
		   			}else{
		   				var rows = $('#dataGridJG').datagrid('getFooterRows');
			   			rows[1]['jhrkQty'] = datecontentreport.jhrkQty;
			   			rows[1]['tcrkQty'] = datecontentreport.tcrkQty;
			   			rows[1]['tcckQty'] = datecontentreport.tcckQty;
			   			rows[1]['fhckQty'] = datecontentreport.fhckQty;
			   			rows[1]['qtrkQty'] = datecontentreport.qtrkQty;
			   			rows[1]['bsckQty'] = datecontentreport.bsckQty;
			   			rows[1]['pyrkQty'] = datecontentreport.pyrkQty;
			   			rows[1]['kpckQty'] = datecontentreport.kpckQty;
			   			$('#dataGridJG').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
});
