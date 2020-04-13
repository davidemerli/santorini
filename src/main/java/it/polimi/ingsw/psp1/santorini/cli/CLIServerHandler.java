package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.EnumMoveType;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType.*;

public class CLIServerHandler implements ServerHandler {

    @Override
    public void handleKeepAlive(ServerKeepAlive packet) {
        // segnalo il fatto che il client è ancora connesso
    }

    @Override
    public void handleSendGameData(ServerGameData packet) {
        GameMap map = packet.getGameMap();
        List<PlayerData> playerList = packet.getPlayers();
        playerList.get(0).getName();
        // stampo le info generali
        // stampo la mappa
        // stampo le info sui player
    }

    @Override
    public void handleRequest(ServerAskRequest packet) {
        // il server dice al client cosa deve fare
        EnumRequestType action = packet.getRequestType();
        if (action == SELECT_NAME) {
            System.out.print("Choose your name: ");
        } else if (action == CHOOSE_POWERS) {
            System.out.print("Choose the gods who will play: ");
        } else if (action == SELECT_POWER) {
            System.out.print("Choose your god: ");
        } else if (action == SELECT_SQUARE) {
            System.out.print("Select square: ");
        } else if (action == SELECT_WORKER) {
            System.out.print("Select worker: ");
        }
    }

    @Override
    public void handlePlayerUpdate(ServerSendPlayerUpdate packet) {
        // il server aggiorna lo stato di un player per far proseguire la partita
    }

    @Override
    public void handleReceivedMoves(ServerMovePossibilities packet) {
        List<Point> validMoves = packet.getValidMoves();
        List<Point> blockedMoves = packet.getBlockedMoves();
        // le validMoves non devono essere dentro le blockedMoves
        // stampo le mosse valide sulla map
        // stampo la mappa aggiornata
    }

    @Override
    public void handleError(ServerInvalidPacket packet) {
        // il server invia questo packet per segnalare un errore
        // l'errore viene mostrato in console
        String error = packet.getError();
        // stampo errore nella cli correttamente col cursore
        System.out.println(error);
    }

    @Override
    public void handlePlayerMove(ServerPlayerMove serverPlayerMove) {
        // il server segnala al client che un player ha effettuato una mossa (move o build)
        // prendo le info dal packet
        PlayerData playerInfo = serverPlayerMove.getPlayerData();
        EnumMoveType move = serverPlayerMove.getMoveType();
        // stampo dicendo che il player x ha fatto la mossa y
        String name = playerInfo.getName();
        System.out.print(name + ": ");
        // stampo la mappa aggiornata per far visualizzare i cambiamenti
    }

    @Override
    public void handlePowerList(ServerPowerList serverPowerList) {
        List<Power> powerList = serverPowerList.getPowerList();
        // stampo la power list
        for (int i = 0; i < powerList.size(); i++) {
            System.out.print(powerList.get(i));
        }
    }
}
