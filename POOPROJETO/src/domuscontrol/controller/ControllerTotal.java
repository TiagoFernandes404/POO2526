package domuscontrol.controller;

import domuscontrol.model.automation.Automation;
import domuscontrol.model.automation.SensorCondition;
import domuscontrol.model.automation.SensorCondition.Operator;
import domuscontrol.model.devices.*;
import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.model.scenario.*;
import domuscontrol.model.scheduling.Schedule;
import domuscontrol.model.users.User;
import domuscontrol.view.*;
import java.util.List;

public class ControllerTotal {

    private final DomusController controller;
    private final MainUI mainUI;
    private final UserUI userUI;
    private final HouseUI houseUI;
    private final DeviceUI deviceUI;
    private final ScenarioUI scenarioUI;
    private final AutomationUI automationUI;
    private final TimeUI timeUI;

    private User loggedUser;

    public ControllerTotal() {
        DomusController loaded = StateManager.load();
        this.controller = (loaded != null) ? loaded : new DomusController();
        this.mainUI       = new MainUI();
        this.userUI       = new UserUI();
        this.houseUI      = new HouseUI();
        this.deviceUI     = new DeviceUI();
        this.scenarioUI   = new ScenarioUI();
        this.automationUI = new AutomationUI();
        this.timeUI       = new TimeUI();
    }

    // Converte o tempo atual da simulação em minutos totais (para estatísticas)
    private int currentTotalMinutes() {
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

    // =========================================================================
    // LOGIN E REGISTO
    // =========================================================================

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
            if (controller.getUserById(data[0]) != null) {
                mainUI.showError("Já existe um utilizador com o ID '" + data[0] + "'.");
                return;
            }
            boolean emailExists = controller.getUsers().stream()
                    .anyMatch(u -> u.getEmail().equals(data[2]));
            if (emailExists) {
                mainUI.showError("Já existe um utilizador com o email '" + data[2] + "'.");
                return;
            }
            controller.addUser(new User(data[0], data[1], data[2], data[3], false));
            StateManager.save(controller);
            mainUI.showSuccess("Utilizador '" + data[1] + "' registado. Pode fazer login.");
        } catch (IllegalArgumentException e) {
            mainUI.showError(e.getMessage());
        }
    }

    // =========================================================================
    // DASHBOARD
    // =========================================================================

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
                    case 1 -> { handleHouses();        StateManager.save(controller); }
                    case 2 -> { handleDevices();       StateManager.save(controller); }
                    case 3 -> { handleScenarios();     StateManager.save(controller); }
                    case 4 -> { handleAutomations();   StateManager.save(controller); }
                    case 5 -> { handleTime();          StateManager.save(controller); }
                    case 6 -> { handleUsers();         StateManager.save(controller); }
                    case 7 -> { handleStatistics();    StateManager.save(controller); }
                    // case 8 = Logout
                }
            } else {
                switch (choice) {
                    case 1 -> viewHousesOnly();
                    case 2 -> { handleDevicesGuest();   StateManager.save(controller); }
                    case 3 -> { handleScenariosGuest(); StateManager.save(controller); }
                    case 4 -> { handleTime();            StateManager.save(controller); }
                    case 5 -> {
                        loggedUser.setAdmin(true);
                        StateManager.save(controller);
                        mainUI.showSuccess("És agora administrador!");
                    }
                    // case 6 = Logout
                }
            }
        } while (isAdmin() ? choice != 8 : choice != 6);
    }

    // =========================================================================
    // UTILIZADORES
    // =========================================================================

    private void handleUsers() {
        int choice;
        do {
            choice = userUI.showMenu();
            switch (choice) {
                case 1 -> createUser();
                case 2 -> listUsers();
                case 3 -> viewUserDetails();
            }
        } while (choice != 4);
    }

    private void createUser() {
        try {
            String[] data = userUI.readUserData();
            if (controller.getUserById(data[0]) != null) {
                userUI.showError("Já existe um utilizador com o ID '" + data[0] + "'.");
                return;
            }
            controller.addUser(new User(data[0], data[1], data[2], data[3], false));
            userUI.showSuccess("Utilizador '" + data[1] + "' criado.");
        } catch (IllegalArgumentException e) {
            userUI.showError(e.getMessage());
        }
    }

    private void listUsers() {
        userUI.displayUsers(controller.getUsers());
    }

    private void viewUserDetails() {
        String id = userUI.readUserId();
        User user = controller.getUserById(id);
        if (user == null) { userUI.showError("Utilizador não encontrado."); return; }
        userUI.displayUser(user);
    }

    // =========================================================================
    // CASAS
    // =========================================================================

    private void handleHouses() {
        int choice;
        do {
            choice = houseUI.showMenu();
            switch (choice) {
                case 1 -> createHouse();
                case 2 -> addRoom();
                case 3 -> listHouses();
                case 4 -> viewHouseDetails();
                case 5 -> addGuestToHouse();
            }
        } while (choice != 6);
    }

    private void viewHousesOnly() {
        houseUI.displayHouses(loggedUser.getAllHouses());
    }

    private void createHouse() {
        try {
            String[] data = houseUI.readHouseData();
            if (controller.getHouseById(data[0]) != null) {
                houseUI.showError("Já existe uma casa com esse ID."); return;
            }
            User owner = controller.getUserById(data[3]);
            if (owner == null) { houseUI.showError("Utilizador não encontrado."); return; }
            House house = new House(data[0], data[1], data[2]);
            controller.addHouse(house);
            owner.addOwnedHouse(house);
            houseUI.showSuccess("Casa '" + data[1] + "' criada.");
        } catch (IllegalArgumentException e) {
            houseUI.showError(e.getMessage());
        }
    }

    private void addRoom() {
        try {
            String[] data = houseUI.readRoomData();
            House house = controller.getHouseById(data[0]);
            if (house == null) { houseUI.showError("Casa não encontrada."); return; }
            if (!loggedUser.isAdminOf(house)) {
                houseUI.showError("Não tens permissão para gerir esta casa."); return;
            }
            if (house.getRoomByName(data[1]) != null) {
                houseUI.showError("Já existe uma divisão com esse nome."); return;
            }
            house.addRoom(new Room(data[1]));
            houseUI.showSuccess("Divisão '" + data[1] + "' adicionada.");
        } catch (IllegalArgumentException e) {
            houseUI.showError(e.getMessage());
        }
    }

    private void listHouses() {
        houseUI.displayHouses(controller.getHouses());
    }

    private void viewHouseDetails() {
        String id = houseUI.readHouseId();
        House house = controller.getHouseById(id);
        if (house == null) { houseUI.showError("Casa não encontrada."); return; }
        houseUI.displayHouseDetails(house);
    }

    private void addGuestToHouse() {
        try {
            String[] data = houseUI.readGuestData();
            House house = controller.getHouseById(data[0]);
            if (house == null) { houseUI.showError("Casa não encontrada."); return; }
            if (!loggedUser.isAdminOf(house)) {
                houseUI.showError("Não tens permissão para gerir esta casa."); return;
            }
            User guest = controller.getUserById(data[1]);
            if (guest == null) { houseUI.showError("Utilizador não encontrado."); return; }
            guest.addGuestHouse(house);
            houseUI.showSuccess(guest.getName() + " adicionado como convidado.");
        } catch (IllegalArgumentException e) {
            houseUI.showError(e.getMessage());
        }
    }

    // =========================================================================
    // DISPOSITIVOS
    // =========================================================================

    private void handleDevices() {
        int choice;
        do {
            choice = deviceUI.showMenu();
            switch (choice) {
                case 1 -> createDevice();
                case 2 -> addDeviceToRoom();
                case 3 -> listDevices();
                case 4 -> viewDeviceStatus();
                case 5 -> turnOnDevice();
                case 6 -> turnOffDevice();
                case 7 -> controlDeviceAttributes();
            }
        } while (choice != 8);
    }

    private void handleDevicesGuest() {
        Menu guestMenu = new Menu("Dispositivos", new String[]{
            "Listar dispositivos",
            "Ver estado de dispositivo",
            "Ligar dispositivo",
            "Desligar dispositivo",
            "Voltar"
        });
        int choice;
        do {
            choice = guestMenu.show();
            switch (choice) {
                case 1 -> listDevicesOfUser();
                case 2 -> viewDeviceStatus();
                case 3 -> turnOnDevice();
                case 4 -> turnOffDevice();
            }
        } while (choice != 5);
    }

    private void listDevicesOfUser() {
        loggedUser.getAllHouses().forEach(h ->
            h.getAllDevices().forEach(d -> System.out.println(d.getStatus()))
        );
    }

    private void createDevice() {
        try {
            String[] data = deviceUI.readDeviceData();
            String type  = data[0]; String id    = data[1];
            String brand = data[2]; String model = data[3];
            double power = Double.parseDouble(data[4]);
            if (controller.getDeviceById(id) != null) {
                deviceUI.showError("Já existe um dispositivo com esse ID."); return;
            }
            Device device = switch (type) {
                case "1" -> new Relay(id, brand, model, power);
                case "2" -> new SmartLight(id, brand, model, power, data[5].equalsIgnoreCase("s"));
                case "3" -> new SmartSpeaker(id, brand, model, power);
                case "4" -> new Blinds(id, brand, model, power);
                case "5" -> new GarageDoor(id, brand, model, power);
                case "6" -> new RainSensor(id, brand, model, power);
                case "7" -> new LightSensor(id, brand, model, power);
                case "8" -> new TemperatureSensor(id, brand, model, power);
                default  -> throw new IllegalArgumentException("Tipo inválido.");
            };
            controller.addDevice(device);
            deviceUI.showSuccess("Dispositivo '" + id + "' criado.");
        } catch (IllegalArgumentException e) {
            deviceUI.showError(e.getMessage());
        }
    }

    private void addDeviceToRoom() {
        try {
            String[] data = deviceUI.readAddToRoomData();
            House house = controller.getHouseById(data[0]);
            if (house == null) { deviceUI.showError("Casa não encontrada."); return; }
            if (!loggedUser.isAdminOf(house)) {
                deviceUI.showError("Não tens permissão para gerir esta casa."); return;
            }
            Room room = house.getRoomByName(data[1]);
            if (room == null) { deviceUI.showError("Divisão não encontrada."); return; }
            Device device = controller.getDeviceById(data[2]);
            if (device == null) { deviceUI.showError("Dispositivo não encontrado."); return; }
            room.addDevice(device);
            deviceUI.showSuccess("Dispositivo adicionado à divisão '" + data[1] + "'.");
        } catch (IllegalArgumentException e) {
            deviceUI.showError(e.getMessage());
        }
    }

    private void listDevices() {
        deviceUI.displayDevices(controller.getAllDevices());
    }

    private void viewDeviceStatus() {
        String id = deviceUI.readDeviceId();
        Device device = controller.getDeviceById(id);
        if (device == null) { deviceUI.showError("Dispositivo não encontrado."); return; }
        deviceUI.displayDevice(device);
    }

    // ATUALIZADO: regista o tempo de início com o tempo atual da simulação
    private void turnOnDevice() {
        String id = deviceUI.readDeviceId();
        Device device = controller.getDeviceById(id);
        if (device == null) { deviceUI.showError("Dispositivo não encontrado."); return; }
        device.turnOn(currentTotalMinutes());
        deviceUI.showSuccess("Dispositivo '" + id + "' ligado.");
    }

    // ATUALIZADO: acumula o tempo ligado com o tempo atual da simulação
    private void turnOffDevice() {
        String id = deviceUI.readDeviceId();
        Device device = controller.getDeviceById(id);
        if (device == null) { deviceUI.showError("Dispositivo não encontrado."); return; }
        device.turnOff(currentTotalMinutes());
        deviceUI.showSuccess("Dispositivo '" + id + "' desligado.");
    }

    private void controlDeviceAttributes() {
        String id = deviceUI.readDeviceId();
        Device device = controller.getDeviceById(id);
        if (device == null) { deviceUI.showError("Dispositivo não encontrado."); return; }
        int choice = deviceUI.showControlMenu();
        try {
            switch (choice) {
                case 1 -> {
                    if (!(device instanceof SmartLight l)) { deviceUI.showError("Não é lâmpada."); return; }
                    l.setBrightness(deviceUI.readBrightness());
                    deviceUI.showSuccess("Brilho definido.");
                }
                case 2 -> {
                    if (!(device instanceof SmartLight l) || !l.hasColorSupport()) {
                        deviceUI.showError("Lâmpada sem suporte de cor."); return;
                    }
                    l.setColorTemperature(deviceUI.readColorTemperature());
                    deviceUI.showSuccess("Temperatura de cor definida.");
                }
                case 3 -> {
                    if (!(device instanceof SmartSpeaker s)) { deviceUI.showError("Não é coluna."); return; }
                    s.setVolume(deviceUI.readVolume());
                    deviceUI.showSuccess("Volume definido.");
                }
                case 4 -> {
                    if (!(device instanceof ApertureDevice a)) { deviceUI.showError("Sem controlo de abertura."); return; }
                    a.setOpeningPercentage(deviceUI.readOpeningPercentage());
                    deviceUI.showSuccess("Abertura definida.");
                }
            }
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            deviceUI.showError(e.getMessage());
        }
    }

    // =========================================================================
    // ESTATÍSTICAS (novo)
    // =========================================================================

    private void handleStatistics() {
        Menu statsMenu = new Menu("Estatísticas", new String[]{
            "Casa que mais consome",
            "Top 3 dispositivos por ativações",
            "Top 3 dispositivos por tempo ligado",
            "Top 3 divisões com mais dispositivos",
            "Voltar"
        });
        int choice;
        do {
            choice = statsMenu.show();
            switch (choice) {
                case 1 -> showHighestConsumingHouse();
                case 2 -> showTopDevicesByActivations();
                case 3 -> showTopDevicesByTime();
                case 4 -> showTopRooms();
            }
        } while (choice != 5);
    }

    private void showHighestConsumingHouse() {
        var house = controller.getHighestConsumingHouse();
        if (house == null) { Menu.showMessage("Sem casas registadas."); return; }
        Menu.showMessage("Casa que mais consome: " + house.getName()
                + " — " + house.getTotalPowerConsumption() + " Wh");
    }

    private void showTopDevicesByActivations() {
        List<Device> top = controller.getTopDevicesByActivations(3);
        if (top.isEmpty()) { Menu.showMessage("Sem dispositivos registados."); return; }
        Menu.showMessage("Top 3 por ativações:");
        for (int i = 0; i < top.size(); i++) {
            Device d = top.get(i);
            Menu.showMessage((i + 1) + ". " + d.getId() + " — " + d.getActivationCount() + " ativações");
        }
    }

    private void showTopDevicesByTime() {
        List<Device> top = controller.getTopDevicesByTime(3);
        if (top.isEmpty()) { Menu.showMessage("Sem dispositivos registados."); return; }
        Menu.showMessage("Top 3 por tempo ligado:");
        for (int i = 0; i < top.size(); i++) {
            Device d = top.get(i);
            Menu.showMessage((i + 1) + ". " + d.getId() + " — " + d.getTotalOnTime() + " minutos");
        }
    }

    private void showTopRooms() {
        List<String> top = controller.getTopRoomsByDeviceCount(3);
        if (top.isEmpty()) { Menu.showMessage("Sem divisões registadas."); return; }
        Menu.showMessage("Top 3 divisões com mais dispositivos:");
        for (int i = 0; i < top.size(); i++) {
            Menu.showMessage((i + 1) + ". " + top.get(i));
        }
    }

    // =========================================================================
    // CENÁRIOS
    // =========================================================================

    private void handleScenarios() {
        int choice;
        do {
            choice = scenarioUI.showMenu();
            switch (choice) {
                case 1 -> createScenario();
                case 2 -> addActionToScenario();
                case 3 -> listScenarios();
                case 4 -> executeScenario();
            }
        } while (choice != 5);
    }

    private void handleScenariosGuest() {
        Menu guestMenu = new Menu("Cenários", new String[]{
            "Criar cenário",
            "Adicionar ação a cenário",
            "Listar cenários",
            "Executar cenário",
            "Voltar"
        });
        int choice;
        do {
            choice = guestMenu.show();
            switch (choice) {
                case 1 -> createScenario();
                case 2 -> addActionToScenario();
                case 3 -> listScenarios();
                case 4 -> executeScenario();
            }
        } while (choice != 5);
    }

    private void createScenario() {
        try {
            String[] data = scenarioUI.readScenarioData();
            User owner = controller.getUserById(data[1]);
            if (owner == null) { scenarioUI.showError("Utilizador não encontrado."); return; }
            controller.addScenario(new Scenario(data[0], owner));
            scenarioUI.showSuccess("Cenário '" + data[0] + "' criado.");
        } catch (IllegalArgumentException e) {
            scenarioUI.showError(e.getMessage());
        }
    }

    private void addActionToScenario() {
        String scenarioName = scenarioUI.readScenarioName();
        Scenario scenario = controller.getScenarios().stream()
                .filter(s -> s.getName().equals(scenarioName))
                .findFirst().orElse(null);
        if (scenario == null) { scenarioUI.showError("Cenário não encontrado."); return; }
        int actionType = scenarioUI.showActionMenu();
        if (actionType == 6) return;
        String[] actionData = scenarioUI.readActionData();
        Device device = controller.getDeviceById(actionData[0]);
        if (device == null) { scenarioUI.showError("Dispositivo não encontrado."); return; }
        try {
            switch (actionType) {
                case 1 -> scenario.addAction(new TurnOnAction(device));
                case 2 -> scenario.addAction(new TurnOffAction(device));
                case 3 -> {
                    if (!(device instanceof SmartLight l)) { scenarioUI.showError("Não é lâmpada."); return; }
                    scenario.addAction(new SetBrightnessAction(l, Integer.parseInt(actionData[1])));
                }
                case 4 -> {
                    if (!(device instanceof SmartSpeaker s)) { scenarioUI.showError("Não é coluna."); return; }
                    scenario.addAction(new SetVolumeAction(s, Integer.parseInt(actionData[1])));
                }
                case 5 -> {
                    if (!(device instanceof ApertureDevice a)) { scenarioUI.showError("Sem abertura."); return; }
                    scenario.addAction(new SetOpeningAction(a, Integer.parseInt(actionData[1])));
                }
            }
            scenarioUI.showSuccess("Ação adicionada.");
        } catch (NumberFormatException e) {
            scenarioUI.showError("Valor numérico inválido.");
        } catch (IllegalArgumentException e) {
            scenarioUI.showError(e.getMessage());
        }
    }

    private void listScenarios() {
        scenarioUI.displayScenarios(controller.getScenarios());
    }

    private void executeScenario() {
        String name = scenarioUI.readScenarioName();
        try {
            controller.executeScenario(name);
            scenarioUI.showSuccess("Cenário '" + name + "' executado.");
        } catch (IllegalArgumentException e) {
            scenarioUI.showError(e.getMessage());
        }
    }

    // =========================================================================
    // AUTOMAÇÕES E ESCALONAMENTOS
    // =========================================================================

    private void handleAutomations() {
        int choice;
        do {
            choice = automationUI.showMenu();
            switch (choice) {
                case 1 -> createAutomation();
                case 2 -> listAutomations();
                case 3 -> evaluateAutomations();
                case 4 -> createSchedule();
                case 5 -> listSchedules();
            }
        } while (choice != 6);
    }

    private void createAutomation() {
        try {
            String[] data = automationUI.readAutomationData();
            Device sensorDevice = controller.getDeviceById(data[1]);
            if (sensorDevice == null) { automationUI.showError("Sensor não encontrado."); return; }
            if (!(sensorDevice instanceof Sensor sensor)) {
                automationUI.showError("O dispositivo não é um sensor."); return;
            }
            Operator op = switch (data[2]) {
                case "1" -> Operator.GREATER_THAN;
                case "2" -> Operator.LESS_THAN;
                case "3" -> Operator.EQUALS;
                default  -> throw new IllegalArgumentException("Operador inválido.");
            };
            double threshold = Double.parseDouble(data[3]);
            Automation automation = new Automation(data[0], new SensorCondition(sensor, op, threshold));
            String deviceId = automationUI.readDeviceId();
            Device target = controller.getDeviceById(deviceId);
            if (target == null) { automationUI.showError("Dispositivo não encontrado."); return; }
            automation.addAction(new TurnOnAction(target));
            controller.addAutomation(automation);
            automationUI.showSuccess("Automação '" + data[0] + "' criada.");
        } catch (NumberFormatException e) {
            automationUI.showError("Valor numérico inválido.");
        } catch (IllegalArgumentException e) {
            automationUI.showError(e.getMessage());
        }
    }

    private void listAutomations() {
        automationUI.displayAutomations(controller.getAutomations());
    }

    private void evaluateAutomations() {
        controller.evaluateAutomations();
        automationUI.showSuccess("Automações avaliadas.");
    }

    private void createSchedule() {
        try {
            String[] data = automationUI.readScheduleData();
            int hour = Integer.parseInt(data[1]);
            int minute = Integer.parseInt(data[2]);
            boolean repeating = data[3].equalsIgnoreCase("s");
            Schedule schedule = new Schedule(data[0], hour, minute, repeating);
            String deviceId = automationUI.readDeviceId();
            Device target = controller.getDeviceById(deviceId);
            if (target == null) { automationUI.showError("Dispositivo não encontrado."); return; }
            schedule.addAction(new TurnOnAction(target));
            controller.addSchedule(schedule);
            automationUI.showSuccess("Escalonamento '" + data[0] + "' criado.");
        } catch (NumberFormatException e) {
            automationUI.showError("Valor numérico inválido.");
        } catch (IllegalArgumentException e) {
            automationUI.showError(e.getMessage());
        }
    }

    private void listSchedules() {
        automationUI.displaySchedules(controller.getSchedules());
    }

    // =========================================================================
    // TEMPO
    // =========================================================================

    private void handleTime() {
        int choice;
        do {
            choice = timeUI.showMenu();
            switch (choice) {
                case 1 -> {
                    controller.tick();
                    timeUI.showSuccess("Tempo avançado 1 minuto.");
                    timeUI.displayTime(controller.getTime().toString());
                }
                case 2 -> {
                    int minutes = timeUI.readMinutes();
                    if (minutes <= 0) { Menu.showMessage("Deve ser positivo."); break; }
                    for (int i = 0; i < minutes; i++) controller.tick();
                    timeUI.showSuccess("Avançado " + minutes + " minutos.");
                    timeUI.displayTime(controller.getTime().toString());
                }
                case 3 -> timeUI.displayTime(controller.getTime().toString());
            }
        } while (choice != 4);
    }
}