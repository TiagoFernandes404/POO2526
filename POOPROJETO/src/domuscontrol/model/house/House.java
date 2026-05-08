package domuscontrol.model.house;

import domuscontrol.model.devices.Device;
import domuscontrol.model.users.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * representa uma casa no sistema — é o contentor principal.
 * tem quartos, e cada quarto tem devices.
 * tem também utilizadores divididos em admins e convidados.
 * o admin que cria a casa fica automaticamente como admin.
 */
public class House implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id; // ID nunca muda
    private String name;
    private String address;
    private final List<Room> rooms; // quartos que fazem parte da casa
    private final List<User> admins; // utilizadores que são admins desta casa
    private final List<User> users; // todos os utilizadores (admins + convidados)

    public House(String id, String name, String address, User admin) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rooms = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.users = new ArrayList<>();
        // o utilizador que cria a casa é automaticamente admin e utilizador
        if (admin != null) {
            this.admins.add(admin);
            this.users.add(admin);
        }
    }

    // adiciona um novo quarto à casa
    public void addRoom(Room room) {
        if (room == null)
            throw new IllegalArgumentException("A divisão não pode ser nula.");
        rooms.add(room);
    }

    // remove um quarto pelo nome, devolve true se encontrou e removeu
    public boolean removeRoom(String roomName) {
        return rooms.removeIf(r -> r.getName().equals(roomName));
    }

    // procura um quarto pelo nome, devolve null se não existir
    public Room getRoomByName(String roomName) {
        return rooms.stream().filter(r -> r.getName().equals(roomName)).findFirst().orElse(null);
    }

    // devolve cópia da lista para ninguém mexer diretamente na lista interna
    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    // junta todos os devices de todos os quartos numa lista só
    public List<Device> getAllDevices() {
        List<Device> allDevices = new ArrayList<>();
        for (Room room : rooms)
            allDevices.addAll(room.getDevices());
        return allDevices;
    }

    // calcula o consumo total da casa neste momento — só conta devices que estão on
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

    // adiciona um utilizador à casa — verifica se já existe para não duplicar
    public void addUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("O utilizador não pode ser nulo.");
        if (!users.contains(user))
            users.add(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }

    // devolve cópia da lista de utilizadores
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    // adiciona um admin — se ainda não for utilizador, adiciona às duas listas
    public void addAdmin(User admin) {
        if (admin == null)
            throw new IllegalArgumentException("O administrador não pode ser nulo.");
        if (!admins.contains(admin))
            admins.add(admin);
        if (!users.contains(admin))
            users.add(admin);
    }

    /*
     * remove um admin — mas não deixa remover o último.
     * uma casa sem admins ficava sem ninguém para a gerir,
     * por isso lançamos exceção se tentarmos remover o único que resta.
     */
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

    // devolve cópia da lista de admins
    public List<User> getAdmins() {
        return new ArrayList<>(admins);
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
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("O nome da casa não pode ser nulo ou vazio.");
        this.name = name;
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank())
            throw new IllegalArgumentException("O endereço da casa não pode ser nulo ou vazio.");
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
        return this.id.equals(((House) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}