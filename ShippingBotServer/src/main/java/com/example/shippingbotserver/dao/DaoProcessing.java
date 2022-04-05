package com.example.shippingbotserver.dao;

import com.example.shippingbotserver.entity.Lover;
import com.example.shippingbotserver.model.LoverModel;
import com.example.shippingbotserver.repository.LoverModelRepository;
import com.example.shippingbotserver.repository.LoverRepository;
import com.example.shippingbotserver.view.FormHandler;
import com.example.shippingbotserver.view.FormLoverModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DaoProcessing {
    private final LoverRepository repository;
    private final LoverModelRepository modelRepository;
    private final FormHandler formHandler;

    public DaoProcessing(LoverRepository repository, LoverModelRepository modelRepository, FormHandler formHandler) {
        this.repository = repository;
        this.modelRepository = modelRepository;
        this.formHandler = formHandler;
    }

    public FormLoverModel findLoverById(Long request) throws IOException {
        Lover person = repository.findById(request).get();
        formHandler.init(person);
        formHandler.meth();
        person.setName(formHandler.getName());
        FormLoverModel form = new FormLoverModel(person);
        form.initBytes();
        return form;
    }

    public FormLoverModel getQuestionnaire(Long id) throws IOException {
        Lover lover = repository.findById(id).get();
        LoverModel loverModel = showMe(lover);
        formHandler.init(new Lover(loverModel));
        formHandler.meth();
        Lover res = new Lover(loverModel);
        res.setName(formHandler.getName());
        FormLoverModel formLoverModel = new FormLoverModel(res);
        formLoverModel.initBytes();
        return formLoverModel;
    }

    public FormLoverModel getFavorite(Long id, String action) throws IOException {
        boolean increment = action.equalsIgnoreCase("Вправо");
        Lover lover = repository.findById(id).get();
        LoverModel favorite = lover.getLover(increment);
        String status = getLoverModelStatus(lover, favorite);
        repository.save(lover);
        formHandler.init(new Lover(favorite));
        formHandler.meth();
        Lover res = new Lover(favorite);
        res.setName(formHandler.getName());
        FormLoverModel formLoverModel = new FormLoverModel(res);
        formLoverModel.initBytes();
        formLoverModel.setStatus(status);
        System.out.println(status);
        return formLoverModel;
    }

    public FormLoverModel dislike(Lover id) throws IOException {
        Lover lover = repository.findById(id.getId()).get();
        LoverModel loverModel = showMe(lover);
        if (Objects.equals(loverModel.getId(), -1L)) {
            return new FormLoverModel(new Lover(loverModel));
        }
        lover.getDislikes().add(loverModel);
        repository.save(lover);
        loverModel = showMe(lover);
        Lover res = new Lover(loverModel);
        res.setName(formHandler.getName());
        formHandler.init(res);
        formHandler.meth();
        FormLoverModel formLoverModel = new FormLoverModel(new Lover(loverModel));
        formLoverModel.initBytes();
        return formLoverModel;
    }

    public FormLoverModel like(Lover id) throws IOException {
        Lover lover = repository.findById(id.getId()).get();
        LoverModel loverModel = showMe(lover);
        if (Objects.equals(loverModel.getId(), -1L)) {
            return new FormLoverModel(new Lover(loverModel));
        }
        lover.getLike().add(loverModel);
        repository.save(lover);
        loverModel = showMe(lover);
        Lover res = new Lover(loverModel);
        res.setName(formHandler.getName());
        formHandler.init(res);
        formHandler.meth();
        FormLoverModel formLoverModel = new FormLoverModel(new Lover(loverModel));
        formLoverModel.initBytes();
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
    }

    public void deleteLover(Long id) {
        repository.delete(repository.findById(id).get());
    }

    private String getLoverModelStatus(Lover me, LoverModel show) {
        String nameOfLoverModel = show.getName();
        if (me.mutuallyLovers().contains(show)) {
            return "Взаимность";
        } else if (me.getLikeMe().contains(show)) {
            return "Вы любимъ";
        } else if (me.getLike().contains(show)) {
            return "Любимъ вами";
        }
        return "";
    }

    private LoverModel showMe(Lover lover) {
        List<LoverModel> listOfLovers = modelRepository.findAll();
        List<LoverModel> likeOfLover = listOfLovers.stream().filter(l -> lover.getLike().contains(l)).collect(Collectors.toList());
        long count = listOfLovers.stream()
                .filter(l -> !Objects.equals(l.getId(), lover.getId()))
                .filter(l -> isPreference(new LoverModel(lover), l))
                .count();
        if (likeOfLover.size() == count) {
            return new LoverModel(-1L, "", "", "Здесь никого нет", "");
        }
        listOfLovers = listOfLovers.stream()
                .filter(l -> !Objects.equals(l.getId(), lover.getId()))
                .filter(l -> !lover.getDislikes().contains(l))
                .filter(l -> !lover.getLike().contains(l))
                .filter(l -> isPreference(new LoverModel(lover), l))
                .collect(Collectors.toList());
        if (listOfLovers.isEmpty()) {
            return new LoverModel(-1L, "", "", "Здесь никого нет", "");
        }
        return listOfLovers.get(0);
    }

    public void update(Lover lover) {
        Lover orig = repository.findById(lover.getId()).get();
        lover.setDislikes(orig.getDislikes());
        lover.setLike(orig.getLike());
        lover.setLikeMe(orig.getLikeMe());
        if (lover.getGender().equals("Сударь")) {
            lover.setGender("boy");
        } else {
            lover.setGender("girl");
        }
        if (lover.getPreference().equals("Сударя")) {
            lover.setPreference("boy");
        }
        if (lover.getPreference().equals("Сударыню")) {
            lover.setPreference("girl");
        } else {
            lover.setPreference("all");
        }
        repository.save(lover);
    }

    private boolean isPreference(LoverModel one, LoverModel two) {
        return (one.getPreference().equals("all") || one.getPreference().equals(two.getGender())) &&
                (two.getPreference().equals("all") || two.getPreference().equals(one.getGender()));
    }
}
