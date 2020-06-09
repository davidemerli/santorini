package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores as a packet all relevant information about a game
 * <p>
 * The first element in 'players' is the CURRENT PLAYER playing its turn
 */
public class ServerGameData implements Packet<ServerHandler> {

    private final GameMap gameMap;
    private final EnumTurnState gameState;
    private final List<PlayerData> players;
    private final boolean forced;

    public ServerGameData(GameMap gameMap, List<PlayerData> players, EnumTurnState gameState, boolean forced) {
        this.gameMap = gameMap;
        this.players = players;
        this.gameState = gameState;
        this.forced = forced;
    }

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handleGameData(this);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public EnumTurnState getTurnState() {
        return gameState;
    }

    public List<PlayerData> getPlayerData() {
        return players;
    }

    public boolean isForced() {
        return forced;
    }

    @Override
    public String toString() {
        String workers = players.stream().map(PlayerData::toString).collect(Collectors.joining(","));

        return toString(gameState, gameMap, workers);
    }
}


