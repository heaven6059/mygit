<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>复核打包</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/outstockrecheck/outstockrecheck.js?version=1.0.8.3"></script>
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"outstockrecheck.searchData();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"outstockrecheck.clearSearch();", "type":0},
			{"title":"新增","iconCls":"icon-add","action":"outstockrecheck.showAddDialog();", "type":1},
	        {"title":"修改","iconCls":"icon-edit","action":"outstockrecheck.editInfo();","type":2},
	        {"title":"删除","iconCls":"icon-del","action":"outstockrecheck.doDel();","type":3},
	        {"title":"审核","iconCls":"icon-aduit","action":"outstockrecheck.checkUserSelect();","type":4},
			<#--{"id":"printDetail","title":"打印预览","iconCls":"icon-print","action":"outstockrecheck.printDetail()","type":2},-->
			{"title":"打印预览","iconCls":"icon-print","action":"outstockrecheck.print('print')","type":2},
			<#--{"title":"打印预览(箱)","iconCls":"icon-print","action":"outstockrecheck.printDetailShowBox()","type":2},-->
			{"title":"打印预览(箱)","iconCls":"icon-print","action":"outstockrecheck.print('printBox')","type":2},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	   <div id="printMenu" class="easyui-menu" data-options="onClick:outstockrecheck.printDetail4SizeHorizontal" style="width:120px;">   
            <div data-options="name:'A4'">A4打印</div>
	        <div data-options="name:'A5'">A5打印</div>
       </div>   
       <div id="printBoxMenu" class="easyui-menu" data-options="onClick:outstockrecheck.printDetailBox4SizeHorizontal" style="width:120px;">   
            <div data-options="name:'A4'">A4打印</div>
	        <div data-options="name:'A5'">A5打印</div>
       </div>   
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" class="search-div" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
				        	<tr>
				            	<td class="common-td">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
				                <td class="common-td blank">创&nbsp;建&nbsp;人：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
				                <td class="common-td blank">创建日期：</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="startCreatetm" id="startCreatetmCondition" /></td>
				                <td class="common-line">&mdash;</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="endCreatetm" id="endCreatetmCondition" 
								data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/></td>
				        	</tr>
				        	<tr>
				            	<td class="common-td">单据编号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="recheckNo" id="divideNoCondition" /></td>
				                <td class="common-td blank">审&nbsp;核&nbsp;人：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
				                <td class="common-td blank">审核日期：</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="startAudittm" id="startAudittmCondition" /></td>
				                <td class="common-line">&mdash;</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="endAudittm" id="endAudittmCondition" 
								data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/></td>
				        	</tr>
				        	<tr>
				        		<td class="common-td">客户编码：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNoCondition" /></td>
				                <td class="common-td blank">单据类型：</td>
				                <td><input class="easyui-combobox ipt" style="width:120px" name="sourceType" id="sourceTypeCondition" /></td>
				                <td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		             			<td class="common-td blank">所属品牌：</td>
				                <td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
      		<#--查询end-->
			<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    		<@p.datagrid id="mainDataGrid" loadUrl=""
	    			saveUrl="" defaultColumn="" title="复核单列表"
					isHasToolBar="false"  divToolbar="" onClickRowEdit="false" singleSelect="false" pageSize="20" pagination="true"
					rownumbers="true" emptyMsg="" showFooter="true"
					columnsJsonList="[
						{field : 'id',checkbox :true},
						{field : 'locno',hidden :true,align:'left'},
						{field : 'status',hidden : true,align:'left'},
						{field : 'statusStr',title : '状态',width : 80,align:'left'},
						{field : 'recheckNo',title : '单据编号',width : 150},
						{field : 'divideNo',title : '来源单号',width : 150},
						{field : 'storeNo',title : '客户编码',width : 80,align:'left'},
						{field : 'storeName',title : '客户名称',width : 160,align:'left'},
						{field : 'sourceTypeStr',title : '单据类型',width : 80},
						
						{field : 'realQty',title : '实际拣货数量',width : 100,align:'right'},
						{field : 'recheckQty',title : '实际复核数量',width : 100,align:'right'},
						{field : 'packageQty',title : '装箱数量',width : 100,align:'right'},
						
						{field : 'creator',title : '创建人',width : 80,align:'left'},
						{field : 'creatorname',title : '创建人名称',width : 80,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130},
						{field : 'auditor',title : '审核人',width : 100,align:'left'},
						{field : 'auditorname',title : '审核人名称',width : 100,align:'left'},
						{field : 'audittm',title : '审核时间',width : 130},
						{field : 'editor',title : '编辑人',width : 100,align:'left'},
						{field : 'editorname',title : '编辑人名称',width : 100,align:'left'},
						{field : 'edittm',title : '编辑时间',width : 130},
						{field : 'printStatusStr',title : '打印状态',width : 100,align:'left'}
					]" 
					jsonExtend='{onLoadSuccess:function(data){
						outstockrecheck.onLoadSuccess(data);
					},onDblClickRow:function(rowIndex, rowData){
					    outstockrecheck.loadDetail(rowData,\'select\');
					},
					queryParams:{locno:"${session_user.locNo}"}}'
				/>
			</div>
        	<#--显示列表end-->
	    </div>
	</div>  
	<#-- 主表end -->
	
	<#-- 复核单明细窗口 Begin -->
	<div id="showDetailDialog"  class="easyui-dialog" title="复核单明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div id="main_order_dtlId" class="easyui-tabs" data-options="fit:true">
			
			<#-- 复核明细 Begin--> 
		    <div id="tab_order_dtl1" title="复核明细" >
		    	<div class="easyui-layout" data-options="fit:true">
					<#-- 工具菜单start -->
					<div data-options="region:'north',border:false" class="toolbar-region">
						<div data-options="region:'north',border:false">
							<@p.toolbar id="toolbar1"   listData=[
								{"id":"saveBtn","title":"保存","iconCls":"icon-save","action":"outstockrecheck.saveMainInfo();", "type":1},
								{"id":"checkBoxBtn","title":"复核装箱","iconCls":"icon-ok","action":"outstockrecheck.opencheckbox();","type":2},
								{"title":"取消","iconCls":"icon-cancel","action":"outstockrecheck.closeWindow('showDetailDialog')","type":0}
							]/>
						</div>
			    	</div>
			    	<#-- 工具菜单end -->
			    	<div data-options="region:'center',border:false" class="toolbar-region">
			    		<div class="easyui-layout" data-options="fit:true">
			    			<div data-options="region:'north',border:false" id="divideSearchDiv">
								<form id="divideMainInfoForm" name="divideMainInfoForm" method="post" class="city-form">
					        		<input type="hidden" name="locno" id="locno">
									<input type="hidden" name="expDate" id="expDate">
									<input type="hidden" name="serialNo" id="serialNo">
									<input type="hidden" name="lineNo" id="lineNo">
									<input type="hidden"  id="sourceTypeHid">
									
					        		<table>
							        	<tr>
							            	<td class="common-td blank">单据编号：</td>
							                <td><input class="easyui-validatebox ipt" style="width:120px" name="recheckNo" id="recheckNo" readOnly="true" /></td>
							                <td class="common-td blank">来源单号：</td>
							                <td><input class="easyui-validatebox ipt" style="width:120px" name="divideNo" id="locateNo" data-options="required:true" readOnly="true"/><input id="selectOutstockBtn" type="button" value="..." onclick="javascript:outstockrecheck.opendividenoselect();" size="3"></td>
							                <td class="common-td blank">客户：</td>
							                <td><input class="easyui-combobox" style="width:120px" name="storeNo" id="storeNo" data-options="required:true,valueField:'storeNo',textField:'storeName',onSelect:function(rec){outstockrecheck.selectStoreNoToOutstockDtl(rec)}" readOnly="true"/></td>
							               	<td class="common-td blank"></td>
							                <td></td>
							        	</tr>
							        </table>
					        	</form>
							</div>
							<div data-options="region:'center',border:false">
							<@p.datagrid id="order_dtl1_dataGrid" loadUrl="" showFooter="true"
				    			saveUrl="" defaultColumn="" title="复核明细" emptyMsg=""
								isHasToolBar="false"  divToolbar="" onClickRowEdit="false" singleSelect="false" pageSize="20" pagination="true"
									columnsJsonList="[
									  {field : 'id',checkbox :true},
									  {field : 'itemId',hidden : true},
									  {field : 'serialNo',hidden : true},
									  {field : 'packQty',hidden : true},
									  {field : 'expNo',hidden : true},
									  {field : 'expType',hidden : true},
									  {field : 'expDate',hidden : true},
									  {field : 'lineNo',hidden : true},
									  {field : 'divideId',hidden : true},
									  {field : 'scanLabelNo',hidden : true},
									  {field : 'outstockNo',hidden : true},
									  {field : 'styleNo',hidden : true},
							       	  {field : 'ownerName',title : '货主',width : 80,align:'left'},
							          {field : 'itemNo',title : '商品编码',width : 140,align:'left'},
							          {field : 'itemName',title : '商品名称',width : 160,formatter:outstockrecheck.itemFormatter,align:'left'},
 									  {field : 'colorName',title : '颜色',width : 65,align:'left'},
							          {field : 'sizeNo',title : '尺码',width : 65,align:'left'},
							          {field : 'itemQty',title : '计划复核数',width : 90,align:'right'},
							          {field : 'realQty',title : '实际拣货数量',width : 90,align:'right'},
							          {field : 'recheckQty',title : '实际复核数量',width : 90,align:'right'},
							          {field : 'diffQty',title : '未复核数量',width : 90,align:'right'},
							          {field : 'packageNoRecheckQty',title : '复核未封箱数量',width : 100,align:'right'}
							         ]"
								rownumbers="true" jsonExtend="{}" />
							</div>
			    		</div>
			    	</div>
				</div>
		   </div>   
		    <#-- 复核明细 End--> 
		    
		    <#-- 箱明细 Begin-->
		    <div id="tab_order_dtl2" title="箱明细">  
		    	<div class="easyui-layout" data-options="fit:true">
					<#-- 工具菜单start -->
					<div data-options="region:'north',border:false" class="toolbar-region">
						<div data-options="region:'north',border:false">
							<@p.toolbar id="toolbar2"   listData=[
								{"id":"searchBtn","title":"查询","iconCls":"icon-search","action":"outstockrecheck.searchBoxDetail();", "type":0},
	        					{"id":"clearBtn","title":"清除","iconCls":"icon-remove","action":"outstockrecheck.clearBoxDetail();", "type":0}
	        					<#--{"title":"标签打印(旧)","iconCls":"icon-print","action":"outstockrecheck.printByBox();","type":2},-->
	        					{"title":"标签打印","iconCls":"icon-print","action":"outstockrecheck.printByBoxNew();","type":2},
	        					<#--{"title":"无明细打印","iconCls":"icon-print","action":"outstockrecheck.printByBox(true);","type":2},-->
	        					{"id":"btn-add-dtl","title":"新增明细","iconCls":"icon-add-dtl","action":"outstockrecheck.addRecheckDtl()", "type":0},
	        					{"id":"btn-del-dtl","title":"删除明细","iconCls":"icon-del-dtl","action":"outstockrecheck.deleteOmOutstockRecheckDtl()", "type":0},
					            {"id":"btn-save-dtl","title":"保存明细","iconCls":"icon-save-dtl","action":"outstockrecheck.updateOmOutstockRecheckDtl()", "type":0},
								{"title":"取消","iconCls":"icon-cancel","action":"outstockrecheck.closeWindow('showDetailDialog')","type":0}
							]/>
						</div>
			    	</div>
			    	<#-- 工具菜单end -->
			    	<div data-options="region:'center',border:false" class="toolbar-region">
			    		<div class="easyui-layout" data-options="fit:true">
			    			<div data-options="region:'north',border:false" id="locSearchDiv">
								<form name="boxSearchForm" id="boxSearchForm" method="post" class="city-form">
					        		<table>
							        	<tr>
							            	<td class="common-td blank">箱号：</td>
							                <td><input class="easyui-validatebox ipt" style="width:120px" name="scanLabelNo"  /></td>
							                <td class="common-td blank">商品编码：</td>
							                <td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo"  /></td>
							                <td class="common-td blank">尺码：</td>
							                <td><input class="easyui-validatebox ipt" style="width:120px" name="sizeNo"  /></td>
							               	<td class="common-td blank"></td>
							                <td></td>
							        	</tr>
							        </table>
					        	</form>
							</div>
							<div data-options="region:'center',border:false">
								<@p.datagrid id="order_dtl2_dataGrid"    defaultColumn=""  fit="true"  title="箱明细"
									isHasToolBar="false" divToolbar="" pageSize="20" showFooter="true"
									onClickRowEdit="true" onClickRowAuto="true" pagination="true" emptyMsg="" 
									columnsJsonList="[
									      {field : 'id',checkbox :true},
									      {field : 'containerNo',hidden : true},
									 	  {field : 'scanLabelNo',title : '箱号',width : 120,align:'left'},
								          {field : 'itemNo',title : '商品编码',width : 140,align:'left'},
								          {field : 'itemName',title : '商品名称',width : 160,align:'left'},
								          {field : 'itemQty',title : '实际拣货数量',width : 100,align:'right'},
									      {field : 'realQty',title : '装箱数量',width : 80,align:'right',
									          	editor:{
							 						type:'numberbox',
							 						options:{
							 							min:0,
								 						required:true,
								 						missingMessage:'实际数量为必填项!'
							 						}
							 			  }},
								          {field : 'styleNo',hidden : true},
								          {field : 'colorName',title : '颜色',width : 65,align:'left'},
								          {field : 'sizeNo',title : '尺码',width : 65,align:'left'},
								           {field : 'statusStr',title : '状态',width : 100,align:'left'}
								         ]"
									rownumbers="true" jsonExtend="{}" />
							</div>
			    		</div>
			    	</div>
				</div>
		   </div>
		    <#-- 箱明细 End-->
		</div>
	</div>
	<#-- 复核单明细窗口 END -->
	
	<#-- 分货单号选择 Begin -->
	<div id="outstock_no_dialog"  class="easyui-dialog" title="波次单选择"  
		 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		 maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
			<div data-options="region:'north',border:false" >
				<@p.toolbar id="itemtoolbar"   listData=[
					{"title":"查询","iconCls":"icon-search","action":"outstockrecheck.outstockQuery();", "type":0},
	        		{"title":"清除","iconCls":"icon-remove","action":"outstockrecheck.searchClear2();", "type":0},
					{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"outstockrecheck.closeWindow('outstock_no_dialog');","type":0}
				]/>
				<div nowrap class="search-div" style="padding:10px;" id="divideNoSearchDiv">
					<form name="locateNoSearchForm" id="locateNoSearchForm" method="post" class="city-form">
						<table>
				        	<tr>
				            	<td class="common-td">波次号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="locateNo" id="locateNoCon"   /></td>
				                <td class="common-td blank">操作日期：</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="createTmStart" id="createTmStart" /></td>
				                <td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="createTmEnd" id="createTmEnd" /></td>
				        		<td class="common-td blank">创建人：</td>
				                <td><input class="easyui-datebox ipt" style="width:120px" name="creator" id="creator" /></td>
				        	</tr>
				        </table>
					</form>
				</div>
			</div>
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false">
				<@p.datagrid id="outstock_no_dataGrid"    defaultColumn=""  
					 isHasToolBar="false" divToolbar="" height="500"  pageSize="20" 
					 onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect = "true"
					 rownumbers="true" checkOnSelect="false" emptyMsg="" 
					 columnsJsonList="[
						{field : 'locno',hidden:true},
					 	{field : 'lineNo',hidden:true},
					 	{field : 'expDate',hidden:true},
					  	{field : 'locateNo',title : '波次号',width : 130,align:'left'},
					  	{field : 'operateDate',title : '操作日期',width : 120},
					  	{field : 'totalItemQty',title : '计划数量',width : 80,align:'right'},
					  	{field : 'totalRealQty',title : '实际数量',width : 80,align:'right'},
					  	{field : 'creator',title : '创建人',width : 80,align:'left'},
					  	{field : 'creatorname',title : '创建人名称',width : 80,align:'left'}
					]"
					rownumbers="true" jsonExtend="{onDblClickRow:function(rowIndex, rowData){
					    outstockrecheck.setDivideNo(rowData,'add');
					}}" 
				/>
			</div>
			<#--显示列表end-->		
		</div>	
	</div> 
	<#-- 分货单号选择 END -->

	<#-- 复核装箱 Begin -->
	
	<div id="check_box_dialog"  class="easyui-dialog" title="复核装箱"  
		 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		 maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<#--
			<div data-options="region:'north',border:false" >
				
				<div nowrap class="search-div" style="padding:10px;" id="checkboxsearchForm">
					<form name="checkboxsearchForm" id="checkboxsearchForm" method="post" class="city-form">
						<label style="display:none;">
							箱号：<input class="easyui-validatebox ipt" style="width:100px" name="boxNo"/>
							<a href="javascript:outstockrecheck.getConLabelNo();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">新箱</a>
						</label>
					</form>
				</div>
			</div>
			-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false">
				<@p.toolbar id="boxtoolbar"   listData=[
					{"title":"装箱","iconCls":"icon-download","action":"outstockrecheck.packageBox();", "type":0},
					{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"outstockrecheck.closeWindow('check_box_dialog');","type":0}
				]/>
				<@p.datagrid id="check_box_dataGrid" defaultColumn=""  emptyMsg=""
					isHasToolBar="false" divToolbar="#boxtoolbar" pageSize="20" rownumbers="true" 
					onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect = "true"
					columnsJsonList="[
						  {field : 'itemId',hidden : true},
						  {field : 'serialNo',hidden : true},
						  {field : 'packQty',hidden : true},
						  {field : 'expNo',hidden : true},
						  {field : 'expType',hidden : true},
						  {field : 'expDate',hidden : true},
						  {field : 'ownerNo',hidden : true},
						  {field : 'lineNo',hidden : true},
						  {field : 'divideId',hidden : true},
						  {field : 'scanLabelNo',hidden : true},
						  {field : 'outstockNo',hidden : true},
				          {field : 'itemNo',title : '商品编码',width : 150,align:'left'},
				          {field : 'itemName',title : '商品名称',width : 160,align:'left'},
				          {field : 'itemQty',title : '数量',width : 80,align:'right'},
				          {field : 'packageNum',title : '装箱数量',width : 80,align:'right',
				          			editor:{
				          				type:'numberbox',
				          				options:{
				          					value:0
				          				}
				          			}},
				          {field : 'colorName',title : '颜色',width : 100,align:'left'},
				          {field : 'sizeNo',title : '尺码',width : 120,align:'left'}
				         ]"
					jsonExtend="{}" />
			</div>
			<#--显示列表end-->		
		</div>	
	</div>  
	<#-- 复核装箱 END -->
	
	
	<#-- RF复核装箱 -->
	<div id="check_box_dialog_rf" class="easyui-dialog" title="RF复核装箱"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false" >
					<@p.toolbar id="recheckLoadBoxtoolbarRf" listData=[
	                         {"title":"封箱","iconCls":"icon-download","action":"outstockrecheck.packageBoxRf()", "type":0},
	                         {"title":"取消","iconCls":"icon-cancel","action":"outstockrecheck.closeWindow('check_box_dialog_rf')","type":0}
		                  ]
					 />			
					 
					 <div class="search-div">
					 	<form name="divideMainInfoRfForm" id="divideMainInfoRfForm" method="post" class="city-form">
							<table>
						    	<tr>
						        	<td class="common-td">单据编号：</td>
						            <td><input class="easyui-validatebox ipt" style="width:120px" name="recheckNo" id="recheckNoRf" readOnly="true" /></td>
						            <td class="common-td blank">波次号：</td>
						            <td>
						            	<input class="easyui-validatebox ipt" style="width:120px" name="divideNo" id="divideNoRf" data-options="required:true" readOnly="true"/>
						            </td>
						            <td class="common-td blank">客户：</td>
						            <td>
						            	<input class="easyui-combobox" style="width:160px" name="storeNo" id="storeNoRf" data-options="required:true,valueField:'storeNo',textField:'storeName',onSelect:function(rec){}"/>
									</td>
						         </tr>
						     </table>
						</form>
					</div>	
						
				</div>
				<div  data-options="region:'center',border:false" >
					<@p.datagrid id="check_box_dataGrid_rf"    defaultColumn=""  showFooter="true" 
						isHasToolBar="false" divToolbar="" height="390"    pageSize="10" 
						onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect = "true"
						rownumbers="true"  emptyMsg=""
						columnsJsonList="[
					          {field : 'itemNo',title : '商品编码',width : 160,align:'left'},
					          {field : 'itemName',title : '商品名称',width : 180,align:'left'},
					          {field : 'itemQty',title : '数量',width : 60,align:'right',hidden:true},
					          {field : 'realQty',title : '复核未封箱数量',width : 100,align:'right'},
					          {field : 'colorName',title : '颜色',width : 100,align:'left'},
					          {field : 'sizeNo',title : '尺码',width : 120,align:'left'}
					         ]"
						rownumbers="true" jsonExtend="{}" />
				</div>
			</div>
	</div>
	
	
	<#-- 添加明细 -->
	<div id="add_recheckdtl_dialog" class="easyui-dialog" title="新增商品明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false" >
					<@p.toolbar id="addItemToolbar" listData=[
							 {"title":"查询","iconCls":"icon-search","action":"outstockrecheck.searchItem()", "type":0},
	                         {"title":"确定","iconCls":"icon-ok","action":"outstockrecheck.selectItem()", "type":0},
	                         {"title":"取消","iconCls":"icon-cancel","action":"outstockrecheck.closeWindow('add_recheckdtl_dialog')","type":0}
		                  ]
					 />			
					 
					 <div class="search-div">
					 	<form name="searchItemForm" id="searchItemForm" method="post" class="city-form">
							<table>
						    	<tr>
						        	<td class="common-td">商品编码：</td>
						            <td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo"/></td>
						            <td class="common-td blank">条码：</td>
						            <td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcode" /></td>
						         </tr>
						     </table>
						</form>
					</div>	
						
				</div>
				<div  data-options="region:'center',border:false" >
					<@p.datagrid id="addRecheckDataGrid"   defaultColumn=""  showFooter="true" 
						isHasToolBar="false" divToolbar=""  height="390"    pageSize="10" 
						onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect = "true"
						rownumbers="true"  emptyMsg=""
						columnsJsonList="[
							  {field : 'id',checkbox :true},
					          {field : 'itemNo',title : '商品编码',width : 160,align:'left'},
					          {field : 'itemName',title : '商品名称',width : 180,align:'left'},
					          {field : 'colorName',title : '颜色',width : 100,align:'left'},
					          {field : 'sizeNo',title : '尺码',width : 120,align:'left'},
					          {field : 'noRecheckQty',title : '待复核数量',width : 80,align:'right'}
					         ]"
						rownumbers="true" jsonExtend="{}" />
				</div>
			</div>
	</div>
	
	<#-- 箱号输入Begin -->
	<div id="package_box_dialog"  class="easyui-dialog" title="输入箱号" style="padding:10px;height:150px;width:230px;"
		 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		 minimizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false">
				<table>
					<tr>
						<td class="common-td" width="50" height="60">箱号：</td>
						<td><input class="easyui-validatebox ipt" style="width:140px" id="boxNo" /></td>
					</tr>
					<tr>
						<td colspan='2' align = 'center' class="common-td">
							<a href="javascript:outstockrecheck.querySaveDtl();"  class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a>
							<a href="javascript:outstockrecheck.closeWindow('package_box_dialog');"  class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
		</div> 
	</div>
	<#-- 复核人选择 END -->
	
		
	<#-- 复核人选择Begin -->
	<div id="check_user_dialog"  class="easyui-dialog" title="复核人选择" style="padding:10px;height:200px;width:300px;"
		 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		 minimizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false">
				<table>
					<tr>
						<td class="common-td blank" width="80" height="60">复核人：</td>
						<td width="100"><input class="easyui-combobox" data-options="editable:false" style="width:110px" id="checkUser" /></td>
					</tr>
					<tr>
						<td colspan='2' ><div align="center"><a href="javascript:outstockrecheck.check();"  class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a></div></td>
					</tr>
				</table>
			</div>
		</div>
		</div> 
	</div>
	<#-- 复核人选择 END -->
</body>
</html>