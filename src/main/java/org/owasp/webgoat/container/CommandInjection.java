package org.owasp.webgoat.container;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.regex.*;

public class SecureFileListingServlet extends HttpServlet {

    // Directorio para listar archivos (por ejemplo, en un directorio seguro)
    private static final String SAFE_DIRECTORY = "/var/www/uploads/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el parámetro 'directory' de la solicitud de manera segura
        String userInput = request.getParameter("directory");

        // Validación de entrada: Asegurarse de que la entrada solo sea un nombre de directorio seguro
        if (userInput == null || !isValidDirectoryName(userInput)) {
            response.getWriter().println("Entrada no válida. Solo se permiten caracteres alfanuméricos, guiones y barras.");
            return;
        }

        // Construir la ruta completa y segura para el directorio a listar
        Path directoryPath = Paths.get(SAFE_DIRECTORY, userInput).normalize();

        // Validar que la ruta esté dentro del directorio seguro y que no se haya hecho un path traversal
        if (!directoryPath.startsWith(SAFE_DIRECTORY)) {
            response.getWriter().println("Acceso denegado. Ruta no permitida.");
            return;
        }

        // Verificar si el directorio existe
        if (!Files.exists(directoryPath) || !Files.isDirectory(directoryPath)) {
            response.getWriter().println("El directorio no existe o no es válido.");
            return;
        }

        // Listar los archivos de manera segura
        try {
            StringBuilder fileList = new StringBuilder();
            Files.list(directoryPath).forEach(file -> {
                fileList.append(file.getFileName()).append("<br>");
            });

            if (fileList.length() == 0) {
                response.getWriter().println("El directorio está vacío.");
            } else {
                response.getWriter().println("Archivos en el directorio: <br>" + fileList.toString());
            }
        } catch (IOException e) {
            response.getWriter().println("Error al leer el directorio.");
        }
    }

    // Método para validar que el nombre del directorio solo contenga caracteres permitidos
    private boolean isValidDirectoryName(String input) {
        // Expresión regular para solo permitir caracteres alfanuméricos, guiones, y barras
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_/]+$");
        return pattern.matcher(input).matches();
    }
}



