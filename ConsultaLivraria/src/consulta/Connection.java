/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package consulta;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author Caio
 */

public class Connection {
    private HttpConnection con;
    private InputStream is;
    private StringBuffer sb;

    public Connection(){

    }

    public StringBuffer connect(String url) {
        con = null;
        is = null;
        sb = new StringBuffer();
        try {
            con = (HttpConnection) Connector.open(url);
            con.setRequestMethod(HttpConnection.GET);
            is = con.openInputStream();
            int ch = 0;
            while (ch != -1) {
                ch = is.read();
                if (ch == -1) {
                    break;
                }
                sb.append((char) ch);
            }
        } catch (IOException x) {
        } finally {
            try {
                is.close();
                con.close();
            } catch (IOException x) {
            }
        }
        return sb;
    }
}

