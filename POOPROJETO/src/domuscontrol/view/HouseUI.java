package domuscontrol.view;

import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import java.util.List;

// View de casas - só lê input e mostra output
public class HouseUI {

    private final Menu menu;

    public HouseUI() {
        this.menu = new Menu("Gerir Casas", new String[]{
            "Criar casa",
            "Adicionar divisão",
            "Listar casas",
            "Ver detalhes de casa",
            "Adicionar convidado",
            "Voltar"
        });
    }

    // Mostra o menu e devolve a opção escolhida
    public int showMenu() {
        return menu.show();
    }

    // Lê os dados de uma nova casa
    // [0]=id, [1]=nome, [2]=morada, [3]=idDono
    public String[] readHouseData() {
        System.out.println("\n--- Criar Casa ---");
        String id = Menu.readLine("ID: ");
        String name = Menu.readLine("Nome: ");
        String address = Menu.readLine("Morada: ");
        String ownerId = Menu.readLine("ID do utilizador administrador: ");
        return new String[]{id, name, address, ownerId};
    }

    // Lê os dados de uma nova divisão
    // [0]=idCasa, [1]=nomeDivisão
    public String[] readRoomData() {
        System.out.println("\n--- Adicionar Divisão ---");
        String houseId = Menu.readLine("ID da casa: ");
        String roomName = Menu.readLine("Nome da divisão: ");
        return new String[]{houseId, roomName};
    }

    // Lê o id de uma casa
    public String readHouseId() {
        return Menu.readLine("ID da casa: ");
    }

    // Lê os dados para adicionar convidado
    // [0]=idCasa, [1]=idUtilizador
    public String[] readGuestData() {
        System.out.println("\n--- Adicionar Convidado ---");
        String houseId = Menu.readLine("ID da casa: ");
        String userId = Menu.readLine("ID do utilizador convidado: ");
        return new String[]{houseId, userId};
    }

    // Mostra a lista de casas
    public void displayHouses(List<House> houses) {
        System.out.println("\n--- Casas ---");
        if (houses.isEmpty()) {
            System.out.println("Nenhuma casa registada.");
            return;
        }
        for (House h : houses) {
            System.out.println(h);
        }
    }

    // Mostra os detalhes de uma casa com divisões e dispositivos
    public void displayHouseDetails(House house) {
        System.out.println("\n--- Detalhes da Casa ---");
        System.out.println(house);
        System.out.println("Consumo total atual: " + house.getTotalPowerConsumption() + " Wh");
        for (Room r : house.getRooms()) {
            System.out.println("  " + r);
            r.getDevices().forEach(d -> System.out.println("    " + d.getStatus()));
        }
    }

    // Mostra mensagem de sucesso
    public void showSuccess(String message) {
        System.out.println("✓ " + message);
    }

    // Mostra mensagem de erro
    public void showError(String message) {
        System.out.println("✗ Erro: " + message);
    }
}