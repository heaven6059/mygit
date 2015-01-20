<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<#include  "/WEB-INF/ftl/common/header.ftl" >
<script type="text/javascript">
	//修改密码
	function updatePassword(id){
		var param = "id="+id;
		openwindow("toUpdateSystemUserPassword.htm?id="+id,450,250,"密码修改");
	}
	/**
	 *到用户角色分配 toAddSystemUser.htm target="mbdif"
	 * @param id
	 */
	function allotUserRoles(id){
		var params = "id="+id;
		openwindow("toAllotUserRole.htm?id="+id,520,650,"分配角色");
	}
	function toAdd(){
	   var tab = parent.$('#centerFrame').tabs('getSelected');    
       parent.$('#centerFrame').tabs('update', {
		tab: tab,
		options: {
			content:' <iframe src="'+BasePath+'/toAddSystemUser.htm" frameborder="0"  border="0" marginwidth="0" marginheight="0" scrolling="no"   style="width:100%" height="600px"></iframe> '
		}
	  });
	}
	
   function toUpdateSystemUser(userid){
       var url=BasePath+'/toUpdateSystemUser.htm?id='+userid;
       alert(url);
	   var tab = parent.$('#centerFrame').tabs('getSelected');    
       parent.$('#centerFrame').tabs('update', {
		tab: tab,
		options: {
			content:' <iframe src="'+url+'" frameborder="0"  border="0" marginwidth="0" marginheight="0" scrolling="no"   style="width:100%" height="600px"></iframe> '
		}
	  });
	}
	
</script>
</head>
<body>
<#assign systemUser=login_system_user?default('') >
<div class="container">
	<div class="toolbar">
		<div class="t-content">
			<div class="btn">
				<span class="btn_l"></span>
	        	<b class="ico_btn add"></b>
	        	<span class="btn_txt"><a href="javascript:toAdd()" >增加</a></span>
	        	<span class="btn_r"></span>
        	</div> 
		</div>
	</div>
	<div class="list_content">
		<div class="top clearfix">
			<ul class="tab">
				<li class='curr' ><span>系统用户列表</span></li>
			</ul>
		</div>
		<div class="modify">
			<div class="add_detail_box">
				<form method="post" action="querySystemUserList.htm" name="queryForm" id="queryForm"  >
					<span>真实姓名：</span>
					<input type="text" name="username" />
					<span>登录用户名：</span>
					<input type="text" name="loginName" />
				
					
					<input type="submit" value="查询" class="btn-add-normal"/>
				</form>
			</div>
			<table cellpadding="0" cellspacing="0" class="list_table">
			    <thead>
	            	<tr>
			            <th>真实姓名 </th>
			            <th>登录用户名</th>
			            <th>性别 </th>
			            <th>手机</th>
			            <th>部门</th>
			            <th>电话</th>
			            <th>Email</th>
			            <th>QQ</th>
			            <th>状态</th>
			            <th>操作</th>
		            </tr>              
	            </thead>
	            <tbody>
	              <#if systemUserList??  >
		      		<#list systemUserList as item>		
		      		<tr id='Tr${item.userid}'>
	                    <td>
							${item.username?default("")}                 
	                    </td>
	                    <td>
	                    	${item.loginName?default("")}
	                    </td>
	                    <td>
	                    	<#if (item.sex == "0")>
								男
							<#elseif (item.sex == "1") >
								女
							</#if>
	                    </td>
	                    <td>
	                    	${item.telPhone?default("")}
	                    </td>
	                    
	                     <td>
	                    	${item.organizName?default("")}
	                    </td>
	                    <td>
		                   ${item.mobilePhone?default("")}
	                    </td>
	                    <td>
	                    	${item.email?default("")}
	                    </td>
	                    <td>
	                    	${item.qqNum?default("")}
	                    </td>
	                    <td>
	                    	<span id="State${item.id}">
	                    	<#if item.state?? && (item.state == "1")>
								正常
							<#elseif item.state?? && (item.state == "2") >
								锁定
							<#elseif item.state?? && (item.state == "3") >
								逻辑删除
							<#else>
								${item.state?default("")}
							</#if>
							</span>
	                    </td>
	                    
	                    </td>
	                    <td class="td0" style="text-align:left;">
	                    	<#if item.state?? && (item.state != "3")>
	                    	<a href="javascript:toUpdateSystemUser('${item.userid}');"  style="padding-right: 5px;padding-left: 5px;">编辑</a>
	                    	<#if (item.level)?? && (item.level == "0")>
	           					<#if (systemUser != '')  && (systemUser.level)?? && (systemUser.level == "0" )>
	           						<a href="javascript:updatePassword('${item.userid}');"  style="padding-right: 5px;">修改密码</a>
	           					</#if>
							<#else>
								<a href="javascript:removeSystemUser('${item.userid}');"  style="padding-right: 5px;">删除</a>
								<a href="javascript:allotUserRoles('${item.userid}');"  style="padding-right: 5px;">分配角色</a>
								<a href="javascript:updatePassword('${item.userid}');"  style="padding-right: 5px;">修改密码</a>
							</#if>
							
							
	                    	</#if>
	                    </td>
	                </tr>
		      		</#list>	
		      	<#else>
		      		<tr><td colspan="11" style="text-align:center;"><div class="yt-tb-list-no">暂无内容</div></td></tr>
				</#if>
	            </tbody>
		    </table> 
		</div>
		
	</div>
</div>

</body>
</html>