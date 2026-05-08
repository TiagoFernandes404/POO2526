package domuscontrol.model.scenario;

import domuscontrol.model.users.User;
import domuscontrol.utils.actions.Action;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Um cenário é um grupo de ações que o utilizador pode executar de uma vez
// ex: "Cenário Noite" - desliga todas as luzes, fecha persianas, liga sistema de segurança
public class Scenario implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String name;
    // quem criou este cenário
    private User owner;
    // todas as ações que este cenário vai executar
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
        // guarda uma cópia da ação
        actions.add(action.clone());
    }

    // Executa todas as ações do cenário, uma após a outra
    public void execute(int currentMinute) {
        for (Action action : actions) {
            action.execute(currentMinute);
        }
    }

    // Clone seguro - cria uma cópia profunda
    @Override
    public Scenario clone() {
        try {
            Scenario copy = (Scenario) super.clone();
            // cria uma nova lista de ações (não partilhada)
            copy.actions = new ArrayList<>();
            // copia cada ação também
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