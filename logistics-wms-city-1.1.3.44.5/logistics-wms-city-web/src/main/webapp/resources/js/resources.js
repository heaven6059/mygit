//修改节点数据
function updateMenuNode(){
	
	var newNodeText = $('#menuName').val();
	
	

	if(!confirm("确认修改?"))return 
	
//	if( $("#type").val() != "0"  && !isLeaf()){
//		alert('目录必须为菜单资源类型!');
//		return ;
//	}
	
	if($("#menuId").val() == null || $("#menuId").val() == ""){
		alert('资源不存在! 请先增加');
		return ;
	}
	
	var data={
		"menuName":$("#menuName").val(),
		"type" : $("#type").val(),
		"remark": $("#remark").val(),
		"sort" : $("#sort").val(),
		"flag" : $("#flag").val(),
		"menuId":$("#menuId").val(),
		"tempMenuUrl":$("#memuUrl").val()
	};
	
	var url="updateResource.htm";
	ajaxRequest(url,data,function(result){
		if(!result) return ;
		
		if(result.length == ""){
			alert("修改失败");
			return ;
			
		}
		
		
		var selectNode = $('#resourceTree').tree('getSelected');
		var resultNode = eval(result);
		selectNode.text= resultNode.text;
	    $("#resourceTree").tree("update",selectNode);
		
		alert("修改成功");
	});
	
	
}

//增加节点
function addMenuNode(){
	var node = $('#resourceTree').tree('getSelected');
	if(!node){
		alert('请选择一个节点！');
		return false;
	}
	
	var newNodeText = $('#menuName').val();

	if(newNodeText == node.text){
		alert('子目录名称不能与父级相同！');
		return false;
	}
	
	var url="addResource.htm";
	var data={
		"menuId":$("#menuId").val(),
		"menuName":$("#menuName").val(),
		"tempMenuUrl": $("#memuUrl").val(),
		"type" : $("#type").val(),
		"remark": $("#remark").val(),
		"sort" : $("#sort").val(),
		"flag" : $("#flag").val(),
		"parentId":node.id
	};

	
	ajaxRequest(url,data,function(result){
		if(!result) return ;
		
		if(result.length == ""){
			alert("增加失败");
			return ;
			
		}

		var node = eval(result);
		if(node.id){
			var nodeData = [{
				id:node.id,
				text:node.text
			}];
			
			append(nodeData);
			clearInputValue();
		}
	});
}

function loadNodeData(nodeid){
	var url = "queryResourceById.htm";
	var data={
		"id":nodeid
	};
	
	ajaxRequest(url,data,function(result){
		
		//result = result.replace(/(^\s*)|(\s*$)/g,'');
		//如果获取数据为空  则清空数据
		if(result.length == ""){
			clearInputValue();
			return ;
		}

		var node = eval(result);
		
		$("#menuId").attr("value",node.id);
		$("#menuName").attr("value",node.text);
		$("#menuName").blur();
		$("#memuUrl").attr("value",node.url);
		$("#type").attr("value",node.type);
		$("#remark").attr("value",node.remark);
		$("#sort").attr("value",node.order);
		$("#sort").blur();
		$("#flag").val(node.attributes);
	});
	
}

function removeMenuNode(){
	
	var node = $('#resourceTree').tree('getSelected');
	if(!node){
		alert('请选择要删除的节点');
		return ;
	}
	
	if(!confirm("确认删除?"))return 
	
	if(node.attributes != null && node.attributes.struc != null && node.attributes.struc == "root"){
		alert("根目录不能删除");
		return false;
	}
	
	var url = "delResource.htm";
	var data={
		"menuId":node.id
	};
	
	ajaxRequest(url,data,function(result){
		if(!result) return ;
		result = result.replace(/(^\s*)|(\s*$)/g,'');
		if(result == "success"){
			clearInputValue();
			remove();
			return ;
		}else{
			alert("删除失败,被使用的资源不允许删除");
		}
	});
}

function clearInputValue(){
	$("#menuId").attr("value","");
	$("#menuName").attr("value","");
	$("#menuName").blur();
	$("#memuUrl").attr("value","");
	$("#type").attr("value","0");
	$("#remark").attr("value","");
	$("#sort").attr("value","");
	$("#sort").blur();
	$("#flag").val("all");
}

//发达ajax请求
function ajaxRequest(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
}



/**
 * 提交资源分配表单
 */
function submitSelectOrgForm(){
	
	$("#allCheckResources").attr("value",getCheckedNodes());
	
	var form = document.forms[0];
	form.target="mbdif";		//表单提交后在父页面显示结果
	form.submit();
	window.top.TB_remove();
}