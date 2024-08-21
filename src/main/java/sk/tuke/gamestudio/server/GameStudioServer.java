package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import sk.tuke.gamestudio.game.core.Field;
import sk.tuke.gamestudio.repos.UnitPropsRepo;
import sk.tuke.gamestudio.services.*;


@ComponentScan("sk.tuke.gamestudio")
@SpringBootApplication
@Configuration
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }


//    @Bean
//    public Field field() {
//        return new Field();
//    }
//
//    @Bean
//    public UnitPropsService unitPropsService() {
//        return new UnitPropsService();
//    }

}
