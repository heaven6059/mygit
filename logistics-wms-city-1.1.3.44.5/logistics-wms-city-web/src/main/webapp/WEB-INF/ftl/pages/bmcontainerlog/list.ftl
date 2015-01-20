<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>容器操作日志</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/bmContainer/bmContainerLog.js?version=1.0.6.0"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"bmContainerLog.searchContainer();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"bmContainerLog.searchClear();", "type":0},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" style="padding:10px;width:1000px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
				        	<tr>
				        		<td class="common-td">操作单号：</td>
				                <td><input class="easyui-validatebox ipt"  style="width:100px" name="billNo" id="billNo" /></td>
				        		<td class="common-td blank">容器编码：</td>
				                <td><input class="easyui-validatebox ipt"  style="width:100px" name="conNo" id="conNo" /></td>
				                <td class="common-td blank">子容器编码：</td>
				                <td><input class="easyui-validatebox ipt"  style="width:100px" name="subConNo" id="subConNo" /></td>
				            	<td class="common-td blank">容器类型：</td>
				                <td><input class="easyui-combobox" style="width:80px" name="conType" id="conType" /></td>
				            	<td class="common-td blank">作业类型：</td>
				                <td><input class="easyui-combobox" style="width:100px" name="userType" id="userType" /></td>
				                <td class="common-td blank">作业标示：</td>
				                <td><input class="easyui-combobox" style="width:80px" name="ioFlag" id="ioFlag" /></td>
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
        	<#--查询end-->
        	<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    	<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="容器操作日志"
		    	isHasToolBar="false" divToolbar="" height="390"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
				rownumbers="true" emptyMsg=""
				columnsJsonList="[
			    	{field : 'billNo',title : '操作单号',width : 150,align:'left'},
			    	{field : 'userType',title : '作业类型',formatter:bmContainerLog.userTypeFormatter,width : 80,align:'left'},
			    	{field : 'conNo',title : '容器编码',width : 150,align:'left'},
			    	{field : 'conType',title : '容器类型',formatter:bmContainerLog.containerTypeFormatter,width : 80,align:'left'},
			    	{field : 'subConNo',title : '子容器编码',width : 150,align:'left'},
			    	{field : 'ioFlag',title : '作业标示',formatter:bmContainerLog.ioFlagFormatter,width : 80,align:'left'},
			    	{field : 'cellNo',title : '储位',width : 100,align:'left'},
				    {field : 'creator',title : '操作人',width : 80,align:'left'},
				    {field : 'creatorName',title : '操作人名称',width : 80,align:'left'},
					{field : 'createtm',title : '时间',width : 125,sortable:true}
			    ]" 
				jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		        	// 触发点击方法  调JS方法
		       		bmContainerLog.loadDetail(rowData);
		   	}}'/>
			</div>
        	<#--显示列表end-->
		</div>
	</div>
	<#-- 主表end -->
	<#-- 明细信息div -->
<div id="detailDialogView" class="easyui-window" title="明细"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="padding:5px;">
	    	<form id="detailForm_view" name="detailForm" method="post" class="city-form">
	    		<table>
					<tr>
						<td class="common-td blank">源单号：</td>
						<td><input class="easyui-validatebox ipt"  name="billNo" style="width:120px;"/></td>
						<td class="common-td blank">容器号：</td>
						<td><input class="easyui-validatebox ipt"  name="conNo" style="width:120px;"/></td>
						<td class="common-td blank">子容器号：</td>
						<td><input class="easyui-validatebox ipt"  name="subConNo" style="width:120px;"/></td>
					</tr>
	    		</table>
		    </form>
		</div>
		<input type="hidden"   id="quality" /><input type="hidden"   id="itemType" />
		<div data-options="region:'center'">
			<@p.datagrid id="dataGridJG_view"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true"
					           rownumbers="true"  onClickRowEdit="false" singleSelect="true"  title="明细列表"  emptyMsg="" 
					           columnsJsonList="[
									{field:'itemNo',title:'商品编码',width:150,align:'left'},
									{field:'itemName',title:'商品名称',width:150,align:'left'},
									{field:'sizeNo',title:'尺码',width:50},
									{field:'brandName',title:'品牌',width:80,align:'left'},
				 					{field:'qty',title:'数量',width:50,align:'left',align:'left'},
							        {field : 'quality',title : '品质',formatter:bmContainerLog.qualityFormatter,width : 80,align:'left'},
							        {field : 'itemType',title : '商品类型',formatter:bmContainerLog.itemTypeFormatter,width : 80,align:'left'},
				 					{field : 'creator',title : '操作人',width : 80,align:'left'},
				 					{field : 'creatorName',title : '操作人名称',width : 80,align:'left'},
									{field : 'createtm',title : '时间',width : 125,sortable:true}
								]"/>
				</div>
		</div>
	</div>
</div>
	<#-- 明细信息div -->
</body>
</html>