package domuscontrol.model.devices;

public class SmartLight extends Device implements Dimmable, ColorAdjustable {
    private int brightness;
    private int colorTemperature;
    private final boolean colorSupport;

    public SmartLight(String id, String brand, String model, double powerPerHour, boolean colorSupport) {
        super(id, brand, model, powerPerHour);
        this.brightness = 100;
        this.colorSupport = colorSupport;
        this.colorTemperature = colorSupport ? 2700 : -1;
    }

    @Override
    public void setBrightness(int brightness) {
        if (brightness < 0 || brightness > 100)
            throw new IllegalArgumentException("Brilho deve estar entre 0 e 100.");
        this.brightness = brightness;
    }

    @Override
    public int getBrightness() {
        return brightness;
    }

    @Override
    public boolean hasColorSupport() {
        return colorSupport;
    }

    @Override
    public void setColorTemperature(int kelvin) {
        if (!colorSupport)
            throw new UnsupportedOperationException("Esta lâmpada não suporta ajuste de cor.");
        if (kelvin < 2700 || kelvin > 4000)
            throw new IllegalArgumentException("Temperatura de cor deve estar entre 2700K e 4000K.");
        this.colorTemperature = kelvin;
    }

    @Override
    public int getColorTemperature() {
        return colorTemperature;
    }

    @Override
    public SmartLight clone() {
        return (SmartLight) super.clone();
    }

    @Override
    public String getStatus() {
        String status = toString() + " | Brilho: " + brightness + "%";
        if (colorSupport)
            status += " | Cor: " + colorTemperature + "K";
        return status;
    }
}