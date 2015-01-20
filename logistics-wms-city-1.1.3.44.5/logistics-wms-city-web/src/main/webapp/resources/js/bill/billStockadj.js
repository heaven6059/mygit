var billStockadj = {};
var gridId ='mainDataGridEdit';//明细gridID
var sizeTypeFiledName="sizeKind";//尺寸分类

billStockadj.curRowIndex = -1;//订单表头dataGrid行号
billStockadj.type = 0;//类型 1--上单 2--下单

//格式化status：0-制单 100-完结
billStockadj.formatStatus = function(value){
    if (value == null) {
        return '';
    }
	if(value==0){
		return '制单';
	}else if(value==100){
		return '完结';
	}
	return value;
};

//格式化statusTrans：0-未传输 1-已传输
billStockadj.formatStatusTrans = function(value){
    if (value == null) {
        return '';
    }
	if(value==0){
		return '未传输';
	}else if(value==1){
		return '已传输';
	}
	return value;
};

//单据明细表头
var preColNames=[{title:"序列号",field:"seqId",editor:{options:{required:true,missingMessage:'序列号必须输入!'}}},
                 {title:"商品编码",field:"itemNo",width:120},
                 {title:"商品名称",field:"itemName"},
                 {title:"颜色",field:"colorNo"}, 
                 {title:"类别名称",field:"cateNo"}
                 //{title:"原等级定义",field:"gradeNoOldStr"},
                 //{title:"原属性定义",field:"propertyNoOldStr"},
                 //{title:"等级定义",field:"gradeNoStr"},
                 //{title:"属性定义",field:"propertyNoStr"}
                 ];

var endColNames =[{title:"合计数量",field:"allCounts",editor:{type:"numberbox",options:{precision:2} }},
                  {title:"金额",field:"allCost"}] ;  

//查单
billStockadj.selectBill=function(){
	returnTab('mainTab','单据查询');
};

//导出
billStockadj.exportExcel=function(){
	var sysNo=$('#sysNo').combobox('getValue'); //品牌库
	var excelTitle='库存调整订单导出,订单号'+$('#billNo').val(); //标题+单据号 
	exportExcelBill(gridId,sysNo,preColNames,endColNames,sizeTypeFiledName,excelTitle);
};

//上单
billStockadj.preBill=function(){
	if(billStockadj.curRowIndex < 0){
		alert('不存在当前单据');
		return;
	}
	billStockadj.type = 1;
    preBill('mainDataGrid',billStockadj.curRowIndex,billStockadj.type,billStockadj.callBackBill);
};

//下单
billStockadj.nextBill=function(){
	if(billStockadj.curRowIndex < 0){
		alert('不存在当前单据');
		return;
	}
	billStockadj.type=2;
    preBill('mainDataGrid',billStockadj.curRowIndex,billStockadj.type,billStockadj.callBackBill);
};

//刷新
billStockadj.reload = function(){
	var queryURL=BasePath+ '/bill_stockadj/list.json';
    billStockadj.loadGridDataUtil('mainDataGrid',queryURL,{});
};

billStockadj.callBackBill=function(curRowIndex,rowData){
	if(billStockadj.type==1||billStockadj.type==2){
		if(rowData!=null&&rowData!=''&&rowData!=[]){
			billStockadj.loadDetail(rowData.billNo,rowData.sysNo,curRowIndex);
			billStockadj.type=0;
		}else{
			if(billStockadj.type==1){
				alert('已经是第一单');
			}else{
				alert('已经是最后一单');
			}
		}
	}
};

//加载Grid数据Utils
billStockadj.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
    tempObj.datagrid( 'options' ).queryParams = '';
};

//加载单据表头
var gridColumnData = {};
billStockadj.loadGridColumnHead = function(sysNo){
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
billStockadj.loadFormDetail = function(returnData) {
	$('#dataForm' ).form('load',returnData);
	$('#statusSp').html(returnData.statusStr);
	$('#statusTransSp').html(returnData.statusTransStr);
	$('#creatorSp').html(returnData.creator);
	$('#createtmSp').html(formatDate(returnData.createtm));
	$('#auditorSp').html(returnData.auditor);
	$('#auditdtSp').html(formatDate(returnData.auditdt));
};

//初始化动态表头
billStockadj.initGridColumnData = function(sysNo){
	billStockadj.loadGridColumnHead(sysNo);
    $("#"+gridId).datagrid({
         columns:gridColumnData.queryParams
    }); 
};

//初始化单据数据
billStockadj.initGridDataDetail = function(billNo) {
	var reqUrl = BasePath+ '/bill_stockadj/getMxList.htm';
	var queryParams = {billNo:billNo};
	billStockadj.loadGridDataUtil(gridId,reqUrl,queryParams);
};

//初始化主档信息
billStockadj.initFormDetail = function(billNo) {
	ajaxRequest(BasePath+'/bill_stockadj/get',{billNo:billNo},billStockadj.loadFormDetail);
};

//双击单据查询行,加载单据明细
billStockadj.loadDetail = function(billNo,sysNo,rowIndex){
	//当前选择订单表头dataGrid的行号
	billStockadj.curRowIndex = rowIndex;	
	//切换到单据明细选项卡
	$('#mainTab').tabs( 'select',"单据明细" );
	//初始化FORM数据
	billStockadj.initFormDetail(billNo);
	//初始化动态表头
	billStockadj.initGridColumnData(sysNo);
	//初始化明细信息
	billStockadj.initGridDataDetail(billNo);
};

//初始化品牌信息
billStockadj.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150",
	     onSelect:function(record){
	    	 billStockadj.initGridColumnData(record.itemvalue);
	     }
	 });
};

//初始化客户信息
billStockadj.initStoreNo = function(inputId){
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
billStockadj.initSupplier = function(){
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
		href:BasePath+'/bill_stockadj/list_tabMain.htm'
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
	billStockadj.initBrand();
	billStockadj.initStoreNo('storeNo');
	billStockadj.initSupplier();
});
