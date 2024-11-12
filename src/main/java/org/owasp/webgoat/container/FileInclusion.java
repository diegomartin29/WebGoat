package org.owasp.webgoat.container;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class InsecureFileInclusionExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre del archivo a leer: ");
        String filename = scanner.nextLine();  // Entrada del usuario sin validación

        // Ruta sin sanitización; el usuario controla el archivo que se lee
        String filePath = "/path/to/directory/" + filename;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

