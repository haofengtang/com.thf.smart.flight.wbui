package com.thf.ge.am.wbui.dm;

import java.util.Calendar;

import com.thf.ge.am.wbui.utils.StringUtils;

public class BuildHistory {

	private long recordId;
	private String equipmentCd;
	private String buildCd;
	private boolean hasAnamoly;
	private Calendar startTs;
	private Calendar endTs;
	
	public BuildHistory(String line) {
		String[] fields = line.split(",");
		this.setRecordId(Long.parseLong(fields[0].trim()));
		this.setEquipmentCd(fields[1].trim());
		this.setBuildCd(fields[2].trim());
		this.setHasAnamoly((fields[3]!=null&&"Y".equalsIgnoreCase(fields[3].trim()))?true:false);
		this.setStartTs(StringUtils.getInstance().parseCalendar(fields[4]));
		this.setEndTs(StringUtils.getInstance().parseCalendar(fields[5]));
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

	public String getBuildCd() {
		return buildCd;
	}

	public void setBuildCd(String buildCd) {
		this.buildCd = buildCd;
	}

	public boolean isHasAnamoly() {
		return hasAnamoly;
	}

	public void setHasAnamoly(boolean hasAnamoly) {
		this.hasAnamoly = hasAnamoly;
	}

	public Calendar getStartTs() {
		return startTs;
	}

	public void setStartTs(Calendar startTs) {
		this.startTs = startTs;
	}

	public Calendar getEndTs() {
		return endTs;
	}

	public void setEndTs(Calendar endTs) {
		this.endTs = endTs;
	}

}
