package domuscontrol.controller;

import domuscontrol.model.automation.Automation;
import domuscontrol.model.automation.SensorCondition;
import domuscontrol.model.automation.SensorCondition.Operator;
import domuscontrol.model.devices.*;
import domuscontrol.model.house.House;
import domuscontrol.model.house.Room;
import domuscontrol.model.scenario.Scenario;
import domuscontrol.model.scheduling.Schedule;
import domuscontrol.model.users.AdminUser;
import domuscontrol.model.users.RegularUser;
import domuscontrol.utils.actions.*;

public class PopulateStateManager {
    private PopulateStateManager() {
    }

    public static void seed(DomusController ctrl) {
        if (ctrl == null)
            throw new IllegalArgumentException("O controlador não pode ser nulo.");
        seedUtilizadores(ctrl);
        seedCasaFamiliaSilva(ctrl);
        seedCasaVazia(ctrl);
        seedCasaHugo(ctrl);
        seedAutomacoes(ctrl);
        seedEscalonamentos(ctrl);
        seedCenarios(ctrl);
    }

    private static void seedUtilizadores(DomusController ctrl) {
        AdminUser tiago = new AdminUser("1", "Tiago Fernandes", "tiago@gmail.com", "1");
        ctrl.addUser(tiago);
        RegularUser joao = new RegularUser("2", "Joao Pinheiro", "joao@gmail.com", "1");
        ctrl.addUser(joao);
        AdminUser hugo = new AdminUser("3", "Hugo Moura", "hugo@gmail.com", "1");
        ctrl.addUser(hugo);
    }

    private static void seedCasaFamiliaSilva(DomusController ctrl) {
        AdminUser tiago = (AdminUser) ctrl.getUserById("1");
        RegularUser joao = (RegularUser) ctrl.getUserById("2");
        House casa = new House("1", "Casa da Praia", "Rua do Mar", tiago);
        ctrl.addHouse(casa);
        tiago.addOwnedHouse(casa);
        joao.addGuestHouse(casa);
        casa.addUser(joao);
        Room sala = new Room("1", "Sala");
        SmartLight luzSala = new SmartLight("1", "led", "led", 10.0, false);
        SmartLight luzSalaColor = new SmartLight("2", "led", "led cores", 12.0, true);
        SmartSpeaker coluna = new SmartSpeaker("3", "JBL", "colunaboa", 6.0);
        Blinds persianaSala = new Blinds("4", "presiana", "persiana123", 8.0);
        ctrl.addDevice(luzSala);
        ctrl.addDevice(luzSalaColor);
        ctrl.addDevice(coluna);
        ctrl.addDevice(persianaSala);
        sala.addDevice(luzSala);
        luzSala.setUsed(true);
        sala.addDevice(luzSalaColor);
        luzSalaColor.setUsed(true);
        sala.addDevice(coluna);
        coluna.setUsed(true);
        sala.addDevice(persianaSala);
        persianaSala.setUsed(true);
        casa.addRoom(sala);
        Room cozinha = new Room("2", "Cozinha");
        Relay tomadaCozinha = new Relay("5", "generica", "tomada1", 2.0);
        TemperatureSensor tempCozinha = new TemperatureSensor("6", "generica", "sensor temp", 0.5);
        tempCozinha.setValue(22.5);
        ctrl.addDevice(tomadaCozinha);
        ctrl.addDevice(tempCozinha);
        cozinha.addDevice(tomadaCozinha);
        tomadaCozinha.setUsed(true);
        cozinha.addDevice(tempCozinha);
        tempCozinha.setUsed(true);
        casa.addRoom(cozinha);
        Room quarto = new Room("3", "Quarto");
        SmartLight luzQuarto = new SmartLight("7", "led", "led quarto", 9.0, false);
        TemperatureSensor tempQuarto = new TemperatureSensor("8", "generica", "sensor temp2", 0.5);
        tempQuarto.setValue(20.0);
        Blinds persianaQuarto = new Blinds("9", "generica", "persiana456", 8.0);
        ctrl.addDevice(luzQuarto);
        ctrl.addDevice(tempQuarto);
        ctrl.addDevice(persianaQuarto);
        quarto.addDevice(luzQuarto);
        luzQuarto.setUsed(true);
        quarto.addDevice(tempQuarto);
        tempQuarto.setUsed(true);
        quarto.addDevice(persianaQuarto);
        persianaQuarto.setUsed(true);
        casa.addRoom(quarto);
        Room garagem = new Room("4", "Garagem");
        GarageDoor portaoGaragem = new GarageDoor("10", "generica", "portao1", 15.0);
        RainSensor sensorChuva = new RainSensor("11", "generica", "sensor chuva", 1.0);
        sensorChuva.setValue(0.0);
        LightSensor sensorLuz = new LightSensor("12", "generica", "sensor luz", 0.5);
        sensorLuz.setValue(300.0);
        ctrl.addDevice(portaoGaragem);
        ctrl.addDevice(sensorChuva);
        ctrl.addDevice(sensorLuz);
        garagem.addDevice(portaoGaragem);
        portaoGaragem.setUsed(true);
        garagem.addDevice(sensorChuva);
        sensorChuva.setUsed(true);
        garagem.addDevice(sensorLuz);
        sensorLuz.setUsed(true);
        casa.addRoom(garagem);
    }

    private static void seedCasaVazia(DomusController ctrl) {
        AdminUser tiago = (AdminUser) ctrl.getUserById("1");
        House casaVazia = new House("4", "Casa Vazia", "Rua do Deserto", tiago);
        ctrl.addHouse(casaVazia);
        tiago.addOwnedHouse(casaVazia);
    }

    private static void seedCasaHugo(DomusController ctrl) {
        AdminUser hugo = (AdminUser) ctrl.getUserById("3");
        House apto = new House("2", "Casa da Uni", "Rua de Gualtar 12, Braga", hugo);
        ctrl.addHouse(apto);
        hugo.addOwnedHouse(apto);
        Room sala = new Room("5", "Sala");
        SmartLight luzSalaApto = new SmartLight("13", "led", "led sala2", 8.0, true);
        SmartSpeaker colunaApto = new SmartSpeaker("14", "JBL", "coluna2", 7.0);
        ctrl.addDevice(luzSalaApto);
        ctrl.addDevice(colunaApto);
        sala.addDevice(luzSalaApto);
        luzSalaApto.setUsed(true);
        sala.addDevice(colunaApto);
        colunaApto.setUsed(true);
        apto.addRoom(sala);
        Room escritorio = new Room("6", "Escritório");
        SmartLight luzEsc = new SmartLight("15", "led", "led escritorio", 6.0, false);
        TemperatureSensor tempEsc = new TemperatureSensor("16", "generica", "sensor temp3", 0.5);
        tempEsc.setValue(21.0);
        Relay tomadaEsc = new Relay("17", "generica", "tomada2", 2.0);
        ctrl.addDevice(luzEsc);
        ctrl.addDevice(tempEsc);
        ctrl.addDevice(tomadaEsc);
        escritorio.addDevice(luzEsc);
        luzEsc.setUsed(true);
        escritorio.addDevice(tempEsc);
        tempEsc.setUsed(true);
        escritorio.addDevice(tomadaEsc);
        tomadaEsc.setUsed(true);
        apto.addRoom(escritorio);
    }

    private static void seedAutomacoes(DomusController ctrl) {
        Blinds persianaSala = (Blinds) ctrl.getDeviceById("4");
        Blinds persianaQuarto = (Blinds) ctrl.getDeviceById("9");
        RainSensor sensorChuva = (RainSensor) ctrl.getDeviceById("11");
        LightSensor sensorLuz = (LightSensor) ctrl.getDeviceById("12");
        SmartLight luzSala = (SmartLight) ctrl.getDeviceById("1");
        SmartLight luzSalaColor = (SmartLight) ctrl.getDeviceById("2");
        SensorCondition condicaoChuva = new SensorCondition(sensorChuva, Operator.GREATER_THAN, 2.0);
        Automation fechaPersianas = new Automation("Fechar persianas se chover", condicaoChuva);
        fechaPersianas.addAction(new SetOpeningAction(persianaSala, 0));
        fechaPersianas.addAction(new SetOpeningAction(persianaQuarto, 0));
        ctrl.addAutomation(fechaPersianas);
        SensorCondition condicaoLuz = new SensorCondition(sensorLuz, Operator.LESS_THAN, 100.0);
        Automation ligaLuzes = new Automation("Ligar luzes quando escurece", condicaoLuz);
        ligaLuzes.addAction(new TurnOnAction(luzSala));
        ligaLuzes.addAction(new TurnOnAction(luzSalaColor));
        ctrl.addAutomation(ligaLuzes);
    }

    private static void seedEscalonamentos(DomusController ctrl) {
        Blinds persianaSala = (Blinds) ctrl.getDeviceById("4");
        Blinds persianaQuarto = (Blinds) ctrl.getDeviceById("9");
        SmartLight luzSala = (SmartLight) ctrl.getDeviceById("1");
        SmartLight luzSalaColor = (SmartLight) ctrl.getDeviceById("2");
        SmartLight luzQuarto = (SmartLight) ctrl.getDeviceById("7");
        SmartSpeaker coluna = (SmartSpeaker) ctrl.getDeviceById("3");
        Schedule abrePersianas = new Schedule("Abrir persianas de manha", 7, 30, true);
        abrePersianas.addAction(new SetOpeningAction(persianaSala, 100));
        abrePersianas.addAction(new SetOpeningAction(persianaQuarto, 100));
        ctrl.addSchedule(abrePersianas);
        Schedule acendeSala = new Schedule("Ligar sala ao anoitecer", 19, 0, true);
        acendeSala.addAction(new TurnOnAction(luzSala));
        acendeSala.addAction(new TurnOnAction(luzSalaColor));
        ctrl.addSchedule(acendeSala);
        Schedule fechaPersianas = new Schedule("Fechar persianas ao fim do dia", 21, 0, true);
        fechaPersianas.addAction(new SetOpeningAction(persianaSala, 0));
        fechaPersianas.addAction(new SetOpeningAction(persianaQuarto, 0));
        ctrl.addSchedule(fechaPersianas);
        Schedule desligaTudo = new Schedule("Desligar tudo a meia-noite", 0, 0, true);
        desligaTudo.addAction(new TurnOffAction(luzSala));
        desligaTudo.addAction(new TurnOffAction(luzSalaColor));
        desligaTudo.addAction(new TurnOffAction(luzQuarto));
        desligaTudo.addAction(new TurnOffAction(coluna));
        ctrl.addSchedule(desligaTudo);
    }

    private static void seedCenarios(DomusController ctrl) {
        AdminUser tiago = (AdminUser) ctrl.getUserById("1");
        SmartLight luzSala = (SmartLight) ctrl.getDeviceById("1");
        SmartLight luzSalaColor = (SmartLight) ctrl.getDeviceById("2");
        SmartLight luzQuarto = (SmartLight) ctrl.getDeviceById("7");
        SmartSpeaker coluna = (SmartSpeaker) ctrl.getDeviceById("3");
        Blinds persianaSala = (Blinds) ctrl.getDeviceById("4");
        Blinds persianaQuarto = (Blinds) ctrl.getDeviceById("9");
        GarageDoor portao = (GarageDoor) ctrl.getDeviceById("10");
        Relay tomada = (Relay) ctrl.getDeviceById("5");
        Scenario sairDeCasa = new Scenario("Sair de Casa", tiago);
        sairDeCasa.addAction(new TurnOffAction(luzSala));
        sairDeCasa.addAction(new TurnOffAction(luzSalaColor));
        sairDeCasa.addAction(new TurnOffAction(luzQuarto));
        sairDeCasa.addAction(new TurnOffAction(coluna));
        sairDeCasa.addAction(new TurnOffAction(tomada));
        sairDeCasa.addAction(new SetOpeningAction(persianaSala, 0));
        sairDeCasa.addAction(new SetOpeningAction(persianaQuarto, 0));
        sairDeCasa.addAction(new SetOpeningAction(portao, 0));
        ctrl.addScenario(sairDeCasa);
        Scenario jantarAmigos = new Scenario("Jantar com Amigos", tiago);
        jantarAmigos.addAction(new SetBrightnessAction(luzSalaColor, 40));
        jantarAmigos.addAction(new TurnOnAction(luzSalaColor));
        jantarAmigos.addAction(new SetVolumeAction(coluna, 30));
        jantarAmigos.addAction(new TurnOnAction(coluna));
        jantarAmigos.addAction(new SetOpeningAction(persianaSala, 0));
        ctrl.addScenario(jantarAmigos);
        Scenario deitar = new Scenario("Deitar", tiago);
        deitar.addAction(new TurnOffAction(luzSala));
        deitar.addAction(new TurnOffAction(luzSalaColor));
        deitar.addAction(new TurnOffAction(coluna));
        deitar.addAction(new SetOpeningAction(persianaSala, 0));
        deitar.addAction(new SetOpeningAction(persianaQuarto, 0));
        deitar.addAction(new SetBrightnessAction(luzQuarto, 10));
        deitar.addAction(new TurnOnAction(luzQuarto));
        ctrl.addScenario(deitar);
        Scenario acordar = new Scenario("Acordar", tiago);
        acordar.addAction(new SetOpeningAction(persianaSala, 100));
        acordar.addAction(new SetOpeningAction(persianaQuarto, 100));
        acordar.addAction(new TurnOffAction(luzQuarto));
        acordar.addAction(new SetBrightnessAction(luzSala, 100));
        acordar.addAction(new TurnOnAction(luzSala));
        ctrl.addScenario(acordar);
    }
}