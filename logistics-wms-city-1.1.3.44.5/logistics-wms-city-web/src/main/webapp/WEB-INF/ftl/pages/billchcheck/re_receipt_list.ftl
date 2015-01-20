<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>复盘回单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billchcheck/re_receiptcheck.js?version=1.1.1.6"></script>
   
</head>
<body class="easyui-layout">

	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar" listData=[
			{"title":"查询","iconCls":"icon-search","action":"re_receiptcheck.searchArea()", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"re_receiptcheck.searchClear()", "type":0},
	    	{"title":"修改","iconCls":"icon-edit","action":"re_receiptcheck.checkDtl('','edit');","type":0},
	    	{"title":"审核","iconCls":"icon-aduit","action":"re_receiptcheck.check();","type":0},
	    	{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('复盘回单')","type":0}
	    ]/>
	</div>
	
	
	<div data-options="region:'center',border:false">
			
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
        <div  data-options="region:'north',border:false" >
        
		  	<div id="formDiv" style="padding:10px;">
				<form id='re_receiptcheckForm' class="city-form">
					<table>
						<tr>
							<td class="common-td">盘点单号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo"/></td>
							<td class="common-td blank">创建日期：</td>
							<td><input class="easyui-datebox" style="width:120px" name="createtm_start" id="createtm_start"/> </td>
							<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
							<td><input class="easyui-datebox" style="width:120px" name="createtm_end" id="createtm_end"/></td>
							<td class="common-td blank">盘点人员：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="recheckWorker" id="recheckWorker"/></td>
						</tr>
						<tr>
							<td class="common-td">状态：</td>
                     		<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
							<td class="common-td">计划单号：</td>
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
    		<@p.datagrid id="reCheckDataGrid"  loadUrl="" saveUrl=""   defaultColumn="" 
					isHasToolBar="false" divToolbar="" onClickRowEdit="false"   title="复盘回单列表"
			        pagination="true" rownumbers="true"  singleSelect = "false" showFooter="true"
				    columnsJsonList="[
								{field : ' ',checkbox:true},
								{field : 'status',title : '状态',width : 100, align:'left',formatter:re_receiptcheck.statusFormatter},
								{field : 'planNo',title : '计划单号',width : 180},
								{field : 'recheckNo',title : '复盘单号',width : 180},
								{field : 'checkNo',title : '初盘单号',width :180},
							    {field : 'planDate',title : '盘点日期',width :120},
								{field : 'cellCount',title : '总储位数',width : 100, align:'right'},
								{field : 'itemCount',title : '总商品数',width : 100, align:'right'},
								{field : 'realCount',title : '已盘点数量',width : 100,align:'right'},
								{field : 'differlCount',title : '差异数量',width : 100,align:'right'},
								{field : 'checkWorker',title : '盘点人员',width : 100, align:'left'},
								{field : 'checkWorkerName',title : '盘点人员名称',width : 100, align:'left'}
								
				                 ]" 
					           jsonExtend='{onLoadSuccess:function(data){
					           		re_receiptcheck.onLoadSuccess(data);
					           },onSelect:function(rowIndex, rowData){
			                            // 触发点击方法  调JS方法
			                   },onDblClickRow:function(rowIndex, rowData){
			                   	  re_receiptcheck.checkDtl(rowData,"view");
			}}'/>
		</div>	
		
	</div>

	<#-- 复盘回单明细  -->
	<div id="reCheckDetailDialog" class="easyui-window" title="复盘回单明细" 
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		maximized:true,minimizable:false,maximizable:false">
		
		<div class="easyui-layout" data-options="fit:true">
		
			<div data-options="region:'north',border:false">
				<@p.toolbar  id="edittoolbarcancel"  listData=[
						{"id":"info_close","title":"取消","iconCls":"icon-cancel","action":"re_receiptcheck.closereCheckDetailDialog()", "type":0},
						{"id":"save_main","title":"保存","iconCls":"icon-save","action":"re_receiptcheck.saveDetail()", "type":0}
				]/>
				
				<form id="reCheckDetailForm" class="city-form" style="padding:10px;">
					<input type="hidden" name="planType" id="checkPlanType">
					<input type="hidden" name="planNo" id="checkPlanNo">
					<input type="hidden" name="limitBrandFlag" id="checkLimitBrandFlag">
					<input type="hidden" name="brandNo" id="checkBrandNo">
					<table>
						<tr>
							<td class="common-td">复盘单号：</td>
						  	<td><input class="easyui-validatebox ipt" style="width:120px" id="detailRecheckNo" name="recheckNo" disabled="disabled"/></td>
						  	<td class="common-td blank">初盘单号：</td>
						  	<td><input class="easyui-validatebox ipt" style="width:120px" id="detailCheckNo" name="checkNo" disabled="disabled"/></td>
						  	<td class="common-td blank">盘点日期：</td>
						  	<td><input class="easyui-validatebox ipt" style="width:120px" name="checkDate" disabled="disabled"/></td>
						  	<td class="common-td blank">总储位数：</td>
						  	<td><input class="easyui-validatebox ipt" style="width:120px" name="cellCount" disabled="disabled"/></td>
						  </tr>
						  <tr>
						  	<td class="common-td blank">总商品数：</td>
						  	<td><input class="easyui-validatebox ipt" style="width:120px" name="itemCount" disabled="disabled"/></td>
						  	<td class="common-td blank">盘点人员：</td>
						  	<td colspan="5"><input class="easyui-combobox ipt" style="width:120px" id="checkWorker" data-options="editable:false"  name="checkWorker"/></td>
						  </tr>
					</table>
				</form>
			</div>
		
			<div  data-options="region:'center',border:false">
					<div id="edit_add" class="easyui-layout"  data-options="fit:true">
						<div data-options="region:'center',border:false">
						      <div id="edittag" class="easyui-tabs" data-options="region:'north',fit:true,border:false">
						 	 		<div title="零散明细"> 
									  <div class="easyui-layout"  data-options="fit:true">
									         <@p.toolbar  id="edittoolbardetal"  listData=[
											    	{"id":"add_row","title":"新增商品","iconCls":"icon-add-dtl","action":"re_receiptcheck.openItemSlect()", "type":0},
											        {"id":"info_close","title":"删除明细","iconCls":"icon-del-dtl","action":"re_receiptcheck.deleterow()", "type":0},
											        {"title":"按计划保存","iconCls":"icon-save-dtl","action":"re_receiptcheck.saveByPlan('N')", "type":0},
										       	 	{"title":"实盘置零","iconCls":"icon-save-dtl","action":"re_receiptcheck.resetPlan('N')", "type":0},
											        {"id":"checkDtlBtn","title":"保存明细","iconCls":"icon-save-dtl","action":"re_receiptcheck.saveChckeDtl()", "type":0}
											 ]/>
											 <div data-options="region:'center',border:false">
												<@p.datagrid id="reCheckDataGridDetail" loadUrl="" saveUrl=""   defaultColumn="" 
															isHasToolBar="false" divToolbar="#edittoolbardetal" onClickRowEdit="true"  height="300"  
															pagination="true" rownumbers="true"  singleSelect = "true"  title=""
															onClickRowAuto="false" checkOnSelect="false"  selectOnCheck="false"
															columnsJsonList="[
																{field : 'cellNo',title : '储位',width : 120, align:'left',
																	editor:{
																	   type:'validatebox'
																	}},
																{field : 'itemNo',title : '商品编码',width : 150, align:'left'},
																{field : 'itemName',title : '商品名称',width : 150, align:'left'},
																{field : 'colorName',title : '颜色',width : 100, align:'left'},
																{field : 'sizeNo',title : '尺码',width : 100, align:'left'},
																{field : 'itemQty',title : '计划数量',width : 100, align:'right'},
																{field : 'recheckQty',title : '实盘数量',width : 100, align:'right',
																	editor:{
																		type:'numberbox'
																	}},
																{field : ' ',title : '差异数量', align:'right',width : 100,formatter:re_receiptcheck.difQtyFormatter}
															]"
															jsonExtend='{onClickRow:function(rowIndex, rowData){
										                	  // 触发点击方法  调JS方法
										                   	 re_receiptcheck.onClickRowCheckDtl(rowIndex,rowData);
										                	}}'
											      />
							                
										   </div>
										</div>
										
								  </div>
								  <div title="整箱明细"> 
									  <div class="easyui-layout"  data-options="fit:true">
									         <@p.toolbar  id="edittoolbarBoxdetal"  listData=[
											        {"title":"按计划保存","iconCls":"icon-save-dtl","action":"re_receiptcheck.saveByPlan('Y')", "type":0},
										       	 	{"title":"实盘置零","iconCls":"icon-save-dtl","action":"re_receiptcheck.resetPlan('Y')", "type":0}
											 ]/>
											 <div data-options="region:'center',border:false">
												<@p.datagrid id="reCheckDataGridBoxDetail" loadUrl="" saveUrl=""   defaultColumn="" 
															isHasToolBar="false" divToolbar="#edittoolbarBoxdetal" onClickRowEdit="true"  height="300"  
															pagination="true" rownumbers="true"  singleSelect = "true"  title=""
															onClickRowAuto="false" checkOnSelect="false"  selectOnCheck="false"
															columnsJsonList="[
																{field : 'cellNo',title : '储位',width : 120, align:'left',
																	editor:{
																	   type:'validatebox'
																	}},
																{field : 'labelNo',title : '箱号',width : 150, align:'left'},
																{field : 'itemNo',title : '商品编码',width : 150, align:'left'},
																{field : 'itemName',title : '商品名称',width : 150, align:'left'},
																{field : 'colorName',title : '颜色',width : 100, align:'left'},
																{field : 'sizeNo',title : '尺码',width : 100, align:'left'},
																{field : 'itemQty',title : '计划数量',width : 100, align:'right'},
																{field : 'recheckQty',title : '实盘数量',width : 100, align:'right',
																	editor:{
																		type:'numberbox'
																	}},
																{field : ' ',title : '差异数量', align:'right',width : 100,formatter:re_receiptcheck.difQtyFormatter}
															]"
															jsonExtend='{onClickRow:function(rowIndex, rowData){
										                	  // 触发点击方法  调JS方法
										                   	 //re_receiptcheck.onClickRowCheckDtl(rowIndex,rowData);
										                	}}'
											      />
							                
										   </div>
										</div>
										
								  </div>
								</div>
						
						   
														
						</div>
					</div>
					
					<div id="view" class="easyui-layout" data-options="fit:true">
								<div data-options="region:'center',border:false">
								      <div id="viewtag" class="easyui-tabs" data-options="region:'north',fit:true,border:false">
								 	 		<div title="零散明细"> 
											  <div class="easyui-layout"  data-options="fit:true">
											         
													 <div data-options="region:'center',border:false">
														      <@p.datagrid id="reCheckDataGridDetail_view" loadUrl="" saveUrl=""   defaultColumn="" 
																		isHasToolBar="false" divToolbar="" onClickRowEdit="false"  height="300"  
																	    pagination="true" rownumbers="true"  singleSelect = "true"  title="" showFooter="true"
																		columnsJsonList="[
																				{field : 'cellNo',title : '储位',width : 100, align:'left',sortable:true},
																				{field : 'itemNo',title : '商品编码',width : 150, align:'left',sortable:true},
																				{field : 'itemName',title : '商品名称',width : 150, align:'left',sortable:true},
																				{field : 'colorName',title : '颜色',width : 100, align:'left',sortable:true},
																				{field : 'sizeNo',title : '尺码',width : 100, align:'left',sortable:true},
																				{field : 'itemQty',title : '计划数量',width : 100, align:'right',sortable:true},
																				{field : 'recheckQty',title : '实盘数量',width : 100, align:'right',sortable:true,
																					editor:{
																					    type:'numberbox'
																					}},
																				{field : 'diffQty',title : '差异数量', align:'right',width : 100,sortable:true},
																			{field : 'checkWorker',title : '盘点人员',width : 100, align:'left'},
																			{field : 'checkWorkerName',title : '盘点人员名称',width : 100, align:'left'}
																		 ]" />
									                
												   </div>
												</div>
												
										  </div>
										  <div title="整箱明细"> 
											  <div class="easyui-layout"  data-options="fit:true">
											        
													 <div data-options="region:'center',border:false">
														  <@p.datagrid id="reCheckDataGridBoxDetail_view" loadUrl="" saveUrl=""   defaultColumn="" 
																isHasToolBar="false" divToolbar="" onClickRowEdit="false"  height="300"  
															    pagination="true" rownumbers="true"  singleSelect = "true"  title="" showFooter="true"
																columnsJsonList="[
																		{field : 'cellNo',title : '储位',width : 100, align:'left',sortable:true},
																		{field : 'labelNo',title : '箱号',width : 150, align:'left',sortable:true},
																		{field : 'itemNo',title : '商品编码',width : 150, align:'left',sortable:true},
																		{field : 'itemName',title : '商品名称',width : 150, align:'left',sortable:true},
																		{field : 'colorName',title : '颜色',width : 100, align:'left',sortable:true},
																		{field : 'sizeNo',title : '尺码',width : 100, align:'left',sortable:true},
																		{field : 'itemQty',title : '计划数量',width : 100, align:'right',sortable:true},
																		{field : 'recheckQty',title : '实盘数量',width : 100, align:'right',sortable:true,
																			editor:{
																			    type:'numberbox'
																			}},
																		{field : 'diffQty',title : '差异数量', align:'right',width : 100,sortable:true},
																	{field : 'checkWorker',title : '盘点人员',width : 100, align:'left'},
																	{field : 'checkWorkerName',title : '盘点人员名称',width : 100, align:'left'}
																 ]" />
									                
												   </div>
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
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,minimizable:false">
			
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
				 	<#-- 工具菜单div -->
					<div id="itemSelectDiv">
						<@p.toolbar  id="edittoolbaritem"  listData=[
							{"title":"查询","iconCls":"icon-search","action":"re_receiptcheck.searchItem()", "type":0},
							{"title":"清除","iconCls":"icon-remove","action":"re_receiptcheck.searchItemClear()", "type":0},
							{"title":"确定","iconCls":"icon-ok","action":"re_receiptcheck.selectItemOK()", "type":0},
							{"title":"取消","iconCls":"icon-cancel","action":"re_receiptcheck.closeUI()", "type":0}
						]/>
					</div>
				
					<div id="itemSearchDiv" style="padding:10px;">
						<form name="itemSearchForm" id="itemSearchForm" metdod="post">
							<input type="hidden" name="locno" />
							<input type="hidden" name="checkNo" id="checkNo" />
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
					<div id="itemSelectCellDiv">
						储位：<input class="easyui-combobox ipt" style="width:120px" name="cellNo" id="cellNo" />
					</div>
					-->
				 	<#-- 商品选择数据列表div -->
          	  		<@p.datagrid id="dataGridJGItem"  loadUrl=""  saveUrl=""  defaultColumn="" 
		               isHasToolBar="false" divToolbar="" 
		               onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="false" title="商品明细"
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'itemNo',title : '商品编码 ',width : 150, align:'left'},
			                {field : 'itemName',title : '商品名称',width : 150, align:'left'},
			                {field : 'barcode',title : '商品条码',width : 150, align:'left'},
			                {field : 'sizeNo',title : '尺码',width : 90, align:'left'},
			                {field : 'colorName',title : '颜色',width : 90, align:'left'},
			             
			              
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                	//billimcheck.selectItemOK(rowData);
		            }}'/>
		     </div>
		      
		</div>      
	</div> 


</body>
</html>