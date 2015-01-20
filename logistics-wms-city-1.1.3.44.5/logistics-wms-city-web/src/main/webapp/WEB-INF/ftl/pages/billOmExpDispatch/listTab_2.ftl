<div class="easyui-layout" data-options="fit:true">
	<!-- 工具栏  -->
	<div data-options="region:'north',border:false">
		<@p.toolbar id="toolbar2" listData=[
			{"title":"查询","iconCls":"icon-search","action":"billOmExpDispatch.searchDataTab2()", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"billOmExpDispatch.searchClear('searchFormTab2')", "type":0},
			{"id":"btn-pre","title":"续调","iconCls":"icon-redo","action":"billOmExpDispatch.continueOmExpDispatch()","type":2} ,
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('出库调度')","type":0}
		 ]/>
		 <div class="search-div">
			<form name="searchFormTab2" id="searchFormTab2" method="post" class="city-form">
				<table>
					<tr>
						<td class="common-td">波次号：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="locateNo" id="locateNoTab2" /></td>
						<td class="common-td blank">货主：</td>
						<td><input class="easyui-combobox" style="width:120px" name="ownerNo" id="ownerNoTab2" data-options="valueField:'ownerNo',textField:'ownerName',panelHeight:'auto'"/></td>
						<td class="common-td blank">发货通知单：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNoTab2" data-options="valueField:'ownerNo',textField:'ownerName',panelHeight:'auto'"/></td>
						<td class="common-td blank">合同号：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo" id="poNoTab2" data-options="valueField:'ownerNo',textField:'ownerName',panelHeight:'auto'"/></td>
					</tr>
					<tr>
						<td class="common-td"> 品&nbsp;牌&nbsp;库：</td>
             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch2"/></td>
						<td class="common-td blank">所属品牌：</td>
						<td colspan='3'><input class="easyui-combobox ipt" style="width:322px" name="brandNo" id="brandNoTab2" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!-- 主档信息  -->
	<div data-options="region:'center',border:false">
    	<@p.datagrid id="dataGridJG_tab2"  loadUrl=""
    		saveUrl=""  defaultColumn=""  title="波次列表" isHasToolBar="false" divToolbar="#searchDivTab2" 
    		height="385"  onClickRowEdit="false"  pagination="true" singleSelect = "false" rownumbers="true"
		    columnsJsonList="[
		    		{field : 'id',checkbox:true},
		        	{field : 'locateNo',title : '波次号 ',width : 150,align:'left'},
		        	{field : 'expType',title : '出货单类型 ',width : 130,formatter:billOmExpDispatch.expTypeFormatter,align:'left'}
		    ]" 
			jsonExtend='{}'/>
	</div>
</div>
