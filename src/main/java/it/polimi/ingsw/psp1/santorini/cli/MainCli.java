package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.powers.*;
import it.polimi.ingsw.psp1.santorini.model.turn.Move;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientCreateGame;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientSetName;
import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerAskRequest;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerGameData;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerPowerList;

import java.util.*;

public class MainCli {

    public static void main(String[] args) throws InterruptedException {
//        mainTest(args);

        Client client = new Client(new CLIServerHandler());

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please insert server ip: ");
        String serverIp = scanner.nextLine();
        System.out.print("Please insert server port: ");
        int port = scanner.nextInt();
//
        client.connectToServer(serverIp, port);

        Thread.sleep(1000);

        scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println("NAME: " + name);

        client.sendPacket(new ClientSetName(name));

        System.out.println("Game name: ");
        String gameName = scanner.nextLine();
        System.out.println("# players: ");
        int players = scanner.nextInt();

        client.sendPacket(new ClientCreateGame(gameName, players));
    }

    public static void mainTest(String[] args) {
        CLIServerHandler cliServerHandler = new CLIServerHandler();

        // lista di tutte le divinità
        List<Power> list = Arrays.asList(new Apollo(), new Artemis(), new Athena(), new Atlas(), new Chronus(),
                new Demeter(), new Hephaestus(), new Hestia(), new Minotaur(), new Mortal(), new Pan(),
                new Poseidon(), new Prometheus(), new Triton(), new Zeus());
        ServerPowerList spl = new ServerPowerList(list);

        // first clear
        PrintUtils.clearBoard();

        // info generali sulla partita
        List<PlayerData> hash = new ArrayList<>();
        PlayerData p1 = new PlayerData("Alberto", new Athena(), Collections.emptyList());
        PlayerData p2 = new PlayerData("Maurizio", new Apollo(), Collections.emptyList());
        hash.add(p1);
        hash.add(p2);
        GameMap map = PrintUtils.createMap();
        ServerGameData sgd = new ServerGameData(map, hash, EnumTurnState.BUILD);

        // creo uno scanner
        Scanner scanner = new Scanner(System.in);

        /*
        PrintUtils.setCursor(16, 0);
        System.out.print("Command: ");
        */

        // chiedo il nome al giocatore
        EnumRequestType action = EnumRequestType.SELECT_NAME;
        ServerAskRequest sar1 = new ServerAskRequest(action);
        cliServerHandler.handleRequest(sar1);

        // pulisco la console
        PrintUtils.clearBoard();

        // stampo la lista di tutte le divinità
        cliServerHandler.handlePowerList(spl);

        // dopo aver chiesto il nome chiedo di selezionare 2 gods dalla lista
        action = EnumRequestType.CHOOSE_POWERS;
        ServerAskRequest sar2 = new ServerAskRequest(action);
        cliServerHandler.handleRequest(sar2);

        // pulisco la console
        PrintUtils.clearBoard();

        String command = "Command: ";

        while (true) {

            // PrintUtils.setCursor(3,0);
            // update matrice
            PrintUtils.updateMap(map);
            // stampo la matrice partendo da 8, 0 (sovrascrivo la vecchia)
            sgd.processPacket(cliServerHandler);

//            int y = 2 + (GameMap.SIDE_LENGTH * (PrintUtils.size + 1));
//            PrintUtils.print(command, 0, y, true);
            PrintUtils.printCommand();

            String s = scanner.nextLine();
        }

    }
}
