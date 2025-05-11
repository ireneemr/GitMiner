package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name="Issue", description= "Issue management API")
@RestController
@RequestMapping("gitminer/issues")
public class IssueController {

    @Autowired
    IssueRepository issueRepository;

    // GET http://localhost:8080/gitminer/issues
    @Operation(
            summary = "Retrieve a list of issues",
            description= "Get a list of issues",
            tags={"issues", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Listado de issues",
                    content = { @Content(schema = @Schema(implementation = Issue.class),
                            mediaType = "application/json") })})
    @GetMapping
    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    // GET http://localhost:8080/gitminer/issues/{id}
    @Operation(
            summary = "Retrieve an Issue by Id",
            description= "Get an Issue object by specifying its id",
            tags={"issues", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Listado de issues",
                    content = { @Content(schema = @Schema(implementation = Issue.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "404",
                    description= "Issue no encontrado",
                    content={@Content(schema=@Schema())})
    })
    @GetMapping("/{id}")
    public Issue findOneById(@Parameter(description="id of issue to be searched") @PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException();
        }

        return issue.get();
    }

    //GET http://localhost:8080/gitminer/issues/{id}/comments
    @Operation(
            summary = "Retrieve a list of Comments by Issue Id",
            description= "Get a list of comments by specifying the Issue Id",
            tags={"issues","comments", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Listado de comments de una issue",
                    content = { @Content(schema = @Schema(implementation = Issue.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "404",
                    description= "Issue no encontrado",
                    content={@Content(schema=@Schema())})
    })
    @GetMapping("/{id}/comments")
    public List<Comment> findAllCommentsByIssueId(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException();
        }
        return issue.get().getComments();
    }

    // GET http://localhost:8080/gitminer/issues/state/{state}
    @Operation(
            summary = "Retrieve an Issue by state",
            description= "Get an Issue object by specifying its state",
            tags={"issues", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Listado de issues",
                    content = { @Content(schema = @Schema(implementation = Issue.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "404",
                    description= "Issue no encontrado",
                    content={@Content(schema=@Schema())})
    })
    @GetMapping("/state/{state}")
    public List<Issue> findOneByState(@PathVariable String state) throws IssueNotFoundException {
        List<Issue> issue = issueRepository.findByState(state);
        if (!issue.isEmpty()) {
            throw new IssueNotFoundException();
        }

        return issue;
    }

    // POST http://localhost:8080/gitminer/issues
    @Operation(
            summary= "Insert an Issue",
            description = "Add a new Issue whose data is passed in the body of the request in JSON format",
            tags={"issues", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = { @Content(schema = @Schema(implementation = Issue.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400",
                    content={@Content(schema=@Schema())})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Issue createIssue(@Valid @RequestBody Issue issue) {
        Issue _issue = issueRepository.save(issue);
        return _issue;
    }

    // PUT http://localhost:8080/gitminer/issues
    @Operation(
            summary= "Update an issue",
            description = "Update a new Issue object by specifying its id and whose data is passed in the body of the request in JSON format",
            tags={"issues", "put"})
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400",
                    content={@Content(schema=@Schema())}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema=@Schema())})
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateIssue(@Valid @RequestBody Issue updateIssue, @Parameter(description="id of issue to be updated") @PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issueDATA = issueRepository.findById(id);
        if(!issueDATA.isPresent()) {
            throw new IssueNotFoundException();
        }

        Issue _issue = issueDATA.get();
        _issue.setTitle(updateIssue.getTitle());
        _issue.setDescription(updateIssue.getDescription());
        _issue.setState(updateIssue.getState());
        _issue.setCreatedAt(updateIssue.getCreatedAt());
        _issue.setUpdatedAt(updateIssue.getUpdatedAt());
        _issue.setClosedAt(updateIssue.getClosedAt());
        _issue.setLabels(updateIssue.getLabels());
        _issue.setVotes(updateIssue.getVotes());

        issueRepository.save(_issue);
    }

    //DELETE http://localhost:8080/gitminer/issues
    @Operation(
            summary = "Delete an Issue",
            description = "Delete an issue object by specifying its id",
            tags= {"issues", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    content = { @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400",
                    content={@Content(schema=@Schema())}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema=@Schema())})
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@Parameter(description="id of issue to be deleted") @PathVariable String id) throws IssueNotFoundException {
        if(!issueRepository.existsById(id)){
            throw new IssueNotFoundException();
        }

        issueRepository.deleteById(id);
    }

}
