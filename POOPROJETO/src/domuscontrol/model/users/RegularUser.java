package domuscontrol.model.users;

import domuscontrol.model.house.House;

// Utilizador normal - pode ver casas mas não pode mexer em muita coisa
// Precisa de ser convidado por um admin para aceder a uma casa
public class RegularUser extends User {
    private static final long serialVersionUID = 1L;

    public RegularUser(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    // Utilizador normal não é admin
    @Override
    public boolean isAdmin() {
        return false;
    }

    // Utilizador normal não pode gerir casas (nem as que é convidado)
    @Override
    public boolean canManageHouse(House house) {
        return false;
    }

    // Role é "Utilizador" para aparecer na interface
    @Override
    public String getRole() {
        return "Utilizador";
    }
}