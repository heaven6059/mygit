<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include  "/WEB-INF/ftl/common/header.ftl" >
<style type="text/css">
	#select1,#select2{width:158px; margin:-4px;padding:5px;}
	.sel-div{border:1px solid #ccc;width:150px; overflow:hidden;}
	.a-up,.a-down{display: block;border: 1px solid #ccc;text-decoration:none;width:150px; background:#F2F2F2; text-align:center; height:25px; line-height:25px;cursor:pointer; letter-spacing:2px; zoom:1;}
	.a-up{border-bottom:none;}
	.a-down{border-top:none;}
	.a-right,.a-left{display:block; border:1px solid #ccc; width:100px; height:25px; line-height:25px; background:#F2F2F2;margin:15px; cursor:pointer;}
	.a-left{margin-top:20px;}
</style>
<script type="text/javascript" src="<@s.url "/resources/js/menus.js"/>"></script>	
	<title></title>
	<script type="text/javascript">
	  	var config={form:"resourceMgtForm",submit:submitForm,
	 	fields:[
			{name:'menuName',allownull:false,regExp:"notempty",defaultMsg:'请输入资源名称',focusMsg:'请输入资源名称',errorMsg:'资源名称格式不正确',rightMsg:'',msgTip:'menuNameTip'},
			{name:'sort',allownull:false,regExp:"num4",
				defaultMsg:'请输入排序位置',focusMsg:'请输入排序位置',errorMsg:'排序只能为数字',rightMsg:'',msgTip:'sortTip'}
		]}
	  
	  	Tool.onReady(function(){
			var f = new Fv(config);
			f.register();
		});
	  	
	  	/**
	  	 * 提交表单
	  	 */
	  	function submitForm(result){
	  		if(result){
	  			addMenuNode();
	  		}
	  		return false;
	  	}
	</script>
</head>

<body>
	<div class="contentMain">
		<table>
			<tr>
				<td style="vertical-align: top;padding:10px;" align="left" width="250px;" height="100%">
					<ul id="resourceTree"><script>onloadResourceTree('<@s.url "/authority_menu/tree.json"/>',false);</script></ul>
				</td>
				<td>&nbsp;&nbsp;</td>
				<td style="padding: 15px;vertical-align: top;">
				<form action="post" name="resourceMgtForm" method="post">
					<table>
						<tr>
							<td style="text-algin:right;height:60px;"><input type="hidden" name="menuId" id="id"/>名称：</td>
							<td>
								<input type="text" class="inputtxt" name="menuName" id="name" size="80"  maxLength="14"/>
								<input type="hidden" name="menuType" value="0"/>
							</td>
						</tr>
						<!--
						<tr>
							<td style="text-algin:right;height:60px;">URL：</td>
							<td><input type="text" class="inputtxt" name="memuUrl" id="memuUrl" size="80" maxLength="150"/></td>
						</tr>
						<tr>
							<td style="text-algin:right;height:60px;">类型：</td>
							<td>
								<select name="type" id="type" style="width: 100px;">
									<option value="0" selected="selected">菜单</option>
									<option value="1">功能点</option>
								 </select>
							</td>
						</tr>
						<tr style="display:none">
							<td style="text-algin:right;height:60px;">所属平台：</td>
							<td>
								<select name="flag" id="flag" style="width: 100px;">
									<option value="all">全部后台</option>
									<option value="mobile">手机后台</option>
								 </select>
							</td>
						</tr>
						-->
						<tr>
							<td style="text-algin:right;height:60px;">排序：</td>
							<td>
								<input type="text" class="inputtxt" name="menuSort" id="sort" size="80"  maxLength="5"/>
							</td>
						</tr>
						
						<tr>
							<td style="text-algin:right;height:60px;">备注：</td>
							<td>
								<input type="text" class="inputtxt" name="menuRemark" id="remark" size="80" maxLength="30"/>
							 </td>
						</tr>
					</table>
					 <div>
						<input type="button" value="增加" onclick="addMenuNode()" />
						<input type="button" value="修改" onclick="updateMenuNode();"/>
						<input type="button" value="删除" onclick="removeMenuNode();"/>
						<input type="button" value="清空" onclick="clearInputValue();"/>
						<input type="button" value="增加模块" id="showWin" onclick="showWindow();" style="display:none"/>
			         </div>
				</form>
				</td>
			</tr>
			<tr>
				<td></td>
				<td colspan="2">
						
				</td>
			</tr>
		</table>
	</div>
	
		<div class="list_content" style="display:none">
						 	<div class="modify" style="border:none">  
							 	<form action="<@s.url "/authority_module/post"/>" name="allotRoleForm" id="menuForm" method="post" style="padding:0px;margin:0px;">
						      		<div style="padding-left:20px;padding-right:20px;">
						      		<input type="hidden" name="menuId"/>
							        <table cellpadding="0" cellspacing="0">
							            <tbody>
							            	<tr>
							            		<td align="center" style="height:20px;">所有模块</td>
							            		<td></td>
							            		<td align="center">已拥有模块</td>
							            	</tr>
							                <tr>
								                <td valign="top">
													<a id="right_up" class="a-up">上移</a>
								               		<div class="sel-div">
								                		<select multiple id="select2" size="30">
								                		</select>
								                	</div>
								                	<a id="right_down" class="a-down">下移</a>
								                </td>
								                <td align="center" valign="middle">
								                	<a id="remove" class="a-left">增加模块&gt;&gt;</a>
								                	<a id="add" class="a-right">&lt;&lt; 删除模块</a>
								                </td>
								                <td>
								                	<a  id="left_up" class="a-up">上移</a>
								                	<div class="sel-div">
								               			<select  name="allowRoleValues" size="30" multiple id="select1" >
								                			   
								                		</select>
								               	 	</div>
								                	<a  id="left_down" class="a-down">下移</a>
								                </td>
							                </tr>	
							                <tr>
							                	<td colspan="3" align="center">
							                		<input  type="button" onclick="modulesubmit()" value="保存" />   
							                	</td>
							                </tr>
							            </tbody>
							        </table>
							        </div>
						        </form>
						  	</div>
			</div>
</body>
</html>
