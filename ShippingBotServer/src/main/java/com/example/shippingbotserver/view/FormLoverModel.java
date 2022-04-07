package com.example.shippingbotserver.view;

import com.example.shippingbotserver.entity.Lover;
import com.example.shippingbotserver.model.LoverModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FormLoverModel {
    final String pathWrite = "ShippingBotServer/src/main/resources/template/writeSource.jpg";
    byte[] bytesFromFile;
    Lover lover;
    String status;

    public FormLoverModel(Lover lover) {
        lover.setGender(lover.getGender().equals("boy") ? "Сударь" : "Сударыня");
        this.lover = lover;
        status = "";
    }

    public FormLoverModel(LoverModel loverModel) {
        lover.setId(loverModel.getId());
        lover.setName(loverModel.getName());
        lover.setGender(loverModel.getGender());
        lover.setDescription(loverModel.getDescription());
        lover.setPreference(loverModel.getPreference());
        status = "";
    }

    public void initBytes() {
        try {
            bytesFromFile = Files.readAllBytes(Paths.get(pathWrite));
        } catch (IOException e) {
            log.debug("log message{}", "InitBytes ERROR: " + e.getMessage());
        }
    }
}
