package ru.home.sbertest.my;

public class Runner {

    public static void main(String[] args) {
        ScannerInput scannerInput = new ScannerInput();
        if(args.length > 0 && !args[0].isEmpty()){
            scannerInput.run(args);
        }else {
            scannerInput.run();
        }
    }
}
