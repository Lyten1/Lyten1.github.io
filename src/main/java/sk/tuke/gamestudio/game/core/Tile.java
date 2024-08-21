package sk.tuke.gamestudio.game.core;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    protected TileState state ;
    protected Map<String , Integer> positions = new HashMap<>();





    public void setX(int x){
        positions.put("x", x);
    }

    public void setY(int y){
        positions.put("y", y);
    }

    public int getX(){
        return positions.get("x");
    }

    public int getY(){
        return positions.get("y");
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public TileState getState(){
        return state;
    }

    public void printTileVisualisation(String color, String reset) {
    }

    public boolean checkEntityPosition(int y, int x){
        return this.positions.get("y") == y && this.positions.get("x") == x;
    }

    public abstract String getClassName();

}
