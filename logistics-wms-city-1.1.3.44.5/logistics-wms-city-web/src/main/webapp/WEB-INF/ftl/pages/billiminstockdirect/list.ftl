<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>上架任务</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billiminstockdirect/billiminstockdirect.js?version=1.0.5.2"></script>
</head>
<body class="easyui-layout">
    <div id="ttAll" class="easyui-tabs" data-options="region:'center',border:false"> 
		<div title="验收定位" closable="false"> 
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false" >
					  <@p.toolbar  id='check-toolbar' listData=[
			             {"title":"查询","iconCls":"icon-search","action":"billiminstockdirect.searchCheck()","type":0},
			             {"title":"清空","iconCls":"icon-remove","action":"billiminstockdirect.searchLocClearCheck('searchFormCheck')","type":0},
						 {"title":"上架定位","iconCls":"icon-ok","action":"billiminstockdirect.instockDirectCheck()","type":1},
						 {"title":"取消定位","iconCls":"icon-del","action":"billiminstockdirect.cancelDirectCheck()","type":3},
				         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('上架任务')","type":0}
					  ]/>
					 <form name="searchFormCheck" id="searchFormCheck" method="post" class="city-form">
					 	<table>
							<tr>
								<td class="common-td blank">验收单号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="checkNo"/></td>
								<td class="common-td blank">状态：</td>
								<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="status" id="search_check_status" /></td>
								<td class="common-td blank">验收日期：</td>
								<td><input class="easyui-datebox" style="width:120px" name="startCheckTm" id="startCheckTm"/></td>
								<td class="common-line">&mdash;</td>
								<td><input class="easyui-datebox" style="width:120px" name="endCheckTm"/ id="endCheckTm"></td>
							</tr>
							<tr>
								<td class="common-td blank">货主：</td>
								<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="ownerNo" id="search_check_workerNo" /></td>
								<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
	                 			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch_check"/></td>
	                 			<td class="common-td blank">所属品牌：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="search_check_brandNo" /></td>
	                 			
	                 			<td class="common-td blank" style='display:none;'> 业务类型：</td>
	                 			<td style='display:none;'>
	                 				<input class="easyui-combobox" style="width:120px" name="businessType" id="businessType"/>
	                 			</td>
							</tr>
						</table>
					</form>
				</div>
				<div data-options="region:'center',border:false" >
					<@p.datagrid id="dataGridJGCheck"  loadUrl="" saveUrl=""   defaultColumn=""   title=""
		              isHasToolBar="false" divToolbar="#locSearchDivCheck" onClickRowEdit="false"    pagination="true"
			           rownumbers="true"  singleSelect = "true" 
			           columnsJsonList="[
			           		  {field : ' ',checkbox:true},
			           		   {field : 'status',title : '状态',width : 100,align:'left',formatter:billiminstockdirect.statusFormatter},
			           		  {field : 'checkNo',title : '验收单号',width :180},
			           		  {field : 'ownerNo',title : '货主',width : 120,align:'left',formatter:billiminstockdirect.ownerFormatter},
			                  {field : 'checkEndDate',title : '验收日期',width : 100},
			                  {field : 'showBusinessType',title : '业务类型',width : 100},
			                 ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                        // 触发点击方法  调JS方法
		                        billiminstockdirect.instockCheckDetail();
		                        billiminstockdirect.instockCheckDirectDetail();
		                        //billiminstockdirect.initInstockWorker(rowData);
		                   },onDblClickRow:function(rowIndex, rowData){
		                   	    //双击方法
		                   }}'/>
				</div>
				<div data-options="region:'south',minSplit:true,border:false" style="height:200px">
					<div id="ttCheck" class="easyui-tabs" data-options="fit:true"> 
			         	<div title="验收明细"> 
							<@p.datagrid id="dataGridJGCheck_detail"  loadUrl="" saveUrl=""   defaultColumn=""
			              isHasToolBar="false" onClickRowEdit="false"    pagination="true"
				           rownumbers="true"  singleSelect = "false" height="200" showFooter="true"
				           columnsJsonList="[
				            	  {field : 'boxNo',title :'箱号',width : 100,align:'left'},
				           		  {field : 'itemNo',title : '商品编号',width :150,align:'left'},
				           		  {field : 'itemName',title :'商品名称',width : 200,align:'left'},
				                  {field : 'sizeNo',title :'尺码',width : 80,align:'left'},
				                  {field : 'checkQty',title : '验收数量',width : 100,align:'right'},
				                  {field : 'divideQty',title : '分货数量',width : 100,align:'right'}
				                 ]"/>
						</div> 
						<div title="定位信息" closable="false"> 
							<div class="easyui-layout" data-options="fit:true">
								<div data-options="region:'north',border:false" >
										<div style="padding:8px;text-align:right;padding-right:20px">
											<#-- 
											用户：<input class="easyui-validatebox" style="width:110px" id="instockWorkerCheck" name="instockWorker"/>
											<a id="searchBtn2" href="javascript:billiminstockdirect.selectPickingPeople();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">选择</a>
											<a id="searchBtn" href="javascript:billiminstockdirect.createInstockCheck();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">发单</a>
											-->
										</div>
								</div>
								<div data-options="region:'center',border:false" >
									<@p.datagrid id="dataGridJGCheck_direct"  loadUrl="" saveUrl=""   defaultColumn=""
					                isHasToolBar="false" onClickRowEdit="false"    pagination="true" 
						           rownumbers="true"  singleSelect = "false"  showFooter="true"
						           columnsJsonList="[
						           		  {field : ' ',checkbox:true},
						           		  {field : 'cellNo',title : '来源储位',width :80,align:'left'},
						           		  {field : 'destCellNo',title : '预上储位',width :80,align:'left'},
						           		  {field : 'boxNo',title :'箱号',width : 100,align:'left'},
						           		  {field : 'itemNo',title :'商品编码',width : 150,align:'left'},
						                  {field : 'itemName',title :'商品名称',width : 150,align:'left'},
						                  {field : 'sizeNo',title :'尺码',width : 80,align:'left'},
						                  {field : 'instockQty',title : '预上数量',width : 100,align:'right'}
						                 ]" 
							           />
								</div>
							</div>
						</div> 
	        		 </div>
				</div>
		</div> 
	  </div>
	 
	  <div title="上架发单" closable="false" data-options="border:false"> 
			<div class="easyui-layout" data-options="fit:true,border:false">
				<div data-options="region:'center',border:false">
					<div id="ttSend" class="easyui-tabs" data-options="fit:true,border:false"> 
						<div title="未发单" closable="false"> 
							<div class="easyui-layout" data-options="fit:true,border:false">
								<div data-options="region:'north',border:false" >
								
								    <@p.toolbar  id='mains-toolbar' listData=[
							             {"title":"查询","iconCls":"icon-search","action":"billiminstockdirect.searchNoSendOrder(10,1)","type":0},
							             {"title":"清空","iconCls":"icon-remove","action":"billiminstockdirect.searchLocClearCheck('searchFormNo')","type":0},
								         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('上架任务')","type":0}
									 ]/>
									 
									<form name="searchFormNo" id="searchFormNo" method="post" class="city-form">
										<table>
											<tr>
											    <!--
											    <td class="common-td blank">定位类型：</td>
											    <td>
													<input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemTypeCondition" required="true" style="width:120px"/>
											    </td>
											    -->
												<td class="common-td blank">商品属性：</td>
											    <td>
													<input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemTypeCondition" required="true" style="width:120px"/>
											    </td>
											    <td class="common-td blank">品质：</td>
												<td>
													<input class="easyui-combobox"  name="quality" id="qualityCondition" style="width:120px;" required="true" />
												</td>
												<td class="common-td blank">来源单号：</td>
												<td>
													<input class="easyui-validatebox ipt" style="width:130px" name="sourceNo" id="sourceNoCondition"/>
												</td>
											</tr>
										</table>
									</form> 
								    <div style="padding:8px;text-align:right;padding-right:20px">
										用户：<input class="easyui-validatebox ipt" style="width:120px" id="instockWorkerSend" name="instockWorker"/>
										<a id="searchBtn1" href="javascript:billiminstockdirect.selectPickingPeople();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">选择</a>
										<a id="searchBtn" href="javascript:billiminstockdirect.sendOrderByType();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">发单</a>
									</div>
										
								</div>
								<div data-options="region:'center',border:false" >
									<@p.datagrid id="wSendOrder_detail"  loadUrl="" saveUrl=""   defaultColumn=""
					                isHasToolBar="false" onClickRowEdit="false"    pagination="true"
						           rownumbers="true"  singleSelect = "false" showFooter="true"
						           columnsJsonList="[
						           		  {field : ' ',checkbox:true},
						           		  {field : 'rowId',title : '',width :80,align:'left',hidden:true},
						           		  {field : 'ownerNo',title : '',width :80,align:'left',hidden:true},
						           		  {field : 'cellNo',title : '来源储位',width :80,align:'left'},
						           		  {field : 'sourceNo',title :'来源单号',width : 180},
						           		  {field : 'status',title : '状态',width : 80,align:'left',formatter:billiminstockdirect.statusTaskFormatter},
						           		  {field : 'labelNo',title :'箱号',width : 150,align:'left'},
						           		  {field : 'itemNo',title :'商品编码',width : 150,align:'left'},
						                  {field : 'itemName',title :'商品名称',width : 150,align:'left'},
						                  {field : 'sizeNo',title :'尺码',width : 100,align:'left'},
						                  {field : 'itemType',title :'商品属性',width : 80,align:'left',formatter:billiminstockdirect.typeFormatter},
						           		  {field : 'quality',title :'商品品质',width : 70,align:'left',formatter:billiminstockdirect.qualityFormatter},
						           		  {field : 'destCellNo',title : '预上储位',width :80,align:'left'},
						                  {field : 'instockQty',title : '预上数量',width : 80,align:'right'}
						                 ]" 
							           />
								</div>
							</div>
						</div> 
						
						<div title="已发单"> 
						   <div class="easyui-layout" data-options="fit:true,border:false">
						    <div data-options="region:'north',border:false" >
								
								    <@p.toolbar  id='mainsy-toolbar' listData=[
							             {"title":"查询","iconCls":"icon-search","action":"billiminstockdirect.searchNoSendOrder(13,1)","type":0},
							             {"title":"清空","iconCls":"icon-remove","action":"billiminstockdirect.searchLocClearCheck('searchFormYes')","type":0},
								         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('上架任务')","type":0}
									 ]/>
									 
									<form name="searchFormYes" id="searchFormYes" method="post" class="city-form">
										<table>
											<tr>
											    <td class="common-td blank">商品属性：</td>
											    <td>
													<input class="easyui-combobox" data-options="editable:false" name="itemType" id="itemTypeConditionY"  style="width:120px"/>
											    </td>
											    <td class="common-td blank">品质：</td>
												<td>
													<input class="easyui-combobox"  name="quality" id="qualityConditionY" style="width:120px;"  />
												</td>
												<td class="common-td blank">来源单号：</td>
												<td>
													<input class="easyui-validatebox ipt" style="width:130px" name="sourceNo" id="sourceNoConditionY"/>
												</td>
											</tr>
											<tr>
											</tr>
										</table>
									</form> 
								</div>
								<div data-options="region:'center',border:false" >
										<@p.datagrid id="ySendOrder_detail"  loadUrl="" saveUrl=""   defaultColumn=""
						              isHasToolBar="false" onClickRowEdit="false"    pagination="true"
							           rownumbers="true"  singleSelect = "false" height="200" showFooter="true"
							           columnsJsonList="[
							           				  {field : 'cellNo',title : '来源储位',width :80,align:'left'},
									           		  {field : 'sourceNo',title :'来源单号',width : 180},
									           		  {field : 'status',title : '状态',width : 80,align:'left',formatter:billiminstockdirect.statusTaskFormatter},
									           		  {field : 'labelNo',title :'箱号',width : 150,align:'left'},
									           		  {field : 'itemNo',title :'商品编码',width : 160,align:'left'},
									                  {field : 'itemName',title :'商品名称',width : 150,align:'left'},
									                  {field : 'sizeNo',title :'尺码',width : 100,align:'left'},
									                  {field : 'itemType',title :'商品属性',width : 80,align:'left',formatter:billiminstockdirect.typeFormatter},
									           		  {field : 'quality',title :'商品品质',width : 70,align:'left',formatter:billiminstockdirect.qualityFormatter},
									           		  {field : 'destCellNo',title : '预上储位',width :80,align:'left'},
									                  {field : 'instockQty',title : '预上数量',width : 80,align:'right'}
							                 ]"/>
							    </div>
							</div>
					       
						</div> 
						
	        		 </div>
				</div>
			</div>
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
				       		{"id":"btn-add","title":"确定","iconCls":"icon-ok","action":"billiminstockdirect.confirmPickingPeople()", "type":0},
				            {"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billiminstockdirect.closeShowWin('showSelectPickingPeopleWin')","type":0}
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