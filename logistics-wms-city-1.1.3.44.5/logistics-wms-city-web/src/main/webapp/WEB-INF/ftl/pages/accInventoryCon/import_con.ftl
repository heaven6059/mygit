<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>商品储位库存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/accInventoryCon/accInventoryConSku.js?version=1.0.6.0"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"accInventoryConSku.searchContainer();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"accInventoryConSku.searchClear();", "type":0},
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
				                <td class="common-td blank">储位：</td>
				                <td><input class="easyui-validatebox ipt"  style="width:100px" name="cellNo" id="cellNo" /></td>
				                <td class="common-td blank">货主：</td>
		   						<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="ownerNo" id="ownerNo" /></td>
		   						<td class="common-td blank">商品编码：</td>
		   						<td><input class="easyui-validatebox ipt"  data-options="editable:false" style="width:120px" name="itemNo" id="itemNo" /></td>
		   						<td class="common-td blank">商品条码：</td>
		   						<td><input class="easyui-validatebox ipt"  data-options="editable:false" style="width:120px" name="barcode" id="barcode" /></td>
				        		<td class="common-td blank">商品品质：</td>
						        <td  id="qualityValue"><input class="easyui-combobox" style="width:120px" name="quality" id="quality" data-options="editable:false"/></td>
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
        	<#--查询end-->
        	<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    	<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="容器库存"
		    	isHasToolBar="false" divToolbar="" height="390"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
				rownumbers="true" emptyMsg=""
				columnsJsonList="[
			    	{field : 'ownerNo',title : '货主',formatter:accInventoryConSku.ownerNoFormatter,width : 80,align:'left'},
			    	{field : 'cellNo',title : '储位',width : 100,align:'left'},
			    	{field : 'itemNo',title:'商品编码',width:150,align:'left'},
			    	{field : 'itemName',title:'商品名称',width:150,align:'left'},
					{field : 'barcode',title:'商品条码',width:150,align:'left'},
			        {field : 'quality',title : '品质',formatter:accInventoryConSku.qualityFormatter,width : 80,align:'left'},
			        {field : 'itemType',title : '商品类型',formatter:accInventoryConSku.itemTypeFormatter,width : 80,align:'left'},
			    	{field : 'qty',title : 'SKU数量',width : 80,align:'left'},
			    	{field : 'instockQty',title : '预上数量',width : 80,align:'left'},
			    	{field : 'outstockQty',title : '预下数量',width : 80,align:'left'},
				    {field : 'creator',title : '创建人',width : 80,align:'left'},
					{field : 'createtm',title : '创建时间',width : 125,sortable:true},
					{field : 'editor',title : '修改人',width : 80,align:'left'},
					{field : 'edittm',title : '修改时间',width : 125,sortable:true}
			    ]" 
				jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		        	
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
						<td class="common-td blank">容器号：</td>
						<td>
							<input class="easyui-validatebox ipt"   name="conNo" style="width:120px;"/>
						</td>
					</tr>
	    		</table>
		    </form>
		</div>
	<input type="hidden"   id="itemType" />
		<div data-options="region:'center'">
			<@p.datagrid id="dataGridJG_view"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true"
					           rownumbers="true"  onClickRowEdit="false" singleSelect="true"  title="明细列表"  emptyMsg="" 
					           columnsJsonList="[
					           		{field:'conNo',title:'容器号',width:150,align:'left'},		
									{field:'itemNo',title:'商品编码',width:150,align:'left'},
									{field:'barcode',title:'条码',width:150,align:'left'},
							        {field : 'quality',title : '品质',formatter:accInventoryConSku.qualityFormatter,width : 80,align:'left'},
							        {field : 'itemType',title : '商品类型',formatter:accInventoryConSku.itemTypeFormatter,width : 80,align:'left'},
				 					{field:'qty',title:'数量',width:50,align:'left',align:'right'},
				 					{field : 'createtm',title : '创建时间',width : 125,sortable:true},
									{field : 'updatetm',title : '更新时间',width : 125,sortable:true}
								]"/>
				</div>
		</div>
	</div>
</div>
	<#-- 明细信息div -->
</body>
</html>