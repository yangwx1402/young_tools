package com.young.tools.miner.cluster.canopy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import com.young.tools.miner.cluster.canopy.bean.Canopy;
/**
 * Canopy聚类
 * @author yangy
 *
 */
public class CanopyCluster {

	/**
	 * Canopy聚类算法
	 * @param distance  距离度量方法  
	 * @param points  点集
	 * @param T1   T1
	 * @param T2   T2
	 * @return 所有的Canopy集合
	 */
	public List<Canopy> runCanopy(DistanceMeasure distance,
			Map<String,Vector> points, double T1, double T2) {
		List<Canopy> canopys = new ArrayList<Canopy>();
		List<String> removePoints = new ArrayList<String>();
		int count = 0;
		Canopy canopy = null;
		double similarity = 0.0;
		while (points.size() != 0) {
			for (Map.Entry<String,Vector> point:points.entrySet()) {
				//如果还不存在Canopy那么第一点就作为一个Canopy
				if (canopy == null) {
					canopy = new Canopy("canopy-" + (count++), point.getValue());
					canopy.addPoint(point.getValue());
					removePoints.add(point.getKey());
				} else {
					//计算点跟Canopy之间的距离
					similarity = distance.distance(point.getValue(), canopy.getCenter());
					//如果小于T1,那么加入该Canopy
					if (similarity < T1) {
						canopy.addPoint(point.getValue());
						//如果小于T2,那么从点集中删掉该点
						if (similarity < T2) {
							removePoints.add(point.getKey());
						}
					}
				}
			}
			for(String pointId:removePoints){
				points.remove(pointId);
			}
			removePoints.clear();
			canopys.add(canopy);
			canopy = null;
		}
		return canopys;
	}
}
