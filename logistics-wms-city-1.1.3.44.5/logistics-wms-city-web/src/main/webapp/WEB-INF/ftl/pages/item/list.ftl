<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>商品信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${domainStatic}/resources/js/baseinfo/item.js?version=1.0.5.0"></script>
</head>

<body class="easyui-layout">
<#-- 工具菜单div -->
<div data-options="region:'north',border:false" class="toolbar-region">
	<@p.toolbar id="toolbar"  listData=[
		 {"title":"查询","iconCls":"icon-search","action":"item.searchItem()", "type":0},
		 {"title":"清除","iconCls":"icon-remove","action":"item.searchItemClear()", "type":0},
		 {"id":"btn-close","title":"关闭","iconCls":"icon-close","action":"closeWindow('商品信息')","type":0}
	]/>
</div>

<#-- 主表start -->
	<div data-options="region:'center',border:false">
        <div class="easyui-layout" data-options="fit:true" id="subLayout">
        	<#--查询start-->
        	<div  data-options="region:'north',border:false" >
        		<div nowrap id="itemSearchDiv" class="search-div" style="padding:10px;">
        			<form name="searchForm" id="searchForm" method="post" class="city-form">
						<table>
							<tr>
							    <td class="common-td">商品品类：</td>
								<td><input class="easyui-combobox ipt" style="width:120px" name="cateNoStr" id="cateNoStrCondition" /></td>
								<td class="common-td blank">商品编码：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="itemNoFuzzy" id="itemNoCondition" /></td>
								<td class="common-td blank">货号：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="itemCodeFuzzy" id="itemCodeCondition" /></td>
								<td class="common-td blank">商品名称：</td>
								<td><input class="easyui-validatebox ipt" style="width:120px" name="itemName" id="itemNameCondition" /></td>
							</tr>
							
							<tr>
								<td class="common-td blank">所属品牌库：</td>
		                 		<td><input class="easyui-combobox" style="width:120px" name="sysNo" id="sysNoSearch"/></td>
								<td class="common-td blank">所属品牌：</td>
									<td ><input class="easyui-combobox ipt" style="width:120px" name="brandNo" id="brandNo" /></td>
								<td class="common-td"></td>
								<td></td>
							</tr>
						</table>
					</form>
        		</div>
        	</div>
      		<#--查询end-->
      		<#--显示列表start-->
        	<div data-options="region:'center',split:false" >
		    <@p.datagrid id="dataGridItem"  loadUrl="" saveUrl=""  defaultColumn=""  title="商品列表"
		       isHasToolBar="false" divToolbar=""  onClickRowEdit="false"    
		       pagination="true" rownumbers="true" emptyMsg="" pageNumber=0
		       columnsJsonList="[
		          	{field : 'itemNo',title : '商品编码',width : 150,align:'left'},
		          	{field : 'itemCode',title : '货号',width : 140,align:'left'},
		          	{field : 'itemName',title : '商品名称',width : 150,align:'left'},
		          	{field : 'itemEname',title : '商品英文名',width : 120,align:'left'},
		          	{field : 'sysNoStr',title : '所属品牌库',width : 80,align:'left'},
		          	{field : 'cateNoStr',title : '商品品类',width : 60,align:'left'},
		          	{field : 'cateNo1Str',title : '辅助品类',width : 60,align:'left'},
		          	{field : 'sizeKind',title : '尺寸类别',width : 60},
		          	{field : 'colorNoStr',title : '颜色',width : 50,align:'left'},
		          	{field : 'brandNoStr',title : '商品品牌',width : 100,align:'left'},
		          	{field : 'salePrice',title : '牌价',width : 60,align:'left'},
		          	{field : 'salePrice0',title : '建议牌价',width : 60,align:'left'},
		          	{field : 'cost0',title : '地区价',width : 60,align:'right'},
		          	{field : 'itemStatusStr',title : '商品状态',width : 60,align:'left'},
		          	{field : 'supplierNoStr',title : '缺省厂商',width : 100,align:'left'},
		          	{field : 'workItemNo',title : '工厂货号',width : 100,align:'left'},
		          	{field : 'heeltypeStr',title : '跟型',width : 60,align:'left'},
				 	{field : 'bottomtypeStr',title : '底型',width : 60,align:'left'},
					{field : 'seasonStr',title : '季节',width : 60},
					{field : 'genderStr',title : '性别',width : 60},
					{field : 'ingredientsStr',title : '主料',width : 60,align:'left'},
					{field : 'yearsStr',title : '年份',width : 60},
					{field : 'styleStr',title : '风格',width : 60,align:'left'},
					{field : 'specialfeaturesStr',title : '特殊功能',width : 60,align:'left'},
					{field : 'orderfromStr',title : '订货形式',width : 60,align:'left'},
					{field : 'liningStr',title : '内里',width : 60,align:'left'},
					{field : 'seriesStr',title : '系列',width : 60,align:'left'},
					{field : 'repeatlistingStr',title : '重复上市',width : 60,align:'left'},
					{field : 'productlineStr',title : '产品系列',width : 60,align:'left'},
					{field : 'monthStr',title : '月份',width : 60},
					{field : 'otclistingdateStr',title : '上柜日',width : 60},
					{field : 'brandstyleStr',title : '品牌款式',width : 60,align:'left'},
					{field : 'distributionmustStr',title : '分配必订',width : 60,align:'left'},
					{field : 'marketsupportStr',title : '市场支持',width : 60,align:'left'},
					{field : 'pricerangeStr',title : '价格区间',width : 60,align:'left'},
					{field : 'maincolorStr',title : '主色',width : 60,align:'left'},
					{field : 'secondarycolorStr',title : '副色',width : 60,align:'left'},
					{field : 'supporttypeStr',title : '支持类型',width : 60,align:'left'},
		          	{field : 'creator',title : '建档人',width : 60,align:'left'},
		          	{field : 'createtm',title : '建档时间',width : 125,sortable:true},
		          	{field : 'editor',title : '修改人',width : 60,align:'left'},
		          	{field : 'edittm',title : '修改时间',width : 125,sortable:true}
		         ]" 
		          
		       jsonExtend='{onSelect:function(rowIndex, rowData){
		                // 触发点击方法  调JS方法
		                item.loadDetail(rowData.itemNo);
		       }}'/>
	       </div>
        	<#--显示列表end-->
	       
	       <div data-options="region:'south',split:false,border:false,minSplit:true" style="height:283px;">
	       			<div id="main_item_DetailId" class="easyui-tabs" data-options="fit:true">   
	       				<#-- tab-1 -->
	       				<div id="tab_item_info" title="基本信息" style="padding:5px;background:#F4F4F4;">
	       						<div class="easyui-layout" data-options="fit:true" id="subLayout_tab3" style="height:242px;">
		       						<form name="dataForm" id="dataForm" method="post" class="city-form">
							              <table>
										 	<tr>
										 		<td class="common-td">商品编码：</td>
										 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:272px" name="itemNo" id="itemNo"  readOnly="true" /></td>
										 		<td align='left' class="common-td blank">货号：</td>
										 		<td colspan='3'><input class="easyui-validatebox ipt" style="width:273px" name="itemCode" id="itemCode"  readOnly="true" /></td>
										 	</tr>
										 	
										 	<tr>
										 		<td class="common-td">商品名称：</td>
										 		<td colspan='3' style='padding-right:3px;'><input class="easyui-validatebox ipt" style="width:272px" name="itemName" id="itemName"  readOnly="true" /></td>
										 		<td class="common-td blank">商品英文名：</td>
										 		<td colspan='3' style='padding-right:3px;'><input class="easyui-validatebox ipt" style="width:273px" name="itemEname" id="itemEname"  readOnly="true" /></td>
										 	</tr>
										 	
										 	<tr>
										 		<td class="common-td">颜色：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="colorNoStr" id="colorNoStr"  readOnly="true" /></td>
										 		<td class="common-td blank">尺寸类别：</td>
										 		<td style='padding-right:3px;'><input class="easyui-validatebox ipt" style="width:100px" name="sizeKind" id="sizeKind"  readOnly="true" /></td>
										 		<td class="common-td blank">商品品类：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="cateNoStr" id="cateNoStr"  readOnly="true" /></td>
										 		<td class="common-td blank">辅助品类：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="cateNo1Str" id="cateNo1Str"  readOnly="true" /></td>
										 	</tr>
										 	
										 	<tr>
										 		<td class="common-td">商品品牌：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="brandNoStr" id="brandNoStr"  readOnly="true" /></td>
										 		<td class="common-td blank">牌价：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="salePrice" id="salePrice"  readOnly="true" /></td>
										 		<td class="common-td blank">建议牌价：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="salePrice0" id="salePrice0"  readOnly="true" /></td>
										 		<td class="common-td blank">地区价：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="cost0" id="cost0"  readOnly="true" /></td>
										 	</tr>
										 	
										 	<tr>
										 		<td class="common-td">商品状态：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="itemStatusStr" id="itemStatusStr"  readOnly="true" /></td>
										 		<td class="common-td blank">上市日：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="saleDt" id="saleDt"  readOnly="true" /></td>
										 		<td class="common-td blank">缺省厂商：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="supplierNoStr" id="supplierNoStr"  readOnly="true" /></td>
										 		<td class="common-td blank">工厂货号：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="workItemNo" id="workItemNo"  readOnly="true" /></td>
										 	</tr>
										 	
										 	<tr>
										 		<td class="common-td">建档人：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="creator" id="creator"  readOnly="true" /></td>
										 		<td class="common-td blank">建档时间：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="createtm" id="createtm"  readOnly="true" /></td>
										 		<td class="common-td blank">修改人：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="editor" id="editor"  readOnly="true" /></td>
										 		<td class="common-td blank">修改时间：</td>
										 		<td><input class="easyui-validatebox ipt" style="width:100px" name="edittm" id="edittm"  readOnly="true" /></td>
										 	</tr>
										 	
							              </table>
							        </form>
						        </div>
	       				</div>
	       				
	       				<#-- tab-2 -->
	       				<div id="tab_item_info_other" title="类别信息" style="padding:5px;background:#F4F4F4;">   
	       					<div class="easyui-layout" data-options="fit:true" id="subLayout_tab2" style="height:242px;">
	       					
	       						<table>
								 <tr>
								     <td valign='top'>
											<form name="dataForm" id="dataForm_other" method="post" class="city-form">
									              
									              <table>
												 	
												 	<tr>
												 		<td class="common-td">跟型：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="heeltypeStr" id="heeltypeStr"  readOnly="true" /></td>
												 		<td class="common-td blank">底型：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="bottomtypeStr" id="bottomtypeStr"  readOnly="true" /></td>
												 		<td class="common-td blank">季节：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="seasonStr" id="seasonStr"  readOnly="true" /></td>
												 	</tr>
												 	
												 	<tr>
												 	    <td class="common-td">性别：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="genderStr" id="genderStr"  readOnly="true" /></td>
												 		<td class="common-td blank">主料：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="ingredientsStr" id="ingredientsStr"  readOnly="true" /></td>
												 		<td class="common-td blank">年份：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="yearsStr" id="yearsStr"  readOnly="true" /></td>
												 	</tr>
												 	
												 	<tr>
												 	    <td class="common-td">风格：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="styleStr" id="styleStr"  readOnly="true" /></td>
												 		<td class="common-td blank">特殊功能：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="specialfeaturesStr" id="specialfeaturesStr"  readOnly="true" /></td>
												 		<td class="common-td blank">订货形式：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="orderfromStr" id="orderfromStr"  readOnly="true" /></td>
												 	</tr>
												 	
												 	<tr>
												 	    <td class="common-td">内里：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="liningStr" id="liningStr"  readOnly="true" /></td>
												 		<td class="common-td blank">系列：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="seriesStr" id="seriesStr"  readOnly="true" /></td>
												 		<td class="common-td blank">重复上市：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="repeatlistingStr" id="repeatlistingStr"  readOnly="true" /></td>
												 	</tr>
												 	
												 	<tr>
												 	    <td class="common-td">产品系列：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="productlineStr" id="productlineStr"  readOnly="true" /></td>
												 		<td class="common-td blank">月份：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="monthStr" id="monthStr"  readOnly="true" /></td>
												 		<td class="common-td blank">上柜日：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="otclistingdateStr" id="otclistingdateStr"  readOnly="true" /></td>
												 	</tr>
												 	
												 	<tr>
												 		<td class="common-td">品牌款式：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="brandstyleStr" id="brandstyleStr"  readOnly="true" /></td>
												 		<td class="common-td blank">分配必订：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="distributionmustStr" id="distributionmustStr"  readOnly="true" /></td>
												 		<td class="common-td blank">市场支持：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="marketsupportStr" id="marketsupportStr"  readOnly="true" /></td>
												 	</tr>
												 	
												 	<tr>
												 	    <td class="common-td">价格区间：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="pricerangeStr" id="pricerangeStr"  readOnly="true" /></td>
												 		<td class="common-td blank">主色：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="maincolorStr" id="maincolorStr"  readOnly="true" /></td>
												 		<td class="common-td blank">副色：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="secondarycolorStr" id="secondarycolorStr"  readOnly="true" /></td>
												 	</tr>
												 	
												 	<tr>
												 	    <td class="common-td">支持类型：</td>
												 		<td><input class="easyui-validatebox ipt" style="width:100px" name="supporttypeStr" id="supporttypeStr"  readOnly="true" /></td>
												 		<td class="common-td blank"> 所属品牌库：</td>
												 		<td colspan='3'><input class="easyui-combobox ipt" style="width:100px" name="sysNo" id="sysNo" readOnly="true"/></td>
												 	</tr>
							 	
									              </table>
									        </form>
									        
								     </td>
								     <td align='left' width="200">
										     <@p.datagrid id="dataGridItemTypeinfo"  loadUrl="" saveUrl=""  defaultColumn="" emptyMsg="" 
											    	isHasToolBar="false"   onClickRowEdit="false" pagination="false" 
											        columnsJsonList="[
											         		{field : 'lookupcodeStr',title : '分类属性',width : 100},
											                {field : 'itemname',title : '分类值',width : 100}
											        ]" 
											 />
								     </td>
								 </tr>
							 </table>
									
							 </div>
	       				</div>
	       				
	       				<#-- tab-3 -->
	       				<div id="tab_item_barcode" title="条码信息" style="background:#F4F4F4;">   
		       				<div class="easyui-layout" data-options="fit:true" id="subLayout_tab3" style="height:252px;">
						    	<@p.datagrid id="dataGridItemBarcode"  loadUrl="" saveUrl=""  defaultColumn=""
						               isHasToolBar="false"   onClickRowEdit="false" pagination="false"
						               rownumbers="true" emptyMsg="" 
						               columnsJsonList="[
						                  {field : 'itemNo',title : '商品编码',width : 150,align:'left'},
						                  {field : 'barcode',title : '商品条码',width : 150,align:'left'},
						                  {field : 'barcodeTypeStr',title : '条码类型',width : 100},
						                  {field : 'sizeNo',title : '尺码',align:'left',width : 80,formatter:function(value){
						                      if(value && value.length>2){
						                         return value.substr(2);
						                      }
						                      return '';
						                  }},
						                   {field : 'salePrice',title : '牌价',width : 80,align:'left'},
						                  {field : 'remarks',title : '备注',width : 150,align:'left'}
						                 ]" 
						                />
						    </div>
					    </div>
	       				
	       				<#-- tab-4 -->
	       				<div id="tab_item_pack" title="包装信息" style="background:#F4F4F4;">   
	       					<div class="easyui-layout" data-options="fit:true" id="subLayout_tab4" style="height:252px;">
					            <@p.datagrid id="dataGridItemPack"  loadUrl="" saveUrl=""  defaultColumn=""
					               isHasToolBar="false"   onClickRowEdit="false"    pagination="false"
					               rownumbers="true" emptyMsg="" 
					               columnsJsonList="[
					                  {field : 'itemNo',title : '商品编码',width : 150,align:'left'},
					                  {field : 'sizeNo',title : '尺码',width : 80,align:'left',formatter:function(value){
					                      if(value && value.length>2){
					                         return value.substr(2);
					                      }
					                      return '';
					                  }},
					                  {field : 'packQty',title : '包装数量',width : 80,align:'right'},
					                  {field : 'packUnit',title : '包装单位',width : 80,align:'left'},
					                  {field : 'packSpec',title : '包装描述',width : 80,align:'left'},
					                  {field : 'packLength',title : '长',width : 80,align:'right'},
					                  {field : 'packWight',title : '宽',width : 80,align:'right'},
					                  {field : 'packHight',title : '高',width : 80,align:'right'},
					                  {field : 'packVolum',title : '体积',width : 80,align:'right'}
					                 ]" 
					                />
					    	</div>
					    </div>
	       				
	       			</div>
	       		</div>
       		</div>
	       
	       
    </div>    
 
</body>
</html>