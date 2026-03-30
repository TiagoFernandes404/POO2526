package domuscontrol.model.scheduling;
import domuscontrol.model.actions.Action;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int hour;
    private int minute;
    private boolean repeating;
    private boolean active;
    private List<Action> actions;

    public Schedule(String name, int hour, int minute, boolean repeating) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome não pode ser vazio.");
        }
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("A hora deve estar entre 0 e 23.");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Os minutos devem estar entre 0 e 59.");
        }
        this.name = name;
        this.hour = hour;
        this.minute = minute;
        this.repeating = repeating;
        this.active = true;
        this.actions = new ArrayList<>();
    }

    // Adiciona uma ação ao escalonamento
    public void addAction(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("A ação não pode ser nula.");
        }
        actions.add(action);
    }

    // Verifica se deve executar para a hora/minuto dados e executa se sim
    public void evaluate(int currentHour, int currentMinute) {
        if (active && currentHour == hour && currentMinute == minute) {
            for (Action action : actions) {
                action.execute();
            }
            if (!repeating) active = false;
        }
    }

    // Getters
    public String getName() { return name; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public boolean isRepeating() { return repeating; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public List<Action> getActions() { return new ArrayList<>(actions); }

    @Override
    public String toString() {
        return String.format("Escalonamento: %s às %02d:%02d [%s] [%s]",
                name, hour, minute,
                repeating ? "repetição" : "uma vez",
                active ? "ativo" : "inativo");
    }
}