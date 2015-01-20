
<div class="easyui-layout" data-options="fit:true">
	
	<div data-options="region:'north',border:false" class="toolbar-region">
		<#-- 工具栏  -->
		<@p.toolbar id="tab2_toolbar" listData=[
			{"title":"查询","iconCls":"icon-search","action":"billWmRecedeDispatch.searchDataTab2()", "type":0},
			{"title":"清除","iconCls":"icon-remove","action":"billWmRecedeDispatch.searchClear('searchFormTab2')", "type":0},
			{"title":"发单","iconCls":"icon-redo","action":"billWmRecedeDispatch.sendWmOutstockDirect()","type":0} ,
			{"title":"关闭","iconCls":"icon-close","action":"closeWindow('退厂调度')","type":0}
		 ]/>
	</div>
	
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true">
			
			<div data-options="region:'north',border:false" >
				<#-- 主档信息  -->		
				<form id="searchFormTab2" class="city-form" style="padding:10px;">
					<table>
						<tr>
							<td class="common-td">货主：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="ownerNo" id="ownerNoTab2"/></td>
							<td class="common-td blank">供应商：</td>
							<td><input class="easyui-combobox ipt" style="width:200px" name="supplierNo" id="supplierNoTab2" /></td>
							
						</tr>
						<tr>
							<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
	             			<td>
	             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch2"/>
	             			</td>
	             			<td class="common-td blank">所属品牌：</td>
							<td><input class="easyui-combobox ipt" style="width:200px" name="brandNo" id="brandNoTab2" /></td>
						</tr>
					</table>
				</form>
			</div>
			
			<div data-options="region:'center',border:false" >
				<@p.datagrid id="dataGridJG_tab2"  loadUrl="/wmrecededispatch/listBillWmRecedeByPage.json?locno=${session_user.locNo}"
	        		saveUrl=""  defaultColumn=""  title="退厂通知单列表" isHasToolBar="false" divToolbar="#searchDivTab2" 
	        		onClickRowEdit="false"  pagination="true" singleSelect="true" rownumbers="true"
				    columnsJsonList="[
				    		{field : 'locno',hidden:true},
				    		{field : 'ownerNo',hidden:true},
				        	{field : 'recedeNo',title : '退厂通知单号 ',width : 180},
				        	{field : 'recedeType',title : '退厂类型 ',width : 130,align:'left',formatter: billWmRecedeDispatch.recedeTypeFormatter},
				        	{field : 'poNo',title : '退厂订单号 ',width : 180,align:'left'},
				        	{field : 'recedeDate',title : '退厂日期 ',width : 130}
				    ]" 
				jsonExtend='{onSelect:function(rowIndex, rowData){
	                	billWmRecedeDispatch.loadWmOutstockDirectDetail(rowData);
	       		}}'/>
			</div>
			
			<div data-options="region:'south',border:false,height:200,minSplit:true" >
				<div id="sendDivTab2" style="padding-left:5px;" >
		    		拣货人员：<input class="easyui-combobox" style="width:120px" name="locateNo" id="locateNoTab2" />
		    	</div>
	        	<@p.datagrid id="dataGridDtl_tab2"  loadUrl=""
	        		saveUrl=""  defaultColumn=""  title="退厂通知单明细" isHasToolBar="false" divToolbar="#sendDivTab2" 
	        	    onClickRowEdit="false"  pagination="true" singleSelect="false" rownumbers="true" showFooter="true"
				    columnsJsonList="[
				    		{field : 'id',checkbox:true},
				    		{field : 'locno',hidden:true},
				    		{field : 'operateDate',hidden:true},
				    		{field : 'directSerial',hidden:true},
				    		{field : 'ownerNo',hidden:true},
				    		{field : 'sourceNo',hidden:true},
				    		{field : 'operateType',hidden:true},
				    		{field : 'itemId',hidden:true},
				    		{field : 'packQty',hidden:true},
				    		{field : 'sCellId',hidden:true},
				    		{field : 'sContainerNo',hidden:true},
				    		{field : 'dCellNo',hidden:true},
				    		{field : 'dCellId',hidden:true},
				    		{field : 'dContainerNo',hidden:true},
				    		{field : 'supplierNo',hidden:true},
				    		{field : 'classType',hidden:true},
				    		{field : 'poId',hidden:true},
				    		{field : 'stockType',hidden:true},
				    		{field : 'stockValue',hidden:true},
				    		{field : 'boxNo',hidden:true},
				    		
				    		{field:'sCellNo',title:'来源储位',width:130,align:'left'},
				    		{field:'itemNo',title:'商品编码',width:140,align:'left'},
					 		{field:'itemName',title:'商品名称',width:160,align:'left'},
					 		{field:'styleNo',title:'款号',width:110,align:'left',hidden:true},
					 		{field:'colorName',title:'颜色',width:80,align:'left'},
					 		{field:'sizeNo',title:'尺码',width:80,align:'left'},
							{field:'outstockItemQty',title:'预计退货数量',width:100,align:'right'}
				    ]" 
				jsonExtend='{}'/>
			</div>
		</div>
	</div>

</div>
