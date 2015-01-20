<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>单据类管理</title>
<#include  "/WEB-INF/ftl/common/header.ftl" >
<style>
	.red {background-color:red}
	.grn {background-color:#F4F4F4}
	.while {background-color:#FFFFFF}
</style>


<script type="text/javascript">
	var queryMainURL="<@s.url '/stock/getMain.htm' />";
	var queryMxURL="<@s.url '/stock/getMxList.htm'/>";
	var saveMainMxVo="<@s.url '/stock/saveZhuCongList.htm'/>";
</script>
<script type="text/javascript" src="<@s.url "/resources/js/billsList.js"/>"></script>
<script type="text/javascript">
       
      document.onkeydown = function(e){
             var e = e || window.event;
             var keyCode = e.keyCode || e.which;
             var tg = e.srcElement || e.target;
	         if (keyCode == 65 && e.altKey) {  
	             addDataGrid();
                 return;
            }else if(keyCode == 88 && e.altKey)  {
                removeDataGrid();
                return;
            }
      }
      

</script>


</head>
<body onload="$('#mainTab').tabs({tabPosition:'bottom'});">
 	
	
	<div id="mainTab" class="easyui-tabs" style="height:660px">
	
	
		<div id="mainTab1" title="单据清单查询" style="padding:0px;" width='100%' height='100%' >
		     <iframe src="<@s.url "/stock/queryBillsListCondition_tabMain.htm"/>" frameborder="0"  border="0" marginwidth="0" marginheight="0" scrolling="auto"   style="width:100%" height="99%"></iframe>
		</div>
	
		
		
       
        <div id="mainTab2" title="单据明细管理" style="padding:0px" height='100%' width='100%' >
        <form id="dataFrom">
        <input type='hidden' id='pid' > 
              
              <div id="toolbarEdit" style="padding:5px;height:auto;background:#F4F4F4">
					<div style="margin-bottom:5px">
						<a href="javascript:saveDataGrid()" class="easyui-linkbutton" iconCls="icon-save" plain="true">保存</a>
						<a href="javascript:returnTab_1()" class="easyui-linkbutton" iconCls="icon-back" plain="true">返回</a>
						<a href="javascript:editBillList($('#pid').val())" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新</a>
						<a href="javascript:closeWindow('动态查询条件');" class="easyui-linkbutton" iconCls="icon-no" plain="true">关闭</a>
					</div>
					
					<div style="padding:10px" >
                                                                      库存单号：<input class="easyui-validatebox" style="width:100px" name="stocknum" id="stocknum"  required="true" missingMessage='库存单号必须输入!' />
				          &nbsp;&nbsp;&nbsp;&nbsp;厂号：<input class="easyui-validatebox"   style="width:100px" name="factoryid" id="factoryid"  required="true" missingMessage='厂号必须输入!' />     
				          &nbsp;&nbsp;&nbsp;&nbsp;厂名：<input class="easyui-validatebox" style="width:100px" name="factoryname" id="factoryname" />
				                          楦型类型：<input class="easyui-validatebox" style="width:100px" name="xuantype" id="xuantype" />
				          </br> <div style="padding:1px"></div>
				          &nbsp;&nbsp;仓库号：<input class="easyui-validatebox" style="width:100px" name="stockid" id="stockid" />
				          &nbsp;&nbsp;仓库名：<input class="easyui-validatebox" style="width:100px" name="stockname" id="stockname" />     
				          &nbsp;&nbsp;&nbsp;&nbsp;底型：<input class="easyui-validatebox" style="width:100px" name="basetype" id="basetype" />
						     日期类型：<input class="easyui-datebox" style="width:106px" name="createdate" id="createdate" />
					</div>
			   </div>
			
			
			  <div style='padding:0px;height:auto;width=100%' >
			        <@p.datagrid id="mainDataGridEdit"    defaultColumn=""  
		                   isHasToolBar="false" divToolbar="#toolbarMx" title='库存明细信息'   height="524"   onClickRowEdit="true" singleSelect="true" pageSize="10"
			               columnsJsonList="
			                    [ {title:'明细ID',field : 'stocklistid',width:200,rowspan:2,align:'center'},
			                      {title:'尺码信息',width:200,colspan:2,align:'center'},
			                      {field : 'amount',title : '数量',rowspan:2,width : 150,editor :{type:'numberbox',options:{required:true,missingMessage:'数量必须输入!',validType:'length[0,8]', invalidMessage:'不能超过8位数字' }}}
			                    ],
			                    [ {field : 'sizetype',title:'尺码类型',width :120,editor : 'validatebox'},
			                      {field : 'sizenum',title : '尺码编号',width : 120,editor : 'validatebox'}
			                    ]
			                 " 
			                 
			                  
		           jsonExtend="{onDblClickRow:function(rowIndex, rowData){
                                 
                              }}"
                   />
			  </div>
			   <#-- 工具条与查询条件域 -->	
				<div id="toolbarMx" style="padding:1px;height:auto">
						 <a href="javascript:addDataGridCommon('mainDataGridEdit')" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增明细(Alt+A)</a>
						<a href="javascript:removeDataGridCommon('mainDataGridEdit')" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除明细(Alt+X)</a>
				</div>
			  <#-- 工具条结束 -->	
        </div>
	
		
	</div>

</body>
</html>