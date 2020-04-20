package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.awt.*;

public class ServerPlayerMove implements Packet<ServerHandler> {

    private abstract static class PlayerTurn {
        private final Point where;
        private final Worker worker;

        public PlayerTurn(Worker worker, Point where) {
            this.worker = worker;
            this.where = where;
        }

        public Worker getWorker() {
            return worker;
        }

        public Point getDest() {
            return where;
        }
    }

    public static class PlayerMove extends PlayerTurn {

        private final Point from;

        public PlayerMove(Worker worker, Point from, Point where) {
            super(worker, where);
            this.from = from;
        }

        public Point getSrc() {
            return from;
        }
    }

    public static class PlayerBuild extends PlayerTurn {

        public PlayerBuild(Worker worker, Point where) {
            super(worker, where);
        }

    }

    private final PlayerData playerData;
    private final EnumMoveType moveType;
    private final PlayerTurn move;

    public ServerPlayerMove(PlayerData playerData, PlayerTurn move, EnumMoveType moveType) {
        this.playerData = playerData;
        this.moveType = moveType;
        this.move = move;
    }

    @Override
    public void processPacket(ServerHandler netHandler) {
        netHandler.handlePlayerMove(this);
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public EnumMoveType getMoveType() {
        return moveType;
    }

    public PlayerTurn getMove() {
        return move;
    }
}
