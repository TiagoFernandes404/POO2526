package domuscontrol.model.devices;

// Sensor de temperatura, mede a temperatura em graus Celsius
public class TemperatureSensor extends Sensor {

    public TemperatureSensor(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour, "°C");
    }

    @Override
    public TemperatureSensor clone() { return (TemperatureSensor) super.clone(); }
}