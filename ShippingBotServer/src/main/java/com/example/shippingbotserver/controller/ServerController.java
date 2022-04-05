package com.example.shippingbotserver.controller;

import com.example.shippingbotserver.dao.DaoProcessing;
import com.example.shippingbotserver.entity.Lover;
import com.example.shippingbotserver.model.LoverModel;
import com.example.shippingbotserver.repository.LoverModelRepository;
import com.example.shippingbotserver.repository.LoverRepository;
import com.example.shippingbotserver.translation.TextHandler;
import com.example.shippingbotserver.view.FormHandler;
import com.example.shippingbotserver.view.FormLoverModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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



    @GetMapping("/aaa")
    public String pictureWriteToWrite(){
        FormHandler formHandler = new FormHandler(new TextHandler());
        Lover lover = new Lover(new LoverModel(1234334L,
                "Иван",
                "boy",
                "Андрей\n Вдлмудлкьмдлукьидлькуид лиьдлкЬидлкьеидлкьеидлкьеилДькедБлиьдлкЬЬидлкьеидлкьеидлкьеилДьдлкьеидлкьеилДькедБлиьдлкЬЬидлкьеидлкьеидлкьеилДьдлкьеидлкьеилДькедБлиьдлкЬЬидлкьеидлкьеидлкьеилДькедБлиьдлкЬЬидлкьеидлкьеидлкьеилДькедБлиьдлкЬидлкьеидлкьеидлкьеилДькеилдьктеилдьткедилькелдитлЛлькдБлиьдлкЬидлкьеидлкьеидлкьеилДькеилдьктеилдьткедилькелдитлЛлькдБлиьдлкЬидлкьеидлкьеидлкьеилДькеилдьктеилдьткедилькелдитлЛлькдБлиьдлкЬидлкьеидлкьеидлкьеилДькеилотеЕкилтеВдлмудлкьмдлуклиьдлкЬидлЮьеидлкьеидлкьеилДькедБлиьдлкЬЬидлкьеиКонец",
//                "Андрей\n ЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕЕ",
                "girl"));

        formHandler.init(lover);
        formHandler.meth();
        return "Good";
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
    public String addPersonQuery(@RequestBody Lover person){
        daoProcessing.saveLover(person);
        return "OK";
    }

    @PutMapping("/string/person")
    @ResponseStatus(HttpStatus.OK)
    public String personPutQuery(@RequestBody Lover person){
        daoProcessing.saveLover(person);
        return "OK";
    }

    @DeleteMapping("/person/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String personDeleteQuery(@PathVariable("id") Long id){
        daoProcessing.deleteLover(id);
        return "OK";
    }
}
