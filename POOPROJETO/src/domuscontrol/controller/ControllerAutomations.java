package domuscontrol.controller;

import domuscontrol.model.automation.Automation;
import domuscontrol.model.automation.SensorCondition;
import domuscontrol.model.automation.SensorCondition.Operator;
import domuscontrol.model.devices.*;
import domuscontrol.model.scheduling.Schedule;
import domuscontrol.utils.actions.*;
import domuscontrol.view.AutomationUI;
import java.util.List;

// controlador responsável por gerir tudo o que tem a ver com automações e escalonamentos
// separámos isto do controlador principal para não ficar tudo num sítio só
public class ControllerAutomations {

    // precisamos do modelo para aceder aos dispositivos e guardar as automações
    private final DomusController model;
    // a view para mostrar menus e ler dados do utilizador
    private final AutomationUI automationUI;

    public ControllerAutomations(DomusController model, AutomationUI automationUI) {
        this.model = model;
        this.automationUI = automationUI;
    }

    // loop principal do menu de automações e escalonamentos
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

    // método auxiliar para mostrar a lista de dispositivos disponíveis
    // usamos getSimpleName() para mostrar o tipo (ex: SmartLight, Relay...)
    private List<String> deviceList() {
        return model.getAllDevices().stream()
                .map(d -> "[" + d.getId() + "] " + d.getBrand() + " " + d.getModel()
                        + " (" + d.getClass().getSimpleName() + ")")
                .toList();
    }

    private void createAutomation() {
        try {
            String[] data = automationUI.readAutomationData();

            // verificamos se o dispositivo existe e se é mesmo um sensor
            // se não for sensor não faz sentido usar como condição
            Device sensorDevice = model.getDeviceById(data[1]);
            if (sensorDevice == null) {
                automationUI.showError("Sensor não encontrado.");
                return;
            }
            if (!(sensorDevice instanceof Sensor sensor)) {
                automationUI.showError("O dispositivo não é um sensor.");
                return;
            }

            // convertemos a escolha do utilizador (1,2,3) para o operador correspondente
            Operator op = switch (data[2]) {
                case "1" -> Operator.GREATER_THAN;
                case "2" -> Operator.LESS_THAN;
                case "3" -> Operator.EQUALS;
                default -> throw new IllegalArgumentException("Operador inválido.");
            };

            // criamos a automação com a condição definida pelo utilizador
            Automation automation = new Automation(data[0],
                    new SensorCondition(sensor, op, Double.parseDouble(data[3])));

            // permitimos adicionar várias ações à mesma automação
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

    // aqui usamos instanceof com pattern matching para verificar que tipo de
    // dispositivo é o alvo e assim saber que ações fazem sentido para ele
    private void addActionToAutomation(Automation automation, Device target) {
        int actionType = automationUI.showActionMenu(target);
        try {
            switch (actionType) {
                case 1 -> automation.addAction(new TurnOnAction(target));
                case 2 -> automation.addAction(new TurnOffAction(target));
                case 3 -> {
                    // só faz sentido definir abertura se o dispositivo tiver essa capacidade
                    if (target instanceof ApertureDevice a)
                        automation.addAction(new SetOpeningAction(a, automationUI.readOpeningPercentage()));
                }
                case 4 -> {
                    // brilho só para lâmpadas que implementam Dimmable
                    if (target instanceof Dimmable d)
                        automation.addAction(new SetBrightnessAction(d, automationUI.readBrightness()));
                }
                case 5 -> {
                    // volume só para colunas
                    if (target instanceof SmartSpeaker s)
                        automation.addAction(new SetVolumeAction(s, automationUI.readVolume()));
                }
            }
        } catch (IllegalArgumentException e) {
            automationUI.showError(e.getMessage());
        }
    }

    // este método é igual ao de cima mas para escalonamentos
    // reutilizámos a lógica porque as ações são as mesmas, só muda onde são
    // guardadas
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

    // convertemos cada automação para string para mostrar na lista
    private void listAutomations() {
        automationUI.displayAutomations(model.getAutomations().stream().map(Object::toString).toList());
    }

    // avalia todas as automações - verifica se as condições se verificam
    // e executa as ações correspondentes se sim
    private void evaluateAutomations() {
        model.evaluateAutomations();
        automationUI.showSuccess("Automações avaliadas.");
    }

    private void createSchedule() {
        try {
            String[] data = automationUI.readScheduleData();
            // criamos o escalonamento com hora, minuto e se repete todos os dias
            Schedule schedule = new Schedule(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]),
                    data[3].equalsIgnoreCase("s"));

            // tal como nas automações, permitimos adicionar várias ações
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