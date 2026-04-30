package domuscontrol.utils;

/*
 * como o programa é uma simulação e não corre em tempo real,
 * precisamos de uma forma de controlar o tempo internamente.
 * esta classe faz isso — guarda o dia, hora e minuto atuais da simulação
 * e permite avançarmos o tempo quando quisermos
 */
import java.io.Serializable;

public class TimeSimulator implements Serializable {
    private static final long serialVersionUID = 1L;

    private int day;
    private int hour;
    private int minute;

    public TimeSimulator(int day, int hour, int minute) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    /*
     * avança o tempo em X minutos — trata automaticamente os overflows.
     * ou seja se passarmos 90 minutos, ele soma 1 hora e 30 minutos.
     * se passarmos 25 horas, avança um dia e 1 hora.
     * assim nunca temos minute > 59 ou hour > 23
     * assim facilitamos ver o tempo coreto
     */
    public void advance(int minutes) {
        this.minute += minutes;
        this.hour += this.minute / 60;
        this.minute = this.minute % 60;
        this.day += this.hour / 24;
        this.hour = this.hour % 24;
    }

    // avança apenas um minuto — útil para simular o tempo passo a passo e testar as
    // estaticas hsaha
    public void tick() {
        advance(1);
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    // devolve o tempo atual no formato "Dia X - HH:MM"
    @Override
    public String toString() {
        return String.format("Dia %d - %02d:%02d", day, hour, minute);
    }
}