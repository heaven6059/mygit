
/**
 * 角色分配相关JS
 */
$(function(){
       //移到右边
       $('#add').click(function() {
            $('#select1 option:selected').remove().appendTo('#select2');
       });
       //移到左边
       $('#remove').click(function() {
            $('#select2 option:selected').remove().appendTo('#select1');
       });
       //双击选项
       $('#select1').dblclick(function(){
           $("option:selected",this).remove().appendTo('#select2');
       });
       //双击选项
       $('#select2').dblclick(function(){
           $("option:selected",this).remove().appendTo('#select1');
       });
       //左边向上按钮
       $('#left_up').click(function(){
          var index = $('#select1 option').index($('#select1 option:selected:first'));
        var $recent = $('#select1 option:eq('+(index-1)+')');
        if(index>0){
             var $options = $('#select1 option:selected').remove();
            setTimeout(function(){
                $recent.before($options );
            },10);
        }
       });
       //左边向下按钮
       $('#left_down').click(function(){
          var index = $('#select1 option').index($('#select1 option:selected:last'));
        var len = $('#select1 option').length-1;
        var $recent = $('#select1 option:eq('+(index+1)+')');
        if(index<len ){
             var $options = $('#select1 option:selected').remove();
            setTimeout(function(){
                $recent.after( $options );
            },10);
           }
       });
       //右边向上按钮
       $('#right_up').click(function(){
          var index = $('#select2 option').index($('#select2 option:selected:first'));
        var $recent = $('#select2 option:eq('+(index-1)+')');
        if(index>0){
             var $options = $('#select2 option:selected').remove();
            setTimeout(function(){
                $recent.before($options );
            },10);
        }
       });
       //右边向下按钮
       $('#right_down').click(function(){
          var index = $('#select2 option').index($('#select2 option:selected:last'));
        var len = $('#select2 option').length-1;
        var $recent = $('#select2 option:eq('+(index+1)+')');
        if(index<len ){
             var $options = $('#select2 option:selected').remove();
            setTimeout(function(){
                $recent.after( $options );
            },10);
           }
       });
});

/**
 * 回写组织客户编号与名称
 */
function initOrganizStruct(organizNo,organizName){
	$("#organizName").attr("value",organizName);
	$("#organizNo").attr("value",organizNo);
	$("#organizName").focus();
}

/**
 * 删除系统用户
 */
function removeSystemUser(id){
	 ygdg.dialog.confirm("确定要删除该系统用户?",function(){
		 var url = "d_removeSystemUser.htm";
			var data={
				"id":id
			};
			ajaxRequest(url,data,function(result){
				if(!result) return ;
				
				if(result == "success"){
					ygdg.dialog.alert("删除成功");
					$("#Tr"+id).remove(); 
				}else{
					ygdg.dialog.alert("删除失败");
				}
			});
	 });
}
/**
 *到用户角色分配
 * @param id
 */
function allotUserRole(id){
	var params = "id="+id;
	openwindow("../../systemmgmt/systemuser/toAllotUserRole.sc?id="+id,550,750,"分配角色");
}


/**
 * 修改系统用户状态
 */
function updateUserState(id,state){
	 ygdg.dialog.confirm("确定修改?",function(){
		var url = "u_updateUserState.sc";
		var data={
			"id":id,
			"state":state
		};
		ajaxRequest(url,data,function(result){
			if(!result) return ;
			result = result.replace(/(^\s*)|(\s*$)/g,'');
			if(result == "success"){
				ygdg.dialog.alert("成功！");
				if(state == "1"){
					$("#clockUser"+id).css("display","inline-block");
					$("#unClockUser"+id).css("display","none");
					
					$("#State"+id).text("正常");
					
				}else{
					$("#unClockUser"+id).css("display","inline-block");
					$("#clockUser"+id).css("display","none");
					
					$("#State"+id).text("锁定");
				}
				return ;
			}
		});
	});
}



/**
 * 提交表单
 */
function submitAllotRoleForm(){
	var form = document.allotRoleForm;
	if($("#select1 > option").length == 0){
		$("#select1").append("<option value='null-role'></option>");
	}
	$("#select1 > option").attr("selected",true);
	form.target="mbdif";		//表单提交后在父页面显示结果
	form.submit();
	window.top.TB_remove();
}

/**
 * 提交表单
 */
function submitForm(){
	var form = document.forms[0];
	form.target="mbdif";		//表单提交后在父页面显示结果
	form.submit();
	window.top.TB_remove();
}

//发达ajax请求
function ajaxRequest(url,reqParam,callback){
	$.ajax({
		  type: 'POST',
		  url: url,
		  data: reqParam,
		  cache: true,
		  success: callback
	});
}

/**
* 增加角色
*/
function backList(){
	window.location.href = "querySystemUserList.htm";
}


