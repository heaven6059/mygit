var bsworkerarea = {};

//作业类型
bsworkerarea.operateTypeData= {};
//仓别
bsworkerarea.locno;

//页面加载
$(document).ready(function(){
	bsworkerarea.initLocData();
	//加载列表
	//bsworkerarea.loadDataGrid();
	//新增按钮
	$("#info_add").click(bsworkerarea.save_do);
	//修改按钮
	$("#info_edit").click(bsworkerarea.edit_do);
	
	//级联完整路线信息
	$("#lineName").blur(bsworkerarea.setLineFname);
	$('#transportType').combobox({
	    onSelect:function(data){
	    	bsworkerarea.setLineFname();
	    }
	});
	$('#deliverType').combobox({
	    onSelect:function(data){
	    	bsworkerarea.setLineFname();
	    }
	});
	
	wms_city_common.closeTip("showDialog");
	//初始化员工
	wms_city_common.comboboxLoadFilter(
			["search_workerNo","workerNo"],
			'workerNo',
			'workerName',
			'valueAndText',
			false,
			[true,false],
			BasePath+'/authority_user/user.json',
			{},
			null,
			null);
	//初始化仓区
	wms_city_common.comboboxLoadFilter(
			["search_wareNo","wareNo"],
			'wareNo',
			'wareName',
			'valueAndText',
			false,
			[true,false],
			BasePath+'/cm_defware/get_biz',
			{locno:bsworkerarea.locno},
			null,
			null);
	//初始化作业类型
	wms_city_common.comboboxLoadFilter(
			["operateType"],
			null,
			null,
			null,
			true,
			[false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=BSWA_OPERATE_TYPE',
			{},
			bsworkerarea.operateTypeData,
			null);
});

bsworkerarea.setLineFname = function(){
	$("#lineFname").val($('#deliverType').combobox('getText')+$('#transportType').combobox('getText')+$("#lineName").val());
};

//仓别
bsworkerarea.initLocData = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			bsworkerarea.locno = data.locno;
		}
	});
};

//库区
bsworkerarea.initAreaData = function(type){
	var areaId = 'search_areaNo';
	var locNo = bsworkerarea.locno;
	var wareNo = $('#search_wareNo').combobox("getValue");
	if(type!="search"){
		wareNo = $('#wareNo').combobox("getValue");
		areaId = 'areaNo';
	}
	if(wareNo == ''){
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
				[type=="search"],
				BasePath+'/cm_defarea/get_biz',
				{"locno":locNo,"wareNo":wareNo},
				null,
				null);
	}
	bsworkerarea.initStockData(type);
};
bsworkerarea.stockData = {};
//通道
bsworkerarea.initStockData = function(type){
	var stockId = 'search_stockNo';
	var locNo = bsworkerarea.locno;
	var wareNo =  $('#search_wareNo').combobox("getValue");
	var areaNo =  $('#search_areaNo').combobox("getValue");
	if(type!="search"){
		wareNo = $('#wareNo').combobox("getValue");
		areaNo =  $('#areaNo').combobox("getValue");
		stockId = 'stockNo';
	}
	if(wareNo == '' || areaNo == ''){
		wms_city_common.comboboxLoadFilter(
				[stockId],
				'stockNo',
				'aStockNo',
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
				'aStockNo',
				'valueAndText',
				false,
				[type=="search"],
				BasePath+'/cm_defstock/get_biz',
				{"locno":locNo,"wareNo":wareNo,"areaNo":areaNo},
				bsworkerarea.stockData,
				null);
	}
};
bsworkerarea.writeAStockNo = function(data){
	$("#aStockNo").val(bsworkerarea.stockData[data]);
};


bsworkerarea.searchLocClear = function(){
	$('#searchForm').form("clear");
};

//将数组封装成一个map
bsworkerarea.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\""
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

bsworkerarea.converStr2JsonObj2 = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].usercode +"\":\""+data[i].username+"\""
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//格式化作业类型
bsworkerarea.operateTypeFormatter = function(value,rowData,rowIndex){
	return bsworkerarea.operateTypeData[value];
};

//查询区域信息
bsworkerarea.searchArea = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bs_worker_area/list.json?locNo='+bsworkerarea.locno;
	
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
    
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
bsworkerarea.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bs_worker_area/list.json?locNo='+bsworkerarea.locno,
    			'pageNumber':1
    		});
};

//格式化数据
bsworkerarea.transportTypeFormatter = function(value, rowData, rowIndex){
	return bsworkerarea.transportType[value];
};

bsworkerarea.deliverTypeFormatter = function(value, rowData, rowIndex){
	return bsworkerarea.deliverType[value];
};

bsworkerarea.lineFnameFormatter = function(value, rowData, rowIndex){
	return bsworkerarea.deliverType[rowData.deliverType]+bsworkerarea.transportType[rowData.transportType]+rowData.lineName;
};

bsworkerarea.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};



//新增
bsworkerarea.addInfo = function(){
	$('#showDialog').window({
		title:"新增"
	});
	$("#creatorinfo").hide();
	$("#editorinfo").hide();
	$("#showDialog").window('open'); 
	$("#info_add").show();
	$("#info_edit").hide();
	
	
	//清除数据
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="easyui-combobox combobox-f combo-f"){
			$(this).combobox('enable');
		}
		if($(this).attr("class")=="hide"){
			$(this).attr("disabled",true);
		}
	});
	$("#dataForm").form("clear");
	
};

//清空表单
bsworkerarea.clearAll = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		$(this).validatebox('reset');
	});
};
//清除校验
bsworkerarea.clearAalidate = function(){
	$(this).validatebox('reset');
};

//校验唯一性
bsworkerarea.checkExist = function(){
	var workerNo = $("#workerNo").combobox("getValue");
	var wareNo = $("#wareNo").combobox("getValue");
	var areaNo = $("#areaNo").combobox("getValue");
	var stockNo = $("#stockNo").combobox("getValue");
	var operateType = $("#operateType").combobox("getValue");
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/bs_worker_area/checkExist',
		  data:{
			"workerNo":workerNo,
			"wareNo":wareNo,
			"areaNo":areaNo,
			"stockNo":stockNo,
			"operateType":operateType
		  },
		  cache: true,
		  success: function(data){
		  	if(data=="exist"){
		  		alert("该条记录已经存在");
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	return result;
};

//保存
bsworkerarea.save_do = function(){
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     if(!bsworkerarea.checkExist()){
     	return;
     }
	 //2. 保存
     var url = BasePath+'/bs_worker_area/addBsWorkerArea';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('新增成功!');
					 bsworkerarea.loadDataGrid();
				}else{
					alert('新增失败,请联系管理员!',2);
				}
		    }
	   });
};

//删除
bsworkerarea.del = function(){
	//var rows = $("#dataGridJG").datagrid("getSelections");// 获取所有选中的行
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.locno+"|"+item.workerNo+"|"+item.operateType+"|"+item.wareNo+"|"+item.areaNo+"|"+item.stockNo);
        	}); 
            //2.绑定数据
            var url = BasePath+'/bs_worker_area/deleteBsWorkerArea';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	bsworkerarea.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
					 bsworkerarea.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

//修改
bsworkerarea.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		bsworkerarea.edit(checkedRows[0],"edit");
	}
};



//编辑
bsworkerarea.edit = function(rowData,type){
	var title = "详情";
	if(type=="edit"){
		title = "修改";
	}
	//设置标题
	$('#showDialog').window({
		title:title
	});
	$("#creatorinfo").show();
	$("#editorinfo").show();
	//设置信息
	bsworkerarea.detail(rowData,type);
	//弹窗
	$("#showDialog").window('open'); 
};

bsworkerarea.detail = function(rowData,type){
	
	$('#dataForm').form('load',rowData);
	
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="easyui-combobox combobox-f combo-f"){
			$(this).combobox('disable');
		}
		if($(this).attr("class")=="hide"){
			$(this).attr("disabled",false);
		}
	});
	$("#operateType").combobox('disable');
	//用户信息
	$("#creator").text(rowData.creator);
	$("#createtm").text(rowData.createtm);
	$("#editor").text(rowData.editor);
	$("#edittm").text(rowData.edittm);
	
	//隐藏新增按钮，显示编辑按钮
	$("#info_add").hide();
	if(type=="edit"){
		$("#info_edit").show();
	}else{
		$("#info_edit").hide();
	}
};

//修改
bsworkerarea.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	 //2. 保存
    var url = BasePath+'/bs_worker_area/editBsWorkerArea';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 bsworkerarea.loadDataGrid();
				}else{
					alert('修改失败,请联系管理员!',2);
				}
		    }
	});
};
