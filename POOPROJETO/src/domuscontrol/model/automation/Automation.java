package domuscontrol.model.automation;
import domuscontrol.model.scenario.Action;
import java.util.ArrayList;
import java.util.List;

// Representa uma automação: quando uma condição se verifica, executa uma lista de ações
public class Automation {

    private String name;
    private Condition condition;
    private List<Action> actions;
    private boolean active;

    public Automation(String name, Condition condition) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome da automação não pode ser vazio.");
        }
        if (condition == null) {
            throw new IllegalArgumentException("A condição não pode ser nula.");
        }
        this.name = name;
        this.condition = condition;
        this.actions = new ArrayList<>();
        this.active = true;
    }

    // Adiciona uma ação à automação
    public void addAction(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("A ação não pode ser nula.");
        }
        actions.add(action);
    }

    // Verifica a condição e executa as ações se ela se verificar
    public void evaluate() {
        if (active && condition.isMet()) {
            for (Action action : actions) {
                action.execute();
            }
        }
    }

    // Getters
    public String getName() { return name; }
    public Condition getCondition() { return condition; }
    public List<Action> getActions() { return new ArrayList<>(actions); }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("Automação: %s [%s] - %s", name, active ? "ativa" : "inativa", condition.getDescription());
    }
}