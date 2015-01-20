var billDelivery = {};

billDelivery.curRowIndex = -1;
billDelivery.preType = 0;

//billDelivery.exportDataGridDtl1Id = 'delivery_dtl1_dataGrid';
billDelivery.preColNamesDtl1 = [
				                   {title:"序列号",field:"seqId" },
				                   {title:"商品编码",field:"itemNo",width:120},
				                   {title:"商品名称",field:"itemName",width:120}
                            ];
billDelivery.endColNamesDtl1 = [
			                      {title:"合计",field:"allCount"}
		                        ];
billDelivery.exportDataGridDtl2Id = 'delivery_dtl2_dataGrid';
billDelivery.preColNamesDtl2 = [
         	                   {title:"商品编码",field:"itemNo",width:120},
        	                   {title:"商品名称",field:"itemName",width:120}
                          ];
billDelivery.endColNamesDtl2 = [
         	                   {title:"合计",field:"allCount"}
         	                  ];
billDelivery.sizeTypeFiledName = 'sizeKind';

billDelivery.exportExcel=function(){
	var sysNo=$('#sysNo').combobox('getValue'); //品牌库
	//var tempTitle = $('#main_delivery_dtlId').tabs('getSelected').panel('options').title;
	var excelTitle='配送出库单导出,订单号'+$('#billNo').val(); //标题+单据号 
	exportExcelBill(billDelivery.exportDataGridDtl2Id,sysNo,billDelivery.preColNamesDtl2,billDelivery.endColNamesDtl2,billDelivery.sizeTypeFiledName,excelTitle);
//	if(tempTitle=='汇总'){
//		exportExcelBill(billDelivery.exportDataGridDtl1Id,sysNo,billDelivery.preColNamesDtl1,billDelivery.endColNamesDtl1,billDelivery.sizeTypeFiledName,excelTitle);
//	}else{
//		exportExcelBill(billDelivery.exportDataGridDtl2Id,sysNo,billDelivery.preColNamesDtl2,billDelivery.endColNamesDtl2,billDelivery.sizeTypeFiledName,excelTitle);
//	}
};

billDelivery.callBackBill=function(curRowIndex,rowData){
	if(billDelivery.preType==1 || billDelivery.preType==2){
		if(rowData!=null&&rowData!=''&&rowData!=[]){
			billDelivery.loadDetail(rowData.billNo,rowData.sysNo,curRowIndex);
			billDelivery.preType=0;
		}else{
			if(billDelivery.preType==1){
				alert('已经是第一单');
			}else{
				alert('已经是最后一单');
			}
		}
	}
};

billDelivery.preBill=function(){
	if(billDelivery.curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	billDelivery.preType=1;
    preBill('mainDataGrid',billDelivery.curRowIndex,1,billDelivery.callBackBill);
};

billDelivery.nextBill=function(){
	if(billDelivery.curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	billDelivery.preType=2;
    preBill('mainDataGrid',billDelivery.curRowIndex,2,billDelivery.callBackBill);
};

billDelivery.reload = function(){
    var tempObj = $('#mainDataGrid');
    tempObj.datagrid( 'reload' );
};

billDelivery.formatStatus = function(value){
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

billDelivery.formatStatusTrans = function(value){
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

billDelivery.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

billDelivery.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:'sizeKind'
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

billDelivery.initGridDataUtil = function(gridId,url,queryParams){
    var tempObj = $('#'+gridId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

billDelivery.initGridHead = function(sysNo){
    var beforeColArr = billDelivery.preColNamesDtl1;
	 var afterColArr = billDelivery.endColNamesDtl1; 
	 var columnInfo = billDelivery.getColumnInfo(sysNo,beforeColArr,afterColArr);
//     $("#"+billDelivery.exportDataGridDtl1Id).datagrid({
//         columns:columnInfo.columnArr
//     }); 
     beforeColArr = billDelivery.preColNamesDtl2;
	afterColArr = billDelivery.endColNamesDtl2; 
	columnInfo = billDelivery.getColumnInfo(sysNo,beforeColArr,afterColArr);
	$("#"+billDelivery.exportDataGridDtl2Id).datagrid({
	   columns:columnInfo.columnArr
	}); 
};

billDelivery.initGridData = function(billNo){
    var url = BasePath+ '/bill_delivery_dtl/queryDeliveryDtlList';
    var queryParams = {billNo:billNo};
    //billDelivery.initGridDataUtil(billDelivery.exportDataGridDtl1Id,url,queryParams);
    url = BasePath+ '/bill_delivery_dtl2/queryDeliveryDtlList';
    billDelivery.initGridDataUtil(billDelivery.exportDataGridDtl2Id,url,queryParams);
};

//入口
billDelivery.loadDetail = function(billNo,sysNo,rowIndex){
	
	billDelivery.curRowIndex=rowIndex;
	
	var url = BasePath+'/bill_delivery/get';
	var reqParam={
		   "billNo":billNo
     };
	billDelivery.ajaxRequest(url,reqParam,function(data){
		data.storeNo = data.storeNo+'→'+data.storeNoStr;
		data.storeNoFrom = data.storeNoFrom+'→'+data.storeNoFromStr;
		$('#dltDataForm').form('load',data);
		
		//底部单据状态显示栏
		$('#statusSp').html(data.statusStr);
		$('#statusTransSp').html(data.statusTransStr);
		$('#creatorSp').html(data.creator);
		$('#createtmSp').html(formatDate(data.createtm));
		$('#auditorSp').html(data.auditor);
		$('#auditdtSp').html(formatDate(data.auditdt));
	});
    billDelivery.initGridHead(sysNo);
    billDelivery.initGridData(billNo);
    $('#mainTab').tabs( 'select' ,"单据明细" );
};

billDelivery.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	 });
};

billDelivery.initStoreNoFrom = function(){
	$('#storeNoFrom').combogrid({
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

billDelivery.initStoreNo = function(){
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
		    
		    $('#delivery_dtl2_dataGrid').datagrid('resize', {
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
	billDelivery.initBrand();
	billDelivery.initStoreNoFrom();
	billDelivery.initStoreNo();
});