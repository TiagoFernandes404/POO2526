package domuscontrol.controller;

import domuscontrol.model.automation.Automation;
import domuscontrol.model.devices.Device;
import domuscontrol.model.house.House;
import domuscontrol.model.scenario.Scenario;
import domuscontrol.model.scheduling.Schedule;
import domuscontrol.model.users.User;
import domuscontrol.utils.TimeSimulator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * Esta classe é o controlador principal do sistema DomusControl.
 * É aqui que estão guardadas todas as entidades do sistema (utilizadores, casas,
 * dispositivos, automações, escalonamentos e cenários) e é através desta classe
 * que se faz toda a gestão do sistema.
 * O método tick() avança o tempo e avalia automaticamente todas as automações
 * e escalonamentos, simulando o funcionamento real do sistema.
 */
public class DomusController implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<User> users;
    private final List<House> houses;
    private final List<Device> devices;
    private final List<Automation> automations;
    private final List<Schedule> schedules;
    private final List<Scenario> scenarios;
    private final TimeSimulator time;

    public DomusController() {
        this.users = new ArrayList<>();
        this.houses = new ArrayList<>();
        this.devices = new ArrayList<>();
        this.automations = new ArrayList<>();
        this.schedules = new ArrayList<>();
        this.scenarios = new ArrayList<>();
        this.time = new TimeSimulator(1, 0, 0);
    }

    // --- Utilizadores ---

    // Adiciona um utilizador ao sistema
    public void addUser(User user) {
        if (user == null) throw new IllegalArgumentException("Utilizador não pode ser nulo.");
        users.add(user);
    }

    // Procura utilizador por id
    public User getUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    // Retorna todos os utilizadores
    public List<User> getUsers() { return new ArrayList<>(users); }

    // --- Casas ---

    // Adiciona uma casa ao sistema
    public void addHouse(House house) {
        if (house == null) throw new IllegalArgumentException("Casa não pode ser nula.");
        houses.add(house);
    }

    // Procura casa por id
    public House getHouseById(String id) {
        return houses.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
    }

    // Retorna todas as casas
    public List<House> getHouses() { return new ArrayList<>(houses); }

    // --- Dispositivos ---

    // Regista um dispositivo no sistema
    public void addDevice(Device device) {
        if (device == null) throw new IllegalArgumentException("Dispositivo não pode ser nulo.");
        devices.add(device);
    }

    // Procura dispositivo por id
    public Device getDeviceById(String id) {
        return devices.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    // Retorna todos os dispositivos registados
    public List<Device> getAllDevices() { return new ArrayList<>(devices); }

    // --- Automações ---

    // Regista uma automação no sistema
    public void addAutomation(Automation automation) {
        if (automation == null) throw new IllegalArgumentException("Automação não pode ser nula.");
        automations.add(automation);
    }

    // Avalia todas as automações ativas
    public void evaluateAutomations() {
        for (Automation a : automations) {
            a.evaluate();
        }
    }

    // Retorna todas as automações
    public List<Automation> getAutomations() { return new ArrayList<>(automations); }

    // --- Escalonamentos ---

    // Regista um escalonamento no sistema
    public void addSchedule(Schedule schedule) {
        if (schedule == null) throw new IllegalArgumentException("Escalonamento não pode ser nulo.");
        schedules.add(schedule);
    }

    // Avalia todos os escalonamentos para a hora atual
    public void evaluateSchedules() {
        for (Schedule s : schedules) {
            s.evaluate(time.getHour(), time.getMinute());
        }
    }

    // Retorna todos os escalonamentos
    public List<Schedule> getSchedules() { return new ArrayList<>(schedules); }

    // --- Cenários ---

    // Regista um cenário no sistema
    public void addScenario(Scenario scenario) {
        if (scenario == null) throw new IllegalArgumentException("Cenário não pode ser nulo.");
        scenarios.add(scenario);
    }

    // Executa um cenário pelo nome
    public void executeScenario(String name) {
        for (Scenario s : scenarios) {
            if (s.getName().equals(name)) {
                s.execute();
                return;
            }
        }
        throw new IllegalArgumentException("Cenário '" + name + "' não encontrado.");
    }

    // Retorna todos os cenários
    public List<Scenario> getScenarios() { return new ArrayList<>(scenarios); }

    // --- Tempo ---

    // Avança o tempo um minuto e avalia automações e escalonamentos
    public void tick() {
        time.tick();
        evaluateAutomations();
        evaluateSchedules();
    }

    // Retorna o simulador de tempo
    public TimeSimulator getTime() { return time; }
}