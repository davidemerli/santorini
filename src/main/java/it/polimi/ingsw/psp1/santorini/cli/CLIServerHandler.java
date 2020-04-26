package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CLIServerHandler implements ServerHandler {

    private final List<PlayerData> playerDataList;
    private final Map<Power, List<Point>> blockedMoves;
    private final List<Point> validMoves;
    private final List<Power> powerList;
    private GameMap gameMap;
    private boolean shouldShowInteraction;

    private EnumRequestType lastRequest;

    public CLIServerHandler() {
        this.playerDataList = new ArrayList<>();
        this.blockedMoves = new HashMap<>();
        this.validMoves = new ArrayList<>();
        this.powerList = new ArrayList<>();
        this.shouldShowInteraction = false;
        this.gameMap = null;
        this.lastRequest = null;
    }

    @Override
    public void handleKeepAlive(ServerKeepAlive packet) {
        // segnalo il fatto che il client è ancora connesso
    }

    @Override
    public void handleGameData(ServerGameData packet) {
        GameMap map = packet.getGameMap();
        List<PlayerData> playerList = packet.getPlayerData();

        this.playerDataList.clear();
        this.playerDataList.addAll(playerList);

        this.gameMap = map;

        PrintUtils.clearBoard();

        PrintUtils.printPlayerInfo(playerList, packet.getTurnState());
        PrintUtils.stampMap(map, playerList);

        PrintUtils.printCommand();
    }

    @Override
    public void handleRequest(ServerAskRequest packet) {
        EnumRequestType action = packet.getRequestType();
        String toStamp;

        switch (action) {
            case SELECT_NAME:
                toStamp = "Choose your name: ";
                break;
            case CHOOSE_POWERS:
                toStamp = "Choose the gods who will play: use 'selectpower' command";
                break;
            case SELECT_POWER:
                toStamp = "Choose your god: ";
                break;
            case SELECT_SQUARE:
                toStamp = "Select square: ";
                break;
            case SELECT_WORKER:
                toStamp = "Select worker: ";
                break;
            default:
                toStamp = packet.getRequestType().toString();
        }

        PrintUtils.printFromCommand(toStamp, 0, -2, true);

        this.lastRequest = action;
    }

    @Override
    public void handlePlayerUpdate(ServerSendPlayerUpdate packet) {
        Optional<PlayerData> updated = getPlayerDataList().stream()
                .filter(p -> p.getName().equals(packet.getPlayerData().getName()))
                .findFirst();
        updated.ifPresent(playerData -> playerDataList.set(playerDataList.indexOf(playerData), packet.getPlayerData()));

        PrintUtils.printPlayerInfo(playerDataList, packet.getPlayerState());
    }

    @Override
    public void handleReceivedMoves(ServerMovePossibilities packet) {
        List<Point> validMoves = packet.getValidMoves();
        Map<Power, List<Point>> blockedMoves = packet.getBlockedMoves();

        getValidMoves().clear();
        getValidMoves().addAll(validMoves);

        getBlockedMoves().clear();
        getBlockedMoves().putAll(blockedMoves);

        PrintUtils.stampMap(gameMap, playerDataList);
        PrintUtils.printValidMoves(validMoves, blockedMoves);
    }

    @Override
    public void handleError(ServerInvalidPacket packet) {
        PrintUtils.printFromCommand(packet.getError(), 0, 2, true);
    }

    @Override
    public void handlePlayerMove(ServerPlayerMove serverPlayerMove) {
        PlayerData playerInfo = serverPlayerMove.getPlayerData();
        EnumMoveType move = serverPlayerMove.getMoveType();
        String name = playerInfo.getName();
    }

    @Override
    public void handlePowerList(ServerPowerList serverPowerList) {
        List<Power> powerList = serverPowerList.getAvailablePowers();
        PrintUtils.printPowerList(powerList);

        getPowerList().clear();
        getPowerList().addAll(powerList);

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

    public List<Power> getPowerList() {
        return powerList;
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

    public EnumRequestType getLastRequest() {
        return lastRequest;
    }
}
