package it.polimi.ingsw.psp1.santorini.model.powers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Power implements Serializable, Cloneable {

    protected transient Player player;

    private String name;
    private String alias;
    private String description;

    private String[] interaction;
    private String interactButton;

    private boolean simple;

    private int[] playableIn;

    public Power() {
        URL file = getClass().getResource("/powers/" + getClass().getSimpleName() + ".json");
        File powersFolder = new File(file.getPath());

        Gson gson = new Gson();

        try {
            JsonObject jObject = JsonParser.parseReader(new FileReader(powersFolder)).getAsJsonObject();
            this.name = jObject.get("name").getAsString();
            this.alias = jObject.get("alias").getAsString();
            this.description = jObject.get("description").getAsString();
            this.interaction = gson.fromJson(jObject.get("interaction").getAsJsonArray(), String[].class);
            this.interactButton = jObject.get("interactButton").getAsString();
            this.simple = jObject.get("isSimple").getAsBoolean();
            this.playableIn = gson.fromJson(jObject.get("playableIn").getAsJsonArray(), int[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called on the beginning of a player turn (both own and enemy)
     *
     * @param player current player
     * @param game   current game
     */
    public abstract void onBeginTurn(Player player, Game game);

    /**
     * Called when you want to set the final state
     *
     * @param game current game
     */
    public abstract void onEndTurn(Player player, Game game);

    /**
     * Called after input from the view that asks for a build on given position
     *
     * @param player current player
     * @param worker current selected worker
     * @param where  point where you want to build
     * @param game   current game
     */
    public abstract void onBuild(Player player, Worker worker, Point where, Game game);

    /**
     * Called after input from the view that asks for a move on given position
     *
     * @param player current player
     * @param worker current selected worker
     * @param where  point where you want to move
     * @param game   current game
     */
    public abstract void onMove(Player player, Worker worker, Point where, Game game);

    /**
     * Called when the player interacts with custom bottom on the UI
     *
     * @param game current game
     */
    public abstract void onToggleInteraction(Game game);

    /**
     * Called when the player decides to show the bottom for interaction or not
     *
     * @param game current game
     * @return true if the GUI needs to enable the bottom for interaction
     */
    public abstract boolean shouldShowInteraction(Game game);

    /**
     * Gets the list of unavailable moves for the enemy worker
     *
     * @param player current player playing
     * @param worker worker to get blocked moves of
     * @param game   current game
     * @return list of blocked moves
     */
    public abstract List<Point> getBlockedMoves(Player player, Worker worker, Game game);

    /**
     * Called when you want to list all the valid moves that a worker can make
     *
     * @param game   current game
     * @param worker to get valid moves of
     * @return list of valid moves
     */
    public abstract List<Point> getValidMoves(Worker worker, Game game);

    /**
     * Called when the player decides to show the bottom for interaction or not
     *
     * @param game current game
     * @return true if the GUI needs to enable the bottom for interaction
     */
    public abstract boolean canCompleteValidTurn(Worker worker, Game game);

    /**
     * Returns to the previous turn state, should be customized for powers that can possibly do lots of moves
     */
    public abstract void undo();

    /**
     * Initializes the player
     *
     * @param player that has been given this power
     */
    public void setPlayer(Player player) {
        this.player = player;
    }


    /**
     * Checks if another object is equal to this
     * Only checks if the class is the same, and if the player is the same
     * <p>
     * When choosing powers player is not set, so if both object player is null, the check is valid
     *
     * @param o other object to control
     * @return true if the given object is equal to this
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Power power = (Power) o;
        return Objects.equals(player, power.player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

    /**
     * Needed when passing powers through sockets
     *
     * @return a clone of the current object
     */
    public Power copy() {
        try {
            return (Power) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getInteraction() {
        return Arrays.stream(interaction).collect(Collectors.toUnmodifiableList());
    }

    public String getInteractButton() {
        return interactButton;
    }

    public boolean isSimple() {
        return simple;
    }

    public List<Integer> getPlayableIn() {
        return Arrays.stream(playableIn).boxed().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return "Power: [" + getName() + "]";
    }
}
