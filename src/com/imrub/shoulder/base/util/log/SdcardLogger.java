package com.imrub.shoulder.base.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.imrub.shoulder.base.app.path.EnvirPath;
import com.imrub.shoulder.base.app.path.EnvirUtils;
import com.imrub.shoulder.base.util.ILogger;

public class SdcardLogger implements ILogger{

	public static final String LogPosfix = ".txt";
	
	public SdcardLogger(){
	}
	
	@Override
	public void print(String tag, String log) {
		if(!EnvirUtils.ExistSDCard()){
			return ;
		}
		
		File logFile = createLoggerFile();
		if(logFile == null){
			return ;
		}
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(logFile, true));
			loggerToFile(writer, log);
		} catch (Exception e) {
		} finally{
			closeFile(writer);
		}
	}

	private void loggerToFile(BufferedWriter fileWriter, String log) throws Exception{
		fileWriter.append(LoggerUtils.getCurrentTime()).append(" ");
		fileWriter.append(log);
		fileWriter.write("\r\n\r\n");
		fileWriter.flush();
	}
	
	private void closeFile(BufferedWriter file){
		if(file != null){
			try {
				file.close();
			} catch (IOException e) {
			}
		}
	}
	
	private File createLoggerFile(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date(System.currentTimeMillis()));
		String logName = date + LogPosfix;
				
		String logDir = EnvirPath.getAppLogDir();
		File logFile = new File(logDir, logName);
		
		if(!logFile.exists()){
			try {
				boolean isSuccess = logFile.createNewFile();
				if(!isSuccess){
					return null;
				}
			} catch (IOException e) {
			}
		}
		return logFile;
	}
	
}
