<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>仓区管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/cmdefware.js?version=1.1.1.1"></script>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"cmdefware.searchWare();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"cmdefware.searchClear();", "type":0},
	        {"title":"新增","iconCls":"icon-add","action":"cmdefware.addUI();", "type":1},
	        {"title":"修改","iconCls":"icon-edit","action":"cmdefware.editUI();","type":2},
	        {"title":"删除","iconCls":"icon-del","action":"cmdefware.deleteRows();","type":3},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
        	<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" class="search-div" style="padding:10px;">
        			<form name="searchForm" id="searchForm" method="post">
				   	<table>
					    <tr>
					    	<td class="common-td">仓区编码：</td>
					        <td ><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNoCondition" /></td>
					        <td class="common-td blank">仓区名称：</td>
					        <td ><input class="easyui-validatebox ipt" style="width:120px" name="wareName" id="locNameCondition" /></td>
					        <td class="common-td blank"></td>
					        <td ></td>
					        <td class="common-td blank"></td>
					        <td ></td>
					    </tr>             
					</table>
				</form>
        		</div>
        	</div>
      		<#--查询end-->
      		<#--显示列表start-->
        	<div data-options="region:'center',border:false">
        		<@p.datagrid id="dataGridJG"  loadUrl="/cm_defware/list.json?locno=${session_user.locNo}&sort=ware_no"  saveUrl=""  defaultColumn=""  title="仓区列表"
		               isHasToolBar="false" divToolbar="" height="375"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true"  emptyMsg=""
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'wareNo',title : '仓区编码',width : 100,align:'left'},
			                {field : 'wareName',title : '仓区名称',width : 180,align:'left'},
			                {field : 'creator',title : '创建人',width : 100,align:'left'},
			                {field : 'creatorName',title : '创建人名称',width : 100,align:'left'},
			                {field : 'createtm',title : '创建时间',width : 125,align:'left'},
			                {field : 'editor',title : '修改人',width : 100,align:'left'},
			                {field : 'editorName',title : '修改人名称',width : 100,align:'left'},
			                {field : 'edittm',title : '修改时间',width : 125,align:'left'},
			                {field : 'wareRemark',title : '备注',width : 400,align:'left'}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                   	  cmdefware.loadDetail(rowData);
		                }}'
		    	/>
        	</div>
        	<#--显示列表end-->
		</div>
	</div>  
	<#-- 主表end -->
	<#-- 新增修改页面 BEGIN -->
	<div id="openUI" class="easyui-dialog" style="padding:10px;height:350px;width:620px;"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		minimizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
			<div data-options="region:'north',border:false" >
		    </div>
			<div data-options="region:'center',border:false">
				<form name="dataForm" id="dataForm" method="post" class="city-form">
					<input type="hidden" id="opt"/>
					<table width='580'>
						<tr>
						    <td class="common-td">仓区编码：</td>
						   	<td ><input class="easyui-validatebox  ipt" style="width:150px" name="wareNo" id="wareNo"  data-options="required:true,validType:['vLength[0,64,\'最多只能输入64个字符\']']"  /></td>
							<td class="common-td blank">仓区名称：</td>
							<td ><input class="easyui-validatebox ipt" style="width:150px" name="wareName" id="wareName"    data-options="required:true,validType:['vLength[0,64,\'最多只能输入64个字符\']']"  /></td>
						</tr>
						<tr> 
							<td class="common-td">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
							<td colspan="3"><input class="easyui-validatebox ipt" style="width:454px" name="wareRemark" id="wareRemark"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
						</tr>
						<tr>
						    <td colspan="4">
						    	<div align="center">
						        	<a id="info_save" href="javascript:cmdefware.manage();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
									<a id="info_close" href="javascript:cmdefware.closeUI();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
								</div>
						  	</td>
						 </tr> 
					</table>
				</form>
			</div>
			<#--查询end-->
		</div>
	</div> 
	<#-- 新增修改页面 END -->
</body>
</html>