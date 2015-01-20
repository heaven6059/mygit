<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>移库回单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billomoutstock/planQuery.js?version=1.0.5.4"></script>
</head>
<body class="easyui-layout">
   <div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id='main-toolbar' listData=[
			{"title":"查询","iconCls":"icon-search","action":"planQuery.searchData();","type":0}
			{"title":"清除","iconCls":"icon-remove","action":"planQuery.searchClear('searchForm');","type":0}
			{"title":"修改","iconCls":"icon-edit","action":"planQuery.edit();","type":2}
			{"title":"审核","iconCls":"icon-aduit","action":"planQuery.audit();","type":4}
		    {"title":"关闭","iconCls":"icon-close","action":"closeWindow('移库回单');","type":0}
		]/>	
  	</div>
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
               <div  data-options="region:'north',border:false" >
	               	<form name="searchForm" id="searchForm" metdod="post" class="city-form">
	               		<table>
	               			<tr>
	               				<td class="common-td blank">单据状态：</td>
	               				<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
	               				<td class="common-td blank">创建人：</td>
	               				<td><input class="combobox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
	               				<td class="common-td blank">创建日期：</td>
	               				<td><input class="easyui-datebox" style="width:120px" name="createTmStart" id="createTmStartCondition" /></td>
	               				<td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
	               				<td><input class="easyui-datebox" style="width:120px" name="createTmEnd" id="createTmEndCondition" /></td>
	               			</tr>
	               			<tr>
	               				<td class="common-td blank">单据编号：</td>
	               				<td><input class="easyui-validatebox ipt" style="width:120px" name="outstockNo" id="outstockNoCondition" /></td>
	               				<td class="common-td blank">审核人：</td>
	               				<td><input class="combobox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
	               				<td class="common-td blank">审核日期：</td>
	               				<td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittmCondition" /></td>
	               				<td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
	               				<td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittmCondition" /></td>
	               			</tr>
	               			<tr>
	               				<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		             			<td class="common-td blank">品&nbsp;&nbsp;&nbsp;&nbsp;牌：</td>
	               				<td colspan='5'><input class="easyui-combobox ipt" style="width:454px" name="brandNo" id="brandNoCondition" /></td>
	               			</tr>
	               		</table>
					</form>
               </div>
                <div  data-options="region:'center',border:false" >
	               	<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="移库计划单列表"
			    	isHasToolBar="false" divToolbar="#searchDiv" height="400" emptyMsg="" onClickRowEdit="false"  pagination="true" singleSelect = "false"
				    rownumbers="true"
				    columnsJsonList="[
				    		{field : 'id',checkbox:true},
				        	{field : 'locno',hidden:true},
				        	{field : 'status',title : '状态 ',width : 80,align:'left',formatter:planQuery.columnStatusFormatter},
				        	{field : 'outstockNo',title : '单据编号',width : 180},
				        	{field : 'assignName',title :'指定拣货人员',width : 100,align:'left'},
							{field : 'assignNameCh',title :'指定拣货人员名称',width : 110,align:'left'},
				        	{field : 'creator',title : '创建人',width : 80,align:'left'},
				        	{field : 'creatorname',title : '创建人名称',width : 80,align:'left'},
				            {field : 'createtm',title : '创建时间',width : 140},
				            {field : 'auditor',title : '审核人',width : 80,align:'left'},
				            {field : 'auditorname',title : '审核人名称',width : 80,align:'left'},
				            {field : 'audittm',title : '审核时间',width : 140},
				            {field : 'editor',title : '更新人',width : 100,sortable:true},
						    {field : 'editorname',title : '更新人名称',width : 100,sortable:true},
						    {field : 'edittm',title : '更新时间',width : 130,sortable:true}
				    ]" 
					jsonExtend='{onDblClickRow:function(rowIndex, rowData){
			    		// 触发点击方法  调JS方法
			        	planQuery.loadDetail(rowData);
					}}'/>
               </div>
        </div> 
	</div>      
		<#-- 明细信息div height:470px;-->
	<div id="openWindowPlan" class="easyui-window"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		 <div class="easyui-layout" data-options="fit:true">
           <div  data-options="region:'north',border:false" >
           		<@p.toolbar id='sub-toolbar' listData=[
					{"id":"checkBtn","title":"确认","iconCls":"icon-edit","action":"planQuery.editDetail()","type":2},
				    {"title":"取消","iconCls":"icon-cancel","action":"planQuery.closeWindow('openWindowPlan');","type":0}
				]/>	
           </div>
           <div data-options="region:'center',border:false" >
             	 <div class="easyui-layout" data-options="fit:true">
             	 	 <div data-options="region:'north',border:false" >
             	 	 	<form name="dataForm" id="dataForm" method="post" style="padding:10">
							&nbsp;
							单据编号：<input class="easyui-validatebox ipt" style="width:130px" name="outstockNo" id="outstockNo" readOnly="readOnly" />
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input id="locnoHidden" type="hidden" value=""/>
							<input id="ownerNoHidden" type="hidden" value=""/>
							<input id="status" type="hidden" name="status" style="display:none"/>
						</form>
             	 	 </div>
             	 	 <div data-options="region:'center',border:false" >
             	 	 	<@p.datagrid id="outstockDtlDg" name="" title="移库拣货单明细"  loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  divToolbar="#toolsDiv" height="500"  
				 			onClickRowEdit="false" singleSelect="false"    showFooter="true"
							pagination="true" rownumbers="true"
				 			columnsJsonList="[
				 				{field:'itemId',hidden:true},
				 				{field:'sCellId',hidden:true},
				 				{field:'divideId',hidden:true},
				 				{field:'outstockNo',hidden:true},
				 				{field:'ownerName',title:'货主',width:80,align:'left'},
				 				{field:'itemNo',title:'商品编码',width:140,align:'left'},
				 				{field:'itemName',title:'商品名称',width:150,align:'left'},
							    {field:'colorName',title:'颜色',width:80,align:'left'},
							    {field:'sizeNo',title:'尺码 ',width:60,align:'left'},
							    {field:'itemQty',title:'计划移库数量',width:90,align:'right'},
							    {field:'realQty',title:'实际移库数量',width:90,align:'right',formatter:planQuery.formateRealQty,
							    	editor:{
				 						type:'numberbox'
				 				}},
				 				{field:'sCellNo',title:'来源储位',width:100,align:'left'},
				 				{field:'dCellNo',title:'目的储位',width:100,align:'left'}
				 	]"
				 	
				 	jsonExtend='{onSelect:function(rowIndex, rowData){
		        		 planQuery.selectRow(rowIndex,rowData);
					}}'/>
             	 	 </div>
             	 </div>
             </div>
       </div>
     </div>
</body>
</html>