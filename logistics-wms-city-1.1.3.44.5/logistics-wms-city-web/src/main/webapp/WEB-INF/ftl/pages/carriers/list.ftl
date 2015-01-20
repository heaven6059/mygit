<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>承运商管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="<@s.url "/resources/js/baseinfo/carriers.js"/>"></script>
<script>
  $(window).resize(function(){
   
    $('#dataGridJG').datagrid('resize', {
        width:function(){return document.body.clientWidth;},
    });
 
    
    $('#queryConditionDiv').panel('resize',{
	    width:function(){return document.body.clientWidth;},
   });

    
    $('#toolbarEdit').panel('resize',{
	    width:function(){return document.body.clientWidth;},
    });
    
   });

</script>	
</head>
<body >
<div>
         <#-- 工具菜单div 
         <div style="margin-bottom:0px" id="toolDiv">
			   <@p.toolbar   listData=[
							 {"id":"btn-close","title":"导出","iconCls":"btn-export","action":"carriers.exportExcel();","type":5},
					         {"id":"btn-close","title":"关闭","iconCls":"btn-close","action":"closeWindow('承运商管理');","type":0}
	                       ]
				  />	
		 </div>
		 -->	
	     <div>
	            <#-- 查询面板div -->
	       	<div id="carriersSearchDiv" style="padding:10px;">
	       		 <form name="searchForm" id="searchForm" method="post">
				   	所属品牌库：<input class="easyui-combobox" style="width:110px" name="sysNo" id="sysNoCondition" />
				    &nbsp;承运商编码：<input class="easyui-validatebox" style="width:110px" name="supplierNo" id="supplierNoCondition" />
					&nbsp;承运商名称：<input class="easyui-validatebox" style="width:110px" name="supplierName" id="supplierNameCondition" />
					<!--&nbsp;检索码：<input class="easyui-validatebox" style="width:110px" name="searchCode" id="searchCodeCondition" />
					<input type='hidden' name="businessType" type="hidden" value="8" style="display:none;"/>-->
				 	<a id="searchBtn" href="javascript:carriers.searchCarriers();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a><br/><br/>
				 	承运商类型：<input class="easyui-combobox" style="width:110px" name="supplierType" id="supplierTypeCondition"/>
					&nbsp;承运商状态：<input class="easyui-combobox" style="width:110px" name="supplierStatus" id="supplierStatusCondition" />
				 	<a id="clearBtn" style="padding-left:195px;" href="javascript:carriers.searchCarriersClear();" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">清除</a>
				</form>
			 </div>
       			
              <#-- 数据列表div -->
	          <div>
          	            <@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""  defaultColumn=""   title="承运商列表"
		                   isHasToolBar="false"  divToolbar="#carriersSearchDiv" height="320"  onClickRowEdit="false"    pagination="true"
			               rownumbers="true"
			               columnsJsonList="[
			                  {field : 'supplierNo',title : '承运商编码',width : 80},
			                  {field : 'sysNoStr',title : '所属品牌库',width : 80},
			                  {field : 'supplierCode',title : '承运商操作码',width : 80},
			                  {field : 'searchCode',title : '检索码',width : 80},
			                  {field : 'supplierName',title : '承运商名称',width : 150},
			                  {field : 'supplierLname',title : '承运商全称',width : 150},
			                  {field : 'supplierEname',title : '承运商英文名',width : 120},
			                  {field : 'supplierStatusStr',title : '状态',width : 60},
			                  {field : 'supplierTypeStr',title : '承运商类型',width : 80},
			                  {field : 'bizType',title : '经营类型',width : 80},
			                  {field : 'businessTypeStr',title : '经营性质',width : 80},
			                  {field : 'cman',title : '联系人',width : 80},
			                  {field : 'cmanPhone',title : '联系人电话',width : 80},
			                  {field : 'manager',title : '负责人',width : 80},
			                  {field : 'phone',title : '电话',width : 80},
			                  {field : 'identityCard',title : '客户代码证号',width : 150},
			                  {field : 'chairman',title : '法人代表',width : 80},
			                  {field : 'telno',title : '联系电话',width : 80},
			                  {field : 'faxno',title : '公司传真',width : 80},
			                  {field : 'zoneNoStr',title : '所属地区',width : 80},
			                  {field : 'zipno',title : '邮政编码',width : 80},
			                  {field : 'address',title : '地址',width : 150},
			                  {field : 'bankName',title : '开户银行',width : 150},
			                  {field : 'bankAccount',title : '银行账户名',width : 150},
			                  {field : 'bankAccname',title : '银行账号',width : 150},
			                  {field : 'taxpayingNo',title : '纳税号',width : 150},
			                  {field : 'taxLevelStr',title : '纳税级别',width : 100},
			                  {field : 'supplierCardNo',title : '结算卡号',width : 150},
			                  {field : 'remarks',title : '备注',width : 200},
				          	  {field : 'creator',title : '建档人',width : 60},
				          	  {field : 'createtm',title : '建档时间',width : 125,sortable:true},
				          	  {field : 'editor',title : '修改人',width : 60},
				          	  {field : 'edittm',title : '修改时间',width : 125,sortable:true}
			                 ]" 
			                  
				           jsonExtend='{onSelect:function(rowIndex, rowData){
		                            // 触发点击方法  调JS方法
		                            carriers.loadDetail(rowData.supplierNo);
		                   }}'/>
	          </div>
	          
	           <form name="dataForm" id="dataForm" method="post">
                <#-- 明细信息div -->
	           <div id="toolbarEdit" class="easyui-panel" style="padding:10px;background:#F4F4F4;height:350"  title="明细信息" >
				  <#-- 
				     承运商编码： &nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="supplierNo" id="supplierNo" required="true" missingMessage='承运商编码为必填项!' />
				      承运商类型：<input class="easyui-combobox" style="width:110px" name="supplierType" id="supplierType"  required="true" missingMessage='请选择承运商类型!' />
				      承运商操作码：<input class="easyui-validatebox" style="width:110px" name="supplierCode" id="supplierCode" required="true" missingMessage='承运商操作码为必填项!' />
				      检索码：&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="searchCode" id="searchCode"/></br></br>
				      
				      承运商名称：&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="supplierName" id="supplierName" required="true" missingMessage='承运商名称为必填项!' />
				      状态：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-combobox" style="width:110px" name="supplierStatus" id="supplierStatus" required="true" missingMessage='请选择状态!'  />
				      经营类型：&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="bizType" id="bizType" readOnly="true" value="0"/>
				      经营性质：<input class="easyui-combobox" style="width:110px" name="businessType" id="businessType"  required="true" missingMessage='请选择经营性质!' /></br></br>
				      
				      承运商全称：&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="supplierLname" id="supplierLname"/>
				      承运商英文名：<input class="easyui-validatebox" style="width:110px" name="supplierEname" id="supplierEname"/></br></br>
				      
				      联系人：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="cman" id="cman"/>
				      负责人：&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="manager" id="manager"/>
				      联系人电话：&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="cmanPhone" id="cmanPhone"/>
				      电话：&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="phone" id="phone"/></br></br>
				      
				      客户代码证号：<input class="easyui-validatebox" style="width:110px" name="identityCard" id="identityCard"/>
				      法人代表：&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="chairman" id="chairman"/>
				      联系电话：&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="telno" id="telno"/>
				      公司传真：<input class="easyui-validatebox" style="width:110px" name="faxno" id="faxno"/></br></br>
				      
				      所属地区：&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-combobox" style="width:110px" name="zoneNo" id="zoneNo"  />
				      邮政编码：&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="zipno" id="zipno"/>
				      地址：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="address" id="address"/></br></br>
				      
				      开户银行：&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="bankName" id="bankName"/>
				      银行账户名：<input class="easyui-validatebox" style="width:110px" name="bankAccount" id="bankAccount"/>
				      银行账号：&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="bankAccname" id="bankAccname"/></br></br>
				     
				      纳税号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="taxpayingNo" id="taxpayingNo" required="true" missingMessage='纳税号为必填项!' />
				      纳税级别：&nbsp;&nbsp;<input class="easyui-combobox" style="width:110px" name="taxLevel" id="taxLevel"  required="true" missingMessage='请选择纳税级别!' />
				      结算卡号：&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="supplierCardNo" id="supplierCardNo"/></br></br>
				      所属品牌库：&nbsp;&nbsp;<input class="easyui-combobox" style="width:110px" name="sysNo" id="sysNo"  required="true" missingMessage='请选择所属品牌库!' />
				      备注：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-validatebox" style="width:110px" name="remarks" id="remarks"/>
				 -->
				 
				 <table cellspacing='0' cellpadding='0' >
				 	<tr height='33'>
				 		<td align='left' width='85'>承运商编码：</td>
				 		<td width = '118'><input class="easyui-validatebox" style="width:110px" name="supplierNo" id="supplierNo" required="true" missingMessage='承运商编码为必填项!'   data-options="validType:['vnChinese[\'承运商编码不能包含中文\']','vLength[0,10,\'最多只能输入10个字符\']']" readOnly="true"/></td>
				 		<td align='left' width='75'>所属品牌库：</td>
				 		<td width = '118'><input class="easyui-combobox" style="width:110px" name="sysNo" id="sysNo"  readOnly="true"/></td>
				 		<td align='left' width='85'>承运商操作码：</td>
				 		<td width = '118'><input class="easyui-validatebox" style="width:110px" name="supplierCode" id="supplierCode" required="true" missingMessage='承运商操作码为必填项!'   data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']"  readOnly="true"/></td>
				 		<td align='left' width='75'>检索码：</td>
				 		<td width = '118'><input class="easyui-validatebox" style="width:110px" name="searchCode" id="searchCode"   data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']"   readOnly="true"/></td>
				 	</tr>
				 	
				 	<tr height='33'>
				 		<td align='left'>承运商名称：</td>
				 		<td colspan='3'><input class="easyui-validatebox" style="width:302px" name="supplierName" id="supplierName" required="true" missingMessage='承运商名称为必填项!'    data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>承运商全称：</td>
				 		<td colspan='3'><input class="easyui-validatebox" style="width:303px" name="supplierLname" id="supplierLname"   data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"  readOnly="true"/></td>
				 	</tr>
				 	
				 	<tr height='33'>
				 		<td align='left'>承运商英文名：</td>
				 		<td colspan='3'><input class="easyui-validatebox" style="width:302px" name="supplierEname" id="supplierEname"   data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>状态：</td>
				 		<td><input class="easyui-combobox" style="width:110px" name="supplierStatus" id="supplierStatus" readOnly="true"/></td>
				 		<td align='left'>承运商类型：</td>
				 		<td><input class="easyui-combobox" style="width:110px" name="supplierType" id="supplierType"  readOnly="true"/></td>
				 	</tr>
				 	
				 	<tr height='33'>
				 		<td align='left'>联系人：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="cman" id="cman"   data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>负责人：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="manager" id="manager"   data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>联系人电话：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="cmanPhone" id="cmanPhone"   data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>电话：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="phone" id="phone"   data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  readOnly="true"/></td>
				 	</tr>
				 	
				 	<tr height='33'>
				 		<td align='left'>客户代码证号：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="identityCard" id="identityCard"   data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>法人代表：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="chairman" id="chairman"   data-options="validType:['vLength[0,20,\'最多只能输入20个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>联系电话：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="telno" id="telno"   data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>公司传真：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="faxno" id="faxno"   data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  readOnly="true"/></td>
				 	</tr>
				 	
				 	<tr height='33'>
				 		<td align='left'>所属地区：</td>
				 		<td><input class="easyui-combobox" style="width:110px" name="zoneNo" id="zoneNo"    readOnly="true"/></td>
				 		<td align='left'>邮政编码：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="zipno" id="zipno"   data-options="validType:['vLength[0,10,\'最多只能输入10个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>地址：</td>
				 		<td colspan='3'><input class="easyui-validatebox" style="width:303px" name="address" id="address"   data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"  readOnly="true"/></td>
				 	</tr>
				 	
				 	<tr height='33'>
				 		<td align='left'>开户银行：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="bankName" id="bankName"   data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>银行账户名：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="bankAccount" id="bankAccount"   data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>银行账号：</td>
				 		<td colspan='3'><input class="easyui-validatebox" style="width:303px" name="bankAccname" id="bankAccname"   data-options="validType:['vLength[0,64,\'最多只能输入64个字符\']']"  readOnly="true"/></td>
				 	</tr>
				 	
				 	<tr height='33'>
				 		<td align='left'>纳税号：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="taxpayingNo" id="taxpayingNo" required="true" missingMessage='纳税号为必填项!'    data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  readOnly="true"/></td>
				 		<td align='left'>纳税级别：</td>
				 		<td><input class="easyui-combobox" style="width:110px" name="taxLevel" id="taxLevel"  readOnly="true"/></td>
				 		<td align='left'>结算卡号：</td>
				 		<td colspan='3'><input class="easyui-validatebox" style="width:303px" name="supplierCardNo" id="supplierCardNo"   data-options="validType:['vLength[0,32,\'最多只能输入32个字符\']']"  readOnly="true"/></td>
				 	</tr>
				 	
				 	<tr height='33'>
				 		<td align='left'>经营类型：</td>
				 		<td><input class="easyui-validatebox" style="width:110px" name="bizType" id="bizType" readOnly="true" value="0"/></td>
				 		<td align='left'>经营性质：</td>
				 		<td><input class="easyui-combobox" style="width:110px" name="businessType" id="businessType"   readOnly="true"/></td>
				 		<td align='left'>备注：</td>
				 		<td colspan='3'><input class="easyui-validatebox" style="width:303px" name="remarks" id="remarks"   data-options="validType:['vLength[0,255,\'最多只能输入255个字符\']']"  readOnly="true"/></td>
				 	</tr>
				 	
				 </table>
				 
				</div>
		 </form>	   
	    </div>

</div>


</body>
</html>