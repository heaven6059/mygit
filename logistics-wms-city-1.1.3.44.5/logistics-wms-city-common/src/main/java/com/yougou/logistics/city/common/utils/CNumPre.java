package com.yougou.logistics.city.common.utils;

/**
 * 单号前缀
 * 
 * @author luo.hl
 * @date 2013-10-14 下午2:14:26
 * @version 0.1.0
 * @copyright yougou.com
 */
public class CNumPre {

    // / 板号前缀

    public static final String PAL_ORDER_PRE = "P";

    // / 容器序列号前缀

    public static final String CONTAINER_PRE = "A";

    // / 进货直通单号前缀

    public static final String IM_ID_PRE = "ID";

    // / 进货存储单号前缀

    public static final String IM_IS_PRE = "IS";

    // / 进货存储汇总单号前缀

    public static final String IM_SS_PRE = "SS";

    // / 进货直通汇总单号前缀

    public static final String IM_SD_PRE = "SD";

    // / 进货验收汇总单号前缀

    public static final String IM_S_CHECK_NO_PRE = "SC";

    // / 进货验收单号前缀

    public static final String IM_CHECK_NO_PRE = "IC";

    // / 上架作业单号前缀

    public static final String IM_INSTOCK_PRE = "IP";

    // / 分播单号前缀

    public static final String DIVIDE_PRE = "DO";

    // / 外复核单号前缀

    public static final String RECHECK_PRE = "OC";

    // / 出货调度单号前缀

    public static final String OM_LOCATE_PRE = "LC";

    // / 装车建议单号前缀

    public static final String LOAD_PROPOSE_PRE = "LP";

    // /出货装车单号前缀
    public static final String OM_DELIVER = "OD";

    // / 配送单号前缀

    public static final String DELIVER_PRE = "DV";

    // / 返配进货单号前缀

    public static final String UM_UNTREAD_NO_PRE = "UM";

    // / 返配汇总单号

    public static final String UM_S_UNTREAD_NO_PRE = "VM";

    // / 返配验收单号前缀

    public static final String UM_CHECK_NO_PRE = "UC";

    // / 返配验收汇总单号前缀

    public static final String UM_S_CHECK_NO_PRE = "XS";

    // / 库存调账单号前缀

    public static final String Con_Adj_PRE = "MO";

    // / 出货下架单号前缀

    public static final String Om_OutStock_PRE = "HO";

    // / 补货下架单号前缀

    public static final String Hm_Outstock_PRE = "HS";

    // / 移库计划单前缀

    public static final String Hm_Move_PRE = "HM";

    // / 库存转换计划单前缀

    public static final String Tm_Move_PRE = "TM";

    // / 品质转换单前缀

    public static final String Con_Quality_PRE = "QC";

    // / 打印任务单前缀

    public static final String Print_Task_PRE = "PNT";

    // / GSP进货拒收单号前缀

    public static final String GSP_IM_REFUSE_PRE = "JS";

    // / 进货验收取消单号前缀

    public static final String IM_CHECKCANCEL_PRE = "QS";

    // / 进货质检单号前缀

    public static final String IM_QC_PRE = "QC";

    // / 进货抽检单号前缀

    public static final String IM_PART_CHECK_PRE = "PC";

    // / 客户别移库下架单前缀

    public static final String TS_OUTSTOCK_PRE = "TS";

    // / 客户别移库计划单前缀

    public static final String TM_OUTSTOCK_PRE = "TM";

    // / 客户别移库验收单前缀

    public static final String TM_CHECK_PRE = "TC";

    // / 库存转换配送单前缀

    public static final String TM_DELIVER_PRE = "TD";

    // / 库存转换上架作业单号前缀

    public static final String TM_INSTOCK_PRE = "TP";

    // / 拆箱作业单号前缀

    public static final String UNPACKING_PRE = "CX";

    // / 标签检查单号前缀

    public static final String CONTAINERCHECK_PRE = "LBC";

    // / 出货预约单号前缀

    public static final String OM_ORDERSHEET_PRE = "YO";

    // / 撤票单号前缀

    public static final String OM_CANCEL_PRE = "CE";

    // / 盘点单号前缀

    public static final String CH_PLAN_PRE = "CP";

    // / <summary>
    // / 盘点成需求单号前缀
    // / </summary>
    public static final String CH_REQUEST_PRE = "CQ";
    // / <summary>
    // / 盘点差异单号前缀
    // / </summary>
    public static final String CH_DIFF_PRE = "CY";

    // 盘点 初盘单号前缀
    public static final String CH_CHECK_PRE = "CH";

    // 复盘单号
    public static final String CH_CHECK_CR = "CR";

    // / 退厂通知单号前缀

    public static final String WM_RECEDE_PRE = "RE";

    public static final String WM_RECEDE_WP = "WP";
    
    //退厂申请
    public static final String WM_REQUEST_WR = "WR";

    // /退厂复核单号前缀
    public static final String WM_RECHECK_PRE = "RC";

    // 退厂拣货单前缀
    public static final String WM_OUTSTOCK_PRE = "RO";

    // / 创建发货通知单

    public static final String OM_EXPNO_PRE = "OE";

    // / 进货的收货单前缀

    public static final String IM_RECEDE_PRE = "IR";
    
    
    //分货的预分货单前缀
    public static final String OM_RECEDE_PRE = "PR";

    // / 退仓的收货单前缀

    public static final String UM_RECEDE_PRE = "UR";

    // 库存报损
    public static final String SM_WASTE_PRE = "SW";
    
    //直接出库
    public static final String SM_WASTE_DIRECT_PRE = "DS";

    // 其它入库
    public static final String SM_OTHERIN_PRE = "OI";

    // 库存调整
    public static final String SM_ADJ_PRE = "CA";

    // 收货差异
    public static final String Im_Dif_Record = "IY";

    // / <summary>
    // / 店退仓单号前缀
    // / </summary>
    // public static final String UM_UNTREAD_NO_PRE = "UM";
    // / <summary>
    // / 店退仓通知单号前缀
    // / </summary>
    // public static final String UM_S_UNTREAD_NO_PRE = "VM";
    // / <summary>
    // / 店退仓装箱单号前缀
    // / </summary>
    public static final String UM_LOADBOX_NO_PRE = "UB";
    // / <summary>
    // / 退仓验收单号前缀
    // / </summary>

    // public static final String UM_CHECK_NO_PRE = "UC";
    // / <summary>
    // / 退仓收货单号前缀
    // / </summary>
    public static final String UM_RECEIPT_NO_PRE = "UR";
    // / <summary>
    // / 退仓上架单号前缀
    // / </summary>
    public static final String UM_INSTOCK_NO_PRE = "UP";

    // 客户库存锁定
    public static final String CON_STORELOCK_PRE = "SL";

    //转货单号前缀
    public static final String CON_CONVERT_PRE = "CV";
    
 // 退仓验收任务单号前缀
    public static final String UM_CHECK_TASK_NO_PRE = "UT";
    
 // 库存转货单号前缀
    public static final String CON_CONVERT_GOODS_PRE = "CG";
    
    // 差异调整单号前缀
    public static final String OM_DIVIDE_DIFFERENT_PRE = "OF";
    
    //容器操作单据前缀
    public static final String CONTAINERTASK_PRE = "CN";
}
