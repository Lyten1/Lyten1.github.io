package sk.tuke.gamestudio.game.core.units;

public enum UnitTeam {
    LIGHT("LIGHT"),
    DARK("DARK");

    private final String description;

    // Constructor for enum is always private.
    private UnitTeam(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
