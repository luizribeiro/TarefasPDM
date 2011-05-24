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
    public void draw(Graphics g) {
        g.setColor(255, 255, 255);
        g.drawLine((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY());
    }
    
    public Vetor getA() {
        return a;
    }
    
    public Vetor getB() {
        return b;
    }
}
