package domuscontrol.model.house;

import domuscontrol.model.devices.Device;
import domuscontrol.model.users.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class House implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String name;
    private String address;
    private final List<Room> rooms;
    private final List<User> admins;
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

    public void addRoom(Room room) {
        if (room == null)
            throw new IllegalArgumentException("A divisão não pode ser nula.");
        rooms.add(room);
    }

    public boolean removeRoom(String roomName) {
        return rooms.removeIf(r -> r.getName().equals(roomName));
    }

    public Room getRoomByName(String roomName) {
        return rooms.stream().filter(r -> r.getName().equals(roomName)).findFirst().orElse(null);
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public List<Device> getAllDevices() {
        List<Device> allDevices = new ArrayList<>();
        for (Room room : rooms)
            allDevices.addAll(room.getDevices());
        return allDevices;
    }

    public double getTotalPowerConsumption() {
        double total = 0;
        for (Device device : getAllDevices()) {
            if (device.isOn())
                total += device.getPowerPerHour();
        }
        return total;
    }

    public int getRoomCount() {
        return rooms.size();
    }

    public void addUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("O utilizador não pode ser nulo.");
        if (!users.contains(user))
            users.add(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void addAdmin(User admin) {
        if (admin == null)
            throw new IllegalArgumentException("O administrador não pode ser nulo.");
        if (!admins.contains(admin))
            admins.add(admin);
        if (!users.contains(admin))
            users.add(admin);
    }

    public boolean removeAdmin(User admin) {
        if (admin == null)
            throw new IllegalArgumentException("O administrador não pode ser nulo.");
        if (admins.size() == 1 && admins.contains(admin))
            throw new IllegalArgumentException("Não é possível remover o único administrador da casa.");
        return admins.remove(admin);
    }

    public boolean isAdmin(User user) {
        return user != null && admins.contains(user);
    }

    public List<User> getAdmins() {
        return new ArrayList<>(admins);
    }

    // substitui para admin
    public void replaceAdmin(User oldUser, User newUser) {
        int adminIndex = admins.indexOf(oldUser);
        if (adminIndex >= 0)
            admins.set(adminIndex, newUser);
        int userIndex = users.indexOf(oldUser);
        if (userIndex >= 0)
            users.set(userIndex, newUser);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%d divisões)", id, name, address, rooms.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof House))
            return false;
        House other = (House) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}