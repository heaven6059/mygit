<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>码表定义</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
</head>
	<script>
	    var roleId="${roleId}";
	    var x=BasePath+"/systemsetting/queryMenuTree.htm?roleId=${roleId}";
	    
	  
	</script>

<body>

<form  name="roleResources" id="roleResources" method="post">

	
<div  class="easyui-panel"  style="padding:0px;background:#F4F4F4" 
     iconCls="icon-mini-tis">
     
     
  <@p.datagrid id="mainDataGrid"   loadUrl="/systemsetting/queryMenuTree.htm?roleId=${roleId}" saveUrl=""  defaultColumn=""   title="模块信息"
           isHasToolBar="false" height="770"   onClickRowEdit="true" singleSelect="false"   pagination="false"
           divToolbar="#role_DtlToolDiv"
           columnsJsonList="[
              {field:'checked',checkbox:true}, 
              {field : 'moduleId',title : '菜单编号',width : 120},
              {field : 'moduleName',title : '菜单名称',width : 200},
              {field : 'operPermissions',title : '操作权限',width : 350,editor:{type:'combotree',options:{multiple:true,panelHeight:'auto'} }}
             
             ]" 
              
   jsonExtend='{
       onLoadSuccess:function(data){
            if(data){
               $.each(data.rows, function(index, item){
                   if(item.checked){
					   $("#mainDataGrid").datagrid("checkRow", index);
					}
               });
            }
       },
       onSelect:function(rowIndex, rowData){
              $("#mainDataGrid").datagrid("beginEdit", rowIndex);
              var editor = $("#mainDataGrid").datagrid("getEditor", { index: rowIndex, field: "operPermissions" });
              var $combotree = $(editor.target);
              $combotree.combotree({    
			    url: BasePath+"/systemsetting/queryModuelOperator?roleId="+roleId+"&moduleId="+rowData.moduleId,
			    width:350
			    
			});  


       }
       ,
       onCheck:function(rowIndex, rowData){
              
       }
   
   }'/>
   
   <div id="role_DtlToolDiv">
	<a href="javascript:submitroleResourcesForm(${roleId})" class="easyui-linkbutton" iconCls="icon-add" plain="true">保存</a>
	<a href="javascript:toList()" class="easyui-linkbutton" iconCls="icon-back" plain="true">返回</a>
</div>
</div>		
	
	<input type="hidden" name="roleId" value="${roleId?default('')}" />
	<input type="hidden" name="allCheckResources" id="allCheckResources"/>
</form>
</body>
</html>