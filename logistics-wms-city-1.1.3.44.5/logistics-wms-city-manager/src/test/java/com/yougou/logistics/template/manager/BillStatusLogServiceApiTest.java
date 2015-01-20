package com.yougou.logistics.template.manager;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.city.common.api.BillStatusLogServiceApi;


/**
 * TODO: 增加描述
 * 
 * @author jiang.ys
 * @date 2013-12-11 下午3:53:56
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillStatusLogServiceApiTest extends BaseManagerTest{
	
	public static void main(String[] args) throws RpcException {
		System.out.println("=================TEST START=================");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring-city-dubbo-test.xml"});
        context.start();
        BillStatusLogServiceApi billStatusLogServiceApi = (BillStatusLogServiceApi)context.getBean("billStatusLogServiceApi");
        billStatusLogServiceApi.getBillStatusLogByPoNo(null);
        System.out.println(billStatusLogServiceApi);
	}
}
