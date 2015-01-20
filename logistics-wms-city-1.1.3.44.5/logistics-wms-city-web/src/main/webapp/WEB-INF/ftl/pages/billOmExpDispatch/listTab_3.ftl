<div class="easyui-layout" data-options="fit:true">
	<!-- 工具栏  -->
	<div data-options="region:'north',border:false">
		<@p.toolbar id="toolbar3" listData=[
			{"title":"查询","iconCls":"icon-search","action":"billOmExpDispatch.searchDataTab3()", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"billOmExpDispatch.searchClear('searchFormTab3')", "type":0},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('出库调度')","type":0}
		 ]/>
		 <div class="search-div">
			<form name="searchFormTab3" id="searchFormTab3" method="post" class="city-form">
				<table>
					<tr>
						<td class="common-td">波次号：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="locateNo" id="locateNoTab3" /></td>
						<td class="common-td blank">发货通知单：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNoTab3"/></td>
						<td class="common-td blank">商品编码：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoTab3"/></td>
					</tr>
					<tr>
						<td class="common-td"> 品&nbsp;牌&nbsp;库：</td>
             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch3"/></td>
             			<td class="common-td blank">所属品牌：</td>
						<td colspan='3'><input class="easyui-combobox ipt" style="width:312px" name="brandNo" id="brandNoTab3" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!-- 主档信息  -->
	<div data-options="region:'center',border:false">
    	<@p.datagrid id="dataGridJG_tab3"  loadUrl=""  saveUrl=""  defaultColumn=""  title="异常调度列表"
		    	isHasToolBar="false" divToolbar="" height="410"  onClickRowEdit="false"  pagination="true" singleSelect = "true"
			    rownumbers="true"
			    columnsJsonList="[
			        	{field : 'locateNo',title : '波次号 ',width : 130},
			        	{field : 'expNo',title : '发货通知单 ',width : 130},
			        	{field : 'itemNo',title : '商品编码 ',width : 150,align:'left'},
			        	{field : 'itemName',title : '商品名称 ',width : 180,align:'left'},
			        	{field : 'colorName',title : '颜色 ',width : 90,align:'left'},
			        	{field : 'sizeNo',title : '尺码 ',width : 80,align:'left'},
			        	{field : 'errorReason',title : '异常记录 ',width : 150,align:'left'}
			        	
			    ]" 
				jsonExtend='{}'/>
	</div>
</div>