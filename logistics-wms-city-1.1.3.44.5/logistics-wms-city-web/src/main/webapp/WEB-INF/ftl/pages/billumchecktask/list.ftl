<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退仓验收任务</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billumchecktask/billumchecktask.js?version=1.1.1.3"></script>
</head>

<body class="easyui-layout">

	<#-- 工具菜单div -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
				{"title":"查询","iconCls":"icon-search","action":"billumchecktask.searchData()", "type":0},
		 		{"title":"清除","iconCls":"icon-remove","action":"billumchecktask.searchClear('searchForm')", "type":0},
			 	{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"billumchecktask.add();","type":1},
			    {"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billumchecktask.edit()","type":2},
			    {"id":"btn-remove","title":"删除","iconCls":"icon-del","action":"billumchecktask.doDel()","type":3},
			    {"id":"btn-audit","title":"审核","iconCls":"icon-aduit","action":"billumchecktask.auditUmCheckTask()","type":4},
	            {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('退仓验收任务');","type":0}
		]/>
	</div>
	
	<div data-options="region:'center',border:false">
		
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<div  data-options="region:'north',border:false" >
				<div id="searchDiv" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
							<tr>
								<td class="common-td">状态：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="status" id="statusCondition" /></td>
								<td class="common-td blank">验收任务单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="checkTaskNo" id="checkTaskNoCondition" /></td>
								<td class="common-td blank">客户名称：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="storeName" id="storeName" /></td>
								<td class="common-td blank">店退仓单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo" /></td>
							</tr>
							<tr>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="ownerNo" id="ownerNoCondition" /></td>
								<td class="common-td blank">创建人：</td>
								<td><input class="combobox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="startCreatetmCondition" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="createtmEnd" id="endCreatetmCondition" 
									data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>								
							<tr>
								<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
								<td class="common-td blank">审核人：</td>
								<td><input class="combobox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
								<td class="common-td blank">审核日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="startAudittmCondition" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="audittmEnd" id="endAudittmCondition" 
									data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>
							<tr>
								<td class="common-td blank">所属品牌：</td>
								<td colspan="7"><input class="easyui-combobox ipt" style="width:335px" name="brandNo" id="brandNo" /></td>
							</tr>
						</table>
					</form>	
				</div>
			</div>
			
			<div  data-options="region:'center',border:false" >
				<@p.datagrid id="dataGridJG"  loadUrl="" emptyMsg="" 
					saveUrl=""  defaultColumn=""  title="退仓验收任务单列表"
		               isHasToolBar="false" divToolbar="" height="410"  
		               onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true" showFooter="true"
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'statusStr',title : '状态 ',align:'left',width : 80},
			           		{field : 'checkTaskNo',title : '验收任务单号',width : 140,sortable:true},
			                {field : 'ownerNo',title : '货主',align:'left',width : 80,align:'left',formatter:billumchecktask.ownerFormatter},
			                {field : 'itemQty',title : '计划数量',width : 80,align:'right'},
                            {field : 'checkQty',title : '实际验收数量',width : 100,align:'right'},
			                {field : 'creator',title : '创建人',width : 80,align:'left'},
			                {field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			                {field : 'createtm',title : '创建时间',width : 130,sortable:true},
			                {field : 'auditor',title : '审核人',width : 80,align:'left'},
			                {field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			                {field : 'audittm',title : '审核时间',width : 130,sortable:true},
			                {field : 'remark',title : '备注',width : 100,align:'left'},
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		    				// 触发点击方法  调JS方法
		        			billumchecktask.loadDetail(rowData,1);
				}}'/>
			</div>
		</div>
	</div>
	
	<#--新增div-->
	<div id="addUI" class="easyui-window" title="新增退仓验收任务"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true">
	    	
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
				
					<#-- 工具菜单div -->
		            <@p.toolbar id="addtoolbar"  listData=[
		            	{"title":"查询","iconCls":"icon-search","action":"billumchecktask.searchUntreadData()", "type":0},
		 				{"title":"清除","iconCls":"icon-remove","action":"billumchecktask.searchUntreadClear('searchUntreadForm')", "type":0},
		            	{"id":"btn-save-detail","title":"保存","iconCls":"icon-save","action":"billumchecktask.doSave()", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"billumchecktask.closeWindow('addUI')","type":0}
		            ]/>
		            
		            <form name="searchUntreadForm" id="searchUntreadForm" method="post" class="city-form" style="padding:10px;">
		            	<table>
			   				<tr>
			   					<td class="common-td">货主：</td>
			   					<td><input class="easyui-combobox" data-options="editable:false,required:true" style="width:120px" name="ownerNo" id="search_ownerNo"/></td>
			   					<td class="common-td blank">创建人：</td>
			   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" data-options="editable:false" id="search_creator" name="creator"/></td>
			   					<td class="common-td blank">创建日期：</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="startCreatetm"/></td>
			   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="endCreatetm"/></td>
			   				</tr>
			   				<tr>
			   					<td class="common-td">店退仓单号：</td>
			   					<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo"/></td>
			   					<td class="common-td blank">审核人：</td>
			   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" data-options="editable:false" id="search_auditor" name="auditor"/></td>
			   					<td class="common-td blank">审核日期：</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="startAudittm"/></td>
			   					<td class="common-line" style="padding:0px 5px;">&mdash;</td>
			   					<td><input class="easyui-datebox" style="width:120px" name="audittmEnd" id="endAudittm"/></td>
			   				</tr>
			   				<tr>
			   					<td class="common-td">店退仓通知单号：</td>
			   					<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadMmNo"/></td>
			   					<td class="common-td blank">来源单号：</td>
			   					<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo"/></td>
			   					<td class="common-td blank">客户名称：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="storeName" id="search_storeName" /></td>
								<td class="common-td blank" >退仓类型：</td>
			   					<td><input class="easyui-combobox" data-options="editable:false" name="untreadType" id="untreadType_search"  style="width:120px"/></td>
			   				</tr>
			   				<tr>
			   					<td class="common-td">品质：</td>
								<td><input class="easyui-combobox" name="quality" id="quality_search" style="width:120px;"/></td>						
			   					<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="search_sysNoSearch"/></td>
		             			<td class="common-td blank">所属品牌：</td>
								<td colspan = '3'><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="search_brandNo" /></td>
			   				</tr>
			   			</table>
					</form>
				</div>
				
				<div data-options="region:'center',border:false" title="添加店退仓明细">
				
					<div class="easyui-layout" data-options="fit:true">
						<div data-options="region:'west'" data-options="fit:true" style="width:500px;">
						    <@p.datagrid id="untreadDg" name="" title=""  
						 			loadUrl="" saveUrl="" defaultColumn=""  singleSelect = "false"
						 			isHasToolBar="false"  divToolbar="" 
						 			onClickRowEdit="false" pagination="false" rownumbers="true" emptyMsg=""
						 			columnsJsonList="[
						 				{field : 'id',checkbox:true},
						 				{field : 'untreadNo',title : '店退仓单号',width :130,sortable:true},
						 				{field : 'poNo',title : '来源单号',width :130,sortable:true},
						 				{field : 'storeNo',title : '客户编码',width : 80,align:'left',sortable:true},
						 				{field : 'storeName',title : '客户名称',width : 120,align:'left',sortable:true},
						 				{field : 'itemQty',title:'退仓数量',width:60,align:'left',align:'right',sortable:true},
						 				{field : 'remark',title:'备注',width:80,align:'left',align:'left',sortable:true}
						 			]"
						 	jsonExtend='{}'/>
						</div>
						<div data-options="region:'center'" >
						    <div class="easyui-layout" data-options="fit:true">
						    	<div data-options="region:'west'" data-options="fit:true" 
						    		style="width:60px;border-left:0px;border-top:0px;border-bottom:0px;">
						    		<div style="text-align:center;padding-top:100px;">
						            	<a class="easyui-linkbutton" href="javascript:billumchecktask.toRgiht();"> >> </a>
						            	<br/><br/>
						            	<a class="easyui-linkbutton" href="javascript:billumchecktask.toLeft();"> << </a>
						            </div>
						    	</div>
						        <div data-options="region:'center',border:false" >
						        	<@p.datagrid id="untreadDg2" name="" title=""  singleSelect = "false"
								 			loadUrl="" saveUrl="" defaultColumn="" showFooter="true"
								 			isHasToolBar="false"  divToolbar="" 
								 			onClickRowEdit="false" pagination="false" rownumbers="false" emptyMsg=""
								 			columnsJsonList="[
								 				{field : 'id',checkbox:true},
								 				{field : 'ownerNo',hidden:true},
								 				{field : 'untreadNo',title : '店退仓单号',width :130},
								 				{field : 'poNo',title : '来源单号',width :130},
								 				{field : 'storeNo',title : '客户编码',width : 80,align:'left'},
								 				{field : 'storeName',title : '客户名称',width : 120,align:'left'},
								 				{field : 'itemQty',title:'退仓数量',width:60,align:'left',align:'right'},
								 				{field : 'remark',title:'备注',width:80,align:'left',align:'left'}
								 			]"
								 	jsonExtend='{}'/>
						        </div>
						    </div>
						</div>
						
						
					</div>
				</div>
		</div>
			
	</div>
	
	<#-- 详细 -->
	<div id="detailUI" class="easyui-window" title="详情"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true">
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<@p.toolbar  id="viewToolBar"  listData=[
						 {"title":"取消","iconCls":"icon-cancel","action":"billumchecktask.closeWindow('detailUI')","type":0}
				       ]
					/>
					<#-- 工具菜单div -->
		            <form name="dataForm" id="dataForm_view" method="post" class="city-form" style="padding:10px;">
		            	<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="checkTaskNo" id="checkTaskNo_view"  readOnly="readOnly"/></td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="ownerNo" id="ownerNo_view"  /></td>
								<td class="common-td blank">备注：</td>
								<td><input class="easyui-validatebox ipt" style="width:200px;" name="remark" id="remark_view"  data-options="validType:['vLength[0,225,\'最多只能输入225个字符\']']"/></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div data-options="region:'center',border:false">
				 	<@p.datagrid id="checkDtlDgView" name="" title="退仓验收任务明细"  loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  height="295"   showFooter="true"
				 			onClickRowEdit="false" singleSelect="false"   
							pagination="true" rownumbers="true" emptyMsg=""
				 			columnsJsonList="[
				 				{field:'untreadNo',title:'店退仓单号',width:130,align:'left',sortable:true},
				 				{field:'storeNo',title:'客户编码',width:100,align:'left',sortable:true},
				 				{field:'storeName',title:'客户名称',width:150,align:'left',sortable:true},
				 				{field:'itemNo',title:'商品编码',width:150,align:'left',sortable:true},
				 				{field:'itemName',title:'商品名称',width:150,align:'left',sortable:true},
							    {field:'sizeNo',title:'尺码',width:80,align:'left',sortable:true},
							    {field:'colorName',title:'颜色',width:80,align:'left',sortable:true},
							    {field:'brandName',title:'品牌',width:80,align:'left',sortable:true},
							    {field:'boxNo',title:'箱号',width:140,align:'left',sortable:true},
							    {field:'checkWorker',title:'验收人',width:80,align:'left'},
							    {field:'checkWorkerName',title:'验收人名称',width:80,align:'left'},
							    {field:'itemQty',title:'计划数量',width:80,align:'right',sortable:true},
							    {field:'checkQty',title:'验收数量',align:'right',width:80,sortable:true},
							    {field:'difQty',title:'差异数量',width:80,align:'right',sortable:true}
				 	]"/>
				 	
				</div>
		</div>
	</div>          
	
	
	<#-- 修改 -->
	<div id="editUI" class="easyui-window" title="修改"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true">
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<@p.toolbar  id="editToolBar"  listData=[
		            	 {"title":"修改","iconCls":"icon-edit","action":"billumchecktask.doEdit()", "type":0},
						 {"title":"取消","iconCls":"icon-cancel","action":"billumchecktask.closeWindow('editUI')","type":0}
				       ]
					/>
					<#-- 工具菜单div -->
		            <form name="dataEditForm" id="dataEditForm" method="post" class="city-form" style="padding:10px;">
		            	<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="checkTaskNo" id="checkTaskNo_edit"  readOnly="readOnly"/></td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="ownerNo" id="ownerNo_edit"  /></td>
								<td class="common-td blank">备注：</td>
								<td><input class="easyui-validatebox ipt" style="width:200px;" name="remark" id="remark_edit"  data-options="validType:['vLength[0,225,\'最多只能输入225个字符\']']"/></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div data-options="region:'center',border:false">
					<div id="toolCheckDtlDiv">
						<@p.toolbar id="toolbar_itemdetal"  listData=[
								{"title":"按计划保存","iconCls":"icon-save-dtl","action":"billumchecktask.savePlanCheckDtl();","type":0},
								{"title":"按单删除","iconCls":"icon-del-dtl","action":"billumchecktask.delDtlByUntreadNo();","type":0},
								{"title":"商品置零","iconCls":"icon-refresh","action":"billumchecktask.updateCheckQtyToZero();","type":0},
								{"title":"添加差异商品","iconCls":"icon-add-dtl","action":"billumchecktask.addDiffItem();","type":0},
								{"title":"删除明细","iconCls":"icon-del-dtl","action":"billumchecktask.delCheckDtl();","type":0},
								{"title":"保存明细","iconCls":"icon-save-dtl","action":"billumchecktask.saveCheckDtl();","type":0}
						]/>
				 	</div>
				 	<@p.datagrid id="checkDtlDgEdit" name="" title="退仓验收任务明细"  loadUrl=""
				 			 saveUrl="" defaultColumn=""  divToolbar="#toolCheckDtlDiv"
				 			isHasToolBar="false"  height="295"   showFooter="true"
				 			onClickRowEdit="true" singleSelect="true"   
							pagination="true" rownumbers="true" emptyMsg=""
				 			columnsJsonList="[
				 				{field : 'id',checkbox:true},
				 				{field:'brandNo',hidden:true},
				 				{field:'addFlag',hidden:true},
				 				{field:'untreadNo',title:'店退仓单号',width:130,align:'left'},
				 				{field:'storeNo',title:'客户编码',width:100,align:'left'},
				 				{field:'storeName',title:'客户名称',width:150,align:'left'},
				 				{field:'itemNo',title:'商品编码',width:150,align:'left'},
				 				{field:'itemName',title:'商品名称',width:150,align:'left'},
							    {field:'sizeNo',title:'尺码',width:80,align:'left'},
							    {field:'colorName',title:'颜色',width:80,align:'left'},
							    {field:'brandName',title:'品牌',width:80,align:'left'},
							    {field:'boxNo',title:'箱号',width:100,align:'left'},
							    {field:'itemQty',title:'计划数量',width:80,align:'right'},
							    {field:'checkQty',title:'验收数量',align:'right',width:80,
							    	editor:{
				 						type:'numberbox',
				 						options:{
				 							min:0,
					 						required:true,
					 						missingMessage:'验收数量为必填项!'
				 						}
				 					}
				 				}
				 	]"/>
				 	
				</div>
		</div>
	</div>  
	
	
	<#--店退仓单选择删除 -->
	<div id="untreadUI" class="easyui-window"  title="店退仓单"
	    data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	    	
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<#-- 工具菜单div -->
		            <@p.toolbar id="untoolbar"  listData=[
		            	{"title":"查询","iconCls":"icon-search","action":"billumchecktask.searchUntreadDel()", "type":0},
		 				{"title":"清除","iconCls":"icon-remove","action":"billumchecktask.searchClear('unSearchForm')", "type":0},
		            	{"title":"删除","iconCls":"icon-del","action":"billumchecktask.delUntreadByCheckTask()", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"billumchecktask.closeWindow('untreadUI')","type":0}
		            ]/>
		            
		            <form name="unSearchForm" id="unSearchForm" metdod="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td class="common-td">店退仓单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="untreadNo" id="untreadNoConUn" /></td>
								<td class="common-td blank">客户名称：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="storeName" id="storeNameConUn" /></td>
							</tr>
						</table>
					</form>
		        </div>
		        
		        <div data-options="region:'center',border:false">
		        	<#-- 退仓通知单选择数据列表div -->
          	  		<@p.datagrid id="dataGridUn"  loadUrl=""  saveUrl=""  defaultColumn="" 
		               isHasToolBar="false" divToolbar="" width="726" height="325"  onClickRowEdit="false" 
		               singleSelect="true" pageSize='20'  title="退仓通知单列表" 
					   pagination="false" rownumbers="true"
			           columnsJsonList="[
			          		{field : 'id',width : 50, checkbox:true},
			           		{field : 'untreadNo',title : '店退仓单号 ',width : 180},
			           		{field : 'storeNo',title : '客户编码',width : 120,align:'left'},
			                {field : 'storeName',title : '客户名称',width : 150,align:'left'}
			        	]"
			        />
		        </div>
		    </div>
	</div>
  
  
	<#--商品选择div -->
	<div id="itemUI" class="easyui-window"  title="差异商品选择"
	    data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	    
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					
					<#-- 工具菜单div -->
		            <@p.toolbar id="itemtoolbar"  listData=[
		            	{"title":"查询","iconCls":"icon-search","action":"billumchecktask.searchFilterItem()", "type":0},
		 				{"title":"清除","iconCls":"icon-remove","action":"billumchecktask.searchClear('itemSearchForm')", "type":0},
		            	{"title":"确定","iconCls":"icon-ok","action":"billumchecktask.selectItem()", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"billumchecktask.closeWindow('itemUI')","type":0}
		            ]/>
				
					<form name="itemSearchForm" id="itemSearchForm" metdod="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td class="common-td">品牌库：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoConIt" /></td>
								<td class="common-td blank">所属品牌：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoConIt" /></td>
								<td class="common-td blank">商品编码：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoConIt" /></td>
								<td class="common-td blank">商品名称：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemNameConIt"/></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div data-options="region:'center',border:false">
					<div id="itemDiv">
						店退仓单：<input class="easyui-combobox" style="width:160px" name="untreadNo" id="untreadNoList"  data-options="editable:false"/>
						&nbsp;&nbsp;
						客户名称：<label id="lbStoreName"></label>
						<input id="storeNoHid" type="hidden"/>
						<input id="storeNameHid" type="hidden"/>
						
						<input id="itemTypeHid" type="hidden"/>
						<input id="qualityHid" type="hidden"/>
					</div>
					<#-- 商品选择数据列表div -->
          	  		<@p.datagrid id="dataGridItem"  loadUrl=""  saveUrl=""  defaultColumn="" title="商品列表"
		               isHasToolBar="false" divToolbar="" width="726" height="325" onClickRowEdit="false"  
		               pagination="true" singleSelect = "false" divToolbar="#itemDiv"
			           rownumbers="true"
			           columnsJsonList="[
			           		{field : 'id',width : 50, checkbox:true},
			           		{field:'itemNo',title:'商品编码',width:140,align:'left'},
				 			{field:'itemName',title:'商品名称',width:130,align:'left'},
							{field:'colorName',title:'颜色',width:80,align:'left'},
							{field:'sizeNo',title:'尺码',width:60,align:'left'}
			            ]" 
				    jsonExtend='{}'/>
				</div>
			</div>
					
	</div>

</body>
</html>