package aiagents.bazar.api.exeption.telegramuser;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String s) {
        super(s);
    }
}
