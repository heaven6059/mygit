CREATE OR REPLACE PACKAGE PKG_IM_INSTOCK_AUDIT_CITY IS

  --上架回单审核
  -- Author  : LUO.HL
  -- Created : 2013-11-27 20:08:52
  -- Purpose : 
  PROCEDURE PROC_INSTOCK_AUDIT(i_locno      IN bill_im_instock.locno%TYPE, --仓别
                               i_owner      IN bill_im_instock.owner_no%TYPE, --委托业主
                               i_instock_no IN bill_im_instock.instock_no%TYPE, --上架单号
                               i_oper       IN bill_im_instock.creator%TYPE,--操作人
                               stroutmsg    OUT VARCHAR2 --处理返回的结果
                               );

  -- 回单
  -- Author  : LUO.HL
  -- Created : 2013-11-27 20:08:52
  -- Purpose : 
  PROCEDURE PROC_INSTOCK_RECEIPT(i_locno        IN bill_im_instock.locno%TYPE, --仓别
                                 i_owner        IN bill_im_instock.owner_no%TYPE, --委托业主
                                 i_instock_no   IN bill_im_instock.instock_no%TYPE, --上架单号
                                 i_oper       IN bill_im_instock.creator%TYPE, --操作人
                                 i_cell_no      IN bill_im_instock_dtl.cell_no%TYPE, --来源储位
                                 i_cell_id      IN bill_im_instock_dtl.cell_id%TYPE, --来源储位ID
                                 i_dest_cell_no IN bill_im_instock_dtl.dest_cell_no%TYPE, --预上储位
                                 i_dest_cell_id IN bill_im_instock_dtl.dest_cell_id%TYPE, --预上储位ID
                                 i_real_qty     IN bill_im_instock_dtl.real_qty%TYPE, --实际上架数量 
                                 i_item_type    IN bill_im_instock_dtl.item_type%TYPE,--商品属性
                                 i_quality      IN bill_im_instock_dtl.quality%TYPE,  --品质
                                 o_stroutmsg      OUT VARCHAR2);
  --移库
  -- Author  : LUO.HL
  -- Created : 2013-11-27 20:08:52
  -- Purpose :                              
  PROCEDURE PROC_INSTOCK_CON_CONTENT(i_locno        IN bill_im_instock.locno%TYPE, --仓别
                                     i_owner        IN bill_im_instock.owner_no%TYPE, --委托业主  
                                     i_instock_id   IN bill_im_instock_dtl.instock_id%TYPE, --明细id
                                     i_instock_no   IN bill_im_instock.instock_no%TYPE, --上架单号
                                     i_oper        IN bill_im_instock.creator%TYPE, --操作人
                                     i_dest_cell_no IN bill_im_instock_dtl.dest_cell_no%TYPE, --预上储位
                                     i_dest_cell_id IN bill_im_instock_dtl.dest_cell_id%TYPE, --预上储位ID
                                     i_real_cell_no IN bill_im_instock_dtl.real_cell_no%TYPE, --实际上架储位
                                     v_real_cell_id in con_content.cell_id%TYPE, -- 实际的储位ID
                                     v_item_no      in bill_im_instock_dtl.item_no%type,-- 商品编码
                                     v_size_no      in bill_im_instock_dtl.size_no%type,-- 尺码
                                     i_real_qty     IN bill_im_instock_dtl.real_qty%TYPE, --实际上架数量
                                     i_type         IN VARCHAR2, --类型A 新增库存记录  U 更新库存记录
                                     i_item_type    IN bill_im_instock_dtl.item_type%TYPE,--商品属性
                                     i_quality      IN bill_im_instock_dtl.quality%TYPE,  --品质
                                     o_stroutmsg    OUT VARCHAR2) ;

END PKG_IM_INSTOCK_AUDIT_CITY;
/
CREATE OR REPLACE PACKAGE BODY PKG_IM_INSTOCK_AUDIT_CITY IS

  --上架回单审核
  -- Author  : LUO.HL
  -- Created : 2013-11-27 20:08:52
  -- Purpose : 
  PROCEDURE PROC_INSTOCK_AUDIT(i_locno      IN bill_im_instock.locno%TYPE, --仓别
                               i_owner      IN bill_im_instock.owner_no%TYPE, --委托业主
                               i_instock_no IN bill_im_instock.instock_no%TYPE, --上架单号
                               i_oper       IN bill_im_instock.creator%TYPE, --操作人
                               stroutmsg    OUT VARCHAR2 --处理返回的结果
                               ) IS
  
    v_con_count            NUMBER; --库存数量
    v_con_same_style_count NUMBER; --同款的数量
    v_mix_flag             NUMBER(1); --储位是否可混
    v_receipt_no           bill_im_instock_dtl.source_no%TYPE;
    v_po_count             NUMBER(10); -- 数量汇总
    --STR_LIMIT_QTY   number(10);-- 储位可以存储的最大数量
    --v_cell_sum_cout  number(10);-- 储位存放的最大数量
    v_real_cell_no    bill_im_instock_dtl.real_cell_no%TYPE; -- 实际储位编码
    v_real_qty        bill_im_instock_dtl.real_qty%TYPE; -- 实际数量
    v_real_cell_id    bill_im_instock_dtl.cell_id%TYPE; -- 实际储位ID
    v_same_item_count NUMBER(10); -- 同款的数量（只带item_id）
    v_locate_type     bill_im_instock.locate_type%TYPE; --定位类型
    v_is_z_cell       NUMBER(10); -- 是否有合法正确的储位；
  
  BEGIN
  
    v_con_count := 0;
    v_mix_flag  := 0;
    stroutmsg   := 'Y';
  
    --查询所有上架单明细
    FOR dtl IN (SELECT *
                  FROM bill_im_instock_dtl d
                 WHERE d.locno = i_locno
                   AND d.owner_no = i_owner
                   AND d.instock_no = i_instock_no) LOOP
    
      v_real_cell_no := dtl.real_cell_no;
      v_real_qty     := dtl.real_qty;
    
      --如果实际储位和实际数量为空，那么则自动更新实际储位为预上储位
     /*  IF v_real_cell_no IS NULL OR v_real_cell_no = 'N' OR v_real_qty IS NULL OR v_real_qty = 0 THEN
      
       UPDATE bill_im_instock_dtl d
           SET d.real_cell_no = d.dest_cell_no, d.real_qty = d.item_qty
         WHERE INSTOCK_NO = dtl.instock_no
           AND d.LOCNO = dtl.locno
           AND d.OWNER_NO = dtl.owner_no
           AND d.INSTOCK_ID = dtl.instock_id;
      
        --实际储位的值；
        v_real_cell_no := dtl.dest_cell_no;
        v_real_qty     := dtl.item_qty;
      
      END IF;*/
    
     IF v_real_cell_no IS NULL OR v_real_cell_no = 'N' THEN
        stroutmsg := 'N|明细中有实际储位为空的数据，不能审核！';
        RETURN;
      END IF;
    
      IF v_real_qty IS NULL OR v_real_qty <= 0 THEN
        stroutmsg := 'N|实际储位:' || v_real_cell_no || '的实际上架数量非法，不能审核！';
        RETURN;
      END IF;
      
      v_real_qty    := v_real_qty-dtl.instocked_qty;
      
      -- 先调用回单的存储过程
      PKG_IM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_RECEIPT(i_locno,
                                                     i_owner,
                                                     i_instock_no,
                                                     i_oper,
                                                     dtl.cell_no,
                                                     dtl.cell_id,
                                                     dtl.dest_cell_no,
                                                     dtl.dest_cell_id,
                                                     v_real_qty,
                                                     dtl.item_type,
                                                     dtl.quality,
                                                     stroutmsg);
    
      -- 如果返回N,则返回并给出提示
      IF instr(stroutmsg, 'N', 1, 1) = 1 THEN
        RETURN;
      END IF;
    
      --如果实际上架储位跟预上储位不一致
      IF (v_real_cell_no <> dtl.dest_cell_no) THEN
      
        -- 校验储位是否合法
        SELECT COUNT(*)
          INTO v_is_z_cell
          FROM CM_DEFCELL d
         WHERE d.cell_no = v_real_cell_no
           AND d.locno = i_locno
           AND d.cell_status = '0'
           AND d.check_status = '0';
      
        IF v_is_z_cell < 1 THEN
          stroutmsg := 'N|要上的实际储位:' || v_real_cell_no || '非法，请选择其他储位！';
          RETURN;
        END IF;
      
        --判断实际储位是否有库存
        SELECT COUNT(*)
          INTO v_con_count
          FROM con_content c
         WHERE c.locno = dtl.locno
           AND c.owner_no = i_owner
           AND c.cell_no = v_real_cell_no
           AND c.status = '0'
           AND c.flag = '0';
      
        --有库存
        IF (v_con_count > 0) THEN
        
          --判断是否同款
          SELECT COUNT(*)
            INTO v_con_same_style_count
            FROM con_content c
           WHERE c.locno = dtl.locno
             AND c.owner_no = i_owner
             AND c.cell_no = v_real_cell_no
             AND c.item_no = dtl.item_no
             AND c.status = '0'
             AND c.flag = '0';
        
          -- 有同款  
          IF (v_con_same_style_count > 0) THEN
            BEGIN
              -- 带item_id，和箱号
              SELECT c.cell_id
                INTO v_real_cell_id
                FROM con_content c
               WHERE c.locno = dtl.locno
                 AND c.owner_no = i_owner
                 AND c.cell_no = v_real_cell_no
                 AND c.item_no = dtl.item_no
                 AND EXISTS (SELECT 'x'
                        FROM item_barcode bar
                       WHERE bar.item_no = dtl.item_no
                         AND bar.size_no = dtl.size_no
                         AND bar.pack_qty = 1
                         AND bar.package_id = 0
                         AND c.barcode = bar.barcode)
                 AND EXISTS (SELECT 'x'
                        FROM con_box x, con_box_dtl d
                       WHERE x.locno = d.locno
                         AND x.owner_no = d.owner_no
                         AND x.box_no = d.box_no
                         AND d.item_no = c.item_no)
                 AND EXISTS (SELECT 'x'
                        FROM item ite
                       WHERE ite.item_no = c.item_no
                         AND ite.supplier_no = c.supplier_no)
                 AND c.qty = 0
                 AND c.item_type = 0
                    --结束
                 AND c.status = '0'
                 AND c.flag = '0';
            EXCEPTION
              WHEN no_data_found THEN
                -- 带item_id
                SELECT c.cell_id
                  INTO v_real_cell_id
                  FROM con_content c
                 WHERE c.locno = dtl.locno
                   AND c.owner_no = i_owner
                   AND c.cell_no = v_real_cell_no
                   AND c.item_no = dtl.item_no
                      /*AND c.item_id = dtl.item_id*/
                      --updt by crm 20140111 统一库存记账，写储储位预上量
                      --开始
                   AND EXISTS (SELECT 'x'
                          FROM item_barcode bar
                         WHERE bar.item_no = dtl.item_no
                           AND bar.size_no = dtl.size_no
                           AND bar.pack_qty = 1
                           AND bar.package_id = 0
                           AND c.barcode = bar.barcode)
                   AND NOT EXISTS (SELECT 'x'
                          FROM con_box x
                         WHERE x.locno = c.locno
                           AND x.owner_no = c.owner_no
                           AND x.cell_no = c.cell_no)
                   AND EXISTS (SELECT 'x'
                          FROM item ite
                         WHERE ite.item_no = c.item_no
                           AND ite.supplier_no = c.supplier_no)
                      --结束
                   AND c.status = '0'
                   AND c.flag = '0'
                   AND rownum = 1;
            END;
          
            --调用移库存储过程
            PKG_IM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_CON_CONTENT(i_locno,
                                                               i_owner,
                                                               dtl.instock_id,
                                                               i_instock_no,
                                                               i_oper,    
                                                               dtl.dest_cell_no,
                                                               dtl.dest_cell_id,
                                                               v_real_cell_no,
                                                               v_real_cell_id,
                                                               dtl.item_no,
                                                               dtl.size_no,
                                                               v_real_qty,
                                                               'U',
                                                               dtl.item_type,
                                                               dtl.quality,
                                                               stroutmsg);
          
            -- 如果返回N,则返回并给出提示
            IF instr(stroutmsg, 'N', 1, 1) = 1 THEN
              RETURN;
            END IF;
          
          ELSE
            --不同款
          
            --判断是否可混
            SELECT c.mix_flag
              INTO v_mix_flag
              FROM cm_defcell c
             WHERE c.locno = i_locno
               AND c.cell_no = v_real_cell_no;
          
            -- 如果可混
            IF (v_mix_flag <> 0) THEN
            
              --新增库存新生成储位ID
              SELECT SEQ_CON_CONTENT.NEXTVAL INTO v_real_cell_id FROM DUAL;
            
              --调用移库存储过程，直接插入库存记录
              PKG_IM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_CON_CONTENT(i_locno,
                                                                 i_owner,
                                                                 dtl.instock_id,
                                                                 i_instock_no,
                                                                 i_oper,
                                                                 dtl.dest_cell_no,
                                                                 dtl.dest_cell_id,
                                                                 v_real_cell_no,
                                                                 v_real_cell_id,
                                                                 dtl.item_no,
                                                                 dtl.size_no,
                                                                 v_real_qty,
                                                                 'A',
                                                                 dtl.item_type,
                                                                 dtl.quality,
                                                                 stroutmsg);
            
              -- 如果返回N,则返回并给出提示
              IF instr(stroutmsg, 'N', 1, 1) = 1 THEN
                RETURN;
              END IF;
            
            ELSE
              --不可混，数据回滚，直接返回
              stroutmsg := 'N|储位' || v_real_cell_no || '不可混，请选择其他储位！';
              RETURN;
            END IF;
          
          END IF;
        
        ELSE
          -- 如果没有库存，则直接移库新增库存记录
        
          --新增库存新生成储位ID
          SELECT SEQ_CON_CONTENT.NEXTVAL INTO v_real_cell_id FROM DUAL;
        
          --调用移库存储过程
          PKG_IM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_CON_CONTENT(i_locno,
                                                             i_owner,
                                                             dtl.instock_id,
                                                             i_instock_no,
                                                             i_oper,
                                                             dtl.dest_cell_no,
                                                             dtl.dest_cell_id,
                                                             v_real_cell_no,
                                                             v_real_cell_id,
                                                             dtl.item_no,
                                                             dtl.size_no,
                                                             v_real_qty,
                                                             'A',
                                                             dtl.item_type,
                                                             dtl.quality,
                                                             stroutmsg);
        
          -- 如果返回N,则返回并给出提示
          IF instr(stroutmsg, 'N', 1, 1) = 1 THEN
            RETURN;
          END IF;
        
        END IF;
      END IF;
    
    END LOOP;
  
    --整单上架   更新上架单主档状态
    UPDATE bill_im_instock b
       SET b.status = '13', b.instock_date = SYSDATE
    --b.auditor =,
    --b.audittm = sysdate
     WHERE b.locno = i_locno
       AND b.owner_no = i_owner
       AND b.instock_no = i_instock_no;
  
    -- 查找定位类型
    SELECT bi.locate_type
      INTO v_locate_type
      FROM bill_im_instock bi
     WHERE bi.instock_no = i_instock_no
       AND bi.locno = i_locno
       AND bi.owner_no = i_owner;
  
    -- 如果定位类型收货定位（4）,则表示是根据收货直接做的定位，则需要更新收货单和预到货通知单的验收数量和状态               
    IF v_locate_type = '4' THEN
      --查询到指定的收货单号
      SELECT DISTINCT d.source_no
        INTO v_receipt_no
        FROM bill_im_instock_dtl d
       WHERE d.instock_no = i_instock_no;
    
      --更新收货单的验收数量
      UPDATE bill_im_receipt_dtl rd
         SET rd.check_qty = rd.receipt_qty
       WHERE rd.receipt_no = v_receipt_no;
    
      IF SQL%ROWCOUNT = 0 THEN
        stroutmsg := 'N|更新收货单号' || v_receipt_no || '的验收数量时异常！';
        RETURN;
      END IF;
    
      -- 更新收货单的状态为验收完成
      -- update bill_im_receipt r
      -- set r.status = '25'
      -- where r.receipt_no = v_receipt_no;
    
      -- 循环遍历收货单 
      FOR repit_item IN (SELECT sd.import_no,
                                sd.box_no,
                                sd.item_no,
                                sd.size_no,
                                sd.pack_qty,
                                sd.check_qty
                           FROM bill_im_receipt_dtl sd
                          WHERE sd.receipt_no = v_receipt_no) LOOP
      
        -- 更新通知单的验收数量 
        UPDATE BILL_IM_IMPORT_DTL B
           SET B.IMPORT_QTY = nvl(B.IMPORT_QTY, 0) + repit_item.check_qty
         WHERE B.BOX_NO = repit_item.BOX_NO
           AND B.ITEM_NO = repit_item.ITEM_NO
           AND B.SIZE_NO = repit_item.SIZE_NO
           AND B.IMPORT_NO = repit_item.import_no
           AND b.pack_qty = repit_item.pack_qty;
      
        IF SQL%ROWCOUNT = 0 THEN
          stroutmsg := 'N|更新预通知单' || repit_item.import_no || '的验收数量时异常！';
          RETURN;
        END IF;
      
      END LOOP;
    
      DECLARE
        --类型定义
        CURSOR c_job IS
          SELECT sd.import_no
            FROM bill_im_receipt_dtl sd
           WHERE sd.receipt_no = v_receipt_no;
        --定义一个游标变量v_cinfo c_emp%ROWTYPE ，该类型为游标c_emp中的一行数据类型
        c_row c_job%ROWTYPE;
      BEGIN
        FOR c_row IN c_job LOOP
          -- 判断预到货通知单的计划数量和验收数量
          SELECT COUNT(*)
            INTO v_po_count
            FROM bill_im_import_dtl b
           WHERE b.import_no = c_row.import_no
             AND b.import_qty <> b.po_qty;
          IF v_po_count = 0 THEN
            UPDATE bill_im_import im
               SET im.status = '90'
             WHERE im.status <> '90'
               AND im.import_no = c_row.import_no;
          ELSE
            UPDATE bill_im_import im
               SET im.status = '30'
             WHERE im.status <> '30'
               AND im.import_no = c_row.import_no;
          END IF;
        
          IF SQL%ROWCOUNT = 0 THEN
            stroutmsg := 'N|更新预通知单' || c_row.import_no || '的状态时异常！';
            RETURN;
          END IF;
        
        END LOOP;
      END;
    
    END IF;
  
    --更新审核人，审核时间  
    -- Author  : LUO.HL
    -- Created : 2014-02-10 
    -- Purpose : 
    UPDATE BILL_IM_INSTOCK b
       SET b.auditor = i_oper, b.audittm = SYSDATE
     WHERE b.locno = i_locno
       AND b.owner_no = i_owner
       AND b.instock_no = i_instock_no;
  
    stroutmsg := 'Y|';
  
  EXCEPTION
    WHEN OTHERS THEN
      stroutmsg := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
    
  END PROC_INSTOCK_AUDIT;

  --上架回单
  -- Author  : LUO.HL
  -- Created : 2013-11-27 20:08:52
  -- Purpose : 
  PROCEDURE PROC_INSTOCK_RECEIPT(i_locno        IN bill_im_instock.locno%TYPE, --仓别
                                 i_owner        IN bill_im_instock.owner_no%TYPE, --委托业主
                                 i_instock_no   IN bill_im_instock.instock_no%TYPE, --上架单号
                                 i_oper       IN bill_im_instock.creator%TYPE, --操作人
                                 i_cell_no      IN bill_im_instock_dtl.cell_no%TYPE, --来源储位
                                 i_cell_id      IN bill_im_instock_dtl.cell_id%TYPE, --来源储位ID
                                 i_dest_cell_no IN bill_im_instock_dtl.dest_cell_no%TYPE, --预上储位
                                 i_dest_cell_id IN bill_im_instock_dtl.dest_cell_id%TYPE, --预上储位ID 
                                 i_real_qty     IN bill_im_instock_dtl.real_qty%TYPE, --实际上架数量 
                                 i_item_type    IN bill_im_instock_dtl.item_type%TYPE,--商品属性
                                 i_quality      IN bill_im_instock_dtl.quality%TYPE,  --品质
                                 o_stroutmsg    OUT VARCHAR2 --处理返回的结果
                                 ) IS
  v_strBox_No      con_box.box_no%TYPE;
    v_strSupplier_No supplier.supplier_no%TYPE;
  BEGIN
    --log20140322 modi by chenhaitao 不管是否修改储位，均查询出箱号与供应商
    begin
    SELECT d.label_no, ite.supplier_no
        INTO v_strBox_No, v_strSupplier_No
        FROM bill_im_instock_dtl d, item ite
       WHERE d.locno = i_locno
         AND d.owner_no = i_owner
         AND d.instock_no = i_instock_no
         and d.cell_no=i_cell_no
         and d.cell_id=i_cell_id
         AND d.dest_cell_no = i_dest_cell_no
         AND d.dest_cell_id = i_dest_cell_id
         and d.item_type=i_item_type
         and d.quality=i_quality
         AND d.item_no = ite.item_no
         and rownum=1;
     exception when no_data_found then
      o_stroutmsg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
    end;
    --end log2014032
    -- 如果来源储位不为空，则减去来源储位的预下数量和库存数量
    IF i_cell_no IS NOT NULL AND i_cell_no <> 'N' THEN
    
      --updt by crm 20140111 统一库存记账，写储储位预上量
      --开始
      ACC_PREPARE_DATA_EXT(i_instock_no,
                           'IP',
                           'O',
                           i_oper,
                           1,
                           I_LOCNO        => i_locno,
                           I_OWNER_NO     => i_owner,
                           I_CELL_ID      => i_cell_id,
                           I_CELL_NO      => i_cell_no,
                           I_QTY          => i_real_qty,
                           I_OUTSTOCK_QTY => -i_real_qty,
                           I_ITEM_TYPE    => i_item_type,
                           I_QUALITY      => i_quality
                           );
    
      acc_apply(i_instock_no, '2', 'IP', 'O', 1);
      --结束    
    END IF;
  
    -- 更新预上储位的预上数量和实际库存数；
    --updt by crm 20140111 统一库存记账，写储储位预上量
    --开始
    ACC_PREPARE_DATA_EXT(i_instock_no,
                         'IP',
                         'I',
                         i_oper,
                         1,
                         I_LOCNO       => i_locno,
                         I_OWNER_NO    => i_owner,
                         I_CELL_ID     => i_dest_cell_id,
                         I_CELL_NO     => i_dest_cell_no,
                         I_QTY         => i_real_qty,
                         I_INSTOCK_QTY => -i_real_qty,
                         I_ITEM_TYPE    => i_item_type,
                         I_QUALITY      => i_quality);
    acc_apply(i_instock_no, '2', 'IP', 'I', 1);
    --结束    
    --log20140322 modi by chenhaitao 上架完后回写箱码储位
      --回写箱码储位
      UPDATE con_box x
         SET x.cell_no = i_dest_cell_no
       WHERE x.locno = i_locno
         AND x.owner_no = i_owner
         AND x.box_no = v_strBox_No;
    --end log20140322
  END PROC_INSTOCK_RECEIPT;

  --移库存
  -- Author  : LUO.HL
  -- Created : 2013-11-27 20:08:52
  -- Purpose : 
  PROCEDURE PROC_INSTOCK_CON_CONTENT(i_locno        IN bill_im_instock.locno%TYPE, --仓别
                                     i_owner        IN bill_im_instock.owner_no%TYPE, --委托业主  
                                     i_instock_id   IN bill_im_instock_dtl.instock_id%TYPE, --明细id
                                     i_instock_no   IN bill_im_instock.instock_no%TYPE, --上架单号
                                     i_oper        IN bill_im_instock.creator%TYPE, --操作人
                                     i_dest_cell_no IN bill_im_instock_dtl.dest_cell_no%TYPE, --预上储位
                                     i_dest_cell_id IN bill_im_instock_dtl.dest_cell_id%TYPE, --预上储位ID
                                     i_real_cell_no IN bill_im_instock_dtl.real_cell_no%TYPE, --实际上架储位
                                     v_real_cell_id IN con_content.cell_id%TYPE, -- 实际的储位ID
                                     v_item_no      IN bill_im_instock_dtl.item_no%TYPE, -- 商品编码
                                     v_size_no      IN bill_im_instock_dtl.size_no%TYPE, -- 尺码
                                     i_real_qty     IN bill_im_instock_dtl.real_qty%TYPE, --实际上架数量
                                     i_type         IN VARCHAR2, --类型A 新增库存记录  U 更新库存记录
                                     i_item_type    IN bill_im_instock_dtl.item_type%TYPE,--商品属性
                                     i_quality      IN bill_im_instock_dtl.quality%TYPE,  --品质
                                     o_stroutmsg    OUT VARCHAR2) IS
    v_strBox_No      con_box.box_no%TYPE;
    v_strSupplier_No supplier.supplier_no%TYPE;
  
  BEGIN
  
    --updt by crm 20140111 统一库存记账，更新预上储位库存信息 减去库存数
    --开始
    ACC_PREPARE_DATA_EXT(i_instock_no,
                         'IP',
                         'O',
                         i_oper,
                         1,
                         I_LOCNO      => i_locno,
                         I_OWNER_NO   => i_owner,
                         I_CELL_ID    => i_dest_cell_id,
                         I_CELL_NO    => i_dest_cell_no,
                         I_QTY        => i_real_qty,
                         I_ITEM_TYPE    => i_item_type,
                         I_QUALITY      => i_quality);
    acc_apply(i_instock_no, '2', 'IP', 'O', 1);
    --结束    
  
  --log20140322 modi by chenhaitao 不管是否修改储位，均查询出箱号与供应商
  begin
    SELECT d.label_no, ite.supplier_no
        INTO v_strBox_No, v_strSupplier_No
        FROM bill_im_instock_dtl d, item ite
       WHERE d.locno = i_locno
         AND d.owner_no = i_owner
         AND d.instock_no = i_instock_no
         AND d.item_no = v_item_no
         AND d.size_no = v_size_no
         AND d.instock_id = i_instock_id
         AND d.item_no = ite.item_no;
    exception when no_data_found then
      o_stroutmsg := 'N|' || SQLERRM ||
               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
      RETURN;
    end;
    --end log2014032
    -- 新插入库存记录
    IF i_type = 'A' THEN
      --updt by crm 20140111 统一库存记账，写储储位预上量
      --开始
    
      acc_prepare_data_ext(i_instock_no,
                           'IP',
                           'I',
                           i_oper,
                           i_instock_id,
                           '',
                           i_locno,
                           i_real_cell_no,
                           v_item_no,
                           v_size_no,
                           1,
                           i_item_type,
                           i_quality,
                           i_owner,
                           v_strSupplier_No,
                           v_strBox_No,
                           i_real_qty,
                           0,
                           0,
                           '0',
                           '0',
                           '1');

      acc_apply(i_instock_no, '2', 'IP', 'I', 1);
      --结束    
    
    ELSE
      -- 更新库存记录
    
      --updt by crm 20140111 统一库存记账，更新库存 直接加库存
      --开始
    
      ACC_PREPARE_DATA_EXT(i_instock_no,
                           'IP',
                           'I',
                           i_oper,
                           1,
                           I_LOCNO      => i_locno,
                           I_OWNER_NO   => i_owner,
                           I_CELL_ID    => v_real_cell_id,
                           I_CELL_NO    => i_real_cell_no,
                           I_QTY        => v_real_cell_id,
                           I_ITEM_TYPE    => i_item_type,
                           I_QUALITY      => i_quality);
      acc_apply(i_instock_no, '2', 'IP', 'I', 1);
      --结束
         
    END IF;
    --log20140322 modi by chenhaitao 上架完后回写箱码储位
      --回写箱码储位
      UPDATE con_box x
         SET x.cell_no = i_real_cell_no
       WHERE x.locno = i_locno
         AND x.owner_no = i_owner
         AND x.box_no = v_strBox_No;
    --end log20140322
  END PROC_INSTOCK_CON_CONTENT;

END PKG_IM_INSTOCK_AUDIT_CITY;
/
