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

    @Override
    public Automation clone() {
        try {
            Automation copy = (Automation) super.clone();
            copy.actions = new ArrayList<>(this.actions);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
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

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("Automação: %s [%s] - %s", name, active ? "ativa" : "inativa", condition.getDescription());
    }

    // Automation.java — adicionar
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