<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>自由上架</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/immediatemovestock/immediatemovestock.js?version=1.1.1.1"></script>
</head>
<body class="easyui-layout">

	<#-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
			<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"immediatemovestock.searchData()", "type":0},
	            {"title":"清除","iconCls":"icon-remove","action":"immediatemovestock.searchClear('searchForm')", "type":0},
				{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"immediatemovestock.showAddDialog()","type":1},
				{"id":"btn-add","title":"确认","iconCls":"icon-ok","action":"immediatemovestock.queryBill()","type":1},
				{"id":"btn-cancel","title":"关闭","iconCls":"icon-cancel","action":"closeWindow()","type":0}
			 ]/>
	</div>
			
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<!--搜索start-->
			<div  data-options="region:'north',border:false" >
				 <div class="search-div">
				 	<form name="searchForm" id="searchForm" method="post" class="city-form">
				 		<table>
                     		<tr>
                     			<td class="common-td">状态：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
                     			<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
                     			<td><input class="combobox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
                     			<td class="common-td blank">创建日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createTmStart" id="createTmStartCondition" /></td>
                     			<td class="common-line" width='40'>&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createTmEnd" id="createTmEndCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td">单据编号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="outstockNo" id="outstockNoCondition" /></td>	
                     			<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
                     			<td><input class="combobox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
                     			<td class="common-td blank">审核日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittmCondition" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittmCondition" /></td>
                     		</tr>
                     		
                     		<tr>
                     			<td class="common-td"> 品&nbsp;牌&nbsp;库：</td>
		             			<td>
		             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/>
		             			</td>
                     			<td class="common-td blank">所属品牌：</td>
                     			<td colspan='5'><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" /></td>
                     		</tr>
                     		
                     	</table>
				 	</form>	
				 </div>
			</div>
			
			<!--显示列表-->
            <div data-options="region:'center',border:false">
            	<@p.datagrid id="mainDataGrid"  saveUrl=""  loadUrl="" defaultColumn=""  title="自由上架列表"
					isHasToolBar="false"  divToolbar="#" height="450"  onClickRowEdit="false" 
					singleSelect="false" pageSize="20"
					rownumbers="true" emptyMsg="" showFooter="true"
					columnsJsonList="[
						{field : 'id',checkbox :true},	
						{field : 'statusStr',title : '状态',width : 80,align:'left'},	
						{field : 'outstockNo',title : '单据编号',width : 140},
						{field : 'itemQty',title : '上架数量',width : 100,align:'right'},
						{field : 'creator',title : '创建人',width : 80,align:'left'},
						{field : 'creatorname',title : '创建人名称',width : 80,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130,sortable:true},
						{field : 'auditor',title : '审核人',width : 80,align:'left'},
						{field : 'auditorname',title : '审核人名称',width : 80,align:'left'},
						{field : 'audittm',title : '审核时间',width : 130},
						{field : 'editor',title : '更新人',width : 100,sortable:true},
						{field : 'editorname',title : '更新人名称',width : 100,sortable:true},
						{field : 'edittm',title : '更新时间',width : 130,sortable:true}
						]" 
						jsonExtend='{onDblClickRow:function(rowIndex, rowData){
			        		// 触发点击方法  调JS方法
			        		immediatemovestock.loadView(rowData);
			     		}}'/>
            </div>
		</div>
	</div>
	
	
	<#-- 新增修改页面 BEGIN -->
	<div id="openUI"  class="easyui-dialog" title="新增"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="addtoolbar"   listData=[
		        	{"id":"save_main_info","title":"保存","iconCls":"icon-save","action":"immediatemovestock.saveMainInfo();", "type":1},
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"immediatemovestock.closeUI('openUI');","type":0}
		        ]/>
			</div>
			
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false" id='detailDiv'>
				<@p.toolbar id="contentToolDiv" listData=[
		        	{"id":"add_row","title":"新增明细","iconCls":"icon-add-dtl","action":"immediatemovestock.openConContentSelect();", "type":0},
		        	{"id":"del_row","title":"批储位添加","iconCls":"icon-add-dtl","action":"immediatemovestock.openBatchAddCell();", "type":0},
		        	{"id":"del_row","title":"删除明细","iconCls":"icon-del-dtl","action":"immediatemovestock.delMoveStockDtl();", "type":0}
		        ]/>
		        
				<@p.datagrid id="moveStockDetail" name="" title="自由上架明细" loadUrl="" saveUrl="" defaultColumn="" 
					 isHasToolBar="false"  divToolbar="#contentToolDiv" onClickRowEdit="true" singleSelect="true"   
					 pagination="true" rownumbers="true" onClickRowAuto="true" emptyMsg="" 
					 columnsJsonList="[
					 	{field : 'id',checkbox :true},	
						{field : 'rowId',hidden:true},
						{field : 'storelockNo',hidden:true},
					    {field : 'sCellNo',title : '储位 ',width : 110,align:'left'},
					    {field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
					    {field : 'itemName',title : '商品名称',width : 160,align:'left'},
					    {field : 'colorName',title : '颜色 ',width : 80,align:'left'},
					    {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
					    {field : 'barcode',title : '条码',width : 150,align:'left'},
					    {field : 'goodContentQty',title : '可上架数量',width : 80,align:'right'},
					    {field : 'dCellNo',title : '目的储位',width : 80,align:'right',
					    	editor:{
					        	type:'validatebox'
					        }
					    },
					    {field : 'itemQty',title : '实际上架数量',width : 90,align:'right',
					    	editor:{
					        	type:'numberbox'
					        }
					    }
					]"
					jsonExtend='{}'
				/>
			</div>
			<#--显示列表end-->
		</div>
	</div>
	<#-- 新增修改页面 END -->
	
	
	<#-- 查看明细信息div BEGIN -->
	<div id="openDtlUI"  class="easyui-dialog" title="明细"
		 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		 maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="addtoolbarview"   listData=[
		        	{"id":"btn-canel-view","title":"取消","iconCls":"icon-cancel","action":"immediatemovestock.closeUI('openDtlUI');","type":0}
		        ]/>
				<div nowrap class="search-div" style="padding:10px;">
					<form name="dataViewForm" id="dataViewForm" metdod="post" class="city-form">
						<input type="hidden" id="opt"/>
						<table>
							<tr>
						    	<td class="common-td blank">单据编号：</td>
						        <td><input class="easyui-validatebox ipt" style="width:120px" name="outstockNo" id="outstockNoView" readOnly="readOnly"/></td>
						    </tr>
						 </table>
					</form>
				</div>
			</div>
			
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false" id='detailDivView'>
				<@p.datagrid id="moveStockDetailView" name="" title="自由上架明细" loadUrl="" saveUrl="" defaultColumn="" 
					 isHasToolBar="false"  divToolbar="" onClickRowEdit="true" singleSelect="true"   
					 pagination="true" rownumbers="true" emptyMsg="" showFooter="true"
					 columnsJsonList="[
						{field : 'rowId',hidden:true},
					    {field : 'sCellNo',title : '来源储位 ',width : 110,align:'left'},
					    {field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
					    {field : 'itemName',title : '商品名称',width : 160,align:'left'},
					    {field : 'colorName',title : '颜色 ',width : 80,align:'left'},
					    {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
					    {field : 'barcode',title : '条码',width : 150,align:'left'},
					    {field : 'scanLabelNo',title : '箱号',width : 100,align:'left'},
					    {field : 'panNo',title : '父容器号',width : 100,align:'left'},
					    {field : 'dCellNo',title : '目的储位 ',width : 110,align:'left'},
					    {field : 'goodContentQty',title : '可上架数量',width : 80,align:'right'},
					    {field : 'itemQty',title : '实际上架数量',width : 90,align:'right'}
					]"
				/>
			</div>
			<#--显示列表end-->
		</div>
	</div>
	<#-- 查看明细信息div END -->
	
	
	
	<#-- 库存选择S -->
	<div id="content_select_dialog"  id="showDialog"  class="easyui-dialog" title="库存查询"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'center',border:false">
		    		<div class="easyui-layout" data-options="fit:true">
		    			<div data-options="region:'north',border:false">
		    				<@p.toolbar id="itemtoolbar" listData=[
			                             {"title":"查询","iconCls":"icon-search","action":"immediatemovestock.searchConContent()", "type":0},
			                             {"title":"清除","iconCls":"icon-remove","action":"immediatemovestock.searchConContentSearchClear()", "type":0},
			                             {"title":"确认","iconCls":"icon-ok","action":"immediatemovestock.contentSelectOK()","type":0}
			                             {"title":"取消","iconCls":"icon-cancel","action":"immediatemovestock.closeUI('content_select_dialog')","type":0}
				                       ]
							  />
		    				<div class="search-div">
				         		<form name="selectContentSearchForm" id="selectContentSearchForm" method="post" class="city-form">
				         			<table>
				                 		<tr>
				                 			<td class="common-td blank">仓区：</td>
				                     		<td><input class="easyui-combobox ipt" style="width:120px" name="wareNo" id="wareNo" /></td>
				                 			<td class="common-td blank">库区：</td>
				                     		<td><input class="easyui-combobox ipt" style="width:120px" name="areaNo" id="areaNo" /></td>
				                 			<td class="common-td blank">通道：</td>
				                     		<td><input class="easyui-combobox ipt" style="width:120px" name="stockNo" id="stockNo" /></td>
											<td class="common-td blank">储位：</td>
											<td><input class="easyui-combobox ipt" style="width:120px" name="cellNo" id="cellNo" /></td>
											
				                 		</tr>
				                 		<tr>
							            	<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
					             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch2"/></td>
											<td class="common-td blank">季节：</td>
				                     		<td><input class="easyui-combobox ipt" style="width:120px" name="season" id="seasonCondition" /></td>
			                     			<td class="common-td blank">性别：</td>
								            <td><input class="easyui-combobox ipt" style="width:120px" name="gender" id="genderCondition" /></td>
								            <td class="common-td blank">年份：</td>
											<td><input class="easyui-validatebox ipt" style="width:120px" name="years" id="yearsCondition" /></td>
							            </tr>
				                 		<tr>
				                 			<td class="common-td blank">所属品牌：</td>
			                     			<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo2" /></td>
			                     			<td class="common-td blank">大类一：</td>
			            					<td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
			            					<td class="common-td blank">大类二：</td>
			            					<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
			            					<td class="common-td blank">大类三：</td>
			            					<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
							            </tr>
							            <tr>
							                <td class="common-td blank">商品编码：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo" /></td>
				                 			<td class="common-td blank">商品条码：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcode" /></td>
				                 			<td class="common-td blank"></td>
				                 			<td></td>
				                 			<td class="common-td blank"></td>
				                 			<td></td>
							            </tr>
				                    </table>
				         		</form>
				         	</div>
		    			</div>
		    			<div data-options="region:'center',border:false">
		    				<@p.datagrid id="content_select_datagrid"    defaultColumn=""  
							 isHasToolBar="false" divToolbar="" height="500"  pageSize="20" title="库存列表"
							 onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect = "true"
							 rownumbers="true" checkOnSelect="false" emptyMsg="" 
				               columnsJsonList="[
					           		{field:'ck',checkbox:true},
					           		{field : 'sCellNo',title : '储位 ',width : 100,align:'left'},
					           		{field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
					                {field : 'itemName',title : '商品名称',width : 160,align:'left'},
					                {field : 'colorName',title : '颜色 ',width : 80,align:'left'},
					                {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
					                {field : 'barcode',title : '条码',width : 140,align:'left'},
					                {field : 'goodContentQty',title : '可分配数量',width : 80,align:'right'}
					            ]"/>
		    			</div>
		    		</div>
		    	</div>
		    </div>
	</div>
	<#-- 商品选择E -->
	
	<#-- 储位选择 S -->
	<#-- 库存选择S -->
	<div id="cell_select_dialog"  class="easyui-dialog" title="储位查询"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'center',border:false">
		    		<div class="easyui-layout" data-options="fit:true">
		    			<div data-options="region:'north',border:false">
		    				<@p.toolbar id="celltoolbar" listData=[
			                             {"title":"查询","iconCls":"icon-search","action":"immediatemovestock.searchCell()", "type":0},
			                             {"title":"清除","iconCls":"icon-remove","action":"immediatemovestock.searchConContentClear('selectCellSearchForm')", "type":0},
			                             {"title":"确认","iconCls":"icon-ok","action":"immediatemovestock.cellSelectOK()","type":0}
			                             {"title":"取消","iconCls":"icon-cancel","action":"immediatemovestock.closeUI('cell_select_dialog')","type":0}
				                       ]
							  />
		    				<div class="search-div">
				         		<form name="selectCellSearchForm" id="selectCellSearchForm" method="post" class="city-form">
				         			<table>
				                 		<tr>
				                 			<td class="common-td">仓区：</td>
				                     		<td><input class="easyui-combobox ipt" style="width:120px" name="wareNo" id="wareNo4SelectCell" /></td>
				                 			<td class="common-td blank">库区：</td>
				                     		<td><input class="easyui-combobox ipt" style="width:120px" name="areaNo" id="areaNo4SelectCell" /></td>
				                 			<td class="common-td blank">通道：</td>
				                     		<td><input class="easyui-combobox ipt" style="width:120px" name="stockNo" id="stockNo4SelectCell" /></td>
				                 		</tr>
				                    </table>
				         		</form>
				         	</div>
		    			</div>
		    			<div data-options="region:'center',border:false">
		    				<@p.datagrid id="cell_select_datagrid"    defaultColumn=""  
							 isHasToolBar="false" divToolbar="" height="500"  pageSize="20" title="储位列表"
							 onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect = "true"
							 rownumbers="true" checkOnSelect="false" emptyMsg="" 
				               columnsJsonList="[
					           		{field:'ck',checkbox:true},
					           		{field : 'cellNo',title : '储位 ',width : 100,align:'left'}
					            ]"/>
		    			</div>
		    		</div>
		    	</div>
		    </div>
	</div>
	<#-- 储位选择E -->
</body>
</html>