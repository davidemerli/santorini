package it.polimi.ingsw.psp1.santorini.gui;

import it.polimi.ingsw.psp1.santorini.gui.controllers.IpSelectionController;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.packets.client.*;

import java.util.List;

public class GuiObserver {

    private final Client client;
    private final GuiServerHandler serverHandler;

    public GuiObserver(Client client, GuiServerHandler serverHandler) {
        this.client = client;
        this.serverHandler = serverHandler;
    }

    public static void undoPressed(GuiObserver guiObserver) {
        //TODO
    }

    public void onMoveSelected(Point point) {
        client.sendPacket(new ClientSelectSquare(point));
    }

    public void onWorkerSelected(Point p) {
        client.sendPacket(new ClientSelectWorker(p));
    }

    public void connectToServer(String ip, int port) {
        IpSelectionController.getInstance().startConnectionAnimation();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            client.connectToServer(ip, port);

            if (client.isConnected()) {
                IpSelectionController.getInstance().changeToNameSelection();
            }

            IpSelectionController.getInstance().stopConnectionAnimation();
        }).start();
    }

    public void onNameSelection(String name) {
        client.sendPacket(new ClientSetName(name));
        serverHandler.setPlayerName(name);
    }

    public void createGame(int players) {
        client.sendPacket(new ClientCreateGame(players));
    }

    public void joinGame(int players) {
        client.sendPacket(new ClientJoinGame(players, -1));
    }

    public void selectPowers(List<Power> selectedPowers) {
        selectedPowers.forEach(p -> client.sendPacket(new ClientChoosePower(p)));
    }

    public void interactPressed() {
        client.sendPacket(new ClientToggleInteraction());
    }
}
