package domuscontrol.view;

import domuscontrol.model.devices.Device;
import java.util.List;

public class DeviceUI {
    private final Menu menu;
    private final Menu controlMenu;
    private final Menu guestMenu;

    public DeviceUI() {
        this.menu = new Menu("Gerir Dispositivos", new String[] {
                "Criar dispositivo",
                "Adicionar dispositivo a divisão",
                "Listar dispositivos",
                "Ver estado de dispositivo",
                "Ligar dispositivo",
                "Desligar dispositivo",
                "Controlar atributos",
                "Voltar"
        });
        this.controlMenu = new Menu("Controlar Atributos", new String[] {
                "Definir brilho (lâmpada)",
                "Definir temperatura de cor (lâmpada)",
                "Definir volume (coluna)",
                "Definir abertura (cortina/portão)",
                "Voltar"
        });

        this.guestMenu = new Menu("Dispositivos", new String[] {
                "Listar dispositivos",
                "Ver estado de dispositivo",
                "Ligar dispositivo",
                "Desligar dispositivo",
                "Voltar"
        });
    }

    public int showMenu() {
        return menu.show();
    }

    public int showControlMenu() {
        return controlMenu.show();
    }

    public int showGuestMenu() {
        return guestMenu.show();
    }

    public String[] readDeviceData() {
        System.out.println("\n--- Criar Dispositivo ---");
        System.out
                .println("Tipos: 1-Relay 2-Lampada 3-Coluna 4-Cortina 5-Portao 6-SensorChuva 7-SensorLuz 8-SensorTemp");
        String type = Menu.readLine("Tipo: ");
        String id = Menu.readLine("ID: ");
        String brand = Menu.readLine("Marca: ");
        String model = Menu.readLine("Modelo: ");
        String power = Menu.readLine("Consumo por hora (Wh): ");
        String extra = "";
        if (type.equals("2"))
            extra = Menu.readLine("Suporta cor? (s/n): ");
        return new String[] { type, id, brand, model, power, extra };
    }

    public String[] readAddToRoomData() {
        System.out.println("\n--- Adicionar a Divisão ---");
        String houseId = Menu.readLine("ID da casa: ");
        String roomName = Menu.readLine("Nome da divisão: ");
        String deviceId = Menu.readLine("ID do dispositivo: ");
        return new String[] { houseId, roomName, deviceId };
    }

    public String readDeviceId() {
        return Menu.readLine("ID do dispositivo: ");
    }

    public int readBrightness() {
        return Menu.readInt("Brilho (0-100): ");
    }

    public int readColorTemperature() {
        return Menu.readInt("Temperatura de cor em Kelvin (2700-4000): ");
    }

    public int readVolume() {
        return Menu.readInt("Volume (0-100): ");
    }

    public int readOpeningPercentage() {
        return Menu.readInt("Abertura em % (0-100): ");
    }

    public void displayDevices(List<Device> devices) {
        System.out.println("\n--- Dispositivos ---");
        if (devices.isEmpty()) {
            System.out.println("Nenhum dispositivo registado.");
            return;
        }
        for (Device d : devices)
            System.out.println(d.getStatus());
    }

    public void displayDevice(Device device) {
        System.out.println("\n--- Estado ---");
        System.out.println(device.getStatus());
    }

    public void showSuccess(String message) {
        System.out.println("Sucesso:" + message);
    }

    public void showError(String message) {
        System.out.println("Erro: " + message);
    }

    public void displayHouses(java.util.List<domuscontrol.model.house.House> houses) {
        System.out.println("\n--- Casas Disponíveis ---");
        for (int i = 0; i < houses.size(); i++) {
            System.out.println((i + 1) + ". " + houses.get(i).getName());
        }
    }

    public void displayRooms(java.util.List<domuscontrol.model.house.Room> rooms, String houseName) {
        System.out.println("\n--- Divisões de \"" + houseName + "\" ---");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println((i + 1) + ". " + rooms.get(i).getName() +
                    " (" + rooms.get(i).getDeviceCount() + " dispositivos)");
        }
    }

    public void displayAvailableDevices(java.util.List<Device> devices) {
        System.out.println("\n--- Dispositivos Disponíveis (não atribuídos) ---");
        for (int i = 0; i < devices.size(); i++) {
            Device d = devices.get(i);
            System.out.println((i + 1) + ". [" + d.getClass().getSimpleName() + "] " +
                    d.getId() + " (" + d.getBrand() + ") - " + d.getPowerPerHour() + "W");
        }
    }

    public void displayDevicesWithStatus(java.util.List<Device> devices) {
        System.out.println("\n--- Dispositivos ---");
        for (int i = 0; i < devices.size(); i++) {
            Device d = devices.get(i);
            String status = d.isOn() ? " ON" : " OFF";
            System.out.println((i + 1) + ". " + d.getId() + " - " + status +
                    " [" + d.getClass().getSimpleName() + "]");
        }
    }

    public String getUserChoice(String prompt) {
        return Menu.readLine(prompt);
    }
}