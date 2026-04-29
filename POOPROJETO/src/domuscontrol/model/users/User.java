package domuscontrol.model.users;

import domuscontrol.model.house.House;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String name;
    private String email;
    private String password;
    private final List<House> ownedHouses;
    private final List<House> guestHouses;

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.ownedHouses = new ArrayList<>();
        this.guestHouses = new ArrayList<>();
    }

    public abstract boolean isAdmin();

    public abstract boolean canManageHouse(House house);

    public abstract String getRole();

    public void addOwnedHouse(House house) {
        if (house == null)
            throw new IllegalArgumentException("A casa não pode ser nula.");
        if (!ownedHouses.contains(house))
            ownedHouses.add(house);
    }

    public void addGuestHouse(House house) {
        if (house == null)
            throw new IllegalArgumentException("A casa não pode ser nula.");
        if (!guestHouses.contains(house))
            guestHouses.add(house);
    }

    public void setOwnedHouses(List<House> houses) {
        if (houses == null)
            throw new IllegalArgumentException("A lista de casas não pode ser nula.");
        this.ownedHouses.clear();
        this.ownedHouses.addAll(houses);
    }

    public void setGuestHouses(List<House> houses) {
        if (houses == null)
            throw new IllegalArgumentException("A lista de casas não pode ser nula.");
        this.guestHouses.clear();
        this.guestHouses.addAll(houses);
    }

    public boolean isAdminOf(House house) {
        return house != null && ownedHouses.contains(house);
    }

    public boolean hasAccessTo(House house) {
        return house != null && (ownedHouses.contains(house) || guestHouses.contains(house));
    }

    public List<House> getAllHouses() {
        List<House> all = new ArrayList<>();
        all.addAll(ownedHouses);
        all.addAll(guestHouses);
        return all;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Promove este User para AdminUser, mantendo todas as credenciais e casas.
     * NOTA: Este método é usado exclusivamente durante o processo de promoção de
     * utilizador.
     * A password é copiada internamente de forma segura (não exposta publicamente).
     *
     * @return novo AdminUser com mesmas credenciais e casas que este User
     */
    public AdminUser promoteToAdminUser() {
        AdminUser admin = new AdminUser(getId(), getName(), getEmail(), this.password);
        admin.setOwnedHouses(getOwnedHouses());
        admin.setGuestHouses(getGuestHouses());
        return admin;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String pw) {
        this.password = pw;
    }

    public List<House> getOwnedHouses() {
        return new ArrayList<>(ownedHouses);
    }

    public List<House> getGuestHouses() {
        return new ArrayList<>(guestHouses);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) — %s", id, name, email, getRole());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}