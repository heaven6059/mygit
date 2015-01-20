<#-- 
id:
title:
iconCls:
action:
type: 1-新增 2-修改 3-删除 4-审核5-导出      1、2、3，4,5之外不验证权限

验证： checkPower("${thisPowerV?number}",2)==true
-->
<#macro toolbar  id  listData  >
<div id="${id}"></div>
<#assign thisPowerV="${thisPower!0}" >
<script type="text/javascript">
  var toolbarList=[];
  <#list listData as item>
      <#assign flag=1>
      <#if (item.type)?? >
      	<#--  
          <#if (item.type==1) || (item.type==2) ||(item.type==3)||(item.type==4)||(item.type==5)>
                <#if  checkPower("${thisPowerV?number}",item.type)==false>
                     <#assign flag=0>
                     
                </#if>
          </#if>
          -->
          var this_bar={};
          this_bar.text='${item.title}';
          <#if flag==1>
          		this_bar.id='${item.id}';
          		this_bar.iconCls='${item.iconCls}';
          		this_bar.handler=function(){${item.action}};
              <#else>
                this_bar.iconCls='${item.iconCls}';
                this_bar.disabled=true;
           </#if>
           toolbarList.push(this_bar);
           toolbarList.push('-');
      </#if> 
  </#list>
  toolbarList.pop();
  $('#${id}').toolbar({items:toolbarList});
</script>
</#macro>
