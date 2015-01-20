<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>初盘发单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <#include  "/WEB-INF/ftl/common/header.ftl" >
   <script type="text/javascript" src="${domainStatic}/resources/js/billchcheck/billchcheck.js?version=1.0.8.2"></script>
    	<!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout">
	<div id="tags" class="easyui-tabs" data-options="region:'north',fit:true,border:false">
		<div title="创建盘点任务"> 
			 <div class="easyui-layout" data-options="fit:true">
			 	 <div  data-options="region:'north',border:false" >
			 	 	<@p.toolbar  id="edittoolbar"  listData=[
	                             {"title":"查询","iconCls":"icon-search","action":"billchcheck.searchDirectArea()", "type":0},
	                             {"title":"清除","iconCls":"icon-remove","action":"billchcheck.searchDirectClear()", "type":0}
		                       ]
					/> 
					<form id='directForm' method="post" class="city-form">
						<table>
							<tr>
								<td class="common-td blank">计划单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="planNo" id="planNo" readonly="true" data-options="required:true" /><input type="button" value="..." onclick="billchcheck.selectPlanNo()"></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="requestDateStart" id="requestDateStart"/></td>
								<td class="common-td blank">至：</td>
								<td><input class="easyui-datebox" style="width:120px" name="requestDateEnd" id="requestDateEnd"/></td>
								<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td>
		             				<input class="easyui-combobox" style="width:275px" name="sysNo" id="sysNoSearch"/>
		             			</td>
							</tr>
							<tr>
								<td class="common-td">仓区：</td>
	                 			<td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNo"/>
								</td>
	                 			<td class="common-td blank">库区：</td>
	                 			<td><input class="easyui-combobox" style="width:120px" name="areaNo" id="areaNo"/>
								</td>
	                 			<td class="common-td blank">通道：</td>
	                 			<td><input class="easyui-combobox" style="width:120px" name="stockNo" id="stockNo"/>
								</td>
								<td class="common-td blank">品牌：</td>
								<td><input class="easyui-combobox" style="width:275px" id="brandNo" name="brandNo" data-options="editable:false"/></td>
							</tr>
							<tr>
								<td class="common-td blank">储位：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="cellNoLike" id="cellNo" /></td>
							</tr>
						</table>
		    		</form>
		    		<div style="padding:10px;">    
			        	<form id="createChCheckForm" method="post" style="border:1px solid #95B8E7;padding:10px">
				        	盘点日期：<input class="easyui-datebox" data-options="editable:false" style="width:120px" name="checkDate" id="checkDate"/> 
				    		通道数量：<input class="easyui-numberbox ipt" style="width:120px" name="stockCount" id="stockCount"/>&nbsp;&nbsp;
				    		储位数量：<input class="easyui-numberbox ipt" style="width:120px" name="cellCount" id="cellCount"/>&nbsp;&nbsp;
				    		<a id="createTask" href="javascript:billchcheck.createBillChCheck();" data-options="iconCls:'icon-ok'" class="easyui-linkbutton">生成任务</a>
		    			</form>
		    		</div>
		    		<div style="padding:10px;">   
		    			<div style="border:1px solid #95B8E7;padding:10px">
		    				通道数：<input class="easyui-validatebox ipt" data-options="editable:false" style="width:120px"  id="allStockCount"/> 
				    		储位数：<input class="easyui-validatebox ipt" style="width:120px"  id="allCellCount"/>&nbsp;&nbsp;
		    			</div> 
		    		</div>
			 	 </div>
			 	<div  data-options="region:'center',border:false" >
			 			<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn="" 
			              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"  height="460"  
			               pagination="true" rownumbers="true"  singleSelect = "false" title="盘点定位信息" emptyMsg=""
				           columnsJsonList="[
								{field : 'planNo',title : '计划单号',width : 180},
								{field : 'areaName',title : '库区',width :120,align:'left'},
								{field : 'stockNo',title : '通道',width :100,align:'left'},
								{field : 'cellNo',title : '储位',width : 120,align:'left',sortable:true},
								{field : 'audittm',title : '审核日期',width :135,sortable:true},
								{field : 'itemNo',title : '商品编码',width : 150,align:'left',sortable:true},
								{field : 'itemName',title : '商品名称',width : 150,align:'left'},
								{field : 'colorName',title : '颜色',width : 100,align:'left'},
								{field : 'sizeNo',title : '尺码',width : 100,align:'left'}
				                 ]" 
					           jsonExtend='{onSelect:function(rowIndex, rowData){
			                            // 触发点击方法  调JS方法
			                   },onDblClickRow:function(rowIndex, rowData){
			                   	  // billimreceipt.instockDetail(rowData,"view");
			                   }}'/>
			 	</div>
			 </div>
    	</div> 
    	<div title="盘点任务分配">
    		 <div class="easyui-layout" data-options="fit:true">
			 	 <div  data-options="region:'north',border:false" >
			 	 	<@p.toolbar  id="check_toolbar"  listData=[
	                             {"title":"查询","iconCls":"icon-search","action":"billchcheck.searchArea()", "type":0},
	                             {"title":"清除","iconCls":"icon-remove","action":"billchcheck.searchLocClear()", "type":0},
	                             {"title":"打印预览","iconCls":"icon-print","action":"billchcheck.printBillchcheck()", "type":2},
	                             {"title":"切单还原","iconCls":"icon-refresh","action":"billchcheck.restoreCheck();", "type":2}
		                       ]
					/> 
			 	 	<form id='checkForm' style="padding:10px;padding-bottom:0px;">
			        	盘点单号：<input class="easyui-validatebox ipt" style="width:120px" name="checkNo"/>&nbsp;&nbsp;  
			        	计划单号：<input class="easyui-validatebox ipt" style="width:120px" name="planNo"/>&nbsp;&nbsp;  
			        	创建日期：<input class="easyui-datebox" style="width:120px" name="createtm_start" id="createtm_start"/> 
			    		至：<input class="easyui-datebox" style="width:120px" name="createtm_end" id="createtm_end"/>&nbsp;&nbsp;
    				</form>
    				<div style="padding:10px;padding-top:0px;">
    					<div style="border:1px solid #95B8E7;padding:10px;margin-top:10px;margin-bottom:5px;">   
				    		盘点人员：<input class="easyui-combobox ipt" style="width:120px" name="assignNo" id="assignNo"  />&nbsp;&nbsp;
				    		<a id="createTask" href="javascript:billchcheck.distributionAssignNoBatch();" data-options="iconCls:'icon-ok'" class="easyui-linkbutton">分配任务</a>
		    			</div> 
    				</div>
		    		
			 	 </div>
			 	 <div  data-options="region:'center',border:false" >
			 	 			 <@p.datagrid id="dataGridJGCheck"  loadUrl="" 
			 	 			   saveUrl=""   defaultColumn=""   showFooter="true"
				              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false" 
				               pagination="true" rownumbers="true"  singleSelect = "false" title="盘点单"  emptyMsg=""
					           columnsJsonList="[
									{field : ' ',checkbox:true},
									{field : 'status',title : '状态',width : 100,align:'left',formatter:billchcheck.statusFormatter},
									{field : 'checkNo',title : '盘点单号',width : 180},
									{field : 'planNo',title : '计划单号',width : 180},
									{field : 'checkDate',title : '计划盘点日期',width :120},
									{field : 'planType',title : '盘点类型',width : 100,formatter:billchcheck.planTypeFormatter},
									{field : 'assignNo',title : '盘点人员',width : 100,align:'left'},
									{field : 'assignName',title : '盘点人员名称',width : 100,align:'left'},
									{field : 'itemQty',title : '计划数量',width : 80,align:'right'},
									{field : 'creator',title : '创建人',width : 120},
									{field : 'creatorName',title : '创建人名称',width : 120},
									{field : 'createtm',title : '创建日期',width : 135,sortable:true}
					                 ]" 
						           jsonExtend='{onSelect:function(rowIndex, rowData){
				                            // 触发点击方法  调JS方法
				                   },onDblClickRow:function(rowIndex, rowData){
				                   	   billchcheck.loadDetail(rowData,"view");
				                   }}'/>
			 	 </div>
			 	 <div  data-options="region:'south',minSplit:true" style="height:200px;">
		 	 			 <@p.datagrid id="dataGridJGCheckDtl"  loadUrl="" saveUrl=""   defaultColumn="" 
			              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"   showFooter="true"
			               pagination="true" rownumbers="true"  singleSelect = "false"  title="盘点单明细"  emptyMsg=""
				           columnsJsonList="[
								{field : 'cellNo',title : '储位',width : 70},
								{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
								{field : 'itemName',title : '商品名称',width : 150,align:'left'},
								{field : 'colorName',title : '颜色',width : 100,align:'left'},
								{field : 'sizeNo',title : '尺码',width : 100,align:'left'},
								{field : 'itemQty',title : '计划数量',width : 100,align:'right'}
				                 ]" 
					           jsonExtend='{onSelect:function(rowIndex, rowData){
			                            // 触发点击方法  调JS方法
			                   },onDblClickRow:function(rowIndex, rowData){
			                   	  // billimreceipt.instockDetail(rowData,"view");
			                   }}'/>
			 	 </div>
			</div>
    	</div> 
	</div>	
<div id="planNoDialog" class="easyui-window" title="选择计划单号"
	   		style="width:435px;"   
		    data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		     minimizable:false,maximizable:false" class="easyui-layout">
		<div data-options="region:'north',border:false" style="height:200px;">
			<@p.datagrid id="dataPlanNo"  loadUrl="" saveUrl=""   defaultColumn="" 
			              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"  height="200"  
			               pagination="true" rownumbers="true"  singleSelect = "true" width="410"
				           columnsJsonList="[
								{field : 'planNo',title : '计划单号',width :250},
								{field : 'planAuditor',title : '审核人',width :80},
								{field : 'planAuditorName',title : '审核人名称',width :80}
				                 ]" 
				              jsonExtend='{onSelect:function(rowIndex, rowData){
			                            // 触发点击方法  调JS方法
			                   },onDblClickRow:function(rowIndex, rowData){
			                   	     billchcheck.checkPlanNo(rowData);
			                   }}'/>
		</div>
</div>
</body>
</html>