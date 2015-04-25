package com.young.tools.miner.cluster.canopy.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.math.Vector;
/**
 * Canopy类
 * @author yangy
 *
 */
public class Canopy {
	/**
	 * canopy的id
	 */
	private String canopyId;
    /**
     * 中心点
     */
	private Vector center;
    /**
     * 属于canopy的点
     */
	private List<Vector> points = new ArrayList<Vector>();

	public Canopy(String canopyId,Vector center){
		this.canopyId = canopyId;
		this.center = center;
	}
	
	public void addPoint(Vector point){
		points.add(point);
	}
	
	public String getCanopyId() {
		return canopyId;
	}

	public Vector getCenter() {
		return center;
	}

	public List<Vector> getPoints() {
		return points;
	}
	
	public String toString(){
		return canopyId+"\t"+center.asFormatString();
	}

}
