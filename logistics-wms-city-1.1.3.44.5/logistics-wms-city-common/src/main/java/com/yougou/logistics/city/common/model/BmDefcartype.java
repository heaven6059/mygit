/*
 * 类名 com.yougou.logistics.city.common.model.BmDefcartype
 * @author luo.hl
 * @date  Wed Sep 25 09:27:35 CST 2013
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
package com.yougou.logistics.city.common.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.yougou.logistics.city.common.utils.JsonDateSerializer$19;

public class BmDefcartype {
	private String cartypeNo;

	private String cartypeName;

	private BigDecimal cartypeWeight;

	private BigDecimal cartypeLength;

	private BigDecimal cartypeWidth;

	private BigDecimal cartypeHeight;

	private Short maxLayer;

	private String creator;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date createtm;

	private String editor;
	@JsonSerialize(using = JsonDateSerializer$19.class)
	private Date edittm;

	public String getCartypeNo() {
		return cartypeNo;
	}

	public void setCartypeNo(String cartypeNo) {
		this.cartypeNo = cartypeNo;
	}

	public String getCartypeName() {
		return cartypeName;
	}

	public void setCartypeName(String cartypeName) {
		this.cartypeName = cartypeName;
	}

	public BigDecimal getCartypeWeight() {
		return cartypeWeight;
	}
	public String getCartypeWeightStr() {
		String s = "";
		try {
			NumberFormat nf = NumberFormat.getNumberInstance();
			s = nf.format(cartypeWeight);
		} catch (Exception e) {
		}
		return s;
	}
	public void setCartypeWeight(BigDecimal cartypeWeight) {
		this.cartypeWeight = cartypeWeight;
	}

	public BigDecimal getCartypeLength() {
		return cartypeLength;
	}
	public String getCartypeLengthStr() {
		String s = "";
		try {
			NumberFormat nf = NumberFormat.getNumberInstance();
			s = nf.format(cartypeLength);
		} catch (Exception e) {
		}
		return s;
	}
	public void setCartypeLength(BigDecimal cartypeLength) {
		this.cartypeLength = cartypeLength;
	}

	public BigDecimal getCartypeWidth() {
		return cartypeWidth;
	}
	public String getCartypeWidthStr() {
		String s = "";
		try {
			NumberFormat nf = NumberFormat.getNumberInstance();
			s = nf.format(cartypeWidth);
		} catch (Exception e) {
		}
		return s;
	}
	public void setCartypeWidth(BigDecimal cartypeWidth) {
		this.cartypeWidth = cartypeWidth;
	}

	public BigDecimal getCartypeHeight() {
		return cartypeHeight;
	}

	public void setCartypeHeight(BigDecimal cartypeHeight) {
		this.cartypeHeight = cartypeHeight;
	}

	public Short getMaxLayer() {
		return maxLayer;
	}

	public void setMaxLayer(Short maxLayer) {
		this.maxLayer = maxLayer;
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
}