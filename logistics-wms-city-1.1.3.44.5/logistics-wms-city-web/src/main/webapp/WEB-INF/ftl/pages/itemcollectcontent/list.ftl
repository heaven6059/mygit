<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>商品汇总库存</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/itemcollectcontent.js?version=1.0.5.2"></script>
   
</head>
<body  class="easyui-layout">
<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<@p.toolbar id="toolBarOne" listData=[
			{"title":"查询","iconCls":"icon-search","action":"itemcollectcontent.searchItemcollectcontent();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"itemcollectcontent.searchClear();","type":0},
			{"id":"btn-export","title":"导出","iconCls":"icon-export","action":"itemcollectcontent.do_export()","type":5}
		]
	/>
</div>
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true" id="subLayout">
 	  
 	  <div data-options="region:'north',border:false" >
		<div class="search-div" id="itemcollectcontentSearchDiv">
			<form id='exportForm' method="post" action="../item_collect_content/do_exportlist">
				<input type="hidden" name="sysNo" id="sysNoCondition_export"/>
				<input type="hidden" name="brandNo" id="brandNoCondition_export"/>
				
				<input type="hidden" name="itemType" id="itemTypeCondition_export"/>
				<input type="hidden" name="quality" id="qualityCondition_export"/>
				
				<input type="hidden" name="itemNo" id="itemNoCondition_export" />
				<input type="hidden" name="cateOne" id="cateOneCondition_export"/>
				<input type="hidden" name="cateTwo" id="cateTwoCondition_export"/>
				<input type="hidden" name="cateThree" id="cateThreeCondition_export"/>
				
				<input type="hidden" name="itemName" id="itemNameCondition_export" />
				<input type="hidden" name="barcode" id="barcodeCondition_export" />
				<input type="hidden" name="yearsName" id="yearsNameCondition_export" />
				<input type="hidden" name="seasonName" id="seasonNameCondition_export" />
				<input type="hidden" name="genderName" id="genderNameCondition_export" />
				
				<input type="hidden" name="locno" id="locnoCondition_export"/>
				<input type="hidden" name="exportColumns" id="exportColumnsCondition_export"/>
				<input type="hidden" name="fileName" value="商品汇总库存报表"/>
			</form>
			<form name="searchForm" id="searchForm" method="post" class="city-form">
				<table>
					<tr>
						<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
		     			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		     			<td class="common-td blank">品牌：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoCondition"/></td>
						<td class="common-td blank">商品属性：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="itemType" id="itemTypeCondition"/></td>
						<td class="common-td blank">商品品质：</td>
		     			<td><input class="easyui-combobox" style="width:120px" name="quality" id="qualityCondition"/></td>
					</tr>
					<tr>
						<td class="common-td blank">商品编码：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>
						<td class="common-td blank">大类一：</td>
					    <td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
					    <td class="common-td blank">大类二：</td>
					    <td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
					    <td class="common-td blank">大类三：</td>
						 <td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
					</tr>
					<tr>
					    <td class="common-td blank">商品名称：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemNameCondition" /></td>
						<td class="common-td blank">年份：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="yearsName" id="yearsNameCondition" /></td>
						<td class="common-td blank">季节：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="seasonName" id="seasonNameCondition" /></td>
						<td class="common-td blank">性别：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="genderName" id="genderNameCondition" /></td>
					</tr>
					<tr>
						<td class="common-td blank">商品条码：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcodeCondition" /></td>
						<td class="common-td blank"></td>
						<td></td>
					    <td class="common-td blank"></td>
						<td></td>
						<td class="common-td blank"></td>
						<td></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
			 
<#-- 数据列表div -->
	<div data-options="region:'center',border:false">
	    <@p.datagrid id="dataGriditemcollectcontent"  loadUrl="" saveUrl=""  defaultColumn=""   title="商品列表"
	       isHasToolBar="false" onClickRowEdit="false" pagination="true" rownumbers="true" emptyMsg="" showFooter="true"
	       columnsJsonList="[
	          	{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
	          	{field : 'itemName',title : '商品名称',width : 150,align:'left'},
	          	{field : 'colorName',title : '颜色',width : 50,align:'left'},
	          	{field : 'sizeNo',title : '尺码',width : 50,align:'left'},
	          	{field : 'brandName',title : '品牌',width : 80,align:'left'},
	          	{field : 'areaName',title : '库区',width : 120,align:'left'},
	          	{field : 'qty',title : '库存数量',width : 60,align:'right'},
	          	{field : 'itemTypeName',title : '商品属性',width : 70,align:'left'},
	          	{field : 'qualityName',title : '商品品质',width : 70,align:'left'},
	          	{field : 'cateName',title : '商品大类',width : 70,align:'left'},
	          	{field : 'yearsName',title:'年份',width:70,align:'left'},
		        {field : 'seasonName',title:'季节',width:70,align:'left'},
		        {field : 'genderName',title:'性别',width:70,align:'left'},
	          	{field : 'barcode',title : '商品条码',width : 170,align:'left'},
	          	{field : 'statusName',title : '盘点锁定标识',width : 100,align:'left'},
	          	{field : 'manualName',title : '手工移库标识',width : 100,align:'left'}
	          	
	         ]" 
	          
	       jsonExtend='{onSelect:function(rowIndex, rowData){
	                // 触发点击方法  调JS方法
	                //item.loadDetail(rowData.itemNo);
	       }}'/>
	 </div>
   </div>
 </div>
</body>
</html>