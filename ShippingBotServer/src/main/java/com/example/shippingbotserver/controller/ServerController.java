package com.example.shippingbotserver.controller;

import com.example.shippingbotserver.dao.DaoProcessing;
import com.example.shippingbotserver.entity.Lover;
import com.example.shippingbotserver.view.FormLoverModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
public class ServerController {

    private final DaoProcessing daoProcessing;

    @Autowired
    public ServerController(DaoProcessing daoProcessing) {
        this.daoProcessing = daoProcessing;
    }

    @GetMapping("/person/{id}")
    public @ResponseBody ResponseEntity<FormLoverModel> getLoverByUserId(@PathVariable("id") Long request) {
        log.debug("log message {}", "request by /server/person/" + request);
        FormLoverModel form = daoProcessing.findLoverById(request);
        log.debug("log message {}", "Response OK");
        return new ResponseEntity<>(form, HttpStatus.OK);
    }

    @GetMapping("/person/{id}/show/lovers")
    public @ResponseBody ResponseEntity<FormLoverModel> showQuestionnaire(@PathVariable Long id){
        log.debug("log message {}", "request by /server/person/" + id +"/show/lovers");
        FormLoverModel formLoverModel = daoProcessing.getQuestionnaire(id);
        log.debug("log message {}", "Response OK");
        return new ResponseEntity<>(formLoverModel, HttpStatus.OK);
    }

    @GetMapping("/{id}/preference/{action}")
    public @ResponseBody ResponseEntity<FormLoverModel> getLoverPreference(@PathVariable("id") Long id,
                                                                           @PathVariable("action") String action) {
        log.debug("log message {}", "request by /server/" + id + "/preference/" + action);
        FormLoverModel favorite = daoProcessing.getFavorite(id, action);
        log.debug("log message {}", "Response OK");
        return new ResponseEntity<>(favorite, HttpStatus.OK);
    }

    @PostMapping("/person/dislike")
    public @ResponseBody ResponseEntity<FormLoverModel> dislikeLover(@RequestBody Lover id) {
        log.debug("log message {}", "request by /server/person/dislike with parameter" + id.getId());
        FormLoverModel formLoverModel = daoProcessing.dislike(id);
        log.debug("log message {}", "Response OK");
        return new ResponseEntity<>(formLoverModel, HttpStatus.OK);
    }

    @PostMapping("/person/like")
    public @ResponseBody ResponseEntity<FormLoverModel> likeLover(@RequestBody Lover id) {
        log.debug("log message {}", "request by /server/person/like with parameter" + id.getId());
        FormLoverModel formLoverModel = daoProcessing.like(id);
        log.debug("log message {}", "Response OK");
        return new ResponseEntity<>(formLoverModel, HttpStatus.OK);
    }


    @PostMapping("/person")
    @ResponseStatus(HttpStatus.OK)
    public String addPersonQuery(@RequestBody Lover person) {
        log.debug("log message {}", "request by /server/person with parameter" + person);
        daoProcessing.saveLover(person);
        log.debug("log message {}", "Response OK");
        return "OK";
    }

    @PutMapping("/update/person")
    @ResponseStatus(HttpStatus.OK)
    public String personPutQuery(@RequestBody Lover person) {
        log.debug("log message {}", "request by /server/update/person with parameter" + person);
        daoProcessing.update(person);
        log.debug("log message {}", "Response OK");
        return "OK";
    }

    @DeleteMapping("/person/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String personDeleteQuery(@PathVariable("id") Long id) {
        log.debug("log message {}", "request by /server/person/" + id);
        daoProcessing.deleteLover(id);
        log.debug("log message {}", "Response OK");
        return "OK";
    }
}