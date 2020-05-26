package it.polimi.ingsw.psp1.santorini.gui;

import it.polimi.ingsw.psp1.santorini.gui.controllers.*;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;
import javafx.scene.paint.Color;

public class GuiServerHandler extends ServerHandler {


    public GuiServerHandler(Client client) {
        super(client);

        GuiObserver guiObserver = new GuiObserver(client, this);

        ChooseGameSceneController.getInstance().addObserver(guiObserver);
        NameSelectionController.getInstance().addObserver(guiObserver);
        IpSelectionController.getInstance().addObserver(guiObserver);
        GameSceneController.getInstance().addObserver(guiObserver);
        ChoosePowersController.getInstance().addObserver(guiObserver);
    }

    @Override
    public void handleGameData(ServerGameData packet) {
        super.handleGameData(packet);


    }

    @Override
    public void handleRequest(ServerAskRequest packet) {
        super.handleRequest(packet);

        //TODO: request types to decently written strings

        GameSceneController.getInstance().showRequest(packet.getRequestType().toString());
    }

    @Override
    public void handlePlayerUpdate(ServerSendPlayerUpdate packet) {
        super.handlePlayerUpdate(packet);

        GameSceneController.getInstance().showInteract(packet.shouldShowInteraction());
    }

    @Override
    public void handleReceivedMoves(ServerMovePossibilities packet) {
        super.handleReceivedMoves(packet);

        if(isYourTurn()) {
            Gui.getInstance().changeSceneAsync(EnumScene.GAME, EnumTransition.DOWN);

            GameSceneController.getInstance().showValidMoves(packet.getValidMoves());
        }
    }

    @Override
    public void handleError(ServerInvalidPacket packet) {

    }

    @Override
    public void handlePlayerMove(ServerPlayerMove packet) {
        switch (packet.getMoveType()) {
            case MOVE:
                ServerPlayerMove.PlayerMove move = (ServerPlayerMove.PlayerMove) packet.getMove();

                GameSceneController.getInstance().moveWorker(move.getSrc(), move.getDest());
                break;
            case BUILD:
                ServerPlayerMove.PlayerBuild build = (ServerPlayerMove.PlayerBuild) packet.getMove();

                GameSceneController.getInstance().addBlockAt(build.getDest().x, build.getDest().y, build.forceDome());
                break;
            case PLACE_WORKER:
                ServerPlayerMove.PlayerPlaceWorker worker = (ServerPlayerMove.PlayerPlaceWorker) packet.getMove();

                GameSceneController.getInstance().addWorker(worker.getDest().x, worker.getDest().y,
                        getPlayerColorMap().get(packet.getPlayerData().getName()).getColor().darker(),
                        isYourTurn());
                break;
        }
    }

    @Override
    public void handlePowerList(ServerPowerList packet) {
        super.handlePowerList(packet);

        if (isYourTurn()) {
            Gui.getInstance().changeSceneAsync(EnumScene.CHOOSE_POWERS, EnumTransition.UP);
            ChoosePowersController.getInstance().addGods(packet.getAvailablePowers(), packet.getToSelect());
        }
    }

    @Override
    public void handlePlayerConnected(ServerConnectedToGame packet) {
        WaitGodSelectionController.getInstance().addPlayer(packet.getName());
    }

}