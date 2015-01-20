<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>初盘回单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billchcheck/receiptcheck.js?version=1.1.1.5"></script>
</head>

<body class="easyui-layout">
	
	<#-- 工具菜单div -->
    <div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
	    	{"title":"查询","iconCls":"icon-search","action":"receiptcheck.searchArea()", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"receiptcheck.searchClear()", "type":0},
	        {"title":"修改","iconCls":"icon-edit","action":"receiptcheck.checkDtl();","type":2},
	    	{"title":"审核","iconCls":"icon-aduit","action":"receiptcheck.check();","type":4}
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('初盘回单')","type":0}
	     ]/>
	</div>
	
	
	<div data-options="region:'center',border:false">
		
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
        
        <div  data-options="region:'north',border:false" >
			<div id="formDiv" style="padding:10px;">
				<form id='receiptCheckForm' name="receiptCheckForm" class="city-form">
					<table>
						<tr>
							<td class="common-td">盘点单号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo"/></td>
							<td class="common-td blank">创建日期：</td>
							<td><input class="easyui-datebox" style="width:120px" name="createtm_start" id="createtm_start"/> </td>
							<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
							<td><input class="easyui-datebox" style="width:120px" name="createtm_end" id="createtm_end"/></td>
							<td class="common-td blank">盘点人员：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="assignNo" id="recheckWorker"/></td>
						</tr>
						<tr>
							<td class="common-td">状态：</td>
                     		<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
							<td class="common-td blank">计划单号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo"/></td>
							<td class="common-td blank">盘点日期：</td>
                 			<td><input class="easyui-datebox" style="width:120px" name="planDateStart" id="startPlanDateCondition" /></td>
                 			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                 			<td><input class="easyui-datebox" style="width:120px" name="planDateEnd" id="endPlanDateCondition" /></td>
						</tr>
						<tr>
							<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
	             			<td>
	             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/>
	             			</td>
							<td class="common-td blank">所属品牌：</td>
							<td colspan="2"><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		
		<div data-options="region:'center',border:false">
			<!--显示列表-->
			<@p.datagrid id="checkDataGrid"  loadUrl="" saveUrl=""   defaultColumn="" 
				isHasToolBar="false" divToolbar="" onClickRowEdit="false" title="初盘回单列表" 
				pagination="true" rownumbers="true"  singleSelect = "false" showFooter="true" emptyMsg=""
				columnsJsonList="[
						{field : ' ',checkbox:true},
						{field : 'status',title : '状态',width : 120,align:'left',formatter:receiptcheck.statusFormatter},
						{field : 'planNo',title : '计划单号',width : 180},
						{field : 'checkNo',title : '初盘单号',width : 180},
						{field : 'planDate',title : '盘点日期',width :135},
						{field : 'createtm',title : '创建时间',width :135},
						{field : 'cellCount',title : '总储位数',width : 130,align:'right'},
						{field : 'itemCount',title : '总商品数',width : 100,align:'right'},
						{field : 'realCount',title : '已盘点数量',width : 100,align:'right'},
						{field : 'differlCount',title : '差异数量',width : 100,align:'right'},
						{field : 'assignNo',title : '盘点人员',width : 100,align:'left'},
						{field : 'assignName',title : '盘点人员名称',width : 100}
						]" 
						jsonExtend='{onLoadSuccess:function(data){
							receiptcheck.onLoadSuccess(data);
						},onSelect:function(rowIndex, rowData){
					    	// 触发点击方法  调JS方法
					    },onDblClickRow:function(rowIndex, rowData){
					   		receiptcheck.loadDetail_view(rowData)
			}}'/>
		</div>
		
	</div>

	<#-- 初盘回单明细  -->
	<div id="checkDetailDialog" class="easyui-window" title="初盘回单明细" 
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		maximized:true,minimizable:false,maximizable:false">
		
		<div class="easyui-layout" data-options="fit:true">
				 <div data-options="region:'north',border:false">
					<@p.toolbar  id="edittoolbarcancel"  listData=[
					 		{"id":"info_close","title":"取消","iconCls":"icon-cancel","action":"receiptcheck.closeCheckDetailDialog()", "type":0}
					 ]/>
					<form id="checkDetailForm" class="city-form" style="padding:10px;">
						  <input type="hidden" name="planType" id="checkPlanType">
						  <input type="hidden" name="planNo" id="checkPlanNo">
						  <input type="hidden" name="limitBrandFlag" id="checkLimitBrandFlag">
						  <input type="hidden" name="brandNo" id="checkBrandNo">
						  <table>
						  		<tr>
						  			<td class="common-td">初盘单号：</td>
						  			<td><input class="easyui-validatebox ipt" style="width:120px" id="detailCheckNo" name="checkNo"  disabled="disabled"/></td>
						  			<td class="common-td blank">盘点日期：</td>
						  			<td><input class="easyui-validatebox ipt" style="width:120px" name="createtm" disabled="disabled"/></td>
						  			<td class="common-td blank">总储位数：</td>
						  			<td><input class="easyui-validatebox ipt" style="width:120px" name="cellCount" disabled="disabled"/></td>
						  			<td class="common-td blank">总商品数：</td>
						  			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemCount" disabled="disabled"/></td>
						  		</tr>
						  		<tr>
						  			<td>盘点人员：</td>
						  			<td colspan="7"><input class="easyui-validatebox ipt" style="width:120px" name="assignNo" disabled="disabled"/></td>
						  		</tr>
						  </table>
					 </form>
			 	 </div>
				 <div data-options="region:'center',border:false">
			 	 	<div id="edittag" class="easyui-tabs" data-options="region:'north',fit:true,border:false">
			 	 		<div title="零散明细"> 
						  <div class="easyui-layout"  data-options="fit:true">
						         <@p.toolbar  id="edittoolbaritem"  listData=[
								    	{"id":"add_row","title":"新增商品","iconCls":"icon-add-dtl","action":"receiptcheck.openItemSlect()", "type":0},
								    	{"title":"模板下载","iconCls":"icon-download","action":"receiptcheck.downloadTemp()","type":0},
								    	{"title":"导入明细","iconCls":"icon-import","action":"receiptcheck.showImport()", "type":0},
								        {"id":"info_close","title":"删除明细","iconCls":"icon-del-dtl","action":"receiptcheck.deleterow()", "type":0},
								        {"title":"按计划保存","iconCls":"icon-save-dtl","action":"receiptcheck.saveByPlan('N')", "type":0},
								        {"title":"实盘置零","iconCls":"icon-save-dtl","action":"receiptcheck.resetPlan('N')", "type":0},
								        {"id":"checkDtlBtn","title":"保存明细","iconCls":"icon-save-dtl","action":"receiptcheck.saveChckeDtl()", "type":0},
								        {"title":"导出明细","iconCls":"icon-export","action":"receiptcheck.doExport('N')", "type":0}
									]/>
								 <div data-options="region:'center',border:false">
									<@p.datagrid id="checkDataGridDetail" loadUrl="" saveUrl=""   defaultColumn="" emptyMsg=""
									  	isHasToolBar="false" divToolbar="#edittoolbaritem" onClickRowEdit="false" showFooter="true"
										pagination="true" rownumbers="true"  singleSelect = "true" title=""
										onClickRowAuto="false" checkOnSelect="false"  selectOnCheck="false"
										columnsJsonList="[
											{field : 'cellNo',title : '储位',width : 100, align:'left',
												editor:{
												   type:'validatebox'
												}},
											{field : 'itemNo',title : '商品编码',width : 150},
											{field : 'itemName',title : '商品名称',width : 180, align:'left'},
											{field : 'colorName',title : '颜色',width : 120, align:'left'},
											{field : 'sizeNo',title : '尺码',width : 80,align:'left'},
											{field : 'itemQty',title : '计划数量',width : 80, align:'right'},
											{field : 'checkQty',title : '实盘数量',width : 80, align:'right',
												editor:{
														  type:'numberbox'
												}},
											{field : 'diffQty',title : '差异数量',width : 100, align:'right'}
									]"
									
									jsonExtend='{onClickRow:function(rowIndex, rowData){
					                	  // 触发点击方法  调JS方法
					                   	 receiptcheck.onClickRowCheckDtl(rowIndex,rowData);
					                }}'/>
				                
							 </div>
							</div>
							
					  </div>
					  <div title="整箱明细"> 
						  <div class="easyui-layout"  data-options="fit:true">
						           <@p.toolbar  id="edittoolbarboxitem"  listData=[
								        {"title":"按计划保存","iconCls":"icon-save-dtl","action":"receiptcheck.saveByPlan('Y')", "type":0},
								        {"title":"实盘置零","iconCls":"icon-save-dtl","action":"receiptcheck.resetPlan('Y')", "type":0},
								        {"title":"按箱计划保存","iconCls":"icon-save-dtl","action":"receiptcheck.saveByPlanBox('Y')", "type":0},
								        {"title":"按箱实盘置零","iconCls":"icon-save-dtl","action":"receiptcheck.resetPlanBox('Y')", "type":0},
								        {"title":"导出明细","iconCls":"icon-export","action":"receiptcheck.doExport('Y')", "type":0}
									]/>
								 <div data-options="region:'center',border:false">
									<@p.datagrid id="checkDataGridBoxDetail" loadUrl="" saveUrl=""   defaultColumn="" emptyMsg=""
									  	isHasToolBar="false" divToolbar="#edittoolbarboxitem" onClickRowEdit="false" showFooter="true"
										pagination="true" rownumbers="true"  singleSelect = "true" title=""
										onClickRowAuto="false" checkOnSelect="false"  selectOnCheck="false"
										columnsJsonList="[
											{field : 'cellNo',title : '储位',width : 100, align:'left',
												editor:{
												   type:'validatebox'
												}},
										    {field : 'labelNo',title : '箱号',width : 150,align:'left'},
											{field : 'itemNo',title : '商品编码',width : 150},
											{field : 'itemName',title : '商品名称',width : 180, align:'left'},
											{field : 'colorName',title : '颜色',width : 120, align:'left'},
											{field : 'sizeNo',title : '尺码',width : 80,align:'left'},
											{field : 'itemQty',title : '计划数量',width : 80, align:'right'},
											{field : 'checkQty',title : '实盘数量',width : 80, align:'right',
												editor:{
														  type:'numberbox'
												}},
											{field : 'diffQty',title : '差异数量',width : 100, align:'right'}
									]"
									
									jsonExtend='{onClickRow:function(rowIndex, rowData){
					                	  // 触发点击方法  调JS方法
					                   	 //receiptcheck.onClickRowCheckDtl(rowIndex,rowData);
					                }}'/>
				                
							 </div>
							</div>
							
					  </div>
					</div>
          </div>	
		</div>
	</div>

    <#-- 查看初盘回单明细  -->
	<div id="checkDetailDialog_view" class="easyui-window" title="初盘回单明细"  
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		maximized:true,minimizable:false,maximizable:false">
		
		<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<@p.toolbar  id="edittoolbarcancel_view"  listData=[
							{"title":"导出","iconCls":"icon-export","action":"receiptcheck.exportDtl();","type":0},
					 		{"id":"info_close","title":"取消","iconCls":"icon-cancel","action":"receiptcheck.closeCheckDetailDialog_view()", "type":0}
					 ]/>
					<form id="checkDetailForm_view" class="city-form" style="padding:10px;">
						  <input type="hidden" name="planType" id="checkPlanType">
						  <input type="hidden" name="planNo" id="checkPlanNo">
						  <input type="hidden" name="limitBrandFlag" id="checkLimitBrandFlag">
						  <input type="hidden" name="brandNo" id="checkBrandNo">
						  <table>
						  		<tr>
						  			<td class="common-td">初盘单号：</td>
						  			<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo" id="checkNoId" disabled="disabled"/></td>
						  			<td class="common-td blank">盘点日期：</td>
						  			<td><input class="easyui-validatebox ipt" style="width:120px" name="createtm" disabled="disabled"/></td>
						  			<td class="common-td blank">总储位数：</td>
						  			<td><input class="easyui-validatebox ipt" style="width:120px" name="cellCount" disabled="disabled"/></td>
						  			<td class="common-td blank">总商品数：</td>
						  			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemCount" disabled="disabled"/></td>
						  		</tr>
						  		<tr>
						  			<td>盘点人员：</td>
						  			<td colspan="7"><input class="easyui-validatebox ipt" style="width:120px" name="assignNo" disabled="disabled"/></td>
						  		</tr>
						  </table>
					 </form>
			 	 </div>
			 	 <div data-options="region:'center',border:false">
			 	 	<div id="viewtag" class="easyui-tabs" data-options="region:'north',fit:true,border:false">
			 	 		<div title="零散明细"> 
			 	 			<div class="easyui-layout"  data-options="fit:true">
								 <div data-options="region:'center',border:false">
									<@p.datagrid id="checkDataGridDetail_view" loadUrl="" saveUrl=""   defaultColumn="" 
									  	isHasToolBar="false" divToolbar="#checkDtlBtnArea" onClickRowEdit="false" showFooter="true"
										pagination="true" rownumbers="true"  singleSelect = "true" title="初盘回单明细列表" emptyMsg=""
										columnsJsonList="[
											{field : 'cellNo',title : '储位',width : 100, align:'left',sortable:true},
											{field : 'itemNo',title : '商品编码',width : 150,sortable:true},
											{field : 'itemName',title : '商品名称',width : 150, align:'left',sortable:true},
											{field : 'colorName',title : '颜色',width : 120, align:'left',sortable:true},
											{field : 'sizeNo',title : '尺码',width : 80,align:'left',sortable:true},
											{field : 'itemQty',title : '计划数量',width : 80, align:'right',sortable:true},
											{field : 'checkQty',title : '初盘数量',width : 80, align:'right',sortable:true},
											{field : 'diffQty',title : '初盘差异数量',width : 100, align:'right',sortable:true},
											{field : 'recheckQty',title : '复盘数量',width : 80, align:'right',sortable:true},
											{field : 'recheckDiffQty',title : '复盘差异数量',width : 100, align:'right',sortable:true},
											{field : 'checkType',title : '盘点类型',width : 80, align:'center',formatter:receiptcheck.checkTypeFormatter,sortable:true},
											{field : 'realWorker',title : '盘点人员',width : 120,align:'left',sortable:true},
											{field : 'realWorkerName',title : '盘点人员名称',width : 100,align:'left',sortable:true}
									]"/>
								</div>	
							</div>
			 	 		</div>
			 	 		<div title="整箱明细"> 
			 	 			<div class="easyui-layout"  data-options="fit:true">
								 <div data-options="region:'center',border:false">
									<@p.datagrid id="checkDataGridDetailBox_view" loadUrl="" saveUrl=""   defaultColumn="" 
									  	isHasToolBar="false" divToolbar="#checkDtlBtnArea" onClickRowEdit="false" showFooter="true"
										pagination="true" rownumbers="true"  singleSelect = "true" title="初盘回单明细列表" emptyMsg=""
										columnsJsonList="[
											{field : 'cellNo',title : '储位',width : 100, align:'left',sortable:true},
											{field : 'labelNo',title : '箱号',width : 150,align:'left'},
											{field : 'itemNo',title : '商品编码',width : 150,sortable:true},
											{field : 'itemName',title : '商品名称',width : 150, align:'left',sortable:true},
											{field : 'colorName',title : '颜色',width : 120, align:'left',sortable:true},
											{field : 'sizeNo',title : '尺码',width : 80,align:'left',sortable:true},
											{field : 'itemQty',title : '计划数量',width : 80, align:'right',sortable:true},
											{field : 'checkQty',title : '初盘数量',width : 80, align:'right',sortable:true},
											{field : 'diffQty',title : '初盘差异数量',width : 100, align:'right',sortable:true},
											{field : 'recheckQty',title : '复盘数量',width : 80, align:'right',sortable:true},
											{field : 'recheckDiffQty',title : '复盘差异数量',width : 100, align:'right',sortable:true},
											{field : 'checkType',title : '盘点类型',width : 80, align:'center',formatter:receiptcheck.checkTypeFormatter,sortable:true},
											{field : 'realWorker',title : '盘点人员',width : 120,align:'left',sortable:true},
											{field : 'realWorkerName',title : '盘点人员名称',width : 120,align:'left',sortable:true}
									]"/>
								</div>	
							</div>
			 	 		</div>
			 	 		<div title="差异明细">
			 	 			<div class="easyui-layout"  data-options="fit:true">
			 	 				<div data-options="region:'north',border:false">
				 	 				<@p.toolbar  id="detailExp"  listData=[
						 					{"title":"导出差异明细","iconCls":"icon-export","action":"receiptcheck.do_export()", "type":0}
									 ]/>
								 </div>
								 <div data-options="region:'center',border:false">
									<@p.datagrid id="checkDataGridDetail_view_dif" loadUrl="" saveUrl=""   defaultColumn="" emptyMsg=""
									  	isHasToolBar="false" divToolbar="#checkDtlBtnArea" onClickRowEdit="false" showFooter="true"
										pagination="true" rownumbers="true"  singleSelect = "true" title="初盘回单明细列表"
										columnsJsonList="[
											{field : 'cellNo',title : '储位',width : 100, align:'left',sortable:true},
											{field : 'itemNo',title : '商品编码',width : 150,sortable:true},
											{field : 'itemName',title : '商品名称',width : 150, align:'left',sortable:true},
											{field : 'colorName',title : '颜色',width : 100, align:'left',sortable:true},
											{field : 'sizeNo',title : '尺码',width : 100,align:'left',sortable:true},
											{field : 'itemQty',title : '计划数量',width : 100, align:'right',sortable:true},
											{field : 'checkQty',title : '初盘数量',width : 100, align:'right',sortable:true},
											{field : 'diffQty',title : '初盘差异数量',width : 100, align:'right',sortable:true},
											{field : 'recheckQty',title : '复盘数量',width : 100, align:'right',sortable:true},
											{field : 'recheckDiffQty',title : '复盘差异数量',width : 100, align:'right',sortable:true},
											{field : 'checkType',title : '盘点类型',width : 100, align:'center',formatter:receiptcheck.checkTypeFormatter,sortable:true}
									]"/>
								</div>	
							</div>
			 	 		</div>
			 	 	</div>
			 	 </div>
			</div>	
		</div>
	</div>


	<#-- 商品选择  -->
	<div id="openUIItem" class="easyui-window" style="width:680px;height:450px;"
	 	data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		maximized:false,minimizable:false,maximizable:false">
	 	
	 	<div class="easyui-layout" data-options="fit:true">
	 	
		 	<div data-options="region:'north',border:false">
			 	<#-- 工具菜单div -->
				<div id="itemSelectDiv">
					<@p.toolbar  id="edittoolbardetal"  listData=[
						{"title":"查询","iconCls":"icon-search","action":"receiptcheck.searchItem()", "type":0},
						{"title":"清除","iconCls":"icon-remove","action":"receiptcheck.searchItemClear()", "type":0},
						{"title":"确定","iconCls":"icon-ok","action":"receiptcheck.selectItemOK()", "type":0},
						{"title":"取消","iconCls":"icon-cancel","action":"receiptcheck.closeUI()", "type":0}
					]/>
				</div>
				
				<div id="itemSearchDiv" style="padding:10px;">
					<form name="itemSearchForm" id="itemSearchForm" metdod="post">
						<input type="hidden" name="locno" />
						<table>
							<tr>
								<td class="common-td">商品编码：</td>
							  	<td><input class="easyui-validatebox ipt" style="width:130px" name="itemNo" id="itemNo"   disable="true"/></td>
							  	<td class="common-td blank">商品条码：</td>
							  	<td><input class="easyui-validatebox ipt" style="width:130px" name="barcode" id="barcode"   disable="true"/></td>
							  	<td class="common-td blank">商品名称：</td>
							  	<td><input class="easyui-validatebox ipt" style="width:130px" name="itemName" id="itemName"   disable="true"/></td>
							</tr>
						</table>
					</form>
				</div>
			</div>
			
			<div data-options="region:'center',border:false">
				
				<#-- 工具菜单div 
				<div id="itemSelectCellDiv" style="display:none;">
					储位：<input class="easyui-combobox" style="width:120px" name="cellNo" id="cellNo" />
				</div>
				-->
				<#-- 商品选择数据列表div -->
		        <@p.datagrid id="dataGridJGItem"  loadUrl=""  saveUrl=""  defaultColumn="" 
					isHasToolBar="false" divToolbar="" height="300"  
					onClickRowEdit="false"  pagination="true" singleSelect = "false" title="商品明细"
					rownumbers="false"
					columnsJsonList="[
						{field : 'id',checkbox:true},
					    {field : 'itemNo',title : '商品编码 ',width : 150},
					    {field : 'itemName',title : '商品名称',width : 150, align:'left'},
					    {field : 'barcode',title : '商品条码',width : 150, align:'left'},
					    {field : 'sizeNo',title : '尺码',width : 90,align:'left'},
					     {field : 'colorName',title : '颜色',width : 90, align:'left'},
					    ]" 
						jsonExtend='{onDblClickRow:function(rowIndex, rowData){
				        	// 触发点击方法  调JS方法
				            //billimcheck.selectItemOK(rowData);
				 }}'/>
			 </div>
		 
		 </div>
	</div> 
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
	</div>

</body>
</html>