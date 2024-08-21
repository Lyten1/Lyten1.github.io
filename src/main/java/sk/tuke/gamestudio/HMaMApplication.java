package sk.tuke.gamestudio;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.core.Field;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.game.ConsoleUI;
import sk.tuke.gamestudio.server.controller.GameRestController;
import sk.tuke.gamestudio.services.PlayerService;
import sk.tuke.gamestudio.services.UnitPropsService;
import sk.tuke.gamestudio.services.*;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))
public class HMaMApplication {

    public static void main(String[] args) {
        //SpringApplication.run(HMaMApplication.class, args);
        new SpringApplicationBuilder(HMaMApplication.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }
    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public PlayerService playerService() {
        return new PlayerService();
    }
    @Bean
    public UnitPropsService unitPropsService() {
        return new UnitPropsService();
    }

    @Bean
    public ConsoleUI consoleUI() {
        return new ConsoleUI();
    }

    @Bean
    public Field field() {
        return new Field();
    }



//    @Bean
//    public CommandLineRunner runner(ConsoleUI ui) {
//        //return args -> ui.playRuleGame();
//        return args -> ui.showStats();
//    }



}
