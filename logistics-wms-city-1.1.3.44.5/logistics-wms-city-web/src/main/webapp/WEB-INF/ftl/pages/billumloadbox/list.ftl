<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退仓装箱</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <#include  "/WEB-INF/ftl/common/header.ftl" >
   <script type="text/javascript" src="${domainStatic}/resources/js/billumcheck/billumcheck.js?version=1.0.5.1"></script>
   <script type="text/javascript" src="${domainStatic}/resources/js/billumloadbox/billumloadbox.js?version=1.0.6.5"></script>
	<!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout">
	<div id="tags" class="easyui-tabs" data-options="region:'north',fit:true,border:false">
		<div title="创建装箱任务"> 
			 <div class="easyui-layout" data-options="fit:true">
			 	 <div  data-options="region:'north',border:false" >
			 	 	<@p.toolbar  id="edittoolbar"  listData=[
	                             {"title":"查询","iconCls":"icon-search","action":"billumloadbox.searchCheckData()", "type":0},
	                             {"title":"清除","iconCls":"icon-remove","action":"billumcheck.searchClear('searchForm')", "type":0},
		                     	 {"title":"生成任务","iconCls":"icon-ok","action":"billumloadbox.createLoadBox()", "type":1}
		                       ]
					/> 
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
							<tr>
								<td class="common-td blank">验收单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo"/></td>	
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="ownerNo" id="ownerNoCondition" /></td>
								<td class="common-td blank" >商品类型：</td>
			   					<td><input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemType_search"  style="width:120px"/></td>
			   					<td class="common-td blank">品质：</td>
								<td><input class="easyui-combobox" name="quality" id="quality_search" style="width:120px;"/></td>	
							</tr>
							<tr>
								<td class="common-td blank">店退仓通知单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadMmNo"/></td>
								<td class="common-td blank">创建人：</td>
								<td><input class="combobox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="startCreatetmCondition" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="createtmEnd" id="endCreatetmCondition" 
									data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>
							<tr>
								<td class="common-td blank">店退仓单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadNo"/></td>
								<td class="common-td blank">审核人：</td>
								<td><input class="combobox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
								<td class="common-td blank">审核日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="startAudittmCondition" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="audittmEnd" id="endAudittmCondition" 
									data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>
							<tr>
								<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
			             		<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch_box"/></td>
			             		<td class="common-td blank">所属品牌：</td>
								<td colspan="5"><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo_box" /></td>
		             		</tr>
						</table>
					</form>	
			 	 </div>
			 	<div  data-options="region:'center',border:false" >
			 			<@p.datagrid id="dataGridJG"  loadUrl=""  saveUrl=""  defaultColumn=""  title="退仓验收单列表"
		               isHasToolBar="false" divToolbar="" height="410"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true" emptyMsg="" 
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'checkType',hidden:true},
			           		{field : 'status',hidden:'true'},
			           		{field : 'checkNo',title : '验收单号',width : 180},
			                {field : 'untreadMmNo',title : '店退仓通知单号',width : 180},
			                {field : 'untreadNo',title : '店退仓单号',width : 180},
			                {field : 'ownerNo',title : '货主',align:'left',width : 100,formatter:billumcheck.ownerFormatter},
			                {field : 'itemType',title : '商品类型',width : 100,formatter:billumcheck.typeFormatter,align:'left'},
							{field : 'quality',title : '品质',width : 100,formatter:billumloadbox.qualityFormatter,align:'left'},
			                {field : 'creator',title : '创建人',width : 80,align:'left'},
			                {field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			                {field : 'createtm',title : '创建时间',width : 130,sortable:true},
			                {field : 'auditor',title : '审核人',width : 80,align:'left'},
			                {field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
			                {field : 'audittm',title : '审核时间',width : 130}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		    				// 触发点击方法  调JS方法
		        			billumcheck.loadDetail4checkBox(rowData,1);
					}}'/>
			 	</div>
			 	<div  data-options="region:'south',minSplit:true,border:false" style="height:200px">
			           <@p.datagrid id="checkGridDetail" name="" title="退仓验收单明细"  loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  height="295"  
				 			onClickRowEdit="false" singleSelect="false"   
							pagination="true" rownumbers="false" emptyMsg=""  showFooter="true"
				 			columnsJsonList="[
				 			    {field:'boxNo',title:'箱号',width:160,align:'left'},
				 				{field:'itemNo',title:'商品编码',width:150,align:'left'},
				 				{field:'itemName',title:'商品名称',width:150,align:'left'},
							    {field:'colorName',title:'颜色',width:80,align:'left'},
							    {field:'sizeNo',title:'尺码',width:80,align:'left'},
							    {field:'brandName',title:'品牌',width:100,align:'left'},
							    {field:'itemQty',title:'计划数量',width:80,align:'right'},
							    {field:'checkQty',title:'验收数量',align:'right',width:80},
							    {field:'difQty',title:'差异数量',width:80,align:'right',formatter:billumcheck.diffQty}
				 			]"/>
			 	</div>
			 </div>
    	</div> 
    	<div title="退仓装箱">
    		 <div class="easyui-layout" data-options="fit:true">
			 	 <div  data-options="region:'north',border:false" >
			 	 	<@p.toolbar  id="splitboxtoolbar"  listData=[
	                             {"title":"查询","iconCls":"icon-search","action":"billumloadbox.searchArea()","type":0},
	                             {"title":"清除","iconCls":"icon-remove","action":"billumloadbox.searchClear('loadBoxForm')", "type":0},
	                             {"title":"封箱","iconCls":"icon-ok","action":"billumloadbox.splitBox()", "type":1}
		                       ]
					/>
			 	 	<form id='loadBoxForm' class="city-form">
			        	<table>
							<tr>
								<td class="common-td blank">状态：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="status" id="boxStatus" /></td>
								<td class="common-td blank">创建人：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="creator" id="boxCreator" /></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="boxCreatetmStart" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="createtmEnd" id="boxCreatetmEnd" 
									data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>
							<tr>
								<td class="common-td blank">退仓装箱单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="loadboxNo"/></td>
		   						<td class="common-td blank">审核人：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="auditor" id="boxAuditor" /></td>
								<td class="common-td blank">审核日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="boxAudittmStart" /></td>
								<td class="common-line">&nbsp;&nbsp;&nbsp;&mdash;&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="audittmEnd" id="boxAudittmEnd" 
									data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/>
								</td>
							</tr>
							<tr>
								<td class="common-td blank">验收单：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo"/></td>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="ownerNo" id="boxOwnerNo" /></td>
								<td class="common-td blank" >商品类型：</td>
		   						<td><input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemType_box"  style="width:120px"/></td>
							</tr>
						</table>
    				</form>
			 	 </div>
			 	 <div  data-options="region:'center',border:false" >
			 	 			 <@p.datagrid id="loadBoxGrid"  loadUrl="/bill_um_loadbox/mainlist.json?locno=${session_user.locNo}" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false" 
				               pagination="true" rownumbers="true"  singleSelect = "false" title="退仓装箱列表"  emptyMsg=""
					           columnsJsonList="[
									{field : ' ',checkbox:true},
									{field : 'status',title : '状态',width :100,formatter:billumloadbox.boxStatusFormatter,align:'left'},
									{field : 'loadboxNo',title : '退仓装箱单号',width :180},
									{field : 'ownerNo',title : '货主',width :120,align:'left',formatter:billumloadbox.boxOwnerFormatter},
									{field : 'itemType',title : '退仓类型',width : 120,align:'left',formatter:billumloadbox.typeFormatter},
									{field : 'noSealedCount',width : 100,hidden:true},
									{field : 'creator',title : '创建人',width : 80,align:'left'},
									{field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
									{field : 'createtm',title : '创建日期',width : 150},
									{field : 'auditor',title : '审核人',width : 80,align:'left'},
									{field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
									{field : 'audittm',title : '审核日期',width : 150}
					                 ]" 
						           jsonExtend='{onSelect:function(rowIndex, rowData){
				                            // 触发点击方法  调JS方法
				                   },onDblClickRow:function(rowIndex, rowData){
				                   	   billumloadbox.loadBoxDtl(rowData);
				                   }}'/>
			 	 </div>
			</div>
    	</div> 
	</div>	
	<div id="splitBox" class="easyui-window" title="封箱"
		   data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	     <div class="easyui-layout" data-options="fit:true">
			<div  data-options="region:'north',border:false" >
				<form id='loadBoxDtlForm' class="city-form">
				       <input type='hidden' id="itemType4fx" />
			        	<table>
							<tr>
									<td class="common-td blank">退仓装箱单号：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="loadboxNo" id="box_loadBoxNo" readOnly="true"/></td>
									<td class="common-td blank">货主：</td>
									<td>
										<input class="easyui-validatebox ipt" data-options="editable:false" style="width:120px" id="box_owenrName"/>
										<input type="hidden" id="box_owenrNo" name="ownerNo">
									</td>
									
									<td class="common-td blank">退仓类型：</td>
									<td><input class="easyui-validatebox ipt" data-options="editable:false" style="width:120px" id="box_itemType_name"/></td>
									
							</tr>
						<tr>
							<td class="common-td blank">创建人：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" /></td>
							<td class="common-td blank">创建日期：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="createtm" /></td>
							<td class="common-td blank">审核人：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" /></td>
							<td class="common-td blank">审核日期：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="audittm" /></td>
						</tr>
						</table>
    				</form>
			</div>
			<div data-options="region:'center',border:false">
				<div id="boxtag" class="easyui-tabs" data-options="region:'north',fit:true,border:false">
					<div title="装箱任务详情"> 
						 <div class="easyui-layout" data-options="fit:true">
						 	 <div  data-options="region:'north',border:false" >
						 	 	<@p.toolbar  id="splitBoxToolBar"  listData=[
					                     	 {"title":"确认","iconCls":"icon-ok","action":"billumloadbox.splitBoxOk()", "type":0}
					                       ]
								/> 
						 	 </div>
						 	<div  data-options="region:'center',border:false" >
						 			<@p.datagrid id="loadBoxDtl"  loadUrl="/bill_um_loadbox/selectCheck4LoadBox?locno=${session_user.locNo}"  saveUrl=""  defaultColumn=""  title="退仓验收单明细"
					               isHasToolBar="false" divToolbar="" height="410"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
						           rownumbers="true"
						           columnsJsonList="[
						           		{field : 'id',checkbox:true},
						           		{field:'itemNo',title:'商品编码',width:140,align:'left'},
							 			{field:'itemName',title:'商品名称',width:130,align:'left'},
										{field:'colorName',title:'颜色',width:80,align:'left'},
										{field:'sizeNo',title:'尺码',width:60,align:'left'},
										{field:'itemQty',title:'数量',width:60,align:'right'},
										{field:'boxingQty',title:'已装箱数量',width:80,align:'right'}
						            ]" 
							        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
					    				// 触发点击方法  调JS方法
					        			billumcheck.loadDetail4checkBox(rowData,1);
								}}'/>
						 	</div>
						 </div>
			    	</div> 
			    	<div title="箱明细">
			    		 <div class="easyui-layout" data-options="fit:true">
						 	 <div  data-options="region:'center',border:false" >
						 	 			 <@p.datagrid id="boxDtlGrid"   saveUrl=""   defaultColumn="" 
							              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false" 
							               pagination="true" rownumbers="true"  singleSelect = "false" title="退仓装箱列表"  emptyMsg="" 
								           columnsJsonList="[
								           		{field : 'boxNo',title : '箱号',width : 160,align:'left'},
								           		{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
								           		{field : 'itemName',title : '商品名称',width : 150,align:'left'},
								           		{field : 'colorName',title : '颜色',width : 120,align:'left'},
								           		{field : 'sizeNo',title : '尺码',width : 80,align:'left'},
								           		{field : 'itemQty',title : '数量',width :50,align:'right'},
												{field : 'creator',title : '创建人',width : 100},
												{field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
												{field : 'createtm',title : '创建日期',width : 150}
								                 ]" 
									           jsonExtend='{onSelect:function(rowIndex, rowData){
							                            // 触发点击方法  调JS方法
							                   },onDblClickRow:function(rowIndex, rowData){
							                   	 //  billchcheck.loadDetail(rowData,"view");
							                   }}'/>
						 	 </div>
						</div>
			    	</div> 
			</div>	
			</div>
	     </div>
	</div>
	<#-- RF未封箱的明细 -->
	<div id="RfSplitBox" class="easyui-window" title="RF未封箱明细"
		   data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	     <div class="easyui-layout" data-options="fit:true">
			<div  data-options="region:'north',border:false" >
				<form id='loadRfBoxDtlForm' class="city-form">
				       <input type='hidden' id="rf_itemType4fx" />
			        	<table>
							<tr>
									<td class="common-td blank">退仓装箱单号：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="loadboxNo" id="rf_box_loadBoxNo" readOnly="true"/></td>
									<td class="common-td blank">货主：</td>
									<td>
										<input class="easyui-validatebox ipt" data-options="editable:false" style="width:150px" id="rf_box_owenrName"/>
										<input type="hidden" id="box_owenrNo" name="ownerNo">
									</td>
									
									<td class="common-td blank">退仓类型：</td>
									<td><input class="easyui-validatebox ipt" data-options="editable:false" style="width:120px" id="rf_box_itemType_name"/></td>
									
							</tr>
						<tr>
							<td class="common-td blank">创建人：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" /></td>
							<td class="common-td blank">创建日期：</td>
							<td><input class="easyui-validatebox ipt" style="width:150px" name="createtm" /></td>
							<td class="common-td blank">审核人：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" /></td>
							<td class="common-td blank">审核日期：</td>
							<td><input class="easyui-validatebox ipt" style="width:150px" name="audittm" /></td>
						</tr>
						</table>
    				</form>
			</div>
			<div data-options="region:'center',border:false">
				 	 	<@p.toolbar  id="splitBoxToolBarx"  listData=[
			                     	 {"title":"确认封箱","iconCls":"icon-ok","action":"billumloadbox.RfSplitBoxOk()", "type":0}
			                       ]
						/> 
		 			<@p.datagrid id="RfboxDtlGridx"   saveUrl=""   defaultColumn="" 
			              isHasToolBar="false" divToolbar="#splitBoxToolBarx" onClickRowEdit="false" 
			               pagination="true" rownumbers="true"  singleSelect = "false" title="退仓装箱列表"  emptyMsg="" 
				           columnsJsonList="[
				           		{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
				           		{field : 'itemName',title : '商品名称',width : 150,align:'left'},
				           		{field : 'colorName',title : '颜色',width : 120,align:'left'},
				           		{field : 'sizeNo',title : '尺码',width : 80,align:'left'},
				           		{field : 'itemQty',title : '数量',width :50,align:'right'},
								{field : 'creator',title : '创建人',width : 100},
								{field : 'createtm',title : '创建日期',width : 150}
				                 ]" 
					           jsonExtend='{onSelect:function(rowIndex, rowData){
			                            // 触发点击方法  调JS方法
			                   },onDblClickRow:function(rowIndex, rowData){
			                   	 //  billchcheck.loadDetail(rowData,"view");
			                   }}'/>
			</div>
	     </div>
	</div>
	
	<div id="splitBox_view" class="easyui-window" title="明细"
		   data-options="modal:true,resizable:false,draggable:true,collapsible:false,
	    closed:true,minimizable:false,maximizable:false,maximized:true">
	     <div class="easyui-layout" data-options="fit:true">
		    <div  data-options="region:'north',border:false" >
				<@p.toolbar id="viewtoolbar"   listData=[
		    		{"id":"printDetail","title":"打印预览","iconCls":"icon-print","action":"billumloadbox.loadPrint()","type":2},
	                {"title":"导出","iconCls":"icon-export","action":"billumloadbox.do_export()","type":5},
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billumloadbox.closeWindow('splitBox_view');","type":0}
		        ]/>
		       <form id='exportForm' method="post" action="../bill_um_loadbox_dtl/do_export">
						<input type="hidden" name="loadboxNo" id="loadboxNo_export"/>
						<input type="hidden" name="locno" id="locno_export"/>
						<input type="hidden" name="ownerNo" id="ownerNo_export"/>
						<input type="hidden" name="exportColumns" id="exportColumnsCondition_export"/>
						<input type="hidden" name="fileName" value="退仓装箱"/>
				</form>
		        <form id='loadBoxDtlForm_view' class="city-form">
			    	<table>
						<tr>
							<td class="common-td blank">退仓装箱单号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="loadboxNo" id="box_loadBoxNo_view" readOnly="true"/></td>
							<td class="common-td blank">货主：</td>
							<td>
								<input class="easyui-validatebox ipt" data-options="editable:false" style="width:150px" id="box_owenrName_view"/>
								<input class="easyui-validatebox ipt" type="hidden" id="box_owenrNo_view" name="ownerNo">
							</td>
							<td class="common-td blank">退仓类型：</td>
							<td><input class="easyui-validatebox ipt" data-options="editable:false" style="width:120px" id="box_itemType_name_view"/></td>	
						</tr>
						<tr>
							<td class="common-td blank">创建人：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" /></td>
							<td class="common-td blank">创建日期：</td>
							<td><input class="easyui-validatebox ipt" style="width:150px" name="createtm" /></td>
							<td class="common-td blank">审核人：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" /></td>
							<td class="common-td blank">审核日期：</td>
							<td><input class="easyui-validatebox ipt" style="width:150px" name="audittm" /></td>
						</tr>
					</table>
				</form>
		     </div>
				<#--查询start-->
				<div data-options="region:'center',border:false">
					<div id="boxtag_view" class="easyui-tabs" data-options="region:'north',fit:true,border:false">
					<div title="装箱任务详情"> 
						 <div class="easyui-layout" data-options="fit:true">
						 	<div  data-options="region:'center',border:false" >
						 			<@p.datagrid id="loadBoxDtl_view"  loadUrl="/bill_um_loadbox/selectCheck4LoadBox?locno=${session_user.locNo}"  saveUrl=""  defaultColumn=""  title="退仓装箱明细列表"
					               isHasToolBar="false" divToolbar="" height="410"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
						           rownumbers="true" showFooter = "true"
						           columnsJsonList="[
						           		{field:'loadboxNo',title:'单号',width:180,hidden:true},
						           		{field:'itemNo',title:'商品编码',width:150,align:'left'},
							 			{field:'itemName',title:'商品名称',width:150,align:'left'},
										{field:'colorName',title:'颜色',width:80,align:'left'},
										{field:'sizeNo',title:'尺码',width:60,align:'left'},
										{field:'brandName',title:'品牌',width:100,align:'left'},
										{field:'itemQty',title:'数量',width:60,align:'right'},
										{field:'boxingQty',title:'已装箱数量',width:80,align:'right'}
						            ]" 
							        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
					    				// 触发点击方法  调JS方法
					        			//billumcheck.loadDetail4checkBox(rowData,1);
								}}'/>
						 	</div>
						 </div>
			    	</div> 
			    	<div title="箱明细">
			    		 <div class="easyui-layout" data-options="fit:true">
						 	 <div  data-options="region:'center',border:false" >
						 	 			 <@p.datagrid id="boxDtlGrid_view"  loadUrl="" saveUrl=""   defaultColumn="" 
							              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false" 
							               pagination="true" rownumbers="true"  singleSelect = "false" title="退仓装箱列表"  emptyMsg="" showFooter = "true"
								           columnsJsonList="[
								          		{field : 'id',checkbox:true},
												{field : 'boxNo',title : '箱号',width : 160,align:'left'},
								           		{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
								           		{field : 'itemName',title : '商品名称',width : 150,align:'left'},
								           		{field : 'colorName',title : '颜色',width : 120,align:'left'},
								           		{field : 'sizeNo',title : '尺码',width : 80,align:'left'},
								           		{field : 'itemQty',title : '数量',width :50,align:'right'},
												{field : 'creator',title : '创建人',width : 100},
												{field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
												{field : 'createtm',title : '创建日期',width : 150}
												]"
									           jsonExtend='{onSelect:function(rowIndex, rowData){
							                            // 触发点击方法  调JS方法
							                   },onDblClickRow:function(rowIndex, rowData){
							                   	 //  billchcheck.loadDetail(rowData,"view");
							                   }}'/>
						 	 </div>
						</div>
		    		</div> 
				</div>	
			</div>
		</div>
	</div>
	
<div id="splitBoxConfirm" class="easyui-window" title="封箱确认"
	   		style="width:600px;height:500px;"   
		    data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		     minimizable:false,maximizable:false" >
		      <div class="easyui-layout" data-options="fit:true">
			 	 <div  data-options="region:'north',border:false" >
			 	 	<@p.toolbar  id="splitBoxConfirmBar"  listData=[
	                             {"title":"确认封箱","iconCls":"icon-ok","action":"billumloadbox.splitBoxDo()", "type":0},
	                             {"title":"取消","iconCls":"icon-cancel","action":"billumloadbox.closeWindow('splitBoxConfirm')", "type":0}
		                       ]
					/> 
			 	 </div>
				<div data-options="region:'center',border:false">
					<@p.datagrid id="splitBoxConfirmGrid"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false"   onClickRowEdit="true"  height="200"  
				               pagination="true" rownumbers="true"  singleSelect = "true" width="410" emptyMsg=""
					           columnsJsonList="[
				           		{field:'itemNo',title:'商品编码',width:140,align:'left'},
								{field:'sizeNo',title:'尺码',width:60,align:'left'},
								{field:'itemQty',title:'数量',width:60,align:'right'},
								{field:'boxingQty',title:'已装箱数量',width:80,align:'right'},
								{field:'checkQty',title:'装箱数量',width:80,align:'right',
									editor:{
						 						type:'numberbox'
						 					}
					 			}
				            ]" 
					       />
				</div>
			</div>
</div>
</body>
</html>