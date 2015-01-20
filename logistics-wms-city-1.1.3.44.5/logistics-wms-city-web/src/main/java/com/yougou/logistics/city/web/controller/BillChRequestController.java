package com.yougou.logistics.city.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yougou.logistics.base.web.controller.BaseCrudController;
import com.yougou.logistics.city.common.model.BillChRequest;
import com.yougou.logistics.city.manager.BillChRequestManager;

@Controller
@RequestMapping("/bill_ch_request")
public class BillChRequestController extends BaseCrudController<BillChRequest> {
    @Resource
    private BillChRequestManager billChRequestManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("billchrequest/",billChRequestManager);
    }
    @RequestMapping(value = "/addRequest")
	@ResponseBody
    public Object addRequest(HttpServletRequest req,BillChRequest billChRequest){
    	Map<String, Object> map = new HashMap<String, Object>();
    	Object result = "";
    	map.put("result", result);
    	return map;
    }
}