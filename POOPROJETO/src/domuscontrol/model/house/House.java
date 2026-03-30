package domuscontrol.model.house;

import domuscontrol.model.devices.Device;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class House implements Serializable {
    private static final long serialVersionUID = 1L;

    // Identificador único da casa
    private final String id;

    // Nome da casa (ex: "Casa da Praia")
    private String name;

    // Morada da casa
    private String address;

    // Lista de divisões da casa
    private final List<Room> rooms;

    public House(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rooms = new ArrayList<>();
    }

    // Adiciona uma divisão à casa
    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("A divisão não pode ser nula.");
        }
        rooms.add(room);
    }

    // Remove uma divisão da casa pelo nome
    public boolean removeRoom(String roomName) {
        return rooms.removeIf(r -> r.getName().equals(roomName));
    }

    // Retorna uma divisão pelo nome, ou null se não existir
    public Room getRoomByName(String roomName) {
        return rooms.stream()
                .filter(r -> r.getName().equals(roomName))
                .findFirst()
                .orElse(null);
    }

    // Retorna a lista de todas as divisões da casa
    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    // Retorna todos os dispositivos de todas as divisões da casa
    public List<Device> getAllDevices() {
        List<Device> allDevices = new ArrayList<>();
        for (Room room : rooms) {
            allDevices.addAll(room.getDevices());
        }
        return allDevices;
    }

    // Retorna o consumo total da casa em Wh (soma de todos os dispositivos ligados)
    public double getTotalPowerConsumption() {
        double total = 0;
        for (Device device : getAllDevices()) {
            if (device.isOn()) {
                total += device.getPowerPerHour();
            }
        }
        return total;
    }

    // Retorna o número total de divisões da casa
    public int getRoomCount() {
        return rooms.size();
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%d divisões)", id, name, address, rooms.size());
    }
}