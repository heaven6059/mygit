var lookup = {};
lookup.user = {};
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
$(document).ready(function(){
	lookup.getCurrentUser();
	/** 初始化下拉框* */
	lookup.initlookutype();
	lookup.initlookuplevel();
	lookup.initsysno();
});

/***********************************************初始化begin******************************************************/
lookup.getCurrentUser = function() {
	$.ajax({
		type : 'POST',
		url : BasePath + '/initCache/getCurrentUser',
		data : {},
		cache : true,
		async : false, // 一定要
		success : function(resultData) {
			lookup.user = resultData;
		}
	});
	return lookup.user;
};
lookup.lookupType = [ {
	'label' : '0',
	'text' : '系统初始化属性',
	'value' : '0→系统初始化属性'
}, {
	'label' : '1',
	'text' : '商品基本分类属性',
	'value' : '1→商品基本分类属性'
}, {
	'label' : '2',
	'text' : '商品类别属性',
	'value' : '2→商品类别属性'
} ];
/** 初始化编码类型**/
lookup.initlookutype = function() {
	$('#lookuptypeadd').combobox({
		valueField : "label",
		textField : "value",
		data : lookup.lookupType,
		panelHeight : 150
	});
	$('#lookuptypedialog').combobox({
		valueField : "label",
		textField : "value",
		data : lookup.lookupType,
		panelHeight : 150
	});
};

lookup.lookuplevel = [ {
	'label' : '0',
	'text' : '用户不可配置',
	'value' : '用户不可配置'
}, {
	'label' : '1',
	'text' : '用户可配置',
	'value' : '用户可配置'
} ];
/** 初始化用户是否可配置**/
lookup.initlookuplevel = function() {
	$('#lookupleveladd').combobox({
		valueField : "label",
		textField : "value",
		data : lookup.lookuplevel,
		panelHeight : 150
	});
	$('#lookupleveldialog').combobox({
		valueField : "label",
		textField : "value",
		data : lookup.lookuplevel,
		panelHeight : 150
	});
};

/** 初始化品牌库**/
lookup.initsysno = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			$('#sysNoadd').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:200
			});
			$('#sysNodialog').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:200
			});
		}
	});
};
/***********************************************初始化end******************************************************/
/***********************************************主页面begin******************************************************/
/** 清空查询条件* */
lookup.searchClear = function() {
	$('#searchForm').form("clear");
};

/** 查询码表js方法* */
lookup.searchLookup = function() {
	var fromObjStr = convertArray($('#searchForm').serializeArray());
	var queryMxURL = BasePath + '/lookup/list.json';
	$("#dataGridLU").datagrid('options').queryParams = eval("(" + fromObjStr + ")");
	$("#dataGridLU").datagrid('options').url = queryMxURL;
	$("#dataGridLU").datagrid('load');
};

/** 删除码表信息* */
lookup.deleteLookup = function() {
	var checkedRows = $("#dataGridLU").datagrid("getChecked");// 获取所有勾选checkbox的行
	if (checkedRows.length != 1) {
		alert('请选择一条记录！', 1);
		return;
	}
	wms_city_common.loading('show');
	$.ajax({
		type : "POST",
		url : "deleteLookupCode.json",
		data : [ {
			name : 'lookupcode',
			value : checkedRows[0].lookupcode
		} ],
		success : function(msg) {
			wms_city_common.loading();
			lookup.searchLookup();
			alert("删除成功", 1);
		},
		error : function(msg) {
			wms_city_common.loading();
			if(msg.status==403){
				alert('您没有删除权限',2);
			}else{
				alert('删除失败，请联系管理员', 1);
			}
			
		}
	});
};

lookup.systemidDtl = null;
lookup.lookupcodeDtl = null;
/** 明细面板* */
lookup.loadDetailDtl = function(rowData) {
	lookup.systemidDtl = rowData.systemid;
	lookup.lookupcodeDtl = rowData.lookupcode;
	$('#dataGridLU_Dtl').datagrid(
			{
				'url' : BasePath + '/lookupdtl/list.json?lookupcode=' + rowData.lookupcode,
				'title' : '码表定义明细--当前编码' + rowData.lookupcode,
				'pageNumber' : 1
			});

	$("#lookupcodedialog").val(rowData.lookupcode);
	$("#lookupnamedialog").val(rowData.lookupname);
	$("#lookuptypedialog").combobox('setValue', rowData.lookuptype);
	$("#lookupleveldialog").combobox('setValue', rowData.lookuplevel);
	$("#sysNodialog").combobox('setValue', rowData.brandNo);
	$("#remarkdialog").val(rowData.remarks);
	
	$("#lookupcodedialog").attr("disabled",true);
	$("#lookupnamedialog").attr("disabled",true);
	$("#remarkdialog").attr("disabled",true);
	$("#lookuptypedialog").combobox("disable");
	$("#lookupleveldialog").combobox("disable");
	$("#sysNodialog").combobox("disable");
	
	$("#lookupDtlInfo").show();
	$("#lookupDtlInfo").window('open');
};

/** 打开新增码表表头窗口* */
lookup.showAddWindow = function() {
	var tempDialog = $("#addWindow");
	tempDialog.window({
		title : '新增码表表头信息'
	});
	lookup.showType("add");
	tempDialog.window('open');
};

lookup.showType = function(type) {
	if(type == "add") {
		$("#dataGridLU_DtlForAdd").datagrid('loadData',{rows:[],total:1});//清空datagrid数据
		$("#lookupcodeadd").val('');
		$("#lookupnameadd").val('');
		$("#remarkadd").val('');
		$(':input', '#lookupdataForm').not(':button, :submit, :reset, :hidden').val('').removeAttr('checked').removeAttr('selected');
		
		$("#lookupnameadd").attr("disabled",false);
		$("#lookuptypeadd").combobox("enable");
		$("#lookupleveladd").combobox("enable");
		$("#sysNoadd").combobox("enable");
		$("#remarkadd").attr("disabled",false);
		$("#lookupcodeadd").attr('readOnly',false);
		
		$("#btn-modify").hide();// 隐藏修改按钮
		$("#btn-add").show();//显示新增按钮
		$("#lookupdtl").hide();
	} else if(type == "edit"){
		$("#lookupnameadd").attr("disabled",true);
		$("#lookuptypeadd").combobox("enable");
		$("#lookupleveladd").combobox("enable");
		$("#sysNoadd").combobox("enable");
		$("#remarkadd").attr("disabled",false);
		$("#lookupcodeadd").attr("readOnly", true);// 设置编码为只读
		
		$("#btn-modify").show();// 显示修改按钮
		$("#btn-add").hide();// 隐藏新增按钮
		$("#lookupdtl").show();
	} else{
		$("#lookupnameadd").attr("disabled",true);
		$("#lookuptypeadd").combobox("disable");
		$("#lookupleveladd").combobox("disable");
		$("#sysNoadd").combobox("disable");
		$("#remarkadd").attr("disabled",true);
		$("#lookupcodeadd").attr("readOnly", true);
		$("#btn-modify").hide();
		$("#btn-add").hide();
		$("#lookupdtl").hide();
	}
};

/** 显示修改面板* */
lookup.showUpdateWindow = function() {
	var checkedRows = $("#dataGridLU").datagrid("getChecked");// 获取所有勾选checkbox的行
	if (checkedRows.length != 1) {
		alert('请选择一条记录！', 1);
		return;
	} else if(checkedRows[0].systemid!='22') {
		alert('只允许修改系统编码为22的数据！', 1);
		return;
	}
	
	var tempDialog = $("#addWindow");
	tempDialog.window({
		title : '修改码表信息'
	});
	lookup.loadlookupByCode(checkedRows);
	tempDialog.window('open');
};

/** 根据编码获取码表表头信息* */
lookup.loadlookupByCode = function(checkedRows) {
	$.ajax({
		type : "POST",
		url : "findlookupByCode.json",
		data : [ {
			name : 'lookupcode',
			value : checkedRows[0].lookupcode
		}, {
			name : 'systemid',
			value : checkedRows[0].systemid
		}, {
			name : 'lookuptype',
			value : checkedRows[0].lookuptype
		} ],
		success : function(msg) {
			lookup.setMsg(msg);
			lookup.loadLookupDtl(checkedRows[0].systemid);

		},
		error : function() {
			alert('获取码表详情失败，请联系管理员', 1);
		}
	});
};
/** 设置码表表头详情* */
lookup.setMsg = function(msg) {
	lookup.showType("edit");
	$("#lookupcodeadd").val(msg.lookupcode);
	$("#lookuptypeadd").combobox('setValue', msg.lookuptype);
	$("#lookupleveladd").combobox('setValue', msg.lookuplevel);
	$("#sysNoadd").combobox('setValue', msg.brandNo);
	$("#lookupnameadd").val(msg.lookupname);
	$("#remarkadd").val(msg.remarks);
};
/** 加载码头表体详情* */
lookup.loadLookupDtl = function(systemid, lookupcode) {
	$('#dataGridLU_DtlForAdd').datagrid(
			{
				'url' : BasePath + '/lookupdtl/list.json?lookupcode=' + $("#lookupcodeadd").val(),
				'title' : '码表定义明细--当前编码' + $("#lookupcodeadd").val(),
				'pageNumber' : 1
			});
};

/***********************************************主页面end******************************************************/
/***********************************************新增/修改begin******************************************************/
/** 关闭新增码头表头窗口* */
lookup.closeAddWindow = function() {
	var tempDialog = $("#addWindow");
	$(':input', '#lookupdataForm').not(':button, :submit, :reset, :hidden')
			.val('').removeAttr('checked').removeAttr('selected');
	lookup.searchLookup();
	tempDialog.window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	tempDialog.window('close');
};
/** 保存码表表头信息* */
lookup.saveLookupInfo = function() {
	if ($('#lookupdataForm').form('validate')) {
		
		$.ajax({
			url : 'checkLookupCode.json',
			type : 'post',
			data : [ {
				name : 'lookupcode',
				value : $("#lookupcodeadd").val()
			}],
			dataType : 'json',
			cache : false,
			success : function(r) {
				if(r>0){
					alert('编码值不能重复', 1);
				}else{
					lookup.privt();
				}
			},
			error : function() {
				alert('新增失败，请联系管理员', 1);
			}
		});
	} else {
		return false;
	}
};
lookup.privt= function(){
	var data = $('#lookupdataForm').serializeArray();
	wms_city_common.loading('show');
	$.ajax({
		url : 'addLookup.json',
		type : 'post',
		data : data,
		dataType : 'json',
		cache : false,
		success : function(r) {
			wms_city_common.loading();

			lookup.loadLookupDtl('22');
			lookup.showType("edit");
		},
		error : function(msg) {
			wms_city_common.loading();
			if(msg.status==403){
				alert('您没有新增权限',2);
			}else{
				alert('新增失败，请联系管理员', 1);
			}
		}
	});
};
/** 修改码表表头信息* */
lookup.updateLookup = function() {
	wms_city_common.loading('show');
	var datas = $('#lookupdataForm').serializeArray();
	$.ajax({
		url : "update.json",
		data : datas,
		type : "post",
		dataType : 'json',
		cache : false,
		success : function(r) {
			wms_city_common.loading();
			alert('修改成功', 1);
		},
		error : function(msg) {
			wms_city_common.loading();
			if(msg.status==403){
				alert('您没有修改权限',2);
			}else{
				alert('修改失败，请联系管理员', 1);	
			}
			
		}
	});
};

/** 保存详情明细* */
lookup.saveDtl = function() {
	var tempFlag = lookup.endEdit('dataGridLU_DtlForAdd');
	if (!tempFlag) {
		alert('数据验证没有通过!', 1);
		return;
	}
	var tempObj = $('#dataGridLU_DtlForAdd');
	var chgSize = tempObj.datagrid('getChanges').length;
	if (chgSize < 1) {
		alert('没有数据改动!', 1);
		return;
	}
	var inserted = tempObj.datagrid('getChanges', "inserted");
	if (inserted.length > 0) {
		var checkUrl = BasePath + '/lookup/checkItemValue.json';
		var checkData = {
			"systemid" : 22,
			"lookupcode" : $("#lookupcodeadd").val(),
			"itemval" : '',
		};
		for ( var i = 0; i < inserted.length; i++) {
			checkData.itemval = inserted[i]['itemval'];
			// 验证数据库是否存在相应项目值
			if (lookup.checkExistFun(checkUrl, checkData)) {
				alert('项目值不能重复!', 1);
				return;
			}
			inserted[i].systemid = 22;
			inserted[i].lookupcode = $("#lookupcodeadd").val();
		}
	}
	var deleted = tempObj.datagrid('getChanges', "deleted");
	var updated = tempObj.datagrid('getChanges', "updated");
	if (updated.length > 0) {
		for ( var i = 0; i < updated.length; i++) {
			// delete updated[i]['createtm'];
			// updated[i].editor = currentUser.loginName;
			// updated[i].edittm = currentUser.currentDate19Str;
		}
	}
	var effectRow = {
		inserted : JSON.stringify(inserted),
		deleted : JSON.stringify(deleted),
		updated : JSON.stringify(updated),
	};
	wms_city_common.loading('show');
	$.post(BasePath + '/lookupdtl/addLookupDtl', effectRow, function(result) {
		if (result.success) {
			wms_city_common.loading();
			alert('保存成功!', 1);
			tempObj.datagrid('acceptChanges');
		} else {
			wms_city_common.loading();
			alert('保存失败!', 1);
		}
	}, "JSON").error(function() {
		wms_city_common.loading();
		alert('保存失败!', 1);
	});
};

/***********************************************新增/修改end******************************************************/

lookup.systemId = 0;
lookup.sysNo = '0';
lookup.lookupNo = '0';


lookup.checkExistFun = function(url, checkColumnJsonData) {
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

lookup.insertRowLookup = function(gid) {
	lookup.insertRowAtEnd(gid, {
		systemid : lookup.systemId,
		brandNo : lookup.sysNo,
		lookupno : lookup.lookupNo,
	});
};

lookup.insertRowAtEnd = function(gid, rowData) {
	var tempObj = $('#' + gid);
	if (rowData) {
		tempObj.datagrid('appendRow', rowData);
	} else {
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
};

lookup.removeBySelected = function(gid) {
	var tempObj = $('#' + gid);
	var rowObj = tempObj.datagrid('getSelected');
	if (rowObj) {
		var checkUrl = BasePath + '/lookupdtl/get_count.json';
		var checkData = {
			"systemid" : rowObj.systemid,
			"lookupcode" : rowObj.lookupcode
		};
		if (lookup.checkExistFun(checkUrl, checkData)) {
			alert('请先删除码表定义明细!', 1);
			return;
		}
		var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
		tempObj.datagrid('deleteRow', rowIndex);
	}
};

lookup.removeDtlBySelected = function(gid) {
	var tempObj = $('#' + gid);
	var rowObj = tempObj.datagrid('getSelected');
	if (rowObj) {
		var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
		tempObj.datagrid('deleteRow', rowIndex);
	}
};

lookup.endEdit = function(gid) {
	var tempObj = $('#' + gid);
	var rowArr = tempObj.datagrid('getRows');
	for ( var i = 0; i < rowArr.length; i++) {
		if (tempObj.datagrid('validateRow', i)) {
			tempObj.datagrid('endEdit', i);
		} else {
			return false;
		}
	}
	return true;
};
/**

lookup.save = function() {
	var tempFlag = lookup.endEdit('dataGridLU');
	if (!tempFlag) {
		alert('数据验证没有通过!', 1);
		return;
	}
	var tempObj = $('#dataGridLU');
	var chgSize = tempObj.datagrid('getChanges').length;
	if (chgSize < 1) {
		alert('没有数据改动!', 1);
		return;
	}
	var currentUser = lookup.user;
	var inserted = tempObj.datagrid('getChanges', "inserted");
	if (inserted.length > 0) {
		var checkUrl = BasePath + '/lookup/get_count.json';
		var checkData = {
			"systemid" : lookup.systemId
		};
		for ( var i = 0; i < inserted.length; i++) {
			checkData.lookupcode = inserted[i]['lookupcode'];
			// 验证数据库是否存在相应编码
			if (lookup.checkExistFun(checkUrl, checkData)) {
				alert('编码不能重复!', 1);
				return;
			}
			inserted[i].createtm = currentUser.currentDate19Str;
			inserted[i].creator = currentUser.loginName;
			inserted[i].editor = currentUser.loginName;
			inserted[i].edittm = currentUser.currentDate19Str;
		}
	}
	var deleted = tempObj.datagrid('getChanges', "deleted");
	var updated = tempObj.datagrid('getChanges', "updated");
	if (updated.length > 0) {
		for ( var i = 0; i < updated.length; i++) {
			delete updated[i]['createtm'];
			updated[i].editor = currentUser.loginName;
			updated[i].edittm = currentUser.currentDate19Str;
		}
	}
	var effectRow = {
		inserted : JSON.stringify(inserted),
		deleted : JSON.stringify(deleted),
		updated : JSON.stringify(updated),
	};
	$.post(BasePath + '/lookup/save', effectRow, function(result) {
		if (result.success) {
			alert('保存成功!', 1);
			tempObj.datagrid('acceptChanges');
		}
	}, "JSON").error(function() {
		alert('保存失败!', 1);
	});
};
 **/

lookup.insertRowLookupDtl = function(gid) {
	/*
	 * if (lookup.systemidDtl == null || lookup.lookupcodeDtl == null) {
	 * alert('请先选择码表定义记录!', 1); return; }
	 */
	lookup.insertRowAtEnd(gid, {
		systemid : lookup.systemidDtl,
		lookupcode : lookup.lookupcodeDtl
	});
};