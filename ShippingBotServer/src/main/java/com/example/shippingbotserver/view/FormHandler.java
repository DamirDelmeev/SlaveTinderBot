package com.example.shippingbotserver.view;

import com.example.shippingbotserver.entity.Lover;
import com.example.shippingbotserver.translation.TextHandler;
import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class FormHandler {
    Lover person;
    String gender;
    String name;
    String title;
    String text;
    File file;
    List<String> list = new ArrayList<>();
    private final TextHandler textHandler;
    private final TextEditor textEditor;

    @Autowired
    public FormHandler(TextHandler textHandler, TextEditor textEditor) {
        this.textHandler = textHandler;
        this.textEditor = textEditor;
    }

    public void init(Lover person) {
        nullInitFields();
        this.person = person;
        this.gender = textHandler.getTranslation(person.getGender().equals("boy") ? "Сударь" : "Сударыня");
        this.name = person.getName().equals("") ? "" : (person.getName().matches("[^a-zA-Z]*") ?
                textHandler.getTranslation(person.getName()) : person.getName());
        this.title = person.getDescription().matches("[^a-zA-Z]*") ?
                textHandler.getTranslation(textEditor.getTitle(person.getDescription())) : textEditor.getTitle(person.getDescription());
        this.text = person.getDescription().matches("[^a-zA-Z]*") ?
                textHandler.getTranslation(textEditor.getText(person.getDescription())) : textEditor.getText(person.getDescription());
    }

    private void nullInitFields() {
        this.person = null;
        this.gender = null;
        this.name = null;
        this.title = null;
        this.text = null;
        list.clear();
    }

    public void meth() {
        String pathIn = "ShippingBotServer/src/main/resources/template/background.jpg";
        String pathOut = "ShippingBotServer/src/main/resources/template/writeSource.jpg";
        signImageImageProcessor(pathIn, pathOut);
        signImageCenter1(pathOut, text);
    }

    public void signImageImageProcessor(String path, String pathOut) {
        ImagePlus image = IJ.openImage(path);
        Font font = new Font("Old Standard TT", Font.BOLD, 31 + (text.length() < 10 ? 13 : 8));

        ImageProcessor ip = image.getProcessor();
        ip.setColor(Color.BLACK);
        ip.setFont(font);
        ip.drawString(title, 50, 100);

        new FileSaver(image).saveAsJpeg(pathOut);
        file = new File(pathOut);
    }

    public void signImageCenter1(String pathOut, String text) {
        ImagePlus image = IJ.openImage(pathOut);
        int size = textEditor.sizeOfFont(text.trim());
        if (size == 0) {
            size = 1;
        }
        int sizeOfString = textEditor.sizeOfString(size);
        text = textEditor.textEditor(text.trim(), sizeOfString);
        list.add(text);
        textEditor.splitToN(list);
        Font font = new Font("Old Standard TT", Font.BOLD, size);
        for (int i = 1; i < list.size() + 1; i++) {
            ImageProcessor ip = image.getProcessor();
            ip.setColor(Color.BLACK);
            ip.setFont(font);
            ip.drawString(list.get(i - 1), 50, 100 + (i * size));
            new FileSaver(image).saveAsJpeg(pathOut);
            file = new File(pathOut);
        }
    }

}
