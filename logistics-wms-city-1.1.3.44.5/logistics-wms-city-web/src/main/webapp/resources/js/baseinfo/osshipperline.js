var osshipperline = {};

//承运商
osshipperline.shipperData = {};
//仓别
osshipperline.locno;

//页面加载
$(document).ready(function(){
	//加载仓库
	osshipperline.initLoc();
	
	osshipperline.initLine();
	//承运商
	osshipperline.initShipper();
	//加载列表
	//osshipperline.loadDataGrid();
	//新增按钮
	$("#info_add").click(osshipperline.save_do);
	//修改按钮
	$("#info_edit").click(osshipperline.edit_do);
});


//承运商
osshipperline.initShipper = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/supplier/get_biz?businessType=8',
		success : function(data) {
			$('#shipperNo').combobox({
			     valueField:"supplierNo",
			     textField:"supplierName",
			     data:data,
			     panelHeight:"300",
			  });
			osshipperline.shipperData =osshipperline.converStr2JsonObj(data);
		}
	});
};

//加载仓库信息
osshipperline.initLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			osshipperline.locno = data.locno;
		}
	});
};

//线路
osshipperline.initLine = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/os_defline/get_biz?locno='+osshipperline.locno,
		success : function(data) {
			$('#lineNo').combobox({
			     valueField:"lineNo",
			     textField:"lineName",
			     data:data,
			     panelHeight:"300",
			  }).combobox('select',data[0]==""||data[0]==null?"":data[0].lineNo);
		}
	});
};


//将数组封装成一个map
osshipperline.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].supplierNo +"\":\""+data[i].supplierName+"\""
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//加载数据
osshipperline.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/os_shipper_line/list.json?locno='+osshipperline.locno,
    			'pageNumber':1
    		});
};

//格式话数据
osshipperline.shipperFormatter = function(value, rowData, rowIndex){
	return osshipperline.shipperData[value];
};

osshipperline.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//新增
osshipperline.addInfo = function(){
	$('#showDialog').window({
		title:"新增"
	});
	$(".creatorinfo").hide();
	$("#showDialog").window('open'); 
	$("#info_add").show();
	$("#info_edit").hide();
	
	//清除数据
	$('#dataForm').form("clear");
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="hide"){
			$(this).attr("disabled",true);
		}
		if($(this).attr("class")=="easyui-combobox combobox-f combo-f"){
			$(this).combobox('enable');
		}
	});
	
};

//清空表单
osshipperline.clearAll = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		$(this).validatebox('reset');
	});
};
//清除校验
osshipperline.clearAalidate = function(){
	$(this).validatebox('reset');
};

osshipperline.checkExist = function(){
	var locNo = osshipperline.locno;
	var lineNo = $("#lineNo").combobox("getValue");
	var shipperNo = $("#shipperNo").combobox("getValue");
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/os_shipper_line/checkExist',
		  data:{
			"locno":locNo,
			"lineNo":lineNo,
			"shipperNo":shipperNo
		  },
		  cache: true,
		  success: function(data){
		  	if(data=="exist"){
		  		alert("该线路下的承运商已经存在");
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	return result;
};

//保存
osshipperline.save_do = function(){
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     if(!osshipperline.checkExist()){
     	return;
     }
	 //2. 保存
     var url = BasePath+'/os_shipper_line/addOsShipperLine';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('新增成功!');
					 osshipperline.loadDataGrid();
				}else{
					alert('新增失败,请联系管理员!',2);
				}
		    }
	   });
};

//删除
osshipperline.del = function(){
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
        		keyStr.push(item.locno+"|"+item.lineNo+"|"+item.shipperNo);
        	});               
            //2.绑定数据
            var url = BasePath+'/os_shipper_line/deleteOsShipperLine';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	osshipperline.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
					 osshipperline.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

//修改
osshipperline.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		osshipperline.edit(checkedRows[0],"edit");
	}
}

//编辑
osshipperline.edit = function(rowData,type){
	var title = "详情";
	if(type=="edit"){
		title = "修改";
	}
	//设置标题
	$('#showDialog').window({
		title:title
	});
	$(".creatorinfo").show();
	//设置信息
	osshipperline.detail(rowData,type);
	//弹窗
	$("#showDialog").window('open'); 
};

osshipperline.detail = function(rowData,type){
	$('#dataForm').form('load',rowData);
	$("#lineNo").combobox('select',rowData.lineName);
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="easyui-combobox combobox-f combo-f"){
			$(this).combobox('disable');
		}
		if($(this).attr("class")=="hide"){
			$(this).attr('disabled',false);
		}
	});
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
osshipperline.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	 //2. 保存
    var url = BasePath+'/os_shipper_line/editOsShipperLine';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 osshipperline.loadDataGrid();
				}else{
					alert('修改失败,请联系管理员!',2);
				}
		    }
	});
};
