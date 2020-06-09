package it.polimi.ingsw.psp1.santorini.model.powers;

import it.polimi.ingsw.psp1.santorini.model.Game;
import it.polimi.ingsw.psp1.santorini.model.Player;
import it.polimi.ingsw.psp1.santorini.model.map.Point;
import it.polimi.ingsw.psp1.santorini.model.map.Worker;
import it.polimi.ingsw.psp1.santorini.model.turn.Build;

import java.util.Optional;

public class Poseidon extends Mortal {

    private int counter;

    @Override
    public void onBeginTurn(Player player, Game game) {
        super.onBeginTurn(player, game);

        if (player.equals(this.player)) {
            counter = 0;
        }
    }

    @Override
    public boolean shouldShowInteraction(Game game) {
        return game.getTurnState() instanceof Build && counter > 0;
    }

    @Override
    public void onToggleInteraction(Game game) {
        game.endTurn();
    }

    @Override
    public void onBuild(Player player, Worker worker, Point where, Game game) {
        if (player.equals(this.player)) {
            boolean shouldBuildDome = game.getMap().getLevel(where) == 3;
            game.buildBlock(where, shouldBuildDome);

            if (counter == 0) {
                //Try to get the unmoved worker, the optional will be empty if only one worker is present on the map
                Optional<Worker> otherWorker = player.getWorkers().stream()
                        .filter(w -> !w.equals(worker))
                        .findFirst();

                if (otherWorker.isPresent()) {
                    int level = game.getMap().getLevel(otherWorker.get().getPosition());

                    //Activate power if other worker is on ground level
                    if (level == 0) {
                        //select and lock the other worker and return to build
                        player.setSelectedWorker(otherWorker.get());
                        player.lockWorker();

                        counter++;
                        game.setTurnState(new Build());
                        return;
                    }
                }
            } else if (counter < 3) {
                //can build up to 3 times with the unmoved worker
                game.setTurnState(new Build());
                counter++;
                return;
            }

            game.endTurn();
        } else {
            super.onBuild(player, worker, where, game);
        }
    }
}
