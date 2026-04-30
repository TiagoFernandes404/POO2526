package domuscontrol.controller;

import domuscontrol.model.users.RegularUser;
import domuscontrol.model.users.User;
import domuscontrol.view.*;

public class ControllerTotal {

    private final DomusController controller;
    private final MainUI mainUI;

    private final ControllerUsers       ctrlUsers;
    private final ControllerHouses      ctrlHouses;
    private final ControllerDevices     ctrlDevices;
    private final ControllerScenarios   ctrlScenarios;
    private final ControllerAutomations ctrlAutomations;
    private final ControllerTime        ctrlTime;
    private final ControllerStatistics  ctrlStatistics;

    private User loggedUser;

    public ControllerTotal() {
        DomusController loaded = StateManager.load();
        this.controller = (loaded != null) ? loaded : new DomusController();
        this.mainUI = new MainUI();

        this.ctrlUsers       = new ControllerUsers(controller, new UserUI());
        this.ctrlHouses      = new ControllerHouses(controller, new HouseUI(), this);
        this.ctrlDevices     = new ControllerDevices(controller, new DeviceUI(), this);
        this.ctrlScenarios   = new ControllerScenarios(controller, new ScenarioUI(), this);
        this.ctrlAutomations = new ControllerAutomations(controller, new AutomationUI());
        this.ctrlTime        = new ControllerTime(controller, new TimeUI());
        this.ctrlStatistics  = new ControllerStatistics(controller, new StatisticsUI(), this);
    }

    // Métodos de acesso partilhado (usados pelos sub-controllers)

    public User getLoggedUser() {
        return loggedUser;
    }

    public int currentTotalMinutes() {
        return controller.getTime().getDay() * 24 * 60
                + controller.getTime().getHour() * 60
                + controller.getTime().getMinute();
    }

    public void run() {
        int choice;
        do {
            choice = mainUI.showMenu();
            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> handleRegister();
                case 3 -> {
                    StateManager.save(controller);
                    mainUI.showGoodbye();
                }
            }
        } while (choice != 3);
    }

    // LOGIN E REGISTO

    private void handleLogin() {
        String[] data = mainUI.readLoginData();
        User user = controller.getUsers().stream()
                .filter(u -> u.getEmail().equals(data[0]))
                .findFirst().orElse(null);
        if (user == null || !user.checkPassword(data[1])) {
            mainUI.showError("Email ou password incorretos.");
            return;
        }
        loggedUser = user;
        mainUI.showSuccess("Bem-vindo, " + user.getName() + "!");
        handleDashboard();
        loggedUser = null;
    }

    private void handleRegister() {
        try {
            String[] data = mainUI.readRegisterData();
            controller.addUser(new RegularUser(data[0], data[1], data[2], data[3]));

            if (mainUI.askPromoteToAdmin()) {
                ctrlUsers.promoteUserRegister(data[0]);
            }

            StateManager.save(controller);
            mainUI.showSuccess("Utilizador '" + data[1] + "' registado. Pode fazer login.");
        } catch (IllegalArgumentException e) {
            mainUI.showError(e.getMessage());
        }
    }

    // DASHBOARD

    private boolean isAdmin() {
        return loggedUser.isAdmin();
    }

    private void handleDashboard() {
        int choice;
        do {
            boolean admin = isAdmin();
            choice = mainUI.showDashboard(loggedUser.getName(), admin);
            if (admin) {
                switch (choice) {
                    case 1 -> { ctrlHouses.handle();      StateManager.save(controller); }
                    case 2 -> { ctrlDevices.handle();     StateManager.save(controller); }
                    case 3 -> { ctrlScenarios.handle();   StateManager.save(controller); }
                    case 4 -> { ctrlAutomations.handle(); StateManager.save(controller); }
                    case 5 -> { ctrlTime.handle();        StateManager.save(controller); }
                    case 6 -> { ctrlUsers.handle();       StateManager.save(controller); }
                    case 7 -> { ctrlStatistics.handle();  StateManager.save(controller); }
                    // case 8 = Logout
                }
            } else {
                switch (choice) {
                    case 1 -> ctrlHouses.viewHousesOnly();
                    case 2 -> { ctrlDevices.handleGuest();  StateManager.save(controller); }
                    case 3 -> { ctrlScenarios.handle();     StateManager.save(controller); }
                    case 4 -> { ctrlTime.handle();          StateManager.save(controller); }
                    // case 5 = Logout
                }
            }
        } while (isAdmin() ? choice != 8 : choice != 5);
    }
}