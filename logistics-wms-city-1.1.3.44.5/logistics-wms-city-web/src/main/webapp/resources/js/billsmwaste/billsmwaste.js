var billsmwaste = {};

billsmwaste.ajaxRequest = function(url,reqParam,async,callback){
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
	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
	billsmwaste.initCurrentUser();
	//仓库
	billsmwaste.ajaxRequest(BasePath+'/bmdefloc/get_biz',{},false,billsmwaste.initLocno);
	//委托业主
	billsmwaste.ajaxRequest(BasePath+'/entrust_owner/get_biz',{},false,billsmwaste.initOwnerNo);
	//状态
	billsmwaste.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_WASTE_STATUS',{},false,billsmwaste.initStatus);
	//类型
	billsmwaste.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_WASTE_TYPE',{},false,billsmwaste.initTypes);
	//查询类型
	//billsmwaste.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_SM_WASTE_ITEM_SELECT_TYPE',{},false,billsmwaste.initSelectType);
	
	wms_city_common.initItemTypeAndQuality("itemTypeForItem","qualityForItem");
	//品牌
	var objs = [];
	objs.push({"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNoCondition]',$('#searchForm'))});
	wms_city_common.loadSysNo4Cascade(objs);
	
	//新增明细选择商品，品牌
	var objsItem = [];
	objsItem.push({"sysNoObj":$('#sysNo'),"brandNoObj":$('input[id=brandNoItem]',$('#itemSearchForm'))});
	wms_city_common.loadSysNo4Cascade(objsItem);
	//wms_city_common.loadSysNo('#sysNo');
	
	wms_city_common.closeTip("openUI");
	
	$('#detaildataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
			   				billsmwaste.wasteQty = data.footer[1].wasteQty;
			   			}else{
			   				var rows = $('#detaildataGrid').datagrid('getFooterRows');
				   			rows[1]['wasteQty'] = billsmwaste.wasteQty;
				   			$('#detaildataGrid').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
});
billsmwaste.onLoadSuccess = function(data){
	if(data.rows.length > 0){
		if (data.footer[1] != null) {
			billsmwaste.wasteQty = data.footer[1];
		} else {
			var rows = $('#dataGridJG').datagrid('getFooterRows');
   			rows[1] = billsmwaste.wasteQty;
   			$('#dataGridJG').datagrid('reloadFooter');
		}
	}
}
//加载Grid数据Utils
billsmwaste.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};
//========================用户 信息========================
billsmwaste.user = {};
billsmwaste.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billsmwaste.userid = data.userid;
		billsmwaste.loginName = data.loginName;
		billsmwaste.currentDate = data.currentDate19Str;
		billsmwaste.locno = data.locno;
		billsmwaste.user = data;
	});
};
//========================用户 信息END========================
//========================仓库代码========================
billsmwaste.locno = {};
billsmwaste.locnoFormatter = function(value, rowData, rowIndex){
	return billsmwaste.locno[value];
};
//加载仓库代码
billsmwaste.initLocno = function(data){
	$('#locno').combobox({
	    data:data,
	    valueField:'locno',    
	    textField:'locname',
	    panelHeight:"auto"
	});
	billsmwaste.locno = billsmwaste.tansforLocno(data);
};
billsmwaste.tansforLocno = function(data){
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
billsmwaste.ownerNo = {};
billsmwaste.ownerNoFormatter = function(value, rowData, rowIndex){
	return billsmwaste.ownerNo[value];
};
billsmwaste.initOwnerNo = function(data){
	$('#ownerNo').combobox({
		valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto"
	});
	$('#ownerNoDtl').combobox({
		valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto"
	});
	billsmwaste.ownerNo = billsmwaste.tansforOwner(data);
};
billsmwaste.tansforOwner = function(data){
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
billsmwaste.status = {};
billsmwaste.statusFormatter = function(value, rowData, rowIndex){
	return billsmwaste.status[value];
};
//初始化状态
billsmwaste.initStatus = function(data){
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
	billsmwaste.status = temp;
};

//初始化状态
billsmwaste.initSelectType = function(data){
	$('#selectTypeItem').combobox({
		data:data,
	    valueField:"itemvalue",
	    textField:"itemnamedetail",
	    panelHeight:"auto"
	});
};

//========================状态END========================
billsmwaste.types = {};
billsmwaste.typesFormatter = function(value, rowData, rowIndex){
	return billsmwaste.types[value];
};
//初始化状态
billsmwaste.initTypes = function(data){
	var tempData = [];
	 for(var idx=0;idx<data.length;idx++){
		 if(data[idx].itemvalue != '04'){
			 tempData[tempData.length] = data[idx];
		 }
	 }
	 data = tempData;
	$('#wasteType').combobox({
		data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	$('#wasteTypeCondition').combobox({
		data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	$('#wasteTypeDtl').combobox({
		data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billsmwaste.types = temp;
};
//========================品质========================
billsmwaste.quality = {};
billsmwaste.qualityFormatter = function(value, rowData, rowIndex){
	return billsmwaste.quality[value];
};
billsmwaste.initQuality = function(data){
	$('#qualityForItem').combobox({
		valueField:"itemvalue",
	     textField:"itemname",
	     data:data,
	     panelHeight:"auto"
	});
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billsmwaste.quality = temp;
};
/**商品属性**/
billsmwaste.itemType={};
billsmwaste.itemTypeFormatter = function(value, rowData, rowIndex){
	return billsmwaste.itemType[value];
};
billsmwaste.getItemTypeData = function(data){
	$("#itemTypeForItem").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
//	$("#itemType").combobox({
//	     valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     data:data,
//	     panelHeight:150
//	});
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billsmwaste.itemType=temp;
};
//========================品质END========================
//========================主表查询、清空========================
billsmwaste.clearSearch = function(opt){
	$("#" + opt).form('clear');
	$('#brandNoCondition').combobox("loadData",[]);
};
//主表
billsmwaste.searchData = function(){
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
	var queryMxURL=BasePath+'/bill_sm_waste/d_list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam.locno = billsmwaste.user.locno;
	reqParam.isDirect = 'N';
	billsmwaste.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
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
//========================主表查询、清空END========================
//========================新增、修改页面========================
//打开窗口
billsmwaste.openWindow = function(windowId,opt){
	billsmwaste.closeWindow(windowId);
	$('#'+windowId).window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#'+windowId).window('open');
};

//关闭窗口
billsmwaste.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};
//新增窗口
billsmwaste.addUI = function(){
	$('#dataForm').form('clear');
	billsmwaste.showHideBtn("add");
	$('#wasteDtlDg').datagrid('loadData', { total: 0, rows: [] });
	$("#wasteDtl").hide();
	billsmwaste.openWindow('openUI', '新增');
};
//明细
billsmwaste.dtlView = function(rowData,type){
	if(type=="view"){
		$("#wasteNoDtl").val(rowData.wasteNo);
		$("#wasteTypeDtl").combobox("select",rowData.wasteType);
		$("#ownerNoDtl").combobox("select",rowData.ownerNo);
		$("#wasteDateDtl").datebox("setValue",rowData.wasteDate);
		$("#remarkDtl").val(rowData.remark);
		billsmwaste.showHideBtn("info");
		$('#detaildataGrid').datagrid('loadData', { total: 0, rows: [] });
		billsmwaste.openWindow('openDtlUI', '查看');
		var tempObj = $('#detaildataGrid');
		tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_waste_dtl/dtl_List.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&wasteNo="+rowData.wasteNo;
	  tempObj.datagrid('load');
	}
};
//修改明细窗口
billsmwaste.loadDetail = function(rowData){
	$('#dataForm').form('load',rowData);
	billsmwaste.showHideBtn("edit");
	$('#wasteDtlDg').datagrid('loadData', { total: 0, rows: [] });
	$("#wasteDtl").show();
	billsmwaste.openWindow('openUI', '修改');
	var tempObj = $('#wasteDtlDg');
	tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_waste_dtl/dtlList.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&wasteNo="+rowData.wasteNo;
  tempObj.datagrid('load');
};

//修改退仓收货单
billsmwaste.updateUI = function(){
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
		billsmwaste.loadDetail(item);
	});
};
var isAdd = "";
//隐藏显示按钮
billsmwaste.showHideBtn = function(type){
	isAdd=type;
	if(type=="add") {
		$("#wasteType").combobox('enable');
		$("#ownerNo").combobox('enable');
		$("#wasteDate").datebox('enable');
		$("#remark").attr("disabled",false);
		$("#save_main").show();
		$("#info_save").hide();
	} else if(type=="edit"){
		$("#wasteType").combobox('disable');
		$("#ownerNo").combobox('disable');
		$("#wasteDate").datebox('enable');
		$("#remark").attr("disabled",false);
		$("#save_main").hide();
		$("#info_save").show();
	} else {
		$("#wasteTypeDtl").combobox('disable');
		$("#ownerNoDtl").combobox('disable');
		$("#wasteDateDtl").datebox('disable');
		$("#remarkDtl").attr('disabled',true);
		$("#save_main").hide();
		$("#info_save").hide();
	}
};
//========================新增、修改页面END========================
//========================保存主表======================================

billsmwaste.editMain = function(){
	if(!$("#dataForm").form('validate')){
		alert('请输入完整信息！',1);
		return;
	}
	var wasteDate = $("#wasteDate").datebox('getValue');
	if(wasteDate != null && wasteDate!="") {
	} else {
		alert('日期不能为空，请录入出库日期！', 1);
		return;
	}
	if(!isStartEndDate(wms_city_common.getCurDate(),wasteDate)){ 
		alert("出库日期不能小于当前日期！");   
        return;   
    }
	var remark = $("#remark").val();
	if(trim(remark) == ""){
		alert("请输入备注信息！",1);
		return;
	}
	wms_city_common.loading("show","正在修改......");
	var ownerNo = $("#ownerNo").combobox('getValue');
	var url = BasePath+'/bill_sm_waste/editMainInfo';
	$('#dataForm').form('submit', {
			url: url,
			onSubmit: function(param){
				param.editor = billsmwaste.user.loginName;
				param.editorname = billsmwaste.user.username;
				param.locno = billsmwaste.user.locno;
				param.ownerNo = ownerNo;
			},
			success: function(r){
				wms_city_common.loading();
				r = billsmwaste.parseJsonStringToJsonObj(r);
				if(r.flag == 'success'){
					 billsmwaste.showHideBtn("edit");
					 alert('保存成功!');
					 billsmwaste.searchData();
					 return;
				}else{
				   alert(r.msg);
				}
		    },
			error:function(){
				wms_city_common.loading();
				alert('保存失败,请联系管理员!',2);
			}
	   });
};
billsmwaste.saveMain = function(){
	if(!$("#dataForm").form('validate')){
		alert('请输入完整信息！',1);
		return;
	}
	var wasteDate = $("#wasteDate").datebox('getValue');
	if(wasteDate != null && wasteDate!="") {
	} else {
		alert('日期不能为空，请录入出库日期！', 1);
		return;
	}
	if(!isStartEndDate(wms_city_common.getCurDate(),wasteDate)){ 
		alert("出库日期不能小于当前日期！");   
        return;   
    }
	var remark = $("#remark").val();
	if(trim(remark) == ""){
		alert("请输入备注信息！",1);
		return;
	}
	wms_city_common.loading("show","正在保存......");
	var url = BasePath+'/bill_sm_waste/saveMainInfo';
	$('#dataForm').form('submit', {
			url: url,
			onSubmit: function(param){
				param.creator = billsmwaste.user.loginName;
				param.creatorname = billsmwaste.user.username;
				param.editor = billsmwaste.user.loginName;
				param.editorname = billsmwaste.user.username;
				param.locno = billsmwaste.user.locno;
			},
			success: function(r){
				wms_city_common.loading();
				r = billsmwaste.parseJsonStringToJsonObj(r);
				if(r && r.success == 'true'){
					$("#dataForm input[id=wasteNo]").val(r.wasteNo);
					 billsmwaste.showHideBtn("edit");
					 alert('保存成功!');
					 billsmwaste.searchData();
					 //$('#wasteDtlDg').datagrid('loadData', { total: 0, rows: [] });
					 var tempObj = $('#wasteDtlDg');
					tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_waste_dtl/dtl_List.json?locno='+billsmwaste.user.locno+'&ownerNo='+$('#ownerNo').combobox('getValue')+"&wasteNo="+$('#wasteNo').val();
					tempObj.datagrid('load');
					 $("#wasteDtl").show();
					 return;
				}else{
					 alert('保存失败!');
				}
		    },
			error:function(){
				wms_city_common.loading();
				alert('保存失败,请联系管理员!',2);
			}
	   });
};
billsmwaste.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
//========================库存商品信息======================================

//明细添加
billsmwaste.showAddItem = function(type){
	if(type == '1'){
		$('#boxSearchForm').form('clear');
		$("#dataGridJGBoxSelect").datagrid('clearData');
		$('#openUIBox').show().window('open');
	}else{
		$('#itemSearchForm').form('clear');
		$("#dataGridJGItemSelect").datagrid('clearData');
		//$('#showGirdNameForItem').val(gid);
		$('#openUIItem').show().window('open');
	}
};


//明细删除
billsmwaste.deleteItem  = function(gid){
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
//查询商品信息
billsmwaste.searchItem = function(){
	var ownerNo = $("#ownerNo").combobox('getValue');
    var fromObjStr=convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_sm_waste_dtl/get_Content?locno='+billsmwaste.user.locno+"&ownerNo="+ownerNo;
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['selectType'] = "0";
	billsmwaste.loadGridDataUtil('dataGridJGItemSelect', queryMxURL, reqParam);
};

//查询商品信息
billsmwaste.searchBox = function(){
	var ownerNo = $("#ownerNo").combobox('getValue');
    var fromObjStr=convertArray($('#boxSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_sm_waste_dtl/get_Content?locno='+billsmwaste.user.locno+"&ownerNo="+ownerNo;
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['selectType'] = "1";
	billsmwaste.loadGridDataUtil('dataGridJGBoxSelect', queryMxURL, reqParam);
};

billsmwaste.confirmItem = function(){
	var checkedRows = $("#dataGridJGItemSelect").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要添加的记录!',1);
		return;
	}
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"条商品信息吗？", function (r){  
		if (r) {
			$.each(checkedRows, function(index, item){
				var  rowData = {
						"cellNo":item.cellNo,
						"cellId":item.cellId,
						"itemNo":item.itemNo,
						"itemName":item.itemName,
						"barcode":item.barcode,
						"quality":item.quality,
						"itemType":item.itemType,
						"colorNo":item.colorNo,
						"colorName":item.colorName,
						"sizeNo":item.sizeNo,
						"wasteQty":item.conQty,
						"conQty":item.conQty,
						"sysNo":item.sysNo,
						"sysNoStr":item.sysNoStr,
						"brandNo":item.brandNo,
						"brandName":item.brandName
				};
				//把选择的商品编码行记录插入到父窗体中
				billsmwaste.insertRowAtEnd("wasteDtlDg",rowData);
				$('#openUIItem').window('close').hide();
			});
		}
	});
};

billsmwaste.confirmBox = function(){
	// 获取所有勾选checkbox的行
	var checkedRows = $("#dataGridJGBoxSelect").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要添加的记录!',1);
		return;
	}
	
	//返回的结果
	var saveFn = function(data){
		if (data.result == "success") {
			alert("添加成功!");
			$('#openUIBox').window('close');
			$('#wasteDtlDg').datagrid('load');
			$('#dataGridJGBoxSelect').datagrid('loadData', {total : 0,rows : [] });
		}else{
			alert(data.msg);
		}
		wms_city_common.loading();
	};
	
	//确定是否保持
	var wasteNo = $('#wasteNo').val();
	$.messager.confirm("确认","您确定要添加并保存这"+checkedRows.length+"个箱容器吗？", function (r){  
		if (r) {
			wms_city_common.loading("show","正在保存......");
			var boxList = [];
			$.each(checkedRows, function(index, item){
				var rowData = {
						"locno":item.locno,
						"ownerNo":item.ownerNo,
						"wasteNo":wasteNo,
						"panNo":item.panNo,
						"boxNo":item.boxNo
				};
				boxList[boxList.length] = rowData;
			});
			var url = BasePath+'/bill_sm_waste_dtl/saveWasteBoxContainer';
			var params = {datas:JSON.stringify(boxList)};
			ajaxRequest(url,params,saveFn);
		}
	});
};

billsmwaste.insertRowAtEnd = function(gid,rowData){
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
//========================库存商品信息END======================================
//========================保存明细======================================
//保存退仓单明细
billsmwaste.doSave = function(gid){
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
  var tempFlag = billsmwaste.endEdit(gid);
  if(tempFlag!=""){
		alert(tempFlag);
		return;
	}
	//debugger;
  //获取所有明细验证是否存在相同
	var hashOK = true;
	var checkSameResult = "";
	var hashObject = {};
	if(inserted.length>0){
		for (var i = 0; i < inserted.length; i++) {
			var no1 = inserted[i]['cellId']+"_"+inserted[i]['cellNo']+"_"+inserted[i]['itemNo']+"_"+inserted[i]['sizeNo'];
			var no2 = inserted[i]['cellId']+"_"+inserted[i]['cellNo']+"_"+inserted[i]['itemName']+"_"+inserted[i]['sizeNo'];
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
	
  //验证是否有删除整箱的数据
  var boxNoStr = "";
  if(deleted.length > 0){
	  $.each(deleted,function(index,item){
		  if(item.boxNo !=''&&item.boxNo!=null){
			  boxNoStr = item.boxNo;
			  return false;
		  }
	  });
  }	

  var tipMsg = "";
  if(boxNoStr!=''){
	  tipMsg = "存在删除整箱的明细,将会把箱下的所有明细删除,确定保存?";
  }else{
	  tipMsg = "确定保存?";
  }
	
  var wasteNo = $("input[id=wasteNo]",fromObj).val();
  var wasteType = $("input[id=wasteType]",fromObj).combobox('getValue');
  var ownerNo = $("input[id=ownerNo]",fromObj).combobox('getValue');
  var wasteDate = $("input[id=wasteDate]",fromObj).val();
  var remark = $("input[id=remark]",fromObj).val();
  var effectRow = {
  		inserted:JSON.stringify(inserted),
  		deleted:JSON.stringify(deleted),
  		updated:JSON.stringify(updated),
  		
		"locno":billsmwaste.user.locno,
		"wasteNo":wasteNo,
		"wasteType":wasteType,
		"ownerNo":ownerNo,
		"wasteDate":wasteDate,
		"remark":remark,
		"edittm":billsmwaste.user.currentDate19Str,
		"editor":billsmwaste.user.loginName,
		"editorname":billsmwaste.user.username
  };
  
  //确定是否保存
  $.messager.confirm('确认',tipMsg,function(r){    
		 if (r){
			  //3. 保存
			  wms_city_common.loading("show","正在保存......");
			  var url = BasePath+'/bill_sm_waste_dtl/addSmWasteDtl';
			  $.post(url, effectRow, function(result) {
				  wms_city_common.loading();
			      if(result.flag=='true'){
			    	  alert('保存成功!',1);
			    	  tempObj.datagrid('acceptChanges');
//			          $('#openUI').window('close'); 
			          //billsmwaste.closeWindow('openUI');
			          $('#wasteDtlDg').datagrid("reload");
			          billsmwaste.searchData();
			      }else if(result.flag=='warn'){
			      	alert(result.msg,1);
			      	return;
			      }else{
			      	alert(result.flag,2);
			      	return;
			      }
			  }, "JSON").error(function() {
				wms_city_common.loading();
			  	alert('保存失败!',1);
			  });
		 }    
  });
  

};
billsmwaste.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
//    		var itemNo = rowArr[i].itemNo;
    		var itemName = rowArr[i].itemName;
			var sizeNo = rowArr[i].sizeNo;
			var wasteQty = rowArr[i].wasteQty;
    		if(wasteQty!=null){
    			var conQty = rowArr[i].conQty;
    			if(wasteQty <= 0){
        			$(wasteQty.target).focus();
            		return "商品：" + itemName + " 尺码：" + sizeNo + " 数量不能为0;";
        		}else{
        			tempObj.datagrid('endEdit', i);
        		}
//        		if(wasteQty > conQty){
//            		$(wasteQty.target).focus();
//            		return "商品：" + itemName + " 尺码：" + sizeNo + " 数量不能大于库存数量;";
//            	}else{
//            		tempObj.datagrid('endEdit', i);
//            	}
    		}
    	}else{
    		return '数据验证没有通过!';
    	}
    }
    return "";
};
//========================保存明细END======================================

//==================主表删除、审核====================
//删除
billsmwaste.deleteUI = function(){
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
		if(checkedRows[i].status != 10){
			deliver += checkedRows[i].wasteNo + "\n";
			isDel = false;
		}
		keys.push(billsmwaste.user.locno+"-"+checkedRows[i].ownerNo+"-"+checkedRows[i].wasteNo);
	}
	if(!isDel) {
		alert("单据:" + deliver +"当前状态不允许删除，请重新选择！");
		return;
	}
     //2.绑定数据
     var url = BasePath+'/bill_sm_waste/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		 if (r){
			 wms_city_common.loading("show","正在删除......");
			 billsmwaste.ajaxRequest(url,data,true,function(result){
				 wms_city_common.loading();
				 if(result.flag=='success'){
					 alert('删除成功!');
				 }else{
					// alert("单据"+0+"已删除或状态已改变，不能进行 “修改/删除/审核”操作",2);
					 alert(result.msg);
				 }
				 billsmwaste.searchData();
			});  
		    }    
		});
};
function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
//审核
billsmwaste.check = function(){
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
				deliver += checkedRows[i].wasteNo + "\n";
				isDel = false;
			}
			keys.push(billsmwaste.user.locno+"-"+checkedRows[i].ownerNo+"-"+checkedRows[i].wasteNo);
		}
		if(!isDel) {
			alert("单据:" + deliver +"当前状态不允许审核，请重新选择！");
			return;
		}
		for(var i=0;i<checkedRows.length;i++){
			var wasteNo = checkedRows[i].wasteNo;
			var ownerNo = checkedRows[i].ownerNo;
			var wasteDate = checkedRows[i].wasteDate;
			var remark = checkedRows[i].remark;
			if(remark!=null) {
				if(trim(remark)==""){
					alert("单据:" + wasteNo +"无备注信息！");
					return;
				}
			} else {
				alert("单据:" + wasteNo +"备注信息不能为空！");
				return;
			}
			
			var untreadDtlUrl = BasePath
				+'/bill_sm_waste_dtl/dtlList.json?locno='+billsmwaste.user.locno
				+"&ownerNo="+ownerNo
				+"&wasteNo="+wasteNo;
			var existDtl = false;
			billsmwaste.ajaxRequest(untreadDtlUrl,{},false,function(result){
    			 if(result.total>0){
    				 existDtl = true;
    			 }
    		});
			if(!existDtl){
				alert("出库单["+wasteNo+"]不存在明细,不允许审核!",2);
				return;
			}
			if(wasteDate != null && wasteDate!="") {
			} else {
				alert('单据：' + wasteNo +',请录入调整日期!', 1);
				return;
			}
		}
		$.messager.confirm("确认","审核后数据将无法修改，您确定并继续操作吗？", function (r){  
	        if (r) {
	        	wms_city_common.loading("show","正在审核......");
	    		var url = BasePath+'/bill_sm_waste/check';
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
							billsmwaste.searchData();
						}
					});
	        }
		});
	}
};
//==================主表删除、审核====================
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
//下载模板
billsmwaste.downloadTemp = function (){
	window.open(BasePath + "/bill_sm_waste_dtl/downloadTemple");
}
//导入 
billsmwaste.showImport = function(formId){
	var fromObj = $("#"+formId);
	var wasteNo = $('input[id=wasteNo]',fromObj).val();
	var ownerNo = $('input[id=ownerNo]',fromObj).combobox('getValue');
	$("#iframe").attr("src",BasePath + "/bill_sm_waste_dtl/iframe?wasteNo="+wasteNo+"&ownerNo="+ownerNo+"&v="+new Date());
	$("#showImportDialog").window('open'); 
};
billsmwaste.importSuccess = function(){
	billsmwaste.closeWindow("showImportDialog");	
	var tempObj = $('#wasteDtlDg');
	tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_waste_dtl/dtl_List.json?locno='+billsmwaste.user.locno+'&ownerNo='+$('#ownerNo').combobox('getValue')+"&wasteNo="+$('#wasteNo').val();
	tempObj.datagrid('load');
	
	alert("导入成功!");
};
billsmwaste.loading = function(type,msg){
	wms_city_common.loading(type,msg);
};
billsmwaste.preColNames = [
                  			 	{field : 'cellNo',title : '储位编码',width : 200,align:'left'},
                  			 	{field : 'brandName',title : '品牌',width : 80,align:'left'},
                			 	{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
                			 	{field : 'itemName',title : '商品名称',width : 200,align:'left'},
                			 	{field : 'itemTypeStr',title : '商品属性',width : 200,align:'left'},
                			 	{field : 'qualityStr',title : '商品品质',width : 200,align:'left'},
                			 	{field : 'colorName',title : '颜色',width : 200,align:'left'},
                			 ];
billsmwaste.endColNames = [{field : 'total',title : '合计',width : 40,align:'right'}] ;

billsmwaste.exportDtl = function(){
	var wasteNo = $("#wasteNoDtl").val();
//	exportExcelBaseInfo('detaildataGrid','/bill_sm_waste_dtl/do_export.htm?wasteNo='+wasteNo,'其他出库明细('+wasteNo+')');
	export4Size("detaildataGrid", billsmwaste.preColNames, billsmwaste.endColNames, "其他出库明细("+wasteNo+")");
};
function export4Size(dgId,preColNames,endColNames,fileName){
	var url = BasePath+'/bill_sm_waste_dtl/dtlExport';
	var dgObj = $("#"+dgId);
	var locno = billsmwaste.user.locno;
	var wasteNo = $("#wasteNoDtl").val();
	var ownerNo = $('#ownerNoDtl').combobox('getValue');
	var dataRow = dgObj.datagrid('getRows');
	if(dataRow.length > 0){
		$("#exportExcelForm").remove();
		$("<form id='exportExcelForm'  method='post'></form>").appendTo("body");
		var fromObj = $('#exportExcelForm');
		wms_city_common.loading("show","正在导出");
		fromObj.form('submit', {
			url : url,
			onSubmit : function(param) {
				param.locno=locno;
				param.wasteNo=wasteNo;
				param.ownerNo=ownerNo;
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
	
}

//按库存数量保存出库数量
billsmwaste.saveShipment = function(){

  var updated = $('#wasteDtlDg').datagrid('getRows');
  if (updated <1) 
  	{
  		alert("需要先保存明细！");
  		return;
  	}
  var fromObj=$('#dataForm');
  var wasteNo = $("input[id=wasteNo]",fromObj).val();
  var wasteType = $("input[id=wasteType]",fromObj).combobox('getValue');
  var ownerNo = $("input[id=ownerNo]",fromObj).combobox('getValue');
  var wasteDate = $("input[id=wasteDate]",fromObj).val();
  var remark = $("input[id=remark]",fromObj).val();
	
	var effectRow = {
  		
  		updated:JSON.stringify(updated),
  		
		"locno":billsmwaste.user.locno,
		"wasteNo":wasteNo,
		"wasteType":wasteType,
		"ownerNo":ownerNo,
		"wasteDate":wasteDate,
		"remark":remark,
		"edittm":billsmwaste.user.currentDate19Str,
		"editor":billsmwaste.user.loginName,
		"editorname":billsmwaste.user.username
  };
	
  var url = BasePath+'/bill_sm_waste_dtl/saveShipment';
  $.post(url, effectRow, function(result) {
	  wms_city_common.loading();
      if(result.flag=='true'){
    	  alert('同步成功!',1);
    	  $('#wasteDtlDg').datagrid("reload");
          //billsmwaste.closeWindow('openUI');
          billsmwaste.searchData();
      }else if(result.flag=='warn'){
      	alert(result.msg,1);
      	return;
      }else{
      	alert(result.flag,2);
      	return;
      }
  }, "JSON").error(function() {
	wms_city_common.loading();
  	alert('同步失败!',1);
  });
};
billsmwaste.print = function(){
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows == null || rows.length == 0){
		alert("请选择需要打印的数据!");
		return;
	}
	var nos = '';
	for(var idx=0;idx<rows.length;idx++){
		nos += rows[idx].wasteNo;
		if(idx != rows.length-1){
			nos += ',';
		}
	}
	billsmwaste.ajaxRequest(BasePath+'/bill_sm_waste/print',{nos:nos,locno:billsmwaste.user.locno},false,function(data){
		if(data){
			if(data.status == 'success'){
				createPrintView(data);
			}else{
				alert(data.msg);
			}
		}
	});
};
function createPrintView(data){
	LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
	if (LODOP == null) {
		return;
	}
	LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
	var result = data.rows;
	for(var i=0,length=result.length;i<length;i++){
		LODOP.NewPage();
     	var headHtml = getHeadHtml(result[i]);
     	var bodyHtml = getBodyHtml(result[i]);
     	//设置表格内容
		LODOP.ADD_PRINT_TABLE(80,0,"100%",620,strStyle+bodyHtml);
     	//设置表格头部
		LODOP.ADD_PRINT_HTM(0,0,"100%",70,headHtml);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		
		
	}
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
}
function getHeadHtml(bill){
	var html = "<table style='width:100%;'><tr><td colspan='4' style='text-align:center;font-size:30px;'>其它出库单</td></tr>";
	html = html+"<tr><td>单号:"+bill.wasteNo+"</td>" +
			"<td>创建人:"+bill.creatorName+"</td>" +
			"<td>审核人:"+(bill.auditorName==null?"&nbsp;":bill.auditorName)+"</td>" +
			"<td>总计:"+bill.total+"</td></tr></table>";
	return html;
}
function getBodyHtml(bill){
	/*测试分页 新增数据S*/
	/*var array = bill.list;
	var tem = array[0];
	for(var idx=0;idx<200;idx++){
		array[array.length] = tem;
	}
	bill.list = array;*/
	/*测试分页 新增数据E*/
	var sizeMaxLength = bill.sizeMaxLength;
	var sizeTypeLen = 0;
	var _1LineSizeHtml = "";//第一行尺码
	var _2LineSizeHtml = "";//从第二行起尺码
	for(var key in bill.sizeList){
		if(++sizeTypeLen == 1){
			_1LineSizeHtml += "<td>"+key+"</td>";
			for(var idx=0;idx<sizeMaxLength;idx++){
				if(idx<bill.sizeList[key].length){
					_1LineSizeHtml += "<td>"+bill.sizeList[key][idx]+"</td>";
				}else{
					_1LineSizeHtml += "<td>&nbsp;</td>";
				}
			}
		}else{
			_2LineSizeHtml += "<tr><td>"+key+"</td>";
			for(var idx=0;idx<sizeMaxLength;idx++){
				if(idx<bill.sizeList[key].length){
					_2LineSizeHtml += "<td>"+bill.sizeList[key][idx]+"</td>";
				}else{
					_2LineSizeHtml += "<td>&nbsp;</td>";
				}
			}
			_2LineSizeHtml += "</tr>";
		}
	}
	var html = "<table border='1' cellspacing='0' cellpadding='1' width='100%' style='border-collapse:collapse' bordercolor='#333333'>";
	html += "<thead><tr><td rowspan='"+sizeTypeLen+"'>储位编码</td><td rowspan='"+sizeTypeLen+"'>商品编码</td><td rowspan='"+sizeTypeLen+"'>商品类型</td><td rowspan='"+sizeTypeLen+"'>商品品质</td>";
	html += _1LineSizeHtml;
	html += "<td rowspan='"+sizeTypeLen+"'>合计</td></tr>";
	html += _2LineSizeHtml;
	html += "</thead>";
	var list = bill.list;
	for(var idx=0;idx<list.length;idx++){
		var r = list[idx];
		html += "<tr>";
		html += "<td>"+r.cellNo+"</td>";
		html += "<td>"+r.itemNo+"</td>";
		html += "<td>"+r.itemTypeName+"</td>";
		html += "<td>"+r.qualityName+"</td>";
		html += "<td>"+r.sizeKind+"</td>";
		var sizeQtyMap = r.sizeQtyMap;
		for(var i=0;i<sizeMaxLength;i++){
			if(i<bill.sizeList[r.sizeKind].length){
				if(sizeQtyMap[bill.sizeList[r.sizeKind][i]] != null){
					html += "<td>"+sizeQtyMap[bill.sizeList[r.sizeKind][i]]+"</td>";
				}else{
					html += "<td>&nbsp;</td>";
				}
			}else{
				html += "<td>&nbsp;</td>";
			}
		}
		html += "<td>"+r.totalQty+"</td>";
		html += "</tr>";
	}
	html+="<tfoot><tr><td style='text-align:center;' colspan='"+(6+sizeMaxLength)+"'><font tdata='pageNO' format = '#' align='center'>第#页</font>/<font format='#' tdata='pageCount'>共#页</font></td></tr></tfoot>";
	html += "</table>";
	return html;
}

billsmwaste.onClickRowDtl = function(rowIndex, rowData){
	var curObj = $("#wasteDtlDg");
	curObj.datagrid('beginEdit', rowIndex);
	var edWasteQty = curObj.datagrid('getEditor', {index:rowIndex,field:'wasteQty'});
	var boxNo = rowData.boxNo;
	if(boxNo != ""&&boxNo!=null){
		$(edWasteQty.target).attr('disabled',true);
	}
};
