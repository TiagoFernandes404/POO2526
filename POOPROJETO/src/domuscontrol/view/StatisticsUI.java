package domuscontrol.view;

import java.util.List;

public class StatisticsUI {

    private final Menu menu;

    public StatisticsUI() {
        this.menu = new Menu("Estatísticas", new String[] {
                "Casa que mais consome",
                "Top 3 dispositivos por ativações (global)",
                "Top 3 dispositivos por tempo ligado (global)",
                "Top 3 divisões com mais dispositivos",
                "Consumo total do sistema",
                "Utilizador com mais casas",
                "Top 3 dispositivos por ativações (por casa)",
                "Top 3 dispositivos por tempo ligado (por casa)",
                "Voltar"
        });
    }

    public int showMenu() {
        return menu.show();
    }

    public void displayHighestConsumingHouse(String houseInfo) {
        if (houseInfo == null) {
            Menu.showMessage("Sem casas registadas.");
            return;
        }
        Menu.showMessage("Casa que mais consome: " + houseInfo);
    }

    public void displayTopDevicesByActivations(List<String> devices) {
        if (devices.isEmpty()) {
            Menu.showMessage("Sem dispositivos registados.");
            return;
        }
        Menu.showMessage("Top 3 por ativações:");
        for (int i = 0; i < devices.size(); i++)
            Menu.showMessage((i + 1) + ". " + devices.get(i));
    }

    public void displayTopDevicesByTime(List<String> devices) {
        if (devices.isEmpty()) {
            Menu.showMessage("Sem dispositivos registados.");
            return;
        }
        Menu.showMessage("Top 3 por tempo ligado:");
        for (int i = 0; i < devices.size(); i++)
            Menu.showMessage((i + 1) + ". " + devices.get(i));
    }

    public void displayTopRooms(List<String> rooms) {
        if (rooms.isEmpty()) {
            Menu.showMessage("Sem divisões registadas.");
            return;
        }
        Menu.showMessage("Top 3 divisões com mais dispositivos:");
        for (int i = 0; i < rooms.size(); i++)
            Menu.showMessage((i + 1) + ". " + rooms.get(i));
    }

    public void displayTotalSystemConsumption(double total) {
        Menu.showMessage("Consumo total do sistema: " + total + " Wh");
    }

    public void displayUserWithMostHouses(String userInfo) {
        if (userInfo == null) {
            Menu.showMessage("Sem utilizadores registados.");
            return;
        }
        Menu.showMessage("Utilizador com mais casas: " + userInfo);
    }

    public void displayTopDevicesByActivationsByHouse(String houseName, List<String> devices) {
        if (devices.isEmpty()) {
            Menu.showMessage("Sem dispositivos na casa '" + houseName + "'.");
            return;
        }
        Menu.showMessage("Top 3 por ativações na casa '" + houseName + "':");
        for (int i = 0; i < devices.size(); i++)
            Menu.showMessage((i + 1) + ". " + devices.get(i));
    }

    public void displayTopDevicesByTimeByHouse(String houseName, List<String> devices) {
        if (devices.isEmpty()) {
            Menu.showMessage("Sem dispositivos na casa '" + houseName + "'.");
            return;
        }
        Menu.showMessage("Top 3 por tempo ligado na casa '" + houseName + "':");
        for (int i = 0; i < devices.size(); i++)
            Menu.showMessage((i + 1) + ". " + devices.get(i));
    }

    public String readHouseId(List<String> houses) {
        System.out.println("\nCasas disponíveis:");
        for (String h : houses)
            System.out.println("  " + h);
        return Menu.readLine("ID da casa: ");
    }

    public void showError(String message) {
        Menu.showMessage("Erro: " + message);
    }
}