package it.polimi.ingsw.psp1.santorini.model;

import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.powers.Power;
import it.polimi.ingsw.psp1.santorini.model.map.Map;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private final List<Power> availableGodList;
    private final List<Player> playerList;
    private Map map;

    public Game() {
        this.availableGodList = new ArrayList<>();
        this.playerList = new ArrayList<>();
        this.map = new Map();
    }

    public void addPlayer(Player player) {
        this.playerList.add(player);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
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

    public List<Player> getPlayerList() {
        return Collections.unmodifiableList(playerList);
    }

    public List<Player> getPlayerOpponents(Player player) {
        return playerList.stream().filter(p -> p != player).collect(Collectors.toUnmodifiableList());
    }
}
