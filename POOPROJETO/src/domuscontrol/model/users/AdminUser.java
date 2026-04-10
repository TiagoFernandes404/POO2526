package domuscontrol.model.users;

import domuscontrol.model.house.House;

public class AdminUser extends User {
    private static final long serialVersionUID = 1L;

    public AdminUser(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public boolean isAdmin() { return true; }

    @Override
    public boolean canManageHouse(House house) {
        return isAdminOf(house);
    }

    @Override
    public String getRole() { return "Administrador"; }
}