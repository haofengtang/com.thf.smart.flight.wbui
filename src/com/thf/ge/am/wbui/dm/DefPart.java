package com.thf.ge.am.wbui.dm;

import java.util.ArrayList;
import java.util.List;

public class DefPart {

	private long recordId;
	private int partIndex;
	private String partCd;
	private String name;
	private String desc;
	private boolean hasAnomaly;
	private List<DefPartLayer> layers;
	
	public List<DefPartLayer> getLayers() {
		return layers;
	}

	public void setLayers(List<DefPartLayer> layers) {
		this.layers = layers;
	}

	public void addLayer(DefPartLayer layer) {
		if(this.layers==null) this.layers=new ArrayList<DefPartLayer>();
		this.layers.add(layer);
	}

	public DefPart() {
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

	public int getPartIndex() {
		return partIndex;
	}

	public void setPartIndex(int partIndex) {
		this.partIndex = partIndex;
	}
}
