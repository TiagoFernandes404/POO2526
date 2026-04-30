package domuscontrol.utils.actions;

import domuscontrol.model.devices.SmartSpeaker;

/*
 * ação que define o volume de uma coluna.
 * se o volume for 0 desliga a coluna, se for > 0 liga-a.

 */
public class SetVolumeAction implements Action {
    private static final long serialVersionUID = 1L;

    private SmartSpeaker speaker;
    private int volume; // 0 = mudo, 100 = volume máximo

    public SetVolumeAction(SmartSpeaker speaker, int volume) {
        if (speaker == null)
            throw new IllegalArgumentException("A coluna não pode ser nula.");
        if (volume < 0 || volume > 100)
            throw new IllegalArgumentException("O volume deve estar entre 0 e 100.");
        this.speaker = speaker;
        this.volume = volume;
    }

    // define o volume e trata do estado ligado/desligado
    @Override
    public void execute(int currentMinute) {
        speaker.setVolume(volume);
        if (volume > 0)
            speaker.turnOn(currentMinute);
        else
            speaker.turnOff(currentMinute);
    }

    @Override
    public String toString() {
        return "Definir volume de " + speaker.toString() + " para " + volume + "%";
    }

    // shallow copy é suficiente — aponta para o mesmo speaker real, que é o que
    // queremos
    @Override
    public SetVolumeAction clone() {
        try {
            return (SetVolumeAction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}