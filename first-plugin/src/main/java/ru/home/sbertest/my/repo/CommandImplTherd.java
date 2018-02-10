package ru.home.sbertest.my.repo;

import ru.home.sbertest.other.Command;
import ru.home.sbertest.other.Commands;
import ru.home.sbertest.other.Null;
import ru.home.sbertest.plugin.IRepo;

import java.util.stream.Collectors;

@Commands
public class CommandImplTherd implements IRepo {

    @Command("list2")
    public void list(@Null String delimiter) {
        System.out.println(String.join("\n",
                repo.stream().map(String::valueOf).collect(Collectors.toList())));
    }
}
