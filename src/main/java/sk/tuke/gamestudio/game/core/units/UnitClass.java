package sk.tuke.gamestudio.game.core.units;

public enum UnitClass {
    PIKEMAN("PIKEMAN"),
    ARCHER("ARCHER"),
    GRIFFIN("GRIFFIN"),
    SWORDSMAN("SWORDSMAN"),
    MONK("MONK"),
    CAVALIER("CAVALIER"),
    ANGEL("ANGEL");

    private final String description;


    private UnitClass(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
