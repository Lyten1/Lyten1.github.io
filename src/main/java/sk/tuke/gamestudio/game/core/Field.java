package sk.tuke.gamestudio.game.core;

import org.springframework.stereotype.Component;
import sk.tuke.gamestudio.game.core.units.Unit;
import sk.tuke.gamestudio.services.UnitPropsService;
import sk.tuke.gamestudio.game.core.units.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class Field {

    private int rowCount;
    private int columnCount;
    private final List<Tile> boardEntities = new ArrayList<>();

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public List<Tile> getBoardEntities() {

        List<Unit> dead = boardEntities.stream().filter((obj)-> obj instanceof Unit)
                .map(obj -> (Unit)obj)
                .filter((unit)->unit.getAmount() <=0).toList();
        if(!dead.isEmpty())
            boardEntities.removeAll(dead);
        return boardEntities;
    }

    public List<Coordinates> getOpportunityPlaces() {
        return opportunityPlaces;
    }

    private final List<Coordinates> opportunityPlaces = new ArrayList<>();

    private final UnitBuilder builder = new UnitBuilder();


    public Field() {
        this.rowCount = 8;
        this.columnCount = 16;

    }

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;

    }

    public void loadDefaultGame(UnitPropsService unitPropsService){
        loadDefaultUnits(unitPropsService);
        loadMap();
    }


    public void loadMap(){
        boardEntities.add(new Baricade(0,2));
    }


    public void loadDefaultUnits(UnitPropsService unitPropsService){
        loadLightTeam(unitPropsService);
        loadDarkTeam(unitPropsService);
    }

    public void loadDarkTeam(UnitPropsService unitPropsService){
        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.PIKEMAN)
                .setAmountAndTotalHp(50)
                .setPositions(0,14)
                .setTeam(UnitTeam.DARK).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.ARCHER)
                .setAmountAndTotalHp(37)
                .setPositions(7,15)
                .setTeam(UnitTeam.DARK).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.MONK)
                .setAmountAndTotalHp(12)
                .setPositions(4,14)
                .setTeam(UnitTeam.DARK).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.SWORDSMAN)
                .setAmountAndTotalHp(12)
                .setPositions(6,15)
                .setTeam(UnitTeam.DARK).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.GRIFFIN)
                .setAmountAndTotalHp(12)
                .setPositions(2,15)
                .setTeam(UnitTeam.DARK).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.ANGEL)
                .setAmountAndTotalHp(3)
                .setPositions(3,14)
                .setTeam(UnitTeam.DARK).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.CAVALIER)
                .setAmountAndTotalHp(12)
                .setPositions(0,15)
                .setTeam(UnitTeam.DARK).build());
        builder.reset();
    }

    public void loadLightTeam(UnitPropsService unitPropsService){
        boardEntities.add(
                unitPropsService.fillDataToUnitByUClass(UnitClass.PIKEMAN)
                        .setAmountAndTotalHp(34)
                        .setPositions(0,0)
                        .setTeam(UnitTeam.LIGHT).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.ARCHER)
                .setAmountAndTotalHp(12)
                .setPositions(6,0)
                .setTeam(UnitTeam.LIGHT).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.MONK)
                .setAmountAndTotalHp(21)
                .setPositions(1,0)
                .setTeam(UnitTeam.LIGHT).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.SWORDSMAN)
                .setAmountAndTotalHp(16)
                .setPositions(0,1)
                .setTeam(UnitTeam.LIGHT).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.GRIFFIN)
                .setAmountAndTotalHp(1)
                .setPositions(7,1)
                .setTeam(UnitTeam.LIGHT).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.ANGEL)
                .setAmountAndTotalHp(5)
                .setPositions(3,1)
                .setTeam(UnitTeam.LIGHT).build());
        builder.reset();

        boardEntities.add(unitPropsService.fillDataToUnitByUClass(UnitClass.CAVALIER)
                .setAmountAndTotalHp(12)
                .setPositions(5,1)
                .setTeam(UnitTeam.LIGHT).build());
        builder.reset();
    }

    public void addUnitToCustomGame(UnitClass uClass, int amount, int y, int x, UnitTeam team, UnitPropsService unitPropsService){
        boardEntities.add(unitPropsService.fillDataToUnitByUClass(uClass)
                .setAmountAndTotalHp(amount)
                .setPositions(y,x)
                .setTeam(team).build());
        builder.reset();
    }

    public String checkGameStatus(){
        boolean lightIn = boardEntities.stream().filter((obj)->obj instanceof Unit)
                .anyMatch((unit) -> ((Unit) unit).getTeam() == UnitTeam.LIGHT);
        boolean darkIn = boardEntities.stream().filter((obj)->obj instanceof Unit)
                .anyMatch((unit) -> ((Unit) unit).getTeam() == UnitTeam.DARK);
        if(lightIn && darkIn)
            return GameStatus.PLAYING.getDescription();
        else if (lightIn)
            return GameStatus.LIGHT_WIN.getDescription();
         else if (darkIn)
            return GameStatus.DARK_WIN.getDescription();
        else
            return GameStatus.TIE.getDescription();

    }


    public Unit chooseTheUnit(int y, int x){
        Tile entity = boardEntities.stream().filter(tile -> tile.checkEntityPosition(y,x)).findFirst().orElse(null);
        if(entity != null){
            if(entity instanceof Unit)
                return (Unit) entity;
        }
        return null;
    }


    public void markPotentialMovesForUnit(Unit unit){
        if(unit == null) return;
        opportunityPlaces.removeAll(opportunityPlaces);
        unit.setState(TileState.ACTIVE);
        int[] stepsRange = getRangeMinXMinYMaxXMaxY(unit, unit.getStepsDistance());

        for(int i = stepsRange[1]; i <= stepsRange[3]; i++){
            for (int j = stepsRange[0]; j<= stepsRange[2]; j++){
                final int y = i; final int x = j;
                Tile entity = boardEntities.stream().filter(tile -> tile.checkEntityPosition(y,x)).findFirst().orElse(null);
                if(entity != null) {
                    if (entity instanceof Baricade) continue;
                    if (entity.equals(unit)) continue;
                    if (((Unit)entity).getTeam() == unit.getTeam()) continue;
                }
                opportunityPlaces.add(new Coordinates(i, j, true, true));
            }
        }

        int[] fightRange = getRangeMinXMinYMaxXMaxY(unit, unit.getFightDistance());
        for(int i = fightRange[1]; i <= fightRange[3]; i++){
            for (int j = fightRange[0]; j<= fightRange[2]; j++){
                final int y = i; final int x = j;
                Tile entity = boardEntities.stream().filter(tile -> tile.checkEntityPosition(y,x)).findFirst().orElse(null);
                if(entity != null) {
                    if (entity instanceof Baricade) continue;
                    if (entity.equals(unit)) continue;
                    if (((Unit)entity).getTeam() == unit.getTeam()) continue;
                }
                if(!opportunityPlaces.isEmpty()) {
                    if (opportunityPlaces.stream().noneMatch((obj) -> y == obj.getY() && x == obj.getX()) &&
                            boardEntities.stream().anyMatch(tile -> tile.checkEntityPosition(y, x)))
                        opportunityPlaces.add(new Coordinates(i, j, false, true));
                }
            }
        }

    }

    public boolean checkPlaceToUnitAction(int y, int x){
        return opportunityPlaces.stream().anyMatch((obj) -> y == obj.getY() && x == obj.getX());
    }

    public Unit getUnitFromFieldByExample(Unit exampleUnit){
        return boardEntities.stream().filter(obj -> obj instanceof Unit).map(obj -> (Unit)obj)
                .filter(unit -> unit.equals(exampleUnit)).findFirst().orElse(null);
    }

    private int[] getRangeMinXMinYMaxXMaxY(Unit unit, int distance){
        int[] range = new int[4];
        int currX = unit.getX();
        int currY = unit.getY();
        range[0] = Math.max(currX - distance, 0);
        range[1] = Math.max(currY - distance, 0);
        range[2] = Math.min(currX + distance, columnCount-1);
        range[3] = Math.min(currY + distance, rowCount-1);
        return range;
    }


    @SuppressWarnings("CollectionAddedToSelf")
    public void moveUnit(Unit unit, int y, int x){
        Tile entity = boardEntities.stream().filter(tile -> tile.checkEntityPosition(y,x)).findFirst().orElse(null);
        if(entity instanceof Unit && ((Unit)entity).getTeam() != unit.getTeam()) {
            if (opportunityPlaces.stream().anyMatch((obj) -> y == obj.getY() && x == obj.getX() && !obj.isInStepsRange())) {
                unit.damageTheUnit((Unit)entity, false);
                unit.setState(TileState.BUSY);
            } else {
                int[] arroundRange = getRangeMinXMinYMaxXMaxY((Unit)entity, 1);
                for (int i = arroundRange[1]; i <= arroundRange[3]; i++) {
                    for (int j = arroundRange[0]; j <= arroundRange[2]; j++) {
                        final int Y = i;
                        final int X = j;
                        //TODO якшо все занято біля юніта, відмінити хід
                        Tile freePlace = boardEntities.stream().filter(tile -> tile.checkEntityPosition(Y,X)).findFirst().orElse(null);
                        if (freePlace == null) {
                            unit.setY(i);
                            unit.setX(j);
                            unit.setState(TileState.BUSY);
                            unit.damageTheUnit((Unit)entity, true);
                            opportunityPlaces.removeAll(opportunityPlaces);
                            return;
                        }

                    }
                }
            }
        }
        else {
            unit.setX(x);
            unit.setY(y);
            unit.setState(TileState.BUSY);
        }
        opportunityPlaces.removeAll(opportunityPlaces);
    }


    public void checkUnitsHealthToPrintAlives(){
        List<Tile> deathUnit = boardEntities.stream().filter(entity -> entity instanceof Unit)
                .filter(entity -> ((Unit) entity).getAmount() <= 0).toList();
        boardEntities.removeAll(deathUnit);
    }


    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }
}
