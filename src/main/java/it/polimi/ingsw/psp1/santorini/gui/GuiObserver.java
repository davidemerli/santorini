package it.polimi.ingsw.psp1.santorini.gui;

import it.polimi.ingsw.psp1.santorini.gui.controllers.IpSelectionController;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.*;
import javafx.application.Platform;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Turns gui information into packets
 */
public class GuiObserver {

    private final Client client;
    private final GuiServerHandler serverHandler;

    /**
     * Creates a gui observer using client and server handler
     *
     * @param client        current client
     * @param serverHandler valid server handler
     */
    public GuiObserver(Client client, GuiServerHandler serverHandler) {
        this.client = client;
        this.serverHandler = serverHandler;
    }

    /**
     * Manages a request of undo
     */
    public void undoPressed() {
        if (serverHandler.isYourTurn()) {
            client.sendPacket(new ClientUndo());
        }
    }

    /**
     * Manages the selection of a square
     *
     * @param point on the map
     */
    public void onMoveSelected(Point point) {
        if (serverHandler.isYourTurn()) {
            client.sendPacket(new ClientSelectSquare(point));
        }
    }

    /**
     * Manages the selection of a worker
     *
     * @param p on the map
     */
    public void onWorkerSelected(Point p) {
        if (serverHandler.isYourTurn()) {
            client.sendPacket(new ClientSelectWorker(p));
        }
    }

    /**
     * Manages the connection to the server
     *
     * @param ip   server ip
     * @param port socket port
     */
    public void connectToServer(String ip, int port) {
        IpSelectionController.getInstance().startConnectionAnimation();

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            client.connectToServer(ip, port);

            Platform.runLater(() -> {
                if (client.isConnected()) {
                    IpSelectionController.getInstance().changeToNameSelection();
                    IpSelectionController.getInstance().stopConnectionAnimation();
                } else {
                    IpSelectionController.getInstance().showConnectionError();
                }
            });
        }, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Manages the name selection
     *
     * @param name to be set
     */
    public void onNameSelection(String name) {
        client.sendPacket(new ClientSetName(name));
        serverHandler.setPlayerName(name);
    }

    /**
     * Manages the creation of the game
     *
     * @param players number of players
     */
    public void createGame(int players) {
        client.sendPacket(new ClientCreateGame(players));
    }

    /**
     * Manages the join into the game
     *
     * @param players number of players
     */
    public void joinGame(int players) {
        client.sendPacket(new ClientJoinGame(players, null));
    }

    /**
     * Manages the gods selection
     *
     * @param selectedPowers list of the selected gods
     */
    public void selectPowers(List<Power> selectedPowers) {
        selectedPowers.forEach(p -> client.sendPacket(new ClientChoosePower(p)));
    }

    /**
     * Manages the pressure of the interaction button
     */
    public void interactPressed() {
        if (serverHandler.isYourTurn()) {
            client.sendPacket(new ClientToggleInteraction());
        }
    }

    /**
     * Manages the selection of the starting player
     *
     * @param playerName starting player
     */
    public void selectStartingPlayer(String playerName) {
        client.sendPacket(new ClientSelectStartingPlayer(playerName));
    }

    public void disconnect() {
        client.disconnect();
    }
}
