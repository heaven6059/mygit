<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>委托业主管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/bmdefowner.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
      <#-- 工具菜单div 	-->
       <div data-options="region:'north',border:false" style="margin-bottom:0px;height:33px;background:#F4F4F4;padding:3px;" id="toolDiv">
			   <@p.toolbar  id="toolBarOne" listData=[
			   				 {"title":"查询","iconCls":"icon-search","action":"bmdefowner.searchBmdefowner();","type":0},
		       				 {"title":"清空","iconCls":"icon-remove","action":"bmdefowner.searchClear();","type":0}
			    			 {"title":"新增","iconCls":"icon-add","action":"bmdefowner.addUI();","type":1},
			    			 {"title":"修改","iconCls":"icon-edit","action":"bmdefowner.editUI();","type":2},
			    			 {"title":"删除","iconCls":"icon-remove","action":"bmdefowner.deleteRows()","type":3},
					         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('委托业主管理');","type":0}
	                       ]
				  />	
	   </div>
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true">
 		<div data-options="region:'north',border:false" >
	  		<div id="searchDiv" style="height:40px;padding:5px">
		       		 <form name="searchForm" id="searchForm" method="post" class="city-form">
		       		 	<table>
		       		 		<tr>
		       		 			<td class="common-td">货主编号：</td>
		       		 			<td><input class="easyui-validatebox ipt" style="width:120px" name="ownerNo"/></td>
		       		 			<td class="common-td blank">货主名称：</td>
		       		 			<td><input class="easyui-validatebox ipt" style="width:120px" name="ownerAlias"/></td>
		       		 		</tr>
		       		 	</table>
					</form>
			 </div>
		</div>		 
	         <#-- 数据列表div -->
	          <div data-options="region:'center',border:false">
          	  		<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="货主列表"
		               isHasToolBar="false"  divToolbar="" height="440"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true" emptyMsg=""
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'ownerNo',title : '货主编号',width : 100,align:'left'},
			                {field : 'ownerAlias',title : '货主名称',width : 100,align:'left'},
			                {field : 'ownerName',title : '货主全称',width : 100,align:'left'},
			                {field : 'ownerContact',title : '联系人',width : 90,align:'left'},
			                {field : 'ownerPhone',title : '电话',width : 90},
			                {field : 'ownerFax',title : '传真',width : 90},
			                {field : 'ownerAddress',title : '货主地址',width : 100,align:'left'},
			                {field : 'status',title : '货主状态',width : 100,formatter:bmdefowner.statusDataFormatter},
			                {field : 'ownerRemark',title : '货主备注',width : 100,align:'left'},
			                {field : 'creator',title : '建档人',width : 60,align:'left'},
				          	{field : 'createtm',title : '建档时间',width : 125,sortable:true},
				          	{field : 'editor',title : '修改人',width : 60,align:'left'},
				          	{field : 'edittm',title : '修改时间',width : 125,sortable:true}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                   	  bmdefowner.loadDetail(rowData);
		                }}'/>
	          </div>
	      </div>    
       </div>  	
         	<#-- 明细信息div -->
	     <div id="openUI" class="easyui-window"  style="width:600px;height:350px;padding:8px;"   data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		    minimizable:false">
				 <form name="dataForm" id="dataForm" method="post"  class="city-form">
				 <input type="hidden" id="opt"/>
				 	<table>
				 		<tr>
				 			<td align='left' width='85'>货主编号：</td>
				 			<td width = '150'><input class="easyui-validatebox ipt" style="width:120px" name="ownerNo" id="ownerNo"  data-options="required:true,validType:['vnChinese[\'委托业主编号不能包含中文\']','vLength[0,3,\'最多只能输入3个字符\']']" /></td>
				 			<td align='left' width='85'>货主名称：</td>
				 			<td width = '150'><input class="easyui-validatebox ipt" style="width:120px" name="ownerAlias" id="supplierName"  data-options="required:true,validType:['vLength[0,64,\'最多只能输入64个字符\']']"  /></td>
				 		</tr>
				 		<tr>
				 			<td>货主全称：</td>
				 			<td colspan="3" width = '475'><input class="easyui-validatebox ipt" style="width:410px" name="ownerName" id="supplierLname"    data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"  /></td>
				 		</tr>
				 		<tr>
				 			<td>联系人：</td>
				 			<td><input class="easyui-validatebox ipt" style="width:120px" name="ownerContact" id="cman"     data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']"  /></td>
				 			<td>电话：</td>
				 			<td><input class="easyui-validatebox ipt" style="width:120px" name="ownerPhone" id="cmanPhone"    data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  /></td>
				 		</tr>
				 		<tr>
				 			<td>传真：</td>
				 			<td><input class="easyui-validatebox ipt" style="width:120px" name="ownerFax" id="faxno"   data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  /></td>
				 			<td>货主状态：</td>
				 			<td><input class="easyui-combobox" style="width:120px" name="status" id="status"/></td>
				 		</tr>
				 		<tr>
				 			<td>货主地址：</td>
				 			<td colspan="3"><input class="easyui-validatebox ipt" style="width:410px" name="ownerAddress" id="address"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
				 		</tr>
				 		<tr>
				 		<td>货主备注：</td>
				 		<td colspan="3"><input class="easyui-validatebox ipt" style="width:410px" name="ownerRemark" id="remarks"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"  /></td>
				 		</tr>
				 	</table>
				<span style="padding-left:200px;">
				<a id="info_save" href="javascript:bmdefowner.manage();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a id="info_close" href="javascript:bmdefowner.closeUI();" class="easyui-linkbutton" data-options="iconCls:'icon-close'">取消</a>
				</span>
				</form>	 
			</div>

</body>
</html>