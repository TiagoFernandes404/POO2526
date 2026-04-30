package domuscontrol.model.devices;

/*
 * representa uma persiana da casa.
 * herda tudo do ApertureDevice — abertura em %, ligar/desligar automático, etc.
 * e como nao tem mais nenhum comportamento que falte defenir chega isto 
 */

public class Blinds extends ApertureDevice {

    public Blinds(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
    }

    @Override
    public Blinds clone() {
        return (Blinds) super.clone();
    }
}