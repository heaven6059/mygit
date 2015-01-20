package com.yougou.logistics.city.common.enums;

/**
 * 定义Csharp交互接口类型对应枚举
 * 执行平台服务层命令类型
 * @author wei.b
 *
 */
public enum InteractiveDataRowTypeEnum {
	/**
	 * 查询返回类型
	 */
	UNCHANGED(0),
	/**
	 * 添加
	 */
	ADDED(4),
	/**
	 * 删除
	 */
	DELETED(8),
	/**
	 * 更新
	 */
	MODIFIED(16);

	private int type;

	InteractiveDataRowTypeEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public static InteractiveDataRowTypeEnum getType(int type){
		InteractiveDataRowTypeEnum typeEnum=null;
		for(InteractiveDataRowTypeEnum e:InteractiveDataRowTypeEnum.values()){
			if(e.getType()==type){
				typeEnum=e;
				break;
			}
		}
		return typeEnum;
	}

}
