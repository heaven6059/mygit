<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退厂复核单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/wmrecheck/wmrecheck.js?version=1.1.1.1"></script>
    <!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout">

	<#-- 工具菜单div -->
    <div data-options="region:'north',border:false" class="toolbar-region">
    	<@p.toolbar id="toolbar" listData=[
    		{"title":"查询","iconCls":"icon-search","action":"wmrecheck.searchData()", "type":0},
		 	{"title":"清除","iconCls":"icon-remove","action":"wmrecheck.clearSearchCondition()", "type":0},
        	{"id":"btn-add","title":"新单","iconCls":"icon-add","action":"wmrecheck.showAddDialog()","type":1},
			{"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"wmrecheck.editInfo()","type":2},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('退厂复核单查询')","type":0}
	    ]/>
	</div>
	
	
	<div data-options="region:'center',border:false">
		<#-- 查询条件 start -->
		<div class="easyui-layout" data-options="fit:true" >
			<div data-options="region:'north',border:false" >
					<form name="searchForm" id="searchForm" method="post" class="city-form" style="padding:10px;">
						<table>
								<tr>
									<td class="common-td">状态：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="status" id="statusCondition" /></td>
									
									<td class="common-td blank">创建人：</td>	
									<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" /></td>
									<td class="common-td blank">创建日期：</td>
									<td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetm" /></td>
									<td class="common-line" width="50">&mdash;</td>
									<td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetm" /></td>
								</tr>
								
								<tr>
									<td class="common-td">品&nbsp;牌&nbsp;库：</td>
									<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
									<td class="common-td blank">单据编号：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="recheckNo" /></td>
									<td class="common-td blank">供应商：</td>
									<td colspan='3'><input class="easyui-combobox ipt" style="width:290px" name="supplierNo"  id="supplierNoSearch"/></td>
								</tr>
								<tr>
									<td class="common-td blank">所属品牌：</td>
			             			<td colspan="7">
			             				<input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" />
			             			</td>
								</tr>
							</table>
						</form>
				</div>
				
				<div data-options="region:'center',border:false" >
					<@p.datagrid id="mainDataGrid"  loadUrl=""
						 saveUrl=""  defaultColumn=""  title="退厂复核单列表"
						isHasToolBar="false"  divToolbar="" onClickRowEdit="false" singleSelect="true" pageSize="20"
						rownumbers="true" emptyMsg=""
						columnsJsonList="[
							{field : 'id',checkbox :true},	
							{field : 'supplierNo',hidden :true},	
							{field : 'status',title : '状态',align:'left',width : 100,formatter:wmrecheck.statusFormatter},	
							{field : 'recheckNo',title : '单据编号',width : 180},
							{field : 'supplierName',title : '供应商',width : 180,align:'left'},
							{field : 'creator',title : '创建人',width : 100,align:'left'},
							{field : 'creatorName',title : '创建人名称',width : 100,sortable:true},
							{field : 'createtm',title : '创建日期',width : 130},
							{field : 'auditor',title : '审核人',width : 100,align:'left',hidden:true},
							{field : 'audittm',title : '审核日期',width : 120,hidden:true},
							{field : 'printStatus',title : '打印状态',width : 100,align:'left'},
							{field : 'editor',title : '更新人',width : 100,sortable:true},
							{field : 'editorName',title : '更新人名称',width : 100,sortable:true},
						    {field : 'edittm',title : '更新时间',width : 130,sortable:true},
							{field : 'locno',hidden :true}
							]" 
							jsonExtend='{onDblClickRow:function(rowIndex, rowData){
							    wmrecheck.loadDetail(rowData,\'select\');
					}}'/>
				</div>
				
		</div>
	</div>
	
	
	<#-- 复核单明细窗口 -->
	<div id="showDetailDialog"  class="easyui-window" title="退厂复核明细"  
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		minimizable:false,maximizable:false,maximized:true">
		
		<div id="main_order_dtlId" class="easyui-tabs" data-options="fit:true">  
		
			<#-- tab1开始--> 
			<div id="tab_order_dtl1" title="复核明细">
				
				<div class="easyui-layout" data-options="fit:true" >
					<#-- 工具栏  -->
					<#--
					<div data-options="region:'north',border:false" class="toolbar-region">
						<@p.toolbar id="tab1_toolbar" listData=[
							{"id":"saveBtn","title":"保存","iconCls":"icon-save","action":"wmrecheck.saveMainInfo()","type":0},
							{"id":"checkBoxBtn","title":"复核装箱","iconCls":"icon-download","action":"wmrecheck.opencheckbox()","type":0},
							{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('退厂调度')","type":0}
						 ]/>
					</div>
					-->
				
					<div data-options="region:'center',border:false">
						<div class="easyui-layout" data-options="fit:true" >
							
							<div data-options="region:'north',border:false" >
								<form id="searchFormTab1" name="searchFormTab1" class="city-form" style="padding:10px;">
									<input id="supplierNoHidden" name="supplierNo" type="hidden" />
									<table>
										<tr>
											<td class="common-td">单据编号：</td>
											<td><input class="easyui-validatebox ipt" style="width:120px" name="recheckNo" id="recheckNo" readOnly="true" /></td>
											<td class="common-td blank">退厂通知单号：</td>
							                <td><input class="easyui-validatebox ipt" style="width:120px" name="divideNo" id="recedeNo" data-options="required:true" readOnly="true"/><input id="selectRecedeBtn" type="button" value="..." onclick="javascript:wmrecheck.openRecedeNoSelect();" size="3"></td>
											<td class="common-td blank">供应商：</td>
											<td><input class="easyui-validatebox ipt" style="width:170px" name="supplierName" id="supplierName" readOnly="true" /></td>
											<td class="common-td blank" style="display:none;">复核人：</td>
											<td style="display:none;"><input class="easyui-validatebox ipt" style="width:100px" name="locnoStr" id="locnoStr" /></td>
											<td>
												&nbsp;
												<a id="checkBoxBtn"  class="easyui-linkbutton"  href="javascript:wmrecheck.opencheckbox();" iconCls="icon-download">复核装箱</a>
												<a id="saveBtn"  class="easyui-linkbutton"  href="javascript:wmrecheck.saveMainInfo();"  iconCls="icon-save">保存</a>
											</td>
										</tr>
									</table>
								</form>
							</div>
							
							<div data-options="region:'center',border:false">
								<@p.datagrid id="order_dtl1_dataGrid" defaultColumn=""  
									isHasToolBar="false" divToolbar="#toolbarDlt" height="350"    pageSize="10" 
									onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect="false"
									rownumbers="true"  title="复核明细" showFooter="true"
									columnsJsonList="[
									  {field : 'id',checkbox :true},	
									  {field : 'barcode',hidden:true},	
									  {field : 'sourceNo',hidden:true},	
									  {field : 'colorNo',hidden:true},	
									  {field : 'recedeType',hidden:true},
									  {field : 'recedeDate',hidden:true},
									  {field : 'divideId',hidden:true},
									  {field : 'dCellNo',hidden:true},
									  {field : 'dCellId',hidden:true},
									  {field : 'brandNo',hidden:true},
							          {field : 'itemNo',title : '商品编码',width : 160,align:'left'},
							       	  {field : 'ownerNo',title : '货主',formatter:wmrecheck.ownerNoFormatter,width : 100,align:'left'},	
							          {field : 'itemName',title : '商品名称',width : 160,align:'left'},
							          {field : 'colorName',title : '颜色',width : 80,align:'left'},
							          {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
							          {field : 'realQty',title : '计划复核数量',width : 90,align:'right'},
							          {field : 'recheckQty',title : '实际复核数量',width : 90,align:'right'},
							          {field : 'recheckQty',title : '装箱数量',width : 90,align:'right'},
							          {field : 'diffQty',title : '未装箱数量',width : 90,align:'right'}
							         ]" 
								jsonExtend="{}" />
							</div>
							
						</div>
					</div>
					
				</div>
			</div>  
			<#-- tab1结束-->
			
			<#-- tab2开始-->
			<#-- 箱明细-->
			<div id="tab_order_dtl2" title="箱明细">  
				<div class="easyui-layout" data-options="fit:true" >
					<div data-options="region:'north',border:false">
						<@p.toolbar id="boxdtlbar"   listData=[
							{"title":"标签打印","iconCls":"icon-print","action":"wmrecheck.printByBox();", "type":0},
							{"title":"无明细打印","iconCls":"icon-print","action":"wmrecheck.printByBox(true);", "type":0},
							{"title":"取消","iconCls":"icon-cancel","action":"wmrecheck.closeWindow('showDetailDialog')","type":0}
						]/>
					</div>
					<div data-options="region:'center',border:false">
						<@p.datagrid id="order_dtl2_dataGrid"    defaultColumn=""  
							isHasToolBar="false" divToolbar="" height="390"    pageSize="10" 
							onClickRowEdit="false" onClickRowAuto="false" pagination="true"
							rownumbers="true"  title="退厂复核明细" showFooter="true"
							columnsJsonList="[
								  {field : 'id',checkbox :true},	
							 	  {field : 'scanLabelNo',title : '箱号',width : 120,align:'left'},
						          {field : 'itemNo',title : '商品编码',width : 160,align:'left'},
						          {field : 'itemName',title : '商品名称',width : 160,align:'left'},
						          {field : 'itemQty',title : '数量',width : 90,align:'right'},
						          {field : 'styleNo',hidden:true},
						          {field : 'colorNoStr',title : '颜色',width : 100,align:'left'},
						          {field : 'sizeNo',title : '尺码',width : 100,align:'left'},
						          {field : 'statusStr',title : '状态',width : 100,align:'left'}
						         ]"
						rownumbers="true" jsonExtend="{}" />
					</div>					
				</div>
			</div> 
			<#-- tab2结束-->
			
		</div>
	</div>
	
	
	<#-- 退厂通知单单号选择 Begin -->
	<div id="recede_no_dialog"  class="easyui-dialog" title="退厂通知单选择"  
		 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		 maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
			<div data-options="region:'north',border:false" >
				<@p.toolbar id="itemtoolbar"   listData=[
					{"title":"查询","iconCls":"icon-search","action":"wmrecheck.recedeQuery();", "type":0},
	        		{"title":"清除","iconCls":"icon-remove","action":"wmrecheck.searchClear2();", "type":0},
					{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"wmrecheck.closeWindow('recede_no_dialog');","type":0}
				]/>
				<div nowrap class="search-div" style="padding:10px;" id="recedeNoSearchDiv">
					<form name="recedeSearchForm" id="recedeSearchForm" method="post" class="city-form">
						<table>
				        	<tr>
				            	<td class="common-td">退厂通知单号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="recedeNo" id="recedeNoCon"   /></td>
				                <td class="common-td blank">创建日期：</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="createTmStart" id="createTmStart" /></td>
				                <td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="createTmEnd" id="createTmEnd" /></td>
				        	</tr>
				        </table>
					</form>
				</div>
			</div>
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false">
				<@p.datagrid id="recede_no_dataGrid"    defaultColumn=""  
					 isHasToolBar="false" divToolbar="" height="500"  pageSize="20" title="退厂通知单列表"
					 onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect = "true"
					 rownumbers="true" checkOnSelect="false" emptyMsg="" 
					 columnsJsonList="[
						{field : 'locno',hidden:true},
						{field : 'ownerNo',hidden:true},
						{field : 'supplierNo',hidden:true},
						{field : 'supplierName',hidden:true},
					  	{field : 'recedeNo',title : '退厂通知单号',width : 180},
					  	{field : 'createtm',title : '创建日期',width : 130},
					  	{field : 'totalRecedeQty',title : '计划数量',width : 80,align:'right'},
					  	{field : 'totalOutstockQty',title : '下架数量',width : 80,align:'right'}
					]"
					rownumbers="true" jsonExtend="{onDblClickRow:function(rowIndex, rowData){
					    wmrecheck.selectRecedeQuery(rowData);
					}}" 
				/>
			</div>
			<#--显示列表end-->		
		</div>	
	</div> 
	<#-- 退厂通知单单号选择 END -->
	

	<#-- 复核装箱-->
	<div id="check_box_dialog" class="easyui-window" title="复核装箱" 
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		minimizable:false,maximizable:false,maximized:true">
		
			<div class="easyui-layout" data-options="fit:true">
				
				
				<div data-options="region:'north',border:false">
					<@p.toolbar id="addtoolbar" listData=[
	                        {"title":"装箱","iconCls":"icon-download","action":"wmrecheck.packageBox()", "type":0},
	                        {"title":"取消","iconCls":"icon-cancel","action":"wmrecheck.closeWindow('check_box_dialog')","type":0}
		            ]/>
					
					<#-- 
					<form name="checkboxsearchForm" id="checkboxsearchForm" method="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td class="common-td">箱号：</td>
								<td><input class="easyui-validatebox ipt" style="width:100px;" name="boxNo" /></td>
							</tr>
						</table>
						<input type="hidden" name="containerNo"    />
						<input type="hidden" name="containerType"    />
					</form>
					-->
				</div>
				
				<div data-options="region:'center',border:false">
					<@p.datagrid id="check_box_dataGrid"    defaultColumn=""  
						isHasToolBar="false" divToolbar=""  pageSize="10" title="装箱"
						onClickRowEdit="true" onClickRowAuto="true" pagination="true" singleSelect = "true"
						rownumbers="true" emptyMsg="" 
						columnsJsonList="[
							  {field : 'barcode',hidden : true},	
							  {field : 'sourceNo',hidden:true},	
							  {field : 'colorNo',hidden:true},
							  {field : 'recedeType',hidden:true},
							  {field : 'recedeDate',hidden:true},
							  {field : 'itemId',hidden:true},
							  {field : 'onwerNo',hidden : true},
							  {field : 'divideId',hidden:true},
							  {field : 'dCellNo',hidden:true},
							  {field : 'dCellId',hidden:true},
							  {field : 'brandNo',hidden:true},
					          {field : 'itemNo',title : '商品编码',width : 160,align:'left'},
					          {field : 'itemName',title : '商品名称',width : 120,align:'left'},
					          {field : 'realQty',title : '数量',width : 60,align:'right'},
					          {field : 'packageNum',title : '装箱数量',align:'right',width : 60,
					          			editor:{
					          				type:'numberbox',
					          				options:{
					          					required:true
					          				}
					          				
					          			}},
					          {field : 'styleNo',hidden:true},
					          {field : 'colorName',title : '颜色',width : 100,align:'left'},
					          {field : 'sizeNo',title : '尺码',width : 120,align:'left'}
					         ]"
					rownumbers="true" jsonExtend="{}" />
				</div>
			</div>
	</div> 
	<#-- 复核装箱end-->
	
	
	<#-- RF复核装箱 -->
	<div id="check_box_dialog_rf" class="easyui-dialog" title="RF复核装箱"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false" >
					<@p.toolbar id="recheckLoadBoxtoolbarRf" listData=[
	                         {"title":"封箱","iconCls":"icon-download","action":"wmrecheck.packageBoxRf()", "type":0},
	                         {"title":"取消","iconCls":"icon-cancel","action":"wmrecheck.closeWindow('check_box_dialog_rf')","type":0}
		                  ]
					 />			
					 
					 <div class="search-div">
					 	<form name="divideMainInfoRfForm" id="divideMainInfoRfForm" method="post" class="city-form">
					 		<input id="supplierNoHiddenRf" type = "hidden" value = "">
							<table>
						    	<tr>
						        	<td class="common-td">单据编号：</td>
						            <td><input class="easyui-validatebox ipt" style="width:120px" name="recheckNo" id="recheckNoRf" readOnly="true" /></td>
						            <td class="common-td blank">退厂通知单号：</td>
						            <td>
						            	<input class="easyui-validatebox ipt" style="width:120px" name="recedeNo" id="recedeNoRf" data-options="required:true" readOnly="true"/>
						            </td>
						            <td class="common-td blank">供应商：</td>
						            <td>
						            	<input class="easyui-validatebox ipt" style="width:160px" name="supplierName" id="supplierNameRf" />
									</td>
						         </tr>
						     </table>
						</form>
					</div>	
						
				</div>
				<div  data-options="region:'center',border:false" >
					<@p.datagrid id="check_box_dataGrid_rf"    defaultColumn=""  
						isHasToolBar="false" divToolbar="" height="390"    pageSize="10" 
						onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect = "true"
						rownumbers="true"  emptyMsg=""
						columnsJsonList="[
					          {field : 'itemNo',title : '商品编码',width : 160,align:'left'},
					          {field : 'itemName',title : '商品名称',width : 180,align:'left'},
					          {field : 'itemQty',title : '数量',width : 60,align:'right'},
					          {field : 'realQty',title : '装箱数量',width : 60},
					          {field : 'colorName',title : '颜色',width : 100,align:'left'},
					          {field : 'sizeNo',title : '尺码',width : 120,align:'left'}
					         ]"
						rownumbers="true" jsonExtend="{}" />
				</div>
			</div>
	</div>
	
	
	<#-- 复核人选择-->
	<div id="check_user_dialog" class="easyui-window" title="复核人选择" 
		style="width:300px;height:140px;padding:5px;"   
		data-options="modal:true,resizable:false,draggable:true,
		collapsible:false,closed:true,minimizable:false,maximizable:false">  
			
			<div style="padding-top:10px;">
			<table width="100%" >
				<tr>
					<td class="common-td">复核人：</td>
					<td><input class="easyui-combobox ipt"  style="width:150px" id="checkUser" /></td>
				</tr>
				<tr>
					<td colspan='2' align='center' height="60">
						<a href="javascript:wmrecheck.check();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a>
						&nbsp;
						<a href="javascript:wmrecheck.closeWindow('check_user_dialog');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
					</td>
				</tr>
			</table>
			</div>
	</div> 
	<#-- 复核人选择end-->
			
</body>
</html>