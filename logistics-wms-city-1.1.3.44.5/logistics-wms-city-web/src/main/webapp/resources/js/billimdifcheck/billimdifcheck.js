function parsePage(){
        //工具条快捷搜索
        toolSearch();
};
var billimdifcheck = {};

billimdifcheck.locno;

billimdifcheck.curRowIndex = -1;
billimdifcheck.preType = 0;

billimdifcheck.exportDataGridDtl1Id = 'moduleView';
billimdifcheck.preColNamesDtl1 = [
		                   {title:"商品编码",field:"itemNo",width:150},
		                   /*{title:"颜色",field:"colorNoStr",width:80},
		                   {title:"商品名称",field:"itemName",width:150},*/
		                   {title:"箱号",field:"boxNo",width:120},
		                   {title:'装车单号',field:"deliverNo",width:120},
		                   {title:"品牌",field:"brandNoStr",width:100}
                    ];
billimdifcheck.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billimdifcheck.sizeTypeFiledName = 'sizeKind';

billimdifcheck.getDate = function(n){
	var nowDate = new Date();
	var newDate = new Date();
	var newTimes = nowDate.getTime() + (n*24*3600000);
	newDate.setTime(newTimes);
	var y = newDate.getFullYear();
	var m = newDate.getMonth()+1;
	var d = newDate.getDate();
	var dateStr = y+"-";
	dateStr += (m<10?("0"+m):(m))+"-";
	dateStr += (d<10?("0"+d):(d));
	return dateStr;
};


//将数组封装成一个map
billimdifcheck.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};


billimdifcheck.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  async:false,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//查询预到货通知单
billimdifcheck.searchBillImImport = function(){
	
	$('#searchForm input[name=locno]').val(billimdifcheck.locno);
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/billimimport/list.json';
	
    //3.加载明细
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

//清楚查询条件
billimdifcheck.searchBillImImportClear = function(){
	$('#supplierNoCondition').combobox("loadData",[]);
	$('#brandNo').combobox("loadData",[]);
	$('#searchForm').form("clear");
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

//获取明细信息
billimdifcheck.setDetail = function(rowData,fromObj){
	$("input[id=importNo]",fromObj).val(rowData.importNo).focus();
	
	$("input[id=transNo]",fromObj).val(rowData.transNo);
	
	
	if(rowData.supplierName!='' && rowData.supplierName!=null){
		$("input[id=supplierNo]",fromObj).combo('setText',rowData.supplierNo+'→'+rowData.supplierName);
	}else{
		$("input[id=supplierNo]",fromObj).combobox("select",rowData.supplierNo);
	}
	
	
	$("input[id=supplierNo]",fromObj).combobox('disable');
	$("input[id=supplierNoHide]",fromObj).attr("disabled",false);
	$("input[id=supplierNoHide]",fromObj).val(rowData.supplierNo);
	
	$("input[id=sysNo]",fromObj).combobox("select",rowData.sysNo);
	$("input[id=sysNo]",fromObj).combobox('disable');
	$("input[id=sysNoHide]",fromObj).attr("disabled",false);
	$("input[id=sysNoHide]",fromObj).val(rowData.sysNo);
	
	$("input[id=ownerNo]",fromObj).combobox("select",rowData.ownerNo);
	$("input[id=ownerNo]",fromObj).combobox('disable');
	$("input[id=ownerNoHide]",fromObj).attr("disabled",false);
	$("input[id=ownerNoHide]",fromObj).val(rowData.ownerNo);
	
	$("input[id=businessType]",fromObj).combobox("select",rowData.businessType);
	$("input[id=businessType]",fromObj).combobox('disable');
	$("input[id=businessTypeHide]",fromObj).attr("disabled",false);
	$("input[id=businessTypeHide]",fromObj).val(rowData.businessType);
	//$('input:radio[name=adjustBoard]')[rowData.adjustBoard].checked = true;
	$("input[id=orderDate]",fromObj).datebox("setValue",rowData.orderDate);
	
	$("input[id=requestDate]",fromObj).datebox("setValue",rowData.requestDate);
	
	$("input[id=orderDateEdit]",fromObj).datebox("setValue",rowData.orderDate);
	
	$("input[id=requestDateEdit]",fromObj).datebox("setValue",rowData.requestDate);
	
	$("input[id=poNo]",fromObj).val(rowData.sPoNo);
	
	$("input[id=importRemark]",fromObj).val(rowData.importRemark);
	
	$("input[id=creator]",fromObj).val(rowData.creator);
	$("input[id=createtm]",fromObj).val(rowData.createtm);
	$("input[id=editor]",fromObj).val(rowData.editor);
	$("input[id=edittm]",fromObj).val(rowData.edittm);
	
	
};

//差异验收--手工关闭
billimdifcheck.overFlocByDif=function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要关闭的记录!',1);
		return;
	}
		
	var keyStr = [];
	var ownerNo ;
	var locNo;

	$.each(checkedRows, function(index, item){
		keyStr.push(item.importNo);
		if(index==0){
			ownerNo = item.ownerNo;
			locNo = item.locno;
		}
	});
	
    var url = BasePath+'/billimimport/overFlocByDif';
	var data={
	    "keyStr":keyStr.join(","),
	    "ownerNo":ownerNo,
	    "locNo":locNo
	};
	
	$.messager.confirm("确认","手工关闭以后该单据将不允许继续验货，是否确定？", function (r){  
        if (r) {
        	wms_city_common.loading("show","正在关单......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					 wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('单据关闭成功!');
        				 billimdifcheck.searchBillImImport();
        		 	}else if(data.result=='fail'){
			        	alert(data.returnMsg,1);
				     }else{
        			 	alert('单据关闭失败,请联系管理员!',2);
        		 	}
				  }
			});
        }  
    });	
};

//分批上传
billimdifcheck.bathUpload=function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请至少选择一条记录!',1);
		return;
	}
		
	var keyStr = [];
	var locNo = '';
	var ownerNo ;

	$.each(checkedRows, function(index, item){
		keyStr.push(item.importNo);
		if(index==0){
			locNo = item.locno;
			ownerNo = item.ownerNo;
		}
	});
	
    var url = BasePath+'/billimimport/bathUpload';
	var data={
	    "importNoStr":keyStr.join(","),
	    "ownerNo":ownerNo,
	    "locno":locNo
	};
	
	$.messager.confirm("确认","你确定要分批上传这"+checkedRows.length+"条数据吗？", function (r){  
        if (r) {
        	wms_city_common.loading("show","正在上传......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					  wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('单据分批上传成功!');
        				 billimdifcheck.searchBillImImport();
        		 	}else if(data.result=='error'){
        		 		alert(data.msg,1);
				     }else{
				    	alert('单据分批上传异常,请联系管理员!',2);
        		 	}
				  }
			});
        }  
    });	
};

//加载仓库信息
billimdifcheck.loadLoc = function(formObj){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billimdifcheck.locno = data.locno;
		}
	});
};
//========================业务类型BEGIN========================
billimdifcheck.businessTypes = {};
billimdifcheck.businessTypesFormatter = function(value, rowData, rowIndex){
	return billimdifcheck.businessTypes[value];
};

billimdifcheck.loadSupplierNo = function(type){
	if(type=="0"){
		wms_city_common.comboboxLoadFilter(
				["supplierNoCondition"],
				'supplierNo',
				'supplierName',
				'valueAndText',
				false,
				[true],
				BasePath+'/supplier/get_all',
				{},
				null,
				null);
	}else if(type=="4"){
		wms_city_common.comboboxLoadFilter(
				["supplierNoCondition"],
				'storeNo',
				'storeName',
				'valueAndText',
				false,
				[true],
				BasePath+'/store/get_all?storeType=22',
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				["supplierNoCondition"],
				'storeNo',
				'storeName',
				'valueAndText',
				false,
				[true],
				null,
				{},
				null,
				null);
	}
};

//========================业务类型END========================

billimdifcheck.clearAll = function(fromObj){
	fromObj.form("clear");
	//$("input[id=importNo]",fromObj).attr('readOnly',false);
};
billimdifcheck.initBrandClass = function(){
	$('#brandClass').combobox({
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=BRAND_CLASS',
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:150
	  });
};


//格式化状态
billimdifcheck.columnStatusFormatter = function(value, rowData, rowIndex){
	return billimdifcheck.init_status_Text[value];
};

//状态
billimdifcheck.init_IMPORT_STATUS = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IMPORTOR_STATUS',
		success : function(data) {
			billimdifcheck.init_status_Text=billimdifcheck.converStr2JsonObj(data);
			$('#statusCondition').combobox({
				 data:data,
			     valueField:"itemvalue",
			     textField:"valueAndText",
			     panelHeight:150,
			     loadFilter:function(data){
	    			if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
	       	 		}
	    			return data;
	   			 } 
			  });
			
		}
	});
    
};
$(document).ready(function(){
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["ownerNoCondition","ownerNo"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[true,false],
			BasePath+'/entrust_owner/get_biz',
			{},
			wms_city_common.init_Owner_Text,
			null);
	billimdifcheck.loadLoc();
	//初始化供应商(明细)
	wms_city_common.comboboxLoadFilter(
			["supplierNo"],
			'supplierNo',
			'supplierName',
			'valueAndText',
			false,
			[false],
			BasePath+'/supplier/get_all',
			{},
			wms_city_common.init_Supplier_Text,
			null);
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNo]',$('#dataForm'))},//新增
			{"sysNoObj":$('input[id=sysNo]',$('#dataEditForm'))},//修改
			{"sysNoObj":$('input[id=sysNo]',$('#dataViewForm'))},//详情
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	//属性
	//billimdifcheck.businessTypeData();
	//初始化业务类型
	wms_city_common.comboboxLoadFilter(
			["businessTypeCondition","businessType"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IMIMPORT_BUSINESS_TYPE',
			{},
			billimdifcheck.businessTypes,
			null);
	billimdifcheck.init_IMPORT_STATUS();//初始化状态
	
	
	//$("#orderDateBeginCondition").datebox('setValue',billimdifcheck.getDate(-6));
	//$("#orderDateEndCondition").datebox('setValue',billimdifcheck.getDate(0));
	$("#createtmBeginCondition").datebox('setValue',billimdifcheck.getDate(-2));
	
	$('#moduleView_dtl').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billimdifcheck.poQty = data.footer[1].poQty;
		   				billimdifcheck.receiptQty = data.footer[1].receiptQty;
		   				billimdifcheck.importQty = data.footer[1].importQty;
		   				billimdifcheck.differQty = data.footer[1].differQty;
		   			}else{
		   				var rows = $('#moduleView_dtl').datagrid('getFooterRows');
			   			rows[1]['poQty'] = billimdifcheck.poQty;
			   			rows[1]['receiptQty'] = billimdifcheck.receiptQty;
			   			rows[1]['importQty'] = billimdifcheck.importQty;
			   			rows[1]['differQty'] = billimdifcheck.differQty;
			   			$('#moduleView_dtl').datagrid('reloadFooter');
		   			}
		   		}
			}
		);

});

//导出
exportExcel=function(){
	exportExcelBaseInfo('dataGridJG','/billimimport/do_export.htm','预到货通知单导出');
};

/**
 * ===================================尺码横排======================================================
 */

//弹出详情页面
billimdifcheck.showView = function(rowData,rowIndex){
	
	//清空datagrid数据
	$('#moduleView').datagrid('clearData');
	
	var fromObj=$('#dataViewForm');
	
	billimdifcheck.clearAll(fromObj);
	
	//仓库编码设置为只读，不可修改
	$('input[name=importNo]',fromObj).attr("readOnly",true);

	$('#detailViewDiv').show();
	
	//设置信息
	billimdifcheck.setDetail(rowData,fromObj);
	//明细信息
	billimdifcheck.loadDetail(rowData,rowIndex);
	$('#moduleView_dtl').datagrid({'url':BasePath+'/bill_im_import_dtl/dtllist.json?locno='+billimdifcheck.locno+'&importNo='+$('input[name=importNo]',fromObj).val()+'&sort=od.box_no,od.item_no,od.size_no','pageNumber':1 });
	//弹窗
	$("#showViewDialog").window('open');
};


billimdifcheck.loadDetail = function(rowData,rowIndex){
	
	billimdifcheck.curRowIndex=rowIndex;
	
	billimdifcheck.initGridHeadAndData(rowData);
   
};


//同时加载表头和表体
billimdifcheck.initGridHeadAndData = function(rowData){
	
	 var beforeColArr = billimdifcheck.preColNamesDtl1;
	 var afterColArr = billimdifcheck.endColNamesDtl1; 
	 var columnInfo = billimdifcheck.getColumnInfo(rowData.sysNo,beforeColArr,afterColArr);
	 
	 var url = BasePath+'/bill_im_import_dtl/queryBillImImportDtlDTOlList';
	 var queryParams = {importNo:rowData.importNo,sysNo:rowData.sysNo,ownerNo:rowData.ownerNo};
	 
	 $("#moduleView").datagrid({
		 queryParams:queryParams,
		 url:url,
         columns:columnInfo.columnArr
     }); 
};

billimdifcheck.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billimdifcheck.sizeTypeFiledName
        },
        cache: true,
        async: false,
        success: function(returnData){
         	resultData.columnArr = returnData.returnCols;
         	resultData.startType = returnData.startType;
         	resultData.maxType = returnData.maxType;
		}
      });
      return resultData;
};
function getHtmlByTemplate(data,importNo,supplierName,sDate,expDate,transNo){
	var sizeCols = data["sizeCols"];
	var colLen = 6+sizeCols.length;
	var colArray = [];
	var colMap = {};
	colArray[0] = "brandNoStr";
	colMap["brandNoStr"] = "品牌";
	colArray[1] = "itemNo";
	colMap["itemNo"] = "商品编码";
	colArray[2] = "itemName";
	colMap["itemName"] = "商品名称";
	for(var index=3;index<colLen-3;index++){
		colArray[index] = sizeCols[index-3];
		colMap[sizeCols[index-3]] = sizeCols[index-3];
	}
	colArray[colLen-3] = "allCount";
	colMap["allCount"] = "总数";
	colArray[colLen-2] = "unitName";
	colMap["unitName"] = "单位";
	colArray[colLen-1] = "boxNo";
	colMap["boxNo"] = "箱号";
	
	
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='3'>预到货通知单</td></tr>";
	html += "<tr style='height:40px;'><td colspan='3' style='width:60%;'>单据号条码：</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;据&nbsp;&nbsp;&nbsp;&nbsp;号："+importNo+"</td><td>源预到货通知单："+transNo+"</td><td>&nbsp;</td></tr>";
	html += "<tr><td>供&nbsp;&nbsp;&nbsp;&nbsp;应&nbsp;&nbsp;&nbsp;&nbsp;商："+supplierName+"</td><td>源&nbsp;&nbsp;发&nbsp;&nbsp;货&nbsp;&nbsp;日&nbsp;&nbsp;期："+sDate+"</td><td style='text-align:right;'>预到货日期："+expDate+"</td></tr>";
	html += "</table>";
	html += "<table border='0' cellpadding='3' cellspacing='1' width='100%' style='background-color: #000;'><tr bgcolor='#fff'>";
	for(var index=0;index<colLen;index++){
		html += "<td>"+colMap[colArray[index]]+"</td>";
	}
	html += "</tr>";
	var temp;
	var total = 0;
	for(var i=0;i<data["total"];i++){
		temp = null;
		temp = data["rows"][i];
		html += "<tr bgcolor='#fff'>";
		for(var j=0;j<colLen;j++){
			if(temp[colArray[j]]){
				html += "<td>"+temp[colArray[j]]+"</td>";
			}else{
				if('allCount' == colArray[j]){
					html += "<td>0</td>";
				}else{
					html += "<td>&nbsp;</td>";
				}
			}
			
		}
		html += "</tr>";
		total += temp["allCount"];
	}
	html += "</table>";
	html += "<table  style='width:100%;'>";
	html += "<tr><td style='text-align:right;width:70%;'>总箱数："+data["total"]+"</td><td style='text-align:center;'>件数："+total+"</td></tr>";
	html += "</table>";
	return html;
}
billimdifcheck.printBillImImport = function(){
	
	var data = {};
	var fromObj = $("#dataViewForm");
	var importNo = $('input[name=importNo]',fromObj).val();
	var supplierName = $("input[id=supplierNo]",fromObj).combobox("getText");
	var sDate = $('input[name=orderDate]',fromObj).val();
	var ownerNo = $('input[name=ownerNo]',fromObj).val();
	var expDate = $('input[name=requestDate]',fromObj).val();
	var sysNo = $('input[name=sysNo]',fromObj).val();
	var transNo = $('input[name=transNo]',fromObj).val();
	var url = BasePath+'/bill_im_import_dtl/queryBillImImportDtlByPrint';
	$.ajax({
        type: 'POST',
        url: url,
        data: {
        		importNo:importNo,
        		ownerNo:ownerNo,
        		sysNo:sysNo
        },
        cache: true,
        async: false,
        success: function(returnData){
        	data = returnData;
		}
      });
	//***************************************
	
	if(data["total"]<=0){
		alert("没有需要打印的数据!");
		return;
	}
	
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	if(LODOP==null)return;
	var html = getHtmlByTemplate(data, importNo, supplierName, sDate, expDate, transNo);
	LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
	LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
	LODOP.ADD_PRINT_BARCODE(53,110,250,40,"128A",importNo);
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
	
};