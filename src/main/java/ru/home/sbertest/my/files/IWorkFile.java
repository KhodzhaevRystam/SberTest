package ru.home.sbertest.my.files;

import java.io.File;
import java.util.List;

public interface IWorkFile {

    void load(File file, String delimiter, List list);

    void save(File file, String data, String delimiter);

}
