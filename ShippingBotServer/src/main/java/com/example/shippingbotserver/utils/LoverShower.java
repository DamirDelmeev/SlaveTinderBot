package com.example.shippingbotserver.utils;

import com.example.shippingbotserver.entity.Lover;
import com.example.shippingbotserver.model.LoverModel;
import com.example.shippingbotserver.repository.LoverModelRepository;
import com.example.shippingbotserver.repository.LoverRepository;
import com.example.shippingbotserver.view.FormLoverModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoverShower {

    public void loverUpdate(Lover lover, Lover orig) {
        lover.setDislikes(orig.getDislikes());
        lover.setLike(orig.getLike());
        lover.setLikeMe(orig.getLikeMe());
        initLover(lover);
    }

    public String getLoverModelStatus(Lover me, LoverModel show) {
        if (me.mutuallyLovers().contains(show)) {
            return "Взаимность";
        } else if (me.getLikeMe().contains(show)) {
            return "Вы любимъ";
        } else if (me.getLike().contains(show)) {
            return "Любимъ вами";
        }
        return "";
    }

    public void formInit(FormLoverModel formLoverModel) {
        try{
            log.debug("log message{}", "converting image to bytes...");
            formLoverModel.initBytes();
            log.debug("log message{}", "converting image to bytes exit success");
        }catch (Exception e){
            log.debug("log message{}", e.getMessage());
        }
    }

    private void initLover(Lover lover) {
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
    }

    public String translateLover(String s){
        switch (s){
            case ("boy") : return "Сударь";
            case ("girl") : return "Сударыня";
            default:return s;
        }
    }

    public LoverModel showMe(Lover lover, LoverModelRepository modelRepository, LoverRepository loverRepository) {
        List<LoverModel> listOfLovers = modelRepository.findAll();
        List<LoverModel> likeOfLover = listOfLovers.stream().filter(l -> lover.getLike().contains(l)).collect(Collectors.toList());
        long count = listOfLovers.stream()
                .filter(l -> !Objects.equals(l.getId(), lover.getId()))
                .filter(l -> isPreference(new LoverModel(lover), l))
                .count();
        if (likeOfLover.size() == count) {
            return new LoverModel(-1L, "", "", "Список пуст\n", "");
        }
        listOfLovers = listOfLovers.stream()
                .filter(l -> !Objects.equals(l.getId(), lover.getId()))
                .filter(l -> !lover.getDislikes().contains(l))
                .filter(l -> !lover.getLike().contains(l))
                .filter(l -> isPreference(new LoverModel(lover), l))
                .collect(Collectors.toList());
        if (listOfLovers.isEmpty()) {
            lover.getDislikes().clear();
            loverRepository.save(lover);
            return showMe(lover, modelRepository, loverRepository);
        }
        return listOfLovers.get(0);
    }

    private boolean isPreference(LoverModel one, LoverModel two) {
        return (one.getPreference().equals("all") || one.getPreference().equals(two.getGender())) &&
                (two.getPreference().equals("all") || two.getPreference().equals(one.getGender()));
    }
}
