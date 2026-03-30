package domuscontrol.model.users;

import domuscontrol.model.house.House;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
// Representa um utilizador do sistema DomusControl


    // Identificador único do utilizador
    private final String id;

    // Nome do utilizador
    private String name;

    // Email do utilizador
    private String email;

    // Password do utilizador
    private String password;

    // Lista de casas em que o utilizador é administrador
    private final List<House> ownedHouses;

    // Lista de casas em que o utilizador é apenas usufrutuário
    private final List<House> guestHouses;

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.ownedHouses = new ArrayList<>();
        this.guestHouses = new ArrayList<>();
    }

    // Adiciona uma casa à lista de casas administradas pelo utilizador
    public void addOwnedHouse(House house) {
        if (house == null) {
            throw new IllegalArgumentException("A casa não pode ser nula.");
        }
        ownedHouses.add(house);
    }

    // Adiciona uma casa à lista de casas em que o utilizador é usufrutuário
    public void addGuestHouse(House house) {
        if (house == null) {
            throw new IllegalArgumentException("A casa não pode ser nula.");
        }
        guestHouses.add(house);
    }

    // Verifica se o utilizador é administrador de uma casa
    public boolean isAdminOf(House house) {
        return ownedHouses.contains(house);
    }

    // Verifica se o utilizador tem acesso a uma casa (admin ou usufrutuário)
    public boolean hasAccessTo(House house) {
        return ownedHouses.contains(house) || guestHouses.contains(house);
    }

    // Retorna todas as casas a que o utilizador tem acesso
    public List<House> getAllHouses() {
        List<House> all = new ArrayList<>();
        all.addAll(ownedHouses);
        all.addAll(guestHouses);
        return all;
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    public List<House> getOwnedHouses() { return new ArrayList<>(ownedHouses); }
    public List<House> getGuestHouses() { return new ArrayList<>(guestHouses); }

    // Verifica se a password está correta
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", id, name, email);
    }
}