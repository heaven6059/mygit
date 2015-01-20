<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>分货交接</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/billomrecheckjoin.js?version=1.0.6.1"></script>
    
    <link rel="stylesheet" type="text/css" href="<@s.url "/resources/css/styles/billomrecheckjoin.css"/>" />
</head>

<body class="easyui-layout">
	<!--分页标签-->
    <div data-options="region:'center',border:false">
		<div id="tt" class="easyui-tabs" data-options="tabPosition:'top',fit:true">
	<!--分页标签-->
			<#-- 分货复核交接 BEGIN -->
			<div id="check" title="分货复核交接" style="padding:0px" >
				<div class="easyui-layout" data-options="fit:true" >
					<#-- 工具菜单start -->
					<div data-options="region:'north',border:false" class="toolbar-region">
				    	<@p.toolbar id="toolbar1"   listData=[
							{"id":"searchBtn","title":"查询","iconCls":"icon-search","action":"billomrecheckjoin.searchNoChecked();", "type":0},
					        {"id":"clearBtn","title":"清除","iconCls":"icon-remove","action":"billomrecheckjoin.searchClearNochecked();", "type":0},
							{"id":"okBtn","title":"确认","iconCls":"icon-ok","action":"billomrecheckjoin.sendCheck();","type":4}
						]/>
		        	</div>
		            <#-- 工具菜单end -->
		            <#--查询start-->
		            <div data-options="region:'center',border:false">
		     			<div class="easyui-layout" data-options="fit:true" id="subLayout">
			            	<!--搜索start-->
							<div  data-options="region:'north',border:false" >
								<form id="noCheckSearchForm" name="noCheckSearchForm" method="post" class="city-form">
									<table>
										<tr>
									 		<td class="common-td blank">复核单号：</td>
											<td><input class="easyui-validatebox ipt" name="recheckNo"  id="searchRecheckNo" style="width:120px" /><input type="button" onclick="billomrecheckjoin.showSearchDialog('check')" value="..."></td>
											<td class="common-td blank">箱&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
											<td><input class="easyui-validatebox ipt" name="labelNo" style="width:120px" /></td>
											<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
					             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
											<td class="common-td blank">所属品牌：</td>
                     						<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
										</tr>
										<tr>
											<td class="common-td blank">复核日期：</td>
											<td><input class="easyui-datebox ipt" style="width:120px" name="recheckStart" id="recheckStart" /></td>
											<td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
											<td><input class="easyui-datebox ipt" style="width:120px" name="recheckEnd"  id="recheckEnd"/></td>
											<td class="common-td blank"> 复核时间：</td>
											<td><input class="easyui-timespinner ipt" data-options="showSeconds:true" style="width:120px" name="recheckTimeStart" id="recheckTimeStart"/></td>
											<td class="common-line">&nbsp;&nbsp;&mdash;&nbsp;&nbsp;</td>
											<td><input class="easyui-timespinner ipt" data-options="showSeconds:true" style="width:120px" name="recheckTimeEnd"  id="recheckTimeEnd"/></td>

											<td></td>
										</tr>
										<tr>
									 		<td class="common-td blank">来源单号：</td>
											<td><input class="easyui-validatebox ipt" name="divideNoStr"  id="searchDivideNo" style="width:120px" /><input type="button" onclick="billomrecheckjoin.showSourceDialog()" value="..."></td>
											<td class="common-td blank">客&nbsp;&nbsp;&nbsp;&nbsp;户：</td>
											<td><input class="easyui-validatebox ipt" name="storeNo" style="width:120px" /></td>
										</tr>
									</table>
								</form>		
							</div>
		                   	<!--显示列表-->
							<div data-options="region:'center',border:false">
								<@p.datagrid id="dataGridJG_NoChecked"  loadUrl="" saveUrl=""   defaultColumn=""
									isHasToolBar="false" onClickRowEdit="false"    pagination="true" title="未分货交接明细"
									rownumbers="true"  singleSelect = "false" height="375" emptyMsg="" showFooter="true"
									columnsJsonList="[
										{field : ' ',checkbox:true},
										{field : 'STATUS',title :'状态',width : 120,align:'left'},
										{field : 'RECHECK_NO',title : '复核单号',width :180},
										{field : 'LABELNO',title : '箱号',width :150,align:'left'},
										{field : 'STORE_NO',title :'客户编码',width : 100,align:'left'},
										{field : 'STORE_NAME',title :'客户',width : 170,align:'left'},
										{field : 'STORESTATUS',title :'客户状态',width : 100,align:'left',formatter:billomrecheckjoin.storeStatusFormatter},
										{field : 'REALQTYCOUNT',title :'数量',width : 80,align:'right'}
									]"
								/>
							</div>
		       			</div>     
		       		</div>
		  		</div>
		     	<#--查询end-->        
	   		</div> 
			<#-- 分货单号选择 END -->
			<#-- 分货交接明细 BEGIN -->
			<div id="checked" title="集货明细" style="padding:0px" >
				<div class="easyui-layout" data-options="fit:true" >
					<#-- 工具菜单start -->
					<div data-options="region:'north',border:false" class="toolbar-region">
						<@p.toolbar id="toolbar2"   listData=[
							{"title":"查询","iconCls":"icon-search","action":"billomrecheckjoin.searchChecked();", "type":0},
					    	{"title":"清除","iconCls":"icon-remove","action":"billomrecheckjoin.searchClearchecked();", "type":0}							
						]/>
		           	</div>
		           	<#-- 工具菜单end -->
		           	<#--查询start-->
		            <div data-options="region:'center',border:false">
		            	<div class="easyui-layout" data-options="fit:true" id="subLayout">
			           		<!--搜索start-->
							<div  data-options="region:'north',border:false" >
								<form id="checkedForm" name="checkedForm" method="post" class="city-form">
									<table>
							        	<tr>
							            	<td class="common-td blank">复核单号：</td>
							                <td><input class="easyui-validatebox ipt" name="recheckNo" id="searchRecheckedNo" style="width:120px" /><input type="button" value="..." onclick="billomrecheckjoin.showSearchDialog('checked')"></td>
							                <td class="common-td blank">箱&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
							                <td><input class="easyui-validatebox ipt" name="labelNo" style="width:120px" /></td>
			                                <td class="common-td blank">集货时间：</td>
		             						<td><input class="easyui-datebox" style="width:120px" name="startEdittm" id="searchStartEdittm" /></td>
		             						<td class="common-line" >&mdash;</td>
		             						<td><input class="easyui-datebox" style="width:120px" name="endEdittm" id="searchEndEdittm" 
												data-options="validType:['vCheckDateRange[\'#startCreatetmCondition\',\'结束日期不能小于开始日期\']']"/></td>
							        	</tr>
							        	<tr>
							            	<td class="common-td blank">商品编号：</td>
							                <td><input class="easyui-validatebox ipt" name="itemNo" style="width:120px" /></td>
							        		<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
					             			<td>
					             				<input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch2"/>
					             			</td>
							        		<td class="common-td blank">所属品牌：</td>
                     						<td colspan="3"><input class="easyui-combobox ipt" style="width:254px" name="brandNo" id="brandNoIt" /></td>
							        	</tr>
							        	<tr>
							            	<td class="common-td blank">装车状态：</td>
							                <td><input class="easyui-combobox ipt" style="width:120px" name="deliverStatus" id="deliverStatus" value="10"/></td>
							                <td class="common-td blank"></td>
							                <td></td>
			                                <td class="common-td blank"></td>
		             						<td></td>
		             						<td class="common-td blank" ></td>
		             						<td></td>
							        	</tr>
							        </table>
								</form>
							</div>
		                	<!--显示列表-->
							<div data-options="region:'center',border:false">
							     <@p.datagrid id="dataGridJG_checked"  loadUrl="" saveUrl=""   defaultColumn=""
							      isHasToolBar="false" onClickRowEdit="false"    pagination="true" title="集货明细"
							       rownumbers="true"  singleSelect = "false"  height="375" emptyMsg="" showFooter="true"
							       columnsJsonList="[
							       		  {field : 'recheckNo',title : '复核单号',width :180},
							       		  {field : 'scanLabelNo',title :'箱号',width : 150,align:'left'},
							              {field : 'itemNo',title :'商品编码',width : 150,align:'left'},
							              {field : 'itemName',title : '商品名称',width : 180,align:'left'},
							              {field : 'colorName',title : '颜色',width : 80,align:'left'},
							              {field : 'sizeNo',title : '尺码',width : 80,align:'left'},
							              {field : 'realQty',title : '数量',width : 80,align:'right'},
							              {field : 'joinName',title : '集货人',width : 80,align:'left'},
							              {field : 'joinnamech',title : '集货人名称',width : 80,align:'left'},
							              {field : 'statusName',title : '装车状态',width : 80,align:'left'},
							              {field : 'deliverNo',title : '装车单号',width : 150},
							              {field : 'joinDate',title : '集货时间',width : 130}
							             ]" 
							           />
							</div>
						</div>
		         	</div>
		         	<#--查询end--> 
		     	
		 		</div>        
			</div>
			<#-- 分货交接明细 END -->
		</div>
	</div>
	
	<#-- 商品选择div -->
		<div id="showDialog"  class="easyui-dialog" title="选择复核单"  
		    	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    	maximized:true,minimizable:false,maximizable:false"> 
			<div class="easyui-layout" data-options="fit:true">
				<#--查询start-->
				<div data-options="region:'north',border:false" >
					<@p.toolbar id="itemtoolbar"   listData=[
			    		{"title":"查询","iconCls":"icon-search","action":"billomrecheckjoin.searchRecheckNo();", "type":0},
	       				{"title":"清除","iconCls":"icon-remove","action":"billomrecheckjoin.searchClear();", "type":0},
	        			{"title":"确定","iconCls":"icon-ok","action":"billomrecheckjoin.searchAdd();", "type":0},
			    		{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billomrecheckjoin.closeWindow('showDialog');","type":0}
			        ]/>
					<div nowrap class="search-div" style="padding:10px;">
						<form id="searchDialog" class="city-form">
							<input type="hidden" name="status" id="searchStatus">
							<table>
								<tr>
									<td>箱&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="labelNo" id="labelNo" /></td>
									<td style="padding-left:10px;">商品编码：</td>
									<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNo" /></td>
									<td style="padding-left:10px;">复核日期：</td>
									<td><input class="easyui-datebox ipt" style="width:120px" name="recheckDateStart" id="recheckDateStart" /></td>
									<td>&nbsp;&nbsp;-&nbsp;&nbsp;</td>
									<td><input class="easyui-datebox ipt" style="width:120px" name="recheckDateEnd"  id="recheckDateEnd"/></td>
								</tr>
								
							</table>
						</form>
					</div>
				</div>
				<#--查询end-->
				<#--显示列表start-->
				<div data-options="region:'center',border:false">
					<@p.datagrid id="dataGridJG_Search"  loadUrl="" saveUrl=""   defaultColumn=""
				      isHasToolBar="false" onClickRowEdit="false"    pagination="true" emptyMsg=""
				       rownumbers="true"  singleSelect = "true"  height="340" title="复核单列表"
				       columnsJsonList="[
				       		  {field : ' ',checkbox:true},
				       		  {field : 'RECHECK_NO',title : '复核单号',width :180},
				       		  {field : 'STATUS',title :'状态',width : 100,formatter:billomrecheckjoin.statusSearchFormatter,align:'left'},
				              {field : 'CONTAINERNOCOUNT',title :'总箱数',width : 80,align:'right'},
				              {field : 'REALQTYCOUNT',title : '总件数量',width : 80,align:'right'}
				             ]" 
				           />
				</div>
				<#--显示列表end-->
			</div>			
		</div>
		<#-- 商品选择div -->
	<#-- 来源单选择div -->
		<div id="sourceDialog"  class="easyui-dialog" title="选择来源单"  
		    	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    	maximized:true,minimizable:false,maximizable:false"> 
			<div class="easyui-layout" data-options="fit:true">
				<#--查询start-->
				<div data-options="region:'north',border:false" >
					<@p.toolbar id="sourcetoolbar"   listData=[
			    		{"title":"查询","iconCls":"icon-search","action":"billomrecheckjoin.search4Source();", "type":0},
	       				{"title":"清除","iconCls":"icon-remove","action":"billomrecheckjoin.formClear('sourceForm');", "type":0},
	        			{"title":"确定","iconCls":"icon-ok","action":"billomrecheckjoin.searchAdd4Source();", "type":0},
			    		{"title":"取消","iconCls":"icon-cancel","action":"billomrecheckjoin.closeWindow('sourceDialog');","type":0}
			        ]/>
					<div nowrap class="search-div" style="padding:10px;">
						<form id="sourceForm" class="city-form">
							<table>
								<tr>
									<td><input type='hidden' id='recheck_status' name='status'/>单据类型：</td>
									<td><input class="easyui-combobox ipt" style="width:120px" name="divideType" id="divideType" /></td>
									<td style="padding-left:10px;">审核日期：</td>
									<td><input class="easyui-datebox ipt" style="width:120px" name="audittmStart" id="audittmStart" /></td>
									<td>&nbsp;&nbsp;-&nbsp;&nbsp;</td>
									<td><input class="easyui-datebox ipt" style="width:120px" name="audittmEnd"  id="audittmEnd"/></td>
								</tr>
								
							</table>
						</form>
					</div>
				</div>
				<#--查询end-->
				<#--显示列表start-->
				<div data-options="region:'center',border:false">
					<@p.datagrid id="dataGridJG_Source"  loadUrl="" saveUrl=""   defaultColumn=""
				      isHasToolBar="false" onClickRowEdit="false"    pagination="true" emptyMsg=""
				       rownumbers="true"  singleSelect = "true"  height="340" title="复核单列表"
				       columnsJsonList="[
				       		  {field : ' ',checkbox:true},
				       		  {field : 'divideNo',title : '来源单号',width :180},
				       		  {field : 'divideTypeName',title :'单据类型',width : 100},
				              {field : 'qty',title :'数量',width : 80,align:'right'},
				              {field : 'audittm',title : '审核时间',width : 140}
				             ]" 
				           />
				</div>
				<#--显示列表end-->
			</div>			
		</div>
		<#-- 来源单选择div -->
</body>
</html>