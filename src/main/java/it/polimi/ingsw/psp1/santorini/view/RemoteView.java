package it.polimi.ingsw.psp1.santorini.view;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;
import it.polimi.ingsw.psp1.santorini.network.server.ClientConnectionHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteView extends View {

    private final ClientConnectionHandler connection;

    public RemoteView(Player player, ClientConnectionHandler connection) {
        super(player);
        this.connection = connection;
    }

    @Override
    public void notifyError(String error) {
        connection.sendPacket(new ServerInvalidPacket(error));
    }

    @Override
    public void mapChange(Game game, GameMap map) {
        List<PlayerData> playerDataList = game.getPlayerList().stream()
                .map(this::toData).collect(Collectors.toList());

        ServerGameData packet = new ServerGameData(map, playerDataList, EnumTurnState.fromTurnState(game.getTurnState()));
        connection.sendPacket(packet);
    }

    @Override
    public void playerMove(Player player, EnumMoveType moveType, Worker worker, Point from, Point where) {
        ServerPlayerMove.PlayerMove move = new ServerPlayerMove.PlayerMove(worker, where, from);

        ServerPlayerMove packet = new ServerPlayerMove(toData(player), move, EnumMoveType.MOVE);
        connection.sendPacket(packet);
    }

    @Override
    public void playerBuild(Player player, EnumMoveType moveType, Worker worker, Point where) {
        ServerPlayerMove.PlayerBuild move = new ServerPlayerMove.PlayerBuild(worker, where);

        ServerPlayerMove packet = new ServerPlayerMove(toData(player), move, EnumMoveType.BUILD);
        connection.sendPacket(packet);
    }

    @Override
    public void playerUpdate(Game game, Player player) {
        ServerSendPlayerUpdate packet = new ServerSendPlayerUpdate(toData(player),
                EnumTurnState.fromTurnState(game.getTurnState()),
                player.hasWon(), player.hasLost());

        connection.sendPacket(packet);
    }

    /**
     * Gets the corresponding PlayerData
     *
     * @param player to get the data from
     * @return a PlayerData object
     */
    private PlayerData toData(Player player) {
        return new PlayerData(player.getName(), player.getPower(), new ArrayList<>(player.getWorkers()));
    }
}
