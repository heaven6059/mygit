var billsmwastedirect = {};
billsmwastedirect.wasteType = '04';
billsmwastedirect.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billsmwastedirect.store = {};
billsmwastedirect.storeNoFormatter = function(value, rowData, rowIndex){
	return billsmwastedirect.store[value];
};

billsmwastedirect.onLoadSuccess = function(data){
	if(data.footer[1] != null){
		billsmwastedirect.mainSumFoot = data.footer[1];
	}else{
			var rows = $('#dataGridJG').datagrid('getFooterRows');
   			rows[1] = billsmwastedirect.mainSumFoot;
			$('#dataGridJG').datagrid('reloadFooter');
	}
};

billsmwastedirect.operator = {};
$(document).ready(function(){

	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
	billsmwastedirect.initCurrentUser();
	//仓库
	billsmwastedirect.ajaxRequest(BasePath+'/bmdefloc/get_biz',{},false,billsmwastedirect.initLocno);
	//委托业主
	wms_city_common.comboboxLoadFilter(
			['ownerNo','ownerNoDtl'],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[false,false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billsmwastedirect.ownerNo,
			null,
			null
			);
	//状态
	wms_city_common.comboboxLoadFilter(
				['statusCondition'],
				null,
				null,
				null,
				true,
				[true],
				BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_WASTE_STATUS',
				{},
				billsmwastedirect.status,
				null,
				null
				);
	//门店
	wms_city_common.comboboxLoadFilter(
				['storeNoCondition'],
				'storeNo',
				'storeName',
				'valueAndText',
				false,
				[false,false,true],
				BasePath+'/store/getBizByBrand',
				{'storeType':'11','storeNoDc':billsmwastedirect.user.locno,'status':'0'},
				billsmwastedirect.store,
				null,
				null
				);
	$("#storeNo,#storeNoDtl").combogrid({
		delay: 350,
		panelWidth:350,   
        idField:'storeNo',  
        textField:'textFieldName',   
        pagination : true,
        rownumbers:true,
        mode: 'remote',
        pageSize:10,
        url:BasePath+'/store/list.json?storeType=11&status=0&storeNoDc='+billsmwastedirect.user.locno,
        columns:[[
			{field:'storeNo',title:'客户编码',width:100},  
			{field:'storeName',title:'客户名称',width:120}  
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
	//门店
	wms_city_common.comboboxLoadFilter(
				['operatorDtl','operator'],
				'workerNo',
				'workerName',
				'valueAndText',
				false,
				[false,false],
				BasePath+'/authority_user/user.json',
				{},
				billsmwastedirect.operator,
				null,
				null
				);
	
	//来源类型
	wms_city_common.comboboxLoadFilter(
			["sourceTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CONVERT_SOURCE_TYPE',
			{},
			billsmwastedirect.sourceType,
			null,
			null);
	
	//wms_city_common.initItemTypeAndQuality("itemTypeForItem","qualityForItem");
	billsmwastedirect.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',{},false,billsmwastedirect.getItemTypeData);
	billsmwastedirect.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',{},false,billsmwastedirect.initQuality);
	//品牌
	var objs = [];
	objs.push({"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNoCondition]',$('#searchForm'))});
	wms_city_common.loadSysNo4Cascade(objs);
	//wms_city_common.loadSysNo('#sysNo');
	//品牌
	var objsItem = [];
	objsItem.push( {"sysNoObj":$('#sysNo'),"brandNoObj":$('input[id=brandNo]',$('#itemSearchForm'))});
	wms_city_common.loadSysNo4Cascade(objsItem);
	
	//品牌
	var objsItemBox = [];
	objsItemBox.push( {"sysNoObj":$('#sysNoBox'),"brandNoObj":$('input[id=brandNoBox]',$('#boxSearchForm'))});
	wms_city_common.loadSysNo4Cascade(objsItemBox);
	
	wms_city_common.closeTip("openUI");
	
	$('#detaildataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
			   				billsmwastedirect.wasteQty = data.footer[1].wasteQty;
			   			}else{
			   				var rows = $('#detaildataGrid').datagrid('getFooterRows');
				   			rows[1]['wasteQty'] = billsmwastedirect.wasteQty;
				   			$('#detaildataGrid').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
});

//加载Grid数据Utils
billsmwastedirect.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};
//========================用户 信息========================
billsmwastedirect.user = {};
billsmwastedirect.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billsmwastedirect.userid = data.userid;
		billsmwastedirect.loginName = data.loginName;
		billsmwastedirect.currentDate = data.currentDate19Str;
		billsmwastedirect.locno = data.locno;
		billsmwastedirect.user = data;
	});
};
//========================用户 信息END========================
//========================仓库代码========================
billsmwastedirect.locno = {};
billsmwastedirect.locnoFormatter = function(value, rowData, rowIndex){
	return billsmwastedirect.locno[value];
};
//加载仓库代码
billsmwastedirect.initLocno = function(data){
	$('#locno').combobox({
	    data:data,
	    valueField:'locno',    
	    textField:'locname',
	    panelHeight:"auto"
	});
	billsmwastedirect.locno = billsmwastedirect.tansforLocno(data);
};
billsmwastedirect.tansforLocno = function(data){
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
billsmwastedirect.ownerNo = {};
billsmwastedirect.ownerNoFormatter = function(value, rowData, rowIndex){
	return billsmwastedirect.ownerNo[value];
};
//========================委托业主END========================
//========================状态========================
billsmwastedirect.status = {};
billsmwastedirect.statusFormatter = function(value, rowData, rowIndex){
	return billsmwastedirect.status[value];
};
//========================状态END========================
//========================来源类型========================
billsmwastedirect.sourceType = {};
billsmwastedirect.sourceTypeFormatter = function(value, rowData, rowIndex){
	return billsmwastedirect.sourceType[value];
};
//========================来源类型END========================
//========================品质========================
billsmwastedirect.quality = {};
billsmwastedirect.qualityFormatter = function(value, rowData, rowIndex){
	return billsmwastedirect.quality[value];
};
billsmwastedirect.initQuality = function(data){
	$('#qualityForItem').combobox({
		valueField:"itemvalue",
	     textField:"itemname",
	     data:data,
	     panelHeight:150
	});
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	billsmwastedirect.quality = temp;
};
/**商品属性**/
billsmwastedirect.itemType={};
billsmwastedirect.itemTypeFormatter = function(value, rowData, rowIndex){
	return billsmwastedirect.itemType[value];
};
billsmwastedirect.getItemTypeData = function(data){
	$("#itemTypeForItem").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150,
	     loadFilter:function(data){
	    	 var tempData = [];
	    	 for(var idx=0;idx<data.length;idx++){
	    		 if(data[idx].itemvalue == '0' || data[idx].itemvalue == '9'){
	    			 tempData[tempData.length] = data[idx];
	    		 }
	    	 }
	    	 return tempData;
	     }
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
	billsmwastedirect.itemType=temp;
};
//========================品质END========================
//========================主表查询、清空========================
billsmwastedirect.clearSearch = function(opt){
	var sysNo = $('#sysNoBox').combobox('getValue');
	$("#" + opt).form('clear');
	if(opt=='boxSearchForm'){
		$('#sysNoBox').combobox('setValue',sysNo);
	}
	$('#brandNoCondition').combobox("loadData",[]);
};
//主表
billsmwastedirect.searchData = function(){
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
	var queryMxURL=BasePath+'/bill_sm_waste_direct/d_list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam.locno = billsmwastedirect.user.locno;
	reqParam.wasteType = billsmwastedirect.wasteType;
	billsmwastedirect.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
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
billsmwastedirect.openWindow = function(windowId,opt){
	billsmwastedirect.closeWindow(windowId);
	$('#'+windowId).window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#'+windowId).window('open');
};

//关闭窗口
billsmwastedirect.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};
//新增窗口
billsmwastedirect.addUI = function(){
	$('#dataForm').form('clear');
	$("#storeNo").combobox('enable');
	billsmwastedirect.showHideBtn("add");
	$('#wasteDtlDg').datagrid('loadData', { total: 0, rows: [] });
	$("#wasteDtl").hide();
	billsmwastedirect.openWindow('openUI', '新增');
};
//明细
billsmwastedirect.dtlView = function(rowData,type){
	if(type=="view"){
		$("#wasteNoDtl").val(rowData.wasteNo);
		$('#storeNoDtl').combogrid('setValue',rowData.storeNo);
		billsmwastedirect.loadStoreName('#storeNoDtl',rowData.storeNo);
		$("#ownerNoDtl").combobox("select",rowData.ownerNo);
		$("#wasteDateDtl").datebox("setValue",rowData.wasteDate);
		$("#remarkDtl").val(rowData.remark);
		$("#operatorDtl").combobox("select",rowData.operator);
		billsmwastedirect.showHideBtn("info");
		$('#detaildataGrid').datagrid('loadData', { total: 0, rows: [] });
		billsmwastedirect.openWindow('openDtlUI', '查看');
		var tempObj = $('#detaildataGrid');
		tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_waste_dtl/dtl_List.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&wasteNo="+rowData.wasteNo;
	  tempObj.datagrid('load');
	}
};
//修改明细窗口
billsmwastedirect.loadDetail = function(rowData){
	
	$('#dataForm').form('load',rowData);
	billsmwastedirect.loadStoreName('#storeNo',rowData.storeNo);
	$("#storeNo").combobox('disable');
	billsmwastedirect.showHideBtn("edit");
	$('#wasteDtlDg').datagrid('loadData', { total: 0, rows: [] });
	$("#wasteDtl").show();
	billsmwastedirect.openWindow('openUI', '修改');
	var tempObj = $('#wasteDtlDg');
	tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_waste_dtl/dtlList.json?locno='+rowData.locno+'&ownerNo='+rowData.ownerNo+"&wasteNo="+rowData.wasteNo;
  tempObj.datagrid('load');
};
//加载客户
billsmwastedirect.loadStoreName = function(inputId,storeNo){
	var url = BasePath+'/store/get_biz';
	ajaxRequestAsync(url,{storeNo:storeNo},function(data){
		if(data != null){
			var columns = {storeNo:data[0].storeNo};
			$(inputId).combogrid("grid").datagrid("load", columns);
		}
	}); 
};
//修改退仓收货单
billsmwastedirect.updateUI = function(){
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
		billsmwastedirect.loadDetail(item);
	});
};
var isAdd = "";
//隐藏显示按钮
billsmwastedirect.showHideBtn = function(type){
	isAdd=type;
	if(type=="add") {
		$("#ownerNo").combobox('enable');
		$("#wasteDate").datebox('enable');
		$("#remark").attr("disabled",false);
		$("#save_main").show();
		$("#info_save").hide();
	} else if(type=="edit"){
		$("#ownerNo").combobox('disable');
		$("#wasteDate").datebox('enable');
		$("#remark").attr("disabled",false);
		$("#save_main").hide();
		$("#info_save").show();
	} else {
		$("#ownerNoDtl").combobox('disable');
		$("#wasteDateDtl").datebox('disable');
		$("#remarkDtl").attr('disabled',true);
		$("#save_main").hide();
		$("#info_save").hide();
	}
};
//========================新增、修改页面END========================
//========================保存主表======================================

billsmwastedirect.editMain = function(){
	if(!$("#dataForm").form('validate')){
		alert('数据验证没有通过!',1);
		return;
	}
	var wasteDate = $("#wasteDate").datebox('getValue');
	if(wasteDate != null && wasteDate!="") {
	} else {
		alert('日期不能为空，请录入出库日期!', 1);
		return;
	}
	if(!isStartEndDate(wms_city_common.getCurDate(),wasteDate)){ 
		alert("出库日期不能小于当前日期");   
        return;   
    }
	wms_city_common.loading("show","正在修改......");
	var ownerNo = $("#ownerNo").combobox('getValue');
	var url = BasePath+'/bill_sm_waste_direct/editMainInfo';
	$('#dataForm').form('submit', {
			url: url,
			onSubmit: function(param){
				param.editor = billsmwastedirect.user.loginName;
				param.editorname = billsmwastedirect.user.username;
				param.locno = billsmwastedirect.user.locno;
				param.ownerNo = ownerNo;
			},
			success: function(r){
				wms_city_common.loading();
				r = billsmwastedirect.parseJsonStringToJsonObj(r);
				if(r && r.flag == 'success'){
					 billsmwastedirect.showHideBtn("edit");
					 alert('保存成功!');
					 billsmwastedirect.searchData();
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
billsmwastedirect.saveMain = function(){
	var operator = $('#operator').combobox('getValue');
	var oms = $('#operator').combobox('getData');
	var operatorname = '';
	for(var idx=0;idx<oms.length;idx++){
		if(operator == oms[idx].workerNo){
			operatorname = oms[idx].workerName;
			break;
		}
	}
	if(!$("#dataForm").form('validate')){
		alert('数据验证没有通过!',1);
		return;
	}
	var wasteDate = $("#wasteDate").datebox('getValue');
	if(wasteDate != null && wasteDate!="") {
	} else {
		alert('日期不能为空，请录入出库日期!', 1);
		return;
	}
	if(!isStartEndDate(wms_city_common.getCurDate(),wasteDate)){ 
		alert("出库日期不能小于当前日期");   
        return;   
    }
	wms_city_common.loading("show","正在保存......");
	var url = BasePath+'/bill_sm_waste_direct/saveMainInfo';
	$('#dataForm').form('submit', {
			url: url,
			onSubmit: function(param){
				param.creator = billsmwastedirect.user.loginName;
				param.creatorname = billsmwastedirect.user.username;
				param.editor = billsmwastedirect.user.loginName;
				param.editorname = billsmwastedirect.user.username;
				param.operatorname = operatorname;
				param.locno = billsmwastedirect.user.locno;
				param.wasteType = billsmwastedirect.wasteType;
			},
			success: function(r){
				wms_city_common.loading();
				r = billsmwastedirect.parseJsonStringToJsonObj(r);
				if(r && r.success == 'true'){
					$("#dataForm input[id=wasteNo]").val(r.wasteNo);
					 billsmwastedirect.showHideBtn("edit");
					 alert('保存成功!');
					 billsmwastedirect.searchData();
					 var tempObj = $('#wasteDtlDg');
					tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_waste_dtl/dtl_List.json?locno='+billsmwastedirect.user.locno+'&ownerNo='+$('#ownerNo').combobox('getValue')+"&wasteNo="+$('#wasteNo').val();
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
billsmwastedirect.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
//========================库存商品信息======================================
//明细添加
billsmwastedirect.showAddItem = function(type,gid){
	if(type == '1'){
		$('#boxSearchForm').form('clear');
		$("#dataGridJGBoxSelect").datagrid('clearData');
		$('#openUIBox').show().window('open');
	}else{
		$('#itemSearchForm').form('clear');
		$("#dataGridJGItemSelect").datagrid('clearData');
		$('#openUIItem').show().window('open');
	}
	$('#showGirdNameForItem').val(gid);
	var sysNo = $('#storeNo').combogrid('getValue');
	if(sysNo != null && sysNo != '' && sysNo.length > 2){
		sysNo = sysNo.substring(0,2);
		if(type == '1'){
			$('#sysNoBox').combobox('setValue',sysNo);
			$("#sysNoBox").combobox('disable');
			billsmwastedirect.loadBrand(sysNo,'brandNoBox');
		}else{
			$('#sysNo').combobox('setValue',sysNo);
			$("#sysNo").combobox('disable');
			billsmwastedirect.loadBrand(sysNo,'brandNo');
		}
	}
};
billsmwastedirect.clearSearchItem = function(){
	var sysNo = $('#sysNo').combobox('getValue');
	$("#itemSearchForm").form('clear');
	$('#sysNo').combobox('setValue',sysNo);
};
billsmwastedirect.loadBrand = function(sysNo,bid){
	var url = BasePath + '/brand/get_bizDy?sysNo=' + sysNo;
	billsmwastedirect.ajaxRequest(url,{},true,function(data){
		obj  = $("#"+bid);
		obj.combobox({
			data:data,
			valueField:'brandNo',
			textField:'showName',
			panelHeight:150,
			loadFilter:function(data){
				var first = {};
				first.brandNo = '';
				first.showName = '全选';
				var tempData = [];
				tempData[tempData.length] = first;
				for(var idx=0;idx<data.length;idx++){
					data[idx].showName = data[idx].brandNo + '→' + data[idx].brandName;
					tempData[tempData.length] = data[idx];
				}
				return tempData;
			}
		});
	});
	
};
//明细删除
billsmwastedirect.deleteItem  = function(gid){
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
billsmwastedirect.searchItem  = function(){
	var ownerNo = $("#ownerNo").combobox('getValue');
    var fromObjStr=convertArray($('#itemSearchForm').serializeArray());
    var sysNo = $('#sysNo').combobox('getValue');
	var queryMxURL=BasePath+'/bill_sm_waste_dtl/get_Content?locno='+billsmwastedirect.user.locno+"&ownerNo="+ownerNo+"&attribute=4";
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam.isSmWasteDirect = 'Y';
	reqParam.sysNo = sysNo;
	billsmwastedirect.loadGridDataUtil('dataGridJGItemSelect', queryMxURL, reqParam);
};

billsmwastedirect.confirmItem = function(){
	var checkedRows = $("#dataGridJGItemSelect").datagrid("getChecked");// 获取所有勾选checkbox的行
	var len = checkedRows.length;
	var msg = "";
	var pass = false;
	if(len < 1){
		alert('请选择要添加的记录!',1);
		return;
	}
	var exist = $("#wasteDtlDg").datagrid("getRows");
	var rs = [];
	if(exist.length > 0){
		for(var i=0;i<len;i++){
			
				var str = checkedRows[i].cellNo+checkedRows[i].itemNo+checkedRows[i].sizeNo;
				var idx=0;
				for(;idx<exist.length;idx++){
					var boxNo=exist[idx].boxNo;
					if(!boxNo){
						if(str == exist[idx].cellNo+exist[idx].itemNo+exist[idx].sizeNo){
							break;
						}
					}
				}
				if(idx==exist.length){
					rs[rs.length] = checkedRows[i];
				}
		}
		checkedRows = rs;
		if(checkedRows.length == 0){
			msg = "所有商品信息全部存在,请重新选择商品！";
			pass = false;
		}else if(checkedRows.length == len){
			msg = "您确定要添加这"+checkedRows.length+"条商品信息吗？";
			pass = true;
		}else{
			msg = "部分商品信息已经存在,将去掉存在的商品信息继续添加？";
			pass = true;
		}
	}else{
		msg = "您确定要添加这"+checkedRows.length+"条商品信息吗？";
		pass = true;
	}
	if(pass){
		$.messager.confirm("确认",msg, function (r){  
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
					billsmwastedirect.insertRowAtEnd("wasteDtlDg",rowData);
					$('#openUIItem').window('close').hide();
				});
			}
		});
	}else{
		alert(msg);
	}
	
};
billsmwastedirect.insertRowAtEnd = function(gid,rowData){
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
billsmwastedirect.doSave = function(gid){
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
  var tempFlag = billsmwastedirect.endEdit(gid);
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
  var wasteNo = $("input[id=wasteNo]",fromObj).val();
  var wasteType = billsmwastedirect.wasteType;
  var ownerNo = $("input[id=ownerNo]",fromObj).combobox('getValue');
  var wasteDate = $("input[id=wasteDate]",fromObj).val();
  var remark = $("input[id=remark]",fromObj).val();
  var effectRow = {
  		inserted:JSON.stringify(inserted),
  		deleted:JSON.stringify(deleted),
  		updated:JSON.stringify(updated),
  		
		"locno":billsmwastedirect.user.locno,
		"wasteNo":wasteNo,
		"wasteType":wasteType,
		"ownerNo":ownerNo,
		"wasteDate":wasteDate,
		"remark":remark,
		"edittm":billsmwastedirect.user.currentDate19Str,
		"editor":billsmwastedirect.user.loginName,
		"editorname":billsmwastedirect.user.username
  };
  //3. 保存
  wms_city_common.loading("show","正在保存......");
  var url = BasePath+'/bill_sm_waste_dtl/addSmWasteDtl';
  $.post(url, effectRow, function(result) {
	  wms_city_common.loading();
      if(result.flag=='true'){
    	  alert('保存成功!',1);
    	  tempObj.datagrid('acceptChanges');
//          $('#openUI').window('close'); 
          //billsmwaste.closeWindow('openUI');
          $('#wasteDtlDg').datagrid("reload");
          billsmwastedirect.searchData();
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
billsmwastedirect.endEdit = function(gid){
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
        		}
    			tempObj.datagrid('endEdit', i);
        		/*if(wasteQty > conQty){
            		$(wasteQty.target).focus();
            		return "商品：" + itemName + " 尺码：" + sizeNo + " 数量不能大于库存数量;";
            	}else{
            		
            	}*/
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
billsmwastedirect.deleteUI = function(){
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
		keys.push(billsmwastedirect.user.locno+"-"+checkedRows[i].ownerNo+"-"+checkedRows[i].wasteNo);
	}
	if(!isDel) {
		alert("单据:" + deliver +"当前状态不允许删除，请重新选择！");
		return;
	}
     //2.绑定数据
     var url = BasePath+'/bill_sm_waste_direct/delete_records';
	 var data={
			    "ids":keys.join(",")
	  };	 
	 //3. 删除
	 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		 if (r){
			 wms_city_common.loading("show","正在删除......");
			 billsmwastedirect.ajaxRequest(url,data,true,function(result){
				 wms_city_common.loading();
				 if(result.flag=='success'){
					 alert('删除成功!');
				 }else{
					 //alert('删除失败,请联系管理员!',2);
					 alert(result.msg);
				 }
				 billsmwastedirect.searchData();
			});  
		    }    
		});
};
//审核
billsmwastedirect.check = function(){
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
			keys.push(billsmwastedirect.user.locno+"-"+checkedRows[i].ownerNo+"-"+checkedRows[i].wasteNo);
		}
		if(!isDel) {
			alert("单据:" + deliver +"当前状态不允许审核，请重新选择！");
			return;
		}
		for(var i=0;i<checkedRows.length;i++){
			var wasteNo = checkedRows[i].wasteNo;
			var ownerNo = checkedRows[i].ownerNo;
			var wasteDate = checkedRows[i].wasteDate;
			var untreadDtlUrl = BasePath
				+'/bill_sm_waste_dtl/dtlList.json?locno='+billsmwastedirect.user.locno
				+"&ownerNo="+ownerNo
				+"&wasteNo="+wasteNo;
			var existDtl = false;
			billsmwastedirect.ajaxRequest(untreadDtlUrl,{},false,function(result){
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
	    		var url = BasePath+'/bill_sm_waste_direct/check';
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
							billsmwastedirect.searchData();
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
billsmwastedirect.downloadTemp = function (){
	window.open(BasePath + "/bill_sm_waste_dtl/downloadTemple");
}
//导入 
billsmwastedirect.showImport = function(formId){
	var fromObj = $("#"+formId);
	var wasteNo = $('input[id=wasteNo]',fromObj).val();
	var ownerNo = $('input[id=ownerNo]',fromObj).combobox('getValue');
	$("#iframe").attr("src",BasePath + "/bill_sm_waste_dtl_direct/iframe?wasteNo="+wasteNo+"&ownerNo="+ownerNo+"&v="+new Date());
	$("#showImportDialog").window('open'); 
}
billsmwastedirect.importSuccess = function(){
	billsmwastedirect.closeWindow("showImportDialog");	
	var tempObj = $('#wasteDtlDg');
	tempObj.datagrid( 'options' ).url = BasePath+'/bill_sm_waste_dtl/dtl_List.json?locno='+billsmwastedirect.user.locno+'&ownerNo='+$('#ownerNo').combobox('getValue')+"&wasteNo="+$('#wasteNo').val();
	tempObj.datagrid('load');
	
	alert("导入成功!");
};
billsmwastedirect.loading = function(type,msg){
	wms_city_common.loading(type,msg);
};
billsmwastedirect.exportDtl = function(){
	var wasteNo = $("#wasteNoDtl").val();
	exportExcelBaseInfo('detaildataGrid','/bill_sm_waste_dtl/do_export.htm?wasteNo='+wasteNo,'直接出库明细('+wasteNo+')');
};
//按库存数量保存出库数量
billsmwastedirect.saveShipment = function(){

  var updated = $('#wasteDtlDg').datagrid('getRows');
  if (updated <1) 
  	{
  		alert("需要先保存明细！");
  		return;
  	}
  var fromObj=$('#dataForm');
  var wasteNo = $("input[id=wasteNo]",fromObj).val();
  var wasteType = billsmwastedirect.wasteType;
  var ownerNo = $("input[id=ownerNo]",fromObj).combobox('getValue');
  var wasteDate = $("input[id=wasteDate]",fromObj).val();
  var remark = $("input[id=remark]",fromObj).val();
	
	var effectRow = {
  		
  		updated:JSON.stringify(updated),
  		
		"locno":billsmwastedirect.user.locno,
		"wasteNo":wasteNo,
		"wasteType":wasteType,
		"ownerNo":ownerNo,
		"wasteDate":wasteDate,
		"remark":remark,
		"edittm":billsmwastedirect.user.currentDate19Str,
		"editor":billsmwastedirect.user.loginName,
		"editorname":billsmwastedirect.user.username
  };
	
  var url = BasePath+'/bill_sm_waste_dtl/saveShipment';
  $.post(url, effectRow, function(result) {
	  wms_city_common.loading();
      if(result.flag=='true'){
    	  alert('同步成功!',1);
    	  $('#wasteDtlDg').datagrid("reload");
          //billsmwaste.closeWindow('openUI');
          billsmwastedirect.searchData();
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
billsmwastedirect.printDetail4SizeHorizontal = function(){
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	wms_city_common.loading("show","正在加载......");
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].locno+"|"+rows[i].wasteNo+"|"+rows[i].ownerNo+"|"+rows[i].storeNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_sm_waste_dtl_direct/printDetail4SizeHorizontal',
        data: {
        	keys:keys.join(",")
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
	var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	if(LODOP==null){
		return;
	}
	LODOP.SET_PRINT_PAGESIZE(1, 0, 0, "A4");
	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
	if(data.pages.length <= 0){
		alert("所选单据不存在明细,请重新选择!");
		return;
	}
	for(var idx=0;idx<data.pages.length;idx++){
		LODOP.NewPageA();
		var headHtml = createHeadHtml(data.pages[idx]);
		var bodyHtml = createBodyHtml(data.pages[idx]);
		//设置表格内容
		LODOP.ADD_PRINT_TABLE(85,0,"100%",380,strStyle+bodyHtml);
		LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
		//设置表格头部
		LODOP.ADD_PRINT_HTM(0,0,"100%",110,headHtml);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		//设置条码
		LODOP.ADD_PRINT_BARCODE(50,10,250,40,"128A",data.pages[idx].wasteNo);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	}
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
}
function createHeadHtml(page){
	var html = "<table style='width:100%;font-size:13px;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='4'>门店送货单</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+page.wasteNo+"</td><td>发货方："+billsmwastedirect.user.locname+"" +
			"</td><td>收货方："+billsmwastedirect.store[page.storeNo]+"("+page.storeNo+")</td><td>总合计："+page.total+"</td></tr>";
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
	html += "<td rowspan='"+rowspan+"'>商品编码</td>";
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
};
//查询箱列表
billsmwastedirect.searchBox = function(){
	var ownerNo = $("#ownerNo").combobox('getValue');
    var fromObjStr=convertArray($('#boxSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_sm_waste_dtl/get_Content?locno='+billsmwastedirect.user.locno+"&ownerNo="+ownerNo;
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['selectType'] = "1";
	var sysNo = $('#sysNoBox').combobox('getValue');
	reqParam.sysNo=sysNo;
	billsmwastedirect.loadGridDataUtil('dataGridJGBoxSelect', queryMxURL, reqParam);
};

billsmwastedirect.confirmBox = function(){
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
billsmwastedirect.onClickRowDtl = function(rowIndex, rowData){
	var curObj = $("#wasteDtlDg");
	curObj.datagrid('beginEdit', rowIndex);
	var edWasteQty = curObj.datagrid('getEditor', {index:rowIndex,field:'wasteQty'});
	var boxNo = rowData.boxNo;
	if(boxNo != ""&&boxNo!=null){
		$(edWasteQty.target).attr('disabled',true);
	}
};