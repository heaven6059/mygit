<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>日库存变动报表查询</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/datecontentreport/datecontentreport.js?version=1.0.5.2"></script>
</head>
<body  class="easyui-layout">


<div data-options="region:'north',border:false" style="margin-bottom:0px" id="toolDiv">
	<@p.toolbar id="toolBarOne" listData=[
			{"title":"查询","iconCls":"icon-search","action":"datecontentreport.searchData();","type":0},
       		{"title":"清空","iconCls":"icon-remove","action":"datecontentreport.searchClear('searchForm');","type":0}
			{"id":"btn-export","title":"导出","iconCls":"icon-export","action":"datecontentreport.do_export()","type":5},
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
							<#-- 
							<td class="common-td">商品编码：</td>
							<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" id="itemNoCondition" /></td>
							-->
							<td class="common-td">所属品牌：</td>
                     		<td colspan='3'><input class="easyui-combobox ipt" style="width:200px" name="brandNo" id="brandNo" /></td>
							<td class="common-td blank">变动日期：</td>
				            <td><input class="easyui-datebox" style="width:120px" name="startAudittm" id="startAudittmCondition" /></td>
				            <td class="common-line" width='40'>&mdash;</td>
				            <td><input class="easyui-datebox" style="width:120px" name="endAudittm" id="endAudittmCondition" /></td>
						</tr>
					</table>
				</form>
			</div>
			
	</div>			 


	<#-- 数据列表div -->
	<div data-options="region:'center',border:false">
	    <@p.datagrid id="dataGridJG"  loadUrl=""
	    	  		saveUrl=""  defaultColumn=""  title="日库存变动报表列表"
		    		isHasToolBar="false" divToolbar="" height="390"  onClickRowEdit="false"  
		    		pagination="true" singleSelect = "false"
					rownumbers="true" emptyMsg="" showFooter="true"
					columnsJsonList="[
			                {field : 'audittmStr',title : '日期',width : 120},
			                {field : 'itemNo',width : 160,align:'left',hidden:true},
			                {field : 'sizeNo',width : 80,align:'left',hidden:true},
			                {field : 'reportTypeStr',width : 80,hidden:true},
			                {field : 'jhrkQty',title : '进货入库',width : 90,align:'right'},
			                {field : 'tcrkQty',title : '退仓入库',width : 90,align:'right'},
			                {field : 'tcckQty',title : '退厂出库',width : 90,align:'right'},
			                {field : 'fhckQty',title : '发货出库',width : 90,align:'right'},
			                {field : 'qtrkQty',title : '其他入库',width : 90,align:'right'},
			                {field : 'bsckQty',title : '报损出库',width : 90,align:'right'},
			                {field : 'pyrkQty',title : '盘盈入库',width : 90,align:'right'},
			                {field : 'kpckQty',title : '盘亏出库',width : 90,align:'right'}
			            ]" 
						jsonExtend='{
		                     	url:BasePath+"/datecontentreport/listReport.json",
					           	method:"post",
		    				   	queryParams:{"locno": datecontentreport.locno,
				                 "startAudittm": datecontentreport.getDate(0),
				                 "endAudittm": datecontentreport.getDate(0)
						       	},
		                     	onDblClickRow:function(rowIndex, rowData){
				                	 
				                }
		        }'
		/>
	 </div>
 
 </div>
 </div>
</body>
</html>