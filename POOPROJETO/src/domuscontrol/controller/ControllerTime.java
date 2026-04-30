package domuscontrol.controller;

import domuscontrol.view.Menu;
import domuscontrol.view.TimeUI;

public class ControllerTime {

    private final DomusController model;
    private final TimeUI timeUI;

    public ControllerTime(DomusController model, TimeUI timeUI) {
        this.model = model;
        this.timeUI = timeUI;
    }

    public void handle() {
        int choice;
        do {
            choice = timeUI.showMenu();
            switch (choice) {
                case 1 -> {
                    model.tick();
                    timeUI.showSuccess("Tempo avançado 1 minuto.");
                    timeUI.displayTime(model.getTime().toString());
                }
                case 2 -> {
                    int minutes = timeUI.readMinutes();
                    if (minutes <= 0) {
                        Menu.showMessage("Deve ser positivo.");
                        break;
                    }
                    for (int i = 0; i < minutes; i++)
                        model.tick();
                    timeUI.showSuccess("Avançado " + minutes + " minutos.");
                    timeUI.displayTime(model.getTime().toString());
                }
                case 3 -> timeUI.displayTime(model.getTime().toString());
            }
        } while (choice != 4);
    }
}