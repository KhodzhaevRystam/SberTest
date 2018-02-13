package ru.home.sbertest.my;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader implements Runnable{

    private static final String PLUGIN_PATH = "./plugins";
    private ScannerInput scannerInput;

    private List<File> lastFiles = new ArrayList<File>();

    public PluginLoader(ScannerInput scannerInput){
        this.scannerInput = scannerInput;
    }

    public List<Class> load(){
        List<Class> classes = new ArrayList<>();
        File file = new File(PLUGIN_PATH);
        if(!file.exists()){
            file.mkdir();
        }
        for(File _f : file.listFiles()){
            if (!lastFiles.contains(_f)) {
                if (_f.getName().endsWith(".jar")) {
                    System.out.println("Найден плагин: " + _f.getName());
                    try {
                        URL jarURL = _f.toURI().toURL();
                        URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
                        JarFile jarFile = new JarFile(_f);
                        Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                        while (jarEntryEnumeration.hasMoreElements()) {
                            JarEntry jarEntry = jarEntryEnumeration.nextElement();
                            String e = jarEntry.getName();
                            if (e.endsWith(".class")) {
                                e = e.replaceAll("/", ".");
                                e = e.replace(".class", "");
                                Class clazz = classLoader.loadClass(e);
                                classes.add(clazz);
                                lastFiles.add(_f);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Ошибка загрузки плагина: " + _f.getName());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return classes;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Class _z : load()) {
                try {
                    scannerInput.getClases().put(_z.getName(), _z.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            scannerInput.intiPlugin();
        }
    }
}
