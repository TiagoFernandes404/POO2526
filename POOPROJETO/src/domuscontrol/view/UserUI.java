package domuscontrol.view;

import domuscontrol.controller.DomusController;
import domuscontrol.model.users.User;

// Interface de utilizador para gestão de utilizadores
public class UserUI {

    private DomusController controller;

    public UserUI(DomusController controller) {
        this.controller = controller;
    }

    // Menu principal de utilizadores
    public void show() {
        Menu menu = new Menu("Gerir Utilizadores", new String[]{
            "Criar utilizador",
            "Listar utilizadores",
            "Ver detalhes de utilizador",
            "Voltar"
        });

        int choice;
        do {
            choice = menu.show();
            switch (choice) {
                case 1 -> createUser();
                case 2 -> listUsers();
                case 3 -> showUserDetails();
                case 4 -> Menu.showMessage("A voltar...");
            }
        } while (choice != 4);
    }

    // Cria um novo utilizador pedindo os dados ao utilizador
    private void createUser() {
        System.out.println("\n--- Criar Utilizador ---");
        String id = Menu.readLine("ID: ");
        if (controller.getUserById(id) != null) {
            Menu.showMessage("Erro: já existe um utilizador com esse ID.");
            return;
        }
        String name = Menu.readLine("Nome: ");
        String email = Menu.readLine("Email: ");
        String password = Menu.readLine("Password: ");

        User user = new User(id, name, email, password);
        controller.addUser(user);
        Menu.showMessage("Utilizador '" + name + "' criado com sucesso!");
    }

    // Lista todos os utilizadores registados
    private void listUsers() {
        System.out.println("\n--- Utilizadores ---");
        var users = controller.getUsers();
        if (users.isEmpty()) {
            Menu.showMessage("Nenhum utilizador registado.");
            return;
        }
        for (User u : users) {
            System.out.println(u);
        }
    }

    // Mostra os detalhes de um utilizador (casas próprias e como convidado)
    private void showUserDetails() {
        System.out.println("\n--- Detalhes de Utilizador ---");
        String id = Menu.readLine("ID do utilizador: ");
        User user = controller.getUserById(id);
        if (user == null) {
            Menu.showMessage("Utilizador não encontrado.");
            return;
        }
        System.out.println("Nome:  " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Casas administradas: " + user.getOwnedHouses().size());
        for (var h : user.getOwnedHouses()) {
            System.out.println("  " + h);
        }
        System.out.println("Casas como convidado: " + user.getGuestHouses().size());
        for (var h : user.getGuestHouses()) {
            System.out.println("  " + h);
        }
    }
}