package com.yougou.logistics.city.common.enums;

/**
 * 定义Csharp交互接口类型对应枚举
 * DataTable表里类型映射
 * @author wei.b
 *
 */
public enum InteractiveDataTypeEnum {
	BOOLEAN("boolean"),
	BYTE("byte"),
	CHAR("char"),
	STRING("String"),
	SHORT("short"),
	INT("int"),
	LONG("long"),
	DOUBLE("double"),
	DATE("Date");
	
	private String type;
	
	InteractiveDataTypeEnum(String t){
		this.type=t;
	}

	public String getType() {
		return type;
	}
	
	public static InteractiveDataTypeEnum getType(String type){
		InteractiveDataTypeEnum e=null;
		for(InteractiveDataTypeEnum et:InteractiveDataTypeEnum.values()){
			if(et.type.equals(type)){
				e=et;
				break;
			}
		}
		return e;
	}
	
	public Object instenceOfOject(String type,Object o){
		
		return null;
	}
}
