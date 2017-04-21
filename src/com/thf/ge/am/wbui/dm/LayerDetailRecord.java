package com.thf.ge.am.wbui.dm;

import java.util.ArrayList;
import java.util.List;

public class LayerDetailRecord {

	private DefLayer layer;
	private List<DefPartLayer> parts;

	public LayerDetailRecord() {
	}

	public DefLayer getLayer() {
		return layer;
	}

	public void setLayer(DefLayer layer) {
		this.layer = layer;
	}

	public List<DefPartLayer> getParts() {
		return parts;
	}

	public void setParts(List<DefPartLayer> parts) {
		this.parts = parts;
	}

	public void addPart(DefPartLayer part) {
		if(this.parts==null) this.parts=new ArrayList<DefPartLayer>();
		this.parts.add(part);
	}
	
//	private String buildCd;
//	private Hashtable<String, String[]> layers;
//	
//	public LayerDetailRecord() {
//		layers = new Hashtable<String, String[]>();
//		layers.put("SI700120170110010101_010_00_050", new String[]{"Part 1","Part 3","Part 4"});
//		layers.put("SI700120170110010101_010_00_150", new String[]{"Part 3"});
//	}
//
//	public String getBuildCd() {
//		return buildCd;
//	}
//
//	public void setBuildCd(String buildCd) {
//		this.buildCd = buildCd;
//	}
//
//	public Hashtable<String, String[]> getLayers() {
//		return layers;
//	}
//
//	public void setLayers(Hashtable<String, String[]> layers) {
//		this.layers = layers;
//	}
}
