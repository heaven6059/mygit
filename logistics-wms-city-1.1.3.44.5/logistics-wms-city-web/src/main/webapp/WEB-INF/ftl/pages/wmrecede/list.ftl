<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退厂通知单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/wmrecede/wmrecede.js?version=1.0.6.2"></script>
</head>

<body class="easyui-layout">

	<#-- 工具菜单div -->
    <div data-options="region:'north',border:false" class="toolbar-region">
    	<@p.toolbar id="toolbar" listData=[
    		{"title":"查询","iconCls":"icon-search","action":"wmrecede.searchWmRecede()", "type":0},
		 	{"title":"清除","iconCls":"icon-remove","action":"wmrecede.searchWmRecedeClear()", "type":0},
        	{"title":"新增","iconCls":"icon-add","action":"wmrecede.showAdd()", "type":1},
            {"title":"修改","iconCls":"icon-edit","action":"wmrecede.showModify()", "type":2},
            {"title":"删除","iconCls":"icon-del","action":"wmrecede.deleteWmrecede()","type":3},
			{"title":"审核","iconCls":"icon-aduit","action":"wmrecede.auditWmrecede()","type":4},
			{"title":"导出","iconCls":"icon-export","action":"exportExcel()","type":5},
			{"title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
	    ]/>
	</div>
	
	<div data-options="region:'center',border:false">
		<#-- 查询条件 start -->
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			
			<div data-options="region:'north',border:false" >
				<div id="locSearchDiv" style="padding:10px;">
		       		<form name="searchForm" id="searchForm" method="post" class="city-form">
		       			<table>
							<tr>
								<td class="common-td">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="status" id="statusCondition" /></td>
								<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="createtmBegin" id="createtmBeginCondition" /></td>
								<td class="common-line">&mdash;</td>
								<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="createtmEndCondition" /></td>
							</tr>
							<tr>
								<td class="common-td">退厂类型：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="recedeType" id="recedeTypeCondition"  /></td>
								<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
								<td class="common-td blank">审核日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="audittmBegin" id="audittmBeginCondition" /></td>
								<td class="common-line">&mdash;</td>
								<td>
									<input class="easyui-datebox" style="width:120px" name="audittmEnd" id="audittmEndCondition" />
								</td>
							</tr>
							
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="recedeNo" id="recedeNoCondition" /></td>
								<td class="common-td blank">来源单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo" id="poNoCondition" /></td>
								<td class="common-td blank">退厂日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="recedeDateBegin" id="recedeDateBeginCondition" /></td>
								<td class="common-line" width='40'>&mdash;</td>
								<td><input class="easyui-datebox" style="width:120px" name="recedeDateEnd" id="recedeDateEndCondition" /></td>
							</tr>
							
							<tr>
								<td class="common-td">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNoCondition"  /></td>
								<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
								<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
								<td class="common-td blank">所属品牌：</td>
								<td colspan='3'><input class="easyui-combobox ipt" style="width:280px" name="brandNo" id="brandNo" /></td>
							</tr>
							<tr>
								<td class="common-td blank"> 供&nbsp;应&nbsp;商：</td>
		             			<td  colspan='7'>
		             				<input class="easyui-combobox ipt" style="width:310px" name="supplierNo" id="supplierNoCondition"  />
		             			</td>
							</tr>
						</table>
						<input class="easyui-validatebox" style="width:110px" name="locno" id="locno" type="hidden" />
					</form>
				</div>
			</div>
			
			<div data-options="region:'center',border:false" >
				<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="退厂通知单"
		                   isHasToolBar="false" divToolbar=""  height="520"  onClickRowEdit="false"    pagination="true"
			               rownumbers="true"  singleSelect = "false" emptyMsg=""
			               columnsJsonList="[
			                  {field : 'ck',title : '',width : 50, checkbox:true},
			                  {field : 'locno',title : '仓库编码',width : 80,hidden:true},
			                  {field : 'showStatus',title : '单据状态',width : 80, align:'left'},
			                  {field : 'recedeNo',title : '单据编号',width : 180},
			                  {field : 'poNo',title : '来源单号',width : 180},
			                  {field : 'ownerNo',title : '货主',width : 80, align:'left', formatter: wms_city_common.columnOwnerFormatter},
			                  {field : 'supplierName',title : '供应商',width : 180, align:'left'},
			                  {field : 'showRecedeType',title : '退厂类型',width : 100, align:'left'},
			                  {field : 'recedeDate',title : '退厂日期',width : 100,sortable:true},
			                  {field : 'creator',title : '创建人',width : 100, align:'left'},
			                   {field : 'creatorName',title : '创建人名称',width : 100, sortable:true},
			                  {field : 'createtm',title : '创建时间',width : 125,sortable:true},
			                  {field : 'auditor',title : '审核人',width : 100, align:'left'},
			                  {field : 'auditorName',title : '审核人名称',width : 100, sortable:true},
 			                  {field : 'audittm',title:'审核时间',width:125,sortable:true},
 			                  {field : 'editor',title : '更新人',width : 100,sortable:true},
							  {field : 'editorName',title : '更新人名称',width : 100,sortable:true},
						      {field : 'edittm',title : '更新时间',width : 130,sortable:true},
 			                  {field : 'memo',hidden:true}
			                 ]" 
			                 jsonExtend='{
			                 	onDblClickRow:function(rowIndex, rowData){
				                	  //双击方法
				                   	  wmrecede.showView(rowData,rowIndex);
				                }
		                     }'
				/>
			</div>
		</div>
	</div>
	
	<#-- 新增  窗口 -->
	<div id="showDialog"  class="easyui-window" title="新增"  
		   data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    minimizable:false,maximizable:false,maximized:true">
		    
		    <div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<@p.toolbar id="addtoolbar" listData=[
	                 		{"id":"btn-add","title":"保存","iconCls":"icon-save","action":"wmrecede.saveMain();", "type":1},
	                        {"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"wmrecede.closeShowWin('showDialog')","type":0}
		            ]/>
				
					<form name="dataForm" id="dataForm" method="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="recedeNo" id="recedeNo" required="true" readOnly='readOnly' missingMessage='编码为必填项!'/></td>
								<td class="common-td blank">退厂类型：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="recedeType" id="recedeType"   required="true" /></td>
								<td class="common-td blank">货主：</td>
								<td>
									<input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNo" required="true" />
								  	<input class="easyui-validatebox" style="width:120px" name="ownerNo" id="ownerNoHide" type="hidden"/>
								</td>
								<td class="common-td blank">供应商： </td>
								<td>
									<input class="easyui-combobox ipt" style="width:120px" name="supplierNo" id="supplierNo" required="true"/> 
					           		<input class="easyui-validatebox" style="width:120px" name="supplierNo" id="supplierNoHide" type="hidden"/>
								</td>
							</tr>
							<tr>
								<td class="common-td">品牌库：</td>
								<td>
									<input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNo" required="true" />
								  	<input class="easyui-validatebox" style="width:120px" name="sysNo" id="sysNoHide" type="hidden"/>
								</td>
								<td class="common-td blank"> 退厂日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="recedeDate" id="recedeDate"  required="true"/></td>
								<td class="common-td">备注：</td>
								<td colspan='3'><input class="easyui-validatebox ipt" style="width:300px" name="memo" id="memo" /></td>
							</tr>
						</table>
					 </form>
				</div>
				
				<div id = 'detailDiv' data-options="region:'center',border:false">
					<div id="dtlToolDiv">
						<@p.toolbar id="dtltoolbar" listData=[
	                 		{"title":"新增明细","iconCls":"icon-add-dtl","action":"wmrecede.addItemDetail_module('module')", "type":0},
	                        {"title":"删除明细","iconCls":"icon-del-dtl","action":"wmrecede.removeBySelected('module')", "type":0},
	                        {"title":"保存明细","iconCls":"icon-save-dtl","action":"wmrecede.save('module')","type":0},
	                        {"title":"模板下载","iconCls":"icon-download","action":"wmrecede.downloadTemp()","type":0},
	                        {"title":"导入","iconCls":"icon-import","action":"wmrecede.showImport('dataForm','showDialog')","type":0}
		            	]/>
			 		</div>
			 		<@p.datagrid id="module" name="" title="退厂通知单明细" loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  divToolbar="#dtlToolDiv"  height="300"  onClickRowEdit="true" singleSelect="true" pageSize='20'  
							pagination="true" rownumbers="true" emptyMsg="" 
				 			columnsJsonList="[
				 				{field:'id',title:'编号',width:70,hidden:true},
				 				{field:'itemNo',title:'商品编码',width:130, align:'left'},
				 				{field : 'itemName',title : '商品名称',width : 160, align:'left'},
				 				{field:'sizeNo',title:'尺码',width:90, align:'left'},
				 				{field:'colorNoStr',title:'颜色',width:90, align:'left'},
				 				{field:'brandNoStr',title:'品牌',width:90, align:'left'},
				 				{field : 'conContentQty',title : '可用库存数量',width : 90, align:'left'},
							    {field:'recedeQty',title:'退厂数量',width:60, align:'right',
							    	editor:{
				 						type:'numberbox',
				 						options:{
					 						required:true,
					 						missingMessage:'退厂数量为必填项!'
				 						}
				 					}
							    }
				 			]"
				 	/>
				</div>
			</div>
			
	</div>	

	<#-- 修改 窗口 -->
	<div id="showEditDialog"  class="easyui-window" title="修改"  
		   data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		   minimizable:false,maximizable:false,maximized:true">
		   
		   	<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<@p.toolbar id='edittoolbar' listData=[
	                	{"id":"btn-add-edit","title":"修改","iconCls":"icon-edit","action":"wmrecede.modifyWmrecede('dataEditForm')", "type":2},
	                    {"id":"btn-canle-edit","title":"取消","iconCls":"icon-cancel","action":"wmrecede.closeShowWin('showEditDialog')", "type":0}
		            ]/>
		            
		            <form name="dataEditForm" id="dataEditForm" method="post" class="city-form" style="padding:10px;">
			        	<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="recedeNo" id="recedeNo" required="true" readOnly='readOnly' missingMessage='编码为必填项!'  /></td>
								<td class="common-td blank">退厂类型：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="recedeType" id="recedeType"   required="true" /></td>
								<td class="common-td blank">货主：</td>
								<td>
									<input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNo" required="true" />
								  	<input class="easyui-validatebox" style="width:120px" name="ownerNo" id="ownerNoHide" type="hidden"/>
								</td>
								<td class="common-td blank">供应商：</td>
								<td>
									<input class="easyui-combobox ipt" style="width:120px" name="supplierNo" id="supplierNo" required="true"/> 
									<input class="easyui-validatebox" style="width:120px" name="supplierNo" id="supplierNoHide" type="hidden"/>
								</td>
							</tr>
										
							<tr>
								<td class="common-td">品牌库：</td>
								<td>
									<input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNo" required="true" />
								  	<input class="easyui-validatebox" style="width:120px" name="sysNo" id="sysNoHide" type="hidden"/>
								</td>
								<td class="common-td blank"> 退厂日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="recedeDate" id="recedeDate"  required="true"/></td>
								<td class="common-td">备注：</td>
								<td colspan='3'><input class="easyui-validatebox ipt" style="width:300px" name="memo" id="memo" /></td>
							</tr>
							
							
						</table>
					</form>
				</div>
				
				<div id = 'detailEditDiv' data-options="region:'center',border:false">
					<div id="dtlToolDivEdit">
						<@p.toolbar id="dtledittoolbar" listData=[
	                 		{"title":"新增明细","iconCls":"icon-add-dtl","action":"wmrecede.addItemDetail_module('moduleEdit')", "type":0},
	                        {"title":"删除明细","iconCls":"icon-del-dtl","action":"wmrecede.removeBySelected('moduleEdit')", "type":0},
	                        {"title":"保存明细","iconCls":"icon-save-dtl","action":"wmrecede.save('moduleEdit')","type":0},
	                        {"title":"模板下载","iconCls":"icon-download","action":"wmrecede.downloadTemp()","type":0},
	                        {"title":"导入","iconCls":"icon-import","action":"wmrecede.showImport('dataEditForm','showEditDialog')","type":0}
		            	]/>
			 		</div>
			 		<@p.datagrid id="moduleEdit" name="" title="退厂通知单明细" loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  divToolbar="#dtlToolDivEdit"  height="400"  onClickRowEdit="true" 
				 			singleSelect="false" pageSize='20'  
							pagination="true" rownumbers="true" emptyMsg="" 
				 			columnsJsonList="[
				 				{field:'poId',title:'序号',width:70,hidden:true},
				 				{field:'itemNo',title:'商品编码',width:135, align:'left'},
				 				{field : 'itemName',title : '商品名称',width : 160, align:'left'},
				 				{field:'sizeNo',title:'尺码',width:90, align:'left'},
				 				{field:'colorNoStr',title:'颜色',width:90, align:'left'},
				 				{field:'brandNoStr',title:'品牌',width:90, align:'left'},
				 				{field : 'packQty',hidden:true},
				 				{field : 'conContentQty',title : '可用库存数量',width : 90, align:'left'},
							    {field:'recedeQty',title:'退厂数量',width:60, align:'right',
							    	editor:{
				 						type:'numberbox',
				 						options:{
					 						required:true,
					 						missingMessage:'退厂数量为必填项!'
				 						}
				 					}
							    }
				 			]"
				 	/>
				</div>
			</div>
	</div>	

	<#-- 商品选择div -->
	<div id="openUIItem"  class="easyui-window" title="选择商品"  
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		minimizable:false,maximizable:false,maximized:true">
		
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
		            <@p.toolbar id="itemtoolbar" listData=[
			    		{"title":"查询","iconCls":"icon-search","action":"wmrecede.searchItem()", "type":0},
					 	{"title":"清除","iconCls":"icon-remove","action":"wmrecede.itemClear()", "type":0},
			        	{"title":"确定","iconCls":"icon-ok","action":"wmrecede.confirmItem()", "type":0},
						{"title":"取消","iconCls":"icon-cancel","action":"wmrecede.closeShowWin('openUIItem')","type":0}
				    ]/>
				    
				    <form name="itemSearchForm" id="itemSearchForm" metdod="post" class="city-form" style="padding:10px;">
				    	<table>
							<tr>
								<td class="common-td">商品编码：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo" disable="true"/></td>
								<td class="common-td blank">商品名称：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemName" disable="true"/></td>
								<td class="common-td blank">商品条码：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcode" disable="true"/></td>
				    			<td class="common-td blank">所属品牌：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoIt" /></td>
				    		</tr>
				    	</table>
				    	<input class="easyui-validatebox" style="width:120px" name="showGirdName" id="showGirdName" disable="true"  type ="hidden" />
						<input class="easyui-validatebox" style="width:120px" name="supplierNo" id="supplierNoForItem" disable="true"  type ="hidden"/> 
						<input class="easyui-validatebox" style="width:120px" name="sysNo" id="sysNoForItem" type ="hidden" />
						<input class="easyui-validatebox" style="width:120px" name="areaQuality" id="areaQualityForItem" type ="hidden" />						
						<input class="easyui-validatebox" style="width:120px" name="itemType" id="itemTypeForItem" type ="hidden" />					
						<input class="easyui-validatebox" style="width:120px" name="recedeType" id="recedeTypeForItem" type ="hidden" />
				    </form>
				</div>
				
				<div data-options="region:'center',border:false">
					<#-- 商品选择数据列表div -->
          	  		<@p.datagrid id="dataGridJGItem"  loadUrl=""  saveUrl=""  defaultColumn="" 
		               isHasToolBar="false" divToolbar="" onClickRowEdit="false" singleSelect="false" pageSize='20'  
					   pagination="true" rownumbers="true" emptyMsg=""
			           columnsJsonList="[
			           		{field : 'ck',title : '',width : 50, checkbox:true},
			           		{field : 'qminOperatePacking',hidden:true},
			           		{field : 'itemNo',title : '商品编码 ',width : 140, align:'left'},
			                {field : 'itemName',title : '商品名称',width : 160, align:'left'},
			                {field : 'sizeNo',title : '尺码',width : 80, align:'left'},
			                {field : 'barcode',title : '商品条码',width : 140, align:'left'},
			                {field : 'colorNoStr',title : '颜色',width : 80, align:'left'},
			                {field : 'brandNoStr',title : '品牌',width : 100, align:'left'},
			                {field : 'conContentQty',title : '可用库存数量',width : 90, align:'left'}
			                
			                
			       ]" />
				</div>
			</div>
	</div>

	<#-- 查看 窗口   - 带尺码横排  -->
	<div id="showViewDialog"  class="easyui-window" title="查看"
		data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		minimizable:false,maximizable:false,maximized:true">
		
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<form name="dataViewForm" id="dataViewForm" method="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td class="common-td">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="recedeNo" id="recedeNo" required="true" readOnly='readOnly' missingMessage='编码为必填项!'  /></td>
								<td class="common-td blank">退厂类型：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="recedeType" id="recedeType"   required="true" /></td>
								<td class="common-td blank">货主：</td>
								<td>
									<input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNo" required="true" />
							  		<input class="easyui-validatebox" style="width:120px" name="ownerNo" id="ownerNoHide" type="hidden"/>
								</td>
								<td class="common-td blank">供应商：</td>
								<td>
									<input class="easyui-combobox ipt" style="width:120px" name="supplierNo" id="supplierNo" required="true"/> 
									<input class="easyui-validatebox" style="width:120px" name="supplierNo" id="supplierNoHide" type="hidden"/>
								</td>
							</tr>
										
							<tr>
								<td class="common-td">品牌库：</td>
								<td>
									<input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNo" required="true" />
							  		<input class="easyui-validatebox" style="width:120px" name="sysNo" id="sysNoHide" type="hidden"/>
								</td>
								<td class="common-td blank"> 退厂日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="recedeDate" id="recedeDate"  required="true"/></td>
								<td class="common-td">备注：</td>
								<td colspan='3'><input class="easyui-validatebox ipt" style="width:300px" name="memo" id="memo" /></td>
							</tr>
							
							
						</table>
					</form>
				</div>
				
				<div data-options="region:'center',border:false">
					<@p.datagrid id="moduleView" name="" title="退厂通知单明细" loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  divToolbar="#"  height="400"  onClickRowEdit="false" singleSelect="true" pageSize='20'  
							pagination="true" rownumbers="true"  jsonExtend="{}"  columnsJsonList="" emptyMsg=""    showFooter="true"
				 	/>
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