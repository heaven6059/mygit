<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>上架策略</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/csinstocksetting.js?version=1.0.5.1"></script>
    <script type="text/javascript" src="${domainStatic}/resources/common/other-lib/common.js?version=1.0.5.0"></script>
    <link rel="stylesheet" type="text/css" href="${domainStatic}/resources/css/styles/csinstocksetting.css?version=1.0.5.0" />
</head>
<style>
div.city-form table tr{height:30px;}
</style>
<body class="easyui-layout">
	<!-- 工具栏  -->
	<div data-options="region:'north',border:false">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"csinstocksetting.searchArea()", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"csinstocksetting.searchClear('searchForm')", "type":0},
	        {"id":"btn-close","title":"新增","iconCls":"icon-add","action":"csinstocksetting.addInfo()","type":1},
	        {"id":"btn-close","title":"修改","iconCls":"icon-edit","action":"csinstocksetting.editInfo()","type":2},
	        {"id":"btn-close","title":"删除","iconCls":"icon-del","action":"csinstocksetting.del()","type":3},
	        {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('上架策略')","type":0}
		]/>
		<div class="search-div">
			<form name="searchForm" id="searchForm" method="post" class="city-form">
				<table>
					<tr>
						<td class="common-td">策略编码：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="settingNo"/></td>
						<td class="common-td blank">策略名称：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="settingName"/></td>
						<td class="common-td blank">状态：</td>
						<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="status" id="search_status" /></td>
				</table>
			</form>
		</div>
	</div>
	<!-- 主档信息  -->
	<div data-options="region:'center',border:false">
		<@p.datagrid id="dataGridJG"  loadUrl="/cs_instock_setting/list.json?locno=${session_user.locNo}" saveUrl=""   defaultColumn=""   title="上架策略"
              isHasToolBar="false" divToolbar="#storeSearchDiv" onClickRowEdit="false" pageSize="20" emptyMsg=""
	           rownumbers="true"  singleSelect = "false" height="480"
	           columnsJsonList="[
	           		  {field : ' ',checkbox:true},
	           		  {field : 'settingNo',title : '策略编码',width : 120,align:'left'},
	                  {field : 'settingName',title : '策略名称',width : 120,align:'left'},
	                  {field : 'settingDesc',title : '描述',width : 150,align:'left'},
	                  {field : 'status',title : '状态',width : 100,formatter:csinstocksetting.statusFormatter,align:'left'},
	                  {field : 'setType',title : '适用场景',width : 100,formatter:csinstocksetting.setTypeFormatter,align:'left'},
	                  {field : 'memo',title : '备注',width : 150,align:'left'}
	                 ]" 
		           jsonExtend='{onSelect:function(rowIndex, rowData){
                            // 触发点击方法  调JS方法
                   },onDblClickRow:function(rowIndex, rowData){
                   	//双击方法
                   	  csinstocksetting.edit(rowData)
                   }}'/>
	</div>
	
	<!-- 明细窗口S -->
	<div id="showDialog" class="easyui-dialog" title="新增"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    <form method="post" id="dataForm" name="dataForm">
		    	<div data-options="region:'north',border:false" style="padding: 8px;">
		    		<div class="search-div" style="border:none;">
		    			<div class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td">货主：</td>
		                 			<td><input class="easyui-combobox" data-options="editable:false" style="width:138px" name="ownerNo" id="ownerNo"/></td>
		                 			<td class="common-td blank">策略编码：</td>
		                 			<td>
		                 				<input class="easyui-validatebox ipt" style="width:138px" name="settingNo" id="settingNo" required="true"  data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']" />
										<input style="width:100px" name="settingNo" id="settingNoHide" class="hide" type="hidden"/>
		                 			</td>
		                 			<td class="common-td blank">策略名称：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:138px" name="settingName" id="settingName" required="true" data-options="validType:['vLength[0,40,\'最多只能输入40个字符\']']"/></td>
		                 			<td class="common-td blank">状态：</td>
		                 			<td><input class="easyui-combobox" data-options="editable:false" style="width:138px" name="status" id="status"/></td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-td">策略描述：</td>
		                 			<td colspan="3"><input class="easyui-validatebox ipt" style="width:347px" name="settingDesc" id="settingDesc" data-options="validType:['vLength[0,100,\'最多只能输入100个字符\']']"/></td>
		                 			<td class="common-td blank">备注：</td>
		                 			<td colspan="3"><input class="easyui-validatebox ipt" style="width:323px" name="memo" id="memo" data-options="validType:['vLength[0,200,\'最多只能输入200个字符\']']"/></td>
		                 		</tr>
		                    </table>
						</div>
		    		</div>
		    	</div>
		    	<div data-options="region:'west',border:false" style="padding: 8px;width:440px;">
		    		<div class="easyui-layout" data-options="fit:true">
						<div data-options="region:'north',border:true" style="height:62px">
							<div class="search-div" style="padding:0;border:none">
								<div class="city-form">
				         			<table>
				                 		<tr>
				                 			<td class="common-td" style="text-align:left;" colspan="5">上架范围<input type="hidden" name="cellNo" id="cellNoTrue"></td>
				                 		</tr>
				                 		<tr>
				                 			<td class="common-line"><input type="radio" name="instockScope" class="instock_scope" value="1" checked="checked">指定库区&nbsp;&nbsp;&nbsp;&nbsp;</td>
				                 			<td class="common-line"><input type="radio" name="instockScope" class="instock_scope" value="2">指定通道&nbsp;&nbsp;&nbsp;&nbsp;</td>
				                 			<td class="common-line"><input type="radio" name="instockScope" class="instock_scope" value="3">指定储位&nbsp;&nbsp;&nbsp;&nbsp;</td>
				                 			<td class="common-line"><a id="addLineForCell" href="javascript:csinstocksetting.openCellWindow()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				                 			<td class="common-line"><a id="delLineForCell" href="javascript:csinstocksetting.delCell()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				                 		</tr>
				                    </table>
								</div>
				    		</div>
				    	</div>
						<div data-options="region:'center'" >
								<@p.datagrid id="cellDetail"  loadUrl=""  saveUrl=""  defaultColumn="" 
					               isHasToolBar="false" divToolbar=""  onClickRowEdit="false" singleSelect="false" pageSize='20'  
								   pagination="true" rownumbers="true" emptyMsg=""
						 			columnsJsonList="[
						 				{field:'ck',title:'',checkbox:true},
						 				{field:'CODE',title:'编码',width:120,align:'left'},
						 				{field:'NAME',title:'名称',width:140,align:'left'}
						 			]"
						 		/>
					 	</div>
					</div>
		    	</div>
		    	<div data-options="region:'center',border:false" style="padding: 8px;">
					<div class="easyui-layout" data-options="fit:true">
						<div data-options="region:'north'" style="height:62px">
							<div class="search-div" style="padding:0;border:none">
								<div class="city-form">
									<table>
										<tr>
											<td class="common-td" style="text-align:left;" colspan="5">生效对象<input type="hidden" name="selectValue" id="selectValue"></td>
										</tr>
										<tr>
											<td class="common-line"><input type="radio"  name="detailType" value="0">品牌&nbsp;&nbsp;&nbsp;&nbsp;</td>
											<td class="common-line"><input type="radio" name="detailType" value="1">类别(二级)&nbsp;&nbsp;&nbsp;&nbsp;</td>
											<td class="common-line"><input type="radio" name="detailType" value="2">商品&nbsp;&nbsp;&nbsp;&nbsp;</td>
											<td class="common-line"><a id="addLineForItem" href="javascript:csinstocksetting.openItemWindow()" class="easyui-linkbutton" data-options="iconCls:'icon-add'" plain="true">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
											<td class="common-line"><a id="delLineForItem" href="javascript:csinstocksetting.delItem()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" plain="true">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
						<div data-options="region:'center'">
							<@p.datagrid id="itemDetail"  loadUrl=""  saveUrl=""  defaultColumn="" 
									   isHasToolBar="false" divToolbar=""  onClickRowEdit="false" singleSelect="false" pageSize='20'  
									   pagination="true" rownumbers="true" emptyMsg=""
										columnsJsonList="[
											{field:'ck',title:'',checkbox:true},
											{field:'CODE',title:'编码',width:115,align:'left'},
											{field:'NAME',title:'名称',width:130,align:'left'}
										]"
							/>
						</div>
					</div>
		    	</div>
		    	<div data-options="region:'south',border:false" style="padding: 8px;">
		    		<div class="search-div" style="padding:0;border: 1px solid #95B8E7;float: left;">
		    			<div class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td" style="text-align:left;" colspan="2">储位分配顺序</td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-line"><input type="radio" name="cellSort" value="0" checked="checked">底层->高层&nbsp;&nbsp;&nbsp;&nbsp;</td>
		                 			<td class="common-line"><input type="radio" name="cellSort" value="1">高层->底层&nbsp;&nbsp;&nbsp;&nbsp;</td>
		                 		</tr>
		                    </table>
						</div>
		    		</div>
		    		<div class="search-div" style="padding:0;border: 1px solid #95B8E7;float: left;margin-left: 8px;">
		    			<div class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td" style="text-align:left;" colspan="2">储位分配限定</td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-line"><input type="radio" name="limitedType" value="0" checked="checked">数量限定&nbsp;&nbsp;&nbsp;&nbsp;</td>
		                 			<td class="common-line"><input type="radio" name="limitedType" value="1">体积限定&nbsp;&nbsp;&nbsp;&nbsp;</td>
		                 		</tr>
		                    </table>
						</div>
		    		</div>
		    		<div class="search-div" style="padding:0;border: 1px solid #95B8E7;float: left;margin-left: 8px;margin-right: 8px;">
		    			<div class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td" style="text-align:left;" colspan="2">储位分配规则(优先级从左至右)</td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-line"><input type="checkbox" name="sameItemFlag" value="1">同款商品临近&nbsp;&nbsp;&nbsp;&nbsp;</td>
		                 			<td class="common-line"><input type="checkbox" name="emptyCellFlag" value="1">空储位优先分配&nbsp;&nbsp;&nbsp;&nbsp;</td>
		                 		</tr>
		                    </table>
						</div>
		    		</div>
		    		<div class="search-div" style="padding:0;border: 1px solid #95B8E7;">
		    			<div class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td" style="text-align:left;" colspan="2">适用场景</td>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-line"><input type="radio" name="setType" value="0" checked="checked">进货上架&nbsp;&nbsp;&nbsp;&nbsp;</td>
		                 			<td class="common-line"><input type="radio" name="setType" value="1">退仓上架&nbsp;&nbsp;&nbsp;&nbsp;</td>
		                 		</tr>
		                    </table>
						</div>
		    		</div>
		    		<div style="padding:0;width:100%;margin-top: 8px;">
		    			<div class="city-form">
		         			<table style="width:100%;">
		                 		<tr>
		                 			<td style="text-align:center;">
		                 				<a id="info_add" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
										<a id="info_edit" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a> 
		                 			</td>
		                 		</tr>
		                    </table>
						</div>
		    		</div>
		    	</div>
		    </form>
		    </div>
	</div>
	
	<#--库区选择div -->
	<div id="openUIArea"  class="easyui-dialog" style="width:690px;height:400px;"  title="库区选择"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,minimizable:false,maximizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<@p.toolbar id="areatoolbar"  listData=[
					{"title":"查询","iconCls":"icon-search","action":"csinstocksetting.search('Area')", "type":0},
			        {"title":"清除","iconCls":"icon-remove","action":"csinstocksetting.searchClear('searchAreaForm')", "type":0},
			        {"title":"确认","iconCls":"icon-ok","action":"csinstocksetting.confirm('cell','Area')","type":0},
			        {"title":"取消","iconCls":"icon-cancel","action":"csinstocksetting.closeWindow('openUIArea')","type":0}
				]/>
	    		<div class="search-div">
	    			<form name="searchAreaForm" id="searchAreaForm" method="post" class="city-form">
	         			<table>
	                 		<tr>
	                 			<td class="common-td">仓区：</td>
	                 			<td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNoByAreaCon" data-options="
																		onChange:function(){
																			csinstocksetting.selectAreaNoForSearchByArea();
																		}
																		"/></td>
	                 			<td class="common-td blank">库区：</td>
	                 			<td><input class="easyui-combobox" style="width:120px" name="areaNo" id="areaNoCon" /></td>
	                 		</tr>
	                    </table>
					</form>
	    		</div>
	    	</div>
	    	<div data-options="region:'center',border:false">
	    		<#-- 库区选择数据列表div -->
		        <@p.datagrid id="dataGridAreaOpen"  loadUrl=""  saveUrl=""  defaultColumn="" 
				  	isHasToolBar="false" divToolbar=""  onClickRowEdit="false" 
				  	singleSelect="false" pageSize='20'  
					pagination="true" rownumbers="true"
					columnsJsonList="[
					           		{field : 'id',checkbox:true},
									{field : 'wareNo',title : '仓区编码',width : 120,align:'left'},
									{field : 'areaNo',title : '库区编码',width : 120,align:'left'},
									{field : 'areaName',title : '库区名称',width : 140,align:'left'}
					]"
					jsonExtend='{}'
				 />
	    	</div>
	    </div>
	</div>
	<#--通道选择div -->
	<div id="openUIStock"  class="easyui-dialog" style="width:690px;height:400px;"  title="通道选择"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,minimizable:false,maximizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<@p.toolbar id="stocktoolbar"  listData=[
					{"title":"查询","iconCls":"icon-search","action":"csinstocksetting.search('Stock')", "type":0},
			        {"title":"清除","iconCls":"icon-remove","action":"csinstocksetting.searchClear('searchStockForm')", "type":0},
			        {"title":"确认","iconCls":"icon-ok","action":"csinstocksetting.confirm('cell','Stock')","type":0},
			        {"title":"取消","iconCls":"icon-cancel","action":"csinstocksetting.closeWindow('openUIStock')","type":0}
				]/>
	    		<div class="search-div">
	    			<form name="searchStockForm" id="searchStockForm" method="post" class="city-form">
	         			<table>
	                 		<tr>
	                 			<td class="common-td">仓区：</td>
	                 			<td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNoByStockCon" data-options="
																		onChange:function(){
																			csinstocksetting.selectAreaNoForSearchByStock();
																		}
																		"/></td>
	                 			<td class="common-td blank">库区：</td>
	                 			<td><input class="easyui-combobox" style="width:110px" name="areaNo" id="areaNoByStockCon" /></td>
	                 			<td class="common-td blank">通道：</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:110px" name="stockNo" id="stockNoCon" /></td>
	                 		</tr>
	                    </table>
					</form>
	    		</div>
	    	</div>
	    	<div data-options="region:'center',border:false">
	    		<#-- 通道选择数据列表div -->
		       <@p.datagrid id="dataGridStockOpen"  loadUrl=""  saveUrl=""  defaultColumn="" 
				  	isHasToolBar="false" divToolbar="#searchStockDiv"  onClickRowEdit="false" 
				  	singleSelect="false" pageSize='20'  
					pagination="true" rownumbers="true"
					columnsJsonList="[
					           		{field : 'id',checkbox:true},
					           		{field : 'wareNo',title : '仓区编码',width : 120,align:'left'},
									{field : 'areaNo',title : '库区编码',width : 120,align:'left'},
									{field : 'stockNo',title : '通道编码',width : 120,align:'left'}
					]"
					jsonExtend='{}'
				 />
	    	</div>
	    </div>
	</div>
	<#--储位选择div -->
	<div id="openUICell"  class="easyui-dialog" style="width:690px;height:400px;"  title="储位选择"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,minimizable:false,maximizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<@p.toolbar id="celltoolbar"  listData=[
					{"title":"查询","iconCls":"icon-search","action":"csinstocksetting.search('Cell')", "type":0},
			        {"title":"清除","iconCls":"icon-remove","action":"csinstocksetting.searchClear('searchCellForm')", "type":0},
			        {"title":"确认","iconCls":"icon-ok","action":"csinstocksetting.confirm('cell','Cell')","type":0},
			        {"title":"取消","iconCls":"icon-cancel","action":"csinstocksetting.closeWindow('openUICell')","type":0}
				]/>
	    		<div class="search-div">
	    			<form name="searchCellForm" id="searchCellForm" method="post" class="city-form">
	         			<table>
	                 		<tr>
	                 			<td class="common-td">仓区：</td>
	                 			<td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNoByCellCon" data-options="
																		onChange:function(){
																			csinstocksetting.selectAreaNoForSearchByCell();
																		}
																		"/></td>
	                 			<td class="common-td blank">库区：</td>
	                 			<td><input class="easyui-combobox" style="width:110px" name="areaNo" id="areaNoByCellCon" data-options="
																		onChange:function(){
																			csinstocksetting.selectStockNoForSearchByCell();
																		}
																		"/></td>
	                 			<td class="common-td blank">通道：</td>
	                 			<td><input class="easyui-combobox" style="width:110px" name="stockNo" id="stockNoByCellCon" /></td>
	                 			<td class="common-td blank">储位：</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:110px" name="cellNo" id="cellNoCon" /></td>
	                 		</tr>
	                    </table>
					</form>
	    		</div>
	    	</div>
	    	<div data-options="region:'center',border:false">
	    		<#-- 储位选择数据列表div -->
		        <@p.datagrid id="dataGridCellOpen"  loadUrl=""  saveUrl=""  defaultColumn="" 
				  	isHasToolBar="false" divToolbar="#searchCellDiv" height="285"  onClickRowEdit="false" 
				  	singleSelect="false" pageSize='20'  
					pagination="true" rownumbers="true"
					columnsJsonList="[
					           		{field : 'id',checkbox:true},
					           		{field : 'wareNo',title : '仓区编码',width : 120,align:'left'},
									{field : 'areaNo',title : '库区编码',width : 120,align:'left'},
									{field : 'stockNo',title : '通道编码',width : 120,align:'left'},
									{field : 'cellNo',title : '储位编码',width : 120,align:'left'}
					]"
					jsonExtend='{}'
				 />
	    	</div>
	    </div>
	</div>

	<#--品牌选择div -->
	<div id="openUIBrand"  class="easyui-dialog" style="width:690px;height:400px;"  title="品牌选择"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,minimizable:false,maximizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<@p.toolbar id="brandtoolbar"  listData=[
					{"title":"查询","iconCls":"icon-search","action":"csinstocksetting.search('Brand')", "type":0},
			        {"title":"清除","iconCls":"icon-remove","action":"csinstocksetting.searchClear('searchBrandForm')", "type":0},
			        {"title":"确认","iconCls":"icon-ok","action":"csinstocksetting.confirm('item','Brand')","type":0},
			        {"title":"取消","iconCls":"icon-cancel","action":"csinstocksetting.closeWindow('openUIBrand')","type":0}
				]/>
	    		<div class="search-div">
	    			<form name="searchBrandForm" id="searchBrandForm" method="post" class="city-form">
	         			<table>
	                 		<tr>
	                 			<td class="common-td">品牌编码：</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:110px" name="brandNo" id="brandNoCon" /></td>
	                 			<td class="common-td blank">品牌名称：</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:110px" name="brandName" id="brandNameCon" /></td>
	                 		</tr>
	                    </table>
					</form>
	    		</div>
	    	</div>
	    	<div data-options="region:'center',border:false">
	    		<#-- 品牌选择数据列表div -->
		        <@p.datagrid id="dataGridBrandOpen"  loadUrl=""  saveUrl=""  defaultColumn="" 
				  	isHasToolBar="false" divToolbar="#searchBrandDiv" height="285"  onClickRowEdit="false" 
				  	singleSelect="false" pageSize='20'  
					pagination="true" rownumbers="true"
					columnsJsonList="[
					           		{field : 'id',checkbox:true},
									{field : 'brandNo',title : '品牌编码',width : 120,align:'left'},
									{field : 'brandName',title : '品牌名称',width : 140,align:'left'}
					]"
					jsonExtend='{}'
				 />
	    	</div>
	    </div>
	</div>
	<#-- 类别选择div -->
	<div id="openUICategory"  class="easyui-dialog" style="width:690px;height:400px;"  title="类别选择"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,minimizable:false,maximizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<@p.toolbar id="categorytoolbar"  listData=[
					{"title":"查询","iconCls":"icon-search","action":"csinstocksetting.search('Category')", "type":0},
			        {"title":"清除","iconCls":"icon-remove","action":"csinstocksetting.searchClear('searchCategoryForm')", "type":0},
			        {"title":"确认","iconCls":"icon-ok","action":"csinstocksetting.confirm('item','Category')","type":0},
			        {"title":"取消","iconCls":"icon-cancel","action":"csinstocksetting.closeWindow('openUICategory')","type":0}
				]/>
	    		<div class="search-div">
	    			<form name="searchCategoryForm" id="searchCategoryForm" method="post" class="city-form">
	         			<table>
	                 		<tr>
	                 			<td class="common-td">类别编码：</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:110px" name="cateNo" id="cateNoCon" /></td>
	                 			<td class="common-td blank">类别名称：</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:110px" name="cateName" id="cateNameCon" /></td>
	                 		</tr>
	                    </table>
					</form>
	    		</div>
	    	</div>
	    	<div data-options="region:'center',border:false">
	    		<#-- 类别选择数据列表div -->
		        <@p.datagrid id="dataGridCategoryOpen"  loadUrl=""  saveUrl=""  defaultColumn="" 
				  	isHasToolBar="false" divToolbar="#searchCategoryDiv" height="285"  onClickRowEdit="false" 
				  	singleSelect="false" pageSize='20'  
					pagination="true" rownumbers="true"
					columnsJsonList="[
					           		{field : 'id',checkbox:true},
									{field : 'cateNo',title : '类别编码',width : 120,align:'left'},
									{field : 'cateName',title : '类别名称',width : 140,align:'left'}
					]"
					jsonExtend='{}'
				 />
	    	</div>
	    </div>
	</div>
	<#-- 商品选择div -->
	<div id="openUIItem"  class="easyui-dialog" style="width:690px;height:400px;"  title="商品选择"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,minimizable:false,maximizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<@p.toolbar id="itemtoolbar"  listData=[
					{"title":"查询","iconCls":"icon-search","action":"csinstocksetting.search('Item')", "type":0},
			        {"title":"清除","iconCls":"icon-remove","action":"csinstocksetting.searchClear('searchItemForm')", "type":0},
			        {"title":"确认","iconCls":"icon-ok","action":"csinstocksetting.confirm('item','Item')","type":0},
			        {"title":"取消","iconCls":"icon-cancel","action":"csinstocksetting.closeWindow('openUIItem')","type":0}
				]/>
	    		<div class="search-div">
	    			<form name="searchItemForm" id="searchItemForm" method="post" class="city-form">
	         			<table>
	                 		<tr>
	                 			<td class="common-td">商品编码：</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:110px" name="itemNo" id="itemNoCon" /></td>
	                 			<td class="common-td blank">商品名称：</td>
	                 			<td><input class="easyui-validatebox ipt" style="width:110px" name="itemName" id="itemNameCon" /></td>
	                 		</tr>
	                    </table>
					</form>
	    		</div>
	    	</div>
	    	<div data-options="region:'center',border:false">
	    		<#-- 商品选择数据列表div -->
		        <@p.datagrid id="dataGridItemOpen"  loadUrl=""  saveUrl=""  defaultColumn="" 
				  	isHasToolBar="false" divToolbar="#searchItemDiv" height="285"  onClickRowEdit="false" 
				  	singleSelect="false" pageSize='20'  
					pagination="true" rownumbers="true"
					columnsJsonList="[
					           		{field : 'id',checkbox:true},
									{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
									{field : 'itemName',title : '商品名称',width : 180,align:'left'}
					]"
					jsonExtend='{}'
				 />
	    	</div>
	    </div>
	</div>
</body>
</html>