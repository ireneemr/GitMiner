package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
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

@Tag(name="Commit", description= "Commit management API")
@RestController
@RequestMapping("gitminer/commits")
public class CommitController {

    @Autowired
    CommitRepository commitRepository;

    //GET http://localhost:8080/gitminer/commits
    @Operation(
            summary = "Retrieve a list of commits",
            description= "Get a list of commits",
            tags={"commits", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Listado de commits",
                    content = { @Content(schema = @Schema(implementation = Commit.class),
                            mediaType = "application/json") })})
    @GetMapping
    public List<Commit> findAll(){return commitRepository.findAll();}

    //GET http://localhost:8080/gitminer/commits/{id}
    @Operation(
            summary = "Retrieve a Commit by Id",
            description= "Get a Commit object by specifying its id",
            tags={"commits", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Listado de commits",
                    content = { @Content(schema = @Schema(implementation = Commit.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "404",
                    description= "Commit no encontrado",
                    content={@Content(schema=@Schema())})
    })
    @GetMapping("/{id}")
    public Commit findOne(@Parameter(description="id of commit to be searched") @PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> commit = commitRepository.findById(id);
        if(!commit.isPresent()){
            throw new CommitNotFoundException();
        }
        return commit.get();
    }

    //POST http://localhost:8080/gitminer/commits
    @Operation(
            summary= "Insert a Commit",
            description = "Add a new Commit whose data is passed in the body of the request in JSON format",
            tags={"commits", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = { @Content(schema = @Schema(implementation = Commit.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400",
                    content={@Content(schema=@Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Commit createCommit(@Valid @RequestBody Commit commit) {
        Commit _commit= commitRepository.save(commit);
        return _commit;
    }

    //PUT http://localhost:8080/gitminer/commits/{id}
    @Operation(
            summary= "Update a Commit",
            description = "Update a new Commit object by specifying its id and whose data is passed in the body of the request in JSON format",
            tags={"commits", "put"})
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
    public void updateCommit(@Valid @RequestBody Commit updatedCommit, @Parameter(description = "id of the commit to be updated") @PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> commitData = commitRepository.findById(id);
        if(!commitData.isPresent()){
            throw new CommitNotFoundException();
        }
        Commit _commit = commitData.get();
        _commit.setTitle(updatedCommit.getTitle());
        _commit.setMessage(updatedCommit.getMessage());
        _commit.setAuthorName(updatedCommit.getAuthorName());
        _commit.setAuthorEmail(updatedCommit.getAuthorEmail());
        _commit.setAuthoredDate(updatedCommit.getAuthoredDate());
        _commit.setWebUrl(updatedCommit.getWebUrl());
        commitRepository.save(_commit);
    }

    //DELETE http://localhost:8080/gitminer/commits/{id}
    @Operation(
            summary = "Delete a Commit",
            description = "Delete a commit object by specifying its id",
            tags= {"commits", "delete"})
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
    public void deleteCommit(@Parameter(description = "id of the commit to be deleted") @PathVariable String id) throws CommitNotFoundException {
        if(!commitRepository.existsById(id)){
            throw new CommitNotFoundException();
        }
        commitRepository.deleteById(id);
    }

}
