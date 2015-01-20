<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>供应商管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/supplier.js?version=1.0.6.0"></script>

</head>
<body class="easyui-layout">

	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"supplier.searchSupplier();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"supplier.searchSupplierClear();", "type":0},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
        	<#--查询start-->
        	<div  data-options="region:'north',border:false">
        		<div nowrap id="supplierSearchDiv" class="search-div">
        			<form name="searchForm" id="searchForm" method="post" class="city-form">
		       		 	<table >
							<tr>
								<td class="common-td blank">所属品牌库：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoCondition" data-options="editable:false"/></td>
		                        <td class="common-td blank">供应商编码：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="supplierNo" id="supplierNoCondition" /></td>
		                        <td class="common-td blank">供应商名称：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="supplierName" id="supplierNameCondition" /></td>
		                        <td class="common-td blank">供应商类型：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="supplierType" id="supplierTypeCondition" data-options="editable:false"/></td>
							</tr>
							<tr>
								<td class="common-td blank">供应商状态：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="supplierStatus" id="supplierStatusCondition" data-options="editable:false"/></td>
		                        <td class="common-td blank"></td>
								<td></td>
		                        <td class="common-td blank"></td>
								<td></td>
		                        <td class="common-td blank"></td>
								<td></td>
							</tr>
						</table>
					</form>
        		</div>
   			</div>
        	<#--查询end-->
        	<#--显示列表start-->
        	<div data-options="region:'center',border:false" style="height:300px;" id="supplierSearchDiv">
	    		<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="供应商列表"
		                   isHasToolBar="false" divToolbar=""  onClickRowEdit="false" pagination="true"
		                   rownumbers="true" emptyMsg="" pageNumber=0
			               columnsJsonList="[
			                  {field : 'supplierNo',title : '供应商编码',width : 80,align:'left'},
			                  {field : 'sysNoStr',title : '所属品牌库',width : 80,align:'left'},
			                  {field : 'supplierCode',title : '操作码',width : 80,align:'left'},
			                  {field : 'searchCode',title : '检索码',width : 80,align:'left'},
			                  {field : 'supplierName',title : '供应商名称',width : 200,align:'left'},
			                  {field : 'supplierLname',title : '供应商全称',width : 250,align:'left'},
			                  {field : 'supplierEname',title : '供应商英文名',width : 120,align:'left'},
			                  {field : 'supplierStatusStr',title : '状态',width : 60,align:'left'},
			                  {field : 'supplierTypeStr',title : '供应商类型',width : 80,align:'left'},
			                  {field : 'bizType',title : '经营类型',width : 80,align:'left'},
			                  {field : 'businessTypeStr',title : '经营性质',width : 80,align:'left'},
			                  {field : 'cman',title : '联系人',width : 80,align:'left'},
			                  {field : 'cmanPhone',title : '联系人电话',width : 150,align:'left'},
			                  {field : 'manager',title : '负责人',width : 80,align:'left'},
			                  {field : 'phone',title : '电话',width : 120,align:'left'},
			                  {field : 'identityCard',title : '客户代码证号',width : 150,align:'left'},
			                  {field : 'chairman',title : '法人代表',width : 80,align:'left'},
			                  {field : 'telno',title : '联系电话',width : 80,align:'left'},
			                  {field : 'faxno',title : '公司传真',width : 80,align:'left'},
			                  {field : 'zoneNoStr',title : '所属地区',width : 80,align:'left'},
			                  {field : 'zipno',title : '邮政编码',width : 80,align:'left'},
			                  {field : 'address',title : '地址',width : 200,align:'left'},
			                  {field : 'bankName',title : '开户银行',width : 150,align:'left'},
			                  {field : 'bankAccount',title : '银行账户名',width : 150,align:'left'},
			                  {field : 'bankAccname',title : '银行账号',width : 150,align:'left'},
			                  {field : 'taxpayingNo',title : '纳税号',width : 150,align:'left'},
			                  {field : 'taxLevelStr',title : '纳税级别',width : 100,align:'left'},
			                  {field : 'supplierCardNo',title : '结算卡号',width : 150,align:'left'},
			                  {field : 'remarks',title : '备注',width : 200,align:'left'},
			                  {field : 'creator',title : '建档人',width : 60,align:'left'},
				          	  {field : 'createtm',title : '建档时间',width : 125,sortable:true},
				          	  {field : 'editor',title : '修改人',width : 60,align:'left'},
				          	  {field : 'edittm',title : '修改时间',width : 125,sortable:true}
			                 ]" 
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                            supplier.loadDetail(rowData.supplierNo);
		                   }}'/>
			</div>
        	<#--显示列表end-->
        	<#--显示列表start-->
        	<div data-options="region:'south',split:false,border:false,minSplit:true" style="height:280px;">
				<div id="toolbarEdit" class="easyui-panel" data-options="collapsible:false,height:270,fit:true"  title="明细信息" >
					<div class="easyui-layout" data-options="fit:true">    
						<form name="dataForm" id="dataForm" method="post" class="city-form">
							<table cellspacing='0' cellpadding='0' >
								<tr height='33'>
							 		<td class="common-td blank">供应商编码：</td>
							 		<td width = '120'><input class="easyui-validatebox ipt" style="width:120px" name="supplierNo" id="supplierNo" readOnly="true"/></td>
							 		<td class="common-td blank">所属品牌库：</td>
							 		<td width = '120'><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNo" readOnly="true"/></td>
							 		<td class="common-td blank">供应商操作码：</td>
							 		<td width = '120'><input class="easyui-validatebox ipt" style="width:120px" name="supplierCode" id="supplierCode" readOnly="true"/></td>
							 		<td class="common-td blank">检索码：</td>
							 		<td width = '120'><input class="easyui-validatebox ipt" style="width:120px" name="searchCode" id="searchCode" readOnly="true"/></td>
							 	</tr>
							 	
							 	<tr height='33'>
							 		<td class="common-td blank">供应商名称：</td>
							 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:322px" name="supplierName" id="supplierName" readOnly="true"/></td>
							 		<td class="common-td blank">供应商全称：</td>
							 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:322px" name="supplierLname" id="supplierLname" readOnly="true"/></td>
							 	</tr>
							 	
							 	<tr height='33'>
							 		<td class="common-td blank">供应商英文名：</td>
							 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:322px" name="supplierEname" id="supplierEname" readOnly="true"/></td>
							 		<td class="common-td blank">状态：</td>
							 		<td><input class="easyui-combobox ipt" style="width:120px" name="supplierStatus" id="supplierStatus" readOnly="true"/></td>
							 		<td class="common-td blank">供应商类型：</td>
							 		<td><input class="easyui-combobox ipt" style="width:120px" name="supplierType" id="supplierType" readOnly="true"/></td>
							 	</tr>
							 	
							 	<tr height='33'>
							 		<td class="common-td blank">联系人：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="cman" id="cman" readOnly="true"/></td>
							 		<td class="common-td blank">负责人：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="manager" id="manager" readOnly="true"/></td>
							 		<td class="common-td blank">联系人电话：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="cmanPhone" id="cmanPhone" readOnly="true"/></td>
							 		<td class="common-td blank">电话：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="phone" id="phone" readOnly="true"/></td>
							 	</tr>
							 	
							 	<tr height='33'>
							 		<td class="common-td blank">客户代码证号：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="identityCard" id="identityCard" readOnly="true"/></td>
							 		<td class="common-td blank">法人代表：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="chairman" id="chairman" readOnly="true""/></td>
							 		<td class="common-td blank">联系电话：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="telno" id="telno" readOnly="true"/></td>
							 		<td class="common-td blank">公司传真：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="faxno" id="faxno" readOnly="true"/></td>
							 	</tr>
							 	
							 	<tr height='33'>
							 		<td class="common-td blank">所属地区：</td>
							 		<td><input class="easyui-combobox ipt" style="width:120px" name="zoneNo" id="zoneNo" readOnly="true"/></td>
							 		<td class="common-td blank">邮政编码：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="zipno" id="zipno" readOnly="true"/></td>
							 		<td class="common-td blank">地址：</td>
							 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:322px" name="address" id="address" readOnly="true"/></td>
							 	</tr>
							 	
							 	<tr height='33'>
							 		<td class="common-td blank">开户银行：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="bankName" id="bankName" readOnly="true"/></td>
							 		<td class="common-td blank"> 银行账户名：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="bankAccount" id="bankAccount" readOnly="true"/></td>
							 		<td class="common-td blank">银行账号：</td>
							 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:322px" name="bankAccname" id="bankAccname" readOnly="true"/></td>
							 	</tr>
							 	
							 	<tr height='33'>
							 		<td class="common-td blank">纳税号：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="taxpayingNo" id="taxpayingNo" readOnly="true"/></td>
							 		<td class="common-td blank">纳税级别：</td>
							 		<td><input class="easyui-combobox ipt" style="width:120px" name="taxLevel" id="taxLevel"  readOnly="true"/></td>
							 		<td class="common-td blank">结算卡号：</td>
							 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:322px" name="supplierCardNo" id="supplierCardNo" readOnly="true"/></td>
							 	</tr>
							 	
							 	<tr height='33'>
							 		<td class="common-td blank">经营类型：</td>
							 		<td><input class="easyui-validatebox ipt" style="width:120px" name="bizType" id="bizType" readOnly="true" value="0"/></td>
							 		<td class="common-td blank">经营性质：</td>
							 		<td><input readOnly="true" class="easyui-combobox ipt" style="width:120px" name="businessType" id="businessType" readOnly="true"/></td>
							 		<td class="common-td blank">备注：</td>
						 			<td colspan='3'><input readOnly="true" class="easyui-validatebox ipt" style="width:322px" name="remarks" id="remarks" readOnly="true"/></td>
							 	</tr>
							 	
							 </table>
					  	</form>
					 </div>
				</div>
			</div>
			<#--显示列表end-->
        </div>
   	</div>
    <#-- 主表end -->
    
</body>
</html>