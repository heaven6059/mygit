CREATE OR REPLACE PACKAGE PKG_IM_CITY_CANCL_DIRECT IS

  --上架定位-取消
  -- Author  : zuo.sw
  -- Created : 2013-12-24
  -- Purpose :
  PROCEDURE PROC_CANCL_DIRECT(strlocno       IN bill_im_instock.locno%TYPE, --仓别
                               strowner      IN bill_im_instock.owner_no%TYPE, --委托业主
                               strsourceno  IN bill_im_instock.instock_no%TYPE, --来源单号
                               strflag       in VARCHAR2,-- 标记 R 表示收货，C 表示验收
                               stroutmsg    OUT VARCHAR2 --处理返回的结果
                               );

  -- 取消定位公用方法
  -- Author  : zuo.sw
  -- Created : 2013-12-24
  -- Purpose :
  PROCEDURE PROC_CANCL_DIRECT_PUBLIC(i_locno       IN bill_im_instock.locno%TYPE, --仓别
                               i_owner      IN bill_im_instock.owner_no%TYPE, --委托业主
                               i_source_no  IN bill_im_instock.instock_no%TYPE, --来源单号
                               strflag       in VARCHAR2,-- 标记 R 表示收货，C 表示验收
                               stroutmsg    OUT VARCHAR2 --处理返回的结果
                               );

  --上架定位-发单的校验逻辑
  -- Author  : zuo.sw
  -- Created : 2013-12-24
  -- Purpose :
  PROCEDURE PROC_IM_DIRECT_SEND_ORDER(strlocno       IN bill_im_instock.locno%TYPE, --仓别
                               strowner      IN bill_im_instock.owner_no%TYPE, --委托业主
                               strflag       in VARCHAR2,-- 标记 R 表示收货，C 表示验收
                               stroutmsg    OUT VARCHAR2 --处理返回的结果
                               );
  
   /*
    功能：入库上架任务发单
   作者：zuo.sw
   日期：2014-3-14
 */
 procedure Proc_im_send_order(strLocno            in bill_im_instock.LOCNO%type, --仓别
                                  strOwnerNo          in bill_im_instock.Owner_No%type, --货主
                                  strRowIdList        in varchar2, --rowid的集合
                                  strSender           in varchar2, --发单人
                                  strWorkerNo         in bill_im_instock.CREATOR%type, --操作人
                                  stroutmsg           out varchar2); --返回 执行结果

end  PKG_IM_CITY_CANCL_DIRECT;
/
CREATE OR REPLACE PACKAGE BODY PKG_IM_CITY_CANCL_DIRECT IS

  --上架定位-取消
  -- Author  : zuo.sw
  -- Created : 2013-12-24
  -- Purpose :
  PROCEDURE PROC_CANCL_DIRECT(strlocno       IN bill_im_instock.locno%TYPE, --仓别
                               strowner      IN bill_im_instock.owner_no%TYPE, --委托业主
                               strsourceno  IN bill_im_instock.instock_no%TYPE, --来源单号
                               strflag       in VARCHAR2,-- 标记 R 表示收货，C 表示验收
                               stroutmsg    OUT VARCHAR2 --处理返回的结果
                               ) IS

    v_check_no     bill_im_check.check_no%type; -- 验收单号
    v_is_f         number(10); -- 已经发单的明细的数量；
    v_status       bill_im_check.status%type;-- 状态
    STR_Outsdefine    BM_DEFBASE.SDEFINE%TYPE; --参数的字符串值
    STR_Outndefine    BM_DEFBASE.NDEFINE%TYPE; --参数的整型值

  BEGIN

    -- 获取系统配置表的参数设置的值
    PKG_WMS_BASE.proc_GetBasePara(strlocno,strowner,'instock_direct','IM','',STR_Outsdefine,STR_Outndefine,stroutmsg);
    if instr(stroutmsg, 'N', 1, 1) = 1 then
          stroutmsg := 'N|未获取到当前仓库的入库上架定位配置信息，请先检查配置！';
          return;
    end if;

    -- 验收单的逻辑
    if  strflag = 'C' and STR_Outndefine <> 1  then
          stroutmsg := 'N|该仓库设置的上架定位方式为按收货定位，不能进行验收单的取消定位操作！';
          return;
    end if;

    -- 收货单的逻辑
    if  strflag = 'R' and STR_Outndefine <> 0  then
          stroutmsg := 'N|该仓库设置的上架定位方式为按验收定位，不能进行收货单的取消定位操作！';
          return;
    end if;

    -- 校验是否有发单的单据
    select count(*) into  v_is_f from   bill_im_instock_direct d
    where d.source_no =strsourceno
        and d.locno=strlocno
        and d.owner_no = strowner
        and d.status = '13';
    if v_is_f > 0 then
        stroutmsg := 'N|已经操作了发单的单据不可以取消定位！';
        RETURN;
    end if;


    -- 验收单的逻辑
    if  strflag = 'C'  then

       select p.status into v_status  from   bill_im_check  p where p.check_no = strsourceno;
       if v_status  <> '30' then
            stroutmsg := 'N|当前状态下，不能进行取消定位的操作！';
            RETURN;
       end if;

       -- 更改验收单的状态 为25 验收完成
       update bill_im_check  y
       set y.status = '25'
       where y.check_no = strsourceno;
       IF SQL%ROWCOUNT <> 1 THEN
              stroutmsg := 'N|更新验收单'||v_check_no||'的状态时异常！';
              RETURN;
       END IF;

    else -- 收货单的逻辑

       select p.status into v_status  from   bill_im_receipt  p where p.receipt_no = strsourceno;
       if v_status  <> '30' then
            stroutmsg := 'N|当前状态下，不能进行取消定位的操作！';
            RETURN;
       end if;

       -- 更改收货单的状态 为20 收货完成
       update bill_im_receipt r
       set r.status = '20'
       where r.receipt_no = strsourceno;

       IF SQL%ROWCOUNT <> 1 THEN
              stroutmsg := 'N|更新收货单'||v_check_no||'的状态时异常！';
              RETURN;
       END IF;

       -- 根据收货单号，查询到对应的验收单号，删除对应的验收表数据
       select r.check_no into  v_check_no from  bill_im_check  r where r.s_import_no = strsourceno;

       -- 删除板明细
       delete  from  bill_im_check_pal  where CHECK_NO = v_check_no;
       IF SQL%ROWCOUNT = 0 THEN
              stroutmsg := 'N|删除验收单'||v_check_no||'对应的板明细时异常！';
              RETURN;
       END IF;

       -- 删除验收单表明细的数据
       delete  from  bill_im_check_dtl  where check_no = v_check_no;
       IF SQL%ROWCOUNT = 0 THEN
              stroutmsg := 'N|删除验收单'||v_check_no||'明细时异常！';
              RETURN;
       END IF;

       -- 删除验收单主表的数据
       delete  from  bill_im_check  where check_no = v_check_no;
       IF SQL%ROWCOUNT = 0 THEN
              stroutmsg := 'N|删除验收单'||v_check_no||'时异常！';
              RETURN;
       END IF;

    end if;

    -- 调用公用的存储过程，调整库存并删除定位表的数据；
    PKG_IM_CITY_CANCL_DIRECT.PROC_CANCL_DIRECT_PUBLIC(strlocno, strowner,strsourceno,strflag,stroutmsg);

    -- 如果返回N,则返回并给出提示
    if instr(stroutmsg, 'N', 1, 1) = 1 then
         return;
    end if;

    stroutmsg := 'Y|';

    EXCEPTION
    WHEN OTHERS THEN
      stroutmsg := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);

  END PROC_CANCL_DIRECT;


  -- 取消定位公用方法
  -- Author  : zuo.sw
  -- Created : 2013-12-24
  -- Purpose :
  PROCEDURE PROC_CANCL_DIRECT_PUBLIC(i_locno       IN bill_im_instock.locno%TYPE, --仓别
                               i_owner      IN bill_im_instock.owner_no%TYPE, --委托业主
                               i_source_no  IN bill_im_instock.instock_no%TYPE, --来源单号
                               strflag       in VARCHAR2,-- 标记 R 表示收货，C 表示验收
                               stroutmsg    OUT VARCHAR2 --处理返回的结果
                               ) IS
   v_strFlag   VARCHAR2(10);-- 标记 R 表示收货，C 表示验收
  BEGIN

       if strflag='R' then
          v_strFlag:='IR';
       else
          v_strFlag:='SC';
       end IF ;

    -- 根据来源编号查询
    for  R_DIRECT in(select d.locno,
                       d.owner_no,
                       d.LOCATE_TYPE,
                       d.cell_no,
                       d.cell_id,
                       d.row_id,
                       d.dest_cell_no,
                       d.dest_cell_id,
                       d.instock_qty,
                       d.item_no,
                       d.item_id,
                       d.size_no
                  from bill_im_instock_direct d
                 where d.source_no = i_source_no
                   and d.locno = i_locno
                   and d.owner_no = i_owner) loop

            -- 如果来源储位不为空，则回滚之前的库存数据（来源储位的预下数量加回去，库存数也加回去）
            if R_DIRECT.cell_no is not null  and  R_DIRECT.cell_no <> 'N'
                 and R_DIRECT.cell_id is not null and  R_DIRECT.cell_id > 0  then

                   --updt by crm 20140111 统一库存记账，减来源储位库存和预下量
                   --开始
                   ACC_PREPARE_DATA_EXT(i_source_no,v_strFlag,'O','',R_DIRECT.row_id,I_LOCNO => i_locno,I_OWNER_NO => R_DIRECT.owner_no,
                   I_CELL_ID =>R_DIRECT.cell_id,I_CELL_NO =>R_DIRECT.cell_no ,I_QTY=>0,I_OUTSTOCK_QTY =>-R_DIRECT.instock_qty  );
                   acc_apply(i_source_no,2,v_strFlag,'O',1);
                   --结束

            /*      update con_content cc
                  set cc.outstock_qty = nvl(cc.outstock_qty,0) - nvl(R_DIRECT.instock_qty,0)
                  where cc.cell_no = R_DIRECT.cell_no
                  and cc.cell_id = R_DIRECT.cell_id
                  and cc.locno = i_locno
                  and cc.qty >=0
                  and cc.outstock_qty >=0
                  and nvl(cc.outstock_qty,0) >= nvl(R_DIRECT.instock_qty,0)
                  and R_DIRECT.instock_qty >0;

                  IF SQL%ROWCOUNT = 0 THEN
                         stroutmsg := 'N|扣减来源储位'||R_DIRECT.cell_no||'的预下库存数量时未更新到数据！';
                         RETURN;
                  END IF;*/

            end if;

            -- 预上储位的合法性判断
            if R_DIRECT.dest_cell_no is  null  or  R_DIRECT.dest_cell_no = 'N'
                 or R_DIRECT.dest_cell_id is null or  R_DIRECT.dest_cell_id < 1  then
                  stroutmsg := 'N|预上储位的数据非法！';
                  RETURN;
            end if;

             --updt by crm 20140111 统一库存记账，减来源储位库存和预下量
             --开始
             ACC_PREPARE_DATA_EXT(i_source_no,v_strFlag,'O','',R_DIRECT.row_id,I_LOCNO => i_locno,I_OWNER_NO => R_DIRECT.owner_no,
             I_CELL_ID =>R_DIRECT.dest_cell_id,I_CELL_NO =>R_DIRECT.dest_cell_no ,I_QTY=>0,I_INSTOCK_QTY => -R_DIRECT.instock_qty );
             acc_apply(i_source_no,2,v_strFlag,'O',1);
             --结束


/*            -- 更新预上储位的预上数量，减回去
            update con_content cc
                  set cc.instock_qty = nvl(cc.instock_qty,0) - nvl(R_DIRECT.instock_qty,0)
                  where cc.cell_no = R_DIRECT.dest_cell_no
                  and cc.cell_id = R_DIRECT.dest_cell_id
                  and cc.locno = i_locno
                  and cc.owner_no = i_owner
                  and cc.instock_qty >= nvl(R_DIRECT.instock_qty,0)
                  and cc.qty >=0
                  and cc.instock_qty >=0
                  and R_DIRECT.instock_qty > 0;

            IF SQL%ROWCOUNT = 0 THEN
                         stroutmsg := 'N|扣减预上储位'||R_DIRECT.dest_cell_no||'；'||R_DIRECT.dest_cell_id||'的预上库存数量时未更新到数据！';
                         RETURN;
            END IF;
                       */
     end loop;

     -- 删除上架定位表的数据
     delete  from  bill_im_instock_direct
     where source_no = i_source_no
        and locno = i_locno
        and owner_no = i_owner;

    IF SQL%ROWCOUNT = 0 THEN
          stroutmsg := 'N|删除上架定位表的数据时异常！';
          RETURN;
    END IF;

  END PROC_CANCL_DIRECT_PUBLIC;



  --上架定位-发单的校验逻辑
  -- Author  : zuo.sw
  -- Created : 2013-12-24
  -- Purpose :
  PROCEDURE PROC_IM_DIRECT_SEND_ORDER(strlocno       IN bill_im_instock.locno%TYPE, --仓别
                               strowner      IN bill_im_instock.owner_no%TYPE, --委托业主
                               strflag       in VARCHAR2,-- 标记 R 表示收货，C 表示验收
                               stroutmsg    OUT VARCHAR2 --处理返回的结果
                               ) IS

    STR_Outsdefine    BM_DEFBASE.SDEFINE%TYPE; --参数的字符串值
    STR_Outndefine    BM_DEFBASE.NDEFINE%TYPE; --参数的整型值

  BEGIN

    -- 获取系统配置表的参数设置的值
    PKG_WMS_BASE.proc_GetBasePara(strlocno,strowner,'instock_direct','IM','',STR_Outsdefine,STR_Outndefine,stroutmsg);
    if instr(stroutmsg, 'N', 1, 1) = 1 then
          stroutmsg := 'N|未获取到当前仓库的入库上架定位配置信息，请先检查配置！';
          return;
    end if;

    -- 验收单的逻辑
    if  strflag = 'C' and STR_Outndefine <> 1  then
          stroutmsg := 'N|该仓库设置的上架定位方式为按收货定位，不能进行验收单的发单操作！';
          return;
    end if;

    -- 收货单的逻辑
    if  strflag = 'R' and STR_Outndefine <> 0  then
          stroutmsg := 'N|该仓库设置的上架定位方式为按验收定位，不能进行收货单的发单操作！';
          return;
    end if;

    stroutmsg := 'Y|';

    EXCEPTION
    WHEN OTHERS THEN
      stroutmsg := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);

  END PROC_IM_DIRECT_SEND_ORDER;
  
 /*
    功能：入库上架任务发单
   作者：zuo.sw
   日期：2014-3-14
 */
  procedure Proc_im_send_order( strLocno            in bill_im_instock.LOCNO%type, --仓别
                                    strOwnerNo          in bill_im_instock.Owner_No%type, --货主
                                    strRowIdList        in varchar2, --rowid的集合
                                    strSender           in varchar2, --发单人
                                    strWorkerNo         in bill_im_instock.CREATOR%type, --操作人
                                    stroutmsg           out varchar2)  as --返回 执行结果
   
      v_instock_no  bill_im_instock.Instock_No%type;-- 退仓上架单号
      v_row_num   number(10); --行号
      v_item_type con_content.item_type%type;
      v_quality   con_content.quality%type;
      v_no_type_qualiy  number(10); 
        
      begin
        
                -- 获取商品属性和品质
               select sum(countc) into v_no_type_qualiy  from  
               (select 1 as countc
                           from BILL_IM_INSTOCK_DIRECT bd
                          where bd.locno = strLocno
                            and bd.owner_no = strOwnerNo
                            and bd.status = '10'
                            AND instr(',' || strRowIdList || ',',
                                      ',' || bd.ROW_ID || ',') > 0
                            group by  bd.item_type,bd.quality) a;
                
               if v_no_type_qualiy > 1 then
                  stroutmsg := 'N|请选择相同属性和品质的明细进行发单!';
                  RETURN;
               end if;
               
               v_row_num := 1; -- row_id
      
                -- 调用自动生成单号的存储过程
                PKG_WMS_BASE.proc_getsheetno(strLocno, 'IP', v_instock_no, stroutmsg);
              
                IF instr(stroutmsg, 'N', 1, 1) = 1 THEN
                  RETURN;
                END IF;
              
                IF v_instock_no = 'N' THEN
                  stroutmsg := 'N|自动生成上架单号错误!';
                  RETURN;
                END IF;     
                
                -- 获取商品属性和品质
                select bd.item_type,bd.quality into v_item_type,v_quality
                           from BILL_IM_INSTOCK_DIRECT bd
                          where bd.locno = strLocno
                            and bd.owner_no = strOwnerNo
                            and bd.status = '10'
                            AND instr(',' || strRowIdList || ',',
                                      ',' || bd.ROW_ID || ',') > 0
                            and rownum =1;
                  
                
                for   R_task_detail  in(select bd.*
                           from BILL_IM_INSTOCK_DIRECT bd
                          where bd.locno = strLocno
                            and bd.owner_no = strOwnerNo
                            and bd.status = '10'
                            AND instr(',' || strRowIdList || ',',
                                      ',' || bd.ROW_ID || ',') > 0) loop
                                      
                    if v_row_num = 1 then
                      
                        -- 插入上架单主表
                        insert into BILL_IM_INSTOCK
                          (LOCNO, Owner_No, INSTOCK_NO, STATUS, Creator, CREATETM,OPERATE_TYPE,LOCATE_TYPE,Instock_Worker,DISPATCH_WORKER,
                          DISPATCH_DATE)
                        values
                          (strLocno,strOwnerNo,v_instock_no,'10',strWorkerNo,sysdate,'B','1',strSender,strSender,sysdate);
                          
                        -- 如果更新不到数据，则提示异常      
                        if sql%rowcount=0 then
                               stroutmsg:='N|插入上架单主表信息时异常！';
                               return;
                        end if;     
                        
                     end if;
                     
                     
                     -- 插入上架单明细表（多条）
                     insert into Bill_Im_Instock_Dtl
                       (Locno,
                        Owner_No,
                        Direct_Serial,
                        Instock_No,
                        instock_id,
                        Item_No,
                        Size_No,
                        brand_no,
                        Cell_No,
                        Cell_Id,
                        Dest_Cell_No,
                        Dest_Cell_Id,
                        Item_Qty,
                        Status,
                        source_no,
                        CONTAINER_NO,
                        PACK_QTY,
                        LABEL_NO)
                      values( R_task_detail.locno,
                              R_task_detail.owner_no,
                              R_task_detail.row_id,
                              v_instock_no,
                              v_row_num,
                              R_task_detail.item_no,
                              R_task_detail.size_no,
                              R_task_detail.brand_no,
                              R_task_detail.cell_no,
                              R_task_detail.cell_id,
                              R_task_detail.dest_cell_no,
                              R_task_detail.dest_cell_id,
                              R_task_detail.INSTOCK_QTY,
                              '10',
                              R_task_detail.source_no,
                              'N',
                              R_task_detail.pack_qty,
                              R_task_detail.label_no);
                         
                                              
                     -- 如果更新不到数据，则提示异常      
                     if sql%rowcount=0 then
                               stroutmsg:='N|插入上架单明细信息时异常！';
                               return;
                     end if;  
                    
                     v_row_num := v_row_num + 1;
                                     
                end loop;
                
             -- 更新上架任务单的状态为已发单
             update  BILL_IM_INSTOCK_DIRECT  udd 
             set udd.status = '13'
             where udd.locno = strLocno
             and udd.owner_no = strOwnerNo
             and udd.status <> '13'
             AND instr(',' || strRowIdList || ',',
                                       ',' || udd.ROW_ID || ',') > 0;
                                       
             -- 如果更新不到数据，则提示异常      
             if sql%rowcount=0 then
                     stroutmsg:='N|更新上架任务单为已发单时异常！';
                     return;
             end if; 
      
        
         stroutmsg := 'Y|';

      EXCEPTION
        WHEN OTHERS THEN
          stroutmsg := 'N|' || SQLERRM ||
                       SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
   END Proc_im_send_order;           

END PKG_IM_CITY_CANCL_DIRECT;
/
