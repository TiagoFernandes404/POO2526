package domuscontrol.view;

import domuscontrol.model.automation.Automation;
import domuscontrol.model.scheduling.Schedule;
import java.util.List;

// View de automações e escalonamentos - só lê input e mostra output
public class AutomationUI {

    private final Menu menu;

    public AutomationUI() {
        this.menu = new Menu("Automações e Escalonamentos", new String[] {
                "Criar automação",
                "Listar automações",
                "Avaliar automações",
                "Criar escalonamento",
                "Listar escalonamentos",
                "Voltar"
        });
    }

    // Mostra o menu e devolve a opção escolhida
    public int showMenu() {
        return menu.show();
    }

    // Lê os dados de uma nova automação
    // [0]=nome, [1]=idSensor, [2]=operador(>,<,=), [3]=threshold
    public String[] readAutomationData() {
        System.out.println("\n--- Criar Automação ---");
        String name = Menu.readLine("Nome da automação: ");
        String sensorId = Menu.readLine("ID do sensor: ");
        System.out.println("Operador: 1-> (maior)  2-< (menor)  3-= (igual)");
        String operator = Menu.readLine("Operador: ");
        String threshold = Menu.readLine("Valor limite: ");
        return new String[] { name, sensorId, operator, threshold };
    }

    // Lê os dados de um novo escalonamento
    // [0]=nome, [1]=hora, [2]=minuto, [3]=repetição(s/n)
    public String[] readScheduleData() {
        System.out.println("\n--- Criar Escalonamento ---");
        String name = Menu.readLine("Nome: ");
        String hour = Menu.readLine("Hora (0-23): ");
        String minute = Menu.readLine("Minuto (0-59): ");
        String repeating = Menu.readLine("Repetir todos os dias? (s/n): ");
        return new String[] { name, hour, minute, repeating };
    }

    // Lê o id de um dispositivo para associar a uma automação/escalonamento
    public String readDeviceId() {
        return Menu.readLine("ID do dispositivo a ligar/desligar: ");
    }

    // Mostra a lista de automações
    public void displayAutomations(List<Automation> automations) {
        System.out.println("\n--- Automações ---");
        if (automations.isEmpty()) {
            System.out.println("Nenhuma automação registada.");
            return;
        }
        for (Automation a : automations) {
            System.out.println(a);
        }
    }

    // Mostra a lista de escalonamentos
    public void displaySchedules(List<Schedule> schedules) {
        System.out.println("\n--- Escalonamentos ---");
        if (schedules.isEmpty()) {
            System.out.println("Nenhum escalonamento registado.");
            return;
        }
        for (Schedule s : schedules) {
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