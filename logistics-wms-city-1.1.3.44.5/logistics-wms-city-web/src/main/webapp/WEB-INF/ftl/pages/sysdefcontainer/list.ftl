<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>容器资料维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/sysdefcontainer.js?version=1.0.6.0"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"sysdefcontainer.searchContainer();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"sysdefcontainer.searchClear();", "type":0},
	        {"title":"新增","iconCls":"icon-add","action":"sysdefcontainer.addUI();", "type":1},
	        {"title":"修改","iconCls":"icon-edit","action":"sysdefcontainer.editUI();","type":2},
	        {"title":"删除","iconCls":"icon-del","action":"sysdefcontainer.deleteRows();","type":3},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" style="padding:10px;width:760px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
				        	<tr>
				            	<td class="common-td">容器类型：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="containerType" id="containerTypeCondition" /></td>
				                <td class="common-td blank">用途：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="useType" id="useTypeCondition" /></td>
				                <td class="common-td blank">状态：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="manageStatus" id="manageStatusCondition" /></td>
				                <td class="common-td blank">容器编号前缀：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="containerPrefix" id="containerPrefixCondition" /></td>
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
        	<#--查询end-->
        	<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    	<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="容器资料维护"
		    	isHasToolBar="false" divToolbar="" height="390"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
				rownumbers="true" emptyMsg=""
				columnsJsonList="[
			    	{field : 'id',checkbox:true},
			        {field : 'containerType',title : '容器类型',width : 90,formatter:sysdefcontainer.containerTypeFormatter,align:'left'},
			        {field : 'useType',title : '用途',width : 90,formatter:sysdefcontainer.useTypeFormatter,align:'left'},
			        {field : 'manageStatus',title : '状态',width : 80,formatter:sysdefcontainer.manageStatusFormatter,align:'left'},
			        {field : 'containerPrefix',title : '容器编号前缀 ',width : 90,formatter:sysdefcontainer.containerPrefixFormatter,align:'left'},
			        {field : 'codeLength',title : '容器代码长度',width : 90,align:'right'},
			        {field : 'length',title : '长',width : 50,align:'right'},
			        {field : 'width',title : '宽',width : 50,align:'right'},
			        {field : 'height',title : '高',width : 50,align:'right'},
			        {field : 'containerCapacity',title : '最大承载重量',width : 90,align:'right'},
			        {field : 'capacityTolerance',title : '最大容积比率',width : 90,align:'right'},
				    {field : 'availableNoQty',title : '容器数量',width : 80,sortable:true,align:'right'},
				    {field : 'containerDesc',title : '容器描述',width : 120,align:'left'},
				    {field : 'creator',title : '建档人',width : 80,align:'left'},
					{field : 'createtm',title : '建档时间',width : 125,sortable:true},
				    {field : 'editor',title : '修改人',width : 80,align:'left'},
				    {field : 'edittm',title : '修改时间',width : 125,sortable:true}
			    ]" 
				jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		        	// 触发点击方法  调JS方法
		       		sysdefcontainer.loadDetail(rowData);
		   	}}'/>
			</div>
        	<#--显示列表end-->
		</div>
	</div>
	<#-- 主表end -->
	<#-- 明细信息div -->
	<div id="openUI" class="easyui-dialog" style="padding:10px;height:350px;width:620px;"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		minimizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
			</div>
			<div data-options="region:'center',border:false" id = 'detailDiv'>
				<form name="dataForm" id="dataForm" method="post" class="city-form">	
						<input type="hidden" id="opt"/>
						<table width='580'>
					    	<tr>
					        	<td width="100px" class="common-td">容器类型：</td>
					            <td><input class="easyui-combobox" style="width:150px" name="containerType" id="containerType" /></td>
								<td width="100px" class="common-td blank">用途：</td>
					            <td width="150px"><input class="easyui-combobox" style="width:150px" name="useType" id="useType" data-options="required:true"/></td>
							</tr>
					        <tr>
					        	<td width="100px" class="common-td blank">容器编号前缀：</td>
					            <td width="150px"><input class="easyui-combobox" style="width:150px" name="containerPrefix" id="containerPrefix" data-options="editable:false"/></td>
					        	<td width="100px" class="common-td">容器代码长度：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="codeLength" min="1"  id="supplierLname"/></td>
					            
							</tr>
					        <tr>
					        	<td width="100px" class="common-td">长：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="length" min="1"/></td>
					            <td width="100px" class="common-td blank">宽：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="width" min="1"/></td>
							</tr>
					        <tr>
					        	<td width="100px" class="common-td">高：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="height" min="1"/></td>
					            <td width="100px" class="common-td blank">最大承载重量：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="containerCapacity" min="1" /></td>
							</tr>
					        <tr>
					            <td width="100px" class="common-td">最大容积比率：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="capacityTolerance" min="1"/></td>
								<td width="100px" class="common-td blank">容器数量：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="availableNoQty"  min="1"/></td>
							</tr>
					         <tr>
					            <td width="100px" class="common-td">状态：</td>
					            <td width="150px"><input class="easyui-combobox" style="width:150px" name="manageStatus" id="manageStatus" /></td>
								<td width="100px" class="common-td blank"></td>
					            <td width="150px"></td>
							</tr>
					        <tr>
					        	<td width="100px" class="common-td">容器描述：</td>
					            <td colspan="3"><input class="easyui-validatebox ipt" style="width:450px" name="containerDesc" id="remarks"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
					        </tr>
					         <tr>
						         <td colspan="4">
						         	<div align="center">
						         		<a id="info_save" href="javascript:sysdefcontainer.manage();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
										<a id="info_close" href="javascript:sysdefcontainer.closeUI();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
									</div>
						         </td>
							</tr>
						</table>
					</form>
			</div>
		</div>
	</div>
	<#-- 明细信息div -->
</body>
</html>