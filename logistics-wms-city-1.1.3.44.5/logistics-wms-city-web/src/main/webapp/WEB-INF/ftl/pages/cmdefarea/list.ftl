<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>库区维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/cmdefarea.js?version=1.1.1.1"></script>
<style type="text/css">
.left{float:left;width:450px;}
.right{float:left;width:450px;margin-left:10px;}
 </style>
</head>
<body class="easyui-layout">
    	<div data-options="region:'north',border:false" id="toolDiv">
			<@p.toolbar id="main-toolbar"   listData=[
				 {"title":"查询","iconCls":"icon-search","action":"cmdefarea.searchArea();","type":0},
				 {"title":"清空","iconCls":"icon-remove","action":"cmdefarea.searchClear();","type":0},
				 {"title":"新增","iconCls":"icon-add","action":"cmdefarea.addUI();","type":1},
				 {"title":"修改","iconCls":"icon-edit","action":"cmdefarea.editUI()","type":2},
				 {"title":"删除","iconCls":"icon-del","action":"cmdefarea.deleteRows()","type":3},
		         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('库区管理');","type":0}
		        ]
			/>	
		</div>   
	<#-- 条件查询区域div -->	
		<div data-options="region:'center',border:false">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',border:false">
					<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
							<tr>
								<td class="common-td blank">仓区编码：</td>
								<td><input class="easyui-combobox" style="width:120px" name="wareNo" id="wareNoCondition" /></td>
								<td class="common-td blank">库区编码：</td>
								<td><input class="easyui-combobox" style="width:120px" name="areaNo" id="areaNoCondition" /></td>
								<td class="common-td blank">属性类型：</td>
								<td><input class="easyui-combobox" style="width:120px" name="attributeType" id="attributeTypeCondition" /></td>
								<td class="common-td blank">库区类型：</td>
								<td><input class="easyui-combobox" style="width:120px" name="areaType" id="areaTypeCondition" /></td>
							</tr>
							<tr>
								<td class="common-td blank" >库区属性：</td>
								<td><input class="easyui-combobox" style="width:120px" name="areaAttribute" id="areaAttributeCondition" /></td>
								<td class="common-td blank">库区用途：</td>
								<td><input class="easyui-combobox" style="width:120px" name="areaUsetype" id="areaUsetypeCondition" /></td>
								<td class="common-td blank">下架方式：</td>
								<td><input class="easyui-combobox" style="width:120px" name="oType" id="oTypeCondition" /></td>
								<td class="common-td blank">混载标志：</td>
								<td><input class="easyui-combobox" style="width:120px" name="mixFlag" id="mixFlagCondition" /></td>
							</tr>
							<tr>
								<td class="common-td blank">库区全称：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="wareAreaNo" id="wareAreaNo"/></td>
								<td class="common-td">商品品质：</td>
								<td colspan="5"><input class="easyui-combobox" style="width:120px" name="areaQuality" id="areaQuality"/></td>
							</tr>
						</table>
					</form>
				</div>
				<div data-options="region:'center',border:false">
						<@p.datagrid id="dataGridJG"  loadUrl="/cm_defarea/list.json?locno=${session_user.locNo}&sort=CONCAT(ware_no,area_no)"  saveUrl=""  defaultColumn=""  title="库区列表"
		               isHasToolBar="false" divToolbar="#searchDiv" height="468"  onClickRowEdit="false"  pagination="true" singleSelect = "false"
			           rownumbers="true"
			           columnsJsonList="[
			           		{field : 'id',checkbox:true},
			                {field : 'wareNo',title : '仓区编码',width : 60,align:'left'},
			                {field : 'wareName',title : '仓区名称',width : 120,align:'left'},
			                {field : 'areaNo',title : '库区编码',width : 80,align:'left'},
			                {field : 'areaName',title : '库区名称 ',width : 150,align:'left'},
			                {field : 'wareAreaNo',title : '库区全称 ',width : 80,align:'left'},
			                {field : 'itemType',title : '商品类型   ',width : 100,align:'left',formatter:wms_city_common.columnItemTypeFormatter},
			                {field : 'areaQuality',title : '商品品质',width : 80,align:'left',formatter:wms_city_common.columnQualityFormatter},
  							{field : 'attributeType',title : '属性类型  ',width : 80,align:'left',formatter:cmdefarea.attributeTypeFormatter},			               
			                {field : 'areaAttribute',title : '库区属性',width : 80,formatter:cmdefarea.areaAttributeFormatter},
			                {field : 'areaUsetype',title : '库区用途',width : 80,align:'left',formatter:cmdefarea.areaUsetypeFormatter},
			                {field : 'areaType',title : '库区类型',width : 80,align:'left',formatter:cmdefarea.areaTypeFormatter},
			                {field : 'oType',title : '下架方式',width : 60,formatter:cmdefarea.oTypeFormatter},
			                {field : 'mixFlag',title : '混载标志 ',width : 150,align:'left',formatter:cmdefarea.mixFlagFormatter},
				          	{field : 'maxQty',title : '最大板数',width : 60,align:'right'},
					        {field : 'stockNum',title : '通道数量',width : 60,align:'right',sortable:true},
				          	{field : 'bPick',title : '试算标识',width : 125,align:'left',formatter:cmdefarea.bPickFormatter},
				          	{field : 'creator',title : '创建人',width : 100,align:'left'},
				          	{field : 'creatorName',title : '创建人名称',width : 100,align:'left'},
				          	{field : 'createtm',title : '创建时间',width : 125,align:'left'},
				          	{field : 'editor',title : '修改人',width : 100,align:'left'},
				          	{field : 'editorName',title : '修改人名称',width : 100,align:'left'},
				          	{field : 'edittm',title : '修改时间',width : 125,align:'left'}
			            ]" 
				        jsonExtend='{onDblClickRow:function(rowIndex, rowData){
		                	  // 触发点击方法  调JS方法
		                   	  cmdefarea.loadDetail(rowData);
						}}'/>
				</div>
			</div>
		</div>
		<div id="openUI" class="easyui-window" 
			data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
			<form name="dataForm" id="dataForm" method="post" style="padding:8" class="city-form">
				<input type="hidden" id="opt"/>
				<div style="float:left;width:50%">
					<table  width="100%">
						<tr>
							<td class="common-td blank">仓区：</td>
							<td colspan="3"><input class="easyui-combobox" style="width:405px" name="wareNo" id="wareNo"  data-options="editable:false,required:true" /></td>
						</tr>
						<tr>
							<td class="common-td blank">库区编码：</td>
							<td><input class="easyui-validatebox ipt" style="width:130px" name="areaNo" id="areaNo"  data-options="required:true,validType:['vLength[0,64,\'最多只能输入64个字符\']']"   /></td>
							<td class="common-td blank">库区名称：</td>
							<td><input class="easyui-validatebox ipt" style="width:130px" name="areaName" id="areaName"    data-options="required:true,validType:['vLength[0,64,\'最多只能输入64个字符\']']"  /></td>
						</tr>
						<tr>
							<td class="common-td blank">商品类型：</td>
							<td><input class="easyui-combobox" style="width:130px" name="itemType" id="itemType"    data-options="required:true,editable:false" /></td>
							<td class="common-td blank">商品品质：</td>
							<td><input class="easyui-combobox" style="width:130px" name="areaQuality" id="areaQualityform" data-options="required:true,editable:false" /></td>
						</tr>
						<tr>
							<td class="common-td blank">库区用途：</td>
							<td><input class="easyui-combobox" style="width:130px" name="areaUsetype" id="areaUsetype"    data-options="required:true,editable:false" /></td>
							<td class="common-td blank">库区类型：</td>
							<td><input class="easyui-combobox" style="width:130px" name="areaType" id="areaType"    data-options="required:true,editable:false" /></td>
						</tr>
						<tr>
							<td class="common-td blank">库区属性：</td>
							<td><input class="easyui-combobox" style="width:130px" name="areaAttribute" id="areaAttribute" data-options="required:true,editable:false,onChange:function(data){
																																			cmdefarea.onSelectAreaAttribute(data);
																																		}" /></td>
							<td class="common-td blank">属性类型：</td>
							<td><input class="easyui-combobox" style="width:130px" name="attributeType" id="attributeType"     data-options="required:true,editable:false"  /></td>
							
						</tr>
						<tr>
							<td class="common-td blank">楼层：</td>
							<td><input class="easyui-numberbox ipt" style="width:130px" name="floor" id="floor"    data-options="required:true,validType:['vLength[0,3,\'最多只能输入3个字符\']']" /></td>
							<td class="common-td blank">最大箱数：</td>
							<td><input class="easyui-numberbox ipt" style="width:130px" name="maxCase" id="maxCase"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"  /></td>
						</tr>
						<tr>
							<td class="common-td blank">最大板数：</td>
							<td><input class="easyui-numberbox ipt" style="width:130px" name="maxQty" id="maxQty"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"  /></td>
							<td class="common-td blank">通道数量：</td>
							<td><input class="easyui-numberbox ipt" style="width:130px" name="stockNum" id="stockNum"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']" /></td>
						</tr>
						<tr>
							<td class="common-td blank">下架方式：</td>
							<td><input class="easyui-combobox" style="width:130px" name="oType" id="oType"    data-options="editable:false" /></td>
							<td class="common-td blank">混载标志：</td>
							<td><input class="easyui-combobox" style="width:130px" name="mixFlag" id="mixFlag"    data-options="editable:false" /></td>
						</tr>
						<tr>
							<td class="common-td blank">试算标示：</td>
							<td colspan="3"><input class="easyui-combobox" style="width:405px" name="bPick" id="bPick"  data-options="editable:false" /></td>
						</tr>
						<tr>
							<td class="common-td blank">备注：</td>
							<td colspan="3"><textarea class='ipt' name="areaRemark" id="areaRemark" style="height:30px;width:405px;resize: none;"></textarea></td>
						</tr>
					</table>
				</div>
				<div style="float:left;width:50%">
					<table  width="100%">
						<tr>
							<td class="common-td blank">限制比率(%)：</td>
							<td colspan="2"><input class="easyui-numberbox ipt" style="width:160px" name="limitRate" id="limitRate"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,100,\'只能输入0~100的数字范围\']']" /></td>
						</tr>
						<tr>
							<td class="common-td blank">板型出货比率：</td>
							<td colspan="2"><input class="easyui-numberbox ipt" style="width:160px" name="palOutRate" id="palOutRate"    data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']','vNegativeNum[0,100,\'只能输入0~100的数字范围\']']" /></td>
							
						</tr>
						<tr>
							<td class="common-td blank">限制入库类型：</td>
							<td><input type="radio" value="0" name="limitType">标准堆叠比率</td>
							<td><input type="radio" value="1" name="limitType">件数或箱数</td>
						</tr>
						<tr>
							<td class="common-td blank">播种标识：</td>
							<td><input type="radio" value="0" name="divideFlag">需要</td>
							<td><input type="radio" value="1" name="divideFlag">不需要</td>
						</tr>
						<tr>
							<td class="common-td blank">拣货标识：</td>
							<td><input type="radio" value="1" name="areaPick">拣货区</td>
							<td><input type="radio" value="0" name="areaPick">非拣区</td>
						</tr>	
						<tr>
							<td class="common-td blank">A类库区：</td>
							<td><input type="radio" value="1" name="aFlag">A类库区 </td>
							<td><input type="radio" value="0" name="aFlag">非A类库区</td>
						</tr>	
						<tr>
							<td class="common-td blank">允许拣货：</td>
							<td><input type="radio" value="1" name="pickFlag">允许</td>
							<td><input type="radio" value="0" name="pickFlag">不允许</td>
						</tr>
						<tr>
							<td class="common-td blank">是否做提前拣选设置：</td>
							<td><input type="radio" value="1" name="advancerPickFlag">是</td>
							<td><input type="radio" value="0" name="advancerPickFlag">否</td>
						</tr>
						<tr>
							<td class="common-td blank">供应商混载标志：</td>
							<td><input type="radio" value="1" name="mixSupplier">是</td>
							<td><input type="radio" value="0" name="mixSupplier">否</td>
						</tr>				
					</table>
				</div>
				<div style="clear:left;"></div>
			</form>
			<div style="text-align:center">
				<a id="info_save" href="javascript:cmdefarea.manage();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<a id="info_edit" href="javascript:cmdefarea.manage();" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>
				<a id="info_cancel" href="javascript:cmdefarea.closeUI();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			</div>
		</div>
</body>
</html>