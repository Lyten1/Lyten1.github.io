package sk.tuke.gamestudio.game.core;

public enum GameStatus {
    PLAYING("playing"),
    LIGHT_WIN("light_win"),
    DARK_WIN("dark_win"),
    TIE("tie"),
    END("end");

    private final String description;


    private GameStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
