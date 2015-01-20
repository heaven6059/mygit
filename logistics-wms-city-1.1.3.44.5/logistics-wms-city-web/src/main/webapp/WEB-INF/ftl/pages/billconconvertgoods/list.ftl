<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>库存转货单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billconconvertgoods/billconconvertgoods.js?version=1.1.1.2"></script>
	 <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
        <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
    </object>
</head>

<body class="easyui-layout">

	<#-- 工具菜单div -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
				{"title":"查询","iconCls":"icon-search","action":"billconconvertgoods.searchData()", "type":0},
		 		{"title":"清除","iconCls":"icon-remove","action":"billconconvertgoods.searchClear('searchForm')", "type":0},
			 	{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"billconconvertgoods.add();","type":1},
			    {"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billconconvertgoods.edit()","type":2},
			    {"id":"btn-remove","title":"删除","iconCls":"icon-del","action":"billconconvertgoods.doDel()","type":3},
			    {"id":"btn-audit","title":"审核","iconCls":"icon-aduit","action":"billconconvertgoods.auditConvertGoods()","type":4},
			    {"title":"打印预览","id":"printBtn","iconCls":"icon-print","action":"billconconvertgoods.printDetail4SizeHorizontal()", "type":2},
	            {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('库存转货单');","type":0}
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
								<td class="common-td blank">转货单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="convertGoodsNo" id="convertGoodsNoCondition" /></td>
								<td class="common-td blank">转货类型：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="convertType" id="convertTypeCondition" /></td>
								<td class="common-td blank">目的门店：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="storeNo" id="storeNoCondition" /></td>
							</tr>
							<tr>
								<td class="common-td blank">创建人：</td>
								<td><input class="combobox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="startCreatetmCondition" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="createtmEnd" id="endCreatetmCondition" 
									data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="ownerNo" id="ownerNoCondition" /></td>
							</tr>								
							<tr>
								<td class="common-td blank">审核人：</td>
								<td><input class="combobox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
								<td class="common-td blank">审核日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="startAudittmCondition" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="audittmEnd" id="endAudittmCondition" 
									data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
								<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
							</tr>
							<tr>
								<td class="common-td">目的仓库：</td>
			  					<td><input class="easyui-combobox"  name="storeNoLocno" id="storeNoLocnoCondition" /></td>
								<td class="common-td blank">所属品牌：</td>
								<td colspan="3"><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" /></td>
							</tr>
						</table>
					</form>	
				</div>
			</div>
			
			<div  data-options="region:'center',border:false" >
				<@p.datagrid id="dataGridJG"  loadUrl=""  
					saveUrl=""  defaultColumn=""  title="库存转货单列表"
		               isHasToolBar="false" divToolbar="" height="410"  
		               onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true" emptyMsg ="" showFooter="true"
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'statusStr',title : '状态 ',align:'left',width : 50},
			                {field : 'ownerNo',title : '货主',align:'left',width : 80,align:'left',formatter:billconconvertgoods.ownerFormatter},
			                {field : 'convertGoodsNo',title : '转货单号',width : 140,sortable:true},
			                {field : 'convertTypeStr',title : '转货类型',width : 100},
			                {field : 'locnoName',title : '目的仓库',width : 140,align:'left'},
			                {field : 'storeName',title : '目的门店',width : 160,align:'left'},
			                {field : 'creator',title : '创建人',width : 80,align:'left'},
			                {field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			                {field : 'createtm',title : '创建时间',width : 130,sortable:true},
			                {field : 'editor',title : '编辑人',width : 80,align:'left'},
			                {field : 'editorName',title : '编辑人名称',width : 80,align:'left'},
			                {field : 'edittm',title : '编辑时间',width : 130,sortable:true},
			                {field : 'auditor',title : '审核人',width : 80,align:'left'},
			                {field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			                {field : 'audittm',title : '审核时间',width : 130,sortable:true},
			                {field : 'remark',title : '备注',width : 100,align:'left'},
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		    				// 触发点击方法  调JS方法
		        			billconconvertgoods.loadDetail(rowData);
				}}'/>
			</div>
		</div>
	</div>
	
	<#--新增div-->
	<div id="addUI" class="easyui-window" title="新增库存转货单"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true">
	    	
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
				
					<#-- 工具菜单div -->
		            <@p.toolbar id="opea-toolbar"  listData=[
				         {"id":"save_main","title":"保存","iconCls":"icon-save","action":"billconconvertgoods.doSaveMain()","type":1},
				         {"id":"edit_main","title":"修改","iconCls":"icon-edit","action":"billconconvertgoods.doEditMain()","type":2},
						 {"title":"取消","iconCls":"icon-cancel","action":"billconconvertgoods.closeWindow('addUI')","type":0}
						 ]
				  	/>
		            
		            <form name="dataForm" id="dataForm" method="post" class="city-form" style="padding:10px;">
		            	<input type = 'hidden' id = 'sQualityHid' name = "sQuality">
		            	<input type = 'hidden' id = 'dQualityHid' name = "dQuality">
			  			<table>
			  				<tr>
			  					<td class="common-td">单据编号：</td>
			  					<td><input class="easyui-validatebox ipt" style="width:120px" name="convertGoodsNo" id="convertGoodsNo"   readOnly="readOnly"/></td>
			  					<td class="common-td blank">货主：</td>
			  					<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo"  data-options="required:true" /></td>
			  					<td class="common-td blank">备注：</td>
								<td colspan="3"><input class="easyui-validatebox ipt" style="width:200px" name="remark" id="remark" /></td>
			  				</tr>
							<tr>
								<td class="common-td">转货类型：</td>
			  					<td>
			  						<input class="easyui-combobox ipt" style="width:120px" name="convertType" id="convertType"  
									data-options="required:true,panelHeight:50,editable:false,onChange:function(data){billconconvertgoods.convertTypeSH(data);}"/>
								</td>
			  					<td class="common-td blank" id="st1" style="display:none;">目的仓库：</td>
			  					<td id="s1"  style="display:none;"><input class="easyui-combobox" style="width:250px" name="storeNoLocno" id="storeNoLocno" /></td>
			  					<td class="common-td blank" id="st2" style="display:none;">目的门店：</td>
			  					<td id="s2"  style="display:none;"><input class="easyui-combobox" style="width:250px" name="storeNo" id="storeNo" /></td>
								<td class="common-td blank" id="qt1" >调整前品质：</td>
								<td id="q1" ><input class="easyui-combobox" data-options="editable:false" style="width:120px" id="sQuality"  /></td>
								<td class="common-td blank" id="qt2" >调整后品质：</td>
								<td id="q2" ><input class="easyui-combobox ipt" style="width:120px"  id="dQuality" /></td>
							</tr>	  				
			  			</table>
					</form>
				</div>
				
				<div data-options="region:'center'">
					<@p.toolbar id="opBtn"  listData=[
						{"id":"addDtlBtn","title":"新增明细","iconCls":"icon-add-dtl","action":"billconconvertgoods.openCheckSlect()","type":0},
						{"title":"删除明细","iconCls":"icon-del-dtl","action":"billconconvertgoods.delCheckSelect()","type":0}
				        {"title":"保存明细","iconCls":"icon-save-dtl","action":"billconconvertgoods.saveCheckSlect()","type":0}
						]
				  	/>
					<@p.datagrid id="convertGoodsDtlDg" name=""  loadUrl="" saveUrl="" defaultColumn=""  onClickRowAuto="false"
		 				isHasToolBar="false"  divToolbar="#opBtn"  height="400"  onClickRowEdit="false" 
		 				singleSelect="true"  pagination="true" rownumbers="true" emptyMsg="" title="转货单明细"
		 				checkOnSelect="false"  selectOnCheck="false"
		 				columnsJsonList="[
			 				{field : 'id',checkbox:true},
			 				{field:'locno',hidden:true},
			 				{field:'ownerNo',hidden:true},
			 			    {field:'checkNo',title:'验收单号',width:140},
			 				{field:'sourceNo',title:'来源单号',width:140},
			 				{field:'itemQty',title:'计划数量',width:80,align:'right',hidden:true},
						    {field:'realQty',title:'验收数量',width:80,align:'right'},
						    {field:'remark',title:'备注',width:150,align:'left'}
		 			 ]"
		 			jsonExtend='{}'/>
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
						 {"title":"取消","iconCls":"icon-cancel","action":"billconconvertgoods.closeWindow('detailUI')","type":0}
				       ]
					/>
					<#-- 工具菜单div -->
		            <form name="dataFormView" id="dataFormView" method="post" class="city-form" style="padding:10px;">
		            	<table>
			  				<tr>
			  					<td class="common-td">单据编号：</td>
			  					<td><input class="easyui-validatebox ipt" style="width:120px" name="convertGoodsNo" id="convertGoodsNoView"   readOnly="readOnly"/></td>
			  					<td class="common-td blank">货主：</td>
			  					<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNoView"  data-options="required:true" /></td>
			  					<td class="common-td blank">备注：</td>
								<td colspan="3"><input class="easyui-validatebox ipt" style="width:200px" name="remark" id="remarkView" /></td>
			  				</tr>
							<tr>
								<td class="common-td">转货类型：</td>
			  					<td>
			  						<input class="easyui-combobox ipt" style="width:120px" name="convertType" id="convertTypeView" />
								</td>
			  					<td class="common-td blank" id="v_st1" style="display:none;">目的仓库：</td>
			  					<td id="v_s1"  style="display:none;"><input class="easyui-combobox" style="width:250px" name="storeNoLocno" id="storeNoLocnoView" /></td>
			  					<td class="common-td blank" id="v_st2" style="display:none;">目的门店：</td>
			  					<td id="v_s2"  style="display:none;"><input class="easyui-combobox" style="width:250px" name="storeNo" id="storeNoView" /></td>
								<td class="common-td blank" id="v_qt1" >调整前品质：</td>
								<td id="v_q1" ><input class="easyui-combobox" data-options="editable:false" style="width:120px" id="sQualityView"  /></td>
								<td class="common-td blank" id="v_qt2" >调整后品质：</td>
								<td id="v_q2" ><input class="easyui-combobox ipt" style="width:120px"  id="dQualityView" /></td>
							</tr>  				
			  			</table>
					</form>
				</div>
				
				<#--查询start-->
				<div data-options="region:'center',border:false">
					<div id="main_order_dtlId" class="easyui-tabs" fit="true">
					
				    	<#-- 验收单主档 BEGIN -->
				    	<div id="tab_order_dtl1" title="验收单" height='100%'>
			    			<div class="easyui-layout" data-options="fit:true">
			    				<div data-options="region:'center',border:false">
			    					<@p.datagrid id="order_dtl1_dataGrid" defaultColumn=""  
										isHasToolBar="false" divToolbar="" height="295" pageSize="20" 
										onClickRowEdit="false" onClickRowAuto="false" pagination="true"
										rownumbers="true" emptyMsg="" showFooter="true"
										columnsJsonList="[
										    {field:'checkNo',title:'验收单号',width:140,sortable:true},
							 				{field:'sourceNo',title:'来源单号',width:140,sortable:true},
							 				{field:'itemQty',title:'计划数量',width:80,align:'right',hidden:true},
										    {field:'realQty',title:'验收数量',width:80,align:'right',sortable:true},
										    {field:'remark',title:'备注',width:150,align:'left',sortable:true}
									     ]" 
										jsonExtend='{}' />
				    			</div>
			    			</div>
			    		</div>
			    		<#-- 验收单主档 end -->
			    		
			    		<#-- 分货单差异 BEGIN -->
			    		<div id="tab_order_dtl2" title="验收单明细" height='100%'>
		    				<div class="easyui-layout" data-options="fit:true">
		    					<div data-options="region:'center',border:false">
		    						<@p.datagrid id="order_dtl2_dataGrid"    defaultColumn=""  showFooter="true" 
											isHasToolBar="false" divToolbar="#toolbarDlt2" height="349" pageSize="20" 
											onClickRowEdit="false" onClickRowAuto="false" pagination="true"
											rownumbers="true" emptyMsg=""
											columnsJsonList="[
												{field:'convertGoodsNo',title:'转货单号',width:130,align:'left',sortable:true},
												{field:'checkNo',title:'验收单号',width:130,align:'left',sortable:true},
												{field:'sourceNo',title:'来源单号',width:130,align:'left',sortable:true},
								 				{field:'itemNo',title:'商品编码',width:150,align:'left',sortable:true},
								 				{field:'itemName',title:'商品名称',width:150,align:'left',sortable:true},
											    {field:'sizeNo',title:'尺码',width:80,align:'left',sortable:true},
											    {field:'colorName',title:'颜色',width:80,align:'left',sortable:true},
											    {field:'brandName',title:'品牌',width:80,align:'left',sortable:true},
											    {field:'boxNo',title:'箱号',width:100,align:'left',sortable:true},
											    {field:'itemQty',title:'计划数量',width:80,align:'right',sortable:true,hidden:true},
											    {field:'realQty',title:'验收数量',align:'right',width:80,sortable:true}
									         ]"
										rownumbers="true" jsonExtend="{}" />
		    					</div>
		    				</div>
			    		</div>
			    		<#-- 分货单差异 end -->
			    	</div>
				</div>
			<#--查询end-->	
		
		</div>
	</div>          
	
	
	<#--验收单选择 -->
	<div id="checkUI" class="easyui-window"  title="验收单选择"
	    data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	    	
	    	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<#-- 工具菜单div -->
		            <@p.toolbar id="checktoolbar"  listData=[
		            	{"title":"查询","iconCls":"icon-search","action":"billconconvertgoods.searchCheckData()", "type":0},
		 				{"title":"清除","iconCls":"icon-remove","action":"billconconvertgoods.searchCheckClear('searchCheckForm')", "type":0},
		            	{"title":"确定","iconCls":"icon-ok","action":"billconconvertgoods.selectCheck()", "type":0},
		                {"title":"取消","iconCls":"icon-cancel","action":"billconconvertgoods.closeWindow('checkUI')","type":0}
		            ]/>
		            
		            <form name="searchCheckForm" id="searchCheckForm" method="post" class="city-form">
						<table>
							<tr>
								<td class="common-td">验收单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo" id="checkNoCheckSearch" /></td>
								<td class="common-td blank">店退仓单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo" id="untreadNoCheckSearch" /></td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="ownerNo" id="ownerNoCheckSearch" /></td>
								<td class="common-td blank" >商品类型：</td>
			   					<td><input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemTypeCheckSearch"  style="width:120px"/></td>
							</tr>
							<tr>
								<td class="common-td">品质：</td>
								<td><input class="easyui-combobox" name="quality" id="qualityCheckSearch" style="width:120px;"/></td>
								<td class="common-td blank">创建人：</td>
								<td><input class="combobox ipt" style="width:120px" name="creator" id="creatorCheckSearch" /></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="startCreatetmCheckSearch" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="createtmEnd" id="endCreatetmCheckSearch" 
									data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>								
							<tr>
								<td class="common-td"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoCheckSearch"/></td>
								<td class="common-td blank">审核人：</td>
								<td><input class="combobox ipt" style="width:120px" name="auditor" id="auditorCheckSearch" /></td>
								<td class="common-td blank">审核日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="startAudittmCheckSearch" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="audittmEnd" id="endAudittmCheckSearch" 
									data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>
							<tr>
		             			<td class="common-td blank">所属品牌：</td>
								<td colspan="7"><input class="easyui-combobox ipt" style="width:320px" name="brandNo" id="brandNoCheckSearch" /></td>
							</tr>
						</table>
					</form>
		        </div>
		        
		        <div data-options="region:'center',border:false">
		        	<#-- 验收单选择数据列表div -->
          	  		<@p.datagrid id="dataGridCheck"  loadUrl=""  saveUrl=""  defaultColumn="" 
		               isHasToolBar="false" divToolbar="" width="726" height="325"  onClickRowEdit="false" 
		               singleSelect="true" pageSize='20'  title="验收单列表" 
					   pagination="true" rownumbers="true"
			           columnsJsonList="[
			          		{field : 'id',checkbox:true},
			 			    {field:'checkNo',title:'验收单号',width:140,sortable:true},
			 				{field:'sourceNo',title:'来源单号',width:140,sortable:true},
			 				{field:'itemTypeStr',title:'商品类型',width:80,align:'left',sortable:true},
			 				{field:'qualityStr',title:'品质',width:80,align:'left',sortable:true},
			 				{field:'itemQty',title:'计划数量',width:80,align:'right',sortable:true},
						    {field:'checkQty',title:'验收数量',width:80,align:'right',sortable:true},
						    {field:'remark',title:'备注',width:150,align:'left',sortable:true}
			        	]"
			        />
		        </div>
		    </div>
	</div>
  

</body>
</html>