
var planQuery = {};
planQuery.showType;

//查询
planQuery.searchData = function(){
	var startCreatetmCondition = $('#createTmStartCondition').datebox('getValue');
	var endCreatetmCondition = $('#createTmEndCondition').datebox('getValue');
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
	
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_om_outstock/list.json';
	var reqParam = eval("(" +fromObjStr+ ")");
	reqParam['locno'] = planQuery.locno;
	reqParam['outstockType'] = 4;
	planQuery.loadGridDataUtil('dataGridJG', queryMxURL, reqParam);
};


//清空
planQuery.searchClear = function(id){
	$('#'+id).form("clear");
	$('#brandNoCondition').combobox("loadData",[]);
};


//刷新数据列表
planQuery.reload = function(){
	$('#dataGridJG').datagrid('reload');
};


//加载dataGrid
planQuery.loadDataGrid = function(){
	planQuery.searchData();
};

//查看详情
planQuery.loadDetail = function(rowData,type){
	
	planQuery.openWindow('openWindowPlan', '修改/查看');
	planQuery.edBtn(rowData.status);
	
	//$('#status').combobox('disable');
	//$('#status').combobox('setValue',rowData.status);
	$('#outstockNo').val(rowData.outstockNo);
	$("#status").val(rowData.status);
	$("#checkBtn").hide();
	if(type=="edit"){
		planQuery.showType = "edit";
		$("#checkBtn").show();
	}else{
		planQuery.showType = "show";
		$("#checkBtn").hide();
	}
	var queryParams = {outstockNo:rowData.outstockNo,locno:planQuery.locno};
	var queryMxURL = BasePath+'/bill_om_outstock_dtl/getListByPage.json';
	planQuery.loadGridDataUtil('outstockDtlDg', queryMxURL, queryParams);
	
};


//编辑明细行
planQuery.selectRow = function(index,rowData){
	var status = $('#status').val();
	if(status != '13'&&planQuery.showType=="edit"){
		$('#outstockDtlDg').datagrid('beginEdit', index);
		var ed = $('#outstockDtlDg').datagrid('getEditor', {index:index,field:'realQty'});
		var realQty; 
		if(rowData.realQty==null||rowData.realQty==""){
			realQty=rowData.itemQty;
		}else{
			realQty=rowData.realQty;
		}
		$(ed.target).focus();
	    $(ed.target).val(realQty);
	}
};


//确认修改
planQuery.editDetail = function(){
	
	// 获取勾选的行
	var checkedRows = $("#outstockDtlDg").datagrid("getRows");
	//验证明细数据是否录入
    var tempFlag = planQuery.endEdit('outstockDtlDg');
	if(tempFlag!=""){
		alert(tempFlag);
		return;
	}
	var outstockNo=$("#outstockNo").val();
	
	var dataList = [];
	$.each(checkedRows, function(index, item){
		var reqParam = {
						locno:item.locno,
						ownerNo:item.ownerNo,
						divideId:item.divideId,
						outstockNo:item.outstockNo,
						realQty:item.realQty,
						dCellNo:item.dCellNo
						};
		dataList[dataList.length] = reqParam;
	}); 
	
	 if(!planQuery.checkAllData()){
   		return;
  	 }
	 
		$.messager.confirm("确认","该操作将保存明细的修改，并完成单据的审核，是否确认？", function (r){
			if(!r){
				return;
			}
			var url = BasePath+'/bill_om_outstock_dtl/editDetail';
			var effectRow = {
			datas : JSON.stringify(dataList),
			outstockNo : outstockNo
		};
			wms_city_common.loading("show","正在保存......");
			$.post(url, effectRow, function(data) {
				wms_city_common.loading();
		    	if(data.result=='success'){
		    		alert('确认成功!');
		    		planQuery.loadDataGrid();
		    		planQuery.closeWindow('openWindowPlan');//关闭窗口
				}else{
					alert(data.msg,2);
				}
		    }, "JSON").error(function() {
		    	alert('确认异常,请联系管理员!',2);
		    });			
		});	 
};

planQuery.audit = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");
	if(checkedRows.length < 1){
		alert('请选择要移库的记录!',1);
		return;
	}
	if(checkedRows.length > 1){
		alert('只能选择一条记录!',1);
		return;
	}
	if(checkedRows[0].status!='10'&&checkedRows[0].status!='30'){
		alert('非建单、下架完成状态的数据不能修改',1);
		return;
	}
	$.messager.confirm("确认","你确定要审核这条数据吗", function (r){  
		if(!r){
			return;
		}
		
		var url = BasePath+'/bill_om_outstock_dtl/omPlanOutStockAudit';
		wms_city_common.loading("show","正在审核......");
		$.ajax({
			  type: 'POST',
			  url: url,
			  data: {
			  	outstockNo:checkedRows[0].outstockNo
			  },
			  cache: true,
			  async:false, // 一定要
			  success: function(data){
				  wms_city_common.loading();
				  if(data.result=="success"){
				  	 alert('审核成功!');
				  	 planQuery.searchData();
				  }else{
				  	alert(data.msg);
				  }
			  }
	     	});
		
	});
};


planQuery.checkAllData = function(){
	var tempObj = $('#outstockDtlDg');
	var allData = tempObj.datagrid('getRows');
	var dCellNo;
	var realQty;
	var msg;
	var flag = true;
	for(var i=0,length=allData.length;i<length;i++){
		msg = "第"+(i+1)+"行";
		tempObj.datagrid('endEdit', i);
		dCellNo = allData[i].dCellNo;
		realQty = allData[i].realQty;
		if(realQty<0||realQty==0){
			alert(msg+"实际库数必须大于0",1);
			flag=false;
			break;
		}
//		if(dCellNo==""||dCellNo==null){
//			alert(msg+"目的储位不能为空",1);
//			flag=false;
//				break;
//		}
//		if(dCellNo==allData[i].sCellNo){
//			alert(msg+"来源储位和目的储位不能相同",1);
//			flag=false;
//			break;
//		}
//		if(!planQuery.checkCellNo(dCellNo)){
//			alert(msg+"目的储位不存在",1);
//			flag=false;
//			break;
//		}
	}
	return flag;
};
planQuery.checkCellNo = function(cellNo){
	var result = false;
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/bill_hm_plan/checkCellNo4BillHmPlan?cellNo='+cellNo,
		success : function(data) {
			if(data.result=="success"){
				result = true;
			}
		}
	});
	return  result;
};


//验证编辑行
planQuery.endEdit = function(gid){
	var tempObj = $('#'+gid);
	var rowArr = tempObj.datagrid('getRows');
    for (var i = 0; i < rowArr.length; i++) {
    	if(tempObj.datagrid('validateRow', i)){
    		var realQty = tempObj.datagrid('getEditor', {index:i,field:'realQty'});
    		if(realQty!=null){
    			var itemNo = rowArr[i].itemNo;
    			var itemQty = rowArr[i].itemQty;
            	if(realQty.target.val() > itemQty){
            		$(realQty.target).focus();
            		return "商品"+itemNo+":实际数量不能大于计划数量;";
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


//加载Grid数据Utils
planQuery.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};


//打开窗口
planQuery.openWindow = function(windowId,opt){
	$('#'+windowId).window({
		title:opt
	});
	$('#'+windowId).window('open');
};


//关闭窗口
planQuery.closeWindow = function(windowId){
	$('#'+windowId).window('close');
};

//禁用或启用确认移库控件
planQuery.edBtn = function(status){
	if(status == '13'){
		$('#btnMove').linkbutton('disable');
	}else{
		$('#btnMove').linkbutton('enable');
	}
};

//格式化状态
planQuery.columnStatusFormatter = function(value, rowData, rowIndex){
	return planQuery.init_status_Text[value];
};

//初始化状态
planQuery.init_status_Text = {};
planQuery.initStatus = function(){
	wms_city_common.comboboxLoadFilter(
			["statusCondition"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=OUTSTOCK_STATUS',
			{},
			planQuery.init_status_Text,
			null);
};

//将数组封装成一个map
planQuery.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};


//初始化当前登录的用户 信息
planQuery.initCurrentUser = function(){
	ajaxRequestAsync(BasePath+'/initCache/getCurrentUser', {}, function(data){
		planQuery.userid = data.userid;
		planQuery.loginName = data.loginName;
		planQuery.currentDate = data.currentDate19Str;
		planQuery.locno = data.locno;
	});
};

planQuery.initUsers = function(){
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

//初始化信息
$(document).ready(function(){
	//planQuery.initStatus('status');
	$("#createTmStartCondition").datebox('setValue',getDateStr(-2));
	planQuery.initStatus();
	planQuery.initCurrentUser();
	planQuery.initUsers();
	
	var objs = [];
	objs.push(
			{"sysNoObj":$('#sysNoSearch'),"brandNoObj":$('input[id=brandNoCondition]',$('#searchForm'))}
			);
	wms_city_common.loadSysNo4Cascade(objs);
	
	//总计
	$('#outstockDtlDg').datagrid({
		'onLoadSuccess' : function(data) {
			if(data.rows.length > 0){
				if (data.footer[1].isselectsum) {
					planQuery.itemQty = data.footer[1].itemQty;
					planQuery.realQty = data.footer[1].realQty;
				} else {
					var rows = $('#outstockDtlDg').datagrid('getFooterRows');
					rows[1]['itemQty'] = planQuery.itemQty;
					rows[1]['realQty'] = planQuery.realQty;
					$('#outstockDtlDg').datagrid('reloadFooter');
				}
			}
			
		}
	});
});

//验证是否可以编辑
planQuery.checkIsEdit = function(rowData){
	var result = true;
	ajaxRequestAsync(BasePath+'/bill_om_outstock/get', {locno:rowData.locno,outstockNo:rowData.outstockNo}, function(data){
		if(data.status!='10'){
			result = false;
		}
	});
	return result;
};

planQuery.edit = function(){
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
	if(checkedRows[0].status!='10'||!planQuery.checkIsEdit(checkedRows[0])){
		alert('非建单状态的单据不能修改!',1);
		return;
	}
	planQuery.loadDetail(checkedRows[0],"edit");
};
planQuery.formateRealQty = function(value,rowData,rowIndex){
	var status=$("#status").val();	
	if(status=='30'){
		return rowData.instockQty;
	}else{
		return rowData.realQty;
	}
};
