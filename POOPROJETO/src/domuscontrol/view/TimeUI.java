package domuscontrol.view;

// View do tempo simulado - só lê input e mostra output
public class TimeUI {

    private final Menu menu;

    public TimeUI() {
        this.menu = new Menu("Avançar Tempo", new String[]{
            "Avançar 1 minuto",
            "Avançar X minutos",
            "Ver hora atual",
            "Voltar"
        });
    }

    // Mostra o menu e devolve a opção escolhida
    public int showMenu() {
        return menu.show();
    }

    // Lê quantos minutos avançar
    public int readMinutes() {
        return Menu.readInt("Quantos minutos? ");
    }

    // Mostra a hora atual
    public void displayTime(String time) {
        System.out.println("Tempo atual: " + time);
    }

    // Mostra mensagem de sucesso
    public void showSuccess(String message) {
        System.out.println("✓ " + message);
    }
}