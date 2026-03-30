package domuscontrol.view;

import domuscontrol.controller.DomusController;
import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.model.users.User;

// Interface de utilizador para gestão de casas e divisões
public class HouseUI {

    private DomusController controller;

    public HouseUI(DomusController controller) {
        this.controller = controller;
    }

    // Menu principal de casas
    public void show() {
        Menu menu = new Menu("Gerir Casas", new String[]{
            "Criar casa",
            "Adicionar divisão a uma casa",
            "Listar casas",
            "Ver detalhes de uma casa",
            "Adicionar convidado a uma casa",
            "Voltar"
        });

        int choice;
        do {
            choice = menu.show();
            switch (choice) {
                case 1 -> createHouse();
                case 2 -> addRoom();
                case 3 -> listHouses();
                case 4 -> showHouseDetails();
                case 5 -> addGuest();
                case 6 -> Menu.showMessage("A voltar...");
            }
        } while (choice != 6);
    }

    // Cria uma nova casa e associa-a ao utilizador administrador
    private void createHouse() {
        System.out.println("\n--- Criar Casa ---");
        String id = Menu.readLine("ID da casa: ");
        if (controller.getHouseById(id) != null) {
            Menu.showMessage("Erro: já existe uma casa com esse ID.");
            return;
        }
        String name = Menu.readLine("Nome da casa: ");
        String address = Menu.readLine("Morada: ");
        String ownerId = Menu.readLine("ID do utilizador administrador: ");
        User owner = controller.getUserById(ownerId);
        if (owner == null) {
            Menu.showMessage("Erro: utilizador não encontrado.");
            return;
        }

        House house = new House(id, name, address);
        owner.addOwnedHouse(house);
        controller.addHouse(house);
        Menu.showMessage("Casa '" + name + "' criada e associada a " + owner.getName() + "!");
    }

    // Adiciona uma divisão a uma casa existente
    private void addRoom() {
        System.out.println("\n--- Adicionar Divisão ---");
        House house = selectHouse();
        if (house == null) return;

        String roomName = Menu.readLine("Nome da divisão: ");
        if (house.getRoomByName(roomName) != null) {
            Menu.showMessage("Erro: já existe uma divisão com esse nome nesta casa.");
            return;
        }

        house.addRoom(new Room(roomName));
        Menu.showMessage("Divisão '" + roomName + "' adicionada à casa '" + house.getName() + "'!");
    }

    // Lista todas as casas registadas
    private void listHouses() {
        System.out.println("\n--- Casas ---");
        var houses = controller.getHouses();
        if (houses.isEmpty()) {
            Menu.showMessage("Nenhuma casa registada.");
            return;
        }
        for (House h : houses) {
            System.out.println(h);
        }
    }

    // Mostra os detalhes de uma casa (divisões e dispositivos)
    private void showHouseDetails() {
        System.out.println("\n--- Detalhes da Casa ---");
        House house = selectHouse();
        if (house == null) return;

        System.out.println(house);
        System.out.println("Consumo total atual: " + house.getTotalPowerConsumption() + " Wh");
        for (Room r : house.getRooms()) {
            System.out.println("  " + r);
            for (var d : r.getDevices()) {
                System.out.println("    " + d.getStatus());
            }
        }
    }

    // Adiciona um utilizador como convidado de uma casa
    private void addGuest() {
        System.out.println("\n--- Adicionar Convidado ---");
        House house = selectHouse();
        if (house == null) return;

        String userId = Menu.readLine("ID do utilizador convidado: ");
        User user = controller.getUserById(userId);
        if (user == null) {
            Menu.showMessage("Erro: utilizador não encontrado.");
            return;
        }
        if (user.hasAccessTo(house)) {
            Menu.showMessage("Erro: o utilizador já tem acesso a esta casa.");
            return;
        }

        user.addGuestHouse(house);
        Menu.showMessage(user.getName() + " adicionado como convidado de '" + house.getName() + "'!");
    }

    // Método auxiliar para selecionar uma casa pelo ID
    public House selectHouse() {
        listHouses();
        if (controller.getHouses().isEmpty()) return null;
        String id = Menu.readLine("ID da casa: ");
        House house = controller.getHouseById(id);
        if (house == null) {
            Menu.showMessage("Casa não encontrada.");
        }
        return house;
    }

    // Método auxiliar para selecionar uma divisão de uma casa
    public Room selectRoom(House house) {
        System.out.println("Divisões de '" + house.getName() + "':");
        for (Room r : house.getRooms()) {
            System.out.println("  " + r);
        }
        String roomName = Menu.readLine("Nome da divisão: ");
        Room room = house.getRoomByName(roomName);
        if (room == null) {
            Menu.showMessage("Divisão não encontrada.");
        }
        return room;
    }
}