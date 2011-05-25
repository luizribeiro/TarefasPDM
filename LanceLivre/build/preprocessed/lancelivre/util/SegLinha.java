package lancelivre.util;

import javax.microedition.lcdui.Graphics;

public class SegLinha {
    private Vetor a;
    private Vetor b;
    
    public SegLinha(Vetor a, Vetor b) {
        this.a = a;
        this.b = b;
    }
    
    public Vetor getPontoMaisProximo(Circunf c) {
        Vetor seg = b.sub(a);
        Vetor pt = c.getCentro().sub(a);
        double proj = pt.dot(seg.norm());
        if(proj < -1e-9) return a;
        else if(proj - seg.abs() > 1e-9) return b;
        return a.add(seg.norm().mul(proj));
    }
    
    // usado soh pra debugar
    public void draw(Graphics g, boolean telaDeitada) {
        g.setColor(255, 255, 255);
        if(telaDeitada)
            g.drawLine((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY());
        else
            g.drawLine((int)a.getY(), (int)a.getX(), (int)b.getY(), (int)b.getX());
    }
    
    public Vetor getA() {
        return a;
    }
    
    public Vetor getB() {
        return b;
    }

    public double cruza(Vetor a, Vetor b, Vetor c) {
        return (b.getX() - a.getX())*(c.getY() - a.getY()) - (c.getX() - a.getX())*(b.getY() - a.getY());
    }

    public boolean noSegmento(Vetor i, Vetor j, Vetor k) {
        return Math.min(i.getX(), j.getX()) <= k.getX() && k.getX() <= Math.max(i.getX(), j.getX()) && Math.min(i.getY(), j.getY()) <= k.getY() && k.getY() <= Math.max(i.getY(), j.getY());
    }

    public double direcao(Vetor a, Vetor b, Vetor c) {
        return cruza(a, c, b);
    }

    /* true se os segmentos ab e cd intercedem */
    public boolean intersecta(Vetor c, Vetor d) {
        double d1 = direcao(c, d, this.a);
        double d2 = direcao(c, d, this.b);
        double d3 = direcao(this.a, this.b, c);
        double d4 = direcao(this.a, this.b, d);

        if(((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) return true;
        if(d1 == 0 && noSegmento(c, d, this.a)) return true;
        if(d2 == 0 && noSegmento(c, d, this.b)) return true;
        if(d3 == 0 && noSegmento(this.a, this.b, c)) return true;
        if(d4 == 0 && noSegmento(this.a, this.b, d)) return true;

        return false;
    }
}
