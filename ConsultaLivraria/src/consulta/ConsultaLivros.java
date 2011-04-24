/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package consulta;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.midlet.*;

/**
 * @author Caio
 */
public class ConsultaLivros extends MIDlet implements CommandListener, ItemCommandListener{
    private Display tela;
    //private List lista;
    private ChoiceGroup lista;
    private Command sair, listar;
    private Form form;

    public ConsultaLivros(){
        tela = Display.getDisplay(this);
        sair = new Command("Sair", Command.EXIT, 0);
        listar = new Command("Listar", Command.OK, 0);

        lista = new ChoiceGroup("Livros", ChoiceGroup.EXCLUSIVE);

        form = new Form("Livraria");
        form.setCommandListener(this);
        form.addCommand(sair);
        form.addCommand(listar);

        form.append(lista);
    }
    public void startApp() {
        tela.setCurrent(form);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == sair) {
            this.notifyDestroyed();
        }
        if (c == listar) {
            HttpConnection con = null;
            InputStream is = null;
            StringBuffer sb = new StringBuffer();
            try {
                con = (HttpConnection) Connector.open("http://localhost:8080/ConsultaServlet/ListaServlet");
                con.setRequestMethod(HttpConnection.GET); //default
                is = con.openInputStream(); // transition to connected!
                int ch = 0;
                while (ch != -1) { // get the title.
                    ch = is.read();
                    if (ch == -1) {
                        break;
                    }
                    sb.append((char) ch);
                }
            } catch (IOException x) {
                x.printStackTrace();
            } finally {
                try {
                    is.close();
                    con.close();
                } catch (IOException x) {
                    x.printStackTrace();
                }
            }
            lista.deleteAll();
            String entrada;
            String listaCompleta = sb.toString();
            int index0 = 0, index1 = listaCompleta.indexOf("\n");
            while(index1 != -1){
                entrada = listaCompleta.substring(index0, index1);
                lista.append(entrada, null);
                listaCompleta = listaCompleta.substring(index1+1, listaCompleta.length());
                index1 = listaCompleta.indexOf("\n");
            }
        }
    }

    public void commandAction(Command c, Item item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
