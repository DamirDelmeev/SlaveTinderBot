package com.example.shippingbotserver.view;

import com.example.shippingbotserver.entity.Lover;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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


    public void initBytes() throws IOException {
        bytesFromFile = Files.readAllBytes(Paths.get(pathWrite));
    }
}
