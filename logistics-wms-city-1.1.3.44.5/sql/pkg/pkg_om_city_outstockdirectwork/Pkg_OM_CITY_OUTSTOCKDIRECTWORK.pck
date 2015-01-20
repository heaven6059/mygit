create or replace package Pkg_OM_CITY_OUTSTOCKDIRECTWORK is

/*****************************************************************************************
   功能：城市仓-拣货任务分派发单
   create By  su.yq AT 2014-01-15 14:18:00
   Modify By
  *****************************************************************************************/

PROCEDURE PROC_OM_OUTSTOCK_DIRECT (
  v_exp_type IN BILL_OM_OUTSTOCK_DIRECT.Exp_Type%TYPE,
  v_sort_type IN varchar2,
  v_work_num IN NUMBER,
  v_operate_type  in BILL_OM_OUTSTOCK_DIRECT.Operate_Type%TYPE,
  v_batch_no in  BILL_OM_OUTSTOCK_DIRECT.Batch_No%type,
  v_locate_no in  BILL_OM_OUTSTOCK_DIRECT.Locate_No%type,
  v_locno  in  BILL_OM_OUTSTOCK_DIRECT.Locno%type,
  v_area_no in  cm_defcell.area_no%type,
  v_store_no  in  BILL_OM_OUTSTOCK_DIRECT.Store_No%type,
  v_pickingpeople in  Bill_Om_Outstock_Dtl.Assign_Name%type,
  v_creator  in  BILL_OM_OUTSTOCK.Creator%type,
  strOutMsg   out varchar2
);

end Pkg_OM_CITY_OUTSTOCKDIRECTWORK;
/
create or replace package body Pkg_OM_CITY_OUTSTOCKDIRECTWORK is

  /*****************************************************************************************
   功能：城市仓-拣货任务分派发单
   create By  zuo.sw AT 2013-10-16 17:18:00
   Modify By
  *****************************************************************************************/
  PROCEDURE PROC_OM_OUTSTOCK_DIRECT(v_exp_type      IN BILL_OM_OUTSTOCK_DIRECT.Exp_Type%TYPE,
                                    v_sort_type IN varchar2, -- 排序类型 0 按储位 1按商品
                                    v_work_num      IN NUMBER,
                                    v_operate_type  in BILL_OM_OUTSTOCK_DIRECT.Operate_Type%TYPE,
                                    v_batch_no      in BILL_OM_OUTSTOCK_DIRECT.Batch_No%type,
                                    v_locate_no     in BILL_OM_OUTSTOCK_DIRECT.Locate_No%type,
                                    v_locno         in BILL_OM_OUTSTOCK_DIRECT.Locno%type,
                                    v_area_no       in cm_defcell.area_no%type,
                                    v_store_no      in BILL_OM_OUTSTOCK_DIRECT.Store_No%type,
                                    v_pickingpeople in Bill_Om_Outstock_Dtl.Assign_Name%type,
                                    v_creator       in BILL_OM_OUTSTOCK.Creator%type,
                                    strOutMsg       out varchar2)
  
   IS
  
    v_direct_cow_old     VARCHAR2(1000); -- 上一成单条件
    v_direct_cow_new     VARCHAR2(1000); -- 当前成单条件
    v_no                 VARCHAR2(30);
    v_nExists            NUMBER := 0;
    v_N_COUNT            NUMBER(10); --记录数
    v_row_no             NUMBER(10); -- 客户编码对应的流道号
    v_is_hava            NUMBER(10); -- 在下架定位表的数据
    v_is_diff            NUMBER(10); -- 发货通知单计划数量和定位数量不相等的数据
    v_exp_status         VARCHAR2(30); -- 发货通知单的状态；
    v_status_description VARCHAR2(200); -- 状态描述
    v_direct_sum         NUMBER;
    v_operate_date       date;
    v_leave_num          NUMBER;
    v_leave_num_cur      NUMBER;
    v_work_num_in        NUMBER;
    v_num                NUMBER;
    v_while_num          NUMBER;
    v_add_qty                NUMBER;
    v_outstock_type VARCHAR2(1000);
  
  BEGIN
  
    strOutMsg        := 'N|[E00025]';
    v_direct_cow_old := 'N';
    v_direct_cow_new := 'N';
    v_no             := 'N';
    v_while_num      := 0;
    v_N_COUNT        := 0;
    v_add_qty        := 0;
  
    -- 写入临时表
    insert into SYS_OUTSTOCK_locate
      select distinct d.store_no, dense_rank() over(order by d.store_no)
        from bill_om_locate_dtl d
       where d.locate_no = v_locate_no;
    if sql%rowcount <= 0 then
      strOutMsg := 'N|写入临时表数据失败!';
      return;
    end if;
  
    -- 按库区发单
    -- 如果库区不为空,且客户编码为空
    if ((v_area_no is not null and v_area_no <> 'N') and
       (v_store_no is null or v_store_no = 'N')) then
    
      if v_operate_type = 'B' THEN--零散
         	
       
       --按商品排序分组
       if v_sort_type = '1' then
          
                    for direct_cord in 
                        (select od.* from BILL_OM_OUTSTOCK_DIRECT od
                         inner join cm_defcell d
                            on d.cell_no = od.s_cell_no
                           and od.locno = d.locno
                         INNER JOIN CM_DEFAREA CDA
                            ON d.LOCNO = CDA.LOCNO
                           AND d.WARE_NO = CDA.WARE_NO
                           and d.area_no = cda.area_no
                         where od.Exp_Type = v_exp_type
                           and od.operate_type = v_operate_type
                           and od.batch_no = v_batch_no
                           and od.locate_no = v_locate_no
                           and od.locno = v_locno
                           and od.status = '10'
                           and d.area_no = v_area_no
                           and od.item_qty - nvl(od.work_qty, 0) > 0
                         order by  od.item_no asc) loop
                             
                              if v_N_COUNT = 0 then
                                  
                                  -- 调用自动生成单号的存储过程
                                  PKG_WMS_BASE.proc_getsheetno(v_locno, 'HO', v_no, strOutMsg);
                                  
                                  -- 插入下架单主表  BILL_OM_OUTSTOCK
                                  insert into BILL_OM_OUTSTOCK
                                    (LOCNO,
                                     OUTSTOCK_NO,
                                     OUTSTOCK_TYPE,
                                     BATCH_NO,
                                     OPERATE_TYPE,
                                     Operate_Date,
                                     HANDOUT_DATE,
                                     PICK_TYPE,
                                     STATUS,
                                     PRIORITY,
                                     Creator,
                                     Createtm,
                                     OUTSTOCK_SEND_TYPE)
                                    SELECT direct_cord.locno,
                                           v_no,
                                           direct_cord.outstock_type,
                                           direct_cord.batch_no,
                                           direct_cord.operate_type,
                                           direct_cord.operate_date,
                                           sysdate,
                                           direct_cord.pick_type,
                                           '10',
                                           direct_cord.priority,
                                           v_creator,
                                           sysdate,
                                           '1'
                                      FROM DUAL;
                                      
                                   if sql%rowcount <= 0 then
                                      strOutMsg := 'N|新增拣货单主表失败!';
                                      return;
                                    end if;
                               
                              end if;
                              
                              -- 获取客户对应的流道编码
                              select row_no
                                into v_row_no
                                from SYS_OUTSTOCK_locate
                               where store_no = direct_cord.store_no;                    
                              -- 插入下架单明细表  BILL_OM_OUTSTOCK_DTL
                              insert into Bill_Om_Outstock_Dtl
                                (Locno,
                                 Owner_No,
                                 DIVIDE_ID,
                                 Outstock_No,
                                 Operate_Date,
                                 Batch_No,
                                 Exp_Type,
                                 Exp_No,
                                 Exp_Date,
                                 Locate_No,
                                 Store_No,
                                 Item_No,
                                 Item_Id,
                                 SCAN_LABEL_NO,
                                 s_Cell_No,
                                 s_Cell_Id,
                                 s_Container_No,
                                 d_cell_no,
                                 d_Cell_Id,
                                 PICK_CONTAINER_NO,
                                 STOCK_TYPE,
                                 DELIVER_AREA,
                                 PRIORITY,
                                 A_SORTER_CHUTE_NO,
                                 CHECK_CHUTE_NO,
                                 DELIVER_OBJ,
                                 ASSIGN_NAME,
                                 Item_Qty,
                                 PACK_QTY,
                                 SERIAL_NO,
                                 Size_No,
                                 brand_no)
                                SELECT direct_cord.locno,
                                       direct_cord.owner_no,
                                       direct_cord.direct_serial,
                                       v_no,
                                       direct_cord.operate_date,
                                       direct_cord.batch_no,
                                       direct_cord.exp_type,
                                       direct_cord.exp_no,
                                       direct_cord.exp_date,
                                       direct_cord.locate_no,
                                       direct_cord.store_no,
                                       direct_cord.item_no,
                                       direct_cord.item_id,
                                       direct_cord.scan_label_no,
                                       direct_cord.s_cell_no,
                                       direct_cord.s_cell_id,
                                       direct_cord.s_container_no,
                                       direct_cord.d_cell_no,
                                       direct_cord.d_cell_id,
                                       direct_cord.PICK_CONTAINER_NO,
                                       direct_cord.stock_type,
                                       direct_cord.deliver_area,
                                       direct_cord.priority,
                                       direct_cord.a_sorter_chute_no,
                                       direct_cord.check_chute_no,
                                       direct_cord.deliver_obj,
                                       v_pickingpeople,
                                       direct_cord.item_qty,
                                       direct_cord.pack_qty,
                                       v_row_no,
                                       direct_cord.size_no,
                                       direct_cord.brand_no
                                  FROM DUAL;
                            
                              if sql%rowcount <= 0 then
                                strOutMsg := 'N|插入下架单明细异常！';
                                return;
                              end if;
                              
                              -- 更新下架指示的状态 （13-已发单）
                              update BILL_OM_OUTSTOCK_DIRECT ak
                                 set ak.work_qty = ak.item_qty,
                                     ak.status = '13',
                                     ak.editor = v_creator,
                                     ak.edittm = sysdate
                               where ak.locno = direct_cord.locno
                                 and ak.owner_no = direct_cord.owner_no
                                 and ak.direct_serial = direct_cord.direct_serial
                                 and ak.operate_date = direct_cord.operate_date
                                 and ak.locate_no = direct_cord.locate_no;
                              
                              --不拆单：累加商品的数量大于或等于任务数量成一单
                              v_N_COUNT := v_N_COUNT + 1; 
                              v_add_qty := v_add_qty + direct_cord.item_qty;                             
                              if v_add_qty >= v_work_num
                                then
                                  v_N_COUNT := 0;
                                  v_add_qty := 0;
                              end if;     
                                            
                    end loop;                            
       
          else---按储位分组排序
          
               select ceil(sum(nvl(od.item_qty, 0)) / v_work_num)
                into v_direct_sum
                from BILL_OM_OUTSTOCK_DIRECT od
               inner join cm_defcell d
                  on d.cell_no = od.s_cell_no
                 and od.locno = d.locno
               INNER JOIN CM_DEFAREA CDA
                  ON d.LOCNO = CDA.LOCNO
                 AND d.WARE_NO = CDA.WARE_NO
                 and d.area_no = cda.area_no
               where od.Exp_Type = v_exp_type
                 and od.operate_type = v_operate_type
                 and od.batch_no = v_batch_no
                 and od.locate_no = v_locate_no
                 and od.locno = v_locno
                 and od.status = '10'
                 and d.area_no = v_area_no;
              if v_direct_sum < 1 then
                strOutMsg := 'N|未找到对应的储位信息,请核实!';
                RETURN;
              END if;
              
              select od.outstock_type, od.operate_date
                into v_outstock_type, v_operate_date
                from BILL_OM_OUTSTOCK_DIRECT od
               inner join cm_defcell d
                  on d.cell_no = od.s_cell_no
                 and od.locno = d.locno
               INNER JOIN CM_DEFAREA CDA
                  ON d.LOCNO = CDA.LOCNO
                 AND d.WARE_NO = CDA.WARE_NO
                 and d.area_no = cda.area_no
               where od.Exp_Type = v_exp_type
                 and od.operate_type = v_operate_type
                 and od.batch_no = v_batch_no
                 and od.locate_no = v_locate_no
                 and od.locno = v_locno
                 and od.status = '10'
                 and d.area_no = v_area_no
                 and rownum = 1;
            
              if v_direct_sum > 0 then
                while 0 < v_direct_sum loop
                  v_while_num := v_while_num + 1;
                  -- 调用自动生成单号的存储过程
                  PKG_WMS_BASE.proc_getsheetno(v_locno, 'HO', v_no, strOutMsg);
                
                  if instr(strOutMsg, 'N', 1, 1) = 1 then
                    return;
                  end if;
                
                  IF v_no = 'N' THEN
                    strOutMsg := 'N|自动生成单号错误！';
                    return;
                  END IF;
                
                  -- 插入下架单主表  BILL_OM_OUTSTOCK
                  insert into BILL_OM_OUTSTOCK
                    (LOCNO,
                     OUTSTOCK_NO,
                     OUTSTOCK_TYPE,
                     BATCH_NO,
                     OPERATE_TYPE,
                     Operate_Date,
                     HANDOUT_DATE,
                     STATUS,
                     Creator,
                     Createtm,
                     OUTSTOCK_SEND_TYPE)
                    SELECT v_locno,
                           v_no,
                           v_outstock_type,
                           v_batch_no,
                           v_operate_type,
                           v_operate_date,
                           sysdate,
                           '10',
                           v_creator,
                           sysdate,
                           '1'
                      FROM DUAL;
                      
                   if sql%rowcount <= 0 then
                      strOutMsg := 'N|新增拣货单主表失败!';
                      return;
                    end if;
                
                  v_leave_num := 0;
                  v_num       := 0;
                  v_add_qty   := 0;
                
                  for direct_cord in (select od.*,
                                       (od.item_qty - nvl(od.work_qty, 0)) as IO_QTY
                                        from BILL_OM_OUTSTOCK_DIRECT od
                                       inner join cm_defcell d
                                          on d.cell_no = od.s_cell_no
                                         and od.locno = d.locno
                                       INNER JOIN CM_DEFAREA CDA
                                          ON d.LOCNO = CDA.LOCNO
                                         AND d.WARE_NO = CDA.WARE_NO
                                         and d.area_no = cda.area_no
                                       where od.Exp_Type = v_exp_type
                                         and od.operate_type = v_operate_type
                                         and od.batch_no = v_batch_no
                                         and od.locate_no = v_locate_no
                                         and od.locno = v_locno
                                         and od.status = '10'
                                         and d.area_no = v_area_no
                                         and od.item_qty - nvl(od.work_qty, 0) > 0
                                       order by od.locno,
                                                od.owner_no,
                                                od.locate_no,
                                                od.s_cell_no asc) loop
                  
                    v_work_num_in := 0;
                  
                    if v_num = 0 then
                      v_leave_num := v_work_num - direct_cord.io_qty;
                      if v_leave_num >= 0 then
                        v_work_num_in := direct_cord.io_qty;
                      else
                        v_work_num_in := v_work_num;
                      end if;
                    else
                      v_leave_num := v_leave_num - direct_cord.io_qty;
                      IF v_leave_num >= 0 THEN
                        v_work_num_in := direct_cord.io_qty;
                      else
                        v_work_num_in := v_leave_num + direct_cord.io_qty;
                      END IF;
                    end if;
                    
                    v_add_qty := v_add_qty + v_work_num_in;
                  
                    -- 获取客户对应的流道编码
                    select row_no
                      into v_row_no
                      from SYS_OUTSTOCK_locate
                     where store_no = direct_cord.store_no;
                  
                    -- 插入下架单明细表  BILL_OM_OUTSTOCK_DTL
                    insert into Bill_Om_Outstock_Dtl
                      (Locno,
                       Owner_No,
                       DIVIDE_ID,
                       Outstock_No,
                       Operate_Date,
                       Batch_No,
                       Exp_Type,
                       Exp_No,
                       Exp_Date,
                       Locate_No,
                       Store_No,
                       Item_No,
                       Item_Id,
                       SCAN_LABEL_NO,
                       s_Cell_No,
                       s_Cell_Id,
                       s_Container_No,
                       d_cell_no,
                       d_Cell_Id,
                       PICK_CONTAINER_NO,
                       STOCK_TYPE,
                       DELIVER_AREA,
                       PRIORITY,
                       A_SORTER_CHUTE_NO,
                       CHECK_CHUTE_NO,
                       DELIVER_OBJ,
                       ASSIGN_NAME,
                       Item_Qty,
                       PACK_QTY,
                       SERIAL_NO,
                       Size_No,
                       brand_no)
                      SELECT direct_cord.locno,
                             direct_cord.owner_no,
                             direct_cord.direct_serial,
                             v_no,
                             direct_cord.operate_date,
                             direct_cord.batch_no,
                             direct_cord.exp_type,
                             direct_cord.exp_no,
                             direct_cord.exp_date,
                             direct_cord.locate_no,
                             direct_cord.store_no,
                             direct_cord.item_no,
                             direct_cord.item_id,
                             direct_cord.scan_label_no,
                             direct_cord.s_cell_no,
                             direct_cord.s_cell_id,
                             direct_cord.s_container_no,
                             direct_cord.d_cell_no,
                             direct_cord.d_cell_id,
                             direct_cord.PICK_CONTAINER_NO,
                             direct_cord.stock_type,
                             direct_cord.deliver_area,
                             direct_cord.priority,
                             direct_cord.a_sorter_chute_no,
                             direct_cord.check_chute_no,
                             direct_cord.deliver_obj,
                             v_pickingpeople,
                             v_work_num_in,
                             direct_cord.pack_qty,
                             v_row_no,
                             direct_cord.size_no,
                             direct_cord.brand_no
                        FROM DUAL;
                  
                    if sql%rowcount <= 0 then
                      strOutMsg := 'N|插入下架单明细异常！';
                      return;
                    end if;
                  
                    -- 更新下架指示的数量
                    update BILL_OM_OUTSTOCK_DIRECT ak
                       set ak.work_qty = ak.work_qty + v_work_num_in,
                           ak.editor   = v_creator,
                           ak.edittm   = sysdate
                     where ak.locno = direct_cord.locno
                       and ak.owner_no = direct_cord.owner_no
                       and ak.direct_serial = direct_cord.direct_serial
                       and ak.operate_date = direct_cord.operate_date;
                  
                    if sql%rowcount <= 0 then
                      strOutMsg := 'N|更新下架指示的分配数量异常！';
                      return;
                    end if;
                  
                    v_num := v_num + 1;
                  
                    /*--如果这单装完 跳出循环
                    if v_leave_num <= 0 or v_while_num = v_direct_sum then
                      exit;
                    end if;*/
                    
                    if v_add_qty = v_work_num
                      then
                        exit;
                    end if;
                  
                  end loop;
                  
                  --如果这单装完 跳出循环
                  if v_while_num = v_direct_sum then
                      exit;
                  end if;
                
                end loop;
              
                -- 更新下架指示的状态 （13-已发单）
                update BILL_OM_OUTSTOCK_DIRECT ak
                   set ak.status = case
                                     when (select nvl(d.item_qty, 0)
                                             from BILL_OM_OUTSTOCK_DIRECT d
                                            where d.locno = ak.locno
                                              and d.owner_no = ak.owner_no
                                              and d.locate_no = ak.locate_no
                                              and d.direct_serial = ak.direct_serial
                                              and d.operate_date = ak.operate_date) =
                                          nvl(ak.work_qty, 0) then
                                      '13'
                                     else
                                      ak.status
                                   end
                 where ak.Exp_Type = v_exp_type
                   and ak.operate_type = v_operate_type
                   and ak.batch_no = v_batch_no
                   and ak.locate_no = v_locate_no
                   and ak.locno = v_locno
                   and ak.status = '10';
              end if;               
                      
          
       end if; 
       
        
      
      
      
      elsif v_operate_type = 'C' THEN --整箱
       
        select ceil(sum(count(distinct od.scan_label_no)) / v_work_num)
          into v_direct_sum
          from BILL_OM_OUTSTOCK_DIRECT od
         inner join cm_defcell d
            on d.cell_no = od.s_cell_no
           and od.locno = d.locno
         INNER JOIN CM_DEFAREA CDA
            ON d.LOCNO = CDA.LOCNO
           AND d.WARE_NO = CDA.WARE_NO
           and d.area_no = cda.area_no
         where od.Exp_Type = v_exp_type
           and od.operate_type = v_operate_type
           and od.batch_no = v_batch_no
           and od.locate_no = v_locate_no
           and od.locno = v_locno
           and od.status = '10'
           and d.area_no = v_area_no
         group by od.scan_label_no;
        if v_direct_sum < 1 then
          strOutMsg := 'N|未找到对应的储位信息,请核实!';
          RETURN;
        END if;
        
        select od.outstock_type, od.operate_date
          into v_outstock_type, v_operate_date
          from BILL_OM_OUTSTOCK_DIRECT od
         inner join cm_defcell d
            on d.cell_no = od.s_cell_no
           and od.locno = d.locno
         INNER JOIN CM_DEFAREA CDA
            ON d.LOCNO = CDA.LOCNO
           AND d.WARE_NO = CDA.WARE_NO
           and d.area_no = cda.area_no
         where od.Exp_Type = v_exp_type
           and od.operate_type = v_operate_type
           and od.batch_no = v_batch_no
           and od.locate_no = v_locate_no
           and od.locno = v_locno
           and od.status = '10'
           and d.area_no = v_area_no
           and rownum = 1;
      
        while 0 < v_direct_sum loop
        
          v_num       := 0;
          v_while_num := v_while_num + 1;
        
          -- 调用自动生成单号的存储过程
          PKG_WMS_BASE.proc_getsheetno(v_locno, 'HO', v_no, strOutMsg);
        
          if instr(strOutMsg, 'N', 1, 1) = 1 then
            return;
          end if;
        
          IF v_no = 'N' THEN
            strOutMsg := 'N|自动生成单号错误！';
            return;
          END IF;
        
          -- 插入下架单主表  BILL_OM_OUTSTOCK
          insert into BILL_OM_OUTSTOCK
            (LOCNO,
             OUTSTOCK_NO,
             OUTSTOCK_TYPE,
             BATCH_NO,
             OPERATE_TYPE,
             Operate_Date,
             HANDOUT_DATE,
             STATUS,
             Creator,
             Createtm,
             OUTSTOCK_SEND_TYPE)
            SELECT v_locno,
                   v_no,
                   '0',
                   v_batch_no,
                   v_operate_type,
                   v_operate_date,
                   sysdate,
                   '10',
                   v_creator,
                   sysdate,
                   '1'
              FROM DUAL;
        
          for v_box in (select od.locno, od.owner_no, od.scan_label_no,od.s_cell_no
                          from BILL_OM_OUTSTOCK_DIRECT od
                         inner join cm_defcell d
                            on d.cell_no = od.s_cell_no
                           and od.locno = d.locno
                         INNER JOIN CM_DEFAREA CDA
                            ON d.LOCNO = CDA.LOCNO
                           AND d.WARE_NO = CDA.WARE_NO
                           and d.area_no = cda.area_no
                         where od.Exp_Type = v_exp_type
                           and od.operate_type = v_operate_type
                           and od.batch_no = v_batch_no
                           and od.locate_no = v_locate_no
                           and od.locno = v_locno
                           and od.status = '10'
                           and d.area_no = v_area_no
                         group by od.locno, od.owner_no, 
                         od.scan_label_no,od.s_cell_no
                         order by od.s_cell_no asc) loop
          
            v_num := v_num + 1;
          
            for direct_cord in (select od.*
                                  from BILL_OM_OUTSTOCK_DIRECT od
                                 inner join cm_defcell d
                                    on d.cell_no = od.s_cell_no
                                   and od.locno = d.locno
                                 INNER JOIN CM_DEFAREA CDA
                                    ON d.LOCNO = CDA.LOCNO
                                   AND d.WARE_NO = CDA.WARE_NO
                                   and d.area_no = cda.area_no
                                 where od.Exp_Type = v_exp_type
                                   and od.operate_type = v_operate_type
                                   and od.batch_no = v_batch_no
                                   and od.locate_no = v_locate_no
                                   and od.locno = v_locno
                                   and od.status = '10'
                                   and d.area_no = v_area_no
                                   and od.scan_label_no =
                                       v_box.scan_label_no) loop
            
              -- 获取客户对应的流道编码
              select row_no
                into v_row_no
                from SYS_OUTSTOCK_locate
               where store_no = direct_cord.store_no;
            
              -- 插入下架单明细表  BILL_OM_OUTSTOCK_DTL
              insert into Bill_Om_Outstock_Dtl
                (Locno,
                 Owner_No,
                 DIVIDE_ID,
                 Outstock_No,
                 Operate_Date,
                 Batch_No,
                 Exp_Type,
                 Exp_No,
                 Exp_Date,
                 Locate_No,
                 Store_No,
                 Item_No,
                 Item_Id,
                 SCAN_LABEL_NO,
                 s_Cell_No,
                 s_Cell_Id,
                 s_Container_No,
                 d_cell_no,
                 d_Cell_Id,
                 PICK_CONTAINER_NO,
                 STOCK_TYPE,
                 DELIVER_AREA,
                 PRIORITY,
                 A_SORTER_CHUTE_NO,
                 CHECK_CHUTE_NO,
                 DELIVER_OBJ,
                 ASSIGN_NAME,
                 Item_Qty,
                 PACK_QTY,
                 SERIAL_NO,
                 Size_No,
                 brand_no)
                SELECT direct_cord.locno,
                       direct_cord.owner_no,
                       direct_cord.direct_serial,
                       v_no,
                       direct_cord.operate_date,
                       direct_cord.batch_no,
                       direct_cord.exp_type,
                       direct_cord.exp_no,
                       direct_cord.exp_date,
                       direct_cord.locate_no,
                       direct_cord.store_no,
                       direct_cord.item_no,
                       direct_cord.item_id,
                       direct_cord.scan_label_no,
                       direct_cord.s_cell_no,
                       direct_cord.s_cell_id,
                       direct_cord.s_container_no,
                       direct_cord.d_cell_no,
                       direct_cord.d_cell_id,
                       direct_cord.PICK_CONTAINER_NO,
                       direct_cord.stock_type,
                       direct_cord.deliver_area,
                       direct_cord.priority,
                       direct_cord.a_sorter_chute_no,
                       direct_cord.check_chute_no,
                       direct_cord.deliver_obj,
                       v_pickingpeople,
                       direct_cord.item_qty,
                       direct_cord.pack_qty,
                       v_row_no,
                       direct_cord.size_no,
                       direct_cord.brand_no
                  FROM DUAL;
            
              if sql%rowcount <= 0 then
                strOutMsg := 'N|插入下架单明细异常！';
                return;
              end if;
            
              -- 更新下架指示的状态 （13-已发单）
              update BILL_OM_OUTSTOCK_DIRECT ak
                 set ak.work_qty = ak.item_qty,
                     ak.status = '13',
                     ak.editor = v_creator,
                     ak.edittm = sysdate
               where ak.locno = direct_cord.locno
                 and ak.owner_no = direct_cord.owner_no
                 and ak.direct_serial = direct_cord.direct_serial
                 and ak.operate_date = direct_cord.operate_date
                 and ak.locate_no = direct_cord.locate_no;
            
            end loop;
          
            if v_num = v_work_num then
              exit;
            end if;
          
          end loop;
          
          --循环生成明细的次数=成单数跳出循环
          if v_while_num = v_direct_sum
            then
              exit;
          end if;
        
        end loop;
      
      ELSE
        strOutMsg := 'N|操作类型为空!';
        return;
      END if;
      
    --------------------------------按客户-------------------------
    
    else
          
    
        if v_operate_type = 'B' THEN--零散
                  
                 --按商品排序分组
                 if v_sort_type = '1' then
                                
                       
                         for direct_cord in 
                              	(select od.*
                                    from BILL_OM_OUTSTOCK_DIRECT od
                                   inner join cm_defcell d
                                      on d.cell_no = od.s_cell_no
                                     and od.locno = d.locno
                                   INNER JOIN CM_DEFAREA CDA
                                      ON d.LOCNO = CDA.LOCNO
                                     AND d.WARE_NO = CDA.WARE_NO
                                     and d.area_no = cda.area_no
                                   where od.Exp_Type = v_exp_type
                                     and od.operate_type = v_operate_type
                                     and od.batch_no = v_batch_no
                                     and od.locate_no = v_locate_no
                                     and od.locno = v_locno
                                     and od.status = '10'
                                     and od.store_no = v_store_no
                                     and od.item_qty - nvl(od.work_qty, 0) > 0
                                   order by od.item_no asc) loop
                                   
                                    if v_N_COUNT = 0 then
                                        
                                        -- 调用自动生成单号的存储过程
                                        PKG_WMS_BASE.proc_getsheetno(v_locno, 'HO', v_no, strOutMsg);
                                        
                                        -- 插入下架单主表  BILL_OM_OUTSTOCK
                                        insert into BILL_OM_OUTSTOCK
                                          (LOCNO,
                                           OUTSTOCK_NO,
                                           OUTSTOCK_TYPE,
                                           BATCH_NO,
                                           OPERATE_TYPE,
                                           Operate_Date,
                                           HANDOUT_DATE,
                                           PICK_TYPE,
                                           STATUS,
                                           PRIORITY,
                                           Creator,
                                           Createtm,
                                           OUTSTOCK_SEND_TYPE)
                                          SELECT direct_cord.locno,
                                                 v_no,
                                                 direct_cord.outstock_type,
                                                 direct_cord.batch_no,
                                                 direct_cord.operate_type,
                                                 direct_cord.operate_date,
                                                 sysdate,
                                                 direct_cord.pick_type,
                                                 '10',
                                                 direct_cord.priority,
                                                 v_creator,
                                                 sysdate,
                                                 '0'
                                            FROM DUAL;
                                            
                                         if sql%rowcount <= 0 then
                                            strOutMsg := 'N|新增拣货单主表失败!';
                                            return;
                                          end if;
                                     
                                    end if;
                                    
                                    -- 获取客户对应的流道编码
                                    select row_no
                                      into v_row_no
                                      from SYS_OUTSTOCK_locate
                                     where store_no = direct_cord.store_no;                    
                                    -- 插入下架单明细表  BILL_OM_OUTSTOCK_DTL
                                    insert into Bill_Om_Outstock_Dtl
                                      (Locno,
                                       Owner_No,
                                       DIVIDE_ID,
                                       Outstock_No,
                                       Operate_Date,
                                       Batch_No,
                                       Exp_Type,
                                       Exp_No,
                                       Exp_Date,
                                       Locate_No,
                                       Store_No,
                                       Item_No,
                                       Item_Id,
                                       SCAN_LABEL_NO,
                                       s_Cell_No,
                                       s_Cell_Id,
                                       s_Container_No,
                                       d_cell_no,
                                       d_Cell_Id,
                                       PICK_CONTAINER_NO,
                                       STOCK_TYPE,
                                       DELIVER_AREA,
                                       PRIORITY,
                                       A_SORTER_CHUTE_NO,
                                       CHECK_CHUTE_NO,
                                       DELIVER_OBJ,
                                       ASSIGN_NAME,
                                       Item_Qty,
                                       PACK_QTY,
                                       SERIAL_NO,
                                       Size_No,
                                       brand_no)
                                      SELECT direct_cord.locno,
                                             direct_cord.owner_no,
                                             direct_cord.direct_serial,
                                             v_no,
                                             direct_cord.operate_date,
                                             direct_cord.batch_no,
                                             direct_cord.exp_type,
                                             direct_cord.exp_no,
                                             direct_cord.exp_date,
                                             direct_cord.locate_no,
                                             direct_cord.store_no,
                                             direct_cord.item_no,
                                             direct_cord.item_id,
                                             direct_cord.scan_label_no,
                                             direct_cord.s_cell_no,
                                             direct_cord.s_cell_id,
                                             direct_cord.s_container_no,
                                             direct_cord.d_cell_no,
                                             direct_cord.d_cell_id,
                                             direct_cord.PICK_CONTAINER_NO,
                                             direct_cord.stock_type,
                                             direct_cord.deliver_area,
                                             direct_cord.priority,
                                             direct_cord.a_sorter_chute_no,
                                             direct_cord.check_chute_no,
                                             direct_cord.deliver_obj,
                                             v_pickingpeople,
                                             direct_cord.item_qty,
                                             direct_cord.pack_qty,
                                             v_row_no,
                                             direct_cord.size_no,
                                             direct_cord.brand_no
                                        FROM DUAL;
                                  
                                    if sql%rowcount <= 0 then
                                      strOutMsg := 'N|插入下架单明细异常！';
                                      return;
                                    end if;
                                    
                                    -- 更新下架指示的状态 （13-已发单）
                                    update BILL_OM_OUTSTOCK_DIRECT ak
                                       set ak.work_qty = ak.item_qty,
                                           ak.status = '13',
                                           ak.editor = v_creator,
                                           ak.edittm = sysdate
                                     where ak.locno = direct_cord.locno
                                       and ak.owner_no = direct_cord.owner_no
                                       and ak.direct_serial = direct_cord.direct_serial
                                       and ak.operate_date = direct_cord.operate_date
                                       and ak.locate_no = direct_cord.locate_no;
                                    
                                    --不拆单：累加商品的数量大于或等于任务数量成一单
                                    v_N_COUNT := v_N_COUNT + 1; 
                                    v_add_qty := v_add_qty + direct_cord.item_qty;                             
                                    if v_add_qty >= v_work_num
                                      then
                                        v_N_COUNT := 0;
                                        v_add_qty := 0;
                                    end if;     
                                                  
                          end loop; 
                                
                 
                    else --按储位排序
                         
                         
                         select ceil(sum(nvl(od.item_qty, 0)) / v_work_num)
                          into v_direct_sum
                          from BILL_OM_OUTSTOCK_DIRECT od
                         inner join cm_defcell d
                            on d.cell_no = od.s_cell_no
                           and od.locno = d.locno
                         INNER JOIN CM_DEFAREA CDA
                            ON d.LOCNO = CDA.LOCNO
                           AND d.WARE_NO = CDA.WARE_NO
                           and d.area_no = cda.area_no
                         where od.Exp_Type = v_exp_type
                           and od.operate_type = v_operate_type
                           and od.batch_no = v_batch_no
                           and od.locate_no = v_locate_no
                           and od.locno = v_locno
                           and od.status = '10'
                           and od.store_no = v_store_no;
                        if v_direct_sum < 1 then
                          strOutMsg := 'N|未找到对应的储位信息,请核实!';
                          RETURN;
                        END if;
                        
                        select od.outstock_type,od.operate_date
                          into v_outstock_type,v_operate_date
                          from BILL_OM_OUTSTOCK_DIRECT od
                         inner join cm_defcell d
                            on d.cell_no = od.s_cell_no
                           and od.locno = d.locno
                         INNER JOIN CM_DEFAREA CDA
                            ON d.LOCNO = CDA.LOCNO
                           AND d.WARE_NO = CDA.WARE_NO
                           and d.area_no = cda.area_no
                         where od.Exp_Type = v_exp_type
                           and od.operate_type = v_operate_type
                           and od.batch_no = v_batch_no
                           and od.locate_no = v_locate_no
                           and od.locno = v_locno
                           and od.status = '10'
                           and od.store_no = v_store_no
                           and rownum = 1;
                      
                        if v_direct_sum > 0 then
                          while 0 < v_direct_sum loop
                            v_while_num := v_while_num + 1;
                            
                            -- 调用自动生成单号的存储过程
                            PKG_WMS_BASE.proc_getsheetno(v_locno, 'HO', v_no, strOutMsg);
                          
                            if instr(strOutMsg, 'N', 1, 1) = 1 then
                              return;
                            end if;
                          
                            IF v_no = 'N' THEN
                              strOutMsg := 'N|自动生成单号错误！';
                              return;
                            END IF;
                          
                            -- 插入下架单主表  BILL_OM_OUTSTOCK
                            insert into BILL_OM_OUTSTOCK
                              (LOCNO,
                               OUTSTOCK_NO,
                               OUTSTOCK_TYPE,
                               BATCH_NO,
                               OPERATE_TYPE,
                               Operate_Date,
                               HANDOUT_DATE,
                               STATUS,
                               Creator,
                               Createtm,
                               OUTSTOCK_SEND_TYPE)
                              SELECT v_locno,
                                     v_no,
                                     v_outstock_type,
                                     v_batch_no,
                                     v_operate_type,
                                     v_operate_date,
                                     sysdate,
                                     '10',
                                     v_creator,
                                     sysdate,
                                     '0'
                                FROM DUAL;
                              if sql%rowcount <= 0 then
                                strOutMsg := 'N|新增拣货单主表失败';
                                return;
                              end if;
                          
                            v_leave_num := 0;
                            v_num       := 0;
                            v_add_qty   := 0;
                          
                            for direct_cord in (select od.*,
                                                 (od.item_qty - nvl(od.work_qty, 0)) as IO_QTY
                                                  from BILL_OM_OUTSTOCK_DIRECT od
                                                 inner join cm_defcell d
                                                    on d.cell_no = od.s_cell_no
                                                   and od.locno = d.locno
                                                 INNER JOIN CM_DEFAREA CDA
                                                    ON d.LOCNO = CDA.LOCNO
                                                   AND d.WARE_NO = CDA.WARE_NO
                                                   and d.area_no = cda.area_no
                                                 where od.Exp_Type = v_exp_type
                                                   and od.operate_type = v_operate_type
                                                   and od.batch_no = v_batch_no
                                                   and od.locate_no = v_locate_no
                                                   and od.locno = v_locno
                                                   and od.status = '10'
                                                   and od.store_no = v_store_no
                                                   and od.item_qty - nvl(od.work_qty, 0) > 0
                                                 order by od.locno,
                                                          od.owner_no,
                                                          od.locate_no,
                                                          od.s_cell_no asc) loop
                            
                              v_work_num_in := 0;
                            
                              if v_num = 0 then
                                v_leave_num := v_work_num - direct_cord.io_qty;
                                if v_leave_num >= 0 then
                                  v_work_num_in := direct_cord.io_qty;
                                else
                                  v_work_num_in := v_work_num;
                                end if;
                              else
                                v_leave_num := v_leave_num - direct_cord.io_qty;
                                IF v_leave_num >= 0 THEN
                                  v_work_num_in := direct_cord.io_qty;
                                else
                                  v_work_num_in := v_leave_num + direct_cord.io_qty;
                                END IF;
                              end if;
                              
                              v_add_qty := v_add_qty + v_work_num_in;
                              
                            
                              -- 获取客户对应的流道编码
                              select row_no
                                into v_row_no
                                from SYS_OUTSTOCK_locate
                               where store_no = direct_cord.store_no;
                            
                              -- 插入下架单明细表  BILL_OM_OUTSTOCK_DTL
                              insert into Bill_Om_Outstock_Dtl
                                (Locno,
                                 Owner_No,
                                 DIVIDE_ID,
                                 Outstock_No,
                                 Operate_Date,
                                 Batch_No,
                                 Exp_Type,
                                 Exp_No,
                                 Exp_Date,
                                 Locate_No,
                                 Store_No,
                                 Item_No,
                                 Item_Id,
                                 SCAN_LABEL_NO,
                                 s_Cell_No,
                                 s_Cell_Id,
                                 s_Container_No,
                                 d_cell_no,
                                 d_Cell_Id,
                                 PICK_CONTAINER_NO,
                                 STOCK_TYPE,
                                 DELIVER_AREA,
                                 PRIORITY,
                                 A_SORTER_CHUTE_NO,
                                 CHECK_CHUTE_NO,
                                 DELIVER_OBJ,
                                 ASSIGN_NAME,
                                 Item_Qty,
                                 PACK_QTY,
                                 SERIAL_NO,
                                 Size_No,
                                 brand_no)
                                SELECT direct_cord.locno,
                                       direct_cord.owner_no,
                                       direct_cord.direct_serial,
                                       v_no,
                                       direct_cord.operate_date,
                                       direct_cord.batch_no,
                                       direct_cord.exp_type,
                                       direct_cord.exp_no,
                                       direct_cord.exp_date,
                                       direct_cord.locate_no,
                                       direct_cord.store_no,
                                       direct_cord.item_no,
                                       direct_cord.item_id,
                                       direct_cord.scan_label_no,
                                       direct_cord.s_cell_no,
                                       direct_cord.s_cell_id,
                                       direct_cord.s_container_no,
                                       direct_cord.d_cell_no,
                                       direct_cord.d_cell_id,
                                       direct_cord.PICK_CONTAINER_NO,
                                       direct_cord.stock_type,
                                       direct_cord.deliver_area,
                                       direct_cord.priority,
                                       direct_cord.a_sorter_chute_no,
                                       direct_cord.check_chute_no,
                                       direct_cord.deliver_obj,
                                       v_pickingpeople,
                                       v_work_num_in,
                                       direct_cord.pack_qty,
                                       v_row_no,
                                       direct_cord.size_no,
                                       direct_cord.brand_no
                                  FROM DUAL;
                            
                              if sql%rowcount <= 0 then
                                strOutMsg := 'N|插入下架单明细异常！';
                                return;
                              end if;
                            
                              -- 更新下架指示的数量
                              update BILL_OM_OUTSTOCK_DIRECT ak
                                 set ak.work_qty = ak.work_qty + v_work_num_in,
                                     ak.editor   = v_creator,
                                     ak.edittm   = sysdate
                               where ak.locno = direct_cord.locno
                                 and ak.owner_no = direct_cord.owner_no
                                 and ak.direct_serial = direct_cord.direct_serial
                                 and ak.operate_date = direct_cord.operate_date;
                            
                              if sql%rowcount <= 0 then
                                strOutMsg := 'N|更新下架指示的分配数量异常！';
                                return;
                              end if;
                            
                              v_num := v_num + 1;
                            
                            /*  --如果这单装完 跳出循环
                              if v_leave_num <= 0 or v_while_num = v_direct_sum then
                                exit;
                              end if;*/
                              
                              if v_add_qty = v_work_num
                                then
                                  exit;
                              end if;
                            
                            end loop;
                                        
                            --如果这单装完 跳出循环
                            if v_while_num = v_direct_sum then
                                exit;
                            end if;
                          
                          end loop;
                        
                          -- 更新下架指示的状态 （13-已发单）
                          update BILL_OM_OUTSTOCK_DIRECT ak
                             set ak.status = case
                                               when (select nvl(d.item_qty, 0)
                                                       from BILL_OM_OUTSTOCK_DIRECT d
                                                      where d.locno = ak.locno
                                                        and d.owner_no = ak.owner_no
                                                        and d.locate_no = ak.locate_no
                                                        and d.direct_serial = ak.direct_serial
                                                        and d.operate_date = ak.operate_date) =
                                                    nvl(ak.work_qty, 0) then
                                                '13'
                                               else
                                                ak.status
                                             end
                           where ak.Exp_Type = v_exp_type
                             and ak.operate_type = v_operate_type
                             and ak.batch_no = v_batch_no
                             and ak.locate_no = v_locate_no
                             and ak.locno = v_locno
                             and ak.status = '10';
                        end if;
                              
                 
                 end if;
      
        
      
      elsif v_operate_type = 'C' THEN --整箱
       
      
        select ceil(sum(count(distinct od.scan_label_no)) / v_work_num)
          into v_direct_sum
          from BILL_OM_OUTSTOCK_DIRECT od
         inner join cm_defcell d
            on d.cell_no = od.s_cell_no
           and od.locno = d.locno
         INNER JOIN CM_DEFAREA CDA
            ON d.LOCNO = CDA.LOCNO
           AND d.WARE_NO = CDA.WARE_NO
           and d.area_no = cda.area_no
         where od.Exp_Type = v_exp_type
           and od.operate_type = v_operate_type
           and od.batch_no = v_batch_no
           and od.locate_no = v_locate_no
           and od.locno = v_locno
           and od.status = '10'
           and od.store_no = v_store_no
         group by od.scan_label_no;
        if v_direct_sum < 1 then
          strOutMsg := 'N|未找到对应的储位信息,请核实!';
          RETURN;
        END if;
        
         select od.outstock_type,od.operate_date
          into v_outstock_type,v_operate_date
          from BILL_OM_OUTSTOCK_DIRECT od
         inner join cm_defcell d
            on d.cell_no = od.s_cell_no
           and od.locno = d.locno
         INNER JOIN CM_DEFAREA CDA
            ON d.LOCNO = CDA.LOCNO
           AND d.WARE_NO = CDA.WARE_NO
           and d.area_no = cda.area_no
         where od.Exp_Type = v_exp_type
           and od.operate_type = v_operate_type
           and od.batch_no = v_batch_no
           and od.locate_no = v_locate_no
           and od.locno = v_locno
           and od.status = '10'
           and od.store_no = v_store_no
           and rownum = 1;
        
      
        while 0 < v_direct_sum loop
        
          v_num       := 0;
          v_while_num := v_while_num + 1;
        
          -- 调用自动生成单号的存储过程
          PKG_WMS_BASE.proc_getsheetno(v_locno, 'HO', v_no, strOutMsg);
        
          if instr(strOutMsg, 'N', 1, 1) = 1 then
            return;
          end if;
        
          IF v_no = 'N' THEN
            strOutMsg := 'N|自动生成单号错误！';
            return;
          END IF;
        
          -- 插入下架单主表  BILL_OM_OUTSTOCK
          insert into BILL_OM_OUTSTOCK
            (LOCNO,
             OUTSTOCK_NO,
             OUTSTOCK_TYPE,
             BATCH_NO,
             OPERATE_TYPE,
             Operate_Date,
             HANDOUT_DATE,
             STATUS,
             Creator,
             Createtm,
             OUTSTOCK_SEND_TYPE)
            SELECT v_locno,
                   v_no,
                   v_outstock_type,
                   v_batch_no,
                   v_operate_type,
                   v_operate_date,
                   sysdate,
                   '10',
                   v_creator,
                   sysdate,
                   '0'
              FROM DUAL;
        
          for v_box in (select od.locno, od.owner_no,od.scan_label_no,od.s_cell_no
                          from BILL_OM_OUTSTOCK_DIRECT od
                         inner join cm_defcell d
                            on d.cell_no = od.s_cell_no
                           and od.locno = d.locno
                         INNER JOIN CM_DEFAREA CDA
                            ON d.LOCNO = CDA.LOCNO
                           AND d.WARE_NO = CDA.WARE_NO
                           and d.area_no = cda.area_no
                         where od.Exp_Type = v_exp_type
                           and od.operate_type = v_operate_type
                           and od.batch_no = v_batch_no
                           and od.locate_no = v_locate_no
                           and od.locno = v_locno
                           and od.status = '10'
                           and od.store_no = v_store_no
                         group by od.locno, od.owner_no, od.scan_label_no,od.s_cell_no
                         order by od.s_cell_no asc) loop
          
            v_num := v_num + 1;
          
            for direct_cord in (select od.*
                                  from BILL_OM_OUTSTOCK_DIRECT od
                                 inner join cm_defcell d
                                    on d.cell_no = od.s_cell_no
                                   and od.locno = d.locno
                                 INNER JOIN CM_DEFAREA CDA
                                    ON d.LOCNO = CDA.LOCNO
                                   AND d.WARE_NO = CDA.WARE_NO
                                   and d.area_no = cda.area_no
                                 where od.Exp_Type = v_exp_type
                                   and od.operate_type = v_operate_type
                                   and od.batch_no = v_batch_no
                                   and od.locate_no = v_locate_no
                                   and od.locno = v_locno
                                   and od.status = '10'
                                   and od.store_no = v_store_no
                                   and od.scan_label_no =v_box.scan_label_no) loop
            
              -- 获取客户对应的流道编码
              select row_no
                into v_row_no
                from SYS_OUTSTOCK_locate
               where store_no = direct_cord.store_no;
            
              -- 插入下架单明细表  BILL_OM_OUTSTOCK_DTL
              insert into Bill_Om_Outstock_Dtl
                (Locno,
                 Owner_No,
                 DIVIDE_ID,
                 Outstock_No,
                 Operate_Date,
                 Batch_No,
                 Exp_Type,
                 Exp_No,
                 Exp_Date,
                 Locate_No,
                 Store_No,
                 Item_No,
                 Item_Id,
                 SCAN_LABEL_NO,
                 s_Cell_No,
                 s_Cell_Id,
                 s_Container_No,
                 d_cell_no,
                 d_Cell_Id,
                 PICK_CONTAINER_NO,
                 STOCK_TYPE,
                 DELIVER_AREA,
                 PRIORITY,
                 A_SORTER_CHUTE_NO,
                 CHECK_CHUTE_NO,
                 DELIVER_OBJ,
                 ASSIGN_NAME,
                 Item_Qty,
                 PACK_QTY,
                 SERIAL_NO,
                 Size_No,
                 brand_no)
                SELECT direct_cord.locno,
                       direct_cord.owner_no,
                       direct_cord.direct_serial,
                       v_no,
                       direct_cord.operate_date,
                       direct_cord.batch_no,
                       direct_cord.exp_type,
                       direct_cord.exp_no,
                       direct_cord.exp_date,
                       direct_cord.locate_no,
                       direct_cord.store_no,
                       direct_cord.item_no,
                       direct_cord.item_id,
                       direct_cord.scan_label_no,
                       direct_cord.s_cell_no,
                       direct_cord.s_cell_id,
                       direct_cord.s_container_no,
                       direct_cord.d_cell_no,
                       direct_cord.d_cell_id,
                       direct_cord.PICK_CONTAINER_NO,
                       direct_cord.stock_type,
                       direct_cord.deliver_area,
                       direct_cord.priority,
                       direct_cord.a_sorter_chute_no,
                       direct_cord.check_chute_no,
                       direct_cord.deliver_obj,
                       v_pickingpeople,
                       direct_cord.item_qty,
                       direct_cord.pack_qty,
                       v_row_no,
                       direct_cord.size_no,
                       direct_cord.brand_no
                  FROM DUAL;
            
              if sql%rowcount <= 0 then
                strOutMsg := 'N|插入下架单明细异常！';
                return;
              end if;
            
              -- 更新下架指示的状态 （13-已发单）
              update BILL_OM_OUTSTOCK_DIRECT ak
                 set ak.work_qty = ak.item_qty,
                     ak.status = '13',
                     ak.editor = v_creator,
                     ak.edittm = sysdate
               where ak.locno = direct_cord.locno
                 and ak.owner_no = direct_cord.owner_no
                 and ak.direct_serial = direct_cord.direct_serial
                 and ak.operate_date = direct_cord.operate_date
                 and ak.locate_no = direct_cord.locate_no;
            
            end loop;
          
            if v_num = v_work_num then
              exit;
            end if;
          
          end loop;
          
          --循环的数量=成单数量 停止循环
          if v_while_num = v_direct_sum
            then
              exit;
          end if;
        
        end loop;
      
      ELSE
        strOutMsg := 'N|操作类型为空!';
        return;
      END if;       
                     
    end if;
    
    
    -- 遍历发货通知单号，然后更改对应的状态为已调度或部分调度
    for R_EXP_NO  in(select od.exp_no
                     from BILL_OM_OUTSTOCK_DIRECT od
                     where od.Exp_Type = v_exp_type
                           and od.operate_type = v_operate_type
                           and od.batch_no = v_batch_no
                           and od.locate_no = v_locate_no
                           and od.locno = v_locno
                           and od.status = '13') loop
               
          -- 新建状态的记录数有多少 
          select count(*)  into v_is_hava  from  BILL_OM_OUTSTOCK_DIRECT ood where ood.exp_no = R_EXP_NO.exp_no and ood.status < '18';
         
          -- 如果有，则说明是没有发单，则更改发货通知单的状态为已发单-18
          if  v_is_hava  > 0  then
              v_exp_status :='18';
              -- 更新发货通知单的状态，并记录状态日志
              update bill_om_exp p
                 set p.status = v_exp_status
               where p.exp_no = R_EXP_NO.exp_no
                 and p.status < '18';
                 
                 -- 如果更新到了记录，则说明状态变化了，需要记录日志
                if sql%rowcount > 0 then
                   PKG_COMMON_CITY.PROC_BILL_STATUS_LOG(v_locno,R_EXP_NO.exp_no,'OM',v_exp_status,'已发单',v_creator,strOutMsg);
                end if;
          end if;

    end loop;
    
    strOutMsg := 'Y';
    delete from SYS_OUTSTOCK_locate;
    
  end PROC_OM_OUTSTOCK_DIRECT;
end Pkg_OM_CITY_OUTSTOCKDIRECTWORK;
/
