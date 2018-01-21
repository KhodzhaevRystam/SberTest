package ru.home.sbertest.my;


import ru.home.sbertest.my.files.TxtFile;
import ru.home.sbertest.my.other.Command;
import ru.home.sbertest.my.repo.Repository;
import ru.home.sbertest.my.util.Delimiter;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ScannerInput {

    private Repository repository;

    public void run() {
        init();
        scanner();
    }

    public void run(String... args) {
        init();
        scannFile(new File(args[0]));
    }

    private void init() {
        repository = new Repository();
        repository.setFileType(new TxtFile());
    }

    private void scannFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s;
            while ((s = reader.readLine()) != null) {
                process(s);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        } catch (IOException e) {
            System.out.println("Ошибка доступа к файлу!");
        }
    }

    private void scanner() {
        String s;
        while (!(s = new Scanner(System.in).nextLine()).equalsIgnoreCase("exit")) {
            process(s);
        }
        System.out.println("Завершение работы программы!");
    }

    private void process(String s) {
        String[] ss = s.split("[\\s]+");
        List<String> input = new ArrayList<>(Arrays.asList(ss));
        String command = input.remove(0);
        Object[] args = input.toArray();
        if (workInFile(command, args)) {
            invokeCommand(command, args);
        }
    }

    private boolean workInFile(String command, Object... args) {
        Boolean f = true;
        if (command.equalsIgnoreCase("load") || command.equalsIgnoreCase("save")) {
            f = false;
            String defpath = "out/";
            File file;
            if (((String) args[0]).matches("^\\S:\\\\{2}[\\S.]+.*")) {
                //если с путем
                file = new File(Delimiter.get((String) args[0]));
            } else {
                //если без указания пути к файлу
                file = new File(defpath + Delimiter.get((String) args[0]));
            }
            args[0] = file;
            invokeCommand(command, args);
        }
        return f;
    }

    private Method invokeCommand(String command, Object... args) {
        Class<?> c = repository.getClass();
        for (Method m : c.getMethods()) {
            Command annotation = m.getAnnotation(Command.class);
            if (annotation != null && annotation.value().equalsIgnoreCase(command)
                    && m.getParameterCount() == args.length) {
                try {
                    m.invoke(repository, castParamToMethod(m, args));
                    return m;
                } catch (Exception e) {
                    System.out.println("Ошибка вызова команды");
                    return m;
                }
            }
        }
        System.out.println("Комманда не поддерживается!!!");
        return null;
    }

    private Object[] castParamToMethod(Method method, Object... args) {
        int i = 0;
        for (Class clazz : method.getParameterTypes()) {
            if (clazz == Integer.class) {
                args[i] = Integer.valueOf((String) args[i++]);
            }
        }
        return args;
    }

}
