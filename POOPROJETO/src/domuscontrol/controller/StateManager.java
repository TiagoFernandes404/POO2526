package domuscontrol.controller;
import java.io.*;

// Responsável por guardar e carregar o estado do DomusController em ficheiro binário
public class StateManager {

    // Nome fixo do ficheiro de estado
    private static final String FILE_NAME = "domuscontrol.dat";

    // Guarda o estado atual do controller no ficheiro
    public static void save(DomusController controller) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(controller);
            System.out.println("✓ Estado guardado em '" + FILE_NAME + "'.");
        } catch (IOException e) {
            System.out.println("✗ Erro ao guardar: " + e.getMessage());
        }
    }

    // Carrega o estado a partir do ficheiro; devolve null se o ficheiro não existir
    public static DomusController load() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            DomusController controller = (DomusController) ois.readObject();
            System.out.println("✓ Estado carregado de '" + FILE_NAME + "'.");
            return controller;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("✗ Erro ao carregar: " + e.getMessage());
            return null;
        }
    }
}