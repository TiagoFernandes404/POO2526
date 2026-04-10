package domuscontrol.model.devices;

public class Blinds extends ApertureDevice {

    public Blinds(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
    }

   
    @Override
    public Blinds clone() { return (Blinds) super.clone(); }
}