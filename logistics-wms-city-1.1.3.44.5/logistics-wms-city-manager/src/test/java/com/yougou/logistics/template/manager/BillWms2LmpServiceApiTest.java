package com.yougou.logistics.template.manager;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.city.common.api.BillWms2LmpServiceApi;
import com.yougou.logistics.city.common.api.dto.Bill4WmsDto;

/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2013-12-11 下午3:53:56
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillWms2LmpServiceApiTest extends BaseManagerTest {

	public static void main(String[] args) throws RpcException {
		System.out.println("=================TEST START=================");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring-city-dubbo-test.xml" });
		context.start();
		BillWms2LmpServiceApi billWms2LmpServiceApi = (BillWms2LmpServiceApi) context.getBean("billWms2LmpServiceApi");
		List<Bill4WmsDto> list = billWms2LmpServiceApi.getBill4Wms("006", "TM", null, null, null, null);
		System.out.println(list.size());
	}
}
