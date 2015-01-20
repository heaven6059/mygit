create or replace package PKG_OM_CITY_RECHECK is
  -- Author  : HUANG.JG
  -- Created : 2013-9-29
  -- Purpose : 
  /*
      功能：城市仓出库复核-整箱复核
     作者：HUANG.JG
     日期：2013-9-29 
  */
  PROCEDURE PROC_OM_CREATERECHECKDTLZX(I_locno    bm_defloc.locno%type, --仓别
                                       I_divideNo bill_om_divide.divide_no%type, --分货单号
                                       I_boxNo    con_box.box_no%type, --箱号
                                       I_serialNo bill_om_divide_dtl.serial_no%type, --流道编码
                                       I_creator  bill_om_recheck.creator%type,
                                       O_msg      OUT VARCHAR2 --输出信息
                                       );
  /*
      功能：城市仓出库复核-拆箱复核
     作者：HUANG.JG
     日期：2013-10-11 
  */
  PROCEDURE PROC_OM_CREATERECHECKDTLCX(I_locno    bm_defloc.locno%type, --仓别
                                       I_divideNo bill_om_divide.divide_no%type, --分货单号
                                       I_barcode  item_barcode.barcode%type, --商品条码
                                       I_serialNo bill_om_divide_dtl.serial_no%type, --流道编码
                                       I_creator  bill_om_recheck.creator%type,
                                       O_msg      OUT VARCHAR2 --输出信息
                                       );
  /*
      功能：城市仓出库复核-封箱
     作者：HUANG.JG
     日期：2013-10-11 
  */
  PROCEDURE PROC_OM_CREATERELabel(I_locno    bm_defloc.locno%type, --仓别
                                  I_divideNo bill_om_divide.divide_no%type, --分货单号
                                  I_serialNo bill_om_divide_dtl.serial_no%type, --流道编码
                                  I_creator  bill_om_recheck.creator%type,
                                  O_msg      OUT VARCHAR2 --输出信息
                                  );

  /*
      功能：城市仓分货交接
     作者：LONG.QW
     日期：2013-10-14
  */
  PROCEDURE PROC_OM_RECHECK(I_locno     bm_defloc.locno%type, --仓别
                            I_reCheckNo bill_om_recheck.recheck_no%type, --复核单号
                            I_boxNo     con_label.scan_label_no%type, --标签号
                            I_creator   varchar2,--交接人
                            O_msg       OUT VARCHAR2 --输出信息
                            );
  /*
   功能：城市仓-RF分货贴标,记录扫描日志
   作者：HUANG.JG
   日期：2013-11-12 
  */
  PROCEDURE PROC_OM_DIVIDESCAN(I_locno    bm_defloc.locno%type, --仓别
                               I_divideNo bill_om_divide.divide_No%type, --分货单号
                               I_boxNo    bm_scan_log.merge_box_no%type, --箱号
                               I_creator  bm_scan_log.creator%type, --分货贴标人
                               O_msg      OUT VARCHAR2 --输出信息
                               );
  /*
   功能：城市仓-分货差异处理
   根据分货单号、商品条码找出该商品是哪个门店要的货
   输出流道编码、客户、品牌、款号
   作者：HUANG.JG
   日期：2013-11-13 
  */
  PROCEDURE PROC_OM_DIVIDEDIFF(I_locno     bm_defloc.locno%type, --仓不饿
                               I_divideNo  bill_om_divide.divide_no%type, --分货单号
                               I_barcode   item_barcode.barcode%type, --商品条码
                               O_serialNo  OUT bill_om_divide_dtl.serial_no%type, --流道编码
                               O_storeNo   OUT store.store_no%type, --客户编码
                               O_storeName OUT store.store_name%type, --客户名称
                               O_brandName OUT brand.brand_name%type, --品牌
                               O_styleNo   OUT item.style_no%type, --款号
                               O_flag      OUT number, --串码、串款标识,0:正常,1:串码,2:串款
                               O_msg       OUT VARCHAR2 --输出信息
                               );
  /*
   功能：城市仓-分货差异处理确认
   对有差异的分货任务进行再次分货处理,更新复核单实际数量
   作者：HUANG.JG
   日期：2013-11-13 
  */
  PROCEDURE PROC_OM_DIVIDEDIFF_CONFIRM(I_locno    bm_defloc.locno%type,
                                       I_divideNo bill_om_divide.divide_no%type, --分货单号
                                       I_serialNo bill_om_divide_dtl.serial_no%type, --流道编码
                                       I_boxNo    con_box.box_no%type, --箱号,可空,为空时则会自动产生一个箱号
                                       I_barcode  item_barcode.barcode%type, --商品条码                                       
                                       I_qty      number, --分货数量
                                       I_creator  bill_om_recheck_dtl.recheck_name%type,
                                       O_newBoxNo OUT con_box.box_no%type, --输出新箱号
                                       O_msg      OUT VARCHAR2 --输出信息
                                       );
                                       
  /*
   功能：城市仓-分货复核单删除
   删除状态为10的分货复核单,回写分货单数量、删除标签表信息
   作者：SU.YQ
   日期：2013-11-30
  */
  PROCEDURE PROC_OM_RECHECK_DEL(I_locno IN bill_om_recheck.locno%type,
                                I_recheckNo IN bill_om_recheck.recheck_no%type, --复核单号
                                O_msg      OUT VARCHAR2 --输出信息
                                );
  /*
   功能：城市仓-更新分货单、复核单状态
   作者：HUANG.JG
   日期：2013-12-18
  */
  PROCEDURE PROC_OM_UpdateStatus_ByDivide(I_locno     bm_defloc.locno%type,
                                          I_divideNo  bill_om_divide.divide_no%type, --分货单号
                                          I_recheckNo bill_om_recheck.recheck_no%type,--复核单号
                                          I_serialNo  bill_om_divide_dtl.serial_no%type,--流道编码
                                          I_creator   varchar2,
                                          O_msg       OUT VARCHAR2 --输出信息
                                          );
end PKG_OM_CITY_RECHECK;
/
create or replace package body PKG_OM_CITY_RECHECK is
  /*
      功能：城市仓出库复核-整箱复核
     作者：HUANG.JG
     日期：2013-9-29
     modify by huangjg at 2013-12-17 复核单加recheck_type=0区别分货复核、波次复核
     modify by huangjg at 2013-12-18 更新单据状态时记录单据状态日志表bill_status_log
  */
  PROCEDURE PROC_OM_CREATERECHECKDTLZX(I_locno    bm_defloc.locno%type, --仓别
                                       I_divideNo bill_om_divide.divide_no%type, --分货单号
                                       I_boxNo    con_box.box_no%type, --箱号
                                       I_serialNo bill_om_divide_dtl.serial_no%type, --流道编码
                                       I_creator  bill_om_recheck.creator%type,
                                       O_msg      OUT VARCHAR2 --输出信息
                                       ) is
    v_storeNo          bill_om_divide_dtl.store_no%type; --门店编码
    v_lineNo           os_defline.line_no%type; --线路
    v_recheckNo        bill_om_recheck.recheck_no%type; --复核单号
    v_MasterStauts     bill_om_divide.status%type; --分货单状态
    v_CheckStatus      bill_om_recheck.status%type; --复核单状态
    v_expDate          bill_om_divide_dtl.exp_date%type;
    v_storeQty         number; --箱子所属门店数
    v_canCheckQty      number; --可复核数
    v_rowid            number;
    v_strLabelNo       con_label.label_no%type;
    v_strContainerNo   con_label.container_no%type;
    v_strContainerType con_label.container_type%type;
    v_nSessionId       number;
  begin
    v_canCheckQty      := '0';
    v_rowid            := 0;
    v_strContainerType := 'C';
    --分货单是否已结案
    begin
      select status
        into v_MasterStauts
        from bill_om_divide
       where locno = I_locno
         and divide_no = I_divideNo;
    exception
      when no_data_found then
        O_msg := 'N|分货单号:' || I_divideNo || '不存在,请核实!';
        return;
    end;
    if v_MasterStauts = '35' then
      O_msg := 'N|分货单号:' || I_divideNo || '已复核完成!';
      return;
    end if;
  
    --统计箱号所属门店数
    begin
      select count(distinct store_no),
             max(store_no),
             max(line_no),
             max(exp_date),
             sum(item_qty - nvl(real_qty,0))
        into v_storeQty, v_storeNo, v_lineNo, v_expDate, v_canCheckQty
        from bill_om_divide_dtl
       where locno = I_locno
         and divide_no = I_divideNo
         and box_no = I_boxNo;
    exception
      when no_data_found then
        O_msg := 'N|箱号:' || I_boxNo || '无待复核数据,请核实!';
        return;
    end;
    if v_storeQty > 1 then
      O_msg := 'N|箱号:' || I_boxNo || '有多个门店配送,需拆箱复核!';
      return;
    end if;
  
    --判断箱号是否有待复核数量
    if v_canCheckQty = 0 then
      O_msg := 'N|箱号:' || I_boxNo || '待复核数量为0,请核实!';
      return;
    end if;
  
    --判断门店是否有复核单
    --1.若有没有复核单则自动写复核头档;
    --2.若有复核单并且已结案则拦截(一个门店对应一个复核单)
    begin
      select recheck_no, status
        into v_recheckNo, v_CheckStatus
        from bill_om_recheck
       where locno = I_locno
         and divide_no = I_divideNo
         and store_no = v_storeNo;
    exception
      when no_data_found then
        --产生复核单号
        PKG_WMS_BASE.proc_getsheetno(I_locno, 'OC', v_recheckNo, O_msg);
        if instr(O_msg, 'N', 1, 1) = 1 then
          O_msg := 'N|获取复核单号失败!';
          return;
        end if;
        v_CheckStatus := '10';
        --按门店写复核头档
        insert into bill_om_recheck
          (locno,recheck_no,line_no,store_no,status,recheck_type,
           creator,createtm,editor,edittm,serial_no,exp_date,divide_no)
        values
          (I_locno,v_recheckNo,v_lineNo,v_storeNo,'10','0',
           I_creator,sysdate,I_creator,sysdate,I_serialNo,v_expDate,I_divideNo);
    end;
    --复核单已结案拦截
    if v_CheckStatus <> '10' then
      O_msg := 'N|复核单:' || v_recheckNo || '不是建单状态,不能进行复核操作!';
      return;
    end if;
  
    --新取容器号
    Pkg_Label.proc_get_ContainerNoBase(I_locNo,
                                       v_strContainerType,
                                       I_creator,
                                       '',
                                       1,
                                       '1',
                                       '',
                                       v_strLabelNo,
                                       v_strContainerNo,
                                       v_nSessionId,
                                       O_msg);
    if instr(O_msg, 'N', 1, 1) = 1 then
      return;
    end if;
  
    --写标签头档
    insert into con_label
      (locno,source_no,label_no,recheck_no,container_no,container_type,
       status,load_container_no,owner_container_no,owner_cell_no,store_no,
       line_no,scan_label_no,exp_date,creator,createtm)
    values
      (I_locno,I_divideNo,v_strLabelNo,v_recheckNo,v_strContainerNo,v_strContainerType,
       '52',v_strContainerNo,v_strContainerNo,'N',v_storeNo,
       v_lineNo,I_boxNo,v_expDate,I_creator,sysdate);
    --写标签明细
    v_rowid := 0;
    select nvl(max(row_id), 0)
      into v_rowid
      from con_label_dtl
     where locno = I_locno
       and container_no = v_strContainerNo;
    insert into con_label_dtl
      (locno,owner_no,source_no,container_no,container_type,item_no,item_id,
       pack_qty,qty,exp_no,store_no,line_no,status,row_id,divide_id,
       creator,createtm,exp_date,scan_label_no,size_no)
      select locNo, owner_no,v_recheckNo,v_strContainerNo,v_strContainerType,
             Item_No,Item_Id,pack_qty,Item_Qty,Exp_No,store_no,line_no,
             '52',v_rowid + rownum,divide_id,I_creator,sysdate,Exp_Date,
             Box_No,Size_No
        from bill_om_divide_dtl
       where locno = I_locno
         and divide_no = I_divideNo
         and store_no = v_storeNo
         and box_no = I_boxNo
         and serial_no = I_serialNo
         order by item_no,size_no;
    if sql%rowcount <= 0 then
      O_msg := 'N|新增标签明细失败';
      return;
    end if;
    --取最大rowid
    v_rowid := 0;
    select nvl(max(row_id), 0)
      into v_rowid
      from bill_om_recheck_dtl
     where locno = I_locno
       and recheck_no = v_recheckNo;
    --批量写复核明细
    insert into bill_om_recheck_dtl
      (locno,owner_no,recheck_no,row_id,container_no,item_no,item_id,
       item_qty,real_qty,status,assign_name,recheck_name,
       recheck_date,exp_no,exp_type,exp_date,pack_qty,size_no,box_no)
      select I_locno,owner_no,v_recheckNo,v_rowid + rownum,v_strContainerNo,
             item_no,item_id,item_qty,item_qty,'10',I_creator,I_creator,
             sysdate,exp_no,exp_type,exp_date,nvl(pack_qty,1),size_no,I_boxNo
        from bill_om_divide_dtl
       where locno = I_locno
         and divide_no = I_divideNo
         and store_no = v_storeNo
         and serial_no = I_serialNo
         and box_no = I_boxNo
         order by item_no,item_id;
    if sql%rowcount = 0 then
      O_msg := 'N|写复核明细失败(0行)!';
      return;
    end if;
    --更新分货明细复核数量
    update bill_om_divide_dtl
       set status = '13', real_qty = item_qty
     where locno = I_locno
       and divide_no = I_divideNo
       and box_no = I_boxNo
       and store_no = v_storeNo
       and serial_no = I_serialNo;
    if sql%rowcount = 0 then
      O_msg := 'N|箱号:' || I_boxNo || '更新分货单复核数量失败(0行)!';
      return;
    end if;
    
    --更新分货单、复核单状态
    PROC_OM_UpdateStatus_ByDivide(I_locno,I_divideNo,v_recheckNo,I_serialNo,I_creator,O_msg);
    if instr(O_msg, 'N', 1, 1) = 1 then
        return;
    end if;
   
    O_msg := 'Y|';
  EXCEPTION
    WHEN OTHERS THEN
      O_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END PROC_OM_CREATERECHECKDTLZX;

  /*
      功能：城市仓出库复核-拆箱复核
     作者：HUANG.JG
     日期：2013-10-11
     modify by huangjg at 2013-12-17 复核单加recheck_type=0区别分货复核、波次复核
  */
  PROCEDURE PROC_OM_CREATERECHECKDTLCX(I_locno    bm_defloc.locno%type, --仓别
                                       I_divideNo bill_om_divide.divide_no%type, --分货单号
                                       I_barcode  item_barcode.barcode%type, --商品条码
                                       I_serialNo bill_om_divide_dtl.serial_no%type, --流道编码
                                       I_creator  bill_om_recheck.creator%type,
                                       O_msg      OUT VARCHAR2 --输出信息
                                       ) is
    v_itemNo       item.item_no%type; --商品编码
    v_rowid        number;
    v_itemId       bill_om_divide_dtl.item_id%type;
    v_sizeNo       item_barcode.size_no%type;
    v_storeNo      bill_om_divide_dtl.store_no%type; --门店编码
    v_boxNo        bill_om_divide_dtl.box_no%type;
    v_lineNo       os_defline.line_no%type; --线路
    v_recheckNo    bill_om_recheck.recheck_no%type; --复核单号
    v_MasterStauts bill_om_divide.status%type; --分货单状态
    v_CheckStatus  bill_om_recheck.status%type; --复核单状态
    v_expDate      bill_om_divide_dtl.exp_date%type;
    v_divideID     bill_om_divide_dtl.divide_id%type;
    v_ownerNo      bill_om_divide_dtl.owner_no%type;
    v_divideQty    number;
  begin
    v_divideQty := 1;
    --分货单是否已结案
    begin
      select status
        into v_MasterStauts
        from bill_om_divide
       where locno = I_locno
         and divide_no = I_divideNo;
    exception
      when no_data_found then
        O_msg := 'N|分货单号:' || I_divideNo || '不存在,请核实!';
        return;
    end;
    if v_MasterStauts = '35' then
      O_msg := 'N|分货单号:' || I_divideNo || '已复核完成!';
    end if;
  
    --根据条码查找商品编码
    begin
      select item_no, size_No
        into v_itemNo, v_sizeNo
        from item_barcode
       where barcode = I_barcode
         and package_id ='0'
         and rownum = 1;
    exception
      when no_data_found then
        O_msg := 'N|商品条码:' || I_barcode || '在商品档案中不存在!';
        return;
    end;
    --查找门店
    begin
      select store_no, line_no,box_no, exp_date, divide_id, owner_no, item_id
        into v_storeNo,v_lineNo,v_boxNo,v_expDate,v_divideID,v_ownerNo,v_itemId
        from (select dtl.store_no,dtl.line_no,dtl.box_no,dtl.exp_date,dtl.divide_id,dtl.owner_no,dtl.item_id
                from bill_om_divide_dtl dtl
               where dtl.locno = I_locno
                 and dtl.divide_no = I_divideNo
                 and dtl.item_no = v_itemNo
                 and dtl.size_no = v_sizeNo
                 and dtl.serial_no = I_serialNo
                 and dtl.item_qty - nvl(dtl.real_qty, 0) > 0
               order by dtl.item_qty - nvl(dtl.real_qty, 0))
       where rownum = 1;
    exception
      when no_data_found then
        O_msg := 'N|商品条码:' || I_barcode || '无待复核数据,请核实!';
        return;
    end;
  
    --判断门店是否有复核单
    --1.若有没有复核单则自动写复核头档;
    --2.若有复核单并且已结案则拦截(一个门店对应一个复核单)
    begin
      select recheck_no, status into v_recheckNo, v_CheckStatus
       from bill_om_recheck
       where locno = I_locno 
       and divide_no = I_divideNo 
       and store_no = v_storeNo;
    exception
      when no_data_found then
        --产生复核单号
        PKG_WMS_BASE.proc_getsheetno(I_locno, 'OC', v_recheckNo, O_msg);
        if instr(O_msg, 'N', 1, 1) = 1 then
          O_msg := 'N|获取复核单号失败!';
          return;
        end if;
        v_CheckStatus := '10';
        --按门店写复核头档
        insert into bill_om_recheck
          (locno,recheck_no,line_no,store_no,status,recheck_type,
           creator,createtm,editor,edittm,serial_no,exp_date,divide_no)
        values
          (I_locno,v_recheckNo,v_lineNo,v_storeNo,'10','0',I_creator,sysdate,
          I_creator,sysdate,I_serialNo,v_expDate,I_divideNo);
    end;
    --复核单已结案拦截
    if v_CheckStatus <> '10' then
      O_msg := 'N|复核单:' || v_recheckNo || '不是建单状态,不能进行复核操作!';
      return;
    end if;
    --更新复核明细数量,未更新到时则新增
    update bill_om_recheck_dtl
       set real_qty = nvl(real_qty, 0) + v_divideQty,
           item_qty = nvl(item_qty, 0) + v_divideQty
     where locno = I_locno
       and owner_no = v_ownerNo
       and recheck_no = v_recheckNo
       and box_no = v_boxNo
       and item_no = v_itemNo
       and nvl(item_id,'0') = nvl(v_itemId,'0')
       and size_no = v_sizeNo
       and status = '10';
    if sql%rowcount = 0 then
      --取最大rowid
      v_rowid := 0;
      select nvl(max(row_id), 0)
        into v_rowid
        from bill_om_recheck_dtl
       where locno = I_locno
         and recheck_no = v_recheckNo;
      --写复核明细
      insert into bill_om_recheck_dtl
        (locno,owner_no,recheck_no,row_id,container_no,item_no,
         size_no,item_id,item_qty,real_qty,status,assign_name,
         recheck_name,recheck_date,exp_no,exp_type,exp_date,pack_qty,box_no)
        select I_locno,v_ownerNo, v_recheckNo,v_rowid + rownum,'N',--封箱时回写
               dtl.item_no,dtl.size_no,dtl.item_id,v_divideQty,v_divideQty,
               '10',I_creator,I_creator,sysdate, exp_no,
               exp_type,exp_date,nvl(dtl.pack_qty,1),dtl.box_no
          from bill_om_divide_dtl dtl
         where dtl.locno = I_locno
           and dtl.divide_No = I_divideNo
           and dtl.owner_no = v_ownerNo
           and dtl.divide_id = v_divideID
           order by dtl.item_No,dtl.size_no;
      if sql%rowcount = 0 then
        O_msg := 'N|写复核明细失败(0行)!';
        return;
      end if;
    end if;
    --更新分货明细复核数量
    update bill_om_divide_dtl
       set real_qty  = nvl(real_qty, 0) + v_divideQty,
           status = case
                      when item_qty = nvl(real_qty, 0) + v_divideQty then
                       '13'
                      else
                       status
                    end
     where locno = I_locno
       and divide_no = I_divideNo
       and owner_no = v_ownerNo
       and divide_id = v_divideId;
    if sql%rowcount = 0 then
      O_msg := 'N|商品条码:' || I_barcode || '待复核数量为0!';
      return;
    end if;
    O_msg := 'Y|';
  EXCEPTION
    WHEN OTHERS THEN
      O_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END PROC_OM_CREATERECHECKDTLCX;

  /*
      功能：城市仓出库复核-封箱
     作者：HUANG.JG
     日期：2013-10-11
     modi by he.w 2013-12-05 line 667 
  */
  PROCEDURE PROC_OM_CREATERELabel(I_locno    bm_defloc.locno%type, --仓别
                                  I_divideNo bill_om_divide.divide_no%type, --分货单号
                                  I_serialNo bill_om_divide_dtl.serial_no%type, --流道编码
                                  I_creator  bill_om_recheck.creator%type,
                                  O_msg      OUT VARCHAR2 --输出信息
                                  ) is
    v_storeNo           bill_om_divide_dtl.store_no%type; --门店编码
    v_recheckNo         bill_om_recheck.recheck_no%type; --复核单号
    v_expNo             bill_om_divide_dtl.exp_no%type;
    v_lineNo            os_defline.line_no%type; --线路
    v_CheckStatus       bill_om_recheck.status%type; --复核单状态
    v_MasterStauts      bill_om_divide.status%type; --分货单状态
    v_expDate           bill_om_divide_dtl.exp_date%type;
    v_rowid             number;
    v_strLabelNo        con_label.label_no%type;
    v_strContainerNo    con_label.container_no%type;
    v_strContainerType  con_label.container_type%type;
    v_nSessionId        number;
    v_ownerNo           bm_Defowner.owner_no%type;
    v_strScanLabelNo    con_label.scan_label_no%type;
    v_strContainerNo1   con_label.container_no%type;
    v_strContainerType1 varchar2(10);
    v_totalQty          number;
  begin
    v_rowid             := 0;
    v_strContainerType  := 'C';
    v_strContainerType1 := 'T';
    v_totalQty          := 0;
    --分货单是否已结案
    begin
      select status
        into v_MasterStauts
        from bill_om_divide
       where locno = I_locno
         and divide_no = I_divideNo;
    exception
      when no_data_found then
        O_msg := 'N|分货单号:' || I_divideNo || '不存在,请核实!';
        return;
    end;
    if v_MasterStauts = '35' then
      O_msg := 'N|分货单号:' || I_divideNo || '已复核完成!';
    end if;
    --查找复核数据
    select count(1),max(dtl.store_no),max(dtl.line_no),max(dtl.exp_date),
           max(dtl.owner_no),max(dtl.exp_no),sum(nvl(dtl.real_qty, 0))
      into v_rowid,v_storeNo,v_lineNo,v_expDate,v_ownerNo,v_expNo,v_totalQty
      from bill_om_divide_dtl dtl
     where dtl.locno = I_locno
       and dtl.divide_No = I_divideNo
       and dtl.serial_no = I_serialNo
       and nvl(dtl.real_Qty, 0) > 0;
    if v_rowid = 0 then
      O_msg := 'N|流道编码:' || I_serialNo || '无复核数据,不能封箱!';
      return;
    end if;
    --查找复核单号
    begin
    select recheck_no, status
      into v_recheckNo, v_CheckStatus
      from bill_om_recheck
     where locno = I_locno
       and divide_no = I_divideNo
       and store_no = v_storeNo
       and status='10';
    exception when no_data_found then
     O_msg := 'N|流道编码:' || I_serialNo || '无复核单或已复核完成!';
      return;
    end;

    --取箱号
    Pkg_Label.proc_get_ContainerNoBase(I_locNo,
                                       v_strContainerType1,
                                       I_creator,
                                       '',
                                       1,
                                       '1',
                                       '11',
                                       v_strScanLabelNo,
                                       v_strContainerNo1,
                                       v_nSessionId,
                                       O_msg);
    if instr(O_msg, 'N', 1, 1) = 1 then
      return;
    end if;
    v_strScanLabelNo := v_strScanLabelNo || 'C';
    --写箱头档
    insert into con_box
      (locno,owner_no,box_no,creat_date,import_date,
       status,qty,s_import_no,export_no,store_no)
    values
      (I_locno,v_ownerNo,v_strScanLabelNo,sysdate,sysdate,
       '2',v_totalQty,'N',v_expNo,v_storeNo);
    --箱明细
    insert into con_box_dtl
      (locno, owner_no, box_no, box_id, item_no, style_no, size_no, qty)
      select I_locno,v_ownerNo,v_strScanLabelNo,rownum,
             a.Item_No,(select nvl(max(item.style_no), 'N') from item where item.item_no=a.item_no),
             a.size_no,a.real_qty
        from bill_om_recheck_dtl a
       inner join bill_om_recheck b
          on a.locno = b.locno
         and a.recheck_no = b.recheck_no
       where a.locno = I_locno
         and a.status = '10'
         and b.divide_no = I_divideNo
         and b.store_no = v_storeNo
       order by a.item_no,a.size_no;
    if sql%rowcount <= 0 then
      O_msg := 'N|新增箱明细失败,未找到复核数据!';
      return;
    end if;
    --回写箱数量
    update con_box
       set qty =
           (select nvl(sum(qty),0)
              from con_box_dtl
             where locno = I_locno
               and box_no = v_strScanLabelNo
               and owner_no = v_ownerNo)
     where locno = I_locno
       and box_no = v_strScanLabelNo
       and owner_no = v_ownerNo;
    --新取容器号
    Pkg_Label.proc_get_ContainerNoBase(I_locNo,
                                       v_strContainerType,
                                       I_creator,
                                       '',
                                       1,
                                       '1',
                                       '',
                                       v_strLabelNo,
                                       v_strContainerNo,
                                       v_nSessionId,
                                       O_msg);
    if instr(O_msg, 'N', 1, 1) = 1 then
      return;
    end if;
    --写标签头档
    insert into con_label
      (locno,source_no,label_no,recheck_no,container_no,container_type,
       status,load_container_no,owner_container_no,owner_cell_no,scan_label_no,
       store_no,line_no,exp_date,creator,createtm)
    values
      (I_locno,I_divideNo,v_strLabelNo,v_recheckNo,v_strContainerNo,v_strContainerType,
       '52',v_strContainerNo,v_strContainerNo,'N',v_strScanLabelNo,
       v_storeNo,v_lineNo,v_expDate,I_creator,sysdate);
  
    --写标签明细
    v_rowid := 0;
    select nvl(max(row_id), 0) into v_rowid
      from con_label_dtl
     where locno = I_locno
       and container_no = v_strContainerNo;
    insert into con_label_dtl
      (locno,owner_no,source_no,container_no,container_type,item_no,
       item_id,pack_qty,qty,exp_no,store_no,line_no,status,row_id,
       creator,createtm,exp_date,size_no,divide_id,scan_label_no)
      select a.locNo,a.owner_no,v_recheckNo,v_strContainerNo,v_strContainerType,
             a.Item_No,a.Item_Id,a.pack_qty,a.real_Qty,a.Exp_No,v_storeNo,
             v_lineNo,'52',v_rowid + rownum,I_creator,sysdate,a.Exp_Date,a.Size_No,
             (select nvl(max(divide_id),0) from bill_om_divide_dtl dtl 
              where dtl.locno = I_locno and dtl.divide_no = I_divideNo
              and dtl.item_no = a.item_no and dtl.size_no = a.size_no
              and dtl.box_no=a.box_no and dtl.store_no = v_storeNo
              and nvl(dtl.item_id,'0') = nvl(a.item_id,'0')
              ),v_strScanLabelNo
        from bill_om_recheck_dtl a
       inner join bill_om_recheck b
          on a.locno = b.locno
         and a.recheck_no = b.recheck_no
        left join item
          on a.item_no = item.item_no
       where a.locno = I_locno
         and a.status = '10'
         and b.divide_no = I_divideNo
         and b.store_no = v_storeNo
         order by a.item_no,a.size_no;
    if sql%rowcount <= 0 then
      O_msg := 'N|新增标签明细失败,未找到复核数据!';
      return;
    end if;
  
    --更新复核明细为完结,回写标签号
    update bill_om_recheck_dtl dtl
       set dtl.status = '13', container_no = v_strContainerNo
     where dtl.locno = I_locno
       and dtl.owner_no = v_ownerNo
       and dtl.recheck_no = v_recheckNo
       and dtl.status = '10'
       and exists (select 'X'
              from bill_om_recheck chk
             where chk.locno = dtl.locno
               and chk.recheck_no = v_recheckNo
               and chk.store_no = v_storeNo
               and chk.divide_no = I_divideNo);
    if sql%rowcount = 0 then
      O_msg := 'N|更新复核明细失败(0行)!';
      return;
    end if;
    
    --更新分货单、复核单状态
    PROC_OM_UpdateStatus_ByDivide(I_locno,I_divideNo,v_recheckNo,I_serialNo,I_creator,O_msg);
    if instr(O_msg, 'N', 1, 1) = 1 then
        return;
    end if;                         
    
    O_msg := 'Y|';
  EXCEPTION
    WHEN OTHERS THEN
      O_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END PROC_OM_CREATERELabel;

  /*
      功能：分货交接
     作者：LONG.QW
     日期：2013-10-14
     modify by huangjg at 2013-12-12 分货交接时将标签头档、明细状态改为待装车A0
     modify by huangjg at 2013-12-18 更新单据状态时记录单据状态日志表bill_status_log
  */
  PROCEDURE PROC_OM_RECHECK(I_locno     bm_defloc.locno%type, --仓别
                            I_reCheckNo bill_om_recheck.recheck_no%type, --复核单号
                            I_boxNo     con_label.scan_label_no%type, --标签号
                            I_creator   varchar2,--交接人
                            O_msg       OUT VARCHAR2 --输出信息
                            ) is
    v_status      bill_om_recheck.status%type;
    v_containerNo bill_om_recheck_dtl.container_no%type;
    v_dtlStatus   bill_om_recheck_dtl.status%type;
    v_cellNo      con_content.cell_no%type;
    v_lineNo      bill_om_recheck.line_no%type; --线路
    v_storeNo     bill_om_recheck.store_no%type;
  begin
    --根据扫描标签号查找容器号
    begin
      select chk.status,dtl.container_No,dtl.status,chk.line_no,chk.store_no
        into v_status, v_containerNo, v_dtlStatus, v_lineNo, v_storeNo
        from con_label lbl
       inner join bill_om_recheck_dtl dtl
          on lbl.locno = dtl.locno
         and lbl.container_no = dtl.container_no
         and lbl.recheck_no = dtl.recheck_no
       inner join bill_om_recheck chk
          on dtl.locno = chk.locno
         and dtl.recheck_no = chk.recheck_no
       where rownum = 1
         and lbl.locno = I_locno
         and lbl.scan_label_no = I_boxNo
         and lbl.recheck_no = I_reCheckNo;
    exception
      when no_data_found then
        O_msg := 'N|箱条码:' || I_boxNo || '不属于该复核单!';
        return;
    end;
    if v_status = '10' then
      O_msg := 'N|复核单:' || I_reCheckNo || '还未完成复核!';
      return;
    elsif v_status = '14' then
      O_msg := 'N|复核单:' || I_reCheckNo || '已完成交接!';
      return;
    end if;
    if v_dtlStatus = '14' then
      O_msg := 'N|箱条码:' || I_boxNo || '已完成交接!';
      return;
    end if;
    --更新标签状态为待装车
    update con_label lbl
       set lbl.status = 'A0'
     where lbl.locno = I_locno
       and lbl.recheck_no = I_reCheckNo
       and lbl.container_no = v_containerNo
       and lbl.scan_label_no = I_boxNo;
    if sql%rowcount = 0 then
      O_msg := 'N|箱号:' || I_boxNo || '更新标签状态失败!';
      return;
    end if;
    update con_label_dtl dtl
       set dtl.status = 'A0'
     where dtl.locno = I_locno
       and dtl.container_no = v_containerNo
       and dtl.scan_label_no = I_boxNo;
    if sql%rowcount = 0 then
      O_msg := 'N|箱号:' || I_boxNo || '更新标签明细状态失败!';
      return;
    end if;
    --更新复核单明细状态
    update bill_om_recheck_dtl rd
       set rd.status = '14'
     where rd.locno = I_locno
       and rd.container_no = v_containerNo
       and rd.recheck_no = I_reCheckNo
       and rd.status = '13';
    if sql%rowcount = 0 then
      O_msg := 'N|复核单:' || I_reCheckNo || '明细状态更新失败(0行)!';
      return;
    end if;
    --更新复核单头档状态
    update bill_om_recheck r
       set r.status = '14'
     where r.locno = I_locno
       and r.recheck_no = I_reCheckNo
       and not exists (select 'X'
              from bill_om_recheck_dtl dtl
             where dtl.locno = I_locno
               and dtl.recheck_no = I_reCheckNo
               and dtl.status = '13');
     if sql%rowcount >0 then
      --写状态日志表
      Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                           I_recheckNo,
                                           'OM',
                                           '14',
                                           '分货交接,更新复核单状态为:集货完成',
                                           I_creator,
                                           O_msg);
     end if;
    v_cellNo := '';
   begin
     select cell_no
       into v_cellNo
       from os_cust_buffer
      where locno = I_locno
        and store_no = v_storeNo
        and rownum=1;
   exception
     when no_data_found then
       O_msg := 'N|获取客户暂存区储位失败,无可用的储位!';
       return;
   end;
    --回写库存储位
    update con_content
       set cell_no = v_cellNo
     where locno = I_locno
       and label_no = I_boxNo
       and cell_no = 'N';
    if sql%rowcount = 0 then
      O_msg := 'N|回写库存储位失败,未找到库存!';
      return;
    end if;
    update con_content_move
       set cell_no = v_cellNo
     where locno = I_locno
       and container_no = I_boxNo
       and cell_no = 'N';
    O_msg := 'Y|';
  EXCEPTION
    WHEN OTHERS THEN
      O_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END PROC_OM_RECHECK;

  /*
   功能：城市仓-RF分货贴标,记录扫描日志
   作者：HUANG.JG
   日期：2013-11-12
  */
  PROCEDURE PROC_OM_DIVIDESCAN(I_locno    bm_defloc.locno%type, --仓别
                               I_divideNo bill_om_divide.divide_No%type, --分货单号
                               I_boxNo    bm_scan_log.merge_box_no%type, --箱号
                               I_creator  bm_scan_log.creator%type, --分货贴标人
                               O_msg      OUT VARCHAR2 --输出信息
                               ) IS
    v_qty number;
  BEGIN
    v_qty := 0;
    begin
      select sum(nvl(item_qty, 0))
        into v_qty
        from bill_om_divide_dtl
       where locno = I_locno
         and divide_no = I_divideNo
         and box_no = I_boxNo;
    exception
      when no_data_found then
        v_qty := 0;
    end;
    update bm_scan_log
       set creator = I_creator, createtm = sysdate
     where locno = I_locno
       and source_no = I_divideNo
       and merge_box_no = I_boxNo
       and scan_code = I_boxNo
       and nvl(item_no, 'N') = 'N';
    if sql%rowcount = 0 then
      --写扫描日志
      insert into bm_scan_log
        (locno,scan_code,source_no,item_no,size_no,qty,
         creator,createtm,merge_box_no,batch_serial_no)
      values
        (I_locno,I_boxNo,I_divideNo,'N','N',v_qty,
         I_creator,sysdate,I_boxNo,'N');
    end if;
    O_msg := 'Y|';
  EXCEPTION
    WHEN OTHERS THEN
      O_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END PROC_OM_DIVIDESCAN;

  /*
   功能：城市仓-分货差异处理
   根据分货单号、商品条码找出该商品是哪个门店要的货
   输出流道编码、客户、品牌、款号
   作者：HUANG.JG
   日期：2013-11-13
  */
  PROCEDURE PROC_OM_DIVIDEDIFF(I_locno     bm_defloc.locno%type, --仓不饿
                               I_divideNo  bill_om_divide.divide_no%type, --分货单号
                               I_barcode   item_barcode.barcode%type, --商品条码
                               O_serialNo  OUT bill_om_divide_dtl.serial_no%type, --流道编码
                               O_storeNo   OUT store.store_no%type, --客户编码
                               O_storeName OUT store.store_name%type, --客户名称
                               O_brandName OUT brand.brand_name%type, --品牌
                               O_styleNo   OUT item.style_no%type, --款号
                               O_flag      OUT number, --串码、串款标识,0:正常,1:串码,2:串款
                               O_msg       OUT VARCHAR2 --输出信息
                               ) IS
    v_status   bill_om_divide.status%type;
    v_strItemNo   item.item_no%type;
    v_strSizeNo   item_barcode.barcode%type;
    v_strNum number:=0;
  BEGIN
    begin
      select status into v_status
        from bill_om_divide
       where locno = I_locno
         and divide_no = I_divideNo;
    exception
      when no_data_found then
        O_msg := 'N|分货单号:' || I_divideNo || '不存在!';
        return;
    end;
    if v_status = '35' then
      O_msg := 'N|分货单号:' || I_divideNo || '已复核完成!';
      return;
    end if;
    begin
    select bar.item_no,bar.size_no,br.brand_name,item.style_no
      into v_strItemNo,v_strSizeNo,O_brandName,O_styleNo
      from item_barcode bar
      inner join item 
      on bar.item_no=item.item_No
      left join brand br
      on item.brand_no=br.brand_no
     where bar.barcode = I_barcode and rownum=1
                 and bar.package_id ='0';
    exception
      when no_data_found then
      O_msg := 'N|商品条码:' || I_barcode || '在商品资料中不存在!';
      return;
    end;
    begin
      --按商品、尺码查找
      select dtl.serial_no,
             dtl.store_no,
             (select s.store_name
                from store s
               where s.store_type = '11'
                 and s.store_no = dtl.store_no) store_name,
             '0'
        into O_serialNo, O_storeNo, O_storeName, O_flag
        from bill_om_divide_dtl dtl
       where dtl.locno = I_locno
         and dtl.divide_no = I_divideNo
         and dtl.item_no = v_strItemNo
         and dtl.size_no = v_strSizeNo
         and dtl.status <> '13'
         and rownum = 1;
    exception
      when no_data_found then
        O_flag := 10;
    end;
    --按商品、尺码未找到分货任务明细时则只带商品编码查找即串码
    if O_flag = 10 then
        select count(1) into v_strNum
        from bill_om_divide_dtl dtl
        where dtl.locno = I_locno
        and dtl.divide_no = I_divideNo
        and dtl.item_no = v_strItemNo
        and dtl.status <> '13'
        order by dtl.serial_no;
     if v_strNum=0 then
          O_flag := 2;--串款
          O_msg  := 'N|分货单:' || I_divideNo || '不存在分货商品:' || I_barcode ||
                    '(串款)';
          return;
      else
        O_flag:=1;--串码
      end if;
      
    end if;
    O_msg := 'Y|';
  EXCEPTION
    WHEN OTHERS THEN
      O_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END PROC_OM_DIVIDEDIFF;

  /*
   功能：城市仓-分货差异处理确认
   对有差异的分货任务进行再次分货处理,更新复核单实际数量
   作者：HUANG.JG
   日期：2013-11-13
   modify by huangjg at 2013-12-17 复核单加recheck_type=0区别分货复核、波次复核
  */
  PROCEDURE PROC_OM_DIVIDEDIFF_CONFIRM(I_locno    bm_defloc.locno%type,
                                       I_divideNo bill_om_divide.divide_no%type, --分货单号
                                       I_serialNo bill_om_divide_dtl.serial_no%type, --流道编码
                                       I_boxNo    con_box.box_no%type, --箱号,可空,为空时则会自动产生一个箱号
                                       I_barcode  item_barcode.barcode%type, --商品条码
                                       I_qty      number, --分货数量
                                       I_creator  bill_om_recheck_dtl.recheck_name%type,
                                       O_newBoxNo OUT con_box.box_no%type, --输出新箱号
                                       O_msg      OUT VARCHAR2 --输出信息
                                       ) IS
    v_status            bill_om_divide.status%type;
    v_recheckNo         bill_om_recheck.recheck_no%type; --复核单号
    v_CheckStatus       bill_om_recheck.status%type; --复核单状态
    v_lineNo            bill_om_divide_dtl.line_no%type; --线路
    v_rowid             number;
    v_strLabelNo        con_label.label_no%type;
    v_strContainerNo    con_label.container_no%type;
    v_strContainerType  con_label.container_type%type;
    v_expDate           bill_om_divide_dtl.exp_date%type;
    v_nSessionId        number;
    v_cellNo            cm_defcell.cell_no%type;
    v_ownerNo           bill_om_divide_dtl.owner_no%type;
    v_strScanLabelNo    con_label.scan_label_no%type;
    v_strContainerNo1   con_label.container_no%type;
    v_strContainerType1 con_label.container_type%type;
    v_storeNo           bill_om_divide_dtl.store_no%type;
    v_itemNo            bill_om_divide_dtl.item_no%type;
    v_sizeNo            bill_om_divide_dtl.size_no%type;
    v_divideID          bill_om_divide_dtl.divide_id%type;
    v_strBoxNo          bill_om_divide_dtl.box_no%type;
    v_strExpNo          bill_om_exp.exp_no%type;
    v_flag              number;
  BEGIN
    v_flag              := 0;
    v_strContainerType  := 'C';
    v_strContainerType1 := 'T';
    begin
      select status
        into v_status
        from bill_om_divide
       where locno = I_locno
         and divide_no = I_divideNo;
    exception
      when no_data_found then
        O_msg := 'N|分货单号:' || I_divideNo || '不存在!';
        return;
    end;
    if v_status = '35' then
      O_msg := 'N|分货单号:' || I_divideNo || '已复核完成!';
      return;
    end if;
    
    begin
      select item_no, size_no
        into v_itemNo, v_sizeNo
        from item_barcode
       where barcode = I_barcode
         and rownum = 1
         and package_id ='0';
    exception
      when no_data_found then
        O_msg := 'N|商品条码:' || I_barcode || '在商品资料中不存在!';
        return;
    end;
    
    --是否存在该尺码商品
    begin
      select dtl.owner_no,0,dtl.store_no,dtl.line_no,dtl.exp_no,
             dtl.exp_date,dtl.box_no,dtl.divide_id
        into v_ownerNo,v_flag,v_storeNo,v_lineNo,v_strExpNo,
             v_expDate,v_strBoxNo,v_divideID
        from bill_om_divide_dtl dtl
       where dtl.locno = I_locno
         and dtl.divide_no = I_divideNo
         and dtl.serial_no = I_serialNo
         and dtl.size_no = v_sizeNo
         and dtl.item_no = v_itemNo
         and dtl.status <> '13'
         and rownum = 1;
    exception
      when no_data_found then
        v_flag := 2;
    end;
    if v_flag = 2 then
      begin
        --串码处理
        select dtl.owner_no,1,dtl.store_no,dtl.line_no,dtl.exp_no,
               dtl.exp_date,dtl.box_no,dtl.divide_id
          into v_ownerNo,v_flag,v_storeNo,v_lineNo,v_strExpNo,
               v_expDate,v_strBoxNo,v_divideID
          from bill_om_divide_dtl dtl
         where dtl.locno = I_locno
           and dtl.divide_no = I_divideNo
           and dtl.serial_no = I_serialNo
           and dtl.item_no = v_itemNo
           and dtl.status <> '13'
           and rownum = 1;
      exception
        when no_data_found then
          v_flag := 2;
      end;
    end if;
    if v_flag = 2 then
      O_msg := 'N|分货单:' || I_divideNo || '不存在分货商品:' || I_barcode || '(串款)';
      return;
    end if;
    --查找复核单号
    begin
    select recheck_no, status into v_recheckNo, v_CheckStatus
      from bill_om_recheck
     where locno = I_locno
       and divide_no = I_divideNo
       and store_no = v_storeNo;
    exception when no_data_found then
      --产生复核单号
        PKG_WMS_BASE.proc_getsheetno(I_locno, 'OC', v_recheckNo, O_msg);
        if instr(O_msg, 'N', 1, 1) = 1 then
          O_msg := 'N|获取复核单号失败!';
          return;
        end if;
        v_CheckStatus := '10';
        --按门店写复核头档
        insert into bill_om_recheck
          (locno,recheck_no,line_no,store_no,status,recheck_type,
           creator,createtm,editor,edittm,serial_no,exp_date,divide_no)
        values
          (I_locno,v_recheckNo,v_lineNo,v_storeNo,'10','0',I_creator,sysdate,
          I_creator,sysdate,I_serialNo,v_expDate,I_divideNo);
      return;
    end;
    --复核单已结案拦截
    if v_CheckStatus <> '10' then
      O_msg := 'N|复核单' || v_recheckNo || '已结案!';
      return;
    end if;
    --产生新箱
    if nvl(I_boxNo, 'N') = 'N' then
      --取箱号
      Pkg_Label.proc_get_ContainerNoBase(I_locNo,v_strContainerType1,I_creator,'',1,'1','11',
                                         v_strScanLabelNo, v_strContainerNo1,v_nSessionId,O_msg);
      if instr(O_msg, 'N', 1, 1) = 1 then
        return;
      end if;
      v_strScanLabelNo := v_strScanLabelNo || 'C';
      O_newBoxNo       := v_strScanLabelNo;
      --写箱头档
      insert into con_box
        (locno,owner_no,box_no,creat_date,import_date,status,qty,s_import_no, export_no,store_no)
      values
        (I_locno,v_ownerNo,v_strScanLabelNo,sysdate, sysdate,'2',I_qty,'N',v_strExpNo,v_storeNo);
      --箱明细
      insert into con_box_dtl
        (locno, owner_no, box_no, box_id, item_no, style_no, size_no, qty)
        select 
        I_locno,v_ownerNo,v_strScanLabelNo,1,v_itemNo,nvl(a.style_no, 'N'),v_sizeNo,I_qty
         from item a
         where a.item_no = v_itemNo;
      if sql%rowcount <= 0 then
        O_msg := 'N|新增箱明细失败!';
        return;
      end if;
      --新取容器号
      Pkg_Label.proc_get_ContainerNoBase(I_locNo,v_strContainerType,I_creator,'',1,'1',
                                         '',v_strLabelNo,v_strContainerNo,v_nSessionId,O_msg);
      if instr(O_msg, 'N', 1, 1) = 1 then
        return;
      end if;
      --写标签头档
      insert into con_label
        (locno,source_no,label_no,recheck_no,container_no,container_type,status,
        load_container_no,owner_container_no,owner_cell_no,scan_label_no
        ,store_no,line_no,exp_date,creator,createtm)
      values
        (I_locno,I_divideNo,v_strLabelNo,v_recheckNo,v_strContainerNo,v_strContainerType,'52',
         v_strContainerNo,v_strContainerNo,v_cellNo,v_strScanLabelNo,
         v_storeNo, v_lineNo,sysdate,I_creator,sysdate);
      --写标签明细
      v_rowid := 0;
      select nvl(max(row_id), 0)
        into v_rowid
        from con_label_dtl
       where locno = I_locno
         and container_no = v_strContainerNo;
      insert into con_label_dtl
        (locno,owner_no,source_no,container_no,container_type,item_no,
         item_id,pack_qty,qty,exp_no,store_no,line_no,scan_label_no,
         status,row_id,creator,createtm,exp_date,size_no,divide_id)
        select I_locno,v_ownerNo,v_recheckNo,v_strContainerNo,v_strContainerType,v_itemNo,a.Item_Id,
               a.pack_qty,I_qty,a.exp_no,v_storeNo,v_lineNo,v_strScanLabelNo,'52',v_rowid + rownum,
               I_creator, sysdate,a.Exp_Date,v_sizeNo,a.divide_id
          from bill_om_divide_dtl a
         where a.locno = I_locno
           and a.divide_no = I_divideNo
           and a.owner_no = v_ownerNo
           and divide_id = v_divideID;
       if sql%rowcount <= 0 then
        O_msg := 'N|新增箱标签明细失败!';
        return;
       end if;
     else
      v_strScanLabelNo:=I_boxNo;
      --箱码表信息更新
      update con_box
         set qty = nvl(qty, 0) + I_qty
       where locno = I_locno
         and owner_no = v_ownerNo
         and box_no = I_boxNo;
      if sql%rowcount = 0 then
        O_msg := 'N|箱号:' || I_boxNo || '不存在!';
        return;
      end if;
      update con_box_dtl
         set qty = nvl(qty, 0) + I_qty
       where locno = I_locno
         and box_no = I_boxNo
         and item_no = v_itemNo
         and size_no = v_sizeNo;
      --没有更新到则新增
      if sql%rowcount = 0 then
          v_rowid := 0;
          select nvl(max(box_id), 0)into v_rowid
            from con_box_dtl
           where locno = I_locno and owner_no = v_ownerNo and box_no = I_boxNo;
          insert into con_box_dtl
            (locno,owner_no,box_no,box_id,item_no,style_no,size_no,qty)
            select I_locno,v_ownerNo,I_boxNo,v_rowid + 1,v_itemNo,nvl(a.style_no, 'N'),v_sizeNo,I_qty
              from item a
             where a.item_no = v_itemNo;
      end if;
      --标签信息更新
      begin
        select container_no
          into v_strContainerNo
          from con_label
         where locno = I_locno
           and source_no = I_divideNo
           and recheck_no = v_recheckNo
           and scan_label_no = v_strScanLabelNo;
      exception
        when no_data_found then
          O_msg := 'N|标签:' || v_strScanLabelNo || '不存在!';
          return;
      end;
      update con_label_dtl
         set qty = nvl(qty, 0) + I_qty
       where locno = I_locno
         and source_no = v_recheckNo
         and container_no = v_strContainerNo
         and scan_label_no = v_strScanLabelNo
         and store_no = v_storeNo
         and item_no = v_itemNo
         and size_no = v_sizeNo;
      if sql%rowcount =0 then
        --写标签明细
      v_rowid := 0;
      select nvl(max(row_id), 0)
        into v_rowid
        from con_label_dtl
       where locno = I_locno
         and container_no = v_strContainerNo;
      insert into con_label_dtl
        (locno,owner_no,source_no,container_no,container_type,
         item_no,item_id,pack_qty,qty,exp_no,store_no,line_no,scan_label_no,
         status,row_id,creator,createtm,exp_date,size_no,divide_id)
        select I_locno,v_ownerNo,v_recheckNo,v_strContainerNo,v_strContainerType,
               v_itemNo,a.Item_Id,a.pack_qty,I_qty,a.exp_no,v_storeNo,v_lineNo,v_strScanLabelNo,
               '52',v_rowid + rownum,I_creator, sysdate,a.Exp_Date,v_sizeNo,a.divide_id
          from bill_om_divide_dtl a
        where a.locno = I_locno
           and a.divide_no = I_divideNo
           and a.owner_no = v_ownerNo
           and divide_id = v_divideID;
      end if;
    end if;
    --复核明细更新
    update bill_om_recheck_dtl
       set item_qty = nvl(item_qty, 0) + I_qty,
           real_qty = nvl(real_qty, 0) + I_qty
     where locno = I_locno
       and recheck_no = v_recheckNo
       and item_no = v_itemNo
       and size_no = v_sizeNo
       and box_no = v_strBoxNo
       and container_no = v_strContainerNo;
    if sql%rowcount = 0 then
      v_rowid := 0;
      select nvl(max(row_id), 0)
        into v_rowid
        from bill_om_recheck_dtl
       where locno = I_locno
         and recheck_no = v_recheckNo;
      insert into bill_om_recheck_dtl
        (locno,owner_no,recheck_no,row_id,container_no,item_no,
         size_no,item_id,item_qty,real_qty,status,assign_name,
         recheck_name,recheck_date,exp_no,exp_type,exp_date,pack_qty,box_no)
        select I_locno,dtl.owner_no,v_recheckNo,v_rowid + rownum,v_strContainerNo,
               v_itemNo,v_sizeNo,dtl.item_id,I_qty,I_qty,'13',
               I_creator,I_creator,sysdate,exp_no,exp_type,exp_date,dtl.pack_qty,dtl.box_no
          from bill_om_divide_dtl dtl
        where dtl.locno = I_locno
           and dtl.divide_no = I_divideNo
           and dtl.owner_no = v_ownerNo
           and dtl.divide_id = v_divideID;
    end if;
    --更新分货任务明细复核数量
    update bill_om_divide_dtl
       set real_qty = nvl(real_qty, 0) + I_qty,
           status = case
                      when item_qty = nvl(real_qty, 0) + I_qty then
                       '13'
                      else
                       status
                    end
     where locno = I_locno
       and divide_no = I_divideNo
       and owner_no = v_ownerNo
       and divide_id = v_divideID;
       
   --更新分货单、复核单状态
    PROC_OM_UpdateStatus_ByDivide(I_locno,I_divideNo,v_recheckNo,I_serialNo,I_creator,O_msg);
    if instr(O_msg, 'N', 1, 1) = 1 then
        return;
    end if;
  
    O_msg := 'Y|';
  EXCEPTION
    WHEN OTHERS THEN
      O_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END PROC_OM_DIVIDEDIFF_CONFIRM;
  
  
  
  /*
   功能：城市仓-分货复核单删除
   删除状态为10的分货复核单,回写分货单数量、删除标签表信息
   作者：SU.YQ
   日期：2013-11-30
  */
  PROCEDURE PROC_OM_RECHECK_DEL(I_locno IN  bill_om_recheck.locno%type,
                                I_recheckNo IN bill_om_recheck.recheck_no%type, --复核单号
                                O_msg OUT VARCHAR2 --输出信息
                                )IS
                                
  v_divide_real_qty number;--是否存在已复核数量 
  v_divide_no varchar2(20);--分货单                        
   
  --查询分货复核明细信息                              
  Cursor v_cursor_get_recheckdtl_info is
     select 
     r.locno,
     r.recheck_no,
     r.store_no,
     r.divide_no,
     rd.owner_no,
     rd.item_no,
     rd.size_no,
     rd.box_no,
     rd.pack_qty,
     rd.exp_no,
     rd.exp_type,
     sum(rd.real_qty) as totalRealQty
     from bill_om_recheck r
     inner join bill_om_recheck_dtl rd
         on rd.locno=r.locno
         and rd.recheck_no=r.recheck_no     
     where r.locno = I_locno
         and r.recheck_no=I_recheckNo
         and r.status='10'
     group by 
     r.locno,
     r.recheck_no,
     r.store_no,
     r.divide_no,
     rd.owner_no,
     rd.item_no,
     rd.size_no,
     rd.box_no,
     rd.pack_qty,
     rd.exp_no,
     rd.exp_type;                     
         
                             
  BEGIN 
       
       v_divide_real_qty := 0;
       v_divide_no := 'N';
       O_msg := 'N|[E00025]';--初始化返回信息值
       
       select max(r.divide_no) into v_divide_no
       from bill_om_recheck r
       where r.recheck_no=I_recheckNo
       and r.locno=I_locno;
         
       --循环分货复核明细做回写操作
       if(I_locno is not null and I_recheckNo is not null)
          then 
            for r_dtl in v_cursor_get_recheckdtl_info loop
                update bill_om_divide_dtl dtl
                       set dtl.real_qty = dtl.real_qty-r_dtl.totalrealqty                  
                 where dtl.divide_no=r_dtl.divide_no                    
                    and dtl.locno=r_dtl.locno
                    and dtl.owner_no=r_dtl.owner_no
                    and dtl.store_no=r_dtl.store_no
                    and dtl.item_no=r_dtl.item_no
                    and dtl.size_no=r_dtl.size_no
                    and dtl.exp_no=r_dtl.exp_no
                    and dtl.exp_type=r_dtl.exp_type
                    and dtl.pack_qty=r_dtl.pack_qty
                    and dtl.box_no=r_dtl.box_no;    
                    
                    if sql%rowcount <= 0
                       then 
                         rollback;
                         O_msg := 'N|回写分货明细数量失败!';
                         return;
                    end if;  
                    
                  select dtl.real_qty into v_divide_real_qty
                          from bill_om_divide_dtl dtl
                   where dtl.divide_no=r_dtl.divide_no                    
                    and dtl.locno=r_dtl.locno
                    and dtl.owner_no=r_dtl.owner_no
                    and dtl.store_no=r_dtl.store_no
                    and dtl.item_no=r_dtl.item_no
                    and dtl.size_no=r_dtl.size_no
                    and dtl.exp_no=r_dtl.exp_no
                    and dtl.exp_type=r_dtl.exp_type
                    and dtl.pack_qty=r_dtl.pack_qty
                    and dtl.box_no=r_dtl.box_no;  
                   
                   --如果实际分货数量为0,更改状态为10
                   if v_divide_real_qty = 0
                     then
                       update bill_om_divide_dtl dtl
                       set dtl.status = '10'               
                       where dtl.divide_no=r_dtl.divide_no                    
                          and dtl.locno=r_dtl.locno
                          and dtl.owner_no=r_dtl.owner_no
                          and dtl.store_no=r_dtl.store_no
                          and dtl.item_no=r_dtl.item_no
                          and dtl.size_no=r_dtl.size_no
                          and dtl.exp_no=r_dtl.exp_no
                          and dtl.exp_type=r_dtl.exp_type
                          and dtl.pack_qty=r_dtl.pack_qty
                          and dtl.box_no=r_dtl.box_no;
                   end if;
                             
            end loop;
       else
         O_msg := 'N|参数不合法!';
          rollback;
          return;
       end if;  
       
       --检测分货单是否更改状态
       /*select nvl(sum(dtl.real_qty),0) into v_dtl_num
       from bill_om_divide d
       inner join bill_om_divide_dtl dtl 
             on d.locno=dtl.locno 
             and d.divide_no=dtl.divide_no
       inner join bill_om_recheck r
             on r.locno=dtl.locno
             and r.divide_no=dtl.divide_no   
       where r.recheck_no=I_recheckNo
             and r.locno=I_locno
             and d.status='30';   
       
       if v_dtl_num > 0
         then
          
       end if;                                                                       
     */     
     
     
     --删除箱号表
     for r_box in (select c.locno,cd.owner_no,c.scan_label_no from bill_om_recheck d
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
                and d.divide_no=v_divide_no
                group by c.locno,cd.owner_no,c.scan_label_no)loop 
                
           --删除箱明细表
           delete from con_box_dtl cbd 
           where cbd.locno=r_box.locno
                 and cbd.owner_no=r_box.owner_no
                 and cbd.box_no=r_box.scan_label_no;  
                    
           /*if sql%rowcount <= 0
              then 
                 rollback;
                 O_msg := 'N|删除箱明细表失败!';
                return;
            end if;
          */
          --删除箱表
          delete from con_box c 
           where c.locno=r_box.locno
                 and c.owner_no=r_box.owner_no
                 and c.box_no=r_box.scan_label_no;     
           /*if sql%rowcount <= 0
              then 
                 rollback;
                 O_msg := 'N|删除箱表失败!';
                return;
            end if;*/    
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
           /*if sql%rowcount <= 0
              then 
                 rollback;
                 O_msg := 'N|删除标签明细表失败!';
                return;
            end if; */
            
           --删除标签表
           delete from con_label c
            where c.locno=r_recheck.locno
                  and c.container_no=r_recheck.container_no;              
           /*if sql%rowcount <= 0
              then 
                 rollback;
                 O_msg := 'N|删除标签表失败!';
                return;
            end if;*/     
     end loop;
     
     --删除复核明细表
     delete from bill_om_recheck_dtl dtl
            where dtl.locno=I_locno
            and dtl.recheck_no=I_recheckNo;
     /*if sql%rowcount <= 0
        then 
          rollback;
          O_msg := 'N|删除复核明细表失败!';
          return;
     end if;
     */
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
  END PROC_OM_RECHECK_DEL;
  /*
   功能：城市仓-更新分货单、复核单状态
   作者：HUANG.JG
   日期：2013-12-18
  */
  PROCEDURE PROC_OM_UpdateStatus_ByDivide(I_locno     bm_defloc.locno%type,
                                          I_divideNo  bill_om_divide.divide_no%type, --分货单号
                                          I_recheckNo bill_om_recheck.recheck_no%type, --复核单号
                                          I_serialNo  bill_om_divide_dtl.serial_no%type, --流道编码
                                          I_creator   varchar2,
                                          O_msg       OUT VARCHAR2 --输出信息
                                          ) IS
    v_strStatus varchar2(2);
  BEGIN
    --判断该门店是否已全部复核完成
    update bill_om_recheck chk
       set chk.status = '13'
     where chk.locno = I_locno
       and chk.recheck_no = I_recheckNo
       and chk.divide_no = I_divideNo
       and not exists (select 'X'
              from bill_om_divide_dtl dtl
             where dtl.locno = I_locno
               and dtl.divide_no = I_divideNo
               and dtl.serial_no = I_serialNo
               and dtl.status = '10');
    --当该门店复核完成时
    if sql%rowcount > 0 then
      --写状态日志表
      Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                           I_recheckNo,
                                           'OM',
                                           '13',
                                           '分货复核,更新复核单状态为:复核完成',
                                           I_creator,
                                           O_msg);
      --写验收单、库存
      PKG_IM_CITY_RECEIPT.PROC_IM_CREATECHECKDTL(I_locno,
                                                 I_divideNo, --分货单号
                                                 I_recheckNo, --复核单号
                                                 I_creator,
                                                 O_msg);
      if instr(O_msg, 'N', 1, 1) = 1 then
        return;
      end if;
    end if;
    --更新分货任务单
    select case
             when (exists (select 'X'
                             from bill_om_divide_dtl
                            where locno = I_locno
                              and divide_no = I_divideNo
                              and status = '10')) or
                  (exists (select 'X'
                             from bill_om_recheck br
                            where br.locno = I_locno
                              and br.divide_no = I_divideNo
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
       and divide_no = I_divideNo;
    if sql%rowcount = 0 then
      O_msg := 'N|更新分货任务单状态失败(0行)!';
      return;
    end if;
    --写状态日志表
    Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                         I_divideNo,
                                         'OM',
                                         v_strStatus,
                                         '分货复核,更新分货单状态为:' || case when
                                         v_strStatus = '30' then '部分复核' else
                                         '复核完成' end,
                                         I_creator,
                                         O_msg);
    O_msg := 'Y|';
  EXCEPTION
    WHEN OTHERS THEN
      O_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END PROC_OM_UpdateStatus_ByDivide;

END PKG_OM_CITY_RECHECK;
/
