package org.example.exceptions;

public class InvalidTicketTypeException extends Exception{
    public InvalidTicketTypeException(String msg){
        super(msg);
    }

    public String validateTypeTicket(String nameTicket) throws InvalidTicketTypeException {
        String nameTicketCorrected=" ";
        if (nameTicket.equalsIgnoreCase("a") || nameTicket.equalsIgnoreCase("b")){
            nameTicketCorrected=nameTicket;
        }
        if (nameTicket.length()!=1){
            throw new InvalidTicketTypeException("El ticket solo puede ser A o B");
        }
        if (!nameTicket.equalsIgnoreCase("a") && !nameTicket.equalsIgnoreCase("b")){
            throw new InvalidTicketTypeException("Ticket solo puede ser A o B");
        }
        return nameTicketCorrected;
    }
}
