<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>单据类管理</title>
<#include  "/WEB-INF/ftl/common/header.ftl" >

<script type="text/javascript">

	
	

</script>

<script type="text/javascript">


   
   // 删除
   function removeStockVo(){
        var checkedItems = $('#mainDataGrid').datagrid('getChecked');
		var stocknumArray = [];
		$.each(checkedItems, function(index, item){
		     stocknumArray.push(item.stocknum);
		});               
		
		stocknumArray=stocknumArray.join(",");
		if(stocknumArray!=null&&stocknumArray!=''){
		   $.messager.confirm('提示','是否确定要删除库存信息及其明细?',function(r){
		          if(r){
		              	var url = "<@s.url '/stock/removeStockVo.htm'/>";
						var data={
							"stocknumArray":stocknumArray
						};
						ajaxRequest(url,data,function(result){
							if(!result) return ;
							if(result == "success"){
								doQuery();
								return ;
							}else{
								$.messager.alert('提示','删除失败,请联系管理员');
							}
						});
						
		          }
		   });
		   
		 
		}else{
		   $.messager.alert('提示','请先选择需要删除的记录!','warning');
		}
		
   }

   
   //查询
   function doQuery(){
          $("#mainDataGrid").datagrid('options').queryParams={ queryCondition:" and 1=1 " };; 
		  $("#mainDataGrid").datagrid('options').url=BasePath+"/stock/list.json";
		  $("#mainDataGrid").datagrid('load');
   }

   
 

               
       
     //发达ajax请求
    function ajaxRequest(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
  }
  

</script>

</head>
<body  >
       
	  	       <#--divToolbar有值时，isHasToolBar不能为 true  -->	
		      <@p.datagrid id="mainDataGrid"  loadUrl="/stock/list.json" saveUrl=""  defaultColumn=""  
		                   isHasToolBar="false"  divToolbar="#toolbar" height="623"  onClickRowEdit="false" singleSelect="false" pageSize="10"
			               columnsJsonList="[
			                  {field : 'stocknum',checkbox:true,width : 30},
			                  {field : 'factoryid',title : '厂号',width : 100,sortable:true},
			                  {field : 'factoryname',title : '厂名',width : 100,sortable:true},
			                  {field : 'xuantype',title : '楦型',width : 100,sortable:true},
			                  {field : 'stockid',title : '仓库号',width : 100,sortable:true},
			                  {field : 'stockname',title : '仓库名',width : 100,sortable:true},
			                  {field : 'basetype',title : '底型',width : 100,sortable:true},
			                  {field : 'createdate',title : '创建时间',width : 130,sortable:true}
			                 ]" 
			                  
		           jsonExtend='{onDblClickRow:function(rowIndex, rowData){
                            parent.editBillList(rowData.stocknum);
                   }}'/>
			
			 <#-- 工具条与查询条件域 -->	
				<div id="toolbar" style="padding:0px;height:auto">
					<div style="margin-bottom:5px">
						<a href="javascript:parent.addStockVo();" class="easyui-linkbutton" iconCls="icon-add" plain="true">新单</a>
						<a href="javascript:selectCheckAllRowCommon('mainDataGrid',1);" class="easyui-linkbutton" iconCls="icon-ok" plain="true">全选</a>
						<a href="javascript:selectCheckAllRowCommon('mainDataGrid',0);" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">全不选</a>
						<a href="javascript:removeStockVo();" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
						<a href="javascript:doQuery()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
						<a href="javascript:parent.closeWindow('主从表操作');" class="easyui-linkbutton" iconCls="icon-no" plain="true">关闭</a>
					</div>
					
                   <@p.queryConditionDiv id="queryConditionDiv" dbTableName="stock" queryGridId="mainDataGrid"  queryDataUrl="${BasePath}/stock/list.json" collapsed="true" />
                    
					<div  class="easyui-panel" title="查询结果" style="width:auto;padding:0px;background:#F4F4F4" iconCls="icon-mini-tis">
					</div>
	  				
				</div>
				
				 

</body>
</html>