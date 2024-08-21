package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.game.core.Player;
import sk.tuke.gamestudio.services.PlayerService;

@RestController
@RequestMapping("/api")
public class UserRestController {


    @Autowired
    private PlayerService playerService;

    @PostMapping("/authorize")
    public ResponseEntity<String> authorization(@RequestBody UserCredentials credentials){
        Player player = playerService.getPlayerByName(credentials.getLogin());
        if(player != null){
            if(player.getPassword().equals(credentials.getPassword()))
                return ResponseEntity.ok().body("User authorized");
            return ResponseEntity.badRequest().body("User enter wrong password");
        }
        return ResponseEntity.badRequest().body("User isn't registered");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserCredentials credentials){
        Player player = playerService.getPlayerByName(credentials.getLogin());
        if(player == null){
            Player newPlayer = new Player(credentials.getLogin(), credentials.getPassword(), 4000);
            playerService.savePlayer(newPlayer);
            return ResponseEntity.ok().body("User registered");
        }
        return ResponseEntity.badRequest().body("User is already registered");
    }
}


class UserCredentials {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}