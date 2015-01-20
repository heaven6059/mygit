
var gridId = 'dataGridStock';
var sizeTypeFiledName="sizeKind";  
var conContentMove = {};

//单据明细表头
var preColNames=[{title:"客户编码",field:"itemNo",width:80},
                 {title:"客户名称",field:"itemName",width:120},
                 {title:"商品编码",field:"itemNo",width:80},
                 {title:"商品名称",field:"itemName",width:120}];

var endColNames =[{title:"发生数量",field:"allStockCounts"},
                  {title:"存储数量",field:"dFirstQty"},
                  {title:"单据编号",field:"paperNo"},
                  {title:"单据类型",field:"paperType"},
                  {title:"业务发生日期",field:"expDate"},
                  {title:"记账日期",field:"createtm"},
                  {title:"记账时间",field:"createtm"}] ;


//加载单据表头
var gridColumnData = {};
conContentMove.loadGridColumnHead = function(sysNo){
	//请求路径
	var reqUrl = BasePath+'/initCache/getBrandList.htm';
	//请求参数
	var reParam = {sysNo:sysNo,preColNames:JSON.stringify(preColNames),endColNames:JSON.stringify(endColNames),sizeTypeFiledName:sizeTypeFiledName};
	//返回结果
	$.ajax({
		type: 'POST',
 		url: reqUrl,
        data: reParam,
        cache: true,
        async: false,
        success: function(returnData,msg){
        	gridColumnData.queryParams=returnData.returnCols;
        	gridColumnData.maxType=returnData.maxType;
        	gridColumnData.startType=returnData.startType;
		}
	});
};


//初始化动态表头
conContentMove.initGridColumnData = function(sysNo){
	conContentMove.loadGridColumnHead(sysNo);
    $("#"+gridId).datagrid({
         columns:gridColumnData.queryParams
    }); 
};


//公共加载DATAGRID
conContentMove.loadGridDataUtil = function(gridId,url,queryParams){
    var tempObj = $('#'+gridId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
    tempObj.datagrid( 'options' ).queryParams = '';
};


//初始化单据数据
conContentMove.initGridDataDetail = function(gridDataId) {
	//序列form转换json
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/con_content_move/findConContentMoveList';
	var reqParam = eval("(" +fromObjStr+ ")");
	//加载数据
	conContentMove.loadGridDataUtil(gridDataId,queryMxURL,reqParam);
};


//搜索库存变动信息
conContentMove.searchData = function(){
	var fromObj=$('#searchForm');
	//验证必输项
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return ;
    }
	var sysNo = $('#sysNoCondition').combobox('getValue');
	//初始化动态表头
	conContentMove.initGridColumnData(sysNo);
	//初始化数据
	conContentMove.initGridDataDetail('dataGridStock');
};


//清除查询条件
conContentMove.searchDataClear = function(){
	$('#searchForm').form("clear");
	$('#'+gridId).datagrid( 'options' ).queryParams = '';
	$("#st1").attr("checked","checked");
};


//导出
conContentMove.exportExcel=function(){
	var sysNo=$('#sysNoCondition').combobox('getValue'); //品牌库
	var excelTitle='全国库存明细导出('+ selectType+")"; //标题+查询方式
	exportExcelBill('dataGridStock',sysNo,preColNames,endColNames,sizeTypeFiledName,excelTitle);
};


//主要是推荐这个函数。它将jquery系列化后的值转为name:value的形式。
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

//初始化品牌
conContentMove.initBrand = function(){
	$('#sysNoCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	 });
};

//页面加载完成初始化
$(document).ready(function(){
	conContentMove.initBrand();
});