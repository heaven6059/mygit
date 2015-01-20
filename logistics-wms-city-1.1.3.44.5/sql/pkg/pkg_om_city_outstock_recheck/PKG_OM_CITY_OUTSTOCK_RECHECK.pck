create or replace package PKG_OM_CITY_OUTSTOCK_RECHECK is


 /*
   ���ܣ����в�-������˵����
   ��˼�����˵��������ⵥ���ֻ������ĸ���״̬
   ���ߣ�SU.YQ
   ���ڣ�2013-12-18
  */
  PROCEDURE PROC_OM_OUTSTOCK_RECHECK_AUDIT(I_locno IN bill_om_recheck.locno%type,
                                I_recheckNo IN bill_om_recheck.recheck_no%type, --���˵���
                                I_creator IN  bill_om_recheck.creator%type,
                                O_msg      OUT VARCHAR2 --�����Ϣ
                                );
                                
 /*
   ���ܣ����в�-������˵�ɾ��
   ɾ��״̬Ϊ10�ļ�����˵�,��д�ֻ���������������ϸ������ɾ����ǩ����Ϣ
   ���ߣ�SU.YQ
   ���ڣ�2013-12-18
  */
  PROCEDURE PROC_OM_OUTSTOCK_RECHECK_DEL(I_locno IN bill_om_recheck.locno%type,
                                I_recheckNo IN bill_om_recheck.recheck_no%type, --���˵���
                                O_msg      OUT VARCHAR2 --�����Ϣ
                                );
                                
    

end PKG_OM_CITY_OUTSTOCK_RECHECK;
/
create or replace package body PKG_OM_CITY_OUTSTOCK_RECHECK is
   
   /*
   ���ܣ����в�-������˵����
   ��˼�����˵��������ⵥ���ֻ������ĸ���״̬
   ���ߣ�SU.YQ
   ���ڣ�2013-12-18
  */
  PROCEDURE PROC_OM_OUTSTOCK_RECHECK_AUDIT(I_locno IN bill_om_recheck.locno%type,
                                I_recheckNo IN bill_om_recheck.recheck_no%type, --���˵���
                                I_creator IN  bill_om_recheck.creator%type,
                                O_msg      OUT VARCHAR2 --�����Ϣ
                                )IS
                                
      v_locate_no varchar2(20);--���ĺ� 
      v_divide_no varchar2(20);--�ֻ��� 
      v_store_no varchar2(20);--�ͻ����� 
      --v_strStatus varchar(20);--״̬                           
      v_serialNo  varchar2(20);--��������
      v_dtl_num number;
                                                         
   BEGIN
     
       --���Ҹ�����ϸ
       select count(*)
         into v_dtl_num
         from bill_om_recheck_dtl dtl
        where dtl.locno = I_locno
          and dtl.recheck_no = I_recheckNo;
       if v_dtl_num = 0 then
         O_msg := 'N|������ϸΪ��,�������!';
         return;
       end if;
            
       --��ѯ���˵�������Ϣ��ȡ��Ӧ��Ϣ
       select r.store_no, r.divide_no
         into v_store_no, v_locate_no
         from bill_om_recheck r
        where r.locno = I_locno
          and r.recheck_no = I_recheckNo;
       
       --���ҷֻ���
        begin
          select dtl.divide_no into v_divide_no
            from bill_om_divide_dtl dtl
           where dtl.locno = I_locno
             and dtl.source_no = v_locate_no
             and rownum = 1 ;
        exception when no_data_found then
          null;
        end;
        
        --������������
        begin
          select dtl.serial_no
            into v_serialNo
            from bill_om_outstock_dtl dtl
           where dtl.locno = I_locno
             and dtl.store_no = v_store_no
             and dtl.locate_no = v_locate_no
             and rownum = 1;
        exception when no_data_found then
         O_msg := 'N|��ѯ��������Ϊ��!';
         return;
        end;   
        
        --�������д���洢����
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
        
       /* --�Ƿ���ڷֻ���
        if v_divide_no is not null then
            --���·ֻ�����
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
                      '30' --���ָ���
                     else
                      '35' --�������
                   end
              into v_strStatus
              from dual;
            update bill_om_divide
               set status = v_strStatus
             where locno = I_locno
               and divide_no = v_divide_no;
            if sql%rowcount = 0 then
              O_msg := 'N|���·ֻ�����״̬ʧ��(0��)!';
              return;
            end if;
            
            --д״̬��־��
            Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                           v_divide_no,
                                           'OM',
                                           v_strStatus,
                                           '�������,���·ֻ���״̬Ϊ:' || case when
                                           v_strStatus = '30' then '���ָ���' else
                                           '�������' end,
                                           I_creator,
                                           O_msg);
        end if;*/
        
        
        
       /* --���³��ⵥ����״̬
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
                    '40' --���ָ���
                   else
                    '45' --�������
                 end
            into v_strStatus
            from dual;
        
          update bill_om_exp exp
             set status = v_strStatus
           where exp.locno = I_locno
             and exp.exp_no = dr.exp_No;
          if sql%rowcount = 0 then
            O_msg := 'N|���·���֪ͨ��״̬ʧ��(0��)';
            return;
          end if;
          --д״̬��־��
          Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                               dr.exp_no,
                                               'OM',
                                               v_strStatus,
                                               '�������,���·���֪ͨ��״̬Ϊ:' || case when
                                               v_strStatus = '40' then '���ָ���' else
                                               '�������' end,
                                               I_creator,
                                               O_msg);
        end loop;*/
        
        /*--���¸��˵�ȫ���������
        update bill_om_recheck chk
           set chk.status = '13'
         where chk.locno = I_locno
           and chk.store_no = v_store_no
           and chk.recheck_no = I_recheckNo
           and chk.divide_no = v_locate_no;
        --�����ŵ긴�����ʱ
        if sql%rowcount > 0 then
         
          --���¸��˵���ϸȫ���������
           update bill_om_recheck_dtl chk
             set chk.status = '13'
           where chk.locno = I_locno
             and chk.recheck_no = I_recheckNo; 
           if sql%rowcount = 0 then
              O_msg := 'N|���¸�����ϸ״̬ʧ��(0��)!';
              return;
            end if;  
         
          Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                               I_recheckNo,
                                               'OM',
                                               '13',
                                               '�������,���¸��˵�״̬Ϊ:�������',
                                               I_creator,
                                               O_msg);
         else
            O_msg := 'N|���¸��˵�״̬ʧ��(0��)';
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
   ���ܣ����в�-������˵�ɾ��
   ɾ��״̬Ϊ10�ļ�����˵�,��д�ֻ���������������ϸ������ɾ����ǩ����Ϣ
   ���ߣ�SU.YQ
   ���ڣ�2013-12-18
  */
  PROCEDURE PROC_OM_OUTSTOCK_RECHECK_DEL(I_locno IN  bill_om_recheck.locno%type,
                                I_recheckNo IN bill_om_recheck.recheck_no%type, --���˵���
                                O_msg OUT VARCHAR2 --�����Ϣ
                                )IS
                                
  v_locate_no varchar2(20);--���ĺ� 
  v_divide_no varchar2(20);--�ֻ��� 
  v_store_no varchar2(20);--�ͻ�����
  
  BEGIN
    
    --��ѯ���˵�������Ϣ��ȡ��Ӧ��Ϣ
    select r.store_no,r.divide_no into v_store_no,v_locate_no
    from bill_om_recheck r 
    where r.locno=I_locno
    and r.recheck_no=I_recheckNo;
    
    --���ҷֻ���
    begin
      select dtl.divide_no into v_divide_no
        from bill_om_divide_dtl dtl
       where dtl.locno = I_locno
         and dtl.source_no = v_locate_no
         and rownum = 1 ;
    exception when no_data_found then
      null;
    end;
    
    --��д���³��ⶩ����ϸʵ������
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
      O_msg := 'N|���³��ⶩ����ϸʵ������ʧ��(0��)!';
      return;
    end if;
    
    --���³�������״̬,���ʵ������ȫ��=0,����״̬Ϊ20������
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
      
 
    --������ڷֻ�������д�ֻ�������
    --��д���¼������ϸ״̬
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
          O_msg := 'N|���·ֻ���ϸʵ������ʧ��(0��)!';
          return;
        end if;
        
        --���·ֻ���ϸ״̬Ϊ10
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
      
    --��д���¼������ϸ״̬
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
      O_msg := 'N|���¼����ϸʵ������ʧ��(0��)!';
      return;
    end if;
    
    
    --ɾ����ű�
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
                
           --ɾ������ϸ��
           delete from con_box_dtl cbd 
           where cbd.locno=r_box.locno
                 and cbd.owner_no=r_box.owner_no
                 and cbd.box_no=r_box.scan_label_no;  
          
          --ɾ�����
          delete from con_box c 
           where c.locno=r_box.locno
                 and c.owner_no=r_box.owner_no
                 and c.box_no=r_box.scan_label_no;     
          
     end loop;
     
     
     --ɾ����ǩ��
     for r_recheck in (select
         dtl.locno,dtl.owner_no,dtl.recheck_no,
         dtl.container_no from bill_om_recheck r 
       inner join bill_om_recheck_dtl dtl
             on r.locno=dtl.locno
             and r.recheck_no=dtl.recheck_no
       where r.locno=I_locno
             and r.recheck_no=I_recheckNo
       group by dtl.locno,dtl.owner_no,dtl.recheck_no,dtl.container_no)loop
           
           --ɾ����ǩ��ϸ��
           delete from con_label_dtl cd
            where cd.locno=r_recheck.locno
                  and cd.owner_no=r_recheck.owner_no
                  and cd.container_no=r_recheck.container_no;                     
            
           --ɾ����ǩ��
           delete from con_label c
            where c.locno=r_recheck.locno
                  and c.container_no=r_recheck.container_no;              
               
     end loop;
     
     --ɾ��������ϸ��
     delete from bill_om_recheck_dtl dtl
            where dtl.locno=I_locno
            and dtl.recheck_no=I_recheckNo;
     
     --ɾ�����˱�
     delete from bill_om_recheck r
            where r.locno=I_locno
            and r.recheck_no=I_recheckNo;
     if sql%rowcount <= 0
        then 
          rollback;
          O_msg := 'N|ɾ�����˱�ʧ��!';
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
