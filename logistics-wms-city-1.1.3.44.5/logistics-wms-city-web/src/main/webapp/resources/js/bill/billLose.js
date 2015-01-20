var billLose = {};
var gridId ='mainDataGridEdit';//明细gridID
var sizeTypeFiledName="sizeKind";  

billLose.curRowIndex = -1;//订单表头dataGrid行号
billLose.type = 0;//类型 1--上单 2--下单

//单据明细表头
var preColNames=[{title:"序列号",field:"seqId",editor:{options:{required:true,missingMessage:'序列号必须输入!'}}},
                 {title:"商品编码",field:"itemNo",width:120},
                 {title:"商品名称",field:"itemName"},
                 {title:"颜色",field:"colorNo"}, 
                 {title:"类别名称",field:"cateNo"}];

var endColNames =[{title:"合计数量",field:"allCounts",editor:{type:"numberbox",options:{precision:2} }},
                  {title:"金额",field:"allCost"}] ; 

//单据状态
billLose.statusData =[{    
    'label':'0',
    'text':'制单'
},{    
    'label':'100',
    'text':'完结'
}];

//单据传输状态
billLose.statusTransData =[{    
    'label':'0',
    'text':'未传输'
},{    
    'label':'1',
    'text':'已传输'
}];

//查单
billLose.selectBill=function(){
	returnTab('mainTab','单据查询');
};

//导出
billLose.exportExcel=function(){
	var sysNo=$('#sysNo').combobox('getValue'); //品牌库
	var excelTitle='报损订单导出,订单号'+$('#billNo').val(); //标题+单据号 
	exportExcelBill(gridId,sysNo,preColNames,endColNames,sizeTypeFiledName,excelTitle);
};

//上单
billLose.preBill=function(){
	if(billLose.curRowIndex < 0){
		alert('不存在当前单据');
		return;
	}
	billLose.type = 1;
    preBill('mainDataGrid',billLose.curRowIndex,billLose.type,billLose.callBackBill);
};

//下单
billLose.nextBill=function(){
	if(billLose.curRowIndex < 0){
		alert('不存在当前单据');
		return;
	}
	billLose.type=2;
    preBill('mainDataGrid',billLose.curRowIndex,billLose.type,billLose.callBackBill);
};

//刷新
billLose.reload = function(){
	var queryURL=BasePath+ '/bill_lose/list.json';
    billLose.loadGridDataUtil('mainDataGrid',queryURL,{});
};

billLose.callBackBill=function(curRowIndex,rowData){
	if(billLose.type==1||billLose.type==2){
		if(rowData!=null&&rowData!=''&&rowData!=[]){
			billLose.loadDetail(rowData.billNo,rowData.sysNo,curRowIndex);
			billLose.type=0;
		}else{
			if(billLose.type==1){
				alert('已经是第一单');
			}else{
				alert('已经是最后一单');
			}
		}
	}
};

//加载Grid数据Utils
billLose.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
    tempObj.datagrid( 'options' ).queryParams = '';
};

//加载单据表头
var gridColumnData = {};
billLose.loadGridColumnHead = function(sysNo){
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

//加载主档信息
billLose.loadFormDetail = function(returnData) {
	$('#dataForm' ).form('load',returnData);
	$('#statusSp').html(returnData.statusStr);
	$('#statusTransSp').html(returnData.statusTransStr);
	$('#creatorSp').html(returnData.creator);
	$('#createtmSp').html(formatDate(returnData.createtm));
	$('#auditorSp').html(returnData.auditor);
	$('#auditdtSp').html(formatDate(returnData.auditdt));
};

//初始化动态表头
billLose.initGridColumnData = function(sysNo){
	billLose.loadGridColumnHead(sysNo);
    $("#"+gridId).datagrid({
         columns:gridColumnData.queryParams
    }); 
};

//初始化单据数据
billLose.initGridDataDetail = function(billNo) {
	var reqUrl = BasePath+ '/bill_lose/getMxList.htm';
	var queryParams = {billNo:billNo};
	billLose.loadGridDataUtil(gridId,reqUrl,queryParams);
};

//初始化主档信息
billLose.initFormDetail = function(billNo) {
	ajaxRequest(BasePath+'/bill_lose/get',{billNo:billNo},billLose.loadFormDetail);
};

//双击单据查询行,加载单据明细
billLose.loadDetail = function(billNo,sysNo,rowIndex){
	//当前选择订单表头dataGrid的行号
	billLose.curRowIndex = rowIndex;
	//切换到单据明细选项卡
	$('#mainTab').tabs( 'select',"单据明细" );
	//初始化FORM数据
	billLose.initFormDetail(billNo);
	//初始化动态表头
	billLose.initGridColumnData(sysNo);
	//初始化明细信息
	billLose.initGridDataDetail(billNo);
};

//初始化品牌信息
billLose.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150",
	     onSelect:function(record){
	    	 billLose.initGridColumnData(record.itemvalue);
	     }
	 });
};

//初始化客户信息
billLose.initStoreNo = function(inputId){
	$('#'+inputId).combogrid({
		 panelWidth:450,   
         idField:'storeNo',  
         textField:'textFieldName',   
         pagination : true,
         rownumbers:true,
         mode: 'remote',
         url:BasePath+'/store/list.json',   
         columns:[[  
			{field:'storeNo',title:'客户编码',width:140},  
			{field:'storeName',title:'客户名称',width:140}  
         ]],
         loadFilter:function(data){
        	 if(data && data.rows){
        		 for(var i = 0;i<data.rows.length;i++){
        			 var tempData = data.rows[i];
        			 tempData.textFieldName = tempData.storeNo+'→'+tempData.storeName;
        		 }
        	 }
     		 return data;
         }
	});
};

//初始化供应商信息
billLose.initSupplier = function(){
	$('#supplierNo').combogrid({
		 panelWidth:450,   
         idField:'supplierNo',  
         textField:'textFieldName',   
         pagination : true,
         rownumbers:true,
         mode: 'remote',
         url:BasePath+'/supplier/list.json',   
         columns:[[  
             {field:'supplierNo',title:'供应商编码',width:140},  
             {field:'supplierName',title:'供应商名称',width:140}  
         ]],
         loadFilter: function(data){
        	 if(data && data.rows){
        		 for(var i = 0;i<data.rows.length;i++){
        			 var tempData = data.rows[i];
        			 tempData.textFieldName = tempData.supplierNo+'→'+tempData.supplierName;
        		 }
        	 }
     		 return data;
     	 }
	  });
};

//初始化单据查询
function init(){
    $('#mainTab').tabs('add',{
		title: '单据查询',
		selected: false,
		closable: false,
		href:BasePath+'/bill_lose/list_tabMain.htm'
	});
    refreshTabs();
}

//切换选项卡刷新单据查询的dataGrid
function refreshTabs(){
	$('#mainTab').tabs({
		onSelect:function(title,index){
			
			$('#mainDataGrid').datagrid('resize', {
			     width:function(){return document.body.clientWidth;}
			});
			
		    $('#easyui-panel-id').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#mainDataGridEdit').datagrid('resize', {
		        width:function(){return document.body.clientWidth;}
		    });
		   
		},onLoad:function(panel){
			$('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		}
		
	});
}

//JS初始化执行
$(document).ready(function(){
	billLose.initBrand();
	billLose.initStoreNo('storeNo');
	billLose.initSupplier();
});
