<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>尺寸管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/sizeinfo.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
				<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
				<#--工具条-->
				<@p.toolbar id="toolBarOne" listData=[
						{"title":"查询","iconCls":"icon-search","action":"sizeinfo.searchSizeinfo();","type":0},
			       		{"title":"清空","iconCls":"icon-remove","action":"sizeinfo.searchSizeinfoClear();","type":0}
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
							<td class="common-td">所属品牌库：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="brandCondition" /></td>
							<td class="common-td blank">尺寸编码：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="sizeNo" id="sizeNoCondition" /></td>
							<td class="common-td blank">尺寸操作码：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="sizeCode" id="sizeCodeCondition" /></td>
							<td class="common-td blank">尺寸名称：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="sizeName" id="sizeNameCondition" /></td>
						</tr>
						<tr>
							<td class="common-td">尺寸类别：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="sizeKind" id="sizeKindCondition" /></td>
							<td class="common-td blank">建档人：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition"/></td>
							<td class="common-td blank">建档时间：</td>
							<td><input class="easyui-datebox ipt" type="text" style="width:120px" name="createtmBegin" id="createtmBeginCondition"/></td>
							<td class="common-line">&mdash;</td>
							<td><input class="easyui-datebox ipt" type="text" style="width:120px" name="createtmEnd" id="createtmEndCondition"/></td>
						</tr>
						<tr>
							<td class="common-td">尺寸类别2：</td>
							<td><input class="easyui-numberbox ipt" style="width:120px;height:22px" name="sizeKind2" id="sizeKind2Condition" data-options="min:0,precision:0"/></td>
							<td class="common-td blank">修改人：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="editor" id="editorCondition"/></td>
							<td class="common-td blank">修改时间：</td>
							<td><input class="easyui-datebox ipt" type="text" style="width:120px" name="edittmBegin" id="edittmBeginCondition"/></td>
							<td class="common-line">&mdash;</td>
							<td><input class="easyui-datebox ipt" type="text" style="width:120px" name="edittmEnd" id="edittmEndCondition"/></td>
						</tr>
						<tr>
							<td class="common-td">横排列号：</td>
							<td><input class="easyui-numberbox ipt" style="width:120px;height:22px" name="hcolNo" id="hcolNoCondition" data-options="min:0,precision:0"/></td>
							<td class="common-td blank">原厂尺寸：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="workSize" id="workSizeCondition"/></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</table>
				</form>
         		</div>    
 			</div>
 	<div data-options="region:'center',border:false" >
 	<@p.datagrid id="dataGridSI" name="name" title="尺寸列表" loadUrl="" saveUrl=""   defaultColumn="" 
 			isHasToolBar="false"  divToolbar=""  height="500" width="" onClickRowEdit="false" singleSelect="true" pageSize='20'  
			pagination="true" rownumbers="true" emptyMsg="" pageNumber=0
 			columnsJsonList="[
 				{field:'sizeNo',title:'尺寸编码',width:70,align:'left'},
 				{field:'sizeCode',title:'尺寸操作码',width:80,align:'left'},
 				{field:'sizeName',title:'尺寸名称',width:150,align:'left'},
 				{field:'sizeKind',title:'尺寸类别',width:70,align:'left'},
 				{field:'sizeKind2',title:'尺寸类别2',width:70,align:'left'},
 				{field:'hcolNo',title:'横排列号',width:70,align:'left'},
 				{field:'workSize',title:'原厂尺寸',width:70,align:'left'},
 			    {field:'sysNo',title:'所属品牌库',width:150,align:'left'},
 		       	{field:'remarks',title:'备注',width:200,align:'left'},
 			  	{field:'creator',title:'建档人',width:100,align:'left'},
              	{field:'createtm',title : '建档时间',width : 125,sortable:true},
              	{field:'editor',title:'修改人',width:100,align:'left'},
              	{field:'edittm',title : '修改时间',width : 125,sortable:true}
 			]"
 		/>
 </div>
 </div>
 </div>
</body>
</html>