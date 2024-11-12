package org.owasp.webgoat.container;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InsecureIDORExample {

    // Simulación de base de datos de usuarios con información sensible
    private static Map<String, String> userDatabase = new HashMap<>();

    static {
        userDatabase.put("1001", "Cuenta de usuario 1001: Información privada.");
        userDatabase.put("1002", "Cuenta de usuario 1002: Información privada.");
        userDatabase.put("1003", "Cuenta de usuario 1003: Información privada.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Simulación de sesión del usuario logueado (sin control de permisos)
        System.out.print("Introduce el ID de usuario para ver la información de la cuenta: ");
        String requestedUserId = scanner.nextLine();

        // IDOR sin verificación de permisos: cualquier usuario puede acceder a cualquier ID de usuario
        if (userDatabase.containsKey(requestedUserId)) {
            System.out.println("Información de la cuenta: " + userDatabase.get(requestedUserId));
        } else {
            System.out.println("Usuario no encontrado.");
        }

        scanner.close();
    }
}
