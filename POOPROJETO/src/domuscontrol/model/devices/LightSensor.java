package domuscontrol.model.devices;

// Sensor de luminosidade, mede a intensidade da luz em lux
public class LightSensor extends Sensor {

    public LightSensor(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour, "lux");
    }
}