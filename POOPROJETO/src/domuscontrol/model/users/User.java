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

    // Adiciona uma casa à lista de casas em que o utilizador é usufruário
    public void addGuestHouse(House house) {
        if (house == null) {
            throw new IllegalArgumentException("A casa não pode ser nula.");
        }
        guestHouses.add(house);
    }

    // Verifica se o utilizador é user de uma casa
    public boolean isUser(House house) {
        return guestHouses.contains(house);
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

    public void setOwnedHouses(List<House> ownedHouses) {
        if (ownedHouses == null) {
            throw new IllegalArgumentException("A lista de casas não pode ser nula.");
        }
        this.ownedHouses.clear();
        this.ownedHouses.addAll(ownedHouses);
    }

    public void setGuestHouses(List<House> guestHouses) {
        if (guestHouses == null) {
            throw new IllegalArgumentException("A lista de casas não pode ser nula.");
        }
        this.guestHouses.clear();
        this.guestHouses.addAll(guestHouses);
    }

    // Verifica se a password está correta
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    // Verifica se o utilizador é administrador
    public boolean isAdminOf(House house) {
        return house != null && ownedHouses.contains(house);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", id, name, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return id.equals(other.id) && email.equals(other.email) && name.equals(other.name) && password.equals(other.password) && ownedHouses.equals(other.ownedHouses) && guestHouses.equals(other.guestHouses);
    }
}