package domuscontrol.model.scenario;
import domuscontrol.model.devices.ApertureDevice;

// Ação que define a abertura de um dispositivo (cortinas, portão)
public class SetOpeningAction implements Action {

    private ApertureDevice device;
    private int percentage;

    public SetOpeningAction(ApertureDevice device, int percentage) {
        if (device == null) {
            throw new IllegalArgumentException("O dispositivo não pode ser nulo.");
        }
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("A percentagem deve estar entre 0 e 100.");
        }
        this.device = device;
        this.percentage = percentage;
    }

    @Override
    public void execute() {
        device.setOpeningPercentage(percentage);
    }

    @Override
    public String toString() {
        return "Definir abertura de " + device.toString() + " para " + percentage + "%";
    }
}