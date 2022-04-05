package com.ru.liga.shippingbot.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonModel {
    byte[] bytesFromFile;
    Person lover;

    public PersonModel(Person lover) {
        this.lover = lover;
    }

    public String writeToPicture() {
        if (bytesFromFile != null) {
            writeToFile();
        }
        return "ShippingBot/src/main/resources/img/background.jpg";
    }

    private void writeToFile() {
        File out = new File("ShippingBot/src/main/resources/img/background.jpg");
        try (FileOutputStream outputStream = new FileOutputStream((out))) {
            outputStream.write(bytesFromFile);
        } catch (IOException e) {
            throw new RuntimeException("Возникла ошибка при попытке нарисовать анкету.");
        }
    }

    @Override
    public String toString() {
        return lover.getGender() + ", " + lover.getName();
    }
}