package domuscontrol.controller;

import domuscontrol.model.devices.Device;
import domuscontrol.model.house.House;
import domuscontrol.model.users.User;
import domuscontrol.view.StatisticsUI;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerStatistics {

    private final DomusController model;
    private final StatisticsUI statisticsUI;
    private final ControllerTotal main;

    public ControllerStatistics(DomusController model, StatisticsUI statisticsUI, ControllerTotal main) {
        this.model = model;
        this.statisticsUI = statisticsUI;
        this.main = main;
    }

    private List<String> houseList() {
        return model.getHouses().stream()
                .map(h -> "[" + h.getId() + "] " + h.getName()).toList();
    }

    public void handle() {
        int choice;
        do {
            choice = statisticsUI.showMenu();
            switch (choice) {
                case 1 -> {
                    House house = model.getHighestConsumingHouse();
                    statisticsUI.displayHighestConsumingHouse(
                            house == null ? null : house.getName() + " — " + house.getTotalPowerConsumption() + " Wh");
                }
                case 2 -> {
                    List<Device> devs = model.getTopDevicesByActivations(3);
                    statisticsUI.displayTopDevicesByActivations(
                            devs.stream().map(d -> d.getId() + " — " + d.getActivationCount() + " ativações")
                                    .collect(Collectors.toList()));
                }
                case 3 -> {
                    int t = main.currentTotalMinutes();
                    List<Device> devs = model.getTopDevicesByTime(3, t);
                    statisticsUI.displayTopDevicesByTime(
                            devs.stream().map(d -> d.getId() + " — " + d.getEffectiveTotalOnTime(t) + " minutos")
                                    .collect(Collectors.toList()));
                }
                case 4 -> statisticsUI.displayTopRooms(model.getTopRoomsByDeviceCount(3));
                case 5 -> statisticsUI.displayTotalSystemConsumption(model.getTotalSystemConsumption());
                case 6 -> {
                    User user = model.getUserWithMostHouses();
                    if (user == null) {
                        statisticsUI.displayUserWithMostHouses(null);
                        break;
                    }
                    statisticsUI.displayUserWithMostHouses(user.getName() + " — " + user.getAllHouses().size()
                            + " casa(s) (" + user.getOwnedHouses().size() + " própria(s), "
                            + user.getGuestHouses().size() + " usufrutuária(s))");
                }
                case 7 -> {
                    String houseId = statisticsUI.readHouseId(houseList());
                    House house = model.getHouseById(houseId);
                    if (house == null) {
                        statisticsUI.showError("Casa não encontrada.");
                        break;
                    }
                    int t = main.currentTotalMinutes();
                    statisticsUI.displayTopDevicesByActivationsByHouse(house.getName(),
                            model.getTopDevicesByActivationsByHouse(house, 3, t).stream()
                                    .map(d -> d.getId() + " — " + d.getActivationCount() + " ativações")
                                    .collect(Collectors.toList()));
                }
                case 8 -> {
                    String houseId = statisticsUI.readHouseId(houseList());
                    House house = model.getHouseById(houseId);
                    if (house == null) {
                        statisticsUI.showError("Casa não encontrada.");
                        break;
                    }
                    int t = main.currentTotalMinutes();
                    statisticsUI.displayTopDevicesByTimeByHouse(house.getName(),
                            model.getTopDevicesByTimeByHouse(house, 3, t).stream()
                                    .map(d -> d.getId() + " — " + d.getEffectiveTotalOnTime(t) + " minutos")
                                    .collect(Collectors.toList()));
                }
            }
        } while (choice != 9);
    }
}