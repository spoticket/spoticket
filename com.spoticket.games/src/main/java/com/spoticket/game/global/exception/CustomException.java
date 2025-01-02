package com.spoticket.game.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends Exception {

    private final Integer code;
    private final String msg;

}
