package domuscontrol.model.devices;

// Coluna de som inteligente que suporta controlo de volume
public class SmartSpeaker extends Device implements HasVolume {

    // Volume atual da coluna (0-100)
    private int volume;

    public SmartSpeaker(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
        this.volume = 0; // começa sem volume
    }

    // Define o volume da coluna (0-100)
    @Override
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("O volume deve estar entre 0 e 100.");
        }
        this.volume = volume;
    }

    // Retorna o volume atual da coluna
    @Override
    public int getVolume() {
        return volume;
    }

    @Override
    public String getStatus() {
        return toString() + " | Volume: " + volume + "%";
    }
}