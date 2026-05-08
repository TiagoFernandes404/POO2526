package domuscontrol.view;

import java.util.List;

public class HouseUI {

    private final Menu menu;

    public HouseUI() {
        this.menu = new Menu("Gerir Casas", new String[] {
                "Criar casa",
                "Adicionar divisão",
                "Listar casas",
                "Ver detalhes de casa",
                "Adicionar convidado",
                "Voltar"
        });
    }

    public int showMenu() {
        return menu.show();
    }

    public String[] readHouseData(List<String> users) {
        System.out.println("\n--- Criar Casa ---");
        System.out.println("Utilizadores disponíveis:");
        for (String u : users)
            System.out.println("  " + u);
        String id = Menu.readLine("ID: ");
        String name = Menu.readLine("Nome: ");
        String address = Menu.readLine("Morada: ");
        String ownerId = Menu.readLine("ID do utilizador administrador: ");
        return new String[] { id, name, address, ownerId };
    }

    public String[] readRoomData(List<String> houses) {
        System.out.println("\n--- Adicionar Divisão ---");
        System.out.println("Casas disponíveis:");
        for (String h : houses)
            System.out.println("  " + h);
        String houseId = Menu.readLine("ID da casa: ");
        String roomName = Menu.readLine("Nome da divisão: ");
        return new String[] { houseId, roomName };
    }

    public String readHouseId(List<String> houses) {
        System.out.println("\nCasas disponíveis:");
        for (String h : houses)
            System.out.println("  " + h);
        return Menu.readLine("ID da casa: ");
    }

    public String[] readGuestData(List<String> houses, List<String> users) {
        System.out.println("\n--- Adicionar Convidado ---");
        System.out.println("Casas disponíveis:");
        for (String h : houses)
            System.out.println("  " + h);
        String houseId = Menu.readLine("ID da casa: ");
        System.out.println("Utilizadores disponíveis:");
        for (String u : users)
            System.out.println("  " + u);
        String userId = Menu.readLine("ID do utilizador convidado: ");
        return new String[] { houseId, userId };
    }

    public void displayHouses(List<String> houses) {
        System.out.println("\n--- Casas ---");
        if (houses.isEmpty()) {
            System.out.println("Nenhuma casa registada.");
            return;
        }
        for (String h : houses)
            System.out.println(h);
    }

    public void displayHouseDetails(String houseInfo, List<String> roomInfos, String totalConsumptionInfo) {
        System.out.println("\n--- Detalhes da Casa ---");
        System.out.println(houseInfo);
        System.out.println(totalConsumptionInfo);
        for (String r : roomInfos)
            System.out.println("  " + r);
    }

    public void showSuccess(String message) {
        System.out.println("Sucesso:" + message);
    }

    public void showError(String message) {
        System.out.println("Erro: " + message);
    }
}