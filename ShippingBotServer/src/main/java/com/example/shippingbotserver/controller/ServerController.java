package com.example.shippingbotserver.controller;

import com.example.shippingbotserver.dto.UserDto;
import com.example.shippingbotserver.entity.User;
import com.example.shippingbotserver.exceptions.ImageDrawerErrorException;
import com.example.shippingbotserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ServerController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto user(@PathVariable("id") Long id) {
        try {
            return userService.findUser(id);
        } catch (IOException e) {
            throw new ImageDrawerErrorException();
        }
    }

    @GetMapping("/{id}/search")
    public UserDto showQuestionnaire(@PathVariable Long id) {

        try {
            return userService.search(id);
        } catch (IOException e) {
            throw new ImageDrawerErrorException();
        }
    }

    @GetMapping("/{id}/favorites/{action}")
    public UserDto userFavorites(@PathVariable("id") Long id,
                                 @PathVariable("action") String action) {
        try {
            return userService.getFavorite(id, action);
        } catch (IOException e) {
            throw new ImageDrawerErrorException();
        }
    }

    @PostMapping("/attitude/{action}")
    public UserDto attitude(@RequestBody User user, @PathVariable String action) {
        try {
            return userService.attitudePush(user, action);
        } catch (IOException e) {
            throw new ImageDrawerErrorException();
        }
    }


    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String addPersonQuery(@RequestBody User user) {
        userService.saveLover(user);
        return "OK";
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String personPutQuery(@RequestBody User person) {
        userService.update(person);
        return "OK";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String personDeleteQuery(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "OK";
    }
}