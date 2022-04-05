package com.example.shippingbotserver.controller;

import com.example.shippingbotserver.dao.DaoProcessing;
import com.example.shippingbotserver.entity.Lover;
import com.example.shippingbotserver.view.FormLoverModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ServerController {

    private final DaoProcessing daoProcessing;

    @Autowired
    public ServerController(DaoProcessing daoProcessing) {
        this.daoProcessing = daoProcessing;
    }

    @GetMapping("/person/{id}")
    public @ResponseBody ResponseEntity<FormLoverModel> getLoverByUserId(@PathVariable("id") Long request) throws IOException {
        FormLoverModel form = daoProcessing.findLoverById(request);
        return new ResponseEntity<>(form, HttpStatus.OK);
    }

    @GetMapping("/person/{id}/show/lovers")
    public @ResponseBody ResponseEntity<FormLoverModel> showQuestionnaire(@PathVariable Long id) throws IOException {
        FormLoverModel formLoverModel = daoProcessing.getQuestionnaire(id);
        return new ResponseEntity<>(formLoverModel, HttpStatus.OK);
    }

    @GetMapping("/{id}/preference/{action}")
    public @ResponseBody ResponseEntity<FormLoverModel> getLoverPreference(@PathVariable("id") Long id,
                                                                           @PathVariable("action") String action) throws IOException {
        FormLoverModel favorite = daoProcessing.getFavorite(id, action);
        return new ResponseEntity<>(favorite, HttpStatus.OK);
    }

    @PostMapping("/person/dislike")
    public @ResponseBody ResponseEntity<FormLoverModel> dislikeLover(@RequestBody Lover id) throws IOException {
        FormLoverModel formLoverModel = daoProcessing.dislike(id);
        return new ResponseEntity<>(formLoverModel, HttpStatus.OK);
    }

    @PostMapping("/person/like")
    public @ResponseBody ResponseEntity<FormLoverModel> likeLover(@RequestBody Lover id) throws IOException {
        FormLoverModel formLoverModel = daoProcessing.like(id);
        return new ResponseEntity<>(formLoverModel, HttpStatus.OK);
    }


    @PostMapping("/person")
    @ResponseStatus(HttpStatus.OK)
    public String addPersonQuery(@RequestBody Lover person) {
        daoProcessing.saveLover(person);
        return "OK";
    }

    @PutMapping("/update/person")
    @ResponseStatus(HttpStatus.OK)
    public String personPutQuery(@RequestBody Lover person) {
        daoProcessing.update(person);
        return "OK";
    }

    @DeleteMapping("/person/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String personDeleteQuery(@PathVariable("id") Long id) {
        daoProcessing.deleteLover(id);
        return "OK";
    }
}