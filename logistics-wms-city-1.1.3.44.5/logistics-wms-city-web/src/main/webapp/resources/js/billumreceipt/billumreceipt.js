var billumreceipt = {};
//选择店退仓单
billumreceipt.openSelectUntread = function(){
	$("#untreadSearchForm").form('clear');
	$("#dataGridUntreadDtl").datagrid('loadData',[]);
	$('#openUIUnUntread').window('open');
};
billumreceipt.searchUntread = function(){
	var startCreatetm = $('#startCreatetmForUntread').datebox('getValue');
	var endCreatetm = $('#endCreatetmForUntread').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;
    }
	var fromObjStr=convertArray($('#untreadSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_untread/list.json?locno='+billumreceipt.user.locno+"&status=11";
	$("#dataGridUntreadDtl").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
	$("#dataGridUntreadDtl").datagrid( 'options' ).url=queryMxURL;
	$("#dataGridUntreadDtl").datagrid( 'load' );
};
//确认选择店退仓单
billumreceipt.selectUntread = function(){
	var checkedRows = $("#dataGridUntreadDtl").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择记录!',1);
		return;
	}
	if(checkedRows.length > 1){
		alert('只能选择一条记录!',1);
		return;
	}
	var rowData = checkedRows[0];
	$.messager.confirm("确认","您确定选择这条单据吗？", function (r){
		if(!r){
			return;
		}
		var checkUrl = BasePath+'/bill_um_receipt/checkUntread';
		var params = {
				locno:rowData.locno,
				untreadNo:rowData.untreadNo,
				ownerNo:rowData.ownerNo
		};
		$.post(checkUrl, params, function(result) {
			if(result.result=='success'){
				rowData.itemType = rowData.untreadType;
				billumreceipt.loadMain(rowData);
				billumreceipt.closeWindow('openUIUnUntread');
			}else{
		    	alert(result.result,2);
		    	return;
		    }
		}, "JSON").error(function() {
			alert('连接异常,请稍后再试!',1);
		});
	});
};
billumreceipt.selectBatchUntread = function(){
	var checkedRows = $("#dataGridUntreadDtl").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择记录!',1);
		return;
	}
	var rowData = null;
	$.messager.confirm("确认","您确定选择这"+checkedRows.length+"条单据吗？", function (r){
		if(!r){
			return;
		}
		var locnoStr = '';
		var untreadNoStr = '';
		var ownerNoStr = '';
		for(var idx=0;idx<checkedRows.length;idx++){
			rowData = checkedRows[idx];
			if(idx == (checkedRows.length-1)){
				locnoStr += rowData.locno;
				untreadNoStr += rowData.untreadNo;
				ownerNoStr += rowData.ownerNo;
			}else{
				locnoStr += rowData.locno + '|';
				untreadNoStr += rowData.untreadNo + '|';
				ownerNoStr += rowData.ownerNo + '|';
			}
		}
		var checkUrl = BasePath+'/bill_um_receipt/batchCreate';
		var params = {
				locnoStr:locnoStr,
				untreadNoStr:untreadNoStr,
				ownerNoStr:ownerNoStr
		};
		wms_city_common.loading("show","正在批量生成退仓收货单......");
		billumreceipt.ajaxRequest(checkUrl, params, false, function(result) {
			if(result.result=='success'){
				alert("批量生成成功");
				billumreceipt.closeWindow('openUI');
				billumreceipt.closeWindow('openUIUnUntread');
				billumreceipt.searchData();
			}else{
		    	alert(result.result,2);
		    }
			wms_city_common.loading();
		});
	});
};
billumreceipt.loadMain = function(rowData){
	$("#dataForm").form('load',rowData);
};
//选择箱号
billumreceipt.showAddBox = function(){
	var queryMxURL = BasePath+'/bill_um_untread_dtl/listByBox.json?locno='+billumreceipt.user.locno+'&untreadNo='+$("#untreadNo").val()+"&ownerNo="+$("#ownerNo").combobox('getValue');
	$("#dataGridUnDtl").datagrid( 'options' ).url=queryMxURL;
	$("#dataGridUnDtl").datagrid( 'load' );
	$('#openUIUn').window('open');
};




//箱号删除
billumreceipt.delBox = function(){
	var checkItems = $('#receiptDtlDg').datagrid('getChecked');
	if(checkItems.length < 1){
		alert("请勾选箱号!");
		return;
	}
	$.each(checkItems,function(index,item){
		var i = $('#receiptDtlDg').datagrid('getRowIndex',checkItems[index]);//获取某行的行号
		$('#receiptDtlDg').datagrid('deleteRow',i);
	});
};


//新增成功操作
billumreceipt.addSuccess = function(entity){
	$('#dataForm').form('load',entity);
	$("#ownerNoHidden").val(entity.ownerNo);
	$("#ownerNo").combobox('disable');
	billumreceipt.updateSuccess(entity);
	//$("#divUmReceiptDtl").show();
};


//修改成功操作
billumreceipt.updateSuccess = function(entity){
	billumreceipt.closeWindow('openUI');
};




//========================初始化信息======================================

//加载Grid数据Utils
billumreceipt.loadGridDataUtil = function(gridDataId,url,queryParams){
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

billumreceipt.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

$(document).ready(function(){
	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
	billumreceipt.initCurrentUser();
	//仓库
	billumreceipt.ajaxRequest(BasePath+'/bmdefloc/get_biz',{},false,billumreceipt.initLocno);
	//委托业主
	billumreceipt.initOwnerNo();
	//状态
	billumreceipt.initUmReceiptStatus();
	//退仓类型
	billumreceipt.initUmUntreadType();
	//品质
	billumreceipt.initQuality();
	
	//收货码头
	billumreceipt.ajaxRequest(BasePath+'/bm_defdock/get_biz?dockType=3&locno='+billumreceipt.user.locno,{},false,billumreceipt.initDock);
	//客户信息
	billumreceipt.initStore();
	
	billumreceipt.initUsers();
	
	
	wms_city_common.closeTip("openUI");
	
	//加载品牌	
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')}//查询
			);
	wms_city_common.loadSysNo4Cascade(objs);
	//加载品牌	
	var objs1 = [];
	objs1.push(
			{"sysNoObj":$('#sysNoForUntread'),"brandNoObj":$('#brandNoForUntread')}//查询
			);
	wms_city_common.loadSysNo4Cascade(objs1);
	$('#itemDetailShow').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billumreceipt.itemQty = data.footer[1].itemQty;
		   			}else{
		   				var rows = $('#itemDetailShow').datagrid('getFooterRows');
			   			rows[1]['itemQty'] = billumreceipt.itemQty;
			   			$('#itemDetailShow').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	
});
billumreceipt.onLoadSuccess = function(data){
	if(data.rows.length > 0){
		if (data.footer[1].isselectsum) {
			billumreceipt.realQtyMain = data.footer[1].realQty;
		} else {
			var rows = $('#dataGridJG').datagrid('getFooterRows');
			rows[1]['realQty'] = billumreceipt.realQtyMain;
			$('#dataGridJG').datagrid('reloadFooter');
		}
	}
}
//========================用户 信息========================
billumreceipt.user = {};
billumreceipt.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billumreceipt.userid = data.userid;
		billumreceipt.loginName = data.loginName;
		billumreceipt.currentDate = data.currentDate19Str;
		billumreceipt.locno = data.locno;
		billumreceipt.user = data;
	});
};
//========================用户 信息END========================
//========================收货码头========================
billumreceipt.dockNo = {};
billumreceipt.dockNoFormatter = function(value, rowData, rowIndex){
	return billumreceipt.dockNo[value];
};
billumreceipt.initDock = function(datas){
	
	$('#dockNo').combobox({
		valueField:"dockNo",
	    textField:"textFieldName",
	    data:datas,
	    panelHeight:200,
	    loadFilter:function(data){
	       	 if(data.length>0){
	       		 for(var i = 0;i<data.length;i++){
	       			 var tempData = data[i];
	       			 tempData.textFieldName = tempData.dockNo+'→'+tempData.dockName;
	       		 }
	       	 }
	    	return data;
	    }
	});
	
	$('#dockNoCondition').combobox({
	    valueField:'dockNo',    
	    textField:'textFieldName',
	    data:datas,
	    panelHeight:200,
	    loadFilter:function(data){
	       	 if(data.length>0){
	       		 for(var i = 0;i<data.length;i++){
	       			 var tempData = data[i];
	       			 tempData.textFieldName = tempData.dockNo+'→'+tempData.dockName;
	       		 }
	       	 }
	    	return data;
	    }
	});
	
	$('#dockNo_detail').combobox({
		    valueField:'dockNo',    
		    textField:'textFieldName',
		    data:datas,
		    panelHeight:200,
		    loadFilter:function(data){
		       	 if(data.length>0){
		       		 for(var i = 0;i<data.length;i++){
		       			 var tempData = data[i];
		       			 tempData.textFieldName = tempData.dockNo+'→'+tempData.dockName;
		       		 }
		       	 }
		    	return data;
		    }
	});
	
	billumreceipt.dockNo = billumreceipt.tansforDockNo(datas);
};
billumreceipt.tansforDockNo = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].dockNo +"\":\""+data[i].dockName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//========================收货码头END========================
//========================仓库代码========================
billumreceipt.locno = {};
billumreceipt.locnoFormatter = function(value, rowData, rowIndex){
	return billumreceipt.locno[value];
};
//加载仓库代码
billumreceipt.initLocno = function(data){
	$('#locno').combobox({
	    data:data,
	    valueField:'locno',    
	    textField:'locname',
	    panelHeight:"auto",
	});
	billumreceipt.locno = billumreceipt.tansforLocno(data);
};
billumreceipt.tansforLocno = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].locno +"\":\""+data[i].locname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};
//========================仓库代码END========================
//========================委托业主========================
billumreceipt.ownerNo = {};
billumreceipt.ownerNoFormatter = function(value, rowData, rowIndex){
	return billumreceipt.ownerNo[value];
};
billumreceipt.initOwnerNo = function(){
	wms_city_common.comboboxLoadFilter(
			["ownerNoCondition","ownerNo","ownerNoForUntread","ownerNo_detail"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[true,false, true, false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billumreceipt.ownerNo,
			null);
};
billumreceipt.tansforOwner = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//========================委托业主END========================
//========================状态========================

billumreceipt.statusFormatter = function(value, rowData, rowIndex){
	return billumreceipt.status[value];
};
//初始化状态
billumreceipt.status = {};
billumreceipt.initUmReceiptStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_UM_RECEIPT_STATUS',
			{},
			billumreceipt.status,
			null);
};
billumreceipt.untreadType = {};
billumreceipt.untreadTypeFormatter = function(value, rowData, rowIndex){
	return billumreceipt.untreadType[value];
};
//初始化退仓类型
billumreceipt.initUmUntreadType = function(){
	wms_city_common.comboboxLoadFilter(
			["itemType_search","itemType","itemType_detail"],
			null,
			null,
			null,
			true,
			[true, false, false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
			{},
			billumreceipt.untreadType,
			null);
};
billumreceipt.quality = {};
billumreceipt.qualityFormatter = function(value, rowData, rowIndex){
	return billumreceipt.quality[value];
};
//初始化退仓类型
billumreceipt.initQuality = function(){
	wms_city_common.comboboxLoadFilter(
			["quality","quality_detail","quality_search"],
			null,
			null,
			null,
			true,
			[false, false, true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			billumreceipt.quality,
			null);
};

billumreceipt.initUsers = function(){
	wms_city_common.comboboxLoadFilter(
			["creatorCondition","auditorCondition"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true,true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
};
//========================状态END========================
//========================客户信息========================
billumreceipt.initStore = function(){
	$('#storeNoCondition').combogrid({
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
		          loadFilter:function(data){
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
//===========================客户信息END========================
//==============================================================
//========================新增、修改页面========================
//打开窗口
billumreceipt.openWindow = function(windowId,opt){
	billumreceipt.closeWindow(windowId);
	$('#'+windowId).window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#'+windowId).window('open');
};

//关闭窗口
billumreceipt.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};
//新增窗口
billumreceipt.addUI = function(){
	$('#dataForm').form('clear');
	billumreceipt.showHideBtn("add");
	$('#receiptDtlDg').datagrid('loadData', { total: 0, rows: [] });
	$("#divUmReceiptDtl").hide();
	billumreceipt.disableForm();
	billumreceipt.openWindow('openUI', '新增');
	$("#select_untread_but").linkbutton('enable');
};
billumreceipt.disableForm = function(){
	$("#ownerNo").combobox('disable');
	$("#itemType").combobox('disable');
	$("#quality").combobox('disable');
	$("#storeNo").combobox('disable');
};
//修改明细窗口
billumreceipt.loadDetail = function(rowData){
	$("#select_untread_but").linkbutton('disable');
	$('#dataForm').form('load',rowData);
	billumreceipt.showHideBtn("edit");
	$('#receiptDtlDg').datagrid('loadData', { total: 0, rows: [] });
	$("#divUmReceiptDtl").show();
	billumreceipt.openWindow('openUI', '修改/查看');
	var tempObj = $('#receiptDtlDg');
    tempObj.datagrid( 'options' ).url = BasePath+'/bill_um_receipt_dtl/listByBox.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&receiptNo="+rowData.receiptNo;
    tempObj.datagrid('load');
};

//修改退仓收货单
billumreceipt.toUpdate = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}
	if(checkedRows.length > 1){
		alert('只能选择一条记录!',1);
		return;
	}
	$.each(checkedRows,function(index,item){
		if(item.status != '10'){
			alert("只能修改状态为新建的收货单!",1);
			return;
		}
		billumreceipt.loadDetail(item);
	});
};

//隐藏显示按钮
billumreceipt.showHideBtn = function(type){
	if(type=="add") {
		$("#ownerNo").combobox('enable');
		$("#recivedate").datebox('enable');
		$("#dockNo").combobox('enable');
		$("#receiptWorker").attr("disabled",false);
		$("#carPlate").attr("disabled",false);
		$("#shipDriver").attr("disabled",false);
		$("#remark").attr("disabled",false);
		$("#action").val("add");
		//替换保存按钮
		$('#save_main .l-btn-left').html('<span class="l-btn-text icon-save l-btn-icon-left l-btn-focus">保存</span>');
		//$("#save_main").show();
	} else if(type=="info"){
		$("#ownerNo_detail").combobox('disable');
		$("#recivedate_detail").datebox('disable');
		$("#dockNo_detail").combobox('disable');
		$("#receiptWorker_detail").attr("disabled",true);
		$("#carPlate_detail").attr('disabled',true);
		$("#shipDriver_detail").attr('disabled',true);
		$("#remark_detail").attr('disabled',true);
	} else {
		$("#ownerNo").combobox('disable');
		$("#recivedate").datebox('disable');
		$("#dockNo").combobox('disable');
		$("#receiptWorker").attr("disabled",true);
		$("#carPlate").attr("disabled",true);
		$("#shipDriver").attr("disabled",true);
		$("#remark").attr("disabled",false);
		$("#action").val("update");
		//替换成修改按钮
		$('#save_main .l-btn-left').html('<span class="l-btn-text icon-edit l-btn-icon-left l-btn-focus">修改</span>');
		//$("#save_main").hide();
	}
};
//========================新增、修改页面END========================
//========================保存主表======================================
//保存主表
billumreceipt.saveMainInfo = function(){
	if(!$("#dataForm").form('validate')){
		alert('数据验证没有通过!',1);
		return;
	}
	
	var action = $("#action").val();
	if(action == "add"){
		wms_city_common.loading("show","正在保存......");
		var url = BasePath+'/bill_um_receipt/saveMainInfo';
	    $('#dataForm').form('submit', {
				url: url,
				onSubmit: function(param){
					param.creator = billumreceipt.user.loginName;
					param.locno = billumreceipt.user.locno;
					param.status = '10';
				},
				success: function(r){
					r = billumreceipt.parseJsonStringToJsonObj(r);
					if(r && r.result == 'success'){
						$("#dataForm input[id=receiptNo]").val(r.receiptNo);
						 alert('保存成功!');
						 billumreceipt.showHideBtn("edit");
						 $('#receiptDtlDg').datagrid('loadData', { total: 0, rows: [] });
						 $("#divUmReceiptDtl").show();
						 $("#select_untread_but").linkbutton('disable');
					}else if(r.flag=="notEXits"){
						alert(r.result,2);
					}else{
						 alert(r.result,2);
					}
					wms_city_common.loading();
			    },
				error:function(){
					alert('保存失败,请联系管理员!',2);
					wms_city_common.loading();
				}
		});
	}else{
		wms_city_common.loading("show","正在保存......");
		var url = BasePath+'/bill_um_receipt/modifyReceipt';
		var params = {
				editor:billumreceipt.user.loginName,
				locno:billumreceipt.user.locno,
				receiptNo:$("#receiptNo").val(),
				ownerNo:$("#ownerNo").combobox('getValue'),
				remark:$("#remark").val()
		};
		$.post(url, params, function(result) {
	        if(result.result=='success'){
	            alert('修改成功!',1);
	        }else if(result.result=='notExist'){
	        	alert(result.result,2);
	        }
	        else{
	        	alert(result.result,2);
	        }
	        wms_city_common.loading();
	    }, "JSON").error(function() {
	    	alert('保存失败!',1);
	    	wms_city_common.loading();
	    });
	}
};
billumreceipt.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
//========================保存主表END======================================
//========================退仓通知单信息======================================
//查询退仓通知单数据
billumreceipt.searchUn = function(){
	var ownerNo = $("#ownerNo").combobox('getValue');
	var fromObjStr=convertArray($('#unSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_untread/findBillUmUntreadToReceiptByPage.json?locno='+billumreceipt.user.locno+"&ownerNo="+ownerNo;
	$("#dataGridUn").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
	$("#dataGridUn").datagrid( 'options' ).url=queryMxURL;
	$("#dataGridUn").datagrid( 'load' );
};
billumreceipt.searchClear = function(opt){
	$("#" + opt).form('clear');
	$('#brandNo').combobox("loadData",[]);
};
//加载退仓通知单详情
billumreceipt.loadUnDetailDtl = function(rowData){
	var queryMxURL=BasePath+'/bill_um_untread/findBillUmUntreadBoxNumPage.json';
	var queryParams = {locno:billumreceipt.user.locno,untreadNo:rowData.untreadNo,ownerNo:rowData.ownerNo};
	billumreceipt.loadGridDataUtil('dataGridUnDtl', queryMxURL, queryParams);
};
//箱号选择
billumreceipt.selectBox = function(){
	
	var checkItems = $('#dataGridUnDtl').datagrid('getChecked');
	if(checkItems.length < 1){
		alert("请勾选箱号!");
		return;
	}
	$.each(checkItems,function(index,item){
		var reqParas = {boxNo:item.boxNo,untreadNo:item.untreadNo,brandName:item.brandName,sumQty:item.sumQty};
		$('#receiptDtlDg').datagrid('appendRow',reqParas);
	});
	billumreceipt.closeWindow('openUIUn');
};
//========================退仓通知单信息END======================================
//========================保存明细======================================
//保存退仓单明细
billumreceipt.doSave = function(){
	var allRows = $('#receiptDtlDg').datagrid('getRows');
	var len = allRows.length;
	//验证重复
	var checkMap = {};
	var temp = {};
	var boxStr = '';
	for(var i=0;i<len;i++){
		temp = allRows[i];
		if(checkMap[temp.boxNo]){
			alert("【"+temp.boxNo+"】存在重复,请删除后保存!");
			return;
		}else{
			checkMap[temp.boxNo] = true;
			boxStr += temp.boxNo;
			if(i != (len-1)){
				boxStr += "|";
			}
		}
	}
	wms_city_common.loading("show","正在保存明细......");
    var url = BasePath+'/bill_um_receipt_dtl/saveDtl';
    var params = {
    		boxStr:boxStr,
    		locno:billumreceipt.user.locno,
    		receiptNo:$("#receiptNo").val(),
    		untreadNo:$("#untreadNo").val(),
    		ownerNo:$("#ownerNo").combobox('getValue'),
    		editor:billumreceipt.user.loginName
    };
    $.post(url, params, function(result) {
        if(result.result=='success'){
            alert('保存成功!',1);
            $('#openUI').window('close');  
            billumreceipt.searchData();
        }else if(result.result=='notExist'){
        	alert(result.result,2);
        }
        else{
        	alert(result.result+" 保存失败!",2);
        }
        wms_city_common.loading();
    }, "JSON").error(function() {
    	alert('保存失败!',1);
    	wms_city_common.loading();
    });
};
billumreceipt.clearAll = function(){
	$('#dataForm').form("clear");
	$('#receiptDtlDg').datagrid('loadData',[]);	
};
//========================保存明细END======================================
//========================查询主表、明细======================================
//查询退仓单数据
billumreceipt.searchData = function(){
	var startCreatetm =  $('#startCreatetmCondition').datebox('getValue');
	var endCreatetm =  $('#endCreatetmCondition').datebox('getValue');
	var audittmStart =  $('#startAudittmCondition').datebox('getValue');
	var audittmEnd =  $('#endAudittmCondition').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }   
	if(!isStartEndDate(audittmStart,audittmEnd)){    
		alert("审核日期开始日期不能大于结束日期");   
		return;   
	}   
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_receipt/list.json?locno='+billumreceipt.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billumreceipt.locno;
	billumreceipt.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};
//明细查询
billumreceipt.viewDetail = function(rowData){
	$('#openUIShow').window('close');
	$('#openUIShow').window('open');
	try{
		$('#dataFormShow').form('load',rowData);
	}catch(e){
	}
	var tempObj = $('#itemDetailShow');
	tempObj.datagrid( 'options' ).url = BasePath+
		'/bill_um_receipt_dtl/dtl_list.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&receiptNo="+rowData.receiptNo;
	tempObj.datagrid( 'load' );
	billumreceipt.showHideBtn("info");
};
//========================查询主表、明细END======================================
//========================删除、审核======================================
//删除
billumreceipt.doDel = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var temp = {};
	var deleted = '';
	for(var i=0;i<checkedRows.length;i++){
		temp = checkedRows[i];
		if(temp.status != '10'){
			alert("只能删除状态为【建单】的单据,【"+temp.receiptNo+"】不能删除!");
			return;
		}
		deleted += temp.locno+"_"+temp.receiptNo+"_"+temp.ownerNo;
		if(i!=(checkedRows.length-1)){
			deleted += "|";
		}
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){ 
		if(r){
			//提交
			wms_city_common.loading("show","正在删除......");
			var url = BasePath+ '/bill_um_receipt/delBillUmReceipt';
			var effectRow = {deleted:deleted};
			ajaxRequest(url,effectRow,function(result){
				 wms_city_common.loading();
	    		 if(result.result=='success'){
	    			 //4.删除成功,清空表单
	    			 billumreceipt.searchData();
	    			 alert('删除成功!');
	    		 }else if(result.flag=='error'){
	    			 alert(result.msg,2);
	    		 }
	    		 else{
	    			 alert('删除失败,请联系管理员!',2);
	    		 }
			});
		}
		 
    });  
};
//审核
billumreceipt.check = function(){
	// 获取勾选的行
	var checkRows = $("#dataGridJG").datagrid("getChecked");
	if(checkRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}else{
		var temp;
		var keys = "";
		for(var idx=0;idx<checkRows.length;idx++){
			temp = checkRows[idx];
			if(temp.status != '10'){
				alert("请选择新建状态的单进行审核！");
				return;
			}
			if(idx != checkRows.length-1){
				keys += temp.receiptNo+"_"+temp.ownerNo+"_"+temp.untreadNo+"|";
			}else{
				keys += temp.receiptNo+"_"+temp.ownerNo+"_"+temp.untreadNo;
			}
			var receiptNo = temp.receiptNo;
			var ownerNo = temp.ownerNo;
			var untreadDtlUrl = BasePath
				+'/bill_um_receipt_dtl/list.json?locno='+billumreceipt.user.locno
				+"&ownerNo="+ownerNo+"&receiptNo="+receiptNo;
			var existDtl = false;
			billumreceipt.ajaxRequest(untreadDtlUrl,{},false,function(result){
				 if(result.total>0){
					 existDtl = true;
				 }
			});
			if(!existDtl){
				alert("收货单["+receiptNo+"]不存在明细,不允许审核!",2);
				return;
			}
		}
		
		$.messager.confirm("确认","您确定要审核这"+checkRows.length+"张退仓收货单吗？", function (r){
	        if (r) {
	        	var checkUrl = BasePath+'/bill_um_receipt/check';
	        	var params = {
	        			locno:billumreceipt.user.locno,
	        			keys:keys,
	        			auditor:billumreceipt.user.loginName
	        	};
	        	wms_city_common.loading("show","正在审核......");
	        	$.post(checkUrl, params, function(result) {
	        		wms_city_common.loading();
	    			if(result.result=='success'){
	    				 billumreceipt.searchData();
						 alert('审核成功!');
	    			}else{
	    		    	alert(result.result,2);
	    		    	return;
	    		    }
	    		}, "JSON").error(function() {
	    			alert('审核失败,请联系管理员!',2);
	    		});
			}

		});
	}
};
//========================删除、审核END======================================
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
