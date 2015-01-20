package com.yougou.logistics.city.common.enums;

/**
 * 定义Csharp交互接口字段类型对应枚举
 * @author wei.b
 *
 */
public enum InteractiveDbTypeEnum {
	BINARY(1),
	BYTE(2),
	BOOLEAN(3),
	DATE(5),
	DATETIME(6),
	DOUBLE(8),
	INT16(10),
	INT32(11),
	INT64(12),
	STRING(16);
	
	private int type;
	
	InteractiveDbTypeEnum(int t){
		this.type=t;
	}

	public int getType() {
		return type;
	}
	
	public static InteractiveDbTypeEnum getType(String type) {
		InteractiveDbTypeEnum e=null;
		for (InteractiveDbTypeEnum te : InteractiveDbTypeEnum.values()) {
			if((te.getType()+"").equals(type)){
				e=te;
				break;
			}
		}
		return e;
	}
	
}
