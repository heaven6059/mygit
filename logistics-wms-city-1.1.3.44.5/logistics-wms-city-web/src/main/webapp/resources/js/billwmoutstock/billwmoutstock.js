var billwmoutstock = {};
billwmoutstock.locno;

$(function(){
	$("#createTmStart").datebox('setValue',getDateStr(-2));
	//初始化仓别
	billwmoutstock.initLoc();
	//初始化数据
	//billwmoutstock.loadDataGrid();
	//初始化下拉框
	billwmoutstock.initStatusData();
	
	billwmoutstock.initOutstockName();
	
	var objs = [];
	objs.push({
		"sysNoObj" : $('#sysNoSearch'),
		"brandNoObj" : $('input[id=brandNo]', $('#searchForm'))
	});
	wms_city_common.loadSysNo4Cascade(objs);
});


//初始化拣货人员
billwmoutstock.workerLocs = {};
billwmoutstock.initOutstockName = function(){
	wms_city_common.comboboxLoadFilter(
			["assignName"],
			'workerNo',
			'workerName',
			'unionName',
			false,
			[true],
			BasePath+'/authority_user/user.json',
			{},
			billwmoutstock.workerLocs,
			null);
};

billwmoutstock.outstockNameFormatter = function(value,rowData,rowIndex){
	return billwmoutstock.workerLocs[value];
};

billwmoutstock.converStr2JsonObj2= function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].workerNo +"\":\""+data[i].workerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//加载Grid数据Utils
billwmoutstock.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

billwmoutstock.initLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			billwmoutstock.locno = data.locno;
			billwmoutstock.userChName = data.username;
			billwmoutstock.editor = data.loginName;
		}
	});
};

billwmoutstock.status = {};
billwmoutstock.initStatusData = function(){
	wms_city_common.comboboxLoadFilter(
			["status","searchStatus"],
			null,
			null,
			null,
			true,
			[false,true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_WM_OUTSTOCK',
			{},
			billwmoutstock.status,
			null);
};



billwmoutstock.detail = function(rowData,type){
	$("#dtlForm").form("clear");
	$("#dtlForm").form("load",rowData);
	$("#outstockNo").attr("disabled",true);
	$("#status").combobox("disable");
	$('#showDialog').window('open'); 
	$('#ownerNoHide').val(rowData.ownerNo);
	
	
	//仓别
	var locno = rowData.locno;
	//判断单据状态
	if(type=="2"){
		$("#dataGridJG_Detail_div").hide();
		$("#dataGridJG_Detail_div2").show();
		$('#checkBtn').show();
		$('#checkBtn').linkbutton('enable');
		
//		var url = BasePath+'/bill_wm_outstock_dtl/list.json';
//		var queryParams = {locno:rowData.locno,outstockNo:rowData.outstockNo,ownerNo:rowData.ownerNo};
//		billwmoutstock.loadGridDataUtil('dataGridJG_Detail2', url, queryParams);
		
		$('#dataGridJG_Detail2').datagrid(
			{
				'url':BasePath+'/bill_wm_outstock_dtl/dtl_list.json?locno='+rowData.locno+"&outstockNo="+rowData.outstockNo+'&ownerNo='+rowData.ownerNo,
		   		 onLoadSuccess:function(){
		   			var curObj = $('#dataGridJG_Detail2');
		   			var rows = curObj.datagrid("getRows");
					$.each(rows,function(index,item){
						curObj.datagrid('beginEdit', index);
						var ed = curObj.datagrid('getEditor', {index:index,field:'realQty'});
						var edOutstockName = curObj.datagrid('getEditor', {index:index,field:'outstockName'});
						$(ed.target).numberbox('setValue', item.itemQty);
						$(edOutstockName.target).combobox('setValue', item.assignName);
					});
		   		}
		    }
		);
		
	}else{
		$("#dataGridJG_Detail_div").show();
		$("#dataGridJG_Detail_div2").hide();
		$('#checkBtn').hide();
		$('#checkBtn').linkbutton('disable');
		$('#dataGridJG_Detail').datagrid(
			{
				'url':BasePath+'/bill_wm_outstock_dtl/dtl_list.json?locno='+rowData.locno+"&outstockNo="+rowData.outstockNo+'&ownerNo='+rowData.ownerNo
		    }
		);
	}  
	
	//获取用户信息
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#outstockName').combobox({
			    data:data,
			    valueField:'workerNo',    
			    textField:'unionName',
			    panelHeight:"auto",
			    onSelect: function(data){
			    	var workerNo =  data.workerNo;
			    	if($('#outstockNameTemp').combobox("getValue")!=workerNo){
			    		$('#outstockNameTemp').combobox("select",data.workerNo);
			    	}
				}
			})
			.combobox("select",data[0].workerNo);
			$('#outstockNameTemp').combobox({
			    data:data,
			    valueField:'workerNo',    
			    textField:'workerNo',
			    panelHeight:"auto",
			    onSelect: function(data){   
			    	var workerNo =  data.workerNo;
			    	if($('#outstockName').combobox("getValue")!=workerNo){
			    		$('#outstockName').combobox("select",data.workerNo);
			    	} 
				}
			}).combobox("select",data[0].workerNo);
		}
	});
};

//拣货确认
billwmoutstock.check = function(){
	var curObj = $("#dataGridJG_Detail2");
	var rows = curObj.datagrid("getRows");
	var isOk = true;
	var tipMessage = "";
	var keyStr = [];
	
	//结束编辑
	$.each(rows,function(index,item){
		curObj.datagrid('endEdit',index);
	});	
	
	$.each(rows,function(index,item){
		
//		var ed = curObj.datagrid('getEditor', {index:index,field:'realQty'});
//		var edOutstockName = curObj.datagrid('getEditor', {index:index,field:'outstockName'});
//		var value = $(ed.target).val();
//		var outstockName = $(edOutstockName.target).combobox('getValue');
		
		var value = item.realQty;
		var outstockName = item.outstockName;
		var itemQty = item.itemQty;
		
		if(outstockName=="" || outstockName==null){
			tipMessage = "商品："+item.itemNo+"尺码："+item.sizeNo+"拣货人验证不通过,不能为空";
			isOk = false;
			return false;
		}
		
		if(value==""||value<=0){
			tipMessage = "商品："+item.itemNo+"尺码："+item.sizeNo+"实际拣货数量必须大于0";
			//$(ed.target).focus();
			isOk = false;
			return false;
		}
		if(value > itemQty){
			tipMessage = "商品："+item.itemNo+"尺码："+item.sizeNo+"实际数量不能大于计划数量!";
			isOk = false;
			return;
		}
		
		keyStr.push(item.ownerNo+"|"+item.divideId+"|"+value+"|"+outstockName);
	});
	
	if(!isOk&&tipMessage!=''){
		alert(tipMessage);
		return;
	}
	
	if(isOk){
		
		$.messager.confirm("确认","确认拣货后数据将无法修改，您确定并继续操作吗？", function (r){ 
			
			if(!r){
				return;
			}
			wms_city_common.loading("show","拣货确认中......");
			$.ajax({
				async : true,
				cache : false,
				type : 'POST',
				dataType : "json",
				data:{
					locno:billwmoutstock.locno,
					userChName:billwmoutstock.userChName,
					ownerNo:$("#ownerNoHide").val(),
					outstockNo:$("#outstockNoHide").val(),
					outstockName:$("#outstockName").combobox("getValue"),
					editor:billwmoutstock.editor,
					keyStr:keyStr.join(",")
				},
				url:BasePath+'/bill_wm_outstock/checkOutstock',
				success : function(data) {
					if(data.result=="success"){
						alert("拣货成功");
						$('#showDialog').window('close');
						billwmoutstock.loadDataGrid();
					}else{
						alert(data.msg,2);
					}
					wms_city_common.loading();
				}
			});
			
		});
	}
};

billwmoutstock.search = function(){
	
	var createTmStart =  $('#createTmStart').datebox('getValue');
	var createTmEnd =  $('#createTmEnd').datebox('getValue');
	if(!isStartEndDate(createTmStart,createTmEnd)){    
		alert("创建日期开始日期不能大于结束日期");   
        return;   
    }
		
	var outstockDateStart =  $('#outstockDateStart').datebox('getValue');
	var outstockDateEnd =  $('#outstockDateEnd').datebox('getValue');
	if(!isStartEndDate(outstockDateStart,outstockDateEnd)){    
		alert("拣货日期开始日期不能大于结束日期");   
        return;   
    }
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bill_wm_outstock/list_d.json?locno='+billwmoutstock.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
};

//清空form表单数据 
billwmoutstock.searchClear = function(id){
	$('#'+id).form('clear');
	$('#brandNo').combobox("loadData",[]);
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

//格式化
billwmoutstock.statusFormatter = function(value,rowData,rowIndex){
	return billwmoutstock.status[value];
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
}
billwmoutstock.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
		{
			'url':BasePath+'/bill_wm_outstock/list_d.json?locno='+billwmoutstock.locno
		});
};

billwmoutstock.showModify = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length != 1){
		alert('请选择一条记录！',1);
		return;
	}
	$.each(checkedRows,function(index,item){
		if(item.status != '10'){
			alert("只能修改状态为未处理的单据!",1);
			return;
		}
		billwmoutstock.detail(checkedRows[0],'2');
	});
	
};
billwmoutstock.printDetail = function(){
	
	var resultData;
	var rows = $('#dataGridJG').datagrid('getChecked');
	if(rows.length==0){
		alert('请选择需要打印的数据!');
		return;
	}
	var keys = [];
	for(var i=0,length = rows.length;i<length;i++){
		keys.push(rows[i].outstockNo+"_"+rows[i].assignName);
	}
	$.ajax({
		cache: false,
        async: false,
        type: 'POST',
        url: BasePath+'/bill_wm_outstock_dtl/printDetail',
        data: {
        	keys:keys.join("|"),
        	locno:billwmoutstock.locno
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
			LODOP.ADD_PRINT_BARCODE(10,10,250,40,"128A",rows[i].outstockNo);
		}
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//纵向打印时显示正面朝上
		LODOP.PREVIEW();
     }
};