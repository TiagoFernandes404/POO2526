package domuscontrol.controller;

import domuscontrol.model.automation.Automation;
import domuscontrol.model.devices.Device;
import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.model.scenario.Scenario;
import domuscontrol.model.scheduling.Schedule;
import domuscontrol.model.users.AdminUser;
import domuscontrol.model.users.User;
import domuscontrol.utils.TimeSimulator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
        this.time = new TimeSimulator(0, 0, 0);
    }

    // --- Utilizadores ---
    public void addUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("Utilizador não pode ser nulo.");
        if (users.contains(user))
            throw new IllegalArgumentException("Já existe um utilizador com o ID '" + user.getId() + "'.");
        if (users.stream().anyMatch(u -> u.getEmail().equals(user.getEmail())))
            throw new IllegalArgumentException("Já existe um utilizador com o email '" + user.getEmail() + "'.");
        users.add(user);
    }

    public User getUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean removeUser(String id) {
        return users.removeIf(u -> u.getId().equals(id));
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    // promover um utilizador a administrador
    public void promoteToAdmin(String id) {
        User existing = getUserById(id);
        if (existing == null)
            throw new IllegalArgumentException("Utilizador não encontrado: " + id);
        if (existing.isAdmin())
            throw new IllegalArgumentException("O utilizador já é administrador.");
        AdminUser promoted = existing.promoteToAdminUser();
        int index = users.indexOf(existing);
        users.set(index, promoted);

    }

    // --- Casas ---
    public void addHouse(House house) {
        if (house == null)
            throw new IllegalArgumentException("Casa não pode ser nula.");
        if (houses.contains(house))
            throw new IllegalArgumentException("Casa já existe.");
        houses.add(house);
    }

    public House getHouseById(String id) {
        return houses.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean removeHouse(String id) {
        return houses.removeIf(h -> h.getId().equals(id));
    }

    public List<House> getHouses() {
        return new ArrayList<>(houses);
    }

    // --- Dispositivos ---
    public void addDevice(Device device) {
        if (device == null)
            throw new IllegalArgumentException("Dispositivo não pode ser nulo.");
        if (devices.contains(device))
            throw new IllegalArgumentException("Dispositivo já existe.");
        devices.add(device);
    }

    public Device getDeviceById(String id) {
        return devices.stream().filter(d -> d.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean removeDevice(String id) {
        return devices.removeIf(d -> d.getId().equals(id));
    }

    public List<Device> getAllDevices() {
        return new ArrayList<>(devices);
    }

    // --- Automações ---
    public void addAutomation(Automation automation) {
        if (automation == null)
            throw new IllegalArgumentException("Automação não pode ser nula.");
        automations.add(automation);
    }

    public void evaluateAutomations() {
        int now = time.getDay() * 24 * 60 + time.getHour() * 60 + time.getMinute();
        for (Automation a : automations) {
            a.evaluate(now);
        }
    }

    public List<Automation> getAutomations() {
        return new ArrayList<>(automations);
    }

    // --- Escalonamentos ---
    public void addSchedule(Schedule schedule) {
        if (schedule == null)
            throw new IllegalArgumentException("Escalonamento não pode ser nulo.");
        schedules.add(schedule);
    }

    public void evaluateSchedules() {
        int totalMinutes = time.getDay() * 24 * 60 + time.getHour() * 60 + time.getMinute();
        for (Schedule s : schedules) {
            s.evaluate(time.getHour(), time.getMinute(), totalMinutes); // ← atualizado
        }
    }

    public List<Schedule> getSchedules() {
        return new ArrayList<>(schedules);
    }

    // --- Cenários ---
    public void addScenario(Scenario scenario) {
        if (scenario == null)
            throw new IllegalArgumentException("Cenário não pode ser nulo.");
        scenarios.add(scenario);
    }

    public void executeScenario(String name, int currentMinute) {
        scenarios.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cenário '" + name + "' não encontrado."))
                .execute(currentMinute);
    }

    public List<Scenario> getScenarios() {
        return new ArrayList<>(scenarios);
    }

    // --- Tempo ---
    public void tick() {
        time.tick();
        evaluateAutomations();
        evaluateSchedules();
    }

    public TimeSimulator getTime() {
        return time;
    }

    // =========================================================================
    // ESTATÍSTICAS
    // =========================================================================
    public House getHighestConsumingHouse() {
        return houses.stream()
                .max(Comparator.comparingDouble(House::getTotalPowerConsumption))
                .orElse(null);
    }

    public List<Device> getTopDevicesByActivations(int n) {
        return devices.stream()
                .sorted(Comparator.comparingInt(Device::getActivationCount).reversed())
                .limit(n).toList();
    }

    public List<Device> getTopDevicesByTime(int n, int currentTotalMinutes) {
        return devices.stream()
                .sorted(Comparator.comparingInt(
                        (Device d) -> d.getEffectiveTotalOnTime(currentTotalMinutes)).reversed())
                .limit(n)
                .toList();
    }

    public double getTotalSystemConsumption() {
        return houses.stream().mapToDouble(House::getTotalPowerConsumption).sum();
    }

    public User getUserWithMostHouses() {
        return users.stream()
                .max(Comparator.comparingInt(u -> u.getAllHouses().size()))
                .orElse(null);
    }

    public List<String> getTopRoomsByDeviceCount(int n) {
        return houses.stream()
                .flatMap(house -> house.getRooms().stream().map(room -> Map.entry(house, room)))
                .sorted(Comparator.comparingInt((Map.Entry<House, Room> e) -> e.getValue().getDeviceCount()).reversed())
                .limit(n)
                .map(e -> e.getKey().getName() + " › " + e.getValue().getName() + " (" + e.getValue().getDeviceCount()
                        + " dispositivos)")
                .toList();
    }

    public List<Device> getTopDevicesByActivationsByHouse(House house, int n, int currentTotalMinutes) {
        if (house == null)
            throw new IllegalArgumentException("Casa não pode ser nula.");
        return house.getAllDevices().stream()
                .sorted(Comparator.comparingInt(Device::getActivationCount).reversed())
                .limit(n)
                .toList();
    }

    public List<Device> getTopDevicesByTimeByHouse(House house, int n, int currentTotalMinutes) {
        if (house == null)
            throw new IllegalArgumentException("Casa não pode ser nula.");
        return house.getAllDevices().stream()
                .sorted(Comparator.comparingInt(
                        (Device d) -> d.getEffectiveTotalOnTime(currentTotalMinutes)).reversed())
                .limit(n)
                .toList();
    }
}