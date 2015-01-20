<div class="easyui-layout" data-options="fit:true">
		<!-- 工具栏  -->
	<div data-options="region:'north',border:false">
		<@p.toolbar id="toolbar" listData=[
			{"title":"查询","iconCls":"icon-search","action":"billOmExpDispatch.searchDataTab1()", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"billOmExpDispatch.searchClear('searchFormTab1')", "type":0},
			{"id":"btn-pre","title":"调度","iconCls":"icon-redo","action":"billOmExpDispatch.omExpDispatch()","type":1} ,
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('出库调度')","type":0}
		 ]/>
		 <div class="search-div">
			<form name="searchFormTab1" id="searchFormTab1" method="post" class="city-form">
				<table>
					<tr>
						<td class="common-td">发货通知单号：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNoTab1" /></td>
						<td class="common-td blank">订单类型：</td>
						<td><input class="easyui-combobox" style="width:120px" name="expType" id="expTypeTab1"/></td>
						<td class="common-td blank">合同号：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="poNo" id="poNoTab1"/></td>
						<td class="common-td blank" style="display:none;">客户编号：</td>
						<td style="display:none;"><input class="easyui-validatebox ipt" style="width:120px" name="storeNo" id="storeNoTab1" /></td>
					</tr>
					<tr>
						<td class="common-td"> 品&nbsp;牌&nbsp;库：</td>
             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch1"/></td>
             			<td class="common-td blank">所属品牌：</td>
						<td colspan='3'><input class="easyui-combobox ipt" style="width:300px" name="brandNo" id="brandNoTab1" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!-- 主档信息  -->
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
			</div>
			<!--显示列表-->
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG_tabl" loadUrl="" saveUrl=""  defaultColumn=""  title="调度列表"
					isHasToolBar="false" divToolbar="" height="360"  onClickRowEdit="false"  pagination="true" singleSelect="false"
					rownumbers="true" emptyMsg="" 
					columnsJsonList="[
							{field : 'id',checkbox:true},
							{field : 'locno',hidden:true},
							{field : 'totalNoenoughQty',hidden:true},
							{field : 'totalDifferenceQty',hidden:true},
							{field : 'totalVolumeQty',hidden:true},
							{field : 'totalWeightQty',hidden:true},
							{field : 'ownerNo',title : '货主',width : 90,formatter:billOmExpDispatch.ownerFormatter,align:'left'},
							{field : 'expNo',title : '发货通知单号',width : 180},
							{field : 'sourceexpNo',title : '来源单号',width : 180},
							{field : 'expType',title : '订单类型',width : 80,formatter:billOmExpDispatch.expTypeFormatter,align:'left'},
							{field : 'totalItemQty',title : '品项数',width : 80,align:'right'},
							{field : 'totalExpQty',title : '数量',width : 80,align:'right'},
							{field : 'expRemark',title : '备注',width : 120,align:'left'}
					]" 
					jsonExtend='{onDblClickRow:function(rowIndex, rowData){
						billOmExpDispatch.loadDetailDtl(rowData);
					  }
					}'/>
			</div>
			<div data-options="region:'south',border:false,minSplit:true"style="height:220px;">
				<div class="easyui-layout" data-options="fit:true">
					<div  data-options="region:'north',border:false">
						
					</div>
					<!--底部显示栏S-->
					<div data-options="region:'center'">
						<div id="dispatchDtlTools_div">
						<form name="dispatchDtlTools" id="dispatchDtlTools" method="post" class="city-form">
							<table>
								<tr>
									<td class="common-td">是否允许分拨：</td>
									<td><input id="divideFlag" name="divideFlag" type="checkbox" value="0"/></td>
								</tr>
							</table>
						</form>
						</div>
						<@p.datagrid id="dataGridDtl_tabl"  loadUrl=""  saveUrl=""  defaultColumn=""  title="调度明细"
							isHasToolBar="false" onClickRowEdit="false"  
							pagination="true" singleSelect="false" divToolbar="#dispatchDtlTools_div"
							rownumbers="true" emptyMsg="" showFooter="true"
							columnsJsonList="[
									{field:'itemNo',title:'商品编码',width:135,align:'left'},
									{field:'itemName',title:'商品名称',width:150,align:'left'},
									{field:'styleNo',hidden:true},
									{field:'colorName',title:'颜色',width:70,align:'left'},
									{field:'sizeNo',title:'尺码',width:60},
									{field:'itemQty',title:'计划数量',width:70,align:'right'},
									{field:'differenceQty',title:'可调度数量',width:80,align:'right'},
									{field:'usableQty',title:'可用数量',width:80,align:'right'},
									{field:'noenoughQty',title:'缺量',width:60,align:'right'},
									{field:'volume',title:'体积',width:60,align:'right'},
									{field:'weight',title:'重量',width:60,align:'right'}
							]" 
							jsonExtend='{}'/>
						
					</div>
					<!--底部显示栏E-->
				</div>
			</div>
		</div>	
	</div>
</div>
	
			