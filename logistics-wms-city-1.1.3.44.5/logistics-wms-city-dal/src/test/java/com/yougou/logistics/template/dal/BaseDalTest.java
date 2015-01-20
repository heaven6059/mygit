package com.yougou.logistics.template.dal;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 所有core中的Service层测试请继承这个类
 * @author lifeng.li
 * @date 2013-4-9 下午6:38:02
 * @version 0.1.0 
 * @copyright yougou.com 
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "/spring-template-dal-test.xml")
public abstract class BaseDalTest {
	
}
