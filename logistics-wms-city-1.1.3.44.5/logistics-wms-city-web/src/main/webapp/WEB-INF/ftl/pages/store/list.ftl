<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
  <title>客户管理</title>
 <#include  "/WEB-INF/ftl/common/header.ftl" >
 <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/store.js?version=1.0.5.1"/>"></script>
</head>
<body class="easyui-layout">
<#--
<div data-options="region:'center',border:false" style="padding:10px;">
    <div class="easyui-layout" data-options="fit:true,border:false">
        
        <div data-options="region:'west',width:200,minWidth:200,minSplit:true,title:'机构'" >
            <div class="pd10" id="dataTreeId">
            </div>
        </div>
        -->
        <div data-options="region:'center'">
            <div id="subLayout" class="easyui-layout" data-options="fit:true,border:false">
                      <div data-options="region:'north',border:false">
								<@p.toolbar id="toolbar" listData=[
										 {"id":"btn-searchId","title":"查询","iconCls":"icon-search","action":"store.searchStore();", "type":0},
				                         {"id":"btn-removeId","title":"清除","iconCls":"icon-remove","action":"store.searchStoreClear();", "type":0},
								         {"id":"btn-export","title":"导出","iconCls":"icon-export","action":"store.do_export()","type":5},
								         {"id":"btn-closeId","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
									 ]
								/>
								
								<#-- 查询面板div -->
								<div id="storeSearchDiv" class="search-div" style="background:#F4F4F4;">
									     <form id='exportForm' method="post" action="../store/doExport">
											<input type="hidden" name="storeNo" id="storeNo_export"/>
											<input type="hidden" name="storeCode" id="storeCode_export" />
											<input type="hidden" name="storeName" id="storeName_export"/>
											<input type="hidden" name="cman" id="cman_export"/>
											<input type="hidden" name="status" id="status_export" />
											<input type="hidden" name="sysNo" id="sysNo_export" />
											<input type="hidden" name="zoneNo" id="zoneNo_export"/>
											<input type="hidden" name="storeType" id="storeType_export"/>
											<input type="hidden" name="circleNo" id="circleNo_export"/>
											<input type="hidden" name="queryStoreType" id="queryStoreType"/>
											<input type="hidden" name="isLimitLocno" id="isLimitLocno_export"/>
											<input type="hidden" name="locno" id="locno_export"/>
											<input type="hidden" name="exportColumns" id="exportColumnsCondition_export"/>
											<input type="hidden" name="fileName" value="客户管理报表"/>
										</form>
									     
									     <form name="searchForm" id="searchForm" method="post" class="city-form">
								       		 	<table>
												 	<tr>
												 		<td class="common-td blank">机构编码：</td>
												 		<td ><input class="easyui-validatebox ipt" style="width:110px" name="storeNo" id="storeNoCondition" /></td>
												 		<td class="common-td blank">机构外码：</td>
												 		<td ><input class="easyui-validatebox ipt" style="width:110px" name="storeCode" id="storeCodeCondition"/></td>
												 		<td class="common-td blank">机构名称：</td>
												 		<td ><input class="easyui-validatebox ipt" style="width:110px" name="storeName" id="storeNameCondition" /></td>
												 		<td class="common-td blank">联系人：</td>
												 		<td ><input class="easyui-validatebox ipt" style="width:110px" name="cman" id="cmanCondition"/></td>
												 	</tr>
												 	<tr>
												 		<td class="common-td blank">机构状态：</td>
												 		<td ><input class="easyui-combobox ipt" style="width:120px" name="status" id="statusCondition" /></td>
												 		<td class="common-td blank">所属品牌库：</td>
												 		<td ><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoCondition"/></td>
												 		<td class="common-td blank">所属区域：</td>
												 		<td ><input class="easyui-combobox ipt" style="width:120px" name="zoneNo" id="zoneNoCondition" /></td>
												 		<td class="common-td blank">机构类型：</td>
												 		<td ><input class="easyui-combobox ipt" style="width:120px" name="storeType" id="storeTypeCondition" /></td>
												 	</tr>
												 	<tr>
												 		<td class="common-td blank">卸&nbsp;货&nbsp;点：</td>
												 		<td ><input class="easyui-combobox" style="width:120px" name="circleNo" id="circleNoCondition"/></td>
												 		<td class="common-td blank"></td>
												 		<td ></td>
												 		<td class="common-td blank"></td>
												 		<td ></td>
												 		<td class="common-td blank"></td>
												 		<td ></td>
												 	</tr>
												</table>
											</form>
								</div>
                      </div>
		               <div data-options="region:'center',border:false">
		                      <@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn=""   title="机构列表"
					              isHasToolBar="false"   height="380"  onClickRowEdit="false"    pagination="true"
						           rownumbers="true" pageNumber=0
						           columnsJsonList="[
						           		  {field:'ck',checkbox:true}, 
						                  {field : 'storeNo',title : '机构编码',width : 120,align:'left'},
						                  {field : 'storeCode',title : '机构外码',width : 100,align:'left'},
						                  {field : 'statusStr',title : '机构状态',width : 100,align:'left'},
						                  {field : 'sysNoStr',title : '所属品牌库',width : 100,align:'left'},
						                  {field : 'storeLname',title : '机构全称',width : 200,align:'left'},
						                  {field : 'storeName',title : '机构名称',width : 200,align:'left'},
						                  {field : 'storeTypeStr',title : '机构类型',width : 120,align:'left'},
						                  {field : 'cman',title : '联系人',width : 80,align:'left'},
						                  {field : 'cphone',title : '手机号码',width : 100,align:'left'},
						                  {field : 'telno',title : '联系电话',width : 100,align:'left'},
						                  {field : 'faxno',title : '传真',width : 100,align:'left'},
						                  {field : 'zipno',title : '邮编',width : 100,align:'left'},
						                  {field : 'address',title : '地址',width : 200,align:'left'},
						                  {field : 'areaTotal',title : '总面积',width : 100,align:'left'},
						                  {field : 'area',title : '营业面积',width : 100,align:'left'},
						                  {field : 'manager',title : '负责人',width : 80,align:'left'},
						                  {field : 'searchCode',title : '检索码',width : 100,align:'left'},
						                  {field : 'storeNoHead',title : '上级机构ID',width : 100,align:'left'},
						                  {field : 'storeType2',title : '机构类型2',width : 100,align:'left'},
						                  {field : 'dtsNo',title : '传输节点',width : 100,align:'left'},
						                  {field : 'bookNo',title : '帐套编码',width : 100,align:'left'},
						                  {field : 'class3',title : '店用户分类',width : 100,align:'left'},
						                  {field : 'province',title : '省份',width : 100,align:'left'},
						                  {field : 'workCity',title : '城市',width : 100,align:'left'},
						                  {field : 'manageZoneNo',title : '区/县',width : 100,align:'left'},
						                  {field : 'incity',title : '片区',width : 100,align:'left'},
						                  {field : 'circleName',title : '卸货点',width : 100,align:'left'},
						                  {field : 'circle',title : '商圈',width : 100,align:'left'},
						                  {field : 'class2',title : '店仓级别',width : 100,align:'left'},
						                  {field : 'zoneNo',title : '地区编码',width : 100,align:'left'},
						                  {field : 'storeNoDc',title : '从属主仓',width : 100,align:'left'},
						                  {field : 'storeNo2',title : '店号2',width : 100,align:'left'},
						                  {field : 'storeNo3',title : '店号3',width : 100,align:'left'},
						                  {field : 'managerNo',title : '店长代号',width : 100,align:'left'},
						                  {field : 'opendt',title : '开店日期',width : 125},
						                  {field : 'closedt',title : '撤柜日期',width : 125},
						                  {field : 'remarks',title : '备注',width : 100,align:'left'},
						                  {field : 'recievetm',title : '接受时间',width : 125},  
						                  {field : 'creator',title : '建档人',width : 80,align:'left'},
								          {field : 'createtm',title : '建档时间',width : 125,sortable:true},
								          {field : 'editor',title : '修改人',width : 80,align:'left'},
								          {field : 'edittm',title : '修改时间',width : 125,sortable:true}
						                 ]" 
							           jsonExtend='{onSelect:function(rowIndex, rowData){
					                            // 触发点击方法  调JS方法
					                            store.loadDetail(rowData.storeNo);
					                   }}'/>
		               </div>
		               <div style="background:#F4F4F4;" data-options="region:'south',border:false,height:310,maxHeight:310,minHeight:310,minSplit:true" class="pt15">
		                	   <form name="dataForm" id="dataForm" method="post">
					           		<div style="padding-left:10px;">
						           		<table width='813' cellspacing='0' cellpadding='0' >
										 	<tr height='33'>
										 		<td align='left' width='67'>机构编码：</td>
										 		<td width = '128'><input class="easyui-validatebox ipt" style="width:110px" name="storeNo" id="storeNo" readOnly="true"/></td>
										 		<td align='left' width='67'>机构状态：</td>
										 		<td width = '128'><input class="easyui-combobox ipt" style="width:120px" name="status" id="status"   readOnly="true"/></td>
										 		<td align='left' width='80'>所属品牌库：</td>
										 		<td width = '128'><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNo"  readOnly="true"/></td>
										 		<td align='left' width='90'>所属区域：</td>
										 		<td width = '128'><input class="easyui-combobox ipt" style="width:120px" name="zoneNo" id="zoneNo"  readOnly="true"/></td>
										 	</tr>
										 	
										 	<tr height='33'>
										 		<td align='left'>机构名称：</td>
										 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:305px" name="storeName" id="storeName"  readOnly="true"/></td>
										 		<td align='left'>机构全称：</td>
										 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:329px" name="storeLname" id="storeLname" readOnly="true"/></td>
										 	</tr>
										 	
										 	<tr height='33'>
										 		<td align='left'>联系人：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="cman" id="cman"  readOnly="true"/></td>
										 		<td align='left'>手机号码：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="cphone" id="cphone"   readOnly="true"/></td>
										 		<td align='left'>第二联系人：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="cman1" id="cman1"  readOnly="true"/></td>
										 		<td align='left'>第二手机号码：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="cphone1" id="cphone1"   readOnly="true"/></td>
										 	</tr>
										 	
										 	<tr height='33'>
										 		<td align='left'>省份：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="province" id="province"  readOnly="true"/></td>
										 		<td align='left'>联系电话：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="telno" id="telno"   readOnly="true"/></td>
										 		<td align='left'>传真：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="faxno" id="faxno"  readOnly="true"/></td>
										 		<td align='left'>邮编：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="zipno" id="zipno"   readOnly="true"/></td>
										 	</tr>
										 	
										 	<tr height='33'>
										 		<td align='left'>邮箱：</td>
										 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:307px" name="email" id="email" readOnly="true"/></td>
										 		<td align='left'>机构类型2：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="storeType2" id="storeType2"   readOnly="true"/></td>
										 		<td align='left'>机构类型：</td>
										 		<td><input class="easyui-combobox ipt" style="width:120px" name="storeType" id="storeType"   readOnly="true"/></td>
										 	</tr>
										 	
										 	<tr height='33'>
										 		<td align='left'>仓库邮箱：</td>
										 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:307px" name="storeEmail" id="storeEmail" readOnly="true"/></td>
										 		<td align='left'>地址：</td>
										 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:329px" name="address" id="address"  readOnly="true"/></td>
										 	</tr>	
										 	
										 	<tr height='33'>
										 		<td align='left'>城市：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="workCity" id="workCity"   readOnly="true"/></td>
										 		<td align='left'>上级机构：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="storeNoHeadStr" id="storeNoHeadStr"  readOnly="true"/></td>
										 		<td align='left'>负责人：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="manager" id="manager"   readOnly="true"/></td>
										 		<td align='left'>总面积：</td>
										 		<td><input class="easyui-numberbox ipt" style="width:110px" name="areaTotal" id="areaTotal" data-options="min:0,precision:2" readOnly="true"/></td>
										 	</tr>
										 	
										 	<tr height='33'>
										 		<td align='left'>建档人：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="creator" id="creator"   readOnly="true"/></td>
										 		<td align='left'>建档时间：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="createtm" id="createtm"   readOnly="true"/></td>
										 		<td align='left'>修改人：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="editor" id="editor"  readOnly="true"/></td>
										 		<td align='left'>修改时间：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:110px" name="edittm" id="edittm"  readOnly="true"/></td>
										 	</tr>
										</table>
									</div>
				               </form>
		               </div>
            </div>
        <#--
        </div>
    </div>
    -->
</div>
</body>
</html>