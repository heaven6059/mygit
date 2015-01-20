var billOmLocate = {};

//加载Grid数据Utils
billOmLocate.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
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


//打开窗口
billOmLocate.openWindow = function(windowId,opt){
	$('#'+windowId).window({
		title:opt
	});
	$('#'+windowId).window('open');
};


//关闭窗口
billOmLocate.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};


//波次详情
billOmLocate.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bill_om_locate/list.json?locno='+billOmLocate.locno,'title':'波次列表','pageNumber':1 });
};

//查询波次信息
billOmLocate.searchData = function(){
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
    
    var startCreatetm =  $('#startCreatetmCondition').datebox('getValue');
	var endCreatetm =  $('#endCreatetmCondition').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
       return;   
   }
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_locate/listBillOmLocate.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billOmLocate.locno;
	reqParam['isContinue'] = "0";
	billOmLocate.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
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

//清除查询条件
billOmLocate.searchClear = function(id){
	$('#'+id).form("clear");
	$('#brandNo').combobox("loadData",[]); 
};

//波次明细
billOmLocate.loadDetail = function(rowData){
	billOmLocate.openWindow('openWindowLocate', '修改/查看');
	
	var queryParamsLocate = {locateNo:rowData.locateNo,locno:billOmLocate.locno,selectType:"0"};
	var queryMxURLLocate = BasePath+'/bill_om_locate_dtl/dlist.json';
	billOmLocate.loadGridDataUtil('locateDtlDg', queryMxURLLocate, queryParamsLocate);
	
	var queryParamsOutstock= {locateNo:rowData.locateNo,locno:billOmLocate.locno,selectType:"1"};
	var queryMxURLOutstock = BasePath+'/bill_om_locate_dtl/dlist.json';
	billOmLocate.loadGridDataUtil('locateOutstockDtlDg', queryMxURLOutstock, queryParamsOutstock);
	
	$('#locateNo').val(rowData.locateNo);
};
//导出波次明细
billOmLocate.do_exportLocateDtl = function(){
	exportExcelBaseInfo('locateDtlDg','/bill_om_locate_dtl/do_export_dtl.htm','波次明细');
}

//手工关闭
billOmLocate.overFloc=function(){
	
	// 获取勾选的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要关闭的数据!',1);
		return;
	}
	
	var dataList = [];
//	var tipStr = "";
	$.each(checkedRows,function(index,item){
//		if(item.status != '10'){
//			tipStr = item.locateNo + "只能关闭状态为新建的单据!";
//			return;
//		}
		dataList[dataList.length] = {locno:item.locno,locateNo:item.locateNo};
	});
	
//	if(tipStr != ""){
//		alert(tipStr);
//		return;
//	}
	
	$.messager.confirm("确认","是否手工关闭单据？", function (r){
		if (r) {
			wms_city_common.loading("show","正在关闭单据......");
			var url = BasePath+'/bill_om_locate/overBillOmLocate';
			var data = {datas:JSON.stringify(dataList)};
			$.post(url, data, function(data) {
	        	if(data.result == "success"){
	        		alert(data.msg);
	                $('#dataGridJG').datagrid('load');
	            }else{
	                alert(data.msg,1);
	            }
	        	wms_city_common.loading();
	         }, "JSON").error(function() {
	        	 alert('删除失败!',1);
	        	 wms_city_common.loading();
	         });
		}
	});     
};


//删除
billOmLocate.deleteOmLocate=function(){
	// 获取勾选的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要关闭的数据!',1);
		return;
	}
	
	var dataList = [];
	var tipStr = "";
	$.each(checkedRows,function(index,item){
		if(item.status == '91'){
			tipStr = item.locateNo + "手工关闭都单据不能删除!";
			return;
		}
		dataList[dataList.length] = {locno:item.locno,locateNo:item.locateNo};
	});
	
	if(tipStr != ""){
		alert(tipStr);
		return;
	}
	
	$.messager.confirm("确认","是否删除单据？", function (r){
		if (r) {
			wms_city_common.loading("show","正在删除单据......");
			var url = BasePath+'/bill_om_locate/deleteOmLocate';
			var data = {datas:JSON.stringify(dataList)};
			$.post(url, data, function(data) {
	        	if(data.flag == "success"){
	        		alert("删除成功!");
	                $('#dataGridJG').datagrid('load');
	            }else{
	                alert(data.msg,1);
	            }
	        	wms_city_common.loading();
	         }, "JSON").error(function() {
	        	 alert('删除失败!',1);
	        	 wms_city_common.loading();
	         });
		}
	});     
};


//发单还原
billOmLocate.recoveryLocateSend=function(){
	
	// 获取勾选的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要还原的数据!',1);
		return;
	}
	
	var dataList = [];
//	var tipStr = "";
	$.each(checkedRows,function(index,item){
//		if(item.status != '10'){
//			tipStr = item.locateNo + "只能关闭状态为新建的单据!";
//			return;
//		}
		dataList[dataList.length] = {locno:item.locno,locateNo:item.locateNo,status:item.status};
	});
	
//	if(tipStr != ""){
//		alert(tipStr);
//		return;
//	}
	
	$.messager.confirm("确认","是否发单还原选中的单据？", function (r){
		if (r) {
			wms_city_common.loading("show","正在发单还原......");
			var url = BasePath+'/bill_om_locate/recoveryLocateSend';
			var data = {datas:JSON.stringify(dataList)};
			$.post(url, data, function(data) {
	        	if(data.result == "success"){
	        		alert(data.msg);
	                $('#dataGridJG').datagrid('load');
	            }else{
	                alert(data.msg,1);
	            }
	        	wms_city_common.loading();
	         }, "JSON").error(function() {
	        	 alert('发单还原失败!',1);
	        	 wms_city_common.loading();
	         });
		}
	});     
};

//初始化状态
billOmLocate.initStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=BILL_OM_LOCATE',
			{},
			billOmLocate.status,
			null);
};

//初始化用户信息
billOmLocate.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		billOmLocate.locno = data.locno;
	}); 
};
//========================委托业主========================
billOmLocate.ownerNo = {};
billOmLocate.ownerNoFormatter = function(value, rowData, rowIndex){
	return billOmLocate.ownerNo[value];
};
billOmLocate.initOwnerNo = function(data){
	$('#ownerNo').combobox({
		valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto"
	});
	billOmLocate.ownerNo = billOmLocate.tansforOwner(data);
};
billOmLocate.tansforOwner = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//========================委托业主END========================
billOmLocate.onLoadSuccess = function(data){
	if(data.rows.length > 0){
		if(data.footer[1].isselectsum){
			billOmLocate.totalItemQty = data.footer[1].totalItemQty;
			billOmLocate.totalPlanQty = data.footer[1].totalPlanQty;
			billOmLocate.totalVolumeQty = data.footer[1].totalVolumeQty;
			billOmLocate.totalWeightQty = data.footer[1].totalWeightQty;
	   	}else{
	   		var rows = $('#dataGridJG').datagrid('getFooterRows');
	   		rows[1]['totalItemQty'] = billOmLocate.totalItemQty;
		   	rows[1]['totalPlanQty'] = billOmLocate.totalPlanQty;
		   	rows[1]['totalVolumeQty'] = billOmLocate.totalVolumeQty;
		   	rows[1]['totalWeightQty'] = billOmLocate.totalWeightQty;
		   	$('#dataGridJG').datagrid('reloadFooter');
	   	}
	}
}
//========================初始化信息======================================
$(document).ready(function(){
	billOmLocate.initCurrentUser();
	billOmLocate.initStatus();
	//委托业主
	billOmLocate.ajaxRequest(BasePath+'/entrust_owner/get_biz',{},false,billOmLocate.initOwnerNo);
	
	var objs = [];
	objs.push({"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')});
	wms_city_common.loadSysNo4Cascade(objs);
	
	
	$('#locateDtlDg').datagrid({
		'onLoadSuccess':function(data){
			if(data.rows.length > 0){
				if(data.footer[1].isselectsum){
					billOmLocate.planQty = data.footer[1].planQty;
					billOmLocate.locatedQty = data.footer[1].locatedQty;
					billOmLocate.outstockQty = data.footer[1].outstockQty;
					billOmLocate.recheckQty = data.footer[1].recheckQty;
			   	}else{
			   		var rows = $('#locateDtlDg').datagrid('getFooterRows');
				   	rows[1]['planQty'] = billOmLocate.planQty;
				   	rows[1]['locatedQty'] = billOmLocate.locatedQty;
				   	rows[1]['outstockQty'] = billOmLocate.outstockQty;
				   	rows[1]['recheckQty'] = billOmLocate.recheckQty;
				   	$('#locateDtlDg').datagrid('reloadFooter');
			   	}
			}
		}
	});
	
	$('#locateOutstockDtlDg').datagrid({
		'onLoadSuccess':function(data){
			if(data.rows.length > 0){
				if(data.footer[1].isselectsum){
					billOmLocate.planQty = data.footer[1].planQty;
					billOmLocate.locatedQty = data.footer[1].locatedQty;
					billOmLocate.outstockQty = data.footer[1].outstockQty;
					billOmLocate.recheckQty = data.footer[1].recheckQty;
			   	}else{
			   		var rows = $('#locateOutstockDtlDg').datagrid('getFooterRows');
				   	rows[1]['planQty'] = billOmLocate.planQty;
				   	rows[1]['locatedQty'] = billOmLocate.locatedQty;
				   	rows[1]['outstockQty'] = billOmLocate.outstockQty;
				   	rows[1]['recheckQty'] = billOmLocate.recheckQty;
				   	$('#locateOutstockDtlDg').datagrid('reloadFooter');
			   	}
			}
		}
	});
	$('#startCreatetmCondition').datebox('setValue',getDateStr(-2));
});

billOmLocate.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billOmLocate.printDetail = function(){
	
	var resultData;
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].locateNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_om_locate_dtl/printDetail',
        data: {
        	keys:keys.join(","),
        	locno:billOmLocate.locno
        },
        success: function(result){
        	resultData = result;
		}
      });
     if(resultData.result!="success"){
     	alert(resultData.msg);
     	return;
     }else{
    	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
      	if(LODOP==null){
      		return;
      	}
 		LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
		var result = resultData.data;
		for(var i=0,length=result.length;i<length;i++){
			LODOP.NewPage();
	     	var html = result[i];
			LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
			LODOP.ADD_PRINT_BARCODE(10,10,250,40,"128A",rows[i].locateNo);
		}
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
		LODOP.PREVIEW();
     }
};

billOmLocate.preColNamesDtl1 = [
       		                {field:'storeNo',title:'客户编号',width:110},
				 			{field:'expNo',title:'发货通知单号',width:140},
				 			{field:'itemNo',title:'商品编码',width:135}
                           ];
billOmLocate.endColNamesDtl1 = [
       		                      {title:"合计",field:"allCount"}
                               ] ;
billOmLocate.sizeTypeFiledName = 'sizeKind';

billOmLocate.printDetail4SizeHorizontal = function(){
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	wms_city_common.loading("show","正在加载......");
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].locateNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_om_locate_dtl/printDetail4SizeHorizontal',
        data: {
        	keys:keys.join(","),
 	        preColNames:JSON.stringify(billOmLocate.preColNamesDtl1),
 	        endColNames:JSON.stringify(billOmLocate.endColNamesDtl1),
 	        sizeTypeFiledName:billOmLocate.sizeTypeFiledName
        },
        success: function(data){
        	wms_city_common.loading();
        	if(data.result=="success"){
        		print4SizeHorizontal(data);
        	}else{
        		alert(data.msg,2);
        	}
        	
		}
    });
};

function print4SizeHorizontal(data){
	//***************测试数据S****************
	/*var returnCols = data.rows[0].rows;
	var row = returnCols[0];
	for(var idx=0;idx<80;idx++){
		returnCols[returnCols.length] = row;
	}
	data.rows[0].rows = returnCols;*/
	//***************测试数据S****************
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
  	if(LODOP==null){
  		return;
  	}
	LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
	var result = data.rows;
	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
	for(var i=0,length=result.length;i<length;i++){
		LODOP.NewPage();
		
		var headHtml =  getHtmlByTemplateHead(result[i]);
		var bodyHtml = getHtmlByTemplateTable(result[i]);
		var footerHtml = getHtmlByTemplateFooter(result[i]);
		var tableTitleRowNum = getTableTitleRowNum(result[i]);
		var rowH = (tableTitleRowNum-1)*23;
		
		//设置表格内容
		LODOP.ADD_PRINT_TABLE(110,0,"100%",390-rowH,strStyle+bodyHtml);
		LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
		
	   //设置表格头部
		LODOP.ADD_PRINT_HTM(0,0,"100%",110,headHtml);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		
		//设置表格底部
		LODOP.ADD_PRINT_HTM(730,0,"100%",50,footerHtml);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);	
		
	 	//设置条码
		LODOP.ADD_PRINT_BARCODE(30,10,250,40,"128A",result[i].main.locateNo);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		
		
		
		LODOP.NewPageA();
	}
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
}
function getTableTitleRowNum(data){
	return data.header.returnCols.length;
}
function getHtmlByTemplateHead(data){
	var html = "<table style='width:100%;'>";
	html += "<tr style='height:60px;'><td style='text-align:center;font-size:30px;' colspan='2'>拣货调度波次</td></tr>";
	html += "<tr><td>拣货波次单号："+data.main.locateNo+"</td><td>总合计："+data.main.sumQty+"</td></tr>";
	html += "</table>";
	return html;
}
function getHtmlByTemplateTable(data){
	var html ="<table border='1' cellspacing='0' cellpadding='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'><thead>";
	var titleinfo = data.header;
	var rowInfo = data.rows;
	var footer = data.footer[0];
	//最大的尺码数量
	var maxType = titleinfo.maxType;
	//组装标题信息，包括所有的尺码，标题
	for(var i=0,length=titleinfo.returnCols.length;i<length;i++){
		html = html+"<tr>";
		for(var j=0,length2=titleinfo.returnCols[i].length;j<length2;j++){
			 var td = titleinfo.returnCols[i][j];
			 var title = td.title==""?"&nbsp;":td.title;
			html = html+"<td width="+td.width+" rowspan="+td.rowspan+" colspan="+td.colspan+">"+title+"</td>";
		}
		html = html+"</tr>";
	}
	html += "</thead>";
	//组装数据信息
	var rows = rowInfo;
	for(var k=0,length = rows.length;k<length;k++){
		var temdata = rows[k];
		html = html+"<tr><td>"+temdata.storeNo+"</td><td>"+temdata.expNo+"</td><td>"+temdata.itemNo+"</td><td>"+temdata.sizeKind+"</td>";
		for(var n=1;n<=maxType;n++){
			if(temdata["v"+n]!=undefined){
				html =html+"<td>"+temdata["v"+n]+"</td>";
			}else{
				html =html+"<td></td>";
			}
		}
		html = html+"<td>"+temdata.allCount+"</td></tr>";
	}
	//组装合计信息
	html = html+"<tr><td>"+footer.itemNo+"</td><td></td><td></td><td></td>";
	for(var n=1;n<=maxType;n++){
			if(footer["v"+n]!=undefined){
				html =html+"<td>"+footer["v"+n]+"</td>";
			}else{
				html =html+"<td></td>";
			}
		}
	html = html+"<td>"+footer.allCount+"</td></tr>";	
	html = html+"</table>";
	return html;
	
}
function getHtmlByTemplateFooter(data){
	var html = "<table border='0' cellspacing='0' cellpadding='0' width='100%'>";
	html =html+ "<tr><td style='text-align:right;'>制单人："+data.main.creator+"&nbsp;&nbsp;&nbsp;&nbsp;制单时间："+data.main.creattm+"</td></tr>";
	html =html+ "</table>";
	return html;
}

billOmLocate.printDetailSummary = function(data){
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	wms_city_common.loading("show","正在加载......");
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].locateNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_om_locate_dtl/printDetailSummary',
        data: {
        	keys:keys.join(",")
        },
        success: function(data){
        	wms_city_common.loading();
        	if(data.result=="success"){
        		getPrintDetailSummaryHtml(data);
        	}else{
        		alert(data.msg,2);
        	}
        	
		}
    });
}

function getPrintDetailSummaryHtml(data){
	//***************测试数据S****************
	/*var tem = data.rows[0].list[0];
	var array = [];
	for(var idx=0;idx<100;idx++){
		array[idx] = tem;
	}
	data.rows[0].list = array;*/
	//***************测试数据E****************
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
  	if(LODOP==null){
  		return;
  	}
	LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
	var result = data.rows;
	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
	for(var i=0,length=result.length;i<length;i++){
		LODOP.NewPage();
		var headHtml =  getDetailSummaryHead(result[i]);
		var bodyHtml = getDetailSummaryTable(result[i]);
		var bottomHtml = getDetailSummaryBottom(result[i]);
		//设置表格内容
		LODOP.ADD_PRINT_TABLE(110,0,"100%",570,strStyle+bodyHtml);
		//LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
		
	   //设置表格头部
		LODOP.ADD_PRINT_HTM(0,0,"100%",90,headHtml);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		
		//设置表格底部
		LODOP.ADD_PRINT_HTM(725,0,"100%",40,bottomHtml);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		
	 	//设置条码
		LODOP.ADD_PRINT_BARCODE(10,10,250,40,"128A",result[i].main.locateNo);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		
		LODOP.NewPageA();
	}
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
}


function getDetailSummaryHead(data){
	var html = "<table style='width:100%;'>";
	html += "<tr style='height:60px;'><td style='text-align:center;font-size:30px;' colspan='2'>拣货调度波次汇总</td></tr>";
	html += "<tr><td>拣货波次单号："+data.main.locateNo+"</td></tr>";
	html += "</table>";
	return html;
}
function getDetailSummaryTable(data){
	var html ="<table border='1' cellspacing='0' cellpadding='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'>";
	html = html+"<thead><tr><td>客户编码</td><td colspan='2'>客户名称</td><td>计划数量</td><td>拣货数量</td><td>复核数量</td></tr></thead>";
	for(var i=0,length=data.list.length;i<length;i++){
		html = html+"<tr>";
		html = html+"<td>"+data.list[i].storeNo+"</td><td colspan='2'>"+data.list[i].storeName+"</td><td>"+data.list[i].planQty+"</td><td>"+data.list[i].realQty+"</td><td>"+data.list[i].recheckQty+"</td>";
		html = html+"</tr>";
	}
	html = html + "<tfoot><tr><td style='text-align:center;' colspan='2'><font tdata='pageNO' format = '#' align='center'>第#页</font>&nbsp;/&nbsp;<font format='#' tdata='pageCount'>共#页</font></td><td style='width:40px;text-align:center;'>合计</td><td>"+data.main.sumPlanQty+"</td><td>"+data.main.sumRealQty+"</td><td>"+data.main.sumRecheckQty+"</td></tr></tfoot>";
	html = html+"</table>";
	return html;
	
}
function getDetailSummaryBottom(data){
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='width:70%;text-align:right;'>打印人："+data.main.creator+"</td><td style='text-align:right;'>打印时间："+data.main.creattm+"</td></tr>";
	html += "</table>";
	return html;
}