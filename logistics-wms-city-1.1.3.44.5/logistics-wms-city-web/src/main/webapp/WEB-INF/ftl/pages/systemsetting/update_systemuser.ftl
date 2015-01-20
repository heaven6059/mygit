<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网络营销系统-系统管理-系统用户基本信息</title>
<#include  "/WEB-INF/ftl/common/header.ftl" >
<script type="text/javascript">
	$(function(){
		//页面加载完后启动验证
		$("#systemUserForm").validation();
	})

	//修改密码
	function updatePassword(id){
		var param = "id="+id;
		openwindow("../../systemmgmt/systemuser/toUpdateSystemUserPassword.sc?id="+id,450,250,"密码修改");
	}
	//选择用户组织客户
	function toSelectUserOrganiz(){
		openwindow("../../systemmgmt/organiz/toSelectUserOrganiz.sc",550,450,"选择用户组织客户");
	}
</script>
<script type="text/javascript">
    function toList(){
       var tab = parent.$('#centerFrame').tabs('getSelected');    
       parent.$('#centerFrame').tabs('update', {
		tab: tab,
		options: {
			content:'<iframe src="'+BasePath+'/querySystemUserList.htm" frameborder="0"  border="0" marginwidth="0" marginheight="0" scrolling="no"   style="width:100%" height="600px"></iframe> '
		}
	  });
    }
    
    function init(){
       $('#storeNo').combogrid({
		 panelWidth:450,   
         idField:'storeNo',  
         textField:'storeName',   
         pagination : true,
         rownumbers:true,
         mode: 'remote',
         url:BasePath+'/store/list.json', 
         columns:[[  
             {field:'storeNo',title:'客户编号',width:140},  
             {field:'storeName',title:'客户名称',width:140}  
         ]]  
	   });
    }
</script>
</head>
<body onload="init();">
<div class="container">
	<div class="toolbar">
		<div class="t-content">
			<div class="btn">
				<span class="btn_l"></span>
	        	<b class="ico_btn add"></b>
	        	<span class="btn_txt"><a href="javascript:toList()">返回</a></span>
	        	<span class="btn_r"></span>
        	</div> 
		</div>
	</div>
	<div class="list_content">
		<div class="top clearfix">
			<ul class="tab">
				<li id="orderAll" class='curr' ><span>修改系统用户基本信息</span></li>
			</ul>
		</div>
		<div class="modify" id="modify">
			<form action="u_updateSystemUser.htm" method="post" name="systemUserForm" id="systemUserForm" style="margin:0px;padding:0p;">
				<div class="add_detail_box">
					<p>
						<span>
			       			<input type="hidden" name="userid" value="${user.userid?default('')}" />
			       			<label for="username">真实姓名：<font class="ft-cl-r">*</font>&nbsp;</label>
		       				<input name="username" type="text" id="username" value="${user.username?default('')}" size="30"  maxLength="40" class="validate[required]" data-rel="真实姓名不能为空！"/>
		       				
	       				</span>
			       	</p>
			       	<p>
			       		<span>
			       			<label for="loginName">登录用户名：<font class="ft-cl-r">*</font>&nbsp;</label>
			       			<input name="loginName" type="text" id="loginName" readonly="readonly" value="${user.loginName?default('')}" size="30"  maxLength="50" class="validate[required]" data-rel="登录用户名不能为空！"/>
			       		</span>
			       	</p>
			       	<p>
			       		<span>
			       			<label for="loginPassword">登录密码：&nbsp;</label>
			       			<input type="password" value="111111" readonly="readonly" class="inputtxt" maxLength="50"/>
			       		</span>
			       	</p>
			       
				    
			       	 
			       	 
		       		
			       	 <p>
			       	 	<span>
			       	 		<label for="sex">性别：&nbsp;&nbsp;&nbsp;</label>
		       				<#if (user.sex == "0")>
								<input type="radio"  name="sex" value="0" checked/> 男
								&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="radio"  name="sex" value="1"/> 女
							<#elseif (user.sex == "1") >
								<input type="radio"  name="sex" value="0"/> 男
								&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="radio"  name="sex" value="1" checked/> 女
							</#if>
			       		</span>
			       	</p>
			       	<p>
			       		<span>
			       			<label for="telPhone">手机：&nbsp;&nbsp;&nbsp;</label>
		       			  	<input name="telPhone" type="text" id="telPhone" size="30"  value="${user.telPhone?default('')}"  maxLength="50"/>
			       		</span>
		       		</p>
			       	<p>
			       		<span>
			       			<label for="mobilePhone">电话：&nbsp;&nbsp;&nbsp;</label>
		       			 	<input name="mobilePhone" type="text" id="mobilePhone" size="30" value="${user.mobilePhone?default('')}"  maxLength="50"/>
			       		</span>
		       		</p>
			       	<p>
			       		<span>
			       			<label for="email">E-mail：&nbsp;&nbsp;&nbsp;</label>
		       			 	<input name="email" type="text" id="email" size="30"  value="${user.email?default('')}"  maxLength="50" class="validate[email]" />
			       		</span>
		       		</p>
			       	<p>
			       		<span>
			       			<label for="qqNum">QQ：&nbsp;&nbsp;&nbsp;</label>
		       			  	<input name="qqNum" type="text" id="qqNum" size="30" value="${user.qqNum?default('')}"  maxLength="15"/>
			       		</span>
		       		</p>
			       	<p>
			       		<span>
			       			<label for="msnNum">MSN：&nbsp;&nbsp;&nbsp;</label>
		       			  	<input name="msnNum" type="text" id="msnNum" size="30"  value="${user.msnNum?default('')}"  maxLength="50"/>
			       		</span>
		       		</p>
			       	<p>
			       		<span>
			       			<label for="msnNum">所属客户：<font color="red">*</font>&nbsp;</label>
			       		    <input class="easyui-combogrid" style="width:200px" name="storeNo" id="storeNo" value="${user.storeNo?default('')}" />
			       		</span>
		       		</p>
		       		<p>
			       		<span>
			       			<label>&nbsp;</label>
			       			<input type="submit" class="btn-add-normal" value="保存" />  
							<input type="button" class="btn-add-normal" value="取消" onclick="toList();" />
			       		</span>
		       		</p>
				</div>
			</form>
		</div>
	</div>
</div>

</body>
</html>
