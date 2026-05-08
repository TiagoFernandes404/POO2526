package domuscontrol.view;

import java.util.List;

public class UserUI {

    private final Menu menu;

    public UserUI() {
        this.menu = new Menu("Gerir Utilizadores", new String[] {
                "Criar utilizador",
                "Listar utilizadores",
                "Ver detalhes de utilizador",
                "Promover a administrador",
                "Voltar"
        });
    }

    public int showMenu() {
        return menu.show();
    }

    public String[] readUserData() {
        System.out.println("\n--- Criar Utilizador ---");
        String id = Menu.readLine("ID: ");
        String name = Menu.readLine("Nome: ");
        String email = Menu.readLine("Email: ");
        String password = Menu.readLine("Password: ");
        return new String[] { id, name, email, password };
    }

    public String readUserId(List<String> users) {
        System.out.println("\nUtilizadores disponíveis:");
        for (String u : users)
            System.out.println("  " + u);
        return Menu.readLine("ID do utilizador: ");
    }

    public void displayUsers(List<String> userInfos) {
        System.out.println("\n--- Utilizadores ---");
        if (userInfos.isEmpty()) {
            System.out.println("Nenhum utilizador registado.");
            return;
        }
        for (int i = 0; i < userInfos.size(); i++)
            System.out.println((i + 1) + ". " + userInfos.get(i));
    }

    public void displayUser(String userInfo) {
        System.out.println("\n--- Detalhes do Utilizador ---");
        System.out.println(userInfo);
    }

    public void showSuccess(String message) {
        System.out.println("Sucesso: " + message);
    }

    public void showError(String message) {
        System.out.println("Erro: " + message);
    }
}