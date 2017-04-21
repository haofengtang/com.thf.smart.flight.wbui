package com.thf.ge.am.wbui.dm;

public class ConfBuild {

	private long recordid;
	private String buildDef;
	private int partIdx;
	private long partId;
	private String geometry;
	
	public ConfBuild() {
	}

	public long getRecordid() {
		return recordid;
	}

	public void setRecordid(long recordid) {
		this.recordid = recordid;
	}

	public String getBuildDef() {
		return buildDef;
	}

	public void setBuildDef(String buildDef) {
		this.buildDef = buildDef;
	}

	public int getPartIdx() {
		return partIdx;
	}

	public void setPartIdx(int partIdx) {
		this.partIdx = partIdx;
	}

	public long getPartId() {
		return partId;
	}

	public void setPartId(long partId) {
		this.partId = partId;
	}

	public String getGeometry() {
		return geometry;
	}

	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

}
