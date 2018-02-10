package ru.home.sbertest.my.util;

import java.io.File;

public abstract class FileUtil {

    public static File createFile(String file){
        File f = new File(file);
        new File(f.getParent()).mkdirs();
        return f;
    }

}
