package com.thf.ge.am.wbui.utils;

import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OSUtils {

	final static public String Platform_Windows64 = "Win64";
	final static public String Platform_Windows32 = "Win32";
	final static public String Platform_MacOSX = "MacOSX";

	final static private Hashtable<String, Process> procRegsitry = new Hashtable<String, Process>();
	
	static private OSUtils _self;
	
	public OSUtils() {
	}

	static public OSUtils getInstance(){
		if(_self==null) _self=new OSUtils();
		return _self;
	}

	public void registerProcess(String procName, Process proc){
		procRegsitry.put(procName, proc);
	}

	public void killProcess(String procName){
		Process proc = procRegsitry.get(procName);
		if(proc!=null&&proc.isAlive()){
			proc.destroy();
			while(proc!=null&&proc.isAlive()){
				try{Thread.sleep(2000);}catch(Exception ex){ex.printStackTrace();}
				proc.destroyForcibly();
			}
			proc=null;
		}
		procRegsitry.remove(procName);
	}

	public void killWindowsOrphanProcesses(String procName){
		if(procName==null) return;
		try{
			String cmdTaskList = "tasklist.exe /fo csv /nh";
//			String cmdTaskKill = "taskkill /f /im main_V1.exe ";
			String cmdTaskKill = "taskkill /f /PID ";
			Logger.getLogger(this.getClass().getName()).log(Level.INFO, cmdTaskList);
			final Process proc = Runtime.getRuntime().exec(cmdTaskList);
			new Thread(() -> {
		        Scanner sc = new Scanner(proc.getInputStream());
//		        if (sc.hasNextLine()) sc.nextLine();
		        while (sc.hasNextLine()) {
		            String line = sc.nextLine();
					Logger.getLogger(this.getClass().getName()).log(Level.INFO, "[line] "+line);
		            String[] parts = line.split(",");
		            String unq = parts[0].substring(1).replaceFirst(".$", "");
		            String pid = parts[1].substring(1).replaceFirst(".$", "");
		            System.out.println(unq + " " + pid);
		            
		            if(procName.equals(unq)){
		    			Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Killing process ["+cmdTaskKill+pid+"]");
		            	try{Runtime.getRuntime().exec(cmdTaskKill+pid);}catch(Exception ioex){ioex.printStackTrace();}
		    			Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Killed process "+unq);
		            }
		        }
		        sc.close();
		    }).start();
			proc.waitFor();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
