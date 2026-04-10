package domuscontrol.model.devices;

public class GarageDoor extends ApertureDevice {

    public GarageDoor(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
    }

    @Override
    public GarageDoor clone() {
        return (GarageDoor) super.clone();
    }
}
