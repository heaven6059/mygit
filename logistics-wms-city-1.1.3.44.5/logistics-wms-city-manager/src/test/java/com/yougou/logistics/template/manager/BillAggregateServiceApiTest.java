package com.yougou.logistics.template.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.yougou.logistics.base.common.exception.RpcException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.city.common.api.BillAggregateServiceApi;
import com.yougou.logistics.city.common.api.dto.BillUnusualDto;

/**
 * TODO: 增加描述
 * 
 * @author ye.kl
 * @date 2014-1-23 下午9:43:41
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class BillAggregateServiceApiTest extends BaseManagerTest {
	@Resource
	private BillAggregateServiceApi billAggregateServiceApi;

	@Test
	public void test() {
		try {
			Date begin = new Date();
			Date end = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(begin);
			c.add(Calendar.DAY_OF_MONTH, -130);

			int count = billAggregateServiceApi.getBillUnusualCount("", "006", c.getTime(), end);
			SimplePage page = new SimplePage(1, 2, count);
			List<BillUnusualDto> list = billAggregateServiceApi.getBillUnusual("", "006", c.getTime(), end, page);
			for (BillUnusualDto dto : list) {
				System.out.println("LocNo:" + dto.getLocNo() + "	ZoneNo:" + dto.getZoneNo() + "	cQty:" + dto.getcQty()
						+ "	iQty:" + dto.getiQty() + "	oQty:" + dto.getoQty() + "	ReQty:" + dto.getReQty() + "	rQty:"
						+ dto.getrQty());
			}

		} catch (RpcException e) {
			e.printStackTrace();
		}
	}
}
