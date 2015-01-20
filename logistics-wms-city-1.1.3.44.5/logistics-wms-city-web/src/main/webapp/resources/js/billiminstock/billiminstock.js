var billiminstock = {};
billiminstock.locno;
//状态
billiminstock.statusData= {};

billiminstock.exportDataGridDtl1Id = 'dataGridJG_detail';
billiminstock.preColNamesDtl1 = [
		                   {title:"商品编码",field:"itemNo",width:150},
		                   {title:"商品名称",field:"itemName",width:150},
		                   {title:"预上储位",field:"destCellNo",width:120}
                    ];
billiminstock.endColNamesDtl1 = [
		                      {title:"合计",field:"allCount"}
                        ] ;
billiminstock.sizeTypeFiledName = 'sizeKind';

//页面加载
$(document).ready(function(){
	//创建日期初始为前两天
	$("#createtm_start").datebox('setValue',getDateStr(-2));
	//初始化仓库信息
	billiminstock.initLocData();
	//加载用户信息
	wms_city_common.comboboxLoadFilter(
			["search_instockWorker","search_auditor","search_creator"],
			'workerNo',
			'workerName',
			'valueAndText',
			false,
			[true,true,true],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
	//初始化状态信息
	wms_city_common.comboboxLoadFilter(
			["search_status"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_INSTOCK_STATUS',
			{},
			billiminstock.statusData,
			null);
	//初始化货主
	wms_city_common.comboboxLoadFilter(
			["search_ownerNo"],
			'ownerNo',
			'ownerName',
			'valueAndText',
			false,
			[true],
			BasePath+'/entrust_owner/get_biz',
			{},
			null,
			null);
	//加载列表
	//billiminstock.loadDataGrid();
	//加载品牌
//	billiminstock.loadSysNo();
//	billiminstock.loadBrand($('#searchForm'));
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNo]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	//新增按钮
	$("#info_add").click(billiminstock.save_do);
	//修改按钮
	$("#info_edit").click(billiminstock.edit_do);
	
	$('#dataGridJG_edit').datagrid(
			{
				'onLoadSuccess':function(){
		   			var curObj = $('#dataGridJG_edit');
		   			var rows = curObj.datagrid("getRows");
		   			curObj.datagrid('beginEdit',0);
					/*$.each(rows,function(index,item){
						curObj.datagrid('beginEdit', index);
						if(item.realCellNo==""||item.realCellNo==null){
							var ed = curObj.datagrid('getEditor', {index:index,field:'realCellNo'});
					    	$(ed.target).val(item.destCellNo);
						}
						if(item.realQty==null||item.realQty==""){
							var ed2 = curObj.datagrid('getEditor', {index:index,field:'realQty'});
							$(ed2.target).numberbox('setValue', item.itemQty-item.instockedQty);
						}
					});*/
		   		}
			}
		);
	
	$('#dataGridJG_detail_default').datagrid(
			{
				'onLoadSuccess':function(data){
		   			if(data.footer[1].isselectsum){
		   				billiminstock.itemQty = data.footer[1].itemQty;
		   				billiminstock.realQty = data.footer[1].realQty;
		   				billiminstock.instockedQty = data.footer[1].instockedQty;
		   			}else{
		   				var rows = $('#dataGridJG_detail_default').datagrid('getFooterRows');
			   			rows[1]['itemQty'] = billiminstock.itemQty;
			   			rows[1]['realQty'] = billiminstock.realQty;
			   			rows[1]['instockedQty'] = billiminstock.instockedQty;
			   			$('#dataGridJG_detail_default').datagrid('reloadFooter');
		   			}
		   		}
			}
		);
	
});

//仓库
billiminstock.initLocData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billiminstock.locno = data.locno;
		}
	});
};

billiminstock.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

billiminstock.statusFormatter = function(value,rowData,rowIndex){
	return billiminstock.statusData[value];
};


//查询区域信息
billiminstock.searchArea = function(){
	var instockDate_start =  $('#instockDate_start').datebox('getValue');
	var instockDate_end =  $('#instockDate_end').datebox('getValue');
	if(!isStartEndDate(instockDate_start,instockDate_end)){    
		alert("上架时间开始时间不能大于结束时间");   
        return;   
    }   
	var audittm_start =  $('#audittm_start').datebox('getValue');
	var audittm_end =  $('#audittm_end').datebox('getValue');
	if(!isStartEndDate(audittm_start,audittm_end)){    
		alert("审核日期开始日期不能大于结束日期");   
        return;   
    } 
	
	var createtm_start =  $('#createtm_start').datebox('getValue');
	var createtm_end =  $('#createtm_end').datebox('getValue');
	if(!isStartEndDate(createtm_start,createtm_end)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    } 
    
    
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_im_instock/list.json?locno='+billiminstock.locno;
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

billiminstock.searchLocClear = function(){
	$('#searchForm').form("clear");
	$('#brandNo').combobox("loadData",[]);
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
billiminstock.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bill_im_instock/list.json?locno='+billiminstock.locno,
    			'pageNumber':1
    		});
};

billiminstock.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//详情，编辑
billiminstock.showDetail = function(rowData,rowIndex){
	$('#showDialog').window({
		title:"明细"
	});
	$('#dataForm').form('load',rowData);
	billiminstock.loadDetail(rowData,rowIndex);
};

billiminstock.loadDetail = function(rowData,rowIndex){
	
	billiminstock.curRowIndex=rowIndex;
	
	//billiminstock.initGridHead(rowData.sysNo);
	
	var sysNo = billiminstock.getSysNo(rowData);
	if(sysNo==""){
		return;
	}
	billiminstock.initGridHead(sysNo);
	//billiminstock.loadDataDetailViewGrid(rowData);
	
	billiminstock.loadDataDetailDefaultView(rowData);
	
	$("#showDialog").window('open'); 
};

billiminstock.getSysNo = function(rowData){
	var sysNo=  "";
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/bill_im_instock_dtl/findSysNo?instockNo='+rowData.instockNo,
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

billiminstock.initGridHead = function(sysNo){
     var beforeColArr = billiminstock.preColNamesDtl1;
	 var afterColArr = billiminstock.endColNamesDtl1; 
	 var columnInfo = billiminstock.getColumnInfo(sysNo,beforeColArr,afterColArr);
     $("#"+billiminstock.exportDataGridDtl1Id).datagrid({
         columns:columnInfo.columnArr
     }); 
};

billiminstock.getColumnInfo = function(sysNo,beforeColArr,afterColArr){
	var tempUrl = BasePath+'/initCache/getBrandList.htm';
	var resultData = {};
     $.ajax({
        type: 'POST',
        url: tempUrl,
        data: {
        	        sysNo:sysNo,
        	        preColNames:JSON.stringify(beforeColArr),
        	        endColNames:JSON.stringify(afterColArr),
        	        sizeTypeFiledName:billiminstock.sizeTypeFiledName
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
billiminstock.loadDataDetailViewGrid = function(rowData){
	$('#dataGridJG_detail').datagrid(
			{
				'url':BasePath+'/bill_im_instock_dtl/findDetail?instockNo='+rowData.instockNo+'&locno='+rowData.locno,
				'pageNumber':1 
			}
	);
};


billiminstock.loadDataDetailDefaultView = function(rowData){
	
	var queryMxURL=BasePath+'/bill_im_instock_dtl/getByPage.json';
	var params = {};
	params.instockNo = rowData.instockNo;
	params.ownerNo = rowData.ownerNo;
	params.locno = rowData.locno
    $( "#dataGridJG_detail_default").datagrid( 'options' ).queryParams=params;
    $( "#dataGridJG_detail_default").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_detail_default").datagrid( 'load' );
	/*
	$('#dataGridJG_detail_default').datagrid(
			{
				'url':BasePath+'/bill_im_instock_dtl/getByPage.json?instockNo='+rowData.instockNo+'&locno='+rowData.locno
			}
		);*/
};


//修改
billiminstock.edit = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else if(checkedRows[0].status=='13'){
		alert('上架完成的单据不能修改!',1);
		return;
	}else{
		if(checkedRows[0].status=='13'){
			$("#edit-toolbar").hide();
		}else{
			$("#edit-toolbar").show();
		}
		var rowData = checkedRows[0];
		$('#showEditDialog').window({
			title:"修改"
		});
		$('#showEditDialog').window('open');
		$('#dataFormEdit').form('load',rowData);
		/*$('#dataGridJG_edit').datagrid(
			{
				'url':BasePath+'/bill_im_instock_dtl/dtlList.json?instockNo='+rowData.instockNo+'&locno='+rowData.locno
			}
		);
		*/
		$( "#dataGridJG_edit").datagrid( 'options' ).queryParams={};
	    $( "#dataGridJG_edit").datagrid( 'options' ).url=BasePath+'/bill_im_instock_dtl/dtlList.json?instockNo='+rowData.instockNo+'&locno='+rowData.locno;
	    $( "#dataGridJG_edit").datagrid( 'load' );
	}
	
};
billiminstock.saveByPlan = function(){
	wms_city_common.loading("show","正在保存......");
	$.ajax({
	 	async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
	    url: BasePath+'/bill_im_instock_dtl/saveByPlan',
	    data:{
       	 	instockNo:$("#detailInstockNo").val(),
       	 	ownerNo:$("#detailOwnerNo").val()
	    },
	    success: function(data){
	    	wms_city_common.loading();
	    	if(data.result=="success"){
         		alert("保存成功");
         		$("#dataGridJG_edit").datagrid("load");
         	}else{
         		alert(data.msg,2);
         	}
		}
	});
};
//拆分，确定，删除
billiminstock.d_manage = function(op){
	var selectedRow = $("#dataGridJG_edit").datagrid("getSelected");
	if(selectedRow==null){
		alert('请选择要操作的记录!',1);
		return;
	}
	var rowIndex = $("#dataGridJG_edit").datagrid("getRowIndex",selectedRow);
	if(op=="split"){ //拆分
		billiminstock.d_split(selectedRow,rowIndex);
	}else if(op=="del"){
		billiminstock.d_del(selectedRow,rowIndex);
	}
};
//拆分
billiminstock.d_split = function(rowData,rowIndex){
	$("#dataGridJG_edit").datagrid("endEdit",rowIndex);
	 if(rowData.itemQty==0){
		 alert("已经拆分的不能再拆分",1);
		 return;
	 }
	 
	 if(rowData.realCellNo==null||rowData.realCellNo==""){
		 alert("源数据实际上架储位为空不能拆分",1);
		 return;
	 }
	 if(rowData.realQty==null||rowData.realQty==""||rowData.realQty=="0"){
		 alert("源数据实际上架数量为空不能拆分",1);
		 return;
	 }
	 
	 $("#cellNoForm").form("clear");
	 $("#cellNoDialog").window("open");
	 
	/*var ed = $('#dataGridJG_edit').datagrid('getEditor', {index:rowIndex,field:'realQty'});
	var realQty = 0;
	var itemQty = 0;
	if(ed==null){
		var selectRowCur = $('#dataGridJG_edit').datagrid('getSelected');
		realQty = parseInt(selectRowCur.realQty);
		itemQty = parseInt(selectRowCur.itemQty);
	}else{
		realQty=parseInt($(ed.target).numberbox('getValue'));
		itemQty=parseInt(rowData.itemQty);
	}
	if(realQty<0){
		alert("实际上架数量不能小于0",2);
		return;
	}
	if(itemQty==0){
		alert("已经拆分的不能再拆分",2);
		return;
	}
	if(realQty>=itemQty){
		alert("实际上架数量必须小于可上架数量,才能拆分",2);
		return;
	}
	var allRealQty = billiminstock.d_getAllRealQty();
	
	$('#dataGridJG_edit').datagrid('beginEdit',rowIndex);
	
	if(itemQty<=allRealQty){
		alert("该商品实际上架数量必须小于可上架数量，才能拆分",2);
		return;
	}
	
	var selectRow = $('#dataGridJG_edit').datagrid('getSelected');
	var newRow = {};
	for(p in selectRow){
		newRow[p] = selectRow[p];
	}
	newRow.itemQty = 0;
	newRow.realQty=itemQty-allRealQty;
	newRow.instockId ="";
	newRow.instockedQty = "0";
	$('#dataGridJG_edit').datagrid('insertRow',{
		index: rowIndex+1,	// 索引从0开始
		row:newRow
	});*/
	//return;
};
billiminstock.d_split_do = function(){
	 var selectedRow = $("#dataGridJG_edit").datagrid("getSelected");
	 var rowIndex = $("#dataGridJG_edit").datagrid("getRowIndex",selectedRow);
	 var selectRealCellNo  = $("#selectRealCellNo").val();
	 var selectRealQty = $("#selectRealQty").val();
	 var fromObj=$('#cellNoForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
	 if(selectRealQty<=0){
		 alert("实际上架数量不能为0",1);
		 return;
	 }
	 var oldRealCellNo = selectedRow.realCellNo;
	 var oldRealQty= selectedRow.realQty;
	 selectedRow.realCellNo = selectRealCellNo;
	 selectedRow.realQty = selectRealQty;
	 wms_city_common.loading("show","正在拆分......");
	 $.ajax({
		 	async : false,
			cache : false,
			type : 'POST',
			dataType : "json",
	        url: BasePath+'/bill_im_instock_dtl/splitById',
	        data:selectedRow,
	        success: function(data){
	        	wms_city_common.loading();
	        	 selectedRow.realCellNo = oldRealCellNo;
	        	 selectedRow.realQty = oldRealQty;
	         	if(data.result=="success"){
	         		$('#dataGridJG_edit').datagrid('insertRow',{
	         			index: rowIndex+1,	// 索引从0开始
	         			row:data.dtl
	         		});
	         		$("#cellNoDialog").window("close");
	         	}else{
	         		alert(data.msg,2);
	         	}
			}
	 });
}

//获取剩余上架数量
billiminstock.d_getAllRealQty = function(selectRow){
	//获取选中的列
	if(selectRow ==null){
		selectRow = $('#dataGridJG_edit').datagrid('getSelected');
	}
	var itemNo = selectRow.itemNo;
	var sizeNo = selectRow.sizeNo;
	var destCellNo = selectRow.destCellNo;
	var destCellId = selectRow.destCellId;
	var labelNo = selectRow.labelNo;
	//获取当前页所有的列
	var allRows = $('#dataGridJG_edit').datagrid('getRows');
	var allQty = 0;
	for(var i = 0,length = allRows.length;i<length;i++){
		$('#dataGridJG_edit').datagrid('endEdit',i);
		var curData = allRows[i];
		if(curData.itemNo==itemNo&&sizeNo==curData.sizeNo&&destCellNo ==curData.destCellNo&&destCellId==curData.destCellId&&labelNo==curData.labelNo){
			allQty = parseInt(allQty)+parseInt(curData.realQty);
		}
	}
	return allQty;
}

//查询实际上架数量
billiminstock.d_findRealyQty = function(rowData){
	var realQty='';
	 $.ajax({
	 	async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
        url: BasePath+'/bill_im_instock_dtl/getById',
        data:{
        	 instockNo:rowData.instockNo,
        	 locno:rowData.locno, 
        	 ownerNo:rowData.ownerNo, 
        	 instockId:rowData.instockId
        },
        success: function(result){
         	realQty = result.resultData.realQty;
		}
      });
      return realQty;
};
//更新实际上架储位 实际上架数量
billiminstock.d_save = function(){
	var selectRow = $('#dataGridJG_edit').datagrid('getSelected');
	if(selectRow==null){
		alert("请选择要保存的",1);
		return;
	}
	var rowIndex = $('#dataGridJG_edit').datagrid('getRowIndex',selectRow);
	$('#dataGridJG_edit').datagrid('endEdit',rowIndex);
	selectRow = $('#dataGridJG_edit').datagrid('getSelected');
	if(selectRow.realQty<0){
		alert("实际上架数量不能小于0",1);
		return;
	}
	if(selectRow.realCellNo==""){
		alert("实际上架储位不能为空",1);
		return;
	}
	wms_city_common.loading("show","正在保存......");
	$.ajax({
	 	async : false,
		cache : false,
		type : 'POST',
		dataType : "json",
	    url: BasePath+'/bill_im_instock_dtl/saveSingle',
	    data:selectRow,
	    success: function(data){
	    	wms_city_common.loading();
	     	if(data.result=="success"){
	     		$('#dataGridJG_edit').datagrid("load");
	     		alert("保存成功");
	     	}else{
	     		alert(data.msg,2);
	     	}
		}
	});
};
//删除行
billiminstock.d_del = function(rowData,rowIndex){
	$.messager.confirm('确认','您确认要删除该记录吗？',function(r){    
		if (r){ 
			 if(rowData.instockId==""){
				 $('#dataGridJG_edit').datagrid('deleteRow',rowIndex);
			 }else{
				 wms_city_common.loading("show");
				 $.ajax({
					 	async : false,
						cache : false,
						type : 'POST',
						dataType : "json",
					    url: BasePath+'/bill_im_instock_dtl/deleteById',
					    data:{
					    	 instockNo:rowData.instockNo,
					    	 locno:rowData.locno, 
					    	 ownerNo:rowData.ownerNo, 
					    	 instockId:rowData.instockId
					    },
					    success: function(data){
					    	wms_city_common.loading();
					    	if(data.result=="success"){
					     		//删除行
					     		$('#dataGridJG_edit').datagrid('deleteRow',rowIndex);
					     	}else{
					     		alert(data.msg,2);
					     	}
						}
					}); 
			 }
		}    
	}); 
};
billiminstock.check = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要审核的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能审核一条记录!',1);
		return;
	}else if(checkedRows[0].status!='10'&&checkedRows[0].status!='12'){
		alert('只有新建和部分上架的单据才能审核!',1);
		return;
	}else{
		var rowData = checkedRows[0];
		$.messager.confirm("确认","您确定要审核这条信息吗？", function (r){
			if(r){
				wms_city_common.loading("show","正在审核......");
				$.ajax({
					 	async : false,
						cache : false,
						type : 'POST',
						dataType : "json",
					    url: BasePath+'/bill_im_instock/audit',
					    data:{
					    	 instockNo:rowData.instockNo,
					    	 locno:rowData.locno, 
					    	 ownerNo:rowData.ownerNo
					    },
					    success: function(data){
					    	wms_city_common.loading();
					     	if(data.result=="success"){
					     		alert("审核成功");
					     		billiminstock.searchArea();
					     	}else{
					     		alert(data.msg,2);
					     	}
						}
					});
			}
		});
	}
};

//批量打印
billiminstock.printDetail = function(){

	var resultData;
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].instockNo+"|"+rows[i].ownerNo);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_im_instock_dtl/printDetail',
        data: {
        	keys:keys.join("_"),
        	locno:billiminstock.locno
        },
        success: function(result){
        	resultData = result;
		}
      });
     if(resultData.result!="success"){
     	alert(resultData.msg,2);
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

billiminstock.exportDetail = function(){
	exportExcelRepInfo('dataGridJG_detail_default','/bill_im_instock_dtl/do_export','上架回单明细导出');
};

