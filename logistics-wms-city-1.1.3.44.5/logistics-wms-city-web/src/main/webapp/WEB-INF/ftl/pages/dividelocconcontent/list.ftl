<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>分仓库存明细表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/dividelocconcontent/dividelocconcontent.js?version=1.0.5.2"></script>
</head>
<body  class="easyui-layout">


<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<@p.toolbar id="toolBarOne" listData=[
			{"title":"查询","iconCls":"icon-search","action":"dividelocconcontent.searchContent();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"dividelocconcontent.searchClear('searchForm');","type":0},
       		{"title":"导出","iconCls":"icon-export","action":"dividelocconcontent.doExport();","type":0},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
		]
	/>
</div>


<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true" id="subLayout">
 		<div  data-options="region:'north',border:false" >
 		
			<div id="itemcollectcontentSearchDiv"  style="padding:10px;">
				<form name="searchForm" id="searchForm" method="post" class="city-form">
					<table>
						<tr>
							<td class="common-td">品牌库：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoCondition" required="true" data-options="editable:false"/></td>
							<td class="common-td blank">所属品牌：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoCondition"/></td>
							<td class="common-td blank">商品属性：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="itemType" id="itemTypeCondition"/></td>
							<td class="common-td blank">商品品质：</td>
			     			<td><input class="easyui-combobox" style="width:120px" name="quality" id="qualityCondition"/></td>
						</tr>
						<tr>
							<td class="common-td blank">商品编码：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>
							<td class="common-td blank">商品名称：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemNameCondition" /></td>
							<td class="common-td blank">商品大类：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="cateCode" id="cateCodeCondition"/></td>
							<td class="common-td blank">年份：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="yearsName" id="yearsNameCondition" /></td>
						</tr>
						<tr>
							<td class="common-td blank">季节：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="seasonName" id="seasonNameCondition" /></td>
							<td class="common-td blank">性别：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="genderName" id="genderNameCondition" /></td>
							<td class="common-td blank"></td>
							<td></td>
							<td class="common-td blank"></td>
							<td></td>
						</tr>
					</table>
				</form>
			</div>
			
	</div>			 


	<#-- 数据列表div -->
	<div data-options="region:'center',border:false">
	    <@p.datagrid id="dataGrid"  loadUrl="" saveUrl=""  defaultColumn=""   title="商品列表"
	       isHasToolBar="false" divToolbar="" height="462"  onClickRowEdit="false"  pagination="true"
	       rownumbers="true" showFooter="true" emptyMsg="" 
	       jsonExtend='{}'/>
	 </div>
 
 </div>
 </div>
</body>
</html>