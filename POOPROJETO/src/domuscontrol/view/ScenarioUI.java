package domuscontrol.view;

import java.util.List;

public class ScenarioUI {

    private final Menu menu;
    private final Menu actionMenu;

    public ScenarioUI() {
        this.menu = new Menu("Cenários", new String[] {
                "Criar cenário",
                "Adicionar ação a cenário",
                "Listar cenários",
                "Executar cenário",
                "Voltar"
        });
        this.actionMenu = new Menu("Tipo de Ação", new String[] {
                "Ligar dispositivo",
                "Desligar dispositivo",
                "Definir brilho",
                "Definir volume",
                "Definir abertura",
                "Voltar"
        });
    }

    public int showMenu() {
        return menu.show();
    }

    public int showActionMenu() {
        return actionMenu.show();
    }

    public String[] readScenarioData(List<String> users) {
        System.out.println("\n--- Criar Cenário ---");
        System.out.println("Utilizadores disponíveis:");
        for (String u : users)
            System.out.println("  " + u);
        String name = Menu.readLine("Nome do cenário: ");
        String ownerId = Menu.readLine("ID do utilizador dono: ");
        return new String[] { name, ownerId };
    }

    public String readScenarioName(List<String> scenarios) {
        System.out.println("\nCenários disponíveis:");
        for (String s : scenarios)
            System.out.println("  " + s);
        return Menu.readLine("Nome do cenário: ");
    }

    public String[] readActionData(List<String> devices) {
        System.out.println("\nDispositivos disponíveis:");
        for (String d : devices)
            System.out.println("  " + d);
        String deviceId = Menu.readLine("ID do dispositivo: ");
        String value = Menu.readLine("Valor (Enter para ignorar): ");
        return new String[] { deviceId, value };
    }

    public void displayScenarios(List<String> scenarios) {
        System.out.println("\n--- Cenários ---");
        if (scenarios.isEmpty()) {
            System.out.println("Nenhum cenário registado.");
            return;
        }
        for (String s : scenarios)
            System.out.println(s);
    }

    public void showSuccess(String message) {
        System.out.println("Sucesso:" + message);
    }

    public void showError(String message) {
        System.out.println("Erro: " + message);
    }
}