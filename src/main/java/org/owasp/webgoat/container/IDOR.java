package org.owasp.webgoat.container;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.nio.file.*;

public class SecureIDORServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "/var/www/uploads/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el parámetro 'fileId' de la solicitud
        String fileId = request.getParameter("fileId");

        // Verifica si el parámetro 'fileId' es nulo o vacío
        if (fileId == null || fileId.isEmpty()) {
            sendErrorResponse(response, "ID de archivo no proporcionado.");
            return;
        }

        // Sanitiza el 'fileId' para evitar problemas de seguridad como el traversal de directorios
        fileId = sanitizeFileId(fileId);

        // Verifica si el 'fileId' es válido (solo puede contener caracteres seguros como letras, números y guiones bajos)
        if (!fileId.matches("[a-zA-Z0-9_-]+")) {
            sendErrorResponse(response, "ID de archivo no válido.");
            return;
        }

        // Supongamos que el usuario tiene un "usuarioId" y que cada archivo tiene un "propietarioId"
        String usuarioId = (String) request.getSession().getAttribute("usuarioId");

        // Verifica si el usuario está autenticado
        if (usuarioId == null) {
            sendErrorResponse(response, "Usuario no autenticado.");
            return;
        }

        // Aquí deberíamos comprobar si el usuario tiene acceso al archivo 'fileId'
        String fileOwner = getFileOwnerFromDatabase(fileId);

        if (fileOwner == null || !fileOwner.equals(usuarioId)) {
            sendErrorResponse(response, "Acceso denegado: no tiene permiso para ver este archivo.");
            return;
        }

        // Se crea la ruta completa al archivo
        Path filePath = Paths.get(UPLOAD_DIRECTORY + fileId).normalize();

        // Verifica que el archivo esté dentro del directorio permitido y no esté fuera de la raíz
        if (!filePath.startsWith(UPLOAD_DIRECTORY)) {
            sendErrorResponse(response, "Acceso denegado: intento de acceso fuera del directorio permitido.");
            return;
        }

        // Verifica si el archivo existe y es un archivo regular
        File file = filePath.toFile();
        if (file.exists() && file.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                response.setContentType("text/plain"); // Asegura que la respuesta sea texto plano
                while ((line = reader.readLine()) != null) {
                    sendResponse(response, line);  // Envía el contenido de cada línea
                }
            } catch (IOException e) {
                sendErrorResponse(response, "Error al leer el archivo.");
            }
        } else {
            sendErrorResponse(response, "Archivo no encontrado.");
        }
    }

    // Método simulado para obtener el propietario del archivo desde una base de datos
    private String getFileOwnerFromDatabase(String fileId) {
        // Lógica de base de datos ficticia: en la práctica deberías consultar la base de datos para obtener el propietario
        if (fileId.equals("1234")) {
            return "user1";  // Simulamos que el archivo 1234 pertenece al usuario "user1"
        } else if (fileId.equals("5678")) {
            return "user2";  // Simulamos que el archivo 5678 pertenece al usuario "user2"
        }
        return null;  // Archivo no existe en la base de datos
    }

    // Método para sanitizar el fileId y evitar traversal de directorios
    private String sanitizeFileId(String fileId) {
        // Eliminamos caracteres peligrosos y normalizamos la ruta
        return fileId.replaceAll("[^a-zA-Z0-9_-]", "");
    }

    // Método para enviar una respuesta con un mensaje de error
    private void sendErrorResponse(HttpServletResponse response, String message) {
        try {
            response.setContentType("text/plain");
            response.getWriter().println(message);
        } catch (IOException e) {
            log("Error al escribir la respuesta de error: " + e.getMessage(), e);
        }
    }

    // Método para enviar una respuesta con el contenido del archivo
    private void sendResponse(HttpServletResponse response, String content) {
        try {
            response.getWriter().println(content);
        } catch (IOException e) {
            log("Error al escribir la respuesta de contenido: " + e.getMessage(), e);
        }
    }
}

