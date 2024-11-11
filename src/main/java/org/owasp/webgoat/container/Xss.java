package org.owasp.webgoat.container;

import javax.servlet.http.*;
import java.io.*;
import org.apache.commons.lang3.StringEscapeUtils;

public class XSSExample extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Obtener el parámetro 'name' de la solicitud
        String name = request.getParameter("name");

        // Sanitizar y escapar el parámetro 'name' para prevenir XSS
        if (name != null) {
            // Usar la función de escape de Apache Commons Lang para escapar caracteres peligrosos
            name = StringEscapeUtils.escapeHtml4(name);
        }

        // Responder con el contenido de forma segura (sin ejecutar JavaScript)
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>XSS Mitigation Example</title></head>");
        out.println("<body>");
        out.println("<h1>Hello, " + name + "!</h1>"); // Ahora el parámetro 'name' está escapado
        out.println("</body>");
        out.println("</html>");
    }
}

