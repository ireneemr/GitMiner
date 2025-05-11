package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name="Project", description= "Project management API")
@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    // LISTA DONDE SE ALMACENA LOS DATOS ENVIADOS POR LAS OTRAS APIs
    private final List<Project> datosRECIVIDOS = new ArrayList<>();


    // GET http://localhost:8080/gitminer/projects
    @Operation(
            summary = "Retrieve a list of projects",
            description= "Get a list of projects",
            tags={"projects", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Listado de projects",
                    content = { @Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json") })})
    @GetMapping
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    // GET http://localhost:8080/gitminer/projects/{id}
    @Operation(
            summary = "Retrieve a Project by Id",
            description= "Get a Project object by specifying its id",
            tags={"projects", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Listado de projects",
                    content = { @Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "404",
                    description= "Project no encontrado",
                    content={@Content(schema=@Schema())})
    })
    @GetMapping("/{id}")
    public Project findOne(@Parameter(description="id of project to be searched") @PathVariable String id) throws ProjectNotFoundException {

        Optional<Project> project = projectRepository.findById(id);

        if(!project.isPresent()) {
            throw new ProjectNotFoundException();
        }

        return project.get();
    }


    // POST http://localhost:8080/gitminer/projects/recive
    @Operation(
            summary= "Insert a Project",
            description = "Add a new project whose data is passed through the adapters. ",
            tags={"projects", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = { @Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400",
                    content={@Content(schema=@Schema())})
    })
    @PostMapping("/recive")
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProjectA(@RequestBody @Valid Project project) {
        projectRepository.save(new Project(project.getId(), project.getName(), project.getWebUrl(), project.getCommits(), project.getIssues()));
        System.out.println("📦 Datos recibidos en API B: " + project.getName() + ", " + project.getWebUrl());
        return project;
    }

    // POST http://localhost:8080/gitminer/projects
    @Operation(
            summary= "Insert a Project",
            description = "Add a new Project whose data is passed in the body of the request in JSON format",
            tags={"projects", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = { @Content(schema = @Schema(implementation = Project.class),
                            mediaType = "application/json") }),
            @ApiResponse(responseCode = "400",
                    content={@Content(schema=@Schema())})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@Valid @RequestBody Project project) {
        Project _project = projectRepository.save(project);
        return _project;
    }

    // PUT http://localhost:8080/gitminer/projects/{id}
    @Operation(
            summary= "Update a Project",
            description = "Update a new Project object by specifying its id and whose data is passed in the body of the request in JSON format",
            tags={"projects", "put"})
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
    public void updateProject(@RequestBody @Valid Project upProject, @Parameter(description="id of the comment to be updated") @PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> projectDATA = projectRepository.findById(id);
        if(!projectDATA.isPresent()) {
            throw new ProjectNotFoundException();
        }

        Project _proj = projectDATA.get();
        _proj.setName(upProject.name);
        _proj.setWebUrl(upProject.webUrl);

        projectRepository.save(_proj);

    }


    // DELETE http://localhost:8080/gitminer/projects/{id}
    @Operation(
            summary = "Delete a Project",
            description = "Delete a project object by specifying its id",
            tags= {"projects", "delete"}
    )
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
    public void  deleteProject(@Parameter(description="id of the comment to be deleted") @PathVariable String id) throws ProjectNotFoundException {
        if(!projectRepository.existsById(id)){
            throw new ProjectNotFoundException();
        }
        projectRepository.deleteById(id);
    }


}
