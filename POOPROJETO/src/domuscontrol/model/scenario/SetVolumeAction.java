package domuscontrol.model.scenario;

import domuscontrol.model.devices.SmartSpeaker;

// Ação que define o volume de uma coluna de som
public class SetVolumeAction implements Action {

    private SmartSpeaker speaker;
    private int volume;

    public SetVolumeAction(SmartSpeaker speaker, int volume) {
        if (speaker == null) {
            throw new IllegalArgumentException("A coluna não pode ser nula.");
        }
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("O volume deve estar entre 0 e 100.");
        }
        this.speaker = speaker;
        this.volume = volume;
    }

    @Override
    public void execute() {
        speaker.setVolume(volume);
        if (volume > 0) speaker.turnOn(); else speaker.turnOff();
    }

    @Override
    public String toString() {
        return "Definir volume de " + speaker.toString() + " para " + volume + "%";
    }
}