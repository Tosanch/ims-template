
public class PointVariable {

    private double pointx;
    private double pointy;

    public PointVariable() {
    }

    public PointVariable(double x, double y) {
        this.pointx = x;
        this.pointy = y;
    }

    public double gettingX() {
        return pointx;
    }

    public void settingX(double x) {
        this.pointx = x;
    }

    public double gettingY() {
        return pointy;
    }

    public void settingY(double y) {
        this.pointy = y;
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + pointx + ", y=" + pointy + '}';
    }

}
