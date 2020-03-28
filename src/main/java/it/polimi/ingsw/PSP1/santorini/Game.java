package it.polimi.ingsw.PSP1.santorini;

import it.polimi.ingsw.PSP1.santorini.godpowers.God;
import it.polimi.ingsw.PSP1.santorini.map.Map;
import it.polimi.ingsw.PSP1.santorini.player.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {

    public static void main(String[] args) {

    }

    private final List<God> availableGodList;
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

    public List<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }

}
