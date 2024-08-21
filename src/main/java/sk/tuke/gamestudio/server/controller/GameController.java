package sk.tuke.gamestudio.server.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameController {
    @RequestMapping("/login")
    public String HMaM(){
        return "index";
    }

    @RequestMapping("/hmam")
    public String HMaM1(){
        return "react";
    }

}
