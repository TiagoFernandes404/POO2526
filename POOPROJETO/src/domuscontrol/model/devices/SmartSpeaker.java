package domuscontrol.model.devices;

/*
 * representa um coluna da casa.
 * implementa HasVolume porque é um device que tem volume regulável (0-100)
 * tal como o Dimmable para as luzes, o HasVolume permite controlar
 * o volume de todos os speakers da casa de forma genérica
 */
public class SmartSpeaker extends Device implements HasVolume {

    private int volume; // volume de 0 a 100 (0 = sem som, 100 = som máximo)

    public SmartSpeaker(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
        this.volume = 0; // começa desligado sem som
    }

    // defenimos o volume de 0 a 100% senao estiver nesse intervali fazemos uma
    // exception
    @Override
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100)
            throw new IllegalArgumentException("O volume deve estar entre 0 e 100.");
        this.volume = volume;
    }

    @Override
    public int getVolume() {
        return volume;
    }

    @Override
    public SmartSpeaker clone() {
        return (SmartSpeaker) super.clone();
    }

    @Override
    public String getStatus() {
        return toString() + " | Volume: " + volume + "%";
    }
}