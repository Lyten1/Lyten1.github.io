package sk.tuke.gamestudio;

import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.core.GameStatus;
import sk.tuke.gamestudio.services.UnitPropsService;
import sk.tuke.gamestudio.game.core.units.Baricade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class FieldTest {

    @Autowired
    Field field;

    @Autowired
    UnitPropsService unitPropsService;


    @Test
    void sizeFieldTest(){
        int expectedRowSize = 8;
        int expectedColumnSize = 16;

        int actualRowSize = field.getRowCount();
        int actualColumnSize = field.getColumnCount();

        assertEquals(expectedRowSize, actualRowSize, "Size of row the field should be " + expectedRowSize);
        assertEquals(expectedColumnSize, actualColumnSize, "Size of column the field should be " + expectedColumnSize);
    }

    @Test
    void checkTie(){
        field.getBoardEntities().removeAll(field.getBoardEntities());
        Assertions.assertEquals(GameStatus.TIE, field.checkGameStatus());
    }

    @Test
    void checkWinLight(){
        field.getBoardEntities().removeAll(field.getBoardEntities());
        field.loadLightTeam(unitPropsService);
        assertEquals(GameStatus.LIGHT_WIN, field.checkGameStatus());
    }

    @Test
    void checkWinDark(){
        field.getBoardEntities().removeAll(field.getBoardEntities());
        field.loadDarkTeam(unitPropsService);
        assertEquals(GameStatus.DARK_WIN, field.checkGameStatus());
    }

    @Test
    void checkPlay(){
        field.getBoardEntities().removeAll(field.getBoardEntities());
        field.loadDefaultGame(unitPropsService);
        assertEquals(GameStatus.PLAYING, field.checkGameStatus());
    }

    @Test
    void checkMapRelief(){
        field.getBoardEntities().removeAll(field.getBoardEntities());
        field.loadMap();
        boolean relief = field.getBoardEntities().stream().allMatch((obj) -> obj instanceof Baricade);
        assertEquals(true, relief);
    }
}
