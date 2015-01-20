package com.yougou.logistics.city.common.dto;

import com.yougou.logistics.city.common.model.BillSmOtherinDtl;

public class BillSmOtherinDtlDto extends BillSmOtherinDtl{
    
    private String itemName;
    
    private String barcode;
        
    private String colorName;
    
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}  

}
