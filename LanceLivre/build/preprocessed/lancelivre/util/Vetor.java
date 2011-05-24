package lancelivre.util;

public class Vetor {
    protected double x;
    protected double y;
    
    public Vetor(Vetor v) {
        this.set(v.x, v.y);
    }
    
    public Vetor(double x, double y) {
        this.set(x, y);
    }
    
    public final void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public final double getX() {
        return this.x;
    }
    
    public final double getY() {
        return this.y;
    }
    
    public final String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    public final Vetor add(Vetor v) {
        return new Vetor(x + v.x, y + v.y);
    }
    
    public final Vetor sub(Vetor v) {
        return new Vetor(x - v.x, y - v.y);
    }
    
    public final double abs() {
        return Math.sqrt(x*x + y*y);
    }

    public Vetor mul(double k) {
        return new Vetor(x*k, y*k);
    }
    
    public Vetor norm() {
        return mul(1.0/abs());
    }

    public double dot(Vetor v) {
        return x*v.x + y*v.y;
    }
}