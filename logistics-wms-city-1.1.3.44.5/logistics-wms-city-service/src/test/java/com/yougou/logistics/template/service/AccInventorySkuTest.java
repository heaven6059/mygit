package com.yougou.logistics.template.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before; 
import org.junit.Test; 
import org.springframework.beans.factory.annotation.Autowired;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.city.common.vo.AccInventorySkuBookVo;
import com.yougou.logistics.template.BaseServiceTest;

/**
 * 测试accInventorySkuBookService类
 * @author wugy
 */
public class AccInventorySkuTest extends BaseServiceTest {
//
//	@Autowired
//	//private AccInventorySkuBookService accInventorySkuBookService;
//	@Before
//	public void init() throws Exception {
//		System.out.println("test() begin...");
//	}
//	
//	@After
//	public void end() throws Exception {
//		System.out.println("test() end...");
//	}
//	
//	public int getRandom(int max){
//		return (int) (1 + Math.random() * max);
//
//	}
//	
//	/**
//	 * 验收单据库存记账
//	 */
//	@Test
//	public void testqueryAccCheckDtlVoList(){
//		Map<String,Object> params=new HashMap<String,Object>();
//		try {
//			//1
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "SC");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//2 s
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "UC");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//3
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "OD");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//4
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "DV");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//5
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "UP");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//6
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "UP");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//7
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "OC");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//8
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "OC");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//9
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "HS");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			
////			//--------------------------------begin-----------------------------------
////			//10
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "HS");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//11 s
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "HO");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//12
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "HO");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//13
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "SL");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//14
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "SR");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//15
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "IR");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//16
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "CV");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//17
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "CV");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//18 ---
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "CA");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//19
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "CA");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//20 s
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "OI");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//21
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "MO");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//22
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "MO");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//23
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "SI");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//24
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "SW");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "DS");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//25
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "CP");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//26
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "CP");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//27
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "HM");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//28
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "HM");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//29
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "RO");
////			params.put("ioFlag", "O");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
////			//30
////			params=new HashMap<String,Object>();
////			params.put("billNo", "006IC14052100002");//006IC14052100002(3171)
////			params.put("billType", "RO");
////			params.put("ioFlag", "I");
////			params.put("locType", '2');
////			accInventorySkuBookService.testqueryAccCheckDtlVoList(params);
//			//--------------------------------end-----------------------------------
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * sku库存记账
//	 * @throws Exception
//	 */
//	@Test
//	public void testSkuBookThread() throws Exception {
//		try{
//			for(int i=1;i<=3;i++){
//				new Thread(new SkuBookVoThread("线程"+i)).start();
//				//new Thread(new SkuBookThread("线程"+i)).start();
//			}
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * SkuBookVo线程
//	 * @author wugy
//	 *
//	 */
//	class SkuBookVoThread implements Runnable{
//		private String name;
//		public SkuBookVoThread(String name){
//			this.name=name;	
//		}	
//		public void run(){
//			System.out.println(name+"运行");
//			
//			try {
//				for(int i=0;i<1;i++){
//					AccInventorySkuBookVo skuBookVo=new AccInventorySkuBookVo();
//					skuBookVo.setLocno("006");
//					skuBookVo.setOwnerNo("BL");
//					skuBookVo.setItemNo("TMTBL6CI21DS1CM1");
//					skuBookVo.setBarcode("TBL6CI21DS1CM1230");
//					skuBookVo.setCellId((long)2374186); //储位Id
//					skuBookVo.setQuality("0");
//					skuBookVo.setItemType("0");
//					skuBookVo.setMoveQty(BigDecimal.valueOf(getRandom(10)));//发生数量取随机数
//					skuBookVo.setDirection(Long.valueOf(1));//记账方向(1：增加库存，-1：减少库存)
//					skuBookVo.setBillNo("006IC14052600006");
//					skuBookVo.setBillType("SC");
//					skuBookVo.setIoFlag(skuBookVo.getDirection()==1?"I":"0");//进出标识(I=入库 O=出库)
//					skuBookVo.setPreFlag("0");//财务类型(0=数量 1=预上 2=预下)
//					skuBookVo.setCellNo("0");
//					skuBookVo.setCreator("wutest");
//					accInventorySkuBookService.addSkuBookTran(skuBookVo);
//				}
//			} catch (ServiceException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	
//	/**
//	 * 批量sku库存记账
//	 * @throws Exception
//	 */
//	@Test
//	public void testSkuBookBatchAdd() throws Exception {
//		try{
//			int max=3000; //模拟最大数
//			List<AccInventorySkuBookVo> list=new ArrayList<AccInventorySkuBookVo>();
//			for(int i=0;i<max;i++){
//				AccInventorySkuBookVo skuBookVo=new AccInventorySkuBookVo();
//				skuBookVo.setLocno("006");
//				skuBookVo.setOwnerNo("BL");
//				skuBookVo.setItemNo("TMTBL6CI21DS1CM1");
//				skuBookVo.setBarcode("TBL6CI21DS1CM1230");
//				skuBookVo.setCellId((long)2343223); //储位Id
//				skuBookVo.setQuality("0");
//				skuBookVo.setItemType("0");
//				skuBookVo.setMoveQty(BigDecimal.valueOf(getRandom(10)));//发生数量取随机数
//				skuBookVo.setDirection(Long.valueOf(1));//记账方向(1：增加库存，-1：减少库存)
//				skuBookVo.setBillNo("006IC14052600006");
//				skuBookVo.setBillType("SC");
//				skuBookVo.setIoFlag(skuBookVo.getDirection()==1?"I":"0");//进出标识(I=入库 O=出库)
//				skuBookVo.setPreFlag("0");//财务类型(0=数量 1=预上 2=预下)
//				skuBookVo.setCellNo("0");
//				skuBookVo.setCreator("wutest");
//				list.add(skuBookVo);
//			}
//			//accInventorySkuBookService.batchAddSkuBook(list);
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//	}
//	
//	
//	@Test
//	public  void testListSplist() {
//		System.out.println("testListSplist");
//		// TODO Auto-generated method stub
//		List<Integer> list = new ArrayList<Integer>();
//		for (int i = 1; i < 55; i++)
//			// 55是一个动态变量 测试的时候先写死
//			list.add(i);
//		int count = list.size() / 10;
//		int yu = list.size() % 10;
//		for (int i = 0; i < 10; i++) {
//			List<Integer> subList = new ArrayList<Integer>();
//			if (i == 9) {
//				subList = list.subList(i * count, count * (i + 1) + yu);
//			} else {
//				subList = list.subList(i * count, count * (i + 1));
//			}
//
//			System.out.println(subList);
//		}
//	}

}
