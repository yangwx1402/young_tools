package com.young.tools.miner.cluster.kmeans.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

import com.google.common.collect.Lists;
import com.young.tools.miner.cluster.mahout.kmeans.Cluster;
import com.young.tools.miner.cluster.mahout.kmeans.KMeansClusterer;
/**
 * 利用Mahout进行KMeans聚类
 * @author yangy
 *
 */
public class MahoutKMeans {

	/**
	 * 运行聚类算法
	 * @param initCenters 初始中心点
	 * @param points   点集
	 * @param distance  距离计算公式
	 * @param maxIter   最大迭代次数
	 * @param distanceThreshold  距离阀值
	 * @return
	 */
	public List<Cluster> runKMeans(List<Vector> initCenters,
			List<Vector> points, DistanceMeasure distance, int maxIter,
			double distanceThreshold) {
		List<Cluster> initClusters = new ArrayList<Cluster>();
		Cluster cluster = null;
		int count = 0;
		for (Vector v : initCenters) {
			cluster = new Cluster(v, count++, distance);
			initClusters.add(cluster);
		}
		List<List<Cluster>> finalClusters = KMeansClusterer.clusterPoints(
				points, initClusters, distance, maxIter, distanceThreshold);
		List<Cluster> clusters = finalClusters.get(finalClusters.size() - 1);
		if (!CollectionUtils.isEmpty(clusters)) {
			return clusters;
		}
		return Lists.newArrayList();
	}
    /**
     * 计算聚类中心点和半径之间的距离
     * @param measure
     * @param center
     * @param radiusVector
     * @return
     */
	private double getMaxDistance(DistanceMeasure measure, Vector center,
			Vector radiusVector) {
		return measure.distance(center, radiusVector);
	}
    /**
     * 把每个点都分配到各个中心中去
     * @param clusters
     * @param points
     * @param measure
     * @return
     */
	public Map<String, List<Vector>> classifiPoints(List<Cluster> clusters,
			List<Vector> points, DistanceMeasure measure) {
		Map<String, Double> maxDistances = new HashMap<String, Double>();
		double maxDistance = 0.0;
		for (Cluster c : clusters) {
			maxDistance = getMaxDistance(measure, c.getCenter(), c.getRadius());
			maxDistances.put(c.getIdentifier(), maxDistance);
		}
		Map<String, List<Vector>> classiPoints = new HashMap<String, List<Vector>>();
		List<Vector> clusterPoints = null;
		double distance = 0.0;
		for (Vector v : points) {
			System.out.println(v);
			String nearCluster = clusters.get(0).getIdentifier();
			double nearDistance = Double.MAX_VALUE;
			for (Cluster cluster : clusters) {
				distance = measure.distance(cluster.getCenter(), v);
				if (distance <= maxDistances.get(cluster.getIdentifier())) {
					clusterPoints = classiPoints.get(cluster.getIdentifier());
					if (clusterPoints == null) {
						clusterPoints = Lists.newArrayList();
					}
					clusterPoints.add(v);
					classiPoints.put(cluster.getIdentifier(), clusterPoints);
					break;
				}else{
					if(distance<nearDistance){
						nearDistance = distance;
						nearCluster = cluster.getIdentifier();
						System.out.println("nearDistance="+nearDistance+",nearCluster="+nearCluster);
					}
				}
			}
			clusterPoints = classiPoints.get(nearCluster);
			if (clusterPoints == null) {
				clusterPoints = Lists.newArrayList();
			}
			clusterPoints.add(v);
			classiPoints.put(nearCluster, clusterPoints);
		}
		return classiPoints;
	}

}
