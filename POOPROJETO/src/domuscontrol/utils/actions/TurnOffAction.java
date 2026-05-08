package domuscontrol.utils.actions;

import domuscontrol.model.devices.Device;

/*
acao de delisgar dispositivos
 */
public class TurnOffAction implements Action {
    private static final long serialVersionUID = 1L;

    private final Device device;

    public TurnOffAction(Device device) {
        if (device == null)
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        this.device = device;
    }

    @Override
    public void execute(int currentMinute) {
        device.turnOff(currentMinute);
    }

    @Override
    public String toString() {
        return "Desligar " + device.toString();
    }

    // shallow copy é suficiente — aponta para o mesmo device real, que é o que
    // queremos
    @Override
    public TurnOffAction clone() {
        try {
            return (TurnOffAction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}