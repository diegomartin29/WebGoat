package org.owasp.webgoat.container;
import javax.servlet.http.*;
import java.io.*;
import org.apache.commons.lang3.StringEscapeUtils;

public class XSSExample extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Obtener el parámetro 'name' de la solicitud
        String name = request.getParameter("name");

        // Sanitizar y escapar el parámetro 'name' para prevenir XSS
        if (name != null) {
            // Usar la función de escape de Apache Commons Lang para escapar caracteres peligrosos
            name = StringEscapeUtils.escapeHtml4(name);
        }

        // Intentar obtener el PrintWriter y manejar la excepción IOException
        try {
            // Obtener el PrintWriter para enviar la respuesta
            PrintWriter out = response.getWriter();

            // Configurar la respuesta HTTP
            response.setContentType("text/html");

            // Escribir la respuesta sin reflejar directamente los datos del usuario
            out.println("<html>");
            out.println("<head><title>XSS Mitigation Example</title></head>");
            out.println("<body>");
            out.println("<h1>Hello, user!</h1>"); // Mensaje fijo, no refleja los datos del usuario directamente
            out.println("<p>Your input has been safely processed.</p>"); // Mensaje seguro que no refleja datos del usuario
            out.println("</body>");
            out.println("</html>");

        } catch (IOException e) {
            // Manejar la excepción si ocurre un error al obtener el PrintWriter
            e.printStackTrace();
            // Enviar una respuesta de error al cliente
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the response");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
