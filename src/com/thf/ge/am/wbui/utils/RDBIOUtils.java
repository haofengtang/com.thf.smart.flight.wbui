package com.thf.ge.am.wbui.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thf.ge.am.wbui.dm.BuildHistory;
import com.thf.ge.am.wbui.dm.BuildStatus;
import com.thf.ge.am.wbui.dm.DefLayer;
import com.thf.ge.am.wbui.dm.DefPart;
import com.thf.ge.am.wbui.dm.DefPartLayer;

public class RDBIOUtils extends ADataIOUtils {

	static private RDBIOUtils _self;

	static private int[] _alarmThresholds;
	static private long _lastModifiedDate;
	static private Hashtable<Integer, BuildStatus> _data;
	static private Hashtable<Integer, DefLayer> _layerResult;
	static private Hashtable<Integer, DefPart> _partResult;
	static private Hashtable<String, String> _buildTypeCode;
	static private String url;
	static private Properties props;

//	@Inject
//	IEventBroker eventBroker;
	
	public RDBIOUtils() {
		
    	props = new Properties();

    	// Predix Basic
    	url = "jdbc:postgresql://10.72.6.143:5432/dcbb18a7132614823bc5d8952d19403df";
    	props.setProperty("user","ub79496598fab42cb8737433a728e5445");
    	props.setProperty("password","fe3b17a951a849dc9157515624896fac");

    	// Haofeng Dell
//    	url = "jdbc:postgresql://3.39.65.106:5432/geamwbui";
//    	props.setProperty("user","mpwbuser");
//    	props.setProperty("password","wearegreat");
    	// Local
//    	url = "jdbc:postgresql://localhost:5432/geamwbui";
//    	props.setProperty("user","postgres");
//    	props.setProperty("password","wearegreat");

    	_alarmThresholds = new int[]{1,15,7,1};
		_buildTypeCode = new Hashtable<String, String>();
		_buildTypeCode.put("c", "coupon");
		_buildTypeCode.put("fn", "fuelnozzle");
		_buildTypeCode.put("s", "standards");
		_lastModifiedDate = 0;
	}

	static public RDBIOUtils getInstance(){
		if(_self==null) _self=new RDBIOUtils();
		return _self;
	}

	public List<BuildStatus> readData(){
		List<BuildStatus> records = this.readBuildStatusData();
		return records;
	}
	
	public Hashtable<String, BuildHistory> readHistoryData(){
		Hashtable<String, BuildHistory> records = this.readBuildHistoryData();
		return records;
	}

	public byte computeBuildAlertLevel(){
		if(_partResult==null) return EventUtils.AlertLevel_OK;
		Hashtable<String, Integer> levelGrid = new Hashtable<String, Integer>();
		levelGrid.put("Yellow", 0);
		levelGrid.put("Red", 0);
		for(Integer partIdx:_partResult.keySet()){
			int anamalyLayerCount = _partResult.get(partIdx).getLayers().size();
			if(anamalyLayerCount>0&&anamalyLayerCount<_alarmThresholds[1]){	//15
				Integer yellowCount = levelGrid.get("Yellow");
				if(yellowCount==null) yellowCount=0;
				levelGrid.put("Yellow", ++yellowCount);
			}else if(anamalyLayerCount>=_alarmThresholds[1]){				//15
				Integer redCount = levelGrid.get("Red");
				if(redCount==null) redCount=0;
				levelGrid.put("Red", ++redCount);
			}
		}
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, levelGrid.toString());
		if(levelGrid.get("Red")>=_alarmThresholds[3]||levelGrid.get("Yellow")>=_alarmThresholds[2])	//1,7
			return EventUtils.AlertLevel_Error;
		else if(levelGrid.get("Yellow")>=_alarmThresholds[0])										//1
			return EventUtils.AlertLevel_Warning;
		else
			return EventUtils.AlertLevel_OK;
	}

	/**
	 * @param buildCode
	 * @return layer index -> (part index -> plots path)
	 */
	public void cacheLayerAndPartDetailData(String buildCode, int lastLayerIdx){
		// Cretaing a cache
		if(_layerResult==null) _layerResult = new Hashtable<Integer, DefLayer>();
		_layerResult.clear();
		if(_partResult==null) _partResult = new Hashtable<Integer, DefPart>();
		_partResult.clear();

		try{
			Class.forName("org.postgresql.Driver");

			Connection conn = null;
	    	conn = DriverManager.getConnection(url, props);
	    	Statement stmtBuildStatus = conn.createStatement();
	    	/**
					SELECT b.id, b.builddefcode, b.buildcode, b.createdts as build_c_ts, b.modifiedts 
	    					, e.equipcode 
	    					, l.layerindex, l.layerheight, l.apartcount, l.risklevel, l.imageid as layerimgidx, l.createdts as layer_c_ts 
	    					, r.partid, r.imageid as partimgidx
	    					, d.totallayercount 
	    					FROM mpspc.buildstatus b 
	    					INNER JOIN mpspc.builddef d ON b.builddefid = d.id 
	    					INNER JOIN mpspc.equipment e ON b.equipid = e.id 
	    					INNER JOIN mpspc.layerstatus l ON b.id=l.buildid
	    					INNER JOIN mpspc.layerpartregion r ON r.layerid = l.id
	    					WHERE b.status='Y';
	    	*/
	    	String query = "SELECT b.id, b.builddefcode, b.buildcode, b.createdts as build_c_ts, b.modifiedts "
	    					+", e.equipcode "
	    					+", l.layerindex, l.layerheight, l.apartcount, l.risklevel, l.imageid as layerimgidx, l.createdts as layer_c_ts "
	    					+", r.partindex, r.imageid as partimgidx "
	    					+", d.totallayercount "
	    					+"FROM mpspc.buildstatus b "
	    					+"INNER JOIN mpspc.builddef d ON b.builddefid = d.id "
	    					+"INNER JOIN mpspc.equipment e ON b.equipid = e.id "
	    					+"INNER JOIN mpspc.layerstatus l ON b.id=l.buildid "
	    					+"LEFT OUTER JOIN mpspc.layerpartregion r ON r.layerid = l.id "
	    					+"WHERE b.status='Y'";
	    	ResultSet rs = stmtBuildStatus.executeQuery(query);
	    	while(rs.next()){
	    		int layerIndex = rs.getInt("layerindex");
	    		String layerCode = rs.getString("buildcode")+"_"+layerIndex;
	    		long layerImageIndex = rs.getLong("layerimgidx");

	    		int partIndex = -1;
	    		long partImageIndex = -1l;
	    		String partCode = null;
	    		
	    		if(rs.getObject("partindex")!=null){
		    		partIndex = rs.getInt("partindex");
		    		partCode = "Part "+partIndex;
		    		partImageIndex = rs.getLong("partimgidx");
	    		}

	    		DefLayer newLayer = _layerResult.get(layerIndex);
	    		if(newLayer==null) {
	    			newLayer = new DefLayer();
	    			newLayer.setLayerIdx(layerIndex);
					newLayer.setName(layerCode);
					newLayer.setPlotPath(String.valueOf(layerImageIndex));
	    			_layerResult.put(layerIndex, newLayer);
	    		}
	    		if(partIndex>=0){
					DefPart newPart = _partResult.get(partIndex);
					if(newPart==null){
						newPart=new DefPart();
						newPart.setPartIndex(partIndex);
						newPart.setPartCd(partCode);
						_partResult.put(partIndex, newPart);
					}
					DefPartLayer plottedPart = new DefPartLayer();
					plottedPart.setLayerIndex(layerIndex);
					plottedPart.setLayerCd(layerCode);
					plottedPart.setPartIndex(partIndex);
					plottedPart.setPartCd(partCode);
					plottedPart.setPlotPath(String.valueOf(partImageIndex));

					newLayer.addPart(plottedPart);
					newPart.addLayer(plottedPart);
	    		}
			}
		}catch(Exception ex){
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, ex.getMessage());
		}
	}
	
	public Hashtable<Integer, DefLayer> getLayerDetailData(String buildCode){
		return _layerResult;
	}

	/**
	 * @param buildCode
	 * @return layer index -> (part index -> plots path)
	 */
	public Hashtable<Integer, DefPart> getPartDetailData(String buildCode){
		return _partResult;
	}
	
	public String getInitialImagePath(){
//		return _root+"/record/init_plot.png";
		return "/icons/initial_geam.gif";
	}

	private List<BuildStatus> readBuildStatusData(){
		List<BuildStatus> records = new ArrayList<BuildStatus>();
		try{
			Class.forName("org.postgresql.Driver");

			Connection conn = null;
	    	conn = DriverManager.getConnection(url, props);
	    	Statement stmtBuildStatus = conn.createStatement();
	    	/**
SELECT b.id, b.builddefcode, b.buildcode, b.createdts as build_c_ts, b.modifiedts
       , e.equipcode
       , l.layerindex, l.layerheight, l.apartcount, l.risklevel, l.createdts as layer_c_ts
       , d.totallayercount
  FROM mpspc.buildstatus b
    INNER JOIN mpspc.builddef d ON b.builddefid = d.id
    INNER JOIN mpspc.equipment e ON b.equipid = e.id
    LEFT OUTER JOIN mpspc.layerstatus l ON b.id=l.buildid
  WHERE b.status='Y';	    	 */
	    	String query = "SELECT b.id, b.builddefcode, b.buildcode, b.createdts as build_c_ts, b.modifiedts "
	    					+", e.equipcode "
	    					+", l.layerindex, l.layerheight, l.apartcount, l.risklevel, l.createdts as layer_c_ts "
	    					+", d.totallayercount "
	    					+"FROM mpspc.buildstatus b "
	    					+"INNER JOIN mpspc.builddef d ON b.builddefid = d.id "
	    					+"INNER JOIN mpspc.equipment e ON b.equipid = e.id "
	    					+"INNER JOIN mpspc.layerstatus l ON b.id=l.buildid "
	    					+"WHERE b.status='Y' and e.status='Y' and d.status='Y' and l.status='Y'";
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, query);
	    	ResultSet rs = stmtBuildStatus.executeQuery(query);
	    	while(rs.next()){
	    		BuildStatus bsts = new BuildStatus();
	    		bsts.setRecordId(rs.getLong("id"));
	    		bsts.setEquipmentCd(rs.getString("equipcode"));
	    		bsts.setBuildDef(rs.getString("builddefcode"));
	    		bsts.setBuildCd(rs.getString("buildcode"));
	    		bsts.setLayerIdx(rs.getInt("layerindex"));
	    		bsts.setLayerHeight(rs.getDouble("layerheight"));
	    		bsts.setLayerCd(bsts.getBuildCd()+"_"+bsts.getLayerIdx());
	    		int apartCount = rs.getInt("apartcount");
	    		bsts.setHasAnamoly(apartCount>0);
	    		bsts.setRiskLevel(String.valueOf(rs.getInt("risklevel")));
	    		Calendar cCal = Calendar.getInstance();
	    		cCal.setTimeInMillis(rs.getTimestamp("layer_c_ts").getTime());
	    		bsts.setCreateTs(cCal);
	    		bsts.setLayerTotal(rs.getInt("totallayercount"));
	    		records.add(bsts);
	    	}
	    	rs.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return records;
	}
	
	private Hashtable<String, BuildHistory> readBuildHistoryData(){
		Hashtable<String, BuildHistory> records = new Hashtable<String, BuildHistory>();
		return records;
	}

	public InputStream getImageInputStream(String imgID){
		InputStream is = null;
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn = null;
	    	conn = DriverManager.getConnection(url, props);
	    	Statement stmtImg = conn.createStatement();
	    	ResultSet rs = stmtImg.executeQuery("SELECT imagedata FROM mpspc.imagestorage WHERE id="+imgID);
	    	if (rs!=null&&rs.next()) {
	    		byte[] imgBytes = rs.getBytes(1);
	    	    rs.close();
	    	    is = new ByteArrayInputStream(imgBytes);
	    	}
	    	stmtImg.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return is;
	}
}
