package sk.tuke.gamestudio.services;

import sk.tuke.gamestudio.game.core.Player;
import sk.tuke.gamestudio.repos.PlayerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepo playerRepo;

    public Player getPlayerByName(String name){
        return playerRepo.getByLogin(name);
    }

    public void savePlayer(Player player){
        playerRepo.saveAndFlush(player);
    }
}
