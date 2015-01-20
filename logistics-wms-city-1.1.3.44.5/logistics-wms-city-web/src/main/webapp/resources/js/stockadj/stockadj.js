var stockadj = {};

stockadj.locno;
stockadj.dataGridLUFooter={};
stockadj.dataGridJGFooter={};

$(document).ready(function(){
	$("#createtmBeginCondition").datebox('setValue',getDateStr(-2));
	stockadj.initCurrentUser();
	//状态
	stockadj.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_STOCK_STATUS',{},false,stockadj.initStatus);
	//货主
	stockadj.ajaxRequest(BasePath+'/entrust_owner/get_biz?_=1387433922927',{},false,stockadj.initOwner);
	//是否整储位调整
	stockadj.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CON_ADJ_CELL_ADJ_FLAG',{},false,stockadj.initCellAdjFlag);
	//调整类型
	stockadj.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ADJ_TYPE',{},false,stockadj.initadjType);
	//品质
	stockadj.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',{},false,stockadj.getQualityData);
	//属性
	stockadj.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',{},false,stockadj.getItemTypeData);
	//品牌库
//	stockadj.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO&_=1387593750647',{},false,stockadj.initSysNo);
	//仓区
	stockadj.ajaxRequest(BasePath+'/cm_defware/get_biz',{"locno":stockadj.user.locno},false,stockadj.initWareNo);
	//初始化来源类型
	wms_city_common.comboboxLoadFilter(
			["sourceTypeCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_CONVERT_SOURCE_TYPE',
			{},
			stockadj.sourceType,
			null);
	//品牌
	var objs = [];
	objs.push({"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNoCondition]',$('#searchForm'))});
	wms_city_common.loadSysNo4Cascade(objs);
	
	//添加明细选择，品牌
	var objsItem = [];
	objsItem.push( {"sysNoObj":$('#sysNoId'),"brandNoObj":$('input[id=brandNoItem]',$('#itemform'))});
	wms_city_common.loadSysNo4Cascade(objsItem);
	//wms_city_common.loadSysNo('#sysNoId');
	
	//初始化季节、性别
	stockadj.initGenderAndSeason('#sysNoId');
	
	$('#dataGridLU_Dtl').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				stockadj.dataGridLUFooter.adjQty = data.footer[1].adjQty;
		   			}else{
		   				var rows = $('#dataGridLU_Dtl').datagrid('getFooterRows');
			   			rows[1]['adjQty'] = stockadj.dataGridLUFooter.adjQty;
			   			$('#dataGridLU_Dtl').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	
//3.加载明细
	/*$('#dataGridJG').datagrid({
		'onLoadSuccess':function(data){
			if(data.footer[1] != null){
				if(data.footer[1].isselectsum){
	   				stockadj.dataGridJGFooter = data.footer[1];
	   			}else{
	   				var rows = $('#dataGridJG').datagrid('getFooterRows');
		   			rows[1]= stockadj.dataGridJGFooter;
		   			$('#dataGridJG').datagrid('reloadFooter');
	   			}
			}
   		}
	});*/
	//初始化三级大类
	wms_city_common.cateForMultipleCascade(
			'cateOneItem',
			'cateTwoItem',
			'cateThreeItem',
			true
		);
	wms_city_common.comboboxLoadFilter(
			[ "containerTypeCondition", "conType"],
			null,
			null,
			null,
			true,
			[ true, true ],
			BasePath+ '/initCache/getLookupDtlsList.htm?lookupcode=CONTAINER_TYPE',
			{}, stockadj.containerType, null);
});
//B-物流箱；C-原装箱；P-栈板；R-笼车
stockadj.containerType = {};
stockadj.containerTypeFormatter = function(value, rowData, rowIndex){
	return stockadj.containerType[value];
};
/**
 * 在数据加载成功的时候触发。
 */
stockadj.onLoadSuccess = function(data){
	if(data.footer[1] != null){
		if(data.footer[1].isselectsum){
				stockadj.dataGridJGFooter = data.footer[1];
			}else{
				var rows = $('#dataGridJG').datagrid('getFooterRows');
   			rows[1]= stockadj.dataGridJGFooter;
   			$('#dataGridJG').datagrid('reloadFooter');
			}
	}
};
stockadj.initGenderAndSeason =  function(sysNoId){
	$(sysNoId).combobox({
		onChange: function(param){
			var obj = $('#'+this.id);
	    	var sysNo = obj.combobox('getValues');
	    	wms_city_common.initLookupBySysNo('GENDER',sysNo,'genderItem',true,false);
	    	wms_city_common.initLookupBySysNo('SEASON',sysNo,'seasonItem',true,false);
		}
	});
};
	
//将数组封装成一个map
stockadj.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

stockadj.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
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

//========================用户 信息========================
stockadj.user = {};
stockadj.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		stockadj.user = data;
		stockadj.locno = data.locno;
		stockadj.userid = data.userid;
		stockadj.loginName = data.loginName;
		stockadj.currentDate = data.currentDate19Str;
		
	});
};
/**初始化单据状态**/
stockadj.status={};
stockadj.formatterStatus = function(value){
	for(var i=0;i<stockadj.status.length;i++){
		if(value==stockadj.status[i].itemvalue){return stockadj.status[i].itemname;};
	}
};
stockadj.initStatus = function(data){
	$("#statusCondition").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:"auto"
	});
	stockadj.status=data;
};
/**初始化委托业主**/
stockadj.ownerNo = {};
stockadj.ownerNoFormatter = function(value, rowData, rowIndex){
	return stockadj.ownerNo[value];
};
stockadj.initOwner = function(data){
	$('#ownerid').combobox({
	     valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto"
	});
	$('#owneridupdate').combobox({
	     valueField:"ownerNo",
	     textField:"ownerName",
	     data:data,
	     panelHeight:"auto"
	});
	stockadj.ownerNo = stockadj.tansforOwner(data);
};
stockadj.tansforOwner = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
/**初始化单据调整类型**/
stockadj.adjType={};
stockadj.formatteradjType = function(value){
	for(var i=0;i<stockadj.adjType.length;i++){
		if(value==stockadj.adjType[i].itemvalue){return stockadj.adjType[i].itemname;};
	}
};
stockadj.initadjType = function(data){
	$("#adjtypeCondition").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:"auto"
	});
	$("#adjType").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:"auto"
	});
	$("#adjTypeupdate").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150,
	     onChange:function(newValue,oldValue){
	    	 if(newValue==1){
	    		 stockadj.getItemTypeData(stockadj.itemType);
	    	 }
	    	 if(newValue==0){
	    		 stockadj.getQualityData(stockadj.quality);
	    	 }
	     }
	});
	stockadj.adjType=data;
};
/**初始化是否整储位调整**/
stockadj.cellAdjFlag={};
stockadj.forcellAdjFlag = function(value){
	for(var i=0;i<stockadj.cellAdjFlag.length;i++){
		if(value==stockadj.cellAdjFlag[i].itemvalue){return stockadj.cellAdjFlag[i].itemname;};
	}
};
stockadj.initCellAdjFlag = function(data){
	$("#cellAdjFlagUpdate").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	$("#cellAdjFlag").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	stockadj.cellAdjFlag=data;
};
/**品质**/
stockadj.quality={};
stockadj.formatterQuality = function(value){
	for(var i=0;i<stockadj.quality.length;i++){
		if(value==stockadj.quality[i].itemvalue){return stockadj.quality[i].itemname;};
	}
};
stockadj.getQualityData = function(data){
	$("#ajdTypebeforeupdate").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	$("#adjTypeLateupdate").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	$("#ajdTypebefore").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	$("#adjTypeLate").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	stockadj.quality=data;
};
/**商品属性**/
stockadj.itemType={};
stockadj.formatterItemType = function(value){
	for(var i=0;i<stockadj.itemType.length;i++){
		if(value==stockadj.itemType[i].itemvalue){return stockadj.itemType[i].itemname;};
	}
};
stockadj.getItemTypeData = function(data){
	$("#ajdTypebeforeupdate").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	$("#adjTypeLateupdate").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	$("#ajdTypebefore").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	$("#adjTypeLate").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:data,
	     panelHeight:150
	});
	
	stockadj.itemType=data;
};
/**formatter调整结果**/
stockadj.formatterResult = function(value,row,index){
	 var s='';
     var y='->';
     var d='';
     	if(row.adjType==1){
     		for(var i=0;i<stockadj.itemType.length;i++){
     			if(row.sItemType==stockadj.itemType[i].itemvalue){
     				s=stockadj.itemType[i].itemname;
     			}
     		}
     		for(var i=0;i<stockadj.itemType.length;i++){
     			if(row.dItemType==stockadj.itemType[i].itemvalue){
     				d= stockadj.itemType[i].itemname;
     			}
     		}
     		return s+y+d;
     		}
     	if(row.adjType==0){
     		for(var i=0;i<stockadj.quality.length;i++){
     			if(row.sItemType==stockadj.quality[i].itemvalue){
     				s=stockadj.quality[i].itemname;
     			}
     		}
     		for(var i=0;i<stockadj.quality.length;i++){
     			if(row.dItemType==stockadj.quality[i].itemvalue){
 				d= stockadj.quality[i].itemname;
     			}
     		}
     			return s+y+d;
     	}
};
/**来源类型**/
stockadj.sourceType = {};
stockadj.formattersourceType = function(value, rowData, rowIndex){
	return stockadj.sourceType[value];
};
//==============================================================
//==============================================================
//=============================主表=================================
//打开窗口
stockadj.openWindow = function(windowId,opt){
	stockadj.closeWindow(windowId);
	$('#'+windowId).window({
		title:opt,
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$('#'+windowId).window('open');
};

//关闭窗口
stockadj.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};
//隐藏显示按钮
stockadj.showHideBtn = function(type){
	//是否整储位调整 
	var cellAdjFlag = $("#cellAdjFlagUpdate").combobox('getValue');
	
	if(type=="add") {
		//按钮
		$("#btn-add").show();
		$("#btn-modify").hide();
		$("#editDtl").hide();
		$("#toolBarTwoThree").hide();
		
		//
		$("#adjNoupdate").attr('readOnly',true);
		$("#cellAdjFlagUpdate").combobox('enable');
		$("#adjTypeupdate").combobox('enable');
		$("#ajdTypebeforeupdate").combobox('enable');
		$("#adjTypeLateupdate").combobox('enable');
		$("#owneridupdate").combobox('enable');
		//$("#adjDateupdate").datebox('enable');
		$("#remark").attr("disabled",false);
	} else if(type=="edit") {
		//按钮
		$("#btn-add").hide();
		$("#btn-modify").show();
		$("#editDtl").show();
		$("#toolBarTwoThree").show();
		
		//当为是整储位调整时，明细只能新增
		if(cellAdjFlag=='1'){
			$("#itemDelBtn").hide();
			$("#itemSaveBtn").hide();
			$("#itemAddOneBtn").hide();
			$("#downBtn").hide();
			$("#explortBtn").hide();
			$("#itemAddTwoBtn").hide();
			$("#itemAddThreeBtn").show();
		}else if(cellAdjFlag=='0'){
			$("#itemDelBtn").show();
			$("#itemSaveBtn").show();
			$("#itemAddOneBtn").show();
			$("#downBtn").show();
			$("#explortBtn").show();
			$("#itemAddTwoBtn").show();
			$("#itemAddThreeBtn").hide();
		}
		$("#editDtl").show();
		$("#adjNoupdate").attr('readOnly',true);
		$("#cellAdjFlagUpdate").combobox('disable');
		$("#adjTypeupdate").combobox('disable');
		$("#ajdTypebeforeupdate").combobox('disable');
		$("#adjTypeLateupdate").combobox('disable');
		$("#owneridupdate").combobox('disable');
		//$("#adjDateupdate").datebox('enable');
		$("#remark").attr("disabled",false);
	} else {
		//按钮
		$("#btn-add").hide();
		$("#btn-modify").hide();
		$("#editDtl").show();
		$("#toolBarTwoThree").hide();
		//
		$("#adjNoupdate").attr('readOnly',true);
		$("#adjTypeupdate").combobox('disable');
		$("#cellAdjFlagUpdate").combobox('disable');
		$("#ajdTypebeforeupdate").combobox('disable');
		$("#adjTypeLateupdate").combobox('disable');
		$("#owneridupdate").combobox('disable');
		//$("#adjDateupdate").datebox('disable');
		$("#remark").attr("disabled",true);
	}
};

/**打开新增界面**/
stockadj.showAdd = function(){
	$('#stockAdjDtlInfoAdd').form("clear");
	$("#dataGridLU_DtlForAdd").datagrid('loadData', {total: 0, rows: []});
	stockadj.showHideBtn("add");
	stockadj.openWindow('addWindow', '新增');
};
/**修改开始**/
stockadj.showModify = function(rowData){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if (checkedRows.length != 1) {
		alert('请选择一条记录！', 1);
		return;
	}
	if(checkedRows[0].status != 10){
		alert('单据已审核，不能修改！', 1);
		return;
	}
	$('#stockAdjDtlInfoAdd').form("clear");
	$("#dataGridLU_DtlForAdd").datagrid('loadData', {total: 0, rows: []});
	setMainMsgForUpdate(checkedRows[0]);
	stockadj.showHideBtn("edit");
	stockadj.openWindow('addWindow', '修改');
	
	var queryMxURL=BasePath+'/bill_stock_adj_dtl/getDetail.json?locNo='+stockadj.user.locno+
		'&adjNo='+checkedRows[0].adjNo+
		'&ownerNo='+checkedRows[0].ownerNo+
		'&adjType='+$("#adjTypeupdate").combobox('getValue')+
		'&sType='+$("#ajdTypebeforeupdate").combobox('getValue');
    //3.加载明细
    $("#dataGridLU_DtlForAdd").datagrid( 'options' ).url=queryMxURL;
    $("#dataGridLU_DtlForAdd").datagrid( 'load' );
};
/**修改时候赋值**/
function setMainMsgForUpdate(rowData){
	$('#stockAdjDtlInfoAdd').form("clear");
	$("#adjNoupdate").val(rowData.adjNo);
	$("#cellAdjFlagUpdate").combobox('setValue',rowData.cellAdjFlag);
	$("#adjTypeupdate").combobox('setValue',rowData.adjType);
	$("#ajdTypebeforeupdate").combobox('setValue',rowData.sItemType);
	$("#adjTypeLateupdate").combobox('setValue',rowData.dItemType);
	$("#owneridupdate").combobox('setValue',rowData.ownerNo);
	//$("#adjDateupdate").datebox('setValue',rowData.adjDate);
	$("#remarkupdate").val(rowData.remark);
}
/**双击显示详情**/
stockadj.showDetail = function(rowData){
	/**加载表头信息**/
	setMainMsg(rowData);
	$("#stockAdjDtlInfo").window('open').show();
	
	var queryMxURL=BasePath+'/bill_stock_adj_dtl/dtl_getDetail.json?locNo='+rowData.locno+
		'&adjNo='+rowData.adjNo+
		'&ownerNo='+rowData.ownerNo+
		'&adjType='+$("#adjType").combobox('getValue')+
		'&sType='+$("#ajdTypebefore").combobox('getValue');
	var tempObj = $('#dataGridLU_Dtl');
	tempObj.datagrid( 'options' ).url = queryMxURL;
	tempObj.datagrid('load');
};
/**查看详情时候设置表头明细信息**/
function setMainMsg(rowData){
	$('#stockAdjDtlInfo').form("clear");
	if(rowData.adjType==1){
		 stockadj.getItemTypeData(stockadj.itemType);
	} else if(rowData.adjType==0){
		 stockadj.getQualityData(stockadj.quality);
	}
	
	$("#adjNo").val(rowData.adjNo);
	$("#cellAdjFlag").combobox('setValue',rowData.cellAdjFlag);
	$("#adjType").combobox('setValue',rowData.adjType);
	$("#ajdTypebefore").combobox('setValue',rowData.sItemType);
	$("#adjTypeLate").combobox('setValue',rowData.dItemType);
	$("#ownerid").combobox('setValue',rowData.ownerNo);
	//$("#adjDate").datebox('setValue',rowData.adjDate);
	$("#remarkid").val(rowData.remark);
	
	$("#adjNo").attr('readOnly',true);
	$("#cellAdjFlag").combobox('disable');
	$("#adjType").combobox('disable');
	$("#ajdTypebefore").combobox('disable');
	$("#adjTypeLate").combobox('disable');
	$("#ownerid").combobox('disable');
	//$("#adjDate").datebox('disable');
	$("#remarkid").attr("disabled",true);
}


/**查询**/
stockadj.searchStockadj = function(){
	var startCreatetm =  $('#createtmBeginCondition').datebox('getValue');
	var endCreatetm =  $('#createtmEndCondition').datebox('getValue');
	if(!isStartEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
		return;   
	}   
		
		var startAudittm =  $('#audittmBeginCondition').datebox('getValue');
		var endAudittm =  $('#audittmEndCondition').datebox('getValue');
		if(!isStartEndDate(startAudittm,endAudittm)){    
			alert("审核日期开始日期不能大于结束日期");   
	       return;   
	   } 
	
	if($("#searchForm").form('validate')){
		var fromObjStr=convertArray($('#searchForm').serializeArray());
		var queryMxURL=BasePath+'/bill_stock_adj/adjlist.json?locNo='+stockadj.user.locno;
	    //3.加载明细
	    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
	    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
	    $( "#dataGridJG").datagrid( 'load' );
	}
	
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
/**清空查询form**/
stockadj.searchStockadjClear = function(){
	$('#searchForm').form("clear");
	$('#brandNoCondition').combobox("loadData",[]);
};
/**删除单据**/
stockadj.deleteadj = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if (checkedRows.length<0) {
		alert('请至少选择一条记录！', 1);
		return;
	}else{
		var isDel = true;
		var deliver = "";
		var keys = [];
		for(var i=0;i<checkedRows.length;i++){
			if(checkedRows[i].status != 10){
				deliver += checkedRows[i].adjNo + "\n";
				isDel = false;
			}else{
				keys.push(stockadj.user.locno+"-"+checkedRows[i].ownerNo+"-"+checkedRows[i].adjNo);
			}
		}
		if(!isDel) {
			alert("单据:" + deliver +"当前状态不允许删除，请重新选择！");
			return;
		} else {
			//2.绑定数据
		     var url = BasePath+'/bill_stock_adj/adjdelete';
			 var data={
					    "ids":keys.join(",")
			  };	 
			 //3. 删除
			 $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
				 if (r){
					 	wms_city_common.loading("show","正在删除......");
					 stockadj.ajaxRequest(url,data,true,function(result){
						 wms_city_common.loading();
						 if(result.flag=='success'){
							 alert('删除成功!');
						 }else{
							 //alert('删除失败,请联系管理员!',2);
							 alert(result.msg);
						 }
						 stockadj.searchStockadj();
					});  
				    }    
				});
			}
	}

};
//==============================================================
//==============================================================
/**保存调整库存主表信息**/
stockadj.saveStockAdjInfo = function(){
	if($("#stockAdjDtlInfoAdd").form('validate')){
		var ajdTypebeforeupdate = $("#ajdTypebeforeupdate").combobox('getValue');
		var adjTypeLateupdate =$("#adjTypeLateupdate").combobox('getValue');
		if(ajdTypebeforeupdate == adjTypeLateupdate){
			alert("调整后类型不能与调整前类型相同",2);
			return;
		}
//		var adjDate = $("#adjDateupdate").datebox('getValue');
//		if(adjDate != null && adjDate!="") {
//			if(!isStartEndDate(wms_city_common.getCurDate(),adjDate)){    
//				alert("调整日期不能小于当前日期");
//		        return;   
//		    }
//		} else {
//			alert('日期不能为空，请录入调整日期!', 1);
//			return;
//		}
		
		wms_city_common.loading("show","正在保存......");
		var url = BasePath+'/bill_stock_adj/adjadd';
		$('#stockAdjDtlInfoAdd').form('submit', {
				url: url,
				onSubmit: function(param){
					param.editor = stockadj.user.loginName;
					param.edittm = stockadj.user.currentDate19Str;
					param.locno = stockadj.user.locno;
				},
				success: function(r){
					wms_city_common.loading();
					r = stockadj.parseJsonStringToJsonObj(r);
					if(r && r.success == 'true'){
						$("#stockAdjDtlInfoAdd input[id=adjNoupdate]").val(r.adjNo);
						stockadj.showHideBtn("edit");
						var queryMxURL=BasePath+'/bill_stock_adj_dtl/dtl_getDetail.json?locNo='+stockadj.user.locno+
							'&adjNo='+$('#adjNoupdate').val()+
							'&ownerNo='+$('#owneridupdate').combobox('getValue')+
							'&adjType='+$("#adjTypeupdate").combobox('getValue')+
							'&sType='+$("#ajdTypebeforeupdate").combobox('getValue');
						var tempObj = $('#dataGridLU_DtlForAdd');
						tempObj.datagrid( 'options' ).url = queryMxURL;
						tempObj.datagrid('load');
						 alert(r.adjNo + '保存成功!');
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
	}
};
/**修改库存调整表头信息**/
stockadj.updateStockAdj = function(){
	if($("#stockAdjDtlInfoAdd").form('validate')){
		var ajdTypebeforeupdate = $("#ajdTypebeforeupdate").combobox('getValue');
		var adjTypeLateupdate =$("#adjTypeLateupdate").combobox('getValue');
		if(ajdTypebeforeupdate == adjTypeLateupdate){
			alert("调整后类型不能与调整前类型相同",2);
			return;
		}
//		var adjDate = $("#adjDateupdate").datebox('getValue');
//		if(adjDate != null && adjDate!="") {
//			if(!isStartEndDate(wms_city_common.getCurDate(),adjDate)){ 
//				alert("调整日期不能小于当前日期");   
//		        return;   
//		    }
//		} else {
//			alert('日期不能为空，请录入调整日期!', 1);
//			return;
//		}
		var ownerNo = $("#owneridupdate").combobox('getValue');
		wms_city_common.loading("show","正在修改......");
		var url = BasePath+'/bill_stock_adj/adjupdate';
		$('#stockAdjDtlInfoAdd').form('submit', {
				url: url,
				onSubmit: function(param){
					param.editor = stockadj.user.loginName;
					param.editorName = stockadj.user.username;
					param.edittm = stockadj.user.currentDate19Str;
					param.locno = stockadj.user.locno;
					param.ownerNo = ownerNo;
				},
				success: function(r){
					wms_city_common.loading();
					r = stockadj.parseJsonStringToJsonObj(r);
					if(r && r.success == 'true'){
						stockadj.showHideBtn("edit");
						alert(r.adjNo + '保存成功!');
						stockadj.searchStockadj();
						return;
					}else{
						 if(r.result < 1){
							 alert("单据"+r.adjNo+"已删除或状态已改变，不能进行 “修改/删除/审核”操作");
						 }else{
							 alert('保存失败!');
						 }
					}
			    },
				error:function(){
					wms_city_common.loading();
					alert('保存失败,请联系管理员!',2);
				}
		   });
	}
};
stockadj.parseJsonStringToJsonObj = function(jsonStr){
	 return eval('(' + jsonStr + ')'); 
};
/**关闭新增窗口**/
stockadj.closeAddWindow=function(){
	$("#stockAdjDtlInfoAdd").form('clear');
	$("#addWindow").window('close');
	$("#stockadj").hide();
	$(".tooltip").remove();
	stockadj.searchStockadj();
};
/**打开商品选择列表页面**/
stockadj.showItemWindow = function(){
	var cellAdjFlag = $("#cellAdjFlagUpdate").combobox('getValue');
	//调整类型 0→品质转换  1→属性转换
	var adjType = $("#adjTypeupdate").combobox('getValue');	
	//调整前类型
	var sItemType =$("#ajdTypebeforeupdate").combobox('getValue');
	if(cellAdjFlag=='0'){
		$("#item").window({
			onBeforeClose:function(){
				$("#itemdg").datagrid('loadData', { total: 0, rows: []});
				$("#itemform").form('clear');
			}
		});			
		
		$("#itemTypeItem").combobox({
		     valueField:"itemvalue",
		     textField:"itemnamedetail",
		     data:stockadj.itemType,
		     panelHeight:150
		});
		
		$("#qualityItem").combobox({
		     valueField:"itemvalue",
		     textField:"itemnamedetail",
		     data:stockadj.quality,
		     panelHeight:150
		});
		//品质转换
		if(adjType=='0'){
			$("#qualityItem").combobox('setValue',sItemType);
			$("#qualityItem").combo('disable');
			$("#itemTypeItem").combo('enable');
		}else{//属性转换			
			$("#itemTypeItem").combobox('setValue',sItemType);
			$("#itemTypeItem").combo('disable');
			$("#qualityItem").combo('enable');
		}
		
		$("#item").show().window('open');
	}else if(cellAdjFlag=='1'){
		$("#cell_select_dialog").window({
			onBeforeClose:function(){
				$("#cell_select_datagrid").datagrid('loadData', { total: 0, rows: []});
				$("#selectCellSearchForm").form('clear');
			}
		});	
		$("#cell_select_dialog").show().window('open');
	}
};
/**打开容器选择列表页面**/
stockadj.showConListWindow = function(){
	//调整类型 0→品质转换  1→属性转换
	var adjType = $("#adjTypeupdate").combobox('getValue');	
	$("#conItemTypeItem").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:stockadj.itemType,
	     panelHeight:150
	});
	
	$("#conQualityItem").combobox({
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     data:stockadj.quality,
	     panelHeight:150
	});
	$("#con_select_dialog").window({
		onBeforeClose:function(){
			$("#con_select_datagrid").datagrid('loadData', { total: 0, rows: []});
			$("#selectConSearchForm").form('clear');
		}
	});
	$("#con_select_dialog").show().window('open');
	//调整前类型
	var sItemType =$("#ajdTypebeforeupdate").combobox('getValue');
	//品质转换
	if(adjType=='0'){
		$("#conQualityItem").combobox('setValue',sItemType);
		$("#conQualityItem").combo('disable');
		$("#conItemTypeItem").combo('enable');
	}else{//属性转换			
		$("#conItemTypeItem").combobox('setValue',sItemType);
		$("#conItemTypeItem").combo('disable');
		$("#conQualityItem").combo('enable');
	}
};
//==============================================================
//==============================================================
/**弹出选择商品窗口**/
stockadj.showItem = function(){
	var adjType = $("#adjTypeupdate").combobox('getValue');
	var sItemType =$("#ajdTypebeforeupdate").combobox('getValue');
	var ownerNo = $("#owneridupdate").combobox('getValue');
	var adjNo=$("#adjNoupdate").val();
	var queryMxURL=BasePath+'/bill_stock_adj_dtl/getItem?locNo='+stockadj.user.locno+
		'&adjType='+adjType+
		'&sItemType='+sItemType+
		'&ownerNo='+ownerNo+
		'&adjNo='+adjNo;
	
	var fromObjStr=convertArray($('#itemform').serializeArray());
	var reqParam = eval("(" +fromObjStr+ ")");
	stockadj.loadGridDataUtil('itemdg', queryMxURL, reqParam);
};
//加载Grid数据Utils
stockadj.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

/**清空商品选择查询条件**/
stockadj.clearItem = function(){
	$("#itemform").form("clear");
	$('#seasonItem').combobox("loadData",[]);
	$('#genderItem').combobox("loadData",[]);
	var adjType = $("#adjTypeupdate").combobox('getValue');
	//调整前类型
	var sItemType =$("#ajdTypebeforeupdate").combobox('getValue');
	//品质转换
	if(adjType=='0'){
		$("#qualityItem").combobox('setValue',sItemType);
	}else{//属性转换			
		$("#itemTypeItem").combobox('setValue',sItemType);
	}
};
/**清空容器选择查询条件**/
stockadj.clearConForm = function(){
	$("#selectConSearchForm").form("clear");
	
	var adjType = $("#adjTypeupdate").combobox('getValue');
	//调整前类型
	var sItemType =$("#ajdTypebeforeupdate").combobox('getValue');
	//品质转换
	if(adjType=='0'){
		$("#conQualityItem").combobox('setValue',sItemType);
	}else{//属性转换			
		$("#conItemTypeItem").combobox('setValue',sItemType);
	}
};
/**取消商品选择**/
stockadj.cancleItem = function(){
	stockadj.clearItem();
	$("#item").window('close');
	$("#itemdg").datagrid('loadData', { total: 0, rows: []});
};
/**审核**/
stockadj.examineAdj = function(){
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
				deliver += checkedRows[i].adjNo + "\n";
				isDel = false;
			} else {
				keys.push(stockadj.user.locno+"-"+checkedRows[i].ownerNo+"-"+checkedRows[i].adjNo);
			}
		}
		if(!isDel) {
			alert("单据:" + deliver +"当前状态不允许审核，请重新选择！");
			return;
		}
		for(var i=0;i<checkedRows.length;i++){
			var adjNo = checkedRows[i].adjNo;
			var ownerNo = checkedRows[i].ownerNo;
			
			var untreadDtlUrl = BasePath+'/bill_stock_adj_dtl/getDetail.json?locNo='+stockadj.user.locno
			+ '&adjNo='+ adjNo + '&ownerNo='+ownerNo;
			var existDtl = false;
			stockadj.ajaxRequest(untreadDtlUrl,{},false,function(result){
    			 if(result.total>0){
    				 existDtl = true;
    			 }
    		});
			if(!existDtl){
				alert("单据["+adjNo+"]不存在明细,不允许审核!",2);
				return;
			}
		}
		$.messager.confirm("确认","审核后数据将无法修改，您确定并继续操作吗？", function (r){  
	        if (r) {
	        	wms_city_common.loading("show","正在审核......");
	    		var url = BasePath+'/bill_stock_adj/examineAdj';
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
							stockadj.searchStockadj();
						}
					});
	        }
		});
	}

//	if (checkedRows.length != 1) {
//		alert('请选择一条记录！', 1);
//		return;
//	}
//	
//	if(checkedRows[0].status == 13){
//		alert('单据已审核！', 1);
//		return;
//	}
//	var existDtlOk = true;
//	for(var i=0;i<checkedRows.length;i++){
//		var ownerNo = checkedRows[i].ownerNo;
//		var adjNo = checkedRows[i].adjNo;
//		//var adjDate = checkedRows[i].adjDate;
//		var untreadDtlUrl = BasePath+'/bill_stock_adj_dtl/getDetail.json?locNo='+stockadj.user.locno
//			+ '&adjNo='+ adjNo + '&ownerNo='+ownerNo;
//		var existDtl = false;
//		stockadj.ajaxRequest(untreadDtlUrl,{},false,function(result){
//			 if(result.total>0){
//				 existDtl = true;
//			 }
//		});
//		if(!existDtl){
//			alert("单据["+adjNo+"]不存在明细,不允许审核!",2);
//			existDtlOk = false;
//			return;
//		}
//		
//	}
//	if(existDtlOk) {
//		var url = BasePath + "/bill_stock_adj/examineAdj?locNo="+stockadj.user.locno+"&adjNo="+checkedRows[0].adjNo+"&ownerNo="+checkedRows[0].ownerNo;
//		$.messager.confirm("确认","审核后将无法修改，您确定并继续操作吗？", function (r){
//			if(r){		
//				wms_city_common.loading("show","正在审核......");
//				$.ajax({
//					url : url,
//					type : "post",
//					dataType : 'json',
//					cache : false,
//					success : function(r) {
//						wms_city_common.loading();
//						if(r.success){
//							stockadj.searchStockadj();
//							alert(r.msg);
//						}else{
//							alert(r.msg);
//						}
//					},
//					error : function() {
//						wms_city_common.loading();
//						alert('审核失败，请联系管理员', 2);
//					}
//				});
//			}
//		});
//	}
};
//==============================================================
//==============================================================

/**获取单前用户信息**/
//stockadj.getCurrentUser = function() {
//	var currentUser = null;
//	$.ajax({
//		type : 'POST',
//		url : BasePath + '/initCache/getCurrentUser',
//		data : {},
//		cache : true,
//		async : false, // 一定要
//		success : function(resultData) {
//			currentUser = resultData;
//		}
//	});
//	return currentUser;
//};
/**选择商品**/
stockadj.checkItem = function(){
	var checkedRows = $("#itemdg").datagrid("getChecked");// 获取所有勾选checkbox的行
	if (checkedRows.length == 0) {
		alert('请选择商品！', 1);
		return;
	}
	
	$.messager.confirm("确认","您确定要添加这"+checkedRows.length+"条商品信息吗？", function (r){  
		if (r) {
			var fromObj=$('#stockAdjDtlInfoAdd');
			var adjNo = $("input[id=adjNoupdate]",fromObj).val();
			var ownerNo = $("input[id=owneridupdate]",fromObj).combobox('getValue');
			var adjType = $("input[id=adjTypeupdate]",fromObj).combobox('getValue');
			var sItemType = $("input[id=ajdTypebeforeupdate]",fromObj).combobox('getValue');
			var dItemType = $("input[id=adjTypeLateupdate]",fromObj).combobox('getValue');
			 //var adjDate = $("input[id=adjDateupdate]",fromObj).val();
			var remark = $("input[id=remarkupdate]",fromObj).val();
			  
			var effectRow = {
				inserted : JSON.stringify(checkedRows),
				"locno":stockadj.user.locno,
				"adjNo":adjNo,
				"ownerNo":ownerNo,
				"adjType":adjType,
				"sItemType":sItemType,
				"dItemType":dItemType,
				//"adjDate":adjDate,
				"remark":remark,
				"edittm":stockadj.user.currentDate19Str,
				"editor":stockadj.user.loginName,
				"editorName":stockadj.user.username
			};
			wms_city_common.loading("show","正在保存......");
			
			$.post(BasePath + '/bill_stock_adj_dtl/addDtl', effectRow, function(result) {
				wms_city_common.loading();
				if(result.flag=='true'){
					alert('保存成功!', 1);
					$('#item').window('close').hide();

					var queryMxURL=BasePath+'/bill_stock_adj_dtl/getDetail.json?locNo='+stockadj.user.locno+
									'&adjNo='+adjNo+
									'&ownerNo='+ownerNo+
									'&adjType='+$("#adjTypeupdate").combobox('getValue')+
									'&sType='+$("#ajdTypebeforeupdate").combobox('getValue');
									
					$("#dataGridLU_DtlForAdd").datagrid( 'options' ).url=queryMxURL;
					$("#dataGridLU_DtlForAdd").datagrid( 'load' );		
					
				}else if(result.flag=='warn'){
			      	alert(result.msg,1);
			      	return;
			      }else{
			      	alert(result.flag,2);
			      	return;
			      }
			}, "JSON").error(function() {
				wms_city_common.loading();
				alert('保存失败!', 1);
			});
			
		}
	});
};
/**结束行编辑**/
stockadj.insertRowAtEnd = function(gid, rowData) {
	
	var tempObj = $('#' + gid);
	if (rowData) {
		tempObj.datagrid('appendRow', rowData);
	} else {
		tempObj.datagrid('appendRow', {});
	}
	var tempIndex = tempObj.datagrid('getRows').length - 1;
	var fromObj=$('#stockAdjDtlInfoAdd');
	var adjType = $("input[id=adjTypeupdate]",fromObj).combobox('getValue');
	if(adjType == 1) {
	} else {
		tempObj.datagrid('beginEdit', tempIndex);
	}
	
};
/**删除选择的行**/
stockadj.removeBySelected = function(gid) {
	var tempObj = $('#' + gid);
	var rowObj = tempObj.datagrid('getSelected');
	if (rowObj) {
		var conNo=rowObj.labelNo;
		if(conNo){
			$.messager.confirm("确认","该商品是容器内商品，保存后将会删除整箱或整板商品，是否确认？", function (r){
				if (r) {
					var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
					tempObj.datagrid('deleteRow', rowIndex);
				}
			});
		}else{
				var rowIndex = tempObj.datagrid('getRowIndex', rowObj);
				tempObj.datagrid('deleteRow', rowIndex);
		}
	}
};
/**结束编辑**/
stockadj.endEdit = function(gid) {
	var tempObj = $('#' + gid);
	var rowArr = tempObj.datagrid('getRows');
	for ( var i = 0; i < rowArr.length; i++) {
		if (tempObj.datagrid('validateRow', i)) {
			tempObj.datagrid('endEdit', i);
		} else {
			return false;
		}
	}
	return true;
};

stockadj.onClickRowDtl = function(rowIndex, rowData){
	var curObj = $("#dataGridLU_DtlForAdd");
	curObj.datagrid('beginEdit', rowIndex);
	var ed = curObj.datagrid('getEditor', {index:rowIndex,field:'adjQty'});
  
	var fromObj=$('#stockAdjDtlInfoAdd');
	var adjType = $("input[id=adjTypeupdate]",fromObj).combobox('getValue');
	var cellAdjFlag = $("#cellAdjFlagUpdate").combobox('getValue');
	if(cellAdjFlag=='1') {
		$(ed.target).attr("disabled",true);
	} else {
		$(ed.target).attr("disabled",false);
	}
	var labelNo =rowData.labelNo;
	if(labelNo && labelNo!='N'){
		$(ed.target).attr("disabled",true);
	}
};

/**保存明细信息**/
stockadj.saveStockAdjDtl = function(){
//	var flag = true;
//	var cellIdFlag = true;
//	var str='';
	var tempFlag = stockadj.endEdit('dataGridLU_DtlForAdd');
	if (!tempFlag) {
		alert('数据验证没有通过!', 1);
		return;
	}
	var tempObj = $('#dataGridLU_DtlForAdd');
	var chgSize = tempObj.datagrid('getChanges').length;
	if (chgSize < 1) {
		alert('没有数据改动!', 1);
		return;
	}
	
	
	var inserted = tempObj.datagrid('getChanges', "inserted");
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
	var deleted = tempObj.datagrid('getChanges', "deleted");
	var updated = tempObj.datagrid('getChanges', "updated");
	
	var fromObj=$('#stockAdjDtlInfoAdd');
	var adjNo = $("input[id=adjNoupdate]",fromObj).val();
	var ownerNo = $("input[id=owneridupdate]",fromObj).combobox('getValue');
	var adjType = $("input[id=adjTypeupdate]",fromObj).combobox('getValue');
	var sItemType = $("input[id=ajdTypebeforeupdate]",fromObj).combobox('getValue');
	var dItemType = $("input[id=adjTypeLateupdate]",fromObj).combobox('getValue');
	 //var adjDate = $("input[id=adjDateupdate]",fromObj).val();
	var remark = $("input[id=remarkupdate]",fromObj).val();
	  
	var effectRow = {
		inserted : JSON.stringify(inserted),
		deleted : JSON.stringify(deleted),
		updated : JSON.stringify(updated),
		"locno":stockadj.user.locno,
		"adjNo":adjNo,
		"ownerNo":ownerNo,
		"adjType":adjType,
		"sItemType":sItemType,
		"dItemType":dItemType,
		//"adjDate":adjDate,
		"remark":remark,
		"edittm":stockadj.user.currentDate19Str,
		"editor":stockadj.user.loginName,
		"editorName":stockadj.user.username
	};
	wms_city_common.loading("show","正在保存......");
	
	$.post(BasePath + '/bill_stock_adj_dtl/addDtl', effectRow, function(result) {
		wms_city_common.loading();
		if(result.flag=='true'){
			alert('保存成功!', 1);
			tempObj.datagrid('acceptChanges');
			stockadj.closeAddWindow();
		}else if(result.flag=='warn'){
	      	alert(result.msg,1);
	      	return;
	      }else{
	      	alert(result.flag,2);
	      	return;
	      }
	}, "JSON").error(function() {
		wms_city_common.loading();
		alert('保存失败!', 1);
	});
};

/**添加明细储位信息查询**/
stockadj.searchCell = function(){
	//调整类型 0:品质 1:属性
	var adjType = $("#adjTypeupdate").combobox('getValue');
	var ajdTypebefore = $("#ajdTypebeforeupdate").combobox('getValue');
	var itemType='';
	var quality='';
	
	if(adjType=='1'){
		itemType=ajdTypebefore;
	}else{
		quality=ajdTypebefore;
	}
	
	var fromObjStr=convertArray($('#selectCellSearchForm').serializeArray());
	var queryMxURL=BasePath+'/cm_defcell/findCmDefcell4Adj.json?locno='+stockadj.user.locno+'&itemType='+itemType+'&quality='+quality;
	var reqParam = eval("(" +fromObjStr+ ")");
	stockadj.loadGridDataUtil('cell_select_datagrid', queryMxURL, reqParam);
	
};

/**容器查询**/
stockadj.searchCon = function(){
	//调整类型 0:品质 1:属性
	var adjType = $("#adjTypeupdate").combobox('getValue');
	var ajdTypebefore = $("#ajdTypebeforeupdate").combobox('getValue');
	
	var fromObj=$('#stockAdjDtlInfoAdd');
	var adjNo = $("input[id=adjNoupdate]",fromObj).val();
	var itemType='';
	var quality='';
	if(adjType=='1'){
		itemType=ajdTypebefore;
		quality=$("#conQualityItem").combobox('getValue');
	}else{
		quality=ajdTypebefore;
		itemType=$("#conItemTypeItem").combobox('getValue');
	}
	var fromObjStr=convertArray($('#selectConSearchForm').serializeArray());
	var queryMxURL=BasePath+'/acc_con/list.json?panFlag=true&bmStatus=0&status=2&locno='+stockadj.user.locno+'&itemType='+itemType+'&quality='+quality+'&adjNo='+adjNo;
	var reqParam = eval("(" +fromObjStr+ ")");
	stockadj.loadGridDataUtil('con_select_datagrid', queryMxURL, reqParam);
	
};

//仓区下拉框
stockadj.initWareNo = function(data){
	$('#wareNoItem').combobox({
	    data:data,
	    valueField:'wareNo',    
	    textField:'valueAndText',
	    panelHeight:150,
	    onSelect:stockadj.selectWareNo,
	    loadFilter:function(data){
			if(data){
	       		 for(var i = 0;i<data.length;i++){
	       			 var tempData = data[i];
	       			 tempData.valueAndText = tempData.wareNo+'→'+tempData.wareName;
	       		 }
	 		}
			return data;
	   }
	});
	if(data && data[0]){
		$('#wareNoItem').combobox("select",data[0].wareNo);  
	}
};

stockadj.selectWareNo = function(){
	var wareNo = $("#wareNoItem").combobox("getValue");
	stockadj.ajaxRequest(BasePath+'/cm_defarea/get_biz',{"locno":stockadj.user.locno,"wareNo":wareNo},true,stockadj.initAreaNo);
};

//库区下拉框
stockadj.initAreaNo = function(data){
	$('#areaNoItem').combobox({
	    data:data,
	    valueField:'areaNo',    
	    textField:'areaName',
	    panelHeight:150,
	    onSelect:stockadj.selectAreaNo
	});
	if(data && data[0]){
		$('#areaNoItem').combobox("select",data[0].areaNo);
	}
};

stockadj.selectAreaNo = function(){
	var wareNo = $("#wareNoItem").combobox("getValue");
	var areaNo = $("#areaNoItem").combobox("getValue");
	stockadj.ajaxRequest(BasePath+'/cm_defstock/get_biz',{"locno":stockadj.user.locno,"wareNo":wareNo,"areaNo":areaNo},true,stockadj.initStockNo);
};
stockadj.initStockNo = function(data){
	$('#stockNoItem').combobox({
	    data:data,
	    valueField:'stockNo',    
	    textField:'stockNo',
	    panelHeight:150,
	    onSelect:stockadj.selectStockNo
	});
	if(data && data[0]){
		$('#stockNoItem').combobox("select",data[0].stockNo);
	}
};

stockadj.clearForm = function(id){
	$('#'+id).form("clear");
};

//根据储位添加明细
stockadj.cellSelectOK = function(){
	var checkedRows = $("#cell_select_datagrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要添加的储位!',1);
		return;
	}
	var cellNos = [];
	var ownerNo=$("#owneridupdate").combobox("getValue");
	var locNo=stockadj.user.locno;
	var adjNo=$("#adjNoupdate").val();
	
	var queryMxURL=BasePath+'/bill_stock_adj_dtl/getDetail.json?locNo='+stockadj.user.locno+
					'&adjNo='+adjNo+
					'&ownerNo='+ownerNo+
					'&adjType='+$("#adjTypeupdate").combobox('getValue')+
					'&sType='+$("#ajdTypebeforeupdate").combobox('getValue');
	
	$.each(checkedRows, function(index, item){
		cellNos.push("cellNos="+item.cellNo);
	});   
	$.messager.confirm("确认","是否要添加所选储位下的商品？（确定后储位下商品将直接保存）", function (r){  
        if (r) {
            var url = BasePath+'/bill_stock_adj_dtl/addDtlByCell?ownerNo='+ownerNo+'&adjNo='+adjNo+'&locNo='+locNo+'&'+cellNos.join('&');
            wms_city_common.loading("show","正在处理数据......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  cache: true,
				  success: function(data){
					  wms_city_common.loading();
					  if(data.result=='true'){
						$("#dataGridLU_DtlForAdd").datagrid( 'options' ).url=queryMxURL;
						$("#dataGridLU_DtlForAdd").datagrid( 'load' );						    
						$('#cell_select_dialog').window('close');						  
					  }else{
						alert(data.msg,1);
					  }
				  }
			});
        }  
    });  
};
//根据容器添加明细
stockadj.conSelectOK = function(){
	var checkedRows = $("#con_select_datagrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择容器!',1);
		return;
	}
	var conNos = [];
	var ownerNo=$("#owneridupdate").combobox("getValue");
	var locNo=stockadj.user.locno;
	var adjNo=$("#adjNoupdate").val();
	
	var queryMxURL=BasePath+'/bill_stock_adj_dtl/getDetail.json?locNo='+stockadj.user.locno+
					'&adjNo='+adjNo+
					'&ownerNo='+ownerNo+
					'&adjType='+$("#adjTypeupdate").combobox('getValue')+
					'&sType='+$("#ajdTypebeforeupdate").combobox('getValue');
	
	$.each(checkedRows, function(index, item){
		conNos.push("conNos="+item.conNo+":"+item.conType);
	});
	$.messager.confirm("确认","是否要添加所选容器里的商品？（确定后容器里商品将直接保存）", function (r){  
        if (r) {
            var url = BasePath+'/bill_stock_adj_dtl/addDtlByConNo?ownerNo='+ownerNo+'&adjNo='+adjNo+'&locNo='+locNo+'&'+conNos.join('&');
            wms_city_common.loading("show","正在处理数据......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  cache: true,
				  success: function(data){	
					  wms_city_common.loading();
					  if(data.result=='true'){
						$("#dataGridLU_DtlForAdd").datagrid( 'options' ).url=queryMxURL;
						$("#dataGridLU_DtlForAdd").datagrid( 'load' );						    
						$('#con_select_dialog').window('close');						  
					  }else{
						alert(data.msg,1);
					  }
				  }
			});
        }  
    });  
};
//下载模板
stockadj.downloadTemp = function (){
	window.open(BasePath + "/bill_stock_adj_dtl/downloadTemple");
}
//导入 
stockadj.showImport = function(formId){
	var fromObj = $("#"+formId);
	var adjNo = $('input[id=adjNoupdate]',fromObj).val();
	var ownerNo = $('input[id=owneridupdate]',fromObj).combobox('getValue');
	$("#iframe").attr("src",BasePath + "/bill_stock_adj_dtl/iframe?adjNo="+adjNo+"&ownerNo="+ownerNo+"&v="+new Date());
	$("#showImportDialog").window('open'); 
}
stockadj.importSuccess = function(){
	stockadj.closeWindow("showImportDialog");
	var queryMxURL=BasePath+'/bill_stock_adj_dtl/dtl_getDetail.json?locNo='+stockadj.user.locno+
		'&adjNo='+$('#adjNoupdate').val()+
		'&ownerNo='+$('#owneridupdate').combobox('getValue')+
		'&adjType='+$("#adjTypeupdate").combobox('getValue')+
		'&sType='+$("#ajdTypebeforeupdate").combobox('getValue');
	var tempObj = $('#dataGridLU_DtlForAdd');
	tempObj.datagrid( 'options' ).url = queryMxURL;
	tempObj.datagrid('load');
	
	alert("导入成功!");
};
stockadj.loading = function(type,msg){
	wms_city_common.loading(type,msg);
};
stockadj.exportDtl = function(){
	var adjNo = $("#adjNo").val();
	exportExcelBaseInfo('dataGridLU_Dtl','/bill_stock_adj_dtl/do_export.htm?adjNo='+adjNo,'库存调整明细('+adjNo+')');
};
stockadj.printDetail = function(){
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	wms_city_common.loading("show","正在加载......");
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].locno+"|"+rows[i].adjNo+"|"+rows[i].ownerNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_stock_adj_dtl/printDetail',
        data: {
        	keys:keys.join(",")
        },
        success: function(data){
        	wms_city_common.loading();
        	if(data.result=="success"){
        		print4SizeHorizontal(data);
        	}else{
        		if(data.result == "no dtl"){
        			alert(data.msg);
        		}else{
        			alert(data.msg,2);
        		}
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
	for(var idx=0;idx<data.pages.length;idx++){
		LODOP.NewPageA();
		var headHtml = createHeadHtml(data.pages[idx]);
		var bodyHtml = createBodyHtml(data.pages[idx]);
		//设置表格内容
		LODOP.ADD_PRINT_TABLE(120,0,"100%",380,strStyle+bodyHtml);
		LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
		//设置表格头部
		LODOP.ADD_PRINT_HTM(0,10,"100%",120,headHtml);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		//设置条码
		LODOP.ADD_PRINT_BARCODE(30,10,250,40,"128A",data.pages[idx].adjNo);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	}
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
}
function createHeadHtml(page){
	var html = "<table style='width:100%;font-size:15px'>";
	html += "<tr><td style='text-align:center;font-size:30px;height:55px;' colspan='4'>库存调整单</td></tr>";
	
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+page.adjNo+"</td><td>调整属性："+page.adjType+"</td><td>调整结果："+page.adjResult+"</td></tr>";
	html += "<tr><td>备注："+page.remark+"</td><td>发货方："+stockadj.user.locname+"</td><td>总合计："+page.total+"</td></tr>";
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
	html += "<tr style='background-color: #fff;'><td rowspan='"+rowspan+"'>品牌</td>";
	html += "<td rowspan='"+rowspan+"'>商品编码</td><td rowspan='"+rowspan+"'>品质</td>";
	html += "<td rowspan='"+rowspan+"'>商品属性</td><td rowspan='"+rowspan+"'>商品名称</td>";
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
		html += "<td>"+row.sysNo+"</td>";
		html += "<td>"+row.itemNo+"</td>";
		html += "<td>"+row.qualityStr+"</td>";
		html += "<td>"+row.itemTypeStr+"</td>";
		html += "<td>"+row.itemName+"</td>";
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
	html = html + "<tfoot><tr><th tdata='pageNO' format = '#' colspan='"+sizeColNum+5+"' style='text-align:center;font-size:15px''>第#页</th></tr></tfoot>";
	html += "</table>";
	return html;
};