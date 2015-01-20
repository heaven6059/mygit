<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>B网络营销系统-系统管理-角色</title>
<#include  "/WEB-INF/ftl/common/header.ftl" >

</head>
<body>

<div class="container">
	<div class="toolbar">
		<div class="t-content">
			<div class="btn" onclick="javascript:toAddRole()">
				<span class="btn_l"></span>
	        	<b class="ico_btn add"></b>
	        	<span class="btn_txt">添加角色</span>
	        	<span class="btn_r"></span>
        	</div> 
		</div>
	</div>
	<div class="list_content">
		<div class="top clearfix">
			<ul class="tab">
				<li class='curr' ><span>角色列表</span></li>
			</ul>
		</div>
		<div class="modify">
			<div class="add_detail_box">
				<form action="queryRoleList.htm" name="queryForm" id="queryForm" method="post">
					<p>
						<span>
							<label>角色名称：</label>
							<input name="roleName" class="inputtxt" />
						</span>
						<span>
							<label>菜单名称：</label>
							<input name="menuName" class="inputtxt" />
						</span>
						<span>
							<input type="submit" class="btn-add-normal-4ft">
						</span>
					</p>
				</form>
			</div>
			<table cellpadding="0" cellspacing="0" class="list_table">
			    <thead>
			    <tr>
				    <th>角色名称</th>
				    <th>创建时间</th>
				    <th>备注</th>
				    <th>操作</th>
			    </tr>              
			    </thead>
			    <tbody>
			    
			     <#if roleList??  >
			  		<#list roleList as item>
			  		<tr>
			            <td>
							${item.roleName?default('')}               
			            </td>
			            <td>
							<#if item.roleCreatedate ??>
								${item.roleCreatedate?string("yyyy-MM-dd")}
							</#if>
						</td>
			            <td>${item.remark?default('')}</td>
			            <td class="td0">
			        			<a href="javascript:toUpdateRole('${item.roleId}');" >编辑</a>
							<a href="javascript:removeRole('${item.roleId}');" >删除</a>
							<a href="javascript:allotRoleResource('${item.roleId}');" >分配资源</a>
							
			            </td>
			        </tr>
			  		</#list>	
			  	
			  	<#else>
			  		<tr><td colspan="4"><div class="yt-tb-list-no" style="text-align:center;">暂无内容</div></td></tr>
				</#if>
			  		
			    </tbody>
		    </table> 
		</div>
		
	</div>
</div>

</body>
</html>

