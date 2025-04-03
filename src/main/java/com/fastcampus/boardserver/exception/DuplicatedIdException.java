package com.fastcampus.boardserver.exception;

public class DuplicatedIdException extends RuntimeException {

    public DuplicatedIdException(String msg) {
        super(msg);
    }
}
