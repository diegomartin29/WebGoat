package org.owasp.webgoat.container;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class FileInclusionServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el parámetro 'file' de la solicitud
        String fileName = request.getParameter("file");
        
        // Construye la ruta completa al archivo
        String filePath = "/var/www/uploads/" + fileName;
        
        // Abre el archivo para leerlo y escribirlo en la respuesta
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.getWriter().println(line);
                }
                reader.close();
            } else {
                response.getWriter().println("Archivo no encontrado.");
            }
        } catch (IOException e) {
            response.getWriter().println("Error al leer el archivo.");
        }
    }
}


