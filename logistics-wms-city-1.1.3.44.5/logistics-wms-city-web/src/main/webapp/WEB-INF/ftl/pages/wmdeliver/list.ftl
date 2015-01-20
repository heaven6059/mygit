<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退厂确认（配送单）</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/wmdeliver/wmdeliver.js?version=1.0.5.2"></script>    
</head>
<body class="easyui-layout">
      <#-- 工具菜单div -->
	<div data-options="region:'north',border:false">
           <@p.toolbar id="main-toolbar"  listData=[
				{"title":"查询","iconCls":"icon-search","action":"wmdeliver.searchWmDeliver()", "type":0},
				{"title":"清空","iconCls":"icon-remove","action":"wmdeliver.searchWmWmDeliverClear()", "type":0},
                {"title":"新增","iconCls":"icon-add","action":"showAdd()", "type":1},
                {"title":"修改","iconCls":"icon-edit","action":"wmdeliver.showModify()", "type":2},
                {"title":"删除","iconCls":"icon-del","action":"wmdeliver.deleteWmDeliver()","type":3},
				{"title":"审核","iconCls":"icon-aduit","action":"wmdeliver.auditWmDeliver()","type":4},
				{"title":"导出","iconCls":"icon-export","action":"exportExcel()","type":5},
		        {"title":"关闭","iconCls":"icon-close","action":"closeWindow('退厂确认')","type":0}
              ]
			/>
	  </div>
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				 <form name="searchForm" id="searchForm" method="post" class="city-form">
				 	<table>
				 		<tr>
				 			<td class="common-td blank">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
				 			<td><input class="easyui-combobox ipt" style="width:120px" name="status" id="statusCondition" /></td>
				 			<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
				 			<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
				 			<td class="common-td blank">创建日期：</td>
				 			<td><input class="easyui-datebox ipt" style="width:120px" name="createtmBegin" id="createtmBeginCondition" /></td>
				 			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
				 			<td><input class="easyui-datebox ipt" style="width:120px" name="createtmEnd" id="createtmEndCondition" /></td>	
				 		</tr>
				 		<tr>
				 			<td class="common-td blank">单据编号：</td>
				 			<td><input class="easyui-validatebox ipt" style="width:120px" name="deliverNo" id="deliverNoCondition" /></td>
				 			<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
				 			<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
				 			<td class="common-td blank">审核日期：</td>
				 			<td><input class="easyui-datebox ipt" style="width:120px" name="audittmBegin" id="audittmBeginCondition" /></td>
				 			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
				 			<td><input class="easyui-datebox ipt" style="width:120px" name="audittmEnd" id="audittmEndCondition" /></td>
				 		</tr>
				 		<tr>
				 			<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
				 			<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNoCondition"  /></td>
				 			<td class="common-td blank">供&nbsp;应&nbsp;商：</td>
				 			<td><input class="easyui-combobox ipt" style="width:120px" name="supplierNo" id="supplierNoCondition"  /></td>
				 			<td class="common-td blank">退货日期：</td>
				 			<td><input class="easyui-datebox ipt" style="width:120px" name="operateDateBegin" id="operateDateBeginCondition" /></td>
				 			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
				 			<td><input class="easyui-datebox ipt" style="width:120px" name="operateDateEnd" id="operateDateEndCondition" /></td>
				 		</tr>
				 		<tr>
				 			<td class="common-td"> 品&nbsp;牌&nbsp;库：</td>
	             			<td>
	             				<input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoSearch"/>
	             			</td>
	             			<td class="common-td blank">所属品牌：</td>
							<td colspan = '5'><input class="easyui-combobox ipt" style="width:310px" name="brandNo" id="brandNo" /></td>
				 		</tr>
				 		
				 		<input class="easyui-validatebox ipt" style="width:110px" name="locno" id="locno" type="hidden" />
				 	</table>
				</form>
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="退厂配送单"
               isHasToolBar="false" divToolbar="#locSearchDiv"  height="520"  onClickRowEdit="false"    pagination="true"
               rownumbers="true"  emptyMsg="" singleSelect = "false"
               columnsJsonList="[
                  {field : 'ck',title : '',width : 50, checkbox:true},
                  {field : 'locno',title : '仓库编码',width : 100,hidden:true},
                  {field : 'showStatus',title : '单据状态',width : 80,align:'left'},
                  {field : 'deliverNo',title : '单据编号',width : 180},
                  {field : 'ownerNo',title : '货主',width : 100,align:'left', formatter: wms_city_common.columnOwnerFormatter},
                  {field : 'supplierNo',title : '供应商',width : 180,align:'left', formatter: wms_city_common.columnSupplierFormatter},
                  {field : 'operateDate',title : '退货日期',width : 100,sortable:true},
                  {field : 'creator',title : '创建人',align:'left',width : 100},
                  {field : 'creatorName',title : '创建人名称',width : 100,sortable:true},
                  {field : 'createtm',title : '创建时间',width : 125,sortable:true},
                  {field : 'auditor',title : '审核人',align:'left',width : 80},
                  {field : 'auditorName',title : '审核人名称',width : 80,sortable:true},
                  {field : 'audittm',title:'审核时间',width:125,sortable:true},
                  {field : 'editor',title : '编辑人',align:'left',width : 100},
                  {field : 'edittm',title : '编辑时间',width : 125,sortable:true},
                  {field : 'editorName',title : '更新人名称',width : 100}
				  
                 ]" 
                 jsonExtend='{
                 	onDblClickRow:function(rowIndex, rowData){
	               		//双击方法
	                   	wmdeliver.loadDataDetailGrid(rowData);
	                }
                 }'
	           />
			</div>
		</div>
	  </div>
	  
	  
	<#-- 新增  窗口 -->
	<div id="showDialog"  class="easyui-window" title="新增/修改"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
		    		<@p.toolbar id='add-toolbar'  listData=[
	                	{"id":"btn-add","title":"保存","iconCls":"icon-save","action":"saveMain()", "type":1},
	                    {"id":"btn-modify","title":"修改","iconCls":"icon-edit","action":"wmdeliver.modifyWmDeliver('dataForm')", "type":2},
	                    {"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"wmdeliver.closeShowWin('showDialog')","type":0}
		              ]
					 />
					 <form name="dataForm" id="dataForm" method="post" class="city-form">
		    			<table>
		    				<tr>
		    					<td class="common-td blank">单据编号：</td>
		    					<td><input class="easyui-validatebox ipt" style="width:120px" name="deliverNo" id="deliverNo" readOnly='readOnly' missingMessage='编码为必填项!'  data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"  /></td>
		    					<td class="common-td blank">退货日期：</td>
		    					<td><input class="easyui-datebox" style="width:120px" name="operateDate" id="operateDate"  required="true"/></td>
		    					<td class="common-td blank">货主：</td>
		    					<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" required="true" /><input class="easyui-validatebox ipt" style="width:120px" id="ownerNoHide" type="hidden"/></td>
		    					<td class="common-td blank">货主名称： </td>
		    					<td><input class="easyui-validatebox ipt" style="width:120px" name="ownerName" id="ownerName" readOnly='readOnly' /></td>
		    				</tr>
		    				<tr>
		    					<td class="common-td blank">供应商： </td>
		    					<td colspan='3'><input class="easyui-combobox" style="width:310px" name="supplierNo" id="supplierNo" required="true"/><input class="easyui-validatebox ipt" style="width:120px" id="supplierNoHide" type="hidden"/></td>
		    					<#-- 
		    						<td class="common-td blank">供应商名称： </td>
		    						<td><input class="easyui-validatebox ipt" style="width:120px" name="supplierName" id="supplierName" readOnly='readOnly'/> </td>
		    					-->
		    					<td class="common-td blank">备注：</td>
		    					<td colspan="3"><input class="easyui-validatebox ipt" style="width:100%;" name="memo" id="memo" /></td>
		    				</tr>
		    			</table>
					</form>	
		    	</div>
		    	<div data-options="region:'center',border:false">
		    		<@p.toolbar id='detailDiv'  listData=[
	                             {"id":"btn-add","title":"新增商品","iconCls":"icon-add-dtl","action":"wmdeliver.addItemDetail_module('module')", "type":0},
	                             {"id":"btn-modify","title":"删除明细","iconCls":"icon-del-dtl","action":"wmdeliver.removeBySelected('module')", "type":0},
	                             {"id":"btn-save-dtl","title":"保存明细","iconCls":"icon-save-dtl","action":"wmdeliver.save('module')","type":0}
		                       ]
					 />
					 <@p.datagrid id="module" name="" title="退厂配送单明细" loadUrl="" saveUrl="" defaultColumn="" 
				 			isHasToolBar="false"  divToolbar="#detailDiv"  height="300"  onClickRowEdit="false" singleSelect="true" pageSize='20'  
							pagination="true" rownumbers="true" emptyMsg=""
				 			columnsJsonList="[
				 				{field : 'supplierName',title : '供应商名称',width : 180,align:'left'},
				 				{field : 'recheckNo',title:'复核单号 ',width : 180},
					 			{field : 'containerNo',title:'容器号',width:100,hidden:true,align:'left'},
					 			{field : 'boxNo',title:'箱号',width:120,align:'left'},
								{field : 'supplierNo',hidden:true},
								{field : 'recedeNo',hidden:true},
								{field : 'sumQty',title:'总数量',width:80,align:'right'}
				 			]"
				 		/>
		    	</div>
		    </div>
</div>	

<#-- 修改/查看 窗口 -->
<div id="showEditDialog"  class="easyui-window" title="修改/查看"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<form name="dataEditForm" id="dataEditForm" method="post" class="city-form">
    			<table>
    				<tr>
    					<td class="common-td blank">单据编号：</td>
    					<td><input class="easyui-validatebox ipt" style="width:120px" name="deliverNo" id="deliverNo" readOnly='readOnly' missingMessage='编码为必填项!'  data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"  /></td>
    					<td class="common-td blank">退货日期：</td>
    					<td><input class="easyui-datebox" style="width:120px" name="operateDate" id="operateDate"  required="true"/></td>
    					<td class="common-td blank">货主：</td>
    					<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" required="true" /><input class="easyui-validatebox ipt" style="width:120px" name="ownerNo" id="ownerNoHide" type="hidden"/></td>
    					<td class="common-td blank">货主名称： </td>
    					<td><input class="easyui-validatebox ipt" style="width:120px" name="ownerName" id="ownerName" readOnly='readOnly' /></td>
    				</tr>
    				<tr>
    					<td class="common-td blank">供应商： </td>
    					<td colspan='3'><input class="easyui-combobox" style="width:310px" name="supplierNo" id="supplierNo" required="true"/> <input class="easyui-validatebox ipt" style="width:120px" name="supplierNo" id="supplierNoHide" type="hidden"/></td>
    					<td class="common-td blank" style="display:none;">供应商名称： </td>
    					<td style="display:none;"><input class="easyui-validatebox ipt" style="width:120px" name="supplierName" id="supplierName" readOnly='readOnly'/></td>
    					<td class="common-td blank">备注：</td>
    					<td colspan="3"><input class="easyui-validatebox ipt" style="width:100%" name="memo" id="memo" /></td>
    				</tr>
    				<tr style="display:none;">
    					<td class="common-td blank">创建人： </td>
    					<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creator"  disabled="disabled"/> </td>
    					<td class="common-td blank">创建时间： </td>
    					<td><input class="easyui-validatebox ipt" style="width:120px" name="createtm" id="createtm"  disabled="disabled"/></td>
    					<td class="common-td blank">编辑人：</td>
    					<td><input class="easyui-validatebox ipt" style="width:120px" name="editor" id="editor"  disabled="disabled"/></td>
    					<td class="common-td blank">编辑时间：</td>
    					<td><input class="easyui-validatebox ipt" style="width:120px" name="edittm" id="edittm"  disabled="disabled"/></td>
    				</tr>
    			</table>
			</form>	
		</div>
		<div data-options="region:'center',border:false">
			<#-- 
			<@p.toolbar id='detailEditDiv'  listData=[
                 {"id":"btn-add","title":"新增商品","iconCls":"icon-add","action":"wmdeliver.addItemDetail_module('moduleEdit')", "type":0},
                 {"id":"btn-modify","title":"删除明细","iconCls":"icon-del-dtl","action":"wmdeliver.removeBySelected('moduleEdit')", "type":0},
                 {"id":"btn-canel","title":"保存明细","iconCls":"icon-add-dtl","action":"wmdeliver.save('moduleEdit')","type":0}
	           ]
			/>
			-->
			<@p.datagrid id="moduleEdit" name="" title="退厂配送单明细" loadUrl="" saveUrl="" defaultColumn="" 
					isHasToolBar="false"  divToolbar="#detailEditDiv"  height="400"  onClickRowEdit="false" singleSelect="true" pageSize='20'  
					pagination="true" rownumbers="true" showFooter="true"
				 	columnsJsonList="[
				 		{field:'poId',title:'序号',width:70,hidden:true},
				 		{field:'containerNo',title:'',width:70,hidden:true},
				 		{field:'boxNo',title:'标签号',width:100,align:'left'},
				 		{field:'itemNo',title:'商品编码',width:140,align:'left'},
				 		{field:'sizeNo',title:'尺码',width:80,align:'left'},
				 		{field:'itemName',title:'商品名称',width:170,align:'left'},
				 		{field:'styleNo',title:'款号',width:125,align:'left',hidden:true},
				 		{field:'colorNoStr',title:'颜色',width:80,align:'left'},
						{field:'itemQty',title:'数量',width:80,align:'right'},
						{field:'recheckNo',title:'复核单号',width:180},
				 		{field:'recedeNo',title:'退厂通知单号',width:180}
				 ]"
			/>
		</div>
	</div>
</div>	

<#-- 退厂配送单/选择箱号div -->
<div id="openUIItem"  class="easyui-window" title="退厂配送单/选择箱号"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id='detailSelect'  listData=[
                 {"title":"查询","iconCls":"icon-search","action":"wmdeliver.searchItem()", "type":0},
                 {"title":"清空","iconCls":"icon-remove","action":"wmdeliver.clearFormById('itemSearchForm')", "type":0},
                 {"title":"确定","iconCls":"icon-ok","action":"wmdeliver.confirmItem()","type":0},
                 {"title":"取消","iconCls":"icon-cancel","action":"wmdeliver.closeShowWin('openUIItem')","type":0}
	           ]
			/>
			<form name="itemSearchForm" id="itemSearchForm" metdod="post" style="padding:8px;">
			 	通知单号：<input class="easyui-validatebox ipt" style="width:130px" name="recedeNo" id="recedeNoSel"   disable="true"/>
			 	复核单号：<input class="easyui-validatebox ipt" style="width:130px" name="recheckNo" id="recheckNoSel"   disable="true"/>
			 	<input class="easyui-validatebox ipt" style="width:130px" name="showGirdName" id="showGirdName"   disable="true"  type ="hidden" />
			 	<input class="easyui-validatebox ipt" style="width:120px" name="supplierNo" id="supplierNoSel" type ="hidden"/>
			 	<input class="easyui-validatebox ipt" style="width:120px" name="locno" id="locnoSel" type ="hidden"/>
			</form>
		</div>
		<div data-options="region:'center',border:false">
  	  		<@p.datagrid id="dataGridJGItem"  loadUrl=""  saveUrl=""  defaultColumn="" 
               isHasToolBar="false" divToolbar="#itemSearchDiv" height="300"  onClickRowEdit="false" singleSelect="false" pageSize='20'  
			   pagination="true" rownumbers="true"
	           columnsJsonList="[
	           		{field : 'ck',title : '',width : 50, checkbox:true},
	           		{field : 'supplierName',title : '供应商名称',width : 180,align:'left'},
	                {field : 'recedeNo',title : '退厂通知单号',width : 180},
	                {field : 'labelNo',title : '标签号',width : 110,align:'left'},
	                {field : 'recheckNo',title : '复核单号',width : 180},
	                {field : 'sumQty',title : '数量',width : 90,align:'right'},
	                {field : 'containerNo',title:'容器号',width:100,hidden:true,align:'left'}
	            ]" 
		        />
		</div>
	</div>	    
</div>
<#-- 退厂确认（配送单） -->
</body>
</html>