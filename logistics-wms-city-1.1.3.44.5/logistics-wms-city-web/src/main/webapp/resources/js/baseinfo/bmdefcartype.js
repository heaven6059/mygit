var bmdefcartype = {};

//页面加载
$(document).ready(function(){
	//加载列表
	//bmdefcartype.loadDataGrid();
	//新增按钮
	$("#info_add").click(bmdefcartype.save_do);
	//修改按钮
	$("#info_edit").click(bmdefcartype.edit_do);
});

//加载数据
bmdefcartype.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bm_defcartype/list.json',
    			'pageNumber':1
    		});
};

bmdefcartype.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//新增
bmdefcartype.addInfo = function(){
	$('#showDialog').window({
		title:"新增"
	});
	$("#creatorinfo").hide();
	$("#editorinfo").hide();
	$("#showDialog").window('open'); 
	$("#info_add").show();
	$("#info_edit").hide();
	$("#cartypeNo").attr("disabled",false);
	$("#cartypeNoHide").attr("disabled",true);
	bmdefcartype.clearAll();
};

//清空表单
bmdefcartype.clearAll = function(){
	$('#dataForm input').each(function(){ 
		$(this).val('');
	});
};
//清除校验
bmdefcartype.clearAalidate = function(){
	$(this).validatebox('reset');
};

//保存
bmdefcartype.save_do = function(){
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     if(!bmdefcartype.checkExist()){
     	return;
     }
	 //2. 保存
     var url = BasePath+'/bm_defcartype/addDefcartype';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('新增成功!');
					 bmdefcartype.loadDataGrid();
				}else{
					alert('新增失败,请联系管理员!',2);
				}
		    }
	   });
};

bmdefcartype.checkExist = function(){
	var cartypeNo = $("#cartypeNo").val();
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/bm_defcartype/checkExist',
		  data:{
			"cartypeNo":cartypeNo	
		  },
		  cache: true,
		  success: function(data){
		  	if(data=="exist"){
		  		alert("该车辆编号已经存在");
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	return result;
};

//删除
bmdefcartype.del = function(){
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
        		keyStr.push(item.cartypeNo);
        	});               
            //2.绑定数据
            var url = BasePath+'/bm_defcartype/deleteDefdock';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	bmdefcartype.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
					 bmdefcartype.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};


bmdefcartype.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		bmdefcartype.edit(checkedRows[0],"edit");
	}
};


//编辑
bmdefcartype.edit = function(rowData,type){
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
	bmdefcartype.detail(rowData,type);
	//弹窗
	$("#showDialog").window('open'); 
};

bmdefcartype.detail = function(rowData,type){
	$('#dataForm').form('load',rowData);
	bmdefcartype.clearAalidate = function(){
		$(this).validatebox('reset');
	};
	//车辆编码不能修改
	$("#cartypeNo").attr("disabled",true);
	$("#cartypeNoHide").attr("disabled",false);
	$("#cartypeNoHide").val(rowData.cartypeNo);
	//用户信息
	$("#creator").html(rowData.creator);
	$("#createtm").html(rowData.createtm);
	$("#editor").html(rowData.editor);
	$("#edittm").html(rowData.edittm);
	
	//隐藏新增按钮，显示编辑按钮
	$("#info_add").hide();
	if(type=="edit"){
		$("#info_edit").show();
	}else{
		$("#info_edit").hide();
	}
};

//修改
bmdefcartype.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	 //2. 保存
    var url = BasePath+'/bm_defcartype/editDefdock';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 bmdefcartype.loadDataGrid();
				}else{
					alert('修改失败,请联系管理员!',2);
				}
		    }
	});
};
