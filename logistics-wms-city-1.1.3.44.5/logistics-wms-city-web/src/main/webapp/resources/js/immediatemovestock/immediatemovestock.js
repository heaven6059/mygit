
var immediatemovestock = {};

//加载Grid数据Utils
immediatemovestock.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//关闭 新增/修改窗口
immediatemovestock.closeUI = function(opt){
	$('#'+opt).window('close');
};


//插入行开始编辑
immediatemovestock.insertRowAtEnd = function(gid,newData){
	var tempObj = $('#'+gid);
	for(var i = 0;i<newData.length;i++){
		var reqParams = {
				locno:immediatemovestock.user.locno,
				sCellNo:newData[i].sCellNo,
				sysNo:newData[i].sysNo,
				itemNo:newData[i].itemNo,
				itemName:newData[i].itemName,
				colorName:newData[i].colorName,
				sizeNo:newData[i].sizeNo,
				barcode:newData[i].barcode,
				goodContentQty:newData[i].goodContentQty,
				itemQty:newData[i].itemQty,
				creator:immediatemovestock.user.loginName
		};
		tempObj.datagrid('appendRow', reqParams);
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('selectRow',tempIndex);
	tempObj.datagrid('beginEdit', tempIndex);
};


//查询客户锁定库存信息
immediatemovestock.searchData = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/immediate_move_stock/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = immediatemovestock.user.locno;
	reqParam['outstockType'] = "5";
	immediatemovestock.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
};

//==================库存选择查询、清空、关闭====================
immediatemovestock.searchConContent = function(){
	  var year = $("#yearsCondition").val().trim();
	  var season = $("#seasonCondition").combobox("getValues");
	  //var cateCode = $("#cateThreeCondition").combobox("getValue").trim(); 
	  var gender = $("#genderCondition").combobox("getValues");
	  var sysNo = $("#sysNoSearch2").combobox("getValue").trim();
	
	if(year!=''||season!=''||gender!=''){
		if(sysNo ==''){
			alert("请先选择品牌库！");
			return;
		}
		
	}
	var fromObjStr=convertArray($('#selectContentSearchForm').serializeArray());
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = immediatemovestock.user.locno;
	var queryMxURL=BasePath+'/immediate_move_stock/findConContentGroupByPage.json';
	immediatemovestock.loadGridDataUtil('content_select_datagrid', queryMxURL, reqParam);
};
//储位查询
immediatemovestock.searchCell = function(){ 
	var fromObjStr=convertArray($('#selectCellSearchForm').serializeArray());
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = immediatemovestock.user.locno;
	var queryMxURL=BasePath+'/cm_defcell/list.json';
	immediatemovestock.loadGridDataUtil('cell_select_datagrid', queryMxURL, reqParam);
};

//清除查询条件
immediatemovestock.searchClear = function(id){
	$('#'+id).form("clear");
	$('#brandNo').combobox("loadData",[]);
};

//清除库存查询条件
immediatemovestock.searchConContentClear = function(id){
	$('#'+id).form("clear");
	$('#cellNo').combobox("loadData",[]);
	$('#stockNo').combobox("loadData",[]);
	$('#areaNo').combobox("loadData",[]);
	$('#brandNo2').combobox("loadData",[]);
	$('#seasonCondition').combobox("loadData",[]);
	$('#genderCondition').combobox("loadData",[]);
};
//清除库存查询条件
immediatemovestock.searchConContentSearchClear = function(){
	$('#selectContentSearchForm').form("clear");
	$('#areaNo').combobox("loadData",[]);
	$('#stockNo').combobox("loadData",[]);
	$('#cellNo').combobox("loadData",[]);	
	$('#brandNo2').combobox("loadData",[]);
	$('#seasonCondition').combobox("loadData",[]);
	$('#genderCondition').combobox("loadData",[]);
};

//加载明细
immediatemovestock.loadStoreLockDtl = function(locno,ownerNo,storelockNo){
	$('#moveStockDetail').datagrid({ 
				url:BasePath+'/immediate_move_stock/findStorelockGroupByPage.json',
				queryParams:{locno:locno,ownerNo:ownerNo,storelockNo:storelockNo},method:"post"});
};

//新增
immediatemovestock.showAddDialog = function(){
	$("#openUI").window('open');
	$("#dataForm").form("clear");
	$("#save_main_info").show();
	//immediatemovestock.setAe('disable');
	deleteAllGridCommon('moveStockDetail');
};

//禁用按钮
immediatemovestock.setAe = function(ea){
	$("#add_row").linkbutton(ea);
	$("#del_row").linkbutton(ea);
};

//打开明细窗口
immediatemovestock.openConContentSelect = function(){
	$('#content_select_dialog').window('open');
	$('#selectContentSearchForm').form('clear');
    $('#content_select_datagrid').datagrid('loadData', { total: 0, rows: [] }); 
};
//打开储位选择窗口
immediatemovestock.openBatchAddCell = function(){
	var rows = $("#moveStockDetail").datagrid('getRows');
	var selected = $("#moveStockDetail").datagrid('getChecked');
	if(rows.length < 1){
		alert("请先【新增明细】!");
		return;
	}
	if(selected.length < 1){
		alert("请选择需要批量添加目的储位的记录!");
		return;
	}
	$('#cell_select_dialog').window('open');
	$('#selectCellSearchForm').form('clear');
    $('#cell_select_datagrid').datagrid('loadData', { total: 0, rows: [] }); 
};

//双击列表显示明细
immediatemovestock.loadView = function(rowData){
	$("#openDtlUI").window('open');
	$("#dataViewForm").form("clear");
	$("#dataViewForm").form("load",rowData);
	
	var queryMxURL=BasePath+'/immediate_move_stock/findMoveStockGroupByPage.json';
	var reqParam = {locno:rowData.locno,outstockNo:rowData.outstockNo};
	immediatemovestock.loadGridDataUtil('moveStockDetailView', queryMxURL, reqParam);
};

//保存主表信息
immediatemovestock.saveMainInfo = function(){
	var tempObj = $('#moveStockDetail');
	var inserted = tempObj.datagrid('getRows');
	if(inserted.length < 1){
		alert("请添加要上架的数据",1);
		return;
	}
		
	// 验证
	var checkRows = tempObj.datagrid('getSelected');
	if (checkRows != null) {
		var rowIndex = tempObj.datagrid('getRowIndex', checkRows);
		tempObj.datagrid('endEdit', rowIndex);
	}

	// 验证数量可分配数
	var tipStr = "";
	var rows = tempObj.datagrid('getRows');
	$.each(rows, function(index, item) {
		if(item.dCellNo == "" || item.dCellNo == null){
			tipStr = "来源储位：" + item.sCellNo + "商品：" + item.itemNo + "尺码："
			+ item.sizeNo + "目的储位不能为空!";
			return;
		} 
		
		if($.trim(item.sCellNo) == $.trim(item.dCellNo)){
			tipStr = "商品：" + item.itemNo + "尺码：" + item.sizeNo + "来源目的储位不能相等!";
			return;
		} 
		
		if (item.itemQty == "0" || item.itemQty == "" || item.itemQty == null) {
			tipStr = "来源储位：" + item.sCellNo + "商品：" + item.itemNo + "尺码："
					+ item.sizeNo + "上架数量必须大于0且不能为空!";
			return;
		}
		
		if (item.itemQty < 0) {
			tipStr = "来源储位：" + item.sCellNo + "商品：" + item.itemNo + "尺码："
					+ item.sizeNo + "必须大于0!";
			return;
		}
		
		if (item.itemQty > item.goodContentQty) {
			tipStr = "来源储位：" + item.sCellNo + "商品：" + item.itemNo + "尺码："
					+ item.sizeNo + "上架数量不能大于可上架数量!";
			return;
		}
	});
	
	//验证提示
	if (tipStr != "") {
		alert(tipStr);
		return;
	}
	
	var url = BasePath+'/immediate_move_stock/saveMainInfo';
	var params = {
			locno:immediatemovestock.user.locno,
			creator:immediatemovestock.user.loginName,
			inserted:JSON.stringify(inserted)
	};
	wms_city_common.loading("show","正在保存明细......");
	$.post(url, params, function(data) {
		if(data.result == 'success'){
			alert('保存成功!');
			tempObj.datagrid('acceptChanges');
			immediatemovestock.closeUI('openUI');
			$("#mainDataGrid").datagrid('load');
		}else{
			alert(data.msg,1);
		}
		wms_city_common.loading();
	}, "JSON").error(function() {
    	alert('保存失败!',1);
    	wms_city_common.loading();
    });
};


//作废上架单
immediatemovestock.queryBill = function(){
	
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要确认的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要确认这"+checkedRows.length+"条数据", function (r){  
		
		if(r){
			var tipStr = "";
			var dataList = [];
			$.each(checkedRows, function(index, item){
				var reqParam = {locno:immediatemovestock.user.locno,outstockNo:item.outstockNo};
				dataList[dataList.length] = reqParam;
				if(item.status != '10'){
					tipStr = "单据:"+item.outstockNo+";";
					return;
				}
			});   
			
			if(tipStr!=null && tipStr!=""){
				tipStr = tipStr+"不是<建单>状态，不能确认！";
	    		alert(tipStr);
	    		return;
	    	}
			
			//提交
			var url = BasePath+ '/immediate_move_stock/queryBill';
			var effectRow = {datas:JSON.stringify(dataList)};
			wms_city_common.loading("show","正在确认......");
			ajaxRequest(url,effectRow,function(data){
				wms_city_common.loading();
	    		 if(data.result=='success'){
	    			 //4.删除成功,清空表单
	    			 $("#mainDataGrid").datagrid("load");
	    			 alert('确认成功!');
	    		 }else{
	    			 alert(data.msg,2);
	    		 }
			});
		}
		 
    });  
};


//单击编辑库存行
//immediatemovestock.contentEdit = function(rowIndex, rowData){
//	var curObj = $("#storeLockDetail");
//	curObj.datagrid('beginEdit', rowIndex);
//	var ed = curObj.datagrid('getEditor', {index:rowIndex,field:'itemQty'});
//	var storelockNo = rowData.storelockNo;
//	if(storelockNo!=null && storelockNo!=""){
//		$(ed.target).numberbox('disable');
//	}
//};

//选择库存
immediatemovestock.contentSelectOK = function(){
	var selectRows = $("#content_select_datagrid").datagrid('getChecked');
	var pRows = $("#moveStockDetail").datagrid('getRows');
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
			
			immediatemovestock.insertRowAtEnd('moveStockDetail',newRows);
			$("#content_select_dialog").window('close');
		}
	});
};
//选择储位
immediatemovestock.cellSelectOK = function(){
	var selectRows = $("#cell_select_datagrid").datagrid('getChecked');
	var pRows = $("#moveStockDetail").datagrid('getChecked');
	var pAllRows = $("#moveStockDetail").datagrid('getRows');
	var len = selectRows.length;
	if(len < 1){
		alert("请选择储位!");
		return;
	}else if(len > 1){
		alert("只能选择一个储位!");
		return;
	}
	var row = selectRows[0];
	var last4Check = null;
	$.messager.confirm("确认","您确定要添加这个储位吗？", function (r){  
		if (!r) {
			return;
		}else{
			for(var idx=0;idx<pRows.length;idx++){
				for(var j=0;j<pAllRows.length;j++){
					if(pRows[idx].itemNo == pAllRows[j].itemNo 
							&& pRows[idx].sizeNo == pAllRows[j].sizeNo
							&& pRows[idx].sCellNo == pAllRows[j].sCellNo){
						pAllRows[j].dCellNo = row.cellNo;
						break;
					}
				}
			}
			$("#moveStockDetail").datagrid({data:pAllRows});
			$("#cell_select_dialog").window('close');
		}
	});
};
//删除明细
immediatemovestock.delMoveStockDtl = function(){
	var tempObj = $("#moveStockDetail");
	var checkRows = tempObj.datagrid('getChecked');
	if(checkRows == null){
		alert("请选择要删除的明细!");
		return;
	}
	$.messager.confirm("确认","您确定要删除这"+checkRows.length+"条记录吗？", function (r){
		if(!r){
			return;
		}
		var rowIndex = 0;
		var tem;
		for(var idx=0;idx<checkRows.length;idx++){
			tem = checkRows[idx];
			rowIndex = tempObj.datagrid('getRowIndex', tem);
			tempObj.datagrid('deleteRow', rowIndex);
		}
	});
};

//导出报表
immediatemovestock.do_export = function(){
	exportExcelBaseInfo('mainDataGrid','/immediate_move_stock/do_export.htm','日库存变动报表导出');
};


//初始化用户信息
immediatemovestock.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		immediatemovestock.user = data;
	}); 
};

//仓区下拉框
//immediatemovestock.initWareNo = function(locno,wareNoId,areaNoId,stockNoId){
//	$.ajax({
//		async : false,
//		cache : false,
//		type : 'GET',
//		dataType : "json",
//		url:BasePath+'/cm_defware/get_biz?locno='+locno,
//		success : function(data) {
//			$('#'+wareNoId).combobox({
//			    data:data,
//			    valueField:'wareNo',    
//			    textField:'valueAndText',
//			    panelHeight:150,
//			    onSelect:function(){
//			    	immediatemovestock.initArea(locno,wareNoId,areaNoId,stockNoId);
//			    	},
//			    loadFilter:function(data){
//					if(data){
//			       		 for(var i = 0;i<data.length;i++){
//			       			 var tempData = data[i];
//			       			 tempData.valueAndText = tempData.wareNo+'→'+tempData.wareName;
//			       		 }
//			 		}
//					return data;
//			   }
//			});
//		}
//	});
//};

//库区
immediatemovestock.initArea = function(locno,wareNoId,areaNoId,stockNoId){
	var wareNo = $("#"+wareNoId).combobox("getValue");
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/cm_defarea/get_biz?locno='+locno+"&wareNo="+wareNo,
		success : function(data) {
			$('#'+areaNoId).combobox({
			    data:data,
			    valueField:'areaNo',    
			    textField:'valueAndText',
			    panelHeight:150,
			    onSelect:function(){immediatemovestock.initStock(locno,wareNoId,areaNoId,stockNoId);},
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

//通道
immediatemovestock.initStock = function(locno,wareNoId,areaNoId,stockNoId){
	var wareNo = $("#"+wareNoId).combobox("getValue");
	var areaNo = $("#"+areaNoId).combobox("getValue");
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/cm_defstock/get_biz?locno='+locno+"&wareNo="+wareNo+"&areaNo="+areaNo,
		success : function(data) {
			$('#'+stockNoId).combobox({
			    data:data,
			    valueField:'stockNo',    
			    textField:'valueAndText',
			    panelHeight:150,
			    onSelect:function(){immediatemovestock.initCell(locno,wareNo,areaNo);},
			    loadFilter:function(data){
					if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.stockNo+'→'+tempData.stockNo;
			       		 }
			 		}
					return data;
			   }
			});
		}
	});
};

//储位
immediatemovestock.initCell = function(locno,wareNo,areaNo){
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

//初始化状态
immediatemovestock.init_status_Text = {};
immediatemovestock.initStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IMMEDIATE_MOVE_STOCK',
			{},
			immediatemovestock.init_status_Text,
			null);
};

immediatemovestock.initUsers = function(){
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

$(document).ready(function(){
	$("#createTmStartCondition").datebox('setValue',getDateStr(-2));
	immediatemovestock.initCurrentUser();
	//immediatemovestock.initWareNo(immediatemovestock.user.locno,'wareNo','areaNo','stockNo');
	//初始化仓区
	wms_city_common.initWareNoForCascade(
			immediatemovestock.user.locno,
			['wareNo','wareNo4SelectCell'],
			['areaNo','areaNo4SelectCell'],
			['stockNo','stockNo4SelectCell'],
			['cellNo',null],
			[true,true],
			null
			);
	
	/*//初始化商品类别
	wms_city_common.comboboxLoadFilter(
			["cateCodeCondition"],
			'cateCode',
			'cateName',
			'valueAndText',
			false,
			[true],
			BasePath+'/category/getCategory4Simple?cateLevelid=1',
			{},
			null,
			null);*/
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneCondition',
			'cateTwoCondition',
			'cateThreeCondition',
			true
    );
	immediatemovestock.initStatus();
	
	immediatemovestock.initUsers();
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	var objs2 = [];
	objs2.push(
			{"sysNoObj":$('#sysNoSearch2'),"brandNoObj":$('input[id=brandNo2]',$('#selectContentSearchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs2);
	
	//总计
	$('#moveStockDetailView').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					immediatemovestock.itemQty = data.footer[1].itemQty;
					immediatemovestock.goodContentQty = data.footer[1].goodContentQty;
				} else {
					var rows = $('#moveStockDetailView').datagrid('getFooterRows');
					rows[1]['itemQty'] = immediatemovestock.itemQty;
					rows[1]['goodContentQty'] = immediatemovestock.goodContentQty;
					$('#moveStockDetailView').datagrid('reloadFooter');
				}
			}
			
		}
	});
	
	//总计
	$('#mainDataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					immediatemovestock.itemQtyMain = data.footer[1].itemQty;
				} else {
					var rows = $('#mainDataGrid').datagrid('getFooterRows');
					rows[1]['itemQty'] = immediatemovestock.itemQtyMain;
					$('#mainDataGrid').datagrid('reloadFooter');
				}
			}
			
		}
	});
	//初始化季节、性别
	immediatemovestock.initGenderAndSeason('#sysNoSearch2');
});
immediatemovestock.initGenderAndSeason =  function(sysNoId){
	$(sysNoId).combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderCondition',true,false);
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonCondition',true,false);
		}
	});
};