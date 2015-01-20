var billomdividedifferent = {};

//加载Grid数据Utils
billomdividedifferent.loadGridDataUtil = function(gridDataId, url, queryParams) {
	var tempObj = $('#' + gridDataId);
	tempObj.datagrid('options').queryParams = queryParams;
	tempObj.datagrid('options').url = url;
	tempObj.datagrid('load');
};

billomdividedifferent.endEdit = function(gid) {
	var tempObj = $('#' + gid);
	var rowArr = tempObj.datagrid('getRows');
	for ( var i = 0; i < rowArr.length; i++) {
		if (tempObj.datagrid('validateRow', i)) {
			tempObj.datagrid('endEdit', i);
		} else {
			alert('数据验证没有通过!', 1);
			return false;
		}
	}
	return true;
};

//验证条码是否为空
billomdividedifferent.checkDetailIsPass = function(listDatas) {
	var tipStr = "";
	$.each(listDatas, function(index, item) {
		if ((item.dBarcode == "N" || $.trim(item.dBarcode) == "")
			|| (item.dItemNo == "N" || $.trim(item.dItemNo) == "")) {
			tipStr = "客户：" + item.storeNo+",箱号：" + item.boxNo
					+ ",商品：" + item.sItemNo + ",条码：" + item.sBarcode
					+ "调整的商品或者条码不能为空!";
			return;
		}
	});
	return tipStr;
};

//清空
billomdividedifferent.searchClear = function(id) {
	$('#' + id).form("clear");
	$('#brandNo').combobox("loadData", []);
};

//关闭窗口
billomdividedifferent.closeWindow = function(windowId) {
	$('#' + windowId).window('close');
};

//查询主表数据
billomdividedifferent.searchData = function() {
	// 1.校验必填项
	var fromObj = $('#searchForm');
	var validateForm = fromObj.form('validate');
	if (validateForm == false) {
		return;
	}
	var fromObjStr = convertArray($('#searchForm').serializeArray());
	var queryMxURL = BasePath + '/bill_om_divide_different/list.json';
	var reqParam = eval("(" + fromObjStr + ")");
	reqParam['locno'] = billomdividedifferent.locno;
	billomdividedifferent.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};

//查看详情
billomdividedifferent.loadDetail = function(rowData) {
	$("#ownerNo_view").combobox('disable');
	$("#detailUI").window('open');
	$('#dataForm_view').form('load', rowData);
	var queryMxURL = BasePath + '/bill_om_divide_different_dtl/dtl_list.json';
	var queryParams = {
		locno : rowData.locno,
		differentNo : rowData.differentNo
	};
	billomdividedifferent.loadGridDataUtil('defferentDtlDgView', queryMxURL, queryParams);
};

//新建
billomdividedifferent.add = function() {
	$("#addUI").window('open');
	var ownerNos = $('#search_ownerNo').combobox('getData');
	$('#search_ownerNo').combobox('setValue', ownerNos[1].ownerNo);
};

// 修改
billomdividedifferent.edit = function() {
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if (checkedRows.length < 1) {
		alert('请选择要修改的记录!', 1);
		return;
	}
	if (checkedRows.length > 1) {
		alert('只能选择一条记录!', 1);
		return;
	}

	var rowData = checkedRows[0];
	if (rowData.status != '10') {
		alert(rowData.differentNo + "非建单状态不能修改!");
		return;
	}

	$("#editUI").window('open');
	$('#dataEditForm').form('load', rowData);
	$("#ownerNo_edit").combobox('disable');
	var queryMxURL = BasePath + '/bill_om_divide_different_dtl/list.json';
	var queryParams = {
		locno : rowData.locno,
		differentNo : rowData.differentNo
	};
	billomdividedifferent.loadGridDataUtil('defferentDtlDgEdit', queryMxURL, queryParams);
};


//保存差异调整单
billomdividedifferent.doSave = function() {
	
};

//修改主档信息
billomdividedifferent.doEdit = function() {

	var locno = billomdividedifferent.locno;
	//var ownerNo = $("#ownerNo_edit").combobox("getValue");
	var differentNo = $("#differentNo_edit").val();
	var remark = $("#remark_edit").val();
	var paramData = {
		locno : locno,
		//ownerNo : ownerNo,
		differentNo : differentNo,
		remark : remark
	};

	wms_city_common.loading("show", "正在保存....");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data : paramData,
		url : BasePath + '/bill_om_divide_different/editMain',
		success : function(data) {
			if (data.result == "success") {
				alert('修改成功!');
				$('#dataGridJG').datagrid('load');
				billomdividedifferent.closeWindow('editUI'); // 关闭窗口
			}else {
				alert(data.msg, 2);
			}
			wms_city_common.loading();
		}
	});
};

//删除差异调整单
billomdividedifferent.doDel = function() {
	var selectRows = $('#dataGridJG').datagrid('getChecked');
	if (selectRows.length < 1) {
		alert("请至少选择一条数据!");
		return;
	}

	var dataList = [];
	var tipStr = "";
	$.each(selectRows, function(index, item) {
		if (item.status != "10") {
			tipStr = item.differentNo;
			return false;
		}
		dataList[dataList.length] = {
			locno : item.locno,
			ownerNo : item.ownerNo,
			differentNo : item.differentNo,
			differentType : item.differentType,
			sourceNo : item.sourceNo
		};
	});
	
	if (tipStr != "" && tipStr != null) {
		alert(tipStr + "非建单状态不能删除!");
		return;
	}

	$.messager.confirm("确认", "你确定要删除这" + selectRows.length + "条数据",
			function(r) {
				if (r) {
					wms_city_common.loading('show', '删除中,请稍候...');
					var url = BasePath + '/bill_om_divide_different/delDivideDifferent';
					var effectRow = {
						datas : JSON.stringify(dataList)
					};
					$.post(url, effectRow, function(data) {
						if (data.result == "success") {
							wms_city_common.loading();
							alert('删除成功!');
							$('#dataGridJG').datagrid('load');
						} else if (data.result == 'fail') {
							wms_city_common.loading();
							alert(data.msg, 1);
							return;
						} else {
							wms_city_common.loading();
							alert('操作异常！', 1);
							return;
						}
					}, "JSON").error(function() {
						wms_city_common.loading();
						alert('删除失败!', 1);
					});
				}
	});
};

//删除明细
billomdividedifferent.delDifferentDtl = function() {
	var obj = $('#defferentDtlDgEdit');
	var checkRow = obj.datagrid('getChecked');
	if (checkRow == null || checkRow.length < 1) {
		alert("请选择要删除的记录", 1);
		return;
	}
//	var tipStr = false;
//	$.each(checkRow, function(index, item) {
//		if (item.addFlag != "1" && item.itemQty != 0) {
//			alert("店退仓单：" + item.untreadNo + ",门店：" + item.storeName + ",商品："
//					+ item.itemNo + ",尺码：" + item.sizeNo + ",不是新增商品不能删除", 1);
//			tipStr = true;
//			return false;
//		}
//	});
//	if (tipStr) {
//		return;
//	}
	$.each(checkRow, function(index, item) {
		var index = obj.datagrid('getRowIndex', item);
		$('#defferentDtlDgEdit').datagrid('deleteRow', index);
	});
};

//拆分明细
billomdividedifferent.splitDifferentDtl = function() {
	
	var obj = $('#defferentDtlDgEdit');
	var checkRow = obj.datagrid('getChecked');
	if (checkRow == null || checkRow.length < 1) {
		alert("请选择要拆分的记录", 1);
		return;
	}
	
	if(checkRow.length > 1){
		alert("只能选择一条数据拆分", 1);
		return;
	}
	
	var tempFlag = ""; 
	$.each(checkRow, function(index, item) {
		var i = obj.datagrid('getRowIndex', item);
		if (obj.datagrid('validateRow', i)) {
			obj.datagrid('endEdit', i);
		} else {
			tempFlag = '数据验证没有通过!';
			return false;
		}
	});
	
	var rowData = checkRow[0];
	if (tempFlag!="") {
		alert(tempFlag);
		return;
	}
	
	// 验证数据列
	var addTipStr = billomdividedifferent.checkDetailIsPass(checkRow);
	if (addTipStr != "") {
		alert(addTipStr);
		return;
	}
	
	//验证是否是拆分单据
	if(rowData.pixFlag == '1'){
		alert("已拆分的明细不能拆分!");
		return false;
	}
	
	var params = 
	{
		locno:rowData.locno,
		ownerNo:rowData.ownerNo,
		differentNo:rowData.differentNo,
		serialNo:rowData.serialNo,
		storeNo:rowData.storeNo,
		sItemNo:rowData.sItemNo,
		packQty:rowData.packQty,
		itemQty:rowData.itemQty,
		realQty:rowData.realQty,
		sCellNo:rowData.sCellNo,
		sCellId:rowData.sCellId,
		dCellNo:rowData.dCellNo,
		dCellId:rowData.dCellId,
		status:rowData.status,
		boxNo:rowData.boxNo,
		dItemNo:rowData.dItemNo,
		brandNo:rowData.brandNo,
		sBarcode:rowData.sBarcode,
		dBarcode:rowData.dBarcode,
		expNo:rowData.expNo,
		pixFlag:rowData.pixFlag
	};
	
	$.messager.confirm("确认", "你确定要拆分选中的数据",function(r) {
		if(r){
			wms_city_common.loading("show", "正在拆分....");
			$.ajax({
				async : true,
				cache : true,
				type : 'POST',
				dataType : "json",
				data : params,
				url : BasePath + '/bill_om_divide_different_dtl/splitDifferentDtl',
				success : function(data) {
					if (data.result == "success") {
						alert('拆分成功!');
						$('#defferentDtlDgEdit').datagrid('load');
					}else {
						alert(data.msg, 2);
					}
					wms_city_common.loading();
				}
			});
		}
	});
};


//保存明细
billomdividedifferent.saveDifferentDtl = function() {
	var tempFlag = billomdividedifferent.endEdit("defferentDtlDgEdit");
	if (!tempFlag) {
		return;
	}
	var tempObj = $('#defferentDtlDgEdit');
	//var inserted = tempObj.datagrid('getChanges', "inserted");
	var updated = tempObj.datagrid('getChanges', "updated");
	var deleted = tempObj.datagrid('getChanges', "deleted");

	// 验证新增的列
	var addTipStr = billomdividedifferent.checkDetailIsPass(updated);
	if (addTipStr != "") {
		alert(addTipStr);
		return;
	}
	
	//验证是否操作明细数据
	if(updated.length < 1&&deleted.length < 1){
		alert("请进行数据操作后再保存明细!");
		return false;
	}

	wms_city_common.loading("show", "正在保存明细......");
	$.ajax({
		async : true,
		cache : true,
		type : 'POST',
		url : BasePath + '/bill_om_divide_different_dtl/saveDifferentDtl',
		data : {
			locno : billomdividedifferent.locno,
			//ownerNo : $("#ownerNo_edit").combobox("getValue"),
			differentNo : $("#differentNo_edit").val(),
			//inserted : JSON.stringify(inserted),
			updated : JSON.stringify(updated),
			deleted : JSON.stringify(deleted)
		},
		success : function(data) {
			if (data.result == 'success') {
				alert("保存成功");
				$("#defferentDtlDgEdit").datagrid('load');
				$("#defferentDtlDgEdit").datagrid("acceptChanges");
				$('#dataGridJG').datagrid('load');
				billomdividedifferent.closeWindow('editUI');
			} else {
				alert(data.msg, 2);
			}
			wms_city_common.loading();
		}
	});
};

//审核退仓验收任务单
billomdividedifferent.auditDivideDifferent = function() {
	var selectRows = $('#dataGridJG').datagrid('getChecked');
	if (selectRows.length < 1) {
		alert("请至少选择一条数据!");
		return;
	}

	var dataList = [];
	var tipStr = "";
	$.each(selectRows, function(index, item) {
		if (item.status != "10") {
			tipStr = item.differentNo;
			return false;
		}
		dataList[dataList.length] = {
			locno : item.locno,
			ownerNo : item.ownerNo,
			differentNo : item.differentNo,
			differentType : item.differentType,
			sourceNo : item.sourceNo
		};
	});
	
	if (tipStr != "" && tipStr != null) {
		alert(tipStr + "非建单状态不能审核!");
		return;
	}

	$.messager.confirm("确认", "你确定要审核这" + selectRows.length + "条数据",
			function(r) {
				if (r) {
					wms_city_common.loading('show', '审核中,请稍候...');
					var url = BasePath + '/bill_om_divide_different/auditDivideDifferent';
					var effectRow = {
						datas : JSON.stringify(dataList)
					};
					$.post(url, effectRow, function(data) {
						if (data.result == "success") {
							wms_city_common.loading();
							alert('审核成功!');
							$('#dataGridJG').datagrid('load');
						} else if (data.result == 'fail') {
							wms_city_common.loading();
							alert(data.msg, 1);
							return;
						} else {
							wms_city_common.loading();
							alert('操作异常！', 1);
							return;
						}
					}, "JSON").error(function() {
						wms_city_common.loading();
						alert('审核失败!', 1);
					});
				}
	});
};


billomdividedifferent.onClickRowDtl = function(rowIndex, rowData){
	var curObj = $("#defferentDtlDgEdit");
	curObj.datagrid('beginEdit', rowIndex);
	var edItem = curObj.datagrid('getEditor', {index:rowIndex,field:'dItemNo'});
	var edBarcode = curObj.datagrid('getEditor', {index:rowIndex,field:'dBarcode'});
	var pixFlag = rowData.pixFlag;
	if(pixFlag == "1"){
		$(edItem.target).attr('disabled',true);
		$(edBarcode.target).attr('disabled',true);
	}
};

// 委托业主
billomdividedifferent.ownerFormatter = function(value, rowData, rowIndex) {
	return billomdividedifferent.ownnerData[value];
};

// 用户
billomdividedifferent.userFormatter = function(value, rowData, rowIndex) {
	return billomdividedifferent.usersData[value];
};

// 初始化当前登录的用户 信息
billomdividedifferent.initCurrentUser = function() {
	ajaxRequestAsync(BasePath + '/initCache/getCurrentUser', {},
			function(data) {
				billomdividedifferent.loginName = data.loginName;
				billomdividedifferent.locno = data.locno;
			});
};

// 初始化状态
billomdividedifferent.initStatus = function(data) {
	wms_city_common.comboboxLoadFilter(
			[ "statusCondition" ],
			null,
			null,
			null,
			true,
			[ true ],
			BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=CITY_OM_DIVIDE_DIFFERENT_STATUS',
			{}, 
			null, 
			null
	);
};

//初始化委托业主
billomdividedifferent.ownnerData = {};
billomdividedifferent.initOwnerNo = function() {
	wms_city_common.comboboxLoadFilter(
			[ "ownerNo_view", "ownerNo_edit" ],
			"ownerNo", 
			"ownerName",
			"ownerName", 
			false, 
			[ false, false], 
			BasePath + '/entrust_owner/get_biz', 
			{}, 
			billomdividedifferent.ownnerData,
			null
	);
};

//初始化用户
billomdividedifferent.usersData = {};
billomdividedifferent.initUsers = function() {
	wms_city_common.comboboxLoadFilter(
			[],
			'workerNo', 
			'workerName', 
			'unionName',
			false, 
			[ true, true ],
			BasePath + '/authority_user/user.json',
			{},
			billomdividedifferent.usersData, null);
};

//初始化来源类型
billomdividedifferent.initDifferentType = function() {
	wms_city_common.comboboxLoadFilter(
			[ "differentTypeCondition" ],
			null,
			null,
			null,
			true,
			[ true ],
			BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=CITY_OM_DIVIDE_DIFFERENT_TYPE',
			{}, 
			null, 
			null
	);
};

//初始化信息
$(document).ready(function() {
	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
	billomdividedifferent.initCurrentUser();
	billomdividedifferent.initStatus();
	billomdividedifferent.initOwnerNo();
	billomdividedifferent.initUsers();
	billomdividedifferent.initDifferentType();

	// 加载品牌
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('#brandNo')
	});
	// 查询
	wms_city_common.loadSysNo4Cascade(objs);

	//主单数据汇总
	$('#defferentDtlDgView').datagrid({
		'onLoadSuccess':function(data){
			if(data.footer[1].isselectsum){
				billomdividedifferent.itemQty = data.footer[1].itemQty;
				billomdividedifferent.realQty = data.footer[1].realQty;
   			}else{
   				var rows = $('#defferentDtlDgView').datagrid('getFooterRows');
	   			rows[1]['itemQty'] = billomdividedifferent.itemQty;
	   			rows[1]['realQty'] = billomdividedifferent.realQty;
	   			$('#defferentDtlDgView').datagrid('reloadFooter');
   			}
		}
	});
	
});