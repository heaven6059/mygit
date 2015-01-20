<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>盘点查询报表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billchreport/billchreport.js?version=1.1.1.4"></script>
</head>
<body class="easyui-layout">

			
	<#-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
			<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"billchreport.searchData()", "type":0},
	            {"title":"清除","iconCls":"icon-remove","action":"billchreport.clearForm('searchForm')", "type":0},
	            {"id":"btn-export","title":"导出","iconCls":"icon-export","action":"billchreport.do_export()","type":5}
				{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('盘点查询报表')","type":0}
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
                     			<td class="common-td blank">计划单号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo" id="planNoCondition" /></td>
                     			<td class="common-td blank">盘点单号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo" id="checkNoCondition" /></td>
                     			<td class="common-td blank">品牌库：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoCondition" /></td>
                     			<td class="common-td blank">品牌：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="brandNo" id="brandNoCondition" data-options='multiple:true'/></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank">盘点日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="planDateStart" id="startPlanDateCondition" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="planDateEnd" id="endPlanDateCondition" /></td>
                     			<td class="common-td blank">商品编码：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>	
                     			<td class="common-td">条码：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcodeCondition"/></td>
                     			
                     		</tr>
                     		<tr>
                     		   <td class="common-td blank">年份：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="years" id="yearsCondition" data-options='multiple:true'/></td>
                     			<td class="common-td blank">季节：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="season" id="seasonCondition" data-options='multiple:true'/></td>
                     			<td class="common-td blank">性别：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="gender" id="genderCondition" data-options='multiple:true'/></td>
                     			<td class="common-td blank">大类一：</td>
							    <td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" data-options='multiple:true'/></td>
                     		</tr>
                     		<tr>
							    <td class="common-td blank">大类二：</td>
							    <td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" data-options='multiple:true'/></td>
							    <td class="common-td blank">大类三：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" data-options='multiple:true'/></td>
								<td class="common-td">仓区：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNoCondition"/></td>
                     			<td class="common-td">库区：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="areaNo" id="areaNoCondition"/></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank">通道：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="stockNo" id="stockNoCondition" /></td>
                     			<td class="common-td blank">储位：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="cellNo" id="cellNoCondition" /></td>
                     		</tr>
                     		
                     	</table>
				 	</form>	
				 </div>
			</div>
			<!--显示列表-->
            <div data-options="region:'center',border:false">
            	<@p.datagrid id="mainDataGrid"  saveUrl=""  loadUrl="" defaultColumn=""  title="盘点查询报表"
					isHasToolBar="false"  divToolbar="#locSearchDiv" height="450"  onClickRowEdit="false" singleSelect="false" pageSize="20"
					rownumbers="true" emptyMsg="" showFooter="true"
					columnsJsonList="[
							{field : 'planNo',title : '盘点计划单',width : 140},
							{field : 'checkNo',title : '盘点单号',width : 140},
							{field : 'brandName',title : '品牌',width : 80,align:'left'},
							{field : 'wareNo',title : '仓区',width : 60,align:'left'},
							{field : 'areaNo',title : '库区',width : 60,align:'left'},
							{field : 'stockNo',title : '通道',width : 60,align:'left'},
							{field : 'cellNo',title : '储位',width : 100,align:'left'},
							{field : 'cateName',title : '商品大类',width : 80,align:'left'},
							{field : 'yearsName',title : '年份',width : 40,align:'left'},
							{field : 'seasonName',title : '季节',width : 40,align:'left'},
							{field : 'genderName',title : '性别',width : 40,align:'left'},
							{field : 'itemNo',title : '商品编码',width : 140,align:'left'},
							{field : 'sizeNo',title : '尺码',width : 60,align:'left'},
							{field : 'barcode',title : '条码',width : 140,align:'left'},
							{field : 'itemQty',title : '计划数量',width : 60,align:'right'},
							{field : 'realQty',title : '实盘数量',width : 60,align:'right'},
							{field : 'diffQty',title : '差异数量',width : 60,align:'right'}
						]"/>
            </div>
		</div>
	</div>
	
	
</body>
</html>