package domuscontrol.controller;

import domuscontrol.model.devices.ApertureDevice;
import domuscontrol.model.devices.Device;
import domuscontrol.model.devices.Dimmable;
import domuscontrol.model.devices.SmartSpeaker;
import domuscontrol.model.scenario.Scenario;
import domuscontrol.model.users.User;
import domuscontrol.utils.actions.SetBrightnessAction;
import domuscontrol.utils.actions.SetOpeningAction;
import domuscontrol.utils.actions.SetVolumeAction;
import domuscontrol.utils.actions.TurnOffAction;
import domuscontrol.utils.actions.TurnOnAction;
import domuscontrol.view.ScenarioUI;
import java.util.List;

// controlador dos cenários - um cenário é um conjunto de ações que o utilizador
// pode executar de uma vez só (ex: sair de casa desliga tudo)
public class ControllerScenarios {

    private final DomusController model;
    private final ScenarioUI scenarioUI;
    private final ControllerTotal main;

    public ControllerScenarios(DomusController model, ScenarioUI scenarioUI, ControllerTotal main) {
        this.model = model;
        this.scenarioUI = scenarioUI;
        this.main = main;
    }

    public void handle() {
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

    // lista auxiliar de dispositivos para mostrar ao utilizador
    private List<String> deviceList() {
        return model.getAllDevices().stream()
                .map(d -> "[" + d.getId() + "] " + d.getBrand() + " " + d.getModel()
                        + " (" + d.getClass().getSimpleName() + ")")
                .toList();
    }

    private List<String> userList() {
        return model.getUsers().stream()
                .map(u -> "[" + u.getId() + "] " + u.getName()).toList();
    }

    // lista só os nomes dos cenários para o utilizador escolher
    private List<String> scenarioList() {
        return model.getScenarios().stream()
                .map(s -> s.getName()).toList();
    }

    private void createScenario() {
        try {
            String[] data = scenarioUI.readScenarioData(userList());
            User owner = model.getUserById(data[1]);
            if (owner == null) {
                scenarioUI.showError("Utilizador não encontrado.");
                return;
            }
            model.addScenario(new Scenario(data[0], owner));
            scenarioUI.showSuccess("Cenário '" + data[0] + "' criado.");
        } catch (IllegalArgumentException e) {
            scenarioUI.showError(e.getMessage());
        }
    }

    private void addActionToScenario() {
        String scenarioName = scenarioUI.readScenarioName(scenarioList());
        // procuramos o cenário pelo nome
        Scenario scenario = model.getScenarios().stream()
                .filter(s -> s.getName().equals(scenarioName))
                .findFirst().orElse(null);
        if (scenario == null) {
            scenarioUI.showError("Cenário não encontrado.");
            return;
        }

        int actionType = scenarioUI.showActionMenu();
        if (actionType == 6)
            return;

        String[] actionData = scenarioUI.readActionData(deviceList());
        Device device = model.getDeviceById(actionData[0]);
        if (device == null) {
            scenarioUI.showError("Dispositivo não encontrado.");
            return;
        }

        try {
            // verificamos com instanceof se o dispositivo suporta a ação escolhida
            switch (actionType) {
                case 1 -> scenario.addAction(new TurnOnAction(device));
                case 2 -> scenario.addAction(new TurnOffAction(device));
                case 3 -> {
                    if (!(device instanceof Dimmable l)) {
                        scenarioUI.showError("Não é dimmable.");
                        return;
                    }
                    scenario.addAction(new SetBrightnessAction(l, Integer.parseInt(actionData[1])));
                }
                case 4 -> {
                    if (!(device instanceof SmartSpeaker s)) {
                        scenarioUI.showError("Não é coluna.");
                        return;
                    }
                    scenario.addAction(new SetVolumeAction(s, Integer.parseInt(actionData[1])));
                }
                case 5 -> {
                    if (!(device instanceof ApertureDevice a)) {
                        scenarioUI.showError("Sem abertura.");
                        return;
                    }
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
        scenarioUI.displayScenarios(model.getScenarios().stream().map(Object::toString).toList());
    }

    private void executeScenario() {
        String name = scenarioUI.readScenarioName(scenarioList());
        try {
            // passamos o tempo atual para as ações registarem quando foram executadas
            model.executeScenario(name, main.currentTotalMinutes());
            scenarioUI.showSuccess("Cenário '" + name + "' executado.");
        } catch (IllegalArgumentException e) {
            scenarioUI.showError(e.getMessage());
        }
    }
}