
var outstockrecheck = {};
outstockrecheck.user;
outstockrecheck.recheckNo;
outstockrecheck.boxDatagridData;
outstockrecheck.statusData;

//单据状态
outstockrecheck.status = {
	"10":"建单",
	"13":"拣货完成"
};

//========================委托业主========================
outstockrecheck.ownerNo = {};
outstockrecheck.ownerNoFormatter = function(value, rowData, rowIndex){
	return outstockrecheck.ownerNo[value];
};
outstockrecheck.initOwnerNo = function(data){
	$('#ownerNo').combobox({
		valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto"
	});
	outstockrecheck.ownerNo = outstockrecheck.tansforOwner(data);
};
outstockrecheck.tansforOwner = function(data){
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

//初始化拣货单状态
outstockrecheck.initOutStockStatus = function(){
	outstockrecheck.statusData=outstockrecheck.converStr2JsonObj2(outstockrecheck.status);
};

//格式化
outstockrecheck.outStockStatusFormatter = function(value,rowData,rowIndex){
	return outstockrecheck.statusData[value];
};

//加载Grid数据Utils
outstockrecheck.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};



//创建分货单
outstockrecheck.showAddDialog = function(){
	
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
	$("#recheckNo").css('background','#D4D0C8');
	$("#recheckNo").css('color','#808080');
	//var divideNoInput = $("#divideNo");
	//divideNoInput.attr("readonly",false);
	//divideNoInput.find("+ input+ input").attr("disabled",false);
	
//	$('#saveBtn').linkbutton('enable');
//	$('#checkBoxBtn').linkbutton('enable');
	outstockrecheck.btnBtn("add",0);
	
	$("#selectOutstockBtn").attr("disabled",false);
	
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
		rows[0]['recheckQty'] = 0;
		rows[1]['itemQty'] = 0;
		rows[1]['realQty'] = 0;
		rows[1]['diffQty'] = 0;
		rows[1]['recheckQty'] = 0;
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
outstockrecheck.closeWindow = function(id){
	$('#'+id).window('close');  
};

//验证编辑行
outstockrecheck.endEdit = function(gid){
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
            		if(packageNum.target.val() <= 0) {
                		return "商品"+itemNo+":装箱数量不能小于等于0;";
            		} else if(packageNum.target.val() > itemQty){
                		$(packageNum.target).focus();
                		return "商品"+itemNo+":装箱数量不能大于可分货数量;";
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
outstockrecheck.selectOutstock=function(){
	
	var checkedRows = $("#outstock_no_dataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择分货任务单!',1);
		return;
	}
	
	$.each(checkedRows, function(index, item){
		outstockrecheck.setDivideNo(item,'add');
	});
	
};

//打开复核装箱界面
outstockrecheck.opencheckbox = function(){
//	var checkRows = $("#mainDataGrid").datagrid('getChecked');
//	if(checkRows[0].status=='11'){
//		alert("该单没有审核，不能装箱，请选审核！");
//		return;
//	}
	var tempObj = $('#check_box_dataGrid');
	var rows = tempObj.datagrid('getRows');
	if(rows){
		for ( var i = 0,length=rows.length; i < length; i++) {
			var rowIndex = tempObj.datagrid('getRowIndex', rows[i]);
			tempObj.datagrid('deleteRow', rowIndex);
		}
	}
	
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
//	var dataList = [];
	$.each(selectRows, function(index, item){
		
		if(item.realQty == "0"){
			tipStr = '【商品'+item.itemNo+",尺码"+item.sizeNo+"】"+"未拣货,不能复装箱!";
			return false;
		}
		
		if(item.recheckQty >= item.realQty){
			tipStr = '【商品'+item.itemNo+",尺码"+item.sizeNo+"】"+"已经装箱完成,不能再次装箱!";
			return false;
		}
		
		var params = {
				serialNo:item.serialNo,
				packQty:item.packQty,
				expNo:item.expNo,
				expType:item.expType,
				expDate:item.expDate,
				ownerNo:item.ownerNo,
				lineNo:item.lineNo,
				divideId:item.divideId,
				itemNo:item.itemNo,
				itemName:item.itemName,
				itemQty:item.realQty-item.recheckQty,
				packageNum:item.realQty-item.recheckQty,
				colorName:item.colorName,
				sizeNo:item.sizeNo,
				scanLabelNo:item.scanLabelNo,
				outstockNo:item.outstockNo
		};
//		dataList[dataList.length] = params;
		outstockrecheck.insertRowAtEnd("check_box_dataGrid",params);
	});
	if(tipStr!=""){
		alert(tipStr);
		return;
	} else {
		$("#check_box_dialog").window('open');
	}
	
//	$("#check_box_dataGrid").datagrid('loadData',dataList1);
	
	var selectRows = $("#check_box_dataGrid").datagrid('getRows');
	$.each(selectRows, function(index, item){
		$("#check_box_dataGrid").datagrid('beginEdit',index);
	});
	
};
outstockrecheck.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
	return  tempIndex;
};
outstockrecheck.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
	return  tempIndex;
};

//打开分货单选择界面
outstockrecheck.opendividenoselect = function(){
	var queryMxURL=BasePath+ '/bill_om_outstock_recheck/findBillOmOutStock';
	var queryParams={locno:outstockrecheck.user.locno};
	outstockrecheck.loadGridDataUtil('outstock_no_dataGrid', queryMxURL, queryParams);
	$("#outstock_no_dialog").window('open');
};

outstockrecheck.outstockQuery = function(){
	var queryMxURL=BasePath+ '/bill_om_outstock_recheck/findBillOmOutStock';
	var fromObjStr=convertArray($('#locateNoSearchForm').serializeArray());
	var queryParams= eval("("+fromObjStr+")");
	queryParams['locno'] = outstockrecheck.user.locno;
	outstockrecheck.loadGridDataUtil('outstock_no_dataGrid', queryMxURL, queryParams);
	$("#outstock_no_dialog").window('open');
};
outstockrecheck.setDivideNo = function(rowData,type){
	$('#order_dtl1_dataGrid').datagrid('loadData', { total: 0, rows: [] });
	$("#recheckNo").val(rowData.recheckNo);
	if(type == "add"){
		$("#locateNo").val(rowData.locateNo);
	}else{
		$("#locateNo").val(rowData.divideNo);
	}
	$("#locno").val(rowData.locno);
	$("#expDate").val(rowData.expDate);
	$("#lineNo").val(rowData.lineNo);
	$('#sourceTypeHid').val(rowData.sourceType);
	//$("#serialNo").val(rowData.serialNo);
	
	//$("#recheckName").val(rowData.creator);
	outstockrecheck.initStoreByDivideNo(rowData,type);
	$('#divideMainInfoForm input[id=storeNo]').combobox('setValue',rowData.storeNo);
	$("#outstock_no_dialog").window('close');
};
//初始客户选择下拉框
outstockrecheck.initStoreByDivideNo = function(rowData,type){
	var locateNo = rowData.locateNo;
	var locno = rowData.locno;
	var selectType = 0;
	if(type == "add"){
		selectType = 1;
		$('#storeNo').combobox('reload',BasePath+'/bill_om_outstock_recheck/selectStoreFromOutStockDtl?locateNo='+locateNo+'&locno='+locno+"&selectType="+selectType);
	}else{
		outstockrecheck.selectStoreNoToOutstockDtl(rowData);
		$('#storeNo').combobox('reload',BasePath+'/store/get_biz?storeNo='+rowData.storeNo);
	}
	
};

//选择客户执行
outstockrecheck.selectStoreNoToOutstockDtl = function(data){
	//转货复核
	var locno = outstockrecheck.user.locno;
	if(data.sourceType == '1'||data.sourceType == '2'||data.sourceType == '3'){
		var queryURL=BasePath+ '/bill_om_outstock_recheck/findConvertRecheck.json';
		var queryParams={"locno":locno,"recheckNo":data.recheckNo};
		outstockrecheck.loadGridDataUtil('order_dtl1_dataGrid', queryURL, queryParams);
	}else{
		var queryURL=BasePath+ '/bill_om_outstock_recheck/queryRecheckOutstockItem';
		var locateNo = $("#locateNo").val();
		var storeNo = data.storeNo;
		if(locateNo!=""&&locno!=""&&storeNo!=""){
			var queryParams={"locateNo":locateNo,"storeNo":storeNo,"locno":locno};
			outstockrecheck.loadGridDataUtil('order_dtl1_dataGrid', queryURL, queryParams);
		}
	}
};

outstockrecheck.initCheckUser = function(locno){
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
outstockrecheck.checkUserSelect = function(){
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
		
		$('#checkUser').combobox('setValue',outstockrecheck.user.loginName);
		
		$("#check_user_dialog").window('open');
	}
};
outstockrecheck.check = function(){
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
			checkIds.push(checkRows[i].recheckNo+"-"+outstockrecheck.user.locno);
		}
		var url = BasePath+'/bill_om_outstock_recheck/check';
		var data = {ids:checkIds.join(","),checkUser:checkUser};
		wms_city_common.loading("show","正在审核......");
		outstockrecheck.ajaxRequest(url,data,true,function(r){
			
			if(r.success=="true"){
				alert("审核成功!");
				$("#mainDataGrid").datagrid('load');//1,查询加载待复核的商品信息
			}else{
				alert('审核失败,原因:'+r.msg,2);
			}
			
//			 if(result=='success'){
//				 $("#mainDataGrid").datagrid('load');
//				 alert('审核成功!');
//				
//			 }else{
//				 alert('审核失败,请联系管理员!',2);
//			 }
			wms_city_common.loading();
		}); 
		
		
		$("#check_user_dialog").window('close');
	}else{
		alert("请选择复核人！");
		return;
	}
	
	
};
outstockrecheck.editInfo = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status != '10'){
		alert('只能修改【新建】状态的记录!',1);
		return;
	}else{
		var count = outstockrecheck.checkIsRfPackage(checkedRows[0]);
		if(count > 0){
			outstockrecheck.openPackageBoxRf(checkedRows[0]);
		}else{
			outstockrecheck.loadDetail(checkedRows[0],"edit");
		}
	}
};
outstockrecheck.btnBtn = function(type,sourceType){
	$("#saveBtn").hide();
	$("#checkBoxBtn").hide();
	$("#printDetail").show();
	$('#btn-add-dtl').hide();
	$('#btn-del-dtl').hide();
	$('#btn-save-dtl').hide();
	if(type=="edit"){
		$("#printDetail").hide();
		$("#btn-add-dtl").show();
		$('#btn-del-dtl').show();
		$('#btn-save-dtl').show();
		if(sourceType == "0"){
			$("#checkBoxBtn").show();
			$("#btn-add-dtl").hide();
		}
	}else if(type=="add"){
		$("#saveBtn").show();
		$("#printDetail").hide();
		$('#btn-del-dtl').show();
		$('#btn-save-dtl').show();
		$("#btn-add-dtl").hide();
	}
};

//加载订单详情
outstockrecheck.loadDetail = function(rowData,type){
	$("#divideMainInfoForm").form("clear");
	outstockrecheck.setDivideNo(rowData,type);
	var divideNoInput = $("#locateNo");
	divideNoInput.attr("readonly","readonly");
	$("#selectOutstockBtn").attr("disabled",true);
	outstockrecheck.btnBtn(type,rowData.sourceType);
	$("#recheckNo").css('background','#D4D0C8');
	$("#recheckNo").css('color','#808080');
	
	outstockrecheck.recheckNo = rowData.recheckNo;
	outstockrecheck.disableCombox();
	$("#showDetailDialog").window('open');
	//outstockrecheck.queryRecheckBoxItem();
	
	var queryMxURL=BasePath+ '/bill_om_recheck_dtl/list.json';
	var queryParams={recheckNo:rowData.recheckNo,locno:rowData.locno};
	outstockrecheck.loadGridDataUtil('order_dtl2_dataGrid', queryMxURL, queryParams);
	
	
};
outstockrecheck.clearBoxDetail = function(){
	$("#boxSearchForm").form('clear');
	$("#order_dtl2_dataGrid").datagrid('loadData',outstockrecheck.boxDatagridData);
	
};
outstockrecheck.clearSearch = function(){
	$("#searchForm").form('clear');
	$('#brandNo').combobox("loadData",[]); 
};
outstockrecheck.searchBoxDetail = function(){
	var recheckNo = $('#recheckNo').val();
	if(recheckNo == '' || recheckNo == null){
		alert("请先保存复核单据!");
		return;
	}
	var rows = $("#order_dtl2_dataGrid").datagrid('getRows');
	outstockrecheck.boxDatagridData = rows;
	var queryParams = convertArray($("#boxSearchForm").serializeArray());
	queryParams = eval("("+queryParams+")");
	var queryMxURL=BasePath+ '/bill_om_recheck_dtl/list.json';
	var extParam={"recheckNo":recheckNo,"locno":outstockrecheck.user.locno};
	$.extend(queryParams,extParam);
	outstockrecheck.loadGridDataUtil('order_dtl2_dataGrid', queryMxURL, queryParams);
	
};
//查询分货单里面待复核的商品信息
outstockrecheck.queryRecheckBoxItem = function(){
	var queryRecheckBoxItemURL=BasePath+ '/bill_om_outstock_recheck/queryRecheckOutstockItem';
	var divideNo = $("#locateNo").val();
	//var locno = $("#locno").val();
	var storeNo = $('#storeNo').combobox("getValue");
	if(!divideNo || !storeNo){
		alert("请选择分货单号和客户！！");
		return;
	}
	var ueryRecheckBoxItemqueryParams={"locateNo":divideNo,"storeNo":storeNo,"locno":outstockrecheck.user.locno};
	outstockrecheck.loadGridDataUtil('order_dtl1_dataGrid', queryRecheckBoxItemURL, ueryRecheckBoxItemqueryParams);
};
outstockrecheck.disableCombox = function(){
	
	$('#storeNo').combobox('disable');
	
//	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text").attr("readOnly",true);
//	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text+ span span").removeClass("combo-arrow");
//	$("#divideMainInfoForm input[id=storeNo]+ span").css("background-color","#EBEBE4");
//	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text").css("background-color","#EBEBE4");
};
outstockrecheck.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
outstockrecheck.saveMainInfo = function(){
    var url = BasePath+'/bill_om_outstock_recheck/saveMainInfo';
    if($('#divideMainInfoForm').form('validate')){
    	 wms_city_common.loading("show","正在保存......");
    	  $('#divideMainInfoForm').form('submit', {
  			url: url,
  			dataType:'json',
  			onSubmit: function(param){
  				param.creator = outstockrecheck.user.loginName;
  				param.creatorname = outstockrecheck.user.username;
  				param.editor = outstockrecheck.user.loginName;
  				param.editorname = outstockrecheck.user.username;
  				//param.locno = outstockrecheck.user.locno;
  				
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
  				
  			},
  			success: function(r){
  				wms_city_common.loading();
  				r = outstockrecheck.parseJsonStringToJsonObj(r);
  				if(r && r.success=='true'){
  					var locateNoInput = $("#locateNo");
  					locateNoInput.attr("readonly","readonly");
  					$("#selectOutstockBtn").attr("disabled",true);
  					outstockrecheck.disableCombox();
  					$("#divideMainInfoForm input[name=recheckNo]").val(r.recheckNo);
  					outstockrecheck.btnBtn("edit",0);
  					alert('保存成功!');
  				}else{
  					var status=r.status;
  					if(status=='1'){
  						 alert("保存失败!该拣货波次单已存在建单状态的复核单。",2);
  					}else{
  						 alert("保存失败！",2);
  					}
  				}
  		    },
  			error:function(){
  				alert('保存失败,请联系管理员!',2);
  			}
  	   });
    }
  
    
};
outstockrecheck.ajaxRequest = function(url,reqParam,async,callback){
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
outstockrecheck.initStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECITY_CHECK_STATUS',
			{},
			outstockrecheck.status,
			null);
};

//单据类型
outstockrecheck.initSourceType = function(){
	wms_city_common.comboboxLoadFilter(
			["sourceTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECHECK_SOURCE_TYPE',
			{},
			null,
			null);
};

outstockrecheck.itemFormatter = function(value, rowData, rowIndex){
	if(rowData && rowData.item){
		return rowData.item[this.field];
	}
	return value;
};
outstockrecheck.packNumFormatter = function(value, rowData, rowIndex){
	return "";
};
outstockrecheck.numFormatter = function(value, rowData, rowIndex){
	return rowData.itemQty - rowData.realQty;
};
outstockrecheck.realQtyFormatter = function(value, rowData, rowIndex){
	if(value){
		return value;
	}else{
		return '';
	}
	
};
outstockrecheck.status = {"10":"未处理","11":"建单","13":"复核完成","14":"已交接"};
outstockrecheck.statusFormatter = function(value, rowData, rowIndex){
	return outstockrecheck.status[value];
};
//搜索数据
outstockrecheck.searchData = function(){
	
	 //1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
    
    var startCreatetm =  $('#startCreatetmCondition').datebox('getValue');
	var endCreatetm =  $('#endCreatetmCondition').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
       return;   
   }   
	
	var startAudittm =  $('#startAudittmCondition').datebox('getValue');
	var endAudittm =  $('#endAudittmCondition').datebox('getValue');
	if(!isStartEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
       return;   
   } 
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_outstock_recheck/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = outstockrecheck.user.locno;
	reqParam['recheckType'] = "1";
	outstockrecheck.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
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

//清除查询条件
outstockrecheck.searchClear = function(){
	$('#searchForm').form("clear");
};
outstockrecheck.searchClear2 = function(){
	$('#locateNoCon').val('');
	$('#createTmStart').val('');
	$('#createTmEnd').val('');
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
outstockrecheck.getConLabelNo = function(){
	var locno = outstockrecheck.user.locno;
	var callback = function(r){
		$("#checkboxsearchForm input[name=boxNo]").val(r.strOutLabelNo);
	};
	 outstockrecheck.ajaxRequest(BasePath+'/bill_om_outstock_recheck/getLabelNo',{"locno":locno},true,callback);
};
//复核装箱
outstockrecheck.packageBox = function(){
	
	//验证明细数据是否录入
    var tempFlag = outstockrecheck.endEdit('check_box_dataGrid');
	if(tempFlag!=""){
		alert(tempFlag);
		return;
	}
	
	var tipStr = "";
	var selectRows = $("#check_box_dataGrid").datagrid('getRows');
	$.each(selectRows, function(index, item){
		if(item.packageNum <= 0){
			tipStr = '【商品'+item.itemNo+",尺码"+item.sizeNo+"】"+"装箱数量不能小于等于0!";
			return false;
		}
		if(item.packageNum > item.itemQty){
			tipStr = '【商品'+item.itemNo+",尺码"+item.sizeNo+"】"+"装箱数量不能大于计划数量!";
			return false;
		}
	});
	
	if(tipStr!=""){
		alert(tipStr);
		return false;
	}
	
	var rows = $("#check_box_dataGrid").datagrid('getRows');
	var packageboxcallback = function(r){
		wms_city_common.loading();
		if(r.success=="true"){
			alert("装箱成功!");
			outstockrecheck.queryRecheckBoxItem();//1,查询加载待复核的商品信息
			$("#check_box_dialog").window('close');//2,关闭窗口
		}else{
			alert('装箱失败,原因:'+r.msg,2);
		}
		
//		r = outstockrecheck.parseJsonStringToJsonObj(r);
//		if(r && r.success){
//			alert("装箱成功!");
//			outstockrecheck.queryRecheckBoxItem();//1,查询加载待复核的商品信息
//			$("#check_box_dialog").window('close');//2,关闭窗口
//		}else{
//			alert('装箱失败,请联系管理员!',2);
//		}
		
	};
	
	rows = encodeURIComponent(JSON.stringify(rows));
	var params = {"datas":rows,
			"boxNo":'N',
			"storeNo":$('#storeNo').combobox("getValue"),
			"recheckNo":$("#recheckNo").val(),
			"divideNo":$("#locateNo").val(),
			"locno":outstockrecheck.user.locno
	};
	wms_city_common.loading("show","正在装箱......");
	outstockrecheck.ajaxRequest(BasePath+'/bill_om_outstock_recheck/packagebox',params,true,packageboxcallback);
	
//	var boxNo = $("#checkboxsearchForm input[name=boxNo]").val();
//	if(boxNo && boxNo!=''){
//		var params = {  "datas":rows,
//						"boxNo":'N',
//						"recheckName":$('#recheckName').val(),
//						"storeNo":$('#storeNo').combobox("getValue"),
//						"recheckNo":$("#recheckNo").val(),
//						"divideNo":$("#divideNo").val(),
//						"locno":outstockrecheck.user.locno
//		};
//		outstockrecheck.ajaxRequest(BasePath+'/bill_om_outstock_recheck/packagebox',params,true,packageboxcallback);
//	}else{
//		alert("请输入箱号，或取新箱号！");
//	}
	
};


//删除
outstockrecheck.doDel = function(){
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
        		keyStr[keyStr.length] = {locno:outstockrecheck.user.locno,recheckNo:item.recheckNo};
        	});   
        	
        	//删除提示
        	if(tipMessage!=""){
        		tipMessage = tipMessage.substring(0, tipMessage.length-1);
        		alert("单据:【"+tipMessage+"】已操作的复核单不能删除!");
        		return;
        	}
        	
            //2.绑定数据
            var url = BasePath+'/bill_om_outstock_recheck/deleteBillOmRecheck';
        	var data={
        	    "datas":JSON.stringify(keyStr)
        	};
        	wms_city_common.loading("show","正在删除......");
        	//3. 删除
        	ajaxRequest(url,data,function(r){
        		if(r.success=="true"){
        			alert("删除成功!");
        			$("#mainDataGrid").datagrid('load');
        		}else{
        			alert('删除失败,原因:'+r.msg,2);
        		}
        		wms_city_common.loading();
        	}); 
        }  
    });  
};


//验证是否需要RF封箱
outstockrecheck.checkIsRfPackage = function(rowData){
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
outstockrecheck.openPackageBoxRf = function(rowData){
	$("#check_box_dialog_rf").window('open');
	$('#check_box_dataGrid_rf').datagrid('loadData', { total: 0, rows: [] });
	$("#recheckNoRf").val(rowData.recheckNo);
	$("#divideNoRf").val(rowData.divideNo);
	$('#storeNoRf').combobox('reload',BasePath+'/store/get_biz?storeNo='+rowData.storeNo);
	$("#storeNoRf").combobox('setValue',rowData.storeNo);
	var queryMxURL=BasePath+ '/bill_om_recheck_dtl/dtl_list.json';
	var queryParams={recheckNo:rowData.recheckNo,locno:rowData.locno,containerNo : 'N'};
	outstockrecheck.loadGridDataUtil('check_box_dataGrid_rf', queryMxURL, queryParams);
};

//rf封箱
outstockrecheck.packageBoxRf = function(rowData){
	var packageboxcallback = function(r) {
		if (r.success == "true") {
			alert("装箱成功!");
			$("#check_box_dialog_rf").window('close');// 2,关闭窗口
			$('#check_box_dataGrid_rf').datagrid('load');
			$("#mainDataGrid").datagrid('load');
		} else {
			alert('装箱失败,原因:' + r.msg,1);
		}
	};
	
	$.messager.confirm("确认","你确定要对RF未完成的数据进行封箱?", function (r){ 
		if(!r){
			return;
		}
		var params = {
				"storeNo" : $('#storeNoRf').combobox("getValue"),
				"recheckNo" : $("#recheckNoRf").val(),
				"divideNo" : $("#divideNoRf").val(),
				"locno" : outstockrecheck.user.locno
			};
			outstockrecheck.ajaxRequest(BasePath + '/bill_om_outstock_recheck/packageBoxOutstockRf',
					params, true, packageboxcallback);
	});
	
};


// JS初始化执行
$(document).ready(function(){
	outstockrecheck.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(u){
		outstockrecheck.user=u;
		outstockrecheck.initCheckUser(u.locno);
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
	
	//总计
	$('#order_dtl1_dataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					outstockrecheck.itemQty = data.footer[1].itemQty;
					outstockrecheck.realQty = data.footer[1].realQty;
					outstockrecheck.diffQty = data.footer[1].diffQty;
					outstockrecheck.recheckQty = data.footer[1].recheckQty;
					outstockrecheck.packageNoRecheckQty = data.footer[1].packageNoRecheckQty;
				} else {
					var rows = $('#order_dtl1_dataGrid').datagrid('getFooterRows');
					rows[1]['itemQty'] = outstockrecheck.itemQty;
					rows[1]['realQty'] = outstockrecheck.realQty;
					rows[1]['diffQty'] = outstockrecheck.diffQty;
					rows[1]['recheckQty'] = outstockrecheck.recheckQty;
					rows[1]['packageNoRecheckQty'] = outstockrecheck.packageNoRecheckQty;
					$('#order_dtl1_dataGrid').datagrid('reloadFooter');
				}
			}else{
				var rows = $('#order_dtl1_dataGrid').datagrid('getFooterRows');
				if(rows!=null){
					rows[1]['itemQty'] = 0;
					rows[1]['realQty'] = 0;
					rows[1]['diffQty'] = 0;
					rows[1]['recheckQty'] = 0;
					rows[1]['packageNoRecheckQty'] = 0;
					$('#order_dtl1_dataGrid').datagrid('reloadFooter');
				}
			}
			
		}
	});
	
	$('#order_dtl2_dataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					outstockrecheck.itemQty2 = data.footer[1].itemQty;
					outstockrecheck.realQty2 = data.footer[1].realQty;
				} else {
					var rows = $('#order_dtl2_dataGrid').datagrid('getFooterRows');
					rows[1]['itemQty'] = outstockrecheck.itemQty2;
					rows[1]['realQty'] = outstockrecheck.realQty2;
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
					outstockrecheck.itemQty3 = data.footer[1].itemQty;
					outstockrecheck.realQty3 = data.footer[1].realQty;
				} else {
					var rows = $('#check_box_dataGrid_rf').datagrid('getFooterRows');
					rows[1]['itemQty'] = outstockrecheck.itemQty3;
					rows[1]['realQty'] = outstockrecheck.realQty3;
					$('#check_box_dataGrid_rf').datagrid('reloadFooter');
				}
			}
			
		}
	});
	
	$('#addRecheckDataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					outstockrecheck.noRecheckQty = data.footer[1].noRecheckQty;
				} else {
					var rows = $('#addRecheckDataGrid').datagrid('getFooterRows');
					rows[1]['noRecheckQty'] = outstockrecheck.noRecheckQty;
					$('#addRecheckDataGrid').datagrid('reloadFooter');
				}
			}
			
		}
	});
	
	
	outstockrecheck.initStatus();
	outstockrecheck.initSourceType();
	outstockrecheck.initOutStockStatus();
	//委托业主
	outstockrecheck.ajaxRequest(BasePath+'/entrust_owner/get_biz',{},false,outstockrecheck.initOwnerNo);
	
	wms_city_common.closeTip('showDetailDialog');
	
	var objs = [];
	objs.push({"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')});
	wms_city_common.loadSysNo4Cascade(objs);
	
	outstockrecheck.initUsers();
	outstockrecheck.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=UM_PRINT_TYPE',{},false,outstockrecheck.initPrintType);
	
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
outstockrecheck.onLoadSuccess = function(data){
	if(data.rows.length > 0){
		if (data.footer[1].isselectsum) {
			outstockrecheck.realQtyMain = data.footer[1].realQty;
			outstockrecheck.recheckQtyMain = data.footer[1].recheckQty;
			outstockrecheck.packageQtyMain = data.footer[1].packageQty;
		} else {
			var rows = $('#mainDataGrid').datagrid('getFooterRows');
			rows[1]['realQty'] = outstockrecheck.realQtyMain;
			rows[1]['recheckQty'] = outstockrecheck.recheckQtyMain;
			rows[1]['packageQty'] = outstockrecheck.packageQtyMain;
			$('#mainDataGrid').datagrid('reloadFooter');
		}
	}
}



//验证明细数据是否验证通过
outstockrecheck.validateEdit = function(gid){
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
outstockrecheck.checkDetailUpdateIsPass = function(listDatas){
//	var checkRows = $('#mainDataGrid').datagrid('getChecked');
//	if(checkRows.length < 1){
//		alert("请选择复核单!");
//		return;
//	}
//	var sourceType = checkRows[0].sourceType;
	
	var tipStr = "";
	$.each(listDatas, function(index, item){
		if(item.realQty == "0"){
			tipStr = "箱号："+item.scanLabelNo + ",商品："+item.itemNo + ",尺码："+item.sizeNo+"实际数量必须大于0！";
			return;
		}
		if(item.sourceType == '0'){
			if(item.realQty > item.itemQty){
				tipStr = "箱号："+item.scanLabelNo + ",商品："+item.itemNo + ",尺码："+item.sizeNo+"实际数量不能大于计划数量！";
				return;
			}
		}
		
	});
	
	return tipStr;
};

//保存明细
outstockrecheck.updateOmOutstockRecheckDtl = function(){
	
	var tempFlag = outstockrecheck.validateEdit("order_dtl2_dataGrid");
	if(!tempFlag){
		return;
	}
	var tempObj = $('#order_dtl2_dataGrid');
	var inserted = tempObj.datagrid('getChanges', "inserted");
//	var updated = tempObj.datagrid('getChanges', "updated");
//	if(updated.length < 1&&inserted.length<1){
//		alert("没有要更新的明细!");
//		return;
//	}
//	
//	//验证新增的列
//	var addTipStr = "";
//	addTipStr = outstockrecheck.checkDetailUpdateIsPass(inserted);
//	addTipStr = outstockrecheck.checkDetailUpdateIsPass(updated);
//	if(addTipStr != ""){
//		alert(addTipStr);
//		return;
//	}
    
    $.messager.confirm("确认","你确定要保存明细?", function (r){  
    	if(r){
    		if(inserted.length > 0){
    			$('#boxNo').val('');
    			$("#package_box_dialog").window('open');
    			return false;
    		}
    		outstockrecheck.querySaveDtl();
    		
//    		wms_city_common.loading("show","正在保存明细......");
//    		$.ajax({
//    			  async : true,
//    			  cache: true,
//    			  type: 'POST',
//    			  url: BasePath+'/bill_om_recheck_dtl/updateOmOutstockRecheckDtl',
//    			  data: {
//    				locno:outstockrecheck.user.locno,
//    				recheckNo:$("#recheckNo").val(),
//    				divideNo:$("#locateNo").val(),
//    				storeNo:$("#storeNo").combobox('getValue'),
//    			  	updated:JSON.stringify(updated)
//    			  },
//    			  success: function(data){
//    			  	 if(data.result=='success'){
//    			  		alert("保存成功");
//    			  		$("#order_dtl2_dataGrid").datagrid('load');
//    			  		$("#mainDataGrid").datagrid('load');
//    					$("#order_dtl2_dataGrid").datagrid("acceptChanges");
//    			 	}else{
//    				 	alert(data.msg,2);
//    			 	}
//    			  	wms_city_common.loading();
//    			  }
//    		});
    	}
    });
};

//确定保存明细
outstockrecheck.querySaveDtl = function(){
	
	var tempFlag = outstockrecheck.validateEdit("order_dtl2_dataGrid");
	if(!tempFlag){
		return;
	}
	var tempObj = $('#order_dtl2_dataGrid');
	var boxNo = $("#boxNo").val();
	var inserted = tempObj.datagrid('getChanges', "inserted");
	var updated = tempObj.datagrid('getChanges', "updated");
	if(updated.length < 1&&inserted.length<1){
		alert("没有要更新的明细!");
		return;
	}
	
	//验证箱号
	if(inserted.length > 0){
		if(boxNo==''||boxNo==null){
			alert("箱号不能为空!");
			return;
		}
	}
	
	//验证新增的列
	var addTipStr = outstockrecheck.checkDetailUpdateIsPass(inserted);
	var updateTipStr = outstockrecheck.checkDetailUpdateIsPass(updated);
	if(addTipStr != ""){
		alert(addTipStr);
		return;
	}
	if(updateTipStr != ""){
		alert(updateTipStr);
		return;
	}
	
	wms_city_common.loading("show","正在保存明细......");
	$.ajax({
		  async : true,
		  cache: true,
		  type: 'POST',
		  url: BasePath+'/bill_om_recheck_dtl/updateOmOutstockRecheckDtl',
		  data: {
			locno:outstockrecheck.user.locno,
			recheckNo:$("#recheckNo").val(),
			divideNo:$("#locateNo").val(),
			storeNo:$("#storeNo").combobox('getValue'),
			boxNo:boxNo,
		  	updated:JSON.stringify(updated),
		  	inserted:JSON.stringify(inserted)
		  },
		  success: function(data){
		  	 if(data.result=='success'){
		  		alert("保存成功");
		  		$("#order_dtl2_dataGrid").datagrid('load');
		  		$("#mainDataGrid").datagrid('load');
				$("#order_dtl2_dataGrid").datagrid("acceptChanges");
		 	}else{
			 	alert(data.msg,2);
		 	}
		  	if(inserted.length > 0){
		  		outstockrecheck.closeWindow('package_box_dialog');
		  	}
		  	wms_city_common.loading();
		  }
	});
};




//删除明细
outstockrecheck.deleteOmOutstockRecheckDtl = function(){
	
	var tempObj = $("#order_dtl2_dataGrid");
	var checkedRows = tempObj.datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	
	var tipStr = "";
	var scanLabelNo = "";
	$.each(checkedRows, function(index, item) {
		if(index > 0){
			if((item.scanLabelNo==''&&scanLabelNo!='')||(item.scanLabelNo!=''&&scanLabelNo=='')){
				tipStr = "新增数据和已有的数据只能选择一种类型删除!";
				return false;
			}
		}else{
			scanLabelNo = item.scanLabelNo;
		}
	}); 
	
	if(tipStr!=''){
		alert(tipStr);
		return false;
	}
	
	
    $.messager.confirm("确认","你确定要删除选中的"+checkedRows.length+"明细?", function (r){  
    	if(r){
    		var keyStr = [];
    		$.each(checkedRows, function(index, item) {
    			if(item.scanLabelNo!=''&&item.scanLabelNo!=null){
    				keyStr[keyStr.length] = {
    	    				locno : outstockrecheck.user.locno,
    	    				recheckNo : item.recheckNo,
    	    				scanLabelNo : item.scanLabelNo,
    	    				containerNo : item.containerNo,
    	    				itemNo : item.itemNo,
    	    				sizeNo : item.sizeNo,
    	    				rowId : item.rowId
    	    		};
    			}else{
    				 var i = tempObj.datagrid('getRowIndex', checkedRows[index]);
    				 tempObj.datagrid('deleteRow', i);
    			}
    		}); 
    		
    		if(keyStr.length > 0){
    			wms_city_common.loading("show","正在删除明细......");
        		$.ajax({
        			  async : true,
        			  cache: true,
        			  type: 'POST',
        			  url: BasePath+'/bill_om_recheck_dtl/deleteOmOutstockRecheckDtl',
        			  data: {
        				locno:outstockrecheck.user.locno,
        				recheckNo:$("#recheckNo").val(),
        				divideNo:$("#locateNo").val(),
        				storeNo:$("#storeNo").combobox('getValue'),
        			  	deleted:JSON.stringify(keyStr)
        			  },
        			  success: function(data){
        			  	 if(data.result=='success'){
        			  		alert("删除成功");
        			  		tempObj.datagrid('load');
        			  		$("#mainDataGrid").datagrid('load');
        			  		tempObj.datagrid("acceptChanges");
        			 	}else{
        				 	alert(data.msg,2);
        			 	}
        			  	wms_city_common.loading();
        			  }
        		});
    		}
    		
    		
    		
    	}
    });
    
	
};


//新增明细
outstockrecheck.addRecheckDtl = function(){
	var recheckNo = $('#recheckNo').val();
	if(recheckNo ==''||recheckNo==null){
		alert("主档为空不能添加明细!");
	}
	$("#add_recheckdtl_dialog").window('open');
	outstockrecheck.searchItem();
};

//查询商品明细
outstockrecheck.searchItem = function(){
	//只有修改才能进入直通复核或者转货复核的单据
	var sourceType = $('#sourceTypeHid').val();
	var divideNo = $('#locateNo').val();
	var fromObjStr=convertArray($('#searchItemForm').serializeArray());
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = outstockrecheck.user.locno;
	reqParam['isRecheckSelect'] = 'Y';
	var queryMxURL = '';
	if(sourceType == '1'||sourceType == '3'){
		queryMxURL = BasePath+'/bill_um_check_dtl/listByPage.json';
		reqParam['checkNo'] = divideNo;
	}else if(sourceType == '2'){
		queryMxURL = BasePath+'/bill_sm_otherin_dtl/dtl_List.json';
		reqParam['otherinNo'] = divideNo;
	}else{
		return false;
	}
	outstockrecheck.loadGridDataUtil('addRecheckDataGrid', queryMxURL, reqParam);
};

//选择商品明细
outstockrecheck.selectItem = function(){
	var checkRows = $('#addRecheckDataGrid').datagrid('getChecked');
	if(checkRows==null||checkRows.length < 1){
		alert("请选择要新增的商品明细!");
		return false;
	}
	$.messager.confirm("确认","你确定要选择这"+checkRows.length+"条数据吗?", function (r){  
		$.each(checkRows,function(index, item){
			var params = {
				locno : item.locno,
				ownerNo : item.ownerNo,
				itemNo : item.itemNo,
				itemName : item.itemName,
				sizeNo : item.sizeNo,
				colorName : item.colorName,
				itemQty : item.noRecheckQty,
				realQty : item.noRecheckQty,
				boxNo : item.boxNo,
				brandNo : item.brandNo,
				scanLabelNo:''
			};
			outstockrecheck.insertRowAtEnd("order_dtl2_dataGrid",params);
		});
		outstockrecheck.closeWindow('add_recheckdtl_dialog');
	});
};

//打印(箱)
outstockrecheck.printDetailShowBox = function(){
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
     	alert(resultData.result);
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
	     	var html = outstockrecheck.getHtmlByTemplateShowBox(result[i]);
			LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
			LODOP.ADD_PRINT_BARCODE(10,10,250,40,"128A",result[i].recheckNo);
		}
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
		LODOP.PREVIEW();
     }
}
outstockrecheck.getHtmlByTemplateShowBox = function(data){
	var sizeNos = data.sizeCols;
	if(sizeNos == null){
		reu
	}
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
//打印
outstockrecheck.printDetail = function(){

	var resultData;
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
        url: BasePath+'/bill_om_outstock_recheck/printDetail',
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
	     	var html = outstockrecheck.getHtmlByTemplate(result[i],result[i].recheckNo,result[i].receipter,result[i].totalAllCount);
			LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
			LODOP.ADD_PRINT_BARCODE(10,10,250,40,"128A",result[i].recheckNo);
		}
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
		LODOP.PREVIEW();
     }
};


outstockrecheck.getHtmlByTemplate = function(data,recheckNo,receipter,totalAllCount){
	var sizeNos = data.sizeCols;
	var boxNo;
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+recheckNo+"</td><td>发货方："+data.sender+"</td><td>收货方："+data.receipterName+"("+receipter+")</td><td>总合计："+totalAllCount+"</td></tr>";
	html += "</table>";
	html += "<table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'><tr bgcolor='#fff'>";
	html += "<td>序号</td><td>商品编号</td><td>颜色</td><td>名称</td>";
	for(var index=0,length=sizeNos.length;index<length;index++){
		html += "<td>"+sizeNos[index]+"</td>";
	}
	html += "<td>合计</td></tr>";
	for(var i = 0;length=data.rows.length,i<length;i++){
		html+="<tr bgcolor='#fff'>";
			html+="<td>"+(i+1)+"</td>";//序号
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
outstockrecheck.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//jiang.ys Start
outstockrecheck.initPrintType = function(data){
	for(var idx=0;idx<data.length;idx++){
		outstockrecheck.printType[data[idx].itemvalue] = data[idx].itemname;
	}
};
outstockrecheck.printType = {};
outstockrecheck.getPrintTypeCh = function(printType){
	return outstockrecheck.printType[printType];
}
outstockrecheck.printByBoxNew = function(){
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
  		var html = outstockrecheck.getBoxHtmlTemplateNew(boxNoArray[idx]);
  		LODOP.NewPage();
  		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
  		LODOP.ADD_PRINT_BARCODE(35,26,195,45,"128A",boxNoArray[idx]);
  	}
  	LODOP.PREVIEW();
};
outstockrecheck.getBoxHtmlTemplateNew = function(boxNo){
	var printType = boxNo.substring(outstockrecheck.user.locno.length, outstockrecheck.user.locno.length+2);
	var printTypeCh = outstockrecheck.getPrintTypeCh(printType);
	if(printTypeCh != null){
		printTypeCh += '&nbsp;';
	}else{
		printTypeCh = '';
	}
	var html = "<table border='0' cellpadding='1' cellspacing='1' style='height:100%;width:100%;'>"+
				"<tr style='text-align:right;height:12px;font-weight:bold;;font-size:13px;'>"+
					"<td>"+outstockrecheck.user.locname+"&nbsp;"+printTypeCh+"装箱标签</td>"+
				"</tr>"+
				"<tr>"+
					"<td>&nbsp;</td>"+
				"</tr>"+
			"</table>";
	return html;
};
//jiang.ys End
outstockrecheck.printByBox = function(noneDtl){
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
	var params = {
			locno:outstockrecheck.user.locno,
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
    		//LODOP.SET_PRINT_STYLEA(0, "ShowBarText", 0);
     	}
		LODOP.PREVIEW();
     }
};


//尺码横排打印
outstockrecheck.preColNamesDtl1 = [
     		                   {title:"商品编码",field:"itemNo",width:150}
                         ];
outstockrecheck.endColNamesDtl1 = [
     		                      {title:"合计",field:"allCount"}
                             ] ;
outstockrecheck.sizeTypeFiledName = 'sizeKind';

outstockrecheck.print = function(text){
	if(text == 'print'){
	    $('#printMenu').menu('show', {    
		  left: 365,    
		  top: 33
		});
	}else if(text == 'printBox'){
	    $('#printBoxMenu').menu('show', {    
		  left: 456,    
		  top: 33
		}); 
	}
	
};

outstockrecheck.printDetail4SizeHorizontal = function(item){
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
 	        preColNames:JSON.stringify(outstockrecheck.preColNamesDtl1),
 	        endColNames:JSON.stringify(outstockrecheck.endColNamesDtl1),
 	        sizeTypeFiledName:outstockrecheck.sizeTypeFiledName
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
  	var dircection = 1;//打印方向
  	if(size == "A5"){
  		dircection = 2;
  	}else if(size == "A4"){
  		dircection = 1;
  	}
  	LODOP.SET_PRINT_PAGESIZE(dircection,0,0,size);
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
outstockrecheck.getHtmlByTemplateHead = function(data){
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
	html += "<tr style='font-size:13px;'><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+data.main.recheckNo+"</td><td>发货方："+data.main.sender+"</td><td>收货方："+data.main.receipterName+"("+data.main.receipter+")</td><td>总合计："+data.main.sumQty+"</td></tr>";
	html += "</table>";
	return html;
};


//获取数据内容
outstockrecheck.getHtmlByTemplateBody = function(data){
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
outstockrecheck.getHtmlByTemplateFooter = function(data){
	var html = "<table style='width:100%;'>";
	html =html+ "<tr><td>签收：</td><td style='text-align:right;padding-right:50px;'>制单人："+data.main.creator+"&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+data.main.creattm+"</td></tr>";
	html =html+ "</table>";
	return html;
};


function getHtml(strStyle,data,size){
	var headHtml =  outstockrecheck.getHtmlByTemplateHead(data);
	var bodyHtml = outstockrecheck.getHtmlByTemplateBody(data);
	var footerHtml = outstockrecheck.getHtmlByTemplateFooter(data);
	var totalHeight = 1100;//总高度
	var headHeight = 85;
	var spaceHeight = 5;
	var tableHeight = 0;//表格高度
	var footerHeight = 50;//底部高度
	if(size == "A4"){
		totalHeight = 1000;
	}else if(size == "A5"){
		totalHeight = 450;
	}
	tableHeight = totalHeight - headHeight - 2 * spaceHeight - footerHeight;//表格的高度

	//设置表格内容
	LODOP.ADD_PRINT_TABLE(headHeight+spaceHeight,0,"100%",tableHeight,strStyle+bodyHtml);
	//LODOP.SET_PRINT_STYLEA(0,"Vorient",3);

	//设置表格头部
	LODOP.ADD_PRINT_HTM(0,0,"100%",headHeight,headHtml);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	
	//设置表格底部
	LODOP.ADD_PRINT_HTM(totalHeight+footerHeight,0,"100%",footerHeight,footerHtml);
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
//	html =html+ "<table style='width:100%;'>";
//	html =html+ "<tr><td>签收：</td><td style='text-align:right;padding-right:50px;'>制单人："+data.main.creator+"&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+data.main.creattm+"</td></tr>";
//	html =html+ "</table><style>tr{background:#fff}</style>";
//	return html;
}




//打印预览箱
//尺码横排打印
outstockrecheck.preColNamesDtl1_box= [
     		                   {title:"箱号",field:"itemNo",width:150},{title:"商品编码",field:"itemNo",width:150}
                         ];
outstockrecheck.endColNamesDtl1_box = [
     		                      {title:"合计",field:"allCount"}
                             ] ;
outstockrecheck.sizeTypeFiledName_box = 'sizeKind';
outstockrecheck.printDetailBox4SizeHorizontal = function(item){
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
 	        preColNames:JSON.stringify(outstockrecheck.preColNamesDtl1_box),
 	        endColNames:JSON.stringify(outstockrecheck.endColNamesDtl1_box),
 	        sizeTypeFiledName:outstockrecheck.sizeTypeFiledName_box
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
	var headHtml =  outstockrecheck.getHtmlByTemplateHeadByBox(data);
	var bodyHtml = outstockrecheck.getHtmlByTemplateBodyByBox(data);
	var footerHtml = outstockrecheck.getHtmlByTemplateFooterByBox(data);
	
	var totalHeight = 1100;//总高度
	var headHeight = 85;
	var spaceHeight = 5;
	var tableHeight = 0;//表格高度
	var footerHeight = 50;//底部高度
	if(size == "A4"){
		totalHeight = 1000;
	}else if(size == "A5"){
		totalHeight = 450;
	}
	tableHeight = totalHeight - headHeight - 2 * spaceHeight - footerHeight;//表格的高度
	
	//设置表格内容
	LODOP.ADD_PRINT_TABLE(headHeight+spaceHeight,0,"100%",tableHeight,strStyle+bodyHtml);
	
	//设置表格头部
	LODOP.ADD_PRINT_HTM(0,0,"100%",headHeight,headHtml);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	
	//设置表格底部
	LODOP.ADD_PRINT_HTM(totalHeight+footerHeight,0,"100%",footerHeight,footerHtml);
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
outstockrecheck.getHtmlByTemplateHeadByBox = function(data){
	var html = "<table style='width:100%;'>";
	html += "<tr style='font-size:13px;'><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
	html += "<tr style='font-size:13px;'><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+data.main.recheckNo+"</td><td>发货方："+data.main.sender+"</td><td>收货方："+data.main.receipterName+"("+data.main.receipter+")</td><td>总合计："+data.main.sumQty+"</td></tr>";
	html += "</table>";
	return html;
};


//获取数据内容
outstockrecheck.getHtmlByTemplateBodyByBox = function(data){
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
	html = html+"<tr style='font-size:13px;'><td>"+footer.itemNo+"</td><td></td><td></td>";
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
outstockrecheck.getHtmlByTemplateFooterByBox = function(data){
	var html = "<table style='width:100%;'>";
	html =html+ "<tr><td>签收：</td><td style='text-align:right;padding-right:50px;'>制单人："+data.main.creator+"&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+data.main.creattm+"</td></tr>";
	html =html+ "</table>";
	return html;
};


outstockrecheck.initUsers = function(){
	wms_city_common.comboboxLoadFilter(
			["creator"],
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