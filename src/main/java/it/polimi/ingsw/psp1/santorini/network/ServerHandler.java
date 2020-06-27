package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.cli.Color;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.client.ClientKeepAlive;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;

import java.util.*;

public abstract class ServerHandler implements NetHandler {
    protected final Client client;

    protected final Map<String, Color> playerColorMap;

    protected final List<PlayerData> playerDataList;
    protected final Map<Power, List<Point>> blockedMoves;
    protected final List<Point> validMoves;
    protected final List<Power> powerList;
    private final List<Color> colors = List.of(Color.BG_WORKER_BLUE, Color.BG_WORKER_RED, Color.BG_WORKER_ORANGE);

    protected String playerName;
    protected GameMap gameMap;
    protected boolean shouldShowInteraction;
    protected EnumRequestType lastRequest;
    protected EnumTurnState lastTurnState;

    public ServerHandler(Client client) {
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
    }

    /**
     * Handles Keep alive packet
     */
    public void handleKeepAlive() {
        client.sendPacket(new ClientKeepAlive());
    }

    /**
     * Handles Packet with updated game information
     *
     * @param packet to send
     */
    public void handleGameData(ServerGameData packet) {
        GameMap map = packet.getGameMap();
        List<PlayerData> playerList = packet.getPlayerData();

        playerDataList.clear();
        playerDataList.addAll(playerList);

        if (playerColorMap.size() != playerList.size()) {
            List<PlayerData> copy = new ArrayList<>(playerDataList);
            copy.sort(Comparator.comparing(PlayerData::getName));

            playerColorMap.clear();

            for (int i = 0; i < copy.size(); i++) {
                playerColorMap.put(copy.get(i).getName(), colors.get(i));
            }
        }

        gameMap = map;

        lastTurnState = packet.getTurnState();
    }

    /**
     * Handles Packet with a type request to client
     *
     * @param packet to send
     */
    public void handleRequest(ServerAskRequest packet) {
        this.lastRequest = packet.getRequestType();
    }

    /**
     * Handles Packet with updated player information
     *
     * @param packet to send
     */
    public void handlePlayerUpdate(ServerSendPlayerUpdate packet) {
        Optional<PlayerData> updated = getPlayerDataList().stream()
                .filter(p -> p.getName().equals(packet.getPlayerData().getName()))
                .findFirst();

        updated.ifPresent(playerData -> playerDataList.set(playerDataList.indexOf(playerData), packet.getPlayerData()));

        shouldShowInteraction = packet.shouldShowInteraction();
        lastTurnState = packet.getPlayerState();
    }

    /**
     * Handles Packet with possible moves
     *
     * @param packet to send
     */
    public void handleReceivedMoves(ServerMovePossibilities packet) {
        getValidMoves().clear();
        getValidMoves().addAll(packet.getValidMoves());

        getBlockedMoves().clear();
        getBlockedMoves().putAll(packet.getBlockedMoves());
    }

    /**
     * Handles Packet with a message error
     *
     * @param packet to send
     */
    public abstract void handleError(ServerInvalidPacket packet);

    /**
     * Handles Packet with player's move
     *
     * @param packet to send
     */
    public abstract void handlePlayerMove(ServerPlayerMove packet);

    /**
     * Handles Packet with available gods to choose
     *
     * @param packet to send
     */
    public void handlePowerList(ServerPowerList packet) {
        getPowerList().clear();
        getPowerList().addAll(packet.getAvailablePowers());
    }

    /**
     * Adds a player in the players list
     *
     * @param packet to send
     */
    public void handlePlayerConnected(ServerConnectedToGame packet) {
        playerDataList.add(new PlayerData(packet.getUsername(), null, List.of()));
    }

    public Optional<PlayerData> getPlayerData() {
        return playerDataList.stream()
                .filter(p -> p.getName().equals(this.playerName))
                .findFirst();
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isYourTurn() {
        return !getPlayerDataList().isEmpty() && getPlayerDataList().get(0).getName().equals(playerName);
    }

    /**
     * Resets all data received from the server
     *
     * Used on disconnections or on a new game
     */
    public void reset() {
        playerDataList.clear();
        blockedMoves.clear();
        validMoves.clear();
        powerList.clear();
        playerColorMap.clear();

        shouldShowInteraction = false;
        gameMap = new GameMap();
        lastRequest = null;
        lastTurnState = null;
    }

    public abstract void onDisconnect();
}
