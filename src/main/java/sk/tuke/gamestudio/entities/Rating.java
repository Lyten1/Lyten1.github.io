package sk.tuke.gamestudio.entities;


import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Entity
@NamedQuery( name = "Rating.getAverageRating",
        query = "SELECT AVG(r.rating) AS average_rating FROM Rating r WHERE r.game = :game")
@NamedQuery( name = "Rating.getRating",
        query = "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player")
@NamedQuery( name = "Rating.updateRating",
        query = "UPDATE Rating r SET r.rating = :rating WHERE r.game = :game AND r.player = :player")
@NamedQuery( name = "Rating.resetRating",
        query = "DELETE FROM Rating ")
public class Rating {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "game")
    private String game;
    @Column(name = "player", unique = true)
    private String player;
    @Column(name = "rating")
    private int rating;
    @Column(name = "ratedOn")
    private Date ratedOn;

    public Rating(String game, String player, int rating, Date ratedOn) {
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    public Rating() {

    }

    public int getId() {
        return id;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }

    @Override
    public String toString() {
        return String.format("%-15s\t|\t%d\t|\t%s", player, rating, ratedOn);
    }
}

