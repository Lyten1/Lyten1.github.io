package sk.tuke.gamestudio.game.core.units;


public class UnitBuilder {


    private Unit unit = new Unit();

    public UnitBuilder setPropertiesFromUnit(Unit unit) {
        this.unit.setName(unit.getName());
        this.unit.setHealth(unit.getHealth());
        this.unit.setStepsDistance(unit.getStepsDistance());
        this.unit.setFightDistance(unit.getFightDistance());
        this.unit.setCost(unit.getCost());
        this.unit.setDamageMin(unit.getDamageMin());
        this.unit.setDamageMax(unit.getDamageMax());
        this.unit.setAttack(unit.getAttack());
        this.unit.setDefence(unit.getDefence());
        this.unit.setSprite(unit.getSprite());
        this.unit.setSpeed(unit.getSpeed());
        return this;
    }

    public UnitBuilder reset() {
        unit = new Unit();
        return this;
    }



    public UnitBuilder setuClass(UnitClass uClass) {
        unit.setuClass(uClass);
        return this;
    }


    public UnitBuilder setPositions(int y, int x) {
        unit.setX(x);
        unit.setY(y);
        return this;
    }

    public UnitBuilder setTeam(UnitTeam team){
        unit.setTeam(team);
        return this;
    }

    public UnitBuilder setAmountAndTotalHp(int amount){
        unit.setAmount(amount);
        unit.setTotalHP(amount * this.unit.getHealth());
        return this;
    }

    public Unit build() {
        return unit;
    }


    public UnitBuilder setName(String name) {
        unit.setName(name);
        return this;
    }

    public UnitBuilder setSprite(String sprite) {
        unit.setSprite(sprite);
        return this;
    }

    public UnitBuilder setHealth(int health) {
        unit.setHealth(health);
        return this;
    }

    public UnitBuilder setAttack(int attack) {
        unit.setAttack(attack);
        return this;
    }

    public UnitBuilder setDamageMin(int damageMin) {
        unit.setDamageMin(damageMin);
        return this;
    }

    public UnitBuilder setDamageMax(int damageMax) {
        unit.setDamageMax(damageMax);
        return this;
    }

    public UnitBuilder setDefence(int defence) {
        unit.setDefence(defence);
        return this;
    }

    public UnitBuilder setStepsDistance(int stepsDistance) {
        unit.setStepsDistance(stepsDistance);
        return this;
    }

    public UnitBuilder setFightDistance(int fightDistance) {
        unit.setFightDistance(fightDistance);
        return this;
    }

    public UnitBuilder setCost(int cost) {
        unit.setCost(cost);
        return this;
    }

    public UnitBuilder setSpeed(int speed) {
        unit.setSpeed(speed);
        return this;
    }


    public UnitBuilder setTotalHP(int totalHP) {
        unit.setTotalHP(totalHP);
        return this;
    }

    public UnitBuilder setAmount(int amount) {
        unit.setAmount(amount);
        return this;
    }


    public UnitBuilder setX(int x) {
        unit.setX(x);
        return this;
    }

    public UnitBuilder setY(int y) {
        unit.setY(y);
        return this;
    }

}
