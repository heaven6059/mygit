
var gridId = 'dataGridStock';
var sizeTypeFiledName="sizeKind";  
var zoneStock = {};

//单据明细表头
var preColNames=[{title:"区域编码",field:"zoneNo",width:60},
                 {title:"区域名称",field:"zoneName",width:60},
                 {title:"商品编码",field:"itemNo",width:80},
                 {title:"商品名称",field:"itemName",width:120},
                 {title:"库存属性",field:"itemTypeStr",width:60}];

var endColNames =[{title:"库存总数量",field:"allStockCounts"},
                  {title:"所属品牌",field:"brandName"}] ;


//加载单据表头
var gridColumnData = {};
zoneStock.loadGridColumnHead = function(sysNo){
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
zoneStock.initGridColumnData = function(sysNo){
	zoneStock.loadGridColumnHead(sysNo);
    $("#"+gridId).datagrid({
         columns:gridColumnData.queryParams
    }); 
};


//公共加载DATAGRID
zoneStock.loadGridDataUtil = function(gridId,url,queryParams){
    var tempObj = $('#'+gridId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
    tempObj.datagrid( 'options' ).queryParams = '';
};


//初始化单据数据
zoneStock.initGridDataDetail = function(gridDataId) {
	//序列form转换json
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/con_content/findZoneStockList';
	var reqParam = eval("(" +fromObjStr+ ")");
	//加载数据
	zoneStock.loadGridDataUtil(gridDataId,queryMxURL,reqParam);
};


//搜索库存信息
zoneStock.searchData = function(){
	var fromObj=$('#searchForm');
	//验证必输项
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return ;
    }
	var sysNo = $('#sysNoCondition').combobox('getValue');
	//初始化动态表头
	zoneStock.initGridColumnData(sysNo);
	//初始化数据
	zoneStock.initGridDataDetail('dataGridStock');
};


//清除查询条件
zoneStock.searchDataClear = function(){
	$('#searchForm').form("clear");
	$('#'+gridId).datagrid( 'options' ).queryParams = '';
	$("#st1").attr("checked","checked");
};


//导出
zoneStock.exportExcel=function(){
	var checkedValue = $('input[name="selectType"]:checked').val();
	var selectType = (checkedValue=='1')?'库存属性':'汇总';
	var sysNo=$('#sysNoCondition').combobox('getValue'); //品牌库
	var excelTitle='区域库存明细导出('+ selectType+")"; //标题+查询方式
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
zoneStock.initBrand = function(){
	$('#sysNoCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	 });
};

//初始化区域
zoneStock.initZone = function(){
	$('#zoneNo').combobox({
		url:BasePath+'/zone_info/get_biz',
	     valueField:"zoneNo",
	     textField:"zoneName",
	     panelHeight:"auto"
	  });
};

//页面加载完成初始化
$(document).ready(function(){
	zoneStock.initZone();
	zoneStock.initBrand();
});
