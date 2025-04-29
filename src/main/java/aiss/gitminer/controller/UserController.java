package aiss.gitminer.controller;

import aiss.gitminer.exception.UserNotFoundException;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer")
public class UserController {

    @Autowired
    UserRepository userRepository;

    //GET http://localhost:8080/gitminer/users
    @GetMapping
    public List<User> findAll() {
        return userRepository.findAll();
    }

    //GET http://localhost:8080/gitminer/users/{username}
    @GetMapping("/{username}")
    public User findOne(@PathVariable String username) throws UserNotFoundException {
        Optional<User> user=userRepository.findByUsername(username);
        if(!user.isPresent()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }

    //POST http://localhost:8080/gitminer/users
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid User user) {
        User _user=userRepository.save(user);
        return _user;
    }

    //PUT http://localhost:8080/gitminer/users{username}
    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody @Valid User updatedUser, @PathVariable String username) throws UserNotFoundException {
        Optional<User> userData=userRepository.findByUsername(username);
        User _user=userData.get();
        _user.setUsername(updatedUser.getUsername());
        _user.setName(updatedUser.getName());
        _user.setAvatarUrl(updatedUser.getAvatarUrl());
        _user.setWebUrl(updatedUser.getWebUrl());
        userRepository.save(_user);
    }

    //DELETE http://localhost:8080/gitminer/users/{username}
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        }
    }

}
