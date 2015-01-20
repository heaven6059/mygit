<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>分货复核</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billomrecheck/billomrecheck.js?version=1.0.8.5"></script>
<!--object需放在head结尾会截断jquery的html函数获取内容-->
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
</object>
</head>
<body class="easyui-layout">

	<#-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar" listData=[
			{"title":"查询","iconCls":"icon-search","action":"billomrecheck.searchData()", "type":0},
            {"title":"清除","iconCls":"icon-remove","action":"billomrecheck.clearSearch()", "type":0},
			{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"billomrecheck.showAddDialog()","type":1},
			{"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billomrecheck.editInfo()","type":2},
			{"id":"btn-remove","title":"删除","iconCls":"icon-del","action":"billomrecheck.doDel()","type":3},
			{"id":"btn-audit","title":"审核","iconCls":"icon-ok","action":"billomrecheck.checkUserSelect()","type":4},
			<#-- {"id":"printDetail","title":"打印预览","iconCls":"icon-print","action":"billomrecheck.printDetail()","type":2}, -->
			{"title":"打印预览","iconCls":"icon-print","action":"billomrecheck.printA4orA5()","type":2},
			<#-- {"title":"打印预览(箱)","iconCls":"icon-print","action":"billomrecheck.printDetailShowBox()","type":2}, -->
			{"title":"打印预览(箱)","iconCls":"icon-print","action":"billomrecheck.printBoxA4orA5()","type":2},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('分货复核')","type":0}
		 ]/>
	</div>
	<div id="printMenu" class="easyui-menu" data-options="onClick:billomrecheck.printDetail4SizeHorizontal" style="width:120px;">   
            <div data-options="name:'A4'">A4打印</div>
	        <div data-options="name:'A5'">A5打印</div>
     </div> 
     <div id="printBoxMenu" class="easyui-menu" data-options="onClick:billomrecheck.printDetailBox4SizeHorizontal" style="width:120px;">   
            <div data-options="name:'A4'">A4打印</div>
	        <div data-options="name:'A5'">A5打印</div>
     </div> 
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
		             			<td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetmCondition" /></td>
		             			<td class="common-line" >&nbsp;&mdash;&nbsp;</td>
		             			<td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetmCondition" 
									data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/></td>
		             		</tr>
		             		<tr>
		             		<td class="common-td blank">单据编号：</td>
		             			<td><input class="easyui-validatebox ipt" style="width:120px" name="recheckNo" id="divideNoCondition" /></td>
		             			<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
		             			<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
		             			<td class="common-td blank">审核日期：</td>
		             			<td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittmCondition" /></td>
		             			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
		             			<td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittmCondition" 
									data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/></td>
		             		</tr>
		             		<tr>
		             			<td class="common-td blank">分货单号：</td>
		             			<td><input class="easyui-validatebox ipt" style="width:120px" name="divideNo" id="divideNoCondition2" /></td>
		             			<td class="common-td blank">客户编码：</td>
		             			<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNoCondition" /></td>
		             			<td class="common-td blank">复核日期：</td>
		             			<td><input class="easyui-datebox" style="width:120px" name="startExpDate" id="startExpDateCondition" /></td>
		             			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
		             			<td><input class="easyui-datebox" style="width:120px" name="endExpDate" id="endExpDateCondition" 
									data-options="validType:['vCheckDateRange[\'#startExpDateCondition\',\'结束日期不能小于开始日期\']']"/></td>
		             		</tr>
		             		<tr>
		             			<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td>
		             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/>
		             			</td>
		             			<td class="common-td blank">所属品牌：</td>
                     			<td colspan='3'><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" /></td>
		             		</tr>
		             	</table>
				 	</form>	
				 </div>
			</div>
			<!--显示列表-->
            <div data-options="region:'center',border:false">
            	<@p.datagrid id="mainDataGrid" loadUrl="" saveUrl="" defaultColumn="" title="分货复核单列表"
					isHasToolBar="false"  divToolbar="" height="430"  onClickRowEdit="false" singleSelect="false" pageSize="20"
					rownumbers="true"  showFooter="true"
					columnsJsonList="[
						{field : 'id',checkbox :true},	
						{field : 'status',hidden : true},	
						{field : 'statusStr',title : '状态',width : 80,align:'left'},	
						{field : 'recheckNo',title : '单据编号',width : 180},
						{field : 'storeNo',title : '客户编码',width : 100,align:'left'},
						{field : 'storeName',title : '客户名称',width : 170,align:'left'},
						
						{field : 'itemQty',title : '计划复核数量',width : 100,align:'right'},
						{field : 'realQty',title : '实际复核数量',width : 100,align:'right'},
						{field : 'packageQty',title : '装箱数量',width : 80,align:'right'},
						
						{field : 'creator',title : '创建人',width : 80,align:'left'},
						{field : 'creatorname',title : '创建人名称',width : 80,align:'left'},
						{field : 'createtm',title : '创建日期',width : 130},
						{field : 'auditor',title : '审核人',width : 100,align:'left'},
						{field : 'auditorname',title : '审核人名称',width : 100,align:'left'},
						{field : 'audittm',title : '审核日期',width : 130},
						{field : 'edittm',title : '复核日期',width : 130},
						{field : 'printStatusStr',title : '打印状态',width : 100,align:'left'},
						{field : 'locno',hidden :true},
						{field : 'divideNo',hidden :true}
						]" 
						jsonExtend='{onLoadSuccess:function(data){
							billomrecheck.onLoadSuccess(data);
						},onDblClickRow:function(rowIndex, rowData){
						    billomrecheck.loadDetail(rowData,\'select\');
						},
						queryParams:{locno:"${session_user.locNo}"}}'/>
			</div>
		</div>
	</div>

	<#-- 复核单明细窗口 Begin -->
	<div id="showDetailDialog" class="easyui-dialog" title="复核单明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'center',border:false">
			    	<div id="main_order_dtlId" class="easyui-tabs" fit="true">
			    		<!-- 复核明细 -->
			    		<div id="tab_order_dtl1" title="复核明细" height='100%'>
			    			<div class="easyui-layout" data-options="fit:true">
			    				<div data-options="region:'north',border:false">
			    					<@p.toolbar id="rechecktoolbar" listData=[
					                             {"id":"checkBoxBtn","title":"复核装箱","iconCls":"icon-download","action":"billomrecheck.opencheckbox()", "type":2},
					                             {"id":"saveBtn","title":"保存","iconCls":"icon-save","action":"billomrecheck.saveMainInfo()", "type":0},
					                             {"title":"取消","iconCls":"icon-cancel","action":"billomrecheck.closeWindow('showDetailDialog')","type":0}
						                       ]
									  />
			    					<div class="search-div">
						         		<form name="divideMainInfoForm" id="divideMainInfoForm" method="post" class="city-form">
						         			<table>
						                 		<tr>
						                 			<td class="common-td">单据编号：</td>
						                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="recheckNo" id="recheckNo" readOnly="true" /></td>
						                 			<td class="common-td blank">分货单号：</td>
						                 			<td>
						                 				<input class="easyui-validatebox ipt" style="width:120px" name="divideNo" id="divideNo" data-options="required:true" readOnly="true"/>
														<input type="hidden" name="locno" id="locno">
														&nbsp;<a id="selectDivideBtn" href="javascript:billomrecheck.opendividenoselect();" class="easyui-linkbutton" iconCls="icon-import" >选择</a>
														<input type="hidden" name="expDate" id="expDate">
						                 			</td>
						                 			<td class="common-td blank">客户：</td>
						                 			<td>
						                 				<input class="easyui-combobox" style="width:120px" name="storeNo" id="storeNo" data-options="required:true,valueField:'storeNo',textField:'storeName',onSelect:function(rec){billomrecheck.selectStoreNoToDivideDtl(rec)}"/>
													</td>
						                 			<td class="common-td blank"></td>
						                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="recheckName" id="recheckName"  type="hidden"/></td>
						                 		</tr>
						                    </table>
						         		</form>
									</div>
					    		</div>
					    		<div  data-options="region:'center',border:false" >
								    <@p.datagrid id="order_dtl1_dataGrid" defaultColumn="" singleSelect="false"
										isHasToolBar="false" divToolbar=""  pageSize="20"  showFooter="true"
										onClickRowEdit="false" onClickRowAuto="false" pagination="true" height="370" emptyMsg=""
										columnsJsonList="[
										  {field : 'id',checkbox :true},
										  {field : 'itemId',hidden : true},
										  {field : 'packQty',hidden : true},
										  {field : 'expNo',hidden : true},
										  {field : 'expType',hidden : true},
										  {field : 'lineNo',hidden : true},
										  {field : 'divideId',hidden : true},
										  {field : 'poNo',hidden : true},
										  {field : 'boxNo',hidden : true},
								       	  {field : 'ownerName',title : '货主',width : 100,align:'left'},	
								          {field : 'itemNo',title : '商品编码',width : 150,align:'left'},
								          {field : 'itemName',title : '商品名称',width : 150,formatter:billomrecheck.itemFormatter,align:'left'},
								          {field : 'colorName',title : '颜色',width : 80,align:'left'},
								          {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
								          {field : 'styleNo',hidden : true,formatter:billomrecheck.itemFormatter},
								          {field : 'itemQty',title : '计划复核数量',width : 100,align:'right'},
								          {field : 'realQty',title : '实际复核数量',width : 100,align:'right'},
								          {field : 'realQty',title : '装箱数量',width : 100,align:'right'},
								          {field : 'diffQty',title : '未装箱数量',width : 100,formatter:billomrecheck.numFormatter,align:'right'},
								          {field : 'packageNoRecheckQty',title : '复核未封装箱数量',width : 100,align:'right'}
								         ]" 
										rownumbers="true" jsonExtend="{}" />
						    	</div>
				    		</div>
			    		</div>
			    		<!-- 箱明细 -->
			    		<div id="tab_order_dtl2" title="箱明细" height='100%'>
			    			<div class="easyui-layout" data-options="fit:true">
			    				<div data-options="region:'north',border:false">
			    					<@p.toolbar id="boxDtltoolbar" listData=[
					                             {"title":"查询","iconCls":"icon-search","action":"billomrecheck.searchBoxDetail()", "type":0},
					                             {"title":"清除","iconCls":"icon-remove","action":"billomrecheck.clearBoxDetail()", "type":0},
					                             <#--{"title":"标签打印(旧)","iconCls":"icon-print","action":"billomrecheck.printByBox()", "type":0},-->
					                             {"title":"标签打印","iconCls":"icon-print","action":"billomrecheck.printByBoxNew()", "type":0},
					                             <#--{"title":"无明细打印","iconCls":"icon-print","action":"billomrecheck.printByBox(true)", "type":0},-->
					                             {"id":"btn-del-dtl","title":"删除明细","iconCls":"icon-del-dtl","action":"billomrecheck.deleteOmRecheckDtl()", "type":0},
					                             {"id":"btn-save-dtl","title":"保存明细","iconCls":"icon-save-dtl","action":"billomrecheck.updateOmRecheckDtl()", "type":0},
					                             {"title":"取消","iconCls":"icon-cancel","action":"billomrecheck.closeWindow('showDetailDialog')","type":0}
						                       ]
									  />
			    					<div class="search-div">
			    						<form name="boxSearchForm" id="boxSearchForm" method="post" class="city-form">
						         			<table>
						                 		<tr>
						                 			<td class="common-td">箱号：</td>
						                 			<td><input class="easyui-validatebox ipt" style="width:130px" name="scanLabelNo"  /></td>
						                 			<td class="common-td blank">商品编码：</td>
						                 			<td><input class="easyui-validatebox ipt" style="width:130px" name="itemNo"  /></td>
						                 			<td class="common-td blank">尺码：</td>
						                 			<td><input class="easyui-validatebox ipt" style="width:130px" name="sizeNo"  /></td>
						                 		</tr>
						                    </table>
						         		</form>
			    					</div>
		    					</div>
		    					<div  data-options="region:'center',border:false" >
								    <@p.datagrid id="order_dtl2_dataGrid"  name="" title=""  loadUrl=""
							 			 saveUrl="" defaultColumn=""  divToolbar="" 
							 			isHasToolBar="false"  height="295"   showFooter="true"
							 			onClickRowEdit="true" singleSelect="true" onClickRowAuto="true"   
										pagination="true" rownumbers="true" emptyMsg=""
										columnsJsonList="[
										      {field : 'ck',checkbox :true},
										      {field : 'containerNo',hidden : true},
										      {field : 'rowId',hidden : true},
										 	  {field : 'scanLabelNo',title : '箱号',width : 120,align:'left'},
									          {field : 'itemNo',title : '商品编码',width : 150,align:'left'},
									          {field : 'itemName',title : '商品名称',width : 170,align:'left'},
									          {field : 'itemQty',title : '计划数量',width : 80,align:'right'},
									          {field : 'realQty',title : '实际数量',width : 80,align:'right',
									          	editor:{
							 						type:'numberbox',
							 						options:{
							 							min:0,
								 						required:true,
								 						missingMessage:'实际数量为必填项!'
							 						}
							 				   }},
									          {field : 'styleNo',hidden : true},
									          {field : 'colorName',title : '颜色',width : 100,align:'left'},
									          {field : 'sizeNo',title : '尺码',width : 120,align:'left'},
									          {field : 'statusStr',title : '状态',width : 100,align:'left'}
									         ]"
										rownumbers="true" jsonExtend="{}" />
						    	</div>
	    					</div>
			    		</div>
			    	</div>
			    </div>
			</div>
	</div>
	
	<#-- 复核装箱 Begin -->
	<div id="check_box_dialog" class="easyui-dialog" title="复核装箱"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
			<div class="easyui-layout" data-options="fit:true">
				<div  data-options="region:'north',border:false" >
					<@p.toolbar id="recheckLoadBoxtoolbar" listData=[
	                             <#-- {"title":"新箱","iconCls":"icon-add","action":"billomrecheck.getConLabelNo()", "type":0}, -->
	                             {"title":"装箱","iconCls":"icon-download","action":"billomrecheck.packageBox()", "type":0},
	                             {"title":"取消","iconCls":"icon-cancel","action":"billomrecheck.closeWindow('check_box_dialog')","type":0}
		                       ]
					  />
					<div class="search-div" style="display:none;">
					 	<form name="checkboxsearchForm" id="checkboxsearchForm" method="post" class="city-form">
					 		<table>
					     		<tr>
		                 			<td class="common-td">箱号：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:130px" name="boxNo" id="boxNoNew" /></td>
		                 		</tr>
					     	</table>
					 	</form>	
					 </div>
				</div>
				<div  data-options="region:'center',border:false" >
					<@p.datagrid id="check_box_dataGrid"    defaultColumn=""  
						isHasToolBar="false" divToolbar="" height="390"    pageSize="10" 
						onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect = "true"
						rownumbers="true"  emptyMsg=""
						columnsJsonList="[
						
							  {field : 'itemId',hidden : true},
							  {field : 'packQty',hidden : true},
							  {field : 'expNo',hidden : true},
							  {field : 'expType',hidden : true},
							  {field : 'ownerNo',hidden : true},
							  {field : 'lineNo',hidden : true},
							  {field : 'divideId',hidden : true},
							  {field : 'poNo',hidden : true},
							  {field : 'boxNo',hidden : true},
							  
					          {field : 'itemNo',title : '商品编码',width : 120,align:'left'},
					          {field : 'itemName',title : '商品名称',width : 150,align:'left'},
					          {field : 'itemQty',title : '数量',width : 60,align:'right'},
					          {field : 'packageNum',title : '装箱数量',width : 60,formatter:billomrecheck.realQtyFormatter,align:'right',
					          			editor:{
					          				type:'numberbox',
					          				options:{
					          					value:0
					          				}
					          				
					          			}},
					          {field : 'colorName',title : '颜色',width : 100,align:'left'},
					          {field : 'sizeNo',title : '尺码',width : 120,align:'left'}
					         ]"
						rownumbers="true" jsonExtend="{}" />
				</div>
			</div>
	</div>
	
	
	<#-- RF复核装箱 -->
	<div id="check_box_dialog_rf" class="easyui-dialog" title="RF复核装箱"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false" >
					<@p.toolbar id="recheckLoadBoxtoolbarRf" listData=[
	                         {"title":"封箱","iconCls":"icon-download","action":"billomrecheck.packageBoxRf()", "type":0},
	                         {"title":"取消","iconCls":"icon-cancel","action":"billomrecheck.closeWindow('check_box_dialog_rf')","type":0}
		                  ]
					 />			
					 
					 <div class="search-div">
					 	<form name="divideMainInfoRfForm" id="divideMainInfoRfForm" method="post" class="city-form">
							<table>
						    	<tr>
						        	<td class="common-td">单据编号：</td>
						            <td><input class="easyui-validatebox ipt" style="width:120px" name="recheckNo" id="recheckNoRf" readOnly="true" /></td>
						            <td class="common-td blank">分货单号：</td>
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
	
	<#-- 分货单号选择 Begin -->
	<div id="divide_no_dialog" class="easyui-dialog" title="分货单选择"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
			<div class="easyui-layout" data-options="fit:true">
				<div  data-options="region:'north',border:false" >
					<@p.toolbar id="dividetoolbar" listData=[
	                             {"title":"查询","iconCls":"icon-search","action":"billomrecheck.divideQuery()", "type":0},
	                             {"title":"清除","iconCls":"icon-remove","action":"billomrecheck.clearFormById('divideNoSearchForm')", "type":0},
	                             {"title":"确认","iconCls":"icon-ok","action":"billomrecheck.selectDivide()", "type":0},
	                             {"title":"取消","iconCls":"icon-cancel","action":"billomrecheck.closeWindow('divide_no_dialog')","type":0}
		                       ]
					  />
					<div class="search-div">
					 	<form name="divideNoSearchForm" id="divideNoSearchForm" method="post" class="city-form">
					 		<table>
					     		<tr>
		                 			<td class="common-td">箱号：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:130px" name="boxNo" id="boxNo"   /></td>
		                 			<td class="common-td blank">商品编码：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:130px" name="itemNo"   /></td>
		                 			<td class="common-td blank">分货日期：</td>
		                 			<td><input class="easyui-datebox" style="width:130px" name="startDivideDate" id="startDivideDate" /></td>
		                 			<td class="common-line">&nbsp;&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		                 			<td><input class="easyui-datebox" style="width:130px" name="endDivideDate" id="endDivideDate" /></td>
		                 		</tr>
					     	</table>
					 	</form>	
					 </div>
				</div>
				<div  data-options="region:'center',border:false" >
				    <@p.datagrid id="divide_no_dataGrid"    defaultColumn=""  
						isHasToolBar="false" divToolbar="" height="500"  pageSize="20" checkOnSelect="true"  selectOnCheck="true"
						onClickRowEdit="false" onClickRowAuto="false" pagination="true" singleSelect = "true"
						rownumbers="true" 
						columnsJsonList="[
							  {field : 'id',checkbox :true},	
					          {field : 'divideNo',title : '分货单号',width : 180,align:'left'},
					          {field : 'statusStr',title : '状态',width : 100,align:'left'},
					          {field : 'boxTatalCount',title : '总箱数',width : 80,align:'right'},
					          {field : 'itemTotalCount',title : '总件数',width : 80,align:'right'},
					          {field : 'assignNames',title : '分货人',width : 100,align:'left'},
					          {field : 'createtm',title : '创建日期',width : 130},
					          {field : 'locno',hidden:true},
					          {field : 'expDate',hidden:true},
					          {field : 'status',hidden:true}
					         ]"
						rownumbers="true" jsonExtend="{onDblClickRow:function(rowIndex, rowData){
					    //billomrecheck.setDivideNo(rowData,'add');
					}}" />
		    	</div>
			</div>
		</div>
		<#-- 复核人选择Begin -->
		<div id="check_user_dialog" class="easyui-dialog" title="复核人选择"  
		    style="width:500px;height:365px;"   
			data-options="modal:true,resizable:false,draggable:true,
			collapsible:false,closed:true,minimizable:false,maximizable:false">
			<div class="easyui-layout" data-options="fit:true">
				<div  data-options="region:'north',border:false" >
					<div class="search-div" style="border:none;">
					 	<form method="post" class="city-form">
					 		<table>
					     		<tr>
					     			<td class="common-td">复核人：</td>
					     			<td><input class="easyui-combobox" data-options="editable:false" style="width:110px" id="checkUser" /></td>
					     			<td>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:billomrecheck.check();"  class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确定</a></td>
					     		</tr>
					     	</table>
					 	</form>	
					 </div>
				</div>
			</div>
	</div>
</body>
</html>