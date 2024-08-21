package sk.tuke.gamestudio.game.core;

import lombok.Getter;
import lombok.Setter;
import sk.tuke.gamestudio.game.core.units.UnitTeam;
import jakarta.persistence.*;

import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "players_makoda")
@Getter
@Setter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "money")
    private int money;

    @Column(name = "unitsString")
    private String unitsString;


    @Transient
    private UnitTeam playerActualTeam;

    public Player() {
    }

    public Player(String login, String password, int money) {
        if(money < 0 || login == null || password == null){
            return;
        }
        this.login = login;
        this.password = password;
        this.money = money;
        this.unitsString = "Archer:0-Cavalier:0-Pikeman:0-Angel:0-Griffin:0-Monk:0-Swordsman:0";
    }





    public UnitTeam getPlayerActualTeam() {
        return playerActualTeam;
    }

    public void setPlayerActualTeam(UnitTeam playerActualTeam) {
        this.playerActualTeam = playerActualTeam;
    }

    public String getLogin() {
        return login;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        if(money >= 0)
            this.money = money;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return money == player.money &&
                Objects.equals(login, player.login) &&
                Objects.equals(password, player.password);
    }

}
