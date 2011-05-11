/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Caio
 */

public class TabelaCanvas extends Canvas{
    private ScrollableTable table = null;

    public TabelaCanvas() {
        this.table = new ScrollableTable(getData(), getWidth() - 20, getHeight() - 20);
    }

    public String[][] getData() {
        return new String[][]{
                    new String[]{"AAAAAA AAAAAA AAAAAA AAAAAA AAAAAA", "BBBBBB BBBBBB BBBBBB BBBBBB"},
                    new String[]{"CCCCCC CCCCCC CCCCCC CCCCCC CCCCCC", "DDDDDD DDDDDD DDDDDD DDDDDD"},
                    new String[]{"EEEEEE EEEEEE EEEEEE EEEEEE EEEEEE", "FFFFFF FFFFFF FFFFFF FFFFFF"},
                    new String[]{"GGGGGG GGGGGG GGGGGG GGGGGG GGGGGG", "HHHHHH HHHHHH HHHHHH HHHHHH"},
                    new String[]{"IIIIII IIIIII IIIIII IIIIII IIIIII", "JJJJJJ JJJJJJ JJJJJJ JJJJJJ"},
                    new String[]{"KKKKKK KKKKKK KKKKKK KKKKKK KKKKKK", "LLLLLL LLLLLL LLLLLL LLLLLL"}
                };
    }

    protected void keyPressed(int key) {
        int keyCode = getGameAction(key);
        if (keyCode == FIRE) {
            Display.getDisplay(TabelaMidlet.instance).setCurrent(new Alert("Selected", table.getSelectedData(), null, AlertType.INFO));
        } else {
            table.keyPressed(keyCode);
            repaint();
        }
    }

    protected void paint(Graphics g) {
        g.setColor(0xffffff);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.translate(10, 10);
        table.paint(g);
        g.translate(-10, -10);
    }
}
