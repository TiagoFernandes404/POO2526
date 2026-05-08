package domuscontrol.controller;

import domuscontrol.model.automation.Automation;
import domuscontrol.model.automation.SensorCondition;
import domuscontrol.model.automation.SensorCondition.Operator;
import domuscontrol.model.devices.*;
import domuscontrol.model.scheduling.Schedule;
import domuscontrol.utils.actions.*;
import domuscontrol.view.AutomationUI;
import java.util.List;

public class ControllerAutomations {

    private final DomusController model;
    private final AutomationUI automationUI;

    public ControllerAutomations(DomusController model, AutomationUI automationUI) {
        this.model = model;
        this.automationUI = automationUI;
    }

    public void handle() {
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

    private List<String> deviceList() {
        return model.getAllDevices().stream()
                .map(d -> "[" + d.getId() + "] " + d.getBrand() + " " + d.getModel()
                        + " (" + d.getClass().getSimpleName() + ")")
                .toList();
    }

    private void createAutomation() {
        try {
            String[] data = automationUI.readAutomationData();

            Device sensorDevice = model.getDeviceById(data[1]);
            if (sensorDevice == null) {
                automationUI.showError("Sensor não encontrado.");
                return;
            }
            if (!(sensorDevice instanceof Sensor sensor)) {
                automationUI.showError("O dispositivo não é um sensor.");
                return;
            }

            Operator op = switch (data[2]) {
                case "1" -> Operator.GREATER_THAN;
                case "2" -> Operator.LESS_THAN;
                case "3" -> Operator.EQUALS;
                default -> throw new IllegalArgumentException("Operador inválido.");
            };

            Automation automation = new Automation(data[0],
                    new SensorCondition(sensor, op, Double.parseDouble(data[3])));

            boolean addMore = true;
            while (addMore) {
                String deviceId = automationUI.readDeviceId(deviceList());
                Device target = model.getDeviceById(deviceId);
                if (target == null) {
                    automationUI.showError("Dispositivo não encontrado.");
                    return;
                }
                addActionToAutomation(automation, target);
                addMore = automationUI.askAddMoreActions();
            }

            model.addAutomation(automation);
            automationUI.showSuccess("Automação '" + data[0] + "' criada.");
        } catch (NumberFormatException e) {
            automationUI.showError("Valor numérico inválido.");
        } catch (IllegalArgumentException e) {
            automationUI.showError(e.getMessage());
        }
    }

    private void addActionToAutomation(Automation automation, Device target) {
        int actionType = automationUI.showActionMenu(target);
        try {
            switch (actionType) {
                case 1 -> automation.addAction(new TurnOnAction(target));
                case 2 -> automation.addAction(new TurnOffAction(target));
                case 3 -> {
                    if (target instanceof ApertureDevice a)
                        automation.addAction(new SetOpeningAction(a, automationUI.readOpeningPercentage()));
                }
                case 4 -> {
                    if (target instanceof Dimmable d)
                        automation.addAction(new SetBrightnessAction(d, automationUI.readBrightness()));
                }
                case 5 -> {
                    if (target instanceof SmartSpeaker s)
                        automation.addAction(new SetVolumeAction(s, automationUI.readVolume()));
                }
            }
        } catch (IllegalArgumentException e) {
            automationUI.showError(e.getMessage());
        }
    }

    private void addActionToSchedule(Schedule schedule, Device target) {
        int actionType = automationUI.showActionMenu(target);
        try {
            switch (actionType) {
                case 1 -> schedule.addAction(new TurnOnAction(target));
                case 2 -> schedule.addAction(new TurnOffAction(target));
                case 3 -> {
                    if (target instanceof ApertureDevice a)
                        schedule.addAction(new SetOpeningAction(a, automationUI.readOpeningPercentage()));
                }
                case 4 -> {
                    if (target instanceof Dimmable d)
                        schedule.addAction(new SetBrightnessAction(d, automationUI.readBrightness()));
                }
                case 5 -> {
                    if (target instanceof SmartSpeaker s)
                        schedule.addAction(new SetVolumeAction(s, automationUI.readVolume()));
                }
            }
        } catch (IllegalArgumentException e) {
            automationUI.showError(e.getMessage());
        }
    }

    private void listAutomations() {
        automationUI.displayAutomations(model.getAutomations().stream().map(Object::toString).toList());
    }

    private void evaluateAutomations() {
        model.evaluateAutomations();
        automationUI.showSuccess("Automações avaliadas.");
    }

    private void createSchedule() {
        try {
            String[] data = automationUI.readScheduleData();
            Schedule schedule = new Schedule(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]),
                    data[3].equalsIgnoreCase("s"));

            boolean addMore = true;
            while (addMore) {
                String deviceId = automationUI.readDeviceId(deviceList());
                Device target = model.getDeviceById(deviceId);
                if (target == null) {
                    automationUI.showError("Dispositivo não encontrado.");
                    return;
                }
                addActionToSchedule(schedule, target);
                addMore = automationUI.askAddMoreActions();
            }

            model.addSchedule(schedule);
            automationUI.showSuccess("Escalonamento '" + data[0] + "' criado.");
        } catch (NumberFormatException e) {
            automationUI.showError("Valor numérico inválido.");
        } catch (IllegalArgumentException e) {
            automationUI.showError(e.getMessage());
        }
    }

    private void listSchedules() {
        automationUI.displaySchedules(model.getSchedules().stream().map(Object::toString).toList());
    }
}