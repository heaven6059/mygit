<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退厂申请</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billwmrequest/billwmrequest.js?version=1.0.6.2"></script>    
</head>
<body class="easyui-layout">
	<#-- 工具菜单div -->	
     <div data-options="region:'north',border:false">
       <@p.toolbar id="maintoolbar"  listData=[
      		 {"title":"查询","iconCls":"icon-search","action":"billwmrequest.searchArea()","type":0},
      		 {"title":"清空","iconCls":"icon-remove","action":"billwmrequest.searchLocClear()","type":0},
	         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('退厂申请')","type":0}
	         ]
		  />
	 </div>
	 <div data-options="region:'center',border:false">
	 	<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
		   		<form name="searchForm" id="searchForm" method="post" class="city-form">
		   			<table>
		   				<tr>
		   					<td class="common-td blank">状态：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="status" id="search_status" /></td>
		   					<td class="common-td blank">货主：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="ownerNo" id="search_ownerNo" /></td>
		   					<td class="common-td blank">申请单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="requestNo"/></td>
							<td class="common-td blank">来源单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="sourceNo"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">申请类型：</td>
							<td><input class="easyui-combobox" data-options="editable:false" name="requestType" id="requestType_search"  style="width:120px"/></td>
		   					<td class="common-td blank">创建人：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" data-options="editable:false" id="search_creator" name="creator"/></td>
		   					<td class="common-td blank">创建日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetm"/></td>
		   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetm"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">业务类型：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessType" /></td>
		   					<td class="common-td blank">审核人：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" data-options="editable:false" id="search_auditor" name="auditor"/></td>
		   					<td class="common-td blank">审核日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittm"/></td>
		   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittm"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
							<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		   					<td class="common-td blank">所属品牌：</td>
	             			<td colspan="8">
	             				<input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" />
	             			</td>
		   				</tr>
		   			</table>
				</form>
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn=""   title="退厂申请列表"
	              isHasToolBar="false"  onClickRowEdit="false"  
	               pagination="true" rownumbers="true" emptyMsg="" singleSelect = "false"
		           columnsJsonList="[
						{field : ' ',checkbox:true},
						{field : 'statusName',title : '状态',width : 70,align:'left'},
						{field : 'requestNo',title : '申请单号',width :180},
						{field : 'sourceNo',title : '来源单号',width :180},
						{field : 'ownerName',title : '货主',width : 120,align:'center'},
						{field : 'requestTypeName',title : '申请类型',width : 100,align:'center'},
						{field : 'businessTypeName',title : '业务类型',width : 100,align:'center'},
						{field : 'creator',title : '创建人',width : 100,align:'left'},
						{field : 'creatorName',title : '创建人名称',width : 100,sortable:true},
						{field : 'createtm',title : '创建时间',width : 130,sortable:true,sortable:true},
						{field : 'auditor',title : '审核人',width : 100,align:'left'},
						{field : 'auditorName',title : '审核人名称',width : 100,sortable:true},
						{field : 'audittm',title : '审核时间',width : 130,sortable:true}
		                 ]" 
			           jsonExtend='{onSelect:function(rowIndex, rowData){
	                            // 触发点击方法  调JS方法
	                   },onDblClickRow:function(rowIndex, rowData){
	                   	    billwmrequest.showDetail(rowData);
	                   }}'/>
			</div>
		</div>
	</div>

<#-- 详情 -->
<div id="detailDialogView" class="easyui-window" title="明细"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="padding:5px;">
	    	<form id="detailForm_view" name="detailForm" method="post" class="city-form">
	    		<table>
					<tr>
						<td class="common-td blank">申请单号：</td>
						<td>
							<input class="easyui-validatebox ipt" name="requestNo" style="width:120px;" readonly="readonly"/>
						</td>
						<td class="common-td blank">货主：</td>
						<td>
							<input class="easyui-validatebox ipt" data-options="editable:false"  name="ownerName" style="width:120px"/>
						</td>
						<td class="common-td blank">申请类型：</td>
						<td>
							<input class="easyui-validatebox ipt" data-options="editable:false" name="requestTypeName"   style="width:120px"/>
						</td>
						<td class="common-td blank">业务类型：</td>
						<td>
							<input class="easyui-validatebox ipt" data-options="editable:false" name="businessTypeName"   style="width:120px"/>
						</td>
					</tr>
					<tr>
						<td class="common-td blank">来源单号：</td>
						<td colspan="8"><input class="easyui-validatebox ipt" name="sourceNo" style="width:120px"  /></td>
					</tr>
	    		</table>
		    </form>
		</div>
		<div data-options="region:'center'">
			<@p.datagrid id="dataGridJG_view"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true"
					           rownumbers="true"  onClickRowEdit="false" singleSelect="true"  title="明细列表"  emptyMsg="" showFooter="true"
					           columnsJsonList="[		
									{field:'itemNo',title:'商品编码',width:150,align:'left'},
									{field:'itemName',title:'商品名称',width:150,align:'left'},
									{field:'colorName',title:'颜色',width:80,align:'left'},
									{field:'sizeNo',title:'尺码',width:80,align:'left'},
									{field:'packQty',title:'包装数量',width:80,align:'right'},
									{field:'itemQty',title:'数量',width:80,align:'right'}
								]"/>
		</div>
	</div>
</div>
</body>
</html>