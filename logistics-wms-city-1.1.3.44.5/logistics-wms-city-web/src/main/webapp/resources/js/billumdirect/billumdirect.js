var billumdirect = {};

billumdirect.status = {};
billumdirect.r_initStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["search_status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_UM_direct_Status',
			{},
			billumdirect.status,
			null);
};
billumdirect.qualityDataObj ={};
billumdirect.initQuality = function(){
	wms_city_common.comboboxLoadFilter(
			["quality_search"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			billumdirect.qualityDataObj,
			null);
};
billumdirect.typeData ={};
billumdirect.initUntreadType = function(){
	wms_city_common.comboboxLoadFilter(
			["untreadType_search"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
			{},
			billumdirect.typeData,
			null);
};

//加载委托业主信息
billumdirect.ownnerData={};
billumdirect.loadOwner = function(){
	wms_city_common.comboboxLoadFilter(
			["search_workerNo"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[true],
			BasePath+'/entrust_owner/get_biz',
			{},
			billumdirect.ownnerData,
			null);
};



//仓库
billumdirect.locno;

$(document).ready(function(){
	//初始化业主
	billumdirect.loadOwner();
	//初始化仓库
	billumdirect.loadLoc();
	//加载列表数据
	//billumdirect.loadDataGrid();
	billumdirect.initQuality();
	billumdirect.initUntreadType();
	//收货单的状态
	billumdirect.r_initStatus();
	
	//加载品牌
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')}//查询
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	$("#info_add").click(billumdirect.save_do);
	$("#info_edit").click(billumdirect.edit_do);

	$("#info_add").show();
	$("#info_edit").hide();
	
	$('#dataGridJG_detail').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billumdirect.estQty = data.footer[1].estQty;
		   			}else{
		   				var rows = $('#dataGridJG_detail').datagrid('getFooterRows');
			   			rows[1]['estQty'] = billumdirect.estQty;
			   			$('#dataGridJG_detail').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	
	$('#dataGridJG_direct').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billumdirect.estQty2 = data.footer[1].estQty;
		   			}else{
		   				var rows = $('#dataGridJG_direct').datagrid('getFooterRows');
		   				rows[1]['estQty'] = billumdirect.estQty2;
			   			$('#dataGridJG_direct').datagrid('reloadFooter');
		   			}
		   		}
			}
	);
});



billumdirect.converStr2JsonObj2= function(data){
	if(data==null){
		 return "";
	}
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//加载仓库信息
billumdirect.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billumdirect.locno = data.locno;
		}
	});
};

billumdirect.statusFormatter = function(value, rowData, rowIndex){
	return billumdirect.status[value];
};



billumdirect.converStr2JsonObj= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billumdirect.converStr2JsonObj2= function(data){
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
billumdirect.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bill_um_direct/selectImport4Direct?locno='+billumdirect.locno,
    			'pageNumber':1,
    			onLoadSuccess:function(data){
	    			$('#dataGridJG').datagrid('selectRow', 0);
	   			 }
    		});
};

//单击一行查看未定位明细
billumdirect.instockDetail = function(){
	var rowData = $("#dataGridJG").datagrid("getSelections")[0];
	if(rowData==null){
		$('#dataGridJG_detail').datagrid('clearData');
		return;
	}
	var  cellNostr = 'N';
	var queryMxURL=BasePath+'/bill_um_direct/dtl_list.json?untreadMmNo='+rowData.untreadMmNo+'&locno='+billumdirect.locno+'&cellNo='+cellNostr;
	$( "#dataGridJG_detail").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_detail").datagrid( 'load' );
};


//单击一行查看已定位明细
billumdirect.instockDirectDetail = function(){
	var rowData = $("#dataGridJG").datagrid("getSelections")[0];
	if(rowData==null){
		$('#dataGridJG_direct').datagrid('clearData');
		return;
	}
	var  cellNostr = '';
	var queryMxURL=BasePath+'/bill_um_direct/dtl_list.json?untreadMmNo='+rowData.untreadMmNo+'&locno='+billumdirect.locno+'&cellNo='+cellNostr;
	$( "#dataGridJG_direct").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_direct").datagrid( 'load' );
};


//查看发单人
billumdirect.initInstockWorker = function(rowData){
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
billumdirect.ownerFormatter = function(value, rowData, rowIndex){
	return billumdirect.ownnerData[value];
};
//未定位数量
billumdirect.notQtyFormatter = function(value, rowData, rowIndex){
	return rowData.receiptQty-rowData.checkQty;
};

//查询退仓通知单信息
billumdirect.searchArea = function(){
	
	var startTm =  $('#startTm').datebox('getValue');
	var endTm =  $('#endTm').datebox('getValue');
	if(!isStartEndDate(startTm,endTm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_direct/selectImport4Direct?locno='+billumdirect.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};


//查询验收单的数据
billumdirect.searchCheck = function(){
	
	var startTm =  $('#startCheckTm').datebox('getValue');
	var endTm =  $('#endCheckTm').datebox('getValue');
	if(!isStartEndDate(startTm,endTm)){    
		alert("验收日期开始日期不能大于结束日期");   
        return;   
    }
	var fromObjStr=convertArray($('#searchFormCheck').serializeArray());
	var queryMxURL=BasePath+'/bill_im_check/findCheckForDirect?locno='+billumdirect.locno;
    $( "#dataGridJGCheck").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
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

billumdirect.searchLocClear = function(){
	$('#brandNo').combobox("loadData",[]);
	$('#searchForm').form("clear");
};

billumdirect.searchLocClearCheck = function(){
	$('#searchFormCheck').form("clear");
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
billumdirect.instockDirect = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要定位的记录!',1);
		return;
	}
	if(checkedRows[0].status=="25"){
		alert('已预约完成的单据不能定位!',1);
		return;
	}
	$.messager.confirm("确认","你确定要定位这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	  var keyStr = [];
        	var locNo;
        	var receiptNo;
        	var ownerNo;
        	$.each(checkedRows, function(index, item){
        		keyStr.push(billumdirect.locno+"|"+item.ownerNo+"|"+item.untreadMmNo+"|"+item.receiptWorker);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_um_direct/instockDirect';
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
						 billumdirect.loadDataGrid();
		        	}else{
		        		alert(data.msg,2);
		        	}
	       		}
        	});
   	 	}
   	 
    }); 
};

//取消定位
billumdirect.cancelDirect = function(){
	var checkedRows = $("#dataGridJG_direct").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要取消的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要取消定位这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var keyStr = [];
        	var untreadMmNo;
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.rowId);
        		if(index==0){
        			untreadMmNo = item.untreadMmNo;
        		}
        	});               
            //2.绑定数据
            var url = BasePath+'/bill_um_direct/cancelDirectForAll';
        	var data={
        	    "keyStr":keyStr.join(","),
        	    "locno":billumdirect.locno,
        	    "untreadMmNo":untreadMmNo
        	};
        	//3. 取消定位
        	wms_city_common.loading("show","正在取消定位......");
        	billumdirect.ajaxRequest(url,data,function(result){
        		wms_city_common.loading();
        		if(result.result=='success'){
       			 	alert('取消成功!');
       				var  cellNostr = '';
       				var queryMxURL=BasePath+'/bill_um_direct/list.json?untreadMmNo='+untreadMmNo+'&locno='+billumdirect.locno+'&cellNo='+cellNostr;
       				$( "#dataGridJG_direct").datagrid( 'options' ).url=queryMxURL;
       			    $( "#dataGridJG_direct").datagrid( 'load' );
	       		 }else{
	       			 alert(result.msg,1);
	       		 }
        	}); 
        }  
    }); 
};



//发单
billumdirect.createInstock = function(){
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
						 $( "#dataGridJG").datagrid( 'reload' );
		        	}else{
		        		alert(data.msg,2);
		        	}
	       		}
        	});
        }  
    }); 
};



//定位-验收
billumdirect.instockDirectCheck = function(){
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
billumdirect.cancelDirectCheck = function(){
	var checkedRows = $("#dataGridJGCheck").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要取消的记录!',1);
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
        	billumdirect.ajaxRequest(url,data,function(result){
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
billumdirect.createInstockCheck = function(){
	
	var checkedRows = $("#dataGridJGCheck_direct").datagrid("getChecked");// 获取所有勾选checkbox的行
	
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
        	    "instockWorker":$("#instockWorkerCheck").combobox("getValue")
        	};
       		$.ajax({
				async : true,
				cache : false,
				type : 'POST',
				data:{
	        	    "rowStrs":rowStrs.join(","),
	        	    "locNo":locNo,
	        	    "instockWorker":$("#instockWorkerCheck").combobox("getValue")
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

billumdirect.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billumdirect.typeFormatter  = function(value, rowData, rowIndex){
	return billumdirect.typeData[value];
};


billumdirect.qualityFormatter  = function(value, rowData, rowIndex){
	return billumdirect.qualityDataObj[value];
};
