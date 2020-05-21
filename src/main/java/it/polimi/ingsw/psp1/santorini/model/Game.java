package it.polimi.ingsw.psp1.santorini.model;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.*;
import it.polimi.ingsw.psp1.santorini.model.turn.SelectPowers;
import it.polimi.ingsw.psp1.santorini.model.turn.TurnState;
import it.polimi.ingsw.psp1.santorini.network.packets.EnumRequestType;
import it.polimi.ingsw.psp1.santorini.observer.ModelObserver;
import it.polimi.ingsw.psp1.santorini.observer.Observable;

import java.util.*;
import java.util.stream.Collectors;

public class Game extends Observable<ModelObserver> {

    private final int gameID;

    private final int playerNumber;
    private final List<Power> availableGodList;
    private final List<Player> playerList;

    private TurnState turnState;

    private GameMap map;

    private boolean hasEnded;

    public Game(int gameID, int playerNumber) {
        this.gameID = gameID;

        this.playerNumber = playerNumber;
        this.availableGodList = new ArrayList<>();
        this.playerList = Collections.synchronizedList(new ArrayList<>());
        this.map = new GameMap();

        this.turnState = null;

        this.addPowers();
    }

    public void startGame() {
        shufflePlayers();
        setTurnState(new SelectPowers(this));

        notifyObservers(o -> o.gameUpdate(this));
    }

    private Optional<Player> getWinner() {
        Optional<Player> fulfilledWinCond = playerList.stream().filter(Player::hasWon).findFirst();

        List<Player> notLosers = playerList.stream().filter(p -> !p.hasLost()).collect(Collectors.toList());

        if (notLosers.isEmpty()) {
            throw new IllegalStateException("All players have lost");
        }

        if (fulfilledWinCond.isPresent()) {
            return fulfilledWinCond;
        } else if (notLosers.size() == 1) {
            setWinner(notLosers.get(0));
            return Optional.of(notLosers.get(0));
        }

        return Optional.empty();
    }

    public void setWinner(Player player) {
        if (!playerList.contains(player)) {
            throw new NoSuchElementException("Given player is not in this game");
        }

        player.setWinner(true);
        notifyObservers(o -> o.playerUpdate(this, player));
    }

    private void removeLoser() {
        getCurrentPlayer().removeAllWorkers();
        notifyObservers(o -> o.playerUpdate(this, getCurrentPlayer()));

        playerList.remove(getCurrentPlayer());
        nextTurn();
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public void addWorker(Player player, Point position) {
        Worker worker = new Worker(position);
        player.addWorker(worker);
        notifyObservers(o -> o.playerUpdate(this, player));
        notifyObservers(o -> o.playerPlaceWorker(player, worker));
    }

    public void buildBlock(Point position, boolean forceDome) {
        map.buildBlock(position, forceDome);

        notifyObservers(o -> o.gameUpdate(this));
    }

    /**
     * Used to get a Optional containing a worker if there is one on the given position
     *
     * @param position where we see if there is a worker or not
     * @return optional with the worker if present
     */
    public Optional<Worker> getWorkerOn(Point position) {
        return playerList.stream()
                .map(Player::getWorkers)
                .flatMap(List::stream)
                .filter(w -> w.getPosition().equals(position))
                .findFirst();
    }

    public Optional<Player> getPlayerOf(Worker worker) {
        return playerList.stream()
                .filter(p -> p.getWorkers().contains(worker))
                .findFirst();
    }

    /**
     * Used to move a worker on the map
     *
     * @param player      the owner of the worker
     * @param worker      the worker to be moved
     * @param newPosition the new worker position
     * @throws IndexOutOfBoundsException if you try to move a worker out of the map
     * @throws NoSuchElementException    if the given worker is not associated with the player
     */
    public void moveWorker(Player player, Worker worker, Point newPosition) {
        if (map.isPositionOutOfMap(newPosition)) {
            throw new IndexOutOfBoundsException("Given position is out of map");
        }

        Optional<Worker> optWorker = getWorkerOn(worker.getPosition());

        if (optWorker.isEmpty()) {
            throw new NoSuchElementException("Given worker is not present on the map");
        }

        Optional<Player> optPlayer = getPlayerOf(optWorker.get());

        if (optPlayer.isEmpty() || optPlayer.get() != player) {
            throw new UnsupportedOperationException("Player does not own given worker");
        }

        optWorker.get().setPosition(newPosition);

        notifyObservers(o -> o.gameUpdate(this));
    }

    public void startTurn() {
        if (playerList.isEmpty()) {
            throw new UnsupportedOperationException("Player list is empty");
        }

        notifyObservers(o -> o.gameUpdate(this));
        getPlayerList().forEach(p -> p.getPower().onBeginTurn(this.getCurrentPlayer(), this));
        notifyObservers(o -> o.playerUpdate(this, getCurrentPlayer()));

        if (getCurrentPlayer().hasLost() && playerNumber == 3) {
            removeLoser();
        }

        Optional<Player> optWinner = getWinner();

        if (optWinner.isPresent()) {
            getPlayerOpponents(optWinner.get()).forEach(this::setLoser);
            endGame();
        }
    }

    public void nextTurn() {
        shiftPlayers(-1);

        startTurn();
    }

    public void endTurn() {
        //TODO: wait X seconds before starting new turn

        getPlayerList().forEach(p -> p.getPower().onEndTurn(p, this));
        getCurrentPlayer().setSelectedWorker(null);
        getCurrentPlayer().unlockWorker();

        nextTurn();
    }

    public void shiftPlayers(int distance) {
        Collections.rotate(playerList, distance);

        notifyObservers(o -> o.gameUpdate(this));
    }

    public void shufflePlayers() {
        Collections.shuffle(playerList);
    }

    public void askRequest(Player player, EnumRequestType requestType) {
        notifyObservers(o -> o.requestToPlayer(player, requestType));
    }

    public synchronized void endGame() {
        this.hasEnded = true;
        removeAllObservers();
    }

    public void forceEndGame() {
        playerList.forEach(p -> askRequest(p, EnumRequestType.DISCONNECT));
        endGame();
    }

    public void setLoser(Player player) {
        if (!playerList.contains(player)) {
            throw new NoSuchElementException("Given player is not in this game");
        }

        player.setLoser(true);
        notifyObservers(o -> o.playerUpdate(this, player));
    }

    public Player getCurrentPlayer() {
        if (playerList.isEmpty()) {
            throw new UnsupportedOperationException("Player list is empty");
        }

        return playerList.get(0);
    }

    public List<Player> getPlayerList() {
        return Collections.unmodifiableList(playerList);
    }

    public List<Player> getPlayerOpponents(Player player) {
        return playerList.stream().filter(p -> p != player).collect(Collectors.toUnmodifiableList());
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public void setTurnState(TurnState turnState) {
        this.turnState = turnState;
        this.turnState.init();

        notifyObservers(o -> o.playerUpdate(this, getCurrentPlayer()));

        if (getCurrentPlayer().hasLost() && playerNumber == 3) {
            removeLoser();
        }

        Optional<Player> optWinner = getWinner();

        if (optWinner.isPresent()) {
            getPlayerOpponents(optWinner.get()).forEach(this::setLoser);
            endGame();
            return;
        }

        Optional<Worker> optWorker = getCurrentPlayer().getSelectedWorker();

        if (optWorker.isPresent()) {
            List<Point> validMoves = getTurnState().getValidMoves(getCurrentPlayer(), optWorker.get());
            Map<Power, List<Point>> blockedMoves = getTurnState().getBlockedMoves(getCurrentPlayer(), optWorker.get());

            notifyObservers(o -> o.availableMovesUpdate(getCurrentPlayer(), validMoves, blockedMoves));
        }
    }

    public List<Power> getAvailablePowers() {
        return availableGodList;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public boolean hasStarted() {
        return this.turnState != null;
    }

    public synchronized boolean hasEnded() {
        return this.hasEnded;
    }

    public int getGameID() {
        return gameID;
    }

    public void addPowers() {
        availableGodList.add(new Apollo());
        availableGodList.add(new Artemis());
        availableGodList.add(new Athena());
        availableGodList.add(new Atlas());
        availableGodList.add(new Chronus());
        availableGodList.add(new Demeter());
        availableGodList.add(new Hephaestus());
        availableGodList.add(new Hestia());
        availableGodList.add(new Minotaur());
        availableGodList.add(new Mortal());
        availableGodList.add(new Pan());
        availableGodList.add(new Poseidon());
        availableGodList.add(new Prometheus());
        availableGodList.add(new Triton());
        availableGodList.add(new Zeus());

        availableGodList.removeIf(power -> !power.getPlayableIn().contains(this.playerNumber));
    }
}
