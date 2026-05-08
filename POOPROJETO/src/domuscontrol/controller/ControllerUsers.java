package domuscontrol.controller;

import domuscontrol.model.users.RegularUser;
import domuscontrol.model.users.User;
import domuscontrol.view.UserUI;
import java.util.List;

public class ControllerUsers {

    private final DomusController model;
    private final UserUI userUI;

    public ControllerUsers(DomusController model, UserUI userUI) {
        this.model = model;
        this.userUI = userUI;
    }

    public void handle() {
        int choice;
        do {
            choice = userUI.showMenu();
            switch (choice) {
                case 1 -> createUser();
                case 2 -> listUsers();
                case 3 -> viewUserDetails();
                case 4 -> promoteUser();
            }
        } while (choice != 5);
    }

    private List<String> userList() {
        return model.getUsers().stream()
                .map(u -> "[" + u.getId() + "] " + u.getName()).toList();
    }

    private void createUser() {
        try {
            String[] data = userUI.readUserData();
            if (model.getUserById(data[0]) != null) {
                userUI.showError("Já existe um utilizador com o ID '" + data[0] + "'.");
                return;
            }
            model.addUser(new RegularUser(data[0], data[1], data[2], data[3]));
            userUI.showSuccess("Utilizador '" + data[1] + "' criado.");
        } catch (IllegalArgumentException e) {
            userUI.showError(e.getMessage());
        }
    }

    private void listUsers() {
        List<String> userInfos = model.getUsers().stream().map(User::toString).toList();
        userUI.displayUsers(userInfos);
    }

    private void viewUserDetails() {
        String id = userUI.readUserId(userList());
        User user = model.getUserById(id);
        if (user == null) {
            userUI.showError("Utilizador não encontrado.");
            return;
        }
        userUI.displayUser(user.toString());
    }

    private void promoteUser() {
        String id = userUI.readUserId(userList());
        try {
            model.promoteToAdmin(id);
            userUI.showSuccess("Utilizador '" + id + "' promovido a administrador.");
        } catch (IllegalArgumentException e) {
            userUI.showError(e.getMessage());
        }
    }

    public void promoteUserRegister(String id) {
        try {
            model.promoteToAdmin(id);
            userUI.showSuccess("Utilizador '" + id + "' promovido a administrador.");
        } catch (IllegalArgumentException e) {
            userUI.showError(e.getMessage());
        }
    }
}