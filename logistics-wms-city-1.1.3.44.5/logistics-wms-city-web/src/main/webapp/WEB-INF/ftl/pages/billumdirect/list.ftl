<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>退仓上架预约</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/billumdirect/billumdirect.js?version=1.0.5.2"></script>
</head>
<body class="easyui-layout">
		<div data-options="region:'north',border:false" >
			 <@p.toolbar  id='main-toolbar' listData=[
	             {"title":"查询","iconCls":"icon-search","action":"billumdirect.searchArea()","type":0},
	             {"title":"清空","iconCls":"icon-remove","action":"billumdirect.searchLocClear()","type":0},
				 {"title":"上架定位","iconCls":"icon-ok","action":"billumdirect.instockDirect();","type":1},
		         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('上架任务')","type":0}
			 ]/>
			<form name="searchForm" id="searchForm" method="post" class="city-form">
				<table>
					<tr>
						<td class="common-td blank">退仓通知单号：</td>
						<td><input class="easyui-validatebox ipt" style="width:120px" name="untreadMmNo"/></td>
						<td class="common-td blank">状态：</td>
						<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="status" id="search_status" /></td>
						<td class="common-td blank">创建日期：</td>
						<td><input class="easyui-datebox" style="width:120px" name="startTm" id="startTm"/></td>
						<td class="common-line">&mdash;</td>
						<td><input class="easyui-datebox" style="width:120px" name="endTm" id="endTm"></td>
						</tr>
					<tr>
						<td class="common-td blank">品&nbsp;牌&nbsp;库：</td>
						<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
						<td class="common-td blank">货主：</td>
						<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="ownerNo" id="search_workerNo" /></td>
						<td class="common-td blank" >退仓类型：</td>
	   					<td><input class="easyui-combobox" data-options="editable:false" name="untreadType" id="untreadType_search"  style="width:120px"/></td>
						<td class="common-td blank">品质：</td>
						<td><input class="easyui-combobox" name="quality" id="quality_search" style="width:120px;"/></td>
					</tr>
					<tr>
						<td class="common-td blank">所属品牌：</td>
             			<td  colspan="5">
             				<input class="easyui-combobox ipt" style="width:286px" name="brandNo" id="brandNo" />
             			</td>
	             	</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false" >
 			<@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn=""   title=""
              isHasToolBar="false" divToolbar="#locSearchDiv" onClickRowEdit="false"    pagination="true"
	           rownumbers="true"  singleSelect = "true" 
	           columnsJsonList="[
	           		  {field : ' ',checkbox:true},
	           		  {field : 'status',title : '状态',width : 100,align:'left',formatter:billumdirect.statusFormatter},
	           		  {field : 'untreadMmNo',title : '退仓通知单号',width :180},
	           		  {field : 'ownerNo',title : '货主',width :80,align:'left',formatter:billumdirect.ownerFormatter},
	           		  {field : 'untreadType',title : '退仓类型',width : 100,formatter:billumdirect.typeFormatter,align:'left'},
					  {field : 'quality',title : '品质',width : 100,formatter:billumdirect.qualityFormatter,align:'left'},
	                  {field : 'createtm',title : '创建日期',width : 150},
						]" 
		           jsonExtend='{onSelect:function(rowIndex, rowData){
                         //触发点击方法  调JS方法
                        
                   },onDblClickRow:function(rowIndex, rowData){
                   	      billumdirect.instockDetail();
                         billumdirect.instockDirectDetail();
                   }}'/>
		</div>
		<div data-options="region:'south',border:false,minSplit:true" style="height:250px;">
			<div id="tt" class="easyui-tabs" data-options="fit:true"> 
				<div title="未定位明细"> 
					<@p.datagrid id="dataGridJG_detail"  loadUrl="" saveUrl=""   defaultColumn=""
		              isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true" 
			           rownumbers="true"  singleSelect = "false"
			           columnsJsonList="[
			           		  {field : 'itemNo',title : '商品编号',width :150,align:'left'},
			           		  {field : 'itemName',title :'商品名称',width : 200,align:'left'},
			           		  {field : 'sizeNo',title :'尺码',width : 200,align:'left'},
			                  {field : 'estQty',title : '计划数量',width : 100,align:'right'}
			                 ]"/>
				</div> 
				<div title="已定位信息" closable="false"> 
					<div class="easyui-layout" data-options="fit:true">
						<div data-options="region:'north',border:false" >
							<div style="padding:8px;text-align:right;padding-right:20px">
								<a id="searchBtn" href="javascript:billumdirect.cancelDirect();" class="easyui-linkbutton" data-options="iconCls:'icon-del'">取消定位</a>
							</div>
						</div>
						<div data-options="region:'center',border:false" >
							<@p.datagrid id="dataGridJG_direct"  loadUrl="" saveUrl=""   defaultColumn=""
				                isHasToolBar="false" onClickRowEdit="false"    pagination="true"
					           rownumbers="true"  singleSelect = "false"  height="160" showFooter="true" 
					           columnsJsonList="[
					           		  {field : ' ',checkbox:true},
					           		  {field : 'rowId',title : '',width :80,align:'left',hidden:true},
					           		  {field : 'cellId',title : '',width :80,align:'left',hidden:true},
					           		  {field : 'untreadMmNo',title : '',width :80,align:'left',hidden:true},
					           		  {field : 'cellNo',title : '预上储位',width :80,align:'left'},
					           		  {field : 'estQty',title : '预上数量',width : 100,align:'right'},
					           		  {field : 'itemNo',title :'商品编码',width : 150,align:'left'},
					                  {field : 'itemName',title :'商品名称',width : 150,align:'left'},
					                  {field : 'sizeNo',title :'尺码',width : 200,align:'left'}
					                 ]" 
						           />
						</div>
					</div>	
				</div> 
			</div>
		</div>
</body>
</html>