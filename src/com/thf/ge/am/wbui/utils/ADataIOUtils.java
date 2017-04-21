package com.thf.ge.am.wbui.utils;

import java.util.Hashtable;
import java.util.List;

import com.thf.ge.am.wbui.dm.BuildHistory;
import com.thf.ge.am.wbui.dm.BuildStatus;
import com.thf.ge.am.wbui.dm.DefLayer;
import com.thf.ge.am.wbui.dm.DefPart;

public abstract class ADataIOUtils {

//	abstract public ADataIOUtils getInstance();
	
	abstract public List<BuildStatus> readData();
	
	abstract public Hashtable<String, BuildHistory> readHistoryData();
	
//	abstract public int isDataFileModified();
//
	abstract public byte computeBuildAlertLevel();

	abstract public Hashtable<Integer, DefLayer> getLayerDetailData(String buildCode);

	abstract public Hashtable<Integer, DefPart> getPartDetailData(String buildCode);	
	
	abstract public String getInitialImagePath();

//	abstract public String getEquipmentCode();
//
//	abstract public String getCommandString();
//
//	abstract public String getSourceDataPath();
//	
//	abstract public String getTargetDataPath();
//	
//	abstract public String getOpersys();
//
//	abstract public String getWinMainName();
}
