var billReturn = {};

billReturn.curRowIndex = -1;
billReturn.preType = 0;

billReturn.exportDataGridDtl1Id = 'return_dtl_dataGrid';
billReturn.preColNamesDtl1 = [
			                   {title:"序列号",field:"seqId" },
			                   {title:"商品编码",field:"itemNo",width:120},
			                   {title:"商品名称",field:"itemName",width:120},
			                   {title:"箱号",field:"boxNo" }
                        ];
billReturn.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"},
		                      {title:"单价",field:"cost"},
		                      {title:"金额",field:"allCost"}
                        ];
billReturn.sizeTypeFiledName = 'sizeKind';

billReturn.exportExcel=function(){
	var sysNo=$('#sysNo').combobox('getValue'); //品牌库
	var excelTitle='退货单导出,订单号'+$('#billNo').val(); //标题+单据号 
	exportExcelBill(billReturn.exportDataGridDtl1Id,sysNo,billReturn.preColNamesDtl1,billReturn.endColNamesDtl1,billReturn.sizeTypeFiledName,excelTitle);
};

billReturn.callBackBill=function(curRowIndex,rowData){
	if(billReturn.preType==1 || billReturn.preType==2){
		if(rowData!=null&&rowData!=''&&rowData!=[]){
			billReturn.loadDetail(rowData.billNo,rowData.sysNo,curRowIndex);
			billReturn.preType=0;
		}else{
			if(billReturn.preType==1){
				alert('已经是第一单');
			}else{
				alert('已经是最后一单');
			}
		}
	}
};

billReturn.preBill=function(){
	if(billReturn.curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	billReturn.preType=1;
    preBill('mainDataGrid',billReturn.curRowIndex,1,billReturn.callBackBill);
};

billReturn.nextBill=function(){
	if(billReturn.curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	billReturn.preType=2;
    preBill('mainDataGrid',billReturn.curRowIndex,2,billReturn.callBackBill);
};

billReturn.reload = function(){
    var tempObj = $('#mainDataGrid');
    tempObj.datagrid( 'reload' );
};

billReturn.formatStatus = function(value){
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

billReturn.formatStatusTrans = function(value){
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

billReturn.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

billReturn.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billReturn.sizeTypeFiledName
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

billReturn.initGridDataUtil = function(gridId,url,queryParams){
    var tempObj = $('#'+gridId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

billReturn.initGridHead = function(sysNo){
    var beforeColArr = billReturn.preColNamesDtl1;
	 var afterColArr = billReturn.endColNamesDtl1; 
	 var columnInfo = billReturn.getColumnInfo(sysNo,beforeColArr,afterColArr);
     $("#"+billReturn.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
};

billReturn.initGridData = function(billNo){
    var url = BasePath+ '/bill_return_dtl/queryReturnDtlList';
    var queryParams = {billNo:billNo};
    billReturn.initGridDataUtil(billReturn.exportDataGridDtl1Id,url,queryParams);
};

billReturn.loadDetail = function(billNo,sysNo,rowIndex){
	
	billReturn.curRowIndex=rowIndex;
	
	var url = BasePath+'/bill_return/get';
	var reqParam={
		   "billNo":billNo
     };
	billReturn.ajaxRequest(url,reqParam,function(data){
		data.storeNo = data.storeNo+'→'+data.storeNoStr;
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
    billReturn.initGridHead(sysNo);
    billReturn.initGridData(billNo);
    $('#mainTab').tabs( 'select' ,"单据明细" );
};

billReturn.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	 });
};

billReturn.initStoreNo = function(){
	$('#storeNo').combogrid({
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
          loadFilter: function(data){
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

billReturn.initSupplier = function(){
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

$(document).ready(function(){
	billReturn.initBrand();
	billReturn.initStoreNo();
	billReturn.initSupplier();
});


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
		    
		    $('#return_dtl_dataGrid').datagrid('resize', {
		        width:function(){return document.body.clientWidth;}
		    });
		    
		},onLoad:function(panel){
			$('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		}
		
	});
	
}