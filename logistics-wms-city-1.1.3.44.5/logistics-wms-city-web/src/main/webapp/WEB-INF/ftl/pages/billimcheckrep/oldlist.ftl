<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>入库查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billimcheckrep/billimcheckrep.js?version=1.1.1.1"></script>
</head>
<body onload="javascript:billimcheckrep.getColumnInfo();" class="easyui-layout">
      			<#-- 工具菜单div 	-->
		       <div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
					    <@p.toolbar  id="toolBarOne" listData=[
					    			{"title":"查询","iconCls":"icon-search","action":"billimcheckrep.searchBillImCheckRep();","type":0},
       								{"title":"清空","iconCls":"icon-remove","action":"billimcheckrep.clearForm();","type":0},
					    			 {"title":"导出","iconCls":"icon-export","action":"exportExcel();","type":5}
			                       ]
						  />	
			   </div>
<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true" id="subLayout">
 		<div  data-options="region:'north',border:false" >
 		 	<div class="search-div" id="itemCellContentSearchDiv">
				<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
								<tr>
									<td class="common-td">品牌：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="brandName" id="brandNameId"/></td>
									<td class="common-td blank">商品类别：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="itemCate" id="itemCateId" /></td>
									<td class="common-td blank">供&nbsp;应&nbsp;商：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="supplierNo" id="supplierNameId" /></td>
									<td class="common-td blank">预到货通知单：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo"/></td>
								</tr>
								<tr>
									<td class="common-td">入库日期：</td>
									<td><input class="easyui-datebox ipt" style="width:120px" name="reciveDateStart" id="startTime"/></td>
									<td class="common-line">&mdash;</td>
									<td><input class="easyui-datebox ipt" style="width:120px" name="reciveDateEnd" id="endTime"/></td>
									<td class="common-td blank">商品编码：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo"/></td>
									<td class="common-td blank">商品名称：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName"/></td>
								</tr>
						</table>
				</form>		
			</div>
		</div>   
	         <#-- 数据列表div -->
	        <div data-options="region:'center',border:false">
				     <@p.datagrid id="dataGridJG" name="" loadUrl="" saveUrl="" defaultColumn=""  
					 		isHasToolBar="false"  columnsJsonList=""  divToolbar=""  onClickRowEdit="false" singleSelect="true" pageSize='20'   
							pagination="true" rownumbers="true"  jsonExtend="{}" height="500" emptyMsg=""  showFooter="true"
					 /> 
				       
	        </div>
	   </div>     
</div>
</body>
</html>