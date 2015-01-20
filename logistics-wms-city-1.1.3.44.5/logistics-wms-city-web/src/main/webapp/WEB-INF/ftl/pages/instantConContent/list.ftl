<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>即时库存查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/instantConContent/instantConContent.js?version=1.0.5.2"></script>
</head>
<body  class="easyui-layout">


<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<@p.toolbar id="toolBarOne" listData=[
			{"title":"查询","iconCls":"icon-search","action":"instantConContent.searchContent();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"instantConContent.searchClear('searchForm');","type":0},
			{"id":"btn-export","title":"导出","iconCls":"icon-export","action":"instantConContent.do_export()","type":5},
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
							<td colspan='5'><input class="easyui-combobox ipt" style="width:476px" name="brandNo" id="brandNoCondition"/></td>
							
						</tr>
						<tr>
							<td class="common-td blank">商品编码：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>
							<td class="common-td blank">大类一：</td>
			    			<td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
			    			<td class="common-td blank">大类二：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
							<td class="common-td blank">大类三：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
							
					    </tr>
					    <tr>
							<td class="common-td blank">商品名称：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemNameCondition" /></td>
							<td class="common-td blank">年份：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="yearsName" id="yearsNameCondition" /></td>
							<td class="common-td blank">季节：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="seasonName" id="seasonNameCondition" /></td>
							<td class="common-td blank">性别：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="genderName" id="genderNameCondition" /></td>
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