package domuscontrol.model.devices;

public interface Dimmable {
    // Define o brilho do dispositivo (0-100)
    void setBrightness(int brightness);
    
    // Retorna o brilho atual do dispositivo (0-100)
    int getBrightness();
}