package org.owasp.webgoat.container;


import javax.servlet.http.*;
import java.io.*;

public class XSSExample extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Obtener el parámetro 'name' de la solicitud
        String name = request.getParameter("name");

        // Inyección de contenido no filtrado en la respuesta (vulnerabilidad XSS)
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>XSS Example</title></head>");
        out.println("<body>");
        out.println("<h1>Hello, " + name + "!</h1>"); // Inyección de 'name' sin sanitizar
        out.println("</body>");
        out.println("</html>");
    }
}
