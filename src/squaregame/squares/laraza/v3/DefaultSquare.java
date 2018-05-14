package squaregame.squares.laraza.v3;

import squaregame.model.Direction;
import squaregame.model.SquareAction;
import squaregame.model.SquareView;
import squaregame.squares.SquareLogic;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * build 1x2 replicating machines
 */
public class DefaultSquare extends SquareLogic {

    @Override
    public SquareAction run(SquareView squareView) {
        // Kill if possible
        final List<Direction> attackDirections = squareView.getEnemyDirections();
        if (!attackDirections.isEmpty()) {
            Direction direction = attackDirections.get(ThreadLocalRandom.current().nextInt(attackDirections.size()));
            return SquareAction.attack(direction, this);
        }

        int row = squareView.getRow();
        int col = squareView.getCol();

        if (Math.floorMod(row, 3) == 0 && Math.floorMod(col, 2) == 0) {
            return SquareAction.replicate(Direction.S, this, new Avian(Direction.SE, 1));
        }

        final List<Direction> emptyDirections = squareView.getEmptyDirections();
        final Optional<Direction> replicateDirection = emptyDirections.stream().filter(direction -> replicateHere(direction, row, col)).findFirst();
        return replicateDirection.map(direction -> SquareAction.move(direction, new Replicator(1))).orElseGet(() ->
                SquareAction.replicate(Direction.W, new Avian(Direction.W, 1), new Avian(Direction.E, 1)));

    }

    @Override
    public String getSquareName() {
        return "La Raza v3";
    }

    public static boolean replicateHere(Direction direction, int row, int col) {
        return Math.floorMod(row + direction.getyOffset(), 3) == 0 && Math.floorMod(col + direction.getxOffset(), 2) == 0;
    }
}
