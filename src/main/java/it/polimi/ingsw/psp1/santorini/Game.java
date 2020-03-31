package it.polimi.ingsw.psp1.santorini;

import it.polimi.ingsw.psp1.santorini.map.Worker;
import it.polimi.ingsw.psp1.santorini.powers.Power;
import it.polimi.ingsw.psp1.santorini.map.Map;
import it.polimi.ingsw.psp1.santorini.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {

    public static void main(String[] args) {

    }

    private final List<Power> availableGodList;
    private final List<Player> playerList;
    private Map gameMap;

    public Game() {
        this.availableGodList = new ArrayList<>();
        this.playerList = new ArrayList<>();
        this.gameMap = new Map();
    }

    public void addPlayer(Player player) {
        this.playerList.add(player);
    }

    public Map getGameMap() {
        return gameMap;
    }

    public void setGameMap(Map map) {
        this.gameMap = map;
    }

    public List<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }

    public void moveWorker(Worker worker, Point position) {
        gameMap = gameMap.moveWorker(worker, position);

        //TODO: send event

    }

    public void buildBlock(Point position, boolean isDome) {
        gameMap = gameMap.buildBlock(position, isDome);

        //TODO: send event

    }
}
