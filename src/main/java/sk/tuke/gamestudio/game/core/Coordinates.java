package sk.tuke.gamestudio.game.core;

public class Coordinates {

    private int x;
    private int y;
    private boolean inStepsRange;
    private boolean inFightRange;

    public Coordinates(int y, int x, boolean inStepsRange, boolean inFightRange) {
        this.y = y;
        this.x = x;
        this.inFightRange = inFightRange;
        this.inStepsRange = inStepsRange;
    }

    public boolean isInStepsRange() {
        return inStepsRange;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}
