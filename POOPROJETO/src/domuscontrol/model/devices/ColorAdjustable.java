package domuscontrol.model.devices;

/**
 * Interface para dispositivos com temperatura de cor ajustável.
 * O Kelvin é a unidade usada para medir a temperatura de cor da luz:
 * 2700K → luz amarelada/quente (tipo vela, ambiente aconchegante)
 * 3000K → luz branca quente (tipo sala de estar)
 * 3500K → luz neutra (tipo escritório)
 * 4000K → luz branca fria (tipo luz do dia)
 */

public interface ColorAdjustable {
    void setColorTemperature(int kelvin);

    int getColorTemperature();

    boolean hasColorSupport();
}