var billuminstocktask = {};
billuminstocktask.locno;
billuminstocktask.quality;
$(function(){
	//初始化仓库
	billuminstocktask.initLoc();
	//初始化数据
	//billuminstocktask.loadDataGrid();
	//初始化下拉数据
	billuminstocktask.initSelect();
	
  $('#dataGridJG').datagrid({
	  onLoadSuccess:function(){
			var curObj = $('#dataGridJG');
   			var rows = curObj.datagrid("getRows");
			$.each(rows,function(index,item){
				if(item.qty>0){
					curObj.datagrid('beginEdit', index);
					var ed = curObj.datagrid('getEditor', {index:index,field:'instockQty'});
					$(ed.target).numberbox('setValue',item.qty-item.outstockQty);
				}
			});
			$('#dataGridJG').datagrid('selectRow', 0);
   		}
  });
});

billuminstocktask.initSelect = function(){
	//委托业主
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/entrust_owner/get_biz?locNo='+billuminstocktask.locno,
		success : function(data) {
			$('#search_ownerNo').combobox({
			    data:data,
			    valueField:'ownerNo',    
			    textField:'ownerName',
			    panelHeight:200
			});
		}
	});
	
	//库区
	$.ajax({
		async : true,
		cache : true,
		type : 'POST',
		data:{
			"locno":billuminstocktask.locno
		},
		dataType : "json",
		url:BasePath+'/cm_defarea/get_biz',
		success : function(data) {
			$('#search_areaNo').combobox({
			    data:data,
			    valueField:'areaNo',    
			    textField:'areaName',
			    panelHeight:200
			});
		}
	});
	
	//品牌
	$('#search_brandNo').combobox({    
		 valueField:'id',    
		 textField:'text',
		 url:BasePath+'/brand/queryBrandTree',
		 panelHeight:200
	}); 
	//品质
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
		success : function(data) {
			billuminstocktask.quality=billuminstocktask.converStr2JsonObj(data);
			$('#search_quality').combobox({
			     valueField:"itemvalue",
			     textField:"itemname",
			     data:data,
			     panelHeight:"auto",
			  });
			
		}
	});
	//发单人
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
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
//格式化
billuminstocktask.qualityFormat = function(value, rowData, rowIndex){
	return billuminstocktask.quality[value];
};

formatter:billuminstocktask.qtyFormat= function(value, rowData, rowIndex){
	return rowData.qty-rowData.outstockQty;
};

billuminstocktask.converStr2JsonObj= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billuminstocktask.initLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billuminstocktask.locno = data.locno;
		}
	});
};

//搜索
billuminstocktask.searchArea = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_instock_task/instocktasklist.json?locno='+billuminstocktask.locno;
	$( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
};

billuminstocktask.searchLocClear = function(){
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
}
billuminstocktask.loadDataGrid = function(){
	 $('#dataGridJG').datagrid('load');
//    $('#dataGridJG').datagrid({
//			'url':BasePath+'/bill_um_instock_task/list.json?locno='+billuminstocktask.locno,
//			'onLoadSuccess':function(){
//		   			var curObj = $('#dataGridJG');
//		   			var rows = curObj.datagrid("getRows");
//					$.each(rows,function(index,item){
//						if(item.qty>0){
//							curObj.datagrid('beginEdit', index);
//							var ed = curObj.datagrid('getEditor', {index:index,field:'instockQty'});
//							$(ed.target).numberbox('setValue',item.qty-item.outstockQty);
//						}
//					});
//					$('#dataGridJG').datagrid('selectRow', 0);
//		   		}
//	});
};

//定位信息
billuminstocktask.directDetail = function(rowData){
	var queryMxURL=BasePath+'/bill_um_instock_direct/list.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+
				  '&cellNo='+rowData.cellNo+'&cellId='+rowData.cellId;
	$( "#dataGridJGDirect").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGDirect").datagrid( 'load' );
};


//取消定位
billuminstocktask.cancelDirect = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	
	if(checkedRows.length < 1){
		alert('请选择要取消定位的商品!',1);
		return;
	}
	$.messager.confirm("确认","你确定要取消定位这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	var rowStr = [];
        	$.each(checkedRows, function(index, item){
        		rowStr.push(item.locno+"|"+item.ownerNo+"|"+item.cellNo+"|"+item.cellId);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_um_instock_task/cancelDirect';
        	var data={
        	    "rowStr":rowStr.join(",")
        	};
        	//3. 取消定位
        	billuminstocktask.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			alert('取消成功!');
					billuminstocktask.loadDataGrid();
        		 }else{
        			 alert('取消失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    }); 
};


//发单
billuminstocktask.createInstock = function(){
	var checkedRows = $("#dataGridJGDirect").datagrid("getChecked");// 获取所有勾选checkbox的行
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
        		rowStrs.push(item.rowIds);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bill_um_instock_task/createInstock';
        	var data={
        	    "rowStrs":rowStrs.join(","),
        	    "instockWorker":$("#instockWorker").combobox("getValue")
        	};
        	wms_city_common.loading("show","正在发单......");
        	//3. 取消定位
        	billuminstocktask.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 alert('发单成功!');
					billuminstocktask.loadDataGrid();
        		 }else{
        			 alert('发单失败,请联系管理员!',2);
        		 }
        		 wms_city_common.loading();
        	}); 
        }  
    }); 
};

billuminstocktask.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};