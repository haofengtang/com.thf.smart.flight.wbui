package com.thf.ge.am.wbui.dm;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.SortedSet;
import java.util.TreeSet;

public class BuildStatusRecord {

	private String equipment;
	private String buildCd;
	private int latestLayerIdx;
	private int layerTotal;
	private double latestLayerHeight;
	private String latestLayerCd;
	private String buildStatus;
	private boolean lastLayerAnomaly;
	private Calendar timestamp;
	 
	 
	public BuildStatusRecord(Hashtable<Integer, BuildStatus> elements) {
		SortedSet<Integer> lIdxs = new TreeSet<Integer>(elements.keySet());
//		
//		BuildStatus[] sbs = new BuildStatus[elements.size()];
//		sbs = elements.toArray(sbs);
//		Arrays.sort(sbs, new Comparator<BuildStatus>() {
//		    public int compare(BuildStatus o1, BuildStatus o2) {
//		        return o1.getLayerIdx()-o2.getLayerIdx();
//		    }
//		});
//
		StringBuffer s = new StringBuffer();
		for(Integer idx:lIdxs){
			BuildStatus sb = elements.get(idx);
			if(sb.isHasAnamoly()){
				s.append((s.length()>0?",":"")+sb.getLayerIdx());
			}			
		}
		BuildStatus lastLayer = elements.get(lIdxs.last());
//		
//		
//		for(BuildStatus sb:sbs){
//			if(sb.isHasAnamoly()){
//				s.append((s.length()>0?",":"")+sb.getLayerIdx());
//			}
//			if(maxLayerIdx<sb.getLayerIdx()){
//				maxLayerIdx = sb.getLayerIdx();
//				lastLayer = sb;
//			}
//		}
		if(lastLayer!=null){
			this.setEquipment(lastLayer.getEquipmentCd());
			this.setBuildCd(lastLayer.getBuildCd());
			this.setLatestLayerIdx(lastLayer.getLayerIdx());
			this.setLayerTotal(lastLayer.getLayerTotal());
			this.setLatestLayerHeight(lastLayer.getLayerHeight());
			this.setLatestLayerCd(lastLayer.getLayerCd());
			this.setBuildStatus(s.toString()+"_"+lIdxs.last()+"_"+lastLayer.getLayerTotal());
			this.setLastLayerAnomaly(lastLayer.isHasAnamoly());
			this.setTimestamp(lastLayer.getCreateTs());
		}
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getBuildCd() {
		return buildCd;
	}

	public void setBuildCd(String buildCd) {
		this.buildCd = buildCd;
	}

	public int getLatestLayerIdx() {
		return latestLayerIdx;
	}

	public void setLatestLayerIdx(int latestLayerIdx) {
		this.latestLayerIdx = latestLayerIdx;
	}

	public double getLatestLayerHeight() {
		return latestLayerHeight;
	}

	public void setLatestLayerHeight(double latestLayerHeight) {
		this.latestLayerHeight = latestLayerHeight;
	}

	public String getLatestLayerCd() {
		return latestLayerCd;
	}

	public void setLatestLayerCd(String latestLayerCd) {
		this.latestLayerCd = latestLayerCd;
	}

	public String getBuildStatus() {
		return buildStatus;
	}

	public void setBuildStatus(String buildStatus) {
		this.buildStatus = buildStatus;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isLastLayerAnomaly() {
		return lastLayerAnomaly;
	}

	public void setLastLayerAnomaly(boolean lastLayerAnomaly) {
		this.lastLayerAnomaly = lastLayerAnomaly;
	}

	public int getLayerTotal() {
		return layerTotal;
	}

	public void setLayerTotal(int layerTotal) {
		this.layerTotal = layerTotal;
	}
}
