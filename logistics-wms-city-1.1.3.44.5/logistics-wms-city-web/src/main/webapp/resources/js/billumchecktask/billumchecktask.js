var billumchecktask = {};

// 加载Grid数据Utils
billumchecktask.loadGridDataUtil = function(gridDataId, url, queryParams) {
	var tempObj = $('#' + gridDataId);
	tempObj.datagrid('options').queryParams = queryParams;
	tempObj.datagrid('options').url = url;
	tempObj.datagrid('load');
};

// 清空
billumchecktask.searchClear = function(id) {
	$('#' + id).form("clear");
	$('#brandNo').combobox("loadData", []);
};

// 清空
billumchecktask.searchUntreadClear = function(id) {
	$('#' + id).form("clear");
	$('#search_brandNo').combobox("loadData", []);
};

// 关闭窗口
billumchecktask.closeWindow = function(windowId) {
	
	$('#' + windowId).window('close');
	
	
};

// 插入行，开始编辑
billumchecktask.insertRowAtEnd = function(gid, rowData) {
	var tempObj = $('#' + gid);
	if (rowData) {
		tempObj.datagrid('appendRow', rowData);
	} else {
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
	return tempIndex;
};

// 验证明细数据是否验证通过
billumchecktask.endEdit = function(gid) {
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

// 验证验收数量是否大于0
billumchecktask.checkDetailIsPass = function(listDatas) {
	var tipStr = "";
	$.each(listDatas, function(index, item) {
		if (item.checkQty == "0" && item.itemQty == "0") {
			tipStr = "店退仓单：" + item.untreadNo + ",客户名称：" + item.storeName
					+ ",商品：" + item.itemNo + ",尺码：" + item.sizeNo
					+ "串款验收数量必须大于0";
			return;
		}
	});
	return tipStr;
};

// 查询主表数据
billumchecktask.searchData = function() {
	// 1.校验必填项
	var fromObj = $('#searchForm');
	var validateForm = fromObj.form('validate');
	if (validateForm == false) {
		return;
	}
	var fromObjStr = convertArray($('#searchForm').serializeArray());
	var queryMxURL = BasePath + '/bill_um_check_task/list.json';
	var reqParam = eval("(" + fromObjStr + ")");
	reqParam['locno'] = billumchecktask.locno;
	billumchecktask.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};

// 查询店退仓数据
billumchecktask.searchUntreadData = function() {
	// 1.校验必填项
	var fromObj = $('#searchUntreadForm');
	var validateForm = fromObj.form('validate');
	if (validateForm == false) {
		return;
	}
	// 货主不能为空
	var ownerNo = $('#search_ownerNo').combobox('getValue');
	if (ownerNo == "" || ownerNo == null) {
		alert("请选择货主");
		return;
	}

	// var untreadDg2 = $('#untreadDg2').datagrid('getRows');
	var fromObjStr = convertArray($('#searchUntreadForm').serializeArray());
	var queryMxURL = BasePath + '/bill_um_untread/findUntread2CheckTask.json';
	var reqParam = eval("(" + fromObjStr + ")");
	reqParam['locno'] = billumchecktask.locno;
	reqParam['status'] = "11";
	// reqParam['datas'] = JSON.stringify(untreadDg2);
	billumchecktask.loadGridDataUtil('untreadDg', queryMxURL, reqParam);
};

// 查看详情
billumchecktask.loadDetail = function(rowData) {
	$("#ownerNo_view").combobox('disable');
	$("#detailUI").window('open');
	$('#dataForm_view').form('load', rowData);
	var queryMxURL = BasePath + '/bill_um_check_task_dtl/dtl_list.json';
	var queryParams = {
		locno : rowData.locno,
		ownerNo : rowData.ownerNo,
		checkTaskNo : rowData.checkTaskNo
	};
	billumchecktask.loadGridDataUtil('checkDtlDgView', queryMxURL, queryParams);
};

// 新建
billumchecktask.add = function() {
	$("#addUI").window('open');
	var ownerNos = $('#search_ownerNo').combobox('getData');
	$('#search_ownerNo').combobox('setValue', ownerNos[1].ownerNo);
	
	//清楚新增左右两边的数据
	var rows = $('#untreadDg').datagrid('getRows');
	for ( var i = rows.length - 1; i >= 0; i--) {
		var index = $('#untreadDg').datagrid('getRowIndex', rows[i]);
		$('#untreadDg').datagrid('deleteRow', index);
	}
	
	var rows = $('#untreadDg2').datagrid('getRows');
	for ( var i = rows.length - 1; i >= 0; i--) {
		var index = $('#untreadDg2').datagrid('getRowIndex', rows[i]);
		$('#untreadDg2').datagrid('deleteRow', index);
	}
	

};

// 修改
billumchecktask.edit = function() {

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
		alert(rowData.checkTaskNo + "非建单状态不能修改!");
		return;
	}

	$("#editUI").window('open');
	$('#dataEditForm').form('load', rowData);
	$("#ownerNo_edit").combobox('disable');
	var queryMxURL = BasePath + '/bill_um_check_task_dtl/list.json';
	var queryParams = {
		locno : rowData.locno,
		ownerNo : rowData.ownerNo,
		checkTaskNo : rowData.checkTaskNo
	};
	billumchecktask.loadGridDataUtil('checkDtlDgEdit', queryMxURL, queryParams);
};

// 数据右移
billumchecktask.toRgiht = function() {
	var selectRows = $('#untreadDg').datagrid('getChecked');
	if (selectRows.length < 1) {
		alert("请选择店退仓单!");
		return;
	}
	for ( var i = selectRows.length - 1; i >= 0; i--) {
		var datas = {
			ownerNo : selectRows[i].ownerNo,
			untreadNo : selectRows[i].untreadNo,
			poNo : selectRows[i].poNo,
			storeNo : selectRows[i].storeNo,
			storeName : selectRows[i].storeName,
			itemQty : selectRows[i].itemQty,
			remark : selectRows[i].remark
		};
		$('#untreadDg2').datagrid('appendRow', datas);
		var index = $('#untreadDg').datagrid('getRowIndex', selectRows[i]);
		$('#untreadDg').datagrid('deleteRow', index);
		$('#untreadDg').datagrid('clearChecked');
	}
	billumchecktask.calcSelectInfo($('#untreadDg2').datagrid('getRows'));
};

// 数据左移
billumchecktask.toLeft = function() {
	var selectRows = $('#untreadDg2').datagrid('getChecked');
	if (selectRows.length < 1) {
		alert("请选择店退仓单!");
		return;
	}
	for ( var i = selectRows.length - 1; i >= 0; i--) {
		var datas = {
			ownerNo : selectRows[i].ownerNo,
			untreadNo : selectRows[i].untreadNo,
			poNo : selectRows[i].poNo,
			storeNo : selectRows[i].storeNo,
			storeName : selectRows[i].storeName,
			itemQty : selectRows[i].itemQty,
			remark : selectRows[i].remark
		};
		$('#untreadDg').datagrid('appendRow', datas);
		var index = $('#untreadDg2').datagrid('getRowIndex', selectRows[i]);
		$('#untreadDg2').datagrid('deleteRow', index);
		$('#untreadDg2').datagrid('clearChecked');
		
	}
	billumchecktask.calcSelectInfo($('#untreadDg2').datagrid('getRows'));
};

// 计算选择的信息
billumchecktask.calcSelectInfo = function(lists) {
	var uq = {};
	var rq = [];
	var totalItemQty = 0;
	var prefix = '';
	for ( var i = 0; i < lists.length; i++) {
		if (typeof lists[i].storeNo == 'string') {
			prefix = '_str';
		} else {
			prefix = '';
		}
		if (!uq[lists[i].storeNo + prefix]) {
			uq[lists[i].storeNo + prefix] = true;
			rq.push(lists[i].storeNo);
		}
		totalItemQty += lists[i].itemQty;
	}
	var storeNoNum = rq.length;
	$('#untreadDg2').datagrid('reloadFooter', [ {
		untreadNo : '合计',
		storeNo : storeNoNum,
		itemQty : totalItemQty
	} ]);
};

// 保存退仓验收任务单
billumchecktask.doSave = function() {
	var selectRows = $('#untreadDg2').datagrid('getRows');
	if (selectRows.length < 1) {
		alert("请选择要新建的店退仓单!");
		return;
	}

	wms_city_common.loading('show', '保存中,请稍候...');
	var url = BasePath + '/bill_um_check_task/saveUmCheckTask';
	var effectRow = {
		datas : JSON.stringify(selectRows)
	};
	$.post(url, effectRow, function(data) {
		if (data.result == "success") {
			wms_city_common.loading();
			alert('新建成功!');
			$('#untreadDg').datagrid('loadData', {
				total : 0,
				rows : []
			});
			$('#untreadDg2').datagrid('loadData', {
				total : 0,
				rows : []
			});
			$('#untreadDg2').datagrid('reloadFooter', [ {} ]);
			billumchecktask.closeWindow('addUI'); // 关闭窗口
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
		alert('保存失败!', 1);
	});
};

// 修改主档信息
billumchecktask.doEdit = function() {

	var locno = billumchecktask.locno;
	var ownerNo = $("#ownerNo_edit").combobox("getValue");
	var checkTaskNo = $("#checkTaskNo_edit").val();
	var remark = $("#remark_edit").val();
	var paramData = {
		locno : locno,
		ownerNo : ownerNo,
		checkTaskNo : checkTaskNo,
		remark : remark
	};

	wms_city_common.loading("show", "正在保存....");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data : paramData,
		url : BasePath + '/bill_um_check_task/updateUmCheckTask',
		success : function(data) {
			if (data.result == "success") {
				alert('修改成功!');
				$('#dataGridJG').datagrid('load');
				billumchecktask.closeWindow('editUI'); // 关闭窗口
			} else if(data.result == "notExits"){
				alert(data.msg, 2);
			}
			else {
				alert('修改失败,请联系管理员!', 2);
			}
			wms_city_common.loading();
		}
	});
};

// 按计划保存
billumchecktask.savePlanCheckDtl = function() {

	// 是否存在新增差异商品，未保存到数据库
	var inserted = $('#checkDtlDgEdit').datagrid('getChanges', "inserted");
	if (inserted.length > 0) {
		alert("存在差异商品的数据,请先保存明细!");
		return;
	}

	var locno = billumchecktask.locno;
	var ownerNo = $("#ownerNo_edit").combobox("getValue");
	var checkTaskNo = $("#checkTaskNo_edit").val();
	var paramData = {
		locno : locno,
		ownerNo : ownerNo,
		checkTaskNo : checkTaskNo
	};

	$.messager
			.confirm(
					"确认",
					"按计划保存将覆盖原有的验收数量,您确定要执行?",
					function(r) {
						if (r) {
							wms_city_common.loading("show", "正在保存....");
							$
									.ajax({
										async : false,
										cache : false,
										type : 'POST',
										dataType : "json",
										data : paramData,
										url : BasePath
												+ '/bill_um_check_task_dtl/saveCheckQty4itemQty',
										success : function(data) {
											if (data.result == "success") {
												alert('保存成功!');
												$('#dataGridJG').datagrid(
														'load');
												billumchecktask
														.closeWindow('editUI'); // 关闭窗口
											}else if(data.result == "notExits"){
												alert(data.msg, 2);
											}
											else {
												alert('保存失败,请联系管理员!', 2);
											}
											wms_city_common.loading();
										}
									});
						}
					});
};

// 弹出按单删除界面
billumchecktask.delDtlByUntreadNo = function() {

	// 是否存在新增差异商品，未保存到数据库
	var inserted = $('#checkDtlDgEdit').datagrid('getChanges', "inserted");
	if (inserted.length > 0) {
		alert("存在差异商品的数据,请先保存明细!");
		return;
	}

	$("#untreadUI").window('open');
	billumchecktask.searchClear('unSearchForm');
	billumchecktask.searchUntreadDel();
};

// 按单删除
billumchecktask.delUntreadByCheckTask = function() {
	var obj = $('#dataGridUn');
	var checkRow = obj.datagrid('getChecked');
	if (checkRow == null || checkRow.length < 1) {
		alert("请选择要删除的记录", 1);
		return;
	}
	$.messager.confirm("确认", "你确定要删除选中的" + checkRow.length + "条店退仓数据?",
			function(r) {
				if (r) {
					wms_city_common.loading('show', '删除中,请稍候...');
					var url = BasePath
							+ '/bill_um_check_task_dtl/delUntreadByCheckTask';
					var effectRow = {
						datas : JSON.stringify(checkRow)
					};
					$.post(url, effectRow, function(data) {
						if (data.result == "success") {
							wms_city_common.loading();
							alert('删除成功!');
							obj.datagrid('load');
							$('#checkDtlDgEdit').datagrid('load');
							billumchecktask.delMainByDtlOver();
						} else if(data.flag == "notExits"){
							wms_city_common.loading();
							alert(data.msgs, 1);
							return;
						}
						else if (data.result == 'fail') {
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

// 判断全部明细是否删除,如果全部删除则把主档数据也删除
billumchecktask.delMainByDtlOver = function() {
	var url = BasePath + '/bill_um_check_task_dtl/get_biz';
	var locno = billumchecktask.locno;
	var ownerNo = $("#ownerNo_edit").combobox("getValue");
	var checkTaskNo = $("#checkTaskNo_edit").val();
	var paramData = {
		locno : locno,
		ownerNo : ownerNo,
		checkTaskNo : checkTaskNo
	};
	ajaxRequestAsync(url, paramData, function(data) {
		if (data.length < 1) {
			var delUrl = BasePath + '/bill_um_check_task/delete';
			ajaxRequest(delUrl, paramData, function(result, returnMsg) {
				if (returnMsg == 'success') {
					$("#untreadUI").window('close');
					$("#editUI").window('close');
					$('#dataGridJG').datagrid('load');
				} else {
					alert('清除主档数据失败!', 2);
				}
			});
		}
	});
};

// 弹出按单删除界面
billumchecktask.searchUntreadDel = function() {
	var locno = billumchecktask.locno;
	var ownerNo = $("#ownerNo_edit").combobox("getValue");
	var checkTaskNo = $("#checkTaskNo_edit").val();
	var fromObjStr = convertArray($('#unSearchForm').serializeArray());
	var queryMxURL = BasePath
			+ '/bill_um_check_task_dtl/findUntreadNo4CheckTaskDtl.json';
	var reqParam = eval("(" + fromObjStr + ")");
	reqParam['locno'] = locno;
	reqParam['ownerNo'] = ownerNo;
	reqParam['checkTaskNo'] = checkTaskNo;
	billumchecktask.loadGridDataUtil('dataGridUn', queryMxURL, reqParam);
};

// 删除退仓验收任务单
billumchecktask.doDel = function() {
	var selectRows = $('#dataGridJG').datagrid('getChecked');
	if (selectRows.length < 1) {
		alert("请至少选择一条数据!");
		return;
	}

	var dataList = [];
	var tipStr = "";
	$.each(selectRows, function(index, item) {
		if (item.status != "10") {
			tipStr = item.checkTaskNo;
			return false;
		}
		dataList[dataList.length] = {
			locno : item.locno,
			ownerNo : item.ownerNo,
			checkTaskNo : item.checkTaskNo
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
					var url = BasePath + '/bill_um_check_task/delUmCheckTask';
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

// 删除明细
billumchecktask.delCheckDtl = function() {
	var obj = $('#checkDtlDgEdit');
	var checkRow = obj.datagrid('getChecked');
	if (checkRow == null || checkRow.length < 1) {
		alert("请选择要删除的记录", 1);
		return;
	}
	var tipStr = false;
	$.each(checkRow, function(index, item) {
		if (item.addFlag != "1" && item.itemQty != 0) {
			alert("店退仓单：" + item.untreadNo + ",门店：" + item.storeName + ",商品："
					+ item.itemNo + ",尺码：" + item.sizeNo + ",不是新增商品不能删除", 1);
			tipStr = true;
			return false;
		}
	});
	if (tipStr) {
		return;
	}
	$.each(checkRow, function(index, item) {
		var index = obj.datagrid('getRowIndex', item);
		$('#checkDtlDgEdit').datagrid('deleteRow', index);
	});

};

// 商品置零
billumchecktask.updateCheckQtyToZero = function() {
	var obj = $('#checkDtlDgEdit');
	// 是否存在新增差异商品，未保存到数据库
	var inserted = obj.datagrid('getChanges', "inserted");
	if (inserted.length > 0) {
		alert("存在差异商品的数据,请先保存明细!");
		return;
	}

	var checkRow = obj.datagrid('getChecked');
	if (checkRow == null || checkRow.length < 1) {
		alert("请选择要置零的记录", 1);
		return;
	}

	var tipStr = false;
	$.each(checkRow, function(index, item) {
		if (item.addFlag == "1" && item.itemQty == 0) {
			alert("店退仓单：" + item.untreadNo + ",门店：" + item.storeName + ",商品："
					+ item.itemNo + ",尺码：" + item.sizeNo + ",新增商品不能置零", 1);
			tipStr = true;
			return false;
		}
	});
	if (tipStr) {
		return;
	}

	// var tipStr = false;
	// $.each(checkRow,function(index,item){
	// if(item.addFlag!="1"&&item.itemQty!=0){
	// alert("店退仓单："+item.untreadNo+",门店："+item.storeName+",商品："+item.itemNo+",尺码："+item.sizeNo+",不是新增商品不能选择",1);
	// tipStr = true;
	// return false;
	// }
	// });
	// if(tipStr){
	// return;
	// }
	$.messager.confirm("确认", "你确定要对这" + checkRow.length + "条数据置零吗?",
			function(r) {
				if (r) {
					wms_city_common.loading('show', '操作中,请稍候...');
					var url = BasePath
							+ '/bill_um_check_task_dtl/updateCheckQtyToZero';
					var effectRow = {
						datas : JSON.stringify(checkRow)
					};
					$.post(url, effectRow, function(data) {
						if (data.result == "success") {
							wms_city_common.loading();
							alert('操作成功!');
							obj.datagrid('load');
						}else if (data.result == 'notExits') {
							wms_city_common.loading();
							alert(data.msg, 2);
							return;
						} 
						else if (data.result == 'fail') {
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
						alert('操作失败!', 1);
					});
				}
			});

};

// 保存明细
billumchecktask.saveCheckDtl = function() {
	var tempFlag = billumchecktask.endEdit("checkDtlDgEdit");
	if (!tempFlag) {
		return;
	}
	var tempObj = $('#checkDtlDgEdit');
	var inserted = tempObj.datagrid('getChanges', "inserted");
	var updated = tempObj.datagrid('getChanges', "updated");
	var deleted = tempObj.datagrid('getChanges', "deleted");

	// 验证新增的列
	var addTipStr = billumchecktask.checkDetailIsPass(inserted);
	if (addTipStr != "") {
		alert(addTipStr);
		return;
	}

	// 验证修改过的列
	// var updateTipStr = billumchecktask.checkDetailIsPass(updated);
	// if(updateTipStr != ""){
	// alert(updateTipStr);
	// return;
	// }

	wms_city_common.loading("show", "正在保存明细......");
	$.ajax({
		async : true,
		cache : true,
		type : 'POST',
		url : BasePath + '/bill_um_check_task_dtl/saveUmCheckTaskDtl',
		data : {
			locno : billumchecktask.locno,
			ownerNo : $("#ownerNo_edit").combobox("getValue"),
			checkTaskNo : $("#checkTaskNo_edit").val(),
			inserted : JSON.stringify(inserted),
			updated : JSON.stringify(updated),
			deleted : JSON.stringify(deleted)
		},
		success : function(data) {
			if (data.result == 'success') {
				alert("保存成功");
				// $("#editUI").window('close');
				$("#checkDtlDgEdit").datagrid('load');
				$('#dataGridJG').datagrid('load');
				$("#checkDtlDgEdit").datagrid("acceptChanges");
			} else {
				alert(data.msg, 2);
			}
			wms_city_common.loading();
		}
	});
};

// 打开添加差异商品界面
billumchecktask.addDiffItem = function() {
	var selectRows = $("#checkDtlDgEdit").datagrid("getChecked");
	if (selectRows == null || selectRows.length < 1) {
		alert("请选择要添加差异商品的行", 1);
		return;
	}
	if (selectRows.length > 1) {
		alert("只能选择勾选一行添加差异商品", 1);
		return;
	}
	$("#itemUI").window('open');
	$('#dataGridItem').datagrid('loadData', {
		total : 0,
		rows : []
	});

	$("#itemTypeHid").val(selectRows[0].itemType);
	$("#qualityHid").val(selectRows[0].quality);

	// 查询退仓验收任务下的所有店退仓单
	var dataList = {};
	$.ajax({
		async : true,
		cache : false,
		type : 'POST',
		dataType : "json",
		data : {
			locno : billumchecktask.locno,
			ownerNo : $("#ownerNo_edit").combobox("getValue"),
			checkTaskNo : $("#checkTaskNo_edit").val()
		},
		url : BasePath
				+ '/bill_um_check_task_dtl/findUntreadNo4CheckTaskDtl.json',
		success : function(data) {
			if(data.flag =="notExits"){
				wms_city_common.loading();
				alert(data.msg, 2);
				return;
			}
			dataList = data.rows;
			$('#untreadNoList').combobox({
				valueField : "untreadNo",
				textField : "untreadNo",
				data : dataList,
				panelHeight : 150,
				onChange : function(newValue, oldValue) {
					$('#dataGridItem').datagrid('loadData', {
						total : 0,
						rows : []
					});
					for ( var i = 0; i < dataList.length; i++) {
						if (dataList[i].untreadNo == newValue) {
							$('#lbStoreName').html(dataList[i].storeName);
							$('#storeNoHid').val(dataList[i].storeNo);
							$('#storeNameHid').val(dataList[i].storeName);
						}
					}
				}
			});
			$('#untreadNoList').combobox("select", selectRows[0].untreadNo);
		}
	});
};

// 查询商品数据
billumchecktask.searchFilterItem = function() {
	var locno = billumchecktask.locno;
	var ownerNo = $("#ownerNo_edit").combobox("getValue");
	var checkTaskNo = $("#checkTaskNo_edit").val();
	var untreadNo = $("#untreadNoList").combobox("getValue");
	var fromObjStr = convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL = BasePath
			+ '/bill_um_check_task_dtl/findItem4CheckTask.json';
	var reqParam = eval("(" + fromObjStr + ")");
	reqParam["locno"] = locno;
	reqParam["ownerNo"] = ownerNo;
	reqParam["checkTaskNo"] = checkTaskNo;
	reqParam["untreadNo"] = untreadNo;
	billumchecktask.loadGridDataUtil('dataGridItem', queryMxURL, reqParam);
};

// 选择串码商品
billumchecktask.selectItem = function() {
	var checkRow = $('#dataGridItem').datagrid('getChecked');
	if (checkRow.length < 1) {
		alert("请选择商品!");
		return;
	}

	$.messager.confirm("确认", "你确定要添加这" + checkRow.length + "条数据?", function(r) {
		if (r) {
			wms_city_common.loading('show');
			var storeNo = $('#storeNoHid').val();
			var storeName = $('#storeNameHid').val();
			var itemType = $("#itemTypeHid").val();
			var quality = $("#qualityHid").val();

			var untreadNo = $("#untreadNoList").combobox("getValue");
			$.each(checkRow, function(index, item) {
				var reqParams = {
					itemNo : item.itemNo,
					itemName : item.itemName,
					colorName : item.colorName,
					sizeNo : item.sizeNo,
					itemQty : 0,
					checkQty : 0,
					addFlag : "1",
					boxNo : 'N',
					brandNo : item.brandNo,
					brandName : item.brandName,
					storeNo : storeNo,
					storeName : storeName,
					untreadNo : untreadNo,
					itemType : itemType,
					quality : quality
				};

				// 插入商品到父窗口,开始编辑
				billumchecktask.insertRowAtEnd('checkDtlDgEdit', reqParams);
				// 关闭load提示
				wms_city_common.loading();
			});

			// 关闭商品选择窗口
			billumchecktask.closeWindow('itemUI');
			$('#checkDtlDgEdit').datagrid('beginEdit');
		}
	});

};

// 审核退仓验收任务单
billumchecktask.auditUmCheckTask = function() {
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if (checkedRows.length < 1) {
		alert('请选择要审核的记录!', 1);
		return;
	}
	var allOk = true;
	var dtlOk = true;
	$.each(checkedRows, function(index, item) {
		var status = item.status;
		if (status != '10') {
			allOk = false;
			return false;
		}
		if (item.dtlCount == "0") {
			dtlOk = false;
			return false;
		}
	});

	if (!allOk) {
		alert("非建单状态的数据不能审核!", 1);
		return;
	}

	if (!dtlOk) {
		alert("无明细的单据不能审核!", 1);
		return;
	}

	$.messager
			.confirm("确认", "你确定要审核这" + checkedRows.length + "条数据",
					function(r) {

						if (r) {
							var keyStr = [];
							$.each(checkedRows, function(index, item) {
								keyStr.push(item.checkTaskNo);
							});
							var url = BasePath
									+ '/bill_um_check_task/auditUmCheckTask';
							var data = {
								"keyStr" : keyStr.join(",")
							};
							wms_city_common.loading("show", "正在审核......");
							$.ajax({
								async : false,
								cache : false,
								type : 'POST',
								data : data,
								dataType : "json",
								url : url,
								success : function(data) {
									wms_city_common.loading();
									if (data.result == "success") {
										alert('审核成功!');
										$('#dataGridJG').datagrid('load');
									} else {
										alert(data.msg, 2);
									}
								}
							});
						}
					});
};

// 委托业主
billumchecktask.ownerFormatter = function(value, rowData, rowIndex) {
	return billumchecktask.ownnerData[value];
};

// 用户
billumchecktask.userFormatter = function(value, rowData, rowIndex) {
	return billumchecktask.usersData[value];
};

// 初始化当前登录的用户 信息
billumchecktask.initCurrentUser = function() {
	ajaxRequestAsync(BasePath + '/initCache/getCurrentUser', {},
			function(data) {
				billumchecktask.loginName = data.loginName;
				billumchecktask.locno = data.locno;
			});
};

// 初始化状态
billumchecktask.initStatus = function(data) {
	wms_city_common
			.comboboxLoadFilter(
					[ "statusCondition" ],
					null,
					null,
					null,
					true,
					[ true ],
					BasePath
							+ '/initCache/getLookupDtlsList.htm?lookupcode=UM_CHECK_TASK_STATUS',
					{}, null, null);
};

// 初始化委托业主
billumchecktask.ownnerData = {};
billumchecktask.initOwnerNo = function() {
	wms_city_common.comboboxLoadFilter([ "ownerNoCondition", "search_ownerNo",
			"ownerNo_view", "ownerNo_edit" ], "ownerNo", "ownerName",
			"ownerName", false, [ false, true, false, false ], BasePath
					+ '/entrust_owner/get_biz', {}, billumchecktask.ownnerData,
			null);
};

// 退仓类型
billumchecktask.initUntreadType = function() {
	wms_city_common.comboboxLoadFilter([ "untreadType_search" ], null, null,
			null, true, [ false, false, true ], BasePath
					+ '/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
			{}, null, null);

};

billumchecktask.initQuality = function() {
	wms_city_common
			.comboboxLoadFilter(
					[ "quality_search" ],
					null,
					null,
					null,
					true,
					[ false, false, true ],
					BasePath
							+ '/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
					{}, null, null);
};
billumchecktask.dataGridJGfooter = {};

billumchecktask.usersData = {};
billumchecktask.initUsers = function() {
	wms_city_common.comboboxLoadFilter(
			["search_creator", "search_auditor", "creatorCondition", "auditorCondition"],
			'workerNo',
			'workerName',
			'unionName',
			false, [true, true, true, true],
			BasePath + '/authority_user/user.json', {},
			billumchecktask.usersData,
			null);
};


// 初始化信息
$(document).ready(function() {
	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
					billumchecktask.initCurrentUser();
					billumchecktask.initStatus();
					billumchecktask.initOwnerNo();
					billumchecktask.initUsers();
					billumchecktask.initUntreadType();
					billumchecktask.initQuality();
					
					// 加载品牌
					var objs = [];
					objs.push({
						"sysNoObj" : $('#sysNoSearch'),
						"brandNoObj" : $('#brandNo')
					});// 查询
					wms_city_common.loadSysNo4Cascade(objs);

					var objs2 = [];
					objs2.push({
						"sysNoObj" : $('#search_sysNoSearch'),
						"brandNoObj" : $('#search_brandNo')
					});
					wms_city_common.loadSysNo4Cascade(objs2);

					var objs3 = [];
					objs3.push({
						"sysNoObj" : $('#sysNoConIt'),
						"brandNoObj" : $('#brandNoConIt')
					});
					wms_city_common.loadSysNo4Cascade(objs3);

					$('#checkDtlDgView')
							.datagrid(
									{
										'onLoadSuccess' : function(data) {
											if (data != null) {
												if (data.footer != null) {
													if (data.footer[1].isselectsum) {
														billumchecktask.itemQty = data.footer[1].itemQty;
														billumchecktask.checkQty = data.footer[1].checkQty;
														billumchecktask.difQty = data.footer[1].difQty;
													} else {
														var rows = $(
																'#checkDtlDgView')
																.datagrid(
																		'getFooterRows');
														rows[1]['itemQty'] = billumchecktask.itemQty;
														rows[1]['checkQty'] = billumchecktask.checkQty;
														rows[1]['difQty'] = billumchecktask.difQty;
														$('#checkDtlDgView')
																.datagrid(
																		'reloadFooter');
													}
												}
											}

										}
									});

					$('#untreadDg').datagrid(
							{
								'onBeforeLoad' : function(param) {
									if (typeof (param.locno) != "undefined") {
										var untreadDg2 = $('#untreadDg2')
												.datagrid('getRows');
										param.datas = JSON
												.stringify(untreadDg2);
									}
								}
							});
					$('#dataGridJG')
							.datagrid(
									{
										'onLoadSuccess' : function(data) {
											if (data.footer[1].isselectsum) {
												billumchecktask.dataGridJGfooter = data.footer[1];
											} else {
												var rows = $('#dataGridJG')
														.datagrid(
																'getFooterRows');
												rows[1] = billumchecktask.dataGridJGfooter;
												$('#dataGridJG').datagrid(
														'reloadFooter');
											}
										}
									});
				});
