package domuscontrol.model.users;

import domuscontrol.model.house.House;

// Um utilizador admin pode fazer tudo - criar casas, gerir dispositivos, etc
// Herda de User mas implementa de forma diferente
public class AdminUser extends User {
    private static final long serialVersionUID = 1L;

    // Construtor normal - quando se cria um admin desde o início
    public AdminUser(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    // Este construtor é para quando um utilizador normal é promovido a admin
    // Assim a password não é exposta publicamente
    protected AdminUser(User user, String encryptedPassword) {
        super(user.getId(), user.getName(), user.getEmail(), encryptedPassword);
    }

    // Admin é sempre admin (lógico)
    @Override
    public boolean isAdmin() {
        return true;
    }

    // Admin pode gerir uma casa se for o dono dela
    @Override
    public boolean canManageHouse(House house) {
        return isAdminOf(house);
    }

    // Retorna a role do utilizador para mostrar na interface
    @Override
    public String getRole() {
        return "Administrador";
    }
}