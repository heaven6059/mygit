<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>移库发单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/outstockdirect/planSend.js?version=1.0.5.1"></script>  
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false">
		<@p.toolbar id="main-toolbar" listData=[
			{"title":"刷新","iconCls":"icon-reload","action":"outstockdirect.reload()","type":0},
			{"title":"关闭","iconCls":"icon-close","action":"closeWindow('移库发单')","type":0}
		 ]/>
	</div>
	<div data-options="region:'center',border:false">
		<div class="easyui-layout" data-options="fit:true">
			
			<div id="cellSearch">
				<input name="cellType" type="radio" value="0" checked="checked" onClick="outstockdirect.clickCellRadio(this.value)"/>按来源库区&nbsp;&nbsp;&nbsp;&nbsp;
				<input name="cellType" type="radio" value="1" onClick="outstockdirect.clickCellRadio(this.value)"/>按目的库区&nbsp;&nbsp;&nbsp;&nbsp;
				<input name="cellType" type="radio" value="2" onClick="outstockdirect.clickCellRadio(this.value)"/>同来源目的库区
			</div>
		
			<div data-options="region:'center'" >
				<@p.datagrid id="dataGridCellArea"  loadUrl="/outstockdirect/findHmPlanCmDefareaByPage.json?locno=${session_user.locNo}&cellType=0" saveUrl=""  defaultColumn=""  
								isHasToolBar="false"  divToolbar="#cellSearch"  height="250"  onClickRowEdit="false" singleSelect="true" pageSize="10"
								rownumbers="true" 
								columnsJsonList="[
									{field : 'wareNo',hidden:true},
									{field : 'areaNo',hidden:true},
									{field : 'wareAreaNo',title : '库区编码',width : 120,align:'left'},	
									{field : 'areaName',title : '库区名称',width : 160,align:'left'}
									]" 
									jsonExtend='{onSelect:function(rowIndex, rowData){
									   outstockdirect.clickCmDefarea(rowData);
							  }}'/>
							  
							  <input id="wareNoHidden" type = "hidden" />
							  <input id="areaNoHidden" type = "hidden" />
							  <input id="ownerNoHidden" type = "hidden" />
			</div>
			<div data-options="region:'east',border:false" style="width:500px;">
				&nbsp;&nbsp;&nbsp;
							
							<br/><br/><br/><br/>
							
							<form name="dataForm" id="dataForm" metdod="post">
								&nbsp;&nbsp;&nbsp;&nbsp;
								下架人员：<input class="easyui-combobox" style="width:110px" name="roleid" id="roleid" data-options="valueField:'roleid',textField:'rolename'"/>
								&nbsp;&nbsp;&nbsp;
								<input class="easyui-combobox" style="width:110px" name="loginuser" id="loginuser" required="true" missingMessage="下架人员为必选项!"/>
								<br/><br/><br/><br/>
							</form>
							
							<label style="padding-left:10px;text-align:left">	 
								<a id="outStockBtn" href="javascript:outstockdirect.sendPlanOutstockDirect();" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">发单</a>
							</label>
			</div>
		</div>
	</div>
	<div data-options="region:'south',border:false,minSplit:true" style="height:200px" >
				<@p.datagrid id="dataGridOutStock"   loadUrl="" saveUrl=""  defaultColumn=""  title="下架指示信息列表"
				isHasToolBar="false"  divToolbar="#toolbar" height="350"  onClickRowEdit="false" singleSelect="false" pageSize="10"
				rownumbers="true"  showFooter="true"
				columnsJsonList="[
					{field : 'id',checkbox:true},
					{field:'operateDate',hidden:true},
					{field:'directSerial',hidden:true},
					{field:'expNo',title:'来源单号',width:180,align:'left'},
					{field:'itemNo',title:'商品编码',width:140,align:'left'},
					{field:'commdityName',title:'商品名称',width:160,align:'left'},
					{field:'colorName',title:'颜色',width:80,align:'left'},
					{field:'sizeNo',title:'尺码 ',width:80,align:'left'},
					{field:'sCellNo',title:'来源储位',width:100,align:'left'},
					{field:'dCellNo',title:'目的储位',width:100,align:'left'},
					{field:'itemTotalQty',title:'计划移库数量',width:90,align:'right'},
					{field:'locateTotalQty',title:'定位数量',width:100,align:'right',hidden:true}
					]" 
					jsonExtend='{}'/>
			</div>
</body>