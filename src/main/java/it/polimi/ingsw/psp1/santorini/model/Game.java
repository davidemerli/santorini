package it.polimi.ingsw.psp1.santorini.model;

import it.polimi.ingsw.psp1.santorini.model.map.GameMap;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.model.turn.BeginTurn;
import it.polimi.ingsw.psp1.santorini.model.turn.TurnState;
import it.polimi.ingsw.psp1.santorini.observer.ModelObserver;
import it.polimi.ingsw.psp1.santorini.view.View;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Game implements Runnable {

    private final List<Power> availableGodList;
    private final List<Player> playerList;

    private final Set<ModelObserver> observers;

    private TurnState turnState;

    private GameMap map;

    public Game() {
        this.availableGodList = new ArrayList<>();
        this.playerList = new ArrayList<>();
        this.observers = new HashSet<>();
        this.map = new GameMap();
    }

    public void run() {
        //TODO: game turn auto changes @see GameTest.java
    }

    public void addObserver(View view) {
        observers.add(view);
    }

    public void removeObserver(View view) {
        observers.remove(view);
    }

    public void notifyObservers(Consumer<ModelObserver> lambda) {
        observers.forEach(lambda);
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public void removePlayer(Player player) {
        playerList.remove(player);
    }

    public void buildBlock(Point position, boolean forceDome) {
        map.buildBlock(position, forceDome);

        notifyObservers(o -> o.mapChange(this, map));
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

        if (!player.getWorkers().contains(worker)) {
            throw new NoSuchElementException("Given worker is not present on the map");
        }

        worker.setPosition(newPosition);
    }

    public Player getCurrentPlayer() {
        if (playerList.size() == 0) {
            throw new UnsupportedOperationException("Player list is empty");
        }

        return playerList.get(0);
    }

    public void startTurn() {
        if (playerList.size() == 0) {
            throw new UnsupportedOperationException("Player list is empty");
        }

        BeginTurn turn = new BeginTurn(this);
        setTurnState(turn);
        turn.onStart();

        notifyObservers(o -> o.playerUpdate(this, getCurrentPlayer()));
    }

    public void setWinner(Player player) {
        if(!playerList.contains(player)) {
            throw new NoSuchElementException("Given player is not in this game");
        }

        player.setWinner(true);
        notifyObservers(o -> o.playerUpdate(this, getCurrentPlayer()));
    }

    public void setLoser(Player player) {
        if(!playerList.contains(player)) {
            throw new NoSuchElementException("Given player is not in this game");
        }

        player.setLost(true);
        notifyObservers(o -> o.playerUpdate(this, getCurrentPlayer()));
    }

    public void nextTurn() {
        shiftPlayers(1);

        startTurn();
    }

    public void shiftPlayers(int distance) {
        Collections.rotate(playerList, distance);
    }

    public void shufflePlayers() {
        Collections.shuffle(playerList);
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
}
