<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>箱明细库存查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/conboxdtl/conboxdtl.js?version=1.0.8.0"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"conBoxDtl.searchContainer();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"conBoxDtl.searchClear();", "type":0},
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
				        		<td class="common-td blank">箱号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="boxNo" id="boxNo" /></td>
				                <td class="common-td blank">储位：</td>
				                <td><input class="easyui-validatebox ipt"  style="width:120px" name="cellNo" id="cellNo" /></td>
				        		<td class="common-td blank">商品品质：</td>
						        <td  id="qualityValue"><input class="easyui-combobox" style="width:120px" name="quality" id="quality" data-options="editable:false"/></td>
						        <td class="common-td blank">商品类型：</td>
						        <td  id="qualityValue"><input class="easyui-combobox" style="width:120px" name="itemType" id="itemType" data-options="editable:false"/></td>
				        	</tr>
				        	<tr>
			   					<td class="common-td blank">商品编码：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo" /></td>
				                <td class="common-td blank">商品条码：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcode" /></td>
				                <td class="common-td blank">父容器号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="panNo" id="panNo" /></td>
				                <td class="common-td blank">商品名称：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemName" /></td>
				        	</tr>
				        	<tr>
				        		<td class="common-td blank"> 品牌库：</td>
				     			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
				     			<td class="common-td blank">品牌：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoCondition"/></td>
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
        	<#--查询end-->
        	<#--显示列表start-->
        	<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="商品列表"
			       isHasToolBar="false"  onClickRowEdit="false"    pagination="true"
			       rownumbers="true" emptyMsg=""  showFooter="true"
			       columnsJsonList="[
			    	{field : 'boxNo',title : '箱号',width : 120,align:'left'},
			    	{field : 'cellNo',title : '储位',width : 80,align:'left'},
			    	{field : 'itemNo',title:'商品编码',width:120,align:'left'},
			    	{field : 'itemName',title:'商品名称',width:120,align:'left'},
					{field : 'barcode',title:'商品条码',width:120,align:'left'},
			    	{field : 'brandNo',title:'品牌',width:120,align:'left'},
			        {field : 'quality',title : '品质',formatter:conBoxDtl.qualityFormatter,width : 80,align:'left'},
			        {field : 'itemType',title : '商品类型',formatter:conBoxDtl.itemTypeFormatter,width : 80,align:'left'},
			    	{field : 'qty',title : '数量',width : 80,align:'left'},
			    	{field : 'panNo',title : '父容器编号',width : 120,align:'left'}
			    ]" 
				jsonExtend='{onDblClickRow:function(rowIndex, rowData){}}'/>
			</div>
        	<#--显示列表end-->
		</div>
	</div>
<#-- 主表end -->
</body>
</html>