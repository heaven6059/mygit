
var billchplan = {};
billchplan.user;
billchplan.boxDatagridData;
billchplan.planColumnsCell = [    
    				          {field:'cellNo',title:'储位',width:100},    
    				          {field:'stockNo',title:'通道',width:100},    
    				          {field:'areaNo',title:'库区',width:100},    
    				          {field:'areaName',title:'库区名称',width:100},    
    				          {field:'wareNo',title:'仓区',width:100},    
    				          {field:'wareName',title:'仓区名称',width:100}    
    				      ];
billchplan.planColumnsItem = [    
				          {field:'itemNo',title:'商品编码',width:100},    
				          {field:'itemName',title:'商品名称',width:100},    
				          {field:'colorName',title:'颜色',width:100},    
				          {field:'sizeNo',title:'尺码',width:100},    
				          {field:'styleNo',title:'款号',width:100},    
				          {field:'brandName',title:'品牌',width:100}    
				      ];

//加载Grid数据Utils
billchplan.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

billchplan.addrow = function(){
	billchplan.openSelectWin();
};

billchplan.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);	
	$.each(rowData, function(index, item){
		tempObj.datagrid('appendRow', item);
	});
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('selectRow',tempIndex);
	
};
billchplan.showBtn = function(type) {
	$("#option").val("other");
	if(type == 'add') {
		$("#option").val("add");
		$("#saveBtn").show();
		$("#info_save").hide();
	} else if(type == 'edit') {
		$("#saveBtn").hide();
		$("#info_save").show();
	} else {
		$("#saveBtn").hide();
		$("#info_save").hide();
	}
};
//创建分货单
billchplan.showAddDialog = function(){
	billchplan.enableCombox();
	$("#divideMainInfoForm").form("clear");
	var divideNoInput = $("#divideNo");
	divideNoInput.attr("readonly",false);
	divideNoInput.find("+ input+ input").attr("disabled",false);
	//$("#saveBtn").attr("disabled",false);
	billchplan.showBtn('add');
	//$("#checkBoxBtn").attr("disabled",false);
	$("#checkBoxBtn").show();
	billchplan.hideDtlToolsBar(true);
	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text").attr("readOnly",false);
	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#divideMainInfoForm input[id=storeNo]+ span").css("background-color","white");
	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text").css("background-color","white");
	$('#order_dtl2_dataGrid').datagrid('loadData',[]);
	$("#showDetailDialog").window('open');
	billchplan.dtlGridClear();
	$('#cell_div').hide();
	$('#item_div').hide();
	$("#limitBrandFlagTitle").hide();
	$("#limitBrandFlagValue").hide();
	$("#brandNoTitle").hide();
	$("#sysNoTitle").hide();
	$("#brandNoValue").hide();
	$("#sysNoValue").hide();
	billchplan.showItemTypeAndQuality(false);
	billchplan.clearDiff();
	wms_city_common.resetQuality("quality");
	billchplan.planNoInit = "";
	billchplan.ownerNoInit = "";
	billchplan.planTypeInit = "";
	billchplan.limitBrandFlagInit = "";
};
billchplan.dtlGridClear = function(){
	$('#dtl_cell_dataGrid').datagrid('loadData',[]);
	$('#dtl_item_dataGrid').datagrid('loadData',[]);
};
//关闭
billchplan.closeWindow = function(id){
	$('#'+id).window('close');  
};
billchplan.setDivideNo = function(rowData){
	billchplan.planNoInit = rowData.planNo;
	billchplan.ownerNoInit = rowData.ownerNo;
	billchplan.planTypeInit = rowData.planType;
	billchplan.limitBrandFlagInit = rowData.limitBrandFlag;
	$("#planNo").val(rowData.planNo);
	$("#planRemark").val(rowData.planRemark);
	$("#planDate").datebox('setValue', rowData.planDate);
	$("#planType").combobox('setValue',rowData.planType);
	billchplan.planTypeChange();
	var limitBrandFlag = rowData.limitBrandFlag;
	$("#limitBrandFlag").combobox('setValue',limitBrandFlag);
	$("#sysNoForItem").combobox('select',rowData.sysNo);
	
	billchplan.limitBrandFlagChange();
	if(rowData.planType == "0"){
		if(limitBrandFlag == "1"){
			//$("#brandNoForItem").combobox('setValue',rowData.brandNo);
			//billchplan.setBrandValues(rowData);
		}
		billchplan.showItemTypeAndQuality(true);
		$("#itemType").combobox('setValue',rowData.itemType);
		$("#quality").combobox('setValue',rowData.quality);
	}else{
		//billchplan.setBrandValues(rowData);
		//$('#brandNoForItem').combobox('setValue',arrayBrand);
		billchplan.showItemTypeAndQuality(false);
	}
	billchplan.disableCombox();
};
billchplan.showItemTypeAndQuality = function(is){
	$("#quality").combobox('setValue','');
	$("#itemType").combobox('setValue','');
	if(is){
		$("#qualityTitle").show();
		$("#qualityValue").show();
		$("#itemTypeTitle").show();
		$("#itemTypeValue").show();
	}else{
		$("#qualityTitle").hide();
		$("#qualityValue").hide();
		$("#itemTypeTitle").hide();
		$("#itemTypeValue").hide();
	}
};
billchplan.editInfo = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		var data = checkedRows[0];
		if(data.status != '10'){
			alert("只能修改新建状态的单据!",1);
			return;
		}
		if(data.limitBrandFlag == "1"){
			alert("此单已限定品牌,不能修改!",1);
			return;
		}
		billchplan.loadDetail(data,"edit");
	}
};
billchplan.check = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	var len = checkedRows.length;
	var planNos = "";
	if(len < 1){
		alert('请选择要审核的记录!',1);
		return;
	}else{
		var temp = {};
		for(var i=0;i<len;i++){
			temp = checkedRows[i];
			if(temp.status != '10'){
				alert("单据["+temp.planNo+"]不能审核!<br>只能审核新建状态的单据!",1);
				return;
			}
			planNos += ","+temp.planNo;
		}
		$.messager.confirm("确认","您确定要审核这"+len+"条单据吗？", function (r){
			if(!r){
				return;
			}
			var param = {
					locno:billchplan.user.locno,
					planNos:planNos.substring(1),
//					audittm:billchplan.user.currentDate19Str,
					auditor:billchplan.user.loginName,
					auditorName:billchplan.user.username
			};
			var url = BasePath+'/bill_ch_plan/check';
			wms_city_common.loading("show", "正在审核盘点计划单......");
			$.post(url, param, function(result) {
				if(result.result == 'success'){
					alert('审核成功!');
					$("#mainDataGrid").datagrid('load');
				}else if(result.result == 'fail'){
					alert('审核失败!',2);
				}else{
					alert(result.result,2);
				}
				wms_city_common.loading();
			}, "JSON").error(function() {
		    	alert('审核失败!',1);
		    	wms_city_common.loading();
		    });
		});
	}
};
//作废
billchplan.invalid = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	var len = checkedRows.length;
	var planNos = "";
	if(len < 1){
		alert('请选择要作废的记录!',1);
		return;
	}else{
		var temp = {};
		for(var i=0;i<len;i++){
			temp = checkedRows[i];
			if(temp.status != '11'&&temp.status != '15'&&temp.status != '20'&&temp.status != '25'){
				alert("单据["+temp.planNo+"]不能作废!<br>只能作废审核、发起、已发单、初盘/复盘状态的单据!",1);
				return;
			}
			planNos += ","+temp.planNo;
		}
		$.messager.confirm("确认","相关盘点单会置为关闭，是否确定作废？", function (r){
			if(!r){
				return;
			}
			var param = {
					locno:billchplan.user.locno,
					planNos:planNos.substring(1),
					auditor:billchplan.user.loginName,
					auditorName:billchplan.user.username
			};
			var url = BasePath+'/bill_ch_plan/invalid';
			wms_city_common.loading("show", "正在废弃盘点计划单......");
			$.post(url, param, function(result) {
				if(result.result == 'success'){
					alert('作废成功!');
					$("#mainDataGrid").datagrid('load');
				}else if(result.result == 'fail'){
					alert('作废失败!',2);
				}else{
					alert(result.result,2);
				}
				wms_city_common.loading();
			}, "JSON").error(function() {
		    	alert('作废失败!',1);
		    	wms_city_common.loading();
		    });
		});
	}
};
//删除记录
billchplan.deletePlan = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	var len = checkedRows.length;
	var planNos = "";
	if(len < 1){
		alert('请选择要删除的记录!',1);
		return;
	}else{
		var temp = {};
		for(var i=0;i<len;i++){
			temp = checkedRows[i];
			if(temp.status != '10'){
				alert("单据["+temp.planNo+"]不能删除!<br>只能删除新建状态的单据!",1);
				return;
			}
			planNos += ","+temp.planNo;
		}
		$.messager.confirm("确认","您确定要删除这"+len+"条单据吗？", function (r){
			if(!r){
				return;
			}
			var param = {
					locno:billchplan.user.locno,
					planNos:planNos.substring(1),
					audittm:billchplan.user.currentDate19Str,
					auditor:billchplan.user.loginName
			};
			var url = BasePath+'/bill_ch_plan/deleteMain';
			wms_city_common.loading("show", "正在删除盘点计划单......");
			$.post(url, param, function(result) {
				if(result.result == 'success'){
					alert('删除成功!');
					$("#mainDataGrid").datagrid('load');
				}else if(result.result == 'fail'){
					alert('删除失败!',2);
				}else{
					alert(result.result,2);
				}
				wms_city_common.loading();
			}, "JSON").error(function() {
				alert('删除失败!',1);
				wms_city_common.loading();
			});
		});
	}
};
//加载差异
billchplan.loadDiff = function(rowData){
	
	billchplan.clearDiff();
	var status = rowData.status;
	if(!status || status == null || status == ''){
		
	}else{
		var planNo = rowData.planNo;
		$("#planNoDiff").val(planNo);
		$("#differentNoDiff").val("未产生差异单");
		$("#planTypeDiff").combobox('setValue',rowData.planType);
		if(status == '90'){
			$("#beginDateDiff").datebox('setValue',rowData.beginDate);
			$("#endDateDiff").datebox('setValue',rowData.endDate);
			$("#endDateDiff").datebox('setValue',rowData.endDate);
			$("#planRemarkDiff").val(rowData.planRemark);
			var diffMainUrl = BasePath + '/bill_ch_different/get_biz';
			var diffMainParams = {
					locno:billchplan.user.locno,
					planNo:planNo
				};
			var diffMain = [];
			var differentNo;
			billchplan.ajaxRequest(diffMainUrl,diffMainParams,false,function(result){diffMain = result;});
			if(diffMain != null && diffMain.length > 0){
				differentNo = diffMain[0].differentNo;
				$("#differentNoDiff").val(differentNo);
				var diffDtlUrl = BasePath + '/bill_ch_different_dtl/dtl_list.json';
				var diffDtlParams = {
						locno:billchplan.user.locno,
						differentNo:differentNo,
						planType:rowData.planType
					};
				billchplan.loadGridDataUtil('dtl_diff_dataGrid', diffDtlUrl, diffDtlParams);
			}
		}
	}
};
//清空差异
billchplan.clearDiff = function(){
	$("#diffMainForm").form('clear');
	$("#dtl_diff_dataGrid").datagrid('loadData',[]);
};
//加载订单详情
billchplan.loadDetail = function(rowData,type){
	$("#divideMainInfoForm").form("clear");
	if(type=="edit"){
		//$("#checkBoxBtn").attr("disabled",false);
		$("#checkBoxBtn").show();
		//$("#saveBtn").attr("disabled",true);
		billchplan.showBtn('edit');
		billchplan.hideDtlToolsBar(false);
		$("#order_dtl1_dataGrid").datagrid('loadData',[]);
		
	}else{
		//$("#checkBoxBtn").attr("disabled",true);
		$("#checkBoxBtn").hide();
		//$("#saveBtn").attr("disabled",true);
		billchplan.showBtn('view');
		billchplan.hideDtlToolsBar(true);
	}
	billchplan.setDivideNo(rowData);
	billchplan.loadDiff(rowData);
	billchplan.disableCombox();
	$("#showDetailDialog").window('open');
	var queryMxURL=BasePath+ '/bill_ch_plan_dtl/list.json';
	var queryParams={planNo:rowData.planNo,locno:rowData.locno,planType:rowData.planType};
	billchplan.loadGridDataUtil(billchplan.getDtlGirdId(), queryMxURL, queryParams);
};
billchplan.clearSearch = function(){
	$("#boxSearchForm").form('clear');
	$("#order_dtl2_dataGrid").datagrid('loadData',billchplan.boxDatagridData);
	
};
billchplan.disableCombox = function(){
	var planType = $("#planType").combobox('getValue');
	var limitBrandFlag = $("#limitBrandFlag").combobox('getValue');
	$("#planType").combobox('disable');
	$("#limitBrandFlag").combobox('disable');
//	if(planType=='1'&&limitBrandFlag=='0'){
//		$("#limitBrandFlag").combobox('disable');
//	}else{
//		$("#limitBrandFlag").combobox('disable');
//	}
	$("#sysNoForItem").combobox('disable');
	//如果按储位盘并且是抽盘可以修改品牌
	//$("#brandNoForItem").combobox('disable');
	if(planType=='1'&&limitBrandFlag=='0'){
		
	}
	$("#quality").combobox('disable');
	$("#itemType").combobox('disable');
};
billchplan.enableCombox = function(){
	$("#planType").combobox('enable');
	$("#limitBrandFlag").combobox('enable');
	$("#sysNoForItem").combobox('enable');
	$("#brandNoForItem").combobox('enable');
	$("#quality").combobox('enable');
	$("#itemType").combobox('enable');
};


billchplan.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
//========================保存主表======================================
billchplan.editMain = function(){
	var fromObj=$('#divideMainInfoForm');
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
		alert('数据验证没有通过!',1);
		return ;
	}
	var planDate =  $('#planDate').datebox('getValue');
	//时间控件非空控制未生效
	if(planDate == ""){
		alert("计划日期不能为空!");   
        return; 
	}
//	if(!isStartEndDate(wms_city_common.getCurDate(),planDate)){ 
//		alert("出库日期不能小于当前日期");   
//        return;   
//    }
	if(!planDateCheck(wms_city_common.getCurDate(),planDate,7,7)){
		alert("盘点日期只能在当天前、后一周内的日期!"); 
		return;
	}
	var url = BasePath+'/bill_ch_plan/editMainInfo';
	wms_city_common.loading('show',"保存中，请稍后......");
	fromObj.form('submit', {
			url: url,
			onSubmit: function(param){
				param.locno = billchplan.user.locno;
				param.editor = billchplan.user.loginName;
				param.editorName = billchplan.user.username;
			},
			success: function(r){
				wms_city_common.loading();
				r = billchplan.parseJsonStringToJsonObj(r);
				if(r && r.success == 'true'){
					 alert('保存成功!');
					 billchplan.searchData();
					 return;
				} else if(r.success == 'false'){
					alert(r.errorMsg);
				}else{
					 alert('保存失败!');
				}
		    },
			error:function(){
				alert('保存失败,请联系管理员!',2);
			}
	   });
};

billchplan.saveMainInfo = function(){
		var fromObj=$('#divideMainInfoForm');
		var validateForm= fromObj.form('validate');
		if(validateForm==false){
			return ;
		}
		var planDate =  $('#planDate').datebox('getValue');
		//时间控件非空控制未生效
		if(planDate == ""){
			alert("计划日期不能为空!");   
	        return; 
		}
		var planType = $("#planType").combobox('getValue');
		var limitBrandFlag = $("#limitBrandFlag").combobox('getValue');
		var brandNo = $("#brandNoForItem").combobox('getValue');
//		if(planType == "0"){
			if(limitBrandFlag == "0" || limitBrandFlag == "1"){
				if(limitBrandFlag == "1"){
//					if(brandNo == null || brandNo == ""){
//						alert("请选品牌!");
//						return;
//					}
				}
			}else{
				alert("请选择盘点方式!");
				return;
			}
//		}
		//if(!isStartEndDate(wms_city_common.getCurDate(),planDate)){    
			//alert("计划日期不能早于当前日期!");   
	       // return;   
	    //}
		if(!planDateCheck(wms_city_common.getCurDate(),planDate,7,7)){
			alert("盘点日期只能在当天前、后一周内的日期!"); 
			return;
		}
		wms_city_common.loading("show","正在生成盘点计划单......");
        var url = BasePath+'/bill_ch_plan/saveMainInfo';
        $('#divideMainInfoForm').form('submit', {
				url: url,
				onSubmit: function(param){
//					param.createtm = billchplan.user.currentDate19Str;
					param.creator = billchplan.user.loginName;
					param.creatorName = billchplan.user.username;
					param.editor = billchplan.user.loginName;
				    param.editorName = billchplan.user.username;
//					param.edittm = billchplan.user.currentDate19Str;
					param.locno = billchplan.user.locno;
				},
				success: function(r){
					r = billchplan.parseJsonStringToJsonObj(r);
					if(r && r.success){
						billchplan.disableCombox();
						$("#divideMainInfoForm input[name=planNo]").val(r.planNo);
						$("#saveBtn").hide();
						billchplan.hideDtlToolsBar(false);
						if((planType == "0" && limitBrandFlag == "1")||(planType == "1" && limitBrandFlag == "1")){
							billchplan.closeWindow('showDetailDialog');
							$("#mainDataGrid").datagrid('load');
						}
						 alert('保存成功!');
					}else{
						 alert(r.errorMsg,2);
					}
					wms_city_common.loading();
			    },
				error:function(){
					alert('保存失败,请联系管理员!',2);
					wms_city_common.loading();
				}
		 	}
        );
};
//========================保存主表END======================================
billchplan.hideDtlToolsBar = function(yes){
	if(yes){
		$("#toolbarDlt_cell").hide();
		$("#toolbarDlt_item").hide();
	}else{
		$("#toolbarDlt_cell").show();
		$("#toolbarDlt_item").show();
	}
};
billchplan.getDtlGirdId = function(){
	var type = $("#planType").combobox('getValue');
	var id = "";
	if(type){
		if(parseInt(type)==1){
			id = 'dtl_cell_dataGrid';
		}else{
			id = 'dtl_item_dataGrid';
		}
	}
	return id;
};
billchplan.saveDtlInfo = function(){
	var tempObj = $("#"+billchplan.getDtlGirdId());	
	var inserted = tempObj.datagrid('getChanges', "inserted");
	var deleted = tempObj.datagrid('getChanges', "deleted");
	var url = BasePath+'/bill_ch_plan_dtl/save_dtl';
	var praam = {
			planType:$("#planType").combobox('getValue'),
			locno:billchplan.user.locno,
			planNo:$("#planNo").val(),
			inserted:JSON.stringify(inserted),
			deleted:JSON.stringify(deleted)
	};
	wms_city_common.loading("show","正在保存盘点计划明细......");
	$.post(url, praam, function(result) {
		if(result.result == 'success'){
			alert('保存成功!');
			tempObj.datagrid("acceptChanges");
			billchplan.closeWindow('showDetailDialog');
			$("#mainDataGrid").datagrid('load');
		}else if(result.result == 'fail'){
			alert('保存失败!',1);
		}else{
			alert(result.result,2);
		}
		wms_city_common.loading();
	}, "JSON").error(function() {
    	alert('保存失败!',1);
    	wms_city_common.loading();
    });
};
billchplan.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};
billchplan.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billchplan.planType = {};
billchplan.initPlanType = function(data){
	/*$('#planTypeCondition').combobox({
		data:data,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"auto"
	});
	$('#planType').combobox({
		data:data,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"auto"
	});
	$('#planTypeDiff').combobox({
		data:data,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"auto"
	});*/
	/*var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billchplan.planType = temp;*/
};
billchplan.limitBrandFlag = {};
billchplan.initLimitBrandFlag = function(data){
	$('#limitBrandFlag').combobox({
		data:data,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"auto",
		onChange:function(){
			billchplan.limitBrandFlagChange();
		}
	});
	$('#limitBrandFlagCondition').combobox({
		data:data,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"auto"
	});
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billchplan.limitBrandFlag = temp;
};
billchplan.limitBrandFlagFormatter = function(value, rowData, rowIndex){
	return billchplan.limitBrandFlag[value];
};
billchplan.initQuality = function(data){
	$('#quality').combobox({
		data:data,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"auto",
		onChange:function(){
			billchplan.limitBrandFlagChange();
		}
	});
};
billchplan.initItemType = function(data){
	$('#itemType').combobox({
		data:data,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:150,
		onChange:function(){
			billchplan.limitBrandFlagChange();
		}
	});
};
billchplan.limitBrandFlagChange = function(){
	var planType = $('#planType').combobox('getValue');
	var limitBrandFlag = $("#limitBrandFlag").combobox('getValue');
	if(limitBrandFlag == "1"){
		if((planType == '1')||(planType=='0'&&limitBrandFlag=='1')||$("#option").val()=="add"){
			$("#sysNoTitle").show();
			$("#sysNoValue").show();
		}else{
			$("#sysNoForItem").combobox('setValue','');
			$("#sysNoTitle").hide();
			$("#sysNoValue").hide();
		}
		
//		if($("#option").val() == "add"){
//			$("#sysNoTitle").show();
//			$("#sysNoValue").show();
//		}else{
//			$("#sysNoForItem").combobox('setValue','');
//			$("#sysNoTitle").hide();
//			$("#sysNoValue").hide();
//		}
		
		$("#brandNoTitle").show();
		$("#brandNoValue").show();
	}else{
		if(planType == '0'){
			$("#sysNoTitle").hide();
			$("#brandNoTitle").hide();
			$("#sysNoValue").hide();
			$("#brandNoValue").hide();
			$("#sysNoForItem").combobox('setValue','');
			$("#brandNoForItem").combobox('setValue','');
		}else{
			$("#sysNoTitle").show();
			$("#brandNoTitle").show();
			$("#sysNoValue").show();
			$("#brandNoValue").show();
		}
	}
};
billchplan.planTypeFormatter = function(value, rowData, rowIndex){
	return billchplan.planType[value];
};
billchplan.itemFormatter = function(value, rowData, rowIndex){
	if(rowData && rowData.item){
		return rowData.item[this.field];
	}
	return value;
};
billchplan.packNumFormatter = function(value, rowData, rowIndex){
	return "";
};
billchplan.numFormatter = function(value, rowData, rowIndex){
	return rowData.itemQty - rowData.realQty;
};
billchplan.realQtyFormatter = function(value, rowData, rowIndex){
	if(value){
		return value;
	}else{
		return '';
	}
	
};
billchplan.status = {};
billchplan.statusFormatter = function(value, rowData, rowIndex){
	return billchplan.status[value];
};
//搜索数据
billchplan.searchData = function(){
	
	var startCreatetm =  $('#startCreatetmCondition').datebox('getValue');
	var endCreatetm =  $('#endCreatetmCondition').datebox('getValue');
	var audittmStart =  $('#startAudittmCondition').datebox('getValue');
	var audittmEnd =  $('#endAudittmCondition').datebox('getValue');
	var planDateStart =  $('#startPlanDateCondition').datebox('getValue');
	var planDatemEnd =  $('#endPlanDateCondition').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }   
	if(!isStartEndDate(audittmStart,audittmEnd)){    
		alert("审核日期开始日期不能大于结束日期");   
		return;   
	}   
	if(!isStartEndDate(planDateStart,planDatemEnd)){    
		alert("盘点日期开始日期不能大于结束日期");   
		return;   
	} 
	var startAudittm =  $('#startAudittmCondition').datebox('getValue');
	var endAudittm =  $('#endAudittmCondition').datebox('getValue');
	if(!isStartEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    } 
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_ch_plan/list.json?locno='+billchplan.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	billchplan.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
};

//清除查询条件
billchplan.searchClear = function(){
	$('#searchForm').form("clear");
};
//清除Form表单
billchplan.clearForm = function(id){
	$('#'+id).form("clear");
};
billchplan.clearItem = function(){
	$('#selectItemSearchForm').form("clear");
	$('#brandNoItem').combobox("loadData",[]);
	$('#seasonItem').combobox("loadData",[]);
	$('#genderItem').combobox("loadData",[]);	
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
//复核装箱
billchplan.packageBox = function(){
	var rows = $("#check_box_dataGrid").datagrid('getRows');
	var packageboxcallback = function(r){
		r = billchplan.parseJsonStringToJsonObj(r);
		if(r && r.success){
			$("#check_box_dialog").window('close');//2,关闭窗口
		}else{
			
		}
		
	};
	rows = encodeURIComponent(JSON.stringify(rows));
	var boxNo = $("#checkboxsearchForm input[name=boxNo]").val();
	if(boxNo && boxNo!=''){
		var params = {  "datas":rows,
						"boxNo":boxNo,
						"recheckNo":$("#recheckNo").val(),
						"divideNo":$("#divideNo").val(),
						"locno":$("#locno").val()
						};
		billchplan.ajaxRequest(BasePath+'/bill_om_recheck/packagebox',params,true,packageboxcallback);
	}else{
		alert("请输入箱号，或取新箱号！");
	}
	
};
billchplan.openSelectWin = function(){
	var type = $("#planType").combobox('getValue');
	if(parseInt(type)==1){//储位选择界面
		$('#cell_select_dialog').window({
			title:"储位选择",
			data:{record: row}
		});
		$('#cell_select_dialog').window('open');
		$("#cell_select_datagrid").datagrid('loadData',[]);
	}else if(parseInt(type)==0){//商品选择界面
		$('#item_select_dialog').window({
			title:"商品选择",
			data:{record: row}
		});
		$('#item_select_dialog').window('open');
		$("#item_select_datagrid").datagrid('loadData',[]);
	}else{
		alert("请选择盘点类型!");
	}
};
billchplan.openSelectWin = function(obj){
	var row = $(obj).parent().parent().parent().parent().parent().parent().parent();
	//var index = $('#itemDetail').datagrid('getRowIndex', row);
	$('#order_dtl1_dataGrid').datagrid('selectRow',row.index());
	//var row = $('#itemDetail').datagrid('getSelected');
	if(row){
		var type = $("#planType").combobox('getValue');
		if(parseInt(type)==1){//储位选择界面
			$('#cell_select_dialog').window({
				title:"储位选择",
				data:{record: row}
			});
			$('#cell_select_dialog').window('open');
			$("#cell_select_datagrid").datagrid('loadData',[]);
		}else{//商品选择界面
			$('#item_select_dialog').window({
				title:"商品选择",
				data:{record: row}
			});
			$('#item_select_dialog').window('open');
			$("#item_select_datagrid").datagrid('loadData',[]);
		}
		
	}
};
//切换选项卡刷新单据查询的dataGrid
function refreshTabs(){
	$('#mainTab').tabs({
		onSelect:function(title,index){
			
			$('#main_order_dtlId').tabs('resize');
			
		    $('#mainDataGrid').datagrid('resize', {
		    	width:function(){return document.body.clientWidth;}
			});
			
			$('#queryConditionDiv').panel('resize',{
				width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#easyui-panel-id').panel('resize',{
		    	width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#order_dtl1_dataGrid').datagrid('resize', {
		    	width:function(){return document.body.clientWidth;}
		    });
		    
		    $('#order_dtl2_dataGrid').datagrid('resize', {
		    	width:function(){return document.body.clientWidth;}
		    });
		   
		},onLoad:function(panel){
			$('#queryConditionDiv').panel('resize',{
			     width:function(){return document.body.clientWidth;}
		    });
		}
		
	});
}
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
/**盘点日期限制在当前天的前后几天范围内**/
function planDateCheck(curDate,planDate,beforeDay,afterDay){
	if(curDate.length > 0 && planDate.length > 0){
		var arrCurDate = curDate.split("-");
		var arrPlanDate = planDate.split("-");
		var befDate = new Date(arrCurDate[0],arrCurDate[1],arrCurDate[2]);   
		var aftDate = new Date(arrCurDate[0],arrCurDate[1],arrCurDate[2]);
        var allPlanDate = new Date(arrPlanDate[0],arrPlanDate[1],arrPlanDate[2]);   
        befDate.setTime(befDate.getTime()-(beforeDay*24*60*60*1000));
        aftDate.setTime(aftDate.getTime()+(afterDay*24*60*60*1000));
        if(allPlanDate.getTime() >= befDate.getTime() && allPlanDate.getTime() <= aftDate.getTime()){
        	return true;
        }
	}
	return false;
}
billchplan.planTypeChange = function(){
	var newValue = $("#planType").combobox('getValue');
	if(newValue=='1'){
		$("#cell_div").show();
		$("#item_div").hide();
		$("#limitBrandFlag").combobox('setValue','');
		$("#sysNoForItem").combobox('setValue','');
		$("#brandNoForItem").combobox('setValue','');
		//$("#limitBrandFlagTitle").hide();	
		//$("#limitBrandFlagValue").hide();
		$("#sysNoTitle").show();
		$("#brandNoTitle").show();
		$("#sysNoValue").show();
		$("#brandNoValue").show();
		$("#limitBrandFlagTitle").show();
		$("#limitBrandFlagValue").show();
		billchplan.showItemTypeAndQuality(false);
	}else{
		$("#item_div").show();
		$("#cell_div").hide();
		$("#limitBrandFlagTitle").show();
		$("#limitBrandFlagValue").show();
		billchplan.limitBrandFlagChange();
		billchplan.showItemTypeAndQuality(true);
	}
};
$.extend($.fn.datagrid.defaults.editors, {    
    textbutton: {    
        init: function(container, options){
            var input = $('<input type="text" name="itemNo" style="width: 90px;"><input type="button" style="width: 30px;"  onclick="javascript:billchplan.openSelectWin(this);">').appendTo(container);    
            return input;    
        },    
        getValue: function(target){ 
            return $(target).val();    
        },    
        setValue: function(target, value){
        	$(target[0]).val(value);  
        	$(target[1]).val("...");  
        },    
        resize: function(target, width){    
        }    
    }    
});
billchplan.selectWareNo = function(){//当选择仓区的时候
	var wareNo = $("#wareNo").combobox("getValue");
	if(wareNo == ''){
		wms_city_common.comboboxLoadFilter(
				["areaNo"],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[true],
				null,
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				["areaNo"],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[true],
				BasePath+'/cm_defarea/get_biz',
				{"locno":billchplan.user.locno,"wareNo":wareNo},
				null,
				null);
	}
	billchplan.selectAreaNo();
};
billchplan.selectAreaNo = function(){//当选择库区的时候
	var wareNo = $("#wareNo").combobox("getValue");
	var areaNo = $("#areaNo").combobox("getValue");
	if(wareNo == '' || areaNo == ''){
		wms_city_common.comboboxLoadFilter(
				["stockNo"],
				'stockNo',
				'stockNo',
				'valueAndText',
				false,
				[true],
				null,
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				["stockNo"],
				'stockNo',
				'stockNo',
				'valueAndText',
				false,
				[true],
				BasePath+'/cm_defstock/get_biz',
				{"locno":billchplan.user.locno,"wareNo":wareNo,"areaNo":areaNo},
				null,
				null);
	}
};
billchplan.searchCell = function(){
	var fromObjStr=convertArray($('#selectCellSearchForm').serializeArray());
	var queryMxURL=BasePath+'/cm_defcell/findCmDefcell4Plan.json?locno='+billchplan.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	billchplan.loadGridDataUtil('cell_select_datagrid', queryMxURL, reqParam);
	
};
billchplan.searchItem = function(){
	var fromObjStr=convertArray($('#selectItemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/category/queryItemByCate?locno='+billchplan.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam.planNo = $("#planNo").val();
	reqParam.itemType = $("#itemType").combobox('getValue');
	reqParam.quality = $("#quality").combobox('getValue');
	billchplan.loadGridDataUtil('item_select_datagrid', queryMxURL, reqParam);
	
};
billchplan.cellSelectOK = function (){
	var selectRows = $("#cell_select_datagrid").datagrid('getChecked');
	var len = selectRows.length;
	if(len < 1){
		alert("请选择储位!");
		return;
	}
	$.messager.confirm("确认","您确定要添加这"+len+"个储位信息吗？", function (r){  
		if (!r) {
			return;
		}else{
//			for(var i=0;i<len;i++){
//				selectRows[i].wareName=$("#wareNo").combobox('getText');
//				selectRows[i].areaName=$("#areaNo").combobox('getText');
//			}
			billchplan.insertRowAtEnd('dtl_cell_dataGrid',selectRows);
			$("#cell_select_dialog").window('close');
		}
	});
	
};
//整库区添加储位
billchplan.cellSelectOKByAreaNo = function (){
	var wareNo = $("#wareNo").combobox('getValue');
	var areaNo = $("#areaNo").combobox('getValue');
	if(wareNo == null || wareNo == ''){
		alert("请选择仓区!");
		return;
	}
	if(areaNo == null || areaNo == ''){
		alert("请选择库区!");
		return;
	}
	var url = BasePath + '/cm_defcell/findCmDefcell4PlanByAS?locno='+billchplan.user.locno;
	var params = {
			wareNo:wareNo,
			areaNo:areaNo
	};
	var selectRows = [];
	wms_city_common.loading("show", "正在统计储位......");
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: url,
        data: params,
        success: function(result){
        	selectRows = result.rows;
		}
    });
	wms_city_common.loading();
	var len = selectRows.length;
	if(len < 1){
		alert("没有符合库区的储位信息!");
		return;
	}
	$.messager.confirm("确认","您确定要添加这"+len+"个储位信息吗？", function (r){  
		if (!r) {
			return;
		}else{
			url = BasePath + '/bill_ch_plan_dtl/save_dtl_batch';
			params = {
					locno:billchplan.user.locno,
					planNo:$("#planNo").val(),
					planType:$("#planType").combobox('getValue'),
					wareNo:wareNo,
					areaNo:areaNo
			};
			wms_city_common.loading("show", "正在保存储位......");
			$.ajax({
				cache: false,
		        async: false,
		        type: 'POST',
		        url: url,
		        data: params,
		        success: function(result){
		        	if(result.result == 'success'){
		        		billchplan.closeWindow('cell_select_dialog');
		        		billchplan.closeWindow('showDetailDialog');
		        		alert("添加成功!");
		        	}else{
		        		alert(result.result+",添加失败!");
		        	}
				}
		    });
			wms_city_common.loading();
		}
	});
	
};
//整通道添加储位
billchplan.cellSelectOKByStockNo = function (){
	var wareNo = $("#wareNo").combobox('getValue');
	var areaNo = $("#areaNo").combobox('getValue');
	var stockNo = $("#stockNo").combobox('getValue');
	if(wareNo == null || wareNo == ''){
		alert("请选择仓区!");
		return;
	}
	if(areaNo == null || areaNo == ''){
		alert("请选择库区!");
		return;
	}
	if(stockNo == null || stockNo == ''){
		alert("请选择通道!");
		return;
	}
	var url = BasePath + '/cm_defcell/findCmDefcell4PlanByAS?locno='+billchplan.user.locno;
	var params = {
			wareNo:wareNo,
			areaNo:areaNo,
			stockNo:stockNo
	};
	var selectRows = [];
	wms_city_common.loading("show", "正在统计储位......");
	$.ajax({
		cache: false,
		async: false,
		type: 'POST',
		url: url,
		data: params,
		success: function(result){
			selectRows = result.rows;
		}
	});
	wms_city_common.loading();
	var len = selectRows.length;
	if(len < 1){
		alert("没有符合该通道的储位信息!");
		return;
	}
	$.messager.confirm("确认","您确定要添加这"+len+"个储位信息吗？", function (r){  
		if (!r) {
			return;
		}else{
			url = BasePath + '/bill_ch_plan_dtl/save_dtl_batch';
			params = {
					locno:billchplan.user.locno,
					planNo:$("#planNo").val(),
					planType:$("#planType").combobox('getValue'),
					wareNo:wareNo,
					areaNo:areaNo,
					stockNo:stockNo
			};
			wms_city_common.loading("show", "正在保存储位......");
			$.ajax({
				cache: false,
		        async: false,
		        type: 'POST',
		        url: url,
		        data: params,
		        success: function(result){
		        	if(result.result == 'success'){
		        		billchplan.closeWindow('cell_select_dialog');
		        		billchplan.closeWindow('showDetailDialog');
		        		alert("添加成功!");
		        	}else{
		        		alert(result.result+",添加失败!");
		        	}
				}
		    });
			wms_city_common.loading();
		}
	});
	
};
billchplan.itemSelectOK = function (){
	var selectRows = $("#item_select_datagrid").datagrid('getChecked');
	var pRows = $("#dtl_item_dataGrid").datagrid('getRows');
	var newRows = [];
	var len = selectRows.length;
	if(len < 1){
		alert("请选择商品!");
		return;
	}
	$.messager.confirm("确认","您确定要添加这"+len+"个商品信息吗？", function (r){  
		if (!r) {
			return;
		}else{
			for(var i=0;i<selectRows.length;i++){
				selectRows[i].brandName = selectRows[i].brandNoStr;
				selectRows[i].colorName = selectRows[i].colorNoStr;
			}
			var exisit = false;
			var index = 0;
			for(var i=0;i<selectRows.length;i++){
				exisit = false;
				for(var j=0;j<pRows.length;j++){
					if(selectRows[i].itemNo == pRows[j].itemNo && selectRows[i].sizeNo == pRows[j].sizeNo){
						exisit = true;
						break;
					}
				}
				if(!exisit){
					newRows[index] = selectRows[i];
					index++;
				}
			}
			billchplan.insertRowAtEnd('dtl_item_dataGrid',newRows);
			$("#item_select_dialog").window('close');
		}
	});
};
billchplan.itemSelectOKByBrand = function (){
	var brandNo = $("#brandNoForItem").combobox('getValue');
	if(brandNo == null || brandNo == ''){
		alert("请选择品牌!");
		return;
	}
	billchplan.ajaxRequest(BasePath+ '/bill_ch_plan_dtl/list.json',{locno:billchplan.user.locno,planNo:$("#planNo").val()},false,function(result){
		if(result.total != null && result.total > 0){
			alert("该计划单已存在明细,不能整品牌添加!");
			return;
		}else{
			var queryMxURL=BasePath+'/category/queryItemByCate?locno='+billchplan.user.locno;
			billchplan.ajaxRequest(queryMxURL, {brandNo:brandNo}, false,function(result) {
				if(result == null || result.total == null || result.total == 0){
					alert("没有找到品牌为【"+brandNo+"】的商品");
					return;
				}else{
					$.messager.confirm("确认","您确定要添加这"+result.total+"条商品吗？", function (r){
						if(r){
							var url = BasePath+'/bill_ch_plan_dtl/save_dtl_brand';
							var praam = {
									planType:$("#planType").combobox('getValue'),
									locno:billchplan.user.locno,
									planNo:$("#planNo").val(),
									brandNo:brandNo
							};
							$.post(url, praam, function(result) {
								if(result.result == 'success'){
									alert('保存成功!');
									billchplan.closeWindow('item_select_dialog');
									billchplan.closeWindow('showDetailDialog');
									$("#mainDataGrid").datagrid('load');
								}else if(result.result == 'fail'){
									alert('保存失败!',1);
								}else{
									alert(result.result,2);
								}
							}, "JSON").error(function() {
						    	alert('保存失败!',1);
						    });
						}
					});
				}
			});
		}
	});
	
	
};
billchplan.initCategoryTree = function(){
	var sysNo = $("#sysNo").combobox('getValue');
	$('#tt').tree({    
	    url:BasePath+'/category/queryCategoryTree2.htm?sysNo='+sysNo,
	    checkbox:true,
	    onCheck:function(node, checked){
	    	var checkedNodes = $(this).tree('getChecked');
	    	var cateNos = [];
	    	for(var i=0;i<checkedNodes.length;i++){
	    		cateNos.push("'"+checkedNodes[i].id+"'");
	    	}
	    	var queryParams = "";
	    	if(cateNos.length>0){
	    		queryParams = ' and cate_no in ( '+ cateNos.join(',') + ' )';
	    	}else{
	    		queryParams = ' and cate_no in ( '+ null + ' )';
	    	}
	    	$("#item_select_datagrid").datagrid({
	    		url:BasePath+'/item/findItemAndSize',
	    		queryParams:{queryCondition:queryParams}
	    	});
	    }
	});  
};
billchplan.sysNo = {};
billchplan.initMainTree = function(data){
	$('#mainTree').tree({
		data:data,
		onBeforeExpand:function(node){
			//var c = $('#mainTree').tree('getChildren', node.target);
			var c = node.children;
			if(c != null && c.length > 0){
				
			}else{
				billchplan.ajaxRequest(BasePath+'/category/queryChildrenTree',{headCateNo:node.id},false,function(r){
					$('#mainTree').tree('append', {
						parent: node.target,
						data: r
					});
				});
			}
			billchplan.loadItem(node.id, true);
		},
		onClick:function(node){
			if(!node.state){
				billchplan.loadItem(node.id, false);
			}else{
				billchplan.loadItem(node.id, true);
			}
		}
	});
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].id] = data[index].text;
	}
	billchplan.sysNo = temp;
};
billchplan.loadItem = function(cateNo,hasChildren){
	
	var tempObj = $('#item_select_datagrid');
	var queryParams = {
			cateNo:cateNo,
			hasChildren:hasChildren,
			locno:billchplan.user.locno,
			planNo:$("#planNo").val(),
			itemType:$("#itemType").combobox('getValue'),
			quality:$("#quality").combobox('getValue')
	};
	var url = BasePath+'/category/queryItemByCate';
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

// JS初始化执行
$(document).ready(function(){
	//创建日期初始为前两天
	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
	billchplan.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(u){billchplan.user=u;});
	$("#check_box_dataGrid").datagrid({
		onSelect:function(rowIndex, rowData){
			var columnOption  = $("#check_box_dataGrid").datagrid('getColumnOption','packageNum');
			columnOption.editor.options.min = 1;  
            columnOption.editor.options.max = rowData.itemQty - rowData.realQty; 
            
            var rows = $("#check_box_dataGrid").datagrid('getRows');
            for(var i=0;i<rows.length;i++){
            	  $("#check_box_dataGrid").datagrid('endEdit',i);
            }
            $("#check_box_dataGrid").datagrid('beginEdit',rowIndex);
		}
	});
	//billchplan.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CHECKNUM_STATUS',{},false,billchplan.initStatus);
	//billchplan.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_PLAN_TYPE',{},false,billchplan.initPlanType);
	//billchplan.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_PLAN_WAY',{},false,billchplan.initLimitBrandFlag);
	//billchplan.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',{},false,billchplan.initQuality);
	//billchplan.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',{},false,billchplan.initItemType);
	wms_city_common.initItemTypeAndQuality("itemType","quality");
	refreshTabs();
	//billchplan.ajaxRequest(BasePath+'/cm_defware/get_biz',{"locno":billchplan.user.locno},false,billchplan.initWareNo);
	$("#sysNo").combobox({
		onSelect:billchplan.initCategoryTree
	});
	billchplan.ajaxRequest(BasePath+'/category/queryMainTree',{},false,billchplan.initMainTree);
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNoSearch]',$('#searchForm'))},
			{"sysNoObj":$('input[id=sysNoForItem]',$('#divideMainInfoForm'))},
			{"sysNoObj":$('input[id=sysNoItem]',$('#selectItemSearchForm'))},
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
//	var objs1 = [];
//	objs1.push(
//			{"sysNoObj":$('#sysNoForItem'),"brandNoObj":$('input[id=brandNoForItem]',$('#divideMainInfoForm'))}
//			);
//	wms_city_common.loadSysNo4Cascade(objs1);
	
	
	var objs1 = [];
	objs1.push(
			{"sysNoObj":$('#sysNoForItem'),"brandNoObj":$('input[id=brandNoForItem]',$('#divideMainInfoForm'))}
			);
	billchplan.loadSysNoMultiple4CascadePlan(objs1);
	
	var objs2 = [];
	objs2.push(
			{"sysNoObj":$('#sysNoItem'),"brandNoObj":$('input[id=brandNoItem]',$('#selectItemSearchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs2);
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CHECKNUM_STATUS',
			{},
			billchplan.status,
			null);
	wms_city_common.comboboxLoadFilter(
			["planTypeCondition","planType","planTypeDiff"],
			null,
			null,
			null,
			true,
			[true,false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_PLAN_TYPE',
			{},
			billchplan.planType,
			null);
	wms_city_common.comboboxLoadFilter(
			["limitBrandFlagCondition","limitBrandFlag"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_PLAN_WAY',
			{},
			billchplan.limitBrandFlag,
			null);
	wms_city_common.comboboxLoadFilter(
			["wareNo"],
			'wareNo',
			'wareName',
			'valueAndText',
			false,
			[true],
			BasePath+'/cm_defware/get_biz',
			{"locno":billchplan.user.locno},
			null,
			null);	
	//初始化三级大类
	wms_city_common.cateForCascade(
			'cateOneItem',
			'cateTwoItem',
			'cateThreeItem',
			true
		);
	//根据品牌初始季节和性别
	billchplan.initGenderAndSeason();
	
	$('#dtl_diff_dataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer!=null){
						if(data.footer[1].isselectsum){
			   				billchplan.itemQty = data.footer[1].itemQty;
			   				billchplan.realQty = data.footer[1].realQty;
			   				billchplan.diffQty = data.footer[1].diffQty;
			   			}else{
			   				var rows = $('#dtl_diff_dataGrid').datagrid('getFooterRows');
				   			rows[1]['itemQty'] = billchplan.itemQty;
				   			rows[1]['realQty'] = billchplan.realQty;
				   			rows[1]['diffQty'] = billchplan.diffQty;
				   			$('#dtl_diff_dataGrid').datagrid('reloadFooter');
			   			}
					}else{
						var rows = $('#dtl_diff_dataGrid').datagrid('getFooterRows');
						if(rows!=null){
							rows[0]['itemQty'] = "";
				   			rows[0]['realQty'] = "";
				   			rows[0]['diffQty'] = "";
							rows[1]['itemQty'] = "";
				   			rows[1]['realQty'] = "";
				   			rows[1]['diffQty'] = "";
				   			$('#dtl_diff_dataGrid').datagrid('reloadFooter');
						}
					}
		   		}
			}
		);
});

billchplan.initGenderAndSeason =  function(){
	$('#sysNoItem').combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderItem',true,false);
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonItem',true,false);
		}
	});
};




/**
 * isShowAllCheckArray 是否显示全选(默认显示全选)，需与objs中{}的位置一一对应
 */
billchplan.planNoInit = "";
billchplan.ownerNoInit = "";
billchplan.planTypeInit = "";
billchplan.limitBrandFlagInit = "";
billchplan.loadSysNoMultiple4CascadePlan = function(objs,isShowAllCheckArray){
	//params是一个对象数组  对象obj 包含 两个属性  sysNoObj  1：品牌库combox 对象  2 brandNoObj 品牌combox 对象  如果brandNoObj为空就不做级联
	var map = {};
	if(isShowAllCheckArray == null || isShowAllCheckArray.length == 0){
		isShowAllCheckArray = [];
		for(var i=0,length=objs.length;i<length;i++){
			isShowAllCheckArray[i] = true;
		}
	}
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			for(var i=0,length=objs.length;i<length;i++){
				var obj = objs[i];
				map[obj.sysNoObj.selector] = isShowAllCheckArray[i];
				obj.sysNoObj.combobox({
				    data:data,
				    valueField:"itemvalue",
				    textField:"itemnamedetail",
				    panelHeight:150,
				    loadFilter:function(data){
						var tempData = [];
						if(map["#"+this.id]){
							tempData[tempData.length] = {itemvalue:'',itemnamedetail:'全选'};
						}
				    	for(var i=0;i<data.length;i++){
				    		tempData[tempData.length] = data[i];
				    	}
				    	return tempData;
				    }
				});
				if(obj.brandNoObj!=undefined){
					obj.sysNoObj.combobox({
						onSelect:function(){
							billchplan.loadBrand4SysNoMultiplePlan(obj.brandNoObj,$(this).combobox("getValue"),map["#"+this.id]);
						}
					});
		    	}
			}
		}
	});
};
billchplan.loadBrand4SysNoMultiplePlan = function(obj,sysNo,isShowAllCheck){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/brand/get_bizDy?sysNo='+sysNo,
		success : function(data) {
			obj.combobox({
			    data:data,
			    valueField:"brandNo",
			    textField:"textFieldName",
			    panelHeight:150,
			    multiple:true,
			    multiline:true,
			    editable:false,
			    loadFilter:function(data){
			    	if(sysNo == null || sysNo == ''){
			    		if(isShowAllCheck){
			    			data = [{brandNo:'',textFieldName:'全选'}];
			    		}else{
			    			data = [];
			    		}
						return data;
					}
			    	if(data.length>0){
						var tempData = [];
						if(isShowAllCheck){
							tempData[tempData.length] = {brandNo:'',textFieldName:'全选'};
						}
				    	for(var i=0;i<data.length;i++){
				    		var temp = {};
				    		temp.brandNo = data[i].brandNo;
				    		temp.textFieldName = data[i].brandNo + '→' + data[i].brandName;
				    		tempData[tempData.length] = temp;
				    	}
				    	return tempData;
			       	}
			    	return data;
			    },onSelect:function(record){
			    	var obj = $('#'+this.id);
			    	var values = obj.combobox('getValues');
			    	var brandNo=record.brandNo;
			    	var newValues=[];
			    	if(brandNo==''){
			    		newValues.push(brandNo);	
			    		obj.combobox('setValues',newValues);			    		
			    	}else{
			    		for(var i=0; i< values.length;i++){
				    		if(values[i]!=''){				    			
				    			newValues.push(values[i]);
				    		}
				    	}		
			    		obj.combobox('setValues',newValues);
			    	}
			    },onUnselect:function(record){
			    	var obj = $('#'+this.id);
			    	var values = obj.combobox('getValues');	
			    	if(values.length==0 && isShowAllCheck){
			    		obj.combobox('setValues',['']);	
			    	}
			    }
			    
			});
			
			if(isShowAllCheck){
				obj.combobox('setValues',['']);	
			}
			
			//是否赋值
			if(billchplan.planNoInit != ''){
				if((billchplan.planTypeInit=='0'&&billchplan.limitBrandFlagInit=='1')
					|| (billchplan.planTypeInit=='1'&&billchplan.limitBrandFlagInit=='0')
					|| (billchplan.planTypeInit=='1'&&billchplan.limitBrandFlagInit=='1')){
					var vArray = new Array();
					var url = BasePath + '/bill_ch_plan_dtl_brand/get_biz';
					var params = {
							locno:billchplan.user.locno,
							ownerNo:billchplan.ownerNoInit,
							planNo:billchplan.planNoInit
					};
					ajaxRequestAsync(url, params, function(data) {
						for(var index=0;index<data.length;index++){
							vArray.push(data[index].brandNo);
						}
					});
					obj.combobox('setValues',vArray);	
				}
			}else{
				obj.combobox('setValues',['']);	
			}
			
		}
	});
};
