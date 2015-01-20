<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>容器资料维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/bmContainer/bmContainer.js?version=1.0.6.0"></script>
    <!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"bmContainer.searchContainer();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"bmContainer.searchClear();", "type":0},
	        {"title":"新增","iconCls":"icon-add","action":"bmContainer.addUI();", "type":1},
	        {"title":"修改","iconCls":"icon-edit","action":"bmContainer.editUI();","type":2},
	        {"title":"删除","iconCls":"icon-del","action":"bmContainer.deleteRows();","type":3},
	        {"title":"标签打印","iconCls":"icon-print","action":"bmContainer.printByBox();", "type":0},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" style="padding:10px;width:950px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
				        	<tr>
				        		<td class="common-td">容器编码：</td>
				                <td><input class="easyui-validatebox ipt"  style="width:120px" name="conNo" id="conNo" /></td>
				            	<td class="common-td blank">容器类型：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="type" id="containerTypeCondition" /></td>
				                <td class="common-td blank">容器状态：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="status" id="manageStatusCondition" /></td>
				                <td class="common-td blank">来源：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="transFlag" id="manageTransFlagCondition" /></td>
				                <td class="common-td blank">库存状态：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="conStatus" id="manageConStatusCondition" /></td>
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
        	<input type="hidden" id="userType" />
        	<#--查询end-->
        	<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    	<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="容器资料维护"
		    	isHasToolBar="false" divToolbar="" height="390"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
				rownumbers="true" emptyMsg=""
				columnsJsonList="[
			    	{field : 'id',checkbox:true},
			    	{field : 'conNo',title : '容器编码',width : 150,align:'left'},
			        {field : 'ownerNo',title : '货主',width : 80,formatter:bmContainer.ownerFormatter,align:'left'},
			        {field : 'type',title : '容器类型',width : 80,formatter:bmContainer.containerTypeFormatter,align:'left'},
			        {field : 'status',title : '容器状态',width : 80,formatter:bmContainer.manageStatusFormatter,align:'left'},
			        {field : 'transFlag',title : '来源',width : 80,formatter:bmContainer.manageConTransFlagFormatter,align:'left'},
			        {field : 'weight',title : '重量',width : 50,align:'right'},
			        {field : 'volume',title : '体积',width : 50,align:'right'},
			        {field : 'length',title : '长',width : 50,align:'right'},
			        {field : 'wide',title : '宽',width : 50,align:'right'},
			        {field : 'height',title : '高',width : 50,align:'right'},
			        {field : 'mixStyle',title : '是否混款',width : 90,formatter:bmContainer.mixStyleFormatter,align:'left'},
			        {field : 'conStatus',title : '库存状态',width : 80,formatter:bmContainer.manageConStatusFormatter,align:'left'},
				    {field : 'creator',title : '创建人',width : 80,align:'left'},
					{field : 'createtm',title : '创建时间',width : 125,sortable:true},
				    {field : 'editor',title : '修改人',width : 80,align:'left'},
				    {field : 'edittm',title : '修改时间',width : 125,sortable:true}
			    ]" 
				jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		        	// 触发点击方法  调JS方法
		       		bmContainer.loadDetail(rowData);
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
						<input type="hidden" id="conNo" name="conNo"/>
						<!--<input type="hidden" id="ownerNo" name="ownerNo"/>-->
						<table width='580'>
					    	<tr>
					        	<td width="100px" class="common-td">容器类型：</td>
					            <td><input class="easyui-combobox" style="width:150px" name="type" id="containerType" required="true" missingMessage='容器类型为必填项!' /></td>
								<td width="100px" class="common-td blank">是否允许混款：</td>
					            <td width="150px"><input class="easyui-combobox" style="width:150px" name="mixStyle" id="mixStyle"  required="true" missingMessage='是否混款为必填项!'/></td>
							</tr>
							<tr> 
					         	<td class="common-td blank">货主：</td>
		   						<td><input class="easyui-combobox" data-options="editable:false" style="width:150px" name="ownerNo" id="search_ownerNo" required="true" missingMessage='货主为必填项!'/></td>
					            <td width="100px" class="common-td" style="display:none" id="stIsshow">状态：</td>
					            <td width="150px" style="display:none" id="svIsshow"><input class="easyui-combobox" style="width:150px" disabled="disabled" name="status" id="manageStatus"  required="true" missingMessage='容器状态为必填项!'/></td>
							</tr>
							<tr id="trIsShow">
					        	<td width="100px" class="common-td">占用单据编号：</td>
					            <td width="150px"><input disabled="disabled" name="optBillNo" id="optBillNo" style="width:150px" ></td>
					            <td width="100px" class="common-td blank">占用单据类型：</td>
					            <td width="150px"><input disabled="disabled" class="easyui-combobox" style="width:120px" name="optBillType" id="userTypeCondition" /></td>
							</tr>
					        <tr>
					        	<td width="100px" class="common-td blank">重量：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px" name="weight" id="weight" ></td>
					        	<td width="100px" class="common-td">体积：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="volume"  id="volume" /></td>
					            
							</tr>
					        <tr>
					        	<td width="100px" class="common-td">长：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="length"  /></td>
					            <td width="100px" class="common-td blank">宽：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="wide"   /></td>
							</tr>
					        <tr>
					        	<td width="100px" class="common-td">高：</td>
					            <td width="150px"><input class="easyui-numberbox ipt" style="width:150px;" name="height"  /></td>
					         </tr>
					        <tr>
					        	<td width="100px" class="common-td">容器描述：</td>
					            <td colspan="3"><input class="easyui-validatebox ipt" style="width:450px" name="remarks" id="remarks" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
					        </tr>
					         <tr>
						         <td colspan="4">
						         	<div align="center">
						         		<a id="info_save" href="javascript:bmContainer.manage();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
										<a id="info_close" href="javascript:bmContainer.closeUI();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
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