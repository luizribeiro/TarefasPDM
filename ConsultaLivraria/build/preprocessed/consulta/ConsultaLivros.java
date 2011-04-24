/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package consulta;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.*;

/**
 * @author Caio
 */
public class ConsultaLivros extends MIDlet implements CommandListener{
    private Display tela;
    private List lista;
    private Command sair, listar;

    public ConsultaLivros(){
        tela = Display.getDisplay(this);
        sair = new Command("Sair", Command.EXIT, 0);
        listar = new Command("Listar", Command.OK, 0);

        lista = new List("Livros", Choice.IMPLICIT);
        lista.setCommandListener(this);
        lista.addCommand(sair);
        lista.addCommand(listar);
    }
    public void startApp() {
        tela.setCurrent(lista);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if(c == sair){
            this.notifyDestroyed();
        }
        if(c == listar){
            HttpConnection con = null;
            InputStream is = null;
            StringBuffer sb = new StringBuffer();
            try {
              con = (HttpConnection)Connector.open("http://localhost:8080/ConsultaServlet/ListaServlet");
              con.setRequestMethod(HttpConnection.GET); //default
              is = con.openInputStream(); // transition to connected!
              int ch = 0;
              while(ch != -1) { // get the title.
                ch = is.read();
                if (ch == -1){
                  break;
                }
                sb.append((char)ch);
              }
            }
            catch (IOException x){
                    x.printStackTrace();
            }
            finally{
                 try     {
                   is.close();
                      con.close();
                 } catch (IOException x){
                      x.printStackTrace();
                 }
            }
            System.out.println(sb.toString());
//            try {
//                String url = "http://localhost:8080/ConsultaServlet/ListaServlet";
//                HttpConnection con = (HttpConnection) Connector.open(url);
//                int status = -1;
//
//                status = con.getResponseCode();
//                if(status == HttpConnection.HTTP_OK){
//                    InputStream is = con.openInputStream();
//                    StringBuffer sb = new StringBuffer();
//                    int lido = is.read();
//                    while(lido != -1){
//                        byte b1 = (byte) lido;
//                        sb.append(b1);
//                        System.out.println(sb);
//                        is.read();
//
//                    }
//                }
//            }
//            catch (IOException ex) {
//                ex.printStackTrace();
//            }
        }
    }
}
