package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.EnumActionType;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.io.Serializable;

public class ServerPlayerMove implements Packet<ServerHandler> {

    private final PlayerData playerData;
    private final EnumActionType moveType;
    private final PlayerTurn move;

    public ServerPlayerMove(PlayerData playerData, PlayerTurn move, EnumActionType moveType) {
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

    public EnumActionType getMoveType() {
        return moveType;
    }

    public PlayerTurn getMove() {
        return move;
    }


    private abstract static class PlayerTurn implements Serializable {
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

    public static class PlayerPlaceWorker extends PlayerTurn {
        public PlayerPlaceWorker(Worker worker) {
            super(worker, worker.getPosition());
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
}
