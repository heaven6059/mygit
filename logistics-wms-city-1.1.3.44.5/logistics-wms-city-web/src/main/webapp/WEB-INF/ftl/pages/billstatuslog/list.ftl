<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>状态日志管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billstatuslog/billstatuslog.js?version=1.0.5.1"></script>	
</head>
<body  class="easyui-layout">
		<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
			<@p.toolbar id="toolBarOne"  listData=[
				{"title":"查询","iconCls":"icon-search","action":"billstatuslog.search();","type":0},
			    {"title":"清空","iconCls":"icon-remove","action":"billstatuslog.searchClear();","type":0},
				{"title":"关闭","iconCls":"icon-close","action":"closeWindow('状态日志管理');","type":0}
		    ]/>	
		</div>
<#-- 数据列表div -->
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true">
 		<div data-options="region:'north',border:false" >
			<div id="searchDiv" style="padding:10px;">
				<form name="searchForm" id="searchForm" method="post" class="city-form">
					<table>
						<tr>
							<td class="common-td">单据编号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="billNo" id="billNoCon" /></td>
							<td class="common-td blank">源单号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="sourceNo" id="sourceNoCon" /></td>
							<td class="common-td blank">操作时间：</td>
							<td><input class="easyui-datebox ipt" style="width:120px" name="operatetmBegin" id="operatetmBeginCon" /></td>
							<td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
							<td><input class="easyui-datebox ipt" style="width:120px" name="operatetmEnd" id="operatetmEndCon" /></td>
						</tr>
						<tr>
							<td class="common-td blank">单据类型：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="billType" id="billTypeCon" data-options="required:true,editable:false"/></td>
						</tr>
					</table>
				</form>
			</div>
			</div>	
			<div data-options="region:'center',border:false">		
				<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="状态日志列表"
					isHasToolBar="false" divToolbar=""  height="469"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
					rownumbers="true" emptyMsg=""
					columnsJsonList="[
						{field : 'locno',title : '仓库编码',width : 60,align:'left'},
						{field : 'billNo',title : '单据编号',width : 180},
						{field : 'billType',title : '单据类型',width : 60,formatter:billstatuslog.billTypeFormatter,align:'left'},
						{field : 'statusName',title : '状态',width : 80,align:'left'},
						{field : 'description',title : '操作内容',width : 300,align:'left'},
						{field : 'operator',title : '操作人',width : 100,align:'left'},
						{field : 'operatorName',title : '操作人名称',width : 80,align:'left'},
						{field : 'operatetm',title : '操作时间',width : 150},
						{field : 'sourceNo',title : '源单号',width : 180}
						]" 
						jsonExtend='{onDblClickRow:function(rowIndex, rowData){
					    	// 触发点击方法  调JS方法
					        //billStoreRule.edit(rowData.ruleNo,1);
				}}'/>
			</div>
		</div>
</div>
</body>
</html>