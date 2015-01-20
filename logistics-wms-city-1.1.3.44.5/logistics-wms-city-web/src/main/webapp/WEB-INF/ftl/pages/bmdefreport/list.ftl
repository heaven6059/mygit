<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>报表维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/bmdefreport/bmdefreport.js?version=1.0.6.1"></script>
</head>
<body class="easyui-layout">

			
	<#-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
			<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"bmdefreport.searchData()", "type":0},
	            {"title":"清除","iconCls":"icon-remove","action":"bmdefreport.searchClear('searchForm')", "type":0},
				{"title":"新增","iconCls":"icon-add","action":"bmdefreport.showAddDialog()","type":1},
				{"title":"修改","iconCls":"icon-edit","action":"bmdefreport.showEditDialog()","type":2},
				{"title":"删除","iconCls":"icon-del","action":"bmdefreport.deleteReport()","type":3},
				{"title":"关闭","iconCls":"icon-close","action":"closeWindow('报表维护')","type":0}
			 ]/>
	</div>
		
			
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<!--搜索start-->
			<div  data-options="region:'north',border:false" >
				 <div class="search-div">
				 	<form name="searchForm" id="searchForm" method="post" class="city-form">
				 		<table>
                     		<tr>
                     			<td class="common-td">报表编码：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="reportNo" id="reportNoCondition" /></td>
                     			<td class="common-td blank">报表名称：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="reportName" id="reportNameCondition"/></td>
                     			<td class="common-td blank">模板ID：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="moduleid" id="moduleidCondition"/></td>
                     			<td class="common-td blank">&nbsp;&nbsp;状&nbsp;&nbsp;态：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition"/></td>
                     		</tr>
                     	</table>
				 	</form>	
				 </div>
			</div>
			<!--显示列表-->
            <div data-options="region:'center',border:false">
            	<@p.datagrid id="mainDataGrid"  saveUrl=""  loadUrl="" defaultColumn=""  title="报表列表"
					isHasToolBar="false"  divToolbar="#locSearchDiv" height="450"  onClickRowEdit="false" singleSelect="false" pageSize="20"
					rownumbers="true" emptyMsg="" pageNumber=0
					columnsJsonList="[
						{field : 'id',checkbox :true},
						{field : 'reportNo',title : '报表编码',width : 100,align:'left'},
						{field : 'reportName',title : '报表名称',width : 100,align:'left'},
						{field : 'moduleid',title : '模板ID',width : 100,align:'left'},
						{field : 'status',title : '状态',width : 100,align:'left',formatter:bmdefreport.statusFormatter},
						{field : 'creator',title : '创建人',width : 100,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130},
						{field : 'editor',title : '编辑人',width : 100,align:'left'},
						{field : 'edittm',title : '编辑时间',width : 130}
						]"/>
            </div>
		</div>
	</div>
	<div id="showDetailDialog" class="easyui-window" title="报表明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div  data-options="region:'north',border:false" >
				 <div class="search-div">
				 	<form name="dataForm" id="dataForm" method="post" class="city-form">
				 		<table>
                     		<tr>
                     			<td class="common-td">报表编码：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="reportNo" id="reportNoSubmit" data-options="required:true"/></td>
                     			<td class="common-td blank">报表名称：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="reportName" id="reportNameSubmit" readOnly="true" data-options="required:true"/></td>
                     			<td class="common-td blank">模板ID：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="moduleid" id="moduleidSubmit" data-options="required:true"/></td>
                     			<td class="common-td blank">&nbsp;&nbsp;状&nbsp;&nbsp;态：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="status" id="statusSubmit" data-options="required:true"/></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td">报表内容：</td>
                     			<td colspan="5">
                     				<iframe src="../bm_defreport/iframe" id="iframe_" name="iframe_" style="border:none;height:28px;width:100%">
                     				</iframe>
                     			</td>
                     			<td colspan="2">	
                     				<a id='add'  href="javascript:bmdefreport.addSave();"  class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
                     				<a id='edit' href="javascript:bmdefreport.editSave();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
                     				&nbsp;&nbsp;&nbsp;&nbsp;
                     				<a href="javascript:bmdefreport.closeWindow('showDetailDialog');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
                     			</td>
                     		</tr>
                     	</table>
				 	</form>	
				 </div>
			</div>
	</div>
</body>
</html>