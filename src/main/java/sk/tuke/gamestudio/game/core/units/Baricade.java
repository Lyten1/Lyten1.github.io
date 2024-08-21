package sk.tuke.gamestudio.game.core.units;

import sk.tuke.gamestudio.game.core.Tile;
import sk.tuke.gamestudio.game.core.TileState;

public class Baricade extends Tile {
    private final String sprite;


    public Baricade(int row, int column) {
        this.sprite = "#";
        state = TileState.BLOCKED;
        positions.put("y", row);
        positions.put("x", column);
    }

    @Override
    public void printTileVisualisation(String color, String reset){
        System.out.print(sprite);
    }

    @Override
    public String getClassName() {
        return null;
    }
}
