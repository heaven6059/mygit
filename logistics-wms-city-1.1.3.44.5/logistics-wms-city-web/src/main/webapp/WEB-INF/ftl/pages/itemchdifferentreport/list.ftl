<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>商品盘点差异报表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/itemchdifferentreport.js?version=1.1.1.1"></script>
</head>
<body  class="easyui-layout">
	<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
		<@p.toolbar id="toolBarOne" listData=[
				{"title":"查询","iconCls":"icon-search","action":"itemchdifferentreport.searchData();","type":0},
	       		{"title":"清空","iconCls":"icon-remove","action":"itemchdifferentreport.searchClear();","type":0},
				{"id":"btn-export","title":"导出","iconCls":"icon-export","action":"itemchdifferentreport.do_export()","type":5}
			]
		/>
	</div>
	
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<!--搜索start-->
			<div  data-options="region:'north',border:false" >
				 <div class="search-div">
				 	<form name="searchForm" id="searchForm" method="post" class="city-form">
				 		<table>
                     		<tr>
                     			<td class="common-td">计划单号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo" id="planNoCondition" /></td>
                     			<td class="common-td blank">商品编号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>
                     			<td class="common-td blank">条码：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcodeCondition" /></td>
                     		</tr>
                     	</table>
				 	</form>	
				 </div>
			</div>
			<!--显示列表-->
            <div data-options="region:'center',border:false">
            	<@p.datagrid id="mainDataGrid"  saveUrl=""  loadUrl="" defaultColumn=""  title="商品盘点差异列表"
					isHasToolBar="false"  divToolbar="#locSearchDiv" height="450"  onClickRowEdit="false" singleSelect="false" pageSize="20"
					rownumbers="true" emptyMsg="" showFooter="true"
					columnsJsonList="[
						{field : 'planNo',title : '计划单号',width : 150},	
						{field : 'itemNo',title : '商品编号',width : 150,align:'left'},
						{field : 'itemName',title : '商品名称',width : 150,align:'left'},
						{field : 'sizeNo',title : '尺码',width : 100,align:'left'},
						{field : 'barcode',title : '条码',width : 150,align:'left'},
						{field : 'planDate',title : '计划盘点日期',width : 100},
						{field : 'itemQty',title : '计划数量',width : 60,align:'right'},
						{field : 'realQty',title : '实盘数量',width : 60,align:'right'},
						{field : 'diffQty',title : '差异数量',width : 60,align:'right'}
						
						]"/>
            </div>
		</div>
	</div>
	
	
</body>
</html>