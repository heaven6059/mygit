<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退仓上架任务</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billuminstocktask/billuminstocktask.js?version=1.0.5.0"></script>
</head>

<body class="easyui-layout">	
	
	<#-- 工具菜单div -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
				{"title":"查询","iconCls":"icon-search","action":"billuminstocktask.searchArea()", "type":0},
		 		{"title":"清除","iconCls":"icon-remove","action":"billuminstocktask.searchLocClear()", "type":0},
			 	{"title":"上架定位","iconCls":"icon-lock","action":"billuminstocktask.searchArea();","type":0},
			 	{"title":"取消定位","iconCls":"icon-unlock","action":"billuminstocktask.cancelDirect();","type":0},
			 	{"title":"关闭","iconCls":"icon-close","action":"closeWindow('退仓上架任务')","type":0}
		]/>
	</div>
	
	<div data-options="region:'center',border:false">
		<#-- 查询条件 start -->
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<div data-options="region:'north',border:false" >
				<form id="searchForm" method="post" class="city-form" style="padding:10px;">
					<table>
						<tr>
							<td class="common-td">货主：</td>
							<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="ownerNo" id="search_ownerNo"/></td>
							<td class="common-td blank">库区：</td>
							<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="areaNo" id="search_areaNo"/></td>
							<td class="common-td blank">品牌：</td>
							<td><input class="easyui-combotree ipt" data-options="editable:false" style="width:120px" name="brandNo" id="search_brandNo"/></td>
							<td class="common-td blank">品质：</td>
							<td><input class="easyui-combobox ipt" data-options="editable:false" style="width:120px" name="quality" id="search_quality"/></td>
						</tr>
						<tr>
							<td class="common-td">退仓通知单号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="importBatchNo"/></td>
						</tr>
					</table>
				</form>
			</div>
			
			<div data-options="region:'center',border:false" >
				<@p.datagrid id="dataGridJG"  loadUrl="/bill_um_instock_task/instocktasklist.json?locno=${session_user.locNo}"  
					saveUrl=""  defaultColumn=""  title="退仓通知单列表"
				 	isHasToolBar="false" divToolbar=""  onClickRowEdit="false"  pagination="true" singleSelect = "false"
					rownumbers="true" 
		            columnsJsonList="[
		           		  {field : 'id',checkbox:true},
		           		  {field : 'importBatchNo',title : '退仓通知单号',width : 180,align:'left'},
		           		  {field : 'cellNo',title : '储位编码',width : 100,align:'left'},
		                  {field : 'itemNo',title : '商品编码',width : 110,align:'left'},
		                  {field : 'itemName',title : '商品名称',width : 140,align:'left'},
		                  {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
		                  {field : 'qty',title : '数量',width : 80,align:'right',formatter:billuminstocktask.qtyFormat},
		                  {field : 'instockQty',title : '预上数量',align:'right',width : 80,
	                  			editor:{
						    		type:'numberbox'
							    }
						  },
						  {field : 'quality',title : '品质',align:'left',width :80,formatter:billuminstocktask.qualityFormat},
		                  {field : 'colorName',title : '颜色',width : 80,align:'left'},
		                  {field : 'brandName',title : '品牌',width : 80,align:'left'},
		                  {field : 'unitName',title : '单位',width :60,align:'left'}
		                 ]" 
			           jsonExtend='{onSelect:function(rowIndex, rowData){
	                      billuminstocktask.directDetail(rowData);
	                   }}'/>
	                   
			</div>
		</div>
	</div>
	
	<div data-options="region:'south',border:false" style="height:220px;">
		<#-- 主表div2 -->
     	<div id="sendOrderDiv" style="padding:8px;text-align:left;">
			用户：<input class="easyui-combobox ipt" data-options="editable:false" style="width:110px" id="instockWorker" />
			<a id="searchBtn" href="javascript:billuminstocktask.createInstock();" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">发单</a>
	 	</div>
		<@p.datagrid id="dataGridJGDirect"  loadUrl="" saveUrl=""   defaultColumn=""
	    		isHasToolBar="false" pagination="true" title="退仓上架任务明细"
		        rownumbers="true" divToolbar="#sendOrderDiv" onClickRowEdit="false" singleSelect="false"
		        columnsJsonList="[
		           		  {field : 'id',checkbox:true},
		                  {field : 'cellNo',title : '来源储位',width : 80,align:'left'},
		                  {field : 'destCellNo',title : '预上储位',width : 80,align:'left'},
		                  {field : 'itemNo',title : '商品编码',width : 100,align:'left'},
		                  {field : 'itemName',title : '商品名称',width : 150,align:'left'},
		                  {field : 'instockQty',title : '预上数量',width :80,align:'right'},
		                  {field : 'styleNo',title : '款号',width :80,align:'left'},
		                  {field : 'colorName',title : '颜色',width :80,align:'left'},
		                  {field : 'sizeNo',title : '尺码',width : 100,align:'left'},
		                  {field : 'unitName',title : '单位',align:'left'}
		                 ]" 
			           jsonExtend='{onSelect:function(rowIndex, rowData){
	                            // 触发点击方法  调JS方法
	                     // defdock.selectedCheckBox(rowIndex);
	                   },onDblClickRow:function(rowIndex, rowData){
	                   	//双击方法
	                   	  //defdock.edit(rowData)
	   	}}'/>
	</div>

</body>
</html>