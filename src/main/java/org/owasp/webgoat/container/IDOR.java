package org.owasp.webgoat.container;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InsecureIDORExample {

    // Simulación de base de datos de usuarios
    private static Map<String, String> userDatabase = new HashMap<>();

    static {
        userDatabase.put("1001", "Cuenta de usuario 1001: Información confidencial.");
        userDatabase.put("1002", "Cuenta de usuario 1002: Información confidencial.");
        userDatabase.put("1003", "Cuenta de usuario 1003: Información confidencial.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Simulación de la sesión del usuario (userID 1001 está logueado)
        String loggedInUserId = "1001";
        
        System.out.print("Introduce el ID de usuario para ver la información de la cuenta: ");
        String requestedUserId = scanner.nextLine();

        // Vulnerabilidad IDOR: Permite acceder a cualquier cuenta sin verificación de permisos
        if (userDatabase.containsKey(requestedUserId)) {
            System.out.println("Información de la cuenta: " + userDatabase.get(requestedUserId));
        } else {
            System.out.println("Usuario no encontrado.");
        }

        scanner.close();
    }
}
