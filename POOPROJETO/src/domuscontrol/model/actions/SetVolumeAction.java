package domuscontrol.model.actions;

import domuscontrol.model.devices.SmartSpeaker;

public class SetVolumeAction implements Action {
    private static final long serialVersionUID = 1L;

    private SmartSpeaker speaker;
    private int volume;

    public SetVolumeAction(SmartSpeaker speaker, int volume) {
        if (speaker == null)
            throw new IllegalArgumentException("A coluna não pode ser nula.");
        if (volume < 0 || volume > 100)
            throw new IllegalArgumentException("O volume deve estar entre 0 e 100.");
        this.speaker = speaker;
        this.volume = volume;
    }

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

    @Override
    public SetVolumeAction clone() {
        try {
            return (SetVolumeAction) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}