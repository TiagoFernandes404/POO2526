package domuscontrol.model.scenario;

import domuscontrol.model.devices.Device;
// Ação que desliga um dispositivo
public class TurnOffAction implements Action {

    private final Device device;

    public TurnOffAction(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        }
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOff();
    }

    @Override
    public String toString() {
        return "Desligar " + device.toString();
    }
}