<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>储位维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/cmdefcell.js?version=1.1.1.2"></script>
	<!--object需放在head结尾会截断jquery的html函数获取内容-->
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
	</object>
</head>
<body class="easyui-layout">
	<#-- 工具菜单start -->
	<div data-options="region:'north',border:false" class="toolbar-region" id="toolDiv">
		<@p.toolbar id="toolbar"  listData=[
			{"title":"查询","iconCls":"icon-search","action":"cmdefcell.searchCell();", "type":0},
	        {"title":"清除","iconCls":"icon-remove","action":"cmdefcell.searchClear();", "type":0}, 		
	        {"title":"新增","iconCls":"icon-add","action":"cmdefcell.addUI();", "type":1},
	        {"title":"修改","iconCls":"icon-edit","action":"cmdefcell.editUI();","type":2},
	        {"title":"禁用储位","iconCls":"icon-edit","action":"cmdefcell.disableCell();","type":2},
	        {"title":"储位解禁","iconCls":"icon-edit","action":"cmdefcell.enableCell();","type":2},
	        {"title":"删除","iconCls":"icon-del","action":"cmdefcell.deleteRows();","type":3},
	        {"id":"printCell","title":"打印预览","iconCls":"icon-print","action":"cmdefcell.printCell()","type":2},
	        {"title":"全部打印","iconCls":"icon-print","action":"cmdefcell.printCellBySearch()","type":4},
	        {"title":"模板下载","iconCls":"icon-download","action":"cmdefcell.downloadTemp();","type":0},
	        {"title":"导入","iconCls":"icon-import","action":"cmdefcell.importConToItem();","type":0},
	        {"id":"btn-export","title":"导出","iconCls":"icon-export","action":"cmdefcell.exportExcel()","type":5},
			{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('');","type":0}
	   	]/>	
	</div>
	<#-- 工具菜单end -->
	<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="searchDiv" class="search-div" style="padding:10px;">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
				        	<tr>
				        		<td class="common-td">储位编码：</td>
				                <td><input class="easyui-validatebox ipt" style="width:120px" name="cellNoFuzzy" id="cellNoCondition" /></td>
				            	<td class="common-td blank">仓区编码：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNoCondition" /></td>
				                <td class="common-td blank">库区编码：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="areaNo" id="areaNoCondition" /></td>
				                <td class="common-td blank">通道编码：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="stockNo" id="stockNoCondition" /></td>
				        	</tr>
				        	<tr>
				                <td class="common-td">试算标示：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="bPick" id="bPickCondition" /></td>
				               <td class="common-td blank">混载标志：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="mixFlag" id="mixFlagCondition" /></td>
				                <td class="common-td blank" >商品类型：</td>
								<td><input class="easyui-combobox" style="width:120px" name="itemType" id="itemTypeCondition" /></td>
				                <td class="common-td blank">商品品质：</td>
								<td><input class="easyui-combobox" style="width:120px" name="areaQuality" id="areaQualityCondition"/></td>
				        	</tr>
				        	<tr>
				                <td class="common-td">储位状态：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="cellStatus" id="cellStatusCondition" /></td>
				               <td class="common-td blank">盘点状态：</td>
				                <td><input class="easyui-combobox" style="width:120px" name="checkStatus" id="checkStatusCondition" /></td>
				                <td class="common-td blank" ></td>
								<td></td>
				                <td class="common-td"></td>
								<td></td>
				        	</tr>
				        </table>
					</form>
				</div>
        	</div>
      		<#--查询end-->
			<#--显示列表start-->
        	<div data-options="region:'center',border:false">
	    		<@p.datagrid id="dataGridJG"  loadUrl="/cm_defcell/list.json?locno=${session_user.locNo}"  saveUrl=""  defaultColumn=""  title="储位维护"
		   			isHasToolBar="false" divToolbar="" height="420"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			   		rownumbers="true" emptyMsg=""
			  		columnsJsonList="[
			           		{field : 'id',checkbox:true},
			           		{field : 'cellNo',title : '储位编码',width : 100,align:'left'},
			           		{field : 'ownerName',title : '货主',width : 80,align:'left'},
			                {field : 'wareNo',title : '仓区编码',width : 60,align:'left'},
			                {field : 'wareName',title : '仓区名称',width : 180,align:'left'},
			                {field : 'areaNo',title : '库区编码',width : 60,align:'left'},
			                {field : 'areaName',title : '库区名称',width : 180,align:'left'},
			                {field : 'stockNo',title : '通道编码',width : 60,align:'left'},
			                {field : 'showItemType',title : '商品类型   ',width : 80,align:'left'},
			                {field : 'showAreaQuality',title : '商品品质',width : 80,align:'left'},
			                {field : 'showCellStatus',title : '储位状态',width : 80,sortable:true,align:'left'},
				          	{field : 'showCheckStatus',title : '盘点状态',width : 80,align:'left'},
			                {field : 'stockX',title : '储格列 ',width : 60,align:'left'},
			                {field : 'stockY',title : '储格层',width : 60,align:'left'},
			                {field : 'bayX',title : '储格位',width : 60,align:'left'},
			                {field : 'maxCase',title : '最大箱数',width : 65,align:'right'},
			                {field : 'maxQty',title : '最大板数',width : 65,align:'right'},
			                {field : 'maxWeight',title : '最大重量',width : 65,align:'right'},
			                {field : 'maxVolume',title : '最大材积 ',width : 65,align:'right'},
				          	{field : 'creator',title : '创建人',width : 80,align:'left'},
				          	{field : 'creatorName',title : '创建人名称',width : 80,align:'left'},
							{field : 'createtm',title : '创建时间',width : 125,sortable:true},
							{field : 'editor',title : '修改人',width : 80,align:'left'},
							{field : 'editorName',title : '修改人名称',width : 80,align:'left'},
							{field : 'edittm',title : '修改时间',width : 125,sortable:true},
							{field : 'length',hidden:true},
							{field : 'width',hidden:true},
							{field : 'height',hidden:true},
							{field : 'volume',hidden:true}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                   	  cmdefcell.dtlView(rowData,"view");
		      		}}'
		  		/>
			</div>
        	<#--显示列表end-->
	    </div>
	</div>  
	<#-- 主表end -->
	<#-- 修改页面 BEGIN -->
	<div id="openUI"  class="easyui-dialog" title="详情"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
			</div>
			<#--start-->
			<div data-options="region:'center',border:false" style="padding:10px;">
				<form name="dataForm" id="dataForm" method="post" class="city-form">
					<div  nowrap style="width:760px" align="center">
						<#--显示列表start-->
						<table>
								<tr>
									<td>
										<div style="float:left;width:55%">
											<table>
												<tr>
													<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
													<td><input class="easyui-combobox" style="width:130px" name="ownerNo" id="ownerNo" data-options="required:true,editable:false" /></td>
													<td class="common-td blank">仓&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
													<td><input class="easyui-combobox" style="width:130px" name="wareNo" id="wareNo" data-options="required:true" /></td>
												</tr>
												<tr>
													<td class="common-td blank">库&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
													<td><input class="easyui-combobox" style="width:130px" name="areaNo" id="areaNo" data-options="required:true" /></td>
													<td class="common-td blank">通&nbsp;&nbsp;&nbsp;&nbsp;道：</td>
													<td><input class="easyui-combobox" style="width:130px" name="stockNo" id="stockNo" data-options="required:true" /></td>
												</tr>
												<tr>
													<td class="common-td blank">储位编码：</td>
													<td><input class="easyui-validatebox ipt" style="width:130px" name="cellNo" id="cellNo" data-options="required:true" /></td>  
													<td class="common-td blank">储&nbsp;格&nbsp;列：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="stockX" id="stockX" data-options="required:true" /></td>
												</tr>
												<tr>
													<td class="common-td blank">储&nbsp;格&nbsp;层：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="stockY" id="stockY" data-options="required:true" /></td>
													<td class="common-td blank">储&nbsp;格&nbsp;位：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="bayX" id="bayX" data-options="required:true" /></td>
												</tr>
												<tr>
													<td class="common-td blank">商品类型：</td>
													<td><input class="easyui-combobox" style="width:130px" name="itemType" id="itemType" data-options="required:true,editable:false" /></td>
													<td class="common-td blank">商品品质：</td>
													<td><input class="easyui-combobox" style="width:130px" name="areaQuality" id="areaQualityform" data-options="required:true,editable:false" /></td>
												</tr>
												<tr>
													<td class="common-td blank">试算标示：</td>
													<td><input class="easyui-combobox" style="width:130px" name="bPick" id="bPick" data-options="required:true,editable:false" /></td>
													<td class="common-td blank">混载标志：</td>
													<td><input class="easyui-combobox" style="width:130px" name="mixFlag" id="mixFlag" data-options="required:true,editable:false" /></td>
												</tr>
												<tr>
													<td class="common-td blank">储位状态：</td>
													<td><input class="easyui-combobox" style="width:130px" name="cellStatus" id="cellStatus" data-options="required:true,editable:false" /></td>
													<td class="common-td blank">盘点状态：</td>
													<td><input class="easyui-combobox" style="width:130px" name="checkStatus" id="checkStatus" data-options="required:true,editable:false" /></td>
												</tr>
												<tr>
													<td class="common-td blank">最大箱数：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="maxCase" id="maxCase" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']"  /></td>
													<td class="common-td blank">最大板数：</td>
												  	<td><input class="easyui-numberbox ipt" style="width:130px" name="maxQty" id="maxQty" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']"  /></td>
												</tr>
												<tr>
													<td class="common-td blank">最大重量：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="maxWeight" id="maxWeight" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']" /></td>
													<td class="common-td blank">最大材积：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="maxVolume" id="maxVolume" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']" /></td>
												</tr>
												<tr>
													<td class="common-td blank">长(cm)：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="length" id="length_edit" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']"  /></td>
													<td class="common-td blank">宽(cm)：</td>
												  	<td><input class="easyui-numberbox ipt" style="width:130px" name="width" id="width_edit" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']"  /></td>
												</tr>
												<tr>
													<td class="common-td blank">高(cm)：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="height" id="height_edit" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']" /></td>
													<td class="common-td blank">容积(m³)：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="volume" id="volume_edit" data-options="precision:3,validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999999999,\'只能输入0~999999999999的数字范围\']']" /></td>
												</tr>
											</table>
										</div>
										<div style="float:left;width:45%">
											<table>
												<tr>
													<td width='120px' class="common-td blank">限制比率(%)：</td>
													<td colspan="2"><input class="easyui-numberbox ipt" style="width:120px" name="limitRate" id="limitRate"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,100,\'只能输入0~100的数字范围\']']" />&nbsp;&nbsp;%</td>
												</tr>
												
												<tr>
													<td width='120px' class="common-td blank">限制入库类型：</td>
													<td width='120px'><input type="radio" value="0" name="limitType" id="limitType_def">标准堆叠比率数</td>
													<td width='120px'><input type="radio" value="1" name="limitType">件数或箱</td>
												</tr>
												<tr>
													<td width='120px' class="common-td blank">是否A类库区：</td>
													<td width='120px'><input type="radio" value="1" name="aFlag" id="aFlag_def">A类库区</td>
													<td width='120px'><input type="radio" value="0" name="aFlag">非A类库区</td>
												</tr>
												<tr>
													<td width='120px' class="common-td blank">是否允许拣货：</td>
													<td width='120px'><input type="radio" value="1" name="pickFlag" id="pickFlag_def">允许</td>
													<td width='120px'><input type="radio" value="0" name="pickFlag">不允许</td>
												</tr>	
												<tr>
													<td width='120px' class="common-td blank">供应商混载标志：</td>
													<td width='120px'><input type="radio" value="1" name="mixSupplier" id="mixSupplier_def">是</td>
													<td width='120px'><input type="radio" value="0" name="mixSupplier">否</td>
												</tr>		
											</table>
										</div>
									</td>
								</tr>
								<tr>
									<td><div align="center"></br></br>
									<a id="info_save" href="javascript:cmdefcell.update();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">修改</a>
									<a id="info_close" href="javascript:cmdefcell.closeUI();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
									</div></td>
								</tr>
						</table>
						
						<#--显示列表end-->
					</div>
				</form>
			</div>
			<#--start-->
		</div>
	</div>
	<#-- 修改页面 END -->
	<#-- 新增页面 BEGIN -->
	<div id="newUI"  class="easyui-dialog" title="新增"  
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false"> 
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
			</div>
			<#--start-->
			<div data-options="region:'center',border:false" style="padding:10px;">
				<form name="newForm" id="newForm" method="post" class="city-form">
					<div  nowrap style="width:760px" align="center">
						<#--显示列表start-->
						<table>
								<tr>
									<td>
										<div style="float:left;width:55%">
											<table>
												<tr>
													<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
													<td><input class="easyui-combobox" style="width:130px" name="ownerNo" id="ownerNo_new" data-options="required:true,editable:false" /></td>
													<td class="common-td blank">仓&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
													<td><input class="easyui-combobox" style="width:130px" name="wareNo" id="wareNo_new" data-options="required:true" /></td>
												</tr>
												<tr>
													<td class="common-td blank">库&nbsp;&nbsp;&nbsp;&nbsp;区：</td>
													<td><input class="easyui-combobox" style="width:130px" name="areaNo" id="areaNo_new" data-options="required:true" /></td>
													<td class="common-td blank">通&nbsp;&nbsp;&nbsp;&nbsp;道：</td>
													<td><input class="easyui-combobox" style="width:130px" name="stockNo" id="stockNo_new" data-options="required:true" /></td>
												</tr>
												<tr>
													<td class="common-td blank">储位编码：</td>
													<td><input class="easyui-validatebox ipt" style="width:130px" name="cellNo" id="cellNo_new" data-options="required:true" /></td>  
													<td class="common-td blank">储&nbsp;格&nbsp;列：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="stockX" id="stockX_new" data-options="required:true" /></td>
												</tr>
												<tr>
													<td class="common-td blank">储&nbsp;格&nbsp;层：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="stockY" id="stockY_new" data-options="required:true" /></td>
													<td class="common-td blank">储&nbsp;格&nbsp;位：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="bayX" id="bayX_new" data-options="required:true" /></td>
												</tr>
												<tr>
													<td class="common-td blank">商品类型：</td>
													<td><input class="easyui-combobox" style="width:130px" name="itemType" id="itemType_new" data-options="required:true,editable:false" /></td>
													<td class="common-td blank">商品品质：</td>
													<td><input class="easyui-combobox" style="width:130px" name="areaQuality" id="areaQualityform_new" data-options="required:true,editable:false" /></td>
												</tr>
												<tr>
													<td class="common-td blank">试算标示：</td>
													<td><input class="easyui-combobox" style="width:130px" name="bPick" id="bPick_new" data-options="required:true,editable:false" /></td>
													<td class="common-td blank">混载标志：</td>
													<td><input class="easyui-combobox" style="width:130px" name="mixFlag" id="mixFlag_new" data-options="required:true,editable:false" /></td>
												</tr>
												<tr>
													<td class="common-td blank">储位状态：</td>
													<td><input class="easyui-combobox" style="width:130px" name="cellStatus" id="cellStatus_new" data-options="required:true,editable:false" /></td>
													<td class="common-td blank">盘点状态：</td>
													<td><input class="easyui-combobox" style="width:130px" name="checkStatus" id="checkStatus_new" data-options="required:true,editable:false" /></td>
												</tr>
												<tr>
													<td class="common-td blank">最大箱数：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="maxCase" id="maxCase_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']"  /></td>
													<td class="common-td blank">最大板数：</td>
												  	<td><input class="easyui-numberbox ipt" style="width:130px" name="maxQty" id="maxQty_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']"  /></td>
												</tr>
												<tr>
													<td class="common-td blank">最大重量：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="maxWeight" id="maxWeight_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']" /></td>
													<td class="common-td blank">最大材积：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="maxVolume" id="maxVolume_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']" /></td>
												</tr>
												<tr>
													<td class="common-td blank">长(cm)：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="length" id="length_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']"  /></td>
													<td class="common-td blank">宽(cm)：</td>
												  	<td><input class="easyui-numberbox ipt" style="width:130px" name="width" id="width_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']"  /></td>
												</tr>
												<tr>
													<td class="common-td blank">高(cm)：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="height" id="height_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']" /></td>
													<td class="common-td blank">容积(m³)：</td>
													<td><input class="easyui-numberbox ipt" style="width:130px" name="volume" id="volume_new" data-options="precision:3,validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999999999,\'只能输入0~999999999999的数字范围\']']" /></td>
												</tr>
											</table>
										</div>
										<div style="float:left;width:45%">
											<table>
												<tr>
													<td width='120px' class="common-td blank">限制比率(%)：</td>
													<td colspan="2"><input class="easyui-numberbox ipt" style="width:120px" name="limitRate" id="limitRate_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,100,\'只能输入0~100的数字范围\']']" />&nbsp;&nbsp;%</td>
												</tr>
												
												<tr>
													<td width='120px' class="common-td blank">限制入库类型：</td>
													<td width='120px'><input type="radio" value="0" name="limitType" id="limitType_def_new">标准堆叠比率数</td>
													<td width='120px'><input type="radio" value="1" name="limitType">件数或箱</td>
												</tr>
												<tr>
													<td width='120px' class="common-td blank">是否A类库区：</td>
													<td width='120px'><input type="radio" value="1" name="aFlag" id="aFlag_def_new">A类库区</td>
													<td width='120px'><input type="radio" value="0" name="aFlag">非A类库区</td>
												</tr>
												<tr>
													<td width='120px' class="common-td blank">是否允许拣货：</td>
													<td width='120px'><input type="radio" value="1" name="pickFlag" id="pickFlag_def_new">允许</td>
													<td width='120px'><input type="radio" value="0" name="pickFlag">不允许</td>
												</tr>	
												<tr>
													<td width='120px' class="common-td blank">供应商混载标志：</td>
													<td width='120px'><input type="radio" value="1" name="mixSupplier" id="mixSupplier_def_new">是</td>
													<td width='120px'><input type="radio" value="0" name="mixSupplier">否</td>
												</tr>		
											</table>
										</div>
									</td>
								</tr>
								<tr>
									<td><div align="center"></br></br>
									<a id="info_save_new" href="javascript:cmdefcell.save();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">新增</a>
									<a id="info_close_new" href="javascript:cmdefcell.closeNewUI();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
									</div></td>
								</tr>
						</table>
						
						<#--显示列表end-->
					</div>
				</form>
			</div>
			<#--start-->
		</div>
	</div>
	<#-- 新增页面 END -->
	<#-- 导入Excel div -->
<div id="importDialogView" class="easyui-window" title="导入"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closable:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<#--查询start-->
				<div data-options="region:'north',border:false" >
			    	<@p.toolbar id="itemtoolbar"   listData=[
	        			<!--{"title":"模板下载","iconCls":"icon-download","action":"cmdefcell.downloadTemp();","type":0},-->
	        			{"title":"导入","iconCls":"icon-import","action":"cmdefcell.showImportExcel('showDialog');","type":0},
	        			{"title":"保存","iconCls":"icon-ok","action":"cmdefcell.saveExcelItem();", "type":0},
	       				{"title":"清空预览数据","iconCls":"icon-remove","action":"cmdefcell.clearExcelTemp();", "type":0},
			    		{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"cmdefcell.closeWindow('importDialogView');","type":0}
			        ]/>
				</div>
				<#--查询end-->
		<div data-options="region:'center',border:false">
			<input type="hidden" name="uuId" id="uuId" />
			<@p.datagrid id="importGridJG_view"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true"
					           rownumbers="true"  onClickRowEdit="false" singleSelect="true"  title="Excel预览列表"  emptyMsg="" 
					           columnsJsonList="[
					           		{field:'wareNo',title:'仓库编码',width:80,align:'left'},	
					           		{field:'wareName',title:'仓区名称',width:100,align:'left'},		
									{field:'areaNo',title:'库存编码',width:80,align:'left'},
									{field:'areaType',title:'库存类型',width:100,align:'left'},
									{field:'areaAttribute',title:'库区属性',width:80,align:'left'},
									{field:'areaUsetype',title:'库存用途',width:120,align:'left'},	
					           		{field:'stockNo',title:'通道编码',width:80,align:'left'},		
									{field:'stockName',title:'通道名称',width:100,align:'left'},
									{field:'stockX',title:'储格列',width:60,align:'left'},
									{field:'bayX',title:'储格位',width:60,align:'left'},		
									{field:'stockY',title:'储格层',width:60,align:'left'},
									{field:'cellNo',title:'储位编码',width:100,align:'left'},
									{field:'itemType',title:'商品类型',width:70,align:'left'},
									{field:'quality',title:'商品品质',width:70,align:'left'},
									{field:'mixFlag',title:'混载标志',width:120,align:'left'}
								]"/>
			</div>
		</div>
	</div>
</div>
<#--导入Excel div -->
<#--Excel导入选择框 div -->
<div id="showImportDialog"  class="easyui-window" title="导入"
		data-options="modal:true,resizable:false,draggable:true,collapsible:false,closed:true,
		minimizable:false,maximizable:false,maximized:false" style="width:450px;" >
			<div class="easyui-layout" data-options="fit:true" style="height:100px;">
				<div data-options="region:'north',border:false">
					<form name="dataViewForm" id="importForm" method="post" class="city-form" style="padding:10px;">
						<table>
							<tr>
								<td colspan="2" style="color:red;">只允许上传后缀为.xlsx、.xls的文件</td>
							</tr>
							<tr>
								<td class="common-td">选择文件：</td>
								<td>
									<iframe src="" id="iframe" frameborder="0" height="25"></iframe>
								</td>
							</tr>
						</table>
					</form>
				</div>
</div>
<#--Excel导入选择框 div -->
</body>
</html>