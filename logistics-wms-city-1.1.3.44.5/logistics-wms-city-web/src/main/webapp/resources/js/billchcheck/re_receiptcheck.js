var re_receiptcheck = {};
re_receiptcheck.locno;
//状态
re_receiptcheck.searchDirectParams ={};
//页面加载
$(document).ready(function(){
	//创建日期初始为前两天
	$("#createtm_start").datebox('setValue',getDateStr(-2));
	re_receiptcheck.initLocData();
	var objs2 = [];
	objs2.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#re_receiptcheckForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs2);
	//初始化状态
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_BILL_CH_CHECK_STATUS',
			{},
			re_receiptcheck.statusData,
			null);
	//初始化员工
	wms_city_common.comboboxLoadFilter(
			["recheckWorker"],
			'workerNo',
			'workerName',
			'valueAndText',
			false,
			[true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
	$('#reCheckDataGridDetail').datagrid(
		{
			'onLoadSuccess':function(){
	   			var curObj = $('#reCheckDataGridDetail');
	   			var rows = curObj.datagrid("getRows");
	   			var length = rows.length;
	   			if(length==0){
	   				return;
	   			}
	   			//curObj.datagrid('beginEdit', 0);
	   		}
		});
	$('#reCheckDataGridDetail_view').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer!=null){
			   			if(data.footer[1].isselectsum){
			   				re_receiptcheck.itemQty = data.footer[1].itemQty;
			   				re_receiptcheck.recheckQty = data.footer[1].recheckQty;
			   				re_receiptcheck.diffQty = data.footer[1].diffQty;
			   			}else{
			   				var rows = $('#reCheckDataGridDetail_view').datagrid('getFooterRows');
				   			rows[1]['itemQty'] = re_receiptcheck.itemQty;
				   			rows[1]['recheckQty'] = re_receiptcheck.recheckQty;
				   			rows[1]['diffQty'] = re_receiptcheck.diffQty;
				   			$('#reCheckDataGridDetail_view').datagrid('reloadFooter');
			   			}
					}else{
						var rows = $('#reCheckDataGridDetail_view').datagrid('getFooterRows');
						if(rows!=null){
							rows[0]['itemQty'] = "";
				   			rows[0]['recheckQty'] = "";
				   			rows[0]['diffQty'] = "";
				   			$('#reCheckDataGridDetail_view').datagrid('reloadFooter');
						}
					}
		   		}
			}
		);
});
re_receiptcheck.mainSumFoot = {};
re_receiptcheck.onLoadSuccess = function(data){
	if(data.footer[1] != null){
		re_receiptcheck.mainSumFoot = data.footer[1];
		}else{
			data.footer[1] = re_receiptcheck.mainSumFoot;
			$('#reCheckDataGrid').datagrid('reloadFooter');
		}
}

//状态信息
re_receiptcheck.statusData = {};
re_receiptcheck.statusFormatter = function(value,rowData,rowIndex){
	return re_receiptcheck.statusData[value];
};
//仓库
re_receiptcheck.initLocData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			re_receiptcheck.locno = data.locno;
		}
	});
};

re_receiptcheck.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};


//格式化
re_receiptcheck.planTypeFormatter = function(value,rowData,rowIndex){
	return re_receiptcheck.planTypeData[value];
};


re_receiptcheck.difQtyFormatter = function(value,rowData,rowIndex){
	return rowData.checkQty-rowData.itemQty;
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

re_receiptcheck.searchClear = function(){
	$('#re_receiptcheckForm').form("clear");
};

re_receiptcheck.searchItemClear = function(){
	$('#itemSearchForm').form("clear");
	//$('#cellNo').combo("clear");
};

re_receiptcheck.searchArea = function(){
	var createtm_start =  $('#createtm_start').datebox('getValue');
	var createtm_end =  $('#createtm_end').datebox('getValue');
	var planDateStart =  $('#startPlanDateCondition').datebox('getValue');
	var planDatemEnd =  $('#endPlanDateCondition').datebox('getValue');
	if(!isStartEndDate(createtm_start,createtm_end)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	if(!isStartEndDate(planDateStart,planDatemEnd)){    
		alert("盘点日期开始日期不能大于结束日期");   
		return;   
	} 
	var fromObjStr=convertArray($('#re_receiptcheckForm').serializeArray());
	var queryMxURL = BasePath+'/bill_ch_recheck_dtl/selectReCheck?locno='+re_receiptcheck.locno;
	var obj = "reCheckDataGrid";
    $( "#"+obj).datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#"+obj).datagrid( 'options' ).url=queryMxURL;
    $( "#"+obj).datagrid( 'load' );
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
};

re_receiptcheck.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};


re_receiptcheck.difQtyFormatter = function(value,rowData,rowIndex){
	return rowData.recheckQty-rowData.itemQty;
};


//初盘发单明细
re_receiptcheck.checkDtl = function(rowData,type){
	if(type=="edit"){
		$("#edit_add").show();
		$("#view").hide();
		$("#save_main").show();
		//$("#checkDtlBtnArea").show();
		$("#checkDtlBtnArea").css({"height":"auto","padding":"auto","border":"0px"});
		var checkedRows = $("#reCheckDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
		if(checkedRows.length < 1){
			alert('请选择要修改的记录!',1);
			return;
		}else if(checkedRows.length>1){
			alert('只能选择一条记录!',1);
			return;
		}
		if(checkedRows[0].status!='10'){
			alert("非建单状态的单据不能修改",1);
			return;
		}
		rowData = checkedRows[0];
		deleteAllGridCommon("reCheckDataGridDetail");
		$('#reCheckDataGridDetail').datagrid(
		{
			'url':BasePath+'/bill_ch_recheck_dtl/dtl_list.json?locno='+rowData.locno+'&itemNoIsNotNull=Y&isNullBox=N&recheckNo='+rowData.recheckNo
		});
		
		deleteAllGridCommon("reCheckDataGridBoxDetail");
		$('#reCheckDataGridBoxDetail').datagrid(
		{
			'url':BasePath+'/bill_ch_recheck_dtl/dtl_list.json?locno='+rowData.locno+'&itemNoIsNotNull=Y&isNullBox=Y&recheckNo='+rowData.recheckNo
		});
		
	}else{
		$("#edit_add").hide();
		$("#view").show();
		$("#save_main").hide();
		//$("#checkDtlBtnArea").hide();
		$("#checkDtlBtnArea").css({"height":"0px","padding":"0px","border":"0px"});
		deleteAllGridCommon("reCheckDataGridDetail_view");
		$('#reCheckDataGridDetail_view').datagrid(
		{
			'url':BasePath+'/bill_ch_recheck_dtl/dtl_list.json?locno='+rowData.locno+'&isNullBox=N&recheckNo='+rowData.recheckNo
		});
		
		deleteAllGridCommon("reCheckDataGridBoxDetail_view");
		$('#reCheckDataGridBoxDetail_view').datagrid(
		{
			'url':BasePath+'/bill_ch_recheck_dtl/dtl_list.json?locno='+rowData.locno+'&isNullBox=Y&recheckNo='+rowData.recheckNo
		});
		
	}
	$("#reCheckDetailDialog").window('open'); 
	$("#reCheckDetailForm").form("load",rowData);
};

re_receiptcheck.closereCheckDetailDialog = function(){
	$("#reCheckDetailDialog").window('close'); 
};

//选择商品
re_receiptcheck.openItemSlect = function(obj){
	$('#openUIItem').window({
			title:"商品选择列表",
			onBeforeClose:function(){
				$(".tooltip").remove();
			}
		});
	$("#dataGridJGItem").datagrid('clearData');
	//re_receiptcheck.initCellNo();
	re_receiptcheck.searchItemClear();
	$("#checkNo").val($("#detailCheckNo").val());	
	$('#openUIItem').window('open');
};

//查询商品信息
re_receiptcheck.searchItem = function(){
	var planType = $("#checkPlanType").val();	
	//盘点计划单号
	var planNo = $("#checkPlanNo").val();
	//是否限制品牌   0：不限制 1;限制
	var limitBrandFlag = $("#checkLimitBrandFlag").val();
	//计划单品牌编码
	var brandNo = $("#checkBrandNo").val();
	
	var queryMxURL="";
	if(planType=="1"){//储位盘
		queryMxURL=BasePath+'/bill_ch_check_dtl/selectItem4ChCheck?locno='+re_receiptcheck.locno+'&planNo='+planNo
	}else if(planType=="0" && limitBrandFlag=="0"){//商品盘、不限制品牌 
		queryMxURL=BasePath+'/bill_ch_check_dtl/findItemByPlan.json?locno='+re_receiptcheck.locno+'&planNo='+planNo;
	}else if(planType=="0" && limitBrandFlag=="1"){//商品盘、限制品牌 
		queryMxURL=BasePath+'/bill_ch_check_dtl/findBrandLimitItem?locno='+re_receiptcheck.locno+'&planNo='+planNo+'&brandNo='+brandNo;
	}
	var itemSearchFormArr = $('#itemSearchForm').serializeArray();
	var fromObjStr=convertArray(itemSearchFormArr);
    //3.加载明细
    $( "#dataGridJGItem").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJGItem").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGItem").datagrid( 'load' );
};

//确定选择商品
re_receiptcheck.selectItemOK = function(){
	
	var itemRowData = $("#dataGridJGItem").datagrid("getChecked");
	if(itemRowData.length < 1){
		alert("请选择要添加的商品信息!");
		return;
	}
	
	//校验储位
//	var cellNo=$('#cellNo').combo('getText');
//	var cellNoDatas=$('#cellNo').combobox('getData');
//	var cellNoIsValid=false;
//	for(var i =0;i<cellNoDatas.length;i++){  
//        if(cellNo==cellNoDatas[i].cellNo){  
//        	cellNoIsValid=true;
//        	break;  
//        }  
//    }
//	
//	if(!cellNoIsValid || cellNoDatas.length==0){
//		alert("请输入选择正确的储位。");		
//		return;
//	}
		
	
	$('#openUIItem').window('close');
	var rowData = $("#dataGridJGItem").datagrid("getChecked");// 获取所有勾选checkbox的行
	for(var i=0,length=rowData.length;i<length;i++){
		var data = {};
		data.itemNo = rowData[i].itemNo;
		data.itemName = rowData[i].itemName;
		data.barcode = rowData[i].barcode;
		data.brandNo = rowData[i].brandNo;
		data.styleNo = rowData[i].styleNo;
		data.sizeNo = rowData[i].sizeNo;
		data.colorName= rowData[i].colorName;
		data.brandNo =  rowData[i].brandNo;
		//data.cellNo=$("#cellNo").combobox("getValue");
		data.rowId="";
		data.itemQty =0;
		data.recheckQty = 0;
		data.addFlag = "1";
		var tempObj = $('#reCheckDataGridDetail');
		tempObj.datagrid('appendRow',data);
		var tempIndex = tempObj.datagrid('getRows').length - 1;
		tempObj.datagrid('beginEdit', tempIndex);
     }
};
re_receiptcheck.closeUI = function(){
	$('#openUIItem').window('close');
};

re_receiptcheck.deleterow = function(){
	var rowData = $("#reCheckDataGridDetail").datagrid("getSelected");// 获取所有勾选checkbox的行
	if(rowData==null){
		alert("请选择要删除的行",1);
		return ;
	}else{
		if(rowData.addFlag=="0"){
			alert("非新增商品不能删除",1);
			return ;
		}
		 var rowIndex = $("#reCheckDataGridDetail").datagrid('getRowIndex', rowData);
         $("#reCheckDataGridDetail").datagrid('deleteRow', rowIndex);
	}
};


//保存复盘发单明细
re_receiptcheck.saveChckeDtl = function(){
	var allDate = $("#reCheckDataGridDetail").datagrid('getRows');
	var item;
	for(var index=0,length = allDate.length;index<length;index++){
		item = allDate[index];
		$("#reCheckDataGridDetail").datagrid('endEdit',index);
		var recheckQty = item.recheckQty;
		if(parseInt(recheckQty)<0){
			alert("第"+(index+1)+"行实盘数量不能为负!",1);
			return;
		}
		var cellNo = item.cellNo;
		if(cellNo==null||cellNo==""){
			alert("第"+(index+1)+"行储位不能为空!",1);
			return;
		}
	}
	wms_city_common.loading("show","正在保存明细......");
	var inserted = $("#reCheckDataGridDetail").datagrid('getChanges','inserted');
	var deleted = $("#reCheckDataGridDetail").datagrid('getChanges','deleted');
	var updated = $("#reCheckDataGridDetail").datagrid('getChanges','updated');
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		data:{
			inserted:encodeURIComponent(JSON.stringify(inserted)),
			deleted:encodeURIComponent(JSON.stringify(deleted)),
			updated:encodeURIComponent(JSON.stringify(updated)),
			checkNo:$("#detailCheckNo").val(),
			recheckNo:$("#detailRecheckNo").val()
		},
		dataType : "json",
		url:BasePath+'/bill_ch_recheck_dtl/updateReCheckDtl',
		success : function(data) {
			if(data.result=="success"){
				alert("保存成功");
				$("#reCheckDetailDialog").window("close");
				re_receiptcheck.searchArea();
			}else{
				alert(data.msg,2);
			}
			wms_city_common.loading();
		}
	});
};



//初始化储位
re_receiptcheck.initCellNo = function(){
	var planType = $("#checkPlanType").val();	
	//盘点计划单号
	var planNo = $("#checkPlanNo").val();
	
	var url;
	
	if(planType=="1"){//储位盘
		url=BasePath+'/bill_ch_check/findCellNobyPlan?planNo='+planNo;
	}else if(planType=="0"){//商品盘
		url=BasePath+'/cm_defcell/findSimple?cellStatus=0&locno='+re_receiptcheck.locno;
	}
	
	$.ajax({
		async : false,
		cache : true,
		type : 'POST',
		dataType : "json",
		url:url,
		success : function(data) {
			$('#cellNo').combobox({
			     valueField:"cellNo",
			     textField:"cellNo",
			     data:data,
			     panelHeight:"150"
			  });
		}
	});
};

re_receiptcheck.loadDetail = function(rowData,rowIndex){
	
	re_receiptcheck.curRowIndex=rowIndex;
	
	//re_receiptcheck.initGridHead(rowData.sysNo);
	
	var sysNo = re_receiptcheck.getSysNo(rowData);
	if(sysNo==""){
		return;
	}
	re_receiptcheck.initGridHead(sysNo);
	re_receiptcheck.loadDataDetailViewGrid(rowData);
	$("#showDialog").window('open'); 
};

re_receiptcheck.loadDetail = function(rowData){
    $('#dataGridJGCheckDtl').datagrid(
		{
			'url':BasePath+'/bill_ch_check_dtl/list.json?locno='+re_receiptcheck.locno+'&checkNo='+rowData.checkNo,
			'pageNumber':1
		});
};

//审核
re_receiptcheck.check = function(){
	var checkRows = $("#reCheckDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}
	var dtlArray = [];
	for(var i=0;i<checkRows.length;i++){
			if(checkRows[i].status =='10'||checkRows[i].status =='12'){
				dtlArray.push(checkRows[i].recheckNo+"|"+checkRows[i].checkNo);
			}else{
				alert("非建单状态或完成状态的单据不能审核，请重新选择",1);
				return;
			}
	}
	var dtls  = dtlArray.join(",");
	$.messager.confirm('确认','您确认要审核单据吗？',function(r){
		if (r){
			wms_city_common.loading("show","正在审核......");
			$.ajax({
				async : false,
				cache : true,
				type : 'POST',
				data:{
					dtls:dtls
				},
				dataType : "json",
				url:BasePath+'/bill_ch_recheck_dtl/chReCheckAudit',
				success : function(data) {
					wms_city_common.loading();
					if(data.result=="success"){
						alert("审核成功");
						re_receiptcheck.searchArea();
					}else{
						alert(data.msg,2);
					}
				}
			});
		}
	});
};


//按计划保存
re_receiptcheck.saveByPlan = function(fBoxFlag){
	wms_city_common.loading("show","正在保存......");
	$.ajax({
		async : true,
		cache : true,
		type : 'POST',
		data:{
			checkNo:$("#detailCheckNo").val(),
			locno:re_receiptcheck.locno,
			fBoxFlag:fBoxFlag,
			recheckNo:$("#detailRecheckNo").val()
		},
		dataType : "json",
		url:BasePath+'/bill_ch_recheck_dtl/saveByPlan',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert("按计划保存成功");
				if('N'==fBoxFlag){
					$("#reCheckDataGridDetail").datagrid('load');
				}else{
					$("#reCheckDataGridBoxDetail").datagrid('load');
				}
			}else{
				alert(data.msg,2);
			}
		}
	});
};

//实盘置零
re_receiptcheck.resetPlan = function(fBoxFlag){
	wms_city_common.loading("show","正在加载......");
	$.ajax({
		async : true,
		cache : true,
		type : 'POST',
		data:{
			checkNo:$("#detailCheckNo").val(),
			locno:re_receiptcheck.locno,
			fBoxFlag:fBoxFlag,
			recheckNo:$("#detailRecheckNo").val()
		},
		dataType : "json",
		url:BasePath+'/bill_ch_recheck_dtl/resetPlan',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert("实盘置零成功");
				if('N'==fBoxFlag){
					$("#reCheckDataGridDetail").datagrid('load');
				}else{
					$("#reCheckDataGridBoxDetail").datagrid('load');
				}
			}else{
				alert(data.msg,2);
			}
		}
	});
};


re_receiptcheck.saveDetail = function(){
	wms_city_common.loading("show","正在加载......");
	$.ajax({
		async : true,
		cache : true,
		type : 'POST',
		data:{
			locno:re_receiptcheck.locno,
			recheckNo:$("#detailRecheckNo").val(),
			checkWorker:$("#checkWorker").combobox("getValue")
		},
		dataType : "json",
		url:BasePath+'/bill_ch_recheck_dtl/saveMain',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert("保存成功");
			}else{
				alert(data.msg,2);
			}
		}
	});
};


re_receiptcheck.onClickRowCheckDtl = function(rowIndex, rowData){
	var curObj = $("#reCheckDataGridDetail");
	curObj.datagrid('beginEdit', rowIndex);
	var ed = curObj.datagrid('getEditor', {index:rowIndex,field:'cellNo'});
	var itemQty = rowData.itemQty;
	if(itemQty!=0){
		$(ed.target).attr("disabled",true);
	}else{
		$(ed.target).attr("disabled",false);
	}
};