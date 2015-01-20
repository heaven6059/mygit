<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>客户线路关系维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="<@s.url "/resources/js/baseinfo/oslinecust.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<@s.url "/resources/css/styles/oslinecust.css"/>" />
</head>
<script>
  $(window).resize(function(){
  
	    $('#queryConditionDiv').panel('resize',{
		    width:document.body.clientWidth
	    });
	    
	    $('#dataGridJG').datagrid('resize', {
	        width:document.body.clientWidth
	    });
	
	    $('#toolbarEdit').panel('resize',{
		    width:document.body.clientWidth
	    });
  });

</script>
<body >
	<#-- 员工查询 -->
	<div class="loc_main">
		<div class="left">
			<#-- 线路查询
			<form name="searchForm" id="lineSearchForm" method="post">
				线路编码:<input id="usercode" class="easyui-validatebox" style="width:100px"   name="usercode" /> 
				线路名称：<input id="username" class="easyui-validatebox" style="width:100px" name="username"/>
				<a id="searchBtn" href="javascript:oslinecust.searchUser();"  class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			</form>
			-->
			<@p.datagrid id="dataGridJG_line"  loadUrl="/os_defline/list.json?locno=${session_user.locNo}" saveUrl=""   defaultColumn=""   title="选择线路"
		              isHasToolBar="false" divToolbar="#lineSearchForm" onClickRowEdit="false"   pagination="true"
			           rownumbers="true" 
			           columnsJsonList="[
								{field : 'lineNo',title : '线路代码',width :150},
								{field : 'lineName',title : '线路名称',width :212}
			                 ]"
				           jsonExtend='
				           {onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                         oslinecust.loadCustDataGrid(rowData);
		                   		}
		                   }'/>
		</div>
		<div class="right">
			<@p.toolbar   listData=[
                         {"id":"btn-close","title":"新增","iconCls":"icon-add","action":"oslinecust.addInfo()","type":1}
                         {"id":"btn-close","title":"修改","iconCls":"icon-edit","action":"oslinecust.editInfo()","type":2}
	              		 {"id":"btn-close","title":"删除","iconCls":"icon-remove","action":"oslinecust.del()","type":3}
	                ]
				  />
			<@p.datagrid id="dataGridJG_cust"  loadUrl="" saveUrl=""   defaultColumn=""  
		              isHasToolBar="false" divToolbar="#storeSearchDiv" onClickRowEdit="false"   pagination="true"
			           rownumbers="true"  singleSelect = "false" 
			           columnsJsonList="[
			           			{field : 'locno',checkbox:true},
								{field : 'storeNo',title : '客户编号',width :60},
								{field : 'storeName',title : '客户名称'},
								{field : 'lineSeqNo',title : '路顺',width :70},
								{field : 'distance',title : '距离',width :70},
								{field : 'charge',title : '费用',width :70},
								{field : 'tollNoArray',title : '收费站编号',width :70},
								{field : 'speedLimit',title : '汽车限速',width :70}
							]" 
				           jsonExtend='{}'/>
		</div>
		<div id="showDialog"  class="easyui-window" title="新增"  
		    style="width:620px;padding:8px;"   
		    data-options="modal:true,resizable:true,draggable:true,collapsible:false,closed:true,
		    minimizable:false"> 
		     <form name="dataForm" id="dataForm" method="post">
		     	<input type="hidden" name="lineNo" id="lineNo">
	         	<#-- 明细信息div -->
					<table border="1">
						<tr>
							<td>客户编号：</td>
							<td colspan="5">
								<span id="storeNoArea"><input class="easyui-combotree" style="width:100px" id="storeNo" required="true"/></span>
								<input type="text" style="width:150px" name="storeName" id="storeName" disabled="disabled"/>
								<input class="hide" style="width:100px" name="storeNo" id="storeNoHide" type="hidden"/>	
							</td>
						</tr>
						<tr>
							<td>路顺：</td>
							<td>
								<input class="easyui-numberbox" style="width:100px" name="lineSeqNo" id="lineSeqNo" data-options="validType:['vLength[0,38,\'最多只能输入38个字符\']']"/>
							</td>
							<td style="padding-left:15px;">距离：</td>
							<td><input class="easyui-numberbox" style="width:150px" name="distance" id="distance" data-options="validType:['vLength[0,38,\'最多只能输入38个字符\']']"/></td>
							<td style="padding-left:15px;">费用：</td>
							<td><input class="easyui-numberbox" style="width:120px" name="charge" id="charge" precision="5" max="99999999.99999"/></td>
						</tr>
						<tr>
							<td>收费站编号：</td>
							<td><input class="easyui-validatebox" style="width:100px" name="tollNoArray" id="tollNoArray"/></td>
							<td style="padding-left:15px;">汽车限速：</td>
							<td><input class="easyui-numberbox" style="width:150px" name="speedLimit" id="speedLimit" data-options="validType:['vLength[0,13,\'最多只能输入38个字符\']']"/></td>
							<td colspan="2"></td>
						</tr>
						<tr class="creatorinfo">
							<td>创建人：</td>
							<td id="creator"></td>
							<td style="padding-left:15px;">创建时间：</td>
							<td id="createtm"></td>
							<td colspan="2"></td>
						</tr>
						<tr class="creatorinfo">
							<td>修改人：</td>
							<td id="editor"></td>
							<td style="padding-left:15px;">创建时间：</td>
							<td id="edittm"></td>
							<td colspan="2"></td>
						</tr>
						<tr>
							<td colspan="6" style="text-align:center;">
								<a id="info_add" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
								<a id="info_edit" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a> 
							</td>
						</tr>
					</table>
			 </form>	
		</div>	
		<div style="clear:both"></div>
	</div>
</body>
</html>