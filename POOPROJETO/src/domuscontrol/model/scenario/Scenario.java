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

    // Adiciona uma ação ao cenário (cópia defensiva)
    public void addAction(Action action) {
        if (action == null)
            throw new IllegalArgumentException("A ação não pode ser nula.");
        actions.add(action.clone());
    }

    // Executa todas as ações do cenário pela ordem em que foram adicionadas
    public void execute(int currentMinute) {
        for (Action action : actions) {
            action.execute(currentMinute);
        }
    }

    /*
     * Deep clone:
     * - É criada uma nova lista de ações (não partilhada com o original).
     * - Cada Action é também clonada — assim, modificar uma ação no clone
     * não afeta o original e vice-versa.
     * - As ações continuam a apontar para os MESMOS dispositivos da casa,
     * o que é intencional: ao executar o cenário clonado, ele deve atuar
     * nos dispositivos reais e não em cópias deles.
     */
    @Override
    public Scenario clone() {
        try {
            Scenario copy = (Scenario) super.clone();
            copy.actions = new ArrayList<>();
            for (Action a : this.actions) {
                copy.actions.add(a.clone());
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Scenario deve suportar clone()", e);
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