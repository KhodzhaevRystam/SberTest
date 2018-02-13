package ru.home.sbertest.my.repo;

import ru.home.sbertest.my.files.FileOperationException;
import ru.home.sbertest.my.files.TxtFile;

import ru.home.sbertest.my.util.Delimiter;
import ru.home.sbertest.my.util.FileUtil;
import ru.home.sbertest.other.Command;
import ru.home.sbertest.other.Commands;
import ru.home.sbertest.other.Null;
import ru.home.sbertest.plugin.IRepo;

import java.io.File;
import java.util.HashSet;
import java.util.stream.Collectors;

@Commands
public class CommandImlOne implements IRepo {

    @Command("list")
    public void list(@Null String delimiter) {
        System.out.println(String.join(Delimiter.get(delimiter),
                repo.stream().map(String::valueOf).collect(Collectors.toList())));
    }

    @Command("clear")
    public void clear() {
        repo.clear();
    }

    @Command("add")
    public void add(Integer value) {
        if (value != null) {
            repo.add(value);
            System.out.println(String.format("Добавили %d", value));
        }
    }

    @Command("del")
    public void del(Integer index) {
        if (index != null) {
            repo.remove((int) index);
            System.out.println(String.format("Удалили значение по индексу %d", index));
        }
    }

    @Command("del")
    public void del(Integer startIndex, Integer endIndex) {
        if (startIndex != null || endIndex != null) {
            if (startIndex - 1 < endIndex && endIndex < repo.size()) {
                repo.removeAll(repo.subList(startIndex, endIndex));
                System.out.println(String.format("Удалили значения по диапозону индексов с %d по %d", startIndex, endIndex));
            }
        } else {
            System.out.println("Индексы заданны не корректно");
        }
    }

    @Command("find")
    public void find(Integer value) {
        if (value != null) {
            Integer index = repo.indexOf(value);
            if (index == -1) {
                System.out.println(String.format("Значение %d не найдено", value));
            } else {
                System.out.println(String.format("Значение %d найдено в позиции %d", value, index));
            }
        }
    }

    @Command("set")
    public void set(Integer index, Integer value) {
        if (index != null || value != null) {
            repo.set(index, value);
            System.out.println(String.format("Установили значение %d по индексу %d", value, index));
        }
    }

    @Command("get")
    public void get(Integer index) {
        if (index != null) {
            System.out.println(String.format("Значение в позиции %d равно %d", index, repo.get(index)));
        }
    }

    @Command("unique")
    public void unique() {
        repo.clear();
        repo.addAll(new HashSet<>(repo));
        System.out.println("Дубликаты в наборе были удалены");
    }

    @Command("count")
    public void count() {
        System.out.println(String.format("Количество элементов в наборе равно %d ", repo.size()));
    }

    @Command("load")
    public void load(String file, String dilimiter) {
        try {
            new TxtFile().load(new File(file), dilimiter, repo);
            System.out.println("Загрузка файла завершена");
        } catch (FileOperationException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    @Command("save")
    public void save(String file, @Null String delimiter) {
        try {
            String data = String.join(Delimiter.get(delimiter),
                    repo.stream().map(String::valueOf).collect(Collectors.toList()));
            new TxtFile().save(FileUtil.createFile(file), data, delimiter);
            System.out.println("Сохранение файла завершено");
        } catch (FileOperationException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

}
