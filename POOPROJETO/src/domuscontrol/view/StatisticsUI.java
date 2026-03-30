package domuscontrol.view;

import domuscontrol.model.devices.Device;
import domuscontrol.model.house.House;
import domuscontrol.model.users.User;
import java.util.List;

public class StatisticsUI {

    private final Menu menu;

    public StatisticsUI() {
        this.menu = new Menu("Estatísticas", new String[]{
            "Casa que mais consome",
            "Top 3 dispositivos por ativações",
            "Top 3 dispositivos por tempo ligado",
            "Top 3 divisões com mais dispositivos",
            "Consumo total do sistema",
            "Utilizador com mais casas",
            "Voltar"
        });
    }

    public int showMenu() {
        return menu.show();
    }

    public void displayHighestConsumingHouse(House house) {
        if (house == null) {
            Menu.showMessage("Sem casas registadas.");
            return;
        }
        Menu.showMessage("Casa que mais consome: " + house.getName()
                + " — " + house.getTotalPowerConsumption() + " Wh");
    }

    public void displayTopDevicesByActivations(List<Device> devices) {
        if (devices.isEmpty()) {
            Menu.showMessage("Sem dispositivos registados.");
            return;
        }
        Menu.showMessage("Top 3 por ativações:");
        for (int i = 0; i < devices.size(); i++) {
            Device d = devices.get(i);
            Menu.showMessage((i + 1) + ". " + d.getId() + " — " + d.getActivationCount() + " ativações");
        }
    }

    public void displayTopDevicesByTime(List<Device> devices) {
        if (devices.isEmpty()) {
            Menu.showMessage("Sem dispositivos registados.");
            return;
        }
        Menu.showMessage("Top 3 por tempo ligado:");
        for (int i = 0; i < devices.size(); i++) {
            Device d = devices.get(i);
            Menu.showMessage((i + 1) + ". " + d.getId() + " — " + d.getTotalOnTime() + " minutos");
        }
    }

    public void displayTopRooms(List<String> rooms) {
        if (rooms.isEmpty()) {
            Menu.showMessage("Sem divisões registadas.");
            return;
        }
        Menu.showMessage("Top 3 divisões com mais dispositivos:");
        for (int i = 0; i < rooms.size(); i++) {
            Menu.showMessage((i + 1) + ". " + rooms.get(i));
        }
    }

    public void displayTotalSystemConsumption(double total) {
        Menu.showMessage("Consumo total do sistema: " + total + " Wh");
    }

    public void displayUserWithMostHouses(User user) {
        if (user == null) {
            Menu.showMessage("Sem utilizadores registados.");
            return;
        }
        int total = user.getAllHouses().size();
        int owned = user.getOwnedHouses().size();
        int guest = user.getGuestHouses().size();
        Menu.showMessage("Utilizador com mais casas: " + user.getName()
                + " — " + total + " casa(s) (" + owned + " própria(s), " + guest + " usufrutuária(s))");
    }
}