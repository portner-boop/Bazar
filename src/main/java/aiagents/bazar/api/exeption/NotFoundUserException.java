package aiagents.bazar.api.exeption;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String s) {
        super(s);
    }
}
