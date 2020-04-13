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

        while(true) {
            CLIServerHandler cliServerHandler = new CLIServerHandler();

            List<Power> list = Arrays.asList(new Apollo(), new Artemis(), new Athena(), new Atlas(), new Chronus(),
                    new Demeter(), new Hephaestus(), new Hestia(), new Minotaur(), new Mortal(), new Pan(),
                    new Poseidon(), new Prometheus(), new Triton(), new Zeus());
            ServerPowerList spl = new ServerPowerList(list);

            cliServerHandler.handlePowerList(spl);


            HashMap<PlayerData, EnumTurnState> hash = new HashMap<>();
            GameMap map = PrintUtils.createMap();

            ServerGameData sgd = new ServerGameData(map, hash);

            cliServerHandler.handleSendGameData(sgd);

            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();

        }

    }
}
