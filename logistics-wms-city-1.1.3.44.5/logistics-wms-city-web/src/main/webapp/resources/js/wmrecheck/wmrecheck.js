
var wmrecheck = {};
wmrecheck.user={};

// 加载Grid数据Utils
wmrecheck.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};


// wmrecheck.status = {"10":"已审核","11":"建单","12":"部分验收","13":"结案"};
wmrecheck.statusFormatter = function(value, rowData, rowIndex){
	return wmrecheck.status[value];
};
// 创建分货单
wmrecheck.showAddDialog = function(){
	$("#searchFormTab1").form("clear");
	var divideNoInput = $("#divideNo");
	divideNoInput.attr("readonly",false);
	divideNoInput.find("+ input+ input").attr("disabled",false);
	// $("#saveBtn").attr("disabled",false);
	$("#checkBoxBtn").hide();
	
	$("#saveBtn").linkbutton("enable");
	$("#checkBoxBtn").linkbutton("enable");
	$("#selectRecedeBtn").attr("disabled",false);
	
	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text").attr("readOnly",false);
	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#divideMainInfoForm input[id=storeNo]+ span").css("background-color","white");
	$("#divideMainInfoForm input[id=storeNo]+ span input.combo-text").css("background-color","white");
	
	$('#order_dtl1_dataGrid').datagrid('loadData',[]);
	$('#order_dtl2_dataGrid').datagrid('loadData',[]);
	$("#showDetailDialog").window('open');
	
};




// 关闭
wmrecheck.closeWindow = function(id){
	$('#'+id).window('close');  
};
 


// 打开复核装箱界面
wmrecheck.opencheckbox = function(){
	
	if($('#recheckNo').val()==''){
		alert("复核主档信息为空,不能复核装箱!");
		return;
	}
	
	var selectRows = $("#order_dtl1_dataGrid").datagrid('getChecked');
	if(selectRows.length<1){
		alert("请选择需要装箱的商品");
		return;
	}
	for(var i=0;i<selectRows.length;i++){
		if(selectRows[i].status == 10 || selectRows[i].status == 13){
			alert("只有建单状态才能装箱操作！");
			return;
		}
	}
	
	var tipStr = "";
	var dataList = [];
	$.each(selectRows, function(index, item){
		if(item.recheckQty >= item.realQty){
			tipStr = '【商品'+item.itemNo+",尺码"+item.sizeNo+"】";
			return false;
		}
		
		var params = {
				sourceNo:item.sourceNo,
				packQty:item.packQty,
				ownerNo:item.ownerNo,
				divideId:item.divideId,
				itemNo:item.itemNo,
				itemName:item.itemName,
				realQty:item.realQty-item.recheckQty,
				packageNum:item.realQty-item.recheckQty,
				colorNo:item.colorNo,
				colorName:item.colorName,
				sizeNo:item.sizeNo,
				barcode:item.barcode,
				sourceNo:item.sourceNo,
				recedeType:item.recedeType,
				recedeDate:item.recedeDate,
				dCellNo:item.dCellNo,
				dCellId:item.dCellId,
				brandNo:item.brandNo
		};
		dataList[dataList.length] = params;
	});
	
	if(tipStr!=""){
		alert(tipStr+"已经装箱完成,不能再次装箱!");
		return;
	}
	
	$("#check_box_dataGrid").datagrid('loadData',dataList);
	$("#check_box_dialog").window('open');
};

// 打开退厂通知单选择界面
wmrecheck.openRecedeNoSelect = function(){
	var queryMxURL=BasePath+ '/wmrecede/findBillWmRecedeGroupByPage.json';
	var queryParams={locno:wmrecheck.user.locno,status:'20'};
	wmrecheck.loadGridDataUtil('recede_no_dataGrid', queryMxURL, queryParams);
	$("#recede_no_dialog").window('open');
};

wmrecheck.recedeQuery = function(){
	var queryMxURL=BasePath+ '/wmrecede/findBillWmRecedeGroupByPage.json';
	var fromObjStr=convertArray($('#recedeSearchForm').serializeArray());
	var queryParams= eval("("+fromObjStr+")");
	queryParams['locno'] = wmrecheck.user.locno;
	wmrecheck.loadGridDataUtil('recede_no_dataGrid', queryMxURL, queryParams);
	$("#recede_no_dialog").window('open');
};

wmrecheck.setDivideNo = function(rowData){
	$("#recheckNo").val(rowData.recheckNo);
	$("#recedeNo").val(rowData.divideNo);
	if(""==rowData.supplierName||null==rowData.supplierName){
		$('#supplierName').val(rowData.supplierNo);
	}else{
		$('#supplierName').val(rowData.supplierName);
	}
	$('#supplierNoHidden').val(rowData.supplierNo);
	wmrecheck.selectSupplierNoToOutstockDtl(rowData.divideNo);
};

// 选择退厂通知单
wmrecheck.selectRecedeQuery = function(rowData){
	$('#recedeNo').val(rowData.recedeNo);
	$('#supplierName').val(rowData.supplierName);
	$('#supplierNoHidden').val(rowData.supplierNo);
	wmrecheck.selectSupplierNoToOutstockDtl(rowData.recedeNo);
	$("#recede_no_dialog").window('close');
};

// 选择客户执行
wmrecheck.selectSupplierNoToOutstockDtl = function(recedeNo){
	var queryURL=BasePath+ '/bill_wm_outstock_dtl/dtl_findOutstockDtlItem.json';
	var locno = wmrecheck.user.locno;
	if(recedeNo!=""&&locno!=""){
		var queryParams={"sourceNo":recedeNo,"locno":locno};
		wmrecheck.loadGridDataUtil('order_dtl1_dataGrid', queryURL, queryParams);
	}
};

// 初始供应商选择下拉框
wmrecheck.initSupplier = function(){
	wms_city_common.comboboxLoadFilter(
			["supplierNoSearch"],
			"supplierNo",
			"supplierName",
			"supplierName",
			false,
			[true],
			BasePath+'/wmrecheck/querySupplier?locno='+wmrecheck.user.locno,
			{},
			null,
			null);
};

wmrecheck.editInfo = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	// debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		var count = wmrecheck.checkIsRfPackage(checkedRows[0]);
		if(count > 0){
			wmrecheck.openPackageBoxRf(checkedRows[0]);
		}else{
			wmrecheck.loadDetail(checkedRows[0],"edit");
		}
	}
};


//验证是否需要RF封箱
wmrecheck.checkIsRfPackage = function(rowData){
	var count = 0;
	var url = BasePath + '/wmrecheckdtl/get_biz';
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
wmrecheck.openPackageBoxRf = function(rowData){
	$("#check_box_dialog_rf").window('open');
	$('#check_box_dataGrid_rf').datagrid('loadData', { total: 0, rows: [] });
	$('#recedeNoRf').val(rowData.divideNo);
	$('#supplierNameRf').val(rowData.supplierName);
	$('#supplierNoHiddenRf').val(rowData.supplierNo);
	$("#recheckNoRf").val(rowData.recheckNo);
	var queryMxURL=BasePath+ '/wmrecheckdtl/findWmRecheckDtlByPage.json';
	var queryParams={recheckNo:rowData.recheckNo,locno:rowData.locno,containerNo : 'N'};
	wmrecheck.loadGridDataUtil('check_box_dataGrid_rf', queryMxURL, queryParams);
};

//rf封箱
wmrecheck.packageBoxRf = function(rowData){
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
		var params = {
				"supplierNo" : $('#supplierNoHiddenRf').val(),
				"recheckNo" : $("#recheckNoRf").val(),
				"recedeNo" : $("#recedeNoRf").val(),
				"locno" : wmrecheck.user.locno
			};
		wmrecheck.ajaxRequest(BasePath + '/wmrecheck/packageBoxRf',
					params, true, packageboxcallback);
	});
	
};

// 加载订单详情
wmrecheck.loadDetail = function(rowData,type){
	$("#checkBoxBtn").show();
	$("#divideMainInfoForm").form("clear");
	wmrecheck.setDivideNo(rowData);
	
	$("#saveBtn").linkbutton("disable");
	$("#selectRecedeBtn").attr("disabled",true);
	if(type=="edit"){
		$("#checkBoxBtn").linkbutton("enable");
	}else{
		$("#checkBoxBtn").linkbutton("disable");
	}
	
	// wmrecheck.disableCombox("divideMainInfoForm","supplierNo");
	$("#showDetailDialog").window('open');
	// wmrecheck.queryRecheckItem();
	var queryMxURL=BasePath+ '/wmrecheckdtl/dtl_list.json';
	var queryParams={recheckNo:rowData.recheckNo,locno:wmrecheck.user.locno};
	// console.info(queryParams);
	wmrecheck.loadGridDataUtil('order_dtl2_dataGrid', queryMxURL, queryParams);
};
// 查询分货单里面待复核的商品信息
wmrecheck.queryRecheckItem = function(){
	var queryRecheckBoxItemURL=BasePath+ '/wmrecheck/queryRecheckItem';
	var locno = wmrecheck.user.locno;
	var supplierNo = $('#supplierNo').combobox("getValue");
	if(!supplierNo ||!locno){
		alert("请选择供应商！！");
		return;
	}
	var ueryRecheckBoxItemqueryParams={"supplierNo":supplierNo,"locno":locno};
	wmrecheck.loadGridDataUtil('order_dtl1_dataGrid', queryRecheckBoxItemURL, ueryRecheckBoxItemqueryParams);
};
wmrecheck.disableCombox = function(formId,comboxId){
	$("#"+formId).find(" input[id="+comboxId+"]+ span input.combo-text").attr("readOnly",true);
	$("#"+formId).find(" input[id="+comboxId+"]+ span input.combo-text+ span span").removeClass("combo-arrow");
	$("#"+formId).find(" input[id="+comboxId+"]+ span").css("background-color","#EBEBE4");
	$("#"+formId).find(" input[id="+comboxId+"]+ span input.combo-text").css("background-color","#EBEBE4");
};
wmrecheck.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
wmrecheck.saveMainInfo = function(){
	
	if($('#recedeNo').val()==''){
		alert("请选择退厂通知单!");
		return;
	}
	wms_city_common.loading("show","正在保存......");
    var url = BasePath+'/wmrecheck/saveMainInfo';
    $('#searchFormTab1').form('submit', {
			url: url,
			onSubmit: function(param){
				param.createtm = wmrecheck.user.currentDate19Str;
				param.creator = wmrecheck.user.loginName;
				param.creatorName = wmrecheck.user.username;
				param.locno=wmrecheck.user.locno;
			},
			success: function(r){
				r = wmrecheck.parseJsonStringToJsonObj(r);
				if(r && r.success=="true"){
					
					// wmrecheck.disableCombox("searchFormTab1","supplierNo");
					$("#searchFormTab1 input[name=recheckNo]").val(r.recheckNo);
					$("#saveBtn").linkbutton("disable");
					$("#selectRecedeBtn").attr("disabled",true);
					$("#checkBoxBtn").show();
					$('#order_dtl1_dataGrid').datagrid("reload");
					
// var queryRecheckBoxItemURL=BasePath+ '/wmrecheck/queryRecheckItem';
// var locno = wmrecheck.user.locno;
// var supplierNo = $('#supplierNo').combobox("getValue");
// if(!supplierNo ||!locno){
// }else{
// var ueryRecheckBoxItemqueryParams={"supplierNo":supplierNo,"locno":locno};
// wmrecheck.loadGridDataUtil('order_dtl1_dataGrid', queryRecheckBoxItemURL,
// ueryRecheckBoxItemqueryParams);
// }
					 alert('保存成功!');
				}else{
					 alert('保存失败!');
				}
				wms_city_common.loading();
		    },
			error:function(){
				alert('保存失败,请联系管理员!',2);
				wms_city_common.loading();
			}
	   });
};
wmrecheck.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
// 初始化状态
wmrecheck.status = {};
wmrecheck.initStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECEDE_RECHECK_STATUS',
			{},
			wmrecheck.status,
			null);
};

wmrecheck.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

wmrecheck.itemFormatter = function(value, rowData, rowIndex){
	if(rowData && rowData.item){
		return rowData.item[this.field];
	}
	return value;
};
wmrecheck.packNumFormatter = function(value, rowData, rowIndex){
	return "";
};
wmrecheck.numFormatter = function(value, rowData, rowIndex){
	return rowData.itemQty - rowData.realQty;
};
wmrecheck.realQtyFormatter = function(value, rowData, rowIndex){
	if(value){
		return value;
	}else{
		return '';
	}
	
};

// //搜索数据
// wmrecheck.searchData = function(){
//	
// // var startCreatetm = $('#startCreatetm').datebox('getValue');
// // var endCreatetm = $('#endCreatetm').datebox('getValue');
// // if(!isStartEndDate(startCreatetm,endCreatetm)){
// // alert("创建日期开始日期不能大于结束日期");
// // return;
// // }
//	
// // var startAudittm = $('#startAudittmCondition').datebox('getValue');
// // var endAudittm = $('#endAudittmCondition').datebox('getValue');
// // if(!isStartEndDate(startAudittm,endAudittm)){
// // alert("审核日期开始日期不能大于结束日期");
// // return;
// // }
//	
// // var startExpDate = $('#startExpDateCondition').datebox('getValue');
// // var endExpDate = $('#endExpDateCondition').datebox('getValue');
// // if(!isStartEndDate(startExpDate,endExpDate)){
// // alert("分货日期开始日期不能大于结束日期");
// // return;
// // }
//	
// var fromObjStr=convertArray($('#searchForm').serializeArray());
// var queryMxURL=BasePath+'/wmrecheck/list.json';
// var reqParam = eval("(" +fromObjStr+ ")");
// wmrecheck.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
// };

// 清除查询条件
wmrecheck.searchClear = function(){
	$('#searchForm').form("clear");
};

wmrecheck.searchClear2 = function(){
	$('#recedeSearchForm').form("clear");
};

// 主要是推荐这个函数。它将jquery系列化后的值转为name:value的形式。
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
// 取箱号
wmrecheck.getConLabelNo = function(){
	var locno = wmrecheck.user.locno;
	var callback = function(r){
		$("#checkboxsearchForm input[name=boxNo]").val(r.strOutLabelNo);
		$("#checkboxsearchForm input[name=containerNo]").val(r.strOutContainerNo);
		$("#checkboxsearchForm input[name=containerType]").val(r.strpapertype);
	};
	 wmrecheck.ajaxRequest(BasePath+'/bill_om_recheck/getLabelNo',{"locno":locno},true,callback);
};
wmrecheck.validateBoxNo = function(){
	var locno = wmrecheck.user.locno;
	var labelNo = this.value;
	var validataCallBack = function(r){
		
		if(r){
			$("#checkboxsearchForm input[name=containerNo]").val("");
			$("#checkboxsearchForm input[name=containerType]").val("");
		}else{
			alert("该箱号不可用，请再次输入箱号或者获取一个新箱号！");
		}
		
	};
	wmrecheck.ajaxRequest(BasePath+'/bill_om_recheck/validateBoxNo',{"locno":locno,"labelNo":labelNo},true,validataCallBack);
};

// 复核装箱
wmrecheck.packageBox = function(){
	var rows = $("#check_box_dataGrid").datagrid('getRows');
	
	var packageboxcallback = function(r){
		wms_city_common.loading();
		if(r.success=="true"){
			alert("装箱成功!");
			$("#check_box_dialog").window('close');// 2,关闭窗口
			wmrecheck.selectSupplierNoToOutstockDtl($('#recedeNo').val());
			// $('#order_dtl1_dataGrid').datagird('load');
		}else{
			alert('装箱失败:'+r.msg,1);
		}
		
	};
	
	// 验证明细数据是否录入
    var tempFlag = wmrecheck.endEdit('check_box_dataGrid');
	if(tempFlag!=""){
		alert(tempFlag);
		return;
	}
	
	var tipStr = "";
	$.each(rows, function(index, item){
		if(item.packageNum == "0"){
			tipStr = "商品"+item.itemNo+"尺码："+item.sizeNo+":装箱数量不能为0;";
			return;
		}
		
		if(!wmrecheck.isCharOrNumber(item.packageNum)){
			tipStr = "商品"+item.itemNo+"尺码："+item.sizeNo+":装箱数量不是数字类型;";
			return;
		}
		
		if(item.packageNum > item.realQty){
			tipStr =  "商品："+item.itemNo + "尺码："+item.sizeNo+"装箱数量不能大于计划数量!";
			return;
		}
	});   
	
	if(tipStr!=""){
		alert(tipStr);
		return;
	}
	wms_city_common.loading('show');
	rows = encodeURIComponent(JSON.stringify(rows));
	var params = {
			"datas":rows,
			"supplierNo":$('#supplierNoHidden').val(),
			"recheckNo":$("#recheckNo").val(),
			"locno":wmrecheck.user.locno};
	
	wmrecheck.ajaxRequest(BasePath+'/wmrecheck/packagebox',params,true,packageboxcallback);

	// var boxNo = $("#checkboxsearchForm input[name=boxNo]").val();
	// var containerNo = $("#checkboxsearchForm input[name=containerNo]").val();
	// var containerType = $("#checkboxsearchForm
	// input[name=containerType]").val();
// if(boxNo && boxNo!=''){
//		
// }else{
// alert("请输入箱号，或取新箱号！");
// }
	
};

// 验证编辑行
wmrecheck.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		var originQty = tempObj.datagrid('getEditor', {index:i,field:'packageNum'});
    		if(originQty!=null){
    			var itemNo = rowArr[i].itemNo;
    			var itemQty = rowArr[i].realQty;
            	var packageNum = tempObj.datagrid('getEditor', {index:i,field:'packageNum'});
            	if(packageNum!=null){
            		
            		if(!wmrecheck.isCharOrNumber(packageNum.target.val().trim())){
            			return "商品"+itemNo+"尺码："+rowArr[i].sizeNo+":装箱不是数字类型;";
            		}
            		
            		if(packageNum.target.val().trim()=="0"){
            			return "商品"+itemNo+"尺码："+rowArr[i].sizeNo+":装箱数量不能为0;";
            		}
            		
            		if(packageNum.target.val().trim() > itemQty){
                		$(packageNum.target).focus();
                		return "商品"+itemNo+"尺码："+rowArr[i].sizeNo+":装箱数量不能大于计划数量;";
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

/**
 * 验证是否是数字或字母
 */
wmrecheck.isCharOrNumber = function(str){
	var reg = /^[A-Za-z0-9]+$/;
	if(reg.test(str)){
		return true;
	}
	return false;
};

wmrecheck.searchData = function(){
// var startCreatetm = $('#startCreatetm').datebox('getValue');
// var endCreatetm = $('#endCreatetm').datebox('getValue');
// if(!isStartEndDate(startCreatetm,endCreatetm)){
// alert("创建日期开始日期不能大于结束日期");
// return;
// }
//	
// var startAudittm = $('#startAudittm').datebox('getValue');
// var endAudittm = $('#endAudittm').datebox('getValue');
// if(!isStartEndDate(startAudittm,endAudittm)){
// alert("审核日期开始日期不能大于结束日期");
// return;
// }
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/wmrecheck/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = wmrecheck.user.locno;
	wmrecheck.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
	
	
// $.extend(reqParam,{locno:wmrecheck.user.locno});
// $('#mainDataGrid').datagrid({
// url:queryMxURL,
// queryParams:reqParam
// });
};
wmrecheck.clearSearchCondition = function(){
	$("#searchForm").form('clear');
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/wmrecheck/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	$.extend(reqParam,{locno:wmrecheck.user.locno});
	$('#mainDataGrid').datagrid({    
	    url:queryMxURL,
	    queryParams:reqParam
	});  
	$('#brandNo').combobox("loadData",[]);
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
wmrecheck.checkUserSelect = function(){
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
		$("#check_user_dialog").window('open');
	}
};
wmrecheck.check = function(){
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
			checkIds.push(checkRows[i].recheckNo+"-"+wmrecheck.user.locno);
		}
		var url = BasePath+'/wmrecheck/check';
		var data = {ids:checkIds.join(",")};
		wmrecheck.ajaxRequest(url,data,true,function(r){
			if(r.success=="true"){
				alert("审核成功!");
				$("#mainDataGrid").datagrid('load');// 1,查询加载待复核的商品信息
			}else{
				alert('审核失败,原因:'+r.msg,2);
			}
// if(result=='success'){
// $("#mainDataGrid").datagrid('load');
// alert('审核成功!');
//				
// }else{
// alert('审核失败,请联系管理员!',2);
// }
		}); 
		
		
		$("#check_user_dialog").window('close');
	}else{
		alert("请选择复核人！");
		return;
	}
	
};


// 删除
wmrecheck.doDel = function(){
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
        		keyStr[keyStr.length] = {locno:wmrecheck.user.locno,recheckNo:item.recheckNo};
        	});   
        	
        	// 删除提示
        	if(tipMessage!=""){
        		tipMessage = tipMessage.substring(0, tipMessage.length-1);
        		alert("单据:【"+tipMessage+"】已操作的复核单不能删除!");
        		return;
        	}
        	
            // 2.绑定数据
            var url = BasePath+'/wmrecheck/deleteBillWmRecheck';
        	var data={
        	    "datas":JSON.stringify(keyStr)
        	};
        	// 3. 删除
        	ajaxRequest(url,data,function(r){
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

wmrecheck.initCheckUser = function(locno){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#checkUser').combobox({
			     valueField:"workerNo",
			     textField:"workerName",
			     data:data,
			     panelHeight:150,
			  }).combobox("select",data[0].workerNo);
		}
	});
};

// JS初始化执行
$(document).ready(function(){
	$("#startCreatetm").datebox('setValue',getDateStr(-2));
	wmrecheck.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(r){
		wmrecheck.user=r;
		wmrecheck.initCheckUser(r.locno);
	});
	wmrecheck.initSupplier();
	// $('#mainDataGrid').datagrid({'url':BasePath+'/wmrecheck/list.json?locno='+wmrecheck.user.locno,'title':'退厂复核单列表','pageNumber':1
	// });
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
	$("#checkboxsearchForm input[name=boxNo]").blur(wmrecheck.validateBoxNo);
	wmrecheck.initStatus();
	// 委托业主
	wmrecheck.ajaxRequest(BasePath+'/entrust_owner/get_biz',{},false,wmrecheck.initOwnerNo);
	
	$('#main_order_dtlId').tabs({    
	    onSelect:function(title,index){    
	       if(index == 1){
	    	   var recheckNo = $('#recheckNo').val();
	    	   if(recheckNo!=''&&recheckNo!=null){
	    		   var queryMxURL=BasePath+ '/wmrecheckdtl/dtl_list.json';
		    	   var queryParams={recheckNo:recheckNo,locno:wmrecheck.user.locno};
		    	   wmrecheck.loadGridDataUtil('order_dtl2_dataGrid', queryMxURL, queryParams);
	    	   }
	    	   
	       }  
	    }    
	});
	
	//待复核明细列表
	$('#order_dtl1_dataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					wmrecheck.realQty = data.footer[1].realQty;
					wmrecheck.recheckQty = data.footer[1].recheckQty;
					wmrecheck.diffQty = data.footer[1].diffQty;
				} else {
					var rows = $('#order_dtl1_dataGrid').datagrid('getFooterRows');
					rows[1]['realQty'] = wmrecheck.realQty;
					rows[1]['recheckQty'] = wmrecheck.recheckQty;
					rows[1]['diffQty'] = wmrecheck.diffQty;
					$('#order_dtl1_dataGrid').datagrid('reloadFooter');
				}
			}
		}
	});
	
	//箱明细列表
	$('#order_dtl2_dataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					wmrecheck.itemQty = data.footer[1].itemQty;
				} else {
					var rows = $('#order_dtl2_dataGrid').datagrid('getFooterRows');
					rows[1]['itemQty'] = wmrecheck.itemQty;
					$('#order_dtl2_dataGrid').datagrid('reloadFooter');
				}
			}
		}
	});
	
	
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('input[id=brandNo]', $('#searchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs);
});


// ========================委托业主========================
wmrecheck.ownerNo = {};
wmrecheck.ownerNoFormatter = function(value, rowData, rowIndex){
	return wmrecheck.ownerNo[value];
};
wmrecheck.initOwnerNo = function(data){
// $('#ownerNo').combobox({
// valueField:"ownerNo",
// textField:"ownerName",
// data:data,
// panelHeight:"auto"
// });
	wmrecheck.ownerNo = wmrecheck.tansforOwner(data);
};

wmrecheck.tansforOwner = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
wmrecheck.printByBox = function(noneDtl){
	var rows = $('#order_dtl2_dataGrid').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var recedeNo = $("#recedeNo").val();
	var recheckNo = $("#recheckNo").val();
	var supplierName = $("#supplierName").val();
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].scanLabelNo);
	}
	var url = BasePath+'/wmrecheckdtl/printByBox?noneDtl='+noneDtl;
	var params = {
			locno:wmrecheck.user.locno,
			recheckNo:recheckNo,
			recedeNo:recedeNo,
			supplierName:supplierName,
			keys:keys.join("|")
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
      	LODOP.SET_PRINT_PAGESIZE(1,'1000','1600',"");
     	var htmls = resultData.html;
     	var boxs = resultData.box;
     	for(var i=0,length=htmls.length;i<length;i++){
     		LODOP.NewPage();
     		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",htmls[i]);
    		LODOP.ADD_PRINT_BARCODE(110,120,225,40,"128A",boxs[i]);
     	}
		LODOP.PREVIEW();
     }
};