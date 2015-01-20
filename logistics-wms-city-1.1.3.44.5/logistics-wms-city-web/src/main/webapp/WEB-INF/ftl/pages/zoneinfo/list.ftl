<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>区域管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/zoneinfo.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
			<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
			<#--工具条-->
			<@p.toolbar id="toolBarOne" listData=[
					{"title":"查询","iconCls":"icon-search","action":"zoneinfo.searchZoneinfo();","type":0},
		       		{"title":"清空","iconCls":"icon-remove","action":"zoneinfo.searchZoneinfoClear();","type":0},
		       		{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('区域管理')","type":0}
				]
			/>
			</div>
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true">
 		<div data-options="region:'north',border:false" >
	 		 	<div class="search-div" id="search-div">
			       	<form name="searchForm" id="searchForm" method="post" class="city-form" >
			       		 <table>
				       		 	<tr>
				       		 		<td class="common-td">区域编码：</td>
				       		 		<td><input class="easyui-combobox" style="width:120px" name="zoneNo" id="zoneNoCondition" /></td>
				       		 		<td class="common-td blank">区域操作码：</td>
				       		 		<td><input class="easyui-combobox" style="width:120px" name="zoneCode" id="zoneCodeCondition" /></td>
				       		 		<td class="common-td blank">区域名称：</td>
				       		 		<td><input class="easyui-validatebox ipt" style="width:120px" name="zoneName" id="zoneName" /></td>
				       		 	</tr>
				       		 	<tr>
				       		 		<td class="common-td">建档人：</td>
				       		 		<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition"/></td>
				       		 		<td class="common-td blank">建档时间：</td>
				       		 		<td><input class="easyui-datebox" type="text" style="width:120px" name="createtmBegin" id="createtmBeginCondition"/></td>
				       		 		<td class="common-line">&mdash;</td>
				       		 		<td><input class="easyui-datebox" type="text" style="width:120px" name="createtmEnd" id="createtmEndCondition"/></td>
				       		 	</tr>
				       		 	<tr>
				       		 		<td class="common-td">修改人：</td>
				       		 		<td><input class="easyui-validatebox ipt" style="width:120px" name="editor" id="editorCondition"/></td>
				       		 		<td class="common-td blank">修改时间：</td>
				       		 		<td><input class="easyui-datebox" type="text" style="width:120px" name="edittmBegin" id="edittmBeginCondition"/></td>
				       		 		<td class="common-line">&mdash;</td>
				       		 		<td><input class="easyui-datebox" type="text" style="width:120px" name="edittmEnd" id="edittmEndCondition"/></td>
				       		 	</tr>
			       		 </table>
					</form>
				</div> 
         </div>
         <div id="soneInfoList" data-options="region:'center',border:false">
			 		<@p.datagrid id="dataGridJG" name="" title="区域列表" loadUrl="" saveUrl=""   defaultColumn="" 
			 			isHasToolBar="false"  divToolbar=""  height="500" width="" onClickRowEdit="false" singleSelect="true" pageSize='20'  
						pagination="true" rownumbers="true" emptyMsg="" pageNumber=0
			 			columnsJsonList="[
			 				{field:'zoneNo',title:'区域编码',width:70},
			 				{field:'zoneCode',title:'区域操作码',width:80},
			 				{field:'zoneName',title:'区域名称',width:150,align:'left'},
						  	{field:'creator',title:'建档人',width:100,align:'left'},
			              	{field:'createtm',title : '建档时间',width : 125,sortable:true},
			              	{field:'editor',title:'修改人',width:100,align:'left'},
			              	{field:'edittm',title : '修改时间',width : 125,sortable:true},
			              	{field:'remarks',title:'备注',width:200,align:'left'}
			 			]"
			 		/>
			 	</div>     
 		</div>
 </div>
</body>
</html>