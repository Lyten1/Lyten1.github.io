package sk.tuke.gamestudio.game.core.units;


import sk.tuke.gamestudio.game.core.Tile;
import jakarta.persistence.*;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


@Entity
@Table(name = "units_props_makoda")
public class Unit extends Tile {
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj instanceof Unit){
            Unit unitObj = (Unit) obj;
            if(unitObj.name.equals(this.name) && unitObj.team.equals(this.team)
            && unitObj.getX() == this.getX() && unitObj.getY() == this.getY())
                return true;
        }
        return false;
    }

    @Id
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "sprite", nullable = false)
    private String sprite;
    @Column(name = "health", nullable = false)
    private int health;
    @Column(name = "attack", nullable = false)
    private int attack;
    @Column(name = "damage_min", nullable = false)
    private int damageMin;

    @Column(name = "damage_max", nullable = false)
    private int damageMax;
    @Column(name = "defence", nullable = false)
    private int defence;
    @Column(name = "stepsDistance", nullable = false)
    private int stepsDistance;
    @Column(name = "fightDistance", nullable = false)
    private int fightDistance;
    @Column(name = "cost", nullable = false)
    private int cost;
    @Column(name = "speed", nullable = false)
    private int speed;
    @Transient
    private UnitTeam team;
    @Transient
    private UnitClass uClass;
    @Transient
    private int totalHP;
    @Transient
    private int amount;



    public Unit() {
    }

    public Unit(Unit unit){
        this.name = unit.name;
        this.sprite = unit.sprite;
        this.team = unit.team;
        this.uClass = unit.uClass;
        this.health = unit.health;
        this.damageMin = unit.damageMin;
        this.damageMax = unit.damageMax;
        this.amount = unit.amount;
        this.totalHP = health * amount;
        this.attack = unit.attack;
        this.defence = unit.defence;
        this.stepsDistance = unit.stepsDistance;
        this.fightDistance = unit.fightDistance;
        this.positions = unit.positions;
        this.cost = unit.cost;
    }

    public Unit(String name, String sprite, UnitTeam team, UnitClass uClass, int health,
                int amount, int attack, int defence, int damage_min, int damage_max,
                int stepsCount, int fightDistance, int cost, Map<String , Integer> positions) {
        this.name = name;
        this.sprite = sprite;
        this.team = team;
        this.uClass = uClass;
        this.health = health;
        this.damageMin = damage_min;
        this.damageMax = damage_max;
        this.amount = amount;
        this.totalHP = health * amount;
        this.attack = attack;
        this.defence = defence;
        this.stepsDistance = stepsCount;
        this.fightDistance = fightDistance;
        this.positions = positions;
        this.cost = cost;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnitClass getuClass() {
        return uClass;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDamageMin(int damage_min) {
        this.damageMin = damage_min;
    }

    public void setDamageMax(int damage_max) {
        this.damageMax = damage_max;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void setFightDistance(int fightDistance) {
        this.fightDistance = fightDistance;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setTeam(UnitTeam team) {
        this.team = team;
    }

    public void setuClass(UnitClass uClass) {
        this.uClass = uClass;
    }



    public int getDamageMin() {
        return damageMin;
    }

    public int getDamageMax() {
        return damageMax;
    }

    public int getCost() {
        return cost;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    public String getSprite() {
        return sprite;
    }

    public UnitTeam getTeam() {
        return team;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStepsDistance() {
        return stepsDistance;
    }

    public void setStepsDistance(int stepsDistance) {
        this.stepsDistance = stepsDistance;
    }

    public int getTotalHP(){
        return totalHP;
    }
    public void setTotalHP(int hp){
        this.totalHP = hp;
    }



    @Override
    public void printTileVisualisation(String color, String reset){
        System.out.print(color +  sprite + reset);
    }



    public int getFightDistance() {
        return fightDistance;
    }

    @Override
    public String getClassName() {
        return "Unit";
    }

    public void damageTheUnit(Unit enemy, boolean willRevenge){
        int randomNumber = ThreadLocalRandom.current().nextInt(this.damageMin,this.damageMax);
        int defaultDamage = randomNumber * this.amount;
        float extraDamage = 0.0f;
        if(this.attack > enemy.defence){
            extraDamage = (defaultDamage * 0.05f) * (this.attack - enemy.defence);
        }
        else if(this.attack < enemy.defence){
            extraDamage = (defaultDamage * 0.025f) * (this.attack - enemy.defence);
        }

        int totalDamage = (int) Math.ceil(defaultDamage + extraDamage);

        enemy.setTotalHP(Math.max(enemy.getTotalHP() - totalDamage, 0));
        int newAmount = enemy.getTotalHP() / enemy.getHealth();
        int  damageInUnits = enemy.getAmount() - newAmount;
        enemy.setAmount(newAmount);

//        System.out.println(this.getuClass()+  " have dealt " + damageInUnits +" damage  :  "+ enemy.getuClass() +" have left "+ enemy.getAmount() + " units");

        if(willRevenge && enemy.getAmount() != 0){
            enemy.damageTheUnit(this, false);
        }

    }


}
