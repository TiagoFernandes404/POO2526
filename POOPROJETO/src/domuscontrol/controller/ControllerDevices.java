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
        deviceUI.displayDevices(todos);
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
            String[] data = deviceUI.readAddToRoomData();
            House house = model.getHouseById(data[0]);
            if (house == null) {
                deviceUI.showError("Casa não encontrada.");
                return;
            }
            if (!main.getLoggedUser().isAdminOf(house)) {
                deviceUI.showError("Não tens permissão para gerir esta casa.");
                return;
            }
            Room room = house.getRoomByName(data[1]);
            if (room == null) {
                deviceUI.showError("Divisão não encontrada.");
                return;
            }
            Device device = model.getDeviceById(data[2]);
            if (device == null) {
                deviceUI.showError("Dispositivo não encontrado.");
                return;
            }
            room.addDevice(device);
            deviceUI.showSuccess("Dispositivo adicionado à divisão '" + data[1] + "'.");
        } catch (IllegalArgumentException e) {
            deviceUI.showError(e.getMessage());
        }
    }

    private void listDevices() {
        deviceUI.displayDevices(model.getAllDevices());
    }

    private void viewDeviceStatus() {
        String id = deviceUI.readDeviceId();
        Device device = model.getDeviceById(id);
        if (device == null) {
            deviceUI.showError("Dispositivo não encontrado.");
            return;
        }
        deviceUI.displayDevice(device);
    }

    private void turnOnDevice() {
        String id = deviceUI.readDeviceId();
        Device device = model.getDeviceById(id);
        if (device == null) {
            deviceUI.showError("Dispositivo não encontrado.");
            return;
        }
        device.turnOn(main.currentTotalMinutes());
        deviceUI.showSuccess("Dispositivo '" + id + "' ligado.");
    }

    private void turnOffDevice() {
        String id = deviceUI.readDeviceId();
        Device device = model.getDeviceById(id);
        if (device == null) {
            deviceUI.showError("Dispositivo não encontrado.");
            return;
        }
        device.turnOff(main.currentTotalMinutes());
        deviceUI.showSuccess("Dispositivo '" + id + "' desligado.");
    }

    private void controlDeviceAttributes() {
        String id = deviceUI.readDeviceId();
        Device device = model.getDeviceById(id);
        if (device == null) {
            deviceUI.showError("Dispositivo não encontrado.");
            return;
        }
        int choice = deviceUI.showControlMenu();
        try {
            switch (choice) {
                case 1 -> {
                    if (!(device instanceof Dimmable l)) {
                        deviceUI.showError("Não é dimmable.");
                        return;
                    }
                    l.setBrightness(deviceUI.readBrightness());
                    deviceUI.showSuccess("Brilho definido.");
                }
                case 2 -> {
                    if (!(device instanceof ColorAdjustable l) || !l.hasColorSupport()) {
                        deviceUI.showError("Lâmpada sem suporte de cor.");
                        return;
                    }
                    l.setColorTemperature(deviceUI.readColorTemperature());
                    deviceUI.showSuccess("Temperatura de cor definida.");
                }
                case 3 -> {
                    if (!(device instanceof SmartSpeaker s)) {
                        deviceUI.showError("Não é coluna.");
                        return;
                    }
                    s.setVolume(deviceUI.readVolume());
                    deviceUI.showSuccess("Volume definido.");
                }
                case 4 -> {
                    if (!(device instanceof ApertureDevice a)) {
                        deviceUI.showError("Sem controlo de abertura.");
                        return;
                    }
                    a.setOpeningPercentage(deviceUI.readOpeningPercentage(), main.currentTotalMinutes());
                    deviceUI.showSuccess("Abertura definida.");
                }
            }
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            deviceUI.showError(e.getMessage());
        }
    }
}