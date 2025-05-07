package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("gitminer/issues")
public class IssueController {

    @Autowired
    IssueRepository issueRepository;

    // GET http://localhost:8080/gitminer/issues
    @GetMapping
    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    // GET http://localhost:8080/gitminer/issues/{id}
    @GetMapping("/{id}")
    public Issue findOne(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException();
        }

        return issue.get();
    }

    // POST http://localhost:8080/gitminer/issues
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Issue createIssue(@Valid @RequestBody Issue issue) {
        Issue _issue = issueRepository.save(issue);
        return _issue;
    }

    // PUT http://localhost:8080/gitminer/issues
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateIssue(@Valid @RequestBody Issue updateIssue, @PathVariable String id) throws IssueNotFoundException {
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable String id) throws IssueNotFoundException {
        if(!issueRepository.existsById(id)){
            throw new IssueNotFoundException();
        }

        issueRepository.deleteById(id);
    }

}
