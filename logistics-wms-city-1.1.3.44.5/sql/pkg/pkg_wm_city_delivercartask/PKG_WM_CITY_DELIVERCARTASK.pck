create or replace package PKG_WM_CITY_DELIVERCARTASK is

   /*****************************************************************************************
       功能：城市仓退厂装车审核
      create By su.yq AT 2014-01-10
      Modify By
   *****************************************************************************************/
   procedure Proc_WM_DeliverAuditCar(
                strLocNo in bm_defloc.locno%type,--仓别
                strOwnerNo in supplier_owner.owner_no%type,--委托业主
                strDeliverNo in bill_wm_deliver.deliver_no%type,--配送单号
                strUserID    in loginuser.usercode%type,--操作用户
                strResult    out varchar2
                );

end PKG_WM_CITY_DELIVERCARTASK;
/
create or replace package body PKG_WM_CITY_DELIVERCARTASK is

   /*****************************************************************************************
       功能：城市仓退厂装车审核
      create By su.yq AT 2014-01-10
      Modify By
   *****************************************************************************************/
  procedure Proc_WM_DeliverAuditCar(
                strLocNo in bm_defloc.locno%type,--仓别
                strOwnerNo in supplier_owner.owner_no%type,--委托业主
                strDeliverNo in bill_wm_deliver.deliver_no%type,--配送单号
                strUserID    in loginuser.usercode%type,--操作用户
                strResult    out varchar2
                )IS

    BEGIN

       strResult:='N|';

       --改退厂配送单状态
       update bill_wm_deliver der
          set der.status  = '13',
              der.editor  = strUserID,
              der.edittm  = sysdate,
              der.auditor = strUserID,
              der.audittm = sysdate
        where der.locno = strLocNo
          and der.owner_no = strOwnerNo
          and der.deliver_no = strDeliverNo;
       if sql%rowcount = 0 then
         strResult := 'N|更改配送单头档状态失败!';
         return;
       end if;

       --更新标签明细表
       UPDATE con_label_dtl LM
          SET LM.STATUS = 'A2', editor = strUserID, edittm = SYSDATE
        WHERE lm.locno = strLocNo
          and lm.scan_label_no in
              (select distinct odd.box_no
                 from bill_wm_deliver_dtl odd
                where odd.locno = strLocNo
                  and odd.deliver_no = strDeliverNo);
       if sql%rowcount <= 0 then
         strResult := 'N|更新标签明细状态为装车完成失败!';
         return;
       end if;

       --更新标签状态为已配送(标签状态:A2-已配送)
       UPDATE con_label LM
          SET LM.STATUS = 'A2', editor = strUserID, edittm = SYSDATE
        WHERE lm.locno = strLocNo
          and lm.scan_label_no in
              (select distinct odd.box_no
                 from bill_wm_deliver_dtl odd
                where odd.locno = strLocNo
                  and odd.deliver_no = strDeliverNo)
          and not exists (select 'X'
                 from con_label_dtl cld
                where cld.locno = strLocNo
                  and cld.scan_label_no in
                      (select distinct odd.box_no
                         from bill_wm_deliver_dtl odd
                        where odd.locno = strLocNo
                          and odd.deliver_no = strDeliverNo)
                  and cld.status < 'A2');
       if sql%rowcount = 0 then
         strResult := 'N|更新标签状态为装车完成失败!';
         return;
       end if;
              
       --更新箱表状态
       UPDATE con_box cb
          SET cb.STATUS = '9'
        WHERE cb.locno = strLocNo
          and cb.owner_no = strOwnerNo
          and cb.box_no in
              (select distinct odd.box_no
                 from bill_wm_deliver_dtl odd
                where odd.locno = strLocNo
                  and odd.deliver_no = strDeliverNo);
       if sql%rowcount <= 0 then
         strResult := 'N|更新箱状态失败!';
         return;
       end if;

       --回写退厂通知单装车数量
       update bill_wm_recede_dtl wrd
          set wrd.deliver_qty = nvl(wrd.deliver_qty, 0) +
                                (select nvl(ucd.real_qty, 0)
                                   from bill_wm_deliver_dtl ucd
                                  where ucd.locno = wrd.locno
                                    and ucd.owner_no = wrd.owner_no
                                    and ucd.item_no = wrd.item_no
                                    and ucd.size_no = wrd.size_no
                                    and ucd.recede_no = wrd.recede_no
                                    and ucd.deliver_no = strDeliverNo)
        where wrd.locno = strLocNo
          and wrd.owner_no = strOwnerNo
          and wrd.recede_no in
              (select distinct wdd.recede_no
                 from bill_wm_deliver_dtl wdd
                where wdd.locno = strLocNo
                  and wdd.owner_no = strOwnerNo
                  and wdd.deliver_no = strDeliverNo)
          and exists (select 'X'
                 from bill_wm_deliver_dtl ucd
                where ucd.locno = wrd.locno
                  and ucd.owner_no = wrd.owner_no
                  and ucd.item_no = wrd.item_no
                  and ucd.size_no = wrd.size_no
                  and ucd.recede_no = wrd.recede_no
                  and ucd.deliver_no = strDeliverNo);

        if sql%rowcount <= 0 then
          rollback;
          strResult := 'N|更新退厂通知单装车数量失败!';
          return;
        end if;

      --更新退厂通知单主表状态
      update bill_wm_recede wr
         set wr.status = case
                           when (select count(*)
                                   from bill_wm_recede_dtl dtl
                                  where dtl.locno = wr.locno
                                    and dtl.owner_no = wr.owner_no
                                    and dtl.recede_no = wr.recede_no
                                    and nvl(dtl.recede_qty, 0) >
                                        nvl(dtl.deliver_qty, 0)) = 0 then
                            '55'
                           else
                            '50'
                         end
       where wr.locno = strLocNo
         and wr.owner_no = strOwnerNo
         and wr.recede_no in
             (select distinct wdd.recede_no
                from bill_wm_deliver_dtl wdd
               where wdd.locno = strLocNo
                 and wdd.owner_no = strOwnerNo
                 and wdd.deliver_no = strDeliverNo);
      if sql%rowcount <= 0 then
        rollback;
        strResult := 'N|更新退厂通知单状态失败!';
        return;
      end if;
      
      --扣减库存
     ACC_APPLY(strDeliverNo,'2','DV','O',0);    

      strResult := 'Y|';

      exception
        when no_data_found then
          strResult := 'N|[EEEEEE]';
          return;
        when others then
          strResult := 'N|' || SQLERRM ||
                       substr(dbms_utility.format_error_backtrace, 1, 256);
          return;

   end Proc_WM_DeliverAuditCar;

end PKG_WM_CITY_DELIVERCARTASK;
/
