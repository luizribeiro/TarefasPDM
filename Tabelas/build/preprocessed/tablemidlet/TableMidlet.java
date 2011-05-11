package tablemidlet;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

public class TableMidlet extends MIDlet {
    protected Display display;
    protected TableCanvas tableCanvas;
    protected String[] colNames;
    protected String[][] data;

    public void startApp() {
        colNames = new String[] {
            "Coluna 1", "Coluna 2", "Coluna 3"
        };

        data = new String[][] {
                    {"Android", "Sim", "Não"},
                    {"J2ME", "Sim", "Sim"},
                    {"Windows", "Não", "Não"},
                    {"Android", "Sim", "Não"},
                    {"J2ME", "Sim", "Sim"},
                    {"Windows", "Não", "Não"},
                    {"Android", "Sim", "Não"},
                    {"J2ME", "Sim", "Sim"},
                    {"Windows", "Não", "Não"}
        };

        tableCanvas = new TableCanvas(colNames, data);

        display = Display.getDisplay(this);
        display.setCurrent(tableCanvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
