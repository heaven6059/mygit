/*
 * 店退仓
 * **/
var billumuntread = {};
billumuntread.locno;
billumuntread.status10 = "10";
billumuntread.status13 = "11";
billumuntread.currentDate;
//品牌库编码
billumuntread.lookupcode_sys_no = "SYS_NO";
//退仓类型编码
billumuntread.lookupcode_untread_type = "ITEM_TYPE";
//来源类型编码
billumuntread.lookupcode_po_type = "CMD_TYPE";
//退仓状态编码
billumuntread.lookupcode_untread_status= "UMUNTREAD_STATUS";

billumuntread.dataGridJGfooter={};

$(document).ready(function(){
	$("#startCreatetm").datebox('setValue',getDateStr(-2));
	//初始化业主
	billumuntread.loadOwner();
	//初始化仓库
	billumuntread.loadLoc();
	//加载列表数据
	//billumuntread.loadDataGrid();
	//初始化退仓类型
	billumuntread.initUntreadType();
	//初始化来源类型
	billumuntread.initPoType();
	//初始化用户
	billumuntread.initUsers();
	//初始化状态
	billumuntread.initUntreadStatus();
	billumuntread.initQuality();
	//加载品牌
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')}//查询
			);
	wms_city_common.loadSysNo4Cascade(objs);
		$('#dataGridJG_view').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billumuntread.itemQty = data.footer[1].itemQty;
		   				billumuntread.receiptQty = data.footer[1].receiptQty;
		   				billumuntread.checkQty = data.footer[1].checkQty;
		   			}else{
		   				var rows = $('#dataGridJG_view').datagrid('getFooterRows');
			   			rows[1]['itemQty'] = billumuntread.itemQty;
			   			rows[1]['receiptQty'] = billumuntread.receiptQty;
			   			rows[1]['checkQty'] = billumuntread.checkQty;
			   			$('#dataGridJG_view').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
		$('#dataGridJG').datagrid({
			'onLoadSuccess':function(data){
	   			if(data.footer[1].isselectsum){
	   				billumuntread.dataGridJGfooter = data.footer[1];
	   			}else{
	   				var rows = $('#dataGridJG').datagrid('getFooterRows');
		   			rows[1] = billumuntread.dataGridJGfooter;
		   			$('#dataGridJG').datagrid('reloadFooter');
	   			}
	   		}
		});
});


/***********************************************初始化******************************************************/
billumuntread.qualityDataObj = {};
billumuntread.initQuality = function(){
	wms_city_common.comboboxLoadFilter(
			["quality","quality_view","quality_search"],
			null,
			null,
			null,
			true,
			[false, false, true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			billumuntread.qualityDataObj,
			null);
};
//加载货主信息
billumuntread.ownnerData = {};
billumuntread.loadOwner = function(){
	wms_city_common.comboboxLoadFilter(
			["ownerNo","view_ownerNo","search_ownerNo"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[false, false, true],
			BasePath+'/entrust_owner/get_biz',
			{},
			billumuntread.ownnerData,
			null);
};

//加载仓库信息
billumuntread.loadLoc = function(){
		$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billumuntread.locno = data.locno;
			billumuntread.currentDate = data.currentDate10Str;
			$("#startTm").datebox('setValue',billumuntread.currentDate);
			$("#endTm").datebox('setValue',billumuntread.currentDate);
		}
	});
};

//退仓类型
billumuntread.typeData = {};
billumuntread.initUntreadType = function(){
	wms_city_common.comboboxLoadFilter(
			["untreadType","view_untreadType","untreadType_search"],
			null,
			null,
			null,
			true,
			[false, false, true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumuntread.lookupcode_untread_type,
			{},
			billumuntread.typeData,
			null);
	
};

billumuntread.initPoType = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumuntread.lookupcode_po_type,
		success : function(data) {
			$('#poType,#view_poType').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:150
			});
		}
	});
};

//状态
billumuntread.status = {};
billumuntread.initUntreadStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["search_status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billumuntread.lookupcode_untread_status,
			{},
			billumuntread.status,
			null);
};

billumuntread.initUsers = function(){
	wms_city_common.comboboxLoadFilter(
			["search_creator","search_auditor"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true,true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
};
/***********************************************初始化end******************************************************/

//加载Grid数据Utils
billumuntread.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

/***********************************************新增******************************************************/
//弹出新增窗口
billumuntread.addInfo = function(){
	 //清空所有数据
	 $("#detailForm").form("clear");
	 //仓库，委托业主初始化
	// $("#ownerNo").combobox("select",$("#ownerNo").combobox("getData")[0].ownerNo);
	 //清空列表
	 deleteAllGridCommon('dataGridJG_detail');
	 billumuntread.mainInfoShow("add");
	 $("#sub_con").hide();
	 $('#detailDialog').window({
		title:"新增",
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	 });
	  $("#untreadMmBtn").attr("disabled",false);
	  billumuntread.mainInfoShow("edit");
		//品质
	 $("#storeNo").combobox("enable");
	 $('#storeNo').combobox({data:{}});
	 
	 $("#storeNoHide").attr("disabled",true);
	 $("#addBtn").show();
	 $("#editBtn").hide();
	 $("#detailDialog").window('open'); 
};

billumuntread.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status!=billumuntread.status10){
		alert("非建单状态的数据不能修改");
		return;
	}else{
	     $("#untreadMmBtn").attr("disabled",true);
	 	 $("#storeBtn").attr("disabled",true);
		 $("#detailForm").form('load',checkedRows[0]);
		 //清空列表
		 deleteAllGridCommon('dataGridJG_detail');
		 billumuntread.mainInfoShow("edit");
		 $("#storeNo").combobox("disable");
		 $("#storeNoHide").attr("disabled",false);
		 $("#editBtn").show();
		 $("#addBtn").hide();
		 $("#submaintoolbar").show();
		 $('#detailDialog').window({
			title:"修改",
			onBeforeClose:function(){
				$(".tooltip").remove();
			}
		 });
		 $("#sub_con").show();
		 $("#detailDialog").window('open'); 
		 var rowData = checkedRows[0];
		 //加载明细
		var queryMxURL=BasePath+'/bill_um_untread_dtl/select4Box?untreadNo='+rowData.untreadNo+'&ownerNo='+rowData.ownerNo+'&locno='+rowData.locno;
		$( "#dataGridJG_detail").datagrid( 'options' ).url=queryMxURL;
	    $( "#dataGridJG_detail").datagrid( 'load' );
	}
};

//新增主表信息
billumuntread.save_main = function(){
	$("#untreadMmBtn").attr("disabled",false);
	var fromObj=$('#detailForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    wms_city_common.loading("show","正在生成店退仓单......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_um_untread/addMain',
		success : function(data) {
			if(data.result=="success"){
				alert('新增成功!');
				$("#addBtn").hide();
	 			$("#editBtn").show();
	 			$("#untreadNo").val(data.untreadNo);
	 			billumuntread.mainInfoShow("edit");
	 			billumuntread.refresh("dataGridJG");
	 			$("#untreadMmBtn").attr("disabled",true);
	 			$("#storeBtn").attr("disabled",true);
	 			$("#sub_con").show();
			}else{
				alert('新增失败,请联系管理员!',2);
			}
			wms_city_common.loading();
		}
	});
};
//修改主表信息
billumuntread.edit_main = function(){
	var fromObj=$('#detailForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    wms_city_common.loading("show","正在保存店退仓单......");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:fromObj.serialize(),
		url:BasePath+'/bill_um_untread/eidtMain',
		success : function(data) {
			if(data.result=="success"){
				alert('修改成功!');
				$("#addBtn").hide();
	 			$("#editBtn").show();
				billumuntread.refresh("dataGridJG");
			}else{
				alert(data.msg,2);
			}
			wms_city_common.loading();
		}
	});
};

billumuntread.mainInfoShow = function(type){
	var disable = "disable";
	var disabled = true;
	if(type=="add"){
		disable = "enable";
		disabled = true;
	}else{
		disable = "disable";
		disabled = false;
	}
	//货主
	$("#ownerNo").combobox(disable);
	$("#ownerNoHide").attr("disabled",disabled);
	//退仓类型
	$("#untreadType").combobox(disable);
	$("#untreadTypeHide").attr("disabled",disabled);
	//品质
	 $("#quality").combobox(disable);
	 $("#qualityHide").attr("disabled",disabled);
};

billumuntread.showSub = function(data){
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

billumuntread.converStr2JsonObj= function(data){
	if(data==null){
		 return "";
	}
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billumuntread.converStr2JsonObj2= function(data){
	if(data==null){
		 return "";
	}
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

/***********************************************格式化******************************************************/
//状态
billumuntread.statusFormatter = function(value, rowData, rowIndex){
	return billumuntread.status[value];
};
//委托业主
billumuntread.ownerFormatter = function(value, rowData, rowIndex){
	return billumuntread.ownnerData[value];
};

/***********************************************格式化******************************************************/


//查询区域信息
billumuntread.searchArea = function(){
	var startCreatetm = $('#startCreatetm').datebox('getValue');
	var endCreatetm = $('#endCreatetm').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startCreatetm,endCreatetm)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
    
    var startAudittm = $('#startAudittm').datebox('getValue');
	var endAudittm = $('#endAudittm').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startAudittm,endAudittm)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
	}
	var fromObjStr=wms_city_common.convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_untread/list.json?locno='+billumuntread.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
};

billumuntread.searchLocClear = function(){
	$('#brandNo').combobox("loadData",[]);
	$('#searchForm').form("clear");
};

//弹出商品选择对话框
billumuntread.showBoxDialog = function(){
	//清除列表信息
    deleteAllGridCommon('dataGridJGBox');
	$("#boxDialog").window({
		onBeforeClose:function(){
			$(".tooltip").remove();
		}
	});
	$("#boxSearchForm").form("clear");
	$("#boxDialog").window('open');
};

//查询箱号
billumuntread.searchBox = function(){
	var ownerNo = $("#ownerNo").combobox("getValue");
	var fromObjStr=wms_city_common.convertArray($('#boxSearchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_untread/selectBox?locno='+billumuntread.locno+'&ownerNo='+ownerNo;
    $( "#dataGridJGBox").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJGBox").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJGBox").datagrid( 'load' );
};
//清空箱号
billumuntread.searchBoxClear = function(){
	$('#boxSearchForm').form("clear");
};

billumuntread.selectBoxOK = function(){
	var checkedRows = $("#dataGridJGBox").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length==0){
		alert("请选择明细!",1);
		return;
	}
	for(var i=0,length=checkedRows.length;i<length;i++){
        $("#dataGridJG_detail").datagrid('appendRow', checkedRows[i]);
        //var rows = $("#dataGridJG_detail").datagrid("getRows");
        //$("#dataGridJG_detail").datagrid('beginEdit',rows.length-1);
	}
	$("#boxDialog").window('close');
};

billumuntread.closeUI= function(){
	$("#itemDialog").window('close');
};

//删除一行
billumuntread.deleterow = function(){
	var obj = $('#dataGridJG_detail');
	var row = obj.datagrid('getSelected');
	if(row==null){
		return;
	}
	var index = obj.datagrid('getRowIndex', row);
	$('#dataGridJG_detail').datagrid('deleteRow',index);
};

//保存明细
billumuntread.saveDetail = function(){
	var tempFlag = billumuntread.endEdit("dataGridJG_detail");
	if(!tempFlag){
		return;
	}
	var tempObj = $('#dataGridJG_detail');
	var inserted = tempObj.datagrid('getChanges', "inserted");
	var updated = tempObj.datagrid('getChanges', "updated");
	var deleted = tempObj.datagrid('getChanges', "deleted");
	wms_city_common.loading("show","正在保存店退仓明细......");
	$.ajax({
		  async : true,
		  cache: true,
		  type: 'POST',
		  url: BasePath+'/bill_um_untread_dtl/saveDetail',
		  data: {
		  	untreadNo:$("#untreadNo").val(),
		  	ownerNo:$("#ownerNo").combobox("getValue"),
		  	inserted:encodeURIComponent(JSON.stringify(inserted)),
		  	updated:encodeURIComponent(JSON.stringify(updated)),
		  	deleted:encodeURIComponent(JSON.stringify(deleted))
		  },
		  success: function(data){
		  	 if(data.result=='success'){
				alert("保存成功");
				$("#detailDialog").window('close');
				billumuntread.refresh("dataGridJG");
		 	}else{
			 	alert(data.msg,2);
		 	}
		  	wms_city_common.loading();
		  }
	});
};

billumuntread.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		var storNoField = tempObj.datagrid('getEditor', {index:i,field:'storeNo'});
    		if(storNoField!=null){
    			if(!billumuntread.checkStoreNo($(storNoField.target).val())){
	    			alert("第"+(i+1)+"行,客户编码不存在");
	    			return false;
    			}
    		}
    		tempObj.datagrid('endEdit', i);
    	}else{
    		alert('数据验证没有通过!',1);
    		return false;
    	}
    }
    return true;
};
billumuntread.checkStoreNo = function(storeNo){
	var isOk = true;
	$.ajax({
		  async : false,
		  cache: true,
		  type: 'POST',
		  url: BasePath+'/bill_um_untread_dtl/queryStoreByStoreNo',
		  data: {
		  	storeNo:storeNo,
		  	storeType:11
		  },
		  success: function(data){
		  	 if(data.storeNo==""||data.storeNo==null){
				isOk = false;
		 	}else{
			 	isOk = true;
		 	}
		  }
	});
	return isOk;
};
//关闭详情框
billumuntread.coloseDetailDialog = function(){
	$('#detailDialog').window('close');
};


billumuntread.getRepeat = function(tempary) {
var ary=[];
for(var i = 0;i<tempary.length;i++){
	ary.push(tempary[i].boxNo);
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

//详情
billumuntread.showDetail = function(rowData){
	$('#detailDialogView').window({
		title:"店退仓单明细"
	 });
	$("#detailDialogView").window('open'); 
	$("#detailForm_view").form('load',rowData);
	//加载明细
	var queryMxURL=BasePath+'/bill_um_untread_dtl/dtl_list.json?untreadNo='+rowData.untreadNo+'&ownerNo='+rowData.ownerNo+'&locno='+rowData.locno;
	$( "#dataGridJG_view").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_view").datagrid( 'load' );
};




/***********************************************删除******************************************************/
billumuntread.del = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	var keyStr = [];
	var allOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!=billumuntread.status10){
			allOk = false;
			return false;
		}
		keyStr.push(item.untreadNo+"|"+item.ownerNo);
	});   
	if(!allOk){
		alert("非建单状态的数据不能删除",1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
            var url = BasePath+'/bill_um_untread/deleteUntread';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	wms_city_common.loading("show","正在删除......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					 wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('删除成功!');
						 billumuntread.refresh("dataGridJG");
        		 	}else{
        			 	alert(data.msg,2);
        		 	}
				  }
			});
        }  
    });  
};

/***********************************************删除end******************************************************/

/***********************************************审核***********************************************************/
billumuntread.audit=function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}
	
	var keyStr = [];
	var allOk = true;
	var dtlOk = true;
	$.each(checkedRows, function(index, item){
		if(item.status!="10"){
			allOk = false;
			return false;
		}
		if(item.dtlCount=="0"){
			dtlOk = false;
			return false;
		}
		keyStr.push(item.untreadNo+"|"+item.ownerNo);
	});  
	if(!allOk){
		alert("非建单状态的数据不能审核",1);
		return;
	} 
	if(!dtlOk){
		alert("没有明细的单据不能审核",1);
		return;
	} 
    var url = BasePath+'/bill_um_untread/auditUntread';
	var data={
	    "keyStr":keyStr.join(",")
	};
	
	$.messager.confirm("确认","你确定要审核这"+checkedRows.length+"条数据", function (r){  
		if (r) {
			wms_city_common.loading("show","正在审核......");
			$.ajax({
				  type: 'POST',
				  url: url,
				  data: data,
				  cache: true,
				  success: function(data){
					  wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('审核成功!');
						  billumuntread.refresh("dataGridJG");
        		 	}else{
        			 	alert(data.msg,2);
        		 	}
				  }
			});
        }  
    });
};
/*
 * 作废店退仓单
 * wanghb 2014-7-14
*/
billumuntread.invalid=function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	var len = checkedRows.length;
	var untReadNos = [];
	if(len < 1){
		alert('请选择要作废的单据！',1);
		return;
	}else{
		var temp = {};
		for(var i=0;i<len;i++){
			temp = checkedRows[i];
			var result = false;
			ajaxRequestAsync(BasePath+'/bill_um_untread/get', {locno:temp.locno,ownerNo:temp.ownerNo,untreadNo:temp.untreadNo}, function(data){
				if(data.status!='11'){
					result = true;
				}
			});
			if(result){
				alert("单据：" + temp.untreadNo +"已删除或者状态已改变！");
				return false;
			}
			untReadNos.push(temp.untreadNo+";"+temp.ownerNo);
		}
		
		$.messager.confirm("确认","是否确定作废店退仓单？", function (r){
			if(!r){
				return;
			}
			var param = {
					untReadNos:untReadNos.join(","),
					locno:billumuntread.locno
			};
			var url = BasePath+'/bill_um_untread/invalid';
			wms_city_common.loading("show", "正在作废店退仓单......");
			$.post(url, param, function(result) {
				if(result.result == 'success'){
					alert('作废成功!');
					billumuntread.refresh("dataGridJG");
				}else if(result.result == 'fail'){
					var resultList=result.msg.split(",");
					if(resultList.length==1){
						alert("<span>单据"+result.msg+"已创建退仓收货单，不能作废!",1);
					}else{
						alert("<span>以下单据已创建退仓收货单，不能作废!</span><br/><textarea rows=5 cols=34>"+result.msg+"</textarea>",1);
					}
				}else if(result.result == 'error'){
					alert('作废失败!',2);
				}else{
					alert(result.result,2);
				}
				wms_city_common.loading();
			}, "JSON").error(function() {
		    	alert('作废失败!',1);
		    	wms_city_common.loading();
		    });
		});
	}
};
billumuntread.refresh = function(dataGridId){
	$("#"+dataGridId).datagrid('load');
};

//选择通知单号
billumuntread.showUntreadMmNoDialog = function(){
	$("#umtreadMmDialog").window('open');
	deleteAllGridCommon('dataGrid_umtreadMm');
	var queryMxURL=BasePath+'/bill_um_untread_mm/list.json?locno='+billumuntread.locno+'&statusMore='+billumuntread.status13;
	$( "#dataGrid_umtreadMm").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGrid_umtreadMm").datagrid( 'load' );
};

billumuntread.selectUntreadMmNo = function(rowData){
	$("#untreadMmNo").val(rowData.untreadMmNo);
	$("#umtreadMmDialog").window('close');
	$("#detailForm").form("load",rowData);
	$("#remark").val("");
	billumuntread.mainInfoShow("edit");
	$.ajax({
		  async : true,
		  cache: true,
		  type: 'POST',
		  url: BasePath+'/bill_um_untread_mm_dtl/selectStoreNo',
		  data: {
			  untreadMmNo:rowData.untreadMmNo,
		  	  locno:rowData.locno,
		  	  ownerNo:rowData.ownerNo
		  },
		  success: function(data){
			  $('#storeNo').combobox({
				     valueField:"storeNo",
				     textField:"storeName",
				     data:data.data,
				     panelHeight:150
			  });
		  }
	});
};

billumuntread.showStoreNoDialog=function(){
	$("#storeDialog").window('open'); 
	deleteAllGridCommon('dataGrid_storeNo');
	var queryMxURL=BasePath+'/bill_um_untread/selectStore?storeType=11';
	$( "#dataGrid_storeNo").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGrid_storeNo").datagrid( 'load' );
};

billumuntread.searchStore = function(){
	var fromObjStr=wms_city_common.convertArray($('#storeForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_untread/selectStore?storeType=11';
    $( "#dataGrid_storeNo").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
	$( "#dataGrid_storeNo").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGrid_storeNo").datagrid( 'load' );
};

billumuntread.clearSearchStore = function(){
	$("#storeForm").form("clear");
};

billumuntread.selectStore = function(rowData){
	$("#storeNo").val(rowData.storeNo);
	$("#storeDialog").window('close');
};
billumuntread.closeWindow = function(id){
	$("#"+id).window('close');
};

billumuntread.typeFormatter  = function(value, rowData, rowIndex){
	return billumuntread.typeData[value];
};


billumuntread.qualityFormatter  = function(value, rowData, rowIndex){
	return billumuntread.qualityDataObj[value];
};

billumuntread.printBatch = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要打印的记录!',1);
		return;
	}
	var keyStr = [];
	$.each(checkedRows, function(index, item){
		keyStr.push(item.untreadNo+"|"+item.ownerNo);
	}); 
	var resultData;
	$.ajax({
		  async : false,
		  cache: true,
		  type: 'POST',
		  url: BasePath+'/bill_um_untread/pringBatch',
		  data: {
			 "keystr":keyStr.join(",")
		  },
		  success: function(data){
			  resultData = data.list;
		  }
	});
	if(resultData.length<=0){
		alert("没有需要打印的数据!");
		return;
	}
	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
  	if(LODOP==null){
  		return;
  	}
  	LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
  	var strStyle="<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
	for(var i=0,length = resultData.length;i<length;i++){
		LODOP.NewPage();
		var body = getHtmlByTemplate(resultData[i]);
		var head = getHeadByTemplate(resultData[i]);
		
		//设置表格内容
		LODOP.ADD_PRINT_TABLE(150,0,"100%",400,strStyle+body);
		LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
		
		//设置表格头部
		LODOP.ADD_PRINT_HTM(0,0,"100%",140,head);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		
		//设置条码
		LODOP.ADD_PRINT_BARCODE(45,110,250,40,"128A",resultData[i].untread.untreadNo);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	}
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
};
function getHeadByTemplate(data){
	var storeName = data.untread.storeName==null?"": data.untread.storeName;
	var poType = data.untread.poType==null?"": data.untread.poType;
	var poNo = data.untread.poNo==null?"": data.untread.poNo;
	var html = "<table style='width:100%;'>";
	html += "<tr><td style='text-align:center;font-size:30px;' colspan='3'>店退仓单</td></tr>";
	html += "<tr style='height:40px;'><td colspan='3' style='width:60%;'>单据号条码：</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;据&nbsp;&nbsp;&nbsp;&nbsp;号："+data.untread.untreadNo+"</td><td>客户名称："+storeName+"("+data.untread.storeNo+")</td><td>&nbsp;</td></tr>";
	html += "<tr><td>来&nbsp;源&nbsp;&nbsp;单&nbsp;号："+poNo+"</td><td>来源类型："+poType+"</td><td>总合计："+data.allSum+"</td></tr>";
	html += "</table>";
	return html;
}
function getHtmlByTemplate(data){
	var html = "<table border='0' cellpadding='1' cellspacing='1' width='100%' style='border-collapse:collapse;font-size:13px;' bordercolor='#333333'><thead><tr bgcolor='#fff'><td>品牌</td><td>商品编码</td><td>商品名称</td>";
	var sizeNos = data.allSizeNo;
	for(var i=0,length = sizeNos.length;i<length;i++){
		html = html+"<td>"+sizeNos[i]+"</td>";
	}
	html = html+"<td>总数</td><td>箱号</td>";
	html = html+"</tr>";
	html += "</thead>";
	var rows = data.list;
	for(var i=0,length=rows.length;i<length;i++){
		html = html+"<tr bgcolor='#fff'><td>"+rows[i].untreadDtl.brandName+"</td>"+
			"<td>"+rows[i].untreadDtl.itemNo+"</td>"+
			"<td>"+rows[i].untreadDtl.itemName+"</td>";
			for(var j=0,sublength = sizeNos.length;j<sublength;j++){
				var itemQty = rows[i][sizeNos[j]];
				if(itemQty!=""&&itemQty!=null){
					html = html+"<td>"+itemQty+"</td>";
				}else{
					html = html+"<td></td>";
				}
			}
		html= html+"<td>"+rows[i].sumQty+"</td><td>"+(rows[i].untreadDtl.boxNo==null?"":rows[i].untreadDtl.boxNo)+"</td></tr>";	
	}
	html = html + "<tfoot><tr><td style='text-align:13px;text-align:center;' colspan='"+(sizeNos.length+5)+"'><font tdata='pageNO' format = '#' align='center'>第#页</font>&nbsp;/&nbsp;<font format='#' tdata='pageCount'>共#页</font></td></tr></tfoot>";
	html = html+"</table>";
	return html;
};

