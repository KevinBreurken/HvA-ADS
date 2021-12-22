package route_planner;

public class Road {
    private String name;        // the name of the road segment
    private double length;      // the fysical length of the segment in km
    private int maxSpeed;       // the maximum driving speed on the segment in km/h

    public Road(String name) {
        this.name = name;
    }

    public Road(String name, double length, int maxSpeed) {
        this(name);
        this.length = length;
        this.maxSpeed = maxSpeed;
    }

    public Road(Road copy) {
        this(copy.name, copy.length, copy.maxSpeed);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public String toString() {
        return this.name + "/" + this.maxSpeed;
    }
}
