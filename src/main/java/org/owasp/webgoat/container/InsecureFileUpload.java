package org.owasp.webgoat.container;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.nio.file.*;

public class InsecureFileUploadServlet extends HttpServlet {
    
    // Directorio donde se guardarán los archivos cargados
    private static final String UPLOAD_DIR = "uploads";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Establecer la ruta para los archivos cargados
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

        // Crea el directorio si no existe
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Recibe el archivo cargado
        Part filePart = request.getPart("file"); // Obtiene el archivo desde el formulario
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Obtiene el nombre del archivo

        // No hay validación de tipo de archivo ni protección de nombre de archivo
        File file = new File(uploadPath + File.separator + fileName);

        // Guarda el archivo en el servidor
        try (InputStream inputStream = filePart.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        response.getWriter().println("Archivo cargado con éxito: " + fileName);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Este servlet solo acepta solicitudes POST.");
    }
}

