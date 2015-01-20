package com.yougou.logistics.city.dal.jdbcconn;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yougou.logistics.base.common.exception.DaoException;
import com.yougou.logistics.city.common.enums.JdbcTypeMappingEnum;
import com.yougou.logistics.city.common.utils.CommonUtil;
import com.yougou.logistics.city.common.vo.Column;
/**
 * 查询Schema
 * @author wei.hj
 * @date 2013-07-15
 * @version 0.1.0
 * @copyright yougou.com
 *
 */

@Repository("querySchemaMapperl")
public class QuerySchemaMapperImpl implements QuerySchemaMapper {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	/**
	 * 查询Schema   注意：在使用过程中，参数名称必须使用大写的。
	 * @param catalog  目录名称，一般都为空
	 * @param schema   数据库用户名
	 * @param tableName  所查表名
	 * @param type  类型  表的类型(TABLE | VIEW)
	 * @throws DaoException
	 */
	@Override
	public List<Column> selectDataBaseMetaData(String catalog, String schema,
			String tableName, String[] type) throws DaoException {
		Connection conn=this.sqlSessionTemplate.getSqlSessionFactory().openSession().getConnection();
		List<Column> columnList=new ArrayList<Column>();
		ResultSet rs=null;
		try{
			if(CommonUtil.hasValue(catalog)){
				catalog=catalog.toUpperCase();
			}
			
			if(CommonUtil.hasValue(schema)){
				schema=schema.toUpperCase();
			}
			
			if(CommonUtil.hasValue(tableName)){
				tableName=tableName.toUpperCase();
			}
			
			DatabaseMetaData dataMetaData=conn.getMetaData();
			rs =dataMetaData.getColumns(catalog, schema, tableName, null);
			while(rs.next()){
				Column col=new Column(); 
				String colName = rs.getString("COLUMN_NAME");//列名
				String typeName = rs.getString("TYPE_NAME");//类型名称
				int precision = rs.getInt("COLUMN_SIZE");//精度
				int isNull = rs.getInt("NULLABLE");//是否为空  1--为空   0--不为空
				int dataType = rs.getInt("DATA_TYPE");//类型
				int scale = rs.getInt("DECIMAL_DIGITS");// 小数的位数 
				
				String remarks=rs.getString("REMARKS");
				
				colName=colName.toLowerCase();
				
			
				
				String javaType=JdbcTypeMappingEnum.findJavaType(dataType);
				
				
				
				col.setName(colName);
				col.setType(typeName);
				col.setValueField(colName+"#"+javaType);
				col.setTextField(colName);
				columnList.add(col);
			}
			return columnList;
		} catch (Exception e) {
			throw new DaoException(e);
		}finally{
			try {
				if(rs!=null)rs.close();
				if(conn!=null)conn.close();
			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}

	}

}
