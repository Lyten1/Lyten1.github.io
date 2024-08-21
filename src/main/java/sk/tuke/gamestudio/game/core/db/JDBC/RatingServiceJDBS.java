package sk.tuke.gamestudio.game.core.db.JDBC;

import sk.tuke.gamestudio.services.PlayerService;
import sk.tuke.gamestudio.entities.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.services.CommentException;
import sk.tuke.gamestudio.services.RatingException;
import sk.tuke.gamestudio.services.RatingService;
import sk.tuke.gamestudio.services.ScoreException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//@Component
public class RatingServiceJDBS implements RatingService {

    public static final String URL = "jdbc:postgresql://localhost/HMaM";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String SELECT = "SELECT game, player, rating, rated_on FROM rating WHERE game = ?";
    public static final String SELECT_PLAYER_RATE = "SELECT game, player, rating, rated_on FROM rating WHERE game = ? AND player = ?";
    public static final String SELECT_ALL_PLAYER_RATE = "SELECT game, player, rating, rated_on FROM rating WHERE game = ?";
    public static final String DELETE = "DELETE FROM rating";
    public static final String INSERT = "INSERT INTO rating (game, player, rating, rated_on) VALUES (?, ?, ?, ?)";

    public static final String UPDATE = "UPDATE rating SET rating = ?, rated_on = ? WHERE game = ? AND player = ?";

    @Autowired
    PlayerService playerService;

    @Override
    public void setRating(Rating rating) throws RatingException {
        int exist = getRating(rating.getGame(), rating.getPlayer());
        String state;
        if(exist != 0)
            state = UPDATE;
        else
            state = INSERT;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(state)
        ) {
            if(rating.getRating() < 1 || rating.getRating() > 5)
                return;
            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getPlayer());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ScoreException("Problem inserting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Rating> ratings = new ArrayList<>();
                while (rs.next()) {
                    ratings.add(new Rating(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                return (int)ratings.stream().mapToDouble(Rating::getRating).average().getAsDouble();
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting average rate", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER_RATE)
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()) {
                List<Rating> ratings = new ArrayList<>();
                while (rs.next()) {
                    ratings.add(new Rating(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                if(ratings.isEmpty())
                    return 0;
                return ratings.get(0).getRating();
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting player rate", e);
        }
    }

    public List<Rating> getAllRatings(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PLAYER_RATE)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Rating> ratings = new ArrayList<>();
                while (rs.next()) {
                    ratings.add(new Rating(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                return ratings;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting player rate", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new CommentException("Problem deleting rating", e);
        }
    }
}
