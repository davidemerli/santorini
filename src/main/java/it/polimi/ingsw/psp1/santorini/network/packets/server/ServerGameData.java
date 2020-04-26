package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.util.List;

/**
 * Stores as a packet all relevant information about a game
 *
 * The first element in 'players' is the CURRENT PLAYER playing its turn
 */
public class ServerGameData implements Packet<ServerHandler> {

    private final GameMap gameMap;
    private final EnumTurnState gameState;
    private final List<PlayerData> players;

    public ServerGameData(GameMap gameMap, List<PlayerData> players, EnumTurnState gameState) {
        this.gameMap = gameMap;
        this.players = players;
        this.gameState = gameState;
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
}


