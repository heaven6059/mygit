package com.yougou.logistics.city.common.enums;

/**
 * 定义Csharp交互接口类型对应枚举
 * 执行平台服务层命令类型
 * @author wei.b
 *
 */
public enum InteractiveCommandTypeEnum {
	/**
	 * SQL文本命令(默认)
	 */
	TEXT(1),
	/**
	 * 存储过程的名称
	 */
	STORED_PROCEDURE(4),
	/**
	 * 表的名称
	 */
	TABLE_DIRECT(512);

	private int type;

	InteractiveCommandTypeEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public static InteractiveCommandTypeEnum getType(int type){
		InteractiveCommandTypeEnum commandTypeEnum=null;
		for(InteractiveCommandTypeEnum e:InteractiveCommandTypeEnum.values()){
			if(e.getType()==type){
				commandTypeEnum=e;
				break;
			}
		}
		return commandTypeEnum;
	}

}
