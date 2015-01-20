create or replace package PKG_OM_CITY_OUTSTOCK_RECHECK is


 /*
   功能：城市仓-拣货复核单审核
   审核拣货复核单，检测出库单、分货单更改复核状态
   作者：SU.YQ
   日期：2013-12-18
  */
  PROCEDURE PROC_OM_OUTSTOCK_RECHECK_AUDIT(I_locno IN bill_om_recheck.locno%type,
                                I_recheckNo IN bill_om_recheck.recheck_no%type, --复核单号
                                I_creator IN  bill_om_recheck.creator%type,
                                O_msg      OUT VARCHAR2 --输出信息
                                );
                                
 /*
   功能：城市仓-拣货复核单删除
   删除状态为10的拣货复核单,回写分货单数量、出库明细数量、删除标签表信息
   作者：SU.YQ
   日期：2013-12-18
  */
  PROCEDURE PROC_OM_OUTSTOCK_RECHECK_DEL(I_locno IN bill_om_recheck.locno%type,
                                I_recheckNo IN bill_om_recheck.recheck_no%type, --复核单号
                                O_msg      OUT VARCHAR2 --输出信息
                                );
                                
    

end PKG_OM_CITY_OUTSTOCK_RECHECK;
/
create or replace package body PKG_OM_CITY_OUTSTOCK_RECHECK is
   
   /*
   功能：城市仓-拣货复核单审核
   审核拣货复核单，检测出库单、分货单更改复核状态
   作者：SU.YQ
   日期：2013-12-18
  */
  PROCEDURE PROC_OM_OUTSTOCK_RECHECK_AUDIT(I_locno IN bill_om_recheck.locno%type,
                                I_recheckNo IN bill_om_recheck.recheck_no%type, --复核单号
                                I_creator IN  bill_om_recheck.creator%type,
                                O_msg      OUT VARCHAR2 --输出信息
                                )IS
                                
      v_locate_no varchar2(20);--波茨号 
      v_divide_no varchar2(20);--分货单 
      v_store_no varchar2(20);--客户编码 
      --v_strStatus varchar(20);--状态                           
      v_serialNo  varchar2(20);--流到编码
      v_dtl_num number;
                                                         
   BEGIN
     
       --查找复核明细
       select count(*)
         into v_dtl_num
         from bill_om_recheck_dtl dtl
        where dtl.locno = I_locno
          and dtl.recheck_no = I_recheckNo;
       if v_dtl_num = 0 then
         O_msg := 'N|复核明细为空,不能审核!';
         return;
       end if;
            
       --查询复核单主档信息获取对应信息
       select r.store_no, r.divide_no
         into v_store_no, v_locate_no
         from bill_om_recheck r
        where r.locno = I_locno
          and r.recheck_no = I_recheckNo;
       
       --查找分货单
        begin
          select dtl.divide_no into v_divide_no
            from bill_om_divide_dtl dtl
           where dtl.locno = I_locno
             and dtl.source_no = v_locate_no
             and rownum = 1 ;
        exception when no_data_found then
          null;
        end;
        
        --查找流到编码
        begin
          select dtl.serial_no
            into v_serialNo
            from bill_om_outstock_dtl dtl
           where dtl.locno = I_locno
             and dtl.store_no = v_store_no
             and dtl.locate_no = v_locate_no
             and rownum = 1;
        exception when no_data_found then
         O_msg := 'N|查询流到编码为空!';
         return;
        end;   
        
        --调用审核写库存存储过程
         pkg_om_city_pickrecheck.proc_om_writecontent(I_locno,
                                                     v_locate_no,
                                                     v_store_no,
                                                     I_recheckNo,
                                                     I_creator,
                                                     O_msg);
        if O_msg <> 'Y|'
          then
            return;
        end if;
        
       /* --是否存在分货单
        if v_divide_no is not null then
            --更新分货任务单
            select case
                     when (exists (select 'X'
                                     from bill_om_divide_dtl
                                    where locno = I_locno
                                      and divide_no = v_divide_no
                                      and status = '10')) or
                          (exists (select 'X'
                                     from bill_om_recheck br
                                    where br.locno = I_locno
                                      and br.divide_no = v_locate_no
                                      and br.status = '10')) then
                      '30' --部分复核
                     else
                      '35' --复核完成
                   end
              into v_strStatus
              from dual;
            update bill_om_divide
               set status = v_strStatus
             where locno = I_locno
               and divide_no = v_divide_no;
            if sql%rowcount = 0 then
              O_msg := 'N|更新分货任务单状态失败(0行)!';
              return;
            end if;
            
            --写状态日志表
            Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                           v_divide_no,
                                           'OM',
                                           v_strStatus,
                                           '拣货复核,更新分货单状态为:' || case when
                                           v_strStatus = '30' then '部分复核' else
                                           '复核完成' end,
                                           I_creator,
                                           O_msg);
        end if;*/
        
        
        
       /* --更新出库单操作状态
        for dr in (select dtl.exp_no
                     from bill_om_outstock_dtl dtl
                    where dtl.locno = I_locno
                      and dtl.locate_no = v_locate_no
                      and nvl(dtl.recheck_qty, 0) > 0
                    group by dtl.exp_no) loop
          select case
                   when exists (select 'X'
                           from bill_om_exp_dtl dtl
                          where dtl.locno = I_locno
                            and dtl.exp_no = dr.exp_no
                            and nvl(dtl.real_qty, 0) < dtl.item_qty) then
                    '40' --部分复核
                   else
                    '45' --复核完成
                 end
            into v_strStatus
            from dual;
        
          update bill_om_exp exp
             set status = v_strStatus
           where exp.locno = I_locno
             and exp.exp_no = dr.exp_No;
          if sql%rowcount = 0 then
            O_msg := 'N|更新发货通知单状态失败(0行)';
            return;
          end if;
          --写状态日志表
          Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                               dr.exp_no,
                                               'OM',
                                               v_strStatus,
                                               '拣货复核,更新发货通知单状态为:' || case when
                                               v_strStatus = '40' then '部分复核' else
                                               '复核完成' end,
                                               I_creator,
                                               O_msg);
        end loop;*/
        
        /*--更新复核单全部复核完成
        update bill_om_recheck chk
           set chk.status = '13'
         where chk.locno = I_locno
           and chk.store_no = v_store_no
           and chk.recheck_no = I_recheckNo
           and chk.divide_no = v_locate_no;
        --当该门店复核完成时
        if sql%rowcount > 0 then
         
          --更新复核单明细全部复核完成
           update bill_om_recheck_dtl chk
             set chk.status = '13'
           where chk.locno = I_locno
             and chk.recheck_no = I_recheckNo; 
           if sql%rowcount = 0 then
              O_msg := 'N|更新复核明细状态失败(0行)!';
              return;
            end if;  
         
          Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                               I_recheckNo,
                                               'OM',
                                               '13',
                                               '拣货复核,更新复核单状态为:复核完成',
                                               I_creator,
                                               O_msg);
         else
            O_msg := 'N|更新复核单状态失败(0行)';
            return;
         end if;*/
         
         O_msg := 'Y|';
      EXCEPTION
        WHEN OTHERS THEN
          O_msg := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
          RETURN;
         
   end  PROC_OM_OUTSTOCK_RECHECK_AUDIT;                       


   /*
   功能：城市仓-拣货复核单删除
   删除状态为10的拣货复核单,回写分货单数量、出库明细数量、删除标签表信息
   作者：SU.YQ
   日期：2013-12-18
  */
  PROCEDURE PROC_OM_OUTSTOCK_RECHECK_DEL(I_locno IN  bill_om_recheck.locno%type,
                                I_recheckNo IN bill_om_recheck.recheck_no%type, --复核单号
                                O_msg OUT VARCHAR2 --输出信息
                                )IS
                                
  v_locate_no varchar2(20);--波茨号 
  v_divide_no varchar2(20);--分货单 
  v_store_no varchar2(20);--客户编码
  
  BEGIN
    
    --查询复核单主档信息获取对应信息
    select r.store_no,r.divide_no into v_store_no,v_locate_no
    from bill_om_recheck r 
    where r.locno=I_locno
    and r.recheck_no=I_recheckNo;
    
    --查找分货单
    begin
      select dtl.divide_no into v_divide_no
        from bill_om_divide_dtl dtl
       where dtl.locno = I_locno
         and dtl.source_no = v_locate_no
         and rownum = 1 ;
    exception when no_data_found then
      null;
    end;
    
    --回写更新出库订单明细实际数量
    update bill_om_exp_dtl dtl
       set dtl.real_qty = dtl.real_qty-
           (select nvl(sum(real_qty), 0)
              from bill_om_recheck_dtl chk
             where chk.locno = dtl.locno
               and chk.owner_no = dtl.owner_no
               and chk.exp_no = dtl.exp_no
               and chk.item_no = dtl.item_no
               and chk.size_no = dtl.size_no
               and chk.recheck_no = I_recheckNo)
     where dtl.locno = I_locno
       and dtl.store_no = v_store_no
       and exists (select 'X'
              from bill_om_recheck_dtl a
             where a.locno = dtl.locno
               and a.owner_no = dtl.owner_no
               and a.exp_no = dtl.exp_no
               and a.item_no = dtl.item_no
               and a.size_no = dtl.size_no
               and a.recheck_no = I_recheckNo);
    if sql%rowcount = 0 then
      O_msg := 'N|更新出库订单明细实际数量失败(0行)!';
      return;
    end if;
    
    --更新出库主档状态,如果实收数量全部=0,更新状态为20拣货完成
    for rf in (select rd.locno,rd.exp_no,rd.exp_type 
           from bill_om_recheck_dtl rd 
           where rd.locno=I_locno
           and rd.recheck_no=I_recheckNo
           group by rd.locno,rd.exp_no,rd.exp_type)
      loop 
           
        update bill_om_exp e 
           set e.status = '20'
        where not exists (
              select 'X' from bill_om_exp_dtl d 
               where d.locno=e.locno
               and d.exp_no = e.exp_no
               and d.real_qty > 0               
        )and e.locno=rf.locno
         and e.exp_no=rf.exp_no
         and e.exp_type=rf.exp_type;
           
    end loop;
      
 
    --如果存在分货单，回写分货单数量
    --回写更新拣货单明细状态
     if v_divide_no is not null then
        update bill_om_divide_dtl dtl
           set dtl.real_qty = dtl.real_qty-
               (select nvl(sum(chk.recheck_qty), 0)
                  from bill_om_outstock_dtl chk              
                 where chk.locno = dtl.locno
                   and chk.owner_no = dtl.owner_no
                   and chk.divide_id = dtl.divide_id
                   and chk.locate_no = dtl.source_no
                   and chk.locate_no = v_locate_no
                   and chk.store_no = v_store_no)
         where dtl.locno = I_locno
           and dtl.divide_no = v_divide_no
           and exists (select 'X'
                  from bill_om_outstock_dtl chk              
                 where chk.locno = dtl.locno
                   and chk.owner_no = dtl.owner_no
                   and chk.divide_id = dtl.divide_id
                   and chk.locate_no = dtl.source_no
                   and chk.locate_no = v_locate_no
                   and chk.store_no = v_store_no);
        if sql%rowcount = 0 then
          O_msg := 'N|更新分货明细实际数量失败(0行)!';
          return;
        end if;
        
        --更新分货明细状态为10
        update bill_om_divide_dtl dtl
               set dtl.status = '10' 
               where dtl.locno=I_locno
               and dtl.divide_no=v_divide_no
               and exists (
                     select 'X' from bill_om_divide_dtl d
                     where d.item_qty=nvl(d.real_qty,0)
                     and dtl.locno=d.locno
                     and dtl.owner_no=d.owner_no
                     and dtl.divide_id=d.divide_id
                     and dtl.divide_no=d.divide_no
               );
         
     end if;
      
    --回写更新拣货单明细状态
    update bill_om_outstock_dtl dtl
       set dtl.recheck_qty = dtl.recheck_qty-
           (select nvl(sum(chk.real_qty), 0)
              from bill_om_recheck_dtl chk
              inner join bill_om_recheck r
              on chk.locno=r.locno
              and chk.recheck_no=r.recheck_no
             where chk.locno = dtl.locno
               and chk.owner_no = dtl.owner_no
               and chk.exp_no = dtl.exp_no
               and chk.item_no = dtl.item_no
               and chk.size_no = dtl.size_no
               and chk.box_no=dtl.scan_label_no
               and chk.recheck_no = I_recheckNo
               and r.divide_no=v_locate_no)
     where dtl.locno = I_locno
       and dtl.store_no = v_store_no
       and exists (select 'X'
              from bill_om_recheck_dtl a
              inner join bill_om_recheck r
              on a.locno=r.locno
               and a.recheck_no=r.recheck_no
             where a.locno = dtl.locno                        
               and a.owner_no = dtl.owner_no
               and a.exp_no = dtl.exp_no
               and a.item_no = dtl.item_no
               and a.size_no = dtl.size_no
               and a.box_no=dtl.scan_label_no
               and a.recheck_no = I_recheckNo
               and r.divide_no=v_locate_no);
    if sql%rowcount = 0 then
      O_msg := 'N|更新拣货明细实际数量失败(0行)!';
      return;
    end if;
    
    
    --删除箱号表
     for r_box in (select c.locno,cd.owner_no,c.scan_label_no 
                  from bill_om_recheck d
               inner join bill_om_recheck_dtl dtl 
                     on d.locno=dtl.locno 
                     and d.recheck_no=dtl.recheck_no     
               inner join con_label c
                     on c.container_no=dtl.container_no  
               inner join con_label_dtl cd
                     on c.locno=cd.locno
                     and c.container_no=cd.container_no
                     and c.container_type=cd.container_type                             
               where d.locno=I_locno
                      and d.recheck_no=I_recheckNo
                      and d.divide_no=v_locate_no
               group by c.locno,cd.owner_no,c.scan_label_no)loop 
                
           --删除箱明细表
           delete from con_box_dtl cbd 
           where cbd.locno=r_box.locno
                 and cbd.owner_no=r_box.owner_no
                 and cbd.box_no=r_box.scan_label_no;  
          
          --删除箱表
          delete from con_box c 
           where c.locno=r_box.locno
                 and c.owner_no=r_box.owner_no
                 and c.box_no=r_box.scan_label_no;     
          
     end loop;
     
     
     --删除标签表
     for r_recheck in (select
         dtl.locno,dtl.owner_no,dtl.recheck_no,
         dtl.container_no from bill_om_recheck r 
       inner join bill_om_recheck_dtl dtl
             on r.locno=dtl.locno
             and r.recheck_no=dtl.recheck_no
       where r.locno=I_locno
             and r.recheck_no=I_recheckNo
       group by dtl.locno,dtl.owner_no,dtl.recheck_no,dtl.container_no)loop
           
           --删除标签明细表
           delete from con_label_dtl cd
            where cd.locno=r_recheck.locno
                  and cd.owner_no=r_recheck.owner_no
                  and cd.container_no=r_recheck.container_no;                     
            
           --删除标签表
           delete from con_label c
            where c.locno=r_recheck.locno
                  and c.container_no=r_recheck.container_no;              
               
     end loop;
     
     --删除复核明细表
     delete from bill_om_recheck_dtl dtl
            where dtl.locno=I_locno
            and dtl.recheck_no=I_recheckNo;
     
     --删除复核表
     delete from bill_om_recheck r
            where r.locno=I_locno
            and r.recheck_no=I_recheckNo;
     if sql%rowcount <= 0
        then 
          rollback;
          O_msg := 'N|删除复核表失败!';
          return;
     end if;  
     
     O_msg := 'Y|';
      EXCEPTION
        WHEN OTHERS THEN
          O_msg := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
          RETURN;
  
  end PROC_OM_OUTSTOCK_RECHECK_DEL;

end PKG_OM_CITY_OUTSTOCK_RECHECK;
/
