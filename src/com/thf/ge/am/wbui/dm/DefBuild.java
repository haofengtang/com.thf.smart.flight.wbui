package com.thf.ge.am.wbui.dm;

import java.util.Hashtable;

public class DefBuild {

	private long recordId;
	private String buildDef;
	private int totalLayer;
	private double totalHeight;
	private Hashtable<Integer, DefLayer> layers;
	
	public DefBuild() {
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public String getBuildDef() {
		return buildDef;
	}

	public void setBuildDef(String buildDef) {
		this.buildDef = buildDef;
	}

	public int getTotalLayer() {
		return totalLayer;
	}

	public void setTotalLayer(int totalLayer) {
		this.totalLayer = totalLayer;
	}

	public double getTotalHeight() {
		return totalHeight;
	}

	public void setTotalHeight(double totalHeight) {
		this.totalHeight = totalHeight;
	}

	public Hashtable<Integer, DefLayer> getLayers() {
		return layers;
	}

	public void setLayers(Hashtable<Integer, DefLayer> layers) {
		this.layers = layers;
	}

}
