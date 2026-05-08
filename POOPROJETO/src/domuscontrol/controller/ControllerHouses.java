package domuscontrol.controller;

import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.model.users.User;
import domuscontrol.view.HouseUI;
import java.util.List;

// controlador para tudo o que tem a ver com casas e divisões
public class ControllerHouses {

    private final DomusController model;
    private final HouseUI houseUI;
    // precisamos do main para saber quem está logado e verificar permissões
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

    // o utilizador regular só pode ver as casas, não gerir
    public void viewHousesOnly() {
        var houses = main.getLoggedUser().getAllHouses().stream().map(House::toString).toList();
        houseUI.displayHouses(houses);
    }

    // lista auxiliar para mostrar casas disponíveis com id e nome
    private List<String> houseList() {
        return model.getHouses().stream()
                .map(h -> "[" + h.getId() + "] " + h.getName()).toList();
    }

    // lista auxiliar para mostrar utilizadores disponíveis
    private List<String> userList() {
        return model.getUsers().stream()
                .map(u -> "[" + u.getId() + "] " + u.getName()).toList();
    }

    private void createHouse() {
        try {
            String[] data = houseUI.readHouseData(userList());
            // verificamos se já existe casa com o mesmo id
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
            // associamos a casa ao dono para ele a conseguir ver nas suas listas
            owner.addOwnedHouse(house);
            houseUI.showSuccess("Casa '" + data[1] + "' criada.");
        } catch (IllegalArgumentException e) {
            houseUI.showError(e.getMessage());
        }
    }

    private void addRoom() {
        try {
            String[] data = houseUI.readRoomData(houseList());
            House house = model.getHouseById(data[0]);
            if (house == null) {
                houseUI.showError("Casa não encontrada.");
                return;
            }
            // só o administrador da casa pode adicionar divisões
            if (!main.getLoggedUser().isAdminOf(house)) {
                houseUI.showError("Não tens permissão para gerir esta casa.");
                return;
            }
            // não podemos ter duas divisões com o mesmo nome na mesma casa
            if (house.getRoomByName(data[1]) != null) {
                houseUI.showError("Já existe uma divisão com esse nome.");
                return;
            }
            // o id da divisão é composto pelo id da casa + nome para ser único
            house.addRoom(new Room(data[0] + "_" + data[1], data[1]));
            houseUI.showSuccess("Divisão '" + data[1] + "' adicionada.");
        } catch (IllegalArgumentException e) {
            houseUI.showError(e.getMessage());
        }
    }

    private void listHouses() {
        var houses = model.getHouses().stream().map(House::toString).toList();
        houseUI.displayHouses(houses);
    }

    private void viewHouseDetails() {
        String id = houseUI.readHouseId(houseList());
        House house = model.getHouseById(id);
        if (house == null) {
            houseUI.showError("Casa não encontrada.");
            return;
        }
        String houseInfo = house.toString();
        String totalConsumptionInfo = "Consumo total atual: " + house.getTotalPowerConsumption() + " Wh";
        // para cada divisão mostramos os dispositivos e o seu estado
        var roomInfos = house.getRooms().stream()
                .map(r -> r.toString() + "\n    "
                        + r.getDevices().stream().map(d -> d.getStatus()).reduce((a, b) -> a + "\n    " + b).orElse(""))
                .toList();
        houseUI.displayHouseDetails(houseInfo, roomInfos, totalConsumptionInfo);
    }

    private void addGuestToHouse() {
        try {
            String[] data = houseUI.readGuestData(houseList(), userList());
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
            // temos que associar nos dois sentidos - o utilizador sabe que é convidado
            // e a casa sabe que tem aquele utilizador
            guest.addGuestHouse(house);
            house.addUser(guest);
            houseUI.showSuccess(guest.getName() + " adicionado como convidado.");
        } catch (IllegalArgumentException e) {
            houseUI.showError(e.getMessage());
        }
    }
}