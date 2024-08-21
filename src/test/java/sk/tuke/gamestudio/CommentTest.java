package sk.tuke.gamestudio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.AssertionErrors;
import sk.tuke.gamestudio.entities.Score;
import sk.tuke.gamestudio.game.core.db.JDBC.CommentServiceJDBC;
import sk.tuke.gamestudio.entities.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.services.CommentServiceJPA;
import sk.tuke.gamestudio.services.ScoreServiceJPA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentTest {

    private String game = "HMaM";


    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CommentServiceJPA commentServiceJPA;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testGet() {

        Query mockGetTopScores = mock(Query.class);
        Comment comment1 = new Comment(game, "Player", "Hello", new Date());
        Comment comment2 = new Comment(game, "Player2", "Dear", new Date());
        Comment comment3 = new Comment(game, "Player3", "Developer", new Date());

        List<Comment> expectLst = new ArrayList<>();
        expectLst.add(comment1);
        expectLst.add(comment2);
        expectLst.add(comment3);


        when(entityManager.createNamedQuery("Comment.getComments")).thenReturn(mockGetTopScores);
        when(mockGetTopScores.setParameter("game", game)).thenReturn(mockGetTopScores);
        when(mockGetTopScores.getResultList()).thenReturn(expectLst);

        List lst = commentServiceJPA.getComments(game);

        verify(entityManager).createNamedQuery("Comment.getComments");
        verify(mockGetTopScores).getResultList();
        AssertionErrors.assertEquals("Incorrect return", expectLst, lst);
    }

    @Test
    void testGetEmpty() {

        Query mockGetTopScores = mock(Query.class);

        List expectLst = Collections.emptyList();

        when(entityManager.createNamedQuery("Comment.getComments")).thenReturn(mockGetTopScores);
        when(mockGetTopScores.setParameter("game", game)).thenReturn(mockGetTopScores);
        when(mockGetTopScores.getResultList()).thenReturn(expectLst);

        List lst = commentServiceJPA.getComments(game);

        verify(entityManager).createNamedQuery("Comment.getComments");
        verify(mockGetTopScores).getResultList();
        AssertionErrors.assertEquals("Incorrect return", expectLst, lst);
    }

    @Test
    void testAddComment() {
        Query mockAddComment = mock(Query.class);
        Comment comment = new Comment(game, "Player", "Hi", new Date());


        when(entityManager.createNamedQuery(anyString())).thenReturn(mockAddComment);
        when(mockAddComment.setParameter(anyString(), any())).thenReturn(mockAddComment);
        when(mockAddComment.getResultList()).thenReturn(Collections.emptyList());

        commentServiceJPA.addComment(comment);
        verify(entityManager).persist(comment);
    }

    @Test
    void testRemoveComment() {
        Query mockReset = mock(Query.class);

        when(entityManager.createNamedQuery("Comment.resetComments")).thenReturn(mockReset);
        when(mockReset.setParameter(anyString(), any())).thenReturn(mockReset);
        when(mockReset.getResultList()).thenReturn(Collections.emptyList());

        commentServiceJPA.reset();
        verify(entityManager).createNamedQuery("Comment.resetComments");
        verify(mockReset).executeUpdate();
    }



}
