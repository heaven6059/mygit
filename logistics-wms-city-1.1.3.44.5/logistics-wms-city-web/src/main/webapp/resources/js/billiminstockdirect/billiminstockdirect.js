var billiminstockdirect = {};
var billiminstockdirect4check = {};
billiminstockdirect.status ={};

billiminstockdirect.statusTask ={
		"10":"新建",
		"13":"已发单"
	};

billiminstockdirect.statusTaskFormatter = function(value, rowData, rowIndex){
	return billiminstockdirect.statusTask[value];
};

billiminstockdirect.typeData = {};
//退仓类型
billiminstockdirect.typeFormatter  = function(value, rowData, rowIndex){
	return billiminstockdirect.typeData[value];
};

billiminstockdirect.qualityDataObj = {};
//品质
billiminstockdirect.qualityFormatter = function(value, rowData, rowIndex){
	return billiminstockdirect.qualityDataObj[value];
};

//委托业主
billiminstockdirect.ownnerData={};
//仓库
billiminstockdirect.locno;

$(document).ready(function(){
	//验收日期初始为前两天
	$("#startCheckTm").datebox('setValue',getDateStr(-2));
	
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["search_check_workerNo"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[true,false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billiminstockdirect.ownerNo,
			null);
	//初始化仓库
	billiminstockdirect.loadLoc();
	//初始化验收单的状态
	wms_city_common.comboboxLoadFilter(
			["search_check_status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CHECK_STATUS',
			{},
			billiminstockdirect.status,
			null);
	//初始化商品属性
	wms_city_common.comboboxLoadFilter(
			["itemTypeConditionY","itemTypeCondition"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
			{},
			billiminstockdirect.typeData,
			null);
	
	//初始化品质
	wms_city_common.comboboxLoadFilter(
			["qualityConditionY","qualityCondition"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			billiminstockdirect.qualityDataObj,
			null);
	
	$("#info_add").click(billiminstockdirect.save_do);
	$("#info_edit").click(billiminstockdirect.edit_do);

	$("#info_add").show();
	$("#info_edit").hide();
	//billiminstockdirect.initInstockWorker();	
	//初始化业务类型
	wms_city_common.comboboxLoadFilter(
			["businessType"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IM_CHECK_BUSINESS_TYPE',
			{},
			null,
			null);
	$('#businessType').combobox('setValue','1');
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch_receipt'),"brandNoObj":$('input[id=search_brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	var objs1 = [];
	objs1.push(
			{"sysNoObj":$('#sysNoSearch_check'),"brandNoObj":$('input[id=search_check_brandNo]',$('#searchFormCheck'))}
	);
	wms_city_common.loadSysNo4Cascade(objs1);
	
	$('#dataGridJG_detail').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
			   				billiminstockdirect.receiptQty = data.footer[1].receiptQty;
			   			}else{
			   				var rows = $('#dataGridJG_detail').datagrid('getFooterRows');
				   			rows[1]['receiptQty'] = billiminstockdirect.receiptQty;
				   			$('#dataGridJG_detail').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
	
	$('#dataGridJG_direct').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
			   				billiminstockdirect.instockQty = data.footer[1].instockQty;
			   			}else{
			   				var rows = $('#dataGridJG_direct').datagrid('getFooterRows');
				   			rows[1]['instockQty'] = billiminstockdirect.instockQty;
				   			$('#dataGridJG_direct').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
	
	$('#dataGridJGCheck_detail').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
			   				billiminstockdirect.poQty = data.footer[1].poQty;
			   				billiminstockdirect.checkQty = data.footer[1].checkQty;
			   				billiminstockdirect.difQty = data.footer[1].difQty;
			   				billiminstockdirect.divideQty = data.footer[1].divideQty;
			   				
			   			}else{
			   				var rows = $('#dataGridJGCheck_detail').datagrid('getFooterRows');
				   			rows[1]['poQty'] = billiminstockdirect.poQty;
				   			rows[1]['checkQty'] = billiminstockdirect.checkQty;
				   			rows[1]['difQty'] = billiminstockdirect.difQty;
				   			rows[1]['divideQty'] = billiminstockdirect.divideQty;
				   			$('#dataGridJGCheck_detail').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
	
	$('#dataGridJGCheck_direct').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
			   				billiminstockdirect4check.instockQty = data.footer[1].instockQty;
			   			}else{
			   				var rows = $('#dataGridJGCheck_direct').datagrid('getFooterRows');
				   			rows[1]['instockQty'] = billiminstockdirect4check.instockQty;
				   			$('#dataGridJGCheck_direct').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
	
	$('#wSendOrder_detail').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
			   				billiminstockdirect4check.instockQty4Wsend = data.footer[1].instockQty;
			   			}else{
			   				var rows = $('#wSendOrder_detail').datagrid('getFooterRows');
				   			rows[1]['instockQty'] = billiminstockdirect4check.instockQty4Wsend;
				   			$('#wSendOrder_detail').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
	
	$('#ySendOrder_detail').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
			   				billiminstockdirect4check.instockQty4Ysend = data.footer[1].instockQty;
			   			}else{
			   				var rows = $('#ySendOrder_detail').datagrid('getFooterRows');
				   			rows[1]['instockQty'] = billiminstockdirect4check.instockQty4Ysend;
				   			$('#ySendOrder_detail').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
});


//创建tab
$(function(){ 
	$('#ttAll').tabs({
        border:true,
        onSelect:function(title){
        	
        		//获取选中的tab
    	    	var pp = $('#ttAll').tabs('getSelected');   
    	    	var index = $('#ttAll').tabs('getTabIndex',pp);//easyui tabs获取选中索引
    	    	
//    	    	if(index==0){//按收货
//    	    		billiminstockdirect.loadDataGrid();
//    	    	}else if(index==1){//按验收
//    	    		billiminstockdirect.loadDataGridCheck();
//    	    	}else if(index==2){//上架发单
//    	    		//调整到未发单界面
//        			$('#ttSend').tabs("select", 0);
//        			billiminstockdirect.loadDataGridStockNew('10','1');
//    	    	}else{
//    	    		alert('未选中Tab！',1);
//    	    	}
    	    	
    	    	if(index==0){//按验收
    	    		billiminstockdirect.loadDataGridCheck();
    	    	}else if(index==1){//上架发单
    	    		//调整到未发单界面
        			$('#ttSend').tabs("select", 0);
        			billiminstockdirect.loadDataGridStockNew('10','1');
    	    	}else{
    	    		alert('未选中Tab！',1);
    	    	}
        }
       
    });
});


//创建tab
$(function(){ 
 	$('#ttSend').tabs({
         border:true,
         onSelect:function(title){
         		//获取选中的tab
     	    	var pp = $('#ttSend').tabs('getSelected');   
     	    	var index = $('#ttSend').tabs('getTabIndex',pp);//easyui tabs获取选中索引
     	    	if(index==0){//未发单
     	    		//$('#dataGridJG_detail').datagrid('clearData');
     	    		billiminstockdirect.loadDataGridStockNew('10','1');
     	    	}else if(index==1){//已发单
     	    		billiminstockdirect.loadDataGridStockNew('13','1');
     	    	}else{
     	    		alert('未选中Tab！',1);
     	    	}
         }
     });
}); 

//查询未发单的信息
billiminstockdirect.searchNoSendOrder = function(status,locateType){
	
	
	var obj = null;
	var data={};
	if(status =='10'){
		obj = $("#wSendOrder_detail");
		var fromObj=$('#searchFormNo');
		var validateForm= fromObj.form('validate');
		if(validateForm==false){
		     return ;
		}
		data={
			     'locno':billiminstockdirect.locno,
				 'itemType':$('#itemTypeCondition').combobox('getValue'),
			     'quality':$('#qualityCondition').combobox('getValue'),
			     'locateType':locateType,
			     'sourceNo':$('#sourceNoCondition').val(),
			     'status': status
	    };
	}else{
		obj = $("#ySendOrder_detail");
		data={
			     'locno':billiminstockdirect.locno,
				 'itemType':$('#itemTypeConditionY').combobox('getValue'),
			     'quality':$('#qualityConditionY').combobox('getValue'),
			     'locateType':locateType,
			     'sourceNo':$('#sourceNoConditionY').val(),
			     'status': status
			};
	}
	
	var queryMxURL=BasePath+'/bill_im_instock_direct/findInstockDirectByType';
	obj.datagrid('options').url=queryMxURL;
	obj.datagrid('options').queryParams= data;
	obj.datagrid('load');
	
	//billiminstockdirect.loadDataGridStockNew(status);
};


//加载未发单的数据
billiminstockdirect.loadDataGridStockNew = function(status,locateType){
	var obj = $("#wSendOrder_detail");
	if(status =='13'){
		obj = $("#ySendOrder_detail");
	}
	var queryMxURL=BasePath+'/bill_im_instock_direct/findInstockDirectByType?status='+status+'&locateType='+locateType+'&locno='+billiminstockdirect.locno;
	obj.datagrid('options').url=queryMxURL;
	obj.datagrid('load');
};

//加载仓库信息
billiminstockdirect.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billiminstockdirect.locno = data.locno;
		}
	});
};


billiminstockdirect.statusFormatter = function(value, rowData, rowIndex){
	return billiminstockdirect.status[value];
};

billiminstockdirect.converStr2JsonObj= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billiminstockdirect.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//加载数据-按收货
billiminstockdirect.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bill_im_receipt/selectImport4Direct?locno='+billiminstockdirect.locno,
    			'pageNumber':1,
    			onLoadSuccess:function(data){
	    			$('#dataGridJG').datagrid('selectRow', 0);
	   			 }
    		});
};

//加载数据-按验收
billiminstockdirect.loadDataGridCheck = function(){
     $('#dataGridJGCheck').datagrid(
    		{
    			'url':BasePath+'/bill_im_check/findCheckForDirect?locno='+billiminstockdirect.locno+'&businessType=1',
    			'pageNumber':1,
    			onLoadSuccess:function(data){
	    			$('#dataGridJGCheck').datagrid('selectRow', 0);
	   			}
    		});
};

//单击一行查看收货明细
billiminstockdirect.instockDetail = function(){
	var rowData = $("#dataGridJG").datagrid("getSelections")[0];
	if(rowData==null){
		$('#dataGridJG_detail').datagrid('clearData');
		return;
	}
	var queryMxURL=BasePath+'/bill_im_receipt_dtl/selectDetail4Instock?receiptNo='+rowData.receiptNo+'&locno='+billiminstockdirect.locno+'&ownerNo='+rowData.ownerNo;
	$( "#dataGridJG_detail").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_detail").datagrid( 'load' );
};


//单击一行查看定位明细
billiminstockdirect.instockDirectDetail = function(){
	var rowData = $("#dataGridJG").datagrid("getSelections")[0];
	if(rowData==null){
		$('#dataGridJG_direct').datagrid('clearData');
		return;
	}
	var queryMxURL=BasePath+'/bill_im_instock_direct/selectDirect?sourceNo='+rowData.receiptNo+'&locno='+billiminstockdirect.locno+'&ownerNo='+rowData.ownerNo;
	$( "#dataGridJG_direct").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_direct").datagrid( 'load' );
};



//单击一行查看验收明细
billiminstockdirect.instockCheckDetail = function(){
	var rowData = $("#dataGridJGCheck").datagrid("getSelections")[0];
	if(rowData==null){
		//清空datagrid数据
		$('#dataGridJGCheck_detail').datagrid('clearData');
		return;
	}
	var queryMxURL=BasePath+'/bill_im_check_dtl/getByPage.json?checkNo='+rowData.checkNo+'&locno='+billiminstockdirect.locno+'&ownerNo='+rowData.ownerNo;
	$( "#dataGridJGCheck_detail").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGCheck_detail").datagrid( 'load' );
};


//单击一行查看验收明细对应的定位明细
billiminstockdirect.instockCheckDirectDetail = function(){
	var rowData = $("#dataGridJGCheck").datagrid("getSelections")[0];
	if(rowData==null){
		$('#dataGridJGCheck_direct').datagrid('clearData');
		return;
	}
	var queryMxURL=BasePath+'/bill_im_instock_direct/findInstockDirectByCheckNo?checkNo='+rowData.checkNo+'&locno='+billiminstockdirect.locno+'&ownerNo='+rowData.ownerNo;
	$( "#dataGridJGCheck_direct").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGCheck_direct").datagrid( 'load' );
};


//查看发单人
billiminstockdirect.initInstockWorker = function(rowData){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#instockWorker,#instockWorkerCheck').combobox({
			     valueField:"workerNo",
			     textField:"valueAndText",
			     data:data,
			     panelHeight:150,
			    // multiple:true,
			     loadFilter:function(data){
	    			if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.workerNo+'→'+tempData.workerName;
			       		 }
	       	 		}
	    			return data;
	   			 } 
			  }).combobox("select",data[0].workerNo);
		}
	});
};


//委托业主
billiminstockdirect.ownerFormatter = function(value, rowData, rowIndex){
	return billiminstockdirect.ownnerData[value];
};
//未定位数量
billiminstockdirect.notQtyFormatter = function(value, rowData, rowIndex){
	return rowData.receiptQty-rowData.checkQty;
};

//查询收货单信息
billiminstockdirect.searchArea = function(){
	
	var startTm =  $('#startTm').datebox('getValue');
	var endTm =  $('#endTm').datebox('getValue');
	if(!isStartEndDate(startTm,endTm)){    
		alert("收货日期开始日期不能大于结束日期");   
        return;   
    }
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_im_receipt/selectImport4Direct?locno='+billiminstockdirect.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};


//查询验收单的数据
billiminstockdirect.searchCheck = function(){
	
	var startTm =  $('#startCheckTm').datebox('getValue');
	var endTm =  $('#endCheckTm').datebox('getValue');
	if(!isStartEndDate(startTm,endTm)){    
		alert("验收日期开始日期不能大于结束日期");   
        return;   
    }
	var fromObjStr=convertArray($('#searchFormCheck').serializeArray());
	var queryMxURL=BasePath+'/bill_im_check/findCheckForDirect?locno='+billiminstockdirect.locno;
	var queryParams = eval("(" +fromObjStr+ ")");
	queryParams['businessType'] = "1";
    $( "#dataGridJGCheck").datagrid( 'options' ).queryParams = queryParams;
    $( "#dataGridJGCheck").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGCheck").datagrid( 'load' );
    
};


function isStartEndDate(startDate,endDate){   
    if(startDate.length>0&&endDate.length>0){   
        var arrStartDate = startDate.split("-");   
        var arrEndDate = endDate.split("-");   
        var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2]);   
        var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2]);   
        if(allStartDate.getTime()>allEndDate.getTime()){   
             return false;   
        }   
     }   
     return true;   
} 

billiminstockdirect.searchLocClear = function(){
	$('#searchForm').form("clear");
	$('#search_brandNo').combobox("loadData",[]);
};

billiminstockdirect.searchLocClearCheck = function(formNa){
	$('#'+formNa).form("clear");
	if(formNa=='searchFormCheck'){
		$('#search_check_brandNo').combobox("loadData",[]);
	}
};

function convertArray(o) { 
	var v = {};
	for ( var i in o) {
		if (typeof (v[o[i].name]) == 'undefined')
			v[o[i].name] = o[i].value;
		else
			v[o[i].name] += "," + o[i].value;
	}
	return JSON.stringify(v);
} ;

//定位
billiminstockdirect.instockDirect = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要定位的记录!',1);
		return;
	}
	if(checkedRows[0].status=="30"){
		alert('已预约的单据不能定位!',1);
		return;
	}
	
	$.messager.confirm("确认","你确定要定位这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	  var keyStr = [];
        	var locNo;
        	var receiptNo;
        	var ownerNo;
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.locno+"|"+item.ownerNo+"|"+item.receiptNo+"|"+item.receiptWorker);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_im_instock_direct/instockDirect';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
           wms_city_common.loading("show","正在定位......");
	       $.ajax({
				async : true,
				cache : false,
				type : 'POST',
				data:{keyStr:keyStr.join(",")},
				dataType : "json",
				url:url,
				success : function(data) {
					wms_city_common.loading();
					if(data.result=='success'){
		    			 alert('定位成功!');
						 billiminstockdirect.loadDataGrid();
		        	}else{
		        		alert(data.msg,2);
		        	}
	       		}
        	});
   	 	}
   	 
    }); 
};

//取消定位
billiminstockdirect.cancelDirect = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要取消的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要取消定位这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var keyStr = [];
        	var locNo;
        	var sourceNo;
        	var ownerNo;
        	$.each(checkedRows, function(index, item){
        		//keyStr.push(item.rowId+"|"+item.itemNo);
        		if(index==0){
        			locNo = item.locno;
        			sourceNo = item.receiptNo;
        			ownerNo = item.ownerNo;
        		}
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_im_instock_direct/cancelDirectForAll';
        	var data={
        	    //"keyStr":keyStr.join(","),
        	    "locNo":locNo,
        	    "sourceNo":sourceNo,
        	    "flag":'R',
        	    "ownerNo":ownerNo
        	};
        	//3. 取消定位
        	wms_city_common.loading("show","正在取消定位......");
        	billiminstockdirect.ajaxRequest(url,data,function(result){
        		wms_city_common.loading();
        		if(result.result=='success'){
       			 	 alert('取消成功!');
					$( "#dataGridJG").datagrid( 'reload' );
	       		 }else{
	       			 alert(result.msg,1);
	       		 }
        	}); 
        }  
    }); 
};



//发单
billiminstockdirect.createInstock = function(){
	var checkedRows = $("#dataGridJG_direct").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择定位信息!',1);
		return;
	}
	if($("#instockWorker").val()==null ||$("#instockWorker").val()==''){
		alert('请选择用户!',1);
		return;
	}
	$.messager.confirm("确认","你确定要发单吗？", function (r){  
        if (r) {
        	var rowStrs = [];
        	var locNo;
        	$.each(checkedRows, function(index, item){
        		rowStrs.push(item.rowId);
        		if(index==0){
        			locNo = item.locno;
        		}
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_im_instock_direct/createInstock';
        	var data={
        	    "rowStrs":rowStrs.join(","),
        	    "locNo":locNo,
        	    "instockWorker":$("#instockWorker").val()
        	};
       		$.ajax({
				async : true,
				cache : false,
				type : 'POST',
				data:{
	        	    "rowStrs":rowStrs.join(","),
	        	    "locNo":locNo,
	        	    "instockWorker":$("#instockWorker").val()
        		},
				dataType : "json",
				url:url,
				success : function(data) {
					if(data.result=='success'){
		    			alert('发单成功!');
						 $( "#dataGridJG").datagrid( 'reload' );
		        	}else{
		        		alert(data.msg,2);
		        	}
	       		}
        	});
        }  
    }); 
};


//入库上架发单
billiminstockdirect.sendOrderByType = function(){
	var checkedRows = $("#wSendOrder_detail").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要发单的上架任务信息!',1);
		return;
	}
	if($("#instockWorkerSend").val()==null ||$("#instockWorkerSend").val()==''){
		alert('请选择用户!',1);
		return;
	}
	$.messager.confirm("确认","你确定要发单吗？", function (r){
        if (r) {
        	var rowStrs = [];
        	var ownerNo;
        	var firstItemtypeAndQuality;
        	var itemtypeAndQuality;
        	var isR = 'N';
        	$.each(checkedRows, function(index, item){
        		rowStrs.push(item.rowId);
        		if(index==0){
        			ownerNo = item.ownerNo;
        			firstItemtypeAndQuality = item.itemType+"-"+item.quality;
        		}
        		itemtypeAndQuality = item.itemType+"-"+item.quality;
        		if(firstItemtypeAndQuality != itemtypeAndQuality){
        			isR = 'Y';
        			return false;
        		}
        	});    
        	
        	//如果不是相同的属性和品质，则不能发单
        	if(isR == 'Y'){
        		alert('请选择属性和品质都相同的明细一起发单!',1);
    			return;
        	}
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_im_instock_direct/sendOrder';
        	var data={
        	    "rowIdList":rowStrs.join(","),
        	    "locno":billiminstockdirect.locno,
        	    "ownerNo":ownerNo,
        	    "sender":$("#instockWorkerSend").val()
        	};
        	wms_city_common.loading("show","正在发单......");
        	billiminstockdirect.ajaxRequest(url,data,function(result){
         		if(result.result=='success'){
       			 	alert('发单成功!');
       			    $("#instockWorkerSend").val("");
       			    billiminstockdirect.loadDataGridStockNew('10','1');
       			 	//跳转到已发单界面
       			 	//$("#ttCheck").tabs("select", 1);
//       				var  cellNostr = '';
//       				var queryMxURL=BasePath+'/bill_um_direct/list.json?untreadMmNo='+untreadMmNo+'&locno='+billumdirect.locno+'&cellNo='+cellNostr;
//       				$( "#dataGridJG_direct").datagrid( 'options' ).url=queryMxURL;
//       			    $( "#dataGridJG_direct").datagrid( 'load' );
	       		 }else{
	       			 alert(result.msg,1);
	       		 }
         		wms_city_common.loading();
        	}); 
        }  
    }); 
};

//定位-验收
billiminstockdirect.instockDirectCheck = function(){
	var checkedRows = $("#dataGridJGCheck").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要定位的记录!',1);
		return;
	}
	if(checkedRows[0].status=="30"){
		alert('已预约的单据不能定位!',1);
		return;
	}
	
	$.messager.confirm("确认","你确定要定位这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	  var keyStr = [];
        	var locNo;
        	var receiptNo;
        	var ownerNo;
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.locno+"|"+item.ownerNo+"|"+item.checkNo+"|"+item.receiptWorker);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_im_instock_direct/instockDirectForCheck';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
           wms_city_common.loading("show","正在定位......");
	       $.ajax({
				async : true,
				cache : false,
				type : 'POST',
				data:{keyStr:keyStr.join(",")},
				dataType : "json",
				url:url,
				success : function(data) {
					wms_city_common.loading();
					if(data.result=='success'){
		    			 alert('定位成功!');
		    			 $( "#dataGridJGCheck").datagrid( 'reload' );
		        	}else{
		        		alert(data.msg,2);
		        	}
	       		}
        	});
   	 	}
   	 
    }); 
};

//取消定位-验收
billiminstockdirect.cancelDirectCheck = function(){
	var checkedRows = $("#dataGridJGCheck").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要取消的记录!',1);
		return;
	}
	if(checkedRows.length > 1){
		alert('请选择一条记录！',1);
		return;
	}
	if(checkedRows[0].status!="30"){
		alert('已预约的单据才能进行取消定位操作!',1);
		return;
	}
	$.messager.confirm("确认","你确定要取消定位这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var keyStr = [];
        	var locNo;
        	var sourceNo;
        	var ownerNo;
        	$.each(checkedRows, function(index, item){
        		//keyStr.push(item.rowId+"|"+item.itemNo);
        		if(index==0){
        			locNo = item.locno;
        			sourceNo =  item.checkNo;
        			ownerNo = item.ownerNo;
        		}
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_im_instock_direct/cancelDirectForAll';
        	var data={
        	    //"keyStr":keyStr.join(","),
        	    "locNo":locNo,
        	    "sourceNo":sourceNo,
        	    "flag":'C',
        	    "ownerNo":ownerNo
        	};
        	//3. 取消定位
        	wms_city_common.loading("show","正在取消定位......");
        	billiminstockdirect.ajaxRequest(url,data,function(result){
        		 wms_city_common.loading();
        		 if(result.result=='success'){
        			 alert('取消成功!');
        			 $( "#dataGridJGCheck").datagrid( 'reload' );
        		 }else{
        			 alert(result.msg,1);
        		 }
        	}); 
        }  
    }); 
};



//发单-验收
billiminstockdirect.createInstockCheck = function(){
	
	var checkedRows = $("#dataGridJGCheck_direct").datagrid("getChecked");// 获取所有勾选checkbox的行
	
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择定位信息!',1);
		return;
	}
	if($("#instockWorkerCheck").val()==null ||$("#instockWorkerCheck").val()==''){
		alert('请选择用户!',1);
		return;
	}
	$.messager.confirm("确认","你确定要发单吗？", function (r){  
        if (r) {
        	var rowStrs = [];
        	var locNo;
        	$.each(checkedRows, function(index, item){
        		rowStrs.push(item.rowId);
        		if(index==0){
        			locNo = item.locno;
        		}
        	}); 
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_im_instock_direct/createInstock';
        	var data={
        	    "rowStrs":rowStrs.join(","),
        	    "locNo":locNo,
        	    "instockWorker":$("#instockWorkerCheck").val()
        	};
       		$.ajax({
				async : true,
				cache : false,
				type : 'POST',
				data:{
	        	    "rowStrs":rowStrs.join(","),
	        	    "locNo":locNo,
	        	    "instockWorker":$("#instockWorkerCheck").val()
        		},
				dataType : "json",
				url:url,
				success : function(data) {
					if(data.result=='success'){
		    			 alert('发单成功!');
		    			 $( "#dataGridJGCheck").datagrid( 'reload' );
		        	}else{
		        		alert(data.msg,2);
		        	}
	       		}
        	});
        }  
    }); 
};

billiminstockdirect.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};



//选择用户
billiminstockdirect.selectPickingPeople = function(){
	$('#showSelectPickingPeopleWin').show().window('open');  
	$('#pickingPeopleDataGrid').datagrid('clearData');
	billiminstockdirect.loadRoleList();
};

//加载岗位信息
billiminstockdirect.loadRoleList = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'POST',
		dataType : "json",
		url:BasePath+'/authority_organization/role.json',
		success : function(data) {
			$('#roleid').combobox({
			    data:data,
			    valueField:'roleId',    
			    textField:'roleName',
			    panelHeight:150,
			    onSelect:function(data){
//			    	billiminstockdirect.loadDataUserInfoByRoleId(data.roleId);
			    	var uList=[];
			    	 $.each(data.userList,function(i,o){
			    		 o.roleName=data.roleName;
			    		 uList.push(o);
			    	 });
			    	 $('#pickingPeopleDataGrid').datagrid({data:uList});
			    }
			});
		},error:function(){
			alert('加载岗位信息异常,请联系管理员!',2);
		}
	});
};

//根据角色ID查询对应下的所有用户信息 
billiminstockdirect.loadDataUserInfoByRoleId = function(roleId){
	$('#pickingPeopleDataGrid').datagrid('clearData');
	var queryMxURL=BasePath+'/bill_im_instock_direct/getUserListByRoleId?roleId='+roleId;
    //3.加载明细
    $( "#pickingPeopleDataGrid").datagrid( 'options' ).url=queryMxURL;
    $( "#pickingPeopleDataGrid").datagrid( 'load' );
	
};
//确定用户
billiminstockdirect.confirmPickingPeople = function(){
	var checkedRows = $("#pickingPeopleDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请至少选择一个人员信息！',1);
		return;
	}
	if(checkedRows.length > 10){
		alert('最多只能选择十个人员信息！',1);
		return;
	}
	$.messager.confirm("确认","您确定要选择这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	wms_city_common.loading("show");
        	var noStrs = [];
        	$.each(checkedRows, function(index,item){
        		noStrs.push(item.loginName);
        	}); 
        	
        	var pp = $('#ttAll').tabs('getSelected');   
        	var index = $('#ttAll').tabs('getTabIndex',pp);//easyui tabs获取选中索引
//        	if(index == 0){
//        		$("#instockWorker").val();
//            	$('#instockWorker').val(noStrs.join(","));
//        	}else if(index == 1){
//        		$("#instockWorkerCheck").val();
//            	$("#instockWorkerCheck").val(noStrs.join(","));
//        	}else{
//        		$("#instockWorkerSend").val();
//            	$("#instockWorkerSend").val(noStrs.join(","));
//        	}
        	
        	if(index == 0){
        		$("#instockWorkerCheck").val();
            	$("#instockWorkerCheck").val(noStrs.join(","));
        	}else{
        		$("#instockWorkerSend").val();
            	$("#instockWorkerSend").val(noStrs.join(","));
        	}
        	wms_city_common.loading();
        	$('#showSelectPickingPeopleWin').window('close').hide();  
        	
        }
	});   
};

billiminstockdirect.closeShowWin = function(id){
	$('#'+id).window('close');
};