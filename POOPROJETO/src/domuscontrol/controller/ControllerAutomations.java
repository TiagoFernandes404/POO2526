package domuscontrol.controller;

import domuscontrol.model.actions.TurnOnAction;
import domuscontrol.model.automation.Automation;
import domuscontrol.model.automation.SensorCondition;
import domuscontrol.model.automation.SensorCondition.Operator;
import domuscontrol.model.devices.Device;
import domuscontrol.model.devices.Sensor;
import domuscontrol.model.scheduling.Schedule;
import domuscontrol.view.AutomationUI;

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
                default  -> throw new IllegalArgumentException("Operador inválido.");
            };

            double threshold = Double.parseDouble(data[3]);
            Automation automation = new Automation(data[0], new SensorCondition(sensor, op, threshold));

            String deviceId = automationUI.readDeviceId();
            Device target = model.getDeviceById(deviceId);
            if (target == null) {
                automationUI.showError("Dispositivo não encontrado.");
                return;
            }

            automation.addAction(new TurnOnAction(target));
            model.addAutomation(automation);
            automationUI.showSuccess("Automação '" + data[0] + "' criada.");
        } catch (NumberFormatException e) {
            automationUI.showError("Valor numérico inválido.");
        } catch (IllegalArgumentException e) {
            automationUI.showError(e.getMessage());
        }
    }

    private void listAutomations() {
        automationUI.displayAutomations(model.getAutomations());
    }

    private void evaluateAutomations() {
        model.evaluateAutomations();
        automationUI.showSuccess("Automações avaliadas.");
    }

    private void createSchedule() {
        try {
            String[] data = automationUI.readScheduleData();
            int hour       = Integer.parseInt(data[1]);
            int minute     = Integer.parseInt(data[2]);
            boolean repeat = data[3].equalsIgnoreCase("s");

            Schedule schedule = new Schedule(data[0], hour, minute, repeat);

            String deviceId = automationUI.readDeviceId();
            Device target = model.getDeviceById(deviceId);
            if (target == null) {
                automationUI.showError("Dispositivo não encontrado.");
                return;
            }

            schedule.addAction(new TurnOnAction(target));
            model.addSchedule(schedule);
            automationUI.showSuccess("Escalonamento '" + data[0] + "' criado.");
        } catch (NumberFormatException e) {
            automationUI.showError("Valor numérico inválido.");
        } catch (IllegalArgumentException e) {
            automationUI.showError(e.getMessage());
        }
    }

    private void listSchedules() {
        automationUI.displaySchedules(model.getSchedules());
    }
}