package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.powers.*;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.server.PlayerData;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerGameData;
import it.polimi.ingsw.psp1.santorini.network.packets.server.ServerPowerList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MainCli {

    public static void main(String[] args) {
        CLIServerHandler cliServerHandler = new CLIServerHandler();

        List<Power> list = Arrays.asList(new Apollo(), new Artemis(), new Athena(), new Atlas(), new Chronus(),
                new Demeter(), new Hephaestus(), new Hestia(), new Minotaur(), new Mortal(), new Pan(),
                new Poseidon(), new Prometheus(), new Triton(), new Zeus());
        ServerPowerList spl = new ServerPowerList(list);

        cliServerHandler.handlePowerList(spl);

        HashMap<PlayerData, EnumTurnState> hash = new HashMap<>();
        PlayerData p1 = new PlayerData("Gino", new Athena());
        PlayerData p2 = new PlayerData("Maurizio", new Apollo());
        hash.put(p1, EnumTurnState.BUILD);
        hash.put(p2, EnumTurnState.END_TURN);
        GameMap map = PrintUtils.createMap();
        ServerGameData sgd = new ServerGameData(map, hash);
        cliServerHandler.handleSendGameData(sgd);

        Scanner scanner = new Scanner(System.in);
        PrintUtils.setCursor(16, 0);
        System.out.print("Command: ");

        while (true) {

            String s = scanner.nextLine();
            // update matrice
            PrintUtils.updateMap(map);
            // mi metto all'inizio della matrice
            // PrintUtils.setCursor(8,0); è superfluo, lo fa già la print di matrix
            // stampo la matrice partendo da 8, 0 (sovrascrivo la vecchia)
            cliServerHandler.handleSendGameData(sgd);

            // elimino il comando
            for (int i = PrintUtils.MAX_LENGTH; i >= 9; i--) {
                PrintUtils.setCursor(16, i);
                System.out.print(" ");
            }

            //PrintUtils.clearBoard();
            PrintUtils.setCursor(16, 10);
        }

    }
}