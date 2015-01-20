var bmdefprintergroup = {};
//状态
bmdefprintergroup.status = {};
bmdefprintergroup.locno;

$(document).ready(function(){
	bmdefprintergroup.loadLoc();
	bmdefprintergroup.initUser();
	//bmdefprintergroup.initStatus();
	//加载列表数据
	//bmdefprintergroup.loadDataGrid();
	$("#info_add").click(bmdefprintergroup.save_do);
	$("#info_edit").click(bmdefprintergroup.edit_do);
	$("#delBtn").click(bmdefprintergroup.del);
	//初始化状态
	wms_city_common.comboboxLoadFilter(
			["search_status","status"],
			null,
			null,
			null,
			true,
			[true,false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=PRINT_GP_STATUS',
			{},
			bmdefprintergroup.status,
			null);
});

//初始化状态
bmdefprintergroup.initStatus = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=PRINT_GP_STATUS',
		success : function(data) {
			bmdefprintergroup.status =bmdefprintergroup.converStr2JsonObj(data);
			$('#search_status,#status').combobox({
			     valueField:"itemvalue",
			     textField:"valueAndText",
			     data:data,
			     loadFilter:function(data){
	    			if(data){
			       		 for(var i = 0;i<data.length;i++){
			       			 var tempData = data[i];
			       			 tempData.valueAndText = tempData.itemvalue+'→'+tempData.itemname;
			       		 }
	       	 		}
	    			return data;
	   			 },
			     panelHeight:"auto",
			  });
			  
		}
	});
};
//将数组封装成一个map
bmdefprintergroup.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};

//初始化用户信息
bmdefprintergroup.initUser = function(){
//	var locNo = bmdefprintergroup.locno;
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/authority_user/user.json',
		success : function(data) {
			$('#search_editor').combobox({
			     valueField:"workerNo",
			     textField:"workerName",
			     data:data,
			     panelHeight:"200",
			  }).combobox("select",data[0].workerNo);
		}
	});
};

//加载数据
bmdefprintergroup.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bm_defprinter_group/list.json?locno='+bmdefprintergroup.locno
    		});
};
//格式化码头类型
bmdefprintergroup.statusFormatter = function(value, rowData, rowIndex){
	return bmdefprintergroup.status[value];
};
bmdefprintergroup.hiedCreatorinfo = function(is){
	if(is){
		$("#creator_editor_info1,#creator_editor_info2").hide();
	}else{
		$("#creator_editor_info1,#creator_editor_info2").show();
	}
};
//新增
bmdefprintergroup.addInfo = function(){
	$('#showDialog').window({
		title:"新增",
		close:false,
		onBeforeClose:function(){
			$(".tooltip").remove();
		} 
	});
	$('#dataForm').form("clear");
	$("input[showType='show']").attr("disabled",false);
	$("input[showType='hide']").attr("disabled",true);
	$("#showDialog").window('open'); 
	$("#info_add").show();
	$("#info_edit").hide();
	bmdefprintergroup.hiedCreatorinfo(true);
};

//保存
bmdefprintergroup.save_do = function(){
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     if(!bmdefprintergroup.checkExist()){
     	return;
     }
	 //2. 保存
     var url = BasePath+'/bm_defprinter_group/addGroup';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('新增成功!');
					 bmdefprintergroup.loadDataGrid();
				}else{
					alert('新增失败,请联系管理员!',2);
				}
		    }
	   });
};

bmdefprintergroup.checkExist = function(){
	var printerGroupNo = $("#printerGroupNo").val();
	var printerGroupName = $("#printerGroupName").val();
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/bm_defprinter_group/checkExist',
		  data:{
			"printerGroupNo":printerGroupNo, 
			"locno":bmdefprintergroup.locno	
		  },
		  cache: false,
		  success: function(data){
		  	if(data=="exist"){
		  		alert("编码已经存在");
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	if(result){
		$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/bm_defprinter_group/checkExist',
		  data:{
			"printerGroupName":printerGroupName, 
			"locno":bmdefprintergroup.locno	
		  },
		  cache: false,
		  success: function(data){
		  	if(data=="exist"){
		  		alert("名称已经存在");
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
		});
	}
	return result;
};

bmdefprintergroup.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//删除
bmdefprintergroup.del = function(){
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
        		keyStr.push(item.locno+"|"+item.printerGroupNo);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bm_defprinter_group/deleteGroup';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	bmdefprintergroup.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
					 bmdefprintergroup.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

bmdefprintergroup.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//修改
bmdefprintergroup.edit = function(rowData,type){
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
	bmdefprintergroup.detail(rowData,type);
	//弹窗
	$("#showDialog").window('open'); 
	bmdefprintergroup.hiedCreatorinfo(false);
};

//修改
bmdefprintergroup.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	 //2. 保存
    var url = BasePath+'/bm_defprinter_group/editGroup';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 bmdefprintergroup.loadDataGrid();
				}else{
					alert('修改失败,请联系管理员!',2);
				}
		    }
	});
};

bmdefprintergroup.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		bmdefprintergroup.edit(checkedRows[0],"edit");
	}
};

bmdefprintergroup.detail = function(rowData,type){
	$('#dataForm').form('load',rowData);
	$("input[showType='show']").attr("disabled",true);
	$("input[showType='hide']").attr("disabled",false);
	$("#creator").val(rowData.creator);
	$("#createtm").val(rowData.createtm);
	$("#editor").val(rowData.editor);
	$("#edittm").val(rowData.edittm);
	
	//隐藏新增按钮，显示编辑按钮
	$("#info_add").hide();
	if(type=="edit"){
		$("#info_edit").show();
	}else{
		$("#info_edit").hide();
	}
};


bmdefprintergroup.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			bmdefprintergroup.locno = data.locno;
		}
	});
};


//搜索
bmdefprintergroup.search = function(){
	
	var createtmStart =  $('#createtmStart').datebox('getValue');
	var createtmEnd =  $('#createtmEnd').datebox('getValue');
	if(!isStartEndDate(createtmStart,createtmEnd)){    
		alert("创建时间开始时间不能大于结束时间");   
        return;   
    }
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bm_defprinter_group/list.json?locno='+bmdefprintergroup.locno;
    //3.加载明细
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

bmdefprintergroup.clearSearch = function(){
	$('#searchForm').form("clear");
};
bmdefprintergroup.closeUI = function(){
	$("#showDialog").window('close'); 
};
