create or replace package PKG_UM_CITY_INSTOCK_UNTREAD is

  /*
    功能：退仓-分配上架任务（没有通知单的情况）
   作者：zuo.sw
   日期：2014-1-13
  */
 procedure Proc_create_instock_task(strLocno           in bill_um_untread_mm.LOCNO%type, --仓别
                                      strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                      strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                      strCheckNoList      IN   VARCHAR2,-- 验收单号
                                      strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                      strResult           out varchar2); --返回 执行结果
                                      

  /*
    功能：退仓-分配上架任务(有通知单的情况)
   作者：zuo.sw
   日期：2014-1-13
  */
 procedure Proc_create_instock_task_mm(strLocno           in bill_um_untread_mm.LOCNO%type, --仓别
                                      strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                      strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                      strCheckNoList      IN   VARCHAR2,-- 验收单号
                                      strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                      strResult           out varchar2); --返回 执行结果                                      
                                
 /*
    功能：退仓-上架任务发单
   作者：zuo.sw
   日期：2014-1-13
 */
 procedure Proc_instock_task_send(strLocno            in bill_um_untread_mm.LOCNO%type, --仓别
                                  strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                  strRowIdList        in varchar2, --rowid的集合
                                  strSender           in varchar2, --发单人
                                  strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                  strResult           out varchar2); --返回 执行结果

end PKG_UM_CITY_INSTOCK_UNTREAD;
/
CREATE OR REPLACE PACKAGE BODY PKG_UM_CITY_INSTOCK_UNTREAD IS

    
  /*
    功能：退仓-分配上架任务（没有通知单的情况）
   作者：zuo.sw
   日期：2014-1-13
  */
  
  PROCEDURE Proc_create_instock_task(strLocno               in bill_um_untread_mm.LOCNO%type, --仓别
                                        strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                        strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                        strCheckNoList      IN   VARCHAR2,-- 验收单号
                                        strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                        STRRESULT    OUT VARCHAR2) AS
   
    STR_I            NUMBER(10); --定位行号
    v_item_type  Con_Content.item_type%type;  -- 商品类型
    v_quality    Con_Content.Quality%type;-- 品质
    v_PACK_QTY   Con_Content.Pack_Qty%type;-- 包装数量
    v_check_num  NUMBER(10);  -- 验收数量
    v_direct_num NUMBER(10);  -- 定位数量
    v_source_cell_id  NUMBER(10);  -- 来源储位ID
    v_instock_row_id  NUMBER(10);  -- 上架任务ROW_ID
    
  BEGIN
    
    -- 标记是否有部分数据没有做
    STR_I := 0;

    -- 获取商品属性,品质,包装数量
    select uu.untread_type,uu.quality,uu.pack_qty into v_item_type,v_quality,v_PACK_QTY  from BILL_UM_CHECK   uc 
      inner join  bill_um_untread  uu  on uc.untread_no = uu.untread_no
                  and uc.locno = uu.locno  and uc.owner_no = uu.owner_no
      where 1 =1   AND instr(',' || strCheckNoList || ',',
                              ',' || uc.check_no || ',') > 0
                   and rownum =1 ;  

    --循环验收表的数据
    FOR R_check_detail IN (  select cd.check_no,cd.box_no,cd.item_no,cd.size_no,cd.brand_no, b.cell_no, nvl(sum(cd.check_qty),0) root_qty 
                    from  BILL_UM_CHECK_DTL  cd  
                    left  join   con_box  b  on cd.box_no = b.box_no and cd.locno = b.locno and cd.owner_no = b.owner_no
                    where cd.locno = strLocno
                          AND instr(',' || strCheckNoList || ',',
                                    ',' || cd.check_no || ',') > 0
                    group by cd.check_no,cd.item_no,cd.size_no,cd.brand_no,b.cell_no,cd.box_no
                   ) LOOP
                   
       -- 判断来源储位是否为空
       if  R_check_detail.Cell_No is null or  R_check_detail.Cell_No = 'N'  then
           STRRESULT := 'N|商品编码:' || R_check_detail.Item_No  ||'，尺码:'||R_check_detail.Size_No||'未找到对应的储位信息,请核实!';
           RETURN;
       end if;            
                   
      --赋值给验收总数
      v_check_num := R_check_detail.root_qty;
      
      --循环定位表的数据
      FOR R_direct_detail IN (select ud.untread_mm_no,ud.item_no,ud.size_no,ud.cell_no,ud.cell_id,ud.row_id,
                                      nvl(sum(ud.EST_QTY - ud.DIS_QTY),0) as bak_qty
                                  from   BILL_UM_DIRECT  ud 
                                  where ud.locno= strLocno and ud.cell_no <> 'N' and ud.untread_mm_no = R_check_detail.check_no
                                  group by ud.untread_mm_no,ud.item_no,ud.size_no,ud.cell_no,ud.cell_id,ud.row_id ) LOOP
                                  
                      if   v_check_num = 0  then
                           exit;
                      end if;
                      
                      -- 如果商品编码和尺码都相等
                      if R_check_detail.Item_No||''||R_check_detail.Size_No = R_direct_detail.Item_No||''||R_direct_detail.Size_No then
                          
                            if v_check_num > R_direct_detail.Bak_Qty  then
                              
                                v_direct_num := R_direct_detail.Bak_Qty;
                              
                                v_check_num := v_check_num - R_direct_detail.Bak_Qty;   
                                
                                
                            else 
                             
                                v_direct_num := v_check_num;   
                            
                                v_check_num := 0 ;
                                
                            end if;
                            
                            if   v_direct_num < 1   then
                                STRRESULT := 'N|写上架任务时，预上数量非法!';
                                RETURN;
                            end if;
                              
                            -- 获取来源储位
                            BEGIN
                                  select  vc.cell_id into  v_source_cell_id  from  v_content vc 
                                  where vc.item_no = R_direct_detail.Item_No 
                                          and vc.size_no =R_direct_detail.Size_No 
                                          and vc.locno = strLocno 
                                          and vc.owner_no = strOwnerNo 
                                          and vc.pack_qty = v_PACK_QTY 
                                          and vc.item_type = v_item_type
                                          and vc.quality = v_quality
                                          and vc.cell_no = R_check_detail.Cell_No
                                          and rownum = 1 ;
                             EXCEPTION
                                  WHEN NO_DATA_FOUND THEN -- 如果没有找到合法的数据，则继续下一个循环；
                                  STRRESULT := 'N|商品编码:' || R_direct_detail.Item_No  ||'，尺码:'||R_direct_detail.Size_No||'，储位：'||R_check_detail.Cell_No||'找不到对应的库存记录,请核实!';
                                  RETURN;
                             END;
                             
                            /* if v_source_cell_id is null or  v_source_cell_id < 1
                                or R_direct_detail.Cell_No is null or  R_direct_detail.Cell_No = 'N' 
                                or R_direct_detail.Cell_Id  is null or  R_direct_detail.Cell_Id < 1 then
                                
                                  STRRESULT := 'N|插入上架任务时，储位数据非法!';
                                  RETURN;    
                                  
                             end if;*/
                             
                            -- 获取上架任务row_id 
                            select SEQ_UM_INSTOCK_DIRECT.Nextval into v_instock_row_id  FROM DUAL;
                            
                            -- 插入上架任务表
                            insert into BILL_UM_INSTOCK_DIRECT
                              (LOCNO,
                               owner_no,
                               ROW_ID,
                               STATUS,
                               ITEM_NO,
                               SIZE_NO,
                               CELL_NO,
                               CELL_ID,
                               DEST_CELL_NO,
                               DEST_CELL_ID,
                               --REAL_CELL_NO,
                               EST_QTY,
                               ITEM_TYPE,
                               QUALITY,
                               PACK_QTY,
                               source_no,
                               brand_no,
                               box_no)
                            values
                              (strLocno,
                              strOwnerNo,
                              v_instock_row_id,
                              '10',
                              R_check_detail.Item_No,
                              R_check_detail.Size_No,
                              R_check_detail.Cell_No,
                              v_source_cell_id,
                              R_direct_detail.Cell_No,
                              R_direct_detail.Cell_Id,
                              v_direct_num,
                              v_item_type,
                              v_quality,
                              v_PACK_QTY,
                              R_check_detail.Check_No,
                              R_check_detail.brand_no,
                              R_check_detail.Box_No);
                              
                             -- 如果没有插入数据成功，则提示异常      
                             if sql%rowcount=0 then
                                    STRRESULT:='N|新增上架任务时发生异常!';
                                    return;
                             end if;   
                             
                             -- 回写定位表的已分配数量
                             update  BILL_UM_DIRECT  ud 
                              set ud.dis_qty = nvl(ud.dis_qty,0) + v_direct_num
                              where ud.untread_mm_no = strCheckNoList 
                              and ud.locno = strLocno
                              and ud.row_id = R_direct_detail.row_id
                              and ud.est_qty >=  nvl(ud.dis_qty,0) + v_direct_num; 
                             
                             -- 如果没有插入数据成功，则提示异常      
                             if sql%rowcount=0 then
                                    STRRESULT:='N|回写定位的已分配数量时异常!';
                                    return;
                             end if;   
                                       
                      end if;
                             
             end loop;      
           

     -- 如果还有验收数量，还有大于0的，说明还有数据未能成功预约到储位
      if  v_check_num > 0  then
           STRRESULT := 'N|商品编码:' || R_check_detail.Item_No  ||'，尺码:'||R_check_detail.Size_No||' 未能全部预约到储位，请先预约!';
           return;
      end if;   
      
      -- 更新预到货通知单的分配数量
      -- update   BILL_UM_UNTREAD_MM_DTL
      
      STR_I := STR_I+1;
    
   end loop;
   
   if  STR_I < 1 then
       STRRESULT := 'N|未找到任何数据，请先检查!';
       return;
   end if;
   
   -- 回写退仓验收单的状态为已分配
   update BILL_UM_CHECK  cd  
   set cd.status ='25'
   where  cd.locno = strLocno 
       and cd.owner_no = strOwnerNo
       AND instr(',' || strCheckNoList || ',',
                ',' || cd.check_no || ',') > 0;
   
   -- 如果没有插入数据成功，则提示异常      
   if sql%rowcount=0 then
         STRRESULT:='N|更改退仓验收单为已分配时异常!';
         return;
   end if;   
   
   
  STRRESULT := 'Y|';

  EXCEPTION
    WHEN OTHERS THEN
      STRRESULT := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
  END Proc_create_instock_task;


 /*
    功能：退仓-分配上架任务(有通知单的情况)
   作者：zuo.sw
   日期：2014-1-13
  */
  
  PROCEDURE Proc_create_instock_task_mm(strLocno               in bill_um_untread_mm.LOCNO%type, --仓别
                                        strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                        strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                        strCheckNoList      IN   VARCHAR2,-- 验收单号
                                        strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                        STRRESULT    OUT VARCHAR2) AS
   
    STR_I            NUMBER(10); --定位行号
    v_item_type  Con_Content.item_type%type;  -- 商品类型
    v_quality    Con_Content.Quality%type;-- 品质
    v_PACK_QTY   Con_Content.Pack_Qty%type;-- 包装数量
    v_check_num  NUMBER(10);  -- 验收数量
    v_direct_num NUMBER(10);  -- 定位数量
    v_source_cell_id  NUMBER(10);  -- 来源储位ID
    v_instock_row_id  NUMBER(10);  -- 上架任务ROW_ID
    
  BEGIN
    
    -- 标记是否有部分数据没有做
    STR_I := 0;

    -- 获取商品属性,品质,包装数量
    select uu.untread_type,uu.quality,uu.pack_qty into v_item_type,v_quality,v_PACK_QTY  from BILL_UM_CHECK   uc 
      inner join  bill_um_untread  uu  on uc.untread_no = uu.untread_no
                  and uc.locno = uu.locno  and uc.owner_no = uu.owner_no
      where 1 =1   AND instr(',' || strCheckNoList || ',',
                              ',' || uc.check_no || ',') > 0
                   and rownum =1 ;  

    --循环验收表的数据
    FOR R_check_detail IN (  select cd.check_no,cd.box_no,cd.item_no,cd.size_no,cd.brand_no, b.cell_no, nvl(sum(cd.check_qty),0) root_qty 
                    from  BILL_UM_CHECK_DTL  cd  
                    left  join   con_box  b  on cd.box_no = b.box_no and cd.locno = b.locno and cd.owner_no = b.owner_no
                    where cd.locno = strLocno
                          AND instr(',' || strCheckNoList || ',',
                                    ',' || cd.check_no || ',') > 0
                    group by cd.check_no,cd.item_no,cd.size_no,cd.brand_no,b.cell_no,cd.box_no
                   ) LOOP
                   
       -- 判断来源储位是否为空
       if  R_check_detail.Cell_No is null or  R_check_detail.Cell_No = 'N'  then
           STRRESULT := 'N|商品编码:' || R_check_detail.Item_No  ||'，尺码:'||R_check_detail.Size_No||'未找到对应的储位信息,请核实!';
           RETURN;
       end if;            
                   
      --赋值给验收总数
      v_check_num := R_check_detail.root_qty;
      
      
     -- 如果有通知单号
     if strUntreadMMNo <> '-' and strUntreadMMNo <> 'N' and strUntreadMMNo is not null then
             --循环定位表的数据
             FOR R_direct_detail IN (select ud.untread_mm_no,ud.item_no,ud.size_no,ud.cell_no,ud.cell_id,ud.row_id,
                                      nvl(sum(ud.EST_QTY - ud.DIS_QTY),0) as bak_qty
                                  from   BILL_UM_DIRECT  ud 
                                  where ud.locno= strLocno and ud.cell_no <> 'N' and ud.untread_mm_no = strUntreadMMNo
                                  group by ud.untread_mm_no,ud.item_no,ud.size_no,ud.cell_no,ud.cell_id,ud.row_id ) LOOP
                                  
                      if   v_check_num = 0  then
                           exit;
                      end if;
                      
                      -- 如果商品编码和尺码都相等
                      if R_check_detail.Item_No||''||R_check_detail.Size_No = R_direct_detail.Item_No||''||R_direct_detail.Size_No then
                          
                            if v_check_num > R_direct_detail.Bak_Qty  then
                              
                                v_direct_num := R_direct_detail.Bak_Qty;
                              
                                v_check_num := v_check_num - R_direct_detail.Bak_Qty;   
                                
                                
                            else 
                             
                                v_direct_num := v_check_num;   
                            
                                v_check_num := 0 ;
                                
                            end if;
                            
                            if   v_direct_num < 1   then
                                STRRESULT := 'N|写上架任务时，预上数量非法!';
                                RETURN;
                            end if;
                              
                            -- 获取来源储位
                            BEGIN
                                  select  vc.cell_id into  v_source_cell_id  from  v_content vc 
                                  where vc.item_no = R_direct_detail.Item_No 
                                          and vc.size_no =R_direct_detail.Size_No 
                                          and vc.locno = strLocno 
                                          and vc.owner_no = strOwnerNo 
                                          and vc.pack_qty = v_PACK_QTY 
                                          and vc.item_type = v_item_type
                                          and vc.quality = v_quality
                                          and vc.cell_no = R_check_detail.Cell_No
                                          and rownum = 1 ;
                             EXCEPTION
                                  WHEN NO_DATA_FOUND THEN -- 如果没有找到合法的数据，则继续下一个循环；
                                  STRRESULT := 'N|商品编码:' || R_direct_detail.Item_No  ||'，尺码:'||R_direct_detail.Size_No||'，储位：'||R_check_detail.Cell_No||'找不到对应的库存记录,请核实!';
                                  RETURN;
                             END;
                             
                            /* if v_source_cell_id is null or  v_source_cell_id < 1
                                or R_direct_detail.Cell_No is null or  R_direct_detail.Cell_No = 'N' 
                                or R_direct_detail.Cell_Id  is null or  R_direct_detail.Cell_Id < 1 then
                                
                                  STRRESULT := 'N|插入上架任务时，储位数据非法!';
                                  RETURN;    
                                  
                             end if;*/
                             
                            -- 获取上架任务row_id 
                            select SEQ_UM_INSTOCK_DIRECT.Nextval into v_instock_row_id  FROM DUAL;
                            
                            -- 插入上架任务表
                            insert into BILL_UM_INSTOCK_DIRECT
                              (LOCNO,
                               owner_no,
                               ROW_ID,
                               STATUS,
                               ITEM_NO,
                               SIZE_NO,
                               CELL_NO,
                               CELL_ID,
                               DEST_CELL_NO,
                               DEST_CELL_ID,
                               --REAL_CELL_NO,
                               EST_QTY,
                               ITEM_TYPE,
                               QUALITY,
                               PACK_QTY,
                               source_no,
                               brand_no,
                               box_no)
                            values
                              (strLocno,
                              strOwnerNo,
                              v_instock_row_id,
                              '10',
                              R_check_detail.Item_No,
                              R_check_detail.Size_No,
                              R_check_detail.Cell_No,
                              v_source_cell_id,
                              R_direct_detail.Cell_No,
                              R_direct_detail.Cell_Id,
                              v_direct_num,
                              v_item_type,
                              v_quality,
                              v_PACK_QTY,
                              R_check_detail.Check_No,
                              R_check_detail.brand_no,
                              R_check_detail.Box_No);
                              
                             -- 如果没有插入数据成功，则提示异常      
                             if sql%rowcount=0 then
                                    STRRESULT:='N|新增上架任务时发生异常!';
                                    return;
                             end if;   
                             
                             -- 回写定位表的已分配数量
                             update  BILL_UM_DIRECT  ud 
                              set ud.dis_qty = nvl(ud.dis_qty,0) + v_direct_num
                              where ud.untread_mm_no = strUntreadMMNo 
                              and ud.locno = strLocno
                              and ud.row_id = R_direct_detail.row_id
                              and ud.est_qty >=  nvl(ud.dis_qty,0) + v_direct_num; 
                             
                             -- 如果没有插入数据成功，则提示异常      
                             if sql%rowcount=0 then
                                    STRRESULT:='N|回写定位的已分配数量时异常!';
                                    return;
                             end if;   
                                       
                      end if;
                             
             end loop;      
         
     else
             --循环定位表的数据
             FOR R_direct_detail IN (select ud.untread_mm_no,ud.item_no,ud.size_no,ud.cell_no,ud.cell_id,ud.row_id,
                                      nvl(sum(ud.EST_QTY - ud.DIS_QTY),0) as bak_qty
                                  from   BILL_UM_DIRECT  ud 
                                  where ud.locno= strLocno and ud.cell_no <> 'N' and ud.untread_mm_no = strCheckNoList
                                  group by ud.untread_mm_no,ud.item_no,ud.size_no,ud.cell_no,ud.cell_id,ud.row_id ) LOOP
                                  
                      if   v_check_num = 0  then
                           exit;
                      end if;
                      
                      -- 如果商品编码和尺码都相等
                      if R_check_detail.Item_No||''||R_check_detail.Size_No = R_direct_detail.Item_No||''||R_direct_detail.Size_No then
                          
                            if v_check_num > R_direct_detail.Bak_Qty  then
                              
                                v_direct_num := R_direct_detail.Bak_Qty;
                              
                                v_check_num := v_check_num - R_direct_detail.Bak_Qty;   
                                
                                
                            else 
                             
                                v_direct_num := v_check_num;   
                            
                                v_check_num := 0 ;
                                
                            end if;
                            
                            if   v_direct_num < 1   then
                                STRRESULT := 'N|写上架任务时，预上数量非法!';
                                RETURN;
                            end if;
                              
                            -- 获取来源储位
                            BEGIN
                                  select  vc.cell_id into  v_source_cell_id  from  v_content vc 
                                  where vc.item_no = R_direct_detail.Item_No 
                                          and vc.size_no =R_direct_detail.Size_No 
                                          and vc.locno = strLocno 
                                          and vc.owner_no = strOwnerNo 
                                          and vc.pack_qty = v_PACK_QTY 
                                          and vc.item_type = v_item_type
                                          and vc.quality = v_quality
                                          and vc.cell_no = R_check_detail.Cell_No
                                          and rownum = 1 ;
                             EXCEPTION
                                  WHEN NO_DATA_FOUND THEN -- 如果没有找到合法的数据，则继续下一个循环；
                                  STRRESULT := 'N|商品编码:' || R_direct_detail.Item_No  ||'，尺码:'||R_direct_detail.Size_No||'，储位：'||R_check_detail.Cell_No||'找不到对应的库存记录,请核实!';
                                  RETURN;
                             END;
                             
                            /* if v_source_cell_id is null or  v_source_cell_id < 1
                                or R_direct_detail.Cell_No is null or  R_direct_detail.Cell_No = 'N' 
                                or R_direct_detail.Cell_Id  is null or  R_direct_detail.Cell_Id < 1 then
                                
                                  STRRESULT := 'N|插入上架任务时，储位数据非法!';
                                  RETURN;    
                                  
                             end if;*/
                             
                            -- 获取上架任务row_id 
                            select SEQ_UM_INSTOCK_DIRECT.Nextval into v_instock_row_id  FROM DUAL;
                            
                            -- 插入上架任务表
                            insert into BILL_UM_INSTOCK_DIRECT
                              (LOCNO,
                               owner_no,
                               ROW_ID,
                               STATUS,
                               ITEM_NO,
                               SIZE_NO,
                               CELL_NO,
                               CELL_ID,
                               DEST_CELL_NO,
                               DEST_CELL_ID,
                               --REAL_CELL_NO,
                               EST_QTY,
                               ITEM_TYPE,
                               QUALITY,
                               PACK_QTY,
                               source_no,
                               brand_no,
                               box_no)
                            values
                              (strLocno,
                              strOwnerNo,
                              v_instock_row_id,
                              '10',
                              R_check_detail.Item_No,
                              R_check_detail.Size_No,
                              R_check_detail.Cell_No,
                              v_source_cell_id,
                              R_direct_detail.Cell_No,
                              R_direct_detail.Cell_Id,
                              v_direct_num,
                              v_item_type,
                              v_quality,
                              v_PACK_QTY,
                              R_check_detail.Check_No,
                              R_check_detail.brand_no,
                              R_check_detail.Box_No);
                              
                             -- 如果没有插入数据成功，则提示异常      
                             if sql%rowcount=0 then
                                    STRRESULT:='N|新增上架任务时发生异常!';
                                    return;
                             end if;   
                             
                             -- 回写定位表的已分配数量
                             update  BILL_UM_DIRECT  ud 
                              set ud.dis_qty = nvl(ud.dis_qty,0) + v_direct_num
                              where ud.untread_mm_no = strCheckNoList 
                              and ud.locno = strLocno
                              and ud.row_id = R_direct_detail.row_id
                              and ud.est_qty >=  nvl(ud.dis_qty,0) + v_direct_num; 
                             
                             -- 如果没有插入数据成功，则提示异常      
                             if sql%rowcount=0 then
                                    STRRESULT:='N|回写定位的已分配数量时异常!';
                                    return;
                             end if;   
                                       
                      end if;
                             
             end loop;      
     
     end if;
                          

     -- 如果还有验收数量，还有大于0的，说明还有数据未能成功预约到储位
      if  v_check_num > 0  then
           STRRESULT := 'N|商品编码:' || R_check_detail.Item_No  ||'，尺码:'||R_check_detail.Size_No||' 未能全部预约到储位，请先预约!';
           return;
      end if;   
      
      -- 更新预到货通知单的分配数量
      -- update   BILL_UM_UNTREAD_MM_DTL
      
      STR_I := STR_I+1;
    
   end loop;
   
   if  STR_I < 1 then
       STRRESULT := 'N|未找到任何数据，请先检查!';
       return;
   end if;
   
   -- 回写退仓验收单的状态为已分配
   update BILL_UM_CHECK  cd  
   set cd.status ='25'
   where  cd.locno = strLocno 
       and cd.owner_no = strOwnerNo
       AND instr(',' || strCheckNoList || ',',
                ',' || cd.check_no || ',') > 0;
   
   -- 如果没有插入数据成功，则提示异常      
   if sql%rowcount=0 then
         STRRESULT:='N|更改退仓验收单为已分配时异常!';
         return;
   end if;   
   
   
  STRRESULT := 'Y|';

  EXCEPTION
    WHEN OTHERS THEN
      STRRESULT := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
  END Proc_create_instock_task_mm;

  
 /*
    功能：退仓-上架任务发单
   作者：zuo.sw
   日期：2014-1-13
 */
  procedure Proc_instock_task_send( strLocno            in bill_um_untread_mm.LOCNO%type, --仓别
                                    strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                    strRowIdList        in varchar2, --rowid的集合
                                    strSender           in varchar2, --发单人
                                    strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                    strResult           out varchar2)  as --返回 执行结果
   
      v_instock_no  BILL_UM_INSTOCK.Instock_No%type;-- 退仓上架单号
      v_row_num   number(10); --行号
      v_item_type con_content.item_type%type;
      v_quality   con_content.quality%type;
      v_no_type_qualiy  number(10); 
        
      begin
        
                -- 获取商品属性和品质
               select sum(countc) into v_no_type_qualiy  from  
               (select 1 as countc
                           from BILL_UM_INSTOCK_DIRECT bd
                          where bd.locno = strLocno
                            and bd.owner_no = strOwnerNo
                            and bd.status = '10'
                            AND instr(',' || strRowIdList || ',',
                                      ',' || bd.ROW_ID || ',') > 0
                            group by  bd.item_type,bd.quality) a;
                
               if v_no_type_qualiy > 1 then
                  strResult := 'N|请选择相同属性和品质的明细进行发单!';
                  RETURN;
               end if;
               
               v_row_num := 1; -- row_id
      
                -- 调用自动生成单号的存储过程
                PKG_WMS_BASE.proc_getsheetno(strLocno, 'UP', v_instock_no, strResult);
              
                IF instr(strResult, 'N', 1, 1) = 1 THEN
                  RETURN;
                END IF;
              
                IF v_instock_no = 'N' THEN
                  strResult := 'N|自动生成上架单号错误!';
                  RETURN;
                END IF;     
                
                -- 获取商品属性和品质
                select bd.item_type,bd.quality into v_item_type,v_quality
                           from BILL_UM_INSTOCK_DIRECT bd
                          where bd.locno = strLocno
                            and bd.owner_no = strOwnerNo
                            and bd.status = '10'
                            AND instr(',' || strRowIdList || ',',
                                      ',' || bd.ROW_ID || ',') > 0
                            and rownum =1;
                  
                
                for   R_task_detail  in(select bd.*
                           from BILL_UM_INSTOCK_DIRECT bd
                          where bd.locno = strLocno
                            and bd.owner_no = strOwnerNo
                            and bd.status = '10'
                            AND instr(',' || strRowIdList || ',',
                                      ',' || bd.ROW_ID || ',') > 0) loop
                                      
                    if v_row_num = 1 then
                      
                        -- 插入上架单主表
                        insert into BILL_UM_INSTOCK
                          (LOCNO, Owner_No, INSTOCK_NO, STATUS, Creator, CREATETM,item_type,QUALITY,Instock_Worker)
                        values
                          (strLocno,strOwnerNo,v_instock_no,'10',strWorkerNo,sysdate,v_item_type,v_quality,strSender);
                          
                        -- 如果更新不到数据，则提示异常      
                        if sql%rowcount=0 then
                               STRRESULT:='N|插入上架单主表信息时异常！';
                               return;
                        end if;     
                        
                     end if;
                     
                     
                     -- 插入上架单明细表（多条）
                     insert into Bill_Um_Instock_Dtl
                       (Locno,
                        Owner_No,
                        Direct_Serial,
                        Instock_No,
                        instock_id,
                        Item_No,
                        Size_No,
                        Cell_No,
                        Cell_Id,
                        Dest_Cell_No,
                        Dest_Cell_Id,
                        Item_Qty,
                        Instock_Date,
                        Status,
                        source_no,
                        brand_no,
                        box_no)
                      values( R_task_detail.locno,
                              R_task_detail.owner_no,
                              R_task_detail.row_id,
                              v_instock_no,
                              v_row_num,
                              R_task_detail.item_no,
                              R_task_detail.size_no,
                              R_task_detail.cell_no,
                              R_task_detail.cell_id,
                              R_task_detail.dest_cell_no,
                              R_task_detail.dest_cell_id,
                              R_task_detail.est_qty,
                              sysdate,
                              '10',
                              R_task_detail.source_no,
                              R_task_detail.brand_no,
                              R_task_detail.box_no);
                         
                                              
                     -- 如果更新不到数据，则提示异常      
                     if sql%rowcount=0 then
                               STRRESULT:='N|插入上架单明细信息时异常！';
                               return;
                     end if;  
                    
                     v_row_num := v_row_num + 1;
                                     
                end loop;
                
             -- 更新上架任务单的状态为已发单
             update  BILL_UM_INSTOCK_DIRECT  udd  
             set udd.status = '13'
             where udd.locno = strLocno
             and udd.owner_no = strOwnerNo
             and udd.status <> '13'
             AND instr(',' || strRowIdList || ',',
                                       ',' || udd.ROW_ID || ',') > 0;
                                       
             -- 如果更新不到数据，则提示异常      
             if sql%rowcount=0 then
                     STRRESULT:='N|更新上架任务单为已发单时异常！';
                     return;
             end if; 
      
        
         STRRESULT := 'Y|';

      EXCEPTION
        WHEN OTHERS THEN
          STRRESULT := 'N|' || SQLERRM ||
                       SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
   END Proc_instock_task_send;                


END PKG_UM_CITY_INSTOCK_UNTREAD;
/
