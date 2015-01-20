package com.yougou.logistics.city.dal.database;

import java.util.Map;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;

public interface LookupMapper extends BaseCrudMapper {
	
	
	public int checkItemValue(Map<String,Object> map);
	
	
	public void deletelookup(String lookupcode);
	
	
	public void deletelookupdtl(String lookupcode);
	
	
	public int checkLookuoCode(String lookupcode);
}