package it.polimi.ingsw.psp1.santorini.network;

import it.polimi.ingsw.psp1.santorini.network.packets.client.*;

public interface ClientHandler extends NetHandler {

    void handlePlayerSetName(ClientSetName packet);

    void handleCreateGame(ClientCreateGame packet);

    void handleJoinGame(ClientJoinGame packet);

    void handlePowerChoosing(ClientChoosePower clientChoosePower);

    void handleSquareSelect(ClientSelectSquare clientSelectSquare);

    void handleInteractionToggle(ClientToggleInteraction clientToggleInteraction);

    void handlePlayerForfeit(ClientForfeit clientForfeit);

    void handleKeepAlive(ClientKeepAlive clientKeepAlive);

    void handleRequestGameData(ClientRequestGameData clientRequestGameData);

    void handleWorkerSelection(ClientSelectWorker clientSelectWorker);

    void handleSetPlayerNumber(ClientSetPlayerNumber clientSetPlayerNumber);
}
