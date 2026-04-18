package domuscontrol.controller;

import domuscontrol.model.users.RegularUser;
import domuscontrol.model.users.User;
import domuscontrol.view.UserUI;

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
        userUI.displayUsers(model.getUsers());
    }

    private void viewUserDetails() {
        String id = userUI.readUserId();
        User user = model.getUserById(id);
        if (user == null) {
            userUI.showError("Utilizador não encontrado.");
            return;
        }
        userUI.displayUser(user);
    }

    private void promoteUser() {
        String id = userUI.readUserId();
        try {
            model.promoteToAdmin(id);
            userUI.showSuccess("Utilizador '" + id + "' promovido a administrador.");
        } catch (IllegalArgumentException e) {
            userUI.showError(e.getMessage());
        }
    }

    // Usada durante o registo no ControllerTotal
    public void promoteUserRegister(String id) {
        try {
            model.promoteToAdmin(id);
            userUI.showSuccess("Utilizador '" + id + "' promovido a administrador.");
        } catch (IllegalArgumentException e) {
            userUI.showError(e.getMessage());
        }
    }
}