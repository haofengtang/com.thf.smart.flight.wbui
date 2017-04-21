package com.thf.ge.am.wbui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.input.ReversedLinesFileReader;
//import org.eclipse.core.resources.IWorkspace;
//import org.eclipse.core.resources.ResourcesPlugin;
//import org.eclipse.core.runtime.Platform;
//import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.FrameworkUtil;

import com.thf.ge.am.wbui.dm.BuildHistory;
import com.thf.ge.am.wbui.dm.BuildStatus;
import com.thf.ge.am.wbui.dm.DefLayer;
import com.thf.ge.am.wbui.dm.DefPart;
import com.thf.ge.am.wbui.dm.DefPartLayer;

public class RESTIOUtils extends ADataIOUtils {

	static private ADataIOUtils _self;
	static private String opersys;
	static private String winMainName;
	static private String equipcode;
	static private String python_comm;
	static private String source_path;
	static private String target_path;
	static private int[] _alarmThresholds;
	static private File _buildstatusDataFile;
	static private File _buildhistoryDataFile;
	static private long _lastModifiedDate;
	static private Hashtable<Integer, DefLayer> _layerResult;
	static private Hashtable<Integer, DefPart> _partResult;
	static private Hashtable<String, String> _buildTypeCode;

	public RESTIOUtils() {
		// Default - Hard coding
		opersys = OSUtils.Platform_Windows64;
		winMainName = "main_V1.exe";
		python_comm = "ls";
		source_path = "/Data/Temp/InputData";
		target_path = "/Data/Temp/MPData";
		equipcode = "SI7001";
		_alarmThresholds = new int[]{1,15,7,1};
		_buildTypeCode = new Hashtable<String, String>();
		_buildTypeCode.put("c", "coupon");
		_buildTypeCode.put("fn", "fuelnozzle");
		_buildTypeCode.put("s", "standards");
		
		// Overwriting - Property file
		Properties props = new Properties();
		try{
//			InputStream propenv = FileIOUtils.class.getClassLoader().getResourceAsStream("/conf/env.properties");

			/**
			 * Option #1			
			 */
//			Location location = Platform.getInstallLocation();
//			String rootPath = location.getURL().getFile();
//			File rootPathDir = new File(rootPath);
//			rootPath = rootPathDir.getParentFile().getParentFile().getPath();
			/**
			 * Option #2
			 */
//			String rootPath = this.getWorkspaceLocation().getParentFile().getPath();

//			if(rootPath.endsWith("/")) rootPath=rootPath+"../..";
//			else rootPath=rootPath+"/../..";
			
			/**
			 * Option #3
			 */
			String rootPath = FrameworkUtil.getBundle(this.getClass()).getLocation();
			
			// This is test line
//			rootPath = "/Users/212438161/Desktop";
			System.out.println("System installation root - "+rootPath);
			File propertyFile = new File(rootPath+"/env.properties");
			boolean fileExsit = (propertyFile!=null&&propertyFile.exists());
			System.out.println("Property file "+(rootPath+"/env.properties")+(fileExsit?" exsits.":" does NOT exist."));
			if(fileExsit){
				try{
					InputStream propis = new FileInputStream(propertyFile);
					props.load(propis);
//					props.load(propenv);
				}catch(Exception ex){ex.printStackTrace();}
			}
		}catch(Exception ex){ex.printStackTrace();}
		
		if(props.containsKey("opersys")) opersys = props.getProperty("opersys");
		if(props.containsKey("winMainName")) winMainName = props.getProperty("winMainName");
		if(props.containsKey("equipcode")) equipcode = props.getProperty("equipcode");
		if(props.containsKey("python_comm")) python_comm = props.getProperty("python_comm");
		if(props.containsKey("source_path")) source_path = props.getProperty("source_path");
		if(props.containsKey("target_path")) target_path = props.getProperty("target_path");
		if(props.containsKey("alarmThresholds")) StringUtils.getInstance().populateThresholds(_alarmThresholds, props.getProperty("alarmThresholds"));

		// Overwriting - Command line arguments. If the parameter is not showing in commandline, default value is used.
//		String[] commandArgs = Platform.getApplicationArgs();
//		for(String arg:commandArgs){
//			if(arg!=null&&arg.startsWith("target_path=")){
//				target_path = arg.substring(12).trim();
//			}else if(arg.startsWith("source_path=")){
//				source_path = arg.substring(12).trim();
//			}else if(arg.startsWith("alarmThresholds=")){
//				StringUtils.getInstance().populateThresholds(_alarmThresholds, arg.substring(16).trim());
//			}else if(arg.startsWith("opersys=")){
//				opersys = arg.substring(8).trim();
//			}else if(arg.startsWith("winMainName=")){
//				winMainName = arg.substring(12).trim();
//			}else if(arg.startsWith("equipcode=")){
//				equipcode = arg.substring(10).trim();
//			}else if(arg.startsWith("python_comm=")){
//				python_comm = arg.substring(12).trim();
//			}else if(arg.startsWith("alarmThresholds=")){
//				StringUtils.getInstance().populateThresholds(_alarmThresholds, arg.substring(16).trim());
//			}
//		}
		
		_buildstatusDataFile = new File(target_path+"/build_status.txt");
		System.out.println("File - "+_buildstatusDataFile.getAbsolutePath()+" exsists? "+_buildstatusDataFile.exists());
		_buildhistoryDataFile = new File(target_path+"/record/build_history.txt");
		System.out.println("File - "+_buildhistoryDataFile.getAbsolutePath()+" exsists? "+_buildhistoryDataFile.exists());
		_lastModifiedDate = 0;
	}

	static public ADataIOUtils getInstance(){
		if(_self==null) _self=new RESTIOUtils();
		return _self;
	}

	public List<BuildStatus> readData(){
		List<BuildStatus> records = this.readBuildStatusData();
		return records;
	}
	
	public Hashtable<String, BuildHistory> readHistoryData(){
		Hashtable<String, BuildHistory> records = this.readBuildHistoryData();
		return records;
	}
	
	public int isDataFileModified(){
		long currDate = _buildstatusDataFile.lastModified();
		long diff = currDate - _lastModifiedDate;
		_lastModifiedDate = currDate;
		return diff>0?1:(diff<0?-1:0);
	}

	public byte computeBuildAlertLevel(){
		if(_partResult==null) return EventUtils.AlertLevel_OK;
		Hashtable<String, Integer> levelGrid = new Hashtable<String, Integer>();
		levelGrid.put("Yellow", 0);
		levelGrid.put("Red", 0);
		for(Integer partIdx:_partResult.keySet()){
			int anamalyLayerCount = _partResult.get(partIdx).getLayers().size();
			if(anamalyLayerCount>0&&anamalyLayerCount<_alarmThresholds[1]){	//15
				Integer yellowCount = levelGrid.get("Yellow");
				if(yellowCount==null) yellowCount=0;
				levelGrid.put("Yellow", ++yellowCount);
			}else if(anamalyLayerCount>=_alarmThresholds[1]){				//15
				Integer redCount = levelGrid.get("Red");
				if(redCount==null) redCount=0;
				levelGrid.put("Red", ++redCount);
			}
		}
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, levelGrid.toString());
		if(levelGrid.get("Red")>=_alarmThresholds[3]||levelGrid.get("Yellow")>=_alarmThresholds[2])	//1,7
			return EventUtils.AlertLevel_Error;
		else if(levelGrid.get("Yellow")>=_alarmThresholds[0])										//1
			return EventUtils.AlertLevel_Warning;
		else
			return EventUtils.AlertLevel_OK;
	}

	/**
	 * @param buildCode
	 * @return layer index -> (part index -> plots path)
	 */
	public void cacheLayerAndPartDetailData(String buildCode, int lastLayerIdx){
//		String buildFileRoot = _root+"/product/"+buildCode+"/output_files";
		String buildPlotRoot = target_path+"/product/"+buildCode+"/violation_scatter_plots";
//		String layerPlotRoot = _root+"/product/"+buildCode+"/layer_plots";
//		Hashtable<Integer, DefLayer> result = new Hashtable<Integer, DefLayer>();

		// Cretaing a cache
		if(_layerResult==null) _layerResult = new Hashtable<Integer, DefLayer>();
		_layerResult.clear();
		if(_partResult==null) _partResult = new Hashtable<Integer, DefPart>();
		_partResult.clear();
		
		try{
			File plotsDir = new File(buildPlotRoot);
			if(plotsDir.exists()){
				File layerBoardPlotDir;
				String[] layerDirStrs = plotsDir.list();
				for(String layerDirStr:layerDirStrs){
					if(layerDirStr!=null
							&&!layerDirStr.trim().equals("")
							&&!layerDirStr.trim().startsWith(".")){
						Integer layerIdx = Integer.parseInt(layerDirStr.substring(layerDirStr.indexOf("_")+1));
						if(layerIdx<=lastLayerIdx){
							DefLayer newLayer = new DefLayer();
							newLayer.setName(layerDirStr);
							try{
								newLayer.setLayerIdx(Integer.parseInt(layerDirStr.substring(layerDirStr.indexOf("_")+1)));
							}catch(Exception ex){
								Logger.getLogger(this.getClass().getName()).log(Level.WARNING, ex.getMessage());
							}
							newLayer.setLayerCd(layerDirStr);
							String layerBoardPlotPath = buildPlotRoot+"/"+layerDirStr+"/board_scatter_plots/";
							layerBoardPlotDir = new File(layerBoardPlotPath);
							if(layerBoardPlotDir.exists()&&layerBoardPlotDir.list().length>0){
								_layerResult.put(layerIdx, newLayer);
								newLayer.setPlotPath(buildPlotRoot+"/"+layerDirStr
										+"/board_scatter_plots/"+layerBoardPlotDir.list()[0]);
								File partPlotRootDir = new File(buildPlotRoot+"/"+layerDirStr+"/part_scatter_plots");
								String[] partPlotsFileNameStrs = partPlotRootDir.list();
								for(String plotsFNStr:partPlotsFileNameStrs){
									DefPartLayer plottedPart = new DefPartLayer();
									//Populating
									String partSegemtn = plotsFNStr.split("_")[2];
									Integer partIdx = Integer.parseInt(partSegemtn.split(" ")[1].trim());
									plottedPart.setPartIndex(partIdx);
									plottedPart.setPartCd(partSegemtn);
									plottedPart.setLayerIndex(layerIdx);
									plottedPart.setLayerCd(layerDirStr);
									plottedPart.setPlotPath(partPlotRootDir.getAbsolutePath()+"/"+plotsFNStr);
									newLayer.addPart(plottedPart);

									DefPart part = _partResult.get(partIdx);
									if(part==null){
										part=new DefPart();
										part.setPartIndex(partIdx);
										part.setPartCd(partSegemtn);
										_partResult.put(partIdx, part);
									}
									part.addLayer(plottedPart);
								}
							}else{
								System.out.println("Directory either does not exist or is empty - "+layerBoardPlotDir);
							}
						}
					}
				}
			}else{
				System.out.println("Directory does not exist: plotsDir - "+plotsDir);
			}
		}catch(Exception ex){
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, ex.getMessage());
		}
	}
	
	public Hashtable<Integer, DefLayer> getLayerDetailData(String buildCode){
		return _layerResult;
	}

	/**
	 * @param buildCode
	 * @return layer index -> (part index -> plots path)
	 */
	public Hashtable<Integer, DefPart> getPartDetailData(String buildCode){
		return _partResult;
	}
	
	public String getInitialImagePath(){
//		return _root+"/record/init_plot.png";
		return "/icons/initial_geam.gif";
	}

	public String getEquipmentCode(){
		return equipcode;
	}

	public String getCommandString(){
		return python_comm;
	}

	public String getSourceDataPath(){
		return source_path;
	}

	public String getTargetDataPath(){
		return target_path;
	}

//	private Hashtable<String,ConfBuild> readBuildConfData(){
//		Hashtable<String,ConfBuild> records = new Hashtable<String,ConfBuild>();
////		try{
////			BufferedReader buildConfFile = new BufferedReader(new FileReader(""));
////			buildConfFile.
////		}catch(Exception ex){
////			ex.printStackTrace();
////		}
//		return records;
//	}
//
//	private Hashtable<Long,DefPartLayer> readPartDefData(){
//		Hashtable<Long,DefPartLayer> records = new Hashtable<Long,DefPartLayer>();
//		return records;
//	}
//	
	private List<BuildStatus> readBuildStatusData(){
		List<BuildStatus> records = new ArrayList<BuildStatus>();
		try{
			ReversedLinesFileReader fr = new ReversedLinesFileReader(_buildstatusDataFile);
			String ch = fr.readLine();
			if(ch!=null&&!"".equals(ch.trim())&&!ch.trim().startsWith("recordid")){
				BuildStatus lastRecord = new BuildStatus(ch.trim());
				records.add(lastRecord);
				String buildCode = lastRecord.getBuildCd();

//				int time=0;
//				String Conversion="";
				boolean isSameBuild = true;
				for(ch=fr.readLine();
						ch!=null&&isSameBuild;
						ch=fr.readLine()){
					if((ch.trim().startsWith("recordid"))){
						isSameBuild=false;
					}else{
						try{
							BuildStatus newRecord = new BuildStatus(ch.trim());
							if(buildCode.equals(newRecord.getBuildCd())){
								records.add(newRecord);
							}else{
								isSameBuild=false;
							}
						}catch(Exception ex){
							ex.getMessage();
						}
					}
				}
//				System.out.println("Layer record count in current build - "+records.size());
			}
			fr.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return records;
	}
	
//	private String readLastLine(File file){
//		String result="";
//		int lineLength = 100;
//		try( BufferedReader reader = new BufferedReader(new FileReader(file))){
//	        String line = null;
//	        System.out.println("=================== "+file.getAbsolutePath()+" ===================");
//	        if(file.length() >= lineLength*3){
////		        line = reader.readLine();       //Read Line ONE
////		        line = reader.readLine();       //Read Line TWO
////		        System.out.println("first line : " + line);
////		        //Length of one line if lines are of even length
////		        int len = line.length();       
////		        //skip to the end - 3 lines
//		        reader.skip((file.length() - (lineLength*3)));
//	        }
//	        //Searched to the last line for the date I was looking for.
//	        while((line = reader.readLine()) != null){
//	        	if(line!=null&&!"".equals(line.trim())){
//	        		result = line.trim();
//	        	}
//	        }
//	    } catch (IOException x){
//	        x.printStackTrace();
//	    }
//		return result;
//	}
//
	private Hashtable<String, BuildHistory> readBuildHistoryData(){
		Hashtable<String, BuildHistory> records = new Hashtable<String, BuildHistory>();
		try{
			BufferedReader fr = new BufferedReader(new FileReader(_buildhistoryDataFile));
			for(String ch=fr.readLine();
					ch!=null;
					ch=fr.readLine()){
				if(!(ch.trim().startsWith("recordid"))){
					try{
						BuildHistory newRecord = new BuildHistory(ch.trim());
						records.put(newRecord.getBuildCd(), newRecord);
					}catch(Exception ex){
						ex.getMessage();
					}
				}
			}
			fr.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return records;
	}
	
	public String getOpersys() {
		return opersys;
	}

	public void setOpersys(String opersys) {
		RESTIOUtils.opersys = opersys;
	}

	public FileHandler getFileHandler(String buildCode, String buildType){
		return new FileHandler(buildCode, buildType);
	}

	public class FileHandler extends Thread {
		final long INTERVAL = 3000;			// Check directory every 3 seconds
		final long MATURE_PERIOD = 30000;	// File matured if no size change for 30 seconds
		
		boolean active = true;
		String buildCd, buildTp;
		SortedSet<String> layersFileMatured;
		String dataDirecotryStr;
		File logFile;
		private FileHandler(String buildCode, String buildType){
			this.buildCd = buildCode;
			this.buildTp = buildType;
			this.dataDirecotryStr = source_path+"/"+_buildTypeCode.get(this.buildTp)+"/"+this.buildCd+"/";
			this.logFile = new File(source_path+"/"+_buildTypeCode.get(this.buildTp)+"/"+this.buildCd+"/"+_buildTypeCode.get(this.buildTp)+".txt");
		}
		public void run() {
			try{ 
				File dataDirecotry = new File(this.dataDirecotryStr);
				Logger.getLogger(this.getClass().getName()).log(Level.INFO, "FileHandler starting - "+this.dataDirecotryStr);
				long dataDirTS = dataDirecotry.lastModified();
				// Load materd layers
				// Load matured layers, with filter using build code
				layersFileMatured = this.loadMaturedLayers(this.logFile, buildCd); 						
				Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Content in the folder layersFileMatured.size() - "+layersFileMatured.size());
				while(active&&!isInterrupted()){
					long currDataDirTS = dataDirecotry.lastModified();
					Logger.getLogger(this.getClass().getName()).log(Level.INFO, "FileHandler - "+this.dataDirecotryStr+" - [layersFileMatured="+layersFileMatured+" currDataDirTS="+currDataDirTS+"]");
					if((currDataDirTS-dataDirTS)!=0){
						// Load showing layers
						// Load showing layers, with filter using build code
						SortedSet<String> layersFileShowing = this.loadShowingLayers(dataDirecotry, this.buildCd);
						Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Content in the folder layersFileShowing.size() - "+layersFileShowing.size());
						layersFileShowing.removeAll(layersFileMatured);
						for(String layer:layersFileShowing){
							Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Content in the folder - "+layer);
							if(active&&!isInterrupted()){
								this.hangOnUntilMature(layer);
								layersFileMatured.add(layer);
								Logger.getLogger(this.getClass().getName()).log(Level.INFO, "FileHandeler - 1");
								FileWriter writer = new FileWriter(this.logFile,true);
								writer.append("\n"+layer);
								writer.flush();
								if(writer!=null) writer.close();
								Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Matured file "+dataDirecotry+" - "+layer);
							}else{
								Logger.getLogger(this.getClass().getName()).log(Level.INFO, "FileHandeler - 2");
								break;
							}
						}
						dataDirTS = currDataDirTS;
					}
					Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Going into sleep.");
					Thread.sleep(INTERVAL);
				}
				dataDirecotry=null;
			}catch(Exception ex){
				ex.printStackTrace();
			}
			Logger.getLogger(this.getClass().getName()).log(Level.INFO, "FileHandler interrupted during ["+this.buildCd+"]");
		}
		public void inActivate(){
			this.active=false;
		}
		// Utilities
		private SortedSet<String> loadMaturedLayers(File logF, String buildCD){
			SortedSet<String> layers = new TreeSet<String>();
			try{
				if(logF!=null&&logF.exists()){
					BufferedReader reader = new BufferedReader(new FileReader(logF));
					String line = null;
					while((line=reader.readLine())!=null){
						line = line.trim();
						if(line.startsWith(buildCD)){
							layers.add(line);
						}
					}
					reader.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return layers;
		}
		private SortedSet<String> loadShowingLayers(File dataDir, String buildCD){
			SortedSet<String> layers = new TreeSet<String>();
			if(dataDir!=null&&dataDir.exists()&&dataDir.isDirectory()){
				String[] contents = dataDir.list();
				for(String fn:contents){
					if(fn!=null
							&&fn.startsWith(buildCD)
							&&fn.indexOf("result")<0
							&&(!(new File(dataDir, fn)).isDirectory())){
						layers.add(fn);
					}
				}
			}
			return layers;
		}
		private void hangOnUntilMature(String fileName){
			File layerFile = new File(this.dataDirecotryStr+"/"+fileName);
			boolean isComplete = false;
			long lastCheckTimeMS = Calendar.getInstance().getTimeInMillis();
			long initSize = layerFile.getTotalSpace();
			while(!isComplete){
				try{Thread.sleep(INTERVAL);}catch(InterruptedException itex){itex.printStackTrace();}
				long currTimeMS = Calendar.getInstance().getTimeInMillis();
				long currSize = layerFile.getTotalSpace();
				if((currSize-initSize)!=0){
					initSize = currSize;
					lastCheckTimeMS = currTimeMS;
					isComplete = false;
				}else if((currTimeMS-lastCheckTimeMS)>=MATURE_PERIOD){
					isComplete = true;
				}
			}
		}
	}

	public String getWinMainName() {
		return winMainName;
	}

	public void setWinMainName(String winMainName) {
		RESTIOUtils.winMainName = winMainName;
	}
}
