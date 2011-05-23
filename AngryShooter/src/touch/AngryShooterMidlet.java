/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package touch;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

/**
 * @author Caio
 */
public class AngryShooterMidlet extends MIDlet {
    private AngryShooterCanvas as;
    private Display screen;
    
    public AngryShooterMidlet(){
        screen = Display.getDisplay(this);
    }
    public void startApp() {
        as = new AngryShooterCanvas();
        screen.setCurrent(as);
        as.start();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
