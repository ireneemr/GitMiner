

package aiss.gitminer.controller;

import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.CommentRepository;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueRepository issueRepository;

    // Obtener todos los comentarios de un Issue específico
    @GetMapping("/issue/{issueId}")
    public List<Comment> getCommentsByIssue(@PathVariable String issueId) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null) {
            return null;  // o manejar con un error adecuado
        }
        return commentRepository.findByIssue(issue);
    }

    // Crear un nuevo comentario para un Issue
    @PostMapping("/issue/{issueId}")
    public Comment createComment(@PathVariable String issueId, @RequestBody Comment comment) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null) {
            return null;  // o manejar con un error adecuado
        }
        comment.setIssue(issue);
        return commentRepository.save(comment);
    }

    // Otros métodos como editar o eliminar comentarios si es necesario
}
