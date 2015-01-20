<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>预分货单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billompreparedivide/billompreparedivide.js?version=1.0.0.4"></script>    
</head>
<body class="easyui-layout">
	<#-- 工具菜单div -->	
     <div data-options="region:'north',border:false">
       <@p.toolbar id="maintoolbar"  listData=[
      		 {"title":"查询","iconCls":"icon-search","action":"billompreparedivide.searchArea()","type":0},
      		 {"title":"清空","iconCls":"icon-remove","action":"billompreparedivide.searchLocClear()","type":0},
             {"title":"新增","iconCls":"icon-add","action":"billompreparedivide.addInfo()","type":1},
             {"title":"修改","iconCls":"icon-edit","action":"billompreparedivide.editInfo()","type":2},
             {"title":"删除","iconCls":"icon-remove","action":"billompreparedivide.del()","type":3},
             {"title":"审核","iconCls":"icon-aduit","action":"billompreparedivide.audit()","type":4},
             {"title":"手工关闭","iconCls":"icon-del","action":"billompreparedivide.overPrepareDivide()","type":3},
	         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('预分货单')","type":0}
	         ]
		  />
	 </div>
	 <div data-options="region:'center',border:false">
	 	<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false"><div class="search-div">
		   		<form name="searchForm" id="searchForm" method="post" class="city-form">
		   			<table>
		   				<tr>
		   					<td class="common-td blank">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="status" id="search_status" /></td>
		   					<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
		   					<td><input class="easyui-combobox" style="width:120px" data-options="editable:false" id="search_creator" name="creator"/></td>
		   					<td class="common-td blank">创建日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="createtmStart" id="createtmStart"/></td>
		   					<td class="common-line">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="createtmEnd" id="createtmEnd"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">预分货单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="receiptNo"/></td>
		   					<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
		   					<td><input class="easyui-combobox" style="width:120px" data-options="editable:false" id="search_auditor" name="auditor"/></td>
		   					<td class="common-td blank">审核日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="audittmStart"/></td>
		   					<td class="common-line">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="audittmEnd" id="audittmEnd"/></td>
		   				</tr>
		   				
		   				<tr>		   					
		   					<td class="common-td blank">预到货通知单：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="importNo"/></td>
		   					<td class="common-td">货&nbsp;&nbsp;主：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="ownerNo" id="search_ownerNo" /></td>
		   					<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
	             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		   					<td class="common-td blank">所属品牌：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="search_brandNo" /></td>
		   				</tr>
		   			</table>
				</form></div>
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG" loadUrl="" saveUrl="" defaultColumn=""   title="预分货单"
	              isHasToolBar="false"  onClickRowEdit="false"  
	               pagination="true" rownumbers="true"  singleSelect = "false"
		           columnsJsonList="[
						{field : ' ',checkbox:true},
						{field : 'status',title : '状态',width : 70,formatter:billompreparedivide.statusFormatter,align:'left'},
						{field : 'receiptNo',title : '预分货单号',width :180},
						{field : 'ownerNo',title : '货主',width : 120,formatter:billompreparedivide.ownerFormatter,align:'left'},
						{field : 'creator',title : '创建人',width : 100,align:'left'},
						{field : 'creatorName',title : '创建人名称',width : 100,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130,sortable:true},
						{field : 'auditor',title : '审核人',width : 100,align:'left'},
						{field : 'auditorName',title : '审核人名称',width : 100,align:'left'},
						{field : 'audittm',title : '审核时间',width : 130,sortable:true},
						{field : 'locno',hidden:'true'}
		                 ]" 
			           	jsonExtend='{
					   		onSelect:function(rowIndex, rowData){
	                       	 // 触发点击方法  调JS方法
	                   		},
	                   		onDblClickRow:function(rowIndex, rowData){
	                 		 billompreparedivide.showDetail(rowData);
	                   		}
	                   	}'
	        	/>
			</div>
		</div>
	</div>
<div id="detailDialog" class="easyui-window" title="预分货单明细"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id="submaintoolbar"  listData=[
      		 	{"id":"addBtn","title":"保存","iconCls":"icon-save","action":"billompreparedivide.save_main()","type":0},
      		 	{"id":"editBtn","title":"修改","iconCls":"icon-edit","action":"billompreparedivide.edit_main()","type":0},
      		 	{"title":"取消","iconCls":"icon-cancel","action":"billompreparedivide.coloseDetailDialog()","type":0}
      		 	]
		  	/>
			<form id="detailForm" name="detailForm" method="post" class="city-form">
	    		<table>
					<tr>
						<td class="common-td blank">预分货单号：</td>
						<td>
							<input class="easyui-validatebox ipt" id="receiptNo" name="receiptNo" style="width:120px;display:none"/>
							<input class="easyui-validatebox ipt" value="" style="width:120px" name="receiptNo"  id="receiptNoHide"/>
						</td>
						<td class="common-td blank">&nbsp;货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="ownerNo" id="ownerNo" style="width:120px"/>
							<input name="ownerNo" type="hidden" id="ownerNoHide"/>
						</td>
						<td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td colspan="3"><input class="easyui-validatebox ipt" name="remark" style="width:100%" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"/></td>
					</tr>	    			
	    		</table>
		    </form>
		</div>
		<div data-options="region:'center'" data-options="fit:true">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false">
					<div id="sub_con">
						<@p.toolbar id="opBtn"  listData=[
							{"title":"新增明细","iconCls":"icon-add-dtl","action":"billompreparedivide.showBoxDialog()","type":0},
			      		 	{"title":"删除明细","iconCls":"icon-del-dtl","action":"billompreparedivide.deleterow()","type":0},
			      		 	{"title":"保存明细","iconCls":"icon-save-dtl","action":"billompreparedivide.edit()","type":0}
			      		 	]
		  				/>
					</div>
					<@p.datagrid id="dataGridJG_detail"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true"
					           rownumbers="true"  onClickRowEdit="true" singleSelect="true"  title="明细列表" divToolbar="#sub_con" emptyMsg="" 
					           columnsJsonList="[
									{field:'boxNo',title:'箱号',width:120,align:'left'},
								    {field : 'cellNo',title:'储位',width:180},
									{field : 'receiptQty',title : '数量',width : 100,align:'right'}
					]"/>
				</div>
			</div>
		</div>
	</div>	    
</div>
<#-- 详情 -->
<div id="detailDialogView" class="easyui-window" title="预分货单明细"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="padding:5px;"><div class="search-div">
	    	<form id="detailForm_view" name="detailForm" method="post" class="city-form">
	    		<table>
					<tr>
						<td class="common-td blank">预分货单号：</td>
						<td>
							<input class="easyui-validatebox ipt" name="receiptNo" style="width:120px;"  readOnly='readOnly'/>
						</td>
						<td class="common-td blank">&nbsp;货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="ownerNo" id="ownerNo_view" style="width:120px"/>
						</td>
						<td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td colspan="3"><input class="easyui-validatebox ipt" name="remark" style="width: 100%;" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"/></td>						
					</tr>		    			
	    		</table>
	    	</form></div>
		</div>
		<div data-options="region:'center'">
			<@p.datagrid id="dataGridJG_detail_view"  loadUrl="" saveUrl=""   defaultColumn="" 
	              isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true"
		           rownumbers="true"  height="200" onClickRowEdit="true" singleSelect="true"
		           columnsJsonList="[
						{field:'importNo',title:'预到货通知单',width:150,align:'left'},
						{field:'checkNo',title:'验收单号',width:150,align:'left'},
						{field:'itemNo',title:'商品编码',width:150,align:'left'},
						{field:'itemName',title:'商品名称',width:150,align:'left'},
						{field:'colorName',title:'颜色',width:80,align:'left'},
						{field:'sizeNo',title:'尺码',width:50,align:'left'},
						{field:'boxNo',title:'箱号',width:120,align:'left'},
						{field:'cellNo',title:'储位',width:100,align:'left'},
						{field :'brandName',title :'品牌',width : 70,align:'left'},
						{field:'receiptQty',title:'预分货数量',width:100,align:'right'},
						{field:'divideQty',title:'分货数量',width:100,align:'right'}
		                 ]"
		    />
		</div>
	</div>
</div>
<#-- 箱号选择 -->	
<div id="boxDialog" class="easyui-window"  title="箱号选择"  style="width:700px;"
		    data-options="modal:true,resizable:true,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">	
			<@p.toolbar id="box-toolbar"  listData=[
					{"title":"查询","iconCls":"icon-search","action":"billompreparedivide.searchBox()","type":0},
	      		 	{"title":"清空","iconCls":"icon-remove","action":"billompreparedivide.clearSearchBox()","type":0},
	      		 	{"title":"发货通知单","iconCls":"icon-aduit","action":"billompreparedivide.selectExpNo();", "type":0},
	      		 	{"title":"确定","iconCls":"icon-ok","action":"billompreparedivide.batchSelectBoxOk()","type":0},
	      		 	{"title":"取消","iconCls":"icon-cancel","action":"billompreparedivide.coloseDialog('boxDialog')","type":0}
			     ]
		  	/><div class="search-div">
			<form id="boxForm" name="boxForm" metdod="post"  class="city-form">
			    <input type='hidden' id='expNos' name='expNos'/>
	    		<table>
					<tr>
						<td class="common-td blank">单据来源：</td>
		   				<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="billSource" id="billSourceBox"/></td>		   				
		   				<td class="common-td blank"> 品牌库：</td>
		     			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoBox"/></td>
		     			<td class="common-td blank">品牌：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNoBox"/></td>
		   				<td class="common-td blank">大类一：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="cateOne" id="cateOneCondition" /></td>
					</tr>
					<tr>	
					<td class="common-td blank">大类二：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="cateTwo" id="cateTwoCondition" /></td>
						<td class="common-td blank">大类三：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="cateThree" id="cateThreeCondition" /></td>	   				
						<td id='adviceNoBoxLabel' class="common-td blank">预到货通知单：</td>
						<td><input class="easyui-validatebox ipt" name="adviceNo"  id="adviceNoBox" style="width:120px;"/></td>
						<td class="common-td blank">验收单：</td>
						<td><input class="easyui-validatebox ipt" name="checkNo"  id="checkNoBox" style="width:120px;"/></td>
						
					</tr>	
					<tr>
					<td class="common-td blank">储位：</td>
						<td><input class="easyui-validatebox ipt" name="cellNo"  id="cellNoBox" style="width:120px;"/></td>
						<td class="common-td blank">箱号：</td>
						<td><input class="easyui-validatebox ipt" name="boxNo" id="boxNoBox" style="width:120px"/></td>
						<td class="common-td blank">商品编码：</td>
						<td><input class="easyui-validatebox ipt" name="itemNo"  id="itemNoBox" style="width:120px;"/></td>
						<td class="common-td blank">仓区：</td>
						<td><input class="easyui-combobox ipt" style="width:120px" name="wareNo" id="wareNoCondition"/></td>
					</tr>
					<tr>
						<td class="common-td">库区：</td>
						<td><input class="easyui-combobox ipt"  style="width:120px" name="areaNo" id="areaNoCondition"/></td>	
						<td class="common-td blank">托盘号：</td>
						<td><input class="easyui-validatebox ipt"  style="width:120px" name="panNo" id="panNoCondition"/></td>	  			
					</tr>
	    		</table>
		    </form></div>
		</div>
		<div data-options="region:'center',border:false">	
						<@p.datagrid id="mainDataGrid3" name=""  loadUrl="" saveUrl="" defaultColumn="" title=""
							isHasToolBar="false" onClickRowEdit="false" singleSelect="false" pageSize='10'  
							pagination="true" rownumbers="true" divToolbar="" emptyMsg="" 
							columnsJsonList="[
								{field : 'locno',hidden:true},
								{field : 'ownerNo',hidden:true},
								{field : 'expNo',title : '发货通知单号',width : 160,align:'left'}
						]"/>
	    </div>
		<div data-options="region:'south',minSplit:true" style="height:280px">
			<@p.datagrid id="dataGridJG_Box"  loadUrl="" saveUrl=""   defaultColumn="" 
              isHasToolBar="false"   pagination="true"
	           rownumbers="true"  onClickRowEdit="false" height="160" singleSelect="false"
	           columnsJsonList="[
					{field : ' ',checkbox:true},
	           		{field : 'boxNo',title:'箱号',width:120,align:'left'},
					{field : 'cellNo',title:'储位',width:180},
					{field : 'receiptQty',title:'数量',width:180}
	                 ]"
               />
		</div>
	</div>	    
</div>

<#-- 发货单选择div -->
		<div id="showExpDialog"  class="easyui-dialog" title="发货通知单选择"  style="width:680px;height:450px;"
		    	data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    	maximized:false,minimizable:false,maximizable:false"> 
			<div class="easyui-layout" data-options="fit:true">
				<#--查询start-->
				<div data-options="region:'north',border:false" >
			    	<@p.toolbar id="expToolbar"   listData=[
			    		{"title":"查询","iconCls":"icon-search","action":"billompreparedivide.searchExp();", "type":0},
	       				{"title":"清除","iconCls":"icon-remove","action":"billompreparedivide.searchClear('searchExpNoForm');", "type":0},
	       				{"title":"确定","iconCls":"icon-ok","action":"billompreparedivide.selectBillOmExp();", "type":0},
			    		{"id":"btn-canel","title":"取消","iconCls":"icon-cancel","action":"billompreparedivide.closeWindow('showExpDialog');","type":0}
			        ]/>
					<div nowrap class="search-div" style="padding:10px;">
						<form name="searchExpNoForm" id="searchExpNoForm" method="post" class="city-form">
					 		<table>
					     		<tr>
					     			<td class="common-td">发货通知单号：</td>
					     			<td><input class="easyui-validatebox ipt" style="width:120px" name="expNo" id="expNo" /></td>
					     			<td class="common-td blank">发货状态：</td>
                     				<td><input class="easyui-combobox ipt" style="width:120px" name="statusExp" id="statusExp" /></td>
									<td class="common-td blank">所属品牌：</td>
                     				<td ><input class="easyui-combobox ipt" style="width:180px" name="brandNoExp" id="brandNoExp" /></td>
					     		</tr>
					     		<tr>
					     			<td class="common-td">创建日期：</td>
					     			<td><input class="easyui-datebox ipt" style="width:120px" name="startCreatetmExp" id="startCreatetmExp" /></td>
					     			<td class="common-line">&mdash;</td>
                     				<td><input class="easyui-datebox ipt" style="width:120px" name="endCreatetmExp" id="endCreatetmExp" /></td>
									<td class="common-td blank"></td>
                     				<td ></td>
					     		</tr>
					     	</table>
					 	</form>
					</div>
				</div>
				<#--查询end-->
				<#--显示列表start-->
				<div data-options="region:'center',border:false">
					<@p.datagrid id="expNoDataGrid" name=""  loadUrl="" saveUrl="" defaultColumn="" 
						isHasToolBar="false" height="280"  onClickRowEdit="false" singleSelect="false" pageSize='10'  
						pagination="true" rownumbers="true" divToolbar="" emptyMsg=""
						columnsJsonList="[
							{field : 'id',checkbox:true},
							{field : 'ownerNo',hidden:true},
							{field : 'expNo',title : '发货通知单号',width : 160,align:'left'},
							{field : 'statusStr',title : '状态',width : 80,align:'left'},
							{field : 'totalExpQty',title : '待分配数量',width : 80,align:'right'},
							{field : 'createtm',title : '创建日期',width : 135},
							{field : 'expRemark',title : '备注',width : 160,align:'left'}
							
					]"/>
				</div>
				<#--显示列表end-->
			</div>			
		</div>

</body>
</html>