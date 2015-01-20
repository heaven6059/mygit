var oslinecust = {};

oslinecust.locData = {};
oslinecust.locno;
//页面加载
$(document).ready(function(){
	//加载仓库
	oslinecust.initLoc();
	//客户
	oslinecust.initStore();
	//加载列表
	oslinecust.loadLineDataGrid();
	//新增按钮
	$("#info_add").click(oslinecust.save_do);
	//修改按钮
	$("#info_edit").click(oslinecust.edit_do);
});


//客户
oslinecust.initStore = function(){
	$('#storeNo').combotree({    
		url:BasePath+'/store/queryStoreTree',
		required: true   
	}); 
};

//加载仓库信息
oslinecust.initLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			oslinecust.locno = data.locno;
		}
	});
};

//将数组封装成一个map
oslinecust.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].storeNo +"\":\""+data[i].storeName+"\""
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
oslinecust.converStr2JsonObj2 = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].locno +"\":\""+data[i].locname+"\""
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//加载数据
oslinecust.loadLineDataGrid = function(){
    $('#dataGridJG_line').datagrid(
    		{
    			onLoadSuccess:function(data){
    		    	$('#dataGridJG_line').datagrid('selectRow', 0);
    		    }
    		});
};

//加载客户关系信息
oslinecust.loadCustDataGrid = function(rowData){
	var queryMxURL=BasePath+'/os_line_cust/list.json?lineNo='+rowData.lineNo+'&locno='+oslinecust.locno;
    $( "#dataGridJG_cust").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG_cust").datagrid( 'load' );
};


oslinecust.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//新增
oslinecust.addInfo = function(){
	$('#showDialog').window({
		title:"新增"
	});
	$(".creatorinfo").hide();
	$("#storeName").hide();
	$("#storeNoArea").show();
	$("#showDialog").window('open'); 
	$("#info_add").show();
	$("#info_edit").hide();
	
	//清除数据
	$('#dataForm').form("clear");
	//设置线路到隐藏域
	$("#lineNo").val(oslinecust.getLineNo().lineNo);
	$("#dataForm input").each(function(){
		if($(this).attr("class")=="hide"){
			$(this).attr("disabled",true);
		}
		if($(this).attr("class")=="easyui-combobox combobox-f combo-f"){
			$(this).combobox('enable');
		}
	});
	
};

//修改
oslinecust.editInfo = function(){
	var checkedRows = $("#dataGridJG_cust").datagrid('getChecked');
	if(checkedRows.length<1){
		alert("请选择要修改的行！");
	}else if(checkedRows.length>1){
		alert("只能选择一行进行修改！");
	}else{
		oslinecust.edit(checkedRows[0]);
	}
	
};


//清空表单
oslinecust.clearAll = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
	$('#dataForm input').each(function(){
		$(this).validatebox('reset');
	});
};
//清除校验
oslinecust.clearAalidate = function(){
	$(this).validatebox('reset');
};

oslinecust.checkExist = function(){
	var locno = oslinecust.locno;
	var storeNo = $("#storeNo").combobox("getValue");
	var lineNo =  oslinecust.getLineNo().lineNo;
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/os_line_cust/checkExist',
		  data:{
			"locno":locno,	
			"storeNo":storeNo,
			"lineNo":lineNo
		  },
		  cache: true,
		  success: function(data){
		  	if(data=="exist"){
		  		alert("该客户编号已经存在");
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	return result;
};

//保存
oslinecust.save_do = function(type){
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     if(!oslinecust.checkExist()){
     	return;
     }
 	var treeObj = $("#storeNo").combotree('tree').tree('getSelected');
    $("#storeNoHide").attr("disabled",false);
    $("#storeNoHide").val(treeObj.id);
     
	 //2. 保存
     var url = BasePath+'/os_line_cust/addOsLineCust';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('新增成功!');
					 oslinecust.loadCustDataGrid(oslinecust.getLineNo());
				}else{
					alert('新增失败,请联系管理员!',2);
				}
		    }
	   });
};
//获取线路选中行

oslinecust.getLineNo = function(){
	var rows = $("#dataGridJG_line").datagrid("getSelections");
	if(rows.length==0){
		alert("请先选择线路");
		return;
	}
	return rows[0];
}


//删除
oslinecust.del = function(){
	//var rows = $("#dataGridJG").datagrid("getSelections");// 获取所有选中的行
	var checkedRows = $("#dataGridJG_cust").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}
	$.messager.confirm("确认","你确定要删除这"+checkedRows.length+"条数据", function (r){  
        if (r) {
        	var keyStr = [];
        	$.each(checkedRows, function(index, item){
        		keyStr.push(item.locno+"|"+item.storeNo+"|"+item.lineNo);
        	});               
            //2.绑定数据
            var url = BasePath+'/os_line_cust/deleteOsLineCust';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	oslinecust.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
        			 oslinecust.loadCustDataGrid(oslinecust.getLineNo());
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
}


//编辑
oslinecust.edit = function(rowData){
	//设置标题
	$('#showDialog').window({
		title:"修改"
	});
	$(".creatorinfo").show();
	//设置信息
	oslinecust.detail(rowData);
	//弹窗
	$("#showDialog").window('open'); 
};

oslinecust.detail = function(rowData){
	$('#dataForm').form('load',rowData);
	$("#storeNo").combotree('setValue',rowData.storeNo);
	$("#lineNo").val(oslinecust.getLineNo().lineNo);
	//用户信息
	$("#creator").text(rowData.creator);
	$("#createtm").text(rowData.createtm);
	$("#editor").text(rowData.editor);
	$("#edittm").text(rowData.edittm);
	
	$("#storeName").show();
	$("#storeNoArea").hide();
	$("#storeName").val(rowData.storeName);
	//隐藏新增按钮，显示编辑按钮
	$("#info_add").hide();
	$("#info_edit").show();
}

//修改
oslinecust.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	 //2. 保存
    var url = BasePath+'/os_line_cust/editOsLineCust';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 oslinecust.loadCustDataGrid(oslinecust.getLineNo());
				}else{
					alert('修改失败,请联系管理员!',2);
				}
		    }
	});
}
