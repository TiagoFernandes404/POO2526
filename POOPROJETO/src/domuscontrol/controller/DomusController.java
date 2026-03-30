package domuscontrol.controller;

import domuscontrol.model.automation.Automation;
import domuscontrol.model.devices.Device;
import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.model.scenario.Scenario;
import domuscontrol.model.scheduling.Schedule;
import domuscontrol.model.users.User;
import domuscontrol.utils.TimeSimulator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public void addUser(User user) {
        if (user == null) throw new IllegalArgumentException("Utilizador não pode ser nulo.");
        users.add(user);
    }

    public User getUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    public List<User> getUsers() { return new ArrayList<>(users); }

    // --- Casas ---

    public void addHouse(House house) {
        if (house == null) throw new IllegalArgumentException("Casa não pode ser nula.");
        houses.add(house);
    }

    public House getHouseById(String id) {
        return houses.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
    }

    public List<House> getHouses() { return new ArrayList<>(houses); }

    // --- Dispositivos ---

    public void addDevice(Device device) {
        if (device == null) throw new IllegalArgumentException("Dispositivo não pode ser nulo.");
        devices.add(device);
    }

    public Device getDeviceById(String id) {
        return devices.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Device> getAllDevices() { return new ArrayList<>(devices); }

    // --- Automações ---

    public void addAutomation(Automation automation) {
        if (automation == null) throw new IllegalArgumentException("Automação não pode ser nula.");
        automations.add(automation);
    }

    public void evaluateAutomations() {
        for (Automation a : automations) {
            a.evaluate();
        }
    }

    public List<Automation> getAutomations() { return new ArrayList<>(automations); }

    // --- Escalonamentos ---

    public void addSchedule(Schedule schedule) {
        if (schedule == null) throw new IllegalArgumentException("Escalonamento não pode ser nulo.");
        schedules.add(schedule);
    }

    public void evaluateSchedules() {
        for (Schedule s : schedules) {
            s.evaluate(time.getHour(), time.getMinute());
        }
    }

    public List<Schedule> getSchedules() { return new ArrayList<>(schedules); }

    // --- Cenários ---

    public void addScenario(Scenario scenario) {
        if (scenario == null) throw new IllegalArgumentException("Cenário não pode ser nulo.");
        scenarios.add(scenario);
    }

    public void executeScenario(String name) {
        for (Scenario s : scenarios) {
            if (s.getName().equals(name)) {
                s.execute();
                return;
            }
        }
        throw new IllegalArgumentException("Cenário '" + name + "' não encontrado.");
    }

    public List<Scenario> getScenarios() { return new ArrayList<>(scenarios); }

    // --- Tempo ---

    public void tick() {
        time.tick();
        evaluateAutomations();
        evaluateSchedules();
    }

    public TimeSimulator getTime() { return time; }

    // =========================================================================
    // ESTATÍSTICAS
    // =========================================================================

    // Retorna a casa com maior consumo atual (dispositivos ligados)
    public House getHighestConsumingHouse() {
        return houses.stream()
                .max(Comparator.comparingDouble(House::getTotalPowerConsumption))
                .orElse(null);
    }

    // Retorna os n dispositivos mais utilizados por número de ativações
    public List<Device> getTopDevicesByActivations(int n) {
        List<Device> sorted = new ArrayList<>(devices);
        sorted.sort(Comparator.comparingInt(Device::getActivationCount).reversed());
        return sorted.subList(0, Math.min(n, sorted.size()));
    }

    // Retorna os n dispositivos mais utilizados por tempo ligado (em minutos)
    public List<Device> getTopDevicesByTime(int n) {
        List<Device> sorted = new ArrayList<>(devices);
        sorted.sort(Comparator.comparingInt(Device::getTotalOnTime).reversed());
        return sorted.subList(0, Math.min(n, sorted.size()));
    }

    // Retorna o consumo total do sistema (soma de todos os dispositivos ligados em todas as casas)
    public double getTotalSystemConsumption() {
        return houses.stream()
                .mapToDouble(House::getTotalPowerConsumption)
                .sum();
    }

    // Retorna o utilizador com mais casas (próprias + usufrutuárias)
    public User getUserWithMostHouses() {
        return users.stream()
                .max(Comparator.comparingInt(u -> u.getAllHouses().size()))
                .orElse(null);
    }

    // Retorna as n divisões com mais dispositivos (com o nome da casa)
    public List<String> getTopRoomsByDeviceCount(int n) {
        List<String> result = new ArrayList<>();
        for (House house : houses) {
            for (Room room : house.getRooms()) {
                result.add(house.getName() + " › " + room.getName() + " (" + room.getDeviceCount() + " dispositivos)");
            }
        }
        result.sort((a, b) -> {
            int countA = Integer.parseInt(a.replaceAll(".*\\((\\d+).*", "$1"));
            int countB = Integer.parseInt(b.replaceAll(".*\\((\\d+).*", "$1"));
            return Integer.compare(countB, countA);
        });
        return result.subList(0, Math.min(n, result.size()));
    }
}