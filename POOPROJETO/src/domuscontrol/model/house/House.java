package domuscontrol.model.house;

import domuscontrol.model.devices.Device;
import domuscontrol.model.users.User;
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

    // Lista de utilizadores administradores da casa
    private final List<User> admins;

    // Lista de utilizadores da casa
    private final List<User> users;

    public House(String id, String name, String address, User admin) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rooms = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.users = new ArrayList<>();
        if (admin != null) {
            this.admins.add(admin);
            this.users.add(admin);
        }
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

    // Adiciona um utilizador à casa
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("O utilizador não pode ser nulo.");
        }
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    // Remove um utilizador da casa
    public boolean removeUser(User user) {
        return users.remove(user);
    }

    // Retorna a lista de utilizadores da casa
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    // Adiciona um administrador à casa
    public void addAdmin(User admin) {
        if (admin == null) {
            throw new IllegalArgumentException("O administrador não pode ser nulo.");
        }
        if (!admins.contains(admin)) {
            admins.add(admin);
        }
        if (!users.contains(admin)) {
            users.add(admin);
        }
    }

    // Remove um administrador da casa
    public boolean removeAdmin(User admin) {
        if (admin == null) {
            throw new IllegalArgumentException("O administrador não pode ser nulo.");
        }
        if (admins.size() == 1 && admins.contains(admin)) {
            throw new IllegalArgumentException("Não é possível remover o único administrador da casa.");
        }
        return admins.remove(admin);
    }

    // Verifica se um utilizador é administrador da casa
    public boolean isAdmin(User user) {
        return user != null && admins.contains(user);
    }

    // Retorna a lista de administradores da casa
    public List<User> getAdmins() {
        return new ArrayList<>(admins);
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        House other = (House) obj;
        return id.equals(other.id) && name.equals(other.name) && address.equals(other.address) && rooms.equals(other.rooms) && admins.equals(other.admins) && users.equals(other.users);

    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%d divisões)", id, name, address, rooms.size());
    }
}