CREATE OR REPLACE PACKAGE PKG_COMMON_CITY IS

  -- Author  : LUO.HL
  -- Created : 2013-12-11 14:21:07
  -- Purpose : 单据状态日志
  PROCEDURE PROC_BILL_STATUS_LOG(i_locno       IN BILL_STATUS_LOG.locno%TYPE, --仓别
                                 i_bill_no     IN BILL_STATUS_LOG.Bill_No%TYPE, --单据编号
                                 i_bill_type   IN BILL_STATUS_LOG.Bill_Type%TYPE, --单据类型    im ：收货   om：分货   um：退仓
                                 i_status      IN BILL_STATUS_LOG.Status%TYPE, --单据对应的状态
                                 i_description IN BILL_STATUS_LOG.Description%TYPE, --操作内容
                                 i_operator    IN BILL_STATUS_LOG.Operator%TYPE, --操作人
                                 stroutmsg     OUT VARCHAR2 --处理返回的结果
                                 );

END PKG_COMMON_CITY;
/
CREATE OR REPLACE PACKAGE BODY PKG_COMMON_CITY IS
  PROCEDURE PROC_BILL_STATUS_LOG(i_locno       IN BILL_STATUS_LOG.locno%TYPE, --仓别
                                 i_bill_no     IN BILL_STATUS_LOG.Bill_No%TYPE, --单据编号
                                 i_bill_type   IN BILL_STATUS_LOG.Bill_Type%TYPE, --单据类型    im ：收货   om：分货   um：退仓
                                 i_status      IN BILL_STATUS_LOG.Status%TYPE, --单据对应的状态
                                 i_description IN BILL_STATUS_LOG.Description%TYPE, --操作内容
                                 i_operator    IN BILL_STATUS_LOG.Operator%TYPE, --操作人
                                 stroutmsg     OUT VARCHAR2 --处理返回的结果
                                 ) IS
    V_MAX_ROW_ID BILL_STATUS_LOG.Row_Id%TYPE;
  BEGIN
    stroutmsg := 'Y';
    SELECT nvl(MAX(ROW_ID), 0) INTO V_MAX_ROW_ID FROM BILL_STATUS_LOG;
    V_MAX_ROW_ID := V_MAX_ROW_ID + 1;
    --记录状态日志表
    INSERT INTO BILL_STATUS_LOG
      (BILL_NO,
       LOCNO,
       BILL_TYPE,
       ROW_ID,
       STATUS,
       DESCRIPTION,
       OPERATOR,
       OPERATETM)
    VALUES
      (i_bill_no,
       i_locno,
       i_bill_type,
       V_MAX_ROW_ID,
       i_status,
       i_description,
       i_operator,
       SYSDATE);
  END PROC_BILL_STATUS_LOG;
END PKG_COMMON_CITY;
/
