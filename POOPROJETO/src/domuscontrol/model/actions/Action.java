package domuscontrol.model.actions;

/*
 * Usamos uma interface Action para representar qualquer operação sobre um dispositivo.
 * Desta forma, o Scenario (e futuramente as automações e escalonamentos) não precisa
 * de saber o que cada ação faz — apenas chama execute() em cada uma.
 * Para adicionar um novo tipo de ação basta criar uma nova classe que implemente
 * esta interface, sem alterar nada no Scenario nem nas classes existentes.
 * Isto respeita o princípio aberto/fechado: aberto para extensão, fechado para modificação.
 */

// Interface que representa uma ação a executar sobre um dispositivo
import java.io.Serializable;
public interface Action extends Serializable {
    // Executa a ação
    void execute();
}