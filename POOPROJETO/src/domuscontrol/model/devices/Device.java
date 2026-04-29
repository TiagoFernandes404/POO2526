package domuscontrol.model.devices;

import java.io.Serializable;

public abstract class Device implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String brand;
    private String model;
    private double powerPerHour;
    private boolean on;

    private int activationCount;
    private int totalOnTime;
    private int timeOnStart;

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

    // Liga e regista o minuto de início para as estatísticas
    public void turnOn(int currentMinute) {
        if (!this.on) {
            this.on = true;
            this.activationCount++;
            this.timeOnStart = currentMinute;
        }
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

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getPowerPerHour() {
        return powerPerHour;
    }

    public int getActivationCount() {
        return activationCount;
    }

    public int getTotalOnTime() {
        return totalOnTime;
    }

    /**
     * Define a marca do dispositivo com validação.
     * 
     * @param brand nome da marca (não pode ser nulo ou vazio)
     * @throws IllegalArgumentException se brand é inválido
     */
    public void setBrand(String brand) {
        if (brand == null || brand.isBlank())
            throw new IllegalArgumentException("A marca não pode ser nula ou vazia.");
        this.brand = brand;
    }

    /**
     * Define o modelo do dispositivo com validação.
     * 
     * @param model nome do modelo (não pode ser nulo ou vazio)
     * @throws IllegalArgumentException se model é inválido
     */
    public void setModel(String model) {
        if (model == null || model.isBlank())
            throw new IllegalArgumentException("O modelo não pode ser nulo ou vazio.");
        this.model = model;
    }

    /**
     * Define o consumo de energia em watts/hora com validação.
     * 
     * @param p consumo em watts/hora (deve ser não-negativo)
     * @throws IllegalArgumentException se p é negativo
     */
    public void setPowerPerHour(double p) {
        if (p < 0)
            throw new IllegalArgumentException("O consumo de energia não pode ser negativo: " + p);
        this.powerPerHour = p;
    }

    public int getEffectiveTotalOnTime(int currentMinute) {
        if (on && timeOnStart >= 0)
            return totalOnTime + (currentMinute - timeOnStart);
        return totalOnTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Device))
            return false;
        return this.id.equals(((Device) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // Como todos os campos são primitivos ou String (imutável),
    // uma shallow copy é suficiente — não há estado partilhado.
    /*
     * Ou seja fazemos isto pq quando tivermos a atuar num device vai ser nele mesmo
     * por exemplo num cenario
     * nao faz sentido estar a usar uma copia e nao naquele despotivio real da casa
     * que vamos quere estar a trabaljar
     * por isso e q chega
     */
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