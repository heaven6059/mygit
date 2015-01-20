
var billumcheck = {};
billumcheck.allLocno = [];//所以仓库  add by wangb 20140716
billumcheck.allOtherLocnoList = [];
billumcheck.allOtherLocnoStr = [];
billumcheck.statusType = "10";//新建
billumcheck.lookupcode_untread_type = "ITEM_TYPE";
billumcheck.lookupcode_untread_status= "CITY_UM_CHECK_STATUS";

billumcheck.status11 = "11";//审核
billumcheck.status13 = "13";//差异验收
billumcheck.status40 = "40";//已转门店
billumcheck.status30 = "30";//已转门店
billumcheck.convertType2 = "2";//已转门店
//billumcheck.qualityDataObj = {};
//打开窗口
billumcheck.openWindow = function(windowId){
	$('#'+windowId).window('open');
};


//关闭窗口
billumcheck.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};


//清空
billumcheck.searchClear = function(id){
	$('#'+id).form("clear");
	$('#brandNo').combobox("loadData",[]);
};


//加载Grid数据Utils
billumcheck.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};


//插入行，开始编辑
billumcheck.insertRowAtEnd = function(gid,rowData){
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


billumcheck.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		tempObj.datagrid('endEdit', i);
    	}else{
    		return false;
    	}
    }
    return true;
};

/**********************************************新增编辑 **************************************/
//新建
billumcheck.add = function(){
	
	//billumcheck.loadAdd('0');
	$("#ownerNo").combobox('disable');
	$("#ownerNoHide").attr("disabled",false);
	
	$("#checkType").combobox('enable');
	
	$("#itemType").combobox('disable');
	$("#itemTypeHide").attr("disabled",false);
	$("#quality").combobox('disable');
	$("#qualityHide").attr("disabled",false);
	$("#btnUntread").attr('disabled',false);
	$("#btn-save-detail").show();
	$("#btn-edit-detail").hide();
	var fromObj=$('#dataForm');
	fromObj.form("clear");
	deleteAllGridCommon('checkDtlDg');
	$("#toolCheckDtlDiv").hide();
	$('#openUIDetail').window({title:'新增'});  
	$("#openUIDetail").window('open');
	//替换保存按钮
	//$('#btn-save .l-btn-left').html('<span class="l-btn-text icon-save l-btn-icon-left l-btn-focus">保存</span>');
};

//保存退仓验收单
billumcheck.save_main = function(){
	$("#btnUntread").attr("disabled",false);
	var fromObj=$('#dataForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    
    $("#checkTypeHide").val($("#checkType").combobox("getValue"));
    
    var tempFlag = billumcheck.endEdit("checkDtlDg");
	if(!tempFlag){
		return;
	}
	var tempObj = $('#checkDtlDg');
	var inserted = encodeURIComponent(JSON.stringify(tempObj.datagrid('getChanges', "inserted")));
	var updated = encodeURIComponent(JSON.stringify(tempObj.datagrid('getChanges', "updated")));
	var deleted = encodeURIComponent(JSON.stringify(tempObj.datagrid('getChanges', "deleted")));
	
	var paramData = billumcheck.strToObj(decodeURIComponent(fromObj.serialize(),true));
	paramData.inserted = inserted;
	paramData.updated = updated;
	paramData.deleted = deleted;
    
    wms_city_common.loading("show","正在生成退仓验收单......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:paramData,
		url:BasePath+'/bill_um_check/addMain',
		success : function(data) {
			if(data.result=="success"){
				alert('新增成功!');
				$("#checkDtlDg").datagrid("acceptChanges");
				//billumcheck.refresh("checkDtlDg");
				$("#btn-save-detail").hide();
				$("#btn-edit-detail").show();
				
				$("#checkType").combobox('disable');
				$("#checkTypeHide").attr("disabled",false);
				
	 			$("#checkNo").val(data.checkNo);
	 			//billumuntread.mainInfoShow("edit");
	 			billumcheck.refresh("dataGridJG");
	 			//billumcheck.refresh("checkDtlDg");
	 		    $("#toolCheckDtlDiv").show();
			}else{
				alert(data.msg,2);
			}
			wms_city_common.loading();
		}
	});
	/*
	var tempObj = $('#checkDtlDg');
		var fromObj = $('#dataForm');
		var inserted = tempObj.datagrid('getChanges', "inserted");
		var deleted = tempObj.datagrid('getChanges', "deleted");
		var updated = tempObj.datagrid('getChanges', "updated");
				  //主档信息
		var ownerNo = $("input[id=ownerNo]",fromObj).combobox('getValue');
		var itemType = $("input[id=itemType]",fromObj).combobox('getValue');
		var checkNo = $("input[id=checkNo]",fromObj).val();
		var untreadNo = $("input[id=untreadNo]",fromObj).val();
		var untreadType = $("input[id=untreadType]",fromObj).val();
		var checkWorker = $("input[id=checkWorker]",fromObj).val();
		var checkEndDate = $('#checkEndDate').datebox('getValue');
		var remark = $("input[id=remark]",fromObj).val();
				  //1.校验必填项
		var fromObj=$('#dataForm');
		var validateForm= fromObj.form('validate');
		if(validateForm==false){
			 return;
		}
				  //验证明细非空
		var checkDtlGd = tempObj.datagrid('getRows');
		if(checkDtlGd.length < 1){
			alert("退仓验收单明细不能为空!");
			return;
		}
		//验证明细
		var tempFlag = billumcheck.endEdit("checkDtlDg");
		if(!tempFlag){
			alert('数据验证没有通过!',1);
			return;
		}
				  //3. 保存
		var saveFn = function(returnData){
			var effectRow = {
					inserted:JSON.stringify(inserted),
					deleted:JSON.stringify(deleted),
					updated:JSON.stringify(updated),
					"ownerNo":ownerNo,
					"locno":billumcheck.locno,
					"checkNo":checkNo,
					"itemType":itemType,
					"untreadNo":untreadNo,
					"untreadType":untreadType,
					"checkWorker":checkWorker,
					"checkEndDate":checkEndDate,
					"remark":remark,
					"createtm":returnData.currentDate19Str,
					"creator":returnData.loginName,
					"editor":returnData.loginName,
					"edittm":returnData.currentDate19Str
			};
						  $.post(BasePath+'/bill_um_check_dtl/saveBillUmCheck', effectRow, function(data) {
				if(data.flag == "true"){
					alert('保存成功!');
					billumcheck.closeWindow('openUIDetail');
					billumcheck.loadDataGrid();
				}else if(data.flag=='warn'){
					alert(data.msg,1);
					return;
				}else{
					alert('操作异常！',1);
					return;
				}
			}, "JSON").error(function() {
				alert('保存失败!',1);
			});
		};
				  ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);*/
	
};

//修改
billumcheck.edit = function(){
	// 获取勾选的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}
	
	if(checkedRows.length > 1){
		alert('只能选择一条记录!',1);
		return;
	}
	var allOk = true;
	$.each(checkedRows,function(index,item){
		var status = item.status;
		if(status!=billumcheck.statusType){
			allOk = false;
			return false;
		}
	});
	
	if(!allOk){
		alert("非建单状态的数据不能修改!",1);
		return;
	}
	
	var rowData = checkedRows[0];
	$('#dataForm').form('load',rowData);
	$("#btn-save-detail").hide();
	$("#btn-edit-detail").show();
	$("#btnUntread").attr("disabled",true);
	
	$("#ownerNo").combobox("disable");
	$("#ownerNoHide").attr("disabled",false);
	$("#checkType").combobox("disable");
	$("#checkTypeHide").attr("disabled",false);
	$("#itemType").combobox("disable");
	$("#itemTypeHide").attr("disabled",false);
	$("#quality").combobox('disable');
	$("#qualityHide").attr("disabled",false);
	
	$('#openUIDetail').window({title:'修改'});  
	$("#openUIDetail").window('open');
	$("#toolCheckDtlDiv").show();
	var queryMxURL=BasePath+'/bill_um_check_dtl/list.json?locno='+billumcheck.locno+'&checkNo='+rowData.checkNo+'&ownerNo='+rowData.ownerNo;
	$("#checkDtlDg").datagrid( 'options' ).url = queryMxURL;
	$("#checkDtlDg").datagrid( 'load' );
};

billumcheck.strToObj = function(str){    
    str = str.replace(/&/g,"','");    
    str = str.replace(/=/g,"':'");    
    str = "({'"+str +"'})";    
    obj = eval(str);     
    return obj;    
};    

billumcheck.edit_main = function(){
	$("#btnUntread").attr("disabled",true);
	var fromObj=$('#dataForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	/*var inserted = encodeURIComponent(JSON.stringify(tempObj.datagrid('getChanges', "inserted")));
	var updated = encodeURIComponent(JSON.stringify(tempObj.datagrid('getChanges', "updated")));
	var deleted = encodeURIComponent(JSON.stringify(tempObj.datagrid('getChanges', "deleted")));*/
	
	var paramData = billumcheck.strToObj(decodeURIComponent(fromObj.serialize(),true));
	/*paramData.inserted = inserted;
	paramData.updated = updated;
	paramData.deleted = deleted;*/
    wms_city_common.loading("show","正在保存退仓验收单......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:paramData,
		url:BasePath+'/bill_um_check/editMain',
		success : function(data) {
			if(data.result=="success"){
				alert('修改成功!');
				//billumcheck.refresh();
				$("#btn-save-detail").hide();
				$("#btn-edit-detail").show();
	 			//billumuntread.mainInfoShow("edit");
	 			billumcheck.refresh("dataGridJG");
	 			$("#toolCheckDtlDiv").show();
			}else{
				alert(data.msg,2);
				//alert('修改失败,请联系管理员!',2);
			}
			wms_city_common.loading();
		}
	});
};

//打开查询退仓通知单界面
billumcheck.openSelect = function(){
	$("#openUIUn").window('open');
	$("#unSearchForm").form("clear");
	$('#dataGridUn').datagrid('loadData', { total: 0, rows: [] });
};


//打开查询退仓通知单界面
billumcheck.openUIUn = function(){
	$('#openUIUn').window({title:'退仓通知单'});  
	$("#openUIUn").window('open');
	$('#dataGridUn').datagrid('loadData', { total: 0, rows: [] });
};


//打开查询退仓通知单界面
billumcheck.splitBox = function(){
	var selectRows = $("#checkDtlDg").datagrid("getSelected");
	if(selectRows==null){
		alert("请选择要添加差异商品的行",1);
		return;
	}
	$('#openUIItem').window({title:'商品选择'});  
	$("#openUIItem").window('open');
	$('#dataGridItem').datagrid('loadData', { total: 0, rows: [] });
	//查询店退仓单下明细的所有箱子
	$.ajax({
		async : true,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:{
			locno: billumcheck.locno,
			ownerNo:$("#ownerNo").combobox("getValue"),
			untreadNo:$("#untreadNo").val()
		},
		url:BasePath+'/bill_um_check_dtl/selectAllBox.json',
		success : function(data) {
			$('#boxNoList').combobox({
			     valueField:"boxNo",
			     textField:"boxNo",
			     data:data.data,
			     panelHeight:150
			});
			$('#boxNoList').combobox("select",selectRows.boxNo);
		}
	});
};


//查询主表数据
billumcheck.searchData = function(){
	
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
	
	var startCreatetm = $('#startCreatetmCondition').datebox('getValue');
	var endCreatetm = $('#endCreatetmCondition').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	
	var startAudittm = $('#startAudittmCondition').datebox('getValue');
	var endAudittm = $('#endAudittmCondition').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    }
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_check/list.json?locno='+billumcheck.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billumcheck.locno;
	billumcheck.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
	
	 //加载合计
    var url=BasePath+'/bill_um_check/findSumQty.json?locno='+billumcheck.locno;
    wms_city_common.dataSumFooter({
    								'url':url,//选填，当不填时默认为BasePath+'/billimimport'+findSumQty.json
    								'columName': billumcheck.gridFooterColumName,
									'documentObj': $('#dataGridJG'),
									'queryParams': reqParam
    								
    });
};


//查询退仓通知单数据
billumcheck.searchUn = function(){
	var startCreatetm = $('#createtmStart_select').datebox('getValue');
	var endCreatetm = $('#createtmEnd_select').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
    
    var startAudittm = $('#audittmStart_select').datebox('getValue');
	var endAudittm = $('#audittmEnd_select').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
	}
	
	var fromObjStr=convertArray($('#unSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_untread/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billumcheck.locno;
	reqParam['statusMore'] = billumcheck.status11;
	billumcheck.loadGridDataUtil('dataGridUn', queryMxURL, reqParam);
};

//查询主表数据
billumcheck.searchStoreData = function(){
	var fromObjStr=convertArray($('#searchStoreForm').serializeArray());
	var queryMxURL=BasePath+'/store/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['storeNoDc'] = billumcheck.locno;
	reqParam['storeType'] = '11';
	reqParam['status'] = '0';
	reqParam['sysNo'] = $('#sysNoSelect').val();
	billumcheck.loadGridDataUtil('storeDg', queryMxURL, reqParam);
};

//查询商品数据
billumcheck.searchFilterItem = function(){
	var fromObjStr=convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_check_dtl/selectItem.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	billumcheck.loadGridDataUtil('dataGridItem', queryMxURL, reqParam);
};


//查询商品数据
billumcheck.clearConvertWindow = function(type){
	if(type == '0'){
		$('#tcdepaNo').combobox('setValue','');
		$('#storeNoLoc').combobox('setValue','');
	}
	if(type == '1'){
		$('#storeDg').datagrid('loadData', { total: 0, rows: [] });
		$('#storeDg2').datagrid('loadData', { total: 0, rows: [] });
		$('#searchStoreForm').form('clear');
	}
};

//数据右移
billumcheck.toRgiht = function(){
	var selectRows = $('#storeDg').datagrid('getChecked');
	if(selectRows.length < 1){
		alert("请选择客户!");
		return;
	}
	for (var i = selectRows.length - 1; i >= 0; i--) {
		var datas = {
			storeNo : selectRows[i].storeNo,
			storeName : selectRows[i].storeName
		};
		$('#storeDg2').datagrid('appendRow',datas);
        var index = $('#storeDg').datagrid('getRowIndex', selectRows[i]);
        $('#storeDg').datagrid('deleteRow', index);
    }
	$('#storeDg').datagrid('load');
};

//数据右移
billumcheck.toLeft = function(){
	var selectRows = $('#storeDg2').datagrid('getChecked');
	if(selectRows.length < 1){
		alert("请选择客户!");
		return;
	}
	for (var i = selectRows.length - 1; i >= 0; i--) {
		var datas = {
			storeNo : selectRows[i].storeNo,
			storeName : selectRows[i].storeName
		};
		$('#storeDg').datagrid('appendRow',datas);
        var index = $('#storeDg2').datagrid('getRowIndex', selectRows[i]);
        $('#storeDg2').datagrid('deleteRow', index);
    }
};


//选择退仓单
billumcheck.selectUntread = function(){
	
	var checkRow = $('#dataGridUn').datagrid('getChecked');
	if(checkRow.length < 1){
		alert("请选择店退仓单!");
		return;
	}
	
	//设置主档信息
	var rowData = checkRow[0];
	rowData.itemType = rowData.untreadType;
	
	$("#dataForm").form("load",rowData);
	
	$("#ownerNo").combobox("disable");
	$("#ownerNoHide").attr("disabled",false);
	$("#itemType").combobox("disable");
	$("#itemTypeHide").attr("disabled",false);
	$("#quality").combobox('disable');
	$("#qualityHide").attr("disabled",false);
	
	billumcheck.clearUntreadData();
	wms_city_common.loading('show');
	$.each(checkRow,function(index,item){
		
		//加入通知单号
		$('#untreadNo').val(item.untreadNo);
		$('#untreadType').val(item.untreadType);
		
		var url = BasePath+'/bill_um_untread_dtl/get_biz';
		var params = {untreadNo:item.untreadNo,locno:billumcheck.locno,ownerNo:item.ownerNo,isReceipt:true};
		ajaxRequest(url,params,function(data){
			var rowList = data;
			for(var i = 0;i < rowList.length;i++){
				var reqParams = {
						itemNo:rowList[i].itemNo,
						itemName:rowList[i].itemName,
						colorName:rowList[i].colorName,
						sizeNo:rowList[i].sizeNo,
						itemQty:rowList[i].itemQty,
						checkQty:rowList[i].itemQty,
						boxNo:rowList[i].boxNo,
						brandNo:rowList[i].brandNo
					};
				
				//插入商品到父窗口,开始编辑
				billumcheck.insertRowAtEnd('checkDtlDg',reqParams);
			}
			//关闭load提示
			wms_city_common.loading();
		});
	 });
	
	//关闭退仓通知单选择窗口
	billumcheck.closeWindow('openUIUn');
	$('#checkDtlDg').datagrid('beginEdit');
};


//选择串码商品
billumcheck.selectItem = function(){
	
	var checkRow = $('#dataGridItem').datagrid('getChecked');
	if(checkRow.length < 1){
		alert("请选择商品!");
		return;
	}
	wms_city_common.loading('show');
	$.each(checkRow,function(index,item){
		var reqParams = {
				itemNo:item.itemNo,
				itemName:item.itemName,
				colorName:item.colorName,
				sizeNo:item.sizeNo,
				itemQty:0,
				checkQty:0,
				addFlag:"1",
				boxNo:$('#boxNoList').combobox("getValue"),
				brandNo:item.brandNo
			};
		
		//插入商品到父窗口,开始编辑
		billumcheck.insertRowAtEnd('checkDtlDg',reqParams);
		//关闭load提示
		wms_city_common.loading();
	 });
	
	//关闭商品选择窗口
	billumcheck.closeWindow('openUIItem');
	$('#checkDtlDg').datagrid('beginEdit');
};

//删除明细

billumcheck.delCheckDtl = function(){
	var checkRow = $('#checkDtlDg').datagrid('getSelected');
	if(checkRow==null){
		alert("请选择要删除的记录",1);
		return;
	}
	if(checkRow.addFlag!="1"&&checkRow.itemQty!=0){
		alert("非新增商品不能删除",1);
		return;
	}
	
	var obj = $('#checkDtlDg');
	var index = obj.datagrid('getRowIndex', checkRow);
	$('#checkDtlDg').datagrid('deleteRow',index);
};

//退仓验收单详情
billumcheck.loadDetail = function(rowData,type){
	$('#openUIDetail_view').window({title:'明细'});  
	$("#openUIDetail_view").window('open');
	$('#dataForm_view').form('load',rowData);
	deleteAllGridCommon('checkDtlDg_view');
	var queryMxURL=BasePath+'/bill_um_check_dtl/dtl_list.json?locno='+billumcheck.locno+'&checkNo='+rowData.checkNo+'&ownerNo='+rowData.ownerNo+'&untreadNo='+rowData.untreadNo;
	var urlSum=BasePath+'/bill_um_check_dtl/findDtlSumQty.json?locno='+billumcheck.locno+'&checkNo='+rowData.checkNo+'&ownerNo='+rowData.ownerNo+'&untreadNo='+rowData.untreadNo;
	$("#checkDtlDg_view").datagrid( 'options' ).url = queryMxURL;
    $("#checkDtlDg_view").datagrid( 'load' );
    
    wms_city_common.dataSumFooter({
		'url':urlSum,
		'columName':billumcheck.gridFooterColumDtlName,
		'documentObj': $('#checkDtlDg_view')
 });
    
};
//加载明细

billumcheck.loadDetail4checkBox = function(rowData){
	var queryMxURL=BasePath+'/bill_um_check_dtl/dtl_list.json?locno='+billumcheck.locno+'&checkNo='+rowData.checkNo+'&ownerNo='+rowData.ownerNo;
	$("#checkGridDetail").datagrid( 'options' ).url = queryMxURL;
    $("#checkGridDetail").datagrid( 'load' );
};


//加载dataGrid
billumcheck.loadDataGrid = function(){
	//$('#dataGridJG').datagrid({'url':BasePath+'/bill_um_check/listBillUmCheck.json','pageNumber':1 });
	$('#dataGridJG').datagrid('reload');
};


//加载退仓验收单明细
billumcheck.loadCheckData = function(rowData,type){
	
	billumcheck.currentSelectType = type;
	var checkNo = rowData.checkNo;
	var ownerNo = rowData.ownerNo;
	var itemType = rowData.itemType;
	
	var url = BasePath+'/bill_um_check/get';
	var params = {checkNo:checkNo,locno:billumcheck.locno,ownerNo:ownerNo};
	ajaxRequest(url,params,function(data){
		$('#dataForm').form('load',data);
		if(type == '1' || type == '2'){
			$("#ownerNo").combobox('disable');
			$("#itemType").combobox('disable');
			$("#checkEndDate").datebox('disable');
			$("#btnUntread").attr('disabled',true);
		}else{
			$("#ownerNo").combobox('enable');
			$("#itemType").combobox('enable');
			$("#checkEndDate").datebox('enable');
			$("#btnUntread").attr('disabled',false);
		}
		if(data.itemType=='1'){
			$('#itemTypeTitle').html("退仓收货单号");
		}else{
			$('#itemTypeTitle').html("退仓通知单号");
		}
	});
	
	var dtlParams = {checkNo:checkNo,locno:billumcheck.locno,ownerNo:ownerNo,itemType:itemType};
	var queryMxURL=BasePath+'/bill_um_check_dtl/listBillUmCheckDtl.json';
	billumcheck.loadGridDataUtil('checkDtlDg', queryMxURL, dtlParams);
	
};

//加载退仓验收单完成执行编辑
billumcheck.loadSuccess = function(){
	if(billumcheck.currentSelectType=="0"){
		var rows = $('#checkDtlDg').datagrid('getRows');
		$.each(rows,function(index,item){
			$('#checkDtlDg').datagrid('beginEdit', index);
		});
	}
};

//加载新增明细
billumcheck.loadAdd = function(type){
	$('#openUIDetail').window({title:'新增'});  
	$("#openUIDetail").window('open');
	//$('#dataForm')[0].reset();
	$('#dataForm').form('clear');
	//$('#itemType').combobox('setValue','0');
	$('#checkDtlDg').datagrid('loadData', { total: 0, rows: [] });
	//$('#checkEndDate').val(billumcheck.currentYmd);
	billumcheck.showHideBtn(type);
};


//加载修改明细
billumcheck.loadUpdate = function(type){
	$('#openUIDetail').window({title:'修改/查看'});  
	$("#openUIDetail").window('open');
	billumcheck.showHideBtn(type);
};


//保存明细
billumcheck.saveCheckDtl = function(){
	var tempFlag = billumcheck.endEdit("checkDtlDg");
	if(!tempFlag){
		return;
	}
	
	var tempObj = $('#checkDtlDg');
	var inserted = tempObj.datagrid('getChanges', "inserted");
	var updated = tempObj.datagrid('getChanges', "updated");
	var deleted = tempObj.datagrid('getChanges', "deleted");
	
	//验证新增的列
	var addTipStr = billumcheck.checkDetailIsPass(inserted);
    if(addTipStr != ""){
		alert(addTipStr);
		return;
	}
    
    //验证修改过的列
    var updateTipStr = billumcheck.checkDetailIsPass(updated);
    if(updateTipStr != ""){
		alert(updateTipStr);
		return;
	}
	
	wms_city_common.loading("show","正在保存退仓验收明细......");
	$.ajax({
		  async : true,
		  cache: true,
		  type: 'POST',
		  url: BasePath+'/bill_um_check_dtl/saveDetail',
		  data: {
		  	checkNo:$("#checkNo").val(),
		  	ownerNo:$("#ownerNo").combobox("getValue"),
		  	untreadNo:$("#untreadNo").val(),
		  	inserted:encodeURIComponent(JSON.stringify(inserted)),
		  	updated:encodeURIComponent(JSON.stringify(updated)),
		  	deleted:encodeURIComponent(JSON.stringify(deleted))
		  },
		  success: function(data){
		  	 if(data.result=='success'){
		  		alert("保存成功");
				$("#openUIDetail").window('close');
				billumcheck.refresh("dataGridJG");
				$("#checkDtlDg").datagrid("acceptChanges");
		 	}else{
			 	alert(data.msg,2);
		 	}
		  	wms_city_common.loading();
		  }
	});
};


billumcheck.checkDetailIsPass = function(listDatas){
	var tipStr = "";
	$.each(listDatas, function(index, item){
		
		if(item.checkQty == "0" && item.itemQty == "0"){
			tipStr = "箱号："+item.boxNo + "商品："+item.itemNo + "尺码："+item.sizeNo+"串款验收数量必须大于0";
			return;
		}
		
	});
	return tipStr;
};


billumcheck.endEdit = function(gid){
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

//验收类型选择事件触发
billumcheck.onChangeClear = function(rec){
	if(rec.itemvalue=='1'){
		$('#itemTypeTitle').html("退仓收货单号");
	}else{
		$('#itemTypeTitle').html("退仓通知单号");
	}
	billumcheck.clearUntreadData();
};





//删除退仓验收单
billumcheck.doDel = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var allOk =true;
	$.each(checkedRows,function(index,item){
		var status = item.status;
		if(status!=billumcheck.statusType){
			allOk = false;
			return false;
		}
	});
	
	if(!allOk){
		alert("非建单状态的数据不能删除!",1);
		return;
	}
	
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
		
        if (r) {
        	var keyStr = [];

        	$.each(checkedRows, function(index, item){
        		var r = {locno:billumcheck.locno,checkNo:item.checkNo,ownerNo:item.ownerNo};
        		keyStr[keyStr.length] = r;
        	}); 
            //2.绑定数据
            var url = BasePath+'/bill_um_check/deleteBillUmCheck';
        	var data={
        	    "keyStr":JSON.stringify(keyStr)
        	};
        	//3. 删除
        	wms_city_common.loading("show","正在删除......");
        	ajaxRequest(url,data,function(result){
        		wms_city_common.loading();
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
        			 billumcheck.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });
};


//审核退仓验收单
billumcheck.doAudit = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}
	var allOk =true;
	$.each(checkedRows,function(index,item){
		var status = item.status;
		if(status!=billumcheck.statusType){
			allOk = false;
			return false;
		}
	});
	if(!allOk){
		alert("非建单状态的数据不能审核!",1);
		return;
	}
	
	$.messager.confirm("确认","你确定要审核这"+checkedRows.length+"条数据", function (r){  
		
        if (r) {
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.ownerNo+"|"+item.checkNo+"|"+item.untreadNo);
        	});
        	var url = BasePath+'/bill_um_check/auditCheck';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	wms_city_common.loading("show","正在审核......");
        	$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				data:data,
				dataType : "json",
				url:url,
				success : function(data) {
					wms_city_common.loading();
						if(data.result=="success"){
							alert('审核成功!');
			 				billumcheck.refresh("dataGridJG");
						}else{
							alert(data.msg,2);
						}
					}
			});
   	     }  
    });
};


//隐藏显示按钮
billumcheck.showHideBtn = function(type){
	if(type=='1'){
		$('#btn-save').hide();
		$('#info_add').hide();
		$('#info_remove').hide();
		$('#btnUntread').hide();
		$("#toolCheckDtlDiv").css({"height":"0px","padding":"0px","border":"0px"});
	}else{
		$('#btn-save').show();
		$('#info_add').show();
		$('#info_remove').show();
		$('#btnUntread').show();
		$('#itemTypeTitle').html("退仓通知单号");
		$("#toolCheckDtlDiv").css({"height":"auto","padding":"auto","border":"0px"});
	}
};


//清空通知单文本和明细
billumcheck.clearUntreadData=function(){
	$('#checkDtlDg').datagrid('loadData', { total: 0, rows: [] });
};

billumcheck.statusFormatter = function(value, rowData, rowIndex){
	return billumcheck.status[value];
};
//差异数量
billumcheck.diffQty = function(value, rowData, rowIndex){
	return rowData.checkQty-rowData.itemQty;
};

billumcheck.initconvertType = function(){
	wms_city_common.comboboxLoadFilter(
			["convertTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CON_CONVERT_GOODS_CONVERT_TYPE',
			{},
			null,
			null);
	
};

//初始化状态
billumcheck.status = {};
billumcheck.initStatus = function(data){
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumcheck.lookupcode_untread_status,
			{},
			billumcheck.status,
			null);
};


//初始化验收类型
billumcheck.typeData = {};
billumcheck.inititemType = function(){
	wms_city_common.comboboxLoadFilter(
			["itemType_search","itemType","itemType_view","itemType_box","itemTypevalues","chooseitemType"],
			null,
			null,
			null,
			true,
			[true, false, false, true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
			{},
			billumcheck.typeData,
			null);
};

//初始化退仓验收类型
billumcheck.checkTypeData = {};
billumcheck.initcheckType = function(){
	wms_city_common.comboboxLoadFilter(
			["checkType_search","checkType","checkType_view","checkType_box"],
			null,
			null,
			null,
			true,
			[true, false, false, true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_UM_CHECK_TYPE',
			{},
			billumcheck.checkTypeData,
			null);
};

//初始化委托业主代码
billumcheck.ownerFt={};
billumcheck.initOwnerNo = function(){
	wms_city_common.comboboxLoadFilter(
			["ownerNo","ownerNoCondition","ownerNo_view","ownerNo_select"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[false, true, false, false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billumcheck.ownerFt,
			null);
	
};


//初始化客户信息
billumcheck.initStoreNo = function(inputId){
	$('#'+inputId).combogrid({
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


//品牌库
billumcheck.initSysNo = function(){
	$('#sysNoConIt').combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
	    valueField:"itemvalue",
	    textField:"itemnamedetail",
	    panelHeight:150
	});
};

//==================商品类型=======================
billumcheck.qualityFormatter = function(value, rowData, rowIndex){
	return billumcheck.areaQuality[value];
};
billumcheck.areaQuality = {};
billumcheck.initQuality = function(data){
	  wms_city_common.comboboxLoadFilter(
				["quality","quality_view","quality_search"],
				null,
				null,
				null,
				true,
				[false, false, true],
				BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
				{},
				billumcheck.areaQuality,
				null);
};
billumcheck.tansforListToMap = function(oriData){
	var str = "";
	for(var i = 0,length = oriData.length;i<length;i++){
		str = str+"\""+oriData[i].itemvalue +"\":\""+oriData[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};

//初始化当前登录的用户 信息
billumcheck.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billumcheck.userid = data.userid;
		billumcheck.loginName = data.loginName;
		billumcheck.currentDate = data.currentDate19Str;
		billumcheck.currentYmd = data.currentDate10Str;
		billumcheck.locno = data.locno;
	});
};



billumcheck.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

billumcheck.converStr2JsonObj2= function(data){
	if(data==null){
		 return "";
	}
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billumcheck.converStr2JsonObj= function(data){
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


//将数组封装成一个map
billumcheck.converStr2JsonLookupObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//委托业主
billumcheck.ownerFormatter = function(value, rowData, rowIndex){
	return billumcheck.ownerFt[value];
};

//品质
billumcheck.columnQualityDataTextFormatter = function(value,rowData,rowIndex){
	return billumcheck.qualityFt[value];
};
billumcheck.gridFooterColumDtlName=['itemNo','itemQty','checkQty','difQty','recheckQty'];
billumcheck.onLoadSuccessDtlSum = function(data){
	wms_city_common.listDataFooter({
		'columName':billumcheck.gridFooterColumDtlName,
		'documentObj': $('#checkDtlDg_view'),
		'data':data
		});
};

billumcheck.gridFooterColumName=['checkNo','itemQty','realQty'];
billumcheck.onLoadSuccessSum = function(data){
	wms_city_common.listDataFooter({
		'columName':billumcheck.gridFooterColumName,
		'documentObj': $('#dataGridJG'),
		'data':data
		});
};

//初始化信息
$(document).ready(function(){
	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
	billumcheck.initCurrentUser();
	billumcheck.initStatus();
	billumcheck.inititemType();
	billumcheck.initcheckType();
	billumcheck.initSysNo();
	billumcheck.initOwnerNo();
	billumcheck.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',{},false,billumcheck.initQuality);
	billumcheck.initUsers();
	billumcheck.initStoreNo();
	billumcheck.initconvertType();

	//billumcheck.initOwnerNo('ownerNo');
	//billumcheck.initOwnerNo('ownerNoCondition');
	billumcheck.initStoreNo('storeNoConUn');
	billumcheck.initStoreNo('storeNoFromConRp');
	wms_city_common.closeTip("openUIDetail");
	
	//初始化仓别
	billumcheck.ajaxRequest(BasePath+'/user_organization/findUserOrganization',{},false,function(u){billumcheck.allLocno=u.list;});
	//初始化当前机构其他仓别
	billumcheck.ajaxRequest(BasePath+'/store/getWarehouse',{},false,function(m){billumcheck.allOtherLocnoList=m.storeNolist;});
	billumcheck.initOtherLocno();
	billumcheck.initStoreLocno();
	billumcheck.initOtherAllLocno();
	billumcheck.initAllStoreLocno();
	
	//加载品牌
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')}//查询
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	$('#storeDg').datagrid({
		'onBeforeLoad':function(param){
			if(typeof(param.storeNoDc) != "undefined"){
				var selectRows = $('#storeDg2').datagrid('getRows');
				var dataList = '';
				for (var i = selectRows.length - 1; i >= 0; i--) {
					dataList += "'"+selectRows[i].storeNo+"',";
				}
				if(dataList!=''){
					dataList = dataList.substring(0, dataList.length-1);
				}
				param.dataList = dataList;
			}
		 }
	});
	
	
});

billumcheck.refresh = function(dataGridId){
	$("#"+dataGridId).datagrid('load');
};

billumcheck.initUsers = function(){
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

//退仓类型
billumcheck.typeFormatter  = function(value, rowData, rowIndex){
	return billumcheck.typeData[value];
};


//退仓验收类型
billumcheck.checkTypeFormatter  = function(value, rowData, rowIndex){
	return billumcheck.checkTypeData[value];
};

////品质
//billumcheck.qualityFormatter = function(value, rowData, rowIndex){
//	return billumcheck.qualityDataObj[value];
//};

//明细页导出
billumcheck.doDtlExport = function(){
	var checkNo = $("#checkNo_view").val();
	var ownerNo = $("#ownerNo_view").combobox('getValue');
	var titleName='退仓验收单【'+checkNo+'】';
	var url='/bill_um_check_dtl/do_export?locno='+billumcheck.locno+'&checkNo='+checkNo+'&ownerNo='+ownerNo;
	exportExcelBaseInfo('checkDtlDg_view',url,titleName);
};
/*
 * 次品转货
 * wanghb 2014-7-15
*/
billumcheck.tcshoddy = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	var status=billumcheck.tcvalidate(checkedRows,1);
	var checkNos=[];
	if(status){
		$.messager.confirm("确认","您确认要次品转货操作？", function (r){  
	        if(r){
	        	for(var i=0;i<checkedRows.length;i++){
	        		var item=checkedRows[i];
	        		checkNos.push(item.checkNo+";"+item.ownerNo+";"+item.untreadNo);
	    		}
	        	var param = {
	        			checkNos:checkNos.join(","),
						locno:billumcheck.locno,
						convertType:0
				};
	            var url = BasePath+'/bill_um_check/transferCargo';
	        	wms_city_common.loading("show","正在次品转货......");
	        	ajaxRequest(url,param,function(result){
	        		billumcheck.tc_callback(result);
	        	}); 
	        }
		});
	}
};

billumcheck.propertyChange = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	var propertyValue = $("#chooseitemType").combobox("getValue");// 获取所有勾选checkbox的行
	var itemTypevalues = $("#itemTypevalues").combobox("getValue");
	var status=billumcheck.tcvalidate(checkedRows,1);
	var checkNos=[];
	if(status){
		$.messager.confirm("确认","您确认要属性转换操作？", function (r){  
	        if(r){
	        	for(var i=0;i<checkedRows.length;i++){
	        		var item=checkedRows[i];
	        		checkNos.push(item.checkNo+";"+item.ownerNo+";"+item.untreadNo);
	    		}
	        	var param = {
	        			checkNos:checkNos.join(","),
						locno:billumcheck.locno,
						convertType:5,
						propertyValue:propertyValue,
						itemTypevalues:itemTypevalues
	        	
				};
	            var url = BasePath+'/bill_um_check/propertyChange';
	        	wms_city_common.loading("show","正在属性转换......");
	        	ajaxRequest(url,param,function(result){
	        		billumcheck.closeWindow('propetyChangeDialog');
	        		billumcheck.tc_callback(result);
	        	}); 
	        }
		});
	}
};

billumcheck.checkSysNo = function(checkedRows){
	var hashObject = {};
	var msg = "";
	$.each(checkedRows,function(index,item){
		var sysNo = item.sysNo;
		if (hashObject[sysNo]){
			
		} else {
			if(index == 0){
				hashObject[sysNo] = true;
			}else{
				hashObject[sysNo] = false;
			}
		}
		if(index > 0 && !hashObject[sysNo]){
			msg = item.checkNo+'只能选择相同的【'+sysNo+'】品牌库';
			return;
		}
	});
	if(msg == "" || msg== null){
		var sysNo = checkedRows[0].sysNo;
		$('#sysNoSelect').val(sysNo);
	}
	return msg;
};

/*
 * 部门转货
 * wanghb 2014-7-15
*/
billumcheck.tcdepa = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	var status=billumcheck.tcvalidate(checkedRows,2);
	if(status){
		
		//只能选择相同的品牌库
		var msg = billumcheck.checkSysNo(checkedRows);
		if(msg!=""){
			alert(msg);
			return;
		}
		
		billumcheck.clearConvertWindow(0);
		$('#selectTcdepa').window('open');
		$('#convertType1').attr("checked",true);
		$("#sdiv2").show();
		$('#sdiv3').hide();
		$('#sdiv4').hide();
		$('#tcdepaNo').combobox('setValue','');
		$('#tcAlldepaNo').combobox('setValue','');
//    	$('#selectTcdepa').window({ 
//			title:'请选择目标仓库',
//			collapsible:false,
//			minimizable:false,
//			maximizable:false,
//			closable:true,
//	        width:366,  
//	        height:425,
//	        modal:true  
//	    });	
	}
};
/*
 * 门店转货
 * wanghb 2014-7-15
*/
billumcheck.tcstore = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	var status=billumcheck.tcvalidate(checkedRows,3);
	if(status){
		$('#selectTcstore').window('open');
//    	$('#selectTcstore').window({ 
//			title:'请选择目标仓库',
//			collapsible:false,
//			minimizable:false,
//			maximizable:false,
//			closable:true,
//	        width:366,  
//	        height:425,
//	        modal:true  
//	    });	
	}
};


//选择单选框
billumcheck.clickConvertType = function(value){
	if(value == '1'){
		$('#sdiv2').show();
		$('#sdiv3').show();
		$('#sdiv4').hide();
	}else if(value == '2'){
		$('#sdiv2').hide();
		$('#sdiv3').hide();
		$('#sdiv4').show();
	}else{
		$('#sdiv2').show();
		$('#sdiv3').hide();
		$('#sdiv4').hide();
	}
};

//门店转货
billumcheck.showStoreConvert = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	var status=billumcheck.tcvalidate(checkedRows,3);
	if(status){
		//只能选择相同的品牌库
		var msg = billumcheck.checkSysNo(checkedRows);
		if(msg!=""){
			alert(msg);
			return;
		}
		billumcheck.clearConvertWindow(1);
		$('#addStoreUI').window('open');
		billumcheck.searchStoreData();
	}
};


//确定转门店
billumcheck.doStoreConvert = function(){
	var checkeRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkeRows.length < 1){
		alert("请选择退仓验收单!");
		return;
	}
	
	var storeRows = $("#storeDg2").datagrid("getRows");// 获取所有勾选checkbox的行
	if(storeRows.length < 1){
		alert("请选择需要转货的门店!");
		return;
	}
	
	//验证是否支持多对多的门店转货
	if(checkeRows.length > 1 && storeRows.length > 1){
		alert("不支持多对多的门店转货!");
		return;
	}
	
	//抽取验收单信息
	var checkList = [];
	$.each(checkeRows,function(index,item){
		checkList[checkList.length]={locno:item.locno,ownerNo:item.ownerNo,checkNo:item.checkNo};
	});
	
	//var locno = billumcheck.locno;
	//var ownerNo = checkeRows[0].ownerNo;
	//var checkNo = checkeRows[0].checkNo;
	var params = {
		//locno:locno,
		//ownerNo:ownerNo,
		//checkNo:checkNo,
		checkDatas:JSON.stringify(checkList),
		storeDatas:JSON.stringify(storeRows)
	};
	
	$.messager.confirm("确认","您确定要门店转货吗?", function (r){ 
		if(r){
			wms_city_common.loading("show","正在处理....");
			var url = BasePath+'/bill_um_check/toStoreConvert';
			ajaxRequest(url,params,function(data){
				if(data.result=="success"){
					$('#dataGridJG').datagrid('load');
					$('#searchStoreForm').form('clear');
					$('#storeDg').datagrid('loadData', { total: 0, rows: [] });
					$('#storeDg2').datagrid('loadData', { total: 0, rows: [] });
					billumcheck.closeWindow('addStoreUI'); //关闭窗口
					billumcheck.forwaradRecheckTab();
				}else{
					alert(data.msg,2);
				}
				wms_city_common.loading();
			});
		}
	});
};

//初始化门店
billumcheck.initStoreNo = function(){
	$("#storeNo").combogrid({
		delay: 350,
		panelWidth:350,   
        idField:'storeNo',  
        textField:'textFieldName',   
        pagination : true,
        rownumbers:true,
        mode: 'remote',
        pageSize:10,
        url:BasePath+'/store/list.json?storeType=11&status=0&storeNoDc='+billumcheck.locno,
        columns:[[
			{field:'storeNo',title:'客户编码',width:100},  
			{field:'storeName',title:'客户名称',width:120}  
        ]],
        loadFilter:function(data){
       	 if(data && data.rows){
       		 for(var i = 0;i<data.rows.length;i++){
       			 var tempData = data.rows[i];
       			 tempData.textFieldName = tempData.storeNo+'→'+tempData.storeName;
       		 }
       	 }
    		 return data;
        },
        onSelect:function(data,arguments){
        	$("#selectTcstore").window('close');
        	billumcheck.tcstoreSon();
    	}
	});
};

//初始化选择仓库出门店
billumcheck.initLocStoreNo = function(locno,sysNo){
	$("#storeNoLoc").combogrid({
		delay: 350,
		panelWidth:350,   
        idField:'storeNo',  
        textField:'textFieldName',   
        pagination : true,
        rownumbers:true,
        mode: 'remote',
        pageSize:10,
        url:BasePath+'/store/list.json?storeType=11&status=0&storeNoDc='+locno+"&sysNo="+sysNo,
        columns:[[
			{field:'storeNo',title:'客户编码',width:100},  
			{field:'storeName',title:'客户名称',width:120}  
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
//初始化仓别
billumcheck.otherLocno = [];
billumcheck.initOtherLocno = function(){
	var temp = {};
	for(var idx=0;idx<billumcheck.allLocno.length;idx++){
		if(billumcheck.allLocno[idx].organizationNo != billumcheck.locno){
			temp = billumcheck.allLocno[idx];
			temp.valueAndText = temp.organizationNo + '→' + temp.storeName;
			billumcheck.otherLocno[billumcheck.otherLocno.length] = temp;
		}
	}
};
//初始化当前仓别下机构的其他仓别
billumcheck.initOtherAllLocno = function(){
	var data = {};
	for(var i=0;i<billumcheck.allOtherLocnoList.length;i++){
		data = billumcheck.allOtherLocnoList[i];
		data.valueAndText = data.storeNo + '→' + data.storeName;
		//alert(data.valueAndText);
		billumcheck.allOtherLocnoStr[billumcheck.allOtherLocnoStr.length] = data;
	}
};
billumcheck.initStoreLocno = function(){
	$("#tcdepaNo").combobox({
		data:billumcheck.otherLocno,
		valueField:'organizationNo',
		textField:'valueAndText',
		panelHeight:150,
		onSelect:function(data){
			var sysNo = $('#sysNoSelect').val();
			billumcheck.initLocStoreNo(data.organizationNo,sysNo);
    	}
	});
};
billumcheck.initAllStoreLocno = function(){
	$("#tcAlldepaNo").combobox({
		data:billumcheck.allOtherLocnoStr,
		valueField:'storeNo',
		textField:'valueAndText',
		panelHeight:150,
		onSelect:function(){
    	}
	});
};
billumcheck.tcstoreSon = function(){
	$.messager.confirm("确认","您确认要门店转货操作？", function (r){
		if(r){
			var grid=$("#storeNo").combogrid("grid");//获取表格对象
			var row = grid.datagrid('getSelected');//获取行数据
			var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
			var checkNos=[];
			$.each(checkedRows,function(index,item){
				checkNos.push(item.checkNo+";"+item.ownerNo+";"+item.untreadNo);
			});
        	var param = {
        			checkNos:checkNos.join(","),
					locno:billumcheck.locno,
					storeNo:row.storeNo,
					convertType:2
			};
            var url = BasePath+'/bill_um_check/transferCargo';
        	wms_city_common.loading("show","正在门店转货......");
        	ajaxRequest(url,param,function(result){
        		billumcheck.tc_callback(result);
        		billumcheck.initStoreNo();
        	}); 
        }else{
        	billumcheck.initStoreNo();
        }
	});
};
billumcheck.tcdepaSon = function(){
	
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	var convertTypeLoc = $('input[name="convertType"]:checked').val();
	var storeNo = $("#storeNoLoc").combobox('getValue');
	var tcdepaNo = $("#tcdepaNo").combobox('getValue');
	var tcAlldepaNo = $("#tcAlldepaNo").combobox('getValue');
	var html="您确认要部门转货操作？";
	var convertType = '1';
	if(convertTypeLoc == '1'){
		//如果选择的是跨部门转店，生成的转货单也是跨部门转店类型的
		if(tcdepaNo == '' || tcdepaNo==null){
			alert("请选择目标仓库!");
			return;
		}
		if(storeNo == '' || storeNo==null){
			alert("请选择目标门店!");
			return;
		}
		convertType = '3';
	}else if(convertTypeLoc == '2'){//如果选择的是转仓
		if(tcAlldepaNo == '' || tcAlldepaNo==null){
			alert("请选择目标仓库!");
			return;
		}
		tcdepaNo=tcAlldepaNo;
		convertType = '2';
		html="您确认要转仓操作？";
	}else{//部门转货
		if(tcdepaNo == '' || tcdepaNo==null){
			alert("请选择目标仓库!");
			return;
		}
		storeNo = '';
	}
	
	$.messager.confirm("确认",html, function (r){
		if(r){
			var checkNos=[];
			$.each(checkedRows,function(index,item){
				checkNos.push(item.checkNo+";"+item.ownerNo+";"+item.untreadNo);
			});
        	var param = {
        			checkNos:checkNos.join(","),
					locno:billumcheck.locno,
					dLocno:tcdepaNo,
					storeNo:storeNo,
					convertTypeLoc:convertTypeLoc,
					convertType:convertType
			};
            var url = BasePath+'/bill_um_check/transferCargo';
        	wms_city_common.loading("show","正在转货......");
        	ajaxRequest(url,param,function(result){
        		billumcheck.tc_callback(result);
        		$('#selectTcdepa').window('close');
        		$('#tcdepaNo').combobox('setValue','');
        		$('#tcAlldepaNo').combobox('setValue','');
        		$('#storeNoLoc').combobox('setValue','');
        		
        	}); 
        }
	});
};
/*
 * 转货后回调的公共方法
 * wanghb 2014-7-15
*/
billumcheck.tc_callback = function(result){
	wms_city_common.loading();
	 if(result.result =='success'){
		 billumcheck.loadDataGrid();
		 var tranType=result.tranType;
		 if(tranType==1){
			 billumcheck.forwaradTab();//转货单
		 }else{
			 billumcheck.forwaradRecheckTab();//复核打包
		 }
		
	 }else if(result.result =='fail'){
		 var status=result.status;
		 if(status==1){
			 alert('转货失败,请选择的退仓验收单!',1);
		 }else if(status==2){
			 alert('转货失败,非验收状态下退仓验收单不能转货!',1);
		 }else if(status==3){
			 alert('转货失败,目标仓库不能为空!',1);
		 }else if(status==4){
			 alert('转货失败,仓库类型不能为空!',1);
		 }else if(status==5){
			 alert('转货失败,目的门店不能为空!',1);
		 }else if(status==400){
			 alert('转货失败,转货类型无法识别!',1);
		 }else if(status==500){
			 alert('转货失败，系统繁忙请稍后再试!',1);
		 }
	 }else{
		 alert(result.msg,2);
	 }
};
billumcheck.forwaradTab = function(){
	window.parent.addTab({
	    title: "库存转货单",
	    href: BasePath+'/bill_con_convert_goods/list',
	    iframed: true,
	    closabled: true
	});
};

billumcheck.forwaradRecheckTab = function(){
	window.parent.addTab({
	    title: "复核打包",
	    href: BasePath+'/bill_om_outstock_recheck/list',
	    iframed: true,
	    closabled: true
	});
};

/*
 * 转货验证公共方法
 * wanghb 2014-7-15
*/
billumcheck.tcvalidate = function(checkedRows,type){
	if(checkedRows.length < 1){
		alert('请选择要转货的退仓验收单!',1);
		return false;
	}
	if(type==3){
//		if(checkedRows.length > 1){
//			alert('只能选择一张退仓验收单数据!',1);
//			return false;
//		}
	}
	var allOk =0;
	var itemTypes=new Array();
	var i=0;
	$.each(checkedRows,function(index,item){
		var status = item.status;
		var convertType = item.convertType;
		if(!(status==billumcheck.status11||status==billumcheck.status13)&&type!=3){
			allOk = 1;
			return false;
		}else if(type==1){
			var quality=item.quality;//品质
			if(quality!=0){
				allOk = 2;
				return false;
			}
		}else if(type == 3){
			if(!(status==billumcheck.status11||status==billumcheck.status13||status==billumcheck.status30)||(convertType!=billumcheck.convertType2&&convertType!=''&&convertType!=null)){
				allOk = 3;
				return;
			}
		}else if(type == 4){
			if(!(status==billumcheck.status11||status==billumcheck.status13)){
				for(var j =0; j<itemTypes.length ;j++){
					if(item.itemType!=itemTypes[j]){
						allOk = 4;
					}
				}
				
				return false;
			}
			
		}
		itemTypes[i]=item.itemType;
		i++;
	});
	if(allOk==1){
		alert("非验收状态的单据不能转货!",1);
		return false;
	}else if(allOk==2){
		alert("退仓验收单包含次品不能进行次品转货!",1);
		return false;
	}else if(allOk==3){
		alert("生成直通复核单状态只能是<验收完成>、<差异验收>、<已转货>;类型是<门店转货>的单据!",1);
		return false;
	}else if(allOk==4){
		alert("非验收状态不能转货。或当前所选商品的商品类型不一致，请重新选择。!",1);
		return false;
	}
	return true;
};

billumcheck.property=function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	var status=billumcheck.tcvalidate(checkedRows,4);
		if(status){
			$('#itemTypevalues').combobox('setValue', checkedRows[0].itemType);
			var chooseitemTypes = $('#chooseitemType').combobox('getData');
			for(var i = 0 ; i<chooseitemTypes.length;i++ ){
				if(chooseitemTypes[i].itemvalue == checkedRows[0].itemType){
					chooseitemTypes.splice(i,1);
					break;
				}
			}
			$('#chooseitemType').combobox('clear');
			$('#chooseitemType').combobox('loadData',chooseitemTypes);
		//	$('#chooseitemType').combobox('setValues', chooseitemTypes);
			
			$('#propetyChangeDialog').dialog({
			title : '属性转换',
		}).dialog('open');
	}
	
};
