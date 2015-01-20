package com.yougou.logistics.template.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.city.common.api.BillLmp2WmsServiceApi;
import com.yougou.logistics.city.common.api.dto.BillLmp2WmsDto;
import com.yougou.logistics.city.common.api.enums.BillTypeEnum;

public class BillLmp2WmsServiceApiTest {

	public static void main(String[] args) {
		System.out.println("=================TEST START=================");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring-city-dubbo-test.xml"});
        context.start();
        BillLmp2WmsServiceApi billLmp2WmsServiceApi = (BillLmp2WmsServiceApi)context.getBean("billLmp2WmsServiceApi");
        try {
        	
        	List<String> billTypes = new ArrayList<String>();
        	billTypes.add(BillTypeEnum.TYPE_01.getValue());
        	billTypes.add(BillTypeEnum.TYPE_02.getValue());
        	billTypes.add(BillTypeEnum.TYPE_03.getValue());
        	String startDate = "2014-04-29";
        	String endDate = "2014-05-11";
        	String sysNo = "TM";
        	String locno = "006";
        	
        	List<BillLmp2WmsDto> list = billLmp2WmsServiceApi.getBill4Wms(billTypes, startDate, endDate, sysNo, locno, null);
        	System.out.println("size:"+list.size());
		} catch (RpcException e) {
			e.printStackTrace();
		}
	}
}
