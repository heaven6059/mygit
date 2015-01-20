package com.yougou.logistics.template.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yougou.logistics.city.service.AccTaskService;
import com.yougou.logistics.template.BaseServiceTest;

/**
 * 测试accInventorySkuBookService类
 * @author wugy
 */
public class AccTaskTest extends BaseServiceTest {

	@Autowired
	private AccTaskService accTaskService;
	@Before
	public void init() throws Exception {
		System.out.println("test() begin...");
	}
	
	@After
	public void end() throws Exception {
		System.out.println("test() end...");
	}
	
	public int getRandom(int max){
		return (int) (1 + Math.random() * max);

	}
	
	/**
	 * 验收单据库存记账
	 */
	@Test
	public void testexecuteAcctask(){
		try {
			accTaskService.executeAcctask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
