<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Keywords" content="百丽物流总部管理平台" />
<meta name="Description" content="百丽物流总部管理平台" />
<title>百丽物流总部管理平台</title>
<#include  "/WEB-INF/ftl/common/header.ftl" >
<script type="text/javascript" src="<@s.url "/resources/common/js/jquery.layout.js"/>"></script>
<script type="text/javascript" src="<@s.url "/resources/common/js/jquery.layout.extend.js"/>"></script>
<link href="<@s.url "/resources/css/styles/login/base.css" />" rel="stylesheet" />
<link href="<@s.url "/resources/css/styles/login/login.css" />" rel="stylesheet" />
<script type="text/javascript">
	var maxTab=false;
	function changeScreen(){
		if(maxTab){
			$('#cc').layout('show','all');
			maxTab = false;
			$('#tabToolsOne').linkbutton({iconCls:"icon-window-max",text:"全屏"});
		}else{
			$('#center').parent().animate({top:0,left:0},100);
			$('#cc').layout('hidden','all');
			maxTab = true;
			$('#tabToolsOne').linkbutton({iconCls:"icon-window-min",text:"正常"});
		}
	}
	
	$(document).ready(function(){
		//tabs
		var t = $('#centerFrame');
		t.tabs({border:false,fit:true});
		
		t.tabs('add',{
	    title:'欢迎界面',
	    fit:true,   
	    content:'<div><img src="<@s.url "/resources/css/styles/login/login_new/welcome.jpg"/>" /></div>'
	    });
		
	  	$.getJSON("<@s.url "/uc_user_tree.json"/>", function(data){
		  $.each(data[0].children, function(i,m){
		  	var id='panel'+i;
		    $('#accordion').accordion('add', {
		    	id:id,
				title: m.text,
				selected: false
			});
			
			//add menus
			var p = $('<div></div>').appendTo("#"+id);
			p.tree({data:[m],checkbox:false,onClick:function(node){
					//add tabs
					if(node.attributes.url==undefined||node.attributes.url==null)
						return;
					if(!t.tabs('exists',node.text)){
					var url=node.attributes.url;
					if(url.indexOf('?')>0){
					   url+='&moduleId='+node.id;
					}else{
					   url+='?moduleId='+node.id;
					}
					    
						var t_p=t.tabs('add',{
							id:'tags-'+i,
						    title:node.text,    
						    content:'<iframe src="'+url+'" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="yes" width="100%" height="100%"></iframe>',
							closable: true
					    });
					    
						/*相当于 $(document.getElementById('').contentWindow)
					    $(t.tabs('getTab',node.text).find('iframe')[0].contentWindow).resize(function(){
						  var iframeHeight=$(t.tabs('getTab',node.text).find('iframe')[0].contentWindow).height();
						  //t.tabs('getTab',node.text).find('iframe').height(iframeHeight);
						  //t_p.height(1000);
						});*/
					}else{
						t.tabs('select',node.text);
					}
				}
			});
			
		  });
		});
	});
	
    //修改密码
	function updatePassword(){
		
		openwindow("toUpdateLoginSystemUserPassword.htm",450,250,"密码修改");
	}
	
</script>


</head>
<body>
	<div id="cc" class="easyui-layout" fit="true">   
	    <div data-options="region:'north'" style="height:79px;" >
	    	<div class="logo-header">
				<div class="logo-header-banner"></div>
			    <div class="logo-header-status">
				  	<span class="wel">
				  		您好！${(Session["session_user"].username)!}&nbsp;&nbsp;
				  		${(Session["session_user"].locName)!}
				  		(${(Session["session_user"].locNo)!})
				  	</span> 
				    <span class="ml20">
				     <a target="_top" href="${ucIndexUrl!"#"}" class="c-white">返回用户中心</a>|<a target="_top" href="javascript:updatePassword()" class="c-white" >修改密码</a>|<a target="_top" href="<@s.url "/uc_logout"/>" class="c-white">退出</a>
				    </span>
		       </div>
		   </div>
	    </div>   
	    <div data-options="region:'west',title:'目录导航',split:false,iconCls:'text_list_bullets'" style="width:180px">
	    		<div id="accordion" data-options="fit:true" class="easyui-accordion" ></div>
	    </div>   
	    <div id="center" data-options="region:'center'" style="padding:0px;background:#eee;height:500px">

	    	<div id="centerFrame"  data-options="fit:true" ></div>

	    </div>
	 </div>
</body>
</html>
