package es.daw.foodexpressmvc.exception;

public class ConnectionApiRestException extends RuntimeException{
    public ConnectionApiRestException(String message){
        super("Fallo de comunicación con el API:"+ message);
    }
}
