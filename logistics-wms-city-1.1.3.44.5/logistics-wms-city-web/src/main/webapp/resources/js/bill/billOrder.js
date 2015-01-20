var billOrder = {};

billOrder.curRowIndex = -1;
billOrder.preType = 0;

billOrder.exportDataGridDtl1Id = 'order_dtl1_dataGrid';
billOrder.preColNamesDtl1 = [
			                   {title:"序列号",field:"seqId" },
			                   {title:"商品编码",field:"itemNo",width:120},
			                   {title:"商品名称",field:"itemName",width:120}
                        ];
billOrder.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"},
		                      {title:"单价",field:"cost"},
		                      {title:"金额",field:"allCost"}
                        ] ;
billOrder.exportDataGridDtl2Id = 'order_dtl2_dataGrid';
billOrder.preColNamesDtl2 = [
                           {title:"分仓订货单号",field:"refbillNo",width:120},
                           {title:"客户名称",field:"storeName",width:120},
      	                   {title:"商品编码",field:"itemNo",width:120},
    	                   {title:"商品名称",field:"itemName",width:120}
                      ];
billOrder.endColNamesDtl2 = [
      	                   {title:"合计",field:"allCount"}
     	                  ];
billOrder.sizeTypeFiledName = 'sizeKind';

billOrder.exportExcel=function(){
	var sysNo=$('#sysNo').combobox('getValue'); //品牌库
	var tempTitle = $('#main_order_dtlId').tabs('getSelected').panel('options').title;
	var excelTitle='订货单'+tempTitle+'导出,订单号'+$('#billNo').val(); //标题+单据号 
	if(tempTitle=='汇总'){
		exportExcelBill(billOrder.exportDataGridDtl1Id,sysNo,billOrder.preColNamesDtl1,billOrder.endColNamesDtl1,billOrder.sizeTypeFiledName,excelTitle);
	}else{
		exportExcelBill(billOrder.exportDataGridDtl2Id,sysNo,billOrder.preColNamesDtl2,billOrder.endColNamesDtl2,billOrder.sizeTypeFiledName,excelTitle);
	}
};

billOrder.callBackBill=function(curRowIndex,rowData){
	if(billOrder.preType==1 || billOrder.preType==2){
		if(rowData!=null&&rowData!=''&&rowData!=[]){
			billOrder.loadDetail(rowData.billNo,rowData.sysNo,curRowIndex);
			billOrder.preType=0;
		}else{
			if(billOrder.preType==1){
				alert('已经是第一单');
			}else{
				alert('已经是最后一单');
			}
		}
	}
};

billOrder.preBill=function(){
	if(billOrder.curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	billOrder.preType=1;
    preBill('mainDataGrid',billOrder.curRowIndex,1,billOrder.callBackBill);
};

billOrder.nextBill=function(){
	if(billOrder.curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	billOrder.preType=2;
    preBill('mainDataGrid',billOrder.curRowIndex,2,billOrder.callBackBill);
};

billOrder.reload = function(){
    var tempObj = $('#mainDataGrid');
    tempObj.datagrid( 'reload' );
};

billOrder.formatStatus = function(value){
    if (value == null) {
        return '';
    }
	if(value==0){
		return '制单';
	}else if(value==100){
		return '完结';
	}
	return '';
};

billOrder.formatStatusTrans = function(value){
    if (value == null) {
        return '';
    }
	if(value==0){
		return '未传输';
	}else if(value==1){
		return '已传输';
	}
	return '';
};

billOrder.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

billOrder.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billOrder.sizeTypeFiledName
        	     },
        cache: true,
        async: false,
        success: function(returnData){
         	resultData.columnArr = returnData.returnCols;
         	resultData.startType = returnData.startType;
         	resultData.maxType = returnData.maxType;
		  }
      });
      return resultData;
};

billOrder.initGridDataUtil = function(gridId,url,queryParams){
    var tempObj = $('#'+gridId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

billOrder.initGridHead = function(sysNo){
    var beforeColArr = billOrder.preColNamesDtl1;
	 var afterColArr = billOrder.endColNamesDtl1; 
	 var columnInfo = billOrder.getColumnInfo(sysNo,beforeColArr,afterColArr);
     $("#"+billOrder.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
     beforeColArr = billOrder.preColNamesDtl2;
	afterColArr = billOrder.endColNamesDtl2; 
	columnInfo = billOrder.getColumnInfo(sysNo,beforeColArr,afterColArr);
	$("#"+billOrder.exportDataGridDtl2Id).datagrid({
	   columns:columnInfo.columnArr
	}); 
};

billOrder.initGridData = function(billNo){
    var url = BasePath+ '/bill_order_dtl/queryOrderDtlList';
    var queryParams = {billNo:billNo};
    billOrder.initGridDataUtil(billOrder.exportDataGridDtl1Id,url,queryParams);
    url = BasePath+ '/bill_order_dtl2/queryOrderDtlList';
    billOrder.initGridDataUtil(billOrder.exportDataGridDtl2Id,url,queryParams);
};

//入口
billOrder.loadDetail = function(billNo,sysNo,rowIndex){
	
	billOrder.curRowIndex=rowIndex;
	
	var url = BasePath+'/bill_order/get';
	var reqParam={
		   "billNo":billNo
     };
	billOrder.ajaxRequest(url,reqParam,function(data){
		data.supplierNo = data.supplierNo+'→'+data.supplierNoStr;
		$('#dltDataForm').form('load',data);
		
		//底部单据状态显示栏
		$('#statusSp').html(data.statusStr);
		$('#statusTransSp').html(data.statusTransStr);
		$('#creatorSp').html(data.creator);
		$('#createtmSp').html(formatDate(data.createtm));
		$('#auditorSp').html(data.auditor);
		$('#auditdtSp').html(formatDate(data.auditdt));
	});
    billOrder.initGridHead(sysNo);
    billOrder.initGridData(billNo);
    $('#mainTab').tabs( 'select' ,"单据明细" );
};

billOrder.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	 });
};

billOrder.initSupplier = function(){
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

//切换选项卡刷新单据查询的dataGrid
function refreshTabs(){
	$('#mainTab').tabs({
		onSelect:function(title,index){
			
			$('#main_order_dtlId').tabs('resize');
			
		    $('#mainDataGrid').datagrid('resize', {
		    	width:function(){return document.body.clientWidth;}
			});
			
			$('#queryConditionDiv').panel('resize',{
				width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#easyui-panel-id').panel('resize',{
		    	width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#order_dtl1_dataGrid').datagrid('resize', {
		    	width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#order_dtl2_dataGrid').datagrid('resize', {
		    	width:function(){return document.body.clientWidth;}
		    });
		    
		},onLoad:function(panel){
			$('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		}
	});
	
	$('#main_order_dtlId').tabs({
		onSelect:function(title,index){
			
		    $('#order_dtl1_dataGrid').datagrid('resize', {
		    	width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#order_dtl2_dataGrid').datagrid('resize', {
		    	width:function(){return document.body.clientWidth;}
		    });
		}
	});
}

$(document).ready(function(){
	billOrder.initBrand();
	billOrder.initSupplier();
});