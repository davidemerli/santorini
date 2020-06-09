package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.network.packets.client.*;

public interface ClientHandler extends NetHandler {

    void handlePlayerSetName(ClientSetName packet);

    void handleCreateGame(ClientCreateGame packet);

    void handleJoinGame(ClientJoinGame packet);

    void handlePowerChoosing(ClientChoosePower packet);

    void handleSquareSelect(ClientSelectSquare packet);

    void handleWorkerSelection(ClientSelectWorker packet);

    void handleSelectStartingPlayer(ClientSelectStartingPlayer packet);

    void handleInteractionToggle();

    void handleRequestGameData();

    void handlePlayerForfeit();

    void handleKeepAlive();

    void handleUndo();
}
