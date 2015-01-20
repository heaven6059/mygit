var billWholesale = {};
var gridId ='mainDataGridEdit';//明细gridID
var sizeTypeFiledName="sizeKind"; //尺码类型

billWholesale.curRowIndex = -1;//订单表头dataGrid行号
billWholesale.type = 0;//类型 1--上单 2--下单

//单据明细表头
var preColNames=[{title:"序列号",field:"seqId",editor:{options:{required:true,missingMessage:'序列号必须输入!'}}},
                 {title:"商品编码",field:"itemNo",width:120,
						editor:{
							type:'combobox',
							options:{
                 				required:true,
                 				missingMessage:'商品编码为必选项'
                 			}
						}
                 },
                 {title:"商品名称",field:"itemName"},
                 {title:"颜色",field:"colorNo"}, 
                 {title:"类别名称",field:"cateNo"}];

var endColNames =[{title:"合计数量",field:"allCounts",editor:{type:"numberbox",options:{precision:2} }},
                  {title:"批发价",field:"price",editor:{options:{required:true,missingMessage:'批发价必须输入!'}}},
                  {title:"金额",field:"allPrice"}] ; 


//单据状态
billWholesale.statusData =[{    
    'label':'0',
    'text':'制单'
},{    
    'label':'100',
    'text':'完结'
}];

//单据传输状态
billWholesale.statusTransData =[{    
    'label':'0',
    'text':'未传输'
},{    
    'label':'1',
    'text':'已传输'
}];

//导出
billWholesale.exportExcel=function(){
	var sysNo=$('#sysNo').combobox('getValue'); //品牌库
	var excelTitle='批发出库订单导出,订单号'+$('#billNo').val(); //标题+单据号 
	exportExcelBill(gridId,sysNo,preColNames,endColNames,sizeTypeFiledName,excelTitle);
};

//上单
billWholesale.preBill=function(){
	if(billWholesale.curRowIndex < 0){
		alert('不存在当前单据');
		return;
	}
	billWholesale.type = 1;
    preBill('mainDataGrid',billWholesale.curRowIndex,billWholesale.type,billWholesale.callBackBill);
};

//下单
billWholesale.nextBill=function(){
	if(billWholesale.curRowIndex < 0){
		alert('不存在当前单据');
		return;
	}
	billWholesale.type=2;
    preBill('mainDataGrid',billWholesale.curRowIndex,billWholesale.type,billWholesale.callBackBill);
};

//查单
billWholesale.selectBill=function(){
	returnTab('mainTab','单据查询');
};

//刷新
billWholesale.reload = function(){
	var queryURL=BasePath+ '/bill_wholesale/list.json';
    billWholesale.loadGridDataUtil('mainDataGrid',queryURL,{});
};

billWholesale.callBackBill=function(curRowIndex,rowData){
	if(billWholesale.type==1||billWholesale.type==2){
		if(rowData!=null&&rowData!=''&&rowData!=[]){
			billWholesale.loadDetail(rowData.billNo,rowData.sysNo,curRowIndex);
			billWholesale.type=0;
		}else{
			if(billWholesale.type==1){
				alert('已经是第一单');
			}else{
				alert('已经是最后一单');
			}
		}
	}
};

//加载Grid数据Utils
billWholesale.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
    tempObj.datagrid( 'options' ).queryParams = '';
};

//加载单据表头
var gridColumnData = {};
billWholesale.loadGridColumnHead = function(sysNo){
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
billWholesale.loadFormDetail = function(returnData) {
	$('#dataForm').form('load',returnData);
	$('#statusSp').html(returnData.statusStr);
	$('#statusTransSp').html(returnData.statusTransStr);
	$('#creatorSp').html(returnData.creator);
	$('#createtmSp').html(formatDate(returnData.createtm));
	$('#auditorSp').html(returnData.auditor);
	$('#auditdtSp').html(formatDate(returnData.auditdt));
};

//初始化动态表头
billWholesale.initGridColumnData = function(sysNo){
	billWholesale.loadGridColumnHead(sysNo);
    $("#"+gridId).datagrid({
         columns:gridColumnData.queryParams
    }); 
    //billWholesale.updateEditor();
};

//初始化单据数据
billWholesale.initGridDataDetail = function(billNo) {
	var reqUrl = BasePath+ '/bill_wholesale/getMxList.htm';
	var queryParams = {billNo:billNo};
	billWholesale.loadGridDataUtil(gridId,reqUrl,queryParams);
};

//初始化主档信息
billWholesale.initFormDetail = function(billNo) {
	ajaxRequest(BasePath+'/bill_wholesale/get',{billNo:billNo},billWholesale.loadFormDetail);
};

//双击单据查询行,加载单据明细
billWholesale.loadDetail = function(billNo,sysNo,rowIndex){
	//当前选择订单表头dataGrid的行号
	billWholesale.curRowIndex = rowIndex;
	//切换到单据明细选项卡
	$('#mainTab').tabs( 'select',"单据明细" );
	//初始化FORM数据
	billWholesale.initFormDetail(billNo);
	//初始化动态表头
	billWholesale.initGridColumnData(sysNo);
	//初始化明细信息
	billWholesale.initGridDataDetail(billNo);
};

//双击编辑行
billWholesale.editDetailData = function(rowIndex){
	$("#"+gridId).datagrid("beginEdit",rowIndex);
};

//初始化品牌信息
billWholesale.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150",
	     onSelect:function(record){
	    	 billWholesale.initGridColumnData(record.itemvalue);
	     }
	 });
};

//初始化客户信息
billWholesale.initStoreNo = function(inputId){
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

//4.检搜商品 
billWholesale.myloader = function(param,success,error){
    var sysNo = "TM";
	var q = param.q || '';
	if (q.length < 1){return false;}
	$.ajax({
		url:BasePath+'/item/list.json',
        data: {
            maxRows: 20,
            q: q,
            sysNo:sysNo
        },
        panelHeight: 'auto',
        type: 'POST',
	    async:false,
		success: function(data){
			var items = $.map(data.rows, function(item){
				return {
					id: item.itemNo,
					name: item.itemNo
				};
			});
			success(items);
		}
	});
};

//加载表格之前改变editor
billWholesale.updateEditor=function(){
      var e = $("#mainDataGridEdit").datagrid('getColumnOption', 'itemNo');
      e.editor={  type : 'combobox',
    	  options:{
    		  loader: billWholesale.myloader,
    		  mode: 'remote',
    		  panelHeight: 'auto',
    		  valueField: 'id',
    		  textField: 'name'
    	  }
     };
};

//初始化单据查询
function init(){
    $('#mainTab').tabs('add',{
		title: '单据查询',
		selected: false,
		closable: false,
		href:BasePath+'/bill_wholesale/list_tabMain.htm'
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
	billWholesale.initBrand();
	billWholesale.initStoreNo('storeNo');
	billWholesale.initStoreNo('customerNo');
});
