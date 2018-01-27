package ru.home.sbertest.my.sorting;

import java.util.Collections;
import java.util.List;

public class SortingImpl implements Sorting {
    @Override
    public void sort(List collection, String sortDirection) {
        SortEnum sort = SortEnum.valueOf(sortDirection);
        if (null == sort || sort == SortEnum.ASC) {
            Collections.sort(collection);
            System.out.println(String.format("Сортировка элементов по возрастанию"));
        } else if (sort == SortEnum.DESC) {
            collection.sort(Collections.reverseOrder());
            System.out.println(String.format("Сортировка элементов по убыванию"));
        }
    }
}
