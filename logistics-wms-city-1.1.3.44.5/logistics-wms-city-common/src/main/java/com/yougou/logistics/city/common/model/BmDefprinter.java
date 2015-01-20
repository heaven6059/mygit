package com.yougou.logistics.city.common.model;

import java.util.Date;

/**
 * 
 *打印机
 * 
 * @author qin.dy
 * @date 2013-11-1 下午2:30:10
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class BmDefprinter extends BmDefprinterKey {
	/**
	 * 打印机名称
	 */
    private String printerName;

    /**
     * 打印机类型  L-报表C-箱标签P-栈板标签
     */
    private String printerType;

    /**
     * 打印机驱动名
     */
    private String printerDriver;

    /**
     * 打印机端口
     */
    private String printerPort;

    /**
     * 打印状态  0-不可用1-可用
     */
    private Short status;

    private String creator;

    private Date createtm;

    private String editor;

    private Date edittm;

    private String moduleid;
    
    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrinterType() {
        return printerType;
    }

    public void setPrinterType(String printerType) {
        this.printerType = printerType;
    }

    public String getPrinterDriver() {
        return printerDriver;
    }

    public void setPrinterDriver(String printerDriver) {
        this.printerDriver = printerDriver;
    }

    public String getPrinterPort() {
        return printerPort;
    }

    public void setPrinterPort(String printerPort) {
        this.printerPort = printerPort;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatetm() {
        return createtm;
    }

    public void setCreatetm(Date createtm) {
        this.createtm = createtm;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getEdittm() {
        return edittm;
    }

    public void setEdittm(Date edittm) {
        this.edittm = edittm;
    }

	public String getModuleid() {
		return moduleid;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}
    }