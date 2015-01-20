var billomoutstock = {};
billomoutstock.sumData = {};
billomoutstock.locno;

billomoutstock.onLoadSuccess = function(data){
	if(data.footer[1] != null){
		billomoutstock.mainSumFoot = data.footer[1];
	}else{
			var rows = $('#dataGridJG').datagrid('getFooterRows');
   			rows[1] = billomoutstock.mainSumFoot;
			$('#dataGridJG').datagrid('reloadFooter');
	}
};

$(document).ready(function(){
	//加载仓别
	billomoutstock.initLoc();
	//初始化数据
	//billomoutstock.loadDataGrid();
	//初始化下拉框
	billomoutstock.initStatusData();
	billomoutstock.initTaskTypeData();
	billomoutstock.initOutstockName();
	billomoutstock.initSendType();
	
	var objs = [];
	objs.push({"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')});
	wms_city_common.loadSysNo4Cascade(objs);
	
	$('#dataGridJG_Detail').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				//billomoutstock.sumData.itemQty = data.footer[1].itemQty;
		   				//billomoutstock.sumData.realQty = data.footer[1].realQty;
		   				billomoutstock.sumData = data.footer[1];
		   			}else{
		   				var rows = $('#dataGridJG_Detail').datagrid('getFooterRows');
			   			//rows[1]['itemQty'] = billomoutstock.sumData.itemQty;
			   			//rows[1]['realQty'] = billomoutstock.sumData.realQty;
		   				rows[1] = billomoutstock.sumData;
			   			$('#dataGridJG_Detail').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	

	$('#print').click(function(){
		$('#print_menu').menu('show', {    
			  left: 304,    
			  top: 30    
			});
	});
	$('#createTmStart').datebox('setValue',getDateStr(-2));
});

//初始化拣货人员
billomoutstock.initOutstockName = function(){
	//获取用户信息
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			billomoutstock.workerLocs = billomoutstock.converStr2JsonObj2(data);
			$('#assignName').combobox({
			    data:data,
			    valueField:'workerNo',    
			    textField:'unionName',
			    panelHeight:150
			}).combobox("select",data[0].workerNo);
		}
	});
};

billomoutstock.initLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billomoutstock.locno = data.locno;
		}
	});
};

billomoutstock.initStatusData = function(){
	wms_city_common.comboboxLoadFilter(
			["searchStatus","status","statusView"],
			null,
			null,
			null,
			true,
			[true,false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OUTSTOCK_STATUS',
			{},
			billomoutstock.status,
			null);
};


billomoutstock.initSendType = function(){
	wms_city_common.comboboxLoadFilter(
			["outstockSendType"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OM_OUTSTOCK_SENDTYP',
			{},
			billomoutstock.outstockSendType,
			null);
};

billomoutstock.taskType = {};
billomoutstock.initTaskTypeData = function(){
//	$('#searchLabelNo').combobox({
//	     valueField:"value",
//	     textField:"text",
//	     data:billomoutstock.taskTypeData,
//	     panelHeight:"auto"
//	  });
	wms_city_common.comboboxLoadFilter(
			["searchLabelNo"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OM_OUTSTOCK_TASKTYPE',
			{},
			billomoutstock.taskType,
			null);
};

billomoutstock.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].workerNo +"\":\""+data[i].workerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billomoutstock.edit = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status!="10"){
		alert('非建单状态的不能修改',1);
		return;
	}else{
		billomoutstock.detail(checkedRows[0],"edit");
	}
};
billomoutstock.closeWindow = function(id){
	$('#'+id).window('close');
};

billomoutstock.detailView = function(rowData){
	$('#detailDialogView').window('open');
	$("#detailForm_view").form("clear");
	$("#detailForm_view").form("load",rowData);
	$("#outstockNoView").attr("disabled",true);
	$("#statusView").combobox("disable");
	$("#statusView").combobox("setValue",rowData.status);
	enableLinkButton("detail-export");
	/*$('#dataGridJG_Detail').datagrid(
		{
			'url':BasePath+'/bill_om_outstock_dtl/dlist.json?locno='+billomoutstock.locno+"&outstockNo="+rowData.outstockNo+"&page=1",
	    }
	);*/
	$('#dataGridJG_Detail').datagrid( 'options' ).url=BasePath+'/bill_om_outstock_dtl/dlist.json?locno='+billomoutstock.locno+"&outstockNo="+rowData.outstockNo;
	$('#dataGridJG_Detail').datagrid( 'load' );
	var queryParams = {};
	queryParams.outstockNo = rowData.outstockNo;
	queryParams.locno = rowData.locno;
	/****尺码横排****/
	var resultColumns = billomoutstock.getColumnInfo(queryParams);
	if(resultColumns==null){
		 alert("没有查询到数据",1);
		 return;
	 }
	var queryMxURL=BasePath+'/bill_om_outstock_dtl/selectItemDetail4SizeHorizontal';
	$("#moduleView").datagrid({
		queryParams : queryParams,
		url : queryMxURL,
		columns : resultColumns.returnCols
	}); 
};
billomoutstock.detail = function(rowData,type){
	if(type=="edit"){
		$("#showDialog").dialog({title:"拣货单修改"});
//		disableLinkButton("btn-export");
	}else{
		$("#showDialog").dialog({title:"拣货单明细"});
//		enableLinkButton("btn-export");
	}
	$('#showDialog').window('open');
	$("#detailDialog").form("clear");
	$("#detailDialog").form("load",rowData);
	$("#outstockNo").attr("disabled",true);
	$("#status").combobox("disable");
	$("#status").combobox("setValue",rowData.status);
	$("#outstockNo").val(rowData.outstockNo);
	//仓别
	var locno = rowData.locno;
	
	//判断单据状态
	if(rowData.status=="10"&&type=="edit"){
		$("#dataGridJG_Detail_div").hide();
		$("#dataGridJG_Detail_div2").show();
		//$('#checkBtn').linkbutton('enable');
		enableLinkButton("checkBtn");
		enableLinkButton("checkPlanBtn");
		$('#dataGridJG_Detail2').datagrid(
			{
				'url':BasePath+'/bill_om_outstock_dtl/list.json?locno='+rowData.locno+"&outstockNo="+rowData.outstockNo,
		   		'onLoadSuccess':function(){
		   			var curObj = $('#dataGridJG_Detail2');
		   			var rows = curObj.datagrid("getRows");
					$.each(rows,function(index,item){
						curObj.datagrid('beginEdit', index);
						var ed = curObj.datagrid('getEditor', {index:index,field:'realQty'});
						//$(ed.target).numberbox('setValue', item.itemQty);
						var edOutstockName = curObj.datagrid('getEditor', {index:index,field:'outstockName'});
						$(edOutstockName.target).combobox('setValue', item.assignName);
					});
		   		}
		    }
		);
		
	}else{		
		/****明细****/
		$("#dataGridJG_Detail_div").show();
		$("#dataGridJG_Detail_div2").hide();
		//$('#checkBtn').linkbutton('disable');
		disableLinkButton("checkBtn");
		disableLinkButton("checkPlanBtn");
		$('#dataGridJG_Detail').datagrid(
			{
				'url':BasePath+'/bill_om_outstock_dtl/dlist.json?locno='+rowData.locno+"&outstockNo="+rowData.outstockNo,
		    }
		);
		
	}
};

billomoutstock.getColumnInfo = function(params) {
	var beforeColArr = billomoutstock.preColNames;
	var afterColArr = billomoutstock.endColNames; 
	var tempUrl = BasePath+'/bill_om_outstock_dtl/initHead';
    params.preColNames = JSON.stringify(beforeColArr);
    params.endColNames = JSON.stringify(afterColArr);
    params.sizeTypeFiledName = billomoutstock.sizeTypeFiledName;
    var resultColumns;
    wms_city_common.loading("show","正在加载尺码头信息");
    $.ajax({
        type: 'POST',
        url: tempUrl,
        data:params,
        cache: true,
        async: false,
        success: function(returnData){
        	wms_city_common.loading();
        	resultColumns = returnData.header;
		}
      });
     return  resultColumns;
};
billomoutstock.exportDataGridDtl1Id = 'moduleView';
billomoutstock.preColNames = [
                           {field:'sCellNo',title:'来源储位',width:150},
		                   {field:"storeName",title:"店铺名称",width:150},
		                   {field:'itemNo',title:'商品编码',width:150,align:'left'},
						   {field:'itemName',title:'商品名称',width:150,align:'left'},
		                   {field:"colorName",title:"颜色",width:80},
		                   {field:"scanLabelNo",title:"箱号",width:150},
		                   {field:'outstockName',title : '实际拣货人',width : 80,align:'left'},
		                   {field:'outstockNameCh',title : '实际拣货人名称',width : 80,align:'left'}
                    ];
billomoutstock.endColNames = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billomoutstock.sizeTypeFiledName = 'sizeKind';

//发单窗口
billomoutstock.openSendOrder = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	var tipStr = "";
	$.each(checkedRows,function(index,item){
		if(item.status != "10"){
			tipStr = item.outstockNo;
			return;
		}
	});
	if(tipStr!=""){
		alert(tipStr+"只能选择【建单】状态进行发单!");
		return;
	}
	
	if(checkedRows.length < 1){
		alert('请选择要发单的记录!',1);
		return;
	}else if(checkedRows[0].status!="10"){
		alert('非建单状态的不能发单',1);
		return;
	}
	$('#check_user_dialog').window('open');
};

//发单
billomoutstock.sendOrder = function(){
	
	var assignName = $('#assignName').combobox('getValue');
	var ans = $('#assignName').combobox('getData');
	var assignNameCh = '';
	for(var idx=0;idx<ans.length;idx++){
		if(ans[idx].workerNo == assignName){
			assignNameCh = ans[idx].workerName;
			break;
		}
	}
	if(assignName==""||assignName==null){
		alert("请选择指定的拣货人!");
		return;
	}
	
	var rows = $('#dataGridJG').datagrid("getChecked");
	if(rows.length < 1){
		alert("请至少选择一行!");
		return;
	}
	var keyStr = [];
	$.each(rows,function(index,item){
		keyStr.push(item.outstockNo+"|");
	});
	wms_city_common.loading("show","正在发单......");
	$.ajax({
		type : 'POST',
		dataType : "json",
		data:{
			locno:billomoutstock.locno,
			assignName:assignName,
			assignNameCh:assignNameCh,
			keyStr:keyStr.join(",")
		},
		url:BasePath+'/bill_om_outstock/sendOrder',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert("发单成功");
				$('#check_user_dialog').window('close');
				billomoutstock.loadDataGrid();
			}else if(data.result=="fail"){
				alert("发单失败,原因:"+data.msg,2);
			}else{
				alert("发单失败,请联系管理员",2);
			}
		}
	});
};

//拣货确认
billomoutstock.check = function(){
	var curObj = $("#dataGridJG_Detail2");
	var rows = curObj.datagrid("getRows");
	
	//验证是否存在拣货明细
	if(rows.length < 1){
		alert("拣货单明细不能为空!");
		return;
	}
	
	//结束编辑
	$.each(rows,function(index,item){
		curObj.datagrid('endEdit',index);
	});	
	
	var isOk = true;
	var keyStr = [];
	$.each(rows,function(index,item){
		
//		var ed = curObj.datagrid('getEditor', {index:index,field:'realQty'});
//		var edOutstockName = curObj.datagrid('getEditor', {index:index,field:'outstockName'});
//		var value = $(ed.target).val();
//		var outstockName = $(edOutstockName.target).combobox('getValue');
		
		var value = item.realQty;
		var outstockName = item.outstockName;
		
		if(value < 0){
			alert("商品："+item.itemNo+"尺码："+item.sizeNo+"实际拣货数量必须大于或等于0");
			//$(ed.target).focus();
			isOk = false;
			return false;
		}
			
		if(value > item.itemQty){
			alert("商品："+item.itemNo+"尺码："+item.sizeNo+"实际拣货数量不能大于计划数量!");
			isOk = false;
			return;
		}
		
		if(item.outstockedQty > value){
			alert("商品："+item.itemNo+"尺码："+item.sizeNo+"实际数量必须大于或等于RF数量!");
			isOk = false;
			return;
		}
			
		if(outstockName==""){
			alert("商品："+item.itemNo+"尺码："+item.sizeNo+"请选择实际拣货人");
			//$(edOutstockName.target).focus();
			isOk = false;
			return false;
		}
			
		keyStr.push(item.ownerNo+"|"+item.divideId+"|"+value+"|"+outstockName);
		
	});
	
	if(isOk&&keyStr.length>0){
		wms_city_common.loading("show","正在保存......");
		$.ajax({
			async : true,
			cache : false,
			type : 'POST',
			dataType : "json",
			data:{
				locno:billomoutstock.locno,
				outstockNo:$("#outstockNo").val(),
				//outstockName:$("#outstockName").combobox("getValue"),
				keyStr:keyStr.join(",")
			},
			url:BasePath+'/bill_om_outstock/checkOutstock',
			success : function(data) {
				wms_city_common.loading();
				if(data.result=="success"){
					alert("保存成功");
					$('#showDialog').window('close');
					billomoutstock.loadDataGrid();
				}else if(data.result=="fail"){
					alert("拣货失败,原因:"+data.msg,2);
				}else{
					alert("拣货失败,请联系管理员",2);
				}
			}
		});
	}
};

billomoutstock.search = function(){
	
	var createTmStart =  $('#createTmStart').datebox('getValue');
	var createTmEnd =  $('#createTmEnd').datebox('getValue');
	if(!isStartEndDate(createTmStart,createTmEnd)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	
	var outstockDateStart =  $('#outstockDateStart').datebox('getValue');
	var outstockDateEnd =  $('#outstockDateEnd').datebox('getValue');
	if(!isStartEndDate(outstockDateStart,outstockDateEnd)){    
		alert("拣货日期开始日期不能大于结束日期");   
        return;   
    }
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_outstock/list.json?outstockType=0&locno='+billomoutstock.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
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

billomoutstock.clear = function(){
	$("#searchForm").form("clear");
	$('#brandNo').combobox("loadData",[]); 
};

//格式化
billomoutstock.statusFormatter = function(value,rowData,rowIndex){
	return billomoutstock.status[value];
};
billomoutstock.typeFormatter = function(value,rowData,rowIndex){
	return billomoutstock.taskType[value];
};
billomoutstock.outstockNameFormatter = function(value,rowData,rowIndex){
	return billomoutstock.workerLocs[value];
};

billomoutstock.exportDetail = function(){
	exportExcelBill4Detail('moduleView',billomoutstock.preColNames,
			billomoutstock.endColNames, billomoutstock.sizeTypeFiledName,"拣货单明细报表");
};

function exportExcelBill4Detail(dataGridId,preColNames,endColNames,sizeTypeFiledName,excelTitle){
	
	var url=BasePath+'/bill_om_outstock_dtl/do_export4Detail';
	
	var $dg = $("#"+dataGridId+"");
	
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
	
	$("<form id='exportExcelForm' method='post'></form>").appendTo("body"); ;
	
	var fromObj=$('#exportExcelForm');
	
	var dataRow=$dg.datagrid('getRows');
	if(dataRow.length>0){
		if(tempTotal>dataRow.length){
			var dataGridUrl = $dg.datagrid( 'options' ).url;
			var dataGridQueryParams = $dg.datagrid( 'options' ).queryParams;
			dataGridQueryParams.page = 1;
			dataGridQueryParams.rows = tempTotal;
		 	$.ajax({
				  type: 'POST',
				  url: dataGridUrl,
				  data: dataGridQueryParams,
				  cache: true,
				  async:false, // 一定要
				  success: function(resultData){
					  dataRow = resultData.rows;
				  }
		     });
		}
	    fromObj.form('submit', {
			url: url,
			onSubmit: function(param){
				param.locno=billomoutstock.locno;
				param.outstockNo=$("#outstockNoView").val();
				param.preColNames=JSON.stringify(preColNames);
				param.endColNames=JSON.stringify(endColNames);
				param.sizeTypeFiledName=sizeTypeFiledName;
				param.fileName=excelTitle;
				param.dataRow=JSON.stringify(dataRow);
			},
			success: function(){
				
		    }
	   });
	}else{
		alert('数据为空，不能导出!',1);
	}
}

billomoutstock.do_export = function(){
	var exportColumns = $("#dataGridJG_Detail").datagrid('options').columns[0];
	var str = '[';
	for(var index=0;index<exportColumns.length;index++){
		var temp = exportColumns[index];
		str +=  "{\"field\":\""+temp.field+"\",\"title\":\""+temp.title+"\"}";
		if(index != exportColumns.length-1){
			str += ",";
		}
	}
	str += "]";
	
	var searchUrl = BasePath+'/bill_om_outstock_dtl/dlist.json?locno='+billomoutstock.locno;
	var fromObjStr=convertArray($('#detailForm_view').serializeArray());
	var data = eval("(" +fromObjStr+ ")");
	data.page = 1;
	data.rows = $("#dataGridJG_Detail").datagrid('options').pageSize;
	
	var result = {};
	$.ajax({
		  async:false,
		  type: 'POST',
		  url:searchUrl,
		  data: data,
		  cache: true,
		  success: function(r){
			  result = r;
		  }
	});
	if(!result || result == null || result.total == 0){
		alert("没有可以导出的数据!",1);
		return;
	}else if(result.total > 10000){
		alert("数据大于10000条不能导出!",1);
		return;
	} else {
		$("#exportColumnsCondition_export").val(str);
		$("#locnoCondition_export").val(billomoutstock.locno);
		$("#outstockNo_export").val($("#outstockNoView").val());
		$("#exportForm").submit();
	
	}
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
}
billomoutstock.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
		{
			'url':BasePath+'/bill_om_outstock/list.json?outstockType=0&locno='+billomoutstock.locno
		});
};

billomoutstock.loadDataDetail = function(rowData){
	 $('#dataGridJG_Detail').datagrid(
		{
			'url':BasePath+'/bill_om_outstock_dtl/list.json?locno='+rowData.locno+"&outstockNo="+rowData.outstockNo
		});
};

billomoutstock.printDetail4SizeHorizontal = function(size){
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	billomoutstock.print(rows,size);
};

billomoutstock.print = function(rows,size){
	var resultData;
	var keys = [];
	var outstockSendType = rows[0].outstockSendType;
	for(var i=0,length = rows.length;i<length;i++){
		if(rows[i].outstockSendType != outstockSendType){
			alert("只能打印拣货切单类型相同的单据!");
			return; 
		}
		keys.push(rows[i].outstockNo);
	}
    if(outstockSendType=="1"){//按库区拣货
    	$.ajax({
    		cache: false,
            async: false,
            type: 'POST',
            url: BasePath+'/bill_om_outstock_dtl/printDetail4Area',
            data: {
            	keys:keys.join(",")
            },
            success: function(result){
            	resultData = result;
    		}
          });
         if(resultData.result!="success"){
         		alert(resultData.msg);
         		return;
         }else{
        	var data = resultData.data;
        	//******************新增测试数据用于分页S********************
        	/*var array = data[0].rows;
        	var tem = array[0];
        	for(var idx=0;idx<500;idx++){
        		array[array.length] = tem;
        	}
        	data[0].rows = array;*/
        	//******************新增测试数据用于分页E********************
         	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
         	if(LODOP==null){
         		return;
         	}
         	if(size=="A4"){
         		LODOP.SET_PRINT_PAGESIZE(1,0,0,size);
         	} else {
         		LODOP.SET_PRINT_PAGESIZE(2,0,0,size);
         	}
         	//**************************************
    		var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>"
    		for(var i=0,length=data.length;i<length;i++){
    			LODOP.NewPageA();
    			var headHtml =  billomoutstock.getHtmlByTemplateHead4Area(data[i]);
    			var bodyHtml = billomoutstock.getHtmlByTemplateTable4Area(data[i]);
//    			var footerHtml = billomoutstock.getHtmlByTemplateFooter4Area(data[i]);
    			
    			//设置表格内容
    			LODOP.ADD_PRINT_TABLE(120,0,"100%",400,strStyle+bodyHtml);
    			LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
    			//设置头部信息
    			LODOP.ADD_PRINT_HTM(0,0,"100%",120,headHtml);
    			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
    			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
    			
    			//设置底部信息
    			/*LODOP.ADD_PRINT_HTM(1100,0,"100%",50,footerHtml);
    			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
    			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);*/	
    			
    			//设置条码
    			LODOP.ADD_PRINT_BARCODE(50,520,250,40,"128A",data[i].stock.outstockNo);
    			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
    			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
    			
    			
    		}
    		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
    		LODOP.PREVIEW();
         	}
    	}else if(outstockSendType=="0"){
    	$.ajax({
    		cache: false,
            async: false,
            type: 'POST',
            url: BasePath+'/bill_om_outstock_dtl/printDetail',
            data: {
            	keys:keys.join(",")
            },
            success: function(result){
            	resultData = result;
    		}
          });
         if(resultData.result!="success"){
         		alert(resultData.msg);
         		return;
         }else{
        	var data = resultData.data;
        	//******************新增测试数据用于分页S********************
        	/*var array = data[0].rows;
        	var tem = array[0];
        	for(var idx=0;idx<500;idx++){
        		array[array.length] = tem;
        	}
        	data[0].rows = array;*/
        	//******************新增测试数据用于分页E********************
         	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
         	if(LODOP==null){
         		return;
         	}
         	if(size=="A4"){
         		LODOP.SET_PRINT_PAGESIZE(1,0,0,size);
         	} else {
         		LODOP.SET_PRINT_PAGESIZE(2,0,0,size);
         	}
         	//**************************************
         	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>"
    		
    		for(var i=0,length=data.length;i<length;i++){
    			LODOP.NewPageA();
    			//var html = billomoutstock.getHtmlByTemplate(data[i],data[i].outstockNo,rows[i].locateNo);
    			//LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
    			
    			//LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
    			var headHtml =  billomoutstock.getHtmlByTemplateHead(data[i],data[i].outstockNo,rows[i].locateNo);
    			var bodyHtml = billomoutstock.getHtmlByTemplateBody(data[i],data[i].outstockNo,rows[i].locateNo);
//    			var footerHtml = billomoutstock.getHtmlByTemplateFooter(data[i],data[i].outstockNo,rows[i].locateNo);
    			
    			//设置表格内容
    			LODOP.ADD_PRINT_TABLE(120,0,"100%",400,strStyle+bodyHtml);
    			LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
    			
    			//设置表格头部
    			LODOP.ADD_PRINT_HTM(0,0,"100%",120,headHtml);
    			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
    			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
    			
    			//设置表格底部
    			/*LODOP.ADD_PRINT_HTM(1100,0,"100%",50,footerHtml);
    			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
    			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);	*/
    			
    		 	//设置条码
    			LODOP.ADD_PRINT_BARCODE(50,520,250,40,"128A",data[i].outstockNo);
    			LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
    			LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
    			
    			
    		}
    		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
    	 	LODOP.PREVIEW();
         }
    }
};

billomoutstock.getHtmlByTemplateHead4Area = function(data){
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='3'>拣货单(按库区)</td></tr>";
	html += "<tr style='font-size:13px;'><td colspan='3' style='width:60%;height:40' valign='top'>拣货任务："+data.stock.outstockNo+"</td></tr>";
	html += "<tr style='font-size:13px;'><td>拣货类型：出库订单</td>";
	html += "<td>拣货波次："+data.locateNo+"</td>";
	
	html+="<td>商品总数："+data.allSum+"</td></tr>";
	html += "</table>";
	return html;
};

billomoutstock.getHtmlByTemplateTable4Area = function(data){
	var html = "<table border='1' cellspacing='0' cellpadding='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'>";
	html += "<thead><tr bgcolor='#fff' style='font-size:13px;'><td>储位</td><td>商品编码</td><td>客户编号</td><td>客户名称</td><td>颜色</td>";
	var sizeNos = data.allSizeNo;
	for(var i=0,length = sizeNos.length;i<length;i++){
		html = html+"<td>"+sizeNos[i]+"</td>";
	}
	html = html+"<td>合计</td>"
	html = html+"</tr></thead><tbody>";
	var rows = data.list;
	for(var i=0,length=rows.length;i<length;i++){
		for(var j=0,sublength = rows[i].children.length;j<sublength;j++){
			var childData = rows[i].children[j];
			html = html+"<tr bgcolor='#fff' style='font-size:13px;'>"+
			"<td>"+childData.sCellNo+"</td>"+
			"<td>"+childData.itemNo+"</td>"+
			"<td>"+childData.storeNo+"</td>"+
			"<td>"+childData.storeName+"</td>"+
			"<td>"+childData.colorName+"</td>";
			for(var s=0,sizelength = sizeNos.length;s<sizelength;s++){
				var itemQty = childData[sizeNos[s]];
				if(itemQty!=""&&itemQty!=null){
					html = html+"<td>"+itemQty+"</td>";
				}else{
					html = html+"<td></td>";
				}
			}
			html = html+"<td>"+childData.lineSum+"</td></tr>";
		}
		html = html+"<tr bgcolor='#fff' style='font-size:13px;'><td></td><td></td><td></td><td></td><td>小计</td>";
		var allSum = 0;
		for(var j=0,sublength = sizeNos.length;j<sublength;j++){
			var itemQty = rows[i]["sum"+sizeNos[j]];
			if(itemQty!=""&&itemQty!=null){
				html = html+"<td>"+itemQty+"</td>";
				allSum = allSum+itemQty;
			}else{
				html = html+"<td></td>";
			}
		}
		html = html+"<td>"+allSum+"</td></tr>";
		if(i<length-1){
			html = html+"<tr style='height:5px;' bgcolor='#fff' style='font-size:13px;'><td colspan='100'></td></tr>";
		}
	}
	html = html+"</tbody>";
	html+="<tfoot><tr><td style='text-align:center;' colspan='"+(6+sizeNos.length)+"'><font tdata='pageNO' format = '#' align='center'>第#页</font>/<font format='#' tdata='pageCount'>共#页</font></td></tr>";
	html+="<tr><td style='text-align:right;' colspan='"+(6+sizeNos.length)+"'>打印人："+data.curOper+"&nbsp;&nbsp;&nbsp;&nbsp;打印时间："+data.curDate+"</td></tr></tfoot></table>";
	return html;
};
billomoutstock.getHtmlByTemplateFooter4Area = function(data){
	var html  = "<table border='0' cellspacing='0' cellpadding='0' width='100%'>";
	html += "<tr><td style='text-align:right;padding-right:10px;'>打印人："+data.curOper+"&nbsp;&nbsp;&nbsp;&nbsp;打印时间："+data.curDate+"</td></tr>";
	html += "</table>";
	return html;
};

billomoutstock.getHtmlByTemplateHead = function(data,outstockNo,locateNo){
	var html = "<table style='width:100%;'>";
	if(data.haveStore){
		html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>拣货单(明细含客户)</td></tr>";
	}else{
		html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>拣货单(明细无客户)</td></tr>";
	}
	html += "<tr style='font-size:13px;'><td colspan='4' style='width:60%;height:40' valign='top'>拣货任务："+outstockNo+"</td></tr>";
	html += "<tr style='font-size:13px;'><td>拣货类型：出库订单</td>";
	html += "<td>拣货波次："+locateNo+"</td>";
	if(data.haveStore){
		html+="<td>客户："+data.storeNoList[0].storeName+"("+data.storeNoList[0].storeNo+")</td>";
	}else{
		html+="<td>客户总数："+data.storeNoList.length+"</td>";
	}
	html+="<td>商品总数："+data.itemCount+"</td></tr>";
	html += "</table>";
	return html;
};

billomoutstock.getHtmlByTemplateBody = function(data,outstockNo,locateNo){
	var html = "<table border='1' cellspacing='0' cellpadding='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'>";
	html += "<thead><tr bgcolor='#fff' style='font-size:13px;'><td>序号</td><td>储位</td><td>商品编号</td><td>商品名称</td><td>颜色</td><td>尺码</td><td>数量</td><td>箱号</td></tr></thead><tbody>";
	for(var i = 0;length=data.rows.length,i<length;i++){
		html+="<tr bgcolor='#fff' style='font-size:13px;'>";
			html+="<td>"+(i+1)+"</td>";//序号
			html+="<td>"+data.rows[i].sCellNo+"</td>";//储位
			html+="<td>"+data.rows[i].itemNo+"</td>";//商品编码
			html+="<td>"+data.rows[i].itemName+"</td>";//名称
			html+="<td>"+(data.rows[i].colorName==null?"":data.rows[i].colorName)+"</td>";//颜色
			html+="<td>"+data.rows[i].sizeNo+"</td>";//尺码
			html+="<td>"+data.rows[i].itemQty+"</td>";//数量
			html+="<td>"+(data.rows[i].scanLabelNo==null?"":data.rows[i].scanLabelNo)+"</td>";//箱号
		html+"</tr>";
	}
	html+="</tbody><tfoot><tr><td style='text-align:center;' colspan='8'><font tdata='pageNO' format = '#' align='center'>第#页</font>&nbsp;/&nbsp;<font format='#' tdata='pageCount'>共#页</font></td></tr>";
	html+="<tr><td style='text-align:right;' colspan='8'>打印时间："+data.printTime+"</td></tr></tfoot></table>";
	return html;
};

billomoutstock.getHtmlByTemplateFooter = function(data,outstockNo,locateNo){
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:right;padding-right:50px;'>打印时间："+data.printTime+"</td></tr>";
	html += "</table>";
	return html;
};

billomoutstock.printByOutstockSendType = function(){
	var resultData;
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var keys = [];
	var outstockSendType = rows[0].outstockSendType;
	for(var i=0,length = rows.length;i<length;i++){
		if(rows[i].outstockSendType != outstockSendType){
			alert("只能打印拣货切单类型相同的单据!")
			return; 
		}
		keys.push(rows[i].outstockNo);
	}
	if(rows == null || rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].outstockNo);
	}
	var url = '';
	if(outstockSendType == '0'){
		url = BasePath+'/bill_om_outstock_dtl/printByStore?locno='+billomoutstock.locno;
	}else{
		url = BasePath+'/bill_om_outstock_dtl/printByArea?locno='+billomoutstock.locno;
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: url,
        data: {
        	keys:keys.join(",")
        },
        success: function(result){
        	resultData = result;
		}
     });
     if(resultData.result!="success"){
     		alert(resultData.result);
     		return;
     }else{
    	var data = resultData.html;
     	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
     	if(LODOP==null){
     		return;
     	}
		LODOP.SET_PRINT_PAGESIZE(1,'850','480',"");
		for(var i=0,length=data.length;i<length;i++){
			LODOP.NewPage();
			var html = data[i];
			LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
			LODOP.ADD_PRINT_BARCODE(115,85,250,50,"128A",rows[i].outstockNo);
			LODOP.SET_PRINT_STYLEA(0,"Horient",2);//水平居中
		}
		//LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
		LODOP.PREVIEW();
     }
	
};


//按计划保存
billomoutstock.saveByPlan = function(){
	var outstockNo = $("#outstockNo").val();
	if(billomoutstock.checkRealQty(outstockNo) > 0){
		$.messager.confirm("确认",outstockNo+"实际数量存在值,继续操作将会覆盖原有的实际数量,您确定要继续操作吗?", function (r){
			if(r){
				billomoutstock.saveByPlanDtl(outstockNo);
			}
		}); 
	}else{
		billomoutstock.saveByPlanDtl(outstockNo);
	}
};

billomoutstock.saveByPlanDtl = function(outstockNo){
	wms_city_common.loading("show","正在保存......");
	$.ajax({
	 	async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
	    url: BasePath+'/bill_om_outstock_dtl/saveByPlan',
	    data:{
	    	locno:billomoutstock.locno,
			outstockNo:outstockNo
	    },
	    success: function(data){
	    	wms_city_common.loading();
	    	if(data.result=="success"){
         		alert("保存成功");
         		$("#dataGridJG_Detail2").datagrid("load");
         	}else{
         		alert(data.msg,2);
         	}
	    	$('#showDialog').window('close');
		}
	});
};


//审核
billomoutstock.auditOutstock = function(){
	var checkRows = $("#dataGridJG").datagrid('getChecked');
	if(checkRows.length < 1){
		alert("请至少选择一条记录!");
		return;
	}
	
	var msg = "确定要审核选中的"+checkRows.length+"条单据吗?";
	var checkIds = [];
	for(var i=0;i<checkRows.length;i++){
		if(checkRows[i].status != 10){
			alert(checkRows[i].outstockNo+"已拣货完成，不能审核!");
			return;
		}
		checkIds[checkIds.length] = {outstockNo:checkRows[i].outstockNo,locno:billomoutstock.locno};
	}
	
	var datas = JSON.stringify(checkIds);
	$.ajax({
	 	async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
	    url: BasePath+'/bill_om_outstock/selectCheckDtlRealQtyEq',
	    data:{
	    	datas:datas
	    },
	    success: function(data){
	    	if(data.result != "" && data.result !=null){
	    		msg = data.result + "存在实际数量=0的数据,确定要继续审核吗?";
	    	}
		}
	});
	
	$.messager.confirm("确认",msg, function (r){
		if(r){
			var url = BasePath+'/bill_om_outstock/auditOutstock';
			wms_city_common.loading("show");
			billomoutstock.ajaxRequest(url,{datas:datas},true,function(r){
				wms_city_common.loading();
				if(r.result=="success"){
					alert("审核成功!");
					$("#dataGridJG").datagrid('load');//1,查询加载待复核的商品信息
				}else{
					alert('审核失败,原因:'+r.msg,2);
				}
			});
		}
	});
};

//验证是否存在大于0的real_qty
billomoutstock.checkRealQty = function(outstockNo){
	var num = 0;
	$.ajax({
	 	async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
	    url: BasePath+'/bill_om_outstock_dtl/selectCheckDtlRealQty',
	    data:{
	    	locno:billomoutstock.locno,
			outstockNo:outstockNo
	    },
	    success: function(data){
	    	num = data.result;
		}
	});
	return num;
};

billomoutstock.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
