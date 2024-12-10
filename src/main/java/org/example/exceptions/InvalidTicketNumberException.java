package org.example.exceptions;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InvalidTicketNumberException extends Exception{
    public InvalidTicketNumberException(String mensaje){
        super(mensaje);
    }


    public int validateTicketNumber(int number) throws InvalidTicketNumberException{
        int correctValue=0;
        if (number > 2) {
            throw new InvalidTicketNumberException("Número superior a 2.");
        } else if (number <= 0) {
            throw new InvalidTicketNumberException("Los únicos valores permitidos son 1 o 2.");
        } else {
            correctValue=number;
        }
        return correctValue;
    }

}
