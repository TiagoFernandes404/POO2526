package domuscontrol.controller;

import domuscontrol.model.devices.*;
import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.view.DeviceUI;
import java.util.List;

public class ControllerDevices {

    private final DomusController model;
    private final DeviceUI deviceUI;
    private final ControllerTotal main;

    public ControllerDevices(DomusController model, DeviceUI deviceUI, ControllerTotal main) {
        this.model = model;
        this.deviceUI = deviceUI;
        this.main = main;
    }

    // Menu de administrador
    public void handle() {
        int choice;
        do {
            choice = deviceUI.showMenu();
            switch (choice) {
                case 1 -> createDevice();
                case 2 -> addDeviceToRoom();
                case 3 -> listDevices();
                case 4 -> viewDeviceStatus();
                case 5 -> turnOnDevice();
                case 6 -> turnOffDevice();
                case 7 -> controlDeviceAttributes();
            }
        } while (choice != 8);
    }

    // Menu de utilizador normal (convidado)
    public void handleGuest() {
        int choice;
        do {
            choice = deviceUI.showGuestMenu();
            switch (choice) {
                case 1 -> listDevicesOfUser();
                case 2 -> viewDeviceStatus();
                case 3 -> turnOnDevice();
                case 4 -> turnOffDevice();
            }
        } while (choice != 5);
    }

    private void listDevicesOfUser() {
        List<Device> todos = main.getLoggedUser().getAllHouses().stream()
                .flatMap(h -> h.getAllDevices().stream())
                .toList();
        List<String> infos = todos.stream()
                .map(d -> "[" + d.getClass().getSimpleName() + "] " + d.getStatus())
                .toList();
        deviceUI.displayDevices(infos);
    }

    private void createDevice() {
        try {
            String[] data = deviceUI.readDeviceData();
            String type = data[0];
            String id = data[1];
            String brand = data[2];
            String mdl = data[3];
            double power = Double.parseDouble(data[4]);

            if (model.getDeviceById(id) != null) {
                deviceUI.showError("Já existe um dispositivo com esse ID.");
                return;
            }

            Device device = switch (type) {
                case "1" -> new Relay(id, brand, mdl, power);
                case "2" -> new SmartLight(id, brand, mdl, power, data[5].equalsIgnoreCase("s"));
                case "3" -> new SmartSpeaker(id, brand, mdl, power);
                case "4" -> new Blinds(id, brand, mdl, power);
                case "5" -> new GarageDoor(id, brand, mdl, power);
                case "6" -> new RainSensor(id, brand, mdl, power);
                case "7" -> new LightSensor(id, brand, mdl, power);
                case "8" -> new TemperatureSensor(id, brand, mdl, power);
                default -> throw new IllegalArgumentException("Tipo inválido.");
            };

            model.addDevice(device);
            deviceUI.showSuccess("Dispositivo '" + id + "' criado.");
        } catch (IllegalArgumentException e) {
            deviceUI.showError(e.getMessage());
        }
    }

    private void addDeviceToRoom() {
        try {
            // Lista casas do admin
            List<House> adminHouses = main.getLoggedUser().getAllHouses()
                    .stream()
                    .filter(h -> main.getLoggedUser().isAdminOf(h))
                    .toList();

            if (adminHouses.isEmpty()) {
                deviceUI.showError("Nenhuma casa para administrar.");
                return;
            }

            List<String> houseNames = adminHouses.stream().map(House::getName).toList();
            deviceUI.displayHouses(houseNames);
            String houseChoice = deviceUI.getUserChoice("Escolhe a casa (número): ");
            House house = adminHouses.get(Integer.parseInt(houseChoice) - 1);

            // Lista divisões da casa
            List<Room> rooms = house.getRooms();

            if (rooms.isEmpty()) {
                deviceUI.showError("Esta casa não tem divisões.");
                return;
            }

            List<String> roomNames = rooms.stream().map(Room::getName).toList();
            deviceUI.displayRooms(roomNames, house.getName());
            String roomChoice = deviceUI.getUserChoice("Escolhe a divisão (número): ");
            Room room = rooms.get(Integer.parseInt(roomChoice) - 1);

            // Lista dispositivos não atribuídos
            List<Device> availableDevices = model.getAllDevices()
                    .stream()
                    .filter(d -> !d.isUsed())
                    .toList();

            if (availableDevices.isEmpty()) {
                deviceUI.showError("Nenhum dispositivo disponível.");
                return;
            }

            List<String> availableInfos = availableDevices.stream()
                    .map(d -> "[" + d.getClass().getSimpleName() + "] " + d.getId() + " (" + d.getBrand() + ") - "
                            + d.getPowerPerHour() + "W")
                    .toList();
            deviceUI.displayAvailableDevices(availableInfos);
            String deviceChoice = deviceUI.getUserChoice("Escolhe o dispositivo (número): ");
            Device device = availableDevices.get(Integer.parseInt(deviceChoice) - 1);

            room.addDevice(device); // já marca como usado automaticamente
            deviceUI.showSuccess("Dispositivo '" + device.getId()
                    + "' adicionado à divisão '" + room.getName() + "'.");

        } catch (IndexOutOfBoundsException e) {
            deviceUI.showError("Opção inválida!");
        } catch (NumberFormatException e) {
            deviceUI.showError("Entrada inválida! Use números.");
        } catch (IllegalArgumentException e) {
            deviceUI.showError(e.getMessage());
        }
    }

    private void listDevices() {
        List<String> infos = model.getAllDevices().stream()
                .map(d -> "[" + d.getClass().getSimpleName() + "] " + d.getStatus())
                .toList();
        deviceUI.displayDevices(infos);
    }

    private void viewDeviceStatus() {
        String id = deviceUI.readDeviceId();
        Device device = model.getDeviceById(id);
        if (device == null) {
            deviceUI.showError("Dispositivo não encontrado.");
            return;
        }
        deviceUI.displayDevice(device.getStatus());
    }

    private void turnOnDevice() {
        try {
            // Lista dispositivos do utilizador com status
            List<Device> userDevices = main.getLoggedUser().getAllHouses()
                    .stream()
                    .flatMap(h -> h.getAllDevices().stream())
                    .toList();

            if (userDevices.isEmpty()) {
                deviceUI.showError("Nenhum dispositivo disponível.");
                return;
            }

            List<String> userInfos = userDevices.stream()
                    .map(d -> d.getId() + " - " + (d.isOn() ? "ON" : "OFF") + " [" + d.getClass().getSimpleName() + "]")
                    .toList();
            deviceUI.displayDevicesWithStatus(userInfos);
            String choice = deviceUI.getUserChoice("Escolhe o dispositivo (número): ");
            Device device = userDevices.get(Integer.parseInt(choice) - 1);

            device.turnOn(main.currentTotalMinutes());
            deviceUI.showSuccess("Dispositivo '" + device.getId() + "' ligado.");

        } catch (IndexOutOfBoundsException e) {
            deviceUI.showError("Opção inválida!");
        } catch (NumberFormatException e) {
            deviceUI.showError("Entrada inválida! Use números.");
        }
    }

    private void turnOffDevice() {
        try {
            // Lista dispositivos do utilizador com status
            List<Device> userDevices = main.getLoggedUser().getAllHouses()
                    .stream()
                    .flatMap(h -> h.getAllDevices().stream())
                    .toList();

            if (userDevices.isEmpty()) {
                deviceUI.showError("Nenhum dispositivo disponível.");
                return;
            }

            List<String> userInfos = userDevices.stream()
                    .map(d -> d.getId() + " - " + (d.isOn() ? "ON" : "OFF") + " [" + d.getClass().getSimpleName() + "]")
                    .toList();
            deviceUI.displayDevicesWithStatus(userInfos);
            String choice = deviceUI.getUserChoice("Escolhe o dispositivo (número): ");
            Device device = userDevices.get(Integer.parseInt(choice) - 1);

            device.turnOff(main.currentTotalMinutes());
            deviceUI.showSuccess("Dispositivo '" + device.getId() + "' desligado.");

        } catch (IndexOutOfBoundsException e) {
            deviceUI.showError("Opção inválida!");
        } catch (NumberFormatException e) {
            deviceUI.showError("Entrada inválida! Use números.");
        }
    }

    private void controlDeviceAttributes() {
        try {
            // Lista dispositivos do utilizador com status
            List<Device> userDevices = main.getLoggedUser().getAllHouses()
                    .stream()
                    .flatMap(h -> h.getAllDevices().stream())
                    .toList();

            if (userDevices.isEmpty()) {
                deviceUI.showError("Nenhum dispositivo disponível.");
                return;
            }

            List<String> userInfos = userDevices.stream()
                    .map(d -> d.getId() + " - " + (d.isOn() ? "ON" : "OFF") + " [" + d.getClass().getSimpleName() + "]")
                    .toList();
            deviceUI.displayDevicesWithStatus(userInfos);
            String choice = deviceUI.getUserChoice("Escolhe o dispositivo (número): ");
            Device device = userDevices.get(Integer.parseInt(choice) - 1);

            int attrChoice = deviceUI.showControlMenu();
            switch (attrChoice) {
                case 1 -> {
                    if (!(device instanceof Dimmable l)) {
                        deviceUI.showError("Este dispositivo não é dimmable.");
                        return;
                    }
                    l.setBrightness(deviceUI.readBrightness());
                    deviceUI.showSuccess("Brilho definido.");
                }
                case 2 -> {
                    if (!(device instanceof ColorAdjustable l) || !l.hasColorSupport()) {
                        deviceUI.showError("Este dispositivo não tem suporte de cor.");
                        return;
                    }
                    l.setColorTemperature(deviceUI.readColorTemperature());
                    deviceUI.showSuccess("Temperatura de cor definida.");
                }
                case 3 -> {
                    if (!(device instanceof SmartSpeaker s)) {
                        deviceUI.showError("Este dispositivo não é coluna.");
                        return;
                    }
                    s.setVolume(deviceUI.readVolume());
                    deviceUI.showSuccess("Volume definido.");
                }
                case 4 -> {
                    if (!(device instanceof ApertureDevice a)) {
                        deviceUI.showError("Este dispositivo não tem controlo de abertura.");
                        return;
                    }
                    a.setOpeningPercentage(deviceUI.readOpeningPercentage(), main.currentTotalMinutes());
                    deviceUI.showSuccess("Abertura definida.");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            deviceUI.showError("Opção inválida!");
        } catch (NumberFormatException e) {
            deviceUI.showError("Entrada inválida! Use números.");
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            deviceUI.showError(e.getMessage());
        }
    }
}