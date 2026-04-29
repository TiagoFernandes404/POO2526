package domuscontrol.model.automation;

import domuscontrol.model.actions.Action;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Automation implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Condition condition;
    private List<Action> actions;
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

    public void addAction(Action action) {
        if (action == null)
            throw new IllegalArgumentException("A ação não pode ser nula.");
        actions.add(action.clone());
    }

    public void evaluate(int currentMinute) {
        if (active && condition.isMet())
            for (Action action : actions)
                action.execute(currentMinute);
    }

    /*
     * Deep clone:
     * - Lista de ações nova (não partilhada com o original).
     * - Cada Action é também clonada.
     * - A Condition NÃO é clonada porque, no design atual, é imutável após
     * construção (sensor, operador e threshold são finais na prática).
     * As ações continuam a apontar para os mesmos dispositivos — intencional.
     */
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