package domuscontrol.model.devices;

public class SmartLight extends Device implements Dimmable, ColorAdjustable {

    // Brilho da lâmpada (0-100)
    private int brightness;
    
    // Temperatura de cor em Kelvin (2700K-4000K)
    // Se a lâmpada não suportar cor, este valor é -1
    private int colorTemperature;
    
    // Indica se a lâmpada suporta ajuste de cor
    private final boolean colorSupport;

    public SmartLight(String id, String brand, String model, double powerPerHour, boolean colorSupport) {
        super(id, brand, model, powerPerHour);
        this.brightness = 100;
        this.colorSupport = colorSupport;
        this.colorTemperature = colorSupport ? 2700 : -1;
    }

    // Define o brilho da lâmpada (0-100)
    @Override
    public void setBrightness(int brightness) {
        if (brightness < 0 || brightness > 100) {
            throw new IllegalArgumentException("Brilho deve estar entre 0 e 100.");
        }
        this.brightness = brightness;
    }

    // Retorna o brilho atual da lâmpada
    @Override
    public int getBrightness() {
        return brightness;
    }

    // Define a temperatura de cor em Kelvin (2700K-4000K)
    @Override
    public void setColorTemperature(int kelvin) {
        if (!colorSupport) {
            throw new UnsupportedOperationException("Esta lâmpada não suporta ajuste de cor.");
        }
        if (kelvin < 2700 || kelvin > 4000) {
            throw new IllegalArgumentException("Temperatura de cor deve estar entre 2700K e 4000K.");
        }
        this.colorTemperature = kelvin;
    }

    // Retorna a temperatura de cor atual em Kelvin
    @Override
    public int getColorTemperature() {
        return colorTemperature;
    }

    // Retorna se a lâmpada suporta ajuste de cor
    public boolean hasColorSupport() {
        return colorSupport;
    }

    @Override
    public String getStatus() {
        String status = toString() + " | Brilho: " + brightness + "%";
        if (colorSupport) {
            status += " | Cor: " + colorTemperature + "K";
        }
        return status;
    }
}