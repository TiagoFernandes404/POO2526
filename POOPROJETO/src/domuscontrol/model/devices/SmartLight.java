package domuscontrol.model.devices;

// Lâmpada inteligente que pode ter brightness ajustável
// Se tiver suporte a cor, também pode mudar a temperatura
public class SmartLight extends Device implements Dimmable, ColorAdjustable {
    // brilho de 0 a 100 (0 = desligada, 100 = máximo)
    private int brightness;
    // temperatura de cor em Kelvin (2700 = quente, 4000 = fria)
    private int colorTemperature;
    // se true, esta lâmpada pode mudar cor
    private final boolean colorSupport;

    public SmartLight(String id, String brand, String model, double powerPerHour, boolean colorSupport) {
        super(id, brand, model, powerPerHour);
        this.brightness = 100; // inicia com brilho máximo
        this.colorSupport = colorSupport;
        // se tem suporte a cor, define cor de base (2700K = branco quente)
        this.colorTemperature = colorSupport ? 2700 : -1;
    }

    // Implementa Dimmable - muda o brilho
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

    // Implementa ColorAdjustable - só funciona se a lâmpada suporta
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

    // Mostra o estado da lâmpada (se está ligada e qual é o brilho)
    @Override
    public String getStatus() {
        String status = toString() + " | Brilho: " + brightness + "%";
        if (colorSupport)
            status += " | Cor: " + colorTemperature + "K";
        return status;
    }
}