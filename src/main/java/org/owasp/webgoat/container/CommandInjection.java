package org.owasp.webgoat.container;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CommandInjectionServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el parámetro 'command' de la solicitud
        String userInput = request.getParameter("command");
        
        // Ejecuta el comando recibido del usuario
        try {
            // Aquí está la vulnerabilidad: ejecutar un comando del sistema operativo sin validación
            Process process = Runtime.getRuntime().exec(userInput);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.getWriter().println(line);
            }
        } catch (IOException e) {
            response.getWriter().println("Error al ejecutar el comando.");
        }
    }
}


