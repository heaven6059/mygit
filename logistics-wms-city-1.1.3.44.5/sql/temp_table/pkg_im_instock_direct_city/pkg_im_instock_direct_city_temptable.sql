-- 创建所有的上架范围储位的临时表
CREATE  GLOBAL  TEMPORARY  TABLE   SYS_CELL_ALL (
                                  id          VARCHAR2(20),  
                                  CELL_NO     VARCHAR2(30)  
                         )  ON   COMMIT  DELETE   ROWS;

-- 同类型储位的临时表
CREATE  GLOBAL  TEMPORARY  TABLE   SYS_CELL_TYPE (
                                  id          VARCHAR2(20),  
                                  CELL_NO     VARCHAR2(30)  
                         )  ON   COMMIT  DELETE   ROWS;

-- 同款的储位临时表                         
CREATE  GLOBAL  TEMPORARY  TABLE   SYS_CELL_STYLE (
                                  id          VARCHAR2(20),  
                                  CELL_NO     VARCHAR2(30)  
                         )  ON   COMMIT  DELETE   ROWS;

-- 空储位的临时表                         
CREATE  GLOBAL  TEMPORARY  TABLE   SYS_CELL_EMPTY (
                                  id          VARCHAR2(20),  
                                  CELL_NO     VARCHAR2(30)  
                         )  ON   COMMIT  DELETE   ROWS;
                         
-- 排除同款的储位和空储位的临时表                         
CREATE  GLOBAL  TEMPORARY  TABLE   SYS_CELL_OTHER (
                                  id          VARCHAR2(20),  
                                  CELL_NO     VARCHAR2(30)  
                         )  ON   COMMIT  DELETE   ROWS;
                         
                        
--拣货任务分派发单的临时表                      
CREATE  GLOBAL  TEMPORARY  TABLE  SYS_OUTSTOCK_locate (
                                  STORE_NO          VARCHAR2(20),  
                                  ROW_NO            NUMBER(10)  
                         )  ON   COMMIT  DELETE   ROWS;