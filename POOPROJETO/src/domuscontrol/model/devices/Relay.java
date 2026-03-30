package domuscontrol.model.devices;
 // classe do rele 
public class Relay extends Device {

    public Relay(String id, String brand, String model, double powerPerHour) {
        super(id, brand, model, powerPerHour);
    }

    // Retorna o estado atual do relé (ligado/desligado)
    @Override
    public String getStatus() {
        return toString();
    }
}