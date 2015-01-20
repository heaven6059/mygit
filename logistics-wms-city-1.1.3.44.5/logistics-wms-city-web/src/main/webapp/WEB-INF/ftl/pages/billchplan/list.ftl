<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>盘点计划单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billchplan/billchplan.js?version=1.1.1.5"></script>
</head>
<body class="easyui-layout">

			
	<#-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
			<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"billchplan.searchData()", "type":0},
	            {"title":"清除","iconCls":"icon-remove","action":"billchplan.searchClear()", "type":0},
				{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"billchplan.showAddDialog()","type":1},
				{"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billchplan.editInfo()","type":2},
				{"title":"删除","iconCls":"icon-del","action":"billchplan.deletePlan()","type":3},
				{"id":"btn-check","title":"审核","iconCls":"icon-aduit","action":"billchplan.check()","type":4},
				{"id":"btn-close","title":"作废","iconCls":"icon-close","action":"billchplan.invalid()","type":4}
				{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('盘点计划单')","type":0}
			 ]/>
	</div>
		
	<#--divToolbar有值时，isHasToolBar不能为 true loadUrl有值时，请求页面后，就会从远程站点url请求数据，填充到数据表格 -->
	<#-- onDblClickRow-表格双击事件，加载数据 -->
			
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<!--搜索start-->
			<div  data-options="region:'north',border:false" >
				 <div class="search-div">
				 	<form name="searchForm" id="searchForm" method="post" class="city-form">
				 		<table>
                     		<tr>
                     			<td class="common-td blank">状态：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
                     			<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
                     			<td class="common-td blank">创建日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="startCreatetmCondition" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="endCreatetmCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank">单据编号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo" id="planNoCondition" /></td>	
                     			<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
                     			<td class="common-td blank">审核日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="startAudittmCondition" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="audittmEnd" id="endAudittmCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td">盘点类型：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="planType" id="planTypeCondition" data-options="editable:false"/></td>
                     			<td class="common-td">盘点方式：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="limitBrandFlag" id="limitBrandFlagCondition" data-options="editable:false"/></td>
                     			<td class="common-td blank">盘点日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="planDateStart" id="startPlanDateCondition" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="planDateEnd" id="endPlanDateCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
                     			<td class="common-td blank">所属品牌：</td>
                     			<td colspan='3'><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
                     		</tr>
                     	</table>
				 	</form>	
				 </div>
			</div>
			<!--显示列表-->
            <div data-options="region:'center',border:false">
            	<@p.datagrid id="mainDataGrid"  saveUrl=""  loadUrl="" defaultColumn=""  title="盘点计划单列表"
					isHasToolBar="false"  divToolbar="#locSearchDiv" height="450"  onClickRowEdit="false" singleSelect="false" pageSize="20"
					rownumbers="true" emptyMsg=""
					columnsJsonList="[
						{field : 'id',checkbox :true},	
						{field : 'status',title : '状态',width : 100,formatter:billchplan.statusFormatter,align:'left'},	
						{field : 'planNo',title : '单据编号',width : 180},
						{field : 'planType',title : '盘点类型',width : 100,formatter:billchplan.planTypeFormatter,align:'left'},
						{field : 'limitBrandFlag',title : '盘点方式',width : 100,formatter:billchplan.limitBrandFlagFormatter,align:'left'},
						{field : 'planDate',title : '盘点日期',width : 100},
						{field : 'itemType',title : '商品属性   ',width : 100,align:'left',formatter:wms_city_common.columnItemTypeFormatter},
			            {field : 'quality',title : '商品品质',width : 80,align:'left',formatter:wms_city_common.columnQualityFormatter},
			            {field : 'creator',title : '创建人',width : 80,align:'left'},
						{field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130,sortable:true},
						{field : 'auditor',title : '审核人',width : 80,align:'left'},
						{field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
						{field : 'audittm',title : '审核时间',width : 130},
						{field : 'planRemark',title : '备注',width : 120,align:'left'}
						]" 
						jsonExtend='{onDblClickRow:function(rowIndex, rowData){
						    billchplan.loadDetail(rowData);
						},
						queryParams:{locno:"${session_user.locNo}"}}'/>
            </div>
		</div>
	</div>
	
	<!-- 盘点计划单明细窗口S -->
	<div id="showDetailDialog"   class="easyui-dialog" title="计划明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'center',border:false">
		    		<div id="main_order_dtlId" class="easyui-tabs" fit="true">
		    			<!-- 盘点信息S -->
			    		<div id="tab_order_dtl1" title="盘点信息" height='100%'>
			    			<div class="easyui-layout" data-options="fit:true">
			    				<div data-options="region:'north',border:false">
			    					<@p.toolbar id="addtoolbar"   listData=[
				                         {"id":"saveBtn","title":"保存","iconCls":"icon-save","action":"billchplan.saveMainInfo()", "type":1},
				                         {"id":"info_save","title":"修改","iconCls":"icon-edit","action":"billchplan.editMain();", "type":2},
				                         {"title":"取消","iconCls":"icon-cancel","action":"billchplan.closeWindow('showDetailDialog')", "type":0}
				                       ]
									/>
									<div class="search-div">
						         		<form name="divideMainInfoForm" id="divideMainInfoForm" method="post" class="city-form">
						         			<table>
						                 		<tr>
						                 			<td class="common-td blank">单据编号：</td>
						                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo" id="planNo" readOnly="true" /></td>
						                 			<td class="common-td blank">盘点类型：</td>
						                 			<td><input class="easyui-combobox" style="width:120px" name="planType" id="planType" 
															data-options="
																		required:true,
																		panelHeight:50,
																		editable:false,
																		onChange:function(){
																			billchplan.planTypeChange();
																		}
																		"/>
													</td>
						                 			<td class="common-td blank" id="limitBrandFlagTitle">盘点方式：</td>
						                 			<td  id="limitBrandFlagValue"><input class="easyui-combobox" style="width:120px" name="limitBrandFlag" id="limitBrandFlag" data-options="
													                 						editable:false,
													                 						onChange:function(){
																								billchplan.limitBrandFlagChange();
																							}
																					"/>
													</td>
						                 			<td class="common-td blank"  id="sysNoTitle">品牌库：</td>
						                 			<td id="sysNoValue"><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoForItem"/></td>
						                 			<td class="common-td blank"  id="brandNoTitle">品牌：</td>
						                 			<td id="brandNoValue"><input class="easyui-combobox" style="width:120px" name="brandNo" id="brandNoForItem"/></td>
						                 		</tr>
						                 		<tr>
						                 			<td class="common-td blank">盘点日期：</td>
						                 			<td><input class="easyui-datebox" style="width:120px" name="planDate" id="planDate" data-options="required:true"/></td>
						                 			<td class="common-td blank"  id="itemTypeTitle">商品属性：</td>
						                 			<td id="itemTypeValue"><input class="easyui-combobox" style="width:120px" name="itemType" id="itemType" data-options="editable:false"/></td>
						                 			<td class="common-td blank" id="qualityTitle">商品品质：</td>
						                 			<td  id="qualityValue"><input class="easyui-combobox" style="width:120px" name="quality" id="quality" data-options="editable:false"/></td>
						                 			<td class="common-td blank">备注：</td>
						                 			<td ><input class="easyui-validatebox ipt" style="width:120px" name="planRemark" id="planRemark"/></td>
						                 			<td ><input type="hidden" id="option"/></td>
						                 		</tr>
						                    </table>
						         		</form>
							         </div>
			    				</div>
			    				<div data-options="region:'center',border:false">
					    			<!-- 商品盘明细S -->
				    				 <div class="easyui-layout" data-options="fit:true" id="item_div">
										<div data-options="region:'center',border:false">
											<@p.toolbar id="toolbarDlt_item" listData=[
												{"title":"新增","iconCls":"icon-add","action":"billchplan.addrow()", "type":0}
												{"title":"删除","iconCls":"icon-remove","action":"billchplan.removeBySelected('dtl_item_dataGrid')", "type":0}
												{"title":"保存明细","iconCls":"icon-save","action":"billchplan.saveDtlInfo()", "type":0}
				                       		]/>
						    				<@p.datagrid id="dtl_item_dataGrid" defaultColumn=""  title="计划明细列表-商品"
												isHasToolBar="false" divToolbar="#toolbarDlt_item"    pageSize="20" 
												onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect="true"
												rownumbers="true"  emptyMsg=""
												columnsJsonList="[    
													  {field:'itemNo',title:'商品编码',width:150,align:'left'},    
													  {field:'itemName',title:'商品名称',width:150,align:'left'},    
													  {field:'colorName',title:'颜色',width:100,align:'left'},    
													  {field:'sizeNo',title:'尺码',width:100},    
													  {field:'brandNo',title:'品牌',hidden:true},    
													  {field:'brandName',title:'品牌',width:100,align:'left'}]" 
												jsonExtend="{}" />
				    				 	</div>
				    				 </div>
				    				 <!-- 商品盘明细E -->
				    				 <!-- 储位盘明细S -->
				    				 <div class="easyui-layout" data-options="fit:true" id="cell_div">
										<div data-options="region:'center',border:false">
											<@p.toolbar id="toolbarDlt_cell" listData=[
												{"title":"新增","iconCls":"icon-add","action":"billchplan.addrow()", "type":0}
												{"title":"删除","iconCls":"icon-remove","action":"billchplan.removeBySelected('dtl_cell_dataGrid')", "type":0}
												{"title":"保存明细","iconCls":"icon-save","action":"billchplan.saveDtlInfo()", "type":0}
				                       		]/>
						    				<@p.datagrid id="dtl_cell_dataGrid" defaultColumn=""  title="计划明细列表-储位"
												isHasToolBar="false" divToolbar="#toolbarDlt_cell" height="330"    pageSize="20" 
												onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect="true"
												rownumbers="true"  emptyMsg=""
												columnsJsonList="[{field:'cellNo',title:'储位',width:100,align:'left'},    
						    				          {field:'stockNo',title:'通道',width:100,align:'left'},    
						    				          {field:'areaNo',title:'库区',width:100,align:'left'},    
						    				          {field:'areaName',title:'库区名称',width:100,align:'left'},    
						    				          {field:'wareNo',title:'仓区',width:100,align:'left'},    
						    				          {field:'wareName',title:'仓区名称',width:100,align:'left'}]" 
												jsonExtend="{}" />
				    				 	</div>
				    				 </div>
				    				 <!-- 储位盘明细E -->
								</div>
			    			</div>
			    		</div>
			    		<!-- 差异信息S -->
			    		<div id="tab_order_dtl2" title="差异信息" height='100%'>
							<div class="easyui-layout" data-options="fit:true">
			    				<div data-options="region:'north',border:false">
			    					<div class="search-div">
						         		<form name="diffMainForm" id="diffMainForm" method="post" class="city-form">
						         			<table>
						                 		<tr>
						                 			<td class="common-td">计划单号：</td>
						                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo" id="planNoDiff" readOnly="true" /></td>
						                 			<td class="common-td blank">差异单号：</td>
						                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="differentNo" id="differentNoDiff" readOnly="true" /></td>
						                 			<td class="common-td blank">开始日期：</td>
						                 			<td><input class="easyui-datebox" style="width:120px" name="beginDate" id="beginDateDiff" data-options="required:true"/></td>
						                 			<td class="common-td blank">结束日期：</td>
						                 			<td><input class="easyui-datebox" style="width:120px" name="endDate" id="endDateDiff" data-options="required:true"/></td>
						                 		</tr>
						                 		<tr>
						                 			<td class="common-td">盘点类型：</td>
						                 			<td><input class="easyui-combobox" style="width:120px" name="planType" id="planTypeDiff"/></td>
						                 			<td class="common-td">备注：</td>
						                 			<td colspan="5"><input class="easyui-validatebox ipt" style="width:501px" name="planRemark" id="planRemarkDiff"/></td>
						                 		</tr>
						                    </table>
						         		</form>
							         </div>
			    				</div>
			    				<div data-options="region:'center',border:false">
			    					<@p.datagrid id="dtl_diff_dataGrid" defaultColumn=""  title="差异明细"
										isHasToolBar="false" divToolbar="" height="330"    pageSize="20" 
										onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect="true"
										rownumbers="true"  emptyMsg="" showFooter="true"
										columnsJsonList="[
												{field:'cellNo',title:'储位',width:100,align:'left'},    
				    				          	{field:'itemNo',title:'商品编码',width:150,align:'left'},    
				    				          	{field:'itemName',title:'商品名称',width:150,align:'left'},    
				    				          	{field:'colorName',title:'颜色',width:100,align:'left'},    
				    				          	{field:'sizeNo',title:'尺码',width:100},    
				    				          	{field:'itemQty',title:'计划数量',width:100,align:'right'},    
				    				          	{field:'realQty',title:'实盘数量',width:100,align:'right'},    
				    				          	{field:'diffQty',title:'差异数量',width:100,align:'right'},    
				    				          	{field:'brandName',title:'品牌',width:100,align:'left'}
				    				          ]" 
										jsonExtend="{}" />
			    				</div>
			    			</div>
						</div>
			    	</div>
			    </div>
		    </div>
	</div>
	<!-- 盘点计划单明细窗口E -->
	<!-- 商品选择S -->
	<div id="item_select_dialog"  class="easyui-dialog" title="商品选择"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<!--<div data-options="region:'west',title:'品牌列表'" style="width:170px">
				 	<ul id="mainTree" class="easyui-tree" height="300">   
				 	</ul>
				</div>-->
				
		    	<div data-options="region:'north',border:false">
    				<@p.toolbar id="itemtoolbar" listData=[
	                             {"title":"查询","iconCls":"icon-search","action":"billchplan.searchItem()", "type":0},
	                             {"title":"清除","iconCls":"icon-remove","action":"billchplan.clearItem()", "type":0},
	                             {"title":"确认","iconCls":"icon-ok","action":"billchplan.itemSelectOK()","type":0}
	                             {"title":"取消","iconCls":"icon-cancel","action":"billchplan.closeWindow('item_select_dialog')","type":0}
		                       ]
					  />
    				<div class="search-div">
		         		<form name="selectItemSearchForm" id="selectItemSearchForm" method="post" class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td blank">商品编码：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo" /></td>
		                 			<td class="common-td blank">大类一：</td>
			             			<td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneItem" /></td>
			             			<td class="common-td blank">大类二：</td>
			             			<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoItem" /></td>
			             			<td class="common-td blank">大类三：</td>
			             			<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeItem" /></td>
		                 		</tr>
		                 		<tr>
	                     			<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
			             			<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoItem"/></td>
	                     			<td class="common-td blank">季节：</td>
	                     			<td ><input class="easyui-combobox ipt" style="width:120px" name="seasonStr" id="seasonItem" /></td>
	                     			<td class="common-td blank">性别：</td>
	                     			<td ><input class="easyui-combobox ipt" style="width:120px" name="genderStr" id="genderItem" /></td>
	                     			<td class="common-td blank">年份：</td>
	                     			<td ><input class="easyui-validatebox ipt" style="width:120px" name="yearsStr" id="yearsItem" /></td>
		                 		</tr>
		                 		<tr>
			             			<td class="common-td blank">所属品牌：</td>
	                     			<td colspan="3"><input class="easyui-combobox ipt" style="width:298px" name="brandNo" id="brandNoItem" /></td>		
	                     			<td class="common-td blank"></td>
	                     			<td ></td>
	                     			<td class="common-td blank"></td>
	                     			<td ></td>
		                 		</tr>
		                    </table>
		         		</form>
		         	</div>
		    	</div>
		    	<div data-options="region:'center',border:false">
    				<@p.datagrid id="item_select_datagrid"  loadUrl=""  saveUrl=""  defaultColumn="" height="425" title="商品列表"
		               isHasToolBar="false" divToolbar=""  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true"
			           columnsJsonList="[
			           		{field:'id',checkbox:true},
			           		{field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
			                {field : 'itemName',title : '商品名称',width : 150,align:'left'},
			                {field : 'colorNoStr',title : '颜色 ',width : 80,align:'left'},
			                {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
			                {field : 'yearsStr',title : '年份',width : 80,align:'left'},
			                {field : 'seasonStr',title : '季节',width : 80,align:'left'},
			                {field : 'genderStr',title : '性别',width : 80,align:'left'}
			               
			                
			            ]"/>
    			</div>
		    </div>
	</div>
	<!-- 商品选择E -->
	<!-- 储位选择S -->
	<div id="cell_select_dialog"   class="easyui-dialog" title="储位选择"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
                     <div data-options="region:'north',border:false">
                     		<@p.toolbar id="celltoolbar" listData=[
			                             {"title":"查询","iconCls":"icon-search","action":"billchplan.searchCell()", "type":0},
			                             {"title":"清除","iconCls":"icon-remove","action":"billchplan.clearForm('selectCellSearchForm')", "type":0},
			                             {"title":"确认","iconCls":"icon-ok","action":"billchplan.cellSelectOK()","type":0}
			                             {"title":"整库区","iconCls":"icon-ok","action":"billchplan.cellSelectOKByAreaNo()","type":0}
			                             {"title":"整通道","iconCls":"icon-ok","action":"billchplan.cellSelectOKByStockNo()","type":0}
			                             {"title":"取消","iconCls":"icon-cancel","action":"billchplan.closeWindow('cell_select_dialog')","type":0}
				                       ]
							  />
							<div class="search-div">
				         		<form name="selectCellSearchForm" id="selectCellSearchForm" method="post" class="city-form">
				         			<table>
				                 		<tr>
				                 			<td class="common-td">仓区：</td>
				                 			<td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNo" data-options="
																		onChange:function(){
																			billchplan.selectWareNo();
																		}
																		"/>
												</td>
				                 			<td class="common-td blank">库区：</td>
				                 			<td><input class="easyui-combobox" style="width:120px" name="areaNo" id="areaNo" data-options="
																		onChange:function(){
																			billchplan.selectAreaNo();
																		}
												"/>
											</td>
				                 			<td class="common-td blank">通道：</td>
				                 			<td><input class="easyui-combobox" style="width:120px" name="stockNo" id="stockNo"/>
											</td>
				                 			<td class="common-td blank">储位：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="cellNo" id="cellNo" /></td>
				                 		</tr>
				                    </table>
				         		</form>
				         	</div>
                     </div>	
                     <div data-options="region:'center',border:false">
	                     <@p.datagrid id="cell_select_datagrid"  loadUrl=""  saveUrl=""  defaultColumn="" height="300"
			               isHasToolBar="false" divToolbar=""  onClickRowEdit="false"  pagination="true" singleSelect = "false"
				           rownumbers="true"
				           columnsJsonList="[
				           		{field:'id',checkbox:true},
				           		{field : 'cellNo',title : '储位 ',width : 100,align:'left'},
				                {field : 'stockNo',title : '通道',width : 100,align:'left'},
				                {field : 'areaNo',title : '库区',width : 100,align:'left'},
				                {field : 'wareNo',title : '仓区 ',width : 90,align:'left'}
				            ]"/>
			         </div>
		    </div>
	</div>
	<!-- 储位选择E -->
</body>
</html>