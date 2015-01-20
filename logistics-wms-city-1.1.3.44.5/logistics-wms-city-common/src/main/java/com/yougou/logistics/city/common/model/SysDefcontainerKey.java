package com.yougou.logistics.city.common.model;

/**
 * 
 * 容器资料  主键
 * 
 * @author qin.dy
 * @date 2013-9-22 下午2:51:20
 * @copyright yougou.com
 */
public class SysDefcontainerKey {
	/**
	 * 仓别编码
	 */
    private String locno;

    /**
     * 容器类型  B-物流箱；C-原装箱；P-栈板；R-笼车
     */
    private String containerType;

    /**
     * 用途  0：客户物流箱；1：拣货物流箱
     */
    private String useType;

    public String getLocno() {
        return locno;
    }

    public void setLocno(String locno) {
        this.locno = locno;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }
}