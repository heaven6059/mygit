var billAsn = {};

billAsn.curRowIndex = -1;
billAsn.preType = 0;

billAsn.exportDataGridDtl1Id = 'asn_dtl_dataGrid';
billAsn.preColNamesDtl1 = [
		                   {title:"序列号",field:"seqId" },
		                   {title:"商品编码",field:"itemNo",width:120},
		                   {title:"商品名称",field:"itemName",width:120},
		                   {title:"箱号",field:"boxNo" },
		                   {title:"收货方",field:"storeNoStr",width:120}
                    ];
billAsn.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"},
		                      {title:"单价",field:"cost"},
		                      {title:"金额",field:"allCost"}
                        ] ;
billAsn.sizeTypeFiledName = 'sizeKind';

billAsn.exportExcel=function(){
	var sysNo=$('#sysNo').combobox('getValue'); //品牌库
	var excelTitle='预计到货通知单导出,订单号'+$('#billNo').val(); //标题+单据号 
	exportExcelBill(billAsn.exportDataGridDtl1Id,sysNo,billAsn.preColNamesDtl1,billAsn.endColNamesDtl1,billAsn.sizeTypeFiledName,excelTitle);
};

billAsn.callBackBill=function(curRowIndex,rowData){
	if(billAsn.preType==1 || billAsn.preType==2){
		if(rowData!=null&&rowData!=''&&rowData!=[]){
			billAsn.loadDetail(rowData.billNo,rowData.sysNo,curRowIndex);
			billAsn.preType=0;
		}else{
			if(billAsn.preType==1){
				alert('已经是第一单');
			}else{
				alert('已经是最后一单');
			}
		}
	}
};

billAsn.preBill=function(){
	if(billAsn.curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	billAsn.preType=1;
    preBill('mainDataGrid',billAsn.curRowIndex,1,billAsn.callBackBill);
};

billAsn.nextBill=function(){
	if(billAsn.curRowIndex<0){
		alert('不存在当前单据');
		return;
	}
	billAsn.preType=2;
    preBill('mainDataGrid',billAsn.curRowIndex,2,billAsn.callBackBill);
};

billAsn.reload = function(){
    var tempObj = $('#mainDataGrid');
    tempObj.datagrid( 'reload' );
};

billAsn.formatStatus = function(value){
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

billAsn.formatStatusTrans = function(value){
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

billAsn.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

billAsn.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billAsn.sizeTypeFiledName
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

billAsn.initGridDataUtil = function(gridId,url,queryParams){
    var tempObj = $('#'+gridId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

billAsn.initGridHead = function(sysNo){
    var beforeColArr = billAsn.preColNamesDtl1;
	 var afterColArr = billAsn.endColNamesDtl1; 
	 var columnInfo = billAsn.getColumnInfo(sysNo,beforeColArr,afterColArr);
     $("#"+billAsn.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
};

billAsn.initGridData = function(billNo){
    var url = BasePath+ '/bill_asn_dtl/queryAsnDtlList';
    var queryParams = {billNo:billNo};
    billAsn.initGridDataUtil(billAsn.exportDataGridDtl1Id,url,queryParams);
};

billAsn.loadDetail = function(billNo,sysNo,rowIndex){
	
	billAsn.curRowIndex=rowIndex;
	
	var url = BasePath+'/bill_asn/get';
	var reqParam={
		   "billNo":billNo
     };
	billAsn.ajaxRequest(url,reqParam,function(data){
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
    billAsn.initGridHead(sysNo);
    billAsn.initGridData(billNo);
    $('#mainTab').tabs( 'select' ,"单据明细" );
};

billAsn.initBrand = function(){
	$('#sysNo').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"150"
	 });
};

billAsn.initStoreNoFrom = function(){
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
		    
		    $('#asn_dtl_dataGrid').datagrid('resize', {
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
	billAsn.initBrand();
	billAsn.initStoreNoFrom();
});