var billiminstockdirect = {};

billiminstockdirect.status ={
	"20":"收货完成",
	"25":"验收完成",
	"50":"异常验货",
	"30":"已预约"
};
//委托业主
billiminstockdirect.ownnerData={};
//仓库
billiminstockdirect.locno;

$(document).ready(function(){
	//初始化业主
	billiminstockdirect.loadOwner();
	//初始化仓库
	billiminstockdirect.loadLoc();
	//加载列表数据
	billiminstockdirect.loadDataGrid();
	
	$("#info_add").click(billiminstockdirect.save_do);
	$("#info_edit").click(billiminstockdirect.edit_do);

	$("#info_add").show();
	$("#info_edit").hide();
});


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

//加载委托业主信息
billiminstockdirect.loadOwner = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/entrust_owner/get_biz',
		success : function(data) {
			$('#search_workerNo').combobox({
			    data:data,
			    valueField:'ownerNo',    
			    textField:'ownerName',
			    panelHeight:"auto"
			});  
			billiminstockdirect.ownnerData = billiminstockdirect.converStr2JsonObj(data);
		}
	});
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

//加载数据
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
//单击一行查看收货明细
billiminstockdirect.instockDetail = function(){
	var rowData = $("#dataGridJG").datagrid("getSelections")[0];
	if(rowData==null){
		$( "#dataGridJG_detail").datagrid("loadData",[]);
		return;
	}
	var queryMxURL=BasePath+'/bill_im_receipt_dtl/list.json?receiptNo='+rowData.receiptNo+'&locno='+billiminstockdirect.locno+'&ownerNo='+rowData.ownerNo;
	$( "#dataGridJG_detail").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_detail").datagrid( 'load' );
};
//单击一行查看定位明细
billiminstockdirect.instockDirectDetail = function(){
	var rowData = $("#dataGridJG").datagrid("getSelections")[0];
	if(rowData==null){
		$( "#dataGridJG_direct").datagrid("loadData",[]);
		return;
	}
	var queryMxURL=BasePath+'/bill_im_instock_direct/list.json?receiptNo='+rowData.receiptNo+'&locno='+billiminstockdirect.locno+'&ownerNo='+rowData.ownerNo;
	$( "#dataGridJG_direct").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_direct").datagrid( 'load' );
};

billiminstockdirect.initInstockWorker = function(rowData){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/role.json',
		success : function(data) {
			$('#instockWorker').combobox({
			     valueField:"workerNo",
			     textField:"workerName",
			     data:data,
			     panelHeight:"auto",
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
//查询区域信息
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
		alert('请选择要取消的记录!',1);
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
        	
	       $.ajax({
				async : true,
				cache : false,
				type : 'POST',
				data:{keyStr:keyStr.join(",")},
				dataType : "json",
				url:url,
				success : function(data) {
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
	var checkedRows = $("#dataGridJG_direct").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要取消的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要取消定位这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var keyStr = [];
        	var locNo;
        	var receiptNo;
        	var ownerNo;
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.rowId+"|"+item.itemNo);
        		if(index==0){
        			locNo = item.locno;
        			receiptNo =  item.sourceNo;
        			ownerNo = item.ownerNo;
        		}
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_im_instock_direct/cancelDirect';
        	var data={
        	    "keyStr":keyStr.join(","),
        	    "locNo":locNo,
        	    "receiptNo":receiptNo,
        	    "ownerNo":ownerNo
        	};
        	//3. 取消定位
        	billiminstockdirect.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 alert('取消成功!');
					 billiminstockdirect.instockDirectDetail();
					 billiminstockdirect.instockDetail();
        		 }else{
        			 alert('取消失败,请联系管理员!',2);
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
        	    "instockWorker":$("#instockWorker").combobox("getValue")
        	};
       		$.ajax({
				async : true,
				cache : false,
				type : 'POST',
				data:{
	        	    "rowStrs":rowStrs.join(","),
	        	    "locNo":locNo,
	        	    "instockWorker":$("#instockWorker").combobox("getValue")
        		},
				dataType : "json",
				url:url,
				success : function(data) {
					if(data.result=='success'){
		    			 alert('发单成功!');
						 billiminstockdirect.loadDataGrid();
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
