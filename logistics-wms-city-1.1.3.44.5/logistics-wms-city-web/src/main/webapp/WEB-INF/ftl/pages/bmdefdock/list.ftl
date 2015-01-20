<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>码头资料维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/defdock.js?version=1.0.5.0"></script>
    <script type="text/javascript" src="${domainStatic}/resources/common/other-lib/common.js?version=1.0.5.0"></script>
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/defdock.css?version=1.0.5.0" />
</head>
<body class="easyui-layout">
	<!-- 工具菜单 -->
	<div data-options="region:'north',border:false" class="toolbar-region">
	       <@p.toolbar id="toolbar"  listData=[
	       				 {"title":"查询","iconCls":"icon-search","action":"defdock.searchDock()", "type":0},
	                     {"title":"清除","iconCls":"icon-remove","action":"defdock.searchClear()", "type":0},
	                     {"title":"新增","iconCls":"icon-add","action":"defdock.addInfo()", "type":1},
	                     {"title":"修改","iconCls":"icon-edit","action":"defdock.editInfo()","type":2},
	                     {"title":"删除","iconCls":"icon-del","action":"defdock.del()","type":3},
				         {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('码头资料维护')","type":0}
	                   ]
			  />
	  </div>
	 <div data-options="region:'center',border:false">
	 	<div class="easyui-layout" data-options="fit:true" id="subLayout">
	 		<!--搜索start-->
			<div data-options="region:'north',border:false" >
           		<div class="search-div">
           			<form name="searchForm" id="searchForm" method="post" class="city-form">
                     	<table>
                     		<tr>
                     			<td class="common-td">码头编码：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="dockNo" id="dockNoCondition" /></td>
                     			<td class="common-td blank">码头名称：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="dockName" id="dockNameCondition" /></td>
                     			<td class="common-td blank">码头类型：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="dockType" id="dockTypeCondition" /></td>
                     		</tr>
                     	</table>
					</form>
           		</div>
			</div>
			<!--显示列表-->
            <div data-options="region:'center',border:false" id="r1">
            	<@p.datagrid id="dataGridJG"  loadUrl="/bm_defdock/list.json?locno=${session_user.locNo}"  saveUrl=""   defaultColumn=""   title="码头资料维护"
		              isHasToolBar="false" divToolbar="" onClickRowEdit="false"    pagination="true"
			           rownumbers="true"  singleSelect = "false" height="398"
			           columnsJsonList="[
			           		  {field : ' ',checkbox:true},
			                  {field : 'dockNo',title : '码头编码',width : 100,align:'left'},
			                  {field : 'dockName',title : '码头名称',width : 150,align:'left'},
			                  {field : 'dockType',title : '码头类型',width : 150,formatter:defdock.columnTypeFormatter},
			                  {field : 'adjustBoard',title : '是否有调节板',width : 150,formatter:defdock.columnAdjustBoardFormatter},
			                  {field : 'locateType',title : '定位类型',width : 120,formatter:defdock.columnLocalTypeFormatter},
			                  {field : 'creator',hidden:'true'},
			                  {field : 'createtm',hidden:'true'},
			                  {field : 'editor',hidden:'true'},
			                  {field : 'edittm',hidden:'true'}
			                 ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                     // defdock.selectedCheckBox(rowIndex);
		                   },onDblClickRow:function(rowIndex, rowData){
		                   	//双击方法
		                   	  defdock.edit(rowData)
		                   }}'/>
            </div>
	 	</div>
	 </div>
<!-- 新增  窗口 -->
<div id="showDialog"  class="easyui-dialog" title="新增"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	    	<div class="easyui-layout" data-options="fit:true,border:false">
	    		<div data-options="region:'north',border:false">
					<@p.toolbar id="addtoolbar"   listData=[
					             {"id":"info_add","title":"保存","iconCls":"icon-save","action":"defdock.save_do()", "type":0},
					             {"id":"info_edit","title":"保存","iconCls":"icon-save","action":"defdock.edit_do()", "type":0},
					             {"title":"取消","iconCls":"icon-cancel","action":"defdock.closeWin('showDialog')","type":0}
					           ]
					  />
					<div class="search-div" style="border:none">
						<form name="dataForm" id="dataForm" method="post" class="city-form">
							<table>
								<tr>
									<td class="common-td">码头编码：</td>
									<td>
										<input class="easyui-validatebox ipt" style="width:120px" name="dockNo" id="dockNo" required="true"  data-options="validType:['vLength[0,3,\'最多只能输入3个字符\']']"/>
										<input class="easyui-validatebox ipt" style="width:120px" name="dockNo" id="dockNoHide" type="hidden"/>
									</td>
									<td class="common-td blank">码头名称：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="dockName" id="dockName" required="true" data-options="validType:['vLength[0,50,\'最多只能输入50个字符\']']"/></td>
									<td class="common-td blank">码头类型：</td>
									<td><input class="combobox" style="width:120px" name="dockType" id="dockType" data-options="editable:false"/></td>
									<td class="common-td blank">定位类型：</td>
									<td><input class="easyui-combobox" style="width:120px" name="locateType" id="locateType" data-options="editable:false"/></td>
								</tr>
								<tr>
									<td class="common-td">有调节板：</td>
									<td colspan="7"><input type="radio" checked="checked" name="adjustBoard" value="0">没有&nbsp;&nbsp;<input type="radio" name="adjustBoard" value="1">有</td>
								</tr>
								<tr id="creatorinfo">
									<td class="common-td">创&nbsp;建&nbsp;人：</td>
									<td id="creator"></td>
									<td class="common-td blank">创建时间：</td>
									<td id="createtm" colspan="5"></td>
								<tr id="editorinfo">
									<td class="common-td blank">修&nbsp;改&nbsp;人：</td>
									<td id="editor"></td>
									<td class="common-td blank">修改时间：</td>
									<td id="edittm" colspan="5"></td>
								</tr>
							</table>
			         	</form>
					</div>
	    		</div>
	    	</div>
</div> 
		    
		    
		    
		 <#--<div id="showDialog"  class="easyui-window" title="新增"  
		    style="width:500px;padding:8px;"   
		    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
		    minimizable:false"> 
		     <form name="dataForm" id="dataForm" method="post">
					<table border="1">
						<tr>
							<td>码头编码：</td>
							<td>
								<input class="easyui-validatebox" style="width:100px" name="dockNo" id="dockNo" required="true"  data-options="validType:['vLength[0,3,\'最多只能输入3个字符\']']"/>
								<input class="easyui-validatebox" style="width:100px" name="dockNo" id="dockNoHide" type="hidden"/>
							</td>
							<td style="padding-left:15px;">码头名称：</td>
							<td><input class="easyui-validatebox" style="width:100px" name="dockName" id="dockName" required="true" data-options="validType:['vLength[0,50,\'最多只能输入50个字符\']']"/></td>
						</tr>
						<tr>
							<td>码头类型：</td>
							<td><input class="combobox" style="width:100px" name="dockType" id="dockType" data-options="editable:false"/></td>
							<td style="padding-left:15px;">定位类型：</td>
							<td><input class="easyui-combobox" style="width:100px" name="locateType" id="locateType" data-options="editable:false"/></td>
						</tr>
						<tr colspan="6">
							<td>是否有调节板：</td>
							<td><input type="radio" checked="checked" name="adjustBoard" value="0">没有<input type="radio" name="adjustBoard" value="1">有</td>
						</tr>
						<tr id="creatorinfo">
							<td>创建人：</td>
							<td id="creator"></td>
							<td style="padding-left:15px;">创建时间：</td>
							<td id="createtm"></td>
						</tr>
						<tr id="editorinfo">
							<td>修改人：</td>
							<td id="editor"></td>
							<td style="padding-left:15px;">修改时间：</td>
							<td id="edittm"></td>
						</tr>
						<tr>
							<td colspan="6" style="text-align:center;">
								<a id="info_add" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
								<a id="info_edit" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a> 
							</td>
						</tr>
					</table>
			 </form>	
		</div>	
	</div>
-->   
</body>
</html>