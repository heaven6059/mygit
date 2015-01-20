create or replace package PKG_IM_CITY_INSTOCK_RECEIPT is

  /*
    功能：收货单上架定位存储过程
   作者：zuo.sw
   日期：2013-12-3
  */
 procedure Proc_IM_InstockDirect_Receipt(strLocno      in BILL_IM_INSTOCK_DIRECT.LOCNO%type, --仓别
                                         strOwnerNo  in BILL_IM_INSTOCK_DIRECT.Owner_No%type, -- 货主
                                         strReceiptNo in BILL_IM_INSTOCK_DIRECT.SOURCE_NO%type, --收货单号
                                         strWorkerNo   in BILL_IM_INSTOCK_DIRECT.CREATOR%type, --操作人
                                         strResult     out varchar2); --返回 执行结果

 /*
     作者:  zuo.sw
     日期:  2013-12-9
     功能:  上架定位写库存属性和库存/
 */
/* PROCEDURE Proc_add_con_content(STRLOCNO   IN BILL_IM_INSTOCK_DIRECT.LOCNO%TYPE, --仓别
                                 STRSUPPLIERNO IN Bill_Im_Receipt.Supplier_No%TYPE, --供应商
                                 STRBARCODE  IN con_item_info.barcode%TYPE, -- 条码
                                 STRITEMNO  IN con_item_info.item_no%TYPE, -- 商品编码
                                 STRSIZENO  IN  Bill_Im_Receipt_Dtl.Size_No%type, -- 尺码
                                 STRIMPORTNO  IN Bill_Im_Receipt_Dtl.Import_No%TYPE, --预到货通知单号
                                 STRRECEIPTNO  IN Bill_Im_Receipt.Receipt_No%TYPE,--收货单号
                                 STRCELLNO  IN Con_Content.Cell_No%TYPE, -- 储位编码
                                 STRPACKQTY  IN Bill_Im_Receipt_Dtl.Pack_Qty%TYPE, -- 包装数量
                                 STROWNERNO  IN Bill_Im_Receipt.Owner_No%TYPE, -- 委托业主
                                 STRWORKERNO  IN BILL_IM_INSTOCK_DIRECT.CREATOR%TYPE, --操作人
                                 STRITEMTYPE  IN con_item_info.item_type%TYPE, -- 商品类型
                                 STRBOXNO  IN Bill_Im_Receipt_Dtl.Box_No%TYPE, -- 箱号
                                 STRISMAXFLAG   IN VARCHAR2,-- 是否需要判断混款
                                 STRRESULT    OUT VARCHAR2);         */

end PKG_IM_CITY_INSTOCK_RECEIPT;
/
CREATE OR REPLACE PACKAGE BODY PKG_IM_CITY_INSTOCK_RECEIPT IS
 
 
  /*
     作者:  zuo.sw
     日期:  2013-12-3
     功能:  上架定位
  */
  PROCEDURE Proc_IM_InstockDirect_Receipt(STRLOCNO     IN BILL_IM_INSTOCK_DIRECT.LOCNO%TYPE, --仓别
                                          strOwnerNo  in BILL_IM_INSTOCK_DIRECT.Owner_No%type, -- 货主
                                          STRRECEIPTNO IN BILL_IM_INSTOCK_DIRECT.SOURCE_NO%TYPE, --收货单号
                                          STRWORKERNO  IN BILL_IM_INSTOCK_DIRECT.CREATOR%TYPE, --操作人
                                          STRRESULT    OUT VARCHAR2) AS
   
        STRREMAININGQTY  NUMBER(10); --剩余上架数量;
        --STR_CELL_NO      CON_CONTENT.CELL_NO%TYPE; --临时储位变量
        --STR_ITEM_NO      CON_CONTENT.ITEM_NO%TYPE; --商品编码
        --STR_STOCK_NO     CM_DEFCELL.STOCK_NO%TYPE; --通道编码
        STR_QTY          CON_CONTENT.QTY%TYPE; --库存数量
        STR_OUTSTOCK_QTY CON_CONTENT.OUTSTOCK_QTY%TYPE; --预下数量
        STR_INSTOCK_QTY  CON_CONTENT.INSTOCK_QTY%TYPE; --预上数量
        STR_UNUSUAL_QTY  CON_CONTENT.UNUSUAL_QTY%TYPE; --异常数量
        STR_LIMIT_QTY    CS_CELL_PACK_SETTING.LIMIT_QTY%TYPE; --最大存放数量
        STR_MIX_FLAG     CM_DEFCELL.MIX_FLAG%TYPE; --混放货物标识
        STR_I            NUMBER(10); --定位行号
        V_NCONITEMROWID  NUMBER(10); --库存商品属性ID
        V_NCELLID        CON_CONTENT.CELL_ID%TYPE; --库存Cell_ID
        V_TEMP_CELL_NO   VARCHAR2(20); --临时表的唯一单号
        V_RULL_COUNT      NUMBER(10);  -- 是否有上架策略的规则数据
        V_CELL_ALL_COUNT  NUMBER(10);  -- 上架范围的所有储位的数量
        --V_SAME_TYPE_COUNT  NUMBER(10);  -- 同类型的储位的数量
        V_INSTOCK_NUM   NUMBER(10); -- 当前的上架数量
        V_STRCHECKNO   VARCHAR2(20); -- 验收单号
        v_strSCheckNo  VARCHAR2(20); -- 验收批次单号
        v_strLabelNo   con_label.label_no%type;-- 标签号
        v_strContainerNo con_label.container_no%type;--容器号
        v_nSessionId   varchar2(10);-- 返回标签号时，返回sessionID号
        v_exption_cell_no  CON_CONTENT.CELL_NO%TYPE; --异常区的储位变量
        v_exption_cell_no_count  NUMBER(10);  -- 异常区的储位的数量


       STR_Outsdefine    BM_DEFBASE.SDEFINE%TYPE; --参数的字符串值
       STR_Outndefine    BM_DEFBASE.NDEFINE%TYPE; --参数的整型值
       Str_receipt_status   bill_im_receipt.status%type;--收货单的状态
       v_divide_num      number(10); -- 有分货记录的明细的数量
       v_item_type       bill_im_import_dtl.item_type%type;-- 商品属性
       v_item_pack_spec  item_pack.pack_spec%type; -- 商品包装规则
       v_nRowID number:=0;--行号
  BEGIN
    
   
          -- 获取系统配置表的参数设置的值  
          PKG_WMS_BASE.proc_GetBasePara(strLocno,strOwnerNo,'instock_direct','IM','',STR_Outsdefine,STR_Outndefine,STRRESULT);
          if instr(STRRESULT, 'N', 1, 1) = 1 then
              STRRESULT := 'N|未获取到当前仓库的入库上架定位配置信息，请先检查配置！';
              return;
          end if;
          
          -- 按照收货做预约
          if STR_Outndefine = 0 then 
            
                  -- 获取收货单的状态；
                  select s.status into Str_receipt_status  from  bill_im_receipt  s 
                  where s.locno = strLocno
                      and s.owner_no = strOwnerNo
                      and s.receipt_no =  strReceiptNo;
                  
                  --如果状态为新建或已预约，则不能进行上架定位的操作
                  if Str_receipt_status ='10' or Str_receipt_status='30' then
                      STRRESULT := 'N|新建或已预约的单据不能进行上架定位操作！';
                      return;  
                  end if; 
               
                 -- 如果单据状态不为收货完成，则不能做上架定位
                 if Str_receipt_status <> '20' then
                    STRRESULT := 'N|该仓库设置的上架定位方式为按收货定位，当前状态不能进行定位操作！';
                    return;
                 end if;
                 
                 --有分货记录的明细的数量
                 select count(*) into  v_divide_num  from  bill_im_receipt_dtl  rdd 
                 where rdd.locno =strLocno
                    and rdd.owner_no = strOwnerNo
                    and rdd.receipt_no = strReceiptNo
                    and nvl(rdd.divide_qty,0) > 0 ; 
                 
                 -- 如果存在用于分货的明细，则需要提示出来
                 if v_divide_num > 0 then
                    STRRESULT := 'N|该仓库设置的定位方式为按收货定位，却做了分货的操作，请核实配置是否正确！';
                    return; 
                 end if;
          else
                 STRRESULT := 'N|该仓库配置的入库上架定位不支持按收货定位！';
                 return;
          end if;
  
    -- 标记是否有部分数据没有做
    STR_I := 0;
    
    --循环需要定位的商品，按商品汇总的收货单明细
    FOR R_ITEM IN (SELECT DTL.IMPORT_NO,
                          DTL.OWNER_NO,
                          DTL.PACK_QTY,
                          ITM.BRAND_NO,
                          ITM.CATE_NO,
                          DTL.ITEM_NO,
                          DTL.SIZE_NO,
                          --impdtl.item_type,
                          dtl.box_no,
                          BAR.BARCODE,
                          --log20140220 modi by chenhaitao 供应商取商品里的供应商
                          itM.SUPPLIER_NO,
                          --end log20140220
                          SUM(DTL.RECEIPT_QTY) RECEIPT_QTY
                     FROM BILL_IM_RECEIPT_DTL DTL
                    /*INNER JOIN BILL_IM_RECEIPT M
                       ON M.LOCNO = DTL.LOCNO
                      AND M.OWNER_NO = DTL.OWNER_NO
                      AND M.RECEIPT_NO = DTL.RECEIPT_NO*/
                    INNER JOIN ITEM ITM
                       ON ITM.ITEM_NO = DTL.ITEM_NO
                    INNER JOIN ITEM_BARCODE BAR
                       ON BAR.ITEM_NO = DTL.ITEM_NO
                      AND BAR.PACK_QTY = DTL.PACK_QTY
                      AND BAR.SIZE_NO = DTL.SIZE_NO
                      and bar.pack_qty = dtl.pack_qty
                    WHERE DTL.LOCNO = STRLOCNO
                      AND DTL.RECEIPT_NO = STRRECEIPTNO
                    GROUP BY DTL.IMPORT_NO,
                             DTL.OWNER_NO,
                             DTL.PACK_QTY,
                             ITM.BRAND_NO,
                             ITM.CATE_NO,
                             DTL.ITEM_NO,
                             DTL.SIZE_NO,
                             --impdtl.item_type,
                             dtl.box_no,
                             BAR.BARCODE,
                             --log20140220 modi by chenhaitao 供应商取商品里的供应商
                             itM.SUPPLIER_NO
                             --end log20140220
                   ) LOOP

      BEGIN
                -- 查找对应商品属性的数据；
                select IMPdtl.Item_Type into v_item_type  from   Bill_Im_Import_Dtl IMPdtl
                      where IMPdtl.locno = STRLOCNO
                      and IMPdtl.owner_no = R_ITEM.owner_no
                      and IMPdtl.import_no = R_ITEM.import_no
                      and IMPdtl.item_no = R_ITEM.item_no
                      and IMPdtl.size_no = R_ITEM.size_no
                      and IMPdtl.box_no = R_ITEM.box_no
                      and IMPdtl.pack_qty = R_ITEM.pack_qty  
                      and rownum = 1;
                    
                EXCEPTION
                     WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|箱号:' || R_ITEM.Box_No ||'，商品编码'||R_ITEM.Item_No||'未找到对应的商品属性!';
                        RETURN;
      END;             
    
         

      
      
      -- ==============================写商品属性表，获取商品ID  end ==============================
      
       STRREMAININGQTY := R_ITEM.RECEIPT_QTY; --剩余上架数量等于总上架数量
      --STR_I           := 1; --定位行号起始为1
      
      --判断是否有找到对应的上架策略规则
      SELECT count(*) into V_RULL_COUNT
                       FROM CS_INSTOCK_SETTING M
                      INNER JOIN CS_INSTOCK_SETTINGDTL D
                         ON M.LOCNO = D.LOCNO
                        AND M.SETTING_NO = D.SETTING_NO
                      WHERE M.LOCNO = STRLOCNO
                        AND M.STATUS = '1'
                        AND M.SET_TYPE = '0'
                        AND ((M.DETAIL_TYPE = '2' AND
                            D.SELECT_VALUE = R_ITEM.ITEM_NO) OR
                            (M.DETAIL_TYPE = '1' AND
                            D.SELECT_VALUE = R_ITEM.CATE_NO) OR
                            (M.DETAIL_TYPE = '0' AND
                            D.SELECT_VALUE = R_ITEM.BRAND_NO));
      
      if  V_RULL_COUNT = 0  then
          STRRESULT := 'N|品牌'||R_ITEM.BRAND_NO||'-类别'||R_ITEM.CATE_NO||'--商品编码'||R_ITEM.ITEM_NO||'未找到上架策略数据，请先添加!';
          RETURN;
      end if;
      
      
      --优先检查满足的商品、类别、品牌(按此顺序)，循环生效的商品上架策略
      FOR R_RULE IN (SELECT M.SETTING_NO,
                            M.INSTOCK_SCOPE,
                            M.CELL_SORT,
                            M.LIMITED_TYPE,
                            M.SAME_QUALITY_FLAG,
                            M.SAME_ITEM_FLAG,
                            M.EMPTY_CELL_FLAG
                       FROM CS_INSTOCK_SETTING M
                      INNER JOIN CS_INSTOCK_SETTINGDTL D
                         ON M.LOCNO = D.LOCNO
                        AND M.SETTING_NO = D.SETTING_NO
                      WHERE M.LOCNO = STRLOCNO
                        AND M.STATUS = '1'
                        AND M.SET_TYPE = '0'
                        AND ((M.DETAIL_TYPE = '2' AND
                            D.SELECT_VALUE = R_ITEM.ITEM_NO) OR
                            (M.DETAIL_TYPE = '1' AND
                            D.SELECT_VALUE = R_ITEM.CATE_NO) OR
                            (M.DETAIL_TYPE = '0' AND
                            D.SELECT_VALUE = R_ITEM.BRAND_NO))
                      ORDER BY M.DETAIL_TYPE DESC) LOOP
                      
        --如果待上架数量等于零，则跳过本次循环
        IF (STRREMAININGQTY = 0) THEN
          CONTINUE;
        END IF;
      
        --==============================取出所有符合规则的储位开始======================================
        
        -- 取一个唯一编号，用于插入储位临时表时使用
        PKG_WMS_BASE.proc_getsheetno(STRLOCNO,'SJR',V_TEMP_CELL_NO,STRRESULT); --返回 执行结果
      
          --将策略所有满足储位插入临时表
          IF (R_RULE.INSTOCK_SCOPE = '3') THEN
            --判断上架范围 3.储位
            INSERT INTO SYS_CELL_ALL
            SELECT V_TEMP_CELL_NO,CELL.CELL_NO
              FROM CM_DEFCELL CELL
             WHERE CELL.LOCNO = STRLOCNO
               and CELL.Cell_Status = '0'
               AND CELL.CELL_NO IN
                   (SELECT CELL_NO
                      FROM CS_INSTOCK_SETTING M
                     INNER JOIN CS_INSTOCK_SETTINGDTL2 D
                        ON M.LOCNO = D.LOCNO
                       AND M.SETTING_NO = D.SETTING_NO
                     WHERE D.LOCNO = STRLOCNO 
                       AND D.SETTING_NO = R_RULE.SETTING_NO);             
          ELSIF (R_RULE.INSTOCK_SCOPE = '2') THEN
            --判断上架范围 2.通道
           INSERT INTO SYS_CELL_All
            SELECT V_TEMP_CELL_NO,CELL.CELL_NO
              FROM CM_DEFCELL CELL
             WHERE CELL.LOCNO = STRLOCNO
               and CELL.Cell_Status = '0'
               AND CELL.ware_no||''||CELL.area_no||''||CELL.stock_no  IN
                   (SELECT CELL_NO
                      FROM CS_INSTOCK_SETTING M
                     INNER JOIN CS_INSTOCK_SETTINGDTL2 D
                        ON M.LOCNO = D.LOCNO
                       AND M.SETTING_NO = D.SETTING_NO
                     WHERE D.LOCNO = STRLOCNO
                       AND D.SETTING_NO = R_RULE.SETTING_NO) ; 
          ELSIF (R_RULE.INSTOCK_SCOPE = '1') THEN
            --判断上架范围 1.库区
            INSERT INTO SYS_CELL_All
            SELECT V_TEMP_CELL_NO,CELL.CELL_NO
              FROM CM_DEFCELL CELL
             WHERE CELL.LOCNO = STRLOCNO
               and CELL.Cell_Status = '0'
               AND CELL.ware_no||''||CELL.area_no IN
                   (SELECT CELL_NO
                      FROM CS_INSTOCK_SETTING M
                     INNER JOIN CS_INSTOCK_SETTINGDTL2 D
                        ON M.LOCNO = D.LOCNO
                       AND M.SETTING_NO = D.SETTING_NO
                     WHERE D.LOCNO = STRLOCNO 
                       AND D.SETTING_NO = R_RULE.SETTING_NO) ;
          END IF;
          
          -- 查询所有跟商品类型同类型的储位，插入到同类型储位表,只能查询存储区的储位
          BEGIN
                  insert into SYS_CELL_TYPE 
                    select V_TEMP_CELL_NO,c.cell_no  from CM_DEFAREa k 
                    inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                    inner join CM_DEFCELL  c  on   t.locno = c.locno  
                           and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
                    where  k.locno = STRLOCNO
                      and c.item_type = v_item_type
                      and c.cell_no in(select CELL_NO  from  SYS_CELL_ALL where id = V_TEMP_CELL_NO)
                      and c.cell_status = '0'
                      and c.check_status ='0'
                      and k.AREA_USETYPE in('1','6')
                      and k.AREA_ATTRIBUTE ='0' 
                      and k.ATTRIBUTE_TYPE ='0';
              
           EXCEPTION
                WHEN NO_DATA_FOUND THEN -- 如果没有找到合法的数据，则继续下一个循环；
                CONTINUE;
           END;
          
        
          --如果选择了同款商品临近标识
          IF (R_RULE.SAME_ITEM_FLAG = '1') THEN
              INSERT INTO SYS_CELL_STYLE
               SELECT V_TEMP_CELL_NO,CON.CELL_NO
                FROM CON_CONTENT CON
               WHERE CON.LOCNO = STRLOCNO
                 AND CON.ITEM_NO = R_ITEM.ITEM_NO
                 AND con.cell_no in (select CELL_NO  from  SYS_CELL_TYPE 
                   where id = V_TEMP_CELL_NO); 
          END IF;
        
          --如果选择了空储位优先
          IF (r_rule.EMPTY_CELL_FLAG = '1') THEN
            
                -- 如果选择了同款临近
                IF (R_RULE.SAME_ITEM_FLAG = '1') THEN
                  INSERT INTO SYS_CELL_EMPTY
                    select V_TEMP_CELL_NO,tc.CELL_NO From SYS_CELL_TYPE tc  
                    where tc.id = V_TEMP_CELL_NO and  not exists (
                        select t.cell_no from con_content t ,cm_defcell ll
                        where t.locno=ll.locno and t.owner_no=ll.owner_no
                        and t.cell_no=ll.cell_no and ll.CELL_STATUS='0'
                        and t.qty > 0 )
                        and not exists(select * from SYS_CELL_STYLE where id = V_TEMP_CELL_NO);
                       
                else  -- 如果没有选择同款临近
                    INSERT INTO SYS_CELL_EMPTY
                    select V_TEMP_CELL_NO,tc.CELL_NO From SYS_CELL_TYPE  tc where tc.id = V_TEMP_CELL_NO and  not exists (
                        select t.cell_no from con_content t ,cm_defcell ll
                        where t.locno=ll.locno and t.owner_no=ll.owner_no
                        and t.cell_no=ll.cell_no and ll.CELL_STATUS='0'
                        and t.qty > 0 );
                end if;
            
             
             
          END IF;
        
        --==============================取出所有符合规则的储位----end======================================
       
        --==============================按照符合规则的储位写库存--begin======================================
        
        
        -- 储位排序从低到高
        IF (R_RULE.CELL_SORT = '0') THEN
           
            --先遍历同款商品的储位的临时表
            FOR R_STYLE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_STYLE
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO ASC) LOOP   
                          
                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;       
                
                 -- 查询当前储位上的库存数，预上数，预下数，差异数的汇总
                 begin
                    SELECT   
                             SUM(CON.QTY),
                             SUM(CON.OUTSTOCK_QTY),
                             SUM(CON.INSTOCK_QTY),
                             SUM(CON.UNUSUAL_QTY)
                        INTO 
                             STR_QTY,
                             STR_OUTSTOCK_QTY,
                             STR_INSTOCK_QTY,
                             STR_UNUSUAL_QTY
                        FROM CON_CONTENT CON
                    WHERE CON.LOCNO = STRLOCNO
                         AND CON.CELL_NO = R_STYLE_CELL.Cell_No
                         AND CON.STATUS = '0' --盘点锁定标识为未锁定
                         AND CON.FLAG = '0' --冻结库存标识为未冻结
                    GROUP BY CON.CELL_NO;
                  EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                      --库存数量为0
                      STR_QTY          := 0;
                      STR_OUTSTOCK_QTY := 0;
                      STR_INSTOCK_QTY  := 0;
                      STR_UNUSUAL_QTY  := 0;
                  end;
                  
                  -- 判断商品是否有设置包装规格
                  BEGIN
                      SELECT mpk.pack_spec
                        INTO v_item_pack_spec
                        FROM  ITEM_PACK  MPK
                       WHERE MPK.ITEM_NO = R_ITEM.Item_No
                         and mpk.pack_qty = R_ITEM.Pack_Qty
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码:' || R_ITEM.Item_No ||'未设置包装规格，请先设置!';
                        RETURN;
                  END;
                  
                  --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
                  BEGIN
                      SELECT LIMIT_QTY
                        INTO STR_LIMIT_QTY
                        FROM CS_CELL_PACK_SETTING PS
                      where  PS.LOCNO = STRLOCNO
                         and ps.owner_no = strOwnerNo
                         AND PS.AREA_TYPE = (select k.area_type  from CM_DEFAREa k 
                                inner join CM_DEFCELL  c  on   k.locno = c.locno  
                                       and (c.ware_no||''||c.area_no) = (k.ware_no||''||k.area_no)
                                where  c.locno = STRLOCNO
                                and c.cell_no = R_STYLE_CELL.Cell_No and rownum =1 )
                         and ps.pack_spec = v_item_pack_spec
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|储位:' || R_STYLE_CELL.Cell_No ||'对应的库区类型未设置储位包装信息,请核实!';
                        RETURN;
                  END;
                  
                  -- 如果库存数量+预上数量<储位数量限定　(即表示当前的储位未满)
                    IF (STR_QTY + STR_INSTOCK_QTY < STR_LIMIT_QTY) THEN
                      
                       --如果该商品总上架数量小于等于该储位剩余上架数量，则全部商品上架，待上数量为零
                       IF (STRREMAININGQTY <=(STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY)) THEN
                            V_INSTOCK_NUM := STRREMAININGQTY;
                            
                            -- 把需要商家预约的数量清0；
                            STRREMAININGQTY := 0;
                            
                       ELSE --如果该商品上架数量大于该储位剩余上架数量，则将剩余数量填满，待上架数量为上架总数-上架数量
                         
                            V_INSTOCK_NUM := STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY;
                            
                            STRREMAININGQTY := STRREMAININGQTY - (STR_LIMIT_QTY - STR_QTY -STR_INSTOCK_QTY);
                       END IF;
                       
                       
                       -- 查询对应的储位ID             
                       SELECT count(*) into V_CELL_ALL_COUNT
                           FROM CON_CONTENT CON
                         WHERE CON.LOCNO = STRLOCNO
                             AND CON.CELL_NO = R_STYLE_CELL.Cell_No
                             AND CON.ITEM_NO = R_ITEM.Item_No
                             and con.pack_qty = R_ITEM.Pack_Qty
                             and con.barcode=R_ITEM.Barcode
                             and con.supplier_no=R_ITEM.Supplier_No
                             AND CON.STATUS = '0' --盘点锁定标识为未锁定
                             AND CON.FLAG = '0';
                             /*and con.instock_type = 0; -- 上架类型*/ --updt by crm 20140110 新库存表没有这个字段
                             
                       if V_CELL_ALL_COUNT > 0 then
                         -- 查询对应的储位ID             
                         SELECT  max(CON.CELL_ID) into V_NCELLID
                             FROM CON_CONTENT CON
                           WHERE CON.LOCNO = STRLOCNO
                               AND CON.CELL_NO = R_STYLE_CELL.Cell_No
                               AND CON.ITEM_NO = R_ITEM.Item_No
                               and con.barcode=R_ITEM.Barcode
                               and con.pack_qty = R_ITEM.Pack_Qty
                               AND CON.STATUS = '0' --盘点锁定标识为未锁定
                               AND CON.FLAG = '0'
                             /*  and con.instock_type = 0 -- 上架类型*/ --updt by crm 20140110 新库存表没有这个字段
                               and ROWNUM = 1;
                       end if;
                       
                        
                       --如果为空，则新插入一条库存记录,(直接写预上数)
                       IF  V_CELL_ALL_COUNT =0  THEN
                        
          
                       
                          --updt by crm 20140110 调用统计记账过程 ,写储位预上量 
                          --开始
                          --临时表准备数据
                          acc_prepare_data_ext(STRRECEIPTNO ,
                                              'IR' ,
                                              'I' ,
                                              STRWORKERNO ,
                                              v_nRowID ,
                                               '',
                                              STRLOCNO,
                                              R_STYLE_CELL.Cell_No,
                                              R_ITEM.Item_No,
                                              R_ITEM.Size_No,
                                              1,
                                              '0',
                                              '0',
                                              R_ITEM.Owner_No,
                                              R_ITEM.Supplier_No,
                                              R_ITEM.Box_No,
                                              0,
                                              0,
                                              V_INSTOCK_NUM,
                                              '0',
                                              '0',
                                              '1');
                                               acc_apply(STRRECEIPTNO,'2','IR','I',1);
                        --改箱码储位
                        --update con_box x set x.cell_no=R_STYLE_CELL.Cell_No where x.locno=STRLOCNO and x.owner_no=R_ITEM.Owner_No and x.box_no=R_ITEM.Box_No;

                       ELSE -- 如果已经存在记录，则直接更新预上数量
                                
                           --updt by crm 20140111 统一库存记账，增加目的储位库存，扣目的遇上库存
                           --开始      
                           ACC_PREPARE_DATA_EXT(STRRECEIPTNO,'IP','I',STRWORKERNO,v_nRowID,I_LOCNO => STRLOCNO,I_OWNER_NO => strOwnerNo,
                           I_CELL_ID =>V_NCELLID,I_CELL_NO =>R_STYLE_CELL.Cell_No ,I_QTY=>0,I_INSTOCK_QTY =>V_INSTOCK_NUM  );
                          acc_apply(STRRECEIPTNO,'2','IR','I',1);
                          --结束
 
                       END IF ;
                       
                       
                       -- 如果更新的预上数量为0，则给出提示
                       if V_INSTOCK_NUM = 0 then
                           STRRESULT := 'N|储位:' || R_STYLE_CELL.Cell_No ||'，ID:'||V_NCELLID||',商品编码:'||R_ITEM.Item_No||'商品ID:'||V_NCONITEMROWID||'更新库存预上数量时数据非法!';
                           RETURN;
                       end if;
   
                        select t.cell_id into V_NCELLID  from tmp_acc_result t where t.row_id=v_nRowID;
                       
                       
                       -- 插入上架任务表
                       INSERT INTO BILL_IM_INSTOCK_DIRECT
                          (LOCNO,
                           OWNER_NO,
                           LOCATE_TYPE,
                           ROW_ID,
                           LOCATE_ROW_ID,
                           SOURCE_NO,
                           AUTO_LOCATE_FLAG,
                           OPERATE_TYPE,
                           ITEM_NO,
                           ITEM_ID,
                           PACK_QTY,
                           DEST_CELL_NO,
                           DEST_CELL_ID,
                           INSTOCK_QTY,
                           STATUS,
                           CREATOR,
                           CREATETM,
                           EDITOR,
                           EDITTM,
                           INSTOCK_TYPE,
                           LABEL_NO,
                           SIZE_NO)
                        VALUES
                          (STRLOCNO,
                           STROWNERNO,
                           '4',-- 收货定位
                           SEQ_BILL_IM_DIRECT.NEXTVAL,
                           -1,
                           STRRECEIPTNO,--收货单号
                           '1',
                           'B',
                           R_ITEM.Item_No,
                           V_NCONITEMROWID,
                           R_ITEM.Pack_Qty,
                           R_STYLE_CELL.Cell_No,
                           V_NCELLID,
                           V_INSTOCK_NUM,
                           '10',
                           STRWORKERNO,
                           SYSDATE,
                           STRWORKERNO,
                           SYSDATE,
                           '0',
                           R_ITEM.Box_No,
                           R_ITEM.Size_No); --将该储位放满
                        
                        STR_I := STR_I + 1; --行号自动加1
                    
                  END IF;  
                                                  
                 v_nRowID:=v_nRowID+1;
                
            END LOOP;
        
            --再遍历空储位的临时表
            FOR R_EMPTY_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_EMPTY
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO ASC) LOOP   
                          
                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;       
                
                  -- 判断商品是否有设置包装规格
                  BEGIN
                      SELECT mpk.pack_spec
                        INTO v_item_pack_spec
                        FROM  ITEM_PACK  MPK
                       WHERE MPK.ITEM_NO = R_ITEM.Item_No
                         and mpk.pack_qty = R_ITEM.Pack_Qty
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码:' || R_ITEM.Item_No ||'未设置包装规格，请先设置!';
                        RETURN;
                  END;
                  
                  --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
                  BEGIN
                      SELECT LIMIT_QTY
                        INTO STR_LIMIT_QTY
                        FROM CS_CELL_PACK_SETTING PS
                      where  PS.LOCNO = STRLOCNO
                         and ps.owner_no = strOwnerNo
                         AND PS.AREA_TYPE = (select k.area_type  from CM_DEFAREa k 
                                inner join CM_DEFCELL  c  on   k.locno = c.locno  
                                       and (c.ware_no||''||c.area_no) = (k.ware_no||''||k.area_no)
                                where  c.locno = STRLOCNO
                                and c.cell_no = R_EMPTY_CELL.Cell_No and rownum =1 )
                         and ps.pack_spec = v_item_pack_spec
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|储位:' || R_EMPTY_CELL.Cell_No ||'对应的库区类型未设置储位包装信息,请核实!';
                        RETURN;
                  END;
                  
                   -- 如果是空储位商品
                    
                             
                             --如果该商品总上架数量小于等于该储位可以容纳的总数量，则全部商品上架，待上数量为零
                             IF (STRREMAININGQTY <= STR_LIMIT_QTY) THEN
                                  V_INSTOCK_NUM := STRREMAININGQTY; 
                                  
                                  -- 把需要商家预约的数量清0；
                                  STRREMAININGQTY := 0;
                                  
                             ELSE --如果该商品上架数量大于该储位剩余上架数量，则将剩余数量填满，待上架数量为上架总数-上架数量
                               
                                  V_INSTOCK_NUM := STR_LIMIT_QTY ;
                                  
                                  STRREMAININGQTY := STRREMAININGQTY - STR_LIMIT_QTY ;
                             END IF;
                             
                              -- 获取新的储位ID

                              --updt by crm 20140110 调用统计记账过程 ,写储位预上量 
                              --开始
                              acc_prepare_data_ext(STRRECEIPTNO ,
                                              'IR' ,
                                              'I' ,
                                              STRWORKERNO ,
                                              v_nRowID ,
                                               '',
                                              STRLOCNO,
                                              R_EMPTY_CELL.Cell_No,
                                              R_ITEM.Item_No,
                                              R_ITEM.Size_No,
                                              1,
                                              '0',
                                              '0',
                                              R_ITEM.Owner_No,
                                              R_ITEM.Supplier_No,
                                              R_ITEM.Box_No,
                                              0,
                                              0,
                                              V_INSTOCK_NUM,
                                              '0',
                                              '0',
                                              '1');

                             acc_apply(STRRECEIPTNO,'2','IR','I',1);
                             --结束
                        
                             -- 如果更新的预上数量为0，则给出提示
                             if V_INSTOCK_NUM = 0 then
                                 STRRESULT := 'N|储位:' || R_EMPTY_CELL.Cell_No ||'，ID:'||V_NCELLID||',商品编码:'||R_ITEM.Item_No||'商品ID:'||V_NCONITEMROWID||'更新库存预上数量时数据非法!';
                                 RETURN;
                             end if;
                              
                             select t.cell_id into V_NCELLID  from tmp_acc_result t where t.row_id=v_nRowID;   
                             -- 插入上架任务表
                             INSERT INTO BILL_IM_INSTOCK_DIRECT
                                (LOCNO,
                                 OWNER_NO,
                                 LOCATE_TYPE,
                                 ROW_ID,
                                 LOCATE_ROW_ID,
                                 SOURCE_NO,
                                 AUTO_LOCATE_FLAG,
                                 OPERATE_TYPE,
                                 ITEM_NO,
                                 ITEM_ID,
                                 PACK_QTY,
                                 DEST_CELL_NO,
                                 DEST_CELL_ID,
                                 INSTOCK_QTY,
                                 STATUS,
                                 CREATOR,
                                 CREATETM,
                                 EDITOR,
                                 EDITTM,
                                 INSTOCK_TYPE,
                                 LABEL_NO,
                                 SIZE_NO)
                              VALUES
                                (STRLOCNO,
                                 STROWNERNO,
                                 '4',-- 收货定位
                                 SEQ_BILL_IM_DIRECT.NEXTVAL,
                                 -1,
                                 STRRECEIPTNO,--收货单号
                                 '1',
                                 'B',
                                 R_ITEM.Item_No,
                                 V_NCONITEMROWID,
                                 R_ITEM.Pack_Qty,
                                 R_EMPTY_CELL.Cell_No,
                                 V_NCELLID,
                                 V_INSTOCK_NUM,
                                 '10',
                                 STRWORKERNO,
                                 SYSDATE,
                                 STRWORKERNO,
                                 SYSDATE,
                                 '0',
                                 R_ITEM.Box_No,
                                 R_ITEM.Size_No); --将该储位放满
                              
                              STR_I := STR_I + 1; --行号自动加1
                          
                
            END LOOP;
        
            
            --再遍历同类型储位集合中排除同款和空储位的
            FOR R_TYPE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_TYPE
                           where id = V_TEMP_CELL_NO
                           and not exists (select *  from sys_cell_style)
                           and not exists (select *  from sys_cell_empty)
                           ORDER BY CELL_NO ASC) LOOP   
             
                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;       
                
                -- 查询当前储位上的库存数，预上数，预下数，差异数的汇总
                 begin
                    SELECT   
                             SUM(CON.QTY),
                             SUM(CON.OUTSTOCK_QTY),
                             SUM(CON.INSTOCK_QTY),
                             SUM(CON.UNUSUAL_QTY)
                        INTO 
                             STR_QTY,
                             STR_OUTSTOCK_QTY,
                             STR_INSTOCK_QTY,
                             STR_UNUSUAL_QTY
                        FROM CON_CONTENT CON
                    WHERE CON.LOCNO = STRLOCNO
                         AND CON.CELL_NO = R_TYPE_CELL.Cell_No
                         AND CON.STATUS = '0' --盘点锁定标识为未锁定
                         AND CON.FLAG = '0' --冻结库存标识为未冻结
                    GROUP BY CON.CELL_NO;
                  EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                      --库存数量为0
                      STR_QTY          := 0;
                      STR_OUTSTOCK_QTY := 0;
                      STR_INSTOCK_QTY  := 0;
                      STR_UNUSUAL_QTY  := 0;
                  end;
                  
                  -- 判断商品是否有设置包装规格
                  BEGIN
                      SELECT mpk.pack_spec
                        INTO v_item_pack_spec
                        FROM  ITEM_PACK  MPK
                       WHERE MPK.ITEM_NO = R_ITEM.Item_No
                         and mpk.pack_qty = R_ITEM.Pack_Qty
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码:' || R_ITEM.Item_No ||'未设置包装规格，请先设置!';
                        RETURN;
                  END;
                  
                  --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
                  BEGIN
                      SELECT LIMIT_QTY
                        INTO STR_LIMIT_QTY
                        FROM CS_CELL_PACK_SETTING PS
                      where  PS.LOCNO = STRLOCNO
                         and ps.owner_no = strOwnerNo
                         AND PS.AREA_TYPE = (select k.area_type  from CM_DEFAREa k 
                                inner join CM_DEFCELL  c  on   k.locno = c.locno  
                                       and (c.ware_no||''||c.area_no) = (k.ware_no||''||k.area_no)
                                where  c.locno = STRLOCNO
                                and c.cell_no = R_TYPE_CELL.Cell_No and rownum =1 )
                         and ps.pack_spec = v_item_pack_spec
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|储位:' || R_TYPE_CELL.Cell_No ||'对应的库区类型未设置储位包装信息,请核实!';
                        RETURN;
                  END;
                     
                  
                          
                          -- 获取当前储位的是可以可以混款标志  
                          SELECT MIX_FLAG
                                INTO STR_MIX_FLAG
                                FROM CM_DEFCELL
                               WHERE LOCNO = STRLOCNO
                                 AND CELL_NO = R_TYPE_CELL.Cell_No;
                          
                          -- 如果库存数量+预上数量<储位数量限定　(即表示当前的储位未满)
                          IF (STR_QTY + STR_INSTOCK_QTY < STR_LIMIT_QTY) THEN
                            
                             -- 如果可以混款
                             if(STR_MIX_FLAG <> 0) then 
                             --如果该商品总上架数量小于等于该储位剩余上架数量，则全部商品上架，待上数量为零
                             IF (STRREMAININGQTY <=(STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY)) THEN
                                  V_INSTOCK_NUM := STRREMAININGQTY;
                                  
                                  -- 把需要商家预约的数量清0；
                                  STRREMAININGQTY := 0;
                                  
                             ELSE --如果该商品上架数量大于该储位剩余上架数量，则将剩余数量填满，待上架数量为上架总数-上架数量
                               
                                  V_INSTOCK_NUM := STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY;
                                  
                                  STRREMAININGQTY := STRREMAININGQTY - (STR_LIMIT_QTY - STR_QTY -STR_INSTOCK_QTY);
                             END IF;
                             
                             -- 查询对应的储位ID             
                             SELECT count(*) into V_CELL_ALL_COUNT
                                 FROM CON_CONTENT CON
                               WHERE CON.LOCNO = STRLOCNO
                                   AND CON.CELL_NO = R_TYPE_CELL.Cell_No
                                   AND CON.ITEM_NO = R_ITEM.Item_No
                                   and con.barcode=R_ITEM.Barcode
                                   and con.supplier_no=R_ITEM.Supplier_No
                                   and con.pack_qty = R_ITEM.Pack_Qty
                                   AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                   AND CON.FLAG = '0'
                                   /*and con.instock_type = 0*/; -- 上架类型 --updt by crm 20140110 新库存表没有这个字段
                                   
                             if V_CELL_ALL_COUNT > 0 then
                               -- 查询对应的储位ID             
                               SELECT  max(CON.CELL_ID) into V_NCELLID
                                   FROM CON_CONTENT CON
                                 WHERE CON.LOCNO = STRLOCNO
                                     AND CON.CELL_NO = R_TYPE_CELL.Cell_No
                                     AND CON.ITEM_NO = R_ITEM.Item_No
                                     and con.barcode=R_ITEM.Barcode
                                     and con.supplier_no=R_ITEM.Supplier_No
                                     and con.pack_qty = R_ITEM.Pack_Qty
                                     AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                     AND CON.FLAG = '0'
                                     /*and con.instock_type = 0*/ -- 上架类型 --updt by crm 20140110 新库存表没有这个字段
                                     and ROWNUM = 1;
                              end if; 
                               
                              
                             --如果为空，则新插入一条库存记录,(直接写预上数)
                        IF   V_CELL_ALL_COUNT = 0 THEN
                              
                              --updt by crm 20140110 调用统计记账过程 ,写储位预上量 
                              --开始
                              --临时表准备数据
                              acc_prepare_data_ext(STRRECEIPTNO ,
                                                  'IR' ,
                                                  'I' ,
                                                  STRWORKERNO ,
                                                  v_nRowID ,
                                                   '',
                                                  STRLOCNO,
                                                  R_TYPE_CELL.Cell_No,
                                                  R_ITEM.Item_No,
                                                  R_ITEM.Size_No,
                                                  1,
                                                  '0',
                                                  '0',
                                                  R_ITEM.Owner_No,
                                                  R_ITEM.Supplier_No,
                                                  R_ITEM.Box_No,
                                                  0,
                                                  0,
                                                  V_INSTOCK_NUM,
                                                  '0',
                                                  '0',
                                                  '1');
                            --改箱码储位
                            update con_box x set x.cell_no=R_TYPE_CELL.Cell_No where x.locno=STRLOCNO and x.owner_no=R_ITEM.Owner_No and x.box_no=R_ITEM.Box_No;

                       ELSE -- 如果已经存在记录，则直接更新预上数量
                       
                                 acc_prepare_data_ext(STRRECEIPTNO ,
                                              'IR' ,
                                              'I' ,
                                              STRWORKERNO ,
                                              v_nRowID ,
                                               '',
                                              STRLOCNO,
                                              R_TYPE_CELL.Cell_No,
                                              R_ITEM.Item_No,
                                              R_ITEM.Size_No,
                                              1,
                                              '0',
                                              '0',
                                              R_ITEM.Owner_No,
                                              R_ITEM.Supplier_No,
                                              R_ITEM.Box_No,
                                              0,
                                              0,
                                              V_INSTOCK_NUM,
                                              '0',
                                              '0',
                                              '1');
                               
                       END IF ;
                       acc_apply(STRRECEIPTNO,'2','IR','I',1);
                       --结束
                             
                             -- 如果更新的预上数量为0，则给出提示
                             if V_INSTOCK_NUM = 0 then
                                 STRRESULT := 'N|储位:' || R_TYPE_CELL.Cell_No ||'，ID:'||V_NCELLID||',商品编码:'||R_ITEM.Item_No||'商品ID:'||V_NCONITEMROWID||'更新库存预上数量时数据非法!';
                                 RETURN;
                             end if;
                             
/*                            select max(t.cell_id) into V_NCELLID  from con_content t where t.locno=STRLOCNO and t.owner_no=STRWORKERNO
                            and t.cell_no= R_TYPE_CELL.Cell_No and t.item_no=R_ITEM.Item_No
                            and t.barcode=R_ITEM.Barcode and t.supplier_no=R_ITEm.Supplier_No
                            and t.item_type='0' and t.quality='0';*/
                            
                            select t.cell_id into V_NCELLID  from tmp_acc_result t where t.row_id=v_nRowID;
                            
                             -- 插入上架任务表
                             INSERT INTO BILL_IM_INSTOCK_DIRECT
                                (LOCNO,
                                 OWNER_NO,
                                 LOCATE_TYPE,
                                 ROW_ID,
                                 LOCATE_ROW_ID,
                                 SOURCE_NO,
                                 AUTO_LOCATE_FLAG,
                                 OPERATE_TYPE,
                                 ITEM_NO,
                                 ITEM_ID,
                                 PACK_QTY,
                                 DEST_CELL_NO,
                                 DEST_CELL_ID,
                                 INSTOCK_QTY,
                                 STATUS,
                                 CREATOR,
                                 CREATETM,
                                 EDITOR,
                                 EDITTM,
                                 INSTOCK_TYPE,
                                 LABEL_NO,
                                 SIZE_NO)
                              VALUES
                                (STRLOCNO,
                                 STROWNERNO,
                                 '4',-- 收货定位
                                 SEQ_BILL_IM_DIRECT.NEXTVAL,
                                 -1,
                                 STRRECEIPTNO,--收货单号
                                 '1',
                                 'B',
                                 R_ITEM.Item_No,
                                 V_NCONITEMROWID,
                                 R_ITEM.Pack_Qty,
                                 R_TYPE_CELL.Cell_No,
                                 V_NCELLID,
                                 V_INSTOCK_NUM,
                                 '10',
                                 STRWORKERNO,
                                 SYSDATE,
                                 STRWORKERNO,
                                 SYSDATE,
                                 '0',
                                 R_ITEM.Box_No,
                                 R_ITEM.Size_No); --将该储位放满
                              
                              STR_I := STR_I + 1; --行号自动加1
                          
                          end if;
                          
                        END IF;  
 
            END LOOP;
            
        
        ELSE -- 储位排序从高到低
          
              --先遍历同款商品的储位的临时表
            FOR R_STYLE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_STYLE
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP   
                          
                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;       
                
                -- 查询当前储位上的库存数，预上数，预下数，差异数的汇总
                 begin
                    SELECT   
                             SUM(CON.QTY),
                             SUM(CON.OUTSTOCK_QTY),
                             SUM(CON.INSTOCK_QTY),
                             SUM(CON.UNUSUAL_QTY)
                        INTO 
                             STR_QTY,
                             STR_OUTSTOCK_QTY,
                             STR_INSTOCK_QTY,
                             STR_UNUSUAL_QTY
                        FROM CON_CONTENT CON
                    WHERE CON.LOCNO = STRLOCNO
                         AND CON.CELL_NO = R_STYLE_CELL.Cell_No
                         AND CON.STATUS = '0' --盘点锁定标识为未锁定
                         AND CON.FLAG = '0' --冻结库存标识为未冻结
                    GROUP BY CON.CELL_NO;
                  EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                      --库存数量为0
                      STR_QTY          := 0;
                      STR_OUTSTOCK_QTY := 0;
                      STR_INSTOCK_QTY  := 0;
                      STR_UNUSUAL_QTY  := 0;
                  end;
                  
                  -- 判断商品是否有设置包装规格
                  BEGIN
                      SELECT mpk.pack_spec
                        INTO v_item_pack_spec
                        FROM  ITEM_PACK  MPK
                       WHERE MPK.ITEM_NO = R_ITEM.Item_No
                         and mpk.pack_qty = R_ITEM.Pack_Qty
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码:' || R_ITEM.Item_No ||'未设置包装规格，请先设置!';
                        RETURN;
                  END;
                  
                  --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
                  BEGIN
                      SELECT LIMIT_QTY
                        INTO STR_LIMIT_QTY
                        FROM CS_CELL_PACK_SETTING PS
                      where  PS.LOCNO = STRLOCNO
                         and ps.owner_no = strOwnerNo
                         AND PS.AREA_TYPE = (select k.area_type  from CM_DEFAREa k 
                                inner join CM_DEFCELL  c  on   k.locno = c.locno  
                                       and (c.ware_no||''||c.area_no) = (k.ware_no||''||k.area_no)
                                where  c.locno = STRLOCNO
                                and c.cell_no = R_STYLE_CELL.Cell_No and rownum =1 )
                         and ps.pack_spec = v_item_pack_spec
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|储位:' || R_STYLE_CELL.Cell_No ||'对应的库区类型未设置储位包装信息,请核实!';
                        RETURN;
                  END;
                  
                  -- 如果库存数量+预上数量<储位数量限定　(即表示当前的储位未满)
                    IF (STR_QTY + STR_INSTOCK_QTY < STR_LIMIT_QTY) THEN
                      
                       --如果该商品总上架数量小于等于该储位剩余上架数量，则全部商品上架，待上数量为零
                       IF (STRREMAININGQTY <=(STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY)) THEN
                            V_INSTOCK_NUM := STRREMAININGQTY;
                            
                            -- 把需要商家预约的数量清0；
                            STRREMAININGQTY := 0;
                            
                       ELSE --如果该商品上架数量大于该储位剩余上架数量，则将剩余数量填满，待上架数量为上架总数-上架数量
                         
                            V_INSTOCK_NUM := STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY;
                            
                            STRREMAININGQTY := STRREMAININGQTY - (STR_LIMIT_QTY - STR_QTY -STR_INSTOCK_QTY);
                       END IF;
                       
                       
                       -- 查询对应的储位ID             
                       SELECT count(*) into V_CELL_ALL_COUNT
                           FROM CON_CONTENT CON
                         WHERE CON.LOCNO = STRLOCNO
                             AND CON.CELL_NO = R_STYLE_CELL.Cell_No
                             AND CON.ITEM_NO = R_ITEM.Item_No
                             and con.barcode=R_ITEM.Barcode
                             and con.supplier_no=R_ITEM.Supplier_No
                             and con.pack_qty = R_ITEM.Pack_Qty
                             AND CON.STATUS = '0' --盘点锁定标识为未锁定
                             AND CON.FLAG = '0'
                             /*and con.instock_type = 0*/; -- 上架类型  --updt by crm 20140110 新库存表没有这个字段
                             
                       if V_CELL_ALL_COUNT > 0 then
                         -- 查询对应的储位ID             
                         SELECT  max(CON.CELL_ID) into V_NCELLID
                             FROM CON_CONTENT CON
                           WHERE CON.LOCNO = STRLOCNO
                               AND CON.CELL_NO = R_STYLE_CELL.Cell_No
                               AND CON.ITEM_NO = R_ITEM.Item_No
                               and con.barcode=R_ITEM.Barcode
                               and con.supplier_no=R_ITEM.Supplier_No
                               and con.pack_qty = R_ITEM.Pack_Qty
                               AND CON.STATUS = '0' --盘点锁定标识为未锁定
                               AND CON.FLAG = '0'
                               /*and con.instock_type = 0 */-- 上架类型--updt by crm 20140110 新库存表没有这个字段
                               and ROWNUM = 1;
                       end if;
                       
                        
                       --如果为空，则新插入一条库存记录,(直接写预上数)
                       IF  V_CELL_ALL_COUNT =0  THEN
                        
                        -- 获取新的储位ID
    
                       
                     /*   INSERT INTO CON_CONTENT
                          (LOCNO,
                           OWNER_NO,
                           CELL_NO,
                           CELL_ID,
                           CONTAINER_NO,
                           ITEM_NO,
                           ITEM_ID,
                           LABEL_NO,
                           PACK_QTY,
                           QTY,
                           INSTOCK_QTY,
                           HM_MANUAL_FLAG,
                           CREATOR,
                           CREATETM,
                           STATUS,
                           INSTOCK_TYPE)
                        VALUES
                          (STRLOCNO,
                           STROWNERNO,
                           R_STYLE_CELL.Cell_No,
                           V_NCELLID,
                           'N',
                           R_ITEM.Item_No,
                           V_NCONITEMROWID,
                           R_ITEM.Box_No,
                           R_ITEM.Pack_Qty,
                           0,
                           V_INSTOCK_NUM,
                           1,
                           '',
                           SYSDATE,
                           0,
                           0);
                           
                       ELSE -- 如果已经存在记录，则直接更新预上数量
                          UPDATE CON_CONTENT --更新库存表，增加预上数量
                               SET INSTOCK_QTY = INSTOCK_QTY + V_INSTOCK_NUM
                             WHERE LOCNO = STRLOCNO
                               AND CELL_NO = R_STYLE_CELL.Cell_No
                               AND CELL_ID = V_NCELLID;
                       END IF ;*/
                       
                              --updt by crm 20140110 统一库存记账，写储储位预上量
                              --开始
                              acc_prepare_data_ext(STRRECEIPTNO ,
                                                  'IR' ,
                                                  'I' ,
                                                  STRWORKERNO ,
                                                  v_nRowID ,
                                                   '',
                                                  STRLOCNO,
                                                  R_STYLE_CELL.Cell_No,
                                                  R_ITEM.Item_No,
                                                  R_ITEM.Size_No,
                                                  1,
                                                  '0',
                                                  '0',
                                                  R_ITEM.Owner_No,
                                                  R_ITEM.Supplier_No,
                                                  R_ITEM.Box_No,
                                                  0,
                                                  0,
                                                  V_INSTOCK_NUM,
                                                  '0',
                                                  '0',
                                                  '1');
                            --回写箱码头
                            update con_box x set x.cell_no=R_STYLE_CELL.Cell_No where x.locno=STRLOCNO and x.owner_no=R_ITEM.Owner_No and x.box_no=R_ITEM.Box_No;

                       ELSE
                       
                                 acc_prepare_data_ext(STRRECEIPTNO ,
                                              'IR' ,
                                              'I' ,
                                              STRWORKERNO ,
                                              v_nRowID ,
                                               '',
                                              STRLOCNO,
                                              R_STYLE_CELL.Cell_No,
                                              R_ITEM.Item_No,
                                              R_ITEM.Size_No,
                                              1,
                                              '0',
                                              '0',
                                              R_ITEM.Owner_No,
                                              R_ITEM.Supplier_No,
                                              R_ITEM.Box_No,
                                              0,
                                              0,
                                              V_INSTOCK_NUM,
                                              '0',
                                              '0',
                                              '1');
                               
                       END IF ;
                       acc_apply(STRRECEIPTNO,'2','IR','I',1);
                       --结束
                       
                       -- 如果更新的预上数量为0，则给出提示
                       if V_INSTOCK_NUM = 0 then
                           STRRESULT := 'N|储位:' || R_STYLE_CELL.Cell_No ||'，ID:'||V_NCELLID||',商品编码:'||R_ITEM.Item_No||'商品ID:'||V_NCONITEMROWID||'更新库存预上数量时数据非法!';
                           RETURN;
                       end if;
                       
                       select t.cell_id into V_NCELLID  from tmp_acc_result t where t.row_id=v_nRowID;
                       -- 插入上架任务表
                       INSERT INTO BILL_IM_INSTOCK_DIRECT
                          (LOCNO,
                           OWNER_NO,
                           LOCATE_TYPE,
                           ROW_ID,
                           LOCATE_ROW_ID,
                           SOURCE_NO,
                           AUTO_LOCATE_FLAG,
                           OPERATE_TYPE,
                           ITEM_NO,
                           ITEM_ID,
                           PACK_QTY,
                           DEST_CELL_NO,
                           DEST_CELL_ID,
                           INSTOCK_QTY,
                           STATUS,
                           CREATOR,
                           CREATETM,
                           EDITOR,
                           EDITTM,
                           INSTOCK_TYPE,
                           LABEL_NO,
                           SIZE_NO)
                        VALUES
                          (STRLOCNO,
                           STROWNERNO,
                           '4',-- 收货定位
                           SEQ_BILL_IM_DIRECT.NEXTVAL,
                           -1,
                           STRRECEIPTNO,--收货单号
                           '1',
                           'B',
                           R_ITEM.Item_No,
                           V_NCONITEMROWID,
                           R_ITEM.Pack_Qty,
                           R_STYLE_CELL.Cell_No,
                           V_NCELLID,
                           V_INSTOCK_NUM,
                           '10',
                           STRWORKERNO,
                           SYSDATE,
                           STRWORKERNO,
                           SYSDATE,
                           '0',
                           R_ITEM.Box_No,
                           R_ITEM.Size_No); --将该储位放满
                        
                        STR_I := STR_I + 1; --行号自动加1
                    
                  END IF;  
                                
                
            END LOOP;
        
            --再遍历空储位的临时表
            FOR R_EMPTY_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_EMPTY
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP   
                          
                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;       
                
                -- 判断商品是否有设置包装规格
                  BEGIN
                      SELECT mpk.pack_spec
                        INTO v_item_pack_spec
                        FROM  ITEM_PACK  MPK
                       WHERE MPK.ITEM_NO = R_ITEM.Item_No
                         and mpk.pack_qty = R_ITEM.Pack_Qty
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码:' || R_ITEM.Item_No ||'未设置包装规格，请先设置!';
                        RETURN;
                  END;
                  
                  --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
                  BEGIN
                      SELECT LIMIT_QTY
                        INTO STR_LIMIT_QTY
                        FROM CS_CELL_PACK_SETTING PS
                      where  PS.LOCNO = STRLOCNO
                         and ps.owner_no = strOwnerNo
                         AND PS.AREA_TYPE = (select k.area_type  from CM_DEFAREa k 
                                inner join CM_DEFCELL  c  on   k.locno = c.locno  
                                       and (c.ware_no||''||c.area_no) = (k.ware_no||''||k.area_no)
                                where  c.locno = STRLOCNO
                                and c.cell_no = R_EMPTY_CELL.Cell_No and rownum =1 )
                         and ps.pack_spec = v_item_pack_spec
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|储位:' || R_EMPTY_CELL.Cell_No ||'对应的库区类型未设置储位包装信息,请核实!';
                        RETURN;
                  END;
                  
                   -- 如果是空储位商品
                    
                             
                             --如果该商品总上架数量小于等于该储位可以容纳的总数量，则全部商品上架，待上数量为零
                             IF (STRREMAININGQTY <= STR_LIMIT_QTY) THEN
                                  V_INSTOCK_NUM := STRREMAININGQTY; 
                                  
                                  -- 把需要商家预约的数量清0；
                                  STRREMAININGQTY := 0;
                                  
                             ELSE --如果该商品上架数量大于该储位剩余上架数量，则将剩余数量填满，待上架数量为上架总数-上架数量
                               
                                  V_INSTOCK_NUM := STR_LIMIT_QTY ;
                                  
                                  STRREMAININGQTY := STRREMAININGQTY - STR_LIMIT_QTY ;
                             END IF;
                             
                              --updt by crm 20140110 统一库存记账，写储储位预上量
                              --开始
                              acc_prepare_data_ext(STRRECEIPTNO ,
                                                  'IR' ,
                                                  'I' ,
                                                  STRWORKERNO ,
                                                  v_nRowID ,
                                                   '',
                                                  STRLOCNO,
                                                  R_EMPTY_CELL.Cell_No,
                                                  R_ITEM.Item_No,
                                                  R_ITEM.Size_No,
                                                  1,
                                                  '0',
                                                  '0',
                                                  R_ITEM.Owner_No,
                                                  R_ITEM.Supplier_No,
                                                  R_ITEM.Box_No,
                                                  0,
                                                  0,
                                                  V_INSTOCK_NUM,
                                                  '0',
                                                  '0',
                                                  '1');
                            --回写箱码储位
                            update con_box x set x.cell_no=R_EMPTY_CELL.Cell_No where x.locno=STRLOCNO and x.owner_no=R_ITEM.Owner_No and x.box_no=R_ITEM.Box_No;
                             acc_apply(STRRECEIPTNO,'2','IR','I',1);
                            --结束    
                                 
                             -- 如果更新的预上数量为0，则给出提示
                             if V_INSTOCK_NUM = 0 then
                                 STRRESULT := 'N|储位:' || R_EMPTY_CELL.Cell_No ||'，ID:'||V_NCELLID||',商品编码:'||R_ITEM.Item_No||'商品ID:'||V_NCONITEMROWID||'更新库存预上数量时数据非法!';
                                 RETURN;
                             end if;
                             
                             select t.cell_id into V_NCELLID  from tmp_acc_result t where t.row_id=v_nRowID;    
                             -- 插入上架任务表
                             INSERT INTO BILL_IM_INSTOCK_DIRECT
                                (LOCNO,
                                 OWNER_NO,
                                 LOCATE_TYPE,
                                 ROW_ID,
                                 LOCATE_ROW_ID,
                                 SOURCE_NO,
                                 AUTO_LOCATE_FLAG,
                                 OPERATE_TYPE,
                                 ITEM_NO,
                                 ITEM_ID,
                                 PACK_QTY,
                                 DEST_CELL_NO,
                                 DEST_CELL_ID,
                                 INSTOCK_QTY,
                                 STATUS,
                                 CREATOR,
                                 CREATETM,
                                 EDITOR,
                                 EDITTM,
                                 INSTOCK_TYPE,
                                 LABEL_NO,
                                 SIZE_NO)
                              VALUES
                                (STRLOCNO,
                                 STROWNERNO,
                                 '4',-- 收货定位
                                 SEQ_BILL_IM_DIRECT.NEXTVAL,
                                 -1,
                                 STRRECEIPTNO,--收货单号
                                 '1',
                                 'B',
                                 R_ITEM.Item_No,
                                 V_NCONITEMROWID,
                                 R_ITEM.Pack_Qty,
                                 R_EMPTY_CELL.Cell_No,
                                 V_NCELLID,
                                 V_INSTOCK_NUM,
                                 '10',
                                 STRWORKERNO,
                                 SYSDATE,
                                 STRWORKERNO,
                                 SYSDATE,
                                 '0',
                                 R_ITEM.Box_No,
                                 R_ITEM.Size_No); --将该储位放满
                              
                              STR_I := STR_I + 1; --行号自动加1  
             
            END LOOP;
        
            
            --再遍历同类型储位集合中排除同款和空储位的
            FOR R_TYPE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_TYPE
                           where id = V_TEMP_CELL_NO
                           and not exists (select *  from sys_cell_style)
                           and not exists (select *  from sys_cell_empty)
                           ORDER BY CELL_NO desc) LOOP   
             
                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;       
                
                -- 查询当前储位上的库存数，预上数，预下数，差异数的汇总
                 begin
                    SELECT   
                             SUM(CON.QTY),
                             SUM(CON.OUTSTOCK_QTY),
                             SUM(CON.INSTOCK_QTY),
                             SUM(CON.UNUSUAL_QTY)
                        INTO 
                             STR_QTY,
                             STR_OUTSTOCK_QTY,
                             STR_INSTOCK_QTY,
                             STR_UNUSUAL_QTY
                        FROM CON_CONTENT CON
                    WHERE CON.LOCNO = STRLOCNO
                         AND CON.CELL_NO = R_TYPE_CELL.Cell_No
                         AND CON.STATUS = '0' --盘点锁定标识为未锁定
                         AND CON.FLAG = '0' --冻结库存标识为未冻结
                    GROUP BY CON.CELL_NO;
                  EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                      --库存数量为0
                      STR_QTY          := 0;
                      STR_OUTSTOCK_QTY := 0;
                      STR_INSTOCK_QTY  := 0;
                      STR_UNUSUAL_QTY  := 0;
                  end;
                  
                  -- 判断商品是否有设置包装规格
                  BEGIN
                      SELECT mpk.pack_spec
                        INTO v_item_pack_spec
                        FROM  ITEM_PACK  MPK
                       WHERE MPK.ITEM_NO = R_ITEM.Item_No
                         and mpk.pack_qty = R_ITEM.Pack_Qty
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码:' || R_ITEM.Item_No ||'未设置包装规格，请先设置!';
                        RETURN;
                  END;
                  
                  --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
                  BEGIN
                      SELECT LIMIT_QTY
                        INTO STR_LIMIT_QTY
                        FROM CS_CELL_PACK_SETTING PS
                      where  PS.LOCNO = STRLOCNO
                         and ps.owner_no = strOwnerNo
                         AND PS.AREA_TYPE = (select k.area_type  from CM_DEFAREa k 
                                inner join CM_DEFCELL  c  on   k.locno = c.locno  
                                       and (c.ware_no||''||c.area_no) = (k.ware_no||''||k.area_no)
                                where  c.locno = STRLOCNO
                                and c.cell_no = R_TYPE_CELL.Cell_No and rownum =1 )
                         and ps.pack_spec = v_item_pack_spec
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|储位:' || R_TYPE_CELL.Cell_No ||'对应的库区类型未设置储位包装信息,请核实!';
                        RETURN;
                  END;
                     
                  
                          
                          -- 获取当前储位的是可以可以混款标志  
                          SELECT MIX_FLAG
                                INTO STR_MIX_FLAG
                                FROM CM_DEFCELL
                               WHERE LOCNO = STRLOCNO
                                 AND CELL_NO = R_TYPE_CELL.Cell_No;
                          
                          -- 如果库存数量+预上数量<储位数量限定　(即表示当前的储位未满)
                          IF (STR_QTY + STR_INSTOCK_QTY < STR_LIMIT_QTY) THEN
                            
                             -- 如果可以混款
                             if(STR_MIX_FLAG <> 0) then 
                             --如果该商品总上架数量小于等于该储位剩余上架数量，则全部商品上架，待上数量为零
                             IF (STRREMAININGQTY <=(STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY)) THEN
                                  V_INSTOCK_NUM := STRREMAININGQTY;
                                  
                                  -- 把需要商家预约的数量清0；
                                  STRREMAININGQTY := 0;
                                  
                             ELSE --如果该商品上架数量大于该储位剩余上架数量，则将剩余数量填满，待上架数量为上架总数-上架数量
                               
                                  V_INSTOCK_NUM := STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY;
                                  
                                  STRREMAININGQTY := STRREMAININGQTY - (STR_LIMIT_QTY - STR_QTY -STR_INSTOCK_QTY);
                             END IF;
                             
                             -- 查询对应的储位ID             
                             SELECT count(*) into V_CELL_ALL_COUNT
                                 FROM CON_CONTENT CON
                               WHERE CON.LOCNO = STRLOCNO
                                   AND CON.CELL_NO = R_TYPE_CELL.Cell_No
                                   AND CON.ITEM_NO = R_ITEM.Item_No
                                   and con.barcode=R_ITEM.Barcode
                                   and con.supplier_no=R_ITEM.Supplier_No
                                   and con.pack_qty = R_ITEM.Pack_Qty
                                   AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                   AND CON.FLAG = '0';
                                   /*and con.instock_type = 0; -- 上架类型*/ --updt by crm 20140110 新库存表没有这个字段
                                   
                             if V_CELL_ALL_COUNT > 0 then
                               -- 查询对应的储位ID             
                               SELECT  max(CON.CELL_ID) into V_NCELLID
                                   FROM CON_CONTENT CON
                                 WHERE CON.LOCNO = STRLOCNO
                                     AND CON.CELL_NO = R_TYPE_CELL.Cell_No
                                     AND CON.ITEM_NO = R_ITEM.Item_No
                                     and con.barcode=R_ITEM.Barcode
                                     and con.supplier_no=R_ITEM.Supplier_No
                                     and con.pack_qty = R_ITEM.Pack_Qty
                                     AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                     AND CON.FLAG = '0'
                                    /* and con.instock_type = 0*/ -- 上架类型
                                     and ROWNUM = 1;
                              end if; 
                               
                              
                             --如果为空，则新插入一条库存记录,(直接写预上数)
                             IF V_CELL_ALL_COUNT = 0 THEN
                              
                                    --updt by crm 20140110 统一库存记账，写储储位预上量
                                    --开始
                                    acc_prepare_data_ext(STRRECEIPTNO ,
                                                        'IR' ,
                                                        'I' ,
                                                        STRWORKERNO ,
                                                        v_nRowID ,
                                                         '',
                                                        STRLOCNO,
                                                        R_TYPE_CELL.Cell_No,
                                                        R_ITEM.Item_No,
                                                        R_ITEM.Size_No,
                                                        1,
                                                        '0',
                                                        '0',
                                                        R_ITEM.Owner_No,
                                                        R_ITEM.Supplier_No,
                                                        R_ITEM.Box_No,
                                                        0,
                                                        0,
                                                        V_INSTOCK_NUM,
                                                        '0',
                                                        '0',
                                                        '1');
                                  --回写箱码储位
                                  update con_box x set x.cell_no=R_TYPE_CELL.Cell_No where x.locno=STRLOCNO and x.owner_no=R_ITEM.Owner_No and x.box_no=R_ITEM.Box_No;

                             ELSE 
                             
                                       acc_prepare_data_ext(STRRECEIPTNO ,
                                                    'IR' ,
                                                    'I' ,
                                                    STRWORKERNO ,
                                                    v_nRowID ,
                                                     '',
                                                    STRLOCNO,
                                                    R_TYPE_CELL.Cell_No,
                                                    R_ITEM.Item_No,
                                                    R_ITEM.Size_No,
                                                    1,
                                                    '0',
                                                    '0',
                                                    R_ITEM.Owner_No,
                                                    R_ITEM.Supplier_No,
                                                    R_ITEM.Box_No,
                                                    0,
                                                    0,
                                                    V_INSTOCK_NUM,
                                                    '0',
                                                    '0',
                                                    '1');
                                     
                             END IF ;
                             acc_apply(STRRECEIPTNO,'2','IR','I',1);
                             --结束
                             
                             -- 如果更新的预上数量为0，则给出提示
                             if V_INSTOCK_NUM = 0 then
                                 STRRESULT := 'N|储位:' || R_TYPE_CELL.Cell_No ||'，ID:'||V_NCELLID||',商品编码:'||R_ITEM.Item_No||'商品ID:'||V_NCONITEMROWID||'更新库存预上数量时数据非法!';
                                 RETURN;
                             end if;
                             
                            select t.cell_id into V_NCELLID  from tmp_acc_result t where t.row_id=v_nRowID;
                             
                             -- 插入上架任务表
                             INSERT INTO BILL_IM_INSTOCK_DIRECT
                                (LOCNO,
                                 OWNER_NO,
                                 LOCATE_TYPE,
                                 ROW_ID,
                                 LOCATE_ROW_ID,
                                 SOURCE_NO,
                                 AUTO_LOCATE_FLAG,
                                 OPERATE_TYPE,
                                 ITEM_NO,
                                 ITEM_ID,
                                 PACK_QTY,
                                 DEST_CELL_NO,
                                 DEST_CELL_ID,
                                 INSTOCK_QTY,
                                 STATUS,
                                 CREATOR,
                                 CREATETM,
                                 EDITOR,
                                 EDITTM,
                                 INSTOCK_TYPE,
                                 LABEL_NO,
                                 SIZE_NO)
                              VALUES
                                (STRLOCNO,
                                 STROWNERNO,
                                 '4',-- 收货定位
                                 SEQ_BILL_IM_DIRECT.NEXTVAL,
                                 -1,
                                 STRRECEIPTNO,--收货单号
                                 '1',
                                 'B',
                                 R_ITEM.Item_No,
                                 V_NCONITEMROWID,
                                 R_ITEM.Pack_Qty,
                                 R_TYPE_CELL.Cell_No,
                                 V_NCELLID,
                                 V_INSTOCK_NUM,
                                 '10',
                                 STRWORKERNO,
                                 SYSDATE,
                                 STRWORKERNO,
                                 SYSDATE,
                                 '0',
                                 R_ITEM.Box_No,
                                 R_ITEM.Size_No); --将该储位放满
                              
                              STR_I := STR_I + 1; --行号自动加1
                          
                          end if;
                          
                        END IF;  
            END LOOP;
            
        END IF;
        
      END LOOP;
    
      -- 如果策略遍历完后，还有待上架的数量未能成功预约，则随机找一个异常储位进行预约操作；
      if  STRREMAININGQTY > 0 then
        
           select count(*)  into  v_exption_cell_no_count  from CM_DEFAREA k 
                    inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                    inner join CM_DEFCELL  c  on   t.locno = c.locno  
                           and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
                    where  k.locno = STRLOCNO
                      and k.Area_Usetype = '5'
                      --log20140124 modi by chenhaitao 取异常区储位不能带item_type
                      --and k.item_type = v_item_type
                      --end log20140124
                      and c.cell_status = '0'
                      and c.check_status = '0';
                      
          if v_exption_cell_no_count = 0 then 
              STRRESULT := 'N|该仓库下未设置库区用途为异常区的储位，请先设置!';
              RETURN;
          end if;
                      
          select c.cell_no  into  v_exption_cell_no  from CM_DEFAREA k 
                    inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                    inner join CM_DEFCELL  c  on   t.locno = c.locno  
                           and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
                    where  k.locno = STRLOCNO
                      and k.Area_Usetype = '5'
                      --log20140124 modi by chenhaitao 取异常区储位不能带item_type
                      --and k.item_type = v_item_type
                      --end log20140124
                      and c.cell_status = '0'
                      and c.check_status = '0'
                      and rownum =1 ;   
                  
          
          
                    --updt by crm 20140110 统一库存记账，写储储位预上量
                    --开始
                    acc_prepare_data_ext(STRRECEIPTNO ,
                                        'IR' ,
                                        'I' ,
                                        STRWORKERNO ,
                                        v_nRowID ,
                                         '',
                                        STRLOCNO,
                                        v_exption_cell_no,
                                        R_ITEM.Item_No,
                                        R_ITEM.Size_No,
                                        1,
                                        '0',
                                        '0',
                                        R_ITEM.Owner_No,
                                        R_ITEM.Supplier_No,
                                        R_ITEM.Box_No,
                                        0,
                                        0,
                                        STRREMAININGQTY,
                                        '0',
                                        '0',
                                        '1');
                  --回写箱码储位
                  update con_box x set x.cell_no=v_exption_cell_no where x.locno=STRLOCNO and x.owner_no=R_ITEM.Owner_No and x.box_no=R_ITEM.Box_No;
                   acc_apply(STRRECEIPTNO,'2','IR','I',1);
                  --结束    
                 
                 select t.cell_id into V_NCELLID  from tmp_acc_result t where t.row_id=v_nRowID;
                 -- 插入上架任务表
                 INSERT INTO BILL_IM_INSTOCK_DIRECT
                    (LOCNO,
                     OWNER_NO,
                     LOCATE_TYPE,
                     ROW_ID,
                     LOCATE_ROW_ID,
                     SOURCE_NO,
                     AUTO_LOCATE_FLAG,
                     OPERATE_TYPE,
                     ITEM_NO,
                     ITEM_ID,
                     PACK_QTY,
                     DEST_CELL_NO,
                     DEST_CELL_ID,
                     INSTOCK_QTY,
                     STATUS,
                     CREATOR,
                     CREATETM,
                     EDITOR,
                     EDITTM,
                     INSTOCK_TYPE,
                     LABEL_NO,
                     SIZE_NO)
                  VALUES
                    (STRLOCNO,
                     R_ITEM.Owner_No,
                     '4',-- 收货定位
                     SEQ_BILL_IM_DIRECT.NEXTVAL,
                     -1,
                     STRRECEIPTNO,--收货单号
                     '1',
                     'B',
                     R_ITEM.Item_No,
                     V_NCONITEMROWID,
                     R_ITEM.Pack_Qty,
                     v_exption_cell_no,
                     V_NCELLID,
                     STRREMAININGQTY,
                     '10',
                     STRWORKERNO,
                     SYSDATE,
                     STRWORKERNO,
                     SYSDATE,
                     '0',
                     R_ITEM.Box_No,
                     R_ITEM.Size_No); --将该储位放满
                     
                   
                   --待上架数也清0；
                   STRREMAININGQTY := 0;  
                     
                   STR_I := STR_I + 1; --行号自动加1
                     
      end if;

    
    END LOOP;
  
  
   if  STR_I = 0  then
       STRRESULT := 'N|未预约到任何储位信息，请检查上架策略设置!';
       return;
   end if;
  
    -- 自动根据收货单生成验收单
    if  STR_I > 0 then
        
         --产生验收汇总单号
         PKG_WMS_BASE.proc_getsheetno(STRLOCNO,'SC',v_strSCheckNo,STRRESULT);
         if instr(STRRESULT, 'N', 1, 1) = 1 then
              STRRESULT := 'N|获取验收汇总单号失败!';
              return;
         end if;
    
          --产生验收单号
          PKG_WMS_BASE.proc_getsheetno(STRLOCNO, 'IC', V_STRCHECKNO, STRRESULT);
          if instr(STRRESULT, 'N', 1, 1) = 1 then
            STRRESULT := 'N|获取验收单号失败!';
            return;
          end if;
      
          --写验收头档数据
          insert into bill_im_check
            (LOCNO,OWNER_NO,CHECK_NO,s_check_no,S_IMPORT_NO, --收货单号
              SUPPLIER_NO,CHECK_WORKER,
             CHECK_START_DATE,CHECK_END_DATE,STATUS,
             CREATOR,CREATETM,EDITOR,EDITTM,AUDITOR,AUDITTM)
          select irt.locno,irt.owner_no,V_STRCHECKNO,v_strSCheckNo,STRRECEIPTNO,irt.supplier_no,
             STRWORKERNO,sysdate,sysdate,'25',STRWORKERNO,sysdate,STRWORKERNO,sysdate,STRWORKERNO,sysdate
          from  bill_im_receipt irt
          where irt.locno = STRLOCNO
            and irt.receipt_no = STRRECEIPTNO;     
         
          --写验收明细数据   
          for  receipt_detail in (select rdl.locno, rdl.receipt_no,rdl.owner_no,rdl.row_id,rdl.item_no,b.barcode,
                       rdl.pack_qty,rdl.size_no,rdl.box_no,rdl.receipt_qty
                  from bill_im_receipt_dtl  rdl
                  left join item_barcode  b  on  rdl.item_no = b.item_no and rdl.size_no = b.size_no
                 where rdl.locno = STRLOCNO
                   and rdl.receipt_no = STRRECEIPTNO ) loop
                   
               insert into bill_im_check_dtl
                  (LOCNO,OWNER_NO,CHECK_NO,ROW_ID,ITEM_NO,BARCODE,
                   PACK_QTY,SIZE_NO,LOT_NO,PRODUCE_DATE,EXPIRE_DATE,
                   QUALITY,CHECK_QTY,BOX_NO,AUTHORIZED_WORKER,SYS_NO,
                   CHECK_START_DATE,CHECK_END_DATE,STATUS,PO_QTY)
               values(STRLOCNO,receipt_detail.owner_no,V_STRCHECKNO,receipt_detail.row_id,receipt_detail.item_no,receipt_detail.barcode,
                       receipt_detail.pack_qty,receipt_detail.size_no,'N',to_date('1900-01-01', 'yyyy-MM-dd'),to_date('1900-01-01', 'yyyy-MM-dd'),
                       '0',receipt_detail.receipt_qty,receipt_detail.box_no,STRWORKERNO,'N',
                       sysdate,sysdate,'25',receipt_detail.receipt_qty);
               if sql%rowcount=0 then
                    STRRESULT:='N|新增验收明细失败(0行)!';
                    return;
               end if;   
               
               --新取容器号
               --Pkg_Label.proc_get_ContainerNoBase(STRLOCNO,'P',STRWORKERNO,'D',0,'1','',
               --                v_strLabelNo,v_strContainerNo,v_nSessionId,STRRESULT);
               Pkg_Label.proc_get_ContainerNoBase(STRLOCNO,'C',STRWORKERNO,'T',1,'1','',
                               v_strLabelNo,v_strContainerNo,v_nSessionId,STRRESULT);                
                               
               if instr(STRRESULT, 'N', 1, 1) = 1 then
                    return;
               end if;
              
              --写验收板明细
              insert into bill_im_check_pal
                (LOCNO,OWNER_NO,CHECK_NO,s_check_no,CHECK_ROW_ID,ITEM_NO,
                 SIZE_NO,BARCODE,QUALITY,PRODUCE_DATE,EXPIRE_DATE,PACK_QTY,
                 CHECK_QTY,LABEL_NO,STATUS,CONTAINER_NO,SCAN_LABEL_NO,
                 CREATOR,CREATETM,ITEM_TYPE,BUSINESS_TYPE)
              values(
                 STRLOCNO,receipt_detail.owner_no,v_strCheckNo,v_strSCheckNo,receipt_detail.row_id,receipt_detail.item_no,
                 receipt_detail.size_no,receipt_detail.barcode,'0', to_date('1900-01-01', 'yyyy-MM-dd'),to_date('1900-01-01', 'yyyy-MM-dd'),receipt_detail.pack_qty,
                 receipt_detail.receipt_qty,v_strLabelNo,'13', v_strContainerNo,receipt_detail.box_no,
                 STRWORKERNO,sysdate,0,1);
             
               if sql%rowcount=0 then
                  STRRESULT:='N|新增验收板明细失败(0行)!';
                  return;
               end if;  
               
          end loop;
          
          
          -- 更新收货单的状态为已预约；
          update  bill_im_receipt rdd
            set rdd.status = '30'
            where rdd.locno = STRLOCNO 
            and rdd.receipt_no = STRRECEIPTNO;
          if sql%rowcount=0 then
                   STRRESULT:='N|更新收货单为已预约时异常!';
                   return;
          end if; 
           
      end if;
      
      
  
  STRRESULT := 'Y|';
  
  EXCEPTION
    WHEN OTHERS THEN
      STRRESULT := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
  END Proc_IM_InstockDirect_Receipt;
  
  
  /*
     作者:  zuo.sw
     日期:  2013-12-9
     功能:  上架定位写库存属性和库存/
    */
/*  PROCEDURE Proc_add_con_content(STRLOCNO   IN BILL_IM_INSTOCK_DIRECT.LOCNO%TYPE, --仓别
                                 STRSUPPLIERNO IN Bill_Im_Receipt.Supplier_No%TYPE, --供应商
                                 STRBARCODE  IN con_item_info.barcode%TYPE, -- 条码
                                 STRITEMNO  IN con_item_info.item_no%TYPE, -- 商品编码
                                 STRSIZENO  IN  Bill_Im_Receipt_Dtl.Size_No%type, -- 尺码
                                 STRIMPORTNO  IN Bill_Im_Receipt_Dtl.Import_No%TYPE, --预到货通知单号
                                 STRRECEIPTNO  IN Bill_Im_Receipt.Receipt_No%TYPE,--收货单号
                                 STRCELLNO  IN Con_Content.Cell_No%TYPE, -- 储位编码
                                 STRPACKQTY  IN Bill_Im_Receipt_Dtl.Pack_Qty%TYPE, -- 包装数量
                                 STROWNERNO  IN Bill_Im_Receipt.Owner_No%TYPE, -- 委托业主
                                 STRWORKERNO  IN BILL_IM_INSTOCK_DIRECT.CREATOR%TYPE, --操作人
                                 STRITEMTYPE  IN con_item_info.item_type%TYPE, -- 商品类型
                                 STRBOXNO  IN Bill_Im_Receipt_Dtl.Box_No%TYPE, -- 箱号
                                 STRISMAXFLAG   IN VARCHAR2,-- 是否需要判断混款
                                 STRRESULT    OUT VARCHAR2)  as 
                                   
    begin      
    --查询库存商品属性是否有记录（根据预到货通知单号，商品编码，条码，供应商）
            SELECT COUNT(*)
              INTO STR_NCOUNT
              FROM CON_ITEM_INFO INFO
             WHERE INFO.IMPORT_BATCH_NO = STRIMPORTNO
               AND INFO.ITEM_NO = STRITEMNO
               AND INFO.BARCODE = STRBARCODE
               AND INFO.QUALITY = '0'
               AND INFO.SUPPLIER_NO = STRSUPPLIERNO
               AND INFO.ITEM_TYPE =STRITEMTYPE;
           
            --如果没有记录,则取一个最大的item_id
            IF STR_NCOUNT = 0 THEN
              BEGIN
                SELECT NVL(MAX(INFO.ITEM_ID), 0)
                  INTO V_NCONITEMROWID
                  FROM CON_ITEM_INFO INFO
                 WHERE INFO.ITEM_NO = STRITEMNO;
              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  NULL;
              END;
              
              --ItemID向后增加1
              V_NCONITEMROWID := V_NCONITEMROWID + 1;
              
              --锁表
              UPDATE ITEM
                 SET ITEM_STATUS = ITEM_STATUS
               WHERE ITEM_NO = STRITEMNO;
             
              -- 新插入 库存属性记录
              INSERT INTO CON_ITEM_INFO
                (ITEM_NO,
                 ITEM_ID,
                 SUPPLIER_NO,
                 BARCODE,
                 LOT_NO,
                 PRODUCE_DATE,
                 EXPIRE_DATE,
                 QUALITY,
                 IMPORT_BATCH_NO,
                 BATCH_SERIAL_NO,
                 ITEM_TYPE)
              VALUES
                (STRITEMNO,
                 V_NCONITEMROWID,
                 STRSUPPLIERNO,
                 STRBARCODE,
                 'LO1001',
                 SYSDATE,
                 SYSDATE,
                 0,
                 STRIMPORTNO,
                 'N',
                 0);
              IF SQL%ROWCOUNT = 0 THEN
                STRRESULT := 'N|添加商品属性数据异常!';
                return;
              END IF;
            END IF;
            
             --如果有记录,则取出item_id
            IF STR_NCOUNT > 0 THEN
                SELECT item_id
                INTO V_NCONITEMROWID
                FROM CON_ITEM_INFO INFO
               WHERE INFO.IMPORT_BATCH_NO = STRIMPORTNO
                 AND INFO.ITEM_NO = STRITEMNO
                 AND INFO.BARCODE = STRBARCODE
                 AND INFO.QUALITY = '0'
                 AND INFO.SUPPLIER_NO = STRSUPPLIERNO
                 AND INFO.ITEM_TYPE =STRITEMTYPE;
            END IF;
            
            
           -- 查询当前储位上的库存数，预上数，预下数，差异数的汇总
           begin
              SELECT   
                       SUM(CON.QTY),
                       SUM(CON.OUTSTOCK_QTY),
                       SUM(CON.INSTOCK_QTY),
                       SUM(CON.UNUSUAL_QTY)
                  INTO 
                       STR_QTY,
                       STR_OUTSTOCK_QTY,
                       STR_INSTOCK_QTY,
                       STR_UNUSUAL_QTY
                  FROM CON_CONTENT CON
              WHERE CON.LOCNO = STRLOCNO
                   AND CON.CELL_NO = STRCELLNO
                   AND CON.STATUS = '0' --盘点锁定标识为未锁定
                   AND CON.FLAG = '0' --冻结库存标识为未冻结
              GROUP BY CON.CELL_NO;
            EXCEPTION
              WHEN NO_DATA_FOUND THEN
                --库存数量为0
                STR_QTY          := 0;
                STR_OUTSTOCK_QTY := 0;
                STR_INSTOCK_QTY  := 0;
                STR_UNUSUAL_QTY  := 0;
            end;
            
            --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
            BEGIN
                SELECT LIMIT_QTY
                  INTO STR_LIMIT_QTY
                  FROM CS_CELL_PACK_SETTING PS
                 INNER JOIN CM_DEFSTOCK ST
                    ON PS.LOCNO = ST.LOCNO
                   AND PS.AREA_TYPE = ST.AREA_TYPE
                 INNER JOIN ITEM_PACK PK
                    ON PK.PACK_SPEC = PS.PACK_SPEC
                   AND PK.ITEM_NO = STRITEMNO
                 WHERE ST.STOCK_NO =
                       (SELECT STOCK_NO
                          FROM CM_DEFCELL
                         WHERE LOCNO = STRLOCNO
                           AND CELL_NO = STRCELLNO)
                   AND PS.LOCNO = STRLOCNO
                   AND ROWNUM = 1;
              
            EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  STRRESULT := 'N|储位:' || STRCELLNO ||'对应的通道未设置储位包装信息,请核实!';
                  RETURN;
            END;
            
            
            -- 如果是同款商品
            if STRISMAXFLAG = 'S' then
              
              -- 如果库存数量+预上数量<储位数量限定　(即表示当前的储位未满)
              IF (STR_QTY + STR_INSTOCK_QTY < STR_LIMIT_QTY) THEN
                
                 --如果该商品总上架数量小于等于该储位剩余上架数量，则全部商品上架，待上数量为零
                 IF (STRREMAININGQTY <=(STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY)) THEN
                      V_INSTOCK_NUM := STRREMAININGQTY;
                      
                      -- 把需要商家预约的数量清0；
                      STRREMAININGQTY := 0;
                      
                 ELSE --如果该商品上架数量大于该储位剩余上架数量，则将剩余数量填满，待上架数量为上架总数-上架数量
                   
                      V_INSTOCK_NUM := STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY;
                      
                      STRREMAININGQTY := STRREMAININGQTY - (STR_LIMIT_QTY - STR_QTY -STR_INSTOCK_QTY);
                 END IF;
                 
                 
                 -- 查询对应的储位ID             
                 SELECT count(*) into V_CELL_ALL_COUNT
                     FROM CON_CONTENT CON
                   WHERE CON.LOCNO = STRLOCNO
                       AND CON.CELL_NO = STRCELLNO
                       AND CON.ITEM_NO = STRITEMNO
                       AND CON.ITEM_ID = V_NCONITEMROWID
                       AND CON.LABEL_NO = STRBOXNO
                       and con.pack_qty = STRPACKQTY
                       AND CON.STATUS = '0' --盘点锁定标识为未锁定
                       AND CON.FLAG = '0'
                       and con.instock_type = 0; -- 上架类型
                       
                 if V_CELL_ALL_COUNT > 0 then
                   -- 查询对应的储位ID             
                   SELECT  max(CON.CELL_ID) into V_NCELLID
                       FROM CON_CONTENT CON
                     WHERE CON.LOCNO = STRLOCNO
                         AND CON.CELL_NO = STRCELLNO
                         AND CON.ITEM_NO = STRITEMNO
                         AND CON.ITEM_ID = V_NCONITEMROWID
                         AND CON.LABEL_NO = STRBOXNO
                         and con.pack_qty = STRPACKQTY
                         AND CON.STATUS = '0' --盘点锁定标识为未锁定
                         AND CON.FLAG = '0'
                         and con.instock_type = 0 -- 上架类型
                         and ROWNUM = 1;
                 end if;
                 
                  
                 --如果为空，则新插入一条库存记录,(直接写预上数)
                 IF  V_CELL_ALL_COUNT =0  THEN
                  
                  -- 获取新的储位ID
                  SELECT SEQ_CON_CONTENT.NEXTVAL INTO V_NCELLID FROM DUAL;  
                 
                  INSERT INTO CON_CONTENT
                    (LOCNO,
                     OWNER_NO,
                     CELL_NO,
                     CELL_ID,
                     CONTAINER_NO,
                     ITEM_NO,
                     ITEM_ID,
                     LABEL_NO,
                     PACK_QTY,
                     QTY,
                     INSTOCK_QTY,
                     HM_MANUAL_FLAG,
                     CREATOR,
                     CREATETM,
                     STATUS,
                     INSTOCK_TYPE)
                  VALUES
                    (STRLOCNO,
                     STROWNERNO,
                     STRCELLNO,
                     V_NCELLID,
                     '',
                     STRITEMNO,
                     V_NCONITEMROWID,
                     STRBOXNO,
                     STRPACKQTY,
                     0,
                     V_INSTOCK_NUM,
                     1,
                     '',
                     SYSDATE,
                     0,
                     0);
                     
                 ELSE -- 如果已经存在记录，则直接更新预上数量
                    UPDATE CON_CONTENT --更新库存表，增加预上数量
                         SET INSTOCK_QTY = INSTOCK_QTY + V_INSTOCK_NUM
                       WHERE LOCNO = STRLOCNO
                         AND CELL_NO = STRCELLNO
                         AND CELL_ID = V_NCELLID;
                 END IF ;
                 
                 -- 插入上架任务表
                 INSERT INTO BILL_IM_INSTOCK_DIRECT
                    (LOCNO,
                     OWNER_NO,
                     LOCATE_TYPE,
                     ROW_ID,
                     LOCATE_ROW_ID,
                     SOURCE_NO,
                     AUTO_LOCATE_FLAG,
                     OPERATE_TYPE,
                     ITEM_NO,
                     ITEM_ID,
                     PACK_QTY,
                     DEST_CELL_NO,
                     DEST_CELL_ID,
                     INSTOCK_QTY,
                     STATUS,
                     CREATOR,
                     CREATETM,
                     EDITOR,
                     EDITTM,
                     INSTOCK_TYPE,
                     LABEL_NO,
                     SIZE_NO)
                  VALUES
                    (STRLOCNO,
                     STROWNERNO,
                     '4',-- 收货定位
                     SEQ_BILL_IM_DIRECT.NEXTVAL,
                     -1,
                     STRRECEIPTNO,--收货单号
                     '1',
                     'B',
                     STRITEMNO,
                     V_NCONITEMROWID,
                     STRPACKQTY,
                     STRCELLNO,
                     V_NCELLID,
                     V_INSTOCK_NUM,
                     '10',
                     STRWORKERNO,
                     SYSDATE,
                     STRWORKERNO,
                     SYSDATE,
                     '0',
                     STRBOXNO,
                     STRSIZENO); --将该储位放满
                  
                  STR_I := STR_I + 1; --行号自动加1
              
            END IF;  
                                            
         END IF;  
            
            
        -- 如果是空储位商品
        if STRISMAXFLAG = 'P' then
                 
                 --如果该商品总上架数量小于等于该储位可以容纳的总数量，则全部商品上架，待上数量为零
                 IF (STRREMAININGQTY <= STR_LIMIT_QTY) THEN
                      V_INSTOCK_NUM := STRREMAININGQTY; 
                      
                      -- 把需要商家预约的数量清0；
                      STRREMAININGQTY := 0;
                      
                 ELSE --如果该商品上架数量大于该储位剩余上架数量，则将剩余数量填满，待上架数量为上架总数-上架数量
                   
                      V_INSTOCK_NUM := STR_LIMIT_QTY ;
                      
                      STRREMAININGQTY := STRREMAININGQTY - STR_LIMIT_QTY ;
                 END IF;
                 
                  -- 获取新的储位ID
                  SELECT SEQ_CON_CONTENT.NEXTVAL INTO V_NCELLID FROM DUAL;  
                 
                  INSERT INTO CON_CONTENT
                    (LOCNO,
                     OWNER_NO,
                     CELL_NO,
                     CELL_ID,
                     CONTAINER_NO,
                     ITEM_NO,
                     ITEM_ID,
                     LABEL_NO,
                     PACK_QTY,
                     QTY,
                     INSTOCK_QTY,
                     HM_MANUAL_FLAG,
                     CREATOR,
                     CREATETM,
                     STATUS,
                     INSTOCK_TYPE)
                  VALUES
                    (STRLOCNO,
                     STROWNERNO,
                     STRCELLNO,
                     V_NCELLID,
                     '',
                     STRITEMNO,
                     V_NCONITEMROWID,
                     STRBOXNO,
                     STRPACKQTY,
                     0,
                     V_INSTOCK_NUM,
                     1,
                     '',
                     SYSDATE,
                     0,
                     0);
                     
                 -- 插入上架任务表
                 INSERT INTO BILL_IM_INSTOCK_DIRECT
                    (LOCNO,
                     OWNER_NO,
                     LOCATE_TYPE,
                     ROW_ID,
                     LOCATE_ROW_ID,
                     SOURCE_NO,
                     AUTO_LOCATE_FLAG,
                     OPERATE_TYPE,
                     ITEM_NO,
                     ITEM_ID,
                     PACK_QTY,
                     DEST_CELL_NO,
                     DEST_CELL_ID,
                     INSTOCK_QTY,
                     STATUS,
                     CREATOR,
                     CREATETM,
                     EDITOR,
                     EDITTM,
                     INSTOCK_TYPE,
                     LABEL_NO,
                     SIZE_NO)
                  VALUES
                    (STRLOCNO,
                     STROWNERNO,
                     '4',-- 收货定位
                     SEQ_BILL_IM_DIRECT.NEXTVAL,
                     -1,
                     STRRECEIPTNO,--收货单号
                     '1',
                     'B',
                     STRITEMNO,
                     V_NCONITEMROWID,
                     STRPACKQTY,
                     STRCELLNO,
                     V_NCELLID,
                     V_INSTOCK_NUM,
                     '10',
                     STRWORKERNO,
                     SYSDATE,
                     STRWORKERNO,
                     SYSDATE,
                     '0',
                     STRBOXNO,
                     STRSIZENO); --将该储位放满
                  
                  STR_I := STR_I + 1; --行号自动加1
              
                                            
         END IF;  
           
         -- 如果是排除同款商品和空储位以为的商品
        if STRISMAXFLAG = 'O' then
              
              -- 获取当前储位的是可以可以混款标志  
              SELECT MIX_FLAG
                    INTO STR_MIX_FLAG
                    FROM CM_DEFCELL
                   WHERE LOCNO = STRLOCNO
                     AND CELL_NO = STRCELLNO;
              
              -- 如果库存数量+预上数量<储位数量限定　(即表示当前的储位未满)
              IF (STR_QTY + STR_INSTOCK_QTY < STR_LIMIT_QTY) THEN
                
                 -- 如果可以混款
                 if(STR_MIX_FLAG <> 0) then 
                 --如果该商品总上架数量小于等于该储位剩余上架数量，则全部商品上架，待上数量为零
                 IF (STRREMAININGQTY <=(STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY)) THEN
                      V_INSTOCK_NUM := STRREMAININGQTY;
                      
                      -- 把需要商家预约的数量清0；
                      STRREMAININGQTY := 0;
                      
                 ELSE --如果该商品上架数量大于该储位剩余上架数量，则将剩余数量填满，待上架数量为上架总数-上架数量
                   
                      V_INSTOCK_NUM := STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY;
                      
                      STRREMAININGQTY := STRREMAININGQTY - (STR_LIMIT_QTY - STR_QTY -STR_INSTOCK_QTY);
                 END IF;
                 
                 -- 查询对应的储位ID             
                 SELECT count(*) into V_CELL_ALL_COUNT
                     FROM CON_CONTENT CON
                   WHERE CON.LOCNO = STRLOCNO
                       AND CON.CELL_NO = STRCELLNO
                       AND CON.ITEM_NO = STRITEMNO
                       AND CON.ITEM_ID = V_NCONITEMROWID
                       AND CON.LABEL_NO = STRBOXNO
                       and con.pack_qty = STRPACKQTY
                       AND CON.STATUS = '0' --盘点锁定标识为未锁定
                       AND CON.FLAG = '0'
                       and con.instock_type = 0; -- 上架类型
                       
                 if V_CELL_ALL_COUNT > 0 then
                   -- 查询对应的储位ID             
                   SELECT  max(CON.CELL_ID) into V_NCELLID
                       FROM CON_CONTENT CON
                     WHERE CON.LOCNO = STRLOCNO
                         AND CON.CELL_NO = STRCELLNO
                         AND CON.ITEM_NO = STRITEMNO
                         AND CON.ITEM_ID = V_NCONITEMROWID
                         AND CON.LABEL_NO = STRBOXNO
                         and con.pack_qty = STRPACKQTY
                         AND CON.STATUS = '0' --盘点锁定标识为未锁定
                         AND CON.FLAG = '0'
                         and con.instock_type = 0 -- 上架类型
                         and ROWNUM = 1;
                  end if; 
                   
                  
                 --如果为空，则新插入一条库存记录,(直接写预上数)
                 IF V_CELL_ALL_COUNT = 0 THEN
                  
                  -- 获取新的储位ID
                  SELECT SEQ_CON_CONTENT.NEXTVAL INTO V_NCELLID FROM DUAL;  
                 
                  INSERT INTO CON_CONTENT
                    (LOCNO,
                     OWNER_NO,
                     CELL_NO,
                     CELL_ID,
                     CONTAINER_NO,
                     ITEM_NO,
                     ITEM_ID,
                     LABEL_NO,
                     PACK_QTY,
                     QTY,
                     INSTOCK_QTY,
                     HM_MANUAL_FLAG,
                     CREATOR,
                     CREATETM,
                     STATUS,
                     INSTOCK_TYPE)
                  VALUES
                    (STRLOCNO,
                     STROWNERNO,
                     STRCELLNO,
                     V_NCELLID,
                     '',
                     STRITEMNO,
                     V_NCONITEMROWID,
                     STRBOXNO,
                     STRPACKQTY,
                     0,
                     V_INSTOCK_NUM,
                     1,
                     '',
                     SYSDATE,
                     0,
                     0);
                     
                 ELSE -- 如果已经存在记录，则直接更新预上数量
                    UPDATE CON_CONTENT --更新库存表，增加预上数量
                         SET INSTOCK_QTY = INSTOCK_QTY + V_INSTOCK_NUM
                       WHERE LOCNO = STRLOCNO
                         AND CELL_NO = STRCELLNO
                         AND CELL_ID = V_NCELLID;
                 END IF ;
                 
                 -- 插入上架任务表
                 INSERT INTO BILL_IM_INSTOCK_DIRECT
                    (LOCNO,
                     OWNER_NO,
                     LOCATE_TYPE,
                     ROW_ID,
                     LOCATE_ROW_ID,
                     SOURCE_NO,
                     AUTO_LOCATE_FLAG,
                     OPERATE_TYPE,
                     ITEM_NO,
                     ITEM_ID,
                     PACK_QTY,
                     DEST_CELL_NO,
                     DEST_CELL_ID,
                     INSTOCK_QTY,
                     STATUS,
                     CREATOR,
                     CREATETM,
                     EDITOR,
                     EDITTM,
                     INSTOCK_TYPE,
                     LABEL_NO,
                     SIZE_NO)
                  VALUES
                    (STRLOCNO,
                     STROWNERNO,
                     '4',-- 收货定位
                     SEQ_BILL_IM_DIRECT.NEXTVAL,
                     -1,
                     STRRECEIPTNO,--收货单号
                     '1',
                     'B',
                     STRITEMNO,
                     V_NCONITEMROWID,
                     STRPACKQTY,
                     STRCELLNO,
                     V_NCELLID,
                     V_INSTOCK_NUM,
                     '10',
                     STRWORKERNO,
                     SYSDATE,
                     STRWORKERNO,
                     SYSDATE,
                     '0',
                     STRBOXNO,
                     STRSIZENO); --将该储位放满
                  
                  STR_I := STR_I + 1; --行号自动加1
              
              end if;
              
            END IF;  
                                            
         END IF;  
                             
  END Proc_add_con_content;       */     
                              
END PKG_IM_CITY_INSTOCK_RECEIPT;
/
