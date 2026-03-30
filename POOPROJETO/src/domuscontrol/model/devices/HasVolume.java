package domuscontrol.model.devices;

public interface HasVolume {
    // Define o volume do dispositivo (0-100)
    void setVolume(int volume);
    
    // Retorna o volume atual do dispositivo (0-100)
    int getVolume();
}