<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>业务单据明细报表查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/bizbilldetailsreport/bizbilldetailsreport.js?version=1.0.0.3"></script>
</head>
<body  class="easyui-layout">

<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<@p.toolbar id="toolbar" listData=[
			{"title":"查询","iconCls":"icon-search","action":"bizbilldetailsreport.searchData();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"bizbilldetailsreport.searchClear('searchForm');","type":0}
       		{"id":"btn_exp_row","title":"导出","iconCls":"icon-export","action":"bizbilldetailsreport.doExport();","type":0},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('')","type":0}
		]
	/>
</div>


<div data-options="region:'center',border:false">
    <div class="easyui-layout" data-options="fit:true" id="subLayout">
 		<div  data-options="region:'north',border:false" >
 		
			<div id="splitdepotSearchDiv"  style="padding:10px;" class="search-div">
				<form name="searchForm" id="searchForm" method="post" class="city-form">
					<table>
						<tr>
							<td class="common-td">日期：</td>
				            <td><input class="easyui-datebox" style="width:120px" name="startDate" id="startDateCondition"/></td>
				            <td class="common-line" >&mdash;</td>
				            <td><input class="easyui-datebox" style="width:120px" name="endDate" id="endDateCondition"/></td>
							<td class="common-td blank">单据类型：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="billType" id="billTypeCondition" /></td>
							<td class="common-td blank">单据完结状态：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="finishStatus" id="finishStatusCondition" value="0" /></td>
						</tr>						
						<tr>
							<td class="common-td blank">单号：</td>
				            <td><input class="easyui-validatebox ipt" style="width:120px" name="billNo" id="billNoCondition" /></td>
							<td class="common-td blank">来源单号：</td>
				            <td><input class="easyui-validatebox ipt" style="width:120px" name="sourceBillNo" id="sourceBillNoCondition" /></td>
							<td class="common-td blank">箱号：</td>
				            <td><input class="easyui-validatebox ipt" style="width:120px" name="boxNo" id="boxNoCondition" /></td>
							<td class="common-td blank">商品品质：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="quality" id="qualityCondition" /></td>
						</tr>
						<tr>
						    <td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
                     		<td ><input class="easyui-combobox ipt" style="width:120px" name="sysNo" id="sysNoCondition"/></td>
							<td class="common-td blank">品牌：</td>
		             		<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoCondition" /></td>	
						    <td class="common-td blank">大类一：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
			             	<td class="common-td blank">大类二：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
						</tr>
						<tr>
						    <td class="common-td blank">大类三：</td>
			             	<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>
							<td class="common-td blank">年份：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="years" id="yearsCondition" /></td>
							<td class="common-td blank">季节：</td>
                     		<td><input class="easyui-combobox ipt" style="width:120px" name="season" id="seasonCondition" /></td>
                     		<td class="common-td blank">性别：</td>
				            <td><input class="easyui-combobox ipt" style="width:120px" name="gender" id="genderCondition" /></td>
						</tr>
						<tr>
							<td class="common-td blank">商品编码：</td>
				            <td clospan='7'><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>
						</tr>
					</table>
				</form>
			</div>
	</div>			 


	<#-- 数据列表div -->
	<div data-options="region:'center',border:false">
	     <@p.datagrid id="dataGridJG"  defaultColumn=""  title='查询结果列表'  
		  		isHasToolBar="false" height="525"  pageSize="20" 
				onClickRowEdit="false" onClickRowAuto="false" 
				pagination="true" rownumbers="true" showFooter="true" emptyMsg=" " 
				jsonExtend="{}" 
				columnsJsonList="[		
									{field:'createtm',title:'作业日期',width:80,align:'left'},
									{field:'brandName',title:'品牌',width:80,align:'left'},
									{field:'billNo',title:'单号',width:130,align:'left'},
									{field:'billTypeName',title:'单据类型',width:100,align:'left'},
									{field:'statusName',title:'状态',width:100,align:'left'},
									{field:'itemNo',title:'商品编码',width:140,align:'left'},
									{field:'sizeNo',title:'尺码',width:70,align:'left'},
									{field:'boxNo',title:'箱号',width:120,align:'left'},
									{field:'planQty',title:'计划数',width:80,align:'right'},
									{field:'realQty',title:'实数',width:80,align:'right'},
									{field:'sourceBillNo',title:'来源单据',width:130,align:'left'},
									{field:'sourceBillTypeName',title:'单据类型',width:100,align:'left'},
									{field:'editor',title:'更新人',width:80,align:'left'},
									{field:'editorName',title:'更新人名称',width:80,align:'left'},
									{field:'edittm',title:'更新时间',width:80,align:'left'}
								]"/>
						
	 </div>
 
 </div>
 </div>
</body>
</html>