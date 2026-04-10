package domuscontrol.model.devices;

public abstract class ApertureDevice extends Device {

    private int openingPercentage;

    public ApertureDevice(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
        this.openingPercentage = 0;
    }

    
    public void open(int currentMinute) {
        setOpeningPercentage(100, currentMinute);
    }

    public void close(int currentMinute) {
        setOpeningPercentage(0, currentMinute);
    }

   
    public void setOpeningPercentage(int percentage, int currentMinute) {
        if (percentage < 0 || percentage > 100)
            throw new IllegalArgumentException("Percentagem deve estar entre 0 e 100.");
        this.openingPercentage = percentage;
        if (percentage > 0) turnOn(currentMinute); else turnOff(currentMinute);
    }

    public int getOpeningPercentage() { return openingPercentage; }

   
    @Override
    public ApertureDevice clone() { return (ApertureDevice) super.clone(); }

    @Override
    public String getStatus() {
        return toString() + " | Abertura: " + openingPercentage + "%";
    }
}