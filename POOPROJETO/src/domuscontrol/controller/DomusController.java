package domuscontrol.controller;

import domuscontrol.model.automation.Automation;
import domuscontrol.model.devices.Device;
import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.model.scenario.Scenario;
import domuscontrol.model.scheduling.Schedule;
import domuscontrol.model.users.User;
import domuscontrol.utils.TimeSimulator;
import java.util.ArrayList;
import java.util.List;
/*
 * Esta classe é o controlador principal do sistema DomusControl.
 * É aqui que estão guardadas todas as entidades do sistema (utilizadores, casas,
 * automações, escalonamentos e cenários) e é através desta classe que se faz
 * toda a gestão do sistema.
 * O método tick() avança o tempo e avalia automaticamente todas as automações
 * e escalonamentos, simulando o funcionamento real do sistema.
 */

public class DomusController {

    private List<User> users;
    private List<House> houses;
    private List<Automation> automations;
    private List<Schedule> schedules;
    private List<Scenario> scenarios;
    private TimeSimulator time;

    public DomusController() {
        this.users = new ArrayList<>();
        this.houses = new ArrayList<>();
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

    // --- Tempo ---

    // Avança o tempo e avalia automações e escalonamentos
    public void tick() {
        time.tick();
        evaluateAutomations();
        evaluateSchedules();
    }

    // Getters
    public List<User> getUsers() { return new ArrayList<>(users); }
    public List<House> getHouses() { return new ArrayList<>(houses); }
    public List<Automation> getAutomations() { return new ArrayList<>(automations); }
    public List<Schedule> getSchedules() { return new ArrayList<>(schedules); }
    public List<Scenario> getScenarios() { return new ArrayList<>(scenarios); }
    public TimeSimulator getTime() { return time; }
}