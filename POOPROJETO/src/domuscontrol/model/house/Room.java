package domuscontrol.model.house;

import domuscontrol.model.devices.Device;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    // Nome da divisão (ex: "Sala de Estar")
    private String name;

    // Lista de dispositivos associados a esta divisão
    private final List<Device> devices;

    public Room(String name) {
        this.name = name;
        this.devices = new ArrayList<>();
    }

    // Adiciona um dispositivo à divisão
    public void addDevice(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        }
        devices.add(device);
    }

    // Remove um dispositivo da divisão pelo id
    public boolean removeDevice(String deviceId) {
        return devices.removeIf(d -> d.getId().equals(deviceId));
    }

    // Retorna um dispositivo pelo id, ou null se não existir
    public Device getDeviceById(String deviceId) {
        return devices.stream()
                .filter(d -> d.getId().equals(deviceId))
                .findFirst()
                .orElse(null);
    }

    // Retorna a lista de dispositivos da divisão
    public List<Device> getDevices() {
        return new ArrayList<>(devices);
    }

    // Retorna o número de dispositivos na divisão
    public int getDeviceCount() {
        return devices.size();
    }

    // Getters e Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return String.format("Divisão: %s (%d dispositivos)", name, devices.size());
    }
}