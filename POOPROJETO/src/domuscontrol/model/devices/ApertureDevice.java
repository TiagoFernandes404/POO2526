package domuscontrol.model.devices;
// devices que tem aberttura
public abstract class ApertureDevice extends Device {

    // Percentagem de abertura do dispositivo (0-100)
    // 0 = totalmente fechado, 100 = totalmente aberto
    private int openingPercentage;

    public ApertureDevice(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
        this.openingPercentage = 0; // começa fechado
    }

    // Abre o dispositivo totalmente
    public void open() {
        setOpeningPercentage(100);
    }

    // Fecha o dispositivo totalmente
    public void close() {
        setOpeningPercentage(0);
    }

    // Define a percentagem de abertura (0-100)
    public void setOpeningPercentage(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentagem deve estar entre 0 e 100.");
        }
        this.openingPercentage = percentage;
        if (percentage > 0) turnOn(); else turnOff();
    }

    // Retorna a percentagem de abertura atual
    public int getOpeningPercentage() {
        return openingPercentage;
    }

    @Override
    public String getStatus() {
        return toString() + " | Abertura: " + openingPercentage + "%";
    }
}