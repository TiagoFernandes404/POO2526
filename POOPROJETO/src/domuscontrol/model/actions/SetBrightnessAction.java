package domuscontrol.model.actions;

import domuscontrol.model.devices.SmartLight;

public class SetBrightnessAction implements Action {
    private static final long serialVersionUID = 1L;
    private SmartLight light;
    private int brightness;

    public SetBrightnessAction(SmartLight light, int brightness) {
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
        if (brightness > 0)
            light.turnOn(currentMinute);
        else
            light.turnOff(currentMinute);
    }

    @Override
    public String toString() {
        return "Definir brilho de " + light.toString() + " para " + brightness + "%";
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