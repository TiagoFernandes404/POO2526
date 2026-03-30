package domuscontrol.view;

// Menu de entrada da aplicação — Login, Registar, Sair
public class MainUI {

    private final Menu menu;

    public MainUI() {
        this.menu = new Menu("DomusControl", new String[]{
            "Login",
            "Registar",
            "Sair"
        });
    }

    // Mostra o menu principal e devolve a opção escolhida
    public int showMenu() {
        return menu.show();
    }

    // Lê as credenciais de login — devolve [0]=email, [1]=password
    public String[] readLoginData() {
        System.out.println("\n--- Login ---");
        String email    = Menu.readLine("Email: ");
        String password = Menu.readLine("Password: ");
        return new String[]{email, password};
    }

    // Lê os dados de registo — devolve [0]=id, [1]=nome, [2]=email, [3]=password
    public String[] readRegisterData() {
        System.out.println("\n--- Registar ---");
        String id       = Menu.readLine("ID: ");
        String name     = Menu.readLine("Nome: ");
        String email    = Menu.readLine("Email: ");
        String password = Menu.readLine("Password: ");
        return new String[]{id, name, email, password};
    }

    // Mostra o menu pós-login e devolve a opção escolhida
    public int showDashboard(String userName, boolean adminMode) {
        String[] options = adminMode
            ? new String[]{
                "Gerir Casas",
                "Gerir Dispositivos",
                "Cenários",
                "Automações e Escalonamentos",
                "Avançar Tempo",
                "Gerir Utilizadores",
                "Estatísticas",
                "Logout"
              }
            : new String[]{
                "Ver Casas",
                "Controlar Dispositivos",
                "Cenários",
                "Avançar Tempo",
                "Tornar-me Administrador",
                "Logout"
              };

        Menu dashboard = new Menu("DomusControl — " + userName, options);
        return dashboard.show();
    }

    // Mostra uma mensagem de despedida
    public void showGoodbye() {
        System.out.println("A sair... até logo!");
    }

    // Mostra mensagem de erro
    public void showError(String message) {
        System.out.println("✗ Erro: " + message);
    }

    // Mostra mensagem de sucesso
    public void showSuccess(String message) {
        System.out.println("✓ " + message);
    }
}