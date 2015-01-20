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
	$.extend($.fn.validatebox.defaults.rules, {    
    equals: {    
        validator: function(value,param){    
            return value == $(param[0]).val();    
        },    
        message: '两次密码不一样'   
    }    
}); 
	
	function updatePassword(){
	
	       var flag= $('#systemUserForm1').form('validate');
    
		    if(flag==false){
		      return ;
		    }
		var url=BasePath+'/login_updateSystemUserPassword.htm';    
		$.ajax({
		  type: 'POST',
		  url: url,
		  data: {'newPassword':$('#newPassword').val(),'oldPassword':$('#oldPassword').val()},
		  cache: true,
		  async:false, // 一定要
		  success: function(totalData){
			  if(totalData=='success'){
			      alert('操作成功!');
			  }else  if(totalData=='fail'){
			      alert('旧密码输入错误!');
			  }else if(totalData=='error'){
			     alert('操作失败!');
			  }
		  }
     });
	}
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
	   		 	
	            <div>
	            <div class="add_detail_box">
	            	
	            	
	            	
	            	<p>
						<span>
	            			<label>旧密码：</label>
			       			<input name="oldPassword" type="password"  id="oldPassword" maxLength="50" class="easyui-validatebox" missingMessage='不能为空!' required="true" />
			       			<div class="tip" id="newPasswordTip" ></div>
			       		</span>
			       	</p>
					<p>
						<span>
	            			<label>新密码：</label>
			       			<input name="newPassword" type="password"  id="newPassword" maxLength="50" class="easyui-validatebox" missingMessage='不能为空!' required="true"/>
			       			<div class="tip" id="newPasswordTip" ></div>
			       		</span>
			       	</p>
			       	<p>
			       		<span>
	            			<label>确认密码：</label>
			       			<input type="password" id="checknewPassword"  maxLength="50" class="easyui-validatebox" missingMessage='不能为空!' required="true" validType="equals['#newPassword']" />
			       		</span>
			       	</p>
			       	<p>
			       		<span>
	            			<label>&nbsp;&nbsp;&nbsp;&nbsp;</label>
	            			<input type="button" class="btn-add-normal"  value="保存"  onclick='updatePassword()'/>  
			       		</span>
			       	</p>
	            </div>
	        </form>
		</div>
	</div>
</div>

</body>
</html>
