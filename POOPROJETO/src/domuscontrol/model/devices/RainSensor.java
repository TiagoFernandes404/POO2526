package domuscontrol.model.devices;

// Sensor de pluviosidade, mede a quantidade de chuva em mm/h
public class RainSensor extends Sensor {

    public RainSensor(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour, "mm/h");
    }

    @Override
    public RainSensor clone() { return (RainSensor) super.clone(); }
}