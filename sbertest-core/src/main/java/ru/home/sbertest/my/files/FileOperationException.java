package ru.home.sbertest.my.files;

public class FileOperationException extends Exception {

    public FileOperationException() {
    }

    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }


}
