// domuscontrol/model/house/Room.java
package domuscontrol.model.house;

import domuscontrol.model.devices.Device;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private final List<Device> devices;

    public Room(String name) {
        this.name = name;
        this.devices = new ArrayList<>();
    }

    public void addDevice(Device device) {
        if (device == null)
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        devices.add(device);
    }

    public boolean removeDevice(String deviceId) {
        return devices.removeIf(d -> d.getId().equals(deviceId));
    }

    public Device getDeviceById(String deviceId) {
        return devices.stream().filter(d -> d.getId().equals(deviceId)).findFirst().orElse(null);
    }

    public List<Device> getDevices() {
        return new ArrayList<>(devices);
    }

    public int getDeviceCount() {
        return devices.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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
        if (o == null || getClass() != o.getClass())
            return false;
        Room room = (Room) o;
        return name.equals(room.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}