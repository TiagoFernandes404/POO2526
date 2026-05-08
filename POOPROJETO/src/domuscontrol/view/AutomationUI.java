package domuscontrol.view;

import domuscontrol.model.devices.*;
import java.util.List;

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

    public int showMenu() {
        return menu.show();
    }

    public String[] readAutomationData() {
        System.out.println("\n--- Criar Automação ---");
        String name = Menu.readLine("Nome da automação: ");
        String sensorId = Menu.readLine("ID do sensor: ");
        System.out.println("Operador: 1-> (maior)  2-< (menor)  3-= (igual)");
        String operator = Menu.readLine("Operador: ");
        String threshold = Menu.readLine("Valor limite: ");
        return new String[] { name, sensorId, operator, threshold };
    }

    public String[] readScheduleData() {
        System.out.println("\n--- Criar Escalonamento ---");
        String name = Menu.readLine("Nome: ");
        String hour = Menu.readLine("Hora (0-23): ");
        String minute = Menu.readLine("Minuto (0-59): ");
        String repeating = Menu.readLine("Repetir todos os dias? (s/n): ");
        return new String[] { name, hour, minute, repeating };
    }

    public String readDeviceId(List<String> devices) {
        System.out.println("\n--- Dispositivos disponíveis ---");
        for (int i = 0; i < devices.size(); i++)
            System.out.println((i + 1) + ". " + devices.get(i));
        return Menu.readLine("ID do dispositivo: ");
    }

    public int showActionMenu(Device target) {
        boolean hasAperture = target instanceof ApertureDevice;
        boolean hasBrightness = target instanceof Dimmable;
        boolean hasVolume = target instanceof SmartSpeaker;

        System.out.println("\n--- Tipo de Ação ---");
        System.out.println("1. Ligar");
        System.out.println("2. Desligar");
        if (hasAperture)
            System.out.println("3. Definir abertura (%)");
        if (hasBrightness)
            System.out.println("4. Definir brilho (%)");
        if (hasVolume)
            System.out.println("5. Definir volume (%)");

        int choice;
        while (true) {
            choice = Menu.readInt("Opção: ");
            if (choice == 1 || choice == 2)
                return choice;
            if (choice == 3 && hasAperture)
                return choice;
            if (choice == 4 && hasBrightness)
                return choice;
            if (choice == 5 && hasVolume)
                return choice;
            System.out.println("Opção inválida para este dispositivo.");
        }
    }

    public boolean askAddMoreActions() {
        return Menu.readLine("Adicionar mais ações? (s/n): ").equalsIgnoreCase("s");
    }

    public int readOpeningPercentage() {
        return Menu.readInt("Abertura em % (0-100): ");
    }

    public int readBrightness() {
        return Menu.readInt("Brilho (0-100): ");
    }

    public int readVolume() {
        return Menu.readInt("Volume (0-100): ");
    }

    public void displayAutomations(List<String> automations) {
        System.out.println("\n--- Automações ---");
        if (automations.isEmpty()) {
            System.out.println("Nenhuma automação registada.");
            return;
        }
        for (String a : automations)
            System.out.println(a);
    }

    public void displaySchedules(List<String> schedules) {
        System.out.println("\n--- Escalonamentos ---");
        if (schedules.isEmpty()) {
            System.out.println("Nenhum escalonamento registado.");
            return;
        }
        for (String s : schedules)
            System.out.println(s);
    }

    public void showSuccess(String message) {
        System.out.println("Sucesso:" + message);
    }

    public void showError(String message) {
        System.out.println("Erro: " + message);
    }
}