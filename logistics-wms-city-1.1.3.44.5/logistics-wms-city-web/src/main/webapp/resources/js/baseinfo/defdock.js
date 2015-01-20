var defdock = {};
//码头类型
defdock.dockType = {};
defdock.locno;
//定位类型
defdock.localType= {};
defdock.ownerData = {};
//是否有调节板
defdock.adjustBoard = {
	"0":"没有",
	"1":"有"
};

//初始化码头类型下拉框
defdock.initDockType = function(){
	wms_city_common.comboboxLoadFilter(
			["dockType","dockTypeCondition"],
			null,
			null,
			null,
			true,
			[false,true],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=DOCK_TYPE',
			{},
			defdock.dockType,
			null);
};
//将数组封装成一个map
defdock.converStr2JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].itemvalue +"\":\""+data[i].itemname+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//初始化定位类型下拉框
defdock.initLocalType = function(){
	wms_city_common.comboboxLoadFilter(
			["locateType"],
			null,
			null,
			null,
			true,
			[false],
			BasePath+'/initCache/getLookupDtlsList.htm?lookupcode=LOCATE_TYPE',
			{},
			defdock.localType,
			null);
};

//加载委托业主信息
defdock.loadOwner = function(){
	$.ajax({
		async : true,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/entrust_owner/get_biz',
		success : function(data) {
			defdock.ownerData=defdock.converStr3JsonObj(data);
			$('#ownerNo').combobox({
			    data:data,
			    valueField:'ownerNo',    
			    textField:'ownerName',
			    panelHeight:"auto"
			});
			$('#ownerNo').combobox("select",data[0].ownerNo);  
		}
	});
};
//将数组封装成一个map
defdock.converStr3JsonObj = function(data){
	var str = "";
	for(var i = 0,length = data.length;i<length;i++){
		str = str+"\""+data[i].ownerNo +"\":\""+data[i].ownerName+"\"";
		if(i<length-1){
			str = str+",";
		}
	}
	return eval("({"+str+"})");
};
//加载数据
defdock.loadDataGrid = function(){
    $('#dataGridJG').datagrid(
    		{
    			'url':BasePath+'/bm_defdock/list.json?locno='+defdock.locno,
    			'title':"码头资料维护",
    			'pageNumber':1
    		});
};
//格式化码头类型
defdock.columnTypeFormatter = function(value, rowData, rowIndex){
	return defdock.dockType[value];
};
//是否有调节板
defdock.columnAdjustBoardFormatter = function(value,rowData,rowIndex){
	return defdock.adjustBoard[value];
};
//定位类型
defdock.columnLocalTypeFormatter = function(value,rowData,rowIndex){
	return defdock.localType[value];
};

//委托业主
defdock.ownerFormatter = function(value,rowData,rowIndex){
	return defdock.ownerData[value];
};
//新增

//保存
defdock.save_do = function(){
    var fromObj=$('#dataForm');
     //1.校验必填项
     var validateForm= fromObj.form('validate');
     if(validateForm==false){
          return ;
     }
     if(!defdock.checkExist()){
     	return;
     }
	 //2. 保存
     var url = BasePath+'/bm_defdock/addDefdock';
     fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('新增成功!');
					 defdock.loadDataGrid();
				}else{
					alert('新增失败,请联系管理员!',2);
				}
		    }
	   });
};

defdock.checkExist = function(){
	var dockNo = $("#dockNo").val();
	var result;
	$.ajax({
		  async : false,
		  type: 'POST',
		  url: BasePath+'/bm_defdock/checkExist',
		  data:{
			"dockNo":dockNo  	
		  },
		  cache: true,
		  success: function(data){
		  	if(data=="exist"){
		  		alert("该仓别下的码头编码已经存在");
		  		result=false;
		  	}else{
		  		result=true;
		  	}
		  }
	});
	return result;
};

defdock.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
//删除
defdock.del = function(){
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
        		keyStr.push(item.locno+"|"+item.dockNo);
        	});               
        	//console.log(names.join(","));
        	//alert(locnoStrs.join(","));
            //2.绑定数据
            var url = BasePath+'/bm_defdock/deleteDefdock';
        	var data={
        	    "keyStr":keyStr.join(",")
        	};
        	//3. 删除
        	defdock.ajaxRequest(url,data,function(result){
        		 if(result=='success'){
        			 //4.删除成功,清空表单
        			 alert('删除成功!');
					 defdock.loadDataGrid();
        		 }else{
        			 alert('删除失败,请联系管理员!',2);
        		 }
        	}); 
        }  
    });  
};

defdock.ajaxRequest = function(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};

//修改
defdock.edit = function(rowData,type){
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
	defdock.detail(rowData,type);
	//弹窗
	$("#showDialog").window('open'); 
};

//修改
defdock.edit_do = function(rowData){
    var fromObj=$('#dataForm');
    //1.校验必填项
    var validateForm= fromObj.form('validate');
    if(validateForm==false){
         return ;
    }
	 //2. 保存
    var url = BasePath+'/bm_defdock/editDefdock';
    fromObj.form('submit', {
			url: url,
			onSubmit: function(){
			},
			success: function(data){
				if(data=="success"){
					$("#showDialog").window('close'); 
					 alert('修改成功!');
					 defdock.loadDataGrid();
				}else{
					alert('修改失败,请联系管理员!',2);
				}
		    }
	});
};

defdock.editInfo = function(){
	var checkedRows = $("#dataGridJG").datagrid("getChecked");// 获取所有勾选checkbox的行
	//debugger;
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}else{
		defdock.edit(checkedRows[0],"edit");
	}
};

defdock.detail = function(rowData,type){
	$('#dataForm').form('load',rowData);
	//业主信息
	$('#ownerNo').combobox("select",rowData.ownerNo); 
	$("#dockNo").val(rowData.dockNo);
	$("#dockNo").attr("disabled",true);
	$("#dockNoHide").attr("disabled",false);
	$("#dockNoHide").val(rowData.dockNo);
	$("#dockName").val(rowData.dockName);
	$('#dockNo').validatebox('reset');
	$('#dockName').validatebox('reset');
	//码头类型
	$('#dockType').combobox("select",rowData.dockType);  
	//定位类型
	$('#locateType').combobox("select",rowData.locateType);
	$('input:radio[name=adjustBoard]')[rowData.adjustBoard].checked = true;
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

$(document).ready(function(){
	//初始化码头类型
	defdock.initDockType();
	//初始化定位类型
	defdock.initLocalType();
	//初始化业主
	//defdock.loadOwner();
	
	defdock.loadLoc();
	//加载列表数据
	//defdock.loadDataGrid();
	//$("#info_add").click(defdock.save_do);
	//$("#info_edit").click(defdock.edit_do);
	$("#delBtn").click(defdock.del);
});

defdock.loadLoc = function(){
	$.ajax({
		async : false,
		cache : false,
		type : 'GET',
		dataType : "json",
		url:BasePath+'/initCache/getCurrentUser',
		success : function(data) {
			defdock.locno = data.locno;
		}
	});
};

//工具栏操作
//新增
defdock.addInfo = function(){
	$('#showDialog').window({
		title:"新增"
	});
	
	var dockTypeData = $('#dockType').combobox('getData');
	if(dockTypeData != null && dockTypeData[0] != null) {
		$('#dockType').combobox("select",dockTypeData[0].itemvalue);  
	}
	var locateTypeData = $('#locateType').combobox('getData');
	if(locateTypeData != null && locateTypeData[0] != null) {
		$('#locateType').combobox("select",locateTypeData[0].itemvalue);
	}
	
	$('input:radio[name=adjustBoard]')[0].checked = true;
	$("#dockNo").val("");
	$("#dockNo").attr("disabled",false);
	$("#dockNoHide").attr("disabled",true);
	$('#dockNo').validatebox('reset');
	$("#dockName").val("");
	$('#dockName').validatebox('reset');
	$("#creatorinfo").hide();
	$("#editorinfo").hide();
	$("#showDialog").window('open'); 
	$("#info_add").show();
	$("#info_edit").hide();
};
/**清楚查询条件**/
defdock.searchClear = function(){
	$('#searchForm').form("clear");
	//searchForm('/bm_defdock/list.json?locno='+defdock.locno);
};

/**查询码头js方法**/
defdock.searchDock = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bm_defdock/list.json?locno='+defdock.locno;
    $( "#dataGridJG").datagrid( 'options' ).queryParams= eval("(" +fromObjStr+ ")");
    $( "#dataGridJG").datagrid( 'options' ).url=queryMxURL;
    $( "#dataGridJG").datagrid( 'load' );
};
//主要是推荐这个函数。它将jquery系列化后的值转为name:value的形式。
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
defdock.closeWin = function(id){
	$("#"+id).window('close'); 
};