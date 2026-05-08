package domuscontrol.controller;

import domuscontrol.model.devices.*;
import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.view.DeviceUI;
import java.util.List;

// controlador que trata de tudo o que tem a ver com dispositivos
// separámos em handle() para admin e handleGuest() para utilizador normal
// porque os dois têm permissões diferentes
public class ControllerDevices {

    // precisamos do modelo principal para aceder à lista de dispositivos
    private final DomusController model;
    private final DeviceUI deviceUI;
    // guardamos referência ao controlador total para saber quem está logado
    // e para ter acesso ao tempo atual do simulador
    private final ControllerTotal main;

    public ControllerDevices(DomusController model, DeviceUI deviceUI, ControllerTotal main) {
        this.model = model;
        this.deviceUI = deviceUI;
        this.main = main;
    }

    // Menu de administrador - tem acesso a tudo incluindo criar e adicionar
    // dispositivos
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

    // Menu de utilizador normal (convidado) - só pode ver e operar, não criar
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

    // o utilizador convidado só vê os dispositivos das casas a que tem acesso
    // usamos flatMap para juntar todos os dispositivos de todas as casas numa lista
    // só
    private void listDevicesOfUser() {
        List<Device> todos = main.getLoggedUser().getAllHouses().stream()
                .flatMap(h -> h.getAllDevices().stream())
                .toList();
        // mostramos o tipo do dispositivo entre [] para ficar mais claro
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

            // verificamos se já existe um dispositivo com o mesmo id antes de criar
            if (model.getDeviceById(id) != null) {
                deviceUI.showError("Já existe um dispositivo com esse ID.");
                return;
            }

            // usamos switch expression para instanciar o tipo certo de dispositivo
            // o data[5] só existe para lâmpadas - indica se suporta cor
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
            // só mostramos as casas onde o utilizador é administrador
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

            // lista as divisões da casa escolhida
            List<Room> rooms = house.getRooms();
            if (rooms.isEmpty()) {
                deviceUI.showError("Esta casa não tem divisões.");
                return;
            }
            List<String> roomNames = rooms.stream().map(Room::getName).toList();
            deviceUI.displayRooms(roomNames, house.getName());
            String roomChoice = deviceUI.getUserChoice("Escolhe a divisão (número): ");
            Room room = rooms.get(Integer.parseInt(roomChoice) - 1);

            // só mostramos dispositivos que ainda não foram atribuídos a nenhuma divisão
            // o isUsed() é false enquanto o dispositivo não tiver sido adicionado a uma
            // divisão
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

    // lista todos os dispositivos do sistema com o tipo visível
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
            // recolhemos todos os dispositivos das casas do utilizador logado
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
            // passamos o tempo atual para o turnOn poder registar quando foi ligado
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
            // passamos o tempo atual para calcular quanto tempo esteve ligado
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

            // usamos instanceof com pattern matching para verificar se o dispositivo
            // suporta a operação antes de a tentar executar
            switch (attrChoice) {
                case 1 -> {
                    // brilho só para dispositivos que implementam Dimmable
                    if (!(device instanceof Dimmable l)) {
                        deviceUI.showError("Este dispositivo não é dimmable.");
                        return;
                    }
                    l.setBrightness(deviceUI.readBrightness());
                    deviceUI.showSuccess("Brilho definido.");
                }
                case 2 -> {
                    // cor só para lâmpadas com suporte de cor (ColorAdjustable + hasColorSupport)
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
                    // abertura para cortinas e portões de garagem - ambos estendem ApertureDevice
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