package domuscontrol.model.devices;

/*
 * interface para devices que têm brilho regulável, por exemplo lâmpadas.
 * qualquer device que implemente isto compromete-se a ter um brilho entre 0 e 100
 * e a deixar alterá-lo, vamos separar isto numa interface para não obrigar todos os
 * devices a ter brilho, só os que faz sentido e poupar ter que escerver a cena do brilho em varias subclasses
 */

public interface Dimmable {
    // Define o brilho do dispositivo (0-100)
    void setBrightness(int brightness);

    // Retorna o brilho atual do dispositivo (0-100)
    int getBrightness();
}