package org.owasp.webgoat.container;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class SecureFileInclusionServlet extends HttpServlet {
    
    private static final String BASE_DIRECTORY = "/var/www/uploads/"; // Directorio seguro

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el parámetro 'file' de la solicitud
        String fileName = request.getParameter("file");

        // Valida que el nombre del archivo no sea nulo ni vacío
        if (fileName == null || fileName.isEmpty()) {
            sendErrorResponse(response, "Parámetro 'file' no proporcionado.");
            return;
        }
        
        // Valida que el archivo no contenga caracteres peligrosos como '..' (subida de directorios)
        if (!isValidFileName(fileName)) {
            sendErrorResponse(response, "Ruta de archivo no válida.");
            return;
        }

        // Se crea la ruta completa al archivo, asegurándose de que esté dentro del directorio base
        Path filePath = Paths.get(BASE_DIRECTORY, fileName).normalize();

        // Verifica que el archivo está dentro del directorio seguro (sin subir a directorios fuera de /uploads)
        if (!filePath.startsWith(BASE_DIRECTORY)) {
            sendErrorResponse(response, "Acceso denegado: intento de acceso a archivos fuera del directorio permitido.");
            return;
        }

        // Verifica si el archivo existe y es un archivo regular
        File file = filePath.toFile();
        if (!file.exists() || !file.isFile()) {
            sendErrorResponse(response, "Archivo no encontrado o no es un archivo válido.");
            return;
        }

        // Intenta abrir el archivo y leerlo
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            response.setContentType("text/plain"); // Asegura que la respuesta sea texto plano
            String line;
            while ((line = reader.readLine()) != null) {
                sendResponse(response, line);  // Envía el contenido de cada línea
            }
        } catch (IOException e) {
            sendErrorResponse(response, "Error al leer el archivo.");
        }
    }

    // Método para validar el nombre del archivo (permitir solo caracteres seguros)
    private boolean isValidFileName(String fileName) {
        // Solo permite letras, números, guion bajo y guion medio (sin caracteres peligrosos)
        return fileName.matches("^[a-zA-Z0-9_-]+$");
    }

    // Método para enviar una respuesta con un mensaje de error
    private void sendErrorResponse(HttpServletResponse response, String message) {
        try {
            response.setContentType("text/plain");
            response.getWriter().println(message);
        } catch (IOException e) {
            e.printStackTrace(); // Si no se puede escribir la respuesta, registrar el error
        }
    }

    // Método para enviar una respuesta con el contenido del archivo
    private void sendResponse(HttpServletResponse response, String content) {
        try {
            response.getWriter().println(content);
        } catch (IOException e) {
            e.printStackTrace(); // Si no se puede escribir la respuesta, registrar el error
        }
    }
}


