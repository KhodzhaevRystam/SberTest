package ru.home.sbertest.my.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HtmlFile implements IWorkFile {
    @Override
    public void load(File file, String delimiter, List list) throws FileOperationException {
        System.out.println("Метод не реализован!");
    }

    @Override
    public void save(File file, String data, String delimiter) throws FileOperationException{
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.append("<html><body><b>" + data + "</b></body></html>");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка сохранения в файл!");
        }
    }
}
