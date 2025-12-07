package model;

public class CoordinateEntry {

    private int id;
    private String label;
    private double x;
    private double y;
    private double r;
    private double theta;
    private String timestamp;

    public CoordinateEntry(int id, String label, double x, double y, double r, double theta, String timestamp) {
        this.id = id;
        this.label = label;
        this.x = x;
        this.y = y;
        this.r = r;
        this.theta = theta;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public String getLabel() { return label; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }
    public double getTheta() { return theta; }
    public String getTimestamp() { return timestamp; }
}
