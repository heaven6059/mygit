<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>品质转换</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="<@s.url "/resources/js/billconqualitychange/billconqualitychange.js"/>"></script>
    <script type="text/javascript" src="<@s.url "/resources/common/other-lib/common.js"/>"></script>
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
<div>
	<#-- 工具菜单div -->	
    <div style="margin-bottom:0px" id="toolDiv">
       <@p.toolbar   listData=[
				         {"id":"btn-close","title":"关闭","iconCls":"btn-close","action":"closeWindow('品质转换')","type":0}
	                ]
				  />
	 </div>
	<#-- 条件查询区域div -->		
	<div style="background:#F4F4F4;padding:0px;">
	  	   <@p.queryConditionDiv id="queryConditionDiv" moduleFlag="21010020"
	  	    queryGridId="dataGridJG"  queryDataUrl="" collapsed="true" height="120" />
	</div>

		<div id="locSearchDiv" style="padding:10px;">
       		<form name="searchForm" id="searchForm" method="post">
			<table>
					<tr>
					<td>单据编号：</td>
					<td><input class="easyui-validatebox" style="width:100px" name="changeNo"/></td>
					<td style="padding-left:10px">
						<a id="searchBtn" href="javascript:billconqualitychange.searchArea();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</td>
					</tr>
			</table>
			</form>
		 </div>
  	 	<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn=""   title="品质转换"
              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"    pagination="true"
	           rownumbers="true"  height="300" singleSelect = "false"
	           columnsJsonList="[
					{field : 'ownerName',title : '委托业主',width : 120},
					{field : 'changeNo',title : '单据编号',width : 180},
					{field : 'sendFlag',title : '传输标示',width : 100,formatter:billconqualitychange.flagFormatter}
	                 ]" 
		           jsonExtend='{onSelect:function(rowIndex, rowData){
                           billconqualitychange.detail(rowData);
                   },onDblClickRow:function(rowIndex, rowData){
                   	    //billconqualitychange.detail(rowData);
                   }}'/>
	   	
        <@p.datagrid id="dataGridJG_detail"  loadUrl="" saveUrl=""   defaultColumn="" 
              isHasToolBar="false" onClickRowEdit="false"    pagination="true"
	           rownumbers="true"  height="300" onClickRowEdit="true" singleSelect="true"
	      />
</div>
</body>
</html>