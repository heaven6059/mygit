<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>其它出库</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billsmwaste/billsmwaste.js?version=1.0.8.1"></script>
    <!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"billsmwaste.searchData();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"billsmwaste.clearSearch('searchForm');", "type":0},
	        {"title":"新增","iconCls":"icon-add","action":"billsmwaste.addUI();", "type":1},
	        {"title":"修改","iconCls":"icon-edit","action":"billsmwaste.updateUI();","type":2},
	        {"title":"删除","iconCls":"icon-del","action":"billsmwaste.deleteUI();","type":3},
	        {"title":"审核","iconCls":"icon-aduit","action":"billsmwaste.check();","type":4},
	        {"title":"打印预览","iconCls":"icon-print","action":"billsmwaste.print();","type":4},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
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
						<table>
				        	<tr>
				            	<td class="common-td">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="status" id="statusCondition" /></td>
				                <td class="common-td blank">创&nbsp;建&nbsp;人：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
				                <td class="common-td blank">创建日期：</td>
				                <td><input class="easyui-datebox" style="width:120px" name="startCreatetm" id="startCreatetmCondition" /></td>
				                <td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
				                <td><input class="easyui-datebox" style="width:120px" name="endCreatetm" id="endCreatetmCondition"
				                 	data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/></td>
				        	</tr>
				        	<tr>
				            	<td class="common-td">单据编号：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="wasteNo" id="wasteNoCondition" /></td>
				                <td class="common-td blank">审&nbsp;核&nbsp;人：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditorCondition" /></td>
				                <td class="common-td blank">审核日期：</td>
				                <td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittmCondition" /></td>
				                <td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
				                <td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittmCondition" 
									data-options="validType:['vCheckDateRange[\'#startAudittmCondition\',\'结束日期不能小于开始日期\']']"/></td>
				        	</tr>
				        	<tr>
				            	<td class="common-td">出库原因：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="wasteType" id="wasteTypeCondition" /></td>
				              	<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		             			<td class="common-td blank">品&nbsp;&nbsp;&nbsp;&nbsp;牌：</td>
				                <td colspan="3"><input class="easyui-combobox ipt" style="width:264px" name="brandNo" id="brandNoCondition" /></td>
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
      		<#--查询end-->
			<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    		<@p.datagrid id="dataGridJG"  loadUrl=""
	    	  		saveUrl=""  defaultColumn=""  title="出库列表"
		    		isHasToolBar="false" divToolbar="" height="390"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
					rownumbers="true"  emptyMsg="" showFooter="true"
					columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'status',title : '状态 ',width : 80,formatter:billsmwaste.statusFormatter,align:'left'},
			                {field : 'wasteNo',title : '单据编号',width : 180},
			                {field:'wasteQty',title:'出库数量',width:80,align:'right'},
			                {field : 'ownerNo',title : '货主',width : 80,formatter:billsmwaste.ownerNoFormatter,align:'left'},
			                {field : 'wasteType',title : '出库原因',width : 80,formatter:billsmwaste.typesFormatter,align:'left'},
			                {field : 'wasteDate',title : '出库日期 ',width : 100},
			                {field : 'creator',title : '创建人',width : 80,align:'left'},
			                {field : 'creatorname',title : '创建人名称',width : 80,align:'left'},
			                {field : 'createtm',title : '创建时间',width : 130},
			                {field : 'auditor',title : '审核人',width : 80,align:'left'},
			                {field : 'auditorname',title : '审核人名称',width : 80,align:'left'},
			                {field : 'audittm',title : '审核时间',width : 130},
			                {field : 'remark',title : '备注',width : 130,align:'left'}
			            ]" 
					jsonExtend='{onLoadSuccess:function(data){
									billsmwaste.onLoadSuccess(data);
							},onDblClickRow:function(rowIndex, rowData){
						        	// 触发点击方法  调JS方法
						        	billsmwaste.dtlView(rowData,"view");
					}}'
				/>
			</div>
        	<#--显示列表end-->
	    </div>
	</div>  
	<#-- 主表end -->
	
	<#-- 新增修改页面 BEGIN -->
	<div id="openUI"  class="easyui-dialog" title="新增"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="addtoolbar"   listData=[
		        	{"id":"save_main","title":"保存","iconCls":"icon-save","action":"billsmwaste.saveMain();", "type":1},
		        	{"id":"info_save","title":"修改","iconCls":"icon-edit","action":"billsmwaste.editMain();", "type":2},
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billsmwaste.closeWindow('openUI');","type":0}
		        ]/>
				<div nowrap class="search-div" style="padding:10px;">
					<form name="dataForm" id="dataForm" method="post" class="city-form">
						<table>
							<tr>
						    	<td class="common-td">单据编号：</td>
						        <td><input class="easyui-validatebox ipt" style="width:120px" name="wasteNo" id="wasteNo" readOnly="readOnly"/></td>
						        <td class="common-td blank">出库原因：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="wasteType" id="wasteType" data-options="required:true" /></td>
						        <td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNo" data-options="required:true" /></td>
						        <td class="common-td blank">出库日期：</td>
						        <td><input class="easyui-datebox" style="width:120px" name="wasteDate" id="wasteDate" data-options="required:true" /></td>
						    </tr> 
						    <tr>
						    	<td class="common-td">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						        <td colspan="7"><input class="easyui-validatebox ipt" style="width:680px;" name="remark" id="remark"/></td>
						    </tr>             
						 </table>
					</form>
				</div>
				
			</div>
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false" id = 'wasteDtl'>
				<@p.toolbar id="toolsDiv" listData=[
		        	{"title":"按零散新增","iconCls":"icon-add-dtl","action":"billsmwaste.showAddItem('0');", "type":0},
		        	{"title":"按整箱新增","iconCls":"icon-add-dtl","action":"billsmwaste.showAddItem('1');", "type":0},
		        	{"title":"删除明细","iconCls":"icon-del-dtl","action":"billsmwaste.deleteItem('wasteDtlDg');", "type":0},
		       		{"title":"保存明细","iconCls":"icon-save-dtl","action":"billsmwaste.doSave('wasteDtlDg');","type":0},
		       		{"title":"模板下载","iconCls":"icon-download","action":"billsmwaste.downloadTemp()","type":0},
	                {"title":"导入","iconCls":"icon-import","action":"billsmwaste.showImport('dataForm','showDialog')","type":0},
	                {"title":"按库保存","iconCls":"icon-save","action":"billsmwaste.saveShipment();","type":0}
		        ]/>
		        <@p.datagrid id="wasteDtlDg" name=""   loadUrl="" saveUrl="" 
		        	defaultColumn="" title="出库明细" onClickRowAuto="false"
					isHasToolBar="false" divToolbar="#toolsDiv"  
					onClickRowEdit="false" singleSelect="true" pageSize='20'  
					pagination="true" rownumbers="true" emptyMsg="" 
					columnsJsonList="[
				 		{field:'id',width : 50, checkbox:true},
					 	{field:'cellNo',title:'储位编码',width:90,align:'left'},
					 	{field : 'panNo',title : '父容器号',width : 130,align:'left'},
			      		{field : 'boxNo',title : '容器号',width : 120,align:'left'},
						{field:'itemNo',title:'商品编码',width:140,align:'left'},
						{field:'itemName',title:'商品名称',width : 135,align:'left'},
						{field:'sizeNo',title:'尺码',width:80,align:'left'},
						{field : 'itemType',title : '商品属性',width : 100,align:'left',formatter:wms_city_common.columnItemTypeFormatter},
			            {field : 'quality',title : '商品品质',width : 80,align:'left',formatter:wms_city_common.columnQualityFormatter},
						{field:'brandNo',title:'品牌编码',width:100,align:'left'},
						{field:'brandName',title:'品牌',width:100,align:'left'},
						{field:'colorName',title:'颜色',width:80,align:'left'},
						{field:'wasteQty',title:'出库数量',width:80,align:'right',
							editor:{
				 				type:'numberbox',
				 				options:{
					 				required:true,
					 				missingMessage:'出库数量为必填项!'
				 				}
				 			}
				 		},
				 		{field:'conQty',title:'库存数量',width:80,align:'right',hidden:true}
				 	]" 
				 	jsonExtend='{onClickRow:function(rowIndex, rowData){
		        		// 触发点击方法  调JS方法
		        		billsmwaste.onClickRowDtl(rowIndex, rowData);
		     		}}'
		     	/>
			</div>
			<#--显示列表end-->
		</div>
	</div>
	<#-- 新增修改页面 END -->
	
	<#-- 查看明细信息div BEGIN -->
	<div id="openDtlUI"  class="easyui-dialog" title="明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
		<#--查询start-->
			<div data-options="region:'north',border:false" >
		    	<@p.toolbar id="viewtoolbar"   listData=[
		        	{"title":"导出","iconCls":"icon-export","action":"billsmwaste.exportDtl();","type":0},
		        	{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billsmwaste.closeWindow('openDtlUI');","type":0}
		        ]/>
				<div nowrap class="search-div" style="padding:10px;">
					<form name="detailForm" id="detailForm" method="post" class="city-form">
						<table>
							<tr>
						    	<td class="common-td">单据编号：</td>
						        <td><input class="easyui-validatebox ipt" style="width:120px" name="wasteNoDtl" id="wasteNoDtl" readOnly="readOnly"/></td>
						        <td class="common-td blank">出库原因：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="wasteTypeDtl" id="wasteTypeDtl" data-options="required:true" /></td>
						        <td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						        <td><input class="easyui-combobox" style="width:120px" name="ownerNoDtl" id="ownerNoDtl" data-options="required:true" /></td>
						        <td class="common-td blank">出库日期：</td>
						        <td><input class="easyui-datebox" style="width:120px" name="wasteDateDtl" id="wasteDateDtl" data-options="required:true" /></td>
						    </tr> 
						    <tr>
						    	<td class="common-td">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						        <td colspan="7"><input class="easyui-validatebox ipt" style="width:680px;" name="remarkDtl" id="remarkDtl"/></td>
						    </tr>             
						 </table>
					</form>
				</div>
				
			</div>
			<#--查询end-->
			<#--显示列表start-->
			<div data-options="region:'center',border:false">
				<@p.datagrid id="detaildataGrid" name=""   loadUrl="" saveUrl="" defaultColumn="" title="出库明细"
					isHasToolBar="false" divToolbar=""  onClickRowEdit="false" singleSelect="true" pageSize='20'  
					pagination="true" rownumbers="true" emptyMsg=""  showFooter="true"
					columnsJsonList="[
					 	{field:'cellNo',title:'储位编码',width:90,align:'left'},
						{field:'itemNo',title:'商品编码',width:140,align:'left'},
						{field:'panNo',title : '父容器号',width : 130,align:'left'},
			      		{field:'boxNo',title : '容器号',width : 120,align:'left'},
						{field:'itemName',title:'商品名称',width : 130,align:'left'},
						{field:'sizeNo',title:'尺码',width:80,align:'left'},
						{field : 'itemTypeStr',title : '商品属性',width : 100,align:'left'},
			            {field : 'qualityStr',title : '商品品质',width : 80,align:'left'},
						{field:'brandName',title:'品牌',width:100,align:'left'},
						{field:'colorName',title:'颜色',width:80,align:'left'},
						{field:'wasteQty',title:'出库数量',width:80,align:'right'}
				 	]"
				/>
			</div>
			<#--显示列表end-->	
			</div>
		</div>
		<#-- 查看明细信息div END -->
		
		<#-- 商品选择div -->
		<div id="openUIItem"  class="easyui-dialog" title="商品选择"  
		    	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    	maximized:true,minimizable:false,maximizable:false"> 
			<div class="easyui-layout" data-options="fit:true">
				<#--查询start-->
				<div data-options="region:'north',border:false" >
			    	<@p.toolbar id="itemtoolbar"   listData=[
			    		{"title":"查询","iconCls":"icon-search","action":"billsmwaste.searchItem();", "type":0},
	       				{"title":"清除","iconCls":"icon-remove","action":"billsmwaste.clearSearch('itemSearchForm');", "type":0},
	        			{"title":"确定","iconCls":"icon-ok","action":"billsmwaste.confirmItem();", "type":0},
			    		{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billsmwaste.closeWindow('openUIItem');","type":0}
			        ]/>
					<div nowrap class="search-div" style="padding:10px;">
						<form name="itemSearchForm" id="itemSearchForm" metdod="post" class="city-form">
						 	<input class="easyui-validatebox" name="showGirdNameForItem" id="showGirdNameForItem" disable="true" type ="hidden" />
						 	<table>
								<tr>
							    	<td class="common-td">储&nbsp;&nbsp;&nbsp;&nbsp;位：</td>
							        <td ><input class="easyui-validatebox ipt" style="width:120px" name="cellNo" id="cellNoForItem"/></td>
							        <td class="common-td blank">商品编码：</td>
							        <td ><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoForItem"/></td>
							        <td class="common-td blank">商品名称：</td>
							        <td ><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemNameForItem"/></td>
							        <td class="common-td blank">商品条码：</td>
							        <td><input class="easyui-validatebox ipt" style="width:120px" name="barcode" id="barcodeForItem"/></td>
							    </tr> 
							    <tr>
							    	<td class="common-td">品&nbsp;牌&nbsp;库：</td>
							        <td ><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo"/></td>
							        <td class="common-td blank">所属品牌：</td>
                     			    <td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoItem" /></td>
							        <td class="common-td blank">商品属性：</td>
							        <td ><input class="easyui-combobox" style="width:120px" name="itemType" id="itemTypeForItem"/></td>
							        <td class="common-td blank">品&nbsp;&nbsp;&nbsp;&nbsp;质：</td>
							        <td ><input class="easyui-combobox" style="width:120px" name="quality" id="qualityForItem"/></td>
							    </tr>
							    <#-- 
							    <tr>
							    	<td class="common-td">查询类型：</td>
							        <td ><input class="easyui-combobox ipt" style="width:120px" name="selectType" id="selectTypeItem" data-options="editable:false"/></td>
							    	<td class="common-td blank">板号：</td>
							        <td ><input class="easyui-validatebox ipt" style="width:120px" name="panNo" id="panNoForItem"/></td>
							        <td class="common-td blank">箱号：</td>
                     			    <td colspan='3'><input class="easyui-validatebox ipt" style="width:120px" name="boxNo" id="boxNoForItem" /></td>
							    </tr>   
							    -->          
							 </table>
						</form>
					</div>
				</div>
				<#--查询end-->
				<#--显示列表start-->
				<div data-options="region:'center',border:false">
					<@p.datagrid id="dataGridJGItemSelect" name=""   loadUrl="" saveUrl="" defaultColumn="" title="库存明细"
						isHasToolBar="false" divToolbar=""  onClickRowEdit="false" singleSelect="true" pageSize='20'  
						pagination="true" rownumbers="true" emptyMsg="" 
						columnsJsonList="[
						 	{field : 'ck',title : '',width : 50, checkbox:true},
			      			{field : 'cellNo',title : '储位编码',width : 90,align:'left'},
			           		{field : 'itemNo',title : '商品编码 ',width : 140,align:'left'},
			                {field : 'itemName',title : '商品名称',width : 130,align:'left'},
			                {field : 'barcode',title : '商品条码',width : 150,align:'left'},
			                {field : 'sizeNo',title : '尺码',width : 90,align:'left'},
			                {field : 'itemType',title : '商品属性   ',width : 100,align:'left',formatter:wms_city_common.columnItemTypeFormatter},
			                {field : 'quality',title : '商品品质',width : 80,align:'left',formatter:wms_city_common.columnQualityFormatter},
			                {field : 'conQty',title : '数量',width : 90,align:'right'},
			                {field : 'colorNo',title : '颜色编码',hidden : true,align:'left'},
			                {field : 'colorName',title : '颜色',width : 90,align:'left'},
			                {field : 'sysNo',title : '品牌库编码',hidden : true,align:'left'},
			                {field : 'sysNoStr',title : '品牌库',width : 90,align:'left'},
			                {field : 'brandNo',title : '品牌编码',hidden : true,align:'left'},
			                {field : 'brandName',title : '品牌',width : 90,align:'left'}
					 	]"
					/>
				</div>
				<#--显示列表end-->
			</div>			
		</div>
		
		
		<#-- 整箱选择div -->
		<div id="openUIBox"  class="easyui-dialog" title="箱容器选择"  
		    	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    	maximized:true,minimizable:false,maximizable:false"> 
			<div class="easyui-layout" data-options="fit:true">
				<#--查询start-->
				<div data-options="region:'north',border:false" >
			    	<@p.toolbar id="boxtoolbar"   listData=[
			    		{"title":"查询","iconCls":"icon-search","action":"billsmwaste.searchBox();", "type":0},
	       				{"title":"清除","iconCls":"icon-remove","action":"billsmwaste.clearSearch('boxSearchForm');", "type":0},
	        			{"title":"确定","iconCls":"icon-ok","action":"billsmwaste.confirmBox();", "type":0},
			    		{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billsmwaste.closeWindow('openUIBox');","type":0}
			        ]/>
					<div nowrap class="search-div" style="padding:10px;">
						<form name="boxSearchForm" id="boxSearchForm" metdod="post" class="city-form">
						 	<table>
							    <tr>
							    	<td class="common-td">容器号：</td>
							        <td ><input class="easyui-validatebox ipt" style="width:120px" name="boxNo" id="boxNoForItem" /></td>
							        <td class="common-td blank">父容器号：</td>
                     			    <td colspan='3'><input class="easyui-validatebox ipt" style="width:120px" name="panNo" id="panNoForItem"/></td>
							    </tr>             
							 </table>
						</form>
					</div>
				</div>
				<#--查询end-->
				<#--显示列表start-->
				<div data-options="region:'center',border:false">
					<@p.datagrid id="dataGridJGBoxSelect" name=""   loadUrl="" saveUrl="" defaultColumn="" title="箱容器列表"
						isHasToolBar="false" divToolbar=""  onClickRowEdit="false" singleSelect="true" pageSize='20'  
						pagination="true" rownumbers="true" emptyMsg="" 
						columnsJsonList="[
						 	{field : 'ck',title : '',width : 50, checkbox:true},
			      			{field : 'boxNo',title : '容器号',width : 130,align:'left'},
			      			{field : 'panNo',title : '父容器号',width : 130,align:'left'}
					 	]"
					/>
				</div>
				<#--显示列表end-->
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
								<td colspan="2" style="color:red;">只允许上 传后缀为 .xlsx 、 .xls 的文件，商品编码、尺码需加品牌代码。</td>
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