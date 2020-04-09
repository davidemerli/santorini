package it.polimi.ingsw.psp1.santorini.network.packets;

import it.polimi.ingsw.psp1.santorini.model.turn.*;

public enum EnumTurnState {
    PRE_GAME,
    BEGIN_TURN,
    MOVE,
    BUILD,
    WORKER_PLACING,
    END_TURN;

    public static EnumTurnState fromTurnState(TurnState state) {
        if (state instanceof BeginTurn) {
            return BEGIN_TURN;
        } else if (state instanceof Move) {
            return MOVE;
        } else if (state instanceof Build) {
            return BUILD;
        } else if (state instanceof WorkerPlacing) {
            return WORKER_PLACING;
        } else if (state instanceof EndTurn) {
            return END_TURN;
        } else if (state == null) {
            return PRE_GAME;
        }

        throw new IllegalArgumentException("Invalid turn state");
    }
}
