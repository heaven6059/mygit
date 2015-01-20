<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>仓库维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/bmdefloc/bmdefloc.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
      <#-- 工具菜单div -->
	<div data-options="region:'north',border:false" class="toolbar-region">
	   <@p.toolbar id="mainToolBar"   listData=[
					 {"title":"查询","iconCls":"icon-search","action":"bmdefloc.searchLoc()", "type":0},
					 {"title":"清除","iconCls":"icon-remove","action":"bmdefloc.searchLocClear()", "type":0},
	                 {"title":"新增","iconCls":"icon-add","action":"showAdd()", "type":1},
	                 {"title":"修改","iconCls":"icon-edit","action":"bmdefloc.showModify()","type":2},
	                 {"title":"删除","iconCls":"icon-del","action":"deleteFefloc()","type":3},
			         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('仓库维护')","type":0}
	               ]
		  />
	</div>
	<div data-options="region:'center',border:false">
	  		 <div class="easyui-layout" data-options="fit:true">
	 		 	<div id="locSearchDiv">
	       		 	<form name="searchForm" id="searchForm" method="post">
					   	仓库编码：<input class="easyui-validatebox ipt" style="width:110px" name="locno" id="locNoCondition" />
						仓库名称：<input class="easyui-validatebox ipt" style="width:110px" name="locname" id="locNameCondition" />
					</form>
				</div>
  		 		 <div data-options="region:'center',border:false">
  		 		 		<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="仓库列表"
		                   isHasToolBar="false" divToolbar="#locSearchDiv"  height="470"  onClickRowEdit="false"    pagination="true"
			               rownumbers="true"  singleSelect = "false"
			               columnsJsonList="[
			                  {field : 'ck',title : '',width : 150, checkbox:true},
			                  {field : 'locno',title : '仓库编码',width : 150},
			                  {field : 'locname',title : '仓库名称',width : 150},
			                  {field : 'createFlagStr',title : '创建方式',width : 100},
			                  {field:'creator',title:'创建人',width:100},
 			                  {field:'createtm',title : '创建时间',width : 125,sortable:true},
			                  {field:'editor',title:'编辑人',width:100},
 			                  {field:'edittm',title : '编辑时间',width : 125,sortable:true},
 			                  {field:'memo',title:'备注',width:100}
			                 ]"   
			                 jsonExtend='{
			                      onDblClickRow:function(rowIndex, rowData){
				                	  //双击方法
				                   	  bmdefloc.showEdit(rowData);
				                  }
		                     }'
				           />
  		 		 </div>
	  		 </div>
	</div>
	<div id="showDialog"  class="easyui-window" title="新增"  
			     style="height:200px;width:450px;"  data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
			    minimizable:false"> 
			     <form name="dataForm" id="dataForm" method="post" class="city-form" style="margin-top:10px;">
			     	<input type="hidden" name="locType" id="locType">
			     	<table>
			     		<tr>
			     			<td class="common-td blank">仓库编码：</td>
			     			<td><input class="easyui-validatebox ipt" style="width:130px" name="locno" id="locno" required="true" missingMessage='仓库编码为必填项!'  data-options="validType:['vnCharOrNumber[\'只能输入字母或数字的组合\']','vLength[0,10,\'最多只能输入10个字符\']']"  /></td>
			     			<td class="common-td blank">仓库名称：</td>
			     			<td><input class="easyui-validatebox ipt" style="width:130px" name="locname" id="locname" required="true" data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"   /></td>
			     		</tr>
			     		<tr>
			     			<td class="common-td blank">备注：</td>
			     			<td colspan="3"><input class="easyui-validatebox ipt" style="width:100%" name="memo" id="memo" data-options="validType:['vLength[0,100,\'最多只能输入100个字符\']']"/></td>
			     		</tr>
			     		<tr class="creatorDiv">
     						<td class="common-td blank">创建人：</td>
     						<td><input class="easyui-validatebox ipt" style="width:130px" name="creator" id="creator"  disabled="disabled"/></td>
     						<td class="common-td blank">创建时间：</td>
     						<td><input class="easyui-validatebox ipt" style="width:130px" name="createtm" id="createtm"  disabled="disabled"/></td>
			     		</tr>
			     		<tr class="creatorDiv">
	 						<td class="common-td blank">修改人：</td>
	 						<td><input class="easyui-validatebox ipt" style="width:130px" name="editor" id="editor"  disabled="disabled"/></td>
	 						<td class="common-td blank">修改时间：</td>
	 						<td><input class="easyui-validatebox ipt" style="width:130px" name="edittm" id="edittm"  disabled="disabled"/></td>
			     		</tr>
			     		<tr>
			     			<td colspan="4" style="text-align:center">
			     				<a id="info_save" href="javascript:bmdefloc.addFefloc()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			     				<a id="info_update" href="javascript:bmdefloc.modifyFloc();" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
			     				<a id="info_close" href="javascript:bmdefloc.closeFefloc();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			     			</td>
			     		</tr>
			     	</table>
			</form>	
	</div>	
</body>
</html>