package com.yougou.logistics.template.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.city.common.model.Store;
import com.yougou.logistics.city.common.vo.AccInventoryConVo;
import com.yougou.logistics.city.service.AccInventoryConService;
import com.yougou.logistics.city.service.StoreService;
import com.yougou.logistics.template.BaseServiceTest;

/**
 * 测试accInventoryConService类
 * @author wugy
 */
public class AccInventoryConTest extends BaseServiceTest {
	
	@Resource
	AccInventoryConService accInventoryConService;
	
	@Resource
	StoreService storeService;
	

	/**
	 * 容器库存记账
	 */
	@Test
	public void testAccInventoryCon(){
		System.out.println("testAccInventoryCon");
		//accInventoryConService.accontingForCon();
		for(int i=0;i<1;i++){
			Thread t = new Thread(new ThreadTest());
			t.start();
		}
	}
	
	class ThreadTest implements Runnable{

		@Override
		public void run() {
			AccInventoryConVo temp = new AccInventoryConVo();
			temp.setCellNo("AUT0000297546");
			temp.setConNo("AUT0000297546");
			temp.setLocno("006");
			temp.setOwnerNo("BL");
			temp.setCreator("flt");
			temp.setItemType("0");
			temp.setBillNo("123");
			temp.setBillType("1");
			temp.setQuality("1");
			temp.setMoveChildrenQty(BigDecimal.valueOf(0));//取两位的随机数
			temp.setMoveSkuQty(BigDecimal.valueOf((int) (1 + Math.random() * 10)));//取两位的随机数
			temp.setSkuQty(BigDecimal.valueOf(0));
			temp.setChildrenQty(BigDecimal.valueOf(0));
			temp.setDirection(1);//记账方向(1：增加库存，-1：减少库存)
			temp.setCreator("wutest");
			try {
				accInventoryConService.accontingForCon(temp);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 批量容器库存记账
	 */
	@Test
	public void testBatchAccInventoryCon(){
		System.out.println("testBatchAccInventoryCon");
		//accInventoryConService.accontingForCon();
		int max=3000;
		List<AccInventoryConVo> list = new ArrayList<AccInventoryConVo>();
		AccInventoryConVo temp = new AccInventoryConVo();
		for(int i=0;i<max;i++){
			temp = new AccInventoryConVo();
			temp.setCellNo("AUT0000297546");
			temp.setConNo("AUT0000297546");
			temp.setLocno("006");
			temp.setOwnerNo("BL");
			temp.setCreator("flt");
			temp.setItemType("0");
			temp.setBillNo("123");
			temp.setBillType("1");
			temp.setQuality("1");
			temp.setMoveChildrenQty(BigDecimal.valueOf(0));//取两位的随机数
			temp.setMoveSkuQty(BigDecimal.valueOf((int) (1 + Math.random() * 10)));//取两位的随机数
			temp.setSkuQty(BigDecimal.valueOf(0));
			temp.setChildrenQty(BigDecimal.valueOf(0));
			temp.setDirection(1);//记账方向(1：增加库存，-1：减少库存)
			temp.setCreator("wutest");
			list.add(temp);
		}
		//accInventoryConService.batchAddAccInventory(list);
	}
	
	@Test
	public void testcangku(){
		System.out.println(".................");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("locno", "006");
		try {
			List<Store> list = storeService.selectWarehouseListByLocno(map);
			System.out.println(list.size());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
