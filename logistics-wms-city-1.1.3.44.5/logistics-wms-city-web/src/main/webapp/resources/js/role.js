var path=$("#path").val();

/**
 * 增加角色
 */
function toAddRole(){
	
	//alert(checkPowerOperations(14,3));
	window.location.href = "toAddRole.htm";
}


/**
* 增加角色
*/
function backList(){
	window.location.href = "queryRoleList.htm";
}

/**
 * 修改角色
 */
function toUpdateRole(id){
	//var params = "id="+id;
	//openwindow("../../systemmgmt/authorityRole/toUpdateRole.sc?id="+id,600,400,"修改角色");
	//showThickBox("增加角色","../../systemmgmt/authorityRole/toUpdateRole.sc?TB_iframe=true&height=400&width=600&modal=true",false,params);
	window.location.href = "toUpdateRole.htm?id="+id;
 }

/**
 * 删除角色
 */
function removeRole(id){
	if(confirm("确定要删除该角色")){
		window.location.href = "d_remove.htm?id="+id;
	}
}

/**
 *到用户角色资源分配
 * @param id
 */
function allotRoleResource(id){
	var params = "roleId="+id;
	//openwindow("toAllotRoleResource.htm?roleId="+id,650,550,"角色分配资源");
	//showThickBox("角色分配资源","../../systemmgmt/authorityRole/toAllotRoleResource.sc?TB_iframe=true&height=400&width=600",false,params);
	window.location.href = "toAllotRoleResource.htm?roleId="+id;
}






/**
 * 提交资源分配表单
 */
function submitroleResourcesForm(roleId){
	
	
	$("#allCheckResources").attr("value",getCheckedAll(roleId));
	   var url = BasePath+'/systemsetting/u_allotRoleModuel';
	    $('#roleResources').form('submit', {
			url: url,
			onSubmit: function(param){
				
			},
			success: function(){
				 
				toList();
				
		    },
			error:function(){
				
			}
	   });
	
	    

//	var form = document.forms[0];
//	form.target="mbdif";		//表单提交后在父页面显示结果
//	form.submit();
//	window.top.TB_remove();
	
//	 $('#mainDataGrid').datagrid('options').queryParams={x:'1'};
//	   $('#mainDataGrid').datagrid('options').url=BasePath+'/systemsetting/queryMenuTree.htm?roleId='+roleId;
//	   $('#mainDataGrid').datagrid('load');
//	   $('#mainDataGrid').datagrid('options').queryParams="";
}

function toList(){
    var tab = parent.$('#centerFrame').tabs('getSelected');    
    parent.$('#centerFrame').tabs('update', {
		tab: tab,
		options: {
			content:'<iframe src="'+BasePath+'/queryRoleList.htm" frameborder="0"  border="0" marginwidth="0" marginheight="0" scrolling="no"   style="width:100%" height="100%"></iframe> '
		}
	  });
 }


function getCheckedAll(roleId){
	endEditCommon('mainDataGrid');
	var checkedItems = $('#mainDataGrid').datagrid('getChecked');
	var names = [];
	$.each(checkedItems, function(index, item){
	     names.push(item.moduleId+"&"+item.operPermissions);
	     
	     
	});
  
   return names.join("%");
}

//获取选中的节点(选中的checked)
function getCheckedNodes() {
	var nodes = $('#resourceTree').tree('getChecked');
	var s = '';
	for (var i = 0; i < nodes.length; i++) {
		if (s != '') s += ',';
		s += nodes[i].id;
	}
	return s;
}

/**
 * 提交表单
 */
function submitForm(){
	
	var form = document.forms[0];
	form.target="mbdif";		//表单提交后在父页面显示结果
	form.submit();
	window.top.TB_remove();
}

jQuery.extend(jQuery.fn.datagrid.defaults.editors, {
    combotree: {
        init: function(container, options){
            var editor = jQuery('<input type="text">').appendTo(container);
            editor.combotree(options);
            return editor;
        },
        destroy: function(target){
            jQuery(target).combotree('destroy');
        },
        getValue: function(target){
            var temp = jQuery(target).combotree('getValues');
            //alert(temp);
            return temp.join(',');
        },
        setValue: function(target, value){
            var temp = value.split(',');
            //alert(temp);
            jQuery(target).combotree('setValues', temp);
        },
        resize: function(target, width){
            jQuery(target).combotree('resize', width);
        }
}
});



