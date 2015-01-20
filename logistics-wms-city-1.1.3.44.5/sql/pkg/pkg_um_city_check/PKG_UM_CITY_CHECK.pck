create or replace package PKG_UM_CITY_CHECK is

  -- Author  : HUANG.JG
  -- Created : 2013-10-18 16:07:55
  -- Purpose :

  /*
      功能：城市仓-店退仓RF 创建验收单
     作者：HUANG.JG
     日期：2013-10-18
  */
  PROCEDURE PROC_UM_CreateChkDtl(I_locno     bm_defloc.locno%TYPE, --仓别
                                 I_untreadNo bill_um_untread.untread_no%type, --退仓单号
                                 I_boxNo     bill_um_untread_dtl.box_no%type, --箱号
                                 I_barcode   item_barcode.barcode%type, -- 商品条码
                                 I_qty       number, --验收数量
                                 I_creator   bill_um_check.creator%TYPE, --验收人
                                 O_checkNo   OUT bill_um_check.check_no%type, --输出,验收单号
                                 O_itemQty   OUT number, --输出,待验收数量
                                 O_checkQty  OUT number, --输出,已验收数量
                                 O_msg       OUT VARCHAR2 --输出信息
                                 );

  /*
      功能：城市仓-店退仓RF 确认验收单
     作者：HUANG.JG
     日期：2013-10-18
  */
  PROCEDURE PROC_UM_ConfirmChk(I_locno     bm_defloc.locno%TYPE, --仓别
                               I_untreadNo bill_um_untread.untread_no%type, --退仓单号
                               I_creator   bill_um_check.creator%TYPE, --验收人
                               O_msg       OUT VARCHAR2 --输出信息
                               );
  /*
      功能：验证商品条码是否是串码、串款
     作者：HUANG.JG
     日期：2014-01-15
  */
  PROCEDURE proc_um_validate_item(I_locno     bm_defloc.locno%type, --仓别
                                  I_untreadNo bill_um_untread.untread_no%type, --退仓单
                                  I_boxNo     bill_um_untread_dtl.box_no%type, --箱号
                                  I_barcode   item_barcode.barcode%type, --商品条码
                                  O_strItemNo OUT item.item_no%type, --输出,商品编码
                                  O_strSizeNo OUT item_barcode.size_no%type, --输出,尺码
                                  O_flag      OUT varchar2, --0表示正常,1表示串码,2表示串款
                                  O_msg       OUT VARCHAR2 --输出
                                  );
   /*
      功能：城市仓-店退仓RF 删除验收明细
     作者：HUANG.JG
     日期：2014-02-17
  */
  PROCEDURE PROC_UM_DeleteChkDtl(I_locno     bm_defloc.locno%TYPE, --仓别
                                 I_untreadNo bill_um_untread.untread_no%type, --退仓单号
                                 I_boxNo     bill_um_untread_dtl.box_no%type, --箱号
                                 I_barcode   item_barcode.barcode%type, -- 商品条码
                                 I_qty       number, --验收数量
                                 I_creator   bill_um_check.creator%TYPE, --验收人
                                 O_checkNo   OUT bill_um_check.check_no%type, --输出,验收单号
                                 O_itemQty   OUT number, --输出,待验收数量
                                 O_checkQty  OUT number, --输出,已验收数量
                                 O_msg       OUT VARCHAR2 --输出信息
                                 );

  /*****************************************************************************************
   功能：退仓验收单审核
  create By su.yq AT 2013-11-18
  modi by
  pragma autonomous_transaction;
  *****************************************************************************************/
  procedure PROC_UM_CHECK_AUDIT(I_locno     in bill_um_check.locno%type, --仓别
                                I_owner     in bill_um_check.owner_no%type, --委托业主
                                I_check_no  in bill_um_check.check_no%type, --退仓验收单编码
                                I_oper_user varchar2, --操作用户
                                strOutMsg   out varchar2 --处理返回的结果
                                );
  /*
      功能：城市仓-店退仓RF 统计当前箱号计划数量、已验收数量
     作者：HUANG.JG
     日期：2014-03-29
  */
  procedure proc_um_check_calc(I_locno     bm_defloc.locno%TYPE, --仓别
                               I_untreadNo bill_um_untread.untread_no%type, --退仓单号
                               I_boxNo     bill_um_untread_dtl.box_no%type, --箱号
                               O_checkNo   OUT bill_um_check.check_no%type, --输出,验收单号
                               O_itemQty   OUT number, --输出,待验收数量
                               O_checkQty  OUT number, --输出,已验收数量
                               O_msg       OUT VARCHAR2 --输出信息
                               );

end PKG_UM_CITY_CHECK;
/
CREATE OR REPLACE PACKAGE BODY PKG_UM_CITY_CHECK IS
  v_strUntreadStatus bill_um_untread.status%TYPE; --店退仓单号
  v_strCheckStatus   bill_um_check.status%TYPE; --验收单状态
  v_strOwnerNo       bm_defowner.owner_no%TYPE;
  v_strTemp   bill_um_check.check_no%type;
  /*
      功能：城市仓-店退仓RF 创建验收单
     作者：HUANG.JG
     日期：2013-10-18
  */
  PROCEDURE PROC_UM_CreateChkDtl(I_locno     bm_defloc.locno%TYPE, --仓别
                                 I_untreadNo bill_um_untread.untread_no%TYPE, --退仓单号
                                 I_boxNo     bill_um_untread_dtl.box_no%TYPE, --箱号
                                 I_barcode   item_barcode.barcode%TYPE, -- 商品条码
                                 I_qty       NUMBER, --验收数量
                                 I_creator   bill_um_check.creator%TYPE, --验收人
                                 O_checkNo   OUT bill_um_check.check_no%TYPE, --输出,验收单号
                                 O_itemQty   OUT NUMBER, --输出,待验收数量
                                 O_checkQty  OUT NUMBER, --输出,已验收数量
                                 O_msg       OUT VARCHAR2 --输出信息
                                 ) IS
    v_strItemNo item.item_no%TYPE;
    v_strSizeNo item_barcode.size_no%TYPE;
    v_strPriBarcode item_barcode.barcode%type;--主条码
  BEGIN
    --huangjg modify 条码为空时统计该箱号计划验收数量、已验收数量
    if nvl(I_barcode,'N') = 'N' then
      proc_um_check_calc(I_locno,
                         I_untreadNo,
                         I_boxNo,
                         O_checkNo,
                         O_itemQty,
                         O_checkQty,
                         O_msg
                         );
      return;
    end if;
    --取消模式
    if I_qty < 0 then
      proc_um_deleteChkDtl(I_locno,
                           I_untreadNo,
                           I_boxNo,
                           I_barcode,
                           0-I_qty,--传正数,huangjg modify at 2014-02-20
                           I_creator,
                           O_checkNo,
                           O_itemQty,
                           O_checkQty,
                           O_msg);
       return;
    end if;

    --店退仓单状态验证
    BEGIN
      SELECT d.status, d.owner_no
        INTO v_strUntreadStatus, v_strOwnerNo
        FROM bill_um_untread d
       WHERE d.locno = I_locno
         AND d.untread_no = I_untreadNo;
    EXCEPTION
      WHEN no_data_found THEN
        O_msg := 'N|店退仓单:' || I_untreadNo || '不存在!';
        RETURN;
    END;
    IF v_strUntreadStatus in ('10','11') THEN
      O_msg := 'N|店退仓单:' || I_untreadNo || '未收货,不能验收!';
      RETURN;
    ELSIF v_strUntreadStatus in ('30','35') THEN
      O_msg := 'N|店退仓单' || I_untreadNo || '已验收完成,请核实!';
      RETURN;
    END IF;
    --huangjg modify 统一调用取主条码方法
    v_strPriBarcode := FUN_GETPRIBARCODE('N','N',I_barcode);
    if nvl(v_strPriBarcode,'N')= 'N' then
      O_msg:='N|条码:'||I_barcode||'主条码不存在!';
      return;
    end if;

    --huangjg modify 2014-03-08 支持一码多品验收
    BEGIN
      SELECT bar.item_no, bar.size_no
        INTO v_strItemNo, v_strSizeNo
        FROM (select br.item_no, br.size_no
                from item_barcode br
               where br.barcode = v_strPriBarcode
                 and br.package_id ='0'
             order by
                   --店退仓明细同商品排序靠前
                   case when
                     exists(
                     select 1 from bill_um_untread_dtl dtl
                     where dtl.locno =I_locno
                     and dtl.untread_no = I_untreadNo
                     and dtl.item_no = br.item_no
                     and dtl.box_no = I_boxNo)
                     then 1 else 2 end,
                  --验收明细同商品排序靠前
                   case when
                     exists(
                     select 1 from bill_um_check_dtl dtl
                     inner join bill_um_check chk
                     on dtl.locno = I_locno
                     and dtl.check_No = chk.check_no
                     and chk.untread_no = I_untreadNo
                     where dtl.locno = I_locno
                     and chk.untread_no = I_untreadNo
                     and dtl.item_no = br.item_no
                     and dtl.box_no = I_boxNo )
                     then 1 else 2 end,
                    --分摊,先满足还未验收完成的商品
                    case when
                    --收货数量
                    nvl((select sum(dtl.receipt_qty)from bill_um_untread_dtl dtl
                    where dtl.locno = I_locno
                    and dtl.untread_no = I_untreadNo
                    and dtl.item_no = br.item_no
                    and dtl.size_no = br.size_no
                    and dtl.box_no = I_boxNo),0)
                    -
                    --已验收数量
                    nvl((select sum(dtl.check_qty) from
                    bill_um_check_dtl dtl
                    inner join bill_um_check chk
                     on dtl.locno = I_locno
                     and dtl.check_No = chk.check_no
                     and chk.untread_no= I_untreadNo
                    where dtl.locno =I_locno
                    and chk.untread_no = I_untreadNo
                    and dtl.item_no = br.item_no
                    and dtl.size_no = br.size_no
                    and dtl.box_no = I_boxNo),0)
                    >0 then 1 else 2 end,
                    --同品牌库靠前
                    case when
                      exists(select 1 from bill_um_untread_dtl c
                             inner join item i
                             on c.item_no = i.item_no
                             where c.locno = I_locno
                               and c.untread_no = I_untreadNo
                               and br.sys_no = i.sys_no
                               ) then 1 else 2 end,
                     case when
                       exists(select 1 from bill_um_check_dtl dtl
                               inner join bill_um_check chk
                               on dtl.locno = I_locno
                               and dtl.check_No = chk.check_no
                               and chk.untread_no= I_untreadNo
                               inner join item i
                               on dtl.item_no = i.item_no
                               where dtl.locno = I_locno
                               and br.sys_no = i.sys_no
                               and chk.untread_no = I_untreadNo)
                               then 1 else 2 end
               ) bar where rownum = 1;
    EXCEPTION
      WHEN no_data_found THEN
        O_msg := 'N|商品条码:' || I_barcode || '在商品资料中不存在!';
        RETURN;
    END;

    /* --商品串码、串款处理
    proc_um_validate_item(I_locno,
                          I_untreadNo,
                          I_boxNo,
                          I_barcode,
                          v_strItemNo,
                          v_strSizeNo,
                          v_flag,
                          O_msg);
    IF instr(O_msg, 'N', 1, 1) = 1 THEN
      RETURN;
    END IF;*/
    --查找验收单
    BEGIN
      SELECT check_no, status
        INTO O_checkNo, v_strCheckStatus
        FROM bill_um_check chk
       WHERE chk.locno = I_locno
         AND chk.owner_no = v_strOwnerNo
         AND untread_no = I_untreadNo
         AND rownum = 1;
    EXCEPTION
      WHEN no_data_found THEN
        v_strCheckStatus := '10';
        --产生验收单号
        PKG_WMS_BASE.proc_getsheetno(I_locno, 'UC', O_checkNo, O_msg);
        IF instr(O_msg, 'N', 1, 1) = 1 THEN
          O_msg := 'N|获取验收单号失败!';
          RETURN;
        END IF;
        --写验收头档
        INSERT INTO bill_um_check
          (locno,
           owner_no,
           check_no,
           untread_no,
           loadbox_no,
           instock_direct_no,
           status,
           item_type,
           quality,
           creator,
           createtm,
           editor,
           edittm)
          SELECT d.locno,
                 d.owner_No,
                 O_checkNo,
                 I_untreadNo,
                 '',
                 '',
                 '10',
                 d.untread_type,
                 d.quality,
                 I_creator,
                 SYSDATE,
                 I_creator,
                 SYSDATE
            FROM bill_um_untread d
           WHERE d.locno = I_locno
             AND d.owner_no = v_strOwnerNo
             AND d.untread_no = I_untreadNo;
        IF SQL%ROWCOUNT = 0 THEN
          O_msg := 'N|添加验收单头档数据失败(0行)!';
          RETURN;
        END IF;
    END;
    IF v_strCheckStatus = '11' THEN
      O_msg := 'N|店退仓单:' || I_untreadNo || '对应的验收单已审核,不能验收!';
      RETURN;
    END IF;
    --更新验收明细中的验收数量
    UPDATE bill_um_check_dtl
       SET check_qty = nvl(check_qty, 0) + I_qty
     WHERE locno = I_locno
       AND owner_no = v_strOwnerNo
       AND check_no = O_checkNo
       AND box_no = I_boxNo
       AND size_no = v_strSizeNo
       AND item_no = v_strItemNo;
    --如果没有更新到则新增一条验收明细
    IF SQL%ROWCOUNT = 0 THEN
      INSERT INTO bill_um_check_dtl
        (locno,
         owner_no,
         check_no,
         item_no,
         size_no,
         check_qty,
         status,
         box_no,
         row_id,
         item_qty,
         brand_no)--huangjg 2014-04-16记录品牌
      VALUES
        (I_locno,
         v_strOwnerNo,
         O_checkNo,
         v_strItemNo,
         v_strSizeNo,
         I_qty,
         '10',
         I_boxNo,
         (SELECT nvl(MAX(row_id), 0) + 1
            FROM bill_um_check_dtl
           WHERE locno = I_locno
             AND owner_no = v_strOwnerNo
             AND check_no = O_checkNo),
         (select nvl(sum(receipt_qty), 0)
            from bill_um_untread_dtl
           where locno = I_locno
             and owner_no = v_strOwnerNo
             and untread_no = I_untreadNo
             and item_no = v_strItemNo
             and size_no = v_strSizeNo
             and box_no = I_boxNo
             and nvl(receipt_qty, 0) > 0),
             --huangjg modify 2014-04-16记录品牌
             (select i.brand_no from item i where i.item_no=v_strItemNo));
      IF SQL%ROWCOUNT = 0 THEN
        O_msg := 'N|新增验收明细失败,商品条码:' || I_barcode || '在店退仓单明细中不存在!';
        RETURN;
      END IF;
    END IF;
    --回写店退仓单验收数量
    UPDATE bill_um_untread_dtl dtl
       SET dtl.check_qty = nvl(dtl.check_qty, 0) + I_qty,
           dtl.status = CASE
                          WHEN dtl.receipt_qty <=
                               nvl(dtl.check_qty, 0) + I_qty THEN
                           '13'
                          ELSE
                           dtl.status
                        END
     WHERE dtl.locno = I_locno
       AND dtl.owner_no = v_strOwnerNo
       AND dtl.untread_no = I_untreadNo
       AND dtl.box_no = I_boxNo
       AND dtl.item_no = v_strItemNo
       AND dtl.size_no = v_strSizeNo;
    IF sql%rowcount = 0 THEN
      INSERT INTO bill_um_untread_dtl
        (locno,
         owner_no,
         untread_no,
         row_id,
         item_no,
         size_no,
         item_qty,
         receipt_qty,
         check_qty,
         box_no,
         add_flag,
         status,
         brand_no)
      VALUES
        (I_locno,
         v_strOwnerNo,
         I_untreadNo,
         (SELECT nvl(MAX(row_id), 0) + 1
            FROM bill_um_untread_dtl
           WHERE locno = I_locno
             AND owner_no = v_strOwnerNo
             AND untread_no = I_untreadNo),
         v_strItemNo,
         v_strSizeNo,
         0,
         0,
         I_qty,
         I_boxNo,
         '1',
         '13',
         --huangjg modify 2014-04-16记录品牌
         (select i.brand_no from item i where i.item_no=v_strItemNo));
    END IF;
    --没有更新到则新增
    IF SQL%ROWCOUNT = 0 THEN
      O_msg := 'N|更新店退仓单失败(0行)';
      RETURN;
    END IF;
    --合计该店退仓单待验收数量、已验收数量 huangjg modify 2014-03-29 按箱统计
    proc_um_check_calc(I_locno,
                       I_untreadNo,
                       I_boxNo,
                       v_strTemp,
                       O_itemQty,
                       O_checkQty,
                       O_msg
                       );
    if O_itemQty < 0 then
      O_itemQty := 0;
    end if;
    O_msg := 'Y|';
  EXCEPTION
    WHEN OTHERS THEN
      o_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END PROC_UM_CreateChkDtl;

  /*
      功能：城市仓-店退仓RF 确认验收单
     作者：HUANG.JG
     日期：2013-10-18
  */
  PROCEDURE PROC_UM_ConfirmChk(I_locno     bm_defloc.locno%TYPE, --仓别
                               I_untreadNo bill_um_untread.untread_no%TYPE, --退仓单号
                               I_creator   bill_um_check.creator%TYPE, --验收人
                               O_msg       OUT VARCHAR2 --输出信息
                               ) IS
    v_checkNo     bill_um_check.check_no%TYPE;
    v_strCellNo   Cm_Defcell.Cell_No%TYPE;
    v_strQuality  bill_um_check.quality%TYPE;
    v_strItemType bill_um_check.item_type%TYPE;
    v_num         NUMBER := 0;
  BEGIN
    --店退仓单状态验证
    BEGIN
      SELECT d.status, d.owner_no, d.untread_type, d.quality
        INTO v_strUntreadStatus, v_strOwnerNo, v_strItemType, v_strQuality
        FROM bill_um_untread d
       WHERE d.locno = I_locno
         AND d.untread_no = I_untreadNo;
    EXCEPTION
      WHEN no_data_found THEN
        O_msg := 'N|店退仓单:' || I_untreadNo || '不存在!';
        RETURN;
    END;
    IF v_strUntreadStatus in ('10','11') THEN
      O_msg := 'N|店退仓单:' || I_untreadNo || '未收货,不能验收!';
      RETURN;
    ELSIF v_strUntreadStatus in ('30','35') THEN
      O_msg := 'N|店退仓单' || I_untreadNo || '已验收完成,请核实!';
      RETURN;
    END IF;
    --验证验收单
    BEGIN
      SELECT chk.check_no, chk.status
        INTO v_checkNo, v_strCheckStatus
        FROM bill_um_check chk
       INNER JOIN bill_um_check_dtl dtl
          ON chk.locno = dtl.locno
         AND chk.owner_no = dtl.owner_no
         AND chk.check_no = dtl.check_no
       WHERE  chk.locno = I_locno
         AND chk.owner_no = v_strOwnerNo
         AND chk.untread_no = I_untreadNo
         AND rownum = 1;
    EXCEPTION
      WHEN no_data_found THEN
        O_msg := 'N|店退仓单:' || I_untreadNo || '无验收数据!';
        RETURN;
    END;
    IF v_strCheckStatus = '11' THEN
      O_msg := 'N|店退仓单:' || I_untreadNo || '对应的验收单已审核!';
      RETURN;
    END IF;
    --找退货暂存区储位
    pkg_wms_base.proc_GetSpecailCellNo(I_locno,
                                      --log20140228 modi by chenhaitao 退货暂存区
                                       '1',
                                      --end log20140228
                                       v_strQuality,
                                       '1',
                                       '6',
                                       v_strItemtype,
                                       v_strCellNo,
                                       O_msg);
    IF v_strCellNo is null THEN
      O_msg := 'N|没有找到可用的退货区储位';
      RETURN;
    END IF;
    --根据验收单写库存记账
    FOR dr IN (SELECT dtl.check_no,
                      dtl.owner_no,
                      dtl.item_no,
                      dtl.row_id,
                      und.store_no,
                      chk.untread_no,
                      undtl.size_no,
                      undtl.box_no,
                      --log20140208 modi by chenhaitao 加库存商品属性与品质
                      chk.item_type,
                      chk.quality,
                      --log20140208
                      MAX(dtl.check_qty) check_qty,
                      ite.supplier_no
                 FROM bill_um_check_dtl dtl
                INNER JOIN bill_um_check chk
                   ON dtl.locno = chk.locno
                  AND dtl.owner_no = chk.owner_no
                  AND dtl.check_no = chk.check_no
                INNER JOIN bill_um_untread und
                   ON chk.locno = und.locno
                  AND chk.owner_no = und.owner_no
                  AND chk.untread_no = und.untread_no
                INNER JOIN bill_um_untread_dtl undtl
                   ON und.locno = undtl.locno
                  AND und.owner_no = undtl.owner_no
                  AND und.untread_no = undtl.untread_no
                  AND dtl.item_no = undtl.item_no
                  AND dtl.size_no = undtl.size_no
                  AND dtl.box_no = undtl.box_no
                INNER JOIN item ite
                   ON ite.item_no = undtl.item_no
                WHERE dtl.locno = I_locno
                  AND dtl.owner_no = v_strOwnerNo
                  AND dtl.check_no = v_checkNo
                  AND chk.untread_no = I_untreadNo
                  AND nvl(undtl.check_qty, 0) > 0
                GROUP BY dtl.check_no,
                         dtl.owner_no,
                         dtl.item_no,
                         dtl.row_id,
                         und.store_no,
                         chk.untread_no,
                         undtl.size_no,
                         undtl.box_no,
                         --log20140208 modi by chenhaitao 加库存商品属性与品质
                         chk.item_type,
                         chk.quality,
                         --log20140208
                         ite.supplier_no) LOOP
      v_num := 1;
      --updt by crm 20140110 统一库存记账，写储储位预上量
      --开始
      acc_prepare_data_ext(I_untreadNo,
                           'UM',
                           'I',
                           I_creator,
                           dr.row_id,
                           '',
                           I_Locno,
                           v_strCellNo,
                           dr.item_no,
                           dr.size_no,
                           1,
                           --log20140208 modi by chenhaitao 记帐用验收里的属性与品质
                           dr.item_type,
                           dr.quality,
                           --end log20140208
                           v_strOwnerNo,
                           dr.Supplier_No,
                           dr.box_no,
                           dr.check_qty,
                           0,
                           0,
                           '0',
                           '0',
                           '1',
                           '2'); --2-RF
      --回写箱码储位
      UPDATE con_box x
         SET x.cell_no = v_strCellNo
         --log20140318 modi by chenhaitao 退仓验收改箱状态为已入库
         ,x.status='2'
         --end log20140318
       WHERE x.locno = I_Locno
         AND x.owner_no = v_strOwnerNo
         AND x.box_no = dr.box_no;
      acc_apply(I_untreadNo, '2', 'UM', 'I', 1);
      --结束

      --写退货暂存区库存
      /*pkg_content.proc_InstContent_qtyByCellNo(I_Locno, --仓别
      dr.Owner_No, --委托业主
      dr.item_no, --商品编号
      v_nConItemRowid, --商品属性ID
      v_strCellNo, --储位
      'N', --关系储位
      1, --商品包装
      1, --数量
      v_containerNo, --商品容器号
      dr.box_no, --标签号
      '1', --存储类型
      'N', --对应存储值
      I_creator, --操作人员
      I_untreadNo, --操作单号
      '2', --操作设备
      1, --是否可手工移库 0:不允许手工移库；1：可手工移库
      'N', --单据跟踪
      0, --是否增加STOCK_CHANGE_LOG日志 1：增加  0：不加
      n_ROW_ID, --三级帐明细行号
      O_msg); --返回 执行结果*/
      IF (substr(O_msg, 1, 1) <> 'Y') THEN
        RETURN;
      END IF;
      --更新箱状态为返配入库
    END LOOP;
    IF v_num = 0 THEN
      O_msg := 'N|写库存记账失败,未找到验收数据!';
      RETURN;
    END IF;
    --更新验收单为已审核
    UPDATE bill_um_check
       SET status  = '11',
           editor  = I_creator,
           edittm  = SYSDATE,
           auditor = I_creator,
           audittm = SYSDATE
     WHERE locno = I_locno
       AND owner_no = v_strOwnerNo
       AND untread_no = I_untreadNo
       AND check_no = v_checkNo;
    IF SQL%ROWCOUNT = 0 THEN
      O_msg := 'N|更新验收单状态失败(0行)';
      RETURN;
    END IF;
    --写状态日志表
    Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                         v_checkNo,
                                         'UM',
                                         v_strUntreadStatus,
                                         '店退仓验收,更新验收单状态为已审核',
                                         I_creator,
                                         O_msg);
    --更新店退仓单状态
    SELECT CASE
             WHEN EXISTS
              (SELECT 'x'
                     FROM bill_um_untread_dtl dtl
                    WHERE dtl.locno = I_locno
                      AND dtl.owner_no = v_strOwnerNo
                      AND dtl.untread_no = I_untreadNo
                      AND dtl.receipt_qty <> nvl(dtl.check_qty, 0)) THEN
              '30' --差异验收
             ELSE
              '35' --验收完成
           END
      INTO v_strUntreadStatus
      FROM dual;
    UPDATE bill_um_untread
       SET status  = v_strUntreadStatus,
           editor  = I_creator,
           edittm  = SYSDATE,
           auditor = I_creator,
           audittm = SYSDATE
     WHERE locno = I_locno
       AND owner_no = v_strOwnerNo
       AND untread_no = I_untreadNo;
    IF SQL%ROWCOUNT = 0 THEN
      O_msg := 'N|更新店退仓单状态失败(0行)';
      RETURN;
    END IF;
    --写状态日志表
    Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                         I_untreadNo,
                                         'UM',
                                         v_strUntreadStatus,
                                         '店退仓验收,更新店退仓单状态为' || CASE WHEN
                                         v_strUntreadStatus = '30' THEN
                                         '差异验收' ELSE '验收完成' END,
                                         I_creator,
                                         O_msg);
    O_msg := 'Y|';

  EXCEPTION
    WHEN OTHERS THEN
      o_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END PROC_UM_ConfirmChk;

  /*
      功能：验证商品条码是否是串码、串款
     作者：HUANG.JG
     日期：2014-01-15
  */
  PROCEDURE proc_um_validate_item(I_locno     bm_defloc.locno%TYPE, --仓别
                                  I_untreadNo bill_um_untread.untread_no%TYPE, --退仓单
                                  I_boxNo     bill_um_untread_dtl.box_no%TYPE, --箱号
                                  I_barcode   item_barcode.barcode%TYPE, --商品条码
                                  O_strItemNo OUT item.item_no%TYPE, --输出,商品编码
                                  O_strSizeNo OUT item_barcode.size_no%TYPE, --输出,尺码
                                  O_flag      OUT VARCHAR2, --是否串码标识0：正常,1:正常(超量),2:串码,3:串款
                                  O_msg       OUT VARCHAR2 --输出
                                  ) IS
    v_num         NUMBER := 0;
    v_nCheckQty   NUMBER := 0;
    v_nReceiptQty NUMBER := 0;
    v_strPriBarcode item_barcode.barcode%type;--主条码
  BEGIN
    BEGIN
      O_msg  := 'Y|';
      O_flag := '0';
    --huangjg modify 统一调用取主条码方法
    v_strPriBarcode := FUN_GETPRIBARCODE('N','N',I_barcode);
    if nvl(v_strPriBarcode,'N')= 'N' then
      O_msg:='N|条码:'||I_barcode||'主条码不存在!';
      return;
    end if;
      --根据商品条码找商品编码、尺码
      BEGIN
        SELECT item_no, size_no
          INTO O_strItemNo, O_strSizeNo
          FROM item_barcode
         WHERE rownum = 1
           AND barcode = v_strPriBarcode;
      EXCEPTION
        WHEN no_data_found THEN
          O_msg := 'N|商品条码:' || I_barcode || '在商品资料中不存在!';
          RETURN;
      END;
      --log20140411 modi by chenhaitao 判断超量

      --end log20140411
      --按尺码找,没有则按商品找
      SELECT COUNT(1),
             nvl(SUM(nvl(dtl.receipt_qty,0)), 0)
        INTO v_num, v_nReceiptQty
        FROM bill_um_untread_dtl dtl
       WHERE dtl.locno = I_locno
         AND dtl.untread_no = I_untreadNo
         AND dtl.box_no = I_boxNo
         AND dtl.item_no = O_strItemNo
         AND dtl.size_no = O_strSizeNo
         AND nvl(dtl.receipt_qty, 0) > 0;
      IF v_num > 0 THEN
        --huangjg modify 2014-04-14 超量判断
        --已验收数量
        select nvl(sum(dtl.check_qty), 0)
          into v_nCheckQty
          from bill_um_check_dtl dtl
         inner join bill_um_check chk
            on dtl.locno = chk.locno
           and dtl.owner_no = chk.owner_no
           and dtl.check_no = chk.check_no
         where dtl.locno = I_locno
           and chk.untread_no = I_untreadNo
           and dtl.box_no = I_boxNo
           and dtl.item_no = O_strItemNo
           and dtl.size_no = O_strSizeNo;
        IF v_nReceiptQty -v_nCheckQty <= 0 THEN
          O_flag := '1';--超量
          RETURN;
        ELSE
          O_flag := '0';--正常
          RETURN;
        END IF;
      END IF;

      --串码
      IF v_num = 0 THEN
        SELECT COUNT(1)
          INTO v_num
          FROM bill_um_untread_dtl dtl
         WHERE dtl.locno = I_locno
           AND dtl.untread_no = I_untreadNo
           AND dtl.box_no = I_boxNo
           AND dtl.item_no = O_strItemNo
           AND nvl(dtl.receipt_qty, 0) > 0;

      IF v_num = 0 THEN
        O_flag := '3'; --串款
        RETURN;
      ELSE
        O_flag := '2';--串码
      END IF;
     END IF;
      O_msg := 'Y|';
    EXCEPTION
      WHEN OTHERS THEN
        o_msg := 'N|' || SQLERRM ||
                 SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
        RETURN;
    END;
  END proc_um_validate_item;

  /*
      功能：城市仓-店退仓RF 删除验收明细
     作者：HUANG.JG
     日期：2014-02-17
  */
  PROCEDURE PROC_UM_DeleteChkDtl(I_locno     bm_defloc.locno%TYPE, --仓别
                                 I_untreadNo bill_um_untread.untread_no%type, --退仓单号
                                 I_boxNo     bill_um_untread_dtl.box_no%type, --箱号
                                 I_barcode   item_barcode.barcode%type, -- 商品条码
                                 I_qty       number, --验收数量
                                 I_creator   bill_um_check.creator%TYPE, --验收人
                                 O_checkNo   OUT bill_um_check.check_no%type, --输出,验收单号
                                 O_itemQty   OUT number, --输出,待验收数量
                                 O_checkQty  OUT number, --输出,已验收数量
                                 O_msg       OUT VARCHAR2 --输出信息
                                 ) IS
  v_strItemNo item.item_no%TYPE;
  v_strSizeNo item_barcode.size_no%TYPE;
  v_strPriBarcode item_barcode.barcode%type;--主条码
  BEGIN
    begin
    --店退仓单状态验证
    BEGIN
      SELECT d.status, d.owner_no
        INTO v_strUntreadStatus, v_strOwnerNo
        FROM bill_um_untread d
       WHERE d.locno = I_locno
         AND d.untread_no = I_untreadNo;
    EXCEPTION
      WHEN no_data_found THEN
        O_msg := 'N|店退仓单:' || I_untreadNo || '不存在!';
        RETURN;
    END;
    IF v_strUntreadStatus in ('10','11') THEN
      O_msg := 'N|店退仓单:' || I_untreadNo || '未收货,不能验收!';
      RETURN;
    ELSIF v_strUntreadStatus in('30','35') THEN
      O_msg := 'N|店退仓单' || I_untreadNo || '已验收完成,请核实!';
      RETURN;
    END IF;
    --huangjg modify 统一调用取主条码方法
    v_strPriBarcode := FUN_GETPRIBARCODE('N','N',I_barcode);
    if nvl(v_strPriBarcode,'N')= 'N' then
      O_msg:='N|条码:'||I_barcode||'主条码不存在!';
      return;
    end if;
    --根据商品条码找商品编码、尺码
    BEGIN
      SELECT item_no, size_no
        INTO v_strItemNo, v_strSizeNo
        FROM item_barcode br
       WHERE exists(select 1 from bill_um_check chk
         inner join bill_um_check_dtl dtl
         on chk.locno=dtl.locno
         and  chk.owner_no = dtl.owner_no
         and chk.check_no = dtl.check_no
         and chk.untread_no = I_untreadNo
         where chk.locno = I_locno
         and chk.untread_no = I_untreadNo
         and dtl.item_no = br.item_no
         and dtl.size_no = br.size_no)
         AND barcode = v_strPriBarcode
         AND rownum = 1;
    EXCEPTION
      WHEN no_data_found THEN
        O_msg := 'N|商品条码:' || I_barcode || '在商品资料中不存在或未验收!';
        RETURN;
    END;
    --查找验收单
    BEGIN
      SELECT check_no, status
        INTO O_checkNo, v_strCheckStatus
        FROM bill_um_check chk
       WHERE chk.locno = I_locno
         AND chk.owner_no = v_strOwnerNo
         AND untread_no = I_untreadNo
         AND rownum = 1;
    EXCEPTION
      WHEN no_data_found THEN
        O_msg:='N|店退仓单:'||I_untreadNo||'未找到对应的验收单!';
        return;
    END;
    IF v_strCheckStatus = '11' THEN
      O_msg := 'N|店退仓单:' || I_untreadNo || '对应的验收单已审核,不能取消验收!';
      RETURN;
    END IF;
    --扣减验收明细中的验收数量
    UPDATE bill_um_check_dtl dtl
       SET dtl.check_qty = nvl(dtl.check_qty, 0) - I_qty,
           dtl.status = CASE
                          WHEN nvl(dtl.check_qty, 0) - I_qty >=
                               nvl(dtl.item_qty, 0) THEN
                           '13'
                          ELSE
                           '10'
                        END
     WHERE dtl.locno = I_locno
       AND dtl.owner_no = v_strOwnerNo
       AND dtl.check_no = O_checkNo
       AND dtl.box_no = I_boxNo
       AND dtl.size_no = v_strSizeNo
       AND dtl.item_no = v_strItemNo
       AND nvl(dtl.check_qty,0) - I_qty >= 0
       AND rownum = 1;
    IF SQL%ROWCOUNT = 0 THEN
       O_msg := 'N|扣减验收单明细的验收数量失败,商品条码:' || I_barcode || '在验收明细中不存在!';
       RETURN;
    END IF;
    --删除验收数量为0的记录
    DELETE FROM BILL_UM_CHECK_DTL DTL
    WHERE DTL.LOCNO = I_LOCNO
    AND DTL.OWNER_NO = v_strOwnerNo
    AND DTL.CHECK_NO = O_checkNo
    AND DTL.BOX_NO = I_boxNo
    AND DTL.SIZE_NO = v_strSizeNo
    AND DTL.ITEM_NO = v_strItemNo
    AND DTL.CHECK_QTY = 0;
    --扣减店退仓单验收数量
    UPDATE bill_um_untread_dtl dtl
       SET dtl.check_qty = nvl(dtl.check_qty, 0) - I_qty,
           dtl.status = CASE
                          WHEN nvl(dtl.check_qty, 0) - I_qty >=
                               dtl.receipt_qty THEN
                           '13'
                          ELSE
                           '10'
                        END
     WHERE dtl.locno = I_locno
       AND dtl.owner_no = v_strOwnerNo
       AND dtl.untread_no = I_untreadNo
       AND dtl.box_no = I_boxNo
       AND dtl.item_no = v_strItemNo
       AND dtl.size_no = v_strSizeNo
       AND rownum = 1;
    IF sql%rowcount = 0 THEN
       O_msg:='N|扣减店退仓单明细的验收数量失败,商品条码:' || I_barcode || '在店退仓明细中不存在!';
       return;
    END IF;
    --删除计划数量、收货数量、验收数量为0的记录（串码）
    DELETE FROM BILL_UM_UNTREAD_DTL DTL
    WHERE DTL.LOCNO = I_LOCNO
    AND DTL.OWNER_NO = v_strOwnerNo
    AND DTL.UNTREAD_NO = I_untreadNo
    AND DTL.BOX_NO = I_boxNo
    AND DTL.SIZE_NO = v_strSizeNo
    AND DTL.ITEM_NO = v_strItemNo
    AND DTL.CHECK_QTY = 0
    AND DTL.ITEM_QTY = 0
    AND DTL.RECEIPT_QTY = 0
    AND DTL.ADD_FLAG = 1;
    --合计该店退仓单待验收数量、已验收数量 huangjg modify 2014-03-29 按箱统计
    proc_um_check_calc(I_locno,
                       I_untreadNo,
                       I_boxNo,
                       v_strTemp,
                       O_itemQty,
                       O_checkQty,
                       O_msg
                       );
    if O_itemQty < 0 then
      O_itemQty := 0;
    end if;
      O_msg := 'Y|';
    EXCEPTION
      WHEN OTHERS THEN
        o_msg := 'N|' || SQLERRM ||
                 SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
        RETURN;
    end;
  END PROC_UM_DeleteChkDtl;
  /*****************************************************************************************
   功能：退仓验收单审核
  create By su.yq AT 2013-11-18
  modi by
  pragma autonomous_transaction;
  *****************************************************************************************/
  PROCEDURE PROC_UM_CHECK_AUDIT(I_locno     IN bill_um_check.locno%TYPE, --仓别
                                I_owner     IN bill_um_check.owner_no%TYPE, --委托业主
                                I_check_no  IN bill_um_check.check_no%TYPE, --退仓验收单编码
                                I_oper_user VARCHAR2, --操作用户
                                strOutMsg   OUT VARCHAR2 --处理返回的结果
                                ) IS
    v_untread_no      bill_um_untread.untread_no%TYPE;
    v_check_all_count NUMBER;
    v_dif_count     NUMBER;
    v_untread_status  bill_um_untread.status%TYPE;
    v_check_status11  NUMBER := 11;
    v_check_status13  NUMBER := 13;
    v_quality         bill_um_check.quality%TYPE;
    v_item_type       bill_um_check.item_type%TYPE;
    v_cell_no         Cm_defCell.Cell_No%TYPE;
    v_update_msg      VARCHAR2(100 CHAR);
    v_check_Status    bill_um_check.status%TYPE;
    V_MAX_UNTREAD_ID NUMBER := 0;
    V_MAX_BOX_ID NUMBER := 0;
  BEGIN
    strOutMsg := 'Y|[E00025]'; --初始化返回信息值
    BEGIN
      SELECT c.untread_no, c.quality, c.item_type, status
        INTO v_untread_no, v_quality, v_item_type, v_check_Status
        FROM bill_um_check c
       WHERE c.owner_no = I_owner
         AND c.locno = I_locno
         AND c.check_no = I_check_no;
    EXCEPTION
      WHEN no_data_found THEN
        strOutMsg := 'N|验收单:' || I_check_no || '不存在!';
        RETURN;
    END;

    --校验验收单状态
    IF (v_check_Status = v_check_status11) THEN
      strOutMsg := 'N|验收单:' || I_check_no || '已经验收!';
    END IF;

    --更新验收单主档状态
    UPDATE bill_um_check d
       SET d.status  = v_check_status11,
           d.audittm = SYSDATE,
           d.auditor = I_oper_user
     WHERE d.locno = I_locno
       AND d.owner_no = I_owner
       AND d.check_no = I_check_no;
    --写状态日志表
    Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                         I_check_no,
                                         'UC',
                                         v_check_status11,
                                         '店退仓验收,更新验收单状态为已审核',
                                         I_oper_user,
                                         strOutMsg);
    --更新明细状态
    UPDATE bill_um_check_dtl d
       SET d.status = v_check_status13
     WHERE d.locno = I_locno
       AND d.owner_no = I_owner
       AND d.check_no = I_check_no;
    --回写点退仓单验收数量
    FOR dtl IN (SELECT checkdtl.*, ite.supplier_no
                  FROM bill_um_check_dtl checkdtl
                 INNER JOIN item ite
                    ON ite.item_no = checkdtl.item_no
                 WHERE checkdtl.locno = I_locno
                   AND checkdtl.owner_no = I_owner
                   AND checkdtl.check_no = I_check_no
                   AND checkdtl.item_qty > 0) LOOP
      --更新店退仓单明细验收数量
      UPDATE bill_um_untread_dtl d
         SET d.check_qty = dtl.check_qty
       WHERE d.locno = I_locno
         AND d.owner_no = I_owner
         AND d.untread_no = v_untread_no
         AND d.item_no = dtl.item_no
         AND d.size_no = dtl.size_no
         AND d.box_no = dtl.box_no;
      --生成临时储位
      pkg_wms_base.proc_GetSpecailCellNo(I_locno,
      --log20140304 modi by chenhaitao 暂存区use_type是存储区
                                         '1',
      -- end log20140304
                                         v_quality,
                                         '1',
                                         '6',
                                         v_item_type,
                                         v_cell_no,
                                         strOutMsg);
      IF (v_cell_no IS NULL) THEN
        strOutMsg := 'N|未配置相应属性的储位，请检查！';
        RETURN;
      END IF;
      --库存记账
      acc_prepare_data_ext(I_check_no,
                           'UC',
                           'I',
                           I_oper_user,
                           dtl.row_id,
                           '',
                           I_locno,
                           v_cell_no,
                           dtl.Item_No,
                           dtl.Size_No,
                           1,
                           v_item_type,
                           v_quality,
                           dtl.Owner_No,
                           dtl.Supplier_No,
                           dtl.Box_No,
                           dtl.check_qty,
                           0,
                           0,
                           '0',
                           '0',
                           '1');
      --回写箱码储位
      update con_box x
         set x.cell_no = v_cell_no
         --log20140318 modi by chenhaitao 退仓验收改箱状态为已入库
         ,x.status='2'
         --end log20140318
       where x.locno = I_locno
         and x.owner_no = dtl.owner_no
         and x.box_no = dtl.Box_No;
      acc_apply(I_check_no, '2', 'UC', 'I', 1);
    END LOOP;

    ----串款写入点退仓单
    -----串款
    SELECT NVL(MAX(uut.row_id), 0)
      INTO V_MAX_UNTREAD_ID
      FROM bill_um_untread_dtl uut
     WHERE uut.LOCNO = I_locno
       AND uut.OWNER_NO = I_owner
       AND uut.untread_no = v_untread_no;

    FOR dtl IN (SELECT checkdtl.*, ite.supplier_no
                  FROM bill_um_check_dtl checkdtl
                 INNER JOIN item ite
                    ON ite.item_no = checkdtl.item_no
                 WHERE checkdtl.locno = I_locno
                   AND checkdtl.owner_no = I_owner
                   AND checkdtl.check_no = I_check_no
                   AND checkdtl.item_qty = 0) LOOP

             V_MAX_UNTREAD_ID := V_MAX_UNTREAD_ID + 1;

             --插入点退仓单
             insert into bill_um_untread_dtl
               (locno,
                owner_no,
                untread_no,
                row_id,
                item_no,
                size_no,
                item_qty,
                receipt_qty,
                check_qty,
                box_no,
                add_flag,
                status,
                brand_no)--huangjg modify 2014-04-16记录品牌
             values
               (dtl.Locno,
                dtl.Owner_No,
                v_untread_no,
                V_MAX_UNTREAD_ID,
                dtl.item_no,
                dtl.size_no,
                dtl.item_qty,
                '0',
                dtl.check_qty,
                dtl.box_no,
                '1',
                v_check_status13,
                dtl.brand_no);--huangjg modify 2014-04-16记录品牌


              --插入箱明细
              --插入箱明细表
              SELECT NVL(MAX(bd.BOX_ID), 0) + 1
                INTO V_MAX_BOX_ID
                FROM CON_BOX_DTL bd
               WHERE bd.LOCNO = dtl.LOCNO
                 AND bd.OWNER_NO = dtl.OWNER_NO
                 AND bd.BOX_NO = dtl.BOX_NO;
              insert into con_box_dtl
                (locno,
                 owner_no,
                 box_no,
                 box_id,
                 item_no,
                 style_no,
                 size_no,
                 qty,
                 unit_cost,
                 m3_pack_no,
                 m3_developnum,
                 length,
                 wide,
                 height,
                 import_no,
                 add_flag)
              values
                (dtl.LOCNO,
                 dtl.OWNER_NO,
                 dtl.BOX_NO,
                 V_MAX_BOX_ID,
                 dtl.ITEM_NO,
                 'N',
                 dtl.SIZE_NO,
                 dtl.CHECK_QTY,
                 '',
                 '',
                 '',
                 '',
                 '',
                 '',
                 '',
                 '1');

    END LOOP;

    --更新店退仓状态
    --查询验收明细总数

    SELECT COUNT(1)
      INTO v_dif_count
      FROM bill_um_check_dtl d
     WHERE d.locno = I_locno
       AND d.owner_no = I_owner
       AND d.check_no = I_CHEck_no
       AND d.check_qty <> d.item_qty;
    IF (v_dif_count > 0) THEN
      v_untread_status := 30;
      v_update_msg     := '更新店退仓状态为30差异验';
    ELSE
      v_untread_status := 35;
      v_update_msg     := '更新店退仓状态为35验收完成';
    END IF;
    --更新店退仓单状态
    UPDATE bill_um_untread d
       SET d.status = v_untread_status
     WHERE d.locno = I_locno
       AND d.owner_no = I_owner
       AND d.untread_no = v_untread_no;
    --写状态日志表
    Pkg_Common_City.PROC_BILL_STATUS_LOG(I_locno,
                                         v_untread_no,
                                         'UM',
                                         v_check_status11,
                                         v_update_msg,
                                         I_oper_user,
                                         strOutMsg);
  END PROC_UM_CHECK_AUDIT;
    /*
      功能：城市仓-店退仓RF 统计当前箱号计划数量、已验收数量
     作者：HUANG.JG
     日期：2014-03-29
  */
  procedure proc_um_check_calc(I_locno     bm_defloc.locno%TYPE, --仓别
                               I_untreadNo bill_um_untread.untread_no%type, --退仓单号
                               I_boxNo     bill_um_untread_dtl.box_no%type, --箱号
                               O_checkNo   OUT bill_um_check.check_no%type, --输出,验收单号
                               O_itemQty   OUT number, --输出,待验收数量
                               O_checkQty  OUT number, --输出,已验收数量
                               O_msg       OUT VARCHAR2 --输出信息
                               ) IS
  BEGIN
    --查找验收单
    BEGIN
      SELECT check_no
        INTO O_checkNo
        FROM bill_um_check chk
       WHERE chk.locno = I_locno
         AND untread_no = I_untreadNo
         AND rownum = 1;
    EXCEPTION
      WHEN no_data_found THEN
        O_checkNo := '';
    END;
    --统计箱号计划数量
    select sum(dtl.item_qty)
      into O_itemQty
      from bill_um_untread_dtl dtl
     where dtl.locno = I_locno
       and dtl.untread_no = I_untreadNo
       and dtl.box_no = I_boxNo;
    if nvl(O_itemQty, 0) <= 0 then
      O_itemQty := 0;
    end if;
    --统计已验收数量
    select sum(dtl.check_qty)
      into O_checkQty
      from bill_um_check_dtl dtl
     inner join bill_um_check chk
        on dtl.locno = chk.locno
       and dtl.Owner_No = chk.owner_no
       and dtl.check_no = chk.check_no
     where chk.locno = I_locno
       and chk.untread_no = I_untreadNo
       and dtl.box_no = I_boxNo;
    if nvl(O_checkQty, 0) <= 0 then
      O_checkQty := 0;
    end if;
    O_msg := 'Y|';
  EXCEPTION
    WHEN OTHERS THEN
      o_msg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
  END proc_um_check_calc;
END PKG_UM_CITY_CHECK;
/
