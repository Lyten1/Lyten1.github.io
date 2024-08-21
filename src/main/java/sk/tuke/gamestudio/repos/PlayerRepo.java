package sk.tuke.gamestudio.repos;

import sk.tuke.gamestudio.game.core.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepo extends JpaRepository<Player, Integer> {
    @Query("select p from Player p where p.login = :login")
    Player getByLogin(@Param("login") String login);
}
