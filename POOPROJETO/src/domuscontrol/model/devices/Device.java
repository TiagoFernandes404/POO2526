package domuscontrol.model.devices;

import java.io.Serializable;

public abstract class Device implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String brand;
    private String model;
    private double powerPerHour;
    private boolean on;

    // Estatísticas de utilização
    private int activationCount;  // nº de vezes que foi ligado
    private int totalOnTime;      // minutos total ligado
    private int timeOnStart;      // minuto em que foi ligado (para calcular duração)

    public Device(String id, String brand, String model, double powerPerHour) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.powerPerHour = powerPerHour;
        this.on = false;
        this.activationCount = 0;
        this.totalOnTime = 0;
        this.timeOnStart = -1;
    }

    // Liga sem registar tempo (comportamento original mantido)
    public void turnOn() {
        this.on = true;
    }

    // Liga e regista o minuto de início para calcular duração depois
    public void turnOn(int currentMinute) {
        if (!this.on) {
            this.on = true;
            this.activationCount++;
            this.timeOnStart = currentMinute;
        }
    }

    // Desliga sem registar tempo (comportamento original mantido)
    public void turnOff() {
        this.on = false;
    }

    // Desliga e acumula o tempo que esteve ligado
    public void turnOff(int currentMinute) {
        if (this.on && timeOnStart >= 0) {
            this.totalOnTime += (currentMinute - timeOnStart);
            this.timeOnStart = -1;
        }
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
    public int getActivationCount() { return activationCount; }
    public int getTotalOnTime() { return totalOnTime; }

    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setPowerPerHour(double powerPerHour) { this.powerPerHour = powerPerHour; }

    // Dois dispositivos são iguais se tiverem o mesmo id
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Device)) return false;
        Device other = (Device) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // Como todos os campos são primitivos ou String (imutável),
    // uma shallow copy é suficiente — não há estado partilhado.
    @Override
    public Device clone() {
        try {
            return (Device) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s - %s", id, brand, model, on ? "ON" : "OFF");
    }
}