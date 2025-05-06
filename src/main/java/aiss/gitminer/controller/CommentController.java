package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("gitminer/comments")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    //GET http://localhost:8080/gitminer/comments
    @GetMapping
    public List<Comment> findAll(){return commentRepository.findAll();}

    //GET http://localhost:8080/gitminer/comments/{id}
    @GetMapping("/{id}")
    public Comment findOne(@PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }
        return comment.get();
    }

    //POST http://localhost:8080/gitminer/comments
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Comment createComment(@Valid @RequestBody Comment comment) {
        Comment _comment= commentRepository.save(comment);
        return _comment;
    }

    //PUT http://localhost:8080/gitminer/comments/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateComment(@Valid @RequestBody Comment updatedComment, @PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> commentData = commentRepository.findById(id);
        if(!commentData.isPresent()){
            throw new CommentNotFoundException();
        }
        Comment _comment = commentData.get();
        _comment.setBody(updatedComment.getBody());
        _comment.setAuthor(updatedComment.getAuthor());
        _comment.setCreatedAt(updatedComment.getCreatedAt());
        _comment.setUpdatedAt(updatedComment.getUpdatedAt());
        commentRepository.save(_comment);
    }

    //DELETE http://localhost:8080/gitminer/comments/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable String id) throws CommentNotFoundException {
        if(!commentRepository.existsById(id)){
            throw new CommentNotFoundException();
        }
        commentRepository.deleteById(id);
    }



}
