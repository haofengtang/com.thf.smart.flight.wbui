package com.thf.ge.am.wbui.dm;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class DefLayer {

	private long recordId;
	private int layerIdx;
	private String layerCd;
	private String name;
	private String plotPath;
	private int[] anomalyPartIdxs;
	private String desc;
	private SortedSet<DefPartLayer> parts;
	
	public DefLayer() {
		if(this.parts == null) this.parts = new TreeSet<DefPartLayer>(new Comparator<DefPartLayer>(){
			@Override
			public int compare(DefPartLayer o1, DefPartLayer o2) {
				return o1.getPartIndex() - o2.getPartIndex();
			}
		});
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public String getLayerCd() {
		return layerCd;
	}

	public void setLayerCd(String layerCd) {
		this.layerCd = layerCd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int[] getAnomalyPartIdxs() {
		return anomalyPartIdxs;
	}

	public void setAnomalyPartIdxs(int[] anomalyPartIdxs) {
		this.anomalyPartIdxs = anomalyPartIdxs;
	}

	public SortedSet<DefPartLayer> getParts() {
		return parts;
	}

	public void setParts(SortedSet<DefPartLayer> parts) {
		this.parts = parts;
	}

	public void addPart(DefPartLayer part) {
		this.parts.add(part);
	}

	public String getPlotPath() {
		return plotPath;
	}

	public void setPlotPath(String plotPath) {
		this.plotPath = plotPath;
	}

	public int getLayerIdx() {
		return layerIdx;
	}

	public void setLayerIdx(int layerIdx) {
		this.layerIdx = layerIdx;
	}
}
