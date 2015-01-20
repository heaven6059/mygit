create or replace package PKG_IM_CITY_INSTOCK_CHECK is

  /*
    功能：入库上架定位-按验收做定位的存储过程
   作者：zuo.sw
   日期：2013-12-17
  */
 procedure Proc_IM_CITY_INSTOCK_CHECK(strLocno      in BILL_IM_INSTOCK_DIRECT.LOCNO%type, --仓别
                                      strOwnerNo      in BILL_IM_INSTOCK_DIRECT.Owner_No%type, --货主
                                      strCheckNo  in BILL_IM_INSTOCK_DIRECT.SOURCE_NO%type, --验收单号
                                      strWorkerNo   in BILL_IM_INSTOCK_DIRECT.CREATOR%type, --操作人
                                      strResult     out varchar2); --返回 执行结果

 /*
     作者:  zuo.sw
     日期:  2013-12-17
     功能:  上架定位写库存属性和库存/
 */
  PROCEDURE Proc_add_con_content(STR_LOCNO   IN BILL_IM_INSTOCK_DIRECT.LOCNO%TYPE, --仓别
                                 STR_OWNER_NO  IN Bill_Im_Receipt.Owner_No%TYPE, -- 委托业主
                                 STR_check_NO  IN Bill_Im_Receipt.Receipt_No%TYPE,--验收单号
                                 STR_ITEMNO  IN con_content.item_no%TYPE, -- 商品编码
                                 STR_SIZENO  IN  ITEM_BARCODE.SIZE_NO%type, -- 条码
                                 STR_BARCODE  IN  ITEM_BARCODE.BARCODE%type, -- 条码
                                 STR_BRANDNO  IN  BILL_IM_CHECK_DTL.BRAND_NO%type,-- 品牌编码
                                 STR_ITEM_TYPE IN Con_Content.Item_Type%TYPE,-- 商品属性
                                 STR_QUALITY IN Con_Content.Quality%TYPE,-- 品质
                                 STR_ITEM_PACK_SPEC IN item_pack.pack_spec%type, -- 商品包装规则
                                 STR_BOXNO  IN Bill_Im_Receipt_Dtl.Box_No%TYPE, -- 箱号
                                 STR_PACKQTY  IN Bill_Im_Receipt_Dtl.Pack_Qty%TYPE, -- 包装数量
                                 STR_CELLNO  IN Con_Content.Cell_No%TYPE, -- 储位编码
                                 v_z_cell_no in con_content.cell_no%type,-- 暂存区的储位编码
                                 v_z_cell_id in con_content.cell_id%type,--暂存区的储位ID
                                 STR_WORKERNO  IN BILL_IM_INSTOCK_DIRECT.CREATOR%TYPE, --操作人
                                 STR_FLAG     IN   VARCHAR2, -- 标记S,P,O
                                 STRREMAININGQTY in out number,
                                 STR_I  in out number,
                                 STR_SUPPLIER_NO in supplier.supplier_no%type,
                                 STRRESULT  OUT VARCHAR2) ;

end PKG_IM_CITY_INSTOCK_CHECK;
/
CREATE OR REPLACE PACKAGE BODY PKG_IM_CITY_INSTOCK_CHECK IS

    
  /*
    功能：入库上架定位-按验收做定位的存储过程
   作者：zuo.sw
   日期：2013-12-17
  */
  PROCEDURE Proc_IM_CITY_INSTOCK_CHECK(STRLOCNO     IN BILL_IM_INSTOCK_DIRECT.LOCNO%TYPE, --仓别
                                       strOwnerNo   IN BILL_IM_INSTOCK_DIRECT.Owner_No%type, --货主
                                       strCheckNo   IN BILL_IM_INSTOCK_DIRECT.SOURCE_NO%TYPE, --验收单号
                                       STRWORKERNO  IN BILL_IM_INSTOCK_DIRECT.CREATOR%TYPE, --操作人
                                       STRRESULT    OUT VARCHAR2) AS
   
  STRREMAININGQTY  NUMBER(10); --剩余上架数量;
    --STR_CELL_NO      CON_CONTENT.CELL_NO%TYPE; --临时储位变量
    --STR_ITEM_NO      CON_CONTENT.ITEM_NO%TYPE; --商品编码
    --STR_STOCK_NO     CM_DEFCELL.STOCK_NO%TYPE; --通道编码
    --STR_QTY          CON_CONTENT.QTY%TYPE; --库存数量
    --STR_OUTSTOCK_QTY CON_CONTENT.OUTSTOCK_QTY%TYPE; --预下数量
    --STR_INSTOCK_QTY  CON_CONTENT.INSTOCK_QTY%TYPE; --预上数量
    --STR_UNUSUAL_QTY  CON_CONTENT.UNUSUAL_QTY%TYPE; --异常数量
    --STR_LIMIT_QTY    CS_CELL_PACK_SETTING.LIMIT_QTY%TYPE; --最大存放数量
    --STR_MIX_FLAG     CM_DEFCELL.MIX_FLAG%TYPE; --混放货物标识
    STR_I            NUMBER(10); --定位行号
    /*STR_NCOUNT       NUMBER(10); --相同库存商品属性的记录数*/
    V_NCONITEMROWID  NUMBER(10); --库存商品属性ID
    V_NCELLID        CON_CONTENT.CELL_ID%TYPE; --库存Cell_ID
    V_TEMP_CELL_NO   VARCHAR2(20); --临时表的唯一单号
    V_RULL_COUNT      NUMBER(10);  -- 是否有上架策略的规则数据
    --V_CELL_ALL_COUNT  NUMBER(10);  -- 上架范围的所有储位的数量
    -- V_SAME_TYPE_COUNT  NUMBER(10);  -- 同类型的储位的数量
    --V_INSTOCK_NUM   NUMBER(10); -- 当前的上架数量
    -- V_STRCHECKNO   VARCHAR2(20); -- 验收单号
    -- v_strSCheckNo  VARCHAR2(20); -- 验收批次单号
    -- v_strLabelNo   con_label.label_no%type;-- 标签号
    -- v_strContainerNo con_label.container_no%type;--容器号
    -- v_nSessionId   varchar2(10);-- 返回标签号时，返回sessionID号
    v_exption_cell_no  CON_CONTENT.CELL_NO%TYPE; --异常区的储位变量
    v_exption_cell_no_count  NUMBER(10);  -- 异常区的储位的数量
    
    --v_item_type  con_content.item_type%type;  -- 商品类型
    --v_import_no  bill_im_import.import_no%type;  -- 预到货通知单号
    
    v_z_cell_no   CON_CONTENT.Cell_No%TYPE; -- 暂存区的储位编号；
    v_z_cell_id   CON_CONTENT.Cell_Id%TYPE; -- 暂存区的储位ID；
    v_z_is_have   NUMBER(10);  -- 暂存区的储位数据
                                        
    Str_receipt_status   bill_im_check.status%type;--验收单状态
    STR_Outsdefine    BM_DEFBASE.SDEFINE%TYPE; --参数的字符串值
    STR_Outndefine    BM_DEFBASE.NDEFINE%TYPE; --参数的整型值
    
    v_item_pack_spec  item_pack.pack_spec%type; -- 商品包装规则
    v_box_no          bill_im_import_dtl.box_no%type; -- 箱编码
    
    v_devide_num   NUMBER(10);  -- 没有分货的数量
    v_same_num   NUMBER(10);  -- 同款的数量
    v_empty_num   NUMBER(10); -- 空储位的数量
    
  BEGIN
    
    
    -- 获取系统配置表的参数设置的值  
    PKG_WMS_BASE.proc_GetBasePara(strLocno,strOwnerNo,'instock_direct','IM','',STR_Outsdefine,STR_Outndefine,STRRESULT);
    if instr(STRRESULT, 'N', 1, 1) = 1 then
              STRRESULT := 'N|未获取当前仓库的入库上架定位配置信息，请先检查配置！';
              return;
    end if;
    
    -- 按照验收做预约
    if STR_Outndefine = 1 then 
         
        -- 获取验收单的状态；
        select s.status into Str_receipt_status  from  bill_im_check  s 
          where s.locno = strLocno
                  and s.owner_no = strOwnerNo
                  and s.check_no = strCheckNo;
              
        --如果状态为新建或已预约，则不能进行上架定位的操作
        if Str_receipt_status ='10' or Str_receipt_status='30' then
                  STRRESULT := 'N|新建或已预约的验收单据不能进行上架定位操作！';
                  return;  
        end if;  
        
        --如果状态不为验收完成25，则提示不能进行验收定位操作
        if Str_receipt_status <> '25'  then
               STRRESULT := 'N|当前状态下，不能进行按验收定位的操作！';
               return;
        end if;
           
    else         
        STRRESULT := 'N|该仓库配置的入库上架定位不支持验收定位！';
        return;        
    end if;
    
    -- 标记是否有部分数据没有做
    STR_I := 0;
    
    -- 判断是否是分货完成后生成的验收单
    SELECT count(1) into  v_devide_num  FROM BILL_IM_check_DTL DTL 
    WHERE DTL.LOCNO = STRLOCNO
          AND DTL.check_no = strCheckNo
          and dtl.owner_no = strOwnerNo
          and dtl.check_qty > 0
          and nvl(dtl.check_qty,0) - nvl(dtl.DIVIDE_QTY,0) > 0;
    
    if  v_devide_num < 1 then
        STRRESULT := 'N|该单是分货完成后生成的，不可以再做验收定位！';
        return; 
    end if;

    --循环需要定位的商品，按商品汇总的验收单明细（验收单的验收数量大于0，且验收数量大于分货数量的进行定位）
    FOR R_ITEM IN (SELECT DTL.PACK_QTY,
                          ITM.BRAND_NO,
                          ITM.CATE_NO,
                          DTL.ITEM_NO,
                          DTL.SIZE_NO,
                          dtl.box_no,
                          DTL.BARCODE,
                          dtl.item_type,--log20140314 modi by zuo.sw 新增属性和和品质
                          dtl.quality,
                          --log20140220 modi by chenhaitao 供应商取的是商品里的供应商
                          itm.SUPPLIER_NO,
                          --end log20140220
                          (nvl(SUM(DTL.check_qty),0) - nvl(sum(dtl.DIVIDE_QTY),0)) as  DIRECT_QTY
                     FROM BILL_IM_check_DTL DTL
                    INNER JOIN BILL_IM_check M
                       ON M.LOCNO = DTL.LOCNO
                      AND M.OWNER_NO = DTL.OWNER_NO
                      AND M.check_no = DTL.check_no
                    /*INNER join bill_im_receipt  rd
                       on m.s_import_no = rd.receipt_no*/
                    INNER JOIN ITEM ITM
                       ON ITM.ITEM_NO = DTL.ITEM_No
                    WHERE DTL.LOCNO = STRLOCNO
                      AND DTL.check_no = strCheckNo
                      and dtl.owner_no = strOwnerNo
                      and dtl.check_qty > 0
                      and nvl(dtl.check_qty,0) - nvl(dtl.DIVIDE_QTY,0) > 0
                    GROUP BY DTL.PACK_QTY,
                             ITM.BRAND_NO,
                             ITM.CATE_NO,
                             DTL.ITEM_NO,
                             DTL.SIZE_NO,
                             dtl.box_no,
                             DTL.BARCODE,
                             dtl.item_type,
                             dtl.quality,
                             --log20140220 modi by chenhaitao 供应商取的是商品里的供应商
                             itm.SUPPLIER_NO
                         order by dtl.item_no,dtl.size_no,dtl.item_type,dtl.quality
                             --end log20140220
                             ) LOOP
                             
      --剩余上架数量等于总上架数量
      STRREMAININGQTY := R_ITEM.DIRECT_QTY; 
     
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
      
      
      -- 判断商品是否有设置包装规格
      BEGIN
                      SELECT mpk.pack_spec
                        INTO v_item_pack_spec
                        FROM  ITEM_PACK  MPK
                       WHERE MPK.ITEM_NO = R_ITEM.ITEM_NO
                         and mpk.pack_qty = R_ITEM.pack_qty
                         AND ROWNUM = 1;
                    
       EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码:' || R_ITEM.ITEM_NO ||'未设置包装规格，请先设置!';
                        RETURN;
       END;
      
      
       -- 如果箱号为空，则说明是新增加的串款的商品明细，则找一个同款商品的商品类型，预到货通知单号
     -- if  R_ITEM.Box_No is null  or R_ITEM.Box_No ='N' then  --log  20140314  modify zuo.sw  一箱多单和串款串码的需求上线后，箱号字段一定有值，无需再进行此判断
              /*-- 箱子编码赋值
              v_box_no := 'N';  
      
              select md.item_type into v_item_type 
              from  bill_im_check k 
                inner join  bill_im_receipt_dtl  rd  on  rd.RECEIPT_NO = k.s_import_no
                inner join  bill_im_import_dtl  md  on rd.import_no = md.import_no
              where k.check_no = strCheckNo
                and md.item_no = R_ITEM.Item_No
                and md.pack_qty =  R_ITEM.Pack_Qty
                and rownum =1 ;
                
             
                
                --查询暂存区的库存数据
                select count(*) into  v_z_is_have from  CON_CONTENT  zc 
                where zc.STATUS = '0' --盘点锁定标识为未锁定
                   AND zc.FLAG = '0' --冻结库存标识为未冻结
                   and zc.item_no = R_ITEM.Item_No
                   and zc.barcode=R_ITEM.Barcode
                   and zc.supplier_no=R_ITEM.Supplier_No
                   and zc.pack_qty = R_ITEM.Pack_Qty
                   and zc.locno = STRLOCNO
                   and zc.owner_no = strOwnerNo
                   AND ZC.QTY >= nvl(STRREMAININGQTY,0) + nvl(zc.outstock_qty,0)
                   and zc.cell_no in (select c.cell_no  from CM_DEFAREA k 
                              inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                              inner join CM_DEFCELL  c  on   t.locno = c.locno  
                                     and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
                              where k.locno = STRLOCNO
                                and k.AREA_ATTRIBUTE = 1-- 暂存区
                                and k.ATTRIBUTE_TYPE = 1 --进货
                                and c.cell_status = '0'
                                and c.check_status = '0');
                if v_z_is_have = 0 then
                    STRRESULT := 'N|商品编码：'||R_ITEM.Item_No||'，条码：'||R_ITEM.Barcode||'，供应商：'||R_ITEM.Supplier_No||'未找到对应的进货暂存区数据!';
                    return;
                end if;
                
                -- 查询对应的暂存区的储位ID和储位编码
                select  zc.cell_no,zc.cell_id into v_z_cell_no,v_z_cell_id  from  CON_CONTENT  zc 
                where zc.STATUS = '0' --盘点锁定标识为未锁定
                   AND zc.FLAG = '0' --冻结库存标识为未冻结
                   and zc.item_no = R_ITEM.Item_No
                   and zc.barcode=R_ITEM.Barcode
                   and zc.supplier_no=R_ITEM.Supplier_No
                   and zc.pack_qty = R_ITEM.Pack_Qty
                   and zc.locno = STRLOCNO
                   and zc.owner_no = STROWNERNO
                   AND ZC.QTY >= nvl(STRREMAININGQTY,0) + nvl(zc.outstock_qty,0)
                   and zc.cell_no in (select c.cell_no  from CM_DEFAREA k 
                              inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                              inner join CM_DEFCELL  c  on   t.locno = c.locno  
                                     and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
                              where k.locno = STRLOCNO
                                and k.AREA_ATTRIBUTE = 1-- 暂存区
                                and k.ATTRIBUTE_TYPE = 1 --进货
                                and c.cell_status = '0'
                                and c.check_status = '0')
                    and rownum = 1;   */
                    
     -- else -- 如果箱号不为空，则可以找到对应的商品属性和预到货通知单号    
              
              -- 箱子编码赋值
              v_box_no := R_ITEM.Box_No;      
          
/*              select md.item_type,md.import_no into v_item_type,v_import_no 
              from  bill_im_check k 
                inner join  bill_im_receipt_dtl  rd  on  rd.receipt_no = k.s_import_no
                inner join  bill_im_import_dtl  md  on md.import_no = rd.import_no
              where k.check_no = strCheckNo
                and md.item_no = R_ITEM.Item_No
                and md.size_no = R_ITEM.Size_No
                and md.box_no = R_ITEM.Box_No
                and md.pack_qty =  R_ITEM.Pack_Qty
                and rownum =1 ;*/
                
            
                
                --查询暂存区的库存数据
/*                select count(*) into  v_z_is_have from  CON_CONTENT  zc 
                where zc.STATUS = '0' --盘点锁定标识为未锁定
                   AND zc.FLAG = '0' --冻结库存标识为未冻结
                   and zc.item_no = R_ITEM.Item_No
                   and zc.barcode=R_ITEM.Barcode
                   and zc.supplier_no=R_ITEM.Supplier_No
                   and zc.pack_qty = R_ITEM.Pack_Qty
                   and zc.locno = STRLOCNO
                   and zc.owner_no = strOwnerNo
                  -- log  20140314  modify zuo.sw   验收单添加属性和品质   begin
                   and zc.item_type = R_ITEM.item_type 
                   and zc.quality = R_ITEM.quality
                   -- log  20140314  modify zuo.sw  验收单添加属性和品质  end
                   AND ZC.QTY >= nvl(STRREMAININGQTY,0) + nvl(zc.outstock_qty,0)
                   and zc.cell_no in (select c.cell_no  from CM_DEFAREA k 
                              inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                              inner join CM_DEFCELL  c  on   t.locno = c.locno  
                                     and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
                              where k.locno = STRLOCNO
                                and k.AREA_ATTRIBUTE = 1-- 暂存区
                                and k.ATTRIBUTE_TYPE = 1 --进货
                                and c.cell_status = '0'
                                and c.check_status = '0');
                if v_z_is_have = 0 then
                    STRRESULT := 'N|商品编码：'||R_ITEM.Item_No||'，条码：'||R_ITEM.Barcode||'，属性:'||R_ITEM.item_type||',品质:'||R_ITEM.quality||',供应商：'||R_ITEM.Supplier_No||'未找到对应的进货暂存区数据，请检查验收记账!';
                    return;
                end if;
                */
                -- 查询对应的暂存区的储位ID和储位编码
              begin 
                select zc.cell_no,zc.cell_id into v_z_cell_no,v_z_cell_id  from  CON_CONTENT  zc 
                where zc.STATUS = '0' --盘点锁定标识为未锁定
                   AND zc.FLAG = '0' --冻结库存标识为未冻结
                   and zc.item_no = R_ITEM.Item_No
                   and zc.barcode=R_ITEM.Barcode
                   and zc.supplier_no=R_ITEM.Supplier_No
                   and zc.pack_qty = R_ITEM.Pack_Qty
                   and zc.locno = STRLOCNO
                   and zc.owner_no = STROWNERNO
                    -- log  20140314  modify zuo.sw   验收单添加属性和品质   begin
                   and zc.item_type = R_ITEM.item_type 
                   and zc.quality = R_ITEM.quality
                   -- log  20140314  modify zuo.sw  验收单添加属性和品质  end
                   AND ZC.QTY >= nvl(STRREMAININGQTY,0) + nvl(zc.outstock_qty,0)
                   and zc.cell_no in (select c.cell_no  from CM_DEFAREA k 
                              -- 性能优化-直接通过编码关联 add by zuo.sw 2014-05-08
                              inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no  = t.ware_no and   k.area_no =t.area_no
                              inner join CM_DEFCELL  c  on   t.locno = c.locno  
                                     and c.ware_no =  t.ware_no  and  c.area_no = t.area_no  and c.stock_no = t.stock_no
                              where k.locno = STRLOCNO
                                and k.AREA_ATTRIBUTE = 1-- 暂存区
                                and k.ATTRIBUTE_TYPE = 1 --进货
                                and c.cell_status = '0'
                                and c.check_status = '0')
                    and rownum = 1;   
               EXCEPTION
                     WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码：'||R_ITEM.Item_No||'，条码：'||R_ITEM.Barcode||'，属性:'||R_ITEM.item_type||',品质:'||R_ITEM.quality||',供应商：'||R_ITEM.Supplier_No||'未找到对应的进货暂存区数据，请检查验收记账!';
                        RETURN;
               END;    
                    
     --  end if;
       
        --获取到暂存区的储位ID和储位编码，加上暂存区的预下数量
    /*   update CON_CONTENT  cn
        set cn.outstock_qty = nvl(cn.outstock_qty,0) +  STRREMAININGQTY
       where cn.cell_no = v_z_cell_no 
             and cn.cell_id = v_z_cell_id
             and cn.locno = STRLOCNO
             and cn.outstock_qty >=0
             and cn.qty >= nvl(cn.outstock_qty,0) +  STRREMAININGQTY;
       -- 如果更新不到数据，则提示异常      
       if sql%rowcount=0 then
             STRRESULT:='N|更新暂存区储位：'||v_z_cell_no||'，ID：'||v_z_cell_id||'的预下数量时未更新到数据!';
             return;
       end if;      */
       
     begin
      --updt by crm 20140110 统一库存记账，获取到暂存区的储位ID和储位编码,直接更新掉暂存区储位的预下数量 (加上暂存区的预下数量) 
      --开始
     ACC_PREPARE_DATA_EXT(strCheckNo,'SC','I',STRWORKERNO,1,I_LOCNO => STRLOCNO,I_OWNER_NO =>strOwnerNo,
     I_CELL_ID =>v_z_cell_id,I_CELL_NO =>v_z_cell_no ,I_QTY=>0,I_OUTSTOCK_QTY =>STRREMAININGQTY  );
    --回写箱码储位
   -- update con_box x set x.cell_no=v_z_cell_no where x.locno=STRLOCNO and x.owner_no=strOwnerNo and x.box_no=R_ITEM.Box_No;
    acc_apply(strCheckNo,'2','SC','I',1);
    --结束    
      --结束
     exception when others then
          STRRESULT := 'N|' || SQLERRM ||
          SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
          RETURN;
     end;
       
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
           EXIT;
        END IF;

        --==============================取出所有符合规则的储位开始======================================

        if  STR_I = 0 then
            -- 取一个唯一编号，用于插入储位临时表时使用
            PKG_WMS_BASE.proc_getsheetnoother(STRLOCNO,'SJC',V_TEMP_CELL_NO,STRRESULT); --返回 执行结果
        else 
          
            select  STRLOCNO||''||'SJC'||''||(substr(max(h.id),-11)+1) into  V_TEMP_CELL_NO from
            (select distinct a.id   from  SYS_CELL_ALL   a 
              union all select distinct  t.id   from  SYS_CELL_TYPE   t
              union all select distinct  s.id   from  SYS_CELL_STYLE  s
              union all select distinct  e.id   from  SYS_CELL_EMPTY  e
              union all select distinct o.id  from  SYS_CELL_OTHER  o) h;
            
        end if;

        -- 取一个唯一编号，用于插入储位临时表时使用
        --PKG_WMS_BASE.proc_getsheetnoother(STRLOCNO,'SJC',V_TEMP_CELL_NO,STRRESULT); --返回 执行结果

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
          
     
          -- 查询所有跟当前商品的商品属性和品质相同的储位，插入到同类型储位表
          BEGIN
                  insert into SYS_CELL_TYPE
                    select V_TEMP_CELL_NO,c.cell_no  from CM_DEFAREa k
                    -- 性能优化 add by zuo.sw 2014-05-08 
                    inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no= t.ware_no and k.area_no = t.area_no
                    inner join CM_DEFCELL  c  on   t.locno = c.locno
                           and c.ware_no =  t.ware_no and c.area_no = t.area_no and c.stock_no = t.stock_no
                    where  k.locno = STRLOCNO
                      -- log  20140314  modify zuo.sw   验收单添加属性和品质   begin
                      and c.item_type = R_ITEM.item_type
                      and c.area_quality = R_ITEM.quality
                       -- log  20140314  modify zuo.sw  验收单添加属性和品质  end
                      and c.cell_no in(select CELL_NO  from  SYS_CELL_ALL where id = V_TEMP_CELL_NO)
                      and c.cell_status = '0'
                      and c.check_status ='0'
                      and k.AREA_USETYPE in('1','6') -- 普通存储区，贵重物品区
                      and k.AREA_ATTRIBUTE ='0' -- 作业区
                      and k.ATTRIBUTE_TYPE ='0'; -- 存储区
                      
                      
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
                 and con.status ='0'
                 and con.flag = '0'
                 and con.item_type = R_ITEM.item_type
                 and con.quality = R_ITEM.quality
                -- and con.pack_qty = R_ITEM.pack_qty
                 AND con.cell_no in (select CELL_NO  from  SYS_CELL_TYPE
                   where id = V_TEMP_CELL_NO);
          END IF;
         
  
           
          --如果选择了空储位优先
          IF (r_rule.EMPTY_CELL_FLAG = '1') THEN

            -- 同款的数量
           /*  select  count(*) into v_same_num  from SYS_CELL_STYLE where id = V_TEMP_CELL_NO;
             
                -- 如果选择了同款临近
                IF (R_RULE.SAME_ITEM_FLAG = '1' and  v_same_num > 0) THEN
                  INSERT INTO SYS_CELL_EMPTY
                    select V_TEMP_CELL_NO,tc.CELL_NO From SYS_CELL_TYPE tc
                    where tc.id = V_TEMP_CELL_NO 
                        and  not exists (
                            select t.cell_no from con_content t ,cm_defcell ll
                            where t.locno=ll.locno and t.owner_no=ll.owner_no
                              and t.locno = STRLOCNO
                              and t.cell_no=ll.cell_no 
                              --and ll.CELL_STATUS='0' 
                              and (t.qty > 0 or t.instock_qty >0 or t.outstock_qty >0))
                        and not exists(select * from SYS_CELL_STYLE where id = V_TEMP_CELL_NO);
*/
                /*else  -- 如果没有选择同款临近*/
                    INSERT INTO SYS_CELL_EMPTY
                    select V_TEMP_CELL_NO,tc.CELL_NO From SYS_CELL_TYPE  tc where tc.id = V_TEMP_CELL_NO 
                    and  not exists (
                        select t.cell_no from con_content t ,cm_defcell ll
                        where t.locno=ll.locno and t.owner_no=ll.owner_no
                        and t.locno = STRLOCNO
                        and t.cell_no=ll.cell_no 
                        --and ll.CELL_STATUS='0'
                        and (t.qty > 0 or t.instock_qty >0 or t.outstock_qty >0));
                /*end if;*/


              END IF;
          
          select  count(*) into v_same_num  from SYS_CELL_STYLE where id = V_TEMP_CELL_NO;
          
          select  count(*) into v_empty_num  from SYS_CELL_EMPTY where id = V_TEMP_CELL_NO;
          
          if v_same_num > 0 and v_empty_num < 1 then
                  INSERT INTO SYS_CELL_OTHER
                  SELECT distinct  V_TEMP_CELL_NO,CELL_NO
                           FROM SYS_CELL_TYPE
                           where id = V_TEMP_CELL_NO
                           and not exists (select *  from sys_cell_style  where   id = V_TEMP_CELL_NO );
                          
          
          end  if;
        
          
          
          if  v_empty_num  > 0 and v_same_num < 1 then
                  INSERT INTO SYS_CELL_OTHER
                  SELECT distinct V_TEMP_CELL_NO,CELL_NO
                           FROM SYS_CELL_TYPE
                           where id = V_TEMP_CELL_NO
                           and not exists (select *  from sys_cell_empty  where   id = V_TEMP_CELL_NO );
                          
          end  if;
          
           if  v_empty_num  > 0 and v_same_num > 0 then
                   INSERT INTO SYS_CELL_OTHER
                  SELECT distinct V_TEMP_CELL_NO, CELL_NO
                           FROM SYS_CELL_TYPE
                           where id = V_TEMP_CELL_NO
                           and not exists (select *  from sys_cell_style  where   id = V_TEMP_CELL_NO )
                           and not exists (select *  from sys_cell_empty  where   id = V_TEMP_CELL_NO );
           
          end if;
          
          if  v_empty_num  < 1 and v_same_num < 1 then
                  INSERT INTO SYS_CELL_OTHER
                  SELECT distinct V_TEMP_CELL_NO, CELL_NO
                           FROM SYS_CELL_TYPE
           
                           where id = V_TEMP_CELL_NO;
          end if;
          
        
        --==============================取出所有符合规则的储位----end======================================

        --==============================按照符合规则的储位写库存--begin======================================

        -- 储位排序从低到高
        IF (R_RULE.CELL_SORT = '0') THEN

            --先遍历同款商品的储位的临时表
            FOR R_STYLE_CELL IN (SELECT distinct CELL_NO
                           FROM SYS_CELL_STYLE
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO ASC) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;
               
            
                -- 调用通用的存储过程
                PKG_IM_CITY_INSTOCK_CHECK.Proc_add_con_content(
                STRLOCNO,strOwnerNo,strCheckNo,R_ITEM.ITEM_NO,R_ITEM.Size_No,
                       R_ITEM.BARCODE,R_ITEM.brand_no,R_ITEM.item_type, R_ITEM.quality,v_item_pack_spec,v_box_no,R_ITEM.PACK_QTY,R_STYLE_CELL.Cell_No,
                       v_z_cell_no,v_z_cell_id,STRWORKERNO,'S',STRREMAININGQTY,STR_I,R_ITEM.Supplier_No,STRRESULT);
                
                
                -- 如果返回X，则表示需要走下一个循环
                if instr(STRRESULT, 'X', 1, 1) = 1 then
                   CONTINUE;
                end if;
                
                -- 如果返回N,则返回并给出提示
                if instr(STRRESULT, 'N', 1, 1) = 1 then
                   return;
                end if;
                
               
            END LOOP;


            --再遍历空储位的临时表
            FOR R_EMPTY_CELL IN (SELECT distinct CELL_NO
                           FROM SYS_CELL_EMPTY
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO ASC) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;
                
               -- 调用通用的存储过程
                PKG_IM_CITY_INSTOCK_CHECK.Proc_add_con_content(STRLOCNO,strOwnerNo,strCheckNo,R_ITEM.ITEM_NO,R_ITEM.Size_No,
                       R_ITEM.BARCODE,R_ITEM.brand_no,R_ITEM.item_type,R_ITEM.quality,v_item_pack_spec,v_box_no,R_ITEM.PACK_QTY,R_EMPTY_CELL.Cell_No,v_z_cell_no,v_z_cell_id,STRWORKERNO,'P',STRREMAININGQTY,STR_I,R_ITEM.Supplier_No,STRRESULT);
               -- 如果返回X，则表示需要走下一个循环
               if instr(STRRESULT, 'X', 1, 1) = 1 then
                   CONTINUE;
               end if;
               
               -- 如果返回N,则返回并给出提示
               if instr(STRRESULT, 'N', 1, 1) = 1 then
                   return;
               end if;
               
            END LOOP;


            --再遍历同类型储位集合中排除同款和空储位的
            FOR R_TYPE_CELL IN (SELECT distinct CELL_NO
                           FROM SYS_CELL_OTHER
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO ASC) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
               -- 调用通用的存储过程
                PKG_IM_CITY_INSTOCK_CHECK.Proc_add_con_content(STRLOCNO,strOwnerNo,strCheckNo,R_ITEM.ITEM_NO,R_ITEM.Size_No,
                       R_ITEM.BARCODE,R_ITEM.brand_no,R_ITEM.item_type, R_ITEM.quality,v_item_pack_spec,v_box_no,R_ITEM.PACK_QTY,R_TYPE_CELL.Cell_No,v_z_cell_no,v_z_cell_id,STRWORKERNO,'O',STRREMAININGQTY,STR_I,R_ITEM.Supplier_No,STRRESULT);
                
                -- 如果返回X，则表示需要走下一个循环
                if instr(STRRESULT, 'X', 1, 1) = 1 then
                   CONTINUE;
                end if;
                
                -- 如果返回N,则返回并给出提示
                if instr(STRRESULT, 'N', 1, 1) = 1 then
                   return;
                end if;

            END LOOP;


        ELSE -- 储位排序从高到低

              --先遍历同款商品的储位的临时表
            FOR R_STYLE_CELL IN (SELECT distinct CELL_NO
                           FROM SYS_CELL_STYLE
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_IM_CITY_INSTOCK_CHECK.Proc_add_con_content(STRLOCNO,strOwnerNo,strCheckNo,R_ITEM.ITEM_NO,R_ITEM.Size_No,
                       R_ITEM.BARCODE,R_ITEM.brand_no,R_ITEM.item_type, R_ITEM.quality,v_item_pack_spec,v_box_no,R_ITEM.PACK_QTY,R_STYLE_CELL.Cell_No,v_z_cell_no,v_z_cell_id,STRWORKERNO,'S',STRREMAININGQTY,STR_I,R_ITEM.Supplier_No,STRRESULT);
                
                -- 如果返回X，则表示需要走下一个循环
                if instr(STRRESULT, 'X', 1, 1) = 1 then
                   CONTINUE;
                end if;
                
                -- 如果返回N,则返回并给出提示
                if instr(STRRESULT, 'N', 1, 1) = 1 then
                   return;
                end if;
                
            END LOOP;

            --再遍历空储位的临时表
            FOR R_EMPTY_CELL IN (SELECT distinct CELL_NO
                           FROM SYS_CELL_EMPTY
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_IM_CITY_INSTOCK_CHECK.Proc_add_con_content(STRLOCNO,strOwnerNo,strCheckNo,R_ITEM.ITEM_NO,R_ITEM.Size_No,
                       R_ITEM.barcode,R_ITEM.brand_no,R_ITEM.item_type, R_ITEM.quality,v_item_pack_spec,v_box_no,R_ITEM.PACK_QTY,R_EMPTY_CELL.Cell_No,v_z_cell_no,v_z_cell_id,STRWORKERNO,'P',STRREMAININGQTY,STR_I,R_ITEM.Supplier_No,STRRESULT);
                  
                -- 如果返回X，则表示需要走下一个循环
                if instr(STRRESULT, 'X', 1, 1) = 1 then
                   CONTINUE;
                end if;
                
                -- 如果返回N,则返回并给出提示
                if instr(STRRESULT, 'N', 1, 1) = 1 then
                   return;
                end if;
                
            END LOOP;

          

            --再遍历同类型储位集合中排除同款和空储位的
            FOR R_TYPE_CELL IN (SELECT distinct CELL_NO
                           FROM SYS_CELL_OTHER
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_IM_CITY_INSTOCK_CHECK.Proc_add_con_content(STRLOCNO,strOwnerNo,strCheckNo,R_ITEM.ITEM_NO,R_ITEM.Size_No,
                       R_ITEM.BARCODE,R_ITEM.brand_no,R_ITEM.item_type, R_ITEM.quality,v_item_pack_spec,v_box_no,R_ITEM.PACK_QTY,R_TYPE_CELL.Cell_No,v_z_cell_no,v_z_cell_id,STRWORKERNO,'O',STRREMAININGQTY,STR_I,R_ITEM.Supplier_No,STRRESULT);
              
                -- 如果返回X，则表示需要走下一个循环
                if instr(STRRESULT, 'X', 1, 1) = 1 then
                   CONTINUE;
                end if;
                
                -- 如果返回N,则返回并给出提示
                if instr(STRRESULT, 'N', 1, 1) = 1 then
                   return;
                end if;
                
            END LOOP;

        END IF;

      END LOOP;

      -- 如果策略遍历完后，还有待上架的数量未能成功预约，则随机找一个异常储位进行预约操作；
      if  STRREMAININGQTY > 0 then

           select count(*)  into  v_exption_cell_no_count  from CM_DEFAREA k
                  -- 性能优化 add by zuo.sw 2014-05-08 
                    inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no= t.ware_no and k.area_no = t.area_no
                    inner join CM_DEFCELL  c  on   t.locno = c.locno
                           and c.ware_no =  t.ware_no and c.area_no = t.area_no and c.stock_no = t.stock_no
                    where  k.locno = STRLOCNO
                      and k.Area_Usetype = '5' -- 用途为退货
                      -- 异常区加上存储作业区 add by 2014-05-07
                      and k.AREA_ATTRIBUTE ='0' -- 作业区
                      and k.ATTRIBUTE_TYPE ='0' -- 存储区
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
                    -- 性能优化 add by zuo.sw 2014-05-08 
                    inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no= t.ware_no and k.area_no = t.area_no
                    inner join CM_DEFCELL  c  on   t.locno = c.locno
                           and c.ware_no =  t.ware_no and c.area_no = t.area_no and c.stock_no = t.stock_no
                    where  k.locno = STRLOCNO
                      and k.Area_Usetype = '5'
                      -- 异常区加上存储作业区 add by 2014-05-07
                      and k.AREA_ATTRIBUTE ='0' -- 作业区
                      and k.ATTRIBUTE_TYPE ='0' -- 存储区
                      --log20140124 modi by chenhaitao 取异常区储位不能带item_type
                      --and k.item_type = v_item_type
                      --end log20140124
                      and c.cell_status = '0'
                      and c.check_status = '0'
                      and rownum =1 ;
 
                  --updt by crm 20140110 统一库存记账，写储储位预上量
                  --开始
                  begin
                  acc_prepare_data_ext(
                                      strCheckNo ,
                                      'SC' ,
                                      'I' ,
                                      STRWORKERNO ,
                                      STR_I ,
                                       '',
                                      STRLOCNO,
                                      v_exption_cell_no,
                                      R_ITEM.Item_No,
                                      R_ITEM.Size_No,
                                      1,
                                      R_ITEM.item_type,
                                     R_ITEM.quality,
                                      strOwnerNo,
                                      R_ITEM.Supplier_No,
                                      R_ITEM.Box_No,
                                      0,
                                      0,
                                      STRREMAININGQTY,
                                      '0',
                                      '0',
                                      '1');
                --回写箱码储位
               -- update con_box x set x.cell_no=v_exption_cell_no where x.locno=STRLOCNO and x.owner_no=strOwnerNo and x.box_no=R_ITEM.Box_No;
                acc_apply(strCheckNo,'2','SC','I',1);
                --结束    
                 --获取目的储位Cell_ID   updt by crm 20140117 
                 --开始
                   begin
                    /*   select max(t.cell_id) into V_NCELLID  from tmp_acc_data  t where t.locno=STRLOCNO and t.owner_no=strOwnerNo
                       and t.cell_no=v_exption_cell_no and t.item_no=R_ITEM.ITEM_NO and t.barcode=R_ITEM.BARCODE
                       and t.supplier_no=R_ITEM.Supplier_No and t.quality=R_ITEM.quality and t.item_type=R_ITEM.item_type;*/
                       select t.cell_id  into V_NCELLID  From tmp_acc_result t where t.row_id=STR_I;
                   exception when no_Data_found then
                       STRRESULT := 'N|获取储位CELL_ID失败!';
                        return;
                   end;
                 --结束
               exception when others then
                    STRRESULT := 'N|' || SQLERRM ||
                    SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
                    RETURN;
          RETURN;
     end;
             
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
                     cell_no,
                     cell_id,
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
                     SIZE_NO,
                     brand_no,
                     item_type,
                     quality)
                  VALUES
                    (STRLOCNO,
                     strOwnerNo,
                     '1',-- 进货上架细定位
                     SEQ_BILL_IM_DIRECT.NEXTVAL,
                     -1,
                     STRcheckNO,--验收单号
                     '1',
                     'B',
                     R_ITEM.Item_No,
                     V_NCONITEMROWID,
                     R_ITEM.Pack_Qty,
                     v_z_cell_no,
                     v_z_cell_id,
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
                     R_ITEM.Size_No,
                     R_ITEM.brand_no,
                     R_ITEM.item_type,
                     R_ITEM.quality); --将该储位放满

                   STR_I := STR_I + 1; --行号自动加1

      end if;


    END LOOP;


   if  STR_I = 0  then
       STRRESULT := 'N|未预约到任何储位信息，请检查上架策略设置!';
       return;
   end if;

    -- 如果有预约成功的储位，则直接更新验收单的状态；
    if  STR_I > 0 then

          -- 更新验收单的状态为已预约；
          update  bill_im_check rdd
            set rdd.status = '30'
            where rdd.locno = STRLOCNO
            and rdd.check_no = STRcheckNO;
          if sql%rowcount=0 then
                   STRRESULT:='N|更新验收单为已预约时异常!';
                   return;
          end if;

    end if;



  STRRESULT := 'Y|';

  EXCEPTION
    WHEN OTHERS THEN
      STRRESULT := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
  END Proc_IM_CITY_INSTOCK_CHECK;


  /*
     作者:  zuo.sw
     日期:  2013-12-9
     功能:  上架定位写库存属性和库存/
    */
  PROCEDURE Proc_add_con_content(STR_LOCNO   IN BILL_IM_INSTOCK_DIRECT.LOCNO%TYPE, --仓别
                                 STR_OWNER_NO  IN Bill_Im_Receipt.Owner_No%TYPE, -- 委托业主
                                 STR_check_NO  IN Bill_Im_Receipt.Receipt_No%TYPE,--验收单号
                                 STR_ITEMNO  IN con_content.item_no%TYPE, -- 商品编码
                                 STR_SIZENO  IN  ITEM_BARCODE.SIZE_NO%type, -- 条码
                                 STR_BARCODE  IN  ITEM_BARCODE.BARCODE%type, -- 条码
                                 STR_BRANDNO  IN  BILL_IM_CHECK_DTL.BRAND_NO%type,-- 品牌编码
                                 STR_ITEM_TYPE IN Con_Content.Item_Type%TYPE,-- 商品属性
                                 STR_QUALITY IN Con_Content.Quality%TYPE,-- 品质
                                 STR_ITEM_PACK_SPEC IN item_pack.pack_spec%type, -- 商品包装规则
                                 STR_BOXNO  IN Bill_Im_Receipt_Dtl.Box_No%TYPE, -- 箱号
                                 STR_PACKQTY  IN Bill_Im_Receipt_Dtl.Pack_Qty%TYPE, -- 包装数量
                                 STR_CELLNO  IN Con_Content.Cell_No%TYPE, -- 储位编码
                                 v_z_cell_no in con_content.cell_no%type,-- 暂存区的储位编码
                                 v_z_cell_id in con_content.cell_id%type,--暂存区的储位ID
                                 STR_WORKERNO  IN BILL_IM_INSTOCK_DIRECT.CREATOR%TYPE, --操作人
                                 STR_FLAG     IN   VARCHAR2, -- 标记S,P,O
                                 STRREMAININGQTY in out number,
                                 STR_I  in out number,
                                 STR_SUPPLIER_NO in supplier.supplier_no%type,
                                 STRRESULT  OUT VARCHAR2)  as
  
    STR_QTY          CON_CONTENT.QTY%TYPE; --库存数量
    STR_INSTOCK_QTY  CON_CONTENT.INSTOCK_QTY%TYPE; --预上数量
    STR_LIMIT_QTY    CS_CELL_PACK_SETTING.LIMIT_QTY%TYPE; --最大存放数量
    STR_MIX_FLAG     CM_DEFCELL.MIX_FLAG%TYPE; --混放货物标识
    V_NCELLID        CON_CONTENT.CELL_ID%TYPE; --库存Cell_ID
    V_CELL_ALL_COUNT  NUMBER(10);  -- 上架范围的所有储位的数量
    V_INSTOCK_NUM   NUMBER(10); -- 当前的上架数量
    --v_item_pack_spec  item_pack.pack_spec%type; -- 商品包装规则
    v_cell_no_num  number(10);
    v_dif_item_no_num number(10);
  
    begin
         
                
                  
                  --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
                  BEGIN
                      SELECT LIMIT_QTY
                        INTO STR_LIMIT_QTY
                        FROM CS_CELL_PACK_SETTING PS
                      where  PS.LOCNO = STR_LOCNO
                         and ps.owner_no = STR_OWNER_NO
                         AND PS.AREA_TYPE = (select k.area_type  from CM_DEFAREa k 
                                inner join CM_DEFCELL  c  on   k.locno = c.locno  
                                       and c.ware_no = k.ware_no and  c.area_no = k.area_no 
                                where  c.locno = STR_LOCNO
                                and c.cell_no = STR_CELLNO and rownum =1 )
                         and ps.pack_spec = STR_ITEM_PACK_SPEC
                         AND ROWNUM = 1;
                    
                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|储位:' || STR_CELLNO ||'对应的库区类型未设置储位包装信息,请核实!';
                        RETURN;
                  END;
                  
                  
                  -- 查询当前储位上的库存数，预上数，预下数，差异数的汇总
                  begin
                      SELECT   
                               SUM(CON.QTY),
                               SUM(CON.INSTOCK_QTY)
                          INTO 
                               STR_QTY,
                               STR_INSTOCK_QTY
                          FROM CON_CONTENT CON
                      WHERE CON.LOCNO = STR_LOCNO
                           AND CON.CELL_NO = STR_CELLNO
                           AND CON.STATUS = '0' --盘点锁定标识为未锁定
                           AND CON.FLAG = '0' --冻结库存标识为未冻结
                      GROUP BY CON.CELL_NO;
                    EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        --库存数量为0
                        STR_QTY          := 0;
                        STR_INSTOCK_QTY  := 0;
                    end;
                  
                    -- 如果库存数量+预上数量<储位数量限定　(即表示当前的储位未满)
                    IF (STR_QTY + STR_INSTOCK_QTY < STR_LIMIT_QTY) THEN
                      
                       
                        -- 如果是排除同款储位和空储位的其他储位信息
                        if STR_FLAG ='O' then
                            -- 查询当前储位的库存记录
                            select count(*) into v_cell_no_num  from  v_content  v  
                            where v.cell_no = STR_CELLNO  
                            and v.locno = STR_LOCNO 
                            and v.owner_no = STR_OWNER_NO;
                            
                            -- 如果当前储位有库存记录
                            if v_cell_no_num > 0 then
                              
                                -- 查询当前商品在当前储位是否有库存记录
                                select count(*) into v_dif_item_no_num  from  v_content  v
                                where v.cell_no = STR_CELLNO  
                                and v.locno = STR_LOCNO 
                                and v.owner_no = STR_OWNER_NO
                                and v.item_no = STR_ITEMNO;
                                
                                --如果没有记录，则判断是否可以混款
                                if  v_dif_item_no_num < 1 then
                                   -- 获取当前储位的是可以可以混款标志
                                    SELECT MIX_FLAG INTO STR_MIX_FLAG  FROM CM_DEFCELL
                                    WHERE LOCNO = STR_LOCNO AND CELL_NO = STR_CELLNO;
                                
                                     -- 如果不可以混款，则继续下一个循环
                                    if STR_MIX_FLAG ='0' then
                                        STRRESULT:='X|';
                                        return;
                                    end if;
                                
                                end if;
                            
                            end if;
                            
                        end if;
                        
                        
                       --如果该商品总上架数量小于等于该储位剩余上架数量，则全部商品上架，待上数量为零
                       IF (STRREMAININGQTY <=(STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY)) THEN
                         
                            V_INSTOCK_NUM := STRREMAININGQTY;

                            -- 把需要商家预约的数量清0；
                            STRREMAININGQTY := 0;

                       ELSE --如果该商品上架数量大于该储位剩余上架数量，则将剩余数量填满，待上架数量为上架总数-上架数量

                            V_INSTOCK_NUM := STR_LIMIT_QTY - STR_QTY - STR_INSTOCK_QTY;

                            STRREMAININGQTY := STRREMAININGQTY - (STR_LIMIT_QTY - STR_QTY -STR_INSTOCK_QTY);
                            
                       END IF;

                             /*-- 查询对应的储位ID
                             SELECT count(*) into V_CELL_ALL_COUNT
                                 FROM CON_CONTENT CON
                               WHERE CON.LOCNO = STR_LOCNO
                                   AND CON.CELL_NO = STR_CELLNO
                                   AND CON.ITEM_NO = STR_ITEMNO
                                   AND CON.BARCODE=STR_BARCODE
                                   and con.supplier_no=STR_SUPPLIER_NO
                                   and con.pack_qty = STR_PACKQTY
                                   and con.item_type = STR_ITEM_TYPE
                                   and con.quality = STR_QUALITY
                                   AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                   AND CON.FLAG = '0'
                                  \* and con.instock_type = 0*\; -- 上架类型 --updt by crm 20140110 新库存表没有这个字段
                             
                             -- 如果有对应的储位库存信息，而且是同款储位或排除同款和空储位的其他储位
                             if  ((STR_FLAG ='O' OR  STR_FLAG ='S') AND  V_CELL_ALL_COUNT > 0) then
                                 -- 查询对应的储位ID
                                 SELECT  max(CON.CELL_ID) into V_NCELLID
                                     FROM CON_CONTENT CON
                                   WHERE CON.LOCNO = STR_LOCNO
                                       AND CON.CELL_NO = STR_CELLNO
                                       AND CON.ITEM_NO = STR_ITEMNO
                                       AND CON.BARCODE=STR_BARCODE
                                       and con.supplier_no=STR_SUPPLIER_NO
                                       and con.pack_qty = STR_PACKQTY
                                       and con.item_type = STR_ITEM_TYPE
                                      and con.quality = STR_QUALITY
                                       AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                       AND CON.FLAG = '0'

                                       \*and con.instock_type = 0*\; -- 上架类型
                                       
                                   
                              --updt by crm 20140110 统一库存记账，写储位预上量
                              --开始
                              acc_prepare_data_ext(STR_check_NO ,
                                                  'SC' ,
                                                  'I' ,
                                                  STR_WORKERNO ,
                                                  STR_I ,
                                                   '',
                                                  STR_LOCNO,
                                                  STR_CELLNO,
                                                  STR_ITEMNO,
                                                  STR_SIZENO,
                                                  1,
                                                  STR_ITEM_TYPE,
                                                  STR_QUALITY,
                                                  STR_OWNER_NO,
                                                  STR_SUPPLIER_NO,
                                                  STR_BOXNO,
                                                  0,
                                                  0,
                                                  V_INSTOCK_NUM,
                                                  '0',
                                                  '0',
                                                  '1');
                            --回写箱码储位
                             acc_apply(STR_check_NO,'2','SC','I',1);
                            --结束    

                                   
                                   
                             \*       -- 如果更新不到数据，则提示异常      
                                    if sql%rowcount=0 then
                                         STRRESULT:='N|更新存储区储位：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码；'||STR_ITEMNO||'
                                         ,箱号：'||STR_BOXNO||'的库存记录时未更新到数据!';
                                         return;
                                    end if;      *\
                                     
                              end if;  
                              
                              -- 如果是空储位，或者是同款储位，其他储位且找不到储位信息的，就全部新增
                              if (((STR_FLAG ='O' OR  STR_FLAG ='S') AND  V_CELL_ALL_COUNT < 1) OR STR_FLAG ='P')  then
                              --updt by crm 20140110 统一库存记账，写储储位预上量
                              --开始
                              acc_prepare_data_ext(STR_check_NO ,
                                                  'SC' ,
                                                  'I' ,
                                                  STR_WORKERNO ,
                                                  STR_I ,
                                                   '',
                                                  STR_LOCNO,
                                                  STR_CELLNO,
                                                  STR_ITEMNO,
                                                  STR_SIZENO,
                                                  1,
                                                  STR_ITEM_TYPE,
                                                  STR_QUALITY,
                                                  STR_OWNER_NO,
                                                  STR_SUPPLIER_NO,
                                                  STR_BOXNO,
                                                  0,
                                                  0,
                                                  V_INSTOCK_NUM,
                                                  '0',
                                                  '0',
                                                  '1');
                            --回写箱码储位
                             acc_apply(STR_check_NO,'2','SC','I',1);
                            --结束    
                            
                                    if  STR_ITEMNO is null  or  V_INSTOCK_NUM < 1 then
                                         STRRESULT:='N|新增存储区储位：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码'||STR_ITEMNO||'库存记录时，参数非法!';
                                         return;
                                    end if;
                                    
                                    -- 如果更新不到数据，则提示异常      
                                    if sql%rowcount=0 then
                                         STRRESULT:='N|新增存储区储位：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码；'||STR_ITEMNO||'
                                         ，箱号：'||STR_BOXNO||'的库存记录时异常!';
                                         return;
                                    end if;   
                                     
                             end if;
                       */
                       begin
                       --updt by crm 20140110 统一库存记账，写储位预上量
                              --开始
                              acc_prepare_data_ext(STR_check_NO ,
                                                  'SC' ,
                                                  'I' ,
                                                  STR_WORKERNO ,
                                                  STR_I ,
                                                   '',
                                                  STR_LOCNO,
                                                  STR_CELLNO,
                                                  STR_ITEMNO,
                                                  STR_SIZENO,
                                                  1,
                                                  STR_ITEM_TYPE,
                                                  STR_QUALITY,
                                                  STR_OWNER_NO,
                                                  STR_SUPPLIER_NO,
                                                  STR_BOXNO,
                                                  0,
                                                  0,
                                                  V_INSTOCK_NUM,
                                                  '0',
                                                  '0',
                                                  '1');
                            --回写箱码储位
                             acc_apply(STR_check_NO,'2','SC','I',1);
                            --结束    
                       
                       --获取目的储位Cell_ID   updt by crm 20140117 
                       --开始
                      /*   begin
                         select max(t.cell_id) into V_NCELLID  from tmp_acc_data  t where t.locno=STR_LOCNO and t.owner_no=STR_OWNER_NO
                         and t.cell_no=STR_CELLNO and t.item_no=STR_ITEMNO and t.barcode=STR_BARCODE
                         and t.supplier_no=STR_SUPPLIER_NO and t.quality=STR_QUALITY and t.item_type=STR_ITEM_TYPE;
                         exception when no_data_found then
                          end ;*/
                          begin
                          /*   select max(t.cell_id) into V_NCELLID  from tmp_acc_data  t where t.locno=STRLOCNO and t.owner_no=strOwnerNo
                             and t.cell_no=v_exption_cell_no and t.item_no=R_ITEM.ITEM_NO and t.barcode=R_ITEM.BARCODE
                             and t.supplier_no=R_ITEM.Supplier_No and t.quality=R_ITEM.quality and t.item_type=R_ITEM.item_type;*/
                             select t.cell_id  into V_NCELLID  From tmp_acc_result t where t.row_id=STR_I;
                         exception when no_Data_found then
                             STRRESULT := 'N|获取储位CELL_ID失败!';
                              return;
                         end;
                       --结束
                       exception when others then
                            STRRESULT := 'N|' || SQLERRM ||
                            SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
                            RETURN;
                      end;     
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
                           PACK_QTY,
                           CELL_NO,
                           CELL_id,
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
                           SIZE_NO,
                           brand_no,
                           item_type,
                           quality)
                        VALUES
                          (STR_LOCNO,
                           STR_OWNER_NO,
                           '1',-- 进货上架细定位
                           SEQ_BILL_IM_DIRECT.NEXTVAL,
                           -1,
                           STR_check_NO,--验收单号
                           '1',
                           'B',
                           STR_ITEMNO,
                           STR_PACKQTY,
                           v_z_cell_no,
                           v_z_cell_id,
                           STR_CELLNO,
                           V_NCELLID,
                           V_INSTOCK_NUM,
                           '10',
                           STR_WORKERNO,
                           SYSDATE,
                           STR_WORKERNO,
                           SYSDATE,
                           '0',
                           STR_BOXNO,
                           STR_SIZENO,
                           STR_BRANDNO,
                           STR_ITEM_TYPE,
                           STR_QUALITY); --将该储位放满
                           
                           
                           if  STR_ITEMNO is null or  V_INSTOCK_NUM < 1 then
                                         STRRESULT:='N|新增定位记录，预上储位编码：'||STR_CELLNO||'，ID：'||V_NCELLID||'时参数非法!';
                                         return;
                           end if;
                                    
                           -- 如果更新不到数据，则提示异常      
                           if sql%rowcount=0 then
                                         STRRESULT:='N|新增定位记录，预上储位编码：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码；'||STR_ITEMNO||'
                                         ，箱号：'||STR_BOXNO||'的时异常!';
                                         return;
                           end if;   
                           

                           STR_I := STR_I + 1; --行号自动加1
     end if;
 
  END Proc_add_con_content;

END PKG_IM_CITY_INSTOCK_CHECK;
/
