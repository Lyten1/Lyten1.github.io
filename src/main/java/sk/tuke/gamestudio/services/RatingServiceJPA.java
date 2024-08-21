package sk.tuke.gamestudio.services;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entities.Rating;


@Transactional
public class RatingServiceJPA implements RatingService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        if(rating.getRating() < 1 || rating.getRating() > 5){
            System.out.println("Incorrect data");
        }
        else {
            Rating exRating = null;
            try {
                exRating = ((Rating) entityManager.createNamedQuery("Rating.getRating")
                        .setParameter("game", rating.getGame())
                        .setParameter("player", rating.getPlayer())
                        .getSingleResult());
            } catch (NoResultException e) {

            }
            if (exRating == null) {
                entityManager.persist(rating);
                //System.out.println("Add new rating");
            } else {
                entityManager.createNamedQuery("Rating.updateRating")
                        .setParameter("rating", rating.getRating())
                        .setParameter("game", rating.getGame())
                        .setParameter("player", rating.getPlayer())
                        .executeUpdate();
                //System.out.println("Update rating");
            }
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        var res = entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getSingleResult();
        if(res == null)
            return 0;
        return (int) Math.round((Double) entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getSingleResult());
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            Rating rating = (Rating) entityManager.createNamedQuery("Rating.getRating")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
            return rating.getRating();
        }
        catch (NoResultException e){
            System.out.println("No entity found");
        }
        return 0;
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRating").executeUpdate();
    }
}
