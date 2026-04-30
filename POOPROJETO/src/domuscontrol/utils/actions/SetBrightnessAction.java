package domuscontrol.utils.actions;

import domuscontrol.model.devices.Device;
import domuscontrol.model.devices.Dimmable;

/*
 * ação que define o brilho de uma lâmpada.
 * se o brilho for 0 desliga o device, se for > 0 liga-o.
 * usamos Dimmable em vez de SmartLight para funcionar com qualquer
 * device que tenha brilho — não só lâmpadas
 */
public class SetBrightnessAction implements Action {
    private static final long serialVersionUID = 1L;

    private Dimmable light;
    private int brightness; // 0 = apagado, 100 = brilho máximo

    public SetBrightnessAction(Dimmable light, int brightness) {
        if (light == null)
            throw new IllegalArgumentException("A lâmpada não pode ser nula.");
        if (brightness < 0 || brightness > 100)
            throw new IllegalArgumentException("O brilho deve estar entre 0 e 100.");
        this.light = light;
        this.brightness = brightness;
    }

    /*
     * define o brilho e trata do estado ligado/desligado.
     * verificamos se é também um Device para poder chamar turnOn/turnOff
     * e assim as estatísticas ficarem atualizadas
     */
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

    /*
     * shallow copy é suficiente — só temos um int e uma referência a Dimmable.
     * o clone aponta para o mesmo device — é isso que queremos,
     * atuar no device real e não numa cópia
     */
    @Override
    public SetBrightnessAction clone() {
        try {
            return (SetBrightnessAction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}