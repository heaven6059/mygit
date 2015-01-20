create or replace package PKG_UM_CITY_DIRECT_UNTREAD is

  /*
    功能：退仓通知单-预约定位
   作者：zuo.sw
   日期：2014-1-13
  */
 procedure Proc_UM_CITY_DIRECT_UNTREAD(strLocno           in bill_um_untread_mm.LOCNO%type, --仓别
                                      strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                      strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                      strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                      strResult           out varchar2); --返回 执行结果

 /*
     功能:  退仓通知单-预约定位  写库存属性和库存
     作者:  zuo.sw
     日期:  2013-12-17
 */
  PROCEDURE Proc_add_con_content(STR_LOCNO   IN BILL_UM_UNTREAD_MM.LOCNO%TYPE, --仓别
                                 STR_OWNER_NO  IN BILL_UM_UNTREAD_MM.Owner_No%TYPE, -- 委托业主
                                 STR_UNTREAD_MM_NO  IN BILL_UM_UNTREAD_MM.Untread_Mm_No%TYPE,--验收单号
                                 STR_ITEMNO  IN Con_Content.item_no%TYPE, -- 商品编码
                                 STR_SIZENO  IN  BILL_UM_UNTREAD_MM_Dtl.Size_No%type, --尺码
                                 STR_BARCODE  IN Con_Content.Barcode%TYPE, -- 条码
                                 STR_PACKQTY  IN Con_Content.Pack_Qty%TYPE, -- 包装数量
                                 STR_quality  IN Con_Content.Quality%TYPE, -- 品质
                                 STR_ITEM_TYPE IN Con_Content.Item_Type%type,--商品属性
                                 STR_CELLNO  IN Con_Content.Cell_No%TYPE, -- 储位编码
                                 STR_WORKERNO  IN BILL_UM_UNTREAD_MM.CREATOR%TYPE, --操作人
                                 STR_SUPPLIER_NO in item.supplier_no%type,--供应商
                                 STR_FLAG     IN   VARCHAR2, -- 标记S,P,O
                                 STRREMAININGQTY in out number,
                                 STR_I  in out number,
                                 STRRESULT  OUT VARCHAR2) ;

 /*
    功能：退仓通知单-按明细取消定位
    作者：zuo.sw
    日期：2014-1-13
 */
 procedure Proc_UM_CITY_DIRECT_CANCEL(strLocno            in bill_um_untread_mm.LOCNO%type, --仓别
                                      strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                      strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                      strRowId            in varchar2, --rowid的集合
                                      strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                      strResult           out varchar2); --返回 执行结果
  /*
    功能：匹配差异的继续定位 （如果还定位不到的，直接写异常储位）
    作者：zuo.sw
    日期：2014-1-17
 */
  procedure Proc_diff_direct(strLocno           in bill_um_untread_mm.LOCNO%type, --仓别
                                      strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                      strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                      strCheckNoList      IN   VARCHAR2,-- 验收单号
                                      strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                      strResult           out varchar2); --返回 执行结果

  /*
    功能：匹配差异的继续定位，退仓通知单为‘-’或为空的情况，通过验收单来做定位 （如果还定位不到的，直接写异常储位）
    作者：zuo.sw
    日期：2014-4-16
 */
  procedure Proc_diff_direct_by_check(strLocno           in bill_um_untread_mm.LOCNO%type, --仓别
                                      strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                      strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                      strCheckNoList      IN   VARCHAR2,-- 验收单号
                                      strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                      strResult           out varchar2); --返回 执行结果

    /*
      功能:  退仓-匹配差异定位 写库存属性和库存/
      作者:  zuo.sw
      日期:  2013-1-17
    */
  PROCEDURE Proc_diff_add_con_content(STR_LOCNO   IN BILL_UM_UNTREAD_MM.LOCNO%TYPE, --仓别
                                 STR_OWNER_NO  IN BILL_UM_UNTREAD_MM.Owner_No%TYPE, -- 委托业主
                                 STR_UNTREAD_MM_NO  IN BILL_UM_UNTREAD_MM.Untread_Mm_No%TYPE,--通知单号
                                 STR_UC_CHECK_NO    IN BILL_UM_CHECK.CHECK_NO%TYPE,-- 验收单号
                                 STR_ITEMNO  IN Con_Content.item_no%TYPE, -- 商品编码
                                 STR_SIZENO  IN  BILL_UM_UNTREAD_MM_Dtl.Size_No%type, --尺码
                                 STR_BARCODE  IN Con_Content.Barcode%TYPE, -- 条码
                                 STR_PACKQTY  IN Con_Content.Pack_Qty%TYPE, -- 包装数量
                                 STR_quality  IN Con_Content.Quality%TYPE, -- 品质
                                 STR_ITEM_TYPE IN Con_Content.Item_Type%type,--商品属性
                                 STR_CELLNO  IN Con_Content.Cell_No%TYPE, -- 储位编码
                                 STR_WORKERNO  IN BILL_UM_UNTREAD_MM.CREATOR%TYPE, --操作人
                                 STR_SUPPLIERNO  IN ITEM.SUPPLIER_NO%TYPE,--供应商
                                 STR_FLAG     IN   VARCHAR2, -- 标记S,P,O
                                 STRREMAININGQTY in out number,
                                 STR_I  in out number,
                                 STRRESULT  OUT VARCHAR2) ;




    /*
      功能:  退仓-匹配差异定位 写库存属性和库存/  通过验收单
      作者:  zuo.sw
      日期:  2013-1-17
    */
  PROCEDURE Proc_diff_add_content_by_check(STR_LOCNO   IN BILL_UM_UNTREAD_MM.LOCNO%TYPE, --仓别
                                 STR_OWNER_NO  IN BILL_UM_UNTREAD_MM.Owner_No%TYPE, -- 委托业主
                                 STR_UNTREAD_MM_NO  IN BILL_UM_UNTREAD_MM.Untread_Mm_No%TYPE,--通知单号
                                 STR_UC_CHECK_NO    IN BILL_UM_CHECK.CHECK_NO%TYPE,-- 验收单号
                                 STR_ITEMNO  IN Con_Content.item_no%TYPE, -- 商品编码
                                 STR_SIZENO  IN  BILL_UM_UNTREAD_MM_Dtl.Size_No%type, --尺码
                                 STR_BARCODE  IN Con_Content.Barcode%TYPE, -- 条码
                                 STR_PACKQTY  IN Con_Content.Pack_Qty%TYPE, -- 包装数量
                                 STR_quality  IN Con_Content.Quality%TYPE, -- 品质
                                 STR_ITEM_TYPE IN Con_Content.Item_Type%type,--商品属性
                                 STR_CELLNO  IN Con_Content.Cell_No%TYPE, -- 储位编码
                                 STR_Z_CELLID  IN Con_Content.Cell_Id%TYPE,--  来源储位ID
                                 STR_Z_CELLNO  IN Con_Content.Cell_NO%TYPE,--  来源储位编码
                                 STR_BOX_NO    in  BILL_UM_CHECK_dtl.Box_No%type, -- 箱号
                                 STR_BRAND_NO  in  BILL_UM_CHECK_dtl.Brand_No%type, -- 品牌编码
                                 STR_WORKERNO  IN BILL_UM_UNTREAD_MM.CREATOR%TYPE, --操作人
                                 STR_SUPPLIERNO  IN ITEM.SUPPLIER_NO%TYPE,--供应商
                                 STR_FLAG     IN   VARCHAR2, -- 标记S,P,O
                                 STRREMAININGQTY in out number,
                                 STR_I  in out number,
                                 STRRESULT  OUT VARCHAR2) ;

end PKG_UM_CITY_DIRECT_UNTREAD;
/
CREATE OR REPLACE PACKAGE BODY PKG_UM_CITY_DIRECT_UNTREAD IS


  /*
    功能：退仓通知单-预约定位
   作者：zuo.sw
   日期：2014-1-13
  */

  PROCEDURE Proc_UM_CITY_DIRECT_UNTREAD(strLocno            in bill_um_untread_mm.LOCNO%type, --仓别
                                        strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                        strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                        strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                        STRRESULT    OUT VARCHAR2) AS

    STRREMAININGQTY  NUMBER(10); --剩余上架数量;
    STR_I            NUMBER(10); --定位行号
    V_TEMP_CELL_NO   VARCHAR2(20 char); --临时表的唯一单号
    V_RULL_COUNT      NUMBER(10);  -- 是否有上架策略的规则数据
    v_item_type  Con_Content.item_type%type;  -- 商品类型
    v_quality    Con_Content.Quality%type;-- 品质
    v_PACK_QTY   Con_Content.Pack_Qty%type;-- 包装数量
    v_barcode    Con_Content.Barcode%type; -- 条码
    Str_receipt_status   bill_im_check.status%type;--验收单状态
    v_max_row_id_in  NUMBER(10);  -- 最大的行号
    v_c_num       NUMBER(10);  -- 统计数量
    v_SupplierNo item.supplier_no%type;--供应商
     v_same_num   NUMBER(10);  -- 同款的数量
  BEGIN

        -- 获取退仓通知单的状态；
        select s.status into Str_receipt_status  from  bill_um_untread_mm  s
          where s.locno = strLocno
                  and s.owner_no = strOwnerNo
                  and s.untread_mm_no = strUntreadMMNo;

        --如果状态为新建或已预约，则不能进行上架定位的操作
      /*  if Str_receipt_status ='10' or Str_receipt_status='30' then
                  STRRESULT := 'N|新建或已预约的验收单据不能进行上架定位操作！';
                  return;
        end if; */

        --如果状态不为11和25，则提示不能进行定位操作
        if Str_receipt_status <> '11' and Str_receipt_status <> '20' then
               STRRESULT := 'N|当前状态下，单据不能进行按定位操作！';
               return;
        end if;

    -- 标记是否有部分数据没有做
    STR_I := 0;


    -- 获取商品属性和品质
    select m.untread_type,m.quality into v_item_type,v_quality
    from  BILL_UM_UNTREAD_MM  m
    where m.locno = strLocno and  m.owner_no = strOwnerNo
      and m.untread_mm_no=strUntreadMMNo;

    --循环需要定位的商品,定位表的数据（储位为'N'，就表示还成功定位）
    FOR R_ITEM IN (select t.row_id,t.item_no,t.size_no,t.est_qty,m.brand_no,m.cate_no,m.supplier_no
                    from  BILL_UM_DIRECT  t
                    inner join item  m on t.item_no = m.item_no
                    where t.locno = strLocno
                    and t.untread_mm_no = strUntreadMMNo and t.cell_no = 'N'
                    order by t.item_no,t.size_no
                   ) LOOP


      --剩余上架数量等于总上架数量
      STRREMAININGQTY := R_ITEM.est_qty;

      --判断是否有找到对应的上架策略规则
      SELECT count(*) into V_RULL_COUNT
                       FROM CS_INSTOCK_SETTING M
                      INNER JOIN CS_INSTOCK_SETTINGDTL D
                         ON M.LOCNO = D.LOCNO
                        AND M.SETTING_NO = D.SETTING_NO
                      WHERE M.LOCNO = STRLOCNO
                        AND M.STATUS = '1'
                        AND M.SET_TYPE = '1'-- 退仓上架策略
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

      begin
      select m.supplier_no into  v_SupplierNo from item m where m.item_no=R_ITEM.Item_No;
      exception when no_data_found  then
               STRRESULT := 'N|商品'||R_ITEM.ITEM_NO||'没有对应的供应商信息，请先添加!';
          RETURN;
      end;

      -- 获取包装数量
      select md.PACK_QTY into v_PACK_QTY
      from  BILL_UM_UNTREAD_MM_dtl  md
      where md.locno = strLocno and  md.owner_no = strOwnerNo
        and md.untread_mm_no=strUntreadMMNo
        and md.item_no = R_ITEM.Item_No
        and md.size_no = R_ITEM.Size_No
        and rownum=1;

      -- 获取条码
      select b.barcode into v_barcode
      from  item_barcode  b
      where b.item_no = R_ITEM.Item_No
          and b.size_no = R_ITEM.Size_No
          and b.pack_qty = v_PACK_QTY
          and b.package_id = 0;

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
                        AND M.SET_TYPE = '1' -- 退仓上架策略
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

        -- 取一个唯一编号，用于插入储位临时表时使用
        PKG_WMS_BASE.proc_getsheetno(STRLOCNO,'SJU',V_TEMP_CELL_NO,STRRESULT); --返回 执行结果

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



          -- 查询所有跟当前商品的商品属性相同的储位，插入到同类型储位表
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
                 and con.status ='0'
                 and con.flag = '0'
                 AND con.cell_no in (select CELL_NO  from  SYS_CELL_TYPE
                   where id = V_TEMP_CELL_NO);
          END IF;

          --如果选择了空储位优先
          IF (r_rule.EMPTY_CELL_FLAG = '1') THEN

                -- 同款的数量
                select  count(*) into v_same_num  from SYS_CELL_STYLE where id = V_TEMP_CELL_NO;

                -- 如果选择了同款临近
                IF (R_RULE.SAME_ITEM_FLAG = '1'  and  v_same_num > 0) THEN
                  INSERT INTO SYS_CELL_EMPTY
                    select V_TEMP_CELL_NO,tc.CELL_NO From SYS_CELL_TYPE tc
                    where tc.id = V_TEMP_CELL_NO
                        and  not exists (
                            select t.cell_no from con_content t ,cm_defcell ll
                            where t.locno=ll.locno and t.owner_no=ll.owner_no
                              and t.locno = STRLOCNO
                              and t.cell_no=ll.cell_no and ll.CELL_STATUS='0'
                              and (t.qty > 0 or t.instock_qty >0 or t.outstock_qty >0))
                        and not exists(select * from SYS_CELL_STYLE where id = V_TEMP_CELL_NO);

                else  -- 如果没有选择同款临近
                    INSERT INTO SYS_CELL_EMPTY
                    select V_TEMP_CELL_NO,tc.CELL_NO From SYS_CELL_TYPE  tc where tc.id = V_TEMP_CELL_NO and  not exists (
                        select t.cell_no from con_content t ,cm_defcell ll
                        where t.locno=ll.locno and t.owner_no=ll.owner_no
                        and t.locno = STRLOCNO
                        and t.cell_no=ll.cell_no and ll.CELL_STATUS='0'
                        and (t.qty > 0 or t.instock_qty >0 or t.outstock_qty >0) );
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

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_STYLE_CELL.Cell_No,'S',strWorkerNo,r_item.supplier_no,STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_EMPTY_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_EMPTY
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO ASC) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_EMPTY_CELL.Cell_No,strWorkerNo,r_item.supplier_no,'P',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_TYPE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_TYPE
                           where id = V_TEMP_CELL_NO
                           and not exists (select *  from sys_cell_style  where   id = V_TEMP_CELL_NO )
                           and not exists (select *  from sys_cell_empty where  id = V_TEMP_CELL_NO)
                           ORDER BY CELL_NO ASC) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_TYPE_CELL.Cell_No,strWorkerNo,r_item.supplier_no,'O',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_STYLE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_STYLE
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;


                 -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_STYLE_CELL.Cell_No,strWorkerNo,r_item.supplier_no,'S',STRREMAININGQTY,STR_I,STRRESULT);


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
            FOR R_EMPTY_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_EMPTY
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_EMPTY_CELL.Cell_No,strWorkerNo,r_item.supplier_no,'P',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_TYPE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_TYPE
                           where id = V_TEMP_CELL_NO
                           and not exists (select *  from sys_cell_style  where   id = V_TEMP_CELL_NO )
                           and not exists (select *  from sys_cell_empty where  id = V_TEMP_CELL_NO)
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_TYPE_CELL.Cell_No,strWorkerNo,r_item.supplier_no,'O',STRREMAININGQTY,STR_I,STRRESULT);


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
      if  STRREMAININGQTY >= 0 and STRREMAININGQTY < R_ITEM.EST_QTY  then

             --删除为'N'的定位数据
             delete  from  BILL_UM_DIRECT
             where   UNTREAD_MM_NO =  strUntreadMMNo
                 and LOCNO = strLocno
                 and item_no = R_ITEM.ITEM_NO
                 and size_no = SIZE_NO
                 and pack_qty = v_PACK_QTY
                 and  CELL_NO = 'N';

            -- 如果还有没有定位到的商品编码，则添加到定位表
            if  STRREMAININGQTY > 0  then

                 BEGIN
                      select nvl(max(w.row_id),0) into v_max_row_id_in  from  BILL_UM_DIRECT  w
                            where w.untread_mm_no = strUntreadMMNo and w.locno = strLocno;
                       EXCEPTION
                            WHEN NO_DATA_FOUND THEN
                            v_max_row_id_in :=0;
                 END;

                 -- 插入定位表
                 INSERT INTO BILL_UM_DIRECT
                              (LOCNO,
                               ROW_ID,
                               UNTREAD_MM_NO,
                               ITEM_NO,
                               SIZE_NO,
                               PACK_QTY,
                               EST_QTY,
                               CELL_NO,
                               CREATETM)
                  VALUES(strLocno,
                         v_max_row_id_in+1,
                         strUntreadMMNo,--单号
                         R_ITEM.ITEM_NO,
                         R_ITEM.SIZE_NO,
                         v_PACK_QTY,
                         STRREMAININGQTY,
                         'N',
                         sysdate); --将该储位放满

                  STR_I := STR_I + 1; --行号自动加1

              end if;

      end if;


    END LOOP;

   if  STR_I = 0  then
         STRRESULT := 'N|未预约到任何储位信息，请检查上架策略设置!';
         return;
   end if;

   -- 查询是否有空储位的数据；
   select COUNT(*) into v_c_num
   from  BILL_UM_DIRECT ud  where ud.untread_mm_no = strUntreadMMNo and ud.locno = strLocno
         and ud.cell_no ='N';

   if  v_c_num > 0   then
         update  BILL_UM_UNTREAD_MM  mm
         set mm.status = '20',
             mm.edittm = sysdate,
             mm.editor = strWorkerNo
         where mm.untread_mm_no = strUntreadMMNo
          and  mm.locno = strLocno
          and  mm.owner_no = strOwnerNo
          and  mm.status <> '20';
   else
         update  BILL_UM_UNTREAD_MM  mm
         set mm.status = '25',
             mm.edittm = sysdate,
             mm.editor = strWorkerNo
         where mm.untread_mm_no = strUntreadMMNo
          and  mm.locno = strLocno
          and  mm.owner_no = strOwnerNo;

          if sql%rowcount=0 then
                   STRRESULT:='N|更新退仓通知单为预约完成时异常!';
                   return;
          end if;

   end if;

  STRRESULT := 'Y|';

  EXCEPTION
    WHEN OTHERS THEN
      STRRESULT := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
  END Proc_UM_CITY_DIRECT_UNTREAD;


  /*
     作者:  zuo.sw
     日期:  2013-1-13
     功能:  退仓定位写库存属性和库存/
    */

  PROCEDURE Proc_add_con_content(STR_LOCNO   IN BILL_UM_UNTREAD_MM.LOCNO%TYPE, --仓别
                                 STR_OWNER_NO  IN BILL_UM_UNTREAD_MM.Owner_No%TYPE, -- 委托业主
                                 STR_UNTREAD_MM_NO  IN BILL_UM_UNTREAD_MM.Untread_Mm_No%TYPE,--验收单号
                                 STR_ITEMNO  IN Con_Content.item_no%TYPE, -- 商品编码
                                 STR_SIZENO  IN  BILL_UM_UNTREAD_MM_Dtl.Size_No%type, --尺码
                                 STR_BARCODE  IN Con_Content.Barcode%TYPE, -- 条码
                                 STR_PACKQTY  IN Con_Content.Pack_Qty%TYPE, -- 包装数量
                                 STR_quality  IN Con_Content.Quality%TYPE, -- 品质
                                 STR_ITEM_TYPE IN Con_Content.Item_Type%type,--商品属性
                                 STR_CELLNO  IN Con_Content.Cell_No%TYPE, -- 储位编码
                                 STR_WORKERNO  IN BILL_UM_UNTREAD_MM.CREATOR%TYPE, --操作人
                                 STR_SUPPLIER_NO in item.supplier_no%type,--供应商
                                 STR_FLAG     IN   VARCHAR2, -- 标记S,P,O
                                 STRREMAININGQTY in out number,
                                 STR_I  in out number,
                                 STRRESULT  OUT VARCHAR2)  as

    STR_QTY          CON_CONTENT.QTY%TYPE; --库存数量
    STR_INSTOCK_QTY  CON_CONTENT.INSTOCK_QTY%TYPE; --预上数量
    STR_LIMIT_QTY    CS_CELL_PACK_SETTING.LIMIT_QTY%TYPE; --最大存放数量
    STR_MIX_FLAG     CM_DEFCELL.MIX_FLAG%TYPE; --混放货物标识
    V_NCELLID        CON_CONTENT.CELL_ID%TYPE; --库存Cell_ID
    V_CELL_ALL_COUNT  NUMBER(10);  -- 上架范围的所有储位的数量
    V_INSTOCK_NUM   NUMBER(10); -- 当前的上架数量
    v_item_pack_spec  item_pack.pack_spec%type; -- 商品包装规则
    v_max_row_id     BILL_UM_DIRECT.Row_Id%type; -- 行号
    v_cell_no_num     NUMBER(10); -- 当前储位的记录数
    v_dif_item_no_num   NUMBER(10); -- 当前储位当前商品的记录数

    begin

                  -- 判断商品是否有设置包装规格
                  BEGIN
                      SELECT mpk.pack_spec
                        INTO v_item_pack_spec
                        FROM  ITEM_PACK  MPK
                       WHERE MPK.ITEM_NO = STR_ITEMNO
                         and mpk.pack_qty = STR_PACKQTY
                         AND ROWNUM = 1;

                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码:' || STR_ITEMNO ||'未设置包装规格，请先设置!';
                        RETURN;
                  END;

                  --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
                  BEGIN
                      SELECT LIMIT_QTY
                        INTO STR_LIMIT_QTY
                        FROM CS_CELL_PACK_SETTING PS
                      where  PS.LOCNO = STR_LOCNO
                         and ps.owner_no = STR_OWNER_NO
                         AND PS.AREA_TYPE = (select k.area_type  from CM_DEFAREa k
                                inner join CM_DEFCELL  c  on   k.locno = c.locno
                                       and (c.ware_no||''||c.area_no) = (k.ware_no||''||k.area_no)
                                where  c.locno = STR_LOCNO
                                and c.cell_no = STR_CELLNO and rownum =1 )
                         and ps.pack_spec = v_item_pack_spec
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

                             -- 查询对应的储位ID
                             SELECT count(*) into V_CELL_ALL_COUNT
                                 FROM v_content CON
                               WHERE CON.LOCNO = STR_LOCNO
                                   and con.owner_no = STR_OWNER_NO
                                   AND CON.CELL_NO = STR_CELLNO
                                   AND CON.ITEM_NO = STR_ITEMNO
                                   and con.pack_qty = STR_PACKQTY
                                   and con.quality = STR_quality
                                   and con.item_type = STR_ITEM_TYPE
                                   and con.barcode = STR_BARCODE
                                   and con.supplier_no = STR_SUPPLIER_NO
                                   AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                   AND CON.FLAG = '0'
                                   and con.hm_manual_flag = '1'; --允许手工移库

                             -- 如果有对应的储位库存信息，而且是同款储位或排除同款和空储位的其他储位
                             if  ((STR_FLAG ='O' OR  STR_FLAG ='S') AND  V_CELL_ALL_COUNT > 0) then
                                 -- 查询对应的储位ID
                                 SELECT  max(CON.CELL_ID) into V_NCELLID
                                     FROM CON_CONTENT CON
                                 WHERE CON.LOCNO = STR_LOCNO
                                   and con.owner_no = STR_OWNER_NO
                                   AND CON.CELL_NO = STR_CELLNO
                                   AND CON.ITEM_NO = STR_ITEMNO
                                   and con.pack_qty = STR_PACKQTY
                                   and con.quality = STR_quality
                                   and con.item_type = STR_ITEM_TYPE
                                   and con.barcode = STR_BARCODE
                                   and con.supplier_no = STR_SUPPLIER_NO
                                   AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                   AND CON.FLAG = '0'
                                   and con.hm_manual_flag = '1'; --允许手工移库

                                 --updt by crm 20140110 统一库存记账，写储位预上量
                                  --开始
                                  begin
                                          ACC_PREPARE_DATA_EXT(STR_UNTREAD_MM_NO,'UC','I',STR_WORKERNO,1,I_LOCNO => STR_LOCNO,I_OWNER_NO => STR_OWNER_NO,
                                          I_CELL_ID =>V_NCELLID,I_CELL_NO =>STR_CELLNO ,I_QTY=>0,I_INSTOCK_QTY =>V_INSTOCK_NUM  );
                                         acc_apply(STR_UNTREAD_MM_NO,'2','UC','I',1);
                                exception when others then
                                         STRRESULT := 'N|' || SQLERRM ||
                                         SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
                                RETURN;
                                  end;
                                  --结束
                                --结束

                      /*
                                  UPDATE CON_CONTENT --更新库存表，增加预上数量
                                     SET INSTOCK_QTY = INSTOCK_QTY + V_INSTOCK_NUM
                                   WHERE LOCNO = STR_LOCNO
                                     AND CELL_NO = STR_CELLNO
                                     AND CELL_ID = V_NCELLID
                                     and INSTOCK_QTY >=0
                                     and V_INSTOCK_NUM > 0 ;

                                    -- 如果更新不到数据，则提示异常
                                    if sql%rowcount=0 then
                                         STRRESULT:='N|更新存储区储位：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码；'||STR_ITEMNO||'
                                         的库存记录时未更新到数据!';
                                         return;
                                    end if;      */

                              end if;

                              -- 如果是空储位，或者是同款储位，其他储位且找不到储位信息的，就全部新增
                              if (((STR_FLAG ='O' OR  STR_FLAG ='S') AND  V_CELL_ALL_COUNT < 1) OR STR_FLAG ='P')  then

                                --updt by crm 20140110 统一库存记账，写储储位预上量
                              --开始
                              begin
                                    acc_prepare_data_ext(STR_UNTREAD_MM_NO ,
                                                        'UC' ,
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
                                                        STR_quality,
                                                        STR_OWNER_NO,
                                                        STR_SUPPLIER_NO,
                                                       /* STR_BOXNO*/'',
                                                        0,
                                                        0,
                                                        V_INSTOCK_NUM,
                                                        '0',
                                                        '0',
                                                        '1');
                                        --回写箱码储位
                                         acc_apply(STR_UNTREAD_MM_NO,'2','UC','I',1);


                                        -- 查找获取新的储位ID
                                        /*select max(t.cell_id) into V_NCELLID  from con_content t where t.locno=STR_LOCNO
                                        and t.owner_no=STR_OWNER_NO and t.cell_no=STR_CELLNO
                                        and t.item_no=STR_ITEMNO and t.barcode=STR_BARCODE
                                        and t.item_type=STR_ITEM_TYPE and t.quality=STR_quality and t.supplier_no=STR_SUPPLIER_NO;*/
                                        select t.cell_id into V_NCELLID  from tmp_acc_result  t where t.row_id=STR_I;
                                  exception when others then
                                        STRRESULT := 'N|' || SQLERRM ||
                                        SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
                                        RETURN;
                                end;
                                  --结束
                                   /*
                                    if  STR_ITEMNO is null   or  V_INSTOCK_NUM < 1 then
                                         STRRESULT:='N|新增存储区储位：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码'||STR_ITEMNO||'，库存记录时，参数非法!';
                                         return;
                                    end if;*/

                             end if;


                       BEGIN
                            select nvl(max(w.row_id),0) into v_max_row_id  from  BILL_UM_DIRECT  w
                            where w.untread_mm_no = STR_UNTREAD_MM_NO and w.locno = STR_LOCNO;
                       EXCEPTION
                            WHEN NO_DATA_FOUND THEN
                            v_max_row_id :=0;
                       END;

                       -- 插入定位表
                       INSERT INTO BILL_UM_DIRECT
                          (LOCNO,
                           ROW_ID,
                           UNTREAD_MM_NO,
                           ITEM_NO,
                           SIZE_NO,
                           PACK_QTY,
                           EST_QTY,
                           CELL_NO,
                           CELL_id,
                           CREATETM)
                        VALUES
                          (STR_LOCNO,
                           v_max_row_id+1,
                           STR_UNTREAD_MM_NO,--验收单号
                           STR_ITEMNO,
                           STR_SIZENO,
                           STR_PACKQTY,
                           V_INSTOCK_NUM,
                           STR_CELLNO,
                           V_NCELLID,
                           sysdate); --将该储位放满

                           if  STR_ITEMNO is null  or  V_INSTOCK_NUM < 1 then
                                         STRRESULT:='N|新增定位记录，预上储位编码：'||STR_CELLNO||'，ID：'||V_NCELLID||'时参数非法!';
                                         return;
                           end if;

                           -- 如果更新不到数据，则提示异常
                           if sql%rowcount=0 then
                                         STRRESULT:='N|新增定位记录，预上储位编码：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码；'||STR_ITEMNO||'
                                         的时异常!';
                                         return;
                           end if;


                           STR_I := STR_I + 1; --行号自动加1
     end if;

  END Proc_add_con_content;


   /*
    功能：退仓通知单-按明细取消定位
   作者：zuo.sw
   日期：2014-1-13
 */

  procedure Proc_UM_CITY_DIRECT_CANCEL(strLocno            in bill_um_untread_mm.LOCNO%type, --仓别
                                      strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                      strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                      strRowId            in varchar2, --rowid的集合
                                      strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                      strResult           out varchar2)  as --返回 执行结果

      v_c_num_n    number(10);
      v_c_num      number(10);

      begin

         for r_rowid in(select ud.row_id,ud.item_no,ud.size_no,ud.cell_no,ud.cell_id,ud.EST_QTY
                        from   BILL_UM_DIRECT  ud
                        where ud.locno=strLocno  and  ud.untread_mm_no=strUntreadMMNo
                             AND instr(',' || strRowId || ',',
                                       ',' || ud.row_id || ',') > 0
                             and ud.cell_no <> 'N') loop

             -- 取消定位把储位的数据给清除掉
             update BILL_UM_DIRECT  ut
               set ut.cell_no = 'N',
                   ut.cell_id = null
             WHERE ut.locno = strLocno
               and ut.untread_mm_no = strUntreadMMNo
               and ut.row_id = r_rowid.row_id;

             -- 如果更新不到数据，则提示异常
             if sql%rowcount=0 then
                    STRRESULT:='N|取消定位时，未更新到数据！行号：'||r_rowid.row_id||'';
                    return;
             end if;

               --updt by crm 20140111 统一库存记账，增加目的储位库存，扣目的遇上库存
               --开始
               begin
                     ACC_PREPARE_DATA_EXT(strUntreadMMNo,'VM','O',strWorkerNo,strRowId,I_LOCNO => strLocno,I_OWNER_NO =>strOwnerNo,
                     I_CELL_ID =>r_rowid.cell_id,I_CELL_NO =>r_rowid.cell_no ,I_QTY=>0,I_INSTOCK_QTY =>-r_rowid.EST_QTY  );
                     acc_apply(strUntreadMMNo,2,'VM','O',1);
              exception when others then
                     STRRESULT := 'N|' || SQLERRM ||
                     SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
                     RETURN;
            end;
                                  --结束
              --结束
             
         end loop;

         -- 查询是否有空储位的数据；
         select COUNT(*) into v_c_num_n
         from  BILL_UM_DIRECT ud  where ud.untread_mm_no = strUntreadMMNo and ud.locno = strLocno
               and ud.cell_no ='N';

         -- 查询所有的数据
         select COUNT(*) into v_c_num
         from  BILL_UM_DIRECT ud  where ud.untread_mm_no = strUntreadMMNo and ud.locno = strLocno;

         if  v_c_num_n > 0  then
               update  BILL_UM_UNTREAD_MM  mm
               set mm.status = '20',
                   mm.edittm = sysdate,
                   mm.editor = strWorkerNo
               where mm.untread_mm_no = strUntreadMMNo
                and  mm.locno = strLocno
                --and  mm.owner_no = strOwnerNo
                and  mm.status <> '20';
         end if;

         -- 如果全部取消定位，就把单据状态更改为已审核，以便于重新定位
         if  v_c_num_n = v_c_num   then

               update  BILL_UM_UNTREAD_MM  mm
               set mm.status = '11',
                   mm.edittm = sysdate,
                   mm.editor = strWorkerNo
               where mm.untread_mm_no = strUntreadMMNo
                and  mm.locno = strLocno;
                --and  mm.owner_no = strOwnerNo;

                if sql%rowcount=0 then
                         STRRESULT:='N|更新退仓通知单为已审核时异常!';
                         return;
                end if;

         end if;

         STRRESULT := 'Y|';

      EXCEPTION
        WHEN OTHERS THEN
          STRRESULT := 'N|' || SQLERRM ||
                       SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
   END Proc_UM_CITY_DIRECT_CANCEL;



  /*
    功能：匹配差异的继续定位 （如果还定位不到的，直接写异常储位）
   作者：zuo.sw
   日期：2014-1-17
 */

   PROCEDURE Proc_diff_direct(strLocno           in bill_um_untread_mm.LOCNO%type, --仓别
                                      strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                      strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                      strCheckNoList      IN   VARCHAR2,-- 验收单号
                                      strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                      strResult           out varchar2) AS

    STRREMAININGQTY  NUMBER(10); --剩余上架数量;
    STR_I            NUMBER(10); --定位行号
    V_TEMP_CELL_NO   VARCHAR2(20 char); --临时表的唯一单号
    V_RULL_COUNT      NUMBER(10);  -- 是否有上架策略的规则数据
    v_item_type  Con_Content.item_type%type;  -- 商品类型
    v_quality    Con_Content.Quality%type;-- 品质
    v_PACK_QTY   Con_Content.Pack_Qty%type;-- 包装数量
    v_barcode    Con_Content.Barcode%type; -- 条码
    --v_c_num       NUMBER(10);  -- 统计数量
    v_brand_no    item.brand_no%type; -- 品牌
    v_cat_no      item.cate_no%type; --品类
    v_exption_cell_no  CON_CONTENT.CELL_NO%TYPE; --异常区的储位变量
    v_exption_cell_id  CON_CONTENT.CELL_ID%TYPE; --异常区的储位变量
    v_exption_cell_no_count  NUMBER(10);  -- 异常区的储位的数量
    v_count_num              NUMBER(10);
    v_max_row_id             NUMBER(10);
    v_supplierNo               item.supplier_no%type;--供应商
    v_d_same_num  number(10);
    --log20140427 modi by chenhaitao 用于记录上架明细id
    v_instock_row_id    NUMBER(10);
    v_strSourceNo       bill_um_instock_direct.source_no%type;--上架任务的来源单号
    --log20140427

  BEGIN

    -- 标记是否有部分数据没有做
    STR_I := 0;

    -- 获取商品属性,品质,包装数量
    select uu.untread_type,uu.quality,uu.pack_qty into v_item_type,v_quality,v_PACK_QTY  from BILL_UM_CHECK   uc
      inner join  bill_um_untread  uu  on uc.untread_no = uu.untread_no
                  and uc.locno = uu.locno  and uc.owner_no = uu.owner_no
      where 1 =1   AND instr(',' || strCheckNoList || ',',
                              ',' || uc.check_no || ',') > 0
                   and rownum=1;


    --循环有差异需要定位的商品（储位为'N'，就表示还未成功定位）
    FOR R_ITEM IN (select root.item_no,root.size_no,bak.untread_mm_no,(root.root_qty-nvl(bak.bak_qty,0)) as difQty   from (
                    select cd.item_no,cd.size_no,nvl(sum(cd.check_qty),0) root_qty from  BILL_UM_CHECK_DTL  cd
                    where cd.locno = strLocno
                    and  nvl(cd.check_qty,0) > 0
                    AND instr(',' || strCheckNoList || ',',
                              ',' || cd.check_no || ',') > 0
                    group by cd.item_no,cd.size_no) root left join
                    (select ud.untread_mm_no,ud.item_no,ud.size_no,sum(ud.EST_QTY - nvl(ud.DIS_QTY,0)) as bak_qty
                                            from   BILL_UM_DIRECT  ud
                                            where ud.locno= strLocno
                                                 and ud.cell_no != 'N'
                                                 and ud.untread_mm_no = strUntreadMMNo
                                                 and ud.untread_mm_no != '-'-- 表示遇到通知单号不为空
                                            group by ud.untread_mm_no,ud.item_no,ud.size_no) bak
                         on root.item_no=bak.item_no  and root.size_no=bak.size_no
                            and (root.root_qty-nvl(bak.bak_qty,0)) > 0
                         order by  root.item_no,root.size_no
                   ) LOOP


      --剩余上架数量等于总上架数量
      STRREMAININGQTY := R_ITEM.difQty;

      -- 填充 品牌和 品类
      select m.brand_no,m.cate_no,m.supplier_no into v_brand_no,v_cat_no,v_supplierNo  from  item m where m.item_no =  R_ITEM.Item_No;

      --判断是否有找到对应的上架策略规则
      SELECT count(*) into V_RULL_COUNT
                       FROM CS_INSTOCK_SETTING M
                      INNER JOIN CS_INSTOCK_SETTINGDTL D
                         ON M.LOCNO = D.LOCNO
                        AND M.SETTING_NO = D.SETTING_NO
                      WHERE M.LOCNO = STRLOCNO
                        AND M.STATUS = '1'
                        AND M.SET_TYPE = '1'-- 退仓上架策略
                        AND ((M.DETAIL_TYPE = '2' AND
                            D.SELECT_VALUE = R_ITEM.ITEM_NO) OR
                            (M.DETAIL_TYPE = '1' AND
                            D.SELECT_VALUE = v_cat_no) OR
                            (M.DETAIL_TYPE = '0' AND
                            D.SELECT_VALUE = v_brand_no));

      if  V_RULL_COUNT = 0  then
          STRRESULT := 'N|品牌'||v_brand_no||'-类别'||v_cat_no||'--商品编码'||R_ITEM.ITEM_NO||'未找到上架策略数据，请先添加!';
          RETURN;
      end if;

      -- 获取条码
      select b.barcode into v_barcode
      from  item_barcode  b
      where b.item_no = R_ITEM.Item_No
          and b.size_no = R_ITEM.Size_No
          and b.pack_qty = v_PACK_QTY
          and b.package_id = 0;

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
                        AND M.SET_TYPE = '1' -- 退仓上架策略
                        AND ((M.DETAIL_TYPE = '2' AND
                            D.SELECT_VALUE = R_ITEM.ITEM_NO) OR
                            (M.DETAIL_TYPE = '1' AND
                            D.SELECT_VALUE = v_cat_no) OR
                            (M.DETAIL_TYPE = '0' AND
                            D.SELECT_VALUE = v_brand_no))
                      ORDER BY M.DETAIL_TYPE DESC) LOOP

        --如果待上架数量等于零，则跳过本次循环
        IF (STRREMAININGQTY = 0) THEN
           EXIT;
        END IF;

        --==============================取出所有符合规则的储位开始======================================

        -- 取一个唯一编号，用于插入储位临时表时使用
        PKG_WMS_BASE.proc_getsheetno(STRLOCNO,'SJU',V_TEMP_CELL_NO,STRRESULT); --返回 执行结果

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


          -- 查询所有跟当前商品的商品属性相同的储位，插入到同类型储位表
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
                 and con.status ='0'
                 and con.flag = '0'
                 AND con.cell_no in (select CELL_NO  from  SYS_CELL_TYPE
                   where id = V_TEMP_CELL_NO);
          END IF;

          --如果选择了空储位优先
          IF (r_rule.EMPTY_CELL_FLAG = '1') THEN

                -- 同款的数量
             select  count(*) into v_d_same_num  from SYS_CELL_STYLE where id = V_TEMP_CELL_NO;

                -- 如果选择了同款临近
                IF (R_RULE.SAME_ITEM_FLAG = '1' and v_d_same_num > 0) THEN
                  INSERT INTO SYS_CELL_EMPTY
                    select V_TEMP_CELL_NO,tc.CELL_NO From SYS_CELL_TYPE tc
                    where tc.id = V_TEMP_CELL_NO
                        and  not exists (
                            select t.cell_no from con_content t ,cm_defcell ll
                            where t.locno=ll.locno and t.owner_no=ll.owner_no
                              and t.locno = STRLOCNO
                              and t.cell_no=ll.cell_no and ll.CELL_STATUS='0'
                              and (t.qty > 0 or t.instock_qty >0 or t.outstock_qty >0))
                        and not exists(select * from SYS_CELL_STYLE where id = V_TEMP_CELL_NO);

                else  -- 如果没有选择同款临近
                    INSERT INTO SYS_CELL_EMPTY
                    select V_TEMP_CELL_NO,tc.CELL_NO From SYS_CELL_TYPE  tc where tc.id = V_TEMP_CELL_NO and  not exists (
                        select t.cell_no from con_content t ,cm_defcell ll
                        where t.locno=ll.locno and t.owner_no=ll.owner_no
                        and t.locno = STRLOCNO
                        and t.cell_no=ll.cell_no and ll.CELL_STATUS='0'
                        and (t.qty > 0 or t.instock_qty >0 or t.outstock_qty >0) );
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

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,strCheckNoList,R_ITEM.ITEM_NO,
                R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_STYLE_CELL.Cell_No,strWorkerNo,v_supplierNo,'S',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_EMPTY_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_EMPTY
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO ASC) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,strCheckNoList,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_EMPTY_CELL.Cell_No,strWorkerNo,v_supplierNo,'P',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_TYPE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_TYPE
                           where id = V_TEMP_CELL_NO
                           and not exists (select *  from sys_cell_style  where   id = V_TEMP_CELL_NO )
                           and not exists (select *  from sys_cell_empty where  id = V_TEMP_CELL_NO)
                           ORDER BY CELL_NO ASC) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,strCheckNoList,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_TYPE_CELL.Cell_No,strWorkerNo,v_supplierNo,'O',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_STYLE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_STYLE
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;


                 -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,strCheckNoList,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_STYLE_CELL.Cell_No,strWorkerNo,v_supplierNo,'S',STRREMAININGQTY,STR_I,STRRESULT);


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
            FOR R_EMPTY_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_EMPTY
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,strCheckNoList,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_EMPTY_CELL.Cell_No,strWorkerNo,v_supplierNo,'P',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_TYPE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_TYPE
                           where id = V_TEMP_CELL_NO
                           and not exists (select *  from sys_cell_style  where   id = V_TEMP_CELL_NO )
                           and not exists (select *  from sys_cell_empty where  id = V_TEMP_CELL_NO)
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_con_content(STRLOCNO,strOwnerNo,strUntreadMMNo,strCheckNoList,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_TYPE_CELL.Cell_No,strWorkerNo,v_supplierNo,'O',STRREMAININGQTY,STR_I,STRRESULT);


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
      if  STRREMAININGQTY > 0   then

          -- 查找异常区的储位
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

          -- 如果有通知单号
          if strUntreadMMNo <> '-' and strUntreadMMNo <> 'N' and strUntreadMMNo is not null then
                    --updt by crm 20140110 统一库存记账，写储储位预上量
                   --开始
                   begin
                         acc_prepare_data_ext(strUntreadMMNo ,
                                                        'UC' ,
                                                        'I' ,
                                                        strWorkerNo ,
                                                        STR_I ,
                                                         '',
                                                        STRLOCNO,
                                                        v_exption_cell_no,
                                                        R_ITEM.ITEM_NO,
                                                        R_ITEM.SIZE_NO,
                                                        1,
                                                        v_item_type,
                                                        v_quality,
                                                        strOwnerNo,
                                                        v_supplierNo,
                                                       /* STR_BOXNO*/'',
                                                        0,
                                                        0,
                                                        STRREMAININGQTY,
                                                        '0',
                                                        '0',
                                                        '1');
                        --回写箱码储位
                        acc_apply(strUntreadMMNo,'2','UC','I',1);
                        --log20140427 modi by chenhaitao 取库存校验有没有写到库存
                        begin
                        SELECT  CON.CELL_ID into v_exption_cell_id
                                               FROM CON_CONTENT CON
                        WHERE CON.LOCNO = STRLOCNO
                                             and con.owner_no = strOwnerNo
                                             AND CON.CELL_NO = v_exption_cell_no
                                             AND CON.ITEM_NO = R_ITEM.ITEM_NO
                                             and con.pack_qty = v_PACK_QTY
                                             and con.quality = v_quality
                                             and con.item_type = v_item_type
                                             and con.supplier_no=v_supplierNo
                                             and con.barcode = v_BARCODE
                                             --log20140427 modi by chenhaitao 写预上后，预上应该是有值的
                                             and con.instock_qty>0
                                             --end log20140427
                                             AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                             AND CON.FLAG = '0'
                                             and con.hm_manual_flag = '1'--允许手工移库
                                             and rownum=1; 
                        exception when no_data_found then 
                          STRRESULT:='N|新增异常区储位：'||v_exption_cell_no||'未找到对应库存！';
                          return;
                        end;
                        -- 查询对应的储位ID
                        SELECT  max(CON.CELL_ID) into v_exption_cell_id
                                               FROM CON_CONTENT CON
                        WHERE CON.LOCNO = STRLOCNO
                                             and con.owner_no = strOwnerNo
                                             AND CON.CELL_NO = v_exption_cell_no
                                             AND CON.ITEM_NO = R_ITEM.ITEM_NO
                                             and con.pack_qty = v_PACK_QTY
                                             and con.quality = v_quality
                                             and con.item_type = v_item_type
                                             and con.supplier_no=v_supplierNo
                                             and con.barcode = v_BARCODE
                                             --log20140427 modi by chenhaitao 写预上后，预上应该是有值的
                                             and con.instock_qty>0
                                             --end log20140427
                                             AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                             AND CON.FLAG = '0'
                                             and con.hm_manual_flag = '1'; --允许手工移库
                      exception when others then
                                 STRRESULT := 'N|' || SQLERRM ||
                                SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
                                RETURN;
                    end;
                  --end log20140427
                  -- 获取新的储位ID
 /*                 SELECT SEQ_CON_CONTENT.NEXTVAL INTO v_exption_cell_id FROM DUAL;

                  -- 插入库存数据
                  INSERT INTO CON_CONTENT
                                    (LOCNO,
                                     OWNER_NO,
                                     CELL_NO,
                                     CELL_ID,
                                     ITEM_NO,
                                     barcode,
                                     PACK_QTY,
                                     QTY,
                                     INSTOCK_QTY,
                                     HM_MANUAL_FLAG,
                                     CREATOR,
                                     CREATETM,
                                     STATUS)
                   VALUES
                                    (STRLOCNO,
                                     strOwnerNo,
                                     v_exption_cell_no,
                                     v_exption_cell_id,
                                     R_ITEM.ITEM_NO,
                                     v_barcode,
                                     v_PACK_QTY,
                                     0,
                                     STRREMAININGQTY,
                                     1,
                                     '',
                                     SYSDATE,
                                     0);*/

                 /*  if  R_ITEM.ITEM_NO is null   or  STRREMAININGQTY < 1 then
                           STRRESULT:='N|新增异常区储位：'||v_exption_cell_no||'，ID：'||v_exption_cell_id||'，商品编码'||R_ITEM.ITEM_NO||'，库存记录时，参数非法!';
                           return;
                   end if;*/

                   /*-- 如果更新不到数据，则提示异常
                   if sql%rowcount=0 then
                           STRRESULT:='N|新增异常区储位：'||v_exption_cell_no||'，ID：'||v_exption_cell_id||'，商品编码'||R_ITEM.ITEM_NO||'的库存记录时异常!';
                           return;
                   end if;*/


                  -- 判断是否有存在的定位信息；
                       select count(*) into v_count_num  from  BILL_UM_DIRECT  um
                       where um.locno = STRLOCNO
                       and um.untread_mm_no =strUntreadMMNo
                       and um.item_no =R_ITEM.Item_No
                       and um.size_no =R_ITEM.Size_No
                       and um.cell_no =v_exption_cell_no
                       and um.cell_id = v_exption_cell_id
                       and um.pack_qty = v_PACK_QTY ;

                       if  v_count_num > 0  then

                           -- 更新定位表的已定位数量
                           update   BILL_UM_DIRECT  mm
                           set mm.est_qty = nvl(mm.est_qty,0) + STRREMAININGQTY
                           where mm.locno = STRLOCNO
                              and mm.untread_mm_no = strUntreadMMNo
                              and mm.item_no = R_ITEM.Item_No
                              and mm.size_no = R_ITEM.Size_No
                              and mm.cell_no =v_exption_cell_no
                              and mm.cell_id = v_exption_cell_id
                              and mm.pack_qty = v_PACK_QTY ;

                           -- 如果更新不到数据，则提示异常
                           if sql%rowcount <> 1 then
                                   STRRESULT:='N|更新定位数据，储位编码：'||v_exption_cell_no||'，ID：'||v_exption_cell_id||'，商品编码；'||R_ITEM.Item_No||'，尺码；'||R_ITEM.Size_No||'
                                         的时异常!';
                                   return;
                           end if;

                       else

                           BEGIN
                                select nvl(max(w.row_id),0) into v_max_row_id  from  BILL_UM_DIRECT  w
                                where w.untread_mm_no = strUntreadMMNo and w.locno = STRLOCNO;
                           EXCEPTION
                                WHEN NO_DATA_FOUND THEN
                                v_max_row_id :=0;
                           END;

                           -- 插入定位表
                           INSERT INTO BILL_UM_DIRECT
                              (LOCNO,
                               ROW_ID,
                               UNTREAD_MM_NO,
                               ITEM_NO,
                               SIZE_NO,
                               PACK_QTY,
                               EST_QTY,
                               CELL_NO,
                               CELL_id,
                               CREATETM)
                            VALUES
                              (STRLOCNO,
                               v_max_row_id+1,
                               strUntreadMMNo,--验收单号
                               R_ITEM.Item_No,
                               R_ITEM.Size_No,
                               v_PACK_QTY,
                               STRREMAININGQTY,
                               v_exption_cell_no,
                               v_exption_cell_id,
                               sysdate); --将该储位放满

                               /*if  R_ITEM.Item_No is null  or  STRREMAININGQTY < 1 then
                                             STRRESULT:='N|新增定位记录，预上储位编码：'||v_exption_cell_no||'，ID：'||v_exption_cell_id||'时参数非法!';
                                             return;
                               end if;*/

                               -- 如果更新不到数据，则提示异常
                               if sql%rowcount <> 1 then
                                       STRRESULT:='N|新增定位数据，储位编码：'||v_exption_cell_no||'，ID：'||v_exption_cell_id||'，商品编码；'||R_ITEM.Item_No||'，尺码；'||R_ITEM.Size_No||'
                                             的时异常!';
                                       return;
                               end if;
                       end if;
                       --log20140427 modi by chenhaitao 赋值来源单号变量
                       v_strSourceNo:=strUntreadMMNo;
                       --end log20140427

          else  -- 如果没有通知单号，则通过验收单号记账

                 --updt by crm 20140110 统一库存记账，写储储位预上量
                   --开始
                   begin
                         acc_prepare_data_ext(strCheckNoList ,
                                                        'UC' ,
                                                        'I' ,
                                                        strWorkerNo ,
                                                        STR_I ,
                                                         '',
                                                        STRLOCNO,
                                                        v_exption_cell_no,
                                                        R_ITEM.ITEM_NO,
                                                        R_ITEM.SIZE_NO,
                                                        1,
                                                        v_item_type,
                                                        v_quality,
                                                        strOwnerNo,
                                                        v_supplierNo,
                                                       /* STR_BOXNO*/'',
                                                        0,
                                                        0,
                                                        STRREMAININGQTY,
                                                        '0',
                                                        '0',
                                                        '1');
                        --回写箱码储位
                        acc_apply(strCheckNoList,'2','UC','I',1);
                  exception when others then
                        STRRESULT := 'N|' || SQLERRM ||
                        SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
                        RETURN;
                 end;
                  -- log20140427 modi by chenhaitao 查询对应的储位ID，校验有没有写到库存
                  begin
                  SELECT  CON.CELL_ID into v_exption_cell_id
                                         FROM CON_CONTENT CON
                  WHERE CON.LOCNO = STRLOCNO
                                       and con.owner_no = strOwnerNo
                                       AND CON.CELL_NO = v_exption_cell_no
                                       AND CON.ITEM_NO = R_ITEM.ITEM_NO
                                       and con.pack_qty = v_PACK_QTY
                                       and con.quality = v_quality
                                       and con.item_type = v_item_type
                                       and con.supplier_no=v_supplierNo
                                       and con.barcode = v_BARCODE
                                       --log20140427 modi by chenhaitao 写预上后，预上应该是有值的
                                       and con.instock_qty>0
                                       --end log20140427
                                       AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                       AND CON.FLAG = '0'
                                       and con.hm_manual_flag = '1'--允许手工移库
                                       and rownum=1; 
                  exception when no_data_found then 
                    STRRESULT:='N|新增异常区储位：'||v_exption_cell_no||'未找到对应库存！';
                    return;
                  end;
                  -- 查询对应的储位ID
                  SELECT  max(CON.CELL_ID) into v_exption_cell_id
                                         FROM CON_CONTENT CON
                  WHERE CON.LOCNO = STRLOCNO
                                       and con.owner_no = strOwnerNo
                                       AND CON.CELL_NO = v_exption_cell_no
                                       AND CON.ITEM_NO = R_ITEM.ITEM_NO
                                       and con.pack_qty = v_PACK_QTY
                                       and con.quality = v_quality
                                       and con.item_type = v_item_type
                                       and con.supplier_no=v_supplierNo
                                       and con.barcode = v_BARCODE
                                       --log20140427 modi by chenhaitao 写预上后，预上应该是有值的
                                       and con.instock_qty>0
                                       --end log20140427
                                       AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                       AND CON.FLAG = '0'
                                       and con.hm_manual_flag = '1'; --允许手工移库
                   --end log20140427                   

                           BEGIN
                                select nvl(max(w.row_id),0) into v_max_row_id  from  BILL_UM_DIRECT  w
                                where w.untread_mm_no = strCheckNoList and w.locno = STRLOCNO;
                           EXCEPTION
                                WHEN NO_DATA_FOUND THEN
                                v_max_row_id :=0;
                           END;

                           -- 插入定位表
                           INSERT INTO BILL_UM_DIRECT
                              (LOCNO,
                               ROW_ID,
                               UNTREAD_MM_NO,
                               ITEM_NO,
                               SIZE_NO,
                               PACK_QTY,
                               EST_QTY,
                               CELL_NO,
                               CELL_id,
                               CREATETM)
                            VALUES
                              (STRLOCNO,
                               v_max_row_id+1,
                               strCheckNoList,--验收单号
                               R_ITEM.Item_No,
                               R_ITEM.Size_No,
                               v_PACK_QTY,
                               STRREMAININGQTY,
                               v_exption_cell_no,
                               v_exption_cell_id,
                               sysdate); --将该储位放满

                               /*if  R_ITEM.Item_No is null  or  STRREMAININGQTY < 1 then
                                             STRRESULT:='N|新增定位记录，预上储位编码：'||v_exption_cell_no||'，ID：'||v_exption_cell_id||'时参数非法!';
                                             return;
                               end if;*/

                               -- 如果更新不到数据，则提示异常
                               if sql%rowcount <> 1 then
                                       STRRESULT:='N|新增定位数据，储位编码：'||v_exption_cell_no||'，ID：'||v_exption_cell_id||'，商品编码；'||R_ITEM.Item_No||'，尺码；'||R_ITEM.Size_No||'
                                             的时异常!';
                                       return;
                               end if;
                               --log20140427 modi by chenhaitao 赋值来源单号变量
                               v_strSourceNo:=strCheckNoList;
                               --end log20140427
          end if;
          
          --log20140427 modi by chenhaitao 注释定到异常区写异常区的上架指示不是定位指示
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
                              R_ITEM.Item_No,
                              R_ITEM.Size_No,
                              --这时没有验收没有暂存区库存信息
                              '',
                              '',
                              v_exption_cell_no,
                              v_exption_cell_id,
                              STRREMAININGQTY,
                              v_item_type,
                              v_quality,
                              1,
                              v_strSourceNo,
                              v_brand_no,
                              --不知道箱号写N值
                              'N');

                             -- 如果没有插入数据成功，则提示异常
                             if sql%rowcount=0 then
                                    STRRESULT:='N|新增上架任务时发生异常!';
                                    return;
                             end if;
                             --end log20140427

          STR_I := STR_I + 1; --行号自动加1

      end if;

      if  STR_I = 0  then
         STRRESULT := 'N|未预约到任何储位信息，请检查上架策略设置!';
         return;
      end if;

    END LOOP;

  STRRESULT := 'Y|';

  EXCEPTION
    WHEN OTHERS THEN
      STRRESULT := 'N|' || SQLERRM ||
                   SUBSTR(DBMS_UTILITY.FORMAT_ERROR_BACKTRACE, 1, 256);
  END Proc_diff_direct;


  /*
    功能：匹配差异的继续定位，退仓通知单为‘-’或为空的情况，通过验收单来做定位 （如果还定位不到的，直接写异常储位）
    作者：zuo.sw
    日期：2014-4-16
 */

   PROCEDURE Proc_diff_direct_by_check(strLocno           in bill_um_untread_mm.LOCNO%type, --仓别
                                      strOwnerNo          in bill_um_untread_mm.Owner_No%type, --货主
                                      strUntreadMMNo      in bill_um_untread_mm.untread_mm_no%type, --退仓通知单
                                      strCheckNoList      IN   VARCHAR2,-- 验收单号
                                      strWorkerNo         in bill_um_untread_mm.CREATOR%type, --操作人
                                      strResult           out varchar2) AS

    STRREMAININGQTY  NUMBER(10); --剩余上架数量;
    STR_I            NUMBER(10); --定位行号
    V_TEMP_CELL_NO   VARCHAR2(20 char); --临时表的唯一单号
    V_RULL_COUNT      NUMBER(10);  -- 是否有上架策略的规则数据
    v_item_type  Con_Content.item_type%type;  -- 商品类型
    v_quality    Con_Content.Quality%type;-- 品质
    v_PACK_QTY   Con_Content.Pack_Qty%type;-- 包装数量
    v_barcode    Con_Content.Barcode%type; -- 条码
    --v_c_num       NUMBER(10);  -- 统计数量
    v_brand_no    item.brand_no%type; -- 品牌
    v_cat_no      item.cate_no%type; --品类
    v_exption_cell_no  CON_CONTENT.CELL_NO%TYPE; --异常区的储位变量
    v_exption_cell_id  CON_CONTENT.CELL_ID%TYPE; --异常区的储位变量
    v_exption_cell_no_count  NUMBER(10);  -- 异常区的储位的数量
    --v_count_num              NUMBER(10);
    v_max_row_id             NUMBER(10);
    v_supplierNo             item.supplier_no%type;--供应商
    --v_d_same_num             number(10);
    --v_box_no                 BILL_UM_CHECK_DTL.Box_No%type;
    v_z_is_have              number(10);
    v_z_cell_no   CON_CONTENT.Cell_No%TYPE; -- 暂存区的储位编号；
    v_z_cell_id   CON_CONTENT.Cell_Id%TYPE; -- 暂存区的储位ID；
    --log20140427 modi by chenhaitao 用于记录上架明细id
    v_instock_row_id    NUMBER(10);
    --log20140427
    v_same_num   NUMBER(10);  -- 同款的数量
    v_empty_num   NUMBER(10); -- 空储位的数量

  BEGIN

    -- 标记是否有部分数据没有做
    STR_I := 0;

    -- 获取商品属性,品质,包装数量
    select uu.untread_type,uu.quality,uu.pack_qty into v_item_type,v_quality,v_PACK_QTY  from BILL_UM_CHECK   uc
      inner join  bill_um_untread  uu  on uc.untread_no = uu.untread_no
                  and uc.locno = uu.locno  and uc.owner_no = uu.owner_no
      where 1 =1   AND instr(',' || strCheckNoList || ',',
                              ',' || uc.check_no || ',') > 0
                   and rownum=1;


    --循环需要定位的商品
    FOR R_ITEM IN (select cd.item_no,cd.size_no,cd.check_no,cd.box_no,nvl(sum(cd.check_qty),0) difQty from  BILL_UM_CHECK_DTL  cd
                    where cd.locno = strLocno
                    and  nvl(cd.check_qty,0) > 0
                    AND instr(',' || strCheckNoList || ',',
                              ',' || cd.check_no || ',') > 0
                    group by cd.item_no,cd.size_no,cd.check_no,cd.box_no
                        order by  cd.item_no,cd.size_no
                   ) LOOP


      --剩余上架数量等于总上架数量
      STRREMAININGQTY := R_ITEM.difQty;

      -- 填充 品牌和 品类
      select m.brand_no,m.cate_no,m.supplier_no into v_brand_no,v_cat_no,v_supplierNo  from  item m where m.item_no =  R_ITEM.Item_No;

      --判断是否有找到对应的上架策略规则
      SELECT count(*) into V_RULL_COUNT
                       FROM CS_INSTOCK_SETTING M
                      INNER JOIN CS_INSTOCK_SETTINGDTL D
                         ON M.LOCNO = D.LOCNO
                        AND M.SETTING_NO = D.SETTING_NO
                      WHERE M.LOCNO = STRLOCNO
                        AND M.STATUS = '1'
                        AND M.SET_TYPE = '1'-- 退仓上架策略
                        AND ((M.DETAIL_TYPE = '2' AND
                            D.SELECT_VALUE = R_ITEM.ITEM_NO) OR
                            (M.DETAIL_TYPE = '1' AND
                            D.SELECT_VALUE = v_cat_no) OR
                            (M.DETAIL_TYPE = '0' AND
                            D.SELECT_VALUE = v_brand_no));

      if  V_RULL_COUNT = 0  then
          STRRESULT := 'N|品牌'||v_brand_no||'-类别'||v_cat_no||'--商品编码'||R_ITEM.ITEM_NO||'未找到上架策略数据，请先添加!';
          RETURN;
      end if;

      -- 获取条码
      select b.barcode into v_barcode
      from  item_barcode  b
      where b.item_no = R_ITEM.Item_No
          and b.size_no = R_ITEM.Size_No
          and b.pack_qty = v_PACK_QTY
          and b.package_id = 0;



    -- 获取来源储位
    BEGIN
            select distinct b.cell_no into v_z_cell_no  from  con_box  b  where b.box_no = R_ITEM.BOX_NO
            and  b.locno = strLocno  and  b.owner_no =  strOwnerNo;
     EXCEPTION
             WHEN NO_DATA_FOUND THEN -- 如果没有找到合法的数据，则继续下一个循环；
                  STRRESULT := 'N|退仓验收单：'||R_ITEM.check_no||'，商品编码:' || R_ITEM.Item_No  ||'，尺码:'||R_ITEM.Size_No||'，记账数据未找到,请核实!';
             RETURN;
     END;


      --查询暂存区的库存数据
                select count(*) into  v_z_is_have from  CON_CONTENT  zc
                where zc.STATUS = '0' --盘点锁定标识为未锁定
                   AND zc.FLAG = '0' --冻结库存标识为未冻结
                   and zc.item_no = R_ITEM.Item_No
                   and zc.barcode=v_barcode
                   and zc.supplier_no=v_supplierNo
                   and zc.pack_qty = v_PACK_QTY
                   and zc.locno = STRLOCNO
                   and zc.cell_no = v_z_cell_no
                   and zc.owner_no = strOwnerNo
                  -- log  20140314  modify zuo.sw   验收单添加属性和品质   begin
                   and zc.item_type = v_item_type
                   and zc.quality = v_quality
                   -- log  20140314  modify zuo.sw  验收单添加属性和品质  end
                   AND ZC.QTY >= nvl(STRREMAININGQTY,0) + nvl(zc.outstock_qty,0)
                   and zc.cell_no in (select c.cell_no  from CM_DEFAREA k
                              inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                              inner join CM_DEFCELL  c  on   t.locno = c.locno
                                     and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
                              where k.locno = STRLOCNO
                                and k.AREA_ATTRIBUTE = 1-- 暂存区
                                and k.ATTRIBUTE_TYPE = 6 --6:退货
                                and k.area_usetype = 1 -- 库区用途 普通存储
                                and c.cell_status = '0'
                                and c.check_status = '0');
                if v_z_is_have = 0 then
                    STRRESULT := 'N|商品编码：'||R_ITEM.Item_No||'，条码：'||v_barcode||'，属性:'||v_item_type||',品质:'||v_quality||',供应商：'||v_supplierNo||'未找到对应的进货暂存区数据，请检查验收记账!';
                    return;
                end if;

                -- 查询对应的暂存区的储位ID和储位编码
                select zc.cell_id into v_z_cell_id  from  CON_CONTENT  zc
               where zc.STATUS = '0' --盘点锁定标识为未锁定
                   AND zc.FLAG = '0' --冻结库存标识为未冻结
                   and zc.item_no = R_ITEM.Item_No
                   and zc.barcode=v_barcode
                   and zc.supplier_no=v_supplierNo
                   and zc.pack_qty = v_PACK_QTY
                   and zc.locno = STRLOCNO
                   and zc.owner_no = strOwnerNo
                   and zc.cell_no = v_z_cell_no
                  -- log  20140314  modify zuo.sw   验收单添加属性和品质   begin
                   and zc.item_type = v_item_type
                   and zc.quality = v_quality
                   -- log  20140314  modify zuo.sw  验收单添加属性和品质  end
                   AND ZC.QTY >= nvl(STRREMAININGQTY,0) + nvl(zc.outstock_qty,0)
                   and zc.cell_no in (select c.cell_no  from CM_DEFAREA k
                              inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                              inner join CM_DEFCELL  c  on   t.locno = c.locno
                                     and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
                              where k.locno = STRLOCNO
                                and k.AREA_ATTRIBUTE = 1-- 暂存区
                                and k.ATTRIBUTE_TYPE = 6 --6:退货
                                and k.area_usetype = 1 -- 库区用途 普通存储
                                and c.cell_status = '0'
                                and c.check_status = '0')
                   and rownum = 1;

      --updt by crm 20140110 统一库存记账，获取到暂存区的储位ID和储位编码,直接更新掉暂存区储位的预下数量 (加上暂存区的预下数量)
      --开始
      begin
         ACC_PREPARE_DATA_EXT(R_ITEM.check_no,'UC','I',STRWORKERNO,1,I_LOCNO => STRLOCNO,I_OWNER_NO =>strOwnerNo,
         I_CELL_ID =>v_z_cell_id,I_CELL_NO =>v_z_cell_no ,I_QTY=>0,I_OUTSTOCK_QTY =>STRREMAININGQTY  );
        --回写箱码储位
        -- update con_box x set x.cell_no=v_z_cell_no where x.locno=STRLOCNO and x.owner_no=strOwnerNo and x.box_no=R_ITEM.Box_No;
        acc_apply(R_ITEM.check_no,'2','UC','I',1);
     exception when others then
                 STRRESULT := 'N|' || SQLERRM ||
                SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
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
                        AND M.SET_TYPE = '1' -- 退仓上架策略
                        AND ((M.DETAIL_TYPE = '2' AND
                            D.SELECT_VALUE = R_ITEM.ITEM_NO) OR
                            (M.DETAIL_TYPE = '1' AND
                            D.SELECT_VALUE = v_cat_no) OR
                            (M.DETAIL_TYPE = '0' AND
                            D.SELECT_VALUE = v_brand_no))
                      ORDER BY M.DETAIL_TYPE DESC) LOOP

        --如果待上架数量等于零，则跳过本次循环
        IF (STRREMAININGQTY = 0) THEN
           EXIT;
        END IF;

        --==============================取出所有符合规则的储位开始======================================

        -- 取一个唯一编号，用于插入储位临时表时使用
        --PKG_WMS_BASE.proc_getsheetno(STRLOCNO,'SJU',V_TEMP_CELL_NO,STRRESULT); --返回 执行结果
        
        if  STR_I = 0 then
            -- 取一个唯一编号，用于插入储位临时表时使用
            PKG_WMS_BASE.proc_getsheetnoother(STRLOCNO,'SJU',V_TEMP_CELL_NO,STRRESULT); --返回 执行结果
        else 
          
            select  STRLOCNO||''||'SJU'||''||(substr(max(h.id),-11)+1) into  V_TEMP_CELL_NO from
            (select distinct a.id   from  SYS_CELL_ALL   a 
              union all select distinct  t.id   from  SYS_CELL_TYPE   t
              union all select distinct  s.id   from  SYS_CELL_STYLE  s
              union all select distinct  e.id   from  SYS_CELL_EMPTY  e
              union all select distinct o.id  from  SYS_CELL_OTHER  o) h;
            
        end if;

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


          -- 查询所有跟当前商品的商品属性相同的储位，插入到同类型储位表
          BEGIN
                  insert into SYS_CELL_TYPE
                    select V_TEMP_CELL_NO,c.cell_no  from CM_DEFAREa k
                    inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                    inner join CM_DEFCELL  c  on   t.locno = c.locno
                           and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
                    where  k.locno = STRLOCNO
                      and c.item_type = v_item_type -- 属性
                      and c.area_quality = v_quality --品质
                      and c.cell_no in(select CELL_NO  from  SYS_CELL_ALL where id = V_TEMP_CELL_NO)
                      and c.cell_status = '0'
                      and c.check_status ='0'
                      and k.AREA_USETYPE in('1','6')-- 普通存储区，贵重物品区
                      and k.AREA_ATTRIBUTE ='0'  -- 作业区
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
                 and con.item_type = v_item_type
                 and con.quality = v_quality
                 AND con.cell_no in (select CELL_NO  from  SYS_CELL_TYPE
                   where id = V_TEMP_CELL_NO);
          END IF;

          --如果选择了空储位优先
          IF (r_rule.EMPTY_CELL_FLAG = '1') THEN

            /*    -- 同款的数量
             select  count(*) into v_d_same_num  from SYS_CELL_STYLE where id = V_TEMP_CELL_NO;

                -- 如果选择了同款临近
                IF (R_RULE.SAME_ITEM_FLAG = '1' and v_d_same_num > 0) THEN
                  INSERT INTO SYS_CELL_EMPTY
                    select V_TEMP_CELL_NO,tc.CELL_NO From SYS_CELL_TYPE tc
                    where tc.id = V_TEMP_CELL_NO
                        and  not exists (
                            select t.cell_no from con_content t ,cm_defcell ll
                            where t.locno=ll.locno and t.owner_no=ll.owner_no
                              and t.locno = STRLOCNO
                              and t.cell_no=ll.cell_no and ll.CELL_STATUS='0'
                              and (t.qty > 0 or t.instock_qty >0 or t.outstock_qty >0))
                        and not exists(select * from SYS_CELL_STYLE where id = V_TEMP_CELL_NO);

                else */ -- 如果没有选择同款临近
                    INSERT INTO SYS_CELL_EMPTY
                    select V_TEMP_CELL_NO,tc.CELL_NO From SYS_CELL_TYPE  tc where tc.id = V_TEMP_CELL_NO and  not exists (
                        select t.cell_no from con_content t ,cm_defcell ll
                        where t.locno=ll.locno and t.owner_no=ll.owner_no
                        and t.locno = STRLOCNO
                        and t.cell_no=ll.cell_no 
                        --and ll.CELL_STATUS='0'
                        and (t.qty > 0 or t.instock_qty >0 or t.outstock_qty >0) );
               /* end if;*/

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
            FOR R_STYLE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_STYLE
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO ASC) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_content_by_check(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.Check_No,R_ITEM.ITEM_NO,
                R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_STYLE_CELL.Cell_No,v_z_cell_id,v_z_cell_no,R_ITEM.box_no,v_brand_no,strWorkerNo,v_supplierNo,'S',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_EMPTY_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_EMPTY
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO ASC) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_content_by_check(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.Check_No,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_EMPTY_CELL.Cell_No,v_z_cell_id,v_z_cell_no,R_ITEM.box_no,v_brand_no,strWorkerNo,v_supplierNo,'P',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_TYPE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_OTHER
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO ASC) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_content_by_check(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.Check_No,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_TYPE_CELL.Cell_No,v_z_cell_id,v_z_cell_no,R_ITEM.box_no,v_brand_no,strWorkerNo,v_supplierNo,'O',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_STYLE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_STYLE
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;


                 -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_content_by_check(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.Check_No,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_STYLE_CELL.Cell_No,v_z_cell_id,v_z_cell_no,R_ITEM.box_no,v_brand_no,strWorkerNo,v_supplierNo,'S',STRREMAININGQTY,STR_I,STRRESULT);


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
            FOR R_EMPTY_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_EMPTY
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_content_by_check(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.Check_No,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_EMPTY_CELL.Cell_No,v_z_cell_id,v_z_cell_no,R_ITEM.box_no,v_brand_no,strWorkerNo,v_supplierNo,'P',STRREMAININGQTY,STR_I,STRRESULT);

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
            FOR R_TYPE_CELL IN (SELECT CELL_NO
                           FROM SYS_CELL_OTHER
                           where id = V_TEMP_CELL_NO
                           ORDER BY CELL_NO desc) LOOP

                --如果待上架数量等于零，则跳出本次循环，继续下个商品定位
                IF (STRREMAININGQTY = 0) THEN
                    EXIT;
                END IF;

                -- 调用通用的存储过程
                PKG_UM_CITY_DIRECT_UNTREAD.Proc_diff_add_content_by_check(STRLOCNO,strOwnerNo,strUntreadMMNo,R_ITEM.Check_No,R_ITEM.ITEM_NO,
                       R_ITEM.SIZE_NO,v_barcode,V_PACK_QTY,v_quality,v_item_type,R_TYPE_CELL.Cell_No,v_z_cell_id,v_z_cell_no,R_ITEM.box_no,v_brand_no,strWorkerNo,v_supplierNo,'O',STRREMAININGQTY,STR_I,STRRESULT);


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
      if  STRREMAININGQTY > 0   then

          -- 查找异常区的储位
          select count(*)  into  v_exption_cell_no_count  from CM_DEFAREA k
                    inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                    inner join CM_DEFCELL  c  on   t.locno = c.locno
                           and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
                    where  k.locno = STRLOCNO
                      and k.Area_Usetype = '5'
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
                    inner join CM_DEFSTOCK  t  on k.locno = t.locno   and k.ware_no||''||k.area_no = t.ware_no||''||t.area_no
                    inner join CM_DEFCELL  c  on   t.locno = c.locno
                           and c.ware_no||''||c.area_no||''||c.stock_no = t.ware_no||''||t.area_no||''||t.stock_no
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
                   acc_prepare_data_ext(R_ITEM.Check_No ,
                                                  'UC' ,
                                                  'I' ,
                                                  strWorkerNo ,
                                                  STR_I ,
                                                   '',
                                                  STRLOCNO,
                                                  v_exption_cell_no,
                                                  R_ITEM.ITEM_NO,
                                                  R_ITEM.SIZE_NO,
                                                  1,
                                                  v_item_type,
                                                  v_quality,
                                                  strOwnerNo,
                                                  v_supplierNo,
                                                 /* STR_BOXNO*/'',
                                                  0,
                                                  0,
                                                  STRREMAININGQTY,
                                                  '0',
                                                  '0',
                                                  '1');
                  --执行记账
                  acc_apply(R_ITEM.Check_No,'2','UC','I',1);

                  --log20140427 modi by chenhaitao 取库存校验有没有写到库存
                  begin
                /*  SELECT  CON.CELL_ID into v_exption_cell_id
                                         FROM CON_CONTENT CON
                  WHERE CON.LOCNO = STRLOCNO
                                       and con.owner_no = strOwnerNo
                                       AND CON.CELL_NO = v_exption_cell_no
                                       AND CON.ITEM_NO = R_ITEM.ITEM_NO
                                       and con.pack_qty = v_PACK_QTY
                                       and con.quality = v_quality
                                       and con.item_type = v_item_type
                                       and con.supplier_no=v_supplierNo
                                       and con.barcode = v_BARCODE
                                       --log20140427 modi by chenhaitao 写预上后，预上应该是有值的
                                       and con.instock_qty>0
                                       --end log20140427
                                       AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                       AND CON.FLAG = '0'
                                       and con.hm_manual_flag = '1'--允许手工移库
                                       and rownum=1; */
                      SELECT  CON.CELL_ID into v_exption_cell_id
                               FROM tmp_acc_result  CON where con.row_id=STR_I;
                  exception when no_data_found then 
                    STRRESULT:='N|新增异常区储位：'||v_exption_cell_no||'未找到对应库存！';
                    return;
                  end;
           exception when others then
                STRRESULT := 'N|' || SQLERRM ||
                SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
                RETURN;
          end;       

                  -- 查询对应的储位ID
             /*     SELECT  max(CON.CELL_ID) into v_exption_cell_id
                                         FROM CON_CONTENT CON
                  WHERE CON.LOCNO = STRLOCNO
                                       and con.owner_no = strOwnerNo
                                       AND CON.CELL_NO = v_exption_cell_no
                                       AND CON.ITEM_NO = R_ITEM.ITEM_NO
                                       and con.pack_qty = v_PACK_QTY
                                       and con.quality = v_quality
                                       and con.item_type = v_item_type
                                       and con.supplier_no=v_supplierNo
                                       and con.barcode = v_BARCODE
                                       --log20140427 modi by chenhaitao 写预上后，预上应该是有值的
                                       and con.instock_qty>0
                                       --end log20140427
                                       AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                       AND CON.FLAG = '0'
                                       and con.hm_manual_flag = '1'; --允许手工移库
                  --end log20140427*/
                           
                           BEGIN
                                select nvl(max(w.row_id),0) into v_max_row_id  from  BILL_UM_DIRECT  w
                                where w.untread_mm_no = strCheckNoList and w.locno = STRLOCNO;
                           EXCEPTION
                                WHEN NO_DATA_FOUND THEN
                                v_max_row_id :=0;
                           END;
                           
                           -- 插入定位表
                           INSERT INTO BILL_UM_DIRECT
                              (LOCNO,
                               ROW_ID,
                               UNTREAD_MM_NO,
                               ITEM_NO,
                               SIZE_NO,
                               PACK_QTY,
                               EST_QTY,
                               CELL_NO,
                               CELL_id,
                               CREATETM)
                            VALUES
                              (STRLOCNO,
                               v_max_row_id+1,
                               R_ITEM.Check_No,--验收单号
                               R_ITEM.Item_No,
                               R_ITEM.Size_No,
                               v_PACK_QTY,
                               STRREMAININGQTY,
                               v_exption_cell_no,
                               v_exption_cell_id,
                               sysdate); --将该储位放满
                               

                               /*if  R_ITEM.Item_No is null  or  STRREMAININGQTY < 1 then
                                             STRRESULT:='N|新增定位记录，预上储位编码：'||v_exption_cell_no||'，ID：'||v_exption_cell_id||'时参数非法!';
                                             return;
                               end if;*/

                               -- 如果更新不到数据，则提示异常
                               if sql%rowcount <> 1 then
                                       STRRESULT:='N|新增定位数据，储位编码：'||v_exption_cell_no||'，ID：'||v_exption_cell_id||'，商品编码；'||R_ITEM.Item_No||'，尺码；'||R_ITEM.Size_No||'
                                             的时异常!';
                                       return;
                               end if;
                            --log20140427 modi by chenhaitao 注释定到异常区写异常区的上架指示不是定位指示
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
                              R_ITEM.Item_No,
                              R_ITEM.Size_No,
                              v_z_cell_no,
                              v_z_cell_id,
                              v_exption_cell_no,
                              v_exption_cell_id,
                              STRREMAININGQTY,
                              v_item_type,
                              v_quality,
                              1,
                             R_ITEM.Check_No,
                              v_brand_no,
                              R_ITEM.Box_No);

                             -- 如果没有插入数据成功，则提示异常
                             if sql%rowcount=0 then
                                    STRRESULT:='N|新增上架任务时发生异常!';
                                    return;
                             end if;
                             --end log20140427



          STR_I := STR_I + 1; --行号自动加1

      end if;

    END LOOP;
    
    if  STR_I = 0  then
         STRRESULT := 'N|未预约到任何储位信息，请检查上架策略设置!';
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
  END Proc_diff_direct_by_check;



  /*
     功能:  退仓-匹配差异定位 写库存属性和库存
     作者:  zuo.sw
     日期:  2013-1-17
    */
  PROCEDURE Proc_diff_add_con_content(STR_LOCNO   IN BILL_UM_UNTREAD_MM.LOCNO%TYPE, --仓别
                                 STR_OWNER_NO  IN BILL_UM_UNTREAD_MM.Owner_No%TYPE, -- 委托业主
                                 STR_UNTREAD_MM_NO  IN BILL_UM_UNTREAD_MM.Untread_Mm_No%TYPE,--通知单号
                                 STR_UC_CHECK_NO    IN BILL_UM_CHECK.CHECK_NO%TYPE,-- 验收单号
                                 STR_ITEMNO  IN Con_Content.item_no%TYPE, -- 商品编码
                                 STR_SIZENO  IN  BILL_UM_UNTREAD_MM_Dtl.Size_No%type, --尺码
                                 STR_BARCODE  IN Con_Content.Barcode%TYPE, -- 条码
                                 STR_PACKQTY  IN Con_Content.Pack_Qty%TYPE, -- 包装数量
                                 STR_quality  IN Con_Content.Quality%TYPE, -- 品质
                                 STR_ITEM_TYPE IN Con_Content.Item_Type%type,--商品属性
                                 STR_CELLNO  IN Con_Content.Cell_No%TYPE, -- 储位编码
                                 STR_WORKERNO  IN BILL_UM_UNTREAD_MM.CREATOR%TYPE, --操作人
                                 STR_SUPPLIERNO  IN ITEM.SUPPLIER_NO%TYPE,--供应商
                                 STR_FLAG     IN   VARCHAR2, -- 标记S,P,O
                                 STRREMAININGQTY in out number,
                                 STR_I  in out number,
                                 STRRESULT  OUT VARCHAR2)  as

    STR_QTY          CON_CONTENT.QTY%TYPE; --库存数量
    STR_INSTOCK_QTY  CON_CONTENT.INSTOCK_QTY%TYPE; --预上数量
    STR_LIMIT_QTY    CS_CELL_PACK_SETTING.LIMIT_QTY%TYPE; --最大存放数量
    STR_MIX_FLAG     CM_DEFCELL.MIX_FLAG%TYPE; --混放货物标识
    V_NCELLID        CON_CONTENT.CELL_ID%TYPE; --库存Cell_ID
    V_CELL_ALL_COUNT NUMBER(10);  -- 上架范围的所有储位的数量
    V_INSTOCK_NUM    NUMBER(10); -- 当前的上架数量
    v_item_pack_spec item_pack.pack_spec%type; -- 商品包装规则
    v_max_row_id     BILL_UM_DIRECT.Row_Id%type; -- 行号
    v_count_num      NUMBER(10);  -- 汇总数量
    v_dif_item_no_num   NUMBER(10);  -- 当前储位不同的商品数
    v_cell_no_num   NUMBER(10);  -- 当前储位的记录数
    v_ACC_NO       VARCHAR2(20 char);-- 记账单号

    begin

                  -- 判断商品是否有设置包装规格
                  BEGIN
                      SELECT mpk.pack_spec
                        INTO v_item_pack_spec
                        FROM  ITEM_PACK  MPK
                       WHERE MPK.ITEM_NO = STR_ITEMNO
                         and mpk.pack_qty = STR_PACKQTY
                         AND ROWNUM = 1;

                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码:' || STR_ITEMNO ||'未设置包装规格，请先设置!';
                        RETURN;
                  END;

                  --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
                  BEGIN
                      SELECT LIMIT_QTY
                        INTO STR_LIMIT_QTY
                        FROM CS_CELL_PACK_SETTING PS
                      where  PS.LOCNO = STR_LOCNO
                         and ps.owner_no = STR_OWNER_NO
                         AND PS.AREA_TYPE = (select k.area_type  from CM_DEFAREa k
                                inner join CM_DEFCELL  c  on   k.locno = c.locno
                                       and (c.ware_no||''||c.area_no) = (k.ware_no||''||k.area_no)
                                where  c.locno = STR_LOCNO
                                and c.cell_no = STR_CELLNO and rownum =1 )
                         and ps.pack_spec = v_item_pack_spec
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


                        -- 如果有通知单号
                       if STR_UNTREAD_MM_NO <> '-' and STR_UNTREAD_MM_NO <> 'N' and STR_UNTREAD_MM_NO is not null then
                             v_ACC_NO := STR_UNTREAD_MM_NO;
                       else
                             v_ACC_NO := STR_UC_CHECK_NO;
                       end if;


                             -- 查询对应的储位ID
                             SELECT count(*) into V_CELL_ALL_COUNT
                                 FROM v_content CON
                               WHERE CON.LOCNO = STR_LOCNO
                                   and con.owner_no = STR_OWNER_NO
                                   AND CON.CELL_NO = STR_CELLNO
                                   AND CON.ITEM_NO = STR_ITEMNO
                                   and con.pack_qty = STR_PACKQTY
                                   and con.quality = STR_quality
                                   and con.item_type = STR_ITEM_TYPE
                                   and con.supplier_no=STR_SUPPLIERNO
                                   and con.barcode = STR_BARCODE
                                   AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                   AND CON.FLAG = '0'
                                   and con.hm_manual_flag = '1'; --允许手工移库

                             -- 如果有对应的储位库存信息，而且是同款储位或排除同款和空储位的其他储位
                             if  ((STR_FLAG ='O' OR  STR_FLAG ='S') AND  V_CELL_ALL_COUNT > 0) then
                                 -- 查询对应的储位ID
                                 SELECT  max(CON.CELL_ID) into V_NCELLID
                                     FROM CON_CONTENT CON
                                 WHERE CON.LOCNO = STR_LOCNO
                                   and con.owner_no = STR_OWNER_NO
                                   AND CON.CELL_NO = STR_CELLNO
                                   AND CON.ITEM_NO = STR_ITEMNO
                                   and con.pack_qty = STR_PACKQTY
                                   and con.quality = STR_quality
                                   and con.item_type = STR_ITEM_TYPE
                                   and con.supplier_no=STR_SUPPLIERNO
                                   and con.barcode = STR_BARCODE
                                   AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                   AND CON.FLAG = '0'
                                   and con.hm_manual_flag = '1'; --允许手工移库

                                   --updt by crm 库存统一记账
                                   begin
                                           ACC_PREPARE_DATA_EXT(v_ACC_NO,'UC','I',STR_WORKERNO,STR_I,I_LOCNO =>STR_LOCNO,I_OWNER_NO =>STR_OWNER_NO,
                                           I_CELL_ID =>V_NCELLID,I_CELL_NO =>STR_CELLNO ,I_QTY=>0,I_INSTOCK_QTY =>V_INSTOCK_NUM  );
                                           acc_apply(v_ACC_NO,'2','UC','I',1);
                                      --结束
                                       exception when others then
                                       STRRESULT := 'N|' || SQLERRM ||
                                      SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
                                      RETURN;
                                     end;
                              end if;

                              -- 如果是空储位，或者是同款储位，其他储位且找不到储位信息的，就全部新增
                              if (((STR_FLAG ='O' OR  STR_FLAG ='S') AND  V_CELL_ALL_COUNT < 1) OR STR_FLAG ='P')  then

                                        --updt by crm 20140110 统一库存记账，写储储位预上量
                                        --开始
                                        begin
                                              acc_prepare_data_ext(v_ACC_NO ,
                                                                  'UC' ,
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
                                                                  STR_quality,
                                                                  STR_OWNER_NO,
                                                                  STR_SUPPLIERNO,
                                                                 /* STR_BOXNO*/'',
                                                                  0,
                                                                  0,
                                                                  V_INSTOCK_NUM,
                                                                  '0',
                                                                  '0',
                                                                  '1');
                                            --回写箱码储位
                                             acc_apply(v_ACC_NO,'2','UC','I',1);
                                       exception when others then
                                             STRRESULT := 'N|' || SQLERRM ||
                                            SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
                                            RETURN;
                                      end;
                                      if  STR_ITEMNO is null   or  V_INSTOCK_NUM < 1 then
                                           STRRESULT:='N|新增存储区储位：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码'||STR_ITEMNO||'，库存记录时，参数非法!';
                                           return;
                                      end if;

                                      -- 如果更新不到数据，则提示异常
                                      if sql%rowcount=0 then
                                           STRRESULT:='N|新增存储区储位：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码；'||STR_ITEMNO||'
                                           的库存记录时异常!';
                                           return;
                                      end if;


                                      -- 查询对应的储位ID
                                     SELECT  max(CON.CELL_ID) into V_NCELLID
                                         FROM CON_CONTENT CON
                                     WHERE CON.LOCNO = STR_LOCNO
                                       and con.owner_no = STR_OWNER_NO
                                       AND CON.CELL_NO = STR_CELLNO
                                       AND CON.ITEM_NO = STR_ITEMNO
                                       and con.pack_qty = STR_PACKQTY
                                       and con.quality = STR_quality
                                       and con.item_type = STR_ITEM_TYPE
                                       and con.supplier_no=STR_SUPPLIERNO
                                       and con.barcode = STR_BARCODE;
                                       --AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                       --AND CON.FLAG = '0'
                                       --and con.hm_manual_flag = '1'; --允许手工移库


                              end if;

                      -- 如果有通知单号
                       if STR_UNTREAD_MM_NO <> '-' and STR_UNTREAD_MM_NO <> 'N' and STR_UNTREAD_MM_NO is not null then
                              -- 判断是否有存在的定位信息；
                               select count(*) into v_count_num  from  BILL_UM_DIRECT  um  where um.locno = STR_LOCNO and um.untread_mm_no =STR_UNTREAD_MM_NO
                               and um.item_no =STR_ITEMNO and um.size_no =STR_SIZENO and um.cell_no =STR_CELLNO and um.cell_id = V_NCELLID
                               and um.pack_qty = STR_PACKQTY ;

                               if  v_count_num > 0  then

                                   -- 更新定位表的已定位数量
                                   update   BILL_UM_DIRECT  mm
                                   set mm.est_qty = nvl(mm.est_qty,0) + V_INSTOCK_NUM
                                   where mm.locno = STR_LOCNO
                                      and mm.untread_mm_no =STR_UNTREAD_MM_NO
                                      and mm.item_no =STR_ITEMNO
                                      and mm.size_no =STR_SIZENO
                                      and mm.cell_no =STR_CELLNO
                                      and mm.cell_id = V_NCELLID
                                      and mm.pack_qty = STR_PACKQTY ;

                                   -- 如果更新不到数据，则提示异常
                                   if sql%rowcount=0 then
                                           STRRESULT:='N|更新定位数据，储位编码：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码；'||STR_ITEMNO||'，尺码；'||STR_SIZENO||'
                                                 的时异常!';
                                           return;
                                   end if;

                               else

                                   BEGIN
                                        select nvl(max(w.row_id),0) into v_max_row_id  from  BILL_UM_DIRECT  w
                                        where w.untread_mm_no = STR_UNTREAD_MM_NO and w.locno = STR_LOCNO;
                                   EXCEPTION
                                        WHEN NO_DATA_FOUND THEN
                                        v_max_row_id :=0;
                                   END;

                                   -- 插入定位表
                                   INSERT INTO BILL_UM_DIRECT
                                      (LOCNO,
                                       ROW_ID,
                                       UNTREAD_MM_NO,
                                       ITEM_NO,
                                       SIZE_NO,
                                       PACK_QTY,
                                       EST_QTY,
                                       CELL_NO,
                                       CELL_id,
                                       CREATETM)
                                    VALUES
                                      (STR_LOCNO,
                                       v_max_row_id+1,
                                       STR_UNTREAD_MM_NO,--验收单号
                                       STR_ITEMNO,
                                       STR_SIZENO,
                                       STR_PACKQTY,
                                       V_INSTOCK_NUM,
                                       STR_CELLNO,
                                       V_NCELLID,
                                       sysdate); --将该储位放满

                                      /* if  STR_ITEMNO is null  or  V_INSTOCK_NUM < 1 or STR_CELLNO is null
                                            or  STR_CELLNO ='N' OR  V_NCELLID is null or V_NCELLID < 1  then
                                                     STRRESULT:='N|继续定位，新增定位记录，储位和商品数据非法!';
                                                     return;
                                       end if;*/

                                       -- 如果更新不到数据，则提示异常
                                       if sql%rowcount=0 then
                                                     STRRESULT:='N|继续定位，新增定位记录，预上储位编码：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码；'||STR_ITEMNO||'
                                                     的时异常!';
                                                     return;
                                       end if;

                               end if;

                          else -- 根据验收单号来写定位表

                                   BEGIN
                                        select nvl(max(w.row_id),0) into v_max_row_id  from  BILL_UM_DIRECT  w
                                        where w.untread_mm_no = STR_UC_CHECK_NO and w.locno = STR_LOCNO;
                                   EXCEPTION
                                        WHEN NO_DATA_FOUND THEN
                                        v_max_row_id :=0;
                                   END;

                                   -- 插入定位表
                                   INSERT INTO BILL_UM_DIRECT
                                      (LOCNO,
                                       ROW_ID,
                                       UNTREAD_MM_NO,
                                       ITEM_NO,
                                       SIZE_NO,
                                       PACK_QTY,
                                       EST_QTY,
                                       CELL_NO,
                                       CELL_id,
                                       CREATETM)
                                    VALUES
                                      (STR_LOCNO,
                                       v_max_row_id+1,
                                       STR_UC_CHECK_NO,--验收单号
                                       STR_ITEMNO,
                                       STR_SIZENO,
                                       STR_PACKQTY,
                                       V_INSTOCK_NUM,
                                       STR_CELLNO,
                                       V_NCELLID,
                                       sysdate); --将该储位放满

                                      /* if  STR_ITEMNO is null  or  V_INSTOCK_NUM < 1 or STR_CELLNO is null
                                            or  STR_CELLNO ='N' OR  V_NCELLID is null or V_NCELLID < 1  then
                                                     STRRESULT:='N|继续定位，新增定位记录，储位和商品数据非法!';
                                                     return;
                                       end if;*/

                                       -- 如果更新不到数据，则提示异常
                                       if sql%rowcount=0 then
                                                     STRRESULT:='N|继续定位，新增定位记录，预上储位编码：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码；'||STR_ITEMNO||'
                                                     的时异常!';
                                                     return;
                                       end if;

                       end if;

                 STR_I := STR_I + 1; --行号自动加1
     end if;

  END Proc_diff_add_con_content;



  /*
     功能:  退仓-匹配差异定位 写库存属性和库存  通过验收单
     作者:  zuo.sw
     日期:  2013-1-17
    */
  PROCEDURE Proc_diff_add_content_by_check(STR_LOCNO   IN BILL_UM_UNTREAD_MM.LOCNO%TYPE, --仓别
                                 STR_OWNER_NO  IN BILL_UM_UNTREAD_MM.Owner_No%TYPE, -- 委托业主
                                 STR_UNTREAD_MM_NO  IN BILL_UM_UNTREAD_MM.Untread_Mm_No%TYPE,--通知单号
                                 STR_UC_CHECK_NO    IN BILL_UM_CHECK.CHECK_NO%TYPE,-- 验收单号
                                 STR_ITEMNO  IN Con_Content.item_no%TYPE, -- 商品编码
                                 STR_SIZENO  IN  BILL_UM_UNTREAD_MM_Dtl.Size_No%type, --尺码
                                 STR_BARCODE  IN Con_Content.Barcode%TYPE, -- 条码
                                 STR_PACKQTY  IN Con_Content.Pack_Qty%TYPE, -- 包装数量
                                 STR_quality  IN Con_Content.Quality%TYPE, -- 品质
                                 STR_ITEM_TYPE IN Con_Content.Item_Type%type,--商品属性
                                 STR_CELLNO  IN Con_Content.Cell_No%TYPE, -- 储位编码
                                 STR_Z_CELLID  IN Con_Content.Cell_Id%TYPE,--  来源储位ID
                                 STR_Z_CELLNO  IN Con_Content.Cell_NO%TYPE,--  来源储位编码
                                 STR_BOX_NO    in  BILL_UM_CHECK_dtl.Box_No%type, -- 箱号
                                 STR_BRAND_NO  in  BILL_UM_CHECK_dtl.Brand_No%type, -- 品牌编码
                                 STR_WORKERNO  IN BILL_UM_UNTREAD_MM.CREATOR%TYPE, --操作人
                                 STR_SUPPLIERNO  IN ITEM.SUPPLIER_NO%TYPE,--供应商
                                 STR_FLAG     IN   VARCHAR2, -- 标记S,P,O
                                 STRREMAININGQTY in out number,
                                 STR_I  in out number,
                                 STRRESULT  OUT VARCHAR2)  as

    STR_QTY          CON_CONTENT.QTY%TYPE; --库存数量
    STR_INSTOCK_QTY  CON_CONTENT.INSTOCK_QTY%TYPE; --预上数量
    STR_LIMIT_QTY    CS_CELL_PACK_SETTING.LIMIT_QTY%TYPE; --最大存放数量
    STR_MIX_FLAG     CM_DEFCELL.MIX_FLAG%TYPE; --混放货物标识
    V_NCELLID        CON_CONTENT.CELL_ID%TYPE; --库存Cell_ID
    V_CELL_ALL_COUNT NUMBER(10);  -- 上架范围的所有储位的数量
    V_INSTOCK_NUM    NUMBER(10); -- 当前的上架数量
    v_item_pack_spec item_pack.pack_spec%type; -- 商品包装规则
    --v_max_row_id     BILL_UM_DIRECT.Row_Id%type; -- 行号
    --v_count_num      NUMBER(10);  -- 汇总数量
    v_dif_item_no_num   NUMBER(10);  -- 当前储位不同的商品数
    v_cell_no_num   NUMBER(10);  -- 当前储位的记录数
    v_instock_row_id    NUMBER(10);
 --   v_ACC_NO       VARCHAR2(20 char);-- 记账单号
    

    begin
                  --log20140427 modi by chenhaitao 先报出错误信息
                  --STRRESULT:='N|';
                  --end log20140427
                  -- 判断商品是否有设置包装规格
                  BEGIN
                      SELECT mpk.pack_spec
                        INTO v_item_pack_spec
                        FROM  ITEM_PACK  MPK
                       WHERE MPK.ITEM_NO = STR_ITEMNO
                         and mpk.pack_qty = STR_PACKQTY
                         AND ROWNUM = 1;

                  EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                        STRRESULT := 'N|商品编码:' || STR_ITEMNO ||'未设置包装规格，请先设置!';
                        RETURN;
                  END;

                  --获取该储位对应的通道的货架允许存放鞋盒的总数（暂定认为一种鞋包装一致，不按尺码计算体积）
                  BEGIN
                      SELECT LIMIT_QTY
                        INTO STR_LIMIT_QTY
                        FROM CS_CELL_PACK_SETTING PS
                      where  PS.LOCNO = STR_LOCNO
                         and ps.owner_no = STR_OWNER_NO
                         AND PS.AREA_TYPE = (select k.area_type  from CM_DEFAREa k
                                inner join CM_DEFCELL  c  on   k.locno = c.locno
                                       and (c.ware_no||''||c.area_no) = (k.ware_no||''||k.area_no)
                                where  c.locno = STR_LOCNO
                                and c.cell_no = STR_CELLNO and rownum =1 )
                         and ps.pack_spec = v_item_pack_spec
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

                             -- 查询对应的储位ID
                             SELECT count(*) into V_CELL_ALL_COUNT
                                 FROM v_content CON
                               WHERE CON.LOCNO = STR_LOCNO
                                   and con.owner_no = STR_OWNER_NO
                                   AND CON.CELL_NO = STR_CELLNO
                                   AND CON.ITEM_NO = STR_ITEMNO
                                   and con.pack_qty = STR_PACKQTY
                                   and con.quality = STR_quality
                                   and con.item_type = STR_ITEM_TYPE
                                   and con.supplier_no=STR_SUPPLIERNO
                                   and con.barcode = STR_BARCODE
                                   AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                   AND CON.FLAG = '0'
                                   and con.hm_manual_flag = '1'; --允许手工移库

                             -- 如果有对应的储位库存信息，而且是同款储位或排除同款和空储位的其他储位
  /*                           if  ((STR_FLAG ='O' OR  STR_FLAG ='S') AND  V_CELL_ALL_COUNT > 0) then
                                 -- 查询对应的储位ID
                                 SELECT  max(CON.CELL_ID) into V_NCELLID
                                     FROM CON_CONTENT CON
                                 WHERE CON.LOCNO = STR_LOCNO
                                   and con.owner_no = STR_OWNER_NO
                                   AND CON.CELL_NO = STR_CELLNO
                                   AND CON.ITEM_NO = STR_ITEMNO
                                   and con.pack_qty = STR_PACKQTY
                                   and con.quality = STR_quality
                                   and con.item_type = STR_ITEM_TYPE
                                   and con.supplier_no=STR_SUPPLIERNO
                                   and con.barcode = STR_BARCODE
                                   AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                   AND CON.FLAG = '0'
                                   and con.hm_manual_flag = '1'; --允许手工移库

                                   --updt by crm 20140111 统一库存记账，增加目的储位库存，扣目的遇上库存
                                   --开始
                                   begin
                                         ACC_PREPARE_DATA_EXT(STR_UC_CHECK_NO,'UC','I',STR_WORKERNO,STR_I,I_LOCNO =>STR_LOCNO,I_OWNER_NO =>STR_OWNER_NO,
                                         I_CELL_ID =>V_NCELLID,I_CELL_NO =>STR_CELLNO ,I_QTY=>0,I_INSTOCK_QTY =>V_INSTOCK_NUM  );
                                         acc_apply(STR_UC_CHECK_NO,'2','UC','I',1);
                                   exception when others then
                                         STRRESULT := 'N|' || SQLERRM ||
                                        SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
                                        RETURN;
                                  end;
                                  --结束

                              end if;*/

                              -- 如果是空储位，或者是同款储位，其他储位且找不到储位信息的，就全部新增
                           /*   if (((STR_FLAG ='O' OR  STR_FLAG ='S') AND  V_CELL_ALL_COUNT < 1) OR STR_FLAG ='P')  then*/

                                    --updt by crm 20140110 统一库存记账，写储储位预上量
                                    --开始
                                    begin
                                              acc_prepare_data_ext(STR_UC_CHECK_NO ,
                                                                  'UC' ,
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
                                                                  STR_quality,
                                                                  STR_OWNER_NO,
                                                                  STR_SUPPLIERNO,
                                                                 /* STR_BOXNO*/'',
                                                                  0,
                                                                  0,
                                                                  V_INSTOCK_NUM,
                                                                  '0',
                                                                  '0',
                                                                  '1');
                                            --回写箱码储位
                                               acc_apply(STR_UC_CHECK_NO,'2','UC','I',1);
                                               
                                               begin
                                                 
                                                    select t.cell_id  into V_NCELLID  From tmp_acc_result t where t.row_id=STR_I;
                                                    exception when no_Data_found then
                                                       STRRESULT := 'N|获取储位CELL_ID失败!';
                                                        return;
                                                                         
                                               end;
                                               
                                               
                                         exception when others then
                                               STRRESULT := 'N|' || SQLERRM ||
                                               SUBSTR(DBMS_UTILITY.format_error_backtrace, 1, 256);
                                               RETURN;
                                               
                                         
                                             
                                  end;
                                  --结束
                      /*                if  STR_ITEMNO is null   or  V_INSTOCK_NUM < 1 then
                                           STRRESULT:='N|新增存储区储位：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码'||STR_ITEMNO||'，库存记录时，参数非法!';
                                           return;
                                      end if;

                                      -- 如果更新不到数据，则提示异常
                                      if sql%rowcount=0 then
                                           STRRESULT:='N|新增存储区储位：'||STR_CELLNO||'，ID：'||V_NCELLID||'，商品编码；'||STR_ITEMNO||'
                                           的库存记录时异常!';
                                           return;
                                      end if;*/


                                      -- 查询对应的储位ID
                               /*      SELECT  max(CON.CELL_ID) into V_NCELLID
                                         FROM CON_CONTENT CON
                                     WHERE CON.LOCNO = STR_LOCNO
                                       and con.owner_no = STR_OWNER_NO
                                       AND CON.CELL_NO = STR_CELLNO
                                       AND CON.ITEM_NO = STR_ITEMNO
                                       and con.pack_qty = STR_PACKQTY
                                       and con.quality = STR_quality
                                       and con.item_type = STR_ITEM_TYPE
                                       and con.supplier_no=STR_SUPPLIERNO
                                       and con.barcode = STR_BARCODE;*/
                                       --AND CON.STATUS = '0' --盘点锁定标识为未锁定
                                       --AND CON.FLAG = '0'
                                       --and con.hm_manual_flag = '1'; --允许手工移库
                                       
                                      


                           /*   end if;*/

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
                              (STR_LOCNO,
                              STR_OWNER_NO,
                              v_instock_row_id,
                              '10',
                              STR_ITEMNO,
                              STR_SIZENO,
                              STR_Z_CELLNO,
                              STR_Z_CELLID,
                              STR_CELLNO,
                              V_NCELLID,
                              V_INSTOCK_NUM,
                              STR_ITEM_TYPE,
                              STR_quality,
                              STR_PACKQTY,
                             STR_UC_CHECK_NO,
                              STR_BRAND_NO,
                              STR_Box_No);

                             -- 如果没有插入数据成功，则提示异常
                             if sql%rowcount=0 then
                                    STRRESULT:='N|新增上架任务时发生异常!';
                                    return;
                             end if;

                 STR_I := STR_I + 1; --行号自动加1
                 --log201400427 modi by chenhaitao 写完报成功
                 --STRRESULT:='Y|';
                 --end log20140427
     end if;

  END Proc_diff_add_content_by_check;

END PKG_UM_CITY_DIRECT_UNTREAD;
/
