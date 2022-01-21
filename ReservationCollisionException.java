package com.tjpeisde.onlinebooking.exception;

public class ReservationCollisionException extends RuntimeException{
    public ReservationCollisionException(String message) {
        super(message);
    }
}
