package org.owasp.webgoat.container;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InsecureCommandInjectionExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre del archivo para listar: ");
        String filename = scanner.nextLine();  // Entrada sin validar

        // Vulnerable: Concatenación directa de la entrada del usuario en el comando
        String command = "ls " + filename;

        try {
            // Ejecuta el comando con la entrada del usuario directamente
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}

