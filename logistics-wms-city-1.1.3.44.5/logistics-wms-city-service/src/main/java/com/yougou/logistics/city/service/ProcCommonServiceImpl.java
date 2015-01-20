package com.yougou.logistics.city.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.dal.mapper.ProcCommonMapper;

@Service("procCommonService")
class ProcCommonServiceImpl extends BaseCrudServiceImpl implements
	ProcCommonService {
	@Log
	private Logger log;
	
    @Resource
    private ProcCommonMapper procCommonMapper;

    @Override
    public BaseCrudMapper init() {
	return procCommonMapper;
    }

    private final static String RESULTY = "Y";

    @Override
    public String procGetSheetNo(String locNo, String strpapertype)
	    throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strlocno", locNo);
	    map.put("strpapertype", strpapertype);
	    procCommonMapper.procGetSheetNo(map);
	    if (RESULTY.equals(map.get("stroutmsg"))) {
		return map.get("csheetno");
	    } else {
		throw new ServiceException("null");
	    }
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public Map<String, String> procGetContainerNoBase(String locno,
	    String strpapertype, String userId, String getType, String getNum,
	    String useType, String containerMetrial) throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strlocno", locno);
	    map.put("strpapertype", strpapertype);
	    map.put("strUserId", userId);
	    map.put("strGetType", getType);
	    map.put("nGetNum", getNum);
	    map.put("strUseType", useType);
	    map.put("strContainerMaterial", containerMetrial);
	    procCommonMapper.procGetContainerNoBase(map);
	    if ("Y|".equals(map.get("strOutMsg"))) {
		return map;
	    } else {
		throw new ServiceException(map.get("strOutMsg"));
	    }
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    public boolean procImInstockDirectByReceipt(String strLocno,
	    String strOwnerNo, String strReceiptNo, String strWorkerNo)
	    throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strlocno", strLocno);
	    map.put("strOwnerNo", strOwnerNo);
	    map.put("strReceiptNo", strReceiptNo);
	    map.put("strWorkerNo", strWorkerNo);
	    procCommonMapper.procImInstockDirectByReceipt(map);
	    if (RESULTY.equals(map.get("strResult").split("\\|")[0])) {
		return true;
	    } else {
		throw new DaoException(map.get("strResult").split("\\|")[1]);
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    @Override
    public Long procGetCellId() throws ServiceException {
	try {
	    Map<String, Object> map = new HashMap<String, Object>();
	    procCommonMapper.procGetCellId(map);
	    return Long.valueOf(map.get("nCellId").toString());
	} catch (Exception e) {
	    throw new ServiceException(e);
	}
    }

    @Override
    public boolean procImInstockDirectByCheck(String strLocno,
	    String strOwnerNo, String strCheckNo, String strWorkerNo)
	    throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strlocno", strLocno);
	    map.put("strOwnerNo", strOwnerNo);
	    map.put("strCheckNo", strCheckNo);
	    map.put("strWorkerNo", strWorkerNo);
	    procCommonMapper.procImInstockDirectByCheck(map);
	    if (RESULTY.equals(map.get("strResult").split("\\|")[0])) {
		return true;
	    } else {
		throw new DaoException(map.get("strResult").split("\\|")[1]);
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    @Override
    public boolean cancelDirectForAll(String locNo, String ownerNo,
	    String sourceNo, String flag) throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strlocno", locNo);
	    map.put("strowner", ownerNo);
	    map.put("strsourceno", sourceNo);
	    map.put("strflag", flag);
	    procCommonMapper.cancelDirectForAll(map);
	    if (RESULTY.equals(map.get("stroutmsg").split("\\|")[0])) {
		return true;
	    } else {
		throw new DaoException(map.get("stroutmsg").split("\\|")[1]);
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    @Override
    public boolean procAccApply(String iPaperNo, String iLocType,
	    String iPaperType, String iIoFLAG, Integer iPrepareDataExt)
	    throws ServiceException {
	try {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("I_PAPER_NO", iPaperNo);
	    map.put("I_LOC_TYPE", iLocType);
	    map.put("I_PAPER_TYPE", iPaperType);
	    map.put("I_IO_FLAG", iIoFLAG);
	    map.put("I_PREPARE_DATA_EXT", iPrepareDataExt);
	    procCommonMapper.procAccApply(map);
	    // if
	    // (RESULTY.equals(String.valueOf(map.get("strResult")).split("\\|")[0]))
	    // {
	    return true;
	    // } else {
	    // throw new
	    // DaoException(String.valueOf(map.get("strResult")).split("\\|")[1]);
	    // }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    @Override
    public boolean procUmDirect(String strLocno, String strOwnerNo,
	    String strUmNo, String strWorkerNo) throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strlocno", strLocno);
	    map.put("strOwnerNo", strOwnerNo);
	    map.put("strUntreadMMNo", strUmNo);
	    map.put("strWorkerNo", strWorkerNo);
	    procCommonMapper.procUmDirect(map);
	    if (RESULTY.equals(map.get("strResult").split("\\|")[0])) {
		return true;
	    } else {
		throw new DaoException(map.get("strResult").split("\\|")[1]);
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    @Override
    public boolean cancelDirectForUm(String locNo, String strOwnerNo,
	    String sourceNo, String keyStr, String strWorkerN)
	    throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strLocno", locNo);
	    map.put("strOwnerNo", strOwnerNo);
	    map.put("strUntreadMMNo", sourceNo);
	    map.put("strRowId", keyStr);
	    map.put("strWorkerNo", strWorkerN);
	    procCommonMapper.cancelDirectForUm(map);
	    if (RESULTY.equals(map.get("stroutmsg").split("\\|")[0])) {
		return true;
	    } else {
		throw new DaoException(map.get("stroutmsg").split("\\|")[1]);
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    public void auditUmCheck(String locno, String ownerNo, String checkNo,
	    String operUser) throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("i_locno", locno);
	    map.put("i_owner", ownerNo);
	    map.put("i_check_no", checkNo);
	    map.put("i_oper_user", operUser);
	    procCommonMapper.auditUmCheck(map);
	    if (map.get("stroutmsg") == null
		    || !RESULTY.equals(map.get("stroutmsg").split("\\|")[0])) {
		throw new DaoException(map.get("stroutmsg").split("\\|")[1]);
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    @Override
    public boolean createTask(String locNo, String strOwnerNo, String sourceNo,
	    String keyStr, String strWorkerN) throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strLocno", locNo);
	    map.put("strOwnerNo", strOwnerNo);
	    map.put("strUntreadMMNo", sourceNo);
	    map.put("strCheckNoList", keyStr);
	    map.put("strWorkerNo", strWorkerN);
	    procCommonMapper.createTask(map);
	    if (RESULTY.equals(map.get("stroutmsg").split("\\|")[0])) {
		return true;
	    } else {
		throw new DaoException(map.get("stroutmsg").split("\\|")[1]);
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    @Override
    public boolean sendOrder(String locNo, String strOwnerNo, String sennder,
	    String keyStr, String strWorkerNo) throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strLocno", locNo);
	    map.put("strOwnerNo", strOwnerNo);
	    map.put("strSender", sennder);
	    map.put("strRowIdList", keyStr);
	    map.put("strWorkerNo", strWorkerNo);
	    procCommonMapper.sendOrder(map);
	    if (RESULTY.equals(map.get("stroutmsg").split("\\|")[0])) {
		return true;
	    } else {
		throw new DaoException(map.get("stroutmsg").split("\\|")[1]);
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    @Override
    public boolean continueDirect(String locno, String ownerNo,
	    String untreadMmNo, String strCheckNoList, String strWorkerNo)
	    throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strLocno", locno);
	    map.put("strOwnerNo", ownerNo);
	    map.put("strUntreadMMNo", untreadMmNo);
	    map.put("strCheckNoList", strCheckNoList);
	    map.put("strWorkerNo", strWorkerNo);
	    procCommonMapper.continueDirect(map);
	    if (RESULTY.equals(map.get("stroutmsg").split("\\|")[0])) {
		return true;
	    } else {
		throw new DaoException(map.get("stroutmsg").split("\\|")[1]);
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    @Override
    public boolean bathUpload(String locno, String importNo)
	    throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("strLocno", locno);
	    map.put("strImportNo", importNo);
	    procCommonMapper.bathUpload(map);
	    if (RESULTY.equals(map.get("error_msg").split("\\|")[0])) {
		return true;
	    } else {
		throw new DaoException(map.get("error_msg").split("\\|")[1]);
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

    @Override
    public boolean UpdateContentStatus(String locno, String cellId,
	    String cellNo, String status, String flag, String hmManualFlag,
	    String editor) throws ServiceException {
	try {
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("locno", locno);
	    map.put("cellId", cellId);
	    map.put("cellNo", cellNo);
	    map.put("status", status);
	    map.put("flag", flag);
	    map.put("hmManualFlag", hmManualFlag);
	    map.put("editor", editor);
	    procCommonMapper.updateContentStatus(map);
	    if (RESULTY.equals(map.get("error_msg").split("\\|")[0])) {
		return true;
	    } else {
	    log.error(map.get("error_msg").split("\\|")[1]);
	    return false;
	    }
	} catch (DaoException e) {
	    throw new ServiceException(e.getMessage());
	}
    }

	@Override
	public boolean sendOrderForIm(String locNo, String strOwnerNo,
			String sennder, String keyStr, String strWorkerNo)
			throws ServiceException {
		try {
		    Map<String, String> map = new HashMap<String, String>();
		    map.put("strLocno", locNo);
		    map.put("strOwnerNo", strOwnerNo);
		    map.put("strSender", sennder);
		    map.put("strRowIdList", keyStr);
		    map.put("strWorkerNo", strWorkerNo);
		    procCommonMapper.sendOrderForIm(map);
		    if (RESULTY.equals(map.get("stroutmsg").split("\\|")[0])) {
			return true;
		    } else {
			throw new DaoException(map.get("stroutmsg").split("\\|")[1]);
		    }
		} catch (DaoException e) {
		    throw new ServiceException(e.getMessage());
		}
	}
	
	
    public void auditUmCheckTask(String locno, String checkTaskNo, String operUser,String openUserName) throws ServiceException {
    	try {
    	    Map<String, String> map = new HashMap<String, String>();
    	    map.put("i_locno", locno);
    	    map.put("I_checkTaskNo", checkTaskNo);
    	    map.put("i_oper_user_name", openUserName);
    	    map.put("i_oper_user", operUser);
    	    procCommonMapper.auditUmCheckTask(map);
    	    if (map.get("strOutMsg") == null|| !RESULTY.equals(map.get("strOutMsg").split("\\|")[0])) {
    	    	throw new DaoException(map.get("strOutMsg").split("\\|")[1]);
    	    }
    	} catch (DaoException e) {
    	    throw new ServiceException(e.getMessage());
    	}
        }

	@Override
	public String getSpecailCellNo(String strLocno, String strArea_usetype,
			String strArea_quality, String strArea_attribute,
			String strAttribute_type, String strItem_type)throws ServiceException {
		try {
    	    Map<String, String> map = new HashMap<String, String>();
    	    map.put("strLocno", strLocno);
    	    map.put("strArea_usetype", strArea_usetype);
    	    map.put("strArea_quality", strArea_quality);
    	    map.put("strArea_attribute", strArea_attribute);
    	    map.put("strAttribute_type", strAttribute_type);
    	    map.put("strItem_type", strItem_type);
    	    procCommonMapper.getSpecailCellNo(map);
    	    if (map.get("strOutMsg") != null && !RESULTY.equals(map.get("strOutMsg").split("\\|")[0])) {
    	    	throw new DaoException(map.get("strOutMsg").split("\\|")[1]);
    	    }else{
    	    	return map.get("strCellNo");
    	    }
    	} catch (DaoException e) {
    	    throw new ServiceException(e.getMessage());
    	}
		
	}

	@Override
	public void procContaskAudit(String locno, String userType, String conTaskNo, String creator)
			throws ServiceException {
		try {
    	    Map<String, String> map = new HashMap<String, String>();
    	    map.put("I_locno", locno);
    	    map.put("I_useType", userType);
    	    map.put("I_conTaskNo", conTaskNo);
    	    map.put("I_creator", creator);
    	    procCommonMapper.procContaskAudit(map);
    	    if (map.get("O_msg") != null && !RESULTY.equals(map.get("O_msg").split("\\|")[0])) {
    	    	throw new DaoException(map.get("O_msg").split("\\|")[1]);
    	    }
    	} catch (DaoException e) {
    	    throw new ServiceException(e.getMessage());
    	}
	}
	
}