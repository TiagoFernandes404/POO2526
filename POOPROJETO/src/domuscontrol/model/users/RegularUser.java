package domuscontrol.model.users;

import domuscontrol.model.house.House;

public class RegularUser extends User {
    private static final long serialVersionUID = 1L;

    public RegularUser(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public boolean isAdmin() { return false; }

    @Override
    public boolean canManageHouse(House house) { return false; }

    @Override
    public String getRole() { return "Utilizador"; }
}