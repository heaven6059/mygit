<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>打印机组与打印机维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include  "/WEB-INF/ftl/common/header.ftl" >
<link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/bsprinterdock.css"/>" />
<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/bsprintergroup.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单div -->
   <div data-options="region:'north',border:false" class="toolbar-region">
	     <@p.toolbar id="toolBarOne" listData=[
						{"title":"查询","iconCls":"icon-search","action":"bsprintergroup.searchDock();","type":0},
			       		{"title":"清空","iconCls":"icon-remove","action":"bsprintergroup.clearSearchCondition();","type":0}
					]
				/>
	   </div>
	<div data-options="border:false" id="storeSearchDiv" style="padding:10px;">
		<form name="searchForm" id="searchForm" method="post" class="city-form" >
							<table>
								<tr>
									<td class="common-td">打印机组编码：</td>
									<td><input id="usercode" class="easyui-validatebox ipt" name="printerGroupNo" /> </td>
									<td class="common-td blank">打印机组名称：</td>
									<td><input id="username" class="easyui-validatebox ipt" name="printerGroupName"/></td>
								</tr>
							</table>
						</form>
	</div>
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<div data-options="region:'west',split:false" style="width:500px;">
				<@p.datagrid id="dataGridJG_printer_group"  loadUrl="" saveUrl=""   defaultColumn=""   title="选择打印机组"
		              isHasToolBar="false" divToolbar="#searchForm"  height="500"  onClickRowEdit="false"   pagination="true"
			           rownumbers="true" 
			           columnsJsonList="[
								{field : 'printerGroupNo',title : '打印机组编码',width : 200,align:'left'},
								{field : 'printerGroupName',title : '打印机组名称',width : 300,align:'left'}
			                 ]"
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                         bsprintergroup.loadbsprintergroup(rowData);
		                   }}'/>
			</div>
			<div data-options="region:'center',split:false" style="margin-left:5px;">
				<div id="saveLocnoDiv">
					<@p.toolbar id="toolBarTow"  listData=[
					 {"id":"btn-close","title":"保存","iconCls":"icon-add","action":"bsprintergroup.save()","type":1}
					 ]
				  />
				</div>
				<@p.datagrid id="dataGridJG_printer"  loadUrl="" saveUrl=""   defaultColumn=""  
		              isHasToolBar="false" divToolbar=""  height="463"  onClickRowEdit="false"   pagination="false"
			           rownumbers="true"  singleSelect = "false" 
			           columnsJsonList="[
			           			{field : ' ',checkbox:true},
								{field : 'printerNo',title : '打印机编码',width :100,align:'left'},
								{field : 'printerName',title : '打印机名称',width :240,align:'left'}
							]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                          // defdock.detail(rowData);
		                   }}'/>
			</div>
		</div>
	</div>
</body>
</html>