
var billconconvertgoods = {};

//所有仓库
billconconvertgoods.allLocno = [];

//加载Grid数据Utils
billconconvertgoods.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//清空
billconconvertgoods.searchClear = function(id){
	$('#'+id).form("clear");
	$('#brandNo').combobox("loadData",[]);
};

//清空
billconconvertgoods.searchCheckClear = function(id){
	$('#'+id).form("clear");
	var convertType = $('#convertType').combobox('getValue');
	if(convertType == '0'){
		$('#qualityCheckSearch').combobox('setValue','0');
	}
	$('#brandNoCheckSearch').combobox("loadData",[]);
};


//关闭窗口
billconconvertgoods.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};

//插入行，开始编辑
billconconvertgoods.insertRowAtEnd = function(gid,rowData){
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

//验证明细数据是否验证通过
billconconvertgoods.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		tempObj.datagrid('endEdit', i);
    	}else{
    		alert('数据验证没有通过!',1);
    		return false;
    	}
    }
    return true;
};

//加载客户
billconconvertgoods.loadStoreName = function(inputId,storeNo){
	var url = BasePath+'/store/get_biz';
	ajaxRequestAsync(url,{storeNo:storeNo},function(data){
		if(data != null){
			var columns = {storeNo:data[0].storeNo};
			$(inputId).combogrid("grid").datagrid("load", columns);
		}
	}); 
};

//查询主表数据
billconconvertgoods.searchData = function(){
	//1.校验必填项
    var fromObj=$('#searchForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_con_convert_goods/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billconconvertgoods.locno;
	billconconvertgoods.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};

//查询主表数据
billconconvertgoods.searchCheckData = function(){
	
	//1.校验必填项
    var fromObj=$('#searchCheckForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
    
    var convertType = $("#convertType").combobox('getValue');
	var fromObjStr=convertArray($('#searchCheckForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_check/findBillUmCheckByPage.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = billconconvertgoods.locno;
	if(convertType == '0'){
		reqParam['quality'] = '0';
	}
	//reqParam['status'] = "11";
	billconconvertgoods.loadGridDataUtil('dataGridCheck', queryMxURL, reqParam);
};


//选择窗口
billconconvertgoods.add = function(){
	
	$("#dataForm").form('clear');
	$("#addUI").window('open');
	$('#save_main').show();
	$('#edit_main').hide();
	$('#addDtlBtn').show();
	
	//初始化转货类型，如果是修改或者查看明细全部把类型初始化进去
	billconconvertgoods.initEditStatus('0','convertType');
	billconconvertgoods.convertTypeSH('0');
	var ownerNos = $('#ownerNo').combobox('getData');
	$('#ownerNo').combobox('setValue',ownerNos[0].ownerNo);
	
	$('#convertType').combobox('select','0');
	$('#sQuality').combobox('select','0');
	$('#dQuality').combobox('select','A');
	$('#sQualityHid').val('0');
	$('#dQualityHid').val('A');
	$('#sQuality').combobox('disable');
	$('#dQuality').combobox('disable');
	
	$('#convertType').combobox('enable');
	$('#ownerNo').combobox('enable');
	$('#storeNoLocno').combobox('enable');
	$('#storeNo').combobox('enable');
	$('#convertGoodsDtlDg').datagrid('loadData', { total: 0, rows: [] });
	$('#storeNo').combogrid("grid").datagrid({ url:BasePath+'/store/list.json?storeType=11',queryParams:{storeType:"11",storeNoDc:billconconvertgoods.locno},method:"post"});

};

//打开修改窗口
billconconvertgoods.edit = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status!='10'){
		alert(checkedRows[0].convertGoodsNo+'非建单状态的单据不能修改!',1);
		return ;
	}else{
		
		var rowData = checkedRows[0];
		if(rowData.convertType == '1' || rowData.convertType == '3'){
			billconconvertgoods.initDLocnoStoreNo('storeNo',rowData.dLocno);
			rowData.storeNoLocno = rowData.dLocno;
		}else if(rowData.convertType == '2'){
			billconconvertgoods.initDLocnoStoreNo('storeNo',billconconvertgoods.locno);
			billconconvertgoods.loadStoreName('#storeNo', rowData.storeNo);
		}
		//初始化转货类型，如果是修改或者查看明细全部把类型初始化进去
		billconconvertgoods.initEditStatus('1','convertType');
		
		$("#addUI").window('open');
		$('#dataForm').form('load',rowData);
		
		$('#save_main').hide();
		$('#edit_main').show();
		if(rowData.convertType == '2' || rowData.convertType == '3'){
			$('#addDtlBtn').hide();
		}else{
			$('#addDtlBtn').show();
		}
		
		$('#sQuality').combobox('select',rowData.sQuality);
		$('#dQuality').combobox('select',rowData.dQuality);
		$('#sQuality').combobox('disable');
		$('#dQuality').combobox('disable');
		$('#ownerNo').combobox('disable');
		$('#convertType').combobox('disable');
		$('#storeNoLocno').combobox('disable');
		$('#storeNo').combobox('disable');
		
		billconconvertgoods.convertTypeSH(rowData.convertType);
		var queryMxURL=BasePath+ '/bill_con_convert_goods_dtl/findConvertGoodsDtlGroupByCheckByPage.json';
		var queryParams={locno:rowData.locno,ownerNo:rowData.ownerNo,convertGoodsNo:rowData.convertGoodsNo};
		billconconvertgoods.loadGridDataUtil('convertGoodsDtlDg', queryMxURL, queryParams);
	}
};

//修改主档信息
billconconvertgoods.doEditMain = function(){
	
	var locno = billconconvertgoods.locno;
	var ownerNo = $("#ownerNo").combobox("getValue");
	var convertGoodsNo = $("#convertGoodsNo").val();
	var remark = $("#remark").val();
	var paramData = {
		locno:locno,
		ownerNo:ownerNo,
		convertGoodsNo:convertGoodsNo,
		remark:remark
	};
	
    wms_city_common.loading("show","正在保存....");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:paramData,
		url:BasePath+'/bill_con_convert_goods/updateMain',
		success : function(data) {
			if(data.result=="success"){
				alert('修改成功!');
				$('#dataGridJG').datagrid('load');
				billconconvertgoods.closeWindow('addUI'); //关闭窗口
			}else{
				//alert('修改失败,请联系管理员!',2);
				alert(data.msg,2);
			}
			wms_city_common.loading();
		}
	});
};


//删除库存转货单
billconconvertgoods.doDel = function(){
	var selectRows = $('#dataGridJG').datagrid('getChecked');
	if(selectRows.length < 1){
		alert("请至少选择一条数据!");
		return;
	}
	
	var dataList = [];
	var tipStr = "";
	$.each(selectRows, function(index, item){
		if(item.status!="10"){
			tipStr = item.convertGoodsNo;
			return false;
		}
		dataList[dataList.length]={locno:item.locno,ownerNo:item.ownerNo,convertGoodsNo:item.convertGoodsNo};
	}); 
	if(tipStr!=""&&tipStr!=null){
		alert(tipStr+"非建单状态不能删除!");
		return;
	}
	
	$.messager.confirm("确认","你确定要删除这"+selectRows.length+"条数据", function (r){  
        if (r) {
            wms_city_common.loading('show','删除中,请稍候...');
            var url = BasePath+'/bill_con_convert_goods/delMain';
        	var effectRow = {datas:JSON.stringify(dataList)};
        	$.post(url, effectRow, function(data) {
        		if(data.result == "success"){
        			wms_city_common.loading();
        			alert('删除成功!');
                	$('#dataGridJG').datagrid('load');
            	}else if(data.result=='fail'){
            		wms_city_common.loading();
                	alert(data.msg,1);
                	return;
                }else{
                	wms_city_common.loading();
                	alert('操作异常！',1);
                	return;
                }
        	}, "JSON").error(function() {
            	wms_city_common.loading();
            	alert('删除失败!',1);
            });
        }  
    });  
};

//审核
billconconvertgoods.auditConvertGoods = function(){
	var selectRows = $('#dataGridJG').datagrid('getChecked');
	if(selectRows.length < 1){
		alert("请至少选择一条数据!");
		return;
	}
	
	var dataList = [];
	var tipStr = "";
	$.each(selectRows, function(index, item){
		if(item.status=="11"){
			tipStr = item.convertGoodsNo;
			return false;
		}
		dataList[dataList.length] = {
			locno : item.locno,
			ownerNo : item.ownerNo,
			convertGoodsNo : item.convertGoodsNo,
			convertType : item.convertType,
			storeNo : item.storeNo
		};
	}); 
	if(tipStr!=""&&tipStr!=null){
		alert(tipStr+"审核状态的单据不能审核!");
		return;
	}
	
	$.messager.confirm("确认","你确定要审核这"+selectRows.length+"条数据", function (r){  
        if (r) {
            wms_city_common.loading('show','审核中,请稍候...');
            var url = BasePath+'/bill_con_convert_goods/auditConvertGoods';
        	var effectRow = {datas:JSON.stringify(dataList)};
        	$.post(url, effectRow, function(data) {
        		if(data.result == "success"){
        			wms_city_common.loading();
        			alert('审核成功!');
                	$('#dataGridJG').datagrid('load');
            	}else if(data.result=='fail'){
            		wms_city_common.loading();
                	alert(data.msg,1);
                	return;
                }else{
                	wms_city_common.loading();
                	alert('操作异常！',1);
                	return;
                }
        	}, "JSON").error(function() {
            	wms_city_common.loading();
            	alert('审核失败!',1);
            });
        }  
    });  
};

//验收单选择窗口
billconconvertgoods.openCheckSlect = function(){
	
	var convertGoodsNo = $("#convertGoodsNo").val();
	if(convertGoodsNo==""||convertGoodsNo==null){
		alert("请先增加主档信息!");
		return false;
	}
	
	$("#checkUI").window('open');
	var convertType = $("#convertType").combobox('getValue');
	if(convertType == '0'){
		$("#qualityCheckSearch").combobox('select','0');
		$("#qualityCheckSearch").combobox('disable');
	}else{
		$("#qualityCheckSearch").combobox('select','');
		$("#qualityCheckSearch").combobox('enable');
	}
	$('#dataGridCheck').datagrid('loadData', { total: 0, rows: [] });
};


//选择串码商品
billconconvertgoods.selectCheck = function(){
	var checkRow = $('#dataGridCheck').datagrid('getChecked');
	if(checkRow.length < 1){
		alert("请选择验收单!");
		return;
	}
	
	$.messager.confirm("确认","你确定要添加这"+checkRow.length+"条数据?", function (r){  
        if (r) {
        	wms_city_common.loading('show');
        	$.each(checkRow,function(index,item){
        		var reqParams = {
        				locno:item.locno,
        				ownerNo:item.ownerNo,
        				checkNo:item.checkNo,
        				sourceNo:item.sourceNo,
        				itemQty:item.itemQty,
        				realQty:item.checkQty,
        				remark:item.remark,
        				quality:item.quality,
        				itemType:item.itemType
        		};
        		
        		//插入商品到父窗口,开始编辑
        		billconconvertgoods.insertRowAtEnd('convertGoodsDtlDg',reqParams);
        		//关闭load提示
        		wms_city_common.loading();
        	 });
        	
        	//关闭商品选择窗口
        	billconconvertgoods.closeWindow('checkUI');
        	//$('#convertGoodsDtlDg').datagrid('beginEdit');
        }
	});
	
};

//保存退仓验收任务单
billconconvertgoods.doSaveMain = function(){
	
	var convertType = $('#convertType').combobox('getValue');
	if(convertType == '1'){
		var storeNoLocno = $('#storeNoLocno').combobox('getValue');
		if(storeNoLocno==""||storeNoLocno==null){
			alert("目的仓库不能为空!");
			return;
		}
	}else if(convertType == '2'){
		var storeNo = $('#storeNo').combobox('getValue');
		if(storeNo==""||storeNo==null){
			alert("目的门店不能为空!");
			return;
		}
	}
	
	var fromObj=$('#dataForm');
	var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
    var reqParam = fromObj.serialize();
    wms_city_common.loading("show");
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
		data:reqParam,
		url:BasePath+'/bill_con_convert_goods/saveMain',
		success : function(data) {
			wms_city_common.loading();
			if(data.result=="success"){
				alert('新增成功!');
				$("#convertGoodsNo").val(data.convertGoodsNo);
				$("#convertType").combobox('disable');
				$("#ownerNo").combobox('disable');
				$("#save_main").hide();
	 			$("#edit_main").show();
	 			$('#dataGridJG').datagrid('load');
			}else{
				alert('新增失败,请联系管理员!',2);
			}
		}
	});
	
};


//保存明细
billconconvertgoods.saveCheckSlect = function(){
	
	var tempObj = $('#convertGoodsDtlDg');
	var inserted = tempObj.datagrid('getChanges', "inserted");
	var deleted = tempObj.datagrid('getChanges', "deleted");
	
	//校验重复的记录   
	var checkRows = tempObj.datagrid('getRows');
	if(checkRows.length > 0){
		var hashObject = {};
		for (var i = 0; i < checkRows.length; i++) {
	    	var checkNo = checkRows[i]['checkNo'];
	   	 	if (hashObject[checkNo]){
	       		 alert('验收单号【'+checkNo+'】重复！',1);
	       		 return;
	        } else {
	            hashObject[checkNo] = true;
	        }
	    }
	}else{
		alert("明细为空,不能保存!");
		return;
	}
	
	var deletedList = [];
	if(deleted.length > 0){
		$.each(deleted,function(index,item){
			var prarms = {locno:item.locno,ownerNo:item.ownerNo,checkNo:item.checkNo};
			deletedList[deletedList.length] = prarms;
		});
	}
	
	wms_city_common.loading("show","正在保存明细......");
	$.ajax({
		  async : true,
		  cache: true,
		  type: 'POST',
		  url: BasePath+'/bill_con_convert_goods_dtl/saveConvertGoodsDtl',
		  data: {
			locno:billconconvertgoods.locno,
			ownerNo:$("#ownerNo").combobox("getValue"),
			convertGoodsNo:$("#convertGoodsNo").val(),
			convertType:$("#convertType").combobox('getValue'),
		  	inserted:JSON.stringify(inserted),
		  	deleted:JSON.stringify(deletedList)
		  },
		  success: function(data){
		  	 if(data.result=='success'){
		  		alert("保存成功");
		  		$("#convertGoodsDtlDg").datagrid('load',{locno:billconconvertgoods.locno,
					ownerNo:$("#ownerNo").combobox("getValue"),
					convertGoodsNo:$("#convertGoodsNo").val()});
				$('#dataGridJG').datagrid('load');
				$("#convertGoodsDtlDg").datagrid("acceptChanges");
		 	}else{
			 	alert(data.msg,2);
		 	}
		  	wms_city_common.loading();
		  }
	});
};

//删除明细
billconconvertgoods.delCheckSelect = function(){
	var obj = $('#convertGoodsDtlDg');
	var checkRow = obj.datagrid('getChecked');
	if(checkRow==null||checkRow.length < 1){
		alert("请选择要删除的记录",1);
		return;
	}
	$.each(checkRow,function(index,item){
		var index = obj.datagrid('getRowIndex', item);
		$('#convertGoodsDtlDg').datagrid('deleteRow',index);
	});
};


////审核
//billconconvertgoods.auditConvertGoods = function(){
//	var selectRows = $('#dataGridJG').datagrid('getChecked');
//	if(selectRows.length < 1){
//		alert("请至少选择一条数据!");
//		return;
//	}
//	
//	var dataList = [];
//	var tipStr = "";
//	$.each(selectRows, function(index, item){
//		if(item.status!="10"){
//			tipStr = item.convertGoodsNo;
//			return false;
//		}
//		dataList[dataList.length]={locno:item.locno,ownerNo:item.ownerNo,convertGoodsNo:item.convertGoodsNo};
//	}); 
//	if(tipStr!=""&&tipStr!=null){
//		alert(tipStr+"非建单状态不能审核!");
//		return;
//	}
//	
//	$.messager.confirm("确认","你确定要审核这"+selectRows.length+"条数据", function (r){  
//        if (r) {
//            wms_city_common.loading('show','审核中,请稍候...');
//            var url = BasePath+'/bill_con_convert_goods/delMain';
//        	var effectRow = {datas:JSON.stringify(dataList)};
//        	$.post(url, effectRow, function(data) {
//        		if(data.result == "success"){
//        			wms_city_common.loading();
//        			alert('删除成功!');
//                	$('#dataGridJG').datagrid('load');
//            	}else if(data.result=='fail'){
//            		wms_city_common.loading();
//                	alert(data.msg,1);
//                	return;
//                }else{
//                	wms_city_common.loading();
//                	alert('操作异常！',1);
//                	return;
//                }
//        	}, "JSON").error(function() {
//            	wms_city_common.loading();
//            	alert('删除失败!',1);
//            });
//        }  
//    });  
//};


//查看详情
billconconvertgoods.loadDetail = function(rowData){
	$("#detailUI").window('open');
	$('#main_order_dtlId').tabs('select',0);
	if(rowData.convertType == '1'||rowData.convertType == '3'){
		billconconvertgoods.initDLocnoStoreNo('storeNoView',rowData.dLocno);
		rowData.storeNoLocno = rowData.dLocno;
	}else if(rowData.convertType == '2'){
		billconconvertgoods.initDLocnoStoreNo('storeNoView',billconconvertgoods.locno);
		billconconvertgoods.loadStoreName('#storeNoView', rowData.storeNo);
	}
	//初始化转货类型，如果是修改或者查看明细全部把类型初始化进去
	billconconvertgoods.initEditStatus('1','convertTypeView');
	$('#dataFormView').form('load',rowData);
	$("#ownerNoView").combobox('disable');

	$('#sQualityView').combobox('select',rowData.sQuality);
	$('#dQualityView').combobox('select',rowData.dQuality);
	
	billconconvertgoods.convertTypeViewSH(rowData.convertType);
	var queryMxURL=BasePath+ '/bill_con_convert_goods_dtl/findConvertGoodsDtlGroupByCheckByPage.json';
	var queryParams={locno:rowData.locno,ownerNo:rowData.ownerNo,convertGoodsNo:rowData.convertGoodsNo};
	billconconvertgoods.loadGridDataUtil('order_dtl1_dataGrid', queryMxURL, queryParams);
	$('#order_dtl2_dataGrid').datagrid('loadData', { total: 0, rows: [] });
	
	//显示所有的验收单明细
	billconconvertgoods.loadDetail4Main(rowData);
};

//查看详情
billconconvertgoods.loadDetail4Main = function(rowData){
	//$('#main_order_dtlId').tabs('select',1);
	var queryMxURL=BasePath+ '/bill_con_convert_goods_dtl/dtl_list.json';
	var queryParams = {
		locno : rowData.locno,
		ownerNo : rowData.ownerNo,
		//checkNo : rowData.checkNo,
		convertGoodsNo : rowData.convertGoodsNo
	};
	billconconvertgoods.loadGridDataUtil('order_dtl2_dataGrid', queryMxURL, queryParams);
};

//控制转货类型显示
billconconvertgoods.convertTypeChange = function(){
	var convertType = $("#convertType").combobox('getValue');
	billconconvertgoods.convertTypeSH(convertType);
};

//新增修改显示隐藏
billconconvertgoods.convertTypeSH = function(convertType){
	if(convertType=='0'){
		$("#st1").hide();
		$("#s1").hide();
		$("#st2").hide();
		$("#s2").hide();
		$("#qt1").show();
		$("#qt2").show();
		$("#q1").show();
		$("#q2").show();
	}else{
		$("#qt1").hide();
		$("#qt2").hide();
		$("#q1").hide();
		$("#q2").hide();
		if(convertType == '1'){
			$("#st1").show();
			$("#s1").show();
			$("#st2").hide();
			$("#s2").hide();
		}else if(convertType == '2'){
			$("#st2").show();
			$("#s2").show();
			$("#st1").hide();
			$("#s1").hide();
		}else if(convertType == '3'){
			$("#st2").show();
			$("#s2").show();
			$("#st1").show();
			$("#s1").show();
		}
	}
};

//详情显示隐藏
billconconvertgoods.convertTypeViewSH = function(convertType){
	if(convertType=='0'){
		$("#v_st1").hide();
		$("#v_s1").hide();
		$("#v_st2").hide();
		$("#v_s2").hide();
		$("#v_qt1").show();
		$("#v_qt2").show();
		$("#v_q1").show();
		$("#v_q2").show();
	}else{
		$("#v_qt1").hide();
		$("#v_qt2").hide();
		$("#v_q1").hide();
		$("#v_q2").hide();
		if(convertType == '1'){
			$("#v_st1").show();
			$("#v_s1").show();
			$("#v_st2").hide();
			$("#v_s2").hide();
		}else if(convertType == '2'){
			$("#v_st2").show();
			$("#v_s2").show();
			$("#v_st1").hide();
			$("#v_s1").hide();
		}else if(convertType == '3'){
			$("#v_st2").show();
			$("#v_s2").show();
			$("#v_st1").show();
			$("#v_s1").show();
		}
	}
};

//委托业主
billconconvertgoods.ownerFormatter = function(value, rowData, rowIndex){
	return billconconvertgoods.ownnerData[value];
};

// 初始化当前登录的用户 信息
billconconvertgoods.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		billconconvertgoods.loginName = data.loginName;
		billconconvertgoods.locno = data.locno;
		billconconvertgoods.locname = data.locname;
	});
};

//初始化状态
billconconvertgoods.initStatus = function(data){
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CON_CONVERT_GOODS_STATUS',
			{},
			null,
			null);
};

//初始化委托业主
billconconvertgoods.ownnerData = {};
billconconvertgoods.initOwnerNo = function(){
	wms_city_common.comboboxLoadFilter(
			["ownerNoCondition","ownerNo","ownerNoView","ownerNoCheckSearch"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[true, false, false, false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billconconvertgoods.ownnerData,
			null);
};

billconconvertgoods.initUsers = function(){
	wms_city_common.comboboxLoadFilter(
			["creatorCondition","auditorCondition","creatorCheckSearch","auditorCheckSearch"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true,true,true,true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
};

//退仓类型
billconconvertgoods.initConvertType = function(){
	wms_city_common.comboboxLoadFilter(
			["convertTypeCondition"],
			null,
			null,
			null,
			true,
			[true, false, false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CON_CONVERT_GOODS_CONVERT_TYPE',
			{},
			null,
			null);
	
};

billconconvertgoods.initQuality = function(){
	wms_city_common.comboboxLoadFilter(
			["qualityCheckSearch","sQuality","dQuality","sQualityView","dQualityView"],
			null,
			null,
			null,
			true,
			[true,false,false,false,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			null,
			null);
};

billconconvertgoods.initItemType = function(){
	wms_city_common.comboboxLoadFilter(
			["itemTypeCheckSearch"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=ITEM_TYPE',
			{},
			null,
			null);
};



billconconvertgoods.otherLocno = [];
billconconvertgoods.initOtherLocno = function(){
	var temp = {};
	for(var idx=0;idx<billconconvertgoods.allLocno.length;idx++){
		if(billconconvertgoods.allLocno[idx].organizationNo != billconconvertgoods.locno){
			temp = billconconvertgoods.allLocno[idx];
			temp.valueAndText = temp.organizationNo + '→' + temp.storeName;
			billconconvertgoods.otherLocno[billconconvertgoods.otherLocno.length] = temp;
		}
	}
	
	
	
//	$("#storeNo").combobox({
//		data:billconconvertgoods.otherLocno,
//		valueField:'organizationNo',
//		textField:'valueAndText',
//		panelHeight:150,
//		loadFilter:function(data){
//			var first = {};
//			first['organizationNo'] = '';
//			first['valueAndText'] = '全选';
//			var tempData = [];
//			tempData[tempData.length] = first;
//	    	for(var i=0;i<data.length;i++){
//	    		tempData[tempData.length] = data[i];
//	    	}
//	    	return tempData;
//		}
//	});
	
	
};

//初始化仓别
billconconvertgoods.initStoreLocno = function(){
	$("#storeNoLocno,#storeNoLocnoView,#storeNoLocnoCondition").combobox({
		data:billconconvertgoods.otherLocno,
		valueField:'organizationNo',
		textField:'valueAndText',
		panelHeight:150
	});
};

//初始化门店
billconconvertgoods.initStoreNo = function(){
	$('#storeNo,#storeNoView,#storeNoCondition').combogrid({
		delay: 1500,
		panelWidth:450,   
        idField:'storeNo',  
        textField:'textFieldName',   
        pagination : true,
        rownumbers:true,
        mode: 'remote',
        url:BasePath+'/store/list.json?storeType=11&status=0&storeNoDc='+billconconvertgoods.locno,
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
};

//初始化门店
billconconvertgoods.initDLocnoStoreNo = function(id,dlocno){
	$('#'+id).combogrid({
		delay: 1500,
		panelWidth:450,   
        idField:'storeNo',  
        textField:'textFieldName',   
        pagination : true,
        rownumbers:true,
        mode: 'remote',
        url:BasePath+'/store/list.json?storeType=11&status=0&storeNoDc='+dlocno,
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
};

//修改，查看初始化 0 新增  1 修改
billconconvertgoods.initEditStatus = function(type,id){
	var url = BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CON_CONVERT_GOODS_CONVERT_TYPE';
	billconconvertgoods.ajaxRequest(url,{},false,function(data){
		if(data != null){
			var tempData = [];
			for(var idx=0;idx<data.length;idx++){
				if(type=='1'){
					tempData[tempData.length] = data[idx];
				}else{
					if(data[idx].itemvalue != '2' && data[idx].itemvalue != '3'){
						tempData[tempData.length] = data[idx];
					}
				}
			}
			$('#'+id).combobox({
				 data:tempData,
			     valueField:"itemvalue",
			     textField:"itemnamedetail",
			     panelHeight:"auto"
			});
		}
	}); 
};

billconconvertgoods.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//初始化转货类型
billconconvertgoods.initGoodsConvertTypes = function(data){
	var tempData = [];
	 for(var idx=0;idx<data.length;idx++){
		 if(data[idx].itemvalue != '2' && data[idx].itemvalue != '3'){
			 tempData[tempData.length] = data[idx];
		 }
	 }
	$('#convertTypeCondition').combobox({
		 data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	$('#convertType').combobox({
		 data:tempData,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
	$('#convertTypeView').combobox({
		 data:data,
	     valueField:"itemvalue",
	     textField:"itemnamedetail",
	     panelHeight:"auto"
	 });
//	var temp = {};
//	for(var index=0;index<data.length;index++){
//		temp[data[index].itemvalue] = data[index].itemname;
//	}
//	billsmwaste.types = temp;
};

//初始化信息
$(document).ready(function(){
	$("#startCreatetmCondition").datebox('setValue',getDateStr(-2));
	billconconvertgoods.initCurrentUser();
	billconconvertgoods.initStatus();
	billconconvertgoods.initOwnerNo();
	billconconvertgoods.initUsers();
	billconconvertgoods.initConvertType();
	billconconvertgoods.initQuality();
	billconconvertgoods.initItemType();
	billconconvertgoods.ajaxRequest(BasePath+'/user_organization/findUserOrganization',{},false,function(u){billconconvertgoods.allLocno=u.list;});
	billconconvertgoods.initOtherLocno();
	billconconvertgoods.initStoreLocno();
	billconconvertgoods.initStoreNo();
	
	//转货类型
	//billconconvertgoods.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CON_CONVERT_GOODS_CONVERT_TYPE',{},false,billconconvertgoods.initGoodsConvertTypes);
	
	//加载品牌
	var objs = [];
	objs.push({"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')});//查询
	wms_city_common.loadSysNo4Cascade(objs);
	
	var objs2 = [];
	objs2.push({"sysNoObj":$('#sysNoCheckSearch'),"brandNoObj":$('#brandNoCheckSearch')});
	wms_city_common.loadSysNo4Cascade(objs2);
	
	
	$('#order_dtl1_dataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data != null) {
						if(data.footer != null){
							if(data.footer[1].isselectsum){
								billconconvertgoods.itemQty = data.footer[1].itemQty;
								billconconvertgoods.realQty = data.footer[1].realQty;
				   			}else{
				   				var rows = $('#order_dtl1_dataGrid').datagrid('getFooterRows');
					   			rows[1]['itemQty'] = billconconvertgoods.itemQty;
					   			rows[1]['realQty'] = billconconvertgoods.realQty;
					   			$('#order_dtl1_dataGrid').datagrid('reloadFooter');
				   			}
						}
					}
		   			
		   		}
			}
	);
	
	$('#order_dtl2_dataGrid').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data != null) {
						if(data.footer != null){
							if(data.footer[1].isselectsum){
								billconconvertgoods.itemQtyDtl = data.footer[1].itemQty;
								billconconvertgoods.realQtyDtl = data.footer[1].realQty;
				   			}else{
				   				var rows = $('#order_dtl2_dataGrid').datagrid('getFooterRows');
					   			rows[1]['itemQty'] = billconconvertgoods.itemQtyDtl;
					   			rows[1]['realQty'] = billconconvertgoods.realQtyDtl;
					   			$('#order_dtl2_dataGrid').datagrid('reloadFooter');
				   			}
						}
					}
		   			
		   		}
			}
	);
	
	
	
});

billconconvertgoods.printDetail4SizeHorizontal = function (){
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	wms_city_common.loading("show","正在加载......");
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		var params = "";
		params += rows[i].locno+"|"+rows[i].convertGoodsNo+"|"+rows[i].ownerNo+"|"+rows[i].convertTypeStr;
		params += "|"+rows[i].storeNo+"|"+rows[i].locnoName+"|"+rows[i].storeName;
		keys.push(params);
		
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_con_convert_goods_dtl/printDetail4SizeHorizontal',
        data: {
        	keys:keys.join(",")
        },
        success: function(data){
        	wms_city_common.loading();
        	if(data.result=="success"){
        		print4SizeHorizontal(data);
        	}else if(data.result == "other"){
        		alert(data.msg);
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
	for(var idx=0;idx<data.pages.length;idx++){
		LODOP.NewPageA();
		var headHtml = createHeadHtml(data.pages[idx]);
		var bodyHtml = createBodyHtml(data.pages[idx]);
		//设置表格内容
		LODOP.ADD_PRINT_TABLE(120,0,"100%",380,strStyle+bodyHtml);
		LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
		//设置表格头部
		LODOP.ADD_PRINT_HTM(10,10,"100%",130,headHtml);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
		//设置条码
		LODOP.ADD_PRINT_BARCODE(50,10,250,40,"128A",data.pages[idx].convertGoodsNo);
		LODOP.SET_PRINT_STYLEA(0,"ItemType",1);
		LODOP.SET_PRINT_STYLEA(0,"LinkedItem",-1);
	}
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
	LODOP.PREVIEW();
}
function createHeadHtml(page){
	var name = "";
	if(page.storeName != null && page.storeName != "" ){
		name = page.storeName + "("+page.storeNo+")";
	}
	var html = "<table style='width:100%;font-size:13px;'>";
	html += "<tr><td style='text-align:center;font-size:35px;' colspan='4'>库存转换单</td></tr>";
	html += "<tr><td>单&nbsp;&nbsp;&nbsp;&nbsp;号："+page.convertGoodsNo+"</td><td>转换类型："+page.convertTypeStr+"" +
			"</td><td>发货方："+billconconvertgoods.locname+"</td><td>总合计："+page.total+"</td></tr>";
	html += "<td>目的仓库："+page.locnoName+"</td><td>目的门店："+name+"</td>";
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
	html += "<td rowspan='"+rowspan+"'>商品编码</td><td rowspan='"+rowspan+"'>商品名称</td>";
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
	html = html + "<tfoot><tr><td style='text-align:13px;text-align:center;' colspan='"+(sizeColNum+3)+"'><font tdata='pageNO' format = '#' align='center'>第#页</font>&nbsp;/&nbsp;<font format='#' tdata='pageCount'>共#页</font></td></tr></tfoot>";
	html += "</table>";
	return html;
}

