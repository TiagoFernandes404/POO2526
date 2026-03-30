package domuscontrol.model.devices;

import java.io.Serializable;
public abstract class Device implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String brand;
    private String model;
    private double powerPerHour;
    private boolean on;

    public Device(String id, String brand, String model, double powerPerHour) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.powerPerHour = powerPerHour;
        this.on = false;
    }

    public void turnOn() {
        this.on = true;
    }

    public void turnOff() {
        this.on = false;
    }

    public boolean isOn() {
        return on;
    }

    public abstract String getStatus();

    // Getters e Setters
    public String getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getPowerPerHour() { return powerPerHour; }

    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setPowerPerHour(double powerPerHour) { this.powerPerHour = powerPerHour; }

    @Override
    public String toString() {
        return String.format("[%s] %s %s - %s", id, brand, model, on ? "ON" : "OFF");
    }
}