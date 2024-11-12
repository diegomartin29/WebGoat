package org.owasp.webgoat.container;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SecureFileInclusionServlet extends HttpServlet {
    
    private static final String BASE_DIRECTORY = "/var/www/uploads/"; // Directorio seguro

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el parámetro 'file' de la solicitud
        String fileName = request.getParameter("file");

        // Valida que el nombre del archivo no sea nulo ni vacío
        if (fileName == null || fileName.isEmpty()) {
            response.getWriter().println("Parámetro 'file' no proporcionado.");
            return;
        }
        
        // Valida que el archivo no contenga caracteres peligrosos como '..' (subida de directorios)
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\") || fileName.contains("%")) {
            response.getWriter().println("Ruta de archivo no válida.");
            return;
        }

        // Se crea la ruta completa al archivo, asegurándose de que esté dentro del directorio base
        String filePath = BASE_DIRECTORY + fileName;

        // Verifica que el archivo está dentro del directorio seguro (sin subir a directorios fuera de /uploads)
        File file = new File(filePath);
        if (!file.getCanonicalPath().startsWith(new File(BASE_DIRECTORY).getCanonicalPath())) {
            response.getWriter().println("Acceso denegado: intento de acceso a archivos fuera del directorio permitido.");
            return;
        }

        // Intenta abrir el archivo y leerlo
        try {
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



