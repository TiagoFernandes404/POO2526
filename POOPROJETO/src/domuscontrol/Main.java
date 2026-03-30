package domuscontrol;

import domuscontrol.controller.DomusController;
import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.model.users.User;
import domuscontrol.model.devices.*;
import domuscontrol.view.Menu;

// Ponto de entrada da aplicação DomusControl
public class Main {

    public static void main(String[] args) {
        DomusController controller = new DomusController();
        loadTestData(controller);

        Menu mainMenu = new Menu("DomusControl", new String[]{
            "Gerir Utilizadores",
            "Gerir Casas",
            "Gerir Dispositivos",
            "Cenários",
            "Automações e Escalonamentos",
            "Avançar Tempo",
            "Sair"
        });

        int choice;
        do {
            choice = mainMenu.show();
            switch (choice) {
                case 1 -> manageUsers(controller, mainMenu);
                case 2 -> manageHouses(controller, mainMenu);
                case 3 -> manageDevices(controller, mainMenu);
                case 4 -> manageScenarios(controller, mainMenu);
                case 5 -> manageAutomations(controller, mainMenu);
                case 6 -> {
                    controller.tick();
                    System.out.println("Tempo atual: " + controller.getTime());
                }
                case 7 -> System.out.println("A sair...");
            }
        } while (choice != 7);
    }

    // Carrega dados de teste
    private static void loadTestData(DomusController controller) {
        User u1 = new User("u1", "Tiago", "tiago@email.com", "1234");
        House h1 = new House("h1", "Casa da Praia", "Rua do Mar, 1");
        Room sala = new Room("Sala");
        Room quarto = new Room("Quarto");

        SmartLight luz1 = new SmartLight("d1", "Philips", "Hue", 10, true);
        SmartSpeaker coluna = new SmartSpeaker("d2", "Sony", "SRS-X3", 15);
        Blinds cortina = new Blinds("d3", "Somfy", "RS100", 5);

        sala.addDevice(luz1);
        sala.addDevice(coluna);
        quarto.addDevice(cortina);
        h1.addRoom(sala);
        h1.addRoom(quarto);
        u1.addOwnedHouse(h1);
        controller.addUser(u1);
        controller.addHouse(h1);

        System.out.println("Dados de teste carregados!");
    }

    // Gerir utilizadores
    private static void manageUsers(DomusController controller, Menu menu) {
        System.out.println("\n--- Utilizadores ---");
        for (var u : controller.getUsers()) {
            System.out.println(u);
        }
    }

    // Gerir casas
    private static void manageHouses(DomusController controller, Menu menu) {
        System.out.println("\n--- Casas ---");
        for (var h : controller.getHouses()) {
            System.out.println(h);
            for (var r : h.getRooms()) {
                System.out.println("  " + r);
            }
        }
    }

    // Gerir dispositivos
    private static void manageDevices(DomusController controller, Menu menu) {
        System.out.println("\n--- Dispositivos ---");
        for (var h : controller.getHouses()) {
            for (var d : h.getAllDevices()) {
                System.out.println(d.getStatus());
            }
        }
    }

    // Gerir cenários
    private static void manageScenarios(DomusController controller, Menu menu) {
        System.out.println("\n--- Cenários ---");
        for (var s : controller.getScenarios()) {
            System.out.println(s);
        }
    }

    // Gerir automações e escalonamentos
    private static void manageAutomations(DomusController controller, Menu menu) {
        System.out.println("\n--- Automações ---");
        for (var a : controller.getAutomations()) {
            System.out.println(a);
        }
        System.out.println("\n--- Escalonamentos ---");
        for (var s : controller.getSchedules()) {
            System.out.println(s);
        }
    }
}