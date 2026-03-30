package domuscontrol.model.automation;

// Interface que representa uma condição a verificar numa automação
public interface Condition {

    // Retorna true se a condição se verificar
    boolean isMet();

    // Retorna uma descrição legível da condição
    String getDescription();
}