package sk.tuke.gamestudio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sk.tuke.gamestudio.game.core.db.JDBC.ScoreServiceJDBC;
import sk.tuke.gamestudio.entities.Score;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.services.RatingServiceJPA;
import sk.tuke.gamestudio.services.ScoreServiceJPA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ScoreTest {
   private String game = "HMaM";
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ScoreServiceJPA scoreServiceJPA;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testGetTop() {

        Query mockGetTopScores = mock(Query.class);
        Score score1 = new Score(game, "Player", 34, new Date());
        Score score2 = new Score(game, "Player2", 433, new Date());
        Score score3 = new Score(game, "Player3", 213, new Date());

        List<Score> expectLst = new ArrayList<>();
        expectLst.add(score1);
        expectLst.add(score2);
        expectLst.add(score3);


        when(entityManager.createNamedQuery("Score.getTopScores")).thenReturn(mockGetTopScores);
        when(mockGetTopScores.setParameter("game", game)).thenReturn(mockGetTopScores);
        when(mockGetTopScores.setMaxResults(anyInt())).thenReturn(mockGetTopScores);
        when(mockGetTopScores.getResultList()).thenReturn(expectLst);

        List lst = scoreServiceJPA.getTopScores(game);

        verify(entityManager).createNamedQuery("Score.getTopScores");
        verify(mockGetTopScores).getResultList();
        assertEquals("Incorrect return", expectLst, lst);
    }

    @Test
    void testGetEmptyTop() {

        Query mockGetTopScores = mock(Query.class);

        List expectLst = Collections.emptyList();

        when(entityManager.createNamedQuery("Score.getTopScores")).thenReturn(mockGetTopScores);
        when(mockGetTopScores.setParameter("game", game)).thenReturn(mockGetTopScores);
        when(mockGetTopScores.setMaxResults(anyInt())).thenReturn(mockGetTopScores);
        when(mockGetTopScores.getResultList()).thenReturn(expectLst);

        List lst = scoreServiceJPA.getTopScores(game);

        verify(entityManager).createNamedQuery("Score.getTopScores");
        verify(mockGetTopScores).getResultList();
        assertEquals("Incorrect return", expectLst, lst);
    }

    @Test
    void testAddScore() {
        Query mockGetTopScores = mock(Query.class);
        Score score1 = new Score(game, "Player", 34, new Date());


        when(entityManager.createNamedQuery(anyString())).thenReturn(mockGetTopScores);
        when(mockGetTopScores.setParameter(anyString(), any())).thenReturn(mockGetTopScores);
        when(mockGetTopScores.getResultList()).thenReturn(Collections.emptyList());

        scoreServiceJPA.addScore(score1);
        verify(entityManager).persist(score1);
    }

    @Test
    void testRemoveRating() {
        Query mockReset = mock(Query.class);

        when(entityManager.createNamedQuery("Score.resetScores")).thenReturn(mockReset);
        when(mockReset.setParameter(anyString(), any())).thenReturn(mockReset);
        when(mockReset.getResultList()).thenReturn(Collections.emptyList());

        scoreServiceJPA.reset();
        verify(entityManager).createNamedQuery("Score.resetScores");
        verify(mockReset).executeUpdate();
    }



}
