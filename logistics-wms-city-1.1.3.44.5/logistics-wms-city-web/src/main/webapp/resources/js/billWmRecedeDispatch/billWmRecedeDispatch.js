
var billWmRecedeDispatch = {};

//加载Grid数据Utils
billWmRecedeDispatch.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//主要是推荐这个函数。它将jquery系列化后的值转为name:value的形式。
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

//委托业主JSON格式
billWmRecedeDispatch.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//退厂类型JSON格式
billWmRecedeDispatch.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//状态JSON格式
billWmRecedeDispatch.converLookUpStrJsonObj= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//清除查询条件
billWmRecedeDispatch.searchClear = function(id){
	$('#'+id).form("clear");
	var pp = $('#mainTab').tabs('getSelected');   
	var index = $('#mainTab').tabs('getTabIndex',pp);//easyui tabs获取选中索引
	if(index == 0){
		$('#brandNoTab1').combobox("loadData",[]);
	}else{
		$('#brandNoTab2').combobox("loadData",[]);
	}
};

//退厂通知单查询
billWmRecedeDispatch.searchDataTab1 = function(){
	var fromObjStr=convertArray($('#searchFormTab1').serializeArray());
	var queryMxURL=BasePath+'/wmrecede/listDispatch.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billWmRecedeDispatch.locno;
	reqParam['status'] = 11;
	billWmRecedeDispatch.loadGridDataUtil('dataGridJG_tabl', queryMxURL, reqParam);
};

//退厂通知单查询
billWmRecedeDispatch.searchDataTab2 = function(){
	var fromObjStr=convertArray($('#searchFormTab2').serializeArray());
	var queryMxURL=BasePath+'/wmrecededispatch/listBillWmRecedeByPage.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billWmRecedeDispatch.locno;
	billWmRecedeDispatch.loadGridDataUtil('dataGridJG_tab2', queryMxURL, reqParam);
};

//加载退厂通知单明细
billWmRecedeDispatch.loadDetailDtl = function(rowData){
	var queryMxURL=BasePath+'/wmrecededtl/findWmRecedeDtlDispatchByPage.json';
	var reqParam = {locno:rowData.locno,ownerNo:rowData.ownerNo,recedeNo:rowData.recedeNo,recedeType:rowData.recedeType};
	billWmRecedeDispatch.loadGridDataUtil('dataGridDtl_tabl', queryMxURL, reqParam);
};

//加载退厂通知单明细
billWmRecedeDispatch.loadWmOutstockDirectDetail = function(rowData){
	var queryMxURL=BasePath+'/bill_wm_outstock_direct/dtl_list.json';
	var reqParam = {locno:rowData.locno,ownerNo:rowData.ownerNo,sourceNo:rowData.recedeNo};
	billWmRecedeDispatch.loadGridDataUtil('dataGridDtl_tab2', queryMxURL, reqParam);
};


//退厂定位调度
billWmRecedeDispatch.wmRecedeLocate = function(){
	
    //校验是否选择退厂通知单
    var checkedRows = $('#dataGridJG_tabl').datagrid('getChecked');
    if(checkedRows.length < 1){
		alert("请选择退厂通知单");
		return ;
	}
    
    //调度
    $.messager.confirm("确认","你确定要定位这"+checkedRows.length+"条数据", function (r){  
    	if(!r){
    		return;
    	}
    	wms_city_common.loading('show');
    	
    	var recedeList = [];
        var url = BasePath+'/wmrecededispatch/procBillWmRecedeLocateQuery';//路径参数
        $.each(checkedRows, function(index, item){
        	var params = {
        			locno:billWmRecedeDispatch.locno,
        			ownerNo:item.ownerNo,
        			sourceNo:item.recedeNo,
        			creator:billWmRecedeDispatch.creator
            };
        	recedeList[recedeList.length] = params;
    	});
        
        var datas={
        	datas:JSON.stringify(recedeList)
        };
        
        $.post(url, datas, function(data) {
        	wms_city_common.loading();
        	if(data.flag == "success"){
            	alert('定位成功!');
        		$('#dataGridJG_tabl').datagrid("reload");
        		//deleteAllGridCommon('dataGridDtl_tabl');
        	}else if(data.flag=='warn'){
            	alert(data.msg,1);
            }else{
            	alert('操作异常！',1);
            	
            }
        	wms_city_common.loading();
        }, "JSON").error(function() {
        	alert('调度失败,请联系管理员!',2);
        });
    	
    });
    
};


//退厂发单
billWmRecedeDispatch.sendWmOutstockDirect = function(){
	
    //校验是否选择退厂通知单
    var checkedRows = $('#dataGridDtl_tab2').datagrid('getChecked');
    if(checkedRows.length < 1){
		alert("请选择退厂拣货明细!");
		return ;
	}
    
    //调度
    $.messager.confirm("确认","你确定要发单这"+checkedRows.length+"条数据", function (r){  
    	if(!r){
    		return;
    	}
    	wms_city_common.loading('show');
    	
//    	var recedeList = [];
        var url = BasePath+'/wmrecededispatch/sendWmOutstockDirect';//路径参数
//        $.each(checkedRows, function(index, item){
//        	var params = {
//        			locno:billWmRecedeDispatch.locno,
//        			ownerNo:item.ownerNo,
//        			recedeNo:item.recedeNo,
//        			creator:billWmRecedeDispatch.creator
//            };
//        	recedeList[recedeList.length] = params;
//    	});
        
        var datas={
        	outstockName:$('#locateNoTab2').combobox('getValue'),
        	datas:JSON.stringify(checkedRows)
        };
        
        $.post(url, datas, function(data) {
        	if(data.flag == "success"){
            	alert('发单成功!');
        		$('#dataGridJG_tab2').datagrid("reload");
        		$('#dataGridDtl_tab2').datagrid("reload");
        	}else if(data.flag=='warn'){
            	alert(data.msg,1);
            }else{
            	alert('操作异常！',1);
            	
            }
        	wms_city_common.loading();
        }, "JSON").error(function() {
        	wms_city_common.loading();
        	alert('发单失败,请联系管理员!',2);
        });
    	
    });
    
};


//初始化委托业主
billWmRecedeDispatch.ownerFt = {};
billWmRecedeDispatch.initOwnerNo = function(){
	wms_city_common.comboboxLoadFilter(
			["ownerNoTab1","ownerNoTab2"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[true,true],
			BasePath+'/entrust_owner/get_biz',
			{},
			billWmRecedeDispatch.ownerFt,
			null);
};

//初始化退厂类型
billWmRecedeDispatch.recedeTypeFt = {};
billWmRecedeDispatch.initRecedeType = function(){
	wms_city_common.comboboxLoadFilter(
			["recedeTypeTab1"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECEDE_TYPE',
			{},
			billWmRecedeDispatch.recedeTypeFt,
			null);
};


//初始化状态
billWmRecedeDispatch.initStatus = function(){
	$.ajax({
		type : 'POST',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECEDE_STATUS',
		success : function(data) {
			billWmRecedeDispatch.statusFt = billWmRecedeDispatch.converLookUpStrJsonObj(data);
		}
	});
};

//初始供应商选择下拉框
billWmRecedeDispatch.initSupplier = function(){
	wms_city_common.comboboxLoadFilter(
			["supplierNoTab2"],
			"supplierNo",
			"supplierName",
			"supplierName",
			false,
			[true],
			BasePath+'/wmrecheck/querySupplier?locno='+billWmRecedeDispatch.locno,
			{},
			null,
			null);
};

//初始化拣货人
billWmRecedeDispatch.initCheckUser = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#locateNoTab2').combobox({
			     valueField:"workerNo",
			     textField:"unionName",
			     data:data,
			     panelHeight:150
			  }).combobox("select",data[0].workerNo);
		}
	});
};

//初始化用户信息
billWmRecedeDispatch.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		billWmRecedeDispatch.locno = data.locno;
		billWmRecedeDispatch.creator = data.loginName;
	}); 
};

//委托业主
billWmRecedeDispatch.ownerFormatter = function(value, rowData, rowIndex){
	return billWmRecedeDispatch.ownerFt[value];
};

//状态
billWmRecedeDispatch.statusFormatter = function(value, rowData, rowIndex){
	return billWmRecedeDispatch.statusFt[value];
};

//退厂类型
billWmRecedeDispatch.recedeTypeFormatter = function(value, rowData, rowIndex){
	return billWmRecedeDispatch.recedeTypeFt[value];
};

//========================初始化信息======================================
$(document).ready(function(){
	billWmRecedeDispatch.initCurrentUser();
	billWmRecedeDispatch.initRecedeType();
	billWmRecedeDispatch.initSupplier();
	billWmRecedeDispatch.initOwnerNo();
	billWmRecedeDispatch.initStatus();
	billWmRecedeDispatch.initCheckUser();
	
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch1'),
		"brandNoObj" : $('input[id=brandNoTab1]', $('#searchFormTab1'))
	});
	wms_city_common.loadSysNo4Cascade(objs);
	
	var objs2 = [];
	objs2.push({
		"sysNoObj" : $('#sysNoSearch2'),
		"brandNoObj" : $('input[id=brandNoTab2]', $('#searchFormTab2'))
	});
	wms_city_common.loadSysNo4Cascade(objs2);
	
	
	$('#dataGridDtl_tabl').datagrid({
		'onLoadSuccess':function(data){
			if(data.total > 0){
				if(data.footer[1].isselectsum){
					billWmRecedeDispatch.noenoughQty = data.footer[1].noenoughQty;
					billWmRecedeDispatch.recedeQty = data.footer[1].recedeQty;
					billWmRecedeDispatch.differenceQty = data.footer[1].differenceQty;
					billWmRecedeDispatch.usableQty = data.footer[1].usableQty;
				}
		   	}else{
		   		var rows = $('#dataGridDtl_tabl').datagrid('getFooterRows');
			   	rows[1]['noenoughQty'] = billWmRecedeDispatch.noenoughQty;
			   	rows[1]['recedeQty'] = billWmRecedeDispatch.recedeQty;
				rows[1]['differenceQty'] = billWmRecedeDispatch.differenceQty;
				rows[1]['usableQty'] = billWmRecedeDispatch.usableQty;
			   	$('#dataGridDtl_tabl').datagrid('reloadFooter');
		   	}
		}
	});
	
});
