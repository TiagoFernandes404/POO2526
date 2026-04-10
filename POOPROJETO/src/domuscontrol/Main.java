package domuscontrol;

import domuscontrol.controller.ControllerTotal;

// Ponto de entrada da aplicação - só arranca o ControllerTotal
public class Main {

    public static void main(String[] args) {
        new ControllerTotal().run();
    }
}