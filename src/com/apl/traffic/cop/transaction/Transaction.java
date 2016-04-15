package com.apl.traffic.cop.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import com.apl.traffic.cop.exception.DataException;
import com.apl.traffic.cop.service.OracleConnectionService;
import com.apl.traffic.cop.util.Logger;

/**
 * 将DML操作封装为事务：
 *
 */
public class Transaction {

	private static Logger logger = Logger.getLogger(Transaction.class);
	
	/**
	 * 数据库连接
	 */
	private Connection conn;
	
	/**
	 * 批量更新 last use id
	 * 
	 * */
	public  int batchUpdate(Map<String, String> params,String sql) throws Exception{
		
		if (conn==null || conn.isClosed()) {
			logger.info("please open transaction first!");
			return -2;
		}
		if (params==null || params.size()==0) {
			return -1;
		}
		
		PreparedStatement pstmt = null;
		int i = 0;
		pstmt = conn.prepareStatement(sql);
		for (Map.Entry<String, String> entry : params.entrySet()) {
		  pstmt.setString(1, entry.getValue());
		  pstmt.setString(2, entry.getKey());
		  pstmt.addBatch();
		  i++;
		}
		int[] executeBatch = pstmt.executeBatch();
		return i;
		
	}
	
	/**
	 * 开始事务
	 * @throws Exception
	 */
	public void beginTransaction() throws Exception
	{
		conn = OracleConnectionService.GetConnection();
		conn.setAutoCommit(false);
	}
	
	/**
	 * 关闭事务
	 * @throws Exception
	 */
	public void endTransaction() throws Exception
	{
		if (conn!=null && !conn.isClosed()) {
			conn.commit();
			conn.setAutoCommit(true);
			conn.close();
			conn = null;
		}
	}
	
	/***
	 * 事务回滚
	 * @throws Exception
	 */
	public void rallbackTransaction() throws Exception
	{
		if (conn!=null && !conn.isClosed()) {
			conn.rollback();
			conn.close();
		}
	}
	
	
	public static void errHandle(Exception e){
		StackTraceElement[] name = e.getStackTrace();
		String message = "";
		for (int i = 0; i < name.length; i++) {
			message = message + "      " + name[i].toString() + "\n";
		}
		message = message + "Message: " + e.getMessage();
		logger.debug(message);
		System.exit(0);
	}
	
	
}
