var billomdeliver = {};
billomdeliver.user={};

billomdeliver.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billomdeliver.mainSumFoot = {};
billomdeliver.onLoadSuccessSum = function(data){
	if(data.footer[1] != null){
		billomdeliver.mainSumFoot = data.footer[1];
		}else{
			data.footer[1] = billomdeliver.mainSumFoot;
			$('#dataGridJG').datagrid('reloadFooter');
		}
};
$(document).ready(function(){
	billomdeliver.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},true,function(user){billomdeliver.user=user;});
	billomdeliver.ajaxRequest(BasePath+'/entrust_owner/get_biz',{},false,billomdeliver.initOwnerNo);
	billomdeliver.ajaxRequest(BasePath+'/bmdefloc/get_biz',{},false,billomdeliver.initLocno);
	billomdeliver.ajaxRequest(BasePath+'/os_line_buffer/get_biz',{"locno":billomdeliver.user.locno},false,billomdeliver.initDeliverArea);
	//初始化状态
	billomdeliver.init_IMPORT_STATUS();
	billomdeliver.init_sendName();
	billomdeliver.initDock();
	billomdeliver.loadCircleNo();
	
	wms_city_common.closeTip("openUI");
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('input[id=brandNo]', $('#searchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs);
	
	var objsNew = [];
	objsNew.push({
		"sysNoObj" : $('#sysNoNew'),
		"brandNoObj" : $('input[id=brandNoNew]', $('#itemSearchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objsNew);
	
	
	//总计
	$('#deliverDetail').datagrid({
		'onLoadSuccess' : function(data) {
			if (data.footer[1].isselectsum) {
				billomdeliver.qty = data.footer[1].qty;
			} else {
				var rows = $('#deliverDetail').datagrid('getFooterRows');
				rows[1]['qty'] = billomdeliver.qty;
				$('#deliverDetail').datagrid('reloadFooter');
			}
		}
	});
	$('#print').click(function(){
		$('#print_menu').menu('show', {    
			  left: 470,    
			  top: 32    
			});  
		
	});
	$('#startCreatetm').datebox('setValue',getDateStr(-2));
});
//==================状态====================
billomdeliver.init_status_Text = {};
billomdeliver.columnStatusFormatter = function(value, rowData, rowIndex){
	return billomdeliver.init_status_Text[value];
};
billomdeliver.init_IMPORT_STATUS = function(){
	wms_city_common.comboboxLoadFilter(
			["searchStatus","status","statusCondition"],
			null,
			null,
			null,
			true,
			[true,false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_LOADCAR_STATUS',
			{},
			billomdeliver.init_status_Text,
			null);

};
//将数组封装成一个map
billomdeliver.converStr2JsonObj = function(data){
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
//========================仓库代码======================================
billomdeliver.locno = {};
billomdeliver.locnoFormatter = function(value, rowData, rowIndex){
	return billomdeliver.locno[value];
};
//加载仓库代码
billomdeliver.initLocno = function(data){
	$('#locno').combobox({
	    data:data,
	    valueField:'locno',    
	    textField:'locname',
	    panelHeight:200,
	});
	billomdeliver.locno = billomdeliver.tansforLocno(data);
};
billomdeliver.tansforLocno = function(data){
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
//==================委托业主====================
billomdeliver.ownerNo = {};
billomdeliver.ownerNoFormatter = function(value, rowData, rowIndex){
	return billomdeliver.ownerNo[value];
};
billomdeliver.initOwnerNo = function(data){
	wms_city_common.comboboxLoadFilter(
			["ownerDtlNo","ownerNo"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[false,false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billomdeliver.ownerNo,
			null);
};

//==================委托业主====================
//==================发货人====================
billomdeliver.sendNo = {};
billomdeliver.columnSendNoFormatter = function(value, rowData, rowIndex){
	return billomdeliver.sendNo[value];
};
billomdeliver.init_sendName = function(){
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
		billomdeliver.sendNo = billomdeliver.tansforSendNo(data);
	}
});
	wms_city_common.comboboxLoadFilter(
			["searchSendName"],
			'workerNo',
			'workerName',
			'workerName',
			false,
			[true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
};

billomdeliver.tansforSendNo = function(data){
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
billomdeliver.initDock = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		'url':BasePath+'/bm_defdock/get_biz?dockType=4&locno='+billomdeliver.user.locno,
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
billomdeliver.loadCircleNo = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/bmcircle/get_biz?locno='+billomdeliver.user.locno,
		success : function(data) {
			$('#circleNo').combobox({
				data:data,
			    valueField:'circleNo',    
			    textField:'valueAndText',
			    panelHeight:200,
			    onSelect:function(){
			    	billomdeliver.initStoreNo();
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
billomdeliver.initStoreNo = function(){
	var circleNo = $('#circleNo').combobox("getValue");
	if(circleNo != "") {
//	$.ajax({
//		async : false,
//		cache : true,
//		type : 'POST',
//		dataType : "json",
//		data:{
//			"locno":billomdeliver.user.locno,
//			"circle":circleNo
//		},
//		success : function(data) {
//			$('#storeNo').combobox({
//				valueField:"storeNo",
//			    textField:"storeName",
//			    data:data,
//			    panelHeight:200
//			}).combobox("select",data[0]==""||data[0]==null?"":data[0].areaNo);
//		}
//	});
		$('#storeNo').combogrid({
			delay: 1500,
			panelWidth:450,   
	        idField:'storeNo',  
	        textField:'storeName',   
	        pagination : true,
	        rownumbers:true,
	        mode: 'remote',
	        url:BasePath+'/store/list.json?storeType=11&circle='+circleNo,
//	        url:BasePath+'/bill_om_deliver_dtl/selectBoxStore?locno='+billomdeliver.user.locno+"&circle="+circleNo,
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
billomdeliver.searchData = function(){
	 //1.校验必填项
   var fromObj=$('#searchForm');
   var validateForm= fromObj.form('validate');
   if(validateForm==false){
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
	
	var startExpDate =  $('#startExpDate').datebox('getValue');
	var endExpDate =  $('#endExpDate').datebox('getValue');
	if(!isStartEndDate(startExpDate,endExpDate)){    
		alert("发货日期开始日期不能大于结束日期");   
       return;   
   } 
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_deliver/deliverlist.json?flag=20&locno='+billomdeliver.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	$.extend(reqParam,{locno:billomdeliver.user.locno});
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
billomdeliver.clearSearchCondition = function(){
	$("#searchForm").form('clear');
	$('#brandNo').combobox("loadData",[]);
};
//==================主窗口查询、清空====================
//==================主表删除、审核、新增、修改、明细查询====================
//删除主表
billomdeliver.deleteRows = function(){
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
		keys.push(billomdeliver.user.locno+"-"+checkedRows[i].deliverNo+"-"+checkedRows[i].ownerNo);
	}
	if(!isDel) {
		alert("单据:" + deliver +"已经装车完成，请重新选择");
		return;
	}
     //2.绑定数据
     var url = BasePath+'/bill_om_deliver/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		 if (r){
			 wms_city_common.loading("show");
			 billomdeliver.ajaxRequest(url,data,true,function(result){
				 wms_city_common.loading();
				 if(result.success=="true"){
					 //4.删除成功,清空表单
					 alert('删除成功!');
				 }else{
					 alert('操作异常：'+result.msg,1);
				 }
				 billomdeliver.searchData();
			});  
		    }    
		});
};
//审核
billomdeliver.check = function(){
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
			checkIds.push(billomdeliver.user.locno + 
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
			var untreadDtlUrl = BasePath+'/bill_om_deliver_dtl/viewDtl.json?locno='+billomdeliver.user.locno
				+ '&deliverNo='+ deliverNo + '&ownerNo='+ownerNo;
			var existDtl = false;
			billomdeliver.ajaxRequest(untreadDtlUrl,{},false,function(result){
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
	    		var url = BasePath+'/bill_om_deliver/check';
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
							billomdeliver.searchData();
						}
					});
	        }
		});
	}
};
//打开 新增/修改窗口
billomdeliver.openUI = function(opt){
	$('#openUI').window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUI').window('open');
};
//关闭 新增/修改窗口
billomdeliver.closeUI = function(opt){
	$('#openUI').window('close');
	$("#opt").val("");
};
//打开 明细窗口
billomdeliver.openDtlUI = function(opt){
	$('#openDtlUI').window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openDtlUI').window('open');
};
//关闭 明细窗口
billomdeliver.closeDtlUI = function(opt){
	$('#openDtlUI').window('close');
	$('#dataGridJGItem').datagrid('loadData', { total: 0, rows: [] });
};
//明细
billomdeliver.dtlView = function(rowData,type){
	if(type=="view"){
		$('#deliverDetail').datagrid(
				{'url':BasePath+'/bill_om_deliver_dtl/viewDtl.json?locno='+billomdeliver.user.locno + '&deliverNo='+rowData.deliverNo,'pageNumber':1 });
		
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
		billomdeliver.openDtlUI("查看");
	}
};
//修改
billomdeliver.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		billomdeliver.loadDetail(checkedRows[0],"edit");
	}
};
billomdeliver.loadDetail = function(rowData,type){
	$("#opt").val("");
	if(type=="edit"){
		$('#dataForm').form('load',rowData);
		$("#save_main_info").hide();
		$("#detailDiv,#btn-modify").show();
		$('#itemDetail').datagrid(
				{'url':BasePath+'/bill_om_deliver_dtl/findDeliverDtlBoxByPage?deliverNo='+rowData.deliverNo+'&locno='+rowData.locno+'&ownerNo='+rowData.ownerNo,'pageNumber':1 });
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
		
		billomdeliver.openUI("修改");
	}
};
//新增窗口
billomdeliver.addUI = function(opt){
	$("#containerType+ span input.combo-text").attr("readOnly",false);
	$("#containerType+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#containerType+ span").css("background-color","white");
	$("#containerType+ span input.combo-text").css("background-color","white");
	
	$("#useType+ span input.combo-text").attr("readOnly",false);
	$("#useType+ span input.combo-text+ span span").addClass("combo-arrow");
	$("#useType+ span").css("background-color","white");
	$("#useType+ span input.combo-text").css("background-color","white");
	
	billomdeliver.clearAll();
	$('#expDate').datebox('setValue',getDate4YMD(1));
};
/**
 * 获取从今日起第num天的年月日
 * @param num
 */
function getDate4YMD(num){
	var now = new Date();
	var dest = new Date(now.getTime()+(num*24000*3600));
	var y = dest.getFullYear();
	var m = dest.getMonth()+1;
	var d = dest.getDate();
	return y+"-"+m+"-"+d;
}
billomdeliver.clearAll = function(){
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
     billomdeliver.openUI("新增");
};
//==================主表删除、审核、新增、修改、明细查询====================
//==================新增主表====================
billomdeliver.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
//新增主表
billomdeliver.saveMainInfo = function(){
	var url = BasePath+ '/bill_om_deliver/saveMainInfo';
	var carPlate = encodeURIComponent($("#carPlate").val());
	var remarks = encodeURIComponent($("#remarks").val());
	wms_city_common.loading("show");
	if($('#dataForm').form('validate')){
		  $('#dataForm').form('submit', {
				url: url,
				onSubmit: function(param){
					param.locno = billomdeliver.user.locno;
					param.carPlate1 = carPlate;
					param.remarks1 = remarks;
				},
				success: function(r){
					wms_city_common.loading();
					r = billomdeliver.parseJsonStringToJsonObj(r);
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
billomdeliver.addrow = function(gid){
	$("#dataGridJGItem").datagrid('clearData');
	$('#itemSearchForm').form("clear");
	
	$('#showGirdName').val(gid);
	var fromObj=$('#dataForm');
	$('#sysNoForItem').val($('input[id=sysNoHide]',fromObj).val());
	
	billomdeliver.openUIItem("箱号选择");
};
//打开 明细窗口
billomdeliver.openUIItem = function(opt){
	$('#openUIItem').window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#openUIItem').window('open');
};

//==================箱号选择窗口====================
//==================箱号选择查询、清空、关闭====================
billomdeliver.searchItem = function(){
	var formArray = $('#itemSearchForm').serializeArray();
	var locnoParam = {"locno":billomdeliver.user.locno};
	var fromObjStr=convertArray(formArray);
	var queryMxURL=BasePath+'/bill_om_deliver_dtl/boxSelectQuery';
	var paramsObj = eval("(" +fromObjStr+ ")");
	$.extend(paramsObj,locnoParam); 
	console.info(paramsObj);
    //3.加载明细
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams= paramsObj;
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
};
billomdeliver.clearSearch = function(){
	$("#itemSearchForm").form('clear');
	$('#brandNoNew').combobox("loadData",[]);
};
billomdeliver.closeOpenUIItem = function(){
	$("#openUIItem").window('close');
};
//==================箱号选择查询、清空、关闭====================
//==================箱号选择确定====================
billomdeliver.confirmItem = function(){
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
        	    		"recheckNo":item.recheckNo,
        	    		"containerNo":item.containerNo,
        	    		"boxNo":item.boxNo,
        	    		"storeNo":item.storeNo,
        	    		"storeName":item.storeName,
        	    		"expNo":item.expNo,
        	    		"qty":item.qty
        	    };
        	  
        		//把选择的商品编码行记录插入到父窗体中
        	    billomdeliver.insertRowAtEnd($('#showGirdName').val(),rowData);
        	    billomdeliver.closeOpenUIItem();
            }); 
            wms_city_common.loading();
        }
	});
};
billomdeliver.insertRowAtEnd = function(gid,rowData){
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
billomdeliver.removeBySelected = function(gid){
	var tempObj = $('#'+gid);
	var rowObj = tempObj.datagrid('getSelected');
    if (rowObj) {
        var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
        tempObj.datagrid('deleteRow', rowIndex);
    }
};
//==================明细删除====================
//==================明细保存====================
billomdeliver.endEdit = function(gid){
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
billomdeliver.checkExistFun = function(url,checkColumnJsonData){
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
//保存明细
billomdeliver.saveDtlInfo = function(id){
	//必须有确认当前新增的行已经结束编辑（endEdit），否则获取新增行datagrid('getChanges', "inserted")是不能获取到
	//正在编辑状态的那一行的
	var tempFlag = billomdeliver.endEdit(id);
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
    
    
    var checkUrl=BasePath+'/bill_om_deliver_dtl/get_count.json';
    var checkData={
    		"locno" : billomdeliver.locno,
    		"deliverNo" : $('#deliverNo').val(),
    		"ownerNo" : $('#ownerNo').combobox('getValue')
    };
    var hashObject = {};
    if(inserted.length>0){
        for (var i = 0; i < inserted.length; i++) {
        	var no = inserted[i]['recheckNo']+"_"+inserted[i]['containerNo']+"_"+inserted[i]['boxNo'];
        	if(hashObject[no]){
        		alert('复核单号_容器号_箱号：【'+no+'】重复！',1);
        		return;
        	}else{
        		hashObject[no] = true;
        	}
        	 //后台ajax校验查询
        	checkData.containerNo = inserted[i]['containerNo'];
        	checkData.boxNo = inserted[i]['boxNo'];
        	checkData.storeNo = inserted[i]['storeNo'];
            if(billomdeliver.checkExistFun(checkUrl,checkData)){
        		alert('复核单号_容器号_箱号：【'+no+'】已存在！',1);
        		return;
    	    }
        }
    }
  var effectRow = {
	inserted:JSON.stringify(inserted),
	deleted:JSON.stringify(deleted),
	updated:JSON.stringify(updated),
	
	"locno" : billomdeliver.user.locno,
	"deliverNo" : $('#deliverNo').val(),
	"ownerNo" : $('#ownerNo').combobox('getValue'),
	"creator" : billomdeliver.user.loginName,
	"editor" : billomdeliver.user.loginName
  };
  wms_city_common.loading("show");
  ajaxRequest(BasePath+'/bill_om_deliver_dtl/saveDtlInfo',effectRow,function(result){
	  wms_city_common.loading();
	  if(result.success=="true"){
	        alert('装车单明细保存成功！',1);
	        tempObj.datagrid('acceptChanges');
	        billomdeliver.clearAll(fromObj);
	        $('#'+showWinObj).window('close');  
	        billomdeliver.searchData();
	   }else{
	   		alert('操作异常：'+result.msg,1);
	      	return;
	   }
   }); 
  
//  $.post(BasePath+'/bill_om_deliver_dtl/saveDtlInfo', effectRow, function(result) {
//    	wms_city_common.loading();
//    	if(result.success=="true"){
//            alert('装车单明细保存成功！',1);
//            tempObj.datagrid('acceptChanges');
//            billomdeliver.clearAll(fromObj);
//            $('#'+showWinObj).window('close');  
//            billomdeliver.searchData();
//        }else{
//        	alert('操作异常：'+result.msg,1);
//        	return;
//        }
//   }, "JSON").error(function() {
//    	wms_city_common.loading();
//    	alert('保存失败!',1);
//   });
    
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

billomdeliver.printDetail4SizeHorizontal = function(size){
	
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	wms_city_common.loading("show","正在加载......");
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].locno+"|"+rows[i].deliverNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_om_deliver_dtl/printDetail4SizeHorizontal',
        data: {
        	keys:keys.join(",")
        },
        success: function(data){
        	wms_city_common.loading();
        	if(data.result=="success"){
        		print4SizeHorizontal(data,size);
        	}else{
        		alert(data.msg,2);
        	}
        	
		}
    });
};
function print4SizeHorizontal(data,size){
	//测试构造多条数据S
	/*var ti = 100;
	for(var i=0;i<ti;i++){
		data.pages[0].list[data.pages[0].list.length] = data.pages[0].list[0];
	}*/
	//测试构造多条数据E
	var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	if(LODOP==null){
		return;
	}
	var direction  = 1;//打印方向
	var totalHeight = 1050;
	var headHeight = 85;
	var spaceHeight = 5;//头部与表格之间的间隙
	var tableHeight = 0;
	if(size == 'A5'){
		direction = 2;//横向
		totalHeight = 490;//A5页面高度
	}else if(size == 'A4'){
		direction = 1;//纵向
		totalHeight = 1050;//A4页面高度
	}
	tableHeight = totalHeight - headHeight - spaceHeight;//表格的高度
	
	LODOP.SET_PRINT_PAGESIZE(direction, 0, 0, size);
	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
	for(var idx=0;idx<data.pages.length;idx++){
		LODOP.NewPageA();
		var headHtml = createHeadHtml(data.pages[idx]);
		var bodyHtml = createBodyHtml(data.pages[idx]);
		//设置表格内容
		LODOP.ADD_PRINT_TABLE(headHeight+spaceHeight,0,"100%",tableHeight,strStyle+bodyHtml);
		//LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
		//设置表格头部
		LODOP.ADD_PRINT_HTM(0,0,"100%",headHeight,headHtml);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		//设置条码
		LODOP.ADD_PRINT_BARCODE(5,10,250,40,"128A",data.pages[idx].deliverNo);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	}
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
}
function createHeadHtml(page){
	var html = "<table style='width:100%;font-size:13px;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
	html += "<tr><td style='text-align:center;' colspan='4'>&nbsp;</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+page.deliverNo+"</td><td>发货方："+billomdeliver.user.locname+"" +
			"</td><td>收货方："+page.list[0].storeName+"("+page.list[0].storeNo+")</td><td>总合计："+page.total+"</td></tr>";
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
	html = "<table border='0' cellpadding='1' cellspacing='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'><thead>";
	html += "<tr style='background-color: #fff;'>";
	html += "<td rowspan='"+rowspan+"'>商品编码</td>";
	for(var i=0;i<rowspan;i++){
		var sizeArray = [];
		if(i > 0){
			html += "<tr style='background-color: #fff;'>";
			for(var j=0;j<sizeColNum;j++){
				if(j >= page.sizeList[i].length){
					html += "<td>&nbsp;</td>";
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
		html += "<td>"+row.sizeKind+"</td>";
		for(var k=0;k<sizeColNum-1;k++){
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
//汇总预览打印
billomdeliver.printOmDeliverList = function(){
	var checkedRows = $("#dataGridJG").datagrid("getRows");// 获取所有行
	if(checkedRows.length < 1){
		alert('没有数据，不能打印!',1);
		return;
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_om_deliver/printDeliverList.json?flag=20&locno='+billomdeliver.user.locno,
        data: $('#searchForm').serialize(),
        success: function(data){
        	wms_city_common.loading();
        	if(data.result=="success"){
        		billomdeliver.print(data);
        	}else{
        		alert(data.msg,2);
        	}
		}
    });
};
billomdeliver.print = function(data){
	//测试构造多条数据S
	/*var ti = 100;
	for(var i=0;i<ti;i++){
		data.pages[0].list[data.pages[0].list.length] = data.pages[0].list[0];
	}*/
	var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	if(LODOP==null){
		return;
	}
	LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
	LODOP.NewPageA();
	var headHtml = billomdeliver.headHtml();
	var bodyHtml = billomdeliver.bodyHtml(data.rows);
	var footerHtml = billomdeliver.footerHtml();
	//设置表格头部
	LODOP.ADD_PRINT_HTM(10,10,"100%",120,headHtml);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	
	//设置表格内容
	LODOP.ADD_PRINT_TABLE(85,0,"100%",380,strStyle+bodyHtml);
	LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
	
	//设置表格底部
	LODOP.ADD_PRINT_HTM(500,0,"100%",50,footerHtml);
	LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
	LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);	
	
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
};
billomdeliver.headHtml = function(){
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单汇总表</td></tr>";
	return html;
};
billomdeliver.bodyHtml = function(page){
	var html = "";
	html = "<table border='0' cellpadding='1' cellspacing='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'><thead>";
	html += "<tr style='background-color: #fff;'>";
	html += "<td>序号</td><td>状态</td><td>单据编号</td><td>货主</td><td>发货人</td><td>出库数量</td><td>发货日期</td><td>车牌号</td>";
	html += "<td>创建人</td><td>创建日期</td><td>审核人</td><td>审核日期</td></tr>";
	html += "</thead>";
	var totalQty = 0;
	var sendName = null;
	for(var x=0;x<page.length;x++){
		row = page[x];
		html += "<tr style='background-color: #fff;text-align:center'>";
		html += "<td>"+(x+1)+"</td>";
		html += "<td>"+billomdeliver.columnStatusFormatter(row.status)+"</td>";
		html += "<td>"+row.deliverNo+"</td>";
		html += "<td>"+billomdeliver.ownerNoFormatter(row.ownerNo)+"</td>";
		var sendName = billomdeliver.columnSendNoFormatter(row.sendName);
		if(sendName == null){
			html += "<td>&nbsp;</td>";
		}else{
			html += "<td>"+sendName+"</td>";
		}
		
	    html += "<td>"+row.sumQty+"</td>";
	    html += "<td>"+row.expDate+"</td>";
		html += "<td>"+row.carPlate+"</td>";
		if(row.creatorname == null){
			html += "<td>&nbsp;</td>";
		}else{
			html += "<td>"+row.creatorname+"</td>";
		}
		html += "<td>"+row.createtm+"</td>";
		if(row.auditorname == null){
			html += "<td>&nbsp;</td>";
		}else{
			html += "<td>"+row.auditorname+"</td>";
		}
		if(row.audittm == null){
			html += "<td>&nbsp;</td>";
		}else{
			html += "<td>"+row.audittm+"</td>";
		}
		totalQty += row.sumQty;
		html += "</tr>";
	}
	html += "<tr><td colspan='5' style='text-align:right;border:none'>合计&nbsp;:&nbsp;&nbsp;</td><td style='border:none' colspan='8'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+totalQty+"</td></tr>";
	html = html + "<tfoot><tr><td style='text-align:13px;text-align:center;' colspan=13><font tdata='pageNO' format = '#' align='center'>第#页</font>&nbsp;/&nbsp;<font format='#' tdata='pageCount'>共#页</font></td></tr></tfoot>";
	html += "</table>";
	return html;
};
billomdeliver.footerHtml = function(){
	var now = new Date();
	var nowStr = now.format("yyyy-MM-dd hh:mm:ss"); 
	var html = "<table style='width:100%;'>";
	html =html+ "<tr><td style='text-align:right;padding-right:50px;'>打印人："+billomdeliver.user.username+"&nbsp;&nbsp;&nbsp;&nbsp;打印时间："+nowStr+"</td></tr>";
	html =html+ "</table>";
	return html;
};

