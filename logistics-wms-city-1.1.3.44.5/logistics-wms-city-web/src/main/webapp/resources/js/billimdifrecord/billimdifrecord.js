var billimdifrecord = {};

/**
 * 初始化开始
 */
$(document).ready(function(){
	billimdifrecord.initCurrentUser();
	//仓库
	billimdifrecord.ajaxRequest(BasePath+'/bmdefloc/get_biz',{},false,billimdifrecord.initLocno);
	//委托业主
	billimdifrecord.ajaxRequest(BasePath+'/entrust_owner/get_biz',{},false,billimdifrecord.initOwnerNo);
	//状态
	billimdifrecord.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_DIF_RECORD',{},false,billimdifrecord.initStatus);
	//状态
	billimdifrecord.init_IMPORT_STATUS();
	//来源类型
	billimdifrecord.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_DIF_RECORD_TYPE',{},false,billimdifrecord.initType);
	//供应商
	billimdifrecord.loadSupplier();
	//品牌库
	billimdifrecord.loadSysNo();
	billimdifrecord.businessTypeData();
	wms_city_common.closeTip("openUI");
});

billimdifrecord.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//将数组封装成一个map
billimdifrecord.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//========================业务类型BEGIN========================
billimdifrecord.businessTypes = {};
billimdifrecord.businessTypesFormatter = function(value, rowData, rowIndex){
	return billimdifrecord.businessTypes[value];
};
//初始化状态
billimdifrecord.businessTypeData = function(formObj){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IMIMPORT_BUSINESS_TYPE',
		success : function(data) {
			$('#businessTypeCondition').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150
			});
			$('#businessTypeForItem').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150
			});
			billimdifrecord.businessTypes = billimdifrecord.businessTypeJsonObj(data);
		}
	});
	
};
billimdifrecord.businessTypeJsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//========================业务类型END========================
//========================加载品牌库========================
billimdifrecord.loadSysNo = function(formObj){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			$('input[id=sysNo]',formObj).combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:200
			});
			if(null!=data){
				$('input[id=sysNo]',formObj).combobox("select",data[0].itemvalue); 
			}
		}
	});
};
//=======================加载品牌库END========================
//========================预到货通知单状态========================
billimdifrecord.columnStatusFormatter = function(value, rowData, rowIndex){
	return billimdifrecord.init_status_Text[value];
};
billimdifrecord.init_IMPORT_STATUS = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_IMPORTOR_STATUS',
		success : function(data) {
			billimdifrecord.init_status_Text=billimdifrecord.converStr2JsonObj(data);
			$('#statusDr').combobox({
				 data:data,
			     valueField:"itemvalue",
			     textField:"valueAndText",
			     panelHeight:"auto",
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
//========================预到货通知单状态END========================
//========================用户 信息========================
billimdifrecord.user = {};
billimdifrecord.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billimdifrecord.userid = data.userid;
		billimdifrecord.loginName = data.loginName;
		billimdifrecord.currentDate = data.currentDate19Str;
		billimdifrecord.locno = data.locno;
		billimdifrecord.user = data;
	});
};
//========================用户 信息END========================
//========================仓库代码========================
billimdifrecord.locno = {};
billimdifrecord.locnoFormatter = function(value, rowData, rowIndex){
	return billimdifrecord.locno[value];
};
//加载仓库代码
billimdifrecord.initLocno = function(data){
	$('#locno').combobox({
	    data:data,
	    valueField:'locno',    
	    textField:'locname',
	    panelHeight:"auto"
	});
	billimdifrecord.locno = billimdifrecord.tansforLocno(data);
};
billimdifrecord.tansforLocno = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].locno +"\":\""+data[i].locname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//========================仓库代码END========================
//========================委托业主========================
billimdifrecord.ownerNo = {};
billimdifrecord.ownerNoFormatter = function(value, rowData, rowIndex){
	return billimdifrecord.ownerNo[value];
};
billimdifrecord.initOwnerNo = function(data){
	$('#ownerNo').combobox({
		valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto"
	});
	$('#ownerNoView').combobox({
		valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto"
	});
	$('#ownerNoDr').combobox({
		valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto"
	});
	billimdifrecord.ownerNo = billimdifrecord.tansforOwner(data);
};
billimdifrecord.tansforOwner = function(data){
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
//========================状态========================
billimdifrecord.status = {};
billimdifrecord.statusFormatter = function(value, rowData, rowIndex){
	return billimdifrecord.status[value];
};
//初始化状态
billimdifrecord.initStatus = function(data){
	$('#statusCondition').combobox({
		data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billimdifrecord.status = temp;
};
//========================状态END========================
//========================来源类型========================
billimdifrecord.difType = {};
billimdifrecord.typeFormatter = function(value, rowData, rowIndex){
	return billimdifrecord.difType[value];
};
billimdifrecord.initType = function(data){
	$('#difType').combobox({
		data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	$('#difTypeView').combobox({
		data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billimdifrecord.difType = temp;
};
//========================来源类型END========================
//========================供应商========================
billimdifrecord.loadSupplier = function(formObj){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/supplier/get_biz',
		success : function(data) {
			
			wms_city_common.init_Supplier_Text = wms_city_common.converSupplierJsonObj(data);
			
			$('input[id=supplierNo]',formObj).combobox({
			    data:data,
			    valueField:'supplierNo',    
			    textField:'valueAndText',
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
			$('input[id=supplierNoView]',formObj).combobox({
			    data:data,
			    valueField:'supplierNo',    
			    textField:'valueAndText',
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
			$('input[id=supplierNoCondition]',formObj).combobox({
			    data:data,
			    valueField:'supplierNo',    
			    textField:'valueAndText',
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
			$('input[id=supplierNoDr]',formObj).combobox({
			    data:data,
			    valueField:'supplierNo',    
			    textField:'valueAndText',
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
//========================供应商END========================
//加载品牌库
billimdifrecord.loadSysNo = function(formObj){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			$('input[id=sysNo]',formObj).combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150
			});
			if(null!=data){
				$('input[id=sysNo]',formObj).combobox("select",data[0].itemvalue); 
			}
		}
	});
};
//================================================
//================================================
//主表清空
billimdifrecord.clearSearch = function(opt){
	$("#" + opt).form('clear');
};
//主表查询
billimdifrecord.searchData = function(){
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
	
	var startAudittm =  $('#startAudittmCondition').datebox('getValue');
	var endAudittm =  $('#endAudittmCondition').datebox('getValue');
	if(!isStartEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
       return;   
   } 
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_im_dif_record/list.json?locno='+billimdifrecord.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	billimdifrecord.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};
//加载Grid数据Utils
billimdifrecord.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
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
//删除
billimdifrecord.deleteUI = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var isDel = true;
	var deliver = "";
	var keys = [];
	for(var i=0;i<checkedRows.length;i++){
		if(checkedRows[i].status != 10){
			deliver += checkedRows[i].defRecordNo + "\n";
			isDel = false;
		}
		keys.push(billimdifrecord.user.locno+"-"+checkedRows[i].ownerNo+"-"+checkedRows[i].defRecordNo);
	}
	if(!isDel) {
		alert("单据:" + deliver +"当前状态不允许删除，请重新选择！");
		return;
	}
     //2.绑定数据
     var url = BasePath+'/bill_im_dif_record/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		 if (r){
			 wms_city_common.loading("show","正在删除......");
			 billimdifrecord.ajaxRequest(url,data,true,function(result){
				 wms_city_common.loading();
				 if(result=='success'){
					 alert('删除成功!');
				 }else{
					 alert('删除失败,请联系管理员!',2);
				 }
				 billimdifrecord.searchData();
			});  
		    }    
		});
};
//审核
billimdifrecord.check = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}else{
		var isDel = true;
		var deliver = "";
		var keys = [];
		for(var i=0;i<checkedRows.length;i++){
			if(checkedRows[i].status != 10){
				deliver += checkedRows[i].defRecordNo + "\n";
				isDel = false;
			}
			keys.push(billimdifrecord.user.locno+"-"+checkedRows[i].ownerNo+"-"+checkedRows[i].defRecordNo);
		}
		if(!isDel) {
			alert("单据:" + deliver + "当前状态不允许审核，请重新选择！");
			return;
		}
		for(var i=0;i<checkedRows.length;i++){
			var ownerNo = checkedRows[i].ownerNo;
			var defRecordNo = checkedRows[i].defRecordNo;
			var untreadDtlUrl = BasePath +'/bill_im_dif_record_dtl/dtlList.json?locno='+billimdifrecord.user.locno+'&ownerNo='+ownerNo+"&defRecordNo="+defRecordNo;
			var existDtl = false;
			billimdifrecord.ajaxRequest(untreadDtlUrl,{},false,function(result){
    			 if(result.total>0){
    				 existDtl = true;
    			 }
    		});
			if(!existDtl){
				alert("库存报损单["+defRecordNo+"]不存在明细,不允许审核!",2);
				return;
			}
		}
		$.messager.confirm("确认","审核后数据将无法修改，您确定并继续操作吗？", function (r){  
	        if (r) {
	        	wms_city_common.loading('show');
	    		var url = BasePath+'/bill_im_dif_record/check';
	    		var data = {ids:keys.join(",")}; 
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
							billimdifrecord.searchData();
						}
					});
	        }
		});
	}
};
//新增窗口
billimdifrecord.addUI = function(){
	$('#dataForm').form('clear');
	billimdifrecord.showHideBtn("add");
	$('#editDtlDg').datagrid('loadData', { total: 0, rows: [] });
	billimdifrecord.openWindow('openUI', '新增');
};
//修改窗口
billimdifrecord.updateUI = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}
	if(checkedRows.length > 1){
		alert('只能选择一条记录!',1);
		return;
	}
	$.each(checkedRows,function(index,item){
		if(item.status != '10'){
			alert("只能修改状态为新建的单据!",1);
			return;
		}
		billimdifrecord.loadDetail(item);
	});
};
//修改明细窗口
billimdifrecord.loadDetail = function(rowData){
	$('#dataForm').form('load',rowData);
	billimdifrecord.showHideBtn("edit");
	$('#editDtlDg').datagrid('loadData', { total: 0, rows: [] });
	billimdifrecord.openWindow('openUI', '修改');
	var tempObj = $('#editDtlDg');
	tempObj.datagrid( 'options' ).url = BasePath+'/bill_im_dif_record_dtl/dtlList.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&defRecordNo="+rowData.defRecordNo;
	tempObj.datagrid('load');
};
//明细
billimdifrecord.dtlView = function(rowData,type){
	$("#defRecordNoView").val(rowData.defRecordNo);
	$("#transNoView").val(rowData.transNo);
	$("#difTypeView").combobox("select",rowData.difType);
	$("#recordDateView").datebox("setValue",rowData.recordDate);
	$("#ownerNoView").combobox("select",rowData.ownerNo);
	$("#poNoView").val(rowData.poNo);
	$("#supplierNoView").combobox("select",rowData.supplierNo);
	$("#importRemarkView").val(rowData.importRemark);
	billimdifrecord.showHideBtn("info");
	$('#detaildataGrid').datagrid('loadData', { total: 0, rows: [] });
	billimdifrecord.openWindow('openDtlUI', '查看');
	var tempObj = $('#detaildataGrid');
	tempObj.datagrid( 'options' ).url = BasePath+'/bill_im_dif_record_dtl/dtlList.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&defRecordNo="+rowData.defRecordNo;
	tempObj.datagrid('load');
};
var isAdd = "";
//隐藏显示按钮
billimdifrecord.showHideBtn = function(type){
	isAdd=type;
	if(type=="add") {
		$("#ownerNo").combobox('disable');
		$("#supplierNo").combobox('disable');
		$("#difType").combobox('enable');
		$("#recordDate").datebox('enable');
		$("#poNo").attr("disabled",true);
		$("#importRemark").attr("disabled",false);
		$("#save_main").show();
		$("#info_save").hide();
		$("#toolsDiv").hide();
		$("#importNoBtn").attr("disabled",false);
	} else if(type=="edit"){
		$("#ownerNo").combobox('disable');
		$("#supplierNo").combobox('disable');
		$("#difType").combobox('disable');
		$("#recordDate").datebox('enable');
		$("#poNo").attr("disabled",true);
		$("#importRemark").attr("disabled",false);
		$("#save_main").hide();
		$("#info_save").show();
		$("#toolsDiv").show();
		$("#importNoBtn").attr("disabled",true);
	} else if(type=="info"){
		$("#ownerNoView").combobox('disable');
		$("#supplierNoView").combobox('disable');
		$("#difTypeView").combobox('disable');
		$("#recordDateView").datebox('disable');
		$("#poNoView").attr("disabled",true);
		$("#importRemarkView").attr("disabled",true);
	} else {
		$("#ownerNo").combobox('disable');
		$("#supplierNo").combobox('disable');
		$("#difType").combobox('disable');
		$("#recordDate").datebox('disable');
		$("#poNo").attr("disabled",true);
		$("#importRemark").attr("disabled",true);
		$("#save_main").hide();
		$("#info_save").hide();
		$("#toolsDiv").hide();
		$("#importNoBtn").attr("disabled",true);
	}
};
//打开窗口
billimdifrecord.openWindow = function(windowId,opt){
	billimdifrecord.closeWindow(windowId);
	$('#'+windowId).window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#'+windowId).window('open');
};

//关闭窗口
billimdifrecord.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};
//================================================
//================================================
billimdifrecord.editMain = function(){
	if(!$("#dataForm").form('validate')){
		alert('数据验证没有通过!',1);
		return;
	}
	var recordDate =  $('#recordDate').datebox('getValue');
	if(recordDate=="") {
		alert('登记日期没有通过!',1);
		return;
	}
	wms_city_common.loading('show');
	var ownerNo = $("#ownerNo").combobox('getValue');
	var url = BasePath+'/bill_im_dif_record/editMainInfo';
	$('#dataForm').form('submit', {
			url: url,
			onSubmit: function(param){
				param.editor = billimdifrecord.user.loginName;
				param.edittm = billimdifrecord.user.currentDate19Str;
				param.locno = billimdifrecord.user.locno;
				param.ownerNo = ownerNo;
			},
			success: function(r){
				wms_city_common.loading();
				r = billimdifrecord.parseJsonStringToJsonObj(r);
				if(r && r.success == 'true'){
					billimdifrecord.showHideBtn("edit");
					 alert('保存成功!');
					 billimdifrecord.searchData();
					 return;
				}else{
					 alert('保存失败!');
				}
		    },
			error:function(){
				alert('保存失败,请联系管理员!',2);
			}
	   });
};
billimdifrecord.saveMain = function(){
	if(!$("#dataForm").form('validate')){
		alert('数据验证没有通过!',1);
		return;
	}
	var recordDate =  $('#recordDate').datebox('getValue');
	if(recordDate=="") {
		alert('登记日期没有通过!',1);
		return;
	}
	var poNo =  $('#poNo').val();
	wms_city_common.loading('show');
	var url = BasePath+'/bill_im_dif_record/saveMainInfo';
	$('#dataForm').form('submit', {
			url: url,
			onSubmit: function(param){
				param.createtm = billimdifrecord.user.currentDate19Str;
				param.creator = billimdifrecord.user.loginName;
				param.editor = billimdifrecord.user.loginName;
				param.edittm = billimdifrecord.user.currentDate19Str;
				param.locno = billimdifrecord.user.locno;
				param.poNo = poNo;
			},
			success: function(r){
				wms_city_common.loading();
				r = billimdifrecord.parseJsonStringToJsonObj(r);
				if(r && r.success == 'true'){
					$("#dataForm input[id=defRecordNo]").val(r.defRecordNo);
					 billimdifrecord.showHideBtn("edit");
					 alert('保存成功!');
					 billimdifrecord.searchData();
					 return;
				}else{
					 alert('保存失败!');
				}
		    },
			error:function(){
				alert('保存失败,请联系管理员!',2);
			}
	   });
};
billimdifrecord.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
//================================================
//================================================
billimdifrecord.openimreceiptWin = function(){
	$('#receiptSelect').window({
		title:"预到货通知单"
	});
	$("#receiptSelectSearchForm").form("clear");
	$("#dataGridJGreceiptSelect").datagrid('loadData', { total: 0, rows: [] });
	$('#receiptSelect').window('open');
	
};
//明细添加
billimdifrecord.showAddItem = function(gid){
	$('#itemSearchForm').form('clear');
	$("#dataGridJGItemSelect").datagrid('clearData');
	$('#showGirdNameForItem').val(gid);
	$('#openUIItem').show().window('open');
};
//明细删除
billimdifrecord.deleteItem  = function(gid){
	var checkItems = $('#' + gid).datagrid('getChecked');
	if(checkItems.length < 1){
		alert("请选择需要删除的数据!");
		return;
	}
	$.each(checkItems,function(index,item){
		var i = $('#' + gid).datagrid('getRowIndex',checkItems[index]);//获取某行的行号
		$('#' + gid).datagrid('deleteRow',i);
	});
};
//明细保存
billimdifrecord.doSave = function(gid){
	//1.校验必填项
  var fromObj=$('#dataForm');
  var validateForm= fromObj.form('validate');
  if(validateForm==false){
       return;
  }
  var tempObj = $('#'+gid);
  var allData = tempObj.datagrid('getRows');
  
  for(var i=0,length=allData.length;i<length;i++){
	  tempObj.datagrid('endEdit', i);
  }
  var inserted = tempObj.datagrid('getChanges', "inserted");
  var deleted = tempObj.datagrid('getChanges', "deleted");
  var updated = tempObj.datagrid('getChanges', "updated");
  var chgSize = tempObj.datagrid('getChanges').length;
  if(chgSize<1){
	  alert('没有数据改动！',1);
	  return;
  }

  //验证明细
  var tempFlag = billimdifrecord.endEdit(gid);
  if(tempFlag!=""){
		alert(tempFlag);
		return;
	}
  //获取所有明细验证是否存在相同
	var hashOK = true;
	var checkSameResult = "";
	var hashObject = {};
	if(inserted.length>0){
		for (var i = 0; i < inserted.length; i++) {
			var no1 = inserted[i]['itemNo']+"_"+inserted[i]['sizeNo']+"_"+inserted[i]['brandNoStr'];
			var no2 = inserted[i]['itemNo']+"_"+inserted[i]['sizeNo']+"_"+inserted[i]['brandNoStr'];
			if(hashObject[no1]){
				checkSameResult += "【" + no2 + "】";
				hashOK = false;
			}
		}
	}
	if(!hashOK){
		alert("商品：" + checkSameResult + "重复！");
		return;
	}
  var defRecordNo = $("input[id=defRecordNo]",fromObj).val();
  var transNo = $("input[id=transNo]",fromObj).val();
  var difType = $("input[id=difType]",fromObj).combobox('getValue');
  var recordDate = $("input[id=recordDate]",fromObj).val();
  var ownerNo = $("input[id=ownerNo]",fromObj).combobox('getValue');
  var supplierNo = $("input[id=supplierNo]",fromObj).combobox('getValue');
  var importRemark = $("input[id=importRemark]",fromObj).val();
  var effectRow = {
  		inserted:JSON.stringify(inserted),
  		deleted:JSON.stringify(deleted),
  		updated:JSON.stringify(updated),
  		
		"locno":billimdifrecord.user.locno,
		"defRecordNo":defRecordNo,
		"transNo":transNo,
		"difType":difType,
		"recordDate":recordDate,
		"ownerNo":ownerNo,
		"supplierNo":supplierNo,
		"importRemark":importRemark,
		"edittm":billimdifrecord.user.currentDate19Str,
		"editor":billimdifrecord.user.loginName,
  };
  //3. 保存
  wms_city_common.loading('show');
  var url = BasePath+'/bill_im_dif_record_dtl/addDtl';
  $.post(url, effectRow, function(result) {
	  wms_city_common.loading();
      if(result.flag=='true'){
    	  alert('保存成功!',1);
    	  tempObj.datagrid('acceptChanges');
          billimdifrecord.closeWindow('openUI');
          billimdifrecord.searchData();
      }else if(result.flag=='warn'){
      	alert(result.msg,1);
      	return;
      }else{
      	alert(result.flag,2);
      	return;
      }
  }, "JSON").error(function() {
  	alert('保存失败!',1);
  });
};
billimdifrecord.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		var itemName = rowArr[i].itemName;
			var sizeNo = rowArr[i].sizeNo;
			var qty = rowArr[i].qty;
    		if(qty!=null){
    			if(qty=="") {
    				$(qty.target).focus();
            		return "商品：" + itemName + " 尺码：" + sizeNo + " 数量不能为空;";
    			}
    			if(qty <= 0){
        			$(qty.target).focus();
            		return "商品：" + itemName + " 尺码：" + sizeNo + " 数量不能为0;";
        		}
    		}
    	}else{
    		return '数据验证没有通过!';
    	}
    }
    return "";
};
//================================================
//================================================
//收货单信息
billimdifrecord.searchReceipt = function(){
	var fromObjStr=convertArray($('#receiptSelectSearchForm').serializeArray());
	var queryMxURL=BasePath+'/billimimport/list.json?locno='+billimdifrecord.user.locno;
	
    //3.加载明细
    $( "#dataGridJGreceiptSelect").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJGreceiptSelect").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGreceiptSelect").datagrid( 'load' );
    
};
//选择清空
billimdifrecord.searchLocClear = function(){
	$("#receiptSelectSearchForm").form("clear");
};

billimdifrecord.selectReceiptOK = function (rowData){
	if(rowData==null){
		rowData= $("#dataGridJGreceiptSelect").datagrid("getSelected");
	}
	$('#transNo').val(rowData.importNo);
	$('#supplierNo').combobox('setValue', rowData.supplierNo);
	$('#ownerNo').combobox('setValue', rowData.ownerNo);
	$('#poNo').val(rowData.transNo);
	$('#receiptSelect').window('close');
};
//================================================
//================================================
//查询商品信息
billimdifrecord.searchItem  = function(){
	var ownerNo = $("#ownerNo").combobox('getValue');
	var supplierNo = $("#supplierNo").combobox('getValue');
	
	var importNo = $("#transNo").val();
    var fromObjStr=convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_im_dif_record_dtl/get_Content?locno='+billimdifrecord.user.locno+"&ownerNo="+ownerNo+"&supplierNo="+supplierNo+"&importNo="+importNo;
	var reqParam = eval("(" +fromObjStr+ ")");
	billimdifrecord.loadGridDataUtil('dataGridJGItemSelect', queryMxURL, reqParam);
};
billimdifrecord.confirmItem = function(){
	var checkedRows = $("#dataGridJGItemSelect").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要添加的记录!',1);
		return;
	}
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"条商品信息吗？", function (r){  
		if (r) {
			wms_city_common.loading("show");
			$.each(checkedRows, function(index, item){
				var  rowData = {
						"itemNo":item.itemNo,
						"itemName":item.itemName,
						"sizeNo":item.sizeNo,
						"barcode":item.barcode,
						"colorNo":item.colorNo,
						"colorName":item.colorName,
						"sysNo":item.sysNo,
						"brandNo":item.brandNo,
						"brandNoStr":item.brandNoStr
				};
				//把选择的商品编码行记录插入到父窗体中
				billimdifrecord.insertRowAtEnd("editDtlDg",rowData);
				$('#openUIItem').window('close').hide();
			});
			wms_city_common.loading();
		}
	});
};
billimdifrecord.insertRowAtEnd = function(gid,rowData){
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