package domuscontrol.model.actions;

import domuscontrol.model.devices.Device;

public class TurnOnAction implements Action {
    private static final long serialVersionUID = 1L;

    private final Device device;

    public TurnOnAction(Device device) {
        if (device == null)
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        this.device = device;
    }

    @Override
    public void execute(int currentMinute) {
        device.turnOn(currentMinute);
    }

    @Override
    public String toString() {
        return "Ligar " + device.toString();
    }

    @Override
    public TurnOnAction clone() {
        try {
            return (TurnOnAction) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}