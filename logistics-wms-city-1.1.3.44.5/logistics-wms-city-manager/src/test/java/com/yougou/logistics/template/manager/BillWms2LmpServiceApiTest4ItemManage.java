package com.yougou.logistics.template.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.city.common.api.BillWms2LmpServiceApi;
import com.yougou.logistics.city.common.api.dto.ItemManageQueryDto;
import com.yougou.logistics.city.common.api.dto.ItemManageVo;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2013-12-11 下午3:53:56
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillWms2LmpServiceApiTest4ItemManage extends BaseManagerTest {

	public static void main(String[] args){
		System.out.println("=================TEST START=================");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring-city-dubbo-test.xml" });
		context.start();
		BillWms2LmpServiceApi billWms2LmpServiceApi = (BillWms2LmpServiceApi) context.getBean("billWms2LmpServiceApi");
		ItemManageQueryDto dto = new ItemManageQueryDto();
		dto.setBrandNo("JP020001");
		List<String> locnoList = new ArrayList<String>();
		locnoList.add("006");
		dto.setLocnoList(locnoList);
		dto.setProductNo("0JK0N282DL1DG3");
		//List<String> yearsList = new ArrayList<String>();
		//yearsList.add("TM007005");
		//dto.setYearsList(yearsList);
		ItemManageVo vo;
		try {
			System.out.println("=====================");
			vo = billWms2LmpServiceApi.findItemManage(dto);
			System.out.println(vo);
		} catch (RpcException e) {
			e.printStackTrace();
		}
	}
}
