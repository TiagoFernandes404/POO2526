package domuscontrol.model.automation;

import domuscontrol.model.devices.Sensor;

// Condição baseada no valor de um sensor (ex: chuva > 5, luminosidade < 100)

public class SensorCondition implements Condition {
    private static final long serialVersionUID = 1L;

    public enum Operator {
        GREATER_THAN, LESS_THAN, EQUALS
    }

    private Sensor sensor;
    private Operator operator;
    private double threshold;

    public SensorCondition(Sensor sensor, Operator operator, double threshold) {
        if (sensor == null) {
            throw new IllegalArgumentException("O sensor não pode ser nulo.");
        }
        this.sensor = sensor;

        this.operator = operator;
        this.threshold = threshold;
    }

    @Override
    public boolean isMet() {
        switch (operator) {
            case GREATER_THAN:
                return sensor.getValue() > threshold;
            case LESS_THAN:
                return sensor.getValue() < threshold;
            case EQUALS:
                return Math.abs(sensor.getValue() - threshold) < 0.0001;
            default:
                return false;
        }
    }

    @Override
    public String getDescription() {
        return sensor.getModel() + " " + operator + " " + threshold + " " + sensor.getUnit();
    }
}