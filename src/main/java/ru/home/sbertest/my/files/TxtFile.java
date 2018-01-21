package ru.home.sbertest.my.files;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TxtFile implements IWorkFile {

    @Override
    public void load(File file, String delimiter, List list) {
        FileReader reader;
        try{
            reader = new FileReader(file);
            int c;
            StringBuilder s = new StringBuilder();
            while((c=reader.read())!=-1){
                s.append((char) c);
            }
            list.addAll(Arrays.stream(s.toString().split(delimiter)).map(Integer::valueOf).collect(Collectors.toList()));
        } catch (IOException e) {
            System.out.println("Ошибка загрузки файла!");
        }catch (NumberFormatException nfe){
            System.out.println("Ошибка загрузки данных, не верно указан раздилитель!");
        }
    }

    @Override
    public void save(File file, String data, String delimiter) {
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.append(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка сохранения в файл!");
        }
    }

}
