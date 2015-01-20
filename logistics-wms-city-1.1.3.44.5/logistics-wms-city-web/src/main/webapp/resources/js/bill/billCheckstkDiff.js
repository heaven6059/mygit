var billCheckstkDiff = {};

billCheckstkDiff.curRowIndex = -1;
billCheckstkDiff.preType = 0;

billCheckstkDiff.exportDataGridDtl1Id = 'billCheckstkDiff_dtl_dataGrid';
billCheckstkDiff.preColNamesDtl1 = [
		                   {title:"序列号",field:"seqId" },
		                   {title:"商品编码",field:"itemNo",width:120},
		                   {title:"商品名称",field:"itemName",width:120}
                    ];
billCheckstkDiff.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billCheckstkDiff.sizeTypeFiledName = 'sizeKind';

billCheckstkDiff.exportExcel=function(){
	var sysNo=$('#sysNo').combobox('getValue'); //品牌库
	var excelTitle='盘差单导出,订单号'+$('#billNo').val(); //标题+单据号 
	exportExcelBill(billCheckstkDiff.exportDataGridDtl1Id,sysNo,billCheckstkDiff.preColNamesDtl1,billCheckstkDiff.endColNamesDtl1,billCheckstkDiff.sizeTypeFiledName,excelTitle);
};

billCheckstkDiff.callBackBill=function(curRowIndex,rowData){
	if(billCheckstkDiff.preType==1 || billCheckstkDiff.preType==2){
		if(rowData!=null&&rowData!=''&&rowData!=[]){
			billCheckstkDiff.loadDetail(rowData.billNo,rowData.sysNo,curRowIndex);
			billCheckstkDiff.preType=0;
		}else{
			if(billCheckstkDiff.preType==1){
				alert('已经是第一单');
			}else{
				alert('已经是最后一单');
			}
		}
	}
};

billCheckstkDiff.preBill=function(){
	if(billCheckstkDiff.curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	billCheckstkDiff.preType=1;
    preBill('mainDataGrid',billCheckstkDiff.curRowIndex,1,billCheckstkDiff.callBackBill);
};

billCheckstkDiff.nextBill=function(){
	if(billCheckstkDiff.curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	billCheckstkDiff.preType=2;
    preBill('mainDataGrid',billCheckstkDiff.curRowIndex,2,billCheckstkDiff.callBackBill);
};

billCheckstkDiff.reload = function(){
    var tempObj = $('#mainDataGrid');
    tempObj.datagrid( 'reload' );
};

//格式化status：0-制单 100-完结
billCheckstkDiff.formatStatus = function(value){
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
billCheckstkDiff.formatStatusTrans = function(value){
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

billCheckstkDiff.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

billCheckstkDiff.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billCheckstkDiff.sizeTypeFiledName
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

billCheckstkDiff.initGridDataUtil = function(gridId,url,queryParams){
    var tempObj = $('#'+gridId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

billCheckstkDiff.initGridHead = function(sysNo){
    var beforeColArr = billCheckstkDiff.preColNamesDtl1;
	 var afterColArr = billCheckstkDiff.endColNamesDtl1; 
	 var columnInfo = billCheckstkDiff.getColumnInfo(sysNo,beforeColArr,afterColArr);
     $("#"+billCheckstkDiff.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
};

billCheckstkDiff.initGridData = function(billNo){
    var url = BasePath+ '/bill_checkstk_diff_dtl/queryBillCheckstkDiffDtlList';
    var queryParams = {billNo:billNo};
    billCheckstkDiff.initGridDataUtil(billCheckstkDiff.exportDataGridDtl1Id,url,queryParams);
};

billCheckstkDiff.loadDetail = function(billNo,sysNo,rowIndex){
	
	billCheckstkDiff.curRowIndex=rowIndex;
	
	var url = BasePath+'/bill_checkstk_diff/get';
	var reqParam={
		   "billNo":billNo
     };
	billCheckstkDiff.ajaxRequest(url,reqParam,function(data){
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
    billCheckstkDiff.initGridHead(sysNo);
    billCheckstkDiff.initGridData(billNo);
    $('#mainTab').tabs( 'select' ,"单据明细" );
};

billCheckstkDiff.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	 });
};

billCheckstkDiff.initStoreNo = function(){
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

billCheckstkDiff.initSupplier = function(){
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
			
			$('#mainDataGrid').datagrid('resize', {
			     width:function(){return document.body.clientWidth;}
			});
			
		    $('#easyui-panel-id').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#billCheckstkDiff_dtl_dataGrid').datagrid('resize', {
		        width:function(){return document.body.clientWidth;}
		     });
		    
		},onLoad:function(panel){
			$('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		}
		
	});
}

$(document).ready(function(){
	billCheckstkDiff.initBrand();
	billCheckstkDiff.initStoreNo();
	billCheckstkDiff.initSupplier();
});