<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>跨部门转货</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billconconvert/billconconvert.js?version=1.1.1.4"></script>
      <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
        <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
    </object>
</head>
<body class="easyui-layout">

			
	<#-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
			<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"billconconvert.searchData()", "type":0},
	            {"title":"清除","iconCls":"icon-remove","action":"billconconvert.searchClear('searchForm')", "type":0},
				{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"billconconvert.openPostDialog('add')","type":1},
				{"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"billconconvert.openPostDialog('edit')","type":2},
				{"title":"删除","iconCls":"icon-del","action":"billconconvert.removeMain()","type":3},
				{"id":"btn-check","title":"审核","iconCls":"icon-aduit","action":"billconconvert.check()","type":4},
				{"title":"打印预览","id":"printBtn","iconCls":"icon-print","action":"billconconvert.printDetail4SizeHorizontal()", "type":2},
				{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('跨部门转货')","type":0}
			 ]/>
	</div>
		
	<#--divToolbar有值时，isHasToolBar不能为 true loadUrl有值时，请求页面后，就会从远程站点url请求数据，填充到数据表格 -->
	<#-- onDblClickRow-表格双击事件，加载数据 -->
			
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<!--搜索start-->
			<div  data-options="region:'north',border:false" >
				 <div class="search-div">
				 	<form name="searchForm" id="searchForm" method="post" class="city-form">
				 		<table>
                     		<tr>
                     			<td class="common-td blank">状态：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="status" id="status" /></td>
                     			<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="creator" id="creator" /></td>
                     			<td class="common-td blank">创建日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="createtmStart" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="createtmEnd" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank">单据编号：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="convertNo" id="convertNo" /></td>	
                     			<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
                     			<td><input class="easyui-validatebox ipt" style="width:120px" name="auditor" id="auditor" /></td>
                     			<td class="common-td blank">审核日期：</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="audittmStart" /></td>
                     			<td class="common-line">&nbsp;&mdash;&nbsp;</td>
                     			<td><input class="easyui-datebox" style="width:120px" name="audittmEnd" id="audittmEnd" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank">来源单号：</td>
		             			<td><input class="easyui-validatebox ipt" style="width:120px" name="sourceNo" id="sourceNo"/></td>
                     			<td class="common-td blank">来源类型：</td>
                     			<td><input class="easyui-combobox ipt" style="width:120px" name="sourceType" id="sourceType" data-options="editable:false"/></td>
                     			<td class="common-td blank">转货类型：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="convertType" id="convertType" data-options="editable:false"/></td>
                     			<td class="common-td blank">转入仓库：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="storeNo" id="storeNo" data-options="editable:false"/></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNo"/></td>
                     			<td class="common-td blank">所属品牌：</td>
                     			<td colspan='5'><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
                     		</tr>
                     	</table>
				 	</form>	
				 </div>
			</div>
			<!--显示列表-->
            <div data-options="region:'center',border:false">
            	<@p.datagrid id="mainDataGrid"  saveUrl=""  loadUrl="" defaultColumn=""  title="转货单列表"
					isHasToolBar="false"  divToolbar="#locSearchDiv" height="450"  onClickRowEdit="false" singleSelect="false" pageSize="20"
					rownumbers="true" emptyMsg=""  showFooter="true"
					columnsJsonList="[
						{field : 'id',checkbox :true},	
						{field : 'status',title : '状态',width : 80,align:'left',formatter:billconconvert.statusFormatter},	
						{field : 'convertNo',title : '单据编号',width : 180},
						{field : 'convertType',title : '转货类型',width : 100,align:'left',formatter:billconconvert.convertTypeFormatter},
						{field : 'storeNo',title : '转入仓库',width : 100,align:'left'},
						{field : 'ownerNo',hidden :true},	
						{field : 'convertDate',title : '转货日期',width : 100},
						{field : 'sourceNo',title : '来源单号',width : 180},
						{field : 'sourceType',title : '来源类型',width : 100,align:'left',formatter:billconconvert.sourceTypeFormatter},
						{field : 'itemQty',title : '计划数量',width : 80,align:'right'},
						{field : 'creator',title : '创建人',width : 80,align:'left'},
						{field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130,sortable:true},
						{field : 'auditor',title : '审核人',width : 80,align:'left'},
						{field : 'auditorName',title : '审核人名称',width : 80,align:'left'},
						{field : 'audittm',title : '审核时间',width : 130},
						{field : 'remark',title : '备注',width : 120,align:'left'}
						]" 
						jsonExtend='{onDblClickRow:function(rowIndex, rowData){
						    billconconvert.openInfo(rowData);
						},
						queryParams:{locno:"${session_user.locNo}"}}'/>
            </div>
		</div>
	</div>
	
	<!-- 转货明细窗口S -->
	<div id="showDetailDialog" class="easyui-dialog" title="转货明细"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
		    		<@p.toolbar id="infotoolbar"  listData=[
						{"title":"关闭","iconCls":"icon-cancel","action":"billconconvert.closeWindow('showDetailDialog');","type":0}
				   	]/>	
		    		<div class="search-div">
		         		<form name="MainFormInfo" id="MainFormInfo" method="post" class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td">单据编号：</td>
		                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="convertNo" id="convertNoInfo" readOnly="true" /></td>
		                 			<td class="common-td blank">货主：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNoInfo" />
		                 			<td class="common-td blank">转入仓库：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="storeNo" id="storeNoInfo" />
		                 			<td class="common-td blank">转货类型：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="convertType" id="convertTypeInfo" />
		                 		</tr>
		                 		<tr>
		                 			<td class="common-td blank">转货日期：</td>
		                 			<td><input class="easyui-datebox" style="width:120px" name="convertDate" id="convertDateInfo"/></td>
		                 			<td class="common-td blank">备注：</td>
		                 			<td colspan='5'><input class="easyui-validatebox ipt" style="width:500px" name="remark" id="remarkInfo"/></td>
		                 		</tr>
		                    </table>
		         		</form>
			         </div>
		    	</div>
		    	<!--显示列表-->
            	<div data-options="region:'center',border:false">
            		<@p.datagrid id="dtl_info_dataGrid"    defaultColumn=""  title="转货信息"
						isHasToolBar="false" divToolbar="" height="350"    pageSize="20" 
						onClickRowEdit="false" onClickRowAuto="false" pagination="true" showFooter="true"
						rownumbers="true" 
						columnsJsonList="[
						 	  {field:'cellNo',title:'储位',width:100,align:'left'},    
								{field:'itemNo',title:'商品编码',width:150,align:'left'},    
								{field:'itemName',title:'商品名称',width:150,align:'left'},    
								{field:'sizeNo',title:'尺码',width:100,align:'left'},    
								{field:'itemQty',title:'计划数量',width:100,align:'right'}
					         ]"
						rownumbers="true" jsonExtend="{}" />
            	</div>
		    </div>
	</div>
	<!-- 转货明细窗口E -->
	
	<!-- 新增修改窗口S -->
	<div id="postDetailDialog" class="easyui-dialog" title="转货/修改"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
		    		<@p.toolbar id="posttoolbar"  listData=[
		    			{"title":"保存","iconCls":"icon-save","action":"billconconvert.save();","type":0},
						{"title":"关闭","iconCls":"icon-cancel","action":"billconconvert.closeWindow('postDetailDialog');","type":0}
				   	]/>	
		    		<div class="search-div">
		         		<form name="MainFormPost" id="MainFormPost" method="post" class="city-form">
		         			<table>
		                 		<tr>
		                 			<td class="common-td">单据编号：<input type='hidden' id='option'/></td>
		                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="convertNo" id="convertNoPost" readOnly="true" /></td>
		                 			<td class="common-td blank">货主：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNoPost" data-options="editable:false,required:true"/>
		                 			<td class="common-td blank">转入仓库：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="storeNo" id="storeNoPost" data-options="editable:false,required:true"/>
		                 			<td class="common-td blank">转货类型：</td>
		                 			<td><input class="easyui-combobox" style="width:120px" name="convertType" id="convertTypePost" data-options="editable:false,required:true"/>
		                 		</tr>
		                 		<tr>
		                 			<td class="common-td blank">转货日期：</td>
		                 			<td><input class="easyui-datebox" style="width:120px" name="convertDate" id="convertDatePost"/ data-options="editable:false,required:true"></td>
		                 			<td class="common-td blank">备注：</td>
		                 			<td colspan='5'><input class="easyui-validatebox ipt" style="width:500px" name="remark" id="remarkPost"/></td>
		                 		</tr>
		                    </table>
		         		</form>
			         </div>
		    	</div>
		    	<!--显示列表-->
            	<div data-options="region:'center',border:false" id="dtl_post_div">
            		<@p.toolbar id="toolbarDlt_item" listData=[
						{"title":"新增","iconCls":"icon-add-dtl","action":"billconconvert.openAddDtl()", "type":0}
						{"title":"删除","iconCls":"icon-del-dtl","action":"billconconvert.removeDtl('dtl_post_dataGrid')", "type":0}
						{"title":"保存明细","iconCls":"icon-save-dtl","action":"billconconvert.saveDtl()", "type":0}
               		]/>
            		<@p.datagrid id="dtl_post_dataGrid"    defaultColumn=""  title="转货信息"
						isHasToolBar="false" divToolbar="#toolbarDlt_item" height="350"    pageSize="20" 
						onClickRowEdit="false" onClickRowAuto="false" pagination="true" showFooter="true"
						rownumbers="true" emptyMsg=""
						columnsJsonList="[   
								{field:' ',checkbox:true},
								{field:'cellNo',title:'储位',width:100,align:'left'},    
								{field:'itemNo',title:'商品编码',width:150,align:'left'},    
								{field:'itemName',title:'商品名称',width:150,align:'left'},    
								{field:'sizeNo',title:'尺码',width:100,align:'left'},    
								{field:'itemQty',title:'计划数量',width:100,align:'right'}
					         ]"
						rownumbers="true" jsonExtend="{}" />
            	</div>
		    </div>
	</div>
	<!-- 新增修改窗口E -->
	
	<!-- 库存选择S -->
	<div id="con_select_dialog"   class="easyui-dialog" title="库存选择"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
                     <div data-options="region:'north',border:false">
                     		<@p.toolbar id="celltoolbar" listData=[
			                             {"title":"查询","iconCls":"icon-search","action":"billconconvert.searchCon()", "type":0},
			                             {"title":"清除","iconCls":"icon-remove","action":"billconconvert.searchClear('selectConSearchForm')", "type":0},
			                             {"title":"确认","iconCls":"icon-ok","action":"billconconvert.selectConOk()","type":0}
			                             <#--{"title":"整库区","iconCls":"icon-ok","action":"billchplan.cellSelectOKByAreaNo()","type":0}
			                             {"title":"整通道","iconCls":"icon-ok","action":"billchplan.cellSelectOKByStockNo()","type":0}-->
			                             {"title":"取消","iconCls":"icon-cancel","action":"billconconvert.closeWindow('con_select_dialog')","type":0}
				                       ]
							  />
							<div class="search-div">
				         		<form name="selectConSearchForm" id="selectConSearchForm" method="post" class="city-form">
				         			<table>
				                 		<tr>
				                 			<#--<td class="common-td">仓区：</td>
				                 			<td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNo"/>
												</td>
				                 			<td class="common-td blank">库区：</td>
				                 			<td><input class="easyui-combobox" style="width:120px" name="areaNo" id="areaNo"/>
											</td>
				                 			<td class="common-td blank">通道：</td>
				                 			<td><input class="easyui-combobox" style="width:120px" name="stockNo" id="stockNo"/>
											</td>-->
				                 			<td class="common-td blank">储位：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="cellNo" id="cellNo" /></td>
				                 			<td class="common-td blank">商品编码：</td>
				                 			<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNo" /></td>
				                 		</tr>
				                    </table>
				         		</form>
				         	</div>
                     </div>	
                     <div data-options="region:'center',border:false">
	                     <@p.datagrid id="con_select_datagrid"  loadUrl=""  saveUrl=""  defaultColumn="" height="300"
			               isHasToolBar="false" divToolbar=""  onClickRowEdit="false"  pagination="true" singleSelect = "false"
				           rownumbers="true"
				           columnsJsonList="[
				           		{field:'id',checkbox:true},
				           		{field : 'cellNo',title : '储位 ',width : 100,align:'left'},
				                {field : 'itemNo',title : '商品编码',width : 140,align:'left'},
				                {field : 'itemName',title : '商品名称',width : 140,align:'left'},
				                {field : 'sizeNo',title : '尺码',width : 60,align:'left'},
				                {field : 'itemQty',title : '计划数量 ',width : 80,align:'right'}
				            ]"/>
			         </div>
		    </div>
	</div>
	<!-- 库存选择E -->
</body>
</html>