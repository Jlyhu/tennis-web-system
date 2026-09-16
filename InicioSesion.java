import java.util.HashMap;
import java.util.Scanner;

public class InicioSesion {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        // Guarda correo y contraseña
        HashMap<String, String> usuarios = new HashMap<>();

        int opcion;

        do {
            System.out.println("\n=========================");
            System.out.println("     SISTEMA DE USUARIOS");
            System.out.println("=========================");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = teclado.nextInt();
            teclado.nextLine(); // Limpiar buffer

            switch (opcion) {

                case 1:
                    System.out.println("\n--- REGISTRO ---");

                    System.out.print("Ingrese su correo: ");
                    String correoRegistro = teclado.nextLine();

                    // Verificar si el correo ya existe
                    if (usuarios.containsKey(correoRegistro)) {
                        System.out.println("Ese correo ya está registrado.");
                    } else {
                        System.out.print("Ingrese una contraseña: ");
                        String contraseñaRegistro = teclado.nextLine();

                        usuarios.put(correoRegistro, contraseñaRegistro);

                        System.out.println("Usuario registrado correctamente.");
                    }

                    break;

                case 2:
                    System.out.println("\n--- INICIAR SESIÓN ---");

                    System.out.print("Ingrese su correo: ");
                    String correoLogin = teclado.nextLine();

                    System.out.print("Ingrese su contraseña: ");
                    String contraseñaLogin = teclado.nextLine();

                    // Comprobar que el correo existe
                    // y que la contraseña corresponde al correo
                    if (usuarios.containsKey(correoLogin)
                            && usuarios.get(correoLogin).equals(contraseñaLogin)) {

                        System.out.println("Inicio de sesión exitoso.");
                        System.out.println("Bienvenido, " + correoLogin);

                    } else {
                        System.out.println("Correo o contraseña incorrectos.");
                    }

                    break;

                case 3:
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 3);

        teclado.close();
    }
}