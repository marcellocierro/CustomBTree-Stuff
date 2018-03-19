package sample;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
    protected List<Point> points;
    protected Point centroid;
    protected int id;

    public Cluster(int d) {
        id = d;
        points = new ArrayList<Point>();
        centroid = null;
    }

    /*
     * returns a list of points containing all the points in the Cluster
     */
    public List<Point> getPoints() {
        return points;
    }

    /*
     * Adds a point into the Cluster
     */
    public void addPoint(Point p) {
        points.add(p);
    }

    /*
     * Retruns the centroid point of the Cluster
     */
    public Point getCentroid() {
        return centroid;
    }

    /*
     * Sets the centroid point of the cluster
     */
    public void setCentroid(Point c) {
        centroid = c;
    }

    /*
     * returns the ID assigned to the cluster
     */
    public int getID() {
        return id;
    }

    /*
     * clears the points list so the cluster contains no points
     */
    public void clear() {
        points.clear();
    }

    /*
     * Used to test what is in the cluster
     */
    public void plotCluster() {
        System.out.println("[Cluster: " + id + "]");
        System.out.println("[Centroid: " + centroid + "]");
        System.out.println("[Points: \n");
        System.out.println("Size: " + points.size());
        for (Point p : points) {
            System.out.println(p);
        }
        System.out.println("]");
    }

}
