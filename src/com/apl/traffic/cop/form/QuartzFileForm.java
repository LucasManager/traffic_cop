/**
 * 读取保存配置文件
 */
package com.apl.traffic.cop.form;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.apl.traffic.cop.bean.BKGRangeBean;
import com.apl.traffic.cop.bean.common.BaseBean;
import com.apl.traffic.cop.exception.DataException;
import com.apl.traffic.cop.scan.ScanFolder;
import com.apl.traffic.cop.util.PropertiesUtil;
import com.apl.traffic.cop.util.TrafficCopUtils;

public class QuartzFileForm {
	private Properties folderProp;
	private Properties regexProp;
	private String inputPath;
	private String backupPath;
	private String errorPath;
	private String wbpPath;
	// source files path
	private String sourcePath;
	
	private String q2cPath;
	//q2c文件夹中区分 Booking 和SI文件夹：
	private String q2cBookingPath;
	private String q2cSIPath;
	private String iftmIndex;
	private String bookingMark;
	private String siMark;
	
	private String headAndFoot;
	private String bkgLocation;
	private String odLocation;
	private int bknIndex;
	private String bknRegex;
	private String headRegex;
	private String footRegex;
	private int maxid;
	
	//存放判断福州的关键字和 关键字所在文件名称的位置：
	private Map<String, String> fuzhou = new HashMap<String, String>();
	//存放判断厦门的关键字和 关键字所在文件名称的位置：
	private Map<String, String> xiamen = new HashMap<String, String>();
	//关键字所在文件名的位置：
	private int kwposition = 0;

	//wave1范围的最小值
	private String wave1min;
	//wave1范围的最大值
	private String wave1max;
	
	private String testKey = "";
	
	
	/**
	 * put ID　and  the last use id  
	 */
	private Map<String, String> wave1UpdateMap = new HashMap<String, String>();
	/**
	 * put ID and  the last use id 
	 */
	private Map<String, String> nowave1UpdateMap = new HashMap<String, String>();
	
	/**
	 * put all blnumber of current file;
	 */
	private List<String> blNumbers = new ArrayList<String>();
	

	public List<String> getBlNumbers() {
		return blNumbers;
	}

	public void setBlNumbers(List<String> blNumbers) {
		this.blNumbers = blNumbers;
	}

	public QuartzFileForm() {
		folderProp = PropertiesUtil.getProperties("config/folder_config.properties");
		regexProp = PropertiesUtil.getProperties("config/regex_config.properties");
		System.out.println("配置文件读取成功！");
		inputPath = folderProp.getProperty("input.path");
		backupPath = folderProp.getProperty("backup.path");
		errorPath = folderProp.getProperty("error.path");
		wbpPath = folderProp.getProperty("ouput_wbp.path");
//		q2cPath = folderProp.getProperty("ouput_csss.path");
		q2cBookingPath = folderProp.getProperty("ouput_csss.Booking");
		q2cSIPath = folderProp.getProperty("ouput_csss.SI");
		
		sourcePath = folderProp.getProperty("sourceFile.path");
		
		iftmIndex = regexProp.getProperty("iftm");
		bookingMark = regexProp.getProperty("isBooking");
		siMark = regexProp.getProperty("isSI");
		
		headAndFoot = regexProp.getProperty("headAndFoot");
		bkgLocation = regexProp.getProperty("bkgLocation");
		odLocation = regexProp.getProperty("ODLocation");
		kwposition = Integer.valueOf(regexProp.getProperty("kwposition"));
		String str = regexProp.getProperty("fuzhou");
		fuzhou.put("keyword", str);
		str = regexProp.getProperty("xiamen");
		xiamen.put("keyword",str);
		testKey = regexProp.getProperty("testkey");
		bknIndex = Integer.valueOf(bkgLocation.split("-")[1]) - 2;
		bknRegex = bkgLocation.split("-")[0];
		headRegex = "'" + headAndFoot.split("-")[0] + ":";
		footRegex = "'" + headAndFoot.split("-")[1] + ":";
		wave1min = regexProp.getProperty("wave1min");
		wave1max = regexProp.getProperty("wave1max");
	}
	
	public Map<String, String> getWave1UpdateMap() {
		return wave1UpdateMap;
	}

	public void setWave1UpdateMap(Map<String, String> wave1UpdateMap) {
		this.wave1UpdateMap = wave1UpdateMap;
	}

	public Map<String, String> getNowave1UpdateMap() {
		return nowave1UpdateMap;
	}

	public void setNowave1UpdateMap(Map<String, String> nowave1UpdateMap) {
		this.nowave1UpdateMap = nowave1UpdateMap;
	}

	public Properties getFolderProp() {
		return folderProp;
	}

	public void setFolderProp(Properties folderProp) {
		this.folderProp = folderProp;
	}

	public Properties getRegexProp() {
		return regexProp;
	}

	public void setRegexProp(Properties regexProp) {
		this.regexProp = regexProp;
	}

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getBackupPath() {
		return backupPath;
	}

	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}

	public String getWbpPath() {
		return wbpPath;
	}

	public void setWbpPath(String wbpPath) {
		this.wbpPath = wbpPath;
	}

	public String getQ2cPath() {
		return q2cPath;
	}

	public void setQ2cPath(String q2cPath) {
		this.q2cPath = q2cPath;
	}

	public String getHeadAndFoot() {
		return headAndFoot;
	}

	public void setHeadAndFoot(String headAndFoot) {
		this.headAndFoot = headAndFoot;
	}

	public String getBkgLocation() {
		return bkgLocation;
	}

	public void setBkgLocation(String bkgLocation) {
		this.bkgLocation = bkgLocation;
	}

	public String getOdLocation() {
		return odLocation;
	}

	public void setOdLocation(String odLocation) {
		this.odLocation = odLocation;
	}

	public int getBknIndex() {
		return bknIndex;
	}

	public void setBknIndex(int bknIndex) {
		this.bknIndex = bknIndex;
	}

	public String getBknRegex() {
		return bknRegex;
	}

	public void setBknRegex(String bknRegex) {
		this.bknRegex = bknRegex;
	}

	public String getHeadRegex() {
		return headRegex;
	}

	public void setHeadRegex(String headRegex) {
		this.headRegex = headRegex;
	}

	public String getFootRegex() {
		return footRegex;
	}

	public void setFootRegex(String footRegex) {
		this.footRegex = footRegex;
	}

	public String getErrorPath() {
		return errorPath;
	}

	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}

	
	/**
	 *  create blNo , get location range,BP range ,origin range; if the range of origin not null,then use origin range,else use default_bp range; 
	 *	if default_bp is null,then use default_location range if it is not null; otherwise throw exception 
	 * @param origin
	 * @param bpcode
	 * @param iswave1
	 * @return digit BlNo
	 * @throws DataException 
	 */
	public String createBKG(String origin,String bpcode,String iswave1,String refvel) throws DataException
	{

		String sql = "";
		String[] param = new String[]{origin,bpcode,bpcode,bpcode};
		if ("wave1".equals(iswave1)) {
			sql = "select 'origin' as identify,wave1_min as blnomin,wave1_max as blnomax,wave1_last_used_id as currentuseid,id as id from T_TRAFFIC_BOOKING_RANGE t where t.CODE=? and t.bk_level='5' and (t.isdelete is null or t.isdelete<>'1') and t.PARENT_ID in(select id from T_TRAFFIC_BOOKING_RANGE f where f.CODE = ? and f.bk_level='4')"
					+"union select 'default_bp' as identify,wave1_min as blnomin,wave1_max as blnomax,wave1_last_used_id as currentuseid,id as id from T_TRAFFIC_BOOKING_RANGE t where t.CODE='default' and (t.isdelete is null or t.isdelete<>'1') and t.PARENT_ID in(select id from T_TRAFFIC_BOOKING_RANGE f where f.CODE = ? and f.bk_level='4')"
					+"union select 'default_location' as identify,wave1_min as blnomin,wave1_max as blnomax,wave1_last_used_id as currentuseid,id as id from T_TRAFFIC_BOOKING_RANGE t where t.CODE='default' and t.bk_level='4' and (t.isdelete is null or t.isdelete<>'1') and t.PARENT_ID in (select id from T_TRAFFIC_BOOKING_RANGE r where r.CODE = (select p.APL_OFFICE as location from webedi.T_BOOKING_PARTY p where p.BOOKING_PARTY_CODE = ?))";
		}else
		{
			sql = "select 'origin' as identify,no_wave1_min as blnomin,no_wave1_max as blnomax,no_wave1_last_used_id as currentuseid,id as id from T_TRAFFIC_BOOKING_RANGE t where t.CODE=? and t.bk_level='5' and (t.isdelete is null or t.isdelete<>'1') and t.PARENT_ID in(select id from T_TRAFFIC_BOOKING_RANGE f where f.CODE = ? and f.bk_level='4')"
					+"union select 'default_bp' as identify,no_wave1_min as blnomin,no_wave1_max as blnomax,no_wave1_last_used_id as currentuseid,id as id from T_TRAFFIC_BOOKING_RANGE t where t.CODE='default' and (t.isdelete is null or t.isdelete<>'1') and t.PARENT_ID in(select id from T_TRAFFIC_BOOKING_RANGE f where f.CODE = ? and f.bk_level='4')"
					+"union select 'default_location' as identify,no_wave1_min as blnomin,no_wave1_max as blnomax,no_wave1_last_used_id as currentuseid,id as id from T_TRAFFIC_BOOKING_RANGE t where t.CODE='default' and t.bk_level='4' and (t.isdelete is null or t.isdelete<>'1') and t.PARENT_ID in (select id from T_TRAFFIC_BOOKING_RANGE r where r.CODE = (select p.APL_OFFICE as location from webedi.T_BOOKING_PARTY p where p.BOOKING_PARTY_CODE = ?))";
		}
		ArrayList<ArrayList<String>> result = BaseBean.Query(sql, param);
		//create blno by result
		String blno = createBlNo(result,iswave1,bpcode,refvel);
		return blno;
	}
	
	//create blno
	private String createBlNo(ArrayList<ArrayList<String>> result,String iswave1,String bpcode,String refvel) throws DataException
	{
		if (result==null || result.size()==0) {
			throw new DataException(bpcode+"#@"+refvel+"#@#@"+"no such origin and BP find in table !","1");
		}
		Map<String, BKGRangeBean> map = new HashMap<String, BKGRangeBean>();
		for (ArrayList<String> arrayList : result) 
		{
			BKGRangeBean bean = new BKGRangeBean();
			bean.setIdentify(arrayList.get(0));
			bean.setBlnoMin(arrayList.get(1));
			bean.setBlnoMax(arrayList.get(2));
			bean.setCurrentUseId(arrayList.get(3));
			bean.setId(arrayList.get(4));
			map.put(bean.getIdentify(), bean);
		}
		//set priority
		if (map.get("origin")!=null) {
			
			return blBybean(map.get("origin"),iswave1,bpcode,refvel);
		}
		if (map.get("default_bp")!=null) {
			return blBybean(map.get("default_bp"),iswave1,bpcode,refvel);
		}
		if (map.get("default_location")!=null) {
			return blBybean(map.get("default_location"),iswave1,bpcode,refvel);
		}
		throw new DataException(bpcode+"#@"+refvel+"#@#@"+"can't find any range by origin or BP","1");
	}
	
	// one file only update lastUseId once times when file deal done
	private String blBybean(BKGRangeBean bean,String iswave1,String bpcode,String refvel) throws DataException
	{
		long maxId = Long.valueOf(bean.getBlnoMax());
		long minId = Long.valueOf(bean.getBlnoMin());
		long currentuseId = Long.valueOf(bean.getCurrentUseId());
		
		if("wave1".equals(iswave1) && wave1UpdateMap.get(bean.getId())!=null)
		{
			currentuseId = Long.valueOf(wave1UpdateMap.get(bean.getId()));
		}else if("nonwave1".equals(iswave1) && nowave1UpdateMap.get(bean.getId())!=null)
		{
			currentuseId = Long.valueOf(nowave1UpdateMap.get(bean.getId()));
		}
		long createId = currentuseId+1;
		
		if (createId>maxId) {
			throw new DataException(bpcode+"#@"+refvel+"#@#@"+" the "+bean.getIdentify()+"  is out of range,maxID:"+bean.getBlnoMax()+" minID:"+bean.getBlnoMin()+" currentuseId:"+bean.getCurrentUseId()+" need the ID:"+createId,"1");
		}
		String blno = TrafficCopUtils.blFormate.format(createId);
		if("wave1".equals(iswave1))
		{
			wave1UpdateMap.put(bean.getId(), String.valueOf(createId));
		}else
		{
			nowave1UpdateMap.put(bean.getId(), String.valueOf(createId));
		}
		return blno;
	}

	public Map<String, String> getFuzhou() {
		return fuzhou;
	}

	public void setFuzhou(Map<String, String> fuzhou) {
		this.fuzhou = fuzhou;
	}

	public Map<String, String> getXiamen() {
		return xiamen;
	}

	public void setXiamen(Map<String, String> xiamen) {
		this.xiamen = xiamen;
	}
	
	public String getWave1min() {
		return wave1min;
	}

	public void setWave1min(String wave1min) {
		this.wave1min = wave1min;
	}

	public String getWave1max() {
		return wave1max;
	}

	public void setWave1max(String wave1max) {
		this.wave1max = wave1max;
	}

	public int getKwposition() {
		return kwposition;
	}

	public void setKwposition(int kwposition) {
		this.kwposition = kwposition;
	}

	public String getTestKey() {
		return testKey;
	}

	public void setTestKey(String testKey) {
		this.testKey = testKey;
	}

	public void setQ2cBookingPath(String q2cBookingPath) {
		this.q2cBookingPath = q2cBookingPath;
	}

	public String getQ2cBookingPath() {
		return q2cBookingPath;
	}

	public void setQ2cSIPath(String q2cSIPath) {
		this.q2cSIPath = q2cSIPath;
	}

	public String getQ2cSIPath() {
		return q2cSIPath;
	}

	public void setIftmIndex(String iftmIndex) {
		this.iftmIndex = iftmIndex;
	}

	public String getIftmIndex() {
		return iftmIndex;
	}

	public void setBookingMark(String bookingMark) {
		this.bookingMark = bookingMark;
	}

	public String getBookingMark() {
		return bookingMark;
	}

	public void setSiMark(String siMark) {
		this.siMark = siMark;
	}

	public String getSiMark() {
		return siMark;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getSourcePath() {
		return sourcePath;
	}
}
