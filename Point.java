package sample;



public class Point {
    private double x = 0, y = 0;
    private int clusterNum = 0;
    private String name;

    public Point(double a, double b) {
        x = a;
        y = b;
    }

    public Point(double a, double b, String s) {
        x = a;
        y = b;
        name = s;
    }

    /*
     * returns the name associated with the Point
     */
    public String getName() {
        return name;
    }

    /*
     * sets the X coordinate of the Point
     */
    public void setX(double d) {
        x = d;
    }

    /*
     * returns the X coordinate associated with this point
     */
    public double getX() {
        return x;
    }

    /*
     * sets the Y value of the Point
     */
    public void setY(double d) {
        y = d;
    }

    /*
     * returns the Y coordinate associated with the point
     */
    public double getY() {
        return y;
    }

    /*
     * sets the cluster that the point in contained within
     */
    public void setCluster(int n) {
        clusterNum = n;
    }

    /*
     * returns the cluster number the point in within
     */
    public int getCluster() {
        return clusterNum;
    }

    /*
     * calculated the distance between two points
     */
    protected static double distance(Point p, Point centroid) {
        double a = Math.pow((centroid.getY() - p.getY()), 2);
        double b = Math.pow((centroid.getX() - p.getX()), 2);
        return Math.sqrt((a * a) + (b * b));
    }

    /*
     * the two string method that prints out all the information about the point
     *
     */
    public String toString() {
        return "(" + x + "," + y + ")" + " " + name;
    }
}
