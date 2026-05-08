package domuscontrol.utils.actions;

import java.io.Serializable;

/*
 * Usamos uma interface Action para representar qualquer operação sobre um dispositivo.
 * Desta forma, o Scenario (e futuramente as automações e escalonamentos) não precisa
 * de saber o que cada ação faz — apenas chama execute() em cada uma.
 * Para adicionar um novo tipo de ação basta criar uma nova classe que implemente
 * esta interface, sem alterar nada no Scenario nem nas classes existentes.assim permitimos o avanco facil 
 * do projeto no futuro
 */

public interface Action extends Serializable, Cloneable {
    void execute(int currentMinute);

    Action clone();
}