package domuscontrol.model.actions;

import domuscontrol.model.devices.Device;
import domuscontrol.model.devices.Dimmable;

public class SetBrightnessAction implements Action {
    private static final long serialVersionUID = 1L;
    private Dimmable light;
    private int brightness;

    public SetBrightnessAction(Dimmable light, int brightness) {
        if (light == null)
            throw new IllegalArgumentException("A lâmpada não pode ser nula.");
        if (brightness < 0 || brightness > 100)
            throw new IllegalArgumentException("O brilho deve estar entre 0 e 100.");
        this.light = light;
        this.brightness = brightness;
    }

    @Override
    public void execute(int currentMinute) {
        light.setBrightness(brightness);
        if (light instanceof Device d) {
            if (brightness > 0)
                d.turnOn(currentMinute);
            else
                d.turnOff(currentMinute);
        }
    }

    @Override
    public String toString() {
        return "Definir brilho para " + brightness + "%";
    }

    @Override
    public SetBrightnessAction clone() {
        try {
            return (SetBrightnessAction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}