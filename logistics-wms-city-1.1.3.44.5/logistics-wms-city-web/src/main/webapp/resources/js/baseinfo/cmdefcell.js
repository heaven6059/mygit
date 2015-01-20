var cmdefcell = {};

cmdefcell.ajaxRequest = function(url, reqParam, async, callback) {
	$.ajax({
		async : async,
		type : 'POST',
		url : url,
		data : reqParam,
		cache : true,
		success : callback
	});
};

cmdefcell.checkExistFun = function(url, checkColumnJsonData) {
	var checkExist = false;
	$.ajax({
		type : 'POST',
		url : url,
		data : checkColumnJsonData,
		cache : true,
		async : false, // 一定要
		success : function(totalData) {
			totalData = parseInt(totalData, 10);
			if (totalData > 0) {
				checkExist = true;
			}
		}
	});
	return checkExist;
};


//========================主页面查询、清空、删除、导出BEGIN========================
//清除
cmdefcell.searchClear = function() {
	$('#searchForm').form("clear");
	$('#areaQualityCondition').combobox("loadData",[]);
	$('#areaNoCondition').combobox("loadData",[]);
	$('#stockNoCondition').combobox("loadData",[]);
};
//查询
cmdefcell.searchCell = function() {
	var fromObjStr = convertArray($('#searchForm').serializeArray());
	var queryMxURL = BasePath + '/cm_defcell/list.json';
	var params = eval("(" + fromObjStr
			+ ")");
	params.locno = cmdefcell.locno;
	$("#dataGridJG").datagrid('options').queryParams = params;
	$("#dataGridJG").datagrid('options').url = queryMxURL;
	$("#dataGridJG").datagrid('load');
};
// 导出
cmdefcell.exportExcel = function() {
	exportExcelBaseInfo('dataGridJG',
			'/cm_defcell/do_export.htm?businessType=9&locno='+cmdefcell.user.locno, '储位导出');
};
// 删除
cmdefcell.deleteRows = function() {
	// 1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	// debugger;
	if (checkedRows.length < 1) {
		alert('请选择要删除的记录!', 1);
		return;
	}

	var cellStatusTip = "";
	var keys = [];
	$.each(checkedRows, function(index, item) {
		keys[keys.length] = {
			locno : item.locno,
			cellNo : item.cellNo
		};
		if (item.cellStatus == '0') {
			cellStatusTip += item.cellNo;
			return false;
		}
	});

	// 2.绑定数据
	var url = BasePath + '/cm_defcell/delete_records';
	var data = {
		locno : cmdefcell.locno,
		datas : JSON.stringify(keys)
	};

	// 3. 删除
	$.messager.confirm('确认', '您确认想要删除记录吗？', function(r) {
		if (r) {

			if (cellStatusTip != '') {
				alert("储位：【" + cellStatusTip + "】启用状态,不能删除!");
				return;
			}
			wms_city_common.loading("show","正在删除储位......");
			cmdefcell.ajaxRequest(url, data, true, function(result) {
				if (result.flag == 'success') {
					// 4.删除成功,清空表单
					cmdefcell.searchCell();
					alert('删除成功!');
				} else if (result.flag == 'warn') {
					alert("储位：【" + result.msg + "】存在库存信息,不能删除!");
				} else if (result.flag == 'hasSettingNo') {
					alert("储位：【" + result.msg + "】已被上架策略引用,不能删除!");
				} else {
					alert('删除失败,请联系管理员!', 2);
				}
				wms_city_common.loading();
			});
		}
	});
};
// ========================主页面查询、清空、删除、导出END========================
// ========================新增明细BEGIN========================
//打开明细
cmdefcell.openNewUI = function(opt) {
	$('#newUI').window({
		title : opt
	});
	$('#newUI').window('open');
};
//关闭新增
cmdefcell.closeNewUI = function(opt) {
	$("#newUI").window({
		onBeforeClose : function() {
			$(".tooltip").remove();
		}
	});
	$('#newUI').window('close');
};
// 新增明细
cmdefcell.addUI = function(opt) {
	$('#newForm').form("clear");
	
	cmdefcell.openNewUI("新增");
	$("#info_save_new").show();
	$("#ownerNo_new").combobox("enable");
	$("#wareNo_new").combobox("enable");
	$("#areaNo_new").combobox("enable");
	$("#stockNo_new").combobox("enable");
	$("#cellNo_new").attr("disabled", false);
	$("#itemType_new").combobox("enable");
	$("#areaQualityform_new").combobox("enable");
	$("#bPick_new").combobox("enable");
	$("#mixFlag_new").combobox("enable");
	$("#cellStatus_new").combobox("enable");
	$("#checkStatus_new").combobox("enable");
	
	$('#itemType_new').combobox("select", '0');
	$('#areaQualityform_new').combobox("select", '0');
	$('#bPick_new').combobox("select", '0');
	//$('#mixFlag_new').combobox("select", '0');
	$('#cellStatus_new').combobox("select", '0');
	$('#checkStatus_new').combobox("select", '0');
	
	$("#limitType_def_new").attr('checked', 'checked');
	$("#aFlag_def_new").attr('checked', 'checked');
	$("#pickFlag_def_new").attr('checked', 'checked');
	$("#mixSupplier_def_new").attr('checked', 'checked');
	
};
//新增保存
cmdefcell.save = function() {
	var fromObj = $('#newForm');
	// 1.校验必填项
	var validateForm = fromObj.form('validate');
	if (validateForm == false) {
		return;
	}
	// 2.检验是否有重复记录((1)主键不重复(如果是序列生成就不用),(2)名称不能重复等等)
	var checkUrl = BasePath + '/cm_defcell/get_count.json';
	var checkDataNo = {
		"locno" : cmdefcell.locno,
		"wareNo" : $("#wareNo_new").datebox('getValue'),
		"areaNo" : $("#areaNo_new").datebox('getValue'),
		"stockNo" : $("#stockNo_new").datebox('getValue'),
		"cellNo" : $("#cellNo_new").val()
	};
	if (cmdefcell.checkExistFun(checkUrl, checkDataNo)) {
		alert('储位编号已存在,不能重复!', 1);
		$("#cellNo_new").focus();
		return;
	}
	$.messager.confirm('确认', '您确认新增储位吗？', function(r) {
	if (r) {
		// 3. 保存
		wms_city_common.loading("show", "正在保存储位......");
		var url = BasePath + '/cm_defcell/add_post';
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {
				param.locno = cmdefcell.user.locno;
				param.creator = cmdefcell.user.loginName;
				param.creatorName = cmdefcell.user.username;
				param.editor = cmdefcell.user.loginName;
				param.editorName = cmdefcell.user.username;
			},
			success : function(data) {
				var obj = eval('(' + data + ')');
				if(obj.result == 'success'){
					alert('新增成功!');
					cmdefcell.searchCell();
					cmdefcell.closeNewUI();
				}else{
					alert(obj.result);
				}
				wms_city_common.loading();
			},
			error : function() {
				alert('新增失败,请联系管理员!', 2);
				wms_city_common.loading();
			}
		});
	}
	});
};
//========================新增明细END========================
// 隐藏显示按钮
cmdefcell.showHideBtn = function(type) {
	if (type == "add") {
	} else if (type == "edit") {
		cmdefcell.openUI("修改");
		$("#info_save").show();
		$("#ownerNo").combobox("disable");
		$("#wareNo").combobox("disable");
		$("#areaNo").combobox("disable");
		$("#stockNo").combobox("disable");
		$("#checkStatus").combobox("disable");
		$("#cellNo").attr("disabled", true);
	} else {
		cmdefcell.openUI("详情");
		$("#info_save").hide();
		$("#ownerNo").combobox("disable");
		$("#wareNo").combobox("disable");
		$("#areaNo").combobox("disable");
		$("#stockNo").combobox("disable");
		$("#checkStatus").combobox("disable");
		$("#cellNo").attr("disabled", true);
	}
};
// 打开明细
cmdefcell.openUI = function(opt) {
	$('#openUI').window({
		title : opt
	});
	$('#openUI').window('open');
};
// 关闭明细
cmdefcell.closeUI = function(opt) {
	$("#openUI").window({
		onBeforeClose : function() {
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('close');
};

// 查询明细
cmdefcell.dtlView = function(rowData, type) {
	$("#itemType").combobox("select", rowData.itemType);
	$("#areaQualityform").combobox("select", rowData.areaQuality);
	$('#dataForm').form('load', rowData);
	cmdefcell.showHideBtn("view");
};
// 编辑明细
cmdefcell.editUI = function() {
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if (checkedRows.length < 1) {
		alert('请选择要修改的记录!', 1);
		return;
	} else if (checkedRows.length > 1) {
		alert('只能修改一条记录!', 1);
		return;
	} else {
		cmdefcell.loadDetail(checkedRows[0], "edit");
	}
};
cmdefcell.loadDetail = function(rowData, type) {
	$('#dataForm').form('load', rowData);
	cmdefcell.showHideBtn("edit");
	if (rowData.ownerNo == null) {
		$("#ownerNo").combobox("enable");
	}
};
//========================明细END=======================
//========================明细保存、更新Begin========================
cmdefcell.update = function() {
	// 判断当前通道是否状态是否可修改
	if (!cmdefcell.checkStock()) {
		return;
	}
	var fromObj = $('#dataForm');
	// 1.校验必填项
	var validateForm = fromObj.form('validate');
	if (validateForm == false) {
		return;
	}
	$.messager.confirm('确认', '您确认修改储位吗？', function(r) {
		if (r) {
			// 2.绑定数据
			wms_city_common.loading("show", "正在修改储位......");
			var url = BasePath + '/cm_defcell/add_put';
			fromObj.form('submit', {
				url : url,
				onSubmit : function(param) {
					param.editor = cmdefcell.user.loginName;
					param.editorName = cmdefcell.user.username;
					param.edittm = cmdefcell.user.currentDate19Str;
					param.locno = cmdefcell.user.locno;
					param.cellNo = $("#cellNo").val();
					param.areaNo = $("#areaNo").combobox('getValue');
					param.wareNo = $("#wareNo").combobox('getValue');
				},
				success : function(data) {
					var obj = eval('(' + data + ')');
					if (obj.result == "success") {
						alert('修改成功!');
						// 3.保存成功,清空表单
						cmdefcell.searchCell();
						cmdefcell.closeUI();
						wms_city_common.loading();
					} else {
						wms_city_common.loading();
						alert(obj.result, 2);
					}
				},
				error : function() {
					alert('修改失败,请联系管理员!', 2);
					wms_city_common.loading();
				}
			});
		}
	});
};
cmdefcell.checkStock = function() {
	// 获取下拉框的值
	var cellStatus = $('#cellStatus').combobox('getValue');
	if (cellStatus != 1) {
		return true;
	}
	var result = false;
	var locNo = cmdefcell.locno;
	var cellNo = $("#cellNo").val();
	$.ajax({
		async : false,
		type : 'POST',
		dataType : "json",
		url : BasePath + '/cm_defcell/checkCell',
		data : {
			"locno" : locNo,
			"cellNo" : cellNo,
			"cellStatus" : cellStatus
		},
		cache : true,
		success : function(data) {
			if (data.result == "success") {
				result = true;
			} else if (data.result == "exist") {
				alert("请检查储位关联库存");
				result = false;
			} else if (data.result == "exist") {
				alert("检查通道出错");
				result = false;
			}
		}
	});
	return result;
};
/**
 * 禁用储位
 */
cmdefcell.disableCell = function() {
	// 1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	// debugger;
	if (checkedRows.length < 1) {
		alert('请选择需要禁用的储位!', 1);
		return;
	}

	var keys = "";
	for ( var index = 0; index < checkedRows.length; index++) {
		var item = checkedRows[index];
		if (!(item.cellStatus == "0" || item.cellStatus == "2")) {
			alert('只能禁用【可用】或【冻结】状态的储位!');
			return;
		}
		if (index == checkedRows.length - 1) {
			keys += item.cellNo+"|"+item.cellStatus
		} else {
			keys += (item.cellNo+"|"+item.cellStatus + ",");
		}
	}
	// alert(keys);
	$.messager.confirm('确认', '您确认想要禁用这'+checkedRows.length+'记录吗？', function(r) {
		if (r) {
			var url = BasePath + '/cm_defcell/disableCell';
			var params = {
					keys:keys,
					locno:cmdefcell.locno
			};
			wms_city_common.loading("show","正在禁用储位......");
			cmdefcell.ajaxRequest(url, params, true, function(result) {
				if (result.result == 'success') {
					cmdefcell.searchCell();
					alert('禁用成功!');
				} else {
					alert(result.result+',禁用失败!', 2);
				}
				wms_city_common.loading();
			});
		}
	});
};
/**
 * 储位解禁
 */
cmdefcell.enableCell = function() {
	// 1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	// debugger;
	if (checkedRows.length < 1) {
		alert('请选择需要解禁的储位!', 1);
		return;
	}

	var keys = "";
	for ( var index = 0; index < checkedRows.length; index++) {
		var item = checkedRows[index];
		if (item.cellStatus == "0" || item.cellStatus == "2") {
			alert('只能解禁【禁用】状态的储位!');
			return;
		}
		if (index == checkedRows.length - 1) {
			keys += item.cellNo+"|"+item.cellStatus;
		} else {
			keys += (item.cellNo+"|"+item.cellStatus + ",");
		}
	}
	// alert(keys);
	$.messager.confirm('确认', '您确认想要解禁这'+checkedRows.length+'记录吗？', function(r) {
		if (r) {
			var url = BasePath + '/cm_defcell/enableCell';
			var params = {
					keys:keys,
					locno:cmdefcell.locno
			};
			wms_city_common.loading("show","正在解禁储位......");
			cmdefcell.ajaxRequest(url, params, true, function(result) {
				if (result.result == 'success') {
					cmdefcell.searchCell();
					alert('解禁成功!');
				} else {
					alert(result.result+',解禁失败!', 2);
				}
				wms_city_common.loading();
			});
		}
	});
};
// ========================明细保存、更新END========================
// ========================初始化BEGIN========================
cmdefcell.user = {};
cmdefcell.locno;
$(document).ready(function() {

			cmdefcell.ajaxRequest(BasePath + '/initCache/getCurrentUser', {},
					true, function(u) {
						cmdefcell.user = u;
						cmdefcell.locno = u.locno;
					});
			//初始化货主
			wms_city_common.comboboxLoadFilter(
					["ownerNo","ownerNo_new"],
					'ownerNo',
					'ownerName',
					'valueAndText',
					false,
					[false,false],
					BasePath+'/entrust_owner/get_biz',
					{},
					cmdefcell.ownerNo,
					null);
			//初始化仓区
			wms_city_common.initWareNoForCascade(
					cmdefcell.locno,
					['wareNoCondition','wareNo','wareNo_new'],
					['areaNoCondition','areaNo','areaNo_new'],
					['stockNoCondition','stockNo','stockNo_new'],
					null,
					[true,false,false],
					null
					);
			//初始化混载标志
			wms_city_common.comboboxLoadFilter(
					["mixFlagCondition","mixFlag","mixFlag_new"],
					null,
					null,
					null,
					true,
					[true,false,false],
					BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=MIX_FLAG',
					{},
					cmdefcell.mixFlag,
					null);
			//初始化试算标志
			wms_city_common.comboboxLoadFilter(
					["bPickCondition","bPick","bPick_new"],
					null,
					null,
					null,
					true,
					[true,false,false],
					BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=B_PICK',
					{},
					cmdefcell.bPick,
					null);
			//初始化储位状态
			wms_city_common.comboboxLoadFilter(
					["cellStatusCondition","cellStatus","cellStatus_new"],
					null,
					null,
					null,
					true,
					[true,false,false],
					BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CELL_STATUS',
					{},
					cmdefcell.cellStatus,
					null);
			//初始化盘点状态
			wms_city_common.comboboxLoadFilter(
					["checkStatusCondition","checkStatus","checkStatus_new"],
					null,
					null,
					null,
					true,
					[true,false,false],
					BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CHECK_STATUS',
					{},
					cmdefcell.checkStatus,
					null);
			wms_city_common.initItemTypeAndQuality("itemType_new",
					"areaQualityform_new", false);
			wms_city_common.initItemTypeAndQuality("itemType",
					"areaQualityform", false);
			//初始化类型
			wms_city_common.initItemTypeAndQuality("itemTypeCondition",
			"areaQualityCondition",true);
			
			wms_city_common.closeTip("openUI");
			wms_city_common.closeTip("newUI");
			
			$("#length_new").numberbox({
				onChange:cmdefcell.countVolume
			});
			$("#width_new").numberbox({
				onChange:cmdefcell.countVolume
			});
			$("#height_new").numberbox({
				onChange:cmdefcell.countVolume
			});
			$("#length_edit").numberbox({
				onChange:cmdefcell.countVolume
			});
			$("#width_edit").numberbox({
				onChange:cmdefcell.countVolume
			});
			$("#height_edit").numberbox({
				onChange:cmdefcell.countVolume
			});
});
// ========================初始化END========================
// ========================委托业主========================
cmdefcell.ownerNo = {};
cmdefcell.ownerNoFormatter = function(value, rowData, rowIndex) {
	return cmdefcell.ownerNo[value];
};
// ========================委托业主END========================
// ==================试算标识BEGIN=======================
cmdefcell.bPick = {};
cmdefcell.bPickFormatter = function(value, rowData, rowIndex) {
	return cmdefcell.bPick[value];
};
// ==================试算标识END=======================
// ==================混载标志BEGIN=======================
cmdefcell.mixFlag = {};
cmdefcell.mixFlagFormatter = function(value, rowData, rowIndex) {
	return cmdefcell.mixFlag[value];
};
// ==================混载标志END=======================
// ==================储位状态BEGIN=======================
cmdefcell.cellStatus = {};
cmdefcell.cellStatusFormatter = function(value, rowData, rowIndex) {
	return cmdefcell.cellStatus[value];
};
// ==================储位状态END=======================
// ==================盘点状态BEGIN=======================
cmdefcell.checkStatus = {};
cmdefcell.checkStatusFormatter = function(value, rowData, rowIndex) {
	return cmdefcell.checkStatus[value];
};
// ==================盘点状态END=======================

// ==================打印=======================
cmdefcell.printCell = function() {

	var resultData;
	var rows = $('#dataGridJG').datagrid('getChecked');
	if (rows.length == 0) {
		alert('请选择需要打印的数据!');
		return;
	}
	var LODOP = getLodop(document.getElementById('LODOP_OB'), document
			.getElementById('LODOP_EM'));
	if (LODOP == null) {
		return;
	}
	LODOP.SET_PRINT_PAGESIZE(1, '84.5mm', '47.5mm', "");
	for ( var i = 0, length = rows.length; i < length; i++) {
		LODOP.NewPage();
		var cmdefcell = rows[i];

		// 标签内容：仓库-库区-通道-储位列格层
		var wareNo = cmdefcell.wareNo;// 仓区
		var areaNo = cmdefcell.areaNo;// 库区
		var stockNo = cmdefcell.stockNo;// 通道
		var stockX = cmdefcell.stockX;// 储格列
		var bayX = cmdefcell.bayX;// 储格位
		var stockY = cmdefcell.stockY;// 储格层

		var html = "<div style='font-size:45px;font-weight:bold;width:100%;text-align:center;'>"
				+ wareNo
				+ ""
				+ areaNo
				+ ""
				+ stockNo
				+ ""
				+ stockX
				+ "-"
				+ bayX + "-" + stockY + "</div>";
		LODOP.ADD_PRINT_HTM(100, 0, "100%", "100%", html);
		LODOP.SET_PRINT_STYLE("FontSize", 20);
		LODOP.ADD_PRINT_BARCODE("5mm", "20mm", "65mm", "25mm", "128A",
				cmdefcell.cellNo);// LODOP.ADD_PRINT_BARCODE(上边距，，条码宽度，条码高度）
		LODOP.SET_PRINT_STYLEA(0, "ShowBarText", 0);
		LODOP.SET_PRINT_STYLEA(0, "Horient", 2);// 水平居中
		// LODOP.SET_PRINT_STYLEA(0,"Vorient",2);//垂直居中
	}
	LODOP.PREVIEW();

};
cmdefcell.printCellBySearch = function() {
	var max = 100;// 打印数据的上限(条)
	var rows = $("#dataGridJG").datagrid('getRows');
	if (rows.length <= 0) {
		alert("查询记录为空 ,不能打印!");
		return;
	}
	var params = $("#dataGridJG").datagrid('options').queryParams;
	var url = BasePath + '/cm_defcell/all_list.json?locno=' + cmdefcell.locno;
	var resultData = null;
	$.ajax({
		cache : false,
		async : false,
		type : 'POST',
		url : url,
		data : params,
		success : function(result) {
			resultData = result;
		}
	});
	if (resultData == null || resultData.total == null || resultData.total <= 0) {
		alert("查询记录为空 ,不能打印!");
		return;
	} else {
		/*
		 * if(resultData.total > max){ alert("查询超过"+max+"条记录,不能打印!"); return; }
		 */
		var LODOP = getLodop(document.getElementById('LODOP_OB'), document
				.getElementById('LODOP_EM'));
		if (LODOP == null) {
			return;
		}
		LODOP.SET_PRINT_PAGESIZE(1, '84.5mm', '47.5mm', "");
		var cmDefCll = null;
		for ( var i = 0; i < resultData.total; i++) {
			cmDefCll = resultData.rows[i];
			LODOP.NewPage();
			var wareNo = cmDefCll.wareNo;// 仓区
			var areaNo = cmDefCll.areaNo;// 库区
			var stockNo = cmDefCll.stockNo;// 通道
			var stockX = cmDefCll.stockX;// 储格列
			var bayX = cmDefCll.bayX;// 储格位
			var stockY = cmDefCll.stockY;// 储格层

			var html = "<div style='font-size:45px;font-weight:bold;width:100%;text-align:center;'>"
					+ wareNo
					+ ""
					+ areaNo
					+ ""
					+ stockNo
					+ ""
					+ stockX
					+ "-"
					+ bayX + "-" + stockY + "</div>";
			LODOP.ADD_PRINT_HTM(100, 0, "100%", "100%", html);
			LODOP.SET_PRINT_STYLE("FontSize", 20);
			LODOP.ADD_PRINT_BARCODE("5mm", "20mm", "65mm", "25mm", "128A",
					cmDefCll.cellNo);// LODOP.ADD_PRINT_BARCODE(上边距，，条码宽度，条码高度）
			LODOP.SET_PRINT_STYLEA(0, "ShowBarText", 0);
			LODOP.SET_PRINT_STYLEA(0, "Horient", 2);// 水平居中
		}
		LODOP.PRINT();
	}
};
cmdefcell.downloadTemp = function() {
	window.open(BasePath + "/cmdefcell_import/downloadTemple");
};
cmdefcell.importConToItem = function() {
	var fromObj = $("#showDialog");
	$("#iframe").attr("src",BasePath + "/cmdefcell_import/iframe?v="+new Date());
	$("#showImportDialog").window('open');
};
cmdefcell.loading = function(type,msg){
	wms_city_common.loading(type,msg);
};
cmdefcell.importSuccess = function(uuId){
	$('#importDialogView').window({title:"储位导入"});
	$("#importDialogView").window('open');
	cmdefcell.closeWindow("showImportDialog");	
	var tempObj = $('#importGridJG_view');
	tempObj.datagrid( 'options' ).url = BasePath+'/cmdefcell_import/excel_preview?locNo='+cmdefcell.locno+'&uuId='+uuId;
	tempObj.datagrid('load');
	$("#uuId").val(uuId);
	//alert("导入成功!");
};
//关闭窗口
cmdefcell.closeWindow = function(id){
	var uuId=$("#uuId").val();
	//清空预览数据
	if(uuId){
		var url = BasePath+'/cmdefcell_import/clearExcelTemp?locNo='+cmdefcell.locno+'&uuId='+uuId;
		$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:url,
			success : function(data) {}
		});
	}
	$('#'+id).window('close');  
	$("#uuId").val('');
};
//清除excel数据
cmdefcell.clearExcelTemp = function(){
	var uuId=$("#uuId").val();
	if(uuId){
		var url = BasePath+'/cmdefcell_import/clearExcelTemp?uuId='+uuId;
		$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:url,
			success : function(data) {
//				alert("清空数据成功!");
				var uuId=data.uuId;
				wms_city_common.loading();
				var tempObj = $('#importGridJG_view');
				tempObj.datagrid( 'options' ).url = BasePath+'/cmdefcell_import/excel_preview?uuId='+uuId;
				tempObj.datagrid('load');
				$("#uuId").val('');
			}
		});
	}else{
		alert("没有可清空的数据！",2);
	}
};
//弹出导入选择框
cmdefcell.showImportExcel = function(formId){
	var fromObj = $("#"+formId);
	$("#iframe").attr("src",BasePath + "/cmdefcell_import/iframe?v="+new Date());
	$("#showImportDialog").window('open'); 
};
cmdefcell.saveExcelItem = function(){
	var uuId=$("#uuId").val();
	if(uuId){
		cmdefcell.loading("show","正在保存数据......");
		var url = BasePath+'/cmdefcell_import/saveExcelTemp?locNo='+cmdefcell.locno+'&uuId='+uuId;
		$.ajax({
			async : false,
			cache : false,
			type : 'GET',
			dataType : "json",
			url:url,
			success : function(data) {
				var uuId=data.uuId;
				var result=data.result;
				if(result=='success'){
					alert("您已成功导入"+data.count+"条数据。");
					cmdefcell.closeWindow("importDialogView");
					$("#uuId").val('');
				}else{
					var msg=data.msg;
					alert(msg,2);
				}
				wms_city_common.loading();
//				var tempObj = $('#importGridJG_view');
//				tempObj.datagrid( 'options' ).url = BasePath+'/cmdefcell_import/excel_preview?uuId='+uuId;
//				tempObj.datagrid('load');
			}
		});
	}else{
		alert("没有保存的数据，请先导入Excel！",2);
	}
};
cmdefcell.countVolume = function(newValue,oldValue){
	  var id=$(this).attr("id");
	  var oper = "new";
	  if(id.lastIndexOf("edit") > 1){
		  oper = "edit";
	  }
	var length = $("#length_"+oper).numberbox("getValue");
	var width = $("#width_"+oper).numberbox("getValue");
	var height = $("#height_"+oper).numberbox("getValue");
	if(length >0 && width>0 && height >0){
		var volume = (length*width*height)/1000000;
		$("#volume_"+oper).numberbox("setValue",volume);
	}else{
		$("#volume_"+oper).numberbox("setValue",'');
	}
};


