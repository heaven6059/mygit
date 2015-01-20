<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退仓上架任务</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billuminstockdirect/billuminstockdirect.js?version=1.0.5.6"></script>
</head>
<body class="easyui-layout" data-options="border:false">
    <div id="ttAll" class="easyui-tabs" data-options="region:'center',border:false"> 
		<div title="退仓上架任务"> 
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false" >
					 <@p.toolbar  id='main-toolbar' listData=[
			             {"title":"查询","iconCls":"icon-search","action":"billuminstockdirect.searchArea()","type":0},
			             {"title":"清空","iconCls":"icon-remove","action":"billuminstockdirect.searchLocClear()","type":0},
						 {"title":"生成任务","iconCls":"icon-ok","action":"billuminstockdirect.createTask();","type":1},
				         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('上架任务')","type":0}
					 ]/>
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
							<tr>
								<td class="common-td blank">单据编号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo" id="checkNoCondition" /></td>
								<td class="common-td blank">创建人：</td>
								<td><input class="combobox ipt" style="width:120px" name="creator" id="creatorCondition" /></td>
								<td class="common-td blank">创建日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="createtmStart"/></td>
								<td class="common-line">&mdash;</td>
								<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="createtmEnd"></td>
							</tr>
							<tr>
								<td class="common-td blank" >商品类型：</td>
			   					<td><input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemType_search"  style="width:120px"/></td>
								<td class="common-td blank">品质：</td>
								<td><input class="easyui-combobox" name="quality" id="quality_search" style="width:120px;"/></td>
								<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
		             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
								<td class="common-td blank">所属品牌：</td>
								<td colspan="5"><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
								
							</tr>
						</table>
					</form>
				</div>
				<div data-options="region:'center',border:false" >
	     			<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn=""   title="验收单列表"
		              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"    pagination="true"
			           rownumbers="true"  singleSelect = "true" emptyMsg="" 
			           columnsJsonList="[
			           		  {field : ' ',checkbox:true},
			           		  {field : 'status',title : '状态',width : 85,align:'left',formatter:billuminstockdirect.statusFormatter},
			           		  {field : 'checkNo',title : '验收单号',width :150},
			           		  {field : 'untreadNo',title : '店退仓单号',width :150},
			           		  {field : 'ownerNo',title : '货主',width : 60,align:'left',formatter:billuminstockdirect.ownerFormatter},
			           		  {field : 'storeName',title : '店家名称',width : 150,align:'left'},
			           		  {field : 'itemType',title : '商品类型',width : 60,formatter:billuminstockdirect.typeFormatter,align:'left'},
							  {field : 'quality',title : '品质',width : 60,formatter:billuminstockdirect.qualityFormatter,align:'left'},
							  {field : 'checkQty',title : '验收数量',width : 80,align:'right'},
							  {field : 'creator',title : '创建人',width : 80,align:'left'},
			                  {field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			                  {field : 'createtm',title : '创建时间',width : 150},
			           		  {field : 'untreadMmNo',title : '退仓通知单号',width :150}
								]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                         //触发点击方法  调JS方法
		                         billuminstockdirect.instockDetail();
		                         //billuminstockdirect.instockDirectDetail();
		                         //billuminstockdirect.initInstockWorker(rowData);
		                   },onDblClickRow:function(rowIndex, rowData){
		                   	     //双击方法
		                   }}'/>
				</div>
				<div data-options="region:'south',border:false,minSplit:true" style="height:200px;">
							<@p.datagrid id="dataGridJG_detail"  loadUrl="" saveUrl=""   defaultColumn=""
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true"
					           rownumbers="true"  singleSelect = "false" title="验收单明细" showFooter="true"
					           columnsJsonList="[
					           		  {field : 'itemNo',title : '商品编号',width :150,align:'left'},
					           		  {field : 'itemName',title :'商品名称',width : 200,align:'left'},
					           		  {field : 'sizeNo',title :'尺码',width : 200,align:'left'},
					           		  {field : 'boxNo',title :'箱号',width : 200,align:'left'},
					           		  {field : 'checkQty',title : '验收数量',width : 100,align:'right'},
					           		  {field : 'recheckQty',title : '复核数量',width : 100,align:'right'},
					                  {field : 'itemQty',title : '计划数量',width : 100,align:'right'}
					                 ]"/>
			</div>
		</div> 
		
	</div>
	
	<div title="上架发单" closable="false" data-options="border:false"> 
			<div class="easyui-layout" data-options="fit:true,border:false">
				<div data-options="region:'center',border:false">
					<div id="ttCheck" class="easyui-tabs" data-options="fit:true,border:false"> 
						<div title="未发单" closable="false"> 
							<div class="easyui-layout" data-options="fit:true,border:false">
								<div data-options="region:'north',border:false" >
								
								    <@p.toolbar  id='mains-toolbar' listData=[
							             {"title":"查询","iconCls":"icon-search","action":"billuminstockdirect.searchNoSendOrder(10)","type":0},
								         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('上架任务')","type":0}
									 ]/>
									 
									<form name="searchFormNo" id="searchFormNo" method="post" class="city-form">
										<table>
											<tr>
												<td class="common-td blank">商品属性：</td>
											    <td><input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemTypeCondition" required="true" style="width:120px"/></td>
											    <td class="common-td blank">品质：</td>
												<td><input class="easyui-combobox" required="true" name="quality" id="qualityCondition" style="width:120px;" required="true" /></td>
												<td class="common-td blank">来源单号：</td>
												<td><input class="easyui-validatebox ipt" id="noSendSourceNo" name="sourceNo" style="width:150px;"  /></td>
											</tr>
										</table>
									</form> 
								
										<div style="padding:8px;text-align:right;padding-right:20px">
											用户：<input class="easyui-validatebox ipt" style="width:110px" id="instockWorker" name="instockWorker"/>
											<a id="searchBtn2" href="javascript:billuminstockdirect.selectPickingPeople();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">选择</a>
											<a id="searchBtn" href="javascript:billuminstockdirect.createInstock();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">发单</a>
											<a id="aKeySendBtn" href="javascript:billuminstockdirect.aKeySendOrder();" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">一键发单</a>
										</div>
								</div>
								<div data-options="region:'center',border:false" >
									<@p.datagrid id="dataGridJGCheck_direct"  loadUrl="" saveUrl=""   defaultColumn=""
					                isHasToolBar="false" onClickRowEdit="false"    pagination="true"
						           rownumbers="true"  singleSelect = "false" showFooter="true"
						           columnsJsonList="[
						           		  {field : ' ',checkbox:true},
						           		  {field : 'status',title : '状态',width : 100,align:'left',formatter:billuminstockdirect.statusTaskFormatter},
						           		  {field : 'rowId',title : '',width :80,align:'left',hidden:true},
						           		  {field : 'ownerNo',title : '',width :80,align:'left',hidden:true},
						           		  {field : 'itemType',title :'商品属性',width : 150,align:'left',hidden:true},
						           		  {field : 'quality',title :'商品品质',width : 150,align:'left',hidden:true},
						           		  {field : 'sourceNo',title :'来源单号',width : 180,align:'left'},
						           		  {field : 'itemNo',title :'商品编码',width : 150,align:'left'},
						                  {field : 'itemName',title :'商品名称',width : 150,align:'left'},
						                  {field : 'sizeNo',title :'尺码',width : 150,align:'left'},
						                  {field : 'boxNo',title :'箱号',width : 150,align:'left'},

						           		  {field : 'cellNo',title : '来源储位',width :80,align:'left'},
						           		  {field : 'destCellNo',title : '预上储位',width :80,align:'left'},
						                  {field : 'estQty',title : '预上数量',width : 100,align:'right'},
						                  {field : 'creator',title : '创建人',width : 80,align:'left'},
			                			  {field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
			                              {field : 'createtm',title : '创建时间',width : 140}
						                 ]" 
							           />
								</div>
							</div>
						</div> 
						
						<div title="已发单"> 
							<@p.datagrid id="dataGridJGCheck_detail"  loadUrl="" saveUrl=""   defaultColumn=""
			              isHasToolBar="false" onClickRowEdit="false"    pagination="true"
				           rownumbers="true"  singleSelect = "false" height="200" showFooter="true"
				           columnsJsonList="[
				           				  {field : 'status',title : '状态',width : 100,align:'left',formatter:billuminstockdirect.statusTaskFormatter},
						           		  {field : 'sourceNo',title :'来源单号',width : 180,align:'left'},
						           		  {field : 'itemNo',title :'商品编码',width : 150,align:'left'},
						                  {field : 'itemName',title :'商品名称',width : 150,align:'left'},
						                  {field : 'sizeNo',title :'尺码',width : 150,align:'left'},
						                  {field : 'boxNo',title :'箱号',width : 150,align:'left'},
						           		  {field : 'cellNo',title : '来源储位',width :80,align:'left'},
						           		  {field : 'destCellNo',title : '预上储位',width :80,align:'left'},
						                  {field : 'estQty',title : '预上数量',width : 100,align:'right'}
						                 
				                 ]"/>
						</div> 
						
	        		 </div>
				</div>
			</div>
	 </div>
	 
   </div>	
	
	<div id="umNoDialog" class="easyui-window" title="选择退仓通知单号"
	   		style="width:435px;"   
		    data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		     minimizable:false,maximizable:false" class="easyui-layout">
		<div data-options="region:'north',border:false" style="height:200px;">
			<@p.datagrid id="dataPlanNo"  loadUrl="" saveUrl=""   defaultColumn="" 
			              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"  height="200"  
			               pagination="true" rownumbers="true"  singleSelect = "true" width="410"
				           columnsJsonList="[
								{field : 'untreadMmNo',title : '退仓通知单号',width :300}
				           ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
			                  // 触发点击方法  调JS方法
			               },onDblClickRow:function(rowIndex, rowData){
			                   	   billuminstockdirect.checkUmNo(rowData);
			               }}'/>
	   </div>
	</div>
	
	<div id="umCheckDiffDialog" class="easyui-window" title="待预约明细"
	   		style="width:600px;"   
		    data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		     minimizable:false,maximizable:false" class="easyui-layout">
		     
		<div data-options="region:'north',border:false" style="height:350px;">
		    <div style="padding:8px;text-align:right;padding-right:20px">
				  <a id="searchBtn" href="javascript:billuminstockdirect.continueDirect();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">继续定位</a>
			</div>
			<input type='hidden' name="checkNoStr" id="checkNoStr" />
			<input type='hidden' name="untreadMmNoStr" id="untreadMmNoStr" />
			<input type='hidden' name="ownerNoStr" id="ownerNoStr" />
			<@p.datagrid id="dataUmCheckDiff"  loadUrl="" saveUrl=""   defaultColumn="" 
			              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"  
			               pagination="true" rownumbers="true"  singleSelect = "true" showFooter="true"
				           columnsJsonList="[
								      {field : 'itemNo',title :'商品编码',width : 160,align:'left'},
						              {field : 'sizeNo',title :'尺码',width : 80,align:'left'},
						              {field : 'difQty',title : '待预约数量',width : 100,align:'right'}
				           ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
			                  // 触发点击方法  调JS方法
			               },onDblClickRow:function(rowIndex, rowData){
			                   	  billuminstockdirect.checkUmNo(rowData);
			               }}'/>
	   </div>
	</div>
	
	<#-- 选择用户窗口 -->
	<div id="showSelectPickingPeopleWin"  class="easyui-window" title="选择用户" 
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		    <div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<div id="itemSelectDiv">
						<#-- 工具菜单div -->
				       <@p.toolbar id="toolbarpeople" listData=[
				       		{"title":"确定","iconCls":"icon-ok","action":"billuminstockdirect.confirmPickingPeople()", "type":0},
				            {"title":"取消","iconCls":"icon-cancel","action":"billuminstockdirect.closeShowWin('showSelectPickingPeopleWin')","type":0}
					    ]/>
					</div>
				
					<div id="itemSearchDiv" style="padding:6px;">
						<form name="dataForm" id="dataForm" method="post" >
							<table>
								<tr>
									<td class="common-td">岗位：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="roleid" id="roleid"  /></td>
								</tr>
							</table>
						</form>	
					</div>
				</div>	
				
				<div data-options="region:'center',split:false">
					<@p.datagrid id="pickingPeopleDataGrid" name="" title="" loadUrl="" saveUrl="" defaultColumn="" 
						 	isHasToolBar="false"  divToolbar=""  height="400"  onClickRowEdit="false" singleSelect="false" pageSize='20'  
							pagination="false" rownumbers="true"
						 	columnsJsonList="[
						 				{field : 'ck',title : '',width : 50, checkbox:true},
						 			    {field:'loginName',title:'人员编码',width:100,align:'left'},
					                    {field:'username',title:'人员名称',width:150,align:'left'},
					                    {field:'roleName',title:'岗位',width:150,align:'left'}
					]"/>
				</div>
				
			</div>
	 </div>	
	
</body>
</html>