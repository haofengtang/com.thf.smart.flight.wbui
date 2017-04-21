package com.thf.ge.am.wbui.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtils {

	private SimpleDateFormat _format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static private StringUtils _self;
	
	public StringUtils() {
	}

	static public StringUtils getInstance(){
		if(_self==null) _self=new StringUtils();
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

	public void populateThresholds(int[] alarmThresholds, String arg) {
		String[] thresholds = arg.split(",");
		if(thresholds!=null&&thresholds.length==4){
			for(int i=0;i<4;i++)
				alarmThresholds[i]=Integer.parseInt(thresholds[i]);
		}
	}

}
