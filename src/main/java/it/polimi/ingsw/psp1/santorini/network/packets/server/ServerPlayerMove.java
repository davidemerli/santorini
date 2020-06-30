package it.polimi.ingsw.psp1.santorini.network.packets.server;

import it.polimi.ingsw.psp1.santorini.model.EnumActionType;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.Packet;

import java.io.Serializable;

/**
 * Server packet containing the player action
 * Could be move, place worker or build
 */
public class ServerPlayerMove implements Packet<ServerHandler> {

    private final PlayerData playerData;
    private final EnumActionType moveType;
    private final PlayerTurn move;

    /**
     * Generic constructor
     *
     * @param playerData player information
     * @param move       player move
     * @param moveType   action type
     */
    public ServerPlayerMove(PlayerData playerData, PlayerTurn move, EnumActionType moveType) {
        this.playerData = playerData;
        this.moveType = moveType;
        this.move = move;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toString(moveType, move);
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

    /**
     * Manages the generic place worker action
     */
    public static class PlayerPlaceWorker extends PlayerTurn {
        /**
         * Manages the place worker action
         *
         * @param worker to be placed
         */
        public PlayerPlaceWorker(Worker worker) {
            super(worker, worker.getPosition());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Place Worker [" + getWorker().toString() + "]";
        }
    }

    /**
     * Manages the generic move action
     */
    public static class PlayerMove extends PlayerTurn {

        private final Point from;

        /**
         * Manages the move action
         *
         * @param worker current worker
         * @param from   initial position
         * @param where  final position
         */
        public PlayerMove(Worker worker, Point from, Point where) {
            super(worker, where);
            this.from = from;
        }

        public Point getSrc() {
            return from;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Move [" + getWorker().toString() + " - from: " + getSrc() + " - to: " + getDest() + "]";
        }
    }

    /**
     * Manages the generic build action
     */
    public static class PlayerBuild extends PlayerTurn {

        private final boolean forceDome;

        /**
         * Manages the build action
         *
         * @param worker    current worker
         * @param where     position
         * @param forceDome true if a dome must be built
         */
        public PlayerBuild(Worker worker, Point where, boolean forceDome) {
            super(worker, where);
            this.forceDome = forceDome;
        }

        /**
         * Manages the forced dome build
         *
         * @return true if a dome must be built
         */
        public boolean forceDome() {
            return forceDome;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Build [" + getWorker().toString() + " - " + getDest() + "]";
        }
    }
}
