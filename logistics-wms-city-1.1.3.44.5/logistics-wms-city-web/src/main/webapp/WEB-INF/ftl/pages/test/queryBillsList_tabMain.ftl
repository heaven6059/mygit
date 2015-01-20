<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>单据类管理</title>
<#include  "/WEB-INF/ftl/common/header.ftl" >

<script type="text/javascript">


   

   // 主明细查询
   function doQuery(){
     //  $("#createdate2").datebox("setValue",'2013-07-17');
      // $("#createdate2").datebox("disable");
      var stocknum=$('#stocknum').val();
      var basetype=$('#basetype').val();
      var createdate1=$("#createdate1").datebox("getValue");
      var queryParams={  stocknum:stocknum, 
                        basetype:basetype,
                        createdate1:createdate1
                     };
           
      $("#mainDataGrid").datagrid('options').queryParams=queryParams; 
      $("#mainDataGrid").datagrid('options').url="<@s.url '/stock/list.json' />";
      $("#mainDataGrid").datagrid('load');
      
   }
   
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
   // 全选或者全不选
   function selectCheckAllRow(dataGridId,checkstatus){
       
        var rows = $('#'+dataGridId).datagrid('getRows');
        for ( var i = 0; i < rows.length; i++) {
            if(checkstatus==0){
               $('#'+dataGridId).datagrid('uncheckRow', i);
            }else{
               $('#'+dataGridId).datagrid('checkRow', i);
            } 
        }
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
<body >

	  	       <#--divToolbar有值时，isHasToolBar不能为 true  -->	
		      <@p.datagrid id="mainDataGrid"  loadUrl="/stock/list.json" saveUrl=""  defaultColumn=""  
		                   isHasToolBar="false"  divToolbar="#toolbar" height="623"  onClickRowEdit="false" singleSelect="false" pageSize="10" rownumbers="true"
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
				<div id="toolbar" style="padding:5px;height:auto">
					<div style="margin-bottom:5px">
						<a href="javascript:parent.addStockVo();" class="easyui-linkbutton" iconCls="icon-add" plain="true">新单</a>
						<a href="javascript:selectCheckAllRow('mainDataGrid',1);" class="easyui-linkbutton" iconCls="icon-ok" plain="true">全选</a>
						<a href="javascript:selectCheckAllRow('mainDataGrid',0);" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">全不选</a>
						<a href="javascript:removeStockVo();" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
						<a href="javascript:doQuery()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
						<a href="javascript:parent.closeWindow('主从表操作');" class="easyui-linkbutton" iconCls="icon-no" plain="true">关闭</a>
					</div>
					<div>
						开始时间: <input class="easyui-datebox" style="width:100px" name="createdate1" id="createdate1">
						结束时间: <input class="easyui-datebox" style="width:100px" name="createdate2" id="createdate2">
						库存单号: <input class="easyui-textbox" style="width:100px" name="stocknum" id="stocknum">
						底型类型: 
						<select class="easyui-combobox" panelHeight="auto" style="width:100px" name="basetype" id="basetype">
							<option value="A">A型</option>
							<option value="B">B型</option>
							<option value="D">D型</option>
							<option value="E">E型</option>
							<option value="F">F型</option>
						</select>
						&nbsp;&nbsp;&nbsp;<a href="javascript:doQuery()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<br>
					</div>
				</div>
			<#-- 工具条结束 -->	
	

</body>
</html>