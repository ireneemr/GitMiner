package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;


    // GET http://localhost:8080/gitminer/projects
    @GetMapping
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    // GET http://localhost:8080/gitminer/projects/{id}
    @GetMapping("/{id}")
    public Project findOne(@PathVariable String id) throws ProjectNotFoundException {

        Optional<Project> project = projectRepository.findById(id);

        if(!project.isPresent()) {
            throw new ProjectNotFoundException();
        }

        return project.get();
    }

    // POST http://localhost:8080/gitminer/projects
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@RequestBody @Valid Project project) {
        Project _proj = projectRepository.save(project);
        return _proj;
    }

    // PUT http://localhost:8080/gitminer/projects/{id}
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProject(@RequestBody @Valid Project upProject, @PathVariable String id) throws ProjectNotFoundException {
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteProject(@PathVariable String id) throws ProjectNotFoundException {
        if(!projectRepository.existsById(id)){
            throw new ProjectNotFoundException();
        }
        projectRepository.deleteById(id);
    }


}
