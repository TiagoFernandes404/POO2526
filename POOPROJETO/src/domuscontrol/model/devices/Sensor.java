package domuscontrol.model.devices;

// Classe base para todos os sensores (temperatura, humidade, luz, etc)
// Cada sensor mede um valor e tem uma unidade de medida
public abstract class Sensor extends Device {

    private double currentValue; // o valor que o sensor está a medir neste momento
    private final String unit; // unidade do valor (ex: "°C", "lux", "mm/h")

    public Sensor(String id, String brand, String model, double powerPerHour, String unit) {
        super(id, brand, model, powerPerHour);
        this.currentValue = 0.0;
        this.unit = unit;
    }

    // Atualiza o valor que o sensor está a medir (com validação)
    public void setValue(double value) {
        validateValue(value); // verifica se o valor é válido
        this.currentValue = value;
    }

    // Retorna o valor atual
    public double getValue() {
        return currentValue;
    }

    // Retorna a unidade (para mostrar junto do valor)
    public String getUnit() {
        return unit;
    }

    /**
     * Valida o valor antes de ser guardado
     * Subclasses podem sobrescrever para adicionar validações específicas
     * (ex: temperatura não pode ser menor que -50°C)
     */
    protected void validateValue(double value) {
        // validação base: sensores não devem ter valores inválidos
        if (Double.isNaN(value) || Double.isInfinite(value))
            throw new IllegalArgumentException("Valor inválido para sensor: " + value);
    }

    @Override
    public Sensor clone() {
        return (Sensor) super.clone();
    }

    @Override
    public String getStatus() {
        return toString() + " | Valor: " + currentValue + " " + unit;
    }
}