var billimreceipt = {};
billimreceipt.locno;
billimreceipt.currentDate;

//状态
billimreceipt.status = {};
//委托业主
billimreceipt.ownnerData={};
//码头
billimreceipt.dockData={};
//委托业主
billimreceipt.dockFormatter = function(value, rowData, rowIndex){
	return billimreceipt.dockData[value];
};

billimreceipt.exportDataGridDtl1Id = 'moduleView';
billimreceipt.preColNames = [
                           {field:'importNo',title:'预到货通知单号',width:150},
		                   {title:"商品编码",field:"itemNo",width:150},
		                   {field:'itemName',title:'商品名称',width:150,align:'left'},
		                   {title:"箱号",field:"boxNo",width:120},
		                   {title:"品牌",field:"brandName",width:100},
                    ];
billimreceipt.endColNames = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billimreceipt.sizeTypeFiledName = 'sizeKind';
billimreceipt.mainSumFoot = {};
billimreceipt.onLoadSuccess = function(data){
	if(data.footer[1] != null){
			billimreceipt.mainSumFoot = data.footer[1];
		}else{
			data.footer[1] = billimreceipt.mainSumFoot;
			$('#dataGridJG').datagrid('reloadFooter');
		}
}
$(document).ready(function(){
	//初始化仓库
	billimreceipt.loadLoc();
	billimreceipt.initBrand();
	//billimreceipt.initUsers();
	billimreceipt.initSupplierNo();
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=search_brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	
	$('#dataGridJG_detail_view').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billimreceipt.receiptQty = data.footer[1].receiptQty;
		   				billimreceipt.checkQty = data.footer[1].checkQty;
		   				billimreceipt.divideQty = data.footer[1].divideQty;
		   			}else{
		   				var rows = $('#dataGridJG_detail_view').datagrid('getFooterRows');
			   			rows[1]['receiptQty'] = billimreceipt.receiptQty;
			   			rows[1]['checkQty'] = billimreceipt.checkQty;
			   			rows[1]['divideQty'] = billimreceipt.divideQty;
			   			$('#dataGridJG_detail_view').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	/*$('#dataGridJG').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1] != null){
		   				billimreceipt.mainSumFoot = data.footer[1];
		   			}else{
			   			data.footer[1] = billimreceipt.mainSumFoot;
			   			$('#dataGridJG').datagrid('reloadFooter');
		   			}
		   		}
			}
		);*/
	//初始化业主
	wms_city_common.comboboxLoadFilter(
			["search_ownerNo","ownerNo","boxOwnerNo","ownerNo_view"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[true,false,false,false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billimreceipt.ownnerData,
			null);
	//初始化码头
	wms_city_common.comboboxLoadFilter(
			["serachDockNo","dockNo"],
			'dockNo',
			'dockName',
			'valueAndText',
			false,
			[true,false],
			BasePath+'/bm_defdock/get_biz',
			{locno:billimreceipt.locno},
			billimreceipt.dockData,
			null);
	//初始化状态
	wms_city_common.comboboxLoadFilter(
			["search_status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_RECEIPT_STATUS',
			{},
			billimreceipt.status,
			null);
	//初始化员工
	wms_city_common.comboboxLoadFilter(
			["search_creator","search_auditor","search_receiptWorker","receiptWorker_view","receiptWorker"],
			'workerNo',
			'workerName',
			'valueAndText',
			false,
			[true,true,true,false,false],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
});

billimreceipt.getDate = function(n){
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

/***********************************************初始化******************************************************/


//初始化供应商
billimreceipt.initSupplierNo = function(){
	 $.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/billimimport/findSupplierNo?locno='+billimreceipt.locno,
		success : function(data) {
			data = data.rows;
			$('#supplierNo').combobox({
			     valueField:"supplierNo",
			     textField:"valueAndText",
			     data:data,
			     panelHeight:150,
			     loadFilter:function(data){
		    			if(data){
				       		 for(var i = 0;i<data.length;i++){
				       			 var tempData = data[i];
				       			 tempData.valueAndText = tempData.supplierNo+'→'+tempData.supplierName;
				       		 }
		       	 		}
		    			return data;
		   			 } 
			});
			$('#supplierNo_view').combobox({
			     valueField:"supplierNo",
			     textField:"valueAndText",
			     data:data,
			     panelHeight:150,
			     loadFilter:function(data){
		    			if(data){
				       		 for(var i = 0;i<data.length;i++){
				       			 var tempData = data[i];
				       			 tempData.valueAndText = tempData.supplierNo+'→'+tempData.supplierName;
				       		 }
		       	 		}
		    			return data;
		   			 } 
			});
		}
	});
};

//加载供应商
billimreceipt.reLoadSupplier = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/billimimport/findSupplierNo?locno='+billimreceipt.locno,
		success : function(data) {
			data = data.rows;
			$('#supplierNo').combobox('loadData',data);
		}
	});
};
billimreceipt.initUsers = function(){
	 $.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#search_creator').combobox({
			     valueField:"workerNo",
			     textField:"unionName",
			     data:data,
			     panelHeight:150
			});
			$('#search_auditor').combobox({
			     valueField:"workerNo",
			     textField:"unionName",
			     data:data,
			     panelHeight:150
			});
			$('#search_receiptWorker').combobox({
			     valueField:"workerNo",
			     textField:"unionName",
			     data:data,
			     panelHeight:150
			});
			$('#receiptWorker_view').combobox({
			     valueField:"workerNo",
			     textField:"unionName",
			     data:data,
			     panelHeight:150
			});
			$('#receiptWorker').combobox({
			    data:data,
			    valueField:'workerNo',    
			    textField:'unionName',
			    panelHeight:150
			}).combobox("select",data[0]==""||data[0]==null?"":data[0].workerNo);
		}
	});
};

//加载仓库信息
billimreceipt.loadLoc = function(){
		$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billimreceipt.locno = data.locno;
			billimreceipt.currentDate = data.currentDate10Str;
			//收货日期默认当天的前两天
			$("#startTm").datebox('setValue',getDateStr(-2));
			//$("#endTm").datebox('setValue',billimreceipt.currentDate);
		}
	});
};



/***********************************************初始化end******************************************************/

//加载Grid数据Utils
billimreceipt.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

/***********************************************新增******************************************************/
//弹出新增窗口
billimreceipt.addInfo = function(){
	 //清空所有数据
	 $("#detailForm").form("clear");
	 //仓库，委托业主初始化
	 $("#ownerNo").combobox("select",$("#ownerNo").combobox("getData")[0].ownerNo);
	 //清空下拉列表
	 $('#dataGridJG_detail').datagrid('loadData',[]);
	 billimreceipt.showReceiptNo("1");
	 
	$("#ownerNo").combobox("enable");
	$("#ownerNoHide").attr("disabled",true);
	 
	billimreceipt.reLoadSupplier();
	
	$("#supplierNo").combobox("enable");
	$("#supplierNoHide").attr("disabled",true);
	var now = new Date(); 
	var SY = now.getFullYear(); 
	var SM = now.getMonth(); 
	var SD = now.getDate(); 
	$("#curDate").val(SY+"-"+(SM+1)+"-"+SD);
	$("#main_btn").show();
	$("#sub_con").hide();
	 $('#detailDialog').window({
		title:"新增",
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	 });
	 $("#addBtn").show();
	 $("#editBtn").hide();
	 $("#detailDialog").window('open'); 
};

//新增主表信息
billimreceipt.save_main = function(){
	var fromObj=$('#detailForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    wms_city_common.loading("show");
    var receiptWorkerText = $("#receiptWorker").combobox("getText");
    var receiptWorkerName = receiptWorkerText.substr(receiptWorkerText.indexOf("→")+1,receiptWorkerText.length);
    $("#receiptWorkerName").val(receiptWorkerName);
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_im_receipt/addMain',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert('新增成功!');
				billimreceipt.showSub(data.data);
				$("#curDate").val(data.createData);
				 $("#addBtn").hide();
	 			 $("#editBtn").show();
				billimreceipt.searchArea();
			}else{
				alert('新增失败,请联系管理员!',2);
			}
		}
	});
};
//修改主表信息
billimreceipt.edit_main = function(){
	var fromObj=$('#detailForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    var receiptWorkerText = $("#receiptWorker").combobox("getText");
    var receiptWorkerName = receiptWorkerText.substr(receiptWorkerText.indexOf("→")+1,receiptWorkerText.length);
    $("#receiptWorkerName").val(receiptWorkerName);
    wms_city_common.loading("show");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_im_receipt/editMain',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert('修改成功!');
				billimreceipt.showSub(data.data);
				$("#addBtn").hide();
	 			$("#editBtn").show();
				billimreceipt.searchArea();
			}else{
				if(data.msg==null){
					alert('修改失败,请联系管理员!',2);
				}else{
					alert(data.msg,1);
				}
			}
		}
	});
};

billimreceipt.showSub = function(data){
	//设置主表信息
	$("#receiptNo").val(data.receiptNo);
	$("#receiptNoHide").val(data.receiptNo);
	//仓库
	$("#locNo").combobox("disable");
	$("#locNoHide").attr("disabled",false);
	$("#locNoHide").val(data.locno);
	//委托业主
	$("#ownerNo").combobox("disable");
	$("#ownerNoHide").attr("disabled",false);
	$("#ownerNoHide").val(data.ownerNo);
	//收货日期
	$("#curDate").val(data.createtm);
	//供应商
	$("#supplierNo").combobox("disable");
	$("#supplierNoHide").attr("disabled",false);
	$("#supplierNoHide").val(data.supplierNo);
	$("#main_btn").hide();
	$("#sub_con").show();
	$("#opBtn").show();
};

/***********************************************新增end******************************************************/


/***********************************************删除******************************************************/
billimreceipt.del = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keyStr = [];
	var ownerNo ;
	var locNo;
	var allOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!="10"){
			allOk = false;
			return false;
		}
		keyStr.push(item.receiptNo);
		if(index==0){
			ownerNo = item.ownerNo;
			locNo = item.locno;
		}
	});   
	if(!allOk){
		alert("非建单状态的数据不能删除",1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	wms_city_common.loading("show");
            var url = BasePath+'/bill_im_receipt/deleteReceipt';
        	var data={
        	    "keyStr":keyStr.join(","),
        	    "ownerNo":ownerNo,
        	    "locNo":locNo
        	};
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					 wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('删除成功!');
						 billimreceipt.loadDataGrid();
        		 	 }else{
        		 		if(data.msg==null){
        		 			alert('删除失败,请联系管理员!',2);
        		 		}else{
        		 			alert(data.msg,1);
        		 		}
        		 	}
				  }
			});
        }  
    });  
};

/***********************************************删除end******************************************************/

/***********************************************审核***********************************************************/
billimreceipt.audit=function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}
	
	var keyStr = [];
	var ownerNo;
	var locNo;
	var allOk = true;
//	var receiptNo = "";
	$.each(checkedRows, function(index, item){
		if(item.status!="10"){
			allOk = false;
			return false;
		}
//		var queryMxURL=BasePath+'/bill_im_receipt_dtl/selectItemDetail?receiptNo='+item.receiptNo+'&locno='+item.locno+'&ownerNo='+item.ownerNo;
//		billimreceipt.ajaxRequest(queryMxURL,{},false,function(result){
//			if(result.total<=0){
//				 receiptNo += "["+item.receiptNo+"]";
//			 }
//		});
		keyStr.push(item.receiptNo);
		if(index==0){
			ownerNo = item.ownerNo;
			locNo = item.locno;
		}
	});
//	if(receiptNo != ""){
//		alert("单据"+receiptNo+"不存在明细,不允许审核!",2);
//		return;
//	}
	if(!allOk){
		alert("非建单状态的数据不能审核",1);
		return;
	}             
    var url = BasePath+'/bill_im_receipt/auditMain';
	var data={
	    "keyStr":keyStr.join(","),
	    "ownerNo":ownerNo,
	    "locNo":locNo
	};
	
	$.messager.confirm("确认","你确定要审核这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	wms_city_common.loading("show");
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					 wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('审核成功!');
						 billimreceipt.loadDataGrid();
				  	
        		 	}else{
        		 		if(data.msg != null || data.msg != ''){
        		 			alert(data.msg,2);
        		 		} else{
        		 			alert('审核失败,请联系管理员!',2);
        		 		}
        		 	}
				  }
			});
        }  
    });
};
billimreceipt.converStr2JsonObj= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billimreceipt.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//加载数据
billimreceipt.loadDataGrid = function(){
	
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bill_im_receipt/findMainRecipt?locno='+billimreceipt.locno + "&businessType=0",
    			'pageNumber':1
    });
    
//    var url = BasePath+'/bill_im_receipt/findMainRecipt';
//    var queryParams = {locno:billimreceipt.locno};
//    billimreceipt.loadGridDataUtil('dataGridJG', url, queryParams);
    
};

billimreceipt.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status!="10"){
		alert("非建单状态的数据不能修改");
		return;
	}else{
		billimreceipt.instockDetail(checkedRows[0],"edit");
	}
};

//单击一行查看收货明细
billimreceipt.instockDetail = function(rowData,type){
	billimreceipt.showReceiptNo("2");
	$('#detailDialog').window({
		title:"收货单明细"
	 });
	$("#detailDialog").window('open'); 
	$("#detailForm").form('load',rowData);
//	$("#supplierNo").combo('setText',rowData.supplierNo+'→'+rowData.supplierName);
	
	$("#locNo").combobox("disable");
	$("#locNoHide").attr("disabled",false);
	
	$("#ownerNo").combobox("disable");
	$("#ownerNoHide").attr("disabled",false);
	
//	$("#supplierNo").combobox("disable");
//	$("#supplierNoHide").attr("disabled",false);
	
	$("#main_btn").hide();
//	debugger;
	if(type=="edit"&&rowData.status=="10"){
		$("#opBtn").show();
		$("#addBtn").hide();
		$("#editBtn").show();
	}else{
		$("#opBtn").hide();
	}
	$("#sub_con").show();
	var queryMxURL=BasePath+'/bill_im_receipt_dtl/findReceiptDetail?receiptNo='+rowData.receiptNo+'&locno='+rowData.locno;
	$( "#dataGridJG_detail").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_detail").datagrid( 'load' );
};


/***********************************************格式化******************************************************/
//状态
billimreceipt.statusFormatter = function(value, rowData, rowIndex){
	return billimreceipt.status[value];
};
//委托业主
billimreceipt.ownerFormatter = function(value, rowData, rowIndex){
	return billimreceipt.ownnerData[value];
};


/***********************************************格式化******************************************************/


//查询区域信息
billimreceipt.searchArea = function(){
//	debugger;
	var audittmStart = $('#audittmStart').datebox('getValue');
	var audittmEnd = $('#audittmEnd').datebox('getValue');
	if(!isStartEndDate(audittmStart,audittmEnd)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    }
    
    var startTm = $('#startTm').datebox('getValue');
	var endTm = $('#endTm').datebox('getValue');
	if(!isStartEndDate(startTm,endTm)){    
		alert("收货日期开始日期不能大于结束日期");   
        return;   
	}
    
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_im_receipt/findMainRecipt?locno='+billimreceipt.locno;
	var queryParams = eval("(" +fromObjStr+ ")");
	queryParams['businessType'] = "0";
	
    $( "#dataGridJG").datagrid( 'options' ).queryParams = queryParams;
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

billimreceipt.searchLocClear = function(){
	$('#searchForm').form("clear");
	$('#search_brandNo').combobox("loadData",[]);
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

billimreceipt.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};


$.extend($.fn.datagrid.defaults.editors, {    
    textbutton: {    
        init: function(container, options){
            var input = $('<input type="text" name="itemNo" style="width: 90px;"><input type="button" style="width: 30px;"  onclick="javascript:billimreceipt.showBoxDialog(this);">').appendTo(container);    
            return input;    
        },    
        getValue: function(target){ 
            return $(target).val();    
        },    
        setValue: function(target, value){
        	$(target[0]).val(value);  
        	$(target[1]).val("...");  
        },    
        resize: function(target, width){    
        }    
    }    
});

//新增箱号，弹出选择箱号对话框
billimreceipt.showBoxDialog = function(){
	//清除列表信息
	$('#dataGridJG_Box').datagrid('loadData',[]);
	$('#dataGridJG_BoxDetail').datagrid('loadData',[]);

	billimreceipt.clearSearchBox("init");
	$("#boxDialog").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$("#boxDialog").window('open');
	
	
};

//删除一行
billimreceipt.deleterow = function(){
	var obj = $('#dataGridJG_detail');
	var row = obj.datagrid('getSelected');
	if(row==null){
		return;
	}
	var rows = obj.datagrid('getRows');
	var boxNo=row.boxNo;
	var delList=[];
	
	$.each(rows,function(index,item){
		if(boxNo==item.boxNo){
			delList.push(item);
		}
	});
	$.each(delList,function(index,item){
		var i = obj.datagrid('getRowIndex', item);
		$('#dataGridJG_detail').datagrid('deleteRow',i);
	});
	
};

//关闭详情框
billimreceipt.coloseDetailDialog = function(){
	$('#detailDialog').window('close');
};
//关闭详情框
billimreceipt.closeWin = function(id){
	$('#'+id).window('close');
};


//修改
billimreceipt.edit = function(){
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = billimreceipt.endEdit("dataGridJG_detail");
	if(!tempFlag){
		alert('数据验证没有通过!',1);
		return;
	}
	
	var tempObj = $('#dataGridJG_detail');

	var inserted = tempObj.datagrid('getChanges', "inserted");
	var updated = tempObj.datagrid('getChanges', "updated");
	var deleted = tempObj.datagrid('getChanges', "deleted");
	
    var fromObj=$('#detailForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
        return ;
    }
   var repeatArry =  billimreceipt.getRepeat(tempObj.datagrid('getRows'));
   if(repeatArry!=null&&repeatArry.length>0){
   		var repeatBox = "";
   		for(var i=0;i<repeatArry.length;i++){
   			repeatBox=repeatBox+repeatArry[i];
   		}
   		alert('保存失败,箱号'+repeatBox+"已经存在，请重新选择",2);
   		return;
   }
     //2.绑定数据
   wms_city_common.loading("show");
    var updateFn = function(returnData){
        var url = BasePath+'/bill_im_receipt/edit';
        fromObj.form('submit', {
   			url: url,
   			onSubmit: function(param){
				param.inserted = encodeURIComponent(JSON.stringify(inserted));
				param.updated = encodeURIComponent(JSON.stringify(updated));
				param.deleted = encodeURIComponent(JSON.stringify(deleted));
   			},
   			success: function(result){
   				wms_city_common.loading();
   				var resultDate = $.parseJSON(result);
   				if(resultDate.result=="fail"){
   					if(resultDate.msg==null){
   						alert('保存失败,请联系管理员!',2);
   					}else{
   						alert(resultDate.msg,1);
   					}
   				}else{
   					if(resultDate.repeat!=null&&resultDate.repeat.length>0){
   						var restr = "";
   						for(var i=0,length=resultDate.repeate.length;i<length;i++){
   							if(i==length-1){
   								restr+=resultDate.repeate[i];
   							}else{
   								restr+=resultDate.repeate[i]+",";
   							}
   						}
   						alert('保存失败,箱号'+restr+"已经存在，请重新选择",2);
   					}else if(resultDate.noused!=null&&resultDate.noused.length>0){
   						var restr = "";
   						for(var i=0,length=resultDate.noused.length;i<length;i++){
   							if(i==length-1){
   								restr+=resultDate.noused[i];
   							}else{
   								restr+=resultDate.noused[i]+",";
   							}
   						}
   						alert('保存失败,箱号'+restr+"不可用，请重新选择",2);
   					}else{
   						alert('保存成功!');
						billimreceipt.loadDataGrid();
						billimreceipt.coloseDetailDialog();
   					}
   				}
   		    },
   			error:function(){
   				wms_city_common.loading();
   				alert('保存失败,请联系管理员!',2);
   			}
   	   });
    };
    billimreceipt.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,updateFn);
};

billimreceipt.getRepeat = function(tempary) {
var ary=[];
for(var i = 0;i<tempary.length;i++){
	ary.push(tempary[i].boxNo+'_'+tempary[i].importNo);
}
var res = [];  
ary.sort();  
for(var i = 0;i<ary.length;){
 var count = 0;  
 for(var j=i;j<ary.length;j++){
  if(ary[i]== ary[j]) {  
   count++;  
  }    
 }
 if(count>1){
 	res.push(ary[i]); 
 }
 i+=count;   
};
return res;
};

billimreceipt.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		tempObj.datagrid('endEdit', i);
    	}else{
    		return false;
    	}
    }
    return true;
};

//显示单号输入框
billimreceipt.showReceiptNo = function(type){
	if(type=="1"){
		$("#receiptNoHide").val("系统自动生成");
	}
	$("#receiptNoHide").attr("disabled",true);
};

//查询箱号主表信息
billimreceipt.searchBox = function(){
	var fromObjStr=convertArray($('#boxForm').serializeArray());
	var queryMxURL=BasePath+'/billimimport/findImportNoByPage?locno='+billimreceipt.locno;
	var params = eval("(" +fromObjStr+ ")");
	
    $( "#dataGridJG_Box").datagrid( 'options' ).queryParams= params;
    $( "#dataGridJG_Box").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_Box").datagrid( 'load' );
};

billimreceipt.getDate = function(n){
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

billimreceipt.clearSearchBox = function(type){
	$("#boxForm").form("clear");
	//委托业主
	var ownerNo = $("#ownerNo").combobox("getValue");
	$("#boxOwnerNo").combobox("select",ownerNo);
	$("#boxOwnerNo").combobox("disable");
	$("#boxOwnerNoHide").val(ownerNo);
//	$("#boxSupplierNo").val($("#supplierNo").combobox("getValue"));
	////
	if(type=="init"){
		$("#boxorderDateStart").datebox('setValue',billimreceipt.getDate(-6));
		$("#boxorderDateEnd").datebox('setValue',billimreceipt.getDate(0));
	}else{
		$("#boxorderDateStart").datebox('setValue','');
		$("#boxorderDateEnd").datebox('setValue','');
	}
	
};
//查询箱号详情信息
billimreceipt.searchBoxDetail = function(rowIndex,rowData){
	var queryMxURL=BasePath+'/bill_im_import_dtl/detail';
	var param = {
		"locno":rowData.locno,
		"ownerNo":rowData.ownerNo,
		"importNo":rowData.importNo,
		"brandNo":$("#brandNo").combobox("getValue"),
		"deliverNo":$("#deliverNo").val()
	};
	param = JSON.stringify(param);
    $( "#dataGridJG_BoxDetail").datagrid( 'options' ).queryParams= eval("(" +param+ ")");
    $( "#dataGridJG_BoxDetail").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_BoxDetail").datagrid( 'load' );
};

//确定选择的箱号
billimreceipt.selectBoxOk = function(){
	var checkedRows = $("#dataGridJG_BoxDetail").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择箱号!',1);
		return;
	}
	var target = $('#dataGridJG_BoxDetail');
	var row = target.datagrid('getChecked');
	var list;
	var manyImport=false;
	$.each(row,function(index,item){
		list=item.boxList;
		if(list.length>1){
			manyImport=true;
		}
		wms_city_common.loading("show");
		var sImportNo=[];
		var qty=parseInt(0);
		$.each(list,function(i,d){
			sImportNo.push(d.sImportNo);
			qty+=parseInt(d.qty);
		});
		var  rowData = {
				"boxNo":item.boxNo,
		    	"importNo":sImportNo.join(','),
		    	"brandName":item.brandName,
		    	"spoNo":item.spoNo,
		    	"qty":qty,
		    	"locno":item.locno,
		    	"deliverNo":item.deliverNo
		};
		$("#dataGridJG_detail").datagrid('appendRow', rowData);
		wms_city_common.loading();  	
	});
	if(manyImport){
		alert('存在一箱多单记录，将载入其它预到货通知单的数据!',1);
	}
	$('#boxDialog').window('close');
};

billimreceipt.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};


//详情
billimreceipt.showDetail = function(rowData){
	$('#detailDialogView').window({
		title:"收货单明细"
	 });
	$("#detailDialogView").window('open'); 
	$("#detailForm_view").form('load',rowData);
	
	$("#supplierNo_view").combo('setText',rowData.supplierNo+'→'+rowData.supplierName);
	
	$("#ownerNo_view").combobox("disable");
	$("#supplierNo_view").combobox("disable");
	$("#receiptWorker_view").combobox("disable");
	$("#dockNo_view").combobox("disable");
	
	var queryMxURL=BasePath+'/bill_im_receipt_dtl/selectItemDetail?receiptNo='+rowData.receiptNo+'&locno='+rowData.locno+'&orderByField=d.import_no,d.box_no,d.item_no,d.size_no';
	$( "#dataGridJG_detail_view").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_detail_view").datagrid( 'load' );
    
	var queryParams = {};
	queryParams.receiptNo = rowData.receiptNo;
	queryParams.locno = rowData.locno;
	billimreceipt.getBoxQty(queryParams);
	/*****获取动态头****/
	var resultColumns = billimreceipt.getColumnInfo(queryParams);
	if(resultColumns==null){
		 alert("没有查询到数据",1);
		 $("#moduleView").datagrid({
			 queryParams:queryParams,
			 url:queryMxURL,
			 columns:{}
	    });
	 } else {
		 var queryMxURL=BasePath+'/bill_im_receipt_dtl/selectItemDetail4SizeHorizontal';
			$("#moduleView").datagrid({
				queryParams : queryParams,
				url : queryMxURL,
				columns : resultColumns.returnCols
			}); 
	 }
	

};

billimreceipt.getColumnInfo = function(params) {
	var beforeColArr = billimreceipt.preColNames;
	var afterColArr = billimreceipt.endColNames; 
	var tempUrl = BasePath+'/bill_im_receipt_dtl/initHead';
    params.preColNames = JSON.stringify(beforeColArr);
    params.endColNames = JSON.stringify(afterColArr);
    params.sizeTypeFiledName = billimreceipt.sizeTypeFiledName;
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

billimreceipt.getBoxQty = function(params){
	
	 $.ajax({
	        type: 'POST',
	        url: BasePath+'/bill_im_receipt_dtl/getDetailBoxQty',
	        data:params,
	        cache: true,
	        async: true,
	        success: function(returnData){
	        	$("#params").val(returnData.boxQty);
			}
	      });
}

//加载品牌
billimreceipt.initBrand = function(){
	$('#brandNo').combobox({    
		 valueField:'id',    
		 textField:'text',
		 panelHeight:150,
		 url:BasePath+'/brand/queryBrandTree'
	}); 
};

//根据预到货通知单，批量添加箱记录
billimreceipt.batchSelectBoxOk = function(){
	var checkedRows = $("#dataGridJG_Box").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要添加的预到货通知单!',1);
		return;
	}
	var importNos = [];
	var ownerNo;
	var locNo;
	
	$.each(checkedRows, function(index, item){
		importNos.push("importNos="+item.importNo);
		if(index==0){
			ownerNo = item.ownerNo;
			locNo = item.locno;
		}
	});   
	$.messager.confirm("确认","将添加所选通知单的未收货箱明细。", function (r){  
        if (r) {
        	wms_city_common.loading("show");
            var url = BasePath+'/bill_im_receipt/findBatchSelectBox?ownerNo='+ownerNo+'&locNo='+locNo+'&'+importNos.join('&');
			$.ajax({
				  type: 'POST',
				  url: url,
				  cache: true,
				  success: function(data){
					  $.each(data.list,function(i,d){
								var  rowData = {
							    		"boxNo":d.boxNo,
							    		"importNo":d.importNo,
							    		"brandName":d.brandName,
							    		"spoNo":d.spoNo,
							    		"qty":d.qty,
							    		"locno":d.locno,
							    	    "deliverNo":d.deliverNo
						        };
						         $("#dataGridJG_detail").datagrid('appendRow', rowData);
						});
					  wms_city_common.loading();
					  $('#boxDialog').window('close');
				  }
			});
        }  
    });  
};
billimreceipt.print = function(){
	var checkedRows = $("#dataGridJG").datagrid("getRows");// 获取所有行
	if(checkedRows.length < 1){
		alert('没有数据，不能打印!',1);
		return;
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_im_receipt/print?locno='+billimreceipt.locno+'&businessType=0',
        data: $('#searchForm').serialize(),
        success: function(data){
        	wms_city_common.loading();
        	if(data.result=="success"){
        		billimreceipt.printImReceipt(data);
        	}else{
        		alert(data.msg,2);
        	}
		}
    });
};
billimreceipt.printImReceipt = function(data){
	var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	if(LODOP==null){
		return;
	}
	LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
	LODOP.NewPageA();
	var headHtml = billimreceipt.createHeadHtml();
	var bodyHtml = billimreceipt.createBodyHtml(data.rows);
	//设置表格内容
	LODOP.ADD_PRINT_TABLE(85,0,"100%",380,strStyle+bodyHtml);
	LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
	//设置表格头部
	LODOP.ADD_PRINT_HTM(10,10,"100%",120,headHtml);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
};
billimreceipt.createHeadHtml = function(){
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>收货单</td></tr>";
	return html;
};
billimreceipt.createBodyHtml =  function(page){
	var html = "";
	html = "<table border='0' cellpadding='1' cellspacing='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'><thead>";
	html += "<tr style='background-color: #fff;'>";
	html += "<td>品牌</td><td>收货日期</td><td>收货单号</td><td>供应商编码</td><td>供应商</td><td>商品数量</td><td>总箱数</td><td>审核人</td></tr>";
	html += "</thead>";
	var totalBoxqty = 0;
	var totalReceiptqty = 0;
	for(var x=0;x<page.length;x++){
		row = page[x];
		html += "<tr style='background-color: #fff;'>";
		if(row.brandNo == "" || row.brandNo == "null" || row.brandNo == null){
			html += "<td>&nbsp;</td>";
		}else{
		    html += "<td>"+row.brandNo.substring(0,2)+"</td>";
		}
		html += "<td>"+row.recivedate+"</td>";
		html += "<td>"+row.receiptNo+"</td>";
		if(row.supplierNo == null){
			html += "<td>"+""+"</td>";
		}else{
			html += "<td>"+row.supplierNo+"</td>";
		}
		if(row.supplierName == null){
			html += "<td>"+""+"</td>";
		}else{
			html += "<td>"+row.supplierName+"</td>";
		}
		html += "<td>"+row.receiptqty+"</td>";
		html += "<td>"+row.boxqty+"</td>";
		if(row.auditor == "" || row.auditor == "null" || row.auditor == null){
			html += "<td>&nbsp;</td>";
		}else{
			html += "<td>"+row.auditor+"</td>";
		}
		totalBoxqty += row.boxqty;
		totalReceiptqty +=  row.receiptqty;
		html += "</tr>";
	}
	html += "<tr><td></td><td></td><td></td><td></td><td>合计</td><td>"+totalReceiptqty+"</td><td>"+totalBoxqty+"</td><td></td></tr>";
	html = html + "<tfoot><tr><td style='text-align:13px;text-align:center;' colspan=7><font tdata='pageNO' format = '#' align='center'>第#页</font>&nbsp;/&nbsp;<font format='#' tdata='pageCount'>共#页</font></td></tr></tfoot>";
	html += "</table>";
	return html;
};

billimreceipt.exportDetail = function(){
	exportExcelBill4Detail(billimreceipt.preColNames,billimreceipt.endColNames, "收货明细导出");
};


function exportExcelBill4Detail(preColNames,endColNames,fileName){
	
	var url=BasePath+'/bill_im_receipt_dtl/do_export4Detail';
	var receiptNo = $('#detail_receiptNo').val();
	var DetailView = $("#dataGridJG_detail_view").datagrid('getRows');
	if(DetailView.length > 0){
		$("#exportExcelForm").remove();
		$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
		var fromObj = $('#exportExcelForm');
		wms_city_common.loading("show","正在导出");
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {
				param.locno=billimreceipt.locno;
				param.receiptNo=receiptNo;
				param.preColNames = JSON.stringify(preColNames);
				param.endColNames = JSON.stringify(endColNames);
				param.fileName = fileName+"("+receiptNo+")";
			},
			success : function() {
			}
		});
		wms_city_common.loading();
	}else{
		alert('数据为空，不能导出!',1);
	}
}
billimreceipt.directCheck = function(){
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length == 0){
		alert("请选择需要转单的数据!");
		return;
	}
	var nos = '';
	for(var idx=0;idx<rows.length;idx++){
		if(rows[idx].status != '20'){
			alert("只能选择【收货完成】的单据!");
			return;
		}
		nos += rows[idx].receiptNo;
		if(idx != rows.length - 1){
			nos += ',';
		}
		
	}
	var params = {
			nos:nos,
			locno:billimreceipt.locno
	};
	var url = BasePath+'/bill_im_check/directCheck';
	$.messager.confirm("确认","是否确认验收转单？", function (r){  
        if (r) {
        	wms_city_common.loading("show");
        	billimreceipt.ajaxRequest(url,params,true,function(data){
        		if(data){
        			if(data.status == 'success'){
        				billimreceipt.searchArea();
        				alert("转单成功!");
        			}else{
        				alert(data.msg);
        			}
        		}
        		wms_city_common.loading();
        	});
        }
	});
};