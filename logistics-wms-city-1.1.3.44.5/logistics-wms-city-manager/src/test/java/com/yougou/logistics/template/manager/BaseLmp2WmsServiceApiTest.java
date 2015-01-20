package com.yougou.logistics.template.manager;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.city.common.api.BaseLmp2WmsServiceApi;
import com.yougou.logistics.city.common.api.dto.BaseLmp2WmsDto;

/**
 * TODO: 增加描述
 * 
 * @author su.yq
 * @date 2014-5-14 下午4:02:38
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BaseLmp2WmsServiceApiTest extends BaseManagerTest{
	
	public static void main(String[] args) throws RpcException {
		System.out.println("=================TEST START=================");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring-city-dubbo-test.xml"});
        context.start();
        BaseLmp2WmsServiceApi baseLmp2WmsServiceApi = (BaseLmp2WmsServiceApi)context.getBean("baseLmp2WmsServiceApi");
        List<BaseLmp2WmsDto> listWmsDtos = baseLmp2WmsServiceApi.getBaseCheck4Wms(null, null);
		for(BaseLmp2WmsDto dto:listWmsDtos){
			System.err.println(dto.getBillName());
		}
	}
}
