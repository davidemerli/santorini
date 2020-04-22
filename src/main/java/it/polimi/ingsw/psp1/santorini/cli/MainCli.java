package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.powers.*;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerAskRequest;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerGameData;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerPowerList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MainCli {

    public static void main(String[] args) {
        CLIServerHandler cliServerHandler = new CLIServerHandler();

        // lista di tutte le divinità
        List<Power> list = Arrays.asList(new Apollo(), new Artemis(), new Athena(), new Atlas(), new Chronus(),
                new Demeter(), new Hephaestus(), new Hestia(), new Minotaur(), new Mortal(), new Pan(),
                new Poseidon(), new Prometheus(), new Triton(), new Zeus());
        ServerPowerList spl = new ServerPowerList(list);

        // first clear
        PrintUtils.clearBoard();

        // info generali sulla partita
        HashMap<PlayerData, EnumTurnState> hash = new HashMap<>();
        PlayerData p1 = new PlayerData("Alberto", new Athena());
        PlayerData p2 = new PlayerData("Maurizio", new Apollo());
        hash.put(p1, EnumTurnState.BUILD);
        hash.put(p2, EnumTurnState.END_TURN);
        GameMap map = PrintUtils.createMap();
        ServerGameData sgd = new ServerGameData(map, hash);

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
            cliServerHandler.handleSendGameData(sgd);

            int x = 20 + (4 * (PrintUtils.size + 1));
            PrintUtils.print(command, x, 0, true);

            String s = scanner.nextLine();
        }

    }
}

// ingrandire mappa
