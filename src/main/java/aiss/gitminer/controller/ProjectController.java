package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;


    // GET http://localhost:8080/api/projects
    @GetMapping
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    // GET http://localhost:8080/api/projects/{id}
    @GetMapping("/{id}")
    public Project findOne(@PathVariable String id) throws ProjectNotFoundException {

        Optional<Project> project = projectRepository.findById(id);

        if(!project.isPresent()) {
            throw new ProjectNotFoundException();
        }

        return project.get();
    }


}
