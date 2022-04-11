package com.example.shippingbotserver.service;

import com.example.shippingbotserver.entity.User;
import com.example.shippingbotserver.view.TextEditor;
import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
@Component
@RequiredArgsConstructor
public class DrawerService {
    private final TextEditor textEditor;
    private String title;
    private String text;

    public byte[] draw(User user) throws IOException {
        String pathIn = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "background.jpg").getPath();
        String pathOut = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "writeSource.jpg").getPath();
        title = textEditor.getTitle(user != null ? user.getDescription() : "Список пуст\n");
        text = textEditor.getText(user != null ? user.getDescription() : "");
        signImageImageProcessor(pathIn, pathOut);
        signImageCenter1(pathOut, text);
        return Files.readAllBytes(Paths.get(pathOut));
    }

    private void signImageImageProcessor(String path, String pathOut) {
        ImagePlus image = IJ.openImage(path);
        Font font = new Font("Old Standard TT", Font.BOLD, 31 + (text.length() < 10 ? 13 : 8));

        drawPicture(image, font, title, 50, 100);
        boolean t = new FileSaver(image).saveAsJpeg(pathOut);
    }


    private void signImageCenter1(String pathOut, String text) {
        List<String> list = new ArrayList<>();
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
            drawPicture(image, font, list.get(i - 1), 50, 100 + (i * size));
//            new FileSaver(image).saveAsJpeg(pathOut);
        }
        new FileSaver(image).saveAsJpeg(pathOut);
    }

    private void drawPicture(ImagePlus image, Font font, String text, int x, int y) {
        ImageProcessor ip = image.getProcessor();
        ip.setColor(Color.BLACK);
        ip.setFont(font);
        ip.drawString(text, x, y);
    }
}
