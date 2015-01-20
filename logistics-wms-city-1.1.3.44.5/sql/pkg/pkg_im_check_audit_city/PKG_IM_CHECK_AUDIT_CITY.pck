create or replace package PKG_IM_CHECK_AUDIT_CITY is

 -- Author  : LUO.HL
  -- Created : 2013-11-19 19:44:38
  -- Purpose : 收货单验收审核
procedure PROC_IM_CHECK_AUDIT(I_locno     in bill_im_check.locno%type, --仓别
                                                I_owner     in bill_im_check.owner_no%type, --委托业主
                                                I_check_no  in bill_im_check.check_no%type, --退仓验收单编码
                                                I_oper_user varchar2, --操作用户
                                                strOutMsg   out varchar2 --处理返回的结果
                                                );
end PKG_IM_CHECK_AUDIT_CITY;
/
CREATE OR REPLACE PACKAGE BODY PKG_IM_CHECK_AUDIT_CITY IS

  PROCEDURE PROC_IM_CHECK_AUDIT(I_LOCNO     IN BILL_IM_CHECK.LOCNO%TYPE, --仓别
                                I_OWNER     IN BILL_IM_CHECK.OWNER_NO%TYPE, --委托业主
                                I_CHECK_NO  IN BILL_IM_CHECK.CHECK_NO%TYPE, --退仓验收单编码
                                I_OPER_USER VARCHAR2, --操作用户
                                STROUTMSG   OUT VARCHAR2 --处理返回的结果
                                ) IS
    V_STATUS               VARCHAR2(2); --状态
    V_IMPORT_NO            VARCHAR2(20); --通知单号
    V_S_IMPORT_NO          VARCHAR2(20); --收货单号
    V_RECEIPT_COUNT        NUMBER; --收货单数量
    V_RECEIPT__DTL_NO      NUMBER; --收货单详情数量
    V_BARCODE              VARCHAR2(20); --barcode
    V_PACK_QTY             NUMBER;
    V_LABEL_NO             VARCHAR2(20); --箱号
    V_CONTAINER_NO         VARCHAR2(20); --容器号
    V_SESSION_ID           VARCHAR2(50); --不返回标签号时，返回sessionID号
    V_CELL_NO              VARCHAR(24); --储位编码
    V_SUPPLIER_NO          VARCHAR(10); --供应商编码
    V_IM_IMPORTSTATUS_TEMP VARCHAR2(2);
    V_IM_LOG_MSG           VARCHAR2(100);
    V_IM_RECEIPTSTATUS     VARCHAR2(2);
    V_CHECK_QTY_ALL        NUMBER;
    V_SCAN_LABEL_NO        BILL_IM_CHECK_PAL.SCAN_LABEL_NO%TYPE;
    V_NCount number:=0;
    V_IN_CHECK_QTY NUMBER := 0;
    V_LOSE_CHECK_QTY NUMBER := 0;
    V_CM_PO_QTY NUMBER := 0;
    V_MAX_RECEIPT_ID NUMBER := 0;
    V_MAX_IMPORT_ID NUMBER := 0;
    V_MAX_BOX_ID NUMBER := 0;
    V_YX_NUM NUMBER := 0;
    V_YX_FOR_NUM NUMBER := 0;
    V_CM_IMPORT_QTY VARCHAR2(50) := 'N';
    V_CM_IMPORT_NO VARCHAR2(50) := 'N';
    V_LOSE_IMPORT_NO VARCHAR2(50) := 'N';
    V_IMPORT_STATUS VARCHAR2(50) := '0';
    
  BEGIN
    V_PACK_QTY := 1;
    STROUTMSG  := 'Y'; --初始化返回信息值
    V_CHECK_QTY_ALL:=0;

    --查询主表信息，并且状态不为10
    BEGIN
      SELECT C.STATUS, C.SUPPLIER_NO, C.S_IMPORT_NO
        INTO V_STATUS, V_SUPPLIER_NO, V_S_IMPORT_NO
        FROM BILL_IM_CHECK C
       WHERE C.LOCNO = I_LOCNO
         AND C.OWNER_NO = I_OWNER
         AND C.CHECK_NO = I_CHECK_NO;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        IF V_STATUS <> '10' THEN
          ROLLBACK;
          RETURN;
        END IF;
        ROLLBACK;
        RETURN;
    END;

    --查询收货单

    SELECT COUNT(1)
      INTO V_RECEIPT_COUNT
      FROM BILL_IM_RECEIPT R
     WHERE R.LOCNO = I_LOCNO
       AND R.OWNER_NO = I_OWNER
       AND R.RECEIPT_NO = V_S_IMPORT_NO;

    IF (V_RECEIPT_COUNT = 0) THEN
      STROUTMSG := 'N|没有查询到对应的收货单！';
      ROLLBACK;
      RETURN;
    END IF;
    --查询验收明细验收数量，如果明细收货数量全部为0，那么不能审核
    SELECT nvl(SUM(d.check_qty),0) INTO V_CHECK_QTY_ALL FROM BILL_IM_CHECK_DTL d   WHERE LOCNO = I_LOCNO
                          AND OWNER_NO = I_OWNER
                          AND CHECK_NO = I_CHECK_NO;
    IF(V_CHECK_QTY_ALL=0)THEN
            STROUTMSG := 'N|验收明细验收数量全部为0，不能审核';
    END IF;
    
    
    -----分摊数量
    FOR IMCHECKDTL IN (SELECT l.*,ite.Supplier_No
                         FROM BILL_IM_CHECK_DTL l,item ite
                        WHERE LOCNO = I_LOCNO
                          AND OWNER_NO = I_OWNER
                          AND l.ITEM_NO=ite.ITEM_NO
                          AND CHECK_NO = I_CHECK_NO
                          AND l.Po_Qty > 0) LOOP
                
         V_LOSE_CHECK_QTY := IMCHECKDTL.CHECK_QTY;
         V_YX_NUM := 0;
         V_YX_FOR_NUM := 0;
         
         SELECT COUNT(*)
           INTO V_YX_NUM
           FROM BILL_IM_RECEIPT_DTL RDTL
          WHERE RDTL.RECEIPT_NO = V_S_IMPORT_NO
            AND RDTL.ITEM_NO = IMCHECKDTL.ITEM_NO
            AND RDTL.SIZE_NO = IMCHECKDTL.SIZE_NO
            AND RDTL.BOX_NO = IMCHECKDTL.BOX_NO
            AND RDTL.RECEIPT_QTY - NVL(RDTL.CHECK_QTY, 0) > 0
          ORDER BY RDTL.RECEIPT_NO ASC;
         
         
         --循环收货单明细分摊数量
         FOR IMRECEIPTDTL IN (SELECT RDTL.*, IIT.TRANS_NO
                               FROM BILL_IM_RECEIPT_DTL RDTL
                              INNER JOIN BILL_IM_IMPORT IIT
                                 ON IIT.LOCNO = RDTL.LOCNO
                                AND IIT.IMPORT_NO = RDTL.IMPORT_NO
                              WHERE RDTL.RECEIPT_NO = V_S_IMPORT_NO
                                AND RDTL.ITEM_NO = IMCHECKDTL.ITEM_NO
                                AND RDTL.SIZE_NO = IMCHECKDTL.SIZE_NO
                                AND RDTL.BOX_NO = IMCHECKDTL.BOX_NO
                                AND RDTL.RECEIPT_QTY - NVL(RDTL.CHECK_QTY, 0) > 0
                              ORDER BY IIT.TRANS_NO ASC) LOOP
                             
             V_YX_FOR_NUM := V_YX_FOR_NUM + 1;
                             
             IF V_LOSE_CHECK_QTY > 0
               THEN 
                  
                   ----计算分摊多少数量
                   V_IN_CHECK_QTY := 0;
                   IF V_LOSE_CHECK_QTY > IMRECEIPTDTL.RECEIPT_QTY-NVL(IMRECEIPTDTL.CHECK_QTY, 0) THEN
                      IF V_YX_FOR_NUM = V_YX_NUM
                        THEN
                          V_IN_CHECK_QTY := V_LOSE_CHECK_QTY;
                      ELSE
                          V_IN_CHECK_QTY := NVL(IMRECEIPTDTL.RECEIPT_QTY, 0) - NVL(IMRECEIPTDTL.CHECK_QTY, 0);
                      END IF;
                      
                   ELSE
                      V_IN_CHECK_QTY := V_LOSE_CHECK_QTY;
                   END IF;
                   
              
                   V_LOSE_CHECK_QTY := V_LOSE_CHECK_QTY-(IMRECEIPTDTL.RECEIPT_QTY-NVL(IMRECEIPTDTL.CHECK_QTY,0)); 
                   -----预到货通知单号 
                   V_LOSE_IMPORT_NO := IMRECEIPTDTL.IMPORT_NO;
                   
                   ----更新收货单明细验收数量                          
                   UPDATE BILL_IM_RECEIPT_DTL DTL
                      SET DTL.CHECK_QTY = NVL(DTL.CHECK_QTY, 0) + V_IN_CHECK_QTY
                    WHERE DTL.LOCNO = IMRECEIPTDTL.LOCNO
                      AND DTL.RECEIPT_NO = IMRECEIPTDTL.RECEIPT_NO
                      AND DTL.OWNER_NO = IMRECEIPTDTL.OWNER_NO
                      AND DTL.ROW_ID = IMRECEIPTDTL.ROW_ID;
                   IF SQL%ROWCOUNT <= 0 THEN
                        ROLLBACK;
                        STROUTMSG := 'N|更新收货单明细验收数量分摊失败!';
                        RETURN;
                   END IF; 
                   
                   --查找预到货通知单状态，如果是90和91的状态就不去更新通知单验收数量
                   BEGIN
                     SELECT II.STATUS
                       INTO V_IMPORT_STATUS
                       FROM BILL_IM_IMPORT II
                      WHERE II.LOCNO = I_LOCNO
                        AND II.OWNER_NO = I_OWNER
                        AND II.IMPORT_NO = V_LOSE_IMPORT_NO;
                   EXCEPTION 
                     WHEN NO_DATA_FOUND THEN
                       STROUTMSG := 'N|没有找到预到货通知单!';
                       RETURN;
                   END;
                    
                   
                   IF V_IMPORT_STATUS <> '90' AND V_IMPORT_STATUS <> '91'
                     THEN
                       ----更新预到货通知单明细验收数量
                       UPDATE BILL_IM_IMPORT_DTL DTL
                           SET DTL.IMPORT_QTY = NVL(DTL.IMPORT_QTY, 0) + V_IN_CHECK_QTY
                         WHERE DTL.LOCNO = IMRECEIPTDTL.LOCNO
                           AND DTL.IMPORT_NO = V_LOSE_IMPORT_NO
                           AND DTL.ITEM_NO = IMRECEIPTDTL.ITEM_NO
                           AND DTL.SIZE_NO = IMRECEIPTDTL.SIZE_NO
                           AND DTL.BOX_NO = IMRECEIPTDTL.BOX_NO;
                         IF SQL%ROWCOUNT <= 0 THEN
                            ROLLBACK;
                            STROUTMSG := 'N|更新预到货通知单明细验收数量分摊失败!';
                            RETURN;
                         END IF;
                   END IF;
                   
                     
             ELSE
                 
                  EXIT;
                
             END IF;
                                      
         END LOOP;   
         
         --剩余的验收数量写入箱表
         IF V_LOSE_CHECK_QTY > 0 
           THEN
              UPDATE CON_BOX_DTL DTL
                 SET DTL.QTY = NVL(DTL.QTY, 0) + V_LOSE_CHECK_QTY,DTL.ADD_FLAG='1'
               WHERE DTL.LOCNO = I_LOCNO
                 AND DTL.BOX_NO = IMCHECKDTL.BOX_NO
                 AND DTL.ITEM_NO = IMCHECKDTL.ITEM_NO
                 AND DTL.SIZE_NO = IMCHECKDTL.SIZE_NO
                 AND DTL.IMPORT_NO = V_LOSE_IMPORT_NO;
               IF SQL%ROWCOUNT <= 0 THEN
                  ROLLBACK;
                  STROUTMSG := 'N|分摊更新多余箱码数量失败!';
                  RETURN;
               END IF;     
         END IF;       
               
         
    END LOOP;
    
    
    -----串款
    FOR IMCHECKDTL IN (SELECT l.*,ite.Supplier_No
                         FROM BILL_IM_CHECK_DTL l,item ite
                        WHERE LOCNO = I_LOCNO
                          AND OWNER_NO = I_OWNER
                          AND l.ITEM_NO=ite.ITEM_NO
                          AND CHECK_NO = I_CHECK_NO
                          AND l.po_qty = 0) LOOP
                          
            ---获取串码的预到货通知单号
            FOR IMRECEIPTDTL IN (SELECT DTL.LOCNO,DTL.OWNER_NO,DTL.IMPORT_NO 
                            FROM BILL_IM_RECEIPT_DTL DTL
                               WHERE DTL.LOCNO = I_LOCNO
                               AND DTL.OWNER_NO = I_OWNER
                               AND DTL.RECEIPT_NO = V_S_IMPORT_NO
                               GROUP BY DTL.LOCNO,DTL.OWNER_NO,DTL.IMPORT_NO
                               ORDER BY DTL.IMPORT_NO ASC) LOOP
                               
                 SELECT NVL(SUM(ITD.PO_QTY),0), NVL(SUM(ITD.IMPORT_QTY),0)
                   INTO V_CM_PO_QTY, V_CM_IMPORT_QTY
                   FROM BILL_IM_IMPORT_DTL ITD
                  WHERE ITD.LOCNO = IMRECEIPTDTL.LOCNO
                    AND ITD.OWNER_NO = IMRECEIPTDTL.OWNER_NO
                    AND ITD.IMPORT_NO = IMRECEIPTDTL.IMPORT_NO;
                                        
                  IF V_CM_PO_QTY <> V_CM_IMPORT_QTY
                    THEN 
                      V_CM_IMPORT_NO := IMRECEIPTDTL.IMPORT_NO;
                      EXIT;
                  END IF;
                                    
            END LOOP;    
            
            --如果没有获取到预到货通知单号
            IF V_CM_IMPORT_NO = 'N' OR V_CM_IMPORT_NO IS NULL
              THEN
                FOR IMRECEIPTDTL IN (SELECT DTL.LOCNO,DTL.OWNER_NO,DTL.IMPORT_NO 
                            FROM BILL_IM_RECEIPT_DTL DTL
                               WHERE DTL.LOCNO = I_LOCNO
                               AND DTL.OWNER_NO = I_OWNER
                               AND DTL.RECEIPT_NO = V_S_IMPORT_NO
                               GROUP BY DTL.LOCNO,DTL.OWNER_NO,DTL.IMPORT_NO) LOOP
                     
                     BEGIN
                       SELECT ITD.IMPORT_NO
                         INTO V_CM_IMPORT_NO
                         FROM BILL_IM_IMPORT_DTL ITD
                        WHERE ITD.LOCNO = I_LOCNO
                          AND ITD.OWNER_NO = I_OWNER
                          AND ITD.IMPORT_NO = IMRECEIPTDTL.IMPORT_NO
                          AND ITD.IMPORT_QTY != ITD.PO_QTY
                          AND ROWNUM = 1;
                     EXCEPTION
                       WHEN NO_DATA_FOUND THEN
                        NULL;
                     END;
                        
                      IF V_CM_IMPORT_NO IS NOT NULL
                        THEN
                          EXIT;
                      END IF;
                        
                END LOOP;
            END IF;
            
            -----如果还是为空，随便取个
            IF V_CM_IMPORT_NO = 'N' OR V_CM_IMPORT_NO IS NULL
              THEN
                SELECT DTL.IMPORT_NO
                  INTO V_CM_IMPORT_NO
                  FROM BILL_IM_RECEIPT_DTL DTL
                 WHERE DTL.LOCNO = I_LOCNO
                   AND DTL.OWNER_NO = I_OWNER
                   AND DTL.RECEIPT_NO = V_S_IMPORT_NO
                   AND ROWNUM = 1;
            END IF;
            
            SELECT NVL(MAX(DTL.ROW_ID), 0)+1
              INTO V_MAX_RECEIPT_ID
              FROM BILL_IM_RECEIPT_DTL DTL
             WHERE DTL.LOCNO = IMCHECKDTL.LOCNO
               AND DTL.OWNER_NO = IMCHECKDTL.OWNER_NO
               AND DTL.RECEIPT_NO = V_S_IMPORT_NO;
            
            --插入收货单明细
            insert into bill_im_receipt_dtl
              (locno,
               owner_no,
               receipt_no,
               import_no,
               row_id,
               box_no,
               qc_worker,
               iqc_status,
               check_worker1,
               check_worker2,
               batch_serial_no,
               status,
               item_no,
               size_no,
               pack_qty,
               receipt_qty,
               check_qty,
               divide_qty,
               ITEM_TYPE,
               QUALITY)
            values
              (IMCHECKDTL.LOCNO,
               IMCHECKDTL.OWNER_NO,
               V_S_IMPORT_NO,
               V_CM_IMPORT_NO,
               V_MAX_RECEIPT_ID,
               IMCHECKDTL.BOX_NO,
               IMCHECKDTL.QC_WORKER,
               IMCHECKDTL.IQC_STATUS,
               IMCHECKDTL.CHECK_WORKER1,
               IMCHECKDTL.CHECK_WORKER2,
               IMCHECKDTL.BATCH_SERIAL_NO,
               '13',
               IMCHECKDTL.ITEM_NO,
               IMCHECKDTL.SIZE_NO,
               IMCHECKDTL.PACK_QTY,
               0,
               IMCHECKDTL.CHECK_QTY,
               0,
               IMCHECKDTL.ITEM_TYPE,
               IMCHECKDTL.QUALITY);
             IF SQL%ROWCOUNT <= 0 THEN
                  ROLLBACK;
                  STROUTMSG := 'N|串码新增收货单明细失败!';
                  RETURN;
             END IF;
          
           ----插入预到货通知单
           SELECT NVL(MAX(DTL.PO_ID), 0)+1
              INTO V_MAX_IMPORT_ID
              FROM BILL_IM_IMPORT_DTL DTL
             WHERE DTL.LOCNO = IMCHECKDTL.LOCNO
               AND DTL.OWNER_NO = IMCHECKDTL.OWNER_NO
               AND DTL.IMPORT_NO = V_CM_IMPORT_NO;
               
           insert into bill_im_import_dtl
             (locno,
              owner_no,
              import_no,
              item_no,
              size_no,
              pack_qty,
              po_qty,
              import_qty,
              po_id,
              unit_cost,
              check_name,
              status,
              check_date,
              out_stock_flag,           
              box_no,
              car_plate,
              deliver_no,
              receipt_qty,
              trans_qty,
              ITEM_TYPE)
           values
             (IMCHECKDTL.LOCNO,
              IMCHECKDTL.OWNER_NO,
              V_CM_IMPORT_NO,
              IMCHECKDTL.ITEM_NO,
              IMCHECKDTL.SIZE_NO,
              IMCHECKDTL.Pack_Qty,
              0,
              IMCHECKDTL.CHECK_QTY,
              V_MAX_IMPORT_ID,
              '',
              I_OPER_USER,
              '13',
              SYSDATE,
              '0',                        
              IMCHECKDTL.BOX_NO,
              '',
              '',
              0,            
              0,
              IMCHECKDTL.ITEM_TYPE);
             IF SQL%ROWCOUNT <= 0 THEN
                  ROLLBACK;
                  STROUTMSG := 'N|串码新增预到货单明细失败!';
                  RETURN;
             END IF;
             
            --插入箱明细表
            SELECT NVL(MAX(DTL.BOX_ID), 0)+1
              INTO V_MAX_BOX_ID
              FROM CON_BOX_DTL DTL
             WHERE DTL.LOCNO = IMCHECKDTL.LOCNO
               AND DTL.OWNER_NO = IMCHECKDTL.OWNER_NO
               AND DTL.BOX_NO = IMCHECKDTL.BOX_NO;
               
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
              (IMCHECKDTL.LOCNO,
               IMCHECKDTL.OWNER_NO,
               IMCHECKDTL.BOX_NO,
               V_MAX_BOX_ID,
               IMCHECKDTL.ITEM_NO,
               'N',
               IMCHECKDTL.SIZE_NO,
               IMCHECKDTL.CHECK_QTY,
               '',
               '',
               '',
               '',
               '',
               '',
               V_CM_IMPORT_NO,
               '1');
             IF SQL%ROWCOUNT <= 0 THEN
                  ROLLBACK;
                  STROUTMSG := 'N|串码新增箱明细失败!';
                  RETURN;
             END IF;
    END LOOP; 
    
    
    --查询验收单明细
    FOR IMCHECKDTL IN (SELECT l.*,ite.supplier_no
                         FROM BILL_IM_CHECK_DTL l,item ite
                        WHERE LOCNO = I_LOCNO
                          AND OWNER_NO = I_OWNER
                          and l.item_no=ite.item_no
                          AND CHECK_NO = I_CHECK_NO) LOOP
                          
      V_SCAN_LABEL_NO:= IMCHECKDTL.BOX_NO;
      IF(V_SCAN_LABEL_NO IS NULL OR V_SCAN_LABEL_NO='N')THEN
        V_SCAN_LABEL_NO:='N';
      END IF;
      
      IF (IMCHECKDTL.PACK_QTY IS NOT NULL) THEN
        V_PACK_QTY := IMCHECKDTL.PACK_QTY;
      END IF;

      --获取商品barcode
      BEGIN
        SELECT BARCODE
          INTO V_BARCODE
          FROM ITEM_BARCODE
         WHERE ITEM_NO = IMCHECKDTL.ITEM_NO
           AND SIZE_NO = IMCHECKDTL.SIZE_NO
           AND PACK_QTY = V_PACK_QTY
           and PACKAGE_ID = 0 ;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          ROLLBACK;
          STROUTMSG := 'N|商品：'||IMCHECKDTL.Item_No||'获取主条码失败!';
          RETURN;
      END;
      --通过存储过程获取箱号

      PKG_LABEL.PROC_GET_CONTAINERNOBASE(I_LOCNO,
                                         'P',
                                         I_OPER_USER,
                                         'D',
                                         1,
                                         '0',
                                         '',
                                         V_LABEL_NO,
                                         V_CONTAINER_NO,
                                         V_SESSION_ID,
                                         STROUTMSG);

      --写入bill_Im_CheckPal
      INSERT INTO BILL_IM_CHECK_PAL
        (LOCNO,
         CHECK_NO,
         S_CHECK_NO,
         CHECK_ROW_ID,
         PRODUCE_DATE,
         EXPIRE_DATE,
         BARCODE,
         SCAN_LABEL_NO,
         STATUS,
         LABEL_NO,
         CONTAINER_NO,
         ITEM_NO,
         PACK_QTY,
         CHECK_QTY,
         OWNER_NO,
         PRICE,
         CREATETM,
         CREATOR,
         SIZE_NO,
         ITEM_TYPE)
      VALUES
        (I_LOCNO,
         IMCHECKDTL.CHECK_NO,
         IMCHECKDTL.CHECK_NO,
         IMCHECKDTL.ROW_ID,
         SYSDATE,
         SYSDATE,
         V_BARCODE,
         V_SCAN_LABEL_NO,
         '13',
         V_LABEL_NO,
         V_CONTAINER_NO,
         IMCHECKDTL.ITEM_NO,
         IMCHECKDTL.PACK_QTY,
         nvl(IMCHECKDTL.CHECK_QTY,0),
         IMCHECKDTL.OWNER_NO,
         IMCHECKDTL.PRICE,
         SYSDATE,
         I_OPER_USER,
         IMCHECKDTL.SIZE_NO,
         '0');
      IF SQL%ROWCOUNT <= 0 THEN
        ROLLBACK;
        STROUTMSG := 'N|写入bill_Im_CheckPal失败!';
        RETURN;
      END IF;

      --查找进货暂存区
       BEGIN
        SELECT C.CELL_NO
          INTO V_CELL_NO
          FROM CM_DEFAREA A
         INNER JOIN CM_DEFCELL C
            ON A.LOCNO = C.LOCNO
           AND A.WARE_NO = C.WARE_NO
           AND A.AREA_NO = C.AREA_NO
         WHERE ROWNUM = 1
           AND A.ATTRIBUTE_TYPE = '1'
           and a.AREA_ATTRIBUTE ='1'
           and c.cell_status ='0'
           and c.check_status ='0'
           AND A.LOCNO = I_LOCNO;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          ROLLBACK;
          STROUTMSG := 'N|获取储位失败!';
          RETURN;
      END;

      --updt by crm 20140110 统一库存记账，写储储位预上量
      --开始
      acc_prepare_data_ext(I_CHECK_NO ,
                          'SC' ,
                          'I' ,
                          I_OPER_USER ,
                          IMCHECKDTL.ROW_ID ,
                           '',
                          I_LOCNO,
                          V_CELL_NO,
                          IMCHECKDTL.ITEM_NO,
                          IMCHECKDTL.Size_No,
                          1,
                          IMCHECKDTL.ITEM_TYPE,
                          IMCHECKDTL.QUALITY,
                          IMCHECKDTL.Owner_No,
                          IMCHECKDTL.Supplier_No,
                          IMCHECKDTL.Box_No,
                          nvl(IMCHECKDTL.CHECK_QTY,0),
                          0,
                          0,
                          '0',
                          '0',
                          '1');
      --回写箱码储位
     update con_box x set x.cell_no=V_CELL_NO where x.locno=I_LOCNO and x.owner_no=IMCHECKDTL.Owner_No and x.box_no=V_SCAN_LABEL_NO
     and x.Box_No is not null;
     acc_apply(I_CHECK_NO,'2','SC','I',1);
     --结束


      if IMCHECKDTL.BOX_NO is not null and IMCHECKDTL.BOX_NO <> 'N' then
          --把验收单对应的收货单中的所有箱子的状态变更为2
          UPDATE CON_BOX
             SET STATUS = '2',
             CELL_NO=V_CELL_NO
           WHERE LOCNO = I_LOCNO
             AND BOX_NO = IMCHECKDTL.BOX_NO;
             -- AND S_IMPORT_NO = V_IMPORT_NO;
          IF SQL%ROWCOUNT <= 0 THEN
            ROLLBACK;
            STROUTMSG := 'N|更新验收单对应的收货单中的所有箱子的状态变失败!';
            RETURN;
          END IF;
      end if;
     V_NCount:=V_NCount+1;
                             
        
    END LOOP;
    
    
    /*FOR IMCHECKDTL IN (SELECT l.*,ite.supplier_no
                         FROM BILL_IM_CHECK_DTL l,item ite
                        WHERE LOCNO = I_LOCNO
                          AND OWNER_NO = I_OWNER
                          and l.item_no=ite.item_no
                          AND CHECK_NO = I_CHECK_NO) LOOP

      V_SCAN_LABEL_NO:= IMCHECKDTL.BOX_NO;
      IF(V_SCAN_LABEL_NO IS NULL)THEN
        V_SCAN_LABEL_NO:='N';
      END IF;

      --查询预到货通知单号
      BEGIN
        SELECT DISTINCT (B.IMPORT_NO)
          INTO V_IMPORT_NO
          FROM BILL_IM_RECEIPT_DTL B
         WHERE B.LOCNO = IMCHECKDTL.LOCNO
           AND B.OWNER_NO = IMCHECKDTL.OWNER_NO
           AND B.ITEM_NO = IMCHECKDTL.ITEM_NO
           AND B.SIZE_NO = IMCHECKDTL.SIZE_NO
           AND B.BOX_NO = V_SCAN_LABEL_NO
           AND B.RECEIPT_NO = V_S_IMPORT_NO;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          V_IMPORT_NO := 'N';
      END;

      --串码的商品通知单号为'N'
     \* IF(V_IMPORT_NO IS NULL)THEN
        V_IMPORT_NO:='N';
      END IF;*\

      --不是串码的商品更新预到货通知单和收货单明细验收数量
      IF V_SCAN_LABEL_NO <> 'N' AND V_SCAN_LABEL_NO IS NOT NULL THEN
        --更新收货单验收数量  通过箱号、商品编码、款号确定验收单和收货单明细一一对应
        UPDATE BILL_IM_RECEIPT_DTL B
           SET B.CHECK_QTY = nvl(B.CHECK_QTY, 0) +
                             (SELECT C.CHECK_QTY
                                FROM BILL_IM_CHECK_DTL C
                               WHERE C.BOX_NO = B.BOX_NO
                                 AND C.ITEM_NO = B.ITEM_NO
                                 AND C.SIZE_NO = B.SIZE_NO
                                 AND C.CHECK_NO = I_CHECK_NO)
         WHERE B.BOX_NO = IMCHECKDTL.BOX_NO
           AND B.ITEM_NO = IMCHECKDTL.ITEM_NO
           AND B.SIZE_NO = IMCHECKDTL.SIZE_NO
           AND B.RECEIPT_NO = V_S_IMPORT_NO;

        --更新预到货通知单明细 验收数量 通过箱号、商品编码、款号确定验收单和收货单明细一一对应
        UPDATE BILL_IM_IMPORT_DTL B
           SET B.IMPORT_QTY = nvl(B.IMPORT_QTY, 0) +
                              (SELECT C.CHECK_QTY
                                 FROM BILL_IM_CHECK_DTL C
                                WHERE C.BOX_NO = B.BOX_NO
                                  AND C.ITEM_NO = B.ITEM_NO
                                  AND C.SIZE_NO = B.SIZE_NO
                                  AND C.CHECK_NO = I_CHECK_NO)
         WHERE B.BOX_NO = IMCHECKDTL.BOX_NO
           AND B.ITEM_NO = IMCHECKDTL.ITEM_NO
           AND B.SIZE_NO = IMCHECKDTL.SIZE_NO
           AND B.IMPORT_NO = V_IMPORT_NO;
      END IF;

      IF (IMCHECKDTL.PACK_QTY IS NOT NULL) THEN
        V_PACK_QTY := IMCHECKDTL.PACK_QTY;
      END IF;

      --获取商品barcode
      BEGIN
        SELECT BARCODE
          INTO V_BARCODE
          FROM ITEM_BARCODE
         WHERE ITEM_NO = IMCHECKDTL.ITEM_NO
           AND SIZE_NO = IMCHECKDTL.SIZE_NO
           AND PACK_QTY = V_PACK_QTY
           and PACKAGE_ID = 0 ;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          ROLLBACK;
          STROUTMSG := 'N|获取barcode失败!';
          RETURN;
      END;
      --通过存储过程获取箱号

      PKG_LABEL.PROC_GET_CONTAINERNOBASE(I_LOCNO,
                                         'P',
                                         I_OPER_USER,
                                         'D',
                                         1,
                                         '0',
                                         '',
                                         V_LABEL_NO,
                                         V_CONTAINER_NO,
                                         V_SESSION_ID,
                                         STROUTMSG);

      --写入bill_Im_CheckPal
      INSERT INTO BILL_IM_CHECK_PAL
        (LOCNO,
         CHECK_NO,
         S_CHECK_NO,
         CHECK_ROW_ID,
         PRODUCE_DATE,
         EXPIRE_DATE,
         BARCODE,
         SCAN_LABEL_NO,
         STATUS,
         LABEL_NO,
         CONTAINER_NO,
         ITEM_NO,
         PACK_QTY,
         CHECK_QTY,
         OWNER_NO,
         PRICE,
         CREATETM,
         CREATOR,
         SIZE_NO,
         ITEM_TYPE)
      VALUES
        (I_LOCNO,
         IMCHECKDTL.CHECK_NO,
         IMCHECKDTL.CHECK_NO,
         IMCHECKDTL.ROW_ID,
         SYSDATE,
         SYSDATE,
         V_BARCODE,
         V_SCAN_LABEL_NO,
         '13',
         V_LABEL_NO,
         V_CONTAINER_NO,
         IMCHECKDTL.ITEM_NO,
         IMCHECKDTL.PACK_QTY,
         nvl(IMCHECKDTL.CHECK_QTY,0),
         IMCHECKDTL.OWNER_NO,
         IMCHECKDTL.PRICE,
         SYSDATE,
         I_OPER_USER,
         IMCHECKDTL.SIZE_NO,
         '0');
      IF SQL%ROWCOUNT <= 0 THEN
        ROLLBACK;
        STROUTMSG := 'N|写入bill_Im_CheckPal失败!';
        RETURN;
      END IF;

      --查找进货暂存区
       BEGIN
        SELECT C.CELL_NO
          INTO V_CELL_NO
          FROM CM_DEFAREA A
         INNER JOIN CM_DEFCELL C
            ON A.LOCNO = C.LOCNO
           AND A.WARE_NO = C.WARE_NO
           AND A.AREA_NO = C.AREA_NO
         WHERE ROWNUM = 1
           AND A.ATTRIBUTE_TYPE = '1'
           and a.AREA_ATTRIBUTE ='1'
           and c.cell_status ='0'
           and c.check_status ='0'
           AND A.LOCNO = I_LOCNO;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          ROLLBACK;
          STROUTMSG := 'N|获取储位失败!';
          RETURN;
      END;

      --updt by crm 20140110 统一库存记账，写储储位预上量
      --开始
      acc_prepare_data_ext(I_CHECK_NO ,
                          'SC' ,
                          'I' ,
                          I_OPER_USER ,
                          IMCHECKDTL.ROW_ID ,
                           '',
                          I_LOCNO,
                          V_CELL_NO,
                          IMCHECKDTL.ITEM_NO,
                          IMCHECKDTL.Size_No,
                          1,
                          '0',
                          '0',
                          IMCHECKDTL.Owner_No,
                          IMCHECKDTL.Supplier_No,
                          IMCHECKDTL.Box_No,
                          nvl(IMCHECKDTL.CHECK_QTY,0),
                          0,
                          0,
                          '0',
                          '0',
                          '1');
      --回写箱码储位
     update con_box x set x.cell_no=V_CELL_NO where x.locno=I_LOCNO and x.owner_no=IMCHECKDTL.Owner_No and x.box_no=V_SCAN_LABEL_NO
     and x.Box_No is not null;
     acc_apply(I_CHECK_NO,'2','SC','I',1);
     --结束


      if IMCHECKDTL.BOX_NO is not null and IMCHECKDTL.BOX_NO <> 'N' then
          --把验收单对应的收货单中的所有箱子的状态变更为4
          UPDATE CON_BOX
             SET STATUS = '2',
             CELL_NO=V_CELL_NO
           WHERE LOCNO = I_LOCNO
             AND BOX_NO = IMCHECKDTL.BOX_NO;
             -- AND S_IMPORT_NO = V_IMPORT_NO;
          IF SQL%ROWCOUNT <= 0 THEN
            ROLLBACK;
            STROUTMSG := 'N|更新验收单对应的收货单中的所有箱子的状态变失败!';
            RETURN;
          END IF;
      end if;
     V_NCount:=V_NCount+1;
    END LOOP;*/

    if  V_NCount=0 then
            STROUTMSG := 'N|验收单'||I_CHECK_NO||'没有对应的明细数据!';
            RETURN;
    end if;
    --更新预到货通知单状态
    --查询预到货通知单详情中 采购数量和验收数量是否一致或验收数量大于采购数量
    FOR IMD IN (SELECT DISTINCT(IRD.IMPORT_NO) AS TEMP_IMPORT_NO
        FROM BILL_IM_RECEIPT_DTL IRD
             WHERE IRD.LOCNO=I_LOCNO
             AND IRD.OWNER_NO=I_OWNER
             AND IRD.RECEIPT_NO=V_S_IMPORT_NO)LOOP

        UPDATE BILL_IM_IMPORT IIP
          SET IIP.STATUS = CASE
                            WHEN (SELECT COUNT(*)
                                    FROM BILL_IM_IMPORT_DTL DTL
                                   WHERE DTL.LOCNO = IIP.LOCNO
                                     AND DTL.OWNER_NO = IIP.OWNER_NO
                                     AND DTL.IMPORT_NO = IIP.IMPORT_NO
                                     AND NVL(DTL.PO_QTY, 0) <> NVL(DTL.IMPORT_QTY, 0)) = 0 THEN
                             '90'
                            ELSE
                             '30'
                          END
        WHERE IIP.LOCNO = I_LOCNO
          AND IIP.OWNER_NO = I_OWNER
          AND IIP.IMPORT_NO = IMD.TEMP_IMPORT_NO;

        IF SQL%ROWCOUNT <= 0 THEN
          ROLLBACK;
          STROUTMSG := 'N|更新预到货通知单主表状态失败!';
          RETURN;
        END IF;

        --查询通知单的状态
        SELECT STATUS
          INTO V_IM_IMPORTSTATUS_TEMP
          FROM BILL_IM_IMPORT
         WHERE LOCNO = I_LOCNO
           AND OWNER_NO = I_OWNER
           AND IMPORT_NO = IMD.TEMP_IMPORT_NO;

        IF V_IM_IMPORTSTATUS_TEMP = '90' THEN
          V_IM_LOG_MSG:='已结案';
        ELSE
          V_IM_LOG_MSG:='部分验收';
        END IF;

        --状态改变记录状态日志表
        PKG_COMMON_CITY.PROC_BILL_STATUS_LOG(I_LOCNO,
                                             IMD.TEMP_IMPORT_NO,
                                             'IM',
                                             V_IM_IMPORTSTATUS_TEMP,
                                             V_IM_LOG_MSG,
                                             I_OPER_USER,
                                             STROUTMSG);
    END LOOP;


    --调用统一记账过程



    /*--更新预到货通知单状态
    --查询预到货通知单详情中 采购数量和验收数量是否一致
    --1查询明细数量
    SELECT COUNT(1)
      INTO V_IMPORT_DTL_NO
      FROM BILL_IM_IMPORT_DTL
     WHERE LOCNO = I_LOCNO
       AND OWNER_NO = I_OWNER
       AND IMPORT_NO = V_IMPORT_NO;

    --如果明细的数量不为0
    IF (V_IMPORT_DTL_NO > 0) THEN
      V_IMPORT_DTL_NO := 0;
      SELECT COUNT(1)
        INTO V_IMPORT_DTL_NO
        FROM BILL_IM_IMPORT_DTL
       WHERE LOCNO = I_LOCNO
         AND OWNER_NO = I_OWNER
         AND IMPORT_NO = V_IMPORT_NO
         AND nvl(IMPORT_QTY, 0) <> nvl(PO_QTY, 0);
      IF (V_IMPORT_DTL_NO = 0) THEN
        --如果数量相当呢 更新预到货通知单主表状态为结案
        V_IM_IMPORTSTATUS := 90;
        V_IM_LOG_MSG:='已结案';
        --如果不相等 更新预到货通知单状态为部分收货 30部分验收
      ELSE
        V_IM_IMPORTSTATUS := 30;
        V_IM_LOG_MSG:='部分验收';
      END IF;

      --查询通知单的状态
      SELECT STATUS
        INTO V_IM_IMPORTSTATUS_TEMP
        FROM BILL_IM_IMPORT
       WHERE LOCNO = I_LOCNO
         AND OWNER_NO = I_OWNER
         AND IMPORT_NO = V_IMPORT_NO;

      --状态改变记录状态日志表
      IF (V_IM_IMPORTSTATUS_TEMP <> V_IM_IMPORTSTATUS) THEN
        PKG_COMMON_CITY.PROC_BILL_STATUS_LOG(I_LOCNO,V_IMPORT_NO,'IM',V_IM_IMPORTSTATUS,V_IM_LOG_MSG,I_OPER_USER,STROUTMSG);
      END IF;

      --更新状态
      UPDATE BILL_IM_IMPORT
         SET STATUS = V_IM_IMPORTSTATUS
       WHERE LOCNO = I_LOCNO
         AND OWNER_NO = I_OWNER
         AND IMPORT_NO = V_IMPORT_NO;

      IF SQL%ROWCOUNT <= 0 THEN
        ROLLBACK;
        STROUTMSG := 'N|更新预到货通知单主表状态失败!';
        RETURN;
      END IF;
    END IF;*/

    --更新收货单状态
    --查询收货数量和验收数量是否一致，一致更改收货单状态为25-验收完成 不一致更新为 50-异常验货

    SELECT COUNT(1)
      INTO V_RECEIPT__DTL_NO
      FROM BILL_IM_RECEIPT_DTL
     WHERE LOCNO = I_LOCNO
       AND OWNER_NO = I_OWNER
       AND RECEIPT_NO = V_S_IMPORT_NO;
    IF (V_RECEIPT__DTL_NO > 0) THEN
      V_RECEIPT__DTL_NO := 0;
      SELECT COUNT(1)
        INTO V_RECEIPT__DTL_NO
        FROM BILL_IM_RECEIPT_DTL
       WHERE LOCNO = I_LOCNO
         AND OWNER_NO = I_OWNER
         AND RECEIPT_NO = V_S_IMPORT_NO
         AND nvl(RECEIPT_QTY, 0) <> nvl(CHECK_QTY, 0);

      IF (V_RECEIPT__DTL_NO = 0) THEN
        --一致更改收货单状态为25-验收完成
        V_IM_RECEIPTSTATUS := 25;
        --不一致更新为 50-异常验货
      ELSE
        V_IM_RECEIPTSTATUS := 50;
      END IF;
      BEGIN
        UPDATE BILL_IM_RECEIPT R
           SET STATUS = V_IM_RECEIPTSTATUS
         WHERE R.LOCNO = I_LOCNO
           AND R.OWNER_NO = I_OWNER
           AND R.RECEIPT_NO = V_S_IMPORT_NO;
      END;

      IF SQL%ROWCOUNT <= 0 THEN
        ROLLBACK;
        STROUTMSG := 'N|更新收货单状态失败!';
        RETURN;
      END IF;

    END IF;

    --更新验收单状态 25 验收完成
    UPDATE BILL_IM_CHECK
       SET STATUS = '25', AUDITOR = I_OPER_USER, AUDITTM = SYSDATE
     WHERE LOCNO = I_LOCNO
       AND OWNER_NO = I_OWNER
       AND CHECK_NO = I_CHECK_NO;

    IF SQL%ROWCOUNT <= 0 THEN
      ROLLBACK;
      STROUTMSG := 'N|更新收验收单状态失败!';
      RETURN;
    END IF;

  END PROC_IM_CHECK_AUDIT;
END PKG_IM_CHECK_AUDIT_CITY;
/
