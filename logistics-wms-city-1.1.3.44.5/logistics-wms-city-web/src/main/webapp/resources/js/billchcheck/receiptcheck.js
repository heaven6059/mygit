var receiptcheck = {};
receiptcheck.locno;
receiptcheck.planTypeData;
receiptcheck.searchDirectParams ={};
//页面加载
$(document).ready(function(){
	//创建日期初始为前两天
	$("#createtm_start").datebox('setValue',getDateStr(-2));
	receiptcheck.initLocData();
	var objs2 = [];
	objs2.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#receiptCheckForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs2);
	//初始化状态
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_BILL_CH_CHECK_STATUS',
			{},
			receiptcheck.statusData,
			null);
	//初始化员工
	wms_city_common.comboboxLoadFilter(
			["recheckWorker"],
			'workerNo',
			'workerName',
			'valueAndText',
			false,
			[true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
	$('#checkDataGridDetail').datagrid(
			{
				'onLoadSuccess':function(){
					var curObj = $('#checkDataGridDetail');
		   			var rows = curObj.datagrid("getRows");
		   			var length = rows.length;
		   			if(length==0){
		   				return;
		   			}
		   			//curObj.datagrid('beginEdit', 0);
		   		}
			});
	$('#checkDataGridDetail_view').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				receiptcheck.footer = data.footer[1];
		   			}else{
		   				var rows = $('#checkDataGridDetail_view').datagrid('getFooterRows');
			   			rows[1] = receiptcheck.footer;
			   			$('#checkDataGridDetail_view').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	$('#checkDataGridDetail_view_dif').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				receiptcheck.footer_dif = data.footer[1];
		   			}else{
		   				var rows = $('#checkDataGridDetail_view_dif').datagrid('getFooterRows');
		   				rows[1] = receiptcheck.footer_dif;
			   			$('#checkDataGridDetail_view_dif').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
});
receiptcheck.mainSumFoot = {};
receiptcheck.onLoadSuccess = function(data){
	if(data.footer[1] != null){
		receiptcheck.mainSumFoot = data.footer[1];
		}else{
			data.footer[1] = receiptcheck.mainSumFoot;
			$('#checkDataGrid').datagrid('reloadFooter');
		}
}
//用户信息
receiptcheck.user = {};
//仓库
receiptcheck.initLocData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			receiptcheck.locno = data.locno;
			receiptcheck.user = data;
		}
	});
};
receiptcheck.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//状态
receiptcheck.statusData = {};
receiptcheck.statusFormatter = function(value,rowData,rowIndex){
	return receiptcheck.statusData[value];
};

receiptcheck.checkTypeFormatter = function(value,rowData,rowIndex){
	if(value=="1"){
		return "初盘";
	}else if(value=="2"){
		return "复盘";
	}else{
		return "";
	}
};

//格式化
receiptcheck.planTypeFormatter = function(value,rowData,rowIndex){
	return receiptcheck.planTypeData[value];
};

receiptcheck.difQtyFormatter = function(value,rowData,rowIndex){
	return rowData.checkQty-rowData.itemQty;
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

receiptcheck.searchClear = function(){
	$('#receiptCheckForm').form("clear");
};

receiptcheck.searchItemClear = function(){
	$('#itemSearchForm').form("clear");
	//$('#cellNo').combo("clear");
};


receiptcheck.searchArea = function(){
	var createtm_start =  $('#createtm_start').datebox('getValue');
	var createtm_end =  $('#createtm_end').datebox('getValue');
	var planDateStart =  $('#startPlanDateCondition').datebox('getValue');
	var planDatemEnd =  $('#endPlanDateCondition').datebox('getValue');
	if(!isStartEndDate(createtm_start,createtm_end)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	if(!isStartEndDate(planDateStart,planDatemEnd)){    
		alert("盘点日期开始日期不能大于结束日期");   
		return;   
	} 
	var fromObjStr=convertArray($('#receiptCheckForm').serializeArray());
	var queryMxURL = BasePath+'/bill_ch_check/selectChCheck?locno='+receiptcheck.locno;
	var params = eval("(" +fromObjStr+ ")");
	params.orderByField = 'b.CREATETM';
	params.orderBy = 'desc';
	var obj = "checkDataGrid";
    $( "#"+obj).datagrid( 'options' ).queryParams= params;
    $( "#"+obj).datagrid( 'options' ).url=queryMxURL;
    $( "#"+obj).datagrid( 'load' );
};

function convertArray(o) { 
	var v = {};
	for ( var i in o) {
		if (typeof (v[o[i].name]) == 'undefined')
			v[o[i].name] = o[i].value;
		else
			v[o[i].name] += "," + o[i].value;
	}
	return JSON.stringify(v);
};

receiptcheck.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//初盘发单明细
receiptcheck.checkDtl = function(){
	$("#edit_add").show();
	var checkedRows = $("#checkDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能选择一条记录!',1);
		return;
	}
	if(checkedRows[0].status!='20'){
		alert("非已发单状态的单据不能修改",1);
		return;
	}
	rowData = checkedRows[0];
	$('#checkDataGridDetail').datagrid( 'options' ).url=BasePath+'/bill_ch_check_dtl/edit_list.json?locno='+rowData.locno+'&checkNo='+rowData.checkNo+'&itemNoIsNot=N&isNullBox=N';
	$('#checkDataGridDetail').datagrid( 'load' );
	
	$('#checkDataGridBoxDetail').datagrid( 'options' ).url=BasePath+'/bill_ch_check_dtl/edit_list.json?locno='+rowData.locno+'&checkNo='+rowData.checkNo+'&itemNoIsNot=N&isNullBox=Y';
	$('#checkDataGridBoxDetail').datagrid( 'load' );
	
	$("#checkDetailDialog").window('open'); 
	$("#checkDetailForm").form("load",rowData);
};

receiptcheck.loadDetail_view  = function(rowData){
	//零散明细
	$('#checkDataGridDetail_view').datagrid( 'options' ).url=BasePath+'/bill_ch_check_dtl/dtl_list.json?locno='+rowData.locno+'&checkNo='+rowData.checkNo+'&itemNoIsNot=N&isNullBox=N';
	$('#checkDataGridDetail_view').datagrid( 'load' );
	
	//整箱明细
	$('#checkDataGridDetailBox_view').datagrid( 'options' ).url=BasePath+'/bill_ch_check_dtl/dtl_list.json?locno='+rowData.locno+'&checkNo='+rowData.checkNo+'&itemNoIsNot=N&isNullBox=Y';
	$('#checkDataGridDetailBox_view').datagrid( 'load' );
	
	//差异明细
	var queryMxURL = BasePath+'/bill_ch_check_dtl/dtl_list.json';
	var params = {};
	params.locno = rowData.locno;
	params.checkNo =rowData.checkNo;
	params.itemNoIsNot ="N";
	params.differentFlag = "1";
	params.hasGroupIs = "Y";
    $('#checkDataGridDetail_view_dif').datagrid( 'options' ).queryParams= params;
    $('#checkDataGridDetail_view_dif').datagrid( 'options' ).url=queryMxURL;
    $('#checkDataGridDetail_view_dif').datagrid( 'load' );
	$("#checkDetailDialog_view").window('open'); 
	$("#checkDetailForm_view").form("load",rowData);
};

receiptcheck.closeCheckDetailDialog = function(){
	$("#checkDetailDialog").window('close'); 
};
receiptcheck.closeCheckDetailDialog_view = function(){
	$("#checkDetailDialog_view").window('close'); 
};


//选择商品
receiptcheck.openItemSlect = function(obj){
	$('#openUIItem').window({
			title:"商品选择列表",
			onBeforeClose:function(){
				$(".tooltip").remove();
			}
		});
	
	$( "#dataGridJGItem").datagrid('clearData');
	//receiptcheck.initCellNo();	
	receiptcheck.searchItemClear();
	$('#openUIItem').window('open');
};

//查询商品信息
receiptcheck.searchItem = function(){
	var planType = $("#checkPlanType").val();	
	//盘点计划单号
	var planNo = $("#checkPlanNo").val();
	//是否限制品牌   0：不限制 1;限制
	var limitBrandFlag = $("#checkLimitBrandFlag").val();
	//计划单品牌编码
	var brandNo = $("#checkBrandNo").val();
	
	var queryMxURL="";
	if(planType=="1"){//储位盘
		queryMxURL=BasePath+'/bill_ch_check_dtl/selectItem4ChCheck?locno='+receiptcheck.locno+'&planNo='+planNo;
	}else if(planType=="0" && limitBrandFlag=="0"){//商品盘、不限制品牌 
		queryMxURL=BasePath+'/bill_ch_check_dtl/findItemByPlan.json?locno='+receiptcheck.locno+'&planNo='+planNo;
	}else if(planType=="0" && limitBrandFlag=="1"){//商品盘、限制品牌 
		queryMxURL=BasePath+'/bill_ch_check_dtl/findBrandLimitItem?locno='+receiptcheck.locno+'&planNo='+planNo+'&brandNo='+brandNo;
	}
	var itemSearchFormArr = $('#itemSearchForm').serializeArray();
	var fromObjStr=convertArray(itemSearchFormArr);
    //3.加载明细
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
};

//确定选择商品
receiptcheck.selectItemOK = function(){
	
	var itemRowData = $("#dataGridJGItem").datagrid("getChecked");
	if(itemRowData.length < 1){
		alert("请选择要添加的商品信息!");
		return;
	}
	
	//校验储位--su,yq注释掉，更改为手输储位
//	var cellNo=$('#cellNo').combo('getText');
//	var cellNoDatas=$('#cellNo').combobox('getData');
//	var cellNoIsValid=false;
//	for(var i =0;i<cellNoDatas.length;i++){  
//        if(cellNo==cellNoDatas[i].cellNo){  
//        	cellNoIsValid=true;
//        	break;  
//        }  
//    }
//	
//	if(!cellNoIsValid || cellNoDatas.length==0){
//		alert("请输入选择正确的储位。");		
//		return;
//	}
		
	$('#openUIItem').window('close');
	var rowData = $("#dataGridJGItem").datagrid("getChecked");// 获取所有勾选checkbox的行
	var data;
	for(var i=0,length=rowData.length;i<length;i++){
		data = rowData[i];
		//data.cellNo=$("#cellNo").combobox("getValue");
		data.rowId="";
		data.itemQty =0;
		data.checkQty=0;
		data.addFlag="1";
		var tempObj = $('#checkDataGridDetail');
		tempObj.datagrid('appendRow',data);
		var tempIndex = tempObj.datagrid('getRows').length - 1;
		tempObj.datagrid('beginEdit', tempIndex);
     }
};
receiptcheck.closeUI = function(){
	$('#openUIItem').window('close');
};

receiptcheck.deleterow = function(){
	var rowData = $("#checkDataGridDetail").datagrid("getSelected");// 获取所有勾选checkbox的行
	if(rowData==null){
		alert("请选择要删除的行",1);
		return ;
	}else{
		if(rowData.addFlag=="0"){//1为新增商品
			alert("非新增商品不能删除",1);
			return ;
		}
		 var rowIndex = $("#checkDataGridDetail").datagrid('getRowIndex', rowData);
         $("#checkDataGridDetail").datagrid('deleteRow', rowIndex);
	}
};


//保存初盘发单明细
receiptcheck.saveChckeDtl = function(){
	
	var allDate = $("#checkDataGridDetail").datagrid('getRows');
	var item;
	for(var index=0,length = allDate.length;index<length;index++){
		item = allDate[index];
		$("#checkDataGridDetail").datagrid('endEdit',index);
		var checkQty = item.checkQty;
		if(parseInt(checkQty)<0){
			alert("第"+(index+1)+"行实盘数量不能为负!",1);
			return;
		}
		if(parseInt(checkQty)>99999999){
			alert("第"+(index+1)+"行实盘数量不能大于99999999!",1);
			return;
		}
		var cellNo = item.cellNo;
		if(cellNo==null||cellNo==""){
			alert("第"+(index+1)+"行储位不能为空!",1);
			return;
		}
	}
	wms_city_common.loading("show","正在保存明细......");
	var inserted = $("#checkDataGridDetail").datagrid('getChanges','inserted');
	var deleted = $("#checkDataGridDetail").datagrid('getChanges','deleted');
	var updated = $("#checkDataGridDetail").datagrid('getChanges','updated');
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		data:{
			inserted:encodeURIComponent(JSON.stringify(inserted)),
			deleted:encodeURIComponent(JSON.stringify(deleted)),
			updated:encodeURIComponent(JSON.stringify(updated)),
			checkNo:$("#detailCheckNo").val()
		},
		dataType : "json",
		url:BasePath+'/bill_ch_check_dtl/saveCheckDtl',
		success : function(data) {
			if(data.result=="success"){
				alert("保存成功");
				$("#checkDetailDialog").window('close'); 
			}else{
				alert(data.msg,2);
			}
			wms_city_common.loading();
		}
	});
};

//初盘回单审核
receiptcheck.check = function(){
	var checkRows = $("#checkDataGrid").datagrid('getChecked');
	if(checkRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}else{
		var checkIds = [];
		for(var i=0;i<checkRows.length;i++){
			if(checkRows[i].status !='20'){
				alert("非已发单状态的单据不能审核，请重新选择",1);
				return;
			}
			checkIds.push(checkRows[i].checkNo+"_"+checkRows[i].planNo);
		}
		
		$.messager.confirm("确认","你确定要审核这"+checkIds.length+"条数据吗？", function (r){  
	        if (r) {
	    		wms_city_common.loading("show","正在审核......");
	    		$.ajax({
	    			async : false,
	    			cache : false,
	    			type : 'POST',
	    			data:{checkNos:checkIds.join(",")},
	    			dataType : "json",
	    			url:BasePath+'/bill_ch_check/chCheck',
	    			success : function(data) {
	    				wms_city_common.loading();
	    				if(data.result=="success"){
	    					 alert('审核成功!');
	    					 receiptcheck.searchArea();
	    				}else{
	    					alert(data.msg,2);
	    				}
	    			}
	    		});
	        }  
	    });
	}
};

//初始化储位
receiptcheck.initCellNo = function(){
	var planType = $("#checkPlanType").val();	
	//盘点计划单号
	var planNo = $("#checkPlanNo").val();
	
	var url;
	
	if(planType=="1"){//储位盘
		url=BasePath+'/bill_ch_check/findCellNobyPlan?planNo='+planNo;
	}else if(planType=="0"){//商品盘
		url=BasePath+'/cm_defcell/findSimple?cellStatus=0&locno='+receiptcheck.locno;
	}
	
	$.ajax({
		async : false,
		cache : true,
		type : 'POST',
		dataType : "json",
		url:url,
		success : function(data) {
			$('#cellNo').combobox({
			     valueField:"cellNo",
			     textField:"cellNo",
			     data:data,
			     panelHeight:"150"
			  });
		}
	});
};

receiptcheck.loadDetail = function(rowData,rowIndex){
	
	receiptcheck.curRowIndex=rowIndex;
	
	//receiptcheck.initGridHead(rowData.sysNo);
	
	var sysNo = receiptcheck.getSysNo(rowData);
	if(sysNo==""){
		return;
	}
	receiptcheck.initGridHead(sysNo);
	receiptcheck.loadDataDetailViewGrid(rowData);
	$("#showDialog").window('open'); 
};

receiptcheck.loadDetail = function(rowData){
    $('#checkDataGridDetail').datagrid( 'options' ).queryParams= {pageNumber:1};
    $('#checkDataGridDetail').datagrid( 'options' ).url=BasePath+'/bill_ch_check_dtl/edit_list.json?locno='+receiptcheck.locno+'&checkNo='+rowData.checkNo;
	$('#checkDataGridDetail').datagrid( 'load' );
};

//按计划保存
receiptcheck.saveByPlan = function(fBoxFlag){
	wms_city_common.loading("show","正在保存......");
	$.ajax({
		async : true,
		cache : true,
		type : 'POST',
		data:{
			checkNo:$("#detailCheckNo").val(),
			locno:receiptcheck.locno,
			fBoxFlag:fBoxFlag
		},
		dataType : "json",
		url:BasePath+'/bill_ch_check_dtl/saveByPlan',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert("按计划保存成功");
				if(fBoxFlag=='N'){
					$("#checkDataGridDetail").datagrid('load');
				}else{
					$("#checkDataGridBoxDetail").datagrid('load');
				}
			}else{
				alert(data.msg,2);
			}
		}
	});
};

//实盘置零
receiptcheck.resetPlan = function(fBoxFlag){
	wms_city_common.loading("show","正在加载......");
	$.ajax({
		async : true,
		cache : true,
		type : 'POST',
		data:{
			checkNo:$("#detailCheckNo").val(),
			locno:receiptcheck.locno,
			fBoxFlag:fBoxFlag
		},
		dataType : "json",
		url:BasePath+'/bill_ch_check_dtl/resetPlan',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert("实盘置零成功");
				if(fBoxFlag=='N'){
					$("#checkDataGridDetail").datagrid('load');
				}else{
					$("#checkDataGridBoxDetail").datagrid('load');
				}
			}else{
				alert(data.msg,2);
			}
		}
	});
};


//按箱计划保存
receiptcheck.saveByPlanBox = function(fBoxFlag){
	var selectRow = $('#checkDataGridBoxDetail').datagrid('getSelected');
	if(selectRow==null){
		alert("请先选择一个箱号！",1);
		return;
	}
	var rowIndex = $('#checkDataGridBoxDetail').datagrid('getRowIndex',selectRow);
	$('#checkDataGridBoxDetail').datagrid('endEdit',rowIndex);
	selectRow = $('#checkDataGridBoxDetail').datagrid('getSelected');
	var  labelNo = selectRow.labelNo;
	
	wms_city_common.loading("show","正在保存......");
	$.ajax({
		async : true,
		cache : true,
		type : 'POST',
		data:{
			checkNo:$("#detailCheckNo").val(),
			locno:receiptcheck.locno,
			labelNo:labelNo,
			fBoxFlag:fBoxFlag
		},
		dataType : "json",
		url:BasePath+'/bill_ch_check_dtl/saveByPlan',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert("按箱计划保存成功");
				$("#checkDataGridBoxDetail").datagrid('load');
			}else{
				alert(data.msg,2);
			}
		}
	});
};

//按箱实盘置零
receiptcheck.resetPlanBox = function(fBoxFlag){
	var selectRow = $('#checkDataGridBoxDetail').datagrid('getSelected');
	if(selectRow==null){
		alert("请先选择一个箱号！",1);
		return;
	}
	var rowIndex = $('#checkDataGridBoxDetail').datagrid('getRowIndex',selectRow);
	$('#checkDataGridBoxDetail').datagrid('endEdit',rowIndex);
	selectRow = $('#checkDataGridBoxDetail').datagrid('getSelected');
	var  labelNo = selectRow.labelNo;
	
	wms_city_common.loading("show","正在加载......");
	$.ajax({
		async : true,
		cache : true,
		type : 'POST',
		data:{
			checkNo:$("#detailCheckNo").val(),
			locno:receiptcheck.locno,
			labelNo:labelNo,
			fBoxFlag:fBoxFlag
		},
		dataType : "json",
		url:BasePath+'/bill_ch_check_dtl/resetPlan',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert("实盘置零成功");
				$("#checkDataGridBoxDetail").datagrid('load');
			}else{
				alert(data.msg,2);
			}
		}
	});
};

//导出差异明细
receiptcheck.do_export = function(){
	exportExcelBaseInfo('checkDataGridDetail_view_dif','/bill_ch_check_dtl/do_export.htm','盘点差异明细');
};


receiptcheck.onClickRowCheckDtl = function(rowIndex, rowData){
	var curObj = $("#checkDataGridDetail");
	curObj.datagrid('beginEdit', rowIndex);
	var ed = curObj.datagrid('getEditor', {index:rowIndex,field:'cellNo'});
	var itemQty = rowData.itemQty;
	if(itemQty!=0){
		$(ed.target).attr("disabled",true);
	}else{
		$(ed.target).attr("disabled",false);
	}
};
//下载模板
receiptcheck.downloadTemp = function (){
	window.open(BasePath + "/bill_ch_check_dtl/downloadTemple");
};
//导入 
receiptcheck.showImport = function(){
	var checkNo = $('#detailCheckNo').val();
	$("#iframe").attr("src",BasePath + "/bill_ch_check_dtl/iframe4receipt?checkNo="+checkNo+"&v="+new Date());
	$("#showImportDialog").window('open'); 
}
receiptcheck.closeowWind = function(id){
	$('#'+id).window('close'); 
}
receiptcheck.importSuccess = function(){
	var checkNo = $('#detailCheckNo').val();
	receiptcheck.closeowWind("showImportDialog");	
	var tempObj = $('#checkDataGridDetail');
	tempObj.datagrid( 'options' ).url = BasePath+'/bill_ch_check_dtl/edit_list.json?locno='+receiptcheck.locno+'&checkNo='+checkNo+'&itemNoIsNot=N&isNullBox=N';
	tempObj.datagrid('load');
	
	alert("导入成功!");
}
receiptcheck.loading = function(type,msg){
	wms_city_common.loading(type,msg);
}
receiptcheck.doExport = function(isNullBox){
	var checkNo = $('#detailCheckNo').val();
	exportExcelBaseInfo('checkDataGridDetail','/bill_ch_check_dtl/do_export?locno='+receiptcheck.locno+'&checkNo='+checkNo+'&itemNoIsNot=N&isNullBox='+isNullBox,'初盘明细('+checkNo+')');
};



//==================初盘回单明细导出 add by li.zm====================
receiptcheck.preColNames = [
             			 	{field : 'cellNo',title : '储位编码',width : 200,align:'left'},
             			 	{field : 'itemName',title : '商品名称',width : 200,align:'left'},
             			 	{field : 'itemNo',title : '商品编码',width : 200,align:'left'},
             			 	{field : 'colorName',title : '颜色',width : 150,align:'left'},
           			 ];
receiptcheck.endColNames = [{field : 'totalCheckQty',title : '合计',width : 40,align:'right'}] ;
receiptcheck.exportDtl = function(){
	var checkNo = $("#checkNoId").val();
	
	export4Size(receiptcheck.preColNames, receiptcheck.endColNames, "初/复盘点回单汇总明细("+checkNo+")");
};
function export4Size(preColNames,endColNames,fileName){
	var url = BasePath+'/bill_ch_check_dtl/dtlExport';
	var locno = receiptcheck.user.locno;
	//初盘单号
	var checkNo = $("#checkNoId").val();
	var DetailView = $("#checkDataGridDetail_view").datagrid('getRows');
	var BoxView = $("#checkDataGridDetailBox_view").datagrid('getRows');
	if(DetailView.length > 0 || BoxView.length >0){
		$("#exportExcelForm").remove();
		$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
		var fromObj = $('#exportExcelForm');
		wms_city_common.loading("show","正在导出");
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {
				param.locno=locno;
				param.checkNo=checkNo;
				param.preColNames = JSON.stringify(preColNames);
				param.endColNames = JSON.stringify(endColNames);
				param.fileName = fileName;
			},
			success : function() {
			}
		});
		wms_city_common.loading();
	}else{
		alert("没有可导出的数据!");
		return false;
	}
};
//==================初盘回单明细导出 add by li.zm====================