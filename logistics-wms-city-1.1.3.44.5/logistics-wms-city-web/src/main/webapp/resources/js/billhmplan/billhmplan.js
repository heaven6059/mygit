
var billhmplan = {};
billhmplan.opt = "0";//0-新增、1-修改
billhmplan.itemType={};
billhmplan.quality={};

billhmplan.statusData = [{    
    "text":"10→建单", 
    "value":"10",   
},{    
    "text":"13→完结", 
    "value":"13"   
}];


billhmplan.downloadTemp = function(){
	window.open(BasePath + "/bill_hm_plan_dtl/downloadTemple");
};

billhmplan.importConToItem = function(){
//	$('#importDialogView').window({title:"箱明细初始化"});
//	$("#importDialogView").window('open'); 
	var fromObj = $("#showDialog");
	$("#iframe").attr("src",BasePath + "/bill_hm_plan_dtl/iframe?v="+new Date());
	$("#showImportDialog").window('open'); 
};

billhmplan.loading = function(type,msg){
	wms_city_common.loading(type,msg);
};

//加载Grid数据Utils
billhmplan.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
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


//打开窗口
billhmplan.openWindow = function(windowId,opt){
	$('#'+windowId).window({
		title:opt
	});
	$('#'+windowId).window('open');
};


//关闭窗口
billhmplan.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};


//插入行开始编辑
billhmplan.insertRowAtEnd = function(gid,rowData){
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


//验证编辑行
billhmplan.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		var originQty = tempObj.datagrid('getEditor', {index:i,field:'originQty'});
    		if(originQty!=null){
    			var itemNo = rowArr[i].itemNo;
    			var conContentQty = rowArr[i].conContentQty;
            	var originQty = tempObj.datagrid('getEditor', {index:i,field:'originQty'});
            	if(originQty!=null){
            		if(originQty.target.val() > conContentQty){
                		$(originQty.target).focus();
                		return "商品"+itemNo+":计划数量不能大于库存数量;";
                	}else{
                		tempObj.datagrid('endEdit', i);
                	}
            	}
    		}
    	}else{
    		return '数据验证没有通过!';
    	}
    }
    return "";
};


//查询移库计划单
billhmplan.searchData = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_hm_plan/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billhmplan.locno;
	
	var startCreatetmCondition = $('#startCreatetmCondition').datebox('getValue');
	var endCreatetmCondition = $('#endCreatetmCondition').datebox('getValue');
	if(!wms_city_common.isStartLessThanEndDate(startCreatetmCondition,endCreatetmCondition)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	
	var startAudittmCondition = $('#startAudittmCondition').datebox('getValue');
	var endAudittmCondition = $('#endAudittmCondition').datebox('getValue');
	
	if(!wms_city_common.isStartLessThanEndDate(startAudittmCondition,endAudittmCondition)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    }
	
	billhmplan.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};


//查询库存数据
billhmplan.searchConContent = function(){
	var fromObjStr=convertArray($('#itemSearchForm').serializeArray());
	var queryMxURL=BasePath+'/con_content/findConContentByPage.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billhmplan.locno;
	//reqParam['ownerNo'] = $('#ownerNoHidden').val();
	billhmplan.loadGridDataUtil('dataGridItem', queryMxURL, reqParam);
};


//清除查询条件
billhmplan.searchClear = function(id){
	$('#'+id).form("clear");
	$('#brandNoCondition').combobox("loadData",[]);
};


//打开新增窗口
billhmplan.addPlanOpen = function(){
	billhmplan.openWindow('openWindowPlan', '新增');
	$('#planDtlDg').datagrid('loadData', { total: 0, rows: [] });
	
	billhmplan.showHideBtn('add');
};


//打开商品选择窗口
billhmplan.addItemOpen = function(){
	var fromObj = $('#dataForm');
	var ownerNo = $("input[id=ownerNo]",fromObj).combobox('getValue');
	$("#ownerNoCondition2").combobox('setValue',ownerNo);
	$("#ownerNoCondition2Hide").val(ownerNo);
	$("#ownerNoCondition2").combobox("disable");
	billhmplan.openWindow('openWindowItem', '商品选择');
	$('#dataGridItem').datagrid('loadData', { total: 0, rows: [] });
};


//修改移库单
billhmplan.toUpdatePlan = function(){
	
	// 获取勾选的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}
	
	if(checkedRows.length > 1){
		alert('只能选择一条记录!',1);
		return;
	}
	
	if(checkedRows[0].status!='10'){
		alert('非建单状态的数据不能修改!',1);
		return;
	}
	
	$.each(checkedRows,function(index,item){
		billhmplan.loadDetail(item,'edit');
		$('#info_add').hide();
		$('#info_remove').hide();
	});
	
};

//导出
billhmplan.do_export = function(){
	var exportColumns = $("#planDtlDg").datagrid('options').columns[0];
	var str = '[';
	for(var index=0;index<exportColumns.length;index++){
		var temp = exportColumns[index];
		if(index == 0 || index == 1 || index == 2) {
		} else {
			str +=  "{\"field\":\""+temp.field+"\",\"title\":\""+temp.title+"\"}";
			if(index != exportColumns.length-1){
				str += ",";
			}
		}
	}
	str += "]";
	
	var planNo = $("#planNo").val();
	var ownerNo = $("#ownerNo").combobox('getValue');
	
	var searchUrl = BasePath+'/bill_hm_plan_dtl/getByPage.json?locno='+billhmplan.locno+'&planNo='+planNo+'&ownerNo='+ownerNo;
	
	var fromObjStr=convertArray($('#loadBoxDtlForm_view').serializeArray());
	var data = eval("(" +fromObjStr+ ")");
	data.page = 1;
	data.rows = $("#planDtlDg").datagrid('options').pageSize;
	var resutl = {};
	$.ajax({
		  async:false,
		  type: 'POST',
		  url: searchUrl,
		  data: data,
		  cache: true,
		  success: function(r){
			  resutl = r;
		  }
	});
	if(!resutl || resutl == null || resutl.total == 0){
		alert("没有可以导出的数据!",1);
		return;
	}else if(resutl.total > 10000){
		alert("数据大于10000条不能导出!",1);
		return;
	}
	$("#locno_export").val(billhmplan.locno);
	$("#ownerNo_export").val(ownerNo);
	$("#planNo_export").val(planNo);
	$("#exportColumnsCondition_export").val(str);
	$("#exportForm").submit();
};

//移库单计划明细
billhmplan.loadDetail = function(rowData,type){
	
	billhmplan.openWindow('openWindowPlan', '修改/查看');
	$('#dataForm').form('load',rowData);
//	var url = BasePath+'/bill_hm_plan/get';
	var queryParams = {planNo:rowData.planNo,locno:billhmplan.locno,ownerNo:rowData.ownerNo};
//	ajaxRequest(url,queryParams,function(data){
//		$('#dataForm').form('load',data);
//		$("#ownerNoHidden").val(data.ownerNo);
//	});
	
	var queryMxURL = BasePath+'/bill_hm_plan_dtl/getByPage.json';
	billhmplan.loadGridDataUtil('planDtlDg', queryMxURL, queryParams);
	
	billhmplan.showHideBtn(type,rowData);
};

//隐藏显示按钮
billhmplan.showHideBtn = function(type,rowData){
	if(type=='add'){
		$('#dataForm').form('clear');
		$("#ownerNo").combobox('enable');
		$("#detail-sub-toolbar").hide();
		//$('#dataForm')[0].reset();
		
		$('#btn-save').show();
		$("#btn-edit").hide();
		$("#btn-export").hide();
		$("#btn-add-detail").show();
	}else if(type=='edit'){
		
		if(rowData!=null && rowData.businessType=='1'){
			$("#btn-add-detail").hide();
		}else{
			$("#btn-add-detail").show();
		}
		
		$("#ownerNo").combobox('disable');
		$("#detail-sub-toolbar").show();
		
		
		$("#btn-save").hide();
		$('#btn-edit').show();
		$("#btn-export").hide();
	} else {
		$("#ownerNo").combobox('disable');
		$("#detail-sub-toolbar").hide();
		
		$('#btn-save').hide();
		$("#btn-edit").hide();
		$("#btn-export").show();
		$("#btn-add-detail").show();
	}
};

billhmplan.loadDetailUtil = function(rowData){
	$('#dataForm').form('load',rowData);
//	var url = BasePath+'/bill_hm_plan/get';
	var queryParams = {planNo:rowData.planNo,locno:billhmplan.locno,ownerNo:rowData.ownerNo};
//	ajaxRequest(url,queryParams,function(data){
//		$('#dataForm').form('load',data);
//		$("#ownerNoHidden").val(data.ownerNo);
//	});
	var queryMxURL = BasePath+'/bill_hm_plan_dtl/list.json';
	billhmplan.loadGridDataUtil('planDtlDg', queryMxURL, queryParams);
};


//退仓单详情
billhmplan.loadDataGrid = function(){
	$('#dataGridJG').datagrid({'url':BasePath+'/bill_hm_plan/list.json?locno='+billhmplan.locno,'title':'移库计划单列表','pageNumber':1 });
};



//选择商品
billhmplan.selectItem = function(){
	
//	var checkItems = $('#dataGridItem').datagrid('getChecked');
//	if(checkItems.length < 1){
//		alert("请选择商品信息!");
//		return;
//	}
	
	var checkItems = $('#dataGridItem').datagrid('getChecked');
	if(!billhmplan.checkSelectItem()){
		return;
	}
	
	//3. 保存
    var confirmFn = function(returnData){
    	$.each(checkItems,function(index,item){
    		var qualityStr=billhmplan.formatterQuality(item.quality);
    		var itemTypeStr=billhmplan.formatterItemType(item.itemType);
    	
    		var reqParas = {
    				//itemId:item.itemId,
    				cellId:item.cellId,
    				conContentQty:item.sumPlanQty,
    				itemNo:item.itemNo,
    				itemName:item.itemName,
    				styleNo:item.styleNo,
    				colorName:item.colorName,
    				sizeNo:item.sizeNo,
    				originQty:item.sumPlanQty,
    				sCellNo:item.cellNo,
    				packQty:item.packQty,
    				quality:item.quality,
    				itemType:item.itemType,
    				qualityStr:qualityStr,
    				itemTypeStr:itemTypeStr,
    				brandNo:item.brandNo
    		};
    		//插入商品到父窗口,开始编辑
    		billhmplan.insertRowAtEnd('planDtlDg',reqParas);
    	});
    	billhmplan.closeWindow('openWindowItem');
    };
	
    ajaxRequest(BasePath+'/initCache/getCurrentUser',{},confirmFn);
	
};

//验证选择箱号
billhmplan.checkSelectItem = function(){
	
	var checkResult = true;
	var checkItems = $('#dataGridItem').datagrid('getChecked');
	if(checkItems.length < 1){
		alert("请选择商品信息!");
		return false;
	}
	
	//所有行颜色改为白色
	var allPlanDtlRows = $('#planDtlDg').datagrid('getRows');
	var allItemRows = $('#dataGridItem').datagrid('getRows');
	$.each(allItemRows,function(index,item){
		$("#datagrid-row-r4-2-"+index).css("background","white"); 
	});
	
	//验证退仓收货单是否有重复箱号
	$.each(checkItems,function(index,item){
		var rowIndex = $('#dataGridItem').datagrid('getRowIndex',item);
		if(allPlanDtlRows.length > 0){
			$.each(allPlanDtlRows,function(index2,item2){
				if(item.itemNo == item2.itemNo
					&&item.sizeNo == item2.sizeNo
					&&item.cellNo == item2.sCellNo
					&&item.quality == item2.quality
					&&item.itemType == item2.itemType){
					checkResult = false;
					$("#datagrid-row-r4-2-"+rowIndex).css("background","red"); 
					alert("明细表中已有相同的记录!");
				}
			});
		}
	});
	
	return checkResult;
};
//新增
billhmplan.saveMain = function(){
	var fromObj = $('#dataForm');
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
         return;
    }
	wms_city_common.loading("show","正在保存......");
	$.ajax({
		async:false,
		cache:true,
		type: 'POST',
		url: BasePath+'/bill_hm_plan/saveMain',
		data:$("#dataForm").serialize(),
		success: function(data){
			wms_city_common.loading();
			if(data.result=="success"){
		  	 	alert('新增成功!');
				$("#ownerNo").combobox('disable');
				$("#detail-sub-toolbar").show();
				$("#planNo").val(data.planNo);
				$("#btn-save").hide();
				$("#btn-edit").show();
				billhmplan.searchData();
			}else{
				alert("新增失败，请联系管理员",2);
			}
		}
	});
};
//修改
billhmplan.editMain = function(){
	var fromObj = $('#dataForm');
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
         return;
    }
	wms_city_common.loading("show","正在保存......");
	$.ajax({
		async:false,
		cache:true,
		type: 'POST',
		url: BasePath+'/bill_hm_plan/editMain',
		data:$("#dataForm").serialize(),
		success: function(data){
			wms_city_common.loading();
			if(data.result=="success"){
		  	 	alert('修改成功!');
				$("#btn-save").hide();
				$("#btn-edit").show();
				billhmplan.searchData();
			}else{
				//alert("修改失败，请联系管理员",2);
				alert(data.msg,2);
			}
		}
	});
};

//保存单据
billhmplan.saveDetail = function(){
	
	var tempObj = $('#planDtlDg');
	var fromObj = $('#dataForm');
	
	 //验证明细数据是否录入
    var tempFlag = billhmplan.endEdit('planDtlDg');
	if(tempFlag!=""){
		alert(tempFlag);
		return;
	}
	
	var inserted = tempObj.datagrid('getChanges', "inserted");
	var deleted = tempObj.datagrid('getChanges', "deleted");
    var updated = tempObj.datagrid('getChanges', "updated");
    
    var ownerNo = $("input[id=ownerNo]",fromObj).combobox('getValue');
    //var locno = $("input[id=locno]",fromObj).combobox('getValue');
    var planNo = $("input[id=planNo]",fromObj).val();
    var remark = $("input[id=remark]",fromObj).val();
    
    //1.校验必填项
    var fromObj=$('#dataForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return;
    }
    //验证是否选择商品明细
    var allRowPlanDtl = tempObj.datagrid('getRows');
    if(allRowPlanDtl.length < 1){
    	alert("商品明细列表不能为空!");
    	return;
    }
   //校验目标储位的合法性
   if(!billhmplan.checkAllData()){
   		return;
   }
    //3. 保存
    var saveFn = function(returnData){
    	var effectRow = {
        		inserted:JSON.stringify(inserted),
        		deleted:JSON.stringify(deleted),
        		updated:JSON.stringify(updated),
        		"ownerNo":ownerNo,"locno":billhmplan.locno,
    			"planNo":planNo,
    			"createtm":returnData.currentDate19Str,
    			"creator":returnData.loginName,
    			"editor":returnData.loginName,
    			"edittm":returnData.currentDate19Str,
    			"remark":remark
        };
    	
    	//提交保存
    	wms_city_common.loading("show","正在保存明细......");
        $.post(BasePath+'/bill_hm_plan_dtl/saveBillHmPlan', effectRow, function(data) {
        	wms_city_common.loading();
            if(data.result == "success"){
//            	var entity = data.entity;
//            	if(planNo!=''&&planNo!=null){
//            		billhmplan.updateSuccess(entity);
//            	}else{
//                	billhmplan.addSuccess(entity);
//            	}
            	billhmplan.closeWindow('openWindowPlan');
            	alert('保存成功!');
            	$("#ownerNo").combobox('disable');
    			billhmplan.loadDataGrid();
            }else{
            	alert(data.msg,1);
            	return;
            }
        }, "JSON").error(function() {
        	wms_city_common.loading();
        	alert('保存失败!',1);
        });
    };
    
    ajaxRequest(BasePath+'/initCache/getCurrentUser',{},saveFn);
    
};
billhmplan.checkAllData = function(){
	var tempObj = $('#planDtlDg');
	var allData = tempObj.datagrid('getRows');
	var dCellNo;
	var originQty;
	var msg;
	var flag = true;
	for(var i=0,length=allData.length;i<length;i++){
		msg = "第"+(i+1)+"行";
		tempObj.datagrid('endEdit', i);
		dCellNo = allData[i].dCellNo;
		originQty = allData[i].originQty;
		if(originQty<0||originQty==0){
			alert(msg+"计划移库数必须大于1",1);
			flag=false;
			break;
		}
		if(dCellNo==""||dCellNo==null){
			alert(msg+"目的储位不能为空",1);
			flag=false;
				break;
		}
		if(dCellNo==allData[i].sCellNo){
			alert(msg+"来源储位和目的储位不能相同",1);
			flag=false;
			break;
		}
		if(!billhmplan.checkCellNo(dCellNo,msg)){
			//alert(msg+"目的储位不存在",1);
			flag=false;
			break;
		}
	}
	return flag;
};
billhmplan.checkCellNo = function(cellNo,msg){
	var planNo = $("#planNo").val();
	var result = false;
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/bill_hm_plan/checkCellNo4BillHmPlan?cellNo='+cellNo+'&planNo='+planNo,
		success : function(data) {
			if(data.result=="success"){
				result = true;
			}else if(data.result=="fail"){
				alert(msg+data.msg,1);
				result = false;
			}
		}
	});
	return  result;
};

//新增成功操作
billhmplan.addSuccess = function(entity){
	$('#dataForm').form('load',entity);
	//$("#locnoHidden").val(entity.locno);
	$("#ownerNoHidden").val(entity.ownerNo);
	//$("#locno").combobox('disable');
	$("#ownerNo").combobox('disable');
	//$("#divHmPlanDtl").show();
};


//修改成功操作
billhmplan.updateSuccess = function(entity){
	billhmplan.closeWindow('openWindowPlan');
};


//作废移库单
billhmplan.cancelBillHmPlan = function(){
	
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要作废的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要关闭这"+checkedRows.length+"条数据", function (r){  
		
		if(r){
			var delTip = "";
			var typeTip = "";
			var dataList = [];
			$.each(checkedRows, function(index, item){
				var reqParam = {locno:billhmplan.locno,planNo:item.planNo,ownerNo:item.ownerNo};
				dataList[dataList.length] = reqParam;
				
				if(item.businessType == '1'){
					typeTip = "单据:"+item.planNo+";";
					return;
				}
				
				if(item.status == '10'||item.status == '90'||item.status == '91'){
					delTip = "单据:"+item.planNo+";";
					return;
				}
			});   
			
			if(typeTip!=null && typeTip!=""){
				typeTip = typeTip+"是退厂计划移库单，不能关闭！";
	    		alert(typeTip);
	    		return;
	    	}
			
			if(delTip!=null && delTip!=""){
				delTip = delTip+"不能关闭<建单>、<关闭>状态的单据！";
	    		alert(delTip);
	    		return;
	    	}
			
			//提交
			var url = BasePath+ '/bill_hm_plan/cancelBillHmPlan';
			var effectRow = {datas:JSON.stringify(dataList)};
			wms_city_common.loading("show","正在关闭......");
			ajaxRequest(url,effectRow,function(data){
				wms_city_common.loading();
	    		 if(data.result=='success'){
	    			 //4.删除成功,清空表单
	    			 billhmplan.loadDataGrid();
	    			 alert('关闭成功!');
	    		 }else{
	    			 alert(data.msg,2);
	    		 }
			});
		}
		 
    });  
};


//删除移库单
billhmplan.doDelPlan = function(){
	
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
		
		if(r){
			var delTip = "";
			var dataList = [];
			$.each(checkedRows, function(index, item){
				var reqParam = {locno:billhmplan.locno,planNo:item.planNo,ownerNo:item.ownerNo};
				dataList[dataList.length] = reqParam;
				if(item.status!=null&&item.status!=""&&item.status=="13"){
					delTip = "单据:"+item.planNo+";";
				}
			});   
			
			if(delTip!=null && delTip!=""){
				delTip = delTip+"已完结，不能删除！";
	    		alert(delTip);
	    		return;
	    	}
			
			//提交
			var url = BasePath+ '/bill_hm_plan/delBillHmPlan';
			var effectRow = {deleted:JSON.stringify(dataList)};
			wms_city_common.loading("show","正在删除......");
			ajaxRequest(url,effectRow,function(result){
				wms_city_common.loading();
	    		 if(result.flag=='success'){
	    			 //4.删除成功,清空表单
	    			 billhmplan.loadDataGrid();
	    			 alert('删除成功!');
	    		 }else{
	    			 //alert('删除失败,请联系管理员!',2);
	    			 alert(result.msg);
	    		 }
			});
		}
		 
    });  
};




//删除移库单据明细
billhmplan.doDelPlanDtl = function(){
	var checkItems = $('#planDtlDg').datagrid('getChecked');
	if(checkItems.length < 1){
		alert("请选择移库单明细!");
		return;
	}
	$.each(checkItems,function(index,item){
		var i = $('#planDtlDg').datagrid('getRowIndex',checkItems[index]);//获取某行的行号
		$('#planDtlDg').datagrid('deleteRow',i);
	});
};

billhmplan.closeItem = function(){
	billhmplan.closeWindow("openWindowItem");
};


//初始化委托业主代码
billhmplan.ownerNo = {};
billhmplan.initOwnerNo = function(id){	
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["ownerNo","ownerNoCondition","ownerNoCondition2"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[false,true,false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billhmplan.ownerNo,
			null);
};

billhmplan.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
billhmplan.status = {};
billhmplan.statusFormatter = function(value, rowData, rowIndex){
	return billhmplan.status[value];
};
//初始化状态
billhmplan.initStatus = function(data){
	wms_city_common.comboboxLoadFilter(
			["statusCondition","status"],
			null,
			null,
			null,
			true,
			[true, false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_BILL_HM_PLAN',
			{},
			billhmplan.status,
			null);
};
billhmplan.initUsers = function(){
	wms_city_common.comboboxLoadFilter(
			["creatorCondition","auditorCondition"],
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
//初始化用户信息
billhmplan.initCurrentUser = function(){
	var url = BasePath+'/initCache/getCurrentUser';
	ajaxRequestAsync(url,{},function(data){
		billhmplan.locno = data.locno;
	}); 
};

//========================初始化信息======================================
$(document).ready(function(){
	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
	billhmplan.initCurrentUser();
	billhmplan.initStatus();
	//品质
	billhmplan.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',{},false,billhmplan.initQualityData);
	//属性
	billhmplan.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',{},false,billhmplan.initItemTypeData);
	billhmplan.initOwnerNo();
	billhmplan.initUsers();
	//品牌
	wms_city_common.loadBrands('brandNoCondition2', true);
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNoCondition]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	
	$('#planDtlDg').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer != null) {
						if(data.footer[1].isselectsum){
							billhmplan.originQty = data.footer[1].originQty;
			   			}else{
			   				var rows = $('#planDtlDg').datagrid('getFooterRows');
				   			rows[1]['originQty'] = billhmplan.originQty;
				   			$('#planDtlDg').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
	$("#showImportDialog").window({
		onBeforeClose:function(){
			var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
			$.each(checkedRows,function(index,item){
				billhmplan.loadDetail(item,'edit');
				//$('#info_add').hide();
				//$('#info_remove').hide();
			});
		}
	});
	
});
billhmplan.initQualityData = function(data){
	billhmplan.quality=data;
};
billhmplan.initItemTypeData = function(data){
	billhmplan.itemType=data;
};

billhmplan.formatterQuality = function(value){
	for(var i=0;i<billhmplan.quality.length;i++){
		if(value==billhmplan.quality[i].itemvalue){return billhmplan.quality[i].itemname;};
	}
};
billhmplan.formatterItemType = function(value){
	for(var i=0;i<billhmplan.itemType.length;i++){
		if(value==billhmplan.itemType[i].itemvalue){return billhmplan.itemType[i].itemname;};
	}
};
billhmplan.formatterSourceNo = function(value){
	if(value=='N'){
		return '';
	}else{
		return value;
	}
};

//审核
billhmplan.audit = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}
	
	if(checkedRows.length > 1){
		alert('只能选择一条记录!',1);
		return;
	}
	if(checkedRows[0].status!='10'){
		alert("非建单状态的单据不能审核");
		return;
	}
	var rowData = checkedRows[0];
	
	$.messager.confirm("确认","你确定要审核吗？", function (r){  
        if (r) {
        	wms_city_common.loading("show","正在审核......");
			$.ajax({
				  type: 'GET',
				  url:BasePath+'/bill_hm_plan/audit?planNo='+rowData.planNo,
				  cache: true,
				  success: function(data){
					  wms_city_common.loading();
				  	 if(data.result=='success'){
        				 alert('审核成功!');
						billhmplan.searchData();
        		 	}else{
        			 	alert(data.msg,2);
        		 	}
				  }
			});
        }  
    });
};
