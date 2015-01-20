var billsmotherin = {};

billsmotherin.curDialogId;
billsmotherin.detaildataGridFooter={};

billsmotherin.ajaxRequest = function(url,reqParam,async,callback){
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
	billsmotherin.initCurrentUser();
	//仓库
	billsmotherin.ajaxRequest(BasePath+'/bmdefloc/get_biz',{},false,billsmotherin.initLocno);
	//委托业主
	billsmotherin.ajaxRequest(BasePath+'/entrust_owner/get_biz',{},false,billsmotherin.initOwnerNo);
	//状态
	billsmotherin.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OTHERIN_STATUS',{},false,billsmotherin.initStatus);
	//类型
	billsmotherin.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_OTHERIN_TYPE',{},false,billsmotherin.initTypes);
	//品质
	billsmotherin.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',{},false,billsmotherin.initQuality);
//	//商品类型
	billsmotherin.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',{},false,billsmotherin.getItemTypeData);
	wms_city_common.initItemTypeAndQuality("itemType","areaQuality");
	//品牌库

	//初始化表格加载事件
	billsmotherin.initGridOnLoadSuccess();
	wms_city_common.closeTip("openUI");
	
	//wms_city_common.loadSysNo('#sysNo');
	var objs = [];
	objs.push(
			{"sysNoObj":$('input[id=sysNoCondition]',$('#searchForm'))},
			{"sysNoObj":$('input[id=sysNo]',$('#itemSearchForm'))},
			{"sysNoObj":$('#sysNoCondition'),"brandNoObj":$('input[id=brandNoCondition]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	
	var objsItem = [];
	objsItem.push({"sysNoObj":$('#sysNo'),"brandNoObj":$('input[id=brandNoItem]',$('#itemSearchForm'))});
	wms_city_common.loadSysNo4Cascade(objsItem);
});
billsmotherin.onLoadSuccess = function(data){
	if(data.rows.length > 0){
		if (data.footer[1] != null) {
			billsmotherin.instorageQty = data.footer[1];
		} else {
			var rows = $('#dataGridJG').datagrid('getFooterRows');
   			rows[1] = billsmotherin.instorageQty;
   			$('#dataGridJG').datagrid('reloadFooter');
		}
	}
}

// 初始化表格加载事件
billsmotherin.initGridOnLoadSuccess = function() {
	$('#detaildataGrid').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.footer!=undefined){
				if (data.footer[1].isselectsum) {
					billsmotherin.detaildataGridFooter.instorageQty = data.footer[1].instorageQty;
					billsmotherin.detaildataGridFooter.recheckQty = data.footer[1].recheckQty;
				} else {
					var rows = $('#detaildataGrid').datagrid('getFooterRows');
					rows[1]['instorageQty'] = billsmotherin.detaildataGridFooter.instorageQty;
					rows[1]['recheckQty'] = billsmotherin.detaildataGridFooter.recheckQty;
					$('#detaildataGrid').datagrid('reloadFooter');
				}
			}			
		}
	});
};

// 加载Grid数据Utils
billsmotherin.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

// 主档表格加载后事件
dataGridOnLoadSuccess = function(data) {
	if(data.result=='fail'){
		alert(data.msg);		
	}
};

//========================用户 信息========================
billsmotherin.user = {};
billsmotherin.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billsmotherin.userid = data.userid;
		billsmotherin.loginName = data.loginName;
		billsmotherin.currentDate = data.currentDate19Str;
		billsmotherin.locno = data.locno;
		billsmotherin.user = data;
	});
};
//========================用户 信息END========================
//========================仓库代码========================
billsmotherin.locno = {};
billsmotherin.locnoFormatter = function(value, rowData, rowIndex){
	return billsmotherin.locno[value];
};
//加载仓库代码
billsmotherin.initLocno = function(data){
	$('#locno').combobox({
	    data:data,
	    valueField:'locno',    
	    textField:'locname',
	    panelHeight:"auto"
	});
	billsmotherin.locno = billsmotherin.tansforLocno(data);
};
billsmotherin.tansforLocno = function(data){
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
billsmotherin.ownerNo = {};
billsmotherin.ownerNoFormatter = function(value, rowData, rowIndex){
	return billsmotherin.ownerNo[value];
};
billsmotherin.initOwnerNo = function(data){
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
	billsmotherin.ownerNo = billsmotherin.tansforOwner(data);
};
billsmotherin.tansforOwner = function(data){
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
billsmotherin.status = {};
billsmotherin.statusFormatter = function(value, rowData, rowIndex){
	return billsmotherin.status[value];
};
//初始化状态
billsmotherin.initStatus = function(data){
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
	billsmotherin.status = temp;
};
//========================状态END========================
billsmotherin.types = {};
billsmotherin.otherinTypeFormatter = function(value, rowData, rowIndex){
	return billsmotherin.types[value];
};
//初始化状态
billsmotherin.initTypes = function(data){
	var newPageOtherinType=[];
	
	//仓出和移仓类型不可新增
	$.each(data,function(index,item){
		if(!(item.itemvalue=='1' || item.itemvalue=='4')){
			newPageOtherinType.push(item);			
		}
	});
		
	
	$('#otherinType').combobox({
		data:newPageOtherinType,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	$('#otherinTypeCondition').combobox({
		data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	$('#otherinTypeDtl').combobox({
		data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	var temp = {};
	
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
		if(data.length==1){
			break;
		}
	}
	billsmotherin.types = temp;
};
//========================品质========================
billsmotherin.quality = {};
billsmotherin.qualityFormatter = function(value, rowData, rowIndex){
	return billsmotherin.quality[value];
};
billsmotherin.initQuality = function(data){
//	$('#areaQuality').combobox({
//		valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     data:data,
//	     panelHeight:200
//	});
	$('#areaQualityDtl').combobox({
		valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:200
	});
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billsmotherin.quality = temp;
};
/**商品属性**/
billsmotherin.itemType={};
billsmotherin.itemTypeFormatter = function(value, rowData, rowIndex){
	return billsmotherin.itemType[value];
};
billsmotherin.getItemTypeData = function(data){
//	$("#itemType").combobox({
//	     valueField:"itemvalue",
//	     textField:"itemnamedetail",
//	     data:data,
//	     panelHeight:"auto"
//	});
	$("#itemTypeDtl").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billsmotherin.itemType=temp;
};
//========================品质END========================
//========================主表查询、清空========================
billsmotherin.clearSearch = function(opt){
	$("#" + opt).form('clear');
};
//主表
billsmotherin.searchData = function(){
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
	var queryMxURL=BasePath+'/bill_sm_otherin/list.json?locno='+billsmotherin.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	billsmotherin.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
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
billsmotherin.openWindow = function(windowId,opt){
	billsmotherin.closeWindow(windowId);
	$('#'+windowId).window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#'+windowId).window('open');
};

//关闭窗口
billsmotherin.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};
//新增窗口
billsmotherin.addUI = function(){
	$('#dataForm').form('clear');
	billsmotherin.showHideBtn("add");
	$('#otherinDtlDg').datagrid('loadData', { total: 0, rows: [] });
	$("#otherinDtl").hide();
	billsmotherin.openWindow('openUI', '新增');
};
//明细
billsmotherin.dtlView = function(rowData,type){
	if(type=="view"){
		$("#otherinNoDtl").val(rowData.otherinNo);
		$("#otherinTypeDtl").combobox("select",rowData.otherinType);
		$("#ownerNoDtl").combobox("select",rowData.ownerNo);
		$("#areaQualityDtl").combobox("select",rowData.areaQuality);
		$("#itemTypeDtl").combobox("select",rowData.itemType);		
		$("#instorageDateDtl").datebox("setValue",rowData.instorageDate);
		$("#remarkDtl").val(rowData.remark);
		billsmotherin.showHideBtn("info");
		$('#detaildataGrid').datagrid('loadData', { total: 0, rows: [] });
		billsmotherin.openWindow('openDtlUI', '查看');
		var tempObj = $('#detaildataGrid');
		tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_otherin_dtl/dtl_List.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&otherinNo="+rowData.otherinNo;
	  tempObj.datagrid('load');
	}
};
//修改明细窗口
billsmotherin.loadDetail = function(rowData){
	$("#itemType").combobox("select",rowData.itemType);
	$('#dataForm').form('load',rowData);
	billsmotherin.showHideBtn("edit");
	$('#otherinDtlDg').datagrid('loadData', { total: 0, rows: [] });
	$("#otherinDtl").show();
	billsmotherin.openWindow('openUI', '修改');
	var tempObj = $('#otherinDtlDg');
	tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_otherin_dtl/list.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&otherinNo="+rowData.otherinNo;
  tempObj.datagrid('load');
};

//修改退仓收货单
billsmotherin.updateUI = function(){
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
		billsmotherin.loadDetail(item);
	});
};
var isAdd = "";
//隐藏显示按钮
billsmotherin.showHideBtn = function(type){
	isAdd=type;
	if(type=="add") {
		$("#otherinType").combobox('enable');
		$("#ownerNo").combobox('enable');
		$("#areaQuality").combobox('enable');
		$("#itemType").combobox('enable');
		$("#instorageDateDate").datebox('enable');
		$("#remark").attr("disabled",false);
		$("#save_main").show();
		$("#info_save").hide();
	} else if(type=="edit"){
		$("#otherinType").combobox('disable');
		$("#ownerNo").combobox('disable');
		$("#areaQuality").combobox('disable');
		$("#itemType").combobox('disable');	
		$("#instorageDateDate").datebox('enable');
		$("#remark").attr("disabled",false);
		$("#save_main").hide();
		$("#info_save").show();
	} else {
		$("#otherinTypeDtl").combobox('disable');
		$("#ownerNoDtl").combobox('disable');
		$("#areaQualityDtl").combobox('disable');		
		$("#itemTypeDtl").combobox('disable');	
		$("#instorageDateDtl").datebox('disable');
		$("#remarkDtl").attr('disabled',true);
		$("#save_main").hide();
		$("#info_save").hide();
	}
};
//========================新增、修改页面END========================
//========================保存主表======================================

billsmotherin.editMain = function(){
	if(!$("#dataForm").form('validate')){
		alert('请输入完整信息！',1);
		return;
	}
	if($("#instorageDate").datebox('getValue')==''){
		alert('日期不能为空，请录入入库日期！',1);
		return;
	}	
	var remark = $("#remark").val();
	if(trim(remark) == ""){
		alert("请输入备注信息！",1);
		return;
	}
	wms_city_common.loading("show","正在修改......");
	var ownerNo = $("#ownerNo").combobox('getValue');
	var url = BasePath+'/bill_sm_otherin/editMainInfo';
	$('#dataForm').form('submit', {
			url: url,
			onSubmit: function(param){
				param.editor = billsmotherin.user.loginName;
				param.edittm = billsmotherin.user.currentDate19Str;
				param.locno = billsmotherin.user.locno;
				param.ownerNo = ownerNo;
			},
			success: function(r){
				wms_city_common.loading();
				r = billsmotherin.parseJsonStringToJsonObj(r);
				if(r && r.flag == 'success'){
					 billsmotherin.showHideBtn("edit");
					 alert('保存成功!');
					 billsmotherin.searchData();
					 return;
				}else{
					// alert('保存失败!');
					alert(r.msg);
				}
		    },
			error:function(){
				wms_city_common.loading();
				alert('保存失败,请联系管理员!',2);
			}
	   });
};
billsmotherin.saveMain = function(){
	if(!$("#dataForm").form('validate')){
		alert('请输入完整信息！',1);
		return;
	}	
	if($("#instorageDate").datebox('getValue')==''){
		alert('日期不能为空，请录入入库日期！',1);
		return;
	}	
	var remark = $("#remark").val();
	if(trim(remark) == ""){
		alert("请输入备注信息！",1);
		return;
	}
	wms_city_common.loading("show","正在保存......");
	var url = BasePath+'/bill_sm_otherin/saveMainInfo';
	$('#dataForm').form('submit', {
			url: url,
			onSubmit: function(param){
				param.createtm = billsmotherin.user.currentDate19Str;
				param.creator = billsmotherin.user.loginName;
				param.editor = billsmotherin.user.loginName;
				param.editorName = billsmotherin.user.username;
				param.edittm = billsmotherin.user.currentDate19Str;
				param.locno = billsmotherin.user.locno;
			},
			success: function(r){
				wms_city_common.loading();
				r = billsmotherin.parseJsonStringToJsonObj(r);
				if(r && r.success == 'true'){
					$("#dataForm input[id=otherinNo]").val(r.otherinNo);
					 billsmotherin.showHideBtn("edit");
					 alert('保存成功!');
					 billsmotherin.searchData();
					 $('#otherinDtlDg').datagrid('loadData', { total: 0, rows: [] });
					 var tempObj = $('#otherinDtlDg');
				     tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_otherin_dtl/list.json?locno='+billsmotherin.user.locno+"&otherinNo="+r.otherinNo;
					 tempObj.datagrid('load');
					 $("#otherinDtl").show();
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
billsmotherin.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
//========================库存商品信息======================================
//明细添加
billsmotherin.showAddItem = function(gid){
	$('#itemSearchForm').form('clear');
	$("#dataGridJGItemSelect").datagrid('clearData');
	$('#showGirdNameForItem').val(gid);
	$('#openUIItem').show().window('open');
};
//明细删除
billsmotherin.deleteItem  = function(gid){
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
billsmotherin.searchItem  = function(){
	var ownerNo = $("#ownerNo").combobox('getValue');
	
	
    var fromObjStr=convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_sm_otherin_dtl/get_Content?locno='+billsmotherin.user.locno
	+"&ownerNo="+ownerNo;
	var reqParam = eval("(" +fromObjStr+ ")");
	billsmotherin.loadGridDataUtil('dataGridJGItemSelect', queryMxURL, reqParam);
};

billsmotherin.confirmItem = function(){
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
						"conQty":item.conQty,
						"sysNo":item.sysNo,
						"sysNoStr":item.sysNoStr,
						"brandNO":item.brandNO,
						"brandNoStr":item.brandNoStr
				};
				//把选择的商品编码行记录插入到父窗体中
				billsmotherin.insertRowAtEnd("otherinDtlDg",rowData);
				$('#openUIItem').window('close').hide();
			});
		}
	});
};
billsmotherin.insertRowAtEnd = function(gid,rowData){
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
//保存其它入库单明细
billsmotherin.doSave = function(gid){
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
  var tempFlag = billsmotherin.endEdit(gid);
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
			var no = inserted[i]['cellNo']+"_"+inserted[i]['itemNo']+"_"+inserted[i]['sizeNo'];
			if(hashObject[no]){
				checkSameResult += "【" + no + "】";
				hashOK = false;
			}
		}
	}
	if(!hashOK){
		alert("商品重复：" + checkSameResult);
		return;
	}
  var otherinNo = $("input[id=otherinNo]",fromObj).val();
  var otherinType = $("input[id=otherinType]",fromObj).combobox('getValue');
  var ownerNo = $("input[id=ownerNo]",fromObj).combobox('getValue');
  var instorageDate = $("input[id=instorageDate]",fromObj).val();
  var remark = $("input[id=remark]",fromObj).val();
  var effectRow = {
  		inserted:JSON.stringify(inserted),
  		deleted:JSON.stringify(deleted),
  		updated:JSON.stringify(updated),
  		
		"locno":billsmotherin.user.locno,
		"otherinNo":otherinNo,
		"otherinType":otherinType,
		"ownerNo":ownerNo,
		"instorageDate":instorageDate,
		"remark":remark,
		"editor":billsmotherin.user.loginName,
		"editorName":billsmotherin.user.username
  };
  //3. 保存
  wms_city_common.loading("show","正在保存......");
  var url = BasePath+'/bill_sm_otherin_dtl/addSmOtherinDtl';
  $.post(url, effectRow, function(result) {
	  wms_city_common.loading();
      if(result.flag=='true'){
    	  alert('保存成功!',1);
    	  tempObj.datagrid('acceptChanges');
          billsmotherin.closeWindow('openUI');
          billsmotherin.searchData();
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
};
billsmotherin.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		var itemName = rowArr[i].itemName;
			var sizeNo = rowArr[i].sizeNo;
			var instorageQty = rowArr[i].instorageQty;
    		if(instorageQty!=null){
    			if(instorageQty <= 0){
        			$(instorageQty.target).focus();
            		return "商品：" + itemName + " 尺码：" + sizeNo + " 数量必须大于0;";
        		}else{
            		tempObj.datagrid('endEdit', i);
            	}
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
billsmotherin.deleteUI = function(){
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
			deliver += checkedRows[i].otherinNo + "\n";
			isDel = false;
		}
		keys.push(billsmotherin.user.locno+"-"+checkedRows[i].ownerNo+"-"+checkedRows[i].otherinNo);
	}
	if(!isDel) {
		alert("单据:" + deliver +"当前状态不允许删除，请重新选择！");
		return;
	}
     //2.绑定数据
     var url = BasePath+'/bill_sm_otherin/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		 if (r){
			 wms_city_common.loading("show","正在删除......");
			 billsmotherin.ajaxRequest(url,data,true,function(result){
				 wms_city_common.loading();
				 if(result.flag=='success'){
					 alert('删除成功!');
				 }else{
					// alert('删除失败,请联系管理员!',2);
					 alert(result.msg);
				 }
				 billsmotherin.searchData();
			});  
		    }    
		});
};
function trim(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
//审核
billsmotherin.check = function(){
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
				deliver += checkedRows[i].otherinNo + "\n";
				isDel = false;
			}
			keys.push(billsmotherin.user.locno+"-"+checkedRows[i].ownerNo+"-"+checkedRows[i].otherinNo);
		}
		if(!isDel) {
			alert("单据:" + deliver +"当前状态不允许审核，请重新选择！");
			return;
		}
		for(var i=0;i<checkedRows.length;i++){
			var otherinNo = checkedRows[i].otherinNo;
			var ownerNo = checkedRows[i].ownerNo;
			var remark = checkedRows[i].remark;
			if(remark!=null) {
				if(trim(remark)==""){
					alert("单据:" + otherinNo +"无备注信息！");
					return;
				}
			} else {
				alert("单据:" + otherinNo +"备注信息不能为空！");
				return;
			}
			var untreadDtlUrl = BasePath
				+'/bill_sm_otherin_dtl/list.json?locno='+billsmotherin.user.locno
				+"&ownerNo="+ownerNo
				+"&otherinNo="+otherinNo;
			var existDtl = false;
			billsmotherin.ajaxRequest(untreadDtlUrl,{},false,function(result){
    			 if(result.total>0){
    				 existDtl = true;
    			 }
    		});
			if(!existDtl){
				alert("其它入库单["+otherinNo+"]不存在明细,不允许审核!",2);
				return;
			}
		}
		$.messager.confirm("确认","审核后数据将无法修改，您确定并继续操作吗？", function (r){  
	        if (r) {
	        	wms_city_common.loading("show","正在审核......");
	    		var url = BasePath+'/bill_sm_otherin/check';
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
							billsmotherin.searchData();
						}
					});
	        }
		});
	}
};
//下载模板
billsmotherin.downloadTemp = function (){
	window.open("../bill_sm_otherin_dtl/downloadTemple");
}
//导入 
billsmotherin.showImport = function(formId,dalogId){
	billsmotherin.curDialogId = dalogId;
	var fromObj = $("#"+formId);
	var otherinNo = $('input[id=otherinNo]',fromObj).val();
	var ownerNo = $('input[id=ownerNo]',fromObj).combobox('getValue');
	$("#iframe").attr("src","../bill_sm_otherin_dtl/iframe?otherinNo="+otherinNo+"&ownerNo="+ownerNo+"&v="+new Date());
	$("#showImportDialog").window('open'); 
}
billsmotherin.importSuccess = function(){
	billsmotherin.closeShowWin(billsmotherin.curDialogId);
	billsmotherin.closeShowWin("showImportDialog");
	$('#otherinDtlDg').datagrid('load');
	alert("导入成功!");
}
billsmotherin.loading = function(type,msg){
	wms_city_common.loading(type,msg);
}
billsmotherin.closeShowWin = function(id){
	$('#'+id).window('close');
};

//==================明细导出====================
billsmotherin.preColNames = [
             			 	{field : 'cellNo',title : '储位编码',width : 200,align:'left'},
             			 	{field : 'brandNoStr',title : '品牌',width : 80,align:'left'},
           			 	{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
           			 	{field : 'itemName',title : '商品名称',width : 200,align:'left'},
           			 	{field : 'colorName',title : '颜色',width : 200,align:'left'},
           			 ];
billsmotherin.endColNames = [{field : 'total',title : '合计',width : 40,align:'right'}] ;
billsmotherin.exportDtl = function(){
	var otherinNo = $("#otherinNoDtl").val();
//	var ownerNo = $("#ownerNoDtl").combobox('getValue');
//	var url = '/bill_sm_otherin_dtl/export_OtherinNo?locno='+billsmotherin.user.locno+'&ownerNo='+ownerNo+"&otherinNo="+otherinNo;
//	exportExcelBaseInfo('detaildataGrid',url,'其他入库明细');
	
	export4Size("detaildataGrid", billsmotherin.preColNames, billsmotherin.endColNames, "其他入库明细("+otherinNo+")");
};
function export4Size(dgId,preColNames,endColNames,fileName){
	var url = BasePath+'/bill_sm_otherin_dtl/dtlExport';
	var dgObj = $("#"+dgId);
	var locno = billsmotherin.user.locno;
	var otherinNo = $("#otherinNoDtl").val();
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
				param.otherinNo=otherinNo;
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
billsmotherin.print = function(){
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows == null || rows.length == 0){
		alert("请选择需要打印的数据!");
		return;
	}
	var nos = '';
	for(var idx=0;idx<rows.length;idx++){
		nos += rows[idx].otherinNo;
		if(idx != rows.length-1){
			nos += ',';
		}
	}
	billsmotherin.ajaxRequest(BasePath+'/bill_sm_otherin/print',{nos:nos,locno:billsmotherin.user.locno},false,function(data){
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
	var html = "<table style='width:100%;'><tr><td colspan='4' style='text-align:center;font-size:30px;'>其它入库单</td></tr>";
	html = html+"<tr><td>单号:"+bill.otherinNo+"</td>" +
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