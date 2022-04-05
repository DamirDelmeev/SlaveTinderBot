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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    public FormHandler(TextHandler textHandler) {
        this.textHandler = textHandler;
    }

    public void init(Lover person) {
        nullInitFields();
        this.person = person;
        this.gender = textHandler.getTranslation(person.getGender().equals("boy") ? "Сударь" : "Сударыня");
        this.name = person.getName().equals("") ? "" : (person.getName().matches("[^a-zA-Z]*") ?
                textHandler.getTranslation(person.getName()) : person.getName());
        this.title = person.getDescription().matches("[^a-zA-Z]*") ?
                textHandler.getTranslation(getTitle(person.getDescription())) : getTitle(person.getDescription());
        this.text = person.getDescription().matches("[^a-zA-Z]*") ?
                textHandler.getTranslation(getText(person.getDescription())) : getText(person.getDescription());
    }

    private void nullInitFields() {
        this.person = null;
        this.gender = null;
        this.name = null;
        this.title = null;
        this.text = null;
        list.clear();
    }

    private String getText(String description) {
        if (description.contains("\n")) {
            String[] split = description.split("\n");
            return Arrays.stream(split).skip(1).collect(Collectors.joining(" "));
        } else {
            String[] split = description.split(" ");
            return Arrays.stream(split).skip(1).collect(Collectors.joining(" "));
        }
    }

    private String getTitle(String description) {
        if (description.contains("\n")) {
            String[] split = description.split("\n");
            return split[0];
        } else {
            String[] split = description.split(" ");
            return split[0];
        }
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
        int size = sizeOfFont(text.trim());
        if (size == 0) {
            size = 1;
        }
        int sizeOfString = sizeOfString(size);
        text = textEditor(text.trim(), sizeOfString);
        list.add(text);
        splitToN(list);
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

    private int sizeOfString(int len) {
        final double defaultSize = 30.0;
        if (len != 29) {
            int percent = (int) Math.abs((((defaultSize - len) / len) * 100));
            return (int) Math.abs((defaultSize / 100) * (100 + percent));
        }
        return (int) defaultSize;
    }

    private int sizeOfFont(String text) {
        final double maxSizeText = 450.0;
        double sizeStd = 28.0;
        double size = text.length();
        if (text.length() > maxSizeText) {
            int percent = (int) (((size - maxSizeText) / maxSizeText) * 100);
            sizeStd = Math.abs((sizeStd / 100.0) * (100.0 - percent));
        }
        return (int) sizeStd;
    }

    private List<String> splitToN(List<String> lst) {
        String[] sentences;
        for (int i = 0; i < lst.size(); i++) {
            if (containN(lst.get(i))) {
                sentences = lst.get(i).split("\n");
                lst.remove(i);
                for (String sentence : sentences) {
                    lst.add(i++, sentence);
                }
            }
        }
        return lst;
    }

    private boolean containN(String s) {
        return s.split("\n").length > 1;
    }

    private String textEditor(String input, int lenDefault) {
        String sentences = stringEditor(input.trim(), lenDefault);
        StringBuilder str = new StringBuilder();
        return sentences;
    }

    private String[] trimToLen(String s, int len) {
        String[] str;
        String s1 = new String(s);
        for (int j = len; j < s1.length(); j += len) {
            s1 = charAdd(s1, '\n', j);
        }
        str = s1.split("\n");
        for (int i = 1; i < str.length; i++) {
            if (str[i - 1].length() + str[i].length() <= len) {
                str[i - 1] += str[i];
                str = reAlloc(str, i);
                i--;
            }
        }
        return str;
    }

    private String moveN(String s) {
        if (s.charAt(s.length() - 1) == '\n') {
            s = s.substring(0, s.length() - 1);
        }
        char[] arr = s.toCharArray();
        int pos ;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '\n' && arr[i + 1] != ' ' && arr[i - 1] != ' ') {
                pos = i;
                while (true) {
                    --i;
                    if (arr[i] == ' ') {
                        arr[i] = '\n';
                        arr[pos] = 127;
                        i = pos;
                        break;
                    } else if (i == 0) {
                        i = pos;
                        break;
                    }
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : arr) {
            if (c != 127) {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    private String[] reAlloc(String[] s, int pos) {
        String[] res = new String[s.length - 1];
        int j = 0;
        for (int i = 0; i < s.length; i++) {
            if (i != pos) {
                res[j] = s[i];
                j++;
            }
        }
        return res;
    }

    private String stringEditor(String input, int lenDefault) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] strings = trimToLen(input, lenDefault);

        for (String obj : strings) {
            stringBuilder.append(obj).append('\n');
        }
        return moveN(stringBuilder.toString());
    }

    private boolean isLenDefault(String sentence, int lenDefault) {
        int count = 0;
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) == '\n') {
                count = 0;
                continue;
            }
            if (++count > lenDefault)
                return false;
        }
        return true;
    }

    private String charAdd(String s, char ch, int pos) {
        StringBuilder stringBuilder = new StringBuilder();
        if (pos >= s.length()) {
            stringBuilder.append(s);
            stringBuilder.append(ch);
            return stringBuilder.toString();
        }
        stringBuilder.append(s, 0, pos);
        stringBuilder.append(ch);
        stringBuilder.append(s, pos, s.length());
        return stringBuilder.toString();
    }
}
