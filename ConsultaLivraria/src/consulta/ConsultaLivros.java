/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package consulta;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.*;

/**
 * @author Caio
 */
public class ConsultaLivros extends MIDlet implements CommandListener, ItemCommandListener{
    private Display tela;
    private List resultado;
    private List lista;
    private Command sair, listar, detalhar, voltar;
    private int livroId;
    private Connection conServlet;
    private String url;
    private StringBuffer sb;

    public ConsultaLivros(){
        conServlet = new Connection();

        tela = Display.getDisplay(this);
        sair = new Command("Sair", Command.EXIT, 0);
        listar = new Command("Listar", Command.OK, 0);
        detalhar = new Command("Detalhes", Command.OK, 0);
        voltar = new Command("Voltar", Command.BACK, 0);

        resultado = new List("Detalhes do Livro", List.IMPLICIT);
        resultado.setCommandListener(this);
        resultado.addCommand(voltar);

        lista = new List("Livros", List.IMPLICIT);
        lista.setCommandListener(this);
        lista.addCommand(detalhar);
        lista.addCommand(sair);
    }
    public void startApp() {
        tela.setCurrent(lista);
        // Preparar URL
        url = "http://localhost:8080/ConsultaServlet/ListaServlet";

        // Conecta, consulta e retorna resultados do servlet
        sb = conServlet.connect(url);

        // Lista a ser mostrada para o usuario
        lista.deleteAll();
        String listaCompleta = sb.toString(); // lista auxiliar, sb permanece com todos os resultados
        int index = listaCompleta.indexOf("\n");
        while(index != -1){
            lista.append(listaCompleta.substring(0, index), null);
            listaCompleta = listaCompleta.substring(index+1, listaCompleta.length());
            index = listaCompleta.indexOf("\n");
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == sair) {
            this.notifyDestroyed();
        }

        if(c == voltar){
            // Retorna da tela de resultado
            tela.setCurrent(lista);
        }

        if(c == lista.SELECT_COMMAND || c == detalhar) {
            // Pegar o livro_id do item selecionado
            //// fazer 'split' do primeiro ' ', fazer trim() e pegar o numero
            String selecao = lista.getString(lista.getSelectedIndex());
            selecao = selecao.substring(0, selecao.indexOf(" ")).trim();
            livroId = Integer.parseInt(selecao);

            // Realizar consulta no Servlet
            url = "http://localhost:8080/ConsultaServlet/DetalhesServlet?livro_id="+livroId;
            sb = conServlet.connect(url);

            // Dispobilizar resultado
            resultado.deleteAll();
            resultado.append(sb.toString(), null);
            tela.setCurrent(resultado);
        }
    }

    public void commandAction(Command c, Item item) {
        if(c == detalhar){
            System.out.println("oi");
            // Pegar o livro_id do item selecionado
            //// fazer 'split' do primeiro ' ', fazer trim() e pegar o numero
            String selecao = lista.getString(lista.getSelectedIndex());
            selecao = selecao.substring(0, selecao.indexOf(" ")).trim();
            livroId = Integer.parseInt(selecao);

            // Realizar consulta no Servlet
            url = "http://localhost:8080/ConsultaServlet/DetalhesServlet?livro_id="+livroId;
            sb = conServlet.connect(url);

            // Dispobilizar resultado
            resultado.deleteAll();
            resultado.append(sb.toString(), null);
            tela.setCurrent(resultado);
        }
    }
}