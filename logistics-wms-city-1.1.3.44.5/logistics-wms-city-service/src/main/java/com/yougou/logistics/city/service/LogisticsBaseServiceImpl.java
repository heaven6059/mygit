package com.yougou.logistics.city.service;

import java.sql.PreparedStatement;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.exception.SqlSessionServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import com.yougou.logistics.base.service.LogisticsSqlSessionService;
import com.yougou.logistics.base.service.log.Log;
import com.yougou.logistics.city.common.model.LogisticsBase;
import com.yougou.logistics.city.dal.database.LogisticsBaseMapper;

/**
 * test
 * 
 * @author lianghuhe
 * 
 */
@Service("logisticsBaseService")
public class LogisticsBaseServiceImpl extends BaseCrudServiceImpl implements LogisticsBaseService {

	@Log
	private Logger log;

	@Autowired
	private LogisticsBaseMapper logisticsBaseMapper;

	private LogisticsSqlSessionService logisticsSqlSessionService;

	@Override
	public BaseCrudMapper init() {
		return logisticsBaseMapper;
	}

	/**
	 * 
	 * @param key
	 * @throws Exception
	 */
	@Override
	public void addLogisticsBase(String key, LogisticsBase logisticsBase) throws ServiceException {
		try {
		/*	SqlSession sqlSession = logisticsSqlSessionService.getNewSqlSession(key);
			sqlSession.insert("com.yougou.logistics.temp.dal.mapper.LogisticsBaseMapper.insert", logisticsBase);
			log.info("插入记录成功," + printSqlSessionInfo(sqlSession));
			System.out.println("插入记录成功," + printSqlSessionInfo(sqlSession));*/
			logisticsBaseMapper.insert(logisticsBase);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}

	}

	/**
	 * Adds the logistic base by connection.
	 * 
	 * @param key
	 *            the key
	 * @param logisticsBase
	 *            the logistics base
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public void addLogisticBaseByConnection(String key, LogisticsBase logisticsBase) throws ServiceException {
		try {
			String sql = "insert into LOGISTICS_BASE (BASE_ID, WAREHORSE_NAME) values (?,?)";
			SqlSession sqlSession = logisticsSqlSessionService.getNewSqlSession(key);
			PreparedStatement preparedStatement = sqlSession.getConnection().prepareStatement(sql);
			preparedStatement.setInt(1, logisticsBase.getBaseId().intValue());
			preparedStatement.setString(2, logisticsBase.getWarehorseName());
			preparedStatement.execute();
			log.info("插入记录成功," + printSqlSessionInfo(sqlSession));
			System.out.println("插入记录成功," + printSqlSessionInfo(sqlSession));
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param key
	 */
	@Override
	public void findLogisticsBase(String key) throws SqlSessionServiceException {
		try {
			final SqlSession sqlSession = logisticsSqlSessionService.getSqlSession(key);

			sqlSession.select("com.yougou.logistics.temp.dal.mapper.LogisticsBaseMapper.selectCount", new ResultHandler() {
				@Override
				public void handleResult(ResultContext context) {
					Integer result = (Integer) context.getResultObject();
					log.info("当前记录数:" + result + "," + printSqlSessionInfo(sqlSession));
					System.out.println("当前记录数:" + result + "," + printSqlSessionInfo(sqlSession));
				}
			});
		} catch (SqlSessionServiceException e) {
			throw e;
		}
	}

	@Override
	public int findCount() throws ServiceException {
		try {
			return logisticsBaseMapper.selectCount(new HashMap<String, Object>(),null);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	private String printSqlSessionInfo(SqlSession sqlSession) {
		StringBuilder log = new StringBuilder();
		log.append("[sqlSession]:" + sqlSession.toString() + ",");
		log.append("[connection]:" + sqlSession.getConnection().toString());
		return log.toString();
	}
}
