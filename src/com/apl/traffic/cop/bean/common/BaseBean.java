/*!
 * Copyright (C) 2010-2011 Kazo Vision. (http://www.kazovision.com)
 * All rights reserved.
 *
 * BaseBean
 * @version 0.01
 * @author
 */

package com.apl.traffic.cop.bean.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.apl.traffic.cop.exception.DataException;
import com.apl.traffic.cop.service.OracleConnectionService;
import com.apl.traffic.cop.util.Logger;

public class BaseBean {
	private static String mLastInsertId = "";
	private static Logger logger = Logger.getLogger(BaseBean.class);
	
	/**
	 * 查询数据库
	 * @param pSqlString 查询sql
	 * @param pParameter 查询条件参数
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<ArrayList<String>> Query(String pSqlString, String[] pParameter)
	{	
		ArrayList<ArrayList<String>> dataset = new ArrayList<ArrayList<String>>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = OracleConnectionService.GetConnection();
			pstmt = conn.prepareStatement(pSqlString,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			if (pParameter != null) {
				for (int i=1; i<=pParameter.length; i++) {
					pstmt.setString(i, pParameter[i-1]);
				}
			}
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			if(columns != 0){
				while(rs.next()){
					ArrayList<String> row = new ArrayList<String>();
					for(int i=0; i<columns;i++){
						row.add(rs.getString(i+1));
					}
					dataset.add(row);
				}
			}
		} catch ( Exception e ){
			try{
				rs.close();
				pstmt.close();
				conn.close();	
			}catch(Exception es){
			}
			errHandle(e);
		}
		finally {
			try{
				rs.close();
				pstmt.close();
				conn.close();	
			}catch(Exception e){
			}
		}
		return dataset;
	}
	
	public static ArrayList<ArrayList<String>> Query(String pSqlString) throws Exception
	{
		ArrayList<ArrayList<String>> dataset = new ArrayList<ArrayList<String>>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = OracleConnectionService.GetConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(pSqlString);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			if(columns != 0){
				while(rs.next()){
					ArrayList<String> row = new ArrayList<String>();
					for(int i=0; i<columns;i++){
						row.add(rs.getString(i+1));
					}
					dataset.add(row);
				}
			}
		} catch ( Exception e ) {
			try{
				rs.close();
				stmt.close();
				conn.close();	
			}catch(Exception es){
			}
			errHandle(e);
		}
		finally {
			try{
				rs.close();
				stmt.close();
				conn.close();	
			}catch(Exception e){
			}
		}
		return dataset;
	}
	
	/**
	* 将获得的结果集进行分页，返回List
	* 
	* @param rs
	* @return
	* @throws SQLException
	 * @throws Exception 
	*/
	public static List<ArrayList<String>> paging(ArrayList<ArrayList<String>> list,int page,int rows){
		int fromIndex = (page-1)*rows;
		int toIndex = fromIndex + rows;
		if(toIndex > list.size()){
			toIndex = list.size();
		}
		return list.subList(fromIndex, toIndex);
	}
	/**
	 * 更新数据库
	 * @param pSqlString
	 * @param pParameter
	 * @param pParameterNum
	 * @return
	 * @throws Exception
	 */
	public static int Update(String pSqlString, String[] pParameter) throws Exception
	{
		int updatecount = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = OracleConnectionService.GetConnection();
			pstmt = conn.prepareStatement(pSqlString);
			if (pParameter != null) {
				for (int i=1; i<=pParameter.length; i++) {
					pstmt.setString(i, pParameter[i-1]);
				}
			}
			updatecount = pstmt.executeUpdate();
		} catch ( Exception e ) {
			try{
				pstmt.close();
				conn.close();	
			}catch(Exception es){
			}
			errHandle(e);
		}
		finally {
			try{
				pstmt.close();
				conn.close();
			}catch(Exception e){
			}
		}
		return updatecount;
	}
	
	/**
	 * 批量更新 last use id
	 * @throws DataException 
	 * */
	public static int batchUpdate(Map<String, String> params,String sql ) throws DataException{
		
		if (params==null || params.size()==0) {
			return -1;
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			conn = OracleConnectionService.GetConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			for (Map.Entry<String, String> entry : params.entrySet()) {
			  pstmt.setString(1, entry.getValue());
			  pstmt.setString(2, entry.getKey());
			  pstmt.addBatch();
			  i++;
			}
			int[] executeBatch = pstmt.executeBatch();
			conn.commit();
			conn.setAutoCommit(true); 
		} catch (Exception e) {
			try {
				logger.info("exec sq:"+sql+" ,fail,rollback!");
				conn.rollback();
				pstmt.close();
				conn.close();
			} catch (SQLException e1) {
			}
			errHandle(e);
		}finally {
			try{
				pstmt.close();
				conn.close();
			}catch(Exception e){
			}
		}
		return i;
		
	}
	
	/**
	 * 更新数据库
	 * @param pSqlString
	 * @param pParameter
	 * @param pParameterNum
	 * @return
	 * @throws Exception
	 */
//	public static int Update(String pSqlString, String[] pParameter,int count,int skip) throws Exception
//	{
//		int updatecount = 0;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		try {
////			conn = MySQLConnectionService.GetInstance().GetConnection();
//			pstmt = conn.prepareStatement(pSqlString);
//			if (pParameter != null) {
//				for(int j=0;j<count;j++){
//					for (int i=1; i<=skip; i++) {
//						pstmt.setString(i, pParameter[i+(j*skip)-1]);
//					}
//					if(j+1<count){
//						pstmt.addBatch();
//					}
//				}
//			}
//			pstmt.executeBatch();
//			updatecount = pstmt.executeUpdate();
//		} catch ( Exception e ) {
//			try{
//				pstmt.close();
//				conn.close();	
//			}catch(Exception es){
//			}
//			errHandle(e);
//		} finally {
//			try{
//				pstmt.close();
//				conn.close();	
//			}catch(Exception e){
//			}
//		}
//		return updatecount;
//	}
	
	/*!
	 * 获取最近一次insert的记录的ID编号。
	 */
	public static String GetLastInsertId(){
		return mLastInsertId;
	}
	
	public static final String ArrayToString(String[] pArray)
	{
		if (pArray == null) {
			return "";
		}
		
		String valuelist = "";
		for (String value : pArray) {
			if (valuelist != "") {
				valuelist += ",";
			}
			valuelist += value;
		}
		return valuelist;
	}
	
	public static final String ArrayToString(ArrayList<String> pArray)
	{
		if (pArray == null) {
			return "";
		}
		
		String valuelist = "";
		for (String value : pArray) {
			if (valuelist != "") {
				valuelist += ",";
			}
			valuelist += value;
		}
		return valuelist;
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
