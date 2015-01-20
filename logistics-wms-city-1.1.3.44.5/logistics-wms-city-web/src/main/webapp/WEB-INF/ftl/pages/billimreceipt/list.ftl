<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>收货单</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
    <script type="text/javascript" src="${domainStatic}/resources/js/billimreceipt/billimreceipt.js?version=1.0.5.6"></script>
    <!--object需放在head结尾会截断jquery的html函数获取内容-->
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
        <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
    </object>
</head>
    
<body class="easyui-layout">
	<#-- 工具菜单div -->	
     <div data-options="region:'north',border:false">
       <@p.toolbar id="maintoolbar"  listData=[
      		 {"title":"查询","iconCls":"icon-search","action":"billimreceipt.searchArea()","type":0},
      		 {"title":"清空","iconCls":"icon-remove","action":"billimreceipt.searchLocClear()","type":0},
             {"title":"新增","iconCls":"icon-add","action":"billimreceipt.addInfo()","type":1},
             {"title":"修改","iconCls":"icon-edit","action":"billimreceipt.editInfo()","type":2},
             {"title":"删除","iconCls":"icon-del","action":"billimreceipt.del()","type":3},
             {"title":"审核","iconCls":"icon-aduit","action":"billimreceipt.audit()","type":4},
             {"title":"打印预览","iconCls":"icon-print","action":"billimreceipt.print();","type":0},
             {"title":"验收转单","iconCls":"icon-redo","action":"billimreceipt.directCheck();","type":0},
	         {"title":"关闭","iconCls":"icon-close","action":"closeWindow('收货单')","type":0}
	         ]
		  />
	 </div>
	 <div data-options="region:'center',border:false">
	 	<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
		   		<form name="searchForm" id="searchForm" method="post" class="city-form">
		   			<table>
		   				<tr>
		   					<td class="common-td blank">状&nbsp;&nbsp;&nbsp;&nbsp;态：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="status" id="search_status" /></td>
		   					<td class="common-td blank">审&nbsp;核&nbsp;人：</td>
		   					<td><input class="easyui-combobox" style="width:120px" data-options="editable:false" id="search_auditor" name="auditor"/></td>
		   					<td class="common-td blank">审核日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="audittmStart" id="audittmStart"/></td>
		   					<td class="common-line">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="audittmEnd" id="audittmEnd"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">收货单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="receiptNo"/></td>
		   					<td class="common-td blank">收&nbsp;货&nbsp;人：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" data-options="editable:false" id="search_receiptWorker" name="receiptWorker"/></td>
		   					<td class="common-td blank">收货日期：</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="startTm" id="startTm"/></td>
		   					<td class="common-line">&mdash;</td>
		   					<td><input class="easyui-datebox" style="width:120px" name="endTm" id="endTm"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">收货码头：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="dockNo" id="serachDockNo"/></td>
		   					<td class="common-td blank">创&nbsp;建&nbsp;人：</td>
		   					<td><input class="easyui-combobox" style="width:120px" data-options="editable:false" id="search_creator" name="creator"/></td>
		   					<td class="common-td blank">来源单号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="transNo"/></td>
		   					<td class="common-td blank">车&nbsp;牌&nbsp;号：</td>
		   					<td><input class="easyui-validatebox ipt" style="width:120px" name="carPlate"/></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">货&nbsp;&nbsp;主：</td>
		   					<td><input class="easyui-combobox" data-options="editable:false" style="width:120px" name="ownerNo" id="search_ownerNo" /></td>
		   					
		   					<td class="common-td blank">预到货通知单：</td>
		   					<td> <input class="easyui-validatebox ipt" style="width:120px" name="importNo"/></td>
		   					<td class="common-td blank"> 品&nbsp;牌&nbsp;库：</td>
	             			<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
		   					<td class="common-td blank">所属品牌：</td>
							<td><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="search_brandNo" /></td>
		   				</tr>
		   				<tr>
		   					<td class="common-td blank">箱号：</td>
			               	<td><input class="easyui-validatebox ipt" style="width:120px" name="boxNo" id="boxNo_dataForm" /></td>
			                <td class="common-td blank"></td>
			                <td></td>
			                <td class="common-td blank"></td>
			                <td></td>
			                <td class="common-td blank"></td>
			                 <td></td>
                    	</tr>
		   			</table>
				</form>
			</div>
			<div data-options="region:'center',border:false">
				<@p.datagrid id="dataGridJG" loadUrl="" saveUrl="" defaultColumn=""   title="收货单"
	              isHasToolBar="false"  onClickRowEdit="false" emptyMsg="" 
	               pagination="true" rownumbers="true"  singleSelect = "false" showFooter="true"
		           columnsJsonList="[
						{field : ' ',checkbox:true},
						{field : 'receiptNo',title : '收货单号',width :150},
						{field : 'status',title : '状态',width : 70,formatter:billimreceipt.statusFormatter,align:'left'},
						{field : 'ownerNo',title : '货主',width : 100,formatter:billimreceipt.ownerFormatter,align:'left'},
						{field : 'carPlate',title : '车牌号',width : 100,align:'left'},
						{field : 'recivedate',title : '收货日期',width : 100,sortable:true},
						{field : 'dockNo',title : '收货码头',width : 100,align:'left',formatter:billimreceipt.dockFormatter},
						{field : 'receiptqty',title : '商品总数',width : 100,align:'right'},
						{field : 'boxqty',title : '	总箱数',width : 80,align:'right'},
						{field : 'receiptWorker',title : '收货人',width : 100,align:'left'},
						{field : 'receiptName',title : '收货人姓名',width : 100,align:'left'},
						{field : 'creator',title : '创建人',width : 100,align:'left'},
						{field : 'creatorName',title : '创建人名称',width : 100,align:'left'},
						{field : 'createtm',title : '创建时间',width : 130,sortable:true},
						{field : 'auditor',title : '审核人',width : 100,align:'left'},
						{field : 'auditorName',title : '审核人名称',width : 100,align:'left'},
						{field : 'audittm',title : '审核时间',width : 130,sortable:true},
						{field : 'locno',hidden:'true'}
		                 ]" 
			           	jsonExtend='{
				   			//url:BasePath+"/bill_im_receipt/findMainRecipt",
				           	//method:"post",
	    				   	//queryParams:{"locno": billimreceipt.locno,
			                // "startTm": billimreceipt.getDate(0),
			                // "endTm": billimreceipt.getDate(0),
			                // "businessType" : "0"
					       	//},
					       	onLoadSuccess:function(data){//合计
					       	billimreceipt.onLoadSuccess(data);
					       	},
					   		onSelect:function(rowIndex, rowData){
	                       	 // 触发点击方法  调JS方法
	                   		},
	                   		onDblClickRow:function(rowIndex, rowData){
	                 		 billimreceipt.showDetail(rowData);
	                   		}
	                   	}'
	        	/>
			</div>
		</div>
	</div>
<#-- 编辑-->
<div id="detailDialog" class="easyui-window" title="收货单明细"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id="submaintoolbar"  listData=[
      		 	{"id":"addBtn","title":"保存","iconCls":"icon-save","action":"billimreceipt.save_main()","type":0},
      		 	{"id":"editBtn","title":"修改","iconCls":"icon-edit","action":"billimreceipt.edit_main()","type":0},
      		 	{"title":"取消","iconCls":"icon-cancel","action":"billimreceipt.coloseDetailDialog()","type":0}
      		 	]
		  	/>
			<form id="detailForm" name="detailForm" method="post" class="city-form">
				<input id="receiptWorkerName" name="receiptName" type="hidden"/>
	    		<table>
					<tr>
						<td class="common-td blank">收货单号：</td>
						<td>
							<input class="easyui-validatebox ipt" id="receiptNo" name="receiptNo" style="width:120px;display:none"/>
							<input class="easyui-validatebox ipt" value="" style="width:120px" name="receiptNo"  id="receiptNoHide"/>
						</td>
						<td class="common-td blank">&nbsp;货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						<td>
							<input class="easyui-combobox" data-options="editable:false" name="ownerNo" id="ownerNo" style="width:120px"/>
							<input name="ownerNo" type="hidden" id="ownerNoHide"/>
						</td>
						<td class="common-td blank">收&nbsp;货&nbsp;人：</td>
						<td><input class="easyui-combobox" data-options="editable:false" name="receiptWorker" id="receiptWorker" style="width:120px"/></td>
						<td class="common-td blank">&nbsp;收货码头：</td>
						<td><input class="easyui-combobox" data-options="editable:false" name="dockNo" id="dockNo" style="width:120px"/></td>
					</tr>
					<tr>	
						<td class="common-td blank">&nbsp;车&nbsp;牌&nbsp;号：</td>
						<td><input class="easyui-validatebox ipt" name="carPlate" style="width:120px" data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"/></td>
						<td class="common-td blank">&nbsp;司&nbsp;&nbsp;&nbsp;&nbsp;机：</td>
						<td><input class="easyui-validatebox ipt" name="shipDriver" style="width:120px" data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"/></td>
						<td class="common-td blank">&nbsp;收货日期：</td>
						<td><input  class="easyui-datebox ipt" style="width:120px" name="recivedate" id="curDate" disabled="true"/></td>
						<td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td colspan="5"><input class="easyui-validatebox ipt" name="remark" style="width:100%" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"/></td>
					</tr>	
	    		</table>
		    </form>
		</div>
		<div data-options="region:'center'" data-options="fit:true">
			<div class="easyui-layout" data-options="fit:true">
				<div data-options="region:'center',border:false">
					<div id="sub_con">
						<@p.toolbar id="opBtn"  listData=[
							{"title":"新增明细","iconCls":"icon-add-dtl","action":"billimreceipt.showBoxDialog()","type":0},
			      		 	{"title":"删除明细","iconCls":"icon-del-dtl","action":"billimreceipt.deleterow()","type":0},
			      		 	{"title":"保存明细","iconCls":"icon-save-dtl","action":"billimreceipt.edit()","type":0}
			      		 	]
		  				/>
					</div>
					<@p.datagrid id="dataGridJG_detail"  loadUrl="" saveUrl=""   defaultColumn="" 
				              isHasToolBar="false" onClickRowEdit="false"    pagination="true"
					           rownumbers="true"  onClickRowEdit="true" singleSelect="true"  title="明细列表" divToolbar="#sub_con" emptyMsg="" 
					           columnsJsonList="[
									{field:'boxNo',title:'箱号',width:120,align:'left'},
									{field:'panNo',title:'父容器号',width:130,align:'left',editor:{
										type:'validatebox'
								  	}},
									{field:'importNo',title:'预到货通知单号',width:180},
									{field : 'brandName',title :'品牌',width : 170,align:'left'},
									{field : 'spoNo',title :'合同号',width : 100,align:'left'},
									{field : 'deliverNo',title :'装车单号',width : 180,align:'left'},
									{field : 'qty',title : '数量',width : 100,align:'right'},
									{field : 'locno',hidden:'true'}
					]"/>
				</div>
			</div>
		</div>
	</div>	    
</div>
<#-- 详情 -->
<div id="detailDialogView" class="easyui-window" title="收货单明细"
		    data-options="modal:true,resizable:false,draggable:false,collapsible:false,closed:true,
		    maximized:true,minimizable:false,maximizable:false">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false">
			<@p.toolbar id="detailbar"  listData=[
      		 	{"title":"导出","iconCls":"icon-export","action":"billimreceipt.exportDetail()","type":0},
      		 	{"title":"取消","iconCls":"icon-cancel","action":"billimreceipt.closeWin('detailDialogView')","type":0}
      		 	]
		  	/>
	    	<form id="detailForm_view" name="detailForm" method="post" class="city-form" style="padding:10px;">
	    		<table>
					<tr>
						<td class="common-td blank">收货单号：</td>
						<td><input class="easyui-validatebox ipt" name="receiptNo" style="width:120px;" id="detail_receiptNo"  readOnly='readOnly'/></td>
						<td class="common-td blank">货&nbsp;&nbsp;&nbsp;&nbsp;主：</td>
						<td><input class="easyui-combobox" data-options="editable:false" name="ownerNo" id="ownerNo_view" style="width:120px"/></td>
						<td class="common-td blank">收货码头：</td>
						<td><input class="easyui-combobox" data-options="editable:false" name="dockNo" id="dockNo_view" style="width:120px"/></td>
						<td class="common-td blank">收&nbsp;货&nbsp;人：</td>
						<td><input class="easyui-combobox" data-options="editable:false" name="receiptWorker" id="receiptWorker_view" style="width:120px"/></td>
						<td class="common-td blank">&nbsp;车&nbsp;牌&nbsp;号：</td>
						<td><input class="easyui-validatebox ipt" name="carPlate" style="width:120px" data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"/></td>
					</tr>
					<tr>	
						<td class="common-td blank">司&nbsp;&nbsp;&nbsp;&nbsp;机：</td>
						<td><input class="easyui-validatebox ipt" name="shipDriver" style="width:120px" data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"/></td>
						<td class="common-td blank">收货日期：</td>
						<td><input style="width:120px" name="recivedate ipt" id="curDate" /></td>
						<td class="common-td blank">箱&nbsp;&nbsp;&nbsp;&nbsp;数：</td>
						<td><input class="easyui-validatebox ipt"  style="width: 120px;" id="params"/></td>
						<td class="common-td blank">备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td colspan="3"><input class="easyui-validatebox ipt" name="remark" style="width: 310px;" data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"/></td>
					</tr>	
	    		</table>
	    	</form>
		</div>
		<div data-options="region:'center'">
						 <@p.datagrid id="dataGridJG_detail_view"  loadUrl="" saveUrl=""   defaultColumn="" 
	              			isHasToolBar="false" onClickRowEdit="false"    pagination="true" showFooter="true"
				           rownumbers="true"  height="200" onClickRowEdit="true" singleSelect="true"
				           columnsJsonList="[
				           		{field:'importNo',title:'预到货通知单号',width:150},
								{field:'itemNo',title:'商品编码',width:150,align:'left'},
								{field:'itemName',title:'商品名称',width:150,align:'left'},
								{field:'colorName',title:'颜色',width:80,align:'left'},
								{field:'sizeNo',title:'尺码',width:50},
								{field:'boxNo',title:'箱号',width:150},
								{field:'panNo',title:'父容器号',width:150},
								{field :'brandName',title :'品牌',width : 70,align:'left'},
								{field:'receiptQty',title:'收货数量',width:60,align:'right'},
								{field:'checkQty',title:'验收数量',width:60,align:'right'},
								{field:'divideQty',title:'计划分货数量',width:100,align:'right'},
								{field : 'checkWorker1',title : '收货人',width : 70,align:'left'},
								{field : 'checkName1',title : '收货人名称',width : 100,align:'left'},
								{field : 'poNo',title :'合同号',width : 100,align:'left'},
								{field : 'deliverNo',title :'装车单号',width : 180,align:'left'}
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
					{"title":"查询","iconCls":"icon-search","action":"billimreceipt.searchBox()","type":0},
	      		 	{"title":"清空","iconCls":"icon-remove","action":"billimreceipt.clearSearchBox()","type":0},
	      		 	{"title":"确定","iconCls":"icon-ok","action":"billimreceipt.selectBoxOk()","type":0}
	      		 	{"title":"批量添加","iconCls":"icon-ok","action":"billimreceipt.batchSelectBoxOk()","type":0}
			     ]
		  	/>
			<form id="boxForm" name="boxForm" metdod="post"  class="city-form">
	    		<input name="supplierNo" type="hidden" id="boxSupplierNo"/>
	    		<table>
					<tr>
						<td class="common-td blank">合同号：</td>
						<td><input class="easyui-validatebox ipt" name="spoNo" id="spoNo"  style="width:120px"/></td>
						<td class="common-td blank">品牌名称：</td>
						<td><input class="easyui-combobox" name="brandNo" id="brandNo" style="width:120px"/>
						<!--<input class="easyui-combobox" name="ownerNo" id="boxOwnerNo"  style="width:100px"/>-->
							<input name="ownerNo" type="hidden" id="boxOwnerNoHide"/>
						</td>
						<td class="common-td blank">发货日期：</td>
						<td><input class="easyui-datebox" name="orderDateStart" id="boxorderDateStart" style="width:120px"/></td>
						<td class="common-line" style="padding:0px 5px;">&mdash;</td>
						<td><input class="easyui-datebox" name="orderDateEnd" id="boxorderDateEnd" style="width:120px"/></td>
					</tr>	
					<tr>
						<td class="common-td blank">来源单号：</td>
						<td><input class="easyui-validatebox ipt" name="transNo" id="boxtransNo" style="width:120px"/></td>
						<td class="common-td blank">装车单号：</td>
						<td><input class="easyui-validatebox ipt" name="deliverNo"  id="boxdeliverNo" style="width:120px;"/></td>
						<td class="common-td blank">预到货日期：</td>
						<td><input class="easyui-datebox" name="requestDateStart" id="boxrequestDateStart" style="width:120px"/></td>
						<td class="common-line" style="padding:0px 5px;">&mdash;</td>
						<td><input class="easyui-datebox" name="requestDateEnd" id="boxrequestDateEnd" style="width:120px"/></td>
					</tr>
					<tr>
						<td class="common-td blank">预到货通知单：</td>
						<td colspan="7"><input class="easyui-validatebox ipt" name="importNo" id="boximportNo" style="width:120px"/></td>
					</tr>		    			
	    		</table>
		    </form>
		</div>
		<div data-options="region:'center',border:false">	
			<@p.datagrid id="dataGridJG_Box"  loadUrl="" saveUrl=""   defaultColumn="" 
              isHasToolBar="false"   pagination="true"
	           rownumbers="true"  onClickRowEdit="false" height="160" singleSelect="false"
	           columnsJsonList="[
					{field : ' ',checkbox:true},
	           		{field : 'sysName',title:'所属品牌库',width:100,align:'left'},
	           		{field : 'sPoNo',title:'合同号',width:100,align:'left'},
	           		{field : 'supplierName',title:'供应商名称',width:150,align:'left'},
					{field : 'importNo',title:'预到货通知单号',width:180},
					{field : 'transNo',title :'来源单号',width : 180,align:'left'},
					{field : 'ownerName',title :'货主',width : 100,align:'left'},
					{field : 'orderDate',title : '发货日期',width : 100},
					{field : 'requestDate',title : '预到货日期',width : 100}
	                 ]"
	                jsonExtend='{onSelect:function(rowIndex, rowData){
                            // 触发点击方法  调JS方法
                   },onDblClickRow:function(rowIndex, rowData){
                   	    billimreceipt.searchBoxDetail(rowIndex,rowData);
                   }}'
               />
		</div>
		<div data-options="region:'south',minSplit:true" style="height:200px">	
		      <@p.datagrid id="dataGridJG_BoxDetail"  loadUrl="" saveUrl=""   defaultColumn="" 
	              isHasToolBar="false" onClickRowEdit="false"    pagination="true"
		           rownumbers="true"  height="170" onClickRowEdit="true" singleSelect="false" 
		           columnsJsonList="[
						{field : ' ',checkbox:true},
						{field : 'itemNo',title:'商品编号',width:150,hidden:true},
						{field : 'itemName',title:'商品名称',width:150,align:'left',hidden:true},
						{field : 'brandName',title :'品牌',width : 100,align:'left',hidden:true},
						{field : 'boxNo',title:'箱号',width:160},
						{field : 'qty',title : '数量',width : 100,align:'right'},
						{field : 'deliverNo',title :'装车单号',width : 180,align:'left'},
						{field : 'spoNo',title : '合同号',width : 100,align:'left'},
						{field : 'locno',hidden:'true'}
		       ]"/>
		</div>
	</div>	    
</div>
</body>
</html>