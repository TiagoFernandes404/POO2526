package domuscontrol.model.devices;

public class Relay extends Device {

    public Relay(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
    }

    
    @Override
    public Relay clone() { return (Relay) super.clone(); }

    @Override
    public String getStatus() { return toString(); }
}