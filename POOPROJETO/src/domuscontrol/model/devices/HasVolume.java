package domuscontrol.model.devices;

/*tal como  interface dos dimmable esta interface serve para os dispotivos que tem volume por exemplo as colunas e asim  */
public interface HasVolume {
    // Define o volume do dispositivo (0-100)
    void setVolume(int volume);

    // Retorna o volume atual do dispositivo (0-100)
    int getVolume();
}