package sk.tuke.gamestudio.repos;

import org.springframework.stereotype.Repository;
import sk.tuke.gamestudio.game.core.units.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface UnitPropsRepo extends JpaRepository<Unit, Integer> {

    Unit getUnitByName(String name);

    @Query("select u from Unit u order by u.cost")
    List<Unit> getAll();

    @Query("select u.cost from Unit u where u.id = :id")
    int getUnitCostById(int id);
}
