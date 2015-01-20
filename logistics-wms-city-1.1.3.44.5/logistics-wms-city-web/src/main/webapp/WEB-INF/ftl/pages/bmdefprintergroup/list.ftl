<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>打印机组维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/bmdefprintergroup/bmdefprintergroup.js?version=1.0.5.0"></script>  
</head>
<body class="easyui-layout">
	<#-- 工具菜单div -->	
    <div data-options="region:'north',border:false">
       <@p.toolbar id="main-toolbar"  listData=[
       					 {"title":"查询","iconCls":"icon-search","action":"bmdefprintergroup.search()","type":0},
		   		         {"title":"清除","iconCls":"icon-remove","action":"bmdefprintergroup.clearSearch()","type":0},
                         {"id":"btn-close","title":"新增","iconCls":"icon-add","action":"bmdefprintergroup.addInfo()","type":1},
                         {"id":"btn-close","title":"修改","iconCls":"icon-edit","action":"bmdefprintergroup.editInfo()","type":2},
                         {"id":"btn-close","title":"删除","iconCls":"icon-del","action":"bmdefprintergroup.del()","type":3},
				         {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('打印机组维护')","type":0}
	                ]
				  />
	 </div>
	 <div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<form name="searchForm" id="searchForm" method="post" class="city-form">
					<table>
						<tr>
							<td class="common-td blank">机组编码：</td>
							<td><input class="easyui-validatebox ipt" style="width:100px" name="printerGroupNo" id="search_printerGroupNo"/></td>
							<td class="common-td blank">状态：</td>
							<td><input class="easyui-combobox" style="width:100px" name="status" id="search_status"/></td>
							<td class="common-td blank">创建时间：</td>
							<td><input class="easyui-datebox" style="width:100px" name="createtmStart" id="createtmStart"/></td>
							<td class="common-line">&mdash;</td>
							<td><input class="easyui-datebox" style="width:100px" name="createtmEnd" id="createtmEnd"/></td>
						</tr>
						<tr>
							<td class="common-td blank">机组名称：</td>
							<td colspan="7"><input class="easyui-validatebox ipt" style="width:100px" name="printerGroupName"/></td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl="/bm_defprinter_group/list.json?locno=${session_user.locNo}" defaultColumn=""   title="打印机组维护"
		              isHasToolBar="false" divToolbar="#searchDiv" onClickRowEdit="false"    pagination="true"
			           rownumbers="true"  singleSelect = "false" height="465"
			           columnsJsonList="[
			           		  {field : ' ',checkbox:true},
			                  {field : 'printerGroupNo',title : '机组编码',width : 80,align:'left'},
			                  {field : 'printerGroupName',title : '机组名称',width : 200,align:'left'},
			                  {field : 'status',title : '状态',width : 100,align:'left',formatter:bmdefprintergroup.statusFormatter},
			                  {field : 'creator',title : '创建人',width : 100,align:'left'},
			                  {field : 'createtm',title : '创建时间',width : 150},
			                  {field : 'editor',title : '修改人',width : 100,align:'left'},
			                  {field : 'edittm',title : '修改时间',width : 150}
			                 ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                     // defdock.selectedCheckBox(rowIndex);
		                   },onDblClickRow:function(rowIndex, rowData){
		                   	//双击方法
		                   	  bmdefprintergroup.edit(rowData)
		                   }}'/>
			</div>
		</div>
	</div>
 <div id="showDialog"  class="easyui-window" title="新增"  
    style="width:650px;height:225px;"   
    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
    minimizable:false"> 
     <@p.toolbar id="detail-toolbar"  listData=[
	   		 {"id":"info_add","title":"保存","iconCls":"icon-save","action":"","type":0},
	   		 {"id":"info_edit","title":"修改","iconCls":"icon-edit","action":"","type":0}
	   		 {"id":"info_edit","title":"取消","iconCls":"icon-cancel","action":"bmdefprintergroup.closeUI()","type":0}
           ]
	/>	
     <form name="dataForm" id="dataForm" method="post" class="city-form">
     	<#-- 明细信息div -->
			<table>
				<tr>
					<td class="common-td blank">机组编码：</td>
					<td>
						<input class="easyui-validatebox ipt" style="width:150px" name="printerGroupNo" id="printerGroupNo" required="true" data-options="validType:['vLength[0,5,\'最多只能输入5个字符\']']" showType="show"/>
						<input class="easyui-validatebox ipt" style="width:150px" name="printerGroupNo"  type="hidden" showType="hide"/>
					</td>
					<td class="common-td blank">机组名称：</td>
					<td>
						<input class="easyui-validatebox ipt" style="width:150px" name="printerGroupName" id="printerGroupName" required="true" data-options="validType:['vLength[0,50,\'最多只能输入50个字符\']']" showType="show"/>
						<input class="easyui-validatebox ipt" style="width:150px" name="printerGroupName"  type="hidden" showType="hide"/>
					</td>
					<td class="common-td blank">状态：</td>
					<td><input class="easyui-combobox" data-options="editable:false" style="width:100px" name="status" id="status" required="true"/></td>
				</tr>
				<tr id="creator_editor_info1">
					<td class="common-td blank">创建人：</td>
					<td><input class="easyui-validatebox ipt" style="width:150px" id="creator" disabled="true"/></td>
					<td class="common-td blank">创建时间：</td>
					<td colspan="3"><input class="easyui-validatebox ipt" style="width:150px" id="createtm" disabled="true"/></td>
				</tr>
				<tr id="creator_editor_info2">
					<td class="common-td blank">修改人：</td>
					<td><input class="easyui-validatebox ipt" style="width:150px" id="editor" disabled="true"/></td>
					<td class="common-td blank">修改时间：</td>
					<td colspan="3"><input class="easyui-validatebox ipt" style="width:150px" id="edittm" disabled="true"/></td>
				</tr>
			</table>
	 </form>	
</div>	
</body>
</html>