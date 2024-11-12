package org.owasp.webgoat.container;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.nio.file.*;

public class SecureCommandServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene el parámetro 'command' de la solicitud
        String userInput = request.getParameter("command");
        
        // Validación estricta de la entrada: solo permitir comandos específicos
        if (userInput == null || !isValidCommand(userInput)) {
            try {
                response.getWriter().println("Comando no válido.");
            } catch (IOException e) {
                handleIOException(response, e);
            }
            return;
        }

        // En lugar de ejecutar un comando, realizamos una acción segura
        try {
            // Si la entrada es válida, ejecutamos alguna operación específica
            // Ejemplo: listar archivos en un directorio específico
            if (userInput.equals("listFiles")) {
                // Listar archivos en un directorio seguro
                File dir = new File("/path/to/safe/directory");
                StringBuilder fileList = new StringBuilder();
                for (File file : dir.listFiles()) {
                    fileList.append(file.getName()).append("<br>");
                }
                try {
                    response.getWriter().println(fileList.toString());
                } catch (IOException e) {
                    handleIOException(response, e);
                }
            } else {
                try {
                    response.getWriter().println("Comando desconocido.");
                } catch (IOException e) {
                    handleIOException(response, e);
                }
            }
        } catch (Exception e) {
            try {
                response.getWriter().println("Error al ejecutar la acción.");
            } catch (IOException ioe) {
                handleIOException(response, ioe);
            }
        }
    }
    
    // Método para validar los comandos permitidos
    private boolean isValidCommand(String input) {
        // Permitimos solo un conjunto de comandos específicos que no son peligrosos
        return "listFiles".equals(input); // Ejemplo de comando permitido
    }

    // Método para manejar la excepción IOException y enviar una respuesta adecuada
    private void handleIOException(HttpServletResponse response, IOException e) {
        try {
            response.getWriter().println("Error al escribir en la respuesta.");
        } catch (IOException ioe) {
            // Si no se puede escribir en la respuesta, no podemos hacer mucho más
            ioe.printStackTrace();  // Registrar el error internamente
        }
    }
}




