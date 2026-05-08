package domuscontrol.controller;

import domuscontrol.model.users.RegularUser;
import domuscontrol.model.users.User;
import domuscontrol.view.*;

// controlador principal que coordena todos os outros controladores
// é aqui que começa tudo - faz o load do estado e inicializa os sub-controladores
public class ControllerTotal {

    private final DomusController controller;
    private final MainUI mainUI;

    // cada área funcional tem o seu próprio controlador
    private final ControllerUsers ctrlUsers;
    private final ControllerHouses ctrlHouses;
    private final ControllerDevices ctrlDevices;
    private final ControllerScenarios ctrlScenarios;
    private final ControllerAutomations ctrlAutomations;
    private final ControllerTime ctrlTime;
    private final ControllerStatistics ctrlStatistics;

    // utilizador que está logado neste momento - null quando não há sessão ativa
    private User loggedUser;

    public ControllerTotal() {
        // tentamos carregar o estado anterior, se não existir iniciamos do zero
        DomusController loaded = StateManager.load();
        if (loaded != null) {
            this.controller = loaded;
        } else {
            this.controller = new DomusController();
            // preenchemos com dados de teste para a apresentação
            PopulateStateManager.seed(this.controller);
            StateManager.save(this.controller);
        }
        this.mainUI = new MainUI();

        // passamos sempre o this para os sub-controladores que precisam de saber
        // quem está logado ou aceder ao tempo atual
        this.ctrlUsers = new ControllerUsers(controller, new UserUI());
        this.ctrlHouses = new ControllerHouses(controller, new HouseUI(), this);
        this.ctrlDevices = new ControllerDevices(controller, new DeviceUI(), this);
        this.ctrlScenarios = new ControllerScenarios(controller, new ScenarioUI(), this);
        this.ctrlAutomations = new ControllerAutomations(controller, new AutomationUI());
        this.ctrlTime = new ControllerTime(controller, new TimeUI());
        this.ctrlStatistics = new ControllerStatistics(controller, new StatisticsUI(), this);
    }

    // métodos de acesso partilhado usados pelos sub-controladores
    public User getLoggedUser() {
        return loggedUser;
    }

    // convertemos dia/hora/minuto para minutos totais para facilitar comparações
    // é assim que o simulador de tempo funciona internamente
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

    private void handleLogin() {
        String[] data = mainUI.readLoginData();
        // procuramos o utilizador pelo email
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
        // quando o utilizador faz logout limpamos a referência
        loggedUser = null;
    }

    private void handleRegister() {
        try {
            String[] data = mainUI.readRegisterData();
            // por defeito regista como utilizador regular
            controller.addUser(new RegularUser(data[0], data[1], data[2], data[3]));
            // depois perguntamos se quer promover a admin
            if (mainUI.askPromoteToAdmin()) {
                ctrlUsers.promoteUserRegister(data[0]);
            }
            StateManager.save(controller);
            mainUI.showSuccess("Utilizador '" + data[1] + "' registado. Pode fazer login.");
        } catch (IllegalArgumentException e) {
            mainUI.showError(e.getMessage());
        }
    }

    private boolean isAdmin() {
        return loggedUser.isAdmin();
    }

    private void handleDashboard() {
        int choice;
        do {
            boolean admin = isAdmin();
            choice = mainUI.showDashboard(loggedUser.getName(), admin);
            // mostramos menus diferentes consoante o tipo de utilizador
            if (admin) {
                switch (choice) {
                    case 1 -> {
                        ctrlHouses.handle();
                        StateManager.save(controller);
                    }
                    case 2 -> {
                        ctrlDevices.handle();
                        StateManager.save(controller);
                    }
                    case 3 -> {
                        ctrlScenarios.handle();
                        StateManager.save(controller);
                    }
                    case 4 -> {
                        ctrlAutomations.handle();
                        StateManager.save(controller);
                    }
                    case 5 -> {
                        ctrlTime.handle();
                        StateManager.save(controller);
                    }
                    case 6 -> {
                        ctrlUsers.handle();
                        StateManager.save(controller);
                    }
                    case 7 -> {
                        ctrlStatistics.handle();
                        StateManager.save(controller);
                    }
                }
            } else {
                switch (choice) {
                    case 1 -> ctrlHouses.viewHousesOnly();
                    case 2 -> {
                        ctrlDevices.handleGuest();
                        StateManager.save(controller);
                    }
                    case 3 -> {
                        ctrlScenarios.handle();
                        StateManager.save(controller);
                    }
                    case 4 -> {
                        ctrlTime.handle();
                        StateManager.save(controller);
                    }
                }
            }
            // a condição de saída é diferente para admin (8) e utilizador normal (5)
        } while (isAdmin() ? choice != 8 : choice != 5);
    }
}