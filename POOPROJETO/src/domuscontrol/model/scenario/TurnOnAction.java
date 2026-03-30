package domuscontrol.model.scenario;

import domuscontrol.model.devices.Device;

public class TurnOnAction implements Action {

    private final Device device;

    public TurnOnAction(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        }
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOn();
    }

    @Override
    public String toString() {
        return "Ligar " + device.toString();
    }
}