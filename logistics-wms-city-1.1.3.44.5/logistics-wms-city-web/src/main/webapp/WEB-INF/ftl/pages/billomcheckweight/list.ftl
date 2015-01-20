<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>称重</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billomcheckweight/billomcheckweight.js?version=1.0.5.1"></script>
	<script type="text/javascript" src="${domainStatic}/resources/common/other-lib/common.js?version=1.0.5.0"></script>
</head>

<body class="easyui-layout">
	<!--分页标签-->
    <div data-options="region:'center',border:false">
		<div id="tt" class="easyui-tabs" data-options="tabPosition:'top',fit:true">
	<!--分页标签-->
			<#-- 称重保存 BEGIN -->
			<div id="check" title="称重保存" style="padding:0px" >
				<div class="easyui-layout" data-options="fit:true" >
					<#-- 工具菜单start -->
					<div data-options="region:'north',border:false" class="toolbar-region">
				    	<@p.toolbar id="toolBarOne" listData=[
											{"title":"保存","iconCls":"icon-save","action":"billomcheckweight.save();","type":1}
										]
									/>
		        	</div>
		            <#-- 工具菜单end -->
		            <#--查询start-->
		            <div data-options="region:'center',border:false">
		     			<div class="easyui-layout" data-options="fit:true" id="subLayout">
			            	<!--搜索start-->
							<div  data-options="region:'north',border:false" >
								<form name="dataForm" id="dataForm" method="post" class="city-form" >
											<input type="hidden" name="labelNoAdd" id="labelNoAdd">
											<input type="hidden" name="locnoAdd" id="locnoAdd">
											<input type="hidden" name="containerNoAdd" id="containerNoAdd">
											<input type="hidden" name="containerTypeAdd" id="containerTypeAdd">
											<table>
											<tr>
											<td class="common-td">扫描箱号：</td>
											<td><input class="easyui-validatebox ipt" style="width:120px" name="labelAdd" id="labelAdd" data-options="required:true"/></td>
											<td class="common-td blank">预计重量：</td>
											<td><input class="easyui-validatebox ipt" style="width:120px" name="weight" id="weight" />千克</td>
											<td class="common-td blank">实际重量：</td>
											<td><input class="easyui-validatebox ipt" style="width:120px" name="realWeight" id="realWeight" data-options="required:true"/>千克</td>
											</tr>
											</table>
											</form>
							</div>
		                   	<!--显示列表-->
							<div data-options="region:'center',border:false">
								<@p.datagrid id="dataGridJGAdd"  loadUrl=""  saveUrl=""  defaultColumn=""  title="箱号称重"
									               isHasToolBar="false"  height="470"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
										           rownumbers="true" emptyMsg=""
										           columnsJsonList="[
										           		{field : 'labelNo',title : '箱号',width : 100,align:'left'},
										           		{field : 'containerNo',title : '容器号',width : 100,align:'left'},
										           		{field : 'containerType',title : '容器类型',width : 100,align:'left'},
										                {field : 'itemNo',title : '商品编码',width : 120,align:'left'},
										                {field : 'itemName',title : '商品名称',width : 180,align:'left'},
										                {field : 'sizeNo',title : '尺码',width : 90,align:'left'},
										                {field : 'itemWeight',title : '商品重量',width : 90,align:'right'}
										            ]" 
											/>
							</div>
		       			</div>     
		       		</div>
		  		</div>
		     	<#--查询end-->        
	   		</div> 
			<#-- 称重保存 END -->
			<#-- 称重查询 BEGIN -->
			<div id="checked" title="称重查询" style="padding:0px" >
				<div class="easyui-layout" data-options="fit:true" >
					<#-- 工具菜单start -->
					<div data-options="region:'north',border:false" class="toolbar-region">
						<@p.toolbar id="toolBarTow" listData=[
								{"title":"查询","iconCls":"icon-search","action":"billomcheckweight.searchChecked()", "type":0},
								{"title":"清除","iconCls":"icon-remove","action":"billomcheckweight.searchClearchecked()", "type":0}
										]
									/>
		           	</div>
		           	<#-- 工具菜单end -->
		           	<#--查询start-->
		            <div data-options="region:'center',border:false">
		            	<div class="easyui-layout" data-options="fit:true" id="subLayout">
			           		<!--搜索start-->
							<div  data-options="region:'north',border:false" >
								<form name="checkedForm" id="checkedForm" method="post" class="city-form" >
											<input type="hidden" name="locNo" id="locNo">
											<input type="hidden" name="containerNo" id="containerNo">
											<input type="hidden" name="containerType" id="containerType">
											<table>
											<tr>
											<td class="common-td">扫描箱号：</td>
											<td><input class="easyui-validatebox ipt" style="width:120px" name="labelNo" id="labelNo" data-options="required:true"/></td>
											</tr>
											</table>
											</form>
							</div>
		                	<!--显示列表-->
							<div data-options="region:'center',border:false">
							     <@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="出货称重"
		               						isHasToolBar="false"  height="500"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           						rownumbers="true" emptyMsg=""
			           						columnsJsonList="[
			           					{field : 'labelNo',title : '箱号',width : 100,align:'left'},
			           					{field : 'containerNo',title : '容器号',width : 80,align:'left'},
			           					{field : 'containerType',title : '容器类型',width : 80,align:'left'},
			           					{field : 'itemNo',title : '商品编码',width : 120,align:'left'},
			                			{field : 'itemName',title : '商品名称',width : 180,align:'left'},
			                			{field : 'styleNo',title : '款号',width : 80,align:'left'},
			                			{field : 'colorName',title : '颜色',width : 80,align:'left'},
			                			{field : 'sizeNo',title : '尺码',width : 80,align:'left'},
			                			{field : 'realWeight',title : '实际重量',width : 80,align:'right'}
			           					 ]" 
										/>
							</div>
						</div>
		         	</div>
		         	<#--查询end--> 
		 		</div>        
			</div>
			<#-- 称重查询 END -->
		</div>
	</div>
</body>
</html>