package domuscontrol.view;

import java.util.Scanner;

// Classe utilitária para mostrar menus e ler input do utilizador
public class Menu {

    private String title;
    private String[] options;
    private static final Scanner scanner = new Scanner(System.in);

    public Menu(String title, String[] options) {
        this.title = title;
        this.options = options;
    }

    // Mostra o menu e devolve a opção escolhida (1 a N)
    public int show() {
        int choice = -1;
        while (choice < 1 || choice > options.length) {
            System.out.println("\n=== " + title + " ===");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.print("Opção: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < 1 || choice > options.length) {
                    System.out.println("Opção inválida, tenta novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida, tenta novamente.");
            }
        }
        return choice;
    }

    // Lê uma linha de texto do utilizador
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // Lê um inteiro do utilizador
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, tenta novamente.");
            }
        }
    }

    // Lê um número decimal do utilizador
    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, tenta novamente.");
            }
        }
    }

    // Mostra uma mensagem simples ao utilizador
    public static void showMessage(String message) {
        System.out.println(message);
    }
}