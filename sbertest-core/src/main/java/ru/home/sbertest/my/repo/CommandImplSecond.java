package ru.home.sbertest.my.repo;

import ru.home.sbertest.my.files.FileOperationException;
import ru.home.sbertest.my.files.HtmlFile;
import ru.home.sbertest.my.other.Command;
import ru.home.sbertest.my.other.Commands;
import ru.home.sbertest.my.other.Null;
import ru.home.sbertest.my.sorting.Sorting;
import ru.home.sbertest.my.sorting.SortingImpl;
import ru.home.sbertest.my.util.Delimiter;
import ru.home.sbertest.my.util.FileUtil;
import ru.home.sbertest.plugin.IRepo;

import java.util.stream.Collectors;

@Commands
public class CommandImplSecond implements IRepo {

    @Command("save_html")
    public void save(String file, @Null String delimiter) {
        try {
            String data = String.join(Delimiter.get(delimiter),
                    repo.stream().map(String::valueOf).collect(Collectors.toList()));
            new HtmlFile().save(FileUtil.createFile(file), data, delimiter);
            System.out.println("Сохранение файла завершено");
        }catch (FileOperationException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    @Command("sort")
    public void sort(@Null String sortDirection) {
        if(null == sortDirection){
            sortDirection = "ASC";
        }
        Sorting sorting = new SortingImpl();
        sorting.sort(repo, sortDirection.toUpperCase());
    }
}