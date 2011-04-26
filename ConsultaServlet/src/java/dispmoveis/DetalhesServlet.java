package dispmoveis;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author luiz
 */
public class DetalhesServlet extends HttpServlet {
    protected static final String DB_URL = "jdbc:postgresql://localhost/livraria";
    protected static final String DB_USERNAME = "postgres";
    protected static final String DB_PASSWORD = "1234";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            // obtem o livro_id passado por GET
            int livroId = Integer.parseInt(request.getParameter("livro_id"));

            // carrega o driver JDBC
            Class.forName("org.postgresql.Driver");

            // estabelece conexao
            Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // cria um statement
            Statement stm = con.createStatement();

            // executa e percorre a consulta
            ResultSet rs = stm.executeQuery("SELECT livro_id, titulo, autor, editora, ano, descricao, preco, estoque, reserva FROM livros WHERE livro_id="+livroId);
            if(rs.next()) {
                out.println("livro_id " + rs.getString(1));
                out.println("titulo " + rs.getString(2));
                out.println("autor " + rs.getString(3));
                out.println("editora " + rs.getString(4));
                out.println("ano " + rs.getString(5));
                out.println("descricao " + rs.getString(6));
                out.println("preco " + rs.getString(7));
                out.println("estoque " + rs.getString(8));
                out.println("reserva " + rs.getString(9));
            }

            // fecha a conexao
            con.close();

        } catch(Exception ex) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            ex.printStackTrace(out);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}