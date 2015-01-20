package com.yougou.logistics.city.web.utils;

import java.util.List;

import com.yougou.logistics.city.common.utils.CommonUtil;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class EncodeURLMethod implements TemplateMethodModel {


	@Override
	public Object exec(List args) throws TemplateModelException {
		boolean flag=false;
		
		if(args!=null&&args.size()>1){ 
			String s1=(String) args.get(0);
			String s2=(String) args.get(1);
			if((CommonUtil.hasValue(s1)&&CommonUtil.validateLong(s1))&&(CommonUtil.hasValue(s2)&&CommonUtil.validateLong(s2))){
				flag=CommonUtil.checkPower(Integer.valueOf(s1), Integer.valueOf(s2));
			}
		}
		
		return flag;
	}

}
