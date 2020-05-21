package it.polimi.ingsw.psp1.santorini.gui;

import it.polimi.ingsw.psp1.santorini.gui.controllers.*;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class GuiServerHandler extends ServerHandler {

    private final GuiObserver guiObserver;

    private final List<GuiController> controllers = new ArrayList<>();

    public GuiServerHandler(Client client) {
        super(client);

        guiObserver = new GuiObserver(client, this);
//        controllers.add(gameSceneController = new GameSceneController());
        IpSelectionController.getInstance().addObserver(guiObserver);
        ChooseGameSceneController.getInstance().addObserver(guiObserver);
        //TODO: Add other observers
    }

    @Override
    public void handleGameData(ServerGameData packet) {
        super.handleGameData(packet);


    }

    @Override
    public void handleRequest(ServerAskRequest packet) {
        super.handleRequest(packet);

    }

    @Override
    public void handlePlayerUpdate(ServerSendPlayerUpdate packet) {
        super.handlePlayerUpdate(packet);

    }

    @Override
    public void handleReceivedMoves(ServerMovePossibilities packet) {
        super.handleReceivedMoves(packet);

    }

    @Override
    public void handleError(ServerInvalidPacket packet) {

    }

    @Override
    public void handlePlayerMove(ServerPlayerMove serverPlayerMove) {
    }

    @Override
    public void handlePowerList(ServerPowerList serverPowerList) {
        super.handlePowerList(serverPowerList);

    }

    @Override
    public void handlePlayerConnected(ServerConnectedToGame serverConnectedToGame) {

    }

}