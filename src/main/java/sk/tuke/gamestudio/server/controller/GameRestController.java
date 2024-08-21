package sk.tuke.gamestudio.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entities.GameSession;
import sk.tuke.gamestudio.game.core.Coordinates;
import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.game.core.Tile;
import sk.tuke.gamestudio.game.core.UnitStringTranslator;
import sk.tuke.gamestudio.game.core.units.Unit;
import sk.tuke.gamestudio.game.core.units.UnitBuilder;
import sk.tuke.gamestudio.game.core.units.UnitClass;
import sk.tuke.gamestudio.game.core.units.UnitTeam;
import sk.tuke.gamestudio.repos.PlayerRepo;
import sk.tuke.gamestudio.services.GameService;
import sk.tuke.gamestudio.services.UnitPropsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@RestController
@RequestMapping("/api")
public class GameRestController {


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UnitBuilder unitBuilder = new UnitBuilder();
    @Autowired
    private UnitPropsService unitPropsService;

    @Autowired
    private PlayerRepo playerRepo;

    @Autowired
    private GameService gameService;

    @PostMapping("/createGame")
    public ResponseEntity<String> createGame(@RequestParam String login1, @RequestParam String login2){

        String token = gameService.createGameSession(); // Create and return a unique game session token
        GameSession session = gameService.getSession(token);

        session.setPlayer_light(playerRepo.getByLogin(login1));
        session.setPlayer_dark(playerRepo.getByLogin(login2));


        if(session.getField().getBoardEntities().isEmpty()) {
            session.getField().loadMap();
//            session.getField().loadLightTeam(unitPropsService);
//            session.getField().loadDarkTeam(unitPropsService);
//            fillQueue(token);
        }

        return ResponseEntity.ok(token);
    }

    @PostMapping("/addUnits")
    public ResponseEntity<String> addUnits(@RequestParam String token, @RequestBody List<UnitDTO> unitDTOList){
        GameSession session = gameService.getSession(token);



        return ResponseEntity.ok(token);
    }

    public void fillQueue( String token){
        GameSession session = gameService.getSession(token);
        if(session.getQueue().isEmpty())
            session.getField().getBoardEntities().stream()
                    .filter(obj -> obj instanceof Unit)
                    .sorted((obj1, obj2) -> ((Unit)obj2).getSpeed() - ((Unit)obj1).getSpeed())
                    .forEach(session.getQueue()::add);
    }



    @GetMapping("/entities")
    public List<Tile> getBoardEntities(@RequestParam String token) {
        GameSession session = gameService.getSession(token);
        if (session != null) {
            return session.getField().getBoardEntities();
        }
        return null;
    }

    @GetMapping("/gameStatus")
    public String getGameStatus(@RequestParam String token) {
        GameSession session = gameService.getSession(token);
        if (session != null) {
            return session.getField().checkGameStatus();
        }
        return null;
    }

    @GetMapping("/getWinCost")
    public int getWinCost(@RequestParam String token) {
        GameSession session = gameService.getSession(token);
        if (session != null) {
            String res = session.getField().checkGameStatus();
            if(!res.equals("playing")){
                if(res.equals("light_win")){
                    return session.getField().getBoardEntities().stream().filter((obj) -> obj instanceof Unit)
                            .filter((obj)->(((Unit) obj).getTeam() == UnitTeam.LIGHT))
                            .mapToInt((obj)->((Unit) obj).getCost()).sum();
                }
                else if(res.equals("dark_win")){
                    return session.getField().getBoardEntities().stream().filter((obj) -> obj instanceof Unit)
                            .filter((obj)->(((Unit) obj).getTeam() == UnitTeam.DARK))
                            .mapToInt((obj)->((Unit) obj).getCost()).sum();
                }
            }

        }
        return 0;
    }


    @GetMapping("/getActualUnit")
    public Unit getActualUnit(@RequestParam String token) {
        GameSession session = gameService.getSession(token);
        if (session == null) {
            return null;
        }

        Queue<Tile> queue = session.getQueue();
        Unit currentUnit = (Unit) queue.peek();

        while (currentUnit != null && currentUnit.getAmount() == 0) {
            queue.poll();
            currentUnit =(Unit) queue.peek();
        }

        if (currentUnit == null) {
            fillQueue(token); // Assumes fillQueue accepts a token
            currentUnit = (Unit)queue.peek();
        }

        return currentUnit;
    }


    @GetMapping("/potentialMoves")
    public List<Coordinates> getMoves(@RequestParam String token) {
        GameSession session = gameService.getSession(token);
        if (session == null) {
            return null;
        }

        try {

            Unit unit = (Unit) session.getQueue().peek();
            Field field = session.getField();
            field.markPotentialMovesForUnit(unit);
            return field.getOpportunityPlaces();
        } catch (Exception e) {
            System.out.println("Error identifying potential moves: " + e.getMessage());
            return null;
        }
    }



    @PostMapping("/move")
    public ResponseEntity<String> makeMove(@RequestParam String token, @RequestParam int newY, @RequestParam int newX, @RequestParam String text) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Session token is required.");
        }

        GameSession session = gameService.getSession(token);
        if (session == null) {
            return ResponseEntity.badRequest().body("Invalid session token or session has expired.");
        }

        try {
            Unit exUnit = parseUnitFromStringData(text);
            if (exUnit == null) {
                return ResponseEntity.badRequest().body("Unit data is invalid or missing.");
            }

            Unit unit = session.getField().getUnitFromFieldByExample(exUnit);
            if (unit == null) {
                return ResponseEntity.notFound().build();
            }

            if (unit.getAmount() == 0) {
                return ResponseEntity.badRequest().body("Unit has been defeated and cannot move.");
            }
            session.getField().moveUnit(unit, newY, newX);
            session.getQueue().poll();
            return ResponseEntity.ok("Unit moved successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing the move: " + e.getMessage());
        }
    }



    @GetMapping("/getPlayersUnits")
    public String getPlayersUnits(@RequestParam String player) {
        System.out.println(player);
        System.out.println(playerRepo.getByLogin(player).getUnitsString());
        return playerRepo.getByLogin(player).getUnitsString();
    }


    private Unit parseUnitFromStringData(String data){
        if(data.equals(""))
            return null;
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(data);
            UnitTeam team = UnitTeam.valueOf(jsonNode.get("team").asText());
            UnitClass unitClass = UnitClass.valueOf(jsonNode.get("uClass").asText());
            return new UnitBuilder()
                    .setName(jsonNode.get("name").asText())
                    .setSprite(jsonNode.get("sprite").asText())
                    .setHealth(jsonNode.get("health").asInt())
                    .setAttack(jsonNode.get("attack").asInt())
                    .setDamageMin(jsonNode.get("damageMin").asInt())
                    .setDamageMax(jsonNode.get("damageMax").asInt())
                    .setDefence(jsonNode.get("defence").asInt())
                    .setStepsDistance(jsonNode.get("stepsDistance").asInt())
                    .setFightDistance(jsonNode.get("fightDistance").asInt())
                    .setCost(jsonNode.get("cost").asInt())
                    .setSpeed(jsonNode.get("speed").asInt())
                    .setTeam(team)
                    .setuClass(unitClass)
                    .setTotalHP(jsonNode.get("totalHP").asInt())
                    .setAmount(jsonNode.get("amount").asInt())
                    .setX(jsonNode.get("x").asInt())
                    .setY(jsonNode.get("y").asInt())
                    .build();
        }catch (NullPointerException e){
            return null;
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
