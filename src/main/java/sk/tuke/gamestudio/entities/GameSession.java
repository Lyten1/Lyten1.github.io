package sk.tuke.gamestudio.entities;

import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.core.Player;
import sk.tuke.gamestudio.game.core.Tile;
import sk.tuke.gamestudio.game.core.units.Unit;
import sk.tuke.gamestudio.repos.PlayerRepo;

import java.util.LinkedList;
import java.util.Queue;

public class GameSession {
    private Field field;
    private Queue<Tile> queue = new LinkedList<>();

    private Player player_light;
    private Player player_dark;


    public GameSession() {
        this.field = new Field();
    }

    public void setPlayer_light(Player player_light) {
        this.player_light = player_light;
    }

    public Player getPlayer_light() {
        return player_light;
    }

    public void setPlayer_dark(Player player_dark) {
        this.player_dark = player_dark;
    }

    public Player getPlayer_dark() {
        return player_dark;
    }

    public Field getField() {
        return field;
    }

    public Queue<Tile> getQueue() {
        return queue;
    }

    // Additional methods to manipulate game state can be added here
}
