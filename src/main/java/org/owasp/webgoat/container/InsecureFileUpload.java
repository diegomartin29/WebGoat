package org.owasp.webgoat.container;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class InsecureFileUploadExample {

    private static final String UPLOAD_DIRECTORY = "/path/to/upload/directory/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre del archivo a subir: ");
        String fileName = scanner.nextLine();

        System.out.print("Introduce el contenido del archivo: ");
        String fileContent = scanner.nextLine();

        try {
            File file = new File(UPLOAD_DIRECTORY + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(fileContent.getBytes());
            fos.close();

            System.out.println("Archivo subido exitosamente: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al subir el archivo: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
