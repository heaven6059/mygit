
var billconconvert = {};
billconconvert.user;
/**
 * 所有仓库
 */
billconconvert.allLocno = [];
/**
 * 除当前登陆之外的仓库
 */
billconconvert.otherLocno = [];
billconconvert.initOtherLocno = function(){
	var temp = {};
	for(var idx=0;idx<billconconvert.allLocno.length;idx++){
		if(billconconvert.allLocno[idx].organizationNo != billconconvert.user.locno){
			temp = billconconvert.allLocno[idx];
			temp.valueAndText = temp.organizationNo + '→' + temp.storeName;
			billconconvert.otherLocno[billconconvert.otherLocno.length] = temp;
		}
	}
	$("#storeNoInfo,#storeNoPost").combobox({
		data:billconconvert.otherLocno,
		valueField:'organizationNo',
		textField:'valueAndText',
		panelHeight:150
	});
	$("#storeNo").combobox({
		data:billconconvert.otherLocno,
		valueField:'organizationNo',
		textField:'valueAndText',
		panelHeight:150,
		loadFilter:function(data){
			var first = {};
			first['organizationNo'] = '';
			first['valueAndText'] = '全选';
			var tempData = [];
			tempData[tempData.length] = first;
	    	for(var i=0;i<data.length;i++){
	    		tempData[tempData.length] = data[i];
	    	}
	    	return tempData;
		}
	});
};
billconconvert.intercept = function(){
	if(billconconvert.otherLocno.length <= 0){
		alert("至少需要两个机构权限才能操作此模块!");
		return false;
	}
	return true;
};
billconconvert.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
/**
 * 加载明细
 * type：info或post
 */
billconconvert.loadDetail = function(type){
	var url = BasePath+'/bill_con_convert_dtl/list.json';
	var params = {};
	var dgId = '';
	if(type == 'info'){
		dgId = 'dtl_info_dataGrid';
		params = {
				convertNo:$("#convertNoInfo").val(),
				locno:billconconvert.user.locno
		};
	}else if(type == 'post'){
		dgId = 'dtl_post_dataGrid';
		params = {
				convertNo:$("#convertNoPost").val(),
				locno:billconconvert.user.locno
		};
	}
	billconconvert.loadGridDataUtil(dgId, url, params);
};
billconconvert.removeMain = function(){
	var checkRows = $("#mainDataGrid").datagrid('getChecked');
	var len = checkRows.length;
	if(len <= 0){
		alert("请选择需要删除的记录!");
		return;
	}
	var keyStr = "";
	for(var idx=0;idx<len;idx++){
		if(checkRows[idx].status != '10'){
			alert("只能删除新建状态的记录!");
			return;
		}
		keyStr += checkRows[idx].locno + "_" +checkRows[idx].ownerNo + "_" + checkRows[idx].convertNo;
		if(idx < (len-1)){
			keyStr += "|";
		}
	}
	var url = BasePath+'/bill_con_convert/removeMainAndDtl';
	var params = {
			keyStr:keyStr
	};
	$.messager.confirm("确认","您确定要删除这"+len+"条信息吗？", function (r){
		if(!r){
			return;
		}
		wms_city_common.loading("show","正在删除主信息......");
		$.post(url, params, function(result) {
			if(result.result == 'success'){
				alert('删除成功!');
				billconconvert.searchData();
			}else{
				alert(result.result,2);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
	    	alert('保存失败!',1);
	    	wms_city_common.loading();
	    });
	});
};
billconconvert.check = function(){
	var checkRows = $("#mainDataGrid").datagrid('getChecked');
	var len = checkRows.length;
	if(len <= 0){
		alert("请选择需要审核的记录!");
		return;
	}
	var keyStr = "";
	for(var idx=0;idx<len;idx++){
		if(checkRows[idx].status != '10'){
			alert("只能审核新建状态的记录!");
			return;
		}
		keyStr += checkRows[idx].locno + "_" +checkRows[idx].ownerNo + "_" + checkRows[idx].convertNo + "_" + checkRows[idx].storeNo;
		if(idx < (len-1)){
			keyStr += "|";
		}
	}
	var url = BasePath+'/bill_con_convert/check';
	var params = {
			keyStr:keyStr,
			operator:billconconvert.user.loginName,
			auditorName:billconconvert.user.username
	};
	$.messager.confirm("确认","您确定要审核这"+len+"条信息吗？", function (r){
		if(!r){
			return;
		}
		wms_city_common.loading("show","正在审核......");
		$.post(url, params, function(result) {
			if(result.result == 'success'){
				alert('审核成功!');
				billconconvert.searchData();
			}else{
				alert(result.result,2);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
	    	alert('审核失败!',1);
	    	wms_city_common.loading();
	    });
	});
};
billconconvert.openInfo = function(rowData){
	$("#MainFormInfo").form("clear");
	$("#MainFormInfo").form("load",rowData);
	billconconvert.infoDisable(true);
	$("#showDetailDialog").window('open');
	billconconvert.loadDetail('info');
};
/**
 * 打开新增或修改弹出框
 * type：add或edit
 */
billconconvert.openPostDialog = function(type){
	if(!billconconvert.intercept()){
		return;
	}
	$("#MainFormPost").form('clear');
	var title = "转货/修改";
	if(type == 'add'){
		title = "转货/新增";
		$("#dtl_post_div").hide();
		billconconvert.postDisable(false);
	}else if(type == 'edit'){
		$("#dtl_post_div").show();
		billconconvert.postDisable(true);
		var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
		if(checkedRows.length < 1){
			alert('请选择要修改的记录!',1);
			return;
		}else if(checkedRows.length>1){
			alert('只能修改一条记录!',1);
			return;
		}else{
			var rowData = checkedRows[0];
			if(rowData.status != '10'){
				alert('只能修新建状态的单据!',1);
				return;
			}
			$("#MainFormPost").form("clear");
			$("#MainFormPost").form("load",rowData);
			billconconvert.loadDetail('post');
		}
	}
	$("#postDetailDialog").window({title:title});
	$("#postDetailDialog").window('open');
	$("#option").val(type);
};
billconconvert.postDisable = function(isDisable){
	if(isDisable){
		$("#ownerNoPost").combobox('disable');
		$("#storeNoPost").combobox('disable');
		$("#convertTypePost").combobox('disable');
	}else{
		$("#ownerNoPost").combobox('enable');
		$("#storeNoPost").combobox('enable');
		$("#convertTypePost").combobox('enable');
	}
};
billconconvert.infoDisable = function(isDisable){
	if(isDisable){
		$("#ownerNoInfo").combobox('disable');
		$("#storeNoInfo").combobox('disable');
		$("#convertTypeInfo").combobox('disable');
	}else{
		$("#ownerNoInfo").combobox('enable');
		$("#storeNoInfo").combobox('enable');
		$("#convertTypeInfo").combobox('enable');
	}
};
billconconvert.save = function(){
	var fromObj=$('#MainFormPost');
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
		return ;
	}
	if($("#convertDatePost").datebox('getValue') == ''){
		alert("请选择转货日期");
		return;
	}
	var option = $("#option").val();
	var url = '';
	var params = {
			locno:billconconvert.user.locno,
			ownerNo:$("#ownerNoPost").combobox('getValue'),
			storeNo:$("#storeNoPost").combobox('getValue'),
			convertType:$("#convertTypePost").combobox('getValue'),
			convertDate:$("#convertDatePost").datebox('getValue'),
			remark:$("#remarkPost").val()
	};
	if(option == 'add'){
		url = BasePath+'/bill_con_convert/add';
		params.creator = billconconvert.user.loginName;
	}else if(option == 'edit'){
		url = BasePath+'/bill_con_convert/modify';
		params.convertNo = $("#convertNoPost").val();
		params.editor = billconconvert.user.loginName;
		params.editorName = billconconvert.user.username;
	}
	$.messager.confirm("确认","您确定要保存主信息吗？", function (r){
		if(!r){
			return;
		}
		wms_city_common.loading("show","正在保存转货主信息......");
		$.post(url, params, function(result) {
			if(result.result == 'success'){
				alert('保存成功!');
				if(option == 'add'){
					billconconvert.postDisable(true);
					$("#option").val('edit');
					$("#dtl_post_div").show();
					$("#dtl_post_dataGrid").datagrid('loadData',[]);
					$("#convertNoPost").val(result.convertNo);
				}
			}else{
				alert(result.result,2);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
	    	alert('保存失败!',1);
	    	wms_city_common.loading();
	    });
	});
};
//关闭窗口
billconconvert.closeWindow = function(id){
	$('#'+id).window('close');  
};
//清除查询条件
billconconvert.searchClear = function(id){
	$('#'+id).form("clear");
};
//搜索数据
billconconvert.searchData = function(){
	if(!billconconvert.intercept()){
		return;
	}
	var startCreatetm =  $('#createtmStart').datebox('getValue');
	var endCreatetm =  $('#createtmEnd').datebox('getValue');
	var audittmStart =  $('#audittmStart').datebox('getValue');
	var audittmEnd =  $('#audittmEnd').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }   
	if(!isStartEndDate(audittmStart,audittmEnd)){    
		alert("审核日期开始日期不能大于结束日期");   
		return;   
	}  
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_con_convert/list.json?locno='+billconconvert.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam.sort = 'd.convert_no';
	reqParam.order = 'desc';
	if($("#storeNo").combobox('getValue') == ''){
		var storeNoStr = '';
		for(var idx=0;idx<billconconvert.otherLocno.length;idx++){
			storeNoStr += "'"+billconconvert.otherLocno[idx].organizationNo+"'";
			if(idx<(billconconvert.otherLocno.length-1)){
				storeNoStr += ",";
			}
		}
		reqParam.storeNo = storeNoStr;
	} else {
		reqParam.storeNo ="'" + $("#storeNo").combobox('getValue') + "'";
	}
	billconconvert.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
};
//搜索数据
billconconvert.searchCon = function(){
	var fromObjStr=convertArray($('#selectConSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_con_convert_dtl/content.json?locno='+billconconvert.user.locno+'&status=0';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam.sort = 'cell_No,item_No,size_No';
	billconconvert.loadGridDataUtil('con_select_datagrid', queryMxURL, reqParam);
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
//加载Grid数据Utils
billconconvert.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};
billconconvert.openAddDtl = function(){
	$("#con_select_datagrid").datagrid('loadData',[]);
	$("#con_select_dialog").window('open');
};
billconconvert.removeDtl = function(id){
	var dgObj = $("#"+id);
	var checkRows = dgObj.datagrid('getChecked');
	var len = checkRows.length;
	if(len <= 0){
		alert("请选择需要删除的记录!");
		return;
	}
	$.messager.confirm("确认","您确定删除这"+len+"条记录吗？", function (r){
		if(!r){
			return;
		}
		var temp = null;
		var rowIndex = 0;
		for(var idx=0;idx<len;idx++){
			temp = checkRows[idx];
			rowIndex = dgObj.datagrid('getRowIndex', temp);
			dgObj.datagrid('deleteRow', rowIndex);
		}
	});
};
billconconvert.saveDtl = function(){
	var allRows = $("#dtl_post_dataGrid").datagrid('getRows');
	var map = {};
	var str = '';
	for(var idx=0;idx<allRows.length;idx++){
		str = allRows[idx].itemNo+"|"+allRows[idx].sizeNo;
		if(map[str]){
			alert("存在重复数据<br>商品|尺码:"+str);
			return;
		}else{
			map[str] = true;
		}
	}
	var inserted = $("#dtl_post_dataGrid").datagrid('getChanges','inserted');
	var deleted = $("#dtl_post_dataGrid").datagrid('getChanges','deleted');
	var updated = $("#dtl_post_dataGrid").datagrid('getChanges','updated');
	if(inserted.length == 0 && deleted.length == 0 && updated.length == 0){
		alert("明细没有更改,无需保存!");
		return;
	}
	var url = BasePath+'/bill_con_convert_dtl/saveDtl';
	var params = {
			inserted:encodeURIComponent(JSON.stringify(inserted)),
			deleted:encodeURIComponent(JSON.stringify(deleted)),
			updated:encodeURIComponent(JSON.stringify(updated)),
			locno:billconconvert.user.locno,
			creatorName:billconconvert.user.username,
			editorName:billconconvert.user.username,
			convertNo:$("#convertNoPost").val(),
			ownerNo:$("#ownerNoPost").combobox('getValue'),
			operator:billconconvert.user.loginName,
			destLocno:$("#storeNoPost").combobox('getValue'),
	};
	$.messager.confirm("确认","您确定保存明细吗？", function (r){
		if(!r){
			return;
		}
		wms_city_common.loading("show","正在保存转货明细......");
		$.post(url, params, function(result) {
			if(result.result == 'success'){
				alert('保存成功!');
				billconconvert.closeWindow('postDetailDialog');
				billconconvert.searchData();
			}else{
				alert(result.result,2);
			}
			wms_city_common.loading();
		}, "JSON").error(function() {
	    	alert('保存失败!',1);
	    	wms_city_common.loading();
	    });
	});
};
billconconvert.selectConOk = function(){
	var checkRows = $("#con_select_datagrid").datagrid('getChecked');
	var len = checkRows.length;
	if(len <= 0){
		alert("请选择需要确认的记录!");
		return;
	}
	$.messager.confirm("确认","您确定添加这"+len+"条记录吗？", function (r){
		if(!r){
			return;
		}
		billconconvert.insertRowAtEnd('dtl_post_dataGrid',checkRows);
		billconconvert.closeWindow('con_select_dialog');
	});
};
billconconvert.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);	
	$.each(rowData, function(index, item){
		tempObj.datagrid('appendRow', item);
	});
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('selectRow',tempIndex);
	
};
//================转货状态S====================
billconconvert.status = {};
billconconvert.statusFormatter = function(value, rowData, rowIndex){
	return billconconvert.status[value];
};
//================转货状态E====================
//================转货类型S====================
billconconvert.convertType = {};
billconconvert.convertTypeFormatter = function(value, rowData, rowIndex){
	return billconconvert.convertType[value];
};
//================转货类型E====================
//================货主S====================
billconconvert.owner = {};
//================货主E====================
//================品牌库S====================
billconconvert.sys = {};
//================品牌库E====================
billconconvert.sourceType = {};
billconconvert.sourceTypeFormatter = function(value, rowData, rowIndex){
	return billconconvert.sourceType[value];
};
// JS初始化执行
$(document).ready(function(){
	$("#createtmStart").datebox('setValue',getDateStr(-2));
	billconconvert.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(u){billconconvert.user=u;});
	billconconvert.ajaxRequest(BasePath+'/user_organization/findUserOrganization',{},false,function(u){billconconvert.allLocno=u.list;});
	billconconvert.initOtherLocno();
	//初始化转货状态
	wms_city_common.comboboxLoadFilter(
			["status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CONVERT_STATUS',
			{},
			billconconvert.status,
			null);
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["ownerNoInfo","ownerNoPost"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[false,false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billconconvert.owner,
			null);
	//初始化转货类型
	wms_city_common.comboboxLoadFilter(
			["convertType","convertTypeInfo","convertTypePost"],
			null,
			null,
			null,
			true,
			[true,false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CONVERT_TYPE',
			{},
			billconconvert.convertType,
			null);
	//初始化来源类型
	wms_city_common.comboboxLoadFilter(
			["sourceType"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CONVERT_SOURCE_TYPE',
			{},
			billconconvert.sourceType,
			null);
	//初始化品牌库
	wms_city_common.comboboxLoadFilter(
			["sysNo"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
			{},
			billconconvert.sys,
			null);
	wms_city_common.sysBind(
			'sysNo',
			['brandNo'],
			['/brand/get_bizDy'],
			[['brandNo','brandName']],
			[true]);
	wms_city_common.initWareNoForCascade(
			billconconvert.user.locno,
			['wareNo'],
			['areaNo'],
			['stockNo'],
			null,
			[true],
			null);
	!billconconvert.intercept();
	$('#dtl_info_dataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1] != null){
		   				billconconvert.itemQty = data.footer[1];
		   			}else{
		   				var rows = $('#dtl_info_dataGrid').datagrid('getFooterRows');
			   			rows[1] = billconconvert.itemQty;
			   			$('#dtl_info_dataGrid').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	$('#mainDataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1] != null){
		   				billconconvert.mainItemQty = data.footer[1];
		   			}else{
		   				var rows = $('#mainDataGrid').datagrid('getFooterRows');
			   			rows[1] = billconconvert.mainItemQty;
			   			$('#mainDataGrid').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
});
billconconvert.printDetail4SizeHorizontal = function(){
	var rows = $('#mainDataGrid').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	wms_city_common.loading("show","正在加载......");
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].locno+"|"+rows[i].convertNo+"|"+rows[i].ownerNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_con_convert_dtl/printDetail4SizeHorizontal',
        data: {
        	keys:keys.join(",")
        },
        success: function(data){
        	wms_city_common.loading();
        	if(data.result=="success"){
        		print4SizeHorizontal(data);
        	}else if(data.result == "other"){
        		alert(data.msg);
        	}else{
        		alert(data.msg,2);
        	}
        	
		}
    });
};
function print4SizeHorizontal(data){
	var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	if(LODOP==null){
		return;
	}
	LODOP.SET_PRINT_PAGESIZE(1, 0, 0, "A4");
	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
	for(var idx=0;idx<data.pages.length;idx++){
		LODOP.NewPageA();
		var headHtml = createHeadHtml(data.pages[idx]);
		var bodyHtml = createBodyHtml(data.pages[idx]);
		//设置表格内容
		LODOP.ADD_PRINT_TABLE(100,0,"100%",380,strStyle+bodyHtml);
		LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
		//设置表格头部
		LODOP.ADD_PRINT_HTM(10,10,"100%",110,headHtml);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		//设置条码
		LODOP.ADD_PRINT_BARCODE(50,10,250,40,"128A",data.pages[idx].convertNo);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	}
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
}
function createHeadHtml(page){
	var storeNo = null;
	for(var i=0;i<billconconvert.otherLocno.length;i++){
		var tempStoreNo = billconconvert.otherLocno[i].valueAndText.split('→');
		if(tempStoreNo[0] == page.storeNo){
			storeNo = tempStoreNo[1];
		}
	}
	var html = "<table style='width:100%;font-size:13px;'>";
	html += "<tr><td style='text-align:center;font-size:35px;' colspan='4'>跨部门转货单</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+page.convertNo+"</td><td>发货方："+billconconvert.user.locname+"" +
			"</td><td>收货方："+storeNo+"("+page.storeNo+")</td><td>总合计："+page.total+"</td></tr>";
	html += "</table>";
	return html;
}
function createBodyHtml(page){
	var sizeMap = {};
	var html = "";
	var rowspan = 1;
	var sizeColNum = 0;
	var rows = [];
	var row = {};
	rowspan = page.sizeList.length;
	sizeColNum = page.sizeColNum;
	html = "<table border='0' cellpadding='1' cellspacing='1' width='100%' style='border-collapse:collapse;font-size:13px;' bordercolor='#333333'><thead>";
	html += "<tr style='background-color: #fff;'>";
	html += "<td rowspan='"+rowspan+"'>商品编码</td><td rowspan='"+rowspan+"'>商品名称</td>";
	for(var i=0;i<rowspan;i++){
		var sizeArray = [];
		if(i > 0){
			html += "<tr style='background-color: #fff;'>";
			for(var j=0;j<sizeColNum;j++){
				if(j >= page.sizeList[i].length){
					html += "<td>&nbsp;</td>";
					if(j>0){
						sizeArray[j-1] = "";
					}
				}else{
					html += "<td>"+page.sizeList[i][j]+"</td>";
					if(j>0){
						sizeArray[j-1] = page.sizeList[i][j];
					}
				}
			}
			html += "</tr>";
		}else{
			for(var j=0;j<sizeColNum;j++){
				if(j >= page.sizeList[i].length){
					html += "<td>&nbsp;</td>";
					if(j>0){
						sizeArray[j-1] = "";
					}
				}else{
					html += "<td>"+page.sizeList[i][j]+"</td>";
					if(j>0){
						sizeArray[j-1] = page.sizeList[i][j];
					}
				}
			}
			html += "<td rowspan='"+rowspan+"'>合计</td>";
			html += "</tr>";
		}
		sizeMap[page.sizeList[i][0]] = sizeArray;
	}
	html += "</thead>";
	rows = page.list;
	for(var x=0;x<rows.length;x++){
		row = rows[x];
		html += "<tr style='background-color: #fff;'>";
		html += "<td>"+row.itemNo+"</td>";
		html += "<td>"+row.itemName+"</td>";
		html += "<td>"+row.sizeKind+"</td>";
		for(var k=0;k<sizeMap[row.sizeKind].length;k++){
			if(row.sizeCodeQtyMap[sizeMap[row.sizeKind][k]] == null){
				html += "<td>&nbsp;</td>";
			}else{
				html += "<td>"+row.sizeCodeQtyMap[sizeMap[row.sizeKind][k]]+"</td>";
			}
		}
		html += "<td>"+row.totalQty+"</td>";
		html += "</tr>";
		
	}
	html = html + "<tfoot><tr><td style='text-align:13px;text-align:center;' colspan='"+(sizeColNum+2)+"'><font tdata='pageNO' format = '#' align='center'>第#页</font>&nbsp;/&nbsp;<font format='#' tdata='pageCount'>共#页</font></td></tr></tfoot>";
	html += "</table>";
	return html;
}
