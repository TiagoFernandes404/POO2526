package domuscontrol.model.scenario;

import domuscontrol.model.actions.Action;
import domuscontrol.model.users.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Scenario implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String name;
    private User owner;
    private List<Action> actions;

    public Scenario(String name, User owner) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome do cenário não pode ser vazio.");
        }
        if (owner == null) {
            throw new IllegalArgumentException("O dono não pode ser nulo.");
        }
        this.name = name;
        this.owner = owner;
        this.actions = new ArrayList<>();
    }

    // Adiciona uma ação ao cenário
    public void addAction(Action action) {
        if (action == null)
            throw new IllegalArgumentException("A ação não pode ser nula.");
        actions.add(action.clone()); // cópia defensiva da action
    }

    // Executa todas as ações do cenário pela ordem em que foram adicionadas
    // Executa todas as ações do cenário pela ordem em que foram adicionadas
    public void execute(int currentMinute) {
        for (Action action : actions) {
            action.execute(currentMinute);
        }
    }

    // Deep copy da lista de ações — o clone tem a sua própria lista
    // mas as ações apontam para os mesmos dispositivos (intencional).
    @Override
    public Scenario clone() {
        try {
            Scenario copy = (Scenario) super.clone();
            copy.actions = new ArrayList<>(this.actions);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public List<Action> getActions() {
        return new ArrayList<>(actions);
    }

    @Override
    public String toString() {
        return String.format("Cenário: %s (%d ações) - dono: %s", name, actions.size(), owner.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Scenario))
            return false;
        Scenario other = (Scenario) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}