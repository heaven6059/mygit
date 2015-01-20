
var concontentmove = {};


//加载Grid数据Utils
concontentmove.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//获取当日期
concontentmove.getDate = function(n){
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
concontentmove.searchData = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/con_content_move/dtllist.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = concontentmove.locno;
	concontentmove.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
	
};


//清除查询条件
concontentmove.searchClear = function(id){
	$('#'+id).form("clear");
//	$('#cellNo').combobox("loadData",[]);
	$('#quality').combobox("loadData",[]);
};

//导出报表
concontentmove.do_export = function(){
	exportExcelBaseInfo('dataGridJG','/con_content_move/dtl_export','库存商品变动报表导出');
};

//初始化用户信息
concontentmove.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		concontentmove.locno = data.locno;
		$('#startCreatetm').datebox('setValue',data.currentDate10Str);
		$('#endCreatetm').datebox('setValue',data.currentDate10Str);
	}); 
};

//储位
concontentmove.initCell = function(wareNo,areaNo){
//	if(areaNo == ''){
//		wms_city_common.comboboxLoadFilter(
//				["cellNo"],
//				'cellNo',
//				'cellNo',
//				'valueAndText',
//				false,
//				[true],
//				null,
//				{},
//				null,
//				null);
//	}else{
//		wms_city_common.comboboxLoadFilter(
//				["cellNo"],
//				'cellNo',
//				'cellNo',
//				'valueAndText',
//				false,
//				[true],
//				BasePath+'/cm_defcell/get_biz?locno='+concontentmove.locno+"&wareNo="+wareNo+"&areaNo="+areaNo,
//				{},
//				null,
//				null);
//	}
};

//业务类型
concontentmove.initPaperType = function(){
	$('#paperType').combobox({
		url:BasePath+'/bill_acc_control/listBillAccControlGroupByBillName',
		valueField:"paperType",
	    textField:"valueAndText",
	    panelHeight:200,
	    loadFilter:function(data){
			if(data){
	       		 for(var i = 0;i<data.length;i++){
	       			 var tempData = data[i];
	       			 tempData.valueAndText = tempData.paperType+'→'+tempData.billName;
	       		 }
	 		}
			return data;
	   }
	});
};

$(document).ready(function(){
	concontentmove.initCurrentUser();
	//concontentmove.initArea(concontentmove.locno);
	//初始化库区(有级联关系,见ftl绑定事件)
	wms_city_common.comboboxLoadFilter(
			["areaNo"],
			'areaNo',
			'areaName',
			'valueAndText',
			false,
			[true],
			BasePath+'/cm_defarea/get_biz?locno='+concontentmove.locno,
			{},
			null,
			null);
	//初始化库存属性和品质(有级联关系)
	wms_city_common.initItemTypeAndQuality("itemType","quality",true);
	//初始化进出标示
	wms_city_common.comboboxLoadFilter(
			["ioFlag"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IO_FLAG',
			{},
			null,
			null);
	//concontentmove.initPaperType();
	
	//初始化记账类型
	wms_city_common.comboboxLoadFilter(
			["preFlag"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_PRE_FLAG',
			{},
			null,
			null);
	//初始化进出标示
	wms_city_common.comboboxLoadFilter(
			["paperType"],
			'paperType',
			'billName',
			'valueAndText',
			false,
			[true],
			BasePath+'/bill_acc_control/listBillAccControlGroupByBillName',
			{},
			null,
			null);
	
	$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
							concontentmove.moveQty = data.footer[1].moveQty;
			   			}else{
			   				var rows = $('#dataGridJG').datagrid('getFooterRows');
				   			rows[1]['moveQty'] = concontentmove.moveQty;
				   			$('#dataGridJG').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
});
