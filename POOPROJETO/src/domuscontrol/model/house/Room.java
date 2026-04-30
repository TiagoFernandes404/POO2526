package domuscontrol.model.house;

import domuscontrol.model.devices.Device;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * representa um quarto/divisão da casa — por exemplo sala, cozinha, quarto.
 * cada divisão tem uma lista de devices que lá estão instalados.
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id; // ID nunca muda
    private String name;
    private final List<Device> devices; // todos os dispositivos que estão nesta divisão

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
        this.devices = new ArrayList<>(); // começa sem devices
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // adiciona um device à divisão
    public void addDevice(Device device) {
        if (device == null)
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        devices.add(device);
    }

    // remove um device pelo ID, devolve true se encontrou e removeu
    public boolean removeDevice(String deviceId) {
        return devices.removeIf(d -> d.getId().equals(deviceId));
    }

    // procura um device pelo ID, devolve null se não existir
    public Device getDeviceById(String deviceId) {
        return devices.stream().filter(d -> d.getId().equals(deviceId)).findFirst().orElse(null);
    }

    // devolve cópia da lista para ninguém mexer diretamente na lista interna
    public List<Device> getDevices() {
        return new ArrayList<>(devices);
    }

    public int getDeviceCount() {
        return devices.size();
    }

    // muda o nome da divisão com validação
    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("O nome da divisão não pode ser nulo ou vazio.");
        this.name = name;
    }

    @Override
    public String toString() {
        return "Divisão: " + name + " (" + devices.size() + " dispositivos)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Room))
            return false;
        return this.id.equals(((Room) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}