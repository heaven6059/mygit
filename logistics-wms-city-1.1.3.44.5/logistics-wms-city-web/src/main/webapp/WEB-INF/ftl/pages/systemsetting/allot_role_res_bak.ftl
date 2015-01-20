<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>码表定义</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
</head>
<body>

<form action="u_allotRoleResource.htm" name="roleResources" id="roleResources" method="post">
	<script>document.write("<input type='hidden' name='parentSourcePage' value='"+getThickBoxUrl()+"'/>");</script>
	<input type="button" onclick="submitroleResourcesForm()" value="提交"/>
	<select id="cc" class="easyui-combotree" data-options="url:'systemsetting/queryMenuTree.htm?roleId=42'" multiple style="width:200px;"></select>
	
  <@p.datagrid id="mainDataGrid"   loadUrl="/systemsetting/queryMenuTree.htm?roleId= ${roleId}" saveUrl=""  defaultColumn=""  
           isHasToolBar="false" width="620"   onClickRowEdit="false" singleSelect="false" pageSize="10"  pagination="false"
           columnsJsonList="[
              {field:'checked',checkbox:true}, 
              {field : 'moduleId',title : '菜单编号',width : 120},
              {field : 'moduleName',title : '菜单名称',width : 200},
               {field : 'itemvalue',title : '项目值',width : 200,
                 editor:{
	             type:'checked',
	             options:{
	                required:true,
	                missingMessage:'项目值为必填项!'
	             }
	          }
          },
             
             
             ]" 
              
   jsonExtend='{onLoadSuccess:function(data){
            if(data){
               $.each(data.rows, function(index, item){
                   if(item.checked){
					   $("#mainDataGrid").datagrid("checkRow", index);
					}
               });
            }
   }}'/>
	
	
	<input type="hidden" name="id" value="${id?default('')}" />
	<input type="hidden" name="allCheckResources" id="allCheckResources"/>
</form>
</body>
</html>