package it.polimi.ingsw.psp1.santorini.network.packets;

import it.polimi.ingsw.psp1.santorini.model.turn.*;

/**
 * Defines all states
 */
public enum EnumTurnState {
    SELECT_POWERS,
    CHOOSE_OWN_POWER,
    SELECT_STARTING_PLAYER,
    MOVE,
    BUILD,
    WORKER_PLACING,
    END_TURN,
    WIN,
    LOSE,
    END_GAME;

    /**
     * Checks the type of a generic state
     *
     * @param state generic state
     * @return type of the state
     */
    public static EnumTurnState fromTurnState(TurnState state) {
        if (state instanceof Build) {
            return BUILD;
        } else if (state instanceof ChoosePlayerPower) {
            return CHOOSE_OWN_POWER;
        } else if (state instanceof Move) {
            return MOVE;
        } else if (state instanceof SelectPowers) {
            return SELECT_POWERS;
        } else if (state instanceof WorkerPlacing) {
            return WORKER_PLACING;
        } else if (state instanceof SelectStartingPlayer) {
            return SELECT_STARTING_PLAYER;
        }

        throw new IllegalArgumentException("Invalid turn state");
    }
}
