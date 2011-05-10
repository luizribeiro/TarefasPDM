/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

/**
 * @author Caio
 */
public class TabelaMidlet extends MIDlet {
    public static TabelaMidlet instance = null;

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        // TODO Auto-generated method stub
    }
    protected void pauseApp() {
        // TODO Auto-generated method stub
    }
    protected void startApp() throws MIDletStateChangeException {
        instance = this;
        Display.getDisplay(this).setCurrent(new TabelaCanvas());
    }
}

