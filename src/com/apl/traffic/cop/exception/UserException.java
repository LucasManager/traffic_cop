package com.apl.traffic.cop.exception;

import com.apl.traffic.cop.bean.common.BaseBean;
import com.apl.traffic.cop.form.QuartzFileForm;
import com.apl.traffic.cop.util.FileUtil;

public class UserException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String errorPath;
	private String inputPath;
	private String exceptionMessage;
	
	public UserException(QuartzFileForm form , String filename , String pExceptionMessage){
		inputPath = form.getInputPath() + filename ;
		errorPath = form.getErrorPath() + filename;
		if(!"".equals(filename)){
			handleErr(filename , pExceptionMessage,"0");
		}
	}
	
	public UserException(QuartzFileForm form , String filename , String pExceptionMessage,String notifyLevel){
		inputPath = form.getInputPath() + filename ;
		errorPath = form.getErrorPath() + filename;
		if(!"".equals(filename)){
			handleErr(filename , pExceptionMessage,notifyLevel);
		}
	}
	
	public String getExceptionMessage(){
		return exceptionMessage;
	}
	
	private void handleErr(String filename , String pExceptionMessage,String level){
		saveErrorlog(filename , pExceptionMessage,level);
		boolean fileChannelCopy = FileUtil.fileChannelCopy(inputPath  , errorPath);
	}
	
	public void saveErrorlog(String filename , String log,String notifyleve){
		try {
			String sql = "insert into T_TRAFFIC_CONVERT_ERROR_LOG(ID,CREATE_DATE,ERROR_DESC,ERROR_FILE_NAME,IS_NOTIFY,modules) values(SEQ_T_ERROR_LOG.nextval,SYSDATE,?,?,?,?)";
			String[] parm = new String[4];
			parm[0] = log;
			parm[1] = filename;
			parm[2] = notifyleve;
			parm[3] = "TRAFFIC";
			BaseBean.Update(sql, parm);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
