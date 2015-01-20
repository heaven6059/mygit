<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>码头与打印机组对应</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	 <#include  "/WEB-INF/ftl/common/header.ftl" >
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/bsprinterdock.css"/>" />
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/bsprinterdock.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单div -->
   <div data-options="region:'north',border:false" class="toolbar-region">
	     <@p.toolbar id="toolBarOne" listData=[
						{"title":"查询","iconCls":"icon-search","action":"bsprinterdock.searchDock();","type":0},
			       		{"title":"清空","iconCls":"icon-remove","action":"bsprinterdock.clearDockForm();","type":0}
					]
				/>
	   </div>
	<div data-options="border:false" id="storeSearchDiv" style="padding:10px;">
		<form name="searchForm" id="searchForm" method="post" class="city-form" >
							<table>
								<tr>
									<td class="common-td">码头编码:</td>
									<td><input id="usercode" class="easyui-validatebox ipt" name="dockNo" /> </td>
									<td class="common-td blank">码头名称：</td>
									<td><input id="username" class="easyui-validatebox ipt" name="dockName"/></td>
								</tr>
							</table>
						</form>
	</div>
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<div data-options="region:'west',split:false" style="width:430px;">
				<@p.datagrid id="dataGridJG_dock"  loadUrl="/bm_defdock/list.json?locno=${session_user.locNo}" saveUrl=""   defaultColumn=""   title="选择码头"
						              isHasToolBar="false" divToolbar="#searchForm"  height="500"  onClickRowEdit="false"   pagination="true"
							           rownumbers="true"  emptyMsg=""
							           columnsJsonList="[
												{field : 'dockNo',title : '码头编码',width : 170,align:'left'},
												{field : 'dockName',title : '码头名称',width : 230,align:'left'}
							                 ]"
								           jsonExtend='{onSelect:function(rowIndex, rowData){
						                            // 触发点击方法  调JS方法
						                         bsprinterdock.loadBsprinterDock(rowData);
						                   }}'/>
			</div>
			<div data-options="region:'center',split:false" style="margin-left:5px;">
				<div id="saveLocnoDiv">
					<@p.toolbar  id="toolBarTow" listData=[
	                         {"title":"保存","iconCls":"icon-add","action":"bsprinterdock.save()","type":1}
		                ]
					  />
				</div>
				<@p.datagrid id="dataGridJG_worker_loc"  loadUrl="/bm_defprinter_group/get_biz?locno=${session_user.locNo}" saveUrl=""   defaultColumn=""  
			              isHasToolBar="false" divToolbar=""  height="463"  onClickRowEdit="false"   pagination="false"
				           rownumbers="true"  singleSelect = "false" emptyMsg=""
				           columnsJsonList="[
				           			{field : ' ',checkbox:true},
									{field : 'printerGroupNo',title : '打印机组编码',width :100,align:'left'},
									{field : 'printerGroupName',title : '打印机组名称',width :240,align:'left'}
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