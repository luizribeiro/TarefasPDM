package lancelivre;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

public class LanceLivreMidlet extends MIDlet {
    private LanceLivreCanvas lanceLivreCanvas;
    private Display display;
    
    public void startApp() {
        this.lanceLivreCanvas = new LanceLivreCanvas();
        
        this.display = Display.getDisplay(this);
        this.display.setCurrent(this.lanceLivreCanvas);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}