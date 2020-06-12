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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Game extends Observable<ModelObserver> {

    private final ScheduledExecutorService pool;

    private final String gameID;

    private final int playerNumber;
    private final List<Power> availableGodList;
    private final List<Player> playerList;

    private TurnState turnState;

    private GameMap map;

    private boolean hasStarted;
    private boolean hasEnded;

    private GameState savedState;

    private ScheduledFuture<?> endTurnRoutine;

    public Game(String gameID, int playerNumber) {
        this.pool = Executors.newSingleThreadScheduledExecutor();

        this.gameID = gameID;

        this.playerNumber = playerNumber;
        this.availableGodList = new ArrayList<>();
        this.playerList = Collections.synchronizedList(new ArrayList<>());
        this.map = new GameMap();

        this.turnState = null;

        this.addPowers();
    }

    public void startGame() {
        hasStarted = true;

        shufflePlayers();

        pool.schedule(() -> {
            setTurnState(new SelectPowers());

            notifyObservers(o -> o.gameUpdate(this, false));
        }, 2, TimeUnit.SECONDS);
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
        if (playerList.contains(player)) {
            throw new IllegalArgumentException("Player is already in game");
        }

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

        notifyObservers(o -> o.gameUpdate(this, false));
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

        if (optPlayer.isEmpty() || !optPlayer.get().equals(player)) {
            throw new UnsupportedOperationException("Player does not own given worker");
        }

        notifyObservers(o -> o.playerMove(player, optWorker.get(), optWorker.get().getPosition(), newPosition));

        optPlayer.get().moveWorker(optWorker.get(), newPosition);
        notifyObservers(o -> o.gameUpdate(this, false));
    }

    public void startTurn() {
        if (playerList.isEmpty()) {
            throw new UnsupportedOperationException("Player list is empty");
        }

        notifyObservers(o -> o.gameUpdate(this, false));
        getPlayerList().forEach(p -> p.getPower().onBeginTurn(getCurrentPlayer(), this));
        notifyObservers(o -> o.playerUpdate(this, getCurrentPlayer()));

        if (getCurrentPlayer().hasLost() && playerNumber == 3) {
            removeLoser();
        }

        Optional<Player> optWinner = getWinner();

        if (optWinner.isPresent()) {
            getPlayerOpponents(optWinner.get()).forEach(this::setLoser);
            endGame();
        }

        saveState();
    }

    public void nextTurn() {
        shiftPlayers(-1);

        startTurn();
    }

    public void endTurn() {
        if (endTurnRoutine == null || endTurnRoutine.isDone()) {
            endTurnRoutine = pool.schedule(() -> {
                getPlayerList().forEach(p -> p.getPower().onEndTurn(p, this));
                getPlayerList().forEach(p -> p.setSelectedWorker(null));
                getPlayerList().forEach(Player::unlockWorker);

                nextTurn();
            }, 5, TimeUnit.SECONDS);
        }
    }

    public void shiftPlayers(int distance) {
        Collections.rotate(playerList, distance);

        notifyObservers(o -> o.gameUpdate(this, false));
    }

    public void shufflePlayers() {
        Collections.shuffle(playerList);
    }

    public void askRequest(Player player, EnumRequestType requestType) {
        notifyObservers(o -> o.requestToPlayer(player, requestType));
    }

    public synchronized void endGame() {
        hasEnded = true;
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
        return playerList.stream().filter(p -> !p.equals(player)).collect(Collectors.toUnmodifiableList());
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public void setTurnState(TurnState newTurn) {
        turnState = newTurn;
        turnState.init(this);

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
            List<Point> validMoves = getTurnState()
                    .getValidMoves(this, getCurrentPlayer(), optWorker.get());
            Map<Power, List<Point>> blockedMoves = getTurnState()
                    .getBlockedMoves(this, getCurrentPlayer(), optWorker.get());

//            notifyObservers(o -> o.availableMovesUpdate(getCurrentPlayer(), validMoves, blockedMoves));
        }
    }

    public List<Power> getAvailablePowers() {
        return availableGodList;
    }

    public GameMap getMap() {
        return map;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public synchronized boolean hasEnded() {
        return hasEnded;
    }

    public String getGameID() {
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

        availableGodList.removeIf(power -> !power.getPlayableIn().contains(playerNumber));
    }

    public void saveState() {
        savedState = new GameState(map.copy(), turnState.copy(),
                playerList.stream().map(Player::copy).collect(Collectors.toUnmodifiableList()));
    }

    public void restoreSavedState() {
        if (savedState != null) {
            if (endTurnRoutine != null) {
                endTurnRoutine.cancel(false);
            }

            map = savedState.getPreviousMap();
            playerList.clear();

            for (Player player : savedState.getPreviousPlayersState()) {
                addPlayer(player);
            }

            setTurnState(savedState.getPreviousTurnState());

            notifyObservers(o -> o.gameUpdate(this, true));

            saveState();
        }
    }
}
