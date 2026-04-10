package domuscontrol.model.actions;

import domuscontrol.model.devices.ApertureDevice;

public class SetOpeningAction implements Action {
    private static final long serialVersionUID = 1L;
    private ApertureDevice device;
    private int percentage;

    public SetOpeningAction(ApertureDevice device, int percentage) {
        if (device == null)
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        if (percentage < 0 || percentage > 100)
            throw new IllegalArgumentException("A percentagem deve estar entre 0 e 100.");
        this.device = device;
        this.percentage = percentage;
    }

    @Override
    public void execute(int currentMinute) {
        device.setOpeningPercentage(percentage, currentMinute); //
    }

    @Override
    public String toString() {
        return "Definir abertura de " + device.toString() + " para " + percentage + "%";
    }

    @Override
    public SetOpeningAction clone() {
        try {
            return (SetOpeningAction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}