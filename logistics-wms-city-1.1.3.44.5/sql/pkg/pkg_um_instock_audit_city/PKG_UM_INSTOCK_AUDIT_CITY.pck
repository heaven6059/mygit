create or replace package PKG_UM_INSTOCK_AUDIT_CITY is

--上架审核
  -- Author  : LUO.HL
  -- Created : 2013-12-10 10:14:02
  -- Purpose : 
  PROCEDURE PROC_INSTOCK_AUDIT(i_locno      IN bill_um_instock.locno%TYPE, --仓别
                               i_owner      IN bill_um_instock.owner_no%TYPE, --委托业主
                               i_instock_no IN bill_um_instock.instock_no%TYPE, --上架单号
                               stroutmsg    OUT VARCHAR2 --处理返回的结果
                               );

  --上架回单
  -- Author  : LUO.HL
  -- Created : 2013-12-10 10:14:02
  -- Purpose : 
  PROCEDURE PROC_INSTOCK_RECEIPT(i_locno        IN bill_um_instock.locno%TYPE, --仓别
                                 i_owner        IN bill_um_instock.owner_no%TYPE, --委托业主
                                 i_instock_id   IN bill_um_instock_dtl.instock_id%TYPE, --上架单明细ID
                                 i_instock_no   IN bill_um_instock.instock_no%TYPE, --上架单号
                                 i_cell_no      IN bill_um_instock_dtl.cell_no%TYPE, --来源储位
                                 i_cell_id      IN bill_um_instock_dtl.cell_id%TYPE, --来源储位ID
                                 i_dest_cell_no IN bill_um_instock_dtl.dest_cell_no%TYPE, --预上储位
                                 i_dest_cell_id IN bill_um_instock_dtl.dest_cell_id%TYPE, --预上储位ID
                                 stroutmsg      OUT VARCHAR2);
  --移库存
  -- Author  : LUO.HL
  -- Created : 2013-12-10 10:14:02
  -- Purpose :                              
  PROCEDURE PROC_INSTOCK_CON_CONTENT(i_locno        IN bill_um_instock.locno%TYPE, --仓别
                                     i_owner        IN bill_um_instock.owner_no%TYPE, --委托业主  
                                     i_instock_id   IN bill_um_instock_dtl.instock_id%TYPE, --上架单明细ID
                                     i_instock_no   IN bill_um_instock.instock_no%TYPE, --上架单号
                                     i_dest_cell_no IN bill_um_instock_dtl.dest_cell_no%TYPE, --预上储位
                                     i_dest_cell_id IN bill_um_instock_dtl.dest_cell_id%TYPE, --预上储位ID
                                     i_real_cell_no IN bill_um_instock_dtl.real_cell_no%TYPE, --实际上架储位
                                     i_type         IN VARCHAR2, --类型A 新增库存记录  U 更新库存记录
                                     stroutmsg      OUT VARCHAR2);

end PKG_UM_INSTOCK_AUDIT_CITY;
/
CREATE OR REPLACE PACKAGE BODY PKG_UM_INSTOCK_AUDIT_CITY IS
  PROCEDURE PROC_INSTOCK_AUDIT(i_locno      IN bill_um_instock.locno%TYPE, --仓别
                               i_owner      IN bill_um_instock.owner_no%TYPE, --委托业主
                               i_instock_no IN bill_um_instock.instock_no%TYPE, --上架单号
                               stroutmsg    OUT VARCHAR2 --处理返回的结果
                               ) IS
  
    v_con_count NUMBER; --库存数量
    v_mix_flag  NUMBER(1); --储位是否可混
  BEGIN
  
    v_con_count := 0;
    v_mix_flag  := 0;
    stroutmsg   := 'Y';
    --查询所有上架单明细
    FOR dtl IN (SELECT *
                  FROM bill_um_instock_dtl d
                 WHERE d.locno = i_locno
                   AND d.owner_no = i_owner
                   AND d.instock_no = i_instock_no) LOOP
      --判断实际上架储位跟计划储位是否一致
      IF (dtl.real_cell_no = dtl.dest_cell_no) THEN
        --一致调用回单存储过程
        PKG_UM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_RECEIPT(i_locno,
                                                       i_owner,
                                                       dtl.instock_id,
                                                       i_instock_no,
                                                       dtl.cell_no,
                                                       dtl.cell_id,
                                                       dtl.dest_cell_no,
                                                       dtl.dest_cell_id,
                                                       stroutmsg);
        IF (stroutmsg <> 'Y') THEN
          RETURN;
        END IF;
      
      ELSE
        --判断实际储位是否有库存
        SELECT COUNT(1)
          INTO v_con_count
          FROM con_content c
         WHERE c.locno = dtl.locno
           AND c.cell_no = dtl.real_cell_no;
        --有库存
        IF (v_con_count > 0) THEN
          --判断是否同款
          SELECT COUNT(1)
            INTO v_con_count
            FROM con_content c
           WHERE c.locno = dtl.locno
             AND c.cell_no = dtl.real_cell_no
             AND c.item_no = dtl.item_no
             AND c.item_id = dtl.item_id;
          IF (v_con_count > 0) THEN
            --同款
            PKG_UM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_RECEIPT(i_locno,
                                                           i_owner,
                                                           dtl.instock_id,
                                                           i_instock_no,
                                                           dtl.cell_no,
                                                           dtl.cell_id,
                                                           dtl.dest_cell_no,
                                                           dtl.dest_cell_id,
                                                           stroutmsg);
            IF (stroutmsg <> 'Y') THEN
              RETURN;
            END IF;
            --调用移库存储过程
            PKG_UM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_CON_CONTENT(i_locno,
                                                               i_owner,
                                                               dtl.instock_id,
                                                               i_instock_no,
                                                               dtl.dest_cell_no,
                                                               dtl.dest_cell_id,
                                                               dtl.real_cell_no,
                                                               'U',
                                                               stroutmsg);
            IF (stroutmsg <> 'Y') THEN
              RETURN;
            END IF;
          ELSE
            --不同款
            --判断是否可混
          
            SELECT nvl(c.mix_flag, 0)
              INTO v_mix_flag
              FROM cm_defcell c
             WHERE c.locno = i_locno
               AND c.cell_no = dtl.real_cell_no;
            IF (v_mix_flag = 2) THEN
              --可混，调用存储过程
              PKG_UM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_RECEIPT(i_locno,
                                                             i_owner,
                                                             dtl.instock_id,
                                                             i_instock_no,
                                                             dtl.cell_no,
                                                             dtl.cell_id,
                                                             dtl.dest_cell_no,
                                                             dtl.dest_cell_id,
                                                             stroutmsg);
              IF (stroutmsg <> 'Y') THEN
                RETURN;
              END IF;
              --调用移库存储过程
              PKG_UM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_CON_CONTENT(i_locno,
                                                                 i_owner,
                                                                 dtl.instock_id,
                                                                 i_instock_no,
                                                                 dtl.dest_cell_no,
                                                                 dtl.dest_cell_id,
                                                                 dtl.real_cell_no,
                                                                 'A',
                                                                 stroutmsg);
              IF (stroutmsg <> 'Y') THEN
                RETURN;
              END IF;
            ELSE
              --不可混，数据回滚，直接返回
              stroutmsg := '储位不可混装不同款货品';
              ROLLBACK;
              RETURN;
            END IF;
          END IF;
        ELSE
          --无库存
          PKG_UM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_RECEIPT(i_locno,
                                                         i_owner,
                                                         dtl.instock_id,
                                                         i_instock_no,
                                                         dtl.cell_no,
                                                         dtl.cell_id,
                                                         dtl.dest_cell_no,
                                                         dtl.dest_cell_id,
                                                         stroutmsg);
          IF (stroutmsg <> 'Y') THEN
            RETURN;
          END IF;
          --调用移库存储过程
          PKG_UM_INSTOCK_AUDIT_CITY.PROC_INSTOCK_CON_CONTENT(i_locno,
                                                             i_owner,
                                                             dtl.instock_id,
                                                             i_instock_no,
                                                             dtl.dest_cell_no,
                                                             dtl.dest_cell_id,
                                                             dtl.real_cell_no,
                                                             'A',
                                                             stroutmsg);
          IF (stroutmsg <> 'Y') THEN
            RETURN;
          END IF;
        
        END IF;
      END IF;
    
    END LOOP;
  
    --整单上架   更新上架单主档状态
    UPDATE bill_um_instock b
       SET b.status = '13'
     WHERE b.locno = i_locno
       AND b.owner_no = i_owner
       AND b.instock_no = i_instock_no;
  
  END PROC_INSTOCK_AUDIT;

  --上架回单
  -- Author  : LUO.HL
  -- Created : 2013-11-27 20:08:52
  -- Purpose : 
  PROCEDURE PROC_INSTOCK_RECEIPT(i_locno        IN bill_um_instock.locno%TYPE, --仓别
                                 i_owner        IN bill_um_instock.owner_no%TYPE, --委托业主
                                 i_instock_id   IN bill_um_instock_dtl.instock_id%TYPE, --上架单明细ID
                                 i_instock_no   IN bill_um_instock.instock_no%TYPE, --上架单号
                                 i_cell_no      IN bill_um_instock_dtl.cell_no%TYPE, --来源储位
                                 i_cell_id      IN bill_um_instock_dtl.cell_id%TYPE, --来源储位ID
                                 i_dest_cell_no IN bill_um_instock_dtl.dest_cell_no%TYPE, --预上储位
                                 i_dest_cell_id IN bill_um_instock_dtl.dest_cell_id%TYPE, --预上储位IDstroutmsg  
                                 stroutmsg      OUT VARCHAR2 --处理返回的结果
                                 ) IS
    v_row_id       NUMBER; --rowID
    v_container_no VARCHAR(25); --容器号
    v_item_id      con_content.item_id%TYPE; --商品ID
    v_item_no      con_content.item_no%TYPE; --商品编码
    v_qty          con_content.qty%TYPE; --数量
    v_outstock_qty con_content.outstock_qty%TYPE;
    v_instock_qty  con_content.Instock_Qty%TYPE;
    v_stock_type   con_content.stock_type%TYPE;
    v_stock_value  con_content.stock_value%TYPE;
    v_real_qty     bill_um_instock_dtl.real_qty%TYPE;
  BEGIN
    --写记账表
    --取row_id
    SELECT SEQ_AUTHORITY_MENU_ID.NEXTVAL INTO v_row_id FROM DUAL;
    --查询库存表
    BEGIN
      SELECT c.container_no,
             c.item_id,
             c.item_no,
             nvl(c.qty, 0),
             nvl(c.instock_qty, 0),
             nvl(c.outstock_qty, 0),
             c.stock_type,
             c.stock_value
        INTO v_container_no,
             v_item_id,
             v_item_no,
             v_qty,
             v_instock_qty,
             v_outstock_qty,
             v_stock_type,
             v_stock_value
        FROM con_content c
       WHERE c.locno = i_locno
         AND c.owner_no = i_owner
         AND c.cell_no = i_dest_cell_no
         AND c.cell_id = i_dest_cell_id;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        stroutmsg := 'N|预上储位编码:' || i_dest_cell_no || ',储位id:' ||
                     i_dest_cell_id || '没有库存';
        ROLLBACK;
        RETURN;
    END;
  
    IF (v_qty < 0 OR v_instock_qty < 0) THEN
      ROLLBACK;
      stroutmsg := 'N|预上储位编码:' || i_dest_cell_no || ',编码id:' ||
                   i_dest_cell_id || '预上数不足';
      RETURN;
    END IF;
  
    INSERT INTO CON_CONTENT_MOVE
      (LOCNO,
       OWNER_NO,
       ROW_ID,
       PAPER_TYPE,
       PAPER_NO,
       TERMINAL_FLAG,
       CELL_NO,
       CELL_ID,
       CONTAINER_NO,
       ITEM_NO,
       ITEM_ID,
       FIRST_QTY,
       FIRST_INSTOCK_QTY， FIRST_OUTSTOCK_QTY,
       R_CELL_NO,
       IO_FLAG,
       CREATETM,
       STOCK_TYPE,
       STOCK_VALUE)
    VALUES
      (i_locno,
       i_owner,
       v_row_id,
       'IP',
       i_instock_no,
       '1',
       i_cell_no,
       i_cell_id,
       v_container_no,
       v_item_no,
       v_item_id,
       v_qty,
       v_instock_qty,
       v_outstock_qty,
       i_dest_cell_no,
       'I',
       SYSDATE,
       v_stock_type,
       v_stock_value);
    --查询实际上架数量  如果实际库存数量为0，那么取计划上架数量
    SELECT CASE
             WHEN nvl(d.real_qty, 0) = 0 THEN
              NVL(d.item_qty, 0)
             ELSE
              nvl(d.real_qty, 0)
           END
      INTO v_real_qty
      FROM bill_um_instock_dtl d
     WHERE d.locno = i_locno
       AND d.owner_no = i_owner
       AND d.instock_no = i_instock_no
       AND d.instock_id = i_instock_id;
    --更新库存表信息
    UPDATE con_content c
       SET c.qty         = c.qty + v_real_qty,
           c.instock_qty = c.instock_qty - v_real_qty
     WHERE c.locno = i_locno
       AND c.owner_no = i_owner
       AND c.cell_no = i_dest_cell_no
       AND c.cell_id = i_dest_cell_id;
  END PROC_INSTOCK_RECEIPT;

  --移库存
  -- Author  : LUO.HL
  -- Created : 2013-11-27 20:08:52
  -- Purpose : 
  PROCEDURE PROC_INSTOCK_CON_CONTENT(i_locno        IN bill_um_instock.locno%TYPE, --仓别
                                     i_owner        IN bill_um_instock.owner_no%TYPE, --委托业主  
                                     i_instock_id   IN bill_um_instock_dtl.instock_id%TYPE, --上架单明细ID
                                     i_instock_no   IN bill_um_instock.instock_no%TYPE, --上架单号
                                     i_dest_cell_no IN bill_um_instock_dtl.dest_cell_no%TYPE, --预上储位
                                     i_dest_cell_id IN bill_um_instock_dtl.dest_cell_id%TYPE, --预上储位ID
                                     i_real_cell_no IN bill_um_instock_dtl.real_cell_no%TYPE, --实际上架储位
                                     i_type         IN VARCHAR2, --类型A 新增库存记录  U 更新库存记录
                                     stroutmsg      OUT VARCHAR2) IS
    v_row_id       NUMBER; --rowID
    v_container_no VARCHAR(25); --容器号
    v_item_id      con_content.item_id%TYPE; --商品ID
    v_item_no      con_content.item_no%TYPE; --商品编码
    v_pack_qty     bill_um_instock_dtl.pack_qty%TYPE; --包装数量
    v_qty          con_content.qty%TYPE; --数量
    v_outstock_qty con_content.outstock_qty%TYPE;
    v_instock_qty  con_content.Instock_Qty%TYPE;
    v_stock_type   con_content.stock_type%TYPE;
    v_stock_value  con_content.stock_value%TYPE;
    v_real_qty     bill_um_instock_dtl.real_qty%TYPE;
    v_cell_id      con_content.cell_id%TYPE;
    v_label_no     bill_um_instock_dtl.label_no%TYPE;
  BEGIN
    --写记账表
    --取row_id
    SELECT SEQ_AUTHORITY_MENU_ID.NEXTVAL INTO v_row_id FROM DUAL;
    --查询库存表
    BEGIN
      SELECT c.container_no,
             c.item_id,
             c.item_no,
             nvl(c.qty, 0),
             nvl(c.instock_qty, 0),
             nvl(c.outstock_qty, 0),
             c.stock_type,
             c.stock_value
        INTO v_container_no,
             v_item_id,
             v_item_no,
             v_qty,
             v_instock_qty,
             v_outstock_qty,
             v_stock_type,
             v_stock_value
        FROM con_content c
       WHERE c.locno = i_locno
         AND c.owner_no = i_owner
         AND c.cell_no = i_dest_cell_no
         AND c.cell_id = i_dest_cell_id;
    EXCEPTION
      WHEN no_data_found THEN
        stroutmsg := 'N|预上储位编码:' || i_dest_cell_no || ',编码id:' ||
                     i_dest_cell_id || '没有库存';
        ROLLBACK;
        RETURN;
    END;
    --查询实际上架数量
    SELECT CASE
             WHEN nvl(d.real_qty, 0) = 0 THEN
              NVL(d.item_qty, 0)
             ELSE
              nvl(d.real_qty, 0)
           END
      INTO v_real_qty
      FROM bill_um_instock_dtl d
     WHERE d.locno = i_locno
       AND d.owner_no = i_owner
       AND d.instock_no = i_instock_no
       AND d.instock_id = i_instock_id;
  
    IF (v_qty < 0 OR v_instock_qty < 0) THEN
      ROLLBACK;
      stroutmsg := 'N|预上储位编码:' || i_dest_cell_no || ',储位id:' ||
                   i_dest_cell_id || '预上数不足';
      RETURN;
    END IF;
  
    --写记账表
    INSERT INTO CON_CONTENT_MOVE
      (LOCNO,
       OWNER_NO,
       ROW_ID,
       PAPER_TYPE,
       PAPER_NO,
       TERMINAL_FLAG,
       CELL_NO,
       CELL_ID,
       CONTAINER_NO,
       ITEM_NO,
       ITEM_ID,
       FIRST_QTY,
       FIRST_INSTOCK_QTY， FIRST_OUTSTOCK_QTY,
       R_CELL_NO,
       IO_FLAG,
       MOVE_QTY,
       CREATETM,
       STOCK_TYPE,
       STOCK_VALUE)
    VALUES
      (i_locno,
       i_owner,
       v_row_id,
       'IP',
       i_instock_no,
       '1',
       i_dest_cell_no,
       i_dest_cell_id,
       v_container_no,
       v_item_no,
       v_item_id,
       v_qty,
       v_instock_qty,
       v_outstock_qty,
       i_real_cell_no,
       v_real_qty,
       'I',
       SYSDATE,
       v_stock_type,
       v_stock_value);
  
    --更新预上储位库存信息 
    UPDATE con_content c
       SET c.qty         = c.qty - v_real_qty,
           c.instock_qty = c.instock_qty + v_real_qty
     WHERE c.locno = i_locno
       AND c.owner_no = i_owner
       AND c.cell_no = i_dest_cell_no
       AND c.cell_id = i_dest_cell_id;
  
    --如果实际储位库存不足，回滚数据
    SELECT nvl(c.qty, 0)
      INTO v_qty
      FROM con_content c
     WHERE c.locno = i_locno
       AND c.owner_no = i_owner
       AND c.cell_no = i_real_cell_no;
    IF (v_qty - v_real_qty < 0) THEN
      ROLLBACK;
      stroutmsg := 'N|预上储位编码:' || i_dest_cell_no || ',储位id:' ||
                   i_dest_cell_id || '库存不足';
      ROLLBACK;
      RETURN;
    END IF;
  
    --查询上架单明细表信息
    SELECT d.container_no,
           d.item_no,
           d.item_id,
           d.pack_qty,
           d.item_qty,
           d.label_no
      INTO v_container_no,
           v_item_no,
           v_item_id,
           v_pack_qty,
           v_instock_qty,
           v_label_no
    
      FROM bill_um_instock_dtl d
     WHERE d.locno = i_locno
       AND d.owner_no = i_owner
       AND d.instock_no = i_instock_no
       AND d.instock_id = i_instock_id;
  
    --更新库存记录
    IF (i_type = 'A') THEN
      --新增库存
      SELECT SEQ_CON_CONTENT.NEXTVAL INTO v_cell_id FROM DUAL;
      INSERT INTO CON_CONTENT
        (locno,
         owner_no,
         cell_no,
         cell_id,
         container_no,
         item_no,
         item_id,
         pack_qty,
         qty,
         instock_qty,
         createtm,
         edittm,
         label_no)
      VALUES
        (I_LOCNO,
         I_OWNER,
         I_REAL_CELL_NO,
         V_CELL_ID,
         V_CONTAINER_NO,
         V_ITEM_NO,
         V_ITEM_ID,
         V_PACK_QTY,
         V_REAL_QTY,
         V_INSTOCK_QTY,
         SYSDATE,
         SYSDATE,
         V_LABEL_NO);
    
    ELSE
      --更新库存
      UPDATE con_content c
         SET c.qty         = c.qty + v_real_qty,
             c.instock_qty = c.instock_qty - v_real_qty
       WHERE c.locno = i_locno
         AND c.owner_no = i_owner
         AND c.cell_no = i_real_cell_no
         AND c.item_no = v_item_no
         AND c.item_id = v_item_id;
    END IF;
    --删除实际、预上、预下均为零的库存
    DELETE FROM con_content c
     WHERE c.locno = c.locno
       AND c.owner_no = i_owner
       AND c.cell_no = i_real_cell_no
       AND c.qty = 0
       AND c.instock_qty = 0
       AND c.outstock_qty = 0;
  
  END PROC_INSTOCK_CON_CONTENT;
END PKG_UM_INSTOCK_AUDIT_CITY;
/
