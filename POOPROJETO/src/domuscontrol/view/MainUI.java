package domuscontrol.view;

public class MainUI {
    private final Menu menu;

    public MainUI() {
        this.menu = new Menu("DomusControl", new String[] {
                "Login",
                "Registar",

                "Sair"
        });
    }

    public int showMenu() {
        return menu.show();
    }

    public String[] readLoginData() {
        System.out.println("\n--- Login ---");
        String email = Menu.readLine("Email: ");
        String password = Menu.readLine("Password: ");
        return new String[] { email, password };
    }

    public String[] readRegisterData() {
        System.out.println("\n--- Registar ---");
        String id = Menu.readLine("ID: ");
        String name = Menu.readLine("Nome: ");
        String email = Menu.readLine("Email: ");
        String password = Menu.readLine("Password: ");
        return new String[] { id, name, email, password };
    }

    public boolean askPromoteToAdmin() {
        System.out.println("\n--- Deseja que o utilizador registado seja Admin? ---");
        System.out.println("1 - Sim");
        System.out.println("2 - Não");

        int escolha;
        do {
            escolha = Menu.readInt("Escolha (1/2): ");
        } while (escolha != 1 && escolha != 2);

        return escolha == 1;
    }

    public int showDashboard(String userName, boolean adminMode) {
        String[] options = adminMode
                ? new String[] {
                        "Gerir Casas", "Gerir Dispositivos", "Cenários",
                        "Automações e Escalonamentos", "Avançar Tempo",
                        "Gerir Utilizadores", "Estatísticas", "Logout"
                }
                : new String[] {
                        "Ver Casas", "Controlar Dispositivos",
                        "Cenários", "Avançar Tempo", "Logout"
                };
        return new Menu("DomusControl — " + userName, options).show();
    }

    public void showGoodbye() {
        System.out.println("A sair... até logo!");
    }

    public void showError(String message) {
        System.out.println("Erro: " + message);
    }

    public void showSuccess(String message) {
        System.out.println("Sucesso:" + message);
    }
}