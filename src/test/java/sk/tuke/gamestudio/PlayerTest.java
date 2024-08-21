package sk.tuke.gamestudio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sk.tuke.gamestudio.game.core.Player;
import sk.tuke.gamestudio.repos.PlayerRepo;
import sk.tuke.gamestudio.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class PlayerTest {

    @Mock
    private PlayerRepo playerRepo;

    @InjectMocks
    private PlayerService playerService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenPlayerExists_thenPlayerShouldBeReturned() {
        String playerName = "JohnDoe";
        Player mockPlayer = new Player(); // Assuming Player is a class with appropriate fields
        mockPlayer.setLogin(playerName);

        when(playerRepo.getByLogin(playerName)).thenReturn(mockPlayer);

        Player result = playerService.getPlayerByName(playerName);

        verify(playerRepo).getByLogin(playerName);
        assertEquals(mockPlayer, result);
        assertEquals(playerName, result.getLogin());
    }

    @Test
    void checkInvalidData(){
        Player player = new Player("login", null, 12);
        assertEquals(player, new Player());
    }

    @Test
    void checkValidData(){
        Player player = new Player("Login", "Password",  12);
        Assertions.assertNotNull(player);
    }




}
