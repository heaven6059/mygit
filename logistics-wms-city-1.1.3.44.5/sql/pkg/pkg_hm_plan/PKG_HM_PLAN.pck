create or replace package PKG_HM_PLAN is

 /*****************************************************************************************
   功能：城市仓-移库发单
   create By  su.yq AT 2013-12-13 11:43:00
   Modify By
  *****************************************************************************************/
 
PROCEDURE PROC_HM_PLAN_SEND_ORDER (
  v_locno  in  BILL_OM_OUTSTOCK_DIRECT.Locno%type,
  v_ware_no in  cm_defcell.ware_no%type,
  v_area_no in  cm_defcell.area_no%type,
  v_str_direct_serial in varchar2,
  v_outstock_people in  bill_om_outstock_dtl.Assign_Name%type,
  v_creator  in  BILL_OM_OUTSTOCK.Creator%type,
  v_cell_type   in varchar2,
  strOutMsg   out varchar2
);

 /*****************************************************************************************
   功能：城市仓-移库确认
   create By  su.yq AT 2013-12-13 11:43:00
   Modify By
  *****************************************************************************************/
 
PROCEDURE PROC_HM_PLAN_QUERY_ORDER (
  v_locno  in  BILL_OM_OUTSTOCK_DTL.Locno%TYPE,
  v_outstock_no  in BILL_OM_OUTSTOCK_DTL.OUTSTOCK_NO%TYPE,
  v_creator  in  con_content_move.Creator%type,
  strOutMsg   out varchar2 
);

 /*****************************************************************************************
   功能：验证移库储位是否正确
   create By  su.yq AT 2014-03-26 11:43:00
   Modify By
  *****************************************************************************************/
procedure PROC_HM_CHeckISAllowMoveStock(I_locno        bm_defloc.locno%type, --仓别
                                     I_sCellNo      cm_defcell.cell_no%type, --源储位
                                     I_sItemType    cm_defcell.item_type%type,
                                     I_sAreaQuality cm_defcell.area_quality%type,
                                     I_dCellNo      cm_defcell.cell_no%type, --目的储位
                                     I_rowid        bill_hm_plan_dtl.row_id%type,--验证标示
                                     O_msg          OUT VARCHAR2);

/*****************************************************************************************
 功能：写移库预约上下架
create By he.w AT 2013-7-16
modi by
pragma autonomous_transaction;
*****************************************************************************************/
procedure proc_WriteContainerContent(strLocNo         in con_content.locno%type, --仓别
                                     strOwnerNo       in con_content.owner_no%type,
                                     strCellNo        in con_content.cell_no%type,
                                     strCellId        in con_content.cell_id%type,
                                     strDCellNo       in con_content.cell_no%type,
                                     strItemNo        in con_content.item_no%type,
                                     strSizeNo        in size_info.size_no%type,
                                     strPackQty       in con_content.pack_qty%type,
                                     strQty           in con_content.qty%type,
                                     strOutStockQty   in con_content.outstock_qty%type,
                                     strInStockQty    in con_content.instock_qty%type,
                                     strUnusualQty    in con_content.unusual_qty%type,
                                     strStatus        in con_content.status%type,
                                     strFlag          in con_content.flag%type,
                                     strHmManualFlag  in con_content.hm_manual_flag%type,
                                     strUserId        in con_content.creator%type,
                                     strPlanNo        in con_content_move.paper_no%type,
                                     strItemType      in con_content.item_type%type,
                                     strQuality       in con_content.quality%type,
                                     strSupplierNo    in con_content.supplier_no%type,
                                     strBoxNo         in con_box.box_no%type,
                                     strRowId         in number,
                                     strTerminalFlag  in con_content_move.terminal_flag%type,
                                     strDCellId       out con_content.cell_id%type,
                                     strOutMsg        out varchar2);

end PKG_HM_PLAN;
/
CREATE OR REPLACE PACKAGE BODY PKG_HM_PLAN IS

  /*****************************************************************************************
   功能：城市仓-移库发单
   create By  su.yq AT 2013-12-13 11:43:00
   Modify By
  *****************************************************************************************/
  PROCEDURE PROC_HM_PLAN_SEND_ORDER(v_locno             IN BILL_OM_OUTSTOCK_DIRECT.Locno%TYPE,
                                    v_ware_no           IN cm_defcell.ware_no%TYPE,
                                    v_area_no           IN cm_defcell.area_no%TYPE,
                                    v_str_direct_serial IN VARCHAR2,
                                    v_outstock_people   IN bill_om_outstock_dtl.Assign_Name%TYPE,
                                    v_creator           IN BILL_OM_OUTSTOCK.Creator%TYPE,
                                    v_cell_type         IN VARCHAR2,
                                    strOutMsg           OUT VARCHAR2)
  
   IS
    v_direct_cow_old VARCHAR2(1000); -- 上一成单条件
    v_direct_cow_new VARCHAR2(1000); -- 当前成单条件
    v_no             VARCHAR2(30);
    v_maxDivideId    NUMBER(11);
    v_nExists        NUMBER := 0;
  
  BEGIN
    strOutMsg        := 'N|[E00025]';
    v_direct_cow_old := 'N';
    v_direct_cow_new := 'N';
    v_no             := 'N';
  
    --v_cell_type:0-来源储位，1-目的储位，2-按来源目的储位
    -- 如果库区不为空,且储区编码不为空
    IF (v_area_no IS NOT NULL AND v_ware_no IS NOT NULL AND
       v_cell_type = '1') THEN
    
      FOR direct_cord IN (
                          
                          SELECT od.*, d.ware_no, d.area_no
                            FROM BILL_OM_OUTSTOCK_DIRECT od
                           INNER JOIN cm_defcell d
                              ON d.cell_no = od.d_cell_no
                             AND od.locno = d.locno
                             AND od.owner_no = d.owner_no
                           INNER JOIN cm_defarea CDA
                              ON d.LOCNO = CDA.LOCNO
                             AND d.WARE_NO = CDA.WARE_NO
                             AND d.area_no = cda.area_no
                           WHERE od.locno = v_locno
                             AND d.area_no = v_area_no
                             AND d.ware_no = v_ware_no
                            /* AND od.d_cell_no LIKE
                                 '%' || v_ware_no || v_area_no || '%'*/
                             AND instr(',' || v_str_direct_serial || ',',
                                       ',' || od.direct_serial || ',') > 0
                           ORDER BY od.exp_Type,
                                     od.outstock_type,
                                     od.operate_type,
                                     od.batch_no,
                                     od.locate_no,
                                     od.pick_type,
                                     od.s_cell_no,
                                     od.item_no
                          
                          ) LOOP
        v_nExists := v_nExists + 1;
        -- 成单条件
        -- 如按仓别+波次+批次+出货类型+下架类型+作业类型+拣货类型+优先级+切单区域方式（切单规则表）+(如果拣货类型为摘果则加上客户条件)+切单计算方式)
        v_direct_cow_new := direct_cord.locno || direct_cord.locate_no ||
                            direct_cord.batch_no || direct_cord.exp_type ||
                            direct_cord.outstock_type ||
                            direct_cord.operate_type ||
                            direct_cord.pick_type || direct_cord.priority ||
                            direct_cord.ware_no || direct_cord.area_no;
      
        -- 如果上一成单条件不等于当前的成单条件，则需要拆单，写入主表和明细表
        IF v_direct_cow_new <> v_direct_cow_old THEN
        
          -- 调用自动生成单号的存储过程
          PKG_WMS_BASE.proc_getsheetno(v_locno, 'HS', v_no, strOutMsg);
        
          IF instr(strOutMsg, 'N', 1, 1) = 1 THEN
            ROLLBACK;
            RETURN;
          END IF;
        
          IF v_no = 'N' THEN
            ROLLBACK;
            strOutMsg := 'N|自动生成单号错误!';
            RETURN;
          END IF;
        
          -- 插入下架单主表  BILL_OM_OUTSTOCK
          INSERT INTO BILL_OM_OUTSTOCK
            (LOCNO,
             OUTSTOCK_NO,
             OUTSTOCK_TYPE,
             BATCH_NO,
             OPERATE_TYPE,
             Operate_Date,
             PICK_TYPE,
             STATUS,
             PRIORITY,
             Creator,
             Createtm)
            SELECT direct_cord.locno,
                   v_no,
                   direct_cord.outstock_type,
                   direct_cord.batch_no,
                   direct_cord.operate_type,
                   direct_cord.operate_date,
                   direct_cord.pick_type,
                   '10',
                   direct_cord.priority,
                   v_creator,
                   SYSDATE
              FROM DUAL;
        
          IF SQL%ROWCOUNT <= 0 THEN
            ROLLBACK;
            strOutMsg := 'N|插入下架单主表异常!';
            RETURN;
          END IF;
        
        END IF;
      
        --查询明细表最大的divideId
        SELECT nvl(MAX(dtl.divide_id),0) + 1
          INTO v_maxDivideId
          FROM Bill_Om_Outstock_Dtl dtl;
      
        -- 插入下架单明细表  BILL_OM_OUTSTOCK_DTL
        INSERT INTO Bill_Om_Outstock_Dtl
          (Locno,
           Owner_No,
           Outstock_No,
           Divide_Id,
           Operate_Date,
           Batch_No,
           Exp_Type,
           Exp_No,
           Exp_Date,
           Locate_No,
           Store_No,
           Item_No,
           Item_Id,
           PACK_QTY,
           s_Cell_No,
           s_Cell_Id,
           D_CELL_NO,
           D_CELL_ID,
           s_Container_No,
           Item_Qty,
           Size_No,
           Assign_Name,
           QUALITY,
           ITEM_TYPE,
           brand_no)
          SELECT direct_cord.locno,
                 direct_cord.owner_no,
                 v_no,
                 v_maxDivideId,
                 direct_cord.operate_date,
                 direct_cord.batch_no,
                 direct_cord.exp_type,
                 direct_cord.exp_no,
                 direct_cord.exp_date,
                 direct_cord.locate_no,
                 direct_cord.store_no,
                 direct_cord.item_no,
                 direct_cord.item_id,
                 direct_cord.pack_qty,
                 direct_cord.s_cell_no,
                 direct_cord.s_cell_id,
                 direct_cord.d_cell_no,
                 direct_cord.d_cell_id,
                 direct_cord.s_container_no,
                 direct_cord.item_qty,
                 direct_cord.size_no,
                 v_outstock_people,
                 direct_cord.Quality,
                 direct_cord.Item_Type,
                 direct_cord.brand_no
            FROM DUAL;
        IF SQL%ROWCOUNT <= 0 THEN
          ROLLBACK;
          strOutMsg := 'N|插入下架单明细异常!';
          RETURN;
        END IF;
      
        -- 更新下架指示的状态 （13-已发单）
        UPDATE BILL_OM_OUTSTOCK_DIRECT ak
           SET ak.status = '13', ak.editor = v_creator, ak.edittm = SYSDATE
         WHERE ak.locno = direct_cord.locno
           AND ak.owner_no = direct_cord.owner_no
           AND ak.direct_serial = direct_cord.direct_serial;
        /*update BILL_OM_OUTSTOCK_DIRECT ak
              set ak.status = '13',ak.editor = v_creator,ak.edittm = sysdate
        where ak.locno = direct_cord.locno
              and ak.owner_no = direct_cord.owner_no
              and ak.store_no = direct_cord.store_no
              and ak.d_cell_no = direct_cord.s_cell_no
              and ak.outstock_type = direct_cord.outstock_type
              and ak.operate_type = direct_cord.operate_type
              and ak.pick_type = direct_cord.pick_type
              and ak.exp_type = direct_cord.exp_type
              and ak.batch_no = direct_cord.batch_no
              and ak.locate_no = direct_cord.locate_no
              and ak.item_no = direct_cord.item_no
              and ak.size_no =  direct_cord.size_no;*/
      
        IF SQL%ROWCOUNT <= 0 THEN
          ROLLBACK;
          strOutMsg := 'N|更新下架指示的状态失败!';
          RETURN;
        END IF;
      
        -- 当前的值赋值给前一个
        v_direct_cow_old := v_direct_cow_new;
      
      END LOOP;
    
    ELSE
    
      FOR direct_cord IN (
                          
                          SELECT od.*, d.ware_no, d.area_no
                            FROM BILL_OM_OUTSTOCK_DIRECT od
                           INNER JOIN cm_defcell d
                              ON d.cell_no = od.s_cell_no
                             AND od.locno = d.locno
                             AND od.owner_no = d.owner_no
                           INNER JOIN cm_defarea CDA
                              ON d.LOCNO = CDA.LOCNO
                             AND d.WARE_NO = CDA.WARE_NO
                             AND d.area_no = cda.area_no
                           WHERE od.locno = v_locno
                             AND d.area_no = v_area_no
                             AND d.ware_no = v_ware_no
                             /*AND od.s_cell_no LIKE
                                 '%' || v_ware_no || v_area_no || '%'*/
                             AND instr(',' || v_str_direct_serial || ',',
                                       ',' || od.direct_serial || ',') > 0
                           ORDER BY od.exp_Type,
                                     od.outstock_type,
                                     od.operate_type,
                                     od.batch_no,
                                     od.locate_no,
                                     od.pick_type,
                                     od.s_cell_no,
                                     od.item_no
                          
                          ) LOOP
        v_nExists := v_nExists + 1;
        -- 成单条件
        -- 如按仓别+波次+批次+出货类型+下架类型+作业类型+拣货类型+优先级+切单区域方式（切单规则表）+(如果拣货类型为摘果则加上客户条件)+切单计算方式)
        v_direct_cow_new := direct_cord.locno || direct_cord.locate_no ||
                            direct_cord.batch_no || direct_cord.exp_type ||
                            direct_cord.outstock_type ||
                            direct_cord.operate_type ||
                            direct_cord.pick_type || direct_cord.priority;
      
        IF v_cell_type = '0' THEN
          v_direct_cow_new := v_direct_cow_new || direct_cord.ware_no ||
                              direct_cord.area_no;
        END IF;
      
        IF v_cell_type = '2' THEN
          v_direct_cow_new := v_direct_cow_new || direct_cord.s_cell_no ||
                              direct_cord.d_cell_no;
        END IF;
      
        -- 如果上一成单条件不等于当前的成单条件，则需要拆单，写入主表和明细表
        IF v_direct_cow_new <> v_direct_cow_old THEN
        
          -- 调用自动生成单号的存储过程
          PKG_WMS_BASE.proc_getsheetno(v_locno, 'HS', v_no, strOutMsg);
        
          IF instr(strOutMsg, 'N', 1, 1) = 1 THEN
            ROLLBACK;
            RETURN;
          END IF;
          IF v_no = 'N' THEN
            ROLLBACK;
            strOutMsg := 'N|自动生成单号错误!';
            RETURN;
          END IF;
        
          -- 插入下架单主表  BILL_OM_OUTSTOCK
          INSERT INTO BILL_OM_OUTSTOCK
            (LOCNO,
             OUTSTOCK_NO,
             OUTSTOCK_TYPE,
             BATCH_NO,
             OPERATE_TYPE,
             Operate_Date,
             PICK_TYPE,
             STATUS,
             PRIORITY,
             Creator,
             Createtm)
            SELECT direct_cord.locno,
                   v_no,
                   direct_cord.outstock_type,
                   direct_cord.batch_no,
                   direct_cord.operate_type,
                   direct_cord.operate_date,
                   direct_cord.pick_type,
                   '10',
                   direct_cord.priority,
                   v_creator,
                   SYSDATE
              FROM DUAL;
        
          IF SQL%ROWCOUNT <= 0 THEN
            ROLLBACK;
            strOutMsg := 'N|插入下架单主表异常!';
            RETURN;
          END IF;
        
        END IF;
      
        --查询明细表最大的divideId
        SELECT nvl(MAX(dtl.divide_id),0) + 1
          INTO v_maxDivideId
          FROM Bill_Om_Outstock_Dtl dtl;
      
        -- 插入下架单明细表  BILL_OM_OUTSTOCK_DTL
        INSERT INTO Bill_Om_Outstock_Dtl
          (Locno,
           Owner_No,
           Outstock_No,
           Divide_Id,
           Operate_Date,
           Batch_No,
           Exp_Type,
           Exp_No,
           Exp_Date,
           Locate_No,
           Store_No,
           Item_No,
           Item_Id,
           PACK_QTY,
           s_Cell_No,
           s_Cell_Id,
           D_CELL_NO,
           D_CELL_ID,
           s_Container_No,
           Item_Qty,
           Size_No,
           Assign_Name,
           QUALITY,
           ITEM_TYPE,
           brand_no)
          SELECT direct_cord.locno,
                 direct_cord.owner_no,
                 v_no,
                 v_maxDivideId,
                 direct_cord.operate_date,
                 direct_cord.batch_no,
                 direct_cord.exp_type,
                 direct_cord.exp_no,
                 direct_cord.exp_date,
                 direct_cord.locate_no,
                 direct_cord.store_no,
                 direct_cord.item_no,
                 direct_cord.item_id,
                 direct_cord.pack_qty,
                 direct_cord.s_cell_no,
                 direct_cord.s_cell_id,
                 direct_cord.d_cell_no,
                 direct_cord.d_cell_id,
                 direct_cord.s_container_no,
                 direct_cord.item_qty,
                 direct_cord.size_no,
                 v_outstock_people,
                 direct_cord.Quality,
                 direct_cord.Item_Type,
                 direct_cord.Brand_No
            FROM DUAL;
      
        IF SQL%ROWCOUNT <= 0 THEN
          ROLLBACK;
          strOutMsg := 'N|插入下架单明细异常!';
          RETURN;
        END IF;
      
        -- 更新下架指示的状态 （13-已发单）
        UPDATE BILL_OM_OUTSTOCK_DIRECT ak
           SET ak.status = '13', ak.editor = v_creator, ak.edittm = SYSDATE
         WHERE ak.locno = direct_cord.locno
           AND ak.owner_no = direct_cord.owner_no
           AND ak.direct_serial = direct_cord.direct_serial;
        /*update BILL_OM_OUTSTOCK_DIRECT ak
              set ak.status = '13',ak.editor = v_creator,ak.edittm = sysdate
        where ak.locno = direct_cord.locno
              and ak.owner_no = direct_cord.owner_no
              and ak.store_no = direct_cord.store_no
              and ak.s_cell_no = direct_cord.s_cell_no
              and ak.outstock_type = direct_cord.outstock_type
              and ak.operate_type = direct_cord.operate_type
              and ak.pick_type = direct_cord.pick_type
              and ak.exp_type = direct_cord.exp_type
              and ak.batch_no = direct_cord.batch_no
              and ak.locate_no = direct_cord.locate_no
              and ak.item_no = direct_cord.item_no
              and ak.size_no =  direct_cord.size_no;*/
      
        IF SQL%ROWCOUNT <= 0 THEN
          ROLLBACK;
          strOutMsg := 'N|更新下架指示的状态异常!';
          RETURN;
        END IF;
      
        -- 当前的值赋值给前一个
        v_direct_cow_old := v_direct_cow_new;
      
      END LOOP;
    
    END IF;
  
    IF v_nExists < 1 THEN
      strOutMsg := 'N|根据库区发单时,循环未执行!';
      RETURN;
    END IF;
    -- 提交
    --commit;
    strOutMsg := 'Y';
  END PROC_HM_PLAN_SEND_ORDER;

  /*****************************************************************************************
   功能：城市仓-移库确认
   create By  su.yq AT 2013-12-13 11:43:00
   Modify By
  *****************************************************************************************/

  PROCEDURE PROC_HM_PLAN_QUERY_ORDER(v_locno       IN BILL_OM_OUTSTOCK_DTL.Locno%TYPE,
                                     v_outstock_no IN BILL_OM_OUTSTOCK_DTL.OUTSTOCK_NO%TYPE,
                                     v_creator     IN con_content_move.Creator%TYPE,
                                     strOutMsg     OUT VARCHAR2)
  
   IS
    intRecord INTEGER; --状态为10的拣货单明细数量
    intRealCount number;--是否
    v_status varchar(20);--状态
    --intRowId integer; --行号
    --intSCellNoNum integer;--来源存储数量
    --intDCellNoNum integer;--目的存储数量
  BEGIN
  
    strOutMsg := 'N|[E00025]';
    
    SELECT oo.status
      into v_status
      FROM bill_om_outstock oo
     WHERE OUTSTOCK_NO = v_outstock_no
       AND locno = v_locno;
    /* 
    select count(*)
      into intRealCount
      from bill_om_outstock d
     where d.OUTSTOCK_NO = v_outstock_no
       AND d.locno = v_locno
       and ((exists (select 'X'
                   from bill_om_outstock_dtl a
                  where a.locno = d.locno
                    and a.outstock_no = d.outstock_no
                    and a.status = '10')       
        and exists (select 'X'
                       from bill_om_outstock_dtl a
                      where a.locno = d.locno
                        and a.outstock_no = d.outstock_no
                        and a.status = '12')) or exists
        (select 'X'
           from bill_om_outstock_dtl a
          where a.locno = d.locno
            and a.outstock_no = d.outstock_no
            and a.real_qty > 0));

    if intRealCount > 0 then
      strOutMsg := 'N|正在下架或下架中,不能进行回单操作!';
      RETURN;
    end if;
    */
    IF (v_locno IS NOT NULL AND v_outstock_no IS NOT NULL AND
       v_creator IS NOT NULL) THEN
      FOR detail IN (SELECT *
                       FROM bill_om_outstock_dtl
                      WHERE OUTSTOCK_NO = v_outstock_no
                        AND locno = v_locno) LOOP                                     
        
        --更新移库明细回单数量,更新状态为13
        UPDATE BILL_OM_OUTSTOCK_DTL dtl
           SET dtl.status = '13'
         WHERE dtl.locno = v_locno
           AND dtl.owner_no = detail.owner_no
           AND dtl.divide_id = detail.divide_id
           AND dtl.outstock_no = v_outstock_no;
      
        IF SQL%ROWCOUNT <= 0 THEN
          ROLLBACK;
          strOutMsg := 'N|更新移库明细回单数量和状态失败,没有更新到数据！]';
          RETURN;
        END IF;
      
        --查询如果明细全部完成更新拣货单主表状态为90
        SELECT COUNT(1)
          INTO intRecord
          FROM BILL_OM_OUTSTOCK_DTL dd
         WHERE dd.locno = v_locno
           AND dd.owner_no = detail.owner_no
           AND dd.divide_id = detail.divide_id
           AND dd.status = '10';
      
        IF (intRecord = 0) THEN
          UPDATE BILL_OM_OUTSTOCK ot
             SET ot.status = '90',
              ot.auditor = v_creator,
             ot.audittm = SYSDATE
           WHERE ot.locno = v_locno
             AND ot.outstock_no = v_outstock_no;
          IF SQL%ROWCOUNT <= 0 THEN
            ROLLBACK;
            strOutMsg := 'N|更新拣货单主表状态失败,,没有更新到数据！]';
            RETURN;
          END IF;
        END IF;       
      
      --通过明细商品信息更新库存表，根据明细商品、储位、数量等更新该商品预上预下数量并记录库存移动表  
      
      /* for direct_cord in 
                    (select od.* from BILL_OM_OUTSTOCK_DTL od 
                          where od.locno=v_locno and od.owner_no=v_owner_no
                          and od.divide_id=v_divide_id and od.outstock_no=v_outstock_no) loop 
                          
                          --更新库存表预下数量
                          update con_content scc set scc.outstock_qty=scc.outstock_qty-direct_cord.real_qty
                          ,scc.qty=scc.qty-direct_cord.real_qty 
                                 where scc.locno=direct_cord.locno and scc.cell_no=direct_cord.s_cell_no
                                 and scc.item_no=direct_cord.item_no and scc.item_id=direct_cord.item_id
                                 and scc.owner_no=direct_cord.owner_no;  
                                 
                         if sql%rowcount<=0 then
                              rollback;
                              strOutMsg := 'N|更新预下数量失败,没有更新到数据！]';
                              return;
                          end if;
                          
                          
                          --更新库存表预上数量
                          update con_content scc set scc.instock_qty=scc.instock_qty-direct_cord.real_qty
                          ,scc.qty=scc.qty+direct_cord.real_qty 
                                 where scc.locno=direct_cord.locno and scc.cell_no=direct_cord.d_cell_no
                                 and scc.item_no=direct_cord.item_no and scc.item_id=direct_cord.item_id
                                 and scc.owner_no=direct_cord.owner_no;   
                          
                          if sql%rowcount<=0 then
                              rollback;
                              strOutMsg := 'N|更新预上数量失败,没有更新到数据！]';
                              return;
                          end if; 
                          
                          
                          --来源储位库存数量、预上数量、预下数量是否为0，为0就删除该库存
                          select count(1) into intSCellNoNum from Con_Content cc1
                           where cc1.locno=direct_cord.locno and cc1.cell_no=direct_cord.s_cell_no
                                 and cc1.item_no=direct_cord.item_no and cc1.item_id=direct_cord.item_id
                                 and cc1.owner_no=direct_cord.owner_no and cc1.qty=0 and cc1.outstock_qty=0
                                 and cc1.instock_qty=0;  
                           if(intSCellNoNum > 0) then 
                               delete from Con_Content cc1d where cc1d.locno=direct_cord.locno and cc1d.cell_no=direct_cord.d_cell_no
                                 and cc1d.item_no=direct_cord.item_no and cc1d.item_id=direct_cord.item_id
                                 and cc1d.owner_no=direct_cord.owner_no and cc1d.qty=0 and cc1d.outstock_qty=0
                                 and cc1d.instock_qty=0; 
                           end if;   
                           
                           
                            --目的储位库存数量、预上数量、预下数量是否为0，为0就删除该库存
                          select count(1) into intSCellNoNum from Con_Content cc1
                           where cc1.locno=direct_cord.locno and cc1.cell_no=direct_cord.d_cell_no
                                 and cc1.item_no=direct_cord.item_no and cc1.item_id=direct_cord.item_id
                                 and cc1.owner_no=direct_cord.owner_no and cc1.qty=0 and cc1.outstock_qty=0
                                 and cc1.instock_qty=0;  
                           if(intDCellNoNum > 0) then 
                               delete from Con_Content cc1d where cc1d.locno=direct_cord.locno and cc1d.cell_no=direct_cord.d_cell_no
                                 and cc1d.item_no=direct_cord.item_no and cc1d.item_id=direct_cord.item_id
                                 and cc1d.owner_no=direct_cord.owner_no and cc1d.qty=0 and cc1d.outstock_qty=0
                                 and cc1d.instock_qty=0; 
                           end if;      
                          
                          --记录库存移动表
                          select max(ccm.row_id) into intRowId from con_content_move ccm;
                          insert into con_content_move
                            (locno, owner_no, row_id, paper_type, paper_no, terminal_flag, 
                            item_no, item_id, cell_no, container_no, cell_id, first_qty, 
                            first_instock_qty, first_outstock_qty, r_cell_no, io_flag, 
                            move_qty, creator, createtm, exp_date, stock_type, stock_value, status_trans)
                          values
                            (direct_cord.locno, direct_cord.owner_no, intRowId, '',direct_cord.outstock_no , '',
                             direct_cord.item_no, direct_cord.item_id, direct_cord.s_cell_no, '', direct_cord.s_cell_id, 
                             0,0, 0, direct_cord.d_cell_no, '', 
                             direct_cord.real_qty, v_creator, sysdate, sysdate, '1', 'N', '10');
                          if sql%rowcount<=0 then
                           rollback;
                           strOutMsg := 'N|记录库存移动表失败,没有插入数据！]';
                           return;
                          end if; 
                          
                  end loop; */
      
      END LOOP;
    END IF;
    
    IF v_status = '30'
      THEN
         --扣减库存
         ACC_APPLY(v_outstock_no,'2','HS','I',0);
    ELSE
         --扣减库存
         ACC_APPLY(v_outstock_no,'2','HS','O',0);         
         --扣减库存
         ACC_APPLY(v_outstock_no,'2','HS','I',0); 
    END IF;
    
  
    -- 提交
    --commit;
    strOutMsg := 'Y';
  END PROC_HM_PLAN_QUERY_ORDER;
  
   /*****************************************************************************************
   功能：验证移库储位是否正确
   create By  su.yq AT 2014-03-26 11:43:00
   Modify By
  *****************************************************************************************/
  procedure PROC_HM_CHeckISAllowMoveStock(I_locno        bm_defloc.locno%type, --仓别
                                     I_sCellNo      cm_defcell.cell_no%type, --源储位
                                     I_sItemType    cm_defcell.item_type%type,
                                     I_sAreaQuality cm_defcell.area_quality%type,
                                     I_dCellNo      cm_defcell.cell_no%type, --目的储位
                                     I_rowid        bill_hm_plan_dtl.row_id%type,--验证标示
                                     O_msg          OUT VARCHAR2)IS
  
  BEGIN
    
       pkg_hm_city_outstock.proc_hm_isallowmovestock(I_locno,
                                                     I_sCellNo,
                                                     I_sItemType,
                                                     I_sAreaQuality,
                                                     I_dCellNo,
                                                     O_msg);
       if instr(O_msg, 'N', 1, 1) = 1 then
          return;
        end if;
                                                     
  END;
                                     
                                     

 /*****************************************************************************************
 功能：写移库预约上下架
create By he.w AT 2013-7-16
modi by
pragma autonomous_transaction;
*****************************************************************************************/
procedure proc_WriteContainerContent(strLocNo         in con_content.locno%type, --仓别
                                     strOwnerNo       in con_content.owner_no%type,--委托业主
                                     strCellNo        in con_content.cell_no%type,--来源储位
                                     strCellId        in con_content.cell_id%type,--来源储位ID
                                     strDCellNo       in con_content.cell_no%type,--目的储位
                                     strItemNo        in con_content.item_no%type,
                                     strSizeNo        in size_info.size_no%type,
                                     strPackQty       in con_content.pack_qty%type,
                                     strQty           in con_content.qty%type,
                                     strOutStockQty   in con_content.outstock_qty%type,
                                     strInStockQty    in con_content.instock_qty%type,
                                     strUnusualQty    in con_content.unusual_qty%type,
                                     strStatus        in con_content.status%type,
                                     strFlag          in con_content.flag%type,
                                     strHmManualFlag  in con_content.hm_manual_flag%type,
                                     strUserId        in con_content.creator%type,
                                     strPlanNo        in con_content_move.paper_no%type,--移库计划单
                                     strItemType      in con_content.item_type%type,
                                     strQuality       in con_content.quality%type,
                                     strSupplierNo    in con_content.supplier_no%type,
                                     strBoxNo         in con_box.box_no%type,--有则传没有则传N
                                     strRowId         in number,
                                     strTerminalFlag  in con_content_move.terminal_flag%type,--操作终端标识(1：前台；2：RF；3：DPS；4：AS)
                                     strDCellId       out con_content.cell_id%type,--要求库存处理后返回 目的储位ID 写指示
                                     strOutMsg        out varchar2)
is
  nROW_ID number(10);                                   
begin
  begin
    strOutMsg := 'N|[E00025]';
    --更新预下库存    
    acc_prepare_data_ext(strPlanNo,
                         'HM',
                         'O',
                         strUserId,
                         strRowId,
                         strCellId,
                         strLocNo,
                         strCellNo,
                         strItemNo,
                         strSizeNo,
                         strPackQty,
                         strItemType,
                         strQuality,
                         strOwnerNo,
                         strSupplierNo,
                         strBoxNo,
                         0,
                         strOutStockQty,
                         0,
                         '0',
                         '0',
                         '1',
                         strTerminalFlag);
    --处理记账临时表
    acc_apply(strPlanNo,'2','HM','O',1);                
    --新增预上库存信息  （ 如果不返回cell_id  则考虑是否不写预上信息 ）
    acc_prepare_data_ext(strPlanNo,
                         'HM',
                         'I',
                         strUserId,
                         strRowId,
                         '',
                         strLocNo,
                         strDCellNo,
                         strItemNo,
                         strSizeNo,
                         strPackQty,
                         strItemType,
                         strQuality,
                         strOwnerNo,
                         strSupplierNo,
                         strBoxNo,
                         0,
                         0,
                         strInStockQty,
                         '0',
                         '0',
                         '1',
                         strTerminalFlag);
    --处理记账临时表
    acc_apply(strPlanNo,'2','HM','I',1);
    --返回cell_id 
    begin 
      select d.cell_id into strDCellId from TMP_ACC_RESULT d where d.row_id=strRowId;
    exception
      when no_data_found then
        strOutMsg := 'N|获取返回储位ID错误';
        return;
    end;
    strOutMsg := 'Y';
  exception
      when no_data_found then
        strOutMsg := 'N|[EEEEEE]';
        return;
      when others then
        strOutMsg := 'N|' || SQLERRM ||
        substr(dbms_utility.format_error_backtrace, 1, 256);
        return;
  end;
end proc_WriteContainerContent;


END PKG_HM_PLAN;
/
