var billomdeliverFlag = {};
billomdeliverFlag.user={};

billomdeliverFlag.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

$(document).ready(function(){
	
	billomdeliverFlag.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(user){billomdeliverFlag.user=user;});
	//初始化委托业主
	billomdeliverFlag.ajaxRequest(BasePath+'/entrust_owner/get_biz',{},false,billomdeliverFlag.initOwnerNo);
	//初始化仓库
	billomdeliverFlag.ajaxRequest(BasePath+'/bmdefloc/get_biz',{},false,billomdeliverFlag.initLocno);
	//
	billomdeliverFlag.ajaxRequest(BasePath+'/os_line_buffer/get_biz',{"locno":billomdeliverFlag.user.locno},false,billomdeliverFlag.initDeliverArea);
	//初始化状态
	billomdeliverFlag.init_IMPORT_STATUS();
	//初始化出货人
	billomdeliverFlag.init_sendName();
	//出货码头
	billomdeliverFlag.initDock();
	//商圈
	billomdeliverFlag.loadCircleNo();
	
	wms_city_common.closeTip("openUI");
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('input[id=brandNo]', $('#searchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs);
	
	//总计
	$('#deliverDetail').datagrid({
		'onLoadSuccess' : function(data) {
			if (data.footer[1].isselectsum) {
				billomdeliverFlag.qty = data.footer[1].qty;
			} else {
				var rows = $('#deliverDetail').datagrid('getFooterRows');
				rows[1]['qty'] = billomdeliverFlag.qty;
				$('#deliverDetail').datagrid('reloadFooter');
			}
		}
	});
	$('#startCreatetm').datebox('setValue',getDateStr(-2));
});

//========================仓库代码======================================
billomdeliverFlag.locno = {};
billomdeliverFlag.locnoFormatter = function(value, rowData, rowIndex){
	return billomdeliverFlag.locno[value];
};
//加载仓库
billomdeliverFlag.initLocno = function(data){
	$('#locno').combobox({
	    data:data,
	    valueField:'locno',    
	    textField:'locname',
	    panelHeight:"auto",
	});
	billomdeliverFlag.locno = billomdeliverFlag.tansforLocno(data);
};
billomdeliverFlag.tansforLocno = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].locno +"\":\""+data[i].locname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
	
};
//========================仓库代码======================================

//==================状态====================
billomdeliverFlag.init_status_Text = {};
billomdeliverFlag.columnStatusFormatter = function(value, rowData, rowIndex){
	return billomdeliverFlag.init_status_Text[value];
};
billomdeliverFlag.init_IMPORT_STATUS = function(){
	wms_city_common.comboboxLoadFilter(
			["searchStatus","status","statusCondition"],
			null,
			null,
			null,
			true,
			[true, false, false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_LOADCAR_STATUS',
			{},
			billomdeliverFlag.init_status_Text,
			null);
};
//将数组封装成一个map
billomdeliverFlag.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//==================状态====================

//==================委托业主====================
billomdeliverFlag.ownerNo = {};
billomdeliverFlag.ownerNoFormatter = function(value, rowData, rowIndex){
	return billomdeliverFlag.ownerNo[value];
};
billomdeliverFlag.initOwnerNo = function(data){
	$('#ownerNo').combobox({
		valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto",
	  });
	$('#ownerDtlNo').combobox({
		valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto",
	  });
	billomdeliverFlag.ownerNo = billomdeliverFlag.tansforOwner(data);
};
billomdeliverFlag.tansforOwner = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//==================委托业主====================
//==================发货人====================
billomdeliverFlag.sendNo = {};
billomdeliverFlag.columnSendNoFormatter = function(value, rowData, rowIndex){
	return billomdeliverFlag.sendNo[value];
};
billomdeliverFlag.init_sendName = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#sendName').combobox({
		     valueField:"workerNo",
		     textField:"unionName",
		     data:data,
		     panelHeight:150
			});
			$('#sendDtlName').combobox({
			     valueField:"workerNo",
			     textField:"unionName",
			     data:data,
			     panelHeight:150
			});
			billomdeliverFlag.sendNo = billomdeliverFlag.tansforSendNo(data);
		}
	});
	wms_city_common.comboboxLoadFilter(
			["searchSendName"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
};

billomdeliverFlag.tansforSendNo = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].workerNo +"\":\""+data[i].workerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//==================发货人====================
//==================出货码头====================
billomdeliverFlag.initDock = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		'url':BasePath+'/bm_defdock/get_biz?dockType=4&locno='+billomdeliverFlag.user.locno,
		success : function(data) {
			$('#dockNo').combobox({
			    data:data,
			    valueField:'dockNo',    
			    textField:'dockName',
			    panelHeight:200
			}).combobox("select",data[0]==""||data[0]==null?"":data[0].dockNo);
			$('#dockDtlNo').combobox({
			    data:data,
			    valueField:'dockNo',    
			    textField:'dockName',
			    panelHeight:200
			}).combobox("select",data[0]==""||data[0]==null?"":data[0].dockNo);
		}
	});
};
//==================出货码头====================
//==================商圈====================
billomdeliverFlag.loadCircleNo = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/bmcircle/get_biz?locno='+billomdeliverFlag.user.locno,
		success : function(data) {
			$('#circleNo').combobox({
				data:data,
			    valueField:'circleNo',    
			    textField:'valueAndText',
			    panelHeight:200,
			    onSelect:function(){
			    	billomdeliverFlag.initStoreNo();
				},
				loadFilter:function(data){
					if(data){
						for(var i = 0;i<data.length;i++){
							var tempData = data[i];
							tempData.valueAndText = tempData.circleNo+'→'+tempData.circleName;
						}
					}
					return data;
				}
			});
		}
	});
};
//==================商圈====================
//==================客戶（级联）====================
billomdeliverFlag.initStoreNo = function(){
	var circleNo = $('#circleNo').combobox("getValue");
	if(circleNo != "") {
//		$.ajax({
//			async : false,
//			cache : true,
//			type : 'POST',
//			dataType : "json",
//			data:{
//				"locno":billomdeliverFlag.user.locno,
//				"circle":circleNo
//			},
//			url:BasePath+'/bill_om_deliver_dtl/selectFlagStore',
//			success : function(data) {
//				$('#storeNo').combobox({
//					valueField:"storeNo",
//				    textField:"storeName",
//				    data:data,
//				    panelHeight:200
//				}).combobox("select",data[0]==""||data[0]==null?"":data[0].areaNo);
//			}
//		});
		$('#storeNo').combogrid({
			delay: 1500,
			panelWidth:450,   
	        idField:'storeNo',  
	        textField:'storeName',   
	        pagination : true,
	        rownumbers:true,
	        mode: 'remote',
	        url:BasePath+'/store/list.json?storeType=11&circle='+circleNo,
//	        url:BasePath+'/bill_om_deliverflag_dtl/selectFlagStore?locno='+billomdeliverFlag.user.locno+"&circle="+circleNo,
	        columns:[[
				{field:'storeNo',title:'客户编码',width:140},  
				{field:'storeName',title:'客户名称',width:180}  
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
	} else {
		$('#storeNo').combobox("clear");
	}
	
};
//==================客戶（级联）====================
//==================主窗口查询、清空====================
//查询
billomdeliverFlag.searchData = function(){
	 //1.校验必填项
	var fromObj=$('#searchForm');
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
		return;
	}
 	var startExpDate =  $('#startExpDate').datebox('getValue');
	var endExpDate =  $('#endExpDate').datebox('getValue');
	if(!isStartEndDate(startExpDate,endExpDate)){    
		alert("发货日期开始日期不能大于结束日期");   
		return;   
	} 
	var startCreatetm =  $('#startCreatetm').datebox('getValue');
	var endCreatetm =  $('#endCreatetm').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
		return;   
	}
	
	var startAudittm =  $('#startAudittm').datebox('getValue');
	var endAudittm =  $('#endAudittm').datebox('getValue');
	if(!isStartEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
       return;   
   } 
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_deliverflag/deliverlist.json?flag=10&locno='+billomdeliverFlag.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	$.extend(reqParam,{locno:billomdeliverFlag.user.locno});
	$('#dataGridJG').datagrid({    
	    url:queryMxURL,
	    queryParams:reqParam
	});  
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
//清空
billomdeliverFlag.clearSearchCondition = function(){
	$("#searchForm").form('clear');
	$('#brandNo').combobox("loadData",[]);
};
//==================主窗口查询、清空====================
//打开 明细窗口
billomdeliverFlag.openDtlUI = function(opt){
	$('#openDtlUI').window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openDtlUI').window('open');
};
//关闭 明细窗口
billomdeliverFlag.closeDtlUI = function(opt){
	$('#openDtlUI').window('close');
	$('#dataGridJGItem').datagrid('loadData', { total: 0, rows: [] });
};
//明细
billomdeliverFlag.dtlView = function(rowData,type){
	if(type=="view"){
		$('#deliverDetail').datagrid(
				{'url':BasePath+'/bill_om_deliverflag_dtl/viewDtl.json?locno='+billomdeliverFlag.user.locno + '&deliverNo='+rowData.deliverNo,'pageNumber':1 });
		
		$("#ownerDtlNo").combobox('disable');
		$("#cartypeDtlNo").combobox('disable');
		$("#expDtlDate").datebox('disable');
		$("#sendDtlName").combobox('disable');
		$("#dockDtlNo").combobox('disable');
		$("#carDtlPlate").attr("disabled",true);
		$("#remarksDtl").attr("disabled",true);
		
		$("#deliverDtlNo").val(rowData.deliverNo);
		$("#carDtlPlate").val(rowData.carPlate);
		$("#remarksDtl").val(rowData.remarks);
		$("#sendDtlName").combobox("select",rowData.sendName);
		$("#dockDtlNo").combobox("select",rowData.dockNo);
		$("#expDtlDate").datebox("setValue",rowData.expDate);
		$("#ownerDtlNo").combobox("select",rowData.ownerNo);
		$("#cartypeDtlNo").combobox("select",rowData.cartypeNo);
		billomdeliverFlag.openDtlUI("查看");
	}
};
//删除
billomdeliverFlag.deleteRows = function(){
	//1.判断是否选择了记录
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var isDel = true;
	var deliver = "";
	var keys = [];
	for(var i=0;i<checkedRows.length;i++){
		if(checkedRows[i].status == 13){
			deliver += checkedRows[i].deliverNo + "\n";
			isDel = false;
		}
		keys.push(billomdeliverFlag.user.locno+"-"+checkedRows[i].deliverNo+"-"+checkedRows[i].ownerNo);
	}
	if(!isDel) {
		alert("单据:" + deliver +"已经装车完成，请重新选择");
		return;
	}
     //2.绑定数据
     var url = BasePath+'/bill_om_deliverflag/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		 if (r){
			 wms_city_common.loading("show");
			 billomdeliverFlag.ajaxRequest(url,data,true,function(result){
				 wms_city_common.loading();
				 if(result.success=="true"){
					 //4.删除成功,清空表单
					 alert('删除成功!');
				 }else{
					 alert('操作异常：'+result.msg,1);
				 }
				 billomdeliverFlag.searchData();
			});  
		    }    
		});
};
//审核
billomdeliverFlag.check = function(){
	var checkRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}else{
		var isDel = true;
		var deliver = "";
		var checkIds = [];
		for(var i=0;i<checkRows.length;i++){
			if(checkRows[i].status == 13){
				deliver += checkRows[i].deliverNo + "\n";
				isDel = false;
			}
			checkIds.push(billomdeliverFlag.user.locno + 
					"-" + checkRows[i].ownerNo +
					"-" + checkRows[i].deliverNo);
		}
		if(!isDel) {
			alert("单据:" + deliver +"已经装车完成，请重新选择");
			return;
		}
		
		for(var i=0;i<checkRows.length;i++){
			var ownerNo = checkRows[i].ownerNo;
			var deliverNo = checkRows[i].deliverNo;
			var untreadDtlUrl = BasePath+'/bill_om_deliverflag_dtl/viewDtl.json?locno='+billomdeliverFlag.user.locno
			+ '&deliverNo='+ deliverNo + '&ownerNo='+ownerNo;
			var existDtl = false;
			billomdeliverFlag.ajaxRequest(untreadDtlUrl,{},false,function(result){
    			 if(result.total>0){
    				 existDtl = true;
    			 }
    		});
			if(!existDtl){
				alert("装车单["+deliverNo+"]不存在明细,不允许审核!",2);
				return;
			}
		}
		$.messager.confirm("确认","审核后数据将无法修改，您确定并继续操作吗？", function (r){  
	        if (r) {
	    		var url = BasePath+'/bill_om_deliverflag/check';
	    		var data = {ids:checkIds.join(",")}; 
	    		wms_city_common.loading("show");
	    		$.ajax({
	    			async : true,
					cache : false,
					type : 'POST',
					dataType : "json",
					data: data,
					url:url,
					success : function(data) {
						wms_city_common.loading();
						if(data.flag=="warn") {
							alert('审核失败: ' + data.msg,2);
						} else {
							alert(data.msg);
						}
						billomdeliverFlag.searchData();
					}
				});
	        }
		});
	}
	
};

//新增窗口
billomdeliverFlag.addUI = function(opt){
	$("#containerType+ span input.combo-text").attr("readOnly",false);
	$("#containerType+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#containerType+ span").css("background-color","white");
	$("#containerType+ span input.combo-text").css("background-color","white");
	
	$("#useType+ span input.combo-text").attr("readOnly",false);
	$("#useType+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#useType+ span").css("background-color","white");
	$("#useType+ span input.combo-text").css("background-color","white");
	
	billomdeliverFlag.clearAll();
};

billomdeliverFlag.clearAll = function(){
	$("#save_main_info").show();
	$("#detailDiv,#btn-modify").hide();
	$("#opt").val("add");
	
	$("#add_row").linkbutton('enable');
	$("#del_row").linkbutton('enable');
	$("#save_row").linkbutton('enable');
	
	$("#ownerNo").combobox('enable');
	$("#cartypeNo").combobox('enable');
	$("#expDate").datebox('enable');
	$("#sendName").combobox('enable');
	$("#dockNo").combobox('enable');
	$("#carPlate").attr("disabled",false);
	$("#remarks").attr("disabled",false);
	$('#dataForm').form("clear");
	
	 var item = $('#itemDetail').datagrid('getRows');
     if (item) {
         for (var i = item.length - 1; i >= 0; i--) {
             var index = $('#itemDetail').datagrid('getRowIndex', item[i]);
             $('#itemDetail').datagrid('deleteRow', index);
         }
     }
     billomdeliverFlag.openUI("新增");
};

//打开 新增/修改窗口
billomdeliverFlag.openUI = function(opt){
	$('#openUI').window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('open');
};
//关闭 新增/修改窗口
billomdeliverFlag.closeUI = function(opt){
	$('#openUI').window('close');
	$("#opt").val("");
};
//打开 明细窗口
billomdeliverFlag.openDtlUI = function(opt){
	$('#openDtlUI').window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openDtlUI').window('open');
};
//关闭 明细窗口
billomdeliverFlag.closeDtlUI = function(opt){
	$('#openDtlUI').window('close');
	$('#dataGridJGItem').datagrid('loadData', { total: 0, rows: [] });
};
//==================新增主表====================
billomdeliverFlag.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
//新增主表
billomdeliverFlag.saveMainInfo = function(){
	var url = BasePath+ '/bill_om_deliverflag/saveMainInfo';
	var carPlate = encodeURIComponent($("#carPlate").val());
	var remarks = encodeURIComponent($("#remarks").val());
	wms_city_common.loading("show");  
	if($('#dataForm').form('validate')){
		  $('#dataForm').form('submit', {
				url: url,
				onSubmit: function(param){
					param.locno = billomdeliverFlag.user.locno;
					param.carPlate1 = carPlate;
					param.remarks1 = remarks;
				},
				success: function(r){
					wms_city_common.loading();
					r = billomdeliverFlag.parseJsonStringToJsonObj(r);
					if(r && r.success=="true"){
						$("#deliverNo").val(r.deliverNo);
						$("#save_main_info").hide();
						$("#detailDiv,#btn-modify").show();
						
						$("#ownerNo").combobox('disable');
						$("#cartypeNo").combobox('disable');
						$("#expDate").datebox('disable');
						$("#sendName").combobox('disable');
						$("#dockNo").combobox('disable');
						$("#carPlate").attr("disabled",true);
						$("#remarks").attr("disabled",true);
						$('#itemDetail').datagrid('clearData');
						
						alert('保存成功!');
						return;
					}else{
						alert('保存失败!');
						return;
					}
			    },
				error:function(){
					wms_city_common.loading();
					alert('保存失败,请联系管理员!',2);
					return;
				}
		   });
	} else {
		wms_city_common.loading();
	}
};
//==================新增主表====================
//==================箱号选择窗口====================
//弹出选择商品的页面
billomdeliverFlag.addrow = function(gid){
	$("#dataGridJGItem").datagrid('clearData');
	$('#itemSearchForm').form("clear");
	
	$('#showGirdName').val(gid);
	var fromObj=$('#dataForm');
	$('#sysNoForItem').val($('input[id=sysNoHide]',fromObj).val());
	
	billomdeliverFlag.openUIItem("箱号选择");
};
//打开 明细窗口
billomdeliverFlag.openUIItem = function(opt){
	$('#openUIItem').window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUIItem').window('open');
};
billomdeliverFlag.closeOpenUIItem = function(){
	$("#openUIItem").window('close');
};
//==================箱号选择窗口====================
//==================箱号选择查询、清空、关闭====================
billomdeliverFlag.searchItem = function(){
	var formArray = $('#itemSearchForm').serializeArray();
	var locnoParam = {"locno":billomdeliverFlag.user.locno};
	var fromObjStr=convertArray(formArray);
	var queryMxURL=BasePath+'/bill_om_deliverflag_dtl/boxSelectQuery';
	var paramsObj = eval("(" +fromObjStr+ ")");
	$.extend(paramsObj,locnoParam); 
	console.info(paramsObj);
    //3.加载明细
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams= paramsObj;
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
};
billomdeliverFlag.clearSearch = function(){
	$("#itemSearchForm").form('clear');
	$('#storeNo').combobox("clear");


};
//==================箱号选择查询、清空、关闭====================
//==================明细修改====================
billomdeliverFlag.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		billomdeliverFlag.loadDetail(checkedRows[0],"edit");
	}
};
billomdeliverFlag.loadDetail = function(rowData,type){
	$("#opt").val("");
	if(type=="edit"){
		$('#dataForm').form('load',rowData);
		$("#save_main_info").hide();
		$("#detailDiv,#btn-modify").show();
		$('#itemDetail').datagrid(
				{'url':BasePath+'/bill_om_deliverflag_dtl/findLoadproposeDeliverDtlBoxByPage?deliverNo='+rowData.deliverNo
					+'&locno='+rowData.locno
					+'&ownerNo='+rowData.ownerNo,'pageNumber':1 });
		if(rowData.status!="10") {
			$("#add_row").linkbutton('disable');
			$("#del_row").linkbutton('disable');
			$("#save_row").linkbutton('disable');
		} else {
			$("#add_row").linkbutton('enable');
			$("#del_row").linkbutton('enable');
			$("#save_row").linkbutton('enable');
		}
		$("#ownerNo").combobox('disable');
		$("#cartypeNo").combobox('disable');
		$("#sendName").combobox('disable');
		$("#dockNo").combobox('disable');
		$("#expDate").datebox('disable');
		$("#carPlate").attr("disabled",true);
		$("#remarks").attr("disabled",true);
		
		billomdeliverFlag.openUI("修改");
	}
};
//==================明细修改====================
//==================箱号选择确定====================
billomdeliverFlag.confirmItem = function(){
	var checkedRows = $("#dataGridJGItem").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要添加的箱号！',1);
		return;
	}
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"条信息吗？", function (r){  
        if (r) {
        	wms_city_common.loading("show");
            $.each(checkedRows, function(index, item){
        	    var  rowData = {
        	    		"loadproposeNo":item.loadproposeNo,
        	    		"tmsDeliverNo":item.tmsDeliverNo,
        	    		"containerNo":item.containerNo,
        	    		"boxNo":item.boxNo,
        	    		"storeNo":item.storeNo,
        	    		"storeName":item.storeName,
        	    		"expNo":item.expNo,
        	    		"qty":item.qty
        	    };
        	  
        		//把选择的商品编码行记录插入到父窗体中
        	    billomdeliverFlag.insertRowAtEnd($('#showGirdName').val(),rowData);
        	    billomdeliverFlag.closeOpenUIItem();
            }); 
            wms_city_common.loading();
        }
	});
};
billomdeliverFlag.insertRowAtEnd = function(gid,rowData){
	var tempObj = $('#'+gid);
	if(rowData){
		tempObj.datagrid('appendRow', rowData);
	}else{
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	tempObj.datagrid('beginEdit', tempIndex);
	return  tempIndex;
};
//==================箱号选择确定====================
//==================明细删除====================
billomdeliverFlag.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};
//==================明细删除====================
//==================明细保存====================
billomdeliverFlag.endEdit = function(gid){
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
billomdeliverFlag.checkExistFun = function(url,checkColumnJsonData){
	var checkExist=false;
 	$.ajax({
		  type: 'POST',
		  url: url,
		  data: checkColumnJsonData,
		  cache: true,
		  async:false, // 一定要
		  success: function(totalData){
			  totalData = parseInt(totalData,10);
			  if(totalData>0){
				  checkExist=true;
			  }
		  }
     });
 	return checkExist;
};
billomdeliverFlag.saveDtlInfo = function(id){
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = billomdeliverFlag.endEdit(id);
	if(!tempFlag){
		alert('数据验证没有通过！',1);
		return;
	}
	var fromObj=$('#dataForm');
	var showWinObj = 'openUI';
	
	var tempObj = $('#'+id);
	var chgSize = tempObj.datagrid('getChanges').length;
	if(chgSize<1){
		alert('没有数据改动！',1);
		return;
	}
	//新增
	var inserted = tempObj.datagrid('getChanges', "inserted");
    //删除
	var deleted = tempObj.datagrid('getChanges', "deleted");
    //更新
	var updated = tempObj.datagrid('getChanges', "updated");
    
    
    var checkUrl=BasePath+'/bill_om_deliverflag_dtl/get_count.json';
    var checkData={
    		"locno" : billomdeliverFlag.locno,
    		"deliverNo" : $('#deliverNo').val(),
    		"ownerNo" : $('#ownerNo').combobox('getValue')
    };
    var hashObject = {};
    if(inserted.length>0){
        for (var i = 0; i < inserted.length; i++) {
        	var no = inserted[i]['loadproposeNo']+"_"+inserted[i]['containerNo']+"_"+inserted[i]['boxNo'];
        	if(hashObject[no]){
        		alert('派车单号_容器号_箱号：【'+no+'】重复！',1);
        		return;
        	}else{
        		hashObject[no] = true;
        	}
        	 //后台ajax校验查询
        	checkData.containerNo = inserted[i]['containerNo'];
        	checkData.boxNo = inserted[i]['boxNo'];
        	checkData.storeNo = inserted[i]['storeNo'];
            if(billomdeliverFlag.checkExistFun(checkUrl,checkData)){
        		alert('派车单号_容器号_箱号：【'+no+'】已存在！',1);
        		return;
    	    }
        }
    }
  var effectRow = {
	inserted:JSON.stringify(inserted),
	deleted:JSON.stringify(deleted),
	updated:JSON.stringify(updated),
	
	"locno" : billomdeliverFlag.user.locno,
	"deliverNo" : $('#deliverNo').val(),
	"ownerNo" : $('#ownerNo').combobox('getValue'),
	"creator" : billomdeliverFlag.user.loginName,
	"editor" : billomdeliverFlag.user.loginName
  };
  wms_city_common.loading("show");
    $.post(BasePath+'/bill_om_deliverflag_dtl/saveDtlInfo', effectRow, function(result) {
    	wms_city_common.loading();
    	if(result.success=="true"){
            alert('装车单明细保存成功！',1);
            tempObj.datagrid('acceptChanges');
            billomdeliverFlag.clearAll(fromObj);
            $('#'+showWinObj).window('close');  
            billomdeliverFlag.searchData();
        }else{
        	alert('操作异常：'+result.msg,1);
        	return;
        }
    }, "JSON").error(function() {
    	wms_city_common.loading();
    	alert('保存失败!',1);
    });
    
//    if(updated.length>0){
//        for (var i = 0; i < updated.length; i++) {
//        	var store_po_item_size = updated[i]['storeNo']+"_"+updated[i]['poNo']+"_"+updated[i]['itemNo']+"_"+updated[i]['sizeNo'];
//        	if(hashObject[store_po_item_size]){
//        		alert('客户_合同_商品编码_尺码：【'+store_po_item_size+'】重复！',1);
//        		return;
//        	}else{
//        		hashObject[store_po_item_size] = true;
//        	}
//        }
//
};

//==================明细保存====================