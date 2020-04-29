package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.cli.commands.CommandManager;
import it.polimi.ingsw.psp1.santorini.model.EnumActionType;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CLIServerHandler implements ServerHandler, Runnable {

    private final Client client;
    private final CommandManager commandManager;

    private final List<Color> colors = Arrays.asList(
            Color.BACKGROUND_BRIGHT_BLUE,
            Color.BACKGROUND_RED,
            Color.BACKGROUND_GREEN);

    private final Map<String, Color> playerColorMap;

    private final List<PlayerData> playerDataList;
    private final Map<Power, List<Point>> blockedMoves;
    private final List<Point> validMoves;
    private final List<Power> powerList;
    private String playerName;

    private GameMap gameMap;
    private boolean shouldShowInteraction;

    private EnumRequestType lastRequest;
    private EnumTurnState lastTurnState;

    public CLIServerHandler(Client client) {
        this.client = client;

        this.playerDataList = new ArrayList<>();
        this.blockedMoves = new HashMap<>();
        this.validMoves = new ArrayList<>();
        this.powerList = new ArrayList<>();
        this.playerColorMap = new HashMap<>();

        this.shouldShowInteraction = false;
        this.gameMap = new GameMap();
        this.lastRequest = null;
        this.lastTurnState = null;

        this.commandManager = new CommandManager();

        new Thread(this).start();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String result = commandManager.runCommand(client, this, scanner.nextLine());
            PrintUtils.printCommand();
//            PrintUtils.clearFrom(PrintUtils.getCommandCoords().y + 1);
            PrintUtils.printFromCommand("Last action: " + result, 0, 2, true);
        }
    }

    @Override
    public void handleKeepAlive(ServerKeepAlive packet) {
        // segnalo il fatto che il client è ancora connesso
    }

    @Override
    public void handleGameData(ServerGameData packet) {
        GameMap map = packet.getGameMap();
        List<PlayerData> playerList = packet.getPlayerData();

        playerList.stream()
                .map(PlayerData::getName)
                .filter(p -> !playerColorMap.containsKey(p))
                .forEach(p -> playerColorMap.put(p, colors.stream()
                        .filter(c -> !playerColorMap.containsValue(c)).findAny().get()));

        this.playerDataList.clear();
        this.playerDataList.addAll(playerList);

        this.gameMap = map;

//        PrintUtils.clearBoard();

        this.lastTurnState = packet.getTurnState();

        PrintUtils.printPlayerInfo(getPlayerName(), playerList, lastTurnState, playerColorMap, shouldShowInteraction);
        PrintUtils.stampMap(map, playerList, playerColorMap);

        PrintUtils.printCommand();
    }

    @Override
    public void handleRequest(ServerAskRequest packet) {
        EnumRequestType action = packet.getRequestType();
        String toStamp;

        switch (action) {
            case SELECT_NAME:
                PrintUtils.clear();
                toStamp = String.format("Choose a nickname: use '%s' command",
                        Color.BLUE + "setname" + Color.RESET);
                break;
            case CHOOSE_POWERS:
                toStamp = String.format("Choose God Powers for this game: use '%s' command",
                        Color.BLUE + "selectpower" + Color.RESET);
                break;
            case SELECT_POWER:
                toStamp = String.format("Choose your God Power: use '%s' command",
                        Color.BLUE + "selectpower" + Color.RESET);
                break;
            case SELECT_STARTING_PLAYER:
                toStamp = String.format("Choose the starting player: use '%s' command",
                        Color.BLUE + "start" + Color.RESET);
                break;
            case PLACE_WORKER:
                toStamp = String.format("Place a worker: use '%s' command",
                        Color.BLUE + "placeworker" + Color.RESET);
                break;
            case SELECT_SQUARE:
                toStamp = String.format("Select square to %s: use '%s' command",
                        Color.RED + lastTurnState.toString() + Color.RESET,
                        Color.BLUE + "select" + Color.RESET);
                break;
            case SELECT_WORKER:
                toStamp = String.format("Select worker: use '%s' command",
                        Color.BLUE + "selectworker" + Color.RESET);
                break;
            case DISCONNECT:
                toStamp = Color.RED + "Unfortunately the game has forcefully ended, please disconnect";
                break;
            default:
                toStamp = packet.getRequestType().toString();
                break;
        }

        PrintUtils.printFromCommand("Last request: " + toStamp, 0, -2, true);
        PrintUtils.printCommand();

        this.lastRequest = action;
    }

    @Override
    public void handlePlayerUpdate(ServerSendPlayerUpdate packet) {
        Optional<PlayerData> updated = getPlayerDataList().stream()
                .filter(p -> p.getName().equals(packet.getPlayerData().getName()))
                .findFirst();

        updated.ifPresent(playerData -> playerDataList.set(playerDataList.indexOf(playerData), packet.getPlayerData()));

        shouldShowInteraction = packet.shouldShowInteraction();

        if (shouldShowInteraction && isYourTurn()) {
            PrintUtils.printFromCommand(Color.RED + "You can interact this turn" + Color.RESET,
                    0, -1, true);
        } else {
            PrintUtils.clearRow(0, PrintUtils.getCommandCoords().y - 1);
        }

        PrintUtils.printPlayerInfo(getPlayerName(), playerDataList, packet.getPlayerState(),
                playerColorMap, shouldShowInteraction);
    }

    @Override
    public void handleReceivedMoves(ServerMovePossibilities packet) {
        List<Point> validMoves = packet.getValidMoves();
        Map<Power, List<Point>> blockedMoves = packet.getBlockedMoves();

        getValidMoves().clear();
        getValidMoves().addAll(validMoves);

        getBlockedMoves().clear();
        getBlockedMoves().putAll(blockedMoves);

        PrintUtils.stampMap(gameMap, playerDataList, playerColorMap);
        PrintUtils.printValidMoves(validMoves, blockedMoves);
    }

    @Override
    public void handleError(ServerInvalidPacket packet) {
        PrintUtils.printFromCommand(packet.getError(), 0, 2, true);
    }

    @Override
    public void handlePlayerMove(ServerPlayerMove serverPlayerMove) {
        PlayerData playerInfo = serverPlayerMove.getPlayerData();
        EnumActionType move = serverPlayerMove.getMoveType();
        String name = playerInfo.getName();
    }

    @Override
    public void handlePowerList(ServerPowerList serverPowerList) {
        List<Power> powerList = serverPowerList.getAvailablePowers();
        PrintUtils.printPowerList(powerList);

        getPowerList().clear();
        getPowerList().addAll(powerList);
    }

    @Override
    public void handlePlayerConnected(ServerConnectedToGame serverConnectedToGame) {

    }

    public PlayerData getPlayerData() {
        return playerDataList.stream()
                .filter(p -> p.getName().equals(this.playerName))
                .findFirst().get();
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

    public Map<String, Color> getPlayerColorMap() {
        return playerColorMap;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isYourTurn() {
        return getPlayerDataList().size() > 0 && getPlayerDataList().get(0).getName().equals(getPlayerName());
    }
}
