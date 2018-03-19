package sample;

/*
Solution derived from DataOnFocus
www.dataonfocus.com
2015
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeans {
    private int clusterNum = 2;

    private List<Point> points;
    private List<Cluster> clusters;

    public KMeans() {
        points = new ArrayList<Point>();
        clusters = new ArrayList<Cluster>();
    }


    public void addPoint(Point p) {
        points.add(p);
    }

    public void setPoints(ArrayList<Point> p) {
        points = p;
    }

    /*
     * Sets the amount of clusters to be used in the K-Means process
     */
    public void setClusterNum(int c) {
        clusterNum = c;
    }

    /*
     * Initialies the process needed to begin the K-Means Clustering Starts by
     * creating random centroids from random points within the list Adds any
     * clusters created to the Cluster list
     */
    public void init() {

        Random r = new Random();
        for (int i = 0; i < clusterNum; i++) {
            Cluster cluster = new Cluster(i);
            Point centroid = points.get(r.nextInt(points.size()));
            cluster.setCentroid(centroid);
            clusters.add(cluster);
        }
        // plotClusters();
    }

    /*
     * Calculates the clustering for the K-Means. Uses private methods to
     * converge the clusters
     */
    public void calculate() {
        boolean finish = false;

        while (!finish) {
            clearClusters();
            List<Point> lastCentroids = getCentroids();
            assignCluster();
            calculateCentroids();
            List<Point> currentCentroids = getCentroids();

            double distance = 0;
            for (int i = 0; i < lastCentroids.size(); i++) {
                distance += Point.distance(lastCentroids.get(i), currentCentroids.get(i));
            }
            if (distance == 0)
                finish = true;
        }
    }

    /*
     * resets all the clusters in the Cluster list
     */
    private void clearClusters() {
        for (Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    /*
     * Returns a list of each Centroid of every cluster
     */
    private List<Point> getCentroids() {
        List<Point> centroids = new ArrayList<Point>(clusterNum);
        for (Cluster cluster : clusters) {
            Point aux = cluster.getCentroid();
            Point point = new Point(aux.getX(), aux.getY());
            centroids.add(point);
        }
        return centroids;
    }

    /*
     * Gives every point in the points list a cluster
     */
    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min = max;
        int cluster = 0;
        double distance = 0.0;

        for (Point point : points) {
            min = max;
            for (int i = 0; i < clusterNum; i++) {
                Cluster c = clusters.get(i);
                distance = Point.distance(point, c.getCentroid());
                if (distance < min) {
                    min = distance;
                    cluster = i;
                }
            }
            point.setCluster(cluster);
            clusters.get(cluster).addPoint(point);
        }
    }

    /*
     * begins converging the clusters and centroids
     */
    private void calculateCentroids() {
        for (Cluster cluster : clusters) {
            double sumX = 0;
            double sumY = 0;
            List<Point> list = cluster.getPoints();
            int points = list.size();

            for (Point p : list) {
                sumX += p.getX();
                sumY += p.getY();
            }

            Point centroid = cluster.getCentroid();
            if (points > 0) {
                double newX = sumX / points;
                double newY = sumY / points;
                centroid.setX(newX);
                centroid.setY(newY);
            }
        }
    }

    /*
     * Used for testing the clusters
     */
    public void plotCluster(int c) {
        clusters.get(c).plotCluster();
        System.out.println(clusters.get(c).points.size());
    }

    /*
     * Find the point that contains the String and returns it
     */
    public Point getPoint(String str) {
        for (Point p : points) {
            if (p.getName().equals(str))
                return p;
        }
        return null;
    }

    /*
     * Puts all the Points of a certain cluster into a String representation
     */
    public String plotClusterPoints(int c) {
        String r = "";
        Cluster clust = clusters.get(c);
        Point cent = clust.getCentroid();
        List<Point> l = clust.points;
        for (Point p : l) {
            if (p.getName().equals(cent.getName()))
                continue;
            r += p.toString();
            r += "\n";
        }
        return r;
    }

    public Point getNearestPoint(Point p){
        double min=Double.MAX_VALUE;
        Point r=null;
        for(Point q: points){
            if(q.toString().equals(p.toString())) continue;
            double d=Point.distance(q, p);
            if(d < min){
                r = q;
                min = d;
                //System.out.println(d);
            }
        }
        return r;
    }
}
