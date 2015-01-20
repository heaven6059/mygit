<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>分货交接</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="<@s.url "/resources/js/baseinfo/billomrecheckjoin.js"/>"></script>
    <script type="text/javascript" src="<@s.url "/resources/common/other-lib/common.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<@s.url "/resources/css/styles/billomrecheckjoin.css"/>" />
</head>
<script>
  $(window).resize(function(){
  
	    $('#queryConditionDiv').panel('resize',{
		    width:document.body.clientWidth
	    });
	    
	    $('#r1').css('width',document.body.clientWidth-152);
	    $('#dataGridJG').datagrid('resize', {
	        width:document.body.clientWidth-152
	    });
	
	    $('#r2').css('width',document.body.clientWidth-150);
	    $('#toolbarEdit').panel('resize',{
		    width:document.body.clientWidth-150
	    });
  });

</script>
<body >
<div id="tt" class="easyui-tabs"> 
 	<div title="分货复核交接" name="check"> 
 		<div style="padding:8px;">
 			<form id="noCheckSearchForm">
 			<table>
 				<tr>
 					<td>复核单号</td>
 					<td><input name="recheckNo"  id="searchRecheckNo" /><input type="button" onclick="billomrecheckjoin.showSearchDialog()" value="...."></td>
 					<td style="padding-left:10px;">箱号</td>
 					<td><input name="labelNo" /></td>
 					<td style="padding-left:10px;">
 						<a id="searchBtn" href="javascript:billomrecheckjoin.searchNoChecked();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
 						<a id="clearBtn" href="javascript:billomrecheckjoin.searchClearNochecked();" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">清空</a>
 						<a id="okBtn" href="javascript:billomrecheckjoin.sendCheck();" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">交接确认</a>
 					</td>
 				</tr>
 			</table>
 			</form>
 		</div>
		<@p.datagrid id="dataGridJG_NoChecked"  loadUrl="" saveUrl=""   defaultColumn=""
      isHasToolBar="false" onClickRowEdit="false"    pagination="true"
       rownumbers="true"  singleSelect = "false" height="300"
       columnsJsonList="[
        	  {field : ' ',checkbox:true},
       		  {field : 'LABELNO',title : '箱号',width :150},
       		  {field : 'REALQTYCOUNT',title :'数量',width : 200},
              {field : 'STATUS',title :'状态',width : 150,formatter:billomrecheckjoin.statusFormatter}
             ]"/>
	</div> 
	<div title="分货交接明细" closable="false" style="overflow:auto;" name="checked"> 
	<div style="padding:8px;">
		<form id="checkedForm">
 			<table>
 				<tr>
 					<td>复核单号</td>
 					<td><input name="recheckNo" id="searchRecheckedNo"/><input type="button" value="...." onclick="billomrecheckjoin.showSearchDialog()"></td>
 					<td style="padding-left:10px;">箱号</td>
 					<td><input name="labelNo" id="searchLabelNo"/></td>
 					<td style="padding-left:10px;">商品编号</td>
 					<td><input name="itemNo" id="searchItemNo" /></td>
 					<td style="padding-left:10px;">
 						<a id="searchBtn" href="javascript:billomrecheckjoin.searchChecked();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
 						<a id="searchBtn" href="javascript:billomrecheckjoin.searchClearchecked();" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">清空</a>
 					</td>
 				</tr>
 			</table>
 			</form>
 		</div>
		<@p.datagrid id="dataGridJG_checked"  loadUrl="" saveUrl=""   defaultColumn=""
      isHasToolBar="false" onClickRowEdit="false"    pagination="true"
       rownumbers="true"  singleSelect = "false"
           />
	</div>  
</div>
<div id="showDialog" class="easyui-window"  title="查询"  
		    style="width:620px;padding:8px;"   
		    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
		    minimizable:false"> 
		<div id="dialogSearch">
			<form id="searchDialog">
				<input type="hidden" name="status" id="searchStatus">
			<table>
				<tr>
					<td>箱号</td>
					<td><input class="easyui-validatebox" style="width:110px" name="labelNo" id="labelNo" /></td>
					<td style="padding-left:10px;">商品编码</td>
					<td><input class="easyui-validatebox" style="width:110px" name="itemNo" /></td>
					<td style="padding-left:10px;">复核日期</td>
					<td><input class="easyui-datebox" style="width:110px" name="recheckDateStart" /></td>
					<td>-</td>
					<td><input class="easyui-datebox" style="width:110px" name="recheckDateEnd" /></td>
				</tr>
				<tr>
					<td colspan="8" style="text-align:right">
						<a id="searchBtn" href="javascript:billomrecheckjoin.searchRecheckNo();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<a id="clearBtn"  href="javascript:billomrecheckjoin.searchClear();" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">清除</a>
						<a id="searchBtn" href="javascript:billomrecheckjoin.searchAdd();" class="easyui-linkbutton" >确认</a>
					</td>
				</tr>
			</table>
			</form>
		</div>
		<@p.datagrid id="dataGridJG_Search"  loadUrl="" saveUrl=""   defaultColumn=""
      isHasToolBar="false" onClickRowEdit="false"    pagination="true"
       rownumbers="true"  singleSelect = "true"  height="300"
       columnsJsonList="[
       		  {field : ' ',checkbox:true},
       		  {field : 'RECHECK_NO',title : '复核单号',width :180},
       		  {field : 'STATUS',title :'状态',width : 120,formatter:billomrecheckjoin.statusSearchFormatter},
              {field : 'CONTAINERNOCOUNT',title :'总箱数',width : 150},
              {field : 'REALQTYCOUNT',title : '总件数量',width : 100}
             ]" 
           />
</div>
</body>
</html>