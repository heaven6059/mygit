<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>按箱分货规则管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billstorerule/billStoreRule.js?version=1.0.5.0"></script>
</head>
<body class="easyui-layout">
	    <div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
			<@p.toolbar id="toolBarOne" listData=[
					{"title":"查询","iconCls":"icon-search","action":"billStoreRule.searchData();","type":0},
				    {"title":"清空","iconCls":"icon-remove","action":"billStoreRule.searchClear();","type":0}
					{"title":"新增","iconCls":"icon-add","action":"billStoreRule.add()","type":1},
					{"title":"修改","iconCls":"icon-edit","action":"billStoreRule.toUpdate()","type":2},
					{"title":"删除","iconCls":"icon-del","action":"billStoreRule.do_del()","type":3},
					{"title":"关闭","iconCls":"icon-close","action":"closeWindow('按箱分货规则管理');","type":0}
		    ]/>	
		</div>
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true">
	 			<div data-options="region:'north',border:false" >		
						<div id="searchDiv" style="padding:10px;">
							<form name="searchForm" id="searchForm" method="post" class="city-form">
								<table>
									<tr>
										<td class="common-td">状态：</td>
										<td><input class="easyui-combobox ipt" style="width:120px" name="status" id="statusCon" /></td>
										<td class="common-td blank">模板编码：</td>
										<td><input class="easyui-combobox ipt" style="width:120px" name="tempNo" id="tempNoCon" data-options="
													                 						editable:false,
													                 						onChange:function(){
																								billStoreRule.initRuleNo('ruleNoCon','tempNoCon');
																							}"
																						/></td>
										<td class="common-td blank">规则编码：</td>
										<td><input class="easyui-combobox ipt" style="width:120px" name="ruleNo" id="ruleNoCon" /></td>
										<td class="common-td blank">规则名称：</td>
										<td><input class="easyui-validatebox ipt" style="width:120px" name="ruleName" id="ruleNameCon" /></td>
									</tr>
								</table>
							</form>
						</div>
				</div>			
	<div data-options="region:'center',border:false">
		<@p.datagrid id="dataGridJG"  loadUrl="/bill_store_rule/list.json?locno=${session_user.locNo}"  saveUrl=""  defaultColumn=""  title="按箱分货规则列表"
			isHasToolBar="false" divToolbar=""  height="430"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			rownumbers="true" emptyMsg=""
			columnsJsonList="[
				{field : 'id',checkbox:true},
				{field : 'tempNo',title : '模板编码',width : 80,align:'left'},
				{field : 'ruleNo',title : '规则编码',width : 80,align:'left'},
				{field : 'ruleName',title : '规则名称',width : 100,align:'left'},
				{field : 'statusStr',title : '状态',width : 80,align:'left'},
				{field : 'groupA',title : 'A组范围值',width : 70,align:'right'},
				{field : 'groupB',title : 'B组范围值',width : 70,align:'right'},
				{field : 'groupC',title : 'C组范围值',width : 70,align:'right'},
				{field : 'groupD',title : 'D组范围值',width : 70,align:'right'},
				{field : 'groupE',title : 'E组范围值',width : 70,align:'right'},
				{field : 'groupF',title : 'F组范围值',width : 70,align:'right'},
				{field : 'groupG',title : 'G组范围值',width : 70,align:'right'},
				{field : 'groupH',title : 'H组范围值',width : 70,align:'right'},
				{field : 'groupI',title : 'I组范围值',width : 70,align:'right'},
				{field : 'groupJ',title : 'J组范围值',width : 70,align:'right'}
				]" 
				jsonExtend='{onDblClickRow:function(rowIndex, rowData){
			    	// 触发点击方法  调JS方法
			        billStoreRule.edit(rowData,1);
		}}'/>
		</div>
	</div>
</div>	
<#-- 分货单明细窗口 BEGIN -->
<div id="showAddDialog"  class="easyui-window" 
		data-options="modal:true,resizable:false,draggable:true,
		collapsible:false,closed:true,minimizable:false,maximized:true,maximizable:false">
		
		<form name="dataForm" id="dataForm" method="post">
			<div style="padding-top:5px;padding-bottom:5px;">
				<fieldset style="border:2px groove #FFFFFF; color:#000000; padding-left:4px; 
					padding-right:4px; padding-top:10px; padding-bottom:1px;height:45px;">
					    <legend>&nbsp;<strong>基础信息</strong>&nbsp;</legend>
					   	模板编码：<input class="easyui-combobox ipt" style="width:120px" name="tempNo" id="tempNo"  required="true" missingMessage="模板编码为必选项!" 
					   		data-options="editable:false,
					   				onChange:function(){billStoreRule.initRuleNo('ruleNo','tempNo');}"/>
					   	&nbsp;&nbsp;
					   	规则编码：<input class="easyui-combobox ipt" style="width:120px" name="ruleNo" id="ruleNo" data-options="panelHeight:'auto'" required="true" missingMessage="规则编码为必选项!"/>
					   	&nbsp;&nbsp;
					   	规则名称：<input class="easyui-validatebox ipt" style="width:120px" name="ruleName" id="ruleName" />
					   	&nbsp;&nbsp;
					   	状态：<input class="easyui-combobox ipt" style="width:120px" name="status" id="status" />
				</fieldset>
			</div>
			
			<div style="padding-top:5px;padding-bottom:5px;">
				<fieldset style="border:2px groove #FFFFFF; color:#000000; padding-left:4px; 
					padding-right:4px; padding-top:10px; padding-bottom:1px;height:87px;">
					    <legend>&nbsp;<strong>店分组规则</strong>&nbsp;</legend>
					   	分组依据：<input class="easyui-validatebox ipt" style="width:120px" name="storeBasic" id="storeBasic" />
					   	&nbsp;&nbsp;
					   	规则排序：<input class="easyui-combobox ipt" style="width:120px" name="storeSort" id="storeSort" /><br/><br/>
				
						A&nbsp;<input class="easyui-numberbox ipt" style="width:30px;height=22px" name="groupA" id="group1" data-options="min:0,precision:0" value=""/>
						&nbsp;B&nbsp;<input class="easyui-numberbox ipt" style="width:35px;height=22px" name="groupB" id="group2" data-options="min:0,precision:0" value=""/>
						&nbsp;C&nbsp;<input class="easyui-numberbox ipt" style="width:35px;height=22px" name="groupC" id="group3" data-options="min:0,precision:0" value=""/>
						&nbsp;D&nbsp;<input class="easyui-numberbox ipt" style="width:35px;height=22px" name="groupD" id="group4" data-options="min:0,precision:0" value=""/>
						&nbsp;E&nbsp;<input class="easyui-numberbox ipt" style="width:35px;height=22px" name="groupE" id="group5" data-options="min:0,precision:0" value=""/>
						&nbsp;F&nbsp;<input class="easyui-numberbox ipt" style="width:35px;height=22px" name="groupF" id="group6" data-options="min:0,precision:0" value=""/>
						&nbsp;G&nbsp;<input class="easyui-numberbox ipt" style="width:35px;height=22px" name="groupG" id="group7" data-options="min:0,precision:0" value=""/>
						&nbsp;H&nbsp;<input class="easyui-numberbox ipt" style="width:35px;height=22px" name="groupH" id="group8" data-options="min:0,precision:0" value=""/>
						&nbsp;I&nbsp;<input class="easyui-numberbox ipt" style="width:35px;height=22px" name="groupI" id="group9" data-options="min:0,precision:0" value=""/>
						&nbsp;J&nbsp;<input class="easyui-numberbox ipt" style="width:35px;height=22px" name="groupJ" id="group10" data-options="min:0,precision:0" value=""/>
						
				</fieldset>
			</div>
			
			<div style="padding-top:5px;padding-bottom:5px;">
				<fieldset style="border:2px groove #FFFFFF; color:#000000; padding-left:4px; 
					padding-right:4px; padding-top:10px; padding-bottom:1px;height:50px;">
					    <legend>&nbsp;<strong>分货优先级别</strong>&nbsp;</legend>
					    
					   	依据&nbsp;<input class="easyui-combobox ipt" style="width:120px" name="cargoBasic" id="cargoBasic" />
					   	从
					   	<input class="easyui-combobox ipt" style="width:120px" name="cargoSort" id="cargoSort" />&nbsp;分配
					   	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					           箱类型：<input class="easyui-combobox ipt" style="width:120px" name="boxType" id="boxType" />
					   	&nbsp;&nbsp;
					   	箱号：<input class="easyui-combobox ipt" style="width:120px" name="boxSort" id="boxSort" />
					   	<br/><br/>
					   	
				</fieldset>
			</div>
			
			<div style="padding-top:5px;">
				<fieldset style="border:2px groove #FFFFFF; color:#000000; padding-left:4px; 
					padding-right:4px; padding-top:10px; padding-bottom:1px;height:50px;">
					    <legend>&nbsp;<strong>箱分组规则</strong>&nbsp;</legend>
					    
					   	&nbsp;&nbsp;
					   	一箱多组：<input class="easyui-combobox ipt" style="width:120px" name="boxBasic" id="boxBasic" />
					   	&nbsp;&nbsp;
					   	<input name="boxFlagCheck" id="boxFlag" type="checkbox" value="0"/>拼箱为一组
					   	
				</fieldset>
			</div>
			
		</form>
		
		<#-- 工具菜单div -->
		<div style="margin-bottom:0px;padding-top:5px;text-align:center;">
			<a id="info_save" class="easyui-linkbutton" iconCls="icon-save" >保存</a>
			<a href="javascript:billStoreRule.closeWindow();" class="easyui-linkbutton" iconCls="icon-close" >关闭</a>
		</div>		          
</div>
<#-- 分货单明细窗口 END -->
</body>
</html>