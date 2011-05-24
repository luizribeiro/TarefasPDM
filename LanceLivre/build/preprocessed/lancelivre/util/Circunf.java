package lancelivre.util;

public class Circunf {
    private Vetor c;
    private double r;
    
    public Circunf(Vetor centro, double raio) {
        this.c = centro;
        this.r = raio;
    }
    
    public Vetor getCentro() {
        return c;
    }
    
    public double getRaio() {
        return r;
    }
}