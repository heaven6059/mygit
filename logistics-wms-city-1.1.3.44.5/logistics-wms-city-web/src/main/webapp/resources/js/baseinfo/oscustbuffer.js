var oscustbuffer = {};
//页面加载
$(document).ready(function(){
	
	//初始化-仓别
	oscustbuffer.initLocData();
	//初始化-仓区编码
	//oscustbuffer.initWareData();
	//oscustbuffer.loadSysNo();
	oscustbuffer.initStoreType();
	//oscustbuffer.loadCircleNo();
	wms_city_common.closeTip("showDialog");
	//初始化仓区
	wms_city_common.comboboxLoadFilter(
			["search_wareNo","wareNo","wareNoBatch"],
			'wareNo',
			'wareName',
			'valueAndText',
			false,
			[true,false,false],
			BasePath+'/cm_defware/get_biz',
			{"locno":oscustbuffer.locno},
			null,
			null);
	//初始品牌库
	wms_city_common.comboboxLoadFilter(
			["sysNoSearch"],
			null,
			null,
			null,
			true,
			[true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
			{},
			null,
			null);
	//初始商圈
	wms_city_common.comboboxLoadFilter(
			["circleNo"],
			'circleNo',
			'circleName',
			'valueAndText',
			false,
			[true],
			BasePath+'/bmcircle/get_biz',
			{locno:oscustbuffer.locno},
			null,
			null);
	
	
	$('#storeDg1').datagrid({
		'onBeforeLoad':function(param){
			if(typeof(param.locno) != "undefined"){
				var selectRows = $('#storeDg2').datagrid('getRows');
				var dataList = '';
				for (var i = selectRows.length - 1; i >= 0; i--) {
					dataList += "'"+selectRows[i].storeNo+"',";
				}
				if(dataList!=''){
					dataList = dataList.substring(0, dataList.length-1);
				}
				param.dataList = dataList;
			}
		 }
	});
});
//////////////////////////////////////// 查询列表  begin ////////////////////////////////////////
//仓别
oscustbuffer.locno;
oscustbuffer.initLocData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			oscustbuffer.locno = data.locno;
		}
	});
};
//==================商圈====================
oscustbuffer.loadCircleNo = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/bmcircle/get_biz?locno='+oscustbuffer.locno,
		success : function(data) {
			$('#circleNo').combobox({
				data:data,
			    valueField:'circleNo',    
			    textField:'valueAndText',
			    panelHeight:200,
				loadFilter:function(data){
					if(data){
						for(var i = 0;i<data.length;i++){
							var tempData = data[i];
							tempData.valueAndText = tempData.circleNo+'→'+tempData.circleName;
						}
					}
					return data;
				}
			});
		}
	});
};
//==================商圈====================
oscustbuffer.initStoreType = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=CITY_STORE_TYPE',
		success : function(data) {
			$('#storeType,#storeTypeBatch').combobox({
			     valueField:"itemvalue",
			     textField:"valueAndText",
			     data:data,
			     panelHeight:"auto",
			     onSelect:function(){
			    	 oscustbuffer.initStoreNo($(this).combobox("getValue"));
			     },
			     loadFilter:function(data){
	    			if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
	       	 		}
	    			return data;
	   			 } 
			  });
		}
	});
};



//仓区
oscustbuffer.initWareData = function(){
	var locNo = oscustbuffer.locno;
	$.ajax({
		async : false,
		cache : true,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/cm_defware/get_biz?locno='+locNo,
		success : function(data) {
			$('#search_wareNo').combobox({
			     valueField:"wareNo",
			     textField:"valueAndText",
			     data:data,
			     panelHeight:200,
			     loadFilter:function(data){
						if(data){
				       		 for(var i = 0;i<data.length;i++){
				       			 var tempData = data[i];
				       			 tempData.valueAndText = tempData.wareNo+'→'+tempData.wareName;
				       		 }
			   	 		}
						return data;
		    		},
			     onSelect:function(){
			    	 oscustbuffer.initAreaData("search");
				 }
			});
			$('#wareNo').combobox({
			     valueField:"wareNo",
			     textField:"valueAndText",
			     data:data,
			     panelHeight:200,
			     loadFilter:function(data){
						if(data){
				       		 for(var i = 0;i<data.length;i++){
				       			 var tempData = data[i];
				       			 tempData.valueAndText = tempData.wareNo+'→'+tempData.wareName;
				       		 }
			   	 		}
						return data;
		    		},
			     onSelect:function(data){
			    	 oscustbuffer.initAreaData();
				 }
			});
		}
	});
};

//库区(级联仓区)
oscustbuffer.initAreaData = function(type){
	var locNo = oscustbuffer.locno;
	var areaId = 'areaNo';
	var wareNo = $('#wareNo').combobox("getValue");
	if(type == "search"){
		areaId = 'search_areaNo';
		wareNo = $('#search_wareNo').combobox("getValue");
	}else if(type == "batch"){
		areaId = 'areaNoBatch';
		wareNo = $('#wareNoBatch').combobox("getValue");
	}
	if(wareNo == null || wareNo == ''){
		wms_city_common.comboboxLoadFilter(
				[areaId],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[type=="search"],
				null,
				{},
				null,
				null);
	}else{
		wms_city_common.comboboxLoadFilter(
				[areaId],
				'areaNo',
				'areaName',
				'valueAndText',
				false,
				[type == 'search'],
				BasePath+'/cm_defarea/get_biz',
				{"locno":locNo,"wareNo":wareNo},
				null,
				null);
	}
	oscustbuffer.initStockData(type);
	$("#areaNo").combobox('enable');
	/*$.ajax({
		async : false,
		cache : true,
		type : 'POST',
		dataType : "json",
		data:{
			"locno":locNo,
			"wareNo":wareNo
		},
		url:BasePath+'/cm_defarea/get_biz',
		success : function(data) {
				if(type=="search"){
					$('#search_areaNo').combobox({
					     valueField:"areaNo",
					     textField:"valueAndText",
					     data:data,
					     panelHeight:200,
					     loadFilter:function(data){
								if(data){
						       		 for(var i = 0;i<data.length;i++){
						       			 var tempData = data[i];
						       			 tempData.valueAndText = tempData.areaNo+'→'+tempData.areaName;
						       		 }
					   	 		}
								return data;
				    		},
					     onSelect:function(data){
					    	 oscustbuffer.initStockData("search");
						 }
					  }).combobox("select",data[0]==""||data[0]==null?"":data[0].areaNo);
				}else{
					$('#areaNo').combobox({
					     valueField:"areaNo",
					     textField:"valueAndText",
					     data:data,
					     panelHeight:200,
					     loadFilter:function(data){
								if(data){
						       		 for(var i = 0;i<data.length;i++){
						       			 var tempData = data[i];
						       			 tempData.valueAndText = tempData.areaNo+'→'+tempData.areaName;
						       		 }
					   	 		}
								return data;
				    		},
					     onSelect:function(data){
					    	 oscustbuffer.initStockData();
						 }
					}).combobox("select",data[0]==""||data[0]==null?"":data[0].areaNo);
					$("#areaNo").combobox('enable');
				}
		}
	});*/
};
//通道(级联仓区-->库区)
oscustbuffer.initStockData = function(type){
	var locNo = oscustbuffer.locno;
	var stockId = 'stockNo';
	var wareNo =  $('#wareNo').combobox("getValue");
	var areaNo =  $('#areaNo').combobox("getValue");
	if(type=="search"){
		stockId = 'search_stockNo';
		wareNo = $('#search_wareNo').combobox("getValue");
		areaNo =  $('#search_areaNo').combobox("getValue");
	}else if(type == "batch"){
		stockId = 'stockNoBatch';
		wareNo = $('#wareNoBatch').combobox("getValue");
		areaNo =  $('#areaNoBatch').combobox("getValue");
	}
	if(wareNo == '' || areaNo == ''){
		wms_city_common.comboboxLoadFilter(
				[stockId],
				'stockNo',
				'stockNo',
				'valueAndText',
				false,
				[type=="search"],
				null,
				{},
				null,
				null);
		
	}else{
		wms_city_common.comboboxLoadFilter(
				[stockId],
				'stockNo',
				'stockNo',
				'valueAndText',
				false,
				[type=="search"],
				BasePath+'/cm_defstock/get_biz',
				{"locno":locNo,"wareNo":wareNo,"areaNo":areaNo},
				null,
				null);
	}
	oscustbuffer.initCellNoData(type);
	$("#stockNo").combobox('enable');
	/*$.ajax({
		async : false,
		cache : true,
		type : 'POST',
		dataType : "json",
		data:{
			"locno":locNo,
			"wareNo":wareNo,
			"areaNo":areaNo
		},
		url:BasePath+'/cm_defstock/get_biz',
		success : function(data) {
			if(type=="search"){
				$('#search_stockNo').combobox({
				     valueField:"stockNo",
				     textField:"stockNo",
				     data:data,
				     panelHeight:200,
				     onSelect:function(data){
				    	 oscustbuffer.initCellNoData("search");
				     }
				  }).combobox("select",data[0]==""||data[0]==null?"":data[0].stockNo);
				
			}else{
				$('#stockNo').combobox({
				     valueField:"stockNo",
				     textField:"stockNo",
				     data:data,
				     panelHeight:200,
				     onSelect:function(data){
				    	 oscustbuffer.initCellNoData();
				    	 oscustbuffer.initAStockData();
					 }
				  }).combobox("select",data[0]==""||data[0]==null?"":data[0].stockNo);
				  $("#stockNo").combobox('enable');
			}
		}
	});*/
};
//通道
oscustbuffer.initAStockData = function(type){
	if(type == 'batch'){
		var wareNo =  $('#wareNoBatch').combobox("getValue");
		var areaNo =  $('#areaNoBatch').combobox("getValue");
		var stockNo = $('#stockNoBatch').combobox("getValue");
		$("#aStockNoBatch").val(wareNo+areaNo+stockNo);
	}else{
		var wareNo =  $('#wareNo').combobox("getValue");
		var areaNo =  $('#areaNo').combobox("getValue");
		var stockNo = $('#stockNo').combobox("getValue");
		$("#aStockNo").val(wareNo+areaNo+stockNo);
	}
};

//储位(级联仓区-->库区-->通道)
oscustbuffer.initCellNoData = function(type){
	var locNo = oscustbuffer.locno;
	var cellId = 'cellNo';
	var wareNo =  $('#wareNo').combobox("getValue");
	var areaNo =  $('#areaNo').combobox("getValue");
	var stockNo = $('#stockNo').combobox("getValue");
	if(type=="search"){
		cellId = 'search_cellNo';
		wareNo = $('#search_wareNo').combobox("getValue");
		areaNo =  $('#search_areaNo').combobox("getValue");
		stockNo = $('#search_stockNo').combobox("getValue");
	}else if(type=="batch"){
		cellId = 'cellNoBatch';
		wareNo = $('#wareNoBatch').combobox("getValue");
		areaNo =  $('#areaNoBatch').combobox("getValue");
		stockNo = $('#stockNoBatch').combobox("getValue");
	}
	if(wareNo == '' || areaNo == '' || stockNo == ''){
		$('#search_cellNo').combobox({
		     valueField:"cellNo",
		     textField:"cellNo",
		     data:[],
		     panelHeight:150
		  });
	}else{
		$.ajax({
			async : false,
			cache : true,
			type : 'POST',
			dataType : "json",
			data:{
				"locno":locNo,
				"wareNo":wareNo,
				"areaNo":areaNo,
				"stockNo":stockNo
			},
			url:BasePath+'/cm_defcell/get_biz',
			success : function(data) {
					$('#'+cellId).combobox({
					     valueField:"cellNo",
					     textField:"showCellNo",
					     data:data,
					     panelHeight:150,
					     loadFilter:function(data){
					    	 if(data == null || data.length == 0){
					    		 return data;
					    	 }else{
								var first = {};
								first.cellNo = '';
								first.showCellNo = '全选';
								var tempData = [];
								if(type=="search"){
									tempData[tempData.length] = first
								}
								var temp = {};
								for(var i=0;i<data.length;i++){
									temp = {};
									temp.cellNo = data[i].cellNo;
									temp.showCellNo = data[i].cellNo;
									tempData[tempData.length] = temp;
								}
								return tempData;
					    	 }
					     }
					  });
					$("#cellNo").combobox('enable');
				/*else{
					$('#cellNo').combobox({
					     valueField:"cellNo",
					     textField:"cellNo",
					     data:data,
					     panelHeight:200
					  }).combobox("select",data[0]==""||data[0]==null?"":data[0].cellNo);
					//$("#cellNo").combobox('enable');
				}*/
			}
		});
	}
};
//加载品牌库
oscustbuffer.loadSysNo = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=SYS_NO',
		success : function(data) {
			$('#sysNoSearch').combobox({
			    data:data,
			    valueField:"itemvalue",
			    textField:"itemnamedetail",
			    panelHeight:200
			});
		}
	});
};

oscustbuffer.initStoreNo = function(type){
	var isLimitLocno = "";
	if(type=='11') {
		isLimitLocno = "&isLimitLocno=yes";
	}
	$('#storeNo').combogrid({
		delay: 1000,
		panelHeight:300,
		panelWidth:450,   
        idField:'storeNo',  
        textField:'textFieldName',   
        pagination : true,
        rownumbers:true,
        mode: 'remote',
        url:BasePath+'/store/list.json?storeType='+type+'&locno='+oscustbuffer.locno+isLimitLocno,
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
//////////////////////////////////////// 查询列表 end ////////////////////////////////////////

//将数组封装成一个map
oscustbuffer.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].lineNo +"\":\""+data[i].lineName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//加载Grid数据Utils
oscustbuffer.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};

//查询区域信息
oscustbuffer.searchArea = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/os_cust_buffer/list.json?locno='+oscustbuffer.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
};

//查询区域信息
oscustbuffer.searchStoreData = function(){
	var fromObj = $('#searchStoreForm');
	var validateForm = fromObj.form('validate');
    if(validateForm==false){
    	return;
    }
    var storeTypeBatch = $('#storeTypeBatch').combobox('getValue');
	var fromObjStr=convertArray(fromObj.serializeArray());
	var url = BasePath+'/store/list.json';
    var queryParams = eval("(" +fromObjStr+ ")");
    queryParams['locno'] = oscustbuffer.locno;
    queryParams['isOsCustBuffer'] = 'Y';
	if(storeTypeBatch=='11') {
		queryParams['isLimitLocno'] = 'yes';
	}
    oscustbuffer.loadGridDataUtil('storeDg1', url, queryParams);
};

//查询条件清空
oscustbuffer.searchLocClear = function(){
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
} 

//加载数据
oscustbuffer.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/os_cust_buffer/list.json?locno='+oscustbuffer.locno,
    			'pageNumber':1
    		});
};

//格式化数据
oscustbuffer.transportTypeFormatter = function(value, rowData, rowIndex){
	return oscustbuffer.transportType[value];
};

oscustbuffer.deliverTypeFormatter = function(value, rowData, rowIndex){
	return oscustbuffer.deliverType[value];
};
oscustbuffer.lineFnameFormatter = function(value, rowData, rowIndex){
	return oscustbuffer.deliverType[rowData.deliverType]+oscustbuffer.transportType[rowData.transportType]+rowData.lineName;
};

oscustbuffer.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

oscustbuffer.dtlViewt = function(type) {
	$("#storeTypeCon").hide();
	$("#storeNameCon").show();
	var title = "详情";
	if(type=="edit"){
		title = "修改";
		$("#storeNo").combobox('disable');
		$("#storeName").attr("disabled",true);
		$("#wareNo").combobox('disable');
		$("#areaNo").combobox('disable');
		$("#stockNo").combobox('disable');
		$("#aStockNo").attr("disabled",true);
		$("#cellNo").combobox('disable');
		$("#bufferName").attr("disabled",false);
		$("#useVolumn").attr("disabled",true);
		$("#useWeight").attr("disabled",true);
		$("#useBoxnum").attr("disabled",true);
		
		$("#info_add").hide();
		$("#info_edit").show();
	} else if(type=="add"){
		title = "新增";
		$("#storeNo").combobox('enable');
		$("#storeName").attr("disabled",true);
		$("#wareNo").combobox('enable');
		$("#areaNo").combobox('enable');
		$("#stockNo").combobox('enable');
		$("#aStockNo").attr("disabled",false);
		$("#cellNo").combobox('enable');
		$("#bufferName").attr("disabled",false);
		$("#useVolumn").attr("disabled",false);
		$("#useWeight").attr("disabled",false);
		$("#useBoxnum").attr("disabled",false);
		
		$("#info_add").show();
		$("#info_edit").hide();
	} else {
		$("#storeNo").combobox('disable');
		$("#storeName").attr("disabled",true);
		$("#wareNo").combobox('disable');
		$("#areaNo").combobox('disable');
		$("#stockNo").combobox('disable');
		$("#aStockNo").attr("disabled",true);
		$("#cellNo").combobox('disable');
		$("#bufferName").attr("disabled",true);
		$("#useVolumn").attr("disabled",true);
		$("#useWeight").attr("disabled",true);
		$("#useBoxnum").attr("disabled",true);
		
		$("#info_add").hide();
		$("#info_edit").hide();
	}
	//设置标题
	$('#showDialog').window({
		title:title
	});
};

//新增
oscustbuffer.addInfo = function(){
	oscustbuffer.dtlViewt('add');
	$('#dataForm').form("clear");
	var wareNoFirst = $('#wareNo').combobox("getData");
	$('#wareNo').combobox("select",wareNoFirst[0].wareNo);
	$("#storeTypeCon").show();
	$("#storeNameCon").hide();
	$("#storeType").combobox("select","11");
	$("#showDialog").window('open'); 
};

//批量新增
oscustbuffer.addBatchInfo = function(){
	$('#searchStoreForm').form("clear");
	$("#storeTypeBatch").combobox("select","11");
	$('#storeDg1').datagrid('loadData', { total: 0, rows: [] });
	$('#storeDg2').datagrid('loadData', { total: 0, rows: [] });
	$("#addBatchUI").window('open'); 
	//清空级联列表的数据
	$("#stockNoBatch").combobox('loadData',[]);
	$("#areaNoBatch").combobox('loadData',[]);
	$("#cellNoBatch").combobox('loadData',[]);
};

//清空表单
oscustbuffer.clearAll = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		$(this).validatebox('reset');
	});
};
//清除校验
oscustbuffer.clearAalidate = function(){
	$(this).validatebox('reset');
};


//数据右移
oscustbuffer.toRgiht = function(){
	var selectRows = $('#storeDg1').datagrid('getChecked');
	if(selectRows.length < 1){
		alert("请选择客户!");
		return;
	}
	for (var i = selectRows.length - 1; i >= 0; i--) {
		var datas = {
			storeNo : selectRows[i].storeNo,
			storeName : selectRows[i].storeName
		};
		$('#storeDg2').datagrid('appendRow',datas);
        var index = $('#storeDg1').datagrid('getRowIndex', selectRows[i]);
        $('#storeDg1').datagrid('deleteRow', index);
    }
	$('#storeDg1').datagrid('load');
};

//数据左移
oscustbuffer.toLeft = function(){
	var selectRows = $('#storeDg2').datagrid('getChecked');
	if(selectRows.length < 1){
		alert("请选择客户!");
		return;
	}
	for (var i = selectRows.length - 1; i >= 0; i--) {
		var datas = {
			storeNo : selectRows[i].storeNo,
			storeName : selectRows[i].storeName
		};
		$('#storeDg1').datagrid('appendRow',datas);
        var index = $('#storeDg2').datagrid('getRowIndex', selectRows[i]);
        $('#storeDg2').datagrid('deleteRow', index);
    }
};

//改变客户类型清空数据
oscustbuffer.changeStoreType = function(){
	$('#storeDg2').datagrid('loadData', { total: 0, rows: [] });
	oscustbuffer.searchStoreData();
};

oscustbuffer.checkExist = function(){
	var locNo = oscustbuffer.locno;
	var storeNo = $("#storeNo").combobox("getValue");
	var wareNo = $("#wareNo").combobox("getValue");
	var areaNo = $("#areaNo").combobox("getValue");
	var stockNo = $("#stockNo").combobox("getValue");
	var cellNo = $("#cellNo").combobox("getValue");
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/os_cust_buffer/check',
		  data:{
			"locno":locNo,
			"storeNo":storeNo,
			"wareNo":wareNo,
			"areaNo":areaNo,
			"stockNo":stockNo,
			"cellNo":cellNo
		  },
		  cache: true,
		  success: function(data){
		  	if(data=="exist"){
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	return result;
};

oscustbuffer.checkCust = function(){
	var storeName = $("#storeName").val().trim();
	var result = false;
	$.ajax({
		  async : false,
		  type: 'POST',
		  dataType : "json",
		  url: BasePath+'/store/checkStore',
		  data:{
			"storeName":storeName
		  },
		  cache: true,
		  success: function(data){
			if(data.result=="success"){
				$("#storeNo").val(data.storeNo);
				result = true;
			}else{
				alert("该客户不存在");
				result = false;
			}
		  }
	});
	return result;
};


//确定转门店
oscustbuffer.doBatchSave = function(){
	
	//验证必填项
	var fromObj = $('#searchStoreForm');
	var validateForm = fromObj.form('validate');
    if(validateForm==false){
    	return ;
    }
	
	var storeRows = $("#storeDg2").datagrid("getRows");// 获取所有勾选checkbox的行
	if(storeRows.length < 1){
		alert("请添加需要保存的客户!");
		return;
	}
	
	var locno = oscustbuffer.locno;
	var bufferName = $('#bufferNameBatch').val();
	var wareNo = $('#wareNoBatch').combobox('getValue');
	var areaNo = $('#areaNoBatch').combobox('getValue');
	var stockNo = $('#stockNoBatch').combobox('getValue');
	var aStockNo = $('#aStockNoBatch').val();
	var cellNo = $('#cellNoBatch').combobox('getValue');
	var useVolumn = $('#useVolumnBatch').numberbox('getValue');
	var useWeight = $('#useWeightBatch').numberbox('getValue');
	var useBoxnum = $('#useBoxnumBatch').numberbox('getValue');
	
	var params = {
		locno : locno,
		bufferName : bufferName,
		wareNo : wareNo,
		areaNo : areaNo,
		stockNo : stockNo,
		aStockNo : aStockNo,
		cellNo : cellNo,
		useVolumn : useVolumn,
		useWeight : useWeight,
		useBoxnum : useBoxnum,
		datas : JSON.stringify(storeRows)
	};
	
	$.messager.confirm("确认","您确定要批量保存吗?", function (r){ 
		if(r){
			wms_city_common.loading("show","正在处理....");
			var url = BasePath+'/os_cust_buffer/insertBatch';
			ajaxRequest(url,params,function(data){
				if(data.result=="success"){
					$('#dataGridJG').datagrid('load');
					$('#searchStoreForm').form('clear');
					$('#storeDg1').datagrid('loadData', { total: 0, rows: [] });
					$('#storeDg2').datagrid('loadData', { total: 0, rows: [] });
					oscustbuffer.closeOpenUIItem('addBatchUI'); //关闭窗口
					alert("保存成功!");
				}else{
					alert(data.msg,2);
				}
				wms_city_common.loading();
			});
		}
	});
};

//保存
oscustbuffer.save_do = function(){
  var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
    	 alert("提交数据不完整！");
          return ;
     }
     //判断客户是否存在
//     if(!oscustbuffer.checkCust()){
//       	return;
//     }
// 	debugger;
     //判断主键是否存在
     if(!oscustbuffer.checkExist()){
    	 alert("门店对应暂存区已存在！");
     	return;
     }
	 //2. 保存
// 	debugger;
 	$.messager.confirm("确认","你确定要保存吗？", function (r){  
        if (r) {
        	var url = BasePath+'/os_cust_buffer/addOsCustBuffer';
        	fromObj.form('submit', {
    			url: url,
    			onSubmit: function(){
    			},
    			success: function(data){
    				if(data=="success"){
    					$("#showDialog").window('close'); 
    					 alert('新增成功!');
    					 oscustbuffer.loadDataGrid();
    				}else if(data=="nocust"){
    					alert('客户代码不存在，不能新增 ',2);
    				}else{
    					alert('新增失败,请联系管理员!',2);
    				}
    		    }
    	   });
        }
 	});
};

//删除
oscustbuffer.del = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.locno+"|"+item.storeNo+"|"+item.wareNo+"|"+item.areaNo+"|"+item.stockNo+"|"+item.cellNo);
        	}); 
            //2.绑定数据
            var url = BasePath+'/os_cust_buffer/delOsCustBuffer';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	oscustbuffer.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
					 oscustbuffer.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

oscustbuffer.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		oscustbuffer.edit(checkedRows[0],"edit");
	}	
};

//编辑
oscustbuffer.edit = function(rowData,type){
	//设置信息
	oscustbuffer.dtlViewt(type);
	$('#dataForm').form('load',rowData);
	//弹窗
	$("#showDialog").window('open'); 
};

//修改
oscustbuffer.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	 //2. 保存
    var url = BasePath+'/os_cust_buffer/editOsCustBuffer';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 oscustbuffer.loadDataGrid();
				}else{
					alert('修改失败,请联系管理员!',2);
				}
		    }
	});
};

oscustbuffer.closeOpenUIItem = function(id){
	$("#"+id).window('close');
};

//关闭window时需要提示
oscustbuffer.closeWindowTip = function(id){
	$.messager.confirm("提示","是否放弃当前编辑?", function (r){  
		if(r){
			$("#"+id).window('close');
		}
	});
};