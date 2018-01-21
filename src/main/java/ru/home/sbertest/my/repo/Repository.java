package ru.home.sbertest.my.repo;


import ru.home.sbertest.my.files.IWorkFile;
import ru.home.sbertest.my.other.Command;
import ru.home.sbertest.my.sorting.Sorting;
import ru.home.sbertest.my.sorting.SortingImpl;
import ru.home.sbertest.my.util.Delimiter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Repository {

    private List<Integer> list;
    private IWorkFile workFile;

    public Repository() {
        this.list = new ArrayList<>();
    }

    public void setFileType(IWorkFile fileType) {
        this.workFile = fileType;
    }

    @Command("list")
    public void listEmpt() {
        list(null);
    }

    @Command("list")
    public void list(String delimiter) {
        System.out.println(String.join(Delimiter.get(delimiter),
                list.stream().map(String::valueOf).collect(Collectors.toList())));
    }

    @Command("clear")
    public void clear() {
        this.list.clear();
    }

    @Command("add")
    public void add(Integer value) {
        this.list.add(value);
        System.out.println(String.format("Добавили %d", value));
    }

    @Command("del")
    public void del(Integer index) {
        list.remove(index);
    }

    @Command("del")
    public void del(Integer startIndex, Integer endIndex) {
        this.list.subList(startIndex, endIndex).clear();
    }

    @Command("find")
    public void find(Integer value) {
        Integer index = this.list.indexOf(value);
        if (index == -1) {
            System.out.println(String.format("Значение %d не найдено", value));
        } else {
            System.out.println(String.format("Значение %d найдено в позиции %d", value, index));
        }
    }

    @Command("set")
    public void set(Integer index, Integer value) {
        this.list.set(index, value);
    }

    @Command("get")
    public Integer get(Integer index) {
        return this.list.get(index);
    }

    @Command("sort")
    public void sort() {
        sort("ASC");
    }

    @Command("sort")
    public void sort(String sortDirection) {
        Sorting sorting = new SortingImpl();
        sorting.sort(this.list, sortDirection.toUpperCase());
    }

    @Command("unique")
    public void unique() {
        this.list = new ArrayList<>(new HashSet<>(this.list));
    }

    @Command("count")
    public void count() {
        System.out.println(this.list.size());
    }

    @Command("load")
    public void load(File file, String dilimiter) {
        workFile.load(file, dilimiter, this.list);
    }

    @Command("save")
    public void save(File file, String delimiter) {
        String data = String.join(Delimiter.get(delimiter),
                list.stream().map(String::valueOf).collect(Collectors.toList()));
        workFile.save(file, data, delimiter);
    }

}
