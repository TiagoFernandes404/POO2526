package domuscontrol.model.devices;

import java.io.Serializable;

// Classe abstrata que define o comportamento base de qualquer dispositivo da casa
// Todos os dispositivos podem ser ligados/desligados e têm estatísticas de uso
public abstract class Device implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private final String id;// Identificador único do dispositivo - não muda depois de criado
    private String brand; // matricula
    private String model; // modelo
    private double powerPerHour; // consumo de energia em watts por hora
    private boolean on; // flag para saber se o dispositivo está ligado ou desligado

    // Variáveis para a parte de estatiicas
    private int activationCount; // quantas vezes foi ligado
    private int totalOnTime; // tempo total que esteve ligado
    private int timeOnStart; // minuto em que foi ligado (para calcular o tempo)

    public Device(String id, String brand, String model, double powerPerHour) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.powerPerHour = powerPerHour;
        this.on = false;
        this.activationCount = 0;
        this.totalOnTime = 0;
        this.timeOnStart = -1; // -1 significa que não está ligado
    }

    // Liga o dispositivo e marca o momento em que foi ligado
    // precisa do minuto atual para depois calcular quanto tempo esteve ligado
    public void turnOn(int currentMinute) {
        if (!this.on) {
            this.on = true;
            this.activationCount++; // conta que foi ligado mais uma vez
            this.timeOnStart = currentMinute; // guarda o minuto para depois quando for desligado podermos ter acesso
            // ao tempo inicil para fazer final -inicial e assim termos o tempo que teve
            // ligado para adicionar as
            // estatisticas
        }
    }

    // Desliga o dispositivo e calcula quanto tempo esteve ligado desde que foi
    // ativado
    public void turnOff(int currentMinute) {
        if (this.on && timeOnStart >= 0) {
            // calcula a diferença entre agora e quando foi ligado
            this.totalOnTime += (currentMinute - timeOnStart);
            this.timeOnStart = -1; // reset para indicar que não está ligado
        }
        this.on = false; // mete o dispositivo desligado
    }

    public boolean isOn() {
        return on;
    }

    public abstract String getStatus();

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getPowerPerHour() {
        return powerPerHour;
    }

    public int getActivationCount() {
        return activationCount;
    }

    public int getTotalOnTime() {
        return totalOnTime;
    }

    /**
     * Define a marca do dispositivo com validação.
     * 
     * @param brand nome da marca (não pode ser nulo ou vazio)
     * @throws IllegalArgumentException se brand é inválido
     */
    public void setBrand(String brand) {
        if (brand == null || brand.isBlank())
            throw new IllegalArgumentException("A marca não pode ser nula ou vazia.");
        this.brand = brand;
    }

    /**
     * Define o modelo do dispositivo com validação.
     * o nome do modelo (não pode ser nulo ou vazio)
     * temos que lancar uma exceotiion se model é inválido
     */
    public void setModel(String model) {
        if (model == null || model.isBlank())
            throw new IllegalArgumentException("O modelo não pode ser nulo ou vazio.");
        this.model = model;
    }

    /**
     * Define o consumo de energia em watts/hora com validação.
     * 
     * o consumo em watts/hora (deve ser não-negativo)
     * lancamos exceptions se p é negativo
     */
    public void setPowerPerHour(double p) {
        if (p < 0)
            throw new IllegalArgumentException("O consumo de energia não pode ser negativo: " + p);
        this.powerPerHour = p;
    }

    /*
     * esta classe serve para quando quiseremos obter o tempo ligado de um
     * dispositivo mas antes so o conseguiamos quando
     * era usada a funcao de deslogar assim temos acesso ao tempo ligado na mesma !!
     */
    public int getEffectiveTotalOnTime(int currentMinute) {
        if (on && timeOnStart >= 0)
            return totalOnTime + (currentMinute - timeOnStart);
        return totalOnTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Device))
            return false;
        return this.id.equals(((Device) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /*
     * Preciso de fazer clone porque há cenários onde quero guardar o estado de um
     * device antes de aplicar mudanças (tipo undo).
     *
     * Mas como é que faço isto de forma segura?
     * Só tenho primitivos (int, boolean, double) e Strings — e Strings são
     * imutáveis!
     * Portanto uma shallow copy é suficiente: não há listas nem objetos partilhados
     * que se possam "estragar" ao copiar.
     *
     * (se houvesse objetos partilhados, modificar o clone afetava o original — mas
     * aqui é seguro, por isso chega fazer super.clone() e pronto)
     */
    @Override
    public Device clone() {
        try {
            return (Device) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s - %s", id, brand, model, on ? "ON" : "OFF");
    }
}