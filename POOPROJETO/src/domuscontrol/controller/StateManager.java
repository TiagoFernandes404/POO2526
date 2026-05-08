package domuscontrol.controller;

import java.io.*;

// classe responsável por guardar e carregar o estado da aplicação em ficheiro binário
// usamos serialização Java para guardar o DomusController com tudo dentro
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
        // se o ficheiro não existir é a primeira vez que o programa corre
        if (!file.exists())
            return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            DomusController controller = (DomusController) ois.readObject();
            System.out.println("Estado carregado de '" + FILE_NAME + "'.");
            return controller;
        } catch (InvalidClassException e) {
            // isto acontece quando mudamos o serialVersionUID ou a estrutura das classes
            // o ficheiro antigo fica preservado mas não conseguimos carregar
            // private static final long serialVersionUID = 1L; basicamnet qunado mudaremos
            // isto. paramos de aceitar
            // a versao antiga dos ficheiros
            System.out.println("Ficheiro '" + FILE_NAME + "' incompatível com esta versão do programa.");
            System.out.println("Causa: " + e.getMessage());
            System.out.println("O ficheiro antigo foi preservado. A iniciar com estado vazio.");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar estado: " + e.getMessage());
            return null;
        }
    }
}