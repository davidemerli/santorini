package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.controller.turn.Build;
import it.polimi.ingsw.psp1.santorini.controller.turn.EndTurn;
import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Poseidon extends Mortal {

    private int counter;

    public Poseidon(Player player) {
        super(player);
    }

    @Override
    public void onBeginTurn(Game game) {
        super.onBeginTurn(game);
        counter = 0;
    }

    @Override
    public boolean shouldShowInteraction() {
        return player.getTurnState() instanceof Build && counter > 0;
    }

    @Override
    public void onToggleInteraction(Game game) {
        player.setTurnState(new EndTurn(player, game));
    }

    @Override
    public void onYourBuild(Worker worker, Point where, Game game) {
        boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
        game.getMap().buildBlock(where, shouldBuildDome);

        if (counter == 0) {
            //Try to get the unmoved worker, the optional will be empty if only one worker is present on the map
            Optional<Worker> otherWorker = player.getWorkers().stream()
                    .filter(w -> w != worker)
                    .findFirst();

            if (otherWorker.isPresent()) {
                int level = game.getMap().getLevel(otherWorker.get().getPosition());

                //Activate power if other worker is on ground level
                if (level == 0) {
                    //select and lock the other worker and return to build
                    player.setSelectedWorker(otherWorker.get());
                    player.lockWorker();

                    player.setTurnState(new Build(player, game));
                    counter++;
                    return;
                }
            }
        } else if (counter < 3) {
            //can build up to 3 times with the unmoved worker
            player.setTurnState(new Build(player, game));
            counter++;
            return;
        }

        player.setTurnState(new EndTurn(player, game));
    }
}
