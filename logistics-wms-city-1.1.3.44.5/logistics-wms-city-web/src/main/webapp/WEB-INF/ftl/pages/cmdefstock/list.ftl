<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>通道维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/cmdefstock.js?version=1.1.1.1"></script>
</head>
<body class="easyui-layout">
	<!-- 工具栏  -->
	<div data-options="region:'north',border:false" class="toolbar-region">
			<@p.toolbar id="toolbar" listData=[
				{"title":"查询","iconCls":"icon-search","action":"cmdefstock.searchCell()", "type":0},
	            {"title":"清除","iconCls":"icon-remove","action":"cmdefstock.searchClear()", "type":0},
				{"id":"btn-add","title":"新增","iconCls":"icon-add","action":"cmdefstock.addUI()","type":0},
				{"id":"btn-edit","title":"修改","iconCls":"icon-edit","action":"cmdefstock.editUI()","type":0},
				{"id":"btn-check","title":"删除","iconCls":"icon-del","action":"cmdefstock.deleteRows()","type":0},
				{"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('通道维护')","type":0}
			 ]/>
	</div>
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<!--搜索start-->
			<div  data-options="region:'north',border:false" >
				 <div class="search-div">
				 	<form name="searchForm" id="searchForm" method="post" class="city-form">
				 		<table>
                     		<tr>
                     			<td class="common-td">仓区编码：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNoCondition" /></td>
                     			<td class="common-td blank">库区编码：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="areaNo" id="areaNoCondition" /></td>
                     			<td class="common-td blank">通道编码：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="stockNo" id="stockNoCondition" /></td>
                     			<td class="common-td blank">库区类型：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="areaType" id="areaTypeCondition" /></td>
                     		</tr>
                     		<tr>
                     			<td class="common-td">试算标示：</td>
                     			<td><input class="easyui-combobox" style="width:120px" name="bPick" id="bPickCondition" /></td>
                     			<td class="common-td blank">混载标志：</td>
                     			<td colspan="5"><input class="easyui-combobox" style="width:120px" name="mixFlag" id="mixFlagCondition" /></td>
                     		</tr>
                     	</table>
				 	</form>
				 </div>
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG"  loadUrl="/cm_defstock/list.json?locno=${session_user.locNo}" saveUrl=""  defaultColumn=""  title="通道列表"
		               isHasToolBar="false" divToolbar="#searchDiv" height="420"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true"
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			                {field : 'wareNo',title : '仓区编码',width : 80,align:'left'},
			                {field : 'wareName',title : '仓区名称',width : 100,align:'left'},
			                {field : 'areaNo',title : '库区编码',width : 80,align:'left'},
			                {field : 'areaName',title : '库区名称',width : 120,align:'left'},
			                {field : 'stockNo',title : '通道编码',width : 80,align:'left'},
			                {field : 'aStockNo',title : '通道全称 ',width : 80,align:'left'},
			                {field : 'itemType',title : '商品类型   ',width : 100,align:'left',formatter:wms_city_common.columnItemTypeFormatter},
			                {field : 'areaQuality',title : '商品品质',width : 80,align:'left',formatter:wms_city_common.columnQualityFormatter},
			                {field : 'qStockX',title : '通道格数',width : 60,align:'right'},
			                {field : 'areaType',title : '库区类型',width : 100,formatter:cmdefstock.areaTypeFormatter,align:'left'},
			                {field : 'qBayX',title : '储格位数',width : 60,align:'right'},
			                {field : 'qStockY',title : '通道层数',width : 60,align:'right'},
			                {field : 'mixFlag',title : '混载标志 ',width : 100,formatter:cmdefstock.mixFlagFormatter,align:'left'},
				          	{field : 'maxQty',title : '最大板数',width : 60,align:'right'},
					        {field : 'maxWeight',title : '最大重量',width : 60,sortable:true,align:'right'},
				          	{field : 'maxVolume',title : '最大材积',width : 60,align:'right'},
				          	{field : 'maxCase',title : '最大箱数   ',width : 60,align:'right'},
				          	{field : 'sumCanuseCellno',title : '总可用储位数',width : 80,align:'right'},
					        {field : 'usedCell',title : '已占储位数  ',width : 80,align:'right'},
				          	{field : 'bPick',title : '试算标识',width : 125,formatter:cmdefstock.bPickFormatter,align:'left'}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                   	  cmdefstock.loadDetail(rowData,"view");
		   			}}'/>
			</div>
		</div>
	</div>
	<div id="openUI"  id="showDialog"  class="easyui-dialog"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
		    <div class="easyui-layout" data-options="fit:true">
		    	<div data-options="region:'north',border:false">
					<div class="search-div" style="border:none;">
		         		<form name="dataForm" id="dataForm" method="post" class="city-form">
		         			<table>
								<tr>
									<td class="common-td">货主：</td>
									<td><input class="easyui-combobox" style="width:130px" name="ownerNo" id="ownerNo" data-options="required:true"/></td>
									<td class="common-td blank">仓区：<input type="hidden" id="opt"/></td>
									<td colspan="4"><input class="easyui-combobox" style="width:130px" name="wareNo" id="wareNo" data-options="required:true"/></td>
								</tr>
								<tr>
									<td class="common-td">库区：</td>
									<td><input class="easyui-combobox" style="width:130px"  name="areaNo" id="areaNo" data-options="required:true,onChange:function(){
																																cmdefstock.selectType();
																													}"/></td>
									<td class="common-td blank">库区类型：</td>
									<td><input class="easyui-combobox" style="width:130px" name="areaType" id="areaType"    data-options="editable:false,validType:['vLength[0,32,\'最多只能输入32个字符\']']" /></td>	
									<td class="common-td blank">限制比率(%)：</td>
									<td colspan="2"><input class="easyui-numberbox ipt" style="width:130px" name="limitRate" id="limitRate" data-options="validType:['vLength[0,3,\'最多只能输入3个字符\']','vNegativeNum[0,999,\'只能输入0~999的数字范围\']']" />&nbsp;&nbsp;%</td>
								</tr>
								<tr>
									<td class="common-td">通道编码：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="stockNo" id="stockNo" data-options="required:true"/></td>
									<td class="common-td blank">通道状态：</td>
									<td><input class="easyui-combobox" style="width:130px" name="stockStatus" id="stockStatus"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']" /></td>
									<td class="common-td blank">限制入库类型：</td>
									<td><input type="radio" value="0" name="limitType">标准堆叠比率</td>
									<td><input type="radio" value="1" name="limitType">件数或箱数</td>
									
								</tr>
								<tr>
									<td class="common-td">储格位数：</td>
									<td><input class="easyui-numberbox ipt" style="width:130px" name="qBayX" id="qBayX" data-options="required:true,validType:['vLength[0,3,\'最多只能输入2个字符\']','vNegativeNum[1,9,\'只能输入1~9的数字范围\']']"/></td>
									<td class="common-td blank">通道全称：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="aStockNo" id="aStockNo" data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']"  /></td>
									<td class="common-td blank">拣货标识：</td>
									<td><input type="radio" value="1" name="areaPick">拣货区 </td>
									<td><input type="radio" value="0" name="areaPick">非拣区</td>
								</tr>
								<tr>
									<td class="common-td">通道格数：</td>
									<td><input class="easyui-numberbox ipt" style="width:130px" name="qStockX" id="qStockX" data-options="required:true,validType:['vLength[0,3,\'最多只能输入2个字符\']','vNegativeNum[1,99,\'只能输入1~99的数字范围\']']"/></td>
									<td class="common-td blank">通道层数：</td>
									<td><input class="easyui-numberbox ipt"  type="text" style="width:130px" name="qStockY" id="qStockY"  value=1 data-options="required:true,validType:['vLength[0,2,\'最多只能输入1个字符\']','vNegativeNum[1,9,\'只能输入1~9的数字范围\']']"/></td>
									<td class="common-td blank">A类库区：</td>
									<td><input type="radio" value="1" name="aFlag">A类库区 </td>
									<td><input type="radio" value="0" name="aFlag">非A类库区</td>
								</tr>
								<tr>
									<td class="common-td blank">商品类型：</td>
									<td><input class="easyui-combobox" style="width:130px" name="itemType" id="itemType" data-options="required:true,editable:false" /></td>
									<td class="common-td blank">商品品质：</td>
									<td><input class="easyui-combobox" style="width:130px" name="areaQuality" id="areaQualityform" data-options="required:true,editable:false" /></td>
								</tr>
								<tr>
									<td class="common-td">最大箱数：</td>
									<td><input class="easyui-numberbox ipt" type="text" style="width:130px" name="maxCase" id="maxCase"  value=1   data-options="validType:['vLength[0,14,\'最多只能输入13个字符\']','vNegativeNum[0,999,\'只能输入0~999的数字范围\']']"/></td>
									<td class="common-td blank">最大板数：</td>
									<td><input class="easyui-numberbox ipt" type="text" style="width:130px" name="maxQty" id="maxQty"   value=1 data-options="validType:['vLength[0,4,\'最多只能输入3个字符\']','vNegativeNum[0,999,\'只能输入0~999的数字范围\']']"/></td>
									<td class="common-td blank">允许拣货：</td>
									<td><input type="radio" value="1" name="pickFlag">允许 </td>
									<td><input type="radio" value="0" name="pickFlag">不允许</td>
								</tr>
								<tr>
									<td class="common-td">最大重量：</td>
									<td><input class="easyui-numberbox ipt" style="width:130px" name="maxWeight" id="maxWeight"    data-options="precision:5,validType:['vLength[0,14,\'最多只能输入13个字符\']','vNegativeNum[0,999,\'只能输入0~999的数字范围\']']" /></td>
									<td class="common-td blank">最大体积：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="maxVolume" id="maxVolume"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
									<td class="common-td blank">供应商混载标志：</td>
									<td><input type="radio" value="1" name="mixSupplier">是 </td>
									<td><input type="radio" value="0" name="mixSupplier">否</td>
								</tr>
								<tr>
									<td class="common-td">试算标示：</td>
									<td><input class="easyui-combobox" style="width:130px" name="bPick" id="bPick"    data-options="editable:false,validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
									<td class="common-td blank">混载标志：</td>
									<td><input class="easyui-combobox" style="width:130px" name="mixFlag" id="mixFlag"    data-options="editable:false,validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
								</tr>
								<tr>
									<td class="common-td">可用储位：</td>
									<td><input class="easyui-validatebox ipt" style="width:130px" name="sumCanuseCellno" id="sumCanuseCellno"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
									<td class="common-td blank">已占储位：</td>
									<td colspan="4"><input class="easyui-validatebox ipt" style="width:130px" name="usedCell" id="usedCell"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
								</tr>
								<tr>
									<td colspan="7" style="text-align:center;">
										<br><a id="info_save" href="javascript:cmdefstock.manage();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
										<a href="javascript:cmdefstock.closeUI();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
									</td>
								</tr>
							</table>
		         		</form>
			         </div>
		    	</div>
		    </div>
	</div>
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
						<input type="hidden" id="opt"/>
						<#--显示列表start-->
						<table>
						  <tr>
							<td class="common-td">货主：</td>
							<td><input class="easyui-combobox" style="width:130px" name="ownerNo" id="ownerNo_new" data-options="required:true"/></td>
							<td class="common-td blank">仓区：</td>
							<td colspan="4"><input class="easyui-combobox" style="width:130px" name="wareNo" id="wareNo_new" data-options="required:true"/></td>
						  </tr>
						  <tr>
						  	<td class="common-td">库区：</td>
						  	<td><input class="easyui-combobox" style="width:130px"  name="areaNo" id="areaNo_new" data-options="required:true,onChange:function(){
																																cmdefstock.selectType();
																													}"/></td>
							<td class="common-td blank">库区类型：</td>
							<td><input class="easyui-combobox" style="width:130px" name="areaType" id="areaType_new" data-options="editable:false,validType:['vLength[0,32,\'最多只能输入32个字符\']']" /></td>	
							<td class="common-td blank">限制比率(%)：</td>
							<td colspan="2"><input class="easyui-numberbox ipt" style="width:130px" name="limitRate" id="limitRate_new" data-options="validType:['vLength[0,3,\'最多只能输入3个字符\']','vNegativeNum[0,999,\'只能输入0~999的数字范围\']']" />&nbsp;&nbsp;%</td>
						  </tr>
						  <tr>
						  	<td class="common-td">通道编码：</td>
						  	<td><input class="easyui-validatebox ipt" style="width:130px" name="stockNo" id="stockNo_new" data-options="required:true"/></td>
						  	<td class="common-td blank">通道状态：</td>
						  	<td><input class="easyui-combobox" style="width:130px" name="stockStatus" id="stockStatus_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,999999,\'只能输入0~999999的数字范围\']']" /></td>
						  	<td class="common-td blank">限制入库类型：</td>
						  	<td><input type="radio" value="0" name="limitType" >标准堆叠比率</td>
						  	<td><input type="radio" value="1" name="limitType">件数或箱数</td>
						  </tr>
						  <tr>
						  	
						  	<td class="common-td">储格位数：</td>
							<td><input class="easyui-numberbox ipt" style="width:130px" name="qBayX" id="qBayX_new" data-options="required:true,validType:['vLength[0,2,\'最多只能输入1个字符\']','vNegativeNum[1,9,\'只能输入1~9的数字范围\']']"/></td>
							<td class="common-td blank">通道全称：</td>
							<td><input class="easyui-validatebox ipt" style="width:130px" name="aStockNo" id="aStockNo_new" data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']"  /></td>
							<td class="common-td blank">拣货标识：</td>
							<td><input type="radio" value="1" name="areaPick_new">拣货区 </td>
							<td><input type="radio" value="0" name="areaPick_new">非拣区</td>
						  </tr>
						  <tr>
						  	<td class="common-td">通道格数：</td>
							<td><input class="easyui-numberbox ipt" style="width:130px" name="qStockX" id="qStockX_new" data-options="required:true,validType:['vLength[0,3,\'最多只能输入2个字符\']','vNegativeNum[1,99,\'只能输入1~99的数字范围\']']"/></td>
							<td class="common-td blank">通道层数：</td>
							<td><input class="easyui-numberbox ipt"  type="text" style="width:130px" name="qStockY" id="qStockY_new"  value=1 data-options="required:true,validType:['vLength[0,2,\'最多只能输入1个字符\']','vNegativeNum[1,9,\'只能输入1~9的数字范围\']']"/></td>
							<td class="common-td blank">A类库区：</td>
							<td><input type="radio" value="1" name="aFlag" >A类库区  </td>
							<td><input type="radio" value="0" name="aFlag">非A类库区</td>
						  </tr>
						  <tr>
							<td class="common-td blank">商品类型：</td>
							<td><input class="easyui-combobox" style="width:130px" name="itemType" id="itemType_new" data-options="required:true,editable:false" /></td>
							<td class="common-td blank">商品品质：</td>
							<td><input class="easyui-combobox" style="width:130px" name="areaQuality" id="areaQualityform_new" data-options="required:true,editable:false" /></td>
						 	<td class="common-td blank">允许拣货：</td>
							<td><input type="radio" value="1" name="pickFlag" >允许 </td>
							<td><input type="radio" value="0" name="pickFlag">不允许</td>
						  </tr>
						  <tr>
							<td class="common-td">最大箱数：</td>
							<td><input class="easyui-numberbox ipt" type="text" style="width:130px" name="maxCase" id="maxCase_new" value=1   data-options="validType:['vLength[0,14,\'最多只能输入13个字符\']','vNegativeNum[0,999,\'只能输入0~999的数字范围\']']"/></td>
							<td class="common-td blank">最大板数：</td>
							<td><input class="easyui-numberbox ipt" type="text" style="width:130px" name="maxQty" id="maxQty_new" value=1 data-options="validType:['vLength[0,4,\'最多只能输入3个字符\']','vNegativeNum[0,999,\'只能输入0~999的数字范围\']']"/></td>
							<td class="common-td blank">供应商混载标志：</td>
							<td><input type="radio" value="1" name="mixSupplier" >是  </td>
							<td><input type="radio" value="0" name="mixSupplier">否</td>
						  </tr>
						  <tr>
						  	<td class="common-td">最大重量：</td>
							<td><input class="easyui-numberbox ipt" style="width:130px" name="maxWeight" id="maxWeight_new" data-options="precision:5,validType:['vLength[0,14,\'最多只能输入13个字符\']','vNegativeNum[0,999,\'只能输入0~999的数字范围\']']" /></td>
							<td class="common-td blank">最大体积：</td>
							<td colspan="4"><input class="easyui-validatebox ipt" style="width:130px" name="maxVolume" id="maxVolume_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
							
						  </tr>
						  <tr>
						  	<td class="common-td">试算标示：</td>
							<td><input class="easyui-combobox" style="width:130px" name="bPick" id="bPick_new" data-options="editable:false,validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
							<td class="common-td blank">混载标志：</td>
							<td colspan="4"><input class="easyui-combobox" style="width:130px" name="mixFlag" id="mixFlag_new" data-options="editable:false,validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
						  </tr>
						  <tr>
							<td class="common-td">可用储位：</td>
							<td><input class="easyui-validatebox ipt" style="width:130px" name="sumCanuseCellno" id="sumCanuseCellno_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
							<td class="common-td blank">已占储位：</td>
							<td colspan="4"><input class="easyui-validatebox ipt" style="width:130px" name="usedCell" id="usedCell_new" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
						  </tr>
						  <tr>
							<td colspan="7" style="text-align:center;">
								<br>
								<a id="add_save" href="javascript:cmdefstock.addManage();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">新增</a>
								<a href="javascript:cmdefstock.closeAddUI();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
							</td>
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
</body>
</html>