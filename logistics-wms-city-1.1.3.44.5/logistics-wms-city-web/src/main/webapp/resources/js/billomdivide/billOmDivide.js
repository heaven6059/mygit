
var billOmDivide = {};


//加载Grid数据Utils
billOmDivide.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//加载dataGrid
billOmDivide.loadDataGrid = function(){
	$('#mainDataGrid').datagrid({'url':BasePath+'/bill_om_divide/list.json?locno='+billOmDivide.locno,'pageNumber':1 });
};

//创建分货单弹出窗口
billOmDivide.showAddDialog = function(){
	$('#searchReceiptForm').form('clear');
	$('#mainDataGrid2').datagrid('loadData', { total: 0, rows: [] });
	
	var isShowOmDivide  = $('#isShowOmDivide').val();
	if(isShowOmDivide=='Y'){
		$('#save_main_new').show();
	}else{
		$('#save_main_new').hide();
	}
	
//	$('#cellNo').combobox('setValue', '');
	$("#showAddDialog").window('open');
};
billOmDivide.searchExp = function(isSearch){
	var checkedRows = $("#mainDataGrid2").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择收货单!',1);
		return;
	} else {	
		var keyStr = [];
		$.each(checkedRows, function(index, item){
			keyStr.push(item.receiptNo);
		});
		
		var startCreatetm =  $('#startCreatetmExp').datebox('getValue');
		var endCreatetm =  $('#endCreatetmExp').datebox('getValue');
		if(!isStartEndDate(startCreatetm,endCreatetm)){    
			alert("创建日期开始日期不能大于结束日期");   
	        return;   
	    }
		
		var queryMxURL=BasePath+'/billomexp/list_divide';
		var reqParam ={};
		reqParam['keyStr'] = keyStr.join(",");
		reqParam['locno'] = billOmDivide.locno;
		reqParam['expNo'] = $("#expNo").val();
		reqParam['status'] = $('#statusExp').combobox('getValue');
		reqParam['brandNo'] = $('#brandNoExp').combobox('getValue');
		reqParam['startCreatetmExp'] = $('#startCreatetmExp').val();
		reqParam['endCreatetmExp'] = $('#endCreatetmExp').val();
		reqParam['businessType'] = $('#businessType').combobox('getValue');
		if(isSearch=='Y') {
			reqParam['isPoNo'] = "Y";
		} else {
			reqParam['isPoNo'] = "N";
		}
		
		billOmDivide.loadGridDataUtil('expNoDataGrid', queryMxURL, reqParam);
	}
};
billOmDivide.billOmExp = function(){
	var checkedRows = $("#mainDataGrid2").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择分货单据!',1);
		return;
	} else {
		$('#searchExpNoForm').form('clear');
		billOmDivide.searchExp("Y");
		$("#showExpDialog").window('open');
	}
};
billOmDivide.loadBusinessType = function(type){
	$('#transNoCon').val("");
	$('#deliverNoCon').val("");
	$('#supplierNoCon').val("");
	if(type=="0"){
		$('#deliverNoTitle').show();
		$('#deliverNoCon').show();
	}else{
		$('#deliverNoTitle').hide();
		$('#deliverNoCon').hide();
	}
};
billOmDivide.selectBillOmExp = function(){
	var checkedRows = $('#expNoDataGrid').datagrid('getChecked');
	if(checkedRows.length == 0){
		alert("请选择发货单!");
		return;
	}
//	if(checkedRows.length > 1){
//		alert("只能选择一条发货单记录!");
//		return;
//	}
	
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"条发货单信息吗？", function (r){  
		if (r) {
			wms_city_common.loading("show");
			$('#mainDataGrid3').datagrid('loadData', { total: 0, rows: [] });
			$.each(checkedRows, function(index, item){
				var  rowData = {
						"locno":billOmDivide.locno,
						"ownerNo":item.ownerNo,
						"expNo":item.expNo
				};
				//把选择的商品编码行记录插入到父窗体中
				billOmDivide.insertRowAtEnd("mainDataGrid3",rowData);
			});
			wms_city_common.loading();
		}
	});
	//关闭窗口
    billOmDivide.closeWindow('showExpDialog');
};
billOmDivide.insertRowAtEnd = function(gid,rowData){
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

//创建分货单
billOmDivide.saveBillOmDivide = function(){
	
	var checkedItems = $('#mainDataGrid3').datagrid('getRows');
	if(checkedItems.length == 0){
		alert("请选择发货单!");
		return;
	}
	var checkedRows = $('#mainDataGrid2').datagrid('getChecked');
	if(checkedRows.length == 0){
		alert("请选择收货单!");
		return;
	}
	var keyStr = [];
	var receiptNos = "";
	$.each(checkedRows, function(index, item){
		keyStr.push(item.receiptNo);
		receiptNos += item.receiptNo + ",";
	});
	receiptNos=receiptNos.substring(0, receiptNos.length - 1);
	
	var ruleNo = $('input[name="storeRuleRadio"]:checked').val();
	var dataList = [];
	$.each(checkedItems, function(index, item){
		var reqParam = {locno:item.locno,ownerNo:item.ownerNo,
				ruleNo:ruleNo,expNo:item.expNo,
				receiptNo:receiptNos,creator:billOmDivide.loginName,
				businessType:$('#businessType').combobox('getValue')};
		dataList[dataList.length] = reqParam;
	});   
	
	//提交
	wms_city_common.loading('show','保存中,请稍候...');
	var url = BasePath+ '/bill_om_divide/saveBillOmDivide';
	var effectRow = {inserted:JSON.stringify(dataList)};
	$.post(url, effectRow, function(data) {
		if(data.flag == "success"){
			wms_city_common.loading();
			alert('创建成功!');
        	$('#mainDataGrid2').datagrid('loadData', { total: 0, rows: [] });
        	$('#mainDataGrid3').datagrid('loadData', { total: 0, rows: [] });
            billOmDivide.closeWindow('showAddDialog'); //关闭窗口
            billOmDivide.loadDataGrid();
    	}else if(data.flag=='warn'){
    		wms_city_common.loading();
        	alert(data.msg,1);
        	return;
        }else{
        	wms_city_common.loading();
        	alert('操作异常！',1);
        	return;
        }
		
//        var volist = data.volist;
//        if(volist.length>0){
//        	var str = "创建失败：";
//        	for ( var i = 0; i < volist.length; i++) {
//        		if(volist[i].errorMessage!=''&&volist[i].errorMessage!=null){
//        			str+= volist[i].errorMessage+",";
//        		}
//			}
//        	if(str!=""){
//        		str = str.substring(0, str.length-1);
//        		alert(str);
//        	}
//        }else{
//        	alert("创建分货单成功");
//        }
		
        
    }, "JSON").error(function() {
    	wms_city_common.loading();
    	alert('保存失败!',1);
    });
	
};

//创建分货单（新）
billOmDivide.saveBillOmDivideNew = function(){
	
	var checkedItems = $('#mainDataGrid3').datagrid('getRows');
	if(checkedItems.length == 0){
		alert("请选择发货单!");
		return;
	}
	var checkedRows = $('#mainDataGrid2').datagrid('getChecked');
	if(checkedRows.length == 0){
		alert("请选择收货单!");
		return;
	}
	var keyStr = [];
	var receiptNos = "";
	$.each(checkedRows, function(index, item){
		keyStr.push(item.receiptNo);
		receiptNos += item.receiptNo + ",";
	});
	receiptNos=receiptNos.substring(0, receiptNos.length - 1);
	
	var ruleNo = $('input[name="storeRuleRadio"]:checked').val();
	var dataList = [];
	$.each(checkedItems, function(index, item){
		var reqParam = {locno:item.locno,ownerNo:item.ownerNo,
				ruleNo:ruleNo,expNo:item.expNo,
				receiptNo:receiptNos,creator:billOmDivide.loginName,
				creatorname:billOmDivide.user.username,
				editor:billOmDivide.loginName,
				editorname:billOmDivide.user.username,
				businessType:$('#businessType').combobox('getValue')};
		dataList[dataList.length] = reqParam;
	});   
	
	//提交
	wms_city_common.loading('show','保存中,请稍候...');
	var url = BasePath+ '/bill_om_divide/saveBillOmDivideNew';
	var effectRow = {inserted:JSON.stringify(dataList)};
	$.post(url, effectRow, function(data) {
		if(data.flag == "success"){
			wms_city_common.loading();
			alert('创建成功!');
        	$('#mainDataGrid2').datagrid('loadData', { total: 0, rows: [] });
        	$('#mainDataGrid3').datagrid('loadData', { total: 0, rows: [] });
            billOmDivide.closeWindow('showAddDialog'); //关闭窗口
            billOmDivide.loadDataGrid();
    	}else if(data.flag=='warn'){
    		wms_city_common.loading();
        	alert(data.msg,1);
        	return;
        }else{
        	wms_city_common.loading();
        	alert('操作异常！',1);
        	return;
        }
		
//        var volist = data.volist;
//        if(volist.length>0){
//        	var str = "创建失败：";
//        	for ( var i = 0; i < volist.length; i++) {
//        		if(volist[i].errorMessage!=''&&volist[i].errorMessage!=null){
//        			str+= volist[i].errorMessage+",";
//        		}
//			}
//        	if(str!=""){
//        		str = str.substring(0, str.length-1);
//        		alert(str);
//        	}
//        }else{
//        	alert("创建分货单成功");
//        }
		
        
    }, "JSON").error(function() {
    	wms_city_common.loading();
    	alert('保存失败!',1);
    });
	
};


//手工关闭
billOmDivide.overFloc=function(){
	
	var currentStatus = $('#currentStatus').val();
	if(currentStatus == '91' || currentStatus == '90'){
		alert("该分货单已经关单,不需再次关单!");
		return false;
	}
	
	var divideNo = $('#divideNo').val();
	var data = {
		"locno" : billOmDivide.locno,
		"divideNo" : divideNo
	};
	
	var messageStr = "";
	var checkNum = billOmDivide.checkNewRecheck(divideNo);
	if(checkNum > 0){
		messageStr = "分货单存在未审核的复核单,";
	}
	$.messager.confirm("确认",messageStr+"请确认是否手工关闭该分货单？", function (r){
			if (r) {
				wms_city_common.loading("show");
				url = BasePath+'/bill_om_divide/overOmDivide';
				ajaxRequest(url,data,function(result){
					wms_city_common.loading();
					if(result.flag=='success'){
				    	//4.删除成功,清空表单
				        alert('手工关闭成功！');
				        $('#mainDataGrid').datagrid('load');
				     }else if(result.flag=='fail'){
				        alert(result.msg,1);
				     }
			     }); 
			}
	});     
	
};

//转商品差异调整
billOmDivide.toDivideDifferent = function(){
	
	// 获取所有勾选checkbox的行
	var checkRows = $("#order_dtl2_dataGrid").datagrid("getChecked");
	if(checkRows.length < 1){
		alert("请选择差异数据!");
		return;
	}
	
	var tipStr = "";
	var keyStr = [];
	$.each(checkRows, function(index, item){
		if(item.status == '14'){
			tipStr = "箱号："+item.boxNo+"商品："+item.itemNo+",尺码："+item.sizeNo+",已转商品差异调整!";
			return false;
		}
		var params = {
			locno : item.locno,
			ownerNo : item.ownerNo,
			divideNo : item.divideNo,
			divideId : item.divideId
		};
		keyStr[keyStr.length] = params;
	});
	
	if(tipStr!=""){
		alert(tipStr);
		return false;
	}
	
	var params = {
		datas : JSON.stringify(keyStr)
	};
	
	$.messager.confirm("确认","您转商品差异调整吗?", function (r){ 
		if(r){
			wms_city_common.loading("show","正在处理....");
			var url = BasePath+'/bill_om_divide_different/toDivideDifferent';
			ajaxRequest(url,params,function(data){
				if(data.result=="success"){
					$('#order_dtl2_dataGrid').datagrid('load');
					alert("处理成功!");
				}else{
					alert(data.msg,2);
				}
				wms_city_common.loading();
			});
		}
	});
};

//验证建单的复核单
billOmDivide.checkNewRecheck = function(divideNo){
	var num = 0;
	var url = BasePath+'/bill_om_recheck/get_biz';
	var params = {locno:billOmDivide.locno,divideNo:divideNo,status:'10'};
	ajaxRequestAsync(url,params,function(data){
		num = data.length;
	});
	return num;
};


//初始化按箱分货规则
billOmDivide.initStoreRule = function(){
	var url = BasePath+ '/bill_store_rule/get_biz?status=1&locno='+billOmDivide.locno;
	var reqParam = {};
	//初始化分货规则
	ajaxRequest(url,reqParam,function(data){
		var rd = "";
		for(var i=0;i<data.length;i++){
			var checkStr = "checked = 'checked'";
			if(i>0){checkStr = "";}
			rd += "<input name='storeRuleRadio' type='radio' "+checkStr+" value='"+data[i].ruleNo+"'/>"+data[i].ruleName+"&nbsp;&nbsp;";
		}
		$("#storeRuleDiv").html("").html(rd);
	});
};

//修改退仓收货单
billOmDivide.toUpdate = function(){
	
	// 获取勾选的行
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");
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
			alert("只能修改状态为新建的单据!",1);
			return;
		}
		billOmDivide.loadDetail(item,'0');
	});
};



//删除分货单
billOmDivide.do_del = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	
        	var delTip = "";
        	var keyStr = [];
        	
        	$.each(checkedRows, function(index, item){
        		var reqParam = {locno:item.locno,divideNo:item.divideNo,creator:billOmDivide.loginName};
        		keyStr[keyStr.length] = reqParam;
        		if(item.status!=null&&item.status!=""&&item.status!="10"){
					delTip = "单据:"+item.divideNo+";";
				}
        	});
        	
        	if(delTip!=null && delTip!=""){
	    		delTip = delTip+"已处理，不能删除！";
	    		alert(delTip);
	    		return;
	    	}
        	
            //2.绑定数据
        	wms_city_common.loading("show");
            var url = BasePath+'/bill_om_divide/deleteBillOmDivide';
        	var data={
        	    "datas":JSON.stringify(keyStr)
        	};
        	$.post(url, data, function(data) {
        		wms_city_common.loading();
        		if(data.flag == "success"){
                	alert('删除成功!');
                    billOmDivide.loadDataGrid();
            	}else if(data.flag=='warn'){
                	alert(data.msg,1);
                	return;
                }else{
                	alert('操作异常！',1);
                	return;
                }
            }, "JSON").error(function() {
            	wms_city_common.loading();
            	alert('删除失败!',1);
            });
        	
        }  
    });  
};


//完结分货单
billOmDivide.do_complete = function(){
	
	var locno = $("#locnoHidden").val();
	var divideNo = $("#divideNoHidden").val();
	
	$.messager.confirm("确认","你确定要完结"+divideNo+"分货单", function (r){  
        if (r) {
            var url = BasePath+'/bill_om_divide/updateCompleteBillOmDivide';
        	var data = {locno:locno,divideNo:divideNo,creator:billOmDivide.loginName};
        	$.post(url, data, function(data) {
                var resultVo = data.resultVo;
                if(resultVo.errorCode!="0"){
                	if(resultVo.errorCode=="1"){
                		alert("完结成功!");
                		billOmDivide.closeWindow('showDetailDialog');
                        billOmDivide.loadDataGrid();
                	}else{
                		alert("完结失败："+resultVo.errorMessage);
                	}
                }else{
                	alert("存储过程返回空数据!");
                }
            }, "JSON").error(function() {
            	alert(' 完结失败!',1);
            });
        }  
    });
};


//关闭弹出窗
billOmDivide.closeWindow = function(id){
	$('#'+id).window('close');  
};
 

//查单
billOmDivide.selectBill=function(){
	returnTab('mainTab','单据查询');
	
};

//刷新
billOmDivide.reload = function(){
	var tempObj = $('#mainDataGrid');
    tempObj.datagrid( 'reload' );
};

//完结
billOmDivide.showLoginUserDialog = function(){
	$("#showLoginUserDialog").window('open');
	$('#roleidCon').combobox('clear');
	$('#loginUserDataGrid').datagrid('loadData', { total: 0, rows: [] });
};

//更新分货单
billOmDivide.updateBillLoginUser = function(){
//	var checkedItems = $('#mainDataGrid').datagrid('getChecked');
	var assignNames = $('#assignName').val();
	var assignNamesCh = $('#assignNameCh').val();
	var divideNo = $('#divideNo').val();
	if(assignNames==''||assignNames==null){
		alert('请选择分货人员');
		return;
	}
//	if(checkedItems.length == 0){
//		alert('请选择分货单');
//		return;
//	}
	
	var dataList = [];
//	$.each(checkedItems, function(index, item){
//		var reqParam = {locno:item.locno,divideNo:item.divideNo};
//		dataList[dataList.length] = reqParam;
//	}); 
	
	
	var reqParam = {locno:billOmDivide.locno,divideNo:divideNo};
	dataList[dataList.length] = reqParam;
	
	var url = BasePath+ '/bill_om_divide_dtl/updateBillOmDivideDtl';
	var effectRow = {assignNames:assignNames,assignNamesCh:assignNamesCh,updated:JSON.stringify(dataList)};
	wms_city_common.loading("show");
	ajaxRequest(url, effectRow, function(result){
		wms_city_common.loading();
		if(result == "success"){
			alert("保存成功!");
			$('#mainDataGrid').datagrid('reload');
			$('#loginUserDataGrid').datagrid('loadData', { total: 0, rows: [] });
			 billOmDivide.closeWindow('showDetailDialog');
		}else{
			alert('保存失败!');
		}
	});
};

//加载订单详情
billOmDivide.loadDetail = function(rowData,type){
	
	$('#currentStatus').val(rowData.status);
	var locno = rowData.locno;
	var divideNo = rowData.divideNo;
	var businessType = rowData.businessType;
	if(businessType == '1'){
		$("#toDivideDif").show();
	}else{
		$("#toDivideDif").hide();
	}
	
//	var status = rowData.status;
//	if(status == "13"){
//		$('#btnComplete').linkbutton('disable');
//	}else{
//		$('#btnComplete').linkbutton('enable');
//	}
	$("#locnoHidden").val(locno);
	$("#divideNoHidden").val(divideNo);
	
	var queryMxURL=BasePath+ '/bill_om_divide_dtl/dtl_list.json';
	var queryMxURL2=BasePath + '/bill_om_divide_different_dtl/dtl_list.json';
	var queryParams1={divideNo:divideNo,locno:locno};
	var queryParams2={divideNo:divideNo,locno:locno,filterDiffQty:'1'};
	var queryParams3={divideNo:divideNo,locno:locno,isDivide:'Y'};
//	$('#detailForm').form('load',BasePath+'/bill_om_divide/get?divideNo='+divideNo+"&locno="+locno);
	$('#detailForm').form('load',rowData);
	billOmDivide.loadGridDataUtil('order_dtl1_dataGrid', queryMxURL, queryParams1);
	billOmDivide.loadGridDataUtil('order_dtl2_dataGrid', queryMxURL, queryParams2);
	billOmDivide.loadGridDataUtil('order_dtl3_dataGrid', queryMxURL2, queryParams3);
	billOmDivide.showHideBtn(type);
	$("#showDetailDialog").window('open');
};

//隐藏显示按钮
billOmDivide.showHideBtn = function(type){
	if(type=='1'){
//		$('#btnComplete').hide();
		$('#btn-save').hide();
		$('#printDetail').show();
		$('#selectPeople').hide();
	}else{
//		$('#btnComplete').show();
		$('#btn-save').show();
		$('#printDetail').hide();
		$('#selectPeople').show();
	}
};

//搜索收货单
billOmDivide.searchReceipt = function(){
	
	var startCreatetm =  $('#startReciveDateCon').datebox('getValue');
	var endCreatetm =  $('#endReciveDateCon').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("收货日期开始日期不能大于结束日期");   
        return;   
    }
	
	//1.校验必填项
	var fromObj=$('#searchReceiptForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
	
	var fromObjStr=convertArray($('#searchReceiptForm').serializeArray());
	var queryMxURL=BasePath+'/bill_im_receipt/findReceiptByPage.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billOmDivide.locno;
	billOmDivide.loadGridDataUtil('mainDataGrid2', queryMxURL, reqParam);
	deleteAllGridCommon('mainDataGrid3');
};

//搜索人员
billOmDivide.searchLoginUser = function(){
	var fromObjStr=convertArray($('#searchLoginUserForm').serializeArray());
	var queryMxURL=BasePath+'/system_user/listSystemUser.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	var roleid = $('#roleidCon').combobox('getValue');
	if(roleid == '' || roleid == null){
		reqParam['roleid'] = "0";
	}
	billOmDivide.loadGridDataUtil('loginUserDataGrid', queryMxURL, reqParam);
};

//清空
billOmDivide.searchClear = function(id){
	$('#'+id).form("clear");
};

billOmDivide.searchMainClear = function(id){
	$('#'+id).form("clear");
	$('#brandNo').combobox("loadData",[]);
};

//选择人员
billOmDivide.selectLoginUser = function(){
	var checkedItems = $('#loginUserDataGrid').datagrid('getChecked');
	if(checkedItems.length == 0){
		alert("请选择分货人员!");
		return;
	}
	wms_city_common.loading("show");
	var str = "";
	var strCh = "";
	$.each(checkedItems, function(index, item){
		str += item.loginName+",";
		strCh += item.username+",";
	}); 
	if(str!=""){
		str = str.substring(0, str.length-1);
		$("#assignName").val(str);
	}
	if(strCh!=""){
		strCh = strCh.substring(0, strCh.length-1);
		$("#assignNameCh").val(strCh);
	}
	wms_city_common.loading();
	//关闭窗口
    billOmDivide.closeWindow('showLoginUserDialog');
};

//搜索数据
billOmDivide.searchData = function(){
	
	var startCreatetm =  $('#startCreatetmCondition').datebox('getValue');
	var endCreatetm =  $('#endCreatetmCondition').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	
	var startExpDate =  $('#startExpDateCondition').datebox('getValue');
	var endExpDate =  $('#endExpDateCondition').datebox('getValue');
	if(!isStartEndDate(startExpDate,endExpDate)){    
		alert("分货日期开始日期不能大于结束日期");   
        return;   
    } 
	
	 //1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_divide/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billOmDivide.locno;
	billOmDivide.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
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

//初始化状态
billOmDivide.initRoleid = function(){
	$('#roleidCon').combobox({
		 url:BasePath+'/authority_organization/role.json',
	     valueField:"roleId",
	     textField:"roleName",
	     panelHeight:150,
	     onSelect:function(record){
	    	 var uList=[];
	    	 $.each(record.userList,function(i,o){
	    		 o.roleName=record.roleName;
	    		 uList.push(o);
	    	 });
	    	 $('#loginUserDataGrid').datagrid({data:uList});
	     }
	});
};

//初始化分货储位
billOmDivide.initCellNo = function(){
	$('#cellNo').combobox({
		 url:BasePath+'/cm_defcell/findCmDefcellByArea?locno='+billOmDivide.locno,
	     valueField:"cellNo",
	     textField:"cellNo",
	     panelHeight:"auto"
	});
};

billOmDivide.user = {};
billOmDivide.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billOmDivide.userid = data.userid;
		billOmDivide.loginName = data.loginName;
		billOmDivide.locno = data.locno;
		billOmDivide.user = data;
	});
};
//JS初始化执行
$(document).ready(function(){
	//用户信息
	billOmDivide.initCurrentUser();
	billOmDivide.initStoreRule();
	billOmDivide.initBusinessType();
//	billOmDivide.initCellNo();
	billOmDivide.initRoleid();
	//状态
//	billOmDivide.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_DIVIDE_STATUS',{},false,billOmDivide.initStatus);
	
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_DIVIDE_STATUS',
			{},
			billOmDivide.status,
			null);

	//不加会致使页面双击window标题错位
	$('#showAddDialog,#showDetailDialog,#showLoginUserDialog').window({    
	    resizable:false,
	    draggable:false,
	    collapsible:false,
	    closed:true,
	    maximized:true,
	    minimizable:false,
	    maximizable:false,
	    modal:true   
	});
	wms_city_common.loadBrands('brandNoExp', true);
	
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('input[id=brandNo]', $('#searchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs);
	
	billOmDivide.expStatus();
	
		$('#order_dtl1_dataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billOmDivide.itemQty = data.footer[1].itemQty;
		   				billOmDivide.realQty = data.footer[1].realQty;
		   				billOmDivide.diffQty = data.footer[1].diffQty;
		   			}else{
		   				var rows = $('#order_dtl1_dataGrid').datagrid('getFooterRows');
			   			rows[1]['itemQty'] = billOmDivide.itemQty;
			   			rows[1]['realQty'] = billOmDivide.realQty;
			   			rows[1]['diffQty'] = billOmDivide.diffQty;
			   			$('#order_dtl1_dataGrid').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
		$('#order_dtl2_dataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
			   		if(data.footer[1].isselectsum){
			   			billOmDivide.itemQty2 = data.footer[1].itemQty;
			   			billOmDivide.realQty2 = data.footer[1].realQty;
			   			billOmDivide.diffQty2 = data.footer[1].diffQty;
			   		}else{
			   			var rows = $('#order_dtl2_dataGrid').datagrid('getFooterRows');
				   		rows[1]['itemQty'] = billOmDivide.itemQty2;
				   		rows[1]['realQty'] = billOmDivide.realQty2;
				   		rows[1]['diffQty'] = billOmDivide.diffQty2;
				   		$('#order_dtl2_dataGrid').datagrid('reloadFooter');
			   		}
			   	}
			}
		);
		$('#order_dtl3_dataGrid').datagrid(
			{
					'onLoadSuccess':function(data){
				   		if(data.footer[1].isselectsum){
				   			billOmDivide.itemQty3 = data.footer[1].itemQty;
				   		}else{
				   			var rows = $('#order_dtl3_dataGrid').datagrid('getFooterRows');
					   		rows[1]['itemQty'] = billOmDivide.itemQty3;
					   		$('#order_dtl3_dataGrid').datagrid('reloadFooter');
				   		}
				   	}
				}
		);
		$('#startCreatetmCondition').datebox('setValue',getDateStr(-2));
});
//========================状态========================
billOmDivide.status = {};
billOmDivide.statusFormatter = function(value, rowData, rowIndex){
	return billOmDivide.status[value];
};

//========================状态END========================

billOmDivide.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//打印
billOmDivide.printDetail = function(){
	// 获取勾选的行
	var checkedRows = $("#order_dtl1_dataGrid").datagrid("getSelected");
	if(checkedRows == null){
		alert('请在明细中选择要打印的记录!',1);
		return;
	}
	billOmDivide.loadPrint(checkedRows,"edit");
};
billOmDivide.loadPrint = function(rowData,type){
	var boxNo = rowData.boxNo;
	var itemNo = rowData.itemNo;
	var groupNo = rowData.groupNo;
	var resultData;
	var divideNo = $("#divideNo").val();
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_om_divide_dtl/printDetail',
        data: {
        	locno:billOmDivide.locno,
        	divideNo:divideNo,
        	boxNo:boxNo,
        	itemNo:itemNo,
        	groupNo:groupNo
        },
        success: function(result){
        	resultData = result;
		}
	});
	if(resultData.result!="success"){
		alert(resultData.msg);
		return;
	} else {
		var html = billOmDivide.getHtmlByTemplate(resultData,boxNo,itemNo,groupNo);
     	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
     	if(LODOP==null){
     		return;
     	}
		LODOP.SET_PRINT_PAGESIZE(3,'800','50',"");
		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
		LODOP.PREVIEW();
	}
};

billOmDivide.getHtmlByTemplate = function(data,boxNo,itemNo,groupNo){
	var html = "";
	html += "<table border='1' cellpadding='0' cellspacing='0' width='100%' >";
	html += "<tr><td colspan='3'>";
	html += "<table border='0' cellpadding='0' cellspacing='0' width='100%' >";
	html += "<tr style='height:30px;'>";
	html += "<td bgcolor='#fff' align='left' width='75%'><strong>箱号:</strong>" + boxNo + "</td>";
	html += "<td bgcolor='#fff' align='left'><strong>组:</strong>" + groupNo + "</td>";
	html += "</tr>";
	html += "<tr style='height:30px;'>";
	html += "<td bgcolor='#fff' align='left'><strong>货号:</strong>" + itemNo + "</td>";
	html += "<td bgcolor='#fff' align='left'><strong>总数:</strong>" + data.total + "</td>";
	html += "</tr>";
	html += "</table>";
	html += "</td></tr>";
	html += "<tr>";
	html += "<td bgcolor='#fff' align='center' width='45%'><strong>门店</strong></td>";
	html += "<td bgcolor='#fff' align='center' width='30%'><strong>尺码</strong></td>";
	html += "<td bgcolor='#fff' align='center'><strong>数量</strong></td>";
	html += "</tr>";
	for(var i = 0;length=data.rows.length,i<length;i++){
		html += "<tr style='height:30px;'>";
		html += "<td bgcolor='#fff' align='center'>"+data.rows[i].serialNo+ "-"+data.rows[i].storeNo+"</td>";//客户编码
		html += "<td bgcolor='#fff' align='center'>"+data.rows[i].sizeNo+"</td>";//尺码
		html += "<td bgcolor='#fff' align='center'>"+data.rows[i].itemQty+"</td>";//数量
		html += "</tr>";
	}
	html += "</table>";
	return html;
};

billOmDivide.loadSysNo = function(){
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

billOmDivide.expStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusExp"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OEM_STATUS',
			{},
			null,
			null);
};

billOmDivide.initBusinessType = function(){
	$('#businessType').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IM_RECEIPT_BUSINESS_TYPE',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto",
	     onSelect:function(record){
	    	 deleteAllGridCommon('mainDataGrid2');
	     }
	  });
};

billOmDivide.printSum = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要打印的记录!',1);
		return;
	}
        	
	var delTip = "";
	var keyStr = [];
	$.each(checkedRows, function(index, item){
		var reqParam = {locno:item.locno,divideNo:item.divideNo};
		keyStr[keyStr.length] = reqParam;
	});
    //2.绑定数据
	wms_city_common.loading("show");
    var url = BasePath+'/bill_om_divide/deleteBillOmDivide';
	var data={
	    "datas":JSON.stringify(keyStr)
	};
	$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			data:data,
			dataType : "json",
			url:BasePath+'/bill_om_divide/getBatchPrintInfo',
			success : function(data) {
				wms_city_common.loading();
				if(data.result=="success"){
					
			     	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
			     	if(LODOP==null){
			     		return;
			     	}
			     	LODOP.SET_PRINT_PAGESIZE(1,0,0,"A4");
			     	//****************测试数据S*********************
			     	/*var tem = data.list[0].dtlList[0];
			     	var array = [];
			     	for(var idx=0;idx<100;idx++){
			     		array[idx] = tem;
			     	}
			     	data.list[0].dtlList = array;*/
			     	//****************测试数据E*********************
			     	var list = data.list;
			     	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>"
			     	for(var i=0,length=list.length;i<length;i++){
			     		var data = list[i];
			     		var dtlList = data.dtlList;
			     		var sourceNoList =data.sourceNoList;
			     		var sourceStr = "";
			     		var total = billOmDivide.getItemQtyTotal(dtlList);
			     		for(var j=0,lengthsub =sourceNoList.length;j<lengthsub;j++ ){
			     			if(j<lengthsub-1){
			     				sourceStr +=sourceNoList[j]+",";
			     			}else{
			     				sourceStr +=sourceNoList[j];
			     			}
			     		}
			     		var headHtml =  billOmDivide.getHtmlByTemplateHead(data.divideNo,sourceStr,total);
		    			var bodyHtml = billOmDivide.getHtmlByTemplateBody(dtlList,total,data.divideNo,sourceStr);
			     		
		    			//设置表格内容
		    			LODOP.ADD_PRINT_TABLE(100,0,"100%",400,strStyle+bodyHtml);
		    			LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
		    			
		    			
		    			//设置表格头部
		    			LODOP.ADD_PRINT_HTM(0,0,"100%",90,headHtml);
		    			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		    			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		    			
		    			//设置条码
		    			LODOP.ADD_PRINT_BARCODE(40,10,250,40,"128A",data.divideNo);
		    			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		    			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		    			
		    			LODOP.NewPageA();
			     	}
			     	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
					LODOP.PREVIEW();
				}else{
					alert(data.msg);
				}
			}
		});
};

billOmDivide.getItemQtyTotal = function(list){
	var total = 0;
	for(var idx=0;idx<list.length;idx++){
		total += list[idx].itemQty;
	}
	return total;
};

billOmDivide.getHtmlByTemplateHead = function(divideNo,sourceStr,total){
	var html = "";
	html +="<table style='width:100%;' border='0' cellpadding='1' cellspacing='1'><tr style='height:30px'><td  colspan='6' align='center' style=';font-weight:bold;font-size:16px;'>分货任务汇总单</td></tr>" +
			"<tr style='height:50px'><td colspan='6'></td></tr>";
	html +="</table>";
	return html;
};

billOmDivide.getHtmlByTemplateBody= function(dtlList,total,divideNo,sourceStr){
	var html =  "<table border='1' cellspacing='0' cellpadding='1' width='100%' style='border-collapse:collapse;word-break:break-all; overflow:auto' bordercolor='#333333'>";
	html += "<thead><tr>";
	html += "<td bgcolor='#fff' align='center' width='15%'>流道号</td>";
	html += "<td bgcolor='#fff' align='center'>客户编码</td>";
	html += "<td bgcolor='#fff' align='center'>客户名称</td>";
	html += "<td bgcolor='#fff' align='center'>计划分货数量</td>";
	html += "</tr></thead>";
	for(var i = 0;length=dtlList.length,i<length;i++){
		html += "<tr>";
		html += "<td bgcolor='#fff' align='center'>"+dtlList[i].serialNo+"</td>";
		html += "<td bgcolor='#fff' align='center'>"+dtlList[i].storeNo+"</td>";
		html += "<td bgcolor='#fff' align='center'>"+dtlList[i].storeName+"</td>";
		html += "<td bgcolor='#fff' align='center'>"+dtlList[i].itemQty+"</td>";
		html += "</tr>";
	}
	html += "<tr>";
	html += "<td bgcolor='#fff' align='center'>分货任务单</td>";
	html += "<td bgcolor='#fff' align='left' colspan='3'>"+divideNo+"</td>";
	html += "</tr>";
	html += "<tr>";
	html += "<td bgcolor='#fff' align='center'>收货单号</td>";
	html += "<td bgcolor='#fff' align='left' colspan='3' sytle='width:79%;'>"+sourceStr+"</td>";
	html += "</tr>";
	html += "<tr>";
	html += "<td bgcolor='#fff' align='center'>合计</td>";
	html += "<td bgcolor='#fff' align='left' colspan='3'>"+total+"</td>";
	html += "</tr>";
	html += "</table>";
	return html;
};


billOmDivide.exportDtl = function(){
	exportExcelBaseInfo('order_dtl1_dataGrid','/bill_om_divide_dtl/do_export.htm','分货单明细');
}



