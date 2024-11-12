package org.owasp.webgoat.container;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.nio.file.*;
import java.util.*;

public class SecureFileUploadServlet extends HttpServlet {
    
    // Directorio donde se guardarán los archivos cargados
    private static final String UPLOAD_DIR = "uploads";
    // Tipos de archivo permitidos
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif", "pdf"));
    // Tamaño máximo del archivo en bytes (5MB por ejemplo)
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;  // 5MB

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verifica que la solicitud sea multipart (carga de archivo)
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.getWriter().println("La solicitud no es de tipo multipart/form-data.");
            return;
        }

        // Establecer la ruta completa para los archivos cargados
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

        // Crea el directorio si no existe
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Procesar el archivo cargado
        Part filePart = request.getPart("file"); // Obtiene el archivo del formulario
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Nombre del archivo

        // Validación de tipo de archivo
        if (!isValidFileType(fileName)) {
            response.getWriter().println("Tipo de archivo no permitido.");
            return;
        }

        // Validación del tamaño del archivo
        if (filePart.getSize() > MAX_FILE_SIZE) {
            response.getWriter().println("El archivo es demasiado grande. El tamaño máximo permitido es 5MB.");
            return;
        }

        // Renombrar el archivo para evitar que se sobrescriban archivos existentes
        String newFileName = UUID.randomUUID().toString() + "." + getFileExtension(fileName);
        File file = new File(uploadPath + File.separator + newFileName);

        // Guardar el archivo en el servidor
        try (InputStream inputStream = filePart.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        response.getWriter().println("Archivo cargado con éxito: " + newFileName);
    }

    // Método para validar si el archivo tiene una extensión permitida
    private boolean isValidFileType(String fileName) {
        String fileExtension = getFileExtension(fileName).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(fileExtension);
    }

    // Método para obtener la extensión del archivo
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Este servlet solo acepta solicitudes POST.");
    }
}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Este servlet solo acepta solicitudes POST.");
    }
}

