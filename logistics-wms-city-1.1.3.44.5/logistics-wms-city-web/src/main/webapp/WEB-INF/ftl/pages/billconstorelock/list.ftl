<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>客户库存锁定</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billconstorelock/billconstorelock.js?version=1.1.1.1"></script>
</head>
<body class="easyui-layout">

	<#-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
			<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"billconstorelock.searchData()", "type":0},
	            {"title":"清除","iconCls":"icon-remove","action":"billconstorelock.searchClear('searchForm')", "type":0},
				{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"billconstorelock.showAddDialog()","type":1},
				{"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billconstorelock.editInfo()","type":2},
				{"id":"btn-del","title":"删除","iconCls":"icon-del","action":"billconstorelock.delStorelock()","type":3},
				{"id":"btn-check","title":"审核","iconCls":"icon-aduit","action":"billconstorelock.auditStorelock()","type":4},
				{"id":"btn-close","title":"手工关闭","iconCls":"icon-close","action":"billconstorelock.overStoreLock()","type":4},
				{"id":"btn-cancel","title":"关闭","iconCls":"icon-cancel","action":"closeWindow('客户库存锁定')","type":0}
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
                     			<td class="common-td">状态：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
                     			<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
                     			<td class="common-td blank">创建日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetmCondition" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetmCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td">单据编号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="storelockNo" id="storelockNoCondition" /></td>	
                     			<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
                     			<td class="common-td blank">审核日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittmCondition" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittmCondition" /></td>
                     		</tr>
                     		
                     		<tr>
                     			<td class="common-td">锁定类型：</td>
                     			<td><input class="easyui-combobox ipt" style="width:120px" name="storelockType" id="storelockTypeCondition" /></td>	
                     			<td class="common-td blank">库存属性：</td>
                     			<td><input class="easyui-combobox ipt" style="width:120px" name="sourceType" id="sourceTypeCondition" /></td>	
                     			<td class="common-td blank">客户：</td>
                     			<td colspan='3'><input class="easyui-combogrid ipt" style="width:310px" name="storeNo" id="storeNoCondition"/></td>
                     		</tr>
                     		
                     		<tr>
                     			<td class="common-td">来源单号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="sourceNo" id="sourceNo" /></td>
                     			<td class="common-td blank">业务类型：</td>
                     			<td><input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessType" /></td>
                     			<td class="common-td blank">品牌库：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo" /></td>
                     			<td class="common-td blank">所属品牌：</td>
                     			<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
                     		</tr>
                     		
                     	</table>
				 	</form>	
				 </div>
			</div>
			
			<!--显示列表-->
            <div data-options="region:'center',border:false">
            	<@p.datagrid id="mainDataGrid"  saveUrl=""  loadUrl="" defaultColumn=""  title="客户库存锁定列表"
					isHasToolBar="false"  divToolbar="#" height="450"  onClickRowEdit="false" 
					singleSelect="false" pageSize="20"
					rownumbers="true"  emptyMsg ="" showFooter="true"
					columnsJsonList="[
						{field : 'id',checkbox :true},	
						{field : 'statusStr',title : '状态',width : 80,align:'left'},	
						{field : 'storelockNo',title : '单据编号',width : 140},
						{field : 'businessTypeStr',title : '业务类型',width : 80},
						{field : 'storelockTypeStr',title : '锁定类型',width : 100,align:'left'},
						{field : 'sourceTypeStr',title : '库存属性',width : 80},
						{field : 'itemQty',title : '实际数量',width : 80,align:'right'},
						{field : 'sourceNo',title : '来源单号',width : 130},
						{field : 'storeName',title : '客户',width : 170,align:'left'},
						{field : 'ownerName',title : '货主',width : 80,align:'left'},
						{field : 'creator',title : '创建人',width : 80,align:'left'},
						{field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130,sortable:true},
						{field : 'auditor',title : '审核人',width : 80,align:'left'},
						{field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
						{field : 'audittm',title : '审核时间',width : 130}
						]" 
						jsonExtend='{onDblClickRow:function(rowIndex, rowData){
			        		// 触发点击方法  调JS方法
			        		billconstorelock.loadView(rowData);
			     		}}'/>
            </div>
		</div>
	</div>
	
	
	<#-- 新增修改页面 BEGIN -->
	<div id="openUI"  class="easyui-dialog" title="新增/修改"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="addtoolbar"   listData=[
		        	{"id":"save_main_info","title":"保存","iconCls":"icon-save","action":"billconstorelock.saveMainInfo();", "type":1},
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billconstorelock.closeUI('openUI');","type":0}
		        ]/>
				<div nowrap class="search-div" style="padding:10px;">
					<form name="dataForm" id="dataForm" metdod="post" class="city-form">
						<input type="hidden" id="opt"/>
						<table>
							<tr>
						    	<td class="common-td blank">单据编号：</td>
						        <td><input class="easyui-validatebox ipt" style="width:120px" name="storelockNo" id="storelockNo" readOnly="readOnly"/></td>
						        <td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						        <td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNo" data-options="required:true" /></td>
						        <td class="common-td blank">锁定类型：</td>
						        <td><input class="easyui-combobox ipt" style="width:120px" name="storelockType" id="storelockType" data-options="required:true" /></td>
						        <td class="common-td blank"><font id="cusTitle">客户：</font></td>
						        <td>
						        	<span id="lbStore" >
						        		<input class="easyui-combogrid ipt" style="width:200px" name="storeNo" id="storeNo" />
						        	</span>
						        	<span id="lbSourceType" style="display:none;">
						        		<input class="easyui-combobox ipt" style="width:120px" name="sourceType" id="sourceType" />
						        	</span>
						        </td>
						    </tr>
						 </table>
					</form>
				</div>
			</div>
			
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false" id='detailDiv'>
				<@p.toolbar id="contentToolDiv" listData=[
		        	{"id":"add_row","title":"新增明细","iconCls":"icon-add-dtl","action":"billconstorelock.openConContentSelect();", "type":0},
		        	{"id":"del_row","title":"删除明细","iconCls":"icon-del-dtl","action":"billconstorelock.delStorelockDtl();", "type":0},
		       		{"id":"save_row","title":"保存明细","iconCls":"icon-save-dtl","action":"billconstorelock.saveStorelockDtl();","type":0},
		       		{"title":"模板下载","iconCls":"icon-download","action":"billconstorelock.downloadTemp();","type":0},
	       			{"id":"import_row","title":"导入","iconCls":"icon-import","action":"billconstorelock.importConToItem();","type":0}
		        ]/>
				<@p.datagrid id="storeLockDetail" name="" title="客户库存锁定明细" loadUrl="" saveUrl="" defaultColumn="" 
					 isHasToolBar="false"  divToolbar="#contentToolDiv" onClickRowEdit="false" singleSelect="true"   
					 pagination="true" rownumbers="true" onClickRowAuto="false" emptyMsg="" 
					 columnsJsonList="[
						{field : 'rowId',hidden:true},
						{field : 'storelockNo',hidden:true},
					    {field : 'cellNo',title : '储位 ',width : 110,align:'left'},
					    {field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
					    {field : 'itemName',title : '商品名称',width : 160,align:'left'},
					    {field : 'colorName',title : '颜色 ',width : 80,align:'left'},
					    {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
					    {field : 'barcode',title : '条码',width : 150,align:'left'},
					    {field : 'goodContentQty',title : '可分配数量',width : 80,align:'right'},
					    {field : 'itemQty',title : '分配数量',width : 80,align:'right',
					    	editor:{
					        	type:'numberbox'
					        }
					    }
					]"
					jsonExtend='{onClickRow:function(rowIndex, rowData){
					 	// 触发点击方法  调JS方法
					    billconstorelock.contentEdit(rowIndex,rowData);
					}}'
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
		    		{"id":"btn-towmrequest","title":"转退厂申请","iconCls":"icon-redo","action":"billconstorelock.toWmRequest()","type":4},
		       		{"id":"btn_exp_row","title":"导出","iconCls":"icon-export","action":"billconstorelock.exportStorelockDtl();","type":0},
		        	{"id":"btn-canel-view","title":"取消","iconCls":"icon-cancel","action":"billconstorelock.closeUI('openDtlUI');","type":0}
		        ]/>
				<div nowrap class="search-div" style="padding:10px;">
					<form name="dataViewForm" id="dataViewForm" metdod="post" class="city-form">
						<input type="hidden" id="opt"/>
						<table>
							<tr>
						    	<td class="common-td blank">单据编号：</td>
						        <td><input class="easyui-validatebox ipt" style="width:120px" name="storelockNo" id="storelockNoView" readOnly="readOnly"/></td>
						        <td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						        <td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNoView"  /></td>
						        <td class="common-td blank">锁定类型：</td>
						        <td><input class="easyui-combobox ipt" style="width:120px" name="storelockType" id="storelockTypeView" /></td>
						        <td class="common-td blank"><font id="cusTitleView">客户：</font></td>
						        <td>
						        	<span id="lbStoreView" >
						        		<input class="easyui-combogrid ipt" style="width:200px" name="storeNo" id="storeNoView" />
						        	</span>
						        	<span id="lbSourceTypeView">
						        		<input class="easyui-combobox ipt" style="width:120px" name="sourceType" id="sourceTypeView" />
						        	</span>
						        </td>
						    </tr>
						 </table>
					</form>
				</div>
			</div>
			
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false" id='detailDivView'>
				<@p.datagrid id="storeLockDetailView" name="" title="客户库存锁定明细" loadUrl="" saveUrl="" defaultColumn="" 
					 isHasToolBar="false"  divToolbar="" onClickRowEdit="true" singleSelect="true"   
					 pagination="true" rownumbers="true" emptyMsg="" showFooter="true"
					 columnsJsonList="[
						{field : 'rowId',hidden:true},
					    {field : 'cellNo',title : '储位 ',width : 110,align:'left'},
					    {field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
					    {field : 'itemName',title : '商品名称',width : 160,align:'left'},
					    {field : 'colorName',title : '颜色 ',width : 80,align:'left'},
					    {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
					    {field : 'barcode',title : '条码',width : 150,align:'left'},
					    {field : 'goodContentQty',title : '可分配数量',width : 80,align:'right'},
					    {field : 'itemQty',title : '分配数量',width : 80,align:'right'}
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
			                             {"title":"查询","iconCls":"icon-search","action":"billconstorelock.searchConContent()", "type":0},
			                             {"title":"清除","iconCls":"icon-remove","action":"billconstorelock.searchConContentClear('selectContentSearchForm')", "type":0},
			                             {"title":"确认","iconCls":"icon-ok","action":"billconstorelock.contentSelectOK()","type":0}
			                             {"title":"取消","iconCls":"icon-cancel","action":"billconstorelock.closeUI('content_select_dialog')","type":0}
				                       ]
							  />
		    				<div class="search-div">
				         		<form name="selectContentSearchForm" id="selectContentSearchForm" method="post" class="city-form">
				         			<table>
				                 		<tr>
				                 			<td class="common-td blank">品牌库：</td>
				   							<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoId"/></td>
				   							<td class="common-td blank">所属品牌：</td>
                     			            <td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoItem" /></td>
	                     					<td class="common-td blank">季节：</td>
	                     					<td ><input class="easyui-combobox ipt" style="width:120px" name="seasonStr" id="seasonItem" /></td>
	                     					<td class="common-td blank">性别：</td>
	                     					<td ><input class="easyui-combobox ipt" style="width:120px" name="genderStr" id="genderItem" /></td>
				                 		</tr>
				                 		<tr>
				                 			<#-- 
				                 			<td class="common-td">库区：</td>
				                     		<td><input class="easyui-combobox ipt" style="width:120px" name="areaNo" id="areaNo" /></td>
				                     		-->
				                     		<td class="common-td blank">年份：</td>
	                     					<td ><input class="easyui-validatebox ipt" style="width:120px" name="yearsStr" id="yearsItem" /></td>
											<td class="common-td blank">储位：</td>
											<td><input class="easyui-validatebox ipt" style="width:120px" name="cellNo" id="cellNo" /></td>
				                 			<td class="common-td blank">商品编码：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo" /></td>
				                 			<td class="common-td blank">商品条码：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcode" /></td>
				                 			<td class="common-td blank"></td>
				                 			<td></td>
				                 		</tr>
				                 		<tr>
			                     			<td class="common-td">大类一：</td>
			                     			<td><input class="easyui-combobox" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
			                     			<td class="common-td blank">大类二：</td>
			                     			<td><input class="easyui-combobox" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
			                     			<td class="common-td blank">大类三：</td>
			                     			<td><input class="easyui-combobox" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
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
					           		{field : 'cellNo',title : '储位 ',width : 100,align:'left'},
					           		{field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
					                {field : 'itemName',title : '商品名称',width : 160,align:'left'},
					                {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
					                {field : 'goodContentQty',title : '可分配数量',width : 80,align:'right'},
					                {field : 'barcode',title : '条码',width : 140,align:'left'},
					                {field : 'colorName',title : '颜色 ',width : 80,align:'left'},
					                {field : 'yearsStr',title : '年份',width : 80,align:'left'},
			                		{field : 'seasonStr',title : '季节',width : 80,align:'left'},
			                		{field : 'genderStr',title : '性别',width : 80,align:'left'}
					            ]"/>
		    			</div>
		    		</div>
		    	</div>
		    </div>
	</div>
	<#-- 商品选择E -->
	<#--Excel导入选择框 div -->
<div id="showImportDialog"  class="easyui-window" title="导入"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		minimizable:false,maximizable:false,maximized:false" style="width:450px;" >
			<div class="easyui-layout" data-options="fit:true" style="height:100px;">
				<div data-options="region:'north',border:false">
					<form name="dataViewForm" id="importForm" method="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td colspan="2" style="color:red;">只允许上传后缀为.xlsx、.xls的文件</td>
							</tr>
							<tr>
								<td class="common-td">选择文件：</td>
								<td>
									<iframe src="" id="iframe" frameborder="0" height="25"></iframe>
								</td>
							</tr>
						</table>
					</form>
				</div>
</div>
<#--Excel导入选择框 div -->
	
</body>
</html>