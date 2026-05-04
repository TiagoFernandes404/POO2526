package domuscontrol.model.automation;

import domuscontrol.utils.actions.Action;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Uma automação é uma regra do tipo "SE isto acontecer, ENTÃO faz aquilo"
// ex: "SE chover ENTÃO fecha as persianas"
public class Automation implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String name;
    // a condição que tem que se verificar (ex: sensor de chuva > 5)
    private Condition condition;
    // lista de ações a executar quando a condição se verifica
    private List<Action> actions;
    // se false, a automação está desativada
    private boolean active;

    public Automation(String name, Condition condition) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("O nome da automação não pode ser vazio.");
        if (condition == null)
            throw new IllegalArgumentException("A condição não pode ser nula.");
        this.name = name;
        this.condition = condition;
        this.actions = new ArrayList<>();
        this.active = true;
    }

    // Adiciona uma ação que vai ser executada quando a condição se verificar
    public void addAction(Action action) {
        if (action == null)
            throw new IllegalArgumentException("A ação não pode ser nula.");
        // guarda uma cópia da ação (para não mexerem depois)
        actions.add(action.clone());
    }

    // Verifica se a condição se verifica e se sim, executa todas as ações
    // basicamnete se vir que a conficao e feita vai executar o a acao que lhe esta
    // associada
    public void evaluate(int currentMinute) {
        if (active && condition.isMet())
            for (Action action : actions)
                action.execute(currentMinute);
    }

    // Clone seguro - cria uma cópia profunda
    @Override
    public Automation clone() {
        try {
            Automation copy = (Automation) super.clone();
            copy.actions = new ArrayList<>();
            for (Action a : this.actions) {
                copy.actions.add(a.clone());
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Automation deve suportar clone()", e);
        }
    }

    public String getName() {
        return name;
    }

    public Condition getCondition() {
        return condition;
    }

    public List<Action> getActions() {
        return new ArrayList<>(actions);
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Ativa a automação, permitindo que seja avaliada.
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Desativa a automação, impedindo que seja avaliada.
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * @deprecated Usar activate() ou deactivate() em seu lugar.
     */
    @Deprecated
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("Automação: %s [%s] - %s", name, active ? "ativa" : "inativa", condition.getDescription());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Automation))
            return false;
        Automation other = (Automation) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}