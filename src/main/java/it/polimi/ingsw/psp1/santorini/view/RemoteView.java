package it.polimi.ingsw.psp1.santorini.view;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.network.server.ClientConnectionHandler;

import java.awt.*;

public class RemoteView extends View {

    private final ClientConnectionHandler connection;

    public RemoteView(Player player, ClientConnectionHandler connection) {
        super(player);
        this.connection = connection;
    }

    @Override
    public void notifyError(String error) {

    }

    @Override
    public void mapChange(GameMap map) {

    }

    @Override
    public void playerMove(Player player, EnumMoveType moveType, Worker worker, Point where) {

    }

    @Override
    public void playerBuild(Player player, EnumMoveType moveType, Worker worker, Point where) {

    }

    @Override
    public void playerWinner(Player player) {

    }

    @Override
    public void playerLoser(Player player) {

    }
}
