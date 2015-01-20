var osdefline = {};

//运输方式
osdefline.transportType = {};
osdefline.locno;
//配送方式
osdefline.deliverType= {};

//页面加载
$(document).ready(function(){
	//加载运输方式
	osdefline.initTransportType();
	//加载配送方式
	osdefline.initDeliverType();
	//加载仓库
	osdefline.initLoc();
	
	//加载列表
	//osdefline.loadDataGrid();
	//新增按钮
	$("#info_add").click(osdefline.save_do);
	//修改按钮
	$("#info_edit").click(osdefline.edit_do);
	
	//级联完整路线信息
	$("#lineName").blur(osdefline.setLineFname);
	$('#transportType').combobox({
	    onSelect:function(data){
	    	osdefline.setLineFname();
	    }
	});
	$('#deliverType').combobox({
	    onSelect:function(data){
	    	osdefline.setLineFname();
	    }
	});
});
osdefline.setLineFname = function(){
	$("#lineFname").val($('#deliverType').combobox('getText')+$('#transportType').combobox('getText')+$("#lineName").val());
};

//运输方式
osdefline.initTransportType = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=TRANSPORT_TYPE',
		success : function(data) {
			osdefline.transportType =osdefline.converStr2JsonObj(data);
			$('#transportType').combobox({
			     valueField:"itemvalue",
			     textField:"itemname",
			     data:data,
			     panelHeight:"auto",
			  });
		}
	});
};
//配送方式
osdefline.initDeliverType = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=DELIVER_TYPE',
		success : function(data) {
			osdefline.deliverType =osdefline.converStr2JsonObj(data);
			$('#deliverType').combobox({
			     valueField:"itemvalue",
			     textField:"itemname",
			     data:data,
			     panelHeight:"auto",
			  });
		}
	});
};
//将数组封装成一个map
osdefline.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//加载仓库信息
osdefline.initLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			osdefline.locno = data.locno;
		}
	});
};
//加载数据
osdefline.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/os_defline/list.json?locno='+osdefline.locno,
    			'pageNumber':1
    		});
};

//格式话数据
osdefline.transportTypeFormatter = function(value, rowData, rowIndex){
	return osdefline.transportType[value];
};

osdefline.deliverTypeFormatter = function(value, rowData, rowIndex){
	return osdefline.deliverType[value];
};

osdefline.lineFnameFormatter = function(value, rowData, rowIndex){
	return osdefline.deliverType[rowData.deliverType]+osdefline.transportType[rowData.transportType]+rowData.lineName;
};

osdefline.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};



//新增
osdefline.addInfo = function(){
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
		if($(this).attr("class")!="easyui-combobox"){
			$(this).val("");
		}
	});
	
	var transportTypeData = $('#transportType').combobox('getData');
	$('#transportType').combobox("select",transportTypeData[0].itemvalue);  
	
	var deliverTypeData = $('#deliverType').combobox('getData');
	$('#deliverType').combobox("select",deliverTypeData[0].itemvalue);  
	
	
	$("#lineNo").attr("disabled",false);
	$("#lineNoHide").attr("disabled",true);
	
	
};

//清空表单
osdefline.clearAll = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		$(this).validatebox('reset');
	});
};
//清除校验
osdefline.clearAalidate = function(){
	$(this).validatebox('reset');
};

osdefline.checkExist = function(){
	var locNo = osdefline.locno;
	var lineNo = $("#lineNo").val();
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/os_defline/checkExist',
		  data:{
			"locno":locNo,
			"lineNo":lineNo  	
		  },
		  cache: true,
		  success: function(data){
		  	if(data=="exist"){
		  		alert("该仓别下的该线路编码已经存在");
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	return result;
};

//保存
osdefline.save_do = function(){
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     if(!osdefline.checkExist()){
     	return;
     }
     
	 //2. 保存
     var url = BasePath+'/os_defline/addOsDefline';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('新增成功!');
					 osdefline.loadDataGrid();
				}else{
					alert('新增失败,请联系管理员!',2);
				}
		    }
	   });
};

//删除
osdefline.del = function(){
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
        		keyStr.push(item.locno+"|"+item.lineNo);
        	});               
            //2.绑定数据
            var url = BasePath+'/os_defline/deleteOsDefline';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	osdefline.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
					 osdefline.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

osdefline.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		osdefline.edit(checkedRows[0],"edit");
	}
};


//编辑
osdefline.edit = function(rowData,type){
	//设置标题
	
	var title="";
	if(type=="edit"){
		title="修改";
	}else{
		title="详情";
	}
	$('#showDialog').window({
		title:title
	});
	$("#creatorinfo").show();
	$("#editorinfo").show();
	//设置信息
	osdefline.detail(rowData,type);
	//弹窗
	$("#showDialog").window('open'); 
};

osdefline.detail = function(rowData,type){
	$('#dataForm').form('load',rowData);
	osdefline.clearAalidate = function(){
		$(this).validatebox('reset');
	}
	$("#lineNo").attr("disabled",true);
	$("#lineNoHide").attr("disabled",false);
	
	
	//车辆编码不能修改
	$("#cartypeNo").attr("disabled",true);
	$("#cartypeNoHide").attr("disabled",false);
	$("#cartypeNoHide").val(rowData.cartypeNo);
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
	$("#lineFname").val(osdefline.deliverType[rowData.deliverType]+osdefline.transportType[rowData.transportType]+rowData.lineName);
};

//修改
osdefline.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	 //2. 保存
    var url = BasePath+'/os_defline/editOsDefline';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 osdefline.loadDataGrid();
				}else{
					alert('修改失败,请联系管理员!',2);
				}
		    }
	});
};
