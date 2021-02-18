package com.sh.maleki.mankalah.exceptions;

import lombok.Getter;

@Getter
public enum ExceptionCodes {
    WRONG_TURN("It is not your turn to play."),
    EMPTY_PIT("This pit is empty."),
    KALAH_PIT("This pis is kalah pit."),
    INVALID_GAME("This game is over!"),
    GAME_NOT_FOUND("Game not found.");


    private final String exceptionMessage;
    ExceptionCodes(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
