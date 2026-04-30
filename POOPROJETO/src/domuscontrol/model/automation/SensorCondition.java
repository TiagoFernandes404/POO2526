package domuscontrol.model.automation;

import domuscontrol.model.devices.Sensor;

// Uma condição baseada num sensor
// ex: "temperatura > 25°C" ou "luminosidade < 100 lux"
public class SensorCondition implements Condition {
    private static final long serialVersionUID = 1L;

    // Os operadores disponíveis
    public enum Operator {
        GREATER_THAN, // >
        LESS_THAN, // <
        EQUALS // =
    }

    private Sensor sensor; // qual é o sensor
    private Operator operator; // qual é a comparação
    private double threshold; // com que valor compara

    public SensorCondition(Sensor sensor, Operator operator, double threshold) {
        if (sensor == null) {
            throw new IllegalArgumentException("O sensor não pode ser nulo.");
        }
        this.sensor = sensor;
        this.operator = operator;
        this.threshold = threshold;
    }

    // Verifica se a condição se verifica neste momento
    @Override
    public boolean isMet() {
        return switch (operator) {
            // valor do sensor é maior que o threshold?
            case GREATER_THAN -> sensor.getValue() > threshold;
            // valor do sensor é menor que o threshold?
            case LESS_THAN -> sensor.getValue() < threshold;
            // valor do sensor é (aproximadamente) igual ao threshold?
            case EQUALS -> Math.abs(sensor.getValue() - threshold) < 0.0001;
        };
    }

    // Retorna uma descrição legível da condição
    @Override
    public String getDescription() {
        return sensor.getModel() + " " + operator + " " + threshold + " " + sensor.getUnit();
    }
}