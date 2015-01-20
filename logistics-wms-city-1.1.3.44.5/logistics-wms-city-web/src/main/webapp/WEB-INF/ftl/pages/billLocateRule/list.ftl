<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>储位锁定策略</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billLocateRule/billLocateRule.js?version=1.0.6.0"></script>
</head>
<body class="easyui-layout">

	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"billLocateRule.searchData();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"billLocateRule.searchClear('searchForm');", "type":0},
			{"title":"新增","iconCls":"icon-add","action":"billLocateRule.add();", "type":1},
	        {"title":"修改","iconCls":"icon-edit","action":"billLocateRule.toUpdate();","type":2},
	        {"title":"删除","iconCls":"icon-del","action":"billLocateRule.doDel();","type":3},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" class="search-div" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
				        	<tr>
				            	<td class="common-td">策略编码：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="ruleNo" id="ruleNoCon" /></td>
				                <td class="common-td blank">策略名称：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="ruleName" id="ruleNameCon" /></td>
				                <td class="common-td blank">状态：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="status" id="statusCon" /></td>
				                <td class="common-line"></td>
				                <td></td>
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
      		<#--查询end-->
			<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    		<@p.datagrid id="dataGridJG"  loadUrl="/bill_locate_rule/list.json?locno=${session_user.locNo}"  saveUrl=""  defaultColumn=""  title="按箱分货规则列表"
					isHasToolBar="false" divToolbar=""  height="430"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
					rownumbers="true" emptyMsg=""
					columnsJsonList="[
						{field : 'id',checkbox:true},
						{field : 'ruleNo',title : '策略编码',width : 100,align:'left'},
						{field : 'ruleName',title : '策略名称',width : 130,align:'left'},
						{field : 'ruleDesc',title : '描述',width : 150,align:'left'},
						{field : 'statusStr',title : '状态',width : 80,align:'left'},
						{field : 'remark',title : '备注',width : 180,align:'left'}
						]" 
						jsonExtend='{onDblClickRow:function(rowIndex, rowData){
					    	// 触发点击方法  调JS方法
					        billLocateRule.edit(rowData.ruleNo,1);
					}}'
				/>
			</div>
        	<#--显示列表end-->
	    </div>
	</div>  
	<#-- 主表end -->
	
	<#-- 明细信息div -->
	<div id="showAddDialog"  class="easyui-dialog" title="新增"  
			data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		<div class="easyui-layout" data-options="fit:true">
			<#-- 工具菜单start -->
			<div data-options="region:'north',border:false" class="toolbar-region">
				<div data-options="region:'north',border:false">
					
					<@p.toolbar id="addtoolbar"   listData=[
						{"id":"info_add","title":"保存","iconCls":"icon-save","action":"billLocateRule.doSave('0');", "type":1},
						{"id":"info_save","title":"修改","iconCls":"icon-edit","action":"billLocateRule.doSave('1');", "type":2},
						{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billLocateRule.closeWindow('showAddDialog');","type":0}
					]/>
					
				</div>
		    </div>
		    <#-- 工具菜单end -->
		    <div data-options="region:'center',border:false" class="toolbar-region"  id = 'detailDiv'>
		    	<div class="easyui-layout" data-options="fit:true">
		    		<form name="dataForm" id="dataForm" method="post" class="city-form">
						<#--start-->
						<div data-options="region:'north',border:false">
							<div style="padding-top:5px;">
								<fieldset style="border:2px groove #FFFFFF; color:#000000; padding-left:4px;
									 padding-right:4px; padding-top:6px; padding-bottom:1px;height:80px;">
									<legend>&nbsp;<strong>基础信息</strong>&nbsp;</legend>	
									<table >
										<tr>
											<td class="common-td blank">策略编码：</td>
											<td><input class="easyui-validatebox ipt" style="width:120px" name="ruleNo" id="ruleNo" required="true" missingMessage="储位策略编码为必填项!" /></td>
											<td class="common-td blank">策略名称：</td>
											<td><input class="easyui-validatebox ipt" style="width:120px" name="ruleName" id="ruleName" required="true" missingMessage="储位策略名称为必填项!"/></td>
											<td class="common-td blank">描&nbsp;&nbsp;&nbsp;&nbsp;述：</td>
											<td><input class="easyui-validatebox ipt" style="width:200px" name="ruleDesc" id="ruleDesc" /></td>
											<td  class="common-td blank"></td>
											<td></td>
										</tr>
										<tr>
											<td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
											<td colspan="7"><input class="easyui-validatebox ipt" style="width:580px" name="remark" id="remark" /></td>
										</tr>
									</table>
								</fieldset>
							</div>
							<div style="padding-top:5px;">
								<table id="tb_main" width="100%">
									<tr>
										<td>
											<fieldset style="border:2px groove #FFFFFF; color:#000000; padding-left:4px; 
												 padding-right:4px; padding-top:6px; padding-bottom:1px;height:45px;">
												<legend>&nbsp;<strong>先进先出规则</strong>&nbsp;</legend>
												<input id="curRuleVal" type="hidden" value="0"/>
												<table>
													<tr>
														<td class="common-td blank"></td>
														<td><input name="ruleFirst" type="checkbox" value="1" checked="checked"/></td>
														<td class="common-td" style="width:100px"><div align="left">先进先出</div></td>
														<td><input name="ruleFirst" type="checkbox" value="2"/></td>
														<td class="common-td" style="width:100px"><div align="left">先进后出</div></td>
													</tr>
												</table>
											</fieldset>	
										</td>
										<td>
											<fieldset style="border:2px groove #FFFFFF; color:#000000; padding-left:4px; 
												 padding-right:4px; padding-top:6px; padding-bottom:1px;height:45px;">
												<legend>&nbsp;<strong>箱</strong>&nbsp;</legend>
												<table>
													<tr>
														<td class="common-td blank"></td>
														<td><input name="ruleBox" type="radio" value="1" checked="checked"/></td>
														<td class="common-td" style="width:100px"><div align="left">优先散件</div></td>
														<td><input name="ruleBox" type="radio" value="2"/></td>
														<td class="common-td" style="width:100px"><div align="left">优先整箱</div></td>
													</tr>
												</table>
											</fieldset>
										</td>
									</tr>
								</table>
							</div>
							<div style="padding-top:5px;">
								<fieldset style="border:2px groove #FFFFFF; color:#000000; padding-left:4px; 
									 padding-right:4px; padding-top:6px; padding-bottom:1px;height:45px;">
									<legend>&nbsp;<strong>商品规则</strong>&nbsp;</legend>
									<table>
										<tr>
											<td class="common-td blank"></td>
											<td><input name="ruleItem" type="radio" value="1" checked="checked"/></td>
											<td class="common-td" style="width:120px"><div align="left">优先同批商品入库</div></td>
											<td><input name="ruleItem" type="radio" value="2"/></td>
											<td class="common-td" style="width:120px"><div align="left">优先到期商品</div></td>
											<td><input name="ruleItem" type="radio" value="3"/></td>
											<td class="common-td" style="width:120px"><div align="left">优先低库存储位</div></td>
										</tr>
									</table>
								</fieldset>
							</div>
			   			</div>
			   			<#--end-->
			   			<#--detailDiv start-->
		    			<div data-options="region:'center',border:false" style="padding-top:5px;">
							<@p.toolbar id="toolsDiv" listData=[
								{"title":"新增明细","iconCls":"icon-add-dtl","action":"billLocateRule.showAddCategory();", "type":0},
								{"title":"删除明细","iconCls":"icon-del-dtl","action":"billLocateRule.delCategory();", "type":0}
							]/>
								<@p.datagrid id="dataGridCategory" name=""   loadUrl="" saveUrl="" defaultColumn="" title="商品类别"
								isHasToolBar="false" divToolbar="#toolsDiv"  onClickRowEdit="true" singleSelect="true" pageSize='20'  
								pagination="true" rownumbers="true" emptyMsg="" 
								columnsJsonList="[
									{field : 'id',checkbox:true},
									{field : 'ruleNo',hidden :true},
									{field : 'ruleCateno',title : '类别编码',width : 120,align:'left'},
									{field : 'ruleCatename',title : '类别名称',width : 180,align:'left'}
								]" 
								jsonExtend='{}'
							/>
						</div>
						<#--detailDiv end-->
					</form>
		    	</div>
		    </div>
		</div>	
	</div>
	<#-- 明细信息div -->
	
	<#--箱号选择div -->
	<div id="openCategory"  class="easyui-dialog" title="类别选择"  
		 data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		 maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true" id="categorySearchDiv">
			<#--查询start-->
			<div data-options="region:'north',border:false" >
				<@p.toolbar id="itemtoolbar"   listData=[
					{"title":"查询","iconCls":"icon-search","action":"billLocateRule.searchCategory();", "type":0},
					{"title":"清除","iconCls":"icon-remove","action":"billLocateRule.searchClear('categorySearchForm');", "type":0},
					{"title":"确定","iconCls":"icon-ok","action":"billLocateRule.selectCategory();", "type":0},
					{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billLocateRule.closeWindow('openCategory');","type":0}
				]/>
				<div nowrap class="search-div" style="padding:10px;">
					<form name="categorySearchForm" id="categorySearchForm" metdod="post" class="city-form">
						类别编码：<input class="easyui-validatebox ipt" style="width:110px" name="cateNo" id="cateNoCon" />
						&nbsp;类别名称：<input class="easyui-validatebox ipt" style="width:110px" name="cateName" id="cateNameCon" />
					</form>
				</div>
			</div>
			<#--查询end-->
			<#--显示列表start-->
				<div data-options="region:'center',border:false">
					<@p.datagrid id="dataGridCategoryOpen"  loadUrl=""  saveUrl=""  defaultColumn="" 
					  	isHasToolBar="false" divToolbar="" height="285"  onClickRowEdit="false" 
					  	singleSelect="false" pageSize='20' pagination="true" rownumbers="true" emptyMsg="" 
						columnsJsonList="[
						           		{field : 'id',checkbox:true},
										{field : 'cateNo',title : '类别编码',width : 120,align:'left'},
										{field : 'cateName',title : '类别名称',width : 180,align:'left'}
						]"
						jsonExtend='{}'
					 />
				</div>
				<#--显示列表end-->
		</div>	
	</div>	
	<#--箱号选择div -->
	
</body>
</html>