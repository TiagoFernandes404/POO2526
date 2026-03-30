package domuscontrol.model.scenario;
import domuscontrol.model.devices.SmartLight;

// Ação que define o brilho de uma lâmpada
public class SetBrightnessAction implements Action {

    private SmartLight light;
    private int brightness;

    public SetBrightnessAction(SmartLight light, int brightness) {
        if (light == null) {
            throw new IllegalArgumentException("A lâmpada não pode ser nula.");
        }
        if (brightness < 0 || brightness > 100) {
            throw new IllegalArgumentException("O brilho deve estar entre 0 e 100.");
        }
        this.light = light;
        this.brightness = brightness;
    }

    @Override
    public void execute() {
        light.setBrightness(brightness);
        if (brightness > 0) light.turnOn(); else light.turnOff();
    }

    @Override
    public String toString() {
        return "Definir brilho de " + light.toString() + " para " + brightness + "%";
    }
}