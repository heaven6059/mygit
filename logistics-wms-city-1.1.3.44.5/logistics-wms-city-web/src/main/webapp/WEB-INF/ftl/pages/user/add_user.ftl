<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<title></title>
	
</head>

<body>
	<div class="contentMain">
		<table>
			<tr>
				<td style="vertical-align: top;padding:10px;" align="left" width="250px;" height="100%">
					<ul id="resourceTree"><script>onloadResourceTree('queryMenuTree.htm',false);</script></ul>
				</td>
				<td>&nbsp;&nbsp;</td>
				<td style="padding: 15px;vertical-align: top;">
				<form action="addResource.htm" name="resourceMgtForm" method="post">
					<table>
						<tr>
							<td style="text-algin:right;height:60px;"><input type="hidden" name="menuId" id="menuId"/>名称：</td>
							<td>
								<input type="text" class="inputtxt" name="menuName" id="menuName" size="80"  maxLength="14"/>
								<span id="menuNameTip"></span>
							</td>
						</tr>
						<tr>
							<td style="text-algin:right;height:60px;">URL：</td>
							<td><input type="text" class="inputtxt" name="memuUrl" id="memuUrl" size="80" maxLength="150"/></td>
						</tr>
						<tr>
							<td style="text-algin:right;height:60px;">类型：</td>
							<td>
								<select name="type" id="type" style="width: 100px;">
									<option value="0">菜单</option>
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
						<tr>
							<td style="text-algin:right;height:60px;">排序：</td>
							<td>
								<input type="text" class="inputtxt" name="sort" id="sort" size="80"  maxLength="5"/>
								<span id="sortTip"></span>
							</td>
						</tr>
						
						<tr>
							<td style="text-algin:right;height:60px;">备注：</td>
							<td>
								<input type="text" class="inputtxt" name="remark" id="remark" size="80" maxLength="30"/>
							 </td>
						</tr>
					</table>
					 <div>
						<input type="submit" value="增加" class="btn-add-normal-4ft"/>
						<input type="button" value="修改" class="btn-add-normal-4ft" onclick="updateMenuNode();"/>
						<input type="button" value="删除" class="btn-add-normal-4ft" onclick="removeMenuNode();"/>
						<!--
						<input type="button" value="清空" class="btn-add-normal-4ft" onclick="clearInputValue();"/>
						-->
			         </div>
				</form>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
