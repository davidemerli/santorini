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

/**
 * Defines all functions that can modify the status of the game
 */
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

    private boolean changedFromLastSave;

    /**
     * Generic constructor
     * Creates a new map, a new player list and the list of all available gods
     *
     * @param gameID       unique ID game
     * @param playerNumber number of the player
     */
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

    /**
     * Starts the game and shuffle the order of the players
     * Notifies all clients
     */
    public void startGame() {
        hasStarted = true;

        shufflePlayers();

        pool.schedule(() -> {
            setTurnState(new SelectPowers());

            notifyObservers(o -> o.gameUpdate(this, false));
        }, 2, TimeUnit.SECONDS);
    }

    /**
     * Used to get a Optional containing a player who has won, if present
     *
     * @return optional with the winner if present
     * @throws IllegalStateException if all players have lost
     */
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

    /**
     * Sets winner
     * Notifies all clients
     *
     * @param player the players who has won
     * @throws NoSuchElementException if player does not exist
     */
    public void setWinner(Player player) {
        if (!playerList.contains(player)) {
            throw new NoSuchElementException("Given player is not in this game");
        }

        player.setWinner(true);
        notifyObservers(o -> o.playerUpdate(this, player));
    }

    /**
     * Removes a player from the game if has lost
     * Notifies all clients
     */
    private void removeLoser() {
        getCurrentPlayer().removeAllWorkers();
        notifyObservers(o -> o.playerUpdate(this, getCurrentPlayer()));

        playerList.remove(getCurrentPlayer());
        nextTurn();
    }

    /**
     * Adds player in the game
     *
     * @param player the player to be added
     * @throws IllegalArgumentException if player is already in game
     */
    public void addPlayer(Player player) {
        if (playerList.contains(player)) {
            throw new IllegalArgumentException("Player is already in game");
        }

        playerList.add(player);
    }

    /**
     * Adds worker on the map
     * Notifies alla clients
     *
     * @param player   the owner of the worker to be added
     * @param position the new worker position on the map
     */
    public void addWorker(Player player, Point position) {
        Worker worker = new Worker(position);
        player.addWorker(worker);
        notifyObservers(o -> o.playerUpdate(this, player));
        notifyObservers(o -> o.playerPlaceWorker(player, worker));

        changedFromLastSave = true;
    }

    /**
     * Builds a block in the map
     * Notifies all clients
     *
     * @param position  where the block must be built
     * @param forceDome true if must be built a dome in a level different from the last one
     */
    public void buildBlock(Point position, boolean forceDome) {
        map.buildBlock(position, forceDome);

        notifyObservers(o -> o.gameUpdate(this, false));

        changedFromLastSave = true;
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

    /**
     * Used to get a Optional containing a plauer giving the worker
     *
     * @param worker used to obtaining his player
     * @return optional with player if present
     */
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

        changedFromLastSave = true;
    }

    /**
     * At the beginning of the turn checks if there is a winner or a player who has lost to be removed
     * Starts player turn
     * Notifies all clients
     *
     * @throws UnsupportedOperationException if player list is empty
     */
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

    /**
     * Goes to next turn, so the player list is shifted
     */
    public void nextTurn() {
        shiftPlayers(-1);

        startTurn();
    }

    /**
     * Checks if player has finished his turn
     * Waits five seconds before change turn in case of undo
     */
    public void endTurn() {
        getPlayerList().forEach(p -> p.getPower().onEndTurn(getCurrentPlayer(), this));

        if (endTurnRoutine == null || endTurnRoutine.isDone()) {
            endTurnRoutine = pool.schedule(() -> {
                getPlayerList().forEach(p -> p.setSelectedWorker(null));
                getPlayerList().forEach(Player::unlockWorker);

                nextTurn();
            }, 5, TimeUnit.SECONDS);
        }
    }

    /**
     * Shifts player list in order to bring the current player in the first position
     * Notifies all clients
     *
     * @param distance number of shifting
     */
    public void shiftPlayers(int distance) {
        Collections.rotate(playerList, distance);

        notifyObservers(o -> o.gameUpdate(this, false));
    }

    /**
     * Shuffles player list
     */
    public void shufflePlayers() {
        Collections.shuffle(playerList);
    }

    /**
     * Sends request to a player
     *
     * @param player      the players who has to receive the request
     * @param requestType type of the sending request
     */
    public void askRequest(Player player, EnumRequestType requestType) {
        notifyObservers(o -> o.requestToPlayer(player, requestType));
    }

    /**
     * Ends the game and remove all clients
     */
    public synchronized void endGame() {
        hasEnded = true;
        removeAllObservers();
    }

    /**
     * Forces the end of the game and remove all clients
     */
    public void forceEndGame() {
        playerList.forEach(p -> askRequest(p, EnumRequestType.DISCONNECT));
        endGame();
    }

    /**
     * Sets player has loser
     * Notifies all clients
     *
     * @param player the player who has lost
     * @throws NoSuchElementException if player is not in the game
     */
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

    /**
     * Used to get all opponents players giving a generic player
     *
     * @param player genenric player
     * @return list with all opponents players
     */
    public List<Player> getPlayerOpponents(Player player) {
        return playerList.stream().filter(p -> !p.equals(player)).collect(Collectors.toUnmodifiableList());
    }

    public TurnState getTurnState() {
        return turnState;
    }

    /**
     * Checks if a player has won ora if a player has lost
     * Sets the new player turn
     * Notifies all clients
     *
     * @param newTurn new player turn
     */
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

    /**
     * Used to know if the game has started
     * @return true if the game has started
     */
    public boolean hasStarted() {
        return hasStarted;
    }

    /**
     * Used to know if the game has ended
     * @return true if the game has ended
     */
    public synchronized boolean hasEnded() {
        return hasEnded;
    }

    public String getGameID() {
        return gameID;
    }

    /**
     * Adds all available gods
     */
    public void addPowers() {
        availableGodList.add(new Mortal());
        availableGodList.add(new Apollo());
        availableGodList.add(new Artemis());
        availableGodList.add(new Athena());
        availableGodList.add(new Atlas());
        availableGodList.add(new Chronus());
        availableGodList.add(new Demeter());
        availableGodList.add(new Hephaestus());
        availableGodList.add(new Hestia());
        availableGodList.add(new Minotaur());
        availableGodList.add(new Pan());
        availableGodList.add(new Poseidon());
        availableGodList.add(new Prometheus());
        availableGodList.add(new Triton());
        availableGodList.add(new Zeus());

        availableGodList.removeIf(power -> !power.getPlayableIn().contains(playerNumber));
    }

    /**
     * Saves a generic state in order to use it in case of undo
     */
    public void saveState() {
        savedState = new GameState(map.copy(), turnState.copy(),
                playerList.stream().map(Player::copy).collect(Collectors.toUnmodifiableList()));
        changedFromLastSave = false;
    }

    /**
     * Restores the saved state
     */
    public void restoreSavedState() {
        if (savedState != null && changedFromLastSave) {
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
