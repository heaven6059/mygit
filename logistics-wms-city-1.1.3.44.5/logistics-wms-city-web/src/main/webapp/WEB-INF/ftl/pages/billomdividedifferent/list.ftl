<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>差异调整单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billomdividedifferent/billomdividedifferent.js?version=1.1.1.2"></script>
</head>

<body class="easyui-layout">

	<#-- 工具菜单div -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
				{"title":"查询","iconCls":"icon-search","action":"billomdividedifferent.searchData()", "type":0},
		 		{"title":"清除","iconCls":"icon-remove","action":"billomdividedifferent.searchClear('searchForm')", "type":0},
			 	<#--{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"billomdividedifferent.add();","type":1},-->
			    {"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billomdividedifferent.edit()","type":2},
			    {"id":"btn-remove","title":"删除","iconCls":"icon-del","action":"billomdividedifferent.doDel()","type":3},
			    {"id":"btn-audit","title":"审核","iconCls":"icon-aduit","action":"billomdividedifferent.auditDivideDifferent()","type":4},
	            {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('差异调整单');","type":0}
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
								<td class="common-td">单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="differentNo" id="differentNoCondition" /></td>
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
								<td class="common-td">条码：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcodeCondition" /></td>
								<td class="common-td blank">来源类型：</td>
								<td><input class="combobox ipt" style="width:120px" name="differentType" id="differentTypeCondition" /></td>
								<td class="common-td blank">来源单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="sourceNo" id="sourceNoCondition" /></td>
								<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
							</tr>
							<tr>
		             			<td class="common-td">所属品牌：</td>
								<td colspan="7"><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" /></td>
							</tr>
						</table>
					</form>	
				</div>
			</div>
			
			<div  data-options="region:'center',border:false" >
				<@p.datagrid id="dataGridJG"  loadUrl=""  
					saveUrl=""  defaultColumn=""  title="差异调整单列表"
		               isHasToolBar="false" divToolbar="" height="410"  
		               onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true" emptyMsg ="" showFooter="true"
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'statusStr',title : '状态 ',align:'left',width : 80},
			           		{field : 'differentNo',title : '单号编号',width : 140},
			                {field : 'ownerNo',title : '货主',align:'left',width : 80,align:'left',formatter:billomdividedifferent.ownerFormatter},
			                {field : 'differentQty',title : '调整数量',width : 80,align:'right'},
                            {field : 'differentTypeStr',title : '来源类型',width : 80},
                            {field : 'sourceNo',title : '来源单号',width : 140},
			                {field : 'creator',title : '创建人',width : 80,align:'left'},
			                {field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			                {field : 'createtm',title : '创建时间',width : 130,sortable:true},
			                {field : 'auditor',title : '审核人',width : 80,align:'left'},
			                {field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			                {field : 'audittm',title : '审核时间',width : 130,sortable:true}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		    				// 触发点击方法  调JS方法
		        			billomdividedifferent.loadDetail(rowData,1);
				}}'/>
			</div>
		</div>
	</div>
	
	<#--新增div
	<div id="addUI" class="easyui-window" title="新增退仓验收任务"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true">
	    	
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
				
		            <@p.toolbar id="addtoolbar"  listData=[
		            	{"title":"查询","iconCls":"icon-search","action":"billomdividedifferent.searchUntreadData()", "type":0},
		 				{"title":"清除","iconCls":"icon-remove","action":"billomdividedifferent.searchUntreadClear('searchUntreadForm')", "type":0},
		            	{"id":"btn-save-detail","title":"保存","iconCls":"icon-save","action":"billomdividedifferent.doSave()", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"billomdividedifferent.closeWindow('addUI')","type":0}
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
						            	<a class="easyui-linkbutton" href="javascript:billomdividedifferent.toRgiht();"> >> </a>
						            	<br/><br/>
						            	<a class="easyui-linkbutton" href="javascript:billomdividedifferent.toLeft();"> << </a>
						            </div>
						    	</div>
						        <div data-options="region:'center',border:false" >
						        	<@p.datagrid id="untreadDg2" name="" title=""  singleSelect = "false"
								 			loadUrl="" saveUrl="" defaultColumn="" showFooter="true"
								 			isHasToolBar="false"  divToolbar="" 
								 			onClickRowEdit="false" pagination="false" rownumbers="false" emptyMsg=""
								 			columnsJsonList="[
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
	-->
	
	<#-- 详细 -->
	<div id="detailUI" class="easyui-window" title="详情"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true">
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<@p.toolbar  id="viewToolBar"  listData=[
						 {"title":"取消","iconCls":"icon-cancel","action":"billomdividedifferent.closeWindow('detailUI')","type":0}
				       ]
					/>
					<#-- 工具菜单div -->
		            <form name="dataForm" id="dataForm_view" method="post" class="city-form" style="padding:10px;">
		            	<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="differentNo" id="differentNo_view"  readOnly="readOnly"/></td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="ownerNo" id="ownerNo_view"  /></td>
								<td class="common-td blank">备注：</td>
								<td><input class="easyui-validatebox ipt" style="width:200px;" name="remark" id="remark_view"  data-options="validType:['vLength[0,225,\'最多只能输入225个字符\']']"/></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div data-options="region:'center',border:false">
				 	<@p.datagrid id="defferentDtlDgView" name="" title="差异调整单明细"  loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  height="295"   showFooter="true"
				 			onClickRowEdit="false" singleSelect="false"   
							pagination="true" rownumbers="true" emptyMsg=""
				 			columnsJsonList="[
				 				{field:'pixFlagStr',title:'拆分标示',width:80,sortable:true},
				 				{field:'storeNo',title:'客户编码',width:80,align:'left',sortable:true},
				 				{field:'sCellNo',title:'储位编码',width:100,align:'left',sortable:true},
				 				{field:'boxNo',title:'来源箱号',width:110,align:'left',sortable:true},
				 				{field:'expNo',title:'发货通知单',width:130,align:'left',sortable:true},
				 				{field:'sItemNo',title:'调整前商品编码',width:150,align:'left',sortable:true},
				 				{field:'sBarcode',title:'调整前商品条码',width:150,align:'left',sortable:true},
				 				{field:'sItemName',title:'调整前商品名称',width:150,align:'left',sortable:true},
							    {field:'brandName',title:'品牌',width:80,align:'left',sortable:true},
							    {field:'dItemNo',title:'调整后商品编码',width:150,align:'left',sortable:true},
				 				{field:'dBarcode',title:'调整后商品条码',width:150,align:'left',sortable:true},
				 				{field:'dItemName',title:'调整后商品名称',width:150,align:'left',sortable:true},
							    {field:'itemQty',title:'商品数量',width:80,align:'right',sortable:true},
							    {field:'realQty',title:'拆分数量',width:80,align:'right',sortable:true,hidden:true}
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
		            	 {"title":"修改","iconCls":"icon-edit","action":"billomdividedifferent.doEdit()", "type":0},
						 {"title":"取消","iconCls":"icon-cancel","action":"billomdividedifferent.closeWindow('editUI')","type":0}
				       ]
					/>
					<#-- 工具菜单div -->
		            <form name="dataEditForm" id="dataEditForm" method="post" class="city-form" style="padding:10px;">
		            	<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:110px" name="differentNo" id="differentNo_edit"  readOnly="readOnly"/></td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" name="ownerNo" id="ownerNo_edit"  /></td>
								<td class="common-td blank">备注：</td>
								<td><input class="easyui-validatebox ipt" style="width:200px;" name="remark" id="remark_edit"  data-options="validType:['vLength[0,225,\'最多只能输入225个字符\']']"/></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div data-options="region:'center',border:false">
					<div id="toolDefferentDtlDiv">
						<@p.toolbar id="toolbar_itemdetal"  listData=[
								{"id":"d_split","title":"拆分","iconCls":"icon-cut","action":"billomdividedifferent.splitDifferentDtl()","type":0},
								{"title":"删除明细","iconCls":"icon-del-dtl","action":"billomdividedifferent.delDifferentDtl();","type":0},
								{"title":"保存明细","iconCls":"icon-save-dtl","action":"billomdividedifferent.saveDifferentDtl();","type":0}
						]/>
				 	</div>
				 	<@p.datagrid id="defferentDtlDgEdit" name="" title="差异调整单明细"  loadUrl=""
				 			 saveUrl="" defaultColumn=""  divToolbar="#toolDefferentDtlDiv"
				 			 isHasToolBar="false"  height="295"   showFooter="true"
				 			 onClickRowEdit="false" singleSelect="true"   onClickRowAuto="false"
							 pagination="true" rownumbers="true" emptyMsg=""
				 			 columnsJsonList="[
				 			 	{field : 'id',checkbox:true},
				 			 	{field:'storeNo',title:'客户编码',width:80,align:'left'},
				 				{field:'sCellNo',title:'储位编码',width:100,align:'left'},
				 				{field:'boxNo',title:'来源箱号',width:110,align:'left'},
				 				{field:'expNo',title:'发货通知单',width:130,align:'left',sortable:true},
				 				{field:'sItemNo',title:'调整前商品编码',width:150,align:'left'},
				 				{field:'sBarcode',title:'调整前商品条码',width:150,align:'left'},
				 				{field:'sItemName',title:'调整前商品名称',width:150,align:'left'},
							    {field:'brandName',title:'品牌',width:80,align:'left'},
							    {field:'dItemNo',title:'调整后商品编码',width:150,align:'left',
							    	editor:{
				 						type:'validatebox',
				 						options:{
					 						required:true,
					 						missingMessage:'调整后商品编码必填项!'
				 						}
				 					}
				 				},
				 				{field:'dBarcode',title:'调整后商品条码',width:150,align:'left',
				 					editor:{
				 						type:'validatebox',
				 						options:{
					 						required:true,
					 						missingMessage:'调整后商品条码为必填项!'
				 						}
				 					}
				 				},
				 				{field:'dItemName',title:'调整后商品名称',width:150,align:'left'},
							    {field:'itemQty',title:'商品数量',width:80,align:'right'},
							    {field:'realQty',title:'拆分数量',width:80,align:'right',hidden:true}
				 	]"
				 	jsonExtend='{onClickRow:function(rowIndex, rowData){
                	  	 // 触发点击方法  调JS方法
	                   	 billomdividedifferent.onClickRowDtl(rowIndex,rowData);
	                }}'
				 	/>
				 	
				</div>
		</div>
	</div>  
	
	<#--商品选择div 
	<div id="itemUI" class="easyui-window"  title="差异商品选择"
	    data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	    
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					
		            <@p.toolbar id="itemtoolbar"  listData=[
		            	{"title":"查询","iconCls":"icon-search","action":"billomdividedifferent.searchFilterItem()", "type":0},
		 				{"title":"清除","iconCls":"icon-remove","action":"billomdividedifferent.searchClear('itemSearchForm')", "type":0},
		            	{"title":"确定","iconCls":"icon-ok","action":"billomdividedifferent.selectItem()", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"billomdividedifferent.closeWindow('itemUI')","type":0}
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
	-->
</body>
</html>