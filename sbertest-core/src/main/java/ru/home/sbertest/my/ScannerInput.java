package ru.home.sbertest.my;


import org.reflections.Reflections;
import ru.home.sbertest.my.files.TxtFile;
import ru.home.sbertest.my.other.Command;
import ru.home.sbertest.my.other.Commands;
import ru.home.sbertest.my.other.Null;
import ru.home.sbertest.my.util.Delimiter;
import ru.home.sbertest.my.util.FileUtil;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ScannerInput {

    private List<Method> repository;
    private Map<String, Object> clases;

    public ScannerInput() {
        init();
    }

    public void run() {
        init();
        scanner();
    }

    public void run(String... args) {
        init();
        scannFile(new File(args[0]));
    }

    private void init() {
        try {
            repository = new ArrayList<>();
            Set<Class<?>> set = new Reflections("ru.home.sbertest.my.repo").getTypesAnnotatedWith(Commands.class);
            clases = new HashMap<>();
            for (Class c : set) {
                clases.put(c.getName(), c.newInstance());
                repository.addAll(Arrays.asList(c.getDeclaredMethods()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка подгрузки команд!");
        }
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

    public Object process(String s) {
        String[] ss = s.split("[\\s]+");
        List<String> input = new ArrayList<>(Arrays.asList(ss));
        String command = input.remove(0);
        Object[] args = input.toArray();
        if (workInFile(command, args)) {
            return invokeCommand(command, args);
        }
        return null;
    }

    private boolean workInFile(String command, Object... args) {
        Boolean f = true;
        if (command.equalsIgnoreCase("load") || command.equalsIgnoreCase("save") || command.equalsIgnoreCase("save_html")) {
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
            args[0] = file.getAbsolutePath();
            invokeCommand(command, args);
        }
        return f;
    }

    private Object invokeCommand(String command, Object... args) {
        for (Method m : repository) {
            Command annotation = m.getAnnotation(Command.class);
            if (annotation != null && annotation.value().equalsIgnoreCase(command)
                    && m.getParameterCount() == (args = castParamToMethod(m, args)).length) {
                try {
                    return m.invoke(clases.get(m.getDeclaringClass().getName()), args);
                } catch (Exception e) {
                    System.out.println("Ошибка вызова команды");
                }
            }
        }
        System.out.println("Комманда не поддерживается!!!");
        return null;
    }

    private Object[] castParamToMethod(Method method, Object... args) {
        int i = 0;
        int j = 0;
        int countParams = method.getParameterCount();
        Object[] mas = new Object[countParams];
        try {
            for (Parameter p : method.getParameters()) {
                if (p.getAnnotation(Null.class) == null) {
                    if (Integer.class == p.getType()) {
                        String value = (String) args[j++];
                        if (value.matches("^-?\\d+$")) {
                            mas[i++] = Integer.valueOf(value);
                        } else {
                            System.out.println("Недопустимый аргумент для команды " + method.getName() + "!");
                        }
                    } else {
                        mas[i++] = args[j++];
                    }
                } else if (p.getAnnotation(Null.class) != null && countParams > args.length) {
                    mas[i++] = null;

                } else if (p.getAnnotation(Null.class) != null && countParams == args.length) {
                    mas[i++] = args[j++];
                }
            }
            return mas;
        } catch (Exception e) {
            return args;
        }
    }

}
