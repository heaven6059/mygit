create or replace package PKG_OM_CITY_DIVIDE is

  /*
      ���ܣ��ֻ���-�ֹ��ص�
     ���ߣ�SU.YQ
     ���ڣ�2014-02-25 
  */
  PROCEDURE PROC_OM_OVER_DIVIDE(I_locno    bill_om_divide.locno%type, --�ֱ�
                                I_divideNo bill_om_divide.divide_no%type, --�ֻ�����
                                I_creator  bill_om_divide.creator%type,
                                O_msg      OUT VARCHAR2 --�����Ϣ
                                );

end PKG_OM_CITY_DIVIDE;
/
create or replace package body PKG_OM_CITY_DIVIDE is

  /*
      ���ܣ��ֻ���-�ֹ��ص�
     ���ߣ�SU.YQ
     ���ڣ�2014-02-25 
  */
  PROCEDURE PROC_OM_OVER_DIVIDE(I_locno    bill_om_divide.locno%type, --�ֱ�
                                I_divideNo bill_om_divide.divide_no%type, --�ֻ�����
                                I_creator  bill_om_divide.creator%type,
                                O_msg      OUT VARCHAR2 --�����Ϣ
                                ) IS
  
    v_recheckdtl_num number;
    v_divide_status  varchar2(20);
    v_d_cell_no varchar2(50);
    v_row_id number;
  
  BEGIN
    
    v_divide_status := '0';
    v_recheckdtl_num := 0;
    v_d_cell_no := 'N';
    v_row_id := 0;
    O_msg            := 'Y|';
  
    select d.status
      into v_divide_status
      from bill_om_divide d
     where d.locno = I_locno
       and d.divide_no = I_divideNo;
    if v_divide_status = '91' then
      O_msg := 'N|�÷ֻ����Ѿ��ر�,�����ٴιر�!';
      return;
    end if;
    if v_divide_status = '45' then
      O_msg := 'N|�÷ֻ����Ѿ��������,�����ֹ��ر�!';
      return;
    end if;
     
    --���ҷֻ���ϸ�����пͻ�
    for dfor in (select dtl.locno,
                        dtl.divide_no,
                        dtl.store_no,
                        dtl.serial_no
                   from bill_om_divide_dtl dtl
                  where dtl.locno = I_locno
                    and dtl.divide_no = I_divideNo
                  group by dtl.locno,
                           dtl.divide_no,
                           dtl.store_no,
                           dtl.serial_no) loop
    
      --ѭ�������ĸ��˵�
      for rfor in (select r.*
                     from bill_om_recheck r
                    where r.locno = I_locno
                      and r.divide_no = I_divideNo
                      and r.store_no = dfor.store_no
                      and r.status = '10') loop
        select count(*)
          into v_recheckdtl_num
          from bill_om_recheck_dtl rd
         where rd.locno = rfor.locno
           and rd.recheck_no = rfor.recheck_no;
      
        if v_recheckdtl_num = 0 then
          O_msg := 'N|���˵�:' || rfor.recheck_no || '�����ڸ�����ϸ,��ɾ�����ֹ��ر�!';
          return;
        end if;
      
        --���ô洢����
        pkg_om_city_recheck.proc_om_audit_recheckorder(I_locno,
                                                       I_divideNo,
                                                       dfor.serial_no,
                                                       rfor.recheck_no,
                                                       I_creator,
                                                       O_msg);
        if instr(O_msg, 'N', 1, 1) = 1 then
          return;
        end if;                                      
      
      end loop;
    
    end loop;
    
    
    --ʣ�������д������ݴ���   
    for dd in (select rd.locno,
                   rd.owner_no,
                   rd.receipt_no,
                   rd.item_no,
                   rd.size_no,
                   rd.item_type,
                   rd.quality,
                   rd.box_no,
                   it.supplier_no,
                   rd.cell_no,
                   rd.cell_id,
                   (rd.receipt_qty - nvl(bodd.real_qty, 0)) real_qty
              from bill_im_receipt r
             inner join bill_im_receipt_dtl rd
                on r.locno = rd.locno
               and r.owner_no = rd.owner_no
               and r.receipt_no = rd.receipt_no
              left join (select odd.locno,
                                odd.owner_no,
                                odd.divide_no,
                                odd.source_no,
                                odd.item_no,
                                odd.size_no,
                                odd.box_no,                                
                                odd.s_cell_no,
                                odd.s_cell_id,
                                nvl(sum(odd.item_qty), 0) item_qty,
                                nvl(sum(odd.real_qty), 0) real_qty
                           from bill_om_divide_dtl odd
                          where odd.locno = I_locno
                            and odd.divide_no = I_divideNo
                          group by odd.locno,
                                   odd.owner_no,
                                   odd.divide_no,
                                   odd.source_no,
                                   odd.item_no,
                                   odd.size_no,
                                   odd.box_no,
                                   odd.s_cell_no,
                                   odd.s_cell_id) bodd
                on rd.locno = bodd.locno
               and rd.owner_no = bodd.owner_no
               and rd.receipt_no = bodd.source_no
               and rd.item_no = bodd.item_no
               and rd.size_no = bodd.size_no
               and rd.cell_no = bodd.s_cell_no
               and rd.cell_id = bodd.s_cell_id
               and rd.box_no = bodd.box_no
              left join item it
                on it.item_no = rd.item_no
             where r.locno = I_locno
               and r.business_type = '1'
               and rd.receipt_qty - nvl(bodd.real_qty,0) > 0
               and bodd.divide_no = I_divideNo) loop
          
             v_row_id := v_row_id + 1;
             --���Ƴ� ��ʼ
             acc_prepare_data_ext(dd.Receipt_No ,
                          'IR' ,
                          'O' ,
                          I_creator ,
                          v_row_id ,
                          dd.cell_id,
                          dd.locno,
                          dd.cell_no,
                          dd.ITEM_NO,
                          dd.Size_No,
                          1,
                          dd.ITEM_TYPE,
                          dd.QUALITY,
                          dd.Owner_No,
                          dd.Supplier_No,
                          dd.Box_No,
                          dd.real_qty,
                          -dd.real_qty,
                          0,
                          '0',
                          '0',
                          '1');
                          
              acc_apply(dd.Receipt_No,'2','IR','O',1);
              
              --ȡ�����ݴ�����λ
              pkg_wms_base.proc_getspecailcellno(dd.locno,
                                                  '1',
                                                  dd.quality,
                                                  '1',
                                                  '1',
                                                  dd.item_type,
                                                  v_d_cell_no,
                                                  O_msg);
               if instr(O_msg, 'N', 1, 1) = 1 then
                  return;
               end if;
              
               --д������ݴ���
               acc_prepare_data_ext(dd.Receipt_No ,
                          'IR' ,
                          'I' ,
                          I_creator ,
                          v_row_id ,
                          '',
                          dd.locno,
                          v_d_cell_no,
                          dd.ITEM_NO,
                          dd.Size_No,
                          1,
                          dd.ITEM_TYPE,
                          dd.QUALITY,
                          dd.Owner_No,
                          dd.Supplier_No,
                          dd.Box_No,
                          dd.real_qty,
                          0,
                          0,
                          '0',
                          '0',
                          '1');
                          
              acc_apply(dd.Receipt_No,'2','IR','I',1);
    
    end loop;
    
    --���·ֻ���״̬Ϊ�ֹ��ر�91
    update bill_om_divide d set d.status = '91' where d.locno = I_locno and d.divide_no = I_divideNo;
    if sql%rowcount = 0 then
      O_msg := 'N|���·ֻ���״̬ʧ��(0��)!';
      return;
    end if;
    
    if instr(O_msg, 'N', 1, 1) = 1 then
      return;
    end if;
    
    O_msg := 'Y|';
  END PROC_OM_OVER_DIVIDE;

end PKG_OM_CITY_DIVIDE;
/
