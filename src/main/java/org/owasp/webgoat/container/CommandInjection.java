package org.owasp.webgoat.container;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.nio.file.*;
import java.util.*;

public class SecureFileListingServlet extends HttpServlet {

    // Directorio para listar archivos (por ejemplo, en un directorio seguro)
    private static final String SAFE_DIRECTORY = "/var/www/uploads/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el parámetro 'directory' de la solicitud
        String userInput = request.getParameter("directory");

        // Validación de entrada: Asegurarse de que la entrada solo sea un nombre de directorio y no un comando
        if (userInput != null && !userInput.matches("[a-zA-Z0-9_/]+")) {
            response.getWriter().println("Entrada no válida. Solo se permiten caracteres alfanuméricos, guiones y barras.");
            return;
        }

        // Construir la ruta segura para el directorio a listar
        String directoryPath = SAFE_DIRECTORY + userInput;

        // Validar que el directorio existe
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            response.getWriter().println("El directorio no existe o no es válido.");
            return;
        }

        // Listar los archivos en el directorio (sin ejecutar comandos)
        StringBuilder fileList = new StringBuilder();
        String[] files = directory.list();
        if (files != null && files.length > 0) {
            for (String file : files) {
                fileList.append(file).append("<br>");
            }
            response.getWriter().println("Archivos en el directorio: <br>" + fileList.toString());
        } else {
            response.getWriter().println("El directorio está vacío.");
        }
    }
}


