package com.example.demo.controller;

import com.example.demo.model.LoginUser;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class IndexController {

    public List<User> users = new ArrayList<>();

    @Autowired
    private NotificationService notificationService;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value ="/users")
    public List<User> getAll(){
        return users;
    }

    @PostMapping(value = "/add")
    public void addUser(@RequestBody User user){
        users.add(user);
    }

    @GetMapping(value = "/find/{name}")//search
    public User getUserByName(@PathVariable String username){
        return users.stream().filter(name -> name.getName()== username).findFirst().get();
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginUser loginUser){//ulvi@mail /123
        System.out.println(users);
        User user =users.stream().filter(name -> name.getUsername().equals(loginUser.getUsername())).findFirst().orElseGet(()->(User)users.stream().filter(name -> name.getEmail().equals(loginUser.getUsername())).findFirst().get());
//                .orElse(users.stream().filter(name -> name.getEmail().equals(loginUser.getUsername())).findFirst().get());
        System.out.println(user);
        if(user.getPassword().equals(loginUser.getPassword())){
            notificationService.sendEmailWhenUserUpdated(user);
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }
    @PostMapping(value = "/test")
    public void test(@RequestBody User user){
        System.out.println(user);
        userRepository.save(user);

    }

    @GetMapping(value = "/testUsers")
    public List<User> getUsers(){
        return userRepository.findAll();
    }


}
