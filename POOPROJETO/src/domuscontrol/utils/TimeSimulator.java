package domuscontrol.utils;

// Simula a passagem do tempo no sistema DomusControl
public class TimeSimulator {

    private int day;
    private int hour;
    private int minute;

    public TimeSimulator(int day, int hour, int minute) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    // Avança o tempo em X minutos
    public void advance(int minutes) {
        this.minute += minutes;
        this.hour += this.minute / 60;
        this.minute = this.minute % 60;
        this.day += this.hour / 24;
        this.hour = this.hour % 24;
    }

    // Avança um minuto de cada vez
    public void tick() {
        advance(1);
    }

    // Getters
    public int getDay() { return day; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }

    @Override
    public String toString() {
        return String.format("Dia %d - %02d:%02d", day, hour, minute);
    }
}