package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entities.Comment;
import sk.tuke.gamestudio.entities.Score;
import sk.tuke.gamestudio.services.CommentService;


import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {


    @Autowired
    private CommentService commentService;

    @GetMapping("/{game}")
//    @CrossOrigin(origins = "http://localhost:3000")
    public List<Comment> getComments(@PathVariable String game) {
        return commentService.getComments(game);
    }

    @PostMapping
//    @CrossOrigin(origins = "http://localhost:3000")
    public void addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
    }
}
