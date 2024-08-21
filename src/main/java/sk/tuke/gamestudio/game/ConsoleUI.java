package sk.tuke.gamestudio.game;


import sk.tuke.gamestudio.game.core.Player;
import sk.tuke.gamestudio.game.core.*;
import sk.tuke.gamestudio.services.PlayerService;
import sk.tuke.gamestudio.services.UnitPropsService;
import sk.tuke.gamestudio.entities.Comment;
import sk.tuke.gamestudio.entities.Rating;
import sk.tuke.gamestudio.entities.Score;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.core.units.Baricade;
import sk.tuke.gamestudio.game.core.units.Unit;
import sk.tuke.gamestudio.game.core.units.UnitClass;
import sk.tuke.gamestudio.game.core.units.UnitTeam;
import sk.tuke.gamestudio.services.*;

import java.util.*;

public class ConsoleUI {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UnitPropsService unitPropsService;
    @Autowired
    private ScoreServiceRestClient scoreService;
    @Autowired
    private CommentServiceRestClient commentService;
    @Autowired
    private RatingServiceRestClient ratingService;

    @Autowired
    private Field field ;

    public static final String RESET = "\033[0m";  // Reset to default color
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String helpTileChoose = "For choose the tile you need to write first Letter of column lower or upper-case,\n and then write number of row";
    public static final String chooseUnitOption = "Choose the unit (A1): ";

    public static final String choosePlaceToUnitAction = "Choose the tile for move (A1): ";

    public final HashMap<Integer, UnitClass> classMap = new HashMap<>();


//    private final CommentService commentService = new CommentService();
//    private final  RatingService ratingService = new RatingService();
//    private final ScoreService scoreService = new ScoreService();

    private final Scanner scanner = new Scanner(System.in);

    public void playRuleGame(){
        List<Player> playerList = introToFight();
        Unit cur_unit = new Unit();
        show();
        System.out.println();
        waitMoment(2000);
        Queue<Tile> queue = fillQueue();
        System.out.println();
        while (field.checkGameStatus().equals("playing")) {
            cur_unit = (Unit)queue.poll();
            if(cur_unit != null)
                while (cur_unit.getAmount() == 0){
                    cur_unit = (Unit)queue.poll();
                    if (cur_unit == null) {
                        queue = fillQueue();
                        cur_unit = (Unit)queue.poll();
                        break;
                    }
                }
            else{
                queue = fillQueue();
                cur_unit = (Unit)queue.poll();
            }

            if(cur_unit != null) {
                if (cur_unit.getTeam() == UnitTeam.LIGHT) {
                    System.out.println(GREEN + "The forces of light are coming with " + cur_unit.getName() + RESET);
                } else{
                    System.out.println(RED + "The forces of dark are coming with " + cur_unit.getName() + RESET);
                }
                field.markPotentialMovesForUnit(cur_unit);
                System.out.println();
                show();
                cur_unit = printOptionAndChooseUnit(choosePlaceToUnitAction, cur_unit);
                show();
                System.out.println();
                waitMoment(2000);
            }
        }

        System.out.println();
        Player lightPlayer = playerList.stream().filter((player -> player.getPlayerActualTeam() == UnitTeam.LIGHT)).findFirst().orElse(null);
        Player darkPlayer = playerList.stream().filter((player -> player.getPlayerActualTeam() == UnitTeam.DARK)).findFirst().orElse(null);

        Score winnerScore = null;
        if(lightPlayer == null || darkPlayer == null) {
            System.out.println("Error with players");
            return;
        }
        if(field.checkGameStatus().equals("light_win")) {
            int costLigth = field.getBoardEntities().stream().filter((obj) -> obj instanceof Unit)
                    .filter((obj)->(((Unit) obj).getTeam() == UnitTeam.LIGHT))
                    .mapToInt((obj)->((Unit) obj).getCost()).sum();
            System.out.println(GREEN + "Powers of Light win, thank you for game" + RESET);
            lightPlayer.setMoney(lightPlayer.getMoney() + 4000 + costLigth);
            darkPlayer.setMoney(darkPlayer.getMoney() + 2000);
            winnerScore = new Score("HMaM", lightPlayer.getLogin(), costLigth, new Date());
        }
        else if(field.checkGameStatus().equals("dark_win")) {
            int costDark = field.getBoardEntities().stream().filter((obj) -> obj instanceof Unit)
                    .filter((obj)->(((Unit) obj).getTeam() == UnitTeam.DARK))
                    .mapToInt((obj)->((Unit) obj).getCost()).sum();
            System.out.println(RED + "Powers of Dark win, thank you for game" + RESET);
            darkPlayer.setMoney(darkPlayer.getMoney() + 4000 + costDark);
            lightPlayer.setMoney(lightPlayer.getMoney() + 2000);
            winnerScore = new Score("HMaM", darkPlayer.getLogin(), costDark, new Date());
        }
        else {
            System.out.println(CYAN + "Well play, Tie" + RESET);
        }

        if(winnerScore != null)
            scoreService.addScore(winnerScore);
        playerService.savePlayer(lightPlayer);
        playerService.savePlayer(darkPlayer);
        System.out.println();


        showStats();
        System.out.println();
        System.out.println();

        askForCommentAndRating(lightPlayer);
        askForCommentAndRating(darkPlayer);


        askForPlayAgain();


    }
    public void playNoRuleGame(){
        List<Player> playerList = introToFight();
        Unit cur_unit = new Unit();
        Score winnerScore = null;
        show();
        while (field.checkGameStatus() .equals("playing")) {
            cur_unit = printOptionAndChooseUnit(chooseUnitOption, cur_unit);
            field.markPotentialMovesForUnit(cur_unit);
            show();
            cur_unit = printOptionAndChooseUnit(choosePlaceToUnitAction, cur_unit);
            show();
        }

        System.out.println();
        if(field.checkGameStatus() .equals("light_win")) {
            int costLigth = field.getBoardEntities().stream().filter((obj) -> obj instanceof Unit)
                    .filter((obj)->(((Unit) obj).getTeam() == UnitTeam.LIGHT))
                    .mapToInt((obj)->((Unit) obj).getCost()).sum();
            System.out.println(GREEN + "Powers of Light win, thank you for game" + RESET);
            Player lightPlayer = playerList.stream().filter((obj)-> obj.getPlayerActualTeam() == UnitTeam.LIGHT).findFirst().orElse(null);
            if (lightPlayer != null) {
                lightPlayer.setMoney(lightPlayer.getMoney() + 4000 + costLigth);
                playerList.stream().filter((obj) -> obj.getPlayerActualTeam() == UnitTeam.LIGHT).forEach(
                        obj -> obj.setMoney(obj.getMoney() + 4000 + costLigth));
                playerList.stream().filter((obj) -> obj.getPlayerActualTeam() == UnitTeam.DARK).forEach(
                        obj -> obj.setMoney(obj.getMoney() + 2000));
                winnerScore = new Score("HMaM", lightPlayer.getLogin(), costLigth, new Date());
            }
        }
        else if(field.checkGameStatus() .equals("dark_win")) {
            int costDark = field.getBoardEntities().stream().filter((obj) -> obj instanceof Unit)
                    .filter((obj)->(((Unit) obj).getTeam() == UnitTeam.DARK))
                    .mapToInt((obj)->((Unit) obj).getCost()).sum();
            System.out.println(RED + "Powers of Dark win, thank you for game" + RESET);
            Player darkPlayer = playerList.stream().filter((obj)-> obj.getPlayerActualTeam() == UnitTeam.DARK).findFirst().orElse(null);
            if (darkPlayer != null) {
                darkPlayer.setMoney(darkPlayer.getMoney() + 4000 + costDark);
                playerList.stream().filter((obj) -> obj.getPlayerActualTeam() == UnitTeam.LIGHT).forEach(
                        obj -> obj.setMoney(obj.getMoney() + 2000));
                winnerScore = new Score("HMaM", darkPlayer.getLogin(), costDark, new Date());
            }
        }
        else
            System.out.println(CYAN + "Well play, Tie" + RESET);

        if (winnerScore != null) {
            scoreService.addScore(winnerScore);
        }
        playerService.savePlayer(playerList.get(0));
        playerService.savePlayer(playerList.get(1));
        System.out.println();

        showStats();
        System.out.println();

    }
    public void showStats(){
        showScore();
        System.out.println();
        showComments();
        System.out.println();
        showRate();
        System.out.println();

    }
    private void printRules(){
        System.out.println(GREEN +
                "   -- Welcome to Heroes of Might and Magic. -- \n" + BLUE +
                "           You can choose the following game variants: default or custom," +
                "\n" +
                " in the first variant you will be provided with a ready-made unit for a strategic battle," +
                "\n" +
                "     in the second case, you will be able to buy units and place them on the field.\n" +
                "\n" + RED +
                " The game is designed for two teams, the attacking forces of Light (green) and the forces of Darkness (red).\n" +
                "\n" + YELLOW +
                " When you see the field, the turn will be started by the attacking team, the units are selected from the fastest to the slowest regardless of the team. \n" +
                "\n" + CYAN +
                " The selected unit will be highlighted in purple, the cells with cyan diamonds will appear, you can move there," +
                "\n" +
                " or units that are yellow will become available for attack, if they are within walking distance, the attack will be made in melee.\n" +
                "\n" +
                " An important mechanic of melee combat is that when an enemy unit attacks, it can strike back.\n" +
                "\n" +  PURPLE +
                " The game will end when all enemy units are destroyed, if the last two units die at the same time, it will be a draw.\n" +
                "\n" + RESET);
    }
    private void addAllUnitsToMap(){
        classMap.put(1, UnitClass.PIKEMAN);
        classMap.put(2, UnitClass.ARCHER);
        classMap.put(3, UnitClass.GRIFFIN);
        classMap.put(4, UnitClass.SWORDSMAN);
        classMap.put(5, UnitClass.MONK);
        classMap.put(6, UnitClass.CAVALIER);
        classMap.put(7, UnitClass.ANGEL);
    }
    private Player loginPlayer(Player player1){
        System.out.println(YELLOW + " -- Player authorisation -- " + RESET);

        System.out.print("Enter login: ");
        String login = scanner.nextLine();

        if(player1 != null) {
            if (login.equals(player1.getLogin())){
                System.out.println("Its user already logined");
                return null;
            }
        }
        Player player = playerService.getPlayerByName(login);
        if (player == null) {
            System.out.println("Player not found");
            return null;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if(player.getPassword().equals(password)) {
            System.out.println("You authorised successfully");
            return player;
        }
        else {
            System.out.println("Sorry wrong password");
            return null;
        }
    }
    private Player registerPlayer(){
        Player player = null;
        System.out.println("Register new player: ");
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if(login != null && password != null){
            player = new Player(login, password, 5000);
            playerService.savePlayer(player);
        }
        return player;
    }
    private List<Player> introToFight(){
        addAllUnitsToMap();
        printRules();
        Player player1 = null, player2 = null;
        String option;
        while (player1 == null) {
            System.out.println("Choose option: [login register]");
            option = scanner.nextLine();
            if (option.equalsIgnoreCase("login")) {
                player1 = loginPlayer(null);
            } else if (option.equalsIgnoreCase("register")) {
                player1 = registerPlayer();
            } else {
                System.out.println("You write wrong option");
            }
        }
        player1.setPlayerActualTeam(UnitTeam.LIGHT);

        System.out.println();
        while (player2 == null) {
            System.out.println("Choose option: [login register]");
            option = scanner.nextLine();
            if (option.equalsIgnoreCase("login")) {
                player2 = loginPlayer(player1);
            } else if (option.equalsIgnoreCase("register")) {
                player2 = registerPlayer();
            } else {
                System.out.println("You write wrong option");
            }
        }
        player2.setPlayerActualTeam(UnitTeam.DARK);

        String game = null;
        while (game == null) {
            System.out.println("You want to play default or custom game (default/custom)");
            game = scanner.nextLine();

            if (game.equalsIgnoreCase("default")) {
                field.loadDefaultGame(unitPropsService);
            } else if (game.equalsIgnoreCase("custom")) {
                field.loadMap();
                show();

                showUnitsShopList();

                System.out.println();
                System.out.println(GREEN + player1.getLogin() + " your turn to buy units, your lines A and B:" + RESET);
                boolean endBuying = false;
                while (player1.getMoney() > 0 && !endBuying) {
                    System.out.println("You have: " + player1.getMoney() + " gold");
                    String[] result = getDataForCreateUnit();
                    if (result.length == 1 && result[0].equalsIgnoreCase("end"))
                        endBuying = true;
                    if (checkDataAndCreateUnit(player1, result))
                        System.out.println("Try again");
                }

                System.out.println();
                System.out.println(RED + player2.getLogin() + " your turn to buy units, your lines O and P:" + RESET);
                endBuying = false;
                while (player1.getMoney() > 0 && !endBuying) {
                    System.out.println("You have: " + player2.getMoney() + " gold");
                    String[] result = getDataForCreateUnit();
                    if (result.length == 1 && result[0].equalsIgnoreCase("end"))
                        endBuying = true;
                    else if (!checkDataAndCreateUnit(player2, result))
                        System.out.println("Try again");
                }
            }
            else {
                game = null;
            }
        }

        List<Player> playerlst = new ArrayList<>();
        playerlst.add(player1);
        playerlst.add(player2);
        return playerlst;

    }
    private void waitMoment(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace(); // Handle the interruption exception if needed
        }
    }
    private Queue<Tile> fillQueue(){
        Queue<Tile> queue = new LinkedList<>();
        field.getBoardEntities().stream().filter(obj -> obj instanceof Unit).sorted((obj1, obj2) -> ((Unit)obj2).getSpeed() - ((Unit)obj1).getSpeed())
                .forEach(queue::add);
        return queue;
    }
    private void showUnitsShopList(){
        List<Unit> unitList = unitPropsService.getAllUnitsList();
        int iterator = 1;
        System.out.println();
        for (Unit unit : unitList){
            System.out.println(iterator++ + ". " + unit.getName() + " by " + unit.getCost() + " per one");

        }
        System.out.println();
        System.out.println("For buy and place on board write with spaces [id amount position(A1)]");
    }
    private String[] getDataForCreateUnit(){
        String stringUnit = scanner.nextLine();
        return stringUnit.split(" ");
    }
    private boolean checkDataAndCreateUnit(Player player, String[] resultArray){
        if(resultArray.length == 3) {
            int classNum = Integer.parseInt(resultArray[0]);
            int amount = Integer.parseInt(resultArray[1]);


            if (!(classNum > 0 && classNum < 8)){
                System.out.println("Wrong class number!");
                return true;
            }

            int unitCost = unitPropsService.getUnitCostById(classNum);
            if (amount < 0 || (player.getMoney() - (unitCost * amount)) < 0){
                System.out.println("Not enough money");
                return true;
            }
            if (!(resultArray[2].matches("[a-pA-P][1-8]"))){
                System.out.println("Not allowed position on board");
                return true;
            }


            int x = resultArray[2].charAt(0) - 65;
            int y = resultArray[2].charAt(1) - 49;

            if(player.getPlayerActualTeam() == UnitTeam.LIGHT) {
                if (!(x == 0 || x == 1)) {
                    System.out.println("You enter wrong position, you can place it only on A or B line");
                    return true;
                }
            }
            else {
                if (!(x == 14 || x == 15)){
                    System.out.println("You enter wrong position, you can place it only on O or P line");
                    return true;
                }
            }



            field.addUnitToCustomGame(classMap.get(classNum), amount, y, x, player.getPlayerActualTeam(), unitPropsService);
            System.out.println("Unit added");
            player.setMoney(player.getMoney() - (amount * unitCost));
            return false;

        }
        return true;
    }
    private Unit printOptionAndChooseUnit(String option, Unit unit){
        System.out.println();
        System.out.print(option);
        String pos = scanner.nextLine().toUpperCase();
        if(pos.matches("[a-pA-P][1-8]")){
            int x = pos.charAt(0) - 65;
            int y = pos.charAt(1) - 49;
            if(option.equals(chooseUnitOption)){
                if(field.chooseTheUnit(y,x) == null){
                    System.out.println("You enter wrong positions! Try again");
                    unit = printOptionAndChooseUnit(option, unit);
                }
                else
                    return field.chooseTheUnit(y,x);
            }
            else if(option.equals(choosePlaceToUnitAction)){
                if(!field.checkPlaceToUnitAction(y, x)){
                    System.out.println("You enter wrong positions! Try again");
                    unit = printOptionAndChooseUnit(option, unit);
                }
                else {
                    field.moveUnit(unit, y, x);
                }
            }
        }
        else{
            System.out.println("Wrong form of position! Try again");
            return printOptionAndChooseUnit(option, unit);
        }
        return unit;
    }
    private void show(){
        field.checkUnitsHealthToPrintAlives();
        printAlfaRow();
        for (int i = 0; i < field.getRowCount(); i++){
            printMinusRow();
            System.out.print("-");
            System.out.println();
            System.out.print(i +1);
            for (int j = 0; j < field.getColumnCount(); j++){
                System.out.print(" | ");
                final int y = i;
                final int x = j;
                Tile entity = field.getBoardEntities().stream().filter(tile -> tile.checkEntityPosition(y,x)).findFirst().orElse(null);
                if(entity != null) {
                    if ((field.getOpportunityPlaces().stream().anyMatch((obj) -> y == obj.getY() && x == obj.getX()))) {
                        entity.printTileVisualisation(YELLOW, RESET);
                    }
                    else {
                        String color = RESET;
                        if(!(entity instanceof Baricade)) {
                            if (((Unit) entity).getTeam() == UnitTeam.LIGHT)
                                color = GREEN;
                            else color = RED;
                            if (entity.getState() == TileState.ACTIVE)
                                color = PURPLE;
                        }

                        entity.printTileVisualisation(color, RESET);
                    }
                }
                else {
                    if(field.getOpportunityPlaces().stream().anyMatch((obj)-> y == obj.getY() && x == obj.getX()))
                        System.out.print(CYAN+  "✦" + RESET);
                    else
                        System.out.print(" ");
                }
            }
            System.out.print(" |");
            System.out.println();
        }
        printMinusRow();
        System.out.print("-");
    }
    private void printMinusRow(){
        System.out.print("  ");
        for (int j = 0; j < field.getColumnCount(); j++){
            System.out.print("-−--");
        }
    }
    private void printAlfaRow(){
        System.out.print("  ");
        for (int j = 0; j < field.getColumnCount(); j++){
            char letter = (char) (j + 65);
            System.out.print("  " + letter + " ");
        }
        System.out.println();
    }
    private void showScore(){
        List<Score> scores = scoreService.getTopScores("HMaM");
        if (scores.isEmpty()) {
            System.out.println(" Empty list ");
            return;
        }
        System.out.println(" --------- ");
        System.out.println("  Scores");
        System.out.println(" --------- ");
        scores.stream().forEach(System.out::println);
    }
    private void showRate(){
        int averageRating = ratingService.getAverageRating("HMaM");
        System.out.println(" ------------------------------ ");
        System.out.println("Average rating is : " + averageRating);
        System.out.println(" ------------------------------ ");
    }
    private void showComments(){
        List<Comment> comments = commentService.getComments("HMaM");
        if (comments.isEmpty()) {
            System.out.println(" Empty list ");
            return;
        }
        System.out.println(" ---------- ");
        System.out.println("  Comments");
        System.out.println(" ---------- ");
        comments.stream().forEach(System.out::println);
    }
    private void askForCommentAndRating(Player player){
        System.out.println(player.getLogin() + " would you want to add comment and rating(Y/N): ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("Y")) {
            System.out.print("Write your rating (0-10): ");
            String rating = scanner.nextLine();
            int ratingNum = Integer.parseInt(rating);
            while (ratingNum < 0 || ratingNum > 10) {
                System.out.print("Write your rating right (0-10): ");
                rating = scanner.nextLine();
                ratingNum = Integer.parseInt(rating);
            }
            ratingService.setRating(new Rating("HMaM", player.getLogin(), ratingNum, new Date()));
            String comment = "";
            while (comment.isEmpty()){
                System.out.print("Write your comment: ");
                comment = scanner.nextLine();
            }
            commentService.addComment(new Comment("HMaM", player.getLogin(), comment, new Date()));
        } else {
            System.out.println("May be next time! It is so important for us ;)");
        }
    }
    private void askForPlayAgain() {
        System.out.println("Do you want play again? (Y/N)");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("Y")) {
            field = new Field();
            playRuleGame();
        }
        System.exit(0);

    }



}
