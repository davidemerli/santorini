package it.polimi.ingsw.psp1.santorini.cli;

import it.polimi.ingsw.psp1.santorini.model.EnumActionType;
import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.network.Client;
import it.polimi.ingsw.psp1.santorini.network.ServerHandler;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumTurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.server.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLIServerHandler extends ServerHandler implements Runnable {

    private final CommandManager commandManager;

    public CLIServerHandler(Client client) {
        super(client);
        this.commandManager = new CommandManager();

        new Thread(this).start();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Used to run CLI
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            PrintUtils.clearRow(0, PrintUtils.getCommandCoords().y + 3);
            PrintUtils.resetCursor();

            String result = commandManager.runCommand(client, this, scanner.nextLine());
            PrintUtils.printCommand();
            PrintUtils.printFromCommand("Last action: " + result, 0, 2, true);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Receives information about game, players and players' state from server
     *
     * @param packet contains information about game
     */
    @Override
    public void handleGameData(ServerGameData packet) {
        super.handleGameData(packet);

        GameMap map = packet.getGameMap();
        List<PlayerData> playerList = packet.getPlayerData();

        PrintUtils.printPlayerInfo(getPlayerName(), playerList, lastTurnState, playerColorMap, shouldShowInteraction);
        PrintUtils.printMap(map);
        PrintUtils.printWorkers(playerList, playerColorMap);
        PrintUtils.printCommand();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Receives a request from the server to be processed
     *
     * @param packet contains a server request
     */
    @Override
    public void handleRequest(ServerAskRequest packet) {
        super.handleRequest(packet);

        String toStamp;

        switch (packet.getRequestType()) {
            case SELECT_NAME:
                PrintUtils.clear();
                toStamp = String.format("Choose a nickname: use '%s' command",
                        Color.BLUE + "setname" + Color.RESET);
                break;
            case CHOOSE_POWERS:
                toStamp = String.format("Choose God Powers for this game: use '%s' command",
                        Color.BLUE + "selectpower" + Color.RESET);
                break;
            case SELECT_POWER:
                toStamp = String.format("Choose your God Power: use '%s' command",
                        Color.BLUE + "selectpower" + Color.RESET);
                break;
            case SELECT_STARTING_PLAYER:
                toStamp = String.format("Choose the starting player: use '%s' command",
                        Color.BLUE + "start" + Color.RESET);
                break;
            case PLACE_WORKER:
                toStamp = String.format("Place a worker: use '%s' command",
                        Color.BLUE + "placeworker" + Color.RESET);
                break;
            case SELECT_SQUARE:
                toStamp = String.format("Select square to %s: use '%s' command",
                        Color.RED + lastTurnState.toString() + Color.RESET,
                        Color.BLUE + "select" + Color.RESET);
                break;
            case SELECT_WORKER:
                toStamp = String.format("Select worker: use '%s' command",
                        Color.BLUE + "selectworker" + Color.RESET);
                break;
            case DISCONNECT:
                toStamp = Color.RED + "Unfortunately the game has forcefully ended, please disconnect";
                break;
            default:
                toStamp = packet.getRequestType().toString();
                break;
        }

        PrintUtils.printFromCommand("Last request: " + toStamp, 0, -2, true);
        PrintUtils.printCommand();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Receives information about current player, players's state and power activation
     *
     * @param packet contains information about current player
     */
    @Override
    public void handlePlayerUpdate(ServerSendPlayerUpdate packet) {
        super.handlePlayerUpdate(packet);

        if (shouldShowInteraction && isYourTurn()) {
            PrintUtils.printFromCommand(String.format("You can use command '%s' to '%s'",
                    Color.RED + "interact" + Color.RESET,
                    Color.BLUE + packet.getPlayerData().getPower().getInteraction() + Color.RESET),
                    0, -1, true);
        } else {
            PrintUtils.clearRow(0, PrintUtils.getCommandCoords().y - 1);
        }

        PrintUtils.printPlayerInfo(getPlayerName(), playerDataList, packet.getPlayerState(),
                playerColorMap, shouldShowInteraction);

        if (packet.getPlayerData().getName().equals(getPlayerName())) {
            if (packet.getPlayerState() == EnumTurnState.WIN) {
                PrintUtils.printWin();
            }

            if (packet.getPlayerState() == EnumTurnState.LOSE) {
                PrintUtils.printLoser();
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Receives all valid moves of the current players
     *
     * @param packet contains valid moves and blocked moves
     */
    @Override
    public void handleReceivedMoves(ServerMovePossibilities packet) {
        super.handleReceivedMoves(packet);

        PrintUtils.printMap(gameMap);
        PrintUtils.printWorkers(playerDataList, playerColorMap);
        PrintUtils.printValidMoves(getValidMoves(), getBlockedMoves());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Receives an error from the server
     *
     * @param packet contains an error
     */
    @Override
    public void handleError(ServerInvalidPacket packet) {
        PrintUtils.printFromCommand(Color.RED + packet.getError(), 0, 3, true);
    }

    @Override
    public void handlePlayerMove(ServerPlayerMove serverPlayerMove) {
        EnumActionType move = serverPlayerMove.getMoveType();

        if (move == EnumActionType.MOVE) {
            ServerPlayerMove.PlayerMove playerMove = (ServerPlayerMove.PlayerMove) serverPlayerMove.getMove();

            Point vector = playerMove.getDest().subtract(playerMove.getSrc());

            PrintUtils.printArrow(EnumArrow.fromVector(vector), playerMove.getSrc());
        } else if (move == EnumActionType.BUILD) {
            PrintUtils.printMapBackground();
        }
    }

    @Override
    public void handlePowerList(ServerPowerList serverPowerList) {
        super.handlePowerList(serverPowerList);

        PrintUtils.printPowerList(getPowerList());
    }

    @Override
    public void handlePlayerConnected(ServerConnectedToGame packet) {
        super.handlePlayerConnected(packet);

        if (packet.getName().equals(getPlayerName())) {
            String string = String.format("You ('%s') joined a game! (game ID '%s')",
                    Color.BLUE + "" + packet.getName() + Color.RESET,
                    Color.RED + "" + packet.getGameID() + Color.RESET);

            PrintUtils.printFromCommand(string, 0, -2, true);
        } else {
            String string = String.format("'%s' joined the game!",
                    Color.BLUE + "" + packet.getName() + Color.RESET);

            Optional<PlayerData> optPlayer = getPlayerDataList().stream()
                    .filter(p -> p.getName().equals(packet.getName())).findFirst();

            if (optPlayer.isPresent()) {
                PrintUtils.printFromCommand(string, 0, -4 - getPlayerDataList().indexOf(optPlayer.get()), true);
            } else {
                throw new IllegalStateException("Invalid local username");
            }
        }
    }

    @Override
    public void onDisconnect() {

    }


}
