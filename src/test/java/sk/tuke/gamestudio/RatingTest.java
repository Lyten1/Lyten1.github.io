package sk.tuke.gamestudio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.After;
import org.junit.Assert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sk.tuke.gamestudio.entities.Rating;
import sk.tuke.gamestudio.services.RatingServiceJPA;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RatingTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private String game = "HMaM";
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private RatingServiceJPA ratingServiceJPA;


    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }


    @Test
    public void testAddNewRating() {
        Rating newRating = new Rating(game, "UnNamed", 5, new Date());
        Query mockQuery = mock(Query.class);

        when(entityManager.createNamedQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(Collections.emptyList()); // Simulate no existing rating

        ratingServiceJPA.setRating(newRating);

        verify(entityManager).persist(newRating);
    }

    @Test
    public void testAddIncorrectRating() {
        Rating newRating = new Rating(game, "UnNamed", 8, new Date());
        Query mockQuery = mock(Query.class);

        when(entityManager.createNamedQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(Collections.emptyList()); // Simulate no existing rating

        ratingServiceJPA.setRating(newRating);

        assertEquals("Not a incorrect data","Incorrect data\n", outContent.toString());
    }

    @Test
    public void testUpdateExistingRating() {
        // Arrange
        Rating existingRating = new Rating(game, "Legolas", 5, new Date());
        Query mockGetRatingQuery = mock(Query.class);
        Query mockUpdateRatingQuery = mock(Query.class);

        when(entityManager.createNamedQuery("Rating.getRating")).thenReturn(mockGetRatingQuery);
        when(mockGetRatingQuery.setParameter("game", existingRating.getGame())).thenReturn(mockGetRatingQuery);
        when(mockGetRatingQuery.setParameter("player", existingRating.getPlayer())).thenReturn(mockGetRatingQuery);
        // Simulate getSingleResult() finding an existing rating
        when(mockGetRatingQuery.getSingleResult()).thenReturn(existingRating);

        when(entityManager.createNamedQuery("Rating.updateRating")).thenReturn(mockUpdateRatingQuery);
        when(mockUpdateRatingQuery.setParameter(anyString(), any())).thenReturn(mockUpdateRatingQuery);
        // No need to mock the result of executeUpdate() as it returns void for the purpose of this test, but you can verify it was called

        // Act
        ratingServiceJPA.setRating(existingRating);

        // Assert
        verify(mockUpdateRatingQuery).executeUpdate(); // Verify that the update operation was indeed called
        // Optionally, verify that setParameter was called with correct values
        verify(mockUpdateRatingQuery).setParameter("rating", existingRating.getRating());
        verify(mockUpdateRatingQuery).setParameter("game", existingRating.getGame());
        verify(mockUpdateRatingQuery).setParameter("player", existingRating.getPlayer());
        // Note: No need to assert System.out.println output
    }


    @Test
    public void testGetAverageRating(){
        double expectedRate = 4.0;
        Query mockGetAverageRating = mock(Query.class);


        when(entityManager.createNamedQuery("Rating.getAverageRating")).thenReturn(mockGetAverageRating);
        when(mockGetAverageRating.setParameter("game", game)).thenReturn(mockGetAverageRating);
        when(mockGetAverageRating.getSingleResult()).thenReturn(expectedRate);

        double averRate = ratingServiceJPA.getAverageRating(game);

        verify(entityManager).createNamedQuery("Rating.getAverageRating");
        verify(mockGetAverageRating).getSingleResult();
        assertEquals("Incorrect return", expectedRate, averRate);
    }


    @Test
    public void testGetRating(){
        Rating rate = new Rating(game, "player", 5, new Date());
        int expectedRate = rate.getRating();
        Query mockGetRating = mock(Query.class);

        when(entityManager.createNamedQuery("Rating.getRating")).thenReturn(mockGetRating);
        when(mockGetRating.setParameter("game", game)).thenReturn(mockGetRating);
        when(mockGetRating.setParameter("player", rate.getPlayer())).thenReturn(mockGetRating);
        when(mockGetRating.getSingleResult()).thenReturn(rate);

        int averRate = ratingServiceJPA.getRating(game, "player");

        verify(entityManager).createNamedQuery("Rating.getRating");
        verify(mockGetRating).getSingleResult();
        assertEquals("Incorrect return", expectedRate, averRate);
    }

    @Test
    void testRemoveRating() {
        Query mockReset = mock(Query.class);

        when(entityManager.createNamedQuery("Rating.resetRating")).thenReturn(mockReset);
        when(mockReset.setParameter(anyString(), any())).thenReturn(mockReset);
        when(mockReset.getResultList()).thenReturn(Collections.emptyList());

        ratingServiceJPA.reset();
        verify(entityManager).createNamedQuery("Rating.resetRating");
        verify(mockReset).executeUpdate();
    }

}
