/*!
 * Copyright (C) 2010-2011 Kazo Vision. (http://www.kazovision.com)
 * All rights reserved.
 *
 * MySQLConnectionService
 * ��ݿ����ӷ���
 * @version 0.04
 * @author tony (tonixinot@gmail.com)
 */

package com.apl.traffic.cop.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.apl.traffic.cop.bean.common.BaseBean;
import com.apl.traffic.cop.util.Logger;
import com.apl.traffic.cop.util.PropertiesUtil;

public class OracleConnectionService {
	
	private static Logger logger = Logger.getLogger(OracleConnectionService.class);
	
	private DataSource mDataSource = null;
	public Connection conn = null;  
	
	public static String url = "";  
    public static String driverName = "";  
    public static String username = "";  
    public static String password = "";
    
	private static OracleConnectionService mOracleConnectionService = null;
	
	static{
		 try {
			Properties properties = PropertiesUtil.getProperties("config/database_config.properties");
			url = properties.getProperty("db_url");
			driverName = properties.getProperty("db_driverName");
			username = properties.getProperty("db_username");
			password = properties.getProperty("db_password");
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			logger.error("get Driver exception: "+e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡ��ݿ�����
	 * @throws Exception
	 */
	public static Connection GetConnection() throws Exception
	{
		Connection	conn = DriverManager.getConnection(url, username, password);//��ȡ����  ;
		return conn;
	}
}