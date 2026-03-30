package domuscontrol.view;

import domuscontrol.model.devices.Device;
import java.util.List;

// View de dispositivos - só lê input e mostra output
public class DeviceUI {

    private final Menu menu;
    private final Menu controlMenu;

    public DeviceUI() {
        this.menu = new Menu("Gerir Dispositivos", new String[]{
            "Criar dispositivo",
            "Adicionar dispositivo a divisão",
            "Listar dispositivos",
            "Ver estado de dispositivo",
            "Ligar dispositivo",
            "Desligar dispositivo",
            "Controlar atributos",
            "Voltar"
        });
        this.controlMenu = new Menu("Controlar Atributos", new String[]{
            "Definir brilho (lâmpada)",
            "Definir temperatura de cor (lâmpada)",
            "Definir volume (coluna)",
            "Definir abertura (cortina/portão)",
            "Voltar"
        });
    }

    // Mostra o menu e devolve a opção escolhida
    public int showMenu() {
        return menu.show();
    }

    // Mostra o menu de controlo de atributos e devolve a opção escolhida
    public int showControlMenu() {
        return controlMenu.show();
    }

    // Lê o tipo e dados de um novo dispositivo
    // [0]=tipo, [1]=id, [2]=marca, [3]=modelo, [4]=consumo, [5]=extra(colorSupport/unit)
    public String[] readDeviceData() {
        System.out.println("\n--- Criar Dispositivo ---");
        System.out.println("Tipos: 1-Relay 2-Lampada 3-Coluna 4-Cortina 5-Portao 6-SensorChuva 7-SensorLuz 8-SensorTemp");
        String type = Menu.readLine("Tipo: ");
        String id = Menu.readLine("ID: ");
        String brand = Menu.readLine("Marca: ");
        String model = Menu.readLine("Modelo: ");
        String power = Menu.readLine("Consumo por hora (Wh): ");
        String extra = "";
        if (type.equals("2")) {
            extra = Menu.readLine("Suporta cor? (s/n): ");
        }
        return new String[]{type, id, brand, model, power, extra};
    }

    // Lê os dados para adicionar dispositivo a divisão
    // [0]=idCasa, [1]=nomeDivisão, [2]=idDispositivo
    public String[] readAddToRoomData() {
        System.out.println("\n--- Adicionar a Divisão ---");
        String houseId = Menu.readLine("ID da casa: ");
        String roomName = Menu.readLine("Nome da divisão: ");
        String deviceId = Menu.readLine("ID do dispositivo: ");
        return new String[]{houseId, roomName, deviceId};
    }

    // Lê o id de um dispositivo
    public String readDeviceId() {
        return Menu.readLine("ID do dispositivo: ");
    }

    // Lê um valor de brilho (0-100)
    public int readBrightness() {
        return Menu.readInt("Brilho (0-100): ");
    }

    // Lê uma temperatura de cor em Kelvin (2700-4000)
    public int readColorTemperature() {
        return Menu.readInt("Temperatura de cor em Kelvin (2700-4000): ");
    }

    // Lê um valor de volume (0-100)
    public int readVolume() {
        return Menu.readInt("Volume (0-100): ");
    }

    // Lê uma percentagem de abertura (0-100)
    public int readOpeningPercentage() {
        return Menu.readInt("Abertura em % (0-100): ");
    }

    // Mostra a lista de dispositivos
    public void displayDevices(List<Device> devices) {
        System.out.println("\n--- Dispositivos ---");
        if (devices.isEmpty()) {
            System.out.println("Nenhum dispositivo registado.");
            return;
        }
        for (Device d : devices) {
            System.out.println(d.getStatus());
        }
    }

    // Mostra o estado de um dispositivo
    public void displayDevice(Device device) {
        System.out.println("\n--- Estado ---");
        System.out.println(device.getStatus());
    }

    // Mostra mensagem de sucesso
    public void showSuccess(String message) {
        System.out.println("✓ " + message);
    }

    // Mostra mensagem de erro
    public void showError(String message) {
        System.out.println("✗ Erro: " + message);
    }
}