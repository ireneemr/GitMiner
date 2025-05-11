package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name="Comment", description= "Comment management API")
@RestController
@RequestMapping("gitminer/comments")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    //GET http://localhost:8080/gitminer/comments
    @Operation(
            summary = "Retrieve a list of comments",
            description= "Get a list of comments",
            tags={"comments", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Listado de comments",
                    content = { @Content(schema = @Schema(implementation = Comment.class),
                    mediaType = "application/json") })})
    @GetMapping
    public List<Comment> findAll(){return commentRepository.findAll();}

    //GET http://localhost:8080/gitminer/comments/{id}
    @Operation(
            summary = "Retrieve a Comment by Id",
            description= "Get a Comment object by specifying its id",
            tags={"comments", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Listado de comments",
                    content = { @Content(schema = @Schema(implementation = Comment.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "404",
                    description= "Comment no encontrado",
                    content={@Content(schema=@Schema())})
    })
    @GetMapping("/{id}")
    public Comment findOne(@Parameter(description="id of comment to be searched") @PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }
        return comment.get();
    }

    //POST http://localhost:8080/gitminer/comments
    @Operation(
            summary= "Insert a Comment",
            description = "Add a new Comment whose data is passed in the body of the request in JSON format",
            tags={"comments", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = { @Content(schema = @Schema(implementation = Comment.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400",
                    content={@Content(schema=@Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Comment createComment(@Valid @RequestBody Comment comment) {
        Comment _comment= commentRepository.save(comment);
        return _comment;
    }

    //PUT http://localhost:8080/gitminer/comments/{id}
    @Operation(
            summary= "Update a Comment",
            description = "Update a new Comment object by specifying its id and whose data is passed in the body of the request in JSON format",
            tags={"comments", "put"})
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400",
                    content={@Content(schema=@Schema())}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema=@Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateComment(@Valid @RequestBody Comment updatedComment, @Parameter(description = "id of the comment to be updated") @PathVariable String id) throws CommentNotFoundException {
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
    @Operation(
            summary = "Delete a Comment",
            description = "Delete a comment object by specifying its id",
            tags= {"comments", "delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400",
                    content={@Content(schema=@Schema())}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema=@Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteComment(@Parameter(description = "id of the comment to be deleted") @PathVariable String id) throws CommentNotFoundException {
        if(!commentRepository.existsById(id)){
            throw new CommentNotFoundException();
        }
        commentRepository.deleteById(id);
    }



}
