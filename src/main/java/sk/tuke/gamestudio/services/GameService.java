package sk.tuke.gamestudio.services;

import org.springframework.stereotype.Service;
import sk.tuke.gamestudio.entities.GameSession;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final ConcurrentHashMap<String, GameSession> gameSessions = new ConcurrentHashMap<>();

    /**
     * Creates a new game session and returns the unique token associated with it.
     * @return the unique token identifying the new game session.
     */
    public String createGameSession() {
        String token = generateUniqueToken();
        GameSession session = new GameSession();
        gameSessions.put(token, session);
        return token;
    }

    /**
     * Retrieves a game session by its token.
     * @param token the unique token identifying the game session.
     * @return the game session associated with the token.
     */
    public GameSession getSession(String token) {
        return gameSessions.get(token);
    }

    /**
     * Generates a unique token for a game session.
     * @return a unique token as a String.
     */
    private String generateUniqueToken() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
}
