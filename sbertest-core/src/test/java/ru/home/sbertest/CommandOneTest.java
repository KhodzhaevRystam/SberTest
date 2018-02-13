package ru.home.sbertest;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.home.sbertest.my.ScannerInput;
import ru.home.sbertest.plugin.IRepo;

import java.util.List;

public class CommandOneTest {

    @BeforeClass
    public static void before(){
        IRepo.repo.clear();
    }

    @Test
    public void test1(){
        List<Integer> repo = IRepo.repo;
        ScannerInput scannerInput = new ScannerInput();
        scannerInput.process("add 5");
        Assert.assertTrue(repo.get(0) == 5);
        scannerInput.process("add 15");
        scannerInput.process("add 25");
        scannerInput.process("del 1");
        scannerInput.process("add 35");
        scannerInput.process("add 45");
        Assert.assertTrue(repo.get(3) == 45);
        scannerInput.process("list");
    }

    @Test
    public void test2() {
        ScannerInput scannerInput = new ScannerInput();
        scannerInput.process("save out/123.txt");
        scannerInput.process("list");
    }
}
