package domuscontrol.controller;

import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.model.users.User;
import domuscontrol.view.HouseUI;

public class ControllerHouses {

    private final DomusController model;
    private final HouseUI houseUI;
    private final ControllerTotal main;

    public ControllerHouses(DomusController model, HouseUI houseUI, ControllerTotal main) {
        this.model = model;
        this.houseUI = houseUI;
        this.main = main;
    }

    public void handle() {
        int choice;
        do {
            choice = houseUI.showMenu();
            switch (choice) {
                case 1 -> createHouse();
                case 2 -> addRoom();
                case 3 -> listHouses();
                case 4 -> viewHouseDetails();
                case 5 -> addGuestToHouse();
            }
        } while (choice != 6);
    }

    // Modo apenas leitura para utilizadores normais
    public void viewHousesOnly() {
        houseUI.displayHouses(main.getLoggedUser().getAllHouses());
    }

    private void createHouse() {
        try {
            String[] data = houseUI.readHouseData();
            if (model.getHouseById(data[0]) != null) {
                houseUI.showError("Já existe uma casa com esse ID.");
                return;
            }
            User owner = model.getUserById(data[3]);
            if (owner == null) {
                houseUI.showError("Utilizador não encontrado.");
                return;
            }
            House house = new House(data[0], data[1], data[2], owner);
            model.addHouse(house);
            owner.addOwnedHouse(house);
            houseUI.showSuccess("Casa '" + data[1] + "' criada.");
        } catch (IllegalArgumentException e) {
            houseUI.showError(e.getMessage());
        }
    }

    private void addRoom() {
        try {
            String[] data = houseUI.readRoomData();
            House house = model.getHouseById(data[0]);
            if (house == null) {
                houseUI.showError("Casa não encontrada.");
                return;
            }
            if (!main.getLoggedUser().isAdminOf(house)) {
                houseUI.showError("Não tens permissão para gerir esta casa.");
                return;
            }
            if (house.getRoomByName(data[1]) != null) {
                houseUI.showError("Já existe uma divisão com esse nome.");
                return;
            }
            house.addRoom(new Room(data[0] + "_" + data[1], data[1]));
            houseUI.showSuccess("Divisão '" + data[1] + "' adicionada.");
        } catch (IllegalArgumentException e) {
            houseUI.showError(e.getMessage());
        }
    }

    private void listHouses() {
        houseUI.displayHouses(model.getHouses());
    }

    private void viewHouseDetails() {
        String id = houseUI.readHouseId();
        House house = model.getHouseById(id);
        if (house == null) {
            houseUI.showError("Casa não encontrada.");
            return;
        }
        houseUI.displayHouseDetails(house);
    }

    private void addGuestToHouse() {
        try {
            String[] data = houseUI.readGuestData();
            House house = model.getHouseById(data[0]);
            if (house == null) {
                houseUI.showError("Casa não encontrada.");
                return;
            }
            if (!main.getLoggedUser().isAdminOf(house)) {
                houseUI.showError("Não tens permissão para gerir esta casa.");
                return;
            }
            User guest = model.getUserById(data[1]);
            if (guest == null) {
                houseUI.showError("Utilizador não encontrado.");
                return;
            }
            guest.addGuestHouse(house);
            house.addUser(guest);
            houseUI.showSuccess(guest.getName() + " adicionado como convidado.");
        } catch (IllegalArgumentException e) {
            houseUI.showError(e.getMessage());
        }
    }
}