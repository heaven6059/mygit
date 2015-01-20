
var billomrecheck = {};
billomrecheck.user;
billomrecheck.recheckNo;
billomrecheck.boxDatagridData;

//加载Grid数据Utils
billomrecheck.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};



//创建分货单
billomrecheck.showAddDialog = function(){
	
	//关闭window关闭提示的tip
//	$('#showDetailDialog').window({
//		onBeforeClose:function(){
//			$('.tooltip').remove();
//		}
//	});
//	wms_city_common.closeTip('showDetailDialog');
	
	$("#showDetailDialog").window('open');
	
	$("#divideMainInfoForm")[0].reset();
	$("#storeNo").combobox('clear');
	//var divideNoInput = $("#divideNo");
	//divideNoInput.attr("readonly",false);
	//divideNoInput.find("+ input+ input").attr("disabled",false);
	
	$('#saveBtn').show();
	$('#checkBoxBtn').linkbutton('enable');
	
	$('#selectDivideBtn').linkbutton('enable');
	
	$('#storeNo').combobox('enable');
//	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text").attr("readOnly",false);
//	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text+ span span").addClass("combo-arrow");
//	$("#divideMainInfoForm input[id=storeNo]+ span").css("background-color","white");
//	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text").css("background-color","white");
	
	deleteAllGridCommon('order_dtl1_dataGrid');
	var rows = $('#order_dtl1_dataGrid').datagrid('getFooterRows');
	if(rows!=null){
		rows[0]['itemQty'] = 0;
		rows[0]['realQty'] = 0;
		rows[0]['diffQty'] = 0;
		rows[1]['itemQty'] = 0;
		rows[1]['realQty'] = 0;
		rows[1]['diffQty'] = 0;
		$('#order_dtl1_dataGrid').datagrid('reloadFooter');
	}
	
	deleteAllGridCommon('order_dtl2_dataGrid');
	var rows2 = $('#order_dtl2_dataGrid').datagrid('getFooterRows');
	if(rows2!=null){
		rows2[0]['itemQty'] = 0;
		rows2[1]['itemQty'] = 0;
		$('#order_dtl2_dataGrid').datagrid('reloadFooter');
	}
	
	//$('#order_dtl1_dataGrid').datagrid('loadData',[]);
	//$('#order_dtl2_dataGrid').datagrid('loadData',[]);
};




//关闭
billomrecheck.closeWindow = function(id){
	$('#'+id).window('close');  
};
 


//验证编辑行
billomrecheck.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		var originQty = tempObj.datagrid('getEditor', {index:i,field:'packageNum'});
    		if(originQty!=null){
    			var itemNo = rowArr[i].itemNo;
    			var itemQty = rowArr[i].itemQty;
            	var packageNum = tempObj.datagrid('getEditor', {index:i,field:'packageNum'});
            	if(packageNum!=null){
            		
            		if(packageNum.target.val() > itemQty){
                		$(packageNum.target).focus();
                		return "商品"+itemNo+":装箱数量不能大于可分货数量;";
            		}else if(packageNum.target.val() == "0"){
            			return "商品"+itemNo+":装箱数量不能为0;";
                	}else{
                		tempObj.datagrid('endEdit', i);
                	}
            	}
    		}
    	}else{
    		return '数据验证没有通过!';
    	}
    }
    return "";
};


//选择分货任务单
billomrecheck.selectDivide=function(){
	
	var checkedRows = $("#divide_no_dataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择分货任务单!',1);
		return;
	}
	wms_city_common.loading("show");
	$.each(checkedRows, function(index, item){
		billomrecheck.setDivideNo(item,'add');
	});
	wms_city_common.loading();
};

//打开复核装箱界面
billomrecheck.opencheckbox = function(){
//	var checkRows = $("#mainDataGrid").datagrid('getChecked');
//	if(checkRows[0].status=='11'){
//		alert("该单没有审核，不能装箱，请选审核！");
//		return;
//	}
	
	var recheckNo = $('#recheckNo').val();
	if(recheckNo == "" || recheckNo==null){
		alert("主档数据不能为空!");
		return;
	}
	
	var selectRows = $("#order_dtl1_dataGrid").datagrid('getChecked');
	if(selectRows.length<1){
		alert("请选择需要装箱的商品");
		return;
	}
	
	var tipStr = "";
	var dataList = [];
	$.each(selectRows, function(index, item){
		
		if(item.realQty >= item.itemQty){
			tipStr = '【商品'+item.itemNo+",尺码"+item.sizeNo+"】";
			return false;
		}
		
		var params = {
				//itemId:item.itemId,
				packQty:item.packQty,
				expNo:item.expNo,
				expType:item.expType,
				ownerNo:item.ownerNo,
				//lineNo:item.lineNo,
				//divideId:item.divideId,
				itemNo:item.itemNo,
				itemName:item.itemName,
				itemQty:item.itemQty-item.realQty,
				packageNum:item.itemQty-item.realQty,
				styleNo:item.styleNo,
				colorName:item.colorName,
				sizeNo:item.sizeNo,
				boxNo:item.boxNo,
				poNo:item.poNo
		};
		dataList[dataList.length] = params;
	});
	
	if(tipStr!=""){
		alert(tipStr+"已经装箱完成,不能再次装箱!");
		return;
	}
	
	
	$("#check_box_dataGrid").datagrid('loadData',dataList);
	$("#check_box_dialog").window('open');
	$('#boxNoNew').val('');//清空箱号文本
	
	var selectRows = $("#check_box_dataGrid").datagrid('getRows');
	$.each(selectRows, function(index, item){
		$("#check_box_dataGrid").datagrid('beginEdit',index);
	});
	
};
//打开分货单选择界面
billomrecheck.opendividenoselect = function(){
	var queryMxURL=BasePath+ '/bill_om_recheck/divideSelectQuery';
	var queryParams={};
	queryParams['locno'] = billomrecheck.user.locno;
	billomrecheck.loadGridDataUtil('divide_no_dataGrid', queryMxURL, queryParams);
	$("#divide_no_dialog").window('open');
};

billomrecheck.divideQuery = function(){
	var startDivideDate =  $('#startDivideDate').datebox('getValue');
	var endDivideDate =  $('#endDivideDate').datebox('getValue');
	if(!isStartEndDate(startDivideDate,endDivideDate)){    
		alert("分货日期开始日期不能大于结束日期");   
        return;   
    }
	var queryMxURL=BasePath+ '/bill_om_recheck/divideSelectQuery';
	var fromObjStr=convertArray($('#divideNoSearchForm').serializeArray());
	var queryParams= eval("("+fromObjStr+")");
	queryParams['locno'] = billomrecheck.user.locno;
	billomrecheck.loadGridDataUtil('divide_no_dataGrid', queryMxURL, queryParams);
	$("#divide_no_dialog").window('open');
};
billomrecheck.setDivideNo = function(rowData,type){
	//$('#order_dtl1_dataGrid').datagrid('loadData', { total: 0, rows: [] });
	$("#recheckNo").val(rowData.recheckNo);
	$("#divideNo").val(rowData.divideNo);
	$("#locno").val(rowData.locno);
	$("#expDate").val(rowData.expDate);
	//$("#recheckName").val(rowData.creator);
	billomrecheck.initStoreByDivideNo(rowData,type);
	$('#divideMainInfoForm input[id=storeNo]').combobox('setValue',rowData.storeNo);
	$("#divide_no_dialog").window('close');
};
//初始客户选择下拉框
billomrecheck.initStoreByDivideNo = function(rowData,type){
	var divideNo = rowData.divideNo;
	var locno = rowData.locno;
	var selectType = 0;
	if(type == "add"){
		selectType = 1;
		$('#storeNo').combobox('reload',BasePath+'/bill_om_recheck/selectStoreFromDivideDtl?divideNo='+divideNo+'&locno='+locno+"&selectType="+selectType);
	}else{
		billomrecheck.selectStoreNoToDivideDtl(rowData);
		$('#storeNo').combobox('reload',BasePath+'/store/get_biz?storeNo='+rowData.storeNo);
	}
	
	
//	$('#storeNo').combobox({
//		 url:BasePath+'/bill_om_recheck/selectStoreFromDivideDtl?divideNo='+divideNo+'&locno='+locno+"&selectType="+selectType,
//	     valueField:"storeNo",
//	     textField:"storeName",
//	     panelHeight:"auto"
//	});
};

//选择客户执行
billomrecheck.selectStoreNoToDivideDtl = function(data){
	var queryURL=BasePath+ '/bill_om_recheck/queryRecheckBoxItem';
	var divideNo = $("#divideNo").val();
	var locno = billomrecheck.user.locno;
	var storeNo = data.storeNo;
	if(divideNo!=""&&locno!=""&&storeNo!=""){
		var queryParams={"divideNo":divideNo,"storeNo":storeNo,"locno":locno};
		billomrecheck.loadGridDataUtil('order_dtl1_dataGrid', queryURL, queryParams);
	}
};

billomrecheck.initCheckUser = function(locno){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#checkUser').combobox({
			     valueField:"workerNo",
			     textField:"unionName",
			     data:data,
			     panelHeight:150,
			  }).combobox("select",data[0].workerNo);
		}
	});
};

billomrecheck.checkUserSelect = function(){
	var checkRows = $("#mainDataGrid").datagrid('getChecked');
	if(checkRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}else{
		for(var i=0;i<checkRows.length;i++){
			if(checkRows[i].status != 10){
				alert("请不要选择已经处理过的单据进行审核，请重新选择");
				$("#check_user_dialog").window('close');
				return;
			}
		}
		
		$('#checkUser').combobox('setValue',billomrecheck.user.loginName);
		
		$("#check_user_dialog").window('open');
	}
};
//审核
billomrecheck.check = function(){
	var checkUser = $("#checkUser").combobox('getValue');
	if(checkUser){
		var checkRows = $("#mainDataGrid").datagrid('getChecked');
		var checkIds = [];
		for(var i=0;i<checkRows.length;i++){
			if(checkRows[i].status != 10){
				alert("请不要选择已经处理过的单据进行审核，请重新选择");
				$("#check_user_dialog").window('close');
				return;
			}
			checkIds.push(checkRows[i].recheckNo+"-"+billomrecheck.user.locno);
		}
		var url = BasePath+'/bill_om_recheck/check';
		var data = {ids:checkIds.join(","),checkUser:checkUser};
		wms_city_common.loading("show");
		billomrecheck.ajaxRequest(url,data,true,function(r){
			wms_city_common.loading();
			if(r.success=="true"){
				alert("审核成功!");
				$("#mainDataGrid").datagrid('load');//1,查询加载待复核的商品信息
			}else{
				alert('审核失败,原因:'+r.msg,2);
			}
		});
		$("#check_user_dialog").window('close');
	}else{
		alert("请选择复核人！");
		return;
	}
	
	
};
billomrecheck.editInfo = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status != '10'){
		alert('只能修改【新建】状态的记录!',1);
		return;
	}else {
		var count = billomrecheck.checkIsRfPackage(checkedRows[0]);
		if(count > 0){
			billomrecheck.openPackageBoxRf(checkedRows[0]);
		}else{
			billomrecheck.loadDetail(checkedRows[0],"edit");
		}
	}
};

//验证是否需要RF封箱
billomrecheck.checkIsRfPackage = function(rowData){
	var count = 0;
	var url = BasePath + '/bill_om_recheck_dtl/get_biz';
	var params = {
		locno : rowData.locno,
		recheckNo : rowData.recheckNo,
		containerNo : 'N'
	};
	ajaxRequestAsync(url,params,function(data){
		count = data.length;
	});
	return count;
};

//rf封箱弹出窗口
billomrecheck.openPackageBoxRf = function(rowData){
	$("#check_box_dialog_rf").window('open');
	$('#check_box_dataGrid_rf').datagrid('loadData', { total: 0, rows: [] });
	$("#recheckNoRf").val(rowData.recheckNo);
	$("#divideNoRf").val(rowData.divideNo);
	$('#storeNoRf').combobox('reload',BasePath+'/store/get_biz?storeNo='+rowData.storeNo);
	//$('#storeNoRf').combobox('reload',BasePath+'/bill_om_recheck/selectStoreFromDivideDtl?divideNo='+rowData.divideNo+'&locno='+rowData.locno+"&selectType=0");
	$("#storeNoRf").combobox('setValue',rowData.storeNo);
	var queryMxURL=BasePath+ '/bill_om_recheck_dtl/dtl_list.json';
	var queryParams={recheckNo:rowData.recheckNo,locno:rowData.locno,containerNo : 'N'};
	billomrecheck.loadGridDataUtil('check_box_dataGrid_rf', queryMxURL, queryParams);
};

//rf封箱
billomrecheck.packageBoxRf = function(rowData){
	var packageboxcallback = function(r) {
		if (r.success == "true") {
			alert("装箱成功!");
			$("#check_box_dialog_rf").window('close');// 2,关闭窗口
			$('#check_box_dataGrid_rf').datagrid('load');
		} else {
			alert('装箱失败,原因:' + r.msg,1);
		}
	};
	
	$.messager.confirm("确认","你确定要对RF未完成的数据进行封箱?", function (r){ 
		if(!r){
			return;
		}
		wms_city_common.loading("show");
		var params = {
				"storeNo" : $('#storeNoRf').combobox("getValue"),
				"recheckNo" : $("#recheckNoRf").val(),
				"divideNo" : $("#divideNoRf").val(),
				"locno" : billomrecheck.user.locno
		};
		billomrecheck.ajaxRequest(BasePath + '/bill_om_recheck/packageBoxRf', params, true, packageboxcallback);
		wms_city_common.loading();
	});
	
};

//加载订单详情
billomrecheck.loadDetail = function(rowData,type){
	$("#divideMainInfoForm").form("clear");
	billomrecheck.setDivideNo(rowData,type);
	var divideNoInput = $("#divideNo");
	divideNoInput.attr("readonly","readonly");
	$('#saveBtn').show();
	$('#saveBtn').hide();
	$('#selectDivideBtn').linkbutton('disable');
	
	if(type=="edit"){
		$('#checkBoxBtn').linkbutton('enable');
		$('#btn-del-dtl').show();
		$('#btn-save-dtl').show();
	}else if(type=="select"){
		$('#checkBoxBtn').linkbutton('enable');
		$('#checkBoxBtn').linkbutton('disable');
		$('#btn-del-dtl').hide();
		$('#btn-save-dtl').hide();
	}else{
		$('#checkBoxBtn').linkbutton('enable');
		$('#checkBoxBtn').linkbutton('disable');
		$('#btn-del-dtl').show();
		$('#btn-save-dtl').show();
	}
	
	
	billomrecheck.recheckNo = rowData.recheckNo;
	billomrecheck.disableCombox();
	$("#showDetailDialog").window('open');
	//billomrecheck.queryRecheckBoxItem();
	var queryMxURL=BasePath+ '/bill_om_recheck_dtl/list.json';
	var queryParams={recheckNo:rowData.recheckNo,locno:rowData.locno};
	billomrecheck.loadGridDataUtil('order_dtl2_dataGrid', queryMxURL, queryParams);
};
billomrecheck.clearBoxDetail = function(){
	$("#boxSearchForm").form('clear');
	$("#order_dtl2_dataGrid").datagrid('loadData',billomrecheck.boxDatagridData);
	
};
billomrecheck.clearSearch = function(){
	$("#searchForm").form('clear');
	$('#brandNo').combobox("loadData",[]);
};
billomrecheck.searchBoxDetail = function(){
	var recheckNo = $('#recheckNo').val();
	if(recheckNo == '' || recheckNo == null){
		alert("请先保存复核单据!");
		return;
	}
	var rows = $("#order_dtl2_dataGrid").datagrid('getRows');
	billomrecheck.boxDatagridData = rows;
	var queryParams = convertArray($("#boxSearchForm").serializeArray());
	queryParams = eval("("+queryParams+")");
	var queryMxURL=BasePath+ '/bill_om_recheck_dtl/list.json';
	var extParam={"recheckNo":recheckNo,"locno":billomrecheck.user.locno};
	$.extend(queryParams,extParam);
	billomrecheck.loadGridDataUtil('order_dtl2_dataGrid', queryMxURL, queryParams);
	
};
//查询分货单里面待复核的商品信息
billomrecheck.queryRecheckBoxItem = function(){
	var queryRecheckBoxItemURL=BasePath+ '/bill_om_recheck/queryRecheckBoxItem';
	var divideNo = $("#divideNo").val();
	var locno = $("#locno").val();
	var storeNo = $('#storeNo').combobox("getValue");
	if(!divideNo || !storeNo ||!locno){
		alert("请选择分货单号和客户！！");
		return;
	}
	var ueryRecheckBoxItemqueryParams={"divideNo":divideNo,"storeNo":storeNo,"locno":locno};
	billomrecheck.loadGridDataUtil('order_dtl1_dataGrid', queryRecheckBoxItemURL, ueryRecheckBoxItemqueryParams);
};
billomrecheck.disableCombox = function(){
	
	$('#storeNo').combobox('disable');
	
//	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text").attr("readOnly",true);
//	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text+ span span").removeClass("combo-arrow");
//	$("#divideMainInfoForm input[id=storeNo]+ span").css("background-color","#EBEBE4");
//	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text").css("background-color","#EBEBE4");
};
billomrecheck.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
//保存主表信息
billomrecheck.saveMainInfo = function(){
	var url = BasePath+'/bill_om_recheck/saveMainInfo';
    if($('#divideMainInfoForm').form('validate')){
    	  $('#divideMainInfoForm').form('submit', {
  			url: url,
  			onSubmit: function(param){
  				param.creator = billomrecheck.user.loginName;
  				param.creatorname = billomrecheck.user.username;
  				param.editor = billomrecheck.user.loginName;
  				param.editorname = billomrecheck.user.username;
  				//param.locno = billomrecheck.user.locno;
  				
  				//验证是否存在明细数据
  				var tempObj = $('#order_dtl1_dataGrid');
  				var divideDtlRow = tempObj.datagrid('getRows');
  				if(divideDtlRow.length < 1){
  					alert("明细不能为空!");
  					return;
  				}
  				
  				var inserted = tempObj.datagrid('getChanges', "inserted");
  				var datas = JSON.stringify(inserted);
  				param.datas = datas;
  				wms_city_common.loading("show");
  			},
  			success: function(r){
  				wms_city_common.loading();
  				r = billomrecheck.parseJsonStringToJsonObj(r);
  				if(r.result == "success"){
  					var divideNoInput = $("#divideNo");
  					divideNoInput.attr("readonly","readonly");
  					//divideNoInput.find("+ input+ input").attr("disabled",true);
  					//$("#divideMainInfoForm input[name=storeNo]").combobox({"disabled":true});
  					billomrecheck.disableCombox();
  					$("#divideMainInfoForm input[name=recheckNo]").val(r.recheckNo);
  					//$("#saveBtn").attr("disabled",true);
  					$('#saveBtn').hide();
  					
//  					var queryRecheckBoxItemURL=BasePath+ '/bill_om_recheck/queryRecheckBoxItem';
//  					var divideNo = $("#divideNo").val();
//  					var locno = billomrecheck.user.locno;
//  					var storeNo = $('#storeNo').combobox("getValue");
//  					if(!divideNo || !storeNo ||!locno){
//  					}else{
//  						var ueryRecheckBoxItemqueryParams={"divideNo":divideNo,"storeNo":storeNo,"locno":locno};
//  						billomrecheck.loadGridDataUtil('order_dtl1_dataGrid', queryRecheckBoxItemURL, ueryRecheckBoxItemqueryParams);
//  					}
  					$('#selectDivideBtn').linkbutton('disable');
  					 alert('保存成功!');
  					 return;
  				}else{
  					 alert(r.msg);
  				}
  				
  		    },
  			error:function(){
  				wms_city_common.loading();
  				alert('保存失败,请联系管理员!',2);
  			}
  	   });
    }
  
    
};
billomrecheck.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//初始化状态
billomrecheck.initStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECITY_CHECK_STATUS',
			{},
			billomrecheck.status,
			null);
};

billomrecheck.itemFormatter = function(value, rowData, rowIndex){
	if(rowData && rowData.item){
		return rowData.item[this.field];
	}
	return value;
};
billomrecheck.packNumFormatter = function(value, rowData, rowIndex){
	return "";
};
billomrecheck.numFormatter = function(value, rowData, rowIndex){
	return rowData.itemQty - rowData.realQty;
};
billomrecheck.realQtyFormatter = function(value, rowData, rowIndex){
	if(value){
		return value;
	}else{
		return '';
	}
	
};
billomrecheck.status = {"10":"未处理","11":"建单","13":"复核完成","14":"已交接"};
billomrecheck.statusFormatter = function(value, rowData, rowIndex){
	return billomrecheck.status[value];
};
//搜索数据
billomrecheck.searchData = function(){
	var createTmStart =  $('#startCreatetmCondition').datebox('getValue');
	var createTmEnd =  $('#endCreatetmCondition').datebox('getValue');
	if(!isStartEndDate(createTmStart,createTmEnd)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	
	var outstockDateStart =  $('#startAudittmCondition').datebox('getValue');
	var outstockDateEnd =  $('#endAudittmCondition').datebox('getValue');
	if(!isStartEndDate(outstockDateStart,outstockDateEnd)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    }
	
	var startExpDateCondition =  $('#startExpDateCondition').datebox('getValue');
	var endExpDateCondition =  $('#endExpDateCondition').datebox('getValue');
	if(!isStartEndDate(startExpDateCondition,endExpDateCondition)){    
		alert("复核日期开始日期不能大于结束日期");   
		return;   
	}
	
	 //1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_recheck/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billomrecheck.user.locno;
	reqParam['recheckType'] = "0";
	billomrecheck.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
};

//清除查询条件
billomrecheck.searchClear = function(){
	$('#searchForm').form("clear");
};
//清除查询条件
billomrecheck.clearFormById = function(id){
	$('#'+id).form("clear");
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
//取箱号
billomrecheck.getConLabelNo = function(){
	var locno = billomrecheck.user.locno;
	var callback = function(r){
		$("#checkboxsearchForm input[name=boxNo]").val(r.strOutLabelNo);
	};
	 billomrecheck.ajaxRequest(BasePath+'/bill_om_recheck/getLabelNo',{"locno":locno},true,callback);
};
//复核装箱
billomrecheck.packageBox = function(){
	
	//验证明细数据是否录入
    var tempFlag = billomrecheck.endEdit('check_box_dataGrid');
	if(tempFlag!=""){
		alert(tempFlag);
		return;
	}
	
	var tempFlagStr = billomrecheck.checkDetailIsPass('check_box_dataGrid');
	if(tempFlagStr!=""){
		alert(tempFlagStr);
		return;
	}
	
	var rows = $("#check_box_dataGrid").datagrid('getRows');
	wms_city_common.loading("show");
	var packageboxcallback = function(r){
		wms_city_common.loading();
		if(r.success=="true"){
			alert("装箱成功!");
			billomrecheck.queryRecheckBoxItem();//1,查询加载待复核的商品信息
			$("#check_box_dialog").window('close');//2,关闭窗口
		}else{
			alert('装箱失败,原因:'+r.msg,2);
		}
		
//		r = billomrecheck.parseJsonStringToJsonObj(r);
//		if(r && r.success){
//			alert("装箱成功!");
//			billomrecheck.queryRecheckBoxItem();//1,查询加载待复核的商品信息
//			$("#check_box_dialog").window('close');//2,关闭窗口
//		}else{
//			alert('装箱失败,请联系管理员!',2);
//		}
		
	};
	rows = encodeURIComponent(JSON.stringify(rows));
	var boxNo = $("#checkboxsearchForm input[name=boxNo]").val();
//	if(boxNo && boxNo!=''){
		var params = {  "datas":rows,
						"boxNo":boxNo,
						"recheckName":$('#recheckName').val(),
						"storeNo":$('#storeNo').combobox("getValue"),
						"recheckNo":$("#recheckNo").val(),
						"divideNo":$("#divideNo").val(),
						"locno":billomrecheck.user.locno
		};
		billomrecheck.ajaxRequest(BasePath+'/bill_om_recheck/packagebox',params,true,packageboxcallback);
//	}else{
//		alert("请输入箱号，或取新箱号！");
//	}
	
};


//删除
billomrecheck.doDel = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	var tipMessage = "";
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		if(item.status!='10'){
        			tipMessage += item.recheckNo+",";
        		}
        		keyStr[keyStr.length] = {locno:billomrecheck.user.locno,recheckNo:item.recheckNo};
        	});   
        	
        	//删除提示
        	if(tipMessage!=""){
        		tipMessage = tipMessage.substring(0, tipMessage.length-1);
        		alert("单据:【"+tipMessage+"】已操作的复核单不能删除!");
        		return;
        	}
        	
            //2.绑定数据
            var url = BasePath+'/bill_om_recheck/deleteBillOmRecheck';
        	var data={
        	    "datas":JSON.stringify(keyStr)
        	};
        	//3. 删除
        	wms_city_common.loading("show");
        	ajaxRequest(url,data,function(r){
        		wms_city_common.loading();
        		if(r.success=="true"){
        			alert("删除成功!");
        			$("#mainDataGrid").datagrid('load');
        		}else{
        			alert('删除失败,原因:'+r.msg,2);
        		}
        	}); 
        }  
    });  
};


//验证明细数据是否验证通过
billomrecheck.validateEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		tempObj.datagrid('endEdit', i);
    	}else{
    		alert('数据验证没有通过!',1);
    		return false;
    	}
    }
    return true;
};

//验证验收数量是否大于0
billomrecheck.checkDetailUpdateIsPass = function(listDatas){
	var tipStr = "";
	$.each(listDatas, function(index, item){
		if(item.realQty == "0"){
			tipStr = "箱号："+item.scanLabelNo + ",商品："+item.itemNo + ",尺码："+item.sizeNo+"实际数量必须大于0！";
			return;
		}
		if(item.realQty > item.itemQty){
			tipStr = "箱号："+item.scanLabelNo + ",商品："+item.itemNo + ",尺码："+item.sizeNo+"实际数量不能大于计划数量！";
			return;
		}
	});
	return tipStr;
};

//保存明细
billomrecheck.updateOmRecheckDtl = function(){
	var tempFlag = billomrecheck.validateEdit("order_dtl2_dataGrid");
	if(!tempFlag){
		return;
	}
	var tempObj = $('#order_dtl2_dataGrid');
	var updated = tempObj.datagrid('getChanges', "updated");
	if(updated.length < 1){
		alert("没有要更新的明细!");
		return;
	}
	
	//验证新增的列
	var addTipStr = billomrecheck.checkDetailUpdateIsPass(updated);
    if(addTipStr != ""){
		alert(addTipStr);
		return;
	}
    
    $.messager.confirm("确认","你确定要保存明细?", function (r){  
    	if(r){
    		wms_city_common.loading("show","正在保存明细......");
    		$.ajax({
    			  async : true,
    			  cache: true,
    			  type: 'POST',
    			  url: BasePath+'/bill_om_recheck_dtl/updateOmRecheckDtl',
    			  data: {
    				locno:billomrecheck.user.locno,
    				recheckNo:$("#recheckNo").val(),
    				divideNo:$("#divideNo").val(),
    				storeNo:$("#storeNo").combobox('getValue'),
    				editor:billomrecheck.user.loginName,
    				editorname:billomrecheck.user.username,    				
    			  	updated:JSON.stringify(updated)
    			  },
    			  success: function(data){
    			  	 if(data.result=='success'){
    			  		alert("保存成功");
    			  		$("#order_dtl2_dataGrid").datagrid('load');
    					$("#order_dtl2_dataGrid").datagrid("acceptChanges");
    			 	}else{
    				 	alert(data.msg,2);
    			 	}
    			  	wms_city_common.loading();
    			  }
    		});
    	}
    });
};

//删除明细
billomrecheck.deleteOmRecheckDtl = function(){
	
//	var obj = $("#order_dtl2_dataGrid");
//	var checkedRows2 = obj.datagrid('getRows');
//	$.each(checkedRows2, function(index, item) {
//		var rowIndex = obj.datagrid('getRowIndex', item);
//		obj.datagrid('endEdit', rowIndex);
//	});
	
	var checkedRows = $("#order_dtl2_dataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	
    $.messager.confirm("确认","你确定要删除选中的"+checkedRows.length+"明细?", function (r){  
    	if(r){
    		var keyStr = [];
    		$.each(checkedRows, function(index, item) {
    			keyStr[keyStr.length] = {
    				locno : billomrecheck.user.locno,
    				recheckNo : item.recheckNo,
    				scanLabelNo : item.scanLabelNo,
    				containerNo : item.containerNo,
    				itemNo : item.itemNo,
    				sizeNo : item.sizeNo,
    				rowId : item.rowId
    			};
    		}); 
    		wms_city_common.loading("show","正在删除明细......");
    		$.ajax({
    			  async : true,
    			  cache: true,
    			  type: 'POST',
    			  url: BasePath+'/bill_om_recheck_dtl/deleteOmRecheckDtl',
    			  data: {
    				locno:billomrecheck.user.locno,
    				recheckNo:$("#recheckNo").val(),
    				divideNo:$("#divideNo").val(),
    				storeNo:$("#storeNo").combobox('getValue'),
    			  	deleted:JSON.stringify(keyStr)
    			  },
    			  success: function(data){
    			  	 if(data.result=='success'){
    			  		alert("删除成功");
    			  		$("#order_dtl2_dataGrid").datagrid('load');
    					$("#order_dtl2_dataGrid").datagrid("acceptChanges");
    			 	}else{
    				 	alert(data.msg,2);
    			 	}
    			  	wms_city_common.loading();
    			  }
    		});
    	}
    });
    
	
};


//切换选项卡刷新单据查询的dataGrid

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
billomrecheck.owner = {};
billomrecheck.ownerFormatter = function(value, rowData, rowIndex){
	return billomrecheck.owner[value];
};
billomrecheck.initOwner = function(){
	var url = BasePath + '/entrust_owner/get_biz';
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: url,
        data: {},
        success: function(result){
        	var temp = {};
        	for(var i=0;i<result.length;i++){
        		temp[result[i].ownerNo] = result[i].ownerName;
        	}
        	billomrecheck.owner = temp;
		}
      });
};
// JS初始化执行
$(document).ready(function(){
	billomrecheck.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(u){
		billomrecheck.user=u;
		billomrecheck.initCheckUser(u.locno);
	});
	
	//总计
	$('#order_dtl1_dataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					billomrecheck.itemQty = data.footer[1].itemQty;
					billomrecheck.realQty = data.footer[1].realQty;
					billomrecheck.diffQty = data.footer[1].diffQty;
					billomrecheck.packageNoRecheckQty = data.footer[1].packageNoRecheckQty;
				} else {
					var rows = $('#order_dtl1_dataGrid').datagrid('getFooterRows');
					rows[1]['itemQty'] = billomrecheck.itemQty;
					rows[1]['realQty'] = billomrecheck.realQty;
					rows[1]['diffQty'] = billomrecheck.diffQty;
					rows[1]['packageNoRecheckQty'] = billomrecheck.packageNoRecheckQty;
					$('#order_dtl1_dataGrid').datagrid('reloadFooter');
				}
			}
			
		}
	});
	
	$('#order_dtl2_dataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					billomrecheck.itemQty2 = data.footer[1].itemQty;
					billomrecheck.realQty2 = data.footer[1].realQty;
				} else {
					var rows = $('#order_dtl2_dataGrid').datagrid('getFooterRows');
					rows[1]['itemQty'] = billomrecheck.itemQty2;
					rows[1]['realQty'] = billomrecheck.realQty2;
					$('#order_dtl2_dataGrid').datagrid('reloadFooter');
				}
			}
			
		}
	});
	
	//总计
	$('#check_box_dataGrid_rf').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					billomrecheck.itemQty3 = data.footer[1].itemQty;
					billomrecheck.realQty3 = data.footer[1].realQty;
				} else {
					var rows = $('#check_box_dataGrid_rf').datagrid('getFooterRows');
					rows[1]['itemQty'] = billomrecheck.itemQty3;
					rows[1]['realQty'] = billomrecheck.realQty3;
					$('#check_box_dataGrid_rf').datagrid('reloadFooter');
				}
			}
			
		}
	});
	
	$("#check_box_dataGrid").datagrid({
		onSelect:function(rowIndex, rowData){
			var columnOption  = $("#check_box_dataGrid").datagrid('getColumnOption','packageNum');
			columnOption.editor.options.min = 0;  
            columnOption.editor.options.max = rowData.itemQty - rowData.realQty; 
            
            var rows = $("#check_box_dataGrid").datagrid('getRows');
            for(var i=0;i<rows.length;i++){
            	  $("#check_box_dataGrid").datagrid('endEdit',i);
            }
            $("#check_box_dataGrid").datagrid('beginEdit',rowIndex);
		}
	});
	
	billomrecheck.initStatus();
	wms_city_common.closeTip('showDetailDialog');
	billomrecheck.initOwner();
	
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('input[id=brandNo]', $('#searchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs);
	
	billomrecheck.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=UM_PRINT_TYPE',{},false,billomrecheck.initPrintType);
	
	$('#main_order_dtlId').tabs({
		onSelect:function(title,index){
			if(index == 0){
				if($('#recheckNo').val()!=''){
					$('#order_dtl1_dataGrid').datagrid('load');
				}
			}
		}
	});  

	$('#startCreatetmCondition').datebox('setValue',getDateStr(-2));

	
});
billomrecheck.onLoadSuccess = function(data){
	if(data.rows.length > 0){
		if (data.footer[1].isselectsum) {
			billomrecheck.itemQtyMain = data.footer[1].itemQty;
			billomrecheck.realQtyMain = data.footer[1].realQty;
			billomrecheck.packageQtyMain = data.footer[1].packageQty;
		} else {
			var rows = $('#mainDataGrid').datagrid('getFooterRows');
			rows[1]['itemQty'] = billomrecheck.itemQtyMain;
			rows[1]['realQty'] = billomrecheck.realQtyMain;
			rows[1]['packageQty'] = billomrecheck.packageQtyMain;
			$('#mainDataGrid').datagrid('reloadFooter');
		}
	}
}
//打印(箱)
billomrecheck.printDetailShowBox = function(){
	var resultData;
	var recheckNo = $("#recheckNo").val();
	var rows = $('#mainDataGrid').datagrid('getChecked');
	if (rows.length == 0) {
		alert('请选择需要打印的数据!');
		return;
	}
	var keys = [];
	for ( var i = 0, length = rows.length; i < length; i++) {
		keys.push(rows[i].locno + "|" + rows[i].recheckNo + "|" + rows[i].storeNo);
	}
	$.ajax({
		cache : false,
		async : false,
		type : 'POST',
		url : BasePath + '/bill_om_recheck_dtl/printDetailShowBox',
		data : {
			keys : keys.join("_")
		},
		success : function(result) {
			resultData = result;
		}
	});
	if(resultData.result!="success"){
     	alert(resultData.msg);
     	return;
     }else{
		LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
		if (LODOP == null) {
			return;
		}
		LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
		var result = resultData.data;
		for(var i=0,length=result.length;i<length;i++){
			LODOP.NewPage();
	     	var html = billomrecheck.getHtmlByTemplateShowBox(result[i]);
			LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
			LODOP.ADD_PRINT_BARCODE(10,10,250,40,"128A",result[i].recheckNo);
		}
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
		LODOP.PREVIEW();
     }
}
billomrecheck.getHtmlByTemplateShowBox = function(data){
	var sizeNos = data.sizeCols;
	var boxNo;
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+data.recheckNo+"</td><td>发货方："+data.sender+"</td><td>收货方："+data.receipterName+"("+data.receipter+")</td><td>总合计："+data.total+"</td></tr>";
	html += "</table>";
	html += "<table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'><tr bgcolor='#fff'>";
	html += "<td>序号</td><td>箱号</td><td>商品编号</td><td>颜色</td><td>商品名称</td>";
	for(var index=0,length=sizeNos.length;index<length;index++){
		html += "<td>"+sizeNos[index]+"</td>";
	}
	html += "<td>合计</td></tr>";
	for(var i = 0;length=data.rows.length,i<length;i++){
		html+="<tr bgcolor='#fff'>";
			html+="<td>"+(i+1)+"</td>";//序号
			html+="<td>"+(data.rows[i].boxNo==null?"":data.rows[i].boxNo)+"</td>";
			html+="<td>"+data.rows[i].itemNo+"</td>";//商品编码
			html+="<td>"+(data.rows[i].colorName==null?"":data.rows[i].colorName)+"</td>";//颜色
			html+="<td>"+data.rows[i].itemName+"</td>";//名称
			//尺码
			for(var index=0,length=sizeNos.length;index<length;index++){
				if(data.rows[i][sizeNos[index]]!=null){
					html += "<td>"+data.rows[i][sizeNos[index]]+"</td>";
				}else{
					html += "<td>&nbsp;</td>";
				}
			}
			html+="<td>"+data.rows[i].allCount+"</td>";//合计
		html+"</tr>";
		
	}
	html+="</table>";
	//显示签收制单人
	html += "<table style='width:100%;'>";
	html += "<tr><td>签收：</td><td style='text-align:right;padding-right:50px;'>制单人："+data.creator+"&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+data.creattm+"</td></tr>";
	html += "</table>";
	return html;
};
// 打印
billomrecheck.printDetail = function(showBox){
	
	var resultData;
	var recheckNo = $("#recheckNo").val();
	var rows = $('#mainDataGrid').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].locno+"|"+rows[i].recheckNo+"|"+rows[i].divideNo+"|"+rows[i].storeNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_om_recheck_dtl/printDetail',
        data: {
        	keys:keys.join(",")
        },
        success: function(result){
        	resultData = result;
		}
      });
     if(resultData.result!="success"){
     	alert(resultData.msg);
     	return;
     }else{
    	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
      	if(LODOP==null){
      		return;
      	}
 		LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
		var result = resultData.data;
		for(var i=0,length=result.length;i<length;i++){
			LODOP.NewPage();
	     	var html = billomrecheck.getHtmlByTemplate(result[i],result[i].recheckNo,result[i].receipter,result[i].totalAllCount,showBox);
			LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
			LODOP.ADD_PRINT_BARCODE(10,10,250,40,"128A",result[i].recheckNo);
		}
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
		LODOP.PREVIEW();
     }
};
billomrecheck.initPrintType = function(data){
	for(var idx=0;idx<data.length;idx++){
		billomrecheck.printType[data[idx].itemvalue] = data[idx].itemname;
	}
};
billomrecheck.printType = {};
billomrecheck.getPrintTypeCh = function(printType){
	return billomrecheck.printType[printType];
}
billomrecheck.printByBoxNew = function(){
	var rows = $('#order_dtl2_dataGrid').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	//除掉重复的箱号
	var boxNoMap = {};
	var boxNoArray = [];
	for(var idx=0;idx<rows.length;idx++){
		if(boxNoMap[rows[idx].scanLabelNo] == null){
			boxNoArray[boxNoArray.length] = rows[idx].scanLabelNo;
			boxNoMap[rows[idx].scanLabelNo] = rows[idx].scanLabelNo;
		}
	}
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
  	if(LODOP==null){
  		return;
  	}
  	LODOP.SET_PRINT_PAGESIZE(1,'600','500',"");
  	for(var idx=0;idx<boxNoArray.length;idx++){
  		var html = billomrecheck.getBoxHtmlTemplateNew(boxNoArray[idx]);
  		LODOP.NewPage();
  		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
  		LODOP.ADD_PRINT_BARCODE(35,26,195,45,"128A",boxNoArray[idx]);
  	}
  	LODOP.PREVIEW();
};
billomrecheck.getBoxHtmlTemplateNew = function(boxNo){
	var printType = boxNo.substring(billomrecheck.user.locno.length, billomrecheck.user.locno.length+2);
	var printTypeCh = billomrecheck.getPrintTypeCh(printType);
	if(printTypeCh != null){
		printTypeCh += '&nbsp;';
	}else{
		printTypeCh = '';
	}
	var html = "<table border='0' cellpadding='1' cellspacing='1' style='height:100%;width:100%;'>"+
				"<tr style='text-align:right;height:12px;font-weight:bold;;font-size:13px;'>"+
					"<td>"+billomrecheck.user.locname+"&nbsp;"+printTypeCh+"装箱标签</td>"+
				"</tr>"+
				"<tr>"+
					"<td>&nbsp;</td>"+
				"</tr>"+
			"</table>";
	return html;
}
billomrecheck.printByBox = function(noneDtl){
	var rows = $('#order_dtl2_dataGrid').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var recheckNo = $('#recheckNo').val();
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].scanLabelNo);
	}
	var url = BasePath+'/bill_om_recheck_dtl/printByBox?noneDtl='+noneDtl;
	wms_city_common.loading("show");
	var params = {
			locno:billomrecheck.user.locno,
			recheckNo:recheckNo,
			keys:keys.join(",")
	};
	var resultData;
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: url,
        data: params,
        success: function(result){
        	wms_city_common.loading();
        	resultData = result;
		}
      });
	if(resultData.result!=null){
     	alert(resultData.result);
     	return;
     }else{
    	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
      	if(LODOP==null){
      		return;
      	}
      	LODOP.SET_PRINT_PAGESIZE(1,'780','1100',"");
     	var htmls = resultData.html;
     	for(var i=0,length=htmls.length;i<length;i++){
     		LODOP.NewPage();
     		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",htmls[i].table);
    		LODOP.ADD_PRINT_BARCODE(198,20,235,55,"128A",htmls[i].scanLabelNo);
    		LODOP.ADD_PRINT_BARCODE(335,20,235,55,"128A",htmls[i].scanLabelNo);
    		//LODOP.SET_PRINT_STYLEA(0,"Horient",2);//水平居中
    		
    		//LODOP.SET_PRINT_STYLEA(0, "ShowBarText", 0);
     	}
		LODOP.PREVIEW();
     }
};


billomrecheck.getHtmlByTemplate = function(data,recheckNo,receipter,totalAllCount,showBox){
	var sizeNos = data.sizeCols;
	var boxNo;
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+recheckNo+"</td><td>发货方："+data.sender+"</td><td>收货方："+data.receipterName+"("+receipter+")</td><td>总合计："+totalAllCount+"</td></tr>";
	html += "</table>";
	html += "<table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'><tr bgcolor='#fff'>";
	html += "<td>序号</td>"+(showBox?"<td>箱号</td>":"")+"<td>商品编号</td><td>颜色</td><td>名称</td>";
	for(var index=0,length=sizeNos.length;index<length;index++){
		html += "<td>"+sizeNos[index]+"</td>";
	}
	html += "<td>合计</td></tr>";
	for(var i = 0;length=data.rows.length,i<length;i++){
		html+="<tr bgcolor='#fff'>";
			html+="<td>"+(i+1)+"</td>";//序号
			if(showBox){
				html+="<td>"+(data.rows[i].boxNo==null?"":data.rows[i].boxNo)+"</td>";
			}
			html+="<td>"+data.rows[i].itemNo+"</td>";//商品编码
			html+="<td>"+(data.rows[i].colorName==null?"":data.rows[i].colorName)+"</td>";//颜色
			html+="<td>"+data.rows[i].itemName+"</td>";//名称
			//尺码
			for(var index=0,length=sizeNos.length;index<length;index++){
				if(data.rows[i][sizeNos[index]]!=null){
					html += "<td>"+data.rows[i][sizeNos[index]]+"</td>";
				}else{
					html += "<td>&nbsp;</td>";
				}
			}
			html+="<td>"+data.rows[i].allCount+"</td>";//合计
		html+"</tr>";
		/*if(data.rows[i].boxNo!=null){
			if(i==length-1){
				boxNo = boxNo+data.rows[i].boxNo;
			}else{
				boxNo = boxNo+data.rows[i].boxNo+",";
			}
		}*/
		
	}
	//拼接箱号
	/*if(boxNo!=null){
		html +="<tr bgcolor='#fff'><td colspan="+(sizeNos.length+5)+">"+boxNo+"</td></tr>";
	}*/
	html+="</table>";
	//显示签收制单人
	html += "<table style='width:100%;'>";
	html += "<tr><td>签收：</td><td style='text-align:right;padding-right:50px;'>制单人："+data.creator+"&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+data.creattm+"</td></tr>";
	html += "</table>";
	return html;
};
//加载品牌库
billomrecheck.loadSysNo = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			$('#sysNoSearch').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150
			});
		}
	});
};


billomrecheck.checkDetailIsPass = function(id){
	var tipStr = "";
	var rows = $('#'+id).datagrid('getRows'); 
	$.each(rows, function(index, item){
		if(item.packageNum == "0" || item.packageNum == "" || item.packageNum == null){
			tipStr =  "商品："+item.itemNo + "尺码："+item.sizeNo+"装箱数量必须大于0且不能为空!";
			return;
		}
		if(item.packageNum > item.itemQty){
			tipStr =  "商品："+item.itemNo + "尺码："+item.sizeNo+"装箱数量不能大于计划数量!";
			return;
		}
	});
	return tipStr;
};



//尺码横排打印
billomrecheck.preColNamesDtl1 = [
     		                   {title:"商品编码",field:"itemNo",width:150}
                         ];
billomrecheck.endColNamesDtl1 = [
     		                      {title:"合计",field:"allCount"}
                             ] ;
billomrecheck.sizeTypeFiledName = 'sizeKind';


//选择A4还是A5打印
billomrecheck.printA4orA5 = function(){
	$('#printMenu').menu('show', {    
		  left: 365,    
		  top: 35
	});
};
billomrecheck.printBoxA4orA5 = function(){
	$('#printBoxMenu').menu('show', {    
		  left: 450,    
		  top: 35
	});
};
//打印预览
billomrecheck.printDetail4SizeHorizontal = function(item){
	var recheckNo = $("#recheckNo").val();
	var rows = $('#mainDataGrid').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	wms_city_common.loading("show","正在加载......");
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].locno+"|"+rows[i].recheckNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_om_recheck_dtl/printDetail4SizeHorizontal',
        data: {
        	keys:keys.join(","),
 	        preColNames:JSON.stringify(billomrecheck.preColNamesDtl1),
 	        endColNames:JSON.stringify(billomrecheck.endColNamesDtl1),
 	        sizeTypeFiledName:billomrecheck.sizeTypeFiledName
        },
        success: function(data){
        	wms_city_common.loading();
        	if(data.result=="success"){
        		print4SizeHorizontal(data,"",item.name);
        	}else{
        		alert(data.msg,2);
        	}
        	
		}
    });
};
function print4SizeHorizontal(data,type,size){
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
  	if(LODOP==null){
  		return;
  	}
  	
  	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
	if (size =="A4"){
		LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
	}else if(size =="A5"){
		LODOP.SET_PRINT_PAGESIZE(2,0,0,"A5");
	}
	var result = data.rows;
	for(var i=0,length=result.length;i<length;i++){
		LODOP.NewPage();
		var html = "";
		if(type=="box"){
			html = getHtml4Box(strStyle,result[i],size);
		}else{
			
			html = getHtml(strStyle,result[i],size);
			
		}
//		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
//		LODOP.ADD_PRINT_BARCODE(10,10,250,40,"128A",result[i].main.recheckNo);
	}
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
}

//获取数据表头
billomrecheck.getHtmlByTemplateHead = function(data){
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
	html += "<tr style='font-size:13px;'><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+data.main.recheckNo+"</td><td>发货方："+data.main.sender+"</td><td>收货方："+data.main.receipterName+"("+data.main.receipter+")</td><td>总合计："+data.main.sumQty+"</td></tr>";
	html += "</table>";
	return html;
};


//获取数据内容
billomrecheck.getHtmlByTemplateBody = function(data){
	var html = "<table border='0' cellpadding='1' cellspacing='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'><thead>";
	var titleinfo = data.header;
	var rowInfo = data.rows;
	var footer = data.footer[0];
	//最大的尺码数量
	var maxType = titleinfo.maxType;
	//组装标题信息，包括所有的尺码，标题
	for(var i=0,length=titleinfo.returnCols.length;i<length;i++){
		html = html+"<tr bgcolor='#fff' style='font-size:13px;'>";
		for(var j=0,length2=titleinfo.returnCols[i].length;j<length2;j++){
			 var td = titleinfo.returnCols[i][j];
			 var title = td.title==""?"&nbsp;":td.title;
			html = html+"<td width="+td.width+" rowspan="+td.rowspan+" colspan="+td.colspan+">"+title+"</td>";
		}
		html =html+ "</tr>";
	}
	html = html + "</thead>";
	//组装数据信息
	var rows = rowInfo;
	for(var k=0,length = rows.length;k<length;k++){
		var temdata = rows[k];
		html = html+"<tr bgcolor='#fff' style='font-size:13px;'><td>"+temdata.itemNo+"</td><td>"+temdata.sizeKind+"</td>";
		for(var n=1;n<=maxType;n++){
			if(temdata["v"+n]!=undefined){
				html =html+"<td>"+temdata["v"+n]+"</td>";
			}else{
				html =html+"<td></td>";
			}
		}
		html = html+"<td>"+temdata.allCount+"</td></tr>";
	}
	//组装合计信息
	html = html+"<tr bgcolor='#fff' style='font-size:13px;'><td>"+footer.itemNo+"</td><td></td>";
	for(var n=1;n<=maxType;n++){
			if(footer["v"+n]!=undefined){
				html =html+"<td>"+footer["v"+n]+"</td>";
			}else{
				html =html+"<td></td>";
			}
		}
	html = html+"<td>"+footer.allCount+"</td></tr>";
	html = html + "<tfoot><tr><td style='text-align:13px;text-align:center;' colspan='"+(maxType+3)+"'><font tdata='pageNO' format = '#' align='center'>第#页</font>&nbsp;/&nbsp;<font format='#' tdata='pageCount'>共#页</font></td></tr></tfoot>";
	html = html+"</table>";
	return html;
};

//页脚
billomrecheck.getHtmlByTemplateFooter = function(data){
	var html = "<table style='width:100%;'>";
	html =html+ "<tr><td>签收：</td><td style='text-align:right;padding-right:50px;'>制单人："+data.main.creator+"&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+data.main.creattm+"</td></tr>";
	html =html+ "</table>";
	return html;
};


function getHtml(strStyle,data,size){
	
	/**********************增加测试数据S**********************/
	/*var rows = data.rows;
	for(var idx=0;idx<200;idx++){
		rows[rows.length] = rows[0];
	}
	data.rows = rows;*/
	/**********************增加测试数据E**********************/
	var totalHeight = 1050;//总的高度
	var headHeight = 85; //
	var bottomHeight = 50; //底部高度
	var spaceHeight = 5;//头部与表格之间的间隙
	var tableHeight = 0; //表格的高度
	if(size=="A4"){
		totalHeight = 1000;//A4页面高度
	}else if(size=="A5"){
		totalHeight = 450;//A5页面高度
	}
	tableHeight = totalHeight - headHeight - 2 * spaceHeight - bottomHeight;//表格的高度
	var headHtml =  billomrecheck.getHtmlByTemplateHead(data);
	var bodyHtml = billomrecheck.getHtmlByTemplateBody(data);
	var footerHtml = billomrecheck.getHtmlByTemplateFooter(data);
	
	//设置表格内容
	LODOP.ADD_PRINT_TABLE(headHeight+spaceHeight,0,"100%",tableHeight,strStyle+bodyHtml);
	
	//设置表格头部
	LODOP.ADD_PRINT_HTM(0,0,"100%",headHeight,headHtml);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	
	//设置表格底部
	LODOP.ADD_PRINT_HTM(totalHeight+bottomHeight,0,"100%",bottomHeight,footerHtml);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);	
	
 	//设置条码
	LODOP.ADD_PRINT_BARCODE(15,10,250,40,"128A",data.main.recheckNo);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	LODOP.NewPageA();
	
//	var html = "<table style='width:100%;'>";
//	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
//	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+data.main.recheckNo+"</td><td>发货方："+data.main.sender+"</td><td>收货方："+data.main.receipterName+"("+data.main.receipter+")</td><td>总合计："+data.main.sumQty+"</td></tr>";
//	html += "</table>";
//	
//	html  = html+ "<table table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'>";
//	var titleinfo = data.header;
//	var rowInfo = data.rows;
//	var footer = data.footer[0];
//	//最大的尺码数量
//	var maxType = titleinfo.maxType;
//	//组装标题信息，包括所有的尺码，标题
//	for(var i=0,length=titleinfo.returnCols.length;i<length;i++){
//		html = html+"<tr>";
//		for(var j=0,length2=titleinfo.returnCols[i].length;j<length2;j++){
//			 var td = titleinfo.returnCols[i][j];
//			 var title = td.title==""?"&nbsp;":td.title;
//			html = html+"<td width="+td.width+" rowspan="+td.rowspan+" colspan="+td.colspan+">"+title+"</td>";
//		}
//		html =html+ "</tr>";
//	}
//	//组装数据信息
//	var rows = rowInfo;
//	for(var k=0,length = rows.length;k<length;k++){
//		var temdata = rows[k];
//		html = html+"<tr><td>"+temdata.itemNo+"</td><td>"+temdata.sizeKind+"</td>";
//		for(var n=1;n<=maxType;n++){
//			if(temdata["v"+n]!=undefined){
//				html =html+"<td>"+temdata["v"+n]+"</td>";
//			}else{
//				html =html+"<td></td>";
//			}
//		}
//		html = html+"<td>"+temdata.allCount+"</td></tr>";
//	}
//	//组装合计信息
//	html = html+"<tr><td>"+footer.itemNo+"</td><td></td>";
//	for(var n=1;n<=maxType;n++){
//			if(footer["v"+n]!=undefined){
//				html =html+"<td>"+footer["v"+n]+"</td>";
//			}else{
//				html =html+"<td></td>";
//			}
//		}
//	html = html+"<td>"+footer.allCount+"</td></tr>";	
//	html = html+"</table>";
//	
//	html =html+ "<table style='width:100%;'>";
//	html =html+ "<tr><td>签收：</td><td style='text-align:right;padding-right:50px;'>制单人："+data.main.creator+"&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+data.main.creattm+"</td></tr>";
//	html =html+ "</table><style>tr{background:#fff}</style>";
//	return html;
}

//打印预览箱
//尺码横排打印
billomrecheck.preColNamesDtl1_box= [
     		                   {title:"箱号",field:"itemNo",width:150},{title:"商品编码",field:"itemNo",width:150}
                         ];
billomrecheck.endColNamesDtl1_box = [
     		                      {title:"合计",field:"allCount"}
                             ] ;
billomrecheck.sizeTypeFiledName_box = 'sizeKind';
billomrecheck.printDetailBox4SizeHorizontal = function(item){
	var recheckNo = $("#recheckNo").val();
	var rows = $('#mainDataGrid').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	wms_city_common.loading("show","正在加载......");
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].locno+"|"+rows[i].recheckNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_om_recheck_dtl/printDetailBox4SizeHorizontal',
        data: {
        	keys:keys.join(","),
 	        preColNames:JSON.stringify(billomrecheck.preColNamesDtl1_box),
 	        endColNames:JSON.stringify(billomrecheck.endColNamesDtl1_box),
 	        sizeTypeFiledName:billomrecheck.sizeTypeFiledName_box
        },
        success: function(data){
        	wms_city_common.loading();
        	if(data.result=="success"){
        		print4SizeHorizontal(data,"box",item.name);
        	}else{
        		alert(data.msg,2);
        	}
        	
		}
    });
};



function getHtml4Box(strStyle,data,size){
	/**********************增加测试数据S**********************/
	/*var rows = data.rows;
	for(var idx=0;idx<200;idx++){
		rows[rows.length] = rows[0];
	}
	data.rows = rows;*/
	/**********************增加测试数据E**********************/
	
	var totalHeight = 1050;//总的高度
	var headHeight = 85; //
	var bottomHeight = 50; //底部高度
	var spaceHeight = 5;//头部与表格之间的间隙
	var tableHeight = 0; //表格的高度
	if(size=="A4"){
		totalHeight = 1000;//A4页面高度
	}else if(size=="A5"){
		totalHeight = 450;//A5页面高度
	}
	tableHeight = totalHeight - headHeight - 2 * spaceHeight - bottomHeight;//表格的高度
	var headHtml =  billomrecheck.getHtmlByTemplateHeadByBox(data);
	var bodyHtml = billomrecheck.getHtmlByTemplateBodyByBox(data);
	var footerHtml = billomrecheck.getHtmlByTemplateFooterByBox(data);
	
	//设置表格内容
	LODOP.ADD_PRINT_TABLE(headHeight+spaceHeight,0,"100%",tableHeight,strStyle+bodyHtml);
	
	//设置表格头部
	LODOP.ADD_PRINT_HTM(0,0,"100%",headHeight,headHtml);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	
	//设置表格底部
	LODOP.ADD_PRINT_HTM(totalHeight+bottomHeight,0,"100%",bottomHeight,footerHtml);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);	
	
 	//设置条码
	LODOP.ADD_PRINT_BARCODE(15,10,250,40,"128A",data.main.recheckNo);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	LODOP.NewPageA();
	
//	var html = "<table style='width:100%;'>";
//	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
//	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+data.main.recheckNo+"</td><td>发货方："+data.main.sender+"</td><td>收货方："+data.main.receipterName+"("+data.main.receipter+")</td><td>总合计："+data.main.sumQty+"</td></tr>";
//	html += "</table>";
//	html  = html+ "<table table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'>";
//	var titleinfo = data.header;
//	var rowInfo = data.rows;
//	var footer = data.footer[0];
//	//最大的尺码数量
//	var maxType = titleinfo.maxType;
//	//组装标题信息，包括所有的尺码，标题
//	for(var i=0,length=titleinfo.returnCols.length;i<length;i++){
//		html = html+"<tr>";
//		for(var j=0,length2=titleinfo.returnCols[i].length;j<length2;j++){
//			 var td = titleinfo.returnCols[i][j];
//			 var title = td.title==""?"&nbsp;":td.title;
//			html = html+"<td width="+td.width+" rowspan="+td.rowspan+" colspan="+td.colspan+">"+title+"</td>";
//		}
//		html =html+ "</tr>";
//	}
//	//组装数据信息
//	var rows = rowInfo;
//	for(var k=0,length = rows.length;k<length;k++){
//		var temdata = rows[k];
//		html = html+"<tr><td>"+temdata.boxNo+"</td><td>"+temdata.itemNo+"</td><td>"+temdata.sizeKind+"</td>";
//		for(var n=1;n<=maxType;n++){
//			if(temdata["v"+n]!=undefined){
//				html =html+"<td>"+temdata["v"+n]+"</td>";
//			}else{
//				html =html+"<td></td>";
//			}
//		}
//		html = html+"<td>"+temdata.allCount+"</td></tr>";
//	}
//	//组装合计信息
//	html = html+"<tr><td>"+footer.itemNo+"</td><td></td><td></td>";
//	for(var n=1;n<=maxType;n++){
//			if(footer["v"+n]!=undefined){
//				html =html+"<td>"+footer["v"+n]+"</td>";
//			}else{
//				html =html+"<td></td>";
//			}
//		}
//	html = html+"<td>"+footer.allCount+"</td></tr>";	
//	html = html+"</table>";
//	html =html+ "<table style='width:100%;'>";
//	html =html+ "<tr><td>签收：</td><td style='text-align:right;padding-right:50px;'>制单人："+data.main.creator+"&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+data.main.creattm+"</td></tr>";
//	html =html+ "</table><style>tr{background:#fff}</style>";
//	return html;
}

//获取数据表头
billomrecheck.getHtmlByTemplateHeadByBox = function(data){
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
	html += "<tr style='font-size:13px;'><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+data.main.recheckNo+"</td><td>发货方："+data.main.sender+"</td><td>收货方："+data.main.receipterName+"("+data.main.receipter+")</td><td>总合计："+data.main.sumQty+"</td></tr>";
	html += "</table>";
	return html;
};


//获取数据内容
billomrecheck.getHtmlByTemplateBodyByBox = function(data){
	var html = "<table border='0' cellpadding='1' cellspacing='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'><thead>";
	var titleinfo = data.header;
	var rowInfo = data.rows;
	var footer = data.footer[0];
	//最大的尺码数量
	var maxType = titleinfo.maxType;
	//组装标题信息，包括所有的尺码，标题
	for(var i=0,length=titleinfo.returnCols.length;i<length;i++){
		html = html+"<tr bgcolor='#fff' style='font-size:13px;'>";
		for(var j=0,length2=titleinfo.returnCols[i].length;j<length2;j++){
			 var td = titleinfo.returnCols[i][j];
			 var title = td.title==""?"&nbsp;":td.title;
			html = html+"<td width="+td.width+" rowspan="+td.rowspan+" colspan="+td.colspan+">"+title+"</td>";
		}
		html =html+ "</tr>";
	}
	html = html + "</thead>";
	//组装数据信息
	var rows = rowInfo;
	for(var k=0,length = rows.length;k<length;k++){
		var temdata = rows[k];
		html = html+"<tr bgcolor='#fff' style='font-size:13px;'><td>"+temdata.boxNo+"</td><td>"+temdata.itemNo+"</td><td>"+temdata.sizeKind+"</td>";
		for(var n=1;n<=maxType;n++){
			if(temdata["v"+n]!=undefined){
				html =html+"<td>"+temdata["v"+n]+"</td>";
			}else{
				html =html+"<td></td>";
			}
		}
		html = html+"<td>"+temdata.allCount+"</td></tr>";
	}
	//组装合计信息
	html = html+"<tr><td>"+footer.itemNo+"</td><td></td><td></td>";
	for(var n=1;n<=maxType;n++){
			if(footer["v"+n]!=undefined){
				html =html+"<td>"+footer["v"+n]+"</td>";
			}else{
				html =html+"<td></td>";
			}
		}
	html = html+"<td>"+footer.allCount+"</td></tr>";	
	html = html + "<tfoot><tr><td style='text-align:13px;text-align:center;' colspan='"+(maxType+4)+"'><font tdata='pageNO' format = '#' align='center'>第#页</font>&nbsp;/&nbsp;<font format='#' tdata='pageCount'>共#页</font></td></tr></tfoot>";
	html = html+"</table>";
	return html;
};

//页脚
billomrecheck.getHtmlByTemplateFooterByBox = function(data){
	var html = "<table style='width:100%;'>";
	html =html+ "<tr><td>签收：</td><td style='text-align:right;padding-right:50px;'>制单人："+data.main.creator+"&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+data.main.creattm+"</td></tr>";
	html =html+ "</table>";
	return html;
};