package com.example.shippingbotserver.dao;

import com.example.shippingbotserver.entity.Lover;
import com.example.shippingbotserver.model.LoverModel;
import com.example.shippingbotserver.repository.LoverModelRepository;
import com.example.shippingbotserver.repository.LoverRepository;
import com.example.shippingbotserver.utils.LoverShower;
import com.example.shippingbotserver.view.FormHandler;
import com.example.shippingbotserver.view.FormLoverModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class DaoProcessing {
    private final LoverRepository repository;
    private final LoverModelRepository modelRepository;
    private final FormHandler formHandler;
    private final LoverShower shower;

    @Autowired
    public DaoProcessing(LoverRepository repository, LoverModelRepository modelRepository, FormHandler formHandler, LoverShower shower) {
        this.repository = repository;
        this.modelRepository = modelRepository;
        this.formHandler = formHandler;
        this.shower = shower;
    }

    public FormLoverModel findLoverById(Long request) {
        Lover person = repository.findById(request).get();
        log.debug("log message{}", "person was received: " + person);
        drawPicture(person);
        log.debug("log message{}", "image was draw");
        person.setName(formHandler.getName());
        FormLoverModel form = new FormLoverModel(person);
        shower.formInit(form);
        return form;
    }

    public FormLoverModel getQuestionnaire(Long id){
        Lover lover = repository.findById(id).get();
        log.debug("log message{}", "person was received: " + lover);
        LoverModel loverModel = shower.showMe(lover, modelRepository, repository);
        drawPicture(new Lover(loverModel));
        log.debug("log message{}", "image was draw");
        Lover res = new Lover(loverModel);
        res.setName(formHandler.getName());
        FormLoverModel formLoverModel = new FormLoverModel(res);
        shower.formInit(formLoverModel);
        return formLoverModel;
    }

    public FormLoverModel getFavorite(Long id, String action){
        boolean increment = action.equalsIgnoreCase("Вправо");
        Lover lover = repository.findById(id).get();
        log.debug("log message{}", "person was received: " + lover);
        LoverModel favorite = lover.getLover(increment);
        String status = shower.getLoverModelStatus(lover, favorite);
        repository.save(lover);
        log.debug("log message{}", "person was updated");
        drawPicture(new Lover(favorite));
        Lover res = new Lover(favorite);
        res.setName(formHandler.getName());
        FormLoverModel formLoverModel = new FormLoverModel(res);
        try{
            formLoverModel.initBytes();
        }catch (Exception ignored){
        }
        formLoverModel.setStatus(status);
        return formLoverModel;
    }

    public FormLoverModel dislike(Lover id){
        Lover lover = repository.findById(id.getId()).get();
        log.debug("log message{}", "person was received: " + lover);
        LoverModel loverModel = shower.showMe(lover, modelRepository, repository);
        if (Objects.equals(loverModel.getId(), -1L)) {
            return new FormLoverModel(new Lover(loverModel));
        }
        lover.getDislikes().add(loverModel);
        repository.save(lover);
        log.debug("log message{}", "person was disliked" + loverModel);
        log.debug("log message{}", "person was updated");
        loverModel = shower.showMe(lover, modelRepository, repository);
        log.debug("log message{}", "Next person was received" + loverModel);
        Lover res = new Lover(loverModel);
        res.setName(formHandler.getName());
        FormLoverModel formLoverModel = getFormLoverModel(loverModel, "", res);
        return formLoverModel;
    }

    public FormLoverModel like(Lover id) {
        Lover lover = repository.findById(id.getId()).get();
        log.debug("log message{}", "person was received: " + lover);
        LoverModel loverModel = shower.showMe(lover, modelRepository, repository);
        if (Objects.equals(loverModel.getId(), -1L)) {
            return new FormLoverModel(new Lover(loverModel));
        }
        lover.getLike().add(loverModel);
        String status = "";
        if (shower.getLoverModelStatus(lover, loverModel).equals("Взаимность")){
            status = "Вы любимы: " + shower.translateLover(loverModel.getGender()) + ", " + loverModel.getName();
        }
        repository.save(lover);
        log.debug("log message{}", "person was liked" + loverModel);
        log.debug("log message{}", "person was updated");
        loverModel = shower.showMe(lover, modelRepository, repository);
        log.debug("log message{}", "Next person was received" + loverModel);
        Lover res = new Lover(loverModel);
        res.setName(formHandler.getName());
        FormLoverModel formLoverModel = getFormLoverModel(loverModel, status, res);
        return formLoverModel;
    }


    public void saveLover(Lover person) {
        if (person.getGender().equals("Сударь")) {
            person.setGender("boy");
        } else {
            person.setGender("girl");
        }
        if (person.getPreference().equals("Сударя")) {
            person.setPreference("boy");
        } else if (person.getPreference().equals("Всех")) {
            person.setPreference("all");
        } else {
            person.setPreference("girl");
        }
        repository.save(person);
        log.debug("log message{}", "person was saved");
    }

    public void deleteLover(Long id) {
        Lover lover = repository.findById(id).get();
        log.debug("log message{}", "person was received: " + lover);
        repository.delete(lover);
        log.debug("log message{}", "person was deleted");
    }


    public void update(Lover lover) {
        Lover orig = repository.findById(lover.getId()).get();
        log.debug("log message{}", "person was received: " + orig);
        shower.loverUpdate(lover, orig);
        repository.save(lover);
        log.debug("log message{}", "person was updated: " + lover);
    }

    private FormLoverModel getFormLoverModel(LoverModel loverModel, String status, Lover res) {
        drawPicture(res);
        FormLoverModel formLoverModel = new FormLoverModel(new Lover(loverModel));
        shower.formInit(formLoverModel);
        formLoverModel.setStatus(status);
        return formLoverModel;
    }

    private void drawPicture(Lover res) {
        formHandler.init(res);
        formHandler.meth();
        log.debug("log message{}", "image was draw");
    }

}
