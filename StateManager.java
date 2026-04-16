package domuscontrol.controller;

import java.io.*;

public class StateManager {

    private static final String FILE_NAME = "domuscontrol.dat";

    public static void save(DomusController controller) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(controller);
            System.out.println("Estado guardado em '" + FILE_NAME + "'.");
        } catch (IOException e) {
            System.out.println("Erro ao guardar: " + e.getMessage());
        }
    }

    public static DomusController load() {
        File file = new File(FILE_NAME);
        if (!file.exists())
            return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            DomusController controller = (DomusController) ois.readObject();
            System.out.println("Estado carregado de '" + FILE_NAME + "'.");
            return controller;
        } catch (InvalidClassException e) {
            // Versão do ficheiro incompatível com o código atual
            System.out.println("Ficheiro '" + FILE_NAME + "' incompatível com esta versão do programa.");
            System.out.println("Causa: " + e.getMessage());
            System.out.println("O ficheiro antigo foi preservado. A iniciar com estado vazio.");
            // NÃO apaga o ficheiro — o utilizador pode recuperá-lo manualmente
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar estado: " + e.getMessage());
            return null;
        }
    }
}