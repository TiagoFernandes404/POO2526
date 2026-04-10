package domuscontrol.model.devices;

// Classe abstrata que representa um sensor genérico
public abstract class Sensor extends Device {

    // Valor atual medido pelo sensor
    private double currentValue;

    // Unidade de medida do sensor (ex: mm/h, lux, °C)
    private final String unit;

    public Sensor(String id, String brand, String model, double powerPerHour, String unit) {
        super(id, brand, model, powerPerHour);
        this.currentValue = 0.0;
        this.unit = unit;
    }

    // Define o valor atual do sensor
    public void setValue(double value) {
        this.currentValue = value;
    }

    // Retorna o valor atual do sensor
    public double getValue() {
        return currentValue;
    }

    // Retorna a unidade de medida do sensor
    public String getUnit() {
        return unit;
    }

    
    @Override
    public Sensor clone() { return (Sensor) super.clone(); }
    
    @Override
    public String getStatus() {
        return toString() + " | Valor: " + currentValue + " " + unit;
    }
}