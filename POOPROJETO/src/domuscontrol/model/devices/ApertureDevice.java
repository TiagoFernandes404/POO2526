package domuscontrol.model.devices;

/*
 * esta classe foi criada para ser a classe abaixo de Device em que vai ser
 * superclasse de todos os devices que abram, por exemplo portões e persianas.
 * basicamente qualquer coisa que tenha uma percentagem de abertura
 */
public abstract class ApertureDevice extends Device {

    private int openingPercentage; // percentagem de abertura (0 = fechado, 100 = totalmente aberto)

    public ApertureDevice(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
        this.openingPercentage = 0; // decidimos que comeca fechado depois temos que dar
        // ao utlizador uma maneira de abrir
    }

    // abre totalmente o dispositivo (100%)
    public void open(int currentMinute) {
        setOpeningPercentage(100, currentMinute);
    }

    // fecha totalmente o dispositivo (0%)
    public void close(int currentMinute) {
        setOpeningPercentage(0, currentMinute);
    }

    /*
     * aqui e a funcao chave desta classe de devices e onde abrimos ou fechamos os
     * devices
     * podemos abrir em qualuer tipo de % de 0-100
     * como e um device ou esta ligado ou nao como e automcatico vamos defenir que
     * como ele comeca fechado ou seja
     * 0% isso e o estado desligado e tudo o resto ele esta ligado pq a abertura foi
     * alterada
     * assim ja temos aqui a parte de ligar e desligar
     */
    public void setOpeningPercentage(int percentage, int currentMinute) {
        if (percentage < 0 || percentage > 100)
            throw new IllegalArgumentException("Percentagem deve estar entre 0 e 100.");
        this.openingPercentage = percentage;
        if (percentage > 0)
            turnOn(currentMinute);
        else
            turnOff(currentMinute);
    }

    public int getOpeningPercentage() {
        return openingPercentage;
    }

    @Override
    public ApertureDevice clone() {
        return (ApertureDevice) super.clone();
    }

    @Override
    public String getStatus() {
        return toString() + " | Abertura: " + openingPercentage + "%";
    }
}