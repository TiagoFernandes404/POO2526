package domuscontrol.view;

import domuscontrol.model.scenario.Scenario;
import java.util.List;

// View de cenários - só lê input e mostra output
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

    // Mostra o menu e devolve a opção escolhida
    public int showMenu() {
        return menu.show();
    }

    // Mostra o menu de tipos de ação e devolve a opção escolhida
    public int showActionMenu() {
        return actionMenu.show();
    }

    // Lê os dados de um novo cenário
    // [0]=nome, [1]=idDono
    public String[] readScenarioData() {
        System.out.println("\n--- Criar Cenário ---");
        String name = Menu.readLine("Nome do cenário: ");
        String ownerId = Menu.readLine("ID do utilizador dono: ");
        return new String[] { name, ownerId };
    }

    // Lê o nome de um cenário
    public String readScenarioName() {
        return Menu.readLine("Nome do cenário: ");
    }

    // Lê os dados de uma ação para o cenário
    // [0]=idDispositivo, [1]=valor (opcional)
    public String[] readActionData() {
        String deviceId = Menu.readLine("ID do dispositivo: ");
        String value = Menu.readLine("Valor (Enter para ignorar): ");
        return new String[] { deviceId, value };
    }

    // Mostra a lista de cenários
    public void displayScenarios(List<Scenario> scenarios) {
        System.out.println("\n--- Cenários ---");
        if (scenarios.isEmpty()) {
            System.out.println("Nenhum cenário registado.");
            return;
        }
        for (Scenario s : scenarios) {
            System.out.println(s);
        }
    }

    // Mostra mensagem de sucesso
    public void showSuccess(String message) {
        System.out.println("Sucesso:" + message);
    }

    // Mostra mensagem de erro
    public void showError(String message) {
        System.out.println("Erro: " + message);
    }
}