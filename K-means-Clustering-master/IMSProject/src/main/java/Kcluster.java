/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.commons.collections4.map.MultiValueMap;

/**
 *
 * @author lucks
 */
public class Kcluster {

    public PointVariable[] CentroidsPos = null;
    public PointVariable[] oldCentroidsPos = null;
    

    private double EucledianDistance(PointVariable point1, PointVariable point2) {
        double distance;
        distance = Math.sqrt(Math.pow((point1.gettingX() - point2.gettingX()), 2) + Math.pow((point1.gettingY() - point2.gettingY()), 2));
        return distance;
    }

    private PointVariable[] randomCentroids(int k) {
        PointVariable[] rndPoints = new PointVariable[k];
        for (int i = 0; i < k; i++) {
            PointVariable point = new PointVariable();
            double x = gettingumberInRange(CSV.lowestX, CSV.highestX);
            double y = gettingumberInRange(CSV.lowestY, CSV.highestY);
            System.out.println(x + "," + y);
            point.settingX(x);
            point.settingY(y);

            rndPoints[i] = point;
        }

        return rndPoints;
    }

    private double gettingumberInRange(double min, double max) {

        if (min <= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    private MultiValueMap<Integer, PointVariable> placePointInCentroid(PointVariable[] Centroids, List<PointVariable> wifiGeoPoints) {
        //ArrayList<Point>[] clustersList = new ArrayList[Centroids.length];
        MultiValueMap<Integer, PointVariable> clustersList = new MultiValueMap<>();
        for (PointVariable point : wifiGeoPoints) {
            double minDistance = 10000000.0;
            int selectedCentroid = 0;
            for (int i = 0; i < Centroids.length; i++) {
                double distance = EucledianDistance(Centroids[i], point);
                //System.out.println("Distance: "+distance);
                if (distance < minDistance) {
                    minDistance = distance;
                    selectedCentroid = i;
                }
            }
            // System.out.println("Point: " + point.toString() + " Is close to Centroid: " + selectedCentroid);
            clustersList.put(selectedCentroid, point);

        }
        return clustersList;
    }

    public PointVariable calculatingClusterMean(int key, MultiValueMap<Integer, PointVariable> clustersList) {
        PointVariable newPoints = new PointVariable();
        double sumX = 0.0;
        double sumY = 0.0;
        double averageX = 0.0;
        double averageY = 0.0;
        int lstSize = 0;

        List<PointVariable> list;
        list = (List) clustersList.get(key);
       // System.out.println(">>>>>>>>>>Centroid " +key+": "+ list.toString());
        lstSize = list.size();
        //System.out.println(">>>>>>>>>>Size " +lstSize);
        for (int j = 0; j < lstSize; j++) {
            // System.out.println("\t" + key + "\t  " + list.get(j));
            sumX = sumX + list.get(j).gettingX();
            sumY = sumY + list.get(j).gettingY();

        }
        averageX = sumX / lstSize;
        averageY = sumY / lstSize;
        newPoints.settingX(averageX);
        newPoints.settingY(averageY);
        return newPoints;
    }

    public PointVariable[] NewClusterPosition(int k, MultiValueMap<Integer, PointVariable> clustersList) {
        PointVariable[] newPoints = new PointVariable[k];
        for (int i = 0; i < k; i++) {
            newPoints[i] = calculatingClusterMean(i, clustersList);
        }
        return newPoints;
    }

    public MultiValueMap<Integer, PointVariable> clusterData(int k, List<PointVariable> wifiGeoPoints, int iterations,double err) {
        CentroidsPos = randomCentroids(k);
       
        MultiValueMap<Integer, PointVariable> clustersList = placePointInCentroid(CentroidsPos, wifiGeoPoints);

        System.out.println("Lowest Value of X: " + CSV.highestX);
        System.out.println("Highest Value of X: " + CSV.lowestX);
        System.out.println("Lowest Value of Y: " + CSV.highestY);
        System.out.println("Highest Value of Y: " + CSV.lowestY);

        System.out.println("Total Classes: " + clustersList.size());
        System.out.println("==============================Before K-means:================================= ");
        for (int key : clustersList.keySet()) {

            System.out.println(key + " - [" + clustersList.get(key) + "]");
        }
         while(clustersList.size()!=k){
            System.out.println("Retrying Genrating Centroid Position and clusters");
            CentroidsPos = randomCentroids(k); 
            clustersList = placePointInCentroid(CentroidsPos, wifiGeoPoints);
        }
         oldCentroidsPos=CentroidsPos;
        for (int i = 0; i < iterations; i++) {
            CentroidsPos = NewClusterPosition(k, clustersList);
            System.out.println("Centroid Position:"+CentroidsPos[1].toString());
            clustersList = placePointInCentroid(CentroidsPos, wifiGeoPoints);
            System.out.println("Diff: "+EucledianDistance(oldCentroidsPos[0], CentroidsPos[0]));
            if(EucledianDistance(oldCentroidsPos[0], CentroidsPos[0])==err){
                if(EucledianDistance(oldCentroidsPos[1], CentroidsPos[1])==err){
                    
                    if(EucledianDistance(oldCentroidsPos[2], CentroidsPos[2])==err){
                         System.out.println("Stopped ar Iteration: " + i );
                        break;
                    }
                }
                
            }
            oldCentroidsPos=CentroidsPos;
           

        }
        System.out.println("==============================After K-means:================================= ");
        List<PointVariable> list;
        for (int key : clustersList.keySet()) {
            list=(List)clustersList.get(key);
            System.out.println("Size of Cluster: "+key+" = "+list.size());
            System.out.println(key + " - [" + clustersList.get(key) + "]");
        }
        System.out.println("==============================After K-means Centroid new Positions:================================= ");
        for (int x = 0; x < CentroidsPos.length; x++) {
            System.out.println("[" + CentroidsPos[x].gettingX() + "," + CentroidsPos[x].gettingY() + "]");
        }
        return clustersList;
    }
}
