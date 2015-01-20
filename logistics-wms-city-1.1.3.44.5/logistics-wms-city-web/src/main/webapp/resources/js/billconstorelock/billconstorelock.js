
var billconstorelock = {};

billconstorelock.mainDataGridFooter = {};

billconstorelock.downloadTemp = function(){
	window.open(BasePath + "/bill_con_storelock_dtl/downloadTemple");
};

billconstorelock.importConToItem = function(){
//	$('#importDialogView').window({title:"箱明细初始化"});
//	$("#importDialogView").window('open'); 
	var fromObj = $("#showDialog");
	$("#iframe").attr("src",BasePath + "/bill_con_storelock_dtl/iframe?v="+new Date());
	$("#showImportDialog").window('open'); 
};


billconstorelock.loading = function(type,msg){
	wms_city_common.loading(type,msg);
};

billconstorelock.statusData = [{    
    "label":"0",
    "text":"0→客户锁定"
},{    
    "label":"1",    
    "text":"1→库存属性锁定"
}];

//加载Grid数据Utils
billconstorelock.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//关闭 新增/修改窗口
billconstorelock.closeUI = function(opt){
	$('#'+opt).window('close');
};

//billconstorelock.insertRowAtEnd = function(gid,rowData){
//	var tempObj = $('#'+gid);
//	var data = tempObj.datagrid('getData');
//	var len = data.rows.length;
//	var total = 0;
//	var newData = [];
//	if(rowData){
//		if(len>0){
//			total = len + rowData.length;
//			newData = data.rows;
//			for(var i=len;i<total;i++){
//				newData[i] = rowData[i-len];
//			}
//		}else{
//			newData = rowData;
//		}
//		tempObj.datagrid('insertRow', data.rows);
//	}else{
//		
//	}
//	var tempIndex = tempObj.datagrid('getRows').length - 1;
//	tempObj.datagrid('selectRow',tempIndex);
//	tempObj.datagrid('beginEdit', tempIndex);
//	
//};


//插入行开始编辑
billconstorelock.insertRowAtEnd = function(gid,newData){
	var tempObj = $('#'+gid);
	for(var i = 0;i<newData.length;i++){
		var reqParams = {
				rowId:newData[i].rowId,
				locno:billconstorelock.user.locno,
				cellNo:newData[i].cellNo,
				itemNo:newData[i].itemNo,
				itemName:newData[i].itemName,
				colorName:newData[i].colorName,
				sizeNo:newData[i].sizeNo,
				barcode:newData[i].barcode,
				goodContentQty:newData[i].goodContentQty,
				itemQty:newData[i].itemQty
		};
		tempObj.datagrid('appendRow', reqParams);
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('selectRow',tempIndex);
	tempObj.datagrid('beginEdit', tempIndex);
};


//查询客户锁定库存信息
billconstorelock.searchData = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_con_storelock/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billconstorelock.user.locno;
	billconstorelock.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
};

//==================库存选择查询、清空、关闭====================
billconstorelock.searchConContent = function(){
	var storelockType = $('#storelockType').combobox('getValue');
	var fromObjStr=convertArray($('#selectContentSearchForm').serializeArray());
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billconstorelock.user.locno;
	if(storelockType == "1"){
		var itemType = "0";
		var sourceType = $('#sourceType').combobox('getValue');
		if(sourceType=="11"||sourceType=="12"){
			itemType = "9";
		}
		reqParam['itemType'] = itemType;
		reqParam['sourceType'] = sourceType;
	}else{
		reqParam['itemType'] = "";
		reqParam['sourceType'] = "";
	}
	var queryMxURL=BasePath+'/bill_con_storelock_dtl/findConContentGroupByPage.json';
	billconstorelock.loadGridDataUtil('content_select_datagrid', queryMxURL, reqParam);
};


//清除查询条件
billconstorelock.searchClear = function(id){
	$('#'+id).form("clear");
};

//清除库存查询条件
billconstorelock.searchConContentClear = function(id){
	$('#'+id).form("clear");
	//$('#cellNo').combobox("loadData",[]);
	$('#seasonItem').combobox("loadData",[]);
	$('#genderItem').combobox("loadData",[]);
};

//加载明细
billconstorelock.loadStoreLockDtl = function(locno,ownerNo,storelockNo){
	$('#storeLockDetail').datagrid({ 
				url:BasePath+'/bill_con_storelock_dtl/findStorelockGroupByPage.json',
				queryParams:{locno:locno,ownerNo:ownerNo,storelockNo:storelockNo},method:"post"});
};

//新增
billconstorelock.showAddDialog = function(){
	$("#openUI").window('open');
	$("#dataForm").form("clear");
	$("#ownerNo").combobox('enable');
	$("#storeNo").combobox('enable');
	$("#storelockType").combobox('enable');
	$("#sourceType").combobox('enable');
	$("#save_main_info").show();
	billconstorelock.setAe('disable');
	deleteAllGridCommon('storeLockDetail');
	$('#storelockType').combobox('select',billconstorelock.statusData[0].label);
	//$('#storeNo').combogrid("grid").datagrid({url:BasePath+'/store/list.json?storeType=11'});
	$('#storeNo').combogrid("grid").datagrid({ url:BasePath+'/store/list.json?storeType=11',queryParams:{storeType:"11"},method:"post"});
};

//禁用按钮
billconstorelock.setAe = function(ea){
	$("#add_row").linkbutton(ea);
	$("#del_row").linkbutton(ea);
	$("#save_row").linkbutton(ea);
	$("#import_row").linkbutton(ea);
	
	
};

//打开明细窗口
billconstorelock.openConContentSelect = function(){
	$('#content_select_dialog').window('open');
	$('#selectContentSearchForm').form('clear');
    $('#content_select_datagrid').datagrid('loadData', { total: 0, rows: [] }); 
	//deleteAllGridCommon('content_select_datagrid');
};

//打开修改界面
billconstorelock.editInfo = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请至少选择一条记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		var data = checkedRows[0];
		if(data.status != '10'){
			alert(data.storelockNo+"只能修改<新建>状态的单据!",1);
			return;
		}
		billconstorelock.loadUpdateDtl(data);
	}
	$("#openUI").window('open');
};

//加载修改的明细
billconstorelock.loadUpdateDtl = function(rowData){
	$("#save_main_info").hide();
	$("#dataForm").form("clear");
	if(rowData.storelockType == "0"){
		$("#cusTitle").html("客户：");
		$("#lbStore").show();
		$("#lbSourceType").hide();
		billconstorelock.loadStoreName('#storeNo',rowData.storeNo);
	}else{
		$("#cusTitle").html("库存属性：");
		$("#lbStore").hide();
		$("#lbSourceType").show();
	}
	$("#dataForm").form("load",rowData);
	$("#ownerNo").combobox('disable');
	$("#storeNo").combobox('disable');
	$("#storelockType").combobox('disable');
	$("#sourceType").combobox('disable');
	billconstorelock.setAe('enable');
	
	var queryMxURL=BasePath+'/bill_con_storelock_dtl/findStorelockGroupByPage.json';
	var reqParam = {locno:rowData.locno,ownerNo:rowData.ownerNo,storelockNo:rowData.storelockNo};
	billconstorelock.loadGridDataUtil('storeLockDetail', queryMxURL, reqParam);
};


//双击列表显示明细
billconstorelock.loadView = function(rowData){
	$("#openDtlUI").window('open');
	$("#dataViewForm").form("clear");
	
	if(rowData.storelockType == "0"){
		$("#cusTitleView").html("客户：");
		$("#lbStoreView").show();
		$("#lbSourceTypeView").hide();
		billconstorelock.loadStoreName('#storeNoView',rowData.storeNo);
	}else{
		$("#cusTitleView").html("库存属性：");
		$("#lbStoreView").hide();
		$("#lbSourceTypeView").show();
	}
	
	$("#dataViewForm").form("load",rowData);
	$("#ownerNoView").combobox('disable');
	$("#storeNoView").combobox('disable');
	$("#sourceTypeView").combobox('disable');
	$("#storelockTypeView").combobox('disable');
	
	//显示转退厂申请按钮
	if(rowData.businessType=="0" && rowData.storelockType=="1"){
		$("#btn-towmrequest").show();
	}else{
		$("#btn-towmrequest").hide();
	}
	
	var queryMxURL=BasePath+'/bill_con_storelock_dtl/findStorelockGroupByPage.json';
	var reqParam = {locno:rowData.locno,ownerNo:rowData.ownerNo,storelockNo:rowData.storelockNo};
	billconstorelock.loadGridDataUtil('storeLockDetailView', queryMxURL, reqParam);
};

//保存主表信息
billconstorelock.saveMainInfo = function(){
	var url = BasePath+ '/bill_con_storelock/saveMainInfo';
	  if($('#dataForm').form('validate')){
		  
		//验证选择
		var storelockType = $('#storelockType').combobox('getValue');
		if(storelockType == "0"){
			var storeNo = $('#storeNo').combogrid('getValue');
			if(storeNo == "" || storeNo == null){
				alert("请选择客户!");
				return;
			}
		}else{
			var sourceType = $('#sourceType').combobox('getValue');
			if(sourceType == "" || sourceType == null){
				alert("请选择库存属性类型!");
				return;
			}
		}
		  
		  $('#dataForm').form('submit', {
				url: url,
				onSubmit: function(param){
					param.locno = billconstorelock.user.locno;
					param.creator = billconstorelock.user.loginName;
				},
				success: function(r){
					var data = eval("(" +r+ ")");
					if(data.result=="success"){
						$("#storelockNo").val(data.storelockNo);
						$("#save_main_info").hide();
						$("#detailDiv").show();
						$("#ownerNo").combobox('disable');
						$("#storeNo").combobox('disable');
						$("#storelockType").combobox('disable');
						$("#sourceType").combobox('disable');
						billconstorelock.setAe('enable');
						$('#storeLockDetail').datagrid('clearData');
						alert('保存成功!');
						return;
					}else{
						alert(data.msg);
						return;
					}
			    },
				error:function(){
					alert('保存失败,请联系管理员!',2);
					return;
				}
		   });
	  }
};

//保存明细
billconstorelock.saveStorelockDtl = function(){
	var tempObj = $('#storeLockDetail');
	var inserted = tempObj.datagrid('getChanges', "inserted");
	if(inserted.length < 1){
		alert("没有改动数据",1);
		//billconstorelock.closeUI('openUI');
		return;
	}
	
	//验证
	var checkRows = tempObj.datagrid('getSelected');
	if(checkRows != null){
		var rowIndex = tempObj.datagrid('getRowIndex', checkRows);
        tempObj.datagrid('endEdit', rowIndex);
	}
	
	//验证数量可分配数
	var tipStr = "";
	var rows = tempObj.datagrid('getRows'); 
	$.each(rows, function(index, item){
		if(item.rowId == "0"){
			if(item.itemQty == "0" || item.itemQty == "" || item.itemQty == null){
				tipStr =  "储位："+item.cellNo+"商品："+item.itemNo + "尺码："+item.sizeNo+"分配数量必须大于0且不能为空!";
				return;
			}
			if(item.itemQty < 1){
				tipStr =  "储位："+item.cellNo+"商品："+item.itemNo + "尺码："+item.sizeNo+"分配数量必须大于0!";
				return;
			}
			if(item.itemQty > item.goodContentQty){
				tipStr =  "储位："+item.cellNo+"商品："+item.itemNo + "尺码："+item.sizeNo+"分配数量不能大于可分配数量!";
				return;
			}
		}
	});
	
	if(tipStr!=""){
		alert(tipStr);
		return;
	}
	
	var url = BasePath+'/bill_con_storelock_dtl/saveStorelockDtl';
	var params = {
			locno:billconstorelock.user.locno,
			ownerNo:$("#ownerNo").combobox('getValue'),
			storelockNo:$("#storelockNo").val(),
			creator:billconstorelock.user.loginName,
			creatorName:billconstorelock.user.username,
			editor:billconstorelock.user.loginName,
			editorName:billconstorelock.user.username,
			inserted:JSON.stringify(inserted)
	};
	wms_city_common.loading("show","正在保存明细......");
	$.post(url, params, function(data) {
		if(data.result == 'success'){
			alert('保存成功!');
			tempObj.datagrid('acceptChanges');
			//billconstorelock.closeUI('openUI');
			//tempObj.datagrid('reload');
			billconstorelock.loadStoreLockDtl(billconstorelock.user.locno,$("#ownerNo").combobox('getValue'),$("#storelockNo").val());
			$("#mainDataGrid").datagrid('load');
		}else{
			alert(data.msg,1);
		}
		wms_city_common.loading();
	}, "JSON").error(function() {
    	alert('删除失败!',1);
    	wms_city_common.loading();
    });
};

//审核
billconstorelock.auditStorelock = function(){
	var tempObj = $("#mainDataGrid");
	var dataCheck = tempObj.datagrid('getChecked');
	if(dataCheck.length < 1){
		alert("请至少选择一条记录!");
		return;
	}
	
	var tipStr = "";
	var dataList = [];
	$.each(dataCheck, function(index, item){
		var reqP = {
			locno : item.locno,
			ownerNo : item.ownerNo,
			storelockNo : item.storelockNo,
			auditor : billconstorelock.user.loginName,
			auditorName : billconstorelock.user.username
		};
		dataList[dataList.length] = reqP;
		if(item.status != '10'){
			tipStr = item.storelockNo + "只能审核<建单>状态的数据!!";
			return;
		}
	});
	
	if(tipStr!=""){
		alert(tipStr);
		return;
	}
	
	//确认是否审核
	$.messager.confirm('确认', '您确认要审核选中的'+dataCheck.length+'条记录吗？', function(r) {
		if (!r) {
			return;
		}
		wms_city_common.loading("show", "正在审核......");
		var url = BasePath+'/bill_con_storelock/auditStorelock';
		var params = {
				datas:JSON.stringify(dataList)
		};
		$.post(url, params, function(data) {
			if (data.result == 'success') {
				alert('审核成功!');
				$("#mainDataGrid").datagrid('load');
			} else {
				alert(data.msg, 1);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
			alert('审核失败!', 1);
			wms_city_common.loading();
		});
	});
};


//手工关闭
billconstorelock.overStoreLock = function(){
	var tempObj = $("#mainDataGrid");
	var checkRows = tempObj.datagrid('getChecked');
	if(checkRows.length < 1){
		alert("请至少选择一条记录!");
		return;
	}
	
	var tipStr = "";
	var dataList = [];
	$.each(checkRows, function(index, item){
		dataList[dataList.length] = {locno:item.locno,ownerNo:item.ownerNo,storelockNo:item.storelockNo};
		if(item.status != '11'){
			tipStr = item.storelockNo + "只能关闭<审核>状态的数据!!";
			return;
		}
	});
	
	if(tipStr!=""){
		alert(tipStr);
		return;
	}
	
	
	//确认是否删除
	$.messager.confirm('确认', '您确认要关闭所选的'+checkRows.length+'条记录吗？', function(r) {
		if (!r) {
			return;
		}
		wms_city_common.loading("show", "正在处理......");
		var url = BasePath+'/bill_con_storelock/overStoreLock';
		var params = {
			datas:JSON.stringify(dataList)
		};
		$.post(url, params, function(data) {
			if (data.result == 'success') {
				alert('关闭成功!');
				$("#mainDataGrid").datagrid('load');
			} else {
				alert(data.msg, 1);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
			alert('关闭失败!', 1);
			wms_city_common.loading();
		});
	});
};

//删除主表
billconstorelock.delStorelock = function(){
	var tempObj = $("#mainDataGrid");
	var checkRows = tempObj.datagrid('getChecked');
	if(checkRows.length < 1){
		alert("请至少选择一条记录!");
		return;
	}
	
	var tipStr = "";
	var dataList = [];
	$.each(checkRows, function(index, item){
		dataList[dataList.length] = {
			locno : item.locno,
			ownerNo : item.ownerNo,
			storelockNo : item.storelockNo,
			businessType : item.businessType,
			sourceNo : item.sourceNo,
			sourceType : item.sourceType
		};
		if(item.status != '10'){
			tipStr = item.storelockNo + "只能删除<建单>状态的数据!";
			return;
		}
	});
	
	if(tipStr!=""){
		alert(tipStr);
		return;
	}
	
	//确认是否删除
	$.messager.confirm('确认', '您确认想要删除'+checkRows.length+'记录吗？', function(r) {
		if (!r) {
			return;
		}
		wms_city_common.loading("show", "正在删除......");
		var url = BasePath+'/bill_con_storelock/delStorelock';
		var params = {
			deleted:JSON.stringify(dataList)
		};
		$.post(url, params, function(data) {
			if (data.result == 'success') {
				alert('删除成功!');
				$("#mainDataGrid").datagrid('load');
			} else {
				alert(data.msg, 1);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
			alert('删除失败!', 1);
			wms_city_common.loading();
		});
	});
};


//删除明细
billconstorelock.delStorelockDtl = function(){
	var tempObj = $("#storeLockDetail");
	var checkRows = tempObj.datagrid('getSelected');
	if(checkRows == null){
		alert("请选择要删除的明细!");
		return;
	}
	
	//var delList = tempObj.datagrid('getChanges', "deleted");
	//确认是否删除
	$.messager.confirm('确认', '您确认想要删除记录吗？', function(r) {
		if (!r) {
			return;
		}
		if (checkRows) {
			var rowData = checkRows;
			if(rowData.storelockNo !=""&&rowData.storelockNo!=null&&typeof(rowData.storelockNo) != "undefined"){
				wms_city_common.loading("show", "正在删除......");
				var dataList = [];
				dataList[dataList.length] = {
						locno:rowData.locno,
						ownerNo:rowData.ownerNo,
						storelockNo:rowData.storelockNo,
						cellNo:rowData.cellNo,
						itemNo:rowData.itemNo,
						sizeNo:rowData.sizeNo,
						barcode:rowData.barcode
				};
				var url = BasePath+'/bill_con_storelock_dtl/delStorelockDtl';
				var params = {
					deleted:JSON.stringify(dataList),
					"locno":rowData.locno,
					"ownerNo":rowData.ownerNo,
					"storelockNo":rowData.storelockNo
				};
				
				$.post(url, params, function(data) {
					if (data.result == 'success') {
						alert('删除成功!');
						$("#storeLockDetail").datagrid('load');
					} else {
						alert(data.msg, 1);
					}
					wms_city_common.loading();
				}, "JSON").error(function() {
					alert('保存失败!', 1);
					wms_city_common.loading();
				});
			}else{
				var rowIndex = tempObj.datagrid('getRowIndex', checkRows);
		        tempObj.datagrid('deleteRow', rowIndex);
			}
	    }
		
	});
};

//转退厂申请
billconstorelock.toWmRequest = function(){
	var storelockNo = $("#storelockNoView").val();
	var ownerNo = $("#ownerNoView").combobox('getValue');
	// 确认是否转退厂申请
	$.messager.confirm('确认', '您确认要对'+storelockNo+'的单据转退厂申请吗？', function(r) {
		if (!r) {
			return;
		}
		wms_city_common.loading("show", "正在转退厂申请......");
		var url = BasePath + '/bill_con_storelock/toWmRequest';
		var params = {
			locno : billconstorelock.user.locno,
			storelockNo : storelockNo,
			ownerNo : ownerNo,
			creator : billconstorelock.user.loginName
		};
		$.post(url, params, function(data) {
			if (data.result == 'success') {
				alert('转退厂申请成功!');
				$("#mainDataGrid").datagrid('load');
			} else {
				alert(data.msg, 1);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
			alert('转退厂申请失败!', 1);
			wms_city_common.loading();
		});
	});
};

//单击编辑库存行
billconstorelock.contentEdit = function(rowIndex, rowData){
	var curObj = $("#storeLockDetail");
	curObj.datagrid('beginEdit', rowIndex);
	var ed = curObj.datagrid('getEditor', {index:rowIndex,field:'itemQty'});
	var storelockNo = rowData.storelockNo;
	if(storelockNo!=null && storelockNo!=""){
		$(ed.target).numberbox('disable');
	}
};

//选择库存
billconstorelock.contentSelectOK = function(){
	var selectRows = $("#content_select_datagrid").datagrid('getChecked');
	var pRows = $("#storeLockDetail").datagrid('getRows');
	var newRows = [];
	var len = selectRows.length;
	if(len < 1){
		alert("请选择商品!");
		return;
	}
	$.messager.confirm("确认","您确定要添加这"+len+"个库存商品信息吗？", function (r){  
		if (!r) {
			return;
		}else{
			for(var i=0;i<selectRows.length;i++){
				selectRows[i].rowId = 0;
			}
			var exisit = false;
			var index = 0;
			for(var i=0;i<selectRows.length;i++){
				exisit = false;
				for(var j=0;j<pRows.length;j++){
					if(selectRows[i].itemNo == pRows[j].itemNo 
							&& selectRows[i].sizeNo == pRows[j].sizeNo
							&& selectRows[i].cellNo == pRows[j].cellNo){
						exisit = true;
						break;
					}
				}
				if(!exisit){
					newRows[index] = selectRows[i];
					index++;
				}
			}
			
			billconstorelock.insertRowAtEnd('storeLockDetail',newRows);
			$("#content_select_dialog").window('close');
		}
	});
};

//到处报表
billconstorelock.do_export = function(){
	exportExcelBaseInfo('dataGridJG','/bill_con_storelock/do_export.htm','日库存变动报表导出');
};

//加载客户
billconstorelock.loadStoreName = function(inputId,storeNo){
	var url = BasePath+'/store/get_biz';
	ajaxRequestAsync(url,{storeNo:storeNo},function(data){
		if(data != null){
			var columns = {storeNo:data[0].storeNo};
			$(inputId).combogrid("grid").datagrid("load", columns);
		}
	}); 
};

//初始化用户信息
billconstorelock.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		billconstorelock.user = data;
	}); 
};

//库区
billconstorelock.initArea = function(locno){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/cm_defarea/get_biz?locno='+locno,
		success : function(data) {
			$('#areaNo').combobox({
			    data:data,
			    valueField:'areaNo',    
			    textField:'valueAndText',
			    panelHeight:150,
			    onSelect:function(rec){billconstorelock.initCell(locno,rec.wareNo,rec.areaNo);},
			    loadFilter:function(data){
					if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.areaNo+'→'+tempData.areaName;
			       		 }
			 		}
					return data;
			   }
			});
		}
	});
};

//储位
billconstorelock.initCell = function(locno,wareNo,areaNo){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/cm_defcell/get_biz?locno='+locno+"&wareNo="+wareNo+"&areaNo="+areaNo,
		success : function(data) {
			$('#cellNo').combobox({
			    data:data,
			    valueField:'cellNo',    
			    textField:'cellNo',
			    panelHeight:150
			});
		}
	});
};


//初始化客户信息
billconstorelock.initStoreNo = function(inputId){
	$('#storeNoCondition,#storeNo,#storeNoView').combogrid({
		 panelWidth:400,   
         idField:'storeNo',  
         textField:'textFieldName',   
         pagination : true,
         rownumbers:true,
         mode: 'remote',
         url:BasePath+'/store/list.json?storeType=11',   
         columns:[[  
			{field:'storeNo',title:'客户编码',width:110, align:'left'},  
			{field:'storeName',title:'客户名称',width:243, align:'left'}  
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

//初始化状态
billconstorelock.initStatus = function(){
	$('#statusCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_BILL_CON_STOCKLOCK_STATUS',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
	});
};

//初始化类型
billconstorelock.initSourceType = function(){
	$('#sourceType,#sourceTypeView,#sourceTypeCondition').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECEDE_TYPE',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	});
};

//业务类型
billconstorelock.initBusinessType = function(){
	$('#businessType').combobox({
		 url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_WM_STOCKLOCK_BUSINESS_TYPE',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	});
};


//初始化委托业主代码
billconstorelock.initOwnerNo = function(){
	$.ajax({
		type : 'POST',
		dataType : "json",
		url:BasePath+'/entrust_owner/get_biz',
		success : function(datas) {
			$('#ownerNo,#ownerNoCondition,#ownerNoView').combobox({
			    data:datas,
			    valueField:'ownerNo',    
			    textField:'textFieldName',
			    panelHeight:200,
			    loadFilter:function(data){
			       	 if(data.length>0){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.textFieldName = tempData.ownerNo+'→'+tempData.ownerName;
			       		 }
			       	 }
			    	 return data;
			    }
			});
		}
	});
};

//初始化类型
billconstorelock.initStorelockType = function(){
	$('#storelockType,#storelockTypeView,#storelockTypeCondition').combobox({
	    data:billconstorelock.statusData,
	    valueField:'label',    
	    textField:'text',
	    panelHeight:"auto",
	    onSelect:function(record){
	    	if(record.label == "0"){
	    		$("#cusTitle").html("客户：");
	    		$("#lbStore").show();
	    		$("#lbSourceType").hide();
	    	}else{
	    		$("#cusTitle").html("库存属性：");
	    		$("#lbStore").hide();
	    		$("#lbSourceType").show();
	    	}
	    }
	});
};


$(document).ready(function(){
	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
	billconstorelock.initCurrentUser();
	//billconstorelock.initArea(billconstorelock.user.locno);
	billconstorelock.initStoreNo();
	billconstorelock.initStatus();
	billconstorelock.initOwnerNo();
	billconstorelock.initSourceType();
	billconstorelock.initBusinessType();
	billconstorelock.initStorelockType();
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNo]',$('#searchForm'))},
			{"sysNoObj":$('#sysNo'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	var objsItem = [];
	objsItem.push({"sysNoObj":$('#sysNoId'),"brandNoObj":$('input[id=brandNoItem]',$('#selectContentSearchForm'))});
	wms_city_common.loadSysNo4Cascade(objsItem);
	//总计
	$('#storeLockDetailView').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					billconstorelock.itemQty = data.footer[1].itemQty;
					billconstorelock.goodContentQty = data.footer[1].goodContentQty;
				} else {
					var rows = $('#storeLockDetailView').datagrid('getFooterRows');
					rows[1]['itemQty'] = billconstorelock.itemQty;
					rows[1]['goodContentQty'] = billconstorelock.goodContentQty;
					$('#storeLockDetailView').datagrid('reloadFooter');
				}
			}
			
		}
	});
	//冻结单合计
	$('#mainDataGrid').datagrid({
		'onLoadSuccess':function(data){
   			if(data.footer[1].isselectsum){
   				billconstorelock.mainDataGridFooter = data.footer[1];
   			}else{
   				var rows = $('#mainDataGrid').datagrid('getFooterRows');
	   			rows[1]= billconstorelock.mainDataGridFooter;
	   			$('#mainDataGrid').datagrid('reloadFooter');
   			}
   		}
	});
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneCondition',
			'cateTwoCondition',
			'cateThreeCondition',
			true
		);
	
	//wms_city_common.loadSysNo('#sysNoId');
	//初始化季节、性别
	billconstorelock.initGenderAndSeason('#sysNoId');
	
	$("#showImportDialog").window({
		onBeforeClose:function(){
			var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
			var data = checkedRows[0];
			billconstorelock.loadUpdateDtl(data);
		}
	});
});
billconstorelock.initGenderAndSeason =  function(sysNoId){
	$(sysNoId).combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderItem',true,false);
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonItem',true,false);
		}
	});
};
billconstorelock.columnsJsonList = [[
							 		{title:'储位',field:'cellNo',width:160,align:'right'},
							 		{title:'商品编码',field:'itemNo',width:160,align:'right'},
							 		{title:'商品名称',field:'itemName',width:220,align:'right'},
							 		{title:'颜色',field:'colorName',width:160,align:'right'},
							 		{title:'尺码',field:'sizeNo',width:160,align:'right'},
							 		{title:'条码',field:'barcode',width:160,align:'right'},
							 		{title:'分配数量',field:'itemQty',width:140,align:'right'}
							 	  ]];
//导出明细
billconstorelock.exportStorelockDtl = function(){
    var url=BasePath+'/bill_con_storelock_dtl/doExport';
    
	var dataGridId = "storeLockDetailView";
	var $dg = $("#"+dataGridId+"");
	var excelTitle = "库存冻结";
	var params = $dg.datagrid('options').queryParams;
	var exportColumns = JSON.stringify(billconstorelock.columnsJsonList);
	
	var tempTotal = -1;
	var paginationObj = $dg.datagrid('getPager').data("pagination");
    if(paginationObj){
    	tempTotal = paginationObj.options.total; 
        if(tempTotal>10000){
        	alert('导出文件数据超出限制最大数量10000!',1);
        	return;
        }
    }
	
	$("#exportExcelForm").remove();
	
	$("<form id='exportExcelForm'  method='post'></form>").appendTo("body"); ;
	
	var fromObj=$('#exportExcelForm');

	var dataRow=$dg.datagrid('getRows');
	if(dataRow.length>0){
	    fromObj.form('submit', {
			url: url,
			onSubmit: function(param){
				param.exportColumns = exportColumns;
				param.fileName=excelTitle;
				param.ownerName = $('#ownerNoView').combo('getText');
				param.storelockTypeName = $('#storelockTypeView').combo('getText');
				param.sourceTypeText = $('#storeNoView').combo('getText');
				if (params != null && params != {}) {
					$.each(params, function(i) {
						param[i] = params[i];
					});
				}
			},
			success: function(){
				
		    }
	   });
	}else{
		alert('数据为空，不能导出!',1);
	}
};
