package domuscontrol.view;
import domuscontrol.model.users.User;
import java.util.List;

public class UserUI {

    private final Menu menu;

    public UserUI() {
        this.menu = new Menu("Gerir Utilizadores", new String[]{
            "Criar utilizador",
            "Listar utilizadores",
            "Ver detalhes de utilizador",
            "Promover a administrador",  
            "Voltar"
        });
    }

    public int showMenu() { return menu.show(); }

    public String[] readUserData() {
        System.out.println("\n--- Criar Utilizador ---");
        String id       = Menu.readLine("ID: ");
        String name     = Menu.readLine("Nome: ");
        String email    = Menu.readLine("Email: ");
        String password = Menu.readLine("Password: ");
        return new String[]{id, name, email, password};
    }

    public String readUserId() {
        return Menu.readLine("ID do utilizador: ");
    }

    public void displayUsers(List<User> users) {
        System.out.println("\n--- Utilizadores ---");
        if (users.isEmpty()) { System.out.println("Nenhum utilizador registado."); return; }
        for (User u : users) System.out.println(u);
    }

    public void displayUser(User user) {
        System.out.println("\n--- Detalhes ---");
        System.out.println("ID:    " + user.getId());
        System.out.println("Nome:  " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Casas administradas: " + user.getOwnedHouses().size());
        user.getOwnedHouses().forEach(h -> System.out.println("  " + h));
        System.out.println("Casas como convidado: " + user.getGuestHouses().size());
        user.getGuestHouses().forEach(h -> System.out.println("  " + h));
    }

    public void showSuccess(String message) { System.out.println("✓ " + message); }
    public void showError(String message)   { System.out.println("✗ Erro: " + message); }
}