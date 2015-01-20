//修改节点数据
function updateMenuNode(){
	
	var newNodeText = $('#name').val();

	if(!confirm("确认修改?"))return 
	
//	if( $("#type").val() != "0"  && !isLeaf()){
//		alert('目录必须为菜单资源类型!');
//		return ;
//	}
	
	if($("#id").val() == null || $("#id").val() == ""){
		alert('资源不存在! 请先增加');
		return ;
	}
	
	var data={
		"menuName":$("#name").val(),
		"menuType" : $("#type").val(),
		"menuRemark": $("#remark").val(),
		"menuSort" : $("#sort").val(),
		"menuFlag" : $("#flag").val(),
		"menuId":$("#id").val()
	};
	
	var url="put";
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
	
	var newNodeText = $('#name').val();

	if(newNodeText == node.text){
		alert('子目录名称不能与父级相同！');
		return false;
	}
	
	var url="post_menu";
	var data={
		"menuId":$("#id").val(),
		"menuName":$("#name").val(),
		"menuType" : $("#type").val(),
		"menuRemark": $("#remark").val(),
		"menuSort" : $("#sort").val(),
		"parentId":node.id
	};

	
	ajaxRequest(url,data,function(result){
		if(!result) return ;
		
		if(result.length == ""){
			alert("增加失败");
			return ;
			
		}

		if(!result.success){
			alert("增加失败,节点已增加模块,无法增加菜单");
			return;
		}
		
		var node = eval(result.data);
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
	var url = "get";
	var data={
		"menuId":nodeid
	};
	
	ajaxRequest(url,data,function(result){
		//result = result.replace(/(^\s*)|(\s*$)/g,'');
		//如果获取数据为空  则清空数据
		if(result.length == ""){
			clearInputValue();
			return ;
		}

		var node = eval(result);
		
		$("#id").val(node.id);
		$("#name").val(node.text);
		$("#name").blur();
		$("#remark").val(node.remark);
		$("#sort").attr("value",node.order);
		$("#sort").blur();
		if(node.isLeaf=="true"){
			$("#menuForm input[name='menuId']").val(node.id);
			$("#select2").empty();
			$("#select1").empty();
			$("#showWin").css("display","inline");
			$.getJSON(BasePath+"/authority_module/get_all_used_list.json?menuId="+node.id, function(data){
				  $.each(data.all, function(i,m){
				    $("<option value='"+m.id+"'>"+m.name+"</option>").appendTo("#select2");
				  });
				  $.each(data.used, function(i,m){
					  $("<option value='"+m.id+"'>"+m.name+"</option>").appendTo("#select1");
				  });
			});
		}else{
			$(".list_content").css("display","none");
		}
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
	
	var url = "delete";
	var data={
		"menuId":node.id
	};
	
	ajaxRequest(url,data,function(result){
		if(!result) return ;
		if(result.success){
			$("#select2").empty();
			$("#select1").empty();
			$("#showWin").hide();
			clearInputValue();
			remove();
			return ;
		}else{
			alert("删除失败,被使用的资源不允许删除");
		}
	});
}

function clearInputValue(){
	$("#id").val("");
	$("#name").val("");
	$("#name").blur();
	$("#type").attr("value","0");
	$("#remark").attr("value","");
	$("#sort").attr("value","");
	$("#sort").blur();
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


function modulesubmit(){
	var menuId=$("#menuForm input[name='menuId']").val();
	var usedList="";
	$.each($("#select1").find("option"),function(i,o){
		usedList+=usedList.length==0?o.value:","+o.value;
	});
	$.ajax({
		   type: "POST",
		   url: BasePath+"/authority_menu/post_relation",
		   data: "menuId="+menuId+"&usedList="+usedList,
		   success: function(msg){
			   if(!msg.success){
				   alert( "保存失败");
			   }else{
				   alert( "保存成功");
			   }
		   }
	});
}

function showWindow(){
	$('.list_content').show();
	$('.list_content').window({
		title:'增加模块关联',
	    width:600,    
	    height:400,    
	    modal:true   
	});
}