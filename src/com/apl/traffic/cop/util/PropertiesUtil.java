package com.apl.traffic.cop.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.apl.traffic.cop.bean.common.BaseBean;

public class PropertiesUtil {
	/**
	 * 
	 * @return prop 
	 */
	public static Properties getProperties(String name) {
		Properties prop = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(name);
			prop.load(in);
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return prop;
	}
	
//	public static void uploadProperties(String min, String max){
//		String sql = "insert into WAVE1_BLNUMBER(id,minid,maxid)VALUES(SEQ_WAVE1ID.nextval,?,?)";
//		String[] parm = new String[2];
//		parm[0] = min;
//		parm[1] = max; 
//		try {
//			BaseBean.Update(sql, parm);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	public static ArrayList<ArrayList<String>> downloadProperties(){
//		String sql = "select MINID,MAXID from WAVE1_BLNUMBER";
//		ArrayList<ArrayList<String>> list = null;
//		try {
//			list = BaseBean.Query(sql);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return list;
//	}
	
}
