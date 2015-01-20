
var bmdefreport = {};
bmdefreport.user;
bmdefreport.showAddDialog = function(){
	$("#add").show();
	$("#edit").hide();
	$("#reportNoSubmit").attr('readOnly',false);
	
	bmdefreport.loadDialog({},'add');
};
bmdefreport.showEditDialog = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要修改的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能修改一条记录!',1);
		return;
	}
	var data = checkedRows[0];
	bmdefreport.loadDialog(data,"edit");
	$("#add").hide();
	$("#edit").show();
	$("#reportNoSubmit").attr('readOnly',true);
	bmdefreport.loadDialog('edit');
};
bmdefreport.deleteReport = function(){
	var checkedRows = $("#mainDataGrid").datagrid("getChecked");// 获取所有勾选checkbox的行
	if(checkedRows.length < 1){
		alert('请选择要删除的记录!',1);
		return;
	}else if(checkedRows.length>1){
		alert('只能删除一条记录!',1);
		return;
	}
	var data = checkedRows[0];
	$.messager.confirm("确认","您确定要删除这条记录吗？", function (r){
		if(!r){
			return;
		}
		var param = {
				reportNo:data.reportNo
		};
		var url = BasePath+'/bm_defreport/deleteReport';
		$.post(url, param, function(result) {
			if(result.result == 'success'){
				alert('删除成功!');
				$("#mainDataGrid").datagrid('load');
			}else{
				alert(result.result,2);
			}
		}, "JSON").error(function() {
	    	alert('删除失败!',1);
	    });
	});
};
bmdefreport.loadDialog = function(data,type){
	var title = '报表明细';
	if(type == 'add'){
		$("#dataForm").form('clear');
		window.frames["iframe_"].removeFile();
		title = '新增报表';
	}else if(type == 'edit'){
		title = '修改报表';
		$("#reportNoSubmit").val(data.reportNo);
		window.frames["iframe_"].setFileName(data.reportName);
		$("#moduleidSubmit").val(data.moduleid);
		$("#statusSubmit").combobox('setValue',data.status);
	}else{
		return;
	}
	$("#showDetailDialog").window({title:title});
	$("#showDetailDialog").window('open');
};
bmdefreport.closeWindow = function(id){
	$("#"+id).window('close');
};
bmdefreport.addSave = function(){
	var fromObj=$('#dataForm');
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
		return ;
	}
	var url = BasePath + '/bm_defreport/addSave';
	var params = {
			locno:bmdefreport.user.locno,
			loginName:bmdefreport.user.loginName,
			reportNo:$("#reportNoSubmit").val(),
			reportName:$("#reportNameSubmit").val(),
			moduleid:$("#moduleidSubmit").val(),
			status:$("#statusSubmit").combobox('getValue')
	};
	$.post(url, params, function(result) {
		if(result.result == 'success'){
			alert('保存成功!');
			bmdefreport.closeWindow('showDetailDialog');
			$("#mainDataGrid").datagrid('load');
		}else{
			alert(result.result,2);
		}
	}, "JSON").error(function() {
    	alert('保存失败!',1);
    });
};
bmdefreport.editSave = function(){
	var fromObj=$('#dataForm');
	var validateForm= fromObj.form('validate');
	if(validateForm==false){
		return ;
	}
	var url = BasePath + '/bm_defreport/editSave';
	var params = {
			locno:bmdefreport.user.locno,
			loginName:bmdefreport.user.loginName,
			reportNo:$("#reportNoSubmit").val(),
			reportName:$("#reportNameSubmit").val(),
			moduleid:$("#moduleidSubmit").val(),
			status:$("#statusSubmit").combobox('getValue')
	};
	$.post(url, params, function(result) {
		if(result.result == 'success'){
			alert('保存成功!');
			bmdefreport.closeWindow('showDetailDialog');
			$("#mainDataGrid").datagrid('load');
		}else{
			alert(result.result,2);
		}
	}, "JSON").error(function() {
		alert('保存失败!',1);
	});
};
bmdefreport.searchData = function(){
	var fromObjStr=convertArray($('#searchForm').serializeArray());
	var queryMxURL=BasePath+'/bm_defreport/list.json?locno='+bmdefreport.user.locno;
	var reqParam = eval("(" +fromObjStr+ ")");
	bmdefreport.loadGridDataUtil('mainDataGrid', queryMxURL, reqParam);
};
bmdefreport.searchClear = function(id){
	$("#"+id).form('clear');
};
bmdefreport.ajaxRequest = function(url,reqParam,async,callback){
	$.ajax({
		  async:async,
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
};
bmdefreport.loadGridDataUtil = function(gridDataId,url,queryParams){
    var tempObj = $('#'+gridDataId);
    tempObj.datagrid( 'options' ).queryParams = queryParams;
    tempObj.datagrid( 'options' ).url = url;
    tempObj.datagrid( 'load' );
};
bmdefreport.status = {};
bmdefreport.statusFormatter = function(value, rowData, rowIndex){
	return bmdefreport.status[value];
};
bmdefreport.initStatus = function(data){
	$('#statusCondition').combobox({
		data:data,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"auto"
	});
	$('#statusSubmit').combobox({
		data:data,
		valueField:"itemvalue",
		textField:"itemnamedetail",
		panelHeight:"auto"
	});
	var temp = {};
	for(var index=0;index<data.length;index++){
		temp[data[index].itemvalue] = data[index].itemname;
	}
	bmdefreport.status = temp;
};
$(document).ready(function(){
	bmdefreport.ajaxRequest(BasePath+'/initCache/getCurrentUser',{},false,function(u){bmdefreport.user=u;});
	var status = [{"itemvalue":"0","itemname":"不可用","itemnamedetail":"0→不可用"},{"itemvalue":"1","itemname":"可用","itemnamedetail":"1→可用"}];
	bmdefreport.initStatus(status);
});
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
function setFileName(fileName){
	$("#reportNameSubmit").val(fileName);
};
function removeFileName(){
	$("#reportNameSubmit").val("");
};