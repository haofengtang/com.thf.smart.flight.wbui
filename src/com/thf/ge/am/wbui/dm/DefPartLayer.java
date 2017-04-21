package com.thf.ge.am.wbui.dm;

public class DefPartLayer {

	private long recordId;
	private int partIndex;
	private String partCd;
	private int layerIndex;
	private String layerCd;
	private String name;
	private String desc;
	private String plotPath;
	private boolean hasAnomaly;
	private double mean;
	
	public DefPartLayer() {
		// TODO Auto-generated constructor stub
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public String getPartCd() {
		return partCd;
	}

	public void setPartCd(String partCd) {
		this.partCd = partCd;
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

	public boolean isHasAnomaly() {
		return hasAnomaly;
	}

	public void setHasAnomaly(boolean hasAnomaly) {
		this.hasAnomaly = hasAnomaly;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public int getPartIndex() {
		return partIndex;
	}

	public void setPartIndex(int partIndex) {
		this.partIndex = partIndex;
	}

	public String getPlotPath() {
		return plotPath;
	}

	public void setPlotPath(String plotPath) {
		this.plotPath = plotPath;
	}

	public int getLayerIndex() {
		return layerIndex;
	}

	public void setLayerIndex(int layerIndex) {
		this.layerIndex = layerIndex;
	}

	public String getLayerCd() {
		return layerCd;
	}

	public void setLayerCd(String layerCd) {
		this.layerCd = layerCd;
	}
}
