package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("gitminer/commits")
public class CommitController {

    @Autowired
    CommitRepository commitRepository;

    //GET http://localhost:8080/gitminer/commits
    @GetMapping
    public List<Commit> findAll(){return commitRepository.findAll();}

    //GET http://localhost:8080/gitminer/commits/{id}
    @GetMapping("/{id}")
    public Commit findOne(@PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> commit = commitRepository.findById(id);
        if(!commit.isPresent()){
            throw new CommitNotFoundException();
        }
        return commit.get();
    }

    //POST http://localhost:8080/gitminer/commits
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Commit createCommit(@Valid @RequestBody Commit commit) {
        Commit _commit= commitRepository.save(commit);
        return _commit;
    }

    //PUT http://localhost:8080/gitminer/commits/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateCommit(@Valid @RequestBody Commit updatedCommit, @PathVariable String id) throws CommitNotFoundException {
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCommit(@PathVariable String id) throws CommitNotFoundException {
        if(!commitRepository.existsById(id)){
            throw new CommitNotFoundException();
        }
        commitRepository.deleteById(id);
    }

}
