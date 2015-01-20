<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>颜色管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/colorinfo.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
			<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
			<#--工具条-->
			<@p.toolbar id="toolBarOne" listData=[
					{"title":"查询","iconCls":"icon-search","action":"colorinfo.searchColor();","type":0},
		       		{"title":"清空","iconCls":"icon-remove","action":"colorinfo.searchClear();","type":0}
		       		{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('颜色管理')","type":0}
				]
			/>
			</div>
			
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true">
 		<div data-options="region:'north',border:false" >
	 		 	<div class="search-div" id="search-div">
	       		 <form name="searchForm" id="searchForm" method="post" class="city-form">
	       		 	<table>
	       		 		<tr>
	       		 			<td  class="common-td">颜色编码：</td>
	       		 			<td><input class="easyui-validatebox ipt" style="width:120px" name="colorNo" id="colorNoCondition" /></td>
	       		 			<td class="common-td blank">颜色操作码：</td>
	       		 			<td><input class="easyui-validatebox ipt" style="width:120px" name="colorCode" id="colorCodeCondition" /></td>
	       		 			<td class="common-td blank">颜色名称：</td>
	       		 			<td><input class="easyui-validatebox ipt" style="width:120px" name="colorName" id="colorNameCondition" /></td>
	       		 			<td class="common-td blank">品牌库：</td>
	       		 			<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoCondition" /></td>
	       		 		</tr>
	       		 	</table>
				</form>
			 </div>
 	 </div>
 	 <div data-options="region:'center',border:false">
	 	<@p.datagrid id="dataGridCI" name="name" title="颜色列表" loadUrl="/color_info/list.json" saveUrl=""   defaultColumn="" 
	 			isHasToolBar="false"  divToolbar=""  height="375" width="" onClickRowEdit="false" singleSelect="true" pageSize='20'  
				pagination="true" rownumbers="true" emptyMsg=""
	 			columnsJsonList="[
	 				{field:'colorNo',title:'颜色编码',width:70,align:'left'},
	 			   	{field:'colorCode',title:'颜色操作码',width:80,align:'left'},
	 				{field:'colorName',title:'颜色名称',width:150,align:'left'},
	 			   	{field:'sysNoStr',title:'所属品牌库',width:150,align:'left'},
	 			  	{field:'creator',title:'建档人',width:100,align:'left'},
	              	{field:'createtm',title : '建档时间',width : 125,sortable:true},
	              	{field:'editor',title:'修改人',width:100,align:'left'},
	              	{field:'edittm',title : '修改时间',width : 125,sortable:true},
	              	{field:'remarks',title:'备注',width:200,align:'left'
	 			  }
	 			]"
	 			jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  
		                }}'/>
	 </div>
	</div>
</div> 				
</body>
</html>