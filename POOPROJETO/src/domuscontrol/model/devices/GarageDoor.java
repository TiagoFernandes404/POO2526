package domuscontrol.model.devices;

/*
 * representa um portaodegaragem da casa.
 * herda tudo do ApertureDevice — abertura em %, ligar/desligar automático, etc.
 * e como nao tem mais nenhum comportamento que falte defenir chega isto 
 */

public class GarageDoor extends ApertureDevice {

    public GarageDoor(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
    }

    @Override
    public GarageDoor clone() {
        return (GarageDoor) super.clone();
    }
}
