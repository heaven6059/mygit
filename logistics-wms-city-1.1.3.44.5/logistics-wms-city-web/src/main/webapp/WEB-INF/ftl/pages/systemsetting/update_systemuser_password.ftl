<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网络营销系统-系统管理-系统用户密码修改</title>
<#include  "/WEB-INF/ftl/common/header.ftl" >
<script type="text/javascript">
	
  	
  	$(function(){
  	    
  		//如果标识该页面关闭
		<#if closeFlag??&&closeFlag=='1'>
			ygdg.dialog.confirm("用户角色更新成功，您确认关闭退出吗？",function(){
				closewindow();//关闭退出
			});
		</#if>
	});
</script>
</head>
<body>
<div class="container">
	<div class="toolbar">
		<div class="t-content">
		</div>
	</div>
	<div class="list_content">
		<div class="top clearfix">
			<ul class="tab">
				<li id="orderAll" class='curr' ><span>密码修改</span></li>
			</ul>
		</div>
		<div class="modify" id="modify">
			<form action="u_updateSystemUserPassword.htm" method="post"  name="systemUserForm1" id="systemUserForm1" style="margin:0px;padding:0p;">
   				<script>document.write("<input type='hidden' name='parentSourcePage' value='"+getThickBoxUrl()+"'/>");</script>
	   		 	<#if warnMessagekey?? >
	            	<div class="warning-show warn-case ft-cl-n ft-sz-12" style="text-align:left;padding-left:5px;">
		            	<img src="${BasePath}/images/yitiansystem/war-stop.gif" alt="" />
		            	<@spring.message code="${warnMessagekey}" />
	            	</div>
	        	<#else>
	        		<div class="warning-show"></div>
	           	</#if>
	            <div>
	            <div class="add_detail_box">
	            	<input type="hidden" name="userid" id="userid"  value="${userid?default('')}" />
	            	${closeFlag}
					<p>
						<span>
	            			<label>新密码：</label>
			       			<input name="newPassword" type="password" class="inputtxt" id="newPassword" maxLength="50"/>
			       			<div class="tip" id="newPasswordTip" ></div>
			       		</span>
			       	</p>
			       	<p>
			       		<span>
	            			<label>确认密码：</label>
			       			<input type="password" id="checknewPassword" class="inputtxt" maxLength="50"/>
			       		</span>
			       	</p>
			       	<p>
			       		<span>
	            			<label>&nbsp;&nbsp;&nbsp;&nbsp;</label>
	            			<input type="submit" class="btn-add-normal"  value="保存" />  
				  			<input type="button" class="btn-add-normal" value="取消" onclick="closewindow();"/>
			       		</span>
			       	</p>
	            </div>
	        </form>
		</div>
	</div>
</div>

</body>
</html>
