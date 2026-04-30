package domuscontrol.utils.actions;

import domuscontrol.model.devices.ApertureDevice;

/*
 * ação que define a percentagem de abertura de um ApertureDevice
 * (persiana, portão, etc).

 */
public class SetOpeningAction implements Action {
    private static final long serialVersionUID = 1L;

    private ApertureDevice device;
    private int percentage; // 0 = fechado, 100 = totalmente aberto

    public SetOpeningAction(ApertureDevice device, int percentage) {
        if (device == null)
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        if (percentage < 0 || percentage > 100)
            throw new IllegalArgumentException("A percentagem deve estar entre 0 e 100.");
        this.device = device;
        this.percentage = percentage;
    }

    // delega tudo no ApertureDevice que já sabe tratar da abertura e do estado
    @Override
    public void execute(int currentMinute) {
        device.setOpeningPercentage(percentage, currentMinute);
    }

    @Override
    public String toString() {
        return "Definir abertura de " + device.toString() + " para " + percentage + "%";
    }

    // shallow copy é suficiente — aponta para o mesmo device real, que é o que
    // queremos
    @Override
    public SetOpeningAction clone() {
        try {
            return (SetOpeningAction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}