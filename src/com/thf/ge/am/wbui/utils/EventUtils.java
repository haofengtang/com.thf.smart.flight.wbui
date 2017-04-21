package com.thf.ge.am.wbui.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventUtils {
	
	final static public String DataChanged_BuildStatus = "buildstatus_data_change";
	final static public String DataChanged_LayerSelect = "layerstatus_selection_change";
	final static public String DataChanged_LayerDetailData = "layerdetail_data_change";
	final static public String DataChanged_LastLayerAnomaly = "buildstatus_data_lastlayer_anomaly";
	final static public String DataChanged_Plot = "image_data_selection_change";
	final static public String DataChanged_AlertLevel = "image_data_alertlevel_change";
	final static public String ButtonClicked_Start = "start_button_clicked";
	final static public String ButtonClicked_Stop = "stop_button_clicked";
	final static public String ProcessMessage_Appended = "process_message_appended";
	
	final static public int AlertLevel_OK = 0;
	final static public int AlertLevel_Warning = 1;
	final static public int AlertLevel_Error = 2;

//	final static public Properties system = new Properties();
	
	private SimpleDateFormat _format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static private EventUtils _self;
	
	public EventUtils() {
//		system.put("python_comm", "cmd /c dir");
//		system.put("source_path", "C:/mpm/source");
//		system.put("target_path", "C:/mpm/target");
//		system.put("python_comm", "ls");
//		system.put("source_path", "/");
//		system.put("target_path", "");
//		Location location = Platform.getInstallLocation();
//		String rootPath = location.getURL().toString();
//		System.out.println("System installation root - "+rootPath);
//		File propertyFile = new File(rootPath+"/mpm.conf");
//		if(propertyFile!=null&&propertyFile.exists()){
//			try{
//				InputStream propis = new FileInputStream(propertyFile);
//				system.load(propis);
//			}catch(Exception ex){ex.printStackTrace();}
//		}
	}

	static public EventUtils getInstance(){
		if(_self==null) _self=new EventUtils();
		return _self;
	}
	
	public Calendar parseCalendar(String calString){
		try{
			Date calDate = _format.parse(calString.trim());
			Calendar cal = Calendar.getInstance();
			cal.setTime(calDate);
			return cal;
		}catch(Exception ex){
			ex.printStackTrace();
			return Calendar.getInstance();
		}
	}

	public String formatCalendar(Calendar cal){
		return this._format.format(cal.getTime());
	}
}
