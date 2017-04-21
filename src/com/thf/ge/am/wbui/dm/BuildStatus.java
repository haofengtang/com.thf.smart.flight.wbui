package com.thf.ge.am.wbui.dm;

import java.util.Calendar;

import com.thf.ge.am.wbui.utils.StringUtils;

public class BuildStatus {

	private long recordId;
	private String equipmentCd;
	private String buildDef;
	private String buildCd;
	private int layerIdx;
	private int layerTotal;
	private double layerHeight;
	private String layerCd;
	private boolean hasAnamoly;
	private String riskLevel;
	private Calendar createTs;
	
	public BuildStatus() {
	}

	public BuildStatus(String line) {
		int startPos = 0;
		String[] fields = line.split(",");
		this.setRecordId(Long.parseLong(fields[startPos++].trim()));
		this.setEquipmentCd(fields[startPos++].trim());
		this.setBuildDef(fields[startPos++].trim());
		this.setBuildCd(fields[startPos++].trim());
		this.setLayerIdx(Integer.parseInt(fields[startPos++].trim()));
		this.setLayerHeight(Double.parseDouble(fields[startPos++].trim()));
		this.setLayerCd(fields[startPos++].trim());
		String hasAnomaly = fields[startPos++];
		this.setHasAnamoly((hasAnomaly!=null&&"Y".equalsIgnoreCase(hasAnomaly.trim()))?true:false);
		this.setRiskLevel(fields[startPos++].trim());
		this.setCreateTs(StringUtils.getInstance().parseCalendar(fields[startPos++]));
		this.setLayerTotal(Integer.parseInt(fields[startPos++].trim()));
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public String getEquipmentCd() {
		return equipmentCd;
	}

	public void setEquipmentCd(String equipmentCd) {
		this.equipmentCd = equipmentCd;
	}

	public String getBuildDef() {
		return buildDef;
	}

	public void setBuildDef(String buildDef) {
		this.buildDef = buildDef;
	}

	public String getBuildCd() {
		return buildCd;
	}

	public void setBuildCd(String buildCd) {
		this.buildCd = buildCd;
	}

	public int getLayerIdx() {
		return layerIdx;
	}

	public void setLayerIdx(int layerIdx) {
		this.layerIdx = layerIdx;
	}

	public double getLayerHeight() {
		return layerHeight;
	}

	public void setLayerHeight(double layerHeight) {
		this.layerHeight = layerHeight;
	}

	public String getLayerCd() {
		return layerCd;
	}

	public void setLayerCd(String layerCd) {
		this.layerCd = layerCd;
	}

	public boolean isHasAnamoly() {
		return hasAnamoly;
	}

	public void setHasAnamoly(boolean hasAnamoly) {
		this.hasAnamoly = hasAnamoly;
	}

	public Calendar getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Calendar createTs) {
		this.createTs = createTs;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public int getLayerTotal() {
		return layerTotal;
	}

	public void setLayerTotal(int layerTotal) {
		this.layerTotal = layerTotal;
	}

}
