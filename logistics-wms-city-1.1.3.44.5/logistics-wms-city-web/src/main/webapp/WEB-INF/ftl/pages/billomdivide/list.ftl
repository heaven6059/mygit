<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>分派分货任务</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billomdivide/billOmDivide.js?version=1.1.1.5"></script>
<!--object需放在head结尾会截断jquery的html函数获取内容-->
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
</object>
</head>
<body class="easyui-layout">

<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar" listData=[
			{"title":"查询","iconCls":"icon-search","action":"billOmDivide.searchData()", "type":0},
            {"title":"清除","iconCls":"icon-remove","action":"billOmDivide.searchMainClear('searchForm')", "type":0},
			{"id":"icon-add","title":"新增","iconCls":"icon-add","action":"billOmDivide.showAddDialog()", "type":1},
			{"id":"btn-close","title":"修改","iconCls":"icon-edit","action":"billOmDivide.toUpdate()","type":2},
			{"id":"icon-remove","title":"删除","iconCls":"icon-del","action":"billOmDivide.do_del()", "type":3},
			{"id":"icon-remove","title":"汇总打印预览","iconCls":"icon-print","action":"billOmDivide.printSum()", "type":3},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
		 ]/>
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" class="search-div" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<input id="currentStatus" type="hidden" value = "0">
						<input type='hidden'  name="isShowOmDivide" id="isShowOmDivide" value=${(Session["isShowOmDivide"])!} />
				 		<table>
		             		<tr>
		             			<td class="common-td">状态：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
		             			<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
		             			<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
		             			<td class="common-td blank">创建日期：</td>
		             			<td><input class="easyui-datebox ipt" style="width:120px" name="startCreatetm" id="startCreatetmCondition" /></td>
		             			<td class="common-line" width='40'>&mdash;</td>
		             			<td><input class="easyui-datebox ipt" style="width:120px" name="endCreatetm" id="endCreatetmCondition" 
								data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/></td>
		             		</tr>
		             		<tr>
		             			<td class="common-td">单据编号：</td>
		             			<td><input class="easyui-validatebox ipt" style="width:120px" name="divideNo" id="divideNoCondition" /></td>
		             			<td class="common-td blank">发货通知单：</td>
		             			<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNoCondition" /></td>
		             			<td class="common-td blank">分货日期：</td>
		             			<td><input class="easyui-datebox ipt" style="width:120px" name="startExpDate" id="startExpDateCondition" /></td>
		             			<td class="common-line">&mdash;</td>
		             			<td><input class="easyui-datebox ipt" style="width:120px" name="endExpDate" id="endExpDateCondition" 
								data-options="validType:['vCheckDateRange[\'#startExpDateCondition\',\'结束日期不能小于开始日期\']']"/></td>
		             		</tr>
		             		<tr>
		             			<td class="common-td blank">收货单号：</td>
		                 		<td><input class="easyui-validatebox ipt" style="width:120px" name="receiptNo" id="receiptNoCondition" /></td>
		             			<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td>
		             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/>
		             			</td>
		             			<td class="common-td blank">所属品牌：</td>
                     			<td colspan='3'><input class="easyui-combobox ipt" style="width:280px" name="brandNo" id="brandNo" /></td>
		             		</tr>
		             	</table>
				 	</form>
				</div>
        	</div>
      		<#--查询end-->
			<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    		<@p.datagrid id="mainDataGrid"   loadUrl="" saveUrl=""  defaultColumn=""  title="分货单列表"
					isHasToolBar="false"  divToolbar="" height="435"  onClickRowEdit="false" singleSelect="false" pageSize="20"
					rownumbers="true" emptyMsg=""
					columnsJsonList="[
						{field : 'id',checkbox:true},
						{field : 'locno',hidden:true},
						{field : 'status',title : '状态',width : 80,formatter:billOmDivide.statusFormatter,align:'left'},
						{field : 'divideNo',title : '单据编号',width : 180},
						{field : 'businessTypeStr',title : '业务类型',width : 80},
						{field : 'ruleName',title : '分货规则名称',width : 120,align:'left'},
						{field : 'assignName',title : '分货人',width : 100,align:'left'},
						{field : 'assignNameCh',title : '分货人名称',width : 100,align:'left'},
						{field : 'operateDate',title : '分货日期',width : 130},
						{field : 'creator',title : '创建人',width : 100,align:'left'},
						{field : 'creatorname',title : '创建人名称',width : 100,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130}
						]" 
						jsonExtend='{onDblClickRow:function(rowIndex, rowData){
						    billOmDivide.loadDetail(rowData,1);
						}}'/>
			</div>
        	<#--显示列表end-->
	    </div>
	</div>  
	<#-- 主表end -->
	
	<#-- 查看明细信息div BEGIN -->
	<div id="showDetailDialog"  class="easyui-dialog" title="修改/查看明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="viewtoolbar"   listData=[
		    		{"id":"btn-save","title":"修改","iconCls":"icon-edit","action":"billOmDivide.updateBillLoginUser();","type":0}
		    		{"id":"printDetail","title":"打印预览","iconCls":"icon-print","action":"billOmDivide.printDetail()","type":2},
		    		{"id":"overDivide","title":"手工关闭","iconCls":"icon-close","action":"billOmDivide.overFloc();","type":4},
		    		{"id":"printDetail","title":"导出","iconCls":"icon-export","action":"billOmDivide.exportDtl()","type":2},
		    		{"id":"toDivideDif","title":"转商品差异调整","iconCls":"icon-redo","action":"billOmDivide.toDivideDifferent();","type":4},
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billOmDivide.closeWindow('showDetailDialog');","type":0}
		        ]/>
			</div>
			<#--查询start-->
			<div data-options="region:'center',border:false">
				<div id="main_order_dtlId" class="easyui-tabs" fit="true">
			    	<#-- 分货单明细 BEGIN -->
			    	<div id="tab_order_dtl1" title="分货单明细" height='100%'>
		    			<div class="easyui-layout" data-options="fit:true">
		    				<div data-options="region:'north',border:false">
		    					<div class="search-div">
					         		<form name="detailForm" id="detailForm" method="post" class="city-form">
					         			<table>
					                 		<tr>
					                 			<td class="common-td">单据编号：</td>
					                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="divideNo" id="divideNo"   readOnly="true"/></td>
					                 			<td class="common-td blank">分货人：</td>
					                 			<td>
					                 				<input class="easyui-validatebox ipt" style="width:120px" name="assignName" id="assignName" readOnly="true"/>
					                 				<input type='hidden' name="assignNameCh"  id="assignNameCh"/>
													<a id="selectPeople" name="selectPeople" href="javascript:billOmDivide.showLoginUserDialog();" class="easyui-linkbutton" >...</a>
													<input id="locnoHidden" type="hidden" value=""/>
													<input id="divideNoHidden" type="hidden" value=""/>
												</td>
					                 		</tr>
					                    </table>
					         		</form>
								</div>
			    			</div>
		    				<div data-options="region:'center',border:true">
		    					<@p.datagrid id="order_dtl1_dataGrid" defaultColumn=""  
									isHasToolBar="false" divToolbar="" height="295" pageSize="10" 
									onClickRowEdit="false" onClickRowAuto="false" pagination="true"
									rownumbers="true" emptyMsg="" showFooter="true"
									columnsJsonList="[
									      {field : 'divideNo',title : '分货单号',width :150,align:'center',hidden:true},
									 	  {field : 'statusStr',title : '状态',width : 60,align:'left',sortable:true},
								          {field : 'serialNo',title : '流道编码',width : 80,align:'left',sortable:true},
								          {field : 'groupName',title : '店组',width : 80,align:'left',sortable:true},
								          {field : 'groupNo',title : '箱组',width : 80,align:'left',sortable:true},
								          {field : 'boxNo',title : '箱号',width : 110,align:'left',sortable:true},
								          {field : 'storeNo',title : '客户编码',width : 80,align:'left',sortable:true},
								          {field : 'storeName',title : '客户名称',width : 180,align:'left',sortable:true},
								       	  {field : 'expNo',title : '发货通知单',width : 160,align:'left',sortable:true},
								          {field : 'itemNo',title : '商品编码',width : 160,align:'left',sortable:true},
								          {field : 'itemName',title : '商品名称',width : 160,align:'left',sortable:true},
								          {field : 'colorName',title : '颜色',width : 80,align:'left',sortable:true},
								          {field : 'sizeNo',title : '尺码',width : 80,align:'left',sortable:true},
								          {field : 'itemQty',title : '分货数量',width : 60,align:'right',sortable:true},
								          {field : 'assignName',title : '分货人',width : 100,align:'left',sortable:true},
								          {field : 'assignNameCh',title : '分货人名称',width : 100,align:'left',sortable:true},
								          {field : 'operateDate',title : '实际分货时间',width : 130,sortable:true}
								         ]" 
										jsonExtend="{}" />
			    			</div>
		    			</div>
		    		</div>
		    		<#-- 分货单明细 end -->
		    		<#-- 分货单差异 BEGIN -->
		    		<div id="tab_order_dtl2" title="分货单差异" height='100%'>
	    				<div class="easyui-layout" data-options="fit:true">
	    					<div data-options="region:'center',border:true">
	    						<@p.datagrid id="order_dtl2_dataGrid"    defaultColumn=""  showFooter="true" 
										isHasToolBar="false" divToolbar="#toolbarDlt2" height="349" pageSize="10" 
										onClickRowEdit="false" onClickRowAuto="false" pagination="true"
										rownumbers="true" emptyMsg=""
										columnsJsonList="[
											{field : 'id',checkbox:true},
											{field : 'statusStr',title : '状态',width : 60,align:'left',sortable:true},
											{field : 'boxNo',title : '箱号',width : 110,align:'left',sortable:true},
										  	{field : 'storeNo',title : '客户编码',width : 80,align:'left',sortable:true},
								          	{field : 'storeName',title : '客户名称',width : 180,align:'left',sortable:true},
								          	{field : 'itemNo',title : '商品编码',width : 160,align:'left',sortable:true},
								          	{field : 'itemName',title : '商品名称',width : 160,align:'left',sortable:true},
								          	{field : 'colorName',title : '颜色',width : 80,align:'left',sortable:true},
								          	{field : 'sizeNo',title : '尺码',width : 80,align:'left',sortable:true},
								          	{field : 'styleNo',hidden : true},
								          	{field : 'itemQty',title : '计划分货数量',width : 80,align:'right',sortable:true},
								          	{field : 'realQty',title : '实际分货数量',width : 80,align:'right',sortable:true},
								         	 {field : 'diffQty',title : '差异数量',width : 80,align:'right',sortable:true}
								         ]"
									rownumbers="true" jsonExtend="{}" />
	    					</div>
	    				</div>
		    		</div>
		    		<#-- 分货单差异 end -->
		    		<#-- 分货单差异调整 BEGIN -->
		    		<div id="tab_order_dtl3" title="差异调整记录" height='100%'>
	    				<div class="easyui-layout" data-options="fit:true">
	    					<div data-options="region:'center',border:true">
	    						<@p.datagrid id="order_dtl3_dataGrid" defaultColumn=""  showFooter="true" 
										isHasToolBar="false" divToolbar="" height="349" pageSize="10" 
										onClickRowEdit="false" onClickRowAuto="false" pagination="true"
										rownumbers="true" emptyMsg=""
										columnsJsonList="[
											{field:'storeNo',title:'客户编码',width:80,align:'left',sortable:true},
				 							{field:'sCellNo',title:'储位编码',width:100,align:'left',sortable:true},
				 							{field:'boxNo',title:'来源箱号',width:110,align:'left',sortable:true},
				 							{field:'expNo',title:'发货通知单',width:130,align:'left',sortable:true},
				 							{field:'itemQty',title:'调整数量',width:60,align:'right',sortable:true},
				 							{field:'sItemNo',title:'调整前商品编码',width:150,align:'left',sortable:true},
				 							{field:'sBarcode',title:'调整前商品条码',width:150,align:'left',sortable:true},
				 							{field:'sItemName',title:'调整前商品名称',width:150,align:'left',sortable:true},
							    			{field:'brandName',title:'品牌',width:80,align:'left',sortable:true},
							    			{field:'dItemNo',title:'调整后商品编码',width:150,align:'left',sortable:true},
				 							{field:'dBarcode',title:'调整后商品条码',width:150,align:'left',sortable:true},
				 							{field:'dItemName',title:'调整后商品名称',width:150,align:'left',sortable:true},
								         ]"
									rownumbers="true" jsonExtend="{}" />
	    					</div>
	    				</div>
		    		</div>
		    		<#-- 分货单差异调整 end -->
		    	</div>
			</div>
		<#--查询end-->	
		</div>
	</div>
	<#-- 查看明细信息div END -->
	
	<#-- 新增页面 BEGIN -->
	<div id="showAddDialog"  class="easyui-window" title="新增"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
			<div data-options="region:'north',border:false" >
				<@p.toolbar id="addtoolbar"   listData=[
		    		{"title":"查询","iconCls":"icon-search","action":"billOmDivide.searchReceipt();", "type":0},
            		{"title":"清除","iconCls":"icon-remove","action":"billOmDivide.searchClear('searchReceiptForm');", "type":0},
		        	{"title":"发货通知单","iconCls":"icon-aduit","action":"billOmDivide.billOmExp();", "type":0},
		        	{"id":"save_main","title":"保存","iconCls":"icon-save","action":"billOmDivide.saveBillOmDivide();", "type":0},
		        	{"id":"save_main_new","title":"新算法保存","iconCls":"icon-save","action":"billOmDivide.saveBillOmDivideNew();", "type":0},
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billOmDivide.closeWindow('showAddDialog');","type":0}
		        ]/>
				<div nowrap class="search-div" style="padding:10px;">
					<form name="searchReceiptForm" id="searchReceiptForm" method="post" class="city-form">
		         		<table>
		                 	<tr>
		                 		<td class="common-td blank">分货类型：</td>
                     			<td ><input class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessType" required="true" 
                     				data-options="editable:false,onChange:function(data){billOmDivide.loadBusinessType(data);}"/></td>
		                 		<td class="common-td blank">单据编号：</td>
		                 		<td><input class="easyui-validatebox ipt" style="width:120px" name="receiptNo" id="receiptNoCon" /></td>
		                 		<td class="common-td blank">收货日期：</td>
		                 		<td><input class="easyui-datebox ipt" style="width:120px" name="startReciveDate" id="startReciveDateCon" /></td>
		                 		<td class="common-line">&nbsp;&mdash;&nbsp;</td>
		                 		<td><input class="easyui-datebox ipt" style="width:120px" name="endReciveDate" id="endReciveDateCon" /></td>
		                 	</tr>
		                 	<tr>
		                 		<td class="common-td blank">预到货通知单号：</td>
		                 		<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo" id="importNoCon" /></td>
		                 		<td class="common-td blank">厂商入库单号：</td>
		                 		<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo" id="transNoCon" /></td>
		                 		<td class="common-td blank">供应商号：</td>
		                 		<td><input class="easyui-validatebox ipt" style="width:120px" name="supplierNo" id="supplierNoCon" /></td>
		                 		<td class="common-td blank""><div id="deliverNoTitle">装车单号：</div></td>
                     			<td ><input class="easyui-validatebox ipt" style="width:120px" name="deliverNo" id="deliverNoCon" /></td>
                     		</tr>
		            	</table>
		         	</form>
				</div>
			</div>
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'center',border:false" id = 'divideDtl'>
						<@p.datagrid id="mainDataGrid2" name=""  loadUrl="" saveUrl="" defaultColumn="" title="分货单据列表"
							isHasToolBar="false" onClickRowEdit="false" singleSelect="false" pageSize='10'  
							pagination="true" rownumbers="true" divToolbar="" emptyMsg="" 
							columnsJsonList="[
								{field : 'id',checkbox:true},
								{field : 'receiptNo',title : '单据编号',width : 180,align:'left'},
								{field : 'transNo',title : '厂商入库单号',width : 180,align:'left',hidden:true},
								{field : 'auditor',title : '审核人',width : 80,align:'left'},
								{field : 'audittm',title : '审核日期',width : 140},
								{field : 'totalBoxQty',title : '总箱数',width : 80,align:'right'},
								{field : 'ownerNo',hidden:true},
								{field : 'locno',hidden:true},
								{field : 'totalReciveQty',title : '总件数',width : 80,align:'right'}
							]"
							jsonExtend='{onSelect:function(rowIndex, rowData){
                          		// 触发点击方法  调JS方法
		                    },onDblClickRow:function(rowIndex, rowData){
		                   	    billOmDivide.searchDetail(rowIndex,rowData);
		                    }}'
						/>
					</div>
					<div data-options="region:'south',minSplit:true" style="height:150px">
						<@p.datagrid id="mainDataGrid3" name=""  loadUrl="" saveUrl="" defaultColumn="" title="收货单客户"
							isHasToolBar="false" onClickRowEdit="false" singleSelect="false" pageSize='10'  
							pagination="true" rownumbers="true" divToolbar="" emptyMsg="" 
							columnsJsonList="[
								{field : 'locno',hidden:true},
								{field : 'ownerNo',hidden:true},
								{field : 'expNo',title : '发货通知单号',width : 160,align:'left'}
						]"/>
					</div>
				</div>
			</div>
			<#--显示列表end-->
			<div  data-options="region:'south',border:false">
				<div class="easyui-layout" data-options="fit:true" >
					<div  data-options="region:'north',border:false" style="height:122px;">
						<div id="ruleSelect"  class="easyui-panel" title="分货规则" fit="true" collapsible="false"> 
							
							<div style="padding:5px;">
								<fieldset style="border:2px groove #FFFFFF; color:#000000; padding-left:4px; 
									padding-right:4px; padding-top:10px; padding-bottom:1px;height:45px;">
									<legend>&nbsp;分货规则选择&nbsp;</legend>
									<div id='storeRuleDiv'></div>
								</fieldset>
								<input type="hidden" id="cellNo" />
							</div>
						</div>
					</div>
				</div>
		    </div>
		</div>
	</div>
	
	
	<#-- 新增修改页面 END -->
	
	<#-- 商品选择div -->
		<div id="showLoginUserDialog"  class="easyui-dialog" title="人员选择"  
		    	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    	maximized:true,minimizable:false,maximizable:false"> 
			<div class="easyui-layout" data-options="fit:true">
				<#--查询start-->
				<div data-options="region:'north',border:false" >
			    	<@p.toolbar id="itemtoolbar"   listData=[
	        			{"title":"确定","iconCls":"icon-ok","action":"billOmDivide.selectLoginUser();", "type":0},
			    		{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billOmDivide.closeWindow('showLoginUserDialog');","type":0}
			        ]/>
					<div nowrap class="search-div" style="padding:10px;">
						<form name="searchLoginUserForm" id="searchLoginUserForm" method="post" class="city-form">
					 		<table>
					     		<tr>
					     			<td class="common-td">岗位：</td>
					     			<td><input class="easyui-combobox" style="width:120px" name="roleid" id="roleidCon" /></td>
					     		</tr>
					     	</table>
					 	</form>
					</div>
				</div>
				<#--查询end-->
				<#--显示列表start-->
				<div data-options="region:'center',border:false">
					<@p.datagrid id="loginUserDataGrid" name=""  loadUrl="" saveUrl="" defaultColumn="" 
						isHasToolBar="false"  divToolbar="#personnelToolbar"  height="280"  onClickRowEdit="false" singleSelect="false" pageSize='20'  
						pagination="true" rownumbers="true" divToolbar="" emptyMsg=""
						columnsJsonList="[
							{field : 'id',checkbox:true},
							{field : 'loginName',title : '人员编码',width : 100,align:'left'},	
							{field : 'username',title : '人员名称',width : 100,align:'left'},
							{field : 'roleName',title : '岗位',width : 190,align:'left'}
					]"/>
				</div>
				<#--显示列表end-->
			</div>			
		</div>
		
		<#-- 发货单选择div -->
		<div id="showExpDialog"  class="easyui-dialog" title="发货通知单选择"  style="width:680px;height:450px;"
		    	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    	maximized:false,minimizable:false,maximizable:false"> 
			<div class="easyui-layout" data-options="fit:true">
				<#--查询start-->
				<div data-options="region:'north',border:false" >
			    	<@p.toolbar id="expToolbar"   listData=[
			    		{"title":"查询","iconCls":"icon-search","action":"billOmDivide.searchExp('N');", "type":0},
	       				{"title":"清除","iconCls":"icon-remove","action":"billOmDivide.searchClear('searchExpNoForm');", "type":0},
	       				{"title":"确定","iconCls":"icon-ok","action":"billOmDivide.selectBillOmExp();", "type":0},
			    		{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billOmDivide.closeWindow('showExpDialog');","type":0}
			        ]/>
					<div nowrap class="search-div" style="padding:10px;">
						<form name="searchExpNoForm" id="searchExpNoForm" method="post" class="city-form">
					 		<table>
					     		<tr>
					     			<td class="common-td">发货通知单号：</td>
					     			<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNo" /></td>
					     			<td class="common-td blank">发货状态：</td>
                     				<td><input class="easyui-combobox ipt" style="width:120px" name="statusExp" id="statusExp" /></td>
									<td class="common-td blank">所属品牌：</td>
                     				<td ><input class="easyui-combobox ipt" style="width:180px" name="brandNoExp" id="brandNoExp" /></td>
					     		</tr>
					     		<tr>
					     			<td class="common-td">创建日期：</td>
					     			<td><input class="easyui-datebox ipt" style="width:120px" name="startCreatetmExp" id="startCreatetmExp" /></td>
					     			<td class="common-line">&mdash;</td>
                     				<td><input class="easyui-datebox ipt" style="width:120px" name="endCreatetmExp" id="endCreatetmExp" /></td>
									<td class="common-td blank"></td>
                     				<td ></td>
					     		</tr>
					     	</table>
					 	</form>
					</div>
				</div>
				<#--查询end-->
				<#--显示列表start-->
				<div data-options="region:'center',border:false">
					<@p.datagrid id="expNoDataGrid" name=""  loadUrl="" saveUrl="" defaultColumn="" 
						isHasToolBar="false" height="280"  onClickRowEdit="false" singleSelect="false" pageSize='10'  
						pagination="true" rownumbers="true" divToolbar="" emptyMsg=""
						columnsJsonList="[
							{field : 'id',checkbox:true},
							{field : 'ownerNo',hidden:true},
							{field : 'expNo',title : '发货通知单号',width : 160,align:'left'},
							{field : 'statusStr',title : '状态',width : 80,align:'left'},
							{field : 'sysNoName',title : '品牌库',width : 80,align:'left'},
							{field : 'totalExpQty',title : '待分配数量',width : 80,align:'right'},
							{field : 'createtm',title : '创建日期',width : 135},
							{field : 'expRemark',title : '备注',width : 160,align:'left'}
							
					]"/>
				</div>
				<#--显示列表end-->
			</div>			
		</div>
</body>
</html>