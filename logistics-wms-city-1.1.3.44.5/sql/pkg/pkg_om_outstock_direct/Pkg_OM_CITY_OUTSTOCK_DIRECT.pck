CREATE OR REPLACE PACKAGE Pkg_OM_CITY_OUTSTOCK_DIRECT IS

 /*****************************************************************************************
   功能：城市仓-拣货任务分派发单
   create By  zuo.sw AT 2013-10-16 17:18:00
   Modify By
  *****************************************************************************************/
 
PROCEDURE PROC_OM_OUTSTOCK_DIRECT (v_exp_type IN BILL_OM_OUTSTOCK_DIRECT.Exp_Type%TYPE,
  v_direct_serial IN VARCHAR2,
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

END  Pkg_OM_CITY_OUTSTOCK_DIRECT;
/
CREATE OR REPLACE PACKAGE BODY Pkg_OM_CITY_OUTSTOCK_DIRECT IS

  /*****************************************************************************************
   功能：城市仓-拣货任务分派发单
   create By  zuo.sw AT 2013-10-16 17:18:00
   Modify By
  *****************************************************************************************/
  PROCEDURE PROC_OM_OUTSTOCK_DIRECT(v_exp_type      IN BILL_OM_OUTSTOCK_DIRECT.Exp_Type%TYPE,
                                    v_direct_serial IN VARCHAR2,
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
  
    v_direct_cow_old VARCHAR2(1000); -- 上一成单条件
    v_direct_cow_new VARCHAR2(1000); -- 当前成单条件
    v_no             VARCHAR2(30);
    v_nExists NUMBER := 0;
    v_N_COUNT       NUMBER(10); --记录数
    v_row_no        NUMBER(10); -- 客户编码对应的流道号
    v_is_hava       NUMBER(10);-- 在下架定位表的数据
    v_is_diff       NUMBER(10);-- 发货通知单计划数量和定位数量不相等的数据
    v_exp_status    VARCHAR2(30); -- 发货通知单的状态；
    v_status_description  VARCHAR2(200); -- 状态描述
    v_DIRECT_sum    NUMBER(10);
  
  BEGIN
    strOutMsg        := 'N|[E00025]';
    v_direct_cow_old := 'N';
    v_direct_cow_new := 'N';
    v_no             := 'N';
  
    -- 校验客户编码或库区是否有选择
    IF ((v_area_no is null or v_area_no ='N') and
       (v_store_no is null or v_store_no='N')) THEN
      strOutMsg := 'N|请先选择客户或库区！';
      return;
    END IF;
    
    select count(*) into v_N_COUNT
      from BILL_OM_OUTSTOCK_DIRECT od
         inner join sys_crtpaper_item c
          on od.outstock_type = c.outstock_type
          and od.operate_type = c.operate_type
    where od.Exp_Type = v_exp_type
           and od.operate_type = v_operate_type
           and od.batch_no = v_batch_no
           and od.locate_no = v_locate_no
           and od.locno = v_locno; 
           
    IF v_N_COUNT = 0 THEN
          strOutMsg := 'N|当前拣货波次'||v_locate_no||'下的拣货任务未找到对应的切单规则！';
          return;   
    END IF;     
   
    -- 写入临时表
    insert into SYS_OUTSTOCK_locate
      select distinct d.store_no, dense_rank() over(order by d.store_no) 
      from bill_om_locate_dtl d where d.locate_no=v_locate_no;
    if sql%rowcount <= 0 then
       strOutMsg := 'N|写入临时表数据失败!';
       return;
    end if;
   
    -- 按库区发单
    -- 如果库区不为空,且客户编码为空
    if ((v_area_no is not null and  v_area_no <> 'N') 
         and (v_store_no is null or v_store_no ='N')) then
       
        select count(*) into  v_DIRECT_sum
                             from BILL_OM_OUTSTOCK_DIRECT od
                             inner join sys_crtpaper_item c
                                on od.outstock_type = c.outstock_type
                               and od.operate_type = c.operate_type
                             inner join cm_defcell d
                                on d.cell_no = od.s_cell_no
                               and od.locno = d.locno
                               --and od.owner_no = d.owner_no
                             INNER JOIN CM_DEFAREA CDA
                                ON d.LOCNO = CDA.LOCNO
                               AND d.WARE_NO = CDA.WARE_NO
                               and d.area_no = cda.area_no
                               and cda.AREA_TYPE = c.AREA_TYPE
                             where od.Exp_Type = v_exp_type
                               and od.operate_type = v_operate_type
                               and od.batch_no = v_batch_no
                               and od.locate_no = v_locate_no
                               and od.locno = v_locno
                               and od.status ='10'
                               and d.area_no = v_area_no
                               AND instr(',' || v_direct_serial || ',',
                                       ',' || od.direct_serial || ',') > 0;
       if v_DIRECT_sum < 1 then
                strOutMsg := 'N|未找到对应的储位信息,请核实!';
                RETURN;
       END if;
        
 
      for direct_cord in (select od.*, c.ALLOT_RULE, c.BOX_FLAG
                           from BILL_OM_OUTSTOCK_DIRECT od
                           inner join sys_crtpaper_item c
                              on od.outstock_type = c.outstock_type
                             and od.operate_type = c.operate_type
                           inner join cm_defcell d
                              on d.cell_no = od.s_cell_no
                             and od.locno = d.locno
                             --and od.owner_no = d.owner_no
                           INNER JOIN CM_DEFAREA CDA
                              ON d.LOCNO = CDA.LOCNO
                             AND d.WARE_NO = CDA.WARE_NO
                             and d.area_no = cda.area_no
                             and cda.AREA_TYPE = c.AREA_TYPE
                           where od.Exp_Type = v_exp_type
                             and od.operate_type = v_operate_type
                             and od.batch_no = v_batch_no
                             and od.locate_no = v_locate_no
                             and od.locno = v_locno
                             and od.status ='10'
                             and d.area_no = v_area_no
                             AND instr(',' || v_direct_serial || ',',
                                       ',' || od.direct_serial || ',') > 0
                           order by od.locno,
                                    od.owner_no,
                                    od.locate_no,
                                    od.outstock_type,
                                    od.operate_type,
                                    od.pick_type,
                                    od.s_cell_no,
                                    od.scan_label_no,
                                    od.pack_qty,
                                    od.item_no) loop
         
        v_nExists := v_nExists + 1;                           
                                    
        -- 成单条件
        -- 如按仓别+波次+批次+出货类型+下架类型+作业类型+拣货类型+优先级+切单区域方式（切单规则表）+(如果拣货类型为摘果则加上客户条件)+切单计算方式)
        v_direct_cow_new := direct_cord.locno || direct_cord.locate_no ||
                            direct_cord.batch_no || direct_cord.exp_type ||
                            direct_cord.outstock_type ||
                            direct_cord.operate_type ||
                            direct_cord.pick_type || direct_cord.priority ||
                            direct_cord.allot_rule || direct_cord.box_flag;
                            
        -- 如果拣货类型为摘果，则加上客户条件
        IF direct_cord.pick_type = '0' THEN
          v_direct_cow_new := v_direct_cow_new || direct_cord.store_no;
        END IF;
        -- 如果按整箱发单，则加上箱号作为成单条件
        IF direct_cord.Operate_Type ='C' THEN
          v_direct_cow_new := v_direct_cow_new ||direct_cord.Scan_Label_No;
        END IF;
        
        -- 如果上一成单条件不等于当前的成单条件，则需要拆单，写入主表和明细表
        if v_direct_cow_new <> v_direct_cow_old then
        
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
                   sysdate,
                   direct_cord.pick_type,
                   '10',
                   direct_cord.priority,
                   v_creator,
                   sysdate
              FROM DUAL;
        
          if sql%rowcount <= 0 then
          
            strOutMsg := 'N|插入下架单主表异常！';
            return;
          end if;
        
        end if;
        
        -- 获取客户对应的流道编码
        select  row_no into v_row_no  from  SYS_OUTSTOCK_locate where store_no = direct_cord.store_no;
        
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
           Size_No)
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
                 direct_cord.size_no
            FROM DUAL;
      
        if sql%rowcount <= 0 then
          strOutMsg := 'N|插入下架单明细异常！';
          return;
        end if;
      
        -- 更新下架指示的状态 （13-已发单）
        update BILL_OM_OUTSTOCK_DIRECT ak
           set ak.status = '13', ak.editor = v_creator, ak.edittm = sysdate
         where ak.locno = direct_cord.locno
           and ak.owner_no = direct_cord.owner_no
           -- and ak.store_no = direct_cord.store_no
           and ak.direct_serial = direct_cord.direct_serial
           --and ak.s_cell_no = direct_cord.s_cell_no
           -- and ak.outstock_type = direct_cord.outstock_type
           -- and ak.operate_type = direct_cord.operate_type
           -- and ak.pick_type = direct_cord.pick_type
           --and ak.exp_type = direct_cord.exp_type
           --and ak.batch_no = direct_cord.batch_no
           --and ak.locate_no = direct_cord.locate_no
           --and ak.item_no = direct_cord.item_no
           --and ak.size_no = direct_cord.size_no
           and ak.operate_date = direct_cord.operate_date;
      
        if sql%rowcount <= 0 then
        
          strOutMsg := 'N|更新下架指示的状态异常！';
          return;
        end if;
      
        -- 当前的值赋值给前一个
        v_direct_cow_old := v_direct_cow_new;
      
      end loop;
      
      if  v_nExists < 1 then
         strOutMsg := 'N|根据库区分派拣货任务时，未找到对应的切单规则，请先添加！';
         return;
      end if;
      
    else  -- 按客户发单
      
    
     
        select count(*) into  v_DIRECT_sum
                            from BILL_OM_OUTSTOCK_DIRECT od
                           inner join sys_crtpaper_item c
                              on od.outstock_type = c.outstock_type
                             and od.operate_type = c.operate_type
                           inner join cm_defcell d
                              on d.cell_no = od.s_cell_no
                             and od.locno = d.locno
                             --and od.owner_no = d.owner_no
                           INNER JOIN CM_DEFAREA CDA
                              ON d.LOCNO = CDA.LOCNO
                             AND d.WARE_NO = CDA.WARE_NO
                             and d.area_no = cda.area_no
                             and cda.AREA_TYPE = c.AREA_TYPE
                           where od.Exp_Type = v_exp_type
                             and od.operate_type = v_operate_type
                             and od.batch_no = v_batch_no
                             and od.locate_no = v_locate_no
                             and od.locno = v_locno
                             and od.store_no = v_store_no
                             and od.status ='10'
                             AND instr(',' || v_direct_serial || ',',
                                       ',' || od.direct_serial || ',') > 0;
                             
       if v_DIRECT_sum < 1 then
                strOutMsg := 'N|未找到对应的储位信息,请核实!';
                RETURN;
       END if;
    
    
      -- 如果库区为空,且客户编码不为空
      for direct_cord in (select od.*, c.ALLOT_RULE, c.BOX_FLAG
                            from BILL_OM_OUTSTOCK_DIRECT od
                           inner join sys_crtpaper_item c
                              on od.outstock_type = c.outstock_type
                             and od.operate_type = c.operate_type
                           inner join cm_defcell d
                              on d.cell_no = od.s_cell_no
                             and od.locno = d.locno
                             --and od.owner_no = d.owner_no
                           INNER JOIN CM_DEFAREA CDA
                              ON d.LOCNO = CDA.LOCNO
                             AND d.WARE_NO = CDA.WARE_NO
                             and d.area_no = cda.area_no
                             and cda.AREA_TYPE = c.AREA_TYPE
                           where od.Exp_Type = v_exp_type
                             and od.operate_type = v_operate_type
                             and od.batch_no = v_batch_no
                             and od.locate_no = v_locate_no
                             and od.locno = v_locno
                             and od.store_no = v_store_no
                             and od.status ='10'
                             AND instr(',' || v_direct_serial || ',',
                                       ',' || od.direct_serial || ',') > 0
                           order by od.locno,
                                    od.owner_no,
                                    od.locate_no,
                                    od.outstock_type,
                                    od.operate_type,
                                    od.pick_type,
                                    od.s_cell_no,
                                    od.scan_label_no,
                                    od.pack_qty,
                                    od.item_no) loop
                                    
        v_nExists := v_nExists+1;                            
        -- 成单条件
        -- 如按仓别+波次+批次+出货类型+下架类型+作业类型+拣货类型+优先级+切单区域方式（切单规则表）+(如果拣货类型为摘果则加上客户条件)+切单计算方式)
        v_direct_cow_new := direct_cord.locno || direct_cord.locate_no ||
                            direct_cord.batch_no || direct_cord.exp_type ||
                            direct_cord.outstock_type ||
                            direct_cord.operate_type ||
                            direct_cord.pick_type || direct_cord.priority ||
                            direct_cord.allot_rule || direct_cord.box_flag;
                            
        -- 如果拣货类型为摘果，则加上客户条件
        IF direct_cord.pick_type = '0' THEN
          v_direct_cow_new := v_direct_cow_new || direct_cord.store_no;
        END IF;
        
        -- 如果按整箱发单，则加上箱号作为成单条件
        IF direct_cord.Operate_Type ='C' THEN
          v_direct_cow_new := v_direct_cow_new ||direct_cord.Scan_Label_No;
        END IF;
        
        -- 如果上一成单条件不等于当前的成单条件，则需要拆单，写入主表和明细表
        if v_direct_cow_new <> v_direct_cow_old then
        
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
                   sysdate,
                   direct_cord.pick_type,
                   '10',
                   direct_cord.priority,
                   v_creator,
                   sysdate
              FROM DUAL;
        
          if sql%rowcount <= 0 then
          
            strOutMsg := 'N|插入下架单主表异常！';
            return;
          end if;
        
        end if;
      
         -- 获取客户对应的流道编码
        select  row_no into v_row_no  from  SYS_OUTSTOCK_locate where store_no = direct_cord.store_no;
      
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
           Size_No)
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
                 direct_cord.size_no
            FROM DUAL;
      
        if sql%rowcount <= 0 then
        
          strOutMsg := 'N|插入下架单明细异常！';
          return;
        end if;
      
        -- 更新下架指示的状态 （13-已发单）
        update BILL_OM_OUTSTOCK_DIRECT ak
           set ak.status = '13', ak.editor = v_creator, ak.edittm = sysdate
         where ak.locno = direct_cord.locno
           and ak.owner_no = direct_cord.owner_no
           and ak.direct_serial = direct_cord.direct_serial
           and ak.operate_date = direct_cord.operate_date
           --and ak.store_no = direct_cord.store_no
           --and ak.s_cell_no = direct_cord.s_cell_no
           --and ak.outstock_type = direct_cord.outstock_type
           --and ak.operate_type = direct_cord.operate_type
           --and ak.pick_type = direct_cord.pick_type
           --and ak.exp_type = direct_cord.exp_type
           -- and ak.batch_no = direct_cord.batch_no
           -- and ak.item_no = direct_cord.item_no
           -- and ak.size_no = direct_cord.size_no
           and ak.locate_no = direct_cord.locate_no;
          
        if sql%rowcount <= 0 then
        
          strOutMsg := 'N|更新下架指示的状态异常！';
          return;
        end if;
      
        -- 当前的值赋值给前一个
        v_direct_cow_old := v_direct_cow_new;
      
      end loop;
      
       if  v_nExists < 1 then
         strOutMsg := 'N|根据客户编码分派拣货任务时，未找到对应的切单规则，请先添加！';
         return;
       end if;
    end if;
    
    
    -- 遍历发货通知单号，然后更改对应的状态为已调度或部分调度
    for R_EXP_NO  in(select od.exp_no
                     from BILL_OM_OUTSTOCK_DIRECT od
                     where od.Exp_Type = v_exp_type
                           and od.operate_type = v_operate_type
                           and od.batch_no = v_batch_no
                           and od.locate_no = v_locate_no
                           and od.locno = v_locno) loop
               
          -- 新建状态的记录数有多少 
          select count(*)  into v_is_hava  from  BILL_OM_OUTSTOCK_DIRECT ood where ood.exp_no = R_EXP_NO.exp_no and ood.status = '10';
          -- 如果有，则说明是部分调度，则更改发货通知单的状态为部分调度-16
          if  v_is_hava  > 0  then
               v_exp_status :='16';
          else -- 如果没有记录，再看下
              select count(*) into v_is_diff  from  bill_om_exp_dtl ed where ed.exp_no = R_EXP_NO.exp_no
                  and ed.locate_qty <  ed.item_qty;
              if  v_is_diff  > 0  then
                  v_exp_status :='16';
              else
                  v_exp_status :='17';
              end if;
          end if;
          
          -- 更新发货通知单的状态，并记录状态日志
          update  bill_om_exp  p 
               set p.status = v_exp_status
               where p.exp_no = R_EXP_NO.exp_no
               and p.status <> v_exp_status;
          
          if  v_exp_status = '16' then
              v_status_description :='部分调度';
          else
              v_status_description :='已调度';
          end if;
               
          -- 如果更新到了记录，则说明状态变化了，需要记录日志
          if sql%rowcount > 0 then
                PKG_COMMON_CITY.PROC_BILL_STATUS_LOG(v_locno,R_EXP_NO.exp_no,'OM',v_exp_status,v_status_description,v_creator,strOutMsg);
          end if;
          
    end loop;
    
    strOutMsg := 'Y';
  END PROC_OM_OUTSTOCK_DIRECT;

end Pkg_OM_CITY_OUTSTOCK_DIRECT;
/
