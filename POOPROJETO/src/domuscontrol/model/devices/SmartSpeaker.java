package domuscontrol.model.devices;

public class SmartSpeaker extends Device implements HasVolume {

    private int volume;

    public SmartSpeaker(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
        this.volume = 0;
    }

    @Override
    public void setVolume(int volume) {
        if (volume < 0 || volume > 100)
            throw new IllegalArgumentException("O volume deve estar entre 0 e 100.");
        this.volume = volume;
    }

    @Override
    public int getVolume() { return volume; }

    
    @Override
    public SmartSpeaker clone() { return (SmartSpeaker) super.clone(); }

    @Override
    public String getStatus() { return toString() + " | Volume: " + volume + "%"; }
}