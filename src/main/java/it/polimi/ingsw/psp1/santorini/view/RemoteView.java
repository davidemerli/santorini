package it.polimi.ingsw.psp1.santorini.view;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;
import it.polimi.ingsw.psp1.santorini.network.server.ClientConnectionHandler;
import it.polimi.ingsw.psp1.santorini.observer.ConnectionObserver;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RemoteView extends View {

    private final ClientConnectionHandler connection;

    private class PacketReceiver implements ConnectionObserver {

        @Override
        public void processPowerList(List<Power> powerList) {
            notifyObservers(o -> o.selectPowers(RemoteView.this, getPlayer(), powerList));
        }

        @Override
        public void processSquareSelection(Point square) {
            notifyObservers(o -> o.selectSquare(RemoteView.this, getPlayer(), square));
        }

        @Override
        public void processToggleInteraction() {
            notifyObservers(o -> o.toggleInteraction(RemoteView.this, getPlayer()));
        }

        @Override
        public void handlePlayerForfeit() {
            //TODO: ff
        }

        @Override
        public void processKeepAlive() {
            //TODO: keep alive, maybe process in connection?
        }

        @Override
        public void processRequestGameData() {
            //TODO: request game data,  maybe process in connection?
        }

        @Override
        public void processWorkerSelection(Point workerPosition) {
            notifyObservers(o -> o.selectWorker(RemoteView.this, getPlayer(), workerPosition));
        }

        @Override
        public void processStartingPlayerSelection(String name) {
            notifyObservers(o -> o.selectStartingPlayer(RemoteView.this, getPlayer(), name));
        }
    }

    public RemoteView(Player player, ClientConnectionHandler connection) {
        super(player);
        this.connection = connection;
        this.connection.addObserver(new PacketReceiver());
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
        EnumTurnState turnState = EnumTurnState.fromTurnState(game.getTurnState());
        boolean interaction = game.getTurnState().shouldShowInteraction(player);

        ServerSendPlayerUpdate packet = new ServerSendPlayerUpdate(toData(player), turnState, interaction);
        connection.sendPacket(packet);
    }

    @Override
    public void gameUpdate(Game game) {
        List<PlayerData> players = game.getPlayerList().stream()
                .map(this::toData).collect(Collectors.toList());
        EnumTurnState turnState = EnumTurnState.fromTurnState(game.getTurnState());

        ServerGameData packet = new ServerGameData(game.getMap(), players, turnState);
        connection.sendPacket(packet);
    }

    @Override
    public void availableMovesUpdate(List<Point> validMoves, Map<Power, List<Point>> blockedMoves) {
        ServerMovePossibilities packet = new ServerMovePossibilities(validMoves, blockedMoves);
        connection.sendPacket(packet);
    }

    @Override
    public void requestToPlayer(Player player, EnumRequestType requestType) {
        if (connection.getPlayer().isPresent() && connection.getPlayer().get().equals(player)) {
            connection.sendPacket(new ServerAskRequest(requestType));
        }
    }

    @Override
    public void sendPowerList(Player player, List<Power> availablePowers) {
        if (this.getPlayer().equals(player)) {
            connection.sendPacket(new ServerPowerList(availablePowers));
        }
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
