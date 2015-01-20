package com.yougou.logistics.city.common.enums;

/**
 * 定义Csharp交互接口类型对应枚举
 * 指定查询内的有关 System.Data.DataSet 的参数的类型。
 * @author wei.b
 *
 */
public enum InteractiveParameterDirectionEnum {
	INPUT(1),
	OUTPUT(2),
	INPUT_OUTPUT(3),
	RETURNVALUE(6);
	
	private int type;
	
	InteractiveParameterDirectionEnum(int t){
		this.type=t;
	}

	public int getType() {
		return type;
	}
	
	public static InteractiveParameterDirectionEnum getType(int type) {
		InteractiveParameterDirectionEnum e=null;
		for(InteractiveParameterDirectionEnum et:InteractiveParameterDirectionEnum.values()){
			if(et.type==type){
				e=et;
				break;
			}
		}
		return e;
	}
}
