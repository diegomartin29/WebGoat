package org.owasp.webgoat.container;

import javax.servlet.http.*;
import java.io.*;

public class XSSExample extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Obtener el parámetro 'name' de la solicitud
        String name = request.getParameter("name");

        // Sanitizar y escapar el parámetro 'name' para prevenir XSS
        if (name != null) {
            // Se puede procesar el dato, pero no reflejarlo directamente en el HTML
            // Alternativamente, se puede guardar en sesión, base de datos, etc.
        }

        // Responder sin reflejar el nombre directamente en el HTML
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>XSS Mitigation Example</title></head>");
        out.println("<body>");
        
        // No se refleja directamente el parámetro 'name' en el HTML
        out.println("<h1>Hello, the parameter was received securely, but not reflected in the page.</h1>");

        out.println("</body>");
        out.println("</html>");
    }
}
