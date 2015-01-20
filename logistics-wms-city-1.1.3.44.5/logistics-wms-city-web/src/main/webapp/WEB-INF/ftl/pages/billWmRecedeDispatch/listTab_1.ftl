
<#-- 工具菜单div -->
<div class="easyui-layout" data-options="fit:true" >

	<div data-options="region:'north',border:false" class="toolbar-region">
		<#-- 工具栏  -->
		<@p.toolbar id="tab1_toolbar" listData=[
			{"title":"查询","iconCls":"icon-search","action":"billWmRecedeDispatch.searchDataTab1()", "type":0},
			{"title":"清除","iconCls":"icon-remove","action":"billWmRecedeDispatch.searchClear('searchFormTab1')", "type":0},
			{"id":"btn-pre","title":"库存定位","iconCls":"icon-flag-red","action":"billWmRecedeDispatch.wmRecedeLocate()","type":0} ,
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('退厂调度')","type":0}
		 ]/>
	</div>
	
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" >
		
			<div data-options="region:'north',border:false" >
				<#-- 主档信息  -->		
				<form id="searchFormTab1" class="city-form" style="padding:10px;">
					<table>
						<tr>
							<td class="common-td">退厂通知单号：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="recedeNo" id="recedeNoTab1" /></td>
							<td class="common-td blank" style="display:none;">退厂订单号：</td>
							<td style="display:none;"><input class="easyui-validatebox ipt" style="width:120px" name="poNo" id="poNoTab1"/></td>
							<td class="common-td blank">货主：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNoTab1"/></td>
							<td class="common-td blank">退货单类型：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="recedeType" id="recedeTypeTab1" /></td>
						</tr>
						<tr>
							<td class="common-td"> 品&nbsp;牌&nbsp;库：</td>
	             			<td >
	             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch1"/>
	             			</td>
	             			<td class="common-td blank">所属品牌：</td>
							<td colspan='5'><input class="easyui-combobox ipt" style="width:322px" name="brandNo" id="brandNoTab1" /></td>
						</tr>
					</table>
				</form>
			</div>
			
			<div data-options="region:'center',border:false" >
				<@p.datagrid id="dataGridJG_tabl" loadUrl="/wmrecede/listDispatch.json?locno=${session_user.locNo}&status=11" saveUrl=""  defaultColumn=""  title="调度列表"
			    	isHasToolBar="false" divToolbar="#searchDivTab1"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
				    rownumbers="true"
				    columnsJsonList="[
				    		{field : 'id',checkbox:true},
				        	{field : 'locno',hidden:true},
				        	{field : 'totalNoenoughQty',hidden:true},
				        	{field : 'totalDifferenceQty',hidden:true},
				        	{field : 'totalVolumeQty',hidden:true},
				        	{field : 'totalWeightQty',hidden:true},
				        	{field : 'ownerNo',title : '货主 ',width : 80,formatter: billWmRecedeDispatch.ownerFormatter,align:'left'},
				        	{field : 'recedeNo',title : '退厂通知单号 ',width : 180},
				        	{field : 'poNo',title : '退厂订单号',width : 180,align:'left'},
				        	{field : 'recedeType',title : '退厂单类型',formatter: billWmRecedeDispatch.recedeTypeFormatter,width : 80,align:'left'},
				        	{field : 'recedeDate',title : '退货日期',width : 100},
				        	{field : 'status',title : '单据状态',formatter: billWmRecedeDispatch.statusFormatter,width : 100,align:'left'},
				        	{field : 'memo',title : '备注',width : 120,align:'left'}
				    ]" 
					jsonExtend='{onSelect:function(rowIndex, rowData){
	                	billWmRecedeDispatch.loadDetailDtl(rowData);
	       			}}'/>
			</div>
			<div data-options="region:'south',border:false,height:190,minSplit:true" >
				<@p.datagrid id="dataGridDtl_tabl"  loadUrl=""  saveUrl=""  defaultColumn=""  title="调度明细"
			    	isHasToolBar="false"  onClickRowEdit="false"  pagination="true" singleSelect="false"
				    rownumbers="true" showFooter="true"
				    columnsJsonList="[
					 		{field:'itemNo',title:'商品编码',width:130,align:'left'},
					 		{field:'itemName',title:'商品名称',width:170,align:'left'},
					 		{field:'styleNo',title:'款号',width:110,align:'left',hidden:true},
					 		{field:'colorName',title:'颜色',width:80,align:'left'},
					 		{field:'sizeNo',title:'尺码',width:80,align:'left'},
							{field:'recedeQty',title:'预计退货数量',width:90,align:'right'},
							{field:'differenceQty',title:'可退货数量',width:80,align:'right'},
					 		{field:'noenoughQty',title:'缺量',width:80,align:'right'},
							{field:'volume',title:'体积',width:80,align:'right'},
							{field:'weight',title:'重量',width:80,align:'right'}
				    ]" 
				jsonExtend='{}'/>
			</div>
			
		</div>
	</div>
</div>