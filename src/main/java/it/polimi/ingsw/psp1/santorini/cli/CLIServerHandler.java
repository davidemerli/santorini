package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIServerHandler implements ServerHandler {

    private final List<PlayerData> playerDataList;
    private final Map<Power, List<Point>> blockedMoves;
    private final List<Point> validMoves;
    private boolean shouldShowInteraction;

    public CLIServerHandler() {
        this.playerDataList = new ArrayList<>();
        this.blockedMoves = new HashMap<>();
        this.validMoves = new ArrayList<>();
        this.shouldShowInteraction = false;
    }

    @Override
    public void handleKeepAlive(ServerKeepAlive packet) {
        // segnalo il fatto che il client è ancora connesso
    }

    @Override
    public void handleSendGameData(ServerGameData packet) {
        GameMap map = packet.getGameMap();
        List<PlayerData> playerList = packet.getPlayerData();

        playerDataList.clear();
        playerDataList.addAll(playerList);

        PrintUtils.printPlayerInfo(playerList);
        PrintUtils.stampMap(map);

        // aggiungere lo stato attuale
    }

    @Override
    public void handleRequest(ServerAskRequest packet) {
        EnumRequestType action = packet.getRequestType();
        String toStamp;

//        PrintUtils.printFromCommand(packet.getRequestType().toString(), 0, -2, true);

        switch (action) {
            case SELECT_NAME:
                toStamp = "Choose your name: ";
                PrintUtils.print(toStamp, 2, 0, false);
                break;
            case CHOOSE_POWERS:
                toStamp = "Choose the gods who will play: use select command";
                PrintUtils.print(toStamp, 0, 10, false);
                break;
            case SELECT_POWER:
                toStamp = "Choose your god: ";
                PrintUtils.print(toStamp, 16, 0, true);
                break;
            case SELECT_SQUARE:
                toStamp = "Select square: ";
                PrintUtils.print(toStamp, 16, 0, true);
                break;
            case SELECT_WORKER:
                toStamp = "Select worker: ";
                PrintUtils.print(toStamp, 16, 0, true);
            default:
                break;
        }
        // TODO: sistemare i cursori
    }

    @Override
    public void handlePlayerUpdate(ServerSendPlayerUpdate packet) {
        // il server aggiorna lo stato di un player per far proseguire la partita
    }

    @Override
    public void handleReceivedMoves(ServerMovePossibilities packet) {
        List<Point> validMoves = packet.getValidMoves();
        Map<Power, List<Point>> blockedMoves = packet.getBlockedMoves();
        PrintUtils.printValidMoves(validMoves, blockedMoves);
    }

    @Override
    public void handleError(ServerInvalidPacket packet) {
        PrintUtils.printFromCommand(packet.getError(), 0, 2, true);
    }

    @Override
    public void handlePlayerMove(ServerPlayerMove serverPlayerMove) {
        // il server segnala al client che un player ha effettuato una mossa (move o build)
        // prendo le info dal packet
        PlayerData playerInfo = serverPlayerMove.getPlayerData();
        EnumMoveType move = serverPlayerMove.getMoveType(); // --> in ServerGameData
        // stampo dicendo che il player x ha fatto la mossa y
        String name = playerInfo.getName();
        // stampo la mappa aggiornata per far visualizzare i cambiamenti
    }

    @Override
    public void handlePowerList(ServerPowerList serverPowerList) {
        List<Power> powerList = serverPowerList.getAvailablePowers();
        PrintUtils.printGodList(powerList);

        /*
        StringJoiner powers = new StringJoiner("\t", "", "");
        IntStream.range(0, powerList.size())
                .forEach(i -> powers.add(String.format("%d) %s", i + 1, powerList.get(i).getClass().getSimpleName())));

        */

    }

    public List<PlayerData> getPlayerDataList() {
        return playerDataList;
    }

    public List<Point> getValidMoves() {
        return validMoves;
    }

    public Map<Power, List<Point>> getBlockedMoves() {
        return blockedMoves;
    }

    public boolean getShowInteraction() {
        return shouldShowInteraction;
    }

    public void setShouldShowInteraction(boolean shouldShowInteraction) {
        this.shouldShowInteraction = shouldShowInteraction;
    }
}

// salvare il precedente sempre (mappa)
