package org.owasp.webgoat.container;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class IDORVulnerableServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el parámetro 'fileId' de la solicitud
        String fileId = request.getParameter("fileId");
        
        // La vulnerabilidad: no se valida si el usuario tiene permiso para acceder a este archivo
        // Solo usa el fileId para acceder al archivo correspondiente
        File file = new File("/var/www/uploads/" + fileId);
        
        if (file.exists() && file.isFile()) {
            // Lógica para leer el archivo y devolver su contenido (sin validaciones de acceso)
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.getWriter().println(line);
                }
            } catch (IOException e) {
                response.getWriter().println("Error al leer el archivo.");
            }
        } else {
            response.getWriter().println("Archivo no encontrado.");
        }
    }
}
