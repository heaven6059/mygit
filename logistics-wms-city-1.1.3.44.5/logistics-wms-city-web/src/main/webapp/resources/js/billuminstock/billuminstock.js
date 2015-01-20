var billuminstock = {};
billuminstock.locno;
billuminstock.user;

billuminstock.exportDataGridDtl1Id = 'dataGridJG_detail';
billuminstock.lookupcode_untread_type = "ITEM_TYPE";
billuminstock.preColNamesDtl1 = [
		                   {title:"商品编码",field:"itemNo",width:120},
		                   {title:"商品名称",field:"itemName",width:150},
		                   {title:"颜色",field:"colorName",width:120},
		                   {title:"预上储位",field:"destCellNo",width:120},
		                   {title:"实际上架储位",field:"realCellNo",width:150}
                    ];
billuminstock.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billuminstock.sizeTypeFiledName = 'sizeKind';

billuminstock.dataGridJGFooter = {};
//页面加载
$(document).ready(function(){
	$("#createtm").datebox('setValue',getDateStr(-2));
	//初始化仓别信息
	billuminstock.initLocData();
	//加载用户信息
	billuminstock.initWorker();
	//初始化状态信息
	billuminstock.initStatus();
	//初始化业主信息
	billuminstock.initOwnerData();
	//加载列表
	//billuminstock.loadDataGrid();
	//加载品牌

	//新增按钮
	$("#info_add").click(billuminstock.save_do);
	//修改按钮
	$("#info_edit").click(billuminstock.edit_do);
	billuminstock.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},function(u){billuminstock.user=u;});
	
	billuminstock.initItemType();
	billuminstock.ajaxRequest(BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',{},billuminstock.initQuality);
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('#brandNo')}//查询
			);
	wms_city_common.loadSysNo4Cascade(objs);
	$('#dataGridJG_edit').datagrid(
			{
				'onLoadSuccess':function(){
					var curObj = $('#dataGridJG_edit');
		   			var rows = curObj.datagrid("getRows");
		   			var length = rows.length;
		   			if(length==0){
		   				return;
		   			}
		   			curObj.datagrid('beginEdit', 0);
		   		}
			});
	
	$('#dataGridJG_edit').datagrid(
			{
				'onLoadSuccess':function(data){
					if(data.footer!=null){
						if(data.footer[1].isselectsum){
			   				billuminstock.itemQty = data.footer[1].itemQty;
			   				billuminstock.realQty = data.footer[1].realQty;
			   			}else{
			   				var rows = $('#dataGridJG_edit').datagrid('getFooterRows');
				   			rows[1]['itemQty'] = billuminstock.itemQty;
				   			rows[1]['realQty'] = billuminstock.realQty;
				   			$('#dataGridJG_edit').datagrid('reloadFooter');
			   			}
					}
		   		}
			}
		);
	$('#dataGridJG').datagrid({
			'onLoadSuccess':function(data){
				if(data.footer!=null){
					if(data.footer[1].isselectsum){
						billuminstock.dataGridJGFooter = data.footer[1];
		   			}else{
		   				var rows = $('#dataGridJG').datagrid('getFooterRows');
		   				rows[1] = billuminstock.dataGridJGFooter;
			   			$('#dataGridJG').datagrid('reloadFooter');
		   			}
				}
	   		}
		});
});

billuminstock.typeData={};
billuminstock.initItemType = function(){
	wms_city_common.comboboxLoadFilter(
			["itemType_search"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode='+billuminstock.lookupcode_untread_type,
			{},
			billuminstock.typeData,
			null);
};

billuminstock.converStr2JsonObj2= function(data){
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
billuminstock.qualityDataObj ={};
billuminstock.initQuality = function(data){
	wms_city_common.comboboxLoadFilter(
			["quality_search"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=AREA_QUALITY',
			{},
			billuminstock.qualityDataObj,
			null);
};
//仓别
billuminstock.initLocData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billuminstock.locno = data.locno;
		}
	});
};

billuminstock.initWorker = function(){
	wms_city_common.comboboxLoadFilter(
			["search_auditor","search_creator","instockWorker_info"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true,true, false],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
};

//状态信息

billuminstock.statusFormatter = function(value,rowData,rowIndex){
	return billuminstock.status[value];
};

billuminstock.ownerNoFormatter= function(value,rowData,rowIndex){
	return billuminstock.ownnerData[value];
};
billuminstock.status = {};
billuminstock.initStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["search_status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_UM_INSTOCK_STATUS',
			{},
			billuminstock.status,
			null);
};

billuminstock.converStr2JsonObj= function(data){
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

billuminstock.ownerFormatter = function(value, rowData, rowIndex){
	return billuminstock.ownnerData[value];
};

//查询区域信息
billuminstock.searchArea = function(){
	
	/*var instockDate =  $('#instockDate').datebox('getValue');
	var instockDate_end =  $('#instockDate_end').datebox('getValue');
	if(!isStartEndDate(instockDate,instockDate_end)){    
		alert("上架时间开始时间不能大于结束时间");   
        return;   
    }*/
    var audittm =  $('#audittm').datebox('getValue');
	var audittm_end =  $('#audittm_end').datebox('getValue');
	if(!isStartEndDate(audittm,audittm_end)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    }
    
    var createtm =  $('#createtm').datebox('getValue');
	var createtm_end =  $('#createtm_end').datebox('getValue');
	if(!isStartEndDate(createtm,createtm_end)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_um_instock/json4List.json?locno='+billuminstock.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
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

billuminstock.searchLocClear = function(){
	$('#brandNo').combobox("loadData",[]);
	$('#searchForm').form("clear");
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

//加载数据
billuminstock.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bill_um_instock/json4List.json?locno='+billuminstock.locno,
    			'pageNumber':1
    		});
};

billuminstock.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//详情，编辑
billuminstock.edit = function(rowData){
	$('#editDialog').window({
		title:"明细"
	});
	var url = BasePath+'/bill_um_instock_dtl/dtl_list.json';
	var params = {
			locno:billuminstock.locno,
			instockNo:rowData.instockNo,
			sort:"item_no,size_no,dest_cell_no"
	};
};
billuminstock.disableModifyBut = function(yes){
	$("#ownerNo").combobox("disable");;
	if(yes){
		$("#d_split").linkbutton('disable');
		$("#plan_save").linkbutton('disable');
		$("#d_save").linkbutton('disable');
		$("#d_delete").linkbutton('disable');
		$("#main_save").linkbutton('disable');
		$("#select_instockWorker_but").linkbutton('disable');
	}else{
		$("#d_split").linkbutton('enable');
		$("#plan_save").linkbutton('enable');
		$("#d_save").linkbutton('enable');
		$("#d_delete").linkbutton('enable');
		$("#main_save").linkbutton('enable');
		$("#select_instockWorker_but").linkbutton('enable');
	}
	
};
billuminstock.check = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择记录!',1);
		return;
	}
	if(checkedRows.length > 1){
		alert('只能选择一条记录!',1);
		return;
	}
	if(checkedRows[0].status!='10'){
		alert('只有建单状态的单据才能审核',1);
		return;
	}
	var params = {
			locno:billuminstock.locno,
			instockNo:checkedRows[0].instockNo,
			ownerNo:checkedRows[0].ownerNo,
			audittm:billuminstock.user.currentDate19Str,
			auditor:billuminstock.user.loginName
	};
	$.messager.confirm("确认","您确定要审核这条单据码？", function (r){
		if(!r){
			return;
		}
		var url = BasePath+'/bill_um_instock/check';
		wms_city_common.loading("show","正在审核......");
		$.post(url, params, function(result) {
			wms_city_common.loading();
	        if(result.result == 'success'){
	            alert('审核成功!',1);
	            $("#dataGridJG").datagrid("load");
	        }else{
	        	alert(result.result+",审核失败!",2);
	        	return;
	        }
	    }, "JSON").error(function() {
	    	wms_city_common.loading();
	    	alert('审核失败!',1);
	    });
	});
	
};
//修改
billuminstock.showEdit = function(type,data){
	var title = '明细';
	var curData = data
	if(type=="edit"){
		title = '修改';
		var checkedRows = $("#dataGridJG").datagrid("getChecked");
		/**/if(checkedRows.length < 1){
			alert('请选择记录!',1);
			return;
		}
		if(checkedRows.length > 1){
			alert('只能选择一条记录!',1);
			return;
		}
		if(checkedRows[0].status != '10'){
			alert('只能选择【新建】状态的单据!',1);
			return;
		}
		curData = checkedRows[0];
		billuminstock.disableModifyBut(false);
	}else{
		billuminstock.disableModifyBut(true);
	}
	$('#editDialog').window({
		title:title
	});
	
	$("#editDialog").window('open');
	$("#editDataForm").form("load",curData);
	var url = BasePath+'/bill_um_instock_dtl/dtl_list.json';
	var params = {
			locno:billuminstock.locno,
			instockNo:curData.instockNo,
			sort:"itemNo,sizeNo,destCellNo,instockId"
	};
	var tempObj = $("#dataGridJG_edit")
	tempObj.datagrid( 'options' ).queryParams = params;
	tempObj.datagrid( 'options' ).url = url;
	tempObj.datagrid( 'load' );
	 
};
function getDtl(row){
	var url = BasePath+'/bill_um_instock_dtl/list.json';
	var params = {
			locno:billuminstock.locno,
			instockNo:row.instockNo
	};
	$.ajax({
		  async:false,
		  type: 'POST',
		  url: url,
		  data: params,
		  cache: true,
		  success: function(result){
			return result;
		  },
		  error:function(){
			alert("连接异常!",2);  
		  },
	});
		
		
};
billuminstock.main_save = function(){
	var ownerNo = $("#ownerNo").combobox('getValue');
	var instockWorker = $("#instockWorker").val();
	var instockNo = $("#instockNo").val();
	if(instockWorker == ''){
		alert("请选择上架人!");
		return;
	}
	var result = false;
	ajaxRequestAsync(BasePath+'/bill_um_instock/get', {locno:billuminstock.locno,ownerNo:ownerNo,instockNo:instockNo}, function(data){
		if(data.status!='10'){
			result = true;
		}
	});
	if(result){
		alert("单据："+instockNo+"状态已改变，保存失败！");
		return;
	}
	var params = {
			instockNo:instockNo,
			ownerNo:ownerNo,
			instockWorker:instockWorker,
			locno:billuminstock.locno
	};	
	var url = BasePath+'/bill_um_instock/put';
	$.messager.confirm('确认','您确定保存主信息吗？',function(r){
		if(r){
			wms_city_common.loading("show","正在保存主信息......");
			$.post(url, params, function(result) {
		        if(result != null){
		            alert('主信息保存成功!',1);
		            $("#dataGridJG_edit").datagrid('load');
		        }
		        wms_city_common.loading();
		    }, "JSON").error(function() {
		    	alert('主信息保存失败!',1);
		    	wms_city_common.loading();
		    });
		}
	});
};

//按计划保存
billuminstock.plan_save = function(){
	var instockNo = $("#instockNo").val();
	var ownerNo = $("#ownerNo").combobox('getValue');
	var params = {
			instockNo:instockNo,
			locno:billuminstock.locno,
			ownerNo:ownerNo
	};
	var url = BasePath+'/bill_um_instock_dtl/plan_save';
	$.messager.confirm('确认','您确定按计划保存吗？',function(r){
		if(r){
			wms_city_common.loading("show","正在按计划保存......");
			$.post(url, params, function(result) {
		        if(result.result == 'success'){
		            alert('保存成功!',1);
		            $("#dataGridJG_edit").datagrid('load');
		        }else{
		        	alert(result.result+",保存失败!",2);
		        }
		        wms_city_common.loading();
		    }, "JSON").error(function() {
		    	alert('保存失败!',1);
		    	wms_city_common.loading();
		    });
		}
	});
	
}
billuminstock.endNowEdit = function(id,key){
	var obj = $("#"+id);
	var row = obj.datagrid('getSelected');
	var rows = obj.datagrid('getRows');
	if(row == null){
		return;
	}
	var idx = 0;
	if(rows == null || rows.length == 0){
		return idx;
	}else{
		var len = rows.length;
		for(;idx<len;idx++){
			if(row[key] == rows[idx][key]){
				break;
			}
		}
	}
	obj.datagrid('endEdit',idx);
};
billuminstock.single_save = function(){
	billuminstock.endNowEdit('dataGridJG_edit','instockId');
	var row = $("#dataGridJG_edit").datagrid('getSelected');
	if(row == null){
		alert("请选择需要保存的数据!");
		return;
	}
	var realCellNo = row.realCellNo;
	var realQty = row.realQty;
	if(realCellNo == null || realCellNo == ""){
		alert("请输入实际上架储位!");
		return;
	}
	if(realQty < row.instockedQty){
		alert("实际上架数量必须大于"+row.instockedQty+"!");
		return;
	}
	var url = BasePath+'/bill_um_instock_dtl/single_save';
	$.messager.confirm('确认','您确定保存此记录吗？',function(r){
		if(r){
			var params = {
					locno:row.locno,
					ownerNo:row.ownerNo,
					instockNo:row.instockNo,
					instockId:row.instockId,
					itemNo:row.itemNo,
					sizeNo:row.sizeNo,
					boxNo:row.boxNo,
					sourceNo:row.sourceNo,
					cellNo:row.cellNo,
					cellId:row.cellId,
					destCellId:row.destCellId,
					destCellNo:row.destCellNo,
					realCellNo:row.realCellNo,
					realQty:row.realQty,
			};
			wms_city_common.loading("show","正在保存......");
			$.post(url, params, function(result) {
		        if(result.result == 'success'){
		            alert('保存成功!',1);
		            //$("#dataGridJG_edit").datagrid('load');
		        }else{
		        	alert(result.result+",保存失败!",2);
		        }
		        wms_city_common.loading();
		    }, "JSON").error(function() {
		    	alert('保存失败!',1);
		    	wms_city_common.loading();
		    });
		}
	});
};
billuminstock.open_split = function(){
	//billuminstock.endNowEdit('dataGridJG_edit','instockId');
	var row = $("#dataGridJG_edit").datagrid('getSelected');
	if(row == null){
		alert("请选择需要拆分的数据!");
		return;
	}
	var itemQty = row.itemQty;
	if(itemQty <= 0){
		alert("只能拆分源数据!");
		return;
	}
	$("#cellNoForm").form("clear");
	$("#cellNoDialog").window("open");
};
billuminstock.openSelectInstockWorker = function(){
	$("#instockWorkerDataGridJG_edit").datagrid('clearChecked');
	var instockWorker = $('#instockWorker').val();
	$("#instockWorkerDialog").window("open");
	if(instockWorker == ''){
		return;
	}
	var array = instockWorker.split(",");
	var allRow = $("#instockWorkerDataGridJG_edit").datagrid('getRows');
	for(var idx1=0;idx1<array.length;idx1++){
		for(var idx2=0;idx2<allRow.length;idx2++){
			if(array[idx1] == allRow[idx2].workerNo){
				$("#instockWorkerDataGridJG_edit").datagrid('checkRow',idx2);
				break;
			}
		}
	}
};
billuminstock.selectInstockWorkerOk = function(){
	var checkedRows = $("#instockWorkerDataGridJG_edit").datagrid('getChecked');
	if(checkedRows == null || checkedRows.length == 0){
		alert("请选择上架人!");
		return;
	}
	var workerStr = '';
	for(var idx=0;idx<checkedRows.length;idx++){
		if(idx == (checkedRows.length-1)){
			workerStr += checkedRows[idx].workerNo;
		}else{
			workerStr += checkedRows[idx].workerNo + ",";
		}
	}
	$('#instockWorker').val(workerStr);
	billuminstock.closeWindow('instockWorkerDialog');
};
billuminstock.single_split = function(){
	var row = $("#dataGridJG_edit").datagrid('getSelected');
	var realCellNo  = $("#selectRealCellNo").val();
	var realQty = $("#selectRealQty").val();
	//1.校验必填项
	var fromObj=$('#cellNoForm');
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	if(realQty<=0){
		alert("实际上架数量不能为0",1);
		return;
	}
	var url = BasePath+'/bill_um_instock_dtl/single_split';
	$.messager.confirm('确认','您确定拆分此记录吗？',function(r){
		if(r){
			var params = {
					locno:row.locno,
					ownerNo:row.ownerNo,
					instockNo:row.instockNo,
					instockId:row.instockId,
					itemNo:row.itemNo,
					sizeNo:row.sizeNo,
					boxNo:row.boxNo,
					sourceNo:row.sourceNo,
					cellNo:row.cellNo,
					cellId:row.cellId,
					destCellId:row.destCellId,
					destCellNo:row.destCellNo,
					realCellNo:realCellNo,
					realQty:realQty,
			};
			wms_city_common.loading("show","正在拆分......");
			$.post(url, params, function(result) {
		        if(result.result == 'success'){
		            alert('拆分成功!',1);
		            $("#cellNoDialog").window("close");
		            $("#dataGridJG_edit").datagrid('load');
		        }else{
		        	alert(result.result+",拆分失败!",2);
		        }
		        wms_city_common.loading();
		    }, "JSON").error(function() {
		    	alert('拆分失败!',1);
		    	wms_city_common.loading();
		    });
		}
	});
};
billuminstock.single_delete = function(){
	billuminstock.endNowEdit('dataGridJG_edit','instockId');
	var row = $("#dataGridJG_edit").datagrid('getSelected');
	if(row == null){
		alert("请选择需要删除的数据!");
		return;
	}
	if(row.itemQty != null && row.itemQty > 0){
		alert("不能删除源数据!");
		return;
	}
	var url = BasePath+'/bill_um_instock_dtl/single_delete';
	$.messager.confirm('确认','您确定删除此记录吗？',function(r){
		if(r){
			var params = {
					locno:row.locno,
					ownerNo:row.ownerNo,
					instockNo:row.instockNo,
					instockId:row.instockId
			};
			wms_city_common.loading("show","正在删除......");
			$.post(url, params, function(result) {
		        if(result.result == 'success'){
		            alert('删除成功!',1);
		            $("#dataGridJG_edit").datagrid('load');
		        }else{
		        	alert(result.result+",删除失败!",2);
		        }
		        wms_city_common.loading();
		    }, "JSON").error(function() {
		    	alert('删除失败!',1);
		    	wms_city_common.loading();
		    });
		}
	});
};
//检验实际上架储位和实际上架数量
billuminstock.validate = function(){
	var tempObj = $('#dataGridJG_edit');
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
//比较计划数量和实际数量
billuminstock.validateQty = function(){
	var planQty = {};
	var realQty = {};
	var str = '';
	var temp = {};
	var tempObj = $('#dataGridJG_edit');
	var rowArr = tempObj.datagrid('getRows');
	for (var i = 0; i < rowArr.length; i++) {
		temp = rowArr[i];
		
		//itemNo==rows[i].itemNo && sizeNo==rows[i].sizeNo && boxNo==rows[i].boxNo && rows[i].itemQty > 0
		//&& sourceNo == rows[i].sourceNo && destCellNo == rows[i].destCellNo && destCellId == rows[i].destCellId
		str = temp.itemNo + "_" + temp.sizeNo + "_" + temp.boxNo + "_" + temp.sourceNo + "_" + temp.destCellNo + "_" + temp.destCellId;
		if(planQty[str] == null){
			planQty[str] = temp.itemQty - 0;
		}else{
			planQty[str] = planQty[str] + (temp.itemQty - 0);
		}
		if((temp.realQty - 0)<=0){
			 alert("商品:"+temp.itemNo+"</br>尺码:"+temp.sizeNo+"</br>箱号:"+temp.boxNo+"</br>存在实际上架数量不大于0的数据!",2);
			  return false;
		}
		if(realQty[str] == null){
			realQty[str] = temp.realQty - 0;
		}else{
			realQty[str] = realQty[str] + (temp.realQty - 0);
		}
	}
	for(var key in planQty){  
		  if(realQty[key] > planQty[key]){
			  var keys = key.split("_");
			  alert("商品:"+keys[0]+"</br>尺码:"+keys[1]+"</br>箱号:"+keys[2]+"</br>实际上架数量之和不能大于计划数量!",2);
			  return false;
		  }
	}
	return true;
};
billuminstock.loadDetail = function(rowData,rowIndex){
	
	billuminstock.curRowIndex=rowIndex;
	var sysNo = billuminstock.getSysNo(rowData);
	if(sysNo==""){
		return;
	}
	billuminstock.initGridHead(sysNo);
	billuminstock.loadDataDetailViewGrid(rowData);
	$("#showDialog").window('open'); 
};
billuminstock.getSysNo = function(rowData){
	var sysNo=  "";
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/bill_um_instock_dtl/findSysNo?instockNo='+rowData.instockNo,
		success : function(data) {
			if(data.result=="success"){
				sysNo = data.sysNo;
			}else{
				alert(data.msg);
			}
		}
	});
	return sysNo;
};

billuminstock.initGridHead = function(sysNo){
     var beforeColArr = billuminstock.preColNamesDtl1;
	 var afterColArr = billuminstock.endColNamesDtl1; 
	 var columnInfo = billuminstock.getColumnInfo(sysNo,beforeColArr,afterColArr);
     $("#"+billuminstock.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
};

billuminstock.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billuminstock.sizeTypeFiledName
        },
        cache: true,
        async: false,
        success: function(returnData){
         	resultData.columnArr = returnData.returnCols;
         	resultData.startType = returnData.startType;
         	resultData.maxType = returnData.maxType;
		}
      });
      return resultData;
};

//加载明细
billuminstock.loadDataDetailViewGrid = function(rowData){
};
//加载业主信息
billuminstock.ownnerData = {};
billuminstock.initOwnerData = function(){
	wms_city_common.comboboxLoadFilter(
			["search_ownerNo","ownerNoInfo","ownerNo"],
			"ownerNo",
			"ownerName",
			"ownerName",
			false,
			[true,false, false, false],
			BasePath+'/entrust_owner/get_biz',
			{},
			billuminstock.ownnerData,
			null);
};

//关闭窗口
billuminstock.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};

billuminstock.typeFormatter  = function(value, rowData, rowIndex){
	return billuminstock.typeData[value];
};

billuminstock.qualityFormatter  = function(value, rowData, rowIndex){
	return billuminstock.qualityDataObj[value];
};

//批量打印
billuminstock.printDetail = function(){

	var resultData;
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].instockNo+"|"+rows[i].instockWorker+"|"+rows[i].instockDate);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_um_instock_dtl/printDetail',
        data: {
        	keys:keys.join("_"),
        	locno:billuminstock.locno
        },
        success: function(result){
        	resultData = result;
		}
      });
     if(resultData.result!="success"){
     	alert(resultData.msg);
     	return;
     }else{
    	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
      	if(LODOP==null){
      		return;
      	}
 		LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");
		var result = resultData.data;
		for(var i=0,length=result.length;i<length;i++){
			LODOP.NewPage();
	     	var html = result[i];
			LODOP.ADD_PRINT_HTM(0,0,"100%","100%",html);
			//LODOP.ADD_PRINT_BARCODE(10,10,250,40,"128A",result[i].recheckNo);
		}
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
		LODOP.PREVIEW();
     }
};
billuminstock.onClickRowReceiptDtl = function(rowIndex,rowData){
	var curObj = $("#dataGridJG_edit");
	curObj.datagrid('beginEdit', rowIndex);
	var instockedQty = rowData.instockedQty;
	var ed = curObj.datagrid('getEditor', {index:rowIndex,field:'realCellNo'});
	if(instockedQty > 0){
		$(ed.target).attr("readonly",true);
	}else{
		$(ed.target).attr("readonly",false);
	}
}