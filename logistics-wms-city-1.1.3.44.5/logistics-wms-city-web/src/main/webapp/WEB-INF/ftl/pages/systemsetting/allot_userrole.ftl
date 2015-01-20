<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include  "/WEB-INF/ftl/common/header.ftl" >
<title>无标题文档</title>
<style type="text/css">
	#select1,#select2{width:158px; margin:-4px;padding:5px;}
	.sel-div{border:1px solid #ccc;width:150px; overflow:hidden;}
	.a-up,.a-down{display: block;border: 1px solid #ccc;text-decoration:none;width:150px; background:#F2F2F2; text-align:center; height:25px; line-height:25px;cursor:pointer; letter-spacing:2px; zoom:1;}
	.a-up{border-bottom:none;}
	.a-down{border-top:none;}
	.a-right,.a-left{display:block; border:1px solid #ccc; width:100px; height:25px; line-height:25px; background:#F2F2F2;margin:15px; cursor:pointer;}
	.a-left{margin-top:20px;}
</style>
<script type="text/javascript">
	$(function(){
  		//如果标识该页面关闭
		<#if closeFlag??&&closeFlag=='1'>
			ygdg.dialog.confirm("用户角色更新成功，您确认关闭退出吗？",function(){
				closewindow();//关闭退出
			});
		</#if>
	});

	/**
	 * 提交表单
	 */
	function mysubmit(){
		if($("#select1 > option").length == 0){
			$("#select1").append("<option value='null-role'></option>");
		}
		$("#select1 > option").attr("selected",true);
		document.allotRoleForm.submit();
	}
</script>
</head>
<body>
	<div class="container">
		<div class="toolbar">
			<div class="t-content">
				<div class="btn">
					<span class="btn_l"></span>
		        	<b class="ico_btn add"></b>
		        	<span class="btn_txt"><a href="#" onclick="closewindow();">返回</a></span>
		        	<span class="btn_r"></span>
	        	</div> 
			</div>
		</div>
		<div class="list_content">
			<div class="top clearfix">
				<ul class="tab">
					<li class='curr' ><span>用户角色分配</span></li>
				</ul>
			</div>
		 	<div class="modify">  
			 	<form action="u_allotUserRole.htm" name="allotRoleForm" id="allotRoleForm" method="post" style="padding:0px;margin:0px;">
		      		<script>document.write("<input type='hidden' name='parentSourcePage' value='"+getThickBoxUrl()+"'/>");</script>
		      		<input  type="hidden" name="id" value="${userid!''}" />
		      		<div style="padding-left:20px;padding-right:20px;">
			        <table cellpadding="0" cellspacing="0">
			            <tbody>
			            	<tr>
			            		<td align="center" style="height:20px;">所有角色</td>
			            		<td></td>
			            		<td align="center">已拥有角色</td>
			            	</tr>
			                <tr>
				                <td valign="top">
									<a id="right_up" class="a-up">上移</a>
				               		<div class="sel-div">
				                		<select multiple id="select2" size="30">
				                			<#if notAllotList??>
				                				<#list notAllotList as role>
				                					<#if role.roleId??&&role.roleName??>
				                						<option value="${role.roleId}">${role.roleName}</option>
				                					</#if>
				                				</#list>
				                			</#if>
				                		</select>
				                	</div>
				                	<a id="right_down" class="a-down">下移</a>
				                </td>
				                <td align="center" valign="middle">
				                	<a id="remove" class="a-left">增加角色&gt;&gt;</a>
				                	<a id="add" class="a-right">&lt;&lt; 删除角色</a>
				                </td>
				                <td>
				                	<a  id="left_up" class="a-up">上移</a>
				                	<div class="sel-div">
				               			<select  name="allowRoleValues" size="30" multiple id="select1" >
				                			<#if myRoleList??>
				                				<#list myRoleList as myRole>
				                					<option value="${myRole.roleId}">${myRole.roleName}</option>
				                				</#list>
				                			</#if>
				                		</select>
				               	 	</div>
				                	<a  id="left_down" class="a-down">下移</a>
				                </td>
			                </tr>	
			                <tr>
			                	<td colspan="3" align="center">
			                		<input  type="button" onclick="mysubmit()" class="btn-add-normal" value="保存" />   
			                		<input type="button" onclick="closewindow();" class="btn-add-normal" value="取消" />
			                	</td>
			                </tr>
			            </tbody>
			        </table>
			        </div>
		        </form>
		  	</div>
		</div>
	</div>
</body>
</html>

