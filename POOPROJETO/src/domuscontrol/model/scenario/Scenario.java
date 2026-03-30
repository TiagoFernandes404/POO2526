package domuscontrol.model.scenario;
import domuscontrol.model.actions.Action;
import domuscontrol.model.users.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class Scenario implements Serializable {
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
        if (action == null) {
            throw new IllegalArgumentException("A ação não pode ser nula.");
        }
        actions.add(action);
    }

    // Executa todas as ações do cenário pela ordem em que foram adicionadas
    public void execute() {
        for (Action action : actions) {
            action.execute();
        }
    }

    // Getters
    public String getName() { return name; }
    public User getOwner() { return owner; }
    public List<Action> getActions() { return new ArrayList<>(actions); }

    @Override
    public String toString() {
        return String.format("Cenário: %s (%d ações) - dono: %s", name, actions.size(), owner.getName());
    }
}