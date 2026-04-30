package domuscontrol.controller;

import domuscontrol.model.house.House;
import domuscontrol.view.StatisticsUI;

public class ControllerStatistics {

    private final DomusController model;
    private final StatisticsUI statisticsUI;
    private final ControllerTotal main;

    public ControllerStatistics(DomusController model, StatisticsUI statisticsUI, ControllerTotal main) {
        this.model = model;
        this.statisticsUI = statisticsUI;
        this.main = main;
    }

    public void handle() {
        int choice;
        do {
            choice = statisticsUI.showMenu();
            switch (choice) {
                case 1 -> statisticsUI.displayHighestConsumingHouse(
                        model.getHighestConsumingHouse());
                case 2 -> statisticsUI.displayTopDevicesByActivations(
                        model.getTopDevicesByActivations(3));
                case 3 -> statisticsUI.displayTopDevicesByTime(
                        model.getTopDevicesByTime(3, main.currentTotalMinutes()),
                        main.currentTotalMinutes());
                case 4 -> statisticsUI.displayTopRooms(
                        model.getTopRoomsByDeviceCount(3));
                case 5 -> statisticsUI.displayTotalSystemConsumption(
                        model.getTotalSystemConsumption());
                case 6 -> statisticsUI.displayUserWithMostHouses(
                        model.getUserWithMostHouses());
                case 7 -> {
                    String houseId = statisticsUI.readHouseId();
                    House house = model.getHouseById(houseId);
                    if (house == null) {
                        statisticsUI.showError("Casa não encontrada.");
                        break;
                    }
                    statisticsUI.displayTopDevicesByActivationsByHouse(
                            house,
                            model.getTopDevicesByActivationsByHouse(house, 3, main.currentTotalMinutes()));
                }
                case 8 -> {
                    String houseId = statisticsUI.readHouseId();
                    House house = model.getHouseById(houseId);
                    if (house == null) {
                        statisticsUI.showError("Casa não encontrada.");
                        break;
                    }
                    statisticsUI.displayTopDevicesByTimeByHouse(
                            house,
                            model.getTopDevicesByTimeByHouse(house, 3, main.currentTotalMinutes()),
                            main.currentTotalMinutes());
                }
            }
        } while (choice != 9);
    }
}